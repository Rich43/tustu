package com.sun.glass.ui;

import com.sun.glass.events.ViewEvent;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.util.Map;

/* loaded from: jfxrt.jar:com/sun/glass/ui/View.class */
public abstract class View {
    public static final int GESTURE_NO_VALUE = Integer.MAX_VALUE;
    public static final double GESTURE_NO_DOUBLE_VALUE = Double.NaN;
    public static final byte IME_ATTR_INPUT = 0;
    public static final byte IME_ATTR_TARGET_CONVERTED = 1;
    public static final byte IME_ATTR_CONVERTED = 2;
    public static final byte IME_ATTR_TARGET_NOTCONVERTED = 3;
    public static final byte IME_ATTR_INPUT_ERROR = 4;
    private volatile long ptr;
    private Window window;
    private EventHandler eventHandler;
    private int width = -1;
    private int height = -1;
    private boolean isValid = false;
    private boolean isVisible = false;
    private boolean inFullscreen = false;
    private static int lastClickedButton;
    private static long lastClickedTime;
    private static int lastClickedX;
    private static int lastClickedY;
    private static int clickCount;
    private ClipboardAssistance dropSourceAssistant;
    ClipboardAssistance dropTargetAssistant;
    static final boolean accessible = ((Boolean) AccessController.doPrivileged(() -> {
        String force = System.getProperty("glass.accessible.force");
        if (force != null) {
            return Boolean.valueOf(Boolean.parseBoolean(force));
        }
        try {
            String platform = Platform.determinePlatform();
            String major = System.getProperty("os.version").replaceFirst("(\\d+)\\.\\d+.*", "$1");
            String minor = System.getProperty("os.version").replaceFirst("\\d+\\.(\\d+).*", "$1");
            int v2 = (Integer.parseInt(major) * 100) + Integer.parseInt(minor);
            return Boolean.valueOf((platform.equals(Platform.MAC) && v2 >= 1009) || (platform.equals(Platform.WINDOWS) && v2 >= 601));
        } catch (Exception e2) {
            return false;
        }
    })).booleanValue();
    private static WeakReference<View> lastClickedView = null;
    private static boolean dragProcessed = false;

    /* loaded from: jfxrt.jar:com/sun/glass/ui/View$Capability.class */
    public static final class Capability {
        public static final int k3dKeyValue = 0;
        public static final int kSyncKeyValue = 1;
        public static final int k3dProjectionKeyValue = 2;
        public static final int k3dProjectionAngleKeyValue = 3;
        public static final int k3dDepthKeyValue = 4;
        public static final int kHiDPIAwareKeyValue = 5;
        public static final Object k3dKey = 0;
        public static final Object kSyncKey = 1;
        public static final Object k3dProjectionKey = 2;
        public static final Object k3dProjectionAngleKey = 3;
        public static final Object k3dDepthKey = 4;
        public static final Object kHiDPIAwareKey = 5;
    }

    protected abstract void _enableInputMethodEvents(long j2, boolean z2);

    protected abstract long _create(Map map);

    protected abstract long _getNativeView(long j2);

    protected abstract int _getX(long j2);

    protected abstract int _getY(long j2);

    protected abstract void _setParent(long j2, long j3);

    protected abstract boolean _close(long j2);

    protected abstract void _scheduleRepaint(long j2);

    protected abstract void _begin(long j2);

    protected abstract void _end(long j2);

    protected abstract int _getNativeFrameBuffer(long j2);

    protected abstract void _uploadPixels(long j2, Pixels pixels);

    protected abstract boolean _enterFullscreen(long j2, boolean z2, boolean z3, boolean z4);

    protected abstract void _exitFullscreen(long j2, boolean z2);

    /* loaded from: jfxrt.jar:com/sun/glass/ui/View$EventHandler.class */
    public static class EventHandler {
        public void handleViewEvent(View view, long time, int type) {
        }

        public void handleKeyEvent(View view, long time, int action, int keyCode, char[] keyChars, int modifiers) {
        }

        public void handleMenuEvent(View view, int x2, int y2, int xAbs, int yAbs, boolean isKeyboardTrigger) {
        }

        public void handleMouseEvent(View view, long time, int type, int button, int x2, int y2, int xAbs, int yAbs, int modifiers, boolean isPopupTrigger, boolean isSynthesized) {
        }

