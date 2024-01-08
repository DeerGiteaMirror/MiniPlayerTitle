package cn.lunadeer.newbtitle;

import cn.lunadeer.newbtitle.utils.ConfigManager;
import cn.lunadeer.newbtitle.utils.Database;
import cn.lunadeer.newbtitle.utils.XLogger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class MiniPlayerTitle extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        config = new ConfigManager(instance);
        Database.migrate();

        Bukkit.getPluginManager().registerEvents(new Events(), this);
        Objects.requireNonNull(Bukkit.getPluginCommand("MiniPlayerTitle")).setExecutor(new Commands());
        Objects.requireNonNull(Bukkit.getPluginCommand("MiniPlayerTitle")).setTabCompleter(new Commands());

        XLogger.info("NewbTitle 称号插件已加载");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static MiniPlayerTitle instance;
    public static ConfigManager config;
}
