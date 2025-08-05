package com.sun.glass.ui;

import com.sun.glass.events.WindowEvent;
import com.sun.prism.impl.PrismSettings;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXMLLoader;

/* loaded from: jfxrt.jar:com/sun/glass/ui/Window.class */
public abstract class Window {
    private long ptr;
    private static final LinkedList<Window> visibleWindows = new LinkedList<>();
    public static final int UNTITLED = 0;
    public static final int TITLED = 1;
    public static final int TRANSPARENT = 2;
    public static final int NORMAL = 0;
    public static final int UTILITY = 4;
    public static final int POPUP = 8;
    public static final int CLOSABLE = 16;
    public static final int MINIMIZABLE = 32;
    public static final int MAXIMIZABLE = 64;
    public static final int RIGHT_TO_LEFT = 128;
    public static final int UNIFIED = 256;
    private final Window owner;
    private final long parent;
    private final int styleMask;
    private final boolean isDecorated;
    private Screen screen;
    private EventHandler eventHandler;
    private volatile long delegatePtr = 0;
    private boolean shouldStartUndecoratedMove = false;
    private View view = null;
    private MenuBar menubar = null;
    private String title = "";
    private UndecoratedMoveResizeHelper helper = null;
    private int state = 1;
    private int level = 1;

    /* renamed from: x, reason: collision with root package name */
    private int f11831x = 0;

    /* renamed from: y, reason: collision with root package name */
    private int f11832y = 0;
    private int width = 0;
    private int height = 0;
    private float alpha = 1.0f;
    private float platformScale = 1.0f;
    private float renderScale = 1.0f;
    private Timer embeddedLocationTimer = null;
    private int lastKnownEmbeddedX = 0;
    private int lastKnownEmbeddedY = 0;
    private volatile boolean isResizable = false;
    private volatile boolean isVisible = false;
    private volatile boolean isFocused = false;
    private volatile boolean isFocusable = true;
    private volatile boolean isModal = false;
    private volatile int disableCount = 0;
    private int minimumWidth = 0;
    private int minimumHeight = 0;
    private int maximumWidth = Integer.MAX_VALUE;
    private int maximumHeight = Integer.MAX_VALUE;

    /* loaded from: jfxrt.jar:com/sun/glass/ui/Window$Level.class */
    public static final class Level {
        private static final int _MIN = 1;
        public static final int NORMAL = 1;
        public static final int FLOATING = 2;
        public static final int TOPMOST = 3;
        private static final int _MAX = 3;
    }

    /* loaded from: jfxrt.jar:com/sun/glass/ui/Window$State.class */
    public static final class State {
        public static final int NORMAL = 1;
        public static final int MINIMIZED = 2;
        public static final int MAXIMIZED = 3;
    }

    protected abstract long _createWindow(long j2, long j3, int i2);

    protected abstract long _createChildWindow(long j2);

    protected abstract boolean _close(long j2);

    protected abstract boolean _setView(long j2, View view);

    protected abstract boolean _setMenubar(long j2, long j3);

    protected abstract boolean _minimize(long j2, boolean z2);

    protected abstract boolean _maximize(long j2, boolean z2, boolean z3);

    protected abstract int _getEmbeddedX(long j2);

    protected abstract int _getEmbeddedY(long j2);

    protected abstract void _setBounds(long j2, int i2, int i3, boolean z2, boolean z3, int i4, int i5, int i6, int i7, float f2, float f3);

    protected abstract boolean _setVisible(long j2, boolean z2);

    protected abstract boolean _setResizable(long j2, boolean z2);

    protected abstract boolean _requestFocus(long j2, int i2);

    protected abstract void _setFocusable(long j2, boolean z2);

    protected abstract boolean _grabFocus(long j2);

    protected abstract void _ungrabFocus(long j2);

    protected abstract boolean _setTitle(long j2, String str);

    protected abstract void _setLevel(long j2, int i2);

    protected abstract void _setAlpha(long j2, float f2);

    protected abstract boolean _setBackground(long j2, float f2, float f3, float f4);

    protected abstract void _setEnabled(long j2, boolean z2);

    protected abstract boolean _setMinimumSize(long j2, int i2, int i3);

    protected abstract boolean _setMaximumSize(long j2, int i2, int i3);

    protected abstract void _setIcon(long j2, Pixels pixels);

    protected abstract void _setCursor(long j2, Cursor cursor);

    protected abstract void _toFront(long j2);

