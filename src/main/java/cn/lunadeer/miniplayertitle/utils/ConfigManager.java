package cn.lunadeer.miniplayertitle.utils;

import cn.lunadeer.miniplayertitle.MiniPlayerTitle;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    public ConfigManager(MiniPlayerTitle plugin) {
        _plugin = plugin;
        _plugin.saveDefaultConfig();
        reload();
        _plugin.saveConfig();
    }

    public void reload() {
        _plugin.reloadConfig();
        _file = _plugin.getConfig();
        _debug = _file.getBoolean("Debug", false);
        _db_host = _file.getString("Database.Host", "localhost");
        _db_port = _file.getString("Database.Port", "5432");
        _db_name = _file.getString("Database.Name", "miniplayertitle");
        _db_user = _file.getString("Database.User", "postgres");
        _db_pass = _file.getString("Database.Pass", "postgres");
        _prefix = _file.getString("Prefix", "[");
        _suffix = _file.getString("Suffix", "]");
        _default_coin = _file.getInt("DefaultCoin", 0);
        _enable_custom = _file.getBoolean("CustomCost.Enabled", true);
        _custom_cost = _file.getInt("CustomCost.Cost", 1000);
    }

    public Boolean isDebug() {
        return _debug;
    }

    public void setDebug(Boolean debug) {
        _debug = debug;
        _file.set("Debug", debug);
        _plugin.saveConfig();
    }

    public String getDBConnectionUrl() {
        return "jdbc:postgresql://" + _db_host + ":" + _db_port + "/" + _db_name;
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


    public String getPrefix() {
        return _prefix;
    }

    public String getSuffix() {
        return _suffix;
    }

    public Integer getDefaultCoin() {
        return _default_coin;
    }

    public Boolean isEnableCustom() {
        return _enable_custom;
    }

    public Integer getCustomCost() {
        return _custom_cost;
    }

    public void enableCustom(){
        _enable_custom = true;
        _file.set("CustomCost.Enabled", true);
        _plugin.saveConfig();
    }

    public void disableCustom(){
        _enable_custom = false;
        _file.set("CustomCost.Enabled", false);
        _plugin.saveConfig();
    }

    public void setCustomCost(Integer cost){
        _custom_cost = cost;
        _file.set("CustomCost.Cost", cost);
        _plugin.saveConfig();
    }


    private final MiniPlayerTitle _plugin;
    private FileConfiguration _file;
    private Boolean _debug;

    private String _db_host;
    private String _db_port;
    private String _db_user;
    private String _db_pass;
    private String _db_name;
    private String _prefix;
    private String _suffix;
    private Integer _default_coin;
    private Boolean _enable_custom;
    private Integer _custom_cost;
}
