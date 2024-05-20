package cn.lunadeer.miniplayertitle.utils;

import cn.lunadeer.miniplayertitle.MiniPlayerTitle;

public class DatabaseTables {

    public static void migrate() {
        String sql = "";

        // title table
        sql = "CREATE TABLE IF NOT EXISTS mplt_title (" +
                "  id                 SERIAL PRIMARY KEY," +
                "  title              TEXT NOT NULL UNIQUE," +
                "  description        TEXT NOT NULL," +
                "  enabled            BOOLEAN NOT NULL DEFAULT TRUE," +
                "  created_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "  updated_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP" +
                ");";
        MiniPlayerTitle.database.query(sql);

        // title shop table
        sql = "CREATE TABLE IF NOT EXISTS mplt_title_shop (" +
                "  id                 SERIAL PRIMARY KEY," +
                "  title_id           INTEGER NOT NULL," +
                "  price              INTEGER NOT NULL DEFAULT 0," +
                "  days               INTEGER NOT NULL DEFAULT 0," +
                "  amount             INTEGER NOT NULL DEFAULT -1," +
                "  sale_end_at        BIGINT NOT NULL DEFAULT -1," +
                "  created_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "  updated_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "  FOREIGN KEY (title_id) REFERENCES mplt_title(id) ON DELETE CASCADE" +
                ");";
        MiniPlayerTitle.database.query(sql);

        // player title info table
        sql = "CREATE TABLE IF NOT EXISTS mplt_player_info (" +
                "  uuid              UUID PRIMARY KEY," +
                "  coin              INTEGER NOT NULL DEFAULT 0," +
                "  using_title_id    INTEGER NOT NULL DEFAULT -1," +
                "  created_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "  updated_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "  FOREIGN KEY (using_title_id) REFERENCES mplt_title(id) ON DELETE CASCADE" +
                ");";
        MiniPlayerTitle.database.query(sql);

        // player title table
        sql = "CREATE TABLE IF NOT EXISTS mplt_player_title (" +
                "  id                SERIAL PRIMARY KEY," +
                "  player_uuid       UUID NOT NULL," +
                "  title_id          INTEGER NOT NULL," +
                "  expire_at         BIGINT NOT NULL DEFAULT -1," +
                "  created_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "  updated_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "  FOREIGN KEY (title_id) REFERENCES mplt_title(id) ON DELETE CASCADE," +
                "  FOREIGN KEY (player_uuid) REFERENCES mplt_player_info(uuid) ON DELETE CASCADE" +
                ");";
        MiniPlayerTitle.database.query(sql);

        sql = "INSERT INTO mplt_title (" +
                "id,         " +
                "title,      " +
                "description," +
                "enabled,    " +
                "created_at, " +
                "updated_at  " +
                ") VALUES (" +
                "-1,          " +
                "'default',   " +
                "'default',   " +
                "TRUE,        " +
                "CURRENT_TIMESTAMP, " +
                "CURRENT_TIMESTAMP  " +
                ") ON CONFLICT (id) DO NOTHING;";
        MiniPlayerTitle.database.query(sql);

        MiniPlayerTitle.database.deleteColumnIfExists("mplt_title", "created_at");
        MiniPlayerTitle.database.deleteColumnIfExists("mplt_title", "updated_at");
        MiniPlayerTitle.database.deleteColumnIfExists("mplt_title", "enabled");

        MiniPlayerTitle.database.deleteColumnIfExists("mplt_title_shop", "created_at");
        MiniPlayerTitle.database.deleteColumnIfExists("mplt_title_shop", "updated_at");

        MiniPlayerTitle.database.deleteColumnIfExists("mplt_player_info", "created_at");
        MiniPlayerTitle.database.deleteColumnIfExists("mplt_player_info", "updated_at");

        MiniPlayerTitle.database.deleteColumnIfExists("mplt_player_title", "created_at");
        MiniPlayerTitle.database.deleteColumnIfExists("mplt_player_title", "updated_at");

        MiniPlayerTitle.database.addColumnIfNotExists("mplt_title_shop", "sale_end_at_y", "INTEGER NOT NULL DEFAULT -1");
        MiniPlayerTitle.database.addColumnIfNotExists("mplt_title_shop", "sale_end_at_m", "INTEGER NOT NULL DEFAULT -1");
        MiniPlayerTitle.database.addColumnIfNotExists("mplt_title_shop", "sale_end_at_d", "INTEGER NOT NULL DEFAULT -1");
        // convert sale_end_at(YYYYMMDD) to sale_end_at_y, sale_end_at_m, sale_end_at_d
        sql = "UPDATE mplt_title_shop SET " +
                "sale_end_at_y = (sale_end_at / 10000), " +
                "sale_end_at_m = (sale_end_at % 10000 / 100), " +
                "sale_end_at_d = (sale_end_at % 100) " +
                "WHERE sale_end_at != -1;";
        MiniPlayerTitle.database.query(sql);
        MiniPlayerTitle.database.deleteColumnIfExists("mplt_title_shop", "sale_end_at");

        MiniPlayerTitle.database.addColumnIfNotExists("mplt_player_title", "expire_at_y", "INTEGER NOT NULL DEFAULT -1");
        MiniPlayerTitle.database.addColumnIfNotExists("mplt_player_title", "expire_at_m", "INTEGER NOT NULL DEFAULT -1");
        MiniPlayerTitle.database.addColumnIfNotExists("mplt_player_title", "expire_at_d", "INTEGER NOT NULL DEFAULT -1");
        sql = "UPDATE mplt_player_title SET " +
                "expire_at_y = (expire_at / 10000), " +
                "expire_at_m = (expire_at % 10000 / 100), " +
                "expire_at_d = (expire_at % 100) " +
                "WHERE expire_at != -1;";
        MiniPlayerTitle.database.query(sql);
        MiniPlayerTitle.database.deleteColumnIfExists("mplt_player_title", "expire_at");
    }
}
