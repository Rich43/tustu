package javafx.scene;

import com.sun.javafx.css.StyleManager;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.scene.CssFlags;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.scene.SubSceneHelper;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.SubSceneTraversalEngine;
import com.sun.javafx.scene.traversal.TopMostTraversalEngine;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGLightBase;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGSubScene;
import com.sun.javafx.tk.Toolkit;
import com.sun.media.jfxmedia.MetadataParser;
import java.util.ArrayList;
import java.util.List;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.WritableValue;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Point3D;
import javafx.scene.input.PickResult;
import javafx.scene.paint.Paint;
import sun.util.logging.PlatformLogger;

/* loaded from: jfxrt.jar:javafx/scene/SubScene.class */
public class SubScene extends Node {
    private static boolean is3DSupported = Platform.isSupported(ConditionalFeature.SCENE3D);
    private final SceneAntialiasing antiAliasing;
    private final boolean depthBuffer;
    private ObjectProperty<Parent> root;
    private ObjectProperty<Camera> camera;
    private Camera defaultCamera;
    private DoubleProperty width;
    private DoubleProperty height;
    private ObjectProperty<Paint> fill;
    private ObjectProperty<String> userAgentStylesheet;
    boolean dirtyLayout;
    private boolean dirtyNodes;
    private TopMostTraversalEngine traversalEngine;
    private int dirtyBits;
    private List<LightBase> lights;

    public SubScene(@NamedArg("root") Parent root, @NamedArg(MetadataParser.WIDTH_TAG_NAME) double width, @NamedArg(MetadataParser.HEIGHT_TAG_NAME) double height) {
        this(root, width, height, false, SceneAntialiasing.DISABLED);
    }

    public SubScene(@NamedArg("root") Parent root, @NamedArg(MetadataParser.WIDTH_TAG_NAME) double width, @NamedArg(MetadataParser.HEIGHT_TAG_NAME) double height, @NamedArg("depthBuffer") boolean depthBuffer, @NamedArg("antiAliasing") SceneAntialiasing antiAliasing) {
        this.userAgentStylesheet = null;
        this.dirtyLayout = false;
        this.dirtyNodes = false;
        this.traversalEngine = new SubSceneTraversalEngine(this);
        this.dirtyBits = -1;
        this.lights = new ArrayList();
        this.depthBuffer = depthBuffer;
        this.antiAliasing = antiAliasing;
        boolean isAntiAliasing = (antiAliasing == null || antiAliasing == SceneAntialiasing.DISABLED) ? false : true;
        setRoot(root);
        setWidth(width);
        setHeight(height);
        if ((depthBuffer || isAntiAliasing) && !is3DSupported) {
            String logname = SubScene.class.getName();
            PlatformLogger.getLogger(logname).warning("System can't support ConditionalFeature.SCENE3D");
        }
        if (isAntiAliasing && !Toolkit.getToolkit().isMSAASupported()) {
            String logname2 = SubScene.class.getName();
            PlatformLogger.getLogger(logname2).warning("System can't support antiAliasing");
        }
    }

    static {
        SubSceneHelper.setSubSceneAccessor(new SubSceneHelper.SubSceneAccessor() { // from class: javafx.scene.SubScene.7
            @Override // com.sun.javafx.scene.SubSceneHelper.SubSceneAccessor
            public boolean isDepthBuffer(SubScene subScene) {
                return subScene.isDepthBufferInternal();
            }

            @Override // com.sun.javafx.scene.SubSceneHelper.SubSceneAccessor
            public Camera getEffectiveCamera(SubScene subScene) {
                return subScene.getEffectiveCamera();
            }
        });
    }

    public final SceneAntialiasing getAntiAliasing() {
        return this.antiAliasing;
    }

