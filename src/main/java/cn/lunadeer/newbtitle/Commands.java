package cn.lunadeer.newbtitle;

import cn.lunadeer.newbtitle.utils.Notification;
import cn.lunadeer.newbtitle.utils.XLogger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Commands implements TabExecutor {
    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        switch (label) {
            case "nt":
                if (args.length == 0) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        Notification.warn(player, "用法: /nt <use|list|shop|buy>");
                    } else  {
                        XLogger.info("用法: /nt <use|list|shop|buy>");
                    }
                    return true;
                }
                switch (args[0]) {
                    case "use":
                        return use(sender, args);
                    case "list":
                        return list(sender, args);
                    case "shop":
                        return shop(sender, args);
                    case "buy":
                        return buy(sender, args);
                    default:
                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            Notification.warn(player, "用法: /nt <use|list|shop|buy>");
                        } else  {
                            XLogger.info("用法: /nt <use|list|shop|buy>");
                        }
                        return true;
                }
            default:
                return false;
        }
    }

    /**
     * Requests a list of possible completions for a command argument.
     *
     * @param sender  Source of the command.  For players tab-completing a
     *                command inside a command block, this will be the player, not
     *                the command block.
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    The arguments passed to the command, including final
     *                partial argument to be completed
     * @return A List of possible completions for the final argument, or null
     * to default to the command executor
     */
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        switch (label) {
            case "nt":
                if (args.length == 0) {
                    return Arrays.asList("use", "list", "shop", "buy");
                }
                switch (args[0]) {
                    case "use":
                        return Collections.singletonList("要使用的称号ID");
                    case "list":
                        return Collections.singletonList("页数(可选)");
                    case "shop":
                        return Collections.singletonList("页数(可选)");
                    case "buy":
                        return Collections.singletonList("要购买的条目ID");
                    default:
                        return Arrays.asList("use", "list", "shop", "buy");
                }
            default:
                return null;
        }
    }

    private static boolean use(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            XLogger.warn("该命令只能由玩家执行");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            Notification.warn(player, "用法: /nt use <称号ID>");
            return true;
        }
        XPlayer xPlayer = new XPlayer(player);
        Integer title_id = Integer.parseInt(args[0]);
        xPlayer.updateUsingTitle(title_id);
        return true;
    }

    private static boolean list(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            XLogger.warn("该命令只能由玩家执行");
            return true;
        }
        Player player = (Player) sender;
        int page = 1;
        if (args.length != 0) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (Exception e) {
                Notification.warn(player, "用法: /nt list <页数>");
                return true;
            }
        }
        XPlayer xPlayer = new XPlayer(player);
        xPlayer.openBackpack(page);
        return true;
    }

    private static boolean shop(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            XLogger.warn("该命令只能由玩家执行");
            return true;
        }
        Player player = (Player) sender;
        int page = 1;
        if (args.length != 0) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (Exception e) {
                Notification.warn(player, "用法: /nt shop <页数>");
                return true;
            }
        }
        Shop.open(player, page);
        return true;
    }

    private static boolean buy(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            XLogger.warn("该命令只能由玩家执行");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            Notification.warn(player, "用法: /nt buy <称号ID>");
            return true;
        }
        XPlayer xPlayer = new XPlayer(player);
        Integer sale_id = Integer.parseInt(args[0]);
        SaleTitle saleTitle = Shop.getSaleTitle(sale_id);
        if (saleTitle == null) {
            Notification.error(player, "该称号不存在");
            return true;
        }
        xPlayer.buyTitle(saleTitle);
        return true;
    }
}
