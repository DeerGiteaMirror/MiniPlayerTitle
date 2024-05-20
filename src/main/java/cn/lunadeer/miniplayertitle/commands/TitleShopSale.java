package cn.lunadeer.miniplayertitle.commands;

import cn.lunadeer.miniplayertitle.MiniPlayerTitle;
import cn.lunadeer.miniplayertitle.dtos.TitleDTO;
import cn.lunadeer.miniplayertitle.dtos.TitleShopDTO;
import cn.lunadeer.miniplayertitle.tuis.SaleInfo;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static cn.lunadeer.miniplayertitle.commands.Apis.notOpOrConsole;
import static cn.lunadeer.miniplayertitle.tuis.Apis.getLastArgsPage;

public class TitleShopSale {

    /**
     * 设置商品信息
     * mplt set_sale <price|days|amount|end_at|end_at_y|end_at_m|end_at_d> <商品ID> <值> [页数]
     *
     * @param sender CommandSender
     * @param args   String[]
     */
    public static void setSale(CommandSender sender, String[] args) {
        if (notOpOrConsole(sender)) return;
        TitleShopDTO titleShop = TitleShopDTO.get(Integer.valueOf(args[2]));
        if (titleShop == null) {
            MiniPlayerTitle.notification.error(sender, "获取详情时出现错误，详情请查看控制台日志");
            return;
        }
        boolean success;
        switch (args[1]) {
            case "price":
                success = titleShop.setPrice(Integer.parseInt(args[3]));
                break;
            case "days":
                success = titleShop.setDays(Integer.parseInt(args[3]));
                break;
            case "amount":
                success = titleShop.setAmount(Integer.parseInt(args[3]));
                break;
            case "end_at":
                String[] date = args[3].split(":");
                int year = Integer.parseInt(date[0]);
                int month = Integer.parseInt(date[1]);
                int day = Integer.parseInt(date[2]);
                success = titleShop.setSaleEndAt(year, month, day);
                break;
            case "end_at_y":
                success = titleShop.setSaleEndAtY(Integer.parseInt(args[3]));
                break;
            case "end_at_m":
                success = titleShop.setSaleEndAtM(Integer.parseInt(args[3]));
                break;
            case "end_at_d":
                success = titleShop.setSaleEndAtD(Integer.parseInt(args[3]));
                break;
            default:
                MiniPlayerTitle.notification.warn(sender, "用法: /mplt set_sale <price|days|amount|end_at|end_at_y|end_at_m|end_at_d> <商品ID> <值> [页数]");
                return;
        }
        if (!success) {
            MiniPlayerTitle.notification.error(sender, "设置商品信息时出现错误，详情请查看控制台日志");
        }
        if (args.length == 5) {
            int page = getLastArgsPage(args);
            SaleInfo.show(sender, new String[]{"sale_info", args[2], String.valueOf(page)});
        }
    }

    /**
     * 创建商品
     * mplt create_sale <称号ID>
     *
     * @param sender CommandSender
     * @param args   String[]
     */
    public static void createSale(CommandSender sender, String[] args) {
        if (notOpOrConsole(sender)) return;
        TitleDTO title = TitleDTO.get(Integer.parseInt(args[1]));
        if (title == null) {
            MiniPlayerTitle.notification.error(sender, "获取称号详情时出现错误，详情请查看控制台日志");
            return;
        }
        TitleShopDTO sale = TitleShopDTO.create(title);
        if (sale == null) {
            MiniPlayerTitle.notification.error(sender, "创建商品时出现错误，详情请查看控制台日志");
            return;
        }
        MiniPlayerTitle.notification.info(sender, "已创建称号商品");
        if (sender instanceof Player) {
            SaleInfo.show(sender, new String[]{"sale_info", String.valueOf(sale.getId())});
        }
    }

    /**
     * 删除商品
     * mplt delete_sale <商品ID> [页数]
     *
     * @param sender CommandSender
     * @param args   String[]
     */
    public static void deleteSale(CommandSender sender, String[] args) {
        if (notOpOrConsole(sender)) return;
        TitleShopDTO titleShop = TitleShopDTO.get(Integer.valueOf(args[1]));
        if (titleShop == null) {
            MiniPlayerTitle.notification.error(sender, "获取详情时出现错误");
            return;
        }
        boolean success = titleShop.delete();
        if (success) {
            MiniPlayerTitle.notification.info(sender, "已删除商品");
        } else {
            MiniPlayerTitle.notification.error(sender, "删除商品时出现错误，详情请查看控制台日志");
        }

        if (args.length == 3) {
            int page = getLastArgsPage(args);
            SaleInfo.show(sender, new String[]{"sale_info", args[1], String.valueOf(page)});
        }
    }

}
