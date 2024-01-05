package cn.lunadeer.newbtitle;

import cn.lunadeer.newbtitle.utils.Database;
import cn.lunadeer.newbtitle.utils.Notification;
import cn.lunadeer.newbtitle.utils.STUI.Line;
import cn.lunadeer.newbtitle.utils.STUI.View;
import cn.lunadeer.newbtitle.utils.XLogger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class Shop {
    public static void open(Player player, Integer page) {
        Line header = Line.create();
        header.set(Line.Slot.LEFT, "称号")
                .set(Line.Slot.MIDDLE, "价格｜天｜剩余")
                .set(Line.Slot.RIGHT, "操作");

        Map<Integer, SaleTitle> titles = getSaleTitles();
        int offset = (page - 1) * 4;
        if (offset >= titles.size() || offset < 0) {
            Notification.error(player, "页数超出范围");
            return;
        }
        View view = View.create();
        view.title("｜｜ 称号商店 ｜｜")
                .set(View.Slot.SUBTITLE, header);
        for (int i = offset; i < offset + 4; i++) {
            if (i >= titles.size()) {
                break;
            }
            Integer title_sale_id = (Integer) titles.keySet().toArray()[i];
            TextComponent idx = Component.text("[" + title_sale_id + "] ");
            SaleTitle title = titles.get(title_sale_id);
            Line line = Line.create();
            TextComponent buy_button = Component.text("购买")
                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/nt shop buy " + title_sale_id));
            line.set(Line.Slot.LEFT, idx.append(title.getTitle()))
                    .set(Line.Slot.MIDDLE, title.getPrice() + "｜" +
                            (title.getDays() < 0 ? "∞" : title.getDays()) + "｜" +
                            (title.getAmount() < 0 ? "∞" : title.getAmount()))
                    .set(Line.Slot.RIGHT, buy_button);
            view.set(View.Slot.LINE_1, line);
        }
        Line action_bar = Line.create();
        TextComponent previous_button = Component.text("上一页")
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/nt shop " + (page - 1)));
        TextComponent next_button = Component.text("下一页")
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/nt shop " + (page + 1)));
        action_bar.set(Line.Slot.MIDDLE, previous_button)
                .set(Line.Slot.RIGHT, next_button);
        view.set(View.Slot.ACTIONBAR, action_bar);
        view.showOn(player);
    }

    public static void addTitle(Integer title_id) {
        String sql = "";
        sql += "INSERT INTO nt_title_shop (title_id, price, days, amount, sale_end_at) ";
        sql += "VALUES (" + title_id + ", 0, 0, -1, CURRENT_TIMESTAMP) ";
        sql += "RETURNING id;";
        ResultSet rs = Database.query(sql);
        try {
            if (rs != null && rs.next()) {
                Integer id = rs.getInt("id");
                SaleTitle title = new SaleTitle(id, title_id, 0, 0, -1, System.currentTimeMillis());
            }
        } catch (Exception e) {
            XLogger.err("SaleTitle create failed: " + e.getMessage());
        }
    }

    public static void deleteTitle(Integer id) {
        String sql = "";
        sql += "DELETE FROM nt_title_shop WHERE id = " + id + ";";
        Database.query(sql);
    }

    public static Map<Integer, SaleTitle> getSaleTitles() {
        String sql = "";
        sql += "SELECT ";
        sql += "id, ";
        sql += "title_id, ";
        sql += "price, ";
        sql += "days, ";
        sql += "amount, ";
        sql += "sale_end_at";
        sql += "FROM nt_title_shop;";
        ResultSet rs = Database.query(sql);
        Map<Integer, SaleTitle> titles = new HashMap<>();
        try {
            while (rs != null && rs.next()) {
                Integer id = rs.getInt("id");
                Integer title_id = rs.getInt("title_id");
                Integer price = rs.getInt("price");
                Integer days = rs.getInt("days");
                Integer amount = rs.getInt("amount");
                Long sale_end_at = rs.getLong("sale_end_at");
                SaleTitle title = new SaleTitle(id, title_id, price, days, amount, sale_end_at);
                titles.put(id, title);
            }
        } catch (Exception e) {
            XLogger.err("XPlayer getTitles failed: " + e.getMessage());
        }
        return titles;
    }

    public static SaleTitle getSaleTitle(Integer sale_id) {
        String sql = "";
        sql += "SELECT ";
        sql += "id, ";
        sql += "title_id, ";
        sql += "price, ";
        sql += "days, ";
        sql += "amount, ";
        sql += "sale_end_at";
        sql += "FROM nt_title_shop ";
        sql += "WHERE id = " + sale_id + ";";
        ResultSet rs = Database.query(sql);
        try {
            if (rs != null && rs.next()) {
                Integer id = rs.getInt("id");
                Integer title_id = rs.getInt("title_id");
                Integer price = rs.getInt("price");
                Integer days = rs.getInt("days");
                Integer amount = rs.getInt("amount");
                Long sale_end_at = rs.getLong("sale_end_at");
                return new SaleTitle(id, title_id, price, days, amount, sale_end_at);
            }
        } catch (Exception e) {
            XLogger.err("XPlayer getTitles failed: " + e.getMessage());
        }
        return null;
    }
}
