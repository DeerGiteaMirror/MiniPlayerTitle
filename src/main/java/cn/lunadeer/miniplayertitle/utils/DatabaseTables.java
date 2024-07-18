package cn.lunadeer.miniplayertitle.utils;

import cn.lunadeer.minecraftpluginutils.databse.DatabaseManager;
import cn.lunadeer.minecraftpluginutils.databse.Field;
import cn.lunadeer.minecraftpluginutils.databse.FieldType;
import cn.lunadeer.minecraftpluginutils.databse.TableColumn;
import cn.lunadeer.minecraftpluginutils.databse.syntax.AddColumn;
import cn.lunadeer.minecraftpluginutils.databse.syntax.CreateTable;
import cn.lunadeer.minecraftpluginutils.databse.syntax.InsertRow;
import cn.lunadeer.minecraftpluginutils.databse.syntax.RemoveColumn;

import static cn.lunadeer.minecraftpluginutils.databse.Common.IsFieldExist;

public class DatabaseTables {

    public static void migrate() {

        // title table
        TableColumn mplt_title_id = new TableColumn("id", FieldType.INT, true, true, true, true, 0);
        TableColumn mplt_title_title = new TableColumn("title", FieldType.STRING, false, false, true, false, "'unknown'");
        TableColumn mplt_title_description = new TableColumn("description", FieldType.STRING, false, false, true, false, "'unknown'");
        TableColumn mplt_title_enabled = new TableColumn("enabled", FieldType.BOOLEAN, false, false, true, false, "true");
        TableColumn mplt_title_created_at = new TableColumn("created_at", FieldType.DATETIME, false, false, true, false, "CURRENT_TIMESTAMP");
        TableColumn mplt_title_updated_at = new TableColumn("updated_at", FieldType.DATETIME, false, false, true, false, "CURRENT_TIMESTAMP");
        CreateTable mplt_title = new CreateTable().ifNotExists();
        mplt_title.table("mplt_title")
                .field(mplt_title_id)
                .field(mplt_title_title)
                .field(mplt_title_description)
                .field(mplt_title_enabled)
                .field(mplt_title_created_at)
                .field(mplt_title_updated_at);
        mplt_title.execute();


        // title shop table
        TableColumn mplt_title_shop_id = new TableColumn("id", FieldType.INT, true, true, true, true, 0);
        TableColumn mplt_title_shop_title_id = new TableColumn("title_id", FieldType.INT, false, false, true, false, 0);
        TableColumn mplt_title_shop_price = new TableColumn("price", FieldType.INT, false, false, true, false, 0);
        TableColumn mplt_title_shop_days = new TableColumn("days", FieldType.INT, false, false, true, false, 0);
        TableColumn mplt_title_shop_amount = new TableColumn("amount", FieldType.INT, false, false, true, false, -1);
        TableColumn mplt_title_shop_sale_end_at = new TableColumn("sale_end_at", FieldType.LONG, false, false, true, false, -1);
        TableColumn mplt_title_shop_created_at = new TableColumn("created_at", FieldType.DATETIME, false, false, true, false, "CURRENT_TIMESTAMP");
        TableColumn mplt_title_shop_updated_at = new TableColumn("updated_at", FieldType.DATETIME, false, false, true, false, "CURRENT_TIMESTAMP");
        CreateTable.ForeignKey mplt_title_shop_title_id_fk = new CreateTable.ForeignKey(mplt_title_shop_title_id, "mplt_title", mplt_title_id, true);
        CreateTable mplt_title_shop = new CreateTable().ifNotExists();
        mplt_title_shop.table("mplt_title_shop")
                .field(mplt_title_shop_id)
                .field(mplt_title_shop_title_id)
                .field(mplt_title_shop_price)
                .field(mplt_title_shop_days)
                .field(mplt_title_shop_amount)
                .field(mplt_title_shop_sale_end_at)
                .field(mplt_title_shop_created_at)
                .field(mplt_title_shop_updated_at)
                .foreignKey(mplt_title_shop_title_id_fk);
        mplt_title_shop.execute();

        // player title info table
        TableColumn mplt_player_info_uuid = new TableColumn("uuid", FieldType.UUID, true, false, true, false, "'00000000-0000-0000-0000-000000000000'");
        TableColumn mplt_player_info_coin = new TableColumn("coin", FieldType.INT, false, false, true, false, 0);
        TableColumn mplt_player_info_using_title_id = new TableColumn("using_title_id", FieldType.INT, false, false, true, false, -1);
        TableColumn mplt_player_info_created_at = new TableColumn("created_at", FieldType.DATETIME, false, false, true, false, "CURRENT_TIMESTAMP");
        TableColumn mplt_player_info_updated_at = new TableColumn("updated_at", FieldType.DATETIME, false, false, true, false, "CURRENT_TIMESTAMP");
        CreateTable.ForeignKey mplt_player_info_using_title_id_fk = new CreateTable.ForeignKey(mplt_player_info_using_title_id, "mplt_title", mplt_title_id, true);
        CreateTable mplt_player_info = new CreateTable().ifNotExists();
        mplt_player_info.table("mplt_player_info")
                .field(mplt_player_info_uuid)
                .field(mplt_player_info_coin)
                .field(mplt_player_info_using_title_id)
                .field(mplt_player_info_created_at)
                .field(mplt_player_info_updated_at)
                .foreignKey(mplt_player_info_using_title_id_fk);
        mplt_player_info.execute();


        // player title table
        TableColumn mplt_player_title_id = new TableColumn("id", FieldType.INT, true, true, true, true, 0);
        TableColumn mplt_player_title_player_uuid = new TableColumn("player_uuid", FieldType.UUID, false, false, true, false, "'00000000-0000-0000-0000-000000000000'");
        TableColumn mplt_player_title_title_id = new TableColumn("title_id", FieldType.INT, false, false, true, false, 0);
        TableColumn mplt_player_title_expire_at = new TableColumn("expire_at", FieldType.LONG, false, false, true, false, -1);
        TableColumn mplt_player_title_created_at = new TableColumn("created_at", FieldType.DATETIME, false, false, true, false, "CURRENT_TIMESTAMP");
        TableColumn mplt_player_title_updated_at = new TableColumn("updated_at", FieldType.DATETIME, false, false, true, false, "CURRENT_TIMESTAMP");
        CreateTable.ForeignKey mplt_player_title_title_id_fk = new CreateTable.ForeignKey(mplt_player_title_title_id, "mplt_title", mplt_title_id, true);
        CreateTable.ForeignKey mplt_player_title_player_uuid_fk = new CreateTable.ForeignKey(mplt_player_title_player_uuid, "mplt_player_info", mplt_player_info_uuid, true);
        CreateTable mplt_player_title = new CreateTable().ifNotExists();
        mplt_player_title.table("mplt_player_title")
                .field(mplt_player_title_id)
                .field(mplt_player_title_player_uuid)
                .field(mplt_player_title_title_id)
                .field(mplt_player_title_expire_at)
                .field(mplt_player_title_created_at)
                .field(mplt_player_title_updated_at)
                .foreignKey(mplt_player_title_title_id_fk)
                .foreignKey(mplt_player_title_player_uuid_fk);
        mplt_player_title.execute();

        new RemoveColumn("expire_at").IfExists().table("mplt_title").execute();
        new RemoveColumn("sale_end_at").IfExists().table("mplt_title_shop").execute();
        new RemoveColumn("expire_at").IfExists().table("mplt_player_title").execute();
        new RemoveColumn("created_at").IfExists().table("mplt_title").execute();
        new RemoveColumn("updated_at").IfExists().table("mplt_title").execute();
        new RemoveColumn("created_at").IfExists().table("mplt_player_info").execute();
        new RemoveColumn("updated_at").IfExists().table("mplt_player_info").execute();
        new RemoveColumn("created_at").IfExists().table("mplt_player_title").execute();
        new RemoveColumn("updated_at").IfExists().table("mplt_player_title").execute();

        TableColumn mplt_title_shop_sale_end_at_y = new TableColumn("sale_end_at_y", FieldType.INT, false, false, true, false, -1);
        TableColumn mplt_title_shop_sale_end_at_m = new TableColumn("sale_end_at_m", FieldType.INT, false, false, true, false, -1);
        TableColumn mplt_title_shop_sale_end_at_d = new TableColumn("sale_end_at_d", FieldType.INT, false, false, true, false, -1);
        new AddColumn(mplt_title_shop_sale_end_at_y).table("mplt_title_shop").ifNotExists().execute();
        new AddColumn(mplt_title_shop_sale_end_at_m).table("mplt_title_shop").ifNotExists().execute();
        new AddColumn(mplt_title_shop_sale_end_at_d).table("mplt_title_shop").ifNotExists().execute();
        // convert sale_end_at(YYYYMMDD) to sale_end_at_y, sale_end_at_m, sale_end_at_d if sale_end_at column exists
        if (IsFieldExist("mplt_title_shop", "sale_end_at")) {
            String sql = "UPDATE mplt_title_shop SET " +
                    "sale_end_at_y = (sale_end_at / 10000), " +
                    "sale_end_at_m = (sale_end_at % 10000 / 100), " +
                    "sale_end_at_d = (sale_end_at % 100) " +
                    "WHERE sale_end_at != -1;";
            DatabaseManager.instance.query(sql);
            new RemoveColumn("sale_end_at").IfExists().table("mplt_title_shop").execute();
        }


        TableColumn mplt_player_title_expire_at_y = new TableColumn("expire_at_y", FieldType.INT, false, false, true, false, -1);
        TableColumn mplt_player_title_expire_at_m = new TableColumn("expire_at_m", FieldType.INT, false, false, true, false, -1);
        TableColumn mplt_player_title_expire_at_d = new TableColumn("expire_at_d", FieldType.INT, false, false, true, false, -1);
        new AddColumn(mplt_player_title_expire_at_y).table("mplt_player_title").ifNotExists().execute();
        new AddColumn(mplt_player_title_expire_at_m).table("mplt_player_title").ifNotExists().execute();
        new AddColumn(mplt_player_title_expire_at_d).table("mplt_player_title").ifNotExists().execute();

        if (IsFieldExist("mplt_player_title", "expire_at")) {
            String sql = "UPDATE mplt_player_title SET " +
                    "expire_at_y = (expire_at / 10000), " +
                    "expire_at_m = (expire_at % 10000 / 100), " +
                    "expire_at_d = (expire_at % 100) " +
                    "WHERE expire_at != -1;";
            DatabaseManager.instance.query(sql);
            new RemoveColumn("expire_at").IfExists().table("mplt_player_title").execute();
        }

        Field mplt_title_id_field = new Field("id", -1);
        Field mplt_title_title_field = new Field("title", "default");
        Field mplt_title_description_field = new Field("description", "default");
        InsertRow mplt_title_default = new InsertRow().table("mplt_title");
        mplt_title_default.field(mplt_title_id_field)
                .field(mplt_title_title_field)
                .field(mplt_title_description_field).onConflictDoNothing(mplt_title_id_field);
        mplt_title_default.execute();


        TableColumn mplt_player_info_last_use_name = new TableColumn("last_use_name", FieldType.STRING, false, false, true, false, "'null'");
        new AddColumn(mplt_player_info_last_use_name).table("mplt_player_info").ifNotExists().execute();

        // 3.0.6
        TableColumn mplt_player_info_coin_d = new TableColumn("coin_d", FieldType.DOUBLE, false, false, true, false, 0);
        TableColumn mplt_title_shop_price_d = new TableColumn("price_d", FieldType.DOUBLE, false, false, true, false, 0);
        new AddColumn(mplt_player_info_coin_d).table("mplt_player_info").ifNotExists().execute();
        new AddColumn(mplt_title_shop_price_d).table("mplt_title_shop").ifNotExists().execute();
        if (IsFieldExist("mplt_player_info", "coin")) {
            String sql = "UPDATE mplt_player_info SET coin_d = coin;";
            DatabaseManager.instance.query(sql);
            new RemoveColumn("coin").IfExists().table("mplt_player_info").execute();
        }
        if (IsFieldExist("mplt_title_shop", "price")) {
            String sql = "UPDATE mplt_title_shop SET price_d = price;";
            DatabaseManager.instance.query(sql);
            new RemoveColumn("price").IfExists().table("mplt_title_shop").execute();
        }
    }
}
