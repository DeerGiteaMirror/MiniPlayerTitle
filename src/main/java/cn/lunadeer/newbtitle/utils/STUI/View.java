package cn.lunadeer.newbtitle.utils.STUI;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;

public class View {
    public enum Slot {
        SUBTITLE,
        LINE_1,
        LINE_2,
        LINE_3,
        LINE_4,
        ACTIONBAR
    }

    protected TextComponent title;
    protected TextComponent subtitle;
    protected TextComponent content_line1;
    protected TextComponent content_line2;
    protected TextComponent content_line3;
    protected TextComponent content_line4;
    protected TextComponent actionbar;

    public void showOn(Player player){
        player.sendMessage(title);
        player.sendMessage(subtitle);
        player.sendMessage(content_line1);
        player.sendMessage(content_line2);
        player.sendMessage(content_line3);
        player.sendMessage(content_line4);
        player.sendActionBar(actionbar);
    }

    public static View create(){
        return new View();
    }

    public View title(String title){
        this.title = Component.text(title);
        return this;
    }

    public View set(Slot line, TextComponent component){
        switch (line){
            case SUBTITLE:
                this.subtitle = component;
                break;
            case LINE_1:
                this.content_line1 = component;
                break;
            case LINE_2:
                this.content_line2 = component;
                break;
            case LINE_3:
                this.content_line3 = component;
                break;
            case LINE_4:
                this.content_line4 = component;
                break;
            case ACTIONBAR:
                this.actionbar = component;
                break;
        }
        return this;
    }

    public View set(Slot line, String component){
        switch (line){
            case SUBTITLE:
                this.subtitle = Component.text(component);
                break;
            case LINE_1:
                this.content_line1 = Component.text(component);
                break;
            case LINE_2:
                this.content_line2 = Component.text(component);
                break;
            case LINE_3:
                this.content_line3 = Component.text(component);
                break;
            case LINE_4:
                this.content_line4 = Component.text(component);
                break;
            case ACTIONBAR:
                this.actionbar = Component.text(component);
                break;
        }
        return this;
    }

    public View set(Slot line, Line component){
        switch (line){
            case SUBTITLE:
                this.subtitle = component.build();
                break;
            case LINE_1:
                this.content_line1 = component.build();
                break;
            case LINE_2:
                this.content_line2 = component.build();
                break;
            case LINE_3:
                this.content_line3 = component.build();
                break;
            case LINE_4:
                this.content_line4 = component.build();
                break;
            case ACTIONBAR:
                this.actionbar = component.build();
                break;
        }
        return this;
    }
}
