package cn.lunadeer.newbtitle;

import cn.lunadeer.newbtitle.commands.AdminCommands;
import cn.lunadeer.newbtitle.utils.Notification;
import cn.lunadeer.newbtitle.utils.XLogger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static cn.lunadeer.newbtitle.commands.PlayerCommands.*;

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
            case "mplt":
                if (args.length == 0) {
                    printHelp(sender);
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
                    case "create":
                        return AdminCommands.createTitle(sender, args);
                    case "delete":
                        return AdminCommands.deleteTitle(sender, args);
                    case "setdesc":
                        return AdminCommands.setTitleDescription(sender, args);
                    case "setname":
                        return AdminCommands.setTitleName(sender, args);
                    case "addshop":
                        return AdminCommands.addShop(sender, args);
                    case "removeshop":
                        return AdminCommands.removeShop(sender, args);
                    case "setprice":
                        return AdminCommands.setPrice(sender, args);
                    case "setamount":
                        return AdminCommands.setAmount(sender, args);
                    case "setendat":
                        return AdminCommands.setSaleEndAt(sender, args);
                    case "listall":
                        return AdminCommands.listAllTitle(sender, args);
                    default:
                        printHelp(sender);
                        return true;
                }
            default:
                return false;
        }
    }

    private void printHelp(@NotNull CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Notification.warn(player, "用法: /mplt <use|list|shop|buy>");
            if (player.isOp()) {
                Notification.warn(player, "用法: /mplt <create|delete|setdesc|setname|addshop|removeshop|setprice|setamount|setendat|listall>");
            }
        } else {
            XLogger.info("用法: /mplt <use|list|shop|buy>");
            XLogger.info("用法: /mplt <create|delete|setdesc|setname|addshop|removeshop|setprice|setamount|setendat|listall>");
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
        if (args.length == 1) {
            String[] player_cmd = {"use", "list", "shop", "buy"};
            String[] admin_cmd = {"create", "delete", "setdesc", "setname", "addshop", "removeshop", "setprice", "setamount", "setendat", "listall"};
            List<String> res = new ArrayList<>();
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.isOp()) {
                    res.addAll(Arrays.asList(player_cmd));
                    res.addAll(Arrays.asList(admin_cmd));
                } else {
                    res.addAll(Arrays.asList(player_cmd));
                }
            } else {
                res.addAll(Arrays.asList(player_cmd));
                res.addAll(Arrays.asList(admin_cmd));
            }
            return res;
        }
        switch (args[0]) {
            case "use":
                return Collections.singletonList("要使用的称号ID");
            case "list":
            case "shop":
                return Collections.singletonList("页数(可选)");
            case "buy":
                return Collections.singletonList("要购买的条目ID");
            case "create":
                return Collections.singletonList("<称号名称> <称号描述>");
            case "delete":
            case "addshop":
                return Collections.singletonList("<称号ID>");
            case "setdesc":
                return Collections.singletonList("<称号ID> <称号描述>");
            case "setname":
                return Collections.singletonList("<称号ID> <称号名称>");
            case "removeshop":
                return Collections.singletonList("<商品ID>");
            case "setprice":
                return Collections.singletonList("<商品ID> <价格> <天数>(-1为永久)");
            case "setamount":
                return Collections.singletonList("<商品ID> <数量> (-1为无限)");
            case "setendat":
                return Collections.singletonList("<商品ID> <结束时间戳>(-1为永久)");
            default:
                return Arrays.asList("use", "list", "shop", "buy");
        }

    }


}
