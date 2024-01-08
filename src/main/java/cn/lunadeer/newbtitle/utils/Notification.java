package cn.lunadeer.newbtitle.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Notification {
    private static final Style i_style = Style.style(TextColor.color(139, 255, 123));
    private static final Style w_style = Style.style(TextColor.color(255, 185, 69));
    private static final Style e_style = Style.style(TextColor.color(255, 96, 72));

    public static void info(Player player, String msg) {
        player.sendMessage(Component.text("[NewbTitle] " + msg, i_style));
    }

    public static void warn(Player player, String msg) {
        player.sendMessage(Component.text("[NewbTitle] " + msg, w_style));
    }

    public static void error(Player player, String msg) {
        player.sendMessage(Component.text("[NewbTitle] " + msg, e_style));
    }

    public static void info(CommandSender sender, String msg) {
        sender.sendMessage(Component.text("[NewbTitle] " + msg, i_style));
    }

    public static void warn(CommandSender sender, String msg) {
        sender.sendMessage(Component.text("[NewbTitle] " + msg, w_style));
    }

    public static void error(CommandSender sender, String msg) {
        sender.sendMessage(Component.text("[NewbTitle] " + msg, e_style));
    }

    public static void info(Player player, Component msg) {
        player.sendMessage(Component.text("[NewbTitle] ", i_style).append(msg));
    }

    public static void warn(Player player, Component msg) {
        player.sendMessage(Component.text("[NewbTitle] " + msg, w_style).append(msg));
    }

    public static void error(Player player, Component msg) {
        player.sendMessage(Component.text("[NewbTitle] " + msg, e_style).append(msg));
    }

    public static void info(CommandSender player, Component msg) {
        player.sendMessage(Component.text("[NewbTitle] ", i_style).append(msg));
    }

    public static void warn(CommandSender player, Component msg) {
        player.sendMessage(Component.text("[NewbTitle] " + msg, w_style).append(msg));
    }

    public static void error(CommandSender player, Component msg) {
        player.sendMessage(Component.text("[NewbTitle] " + msg, e_style).append(msg));
    }
}