        public void handleScrollEvent(View view, long time, int x2, int y2, int xAbs, int yAbs, double deltaX, double deltaY, int modifiers, int lines, int chars, int defaultLines, int defaultChars, double xMultiplier, double yMultiplier) {
        }

        public void handleInputMethodEvent(long time, String text, int[] clauseBoundary, int[] attrBoundary, byte[] attrValue, int commitCount, int cursorPos) {
        }

        public double[] getInputMethodCandidatePos(int offset) {
            return null;
        }

        public void handleDragStart(View view, int button, int x2, int y2, int xAbs, int yAbs, ClipboardAssistance dropSourceAssistant) {
        }

        public void handleDragEnd(View view, int performedAction) {
        }

        public int handleDragEnter(View view, int x2, int y2, int xAbs, int yAbs, int recommendedDropAction, ClipboardAssistance dropTargetAssistant) {
            return recommendedDropAction;
        }

        public int handleDragOver(View view, int x2, int y2, int xAbs, int yAbs, int recommendedDropAction, ClipboardAssistance dropTargetAssistant) {
            return recommendedDropAction;
        }

        public void handleDragLeave(View view, ClipboardAssistance dropTargetAssistant) {
        }

        public int handleDragDrop(View view, int x2, int y2, int xAbs, int yAbs, int recommendedDropAction, ClipboardAssistance dropTargetAssistant) {
            return 0;
        }

        public void handleBeginTouchEvent(View view, long time, int modifiers, boolean isDirect, int touchEventCount) {
        }

        public void handleNextTouchEvent(View view, long time, int type, long touchId, int x2, int y2, int xAbs, int yAbs) {
        }

        public void handleEndTouchEvent(View view, long time) {
        }

        public void handleScrollGestureEvent(View view, long time, int type, int modifiers, boolean isDirect, boolean isInertia, int touchCount, int x2, int y2, int xAbs, int yAbs, double dx, double dy, double totaldx, double totaldy, double multiplierX, double multiplierY) {
        }

        public void handleZoomGestureEvent(View view, long time, int type, int modifiers, boolean isDirect, boolean isInertia, int x2, int y2, int xAbs, int yAbs, double scale, double expansion, double totalscale, double totalexpansion) {
        }

        public void handleRotateGestureEvent(View view, long time, int type, int modifiers, boolean isDirect, boolean isInertia, int x2, int y2, int xAbs, int yAbs, double dangle, double totalangle) {
        }

        public void handleSwipeGestureEvent(View view, long time, int type, int modifiers, boolean isDirect, boolean isInertia, int touchCount, int dir, int x2, int y2, int xAbs, int yAbs) {
        }

        public Accessible getSceneAccessible() {
            return null;
        }
    }

    public static long getMultiClickTime() {
        Application.checkEventThread();
        return Application.GetApplication().staticView_getMultiClickTime();
    }

    public static int getMultiClickMaxX() {
        Application.checkEventThread();
        return Application.GetApplication().staticView_getMultiClickMaxX();
    }

    public static int getMultiClickMaxY() {
        Application.checkEventThread();
        return Application.GetApplication().staticView_getMultiClickMaxY();
    }

    protected void _finishInputMethodComposition(long ptr) {
    }

    protected View() {
        Application.checkEventThread();
        Application.GetApplication();
        this.ptr = _create(Application.getDeviceDetails());
        if (this.ptr == 0) {
            throw new RuntimeException("could not create platform view");
        }
    }

    private void checkNotClosed() {
        if (this.ptr == 0) {
            throw new IllegalStateException("The view has already been closed");
        }
    }

    public boolean isClosed() {
        Application.checkEventThread();
        return this.ptr == 0;
    }

    public long getNativeView() {
        Application.checkEventThread();
        checkNotClosed();
        return _getNativeView(this.ptr);
    }

    public int getNativeRemoteLayerId(String serverName) {
        Application.checkEventThread();
        throw new RuntimeException("This operation is not supported on this platform");
    }

    public Window getWindow() {
        Application.checkEventThread();
        return this.window;
    }

    public int getX() {
        Application.checkEventThread();
        checkNotClosed();
        return _getX(this.ptr);
    }

    public int getY() {
        Application.checkEventThread();
        checkNotClosed();
        return _getY(this.ptr);
    }

    public int getWidth() {
        Application.checkEventThread();
        return this.width;
    }

    public int getHeight() {
        Application.checkEventThread();
        return this.height;
    }

