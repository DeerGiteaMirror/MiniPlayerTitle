package cn.lunadeer.miniplayertitle.commands;

import cn.lunadeer.miniplayertitle.MiniPlayerTitle;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Apis {
    public static boolean notOpOrConsole(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.isOp()) {
                MiniPlayerTitle.notification.warn(player, "你没有权限使用此命令");
                return true;
            }
        }
        return false;
    }
}
