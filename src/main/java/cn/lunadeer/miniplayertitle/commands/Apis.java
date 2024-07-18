package cn.lunadeer.miniplayertitle.commands;

import cn.lunadeer.minecraftpluginutils.Common;
import cn.lunadeer.miniplayertitle.MiniPlayerTitle;
import cn.lunadeer.miniplayertitle.dtos.TitleDTO;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

import static cn.lunadeer.miniplayertitle.MiniPlayerTitle.usingPapi;


public class Apis {

    public static void updateName(Player player, @Nullable TitleDTO title) {
        MiniPlayerTitle.instance.setPlayerUsingTitle(player.getUniqueId(), title);
        if (usingPapi()) {
            return;
        }

        Component titleComponent = Component.text("");
        String titleBukkit = "";

        if (title != null && title.getId() != -1) {
            titleComponent = title.getTitleColored();
            titleBukkit = ChatColor.translateAlternateColorCodes('&', title.getTitleColoredBukkit());
        }

        if (Common.isPaper()) {
            Component newDisplayName = Component.text()
                    .append(titleComponent)
                    .append(Component.text("<"))
                    .append(player.name())
                    .append(Component.text(">")).build();
            Component newListName = Component.text()
                    .append(titleComponent)
                    .append(Component.text(" "))
                    .append(player.name()).build();
            player.displayName(newDisplayName);
            player.playerListName(newListName);
        } else {
            player.setDisplayName(titleBukkit + "<" + player.getName() + ">");
            player.setPlayerListName(titleBukkit + " " + player.getName());
        }
    }
}
