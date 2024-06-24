package cn.lunadeer.miniplayertitle;

import cn.lunadeer.miniplayertitle.commands.PlayerManage;
import cn.lunadeer.miniplayertitle.commands.TitleManage;
import cn.lunadeer.miniplayertitle.commands.TitleShopSale;
import cn.lunadeer.miniplayertitle.dtos.PlayerInfoDTO;
import cn.lunadeer.miniplayertitle.tuis.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Commands implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            Menu.show(sender, args);
            return true;
        }
        switch (args[0]) {
            case "menu":                // mplt menu [页数]
                Menu.show(sender, args);
                break;
            case "all_titles":          // mplt all_titles [页数]
                AllTitles.show(sender, args);
                break;
            case "my_titles":           // mplt my_titles [页数]
                MyTitles.show(sender, args);
                break;
            case "shop":                // mplt shop [页数]
                Shop.show(sender, args);
                break;
            case "custom_info":         // mplt custom_info
                CustomInfo.show(sender, args);
                break;
            case "sale_info":           // mplt sale_info <商品ID>
                SaleInfo.show(sender, args);
                break;
            case "create_sale":         // mplt create_sale <称号ID>
                TitleShopSale.createSale(sender, args);
                break;
            case "set_sale":            // mplt set_sale <price|days|amount|end_at|more_end_at|less_end_at> <商品ID> <值> [页数]
                TitleShopSale.setSale(sender, args);
                break;
            case "delete_sale":         // mplt delete_sale <商品ID> [页数]
                TitleShopSale.deleteSale(sender, args);
                break;
            case "buy_sale":            // mplt buy_sale <商品ID>
                TitleShopSale.buySale(sender, args);
                break;
            case "use_title":           // mplt use_title <背包ID> [页码]
                TitleManage.useTitle(sender, args);
                break;
            case "create_title":        // mplt create_title <称号名称> [称号描述]
                TitleManage.createTitle(sender, args);
                break;
            case "delete_title":        // mplt delete_title <称号ID>  [页码]
                TitleManage.deleteTitle(sender, args);
                break;
            case "edit_title_name":     // mplt edit_title_name <称号ID> <称号名称>
                TitleManage.editTitleName(sender, args);
                break;
            case "edit_title_desc":      // mplt set_title_desc <称号ID> <称号描述>
                TitleManage.editTitleDescription(sender, args);
                break;
            case "custom_title":        // mplt custom_title <称号>
                TitleManage.customTitle(sender, args);
                break;
            case "add_coin":            // mplt add_coin <玩家名称> <称号币数量>
                PlayerManage.addCoin(sender, args);
                break;
            case "set_coin":            // mplt set_coin <玩家名称> <称号币数量>
                PlayerManage.setCoin(sender, args);
                break;
            case "grant_title":         // mplt grant_title <玩家名称> <称号> <描述> [天数]
                PlayerManage.grantTitle(sender, args);
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Arrays.asList("menu", "all_titles", "my_titles", "shop", "custom_info", "sale_info",
                    "create_sale", "set_sale", "delete_sale", "buy_sale", "use_title", "create_title",
                    "delete_title", "edit_title_name", "edit_title_desc", "custom_title", "add_coin", "set_coin",
                    "grant_title"
            );
        }
        if (args.length == 2) {
            switch (args[0]) {
                case "sale_info":
                case "delete_sale":
                case "buy_sale":
                    return Collections.singletonList("<商品ID>");
                case "create_sale":
                case "delete_title":
                case "edit_title_name":
                case "edit_title_desc":
                    return Collections.singletonList("<称号ID>");
                case "use_title":
                    return Collections.singletonList("<背包ID>");
                case "set_sale":
                    return Arrays.asList("price", "days", "amount", "end_at", "more_end_at", "less_end_at");
                case "custom_title":
                case "create_title":
                    return Collections.singletonList("<称号内容>");
                case "add_coin":
                case "set_coin":
                case "grant_title":
                    return PlayerInfoDTO.playerNameList();
                default:
                    return null;
            }
        }
        if (args.length == 3) {
            switch (args[0]) {
                case "set_sale":
                    return Collections.singletonList("<商品ID>");
                case "edit_title_desc":
                    return Collections.singletonList("<新的称号描述>");
                case "edit_title_name":
                    return Collections.singletonList("<新的称号名称>");
                case "create_title":
                    return Collections.singletonList("<称号描述>");
                case "add_coin":
                case "set_coin":
                    return Collections.singletonList("<数量>");
                case "grant_title":
                    return Collections.singletonList("<称号>");
                default:
                    return null;
            }
        }
        if (args.length == 4) {
            switch (args[0]) {
                case "set_sale":
                    return Collections.singletonList("<值>");
                case "grant_title":
                    return Collections.singletonList("<描述>");
                default:
                    return null;
            }
        }
        if (args.length == 5) {
            switch (args[0]) {
                case "grant_title":
                    return Collections.singletonList("<天数(默认永久)>");
                default:
                    return null;
            }
        }
        return null;
    }
}
