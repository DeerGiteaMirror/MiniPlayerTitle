package cn.lunadeer.miniplayertitle.utils.STUI;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static cn.lunadeer.miniplayertitle.utils.STUI.ViewStyles.main_color;

public class View {
    protected TextComponent title_decorate = Component.text("===========", main_color);
    protected TextComponent sub_title_decorate = Component.text("=====", main_color);
    protected TextComponent line_decorate = Component.text("- ", main_color);
    protected TextComponent action_decorate = Component.text("> ", main_color);
    protected TextComponent title = Component.text("       ");
    protected TextComponent subtitle = Component.text("       ");
    protected List<TextComponent> content_lines = new ArrayList<>();
    protected TextComponent actionbar = Component.text("       ");
    protected TextComponent bottom_decorate = Component.text("=================================", main_color);

    public void showOn(Player player) {
        player.sendMessage(Component.text().append(title_decorate).append(title).append(title_decorate).build());
        player.sendMessage(Component.text().append(sub_title_decorate).append(subtitle).build());
        for (TextComponent content_line : content_lines) {
            player.sendMessage(Component.text().append(line_decorate).append(content_line).build());
        }
        player.sendMessage(Component.text().append(action_decorate).append(actionbar).build());
        player.sendMessage(bottom_decorate);
        player.sendMessage(Component.text("     "));
    }

    public static View create() {
        return new View();
    }

    public View title(String title) {
        this.title = Component.text(title);
        return this;
    }

    public View title(TextComponent title) {
        this.title = title;
        return this;
    }

    public View subtitle(String subtitle) {
        this.subtitle = Component.text(subtitle);
        return this;
    }

    public View subtitle(TextComponent subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public View actionBar(TextComponent actionbar) {
        this.actionbar = actionbar;
        return this;
    }

    public View actionBar(String actionbar) {
        this.actionbar = Component.text(actionbar);
        return this;
    }

    public View actionBar(Line actionbar) {
        this.actionbar = actionbar.build();
        return this;
    }

    public View addLine(TextComponent component) {
        this.content_lines.add(component);
        return this;
    }

    public View addLine(String component) {
        this.content_lines.add(Component.text(component));
        return this;
    }

    public View addLine(Line component) {
        this.content_lines.add(component.build());
        return this;
    }
}
