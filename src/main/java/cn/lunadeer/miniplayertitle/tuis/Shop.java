package cn.lunadeer.miniplayertitle.tuis;

import cn.lunadeer.minecraftpluginutils.stui.ListView;
import cn.lunadeer.minecraftpluginutils.stui.components.Button;
import cn.lunadeer.minecraftpluginutils.stui.components.Line;
import cn.lunadeer.miniplayertitle.dtos.TitleShopDTO;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static cn.lunadeer.miniplayertitle.tuis.Apis.getLastArgsPage;
import static cn.lunadeer.miniplayertitle.tuis.Apis.playerOnly;

public class Shop {
    public static void show(CommandSender sender, String[] args) {
        Player player = playerOnly(sender);
        if (player == null) return;
        int page = getLastArgsPage(args);
        List<TitleShopDTO> titles = TitleShopDTO.getAll();

        ListView view = ListView.create(10, "/mplt shop");
        view.title("称号商店");
        view.navigator(Line.create()
                .append(Button.create("主菜单").setExecuteCommand("/mplt menu").build())
                .append("称号商店"));

        for (TitleShopDTO title : titles) {
            if (title == null) {
                continue;
            }
            if (title.getTitle().getId() == -1) {
                continue;
            }
            Line line = Line.create()
                    .append(title.getId().toString())
                    .append(title.getTitle().getTitleColored())
                    .append(Button.createGreen("详情").setExecuteCommand("/mplt sale_info " + title.getId()).build());
            view.add(line);
        }

        view.showOn(player, page);
    }
}
