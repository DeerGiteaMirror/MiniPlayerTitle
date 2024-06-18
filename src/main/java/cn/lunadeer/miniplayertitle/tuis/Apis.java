package cn.lunadeer.miniplayertitle.tuis;

import cn.lunadeer.minecraftpluginutils.Notification;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Apis {
    public static Player playerOnly(CommandSender sender) {
        if (!(sender instanceof Player)) {
            Notification.error(sender, "该命令只能由玩家执行");
            return null;
        }
        return (Player) sender;
    }

    public static int getLastArgsPage(String[] args) {
        if (args.length == 0) return 1;
        int page = 1;
        try {
            page = Integer.parseInt(args[args.length - 1]);
        } catch (Exception ignored) {
        }
        return page;
    }

    public static int getArgPage(String[] args, int pos) {
        int page = 1;
        if (args.length > pos - 1) {
            try {
                page = Integer.parseInt(args[pos - 1]);
            } catch (Exception ignored) {
            }
        }
        return page;
    }
}
