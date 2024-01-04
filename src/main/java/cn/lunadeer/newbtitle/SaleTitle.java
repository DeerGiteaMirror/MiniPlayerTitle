package cn.lunadeer.newbtitle;


import cn.lunadeer.newbtitle.utils.Database;
import cn.lunadeer.newbtitle.utils.XLogger;

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
        sql += "INSERT INTO nt_title_shop (title_id, price, days, amount, sale_end_at) ";
        sql += "VALUES (" + title_id + ", 0, 0, -1, CURRENT_TIMESTAMP) ";
        sql += "RETURNING id;";
        ResultSet rs = Database.query(sql);
        try {
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

    public void setPrice(Integer price) {
        this._price = price;
        this.save();
    }

    public Integer getDays() {
        return this._days;
    }

    public void setDays(Integer days) {
        this._days = days;
        this.save();
    }

    public Integer getAmount() {
        return this._amount;
    }

    public void setAmount(Integer amount) {
        this._amount = amount;
        this.save();
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

    public void setSaleEndAt(Long sale_end_at) {
        this._sale_end_at = sale_end_at;
        this.save();
    }

    public Boolean isSaleExpired() {
        if (this._sale_end_at == -1L) {
            return false;
        } else {
            return this._sale_end_at < System.currentTimeMillis();
        }
    }

    private void save() {
        String sql = "";
        sql += "UPDATE nt_title_shop ";
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
