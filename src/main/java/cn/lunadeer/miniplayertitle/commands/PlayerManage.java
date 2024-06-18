package cn.lunadeer.miniplayertitle.commands;

import cn.lunadeer.miniplayertitle.MiniPlayerTitle;
import cn.lunadeer.miniplayertitle.dtos.PlayerInfoDTO;
import org.bukkit.command.CommandSender;

import static cn.lunadeer.miniplayertitle.commands.Apis.notOpOrConsole;

public class PlayerManage {

    /**
     * 给玩家添加称号币
     * mplt add_coin <玩家名称> <称号币数量>
     *
     * @param sender CommandSender
     * @param args   String[]
     */
    public static void addCoin(CommandSender sender, String[] args) {
        if (notOpOrConsole(sender)) return;
        try {
            PlayerInfoDTO playerInfo = PlayerInfoDTO.get(args[1]);
            if (playerInfo == null) {
                MiniPlayerTitle.notification.error(sender, "玩家不存在");
                return;
            }
            if (playerInfo.addCoin(Integer.parseInt(args[2]))) {
                MiniPlayerTitle.notification.info(sender, "成功给玩家 %s 添加 %s 称号币", playerInfo.getLastUseName(), args[2]);
                MiniPlayerTitle.notification.info(sender, "玩家 %s 当前余额 %f 称号币", playerInfo.getLastUseName(), playerInfo.getCoin());
            } else {
                MiniPlayerTitle.notification.error(sender, "给玩家添加称号币失败，详细错误请查看控制台日志");
            }
        } catch (Exception e) {
            MiniPlayerTitle.notification.error(sender, "给玩家添加称号币时出错：%s", e.getMessage());
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
        if (notOpOrConsole(sender)) return;
        try {
            PlayerInfoDTO playerInfo = PlayerInfoDTO.get(args[1]);
            if (playerInfo == null) {
                MiniPlayerTitle.notification.error(sender, "玩家不存在");
                return;
            }
            if (playerInfo.setCoin(Integer.parseInt(args[2]))) {
                MiniPlayerTitle.notification.info(sender, "成功给玩家 %s 设置 %s 称号币", playerInfo.getLastUseName(), args[2]);
                MiniPlayerTitle.notification.info(sender, "玩家 %s 当前余额 %f 称号币", playerInfo.getLastUseName(), playerInfo.getCoin());
            } else {
                MiniPlayerTitle.notification.error(sender, "给玩家设置称号币失败，详细错误请查看控制台日志");
            }
        } catch (Exception e) {
            MiniPlayerTitle.notification.error(sender, "给玩家设置称号币时出错：%s", e.getMessage());
        }
    }
}
