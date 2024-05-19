package cn.lunadeer.miniplayertitle;

import cn.lunadeer.minecraftpluginutils.*;
import cn.lunadeer.miniplayertitle.utils.ConfigManager;
import cn.lunadeer.miniplayertitle.utils.DatabaseTables;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class MiniPlayerTitle extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        notification = new Notification(this);
        logger = new XLogger(instance);
        config = new ConfigManager(instance);
        logger.setDebug(config.isDebug());
        database = new DatabaseManager(this,
                config.getDbType().equals("pgsql") ? DatabaseManager.TYPE.POSTGRESQL : DatabaseManager.TYPE.SQLITE,
                config.getDbHost(),
                config.getDbPort(),
                config.getDbName(),
                config.getDbUser(),
                config.getDbPass());
        DatabaseTables.migrate();

        Bukkit.getPluginManager().registerEvents(new Events(), this);
        Objects.requireNonNull(Bukkit.getPluginCommand("MiniPlayerTitle")).setExecutor(new Commands());
        Objects.requireNonNull(Bukkit.getPluginCommand("MiniPlayerTitle")).setTabCompleter(new Commands());

        bStatsMetrics metrics = new bStatsMetrics(this, 21444);
        if (config.isCheckUpdate()) {
            giteaReleaseCheck = new GiteaReleaseCheck(this,
                    "https://ssl.lunadeer.cn:14446",
                    "zhangyuheng",
                    "MiniPlayerTitle");
        }

        logger.info("称号插件已加载");
        logger.info("版本: " + getPluginMeta().getVersion());
        // http://patorjk.com/software/taag/#p=display&f=Big&t=MiniPlayerTitle
        logger.info("  __  __ _       _ _____  _                    _______ _ _   _");
        logger.info(" |  \\/  (_)     (_)  __ \\| |                  |__   __(_) | | |");
        logger.info(" | \\  / |_ _ __  _| |__) | | __ _ _   _  ___ _ __| |   _| |_| | ___");
        logger.info(" | |\\/| | | '_ \\| |  ___/| |/ _` | | | |/ _ \\ '__| |  | | __| |/ _ \\");
        logger.info(" | |  | | | | | | | |    | | (_| | |_| |  __/ |  | |  | | |_| |  __/");
        logger.info(" |_|  |_|_|_| |_|_|_|    |_|\\__,_|\\__, |\\___|_|  |_|  |_|\\__|_|\\___|");
        logger.info("                                   __/ |");
        logger.info("                                  |___/");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static MiniPlayerTitle instance;
    public static ConfigManager config;
    public static DatabaseManager database;
    public static XLogger logger;
    public static Notification notification;
    private GiteaReleaseCheck giteaReleaseCheck;
}
