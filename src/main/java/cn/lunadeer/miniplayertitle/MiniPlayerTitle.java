package cn.lunadeer.miniplayertitle;

import cn.lunadeer.miniplayertitle.utils.ConfigManager;
import cn.lunadeer.miniplayertitle.utils.Database;
import cn.lunadeer.miniplayertitle.utils.XLogger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.util.Objects;

public final class MiniPlayerTitle extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        config = new ConfigManager(instance);
        dbConnection = Database.createConnection();
        Database.migrate();

        Bukkit.getPluginManager().registerEvents(new Events(), this);
        Objects.requireNonNull(Bukkit.getPluginCommand("MiniPlayerTitle")).setExecutor(new Commands());
        Objects.requireNonNull(Bukkit.getPluginCommand("MiniPlayerTitle")).setTabCompleter(new Commands());

        String logo = "称号插件已加载 版本: " + getPluginMeta().getVersion() + "\n";
        // http://patorjk.com/software/taag/#p=display&f=Big&t=MiniPlayerTitle
        logo += "  __  __ _       _ _____  _                    _______ _ _   _\n" +
                " |  \\/  (_)     (_)  __ \\| |                  |__   __(_) | | |\n" +
                " | \\  / |_ _ __  _| |__) | | __ _ _   _  ___ _ __| |   _| |_| | ___\n" +
                " | |\\/| | | '_ \\| |  ___/| |/ _` | | | |/ _ \\ '__| |  | | __| |/ _ \\\n" +
                " | |  | | | | | | | |    | | (_| | |_| |  __/ |  | |  | | |_| |  __/\n" +
                " |_|  |_|_|_| |_|_|_|    |_|\\__,_|\\__, |\\___|_|  |_|  |_|\\__|_|\\___|\n" +
                "                                   __/ |\n" +
                "                                  |___/";
        XLogger.info(logo);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static MiniPlayerTitle instance;
    public static ConfigManager config;
    public static Connection dbConnection;
}
