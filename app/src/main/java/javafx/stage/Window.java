package javafx.stage;

import com.sun.javafx.css.StyleManager;
import com.sun.javafx.stage.WindowEventDispatcher;
import com.sun.javafx.stage.WindowHelper;
import com.sun.javafx.stage.WindowPeerListener;
import com.sun.javafx.tk.TKPulseListener;
import com.sun.javafx.tk.TKScene;
import com.sun.javafx.tk.TKStage;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.util.Utils;
import com.sun.javafx.util.WeakReferenceQueue;
import com.sun.media.jfxmedia.MetadataParser;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.AllPermission;
import java.util.HashMap;
import java.util.Iterator;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:javafx/stage/Window.class */
public class Window implements EventTarget {
    private static WeakReferenceQueue<Window> windowQueue;

    @Deprecated
    protected WindowPeerListener peerListener;

    @Deprecated
    protected volatile TKStage impl_peer;
    private static final float CENTER_ON_SCREEN_X_FRACTION = 0.5f;
    private static final float CENTER_ON_SCREEN_Y_FRACTION = 0.33333334f;
    private static final Object USER_DATA_KEY;
    private ObservableMap<Object, Object> properties;
    private DoubleProperty opacity;
    private ObjectProperty<EventHandler<WindowEvent>> onCloseRequest;
    private ObjectProperty<EventHandler<WindowEvent>> onShowing;
    private ObjectProperty<EventHandler<WindowEvent>> onShown;
    private ObjectProperty<EventHandler<WindowEvent>> onHiding;
    private ObjectProperty<EventHandler<WindowEvent>> onHidden;
    private ObjectProperty<EventDispatcher> eventDispatcher;
    private WindowEventDispatcher internalEventDispatcher;
    private int focusGrabCounter;
    static final /* synthetic */ boolean $assertionsDisabled;
    final AccessControlContext acc = AccessController.getContext();
    private TKBoundsConfigurator peerBoundsConfigurator = new TKBoundsConfigurator();
    private boolean sizeToScene = false;
    private boolean xExplicit = false;

    /* renamed from: x, reason: collision with root package name */
    private ReadOnlyDoubleWrapper f12763x = new ReadOnlyDoubleWrapper(this, LanguageTag.PRIVATEUSE, Double.NaN);
    private boolean yExplicit = false;

    /* renamed from: y, reason: collision with root package name */
    private ReadOnlyDoubleWrapper f12764y = new ReadOnlyDoubleWrapper(this, PdfOps.y_TOKEN, Double.NaN);
    private boolean widthExplicit = false;
    private ReadOnlyDoubleWrapper width = new ReadOnlyDoubleWrapper(this, MetadataParser.WIDTH_TAG_NAME, Double.NaN);
    private boolean heightExplicit = false;
    private ReadOnlyDoubleWrapper height = new ReadOnlyDoubleWrapper(this, MetadataParser.HEIGHT_TAG_NAME, Double.NaN);
    private ReadOnlyBooleanWrapper focused = new ReadOnlyBooleanWrapper() { // from class: javafx.stage.Window.2
        @Override // javafx.beans.property.BooleanPropertyBase
        protected void invalidated() {
            Window.this.focusChanged(get());
        }

        @Override // javafx.beans.property.SimpleBooleanProperty, javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Window.this;
        }

