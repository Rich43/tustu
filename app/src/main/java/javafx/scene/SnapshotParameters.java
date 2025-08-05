package javafx.scene;

import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Transform;
import sun.util.logging.PlatformLogger;

/* loaded from: jfxrt.jar:javafx/scene/SnapshotParameters.class */
public class SnapshotParameters {
    private boolean depthBuffer;
    private Camera camera;
    private Transform transform;
    private Paint fill;
    private Rectangle2D viewport;
    Camera defaultCamera;

    public boolean isDepthBuffer() {
        return this.depthBuffer;
    }

    boolean isDepthBufferInternal() {
        if (!Platform.isSupported(ConditionalFeature.SCENE3D)) {
            return false;
        }
        return this.depthBuffer;
    }

    public void setDepthBuffer(boolean depthBuffer) {
        if (depthBuffer && !Platform.isSupported(ConditionalFeature.SCENE3D)) {
            String logname = SnapshotParameters.class.getName();
            PlatformLogger.getLogger(logname).warning("System can't support ConditionalFeature.SCENE3D");
        }
        this.depthBuffer = depthBuffer;
    }

    public Camera getCamera() {
        return this.camera;
    }

    Camera getEffectiveCamera() {
        if ((this.camera instanceof PerspectiveCamera) && !Platform.isSupported(ConditionalFeature.SCENE3D)) {
            if (this.defaultCamera == null) {
                this.defaultCamera = new ParallelCamera();
            }
            return this.defaultCamera;
        }
        return this.camera;
    }

    public void setCamera(Camera camera) {
        if ((camera instanceof PerspectiveCamera) && !Platform.isSupported(ConditionalFeature.SCENE3D)) {
            String logname = SnapshotParameters.class.getName();
            PlatformLogger.getLogger(logname).warning("System can't support ConditionalFeature.SCENE3D");
        }
        this.camera = camera;
    }

    public Transform getTransform() {
        return this.transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }

    public Paint getFill() {
        return this.fill;
    }

    public void setFill(Paint fill) {
        this.fill = fill;
    }

    public Rectangle2D getViewport() {
        return this.viewport;
    }

    public void setViewport(Rectangle2D viewport) {
        this.viewport = viewport;
    }

    SnapshotParameters copy() {
        SnapshotParameters params = new SnapshotParameters();
        params.camera = this.camera == null ? null : this.camera.copy();
        params.depthBuffer = this.depthBuffer;
        params.fill = this.fill;
        params.viewport = this.viewport;
        params.transform = this.transform == null ? null : this.transform.mo1183clone();
        return params;
    }
}