    protected abstract void _toBack(long j2);

    protected abstract void _enterModal(long j2);

    protected abstract void _enterModalWithWindow(long j2, long j3);

    protected abstract void _exitModal(long j2);

    protected abstract void _requestInput(long j2, String str, int i2, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13, double d14, double d15);

    protected abstract void _releaseInput(long j2);

    /* loaded from: jfxrt.jar:com/sun/glass/ui/Window$EventHandler.class */
    public static class EventHandler {
        public void handleWindowEvent(Window window, long time, int type) {
        }

        public void handleScreenChangedEvent(Window window, long time, Screen oldScreen, Screen newScreen) {
        }

        public void handleLevelEvent(int level) {
        }
    }

    public static synchronized List<Window> getWindows() {
        Application.checkEventThread();
        return Collections.unmodifiableList(visibleWindows);
    }

    public static List<Window> getWindowsClone() {
        Application.checkEventThread();
        return (List) visibleWindows.clone();
    }

    protected static void add(Window window) {
        visibleWindows.add(window);
    }

    protected static void addFirst(Window window) {
        visibleWindows.addFirst(window);
    }

    protected static void remove(Window window) {
        visibleWindows.remove(window);
    }

    protected Window(Window owner, Screen screen, int styleMask) {
        this.screen = null;
        Application.checkEventThread();
        switch (styleMask & 3) {
            case 0:
            case 1:
            case 2:
                switch (styleMask & 12) {
                    case 0:
                    case 4:
                    case 8:
                        if ((styleMask & 256) != 0 && !Application.GetApplication().supportsUnifiedWindows()) {
                            styleMask &= -257;
                        }
                        if ((styleMask & 2) != 0 && !Application.GetApplication().supportsTransparentWindows()) {
                            styleMask &= -3;
                        }
                        this.owner = owner;
                        this.parent = 0L;
                        this.styleMask = styleMask;
                        this.isDecorated = (this.styleMask & 1) != 0;
                        this.screen = screen != null ? screen : Screen.getMainScreen();
                        this.ptr = _createWindow(owner != null ? owner.getNativeHandle() : 0L, this.screen.getNativeScreen(), this.styleMask);
                        if (this.ptr == 0) {
                            throw new RuntimeException("could not create platform window");
                        }
                        return;
                    default:
                        throw new RuntimeException("The functional type should be NORMAL, POPUP, or UTILITY, but not a combination of these");
                }
            default:
                throw new RuntimeException("The visual kind should be UNTITLED, TITLED, or TRANSPARENT, but not a combination of these");
        }
    }

    protected Window(long parent) {
        this.screen = null;
        Application.checkEventThread();
        this.owner = null;
        this.parent = parent;
        this.styleMask = 0;
        this.isDecorated = false;
        this.screen = null;
        this.ptr = _createChildWindow(parent);
        if (this.ptr == 0) {
            throw new RuntimeException("could not create platform window");
        }
        if (this.screen == null) {
            this.screen = Screen.getMainScreen();
        }
    }

    public boolean isClosed() {
        Application.checkEventThread();
        return this.ptr == 0;
    }

    private void checkNotClosed() {
        if (this.ptr == 0) {
            throw new IllegalStateException("The window has already been closed");
        }
    }

    public void close() {
        Application.checkEventThread();
        if (this.view != null) {
            if (this.ptr != 0) {
                _setView(this.ptr, null);
            }
            this.view.setWindow(null);
            this.view.close();
            this.view = null;
        }
        if (this.ptr != 0) {
            _close(this.ptr);
        }
    }

    private boolean isChild() {
        Application.checkEventThread();
        return this.parent != 0;
    }

    public long getNativeWindow() {
        Application.checkEventThread();
        checkNotClosed();
        return this.delegatePtr != 0 ? this.delegatePtr : this.ptr;
    }

    public long getNativeHandle() {
        Application.checkEventThread();
        return this.delegatePtr != 0 ? this.delegatePtr : this.ptr;
    }

    public long getRawHandle() {
        return this.ptr;
    }

    public Window getOwner() {
        Application.checkEventThread();
        return this.owner;
    }

    public View getView() {
        Application.checkEventThread();
        return this.view;
    }

