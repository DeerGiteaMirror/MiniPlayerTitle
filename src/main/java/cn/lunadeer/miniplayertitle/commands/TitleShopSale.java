package cn.lunadeer.miniplayertitle.commands;

import cn.lunadeer.minecraftpluginutils.Notification;
import cn.lunadeer.miniplayertitle.dtos.PlayerInfoDTO;
import cn.lunadeer.miniplayertitle.dtos.PlayerTitleDTO;
import cn.lunadeer.miniplayertitle.dtos.TitleDTO;
import cn.lunadeer.miniplayertitle.dtos.TitleShopDTO;
import cn.lunadeer.miniplayertitle.tuis.MyTitles;
import cn.lunadeer.miniplayertitle.tuis.SaleInfo;
import cn.lunadeer.miniplayertitle.tuis.Shop;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static cn.lunadeer.miniplayertitle.tuis.Apis.getArgPage;
import static cn.lunadeer.miniplayertitle.tuis.Apis.getLastArgsPage;

public class TitleShopSale {

    /**
     * 设置商品信息
     * mplt set_sale <price|days|amount|end_at|more_end_at|less_end_at> <商品ID> <值> [页数]
     *
     * @param sender CommandSender
     * @param args   String[]
     */
    public static void setSale(CommandSender sender, String[] args) {
        if (!sender.hasPermission("mplt.admin")) return;
        TitleShopDTO titleShop = TitleShopDTO.get(Integer.valueOf(args[2]));
        if (titleShop == null) {
            Notification.error(sender, "获取详情时出现错误，详情请查看控制台日志");
            return;
        }
        boolean success;
        switch (args[1]) {
            case "price":
                success = titleShop.setPrice(Double.parseDouble(args[3]));
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
            case "more_end_at":
                int days = Integer.parseInt(args[3]);
                success = titleShop.setSaleEndAt(titleShop.getSaleEndAt().plusDays(days));
                break;
            case "less_end_at":
                int days2 = Integer.parseInt(args[3]);
                success = titleShop.setSaleEndAt(titleShop.getSaleEndAt().minusDays(days2));
                break;
            default:
                Notification.warn(sender, "用法: /mplt set_sale <price|days|amount|end_at|end_at_y|end_at_m|end_at_d> <商品ID> <值> [页数]");
                return;
        }
        if (!success) {
            Notification.error(sender, "设置商品信息时出现错误，详情请查看控制台日志");
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
        if (!sender.hasPermission("mplt.admin")) return;
        TitleDTO title = TitleDTO.get(Integer.parseInt(args[1]));
        if (title == null) {
            Notification.error(sender, "获取称号详情时出现错误，详情请查看控制台日志");
            return;
        }
        TitleShopDTO sale = TitleShopDTO.create(title);
        if (sale == null) {
            Notification.error(sender, "创建商品时出现错误，详情请查看控制台日志");
            return;
        }
        Notification.info(sender, "已创建称号商品");
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
        if (!sender.hasPermission("mplt.admin")) return;
        TitleShopDTO titleShop = TitleShopDTO.get(Integer.valueOf(args[1]));
        if (titleShop == null) {
            Notification.error(sender, "获取详情时出现错误");
            return;
        }
        boolean success = titleShop.delete();
        if (success) {
            Notification.info(sender, "已删除商品");
        } else {
            Notification.error(sender, "删除商品时出现错误，详情请查看控制台日志");
        }

        if (args.length == 3) {
            Shop.show(sender, new String[]{"shop"});
        }
    }

    /**
     * 购买商品
     * mplt buy_sale <商品ID>
     *
     * @param sender CommandSender
     * @param args   String[]
     */
    public static void buySale(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            Notification.error(sender, "该命令只能由玩家执行");
            return;
        }
        if (!sender.hasPermission("mplt.command")) return;
        Player player = (Player) sender;
        PlayerInfoDTO playerInfo = PlayerInfoDTO.get(player.getUniqueId());
        if (playerInfo == null) {
            Notification.error(player, "获取玩家信息时出现错误，详情请查看控制台日志");
            return;
        }
        TitleShopDTO titleShop = TitleShopDTO.get(Integer.valueOf(args[1]));
        if (titleShop == null) {
            Notification.error(player, "获取详情时出现错误，详情请查看控制台日志");
            return;
        }

        if (titleShop.isExpired() || titleShop.getDays() == 0) {
            Notification.error(player, "此称号已停止销售");
            return;
        }
        if (titleShop.getAmount() != -1 && titleShop.getAmount() <= 0) {
            Notification.error(player, "此称号已售罄");
            return;
        }
        if (titleShop.getPrice() > playerInfo.getCoin()) {
            Notification.error(player, "你的余额不足");
            return;
        }

        List<PlayerTitleDTO> playerTitles = PlayerTitleDTO.getAllOf(player.getUniqueId());
        PlayerTitleDTO had = null;
        for (PlayerTitleDTO playerTitle : playerTitles) {
            if (Objects.equals(playerTitle.getTitle().getId(), titleShop.getTitle().getId())) {
                had = playerTitle;
                break;
            }
        }

        if (had == null) {
            had = PlayerTitleDTO.create(player.getUniqueId(), titleShop.getTitle(), titleShop.getDays() == -1 ? null : LocalDateTime.now().plusDays(titleShop.getDays()));
            if (had == null) {
                Notification.error(player, "购买称号时出现错误，详情请查看控制台日志");
                return;
            }
            if (titleShop.getAmount() >= 1) {
                titleShop.setAmount(titleShop.getAmount() - 1);
            }
            playerInfo.setCoin(playerInfo.getCoin() - titleShop.getPrice());
            Notification.info(player, Component.text("成功购买称号: ").append(had.getTitle().getTitleColored()));
        } else if (!had.isExpired()) {
            Notification.warn(player, "你已拥有此称号，在过期前无法再次购买");
        } else {
            had.setExpireAt(titleShop.getDays() == -1 ? null : LocalDateTime.now().plusDays(titleShop.getDays()));
            if (titleShop.getAmount() >= 1) {
                titleShop.setAmount(titleShop.getAmount() - 1);
            }
            playerInfo.setCoin(playerInfo.getCoin() - titleShop.getPrice());
            Notification.info(player, Component.text("成功续续期称号: ").append(had.getTitle().getTitleColored()));
        }

        TitleManage.useTitle(player, new String[]{"use_title", String.valueOf(had.getId())});
        int page = getArgPage(args, 3);
        MyTitles.show(sender, new String[]{"my_titles", String.valueOf(page)});
    }

}
