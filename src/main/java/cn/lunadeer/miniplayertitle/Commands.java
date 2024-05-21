package cn.lunadeer.miniplayertitle;

import cn.lunadeer.miniplayertitle.commands.TitleManage;
import cn.lunadeer.miniplayertitle.commands.TitleShopSale;
import cn.lunadeer.miniplayertitle.tuis.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Commands implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            Menu.show(sender, args);
            return true;
        }
        switch (args[0]) {
            case "menu":
                Menu.show(sender, args);
                break;
            case "all_titles":
                AllTitles.show(sender, args);
                break;
            case "my_titles":
                MyTitles.show(sender, args);
                break;
            case "shop":
                Shop.show(sender, args);
                break;
            case "custom_info":
                CustomInfo.show(sender, args);
                break;
            case "sale_info":
                SaleInfo.show(sender, args);
                break;
            case "create_sale":
                TitleShopSale.createSale(sender, args);
                break;
            case "set_sale":
                TitleShopSale.setSale(sender, args);
                break;
            case "delete_sale":
                TitleShopSale.deleteSale(sender, args);
                break;
            case "buy_sale":
                TitleShopSale.buySale(sender, args);
                break;
            case "use_title":
                TitleManage.useTitle(sender, args);
                break;
            case "create_title":
                TitleManage.createTitle(sender, args);
                break;
            case "delete_title":
                TitleManage.deleteTitle(sender, args);
                break;
            case "set_title":
                TitleManage.setTitle(sender, args);
                break;
            case "set_desc":
                TitleManage.setTitleDescription(sender, args);
                break;
            case "custom_title":
                TitleManage.customTitle(sender, args);
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
