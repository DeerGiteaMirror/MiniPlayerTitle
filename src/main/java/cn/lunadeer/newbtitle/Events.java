package cn.lunadeer.newbtitle;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Events implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        XPlayer player = new XPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerSendChat(AsyncChatEvent event) {
        XPlayer xPlayer = new XPlayer(event.getPlayer());
        PlayerTitle title = xPlayer.getTitle();
        Component nameComponent = event.getPlayer().displayName();
        Component chatComponent = event.message();
        if (title == null) {
            Component newChatComponent = Component.text()
                    .append(Component.text("<"))
                    .append(nameComponent)
                    .append(Component.text("> "))
                    .append(chatComponent).build();
            event.setCancelled(true);
            event.getPlayer().getServer().sendMessage(newChatComponent);
        }
        Component titleComponent = title.getTitle();
        Component newChatComponent = Component.text().append(titleComponent)
                .append(Component.text("<"))
                .append(nameComponent)
                .append(Component.text("> "))
                .append(chatComponent).build();
        event.setCancelled(true);
        event.getPlayer().getServer().sendMessage(newChatComponent);
    }
}
