package cn.lunadeer.miniplayertitle.commands;

import cn.lunadeer.miniplayertitle.*;
import cn.lunadeer.miniplayertitle.utils.Notification;
import cn.lunadeer.miniplayertitle.utils.XLogger;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerCommands {
    public static void use(CommandSender sender, String[] args) {
        if (!(sender instanceof org.bukkit.entity.Player)) {
            XLogger.warn("该命令只能由玩家执行");
            return;
        }
        org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
        if (args.length != 2) {
            Notification.warn(player, "用法: /mplt use <称号ID>");
            return;
        }

        XPlayer xPlayer = new XPlayer(player);
        Integer title_id = Integer.parseInt(args[1]);
        xPlayer.updateUsingTitle(title_id);
    }

    public static void list(CommandSender sender, String[] args) {
        if (!(sender instanceof org.bukkit.entity.Player)) {
            XLogger.warn("该命令只能由玩家执行");
            return;
        }
        org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
        int page = 1;
        if (args.length == 2) {
            try {
                page = Integer.parseInt(args[1]);
            } catch (Exception e) {
                Notification.error(player, "页数格式错误");
                return;
            }
        }
        XPlayer xPlayer = new XPlayer(player);
        xPlayer.openBackpack(page);
    }

    public static void shop(CommandSender sender, String[] args) {
        int page = 1;
        if (args.length == 2) {
            try {
                page = Integer.parseInt(args[1]);
            } catch (Exception ignored) {
            }
        }
        Shop.open(sender, page);
    }

    public static void buy(CommandSender sender, String[] args) {
        if (!(sender instanceof org.bukkit.entity.Player)) {
            XLogger.warn("该命令只能由玩家执行");
            return;
        }
        org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
        if (args.length != 2) {
            Notification.warn(player, "用法: /mplt buy <称号ID>");
            return;
        }
        XPlayer xPlayer = new XPlayer(player);
        Integer sale_id = Integer.parseInt(args[1]);
        SaleTitle saleTitle = Shop.getSaleTitles().get(sale_id);
        if (saleTitle == null) {
            Notification.error(player, "该称号不存在");
            return;
        }
        xPlayer.buyTitle(saleTitle);
    }

    public static void custom(CommandSender sender, String[] args) {
        if (!(sender instanceof org.bukkit.entity.Player)) {
            XLogger.warn("该命令只能由玩家执行");
            return;
        }
        Player player = (Player) sender;
        if (args.length != 2) {
            Notification.warn(player, "用法: /mplt custom <称号>");
            return;
        }
        if (!MiniPlayerTitle.config.isEnableCustom()) {
            Notification.error(player, "自定义称号功能已关闭");
            return;
        }
        XPlayer xPlayer = new XPlayer(player);
        xPlayer.custom(args[1]);
    }
}
