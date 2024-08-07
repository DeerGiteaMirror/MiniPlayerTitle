package cn.lunadeer.miniplayertitle.events;

import cn.lunadeer.minecraftpluginutils.Notification;
import cn.lunadeer.miniplayertitle.dtos.PlayerInfoDTO;
import cn.lunadeer.miniplayertitle.dtos.PlayerTitleDTO;
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
            Notification.error(bukkitPlayer, "获取玩家信息时出现错误，请联系管理员");
            return;
        }
        if (player.getUsingTitle().getId() == -1) {
            updateName(bukkitPlayer, null);
            return;
        }
        PlayerTitleDTO title = PlayerTitleDTO.get(bukkitPlayer.getUniqueId(), player.getUsingTitle().getId());
        if (title == null || title.isExpired()) {
            Notification.warn(bukkitPlayer, "你当前使用的称号 %s 已过期", player.getUsingTitle().getTitlePlainText());
            player.setUsingTitle(null);
            updateName(bukkitPlayer, null);
        } else {
            updateName(bukkitPlayer, title.getTitle());
        }
    }
}
