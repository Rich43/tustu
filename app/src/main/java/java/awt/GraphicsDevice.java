package java.awt;

import sun.awt.AppContext;
import sun.awt.SunToolkit;

/* loaded from: rt.jar:java/awt/GraphicsDevice.class */
public abstract class GraphicsDevice {
    private Window fullScreenWindow;
    private AppContext fullScreenAppContext;
    private final Object fsAppContextLock = new Object();
    private Rectangle windowedModeBounds;
    public static final int TYPE_RASTER_SCREEN = 0;
    public static final int TYPE_PRINTER = 1;
    public static final int TYPE_IMAGE_BUFFER = 2;

    /* loaded from: rt.jar:java/awt/GraphicsDevice$WindowTranslucency.class */
    public enum WindowTranslucency {
        PERPIXEL_TRANSPARENT,
        TRANSLUCENT,
        PERPIXEL_TRANSLUCENT
    }

    public abstract int getType();

    public abstract String getIDstring();

    public abstract GraphicsConfiguration[] getConfigurations();

    public abstract GraphicsConfiguration getDefaultConfiguration();

    protected GraphicsDevice() {
    }

    public GraphicsConfiguration getBestConfiguration(GraphicsConfigTemplate graphicsConfigTemplate) {
        return graphicsConfigTemplate.getBestConfiguration(getConfigurations());
    }

    public boolean isFullScreenSupported() {
        return false;
    }

    public void setFullScreenWindow(Window window) {
        if (window != null) {
            if (window.getShape() != null) {
                window.setShape(null);
            }
            if (window.getOpacity() < 1.0f) {
                window.setOpacity(1.0f);
            }
            if (!window.isOpaque()) {
                Color background = window.getBackground();
                window.setBackground(new Color(background.getRed(), background.getGreen(), background.getBlue(), 255));
            }
            GraphicsConfiguration graphicsConfiguration = window.getGraphicsConfiguration();
            if (graphicsConfiguration != null && graphicsConfiguration.getDevice() != this && graphicsConfiguration.getDevice().getFullScreenWindow() == window) {
                graphicsConfiguration.getDevice().setFullScreenWindow(null);
            }
        }
        if (this.fullScreenWindow != null && this.windowedModeBounds != null) {
            if (this.windowedModeBounds.width == 0) {
                this.windowedModeBounds.width = 1;
            }
            if (this.windowedModeBounds.height == 0) {
                this.windowedModeBounds.height = 1;
            }
            this.fullScreenWindow.setBounds(this.windowedModeBounds);
        }
        synchronized (this.fsAppContextLock) {
            if (window == null) {
                this.fullScreenAppContext = null;
            } else {
                this.fullScreenAppContext = AppContext.getAppContext();
            }
            this.fullScreenWindow = window;
        }
        if (this.fullScreenWindow != null) {
            this.windowedModeBounds = this.fullScreenWindow.getBounds();
            GraphicsConfiguration defaultConfiguration = getDefaultConfiguration();
            Rectangle bounds = defaultConfiguration.getBounds();
            if (SunToolkit.isDispatchThreadForAppContext(this.fullScreenWindow)) {
                this.fullScreenWindow.setGraphicsConfiguration(defaultConfiguration);
            }
            this.fullScreenWindow.setBounds(bounds.f12372x, bounds.f12373y, bounds.width, bounds.height);
            this.fullScreenWindow.setVisible(true);
            this.fullScreenWindow.toFront();
        }
    }

    public Window getFullScreenWindow() {
        Window window = null;
        synchronized (this.fsAppContextLock) {
            if (this.fullScreenAppContext == AppContext.getAppContext()) {
                window = this.fullScreenWindow;
            }
        }
        return window;
    }

    public boolean isDisplayChangeSupported() {
        return false;
    }

    public void setDisplayMode(DisplayMode displayMode) {
        throw new UnsupportedOperationException("Cannot change display mode");
    }

    public DisplayMode getDisplayMode() {
        GraphicsConfiguration defaultConfiguration = getDefaultConfiguration();
        Rectangle bounds = defaultConfiguration.getBounds();
        return new DisplayMode(bounds.width, bounds.height, defaultConfiguration.getColorModel().getPixelSize(), 0);
    }

    public DisplayMode[] getDisplayModes() {
        return new DisplayMode[]{getDisplayMode()};
    }

    public int getAvailableAcceleratedMemory() {
        return -1;
    }

    public boolean isWindowTranslucencySupported(WindowTranslucency windowTranslucency) {
        switch (windowTranslucency) {
            case PERPIXEL_TRANSPARENT:
                return isWindowShapingSupported();
            case TRANSLUCENT:
                return isWindowOpacitySupported();
            case PERPIXEL_TRANSLUCENT:
                return isWindowPerpixelTranslucencySupported();
            default:
                return false;
        }
    }

    static boolean isWindowShapingSupported() {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        if (!(defaultToolkit instanceof SunToolkit)) {
            return false;
        }
        return ((SunToolkit) defaultToolkit).isWindowShapingSupported();
    }

    static boolean isWindowOpacitySupported() {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        if (!(defaultToolkit instanceof SunToolkit)) {
            return false;
        }
        return ((SunToolkit) defaultToolkit).isWindowOpacitySupported();
    }

    boolean isWindowPerpixelTranslucencySupported() {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        return (defaultToolkit instanceof SunToolkit) && ((SunToolkit) defaultToolkit).isWindowTranslucencySupported() && getTranslucencyCapableGC() != null;
    }

    GraphicsConfiguration getTranslucencyCapableGC() {
        GraphicsConfiguration defaultConfiguration = getDefaultConfiguration();
        if (defaultConfiguration.isTranslucencyCapable()) {
            return defaultConfiguration;
        }
        GraphicsConfiguration[] configurations = getConfigurations();
        for (int i2 = 0; i2 < configurations.length; i2++) {
            if (configurations[i2].isTranslucencyCapable()) {
                return configurations[i2];
            }
        }
        return null;
    }
}