    public void setView(View view) {
        Window host;
        Application.checkEventThread();
        checkNotClosed();
        View oldView = getView();
        if (oldView == view) {
            return;
        }
        if (oldView != null) {
            oldView.setWindow(null);
        }
        if (view != null && (host = view.getWindow()) != null) {
            host.setView(null);
        }
        if (view != null && _setView(this.ptr, view)) {
            this.view = view;
            this.view.setWindow(this);
            if (!this.isDecorated) {
                this.helper = new UndecoratedMoveResizeHelper();
                return;
            }
            return;
        }
        _setView(this.ptr, null);
        this.view = null;
    }

    public Screen getScreen() {
        Application.checkEventThread();
        return this.screen;
    }

    protected void setScreen(Screen screen) {
        Application.checkEventThread();
        Screen old = this.screen;
        this.screen = screen;
        if (this.eventHandler != null) {
            if ((old == null && this.screen != null) || (old != null && !old.equals(this.screen))) {
                this.eventHandler.handleScreenChangedEvent(this, System.nanoTime(), old, this.screen);
            }
        }
    }

    public int getStyleMask() {
        Application.checkEventThread();
        return this.styleMask;
    }

    public MenuBar getMenuBar() {
        Application.checkEventThread();
        return this.menubar;
    }

    public void setMenuBar(MenuBar menubar) {
        Application.checkEventThread();
        checkNotClosed();
        if (_setMenubar(this.ptr, menubar.getNativeMenu())) {
            this.menubar = menubar;
        }
    }

    public boolean isDecorated() {
        Application.checkEventThread();
        return this.isDecorated;
    }

    public boolean isMinimized() {
        Application.checkEventThread();
        return this.state == 2;
    }

    public boolean minimize(boolean minimize) {
        Application.checkEventThread();
        checkNotClosed();
        _minimize(this.ptr, minimize);
        return isMinimized();
    }

    public boolean isMaximized() {
        Application.checkEventThread();
        return this.state == 3;
    }

    public boolean maximize(boolean maximize) {
        Application.checkEventThread();
        checkNotClosed();
        _maximize(this.ptr, maximize, isMaximized());
        return isMaximized();
    }

    public void setPlatformScale(float platformScale) {
        if (PrismSettings.allowHiDPIScaling) {
            this.platformScale = platformScale;
        }
    }

    public final float getPlatformScale() {
        return this.platformScale;
    }

    public void setRenderScale(float renderScale) {
        if (PrismSettings.allowHiDPIScaling) {
            this.renderScale = renderScale;
        }
    }

    public final float getRenderScale() {
        return this.renderScale;
    }

    public float getOutputScale() {
        return this.platformScale;
    }

    private void checkScreenLocation() {
        this.f11831x = _getEmbeddedX(this.ptr);
        this.f11832y = _getEmbeddedY(this.ptr);
        if (this.f11831x != this.lastKnownEmbeddedX || this.f11832y != this.lastKnownEmbeddedY) {
            this.lastKnownEmbeddedX = this.f11831x;
            this.lastKnownEmbeddedY = this.f11832y;
            handleWindowEvent(System.nanoTime(), 512);
        }
    }

    public int getX() {
        Application.checkEventThread();
        return this.f11831x;
    }

    public int getY() {
        Application.checkEventThread();
        return this.f11832y;
    }

    public int getWidth() {
        Application.checkEventThread();
        return this.width;
    }

    public int getHeight() {
        Application.checkEventThread();
        return this.height;
    }

    public void setBounds(int x2, int y2, boolean xSet, boolean ySet, int w2, int h2, int cw, int ch, float xGravity, float yGravity) {
        Application.checkEventThread();
        checkNotClosed();
        _setBounds(this.ptr, x2, y2, xSet, ySet, w2, h2, cw, ch, xGravity, yGravity);
    }

    public void setPosition(int x2, int y2) {
        Application.checkEventThread();
        setBounds(x2, y2, true, true, 0, 0, 0, 0, 0.0f, 0.0f);
    }

    public void setSize(int w2, int h2) {
        Application.checkEventThread();
        setBounds(0, 0, false, false, w2, h2, 0, 0, 0.0f, 0.0f);
    }

    public void setContentSize(int cw, int ch) {
        Application.checkEventThread();
        setBounds(0, 0, false, false, 0, 0, cw, ch, 0.0f, 0.0f);
    }

    public boolean isVisible() {
        Application.checkEventThread();
        return this.isVisible;
    }

    private void synthesizeViewMoveEvent() {
        View view = getView();
        if (view != null) {
            view.notifyView(423);
        }
    }

