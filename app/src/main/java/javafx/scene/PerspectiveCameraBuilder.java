package javafx.scene;

import javafx.scene.PerspectiveCameraBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/PerspectiveCameraBuilder.class */
public class PerspectiveCameraBuilder<B extends PerspectiveCameraBuilder<B>> implements Builder<PerspectiveCamera> {
    private boolean __set;
    private double fieldOfView;

    protected PerspectiveCameraBuilder() {
    }

    public static PerspectiveCameraBuilder<?> create() {
        return new PerspectiveCameraBuilder<>();
    }

    public void applyTo(PerspectiveCamera x2) {
        if (this.__set) {
            x2.setFieldOfView(this.fieldOfView);
        }
    }

    public B fieldOfView(double x2) {
        this.fieldOfView = x2;
        this.__set = true;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public PerspectiveCamera build() {
        PerspectiveCamera x2 = new PerspectiveCamera();
        applyTo(x2);
        return x2;
    }
}