        @Override // javafx.beans.property.SimpleBooleanProperty, javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "focused";
        }
    };
    private SceneModel scene = new SceneModel();
    private ReadOnlyBooleanWrapper showing = new ReadOnlyBooleanWrapper() { // from class: javafx.stage.Window.9
        private boolean oldVisible;

        @Override // javafx.beans.property.BooleanPropertyBase
        protected void invalidated() throws SecurityException {
            boolean newVisible = get();
            if (this.oldVisible == newVisible) {
                return;
            }
            if (!this.oldVisible && newVisible) {
                Window.this.fireEvent(new WindowEvent(Window.this, WindowEvent.WINDOW_SHOWING));
            } else {
                Window.this.fireEvent(new WindowEvent(Window.this, WindowEvent.WINDOW_HIDING));
            }
            this.oldVisible = newVisible;
            Window.this.impl_visibleChanging(newVisible);
            if (!newVisible) {
                Window.windowQueue.remove(Window.this);
            } else {
                Window.this.hasBeenVisible = true;
                Window.windowQueue.add(Window.this);
            }
            Toolkit tk = Toolkit.getToolkit();
            if (Window.this.impl_peer != null) {
                if (newVisible) {
                    if (Window.this.peerListener == null) {
                        Window.this.peerListener = new WindowPeerListener(Window.this);
                    }
                    Window.this.impl_peer.setTKStageListener(Window.this.peerListener);
                    tk.addStageTkPulseListener(Window.this.peerBoundsConfigurator);
                    if (Window.this.getScene() != null) {
                        Window.this.getScene().impl_initPeer();
                        Window.this.impl_peer.setScene(Window.this.getScene().impl_getPeer());
                        Window.this.getScene().impl_preferredSize();
                    }
                    if (Window.this.getScene() != null && (!Window.this.widthExplicit || !Window.this.heightExplicit)) {
                        Window.this.adjustSize(true);
                    } else {
                        Window.this.peerBoundsConfigurator.setSize(Window.this.getWidth(), Window.this.getHeight(), -1.0d, -1.0d);
                    }
                    if (Window.this.xExplicit || Window.this.yExplicit) {
                        Window.this.peerBoundsConfigurator.setLocation(Window.this.getX(), Window.this.getY(), 0.0f, 0.0f);
                    } else {
                        Window.this.centerOnScreen();
                    }
                    Window.this.applyBounds();
                    Window.this.impl_peer.setOpacity((float) Window.this.getOpacity());
                    Window.this.impl_peer.setVisible(true);
                    Window.this.fireEvent(new WindowEvent(Window.this, WindowEvent.WINDOW_SHOWN));
                } else {
                    Window.this.impl_peer.setVisible(false);
                    Window.this.fireEvent(new WindowEvent(Window.this, WindowEvent.WINDOW_HIDDEN));
                    if (Window.this.getScene() != null) {
                        Window.this.impl_peer.setScene(null);
                        Window.this.getScene().impl_disposePeer();
                        StyleManager.getInstance().forget(Window.this.getScene());
                    }
                    tk.removeStageTkPulseListener(Window.this.peerBoundsConfigurator);
                    Window.this.impl_peer.setTKStageListener(null);
                    Window.this.impl_peer.close();
                }
            }
            if (newVisible) {
                tk.requestNextPulse();
            }
            Window.this.impl_visibleChanged(newVisible);
            if (Window.this.sizeToScene) {
                if (newVisible) {
                    Window.this.sizeToScene();
                }
                Window.this.sizeToScene = false;
            }
        }

        @Override // javafx.beans.property.SimpleBooleanProperty, javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Window.this;
        }

        @Override // javafx.beans.property.SimpleBooleanProperty, javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "showing";
        }
    };
    boolean hasBeenVisible = false;
    private final ReadOnlyObjectWrapper<Screen> screen = new ReadOnlyObjectWrapper<>(Screen.getPrimary());

    static {
        $assertionsDisabled = !Window.class.desiredAssertionStatus();
        windowQueue = new WeakReferenceQueue<>();
        WindowHelper.setWindowAccessor(new WindowHelper.WindowAccessor() { // from class: javafx.stage.Window.1
            @Override // com.sun.javafx.stage.WindowHelper.WindowAccessor
            public void notifyLocationChanged(Window window, double x2, double y2) {
                window.notifyLocationChanged(x2, y2);
            }

            @Override // com.sun.javafx.stage.WindowHelper.WindowAccessor
            public void notifySizeChanged(Window window, double width, double height) {
                window.notifySizeChanged(width, height);
            }

            @Override // com.sun.javafx.stage.WindowHelper.WindowAccessor
            public void notifyScreenChanged(Window window, Object from, Object to) {
                window.notifyScreenChanged(from, to);
            }

            @Override // com.sun.javafx.stage.WindowHelper.WindowAccessor
            public float getUIScale(Window window) {
                TKStage peer = window.impl_peer;
                if (peer == null) {
                    return 1.0f;
                }
                return peer.getUIScale();
            }

            @Override // com.sun.javafx.stage.WindowHelper.WindowAccessor
            public float getRenderScale(Window window) {
                TKStage peer = window.impl_peer;
                if (peer == null) {
                    return 1.0f;
                }
                return peer.getRenderScale();
            }

            @Override // com.sun.javafx.stage.WindowHelper.WindowAccessor
            public ReadOnlyObjectProperty<Screen> screenProperty(Window window) {
                return window.screenProperty();
            }

            @Override // com.sun.javafx.stage.WindowHelper.WindowAccessor
            public AccessControlContext getAccessControlContext(Window window) {
                return window.acc;
            }
        });
        USER_DATA_KEY = new Object();
    }

    @Deprecated
    public static Iterator<Window> impl_getWindows() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new AllPermission());
        }
        return windowQueue.iterator();
    }

    protected Window() {
        initializeInternalEventDispatcher();
    }

    @Deprecated
    public TKStage impl_getPeer() {
        return this.impl_peer;
    }

    @Deprecated
    public String impl_getMXWindowType() {
        return getClass().getSimpleName();
    }

    public void sizeToScene() {
        if (getScene() != null && this.impl_peer != null) {
            getScene().impl_preferredSize();
            adjustSize(false);
        } else {
            this.sizeToScene = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void adjustSize(boolean selfSizePriority) {
        if (getScene() != null && this.impl_peer != null) {
            double sceneWidth = getScene().getWidth();
            double cw = sceneWidth > 0.0d ? sceneWidth : -1.0d;
            double w2 = -1.0d;
            if (selfSizePriority && this.widthExplicit) {
                w2 = getWidth();
            } else if (cw <= 0.0d) {
                w2 = this.widthExplicit ? getWidth() : -1.0d;
            } else {
                this.widthExplicit = false;
            }
            double sceneHeight = getScene().getHeight();
            double ch = sceneHeight > 0.0d ? sceneHeight : -1.0d;
            double h2 = -1.0d;
            if (selfSizePriority && this.heightExplicit) {
                h2 = getHeight();
            } else if (ch <= 0.0d) {
                h2 = this.heightExplicit ? getHeight() : -1.0d;
            } else {
                this.heightExplicit = false;
            }
            this.peerBoundsConfigurator.setSize(w2, h2, cw, ch);
            applyBounds();
        }
    }

    public void centerOnScreen() {
        this.xExplicit = false;
        this.yExplicit = false;
        if (this.impl_peer != null) {
            Rectangle2D bounds = getWindowScreen().getVisualBounds();
            double centerX = bounds.getMinX() + ((bounds.getWidth() - getWidth()) * 0.5d);
            double centerY = bounds.getMinY() + ((bounds.getHeight() - getHeight()) * 0.3333333432674408d);
            this.f12763x.set(centerX);
            this.f12764y.set(centerY);
            this.peerBoundsConfigurator.setLocation(centerX, centerY, 0.5f, CENTER_ON_SCREEN_Y_FRACTION);
            applyBounds();
        }
    }

    public final void setX(double value) {
        setXInternal(value);
    }

    public final double getX() {
        return this.f12763x.get();
    }

    public final ReadOnlyDoubleProperty xProperty() {
        return this.f12763x.getReadOnlyProperty();
    }

    void setXInternal(double value) {
        this.f12763x.set(value);
        this.peerBoundsConfigurator.setX(value, 0.0f);
        this.xExplicit = true;
    }

    public final void setY(double value) {
        setYInternal(value);
    }

    public final double getY() {
        return this.f12764y.get();
    }

    public final ReadOnlyDoubleProperty yProperty() {
        return this.f12764y.getReadOnlyProperty();
    }

    void setYInternal(double value) {
        this.f12764y.set(value);
        this.peerBoundsConfigurator.setY(value, 0.0f);
        this.yExplicit = true;
    }

    void notifyLocationChanged(double newX, double newY) {
        this.f12763x.set(newX);
        this.f12764y.set(newY);
    }

    public final void setWidth(double value) {
        this.width.set(value);
        this.peerBoundsConfigurator.setWindowWidth(value);
        this.widthExplicit = true;
    }

    public final double getWidth() {
        return this.width.get();
    }

    public final ReadOnlyDoubleProperty widthProperty() {
        return this.width.getReadOnlyProperty();
    }

    public final void setHeight(double value) {
        this.height.set(value);
        this.peerBoundsConfigurator.setWindowHeight(value);
        this.heightExplicit = true;
    }

    public final double getHeight() {
        return this.height.get();
    }

    public final ReadOnlyDoubleProperty heightProperty() {
        return this.height.getReadOnlyProperty();
    }

    void notifySizeChanged(double newWidth, double newHeight) {
        this.width.set(newWidth);
        this.height.set(newHeight);
    }

    @Deprecated
    public final void setFocused(boolean value) {
        this.focused.set(value);
    }

    public final void requestFocus() {
        if (this.impl_peer != null) {
            this.impl_peer.requestFocus();
        }
    }

    public final boolean isFocused() {
        return this.focused.get();
    }

    public final ReadOnlyBooleanProperty focusedProperty() {
        return this.focused.getReadOnlyProperty();
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

    protected void setScene(Scene value) {
        this.scene.set(value);
    }

    public final Scene getScene() {
        return this.scene.get();
    }

    public final ReadOnlyObjectProperty<Scene> sceneProperty() {
        return this.scene.getReadOnlyProperty();
    }

    /* loaded from: jfxrt.jar:javafx/stage/Window$SceneModel.class */
    private final class SceneModel extends ReadOnlyObjectWrapper<Scene> {
        private Scene oldScene;

        private SceneModel() {
        }

        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            Scene newScene = get();
            if (this.oldScene == newScene) {
                return;
            }
            if (Window.this.impl_peer != null) {
                Toolkit.getToolkit().checkFxUserThread();
            }
            updatePeerScene(null);
            if (this.oldScene != null) {
                this.oldScene.impl_setWindow(null);
                StyleManager.getInstance().forget(this.oldScene);
            }
            if (newScene != null) {
                Window oldWindow = newScene.getWindow();
                if (oldWindow != null) {
                    oldWindow.setScene(null);
                }
                newScene.impl_setWindow(Window.this);
                updatePeerScene(newScene.impl_getPeer());
                if (Window.this.isShowing()) {
                    newScene.getRoot().impl_reapplyCSS();
                    if (!Window.this.widthExplicit || !Window.this.heightExplicit) {
                        Window.this.getScene().impl_preferredSize();
                        Window.this.adjustSize(true);
                    }
                }
            }
            this.oldScene = newScene;
        }

        @Override // javafx.beans.property.SimpleObjectProperty, javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Window.this;
        }

        @Override // javafx.beans.property.SimpleObjectProperty, javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "scene";
        }

        private void updatePeerScene(TKScene tkScene) {
            if (Window.this.impl_peer != null) {
                Window.this.impl_peer.setScene(tkScene);
            }
        }
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
            this.opacity = new DoublePropertyBase(1.0d) { // from class: javafx.stage.Window.3
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    if (Window.this.impl_peer != null) {
                        Window.this.impl_peer.setOpacity((float) get());
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Window.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "opacity";
                }
            };
        }
        return this.opacity;
    }

    public final void setOnCloseRequest(EventHandler<WindowEvent> value) {
        onCloseRequestProperty().set(value);
    }

    public final EventHandler<WindowEvent> getOnCloseRequest() {
        if (this.onCloseRequest != null) {
            return this.onCloseRequest.get();
        }
        return null;
    }

    public final ObjectProperty<EventHandler<WindowEvent>> onCloseRequestProperty() {
        if (this.onCloseRequest == null) {
            this.onCloseRequest = new ObjectPropertyBase<EventHandler<WindowEvent>>() { // from class: javafx.stage.Window.4
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Window.this.setEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Window.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onCloseRequest";
                }
            };
        }
        return this.onCloseRequest;
    }

    public final void setOnShowing(EventHandler<WindowEvent> value) {
        onShowingProperty().set(value);
    }

    public final EventHandler<WindowEvent> getOnShowing() {
        if (this.onShowing == null) {
            return null;
        }
        return this.onShowing.get();
    }

    public final ObjectProperty<EventHandler<WindowEvent>> onShowingProperty() {
        if (this.onShowing == null) {
            this.onShowing = new ObjectPropertyBase<EventHandler<WindowEvent>>() { // from class: javafx.stage.Window.5
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Window.this.setEventHandler(WindowEvent.WINDOW_SHOWING, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Window.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onShowing";
                }
            };
        }
        return this.onShowing;
    }

    public final void setOnShown(EventHandler<WindowEvent> value) {
        onShownProperty().set(value);
    }

    public final EventHandler<WindowEvent> getOnShown() {
        if (this.onShown == null) {
            return null;
        }
        return this.onShown.get();
    }

    public final ObjectProperty<EventHandler<WindowEvent>> onShownProperty() {
        if (this.onShown == null) {
            this.onShown = new ObjectPropertyBase<EventHandler<WindowEvent>>() { // from class: javafx.stage.Window.6
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Window.this.setEventHandler(WindowEvent.WINDOW_SHOWN, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Window.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onShown";
                }
            };
        }
        return this.onShown;
    }

    public final void setOnHiding(EventHandler<WindowEvent> value) {
        onHidingProperty().set(value);
    }

    public final EventHandler<WindowEvent> getOnHiding() {
        if (this.onHiding == null) {
            return null;
        }
        return this.onHiding.get();
    }

    public final ObjectProperty<EventHandler<WindowEvent>> onHidingProperty() {
        if (this.onHiding == null) {
            this.onHiding = new ObjectPropertyBase<EventHandler<WindowEvent>>() { // from class: javafx.stage.Window.7
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Window.this.setEventHandler(WindowEvent.WINDOW_HIDING, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Window.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onHiding";
                }
            };
        }
        return this.onHiding;
    }

    public final void setOnHidden(EventHandler<WindowEvent> value) {
        onHiddenProperty().set(value);
    }

    public final EventHandler<WindowEvent> getOnHidden() {
        if (this.onHidden == null) {
            return null;
        }
        return this.onHidden.get();
    }

    public final ObjectProperty<EventHandler<WindowEvent>> onHiddenProperty() {
        if (this.onHidden == null) {
            this.onHidden = new ObjectPropertyBase<EventHandler<WindowEvent>>() { // from class: javafx.stage.Window.8
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Window.this.setEventHandler(WindowEvent.WINDOW_HIDDEN, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Window.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onHidden";
                }
            };
        }
        return this.onHidden;
    }

    private void setShowing(boolean value) {
        Toolkit.getToolkit().checkFxUserThread();
        this.showing.set(value);
    }

    public final boolean isShowing() {
        return this.showing.get();
    }

    public final ReadOnlyBooleanProperty showingProperty() {
        return this.showing.getReadOnlyProperty();
    }

    protected void show() {
        setShowing(true);
    }

    public void hide() {
        setShowing(false);
    }

    @Deprecated
    protected void impl_visibleChanging(boolean visible) {
        if (visible && getScene() != null) {
            getScene().getRoot().impl_reapplyCSS();
        }
    }

    @Deprecated
    protected void impl_visibleChanged(boolean visible) {
        if (!$assertionsDisabled && this.impl_peer == null) {
            throw new AssertionError();
        }
        if (!visible) {
            this.peerListener = null;
            this.impl_peer = null;
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

    WindowEventDispatcher getInternalEventDispatcher() {
        initializeInternalEventDispatcher();
        return this.internalEventDispatcher;
    }

    private void initializeInternalEventDispatcher() {
        if (this.internalEventDispatcher == null) {
            this.internalEventDispatcher = createInternalEventDispatcher();
            this.eventDispatcher = new SimpleObjectProperty(this, "eventDispatcher", this.internalEventDispatcher);
        }
    }

    WindowEventDispatcher createInternalEventDispatcher() {
        return new WindowEventDispatcher(this);
    }

    public final void fireEvent(Event event) {
        Event.fireEvent(this, event);
    }

    @Override // javafx.event.EventTarget
    public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
        EventDispatcher eventDispatcherValue;
        if (this.eventDispatcher != null && (eventDispatcherValue = this.eventDispatcher.get()) != null) {
            tail = tail.prepend(eventDispatcherValue);
        }
        return tail;
    }

    void increaseFocusGrabCounter() {
        int i2 = this.focusGrabCounter + 1;
        this.focusGrabCounter = i2;
        if (i2 == 1 && this.impl_peer != null && isFocused()) {
            this.impl_peer.grabFocus();
        }
    }

    void decreaseFocusGrabCounter() {
        int i2 = this.focusGrabCounter - 1;
        this.focusGrabCounter = i2;
        if (i2 == 0 && this.impl_peer != null) {
            this.impl_peer.ungrabFocus();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void focusChanged(boolean newIsFocused) {
        if (this.focusGrabCounter > 0 && this.impl_peer != null && newIsFocused) {
            this.impl_peer.grabFocus();
        }
    }

    final void applyBounds() {
        this.peerBoundsConfigurator.apply();
    }

    Window getWindowOwner() {
        return null;
    }

    private Screen getWindowScreen() {
        Window window = this;
        do {
            if (!Double.isNaN(window.getX()) && !Double.isNaN(window.getY()) && !Double.isNaN(window.getWidth()) && !Double.isNaN(window.getHeight())) {
                return Utils.getScreenForRectangle(new Rectangle2D(window.getX(), window.getY(), window.getWidth(), window.getHeight()));
            }
            window = window.getWindowOwner();
        } while (window != null);
        return Screen.getPrimary();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ReadOnlyObjectProperty<Screen> screenProperty() {
        return this.screen.getReadOnlyProperty();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyScreenChanged(Object from, Object to) {
        this.screen.set(Screen.getScreenForNative(to));
    }

    /* loaded from: jfxrt.jar:javafx/stage/Window$TKBoundsConfigurator.class */
    private final class TKBoundsConfigurator implements TKPulseListener {

        /* renamed from: x, reason: collision with root package name */
        private double f12765x;

        /* renamed from: y, reason: collision with root package name */
        private double f12766y;
        private float xGravity;
        private float yGravity;
        private double windowWidth;
        private double windowHeight;
        private double clientWidth;
        private double clientHeight;
        private boolean dirty;

        public TKBoundsConfigurator() {
            reset();
        }

        public void setX(double x2, float xGravity) {
            this.f12765x = x2;
            this.xGravity = xGravity;
            setDirty();
        }

        public void setY(double y2, float yGravity) {
            this.f12766y = y2;
            this.yGravity = yGravity;
            setDirty();
        }

        public void setWindowWidth(double windowWidth) {
            this.windowWidth = windowWidth;
            setDirty();
        }

        public void setWindowHeight(double windowHeight) {
            this.windowHeight = windowHeight;
            setDirty();
        }

        public void setClientWidth(double clientWidth) {
            this.clientWidth = clientWidth;
            setDirty();
        }

        public void setClientHeight(double clientHeight) {
            this.clientHeight = clientHeight;
            setDirty();
        }

        public void setLocation(double x2, double y2, float xGravity, float yGravity) {
            this.f12765x = x2;
            this.f12766y = y2;
            this.xGravity = xGravity;
            this.yGravity = yGravity;
            setDirty();
        }

        public void setSize(double windowWidth, double windowHeight, double clientWidth, double clientHeight) {
            this.windowWidth = windowWidth;
            this.windowHeight = windowHeight;
            this.clientWidth = clientWidth;
            this.clientHeight = clientHeight;
            setDirty();
        }

        public void apply() {
            if (this.dirty) {
                Window.this.impl_peer.setBounds((float) (Double.isNaN(this.f12765x) ? 0.0d : this.f12765x), (float) (Double.isNaN(this.f12766y) ? 0.0d : this.f12766y), !Double.isNaN(this.f12765x), !Double.isNaN(this.f12766y), (float) this.windowWidth, (float) this.windowHeight, (float) this.clientWidth, (float) this.clientHeight, this.xGravity, this.yGravity);
                reset();
            }
        }

        @Override // com.sun.javafx.tk.TKPulseListener
        public void pulse() {
            apply();
        }

        private void reset() {
            this.f12765x = Double.NaN;
            this.f12766y = Double.NaN;
            this.xGravity = 0.0f;
            this.yGravity = 0.0f;
            this.windowWidth = -1.0d;
            this.windowHeight = -1.0d;
            this.clientWidth = -1.0d;
            this.clientHeight = -1.0d;
            this.dirty = false;
        }

        private void setDirty() {
            if (!this.dirty) {
                Toolkit.getToolkit().requestNextPulse();
                this.dirty = true;
            }
        }
    }
}
