package javafx.scene.text;

/* loaded from: jfxrt.jar:javafx/scene/text/FontPosture.class */
public enum FontPosture {
    REGULAR("", "regular"),
    ITALIC("italic");

    private final String[] names;

    FontPosture(String... names) {
        this.names = names;
    }

    public static FontPosture findByName(String name) {
        if (name == null) {
            return null;
        }
        for (FontPosture s2 : values()) {
            for (String n2 : s2.names) {
                if (n2.equalsIgnoreCase(name)) {
                    return s2;
                }
            }
        }
        return null;
    }
}
