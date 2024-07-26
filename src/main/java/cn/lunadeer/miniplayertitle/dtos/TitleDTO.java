package cn.lunadeer.miniplayertitle.dtos;

import cn.lunadeer.minecraftpluginutils.XLogger;
import cn.lunadeer.minecraftpluginutils.databse.DatabaseManager;
import cn.lunadeer.minecraftpluginutils.databse.Field;
import cn.lunadeer.minecraftpluginutils.databse.FieldType;
import cn.lunadeer.miniplayertitle.Color;
import cn.lunadeer.miniplayertitle.MiniPlayerTitle;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        TextComponent prefix = Component.text(MiniPlayerTitle.config.getPrefix(), new Color("#ffffff").getStyle());
        TextComponent suffix = Component.text(MiniPlayerTitle.config.getSuffix(), new Color("#ffffff").getStyle());
        String[] parts = getTitleRaw().split("&#");
        List<TextComponent> components = new ArrayList<>();
        components.add(prefix);
        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            Color color = new Color("#ffffff");
            String content;
            if (part.length() > 6 && part.substring(0, 6).matches("^[0-9a-fA-F]{6}$")) {
                String color_str = part.substring(0, 6);
                color = new Color("#" + color_str);
                content = part.substring(6);
            } else {
                content = part;
            }
            components.add(Component.text(content, color.getStyle()));
        }
        components.add(suffix);
        TextComponent.Builder title_component = Component.text();
        for (TextComponent component : components) {
            title_component.append(component);
        }
        return title_component.build().hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text(getDescription())));
    }

    /**
     * 获取称号的颜色化字符串
     * 需要使用 ChatColor.translateAlternateColorCodes 方法对返回字符串进行处理
     * &#FFFFFF -> &x&f&f&f&f&f
     *
     * @return String
     */
    public String getTitleColoredBukkit() {
        String title = "&f" + MiniPlayerTitle.config.getPrefix() + getTitleRaw() + "&f" + MiniPlayerTitle.config.getSuffix();
        title = title.replaceAll("&#", "#");
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(title);
        while (matcher.find()) {
            String hexCode = matcher.group();
            StringBuilder builder = new StringBuilder("&x");
            for (char c : hexCode.substring(1).toCharArray()) {
                builder.append('&').append(c);
            }
            title = title.replace(hexCode, builder.toString());
        }
        XLogger.debug("TitleDTO.getTitleColoredBukkit: %s", title);
        return title;
    }

    public String getTitlePlainText() {
        String[] parts = getTitleRaw().split("&#");
        StringBuilder res = new StringBuilder();
        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            String content;
            if (part.length() > 6 && part.substring(0, 6).matches("^[0-9a-fA-F]{6}$")) {
                content = part.substring(6);
            } else {
                content = part;
            }
            res.append(content);
        }
        return res.toString();
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
