package cn.lunadeer.miniplayertitle.commands;

import cn.lunadeer.miniplayertitle.MiniPlayerTitle;
import cn.lunadeer.miniplayertitle.dtos.TitleDTO;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public class Apis {
    public static boolean notOpOrConsole(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.isOp()) {
                MiniPlayerTitle.notification.warn(player, "你没有权限使用此命令");
                return true;
            }
        }
        return false;
    }

    public static void updateName(Player player, @Nullable TitleDTO title) {
        if (title == null) {
            Component newDisplayName = Component.text()
                    .append(Component.text("<"))
                    .append(player.name())
                    .append(Component.text("> ")).build();
            Component newListName = Component.text()
                    .append(player.name()).build();
            player.displayName(newDisplayName);
            player.playerListName(newListName);
            return;
        }

        Component titleComponent = title.getTitleColored();
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
    }
}
