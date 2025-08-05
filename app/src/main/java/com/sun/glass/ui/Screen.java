package com.sun.glass.ui;

import java.security.AccessController;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* loaded from: jfxrt.jar:com/sun/glass/ui/Screen.class */
public final class Screen {
    private static volatile List<Screen> screens = null;
    private static final int dpiOverride = ((Integer) AccessController.doPrivileged(() -> {
        return Integer.getInteger("com.sun.javafx.screenDPI", 0);
    })).intValue();
    private static EventHandler eventHandler;
    private volatile long ptr;
    private volatile int adapter;
    private final int depth;

    /* renamed from: x, reason: collision with root package name */
    private final int f11827x;

    /* renamed from: y, reason: collision with root package name */
    private final int f11828y;
    private final int width;
    private final int height;
    private final int visibleX;
    private final int visibleY;
    private final int visibleWidth;
    private final int visibleHeight;
    private final int resolutionX;
    private final int resolutionY;
    private final float uiScale;
    private final float renderScale;

    /* loaded from: jfxrt.jar:com/sun/glass/ui/Screen$EventHandler.class */
    public static class EventHandler {
        public void handleSettingsChanged() {
        }
    }

    public static double getVideoRefreshPeriod() {
        Application.checkEventThread();
        return Application.GetApplication().staticScreen_getVideoRefreshPeriod();
    }

    public static Screen getMainScreen() {
        return getScreens().get(0);
    }

    public static List<Screen> getScreens() {
        if (screens == null) {
            throw new RuntimeException("Internal graphics not initialized yet");
        }
        return screens;
    }

    protected Screen(long nativePtr, int depth, int x2, int y2, int width, int height, int visibleX, int visibleY, int visibleWidth, int visibleHeight, int resolutionX, int resolutionY, float renderScale) {
        this(nativePtr, depth, x2, y2, width, height, visibleX, visibleY, visibleWidth, visibleHeight, resolutionX, resolutionY, 1.0f, renderScale);
    }

    protected Screen(long nativePtr, int depth, int x2, int y2, int width, int height, int visibleX, int visibleY, int visibleWidth, int visibleHeight, int resolutionX, int resolutionY, float uiScale, float renderScale) {
        this.ptr = nativePtr;
        this.depth = depth;
        this.f11827x = x2;
        this.f11828y = y2;
        this.width = width;
        this.height = height;
        this.visibleX = visibleX;
        this.visibleY = visibleY;
        this.visibleWidth = visibleWidth;
        this.visibleHeight = visibleHeight;
        if (dpiOverride > 0) {
            int i2 = dpiOverride;
            this.resolutionY = i2;
            this.resolutionX = i2;
        } else {
            this.resolutionX = resolutionX;
            this.resolutionY = resolutionY;
        }
        this.uiScale = uiScale;
        this.renderScale = renderScale;
    }

    public int getDepth() {
        return this.depth;
    }

    public int getX() {
        return this.f11827x;
    }

    public int getY() {
        return this.f11828y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getVisibleX() {
        return this.visibleX;
    }

    public int getVisibleY() {
        return this.visibleY;
    }

    public int getVisibleWidth() {
        return this.visibleWidth;
    }

    public int getVisibleHeight() {
        return this.visibleHeight;
    }

    public int getResolutionX() {
        return this.resolutionX;
    }

    public int getResolutionY() {
        return this.resolutionY;
    }

    public float getUIScale() {
        return this.uiScale;
    }

    public float getRenderScale() {
        return this.renderScale;
    }

    public long getNativeScreen() {
        return this.ptr;
    }

    private void dispose() {
        this.ptr = 0L;
    }

    public int getAdapterOrdinal() {
        return this.adapter;
    }

    public void setAdapterOrdinal(int adapter) {
        this.adapter = adapter;
    }

    public static void setEventHandler(EventHandler eh) {
        Application.checkEventThread();
        eventHandler = eh;
    }

    public static void notifySettingsChanged() {
        List<Screen> oldScreens = screens;
        initScreens();
        if (eventHandler != null) {
            eventHandler.handleSettingsChanged();
        }
        List<Window> windows = Window.getWindows();
        for (Window w2 : windows) {
            Screen oldScreen = w2.getScreen();
            Iterator<Screen> it = screens.iterator();
            while (true) {
                if (it.hasNext()) {
                    Screen newScreen = it.next();
                    if (oldScreen.getNativeScreen() == newScreen.getNativeScreen()) {
                        w2.setScreen(newScreen);
                        break;
                    }
                }
            }
        }
        if (oldScreens != null) {
            for (Screen screen : oldScreens) {
                screen.dispose();
            }
        }
    }

    static void initScreens() {
        Application.checkEventThread();
        Screen[] newScreens = Application.GetApplication().staticScreen_getScreens();
        if (newScreens == null) {
            throw new RuntimeException("Internal graphics failed to initialize");
        }
        screens = Collections.unmodifiableList(Arrays.asList(newScreens));
    }

    public String toString() {
        return "Screen:\n    ptr:" + getNativeScreen() + "\n    adapter:" + getAdapterOrdinal() + "\n    depth:" + getDepth() + "\n    x:" + getX() + "\n    y:" + getY() + "\n    width:" + getWidth() + "\n    height:" + getHeight() + "\n    visibleX:" + getVisibleX() + "\n    visibleY:" + getVisibleY() + "\n    visibleWidth:" + getVisibleWidth() + "\n    visibleHeight:" + getVisibleHeight() + "\n    uiScale:" + getUIScale() + "\n    RenderScale:" + getRenderScale() + "\n    resolutionX:" + getResolutionX() + "\n    resolutionY:" + getResolutionY() + "\n";
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || getClass() != o2.getClass()) {
            return false;
        }
        Screen screen = (Screen) o2;
        return this.ptr == screen.ptr && this.adapter == screen.adapter && this.depth == screen.depth && this.f11827x == screen.f11827x && this.f11828y == screen.f11828y && this.width == screen.width && this.height == screen.height && this.visibleX == screen.visibleX && this.visibleY == screen.visibleY && this.visibleWidth == screen.visibleWidth && this.visibleHeight == screen.visibleHeight && this.resolutionX == screen.resolutionX && this.resolutionY == screen.resolutionY && Float.compare(screen.uiScale, this.uiScale) == 0 && Float.compare(screen.renderScale, this.renderScale) == 0;
    }

    public int hashCode() {
        int result = (31 * 17) + ((int) (this.ptr ^ (this.ptr >>> 32)));
        return (31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * result) + this.adapter)) + this.depth)) + this.f11827x)) + this.f11828y)) + this.width)) + this.height)) + this.visibleX)) + this.visibleY)) + this.visibleWidth)) + this.visibleHeight)) + this.resolutionX)) + this.resolutionY)) + (this.uiScale != 0.0f ? Float.floatToIntBits(this.uiScale) : 0))) + (this.renderScale != 0.0f ? Float.floatToIntBits(this.renderScale) : 0);
    }
}
