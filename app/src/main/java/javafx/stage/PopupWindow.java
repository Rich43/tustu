package javafx.stage;

import com.sun.javafx.event.DirectEvent;
import com.sun.javafx.event.EventHandlerManager;
import com.sun.javafx.event.EventRedirector;
import com.sun.javafx.event.EventUtil;
import com.sun.javafx.perf.PerformanceTracker;
import com.sun.javafx.scene.SceneHelper;
import com.sun.javafx.stage.FocusUngrabEvent;
import com.sun.javafx.stage.PopupWindowPeerListener;
import com.sun.javafx.stage.WindowCloseRequestHandler;
import com.sun.javafx.stage.WindowEventDispatcher;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.util.Utils;
import java.security.AllPermission;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;

/* loaded from: jfxrt.jar:javafx/stage/PopupWindow.class */
public abstract class PopupWindow extends Window {
    private Window rootWindow;
    private Bounds cachedExtendedBounds;
    private Bounds cachedAnchorBounds;
    private ChangeListener<Boolean> ownerFocusedListener;
    private boolean autofixActive;
    private boolean autohideActive;
    private final List<PopupWindow> children = new ArrayList();
    private final InvalidationListener popupWindowUpdater = new InvalidationListener() { // from class: javafx.stage.PopupWindow.1
        @Override // javafx.beans.InvalidationListener
        public void invalidated(Observable observable) {
            PopupWindow.this.cachedExtendedBounds = null;
            PopupWindow.this.cachedAnchorBounds = null;
            PopupWindow.this.updateWindow(PopupWindow.this.getAnchorX(), PopupWindow.this.getAnchorY());
        }
    };
    private ChangeListener<Boolean> changeListener = (observable, oldValue, newValue) -> {
        if (oldValue.booleanValue() && !newValue.booleanValue()) {
            hide();
        }
    };
    private WeakChangeListener<Boolean> weakOwnerNodeListener = new WeakChangeListener<>(this.changeListener);
    private ReadOnlyObjectWrapper<Window> ownerWindow = new ReadOnlyObjectWrapper<>(this, "ownerWindow");
    private ReadOnlyObjectWrapper<Node> ownerNode = new ReadOnlyObjectWrapper<>(this, "ownerNode");
    private BooleanProperty autoFix = new BooleanPropertyBase(true) { // from class: javafx.stage.PopupWindow.3
        @Override // javafx.beans.property.BooleanPropertyBase
        protected void invalidated() {
            PopupWindow.this.handleAutofixActivation(PopupWindow.this.isShowing(), get());
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return PopupWindow.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "autoFix";
        }
    };
    private BooleanProperty autoHide = new BooleanPropertyBase() { // from class: javafx.stage.PopupWindow.4
        @Override // javafx.beans.property.BooleanPropertyBase
        protected void invalidated() {
            PopupWindow.this.handleAutohideActivation(PopupWindow.this.isShowing(), get());
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return PopupWindow.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "autoHide";
        }
    };
    private ObjectProperty<EventHandler<Event>> onAutoHide = new SimpleObjectProperty(this, "onAutoHide");
    private BooleanProperty hideOnEscape = new SimpleBooleanProperty(this, "hideOnEscape", true);
    private BooleanProperty consumeAutoHidingEvents = new SimpleBooleanProperty(this, "consumeAutoHidingEvents", true);
    private final ReadOnlyDoubleWrapper anchorX = new ReadOnlyDoubleWrapper(this, "anchorX", Double.NaN);
    private final ReadOnlyDoubleWrapper anchorY = new ReadOnlyDoubleWrapper(this, "anchorY", Double.NaN);
    private final ObjectProperty<AnchorLocation> anchorLocation = new ObjectPropertyBase<AnchorLocation>(AnchorLocation.WINDOW_TOP_LEFT) { // from class: javafx.stage.PopupWindow.5
        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            PopupWindow.this.cachedAnchorBounds = null;
            PopupWindow.this.updateWindow(PopupWindow.this.windowToAnchorX(PopupWindow.this.getX()), PopupWindow.this.windowToAnchorY(PopupWindow.this.getY()));
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return PopupWindow.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "anchorLocation";
        }
    };

    public PopupWindow() {
        Pane popupRoot = new Pane();
        popupRoot.setBackground(Background.EMPTY);
        popupRoot.getStyleClass().add("popup");
        final Scene scene = SceneHelper.createPopupScene(popupRoot);
        scene.setFill(null);
        super.setScene(scene);
        popupRoot.layoutBoundsProperty().addListener(this.popupWindowUpdater);
        popupRoot.boundsInLocalProperty().addListener(this.popupWindowUpdater);
        scene.rootProperty().addListener(new InvalidationListener() { // from class: javafx.stage.PopupWindow.2
            private Node oldRoot;

            {
                this.oldRoot = scene.getRoot();
            }

            @Override // javafx.beans.InvalidationListener
            public void invalidated(Observable observable) {
                Node newRoot = scene.getRoot();
                if (this.oldRoot != newRoot) {
                    if (this.oldRoot != null) {
                        this.oldRoot.layoutBoundsProperty().removeListener(PopupWindow.this.popupWindowUpdater);
                        this.oldRoot.boundsInLocalProperty().removeListener(PopupWindow.this.popupWindowUpdater);
                        this.oldRoot.getStyleClass().remove("popup");
                    }
                    if (newRoot != null) {
                        newRoot.layoutBoundsProperty().addListener(PopupWindow.this.popupWindowUpdater);
                        newRoot.boundsInLocalProperty().addListener(PopupWindow.this.popupWindowUpdater);
                        newRoot.getStyleClass().add("popup");
                    }
                    this.oldRoot = newRoot;
                    PopupWindow.this.cachedExtendedBounds = null;
                    PopupWindow.this.cachedAnchorBounds = null;
                    PopupWindow.this.updateWindow(PopupWindow.this.getAnchorX(), PopupWindow.this.getAnchorY());
                }
            }
        });
    }

    @Deprecated
    protected ObservableList<Node> getContent() {
        Parent rootNode = getScene().getRoot();
        if (rootNode instanceof Group) {
            return ((Group) rootNode).getChildren();
        }
        if (rootNode instanceof Pane) {
            return ((Pane) rootNode).getChildren();
        }
        throw new IllegalStateException("The content of the Popup can't be accessed");
    }

    public final Window getOwnerWindow() {
        return this.ownerWindow.get();
    }

    public final ReadOnlyObjectProperty<Window> ownerWindowProperty() {
        return this.ownerWindow.getReadOnlyProperty();
    }

    public final Node getOwnerNode() {
        return this.ownerNode.get();
    }

    public final ReadOnlyObjectProperty<Node> ownerNodeProperty() {
        return this.ownerNode.getReadOnlyProperty();
    }

    @Override // javafx.stage.Window
    protected final void setScene(Scene scene) {
        throw new UnsupportedOperationException();
    }

    public final void setAutoFix(boolean value) {
        this.autoFix.set(value);
    }

    public final boolean isAutoFix() {
        return this.autoFix.get();
    }

    public final BooleanProperty autoFixProperty() {
        return this.autoFix;
    }

    public final void setAutoHide(boolean value) {
        this.autoHide.set(value);
    }

    public final boolean isAutoHide() {
        return this.autoHide.get();
    }

    public final BooleanProperty autoHideProperty() {
        return this.autoHide;
    }

    public final void setOnAutoHide(EventHandler<Event> value) {
        this.onAutoHide.set(value);
    }

    public final EventHandler<Event> getOnAutoHide() {
        return this.onAutoHide.get();
    }

    public final ObjectProperty<EventHandler<Event>> onAutoHideProperty() {
        return this.onAutoHide;
    }

    public final void setHideOnEscape(boolean value) {
        this.hideOnEscape.set(value);
    }

    public final boolean isHideOnEscape() {
        return this.hideOnEscape.get();
    }

    public final BooleanProperty hideOnEscapeProperty() {
        return this.hideOnEscape;
    }

    public final void setConsumeAutoHidingEvents(boolean value) {
        this.consumeAutoHidingEvents.set(value);
    }

    public final boolean getConsumeAutoHidingEvents() {
        return this.consumeAutoHidingEvents.get();
    }

    public final BooleanProperty consumeAutoHidingEventsProperty() {
        return this.consumeAutoHidingEvents;
    }

    public void show(Window owner) {
        validateOwnerWindow(owner);
        showImpl(owner);
    }

    public void show(Node ownerNode, double anchorX, double anchorY) {
        if (ownerNode == null) {
            throw new NullPointerException("The owner node must not be null");
        }
        Scene ownerNodeScene = ownerNode.getScene();
        if (ownerNodeScene == null || ownerNodeScene.getWindow() == null) {
            throw new IllegalArgumentException("The owner node needs to be associated with a window");
        }
        Window newOwnerWindow = ownerNodeScene.getWindow();
        validateOwnerWindow(newOwnerWindow);
        this.ownerNode.set(ownerNode);
        if (ownerNode != null) {
            ownerNode.visibleProperty().addListener(this.weakOwnerNodeListener);
        }
        updateWindow(anchorX, anchorY);
        showImpl(newOwnerWindow);
    }

    public void show(Window ownerWindow, double anchorX, double anchorY) {
        validateOwnerWindow(ownerWindow);
        updateWindow(anchorX, anchorY);
        showImpl(ownerWindow);
    }

    private void showImpl(Window owner) {
        this.ownerWindow.set(owner);
        if (owner instanceof PopupWindow) {
            ((PopupWindow) owner).children.add(this);
        }
        if (owner != null) {
            owner.showingProperty().addListener(this.weakOwnerNodeListener);
        }
        Scene sceneValue = getScene();
        SceneHelper.parentEffectiveOrientationInvalidated(sceneValue);
        Scene ownerScene = getRootWindow(owner).getScene();
        if (ownerScene != null) {
            if (ownerScene.getUserAgentStylesheet() != null) {
                sceneValue.setUserAgentStylesheet(ownerScene.getUserAgentStylesheet());
            }
            sceneValue.getStylesheets().setAll(ownerScene.getStylesheets());
            if (sceneValue.getCursor() == null) {
                sceneValue.setCursor(ownerScene.getCursor());
            }
        }
        if (getRootWindow(owner).isShowing()) {
            show();
        }
    }

    @Override // javafx.stage.Window
    public void hide() {
        for (PopupWindow c2 : this.children) {
            if (c2.isShowing()) {
                c2.hide();
            }
        }
        this.children.clear();
        super.hide();
        if (getOwnerWindow() != null) {
            getOwnerWindow().showingProperty().removeListener(this.weakOwnerNodeListener);
        }
        if (getOwnerNode() != null) {
            getOwnerNode().visibleProperty().removeListener(this.weakOwnerNodeListener);
        }
    }

    @Override // javafx.stage.Window
    @Deprecated
    protected void impl_visibleChanging(boolean visible) {
        StageStyle popupStyle;
        super.impl_visibleChanging(visible);
        PerformanceTracker.logEvent("PopupWindow.storeVisible for [PopupWindow]");
        Toolkit toolkit = Toolkit.getToolkit();
        if (visible && this.impl_peer == null) {
            try {
                SecurityManager securityManager = System.getSecurityManager();
                if (securityManager != null) {
                    securityManager.checkPermission(new AllPermission());
                }
                popupStyle = StageStyle.TRANSPARENT;
            } catch (SecurityException e2) {
                popupStyle = StageStyle.UNDECORATED;
            }
            this.impl_peer = toolkit.createTKPopupStage(this, popupStyle, getOwnerWindow().impl_getPeer(), this.acc);
            this.peerListener = new PopupWindowPeerListener(this);
        }
    }

    @Override // javafx.stage.Window
    @Deprecated
    protected void impl_visibleChanged(boolean visible) {
        super.impl_visibleChanged(visible);
        Window ownerWindowValue = getOwnerWindow();
        if (visible) {
            this.rootWindow = getRootWindow(ownerWindowValue);
            startMonitorOwnerEvents(ownerWindowValue);
            bindOwnerFocusedProperty(ownerWindowValue);
            setFocused(ownerWindowValue.isFocused());
            handleAutofixActivation(true, isAutoFix());
            handleAutohideActivation(true, isAutoHide());
        } else {
            stopMonitorOwnerEvents(ownerWindowValue);
            unbindOwnerFocusedProperty(ownerWindowValue);
            setFocused(false);
            handleAutofixActivation(false, isAutoFix());
            handleAutohideActivation(false, isAutoHide());
            this.rootWindow = null;
        }
        PerformanceTracker.logEvent("PopupWindow.storeVisible for [PopupWindow] finished");
    }

    public final void setAnchorX(double value) {
        updateWindow(value, getAnchorY());
    }

    public final double getAnchorX() {
        return this.anchorX.get();
    }

    public final ReadOnlyDoubleProperty anchorXProperty() {
        return this.anchorX.getReadOnlyProperty();
    }

    public final void setAnchorY(double value) {
        updateWindow(getAnchorX(), value);
    }

    public final double getAnchorY() {
        return this.anchorY.get();
    }

    public final ReadOnlyDoubleProperty anchorYProperty() {
        return this.anchorY.getReadOnlyProperty();
    }

    public final void setAnchorLocation(AnchorLocation value) {
        this.anchorLocation.set(value);
    }

    public final AnchorLocation getAnchorLocation() {
        return this.anchorLocation.get();
    }

    public final ObjectProperty<AnchorLocation> anchorLocationProperty() {
        return this.anchorLocation;
    }

    /* loaded from: jfxrt.jar:javafx/stage/PopupWindow$AnchorLocation.class */
    public enum AnchorLocation {
        WINDOW_TOP_LEFT(0.0d, 0.0d, false),
        WINDOW_TOP_RIGHT(1.0d, 0.0d, false),
        WINDOW_BOTTOM_LEFT(0.0d, 1.0d, false),
        WINDOW_BOTTOM_RIGHT(1.0d, 1.0d, false),
        CONTENT_TOP_LEFT(0.0d, 0.0d, true),
        CONTENT_TOP_RIGHT(1.0d, 0.0d, true),
        CONTENT_BOTTOM_LEFT(0.0d, 1.0d, true),
        CONTENT_BOTTOM_RIGHT(1.0d, 1.0d, true);

        private final double xCoef;
        private final double yCoef;
        private final boolean contentLocation;

        AnchorLocation(double xCoef, double yCoef, boolean contentLocation) {
            this.xCoef = xCoef;
            this.yCoef = yCoef;
            this.contentLocation = contentLocation;
        }

        double getXCoef() {
            return this.xCoef;
        }

        double getYCoef() {
            return this.yCoef;
        }

        boolean isContentLocation() {
            return this.contentLocation;
        }
    }

    @Override // javafx.stage.Window
    void setXInternal(double value) {
        updateWindow(windowToAnchorX(value), getAnchorY());
    }

    @Override // javafx.stage.Window
    void setYInternal(double value) {
        updateWindow(getAnchorX(), windowToAnchorY(value));
    }

    @Override // javafx.stage.Window
    void notifyLocationChanged(double newX, double newY) {
        super.notifyLocationChanged(newX, newY);
        this.anchorX.set(windowToAnchorX(newX));
        this.anchorY.set(windowToAnchorY(newY));
    }

    private Bounds getExtendedBounds() {
        if (this.cachedExtendedBounds == null) {
            Parent rootNode = getScene().getRoot();
            this.cachedExtendedBounds = union(rootNode.getLayoutBounds(), rootNode.getBoundsInLocal());
        }
        return this.cachedExtendedBounds;
    }

    private Bounds getAnchorBounds() {
        Bounds extendedBounds;
        if (this.cachedAnchorBounds == null) {
            if (getAnchorLocation().isContentLocation()) {
                extendedBounds = getScene().getRoot().getLayoutBounds();
            } else {
                extendedBounds = getExtendedBounds();
            }
            this.cachedAnchorBounds = extendedBounds;
        }
        return this.cachedAnchorBounds;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateWindow(double newAnchorX, double newAnchorY) {
        Rectangle2D visualBounds;
        AnchorLocation anchorLocationValue = getAnchorLocation();
        Parent rootNode = getScene().getRoot();
        Bounds extendedBounds = getExtendedBounds();
        Bounds anchorBounds = getAnchorBounds();
        double anchorXCoef = anchorLocationValue.getXCoef();
        double anchorYCoef = anchorLocationValue.getYCoef();
        double anchorDeltaX = anchorXCoef * anchorBounds.getWidth();
        double anchorDeltaY = anchorYCoef * anchorBounds.getHeight();
        double anchorScrMinX = newAnchorX - anchorDeltaX;
        double anchorScrMinY = newAnchorY - anchorDeltaY;
        if (this.autofixActive) {
            Screen currentScreen = Utils.getScreenForPoint(newAnchorX, newAnchorY);
            if (Utils.hasFullScreenStage(currentScreen)) {
                visualBounds = currentScreen.getBounds();
            } else {
                visualBounds = currentScreen.getVisualBounds();
            }
            Rectangle2D screenBounds = visualBounds;
            if (anchorXCoef <= 0.5d) {
                anchorScrMinX = Math.max(Math.min(anchorScrMinX, screenBounds.getMaxX() - anchorBounds.getWidth()), screenBounds.getMinX());
            } else {
                anchorScrMinX = Math.min(Math.max(anchorScrMinX, screenBounds.getMinX()), screenBounds.getMaxX() - anchorBounds.getWidth());
            }
            if (anchorYCoef <= 0.5d) {
                anchorScrMinY = Math.max(Math.min(anchorScrMinY, screenBounds.getMaxY() - anchorBounds.getHeight()), screenBounds.getMinY());
            } else {
                anchorScrMinY = Math.min(Math.max(anchorScrMinY, screenBounds.getMinY()), screenBounds.getMaxY() - anchorBounds.getHeight());
            }
        }
        double windowScrMinX = (anchorScrMinX - anchorBounds.getMinX()) + extendedBounds.getMinX();
        double windowScrMinY = (anchorScrMinY - anchorBounds.getMinY()) + extendedBounds.getMinY();
        setWidth(extendedBounds.getWidth());
        setHeight(extendedBounds.getHeight());
        rootNode.setTranslateX(-extendedBounds.getMinX());
        rootNode.setTranslateY(-extendedBounds.getMinY());
        if (!Double.isNaN(windowScrMinX)) {
            super.setXInternal(windowScrMinX);
        }
        if (!Double.isNaN(windowScrMinY)) {
            super.setYInternal(windowScrMinY);
        }
        this.anchorX.set(anchorScrMinX + anchorDeltaX);
        this.anchorY.set(anchorScrMinY + anchorDeltaY);
    }

    private Bounds union(Bounds bounds1, Bounds bounds2) {
        double minX = Math.min(bounds1.getMinX(), bounds2.getMinX());
        double minY = Math.min(bounds1.getMinY(), bounds2.getMinY());
        double maxX = Math.max(bounds1.getMaxX(), bounds2.getMaxX());
        double maxY = Math.max(bounds1.getMaxY(), bounds2.getMaxY());
        return new BoundingBox(minX, minY, maxX - minX, maxY - minY);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public double windowToAnchorX(double windowX) {
        Bounds anchorBounds = getAnchorBounds();
        return (windowX - getExtendedBounds().getMinX()) + anchorBounds.getMinX() + (getAnchorLocation().getXCoef() * anchorBounds.getWidth());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public double windowToAnchorY(double windowY) {
        Bounds anchorBounds = getAnchorBounds();
        return (windowY - getExtendedBounds().getMinY()) + anchorBounds.getMinY() + (getAnchorLocation().getYCoef() * anchorBounds.getHeight());
    }

    private static Window getRootWindow(Window win) {
        while (win instanceof PopupWindow) {
            win = ((PopupWindow) win).getOwnerWindow();
        }
        return win;
    }

    void doAutoHide() {
        hide();
        if (getOnAutoHide() != null) {
            getOnAutoHide().handle(new Event(this, this, Event.ANY));
        }
    }

    @Override // javafx.stage.Window
    WindowEventDispatcher createInternalEventDispatcher() {
        return new WindowEventDispatcher(new PopupEventRedirector(this), new WindowCloseRequestHandler(this), new EventHandlerManager(this));
    }

    @Override // javafx.stage.Window
    Window getWindowOwner() {
        return getOwnerWindow();
    }

    private void startMonitorOwnerEvents(Window ownerWindowValue) {
        EventRedirector parentEventRedirector = ownerWindowValue.getInternalEventDispatcher().getEventRedirector();
        parentEventRedirector.addEventDispatcher(getEventDispatcher());
    }

    private void stopMonitorOwnerEvents(Window ownerWindowValue) {
        EventRedirector parentEventRedirector = ownerWindowValue.getInternalEventDispatcher().getEventRedirector();
        parentEventRedirector.removeEventDispatcher(getEventDispatcher());
    }

    private void bindOwnerFocusedProperty(Window ownerWindowValue) {
        this.ownerFocusedListener = (observable, oldValue, newValue) -> {
            setFocused(newValue.booleanValue());
        };
        ownerWindowValue.focusedProperty().addListener(this.ownerFocusedListener);
    }

    private void unbindOwnerFocusedProperty(Window ownerWindowValue) {
        ownerWindowValue.focusedProperty().removeListener(this.ownerFocusedListener);
        this.ownerFocusedListener = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAutofixActivation(boolean visible, boolean autofix) {
        boolean newAutofixActive = visible && autofix;
        if (this.autofixActive != newAutofixActive) {
            this.autofixActive = newAutofixActive;
            if (newAutofixActive) {
                Screen.getScreens().addListener(this.popupWindowUpdater);
                updateWindow(getAnchorX(), getAnchorY());
            } else {
                Screen.getScreens().removeListener(this.popupWindowUpdater);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAutohideActivation(boolean visible, boolean autohide) {
        boolean newAutohideActive = visible && autohide;
        if (this.autohideActive != newAutohideActive) {
            this.autohideActive = newAutohideActive;
            if (newAutohideActive) {
                this.rootWindow.increaseFocusGrabCounter();
            } else {
                this.rootWindow.decreaseFocusGrabCounter();
            }
        }
    }

    private void validateOwnerWindow(Window owner) {
        if (owner == null) {
            throw new NullPointerException("Owner window must not be null");
        }
        if (wouldCreateCycle(owner, this)) {
            throw new IllegalArgumentException("Specified owner window would create cycle in the window hierarchy");
        }
        if (isShowing() && getOwnerWindow() != owner) {
            throw new IllegalStateException("Popup is already shown with different owner window");
        }
    }

    private static boolean wouldCreateCycle(Window parent, Window child) {
        while (parent != null) {
            if (parent == child) {
                return true;
            }
            parent = parent.getWindowOwner();
        }
        return false;
    }

    /* loaded from: jfxrt.jar:javafx/stage/PopupWindow$PopupEventRedirector.class */
    static class PopupEventRedirector extends EventRedirector {
        private static final KeyCombination ESCAPE_KEY_COMBINATION = KeyCombination.keyCombination("Esc");
        private final PopupWindow popupWindow;

        public PopupEventRedirector(PopupWindow popupWindow) {
            super(popupWindow);
            this.popupWindow = popupWindow;
        }

        @Override // com.sun.javafx.event.EventRedirector
        protected void handleRedirectedEvent(Object eventSource, Event event) {
            if (event instanceof KeyEvent) {
                handleKeyEvent((KeyEvent) event);
                return;
            }
            EventType<?> eventType = event.getEventType();
            if (eventType == MouseEvent.MOUSE_PRESSED || eventType == ScrollEvent.SCROLL) {
                handleAutoHidingEvents(eventSource, event);
            } else if (eventType == FocusUngrabEvent.FOCUS_UNGRAB) {
                handleFocusUngrabEvent();
            }
        }

        private void handleKeyEvent(KeyEvent event) {
            if (event.isConsumed()) {
                return;
            }
            Scene scene = this.popupWindow.getScene();
            if (scene != null) {
                EventTarget sceneFocusOwner = scene.getFocusOwner();
                EventTarget eventTarget = sceneFocusOwner != null ? sceneFocusOwner : scene;
                if (EventUtil.fireEvent(eventTarget, new DirectEvent(event.copyFor((Object) this.popupWindow, eventTarget))) == null) {
                    event.consume();
                    return;
                }
            }
            if (event.getEventType() == KeyEvent.KEY_PRESSED && ESCAPE_KEY_COMBINATION.match(event)) {
                handleEscapeKeyPressedEvent(event);
            }
        }

        private void handleEscapeKeyPressedEvent(Event event) {
            if (this.popupWindow.isHideOnEscape()) {
                this.popupWindow.doAutoHide();
                if (this.popupWindow.getConsumeAutoHidingEvents()) {
                    event.consume();
                }
            }
        }

        private void handleAutoHidingEvents(Object eventSource, Event event) {
            if (this.popupWindow.getOwnerWindow() == eventSource && this.popupWindow.isAutoHide() && !isOwnerNodeEvent(event)) {
                Event.fireEvent(this.popupWindow, new FocusUngrabEvent());
                this.popupWindow.doAutoHide();
                if (this.popupWindow.getConsumeAutoHidingEvents()) {
                    event.consume();
                }
            }
        }

        private void handleFocusUngrabEvent() {
            if (this.popupWindow.isAutoHide()) {
                this.popupWindow.doAutoHide();
            }
        }

        private boolean isOwnerNodeEvent(Event event) {
            Node ownerNode = this.popupWindow.getOwnerNode();
            if (ownerNode == null) {
                return false;
            }
            EventTarget eventTarget = event.getTarget();
            if (!(eventTarget instanceof Node)) {
                return false;
            }
            Node node = (Node) eventTarget;
            while (node != ownerNode) {
                node = node.getParent();
                if (node == null) {
                    return false;
                }
            }
            return true;
        }
    }
}
