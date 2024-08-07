package cn.lunadeer.miniplayertitle.commands;

import cn.lunadeer.minecraftpluginutils.Notification;
import cn.lunadeer.miniplayertitle.MiniPlayerTitle;
import cn.lunadeer.miniplayertitle.dtos.PlayerInfoDTO;
import cn.lunadeer.miniplayertitle.dtos.PlayerTitleDTO;
import cn.lunadeer.miniplayertitle.dtos.TitleDTO;
import cn.lunadeer.miniplayertitle.tuis.AllTitles;
import cn.lunadeer.miniplayertitle.tuis.MyTitles;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static cn.lunadeer.miniplayertitle.commands.Apis.updateName;

public class TitleManage {
    /**
     * 创建称号
     * mplt create_title <称号名称> [称号描述]
     *
     * @param sender CommandSender
     * @param args   String[]
     */
    public static void createTitle(CommandSender sender, String[] args) {
        if (!sender.hasPermission("mplt.admin")) return;
        if (args.length < 2) {
            Notification.warn(sender, "用法: /mplt create_title <称号名称> [称号描述]");
            return;
        }
        TitleDTO title = TitleDTO.create(args[1], args.length == 3 ? args[2] : "这是一个管理员创建的称号");
        if (title != null) {
            Notification.info(sender, Component.text("成功创建称号: [" + title.getId() + "]").append(title.getTitleColored()));
            AllTitles.show(sender, new String[]{"all_titles"});
        } else {
            Notification.error(sender, "创建称号失败，具体请查看控制台日志");
        }
    }

    /**
     * 删除称号
     * mplt delete_title <称号ID> [页码]
     *
     * @param sender CommandSender
     * @param args   String[]
     */
    public static void deleteTitle(CommandSender sender, String[] args) {
        try {
            if (!sender.hasPermission("mplt.admin")) return;
            if (args.length < 2) {
                Notification.warn(sender, "用法: /mplt delete_title <称号ID>");
                return;
            }
            TitleDTO title = TitleDTO.get(Integer.parseInt(args[1]));
            if (title == null) {
                Notification.error(sender, "称号不存在");
                return;
            }
            boolean success = title.delete();
            if (!success) {
                Notification.error(sender, "删除称号失败，具体请查看控制台日志");
                return;
            }
            Notification.info(sender, "已删除称号");
            if (args.length == 3) {
                int page = Integer.parseInt(args[2]);
                AllTitles.show(sender, new String[]{"all_titles", String.valueOf(page)});
            }
        } catch (Exception e) {
            Notification.error(sender, e.getMessage());
        }
    }

    /**
     * 设置称号名称
     * mplt edit_title_name <称号ID> <称号名称>
     *
     * @param sender CommandSender
     * @param args   String[]
     */
    public static void editTitleName(CommandSender sender, String[] args) {
        try {
            if (!sender.hasPermission("mplt.admin")) return;
            if (args.length != 3) {
                Notification.warn(sender, "用法: /mplt set_title <称号ID> <称号名称>");
                return;
            }
            TitleDTO title = TitleDTO.get(Integer.parseInt(args[1]));
            if (title == null) {
                Notification.error(sender, "称号不存在");
                return;
            }
            boolean success = title.updateTitle(args[2]);
            if (success) {
                Notification.info(sender, "已更新称号名称");
            } else {
                Notification.error(sender, "更新称号名称失败，具体请查看控制台日志");
            }
        } catch (Exception e) {
            Notification.error(sender, e.getMessage());
        }
    }

    /**
     * 设置称号描述
     * mplt edit_title_desc <称号ID> <称号描述>
     *
     * @param sender CommandSender
     * @param args   String[]
     */
    public static void editTitleDescription(CommandSender sender, String[] args) {
        try {
            if (!sender.hasPermission("mplt.admin")) return;
            if (args.length != 3) {
                Notification.warn(sender, "用法: /mplt set_desc <称号ID> <称号描述>");
                return;
            }
            TitleDTO title = TitleDTO.get(Integer.parseInt(args[1]));
            if (title == null) {
                Notification.error(sender, "称号不存在");
                return;
            }
            boolean success = title.updateDescription(args[2]);
            if (success) {
                Notification.info(sender, "已更新称号描述");
            } else {
                Notification.error(sender, "更新称号描述失败，具体请查看控制台日志");
            }
        } catch (Exception e) {
            Notification.error(sender, e.getMessage());
        }
    }

