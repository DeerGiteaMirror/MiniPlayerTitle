package cn.lunadeer.miniplayertitle.tuis;

import cn.lunadeer.minecraftpluginutils.stui.ListView;
import cn.lunadeer.minecraftpluginutils.stui.components.Button;
import cn.lunadeer.minecraftpluginutils.stui.components.Line;
import cn.lunadeer.minecraftpluginutils.stui.components.NumChanger;
import cn.lunadeer.miniplayertitle.MiniPlayerTitle;
import cn.lunadeer.miniplayertitle.dtos.TitleDTO;
import cn.lunadeer.miniplayertitle.dtos.TitleShopDTO;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;

import static cn.lunadeer.miniplayertitle.tuis.Apis.*;

public class SaleInfo {
    public static void show(CommandSender sender, String[] args) {
        Player player = playerOnly(sender);
        if (player == null) return;
        int page = getArgPage(args, 3);
        TitleShopDTO titleShop = TitleShopDTO.get(Integer.valueOf(args[1]));
        if (titleShop == null) {
            MiniPlayerTitle.notification.error(player, "获取详情时出现错误");
            return;
        }
        TitleDTO title = titleShop.getTitle();
        if (title == null) {
            MiniPlayerTitle.notification.error(player, "获取详情时出现错误");
            return;
        }
        int now_year = LocalDateTime.now().getYear();
        int now_month = LocalDateTime.now().getMonthValue();
        int now_day = LocalDateTime.now().getDayOfMonth();
        ListView view = ListView.create(10, "/mplt sale_info " + args[1]);
        view.title("销售详情");
        view.navigator(Line.create()
                .append(Button.create("主菜单").setExecuteCommand("/mplt menu").build())
                .append(Button.create("称号商店").setExecuteCommand("/mplt shop").build())
                .append("销售详情"));
        view.add(Line.create().append("内容: ").append(titleShop.getTitle().getTitleColored()));
        view.add(Line.create().append("描述: ").append(titleShop.getTitle().getDescription()));

        Line price = Line.create().append("价格: ");
        if (titleShop.getPrice() <= 0) {
            price.append("免费");
            if (player.isOp()) {
                price.append(Button.create("设置价格").setExecuteCommand("/mplt set_sale price " + titleShop.getId() + " 100 " + page).build());
            }
        } else {
            if (player.isOp()) {
                price.append(NumChanger.create(titleShop.getPrice(), "/mplt set_sale price " + titleShop.getId()).setPageNumber(page).build());
                price.append(Button.create("设置为免费").setExecuteCommand("/mplt set_sale price " + titleShop.getId() + " 0 " + page).build());
            } else {
                price.append(titleShop.getPrice().toString());
            }
        }
        view.add(price);

        Line day = Line.create().append("购买天数: ");
        if (titleShop.getDays() <= 0) {
            day.append("永久");
            if (player.isOp()) {
                day.append(Button.create("转为限时").setExecuteCommand("/mplt set_sale days " + titleShop.getId() + " 7 " + page).build());
            }
        } else {
            if (player.isOp()) {
                day.append(NumChanger.create(titleShop.getDays(), "/mplt set_sale days " + titleShop.getId()).setPageNumber(page).build());
                day.append(Button.create("转为永久").setExecuteCommand("/mplt set_sale days " + titleShop.getId() + " -1 " + page).build());
            } else {
                day.append(titleShop.getDays().toString());
            }
        }
        view.add(day);

        Line amount = Line.create().append("剩余数量: ");
        if (titleShop.getAmount() == -1) {
            amount.append("无限");
            if (player.isOp()) {
                amount.append(Button.create("转为限量").setExecuteCommand("/mplt set_sale amount " + titleShop.getId() + " 0 " + page).build());
            }
        } else {

            if (player.isOp()) {
                amount.append(NumChanger.create(titleShop.getAmount(), "/mplt set_sale amount " + titleShop.getId()).setPageNumber(page).build());
                amount.append(Button.create("转为无限").setExecuteCommand("/mplt set_sale amount " + titleShop.getId() + " -1 " + page).build());
            } else {
                amount.append(titleShop.getAmount().toString());
            }
        }
        view.add(amount);

        Line end_at = Line.create().append("售卖结束时间: ");
        if (titleShop.getSaleEndAtY() == -1 || titleShop.getSaleEndAtM() == -1 || titleShop.getSaleEndAtD() == -1) {
            end_at.append("常驻");
            if (player.isOp()) {
                end_at.append(Button.create("转为限时").setExecuteCommand("/mplt set_sale end_at " + titleShop.getId() + " " + now_year + ":" + now_month + ":" + now_day + " " + page).build());
            }
        } else {
            if (player.isOp()) {
                end_at.append(NumChanger.create(titleShop.getSaleEndAtY(), "/mplt set_sale end_at_y " + titleShop.getId()).setPageNumber(page).build().append(Component.text("年")));
                end_at.append(NumChanger.create(titleShop.getSaleEndAtM(), "/mplt set_sale end_at_m " + titleShop.getId()).setPageNumber(page).build().append(Component.text("月")));
                end_at.append(NumChanger.create(titleShop.getSaleEndAtD(), "/mplt set_sale end_at_d " + titleShop.getId()).setPageNumber(page).build().append(Component.text("日")));
                end_at.append(Button.create("转为常驻").setExecuteCommand("/mplt set_sale end_at " + titleShop.getId() + " -1:-1:-1 " + page).build());
            } else {
                end_at.append(titleShop.getSaleEndAtY() + "年" + titleShop.getSaleEndAtM() + "月" + titleShop.getSaleEndAtD() + "日");
            }
        }
        view.add(end_at);

        Line operate = Line.create().append("操作: ");
        if (titleShop.getSaleEndAtY() != -1 && titleShop.getSaleEndAtM() != -1 && titleShop.getSaleEndAtD() != -1
                && (now_year > titleShop.getSaleEndAtY() || (now_year == titleShop.getSaleEndAtY() && now_month > titleShop.getSaleEndAtM()) || (now_year == titleShop.getSaleEndAtY() && now_month == titleShop.getSaleEndAtM() && now_day > titleShop.getSaleEndAtD()))) {
            operate.append(Button.createRed("已过期").build());
        } else if (titleShop.getAmount() == 0) {
            operate.append(Button.createRed("已售罄").build());
        } else {
            operate.append(Button.createGreen("购买").setExecuteCommand("/mplt buy_title " + args[1]).build());
        }
        if (player.isOp()) {
            operate.append(Button.create("删除").setExecuteCommand("/mplt delete_sale " + args[1] + " " + page).build());
        }
        view.showOn(player, page);
    }
}
