package cn.lunadeer.miniplayertitle.utils;

import cn.lunadeer.miniplayertitle.MiniPlayerTitle;

import java.sql.*;

public class Database {

    public static Connection createConnection() {
        try {
            String connectionUrl;
            if (MiniPlayerTitle.config.getDbType().equals("pgsql")) {
                XLogger.info("正在连接到 PostgreSQL 数据库");
                Class.forName("org.postgresql.Driver");
                connectionUrl = "jdbc:postgresql://" + MiniPlayerTitle.config.getDbHost() + ":" + MiniPlayerTitle.config.getDbPort();
                connectionUrl += "/" + MiniPlayerTitle.config.getDbName();
                return DriverManager.getConnection(connectionUrl, MiniPlayerTitle.config.getDbUser(), MiniPlayerTitle.config.getDbPass());
            } else if (MiniPlayerTitle.config.getDbType().equals("sqlite")) {
                XLogger.info("正在连接到 SQLite 数据库");
                Class.forName("org.sqlite.JDBC");
                connectionUrl = "jdbc:sqlite:" + MiniPlayerTitle.instance.getDataFolder() + "/" + MiniPlayerTitle.config.getDbName() + ".db";
                return DriverManager.getConnection(connectionUrl);
            } else {
                XLogger.err("=== 严重错误 ===");
                XLogger.err("数据库类型错误，只能为 pgsql 或 sqlite");
                XLogger.err("===============");
                return null;
            }
        } catch (ClassNotFoundException | SQLException e) {
            XLogger.err("=== 严重错误 ===");
            XLogger.err("Database connection failed: " + e.getMessage());
            XLogger.err("===============");
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
            }
        } catch (SQLException e) {
            handleDatabaseError("Database query failed: ", e, sql);
        }
        return null;
    }

    private static void handleDatabaseError(String errorMessage, SQLException e, String sql) {
        XLogger.err("=== 严重错误 ===");
        XLogger.err(errorMessage + e.getMessage());
        XLogger.err("SQL: " + sql);
        XLogger.err("===============");
    }

    private static void addColumnIfNotExists(String tableName, String columnName, String columnDefinition) {
        if (MiniPlayerTitle.config.getDbType().equals("pgsql")) {
            String sql = "ALTER TABLE " + tableName + " ADD COLUMN IF NOT EXISTS " + columnName + " " + columnDefinition + ";";
            query(sql);
        } else if (MiniPlayerTitle.config.getDbType().equals("sqlite")) {
            try {
                ResultSet rs = query("PRAGMA table_info(" + tableName + ");");
                boolean columnExists = false;
                if (rs != null) {
                    while (rs.next()) {
                        if (columnName.equals(rs.getString("name"))) {
                            columnExists = true;
                            break;
                        }
                    }
                }
                if (!columnExists) {
                    query("ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + columnDefinition + ";");
                }
            } catch (SQLException e) {
                handleDatabaseError("Database operation failed: ", e, "");
            }
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
