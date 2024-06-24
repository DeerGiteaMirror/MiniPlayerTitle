package cn.lunadeer.miniplayertitle.dtos;

import cn.lunadeer.minecraftpluginutils.VaultConnect;
import cn.lunadeer.minecraftpluginutils.XLogger;
import cn.lunadeer.miniplayertitle.MiniPlayerTitle;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerInfoDTO {
    private UUID uuid;
    private Double coin;
    private TitleDTO using_title;
    private String last_use_name;

    public static PlayerInfoDTO get(UUID uuid) {
        String sql = "";
        sql = "SELECT uuid, coin_d, using_title_id, last_use_name FROM mplt_player_info WHERE uuid = ?;";
        try (ResultSet rs = MiniPlayerTitle.database.query(sql, uuid)) {
            if (rs.next()) return getPlayerInfoDTO(rs);
            else return null;
        } catch (Exception e) {
            MiniPlayerTitle.database.handleDatabaseError("获取玩家信息失败", e, sql);
        }
        return null;
    }

    public static PlayerInfoDTO get(Player player) {
        PlayerInfoDTO info = get(player.getUniqueId());
        if (info == null) {
            info = create(player);
            if (info == null) {
                XLogger.err("创建玩家信息时失败");
                return null;
            }
        } else {
            info = updateName(player);
            if (info == null) {
                XLogger.err("更新玩家名称时失败");
                return null;
            }
        }
        return info;
    }

    public static PlayerInfoDTO get(String name) {
        String sql = "";
        sql = "SELECT uuid, coin_d, using_title_id, last_use_name FROM mplt_player_info WHERE last_use_name = ?;";
        try (ResultSet rs = MiniPlayerTitle.database.query(sql, name)) {
            if (rs.next()) return getPlayerInfoDTO(rs);
            else return null;
        } catch (Exception e) {
            MiniPlayerTitle.database.handleDatabaseError("获取玩家信息失败", e, sql);
        }
        return null;
    }

    private static PlayerInfoDTO create(Player player) {
        String sql = "";
        sql = "INSERT INTO mplt_player_info (uuid, coin_d, last_use_name) " +
                "VALUES (?, ?, ?) " +
                "ON CONFLICT DO NOTHING;";
        try (ResultSet rs = MiniPlayerTitle.database.query(sql, player.getUniqueId(), MiniPlayerTitle.config.getDefaultCoin(), player.getName())) {
            return get(player.getUniqueId());
        } catch (Exception e) {
            MiniPlayerTitle.database.handleDatabaseError("创建玩家信息失败", e, sql);
        }
        return null;
    }

    private static PlayerInfoDTO updateName(Player player) {
        String sql = "";
        sql = "UPDATE mplt_player_info SET last_use_name = ? WHERE uuid = ?;";
        try (ResultSet rs = MiniPlayerTitle.database.query(sql, player.getName(), player.getUniqueId())) {
            return get(player.getUniqueId());
        } catch (Exception e) {
            MiniPlayerTitle.database.handleDatabaseError("更新玩家名称失败", e, sql);
        }
        return null;
    }

    private static PlayerInfoDTO getPlayerInfoDTO(ResultSet rs) throws SQLException {
        PlayerInfoDTO playerInfoDTO = new PlayerInfoDTO();
        playerInfoDTO.uuid = UUID.fromString(rs.getString("uuid"));
        playerInfoDTO.coin = rs.getDouble("coin_d");
        playerInfoDTO.using_title = TitleDTO.get(rs.getInt("using_title_id"));
        playerInfoDTO.last_use_name = rs.getString("last_use_name");
        return playerInfoDTO;
    }

    public Double getCoin() {
        if (MiniPlayerTitle.config.isExternalEco()) {
            Player player = MiniPlayerTitle.instance.getServer().getPlayer(uuid);
            return VaultConnect.instance.getBalance(player);
        }
        return coin;
    }

    public TitleDTO getUsingTitle() {
        return using_title;
    }

    public String getLastUseName() {
        return last_use_name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean setUsingTitle(@Nullable TitleDTO title) {
        String sql = "";
        sql = "UPDATE mplt_player_info SET using_title_id = ? WHERE uuid = ?;";
        try (ResultSet rs = MiniPlayerTitle.database.query(sql, title == null ? -1 : title.getId(), uuid)) {
            this.using_title = title == null ? TitleDTO.get(-1) : title;
            return true;
        } catch (Exception e) {
            MiniPlayerTitle.database.handleDatabaseError("设置玩家使用称号失败", e, sql);
        }
        return false;
    }

    public boolean addCoin(double coin) {
        if (MiniPlayerTitle.config.isExternalEco()) {
            Player player = MiniPlayerTitle.instance.getServer().getPlayer(uuid);
            VaultConnect.instance.depositPlayer(player, coin);
            return true;
        }
        return setCoin(this.coin + coin);
    }

    public boolean setCoin(double coin) {
        if (MiniPlayerTitle.config.isExternalEco()) {
            Player player = MiniPlayerTitle.instance.getServer().getPlayer(uuid);
            double balance = VaultConnect.instance.getBalance(player);
            if (balance < coin) {
                VaultConnect.instance.depositPlayer(player, coin - balance);
            } else {
                VaultConnect.instance.withdrawPlayer(player, balance - coin);
            }
            return true;
        }
        String sql = "";
        sql = "UPDATE mplt_player_info SET coin_d = ? WHERE uuid = ?;";
        try (ResultSet rs = MiniPlayerTitle.database.query(sql, coin, uuid)) {
            this.coin = coin;
            return true;
        } catch (Exception e) {
            MiniPlayerTitle.database.handleDatabaseError("设置玩家金币失败", e, sql);
        }
        return false;
    }

    public static List<String> playerNameList() {
        String sql = "";
        sql = "SELECT last_use_name FROM mplt_player_info;";
        List<String> names = new ArrayList<>();
        try (ResultSet rs = MiniPlayerTitle.database.query(sql)) {
            while (rs.next()) {
                names.add(rs.getString("last_use_name"));
            }
        } catch (Exception e) {
            MiniPlayerTitle.database.handleDatabaseError("获取玩家名称列表失败", e, sql);
        }
        return names;
    }

}
