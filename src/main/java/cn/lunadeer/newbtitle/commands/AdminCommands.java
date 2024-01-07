package cn.lunadeer.newbtitle.commands;

import cn.lunadeer.newbtitle.SaleTitle;
import cn.lunadeer.newbtitle.Shop;
import cn.lunadeer.newbtitle.Title;
import org.bukkit.entity.Player;
import cn.lunadeer.newbtitle.utils.Notification;
import cn.lunadeer.newbtitle.utils.XLogger;
import org.bukkit.command.CommandSender;

public class AdminCommands {
    public static boolean createTitle(CommandSender sender, String[] args) {
        if (sender instanceof org.bukkit.entity.Player) {
            Player player = (org.bukkit.entity.Player) sender;
            if (!player.isOp()) {
                XLogger.warn(player, "你没有权限执行该命令");
                return true;
            }
        }
        if (args.length != 2) {
            Notification.warn(sender, "用法: /nt create <称号名称> <称号描述>");
            return true;
        }
        Title.create(args[0], args[1]);
        return true;
    }

    public static boolean deleteTitle(CommandSender sender, String[] args) {
        if (sender instanceof org.bukkit.entity.Player) {
            Player player = (org.bukkit.entity.Player) sender;
            if (!player.isOp()) {
                XLogger.warn(player, "你没有权限执行该命令");
                return true;
            }
        }
        if (args.length != 1) {
            Notification.warn(sender, "用法: /nt delete <称号ID>");
            return true;
        }
        Title.delete(Integer.parseInt(args[0]));
        return true;
    }

    public static boolean setTitleDescription(CommandSender sender, String[] args) {
        if (sender instanceof org.bukkit.entity.Player) {
            Player player = (org.bukkit.entity.Player) sender;
            if (!player.isOp()) {
                XLogger.warn(player, "你没有权限执行该命令");
                return true;
            }
        }
        if (args.length != 2) {
            Notification.warn(sender, "用法: /nt setdesc <称号ID> <称号描述>");
            return true;
        }
        Title title = new Title(Integer.parseInt(args[0]));
        title.setDescription(args[1]);
        return true;
    }

    public static boolean setTitleName(CommandSender sender, String[] args) {
        if (sender instanceof org.bukkit.entity.Player) {
            Player player = (org.bukkit.entity.Player) sender;
            if (!player.isOp()) {
                XLogger.warn(player, "你没有权限执行该命令");
                return true;
            }
        }
        if (args.length != 2) {
            Notification.warn(sender, "用法: /nt setname <称号ID> <称号名称>");
            return true;
        }
        Title title = new Title(Integer.parseInt(args[0]));
        title.setTitle(args[1]);
        return true;
    }

    public static boolean addShop(CommandSender sender, String[] args) {
        if (sender instanceof org.bukkit.entity.Player) {
            Player player = (org.bukkit.entity.Player) sender;
            if (!player.isOp()) {
                XLogger.warn(player, "你没有权限执行该命令");
                return true;
            }
        }
        if (args.length != 2) {
            Notification.warn(sender, "用法: /nt addshop <称号ID>");
            return true;
        }
        SaleTitle title = SaleTitle.create(Integer.parseInt(args[0]));
        if (title == null) {
            Notification.error(sender, "添加商品失败");
        } else {
            Notification.info(sender, "已添加称号 " + title.getTitle() + " 到商店, 商品ID: " + title.getId());
        }
        return true;
    }

    public static boolean removeShop(CommandSender sender, String[] args) {
        if (sender instanceof org.bukkit.entity.Player) {
            Player player = (org.bukkit.entity.Player) sender;
            if (!player.isOp()) {
                XLogger.warn(player, "你没有权限执行该命令");
                return true;
            }
        }
        if (args.length != 1) {
            Notification.warn(sender, "用法: /nt removeshop <商品ID>");
            return true;
        }
        SaleTitle.delete(Integer.parseInt(args[0]));
        return true;
    }

    public static boolean setPrice(CommandSender sender, String[] args) {
        if (sender instanceof org.bukkit.entity.Player) {
            Player player = (org.bukkit.entity.Player) sender;
            if (!player.isOp()) {
                XLogger.warn(player, "你没有权限执行该命令");
                return true;
            }
        }
        if (args.length != 4) {
            Notification.warn(sender, "用法: /nt setprice <商品ID> <价格> <天数>(-1为永久)");
            return true;
        }
        SaleTitle.setPrice(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        SaleTitle.setDays(Integer.parseInt(args[0]), Integer.parseInt(args[2]));
        return true;
    }

    public static boolean setAmount(CommandSender sender, String[] args) {
        if (sender instanceof org.bukkit.entity.Player) {
            Player player = (org.bukkit.entity.Player) sender;
            if (!player.isOp()) {
                XLogger.warn(player, "你没有权限执行该命令");
                return true;
            }
        }
        if (args.length != 2) {
            Notification.warn(sender, "用法: /nt setamount <商品ID> <数量>(-1为无限)");
            return true;
        }
        SaleTitle.setAmount(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        return true;
    }

    public static boolean setSaleEndAt(CommandSender sender, String[] args) {
        if (sender instanceof org.bukkit.entity.Player) {
            Player player = (org.bukkit.entity.Player) sender;
            if (!player.isOp()) {
                XLogger.warn(player, "你没有权限执行该命令");
                return true;
            }
        }
        if (args.length != 2) {
            Notification.warn(sender, "用法: /nt setendat <商品ID> <时间戳>(-1为永久)");
            return true;
        }
        SaleTitle.setSaleEndAt(Integer.parseInt(args[0]), Long.parseLong(args[1]));
        return true;
    }
}