    /**
     * 使用称号
     * mplt use_title <背包ID> [页码]
     *
     * @param sender CommandSender
     * @param args   String[]
     */
    public static void useTitle(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            Notification.error(sender, "该命令只能由玩家执行");
            return;
        }
        if (!sender.hasPermission("mplt.command")) return;
        if (args.length < 2) {
            Notification.warn(sender, "用法: /mplt use_title <背包ID> [页码]");
            return;
        }
        Player player = (Player) sender;
        PlayerInfoDTO playerInfo = PlayerInfoDTO.get((player).getUniqueId());
        if (playerInfo == null) {
            Notification.error(sender, "获取玩家信息时出现错误");
            return;
        }
        int id = Integer.parseInt(args[1]);
        if (id == -1) {
            Notification.info(sender, "已卸下称号");
            playerInfo.setUsingTitle(null);
            updateName(player, null);
        } else {
            PlayerTitleDTO title = PlayerTitleDTO.get(id);
            if (title == null) {
                Notification.error(sender, "称号不存在");
                return;
            }
            if (!title.getPlayerUuid().equals(player.getUniqueId())) {
                Notification.error(sender, "该称号不属于你");
                return;
            }
            if (title.isExpired()) {
                Notification.error(sender, "称号 %s 已过期", title.getTitle().getTitlePlainText());
                playerInfo.setUsingTitle(null);
                updateName(player, null);
                return;
            }
            boolean success = playerInfo.setUsingTitle(title.getTitle());
            if (success) {
                updateName((Player) sender, title.getTitle());
                Notification.info(sender, "已使用称号");
            } else {
                Notification.error(sender, "使用称号失败，具体请查看控制台日志");
            }
        }

        if (args.length == 3) {
            int page = Integer.parseInt(args[2]);
            MyTitles.show(sender, new String[]{"my_titles", String.valueOf(page)});
        }
    }

    /**
     * 创建自定义称号
     * mplt custom_title <称号内容>
     *
     * @param sender CommandSender
     * @param args   String[]
     */
    public static void customTitle(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            Notification.error(sender, "该命令只能由玩家执行");
            return;
        }
        if (!sender.hasPermission("mplt.custom") && !sender.hasPermission("mplt.admin")) {
            Notification.error(sender, "你没有权限使用该命令");
            return;
        }
        Player player = (Player) sender;
        if (!MiniPlayerTitle.config.isEnableCustom()) {
            Notification.error(sender, "自定义称号功能已关闭");
            return;
        }
        PlayerInfoDTO playerInfo = PlayerInfoDTO.get(player.getUniqueId());
        if (playerInfo == null) {
            Notification.error(sender, "获取玩家信息时出现错误");
            return;
        }
        if (MiniPlayerTitle.config.getCustomCost() > playerInfo.getCoin()) {
            Notification.error(sender, "称号币不足");
            return;
        }
        if (args.length < 2) {
            Notification.warn(sender, "用法: /mplt custom_title <称号>");
            return;
        }
        TitleDTO title = TitleDTO.create(args[1], player.getName() + "的自定义称号");
        if (title == null) {
            Notification.error(sender, "创建称号失败，具体请查看控制台日志");
            return;
        }
        if (title.getTitlePlainText().length() > MiniPlayerTitle.config.getMaxLength()) {
            Notification.error(sender, "称号长度超过限制");
            title.delete();
            return;
        }
        PlayerTitleDTO created_rec = PlayerTitleDTO.create(player.getUniqueId(), title, null);
        if (created_rec == null) {
            Notification.error(sender, "创建称号记录失败，具体请查看控制台日志");
            title.delete();
            return;
        }
        playerInfo.setCoin(playerInfo.getCoin() - MiniPlayerTitle.config.getCustomCost());
        Notification.info(sender, "成功创建自定义称号");
        MyTitles.show(sender, new String[]{"my_titles"});
    }
}
