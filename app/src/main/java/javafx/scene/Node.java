package javafx.scene;

import com.sun.glass.ui.Accessible;
import com.sun.glass.ui.Application;
import com.sun.javafx.beans.IDProperty;
import com.sun.javafx.beans.event.AbstractNotifyListener;
import com.sun.javafx.binding.ExpressionHelper;
import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.collections.UnmodifiableListSet;
import com.sun.javafx.css.PseudoClassState;
import com.sun.javafx.css.Selector;
import com.sun.javafx.css.Style;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.CursorConverter;
import com.sun.javafx.css.converters.EffectConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.effect.EffectDirtyBits;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.BoxBounds;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.javafx.geometry.BoundsUtils;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.perf.PerformanceTracker;
import com.sun.javafx.scene.BoundsAccessor;
import com.sun.javafx.scene.CameraHelper;
import com.sun.javafx.scene.CssFlags;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.scene.EventHandlerProperties;
import com.sun.javafx.scene.LayoutFlags;
import com.sun.javafx.scene.NodeEventDispatcher;
import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.scene.SceneHelper;
import com.sun.javafx.scene.SceneUtils;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.scene.transform.TransformUtils;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.util.Logging;
import com.sun.javafx.util.TempState;
import com.sun.javafx.util.Utils;
import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.prism.impl.PrismSettings;
import java.security.AccessControlContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectPropertyBase;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WritableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.css.CssMetaData;
import javafx.css.ParsedValue;
import javafx.css.PseudoClass;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.InputEvent;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.InputMethodRequests;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.input.RotateEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.input.ZoomEvent;
import javafx.scene.shape.Shape3D;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Window;
import javafx.util.Callback;
import sun.util.logging.PlatformLogger;

@IDProperty("id")
/* loaded from: jfxrt.jar:javafx/scene/Node.class */
public abstract class Node implements EventTarget, Styleable {
    private int dirtyBits;
    private static final Object USER_DATA_KEY;
    private ObservableMap<Object, Object> properties;
    private ReadOnlyObjectWrapper<Parent> parent;
    private StringProperty id;
    private StringProperty style;
    private BooleanProperty visible;
    private DoubleProperty opacity;
    private ObjectProperty<BlendMode> blendMode;
    private BooleanProperty pickOnBounds;
    private ReadOnlyBooleanWrapper disabled;
    private Node clipParent;
    private NGNode peer;
    private BooleanProperty managed;
    private DoubleProperty layoutX;
    private DoubleProperty layoutY;
    public static final double BASELINE_OFFSET_SAME_AS_HEIGHT = Double.NEGATIVE_INFINITY;
    boolean boundsChanged;
    private static final double EPSILON_ABSOLUTE = 1.0E-5d;
    private NodeTransformation nodeTransformation;
    private static final double DEFAULT_TRANSLATE_X = 0.0d;
    private static final double DEFAULT_TRANSLATE_Y = 0.0d;
    private static final double DEFAULT_TRANSLATE_Z = 0.0d;
    private static final double DEFAULT_SCALE_X = 1.0d;
    private static final double DEFAULT_SCALE_Y = 1.0d;
    private static final double DEFAULT_SCALE_Z = 1.0d;
    private static final double DEFAULT_ROTATE = 0.0d;
    private static final Point3D DEFAULT_ROTATION_AXIS;
    private EventHandlerProperties eventHandlerProperties;
    private ObjectProperty<NodeOrientation> nodeOrientation;
    private EffectiveOrientationProperty effectiveNodeOrientationProperty;
    private static final byte EFFECTIVE_ORIENTATION_LTR = 0;
    private static final byte EFFECTIVE_ORIENTATION_RTL = 1;
    private static final byte EFFECTIVE_ORIENTATION_MASK = 1;
    private static final byte AUTOMATIC_ORIENTATION_LTR = 0;
    private static final byte AUTOMATIC_ORIENTATION_RTL = 2;
    private static final byte AUTOMATIC_ORIENTATION_MASK = 2;
    private MiscProperties miscProperties;
    private static final boolean DEFAULT_CACHE = false;
    private static final CacheHint DEFAULT_CACHE_HINT;
    private static final Node DEFAULT_CLIP;
    private static final Cursor DEFAULT_CURSOR;
    private static final DepthTest DEFAULT_DEPTH_TEST;
    private static final boolean DEFAULT_DISABLE = false;
    private static final Effect DEFAULT_EFFECT;
    private static final InputMethodRequests DEFAULT_INPUT_METHOD_REQUESTS;
    private static final boolean DEFAULT_MOUSE_TRANSPARENT = false;
    private ReadOnlyBooleanWrapper hover;
    private ReadOnlyBooleanWrapper pressed;
    private FocusedProperty focused;
    private BooleanProperty focusTraversable;
    private boolean treeVisible;
    private TreeVisiblePropertyReadOnly treeVisibleRO;

    @Deprecated
    private BooleanProperty impl_showMnemonics;
    private ObjectProperty<EventDispatcher> eventDispatcher;
    private NodeEventDispatcher internalEventDispatcher;
    private EventDispatcher preprocessMouseEventDispatcher;
    CssStyleHelper styleHelper;
    private static final PseudoClass HOVER_PSEUDOCLASS_STATE;
    private static final PseudoClass PRESSED_PSEUDOCLASS_STATE;
    private static final PseudoClass DISABLED_PSEUDOCLASS_STATE;
    private static final PseudoClass FOCUSED_PSEUDOCLASS_STATE;
    private static final PseudoClass SHOW_MNEMONICS_PSEUDOCLASS_STATE;
    private static final BoundsAccessor boundsAccessor;
    private ObjectProperty<AccessibleRole> accessibleRole;
    AccessibilityProperties accessibilityProperties;
    Accessible accessible;
    private BaseBounds _geomBounds = new RectBounds(0.0f, 0.0f, -1.0f, -1.0f);
    private BaseBounds _txBounds = new RectBounds(0.0f, 0.0f, -1.0f, -1.0f);
    private boolean pendingUpdateBounds = false;
    private final InvalidationListener parentDisabledChangedListener = valueModel -> {
        updateDisabled();
    };
    private final InvalidationListener parentTreeVisibleChangedListener = valueModel -> {
        updateTreeVisible(true);
    };
    private SubScene subScene = null;
    private ReadOnlyObjectWrapperManualFire<Scene> scene = new ReadOnlyObjectWrapperManualFire<>();
    private ObservableList<String> styleClass = new TrackableObservableList<String>() { // from class: javafx.scene.Node.3
        @Override // com.sun.javafx.collections.TrackableObservableList
        protected void onChanged(ListChangeListener.Change<String> c2) {
            Node.this.impl_reapplyCSS();
        }

        @Override // java.util.AbstractCollection
        public String toString() {
            if (size() == 0) {
                return "";
            }
            if (size() == 1) {
                return get(0);
            }
            StringBuilder buf = new StringBuilder();
            for (int i2 = 0; i2 < size(); i2++) {
                buf.append(get(i2));
                if (i2 + 1 < size()) {
                    buf.append(' ');
                }
            }
            return buf.toString();
        }
    };
    private boolean derivedDepthTest = true;
    private LazyBoundsProperty layoutBounds = new LazyBoundsProperty() { // from class: javafx.scene.Node.12
        @Override // javafx.scene.Node.LazyBoundsProperty
        protected Bounds computeBounds() {
            return Node.this.impl_computeLayoutBounds();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Node.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "layoutBounds";
        }
    };
    private BaseTransform localToParentTx = BaseTransform.IDENTITY_TRANSFORM;
    private boolean transformDirty = true;
    private BaseBounds txBounds = new RectBounds();
    private BaseBounds geomBounds = new RectBounds();
    private BaseBounds localBounds = null;
    private boolean geomBoundsInvalid = true;
    private boolean localBoundsInvalid = true;
    private boolean txBoundsInvalid = true;
    private byte resolvedNodeOrientation = 0;
    private boolean canReceiveFocus = false;
    private Node labeledBy = null;
    CssFlags cssFlag = CssFlags.CLEAN;
    final ObservableSet<PseudoClass> pseudoClassStates = new PseudoClassState();

    @Deprecated
    protected abstract NGNode impl_createPeer();

    @Deprecated
    public abstract BaseBounds impl_computeGeomBounds(BaseBounds baseBounds, BaseTransform baseTransform);

    @Deprecated
    protected abstract boolean impl_computeContains(double d2, double d3);

    @Deprecated
    public abstract Object impl_processMXNode(MXNodeAlgorithm mXNodeAlgorithm, MXNodeAlgorithmContext mXNodeAlgorithmContext);

    static {
        PerformanceTracker.logEvent("Node class loaded");
        USER_DATA_KEY = new Object();
        DEFAULT_ROTATION_AXIS = Rotate.Z_AXIS;
        DEFAULT_CACHE_HINT = CacheHint.DEFAULT;
        DEFAULT_CLIP = null;
        DEFAULT_CURSOR = null;
        DEFAULT_DEPTH_TEST = DepthTest.INHERIT;
        DEFAULT_EFFECT = null;
        DEFAULT_INPUT_METHOD_REQUESTS = null;
        HOVER_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("hover");
        PRESSED_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("pressed");
        DISABLED_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("disabled");
        FOCUSED_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("focused");
        SHOW_MNEMONICS_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("show-mnemonics");
        boundsAccessor = (bounds, tx, node) -> {
            return node.getGeomBounds(bounds, tx);
        };
        NodeHelper.setNodeAccessor(new NodeHelper.NodeAccessor() { // from class: javafx.scene.Node.18
            @Override // com.sun.javafx.scene.NodeHelper.NodeAccessor
            public void layoutNodeForPrinting(Node node2) throws SecurityException {
                node2.doCSSLayoutSyncForSnapshot();
            }

            @Override // com.sun.javafx.scene.NodeHelper.NodeAccessor
            public boolean isDerivedDepthTest(Node node2) {
                return node2.isDerivedDepthTest();
            }

            @Override // com.sun.javafx.scene.NodeHelper.NodeAccessor
            public SubScene getSubScene(Node node2) {
                return node2.getSubScene();
            }

            @Override // com.sun.javafx.scene.NodeHelper.NodeAccessor
            public void setLabeledBy(Node node2, Node labeledBy) {
                node2.labeledBy = labeledBy;
            }

            @Override // com.sun.javafx.scene.NodeHelper.NodeAccessor
            public Accessible getAccessible(Node node2) {
                return node2.getAccessible();
            }

            @Override // com.sun.javafx.scene.NodeHelper.NodeAccessor
            public void recalculateRelativeSizeProperties(Node node2, Font fontForRelativeSizes) {
                node2.recalculateRelativeSizeProperties(fontForRelativeSizes);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Deprecated
    public void impl_markDirty(DirtyBits dirtyBit) {
        if (impl_isDirtyEmpty()) {
            addToSceneDirtyList();
        }
        this.dirtyBits = (int) (this.dirtyBits | dirtyBit.getMask());
    }

    private void addToSceneDirtyList() {
        Scene s2 = getScene();
        if (s2 != null) {
            s2.addToDirtyList(this);
            if (getSubScene() != null) {
                getSubScene().setDirty(this);
            }
        }
    }

    @Deprecated
    protected final boolean impl_isDirty(DirtyBits dirtyBit) {
        return (((long) this.dirtyBits) & dirtyBit.getMask()) != 0;
    }

    @Deprecated
    protected final void impl_clearDirty(DirtyBits dirtyBit) {
        this.dirtyBits = (int) (this.dirtyBits & (dirtyBit.getMask() ^ (-1)));
    }

    private void setDirty() {
        this.dirtyBits = -1;
    }

    private void clearDirty() {
        this.dirtyBits = 0;
    }

    @Deprecated
    protected final boolean impl_isDirtyEmpty() {
        return this.dirtyBits == 0;
    }

    @Deprecated
    public final void impl_syncPeer() throws SecurityException {
        if (!impl_isDirtyEmpty()) {
            if (this.treeVisible || impl_isDirty(DirtyBits.NODE_VISIBLE) || impl_isDirty(DirtyBits.NODE_FORCE_SYNC)) {
                impl_updatePeer();
                clearDirty();
            }
        }
    }

    void updateBounds() {
        Node n2 = getClip();
        if (n2 != null) {
            n2.updateBounds();
        }
        if (!this.treeVisible && !impl_isDirty(DirtyBits.NODE_VISIBLE)) {
            if (impl_isDirty(DirtyBits.NODE_TRANSFORM) || impl_isDirty(DirtyBits.NODE_TRANSFORMED_BOUNDS) || impl_isDirty(DirtyBits.NODE_BOUNDS)) {
                this.pendingUpdateBounds = true;
                return;
            }
            return;
        }
        if (this.pendingUpdateBounds) {
            impl_markDirty(DirtyBits.NODE_TRANSFORM);
            impl_markDirty(DirtyBits.NODE_TRANSFORMED_BOUNDS);
            impl_markDirty(DirtyBits.NODE_BOUNDS);
            this.pendingUpdateBounds = false;
        }
        if (impl_isDirty(DirtyBits.NODE_TRANSFORM) || impl_isDirty(DirtyBits.NODE_TRANSFORMED_BOUNDS)) {
            if (impl_isDirty(DirtyBits.NODE_TRANSFORM)) {
                updateLocalToParentTransform();
            }
            this._txBounds = getTransformedBounds(this._txBounds, BaseTransform.IDENTITY_TRANSFORM);
        }
        if (impl_isDirty(DirtyBits.NODE_BOUNDS)) {
            this._geomBounds = getGeomBounds(this._geomBounds, BaseTransform.IDENTITY_TRANSFORM);
        }
    }

    @Deprecated
    public void impl_updatePeer() throws SecurityException {
        NGNode peer = impl_getPeer();
        if (PrismSettings.printRenderGraph && impl_isDirty(DirtyBits.DEBUG)) {
            String id = getId();
            String className = getClass().getSimpleName();
            if (className.isEmpty()) {
                className = getClass().getName();
            }
            peer.setName(id == null ? className : id + "(" + className + ")");
        }
        if (impl_isDirty(DirtyBits.NODE_TRANSFORM)) {
            peer.setTransformMatrix(this.localToParentTx);
        }
        if (impl_isDirty(DirtyBits.NODE_BOUNDS)) {
            peer.setContentBounds(this._geomBounds);
        }
        if (impl_isDirty(DirtyBits.NODE_TRANSFORMED_BOUNDS)) {
            peer.setTransformedBounds(this._txBounds, !impl_isDirty(DirtyBits.NODE_BOUNDS));
        }
        if (impl_isDirty(DirtyBits.NODE_OPACITY)) {
            peer.setOpacity((float) Utils.clamp(0.0d, getOpacity(), 1.0d));
        }
        if (impl_isDirty(DirtyBits.NODE_CACHE)) {
            peer.setCachedAsBitmap(isCache(), getCacheHint());
        }
        if (impl_isDirty(DirtyBits.NODE_CLIP)) {
            peer.setClipNode(getClip() != null ? getClip().impl_getPeer() : null);
        }
        if (impl_isDirty(DirtyBits.EFFECT_EFFECT) && getEffect() != null) {
            getEffect().impl_sync();
            peer.effectChanged();
        }
        if (impl_isDirty(DirtyBits.NODE_EFFECT)) {
            peer.setEffect(getEffect() != null ? getEffect().impl_getImpl() : null);
        }
        if (impl_isDirty(DirtyBits.NODE_VISIBLE)) {
            peer.setVisible(isVisible());
        }
        if (impl_isDirty(DirtyBits.NODE_DEPTH_TEST)) {
            peer.setDepthTest(isDerivedDepthTest());
        }
        if (impl_isDirty(DirtyBits.NODE_BLENDMODE)) {
            BlendMode mode = getBlendMode();
            peer.setNodeBlendMode(mode == null ? null : Blend.impl_getToolkitMode(mode));
        }
    }

    public final ObservableMap<Object, Object> getProperties() {
        if (this.properties == null) {
            this.properties = FXCollections.observableMap(new HashMap());
        }
        return this.properties;
    }

    public boolean hasProperties() {
        return (this.properties == null || this.properties.isEmpty()) ? false : true;
    }

    public void setUserData(Object value) {
        getProperties().put(USER_DATA_KEY, value);
    }

    public Object getUserData() {
        return getProperties().get(USER_DATA_KEY);
    }

    final void setParent(Parent value) {
        parentPropertyImpl().set(value);
    }

    public final Parent getParent() {
        if (this.parent == null) {
            return null;
        }
        return this.parent.get();
    }

    public final ReadOnlyObjectProperty<Parent> parentProperty() {
        return parentPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyObjectWrapper<Parent> parentPropertyImpl() {
        if (this.parent == null) {
            this.parent = new ReadOnlyObjectWrapper<Parent>() { // from class: javafx.scene.Node.1
                private Parent oldParent;

                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    if (this.oldParent != null) {
                        this.oldParent.disabledProperty().removeListener(Node.this.parentDisabledChangedListener);
                        this.oldParent.impl_treeVisibleProperty().removeListener(Node.this.parentTreeVisibleChangedListener);
                        if (Node.this.nodeTransformation != null && Node.this.nodeTransformation.listenerReasons > 0) {
                            this.oldParent.localToSceneTransformProperty().removeListener(Node.this.nodeTransformation.getLocalToSceneInvalidationListener());
                        }
                    }
                    Node.this.updateDisabled();
                    Node.this.computeDerivedDepthTest();
                    Parent newParent = get();
                    if (newParent != null) {
                        newParent.disabledProperty().addListener(Node.this.parentDisabledChangedListener);
                        newParent.impl_treeVisibleProperty().addListener(Node.this.parentTreeVisibleChangedListener);
                        if (Node.this.nodeTransformation != null && Node.this.nodeTransformation.listenerReasons > 0) {
                            newParent.localToSceneTransformProperty().addListener(Node.this.nodeTransformation.getLocalToSceneInvalidationListener());
                        }
                        Node.this.impl_reapplyCSS();
                    } else {
                        Node.this.cssFlag = CssFlags.CLEAN;
                    }
                    Node.this.updateTreeVisible(true);
                    this.oldParent = newParent;
                    Node.this.invalidateLocalToSceneTransform();
                    Node.this.parentResolvedOrientationInvalidated();
                    Node.this.notifyAccessibleAttributeChanged(AccessibleAttribute.PARENT);
                }

                @Override // javafx.beans.property.SimpleObjectProperty, javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Node.this;
                }

                @Override // javafx.beans.property.SimpleObjectProperty, javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "parent";
                }
            };
        }
        return this.parent;
    }

    /* loaded from: jfxrt.jar:javafx/scene/Node$ReadOnlyObjectWrapperManualFire.class */
    private class ReadOnlyObjectWrapperManualFire<T> extends ReadOnlyObjectWrapper<T> {
        private ReadOnlyObjectWrapperManualFire() {
        }

        @Override // javafx.beans.property.SimpleObjectProperty, javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Node.this;
        }

        @Override // javafx.beans.property.SimpleObjectProperty, javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "scene";
        }

        @Override // javafx.beans.property.ReadOnlyObjectWrapper, javafx.beans.property.ObjectPropertyBase
        protected void fireValueChangedEvent() {
        }

        public void fireSuperValueChangedEvent() {
            super.fireValueChangedEvent();
        }
    }

    private void invalidatedScenes(Scene oldScene, SubScene oldSubScene) {
        Scene newScene = sceneProperty().get();
        boolean sceneChanged = oldScene != newScene;
        SubScene newSubScene = this.subScene;
        if (getClip() != null) {
            getClip().setScenes(newScene, newSubScene);
        }
        if (sceneChanged) {
            updateCanReceiveFocus();
            if (isFocusTraversable() && newScene != null) {
                newScene.initializeInternalEventDispatcher();
            }
            focusSetDirty(oldScene);
            focusSetDirty(newScene);
        }
        scenesChanged(newScene, newSubScene, oldScene, oldSubScene);
        if (sceneChanged) {
            impl_reapplyCSS();
        }
        if (sceneChanged && !impl_isDirtyEmpty()) {
            addToSceneDirtyList();
        }
        if (newScene == null && this.peer != null) {
            this.peer.release();
        }
        if (oldScene != null) {
            oldScene.clearNodeMnemonics(this);
        }
        if (getParent() == null) {
            parentResolvedOrientationInvalidated();
        }
        if (sceneChanged) {
            this.scene.fireSuperValueChangedEvent();
        }
        if (this.accessible != null) {
            if (oldScene != null && oldScene != newScene && newScene == null) {
                oldScene.addAccessible(this, this.accessible);
            } else {
                this.accessible.dispose();
            }
            this.accessible = null;
        }
    }

    final void setScenes(Scene newScene, SubScene newSubScene) {
        Scene oldScene = sceneProperty().get();
        if (newScene != oldScene || newSubScene != this.subScene) {
            this.scene.set(newScene);
            SubScene oldSubScene = this.subScene;
            this.subScene = newSubScene;
            invalidatedScenes(oldScene, oldSubScene);
            if (this instanceof SubScene) {
                SubScene thisSubScene = (SubScene) this;
                thisSubScene.getRoot().setScenes(newScene, thisSubScene);
            }
        }
    }

    final SubScene getSubScene() {
        return this.subScene;
    }

    public final Scene getScene() {
        return this.scene.get();
    }

    public final ReadOnlyObjectProperty<Scene> sceneProperty() {
        return this.scene.getReadOnlyProperty();
    }

    void scenesChanged(Scene newScene, SubScene newSubScene, Scene oldScene, SubScene oldSubScene) {
    }

    public final void setId(String value) {
        idProperty().set(value);
    }

    @Override // javafx.css.Styleable
    public final String getId() {
        if (this.id == null) {
            return null;
        }
        return this.id.get();
    }

