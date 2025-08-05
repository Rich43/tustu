package javafx.scene;

import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGParallelCamera;

/* loaded from: jfxrt.jar:javafx/scene/ParallelCamera.class */
public class ParallelCamera extends Camera {
    @Override // javafx.scene.Camera
    Camera copy() {
        ParallelCamera c2 = new ParallelCamera();
        c2.setNearClip(getNearClip());
        c2.setFarClip(getFarClip());
        return c2;
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected NGNode impl_createPeer() {
        NGParallelCamera peer = new NGParallelCamera();
        peer.setNearClip((float) getNearClip());
        peer.setFarClip((float) getFarClip());
        return peer;
    }

    @Override // javafx.scene.Camera
    final PickRay computePickRay(double x2, double y2, PickRay pickRay) {
        return PickRay.computeParallelPickRay(x2, y2, getViewHeight(), getCameraTransform(), getNearClip(), getFarClip(), pickRay);
    }

    @Override // javafx.scene.Camera
    void computeProjectionTransform(GeneralTransform3D proj) {
        double viewWidth = getViewWidth();
        double viewHeight = getViewHeight();
        double halfDepth = viewWidth > viewHeight ? viewWidth / 2.0d : viewHeight / 2.0d;
        proj.ortho(0.0d, viewWidth, viewHeight, 0.0d, -halfDepth, halfDepth);
    }

    @Override // javafx.scene.Camera
    void computeViewTransform(Affine3D view) {
        view.setToIdentity();
    }

    @Override // javafx.scene.Camera
    Vec3d computePosition(Vec3d position) {
        if (position == null) {
            position = new Vec3d();
        }
        double halfViewWidth = getViewWidth() / 2.0d;
        double halfViewHeight = getViewHeight() / 2.0d;
        double distanceZ = halfViewHeight / Math.tan(Math.toRadians(15.0d));
        position.set(halfViewWidth, halfViewHeight, -distanceZ);
        return position;
    }
}
