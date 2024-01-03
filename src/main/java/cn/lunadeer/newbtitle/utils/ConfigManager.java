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
    }

    public Boolean isDebug() {
        return _debug;
    }

    public void setDebug(Boolean debug) {
        _debug = debug;
        _file.set("Debug", debug);
        _plugin.saveConfig();
    }



    private NewbTitle _plugin;
    private FileConfiguration _file;
    private Boolean _debug;
}
