package javafx.scene;

/* loaded from: jfxrt.jar:javafx/scene/SceneAntialiasing.class */
public final class SceneAntialiasing {
    public static final SceneAntialiasing DISABLED = new SceneAntialiasing("DISABLED");
    public static final SceneAntialiasing BALANCED = new SceneAntialiasing("BALANCED");
    private final String val;

    private SceneAntialiasing(String value) {
        this.val = value;
    }

    public String toString() {
        return this.val;
    }
}