    void setWindow(Window window) {
        Application.checkEventThread();
        checkNotClosed();
        this.window = window;
        _setParent(this.ptr, window == null ? 0L : window.getNativeHandle());
        this.isValid = (this.ptr == 0 || window == null) ? false : true;
    }

    void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    public void close() {
        Application.checkEventThread();
        if (this.ptr == 0) {
            return;
        }
        if (isInFullscreen()) {
            _exitFullscreen(this.ptr, false);
        }
        Window host = getWindow();
        if (host != null) {
            host.setView(null);
        }
        this.isValid = false;
        _close(this.ptr);
        this.ptr = 0L;
    }

    public EventHandler getEventHandler() {
        Application.checkEventThread();
        return this.eventHandler;
    }

    public void setEventHandler(EventHandler eventHandler) {
        Application.checkEventThread();
        this.eventHandler = eventHandler;
    }

    private void handleViewEvent(long time, int type) {
        if (this.eventHandler != null) {
            this.eventHandler.handleViewEvent(this, time, type);
        }
    }

    private void handleKeyEvent(long time, int action, int keyCode, char[] keyChars, int modifiers) {
        if (this.eventHandler != null) {
            this.eventHandler.handleKeyEvent(this, time, action, keyCode, keyChars, modifiers);
        }
    }

    private void handleMouseEvent(long time, int type, int button, int x2, int y2, int xAbs, int yAbs, int modifiers, boolean isPopupTrigger, boolean isSynthesized) {
        if (this.eventHandler != null) {
            this.eventHandler.handleMouseEvent(this, time, type, button, x2, y2, xAbs, yAbs, modifiers, isPopupTrigger, isSynthesized);
        }
    }

    private void handleMenuEvent(int x2, int y2, int xAbs, int yAbs, boolean isKeyboardTrigger) {
        if (this.eventHandler != null) {
            this.eventHandler.handleMenuEvent(this, x2, y2, xAbs, yAbs, isKeyboardTrigger);
        }
    }

    public void handleBeginTouchEvent(View view, long time, int modifiers, boolean isDirect, int touchEventCount) {
        if (this.eventHandler != null) {
            this.eventHandler.handleBeginTouchEvent(view, time, modifiers, isDirect, touchEventCount);
        }
    }

    public void handleNextTouchEvent(View view, long time, int type, long touchId, int x2, int y2, int xAbs, int yAbs) {
        if (this.eventHandler != null) {
            this.eventHandler.handleNextTouchEvent(view, time, type, touchId, x2, y2, xAbs, yAbs);
        }
    }

    public void handleEndTouchEvent(View view, long time) {
        if (this.eventHandler != null) {
            this.eventHandler.handleEndTouchEvent(view, time);
        }
    }

    public void handleScrollGestureEvent(View view, long time, int type, int modifiers, boolean isDirect, boolean isInertia, int touchCount, int x2, int y2, int xAbs, int yAbs, double dx, double dy, double totaldx, double totaldy, double multiplierX, double multiplierY) {
        if (this.eventHandler != null) {
            this.eventHandler.handleScrollGestureEvent(view, time, type, modifiers, isDirect, isInertia, touchCount, x2, y2, xAbs, yAbs, dx, dy, totaldx, totaldy, multiplierX, multiplierY);
        }
    }

    public void handleZoomGestureEvent(View view, long time, int type, int modifiers, boolean isDirect, boolean isInertia, int originx, int originy, int originxAbs, int originyAbs, double scale, double expansion, double totalscale, double totalexpansion) {
        if (this.eventHandler != null) {
            this.eventHandler.handleZoomGestureEvent(view, time, type, modifiers, isDirect, isInertia, originx, originy, originxAbs, originyAbs, scale, expansion, totalscale, totalexpansion);
        }
    }

    public void handleRotateGestureEvent(View view, long time, int type, int modifiers, boolean isDirect, boolean isInertia, int originx, int originy, int originxAbs, int originyAbs, double dangle, double totalangle) {
        if (this.eventHandler != null) {
            this.eventHandler.handleRotateGestureEvent(view, time, type, modifiers, isDirect, isInertia, originx, originy, originxAbs, originyAbs, dangle, totalangle);
        }
    }

    public void handleSwipeGestureEvent(View view, long time, int type, int modifiers, boolean isDirect, boolean isInertia, int touchCount, int dir, int originx, int originy, int originxAbs, int originyAbs) {
        if (this.eventHandler != null) {
            this.eventHandler.handleSwipeGestureEvent(view, time, type, modifiers, isDirect, isInertia, touchCount, dir, originx, originy, originxAbs, originyAbs);
        }
    }

