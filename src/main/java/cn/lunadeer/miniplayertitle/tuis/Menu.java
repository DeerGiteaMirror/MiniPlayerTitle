package cn.lunadeer.miniplayertitle.tuis;

import cn.lunadeer.minecraftpluginutils.Notification;
import cn.lunadeer.minecraftpluginutils.VaultConnect;
import cn.lunadeer.minecraftpluginutils.stui.ListView;
import cn.lunadeer.minecraftpluginutils.stui.components.Button;
import cn.lunadeer.minecraftpluginutils.stui.components.Line;
import cn.lunadeer.miniplayertitle.MiniPlayerTitle;
import cn.lunadeer.miniplayertitle.dtos.PlayerInfoDTO;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static cn.lunadeer.miniplayertitle.tuis.Apis.getLastArgsPage;
import static cn.lunadeer.miniplayertitle.tuis.Apis.playerOnly;

public class Menu {
    public static void show(CommandSender sender, String[] args) {
        int page = getLastArgsPage(args);

        Player player = playerOnly(sender);
        if (player == null) return;

        PlayerInfoDTO playerInfo = PlayerInfoDTO.get(player.getUniqueId());
        if (playerInfo == null) {
            Notification.error(player, "获取玩家信息时出现错误");
            return;
        }
        Line balance;
        if (!MiniPlayerTitle.config.isExternalEco()) {
            balance = Line.create()
                    .append("称号币余额: ").append(playerInfo.getCoin().toString());
        } else {
            balance = Line.create()
                    .append("余额: ").append(playerInfo.getCoin().toString()).append(VaultConnect.instance.currencyNamePlural());
        }
        Line backpack = Line.create()
                .append(Button.create("称号背包").setExecuteCommand("/mplt my_titles").build()).append("查看你拥有的称号");
        Line shop = Line.create()
                .append(Button.create("称号商店").setExecuteCommand("/mplt shop").build()).append("购买在售称号");
        Line custom = Line.create()
                .append(Button.create("自定义称号").setExecuteCommand("/mplt custom_info").build()).append("查看如何自定义称号");
        Line manual = Line.create()
                .append(Button.create("帮助文档").setOpenURL("https://ssl.lunadeer.cn:14448/doc/2/").build()).append("在浏览器打开帮助文档");

        ListView view = ListView.create(10, "/mplt");
        view.title("称号系统")
                .navigator(Line.create().append("主菜单"))
                .add(balance)
                .add(Line.create())
                .add(backpack)
                .add(shop)
                .add(manual);

        if (MiniPlayerTitle.config.isEnableCustom()) {
            view.add(custom);
        }

        Line all = Line.create()
                .append(Button.create("所有称号").setExecuteCommand("/mplt all_titles").build()).append("查看所有称号");

        if (player.isOp()) {
            view.add(Line.create().append(""));
            view.add(Line.create().append("---以下选项仅OP可见---"));
            view.add(all);
        }

        view.showOn(player, page);
    }
}
