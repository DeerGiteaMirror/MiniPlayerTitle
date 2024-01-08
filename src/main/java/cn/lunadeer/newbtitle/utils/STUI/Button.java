package cn.lunadeer.newbtitle.utils.STUI;

import net.kyori.adventure.text.Component;

public class Button {

    public static Component create(String text, String command) {
        return Component.text("[" + text + "]", View.action_color)
                .clickEvent(net.kyori.adventure.text.event.ClickEvent.clickEvent(net.kyori.adventure.text.event.ClickEvent.Action.RUN_COMMAND, command));
    }
}