    public final StringProperty idProperty() {
        if (this.id == null) {
            this.id = new StringPropertyBase() { // from class: javafx.scene.Node.2
                @Override // javafx.beans.property.StringPropertyBase
                protected void invalidated() {
                    Node.this.impl_reapplyCSS();
                    if (PrismSettings.printRenderGraph) {
                        Node.this.impl_markDirty(DirtyBits.DEBUG);
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Node.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "id";
                }
            };
        }
        return this.id;
    }

    @Override // javafx.css.Styleable
    public final ObservableList<String> getStyleClass() {
        return this.styleClass;
    }

    public final void setStyle(String value) {
        styleProperty().set(value);
    }

    @Override // javafx.css.Styleable
    public final String getStyle() {
        return this.style == null ? "" : this.style.get();
    }

    public final StringProperty styleProperty() {
        if (this.style == null) {
            this.style = new StringPropertyBase("") { // from class: javafx.scene.Node.4
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // javafx.beans.property.StringPropertyBase, javafx.beans.value.WritableObjectValue
                public void set(String value) {
                    super.set(value != null ? value : "");
                }

                @Override // javafx.beans.property.StringPropertyBase
                protected void invalidated() {
                    Node.this.impl_reapplyCSS();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Node.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return Constants.ATTRNAME_STYLE;
                }
            };
        }
        return this.style;
    }

    public final void setVisible(boolean value) {
        visibleProperty().set(value);
    }

    public final boolean isVisible() {
        if (this.visible == null) {
            return true;
        }
        return this.visible.get();
    }

    public final BooleanProperty visibleProperty() {
        if (this.visible == null) {
            this.visible = new StyleableBooleanProperty(true) { // from class: javafx.scene.Node.5
                boolean oldValue = true;

                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    if (this.oldValue != get()) {
                        Node.this.impl_markDirty(DirtyBits.NODE_VISIBLE);
                        Node.this.impl_geomChanged();
                        Node.this.updateTreeVisible(false);
                        if (Node.this.getParent() != null) {
                            Node.this.getParent().childVisibilityChanged(Node.this);
                        }
                        this.oldValue = get();
                    }
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                    return StyleableProperties.VISIBILITY;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Node.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "visible";
                }
            };
        }
        return this.visible;
    }

    public final void setCursor(Cursor value) {
        cursorProperty().set(value);
    }

    public final Cursor getCursor() {
        return this.miscProperties == null ? DEFAULT_CURSOR : this.miscProperties.getCursor();
    }

    public final ObjectProperty<Cursor> cursorProperty() {
        return getMiscProperties().cursorProperty();
    }

    public final void setOpacity(double value) {
        opacityProperty().set(value);
    }

    public final double getOpacity() {
        if (this.opacity == null) {
            return 1.0d;
        }
        return this.opacity.get();
    }

