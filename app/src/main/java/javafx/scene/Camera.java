package javafx.scene;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.BoxBounds;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.scene.CameraHelper;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGCamera;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.transform.Transform;
import sun.util.logging.PlatformLogger;

/* loaded from: jfxrt.jar:javafx/scene/Camera.class */
public abstract class Camera extends Node {
    private double farClipInScene;
    private double nearClipInScene;
    private DoubleProperty nearClip;
    private DoubleProperty farClip;
    private Affine3D localToSceneTx = new Affine3D();
    private Scene ownerScene = null;
    private SubScene ownerSubScene = null;
    private GeneralTransform3D projViewTx = new GeneralTransform3D();
    private GeneralTransform3D projTx = new GeneralTransform3D();
    private Affine3D viewTx = new Affine3D();
    private double viewWidth = 1.0d;
    private double viewHeight = 1.0d;
    private Vec3d position = new Vec3d();
    private boolean clipInSceneValid = false;
    private boolean projViewTxValid = false;
    private boolean localToSceneValid = false;
    private boolean sceneToLocalValid = false;
    private Affine3D sceneToLocalTx = new Affine3D();

    abstract void computeProjectionTransform(GeneralTransform3D generalTransform3D);

    abstract void computeViewTransform(Affine3D affine3D);

    abstract PickRay computePickRay(double d2, double d3, PickRay pickRay);

    abstract Vec3d computePosition(Vec3d vec3d);

    protected Camera() {
        InvalidationListener dirtyTransformListener = observable -> {
            impl_markDirty(DirtyBits.NODE_CAMERA_TRANSFORM);
        };
        localToSceneTransformProperty().addListener(dirtyTransformListener);
        sceneProperty().addListener(dirtyTransformListener);
    }

    double getFarClipInScene() {
        updateClipPlane();
        return this.farClipInScene;
    }

    double getNearClipInScene() {
        updateClipPlane();
        return this.nearClipInScene;
    }

    private void updateClipPlane() {
        if (!this.clipInSceneValid) {
            Transform localToSceneTransform = getLocalToSceneTransform();
            this.nearClipInScene = localToSceneTransform.transform(0.0d, 0.0d, getNearClip()).getZ();
            this.farClipInScene = localToSceneTransform.transform(0.0d, 0.0d, getFarClip()).getZ();
            this.clipInSceneValid = true;
        }
    }

    Affine3D getSceneToLocalTransform() {
        if (!this.sceneToLocalValid) {
            this.sceneToLocalTx.setTransform(getCameraTransform());
            try {
                this.sceneToLocalTx.invert();
            } catch (NoninvertibleTransformException ex) {
                String logname = Camera.class.getName();
                PlatformLogger.getLogger(logname).severe("getSceneToLocalTransform", ex);
                this.sceneToLocalTx.setToIdentity();
            }
            this.sceneToLocalValid = true;
        }
        return this.sceneToLocalTx;
    }

    public final void setNearClip(double value) {
        nearClipProperty().set(value);
    }

    public final double getNearClip() {
        if (this.nearClip == null) {
            return 0.1d;
        }
        return this.nearClip.get();
    }

