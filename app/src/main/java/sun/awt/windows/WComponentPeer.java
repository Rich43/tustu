package sun.awt.windows;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.BufferCapabilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.dnd.DropTarget;
import java.awt.dnd.peer.DropTargetPeer;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.InvocationEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.PaintEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.VolatileImage;
import java.awt.peer.ComponentPeer;
import java.awt.peer.ContainerPeer;
import sun.awt.AWTAccessor;
import sun.awt.CausedFocusEvent;
import sun.awt.PaintEventDispatcher;
import sun.awt.RepaintArea;
import sun.awt.SunToolkit;
import sun.awt.Win32GraphicsConfig;
import sun.awt.Win32GraphicsEnvironment;
import sun.awt.event.IgnorePaintEvent;
import sun.awt.image.SunVolatileImage;
import sun.awt.image.ToolkitImage;
import sun.java2d.InvalidPipeException;
import sun.java2d.ScreenUpdateManager;
import sun.java2d.SurfaceData;
import sun.java2d.d3d.D3DSurfaceData;
import sun.java2d.opengl.OGLSurfaceData;
import sun.java2d.pipe.Region;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:sun/awt/windows/WComponentPeer.class */
public abstract class WComponentPeer extends WObjectPeer implements ComponentPeer, DropTargetPeer {
    protected volatile long hwnd;
    SurfaceData surfaceData;
    private RepaintArea paintArea;
    protected Win32GraphicsConfig winGraphicsConfig;
    private Color foreground;
    private Color background;
    private Font font;
    int nDropTargets;
    long nativeDropTargetContext;
    private static final double BANDING_DIVISOR = 4.0d;
    private int updateX1;
    private int updateY1;
    private int updateX2;
    private int updateY2;
    private static final PlatformLogger log = PlatformLogger.getLogger("sun.awt.windows.WComponentPeer");
    private static final PlatformLogger shapeLog = PlatformLogger.getLogger("sun.awt.windows.shape.WComponentPeer");
    private static final PlatformLogger focusLog = PlatformLogger.getLogger("sun.awt.windows.focus.WComponentPeer");
    static final Font defaultFont = new Font(Font.DIALOG, 0, 12);
    boolean isLayouting = false;
    boolean paintPending = false;
    int oldWidth = -1;
    int oldHeight = -1;
    private int numBackBuffers = 0;
    private VolatileImage backBuffer = null;
    private BufferCapabilities backBufferCaps = null;
    public int serialNum = 0;
    private volatile boolean isAccelCapable = true;

    @Override // java.awt.peer.ComponentPeer
    public native boolean isObscured();

    private native synchronized void pShow();

    native synchronized void hide();

    native synchronized void enable();

    native synchronized void disable();

    @Override // java.awt.peer.ComponentPeer
    public native Point getLocationOnScreen();

    private native void reshapeNoCheck(int i2, int i3, int i4, int i5);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native synchronized void updateWindow();

    private native int[] createPrintedPixels(int i2, int i3, int i4, int i5, int i6);

    public native synchronized void reshape(int i2, int i3, int i4, int i5);

    native void nativeHandleEvent(AWTEvent aWTEvent);

    native void setFocus(boolean z2);

    private native synchronized void _dispose();

    private native void _setForeground(int i2);

    private native void _setBackground(int i2);

    native synchronized void _setFont(Font font);

    abstract void create(WComponentPeer wComponentPeer);

    native synchronized void start();

    public native void beginValidate();

    public native void endValidate();

    native long addNativeDropTarget();

    native void removeNativeDropTarget();

    native boolean nativeHandlesWheelScrolling();

    native void pSetParent(ComponentPeer componentPeer);

    native void setRectangularShape(int i2, int i3, int i4, int i5, Region region);

    private native void setZOrder(long j2);

    @Override // sun.awt.windows.WObjectPeer
    public /* bridge */ /* synthetic */ Object getTarget() {
        return super.getTarget();
    }

