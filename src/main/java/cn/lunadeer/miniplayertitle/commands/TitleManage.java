package cn.lunadeer.miniplayertitle.commands;

import cn.lunadeer.miniplayertitle.MiniPlayerTitle;
import cn.lunadeer.miniplayertitle.dtos.PlayerInfoDTO;
import cn.lunadeer.miniplayertitle.dtos.TitleDTO;
import cn.lunadeer.miniplayertitle.tuis.AllTitles;
import cn.lunadeer.miniplayertitle.tuis.MyTitles;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static cn.lunadeer.miniplayertitle.commands.Apis.notOpOrConsole;
import static cn.lunadeer.miniplayertitle.commands.Apis.updateName;

public class TitleManage {
    /**
     * 创建称号
     * mplt create_title <称号名称> <称号描述>
     *
     * @param sender CommandSender
     * @param args   String[]
     */
    public static void createTitle(CommandSender sender, String[] args) {
        if (notOpOrConsole(sender)) return;
        if (args.length != 3) {
            MiniPlayerTitle.notification.warn(sender, "用法: /mplt create_title <称号名称> <称号描述>");
            return;
        }
        TitleDTO title = TitleDTO.create(args[1], args[2]);
        if (title != null) {
            MiniPlayerTitle.notification.info(sender, Component.text("成功创建称号: [" + title.getId() + "]").append(title.getTitleColored()));
        } else {
            MiniPlayerTitle.notification.error(sender, "创建称号失败，具体请查看控制台日志");
        }
    }

    /**
     * 删除称号
     * mplt delete_title <称号ID>
     *
     * @param sender CommandSender
     * @param args   String[]
     */
    public static void deleteTitle(CommandSender sender, String[] args) {
        try {
            if (notOpOrConsole(sender)) return;
            if (args.length < 2) {
                MiniPlayerTitle.notification.warn(sender, "用法: /mplt delete_title <称号ID>");
                return;
            }
            TitleDTO.delete(Integer.parseInt(args[1]));
            MiniPlayerTitle.notification.info(sender, "已删除称号");
            if (args.length == 3) {
                int page = Integer.parseInt(args[2]);
                AllTitles.show(sender, new String[]{"all_titles", String.valueOf(page)});
            }
        } catch (Exception e) {
            MiniPlayerTitle.notification.error(sender, e.getMessage());
        }
    }

    /**
     * 设置称号名称
     * mplt set_title <称号ID> <称号名称>
     *
     * @param sender CommandSender
     * @param args   String[]
     */
    public static void setTitle(CommandSender sender, String[] args) {
        try {
            if (notOpOrConsole(sender)) return;
            if (args.length != 3) {
                MiniPlayerTitle.notification.warn(sender, "用法: /mplt set_title <称号ID> <称号名称>");
                return;
            }
            TitleDTO title = TitleDTO.get(Integer.parseInt(args[1]));
            if (title == null) {
                MiniPlayerTitle.notification.error(sender, "称号不存在");
                return;
            }
            boolean success = title.updateTitle(args[2]);
            if (success) {
                MiniPlayerTitle.notification.info(sender, "已更新称号名称");
            } else {
                MiniPlayerTitle.notification.error(sender, "更新称号名称失败，具体请查看控制台日志");
            }
        } catch (Exception e) {
            MiniPlayerTitle.notification.error(sender, e.getMessage());
        }
    }

    /**
     * 设置称号描述
     * mplt set_desc <称号ID> <称号描述>
     *
     * @param sender CommandSender
     * @param args   String[]
     */
    public static void setTitleDescription(CommandSender sender, String[] args) {
        try {
            if (notOpOrConsole(sender)) return;
            if (args.length != 3) {
                MiniPlayerTitle.notification.warn(sender, "用法: /mplt set_desc <称号ID> <称号描述>");
                return;
            }
            TitleDTO title = TitleDTO.get(Integer.parseInt(args[1]));
            if (title == null) {
                MiniPlayerTitle.notification.error(sender, "称号不存在");
                return;
            }
            boolean success = title.updateDescription(args[2]);
            if (success) {
                MiniPlayerTitle.notification.info(sender, "已更新称号描述");
            } else {
                MiniPlayerTitle.notification.error(sender, "更新称号描述失败，具体请查看控制台日志");
            }
        } catch (Exception e) {
            MiniPlayerTitle.notification.error(sender, e.getMessage());
        }
    }

    /**
     * 使用称号
     * mplt use_title <称号ID> [页码]
     *
     * @param sender CommandSender
     * @param args   String[]
     */
    public static void useTitle(CommandSender sender, String[] args) {
        if (notOpOrConsole(sender)) return;
        if (args.length < 2) {
            MiniPlayerTitle.notification.warn(sender, "用法: /mplt use_title <称号ID> [页码]");
            return;
        }
        TitleDTO title = TitleDTO.get(Integer.parseInt(args[1]));
        if (title == null) {
            MiniPlayerTitle.notification.error(sender, "称号不存在");
            return;
        }
        PlayerInfoDTO playerInfo = PlayerInfoDTO.get(((Player) sender).getUniqueId());
        if (playerInfo == null) {
            MiniPlayerTitle.notification.error(sender, "获取玩家信息时出现错误");
            return;
        }
        boolean success = playerInfo.setUsingTitle(title);
        if (success) {
            updateName((Player) sender, title);
            MiniPlayerTitle.notification.info(sender, "已使用称号");
        } else {
            MiniPlayerTitle.notification.error(sender, "使用称号失败，具体请查看控制台日志");
        }

        if (args.length == 3) {
            int page = Integer.parseInt(args[2]);
            MyTitles.show(sender, new String[]{"my_titles", String.valueOf(page)});
        }
    }
}
