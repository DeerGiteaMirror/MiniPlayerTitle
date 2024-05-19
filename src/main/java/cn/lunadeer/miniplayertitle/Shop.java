package cn.lunadeer.miniplayertitle;

import cn.lunadeer.minecraftpluginutils.stui.ListView;
import cn.lunadeer.minecraftpluginutils.stui.components.Button;
import cn.lunadeer.minecraftpluginutils.stui.components.Line;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class Shop {
    public static void open(CommandSender sender, Integer page) {
        Map<Integer, SaleTitle> titles = getSaleTitles();
        if (!(sender instanceof Player)) {
            for (SaleTitle title : titles.values()) {
                Component idx = Component.text("[" + title.getSaleId() + "] ");
                MiniPlayerTitle.notification.info(sender, idx.append(title.getTitle()));
            }
            return;
        }
        Player player = (Player) sender;
        ListView view = ListView.create(5, "/mplt shop");
        view.title("称号商店");
        view.subtitle("当前余额: " + XPlayer.getCoin(player) + "称号币");
        for (Map.Entry<Integer, SaleTitle> entry : titles.entrySet()) {
            Integer title_sale_id = entry.getKey();
            TextComponent idx = Component.text("[" + title_sale_id + "] ");
            SaleTitle title = entry.getValue();
            Line line = Line.create();
            Component button = Button.create("购买").setExecuteCommand("/mplt buy " + title_sale_id).build();
            line.append(idx)
                    .append(title.getTitle())
                    .append("价格:" + title.getPrice() + " 有效期:" + (title.getDays() == -1 ? "永久" : title.getDays() + "天"))
                    .append("售卖截止:" + title.getSaleEndAt())
                    .append("剩余:" + ((title.getAmount() == -1) ? "无限" : title.getAmount()))
                    .append(button);
            view.add(line);
        }
        view.showOn(player, page);
    }

    public static void deleteTitle(Integer id) {
        String sql = "";
        sql += "DELETE FROM mplt_title_shop WHERE id = " + id + ";";
        MiniPlayerTitle.database.query(sql);
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
        sql += "FROM mplt_title_shop;";
        Map<Integer, SaleTitle> titles = new HashMap<>();
        try (ResultSet rs = MiniPlayerTitle.database.query(sql)) {
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
            MiniPlayerTitle.logger.err("XPlayer getTitles failed: " + e.getMessage());
        }
        return titles;
    }
}