    public void setVisible(boolean visible) {
        Application.checkEventThread();
        if (this.isVisible != visible) {
            if (!visible) {
                if (getView() != null) {
                    getView().setVisible(visible);
                }
                if (this.ptr != 0) {
                    this.isVisible = _setVisible(this.ptr, visible);
                } else {
                    this.isVisible = visible;
                }
                remove(this);
                if (this.parent != 0) {
                    this.embeddedLocationTimer.stop();
                    return;
                }
                return;
            }
            checkNotClosed();
            this.isVisible = _setVisible(this.ptr, visible);
            if (getView() != null) {
                getView().setVisible(this.isVisible);
            }
            add(this);
            if (this.parent != 0) {
                Runnable checkRunnable = () -> {
                    checkScreenLocation();
                };
                Runnable timerRunnable = () -> {
                    Application.invokeLater(checkRunnable);
                };
                this.embeddedLocationTimer = Application.GetApplication().createTimer(timerRunnable);
                this.embeddedLocationTimer.start(16);
            }
            synthesizeViewMoveEvent();
        }
    }

    public boolean setResizable(boolean resizable) {
        Application.checkEventThread();
        checkNotClosed();
        if (this.isResizable != resizable && _setResizable(this.ptr, resizable)) {
            this.isResizable = resizable;
            synthesizeViewMoveEvent();
        }
        return this.isResizable;
    }

    public boolean isResizable() {
        Application.checkEventThread();
        return this.isResizable;
    }

    public boolean isUnifiedWindow() {
        return (this.styleMask & 256) != 0;
    }

    public boolean isTransparentWindow() {
        return (this.styleMask & 2) != 0;
    }

    public boolean isFocused() {
        Application.checkEventThread();
        return this.isFocused;
    }

    public boolean requestFocus(int event) {
        Application.checkEventThread();
        checkNotClosed();
        if (!isChild() && event != 542) {
            throw new IllegalArgumentException("Invalid focus event ID for top-level window");
        }
        if (isChild() && (event < 541 || event > 544)) {
            throw new IllegalArgumentException("Invalid focus event ID for child window");
        }
        if (event == 541 && !isFocused()) {
            return true;
        }
        if (!this.isFocusable) {
            return false;
        }
        return _requestFocus(this.ptr, event);
    }

    public boolean requestFocus() {
        Application.checkEventThread();
        return requestFocus(WindowEvent.FOCUS_GAINED);
    }

    public void setFocusable(boolean isFocusable) {
        Application.checkEventThread();
        checkNotClosed();
        this.isFocusable = isFocusable;
        if (isEnabled()) {
            _setFocusable(this.ptr, isFocusable);
        }
    }

    public boolean grabFocus() {
        Application.checkEventThread();
        checkNotClosed();
        if (!isFocused()) {
            throw new IllegalStateException("The window must be focused when calling grabFocus()");
        }
        return _grabFocus(this.ptr);
    }

    public void ungrabFocus() {
        Application.checkEventThread();
        checkNotClosed();
        _ungrabFocus(this.ptr);
    }

    public String getTitle() {
        Application.checkEventThread();
        return this.title;
    }

    public void setTitle(String title) {
        Application.checkEventThread();
        checkNotClosed();
        if (title == null) {
            title = "";
        }
        if (!title.equals(this.title) && _setTitle(this.ptr, title)) {
            this.title = title;
        }
    }

    public void setLevel(int level) {
        Application.checkEventThread();
        checkNotClosed();
        if (level < 1 || level > 3) {
            throw new IllegalArgumentException("Level should be in the range [1..3]");
        }
        if (this.level != level) {
            _setLevel(this.ptr, level);
            this.level = level;
        }
    }

    public int getLevel() {
        Application.checkEventThread();
        return this.level;
    }

    private boolean isInFullscreen() {
        View view = getView();
        if (view == null) {
            return false;
        }
        return view.isInFullscreen();
    }

    void notifyFullscreen(boolean entered) {
        float alpha = getAlpha();
        if (alpha < 1.0f) {
            if (entered) {
                _setAlpha(this.ptr, 1.0f);
            } else {
                setAlpha(alpha);
            }
        }
    }

    public void setAlpha(float alpha) {
        Application.checkEventThread();
        checkNotClosed();
        if (alpha < 0.0f || alpha > 1.0f) {
            throw new IllegalArgumentException("Alpha should be in the range [0f..1f]");
        }
        this.alpha = alpha;
        if (alpha < 1.0f && isInFullscreen()) {
            return;
        }
        _setAlpha(this.ptr, this.alpha);
    }

