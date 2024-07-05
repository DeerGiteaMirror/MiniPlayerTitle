package cn.lunadeer.miniplayertitle;

import cn.lunadeer.minecraftpluginutils.XLogger;
import cn.lunadeer.miniplayertitle.dtos.PlayerInfoDTO;
import cn.lunadeer.miniplayertitle.dtos.PlayerTitleDTO;
import cn.lunadeer.miniplayertitle.dtos.TitleDTO;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import static cn.lunadeer.miniplayertitle.MiniPlayerTitle.usingPapi;

public class Expansion extends PlaceholderExpansion {

    private final JavaPlugin plugin;

    public static Expansion instance;

    public Expansion(JavaPlugin plugin) {
        this.plugin = plugin;
        if (usingPapi()) {
            XLogger.info("PlaceholderAPI is enabled, registering expansion...");
            this.register();
        } else {
            XLogger.warn("PlaceholderAPI is not enabled, using self impl of placeholders...");
        }
        instance = this;
    }

    @Override
    public String onPlaceholderRequest(Player bukkitPlayer, @NotNull String params) {
        if (params.equalsIgnoreCase("player_title")) {
            TitleDTO t = MiniPlayerTitle.instance.getPlayerUsingTitle(bukkitPlayer.getUniqueId());
            if (t == null) {
                return "";
            }
            return ChatColor.translateAlternateColorCodes('&', t.getTitleColoredBukkit());
        }

        return null; //
    }

    @Override
    public @NotNull String getIdentifier() {
        return "mplt";
    }

    @Override
    public @NotNull String getAuthor() {
        return "zhangyuheng";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getPluginMeta().getVersion();
    }
}