    private void handleInputMethodEvent(long time, String text, int[] clauseBoundary, int[] attrBoundary, byte[] attrValue, int commitCount, int cursorPos) {
        if (this.eventHandler != null) {
            this.eventHandler.handleInputMethodEvent(time, text, clauseBoundary, attrBoundary, attrValue, commitCount, cursorPos);
        }
    }

    public void enableInputMethodEvents(boolean enable) {
        Application.checkEventThread();
        checkNotClosed();
        _enableInputMethodEvents(this.ptr, enable);
    }

    public void finishInputMethodComposition() {
        Application.checkEventThread();
        checkNotClosed();
        _finishInputMethodComposition(this.ptr);
    }

    private double[] getInputMethodCandidatePos(int offset) {
        if (this.eventHandler != null) {
            return this.eventHandler.getInputMethodCandidatePos(offset);
        }
        return null;
    }

    private void handleDragStart(int button, int x2, int y2, int xAbs, int yAbs, ClipboardAssistance dropSourceAssistant) {
        if (this.eventHandler != null) {
            this.eventHandler.handleDragStart(this, button, x2, y2, xAbs, yAbs, dropSourceAssistant);
        }
    }

    private void handleDragEnd(int performedAction) {
        if (this.eventHandler != null) {
            this.eventHandler.handleDragEnd(this, performedAction);
        }
    }

    private int handleDragEnter(int x2, int y2, int xAbs, int yAbs, int recommendedDropAction, ClipboardAssistance dropTargetAssistant) {
        if (this.eventHandler != null) {
            return this.eventHandler.handleDragEnter(this, x2, y2, xAbs, yAbs, recommendedDropAction, dropTargetAssistant);
        }
        return recommendedDropAction;
    }

    private int handleDragOver(int x2, int y2, int xAbs, int yAbs, int recommendedDropAction, ClipboardAssistance dropTargetAssistant) {
        if (this.eventHandler != null) {
            return this.eventHandler.handleDragOver(this, x2, y2, xAbs, yAbs, recommendedDropAction, dropTargetAssistant);
        }
        return recommendedDropAction;
    }

    private void handleDragLeave(ClipboardAssistance dropTargetAssistant) {
        if (this.eventHandler != null) {
            this.eventHandler.handleDragLeave(this, dropTargetAssistant);
        }
    }

    private int handleDragDrop(int x2, int y2, int xAbs, int yAbs, int recommendedDropAction, ClipboardAssistance dropTargetAssistant) {
        if (this.eventHandler != null) {
            return this.eventHandler.handleDragDrop(this, x2, y2, xAbs, yAbs, recommendedDropAction, dropTargetAssistant);
        }
        return 0;
    }

    public void scheduleRepaint() {
        Application.checkEventThread();
        checkNotClosed();
        _scheduleRepaint(this.ptr);
    }

    public void lock() {
        checkNotClosed();
        _begin(this.ptr);
    }

    public void unlock() {
        checkNotClosed();
        _end(this.ptr);
    }

    public int getNativeFrameBuffer() {
        return _getNativeFrameBuffer(this.ptr);
    }

    public void uploadPixels(Pixels pixels) {
        Application.checkEventThread();
        checkNotClosed();
        lock();
        try {
            _uploadPixels(this.ptr, pixels);
        } finally {
            unlock();
        }
    }

    public boolean enterFullscreen(boolean animate, boolean keepRatio, boolean hideCursor) {
        Application.checkEventThread();
        checkNotClosed();
        return _enterFullscreen(this.ptr, animate, keepRatio, hideCursor);
    }

    public void exitFullscreen(boolean animate) {
        Application.checkEventThread();
        checkNotClosed();
        _exitFullscreen(this.ptr, animate);
    }

    public boolean isInFullscreen() {
        Application.checkEventThread();
        return this.inFullscreen;
    }

    public boolean toggleFullscreen(boolean animate, boolean keepRatio, boolean hideCursor) {
        Application.checkEventThread();
        checkNotClosed();
        if (!this.inFullscreen) {
            enterFullscreen(animate, keepRatio, hideCursor);
        } else {
            exitFullscreen(animate);
        }
        _scheduleRepaint(this.ptr);
        return this.inFullscreen;
    }