    public final boolean isDepthBuffer() {
        return this.depthBuffer;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDepthBufferInternal() {
        if (is3DSupported) {
            return this.depthBuffer;
        }
        return false;
    }

    public final void setRoot(Parent value) {
        rootProperty().set(value);
    }

    public final Parent getRoot() {
        if (this.root == null) {
            return null;
        }
        return this.root.get();
    }

    public final ObjectProperty<Parent> rootProperty() {
        if (this.root == null) {
            this.root = new ObjectPropertyBase<Parent>() { // from class: javafx.scene.SubScene.1
                private Parent oldRoot;

                private void forceUnbind() {
                    System.err.println("Unbinding illegal root.");
                    unbind();
                }

                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Parent _value = get();
                    if (_value == null) {
                        if (isBound()) {
                            forceUnbind();
                        }
                        throw new NullPointerException("Scene's root cannot be null");
                    }
                    if (_value.getParent() != null) {
                        if (isBound()) {
                            forceUnbind();
                        }
                        throw new IllegalArgumentException(((Object) _value) + "is already inside a scene-graph and cannot be set as root");
                    }
                    if (_value.getClipParent() != null) {
                        if (isBound()) {
                            forceUnbind();
                        }
                        throw new IllegalArgumentException(((Object) _value) + "is set as a clip on another node, so cannot be set as root");
                    }
                    if ((_value.getScene() != null && _value.getScene().getRoot() == _value) || (_value.getSubScene() != null && _value.getSubScene().getRoot() == _value && _value.getSubScene() != SubScene.this)) {
                        if (isBound()) {
                            forceUnbind();
                        }
                        throw new IllegalArgumentException(((Object) _value) + "is already set as root of another scene or subScene");
                    }
                    _value.setTreeVisible(SubScene.this.impl_isTreeVisible());
                    _value.setDisabled(SubScene.this.isDisabled());
                    if (this.oldRoot != null) {
                        StyleManager.getInstance().forget(SubScene.this);
                        this.oldRoot.setScenes(null, null);
                    }
                    this.oldRoot = _value;
                    _value.getStyleClass().add(0, "root");
                    _value.setScenes(SubScene.this.getScene(), SubScene.this);
                    SubScene.this.markDirty(SubSceneDirtyBits.ROOT_SG_DIRTY);
                    _value.resize(SubScene.this.getWidth(), SubScene.this.getHeight());
                    _value.requestLayout();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return SubScene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "root";
                }
            };
        }
        return this.root;
    }

    public final void setCamera(Camera value) {
        cameraProperty().set(value);
    }

    public final Camera getCamera() {
        if (this.camera == null) {
            return null;
        }
        return this.camera.get();
    }

