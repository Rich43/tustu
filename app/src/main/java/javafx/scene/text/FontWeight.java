package javafx.scene.text;

import com.sun.javafx.font.LogicalFont;
import sun.font.Font2D;

/* loaded from: jfxrt.jar:javafx/scene/text/FontWeight.class */
public enum FontWeight {
    THIN(100, "Thin"),
    EXTRA_LIGHT(200, "Extra Light", "Ultra Light"),
    LIGHT(300, "Light"),
    NORMAL(400, "Normal", LogicalFont.STYLE_REGULAR),
    MEDIUM(500, "Medium"),
    SEMI_BOLD(600, "Semi Bold", "Demi Bold"),
    BOLD(Font2D.FWEIGHT_BOLD, LogicalFont.STYLE_BOLD),
    EXTRA_BOLD(800, "Extra Bold", "Ultra Bold"),
    BLACK(900, "Black", "Heavy");

    private final int weight;
    private final String[] names;

    FontWeight(int weight, String... names) {
        this.weight = weight;
        this.names = names;
    }

    public int getWeight() {
        return this.weight;
    }

    public static FontWeight findByName(String name) {
        if (name == null) {
            return null;
        }
        for (FontWeight w2 : values()) {
            for (String n2 : w2.names) {
                if (n2.equalsIgnoreCase(name)) {
                    return w2;
                }
            }
        }
        return null;
    }

    public static FontWeight findByWeight(int weight) {
        if (weight <= 150) {
            return THIN;
        }
        if (weight <= 250) {
            return EXTRA_LIGHT;
        }
        if (weight < 350) {
            return LIGHT;
        }
        if (weight <= 450) {
            return NORMAL;
        }
        if (weight <= 550) {
            return MEDIUM;
        }
        if (weight < 650) {
            return SEMI_BOLD;
        }
        if (weight <= 750) {
            return BOLD;
        }
        if (weight <= 850) {
            return EXTRA_BOLD;
        }
        return BLACK;
    }
}
