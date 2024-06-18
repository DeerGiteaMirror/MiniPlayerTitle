package cn.lunadeer.miniplayertitle;

import cn.lunadeer.minecraftpluginutils.XLogger;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;

public class Color {
    public Integer r;
    public Integer g;
    public Integer b;
    public String hex;

    public Color(Integer r, Integer g, Integer b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.hex = String.format("#%02x%02x%02x", r, g, b);
    }

    public Color(String r, String g, String b) {
        try {
            this.r = Integer.valueOf(r);
            this.g = Integer.valueOf(g);
            this.b = Integer.valueOf(b);
            this.hex = String.format("#%02x%02x%02x", this.r, this.g, this.b);
        } catch (NumberFormatException e) {
            this.r = 0;
            this.g = 0;
            this.b = 0;
            this.hex = "#000000";
            XLogger.err("Invalid color: " + r + ", " + g + ", " + b);
        }
    }

    public Color(String hex) {
        try {
            this.hex = hex;
            this.r = Integer.valueOf(hex.substring(1, 3), 16);
            this.g = Integer.valueOf(hex.substring(3, 5), 16);
            this.b = Integer.valueOf(hex.substring(5, 7), 16);
        } catch (NumberFormatException e) {
            this.r = 0;
            this.g = 0;
            this.b = 0;
            this.hex = "#000000";
            XLogger.err("Invalid color: " + hex);
        }
    }

    public Style getStyle() {
        return Style.style(TextColor.color(r, g, b));
    }
}
