package cn.lunadeer.miniplayertitle.dtos;

import cn.lunadeer.minecraftpluginutils.databse.DatabaseManager;
import cn.lunadeer.minecraftpluginutils.databse.Field;
import cn.lunadeer.minecraftpluginutils.databse.FieldType;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static cn.lunadeer.minecraftpluginutils.databse.DatabaseManager.handleDatabaseError;

public class TitleShopDTO {
    private TitleDTO title;
    private final Field id = new Field("id", FieldType.INT);
    private final Field price = new Field("price_d", FieldType.DOUBLE);
    private final Field days = new Field("days", FieldType.INT);
    private final Field amount = new Field("amount", FieldType.INT);

    private LocalDateTime sale_end_at;

    public Integer getId() {
        return (Integer) id.value;
    }

    public TitleDTO getTitle() {
        return title;
    }

    public Double getPrice() {
        return (Double) price.value;
    }

    public boolean setPrice(Double price) {
        String sql = "";
        sql += "UPDATE mplt_title_shop SET price_d = ? WHERE id = ?;";
        try (ResultSet rs = DatabaseManager.instance.query(sql, price, getId())) {
            this.price.value = price;
            return true;
        } catch (Exception e) {
            handleDatabaseError("设置称号商店价格失败", e, sql);
        }
        return false;
    }

    public Integer getDays() {
        return (Integer) days.value;
    }

    public boolean setDays(int days) {
        String sql = "";
        sql += "UPDATE mplt_title_shop SET days = ? WHERE id = ?;";
        try (ResultSet rs = DatabaseManager.instance.query(sql, days, getId())) {
            this.days.value = days;
            return true;
        } catch (Exception e) {
            handleDatabaseError("设置称号商店天数失败", e, sql);
        }
        return false;
    }

    public Integer getAmount() {
        return (Integer) amount.value;
    }

    public boolean setAmount(int amount) {
        String sql = "";
        sql += "UPDATE mplt_title_shop SET amount = ? WHERE id = ?;";
        try (ResultSet rs = DatabaseManager.instance.query(sql, amount, getId())) {
            this.amount.value = amount;
            return true;
        } catch (Exception e) {
            handleDatabaseError("设置称号商店数量失败", e, sql);
        }
        return false;
    }

    public LocalDateTime getSaleEndAt() {
        return sale_end_at;
    }

    public boolean setSaleEndAt(LocalDateTime dateTime) {
        return setSaleEndAt(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth());
    }

    public boolean setSaleEndAt(int y, int m, int d) {
        String sql = "";
        sql += "UPDATE mplt_title_shop SET sale_end_at_y = ?, sale_end_at_m = ?, sale_end_at_d = ? WHERE id = ?;";
        try (ResultSet rs = DatabaseManager.instance.query(sql, y, m, d, getId())) {
            return true;
        } catch (Exception e) {
            handleDatabaseError("设置称号商店销售结束时间失败", e, sql);
        }
        return false;
    }

    public static TitleShopDTO get(Integer id) {
        String sql = "";
        sql += "SELECT id, title_id, price_d, days, amount, sale_end_at_y, sale_end_at_m, sale_end_at_d " +
                "FROM mplt_title_shop WHERE id = ?;";
        try (ResultSet rs = DatabaseManager.instance.query(sql, id)) {
            if (rs.next()) {
                return getTitleShop(rs);
            }
        } catch (Exception e) {
            handleDatabaseError("获取称号商店信息失败", e, sql);
        }
        return null;
    }

    public static List<TitleShopDTO> getAll() {
        String sql = "";
        sql += "SELECT id, title_id, price_d, days, amount, sale_end_at_y, sale_end_at_m, sale_end_at_d " +
                "FROM mplt_title_shop;";
        List<TitleShopDTO> titleShops = new ArrayList<>();
        try (ResultSet rs = DatabaseManager.instance.query(sql)) {
            while (rs != null && rs.next()) {
                TitleShopDTO titleShop = getTitleShop(rs);
                titleShops.add(titleShop);
            }
        } catch (Exception e) {
            handleDatabaseError("获取称号商店列表失败", e, sql);
        }
        return titleShops;
    }

    private static TitleShopDTO getTitleShop(ResultSet rs) throws Exception {
        TitleShopDTO titleShop = new TitleShopDTO();
        titleShop.id.value = rs.getInt("id");
        titleShop.title = TitleDTO.get(rs.getInt("title_id"));
        titleShop.price.value = rs.getDouble("price_d");
        titleShop.days.value = rs.getInt("days");
        titleShop.amount.value = rs.getInt("amount");
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
        sql += "INSERT INTO mplt_title_shop (title_id, price_d, days, amount, sale_end_at_y, sale_end_at_m, sale_end_at_d) " +
                "VALUES (?, 0, -1, 0, -1, -1, -1) " +
                "RETURNING " +
                "id, title_id, price_d, days, amount, sale_end_at_y, sale_end_at_m, sale_end_at_d;";
        try (ResultSet rs = DatabaseManager.instance.query(sql, title.getId())) {
            if (rs.next()) {
                return getTitleShop(rs);
            }
        } catch (Exception e) {
            handleDatabaseError("创建称号商店失败", e, sql);
        }
        return null;
    }

    public boolean delete() {
        String sql = "";
        sql += "DELETE FROM mplt_title_shop WHERE id = ?;";
        try (ResultSet rs = DatabaseManager.instance.query(sql, getId())) {
            return true;
        } catch (Exception e) {
            handleDatabaseError("删除称号商店失败", e, sql);
        }
        return false;
    }

    public boolean isExpired() {
        if (sale_end_at == null) return false;
        return LocalDateTime.now().isAfter(sale_end_at);
    }

}
