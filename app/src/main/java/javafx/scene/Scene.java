package javafx.scene;

import com.sun.glass.ui.Accessible;
import com.sun.glass.ui.Application;
import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.css.StyleManager;
import com.sun.javafx.cursor.CursorFrame;
import com.sun.javafx.event.EventQueue;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.logging.PulseLogger;
import com.sun.javafx.perf.PerformanceTracker;
import com.sun.javafx.robot.impl.FXRobotHelper;
import com.sun.javafx.runtime.SystemProperties;
import com.sun.javafx.scene.CssFlags;
import com.sun.javafx.scene.LayoutFlags;
import com.sun.javafx.scene.SceneEventDispatcher;
import com.sun.javafx.scene.SceneHelper;
import com.sun.javafx.scene.input.DragboardHelper;
import com.sun.javafx.scene.input.ExtendedInputMethodRequests;
import com.sun.javafx.scene.input.InputEventUtils;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.SceneTraversalEngine;
import com.sun.javafx.scene.traversal.TopMostTraversalEngine;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGLightBase;
import com.sun.javafx.tk.TKClipboard;
import com.sun.javafx.tk.TKDragGestureListener;
import com.sun.javafx.tk.TKDragSourceListener;
import com.sun.javafx.tk.TKDropTargetListener;
import com.sun.javafx.tk.TKPulseListener;
import com.sun.javafx.tk.TKScene;
import com.sun.javafx.tk.TKSceneListener;
import com.sun.javafx.tk.TKScenePaintListener;
import com.sun.javafx.tk.TKStage;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.util.Logging;
import com.sun.javafx.util.Utils;
import com.sun.media.jfxmedia.MetadataParser;
import com.sun.prism.impl.PrismSettings;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.DefaultProperty;
import javafx.beans.InvalidationListener;
import javafx.beans.NamedArg;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectPropertyBase;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.css.CssMetaData;
import javafx.css.StyleableObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.geometry.Bounds;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.GestureEvent;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.InputMethodRequests;
import javafx.scene.input.InputMethodTextRun;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.Mnemonic;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.input.RotateEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.input.TouchPoint;
import javafx.scene.input.TransferMode;
import javafx.scene.input.ZoomEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.Duration;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;
import sun.util.logging.PlatformLogger;

@DefaultProperty("root")
/* loaded from: jfxrt.jar:javafx/scene/Scene.class */
public class Scene implements EventTarget {
    private double widthSetByUser;
    private double heightSetByUser;
    private boolean sizeInitialized;
    private final boolean depthBuffer;
    private final SceneAntialiasing antiAliasing;
    private int dirtyBits;
    final AccessControlContext acc;
    private Camera defaultCamera;
    private Node transientFocusContainer;
    private static final int MIN_DIRTY_CAPACITY = 30;
    private static boolean inSynchronizer;
    private static boolean inMousePick;
    private static boolean allowPGAccess;
    private static int pgAccessCount;
    private static boolean paused;
    private static final boolean PLATFORM_DRAG_GESTURE_INITIATION = false;
    private Node[] dirtyNodes;
    private int dirtyNodesSize;

    @Deprecated
    private TKScene impl_peer;
    ScenePulseListener scenePulseListener;
    private ReadOnlyObjectWrapper<Window> window;
    DnDGesture dndGesture;
    DragGestureListener dragGestureListener;

    /* renamed from: x, reason: collision with root package name */
    private ReadOnlyDoubleWrapper f12645x;

    /* renamed from: y, reason: collision with root package name */
    private ReadOnlyDoubleWrapper f12646y;
    private ReadOnlyDoubleWrapper width;
    private ReadOnlyDoubleWrapper height;
    private TargetWrapper tmpTargetWrapper;
    private ObjectProperty<Camera> camera;
    private ObjectProperty<Paint> fill;
    private ObjectProperty<Parent> root;
    Parent oldRoot;
    private static TKPulseListener snapshotPulseListener;
    private static List<Runnable> snapshotRunnableListA;
    private static List<Runnable> snapshotRunnableListB;
    private static List<Runnable> snapshotRunnableList;
    private ObjectProperty<Cursor> cursor;
    private final ObservableList<String> stylesheets;
    private ObjectProperty<String> userAgentStylesheet;
    private PerformanceTracker tracker;
    private static final Object trackerMonitor;
    private MouseHandler mouseHandler;
    private ClickGenerator clickGenerator;
    private Point2D cursorScreenPos;
    private Point2D cursorScenePos;
    private final TouchGesture scrollGesture;
    private final TouchGesture zoomGesture;
    private final TouchGesture rotateGesture;
    private final TouchGesture swipeGesture;
    private TouchMap touchMap;
    private TouchEvent nextTouchEvent;
    private TouchPoint[] touchPoints;
    private int touchEventSetId;
    private int touchPointIndex;
    private Map<Integer, EventTarget> touchTargets;
    private KeyHandler keyHandler;
    private boolean focusDirty;
    private TopMostTraversalEngine traversalEngine;
    private Node oldFocusOwner;
    private ReadOnlyObjectWrapper<Node> focusOwner;
    Runnable testPulseListener;
    private List<LightBase> lights;
    private ObjectProperty<EventDispatcher> eventDispatcher;
    private SceneEventDispatcher internalEventDispatcher;
    private ObjectProperty<EventHandler<? super ContextMenuEvent>> onContextMenuRequested;
    private ObjectProperty<EventHandler<? super MouseEvent>> onMouseClicked;
    private ObjectProperty<EventHandler<? super MouseEvent>> onMouseDragged;
    private ObjectProperty<EventHandler<? super MouseEvent>> onMouseEntered;
    private ObjectProperty<EventHandler<? super MouseEvent>> onMouseExited;
    private ObjectProperty<EventHandler<? super MouseEvent>> onMouseMoved;
    private ObjectProperty<EventHandler<? super MouseEvent>> onMousePressed;
    private ObjectProperty<EventHandler<? super MouseEvent>> onMouseReleased;
    private ObjectProperty<EventHandler<? super MouseEvent>> onDragDetected;
    private ObjectProperty<EventHandler<? super MouseDragEvent>> onMouseDragOver;
    private ObjectProperty<EventHandler<? super MouseDragEvent>> onMouseDragReleased;
    private ObjectProperty<EventHandler<? super MouseDragEvent>> onMouseDragEntered;
    private ObjectProperty<EventHandler<? super MouseDragEvent>> onMouseDragExited;
    private ObjectProperty<EventHandler<? super ScrollEvent>> onScrollStarted;
    private ObjectProperty<EventHandler<? super ScrollEvent>> onScroll;
    private ObjectProperty<EventHandler<? super ScrollEvent>> onScrollFinished;
    private ObjectProperty<EventHandler<? super RotateEvent>> onRotationStarted;
    private ObjectProperty<EventHandler<? super RotateEvent>> onRotate;
    private ObjectProperty<EventHandler<? super RotateEvent>> onRotationFinished;
    private ObjectProperty<EventHandler<? super ZoomEvent>> onZoomStarted;
    private ObjectProperty<EventHandler<? super ZoomEvent>> onZoom;
    private ObjectProperty<EventHandler<? super ZoomEvent>> onZoomFinished;
    private ObjectProperty<EventHandler<? super SwipeEvent>> onSwipeUp;
    private ObjectProperty<EventHandler<? super SwipeEvent>> onSwipeDown;
    private ObjectProperty<EventHandler<? super SwipeEvent>> onSwipeLeft;
    private ObjectProperty<EventHandler<? super SwipeEvent>> onSwipeRight;
    private ObjectProperty<EventHandler<? super TouchEvent>> onTouchPressed;
    private ObjectProperty<EventHandler<? super TouchEvent>> onTouchMoved;
    private ObjectProperty<EventHandler<? super TouchEvent>> onTouchReleased;
    private ObjectProperty<EventHandler<? super TouchEvent>> onTouchStationary;
    private ObjectProperty<EventHandler<? super DragEvent>> onDragEntered;
    private ObjectProperty<EventHandler<? super DragEvent>> onDragExited;
    private ObjectProperty<EventHandler<? super DragEvent>> onDragOver;
    private ObjectProperty<EventHandler<? super DragEvent>> onDragDropped;
    private ObjectProperty<EventHandler<? super DragEvent>> onDragDone;
    private ObjectProperty<EventHandler<? super KeyEvent>> onKeyPressed;
    private ObjectProperty<EventHandler<? super KeyEvent>> onKeyReleased;
    private ObjectProperty<EventHandler<? super KeyEvent>> onKeyTyped;
    private ObjectProperty<EventHandler<? super InputMethodEvent>> onInputMethodTextChanged;
    private static final Object USER_DATA_KEY;
    private ObservableMap<Object, Object> properties;
    private static final NodeOrientation defaultNodeOrientation;
    private ObjectProperty<NodeOrientation> nodeOrientation;
    private EffectiveOrientationProperty effectiveNodeOrientationProperty;
    private NodeOrientation effectiveNodeOrientation;
    private Map<Node, Accessible> accMap;
    private Accessible accessible;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: jfxrt.jar:javafx/scene/Scene$DragDetectedState.class */
    private enum DragDetectedState {
        NOT_YET,
        PROCESSING,
        DONE
    }

    static {
        $assertionsDisabled = !Scene.class.desiredAssertionStatus();
        PerformanceTracker.setSceneAccessor(new PerformanceTracker.SceneAccessor() { // from class: javafx.scene.Scene.1
            @Override // com.sun.javafx.perf.PerformanceTracker.SceneAccessor
            public void setPerfTracker(Scene scene, PerformanceTracker tracker) {
                synchronized (Scene.trackerMonitor) {
                    scene.tracker = tracker;
                }
            }

            @Override // com.sun.javafx.perf.PerformanceTracker.SceneAccessor
            public PerformanceTracker getPerfTracker(Scene scene) {
                PerformanceTracker performanceTracker;
                synchronized (Scene.trackerMonitor) {
                    performanceTracker = scene.tracker;
                }
                return performanceTracker;
            }
        });
        FXRobotHelper.setSceneAccessor(new FXRobotHelper.FXRobotSceneAccessor() { // from class: javafx.scene.Scene.2
            @Override // com.sun.javafx.robot.impl.FXRobotHelper.FXRobotSceneAccessor
            public void processKeyEvent(Scene scene, KeyEvent keyEvent) {
                scene.impl_processKeyEvent(keyEvent);
            }

            @Override // com.sun.javafx.robot.impl.FXRobotHelper.FXRobotSceneAccessor
            public void processMouseEvent(Scene scene, MouseEvent mouseEvent) {
                scene.impl_processMouseEvent(mouseEvent);
            }

            @Override // com.sun.javafx.robot.impl.FXRobotHelper.FXRobotSceneAccessor
            public void processScrollEvent(Scene scene, ScrollEvent scrollEvent) {
                scene.processGestureEvent(scrollEvent, scene.scrollGesture);
            }

            @Override // com.sun.javafx.robot.impl.FXRobotHelper.FXRobotSceneAccessor
            public ObservableList<Node> getChildren(Parent parent) {
                return parent.getChildren();
            }

            @Override // com.sun.javafx.robot.impl.FXRobotHelper.FXRobotSceneAccessor
            public Object renderToImage(Scene scene, Object platformImage) {
                return scene.snapshot(null).impl_getPlatformImage();
            }
        });
        SceneHelper.setSceneAccessor(new SceneHelper.SceneAccessor() { // from class: javafx.scene.Scene.3
            @Override // com.sun.javafx.scene.SceneHelper.SceneAccessor
            public void setPaused(boolean paused2) {
                boolean unused = Scene.paused = paused2;
            }

            @Override // com.sun.javafx.scene.SceneHelper.SceneAccessor
            public void parentEffectiveOrientationInvalidated(Scene scene) {
                scene.parentEffectiveOrientationInvalidated();
            }

            @Override // com.sun.javafx.scene.SceneHelper.SceneAccessor
            public Camera getEffectiveCamera(Scene scene) {
                return scene.getEffectiveCamera();
            }

            @Override // com.sun.javafx.scene.SceneHelper.SceneAccessor
            public Scene createPopupScene(Parent root) {
                return new Scene(root) { // from class: javafx.scene.Scene.3.1
                    @Override // javafx.scene.Scene
                    void doLayoutPass() {
                        resizeRootToPreferredSize(getRoot());
                        super.doLayoutPass();
                    }

                    @Override // javafx.scene.Scene
                    void resizeRootOnSceneSizeChange(double newWidth, double newHeight) {
                    }
                };
            }

            @Override // com.sun.javafx.scene.SceneHelper.SceneAccessor
            public void setTransientFocusContainer(Scene scene, Node node) {
                if (scene != null) {
                    scene.transientFocusContainer = node;
                }
            }

            @Override // com.sun.javafx.scene.SceneHelper.SceneAccessor
            public Accessible getAccessible(Scene scene) {
                return scene.getAccessible();
            }
        });
        inSynchronizer = false;
        inMousePick = false;
        allowPGAccess = false;
        pgAccessCount = 0;
        paused = false;
        snapshotPulseListener = null;
        trackerMonitor = new Object();
        USER_DATA_KEY = new Object();
        defaultNodeOrientation = ((Boolean) AccessController.doPrivileged(() -> {
            return Boolean.valueOf(Boolean.getBoolean("javafx.scene.nodeOrientation.RTL"));
        })).booleanValue() ? NodeOrientation.RIGHT_TO_LEFT : NodeOrientation.INHERIT;
    }

    static /* synthetic */ int access$5508(Scene x0) {
        int i2 = x0.touchPointIndex;
        x0.touchPointIndex = i2 + 1;
        return i2;
    }

    public Scene(@NamedArg("root") Parent root) {
        this(root, -1.0d, -1.0d, Color.WHITE, false, SceneAntialiasing.DISABLED);
    }

    public Scene(@NamedArg("root") Parent root, @NamedArg(MetadataParser.WIDTH_TAG_NAME) double width, @NamedArg(MetadataParser.HEIGHT_TAG_NAME) double height) {
        this(root, width, height, Color.WHITE, false, SceneAntialiasing.DISABLED);
    }

    public Scene(@NamedArg("root") Parent root, @NamedArg(value = "fill", defaultValue = "WHITE") Paint fill) {
        this(root, -1.0d, -1.0d, fill, false, SceneAntialiasing.DISABLED);
    }

    public Scene(@NamedArg("root") Parent root, @NamedArg(MetadataParser.WIDTH_TAG_NAME) double width, @NamedArg(MetadataParser.HEIGHT_TAG_NAME) double height, @NamedArg(value = "fill", defaultValue = "WHITE") Paint fill) {
        this(root, width, height, fill, false, SceneAntialiasing.DISABLED);
    }

    public Scene(@NamedArg("root") Parent root, @NamedArg(value = MetadataParser.WIDTH_TAG_NAME, defaultValue = "-1") double width, @NamedArg(value = MetadataParser.HEIGHT_TAG_NAME, defaultValue = "-1") double height, @NamedArg("depthBuffer") boolean depthBuffer) {
        this(root, width, height, Color.WHITE, depthBuffer, SceneAntialiasing.DISABLED);
    }

    public Scene(@NamedArg("root") Parent root, @NamedArg(value = MetadataParser.WIDTH_TAG_NAME, defaultValue = "-1") double width, @NamedArg(value = MetadataParser.HEIGHT_TAG_NAME, defaultValue = "-1") double height, @NamedArg("depthBuffer") boolean depthBuffer, @NamedArg(value = "antiAliasing", defaultValue = "DISABLED") SceneAntialiasing antiAliasing) {
        this(root, width, height, Color.WHITE, depthBuffer, antiAliasing);
        if (antiAliasing != null && antiAliasing != SceneAntialiasing.DISABLED && !Toolkit.getToolkit().isMSAASupported()) {
            String logname = Scene.class.getName();
            PlatformLogger.getLogger(logname).warning("System can't support antiAliasing");
        }
    }

