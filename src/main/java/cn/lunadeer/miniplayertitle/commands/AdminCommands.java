package cn.lunadeer.miniplayertitle.commands;

import cn.lunadeer.miniplayertitle.SaleTitle;
import cn.lunadeer.miniplayertitle.Title;
import cn.lunadeer.miniplayertitle.XPlayer;
import cn.lunadeer.miniplayertitle.utils.Notification;
import cn.lunadeer.miniplayertitle.utils.XLogger;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommands {
    public static void createTitle(CommandSender sender, String[] args) {
        if (sender instanceof org.bukkit.entity.Player) {
            Player player = (org.bukkit.entity.Player) sender;
            if (!player.isOp()) {
                XLogger.warn(player, "你没有权限执行该命令");
                return;
            }
        }
        if (args.length != 3) {
            Notification.warn(sender, "用法: /mplt create <称号名称> <称号描述>");
            return;
        }
        Title title = Title.create(args[1], args[2]);
        if (title != null) {
            Notification.info(sender, Component.text("成功创建称号: [" + title.getId() + "]").append(title.getTitle()));
        } else {
            Notification.error(sender, "创建称号失败");
        }
    }

    public static void deleteTitle(CommandSender sender, String[] args) {
        try {
            if (sender instanceof org.bukkit.entity.Player) {
                Player player = (org.bukkit.entity.Player) sender;
                if (!player.isOp()) {
                    XLogger.warn(player, "你没有权限执行该命令");
                    return;
                }
            }
            if (args.length != 2) {
                Notification.warn(sender, "用法: /mplt delete <称号ID>");
                return;
            }
            Title.delete(Integer.parseInt(args[1]));
            Notification.info(sender, "已删除称号");
        } catch (Exception e) {
            Notification.error(sender, e.getMessage());
        }
    }

    public static void listAllTitle(CommandSender sender, String[] args) {
        try {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!player.isOp()) {
                    XLogger.warn(player, "你没有权限执行该命令");
                    return;
                }
            }
            int page = 1;
            if (args.length == 2) {
                try {
                    page = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    Notification.error(sender, "页数格式错误");
                    return;
                }
            }
            Title.listAllTitle(sender, page);
        } catch (Exception e) {
            Notification.error(sender, e.getMessage());
        }
    }

    public static void setTitleDescription(CommandSender sender, String[] args) {
        try {
            if (sender instanceof org.bukkit.entity.Player) {
                Player player = (org.bukkit.entity.Player) sender;
                if (!player.isOp()) {
                    XLogger.warn(player, "你没有权限执行该命令");
                    return;
                }
            }
            if (args.length != 3) {
                Notification.warn(sender, "用法: /mplt setdesc <称号ID> <称号描述>");
                return;
            }
            Title title = new Title(Integer.parseInt(args[1]));
            title.setDescription(args[2]);
            Notification.info(sender, "已设置称号描述");
        } catch (Exception e) {
            Notification.error(sender, e.getMessage());
        }
    }

    public static void setTitleName(CommandSender sender, String[] args) {
        try {
            if (sender instanceof org.bukkit.entity.Player) {
                Player player = (org.bukkit.entity.Player) sender;
                if (!player.isOp()) {
                    XLogger.warn(player, "你没有权限执行该命令");
                    return;
                }
            }
            if (args.length != 3) {
                Notification.warn(sender, "用法: /mplt setname <称号ID> <称号名称>");
                return;
            }
            Title title = new Title(Integer.parseInt(args[1]));
            title.setTitle(args[2]);
            Notification.info(sender, "已设置称号名称");
        } catch (Exception e) {
            Notification.error(sender, e.getMessage());
        }
    }

    public static void addShop(CommandSender sender, String[] args) {
        try {
            if (sender instanceof org.bukkit.entity.Player) {
                Player player = (org.bukkit.entity.Player) sender;
                if (!player.isOp()) {
                    XLogger.warn(player, "你没有权限执行该命令");
                    return;
                }
            }
            if (args.length != 2) {
                Notification.warn(sender, "用法: /mplt addshop <称号ID>");
                return;
            }
            SaleTitle title = SaleTitle.create(Integer.parseInt(args[1]));
            if (title == null) {
                Notification.error(sender, "添加商品失败");
            } else {
                Notification.info(sender, "已添加称号到商店, 商品ID: " + title.getSaleId());
                Notification.info(sender, title.getTitle());
            }
        } catch (Exception e) {
            Notification.error(sender, e.getMessage());
        }
    }

    public static void removeShop(CommandSender sender, String[] args) {
        try {
            if (sender instanceof org.bukkit.entity.Player) {
                Player player = (org.bukkit.entity.Player) sender;
                if (!player.isOp()) {
                    XLogger.warn(player, "你没有权限执行该命令");
                    return;
                }
            }
            if (args.length != 2) {
                Notification.warn(sender, "用法: /mplt removeshop <商品ID>");
                return;
            }
            SaleTitle.delete(Integer.parseInt(args[1]));
            Notification.info(sender, "已从商店移除商品");
        } catch (Exception e) {
            Notification.error(sender, e.getMessage());
        }
    }

    public static void setPrice(CommandSender sender, String[] args) {
        try {
            if (sender instanceof org.bukkit.entity.Player) {
                Player player = (org.bukkit.entity.Player) sender;
                if (!player.isOp()) {
                    XLogger.warn(player, "你没有权限执行该命令");
                    return;
                }
            }
            if (args.length != 4) {
                Notification.warn(sender, "用法: /mplt setprice <商品ID> <价格> <天数>(-1为永久)");
                return;
            }
            int price;
            int days;
            int id;
            try {
                price = Integer.parseInt(args[2]);
                days = Integer.parseInt(args[3]);
                id = Integer.parseInt(args[1]);
            } catch (Exception e) {
                Notification.error(sender, "价格或天数格式错误");
                return;
            }
            if (price < 0 || days < -1) {
                Notification.error(sender, "价格或天数格式错误");
                return;
            }
            SaleTitle.setPrice(id, price);
            SaleTitle.setDays(id, days);
            Notification.info(sender, "已设置商品价格");
        } catch (Exception e) {
            Notification.error(sender, e.getMessage());
        }
    }

    public static void setAmount(CommandSender sender, String[] args) {
        try {
            if (sender instanceof org.bukkit.entity.Player) {
                Player player = (org.bukkit.entity.Player) sender;
                if (!player.isOp()) {
                    XLogger.warn(player, "你没有权限执行该命令");
                    return;
                }
            }
            if (args.length != 3) {
                Notification.warn(sender, "用法: /mplt setamount <商品ID> <数量>(-1为无限)");
                return;
            }
            int amount;
            int id;
            try {
                amount = Integer.parseInt(args[2]);
                id = Integer.parseInt(args[1]);
            } catch (Exception e) {
                Notification.error(sender, "数量格式错误");
                return;
            }
            SaleTitle.setAmount(id, amount);
            Notification.info(sender, "已设置商品数量");
        } catch (Exception e) {
            Notification.error(sender, e.getMessage());
        }
    }

    public static void setSaleEndAt(CommandSender sender, String[] args) {
        try {
            if (sender instanceof org.bukkit.entity.Player) {
                Player player = (org.bukkit.entity.Player) sender;
                if (!player.isOp()) {
                    XLogger.warn(player, "你没有权限执行该命令");
                    return;
                }
            }
            if (args.length != 3) {
                Notification.warn(sender, "用法: /mplt setendat <商品ID> <时间YYYYMMDD>(-1为永久)");
                return;
            }
            long time_stamp;
            try {
                time_stamp = Long.parseLong(args[2]);
            } catch (Exception e) {
                Notification.error(sender, "时间格式错误");
                return;
            }

            SaleTitle.setSaleEndAt(Integer.parseInt(args[1]), time_stamp);
            Notification.info(sender, "已设置商品结束时间");
        } catch (Exception e) {
            Notification.error(sender, e.getMessage());
        }
    }

    public static void addCoin(CommandSender sender, String[] args) {
        try {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!player.isOp()) {
                    XLogger.warn(player, "你没有权限执行该命令");
                    return;
                }
            }
            if (args.length != 3) {
                Notification.warn(sender, "用法: /mplt addcoin <玩家> <数量>");
                return;
            }
            Player target = sender.getServer().getPlayer(args[1]);
            if (target == null) {
                Notification.error(sender, "玩家不在线");
                return;
            }
            int amount;
            try {
                amount = Integer.parseInt(args[2]);
            } catch (Exception e) {
                Notification.error(sender, "数量格式错误");
                return;
            }
            new XPlayer(target).add_coin(amount);
            Notification.info(sender, "已给予玩家 " + target.getName() + " " + amount + " 称号币");
        } catch (Exception e) {
            Notification.error(sender, e.getMessage());
        }
    }

    public static void setCoin(CommandSender sender, String[] args) {
        try {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!player.isOp()) {
                    XLogger.warn(player, "你没有权限执行该命令");
                    return;
                }
            }
            if (args.length != 3) {
                Notification.warn(sender, "用法: /mplt setcoin <玩家> <数量>");
                return;
            }
            Player target = sender.getServer().getPlayer(args[1]);
            if (target == null) {
                Notification.error(sender, "玩家不在线");
                return;
            }
            int amount;
            try {
                amount = Integer.parseInt(args[2]);
            } catch (Exception e) {
                Notification.error(sender, "数量格式错误");
                return;
            }
            new XPlayer(target).set_coin(amount);
            Notification.info(sender, "已设置玩家 " + target.getName() + " 称号币为 " + amount);
        } catch (Exception e) {
            Notification.error(sender, e.getMessage());
        }
    }
}
