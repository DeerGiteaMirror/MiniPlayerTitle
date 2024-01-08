package cn.lunadeer.miniplayertitle.commands;

import cn.lunadeer.miniplayertitle.SaleTitle;
import cn.lunadeer.miniplayertitle.Title;
import cn.lunadeer.miniplayertitle.utils.Notification;
import cn.lunadeer.miniplayertitle.utils.XLogger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommands {
    public static boolean createTitle(CommandSender sender, String[] args) {
        if (sender instanceof org.bukkit.entity.Player) {
            Player player = (org.bukkit.entity.Player) sender;
            if (!player.isOp()) {
                XLogger.warn(player, "你没有权限执行该命令");
                return true;
            }
        }
        if (args.length != 3) {
            Notification.warn(sender, "用法: /mplt create <称号名称> <称号描述>");
            return true;
        }
        Title.create(args[1], args[2]);
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
        if (args.length != 2) {
            Notification.warn(sender, "用法: /mplt delete <称号ID>");
            return true;
        }
        Title.delete(Integer.parseInt(args[1]));
        return true;
    }

    public static boolean listAllTitle(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.isOp()) {
                XLogger.warn(player, "你没有权限执行该命令");
                return true;
            }
        }
        int page = 1;
        if (args.length == 2) {
            try {
                page = Integer.parseInt(args[1]);
            } catch (Exception e) {
                Notification.error(sender, "页数格式错误");
                return true;
            }
        }
        Title.listAllTitle(sender, page);
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
        if (args.length != 3) {
            Notification.warn(sender, "用法: /mplt setdesc <称号ID> <称号描述>");
            return true;
        }
        Title title = new Title(Integer.parseInt(args[1]));
        title.setDescription(args[2]);
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
        if (args.length != 3) {
            Notification.warn(sender, "用法: /mplt setname <称号ID> <称号名称>");
            return true;
        }
        Title title = new Title(Integer.parseInt(args[1]));
        title.setTitle(args[2]);
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
            Notification.warn(sender, "用法: /mplt addshop <称号ID>");
            return true;
        }
        SaleTitle title = SaleTitle.create(Integer.parseInt(args[1]));
        if (title == null) {
            Notification.error(sender, "添加商品失败");
        } else {
            Notification.info(sender, "已添加称号到商店, 商品ID: " + title.getId());
            Notification.info(sender, title.getTitle());
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
        if (args.length != 2) {
            Notification.warn(sender, "用法: /mplt removeshop <商品ID>");
            return true;
        }
        SaleTitle.delete(Integer.parseInt(args[1]));
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
            Notification.warn(sender, "用法: /mplt setprice <商品ID> <价格> <天数>(-1为永久)");
            return true;
        }
        SaleTitle.setPrice(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        SaleTitle.setDays(Integer.parseInt(args[1]), Integer.parseInt(args[3]));
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
        if (args.length != 3) {
            Notification.warn(sender, "用法: /mplt setamount <商品ID> <数量>(-1为无限)");
            return true;
        }
        SaleTitle.setAmount(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
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
        if (args.length != 3) {
            Notification.warn(sender, "用法: /mplt setendat <商品ID> <时间YYYYMMDD>(-1为永久)");
            return true;
        }
        long time_stamp;
        if (Integer.parseInt(args[2]) == -1) {
            time_stamp = -1;
        } else {
            // 字符串转时间戳
            java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyyMMdd");
            try {
                java.util.Date date = simpleDateFormat.parse(args[2]);
                time_stamp = date.getTime();
            } catch (Exception e) {
                Notification.error(sender, "时间格式错误");
                return true;
            }
        }
        SaleTitle.setSaleEndAt(Integer.parseInt(args[1]), time_stamp);
        return true;
    }
}
