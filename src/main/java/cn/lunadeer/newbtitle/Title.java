package cn.lunadeer.newbtitle;

import cn.lunadeer.newbtitle.utils.Database;
import cn.lunadeer.newbtitle.utils.XLogger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;

import java.sql.ResultSet;

public class Title {
    protected Integer _id = null;
    protected String _title;
    protected String _description;
    protected Boolean _enabled;
    protected String _created_at;
    protected String _updated_at;
    JoinConfiguration join = JoinConfiguration.separator(Component.text(" "));

    public Title(String title, String description) {
        this._title = title;
        this._description = description;
        this._enabled = true;
        this.save();
    }

    public Title(Integer id) {
        this._id = id;
        String sql = "";
        sql += "SELECT id, title, description, enabled, ";
        sql += "DATE_FORMAT(created_at, '%Y-%m-%d %H:%i:%s') AS created_at, ";
        sql += "DATE_FORMAT(updated_at, '%Y-%m-%d %H:%i:%s') AS updated_at ";
        sql += "FROM nt_title ";
        sql += "WHERE id = " + id + ";";
        ResultSet rs = Database.query(sql);
        try {
            if (rs != null && rs.next()) {
                this._title = rs.getString("title");
                this._description = rs.getString("description");
                this._enabled = rs.getBoolean("enabled");
                this._created_at = rs.getString("created_at");
                this._updated_at = rs.getString("updated_at");
            }
        } catch (Exception e) {
            XLogger.err("Title load failed: " + e.getMessage());
        }
    }

    public static void delete(Integer id) {
        String sql = "";
        sql += "DELETE FROM nt_title WHERE id = " + id + ";";
        Database.query(sql);
    }

    private void save() {
        String sql = "";
        if (this._id == null) {
            sql += "INSERT INTO nt_title (title, description, enabled) VALUES (";
            sql += "'" + this._title + "', ";
            sql += "'" + this._description + "', ";
            sql += this._enabled + " ";
            sql += ");";
        } else {
            sql += "UPDATE nt_title SET ";
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
        String[] parts = this._title.split("&#");
        TextComponent[] components = new TextComponent[parts.length];
        if (parts[0].length() > 0) {
            components[0] = Component.text(parts[0]);
        }
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            String color_str = part.substring(0, 6);
            String text = part.substring(6);
            Color color = new Color(color_str);
            components[i] = Component.text(text, color.getStyle());
        }
        TextComponent prefix = Component.text(NewbTitle.config.getPrefix());
        TextComponent suffix = Component.text(NewbTitle.config.getSuffix());
        components[0] = prefix.append(components[0]);
        components[parts.length - 1] = components[parts.length - 1].append(suffix);
        return Component.join(join, components);
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

    public String getCreatedAt() {
        return this._created_at;
    }

    public String getUpdatedAt() {
        return this._updated_at;
    }
}
