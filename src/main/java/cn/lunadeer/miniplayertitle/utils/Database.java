package cn.lunadeer.miniplayertitle.utils;

import cn.lunadeer.miniplayertitle.MiniPlayerTitle;

import java.sql.*;

public class Database {

    public static Connection createConnection(){
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(MiniPlayerTitle.config.getDBConnectionUrl(), MiniPlayerTitle.config.getDbUser(), MiniPlayerTitle.config.getDbPass());
        } catch (ClassNotFoundException | SQLException e) {
            XLogger.err("Database connection failed: " + e.getMessage());
            return null;
        }
    }

    public static ResultSet query(String sql) {
        Connection conn = MiniPlayerTitle.dbConnection;
        if (conn == null) {
            return null;
        }
        try {
            Statement stmt = conn.createStatement();
            // if query with no result return null
            if (stmt.execute(sql)) {
                return stmt.getResultSet();
            } else {
                return null;
            }
        } catch (SQLException e) {
            XLogger.err("Database query failed: " + e.getMessage());
            XLogger.err("SQL: " + sql);
            return null;
        }
    }

    public static void migrate() {
        String sql = "";

        // title table
        sql += "CREATE TABLE IF NOT EXISTS mplt_title (" +
                "  id                 SERIAL PRIMARY KEY," +
                "  title              TEXT NOT NULL UNIQUE," +
                "  description        TEXT NOT NULL," +
                "  enabled            BOOLEAN NOT NULL DEFAULT TRUE," +
                "  created_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "  updated_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP" +
                ");";

        // title shop table
        sql += "CREATE TABLE IF NOT EXISTS mplt_title_shop (" +
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

        // player title info table
        sql += "CREATE TABLE IF NOT EXISTS mplt_player_info (" +
                "  uuid              UUID PRIMARY KEY," +
                "  coin              INTEGER NOT NULL DEFAULT 0," +
                "  using_title_id    INTEGER NOT NULL DEFAULT -1," +
                "  created_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "  updated_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "  FOREIGN KEY (using_title_id) REFERENCES mplt_title(id) ON DELETE CASCADE" +
                ");";

        // player title table
        sql += "CREATE TABLE IF NOT EXISTS mplt_player_title (" +
                "  id                SERIAL PRIMARY KEY," +
                "  player_uuid       UUID NOT NULL," +
                "  title_id          INTEGER NOT NULL," +
                "  expire_at         BIGINT NOT NULL DEFAULT -1," +
                "  created_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "  updated_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "  FOREIGN KEY (title_id) REFERENCES mplt_title(id) ON DELETE CASCADE," +
                "  FOREIGN KEY (player_uuid) REFERENCES mplt_player_info(uuid) ON DELETE CASCADE" +
                ");";

        sql += "INSERT INTO mplt_title (" +
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


        query(sql);
    }
}