    @Override // sun.awt.windows.WObjectPeer
    public /* bridge */ /* synthetic */ long getData() {
        return super.getData();
    }

    @Override // java.awt.peer.ComponentPeer
    public boolean canDetermineObscurity() {
        return true;
    }

    public long getHWnd() {
        return this.hwnd;
    }

    @Override // java.awt.peer.ComponentPeer
    public void setVisible(boolean z2) {
        if (z2) {
            show();
        } else {
            hide();
        }
    }

    public void show() {
        Dimension size = ((Component) this.target).getSize();
        this.oldHeight = size.height;
        this.oldWidth = size.width;
        pShow();
    }

    @Override // java.awt.peer.ComponentPeer
    public void setEnabled(boolean z2) {
        if (z2) {
            enable();
        } else {
            disable();
        }
    }

    @Override // java.awt.peer.ComponentPeer
    public void setBounds(int i2, int i3, int i4, int i5, int i6) {
        this.paintPending = (i4 == this.oldWidth && i5 == this.oldHeight) ? false : true;
        if ((i6 & 16384) != 0) {
            reshapeNoCheck(i2, i3, i4, i5);
        } else {
            reshape(i2, i3, i4, i5);
        }
        if (i4 != this.oldWidth || i5 != this.oldHeight) {
            try {
                replaceSurfaceData();
            } catch (InvalidPipeException e2) {
            }
            this.oldWidth = i4;
            this.oldHeight = i5;
        }
        this.serialNum++;
    }

    void dynamicallyLayoutContainer() {
        if (log.isLoggable(PlatformLogger.Level.FINE) && WToolkit.getNativeContainer((Component) this.target) != null) {
            log.fine("Assertion (parent == null) failed");
        }
        final Container container = (Container) this.target;
        WToolkit.executeOnEventHandlerThread(container, new Runnable() { // from class: sun.awt.windows.WComponentPeer.1
            @Override // java.lang.Runnable
            public void run() {
                container.invalidate();
                container.validate();
                if ((WComponentPeer.this.surfaceData instanceof D3DSurfaceData.D3DWindowSurfaceData) || (WComponentPeer.this.surfaceData instanceof OGLSurfaceData)) {
                    try {
                        WComponentPeer.this.replaceSurfaceData();
                    } catch (InvalidPipeException e2) {
                    }
                }
            }
        });
    }

    void paintDamagedAreaImmediately() {
        updateWindow();
        SunToolkit.flushPendingEvents();
        this.paintArea.paint(this.target, shouldClearRectBeforePaint());
    }

    public void paint(Graphics graphics) {
        ((Component) this.target).paint(graphics);
    }

    public void repaint(long j2, int i2, int i3, int i4, int i5) {
    }

    @Override // java.awt.peer.ComponentPeer
    public void print(Graphics graphics) {
        Component component = (Component) this.target;
        int width = component.getWidth();
        int height = component.getHeight();
        int i2 = (int) (height / BANDING_DIVISOR);
        if (i2 == 0) {
            i2 = height;
        }
        int i3 = 0;
        while (true) {
            int i4 = i3;
            if (i4 < height) {
                int i5 = (i4 + i2) - 1;
                if (i5 >= height) {
                    i5 = height - 1;
                }
                int i6 = (i5 - i4) + 1;
                Color background = component.getBackground();
                int[] iArrCreatePrintedPixels = createPrintedPixels(0, i4, width, i6, background == null ? 255 : background.getAlpha());
                if (iArrCreatePrintedPixels != null) {
                    BufferedImage bufferedImage = new BufferedImage(width, i6, 2);
                    bufferedImage.setRGB(0, 0, width, i6, iArrCreatePrintedPixels, 0, width);
                    graphics.drawImage(bufferedImage, 0, i4, null);
                    bufferedImage.flush();
                }
                i3 = i4 + i2;
            } else {
                component.print(graphics);
                return;
            }
        }
    }

