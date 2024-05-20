package cn.lunadeer.miniplayertitle.dtos;

import cn.lunadeer.miniplayertitle.MiniPlayerTitle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PlayerInfoDTO {
    private UUID uuid;
    private Integer coin;
    private TitleDTO using_title;

    public static PlayerInfoDTO get(UUID uuid) {
        String sql = "";
        sql = "SELECT uuid, coin, using_title_id FROM mplt_player_info WHERE uuid = '" + uuid.toString() + "';";
        try (ResultSet rs = MiniPlayerTitle.database.query(sql)) {
            if (rs.next()) return getPlayerInfoDTO(rs);
            else return create(uuid);
        } catch (Exception e) {
            MiniPlayerTitle.database.handleDatabaseError("获取玩家信息失败", e, sql);
        }
        return null;
    }

    private static PlayerInfoDTO create(UUID uuid) {
        String sql = "";
        sql = "INSERT INTO mplt_player_info (uuid, coin) " +
                "VALUES ('" + uuid.toString() + "', " + MiniPlayerTitle.config.getDefaultCoin() + ") " +
                "RETURNING " +
                "uuid, coin, using_title_id " +
                "ON CONFLICT (uuid) DO NOTHING;";
        try (ResultSet rs = MiniPlayerTitle.database.query(sql)) {
            if (rs.next()) return getPlayerInfoDTO(rs);
        } catch (Exception e) {
            MiniPlayerTitle.database.handleDatabaseError("创建玩家信息失败", e, sql);
        }
        return null;
    }

    private static PlayerInfoDTO getPlayerInfoDTO(ResultSet rs) throws SQLException {
        PlayerInfoDTO playerInfoDTO = new PlayerInfoDTO();
        playerInfoDTO.uuid = UUID.fromString(rs.getString("uuid"));
        playerInfoDTO.coin = rs.getInt("coin");
        playerInfoDTO.using_title = TitleDTO.get(rs.getInt("using_title_id"));
        return playerInfoDTO;
    }

    public Integer getCoin() {
        return coin;
    }

    public TitleDTO getUsingTitle() {
        return using_title;
    }

    public boolean setUsingTitle(TitleDTO title) {
        String sql = "";
        sql = "UPDATE mplt_player_info SET using_title_id = " + title.getId() + " WHERE uuid = '" + uuid.toString() + "';";
        try (ResultSet rs = MiniPlayerTitle.database.query(sql)) {
            if (rs != null && rs.next()) {
                using_title = title;
                return true;
            }
        } catch (Exception e) {
            MiniPlayerTitle.database.handleDatabaseError("设置玩家使用称号失败", e, sql);
        }
        return false;
    }

    public boolean addCoin(Integer coin) {
        return setCoin(this.coin + coin);
    }

    public boolean setCoin(Integer coin) {
        String sql = "";
        sql = "UPDATE mplt_player_info SET coin = " + coin + " WHERE uuid = '" + uuid.toString() + "';";
        try (ResultSet rs = MiniPlayerTitle.database.query(sql)) {
            if (rs != null && rs.next()) {
                this.coin = coin;
                return true;
            }
        } catch (Exception e) {
            MiniPlayerTitle.database.handleDatabaseError("设置玩家金币失败", e, sql);
        }
        return false;
    }

}
