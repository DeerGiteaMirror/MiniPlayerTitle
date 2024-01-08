package cn.lunadeer.newbtitle;

import cn.lunadeer.newbtitle.utils.Database;

import java.sql.ResultSet;
import java.util.UUID;

public class PlayerTitle extends Title {
    private Long _expire_at = -1L;
    private final UUID _player_uuid;

    public PlayerTitle(Integer title_id, UUID player_uuid, Long expire_at) {
        super(title_id);
        this._player_uuid = player_uuid;
        this._expire_at = expire_at;
    }

    public static PlayerTitle create(Integer title_id, UUID player_uuid) {
        String sql = "";
        sql += "INSERT INTO nt_player_title (title_id, player_uuid, expire_at) ";
        sql += "VALUES (" + title_id + ", '" + player_uuid.toString() + "', -1) ";
        sql += "RETURNING id;";
        try (ResultSet rs = Database.query(sql)) {
            if (rs != null && rs.next()) {
                return new PlayerTitle(title_id, player_uuid, -1L);
            }
        } catch (Exception e) {
            cn.lunadeer.newbtitle.utils.XLogger.err("PlayerTitle create failed: " + e.getMessage());
        }
        return null;
    }

    public String getExpireAt() {
        if (this._expire_at == -1L) {
            return "永久";
        } else if (this._expire_at < System.currentTimeMillis()) {
            return "已过期";
        } else {
            return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(this._expire_at));
        }
    }

    public Long getExpireAtTimestamp() {
        return this._expire_at;
    }

    public Boolean isExpired() {
        if (this._expire_at == -1L) {
            return false;
        } else {
            return this._expire_at < System.currentTimeMillis();
        }
    }

    public void setExpireAtTimestamp(Long expire_at) {
        this._expire_at = expire_at;
        this.save();
    }

    private void save() {
        String sql = "";
        sql += "UPDATE nt_player_title ";
        sql += "SET expire_at = " + this._expire_at + ", ";
        sql += "updated_at = CURRENT_TIMESTAMP ";
        sql += "WHERE player_uuid = '" + _player_uuid.toString() + "', ";
        sql += "AND title_id = " + this._id + ";";
        Database.query(sql);
    }
}
