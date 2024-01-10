package cn.lunadeer.miniplayertitle;

import cn.lunadeer.miniplayertitle.utils.Database;
import cn.lunadeer.miniplayertitle.utils.Notification;
import cn.lunadeer.miniplayertitle.utils.STUI.Button;
import cn.lunadeer.miniplayertitle.utils.STUI.Line;
import cn.lunadeer.miniplayertitle.utils.STUI.ListView;
import cn.lunadeer.miniplayertitle.utils.Time;
import cn.lunadeer.miniplayertitle.utils.XLogger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.util.*;

public class XPlayer {
    private final Player _player;
    private Integer _coin;
    private final Map<Integer, PlayerTitle> _titles;
    private Integer _current_title_id = -1;

    public XPlayer(Player player) {
        _player = player;
        _titles = getTitles(player.getUniqueId());
        getInfo();
        checkTitleValid();
    }

    public PlayerTitle getTitle() {
        if (_current_title_id == -1) {
            return null;
        }
        return _titles.get(_current_title_id);
    }

    public void openBackpack(Integer page) {
        Collection<PlayerTitle> titles = getTitles(_player.getUniqueId()).values();
        ListView view = ListView.create(5, "/mplt list");
        view.title("我的称号");
        for (PlayerTitle title : titles) {
            int title_id = title.getId();
            TextComponent idx = Component.text("[" + title_id + "] ");
            Line line = Line.create();
            boolean is_using = Objects.equals(title.getId(), _current_title_id);
            Component button = Button.create(is_using ? "卸下" : "使用", "/mplt use " + (is_using ? -1 : title.getId()));
            line.append(idx)
                    .append(title.getTitle())
                    .append(Component.text("有效期至:" + title.getExpireAtStr()))
                    .append(button);
            view.add(line);
        }
        view.showOn(_player, page);
    }

    public void updateUsingTitle(Integer title_id) {
        _current_title_id = title_id;
        checkTitleValid();
        String sql = "";
        sql += "UPDATE mplt_player_info ";
        sql += "SET using_title_id = " + _current_title_id + ", ";
        sql += "updated_at = CURRENT_TIMESTAMP ";
        sql += "WHERE uuid = '" + _player.getUniqueId() + "';";
        Database.query(sql);
        if (_current_title_id == -1) {
            Notification.info(_player, "成功卸下称号");
            return;
        }
        Notification.info(_player, Component.text("成功使用称号: ").append(_titles.get(_current_title_id).getTitle()));
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
            Notification.error(_player, title.getTitle().append(Component.text(" 称号已过期")));
            _current_title_id = -1;
        }
    }

    public void set_coin(Integer coin) {
        _coin = coin;
        String sql = "";
        sql += "UPDATE mplt_player_info ";
        sql += "SET coin = " + coin + ", ";
        sql += "updated_at = CURRENT_TIMESTAMP ";
        sql += "WHERE uuid = '" + _player.getUniqueId().toString() + "';";
        Database.query(sql);
    }

    public void add_coin(Integer coin) {
        set_coin(_coin + coin);
    }

    public Integer get_coin() {
        return _coin;
    }

    public static Integer getCoin(Player player) {
        XPlayer xplayer = new XPlayer(player);
        return xplayer.get_coin();
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

    private void getInfo() {
        UUID uuid = _player.getUniqueId();
        String sql = "";
        sql += "SELECT coin, using_title_id ";
        sql += "FROM mplt_player_info ";
        sql += "WHERE uuid = '" + uuid.toString() + "';";
        this._coin = MiniPlayerTitle.config.getDefaultCoin();
        this._current_title_id = -1;
        try (ResultSet rs = Database.query(sql)) {
            if (rs != null && rs.next()) {
                this._coin = rs.getInt("coin");
                this._current_title_id = rs.getInt("using_title_id");
            } else {
                sql = "";
                sql += "INSERT INTO mplt_player_info (uuid, coin, using_title_id) VALUES (";
                sql += "'" + uuid + "', ";
                sql += this._coin + ", ";
                sql += this._current_title_id + ");";
                Database.query(sql);
            }
        } catch (Exception e) {
            XLogger.err("XPlayer getInfo failed: " + e.getMessage());
        }
    }

    public void buyTitle(SaleTitle title) {
        if (title.isSaleExpired() || title.getDays() == 0) {
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
        PlayerTitle title_bought = null;
        if (_titles.containsKey(title.getId())) {
            if (!_titles.get(title.getId()).isExpired()) {
                Notification.warn(_player, "你已经拥有此称号");
                return;
            }
            title_bought = _titles.get(title.getId());
        } else {
            title_bought = PlayerTitle.create(title.getId(), _player.getUniqueId());
        }
        if (title_bought == null) {
            Notification.error(_player, "购买失败");
            return;
        }
        set_coin(_coin - title.getPrice());
        SaleTitle.setAmount(title.getId(), title.getAmount() - 1);
        Notification.info(_player, Component.text("成功购买称号: ").append(title.getTitle()));
        Notification.info(_player, "花费: " + title.getPrice() + "称号币，余额: " + _coin + "称号币");

        if (title.getDays() == -1) {
            title_bought.setExpireAt(-1L);
        } else {
            Long timestamp = System.currentTimeMillis() + title.getDays() * 24 * 60 * 60 * 1000L;
            title_bought.setExpireAt((long) Time.getFromTimestamp(timestamp));
        }
        Notification.info(_player, title.getTitle().append(Component.text(" 已购买至 " + title_bought.getExpireAtStr())));
    }

    public void custom(String title_str){
        if (this.get_coin() < MiniPlayerTitle.config.getCustomCost()) {
            Notification.error(this._player, "称号币不足");
            return;
        }
        List<String> exist_titles = new ArrayList<>();
        for (Title title : Title.all()) {
            exist_titles.add(title.getTitleContent());
        }
        Title title = Title.create(title_str, this._player.getName() + "的自定义称号");
        if (title == null) {
            Notification.error(this._player, "创建称号失败");
            return;
        }
        if (exist_titles.contains(title.getTitleContent())) {
            Notification.error(this._player, "已存在同名称号");
            Title.delete(title.getId());
            return;
        }
        PlayerTitle playerTitle = PlayerTitle.create(title.getId(), this._player.getUniqueId());
        if (playerTitle == null) {
            Notification.error(this._player, "创建称号失败");
            Title.delete(title.getId());
            return;
        }
        playerTitle.setExpireAt(-1L);
        this.set_coin(this.get_coin() - MiniPlayerTitle.config.getCustomCost());
        Notification.info(this._player, Component.text("成功创建自定义称号: ").append(title.getTitle()));
        Notification.info(this._player, "花费: " + MiniPlayerTitle.config.getCustomCost() + "称号币，余额: " + this.get_coin() + "称号币");
    }
}
