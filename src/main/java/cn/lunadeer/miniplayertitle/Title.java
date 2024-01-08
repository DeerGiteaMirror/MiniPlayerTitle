package cn.lunadeer.miniplayertitle;

import cn.lunadeer.miniplayertitle.utils.Database;
import cn.lunadeer.miniplayertitle.utils.Notification;
import cn.lunadeer.miniplayertitle.utils.STUI.Line;
import cn.lunadeer.miniplayertitle.utils.STUI.View;
import cn.lunadeer.miniplayertitle.utils.XLogger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Title {
    protected Integer _id = null;
    protected String _title;
    protected String _description;
    protected Boolean _enabled;
    JoinConfiguration join = JoinConfiguration.separator(Component.text(" "));

    public static Title create(String title, String description) {
        String sql = "";
        sql += "INSERT INTO mplt_title (title, description, enabled) VALUES (";
        sql += "'" + title + "', ";
        sql += "'" + description + "', ";
        sql += "true ";
        sql += ") RETURNING id;";
        try (ResultSet rs = Database.query(sql)) {
            if (rs != null && rs.next()) {
                int titleId = rs.getInt("id");
                return new Title(titleId);
            }
        } catch (Exception e) {
            XLogger.err("Title create failed: " + e.getMessage());
        }
        return null;
    }

    public static List<Title> all() {
        List<Title> titles = new ArrayList<>();
        String sql = "";
        sql += "SELECT id FROM mplt_title;";
        try (ResultSet rs = Database.query(sql)) {
            if (rs != null) {
                while (rs.next()) {
                    Integer id = rs.getInt("id");
                    titles.add(new Title(id));
                }
            }
        } catch (Exception e) {
            XLogger.err("Title all failed: " + e.getMessage());
        }
        return titles;
    }

    public static void listAllTitle(CommandSender sender, Integer page) {
        List<Title> titles = all();
        if (!(sender instanceof Player)) {
            for (Title title : titles) {
                Notification.info(sender, "[" + title.getId() + "]");
                Notification.info(sender, title.getTitle());
            }
            return;
        }
        Player player = (Player) sender;
        int offset = (page - 1) * 4;
        if (offset >= titles.size() || offset < 0) {
            Notification.error(player, "页数超出范围");
            return;
        }
        View view = View.create();
        view.title("所有称号");
        for (int i = offset; i < offset + 4; i++) {
            if (i >= titles.size()) {
                break;
            }
            TextComponent idx = Component.text("[" + titles.get(i).getId() + "] ");
            Line line = Line.create();
            line.append(idx).append(titles.get(i).getTitle());
            view.set(i, line);
        }
        view.set(View.Slot.ACTIONBAR, View.pagination(page, titles.size(), "/mplt listall"));
        view.showOn(player);
    }

    public Title(Integer id) {
        this._id = id;
        String sql = "";
        sql += "SELECT id, title, description, enabled ";
        sql += "FROM mplt_title ";
        sql += "WHERE id = " + id + ";";
        try (ResultSet rs = Database.query(sql)) {
            if (rs != null && rs.next()) {
                this._title = rs.getString("title");
                this._description = rs.getString("description");
                this._enabled = rs.getBoolean("enabled");
            }
        } catch (Exception e) {
            XLogger.err("Title load failed: " + e.getMessage());
        }
    }

    public static void delete(Integer id) {
        String sql = "";
        sql += "DELETE FROM mplt_title WHERE id = " + id + ";";
        Database.query(sql);
    }

    private void save() {
        String sql = "";
        if (this._id == null) {
            sql += "INSERT INTO mplt_title (title, description, enabled) VALUES (";
            sql += "'" + this._title + "', ";
            sql += "'" + this._description + "', ";
            sql += this._enabled + " ";
            sql += ");";
        } else {
            sql += "UPDATE mplt_title SET ";
            sql += "title = '" + this._title + "', ";
            sql += "description = '" + this._description + "', ";
            sql += "enabled = " + this._enabled + " ";
            sql += "updated_at = CURRENT_TIMESTAMP ";
            sql += "WHERE id = " + this._id + ";";
        }
        Database.query(sql);
    }

    public Integer getId() {
        return this._id;
    }

    public Component getTitle() {
        TextComponent prefix = Component.text(MiniPlayerTitle.config.getPrefix());
        TextComponent suffix = Component.text(MiniPlayerTitle.config.getSuffix());
        String[] parts = this._title.split("&#");
        List<TextComponent> components = new ArrayList<>();
        components.add(prefix);
        for (String part : parts) {
            XLogger.debug(part);
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
        return title_component.build().hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text(this._description)));
    }

    public void setTitle(String title) {
        this._title = title;
        this.save();
    }

    public String getDescription() {
        return this._description;
    }

    public void setDescription(String description) {
        this._description = description;
        this.save();
    }

    public Boolean getEnabled() {
        return this._enabled;
    }

    public void setEnabled(Boolean enabled) {
        this._enabled = enabled;
        this.save();
    }
}
