package javafx.scene.layout;

/* loaded from: jfxrt.jar:javafx/scene/layout/Priority.class */
public enum Priority {
    ALWAYS,
    SOMETIMES,
    NEVER;

    public static Priority max(Priority a2, Priority b2) {
        if (a2 == ALWAYS || b2 == ALWAYS) {
            return ALWAYS;
        }
        if (a2 == SOMETIMES || b2 == SOMETIMES) {
            return SOMETIMES;
        }
        return NEVER;
    }

    public static Priority min(Priority a2, Priority b2) {
        if (a2 == NEVER || b2 == NEVER) {
            return NEVER;
        }
        if (a2 == SOMETIMES || b2 == SOMETIMES) {
            return SOMETIMES;
        }
        return ALWAYS;
    }
}