    public float getAlpha() {
        Application.checkEventThread();
        return this.alpha;
    }

    public boolean setBackground(float r2, float g2, float b2) {
        Application.checkEventThread();
        checkNotClosed();
        return _setBackground(this.ptr, r2, g2, b2);
    }

    public boolean isEnabled() {
        Application.checkEventThread();
        return this.disableCount == 0;
    }

    public void setEnabled(boolean enabled) {
        Application.checkEventThread();
        checkNotClosed();
        if (!enabled) {
            int i2 = this.disableCount + 1;
            this.disableCount = i2;
            if (i2 > 1) {
                return;
            }
        } else {
            if (this.disableCount == 0) {
                return;
            }
            int i3 = this.disableCount - 1;
            this.disableCount = i3;
            if (i3 > 0) {
                return;
            }
        }
        _setEnabled(this.ptr, isEnabled());
    }

    public int getMinimumWidth() {
        Application.checkEventThread();
        return this.minimumWidth;
    }

    public int getMinimumHeight() {
        Application.checkEventThread();
        return this.minimumHeight;
    }

    public int getMaximumWidth() {
        Application.checkEventThread();
        return this.maximumWidth;
    }

    public int getMaximumHeight() {
        Application.checkEventThread();
        return this.maximumHeight;
    }

