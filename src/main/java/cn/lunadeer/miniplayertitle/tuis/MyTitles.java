package cn.lunadeer.miniplayertitle.tuis;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static cn.lunadeer.miniplayertitle.tuis.Apis.getLastArgsPage;
import static cn.lunadeer.miniplayertitle.tuis.Apis.playerOnly;

public class MyTitles {
    public static void show(CommandSender sender, String[] args) {
        Player player = playerOnly(sender);
        if (player == null) return;
        int page = getLastArgsPage(args);
        // todo
    }
}
