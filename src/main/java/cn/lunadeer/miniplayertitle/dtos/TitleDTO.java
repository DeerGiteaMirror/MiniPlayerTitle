package cn.lunadeer.miniplayertitle.dtos;

import cn.lunadeer.minecraftpluginutils.ColorParser;
import cn.lunadeer.minecraftpluginutils.databse.DatabaseManager;
import cn.lunadeer.minecraftpluginutils.databse.Field;
import cn.lunadeer.minecraftpluginutils.databse.FieldType;
import cn.lunadeer.miniplayertitle.MiniPlayerTitle;
import net.kyori.adventure.text.TextComponent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static cn.lunadeer.minecraftpluginutils.databse.DatabaseManager.handleDatabaseError;

public class TitleDTO {
    private final Field id = new Field("id", FieldType.INT);
    private final Field title = new Field("title", FieldType.STRING);
    private final Field description = new Field("description", FieldType.STRING);


    public static TitleDTO get(int id) {
        String sql = "";
        sql += "SELECT id, title, description FROM mplt_title WHERE id = " + id + ";";
        try (ResultSet rs = DatabaseManager.instance.query(sql)) {
            if (rs.next()) return getTitleDTO(rs);
        } catch (Exception e) {
            handleDatabaseError("获取称号失败", e, sql);
        }
        return null;
    }

    public static TitleDTO create(String title, String description) {
        String sql = "";
        sql += "INSERT INTO mplt_title (title, description) " +
                "VALUES (?, ?) " +
                "RETURNING " +
                "id, title, description;";
        try (ResultSet rs = DatabaseManager.instance.query(sql, title, description)) {
            if (rs.next()) return getTitleDTO(rs);
        } catch (Exception e) {
            handleDatabaseError("创建称号失败", e, sql);
        }
        return null;
    }

    public boolean delete() {
        String updateSql = "UPDATE mplt_player_info SET using_title_id = -1 WHERE using_title_id = ?;";
        String deleteSql = "DELETE FROM mplt_title WHERE id = ?;";
        try {
            // 执行更新操作
            DatabaseManager.instance.query(updateSql, getId());
            // 执行删除操作
            DatabaseManager.instance.query(deleteSql, getId());
            return true;
        } catch (Exception e) {
            handleDatabaseError("删除称号失败", e, updateSql + " " + deleteSql);
            return false;
        }
    }

    public static List<TitleDTO> getAll() {
        String sql = "";
        sql += "SELECT id, title, description FROM mplt_title;";
        List<TitleDTO> titleDTOs = new ArrayList<>();
        try (ResultSet rs = DatabaseManager.instance.query(sql)) {
            while (rs.next()) {
                titleDTOs.add(getTitleDTO(rs));
            }
        } catch (Exception e) {
            handleDatabaseError("获取称号列表失败", e, sql);
        }
        return titleDTOs;
    }

    public TextComponent getTitleColored() {
        String with_pre_suf = "&#ffffff" + MiniPlayerTitle.config.getPrefix() + getTitleRaw() + "&#ffffff" + MiniPlayerTitle.config.getSuffix();
        return ColorParser.getComponentType(with_pre_suf);
    }

    /**
     * 获取称号的颜色化字符串
     * 需要使用 ChatColor.translateAlternateColorCodes 方法对返回字符串进行处理
     * &#FFFFFF -> &x&f&f&f&f&f
     *
     * @return String
     */
    public String getTitleColoredBukkit() {
        String with_pre_suf = "&#ffffff" + MiniPlayerTitle.config.getPrefix() + getTitleRaw() + "&#ffffff" + MiniPlayerTitle.config.getSuffix();
        return ColorParser.getBukkitType(with_pre_suf);
    }

    public String getTitlePlainText() {
        return ColorParser.getPlainText(getTitleRaw());
    }

    public Integer getId() {
        return (Integer) this.id.value;
    }

    public String getDescription() {
        return (String) this.description.value;
    }

    public String getTitleRaw() {
        return (String) this.title.value;
    }

    public boolean updateTitle(String title) {
        String sql = "";
        sql += "UPDATE mplt_title SET title = ? WHERE id = ?;";
        try (ResultSet rs = DatabaseManager.instance.query(sql, title, getId())) {
            if (rs != null && rs.next()) {
                this.title.value = title;
                return true;
            }
        } catch (Exception e) {
            handleDatabaseError("更新称号失败", e, sql);
        }
        return false;
    }

    public boolean updateDescription(String description) {
        String sql = "";
        sql += "UPDATE mplt_title SET description = ? WHERE id = ?;";
        try (ResultSet rs = DatabaseManager.instance.query(sql, description, getId())) {
            if (rs != null && rs.next()) {
                this.description.value = description;
                return true;
            }
        } catch (Exception e) {
            handleDatabaseError("更新称号失败", e, sql);
        }
        return false;
    }

    private static TitleDTO getTitleDTO(ResultSet rs) throws SQLException {
        TitleDTO titleDTO = new TitleDTO();
        titleDTO.id.value = rs.getInt("id");
        titleDTO.title.value = rs.getString("title");
        titleDTO.description.value = rs.getString("description");
        return titleDTO;
    }
}
