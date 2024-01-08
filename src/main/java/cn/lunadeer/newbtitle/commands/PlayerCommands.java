package cn.lunadeer.newbtitle.commands;

import cn.lunadeer.newbtitle.SaleTitle;
import cn.lunadeer.newbtitle.Shop;
import cn.lunadeer.newbtitle.XPlayer;
import cn.lunadeer.newbtitle.utils.Notification;
import cn.lunadeer.newbtitle.utils.XLogger;
import org.bukkit.command.CommandSender;

public class PlayerCommands {
    public static boolean use(CommandSender sender, String[] args) {
        if (!(sender instanceof org.bukkit.entity.Player)) {
            XLogger.warn("该命令只能由玩家执行");
            return true;
        }
        org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
        if (args.length != 2) {
            Notification.warn(player, "用法: /mplt use <称号ID>");
            return true;
        }

        XPlayer xPlayer = new XPlayer(player);
        Integer title_id = Integer.parseInt(args[1]);
        xPlayer.updateUsingTitle(title_id);
        return true;
    }

    public static boolean list(CommandSender sender, String[] args) {
        if (!(sender instanceof org.bukkit.entity.Player)) {
            XLogger.warn("该命令只能由玩家执行");
            return true;
        }
        org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
        int page = 1;
        if (args.length == 2) {
            try {
                page = Integer.parseInt(args[1]);
            } catch (Exception e) {
                Notification.error(player, "页数格式错误");
                return true;
            }
        }
        XPlayer xPlayer = new XPlayer(player);
        xPlayer.openBackpack(page);
        return true;
    }

    public static boolean shop(CommandSender sender, String[] args) {
        int page = 1;
        if (args.length == 2) {
            try {
                page = Integer.parseInt(args[1]);
            } catch (Exception ignored) {
            }
        }
        Shop.open(sender, page);
        return true;
    }

    public static boolean buy(CommandSender sender, String[] args) {
        if (!(sender instanceof org.bukkit.entity.Player)) {
            XLogger.warn("该命令只能由玩家执行");
            return true;
        }
        org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
        if (args.length != 2) {
            Notification.warn(player, "用法: /mplt buy <称号ID>");
            return true;
        }
        XPlayer xPlayer = new XPlayer(player);
        Integer sale_id = Integer.parseInt(args[1]);
        SaleTitle saleTitle = Shop.getSaleTitle(sale_id);
        if (saleTitle == null) {
            Notification.error(player, "该称号不存在");
            return true;
        }
        xPlayer.buyTitle(saleTitle);
        return true;
    }
}
