package cn.lunadeer.miniplayertitle.events;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static cn.lunadeer.miniplayertitle.MiniPlayerTitle.usingPapi;

public class PaperChat implements Listener {
    @EventHandler
    public void onPlayerSendChat(AsyncChatEvent event) {
        if (usingPapi()) {
            return;
        }
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
