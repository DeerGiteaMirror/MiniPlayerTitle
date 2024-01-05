package cn.lunadeer.newbtitle.utils.STUI;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;


public class Line {
    public enum Slot {
        LEFT,
        MIDDLE,
        RIGHT
    }
    private TextComponent left_elements;
    private TextComponent middle_elements;
    private TextComponent right_elements;


    public Line() {
        this.left_elements = Component.text().build();
        this.middle_elements = Component.text().build();
        this.right_elements = Component.text().build();
    }

    public TextComponent build() {
        TextComponent gap = Component.text(" ");
        return Component.text().append(left_elements)
                .append(gap)
                .append(middle_elements)
                .append(gap)
                .append(right_elements).build();
    }

    public static Line create() {
        return new Line();
    }

    public Line set(Slot slot, TextComponent component) {
        switch (slot) {
            case LEFT:
                this.left_elements = component;
                break;
            case MIDDLE:
                this.middle_elements = component;
                break;
            case RIGHT:
                this.right_elements = component;
                break;
        }
        return this;
    }

    public Line set(Slot slot, String component) {
        switch (slot) {
            case LEFT:
                this.left_elements = Component.text(component);
                break;
            case MIDDLE:
                this.middle_elements = Component.text(component);
                break;
            case RIGHT:
                this.right_elements = Component.text(component);
                break;
        }
        return this;
    }

}
