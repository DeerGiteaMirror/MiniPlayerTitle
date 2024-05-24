package cn.lunadeer.miniplayertitle;

import cn.lunadeer.miniplayertitle.dtos.PlayerInfoDTO;
import cn.lunadeer.miniplayertitle.dtos.PlayerTitleDTO;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static cn.lunadeer.miniplayertitle.commands.Apis.updateName;

public class Events implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player bukkitPlayer = event.getPlayer();
        PlayerInfoDTO player = PlayerInfoDTO.get(bukkitPlayer);
        if (player == null) {
            MiniPlayerTitle.notification.error(bukkitPlayer, "获取玩家信息时出现错误，请联系管理员");
            return;
        }
        PlayerTitleDTO title = PlayerTitleDTO.get(bukkitPlayer.getUniqueId(), player.getUsingTitle().getId());
        if (title == null || title.isExpired()) {
            MiniPlayerTitle.notification.warn(bukkitPlayer, "你当前使用的称号 %s 已过期", player.getUsingTitle().getTitlePlainText());
            player.setUsingTitle(null);
            updateName(bukkitPlayer, null);
        } else {
            updateName(bukkitPlayer, title.getTitle());
        }
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
