package cn.lunadeer.newbtitle;

import cn.lunadeer.newbtitle.utils.Database;
import cn.lunadeer.newbtitle.utils.Notification;
import cn.lunadeer.newbtitle.utils.XLogger;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
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
        applyCurrentTitle();
    }

    public void updateUsingTitle(Integer title_id) {
        _current_title_id = title_id;
        applyCurrentTitle();
        String sql = "";
        sql += "UPDATE nt_player_using_title ";
        sql += "SET title_id = " + title_id + ", ";
        sql += "updated_at = CURRENT_TIMESTAMP ";
        sql += "WHERE uuid = '" + _player.getUniqueId().toString() + "';";
        Database.query(sql);
        _current_title_id = title_id;
        Notification.info(_player, "成功使用称号: " + _titles.get(title_id).getTitle());
    }

    private void applyCurrentTitle() {
        if (_current_title_id == -1) {
            return;
        }
        if (!_titles.containsKey(_current_title_id)) {
            Notification.error(_player, "你没有这个称号");
            _current_title_id = -1;
            return;
        }
        PlayerTitle title = _titles.get(_current_title_id);
        if (title.isExpired()) {
            Notification.error(_player, "此称号已经过期");
            _current_title_id = -1;
            return;
        }
        _player.sendPlayerListHeader(title.getTitle());
    }

    public void set_coin(Integer coin) {
        _coin = coin;
        String sql = "";
        sql += "UPDATE nt_player_coin ";
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
        sql += "FROM nt_player_title ";
        sql += "WHERE player_uuid = '" + uuid.toString() + "';";
        ResultSet rs = Database.query(sql);
        Map<Integer, PlayerTitle> titles = new HashMap<>();
        try {
            while (rs != null && rs.next()) {
                Integer title_id = rs.getInt("title_id");
                Long expire_at = rs.getLong("expire_at");
                PlayerTitle title = new PlayerTitle(title_id, expire_at);
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
        sql += "FROM nt_player_using_title ";
        sql += "WHERE uuid = '" + uuid.toString() + "';";
        ResultSet rs = Database.query(sql);
        Integer current_title_id = null;
        try {
            if (rs != null && rs.next()) {
                current_title_id = rs.getInt("title_id");
            } else {
                current_title_id = -1;
                sql = "";
                sql += "INSERT INTO nt_player_using_title (uuid, title_id) VALUES (";
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
        sql += "FROM nt_player_coin ";
        sql += "WHERE uuid = '" + uuid.toString() + "';";
        ResultSet rs = Database.query(sql);
        Integer coin = null;
        try {
            if (rs != null && rs.next()) {
                coin = rs.getInt("coin");
            } else {
                coin = 0;
                sql = "";
                sql += "INSERT INTO nt_player_coin (uuid, coin) VALUES (";
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
        // todo 校验是否已有此称号 以及是否已过期
        // todo 如果已则续费 如果未则购买

    }
}