    public final DoubleProperty nearClipProperty() {
        if (this.nearClip == null) {
            this.nearClip = new SimpleDoubleProperty(this, "nearClip", 0.1d) { // from class: javafx.scene.Camera.1
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    Camera.this.clipInSceneValid = false;
                    Camera.this.impl_markDirty(DirtyBits.NODE_CAMERA);
                }
            };
        }
        return this.nearClip;
    }

    public final void setFarClip(double value) {
        farClipProperty().set(value);
    }

    public final double getFarClip() {
        if (this.farClip == null) {
            return 100.0d;
        }
        return this.farClip.get();
    }

    public final DoubleProperty farClipProperty() {
        if (this.farClip == null) {
            this.farClip = new SimpleDoubleProperty(this, "farClip", 100.0d) { // from class: javafx.scene.Camera.2
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    Camera.this.clipInSceneValid = false;
                    Camera.this.impl_markDirty(DirtyBits.NODE_CAMERA);
                }
            };
        }
        return this.farClip;
    }

    Camera copy() {
        return this;
    }

    @Override // javafx.scene.Node
    @Deprecated
    public void impl_updatePeer() throws SecurityException {
        super.impl_updatePeer();
        NGCamera peer = (NGCamera) impl_getPeer();
        if (!impl_isDirtyEmpty()) {
            if (impl_isDirty(DirtyBits.NODE_CAMERA)) {
                peer.setNearClip((float) getNearClip());
                peer.setFarClip((float) getFarClip());
                peer.setViewWidth(getViewWidth());
                peer.setViewHeight(getViewHeight());
            }
            if (impl_isDirty(DirtyBits.NODE_CAMERA_TRANSFORM)) {
                peer.setWorldTransform(getCameraTransform());
            }
            peer.setProjViewTransform(getProjViewTransform());
            this.position = computePosition(this.position);
            getCameraTransform().transform(this.position, this.position);
            peer.setPosition(this.position);
        }
    }

    void setViewWidth(double width) {
        this.viewWidth = width;
        impl_markDirty(DirtyBits.NODE_CAMERA);
    }

    double getViewWidth() {
        return this.viewWidth;
    }

    void setViewHeight(double height) {
        this.viewHeight = height;
        impl_markDirty(DirtyBits.NODE_CAMERA);
    }

    double getViewHeight() {
        return this.viewHeight;
    }

    void setOwnerScene(Scene s2) {
        if (s2 == null) {
            this.ownerScene = null;
            return;
        }
        if (s2 != this.ownerScene) {
            if (this.ownerScene != null || this.ownerSubScene != null) {
                throw new IllegalArgumentException(((Object) this) + "is already set as camera in other scene or subscene");
            }
            this.ownerScene = s2;
            markOwnerDirty();
        }
    }

    void setOwnerSubScene(SubScene s2) {
        if (s2 == null) {
            this.ownerSubScene = null;
            return;
        }
        if (s2 != this.ownerSubScene) {
            if (this.ownerScene != null || this.ownerSubScene != null) {
                throw new IllegalArgumentException(((Object) this) + "is already set as camera in other scene or subscene");
            }
            this.ownerSubScene = s2;
            markOwnerDirty();
        }
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected void impl_markDirty(DirtyBits dirtyBit) {
        super.impl_markDirty(dirtyBit);
        if (dirtyBit == DirtyBits.NODE_CAMERA_TRANSFORM) {
            this.localToSceneValid = false;
            this.sceneToLocalValid = false;
            this.clipInSceneValid = false;
            this.projViewTxValid = false;
        } else if (dirtyBit == DirtyBits.NODE_CAMERA) {
            this.projViewTxValid = false;
        }
        markOwnerDirty();
    }

    private void markOwnerDirty() {
        if (this.ownerScene != null) {
            this.ownerScene.markCameraDirty();
        }
        if (this.ownerSubScene != null) {
            this.ownerSubScene.markContentDirty();
        }
    }

    Affine3D getCameraTransform() {
        if (!this.localToSceneValid) {
            this.localToSceneTx.setToIdentity();
            getLocalToSceneTransform().impl_apply(this.localToSceneTx);
            this.localToSceneValid = true;
        }
        return this.localToSceneTx;
    }

    GeneralTransform3D getProjViewTransform() {
        if (!this.projViewTxValid) {
            computeProjectionTransform(this.projTx);
            computeViewTransform(this.viewTx);
            this.projViewTx.set(this.projTx);
            this.projViewTx.mul(this.viewTx);
            this.projViewTx.mul(getSceneToLocalTransform());
            this.projViewTxValid = true;
        }
        return this.projViewTx;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Point2D project(Point3D p2) {
        Vec3d vec = getProjViewTransform().transform(new Vec3d(p2.getX(), p2.getY(), p2.getZ()));
        double halfViewWidth = getViewWidth() / 2.0d;
        double halfViewHeight = getViewHeight() / 2.0d;
        return new Point2D(halfViewWidth * (1.0d + vec.f11930x), halfViewHeight * (1.0d - vec.f11931y));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Point2D pickNodeXYPlane(Node node, double x2, double y2) {
        PickRay ray = computePickRay(x2, y2, null);
        Affine3D localToScene = new Affine3D();
        node.getLocalToSceneTransform().impl_apply(localToScene);
        Vec3d o2 = ray.getOriginNoClone();
        Vec3d d2 = ray.getDirectionNoClone();
        try {
            localToScene.inverseTransform(o2, o2);
            localToScene.inverseDeltaTransform(d2, d2);
            if (almostZero(d2.f11932z)) {
                return null;
            }
            double t2 = (-o2.f11932z) / d2.f11932z;
            return new Point2D(o2.f11930x + (d2.f11930x * t2), o2.f11931y + (d2.f11931y * t2));
        } catch (NoninvertibleTransformException e2) {
            return null;
        }
    }

    Point3D pickProjectPlane(double x2, double y2) {
        PickRay ray = computePickRay(x2, y2, null);
        Vec3d p2 = new Vec3d();
        p2.add(ray.getOriginNoClone(), ray.getDirectionNoClone());
        return new Point3D(p2.f11930x, p2.f11931y, p2.f11932z);
    }

    @Override // javafx.scene.Node
    @Deprecated
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        return new BoxBounds(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected boolean impl_computeContains(double localX, double localY) {
        return false;
    }

    @Override // javafx.scene.Node
    @Deprecated
    public Object impl_processMXNode(MXNodeAlgorithm alg, MXNodeAlgorithmContext ctx) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    static {
        CameraHelper.setCameraAccessor(new CameraHelper.CameraAccessor() { // from class: javafx.scene.Camera.3
            @Override // com.sun.javafx.scene.CameraHelper.CameraAccessor
            public Point2D project(Camera camera, Point3D p2) {
                return camera.project(p2);
            }

            @Override // com.sun.javafx.scene.CameraHelper.CameraAccessor
            public Point2D pickNodeXYPlane(Camera camera, Node node, double x2, double y2) {
                return camera.pickNodeXYPlane(node, x2, y2);
            }

            @Override // com.sun.javafx.scene.CameraHelper.CameraAccessor
            public Point3D pickProjectPlane(Camera camera, double x2, double y2) {
                return camera.pickProjectPlane(x2, y2);
            }
        });
    }
}
