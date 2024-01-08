package cn.lunadeer.newbtitle.utils;

import cn.lunadeer.newbtitle.MiniPlayerTitle;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class XLogger {
    private static final MiniPlayerTitle _plugin = MiniPlayerTitle.instance;
    private static final Logger _logger = _plugin.getLogger();

    private static final String prefix = "[MiniPlayerTitle] ";

    public static void info(Player player, String message) {
        Notification.info(player, prefix + "I | " + message);
        if (MiniPlayerTitle.config.isDebug())
            debug("来自玩家[ " + player.getName() + " ] 的信息 | " + message);
    }

    public static void info(String message) {
        _logger.info(" I | " + message);
    }

    public static void warn(Player player, String message) {
        Notification.warn(player, prefix + "W | " + message);
        if (MiniPlayerTitle.config.isDebug())
            debug("来自玩家[ " + player.getName() + " ] 的警告 | " + message);
    }

    public static void warn(String message) {
        _logger.info(" W | " + message);
    }

    public static void err(Player player, String message) {
        Notification.error(player, prefix + "E | " + message);
        if (MiniPlayerTitle.config.isDebug())
            debug("来自玩家[ " + player.getName() + " ] 的报错 | " + message);
    }

    public static void err(String message) {
        _logger.info(" E | " + message);
    }

    public static void debug(Player player, String message) {
        if (!MiniPlayerTitle.config.isDebug()) return;
        if (player.isOp())
            Notification.info(player, prefix + "D | " + message);
        else
            debug("来自玩家[ " + player.getName() + " ] 的调试 | " + message);
    }

    public static void debug(String message) {
        if (!MiniPlayerTitle.config.isDebug()) return;
        _logger.info(" D | " + message);
    }
}
