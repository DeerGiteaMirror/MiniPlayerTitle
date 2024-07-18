package cn.lunadeer.miniplayertitle.dtos;

import cn.lunadeer.minecraftpluginutils.databse.DatabaseManager;
import cn.lunadeer.minecraftpluginutils.databse.Field;
import cn.lunadeer.minecraftpluginutils.databse.FieldType;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static cn.lunadeer.minecraftpluginutils.databse.DatabaseManager.handleDatabaseError;

public class PlayerTitleDTO {
    private final Field id = new Field("id", FieldType.INT);
    private final Field player_uuid = new Field("player_uuid", FieldType.UUID);
    private TitleDTO title;
    private LocalDateTime expire_at;

    public Integer getId() {
        return (Integer) id.value;
    }

    public TitleDTO getTitle() {
        return title;
    }

    public UUID getPlayerUuid() {
        return (UUID) player_uuid.value;
    }

    public LocalDateTime getExpireAt() {
        return expire_at;
    }

    public boolean setExpireAt(LocalDateTime dateTime) {
        String sql = "";
        if (dateTime == null) {
            sql += "UPDATE mplt_player_title SET expire_at_y = -1, expire_at_m = -1, expire_at_d = -1 WHERE id = " + getId() + ";";
        } else {
            sql += "UPDATE mplt_player_title SET expire_at_y = " + dateTime.getYear() + ", expire_at_m = " + dateTime.getMonthValue() + ", expire_at_d = " + dateTime.getDayOfMonth() + " WHERE id = " + getId() + ";";
        }
        try (ResultSet rs = DatabaseManager.instance.query(sql)) {
            return true;
        } catch (Exception e) {
            handleDatabaseError("设置玩家称号过期时间失败", e, sql);
        }
        return false;
    }

    public static PlayerTitleDTO create(UUID player_uuid, TitleDTO title, @Nullable LocalDateTime expire_at) {
        String sql = "";
        sql += "INSERT INTO mplt_player_title (player_uuid, title_id, expire_at_y, expire_at_m, expire_at_d) ";

        if (expire_at == null) {
            sql += "VALUES (?, ? , -1, -1, -1) ";
        } else {
            sql += "VALUES (?, ?, " + expire_at.getYear() + ", " + expire_at.getMonthValue() + ", " + expire_at.getDayOfMonth() + ") ";
        }
        sql += "RETURNING " +
                "id, player_uuid, title_id, expire_at_y, expire_at_m, expire_at_d;";
        try (ResultSet rs = DatabaseManager.instance.query(sql, player_uuid, title.getId())) {
            if (rs.next()) {
                return getRs(rs);
            }
        } catch (Exception e) {
            handleDatabaseError("创建玩家称号失败", e, sql);
        }
        return null;
    }

    public static PlayerTitleDTO get(Integer id) {
        String sql = "";
        sql += "SELECT id, player_uuid, title_id, expire_at_y, expire_at_m, expire_at_d FROM mplt_player_title " +
                "WHERE id = ?;";
        try (ResultSet rs = DatabaseManager.instance.query(sql, id)) {
            if (rs.next()) {
                return getRs(rs);
            }
        } catch (Exception e) {
            handleDatabaseError("获取玩家称号失败", e, sql);
        }
        return null;
    }

    public static PlayerTitleDTO get(UUID player, Integer title) {
        String sql = "";
        sql += "SELECT id, player_uuid, title_id, expire_at_y, expire_at_m, expire_at_d FROM mplt_player_title " +
                "WHERE player_uuid = ? AND title_id = ?;";
        try (ResultSet rs = DatabaseManager.instance.query(sql, player, title)) {
            if (rs.next()) {
                return getRs(rs);
            }
        } catch (Exception e) {
            handleDatabaseError("获取玩家称号失败", e, sql);
        }
        return null;
    }

    private static PlayerTitleDTO getRs(ResultSet rs) throws SQLException {
        PlayerTitleDTO playerTitle = new PlayerTitleDTO();
        playerTitle.id.value = rs.getInt("id");
        playerTitle.player_uuid.value = UUID.fromString(rs.getString("player_uuid"));
        playerTitle.title = TitleDTO.get(rs.getInt("title_id"));
        int y = rs.getInt("expire_at_y");
        int m = rs.getInt("expire_at_m");
        int d = rs.getInt("expire_at_d");
        if (y == -1 && m == -1 && d == -1) {
            playerTitle.expire_at = null;
        } else {
            playerTitle.expire_at = LocalDateTime.of(y, m, d, 0, 0, 0);
        }
        return playerTitle;
    }

    public static List<PlayerTitleDTO> getAllOf(UUID player_uuid) {
        String sql = "";
        sql += "SELECT id, player_uuid, title_id, expire_at_y, expire_at_m, expire_at_d FROM mplt_player_title " +
                "WHERE player_uuid = ?;";
        List<PlayerTitleDTO> playerTitles = new ArrayList<>();
        try (ResultSet rs = DatabaseManager.instance.query(sql, player_uuid)) {
            while (rs.next()) {
                playerTitles.add(getRs(rs));
            }
        } catch (Exception e) {
            handleDatabaseError("获取玩家称号失败", e, sql);
        }
        return playerTitles;
    }

    public boolean isExpired() {
        if (expire_at == null) {
            return false;
        } else {
            return expire_at.isBefore(LocalDateTime.now());
        }
    }
}
