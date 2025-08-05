package javafx.scene;

import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParametersBuilder;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Transform;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/SnapshotParametersBuilder.class */
public class SnapshotParametersBuilder<B extends SnapshotParametersBuilder<B>> implements Builder<SnapshotParameters> {
    private int __set;
    private Camera camera;
    private boolean depthBuffer;
    private Paint fill;
    private Transform transform;
    private Rectangle2D viewport;

    protected SnapshotParametersBuilder() {
    }

    public static SnapshotParametersBuilder<?> create() {
        return new SnapshotParametersBuilder<>();
    }

    public void applyTo(SnapshotParameters x2) {
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setCamera(this.camera);
        }
        if ((set & 2) != 0) {
            x2.setDepthBuffer(this.depthBuffer);
        }
        if ((set & 4) != 0) {
            x2.setFill(this.fill);
        }
        if ((set & 8) != 0) {
            x2.setTransform(this.transform);
        }
        if ((set & 16) != 0) {
            x2.setViewport(this.viewport);
        }
    }

    public B camera(Camera x2) {
        this.camera = x2;
        this.__set |= 1;
        return this;
    }

    public B depthBuffer(boolean x2) {
        this.depthBuffer = x2;
        this.__set |= 2;
        return this;
    }

    public B fill(Paint x2) {
        this.fill = x2;
        this.__set |= 4;
        return this;
    }

    public B transform(Transform x2) {
        this.transform = x2;
        this.__set |= 8;
        return this;
    }

    public B viewport(Rectangle2D x2) {
        this.viewport = x2;
        this.__set |= 16;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public SnapshotParameters build2() {
        SnapshotParameters x2 = new SnapshotParameters();
        applyTo(x2);
        return x2;
    }
}
