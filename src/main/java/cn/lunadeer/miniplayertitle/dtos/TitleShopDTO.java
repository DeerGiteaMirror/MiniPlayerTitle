package cn.lunadeer.miniplayertitle.dtos;

import cn.lunadeer.miniplayertitle.MiniPlayerTitle;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TitleShopDTO {
    private Integer id;
    private TitleDTO title;
    private Integer price;
    private Integer days;
    private Integer amount;
    private LocalDateTime sale_end_at;

    public Integer getId() {
        return id;
    }

    public TitleDTO getTitle() {
        return title;
    }

    public Integer getPrice() {
        return price;
    }

    public boolean setPrice(int price) {
        String sql = "";
        sql += "UPDATE mplt_title_shop SET price = " + price + " WHERE id = " + id + ";";
        try (ResultSet rs = MiniPlayerTitle.database.query(sql)) {
            return true;
        } catch (Exception e) {
            MiniPlayerTitle.database.handleDatabaseError("设置称号商店价格失败", e, sql);
        }
        return false;
    }

    public Integer getDays() {
        return days;
    }

    public boolean setDays(int days) {
        String sql = "";
        sql += "UPDATE mplt_title_shop SET days = " + days + " WHERE id = " + id + ";";
        try (ResultSet rs = MiniPlayerTitle.database.query(sql)) {
            return true;
        } catch (Exception e) {
            MiniPlayerTitle.database.handleDatabaseError("设置称号商店天数失败", e, sql);
        }
        return false;
    }

    public Integer getAmount() {
        return amount;
    }

    public boolean setAmount(int amount) {
        String sql = "";
        sql += "UPDATE mplt_title_shop SET amount = " + amount + " WHERE id = " + id + ";";
        try (ResultSet rs = MiniPlayerTitle.database.query(sql)) {
            return true;
        } catch (Exception e) {
            MiniPlayerTitle.database.handleDatabaseError("设置称号商店数量失败", e, sql);
        }
        return false;
    }

    public LocalDateTime getSaleEndAt() {
        return sale_end_at;
    }

    public boolean setSaleEndAt(LocalDateTime dateTime) {
        String sql = "";
        sql += "UPDATE mplt_title_shop SET sale_end_at_y = " + dateTime.getYear() + ", sale_end_at_m = " + dateTime.getMonthValue() + ", sale_end_at_d = " + dateTime.getDayOfMonth() + " WHERE id = " + id + ";";
        try (ResultSet rs = MiniPlayerTitle.database.query(sql)) {
            return true;
        } catch (Exception e) {
            MiniPlayerTitle.database.handleDatabaseError("设置称号商店销售结束时间失败", e, sql);
        }
        return false;
    }

    public boolean setSaleEndAt(int y, int m, int d) {
        String sql = "";
        sql += "UPDATE mplt_title_shop SET sale_end_at_y = " + y + ", sale_end_at_m = " + m + ", sale_end_at_d = " + d + " WHERE id = " + id + ";";
        try (ResultSet rs = MiniPlayerTitle.database.query(sql)) {
            return true;
        } catch (Exception e) {
            MiniPlayerTitle.database.handleDatabaseError("设置称号商店销售结束时间失败", e, sql);
        }
        return false;
    }

    public static TitleShopDTO get(Integer id) {
        String sql = "";
        sql += "SELECT id, title_id, price, days, amount, sale_end_at FROM mplt_title_shop WHERE id = " + id + ";";
        try (ResultSet rs = MiniPlayerTitle.database.query(sql)) {
            if (rs.next()) {
                return getTitleShop(rs);
            }
        } catch (Exception e) {
            MiniPlayerTitle.database.handleDatabaseError("获取称号商店信息失败", e, sql);
        }
        return null;
    }

    public static List<TitleShopDTO> getAll() {
        String sql = "";
        sql += "SELECT id, title_id, price, days, amount, sale_end_at FROM mplt_title_shop;";
        List<TitleShopDTO> titleShops = new ArrayList<>();
        try (ResultSet rs = MiniPlayerTitle.database.query(sql)) {
            while (rs != null && rs.next()) {
                TitleShopDTO titleShop = getTitleShop(rs);
                titleShops.add(titleShop);
            }
        } catch (Exception e) {
            MiniPlayerTitle.database.handleDatabaseError("获取称号商店列表失败", e, sql);
        }
        return titleShops;
    }

    private static TitleShopDTO getTitleShop(ResultSet rs) throws Exception {
        TitleShopDTO titleShop = new TitleShopDTO();
        titleShop.id = rs.getInt("id");
        titleShop.title = TitleDTO.get(rs.getInt("title_id"));
        titleShop.price = rs.getInt("price");
        titleShop.days = rs.getInt("days");
        titleShop.amount = rs.getInt("amount");
        int y = rs.getInt("sale_end_at_y");
        int m = rs.getInt("sale_end_at_m");
        int d = rs.getInt("sale_end_at_d");
        if (y == -1 && m == -1 && d == -1) {
            titleShop.sale_end_at = null;
        } else {
            titleShop.sale_end_at = LocalDateTime.of(y, m, d, 0, 0, 0);
        }
        return titleShop;
    }

    public static TitleShopDTO create(TitleDTO title) {
        String sql = "";
        sql += "INSERT INTO mplt_title_shop (title_id, price, days, amount, sale_end_at_y, sale_end_at_m, sale_end_at_d) " +
                "VALUES (" + title.getId() + ", 0, -1, 0, -1, -1, -1) " +
                "RETURNING " +
                "id, title_id, price, days, amount, sale_end_at_y, sale_end_at_m, sale_end_at_d;";
        try (ResultSet rs = MiniPlayerTitle.database.query(sql)) {
            if (rs.next()) {
                return getTitleShop(rs);
            }
        } catch (Exception e) {
            MiniPlayerTitle.database.handleDatabaseError("创建称号商店失败", e, sql);
        }
        return null;
    }

    public boolean delete() {
        String sql = "";
        sql += "DELETE FROM mplt_title_shop WHERE id = " + id + ";";
        try (ResultSet rs = MiniPlayerTitle.database.query(sql)) {
            return true;
        } catch (Exception e) {
            MiniPlayerTitle.database.handleDatabaseError("删除称号商店失败", e, sql);
        }
        return false;
    }

    public boolean isExpired() {
        if (sale_end_at == null) return false;
        return LocalDateTime.now().isAfter(sale_end_at);
    }

}