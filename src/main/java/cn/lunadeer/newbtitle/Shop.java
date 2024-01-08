package cn.lunadeer.newbtitle;

import cn.lunadeer.newbtitle.utils.Database;
import cn.lunadeer.newbtitle.utils.Notification;
import cn.lunadeer.newbtitle.utils.STUI.Button;
import cn.lunadeer.newbtitle.utils.STUI.Line;
import cn.lunadeer.newbtitle.utils.STUI.View;
import cn.lunadeer.newbtitle.utils.XLogger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class Shop {
    public static void open(CommandSender sender, Integer page) {
        Map<Integer, SaleTitle> titles = getSaleTitles();
        if (!(sender instanceof Player)) {
            for (Integer title_sale_id : titles.keySet()) {
                SaleTitle title = titles.get(title_sale_id);
                XLogger.info("[" + title_sale_id + "] " + title.getTitle().toString() + " price:" + title.getPrice() + " days:" + title.getDays() + " amount:" + title.getAmount());
            }
            return;
        }
        Player player = (Player) sender;
        int offset = (page - 1) * 4;
        if (offset >= titles.size() || offset < 0) {
            Notification.error(player, "页数超出范围");
            return;
        }
        View view = View.create();
        view.title("称号商店");
        for (int i = offset; i < offset + 4; i++) {
            if (i >= titles.size()) {
                break;
            }
            Integer title_sale_id = (Integer) titles.keySet().toArray()[i];
            TextComponent idx = Component.text("[" + title_sale_id + "] ");
            SaleTitle title = titles.get(title_sale_id);
            Line line = Line.create();
            Component button = Button.create("购买", "/nt buy " + title_sale_id);
            line.append(idx)
                    .append(title.getTitle())
                    .append("价格:" + title.getPrice() + " 有效期:" + title.getDays() + "天")
                    .append("售卖截止:" + title.getSaleEndAt())
                    .append("剩余:" + ((title.getAmount() == -1) ? "无限" : title.getAmount()))
                    .append(button);
            view.set(i, line);
        }
        view.set(View.Slot.ACTIONBAR, View.pagination(page, titles.size(), "/nt shop"));
        view.showOn(player);
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
        sql += "sale_end_at ";
        sql += "FROM nt_title_shop;";
        Map<Integer, SaleTitle> titles = new HashMap<>();
        try (ResultSet rs = Database.query(sql)) {
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
        sql += "sale_end_at ";
        sql += "FROM nt_title_shop ";
        sql += "WHERE id = " + sale_id + ";";
        try (ResultSet rs = Database.query(sql)) {
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
