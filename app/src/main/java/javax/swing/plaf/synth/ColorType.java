package javax.swing.plaf.synth;

/* loaded from: rt.jar:javax/swing/plaf/synth/ColorType.class */
public class ColorType {
    public static final ColorType FOREGROUND = new ColorType("Foreground");
    public static final ColorType BACKGROUND = new ColorType("Background");
    public static final ColorType TEXT_FOREGROUND = new ColorType("TextForeground");
    public static final ColorType TEXT_BACKGROUND = new ColorType("TextBackground");
    public static final ColorType FOCUS = new ColorType("Focus");
    public static final int MAX_COUNT = Math.max(FOREGROUND.getID(), Math.max(BACKGROUND.getID(), FOCUS.getID())) + 1;
    private static int nextID;
    private String description;
    private int index;

    protected ColorType(String str) {
        if (str == null) {
            throw new NullPointerException("ColorType must have a valid description");
        }
        this.description = str;
        synchronized (ColorType.class) {
            int i2 = nextID;
            nextID = i2 + 1;
            this.index = i2;
        }
    }

    public final int getID() {
        return this.index;
    }

    public String toString() {
        return this.description;
    }
}
