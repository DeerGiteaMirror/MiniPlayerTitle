package cn.lunadeer.miniplayertitle.tuis;

import cn.lunadeer.minecraftpluginutils.stui.ListView;
import cn.lunadeer.minecraftpluginutils.stui.components.Button;
import cn.lunadeer.minecraftpluginutils.stui.components.Line;
import cn.lunadeer.miniplayertitle.dtos.TitleDTO;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static cn.lunadeer.miniplayertitle.tuis.Apis.getLastArgsPage;
import static cn.lunadeer.miniplayertitle.tuis.Apis.playerOnly;

public class AllTitles {
    public static void show(CommandSender sender, String[] args) {
        Player player = playerOnly(sender);
        if (player == null) return;
        int page = getLastArgsPage(args);
        List<TitleDTO> titles = TitleDTO.getAll();

        ListView view = ListView.create(10, "/mplt all_titles");
        view.title("所有称号");
        view.navigator(Line.create()
                .append(Button.create("主菜单").setExecuteCommand("/mplt menu").build())
                .append("所有称号"));

        for (TitleDTO title : titles) {
            if (title.getId() == -1){
                continue;
            }
            Line line = Line.create()
                    .append(title.getId().toString())
                    .append(title.getTitleColored());
            if (player.hasPermission("mplt.admin")) {
                line.append(Button.createRed("删除").setExecuteCommand("/mplt delete_title " + title.getId() + " " + page).build());
                line.append(Button.createGreen("创建商品").setExecuteCommand("/mplt create_sale " + title.getId()).build());
            }
            view.add(line);
        }
        view.showOn(player, page);
    }
}