    private Scene(Parent root, double width, double height, Paint fill, boolean depthBuffer, SceneAntialiasing antiAliasing) {
        this.widthSetByUser = -1.0d;
        this.heightSetByUser = -1.0d;
        this.sizeInitialized = false;
        this.acc = AccessController.getContext();
        this.scenePulseListener = new ScenePulseListener();
        this.dndGesture = null;
        this.tmpTargetWrapper = new TargetWrapper();
        this.stylesheets = new TrackableObservableList<String>() { // from class: javafx.scene.Scene.11
            @Override // com.sun.javafx.collections.TrackableObservableList
            protected void onChanged(ListChangeListener.Change<String> c2) {
                StyleManager.getInstance().stylesheetsChanged(Scene.this, c2);
                c2.reset();
                while (c2.next() && !c2.wasRemoved()) {
                }
                Scene.this.getRoot().impl_reapplyCSS();
            }
        };
        this.userAgentStylesheet = null;
        this.scrollGesture = new TouchGesture();
        this.zoomGesture = new TouchGesture();
        this.rotateGesture = new TouchGesture();
        this.swipeGesture = new TouchGesture();
        this.touchMap = new TouchMap();
        this.nextTouchEvent = null;
        this.touchPoints = null;
        this.touchEventSetId = 0;
        this.touchPointIndex = 0;
        this.touchTargets = new HashMap();
        this.keyHandler = null;
        this.focusDirty = true;
        this.traversalEngine = new SceneTraversalEngine(this);
        this.focusOwner = new ReadOnlyObjectWrapper<Node>(this, "focusOwner") { // from class: javafx.scene.Scene.13
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                if (Scene.this.oldFocusOwner != null) {
                    ((Node.FocusedProperty) Scene.this.oldFocusOwner.focusedProperty()).store(false);
                }
                Node value = get();
                if (value != null) {
                    ((Node.FocusedProperty) value.focusedProperty()).store(Scene.this.keyHandler.windowFocused);
                    if (value != Scene.this.oldFocusOwner) {
                        value.getScene().impl_enableInputMethodEvents((value.getInputMethodRequests() == null || value.getOnInputMethodTextChanged() == null) ? false : true);
                    }
                }
                Node localOldOwner = Scene.this.oldFocusOwner;
                Scene.this.oldFocusOwner = value;
                if (localOldOwner != null) {
                    ((Node.FocusedProperty) localOldOwner.focusedProperty()).notifyListeners();
                }
                if (value != null) {
                    ((Node.FocusedProperty) value.focusedProperty()).notifyListeners();
                }
                PlatformLogger logger = Logging.getFocusLogger();
                if (logger.isLoggable(PlatformLogger.Level.FINE)) {
                    if (value == get()) {
                        logger.fine("Changed focus from " + ((Object) localOldOwner) + " to " + ((Object) value));
                    } else {
                        logger.fine("Changing focus from " + ((Object) localOldOwner) + " to " + ((Object) value) + " canceled by nested requestFocus");
                    }
                }
                if (Scene.this.accessible != null) {
                    Scene.this.accessible.sendNotification(AccessibleAttribute.FOCUS_NODE);
                }
            }
        };
        this.testPulseListener = null;
        this.lights = new ArrayList();
        this.depthBuffer = depthBuffer;
        this.antiAliasing = antiAliasing;
        if (root == null) {
            throw new NullPointerException("Root cannot be null");
        }
        if ((depthBuffer || (antiAliasing != null && antiAliasing != SceneAntialiasing.DISABLED)) && !Platform.isSupported(ConditionalFeature.SCENE3D)) {
            String logname = Scene.class.getName();
            PlatformLogger.getLogger(logname).warning("System can't support ConditionalFeature.SCENE3D");
        }
        init();
        setRoot(root);
        init(width, height);
        setFill(fill);
    }

    static boolean isPGAccessAllowed() {
        return inSynchronizer || inMousePick || allowPGAccess;
    }

    @Deprecated
    public static void impl_setAllowPGAccess(boolean flag) {
        if (Utils.assertionEnabled()) {
            if (flag) {
                pgAccessCount++;
                allowPGAccess = true;
            } else {
                if (pgAccessCount <= 0) {
                    throw new AssertionError((Object) "*** pgAccessCount underflow");
                }
                int i2 = pgAccessCount - 1;
                pgAccessCount = i2;
                if (i2 == 0) {
                    allowPGAccess = false;
                }
            }
        }
    }

    void addToDirtyList(Node n2) {
        if ((this.dirtyNodes == null || this.dirtyNodesSize == 0) && this.impl_peer != null) {
            Toolkit.getToolkit().requestNextPulse();
        }
        if (this.dirtyNodes != null) {
            if (this.dirtyNodesSize == this.dirtyNodes.length) {
                Node[] tmp = new Node[this.dirtyNodesSize + (this.dirtyNodesSize >> 1)];
                System.arraycopy(this.dirtyNodes, 0, tmp, 0, this.dirtyNodesSize);
                this.dirtyNodes = tmp;
            }
            Node[] nodeArr = this.dirtyNodes;
            int i2 = this.dirtyNodesSize;
            this.dirtyNodesSize = i2 + 1;
            nodeArr[i2] = n2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doCSSPass() {
        Parent sceneRoot = getRoot();
        if (sceneRoot.cssFlag != CssFlags.CLEAN) {
            sceneRoot.impl_clearDirty(com.sun.javafx.scene.DirtyBits.NODE_CSS);
            sceneRoot.processCSS();
        }
    }

    void doLayoutPass() {
        Parent r2 = getRoot();
        if (r2 != null) {
            r2.layout();
        }
    }

    @Deprecated
    public TKScene impl_getPeer() {
        return this.impl_peer;
    }

    @Deprecated
    public TKPulseListener impl_getScenePulseListener() {
        if (SystemProperties.isDebug()) {
            return this.scenePulseListener;
        }
        return null;
    }

    public final SceneAntialiasing getAntiAliasing() {
        return this.antiAliasing;
    }

    private boolean getAntiAliasingInternal() {
        return this.antiAliasing != null && Toolkit.getToolkit().isMSAASupported() && Platform.isSupported(ConditionalFeature.SCENE3D) && this.antiAliasing != SceneAntialiasing.DISABLED;
    }

    private void setWindow(Window value) {
        windowPropertyImpl().set(value);
    }

    public final Window getWindow() {
        if (this.window == null) {
            return null;
        }
        return this.window.get();
    }

    public final ReadOnlyObjectProperty<Window> windowProperty() {
        return windowPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyObjectWrapper<Window> windowPropertyImpl() {
        if (this.window == null) {
            this.window = new ReadOnlyObjectWrapper<Window>() { // from class: javafx.scene.Scene.4
                private Window oldWindow;

                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() throws SecurityException {
                    Window newWindow = get();
                    Scene.this.getKeyHandler().windowForSceneChanged(this.oldWindow, newWindow);
                    if (this.oldWindow != null) {
                        Scene.this.impl_disposePeer();
                    }
                    if (newWindow != null) {
                        Scene.this.impl_initPeer();
                    }
                    Scene.this.parentEffectiveOrientationInvalidated();
                    this.oldWindow = newWindow;
                }

                @Override // javafx.beans.property.SimpleObjectProperty, javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.SimpleObjectProperty, javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "window";
                }
            };
        }
        return this.window;
    }

    @Deprecated
    public void impl_setWindow(Window value) {
        setWindow(value);
    }

    @Deprecated
    public void impl_initPeer() throws SecurityException {
        if (!$assertionsDisabled && this.impl_peer != null) {
            throw new AssertionError();
        }
        Window window = getWindow();
        if (!$assertionsDisabled && window == null) {
            throw new AssertionError();
        }
        TKStage windowPeer = window.impl_getPeer();
        if (windowPeer == null) {
            return;
        }
        boolean isTransparentWindowsSupported = Platform.isSupported(ConditionalFeature.TRANSPARENT_WINDOW);
        if (!isTransparentWindowsSupported) {
            PlatformImpl.addNoTransparencyStylesheetToScene(this);
        }
        PerformanceTracker.logEvent("Scene.initPeer started");
        impl_setAllowPGAccess(true);
        Toolkit tk = Toolkit.getToolkit();
        this.impl_peer = windowPeer.createTKScene(isDepthBufferInternal(), getAntiAliasingInternal(), this.acc);
        PerformanceTracker.logEvent("Scene.initPeer TKScene created");
        this.impl_peer.setTKSceneListener(new ScenePeerListener());
        this.impl_peer.setTKScenePaintListener(new ScenePeerPaintListener());
        PerformanceTracker.logEvent("Scene.initPeer TKScene set");
        this.impl_peer.setRoot(getRoot().impl_getPeer());
        this.impl_peer.setFillPaint(getFill() == null ? null : tk.getPaint(getFill()));
        getEffectiveCamera().impl_updatePeer();
        this.impl_peer.setCamera((NGCamera) getEffectiveCamera().impl_getPeer());
        this.impl_peer.markDirty();
        PerformanceTracker.logEvent("Scene.initPeer TKScene initialized");
        impl_setAllowPGAccess(false);
        tk.addSceneTkPulseListener(this.scenePulseListener);
        tk.enableDrop(this.impl_peer, new DropTargetListener());
        tk.installInputMethodRequests(this.impl_peer, new InputMethodRequestsDelegate());
        PerformanceTracker.logEvent("Scene.initPeer finished");
    }

    @Deprecated
    public void impl_disposePeer() {
        if (this.impl_peer == null) {
            return;
        }
        PerformanceTracker.logEvent("Scene.disposePeer started");
        Toolkit tk = Toolkit.getToolkit();
        tk.removeSceneTkPulseListener(this.scenePulseListener);
        if (this.accessible != null) {
            disposeAccessibles();
            Node root = getRoot();
            if (root != null) {
                root.releaseAccessible();
            }
            this.accessible.dispose();
            this.accessible = null;
        }
        this.impl_peer.dispose();
        this.impl_peer = null;
        PerformanceTracker.logEvent("Scene.disposePeer finished");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void setX(double value) {
        xPropertyImpl().set(value);
    }

    public final double getX() {
        if (this.f12645x == null) {
            return 0.0d;
        }
        return this.f12645x.get();
    }

    public final ReadOnlyDoubleProperty xProperty() {
        return xPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyDoubleWrapper xPropertyImpl() {
        if (this.f12645x == null) {
            this.f12645x = new ReadOnlyDoubleWrapper(this, LanguageTag.PRIVATEUSE);
        }
        return this.f12645x;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void setY(double value) {
        yPropertyImpl().set(value);
    }

    public final double getY() {
        if (this.f12646y == null) {
            return 0.0d;
        }
        return this.f12646y.get();
    }

    public final ReadOnlyDoubleProperty yProperty() {
        return yPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyDoubleWrapper yPropertyImpl() {
        if (this.f12646y == null) {
            this.f12646y = new ReadOnlyDoubleWrapper(this, PdfOps.y_TOKEN);
        }
        return this.f12646y;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void setWidth(double value) {
        widthPropertyImpl().set(value);
    }

    public final double getWidth() {
        if (this.width == null) {
            return 0.0d;
        }
        return this.width.get();
    }

    public final ReadOnlyDoubleProperty widthProperty() {
        return widthPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyDoubleWrapper widthPropertyImpl() {
        if (this.width == null) {
            this.width = new ReadOnlyDoubleWrapper() { // from class: javafx.scene.Scene.5
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    Parent _root = Scene.this.getRoot();
                    if (_root.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                        _root.impl_transformsChanged();
                    }
                    if (_root.isResizable()) {
                        Scene.this.resizeRootOnSceneSizeChange((get() - _root.getLayoutX()) - _root.getTranslateX(), _root.getLayoutBounds().getHeight());
                    }
                    Scene.this.getEffectiveCamera().setViewWidth(get());
                }

                @Override // javafx.beans.property.SimpleDoubleProperty, javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.SimpleDoubleProperty, javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return MetadataParser.WIDTH_TAG_NAME;
                }
            };
        }
        return this.width;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void setHeight(double value) {
        heightPropertyImpl().set(value);
    }

    public final double getHeight() {
        if (this.height == null) {
            return 0.0d;
        }
        return this.height.get();
    }

    public final ReadOnlyDoubleProperty heightProperty() {
        return heightPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyDoubleWrapper heightPropertyImpl() {
        if (this.height == null) {
            this.height = new ReadOnlyDoubleWrapper() { // from class: javafx.scene.Scene.6
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    Parent _root = Scene.this.getRoot();
                    if (_root.isResizable()) {
                        Scene.this.resizeRootOnSceneSizeChange(_root.getLayoutBounds().getWidth(), (get() - _root.getLayoutY()) - _root.getTranslateY());
                    }
                    Scene.this.getEffectiveCamera().setViewHeight(get());
                }

                @Override // javafx.beans.property.SimpleDoubleProperty, javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.SimpleDoubleProperty, javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return MetadataParser.HEIGHT_TAG_NAME;
                }
            };
        }
        return this.height;
    }

    void resizeRootOnSceneSizeChange(double newWidth, double newHeight) {
        getRoot().resize(newWidth, newHeight);
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
            this.camera = new ObjectPropertyBase<Camera>() { // from class: javafx.scene.Scene.7
                Camera oldCamera = null;

                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Camera _value = get();
                    if (_value != null) {
                        if ((_value instanceof PerspectiveCamera) && !Platform.isSupported(ConditionalFeature.SCENE3D)) {
                            String logname = Scene.class.getName();
                            PlatformLogger.getLogger(logname).warning("System can't support ConditionalFeature.SCENE3D");
                        }
                        if ((_value.getScene() != null && _value.getScene() != Scene.this) || _value.getSubScene() != null) {
                            throw new IllegalArgumentException(((Object) _value) + "is already part of other scene or subscene");
                        }
                        _value.setOwnerScene(Scene.this);
                        _value.setViewWidth(Scene.this.getWidth());
                        _value.setViewHeight(Scene.this.getHeight());
                    }
                    if (this.oldCamera != null && this.oldCamera != _value) {
                        this.oldCamera.setOwnerScene(null);
                    }
                    this.oldCamera = _value;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
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
        if (cam == null || ((cam instanceof PerspectiveCamera) && !Platform.isSupported(ConditionalFeature.SCENE3D))) {
            if (this.defaultCamera == null) {
                this.defaultCamera = new ParallelCamera();
                this.defaultCamera.setOwnerScene(this);
                this.defaultCamera.setViewWidth(getWidth());
                this.defaultCamera.setViewHeight(getHeight());
            }
            return this.defaultCamera;
        }
        return cam;
    }

    void markCameraDirty() {
        markDirty(DirtyBits.CAMERA_DIRTY);
        setNeedsRepaint();
    }

    void markCursorDirty() {
        markDirty(DirtyBits.CURSOR_DIRTY);
    }

    public final void setFill(Paint value) {
        fillProperty().set(value);
    }

    public final Paint getFill() {
        return this.fill == null ? Color.WHITE : this.fill.get();
    }

    public final ObjectProperty<Paint> fillProperty() {
        if (this.fill == null) {
            this.fill = new ObjectPropertyBase<Paint>(Color.WHITE) { // from class: javafx.scene.Scene.8
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.markDirty(DirtyBits.FILL_DIRTY);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "fill";
                }
            };
        }
        return this.fill;
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
            this.root = new ObjectPropertyBase<Parent>() { // from class: javafx.scene.Scene.9
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
                    if (_value.getScene() != null && _value.getScene().getRoot() == _value && _value.getScene() != Scene.this) {
                        if (isBound()) {
                            forceUnbind();
                        }
                        throw new IllegalArgumentException(((Object) _value) + "is already set as root of another scene");
                    }
                    if (Scene.this.oldRoot != null) {
                        Scene.this.oldRoot.setScenes(null, null);
                    }
                    Scene.this.oldRoot = _value;
                    _value.getStyleClass().add(0, "root");
                    _value.setScenes(Scene.this, null);
                    Scene.this.markDirty(DirtyBits.ROOT_DIRTY);
                    _value.resize(Scene.this.getWidth(), Scene.this.getHeight());
                    _value.requestLayout();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "root";
                }
            };
        }
        return this.root;
    }

    void setNeedsRepaint() {
        if (this.impl_peer != null) {
            this.impl_peer.entireSceneNeedsRepaint();
        }
    }

    void doCSSLayoutSyncForSnapshot(Node node) {
        if (!this.sizeInitialized) {
            preferredSize();
        } else {
            doCSSPass();
        }
        doLayoutPass();
        if (!paused) {
            getRoot().updateBounds();
            if (this.impl_peer != null) {
                this.impl_peer.waitForRenderingToComplete();
                this.impl_peer.waitForSynchronization();
                try {
                    this.scenePulseListener.synchronizeSceneNodes();
                    return;
                } finally {
                    this.impl_peer.releaseSynchronization(false);
                }
            }
            this.scenePulseListener.synchronizeSceneNodes();
        }
    }

    static WritableImage doSnapshot(Scene scene, double x2, double y2, double w2, double h2, Node root, BaseTransform transform, boolean depthBuffer, Paint fill, Camera camera, WritableImage wimg) {
        Toolkit tk = Toolkit.getToolkit();
        Toolkit.ImageRenderingContext context = new Toolkit.ImageRenderingContext();
        int xMin = (int) Math.floor(x2);
        int yMin = (int) Math.floor(y2);
        int xMax = (int) Math.ceil(x2 + w2);
        int yMax = (int) Math.ceil(y2 + h2);
        int width = Math.max(xMax - xMin, 1);
        int height = Math.max(yMax - yMin, 1);
        if (wimg == null) {
            wimg = new WritableImage(width, height);
        } else {
            width = (int) wimg.getWidth();
            height = (int) wimg.getHeight();
        }
        impl_setAllowPGAccess(true);
        context.f11964x = xMin;
        context.f11965y = yMin;
        context.width = width;
        context.height = height;
        context.transform = transform;
        context.depthBuffer = depthBuffer;
        context.root = root.impl_getPeer();
        context.platformPaint = fill == null ? null : tk.getPaint(fill);
        double cameraViewWidth = 1.0d;
        double cameraViewHeight = 1.0d;
        if (camera != null) {
            cameraViewWidth = camera.getViewWidth();
            cameraViewHeight = camera.getViewHeight();
            camera.setViewWidth(width);
            camera.setViewHeight(height);
            camera.impl_updatePeer();
            context.camera = (NGCamera) camera.impl_getPeer();
        } else {
            context.camera = null;
        }
        context.lights = null;
        if (scene != null && !scene.lights.isEmpty()) {
            context.lights = new NGLightBase[scene.lights.size()];
            for (int i2 = 0; i2 < scene.lights.size(); i2++) {
                context.lights[i2] = (NGLightBase) scene.lights.get(i2).impl_getPeer();
            }
        }
        Toolkit.WritableImageAccessor accessor = Toolkit.getWritableImageAccessor();
        context.platformImage = accessor.getTkImageLoader(wimg);
        impl_setAllowPGAccess(false);
        Object tkImage = tk.renderToImage(context);
        if (tkImage != null) {
            accessor.loadTkImage(wimg, tkImage);
        }
        if (camera != null) {
            impl_setAllowPGAccess(true);
            camera.setViewWidth(cameraViewWidth);
            camera.setViewHeight(cameraViewHeight);
            camera.impl_updatePeer();
            impl_setAllowPGAccess(false);
        }
        if (scene != null && scene.impl_peer != null) {
            scene.setNeedsRepaint();
        }
        return wimg;
    }

    private WritableImage doSnapshot(WritableImage img) {
        doCSSLayoutSyncForSnapshot(getRoot());
        double w2 = getWidth();
        double h2 = getHeight();
        BaseTransform transform = BaseTransform.IDENTITY_TRANSFORM;
        return doSnapshot(this, 0.0d, 0.0d, w2, h2, getRoot(), transform, isDepthBufferInternal(), getFill(), getEffectiveCamera(), img);
    }

    static void addSnapshotRunnable(Runnable runnable) {
        Toolkit.getToolkit().checkFxUserThread();
        if (snapshotPulseListener == null) {
            snapshotRunnableListA = new ArrayList();
            snapshotRunnableListB = new ArrayList();
            snapshotRunnableList = snapshotRunnableListA;
            snapshotPulseListener = () -> {
                if (snapshotRunnableList.size() > 0) {
                    List<Runnable> runnables = snapshotRunnableList;
                    if (snapshotRunnableList == snapshotRunnableListA) {
                        snapshotRunnableList = snapshotRunnableListB;
                    } else {
                        snapshotRunnableList = snapshotRunnableListA;
                    }
                    for (Runnable r2 : runnables) {
                        try {
                            r2.run();
                        } catch (Throwable th) {
                            System.err.println("Exception in snapshot runnable");
                            th.printStackTrace(System.err);
                        }
                    }
                    runnables.clear();
                }
            };
            Toolkit.getToolkit().addPostSceneTkPulseListener(snapshotPulseListener);
        }
        AccessControlContext acc = AccessController.getContext();
        snapshotRunnableList.add(() -> {
            AccessController.doPrivileged(() -> {
                runnable.run();
                return null;
            }, acc);
        });
        Toolkit.getToolkit().requestNextPulse();
    }

    public WritableImage snapshot(WritableImage image) {
        if (!paused) {
            Toolkit.getToolkit().checkFxUserThread();
        }
        return doSnapshot(image);
    }

    public void snapshot(Callback<SnapshotResult, Void> callback, WritableImage image) {
        Toolkit.getToolkit().checkFxUserThread();
        if (callback == null) {
            throw new NullPointerException("The callback must not be null");
        }
        Runnable snapshotRunnable = () -> {
            WritableImage img = doSnapshot(image);
            SnapshotResult result = new SnapshotResult(img, this, null);
            try {
            } catch (Throwable th) {
                System.err.println("Exception in snapshot callback");
                th.printStackTrace(System.err);
            }
        };
        addSnapshotRunnable(snapshotRunnable);
    }

    public final void setCursor(Cursor value) {
        cursorProperty().set(value);
    }

    public final Cursor getCursor() {
        if (this.cursor == null) {
            return null;
        }
        return this.cursor.get();
    }

    public final ObjectProperty<Cursor> cursorProperty() {
        if (this.cursor == null) {
            this.cursor = new ObjectPropertyBase<Cursor>() { // from class: javafx.scene.Scene.10
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.markCursorDirty();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "cursor";
                }
            };
        }
        return this.cursor;
    }

    public Node lookup(String selector) {
        return getRoot().lookup(selector);
    }

    public final ObservableList<String> getStylesheets() {
        return this.stylesheets;
    }

    public final ObjectProperty<String> userAgentStylesheetProperty() {
        if (this.userAgentStylesheet == null) {
            this.userAgentStylesheet = new SimpleObjectProperty<String>(this, "userAgentStylesheet", null) { // from class: javafx.scene.Scene.12
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    StyleManager.getInstance().forget(Scene.this);
                    Scene.this.getRoot().impl_reapplyCSS();
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

    public final boolean isDepthBuffer() {
        return this.depthBuffer;
    }

    boolean isDepthBufferInternal() {
        if (!Platform.isSupported(ConditionalFeature.SCENE3D)) {
            return false;
        }
        return this.depthBuffer;
    }

    private void init(double width, double height) {
        if (width >= 0.0d) {
            this.widthSetByUser = width;
            setWidth((float) width);
        }
        if (height >= 0.0d) {
            this.heightSetByUser = height;
            setHeight((float) height);
        }
        this.sizeInitialized = this.widthSetByUser >= 0.0d && this.heightSetByUser >= 0.0d;
    }

    private void init() {
        if (PerformanceTracker.isLoggingEnabled()) {
            PerformanceTracker.logEvent("Scene.init for [" + ((Object) this) + "]");
        }
        this.mouseHandler = new MouseHandler();
        this.clickGenerator = new ClickGenerator();
        if (PerformanceTracker.isLoggingEnabled()) {
            PerformanceTracker.logEvent("Scene.init for [" + ((Object) this) + "] - finished");
        }
    }

    private void preferredSize() {
        Parent root = getRoot();
        doCSSPass();
        resizeRootToPreferredSize(root);
        doLayoutPass();
        if (this.widthSetByUser < 0.0d) {
            setWidth(root.isResizable() ? root.getLayoutX() + root.getTranslateX() + root.getLayoutBounds().getWidth() : root.getBoundsInParent().getMaxX());
        } else {
            setWidth(this.widthSetByUser);
        }
        if (this.heightSetByUser < 0.0d) {
            setHeight(root.isResizable() ? root.getLayoutY() + root.getTranslateY() + root.getLayoutBounds().getHeight() : root.getBoundsInParent().getMaxY());
        } else {
            setHeight(this.heightSetByUser);
        }
        this.sizeInitialized = getWidth() > 0.0d && getHeight() > 0.0d;
        PerformanceTracker.logEvent("Scene preferred bounds computation complete");
    }

    final void resizeRootToPreferredSize(Parent root) {
        double preferredHeight;
        double preferredWidth;
        Orientation contentBias = root.getContentBias();
        if (contentBias == null) {
            preferredWidth = getPreferredWidth(root, this.widthSetByUser, -1.0d);
            preferredHeight = getPreferredHeight(root, this.heightSetByUser, -1.0d);
        } else if (contentBias == Orientation.HORIZONTAL) {
            preferredWidth = getPreferredWidth(root, this.widthSetByUser, -1.0d);
            preferredHeight = getPreferredHeight(root, this.heightSetByUser, preferredWidth);
        } else {
            preferredHeight = getPreferredHeight(root, this.heightSetByUser, -1.0d);
            preferredWidth = getPreferredWidth(root, this.widthSetByUser, preferredHeight);
        }
        root.resize(preferredWidth, preferredHeight);
    }

    private static double getPreferredWidth(Parent root, double forcedWidth, double height) {
        if (forcedWidth >= 0.0d) {
            return forcedWidth;
        }
        double normalizedHeight = height >= 0.0d ? height : -1.0d;
        return root.boundedSize(root.prefWidth(normalizedHeight), root.minWidth(normalizedHeight), root.maxWidth(normalizedHeight));
    }

    private static double getPreferredHeight(Parent root, double forcedHeight, double width) {
        if (forcedHeight >= 0.0d) {
            return forcedHeight;
        }
        double normalizedWidth = width >= 0.0d ? width : -1.0d;
        return root.boundedSize(root.prefHeight(normalizedWidth), root.minHeight(normalizedWidth), root.maxHeight(normalizedWidth));
    }

    @Deprecated
    public void impl_preferredSize() {
        preferredSize();
    }

    /* loaded from: jfxrt.jar:javafx/scene/Scene$TouchGesture.class */
    private static class TouchGesture {
        EventTarget target;
        Point2D sceneCoords;
        Point2D screenCoords;
        boolean finished;

        private TouchGesture() {
        }
    }

    @Deprecated
    public void impl_processMouseEvent(MouseEvent e2) {
        this.mouseHandler.process(e2, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processMenuEvent(double x2, double y2, double xAbs, double yAbs, boolean isKeyboardTrigger) {
        EventTarget eventTarget = null;
        inMousePick = true;
        if (isKeyboardTrigger) {
            Node sceneFocusOwner = getFocusOwner();
            double xOffset = xAbs - x2;
            double yOffset = yAbs - y2;
            if (sceneFocusOwner != null) {
                Bounds bounds = sceneFocusOwner.localToScene(sceneFocusOwner.getBoundsInLocal());
                x2 = bounds.getMinX() + (bounds.getWidth() / 4.0d);
                y2 = bounds.getMinY() + (bounds.getHeight() / 2.0d);
                eventTarget = sceneFocusOwner;
            } else {
                x2 = getWidth() / 4.0d;
                y2 = getWidth() / 2.0d;
                eventTarget = this;
            }
            xAbs = x2 + xOffset;
            yAbs = y2 + yOffset;
        }
        PickResult res = pick(x2, y2);
        if (!isKeyboardTrigger) {
            eventTarget = res.getIntersectedNode();
            if (eventTarget == null) {
                eventTarget = this;
            }
        }
        if (eventTarget != null) {
            ContextMenuEvent context = new ContextMenuEvent(ContextMenuEvent.CONTEXT_MENU_REQUESTED, x2, y2, xAbs, yAbs, isKeyboardTrigger, res);
            Event.fireEvent(eventTarget, context);
        }
        inMousePick = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processGestureEvent(GestureEvent e2, TouchGesture gesture) {
        EventTarget pickedTarget;
        if (e2.getEventType() == ZoomEvent.ZOOM_STARTED || e2.getEventType() == RotateEvent.ROTATION_STARTED || e2.getEventType() == ScrollEvent.SCROLL_STARTED) {
            gesture.target = null;
            gesture.finished = false;
        }
        if (gesture.target != null && (!gesture.finished || e2.isInertia())) {
            pickedTarget = gesture.target;
        } else {
            pickedTarget = e2.getPickResult().getIntersectedNode();
            if (pickedTarget == null) {
                pickedTarget = this;
            }
        }
        if (e2.getEventType() == ZoomEvent.ZOOM_STARTED || e2.getEventType() == RotateEvent.ROTATION_STARTED || e2.getEventType() == ScrollEvent.SCROLL_STARTED) {
            gesture.target = pickedTarget;
        }
        if (e2.getEventType() != ZoomEvent.ZOOM_FINISHED && e2.getEventType() != RotateEvent.ROTATION_FINISHED && e2.getEventType() != ScrollEvent.SCROLL_FINISHED && !e2.isInertia()) {
            gesture.sceneCoords = new Point2D(e2.getSceneX(), e2.getSceneY());
            gesture.screenCoords = new Point2D(e2.getScreenX(), e2.getScreenY());
        }
        if (pickedTarget != null) {
            Event.fireEvent(pickedTarget, e2);
        }
        if (e2.getEventType() == ZoomEvent.ZOOM_FINISHED || e2.getEventType() == RotateEvent.ROTATION_FINISHED || e2.getEventType() == ScrollEvent.SCROLL_FINISHED) {
            gesture.finished = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processTouchEvent(TouchEvent e2, TouchPoint[] touchPoints) {
        inMousePick = true;
        this.touchEventSetId++;
        List<TouchPoint> touchList = Arrays.asList(touchPoints);
        for (TouchPoint tp : touchPoints) {
            if (tp.getTarget() != null) {
                EventType<TouchEvent> type = null;
                switch (tp.getState()) {
                    case MOVED:
                        type = TouchEvent.TOUCH_MOVED;
                        break;
                    case PRESSED:
                        type = TouchEvent.TOUCH_PRESSED;
                        break;
                    case RELEASED:
                        type = TouchEvent.TOUCH_RELEASED;
                        break;
                    case STATIONARY:
                        type = TouchEvent.TOUCH_STATIONARY;
                        break;
                }
                for (TouchPoint t2 : touchPoints) {
                    t2.impl_reset();
                }
                TouchEvent te = new TouchEvent(type, tp, touchList, this.touchEventSetId, e2.isShiftDown(), e2.isControlDown(), e2.isAltDown(), e2.isMetaDown());
                Event.fireEvent(tp.getTarget(), te);
            }
        }
        for (TouchPoint tp2 : touchPoints) {
            EventTarget grabbed = tp2.getGrabbed();
            if (grabbed != null) {
                this.touchTargets.put(Integer.valueOf(tp2.getId()), grabbed);
            }
            if (grabbed == null || tp2.getState() == TouchPoint.State.RELEASED) {
                this.touchTargets.remove(Integer.valueOf(tp2.getId()));
            }
        }
        inMousePick = false;
    }

    Node test_pick(double x2, double y2) {
        inMousePick = true;
        PickResult result = this.mouseHandler.pickNode(new PickRay(x2, y2, 1.0d, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
        inMousePick = false;
        if (result != null) {
            return result.getIntersectedNode();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public PickResult pick(double x2, double y2) {
        pick(this.tmpTargetWrapper, x2, y2);
        return this.tmpTargetWrapper.getResult();
    }

    private boolean isInScene(double x2, double y2) {
        if (x2 < 0.0d || y2 < 0.0d || x2 > getWidth() || y2 > getHeight()) {
            return false;
        }
        Window w2 = getWindow();
        if ((w2 instanceof Stage) && ((Stage) w2).getStyle() == StageStyle.TRANSPARENT && getFill() == null) {
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pick(TargetWrapper target, double x2, double y2) {
        PickRay pickRay = getEffectiveCamera().computePickRay(x2, y2, null);
        double mag = pickRay.getDirectionNoClone().length();
        pickRay.getDirectionNoClone().normalize();
        PickResult res = this.mouseHandler.pickNode(pickRay);
        if (res != null) {
            target.setNodeResult(res);
            return;
        }
        Vec3d o2 = pickRay.getOriginNoClone();
        Vec3d d2 = pickRay.getDirectionNoClone();
        target.setSceneResult(new PickResult((Node) null, new Point3D(o2.f11930x + (mag * d2.f11930x), o2.f11931y + (mag * d2.f11931y), o2.f11932z + (mag * d2.f11932z)), mag), isInScene(x2, y2) ? this : null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public KeyHandler getKeyHandler() {
        if (this.keyHandler == null) {
            this.keyHandler = new KeyHandler();
        }
        return this.keyHandler;
    }

    final void setFocusDirty(boolean value) {
        if (!this.focusDirty) {
            Toolkit.getToolkit().requestNextPulse();
        }
        this.focusDirty = value;
    }

    final boolean isFocusDirty() {
        return this.focusDirty;
    }

    boolean traverse(Node node, Direction dir) {
        if (node.getSubScene() != null) {
            return node.getSubScene().traverse(node, dir);
        }
        return this.traversalEngine.trav(node, dir) != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void focusInitial() {
        this.traversalEngine.traverseToFirst();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void focusIneligible(Node node) {
        traverse(node, Direction.NEXT);
    }

    @Deprecated
    public void impl_processKeyEvent(KeyEvent e2) {
        if (this.dndGesture != null && !this.dndGesture.processKey(e2)) {
            this.dndGesture = null;
        }
        getKeyHandler().process(e2);
    }

    void requestFocus(Node node) {
        getKeyHandler().requestFocus(node);
    }

    public final Node getFocusOwner() {
        return this.focusOwner.get();
    }

    public final ReadOnlyObjectProperty<Node> focusOwnerProperty() {
        return this.focusOwner.getReadOnlyProperty();
    }

    void focusCleanup() {
        this.scenePulseListener.focusCleanup();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processInputMethodEvent(InputMethodEvent e2) {
        Node node = getFocusOwner();
        if (node != null) {
            node.fireEvent(e2);
        }
    }

    @Deprecated
    public void impl_enableInputMethodEvents(boolean enable) {
        if (this.impl_peer != null) {
            this.impl_peer.enableInputMethodEvents(enable);
        }
    }

    boolean isQuiescent() {
        Parent r2 = getRoot();
        return !isFocusDirty() && (r2 == null || (r2.cssFlag == CssFlags.CLEAN && r2.layoutFlag == LayoutFlags.CLEAN));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void markDirty(DirtyBits dirtyBit) {
        setDirty(dirtyBit);
        if (this.impl_peer != null) {
            Toolkit.getToolkit().requestNextPulse();
        }
    }

    private void setDirty(DirtyBits dirtyBit) {
        this.dirtyBits |= dirtyBit.getMask();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDirty(DirtyBits dirtyBit) {
        return (this.dirtyBits & dirtyBit.getMask()) != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDirtyEmpty() {
        return this.dirtyBits == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearDirty() {
        this.dirtyBits = 0;
    }

    /* loaded from: jfxrt.jar:javafx/scene/Scene$DirtyBits.class */
    private enum DirtyBits {
        FILL_DIRTY,
        ROOT_DIRTY,
        CAMERA_DIRTY,
        LIGHTS_DIRTY,
        CURSOR_DIRTY;

        private int mask = 1 << ordinal();

        DirtyBits() {
        }

        public final int getMask() {
            return this.mask;
        }
    }

    final void addLight(LightBase light) {
        if (!this.lights.contains(light)) {
            this.lights.add(light);
            markDirty(DirtyBits.LIGHTS_DIRTY);
        }
    }

    final void removeLight(LightBase light) {
        if (this.lights.remove(light)) {
            markDirty(DirtyBits.LIGHTS_DIRTY);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void syncLights() {
        if (!isDirty(DirtyBits.LIGHTS_DIRTY)) {
            return;
        }
        inSynchronizer = true;
        NGLightBase[] peerLights = this.impl_peer.getLights();
        if (!this.lights.isEmpty() || peerLights != null) {
            if (this.lights.isEmpty()) {
                this.impl_peer.setLights(null);
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
                this.impl_peer.setLights(peerLights);
            }
        }
        inSynchronizer = false;
    }

    /* loaded from: jfxrt.jar:javafx/scene/Scene$ScenePulseListener.class */
    class ScenePulseListener implements TKPulseListener {
        private boolean firstPulse = true;

        ScenePulseListener() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void synchronizeSceneNodes() throws SecurityException {
            Toolkit.getToolkit().checkFxUserThread();
            boolean unused = Scene.inSynchronizer = true;
            if (Scene.this.dirtyNodes == null) {
                syncAll(Scene.this.getRoot());
                Scene.this.dirtyNodes = new Node[30];
            } else {
                for (int i2 = 0; i2 < Scene.this.dirtyNodesSize; i2++) {
                    Node node = Scene.this.dirtyNodes[i2];
                    Scene.this.dirtyNodes[i2] = null;
                    if (node.getScene() == Scene.this) {
                        node.impl_syncPeer();
                    }
                }
                Scene.this.dirtyNodesSize = 0;
            }
            boolean unused2 = Scene.inSynchronizer = false;
        }

        private int syncAll(Node node) throws SecurityException {
            node.impl_syncPeer();
            int size = 1;
            if (node instanceof Parent) {
                Parent p2 = (Parent) node;
                int childrenCount = p2.getChildren().size();
                for (int i2 = 0; i2 < childrenCount; i2++) {
                    Node n2 = p2.getChildren().get(i2);
                    if (n2 != null) {
                        size += syncAll(n2);
                    }
                }
            } else if (node instanceof SubScene) {
                SubScene subScene = (SubScene) node;
                size = 1 + syncAll(subScene.getRoot());
            }
            if (node.getClip() != null) {
                size += syncAll(node.getClip());
            }
            return size;
        }

        private void synchronizeSceneProperties() throws SecurityException {
            boolean unused = Scene.inSynchronizer = true;
            if (Scene.this.isDirty(DirtyBits.ROOT_DIRTY)) {
                Scene.this.impl_peer.setRoot(Scene.this.getRoot().impl_getPeer());
            }
            if (Scene.this.isDirty(DirtyBits.FILL_DIRTY)) {
                Toolkit tk = Toolkit.getToolkit();
                Scene.this.impl_peer.setFillPaint(Scene.this.getFill() == null ? null : tk.getPaint(Scene.this.getFill()));
            }
            Camera cam = Scene.this.getEffectiveCamera();
            if (Scene.this.isDirty(DirtyBits.CAMERA_DIRTY)) {
                cam.impl_updatePeer();
                Scene.this.impl_peer.setCamera((NGCamera) cam.impl_getPeer());
            }
            if (Scene.this.isDirty(DirtyBits.CURSOR_DIRTY)) {
                Scene.this.mouseHandler.updateCursor(Scene.this.getCursor());
            }
            Scene.this.clearDirty();
            boolean unused2 = Scene.inSynchronizer = false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void focusCleanup() {
            if (Scene.this.isFocusDirty()) {
                Node oldOwner = Scene.this.getFocusOwner();
                if (oldOwner == null) {
                    Scene.this.focusInitial();
                } else if (oldOwner.getScene() != Scene.this) {
                    Scene.this.requestFocus(null);
                    Scene.this.focusInitial();
                } else if (!oldOwner.isCanReceiveFocus()) {
                    Scene.this.requestFocus(null);
                    Scene.this.focusIneligible(oldOwner);
                }
                Scene.this.setFocusDirty(false);
            }
        }

        @Override // com.sun.javafx.tk.TKPulseListener
        public void pulse() throws SecurityException {
            if (Scene.this.tracker != null) {
                Scene.this.tracker.pulse();
            }
            if (this.firstPulse) {
                PerformanceTracker.logEvent("Scene - first repaint");
            }
            focusCleanup();
            Scene.this.disposeAccessibles();
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newPhase("CSS Pass");
            }
            Scene.this.doCSSPass();
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newPhase("Layout Pass");
            }
            Scene.this.doLayoutPass();
            boolean dirty = (Scene.this.dirtyNodes != null && Scene.this.dirtyNodesSize == 0 && Scene.this.isDirtyEmpty()) ? false : true;
            if (dirty) {
                if (PulseLogger.PULSE_LOGGING_ENABLED) {
                    PulseLogger.newPhase("Update bounds");
                }
                Scene.this.getRoot().updateBounds();
                if (Scene.this.impl_peer != null) {
                    try {
                        if (PulseLogger.PULSE_LOGGING_ENABLED) {
                            PulseLogger.newPhase("Waiting for previous rendering");
                        }
                        Scene.this.impl_peer.waitForRenderingToComplete();
                        Scene.this.impl_peer.waitForSynchronization();
                        if (PulseLogger.PULSE_LOGGING_ENABLED) {
                            PulseLogger.newPhase("Copy state to render graph");
                        }
                        Scene.this.syncLights();
                        synchronizeSceneProperties();
                        synchronizeSceneNodes();
                        Scene.this.mouseHandler.pulse();
                        Scene.this.impl_peer.markDirty();
                    } finally {
                        Scene.this.impl_peer.releaseSynchronization(true);
                    }
                } else {
                    if (PulseLogger.PULSE_LOGGING_ENABLED) {
                        PulseLogger.newPhase("Synchronize with null peer");
                    }
                    synchronizeSceneNodes();
                    Scene.this.mouseHandler.pulse();
                }
                if (Scene.this.getRoot().cssFlag != CssFlags.CLEAN) {
                    Scene.this.getRoot().impl_markDirty(com.sun.javafx.scene.DirtyBits.NODE_CSS);
                }
            }
            Scene.this.mouseHandler.updateCursorFrame();
            if (this.firstPulse) {
                if (PerformanceTracker.isLoggingEnabled()) {
                    PerformanceTracker.logEvent("Scene - first repaint - layout complete");
                    if (PrismSettings.perfLogFirstPaintFlush) {
                        PerformanceTracker.outputLog();
                    }
                    if (PrismSettings.perfLogFirstPaintExit) {
                        System.exit(0);
                    }
                }
                this.firstPulse = false;
            }
            if (Scene.this.testPulseListener != null) {
                Scene.this.testPulseListener.run();
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/Scene$ScenePeerListener.class */
    class ScenePeerListener implements TKSceneListener {
        ScenePeerListener() {
        }

        @Override // com.sun.javafx.tk.TKSceneListener
        public void changedLocation(float x2, float y2) {
            if (x2 != Scene.this.getX()) {
                Scene.this.setX(x2);
            }
            if (y2 != Scene.this.getY()) {
                Scene.this.setY(y2);
            }
        }

        @Override // com.sun.javafx.tk.TKSceneListener
        public void changedSize(float w2, float h2) {
            if (w2 != Scene.this.getWidth()) {
                Scene.this.setWidth(w2);
            }
            if (h2 != Scene.this.getHeight()) {
                Scene.this.setHeight(h2);
            }
        }

        @Override // com.sun.javafx.tk.TKSceneListener
        public void mouseEvent(EventType<MouseEvent> type, double x2, double y2, double screenX, double screenY, MouseButton button, boolean popupTrigger, boolean synthesized, boolean shiftDown, boolean controlDown, boolean altDown, boolean metaDown, boolean primaryDown, boolean middleDown, boolean secondaryDown) {
            MouseEvent mouseEvent = new MouseEvent(type, x2, y2, screenX, screenY, button, 0, shiftDown, controlDown, altDown, metaDown, primaryDown, middleDown, secondaryDown, synthesized, popupTrigger, false, null);
            Scene.this.impl_processMouseEvent(mouseEvent);
        }

        @Override // com.sun.javafx.tk.TKSceneListener
        public void keyEvent(KeyEvent keyEvent) {
            Scene.this.impl_processKeyEvent(keyEvent);
        }

        @Override // com.sun.javafx.tk.TKSceneListener
        public void inputMethodEvent(EventType<InputMethodEvent> type, ObservableList<InputMethodTextRun> composed, String committed, int caretPosition) {
            InputMethodEvent inputMethodEvent = new InputMethodEvent(type, composed, committed, caretPosition);
            Scene.this.processInputMethodEvent(inputMethodEvent);
        }

        @Override // com.sun.javafx.tk.TKSceneListener
        public void menuEvent(double x2, double y2, double xAbs, double yAbs, boolean isKeyboardTrigger) {
            Scene.this.processMenuEvent(x2, y2, xAbs, yAbs, isKeyboardTrigger);
        }

        @Override // com.sun.javafx.tk.TKSceneListener
        public void scrollEvent(EventType<ScrollEvent> eventType, double scrollX, double scrollY, double totalScrollX, double totalScrollY, double xMultiplier, double yMultiplier, int touchCount, int scrollTextX, int scrollTextY, int defaultTextX, int defaultTextY, double x2, double y2, double screenX, double screenY, boolean _shiftDown, boolean _controlDown, boolean _altDown, boolean _metaDown, boolean _direct, boolean _inertia) {
            ScrollEvent.HorizontalTextScrollUnits xUnits = scrollTextX > 0 ? ScrollEvent.HorizontalTextScrollUnits.CHARACTERS : ScrollEvent.HorizontalTextScrollUnits.NONE;
            double xText = scrollTextX < 0 ? 0.0d : scrollTextX * scrollX;
            ScrollEvent.VerticalTextScrollUnits yUnits = scrollTextY > 0 ? ScrollEvent.VerticalTextScrollUnits.LINES : scrollTextY < 0 ? ScrollEvent.VerticalTextScrollUnits.PAGES : ScrollEvent.VerticalTextScrollUnits.NONE;
            double yText = scrollTextY < 0 ? scrollY : scrollTextY * scrollY;
            double xMultiplier2 = (defaultTextX <= 0 || scrollTextX < 0) ? xMultiplier : Math.round((xMultiplier * scrollTextX) / defaultTextX);
            double yMultiplier2 = (defaultTextY <= 0 || scrollTextY < 0) ? yMultiplier : Math.round((yMultiplier * scrollTextY) / defaultTextY);
            if (eventType == ScrollEvent.SCROLL_FINISHED) {
                x2 = Scene.this.scrollGesture.sceneCoords.getX();
                y2 = Scene.this.scrollGesture.sceneCoords.getY();
                screenX = Scene.this.scrollGesture.screenCoords.getX();
                screenY = Scene.this.scrollGesture.screenCoords.getY();
            } else if (Double.isNaN(x2) || Double.isNaN(y2) || Double.isNaN(screenX) || Double.isNaN(screenY)) {
                if (Scene.this.cursorScenePos != null && Scene.this.cursorScreenPos != null) {
                    x2 = Scene.this.cursorScenePos.getX();
                    y2 = Scene.this.cursorScenePos.getY();
                    screenX = Scene.this.cursorScreenPos.getX();
                    screenY = Scene.this.cursorScreenPos.getY();
                } else {
                    return;
                }
            }
            boolean unused = Scene.inMousePick = true;
            Scene.this.processGestureEvent(new ScrollEvent(eventType, x2, y2, screenX, screenY, _shiftDown, _controlDown, _altDown, _metaDown, _direct, _inertia, scrollX * xMultiplier2, scrollY * yMultiplier2, totalScrollX * xMultiplier2, totalScrollY * yMultiplier2, xMultiplier2, yMultiplier2, xUnits, xText, yUnits, yText, touchCount, Scene.this.pick(x2, y2)), Scene.this.scrollGesture);
            boolean unused2 = Scene.inMousePick = false;
        }

        @Override // com.sun.javafx.tk.TKSceneListener
        public void zoomEvent(EventType<ZoomEvent> eventType, double zoomFactor, double totalZoomFactor, double x2, double y2, double screenX, double screenY, boolean _shiftDown, boolean _controlDown, boolean _altDown, boolean _metaDown, boolean _direct, boolean _inertia) {
            if (eventType == ZoomEvent.ZOOM_FINISHED) {
                x2 = Scene.this.zoomGesture.sceneCoords.getX();
                y2 = Scene.this.zoomGesture.sceneCoords.getY();
                screenX = Scene.this.zoomGesture.screenCoords.getX();
                screenY = Scene.this.zoomGesture.screenCoords.getY();
            } else if (Double.isNaN(x2) || Double.isNaN(y2) || Double.isNaN(screenX) || Double.isNaN(screenY)) {
                if (Scene.this.cursorScenePos != null && Scene.this.cursorScreenPos != null) {
                    x2 = Scene.this.cursorScenePos.getX();
                    y2 = Scene.this.cursorScenePos.getY();
                    screenX = Scene.this.cursorScreenPos.getX();
                    screenY = Scene.this.cursorScreenPos.getY();
                } else {
                    return;
                }
            }
            boolean unused = Scene.inMousePick = true;
            Scene.this.processGestureEvent(new ZoomEvent(eventType, x2, y2, screenX, screenY, _shiftDown, _controlDown, _altDown, _metaDown, _direct, _inertia, zoomFactor, totalZoomFactor, Scene.this.pick(x2, y2)), Scene.this.zoomGesture);
            boolean unused2 = Scene.inMousePick = false;
        }

        @Override // com.sun.javafx.tk.TKSceneListener
        public void rotateEvent(EventType<RotateEvent> eventType, double angle, double totalAngle, double x2, double y2, double screenX, double screenY, boolean _shiftDown, boolean _controlDown, boolean _altDown, boolean _metaDown, boolean _direct, boolean _inertia) {
            if (eventType == RotateEvent.ROTATION_FINISHED) {
                x2 = Scene.this.rotateGesture.sceneCoords.getX();
                y2 = Scene.this.rotateGesture.sceneCoords.getY();
                screenX = Scene.this.rotateGesture.screenCoords.getX();
                screenY = Scene.this.rotateGesture.screenCoords.getY();
            } else if (Double.isNaN(x2) || Double.isNaN(y2) || Double.isNaN(screenX) || Double.isNaN(screenY)) {
                if (Scene.this.cursorScenePos != null && Scene.this.cursorScreenPos != null) {
                    x2 = Scene.this.cursorScenePos.getX();
                    y2 = Scene.this.cursorScenePos.getY();
                    screenX = Scene.this.cursorScreenPos.getX();
                    screenY = Scene.this.cursorScreenPos.getY();
                } else {
                    return;
                }
            }
            boolean unused = Scene.inMousePick = true;
            Scene.this.processGestureEvent(new RotateEvent(eventType, x2, y2, screenX, screenY, _shiftDown, _controlDown, _altDown, _metaDown, _direct, _inertia, angle, totalAngle, Scene.this.pick(x2, y2)), Scene.this.rotateGesture);
            boolean unused2 = Scene.inMousePick = false;
        }

        @Override // com.sun.javafx.tk.TKSceneListener
        public void swipeEvent(EventType<SwipeEvent> eventType, int touchCount, double x2, double y2, double screenX, double screenY, boolean _shiftDown, boolean _controlDown, boolean _altDown, boolean _metaDown, boolean _direct) {
            if (Double.isNaN(x2) || Double.isNaN(y2) || Double.isNaN(screenX) || Double.isNaN(screenY)) {
                if (Scene.this.cursorScenePos != null && Scene.this.cursorScreenPos != null) {
                    x2 = Scene.this.cursorScenePos.getX();
                    y2 = Scene.this.cursorScenePos.getY();
                    screenX = Scene.this.cursorScreenPos.getX();
                    screenY = Scene.this.cursorScreenPos.getY();
                } else {
                    return;
                }
            }
            boolean unused = Scene.inMousePick = true;
            Scene.this.processGestureEvent(new SwipeEvent(eventType, x2, y2, screenX, screenY, _shiftDown, _controlDown, _altDown, _metaDown, _direct, touchCount, Scene.this.pick(x2, y2)), Scene.this.swipeGesture);
            boolean unused2 = Scene.inMousePick = false;
        }

        @Override // com.sun.javafx.tk.TKSceneListener
        public void touchEventBegin(long time, int touchCount, boolean isDirect, boolean _shiftDown, boolean _controlDown, boolean _altDown, boolean _metaDown) {
            if (!isDirect) {
                Scene.this.nextTouchEvent = null;
                return;
            }
            Scene.this.nextTouchEvent = new TouchEvent(TouchEvent.ANY, null, null, 0, _shiftDown, _controlDown, _altDown, _metaDown);
            if (Scene.this.touchPoints == null || Scene.this.touchPoints.length != touchCount) {
                Scene.this.touchPoints = new TouchPoint[touchCount];
            }
            Scene.this.touchPointIndex = 0;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v28, types: [javafx.event.EventTarget] */
        /* JADX WARN: Type inference failed for: r0v56, types: [javafx.scene.Scene] */
        @Override // com.sun.javafx.tk.TKSceneListener
        public void touchEventNext(TouchPoint.State state, long touchId, double x2, double y2, double screenX, double screenY) {
            boolean unused = Scene.inMousePick = true;
            if (Scene.this.nextTouchEvent == null) {
                return;
            }
            Scene.access$5508(Scene.this);
            int id = state == TouchPoint.State.PRESSED ? Scene.this.touchMap.add(touchId) : Scene.this.touchMap.get(touchId);
            if (state == TouchPoint.State.RELEASED) {
                Scene.this.touchMap.remove(touchId);
            }
            int order = Scene.this.touchMap.getOrder(id);
            if (order >= Scene.this.touchPoints.length) {
                throw new RuntimeException("Too many touch points reported");
            }
            boolean isGrabbed = false;
            PickResult pickRes = Scene.this.pick(x2, y2);
            Node intersectedNode = (EventTarget) Scene.this.touchTargets.get(Integer.valueOf(id));
            if (intersectedNode == null) {
                intersectedNode = pickRes.getIntersectedNode();
                if (intersectedNode == null) {
                    intersectedNode = Scene.this;
                }
            } else {
                isGrabbed = true;
            }
            TouchPoint tp = new TouchPoint(id, state, x2, y2, screenX, screenY, intersectedNode, pickRes);
            Scene.this.touchPoints[order] = tp;
            if (isGrabbed) {
                tp.grab(intersectedNode);
            }
            if (tp.getState() == TouchPoint.State.PRESSED) {
                tp.grab(intersectedNode);
                Scene.this.touchTargets.put(Integer.valueOf(tp.getId()), intersectedNode);
            } else if (tp.getState() == TouchPoint.State.RELEASED) {
                Scene.this.touchTargets.remove(Integer.valueOf(tp.getId()));
            }
            boolean unused2 = Scene.inMousePick = false;
        }

        @Override // com.sun.javafx.tk.TKSceneListener
        public void touchEventEnd() {
            if (Scene.this.nextTouchEvent != null) {
                if (Scene.this.touchPointIndex == Scene.this.touchPoints.length) {
                    Scene.this.processTouchEvent(Scene.this.nextTouchEvent, Scene.this.touchPoints);
                    if (Scene.this.touchMap.cleanup()) {
                        Scene.this.touchEventSetId = 0;
                        return;
                    }
                    return;
                }
                throw new RuntimeException("Wrong number of touch points reported");
            }
        }

        @Override // com.sun.javafx.tk.TKSceneListener
        public Accessible getSceneAccessible() {
            return Scene.this.getAccessible();
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/Scene$ScenePeerPaintListener.class */
    private class ScenePeerPaintListener implements TKScenePaintListener {
        private ScenePeerPaintListener() {
        }

        @Override // com.sun.javafx.tk.TKScenePaintListener
        public void frameRendered() {
            synchronized (Scene.trackerMonitor) {
                if (Scene.this.tracker != null) {
                    Scene.this.tracker.frameRendered();
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/Scene$DropTargetListener.class */
    class DropTargetListener implements TKDropTargetListener {
        DropTargetListener() {
        }

        @Override // com.sun.javafx.tk.TKDropTargetListener
        public TransferMode dragEnter(double x2, double y2, double screenX, double screenY, TransferMode transferMode, TKClipboard dragboard) {
            if (Scene.this.dndGesture == null) {
                Scene.this.dndGesture = Scene.this.new DnDGesture();
            }
            Dragboard db = Dragboard.impl_createDragboard(dragboard);
            Scene.this.dndGesture.dragboard = db;
            DragEvent dragEvent = new DragEvent(DragEvent.ANY, Scene.this.dndGesture.dragboard, x2, y2, screenX, screenY, transferMode, null, null, Scene.this.pick(x2, y2));
            return Scene.this.dndGesture.processTargetEnterOver(dragEvent);
        }

        @Override // com.sun.javafx.tk.TKDropTargetListener
        public TransferMode dragOver(double x2, double y2, double screenX, double screenY, TransferMode transferMode) {
            if (Scene.this.dndGesture == null) {
                System.err.println("GOT A dragOver when dndGesture is null!");
                return null;
            }
            if (Scene.this.dndGesture.dragboard == null) {
                throw new RuntimeException("dndGesture.dragboard is null in dragOver");
            }
            DragEvent dragEvent = new DragEvent(DragEvent.ANY, Scene.this.dndGesture.dragboard, x2, y2, screenX, screenY, transferMode, null, null, Scene.this.pick(x2, y2));
            return Scene.this.dndGesture.processTargetEnterOver(dragEvent);
        }

        @Override // com.sun.javafx.tk.TKDropTargetListener
        public void dragExit(double x2, double y2, double screenX, double screenY) {
            if (Scene.this.dndGesture == null) {
                System.err.println("GOT A dragExit when dndGesture is null!");
                return;
            }
            if (Scene.this.dndGesture.dragboard == null) {
                throw new RuntimeException("dndGesture.dragboard is null in dragExit");
            }
            DragEvent dragEvent = new DragEvent(DragEvent.ANY, Scene.this.dndGesture.dragboard, x2, y2, screenX, screenY, null, null, null, Scene.this.pick(x2, y2));
            Scene.this.dndGesture.processTargetExit(dragEvent);
            if (Scene.this.dndGesture.source != null) {
                return;
            }
            Scene.this.dndGesture.dragboard = null;
            Scene.this.dndGesture = null;
        }

        @Override // com.sun.javafx.tk.TKDropTargetListener
        public TransferMode drop(double x2, double y2, double screenX, double screenY, TransferMode transferMode) {
            if (Scene.this.dndGesture == null) {
                System.err.println("GOT A drop when dndGesture is null!");
                return null;
            }
            if (Scene.this.dndGesture.dragboard == null) {
                throw new RuntimeException("dndGesture.dragboard is null in dragDrop");
            }
            DragEvent dragEvent = new DragEvent(DragEvent.ANY, Scene.this.dndGesture.dragboard, x2, y2, screenX, screenY, transferMode, null, null, Scene.this.pick(x2, y2));
            DragboardHelper.setDataAccessRestriction(Scene.this.dndGesture.dragboard, false);
            try {
                TransferMode tm = Scene.this.dndGesture.processTargetDrop(dragEvent);
                DragboardHelper.setDataAccessRestriction(Scene.this.dndGesture.dragboard, true);
                if (Scene.this.dndGesture.source == null) {
                    Scene.this.dndGesture.dragboard = null;
                    Scene.this.dndGesture = null;
                }
                return tm;
            } catch (Throwable th) {
                DragboardHelper.setDataAccessRestriction(Scene.this.dndGesture.dragboard, true);
                throw th;
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/Scene$DragGestureListener.class */
    class DragGestureListener implements TKDragGestureListener {
        DragGestureListener() {
        }

        @Override // com.sun.javafx.tk.TKDragGestureListener
        public void dragGestureRecognized(double x2, double y2, double screenX, double screenY, int button, TKClipboard dragboard) {
            Dragboard db = Dragboard.impl_createDragboard(dragboard);
            Scene.this.dndGesture = Scene.this.new DnDGesture();
            Scene.this.dndGesture.dragboard = db;
            DragEvent dragEvent = new DragEvent(DragEvent.ANY, db, x2, y2, screenX, screenY, null, null, null, Scene.this.pick(x2, y2));
            Scene.this.dndGesture.processRecognized(dragEvent);
            Scene.this.dndGesture = null;
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/Scene$DnDGesture.class */
    class DnDGesture {
        private double pressedX;
        private double pressedY;
        private final double hysteresisSizeX = Toolkit.getToolkit().getMultiClickMaxX();
        private final double hysteresisSizeY = Toolkit.getToolkit().getMultiClickMaxY();
        private EventTarget source = null;
        private Set<TransferMode> sourceTransferModes = null;
        private TransferMode acceptedTransferMode = null;
        private Dragboard dragboard = null;
        private EventTarget potentialTarget = null;
        private EventTarget target = null;
        private DragDetectedState dragDetected = DragDetectedState.NOT_YET;
        private List<EventTarget> currentTargets = new ArrayList();
        private List<EventTarget> newTargets = new ArrayList();
        private EventTarget fullPDRSource = null;

        DnDGesture() {
        }

        private void fireEvent(EventTarget target, Event e2) {
            if (target != null) {
                Event.fireEvent(target, e2);
            }
        }

        private void processingDragDetected() {
            this.dragDetected = DragDetectedState.PROCESSING;
        }

        private void dragDetectedProcessed() {
            this.dragDetected = DragDetectedState.DONE;
            boolean hasContent = this.dragboard != null && this.dragboard.impl_contentPut();
            if (hasContent) {
                Toolkit.getToolkit().startDrag(Scene.this.impl_peer, this.sourceTransferModes, Scene.this.new DragSourceListener(), this.dragboard);
            } else if (this.fullPDRSource != null) {
                Scene.this.mouseHandler.enterFullPDR(this.fullPDRSource);
            }
            this.fullPDRSource = null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void processDragDetection(MouseEvent mouseEvent) {
            if (this.dragDetected != DragDetectedState.NOT_YET) {
                mouseEvent.setDragDetect(false);
                return;
            }
            if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
                this.pressedX = mouseEvent.getSceneX();
                this.pressedY = mouseEvent.getSceneY();
                mouseEvent.setDragDetect(false);
            } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                double deltaX = Math.abs(mouseEvent.getSceneX() - this.pressedX);
                double deltaY = Math.abs(mouseEvent.getSceneY() - this.pressedY);
                mouseEvent.setDragDetect(deltaX > this.hysteresisSizeX || deltaY > this.hysteresisSizeY);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean process(MouseEvent mouseEvent, EventTarget target) {
            boolean continueProcessing = true;
            if (this.dragDetected != DragDetectedState.DONE && ((mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED || mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) && mouseEvent.isDragDetect())) {
                processingDragDetected();
                if (target != null) {
                    MouseEvent detectedEvent = mouseEvent.copyFor(mouseEvent.getSource(), target, MouseEvent.DRAG_DETECTED);
                    try {
                        fireEvent(target, detectedEvent);
                        if (this.dragboard != null) {
                            DragboardHelper.setDataAccessRestriction(this.dragboard, true);
                        }
                    } catch (Throwable th) {
                        if (this.dragboard != null) {
                            DragboardHelper.setDataAccessRestriction(this.dragboard, true);
                        }
                        throw th;
                    }
                }
                dragDetectedProcessed();
            }
            if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
                continueProcessing = false;
            }
            return continueProcessing;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean processRecognized(DragEvent de2) {
            EventTarget eventTarget;
            MouseEvent me = new MouseEvent(MouseEvent.DRAG_DETECTED, de2.getX(), de2.getY(), de2.getSceneX(), de2.getScreenY(), MouseButton.PRIMARY, 1, false, false, false, false, false, true, false, false, false, false, de2.getPickResult());
            processingDragDetected();
            EventTarget target = de2.getPickResult().getIntersectedNode();
            if (target != null) {
                eventTarget = target;
            } else {
                try {
                    eventTarget = Scene.this;
                } catch (Throwable th) {
                    if (this.dragboard != null) {
                        DragboardHelper.setDataAccessRestriction(this.dragboard, true);
                    }
                    throw th;
                }
            }
            fireEvent(eventTarget, me);
            if (this.dragboard != null) {
                DragboardHelper.setDataAccessRestriction(this.dragboard, true);
            }
            dragDetectedProcessed();
            boolean hasContent = (this.dragboard == null || this.dragboard.getContentTypes().isEmpty()) ? false : true;
            return hasContent;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void processDropEnd(DragEvent de2) {
            if (this.source == null) {
                System.out.println("Scene.DnDGesture.processDropEnd() - UNEXPECTD - source is NULL");
                return;
            }
            DragEvent de3 = new DragEvent(de2.getSource(), this.source, DragEvent.DRAG_DONE, de2.getDragboard(), de2.getSceneX(), de2.getSceneY(), de2.getScreenX(), de2.getScreenY(), de2.getTransferMode(), this.source, this.target, de2.getPickResult());
            Event.fireEvent(this.source, de3);
            Scene.this.tmpTargetWrapper.clear();
            handleExitEnter(de3, Scene.this.tmpTargetWrapper);
            Toolkit.getToolkit().stopDrag(this.dragboard);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public TransferMode processTargetEnterOver(DragEvent de2) {
            Scene.this.pick(Scene.this.tmpTargetWrapper, de2.getSceneX(), de2.getSceneY());
            EventTarget pickedTarget = Scene.this.tmpTargetWrapper.getEventTarget();
            if (this.dragboard == null) {
                this.dragboard = createDragboard(de2, false);
            }
            DragEvent de3 = new DragEvent(de2.getSource(), pickedTarget, de2.getEventType(), this.dragboard, de2.getSceneX(), de2.getSceneY(), de2.getScreenX(), de2.getScreenY(), de2.getTransferMode(), this.source, this.potentialTarget, de2.getPickResult());
            handleExitEnter(de3, Scene.this.tmpTargetWrapper);
            DragEvent de4 = new DragEvent(de3.getSource(), pickedTarget, DragEvent.DRAG_OVER, de3.getDragboard(), de3.getSceneX(), de3.getSceneY(), de3.getScreenX(), de3.getScreenY(), de3.getTransferMode(), this.source, this.potentialTarget, de3.getPickResult());
            fireEvent(pickedTarget, de4);
            Object acceptingObject = de4.getAcceptingObject();
            this.potentialTarget = acceptingObject instanceof EventTarget ? (EventTarget) acceptingObject : null;
            this.acceptedTransferMode = de4.getAcceptedTransferMode();
            return this.acceptedTransferMode;
        }

        private void processTargetActionChanged(DragEvent de2) {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void processTargetExit(DragEvent de2) {
            if (this.dragboard == null) {
                throw new NullPointerException("dragboard is null in processTargetExit()");
            }
            if (this.currentTargets.size() > 0) {
                this.potentialTarget = null;
                Scene.this.tmpTargetWrapper.clear();
                handleExitEnter(de2, Scene.this.tmpTargetWrapper);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public TransferMode processTargetDrop(DragEvent de2) {
            Scene.this.pick(Scene.this.tmpTargetWrapper, de2.getSceneX(), de2.getSceneY());
            EventTarget pickedTarget = Scene.this.tmpTargetWrapper.getEventTarget();
            DragEvent de3 = new DragEvent(de2.getSource(), pickedTarget, DragEvent.DRAG_DROPPED, de2.getDragboard(), de2.getSceneX(), de2.getSceneY(), de2.getScreenX(), de2.getScreenY(), this.acceptedTransferMode, this.source, this.potentialTarget, de2.getPickResult());
            if (this.dragboard == null) {
                throw new NullPointerException("dragboard is null in processTargetDrop()");
            }
            handleExitEnter(de3, Scene.this.tmpTargetWrapper);
            fireEvent(pickedTarget, de3);
            Object acceptingObject = de3.getAcceptingObject();
            this.potentialTarget = acceptingObject instanceof EventTarget ? (EventTarget) acceptingObject : null;
            this.target = this.potentialTarget;
            TransferMode result = de3.isDropCompleted() ? de3.getAcceptedTransferMode() : null;
            Scene.this.tmpTargetWrapper.clear();
            handleExitEnter(de3, Scene.this.tmpTargetWrapper);
            return result;
        }

        private void handleExitEnter(DragEvent e2, TargetWrapper target) {
            EventTarget currentTarget = this.currentTargets.size() > 0 ? this.currentTargets.get(0) : null;
            if (target.getEventTarget() != currentTarget) {
                target.fillHierarchy(this.newTargets);
                int i2 = this.currentTargets.size() - 1;
                int j2 = this.newTargets.size() - 1;
                while (i2 >= 0 && j2 >= 0 && this.currentTargets.get(i2) == this.newTargets.get(j2)) {
                    i2--;
                    j2--;
                }
                while (i2 >= 0) {
                    EventTarget t2 = this.currentTargets.get(i2);
                    if (this.potentialTarget == t2) {
                        this.potentialTarget = null;
                    }
                    e2 = e2.copyFor(e2.getSource(), t2, this.source, this.potentialTarget, DragEvent.DRAG_EXITED_TARGET);
                    Event.fireEvent(t2, e2);
                    i2--;
                }
                this.potentialTarget = null;
                while (j2 >= 0) {
                    EventTarget t3 = this.newTargets.get(j2);
                    e2 = e2.copyFor(e2.getSource(), t3, this.source, this.potentialTarget, DragEvent.DRAG_ENTERED_TARGET);
                    Object acceptingObject = e2.getAcceptingObject();
                    if (acceptingObject instanceof EventTarget) {
                        this.potentialTarget = (EventTarget) acceptingObject;
                    }
                    Event.fireEvent(t3, e2);
                    j2--;
                }
                this.currentTargets.clear();
                this.currentTargets.addAll(this.newTargets);
                this.newTargets.clear();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean processKey(KeyEvent e2) {
            if (e2.getEventType() == KeyEvent.KEY_PRESSED && e2.getCode() == KeyCode.ESCAPE) {
                DragEvent de2 = new DragEvent(this.source, this.source, DragEvent.DRAG_DONE, this.dragboard, 0.0d, 0.0d, 0.0d, 0.0d, null, this.source, null, null);
                if (this.source != null) {
                    Event.fireEvent(this.source, de2);
                }
                Scene.this.tmpTargetWrapper.clear();
                handleExitEnter(de2, Scene.this.tmpTargetWrapper);
                return false;
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Dragboard startDrag(EventTarget source, Set<TransferMode> t2) {
            if (this.dragDetected != DragDetectedState.PROCESSING) {
                throw new IllegalStateException("Cannot start drag and drop outside of DRAG_DETECTED event handler");
            }
            if (t2.isEmpty()) {
                this.dragboard = null;
            } else if (this.dragboard == null) {
                this.dragboard = createDragboard(null, true);
            }
            DragboardHelper.setDataAccessRestriction(this.dragboard, false);
            this.source = source;
            this.potentialTarget = source;
            this.sourceTransferModes = t2;
            return this.dragboard;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void startFullPDR(EventTarget source) {
            this.fullPDRSource = source;
        }

        private Dragboard createDragboard(DragEvent de2, boolean isDragSource) {
            Dragboard dragboard;
            if (de2 == null || (dragboard = de2.getDragboard()) == null) {
                TKClipboard dragboardPeer = Scene.this.impl_peer.createDragboard(isDragSource);
                return Dragboard.impl_createDragboard(dragboardPeer);
            }
            return dragboard;
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/Scene$DragSourceListener.class */
    class DragSourceListener implements TKDragSourceListener {
        DragSourceListener() {
        }

        @Override // com.sun.javafx.tk.TKDragSourceListener
        public void dragDropEnd(double x2, double y2, double screenX, double screenY, TransferMode transferMode) {
            if (Scene.this.dndGesture != null) {
                if (Scene.this.dndGesture.dragboard == null) {
                    throw new RuntimeException("dndGesture.dragboard is null in dragDropEnd");
                }
                DragEvent dragEvent = new DragEvent(DragEvent.ANY, Scene.this.dndGesture.dragboard, x2, y2, screenX, screenY, transferMode, null, null, null);
                DragboardHelper.setDataAccessRestriction(Scene.this.dndGesture.dragboard, false);
                try {
                    Scene.this.dndGesture.processDropEnd(dragEvent);
                    DragboardHelper.setDataAccessRestriction(Scene.this.dndGesture.dragboard, true);
                    Scene.this.dndGesture = null;
                } catch (Throwable th) {
                    DragboardHelper.setDataAccessRestriction(Scene.this.dndGesture.dragboard, true);
                    throw th;
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/Scene$ClickCounter.class */
    static class ClickCounter {
        Toolkit toolkit = Toolkit.getToolkit();
        private int count;
        private boolean out;
        private boolean still;
        private Timeline timeout;
        private double pressedX;
        private double pressedY;

        ClickCounter() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void inc() {
            this.count++;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int get() {
            return this.count;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isStill() {
            return this.still;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clear() {
            this.count = 0;
            stopTimeout();
        }

        private void out() {
            this.out = true;
            stopTimeout();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void applyOut() {
            if (this.out) {
                clear();
            }
            this.out = false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void moved(double x2, double y2) {
            if (Math.abs(x2 - this.pressedX) > this.toolkit.getMultiClickMaxX() || Math.abs(y2 - this.pressedY) > this.toolkit.getMultiClickMaxY()) {
                out();
                this.still = false;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void start(double x2, double y2) {
            this.pressedX = x2;
            this.pressedY = y2;
            this.out = false;
            if (this.timeout != null) {
                this.timeout.stop();
            }
            this.timeout = new Timeline();
            this.timeout.getKeyFrames().add(new KeyFrame(new Duration(this.toolkit.getMultiClickTime()), (EventHandler<ActionEvent>) event -> {
                this.out = true;
                this.timeout = null;
            }, new KeyValue[0]));
            this.timeout.play();
            this.still = true;
        }

        private void stopTimeout() {
            if (this.timeout != null) {
                this.timeout.stop();
                this.timeout = null;
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/Scene$ClickGenerator.class */
    static class ClickGenerator {
        private ClickCounter lastPress = null;
        private Map<MouseButton, ClickCounter> counters = new EnumMap(MouseButton.class);
        private List<EventTarget> pressedTargets = new ArrayList();
        private List<EventTarget> releasedTargets = new ArrayList();

        public ClickGenerator() {
            for (MouseButton mb : MouseButton.values()) {
                if (mb != MouseButton.NONE) {
                    this.counters.put(mb, new ClickCounter());
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public MouseEvent preProcess(MouseEvent e2) {
            Iterator<ClickCounter> it = this.counters.values().iterator();
            while (it.hasNext()) {
                it.next().moved(e2.getSceneX(), e2.getSceneY());
            }
            ClickCounter cc = this.counters.get(e2.getButton());
            boolean still = this.lastPress != null ? this.lastPress.isStill() : false;
            if (e2.getEventType() == MouseEvent.MOUSE_PRESSED) {
                if (!e2.isPrimaryButtonDown()) {
                    this.counters.get(MouseButton.PRIMARY).clear();
                }
                if (!e2.isSecondaryButtonDown()) {
                    this.counters.get(MouseButton.SECONDARY).clear();
                }
                if (!e2.isMiddleButtonDown()) {
                    this.counters.get(MouseButton.MIDDLE).clear();
                }
                cc.applyOut();
                cc.inc();
                cc.start(e2.getSceneX(), e2.getSceneY());
                this.lastPress = cc;
            }
            return new MouseEvent(e2.getEventType(), e2.getSceneX(), e2.getSceneY(), e2.getScreenX(), e2.getScreenY(), e2.getButton(), (cc == null || e2.getEventType() == MouseEvent.MOUSE_MOVED) ? 0 : cc.get(), e2.isShiftDown(), e2.isControlDown(), e2.isAltDown(), e2.isMetaDown(), e2.isPrimaryButtonDown(), e2.isMiddleButtonDown(), e2.isSecondaryButtonDown(), e2.isSynthesized(), e2.isPopupTrigger(), still, e2.getPickResult());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void postProcess(MouseEvent e2, TargetWrapper target, TargetWrapper pickedTarget) {
            if (e2.getEventType() == MouseEvent.MOUSE_RELEASED) {
                ClickCounter cc = this.counters.get(e2.getButton());
                target.fillHierarchy(this.pressedTargets);
                pickedTarget.fillHierarchy(this.releasedTargets);
                int i2 = this.pressedTargets.size() - 1;
                EventTarget clickedTarget = null;
                for (int j2 = this.releasedTargets.size() - 1; i2 >= 0 && j2 >= 0 && this.pressedTargets.get(i2) == this.releasedTargets.get(j2); j2--) {
                    clickedTarget = this.pressedTargets.get(i2);
                    i2--;
                }
                this.pressedTargets.clear();
                this.releasedTargets.clear();
                if (clickedTarget != null && this.lastPress != null) {
                    MouseEvent click = new MouseEvent(null, clickedTarget, MouseEvent.MOUSE_CLICKED, e2.getSceneX(), e2.getSceneY(), e2.getScreenX(), e2.getScreenY(), e2.getButton(), cc.get(), e2.isShiftDown(), e2.isControlDown(), e2.isAltDown(), e2.isMetaDown(), e2.isPrimaryButtonDown(), e2.isMiddleButtonDown(), e2.isSecondaryButtonDown(), e2.isSynthesized(), e2.isPopupTrigger(), this.lastPress.isStill(), e2.getPickResult());
                    Event.fireEvent(clickedTarget, click);
                }
            }
        }
    }

    void generateMouseExited(Node removing) {
        this.mouseHandler.handleNodeRemoval(removing);
    }

    /* loaded from: jfxrt.jar:javafx/scene/Scene$MouseHandler.class */
    class MouseHandler {
        private MouseEvent lastEvent;
        private Cursor currCursor;
        private CursorFrame currCursorFrame;
        private TargetWrapper pdrEventTarget = new TargetWrapper();
        private boolean pdrInProgress = false;
        private boolean fullPDREntered = false;
        private EventTarget currentEventTarget = null;
        private boolean hover = false;
        private boolean primaryButtonDown = false;
        private boolean secondaryButtonDown = false;
        private boolean middleButtonDown = false;
        private EventTarget fullPDRSource = null;
        private TargetWrapper fullPDRTmpTargetWrapper = new TargetWrapper();
        private final List<EventTarget> pdrEventTargets = new ArrayList();
        private final List<EventTarget> currentEventTargets = new ArrayList();
        private final List<EventTarget> newEventTargets = new ArrayList();
        private final List<EventTarget> fullPDRCurrentEventTargets = new ArrayList();
        private final List<EventTarget> fullPDRNewEventTargets = new ArrayList();
        private EventTarget fullPDRCurrentTarget = null;
        private EventQueue queue = new EventQueue();
        private Runnable pickProcess = new Runnable() { // from class: javafx.scene.Scene.MouseHandler.1
            @Override // java.lang.Runnable
            public void run() {
                if (Scene.this.impl_peer != null && MouseHandler.this.lastEvent != null) {
                    MouseHandler.this.process(MouseHandler.this.lastEvent, true);
                }
            }
        };

        MouseHandler() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void pulse() {
            if (this.hover && this.lastEvent != null) {
                Platform.runLater(this.pickProcess);
            }
        }

        private void clearPDREventTargets() {
            this.pdrInProgress = false;
            this.currentEventTarget = this.currentEventTargets.size() > 0 ? this.currentEventTargets.get(0) : null;
            this.pdrEventTarget.clear();
        }

        public void enterFullPDR(EventTarget gestureSource) {
            this.fullPDREntered = true;
            this.fullPDRSource = gestureSource;
            this.fullPDRCurrentTarget = null;
            this.fullPDRCurrentEventTargets.clear();
        }

        public void exitFullPDR(MouseEvent e2) {
            if (!this.fullPDREntered) {
                return;
            }
            this.fullPDREntered = false;
            for (int i2 = this.fullPDRCurrentEventTargets.size() - 1; i2 >= 0; i2--) {
                EventTarget entered = this.fullPDRCurrentEventTargets.get(i2);
                Event.fireEvent(entered, MouseEvent.copyForMouseDragEvent(e2, entered, entered, MouseDragEvent.MOUSE_DRAG_EXITED_TARGET, this.fullPDRSource, e2.getPickResult()));
            }
            this.fullPDRSource = null;
            this.fullPDRCurrentEventTargets.clear();
            this.fullPDRCurrentTarget = null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void handleNodeRemoval(Node removing) {
            if (this.lastEvent == null) {
                return;
            }
            if (this.currentEventTargets.contains(removing)) {
                int i2 = 0;
                EventTarget trg = null;
                while (trg != removing) {
                    int i3 = i2;
                    i2++;
                    trg = this.currentEventTargets.get(i3);
                    this.queue.postEvent(this.lastEvent.copyFor(trg, trg, MouseEvent.MOUSE_EXITED_TARGET));
                }
                this.currentEventTargets.subList(0, i2).clear();
            }
            if (this.fullPDREntered && this.fullPDRCurrentEventTargets.contains(removing)) {
                int i4 = 0;
                EventTarget trg2 = null;
                while (trg2 != removing) {
                    int i5 = i4;
                    i4++;
                    trg2 = this.fullPDRCurrentEventTargets.get(i5);
                    this.queue.postEvent(MouseEvent.copyForMouseDragEvent(this.lastEvent, trg2, trg2, MouseDragEvent.MOUSE_DRAG_EXITED_TARGET, this.fullPDRSource, this.lastEvent.getPickResult()));
                }
                this.fullPDRCurrentEventTargets.subList(0, i4).clear();
            }
            this.queue.fire();
            if (this.pdrInProgress && this.pdrEventTargets.contains(removing)) {
                int i6 = 0;
                EventTarget trg3 = null;
                while (trg3 != removing) {
                    int i7 = i6;
                    i6++;
                    trg3 = this.pdrEventTargets.get(i7);
                    ((Node) trg3).setPressed(false);
                }
                this.pdrEventTargets.subList(0, i6).clear();
                EventTarget trg4 = this.pdrEventTargets.get(0);
                PickResult res = this.pdrEventTarget.getResult();
                if (trg4 instanceof Node) {
                    this.pdrEventTarget.setNodeResult(new PickResult((Node) trg4, res.getIntersectedPoint(), res.getIntersectedDistance()));
                } else {
                    this.pdrEventTarget.setSceneResult(new PickResult((Node) null, res.getIntersectedPoint(), res.getIntersectedDistance()), (Scene) trg4);
                }
            }
        }

        private void handleEnterExit(MouseEvent e2, TargetWrapper pickedTarget) {
            if (pickedTarget.getEventTarget() != this.currentEventTarget || e2.getEventType() == MouseEvent.MOUSE_EXITED) {
                if (e2.getEventType() == MouseEvent.MOUSE_EXITED) {
                    this.newEventTargets.clear();
                } else {
                    pickedTarget.fillHierarchy(this.newEventTargets);
                }
                int newTargetsSize = this.newEventTargets.size();
                int i2 = this.currentEventTargets.size() - 1;
                int j2 = newTargetsSize - 1;
                int k2 = this.pdrEventTargets.size() - 1;
                while (i2 >= 0 && j2 >= 0 && this.currentEventTargets.get(i2) == this.newEventTargets.get(j2)) {
                    i2--;
                    j2--;
                    k2--;
                }
                int memk = k2;
                while (i2 >= 0) {
                    EventTarget exitedEventTarget = this.currentEventTargets.get(i2);
                    if (this.pdrInProgress && (k2 < 0 || exitedEventTarget != this.pdrEventTargets.get(k2))) {
                        break;
                    }
                    this.queue.postEvent(e2.copyFor(exitedEventTarget, exitedEventTarget, MouseEvent.MOUSE_EXITED_TARGET));
                    i2--;
                    k2--;
                }
                int k3 = memk;
                while (j2 >= 0) {
                    EventTarget enteredEventTarget = this.newEventTargets.get(j2);
                    if (this.pdrInProgress && (k3 < 0 || enteredEventTarget != this.pdrEventTargets.get(k3))) {
                        break;
                    }
                    this.queue.postEvent(e2.copyFor(enteredEventTarget, enteredEventTarget, MouseEvent.MOUSE_ENTERED_TARGET));
                    j2--;
                    k3--;
                }
                this.currentEventTarget = pickedTarget.getEventTarget();
                this.currentEventTargets.clear();
                while (true) {
                    j2++;
                    if (j2 >= newTargetsSize) {
                        break;
                    } else {
                        this.currentEventTargets.add(this.newEventTargets.get(j2));
                    }
                }
            }
            this.queue.fire();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void process(MouseEvent e2, boolean onPulse) {
            TargetWrapper target;
            Toolkit.getToolkit().checkFxUserThread();
            boolean unused = Scene.inMousePick = true;
            Scene.this.cursorScreenPos = new Point2D(e2.getScreenX(), e2.getScreenY());
            Scene.this.cursorScenePos = new Point2D(e2.getSceneX(), e2.getSceneY());
            boolean gestureStarted = false;
            if (!onPulse) {
                if (e2.getEventType() == MouseEvent.MOUSE_PRESSED) {
                    if (!this.primaryButtonDown && !this.secondaryButtonDown && !this.middleButtonDown) {
                        gestureStarted = true;
                        Scene.this.dndGesture = Scene.this.new DnDGesture();
                        clearPDREventTargets();
                    }
                } else if (e2.getEventType() == MouseEvent.MOUSE_MOVED) {
                    clearPDREventTargets();
                } else if (e2.getEventType() == MouseEvent.MOUSE_ENTERED) {
                    this.hover = true;
                } else if (e2.getEventType() == MouseEvent.MOUSE_EXITED) {
                    this.hover = false;
                }
                this.primaryButtonDown = e2.isPrimaryButtonDown();
                this.secondaryButtonDown = e2.isSecondaryButtonDown();
                this.middleButtonDown = e2.isMiddleButtonDown();
            }
            Scene.this.pick(Scene.this.tmpTargetWrapper, e2.getSceneX(), e2.getSceneY());
            PickResult res = Scene.this.tmpTargetWrapper.getResult();
            if (res != null) {
                e2 = new MouseEvent(e2.getEventType(), e2.getSceneX(), e2.getSceneY(), e2.getScreenX(), e2.getScreenY(), e2.getButton(), e2.getClickCount(), e2.isShiftDown(), e2.isControlDown(), e2.isAltDown(), e2.isMetaDown(), e2.isPrimaryButtonDown(), e2.isMiddleButtonDown(), e2.isSecondaryButtonDown(), e2.isSynthesized(), e2.isPopupTrigger(), e2.isStillSincePress(), res);
            }
            if (e2.getEventType() == MouseEvent.MOUSE_EXITED) {
                Scene.this.tmpTargetWrapper.clear();
            }
            if (!this.pdrInProgress) {
                target = Scene.this.tmpTargetWrapper;
            } else {
                target = this.pdrEventTarget;
            }
            if (gestureStarted) {
                this.pdrEventTarget.copy(target);
                this.pdrEventTarget.fillHierarchy(this.pdrEventTargets);
            }
            if (!onPulse) {
                e2 = Scene.this.clickGenerator.preProcess(e2);
            }
            handleEnterExit(e2, Scene.this.tmpTargetWrapper);
            if (Scene.this.dndGesture != null) {
                Scene.this.dndGesture.processDragDetection(e2);
            }
            if (this.fullPDREntered && e2.getEventType() == MouseEvent.MOUSE_RELEASED) {
                processFullPDR(e2, onPulse);
            }
            if (target.getEventTarget() != null && e2.getEventType() != MouseEvent.MOUSE_ENTERED && e2.getEventType() != MouseEvent.MOUSE_EXITED && !onPulse) {
                Event.fireEvent(target.getEventTarget(), e2);
            }
            if (this.fullPDREntered && e2.getEventType() != MouseEvent.MOUSE_RELEASED) {
                processFullPDR(e2, onPulse);
            }
            if (!onPulse) {
                Scene.this.clickGenerator.postProcess(e2, target, Scene.this.tmpTargetWrapper);
            }
            if (!onPulse && Scene.this.dndGesture != null && !Scene.this.dndGesture.process(e2, target.getEventTarget())) {
                Scene.this.dndGesture = null;
            }
            Cursor cursor = target.getCursor();
            if (e2.getEventType() != MouseEvent.MOUSE_EXITED) {
                if (cursor == null && this.hover) {
                    cursor = Scene.this.getCursor();
                }
                updateCursor(cursor);
                updateCursorFrame();
            }
            if (gestureStarted) {
                this.pdrInProgress = true;
            }
            if (this.pdrInProgress && !this.primaryButtonDown && !this.secondaryButtonDown && !this.middleButtonDown) {
                clearPDREventTargets();
                exitFullPDR(e2);
                Scene.this.pick(Scene.this.tmpTargetWrapper, e2.getSceneX(), e2.getSceneY());
                handleEnterExit(e2, Scene.this.tmpTargetWrapper);
            }
            this.lastEvent = e2.getEventType() == MouseEvent.MOUSE_EXITED ? null : e2;
            boolean unused2 = Scene.inMousePick = false;
        }

        private void processFullPDR(MouseEvent e2, boolean onPulse) {
            Scene.this.pick(this.fullPDRTmpTargetWrapper, e2.getSceneX(), e2.getSceneY());
            PickResult result = this.fullPDRTmpTargetWrapper.getResult();
            EventTarget eventTarget = this.fullPDRTmpTargetWrapper.getEventTarget();
            if (eventTarget != this.fullPDRCurrentTarget) {
                this.fullPDRTmpTargetWrapper.fillHierarchy(this.fullPDRNewEventTargets);
                int newTargetsSize = this.fullPDRNewEventTargets.size();
                int i2 = this.fullPDRCurrentEventTargets.size() - 1;
                int j2 = newTargetsSize - 1;
                while (i2 >= 0 && j2 >= 0 && this.fullPDRCurrentEventTargets.get(i2) == this.fullPDRNewEventTargets.get(j2)) {
                    i2--;
                    j2--;
                }
                while (i2 >= 0) {
                    EventTarget exitedEventTarget = this.fullPDRCurrentEventTargets.get(i2);
                    Event.fireEvent(exitedEventTarget, MouseEvent.copyForMouseDragEvent(e2, exitedEventTarget, exitedEventTarget, MouseDragEvent.MOUSE_DRAG_EXITED_TARGET, this.fullPDRSource, result));
                    i2--;
                }
                while (j2 >= 0) {
                    EventTarget enteredEventTarget = this.fullPDRNewEventTargets.get(j2);
                    Event.fireEvent(enteredEventTarget, MouseEvent.copyForMouseDragEvent(e2, enteredEventTarget, enteredEventTarget, MouseDragEvent.MOUSE_DRAG_ENTERED_TARGET, this.fullPDRSource, result));
                    j2--;
                }
                this.fullPDRCurrentTarget = eventTarget;
                this.fullPDRCurrentEventTargets.clear();
                this.fullPDRCurrentEventTargets.addAll(this.fullPDRNewEventTargets);
                this.fullPDRNewEventTargets.clear();
            }
            if (eventTarget != null && !onPulse) {
                if (e2.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                    Event.fireEvent(eventTarget, MouseEvent.copyForMouseDragEvent(e2, eventTarget, eventTarget, MouseDragEvent.MOUSE_DRAG_OVER, this.fullPDRSource, result));
                }
                if (e2.getEventType() == MouseEvent.MOUSE_RELEASED) {
                    Event.fireEvent(eventTarget, MouseEvent.copyForMouseDragEvent(e2, eventTarget, eventTarget, MouseDragEvent.MOUSE_DRAG_RELEASED, this.fullPDRSource, result));
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateCursor(Cursor newCursor) {
            if (this.currCursor != newCursor) {
                if (this.currCursor != null) {
                    this.currCursor.deactivate();
                }
                if (newCursor != null) {
                    newCursor.activate();
                }
                this.currCursor = newCursor;
            }
        }

        public void updateCursorFrame() {
            CursorFrame currentFrame;
            if (this.currCursor != null) {
                currentFrame = this.currCursor.getCurrentFrame();
            } else {
                currentFrame = Cursor.DEFAULT.getCurrentFrame();
            }
            CursorFrame newCursorFrame = currentFrame;
            if (this.currCursorFrame != newCursorFrame) {
                if (Scene.this.impl_peer != null) {
                    Scene.this.impl_peer.setCursor(newCursorFrame);
                }
                this.currCursorFrame = newCursorFrame;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public PickResult pickNode(PickRay pickRay) {
            PickResultChooser r2 = new PickResultChooser();
            Scene.this.getRoot().impl_pickNode(pickRay, r2);
            return r2.toPickResult();
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/Scene$KeyHandler.class */
    class KeyHandler {
        private boolean windowFocused;
        private final InvalidationListener sceneWindowFocusedListener = valueModel -> {
            setWindowFocused(((ReadOnlyBooleanProperty) valueModel).get());
        };

        KeyHandler() {
        }

        private void setFocusOwner(Node value) {
            Scene s2;
            TKScene peer;
            if (Scene.this.oldFocusOwner != null && (s2 = Scene.this.oldFocusOwner.getScene()) != null && (peer = s2.impl_getPeer()) != null) {
                peer.finishInputMethodComposition();
            }
            Scene.this.focusOwner.set(value);
        }

        protected boolean isWindowFocused() {
            return this.windowFocused;
        }

        protected void setWindowFocused(boolean value) {
            this.windowFocused = value;
            if (Scene.this.getFocusOwner() != null) {
                Scene.this.getFocusOwner().setFocused(this.windowFocused);
            }
            if (this.windowFocused && Scene.this.accessible != null) {
                Scene.this.accessible.sendNotification(AccessibleAttribute.FOCUS_NODE);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void windowForSceneChanged(Window oldWindow, Window window) {
            if (oldWindow != null) {
                oldWindow.focusedProperty().removeListener(this.sceneWindowFocusedListener);
            }
            if (window != null) {
                window.focusedProperty().addListener(this.sceneWindowFocusedListener);
                setWindowFocused(window.isFocused());
            } else {
                setWindowFocused(false);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void process(KeyEvent e2) {
            Node sceneFocusOwner = Scene.this.getFocusOwner();
            EventTarget eventTarget = sceneFocusOwner != null ? sceneFocusOwner : Scene.this;
            Event.fireEvent(eventTarget, e2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void requestFocus(Node node) {
            if (Scene.this.getFocusOwner() != node) {
                if (node != null && !node.isCanReceiveFocus()) {
                    return;
                }
                setFocusOwner(node);
            }
        }
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

    /* loaded from: jfxrt.jar:javafx/scene/Scene$InputMethodRequestsDelegate.class */
    class InputMethodRequestsDelegate implements ExtendedInputMethodRequests {
        InputMethodRequestsDelegate() {
        }

        @Override // javafx.scene.input.InputMethodRequests
        public Point2D getTextLocation(int offset) {
            InputMethodRequests requests = getClientRequests();
            if (requests != null) {
                return requests.getTextLocation(offset);
            }
            return new Point2D(0.0d, 0.0d);
        }

        @Override // javafx.scene.input.InputMethodRequests
        public int getLocationOffset(int x2, int y2) {
            InputMethodRequests requests = getClientRequests();
            if (requests != null) {
                return requests.getLocationOffset(x2, y2);
            }
            return 0;
        }

        @Override // javafx.scene.input.InputMethodRequests
        public void cancelLatestCommittedText() {
            InputMethodRequests requests = getClientRequests();
            if (requests != null) {
                requests.cancelLatestCommittedText();
            }
        }

        @Override // javafx.scene.input.InputMethodRequests
        public String getSelectedText() {
            InputMethodRequests requests = getClientRequests();
            if (requests != null) {
                return requests.getSelectedText();
            }
            return null;
        }

        @Override // com.sun.javafx.scene.input.ExtendedInputMethodRequests
        public int getInsertPositionOffset() {
            InputMethodRequests requests = getClientRequests();
            if (requests != null && (requests instanceof ExtendedInputMethodRequests)) {
                return ((ExtendedInputMethodRequests) requests).getInsertPositionOffset();
            }
            return 0;
        }

        @Override // com.sun.javafx.scene.input.ExtendedInputMethodRequests
        public String getCommittedText(int begin, int end) {
            InputMethodRequests requests = getClientRequests();
            if (requests != null && (requests instanceof ExtendedInputMethodRequests)) {
                return ((ExtendedInputMethodRequests) requests).getCommittedText(begin, end);
            }
            return null;
        }

        @Override // com.sun.javafx.scene.input.ExtendedInputMethodRequests
        public int getCommittedTextLength() {
            InputMethodRequests requests = getClientRequests();
            if (requests != null && (requests instanceof ExtendedInputMethodRequests)) {
                return ((ExtendedInputMethodRequests) requests).getCommittedTextLength();
            }
            return 0;
        }

        private InputMethodRequests getClientRequests() {
            Node focusOwner = Scene.this.getFocusOwner();
            if (focusOwner != null) {
                return focusOwner.getInputMethodRequests();
            }
            return null;
        }
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

    protected final <T extends Event> void setEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        getInternalEventDispatcher().getEventHandlerManager().setEventHandler(eventType, eventHandler);
    }

    private SceneEventDispatcher getInternalEventDispatcher() {
        initializeInternalEventDispatcher();
        return this.internalEventDispatcher;
    }

    final void initializeInternalEventDispatcher() {
        if (this.internalEventDispatcher == null) {
            this.internalEventDispatcher = createInternalEventDispatcher();
            this.eventDispatcher = new SimpleObjectProperty(this, "eventDispatcher", this.internalEventDispatcher);
        }
    }

    private SceneEventDispatcher createInternalEventDispatcher() {
        return new SceneEventDispatcher(this);
    }

    public void addMnemonic(Mnemonic m2) {
        getInternalEventDispatcher().getKeyboardShortcutsHandler().addMnemonic(m2);
    }

    public void removeMnemonic(Mnemonic m2) {
        getInternalEventDispatcher().getKeyboardShortcutsHandler().removeMnemonic(m2);
    }

    final void clearNodeMnemonics(Node node) {
        getInternalEventDispatcher().getKeyboardShortcutsHandler().clearNodeMnemonics(node);
    }

    public ObservableMap<KeyCombination, ObservableList<Mnemonic>> getMnemonics() {
        return getInternalEventDispatcher().getKeyboardShortcutsHandler().getMnemonics();
    }

    public ObservableMap<KeyCombination, Runnable> getAccelerators() {
        return getInternalEventDispatcher().getKeyboardShortcutsHandler().getAccelerators();
    }

    @Override // javafx.event.EventTarget
    public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
        EventDispatcher eventDispatcherValue;
        if (this.eventDispatcher != null && (eventDispatcherValue = this.eventDispatcher.get()) != null) {
            tail = tail.prepend(eventDispatcherValue);
        }
        if (getWindow() != null) {
            tail = getWindow().buildEventDispatchChain(tail);
        }
        return tail;
    }

    public final void setOnContextMenuRequested(EventHandler<? super ContextMenuEvent> value) {
        onContextMenuRequestedProperty().set(value);
    }

    public final EventHandler<? super ContextMenuEvent> getOnContextMenuRequested() {
        if (this.onContextMenuRequested == null) {
            return null;
        }
        return this.onContextMenuRequested.get();
    }

    public final ObjectProperty<EventHandler<? super ContextMenuEvent>> onContextMenuRequestedProperty() {
        if (this.onContextMenuRequested == null) {
            this.onContextMenuRequested = new ObjectPropertyBase<EventHandler<? super ContextMenuEvent>>() { // from class: javafx.scene.Scene.14
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onContextMenuRequested";
                }
            };
        }
        return this.onContextMenuRequested;
    }

    public final void setOnMouseClicked(EventHandler<? super MouseEvent> value) {
        onMouseClickedProperty().set(value);
    }

    public final EventHandler<? super MouseEvent> getOnMouseClicked() {
        if (this.onMouseClicked == null) {
            return null;
        }
        return this.onMouseClicked.get();
    }

    public final ObjectProperty<EventHandler<? super MouseEvent>> onMouseClickedProperty() {
        if (this.onMouseClicked == null) {
            this.onMouseClicked = new ObjectPropertyBase<EventHandler<? super MouseEvent>>() { // from class: javafx.scene.Scene.15
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(MouseEvent.MOUSE_CLICKED, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onMouseClicked";
                }
            };
        }
        return this.onMouseClicked;
    }

    public final void setOnMouseDragged(EventHandler<? super MouseEvent> value) {
        onMouseDraggedProperty().set(value);
    }

    public final EventHandler<? super MouseEvent> getOnMouseDragged() {
        if (this.onMouseDragged == null) {
            return null;
        }
        return this.onMouseDragged.get();
    }

    public final ObjectProperty<EventHandler<? super MouseEvent>> onMouseDraggedProperty() {
        if (this.onMouseDragged == null) {
            this.onMouseDragged = new ObjectPropertyBase<EventHandler<? super MouseEvent>>() { // from class: javafx.scene.Scene.16
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(MouseEvent.MOUSE_DRAGGED, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onMouseDragged";
                }
            };
        }
        return this.onMouseDragged;
    }

    public final void setOnMouseEntered(EventHandler<? super MouseEvent> value) {
        onMouseEnteredProperty().set(value);
    }

    public final EventHandler<? super MouseEvent> getOnMouseEntered() {
        if (this.onMouseEntered == null) {
            return null;
        }
        return this.onMouseEntered.get();
    }

    public final ObjectProperty<EventHandler<? super MouseEvent>> onMouseEnteredProperty() {
        if (this.onMouseEntered == null) {
            this.onMouseEntered = new ObjectPropertyBase<EventHandler<? super MouseEvent>>() { // from class: javafx.scene.Scene.17
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(MouseEvent.MOUSE_ENTERED, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onMouseEntered";
                }
            };
        }
        return this.onMouseEntered;
    }

    public final void setOnMouseExited(EventHandler<? super MouseEvent> value) {
        onMouseExitedProperty().set(value);
    }

    public final EventHandler<? super MouseEvent> getOnMouseExited() {
        if (this.onMouseExited == null) {
            return null;
        }
        return this.onMouseExited.get();
    }

    public final ObjectProperty<EventHandler<? super MouseEvent>> onMouseExitedProperty() {
        if (this.onMouseExited == null) {
            this.onMouseExited = new ObjectPropertyBase<EventHandler<? super MouseEvent>>() { // from class: javafx.scene.Scene.18
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(MouseEvent.MOUSE_EXITED, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onMouseExited";
                }
            };
        }
        return this.onMouseExited;
    }

    public final void setOnMouseMoved(EventHandler<? super MouseEvent> value) {
        onMouseMovedProperty().set(value);
    }

    public final EventHandler<? super MouseEvent> getOnMouseMoved() {
        if (this.onMouseMoved == null) {
            return null;
        }
        return this.onMouseMoved.get();
    }

    public final ObjectProperty<EventHandler<? super MouseEvent>> onMouseMovedProperty() {
        if (this.onMouseMoved == null) {
            this.onMouseMoved = new ObjectPropertyBase<EventHandler<? super MouseEvent>>() { // from class: javafx.scene.Scene.19
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(MouseEvent.MOUSE_MOVED, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onMouseMoved";
                }
            };
        }
        return this.onMouseMoved;
    }

    public final void setOnMousePressed(EventHandler<? super MouseEvent> value) {
        onMousePressedProperty().set(value);
    }

    public final EventHandler<? super MouseEvent> getOnMousePressed() {
        if (this.onMousePressed == null) {
            return null;
        }
        return this.onMousePressed.get();
    }

    public final ObjectProperty<EventHandler<? super MouseEvent>> onMousePressedProperty() {
        if (this.onMousePressed == null) {
            this.onMousePressed = new ObjectPropertyBase<EventHandler<? super MouseEvent>>() { // from class: javafx.scene.Scene.20
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(MouseEvent.MOUSE_PRESSED, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onMousePressed";
                }
            };
        }
        return this.onMousePressed;
    }

    public final void setOnMouseReleased(EventHandler<? super MouseEvent> value) {
        onMouseReleasedProperty().set(value);
    }

    public final EventHandler<? super MouseEvent> getOnMouseReleased() {
        if (this.onMouseReleased == null) {
            return null;
        }
        return this.onMouseReleased.get();
    }

    public final ObjectProperty<EventHandler<? super MouseEvent>> onMouseReleasedProperty() {
        if (this.onMouseReleased == null) {
            this.onMouseReleased = new ObjectPropertyBase<EventHandler<? super MouseEvent>>() { // from class: javafx.scene.Scene.21
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(MouseEvent.MOUSE_RELEASED, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onMouseReleased";
                }
            };
        }
        return this.onMouseReleased;
    }

    public final void setOnDragDetected(EventHandler<? super MouseEvent> value) {
        onDragDetectedProperty().set(value);
    }

    public final EventHandler<? super MouseEvent> getOnDragDetected() {
        if (this.onDragDetected == null) {
            return null;
        }
        return this.onDragDetected.get();
    }

    public final ObjectProperty<EventHandler<? super MouseEvent>> onDragDetectedProperty() {
        if (this.onDragDetected == null) {
            this.onDragDetected = new ObjectPropertyBase<EventHandler<? super MouseEvent>>() { // from class: javafx.scene.Scene.22
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(MouseEvent.DRAG_DETECTED, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onDragDetected";
                }
            };
        }
        return this.onDragDetected;
    }

    public final void setOnMouseDragOver(EventHandler<? super MouseDragEvent> value) {
        onMouseDragOverProperty().set(value);
    }

    public final EventHandler<? super MouseDragEvent> getOnMouseDragOver() {
        if (this.onMouseDragOver == null) {
            return null;
        }
        return this.onMouseDragOver.get();
    }

    public final ObjectProperty<EventHandler<? super MouseDragEvent>> onMouseDragOverProperty() {
        if (this.onMouseDragOver == null) {
            this.onMouseDragOver = new ObjectPropertyBase<EventHandler<? super MouseDragEvent>>() { // from class: javafx.scene.Scene.23
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(MouseDragEvent.MOUSE_DRAG_OVER, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onMouseDragOver";
                }
            };
        }
        return this.onMouseDragOver;
    }

    public final void setOnMouseDragReleased(EventHandler<? super MouseDragEvent> value) {
        onMouseDragReleasedProperty().set(value);
    }

    public final EventHandler<? super MouseDragEvent> getOnMouseDragReleased() {
        if (this.onMouseDragReleased == null) {
            return null;
        }
        return this.onMouseDragReleased.get();
    }

    public final ObjectProperty<EventHandler<? super MouseDragEvent>> onMouseDragReleasedProperty() {
        if (this.onMouseDragReleased == null) {
            this.onMouseDragReleased = new ObjectPropertyBase<EventHandler<? super MouseDragEvent>>() { // from class: javafx.scene.Scene.24
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(MouseDragEvent.MOUSE_DRAG_RELEASED, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onMouseDragReleased";
                }
            };
        }
        return this.onMouseDragReleased;
    }

    public final void setOnMouseDragEntered(EventHandler<? super MouseDragEvent> value) {
        onMouseDragEnteredProperty().set(value);
    }

    public final EventHandler<? super MouseDragEvent> getOnMouseDragEntered() {
        if (this.onMouseDragEntered == null) {
            return null;
        }
        return this.onMouseDragEntered.get();
    }

    public final ObjectProperty<EventHandler<? super MouseDragEvent>> onMouseDragEnteredProperty() {
        if (this.onMouseDragEntered == null) {
            this.onMouseDragEntered = new ObjectPropertyBase<EventHandler<? super MouseDragEvent>>() { // from class: javafx.scene.Scene.25
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(MouseDragEvent.MOUSE_DRAG_ENTERED, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onMouseDragEntered";
                }
            };
        }
        return this.onMouseDragEntered;
    }

    public final void setOnMouseDragExited(EventHandler<? super MouseDragEvent> value) {
        onMouseDragExitedProperty().set(value);
    }

    public final EventHandler<? super MouseDragEvent> getOnMouseDragExited() {
        if (this.onMouseDragExited == null) {
            return null;
        }
        return this.onMouseDragExited.get();
    }

    public final ObjectProperty<EventHandler<? super MouseDragEvent>> onMouseDragExitedProperty() {
        if (this.onMouseDragExited == null) {
            this.onMouseDragExited = new ObjectPropertyBase<EventHandler<? super MouseDragEvent>>() { // from class: javafx.scene.Scene.26
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(MouseDragEvent.MOUSE_DRAG_EXITED, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onMouseDragExited";
                }
            };
        }
        return this.onMouseDragExited;
    }

    public final void setOnScrollStarted(EventHandler<? super ScrollEvent> value) {
        onScrollStartedProperty().set(value);
    }

    public final EventHandler<? super ScrollEvent> getOnScrollStarted() {
        if (this.onScrollStarted == null) {
            return null;
        }
        return this.onScrollStarted.get();
    }

    public final ObjectProperty<EventHandler<? super ScrollEvent>> onScrollStartedProperty() {
        if (this.onScrollStarted == null) {
            this.onScrollStarted = new ObjectPropertyBase<EventHandler<? super ScrollEvent>>() { // from class: javafx.scene.Scene.27
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(ScrollEvent.SCROLL_STARTED, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onScrollStarted";
                }
            };
        }
        return this.onScrollStarted;
    }

    public final void setOnScroll(EventHandler<? super ScrollEvent> value) {
        onScrollProperty().set(value);
    }

    public final EventHandler<? super ScrollEvent> getOnScroll() {
        if (this.onScroll == null) {
            return null;
        }
        return this.onScroll.get();
    }

    public final ObjectProperty<EventHandler<? super ScrollEvent>> onScrollProperty() {
        if (this.onScroll == null) {
            this.onScroll = new ObjectPropertyBase<EventHandler<? super ScrollEvent>>() { // from class: javafx.scene.Scene.28
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(ScrollEvent.SCROLL, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onScroll";
                }
            };
        }
        return this.onScroll;
    }

    public final void setOnScrollFinished(EventHandler<? super ScrollEvent> value) {
        onScrollFinishedProperty().set(value);
    }

    public final EventHandler<? super ScrollEvent> getOnScrollFinished() {
        if (this.onScrollFinished == null) {
            return null;
        }
        return this.onScrollFinished.get();
    }

    public final ObjectProperty<EventHandler<? super ScrollEvent>> onScrollFinishedProperty() {
        if (this.onScrollFinished == null) {
            this.onScrollFinished = new ObjectPropertyBase<EventHandler<? super ScrollEvent>>() { // from class: javafx.scene.Scene.29
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(ScrollEvent.SCROLL_FINISHED, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onScrollFinished";
                }
            };
        }
        return this.onScrollFinished;
    }

    public final void setOnRotationStarted(EventHandler<? super RotateEvent> value) {
        onRotationStartedProperty().set(value);
    }

    public final EventHandler<? super RotateEvent> getOnRotationStarted() {
        if (this.onRotationStarted == null) {
            return null;
        }
        return this.onRotationStarted.get();
    }

    public final ObjectProperty<EventHandler<? super RotateEvent>> onRotationStartedProperty() {
        if (this.onRotationStarted == null) {
            this.onRotationStarted = new ObjectPropertyBase<EventHandler<? super RotateEvent>>() { // from class: javafx.scene.Scene.30
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(RotateEvent.ROTATION_STARTED, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onRotationStarted";
                }
            };
        }
        return this.onRotationStarted;
    }

    public final void setOnRotate(EventHandler<? super RotateEvent> value) {
        onRotateProperty().set(value);
    }

    public final EventHandler<? super RotateEvent> getOnRotate() {
        if (this.onRotate == null) {
            return null;
        }
        return this.onRotate.get();
    }

    public final ObjectProperty<EventHandler<? super RotateEvent>> onRotateProperty() {
        if (this.onRotate == null) {
            this.onRotate = new ObjectPropertyBase<EventHandler<? super RotateEvent>>() { // from class: javafx.scene.Scene.31
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(RotateEvent.ROTATE, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onRotate";
                }
            };
        }
        return this.onRotate;
    }

    public final void setOnRotationFinished(EventHandler<? super RotateEvent> value) {
        onRotationFinishedProperty().set(value);
    }

    public final EventHandler<? super RotateEvent> getOnRotationFinished() {
        if (this.onRotationFinished == null) {
            return null;
        }
        return this.onRotationFinished.get();
    }

    public final ObjectProperty<EventHandler<? super RotateEvent>> onRotationFinishedProperty() {
        if (this.onRotationFinished == null) {
            this.onRotationFinished = new ObjectPropertyBase<EventHandler<? super RotateEvent>>() { // from class: javafx.scene.Scene.32
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(RotateEvent.ROTATION_FINISHED, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onRotationFinished";
                }
            };
        }
        return this.onRotationFinished;
    }

    public final void setOnZoomStarted(EventHandler<? super ZoomEvent> value) {
        onZoomStartedProperty().set(value);
    }

    public final EventHandler<? super ZoomEvent> getOnZoomStarted() {
        if (this.onZoomStarted == null) {
            return null;
        }
        return this.onZoomStarted.get();
    }

    public final ObjectProperty<EventHandler<? super ZoomEvent>> onZoomStartedProperty() {
        if (this.onZoomStarted == null) {
            this.onZoomStarted = new ObjectPropertyBase<EventHandler<? super ZoomEvent>>() { // from class: javafx.scene.Scene.33
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(ZoomEvent.ZOOM_STARTED, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onZoomStarted";
                }
            };
        }
        return this.onZoomStarted;
    }

    public final void setOnZoom(EventHandler<? super ZoomEvent> value) {
        onZoomProperty().set(value);
    }

    public final EventHandler<? super ZoomEvent> getOnZoom() {
        if (this.onZoom == null) {
            return null;
        }
        return this.onZoom.get();
    }

    public final ObjectProperty<EventHandler<? super ZoomEvent>> onZoomProperty() {
        if (this.onZoom == null) {
            this.onZoom = new ObjectPropertyBase<EventHandler<? super ZoomEvent>>() { // from class: javafx.scene.Scene.34
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(ZoomEvent.ZOOM, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onZoom";
                }
            };
        }
        return this.onZoom;
    }

    public final void setOnZoomFinished(EventHandler<? super ZoomEvent> value) {
        onZoomFinishedProperty().set(value);
    }

    public final EventHandler<? super ZoomEvent> getOnZoomFinished() {
        if (this.onZoomFinished == null) {
            return null;
        }
        return this.onZoomFinished.get();
    }

    public final ObjectProperty<EventHandler<? super ZoomEvent>> onZoomFinishedProperty() {
        if (this.onZoomFinished == null) {
            this.onZoomFinished = new ObjectPropertyBase<EventHandler<? super ZoomEvent>>() { // from class: javafx.scene.Scene.35
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(ZoomEvent.ZOOM_FINISHED, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onZoomFinished";
                }
            };
        }
        return this.onZoomFinished;
    }

    public final void setOnSwipeUp(EventHandler<? super SwipeEvent> value) {
        onSwipeUpProperty().set(value);
    }

    public final EventHandler<? super SwipeEvent> getOnSwipeUp() {
        if (this.onSwipeUp == null) {
            return null;
        }
        return this.onSwipeUp.get();
    }

    public final ObjectProperty<EventHandler<? super SwipeEvent>> onSwipeUpProperty() {
        if (this.onSwipeUp == null) {
            this.onSwipeUp = new ObjectPropertyBase<EventHandler<? super SwipeEvent>>() { // from class: javafx.scene.Scene.36
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(SwipeEvent.SWIPE_UP, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onSwipeUp";
                }
            };
        }
        return this.onSwipeUp;
    }

    public final void setOnSwipeDown(EventHandler<? super SwipeEvent> value) {
        onSwipeDownProperty().set(value);
    }

    public final EventHandler<? super SwipeEvent> getOnSwipeDown() {
        if (this.onSwipeDown == null) {
            return null;
        }
        return this.onSwipeDown.get();
    }

    public final ObjectProperty<EventHandler<? super SwipeEvent>> onSwipeDownProperty() {
        if (this.onSwipeDown == null) {
            this.onSwipeDown = new ObjectPropertyBase<EventHandler<? super SwipeEvent>>() { // from class: javafx.scene.Scene.37
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(SwipeEvent.SWIPE_DOWN, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onSwipeDown";
                }
            };
        }
        return this.onSwipeDown;
    }

    public final void setOnSwipeLeft(EventHandler<? super SwipeEvent> value) {
        onSwipeLeftProperty().set(value);
    }

    public final EventHandler<? super SwipeEvent> getOnSwipeLeft() {
        if (this.onSwipeLeft == null) {
            return null;
        }
        return this.onSwipeLeft.get();
    }

    public final ObjectProperty<EventHandler<? super SwipeEvent>> onSwipeLeftProperty() {
        if (this.onSwipeLeft == null) {
            this.onSwipeLeft = new ObjectPropertyBase<EventHandler<? super SwipeEvent>>() { // from class: javafx.scene.Scene.38
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(SwipeEvent.SWIPE_LEFT, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onSwipeLeft";
                }
            };
        }
        return this.onSwipeLeft;
    }

    public final void setOnSwipeRight(EventHandler<? super SwipeEvent> value) {
        onSwipeRightProperty().set(value);
    }

    public final EventHandler<? super SwipeEvent> getOnSwipeRight() {
        if (this.onSwipeRight == null) {
            return null;
        }
        return this.onSwipeRight.get();
    }

    public final ObjectProperty<EventHandler<? super SwipeEvent>> onSwipeRightProperty() {
        if (this.onSwipeRight == null) {
            this.onSwipeRight = new ObjectPropertyBase<EventHandler<? super SwipeEvent>>() { // from class: javafx.scene.Scene.39
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(SwipeEvent.SWIPE_RIGHT, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onSwipeRight";
                }
            };
        }
        return this.onSwipeRight;
    }

    public final void setOnTouchPressed(EventHandler<? super TouchEvent> value) {
        onTouchPressedProperty().set(value);
    }

    public final EventHandler<? super TouchEvent> getOnTouchPressed() {
        if (this.onTouchPressed == null) {
            return null;
        }
        return this.onTouchPressed.get();
    }

    public final ObjectProperty<EventHandler<? super TouchEvent>> onTouchPressedProperty() {
        if (this.onTouchPressed == null) {
            this.onTouchPressed = new ObjectPropertyBase<EventHandler<? super TouchEvent>>() { // from class: javafx.scene.Scene.40
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(TouchEvent.TOUCH_PRESSED, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onTouchPressed";
                }
            };
        }
        return this.onTouchPressed;
    }

    public final void setOnTouchMoved(EventHandler<? super TouchEvent> value) {
        onTouchMovedProperty().set(value);
    }

    public final EventHandler<? super TouchEvent> getOnTouchMoved() {
        if (this.onTouchMoved == null) {
            return null;
        }
        return this.onTouchMoved.get();
    }

    public final ObjectProperty<EventHandler<? super TouchEvent>> onTouchMovedProperty() {
        if (this.onTouchMoved == null) {
            this.onTouchMoved = new ObjectPropertyBase<EventHandler<? super TouchEvent>>() { // from class: javafx.scene.Scene.41
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(TouchEvent.TOUCH_MOVED, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onTouchMoved";
                }
            };
        }
        return this.onTouchMoved;
    }

    public final void setOnTouchReleased(EventHandler<? super TouchEvent> value) {
        onTouchReleasedProperty().set(value);
    }

    public final EventHandler<? super TouchEvent> getOnTouchReleased() {
        if (this.onTouchReleased == null) {
            return null;
        }
        return this.onTouchReleased.get();
    }

    public final ObjectProperty<EventHandler<? super TouchEvent>> onTouchReleasedProperty() {
        if (this.onTouchReleased == null) {
            this.onTouchReleased = new ObjectPropertyBase<EventHandler<? super TouchEvent>>() { // from class: javafx.scene.Scene.42
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(TouchEvent.TOUCH_RELEASED, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onTouchReleased";
                }
            };
        }
        return this.onTouchReleased;
    }

    public final void setOnTouchStationary(EventHandler<? super TouchEvent> value) {
        onTouchStationaryProperty().set(value);
    }

    public final EventHandler<? super TouchEvent> getOnTouchStationary() {
        if (this.onTouchStationary == null) {
            return null;
        }
        return this.onTouchStationary.get();
    }

    public final ObjectProperty<EventHandler<? super TouchEvent>> onTouchStationaryProperty() {
        if (this.onTouchStationary == null) {
            this.onTouchStationary = new ObjectPropertyBase<EventHandler<? super TouchEvent>>() { // from class: javafx.scene.Scene.43
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(TouchEvent.TOUCH_STATIONARY, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onTouchStationary";
                }
            };
        }
        return this.onTouchStationary;
    }

    /* loaded from: jfxrt.jar:javafx/scene/Scene$TouchMap.class */
    private static class TouchMap {
        private static final int FAST_THRESHOLD = 10;
        int[] fastMap;
        Map<Long, Integer> slowMap;
        List<Integer> order;
        List<Long> removed;
        int counter;
        int active;

        private TouchMap() {
            this.fastMap = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            this.slowMap = new HashMap();
            this.order = new LinkedList();
            this.removed = new ArrayList(10);
            this.counter = 0;
            this.active = 0;
        }

        public int add(long id) {
            this.counter++;
            this.active++;
            if (id < 10) {
                this.fastMap[(int) id] = this.counter;
            } else {
                this.slowMap.put(Long.valueOf(id), Integer.valueOf(this.counter));
            }
            this.order.add(Integer.valueOf(this.counter));
            return this.counter;
        }

        public void remove(long id) {
            this.removed.add(Long.valueOf(id));
        }

        public int get(long id) {
            if (id < 10) {
                int result = this.fastMap[(int) id];
                if (result == 0) {
                    throw new RuntimeException("Platform reported wrong touch point ID");
                }
                return result;
            }
            try {
                return this.slowMap.get(Long.valueOf(id)).intValue();
            } catch (NullPointerException e2) {
                throw new RuntimeException("Platform reported wrong touch point ID");
            }
        }

        public int getOrder(int id) {
            return this.order.indexOf(Integer.valueOf(id));
        }

        public boolean cleanup() {
            Iterator<Long> it = this.removed.iterator();
            while (it.hasNext()) {
                long id = it.next().longValue();
                this.active--;
                this.order.remove(Integer.valueOf(get(id)));
                if (id < 10) {
                    this.fastMap[(int) id] = 0;
                } else {
                    this.slowMap.remove(Long.valueOf(id));
                }
                if (this.active == 0) {
                    this.counter = 0;
                }
            }
            this.removed.clear();
            return this.active == 0;
        }
    }

    public final void setOnDragEntered(EventHandler<? super DragEvent> value) {
        onDragEnteredProperty().set(value);
    }

    public final EventHandler<? super DragEvent> getOnDragEntered() {
        if (this.onDragEntered == null) {
            return null;
        }
        return this.onDragEntered.get();
    }

    public final ObjectProperty<EventHandler<? super DragEvent>> onDragEnteredProperty() {
        if (this.onDragEntered == null) {
            this.onDragEntered = new ObjectPropertyBase<EventHandler<? super DragEvent>>() { // from class: javafx.scene.Scene.44
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(DragEvent.DRAG_ENTERED, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onDragEntered";
                }
            };
        }
        return this.onDragEntered;
    }

    public final void setOnDragExited(EventHandler<? super DragEvent> value) {
        onDragExitedProperty().set(value);
    }

    public final EventHandler<? super DragEvent> getOnDragExited() {
        if (this.onDragExited == null) {
            return null;
        }
        return this.onDragExited.get();
    }

    public final ObjectProperty<EventHandler<? super DragEvent>> onDragExitedProperty() {
        if (this.onDragExited == null) {
            this.onDragExited = new ObjectPropertyBase<EventHandler<? super DragEvent>>() { // from class: javafx.scene.Scene.45
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(DragEvent.DRAG_EXITED, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onDragExited";
                }
            };
        }
        return this.onDragExited;
    }

    public final void setOnDragOver(EventHandler<? super DragEvent> value) {
        onDragOverProperty().set(value);
    }

    public final EventHandler<? super DragEvent> getOnDragOver() {
        if (this.onDragOver == null) {
            return null;
        }
        return this.onDragOver.get();
    }

    public final ObjectProperty<EventHandler<? super DragEvent>> onDragOverProperty() {
        if (this.onDragOver == null) {
            this.onDragOver = new ObjectPropertyBase<EventHandler<? super DragEvent>>() { // from class: javafx.scene.Scene.46
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(DragEvent.DRAG_OVER, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onDragOver";
                }
            };
        }
        return this.onDragOver;
    }

    public final void setOnDragDropped(EventHandler<? super DragEvent> value) {
        onDragDroppedProperty().set(value);
    }

    public final EventHandler<? super DragEvent> getOnDragDropped() {
        if (this.onDragDropped == null) {
            return null;
        }
        return this.onDragDropped.get();
    }

    public final ObjectProperty<EventHandler<? super DragEvent>> onDragDroppedProperty() {
        if (this.onDragDropped == null) {
            this.onDragDropped = new ObjectPropertyBase<EventHandler<? super DragEvent>>() { // from class: javafx.scene.Scene.47
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(DragEvent.DRAG_DROPPED, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onDragDropped";
                }
            };
        }
        return this.onDragDropped;
    }

    public final void setOnDragDone(EventHandler<? super DragEvent> value) {
        onDragDoneProperty().set(value);
    }

    public final EventHandler<? super DragEvent> getOnDragDone() {
        if (this.onDragDone == null) {
            return null;
        }
        return this.onDragDone.get();
    }

    public final ObjectProperty<EventHandler<? super DragEvent>> onDragDoneProperty() {
        if (this.onDragDone == null) {
            this.onDragDone = new ObjectPropertyBase<EventHandler<? super DragEvent>>() { // from class: javafx.scene.Scene.48
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(DragEvent.DRAG_DONE, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onDragDone";
                }
            };
        }
        return this.onDragDone;
    }

    public Dragboard startDragAndDrop(TransferMode... transferModes) {
        return startDragAndDrop(this, transferModes);
    }

    public void startFullDrag() {
        startFullDrag(this);
    }

    Dragboard startDragAndDrop(EventTarget source, TransferMode... transferModes) {
        Toolkit.getToolkit().checkFxUserThread();
        if (this.dndGesture == null || this.dndGesture.dragDetected != DragDetectedState.PROCESSING) {
            throw new IllegalStateException("Cannot start drag and drop outside of DRAG_DETECTED event handler");
        }
        Set<TransferMode> set = EnumSet.noneOf(TransferMode.class);
        for (TransferMode tm : InputEventUtils.safeTransferModes(transferModes)) {
            set.add(tm);
        }
        return this.dndGesture.startDrag(source, set);
    }

    void startFullDrag(EventTarget source) {
        Toolkit.getToolkit().checkFxUserThread();
        if (this.dndGesture.dragDetected != DragDetectedState.PROCESSING) {
            throw new IllegalStateException("Cannot start full drag outside of DRAG_DETECTED event handler");
        }
        if (this.dndGesture != null) {
            this.dndGesture.startFullPDR(source);
            return;
        }
        throw new IllegalStateException("Cannot start full drag when mouse button is not pressed");
    }

    public final void setOnKeyPressed(EventHandler<? super KeyEvent> value) {
        onKeyPressedProperty().set(value);
    }

    public final EventHandler<? super KeyEvent> getOnKeyPressed() {
        if (this.onKeyPressed == null) {
            return null;
        }
        return this.onKeyPressed.get();
    }

    public final ObjectProperty<EventHandler<? super KeyEvent>> onKeyPressedProperty() {
        if (this.onKeyPressed == null) {
            this.onKeyPressed = new ObjectPropertyBase<EventHandler<? super KeyEvent>>() { // from class: javafx.scene.Scene.49
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(KeyEvent.KEY_PRESSED, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onKeyPressed";
                }
            };
        }
        return this.onKeyPressed;
    }

    public final void setOnKeyReleased(EventHandler<? super KeyEvent> value) {
        onKeyReleasedProperty().set(value);
    }

    public final EventHandler<? super KeyEvent> getOnKeyReleased() {
        if (this.onKeyReleased == null) {
            return null;
        }
        return this.onKeyReleased.get();
    }

    public final ObjectProperty<EventHandler<? super KeyEvent>> onKeyReleasedProperty() {
        if (this.onKeyReleased == null) {
            this.onKeyReleased = new ObjectPropertyBase<EventHandler<? super KeyEvent>>() { // from class: javafx.scene.Scene.50
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(KeyEvent.KEY_RELEASED, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onKeyReleased";
                }
            };
        }
        return this.onKeyReleased;
    }

    public final void setOnKeyTyped(EventHandler<? super KeyEvent> value) {
        onKeyTypedProperty().set(value);
    }

    public final EventHandler<? super KeyEvent> getOnKeyTyped() {
        if (this.onKeyTyped == null) {
            return null;
        }
        return this.onKeyTyped.get();
    }

    public final ObjectProperty<EventHandler<? super KeyEvent>> onKeyTypedProperty() {
        if (this.onKeyTyped == null) {
            this.onKeyTyped = new ObjectPropertyBase<EventHandler<? super KeyEvent>>() { // from class: javafx.scene.Scene.51
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(KeyEvent.KEY_TYPED, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onKeyTyped";
                }
            };
        }
        return this.onKeyTyped;
    }

    public final void setOnInputMethodTextChanged(EventHandler<? super InputMethodEvent> value) {
        onInputMethodTextChangedProperty().set(value);
    }

    public final EventHandler<? super InputMethodEvent> getOnInputMethodTextChanged() {
        if (this.onInputMethodTextChanged == null) {
            return null;
        }
        return this.onInputMethodTextChanged.get();
    }

    public final ObjectProperty<EventHandler<? super InputMethodEvent>> onInputMethodTextChangedProperty() {
        if (this.onInputMethodTextChanged == null) {
            this.onInputMethodTextChanged = new ObjectPropertyBase<EventHandler<? super InputMethodEvent>>() { // from class: javafx.scene.Scene.52
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.setEventHandler(InputMethodEvent.INPUT_METHOD_TEXT_CHANGED, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onInputMethodTextChanged";
                }
            };
        }
        return this.onInputMethodTextChanged;
    }

    /* loaded from: jfxrt.jar:javafx/scene/Scene$TargetWrapper.class */
    private static class TargetWrapper {
        private Scene scene;
        private Node node;
        private PickResult result;

        private TargetWrapper() {
        }

        public void fillHierarchy(List<EventTarget> list) {
            list.clear();
            Node subScene = this.node;
            while (true) {
                Node n2 = subScene;
                if (n2 == null) {
                    break;
                }
                list.add(n2);
                Parent p2 = n2.getParent();
                subScene = p2 != null ? p2 : n2.getSubScene();
            }
            if (this.scene != null) {
                list.add(this.scene);
            }
        }

        public EventTarget getEventTarget() {
            return this.node != null ? this.node : this.scene;
        }

        public Cursor getCursor() {
            Cursor cursor = null;
            if (this.node != null) {
                cursor = this.node.getCursor();
                Node parent = this.node.getParent();
                while (true) {
                    Node n2 = parent;
                    if (cursor != null || n2 == null) {
                        break;
                    }
                    cursor = n2.getCursor();
                    Parent p2 = n2.getParent();
                    parent = p2 != null ? p2 : n2.getSubScene();
                }
            }
            return cursor;
        }

        public void clear() {
            set(null, null);
            this.result = null;
        }

        public void setNodeResult(PickResult result) {
            if (result != null) {
                this.result = result;
                Node n2 = result.getIntersectedNode();
                set(n2, n2.getScene());
            }
        }

        public void setSceneResult(PickResult result, Scene scene) {
            if (result != null) {
                this.result = result;
                set(null, scene);
            }
        }

        public PickResult getResult() {
            return this.result;
        }

        public void copy(TargetWrapper tw) {
            this.node = tw.node;
            this.scene = tw.scene;
            this.result = tw.result;
        }

        private void set(Node n2, Scene s2) {
            this.node = n2;
            this.scene = s2;
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

    public final void setNodeOrientation(NodeOrientation orientation) {
        nodeOrientationProperty().set(orientation);
    }

    public final NodeOrientation getNodeOrientation() {
        return this.nodeOrientation == null ? defaultNodeOrientation : this.nodeOrientation.get();
    }

    public final ObjectProperty<NodeOrientation> nodeOrientationProperty() {
        if (this.nodeOrientation == null) {
            this.nodeOrientation = new StyleableObjectProperty<NodeOrientation>(defaultNodeOrientation) { // from class: javafx.scene.Scene.53
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Scene.this.sceneEffectiveOrientationInvalidated();
                    Scene.this.getRoot().applyCss();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scene.this;
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
        if (this.effectiveNodeOrientation == null) {
            this.effectiveNodeOrientation = calcEffectiveNodeOrientation();
        }
        return this.effectiveNodeOrientation;
    }

    public final ReadOnlyObjectProperty<NodeOrientation> effectiveNodeOrientationProperty() {
        if (this.effectiveNodeOrientationProperty == null) {
            this.effectiveNodeOrientationProperty = new EffectiveOrientationProperty();
        }
        return this.effectiveNodeOrientationProperty;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void parentEffectiveOrientationInvalidated() {
        if (getNodeOrientation() == NodeOrientation.INHERIT) {
            sceneEffectiveOrientationInvalidated();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sceneEffectiveOrientationInvalidated() {
        this.effectiveNodeOrientation = null;
        if (this.effectiveNodeOrientationProperty != null) {
            this.effectiveNodeOrientationProperty.invalidate();
        }
        getRoot().parentResolvedOrientationInvalidated();
    }

    private NodeOrientation calcEffectiveNodeOrientation() {
        Scene scene;
        NodeOrientation orientation = getNodeOrientation();
        if (orientation == NodeOrientation.INHERIT) {
            Window window = getWindow();
            if (window != null) {
                Window parent = null;
                if (window instanceof Stage) {
                    parent = ((Stage) window).getOwner();
                } else if (window instanceof PopupWindow) {
                    parent = ((PopupWindow) window).getOwnerWindow();
                }
                if (parent != null && (scene = parent.getScene()) != null) {
                    return scene.getEffectiveNodeOrientation();
                }
            }
            return NodeOrientation.LEFT_TO_RIGHT;
        }
        return orientation;
    }

    /* loaded from: jfxrt.jar:javafx/scene/Scene$EffectiveOrientationProperty.class */
    private final class EffectiveOrientationProperty extends ReadOnlyObjectPropertyBase<NodeOrientation> {
        private EffectiveOrientationProperty() {
        }

        @Override // javafx.beans.value.ObservableObjectValue
        public NodeOrientation get() {
            return Scene.this.getEffectiveNodeOrientation();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Scene.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "effectiveNodeOrientation";
        }

        public void invalidate() {
            fireValueChangedEvent();
        }
    }

    Accessible removeAccessible(Node node) {
        if (this.accMap == null) {
            return null;
        }
        return this.accMap.remove(node);
    }

    void addAccessible(Node node, Accessible acc) {
        if (this.accMap == null) {
            this.accMap = new HashMap();
        }
        this.accMap.put(node, acc);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disposeAccessibles() {
        if (this.accMap != null) {
            for (Map.Entry<Node, Accessible> entry : this.accMap.entrySet()) {
                Node node = entry.getKey();
                Accessible acc = entry.getValue();
                if (node.accessible != null) {
                    if (node.accessible == acc) {
                        System.err.println("[A11y] 'node.accessible == acc' should never happen.");
                    }
                    if (node.getScene() == this) {
                        System.err.println("[A11y] 'node.getScene() == this' should never happen.");
                    }
                    acc.dispose();
                } else if (node.getScene() == this) {
                    node.accessible = acc;
                } else {
                    acc.dispose();
                }
            }
            this.accMap.clear();
        }
    }

    Accessible getAccessible() {
        if (this.impl_peer == null) {
            return null;
        }
        if (this.accessible == null) {
            this.accessible = Application.GetApplication().createAccessible();
            this.accessible.setEventHandler(new Accessible.EventHandler() { // from class: javafx.scene.Scene.54
                @Override // com.sun.glass.ui.Accessible.EventHandler
                public AccessControlContext getAccessControlContext() {
                    return Scene.this.impl_getPeer().getAccessControlContext();
                }

                @Override // com.sun.glass.ui.Accessible.EventHandler
                public Object getAttribute(AccessibleAttribute attribute, Object... parameters) {
                    Node node;
                    switch (AnonymousClass55.$SwitchMap$javafx$scene$AccessibleAttribute[attribute.ordinal()]) {
                        case 1:
                            Parent root = Scene.this.getRoot();
                            if (root != null) {
                                return FXCollections.observableArrayList(root);
                            }
                            break;
                        case 2:
                            Window w2 = Scene.this.getWindow();
                            if (w2 instanceof Stage) {
                                return ((Stage) w2).getTitle();
                            }
                            break;
                        case 3:
                            Window window = Scene.this.getWindow();
                            Point2D pt = (Point2D) parameters[0];
                            PickResult res = Scene.this.pick((pt.getX() - Scene.this.getX()) - window.getX(), (pt.getY() - Scene.this.getY()) - window.getY());
                            return (res == null || (node = res.getIntersectedNode()) == null) ? Scene.this.getRoot() : node;
                        case 4:
                            if (Scene.this.getRoot() != null && Scene.this.getRoot().getAccessibleRole() == AccessibleRole.DIALOG) {
                                return AccessibleRole.DIALOG;
                            }
                            return AccessibleRole.PARENT;
                        case 5:
                            return Scene.this;
                        case 6:
                            if (Scene.this.transientFocusContainer != null) {
                                return Scene.this.transientFocusContainer.queryAccessibleAttribute(AccessibleAttribute.FOCUS_NODE, new Object[0]);
                            }
                            return Scene.this.getFocusOwner();
                    }
                    return super.getAttribute(attribute, parameters);
                }
            });
            PlatformImpl.accessibilityActiveProperty().set(true);
        }
        return this.accessible;
    }

    /* renamed from: javafx.scene.Scene$55, reason: invalid class name */
    /* loaded from: jfxrt.jar:javafx/scene/Scene$55.class */
    static /* synthetic */ class AnonymousClass55 {
        static final /* synthetic */ int[] $SwitchMap$javafx$scene$AccessibleAttribute = new int[AccessibleAttribute.values().length];

        static {
            try {
                $SwitchMap$javafx$scene$AccessibleAttribute[AccessibleAttribute.CHILDREN.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$javafx$scene$AccessibleAttribute[AccessibleAttribute.TEXT.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$javafx$scene$AccessibleAttribute[AccessibleAttribute.NODE_AT_POINT.ordinal()] = 3;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$javafx$scene$AccessibleAttribute[AccessibleAttribute.ROLE.ordinal()] = 4;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$javafx$scene$AccessibleAttribute[AccessibleAttribute.SCENE.ordinal()] = 5;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$javafx$scene$AccessibleAttribute[AccessibleAttribute.FOCUS_NODE.ordinal()] = 6;
            } catch (NoSuchFieldError e7) {
            }
            $SwitchMap$javafx$scene$input$TouchPoint$State = new int[TouchPoint.State.values().length];
            try {
                $SwitchMap$javafx$scene$input$TouchPoint$State[TouchPoint.State.MOVED.ordinal()] = 1;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$javafx$scene$input$TouchPoint$State[TouchPoint.State.PRESSED.ordinal()] = 2;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$javafx$scene$input$TouchPoint$State[TouchPoint.State.RELEASED.ordinal()] = 3;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$javafx$scene$input$TouchPoint$State[TouchPoint.State.STATIONARY.ordinal()] = 4;
            } catch (NoSuchFieldError e11) {
            }
        }
    }
}