    protected void notifyView(int type) {
        if (type == 421) {
            if (this.isValid) {
                handleViewEvent(System.nanoTime(), type);
                return;
            }
            return;
        }
        boolean synthesizeMOVE = false;
        switch (type) {
            case 411:
                this.isValid = true;
                synthesizeMOVE = true;
                break;
            case 412:
                this.isValid = false;
                synthesizeMOVE = true;
                break;
            case 422:
            case 423:
                break;
            case 431:
                this.inFullscreen = true;
                synthesizeMOVE = true;
                if (getWindow() != null) {
                    getWindow().notifyFullscreen(true);
                    break;
                }
                break;
            case ViewEvent.FULLSCREEN_EXIT /* 432 */:
                this.inFullscreen = false;
                synthesizeMOVE = true;
                if (getWindow() != null) {
                    getWindow().notifyFullscreen(false);
                    break;
                }
                break;
            default:
                System.err.println("Unknown view event type: " + type);
                return;
        }
        handleViewEvent(System.nanoTime(), type);
        if (synthesizeMOVE) {
            handleViewEvent(System.nanoTime(), 423);
        }
    }

    protected void notifyResize(int width, int height) {
        if (this.width == width && this.height == height) {
            return;
        }
        this.width = width;
        this.height = height;
        handleViewEvent(System.nanoTime(), 422);
    }

    protected void notifyRepaint(int x2, int y2, int width, int height) {
        notifyView(421);
    }

    protected void notifyMenu(int x2, int y2, int xAbs, int yAbs, boolean isKeyboardTrigger) {
        handleMenuEvent(x2, y2, xAbs, yAbs, isKeyboardTrigger);
    }

    protected void notifyMouse(int type, int button, int x2, int y2, int xAbs, int yAbs, int modifiers, boolean isPopupTrigger, boolean isSynthesized) {
        if (this.window != null && this.window.handleMouseEvent(type, button, x2, y2, xAbs, yAbs)) {
            return;
        }
        long now = System.nanoTime();
        if (type == 221) {
            View lastClickedView2 = lastClickedView == null ? null : lastClickedView.get();
            if (lastClickedView2 == this && lastClickedButton == button && now - lastClickedTime <= 1000000 * getMultiClickTime() && Math.abs(x2 - lastClickedX) <= getMultiClickMaxX() && Math.abs(y2 - lastClickedY) <= getMultiClickMaxY()) {
                clickCount++;
            } else {
                clickCount = 1;
                lastClickedView = new WeakReference<>(this);
                lastClickedButton = button;
                lastClickedX = x2;
                lastClickedY = y2;
            }
            lastClickedTime = now;
        }
        handleMouseEvent(now, type, button, x2, y2, xAbs, yAbs, modifiers, isPopupTrigger, isSynthesized);
        if (type == 223) {
            if (!dragProcessed) {
                notifyDragStart(button, x2, y2, xAbs, yAbs);
                dragProcessed = true;
                return;
            }
            return;
        }
        dragProcessed = false;
    }

    protected void notifyScroll(int x2, int y2, int xAbs, int yAbs, double deltaX, double deltaY, int modifiers, int lines, int chars, int defaultLines, int defaultChars, double xMultiplier, double yMultiplier) {
        if (this.eventHandler != null) {
            this.eventHandler.handleScrollEvent(this, System.nanoTime(), x2, y2, xAbs, yAbs, deltaX, deltaY, modifiers, lines, chars, defaultLines, defaultChars, xMultiplier, yMultiplier);
        }
    }

    protected void notifyKey(int type, int keyCode, char[] keyChars, int modifiers) {
        handleKeyEvent(System.nanoTime(), type, keyCode, keyChars, modifiers);
    }

    protected void notifyInputMethod(String text, int[] clauseBoundary, int[] attrBoundary, byte[] attrValue, int committedTextLength, int caretPos, int visiblePos) {
        handleInputMethodEvent(System.nanoTime(), text, clauseBoundary, attrBoundary, attrValue, committedTextLength, caretPos);
    }

    protected double[] notifyInputMethodCandidatePosRequest(int offset) {
        double[] ret = getInputMethodCandidatePos(offset);
        if (ret == null) {
            ret = new double[]{0.0d, 0.0d};
        }
        return ret;
    }

