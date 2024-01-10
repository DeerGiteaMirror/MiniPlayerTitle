package cn.lunadeer.miniplayertitle;

import cn.lunadeer.miniplayertitle.commands.AdminCommands;
import cn.lunadeer.miniplayertitle.utils.Notification;
import cn.lunadeer.miniplayertitle.utils.STUI.Button;
import cn.lunadeer.miniplayertitle.utils.STUI.Line;
import cn.lunadeer.miniplayertitle.utils.STUI.View;
import cn.lunadeer.miniplayertitle.utils.XLogger;
import net.kyori.adventure.text.Component;
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

import static cn.lunadeer.miniplayertitle.commands.PlayerCommands.*;

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
                    home_view(sender);
                    return true;
                }
                switch (args[0]) {
                    case "use":
                        use(sender, args);
                        return true;
                    case "list":
                        list(sender, args);
                        return true;
                    case "shop":
                        shop(sender, args);
                        return true;
                    case "buy":
                        buy(sender, args);
                        return true;
                    case "create":
                        AdminCommands.createTitle(sender, args);
                        return true;
                    case "delete":
                        AdminCommands.deleteTitle(sender, args);
                        return true;
                    case "setdesc":
                        AdminCommands.setTitleDescription(sender, args);
                        return true;
                    case "setname":
                        AdminCommands.setTitleName(sender, args);
                        return true;
                    case "addshop":
                        AdminCommands.addShop(sender, args);
                        return true;
                    case "removeshop":
                        AdminCommands.removeShop(sender, args);
                        return true;
                    case "setprice":
                        AdminCommands.setPrice(sender, args);
                        return true;
                    case "setamount":
                        AdminCommands.setAmount(sender, args);
                        return true;
                    case "setendat":
                        AdminCommands.setSaleEndAt(sender, args);
                        return true;
                    case "listall":
                        AdminCommands.listAllTitle(sender, args);
                        return true;
                    case "addcoin":
                        AdminCommands.addCoin(sender, args);
                        return true;
                    case "setcoin":
                        AdminCommands.setCoin(sender, args);
                        return true;
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

    private void home_view(CommandSender sender) {
        if (!(sender instanceof Player)) {
            printHelp(sender);
            return;
        }
        View view = View.create();
        view.title("称号系统");
        Component backpack = Button.create("称号背包", "/mplt list");
        Component shop = Button.create("称号商店", "/mplt shop");
        Line line = Line.create();
        line.append(backpack).append(shop);
        view.actionBar(line);
        view.showOn((Player) sender);
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
            String[] admin_cmd = {"create", "delete", "setdesc", "setname", "addshop", "removeshop", "setprice", "setamount", "setendat", "listall", "addcoin", "setcoin"};
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
        if (args.length == 2) {
            switch (args[0]) {
                case "use":
                    return Collections.singletonList("要使用的称号ID");
                case "list":
                case "shop":
                case "listall":
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
                    return Collections.singletonList("<商品ID> <数量>(-1为无限)");
                case "setendat":
                    return Collections.singletonList("<商品ID> <结束时间YYYYMMDD>(-1为永久)");
                case "addcoin":
                case "setcoin":
                    // return player list
                    List<String> res = new ArrayList<>();
                    for (Player player : MiniPlayerTitle.instance.getServer().getOnlinePlayers()) {
                        res.add(player.getName());
                    }
                    return res;
                default:
                    return Arrays.asList("use", "list", "shop", "buy");
            }
        }
        if (args.length == 3) {
            switch (args[0]) {
                case "addcoin":
                case "setcoin":
                    return Collections.singletonList("<金币数量>");
                case "create":
                case "setdesc":
                    return Collections.singletonList("<称号描述>");
                case "setname":
                    return Collections.singletonList("<称号名称>");
                case "setprice":
                    return Collections.singletonList("<价格> <天数>(-1为永久)");
                case "setamount":
                    return Collections.singletonList("<数量>(-1为无限)");
                case "setendat":
                    return Collections.singletonList("<结束时间YYYYMMDD>(-1为永久)");
            }
        }
        if (args.length == 4) {
            if (args[0].equals("setprice")) {
                return Collections.singletonList("<天数>(-1为永久)");
            }
        }
        return Arrays.asList("use", "list", "shop", "buy");
    }


}
