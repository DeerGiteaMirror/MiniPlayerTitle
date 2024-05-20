package cn.lunadeer.miniplayertitle.dtos;

import cn.lunadeer.miniplayertitle.Color;
import cn.lunadeer.miniplayertitle.MiniPlayerTitle;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
                "VALUES ('" + title + "', '" + description + "') " +
                "RETURNING " +
                "id, title, description;";
        try (ResultSet rs = MiniPlayerTitle.database.query(sql)) {
            if (rs.next()) return getTitleDTO(rs);
        } catch (Exception e) {
            MiniPlayerTitle.database.handleDatabaseError("创建称号失败", e, sql);
        }
        return null;
    }

    public static boolean delete(int id) {
        String sql = "";
        sql += "DELETE FROM mplt_title WHERE id = " + id + ";";
        try (ResultSet rs = MiniPlayerTitle.database.query(sql)) {
            if (rs != null && rs.next()) {
                return true;
            }
        } catch (Exception e) {
            MiniPlayerTitle.database.handleDatabaseError("删除称号失败", e, sql);
        }
        return false;
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
        TextComponent prefix = Component.text(MiniPlayerTitle.config.getPrefix());
        TextComponent suffix = Component.text(MiniPlayerTitle.config.getSuffix());
        String[] parts = this.title.split("&#");
        List<TextComponent> components = new ArrayList<>();
        components.add(prefix);
        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            // match hex regx ^[0-9a-fA-F]{6}$
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
        return title_component.build().hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text(this.description)));
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
        sql += "UPDATE mplt_title SET title = '" + title + "' WHERE id = " + this.id + ";";
        try (ResultSet rs = MiniPlayerTitle.database.query(sql)) {
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
        sql += "UPDATE mplt_title SET description = '" + description + "' WHERE id = " + this.id + ";";
        try (ResultSet rs = MiniPlayerTitle.database.query(sql)) {
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
