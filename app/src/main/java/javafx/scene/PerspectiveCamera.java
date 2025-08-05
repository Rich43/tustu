package javafx.scene;

import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGPerspectiveCamera;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import sun.util.logging.PlatformLogger;

/* loaded from: jfxrt.jar:javafx/scene/PerspectiveCamera.class */
public class PerspectiveCamera extends Camera {
    private boolean fixedEyeAtCameraZero;
    private static final Affine3D LOOK_AT_TX = new Affine3D();
    private static final Affine3D LOOK_AT_TX_FIXED_EYE = new Affine3D();
    private DoubleProperty fieldOfView;
    private BooleanProperty verticalFieldOfView;

    static {
        LOOK_AT_TX.setToTranslation(0.0d, 0.0d, -1.0d);
        LOOK_AT_TX.rotate(3.141592653589793d, 1.0d, 0.0d, 0.0d);
        LOOK_AT_TX_FIXED_EYE.rotate(3.141592653589793d, 1.0d, 0.0d, 0.0d);
    }

    public final void setFieldOfView(double value) {
        fieldOfViewProperty().set(value);
    }

    public final double getFieldOfView() {
        if (this.fieldOfView == null) {
            return 30.0d;
        }
        return this.fieldOfView.get();
    }

    public final DoubleProperty fieldOfViewProperty() {
        if (this.fieldOfView == null) {
            this.fieldOfView = new SimpleDoubleProperty(this, "fieldOfView", 30.0d) { // from class: javafx.scene.PerspectiveCamera.1
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    PerspectiveCamera.this.impl_markDirty(DirtyBits.NODE_CAMERA);
                }
            };
        }
        return this.fieldOfView;
    }

    public final void setVerticalFieldOfView(boolean value) {
        verticalFieldOfViewProperty().set(value);
    }

    public final boolean isVerticalFieldOfView() {
        if (this.verticalFieldOfView == null) {
            return true;
        }
        return this.verticalFieldOfView.get();
    }

    public final BooleanProperty verticalFieldOfViewProperty() {
        if (this.verticalFieldOfView == null) {
            this.verticalFieldOfView = new SimpleBooleanProperty(this, "verticalFieldOfView", true) { // from class: javafx.scene.PerspectiveCamera.2
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    PerspectiveCamera.this.impl_markDirty(DirtyBits.NODE_CAMERA);
                }
            };
        }
        return this.verticalFieldOfView;
    }

    public PerspectiveCamera() {
        this(false);
    }

    public PerspectiveCamera(boolean fixedEyeAtCameraZero) {
        this.fixedEyeAtCameraZero = false;
        if (!Platform.isSupported(ConditionalFeature.SCENE3D)) {
            String logname = PerspectiveCamera.class.getName();
            PlatformLogger.getLogger(logname).warning("System can't support ConditionalFeature.SCENE3D");
        }
        this.fixedEyeAtCameraZero = fixedEyeAtCameraZero;
    }

    public final boolean isFixedEyeAtCameraZero() {
        return this.fixedEyeAtCameraZero;
    }

    @Override // javafx.scene.Camera
    final PickRay computePickRay(double x2, double y2, PickRay pickRay) {
        return PickRay.computePerspectivePickRay(x2, y2, this.fixedEyeAtCameraZero, getViewWidth(), getViewHeight(), Math.toRadians(getFieldOfView()), isVerticalFieldOfView(), getCameraTransform(), getNearClip(), getFarClip(), pickRay);
    }

    @Override // javafx.scene.Camera
    Camera copy() {
        PerspectiveCamera c2 = new PerspectiveCamera(this.fixedEyeAtCameraZero);
        c2.setNearClip(getNearClip());
        c2.setFarClip(getFarClip());
        c2.setFieldOfView(getFieldOfView());
        return c2;
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected NGNode impl_createPeer() {
        NGPerspectiveCamera peer = new NGPerspectiveCamera(this.fixedEyeAtCameraZero);
        peer.setNearClip((float) getNearClip());
        peer.setFarClip((float) getFarClip());
        peer.setFieldOfView((float) getFieldOfView());
        return peer;
    }

    @Override // javafx.scene.Camera, javafx.scene.Node
    @Deprecated
    public void impl_updatePeer() throws SecurityException {
        super.impl_updatePeer();
        NGPerspectiveCamera pgPerspectiveCamera = (NGPerspectiveCamera) impl_getPeer();
        if (impl_isDirty(DirtyBits.NODE_CAMERA)) {
            pgPerspectiveCamera.setVerticalFieldOfView(isVerticalFieldOfView());
            pgPerspectiveCamera.setFieldOfView((float) getFieldOfView());
        }
    }

    @Override // javafx.scene.Camera
    void computeProjectionTransform(GeneralTransform3D proj) {
        proj.perspective(isVerticalFieldOfView(), Math.toRadians(getFieldOfView()), getViewWidth() / getViewHeight(), getNearClip(), getFarClip());
    }

    @Override // javafx.scene.Camera
    void computeViewTransform(Affine3D view) {
        if (isFixedEyeAtCameraZero()) {
            view.setTransform(LOOK_AT_TX_FIXED_EYE);
            return;
        }
        double viewWidth = getViewWidth();
        double viewHeight = getViewHeight();
        boolean verticalFOV = isVerticalFieldOfView();
        double aspect = viewWidth / viewHeight;
        double tanOfHalfFOV = Math.tan(Math.toRadians(getFieldOfView()) / 2.0d);
        double xOffset = (-tanOfHalfFOV) * (verticalFOV ? aspect : 1.0d);
        double yOffset = tanOfHalfFOV * (verticalFOV ? 1.0d : 1.0d / aspect);
        double scale = (2.0d * tanOfHalfFOV) / (verticalFOV ? viewHeight : viewWidth);
        view.setToTranslation(xOffset, yOffset, 0.0d);
        view.concatenate(LOOK_AT_TX);
        view.scale(scale, scale, scale);
    }

    @Override // javafx.scene.Camera
    Vec3d computePosition(Vec3d position) {
        if (position == null) {
            position = new Vec3d();
        }
        if (this.fixedEyeAtCameraZero) {
            position.set(0.0d, 0.0d, 0.0d);
        } else {
            double halfViewWidth = getViewWidth() / 2.0d;
            double halfViewHeight = getViewHeight() / 2.0d;
            double halfViewDim = isVerticalFieldOfView() ? halfViewHeight : halfViewWidth;
            double distanceZ = halfViewDim / Math.tan(Math.toRadians(getFieldOfView() / 2.0d));
            position.set(halfViewWidth, halfViewHeight, -distanceZ);
        }
        return position;
    }
}
