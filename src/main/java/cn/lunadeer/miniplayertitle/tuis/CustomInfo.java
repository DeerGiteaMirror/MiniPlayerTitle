package cn.lunadeer.miniplayertitle.tuis;

import cn.lunadeer.minecraftpluginutils.stui.ListView;
import cn.lunadeer.minecraftpluginutils.stui.components.Button;
import cn.lunadeer.minecraftpluginutils.stui.components.Line;
import cn.lunadeer.miniplayertitle.MiniPlayerTitle;
import cn.lunadeer.miniplayertitle.dtos.PlayerInfoDTO;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static cn.lunadeer.miniplayertitle.tuis.Apis.getLastArgsPage;
import static cn.lunadeer.miniplayertitle.tuis.Apis.playerOnly;

public class CustomInfo {
    public static void show(CommandSender sender, String[] args) {
        Player player = playerOnly(sender);
        if (player == null) return;
        int page = getLastArgsPage(args);
        PlayerInfoDTO playerInfo = PlayerInfoDTO.get(player.getUniqueId());
        if (playerInfo == null) {
            return;
        }

        ListView view = ListView.create(10, "/mplt custom_info");
        view.title("自定义称号帮助");
        view.navigator(Line.create()
                .append(Button.create("主菜单").setExecuteCommand("/mplt menu").build())
                .append("自定义称号"));

        Line line_1 = Line.create()
                .append("称号币余额：").append(playerInfo.getCoin().toString());
        Line line_2 = Line.create()
                .append("自定义称号状态：")
                .append(MiniPlayerTitle.config.isEnableCustom() ? "开启" : "关闭");
        Line line_3 = Line.create()
                .append("自定义称号花费：").append(MiniPlayerTitle.config.getCustomCost().toString());
        Line line_4 = Line.create()
                .append("自定义称号最大长度（不含颜色代码）：").append(MiniPlayerTitle.config.getMaxLength().toString());
        Line line_5 = Line.create()
                .append("自定义方法：")
                .append("在聊天框输入 /mplt custom_title <称号>");
        Line line_6 = Line.create()
                .append("可以使用 Minecraft渐变颜色生成器 来生成具有渐变效果的称号")
                .append(Button.create("点击在浏览器中打开生成器").setOpenURL("https://ssl.lunadeer.cn:14440/").build());

        view.add(line_1);
        view.add(line_2);
        view.add(line_3);
        view.add(line_4);
        view.add(line_5);
        view.add(line_6);

        view.showOn(player, page);
    }
}