    protected void notifyDragStart(int button, int x2, int y2, int xAbs, int yAbs) {
        this.dropSourceAssistant = new ClipboardAssistance(Clipboard.DND) { // from class: com.sun.glass.ui.View.1
            @Override // com.sun.glass.ui.ClipboardAssistance
            public void actionPerformed(int performedAction) {
                View.this.notifyDragEnd(performedAction);
            }
        };
        handleDragStart(button, x2, y2, xAbs, yAbs, this.dropSourceAssistant);
        if (this.dropSourceAssistant != null) {
            this.dropSourceAssistant.close();
            this.dropSourceAssistant = null;
        }
    }

    protected void notifyDragEnd(int performedAction) {
        handleDragEnd(performedAction);
        if (this.dropSourceAssistant != null) {
            this.dropSourceAssistant.close();
            this.dropSourceAssistant = null;
        }
    }

    protected int notifyDragEnter(int x2, int y2, int xAbs, int yAbs, int recommendedDropAction) {
        this.dropTargetAssistant = new ClipboardAssistance(Clipboard.DND) { // from class: com.sun.glass.ui.View.2
            @Override // com.sun.glass.ui.ClipboardAssistance
            public void flush() {
                throw new UnsupportedOperationException("Flush is forbidden from target!");
            }
        };
        return handleDragEnter(x2, y2, xAbs, yAbs, recommendedDropAction, this.dropTargetAssistant);
    }

    protected int notifyDragOver(int x2, int y2, int xAbs, int yAbs, int recommendedDropAction) {
        return handleDragOver(x2, y2, xAbs, yAbs, recommendedDropAction, this.dropTargetAssistant);
    }

    protected void notifyDragLeave() {
        handleDragLeave(this.dropTargetAssistant);
        this.dropTargetAssistant.close();
    }

    protected int notifyDragDrop(int x2, int y2, int xAbs, int yAbs, int recommendedDropAction) {
        int performedAction = handleDragDrop(x2, y2, xAbs, yAbs, recommendedDropAction, this.dropTargetAssistant);
        this.dropTargetAssistant.close();
        return performedAction;
    }

    public void notifyBeginTouchEvent(int modifiers, boolean isDirect, int touchEventCount) {
        handleBeginTouchEvent(this, System.nanoTime(), modifiers, isDirect, touchEventCount);
    }

    public void notifyNextTouchEvent(int type, long touchId, int x2, int y2, int xAbs, int yAbs) {
        handleNextTouchEvent(this, System.nanoTime(), type, touchId, x2, y2, xAbs, yAbs);
    }

    public void notifyEndTouchEvent() {
        handleEndTouchEvent(this, System.nanoTime());
    }

    public void notifyScrollGestureEvent(int type, int modifiers, boolean isDirect, boolean isInertia, int touchCount, int x2, int y2, int xAbs, int yAbs, double dx, double dy, double totaldx, double totaldy, double multiplierX, double multiplierY) {
        handleScrollGestureEvent(this, System.nanoTime(), type, modifiers, isDirect, isInertia, touchCount, x2, y2, xAbs, yAbs, dx, dy, totaldx, totaldy, multiplierX, multiplierY);
    }

    public void notifyZoomGestureEvent(int type, int modifiers, boolean isDirect, boolean isInertia, int originx, int originy, int originxAbs, int originyAbs, double scale, double expansion, double totalscale, double totalexpansion) {
        handleZoomGestureEvent(this, System.nanoTime(), type, modifiers, isDirect, isInertia, originx, originy, originxAbs, originyAbs, scale, expansion, totalscale, totalexpansion);
    }

    public void notifyRotateGestureEvent(int type, int modifiers, boolean isDirect, boolean isInertia, int originx, int originy, int originxAbs, int originyAbs, double dangle, double totalangle) {
        handleRotateGestureEvent(this, System.nanoTime(), type, modifiers, isDirect, isInertia, originx, originy, originxAbs, originyAbs, dangle, totalangle);
    }

    public void notifySwipeGestureEvent(int type, int modifiers, boolean isDirect, boolean isInertia, int touchCount, int dir, int originx, int originy, int originxAbs, int originyAbs) {
        handleSwipeGestureEvent(this, System.nanoTime(), type, modifiers, isDirect, isInertia, touchCount, dir, originx, originy, originxAbs, originyAbs);
    }

    long getAccessible() {
        Accessible acc;
        Application.checkEventThread();
        checkNotClosed();
        if (accessible && (acc = this.eventHandler.getSceneAccessible()) != null) {
            acc.setView(this);
            return acc.getNativeAccessible();
        }
        return 0L;
    }
}