    public final DoubleProperty opacityProperty() {
        if (this.opacity == null) {
            this.opacity = new StyleableDoubleProperty(1.0d) { // from class: javafx.scene.Node.6
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Node.this.impl_markDirty(DirtyBits.NODE_OPACITY);
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.OPACITY;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Node.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "opacity";
                }
            };
        }
        return this.opacity;
    }

    public final void setBlendMode(BlendMode value) {
        blendModeProperty().set(value);
    }

    public final BlendMode getBlendMode() {
        if (this.blendMode == null) {
            return null;
        }
        return this.blendMode.get();
    }

    public final ObjectProperty<BlendMode> blendModeProperty() {
        if (this.blendMode == null) {
            this.blendMode = new StyleableObjectProperty<BlendMode>(null) { // from class: javafx.scene.Node.7
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    Node.this.impl_markDirty(DirtyBits.NODE_BLENDMODE);
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData getCssMetaData() {
                    return StyleableProperties.BLEND_MODE;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Node.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "blendMode";
                }
            };
        }
        return this.blendMode;
    }

    public final void setClip(Node value) {
        clipProperty().set(value);
    }

    public final Node getClip() {
        return this.miscProperties == null ? DEFAULT_CLIP : this.miscProperties.getClip();
    }

    public final ObjectProperty<Node> clipProperty() {
        return getMiscProperties().clipProperty();
    }

    public final void setCache(boolean value) {
        cacheProperty().set(value);
    }

    public final boolean isCache() {
        if (this.miscProperties == null) {
            return false;
        }
        return this.miscProperties.isCache();
    }

    public final BooleanProperty cacheProperty() {
        return getMiscProperties().cacheProperty();
    }

    public final void setCacheHint(CacheHint value) {
        cacheHintProperty().set(value);
    }

    public final CacheHint getCacheHint() {
        return this.miscProperties == null ? DEFAULT_CACHE_HINT : this.miscProperties.getCacheHint();
    }

    public final ObjectProperty<CacheHint> cacheHintProperty() {
        return getMiscProperties().cacheHintProperty();
    }

    public final void setEffect(Effect value) {
        effectProperty().set(value);
    }

    public final Effect getEffect() {
        return this.miscProperties == null ? DEFAULT_EFFECT : this.miscProperties.getEffect();
    }

    public final ObjectProperty<Effect> effectProperty() {
        return getMiscProperties().effectProperty();
    }

    public final void setDepthTest(DepthTest value) {
        depthTestProperty().set(value);
    }

    public final DepthTest getDepthTest() {
        return this.miscProperties == null ? DEFAULT_DEPTH_TEST : this.miscProperties.getDepthTest();
    }

    public final ObjectProperty<DepthTest> depthTestProperty() {
        return getMiscProperties().depthTestProperty();
    }

    void computeDerivedDepthTest() {
        boolean newDDT;
        if (getDepthTest() == DepthTest.INHERIT) {
            if (getParent() != null) {
                newDDT = getParent().isDerivedDepthTest();
            } else {
                newDDT = true;
            }
        } else if (getDepthTest() == DepthTest.ENABLE) {
            newDDT = true;
        } else {
            newDDT = false;
        }
        if (isDerivedDepthTest() != newDDT) {
            impl_markDirty(DirtyBits.NODE_DEPTH_TEST);
            setDerivedDepthTest(newDDT);
        }
    }

    void setDerivedDepthTest(boolean value) {
        this.derivedDepthTest = value;
    }

    boolean isDerivedDepthTest() {
        return this.derivedDepthTest;
    }

    public final void setDisable(boolean value) {
        disableProperty().set(value);
    }

    public final boolean isDisable() {
        if (this.miscProperties == null) {
            return false;
        }
        return this.miscProperties.isDisable();
    }

    public final BooleanProperty disableProperty() {
        return getMiscProperties().disableProperty();
    }

    public final void setPickOnBounds(boolean value) {
        pickOnBoundsProperty().set(value);
    }

    public final boolean isPickOnBounds() {
        if (this.pickOnBounds == null) {
            return false;
        }
        return this.pickOnBounds.get();
    }

    public final BooleanProperty pickOnBoundsProperty() {
        if (this.pickOnBounds == null) {
            this.pickOnBounds = new SimpleBooleanProperty(this, "pickOnBounds");
        }
        return this.pickOnBounds;
    }

    protected final void setDisabled(boolean value) {
        disabledPropertyImpl().set(value);
    }

    public final boolean isDisabled() {
        if (this.disabled == null) {
            return false;
        }
        return this.disabled.get();
    }

    public final ReadOnlyBooleanProperty disabledProperty() {
        return disabledPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyBooleanWrapper disabledPropertyImpl() {
        if (this.disabled == null) {
            this.disabled = new ReadOnlyBooleanWrapper() { // from class: javafx.scene.Node.8
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    Node.this.pseudoClassStateChanged(Node.DISABLED_PSEUDOCLASS_STATE, get());
                    Node.this.updateCanReceiveFocus();
                    Node.this.focusSetDirty(Node.this.getScene());
                }

                @Override // javafx.beans.property.SimpleBooleanProperty, javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Node.this;
                }

                @Override // javafx.beans.property.SimpleBooleanProperty, javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "disabled";
                }
            };
        }
        return this.disabled;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDisabled() {
        boolean zIsDisabled;
        boolean isDisabled = isDisable();
        if (!isDisabled) {
            if (getParent() != null) {
                zIsDisabled = getParent().isDisabled();
            } else {
                zIsDisabled = getSubScene() != null && getSubScene().isDisabled();
            }
            isDisabled = zIsDisabled;
        }
        setDisabled(isDisabled);
        if (this instanceof SubScene) {
            ((SubScene) this).getRoot().setDisabled(isDisabled);
        }
    }

    public Node lookup(String selector) {
        Selector s2;
        if (selector == null || (s2 = Selector.createSelector(selector)) == null || !s2.applies(this)) {
            return null;
        }
        return this;
    }

    public Set<Node> lookupAll(String selector) {
        List<Node> results;
        Selector s2 = Selector.createSelector(selector);
        Set<Node> empty = Collections.emptySet();
        if (s2 != null && (results = lookupAll(s2, null)) != null) {
            return new UnmodifiableListSet(results);
        }
        return empty;
    }

    List<Node> lookupAll(Selector selector, List<Node> results) {
        if (selector.applies(this)) {
            if (results == null) {
                results = new LinkedList();
            }
            results.add(this);
        }
        return results;
    }

    public void toBack() {
        if (getParent() != null) {
            getParent().impl_toBack(this);
        }
    }

    public void toFront() {
        if (getParent() != null) {
            getParent().impl_toFront(this);
        }
    }

    private void doCSSPass() {
        if (this.cssFlag != CssFlags.CLEAN) {
            processCSS();
        }
    }

    private static void syncAll(Node node) throws SecurityException {
        node.impl_syncPeer();
        if (node instanceof Parent) {
            Parent p2 = (Parent) node;
            int childrenCount = p2.getChildren().size();
            for (int i2 = 0; i2 < childrenCount; i2++) {
                Node n2 = p2.getChildren().get(i2);
                if (n2 != null) {
                    syncAll(n2);
                }
            }
        }
        if (node.getClip() != null) {
            syncAll(node.getClip());
        }
    }

    private void doLayoutPass() {
        if (this instanceof Parent) {
            Parent p2 = (Parent) this;
            for (int i2 = 0; i2 < 3; i2++) {
                p2.layout();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doCSSLayoutSyncForSnapshot() throws SecurityException {
        doCSSPass();
        doLayoutPass();
        updateBounds();
        Scene.impl_setAllowPGAccess(true);
        syncAll(this);
        Scene.impl_setAllowPGAccess(false);
    }

    private WritableImage doSnapshot(SnapshotParameters params, WritableImage img) throws SecurityException {
        double x2;
        double y2;
        double w2;
        double h2;
        if (getScene() != null) {
            getScene().doCSSLayoutSyncForSnapshot(this);
        } else {
            doCSSLayoutSyncForSnapshot();
        }
        BaseTransform transform = BaseTransform.IDENTITY_TRANSFORM;
        if (params.getTransform() != null) {
            Affine3D tempTx = new Affine3D();
            params.getTransform().impl_apply(tempTx);
            transform = tempTx;
        }
        Rectangle2D viewport = params.getViewport();
        if (viewport != null) {
            x2 = viewport.getMinX();
            y2 = viewport.getMinY();
            w2 = viewport.getWidth();
            h2 = viewport.getHeight();
        } else {
            BaseBounds tempBounds = getTransformedBounds(TempState.getInstance().bounds, transform);
            x2 = tempBounds.getMinX();
            y2 = tempBounds.getMinY();
            w2 = tempBounds.getWidth();
            h2 = tempBounds.getHeight();
        }
        WritableImage result = Scene.doSnapshot(getScene(), x2, y2, w2, h2, this, transform, params.isDepthBufferInternal(), params.getFill(), params.getEffectiveCamera(), img);
        return result;
    }

    public WritableImage snapshot(SnapshotParameters params, WritableImage image) {
        Toolkit.getToolkit().checkFxUserThread();
        if (params == null) {
            params = new SnapshotParameters();
            Scene s2 = getScene();
            if (s2 != null) {
                params.setCamera(s2.getEffectiveCamera());
                params.setDepthBuffer(s2.isDepthBufferInternal());
                params.setFill(s2.getFill());
            }
        }
        return doSnapshot(params, image);
    }

    public void snapshot(Callback<SnapshotResult, Void> callback, SnapshotParameters params, WritableImage image) {
        SnapshotParameters params2;
        Toolkit.getToolkit().checkFxUserThread();
        if (callback == null) {
            throw new NullPointerException("The callback must not be null");
        }
        if (params == null) {
            params2 = new SnapshotParameters();
            Scene s2 = getScene();
            if (s2 != null) {
                params2.setCamera(s2.getEffectiveCamera());
                params2.setDepthBuffer(s2.isDepthBufferInternal());
                params2.setFill(s2.getFill());
            }
        } else {
            params2 = params.copy();
        }
        SnapshotParameters theParams = params2;
        Runnable snapshotRunnable = () -> {
            WritableImage img = doSnapshot(theParams, image);
            SnapshotResult result = new SnapshotResult(img, this, theParams);
            try {
            } catch (Throwable th) {
                System.err.println("Exception in snapshot callback");
                th.printStackTrace(System.err);
            }
        };
        Scene.addSnapshotRunnable(snapshotRunnable);
    }

    public final void setOnDragEntered(EventHandler<? super DragEvent> value) {
        onDragEnteredProperty().set(value);
    }

    public final EventHandler<? super DragEvent> getOnDragEntered() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnDragEntered();
    }

    public final ObjectProperty<EventHandler<? super DragEvent>> onDragEnteredProperty() {
        return getEventHandlerProperties().onDragEnteredProperty();
    }

    public final void setOnDragExited(EventHandler<? super DragEvent> value) {
        onDragExitedProperty().set(value);
    }

    public final EventHandler<? super DragEvent> getOnDragExited() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnDragExited();
    }

    public final ObjectProperty<EventHandler<? super DragEvent>> onDragExitedProperty() {
        return getEventHandlerProperties().onDragExitedProperty();
    }

    public final void setOnDragOver(EventHandler<? super DragEvent> value) {
        onDragOverProperty().set(value);
    }

    public final EventHandler<? super DragEvent> getOnDragOver() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnDragOver();
    }

    public final ObjectProperty<EventHandler<? super DragEvent>> onDragOverProperty() {
        return getEventHandlerProperties().onDragOverProperty();
    }

    public final void setOnDragDropped(EventHandler<? super DragEvent> value) {
        onDragDroppedProperty().set(value);
    }

    public final EventHandler<? super DragEvent> getOnDragDropped() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnDragDropped();
    }

    public final ObjectProperty<EventHandler<? super DragEvent>> onDragDroppedProperty() {
        return getEventHandlerProperties().onDragDroppedProperty();
    }

    public final void setOnDragDone(EventHandler<? super DragEvent> value) {
        onDragDoneProperty().set(value);
    }

    public final EventHandler<? super DragEvent> getOnDragDone() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnDragDone();
    }

    public final ObjectProperty<EventHandler<? super DragEvent>> onDragDoneProperty() {
        return getEventHandlerProperties().onDragDoneProperty();
    }

    public Dragboard startDragAndDrop(TransferMode... transferModes) {
        if (getScene() != null) {
            return getScene().startDragAndDrop(this, transferModes);
        }
        throw new IllegalStateException("Cannot start drag and drop on node that is not in scene");
    }

    public void startFullDrag() {
        if (getScene() != null) {
            getScene().startFullDrag(this);
            return;
        }
        throw new IllegalStateException("Cannot start full drag on node that is not in scene");
    }

    final Node getClipParent() {
        return this.clipParent;
    }

    boolean isConnected() {
        return (getParent() == null && this.clipParent == null) ? false : true;
    }

    boolean wouldCreateCycle(Node parent, Node child) {
        if (child != null && child.getClip() == null && !(child instanceof Parent)) {
            return false;
        }
        Node parent2 = parent;
        while (true) {
            Node n2 = parent2;
            if (n2 != child) {
                if (n2.getParent() != null) {
                    parent2 = n2.getParent();
                } else if (n2.getSubScene() != null) {
                    parent2 = n2.getSubScene();
                } else if (n2.clipParent != null) {
                    parent2 = n2.clipParent;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    @Deprecated
    public <P extends NGNode> P impl_getPeer() {
        if (Utils.assertionEnabled() && getScene() != null && !Scene.isPGAccessAllowed()) {
            System.err.println();
            System.err.println("*** unexpected PG access");
            Thread.dumpStack();
        }
        if (this.peer == null) {
            this.peer = impl_createPeer();
        }
        return (P) this.peer;
    }

    protected Node() {
        setDirty();
        updateTreeVisible(false);
    }

    public final void setManaged(boolean value) {
        managedProperty().set(value);
    }

    public final boolean isManaged() {
        if (this.managed == null) {
            return true;
        }
        return this.managed.get();
    }

    public final BooleanProperty managedProperty() {
        if (this.managed == null) {
            this.managed = new BooleanPropertyBase(true) { // from class: javafx.scene.Node.9
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    Parent parent = Node.this.getParent();
                    if (parent != null) {
                        parent.managedChildChanged();
                    }
                    Node.this.notifyManagedChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Node.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "managed";
                }
            };
        }
        return this.managed;
    }

    void notifyManagedChanged() {
    }

    public final void setLayoutX(double value) {
        layoutXProperty().set(value);
    }

    public final double getLayoutX() {
        if (this.layoutX == null) {
            return 0.0d;
        }
        return this.layoutX.get();
    }

    public final DoubleProperty layoutXProperty() {
        if (this.layoutX == null) {
            this.layoutX = new DoublePropertyBase(0.0d) { // from class: javafx.scene.Node.10
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    Node.this.impl_transformsChanged();
                    Parent p2 = Node.this.getParent();
                    if (p2 != null && !p2.performingLayout) {
                        if (Node.this.isManaged()) {
                            p2.requestLayout();
                        } else {
                            p2.clearSizeCache();
                            p2.requestParentLayout();
                        }
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Node.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "layoutX";
                }
            };
        }
        return this.layoutX;
    }

    public final void setLayoutY(double value) {
        layoutYProperty().set(value);
    }

    public final double getLayoutY() {
        if (this.layoutY == null) {
            return 0.0d;
        }
        return this.layoutY.get();
    }

    public final DoubleProperty layoutYProperty() {
        if (this.layoutY == null) {
            this.layoutY = new DoublePropertyBase(0.0d) { // from class: javafx.scene.Node.11
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    Node.this.impl_transformsChanged();
                    Parent p2 = Node.this.getParent();
                    if (p2 != null && !p2.performingLayout) {
                        if (Node.this.isManaged()) {
                            p2.requestLayout();
                        } else {
                            p2.clearSizeCache();
                            p2.requestParentLayout();
                        }
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Node.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "layoutY";
                }
            };
        }
        return this.layoutY;
    }

    public void relocate(double x2, double y2) {
        setLayoutX(x2 - getLayoutBounds().getMinX());
        setLayoutY(y2 - getLayoutBounds().getMinY());
        PlatformLogger logger = Logging.getLayoutLogger();
        if (logger.isLoggable(PlatformLogger.Level.FINER)) {
            logger.finer(toString() + " moved to (" + x2 + "," + y2 + ")");
        }
    }

    public boolean isResizable() {
        return false;
    }

    public Orientation getContentBias() {
        return null;
    }

    public double minWidth(double height) {
        return prefWidth(height);
    }

    public double minHeight(double width) {
        return prefHeight(width);
    }

    public double prefWidth(double height) {
        double result = getLayoutBounds().getWidth();
        if (Double.isNaN(result) || result < 0.0d) {
            return 0.0d;
        }
        return result;
    }

    public double prefHeight(double width) {
        double result = getLayoutBounds().getHeight();
        if (Double.isNaN(result) || result < 0.0d) {
            return 0.0d;
        }
        return result;
    }

    public double maxWidth(double height) {
        return prefWidth(height);
    }

    public double maxHeight(double width) {
        return prefHeight(width);
    }

    public void resize(double width, double height) {
    }

    public final void autosize() {
        double h2;
        double w2;
        if (isResizable()) {
            Orientation contentBias = getContentBias();
            if (contentBias == null) {
                w2 = boundedSize(prefWidth(-1.0d), minWidth(-1.0d), maxWidth(-1.0d));
                h2 = boundedSize(prefHeight(-1.0d), minHeight(-1.0d), maxHeight(-1.0d));
            } else if (contentBias == Orientation.HORIZONTAL) {
                w2 = boundedSize(prefWidth(-1.0d), minWidth(-1.0d), maxWidth(-1.0d));
                h2 = boundedSize(prefHeight(w2), minHeight(w2), maxHeight(w2));
            } else {
                h2 = boundedSize(prefHeight(-1.0d), minHeight(-1.0d), maxHeight(-1.0d));
                w2 = boundedSize(prefWidth(h2), minWidth(h2), maxWidth(h2));
            }
            resize(w2, h2);
        }
    }

    double boundedSize(double value, double min, double max) {
        return Math.min(Math.max(value, min), Math.max(min, max));
    }

    public void resizeRelocate(double x2, double y2, double width, double height) {
        resize(width, height);
        relocate(x2, y2);
    }

    public double getBaselineOffset() {
        if (isResizable()) {
            return Double.NEGATIVE_INFINITY;
        }
        return getLayoutBounds().getHeight();
    }

    public double computeAreaInScreen() {
        return impl_computeAreaInScreen();
    }

    private double impl_computeAreaInScreen() {
        double minZ;
        double maxZ;
        Scene tmpScene = getScene();
        if (tmpScene != null) {
            Bounds bounds = getBoundsInLocal();
            Camera camera = tmpScene.getEffectiveCamera();
            boolean isPerspective = camera instanceof PerspectiveCamera;
            Transform localToSceneTx = getLocalToSceneTransform();
            Affine3D tempTx = TempState.getInstance().tempTx;
            BaseBounds localBounds = new BoxBounds((float) bounds.getMinX(), (float) bounds.getMinY(), (float) bounds.getMinZ(), (float) bounds.getMaxX(), (float) bounds.getMaxY(), (float) bounds.getMaxZ());
            if (isPerspective) {
                Transform cameraL2STx = camera.getLocalToSceneTransform();
                if (cameraL2STx.getMxx() == 1.0d && cameraL2STx.getMxy() == 0.0d && cameraL2STx.getMxz() == 0.0d && cameraL2STx.getMyx() == 0.0d && cameraL2STx.getMyy() == 1.0d && cameraL2STx.getMyz() == 0.0d && cameraL2STx.getMzx() == 0.0d && cameraL2STx.getMzy() == 0.0d && cameraL2STx.getMzz() == 1.0d) {
                    if (localToSceneTx.getMxx() == 1.0d && localToSceneTx.getMxy() == 0.0d && localToSceneTx.getMxz() == 0.0d && localToSceneTx.getMyx() == 0.0d && localToSceneTx.getMyy() == 1.0d && localToSceneTx.getMyz() == 0.0d && localToSceneTx.getMzx() == 0.0d && localToSceneTx.getMzy() == 0.0d && localToSceneTx.getMzz() == 1.0d) {
                        Vec3d tempV3D = TempState.getInstance().vec3d;
                        tempV3D.set(0.0d, 0.0d, bounds.getMinZ());
                        localToScene(tempV3D);
                        minZ = tempV3D.f11932z;
                        tempV3D.set(0.0d, 0.0d, bounds.getMaxZ());
                        localToScene(tempV3D);
                        maxZ = tempV3D.f11932z;
                    } else {
                        Bounds nodeInSceneBounds = localToScene(bounds);
                        minZ = nodeInSceneBounds.getMinZ();
                        maxZ = nodeInSceneBounds.getMaxZ();
                    }
                    if (minZ > camera.getFarClipInScene() || maxZ < camera.getNearClipInScene()) {
                        return 0.0d;
                    }
                } else {
                    BaseBounds nodeInCameraBounds = new BoxBounds();
                    tempTx.setToIdentity();
                    localToSceneTx.impl_apply(tempTx);
                    tempTx.preConcatenate(camera.getSceneToLocalTransform());
                    tempTx.transform(localBounds, nodeInCameraBounds);
                    if (nodeInCameraBounds.getMinZ() > camera.getFarClip() || nodeInCameraBounds.getMaxZ() < camera.getNearClip()) {
                        return 0.0d;
                    }
                }
            }
            GeneralTransform3D projViewTx = TempState.getInstance().projViewTx;
            projViewTx.set(camera.getProjViewTransform());
            tempTx.setToIdentity();
            localToSceneTx.impl_apply(tempTx);
            GeneralTransform3D tx = projViewTx.mul(tempTx);
            BaseBounds localBounds2 = tx.transform(localBounds, localBounds);
            double area = localBounds2.getWidth() * localBounds2.getHeight();
            if (isPerspective) {
                localBounds2.intersectWith(-1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                area = (localBounds2.getWidth() < 0.0f || localBounds2.getHeight() < 0.0f) ? 0.0d : area;
            }
            return area * (((camera.getViewWidth() / 2.0d) * camera.getViewHeight()) / 2.0d);
        }
        return 0.0d;
    }

    public final Bounds getBoundsInParent() {
        return boundsInParentProperty().get();
    }

    public final ReadOnlyObjectProperty<Bounds> boundsInParentProperty() {
        return getMiscProperties().boundsInParentProperty();
    }

    private void invalidateBoundsInParent() {
        if (this.miscProperties != null) {
            this.miscProperties.invalidateBoundsInParent();
        }
    }

    public final Bounds getBoundsInLocal() {
        return boundsInLocalProperty().get();
    }

    public final ReadOnlyObjectProperty<Bounds> boundsInLocalProperty() {
        return getMiscProperties().boundsInLocalProperty();
    }

    private void invalidateBoundsInLocal() {
        if (this.miscProperties != null) {
            this.miscProperties.invalidateBoundsInLocal();
        }
    }

    public final Bounds getLayoutBounds() {
        return layoutBoundsProperty().get();
    }

    public final ReadOnlyObjectProperty<Bounds> layoutBoundsProperty() {
        return this.layoutBounds;
    }

    @Deprecated
    protected Bounds impl_computeLayoutBounds() {
        BaseBounds tempBounds = getGeomBounds(TempState.getInstance().bounds, BaseTransform.IDENTITY_TRANSFORM);
        return new BoundingBox(tempBounds.getMinX(), tempBounds.getMinY(), tempBounds.getMinZ(), tempBounds.getWidth(), tempBounds.getHeight(), tempBounds.getDepth());
    }

    @Deprecated
    protected final void impl_layoutBoundsChanged() {
        if (!this.layoutBounds.valid) {
            return;
        }
        this.layoutBounds.invalidate();
        if ((this.nodeTransformation != null && this.nodeTransformation.hasScaleOrRotate()) || hasMirroring()) {
            impl_transformsChanged();
        }
    }

    BaseBounds getTransformedBounds(BaseBounds bounds, BaseTransform tx) {
        updateLocalToParentTransform();
        if (tx.isTranslateOrIdentity()) {
            updateTxBounds();
            BaseBounds bounds2 = bounds.deriveWithNewBounds(this.txBounds);
            if (!tx.isIdentity()) {
                double translateX = tx.getMxt();
                double translateY = tx.getMyt();
                double translateZ = tx.getMzt();
                bounds2 = bounds2.deriveWithNewBounds((float) (bounds2.getMinX() + translateX), (float) (bounds2.getMinY() + translateY), (float) (bounds2.getMinZ() + translateZ), (float) (bounds2.getMaxX() + translateX), (float) (bounds2.getMaxY() + translateY), (float) (bounds2.getMaxZ() + translateZ));
            }
            return bounds2;
        }
        if (this.localToParentTx.isIdentity()) {
            return getLocalBounds(bounds, tx);
        }
        double mxx = tx.getMxx();
        double mxy = tx.getMxy();
        double mxz = tx.getMxz();
        double mxt = tx.getMxt();
        double myx = tx.getMyx();
        double myy = tx.getMyy();
        double myz = tx.getMyz();
        double myt = tx.getMyt();
        double mzx = tx.getMzx();
        double mzy = tx.getMzy();
        double mzz = tx.getMzz();
        double mzt = tx.getMzt();
        BaseTransform boundsTx = tx.deriveWithConcatenation(this.localToParentTx);
        BaseBounds bounds3 = getLocalBounds(bounds, boundsTx);
        if (boundsTx == tx) {
            tx.restoreTransform(mxx, mxy, mxz, mxt, myx, myy, myz, myt, mzx, mzy, mzz, mzt);
        }
        return bounds3;
    }

    BaseBounds getLocalBounds(BaseBounds bounds, BaseTransform tx) {
        if (getEffect() == null && getClip() == null) {
            return getGeomBounds(bounds, tx);
        }
        if (tx.isTranslateOrIdentity()) {
            updateLocalBounds();
            BaseBounds bounds2 = bounds.deriveWithNewBounds(this.localBounds);
            if (!tx.isIdentity()) {
                double translateX = tx.getMxt();
                double translateY = tx.getMyt();
                double translateZ = tx.getMzt();
                bounds2 = bounds2.deriveWithNewBounds((float) (bounds2.getMinX() + translateX), (float) (bounds2.getMinY() + translateY), (float) (bounds2.getMinZ() + translateZ), (float) (bounds2.getMaxX() + translateX), (float) (bounds2.getMaxY() + translateY), (float) (bounds2.getMaxZ() + translateZ));
            }
            return bounds2;
        }
        if (tx.is2D() && (tx.getType() & (-76)) != 0) {
            return computeLocalBounds(bounds, tx);
        }
        updateLocalBounds();
        return tx.transform(this.localBounds, bounds);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BaseBounds getGeomBounds(BaseBounds bounds, BaseTransform tx) {
        if (tx.isTranslateOrIdentity()) {
            updateGeomBounds();
            BaseBounds bounds2 = bounds.deriveWithNewBounds(this.geomBounds);
            if (!tx.isIdentity()) {
                double translateX = tx.getMxt();
                double translateY = tx.getMyt();
                double translateZ = tx.getMzt();
                bounds2 = bounds2.deriveWithNewBounds((float) (bounds2.getMinX() + translateX), (float) (bounds2.getMinY() + translateY), (float) (bounds2.getMinZ() + translateZ), (float) (bounds2.getMaxX() + translateX), (float) (bounds2.getMaxY() + translateY), (float) (bounds2.getMaxZ() + translateZ));
            }
            return bounds2;
        }
        if (tx.is2D() && (tx.getType() & (-76)) != 0) {
            return impl_computeGeomBounds(bounds, tx);
        }
        updateGeomBounds();
        return tx.transform(this.geomBounds, bounds);
    }

    void updateGeomBounds() {
        if (this.geomBoundsInvalid) {
            this.geomBounds = impl_computeGeomBounds(this.geomBounds, BaseTransform.IDENTITY_TRANSFORM);
            this.geomBoundsInvalid = false;
        }
    }

    private BaseBounds computeLocalBounds(BaseBounds bounds, BaseTransform tx) {
        BaseBounds bounds2;
        if (getEffect() != null) {
            BaseBounds b2 = getEffect().impl_getBounds(bounds, tx, this, boundsAccessor);
            bounds2 = bounds.deriveWithNewBounds(b2);
        } else {
            bounds2 = getGeomBounds(bounds, tx);
        }
        if (getClip() != null && !(this instanceof Shape3D) && !(getClip() instanceof Shape3D)) {
            double x1 = bounds2.getMinX();
            double y1 = bounds2.getMinY();
            double x2 = bounds2.getMaxX();
            double y2 = bounds2.getMaxY();
            double z1 = bounds2.getMinZ();
            double z2 = bounds2.getMaxZ();
            bounds2 = getClip().getTransformedBounds(bounds2, tx);
            bounds2.intersectWith((float) x1, (float) y1, (float) z1, (float) x2, (float) y2, (float) z2);
        }
        return bounds2;
    }

    private void updateLocalBounds() {
        if (this.localBoundsInvalid) {
            if (getClip() != null || getEffect() != null) {
                this.localBounds = computeLocalBounds(this.localBounds == null ? new RectBounds() : this.localBounds, BaseTransform.IDENTITY_TRANSFORM);
            } else {
                this.localBounds = null;
            }
            this.localBoundsInvalid = false;
        }
    }

    void updateTxBounds() {
        if (this.txBoundsInvalid) {
            updateLocalToParentTransform();
            this.txBounds = getLocalBounds(this.txBounds, this.localToParentTx);
            this.txBoundsInvalid = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Deprecated
    public void impl_geomChanged() {
        if (this.geomBoundsInvalid) {
            impl_notifyLayoutBoundsChanged();
            transformedBoundsChanged();
            return;
        }
        this.geomBounds.makeEmpty();
        this.geomBoundsInvalid = true;
        impl_markDirty(DirtyBits.NODE_BOUNDS);
        impl_notifyLayoutBoundsChanged();
        localBoundsChanged();
    }

    void localBoundsChanged() {
        this.localBoundsInvalid = true;
        invalidateBoundsInLocal();
        transformedBoundsChanged();
    }

    void transformedBoundsChanged() {
        if (!this.txBoundsInvalid) {
            this.txBounds.makeEmpty();
            this.txBoundsInvalid = true;
            invalidateBoundsInParent();
            impl_markDirty(DirtyBits.NODE_TRANSFORMED_BOUNDS);
        }
        if (isVisible()) {
            notifyParentOfBoundsChange();
        }
    }

    @Deprecated
    protected void impl_notifyLayoutBoundsChanged() {
        impl_layoutBoundsChanged();
        Parent p2 = getParent();
        if (!isManaged() || p2 == null) {
            return;
        }
        if ((!(p2 instanceof Group) || isResizable()) && !p2.performingLayout) {
            p2.requestLayout();
        }
    }

    void notifyParentOfBoundsChange() {
        Parent p2 = getParent();
        if (p2 != null) {
            p2.childBoundsChanged(this);
        }
        if (this.clipParent != null) {
            this.clipParent.localBoundsChanged();
        }
    }

    public boolean contains(double localX, double localY) {
        if (containsBounds(localX, localY)) {
            return isPickOnBounds() || impl_computeContains(localX, localY);
        }
        return false;
    }

    @Deprecated
    protected boolean containsBounds(double localX, double localY) {
        TempState tempState = TempState.getInstance();
        BaseBounds tempBounds = tempState.bounds;
        if (getLocalBounds(tempBounds, BaseTransform.IDENTITY_TRANSFORM).contains((float) localX, (float) localY)) {
            if (getClip() != null) {
                tempState.point.f11907x = (float) localX;
                tempState.point.f11908y = (float) localY;
                try {
                    getClip().parentToLocal(tempState.point);
                    if (!getClip().contains(tempState.point.f11907x, tempState.point.f11908y)) {
                        return false;
                    }
                    return true;
                } catch (NoninvertibleTransformException e2) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean contains(Point2D localPoint) {
        return contains(localPoint.getX(), localPoint.getY());
    }

    public boolean intersects(double localX, double localY, double localWidth, double localHeight) {
        BaseBounds tempBounds = TempState.getInstance().bounds;
        return getLocalBounds(tempBounds, BaseTransform.IDENTITY_TRANSFORM).intersects((float) localX, (float) localY, (float) localWidth, (float) localHeight);
    }

    public boolean intersects(Bounds localBounds) {
        return intersects(localBounds.getMinX(), localBounds.getMinY(), localBounds.getWidth(), localBounds.getHeight());
    }

    public Point2D screenToLocal(double screenX, double screenY) {
        Window window;
        Scene scene = getScene();
        if (scene == null || (window = scene.getWindow()) == null) {
            return null;
        }
        com.sun.javafx.geom.Point2D tempPt = TempState.getInstance().point;
        tempPt.setLocation((float) ((screenX - scene.getX()) - window.getX()), (float) ((screenY - scene.getY()) - window.getY()));
        SubScene subScene = getSubScene();
        if (subScene != null) {
            Point2D ssCoord = SceneUtils.sceneToSubScenePlane(subScene, new Point2D(tempPt.f11907x, tempPt.f11908y));
            if (ssCoord == null) {
                return null;
            }
            tempPt.setLocation((float) ssCoord.getX(), (float) ssCoord.getY());
        }
        Point3D ppIntersect = scene.getEffectiveCamera().pickProjectPlane(tempPt.f11907x, tempPt.f11908y);
        tempPt.setLocation((float) ppIntersect.getX(), (float) ppIntersect.getY());
        try {
            sceneToLocal(tempPt);
            return new Point2D(tempPt.f11907x, tempPt.f11908y);
        } catch (NoninvertibleTransformException e2) {
            return null;
        }
    }

    public Point2D screenToLocal(Point2D screenPoint) {
        return screenToLocal(screenPoint.getX(), screenPoint.getY());
    }

    public Bounds screenToLocal(Bounds screenBounds) {
        Point2D p1 = screenToLocal(screenBounds.getMinX(), screenBounds.getMinY());
        Point2D p2 = screenToLocal(screenBounds.getMinX(), screenBounds.getMaxY());
        Point2D p3 = screenToLocal(screenBounds.getMaxX(), screenBounds.getMinY());
        Point2D p4 = screenToLocal(screenBounds.getMaxX(), screenBounds.getMaxY());
        return BoundsUtils.createBoundingBox(p1, p2, p3, p4);
    }

    public Point2D sceneToLocal(double x2, double y2, boolean rootScene) {
        if (!rootScene) {
            return sceneToLocal(x2, y2);
        }
        com.sun.javafx.geom.Point2D tempPt = TempState.getInstance().point;
        tempPt.setLocation((float) x2, (float) y2);
        SubScene subScene = getSubScene();
        if (subScene != null) {
            Point2D ssCoord = SceneUtils.sceneToSubScenePlane(subScene, new Point2D(tempPt.f11907x, tempPt.f11908y));
            if (ssCoord == null) {
                return null;
            }
            tempPt.setLocation((float) ssCoord.getX(), (float) ssCoord.getY());
        }
        try {
            sceneToLocal(tempPt);
            return new Point2D(tempPt.f11907x, tempPt.f11908y);
        } catch (NoninvertibleTransformException e2) {
            return null;
        }
    }

    public Point2D sceneToLocal(Point2D point, boolean rootScene) {
        return sceneToLocal(point.getX(), point.getY(), rootScene);
    }

    public Bounds sceneToLocal(Bounds bounds, boolean rootScene) {
        if (!rootScene) {
            return sceneToLocal(bounds);
        }
        if (bounds.getMinZ() != 0.0d || bounds.getMaxZ() != 0.0d) {
            return null;
        }
        Point2D p1 = sceneToLocal(bounds.getMinX(), bounds.getMinY(), true);
        Point2D p2 = sceneToLocal(bounds.getMinX(), bounds.getMaxY(), true);
        Point2D p3 = sceneToLocal(bounds.getMaxX(), bounds.getMinY(), true);
        Point2D p4 = sceneToLocal(bounds.getMaxX(), bounds.getMaxY(), true);
        return BoundsUtils.createBoundingBox(p1, p2, p3, p4);
    }

    public Point2D sceneToLocal(double sceneX, double sceneY) {
        com.sun.javafx.geom.Point2D tempPt = TempState.getInstance().point;
        tempPt.setLocation((float) sceneX, (float) sceneY);
        try {
            sceneToLocal(tempPt);
            return new Point2D(tempPt.f11907x, tempPt.f11908y);
        } catch (NoninvertibleTransformException e2) {
            return null;
        }
    }

    public Point2D sceneToLocal(Point2D scenePoint) {
        return sceneToLocal(scenePoint.getX(), scenePoint.getY());
    }

    public Point3D sceneToLocal(Point3D scenePoint) {
        return sceneToLocal(scenePoint.getX(), scenePoint.getY(), scenePoint.getZ());
    }

    public Point3D sceneToLocal(double sceneX, double sceneY, double sceneZ) {
        try {
            return sceneToLocal0(sceneX, sceneY, sceneZ);
        } catch (NoninvertibleTransformException e2) {
            return null;
        }
    }

    private Point3D sceneToLocal0(double x2, double y2, double z2) throws NoninvertibleTransformException {
        Vec3d tempV3D = TempState.getInstance().vec3d;
        tempV3D.set(x2, y2, z2);
        sceneToLocal(tempV3D);
        return new Point3D(tempV3D.f11930x, tempV3D.f11931y, tempV3D.f11932z);
    }

    public Bounds sceneToLocal(Bounds sceneBounds) {
        updateLocalToParentTransform();
        if (this.localToParentTx.is2D() && sceneBounds.getMinZ() == 0.0d && sceneBounds.getMaxZ() == 0.0d) {
            Point2D p1 = sceneToLocal(sceneBounds.getMinX(), sceneBounds.getMinY());
            Point2D p2 = sceneToLocal(sceneBounds.getMaxX(), sceneBounds.getMinY());
            Point2D p3 = sceneToLocal(sceneBounds.getMaxX(), sceneBounds.getMaxY());
            Point2D p4 = sceneToLocal(sceneBounds.getMinX(), sceneBounds.getMaxY());
            return BoundsUtils.createBoundingBox(p1, p2, p3, p4);
        }
        try {
            Point3D p12 = sceneToLocal0(sceneBounds.getMinX(), sceneBounds.getMinY(), sceneBounds.getMinZ());
            Point3D p22 = sceneToLocal0(sceneBounds.getMinX(), sceneBounds.getMinY(), sceneBounds.getMaxZ());
            Point3D p32 = sceneToLocal0(sceneBounds.getMinX(), sceneBounds.getMaxY(), sceneBounds.getMinZ());
            Point3D p42 = sceneToLocal0(sceneBounds.getMinX(), sceneBounds.getMaxY(), sceneBounds.getMaxZ());
            Point3D p5 = sceneToLocal0(sceneBounds.getMaxX(), sceneBounds.getMaxY(), sceneBounds.getMinZ());
            Point3D p6 = sceneToLocal0(sceneBounds.getMaxX(), sceneBounds.getMaxY(), sceneBounds.getMaxZ());
            Point3D p7 = sceneToLocal0(sceneBounds.getMaxX(), sceneBounds.getMinY(), sceneBounds.getMinZ());
            Point3D p8 = sceneToLocal0(sceneBounds.getMaxX(), sceneBounds.getMinY(), sceneBounds.getMaxZ());
            return BoundsUtils.createBoundingBox(p12, p22, p32, p42, p5, p6, p7, p8);
        } catch (NoninvertibleTransformException e2) {
            return null;
        }
    }

    public Point2D localToScreen(double localX, double localY) {
        return localToScreen(localX, localY, 0.0d);
    }

    public Point2D localToScreen(Point2D localPoint) {
        return localToScreen(localPoint.getX(), localPoint.getY());
    }

    public Point2D localToScreen(double localX, double localY, double localZ) {
        Window window;
        Scene scene = getScene();
        if (scene == null || (window = scene.getWindow()) == null) {
            return null;
        }
        Point3D pt = localToScene(localX, localY, localZ);
        SubScene subScene = getSubScene();
        if (subScene != null) {
            pt = SceneUtils.subSceneToScene(subScene, pt);
        }
        Point2D projection = CameraHelper.project(SceneHelper.getEffectiveCamera(getScene()), pt);
        return new Point2D(projection.getX() + scene.getX() + window.getX(), projection.getY() + scene.getY() + window.getY());
    }

    public Point2D localToScreen(Point3D localPoint) {
        return localToScreen(localPoint.getX(), localPoint.getY(), localPoint.getZ());
    }

    public Bounds localToScreen(Bounds localBounds) {
        Point2D p1 = localToScreen(localBounds.getMinX(), localBounds.getMinY(), localBounds.getMinZ());
        Point2D p2 = localToScreen(localBounds.getMinX(), localBounds.getMinY(), localBounds.getMaxZ());
        Point2D p3 = localToScreen(localBounds.getMinX(), localBounds.getMaxY(), localBounds.getMinZ());
        Point2D p4 = localToScreen(localBounds.getMinX(), localBounds.getMaxY(), localBounds.getMaxZ());
        Point2D p5 = localToScreen(localBounds.getMaxX(), localBounds.getMaxY(), localBounds.getMinZ());
        Point2D p6 = localToScreen(localBounds.getMaxX(), localBounds.getMaxY(), localBounds.getMaxZ());
        Point2D p7 = localToScreen(localBounds.getMaxX(), localBounds.getMinY(), localBounds.getMinZ());
        Point2D p8 = localToScreen(localBounds.getMaxX(), localBounds.getMinY(), localBounds.getMaxZ());
        return BoundsUtils.createBoundingBox(p1, p2, p3, p4, p5, p6, p7, p8);
    }

    public Point2D localToScene(double localX, double localY) {
        com.sun.javafx.geom.Point2D tempPt = TempState.getInstance().point;
        tempPt.setLocation((float) localX, (float) localY);
        localToScene(tempPt);
        return new Point2D(tempPt.f11907x, tempPt.f11908y);
    }

    public Point2D localToScene(Point2D localPoint) {
        return localToScene(localPoint.getX(), localPoint.getY());
    }

    public Point3D localToScene(Point3D localPoint) {
        return localToScene(localPoint.getX(), localPoint.getY(), localPoint.getZ());
    }

    public Point3D localToScene(double x2, double y2, double z2) {
        Vec3d tempV3D = TempState.getInstance().vec3d;
        tempV3D.set(x2, y2, z2);
        localToScene(tempV3D);
        return new Point3D(tempV3D.f11930x, tempV3D.f11931y, tempV3D.f11932z);
    }

    public Point3D localToScene(Point3D localPoint, boolean rootScene) {
        SubScene subScene;
        Point3D pt = localToScene(localPoint);
        if (rootScene && (subScene = getSubScene()) != null) {
            pt = SceneUtils.subSceneToScene(subScene, pt);
        }
        return pt;
    }

    public Point3D localToScene(double x2, double y2, double z2, boolean rootScene) {
        return localToScene(new Point3D(x2, y2, z2), rootScene);
    }

    public Point2D localToScene(Point2D localPoint, boolean rootScene) {
        if (!rootScene) {
            return localToScene(localPoint);
        }
        Point3D pt = localToScene(localPoint.getX(), localPoint.getY(), 0.0d, rootScene);
        return new Point2D(pt.getX(), pt.getY());
    }

    public Point2D localToScene(double x2, double y2, boolean rootScene) {
        return localToScene(new Point2D(x2, y2), rootScene);
    }

    public Bounds localToScene(Bounds localBounds, boolean rootScene) {
        if (!rootScene) {
            return localToScene(localBounds);
        }
        Point3D p1 = localToScene(localBounds.getMinX(), localBounds.getMinY(), localBounds.getMinZ(), true);
        Point3D p2 = localToScene(localBounds.getMinX(), localBounds.getMinY(), localBounds.getMaxZ(), true);
        Point3D p3 = localToScene(localBounds.getMinX(), localBounds.getMaxY(), localBounds.getMinZ(), true);
        Point3D p4 = localToScene(localBounds.getMinX(), localBounds.getMaxY(), localBounds.getMaxZ(), true);
        Point3D p5 = localToScene(localBounds.getMaxX(), localBounds.getMaxY(), localBounds.getMinZ(), true);
        Point3D p6 = localToScene(localBounds.getMaxX(), localBounds.getMaxY(), localBounds.getMaxZ(), true);
        Point3D p7 = localToScene(localBounds.getMaxX(), localBounds.getMinY(), localBounds.getMinZ(), true);
        Point3D p8 = localToScene(localBounds.getMaxX(), localBounds.getMinY(), localBounds.getMaxZ(), true);
        return BoundsUtils.createBoundingBox(p1, p2, p3, p4, p5, p6, p7, p8);
    }

    public Bounds localToScene(Bounds localBounds) {
        updateLocalToParentTransform();
        if (this.localToParentTx.is2D() && localBounds.getMinZ() == 0.0d && localBounds.getMaxZ() == 0.0d) {
            Point2D p1 = localToScene(localBounds.getMinX(), localBounds.getMinY());
            Point2D p2 = localToScene(localBounds.getMaxX(), localBounds.getMinY());
            Point2D p3 = localToScene(localBounds.getMaxX(), localBounds.getMaxY());
            Point2D p4 = localToScene(localBounds.getMinX(), localBounds.getMaxY());
            return BoundsUtils.createBoundingBox(p1, p2, p3, p4);
        }
        Point3D p12 = localToScene(localBounds.getMinX(), localBounds.getMinY(), localBounds.getMinZ());
        Point3D p22 = localToScene(localBounds.getMinX(), localBounds.getMinY(), localBounds.getMaxZ());
        Point3D p32 = localToScene(localBounds.getMinX(), localBounds.getMaxY(), localBounds.getMinZ());
        Point3D p42 = localToScene(localBounds.getMinX(), localBounds.getMaxY(), localBounds.getMaxZ());
        Point3D p5 = localToScene(localBounds.getMaxX(), localBounds.getMaxY(), localBounds.getMinZ());
        Point3D p6 = localToScene(localBounds.getMaxX(), localBounds.getMaxY(), localBounds.getMaxZ());
        Point3D p7 = localToScene(localBounds.getMaxX(), localBounds.getMinY(), localBounds.getMinZ());
        Point3D p8 = localToScene(localBounds.getMaxX(), localBounds.getMinY(), localBounds.getMaxZ());
        return BoundsUtils.createBoundingBox(p12, p22, p32, p42, p5, p6, p7, p8);
    }

    public Point2D parentToLocal(double parentX, double parentY) {
        com.sun.javafx.geom.Point2D tempPt = TempState.getInstance().point;
        tempPt.setLocation((float) parentX, (float) parentY);
        try {
            parentToLocal(tempPt);
            return new Point2D(tempPt.f11907x, tempPt.f11908y);
        } catch (NoninvertibleTransformException e2) {
            return null;
        }
    }

    public Point2D parentToLocal(Point2D parentPoint) {
        return parentToLocal(parentPoint.getX(), parentPoint.getY());
    }

    public Point3D parentToLocal(Point3D parentPoint) {
        return parentToLocal(parentPoint.getX(), parentPoint.getY(), parentPoint.getZ());
    }

    public Point3D parentToLocal(double parentX, double parentY, double parentZ) {
        Vec3d tempV3D = TempState.getInstance().vec3d;
        tempV3D.set(parentX, parentY, parentZ);
        try {
            parentToLocal(tempV3D);
            return new Point3D(tempV3D.f11930x, tempV3D.f11931y, tempV3D.f11932z);
        } catch (NoninvertibleTransformException e2) {
            return null;
        }
    }

    public Bounds parentToLocal(Bounds parentBounds) {
        updateLocalToParentTransform();
        if (this.localToParentTx.is2D() && parentBounds.getMinZ() == 0.0d && parentBounds.getMaxZ() == 0.0d) {
            Point2D p1 = parentToLocal(parentBounds.getMinX(), parentBounds.getMinY());
            Point2D p2 = parentToLocal(parentBounds.getMaxX(), parentBounds.getMinY());
            Point2D p3 = parentToLocal(parentBounds.getMaxX(), parentBounds.getMaxY());
            Point2D p4 = parentToLocal(parentBounds.getMinX(), parentBounds.getMaxY());
            return BoundsUtils.createBoundingBox(p1, p2, p3, p4);
        }
        Point3D p12 = parentToLocal(parentBounds.getMinX(), parentBounds.getMinY(), parentBounds.getMinZ());
        Point3D p22 = parentToLocal(parentBounds.getMinX(), parentBounds.getMinY(), parentBounds.getMaxZ());
        Point3D p32 = parentToLocal(parentBounds.getMinX(), parentBounds.getMaxY(), parentBounds.getMinZ());
        Point3D p42 = parentToLocal(parentBounds.getMinX(), parentBounds.getMaxY(), parentBounds.getMaxZ());
        Point3D p5 = parentToLocal(parentBounds.getMaxX(), parentBounds.getMaxY(), parentBounds.getMinZ());
        Point3D p6 = parentToLocal(parentBounds.getMaxX(), parentBounds.getMaxY(), parentBounds.getMaxZ());
        Point3D p7 = parentToLocal(parentBounds.getMaxX(), parentBounds.getMinY(), parentBounds.getMinZ());
        Point3D p8 = parentToLocal(parentBounds.getMaxX(), parentBounds.getMinY(), parentBounds.getMaxZ());
        return BoundsUtils.createBoundingBox(p12, p22, p32, p42, p5, p6, p7, p8);
    }

    public Point2D localToParent(double localX, double localY) {
        com.sun.javafx.geom.Point2D tempPt = TempState.getInstance().point;
        tempPt.setLocation((float) localX, (float) localY);
        localToParent(tempPt);
        return new Point2D(tempPt.f11907x, tempPt.f11908y);
    }

    public Point2D localToParent(Point2D localPoint) {
        return localToParent(localPoint.getX(), localPoint.getY());
    }

    public Point3D localToParent(Point3D localPoint) {
        return localToParent(localPoint.getX(), localPoint.getY(), localPoint.getZ());
    }

    public Point3D localToParent(double x2, double y2, double z2) {
        Vec3d tempV3D = TempState.getInstance().vec3d;
        tempV3D.set(x2, y2, z2);
        localToParent(tempV3D);
        return new Point3D(tempV3D.f11930x, tempV3D.f11931y, tempV3D.f11932z);
    }

    public Bounds localToParent(Bounds localBounds) {
        updateLocalToParentTransform();
        if (this.localToParentTx.is2D() && localBounds.getMinZ() == 0.0d && localBounds.getMaxZ() == 0.0d) {
            Point2D p1 = localToParent(localBounds.getMinX(), localBounds.getMinY());
            Point2D p2 = localToParent(localBounds.getMaxX(), localBounds.getMinY());
            Point2D p3 = localToParent(localBounds.getMaxX(), localBounds.getMaxY());
            Point2D p4 = localToParent(localBounds.getMinX(), localBounds.getMaxY());
            return BoundsUtils.createBoundingBox(p1, p2, p3, p4);
        }
        Point3D p12 = localToParent(localBounds.getMinX(), localBounds.getMinY(), localBounds.getMinZ());
        Point3D p22 = localToParent(localBounds.getMinX(), localBounds.getMinY(), localBounds.getMaxZ());
        Point3D p32 = localToParent(localBounds.getMinX(), localBounds.getMaxY(), localBounds.getMinZ());
        Point3D p42 = localToParent(localBounds.getMinX(), localBounds.getMaxY(), localBounds.getMaxZ());
        Point3D p5 = localToParent(localBounds.getMaxX(), localBounds.getMaxY(), localBounds.getMinZ());
        Point3D p6 = localToParent(localBounds.getMaxX(), localBounds.getMaxY(), localBounds.getMaxZ());
        Point3D p7 = localToParent(localBounds.getMaxX(), localBounds.getMinY(), localBounds.getMinZ());
        Point3D p8 = localToParent(localBounds.getMaxX(), localBounds.getMinY(), localBounds.getMaxZ());
        return BoundsUtils.createBoundingBox(p12, p22, p32, p42, p5, p6, p7, p8);
    }

    BaseTransform getLocalToParentTransform(BaseTransform tx) {
        updateLocalToParentTransform();
        tx.setTransform(this.localToParentTx);
        return tx;
    }

    @Deprecated
    public final BaseTransform impl_getLeafTransform() {
        return getLocalToParentTransform(TempState.getInstance().leafTx);
    }

    @Deprecated
    public void impl_transformsChanged() {
        if (!this.transformDirty) {
            impl_markDirty(DirtyBits.NODE_TRANSFORM);
            this.transformDirty = true;
            transformedBoundsChanged();
        }
        invalidateLocalToParentTransform();
        invalidateLocalToSceneTransform();
    }

    @Deprecated
    public final double impl_getPivotX() {
        Bounds bounds = getLayoutBounds();
        return bounds.getMinX() + (bounds.getWidth() / 2.0d);
    }

    @Deprecated
    public final double impl_getPivotY() {
        Bounds bounds = getLayoutBounds();
        return bounds.getMinY() + (bounds.getHeight() / 2.0d);
    }

    @Deprecated
    public final double impl_getPivotZ() {
        Bounds bounds = getLayoutBounds();
        return bounds.getMinZ() + (bounds.getDepth() / 2.0d);
    }

    void updateLocalToParentTransform() {
        if (this.transformDirty) {
            this.localToParentTx.setToIdentity();
            boolean mirror = false;
            double mirroringCenter = 0.0d;
            if (hasMirroring()) {
                Scene sceneValue = getScene();
                if (sceneValue != null && sceneValue.getRoot() == this) {
                    mirroringCenter = sceneValue.getWidth() / 2.0d;
                    if (mirroringCenter == 0.0d) {
                        mirroringCenter = impl_getPivotX();
                    }
                    this.localToParentTx = this.localToParentTx.deriveWithTranslation(mirroringCenter, 0.0d);
                    this.localToParentTx = this.localToParentTx.deriveWithScale(-1.0d, 1.0d, 1.0d);
                    this.localToParentTx = this.localToParentTx.deriveWithTranslation(-mirroringCenter, 0.0d);
                } else {
                    mirror = true;
                    mirroringCenter = impl_getPivotX();
                }
            }
            if (getScaleX() != 1.0d || getScaleY() != 1.0d || getScaleZ() != 1.0d || getRotate() != 0.0d) {
                double pivotX = impl_getPivotX();
                double pivotY = impl_getPivotY();
                double pivotZ = impl_getPivotZ();
                this.localToParentTx = this.localToParentTx.deriveWithTranslation(getTranslateX() + getLayoutX() + pivotX, getTranslateY() + getLayoutY() + pivotY, getTranslateZ() + pivotZ);
                this.localToParentTx = this.localToParentTx.deriveWithRotation(Math.toRadians(getRotate()), getRotationAxis().getX(), getRotationAxis().getY(), getRotationAxis().getZ());
                this.localToParentTx = this.localToParentTx.deriveWithScale(getScaleX(), getScaleY(), getScaleZ());
                this.localToParentTx = this.localToParentTx.deriveWithTranslation(-pivotX, -pivotY, -pivotZ);
            } else {
                this.localToParentTx = this.localToParentTx.deriveWithTranslation(getTranslateX() + getLayoutX(), getTranslateY() + getLayoutY(), getTranslateZ());
            }
            if (impl_hasTransforms()) {
                for (Transform t2 : getTransforms()) {
                    this.localToParentTx = t2.impl_derive(this.localToParentTx);
                }
            }
            if (mirror) {
                this.localToParentTx = this.localToParentTx.deriveWithTranslation(mirroringCenter, 0.0d);
                this.localToParentTx = this.localToParentTx.deriveWithScale(-1.0d, 1.0d, 1.0d);
                this.localToParentTx = this.localToParentTx.deriveWithTranslation(-mirroringCenter, 0.0d);
            }
            this.transformDirty = false;
        }
    }

    void parentToLocal(com.sun.javafx.geom.Point2D pt) throws NoninvertibleTransformException {
        updateLocalToParentTransform();
        this.localToParentTx.inverseTransform(pt, pt);
    }

    void parentToLocal(Vec3d pt) throws NoninvertibleTransformException {
        updateLocalToParentTransform();
        this.localToParentTx.inverseTransform(pt, pt);
    }

    void sceneToLocal(com.sun.javafx.geom.Point2D pt) throws NoninvertibleTransformException {
        if (getParent() != null) {
            getParent().sceneToLocal(pt);
        }
        parentToLocal(pt);
    }

    void sceneToLocal(Vec3d pt) throws NoninvertibleTransformException {
        if (getParent() != null) {
            getParent().sceneToLocal(pt);
        }
        parentToLocal(pt);
    }

    void localToScene(com.sun.javafx.geom.Point2D pt) {
        localToParent(pt);
        if (getParent() != null) {
            getParent().localToScene(pt);
        }
    }

    void localToScene(Vec3d pt) {
        localToParent(pt);
        if (getParent() != null) {
            getParent().localToScene(pt);
        }
    }

    void localToParent(com.sun.javafx.geom.Point2D pt) {
        updateLocalToParentTransform();
        this.localToParentTx.transform(pt, pt);
    }

    void localToParent(Vec3d pt) {
        updateLocalToParentTransform();
        this.localToParentTx.transform(pt, pt);
    }

    @Deprecated
    protected void impl_pickNodeLocal(PickRay localPickRay, PickResultChooser result) {
        impl_intersects(localPickRay, result);
    }

    @Deprecated
    public final void impl_pickNode(PickRay pickRay, PickResultChooser result) {
        if (!isVisible() || isDisable() || isMouseTransparent()) {
            return;
        }
        Vec3d o2 = pickRay.getOriginNoClone();
        double ox = o2.f11930x;
        double oy = o2.f11931y;
        double oz = o2.f11932z;
        Vec3d d2 = pickRay.getDirectionNoClone();
        double dx = d2.f11930x;
        double dy = d2.f11931y;
        double dz = d2.f11932z;
        updateLocalToParentTransform();
        try {
            this.localToParentTx.inverseTransform(o2, o2);
            this.localToParentTx.inverseDeltaTransform(d2, d2);
            impl_pickNodeLocal(pickRay, result);
        } catch (NoninvertibleTransformException e2) {
        }
        pickRay.setOrigin(ox, oy, oz);
        pickRay.setDirection(dx, dy, dz);
    }

    @Deprecated
    protected final boolean impl_intersects(PickRay pickRay, PickResultChooser pickResult) {
        double boundsDistance = impl_intersectsBounds(pickRay);
        if (!Double.isNaN(boundsDistance)) {
            if (isPickOnBounds()) {
                if (pickResult != null) {
                    pickResult.offer(this, boundsDistance, PickResultChooser.computePoint(pickRay, boundsDistance));
                    return true;
                }
                return true;
            }
            return impl_computeIntersects(pickRay, pickResult);
        }
        return false;
    }

    @Deprecated
    protected boolean impl_computeIntersects(PickRay pickRay, PickResultChooser pickResult) {
        double origZ = pickRay.getOriginNoClone().f11932z;
        double dirZ = pickRay.getDirectionNoClone().f11932z;
        if (almostZero(dirZ)) {
            return false;
        }
        double t2 = (-origZ) / dirZ;
        if (t2 < pickRay.getNearClip() || t2 > pickRay.getFarClip()) {
            return false;
        }
        double x2 = pickRay.getOriginNoClone().f11930x + (pickRay.getDirectionNoClone().f11930x * t2);
        double y2 = pickRay.getOriginNoClone().f11931y + (pickRay.getDirectionNoClone().f11931y * t2);
        if (contains((float) x2, (float) y2)) {
            if (pickResult != null) {
                pickResult.offer(this, t2, PickResultChooser.computePoint(pickRay, t2));
                return true;
            }
            return true;
        }
        return false;
    }

    @Deprecated
    protected final double impl_intersectsBounds(PickRay pickRay) {
        double tmin;
        double tmax;
        Vec3d dir = pickRay.getDirectionNoClone();
        Vec3d origin = pickRay.getOriginNoClone();
        double originX = origin.f11930x;
        double originY = origin.f11931y;
        double originZ = origin.f11932z;
        TempState tempState = TempState.getInstance();
        BaseBounds tempBounds = getLocalBounds(tempState.bounds, BaseTransform.IDENTITY_TRANSFORM);
        if (dir.f11930x == 0.0d && dir.f11931y == 0.0d) {
            if (dir.f11932z == 0.0d || originX < tempBounds.getMinX() || originX > tempBounds.getMaxX() || originY < tempBounds.getMinY() || originY > tempBounds.getMaxY()) {
                return Double.NaN;
            }
            double invDirZ = 1.0d / dir.f11932z;
            boolean signZ = invDirZ < 0.0d;
            double minZ = tempBounds.getMinZ();
            double maxZ = tempBounds.getMaxZ();
            tmin = ((signZ ? maxZ : minZ) - originZ) * invDirZ;
            tmax = ((signZ ? minZ : maxZ) - originZ) * invDirZ;
        } else if (tempBounds.getDepth() == 0.0d) {
            if (almostZero(dir.f11932z)) {
                return Double.NaN;
            }
            double t2 = (tempBounds.getMinZ() - originZ) / dir.f11932z;
            double x2 = originX + (dir.f11930x * t2);
            double y2 = originY + (dir.f11931y * t2);
            if (x2 < tempBounds.getMinX() || x2 > tempBounds.getMaxX() || y2 < tempBounds.getMinY() || y2 > tempBounds.getMaxY()) {
                return Double.NaN;
            }
            tmax = t2;
            tmin = t2;
        } else {
            double invDirX = dir.f11930x == 0.0d ? Double.POSITIVE_INFINITY : 1.0d / dir.f11930x;
            double invDirY = dir.f11931y == 0.0d ? Double.POSITIVE_INFINITY : 1.0d / dir.f11931y;
            double invDirZ2 = dir.f11932z == 0.0d ? Double.POSITIVE_INFINITY : 1.0d / dir.f11932z;
            boolean signX = invDirX < 0.0d;
            boolean signY = invDirY < 0.0d;
            boolean signZ2 = invDirZ2 < 0.0d;
            double minX = tempBounds.getMinX();
            double minY = tempBounds.getMinY();
            double maxX = tempBounds.getMaxX();
            double maxY = tempBounds.getMaxY();
            tmin = Double.NEGATIVE_INFINITY;
            tmax = Double.POSITIVE_INFINITY;
            if (!Double.isInfinite(invDirX)) {
                tmin = ((signX ? maxX : minX) - originX) * invDirX;
                tmax = ((signX ? minX : maxX) - originX) * invDirX;
            } else if (minX > originX || maxX < originX) {
                return Double.NaN;
            }
            if (!Double.isInfinite(invDirY)) {
                double tymin = ((signY ? maxY : minY) - originY) * invDirY;
                double tymax = ((signY ? minY : maxY) - originY) * invDirY;
                if (tmin > tymax || tymin > tmax) {
                    return Double.NaN;
                }
                if (tymin > tmin) {
                    tmin = tymin;
                }
                if (tymax < tmax) {
                    tmax = tymax;
                }
            } else if (minY > originY || maxY < originY) {
                return Double.NaN;
            }
            double minZ2 = tempBounds.getMinZ();
            double maxZ2 = tempBounds.getMaxZ();
            if (!Double.isInfinite(invDirZ2)) {
                double tzmin = ((signZ2 ? maxZ2 : minZ2) - originZ) * invDirZ2;
                double tzmax = ((signZ2 ? minZ2 : maxZ2) - originZ) * invDirZ2;
                if (tmin > tzmax || tzmin > tmax) {
                    return Double.NaN;
                }
                if (tzmin > tmin) {
                    tmin = tzmin;
                }
                if (tzmax < tmax) {
                    tmax = tzmax;
                }
            } else if (minZ2 > originZ || maxZ2 < originZ) {
                return Double.NaN;
            }
        }
        Node clip = getClip();
        if (clip != null && !(this instanceof Shape3D) && !(clip instanceof Shape3D)) {
            double dirX = dir.f11930x;
            double dirY = dir.f11931y;
            double dirZ = dir.f11932z;
            clip.updateLocalToParentTransform();
            boolean hitClip = true;
            try {
                clip.localToParentTx.inverseTransform(origin, origin);
                clip.localToParentTx.inverseDeltaTransform(dir, dir);
            } catch (NoninvertibleTransformException e2) {
                hitClip = false;
            }
            boolean hitClip2 = hitClip && clip.impl_intersects(pickRay, null);
            pickRay.setOrigin(originX, originY, originZ);
            pickRay.setDirection(dirX, dirY, dirZ);
            if (!hitClip2) {
                return Double.NaN;
            }
        }
        if (Double.isInfinite(tmin) || Double.isNaN(tmin)) {
            return Double.NaN;
        }
        double minDistance = pickRay.getNearClip();
        double maxDistance = pickRay.getFarClip();
        if (tmin < minDistance) {
            if (tmax >= minDistance) {
                return 0.0d;
            }
            return Double.NaN;
        }
        if (tmin > maxDistance) {
            return Double.NaN;
        }
        return tmin;
    }

    static boolean almostZero(double a2) {
        return a2 < EPSILON_ABSOLUTE && a2 > -1.0E-5d;
    }

    public final ObservableList<Transform> getTransforms() {
        return transformsProperty();
    }

    private ObservableList<Transform> transformsProperty() {
        return getNodeTransformation().getTransforms();
    }

    public final void setTranslateX(double value) {
        translateXProperty().set(value);
    }

    public final double getTranslateX() {
        if (this.nodeTransformation == null) {
            return 0.0d;
        }
        return this.nodeTransformation.getTranslateX();
    }

    public final DoubleProperty translateXProperty() {
        return getNodeTransformation().translateXProperty();
    }

    public final void setTranslateY(double value) {
        translateYProperty().set(value);
    }

    public final double getTranslateY() {
        if (this.nodeTransformation == null) {
            return 0.0d;
        }
        return this.nodeTransformation.getTranslateY();
    }

    public final DoubleProperty translateYProperty() {
        return getNodeTransformation().translateYProperty();
    }

    public final void setTranslateZ(double value) {
        translateZProperty().set(value);
    }

    public final double getTranslateZ() {
        if (this.nodeTransformation == null) {
            return 0.0d;
        }
        return this.nodeTransformation.getTranslateZ();
    }

    public final DoubleProperty translateZProperty() {
        return getNodeTransformation().translateZProperty();
    }

    public final void setScaleX(double value) {
        scaleXProperty().set(value);
    }

    public final double getScaleX() {
        if (this.nodeTransformation == null) {
            return 1.0d;
        }
        return this.nodeTransformation.getScaleX();
    }

    public final DoubleProperty scaleXProperty() {
        return getNodeTransformation().scaleXProperty();
    }

    public final void setScaleY(double value) {
        scaleYProperty().set(value);
    }

    public final double getScaleY() {
        if (this.nodeTransformation == null) {
            return 1.0d;
        }
        return this.nodeTransformation.getScaleY();
    }

    public final DoubleProperty scaleYProperty() {
        return getNodeTransformation().scaleYProperty();
    }

    public final void setScaleZ(double value) {
        scaleZProperty().set(value);
    }

    public final double getScaleZ() {
        if (this.nodeTransformation == null) {
            return 1.0d;
        }
        return this.nodeTransformation.getScaleZ();
    }

    public final DoubleProperty scaleZProperty() {
        return getNodeTransformation().scaleZProperty();
    }

    public final void setRotate(double value) {
        rotateProperty().set(value);
    }

    public final double getRotate() {
        if (this.nodeTransformation == null) {
            return 0.0d;
        }
        return this.nodeTransformation.getRotate();
    }

    public final DoubleProperty rotateProperty() {
        return getNodeTransformation().rotateProperty();
    }

    public final void setRotationAxis(Point3D value) {
        rotationAxisProperty().set(value);
    }

    public final Point3D getRotationAxis() {
        return this.nodeTransformation == null ? DEFAULT_ROTATION_AXIS : this.nodeTransformation.getRotationAxis();
    }

    public final ObjectProperty<Point3D> rotationAxisProperty() {
        return getNodeTransformation().rotationAxisProperty();
    }

    public final ReadOnlyObjectProperty<Transform> localToParentTransformProperty() {
        return getNodeTransformation().localToParentTransformProperty();
    }

    private void invalidateLocalToParentTransform() {
        if (this.nodeTransformation != null) {
            this.nodeTransformation.invalidateLocalToParentTransform();
        }
    }

    public final Transform getLocalToParentTransform() {
        return localToParentTransformProperty().get();
    }

    public final ReadOnlyObjectProperty<Transform> localToSceneTransformProperty() {
        return getNodeTransformation().localToSceneTransformProperty();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invalidateLocalToSceneTransform() {
        if (this.nodeTransformation != null) {
            this.nodeTransformation.invalidateLocalToSceneTransform();
        }
    }

    public final Transform getLocalToSceneTransform() {
        return localToSceneTransformProperty().get();
    }

    private NodeTransformation getNodeTransformation() {
        if (this.nodeTransformation == null) {
            this.nodeTransformation = new NodeTransformation();
        }
        return this.nodeTransformation;
    }

    @Deprecated
    public boolean impl_hasTransforms() {
        return this.nodeTransformation != null && this.nodeTransformation.hasTransforms();
    }

    Transform getCurrentLocalToSceneTransformState() {
        if (this.nodeTransformation == null || this.nodeTransformation.localToSceneTransform == null) {
            return null;
        }
        return this.nodeTransformation.localToSceneTransform.transform;
    }

    /* loaded from: jfxrt.jar:javafx/scene/Node$NodeTransformation.class */
    private final class NodeTransformation {
        private DoubleProperty translateX;
        private DoubleProperty translateY;
        private DoubleProperty translateZ;
        private DoubleProperty scaleX;
        private DoubleProperty scaleY;
        private DoubleProperty scaleZ;
        private DoubleProperty rotate;
        private ObjectProperty<Point3D> rotationAxis;
        private ObservableList<Transform> transforms;
        private LazyTransformProperty localToParentTransform;
        private LazyTransformProperty localToSceneTransform;
        private int listenerReasons;
        private InvalidationListener localToSceneInvLstnr;

        private NodeTransformation() {
            this.listenerReasons = 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public InvalidationListener getLocalToSceneInvalidationListener() {
            if (this.localToSceneInvLstnr == null) {
                this.localToSceneInvLstnr = observable -> {
                    invalidateLocalToSceneTransform();
                };
            }
            return this.localToSceneInvLstnr;
        }

        public void incListenerReasons() {
            Node n2;
            if (this.listenerReasons == 0 && (n2 = Node.this.getParent()) != null) {
                n2.localToSceneTransformProperty().addListener(getLocalToSceneInvalidationListener());
            }
            this.listenerReasons++;
        }

        public void decListenerReasons() {
            this.listenerReasons--;
            if (this.listenerReasons == 0) {
                Node n2 = Node.this.getParent();
                if (n2 != null) {
                    n2.localToSceneTransformProperty().removeListener(getLocalToSceneInvalidationListener());
                }
                if (this.localToSceneTransform != null) {
                    this.localToSceneTransform.validityUnknown();
                }
            }
        }

        public final Transform getLocalToParentTransform() {
            return localToParentTransformProperty().get();
        }

        public final ReadOnlyObjectProperty<Transform> localToParentTransformProperty() {
            if (this.localToParentTransform == null) {
                this.localToParentTransform = new LazyTransformProperty() { // from class: javafx.scene.Node.NodeTransformation.1
                    @Override // javafx.scene.Node.LazyTransformProperty
                    protected Transform computeTransform(Transform reuse) {
                        Node.this.updateLocalToParentTransform();
                        return TransformUtils.immutableTransform(reuse, Node.this.localToParentTx.getMxx(), Node.this.localToParentTx.getMxy(), Node.this.localToParentTx.getMxz(), Node.this.localToParentTx.getMxt(), Node.this.localToParentTx.getMyx(), Node.this.localToParentTx.getMyy(), Node.this.localToParentTx.getMyz(), Node.this.localToParentTx.getMyt(), Node.this.localToParentTx.getMzx(), Node.this.localToParentTx.getMzy(), Node.this.localToParentTx.getMzz(), Node.this.localToParentTx.getMzt());
                    }

                    @Override // javafx.scene.Node.LazyTransformProperty
                    protected boolean validityKnown() {
                        return true;
                    }

                    @Override // javafx.scene.Node.LazyTransformProperty
                    protected int computeValidity() {
                        return this.valid;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Node.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "localToParentTransform";
                    }
                };
            }
            return this.localToParentTransform;
        }

        public void invalidateLocalToParentTransform() {
            if (this.localToParentTransform != null) {
                this.localToParentTransform.invalidate();
            }
        }

        public final Transform getLocalToSceneTransform() {
            return localToSceneTransformProperty().get();
        }

        /* loaded from: jfxrt.jar:javafx/scene/Node$NodeTransformation$LocalToSceneTransformProperty.class */
        class LocalToSceneTransformProperty extends LazyTransformProperty {
            private List localToSceneListeners;
            private long stamp;
            private long parentStamp;

            LocalToSceneTransformProperty() {
                super();
            }

            @Override // javafx.scene.Node.LazyTransformProperty
            protected Transform computeTransform(Transform reuse) {
                this.stamp++;
                Node.this.updateLocalToParentTransform();
                Node parentNode = Node.this.getParent();
                if (parentNode != null) {
                    LocalToSceneTransformProperty parentProperty = (LocalToSceneTransformProperty) parentNode.localToSceneTransformProperty();
                    Transform parentTransform = parentProperty.getInternalValue();
                    this.parentStamp = parentProperty.stamp;
                    return TransformUtils.immutableTransform(reuse, parentTransform, ((LazyTransformProperty) NodeTransformation.this.localToParentTransformProperty()).getInternalValue());
                }
                return TransformUtils.immutableTransform(reuse, ((LazyTransformProperty) NodeTransformation.this.localToParentTransformProperty()).getInternalValue());
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return Node.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "localToSceneTransform";
            }

            @Override // javafx.scene.Node.LazyTransformProperty
            protected boolean validityKnown() {
                return NodeTransformation.this.listenerReasons > 0;
            }

            @Override // javafx.scene.Node.LazyTransformProperty
            protected int computeValidity() {
                if (this.valid != 2) {
                    return this.valid;
                }
                Node n2 = (Node) getBean();
                Node parent = n2.getParent();
                if (parent != null) {
                    LocalToSceneTransformProperty parentProperty = (LocalToSceneTransformProperty) parent.localToSceneTransformProperty();
                    if (this.parentStamp != parentProperty.stamp) {
                        this.valid = 1;
                        return 1;
                    }
                    int parentValid = parentProperty.computeValidity();
                    if (parentValid == 1) {
                        this.valid = 1;
                    }
                    return parentValid;
                }
                return 0;
            }

            @Override // javafx.scene.Node.LazyTransformProperty, javafx.beans.Observable
            public void addListener(InvalidationListener listener) {
                NodeTransformation.this.incListenerReasons();
                if (this.localToSceneListeners == null) {
                    this.localToSceneListeners = new LinkedList();
                }
                this.localToSceneListeners.add(listener);
                super.addListener(listener);
            }

            @Override // javafx.scene.Node.LazyTransformProperty, javafx.beans.value.ObservableValue
            public void addListener(ChangeListener<? super Transform> listener) {
                NodeTransformation.this.incListenerReasons();
                if (this.localToSceneListeners == null) {
                    this.localToSceneListeners = new LinkedList();
                }
                this.localToSceneListeners.add(listener);
                super.addListener(listener);
            }

            @Override // javafx.scene.Node.LazyTransformProperty, javafx.beans.Observable
            public void removeListener(InvalidationListener listener) {
                if (this.localToSceneListeners != null && this.localToSceneListeners.remove(listener)) {
                    NodeTransformation.this.decListenerReasons();
                }
                super.removeListener(listener);
            }

            @Override // javafx.scene.Node.LazyTransformProperty, javafx.beans.value.ObservableValue
            public void removeListener(ChangeListener<? super Transform> listener) {
                if (this.localToSceneListeners != null && this.localToSceneListeners.remove(listener)) {
                    NodeTransformation.this.decListenerReasons();
                }
                super.removeListener(listener);
            }
        }

        public final ReadOnlyObjectProperty<Transform> localToSceneTransformProperty() {
            if (this.localToSceneTransform == null) {
                this.localToSceneTransform = new LocalToSceneTransformProperty();
            }
            return this.localToSceneTransform;
        }

        public void invalidateLocalToSceneTransform() {
            if (this.localToSceneTransform != null) {
                this.localToSceneTransform.invalidate();
            }
        }

        public double getTranslateX() {
            if (this.translateX == null) {
                return 0.0d;
            }
            return this.translateX.get();
        }

        public final DoubleProperty translateXProperty() {
            if (this.translateX == null) {
                this.translateX = new StyleableDoubleProperty(0.0d) { // from class: javafx.scene.Node.NodeTransformation.2
                    @Override // javafx.beans.property.DoublePropertyBase
                    public void invalidated() {
                        Node.this.impl_transformsChanged();
                    }

                    @Override // javafx.css.StyleableProperty
                    public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                        return StyleableProperties.TRANSLATE_X;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Node.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "translateX";
                    }
                };
            }
            return this.translateX;
        }

        public double getTranslateY() {
            if (this.translateY == null) {
                return 0.0d;
            }
            return this.translateY.get();
        }

        public final DoubleProperty translateYProperty() {
            if (this.translateY == null) {
                this.translateY = new StyleableDoubleProperty(0.0d) { // from class: javafx.scene.Node.NodeTransformation.3
                    @Override // javafx.beans.property.DoublePropertyBase
                    public void invalidated() {
                        Node.this.impl_transformsChanged();
                    }

                    @Override // javafx.css.StyleableProperty
                    public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                        return StyleableProperties.TRANSLATE_Y;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Node.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "translateY";
                    }
                };
            }
            return this.translateY;
        }

        public double getTranslateZ() {
            if (this.translateZ == null) {
                return 0.0d;
            }
            return this.translateZ.get();
        }

        public final DoubleProperty translateZProperty() {
            if (this.translateZ == null) {
                this.translateZ = new StyleableDoubleProperty(0.0d) { // from class: javafx.scene.Node.NodeTransformation.4
                    @Override // javafx.beans.property.DoublePropertyBase
                    public void invalidated() {
                        Node.this.impl_transformsChanged();
                    }

                    @Override // javafx.css.StyleableProperty
                    public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                        return StyleableProperties.TRANSLATE_Z;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Node.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "translateZ";
                    }
                };
            }
            return this.translateZ;
        }

        public double getScaleX() {
            if (this.scaleX == null) {
                return 1.0d;
            }
            return this.scaleX.get();
        }

        public final DoubleProperty scaleXProperty() {
            if (this.scaleX == null) {
                this.scaleX = new StyleableDoubleProperty(1.0d) { // from class: javafx.scene.Node.NodeTransformation.5
                    @Override // javafx.beans.property.DoublePropertyBase
                    public void invalidated() {
                        Node.this.impl_transformsChanged();
                    }

                    @Override // javafx.css.StyleableProperty
                    public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                        return StyleableProperties.SCALE_X;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Node.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "scaleX";
                    }
                };
            }
            return this.scaleX;
        }

        public double getScaleY() {
            if (this.scaleY == null) {
                return 1.0d;
            }
            return this.scaleY.get();
        }

        public final DoubleProperty scaleYProperty() {
            if (this.scaleY == null) {
                this.scaleY = new StyleableDoubleProperty(1.0d) { // from class: javafx.scene.Node.NodeTransformation.6
                    @Override // javafx.beans.property.DoublePropertyBase
                    public void invalidated() {
                        Node.this.impl_transformsChanged();
                    }

                    @Override // javafx.css.StyleableProperty
                    public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                        return StyleableProperties.SCALE_Y;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Node.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "scaleY";
                    }
                };
            }
            return this.scaleY;
        }

        public double getScaleZ() {
            if (this.scaleZ == null) {
                return 1.0d;
            }
            return this.scaleZ.get();
        }

        public final DoubleProperty scaleZProperty() {
            if (this.scaleZ == null) {
                this.scaleZ = new StyleableDoubleProperty(1.0d) { // from class: javafx.scene.Node.NodeTransformation.7
                    @Override // javafx.beans.property.DoublePropertyBase
                    public void invalidated() {
                        Node.this.impl_transformsChanged();
                    }

                    @Override // javafx.css.StyleableProperty
                    public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                        return StyleableProperties.SCALE_Z;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Node.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "scaleZ";
                    }
                };
            }
            return this.scaleZ;
        }

        public double getRotate() {
            if (this.rotate == null) {
                return 0.0d;
            }
            return this.rotate.get();
        }

        public final DoubleProperty rotateProperty() {
            if (this.rotate == null) {
                this.rotate = new StyleableDoubleProperty(0.0d) { // from class: javafx.scene.Node.NodeTransformation.8
                    @Override // javafx.beans.property.DoublePropertyBase
                    public void invalidated() {
                        Node.this.impl_transformsChanged();
                    }

                    @Override // javafx.css.StyleableProperty
                    public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                        return StyleableProperties.ROTATE;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Node.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "rotate";
                    }
                };
            }
            return this.rotate;
        }

        public Point3D getRotationAxis() {
            if (this.rotationAxis == null) {
                return Node.DEFAULT_ROTATION_AXIS;
            }
            return this.rotationAxis.get();
        }

        public final ObjectProperty<Point3D> rotationAxisProperty() {
            if (this.rotationAxis == null) {
                this.rotationAxis = new ObjectPropertyBase<Point3D>(Node.DEFAULT_ROTATION_AXIS) { // from class: javafx.scene.Node.NodeTransformation.9
                    @Override // javafx.beans.property.ObjectPropertyBase
                    protected void invalidated() {
                        Node.this.impl_transformsChanged();
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Node.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "rotationAxis";
                    }
                };
            }
            return this.rotationAxis;
        }

        public ObservableList<Transform> getTransforms() {
            if (this.transforms == null) {
                this.transforms = new TrackableObservableList<Transform>() { // from class: javafx.scene.Node.NodeTransformation.10
                    @Override // com.sun.javafx.collections.TrackableObservableList
                    protected void onChanged(ListChangeListener.Change<Transform> c2) {
                        while (c2.next()) {
                            for (Transform t2 : c2.getRemoved()) {
                                t2.impl_remove(Node.this);
                            }
                            for (Transform t3 : c2.getAddedSubList()) {
                                t3.impl_add(Node.this);
                            }
                        }
                        Node.this.impl_transformsChanged();
                    }
                };
            }
            return this.transforms;
        }

        public boolean canSetTranslateX() {
            return this.translateX == null || !this.translateX.isBound();
        }

        public boolean canSetTranslateY() {
            return this.translateY == null || !this.translateY.isBound();
        }

        public boolean canSetTranslateZ() {
            return this.translateZ == null || !this.translateZ.isBound();
        }

        public boolean canSetScaleX() {
            return this.scaleX == null || !this.scaleX.isBound();
        }

        public boolean canSetScaleY() {
            return this.scaleY == null || !this.scaleY.isBound();
        }

        public boolean canSetScaleZ() {
            return this.scaleZ == null || !this.scaleZ.isBound();
        }

        public boolean canSetRotate() {
            return this.rotate == null || !this.rotate.isBound();
        }

        public boolean hasTransforms() {
            return (this.transforms == null || this.transforms.isEmpty()) ? false : true;
        }

        public boolean hasScaleOrRotate() {
            if (this.scaleX != null && this.scaleX.get() != 1.0d) {
                return true;
            }
            if (this.scaleY != null && this.scaleY.get() != 1.0d) {
                return true;
            }
            if (this.scaleZ != null && this.scaleZ.get() != 1.0d) {
                return true;
            }
            if (this.rotate != null && this.rotate.get() != 0.0d) {
                return true;
            }
            return false;
        }
    }

    private EventHandlerProperties getEventHandlerProperties() {
        if (this.eventHandlerProperties == null) {
            this.eventHandlerProperties = new EventHandlerProperties(getInternalEventDispatcher().getEventHandlerManager(), this);
        }
        return this.eventHandlerProperties;
    }

    public final void setNodeOrientation(NodeOrientation orientation) {
        nodeOrientationProperty().set(orientation);
    }

    public final NodeOrientation getNodeOrientation() {
        return this.nodeOrientation == null ? NodeOrientation.INHERIT : this.nodeOrientation.get();
    }

    public final ObjectProperty<NodeOrientation> nodeOrientationProperty() {
        if (this.nodeOrientation == null) {
            this.nodeOrientation = new StyleableObjectProperty<NodeOrientation>(NodeOrientation.INHERIT) { // from class: javafx.scene.Node.13
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Node.this.nodeResolvedOrientationInvalidated();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Node.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "nodeOrientation";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData getCssMetaData() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            };
        }
        return this.nodeOrientation;
    }

    public final NodeOrientation getEffectiveNodeOrientation() {
        return getEffectiveOrientation(this.resolvedNodeOrientation) == 0 ? NodeOrientation.LEFT_TO_RIGHT : NodeOrientation.RIGHT_TO_LEFT;
    }

    public final ReadOnlyObjectProperty<NodeOrientation> effectiveNodeOrientationProperty() {
        if (this.effectiveNodeOrientationProperty == null) {
            this.effectiveNodeOrientationProperty = new EffectiveOrientationProperty();
        }
        return this.effectiveNodeOrientationProperty;
    }

    public boolean usesMirroring() {
        return true;
    }

    final void parentResolvedOrientationInvalidated() {
        if (getNodeOrientation() == NodeOrientation.INHERIT) {
            nodeResolvedOrientationInvalidated();
        } else {
            impl_transformsChanged();
        }
    }

    final void nodeResolvedOrientationInvalidated() {
        byte oldResolvedNodeOrientation = this.resolvedNodeOrientation;
        this.resolvedNodeOrientation = (byte) (calcEffectiveNodeOrientation() | calcAutomaticNodeOrientation());
        if (this.effectiveNodeOrientationProperty != null && getEffectiveOrientation(this.resolvedNodeOrientation) != getEffectiveOrientation(oldResolvedNodeOrientation)) {
            this.effectiveNodeOrientationProperty.invalidate();
        }
        impl_transformsChanged();
        if (this.resolvedNodeOrientation != oldResolvedNodeOrientation) {
            nodeResolvedOrientationChanged();
        }
    }

    void nodeResolvedOrientationChanged() {
    }

    private Node getMirroringOrientationParent() {
        Node parent = getParent();
        while (true) {
            Node parentValue = parent;
            if (parentValue != null) {
                if (parentValue.usesMirroring()) {
                    return parentValue;
                }
                parent = parentValue.getParent();
            } else {
                Node subSceneValue = getSubScene();
                if (subSceneValue != null) {
                    return subSceneValue;
                }
                return null;
            }
        }
    }

    private Node getOrientationParent() {
        Node parentValue = getParent();
        if (parentValue != null) {
            return parentValue;
        }
        Node subSceneValue = getSubScene();
        if (subSceneValue != null) {
            return subSceneValue;
        }
        return null;
    }

    private byte calcEffectiveNodeOrientation() {
        NodeOrientation nodeOrientationValue = getNodeOrientation();
        if (nodeOrientationValue != NodeOrientation.INHERIT) {
            return nodeOrientationValue == NodeOrientation.LEFT_TO_RIGHT ? (byte) 0 : (byte) 1;
        }
        Node parentValue = getOrientationParent();
        if (parentValue != null) {
            return getEffectiveOrientation(parentValue.resolvedNodeOrientation);
        }
        Scene sceneValue = getScene();
        return (sceneValue == null || sceneValue.getEffectiveNodeOrientation() == NodeOrientation.LEFT_TO_RIGHT) ? (byte) 0 : (byte) 1;
    }

    private byte calcAutomaticNodeOrientation() {
        if (!usesMirroring()) {
            return (byte) 0;
        }
        NodeOrientation nodeOrientationValue = getNodeOrientation();
        if (nodeOrientationValue != NodeOrientation.INHERIT) {
            return nodeOrientationValue == NodeOrientation.LEFT_TO_RIGHT ? (byte) 0 : (byte) 2;
        }
        Node parentValue = getMirroringOrientationParent();
        if (parentValue != null) {
            return getAutomaticOrientation(parentValue.resolvedNodeOrientation);
        }
        Scene sceneValue = getScene();
        return (sceneValue == null || sceneValue.getEffectiveNodeOrientation() == NodeOrientation.LEFT_TO_RIGHT) ? (byte) 0 : (byte) 2;
    }

    final boolean hasMirroring() {
        Node parentValue = getOrientationParent();
        byte thisOrientation = getAutomaticOrientation(this.resolvedNodeOrientation);
        byte parentOrientation = parentValue != null ? getAutomaticOrientation(parentValue.resolvedNodeOrientation) : (byte) 0;
        return thisOrientation != parentOrientation;
    }

    private static byte getEffectiveOrientation(byte resolvedNodeOrientation) {
        return (byte) (resolvedNodeOrientation & 1);
    }

    private static byte getAutomaticOrientation(byte resolvedNodeOrientation) {
        return (byte) (resolvedNodeOrientation & 2);
    }

    /* loaded from: jfxrt.jar:javafx/scene/Node$EffectiveOrientationProperty.class */
    private final class EffectiveOrientationProperty extends ReadOnlyObjectPropertyBase<NodeOrientation> {
        private EffectiveOrientationProperty() {
        }

        @Override // javafx.beans.value.ObservableObjectValue
        public NodeOrientation get() {
            return Node.this.getEffectiveNodeOrientation();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Node.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "effectiveNodeOrientation";
        }

        public void invalidate() {
            fireValueChangedEvent();
        }
    }

    private MiscProperties getMiscProperties() {
        if (this.miscProperties == null) {
            this.miscProperties = new MiscProperties();
        }
        return this.miscProperties;
    }

    /* loaded from: jfxrt.jar:javafx/scene/Node$MiscProperties.class */
    private final class MiscProperties {
        private LazyBoundsProperty boundsInParent;
        private LazyBoundsProperty boundsInLocal;
        private BooleanProperty cache;
        private ObjectProperty<CacheHint> cacheHint;
        private ObjectProperty<Node> clip;
        private ObjectProperty<Cursor> cursor;
        private ObjectProperty<DepthTest> depthTest;
        private BooleanProperty disable;
        private ObjectProperty<Effect> effect;
        private ObjectProperty<InputMethodRequests> inputMethodRequests;
        private BooleanProperty mouseTransparent;

        private MiscProperties() {
        }

        public final Bounds getBoundsInParent() {
            return boundsInParentProperty().get();
        }

        public final ReadOnlyObjectProperty<Bounds> boundsInParentProperty() {
            if (this.boundsInParent == null) {
                this.boundsInParent = new LazyBoundsProperty() { // from class: javafx.scene.Node.MiscProperties.1
                    @Override // javafx.scene.Node.LazyBoundsProperty
                    protected Bounds computeBounds() {
                        BaseBounds tempBounds = Node.this.getTransformedBounds(TempState.getInstance().bounds, BaseTransform.IDENTITY_TRANSFORM);
                        return new BoundingBox(tempBounds.getMinX(), tempBounds.getMinY(), tempBounds.getMinZ(), tempBounds.getWidth(), tempBounds.getHeight(), tempBounds.getDepth());
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Node.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "boundsInParent";
                    }
                };
            }
            return this.boundsInParent;
        }

        public void invalidateBoundsInParent() {
            if (this.boundsInParent != null) {
                this.boundsInParent.invalidate();
            }
        }

        public final Bounds getBoundsInLocal() {
            return boundsInLocalProperty().get();
        }

        public final ReadOnlyObjectProperty<Bounds> boundsInLocalProperty() {
            if (this.boundsInLocal == null) {
                this.boundsInLocal = new LazyBoundsProperty() { // from class: javafx.scene.Node.MiscProperties.2
                    @Override // javafx.scene.Node.LazyBoundsProperty
                    protected Bounds computeBounds() {
                        BaseBounds tempBounds = Node.this.getLocalBounds(TempState.getInstance().bounds, BaseTransform.IDENTITY_TRANSFORM);
                        return new BoundingBox(tempBounds.getMinX(), tempBounds.getMinY(), tempBounds.getMinZ(), tempBounds.getWidth(), tempBounds.getHeight(), tempBounds.getDepth());
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Node.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "boundsInLocal";
                    }
                };
            }
            return this.boundsInLocal;
        }

        public void invalidateBoundsInLocal() {
            if (this.boundsInLocal != null) {
                this.boundsInLocal.invalidate();
            }
        }

        public final boolean isCache() {
            if (this.cache == null) {
                return false;
            }
            return this.cache.get();
        }

        public final BooleanProperty cacheProperty() {
            if (this.cache == null) {
                this.cache = new BooleanPropertyBase(false) { // from class: javafx.scene.Node.MiscProperties.3
                    @Override // javafx.beans.property.BooleanPropertyBase
                    protected void invalidated() {
                        Node.this.impl_markDirty(DirtyBits.NODE_CACHE);
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Node.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "cache";
                    }
                };
            }
            return this.cache;
        }

        public final CacheHint getCacheHint() {
            if (this.cacheHint == null) {
                return Node.DEFAULT_CACHE_HINT;
            }
            return this.cacheHint.get();
        }

        public final ObjectProperty<CacheHint> cacheHintProperty() {
            if (this.cacheHint == null) {
                this.cacheHint = new ObjectPropertyBase<CacheHint>(Node.DEFAULT_CACHE_HINT) { // from class: javafx.scene.Node.MiscProperties.4
                    @Override // javafx.beans.property.ObjectPropertyBase
                    protected void invalidated() {
                        Node.this.impl_markDirty(DirtyBits.NODE_CACHE);
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Node.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "cacheHint";
                    }
                };
            }
            return this.cacheHint;
        }

        public final Node getClip() {
            return this.clip == null ? Node.DEFAULT_CLIP : this.clip.get();
        }

        public final ObjectProperty<Node> clipProperty() {
            if (this.clip == null) {
                this.clip = new ObjectPropertyBase<Node>(Node.DEFAULT_CLIP) { // from class: javafx.scene.Node.MiscProperties.5
                    private Node oldClip;

                    @Override // javafx.beans.property.ObjectPropertyBase
                    protected void invalidated() {
                        Node newClip = get();
                        if (newClip != null && ((newClip.isConnected() && newClip.clipParent != Node.this) || Node.this.wouldCreateCycle(Node.this, newClip))) {
                            String cause = (!newClip.isConnected() || newClip.clipParent == Node.this) ? "cycle detected" : "node already connected";
                            if (isBound()) {
                                unbind();
                                set(this.oldClip);
                                throw new IllegalArgumentException("Node's clip set to incorrect value  through binding (" + cause + ", node  = " + ((Object) Node.this) + ", clip = " + ((Object) MiscProperties.this.clip) + "). Binding has been removed.");
                            }
                            set(this.oldClip);
                            throw new IllegalArgumentException("Node's clip set to incorrect value (" + cause + ", node  = " + ((Object) Node.this) + ", clip = " + ((Object) MiscProperties.this.clip) + ").");
                        }
                        if (this.oldClip != null) {
                            this.oldClip.clipParent = null;
                            this.oldClip.setScenes(null, null);
                            this.oldClip.updateTreeVisible(false);
                        }
                        if (newClip != null) {
                            newClip.clipParent = Node.this;
                            newClip.setScenes(Node.this.getScene(), Node.this.getSubScene());
                            newClip.updateTreeVisible(true);
                        }
                        Node.this.impl_markDirty(DirtyBits.NODE_CLIP);
                        Node.this.localBoundsChanged();
                        this.oldClip = newClip;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Node.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "clip";
                    }
                };
            }
            return this.clip;
        }

        public final Cursor getCursor() {
            return this.cursor == null ? Node.DEFAULT_CURSOR : this.cursor.get();
        }

        public final ObjectProperty<Cursor> cursorProperty() {
            if (this.cursor == null) {
                this.cursor = new StyleableObjectProperty<Cursor>(Node.DEFAULT_CURSOR) { // from class: javafx.scene.Node.MiscProperties.6
                    @Override // javafx.beans.property.ObjectPropertyBase
                    protected void invalidated() {
                        Scene sceneValue = Node.this.getScene();
                        if (sceneValue != null) {
                            sceneValue.markCursorDirty();
                        }
                    }

                    @Override // javafx.css.StyleableProperty
                    public CssMetaData getCssMetaData() {
                        return StyleableProperties.CURSOR;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Node.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "cursor";
                    }
                };
            }
            return this.cursor;
        }

        public final DepthTest getDepthTest() {
            if (this.depthTest == null) {
                return Node.DEFAULT_DEPTH_TEST;
            }
            return this.depthTest.get();
        }

        public final ObjectProperty<DepthTest> depthTestProperty() {
            if (this.depthTest == null) {
                this.depthTest = new ObjectPropertyBase<DepthTest>(Node.DEFAULT_DEPTH_TEST) { // from class: javafx.scene.Node.MiscProperties.7
                    @Override // javafx.beans.property.ObjectPropertyBase
                    protected void invalidated() {
                        Node.this.computeDerivedDepthTest();
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Node.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "depthTest";
                    }
                };
            }
            return this.depthTest;
        }

        public final boolean isDisable() {
            if (this.disable == null) {
                return false;
            }
            return this.disable.get();
        }

        public final BooleanProperty disableProperty() {
            if (this.disable == null) {
                this.disable = new BooleanPropertyBase(false) { // from class: javafx.scene.Node.MiscProperties.8
                    @Override // javafx.beans.property.BooleanPropertyBase
                    protected void invalidated() {
                        Node.this.updateDisabled();
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Node.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "disable";
                    }
                };
            }
            return this.disable;
        }

        public final Effect getEffect() {
            return this.effect == null ? Node.DEFAULT_EFFECT : this.effect.get();
        }

        public final ObjectProperty<Effect> effectProperty() {
            if (this.effect == null) {
                this.effect = new StyleableObjectProperty<Effect>(Node.DEFAULT_EFFECT) { // from class: javafx.scene.Node.MiscProperties.9
                    private int oldBits;
                    private Effect oldEffect = null;
                    private final AbstractNotifyListener effectChangeListener = new AbstractNotifyListener() { // from class: javafx.scene.Node.MiscProperties.9.1
                        @Override // javafx.beans.InvalidationListener
                        public void invalidated(Observable valueModel) {
                            int newBits = ((IntegerProperty) valueModel).get();
                            int changedBits = newBits ^ AnonymousClass9.this.oldBits;
                            AnonymousClass9.this.oldBits = newBits;
                            if (EffectDirtyBits.isSet(changedBits, EffectDirtyBits.EFFECT_DIRTY) && EffectDirtyBits.isSet(newBits, EffectDirtyBits.EFFECT_DIRTY)) {
                                Node.this.impl_markDirty(DirtyBits.EFFECT_EFFECT);
                            }
                            if (EffectDirtyBits.isSet(changedBits, EffectDirtyBits.BOUNDS_CHANGED)) {
                                Node.this.localBoundsChanged();
                            }
                        }
                    };

                    @Override // javafx.beans.property.ObjectPropertyBase
                    protected void invalidated() {
                        Effect _effect = get();
                        if (this.oldEffect != null) {
                            this.oldEffect.impl_effectDirtyProperty().removeListener(this.effectChangeListener.getWeakListener());
                        }
                        this.oldEffect = _effect;
                        if (_effect != null) {
                            _effect.impl_effectDirtyProperty().addListener(this.effectChangeListener.getWeakListener());
                            if (_effect.impl_isEffectDirty()) {
                                Node.this.impl_markDirty(DirtyBits.EFFECT_EFFECT);
                            }
                            this.oldBits = _effect.impl_effectDirtyProperty().get();
                        }
                        Node.this.impl_markDirty(DirtyBits.NODE_EFFECT);
                        Node.this.localBoundsChanged();
                    }

                    @Override // javafx.css.StyleableProperty
                    public CssMetaData getCssMetaData() {
                        return StyleableProperties.EFFECT;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Node.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "effect";
                    }
                };
            }
            return this.effect;
        }

        public final InputMethodRequests getInputMethodRequests() {
            if (this.inputMethodRequests == null) {
                return Node.DEFAULT_INPUT_METHOD_REQUESTS;
            }
            return this.inputMethodRequests.get();
        }

        public ObjectProperty<InputMethodRequests> inputMethodRequestsProperty() {
            if (this.inputMethodRequests == null) {
                this.inputMethodRequests = new SimpleObjectProperty(Node.this, "inputMethodRequests", Node.DEFAULT_INPUT_METHOD_REQUESTS);
            }
            return this.inputMethodRequests;
        }

        public final boolean isMouseTransparent() {
            if (this.mouseTransparent == null) {
                return false;
            }
            return this.mouseTransparent.get();
        }

        public final BooleanProperty mouseTransparentProperty() {
            if (this.mouseTransparent == null) {
                this.mouseTransparent = new SimpleBooleanProperty(Node.this, "mouseTransparent", false);
            }
            return this.mouseTransparent;
        }

        public boolean canSetCursor() {
            return this.cursor == null || !this.cursor.isBound();
        }

        public boolean canSetEffect() {
            return this.effect == null || !this.effect.isBound();
        }
    }

    public final void setMouseTransparent(boolean value) {
        mouseTransparentProperty().set(value);
    }

    public final boolean isMouseTransparent() {
        if (this.miscProperties == null) {
            return false;
        }
        return this.miscProperties.isMouseTransparent();
    }

    public final BooleanProperty mouseTransparentProperty() {
        return getMiscProperties().mouseTransparentProperty();
    }

    protected final void setHover(boolean value) {
        hoverPropertyImpl().set(value);
    }

    public final boolean isHover() {
        if (this.hover == null) {
            return false;
        }
        return this.hover.get();
    }

    public final ReadOnlyBooleanProperty hoverProperty() {
        return hoverPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyBooleanWrapper hoverPropertyImpl() {
        if (this.hover == null) {
            this.hover = new ReadOnlyBooleanWrapper() { // from class: javafx.scene.Node.14
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    PlatformLogger logger = Logging.getInputLogger();
                    if (logger.isLoggable(PlatformLogger.Level.FINER)) {
                        logger.finer(((Object) this) + " hover=" + get());
                    }
                    Node.this.pseudoClassStateChanged(Node.HOVER_PSEUDOCLASS_STATE, get());
                }

                @Override // javafx.beans.property.SimpleBooleanProperty, javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Node.this;
                }

                @Override // javafx.beans.property.SimpleBooleanProperty, javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "hover";
                }
            };
        }
        return this.hover;
    }

    protected final void setPressed(boolean value) {
        pressedPropertyImpl().set(value);
    }

    public final boolean isPressed() {
        if (this.pressed == null) {
            return false;
        }
        return this.pressed.get();
    }

    public final ReadOnlyBooleanProperty pressedProperty() {
        return pressedPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyBooleanWrapper pressedPropertyImpl() {
        if (this.pressed == null) {
            this.pressed = new ReadOnlyBooleanWrapper() { // from class: javafx.scene.Node.15
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    PlatformLogger logger = Logging.getInputLogger();
                    if (logger.isLoggable(PlatformLogger.Level.FINER)) {
                        logger.finer(((Object) this) + " pressed=" + get());
                    }
                    Node.this.pseudoClassStateChanged(Node.PRESSED_PSEUDOCLASS_STATE, get());
                }

                @Override // javafx.beans.property.SimpleBooleanProperty, javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Node.this;
                }

                @Override // javafx.beans.property.SimpleBooleanProperty, javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "pressed";
                }
            };
        }
        return this.pressed;
    }

    public final void setOnContextMenuRequested(EventHandler<? super ContextMenuEvent> value) {
        onContextMenuRequestedProperty().set(value);
    }

    public final EventHandler<? super ContextMenuEvent> getOnContextMenuRequested() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.onContextMenuRequested();
    }

    public final ObjectProperty<EventHandler<? super ContextMenuEvent>> onContextMenuRequestedProperty() {
        return getEventHandlerProperties().onContextMenuRequestedProperty();
    }

    public final void setOnMouseClicked(EventHandler<? super MouseEvent> value) {
        onMouseClickedProperty().set(value);
    }

    public final EventHandler<? super MouseEvent> getOnMouseClicked() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnMouseClicked();
    }

    public final ObjectProperty<EventHandler<? super MouseEvent>> onMouseClickedProperty() {
        return getEventHandlerProperties().onMouseClickedProperty();
    }

    public final void setOnMouseDragged(EventHandler<? super MouseEvent> value) {
        onMouseDraggedProperty().set(value);
    }

    public final EventHandler<? super MouseEvent> getOnMouseDragged() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnMouseDragged();
    }

    public final ObjectProperty<EventHandler<? super MouseEvent>> onMouseDraggedProperty() {
        return getEventHandlerProperties().onMouseDraggedProperty();
    }

    public final void setOnMouseEntered(EventHandler<? super MouseEvent> value) {
        onMouseEnteredProperty().set(value);
    }

    public final EventHandler<? super MouseEvent> getOnMouseEntered() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnMouseEntered();
    }

    public final ObjectProperty<EventHandler<? super MouseEvent>> onMouseEnteredProperty() {
        return getEventHandlerProperties().onMouseEnteredProperty();
    }

    public final void setOnMouseExited(EventHandler<? super MouseEvent> value) {
        onMouseExitedProperty().set(value);
    }

    public final EventHandler<? super MouseEvent> getOnMouseExited() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnMouseExited();
    }

    public final ObjectProperty<EventHandler<? super MouseEvent>> onMouseExitedProperty() {
        return getEventHandlerProperties().onMouseExitedProperty();
    }

    public final void setOnMouseMoved(EventHandler<? super MouseEvent> value) {
        onMouseMovedProperty().set(value);
    }

    public final EventHandler<? super MouseEvent> getOnMouseMoved() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnMouseMoved();
    }

    public final ObjectProperty<EventHandler<? super MouseEvent>> onMouseMovedProperty() {
        return getEventHandlerProperties().onMouseMovedProperty();
    }

    public final void setOnMousePressed(EventHandler<? super MouseEvent> value) {
        onMousePressedProperty().set(value);
    }

    public final EventHandler<? super MouseEvent> getOnMousePressed() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnMousePressed();
    }

    public final ObjectProperty<EventHandler<? super MouseEvent>> onMousePressedProperty() {
        return getEventHandlerProperties().onMousePressedProperty();
    }

    public final void setOnMouseReleased(EventHandler<? super MouseEvent> value) {
        onMouseReleasedProperty().set(value);
    }

    public final EventHandler<? super MouseEvent> getOnMouseReleased() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnMouseReleased();
    }

    public final ObjectProperty<EventHandler<? super MouseEvent>> onMouseReleasedProperty() {
        return getEventHandlerProperties().onMouseReleasedProperty();
    }

    public final void setOnDragDetected(EventHandler<? super MouseEvent> value) {
        onDragDetectedProperty().set(value);
    }

    public final EventHandler<? super MouseEvent> getOnDragDetected() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnDragDetected();
    }

    public final ObjectProperty<EventHandler<? super MouseEvent>> onDragDetectedProperty() {
        return getEventHandlerProperties().onDragDetectedProperty();
    }

    public final void setOnMouseDragOver(EventHandler<? super MouseDragEvent> value) {
        onMouseDragOverProperty().set(value);
    }

    public final EventHandler<? super MouseDragEvent> getOnMouseDragOver() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnMouseDragOver();
    }

    public final ObjectProperty<EventHandler<? super MouseDragEvent>> onMouseDragOverProperty() {
        return getEventHandlerProperties().onMouseDragOverProperty();
    }

    public final void setOnMouseDragReleased(EventHandler<? super MouseDragEvent> value) {
        onMouseDragReleasedProperty().set(value);
    }

    public final EventHandler<? super MouseDragEvent> getOnMouseDragReleased() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnMouseDragReleased();
    }

    public final ObjectProperty<EventHandler<? super MouseDragEvent>> onMouseDragReleasedProperty() {
        return getEventHandlerProperties().onMouseDragReleasedProperty();
    }

    public final void setOnMouseDragEntered(EventHandler<? super MouseDragEvent> value) {
        onMouseDragEnteredProperty().set(value);
    }

    public final EventHandler<? super MouseDragEvent> getOnMouseDragEntered() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnMouseDragEntered();
    }

    public final ObjectProperty<EventHandler<? super MouseDragEvent>> onMouseDragEnteredProperty() {
        return getEventHandlerProperties().onMouseDragEnteredProperty();
    }

    public final void setOnMouseDragExited(EventHandler<? super MouseDragEvent> value) {
        onMouseDragExitedProperty().set(value);
    }

    public final EventHandler<? super MouseDragEvent> getOnMouseDragExited() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnMouseDragExited();
    }

    public final ObjectProperty<EventHandler<? super MouseDragEvent>> onMouseDragExitedProperty() {
        return getEventHandlerProperties().onMouseDragExitedProperty();
    }

    public final void setOnScrollStarted(EventHandler<? super ScrollEvent> value) {
        onScrollStartedProperty().set(value);
    }

    public final EventHandler<? super ScrollEvent> getOnScrollStarted() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnScrollStarted();
    }

    public final ObjectProperty<EventHandler<? super ScrollEvent>> onScrollStartedProperty() {
        return getEventHandlerProperties().onScrollStartedProperty();
    }

    public final void setOnScroll(EventHandler<? super ScrollEvent> value) {
        onScrollProperty().set(value);
    }

    public final EventHandler<? super ScrollEvent> getOnScroll() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnScroll();
    }

    public final ObjectProperty<EventHandler<? super ScrollEvent>> onScrollProperty() {
        return getEventHandlerProperties().onScrollProperty();
    }

    public final void setOnScrollFinished(EventHandler<? super ScrollEvent> value) {
        onScrollFinishedProperty().set(value);
    }

    public final EventHandler<? super ScrollEvent> getOnScrollFinished() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnScrollFinished();
    }

    public final ObjectProperty<EventHandler<? super ScrollEvent>> onScrollFinishedProperty() {
        return getEventHandlerProperties().onScrollFinishedProperty();
    }

    public final void setOnRotationStarted(EventHandler<? super RotateEvent> value) {
        onRotationStartedProperty().set(value);
    }

    public final EventHandler<? super RotateEvent> getOnRotationStarted() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnRotationStarted();
    }

    public final ObjectProperty<EventHandler<? super RotateEvent>> onRotationStartedProperty() {
        return getEventHandlerProperties().onRotationStartedProperty();
    }

    public final void setOnRotate(EventHandler<? super RotateEvent> value) {
        onRotateProperty().set(value);
    }

    public final EventHandler<? super RotateEvent> getOnRotate() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnRotate();
    }

    public final ObjectProperty<EventHandler<? super RotateEvent>> onRotateProperty() {
        return getEventHandlerProperties().onRotateProperty();
    }

    public final void setOnRotationFinished(EventHandler<? super RotateEvent> value) {
        onRotationFinishedProperty().set(value);
    }

    public final EventHandler<? super RotateEvent> getOnRotationFinished() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnRotationFinished();
    }

    public final ObjectProperty<EventHandler<? super RotateEvent>> onRotationFinishedProperty() {
        return getEventHandlerProperties().onRotationFinishedProperty();
    }

    public final void setOnZoomStarted(EventHandler<? super ZoomEvent> value) {
        onZoomStartedProperty().set(value);
    }

    public final EventHandler<? super ZoomEvent> getOnZoomStarted() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnZoomStarted();
    }

    public final ObjectProperty<EventHandler<? super ZoomEvent>> onZoomStartedProperty() {
        return getEventHandlerProperties().onZoomStartedProperty();
    }

    public final void setOnZoom(EventHandler<? super ZoomEvent> value) {
        onZoomProperty().set(value);
    }

    public final EventHandler<? super ZoomEvent> getOnZoom() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnZoom();
    }

    public final ObjectProperty<EventHandler<? super ZoomEvent>> onZoomProperty() {
        return getEventHandlerProperties().onZoomProperty();
    }

    public final void setOnZoomFinished(EventHandler<? super ZoomEvent> value) {
        onZoomFinishedProperty().set(value);
    }

    public final EventHandler<? super ZoomEvent> getOnZoomFinished() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnZoomFinished();
    }

    public final ObjectProperty<EventHandler<? super ZoomEvent>> onZoomFinishedProperty() {
        return getEventHandlerProperties().onZoomFinishedProperty();
    }

    public final void setOnSwipeUp(EventHandler<? super SwipeEvent> value) {
        onSwipeUpProperty().set(value);
    }

    public final EventHandler<? super SwipeEvent> getOnSwipeUp() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnSwipeUp();
    }

    public final ObjectProperty<EventHandler<? super SwipeEvent>> onSwipeUpProperty() {
        return getEventHandlerProperties().onSwipeUpProperty();
    }

    public final void setOnSwipeDown(EventHandler<? super SwipeEvent> value) {
        onSwipeDownProperty().set(value);
    }

    public final EventHandler<? super SwipeEvent> getOnSwipeDown() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnSwipeDown();
    }

    public final ObjectProperty<EventHandler<? super SwipeEvent>> onSwipeDownProperty() {
        return getEventHandlerProperties().onSwipeDownProperty();
    }

    public final void setOnSwipeLeft(EventHandler<? super SwipeEvent> value) {
        onSwipeLeftProperty().set(value);
    }

    public final EventHandler<? super SwipeEvent> getOnSwipeLeft() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnSwipeLeft();
    }

    public final ObjectProperty<EventHandler<? super SwipeEvent>> onSwipeLeftProperty() {
        return getEventHandlerProperties().onSwipeLeftProperty();
    }

    public final void setOnSwipeRight(EventHandler<? super SwipeEvent> value) {
        onSwipeRightProperty().set(value);
    }

    public final EventHandler<? super SwipeEvent> getOnSwipeRight() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnSwipeRight();
    }

    public final ObjectProperty<EventHandler<? super SwipeEvent>> onSwipeRightProperty() {
        return getEventHandlerProperties().onSwipeRightProperty();
    }

    public final void setOnTouchPressed(EventHandler<? super TouchEvent> value) {
        onTouchPressedProperty().set(value);
    }

    public final EventHandler<? super TouchEvent> getOnTouchPressed() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnTouchPressed();
    }

    public final ObjectProperty<EventHandler<? super TouchEvent>> onTouchPressedProperty() {
        return getEventHandlerProperties().onTouchPressedProperty();
    }

    public final void setOnTouchMoved(EventHandler<? super TouchEvent> value) {
        onTouchMovedProperty().set(value);
    }

    public final EventHandler<? super TouchEvent> getOnTouchMoved() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnTouchMoved();
    }

    public final ObjectProperty<EventHandler<? super TouchEvent>> onTouchMovedProperty() {
        return getEventHandlerProperties().onTouchMovedProperty();
    }

    public final void setOnTouchReleased(EventHandler<? super TouchEvent> value) {
        onTouchReleasedProperty().set(value);
    }

    public final EventHandler<? super TouchEvent> getOnTouchReleased() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnTouchReleased();
    }

    public final ObjectProperty<EventHandler<? super TouchEvent>> onTouchReleasedProperty() {
        return getEventHandlerProperties().onTouchReleasedProperty();
    }

    public final void setOnTouchStationary(EventHandler<? super TouchEvent> value) {
        onTouchStationaryProperty().set(value);
    }

    public final EventHandler<? super TouchEvent> getOnTouchStationary() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnTouchStationary();
    }

    public final ObjectProperty<EventHandler<? super TouchEvent>> onTouchStationaryProperty() {
        return getEventHandlerProperties().onTouchStationaryProperty();
    }

    public final void setOnKeyPressed(EventHandler<? super KeyEvent> value) {
        onKeyPressedProperty().set(value);
    }

    public final EventHandler<? super KeyEvent> getOnKeyPressed() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnKeyPressed();
    }

    public final ObjectProperty<EventHandler<? super KeyEvent>> onKeyPressedProperty() {
        return getEventHandlerProperties().onKeyPressedProperty();
    }

    public final void setOnKeyReleased(EventHandler<? super KeyEvent> value) {
        onKeyReleasedProperty().set(value);
    }

    public final EventHandler<? super KeyEvent> getOnKeyReleased() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnKeyReleased();
    }

    public final ObjectProperty<EventHandler<? super KeyEvent>> onKeyReleasedProperty() {
        return getEventHandlerProperties().onKeyReleasedProperty();
    }

    public final void setOnKeyTyped(EventHandler<? super KeyEvent> value) {
        onKeyTypedProperty().set(value);
    }

    public final EventHandler<? super KeyEvent> getOnKeyTyped() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnKeyTyped();
    }

    public final ObjectProperty<EventHandler<? super KeyEvent>> onKeyTypedProperty() {
        return getEventHandlerProperties().onKeyTypedProperty();
    }

    public final void setOnInputMethodTextChanged(EventHandler<? super InputMethodEvent> value) {
        onInputMethodTextChangedProperty().set(value);
    }

    public final EventHandler<? super InputMethodEvent> getOnInputMethodTextChanged() {
        if (this.eventHandlerProperties == null) {
            return null;
        }
        return this.eventHandlerProperties.getOnInputMethodTextChanged();
    }

    public final ObjectProperty<EventHandler<? super InputMethodEvent>> onInputMethodTextChangedProperty() {
        return getEventHandlerProperties().onInputMethodTextChangedProperty();
    }

    public final void setInputMethodRequests(InputMethodRequests value) {
        inputMethodRequestsProperty().set(value);
    }

    public final InputMethodRequests getInputMethodRequests() {
        return this.miscProperties == null ? DEFAULT_INPUT_METHOD_REQUESTS : this.miscProperties.getInputMethodRequests();
    }

    public final ObjectProperty<InputMethodRequests> inputMethodRequestsProperty() {
        return getMiscProperties().inputMethodRequestsProperty();
    }

    /* loaded from: jfxrt.jar:javafx/scene/Node$FocusedProperty.class */
    final class FocusedProperty extends ReadOnlyBooleanPropertyBase {
        private boolean value;
        private boolean valid = true;
        private boolean needsChangeEvent = false;

        FocusedProperty() {
        }

        public void store(boolean value) {
            if (value != this.value) {
                this.value = value;
                markInvalid();
            }
        }

        public void notifyListeners() {
            if (this.needsChangeEvent) {
                fireValueChangedEvent();
                this.needsChangeEvent = false;
            }
        }

        private void markInvalid() {
            if (this.valid) {
                this.valid = false;
                Node.this.pseudoClassStateChanged(Node.FOCUSED_PSEUDOCLASS_STATE, get());
                PlatformLogger logger = Logging.getFocusLogger();
                if (logger.isLoggable(PlatformLogger.Level.FINE)) {
                    logger.fine(((Object) this) + " focused=" + get());
                }
                this.needsChangeEvent = true;
                Node.this.notifyAccessibleAttributeChanged(AccessibleAttribute.FOCUSED);
            }
        }

        @Override // javafx.beans.value.ObservableBooleanValue
        public boolean get() {
            this.valid = true;
            return this.value;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Node.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "focused";
        }
    }

    protected final void setFocused(boolean value) {
        FocusedProperty fp = focusedPropertyImpl();
        if (fp.value != value) {
            fp.store(value);
            fp.notifyListeners();
        }
    }

    public final boolean isFocused() {
        if (this.focused == null) {
            return false;
        }
        return this.focused.get();
    }

    public final ReadOnlyBooleanProperty focusedProperty() {
        return focusedPropertyImpl();
    }

    private FocusedProperty focusedPropertyImpl() {
        if (this.focused == null) {
            this.focused = new FocusedProperty();
        }
        return this.focused;
    }

    public final void setFocusTraversable(boolean value) {
        focusTraversableProperty().set(value);
    }

    public final boolean isFocusTraversable() {
        if (this.focusTraversable == null) {
            return false;
        }
        return this.focusTraversable.get();
    }

    public final BooleanProperty focusTraversableProperty() {
        if (this.focusTraversable == null) {
            this.focusTraversable = new StyleableBooleanProperty(false) { // from class: javafx.scene.Node.16
                @Override // javafx.beans.property.BooleanPropertyBase
                public void invalidated() {
                    Scene _scene = Node.this.getScene();
                    if (_scene != null) {
                        if (get()) {
                            _scene.initializeInternalEventDispatcher();
                        }
                        Node.this.focusSetDirty(_scene);
                    }
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                    return StyleableProperties.FOCUS_TRAVERSABLE;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Node.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "focusTraversable";
                }
            };
        }
        return this.focusTraversable;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void focusSetDirty(Scene s2) {
        if (s2 != null) {
            if (this == s2.getFocusOwner() || isFocusTraversable()) {
                s2.setFocusDirty(true);
            }
        }
    }

    public void requestFocus() {
        if (getScene() != null) {
            getScene().requestFocus(this);
        }
    }

    @Deprecated
    public final boolean impl_traverse(Direction dir) {
        if (getScene() == null) {
            return false;
        }
        return getScene().traverse(this, dir);
    }

    public String toString() {
        String klassName = getClass().getName();
        String simpleName = klassName.substring(klassName.lastIndexOf(46) + 1);
        StringBuilder sbuf = new StringBuilder(simpleName);
        boolean hasId = (this.id == null || "".equals(getId())) ? false : true;
        boolean hasStyleClass = !getStyleClass().isEmpty();
        if (!hasId) {
            sbuf.append('@');
            sbuf.append(Integer.toHexString(hashCode()));
        } else {
            sbuf.append("[id=");
            sbuf.append(getId());
            if (!hasStyleClass) {
                sbuf.append("]");
            }
        }
        if (hasStyleClass) {
            if (hasId) {
                sbuf.append(", ");
            } else {
                sbuf.append('[');
            }
            sbuf.append("styleClass=");
            sbuf.append((Object) getStyleClass());
            sbuf.append("]");
        }
        return sbuf.toString();
    }

    private void preprocessMouseEvent(MouseEvent e2) {
        EventType<?> eventType = e2.getEventType();
        if (eventType == MouseEvent.MOUSE_PRESSED) {
            Node parent = this;
            while (true) {
                Node n2 = parent;
                if (n2 != null) {
                    n2.setPressed(e2.isPrimaryButtonDown());
                    parent = n2.getParent();
                } else {
                    return;
                }
            }
        } else if (eventType == MouseEvent.MOUSE_RELEASED) {
            Node parent2 = this;
            while (true) {
                Node n3 = parent2;
                if (n3 != null) {
                    n3.setPressed(e2.isPrimaryButtonDown());
                    parent2 = n3.getParent();
                } else {
                    return;
                }
            }
        } else if (e2.getTarget() == this) {
            if (eventType == MouseEvent.MOUSE_ENTERED || eventType == MouseEvent.MOUSE_ENTERED_TARGET) {
                setHover(true);
            } else if (eventType == MouseEvent.MOUSE_EXITED || eventType == MouseEvent.MOUSE_EXITED_TARGET) {
                setHover(false);
            }
        }
    }

    void markDirtyLayoutBranch() {
        Parent parent = getParent();
        while (true) {
            Parent p2 = parent;
            if (p2 != null && p2.layoutFlag == LayoutFlags.CLEAN) {
                p2.setLayoutFlag(LayoutFlags.DIRTY_BRANCH);
                if (p2.isSceneRoot()) {
                    Toolkit.getToolkit().requestNextPulse();
                    if (getSubScene() != null) {
                        getSubScene().setDirtyLayout(p2);
                    }
                }
                parent = p2.getParent();
            } else {
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTreeVisible(boolean parentChanged) {
        Node subScene;
        boolean isTreeVisible = isVisible();
        if (getParent() != null) {
            subScene = getParent();
        } else if (this.clipParent != null) {
            subScene = this.clipParent;
        } else {
            subScene = getSubScene() != null ? getSubScene() : null;
        }
        Node parentNode = subScene;
        if (isTreeVisible) {
            isTreeVisible = parentNode == null || parentNode.impl_isTreeVisible();
        }
        if (parentChanged && parentNode != null && parentNode.impl_isTreeVisible() && impl_isDirty(DirtyBits.NODE_VISIBLE)) {
            addToSceneDirtyList();
        }
        setTreeVisible(isTreeVisible);
    }

    final void setTreeVisible(boolean value) {
        Node subSceneRoot;
        if (this.treeVisible != value) {
            this.treeVisible = value;
            updateCanReceiveFocus();
            focusSetDirty(getScene());
            if (getClip() != null) {
                getClip().updateTreeVisible(true);
            }
            if (this.treeVisible && !impl_isDirtyEmpty()) {
                addToSceneDirtyList();
            }
            ((TreeVisiblePropertyReadOnly) impl_treeVisibleProperty()).invalidate();
            if ((this instanceof SubScene) && (subSceneRoot = ((SubScene) this).getRoot()) != null) {
                subSceneRoot.setTreeVisible(value && subSceneRoot.isVisible());
            }
        }
    }

    @Deprecated
    public final boolean impl_isTreeVisible() {
        return impl_treeVisibleProperty().get();
    }

    @Deprecated
    protected final BooleanExpression impl_treeVisibleProperty() {
        if (this.treeVisibleRO == null) {
            this.treeVisibleRO = new TreeVisiblePropertyReadOnly();
        }
        return this.treeVisibleRO;
    }

    /* loaded from: jfxrt.jar:javafx/scene/Node$TreeVisiblePropertyReadOnly.class */
    class TreeVisiblePropertyReadOnly extends BooleanExpression {
        private ExpressionHelper<Boolean> helper;
        private boolean valid;

        TreeVisiblePropertyReadOnly() {
        }

        @Override // javafx.beans.Observable
        public void addListener(InvalidationListener listener) {
            this.helper = ExpressionHelper.addListener(this.helper, this, listener);
        }

        @Override // javafx.beans.Observable
        public void removeListener(InvalidationListener listener) {
            this.helper = ExpressionHelper.removeListener(this.helper, listener);
        }

        @Override // javafx.beans.value.ObservableValue
        public void addListener(ChangeListener<? super Boolean> listener) {
            this.helper = ExpressionHelper.addListener(this.helper, this, listener);
        }

        @Override // javafx.beans.value.ObservableValue
        public void removeListener(ChangeListener<? super Boolean> listener) {
            this.helper = ExpressionHelper.removeListener(this.helper, listener);
        }

        protected void invalidate() {
            if (this.valid) {
                this.valid = false;
                ExpressionHelper.fireValueChangedEvent(this.helper);
            }
        }

        @Override // javafx.beans.value.ObservableBooleanValue
        public boolean get() {
            this.valid = true;
            return Node.this.treeVisible;
        }
    }

    private void setCanReceiveFocus(boolean value) {
        this.canReceiveFocus = value;
    }

    final boolean isCanReceiveFocus() {
        return this.canReceiveFocus;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCanReceiveFocus() {
        setCanReceiveFocus((getScene() == null || isDisabled() || !impl_isTreeVisible()) ? false : true);
    }

    String indent() {
        String indent = "";
        Parent parent = getParent();
        while (true) {
            Parent p2 = parent;
            if (p2 != null) {
                indent = indent + sun.security.pkcs11.wrapper.Constants.INDENT;
                parent = p2.getParent();
            } else {
                return indent;
            }
        }
    }

    @Deprecated
    public final void impl_setShowMnemonics(boolean value) {
        impl_showMnemonicsProperty().set(value);
    }

    @Deprecated
    public final boolean impl_isShowMnemonics() {
        if (this.impl_showMnemonics == null) {
            return false;
        }
        return this.impl_showMnemonics.get();
    }

    @Deprecated
    public final BooleanProperty impl_showMnemonicsProperty() {
        if (this.impl_showMnemonics == null) {
            this.impl_showMnemonics = new BooleanPropertyBase(false) { // from class: javafx.scene.Node.17
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    Node.this.pseudoClassStateChanged(Node.SHOW_MNEMONICS_PSEUDOCLASS_STATE, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Node.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "showMnemonics";
                }
            };
        }
        return this.impl_showMnemonics;
    }

    public final void setEventDispatcher(EventDispatcher value) {
        eventDispatcherProperty().set(value);
    }

    public final EventDispatcher getEventDispatcher() {
        return eventDispatcherProperty().get();
    }

    public final ObjectProperty<EventDispatcher> eventDispatcherProperty() {
        initializeInternalEventDispatcher();
        return this.eventDispatcher;
    }

    public final <T extends Event> void addEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        getInternalEventDispatcher().getEventHandlerManager().addEventHandler(eventType, eventHandler);
    }

    public final <T extends Event> void removeEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        getInternalEventDispatcher().getEventHandlerManager().removeEventHandler(eventType, eventHandler);
    }

    public final <T extends Event> void addEventFilter(EventType<T> eventType, EventHandler<? super T> eventFilter) {
        getInternalEventDispatcher().getEventHandlerManager().addEventFilter(eventType, eventFilter);
    }

    public final <T extends Event> void removeEventFilter(EventType<T> eventType, EventHandler<? super T> eventFilter) {
        getInternalEventDispatcher().getEventHandlerManager().removeEventFilter(eventType, eventFilter);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final <T extends Event> void setEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        getInternalEventDispatcher().getEventHandlerManager().setEventHandler(eventType, eventHandler);
    }

    private NodeEventDispatcher getInternalEventDispatcher() {
        initializeInternalEventDispatcher();
        return this.internalEventDispatcher;
    }

    private void initializeInternalEventDispatcher() {
        if (this.internalEventDispatcher == null) {
            this.internalEventDispatcher = createInternalEventDispatcher();
            this.eventDispatcher = new SimpleObjectProperty(this, "eventDispatcher", this.internalEventDispatcher);
        }
    }

    private NodeEventDispatcher createInternalEventDispatcher() {
        return new NodeEventDispatcher(this);
    }

    @Override // javafx.event.EventTarget
    public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
        EventDispatcher eventDispatcherValue;
        if (this.preprocessMouseEventDispatcher == null) {
            this.preprocessMouseEventDispatcher = (event, tail1) -> {
                Event event = tail1.dispatchEvent(event);
                if (event instanceof MouseEvent) {
                    preprocessMouseEvent((MouseEvent) event);
                }
                return event;
            };
        }
        EventDispatchChain tail2 = tail.prepend(this.preprocessMouseEventDispatcher);
        Node curNode = this;
        do {
            if (curNode.eventDispatcher != null && (eventDispatcherValue = curNode.eventDispatcher.get()) != null) {
                tail2 = tail2.prepend(eventDispatcherValue);
            }
            Node curParent = curNode.getParent();
            curNode = curParent != null ? curParent : curNode.getSubScene();
        } while (curNode != null);
        if (getScene() != null) {
            tail2 = getScene().buildEventDispatchChain(tail2);
        }
        return tail2;
    }

    public final void fireEvent(Event event) {
        if (event instanceof InputEvent) {
            PlatformLogger logger = Logging.getInputLogger();
            if (logger.isLoggable(PlatformLogger.Level.FINE)) {
                EventType eventType = event.getEventType();
                if (eventType == MouseEvent.MOUSE_ENTERED || eventType == MouseEvent.MOUSE_EXITED) {
                    logger.finer(event.toString());
                } else if (eventType == MouseEvent.MOUSE_MOVED || eventType == MouseEvent.MOUSE_DRAGGED) {
                    logger.finest(event.toString());
                } else {
                    logger.fine(event.toString());
                }
            }
        }
        Event.fireEvent(this, event);
    }

    @Override // javafx.css.Styleable
    public String getTypeSelector() {
        Class<?> clazz = getClass();
        Package pkg = clazz.getPackage();
        int plen = 0;
        if (pkg != null) {
            plen = pkg.getName().length();
        }
        int clen = clazz.getName().length();
        int pos = (0 >= plen || plen >= clen) ? 0 : plen + 1;
        return clazz.getName().substring(pos);
    }

    public Styleable getStyleableParent() {
        return getParent();
    }

    @Deprecated
    protected Boolean impl_cssGetFocusTraversableInitialValue() {
        return Boolean.FALSE;
    }

    @Deprecated
    protected Cursor impl_cssGetCursorInitialValue() {
        return null;
    }

    /* loaded from: jfxrt.jar:javafx/scene/Node$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<Node, Cursor> CURSOR = new CssMetaData<Node, Cursor>("-fx-cursor", CursorConverter.getInstance()) { // from class: javafx.scene.Node.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Node node) {
                return node.miscProperties == null || node.miscProperties.canSetCursor();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Cursor> getStyleableProperty(Node node) {
                return (StyleableProperty) node.cursorProperty();
            }

            @Override // javafx.css.CssMetaData
            public Cursor getInitialValue(Node node) {
                return node.impl_cssGetCursorInitialValue();
            }
        };
        private static final CssMetaData<Node, Effect> EFFECT = new CssMetaData<Node, Effect>("-fx-effect", EffectConverter.getInstance()) { // from class: javafx.scene.Node.StyleableProperties.2
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Node node) {
                return node.miscProperties == null || node.miscProperties.canSetEffect();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Effect> getStyleableProperty(Node node) {
                return (StyleableProperty) node.effectProperty();
            }
        };
        private static final CssMetaData<Node, Boolean> FOCUS_TRAVERSABLE = new CssMetaData<Node, Boolean>("-fx-focus-traversable", BooleanConverter.getInstance(), Boolean.FALSE) { // from class: javafx.scene.Node.StyleableProperties.3
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Node node) {
                return node.focusTraversable == null || !node.focusTraversable.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(Node node) {
                return (StyleableProperty) node.focusTraversableProperty();
            }

            @Override // javafx.css.CssMetaData
            public Boolean getInitialValue(Node node) {
                return node.impl_cssGetFocusTraversableInitialValue();
            }
        };
        private static final CssMetaData<Node, Number> OPACITY = new CssMetaData<Node, Number>("-fx-opacity", SizeConverter.getInstance(), Double.valueOf(1.0d)) { // from class: javafx.scene.Node.StyleableProperties.4
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Node node) {
                return node.opacity == null || !node.opacity.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(Node node) {
                return (StyleableProperty) node.opacityProperty();
            }
        };
        private static final CssMetaData<Node, BlendMode> BLEND_MODE = new CssMetaData<Node, BlendMode>("-fx-blend-mode", new EnumConverter(BlendMode.class)) { // from class: javafx.scene.Node.StyleableProperties.5
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Node node) {
                return node.blendMode == null || !node.blendMode.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<BlendMode> getStyleableProperty(Node node) {
                return (StyleableProperty) node.blendModeProperty();
            }
        };
        private static final CssMetaData<Node, Number> ROTATE = new CssMetaData<Node, Number>("-fx-rotate", SizeConverter.getInstance(), Double.valueOf(0.0d)) { // from class: javafx.scene.Node.StyleableProperties.6
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Node node) {
                return node.nodeTransformation == null || node.nodeTransformation.rotate == null || node.nodeTransformation.canSetRotate();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(Node node) {
                return (StyleableProperty) node.rotateProperty();
            }
        };
        private static final CssMetaData<Node, Number> SCALE_X = new CssMetaData<Node, Number>("-fx-scale-x", SizeConverter.getInstance(), Double.valueOf(1.0d)) { // from class: javafx.scene.Node.StyleableProperties.7
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Node node) {
                return node.nodeTransformation == null || node.nodeTransformation.scaleX == null || node.nodeTransformation.canSetScaleX();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(Node node) {
                return (StyleableProperty) node.scaleXProperty();
            }
        };
        private static final CssMetaData<Node, Number> SCALE_Y = new CssMetaData<Node, Number>("-fx-scale-y", SizeConverter.getInstance(), Double.valueOf(1.0d)) { // from class: javafx.scene.Node.StyleableProperties.8
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Node node) {
                return node.nodeTransformation == null || node.nodeTransformation.scaleY == null || node.nodeTransformation.canSetScaleY();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(Node node) {
                return (StyleableProperty) node.scaleYProperty();
            }
        };
        private static final CssMetaData<Node, Number> SCALE_Z = new CssMetaData<Node, Number>("-fx-scale-z", SizeConverter.getInstance(), Double.valueOf(1.0d)) { // from class: javafx.scene.Node.StyleableProperties.9
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Node node) {
                return node.nodeTransformation == null || node.nodeTransformation.scaleZ == null || node.nodeTransformation.canSetScaleZ();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(Node node) {
                return (StyleableProperty) node.scaleZProperty();
            }
        };
        private static final CssMetaData<Node, Number> TRANSLATE_X = new CssMetaData<Node, Number>("-fx-translate-x", SizeConverter.getInstance(), Double.valueOf(0.0d)) { // from class: javafx.scene.Node.StyleableProperties.10
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Node node) {
                return node.nodeTransformation == null || node.nodeTransformation.translateX == null || node.nodeTransformation.canSetTranslateX();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(Node node) {
                return (StyleableProperty) node.translateXProperty();
            }
        };
        private static final CssMetaData<Node, Number> TRANSLATE_Y = new CssMetaData<Node, Number>("-fx-translate-y", SizeConverter.getInstance(), Double.valueOf(0.0d)) { // from class: javafx.scene.Node.StyleableProperties.11
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Node node) {
                return node.nodeTransformation == null || node.nodeTransformation.translateY == null || node.nodeTransformation.canSetTranslateY();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(Node node) {
                return (StyleableProperty) node.translateYProperty();
            }
        };
        private static final CssMetaData<Node, Number> TRANSLATE_Z = new CssMetaData<Node, Number>("-fx-translate-z", SizeConverter.getInstance(), Double.valueOf(0.0d)) { // from class: javafx.scene.Node.StyleableProperties.12
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Node node) {
                return node.nodeTransformation == null || node.nodeTransformation.translateZ == null || node.nodeTransformation.canSetTranslateZ();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(Node node) {
                return (StyleableProperty) node.translateZProperty();
            }
        };
        private static final CssMetaData<Node, Boolean> VISIBILITY = new CssMetaData<Node, Boolean>("visibility", new StyleConverter<String, Boolean>() { // from class: javafx.scene.Node.StyleableProperties.13
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // javafx.css.StyleConverter
            public Boolean convert(ParsedValue<String, Boolean> value, Font font) {
                String sval = value != null ? value.getValue() : null;
                return Boolean.valueOf("visible".equalsIgnoreCase(sval));
            }
        }, Boolean.TRUE) { // from class: javafx.scene.Node.StyleableProperties.14
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Node node) {
                return node.visible == null || !node.visible.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(Node node) {
                return (StyleableProperty) node.visibleProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>();
            styleables.add(CURSOR);
            styleables.add(EFFECT);
            styleables.add(FOCUS_TRAVERSABLE);
            styleables.add(OPACITY);
            styleables.add(BLEND_MODE);
            styleables.add(ROTATE);
            styleables.add(SCALE_X);
            styleables.add(SCALE_Y);
            styleables.add(SCALE_Z);
            styleables.add(TRANSLATE_X);
            styleables.add(TRANSLATE_Y);
            styleables.add(TRANSLATE_Z);
            styleables.add(VISIBILITY);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

    @Deprecated
    public static List<Style> impl_getMatchingStyles(CssMetaData cssMetaData, Styleable styleable) {
        return CssStyleHelper.getMatchingStyles(styleable, cssMetaData);
    }

    @Deprecated
    public final ObservableMap<StyleableProperty<?>, List<Style>> impl_getStyleMap() {
        ObservableMap<StyleableProperty<?>, List<Style>> map = (ObservableMap) getProperties().get("STYLEMAP");
        Map<StyleableProperty<?>, List<Style>> ret = CssStyleHelper.getMatchingStyles(map, this);
        if (ret != null) {
            return ret instanceof ObservableMap ? (ObservableMap) ret : FXCollections.observableMap(ret);
        }
        return FXCollections.emptyObservableMap();
    }

    @Deprecated
    public final void impl_setStyleMap(ObservableMap<StyleableProperty<?>, List<Style>> styleMap) {
        if (styleMap == null) {
            getProperties().remove("STYLEMAP");
        } else {
            getProperties().put("STYLEMAP", styleMap);
        }
    }

    @Deprecated
    public Map<StyleableProperty<?>, List<Style>> impl_findStyles(Map<StyleableProperty<?>, List<Style>> styleMap) {
        Map<StyleableProperty<?>, List<Style>> ret = CssStyleHelper.getMatchingStyles(styleMap, this);
        return ret != null ? ret : Collections.emptyMap();
    }

    final CssFlags getCSSFlags() {
        return this.cssFlag;
    }

    private void requestCssStateTransition() {
        if (getScene() == null) {
            return;
        }
        if (this.cssFlag == CssFlags.CLEAN || this.cssFlag == CssFlags.DIRTY_BRANCH) {
            this.cssFlag = CssFlags.UPDATE;
            notifyParentsOfInvalidatedCSS();
        }
    }

    public final void pseudoClassStateChanged(PseudoClass pseudoClass, boolean active) {
        boolean zRemove;
        if (active) {
            zRemove = this.pseudoClassStates.add(pseudoClass);
        } else {
            zRemove = this.pseudoClassStates.remove(pseudoClass);
        }
        boolean modified = zRemove;
        if (modified && this.styleHelper != null) {
            boolean isTransition = this.styleHelper.pseudoClassStateChanged(pseudoClass);
            if (isTransition) {
                requestCssStateTransition();
            }
        }
    }

    @Override // javafx.css.Styleable
    public final ObservableSet<PseudoClass> getPseudoClassStates() {
        return FXCollections.unmodifiableObservableSet(this.pseudoClassStates);
    }

    final void notifyParentsOfInvalidatedCSS() {
        SubScene subScene = getSubScene();
        Parent root = subScene != null ? subScene.getRoot() : getScene().getRoot();
        if (!root.impl_isDirty(DirtyBits.NODE_CSS)) {
            root.impl_markDirty(DirtyBits.NODE_CSS);
            if (subScene != null) {
                subScene.cssFlag = CssFlags.UPDATE;
                subScene.notifyParentsOfInvalidatedCSS();
            }
        }
        Parent parent = getParent();
        while (true) {
            Parent _parent = parent;
            if (_parent != null) {
                if (_parent.cssFlag == CssFlags.CLEAN) {
                    _parent.cssFlag = CssFlags.DIRTY_BRANCH;
                    parent = _parent.getParent();
                } else {
                    parent = null;
                }
            } else {
                return;
            }
        }
    }

    final void recalculateRelativeSizeProperties(Font fontForRelativeSizes) {
        if (this.styleHelper != null) {
            this.styleHelper.recalculateRelativeSizeProperties(this, fontForRelativeSizes);
        }
    }

    @Deprecated
    public final void impl_reapplyCSS() {
        if (getScene() == null || this.cssFlag == CssFlags.REAPPLY) {
            return;
        }
        if (this.cssFlag == CssFlags.DIRTY_BRANCH) {
            this.cssFlag = CssFlags.REAPPLY;
            return;
        }
        if (this.cssFlag == CssFlags.UPDATE) {
            this.cssFlag = CssFlags.REAPPLY;
            notifyParentsOfInvalidatedCSS();
            return;
        }
        reapplyCss();
        if (getParent() != null && getParent().performingLayout) {
            impl_processCSS((WritableValue<Boolean>) null);
        } else {
            notifyParentsOfInvalidatedCSS();
        }
    }

    private void reapplyCss() {
        CssStyleHelper oldStyleHelper = this.styleHelper;
        this.cssFlag = CssFlags.REAPPLY;
        this.styleHelper = CssStyleHelper.createStyleHelper(this);
        if (this instanceof Parent) {
            boolean visitChildren = this.styleHelper == null || oldStyleHelper != this.styleHelper || getParent() == null || getParent().cssFlag != CssFlags.CLEAN;
            if (visitChildren) {
                List<Node> children = ((Parent) this).getChildren();
                int nMax = children.size();
                for (int n2 = 0; n2 < nMax; n2++) {
                    Node child = children.get(n2);
                    child.reapplyCss();
                }
            }
        } else if (this instanceof SubScene) {
            Node subSceneRoot = ((SubScene) this).getRoot();
            if (subSceneRoot != null) {
                subSceneRoot.reapplyCss();
            }
        } else if (this.styleHelper == null) {
            this.cssFlag = CssFlags.CLEAN;
            return;
        }
        this.cssFlag = CssFlags.UPDATE;
    }

    void processCSS() {
        switch (this.cssFlag) {
            case CLEAN:
                break;
            case DIRTY_BRANCH:
                Parent me = (Parent) this;
                me.cssFlag = CssFlags.CLEAN;
                List<Node> children = me.getChildren();
                int max = children.size();
                for (int i2 = 0; i2 < max; i2++) {
                    children.get(i2).processCSS();
                }
                break;
            case REAPPLY:
            case UPDATE:
            default:
                impl_processCSS((WritableValue<Boolean>) null);
                break;
        }
    }

    @Deprecated
    public final void impl_processCSS(boolean reapply) {
        applyCss();
    }

    public final void applyCss() {
        if (getScene() == null) {
            return;
        }
        if (this.cssFlag != CssFlags.REAPPLY) {
            this.cssFlag = CssFlags.UPDATE;
        }
        Node topMost = this;
        boolean dirtyRoot = getScene().getRoot().impl_isDirty(DirtyBits.NODE_CSS);
        if (dirtyRoot) {
            Node parent = getParent();
            while (true) {
                Node _parent = parent;
                if (_parent == null) {
                    break;
                }
                if (_parent.cssFlag == CssFlags.UPDATE || _parent.cssFlag == CssFlags.REAPPLY) {
                    topMost = _parent;
                }
                parent = _parent.getParent();
            }
            if (topMost == getScene().getRoot()) {
                getScene().getRoot().impl_clearDirty(DirtyBits.NODE_CSS);
            }
        }
        topMost.processCSS();
    }

    @Deprecated
    protected void impl_processCSS(WritableValue<Boolean> unused) {
        if (this.cssFlag == CssFlags.CLEAN) {
            return;
        }
        if (this.cssFlag == CssFlags.REAPPLY) {
            reapplyCss();
        }
        this.cssFlag = CssFlags.CLEAN;
        if (this.styleHelper != null && getScene() != null) {
            this.styleHelper.transitionToState(this);
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/Node$LazyTransformProperty.class */
    private static abstract class LazyTransformProperty extends ReadOnlyObjectProperty<Transform> {
        protected static final int VALID = 0;
        protected static final int INVALID = 1;
        protected static final int VALIDITY_UNKNOWN = 2;
        protected int valid;
        private ExpressionHelper<Transform> helper;
        private Transform transform;
        private boolean canReuse;

        protected abstract boolean validityKnown();

        protected abstract int computeValidity();

        protected abstract Transform computeTransform(Transform transform);

        private LazyTransformProperty() {
            this.valid = 1;
            this.canReuse = false;
        }

        @Override // javafx.beans.Observable
        public void addListener(InvalidationListener listener) {
            this.helper = ExpressionHelper.addListener(this.helper, this, listener);
        }

        @Override // javafx.beans.Observable
        public void removeListener(InvalidationListener listener) {
            this.helper = ExpressionHelper.removeListener(this.helper, listener);
        }

        @Override // javafx.beans.value.ObservableValue
        public void addListener(ChangeListener<? super Transform> listener) {
            this.helper = ExpressionHelper.addListener(this.helper, this, listener);
        }

        @Override // javafx.beans.value.ObservableValue
        public void removeListener(ChangeListener<? super Transform> listener) {
            this.helper = ExpressionHelper.removeListener(this.helper, listener);
        }

        protected Transform getInternalValue() {
            if (this.valid == 1 || (this.valid == 2 && computeValidity() == 1)) {
                this.transform = computeTransform(this.canReuse ? this.transform : null);
                this.canReuse = true;
                this.valid = validityKnown() ? 0 : 2;
            }
            return this.transform;
        }

        @Override // javafx.beans.value.ObservableObjectValue
        public Transform get() {
            this.transform = getInternalValue();
            this.canReuse = false;
            return this.transform;
        }

        public void validityUnknown() {
            if (this.valid == 0) {
                this.valid = 2;
            }
        }

        public void invalidate() {
            if (this.valid != 1) {
                this.valid = 1;
                ExpressionHelper.fireValueChangedEvent(this.helper);
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/Node$LazyBoundsProperty.class */
    private static abstract class LazyBoundsProperty extends ReadOnlyObjectProperty<Bounds> {
        private ExpressionHelper<Bounds> helper;
        private boolean valid;
        private Bounds bounds;

        protected abstract Bounds computeBounds();

        private LazyBoundsProperty() {
        }

        @Override // javafx.beans.Observable
        public void addListener(InvalidationListener listener) {
            this.helper = ExpressionHelper.addListener(this.helper, this, listener);
        }

        @Override // javafx.beans.Observable
        public void removeListener(InvalidationListener listener) {
            this.helper = ExpressionHelper.removeListener(this.helper, listener);
        }

        @Override // javafx.beans.value.ObservableValue
        public void addListener(ChangeListener<? super Bounds> listener) {
            this.helper = ExpressionHelper.addListener(this.helper, this, listener);
        }

        @Override // javafx.beans.value.ObservableValue
        public void removeListener(ChangeListener<? super Bounds> listener) {
            this.helper = ExpressionHelper.removeListener(this.helper, listener);
        }

        @Override // javafx.beans.value.ObservableObjectValue
        public Bounds get() {
            if (!this.valid) {
                this.bounds = computeBounds();
                this.valid = true;
            }
            return this.bounds;
        }

        public void invalidate() {
            if (this.valid) {
                this.valid = false;
                ExpressionHelper.fireValueChangedEvent(this.helper);
            }
        }
    }

    public final void setAccessibleRole(AccessibleRole value) {
        if (value == null) {
            value = AccessibleRole.NODE;
        }
        accessibleRoleProperty().set(value);
    }

    public final AccessibleRole getAccessibleRole() {
        return this.accessibleRole == null ? AccessibleRole.NODE : accessibleRoleProperty().get();
    }

    public final ObjectProperty<AccessibleRole> accessibleRoleProperty() {
        if (this.accessibleRole == null) {
            this.accessibleRole = new SimpleObjectProperty(this, "accessibleRole", AccessibleRole.NODE);
        }
        return this.accessibleRole;
    }

    public final void setAccessibleRoleDescription(String value) {
        accessibleRoleDescriptionProperty().set(value);
    }

    public final String getAccessibleRoleDescription() {
        if (this.accessibilityProperties == null || this.accessibilityProperties.accessibleRoleDescription == null) {
            return null;
        }
        return accessibleRoleDescriptionProperty().get();
    }

    public final ObjectProperty<String> accessibleRoleDescriptionProperty() {
        return getAccessibilityProperties().getAccessibleRoleDescription();
    }

    public final void setAccessibleText(String value) {
        accessibleTextProperty().set(value);
    }

    public final String getAccessibleText() {
        if (this.accessibilityProperties == null || this.accessibilityProperties.accessibleText == null) {
            return null;
        }
        return accessibleTextProperty().get();
    }

    public final ObjectProperty<String> accessibleTextProperty() {
        return getAccessibilityProperties().getAccessibleText();
    }

    public final void setAccessibleHelp(String value) {
        accessibleHelpProperty().set(value);
    }

    public final String getAccessibleHelp() {
        if (this.accessibilityProperties == null || this.accessibilityProperties.accessibleHelp == null) {
            return null;
        }
        return accessibleHelpProperty().get();
    }

    public final ObjectProperty<String> accessibleHelpProperty() {
        return getAccessibilityProperties().getAccessibleHelp();
    }

    private AccessibilityProperties getAccessibilityProperties() {
        if (this.accessibilityProperties == null) {
            this.accessibilityProperties = new AccessibilityProperties();
        }
        return this.accessibilityProperties;
    }

    /* loaded from: jfxrt.jar:javafx/scene/Node$AccessibilityProperties.class */
    private class AccessibilityProperties {
        ObjectProperty<String> accessibleRoleDescription;
        ObjectProperty<String> accessibleText;
        ObjectProperty<String> accessibleHelp;

        private AccessibilityProperties() {
        }

        ObjectProperty<String> getAccessibleRoleDescription() {
            if (this.accessibleRoleDescription == null) {
                this.accessibleRoleDescription = new SimpleObjectProperty(Node.this, "accessibleRoleDescription", null);
            }
            return this.accessibleRoleDescription;
        }

        ObjectProperty<String> getAccessibleText() {
            if (this.accessibleText == null) {
                this.accessibleText = new SimpleObjectProperty(Node.this, "accessibleText", null);
            }
            return this.accessibleText;
        }

        ObjectProperty<String> getAccessibleHelp() {
            if (this.accessibleHelp == null) {
                this.accessibleHelp = new SimpleObjectProperty(Node.this, "accessibleHelp", null);
            }
            return this.accessibleHelp;
        }
    }

    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case ROLE:
                return getAccessibleRole();
            case ROLE_DESCRIPTION:
                return getAccessibleRoleDescription();
            case TEXT:
                return getAccessibleText();
            case HELP:
                return getAccessibleHelp();
            case PARENT:
                return getParent();
            case SCENE:
                return getScene();
            case BOUNDS:
                return localToScreen(getBoundsInLocal());
            case DISABLED:
                return Boolean.valueOf(isDisabled());
            case FOCUSED:
                return Boolean.valueOf(isFocused());
            case VISIBLE:
                return Boolean.valueOf(isVisible());
            case LABELED_BY:
                return this.labeledBy;
            default:
                return null;
        }
    }

    public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
        switch (action) {
            case REQUEST_FOCUS:
                if (isFocusTraversable()) {
                    requestFocus();
                    break;
                }
                break;
            case SHOW_MENU:
                Bounds b2 = getBoundsInLocal();
                Point2D pt = localToScreen(b2.getMaxX(), b2.getMaxY());
                ContextMenuEvent event = new ContextMenuEvent(ContextMenuEvent.CONTEXT_MENU_REQUESTED, b2.getMaxX(), b2.getMaxY(), pt.getX(), pt.getY(), false, new PickResult(this, b2.getMaxX(), b2.getMaxY()));
                Event.fireEvent(this, event);
                break;
        }
    }

    public final void notifyAccessibleAttributeChanged(AccessibleAttribute attributes) {
        Scene scene;
        if (this.accessible == null && (scene = getScene()) != null) {
            this.accessible = scene.removeAccessible(this);
        }
        if (this.accessible != null) {
            this.accessible.sendNotification(attributes);
        }
    }

    Accessible getAccessible() {
        Scene scene;
        if (this.accessible == null && (scene = getScene()) != null) {
            this.accessible = scene.removeAccessible(this);
        }
        if (this.accessible == null) {
            this.accessible = Application.GetApplication().createAccessible();
            this.accessible.setEventHandler(new Accessible.EventHandler() { // from class: javafx.scene.Node.19
                @Override // com.sun.glass.ui.Accessible.EventHandler
                public AccessControlContext getAccessControlContext() {
                    Scene scene2 = Node.this.getScene();
                    if (scene2 == null) {
                        throw new RuntimeException("Accessbility requested for node not on a scene");
                    }
                    if (scene2.impl_getPeer() != null) {
                        return scene2.impl_getPeer().getAccessControlContext();
                    }
                    return scene2.acc;
                }

                @Override // com.sun.glass.ui.Accessible.EventHandler
                public Object getAttribute(AccessibleAttribute attribute, Object... parameters) {
                    return Node.this.queryAccessibleAttribute(attribute, parameters);
                }

                @Override // com.sun.glass.ui.Accessible.EventHandler
                public void executeAction(AccessibleAction action, Object... parameters) {
                    Node.this.executeAccessibleAction(action, parameters);
                }

                public String toString() {
                    String klassName = Node.this.getClass().getName();
                    return klassName.substring(klassName.lastIndexOf(46) + 1);
                }
            });
        }
        return this.accessible;
    }

    void releaseAccessible() {
        Accessible acc = this.accessible;
        if (acc != null) {
            this.accessible = null;
            acc.dispose();
        }
    }
}
