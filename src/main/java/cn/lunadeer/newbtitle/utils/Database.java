package cn.lunadeer.newbtitle.utils;

import cn.lunadeer.newbtitle.NewbTitle;

import java.sql.*;

public class Database {

    private static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(NewbTitle.config.getDBConnectionUrl(), NewbTitle.config.getDbUser(), NewbTitle.config.getDbPass());
        } catch (ClassNotFoundException | SQLException e) {
            XLogger.err("Database connection failed: " + e.getMessage());
            return null;
        }
    }

    public static ResultSet query(String sql) {
        Connection conn = getConnection();
        if (conn == null) {
            return null;
        }
        try {
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            XLogger.err("Database query failed: " + e.getMessage());
            XLogger.err("SQL: " + sql);
            return null;
        }
    }

    public static void migrate() {
        String sql = "";

        // title table
        sql += "CREATE TABLE IF NOT EXISTS nt_title (" +
                "  id                 SERIAL PRIMARY KEY," +
                "  title              TEXT NOT NULL," +
                "  description        TEXT NOT NULL," +
                "  enabled            BOOLEAN NOT NULL DEFAULT TRUE," +
                "  created_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "  updated_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP" +
                ");";

        // title shop table
        sql += "CREATE TABLE IF NOT EXISTS nt_title_shop (" +
                "  id                 SERIAL PRIMARY KEY," +
                "  title_id           INTEGER NOT NULL," +
                "  price              INTEGER NOT NULL DEFAULT 0," +
                "  days               INTEGER NOT NULL DEFAULT 0," +
                "  amount             INTEGER NOT NULL DEFAULT -1," +
                "  sale_end_at        BIGINT NOT NULL DEFAULT -1," +
                "  created_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "  updated_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "  FOREIGN KEY (title_id) REFERENCES nt_title(id) ON DELETE CASCADE" +
                ");";

        // player coin table
        sql += "CREATE TABLE IF NOT EXISTS nt_player_coin (" +
                "  uuid              UUID PRIMARY KEY," +
                "  coin              INTEGER NOT NULL DEFAULT 0," +
                "  created_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "  updated_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP" +
                ");";

        // player title table
        sql += "CREATE TABLE IF NOT EXISTS nt_player_title (" +
                "  id                SERIAL PRIMARY KEY," +
                "  player_uuid       UUID NOT NULL," +
                "  title_id          INTEGER NOT NULL," +
                "  expire_at         BIGINT NOT NULL DEFAULT -1," +
                "  created_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "  updated_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "  FOREIGN KEY (title_id) REFERENCES nt_title(id) ON DELETE CASCADE" +
                ");";

        // player using title table
        sql += "CREATE TABLE IF NOT EXISTS nt_player_using_title (" +
                "  uuid              UUID PRIMARY KEY," +
                "  title_id          INTEGER NOT NULL," +
                "  created_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "  updated_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "  FOREIGN KEY (title_id) REFERENCES nt_title(id) ON DELETE CASCADE" +
                ");";

        sql += "INSERT INTO nt_title (" +
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