    public final ObjectProperty<Camera> cameraProperty() {
        if (this.camera == null) {
            this.camera = new ObjectPropertyBase<Camera>() { // from class: javafx.scene.SubScene.2
                Camera oldCamera = null;

                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Camera _value = get();
                    if (_value != null) {
                        if ((_value instanceof PerspectiveCamera) && !SubScene.is3DSupported) {
                            String logname = SubScene.class.getName();
                            PlatformLogger.getLogger(logname).warning("System can't support ConditionalFeature.SCENE3D");
                        }
                        if ((_value.getScene() != null || _value.getSubScene() != null) && (_value.getScene() != SubScene.this.getScene() || _value.getSubScene() != SubScene.this)) {
                            throw new IllegalArgumentException(((Object) _value) + "is already part of other scene or subscene");
                        }
                        _value.setOwnerSubScene(SubScene.this);
                        _value.setViewWidth(SubScene.this.getWidth());
                        _value.setViewHeight(SubScene.this.getHeight());
                    }
                    SubScene.this.markDirty(SubSceneDirtyBits.CAMERA_DIRTY);
                    if (this.oldCamera != null && this.oldCamera != _value) {
                        this.oldCamera.setOwnerSubScene(null);
                    }
                    this.oldCamera = _value;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return SubScene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "camera";
                }
            };
        }
        return this.camera;
    }

    Camera getEffectiveCamera() {
        Camera cam = getCamera();
        if (cam == null || ((cam instanceof PerspectiveCamera) && !is3DSupported)) {
            if (this.defaultCamera == null) {
                this.defaultCamera = new ParallelCamera();
                this.defaultCamera.setOwnerSubScene(this);
                this.defaultCamera.setViewWidth(getWidth());
                this.defaultCamera.setViewHeight(getHeight());
            }
            return this.defaultCamera;
        }
        return cam;
    }

    final void markContentDirty() {
        markDirty(SubSceneDirtyBits.CONTENT_DIRTY);
    }

    public final void setWidth(double value) {
        widthProperty().set(value);
    }

    public final double getWidth() {
        if (this.width == null) {
            return 0.0d;
        }
        return this.width.get();
    }

    public final DoubleProperty widthProperty() {
        if (this.width == null) {
            this.width = new DoublePropertyBase() { // from class: javafx.scene.SubScene.3
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Parent _root = SubScene.this.getRoot();
                    if (_root.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                        _root.impl_transformsChanged();
                    }
                    if (_root.isResizable()) {
                        _root.resize((get() - _root.getLayoutX()) - _root.getTranslateX(), _root.getLayoutBounds().getHeight());
                    }
                    SubScene.this.markDirty(SubSceneDirtyBits.SIZE_DIRTY);
                    SubScene.this.impl_geomChanged();
                    SubScene.this.getEffectiveCamera().setViewWidth(get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return SubScene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return MetadataParser.WIDTH_TAG_NAME;
                }
            };
        }
        return this.width;
    }

    public final void setHeight(double value) {
        heightProperty().set(value);
    }

    public final double getHeight() {
        if (this.height == null) {
            return 0.0d;
        }
        return this.height.get();
    }

    public final DoubleProperty heightProperty() {
        if (this.height == null) {
            this.height = new DoublePropertyBase() { // from class: javafx.scene.SubScene.4
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Parent _root = SubScene.this.getRoot();
                    if (_root.isResizable()) {
                        _root.resize(_root.getLayoutBounds().getWidth(), (get() - _root.getLayoutY()) - _root.getTranslateY());
                    }
                    SubScene.this.markDirty(SubSceneDirtyBits.SIZE_DIRTY);
                    SubScene.this.impl_geomChanged();
                    SubScene.this.getEffectiveCamera().setViewHeight(get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return SubScene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return MetadataParser.HEIGHT_TAG_NAME;
                }
            };
        }
        return this.height;
    }

    public final void setFill(Paint value) {
        fillProperty().set(value);
    }

    public final Paint getFill() {
        if (this.fill == null) {
            return null;
        }
        return this.fill.get();
    }

    public final ObjectProperty<Paint> fillProperty() {
        if (this.fill == null) {
            this.fill = new ObjectPropertyBase<Paint>(null) { // from class: javafx.scene.SubScene.5
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    SubScene.this.markDirty(SubSceneDirtyBits.FILL_DIRTY);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return SubScene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "fill";
                }
            };
        }
        return this.fill;
    }

    @Override // javafx.scene.Node
    @Deprecated
    public void impl_updatePeer() throws SecurityException {
        super.impl_updatePeer();
        this.dirtyNodes = false;
        if (isDirty()) {
            NGSubScene peer = (NGSubScene) impl_getPeer();
            Camera cam = getEffectiveCamera();
            boolean contentChanged = false;
            if (cam.getSubScene() == null && isDirty(SubSceneDirtyBits.CONTENT_DIRTY)) {
                cam.impl_syncPeer();
            }
            if (isDirty(SubSceneDirtyBits.FILL_DIRTY)) {
                Object platformPaint = getFill() == null ? null : Toolkit.getPaintAccessor().getPlatformPaint(getFill());
                peer.setFillPaint(platformPaint);
                contentChanged = true;
            }
            if (isDirty(SubSceneDirtyBits.SIZE_DIRTY)) {
                peer.setWidth((float) getWidth());
                peer.setHeight((float) getHeight());
            }
            if (isDirty(SubSceneDirtyBits.CAMERA_DIRTY)) {
                peer.setCamera((NGCamera) cam.impl_getPeer());
                contentChanged = true;
            }
            if (isDirty(SubSceneDirtyBits.ROOT_SG_DIRTY)) {
                peer.setRoot(getRoot().impl_getPeer());
                contentChanged = true;
            }
            if ((contentChanged | syncLights()) || isDirty(SubSceneDirtyBits.CONTENT_DIRTY)) {
                peer.markContentDirty();
            }
            clearDirtyBits();
        }
    }

    @Override // javafx.scene.Node
    void nodeResolvedOrientationChanged() {
        getRoot().parentResolvedOrientationInvalidated();
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected void impl_processCSS(WritableValue<Boolean> unused) {
        if (this.cssFlag == CssFlags.CLEAN) {
            return;
        }
        if (getRoot().cssFlag == CssFlags.CLEAN) {
            getRoot().cssFlag = this.cssFlag;
        }
        super.impl_processCSS(unused);
        getRoot().processCSS();
    }

    @Override // javafx.scene.Node
    void processCSS() {
        Parent root = getRoot();
        if (root.impl_isDirty(DirtyBits.NODE_CSS)) {
            root.impl_clearDirty(DirtyBits.NODE_CSS);
            if (this.cssFlag == CssFlags.CLEAN) {
                this.cssFlag = CssFlags.UPDATE;
            }
        }
        super.processCSS();
    }

    public final ObjectProperty<String> userAgentStylesheetProperty() {
        if (this.userAgentStylesheet == null) {
            this.userAgentStylesheet = new SimpleObjectProperty<String>(this, "userAgentStylesheet", null) { // from class: javafx.scene.SubScene.6
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    StyleManager.getInstance().forget(SubScene.this);
                    SubScene.this.impl_reapplyCSS();
                }
            };
        }
        return this.userAgentStylesheet;
    }

    public final String getUserAgentStylesheet() {
        if (this.userAgentStylesheet == null) {
            return null;
        }
        return this.userAgentStylesheet.get();
    }

    public final void setUserAgentStylesheet(String url) {
        userAgentStylesheetProperty().set(url);
    }

    @Override // javafx.scene.Node
    void updateBounds() {
        super.updateBounds();
        getRoot().updateBounds();
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected NGNode impl_createPeer() {
        if (!is3DSupported) {
            return new NGSubScene(false, false);
        }
        boolean aa2 = (this.antiAliasing == null || this.antiAliasing == SceneAntialiasing.DISABLED) ? false : true;
        return new NGSubScene(this.depthBuffer, aa2 && Toolkit.getToolkit().isMSAASupported());
    }

    @Override // javafx.scene.Node
    @Deprecated
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        int w2 = (int) Math.ceil(this.width.get());
        int h2 = (int) Math.ceil(this.height.get());
        BaseBounds bounds2 = bounds.deriveWithNewBounds(0.0f, 0.0f, 0.0f, w2, h2, 0.0f);
        return tx.transform(bounds2, bounds2);
    }

    void setDirtyLayout(Parent p2) {
        if (!this.dirtyLayout && p2 != null && p2.getSubScene() == this && getScene() != null) {
            this.dirtyLayout = true;
            markDirtyLayoutBranch();
            markDirty(SubSceneDirtyBits.CONTENT_DIRTY);
        }
    }

    void setDirty(Node n2) {
        if (!this.dirtyNodes && n2 != null && n2.getSubScene() == this && getScene() != null) {
            this.dirtyNodes = true;
            markDirty(SubSceneDirtyBits.CONTENT_DIRTY);
        }
    }

    void layoutPass() {
        if (this.dirtyLayout) {
            Parent r2 = getRoot();
            if (r2 != null) {
                r2.layout();
            }
            this.dirtyLayout = false;
        }
    }

    boolean traverse(Node node, Direction dir) {
        return this.traversalEngine.trav(node, dir) != null;
    }

    /* loaded from: jfxrt.jar:javafx/scene/SubScene$SubSceneDirtyBits.class */
    private enum SubSceneDirtyBits {
        SIZE_DIRTY,
        FILL_DIRTY,
        ROOT_SG_DIRTY,
        CAMERA_DIRTY,
        LIGHTS_DIRTY,
        CONTENT_DIRTY;

        private int mask = 1 << ordinal();

        SubSceneDirtyBits() {
        }

        public final int getMask() {
            return this.mask;
        }
    }

    private void clearDirtyBits() {
        this.dirtyBits = 0;
    }

    private boolean isDirty() {
        return this.dirtyBits != 0;
    }

    private void setDirty(SubSceneDirtyBits dirtyBit) {
        this.dirtyBits |= dirtyBit.getMask();
    }

    private boolean isDirty(SubSceneDirtyBits dirtyBit) {
        return (this.dirtyBits & dirtyBit.getMask()) != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void markDirty(SubSceneDirtyBits dirtyBit) {
        if (!isDirty()) {
            impl_markDirty(DirtyBits.NODE_CONTENTS);
        }
        setDirty(dirtyBit);
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected boolean impl_computeContains(double localX, double localY) {
        if (subSceneComputeContains(localX, localY)) {
            return true;
        }
        return getRoot().impl_computeContains(localX, localY);
    }

    private boolean subSceneComputeContains(double localX, double localY) {
        return localX >= 0.0d && localY >= 0.0d && localX <= getWidth() && localY <= getHeight() && getFill() != null;
    }

    private PickResult pickRootSG(double localX, double localY) {
        double viewWidth = getWidth();
        double viewHeight = getHeight();
        if (localX < 0.0d || localY < 0.0d || localX > viewWidth || localY > viewHeight) {
            return null;
        }
        PickResultChooser result = new PickResultChooser();
        PickRay pickRay = getEffectiveCamera().computePickRay(localX, localY, new PickRay());
        pickRay.getDirectionNoClone().normalize();
        getRoot().impl_pickNode(pickRay, result);
        return result.toPickResult();
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected void impl_pickNodeLocal(PickRay localPickRay, PickResultChooser result) {
        double boundsDistance = impl_intersectsBounds(localPickRay);
        if (!Double.isNaN(boundsDistance) && result.isCloser(boundsDistance)) {
            Point3D intersectPt = PickResultChooser.computePoint(localPickRay, boundsDistance);
            PickResult subSceneResult = pickRootSG(intersectPt.getX(), intersectPt.getY());
            if (subSceneResult != null) {
                result.offerSubScenePickResult(this, subSceneResult, boundsDistance);
            } else if (isPickOnBounds() || subSceneComputeContains(intersectPt.getX(), intersectPt.getY())) {
                result.offer(this, boundsDistance, intersectPt);
            }
        }
    }

    @Override // javafx.scene.Node
    @Deprecated
    public Object impl_processMXNode(MXNodeAlgorithm alg, MXNodeAlgorithmContext ctx) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    final void addLight(LightBase light) {
        if (!this.lights.contains(light)) {
            markDirty(SubSceneDirtyBits.LIGHTS_DIRTY);
            this.lights.add(light);
        }
    }

    final void removeLight(LightBase light) {
        if (this.lights.remove(light)) {
            markDirty(SubSceneDirtyBits.LIGHTS_DIRTY);
        }
    }

    private boolean syncLights() {
        boolean lightOwnerChanged = false;
        if (!isDirty(SubSceneDirtyBits.LIGHTS_DIRTY)) {
            return false;
        }
        NGSubScene pgSubScene = (NGSubScene) impl_getPeer();
        NGLightBase[] peerLights = pgSubScene.getLights();
        if (!this.lights.isEmpty() || peerLights != null) {
            if (this.lights.isEmpty()) {
                pgSubScene.setLights(null);
            } else {
                if (peerLights == null || peerLights.length < this.lights.size()) {
                    peerLights = new NGLightBase[this.lights.size()];
                }
                int i2 = 0;
                while (i2 < this.lights.size()) {
                    peerLights[i2] = (NGLightBase) this.lights.get(i2).impl_getPeer();
                    i2++;
                }
                while (i2 < peerLights.length && peerLights[i2] != null) {
                    int i3 = i2;
                    i2++;
                    peerLights[i3] = null;
                }
                pgSubScene.setLights(peerLights);
            }
            lightOwnerChanged = true;
        }
        return lightOwnerChanged;
    }
}