    @Override // java.awt.peer.ComponentPeer
    public void coalescePaintEvent(PaintEvent paintEvent) {
        Rectangle updateRect = paintEvent.getUpdateRect();
        if (!(paintEvent instanceof IgnorePaintEvent)) {
            this.paintArea.add(updateRect, paintEvent.getID());
        }
        if (log.isLoggable(PlatformLogger.Level.FINEST)) {
            switch (paintEvent.getID()) {
                case 800:
                    log.finest("coalescePaintEvent: PAINT: add: x = " + updateRect.f12372x + ", y = " + updateRect.f12373y + ", width = " + updateRect.width + ", height = " + updateRect.height);
                    break;
                case 801:
                    log.finest("coalescePaintEvent: UPDATE: add: x = " + updateRect.f12372x + ", y = " + updateRect.f12373y + ", width = " + updateRect.width + ", height = " + updateRect.height);
                    break;
            }
        }
    }

    public boolean handleJavaKeyEvent(KeyEvent keyEvent) {
        return false;
    }

    public void handleJavaMouseEvent(MouseEvent mouseEvent) {
        switch (mouseEvent.getID()) {
            case 501:
                if (this.target == mouseEvent.getSource() && !((Component) this.target).isFocusOwner() && WKeyboardFocusManagerPeer.shouldFocusOnClick((Component) this.target)) {
                    WKeyboardFocusManagerPeer.requestFocusFor((Component) this.target, CausedFocusEvent.Cause.MOUSE_EVENT);
                    break;
                }
                break;
        }
    }

    @Override // java.awt.peer.ComponentPeer
    public void handleEvent(AWTEvent aWTEvent) {
        int id = aWTEvent.getID();
        if ((aWTEvent instanceof InputEvent) && !((InputEvent) aWTEvent).isConsumed() && ((Component) this.target).isEnabled()) {
            if ((aWTEvent instanceof MouseEvent) && !(aWTEvent instanceof MouseWheelEvent)) {
                handleJavaMouseEvent((MouseEvent) aWTEvent);
            } else if ((aWTEvent instanceof KeyEvent) && handleJavaKeyEvent((KeyEvent) aWTEvent)) {
            }
        }
        switch (id) {
            case 800:
                this.paintPending = false;
            case 801:
                if (!this.isLayouting && !this.paintPending) {
                    this.paintArea.paint(this.target, shouldClearRectBeforePaint());
                    break;
                }
                break;
            case 1004:
            case 1005:
                handleJavaFocusEvent((FocusEvent) aWTEvent);
            default:
                nativeHandleEvent(aWTEvent);
                break;
        }
    }

    void handleJavaFocusEvent(FocusEvent focusEvent) {
        if (focusLog.isLoggable(PlatformLogger.Level.FINER)) {
            focusLog.finer(focusEvent.toString());
        }
        setFocus(focusEvent.getID() == 1004);
    }

    public Dimension getMinimumSize() {
        return ((Component) this.target).getSize();
    }

    @Override // java.awt.peer.ComponentPeer
    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

    @Override // java.awt.peer.ComponentPeer
    public void layout() {
    }

    public Rectangle getBounds() {
        return ((Component) this.target).getBounds();
    }

    public boolean isFocusable() {
        return false;
    }

    @Override // java.awt.peer.ComponentPeer
    public GraphicsConfiguration getGraphicsConfiguration() {
        if (this.winGraphicsConfig != null) {
            return this.winGraphicsConfig;
        }
        return ((Component) this.target).getGraphicsConfiguration();
    }

    public SurfaceData getSurfaceData() {
        return this.surfaceData;
    }

    public void replaceSurfaceData() {
        replaceSurfaceData(this.numBackBuffers, this.backBufferCaps);
    }

    public void createScreenSurface(boolean z2) {
        this.surfaceData = ScreenUpdateManager.getInstance().createScreenSurface((Win32GraphicsConfig) getGraphicsConfiguration(), this, this.numBackBuffers, z2);
    }

