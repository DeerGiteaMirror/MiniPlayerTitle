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
        _db_type = _file.getString("Database.Type", "sqlite");
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
        _max_length = _file.getInt("CustomCost.MaxLength", 8);
        _check_update = _file.getBoolean("CheckUpdate", true);
        _external_eco = _file.getBoolean("ExternalEco", false);
    }

    public Boolean isDebug() {
        return _debug;
    }

    public void setDebug(Boolean debug) {
        _debug = debug;
        _file.set("Debug", debug);
        _plugin.saveConfig();
    }


    public String getDbType() {
        return _db_type;
    }

    public void setDbType(String db_type) {
        _db_type = db_type;
        _file.set("Database.Type", db_type);
        _plugin.saveConfig();
    }

    public String getDbHost() {
        return _db_host;
    }

    public String getDbPort() {
        return _db_port;
    }

    public String getDbName() {
        return _db_name;
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

    public Integer getMaxLength() {
        return _max_length;
    }

    public void setCustom(boolean enable) {
        _enable_custom = true;
        _file.set("CustomCost.Enabled", enable);
        _plugin.saveConfig();
    }

    public void setCustomCost(Integer cost){
        _custom_cost = cost;
        _file.set("CustomCost.Cost", cost);
        _plugin.saveConfig();
    }

    public void setMaxLength(Integer length){
        _max_length = length;
        _file.set("CustomCost.MaxLength", length);
        _plugin.saveConfig();
    }

    public Boolean isCheckUpdate() {
        return _check_update;
    }

    public Boolean isExternalEco() {
        return _external_eco;
    }


    private final MiniPlayerTitle _plugin;
    private FileConfiguration _file;
    private Boolean _debug;

    private String _db_type;
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
    private Integer _max_length;
    private Boolean _check_update;
    private Boolean _external_eco;
}
