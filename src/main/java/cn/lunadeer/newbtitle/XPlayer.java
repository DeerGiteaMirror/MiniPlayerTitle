package cn.lunadeer.newbtitle;

import cn.lunadeer.newbtitle.utils.Database;
import cn.lunadeer.newbtitle.utils.Notification;
import cn.lunadeer.newbtitle.utils.STUI.Button;
import cn.lunadeer.newbtitle.utils.STUI.Line;
import cn.lunadeer.newbtitle.utils.STUI.View;
import cn.lunadeer.newbtitle.utils.XLogger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class XPlayer {
    private final Player _player;
    private Integer _coin;
    private final Map<Integer, PlayerTitle> _titles;
    private Integer _current_title_id = -1;

    public XPlayer(Player player) {
        _player = player;
        _titles = getTitles(player.getUniqueId());
        _current_title_id = getCurrentTitleId(player.getUniqueId());
        _coin = getCoin(player.getUniqueId());
        checkTitleValid();
    }

    public PlayerTitle getTitle() {
        if (_current_title_id == -1) {
            return null;
        }
        return _titles.get(_current_title_id);
    }

    public void openBackpack(Integer page) {
        Map<Integer, PlayerTitle> titles = getTitles(_player.getUniqueId());
        int offset = (page - 1) * 4;
        if (offset >= titles.size() || offset < 0) {
            Notification.error(_player, "页数超出范围");
            return;
        }
        View view = View.create();
        view.title("我的称号");
        for (int i = offset; i < offset + 4; i++) {
            if (i >= titles.size()) {
                break;
            }
            int title_id = (int) titles.keySet().toArray()[i];
            TextComponent idx = Component.text("[" + title_id + "] ");
            PlayerTitle title = titles.get(title_id);
            Line line = Line.create();
            boolean is_using = Objects.equals(title.getId(), _current_title_id);
            Component button = Button.create(is_using ? "卸下" : "使用", "/mplt use " + (is_using ? -1 : title.getId()));
            line.append(idx)
                    .append(title.getTitle())
                    .append(Component.text("有效期至:" + title.getExpireAt()))
                    .append(button);
            view.set(i, line);
        }
        view.set(View.Slot.ACTIONBAR, View.pagination(page, titles.size(), "/mplt list"));
        view.showOn(_player);
    }

    public void updateUsingTitle(Integer title_id) {
        _current_title_id = title_id;
        checkTitleValid();
        if (_current_title_id == -1) {
            return;
        }
        String sql = "";
        sql += "UPDATE mplt_player_using_title ";
        sql += "SET title_id = " + _current_title_id + ", ";
        sql += "updated_at = CURRENT_TIMESTAMP ";
        sql += "WHERE uuid = '" + _player.getUniqueId().toString() + "';";
        Database.query(sql);
        Notification.info(_player, "成功使用称号: ");
        Notification.info(_player, _titles.get(_current_title_id).getTitle());
    }

    private void checkTitleValid() {
        if (_current_title_id == -1) {
            return;
        }
        if (!_titles.containsKey(_current_title_id)) {
            Notification.error(_player, "称号 " + _current_title_id + " 不存在");
            _current_title_id = -1;
            return;
        }
        PlayerTitle title = _titles.get(_current_title_id);
        if (title.isExpired()) {
            Notification.error(_player, "称号已过期");
            Notification.error(_player, title.getTitle());
            _current_title_id = -1;
        }
    }

    public void set_coin(Integer coin) {
        _coin = coin;
        String sql = "";
        sql += "UPDATE mplt_player_coin ";
        sql += "SET coin = " + coin + ", ";
        sql += "updated_at = CURRENT_TIMESTAMP ";
        sql += "WHERE uuid = '" + _player.getUniqueId().toString() + "';";
        Database.query(sql);
    }

    public void add_coin(Integer coin) {
        set_coin(_coin + coin);
    }

    private static Map<Integer, PlayerTitle> getTitles(UUID uuid) {
        String sql = "";
        sql += "SELECT ";
        sql += "title_id, expire_at ";
        sql += "FROM mplt_player_title ";
        sql += "WHERE player_uuid = '" + uuid.toString() + "';";
        Map<Integer, PlayerTitle> titles = new HashMap<>();
        try (ResultSet rs = Database.query(sql)) {
            while (rs != null && rs.next()) {
                Integer title_id = rs.getInt("title_id");
                Long expire_at = rs.getLong("expire_at");
                PlayerTitle title = new PlayerTitle(title_id, uuid, expire_at);
                titles.put(title_id, title);
            }
        } catch (Exception e) {
            XLogger.err("XPlayer getTitles failed: " + e.getMessage());
        }
        return titles;
    }

    private static Integer getCurrentTitleId(UUID uuid) {
        String sql = "";
        sql += "SELECT title_id ";
        sql += "FROM mplt_player_using_title ";
        sql += "WHERE uuid = '" + uuid.toString() + "';";
        Integer current_title_id = null;
        try (ResultSet rs = Database.query(sql)) {
            if (rs != null && rs.next()) {
                current_title_id = rs.getInt("title_id");
            } else {
                current_title_id = -1;
                sql = "";
                sql += "INSERT INTO mplt_player_using_title (uuid, title_id) VALUES (";
                sql += "'" + uuid + "', ";
                sql += current_title_id + ");";
                Database.query(sql);
            }
        } catch (Exception e) {
            XLogger.err("XPlayer getCurrentTitleId failed: " + e.getMessage());
        }
        return current_title_id;
    }

    private static Integer getCoin(UUID uuid) {
        String sql = "";
        sql += "SELECT coin ";
        sql += "FROM mplt_player_coin ";
        sql += "WHERE uuid = '" + uuid.toString() + "';";
        Integer coin = null;
        try (ResultSet rs = Database.query(sql)) {
            if (rs != null && rs.next()) {
                coin = rs.getInt("coin");
            } else {
                coin = 0;
                sql = "";
                sql += "INSERT INTO mplt_player_coin (uuid, coin) VALUES (";
                sql += "'" + uuid + "', ";
                sql += coin + ");";
                Database.query(sql);
            }
        } catch (Exception e) {
            XLogger.err("XPlayer getCoin failed: " + e.getMessage());
        }
        return coin;
    }

    public void buyTitle(SaleTitle title) {
        if (title.isSaleExpired()) {
            Notification.error(_player, "此称号已停止销售");
            return;
        }
        if (title.getAmount() != -1 && title.getAmount() <= 0) {
            Notification.error(_player, "此称号已售罄");
            return;
        }
        if (title.getPrice() > _coin) {
            Notification.error(_player, "你的余额不足");
            return;
        }
        if (!_titles.containsKey(title.getId())) {
            _titles.put(title.getId(), PlayerTitle.create(title.getId(), _player.getUniqueId()));
        } else if (_titles.get(title.getId()).getExpireAtTimestamp() == -1) {
            Notification.warn(_player, "你已经拥有此称号");
            return;
        }
        PlayerTitle title_bought = _titles.get(title.getId());
        set_coin(_coin - title.getPrice());
        SaleTitle.setAmount(title.getId(), title.getAmount() - 1);
        Notification.info(_player, "成功购买称号: ");
        Notification.info(_player, title.getTitle());
        if (title.getDays() == -1) {
            title_bought.setExpireAtTimestamp(-1L);
            return;
        }
        if (title_bought.isExpired()) {
            title_bought.setExpireAtTimestamp(System.currentTimeMillis() + title.getDays() * 24 * 60 * 60 * 1000L);
            Notification.info(_player, title.getTitle());
            Notification.info(_player, "称号已重新激活，有效期至 " + title_bought.getExpireAt());
        } else {
            title_bought.setExpireAtTimestamp(title_bought.getExpireAtTimestamp() + title.getDays() * 24 * 60 * 60 * 1000L);
            Notification.info(_player, title.getTitle());
            Notification.info(_player, "称号已续期至 " + title_bought.getExpireAt());
        }
    }

    public void setTitle(Integer title_id, Long expire_at) {
        if (!_titles.containsKey(title_id)) {
            _titles.put(title_id, PlayerTitle.create(title_id, _player.getUniqueId()));
        }
        PlayerTitle title = _titles.get(title_id);
        title.setExpireAtTimestamp(expire_at);
        Notification.info(_player, title.getTitle());
        Notification.info(_player, "获得称号，有效期至 " + title.getExpireAt());
    }
}