    public void setMinimumSize(int width, int height) {
        Application.checkEventThread();
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("The width and height must be >= 0. Got: width=" + width + "; height=" + height);
        }
        checkNotClosed();
        if (_setMinimumSize(this.ptr, width, height)) {
            this.minimumWidth = width;
            this.minimumHeight = height;
        }
    }

    public void setMaximumSize(int width, int height) {
        Application.checkEventThread();
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("The width and height must be >= 0. Got: width=" + width + "; height=" + height);
        }
        checkNotClosed();
        if (_setMaximumSize(this.ptr, width == Integer.MAX_VALUE ? -1 : width, height == Integer.MAX_VALUE ? -1 : height)) {
            this.maximumWidth = width;
            this.maximumHeight = height;
        }
    }

    public void setIcon(Pixels pixels) {
        Application.checkEventThread();
        checkNotClosed();
        _setIcon(this.ptr, pixels);
    }

    public void setCursor(Cursor cursor) {
        Application.checkEventThread();
        _setCursor(this.ptr, cursor);
    }

    public void toFront() {
        Application.checkEventThread();
        checkNotClosed();
        _toFront(this.ptr);
    }

    public void toBack() {
        Application.checkEventThread();
        checkNotClosed();
        _toBack(this.ptr);
    }

    public void enterModal() {
        checkNotClosed();
        if (!this.isModal) {
            this.isModal = true;
            _enterModal(this.ptr);
        }
    }

    public void enterModal(Window window) {
        checkNotClosed();
        if (!this.isModal) {
            this.isModal = true;
            _enterModalWithWindow(this.ptr, window.getNativeHandle());
        }
    }

    public void exitModal() {
        checkNotClosed();
        if (this.isModal) {
            _exitModal(this.ptr);
            this.isModal = false;
        }
    }

    public boolean isModal() {
        return this.isModal;
    }

    public void dispatchNpapiEvent(Map eventInfo) {
        Application.checkEventThread();
        throw new RuntimeException("This operation is not supported on this platform");
    }

    public EventHandler getEventHandler() {
        Application.checkEventThread();
        return this.eventHandler;
    }

    public void setEventHandler(EventHandler eventHandler) {
        Application.checkEventThread();
        this.eventHandler = eventHandler;
    }

    public void setShouldStartUndecoratedMove(boolean v2) {
        Application.checkEventThread();
        this.shouldStartUndecoratedMove = v2;
    }

    protected void notifyClose() {
        handleWindowEvent(System.nanoTime(), 521);
    }

    protected void notifyDestroy() {
        if (this.ptr == 0) {
            return;
        }
        handleWindowEvent(System.nanoTime(), 522);
        this.ptr = 0L;
        setVisible(false);
    }

    protected void notifyMove(int x2, int y2) {
        this.f11831x = x2;
        this.f11832y = y2;
        handleWindowEvent(System.nanoTime(), 512);
    }

    protected void notifyMoveToAnotherScreen(Screen newScreen) {
        setScreen(newScreen);
    }

    protected void setState(int state) {
        this.state = state;
    }

    protected void notifyResize(int type, int width, int height) {
        if (type == 531) {
            this.state = 2;
        } else {
            if (type == 532) {
                this.state = 3;
            } else {
                this.state = 1;
            }
            this.width = width;
            this.height = height;
            if (this.helper != null) {
                this.helper.updateRectangles();
            }
        }
        handleWindowEvent(System.nanoTime(), type);
        if (type == 532 || type == 533) {
            handleWindowEvent(System.nanoTime(), WindowEvent.RESIZE);
        }
    }

    protected void notifyFocus(int event) {
        boolean focused = event != 541;
        if (this.isFocused != focused) {
            this.isFocused = focused;
            handleWindowEvent(System.nanoTime(), event);
        }
    }

    protected void notifyFocusDisabled() {
        handleWindowEvent(System.nanoTime(), WindowEvent.FOCUS_DISABLED);
    }

    protected void notifyFocusUngrab() {
        handleWindowEvent(System.nanoTime(), WindowEvent.FOCUS_UNGRAB);
    }

    protected void notifyDelegatePtr(long ptr) {
        this.delegatePtr = ptr;
    }

    protected void handleWindowEvent(long time, int type) {
        if (this.eventHandler != null) {
            this.eventHandler.handleWindowEvent(this, time, type);
        }
    }

    public void setUndecoratedMoveRectangle(int size) {
        Application.checkEventThread();
        if (this.isDecorated) {
            System.err.println("Glass Window.setUndecoratedMoveRectangle is only valid for Undecorated Window. In the future this will be hard error.");
            Thread.dumpStack();
        } else if (this.helper != null) {
            this.helper.setMoveRectangle(size);
        }
    }

    public boolean shouldStartUndecoratedMove(int x2, int y2) {
        Application.checkEventThread();
        if (this.shouldStartUndecoratedMove) {
            return true;
        }
        if (!this.isDecorated && this.helper != null) {
            return this.helper.shouldStartMove(x2, y2);
        }
        return false;
    }

    public void setUndecoratedResizeRectangle(int size) {
        Application.checkEventThread();
        if (this.isDecorated || !this.isResizable) {
            System.err.println("Glass Window.setUndecoratedResizeRectangle is only valid for Undecorated Resizable Window. In the future this will be hard error.");
            Thread.dumpStack();
        } else if (this.helper != null) {
            this.helper.setResizeRectangle(size);
        }
    }

    public boolean shouldStartUndecoratedResize(int x2, int y2) {
        Application.checkEventThread();
        if (!this.isDecorated && this.isResizable && this.helper != null) {
            return this.helper.shouldStartResize(x2, y2);
        }
        return false;
    }

    boolean handleMouseEvent(int type, int button, int x2, int y2, int xAbs, int yAbs) {
        if (!this.isDecorated) {
            return this.helper.handleMouseEvent(type, button, x2, y2, xAbs, yAbs);
        }
        return false;
    }

    public String toString() {
        Application.checkEventThread();
        return "Window:\n    ptr: " + getNativeWindow() + "\n    screen ptr: " + (this.screen != null ? Long.valueOf(this.screen.getNativeScreen()) : FXMLLoader.NULL_KEYWORD) + "\n    isDecorated: " + isDecorated() + "\n    title: " + getTitle() + "\n    visible: " + isVisible() + "\n    focused: " + isFocused() + "\n    modal: " + isModal() + "\n    state: " + this.state + "\n    x: " + getX() + ", y: " + getY() + ", w: " + getWidth() + ", h: " + getHeight() + "\n";
    }

    /* loaded from: jfxrt.jar:com/sun/glass/ui/Window$TrackingRectangle.class */
    private static class TrackingRectangle {
        int size;

        /* renamed from: x, reason: collision with root package name */
        int f11833x;

        /* renamed from: y, reason: collision with root package name */
        int f11834y;
        int width;
        int height;

        private TrackingRectangle() {
            this.size = 0;
            this.f11833x = 0;
            this.f11834y = 0;
            this.width = 0;
            this.height = 0;
        }

        boolean contains(int x2, int y2) {
            return this.size > 0 && x2 >= this.f11833x && x2 < this.f11833x + this.width && y2 >= this.f11834y && y2 < this.f11834y + this.height;
        }
    }

    protected void notifyLevelChanged(int level) {
        this.level = level;
        if (this.eventHandler != null) {
            this.eventHandler.handleLevelEvent(level);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/glass/ui/Window$UndecoratedMoveResizeHelper.class */
    private class UndecoratedMoveResizeHelper {
        TrackingRectangle moveRect;
        TrackingRectangle resizeRect;
        boolean inMove = false;
        boolean inResize = false;
        int startMouseX;
        int startMouseY;
        int startX;
        int startY;
        int startWidth;
        int startHeight;

        UndecoratedMoveResizeHelper() {
            this.moveRect = null;
            this.resizeRect = null;
            this.moveRect = new TrackingRectangle();
            this.resizeRect = new TrackingRectangle();
        }

        void setMoveRectangle(int size) {
            this.moveRect.size = size;
            this.moveRect.f11833x = 0;
            this.moveRect.f11834y = 0;
            this.moveRect.width = Window.this.getWidth();
            this.moveRect.height = this.moveRect.size;
        }

        boolean shouldStartMove(int x2, int y2) {
            return this.moveRect.contains(x2, y2);
        }

        boolean inMove() {
            return this.inMove;
        }

        void startMove(int x2, int y2) {
            this.inMove = true;
            this.startMouseX = x2;
            this.startMouseY = y2;
            this.startX = Window.this.getX();
            this.startY = Window.this.getY();
        }

        void deltaMove(int x2, int y2) {
            int deltaX = x2 - this.startMouseX;
            int deltaY = y2 - this.startMouseY;
            Window.this.setPosition(this.startX + deltaX, this.startY + deltaY);
        }

        void stopMove() {
            this.inMove = false;
        }

        void setResizeRectangle(int size) {
            this.resizeRect.size = size;
            this.resizeRect.f11833x = Window.this.getWidth() - this.resizeRect.size;
            this.resizeRect.f11834y = Window.this.getHeight() - this.resizeRect.size;
            this.resizeRect.width = this.resizeRect.size;
            this.resizeRect.height = this.resizeRect.size;
        }

        boolean shouldStartResize(int x2, int y2) {
            return this.resizeRect.contains(x2, y2);
        }

        boolean inResize() {
            return this.inResize;
        }

        void startResize(int x2, int y2) {
            this.inResize = true;
            this.startMouseX = x2;
            this.startMouseY = y2;
            this.startWidth = Window.this.getWidth();
            this.startHeight = Window.this.getHeight();
        }

        void deltaResize(int x2, int y2) {
            int deltaX = x2 - this.startMouseX;
            int deltaY = y2 - this.startMouseY;
            Window.this.setSize(this.startWidth + deltaX, this.startHeight + deltaY);
        }

        protected void stopResize() {
            this.inResize = false;
        }

        void updateRectangles() {
            if (this.moveRect.size > 0) {
                setMoveRectangle(this.moveRect.size);
            }
            if (this.resizeRect.size > 0) {
                setResizeRectangle(this.resizeRect.size);
            }
        }

        boolean handleMouseEvent(int type, int button, int x2, int y2, int xAbs, int yAbs) {
            switch (type) {
                case 221:
                    if (button == 212) {
                        if (Window.this.shouldStartUndecoratedMove(x2, y2)) {
                            startMove(xAbs, yAbs);
                            break;
                        } else if (Window.this.shouldStartUndecoratedResize(x2, y2)) {
                            startResize(xAbs, yAbs);
                            break;
                        }
                    }
                    break;
                case 222:
                    boolean wasProcessed = inMove() || inResize();
                    stopResize();
                    stopMove();
                    break;
                case 223:
                case 224:
                    if (inMove()) {
                        deltaMove(xAbs, yAbs);
                        break;
                    } else if (inResize()) {
                        deltaResize(xAbs, yAbs);
                        break;
                    }
                    break;
            }
            return true;
        }
    }

    public void requestInput(String text, int type, double width, double height, double Mxx, double Mxy, double Mxz, double Mxt, double Myx, double Myy, double Myz, double Myt, double Mzx, double Mzy, double Mzz, double Mzt) {
        Application.checkEventThread();
        _requestInput(this.ptr, text, type, width, height, Mxx, Mxy, Mxz, Mxt, Myx, Myy, Myz, Myt, Mzx, Mzy, Mzz, Mzt);
    }

    public void releaseInput() {
        Application.checkEventThread();
        _releaseInput(this.ptr);
    }
}
