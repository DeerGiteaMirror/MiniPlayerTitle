package cn.lunadeer.newbtitle.utils.STUI;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class View {
    public enum Slot {
        SUBTITLE,
        LINE_1,
        LINE_2,
        LINE_3,
        LINE_4,
        ACTIONBAR
    }

    public static TextColor main_color = TextColor.color(0, 179, 255);
    public static TextColor sub_color = TextColor.color(143, 143, 143);
    public static TextColor action_color = TextColor.color(251, 255, 139);

    protected TextComponent title_decorate = Component.text("===========", main_color);
    protected TextComponent sub_title_decorate = Component.text("=====", main_color);
    protected TextComponent line_decorate = Component.text("- ", main_color);
    protected TextComponent action_decorate = Component.text("> ", main_color);
    protected TextComponent title = Component.text("       ");
    protected TextComponent subtitle = Component.text("       ");
    protected TextComponent content_line1 = Component.text("       ");
    protected TextComponent content_line2 = Component.text("       ");
    protected TextComponent content_line3 = Component.text("       ");
    protected TextComponent content_line4 = Component.text("       ");
    protected TextComponent actionbar = Component.text("       ");
    protected TextComponent bottom_decorate = Component.text("=================================", main_color);

    public static TextComponent pagination(int page, int item_size, String command) {
        // 第 x/y 页 [上一页] [下一页]
        int page_size = 4;
        int page_count = (int) Math.ceil((double) item_size / page_size);
        if (page_count == 0) {
            page_count = 1;
        }
        List<Component> componentList = new ArrayList<>();
        componentList.add(Component.text("第 ", main_color));
        componentList.add(Component.text(page, sub_color));
        componentList.add(Component.text("/", main_color));
        componentList.add(Component.text(page_count, sub_color));
        componentList.add(Component.text(" 页 ", main_color));
        if (page > 1) {
            componentList.add(Button.create("[上一页]", command + " " + (page - 1)));
        }
        if (page < page_count) {
            componentList.add(Button.create("[下一页]", command + " " + (page + 1)));
        }
        TextComponent.Builder builder = Component.text();
        for (Component component : componentList) {
            builder.append(component);
        }
        return builder.build();
    }

    public void showOn(Player player) {
        player.sendMessage(Component.text().append(title_decorate).append(title).append(title_decorate).build());
        player.sendMessage(Component.text().append(sub_title_decorate).append(subtitle).build());
        player.sendMessage(Component.text().append(line_decorate).append(content_line1).build());
        player.sendMessage(Component.text().append(line_decorate).append(content_line2).build());
        player.sendMessage(Component.text().append(line_decorate).append(content_line3).build());
        player.sendMessage(Component.text().append(line_decorate).append(content_line4).build());
        player.sendMessage(Component.text().append(action_decorate).append(actionbar).build());
        player.sendMessage(bottom_decorate);
    }

    public static View create() {
        return new View();
    }

    public View title(String title) {
        this.title = Component.text(title);
        return this;
    }

    public View set(Slot line, TextComponent component) {
        switch (line) {
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

    public View set(Slot line, String component) {
        switch (line) {
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

    public View set(int index, Line component) {
        if (index % 4 == 0) {
            this.set(View.Slot.LINE_1, component);
        } else if (index % 4 == 1) {
            this.set(View.Slot.LINE_2, component);
        } else if (index % 4 == 2) {
            this.set(View.Slot.LINE_3, component);
        } else if (index % 4 == 3) {
            this.set(View.Slot.LINE_4, component);
        } else {
            throw new IllegalArgumentException("index must be 0-3");
        }
        return this;
    }

    public View set(Slot line, Line component) {
        switch (line) {
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
