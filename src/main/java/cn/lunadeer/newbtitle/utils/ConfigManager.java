package cn.lunadeer.newbtitle.utils;

import cn.lunadeer.newbtitle.NewbTitle;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    public ConfigManager(NewbTitle plugin) {
        _plugin = plugin;
        _plugin.saveDefaultConfig();
        reload();
    }

    public void reload() {
        _plugin.reloadConfig();
        _file = _plugin.getConfig();
        _debug = _file.getBoolean("Debug", false);
        _db_host = _file.getString("Database.Host", "localhost");
        _db_port = _file.getString("Database.Port", "5432");
        _db_name = _file.getString("Database.Name", "newbtitle");
        _db_user = _file.getString("Database.User", "postgres");
        _db_pass = _file.getString("Database.Pass", "postgres");
        _prefix = _file.getString("Prefix", "[");
        _suffix = _file.getString("Suffix", "]");
    }

    public Boolean isDebug() {
        return _debug;
    }

    public void setDebug(Boolean debug) {
        _debug = debug;
        _file.set("Debug", debug);
        _plugin.saveConfig();
    }

    public String getDBConnectionUrl(){
        return "jdbc:postgresql://" + _db_host + ":" + _db_port + "/" + _db_name;
    }

    public void setDbHost(String db_host) {
        _db_host = db_host;
        _file.set("Database.Host", db_host);
        _plugin.saveConfig();
    }


    public void setDbPort(String db_port) {
        _db_port = db_port;
        _file.set("Database.Port", db_port);
        _plugin.saveConfig();
    }


    public void setDbUser(String db_user) {
        _db_user = db_user;
        _file.set("Database.User", db_user);
        _plugin.saveConfig();
    }

    public String getDbUser() {
        if (_db_user.contains("@")) {
            setDbUser("'" + _db_user + "'");
        }
        return _db_user;
    }


    public void setDbPass(String db_pass) {
        _db_pass = db_pass;
        _file.set("Database.Pass", db_pass);
        _plugin.saveConfig();
    }

    public String getDbPass() {
        if (_db_pass.contains("@")) {
            setDbPass("'" + _db_pass + "'");
        }
        return _db_pass;
    }


    public void setDbName(String db_name) {
        _db_name = db_name;
        _file.set("Database.Name", db_name);
        _plugin.saveConfig();
    }

    public String getPrefix() {
        return _prefix;
    }

    public String getSuffix() {
        return _suffix;
    }


    private final NewbTitle _plugin;
    private FileConfiguration _file;
    private Boolean _debug;

    private String _db_host;
    private String _db_port;
    private String _db_user;
    private String _db_pass;
    private String _db_name;
    private String _prefix;
    private String _suffix;
}