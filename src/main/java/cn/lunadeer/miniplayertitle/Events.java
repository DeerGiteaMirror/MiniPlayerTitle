package cn.lunadeer.miniplayertitle;

import cn.lunadeer.miniplayertitle.utils.Notification;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Events implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player bukkitPlayer = event.getPlayer();
        XPlayer player = new XPlayer(bukkitPlayer);
        Commands.home_view(bukkitPlayer);
        player.updateName();
    }

    @EventHandler
    public void onPlayerSendChat(AsyncChatEvent event) {
        Component nameComponent = event.getPlayer().displayName();
        Component chatComponent = event.message();
        Component newChatComponent = Component.text()
                .append(nameComponent)
                .append(Component.text(" "))
                .append(chatComponent).build();
        event.setCancelled(true);
        event.getPlayer().getServer().sendMessage(newChatComponent);
    }
}