    public void replaceSurfaceData(int i2, BufferCapabilities bufferCapabilities) {
        synchronized (((Component) this.target).getTreeLock()) {
            synchronized (this) {
                if (this.pData == 0) {
                    return;
                }
                this.numBackBuffers = i2;
                ScreenUpdateManager screenUpdateManager = ScreenUpdateManager.getInstance();
                SurfaceData surfaceData = this.surfaceData;
                screenUpdateManager.dropScreenSurface(surfaceData);
                createScreenSurface(true);
                if (surfaceData != null) {
                    surfaceData.invalidate();
                }
                VolatileImage volatileImage = this.backBuffer;
                if (this.numBackBuffers > 0) {
                    this.backBufferCaps = bufferCapabilities;
                    this.backBuffer = ((Win32GraphicsConfig) getGraphicsConfiguration()).createBackBuffer(this);
                } else if (this.backBuffer != null) {
                    this.backBufferCaps = null;
                    this.backBuffer = null;
                }
                if (surfaceData != null) {
                    surfaceData.flush();
                }
                if (volatileImage != null) {
                    volatileImage.flush();
                }
            }
        }
    }

    public void replaceSurfaceDataLater() {
        Runnable runnable = new Runnable() { // from class: sun.awt.windows.WComponentPeer.2
            @Override // java.lang.Runnable
            public void run() {
                if (!WComponentPeer.this.isDisposed()) {
                    try {
                        WComponentPeer.this.replaceSurfaceData();
                    } catch (InvalidPipeException e2) {
                    }
                }
            }
        };
        Component component = (Component) this.target;
        if (!PaintEventDispatcher.getPaintEventDispatcher().queueSurfaceDataReplacing(component, runnable)) {
            postEvent(new InvocationEvent(component, runnable));
        }
    }

    @Override // java.awt.peer.ComponentPeer
    public boolean updateGraphicsData(GraphicsConfiguration graphicsConfiguration) {
        this.winGraphicsConfig = (Win32GraphicsConfig) graphicsConfiguration;
        try {
            replaceSurfaceData();
            return false;
        } catch (InvalidPipeException e2) {
            return false;
        }
    }

    @Override // java.awt.peer.ComponentPeer
    public ColorModel getColorModel() {
        GraphicsConfiguration graphicsConfiguration = getGraphicsConfiguration();
        if (graphicsConfiguration != null) {
            return graphicsConfiguration.getColorModel();
        }
        return null;
    }

    public ColorModel getDeviceColorModel() {
        Win32GraphicsConfig win32GraphicsConfig = (Win32GraphicsConfig) getGraphicsConfiguration();
        if (win32GraphicsConfig != null) {
            return win32GraphicsConfig.getDeviceColorModel();
        }
        return null;
    }

    public ColorModel getColorModel(int i2) {
        GraphicsConfiguration graphicsConfiguration = getGraphicsConfiguration();
        if (graphicsConfiguration != null) {
            return graphicsConfiguration.getColorModel(i2);
        }
        return null;
    }

    @Override // java.awt.peer.ComponentPeer
    public Graphics getGraphics() {
        Graphics translucentGraphics;
        if (isDisposed()) {
            return null;
        }
        Component component = (Component) getTarget();
        Window containingWindow = SunToolkit.getContainingWindow(component);
        if (containingWindow != null && (translucentGraphics = ((WWindowPeer) containingWindow.getPeer()).getTranslucentGraphics()) != null) {
            int x2 = 0;
            int y2 = 0;
            Component parent = component;
            while (true) {
                Component component2 = parent;
                if (component2 != containingWindow) {
                    x2 += component2.getX();
                    y2 += component2.getY();
                    parent = component2.getParent();
                } else {
                    translucentGraphics.translate(x2, y2);
                    translucentGraphics.clipRect(0, 0, component.getWidth(), component.getHeight());
                    return translucentGraphics;
                }
            }
        } else {
            SurfaceData surfaceData = this.surfaceData;
            if (surfaceData != null) {
                Color color = this.background;
                if (color == null) {
                    color = SystemColor.window;
                }
                Color color2 = this.foreground;
                if (color2 == null) {
                    color2 = SystemColor.windowText;
                }
                Font font = this.font;
                if (font == null) {
                    font = defaultFont;
                }
                return ScreenUpdateManager.getInstance().createGraphics(surfaceData, this, color2, color, font);
            }
            return null;
        }
    }

