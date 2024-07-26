package cn.lunadeer.miniplayertitle.events;

import cn.lunadeer.miniplayertitle.MiniPlayerTitle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import static cn.lunadeer.miniplayertitle.MiniPlayerTitle.usingPapi;

public class SpigotChat implements Listener {
    @EventHandler
    public void onPlayerSendChat(PlayerChatEvent event) {
        if (usingPapi()) {
            return;
        }
        event.setCancelled(true);
        MiniPlayerTitle.instance.getServer().broadcastMessage(event.getPlayer().getDisplayName() + " " + event.getMessage());
    }
}
