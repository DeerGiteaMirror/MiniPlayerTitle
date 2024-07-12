package cn.lunadeer.miniplayertitle.commands;

import cn.lunadeer.minecraftpluginutils.Notification;
import cn.lunadeer.miniplayertitle.dtos.PlayerInfoDTO;
import cn.lunadeer.miniplayertitle.dtos.PlayerTitleDTO;
import cn.lunadeer.miniplayertitle.dtos.TitleDTO;
import org.bukkit.command.CommandSender;

import java.time.LocalDateTime;


public class PlayerManage {

    /**
     * 给玩家添加称号币
     * mplt add_coin <玩家名称> <称号币数量>
     *
     * @param sender CommandSender
     * @param args   String[]
     */
    public static void addCoin(CommandSender sender, String[] args) {
        if (!sender.hasPermission("mplt.admin")) return;
        try {
            PlayerInfoDTO playerInfo = PlayerInfoDTO.get(args[1]);
            if (playerInfo == null) {
                Notification.error(sender, "玩家不存在");
                return;
            }
            if (playerInfo.addCoin(Integer.parseInt(args[2]))) {
                Notification.info(sender, "成功给玩家 %s 添加 %s 称号币", playerInfo.getLastUseName(), args[2]);
                Notification.info(sender, "玩家 %s 当前余额 %f 称号币", playerInfo.getLastUseName(), playerInfo.getCoin());
            } else {
                Notification.error(sender, "给玩家添加称号币失败，详细错误请查看控制台日志");
            }
        } catch (Exception e) {
            Notification.error(sender, "给玩家添加称号币时出错：%s", e.getMessage());
        }
    }

    /**
     * 给玩家设置称号币余额
     * mplt set_coin <玩家名称> <称号币数量>
     *
     * @param sender CommandSender
     * @param args   String[]
     */
    public static void setCoin(CommandSender sender, String[] args) {
        if (!sender.hasPermission("mplt.admin")) return;
        try {
            PlayerInfoDTO playerInfo = PlayerInfoDTO.get(args[1]);
            if (playerInfo == null) {
                Notification.error(sender, "玩家不存在");
                return;
            }
            if (playerInfo.setCoin(Integer.parseInt(args[2]))) {
                Notification.info(sender, "成功给玩家 %s 设置 %s 称号币", playerInfo.getLastUseName(), args[2]);
                Notification.info(sender, "玩家 %s 当前余额 %f 称号币", playerInfo.getLastUseName(), playerInfo.getCoin());
            } else {
                Notification.error(sender, "给玩家设置称号币失败，详细错误请查看控制台日志");
            }
        } catch (Exception e) {
            Notification.error(sender, "给玩家设置称号币时出错：%s", e.getMessage());
        }
    }

    /**
     * 直接授予玩家某称号
     * mplt grant_title <玩家名称> <称号> <描述> [天数]
     *
     * @param sender CommandSender
     * @param args   String[]
     */
    public static void grantTitle(CommandSender sender, String[] args) {
        if (!sender.hasPermission("mplt.admin")) return;
        try {
            PlayerInfoDTO playerInfo = PlayerInfoDTO.get(args[1]);
            if (playerInfo == null) {
                Notification.error(sender, "玩家不存在");
                return;
            }
            String title = args[2];
            String description = args[3];
            int days = args.length == 5 ? Integer.parseInt(args[4]) : -1;
            TitleDTO titleDTO = TitleDTO.create(title, description);
            if (titleDTO == null) {
                Notification.error(sender, "创建称号失败，详细错误请查看控制台日志");
                return;
            }
            LocalDateTime expire = days == -1 ? null : LocalDateTime.now().plusDays(days);
            PlayerTitleDTO playerTitle = PlayerTitleDTO.create(playerInfo.getUuid(), titleDTO, expire);
            if (playerTitle == null) {
                Notification.error(sender, "授予玩家称号失败，详细错误请查看控制台日志");
                return;
            }
            playerInfo.setUsingTitle(titleDTO);
            Notification.info(sender, "成功给玩家 %s 授予称号 %s", playerInfo.getLastUseName(), titleDTO.getTitlePlainText());
        } catch (Exception e) {
            Notification.error(sender, "授予玩家称号时出错：%s", e.getMessage());
        }
    }
}