    @Override // java.awt.peer.ComponentPeer
    public FontMetrics getFontMetrics(Font font) {
        return WFontMetrics.getFontMetrics(font);
    }

    @Override // sun.awt.windows.WObjectPeer
    protected void disposeImpl() {
        SurfaceData surfaceData = this.surfaceData;
        this.surfaceData = null;
        ScreenUpdateManager.getInstance().dropScreenSurface(surfaceData);
        surfaceData.invalidate();
        WToolkit.targetDisposedPeer(this.target, this);
        _dispose();
    }

    public void disposeLater() {
        postEvent(new InvocationEvent(this.target, new Runnable() { // from class: sun.awt.windows.WComponentPeer.3
            @Override // java.lang.Runnable
            public void run() {
                WComponentPeer.this.dispose();
            }
        }));
    }

    @Override // java.awt.peer.ComponentPeer
    public synchronized void setForeground(Color color) {
        this.foreground = color;
        _setForeground(color.getRGB());
    }

    @Override // java.awt.peer.ComponentPeer
    public synchronized void setBackground(Color color) {
        this.background = color;
        _setBackground(color.getRGB());
    }

    public Color getBackgroundNoSync() {
        return this.background;
    }

    @Override // java.awt.peer.ComponentPeer
    public synchronized void setFont(Font font) {
        this.font = font;
        _setFont(font);
    }

    @Override // java.awt.peer.ComponentPeer
    public void updateCursorImmediately() {
        WGlobalCursorManager.getCursorManager().updateCursorImmediately();
    }

    @Override // java.awt.peer.ComponentPeer
    public boolean requestFocus(Component component, boolean z2, boolean z3, long j2, CausedFocusEvent.Cause cause) {
        if (WKeyboardFocusManagerPeer.processSynchronousLightweightTransfer((Component) this.target, component, z2, z3, j2)) {
            return true;
        }
        switch (WKeyboardFocusManagerPeer.shouldNativelyFocusHeavyweight((Component) this.target, component, z2, z3, j2, cause)) {
            case 0:
                return false;
            case 1:
                return true;
            case 2:
                if (focusLog.isLoggable(PlatformLogger.Level.FINER)) {
                    focusLog.finer("Proceeding with request to " + ((Object) component) + " in " + this.target);
                }
                Window containingWindow = SunToolkit.getContainingWindow((Component) this.target);
                if (containingWindow == null) {
                    return rejectFocusRequestHelper("WARNING: Parent window is null");
                }
                WWindowPeer wWindowPeer = (WWindowPeer) containingWindow.getPeer();
                if (wWindowPeer == null) {
                    return rejectFocusRequestHelper("WARNING: Parent window's peer is null");
                }
                boolean zRequestWindowFocus = wWindowPeer.requestWindowFocus(cause);
                if (focusLog.isLoggable(PlatformLogger.Level.FINER)) {
                    focusLog.finer("Requested window focus: " + zRequestWindowFocus);
                }
                if (!zRequestWindowFocus || !containingWindow.isFocused()) {
                    return rejectFocusRequestHelper("Waiting for asynchronous processing of the request");
                }
                return WKeyboardFocusManagerPeer.deliverFocus(component, (Component) this.target, z2, z3, j2, cause);
            default:
                return false;
        }
    }

    private boolean rejectFocusRequestHelper(String str) {
        if (focusLog.isLoggable(PlatformLogger.Level.FINER)) {
            focusLog.finer(str);
        }
        WKeyboardFocusManagerPeer.removeLastFocusRequest((Component) this.target);
        return false;
    }

    @Override // java.awt.peer.ComponentPeer
    public Image createImage(ImageProducer imageProducer) {
        return new ToolkitImage(imageProducer);
    }

