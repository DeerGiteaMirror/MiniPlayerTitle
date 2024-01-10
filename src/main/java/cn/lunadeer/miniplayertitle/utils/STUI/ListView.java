package cn.lunadeer.miniplayertitle.utils.STUI;

import cn.lunadeer.miniplayertitle.utils.Notification;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ListView {

    private final Integer page_size;
    private final List<Line> lines = new ArrayList<>();
    private String command = "";
    private final View view = View.create();

    public ListView(int page_size, String command) {
        super();
        this.page_size = page_size;
        this.command = command;
    }

    public static ListView create(int page_size, String command) {
        return new ListView(page_size, command);
    }

    public ListView title(String title) {
        view.title(title);
        return this;
    }

    public ListView title(String title, String subtitle) {
        view.title(title);
        view.subtitle(subtitle);
        return this;
    }

    public ListView subtitle(String subtitle) {
        view.subtitle(subtitle);
        return this;
    }

    public ListView add(Line line) {
        lines.add(line);
        return this;
    }

    public ListView addLines(List<Line> lines) {
        this.lines.addAll(lines);
        return this;
    }

    public void showOn(Player player, Integer page) {
        int offset = (page - 1) * page_size;
        if (offset >= lines.size() || offset < 0) {
            Notification.error(player, "页数超出范围");
            return;
        }
        for (int i = offset; i < offset + page_size; i++) {
            if (i >= lines.size()) {
                break;
            }
            view.addLine(lines.get(i));
        }
        view.actionBar(Pagination.create(page, lines.size(), this.command));
        view.showOn(player);
    }
}
