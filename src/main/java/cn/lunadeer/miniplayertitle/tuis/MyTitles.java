package cn.lunadeer.miniplayertitle.tuis;

import cn.lunadeer.minecraftpluginutils.Notification;
import cn.lunadeer.minecraftpluginutils.stui.ListView;
import cn.lunadeer.minecraftpluginutils.stui.components.Button;
import cn.lunadeer.minecraftpluginutils.stui.components.Line;
import cn.lunadeer.miniplayertitle.dtos.PlayerInfoDTO;
import cn.lunadeer.miniplayertitle.dtos.PlayerTitleDTO;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static cn.lunadeer.miniplayertitle.tuis.Apis.getLastArgsPage;
import static cn.lunadeer.miniplayertitle.tuis.Apis.playerOnly;

public class MyTitles {
    public static void show(CommandSender sender, String[] args) {
        Player player = playerOnly(sender);
        if (player == null) return;
        int page = getLastArgsPage(args);

        PlayerInfoDTO playerInfo = PlayerInfoDTO.get(player.getUniqueId());
        if (playerInfo == null) {
            Notification.error(player, "获取玩家信息时出现错误，详情请查看控制台日志");
            return;
        }
        List<PlayerTitleDTO> titles = PlayerTitleDTO.getAllOf(player.getUniqueId());
        ListView view = ListView.create(10, "/mplt my_titles");
        view.title("称号背包");
        view.navigator(Line.create()
                .append(Button.create("主菜单").setExecuteCommand("/mplt menu").build())
                .append("称号背包"));

        for (PlayerTitleDTO title : titles) {
            if (title == null) {
                continue;
            }
            Line line = Line.create()
                    .append(title.getTitle().getTitleColored());
            if (title.getExpireAt() == null) {
                line.append("永久");
            } else if (title.getExpireAt().isBefore(LocalDateTime.now())) {
                line.append("已过期");
            } else {
                line.append("有效期至: " + title.getExpireAt().getYear() + "年" + title.getExpireAt().getMonthValue() + "月" + title.getExpireAt().getDayOfMonth() + "日");
            }
            if (Objects.equals(playerInfo.getUsingTitle().getId(), title.getTitle().getId())) {
                line.append(Button.createRed("卸下").setExecuteCommand("/mplt use_title -1 " + page).build());
            } else {
                line.append(Button.createGreen("使用").setExecuteCommand("/mplt use_title " + title.getId() + " " + page).build());
            }
            view.add(line);
        }

        view.showOn(player, page);
    }
}