    @Override // java.awt.peer.ComponentPeer
    public Image createImage(int i2, int i3) {
        return ((Win32GraphicsConfig) getGraphicsConfiguration()).createAcceleratedImage((Component) this.target, i2, i3);
    }

    @Override // java.awt.peer.ComponentPeer
    public VolatileImage createVolatileImage(int i2, int i3) {
        return new SunVolatileImage((Component) this.target, i2, i3);
    }

    @Override // java.awt.peer.ComponentPeer
    public boolean prepareImage(Image image, int i2, int i3, ImageObserver imageObserver) {
        return Toolkit.getDefaultToolkit().prepareImage(image, i2, i3, imageObserver);
    }

    @Override // java.awt.peer.ComponentPeer
    public int checkImage(Image image, int i2, int i3, ImageObserver imageObserver) {
        return Toolkit.getDefaultToolkit().checkImage(image, i2, i3, imageObserver);
    }

    public String toString() {
        return getClass().getName() + "[" + this.target + "]";
    }

    WComponentPeer(Component component) {
        this.target = component;
        this.paintArea = new RepaintArea();
        create(getNativeParent());
        checkCreation();
        createScreenSurface(false);
        initialize();
        start();
    }

    WComponentPeer getNativeParent() {
        return (WComponentPeer) WToolkit.targetToPeer(SunToolkit.getNativeContainer((Component) this.target));
    }

    protected void checkCreation() {
        if (this.hwnd == 0 || this.pData == 0) {
            if (this.createError != null) {
                throw this.createError;
            }
            throw new InternalError("couldn't create component peer");
        }
    }

    void initialize() {
        if (((Component) this.target).isVisible()) {
            show();
        }
        Color foreground = ((Component) this.target).getForeground();
        if (foreground != null) {
            setForeground(foreground);
        }
        Font font = ((Component) this.target).getFont();
        if (font != null) {
            setFont(font);
        }
        if (!((Component) this.target).isEnabled()) {
            disable();
        }
        Rectangle bounds = ((Component) this.target).getBounds();
        setBounds(bounds.f12372x, bounds.f12373y, bounds.width, bounds.height, 3);
    }

    void handleRepaint(int i2, int i3, int i4, int i5) {
    }

    void handleExpose(int i2, int i3, int i4, int i5) {
        postPaintIfNecessary(i2, i3, i4, i5);
    }

    public void handlePaint(int i2, int i3, int i4, int i5) {
        postPaintIfNecessary(i2, i3, i4, i5);
    }

    private void postPaintIfNecessary(int i2, int i3, int i4, int i5) {
        PaintEvent paintEventCreatePaintEvent;
        if (!AWTAccessor.getComponentAccessor().getIgnoreRepaint((Component) this.target) && (paintEventCreatePaintEvent = PaintEventDispatcher.getPaintEventDispatcher().createPaintEvent((Component) this.target, i2, i3, i4, i5)) != null) {
            postEvent(paintEventCreatePaintEvent);
        }
    }

    void postEvent(AWTEvent aWTEvent) {
        preprocessPostEvent(aWTEvent);
        WToolkit.postEvent(WToolkit.targetToAppContext(this.target), aWTEvent);
    }

    void preprocessPostEvent(AWTEvent aWTEvent) {
    }

    public void beginLayout() {
        this.isLayouting = true;
    }

    public void endLayout() {
        if (!this.paintArea.isEmpty() && !this.paintPending && !((Component) this.target).getIgnoreRepaint()) {
            postEvent(new PaintEvent((Component) this.target, 800, new Rectangle()));
        }
        this.isLayouting = false;
    }

    public Dimension preferredSize() {
        return getPreferredSize();
    }

    @Override // java.awt.dnd.peer.DropTargetPeer
    public synchronized void addDropTarget(DropTarget dropTarget) {
        if (this.nDropTargets == 0) {
            this.nativeDropTargetContext = addNativeDropTarget();
        }
        this.nDropTargets++;
    }

