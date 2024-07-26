package cn.lunadeer.miniplayertitle.commands;

import cn.lunadeer.minecraftpluginutils.Notification;
import cn.lunadeer.miniplayertitle.dtos.PlayerTitleDTO;
import cn.lunadeer.miniplayertitle.dtos.TitleDTO;
import cn.lunadeer.miniplayertitle.dtos.TitleShopDTO;
import cn.lunadeer.miniplayertitle.tuis.MyTitles;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TitleCard implements Listener {

    public static void getTitleCard(CommandSender sender, String[] args) {
        try {
            if (!sender.hasPermission("mplt.admin")) return;
            if (!(sender instanceof Player)) {
                Notification.error(sender, "该命令只能由玩家执行");
                return;
            }
            if (args.length != 2) {
                Notification.warn(sender, "用法: /mplt get_card <销售ID>");
                return;
            }
            int saleId = Integer.parseInt(args[1]);
            TitleShopDTO titleShop = TitleShopDTO.get(saleId);
            if (titleShop == null) {
                Notification.error(sender, "获取销售详情时出现错误");
                return;
            }
            if (titleShop.getDays() == 0) {
                Notification.error(sender, "不可以生成天数为0的称号卡！");
                return;
            }
            ItemStack card = TitleCard.create(titleShop);
            Player player = (Player) sender;
            player.getInventory().addItem(card);
            Notification.info(player, "成功创建称号卡");
        } catch (Exception e) {
            Notification.error(sender, e.getMessage());
        }
    }

    @EventHandler
    public void useTitleCard(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getInventory().getItemInMainHand().getType() != Material.NAME_TAG) {
            return;
        }
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getItemMeta() == null || item.getItemMeta().getLore() == null || item.getItemMeta().getLore().size() != 5) {
            return;
        }
        use(player, item);
        player.getInventory().removeItem(item);
    }

    private static ItemStack create(@NotNull TitleShopDTO saleInfo) {
        ItemStack card = new ItemStack(Material.NAME_TAG);
        TitleDTO title = saleInfo.getTitle();
        card.editMeta(meta -> {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', title.getTitleColoredBukkit()));
            meta.setLore(
                    Arrays.asList(
                            "称号ID: " + title.getId(),
                            "使用后获得天数: " + (saleInfo.getDays() == -1 ? "永久" : saleInfo.getDays()),
                            "称号描述: " + title.getDescription(),
                            "",
                            ChatColor.GRAY + "【右键使用】"

                    )
            );
        });
        return card;
    }

    private static void use(@NotNull Player player, @NotNull ItemStack item) {
        try {
            if (item.getType() != Material.NAME_TAG) {
                return;
            }
            List<String> lore = item.getLore();
            if (lore == null || lore.size() != 5) {
                return;
            }
            int titleId = Integer.parseInt(lore.get(0).split(": ")[1]);
            int day = lore.get(1).split(": ")[1].equals("永久") ? -1 : Integer.parseInt(lore.get(1).split(": ")[1]);

            List<PlayerTitleDTO> playerTitles = PlayerTitleDTO.getAllOf(player.getUniqueId());
            PlayerTitleDTO had = null;
            for (PlayerTitleDTO playerTitle : playerTitles) {
                if (Objects.equals(playerTitle.getTitle().getId(), titleId)) {
                    had = playerTitle;
                    break;
                }
            }

            TitleDTO title = TitleDTO.get(titleId);
            if (title == null) {
                Notification.error(player, "称号不存在");
                return;
            }

            if (had == null) {
                had = PlayerTitleDTO.create(player.getUniqueId(), title, day == -1 ? null : LocalDateTime.now().plusDays(day));
                if (had == null) {
                    Notification.error(player, "购买称号时出现错误，详情请查看控制台日志");
                    return;
                }
                Notification.info(player, Component.text("成功使用称号卡: ").append(had.getTitle().getTitleColored()));
            } else if (!had.isExpired()) {
                Notification.warn(player, "你已拥有此称号，在过期前无法使用称号卡");
            } else {
                had.setExpireAt(day == -1 ? null : LocalDateTime.now().plusDays(day));
                Notification.info(player, Component.text("成功续续期称号: ").append(had.getTitle().getTitleColored()));
            }
            TitleManage.useTitle(player, new String[]{"use_title", String.valueOf(had.getId())});
        } catch (Exception e) {
            Notification.error(player, "使用称号卡时出现错误: %s", e.getMessage());
        }
    }

}
