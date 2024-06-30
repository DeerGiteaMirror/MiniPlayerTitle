package cn.lunadeer.miniplayertitle.dtos;

import cn.lunadeer.minecraftpluginutils.XLogger;
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

public class TitleDTO {
    private int id;
    private String title;
    private String description;

    public static TitleDTO get(int id) {
        String sql = "";
        sql += "SELECT id, title, description FROM mplt_title WHERE id = " + id + ";";
        try (ResultSet rs = MiniPlayerTitle.database.query(sql)) {
            if (rs.next()) return getTitleDTO(rs);
        } catch (Exception e) {
            MiniPlayerTitle.database.handleDatabaseError("获取称号失败", e, sql);
        }
        return null;
    }

    public static TitleDTO create(String title, String description) {
        String sql = "";
        sql += "INSERT INTO mplt_title (title, description) " +
                "VALUES (?, ?) " +
                "RETURNING " +
                "id, title, description;";
        try (ResultSet rs = MiniPlayerTitle.database.query(sql, title, description)) {
            if (rs.next()) return getTitleDTO(rs);
        } catch (Exception e) {
            MiniPlayerTitle.database.handleDatabaseError("创建称号失败", e, sql);
        }
        return null;
    }

    public boolean delete() {
        String updateSql = "UPDATE mplt_player_info SET using_title_id = -1 WHERE using_title_id = ?;";
        String deleteSql = "DELETE FROM mplt_title WHERE id = ?;";
        try {
            // 执行更新操作
            MiniPlayerTitle.database.query(updateSql, this.id);
            // 执行删除操作
            MiniPlayerTitle.database.query(deleteSql, this.id);
            return true;
        } catch (Exception e) {
            MiniPlayerTitle.database.handleDatabaseError("删除称号失败", e, updateSql + " " + deleteSql);
            return false;
        }
    }

    public static List<TitleDTO> getAll() {
        String sql = "";
        sql += "SELECT id, title, description FROM mplt_title;";
        List<TitleDTO> titleDTOs = new ArrayList<>();
        try (ResultSet rs = MiniPlayerTitle.database.query(sql)) {
            while (rs.next()) {
                titleDTOs.add(getTitleDTO(rs));
            }
        } catch (Exception e) {
            MiniPlayerTitle.database.handleDatabaseError("获取称号列表失败", e, sql);
        }
        return titleDTOs;
    }

    public TextComponent getTitleColored() {
        String title = MiniPlayerTitle.config.getPrefix() + this.title + MiniPlayerTitle.config.getSuffix();
        String[] parts = title.split("&#");
        List<TextComponent> components = new ArrayList<>();
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
        TextComponent.Builder title_component = Component.text();
        for (TextComponent component : components) {
            title_component.append(component);
        }
        return title_component.build().hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text(this.description)));
    }

    /**
     * 获取称号的颜色化字符串
     * 需要使用 translateAlternateColorCodes 方法对返回字符串进行处理
     * &#FFFFFF -> &x&f&f&f&f&f
     *
     * @return String
     */
    public String getTitleColoredBukkit() {
        String title = MiniPlayerTitle.config.getPrefix() + this.title + MiniPlayerTitle.config.getSuffix();
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
        String[] parts = this.title.split("&#");
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
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean updateTitle(String title) {
        String sql = "";
        sql += "UPDATE mplt_title SET title = ? WHERE id = ?;";
        try (ResultSet rs = MiniPlayerTitle.database.query(sql, title, this.id)) {
            if (rs != null && rs.next()) {
                this.title = title;
                return true;
            }
        } catch (Exception e) {
            MiniPlayerTitle.database.handleDatabaseError("更新称号失败", e, sql);
        }
        return false;
    }

    public boolean updateDescription(String description) {
        String sql = "";
        sql += "UPDATE mplt_title SET description = ? WHERE id = ?;";
        try (ResultSet rs = MiniPlayerTitle.database.query(sql, description, this.id)) {
            if (rs != null && rs.next()) {
                this.description = description;
                return true;
            }
        } catch (Exception e) {
            MiniPlayerTitle.database.handleDatabaseError("更新称号失败", e, sql);
        }
        return false;
    }

    private static TitleDTO getTitleDTO(ResultSet rs) throws SQLException {
        TitleDTO titleDTO = new TitleDTO();
        titleDTO.id = rs.getInt("id");
        titleDTO.title = rs.getString("title");
        titleDTO.description = rs.getString("description");
        return titleDTO;
    }
}