    @Override // java.awt.dnd.peer.DropTargetPeer
    public synchronized void removeDropTarget(DropTarget dropTarget) {
        this.nDropTargets--;
        if (this.nDropTargets == 0) {
            removeNativeDropTarget();
            this.nativeDropTargetContext = 0L;
        }
    }

    @Override // java.awt.peer.ComponentPeer
    public boolean handlesWheelScrolling() {
        return nativeHandlesWheelScrolling();
    }

    public boolean isPaintPending() {
        return this.paintPending && this.isLayouting;
    }

    @Override // java.awt.peer.ComponentPeer
    public void createBuffers(int i2, BufferCapabilities bufferCapabilities) throws AWTException {
        ((Win32GraphicsConfig) getGraphicsConfiguration()).assertOperationSupported((Component) this.target, i2, bufferCapabilities);
        try {
            replaceSurfaceData(i2 - 1, bufferCapabilities);
        } catch (InvalidPipeException e2) {
            throw new AWTException(e2.getMessage());
        }
    }

    @Override // java.awt.peer.ComponentPeer
    public void destroyBuffers() {
        replaceSurfaceData(0, null);
    }

    @Override // java.awt.peer.ComponentPeer
    public void flip(int i2, int i3, int i4, int i5, BufferCapabilities.FlipContents flipContents) {
        VolatileImage volatileImage = this.backBuffer;
        if (volatileImage == null) {
            throw new IllegalStateException("Buffers have not been created");
        }
        ((Win32GraphicsConfig) getGraphicsConfiguration()).flip(this, (Component) this.target, volatileImage, i2, i3, i4, i5, flipContents);
    }

    @Override // java.awt.peer.ComponentPeer
    public synchronized Image getBackBuffer() {
        VolatileImage volatileImage = this.backBuffer;
        if (volatileImage == null) {
            throw new IllegalStateException("Buffers have not been created");
        }
        return volatileImage;
    }

    public BufferCapabilities getBackBufferCaps() {
        return this.backBufferCaps;
    }

    public int getBackBuffersNum() {
        return this.numBackBuffers;
    }

    public boolean shouldClearRectBeforePaint() {
        return true;
    }

    @Override // java.awt.peer.ComponentPeer
    public void reparent(ContainerPeer containerPeer) {
        pSetParent(containerPeer);
    }

    @Override // java.awt.peer.ComponentPeer
    public boolean isReparentSupported() {
        return true;
    }

    public void setBoundsOperation(int i2) {
    }

    public boolean isAccelCapable() {
        if (this.isAccelCapable && isContainingTopLevelAccelCapable((Component) this.target)) {
            return !SunToolkit.isContainingTopLevelTranslucent((Component) this.target) || Win32GraphicsEnvironment.isVistaOS();
        }
        return false;
    }

    public void disableAcceleration() {
        this.isAccelCapable = false;
    }

    private static final boolean isContainingTopLevelAccelCapable(Component component) {
        while (component != null && !(component instanceof WEmbeddedFrame)) {
            component = component.getParent();
        }
        if (component == null) {
            return true;
        }
        return ((WEmbeddedFramePeer) component.getPeer()).isAccelCapable();
    }

    @Override // java.awt.peer.ComponentPeer
    public void applyShape(Region region) {
        if (shapeLog.isLoggable(PlatformLogger.Level.FINER)) {
            shapeLog.finer("*** INFO: Setting shape: PEER: " + ((Object) this) + "; TARGET: " + this.target + "; SHAPE: " + ((Object) region));
        }
        if (region != null) {
            setRectangularShape(region.getLoX(), region.getLoY(), region.getHiX(), region.getHiY(), region.isRectangular() ? null : region);
        } else {
            setRectangularShape(0, 0, 0, 0, null);
        }
    }

    @Override // java.awt.peer.ComponentPeer
    public void setZOrder(ComponentPeer componentPeer) {
        setZOrder(componentPeer != null ? ((WComponentPeer) componentPeer).getHWnd() : 0L);
    }

    public boolean isLightweightFramePeer() {
        return false;
    }
}
