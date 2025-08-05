package javafx.scene;

import javafx.scene.image.WritableImage;

/* loaded from: jfxrt.jar:javafx/scene/SnapshotResult.class */
public class SnapshotResult {
    private WritableImage image;
    private Object source;
    private SnapshotParameters params;

    SnapshotResult(WritableImage image, Object source, SnapshotParameters params) {
        this.image = image;
        this.source = source;
        this.params = params;
    }

    public WritableImage getImage() {
        return this.image;
    }

    public Object getSource() {
        return this.source;
    }

    public SnapshotParameters getSnapshotParameters() {
        return this.params;
    }
}
