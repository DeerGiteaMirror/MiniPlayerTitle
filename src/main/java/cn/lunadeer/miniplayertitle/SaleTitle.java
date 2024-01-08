package cn.lunadeer.miniplayertitle;


import cn.lunadeer.miniplayertitle.utils.Database;
import cn.lunadeer.miniplayertitle.utils.XLogger;

import java.sql.ResultSet;

public class SaleTitle extends Title {
    private Integer _sale_id;
    private Integer _price;
    private Integer _days;
    private Integer _amount;
    private Long _sale_end_at;

    public SaleTitle(Integer id, Integer title_id, Integer price, Integer days, Integer amount, Long sale_end_at) {
        super(title_id);
        this._sale_id = id;
        this._id = title_id;
        this._price = price;
        this._days = days;
        this._amount = amount;
        this._sale_end_at = sale_end_at;
    }

    public static SaleTitle create(Integer title_id) {
        String sql = "";
        sql += "INSERT INTO mplt_title_shop (title_id, price, days, amount, sale_end_at) ";
        sql += "VALUES (" + title_id + ", 0, 0, 0, -1) ";
        sql += "RETURNING id;";
        try (ResultSet rs = Database.query(sql)) {
            if (rs != null && rs.next()) {
                Integer id = rs.getInt("id");
                return new SaleTitle(id, title_id, 0, 0, -1, System.currentTimeMillis());
            }
        } catch (Exception e) {
            XLogger.err("SaleTitle create failed: " + e.getMessage());
        }
        return null;
    }

    public Integer getPrice() {
        return this._price;
    }

    private void setPrice(Integer price) {
        this._price = price;
        this.save();
    }

    public static void setPrice(Integer id, Integer price) {
        SaleTitle title = Shop.getSaleTitles().get(id);
        title.setPrice(price);
    }

    public Integer getDays() {
        return this._days;
    }

    private void setDays(Integer days) {
        this._days = days;
        this.save();
    }

    public static void setDays(Integer id, Integer days) {
        SaleTitle title = Shop.getSaleTitles().get(id);
        title.setDays(days);
    }

    public Integer getAmount() {
        return this._amount;
    }

    private void setAmount(Integer amount) {
        this._amount = amount;
        this.save();
    }

    public static void setAmount(Integer id, Integer amount) {
        SaleTitle title = Shop.getSaleTitles().get(id);
        title.setAmount(amount);
    }

    public String getSaleEndAt() {
        if (this._sale_end_at == -1L) {
            return "常驻";
        } else if (this._sale_end_at < System.currentTimeMillis()) {
            return "已停售";
        } else {
            return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(this._sale_end_at));
        }
    }

    private void setSaleEndAt(Long sale_end_at) {
        this._sale_end_at = sale_end_at;
        this.save();
    }

    public static void setSaleEndAt(Integer id, Long sale_end_at) {
        SaleTitle title = Shop.getSaleTitles().get(id);
        title.setSaleEndAt(sale_end_at);
    }

    public Boolean isSaleExpired() {
        if (this._sale_end_at == -1L) {
            return false;
        } else {
            return this._sale_end_at < System.currentTimeMillis();
        }
    }

    public Integer getSaleId(){
        return this._sale_id;
    }

    private void save() {
        String sql = "";
        sql += "UPDATE mplt_title_shop ";
        sql += "SET title_id = " + this._id + ", ";
        sql += "price = " + this._price + ", ";
        sql += "days = " + this._days + ", ";
        sql += "amount = " + this._amount + ", ";
        sql += "sale_end_at = " + this._sale_end_at + ", ";
        sql += "updated_at = CURRENT_TIMESTAMP ";
        sql += "WHERE id = " + this._sale_id + ";";

        Database.query(sql);
    }
}
