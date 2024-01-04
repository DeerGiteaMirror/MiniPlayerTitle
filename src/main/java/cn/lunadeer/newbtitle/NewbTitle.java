package cn.lunadeer.newbtitle;

import cn.lunadeer.newbtitle.utils.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class NewbTitle extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        config = new ConfigManager(instance);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static NewbTitle instance;
    public static ConfigManager config;
}
