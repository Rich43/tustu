package javafx.embed.swing;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.cursor.CursorFrame;
import com.sun.javafx.embed.EmbeddedSceneInterface;
import com.sun.javafx.embed.EmbeddedStageInterface;
import com.sun.javafx.embed.HostInterface;
import com.sun.javafx.stage.EmbeddedWindow;
import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.SecondaryLoop;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.InputMethodEvent;
import java.awt.event.InvocationEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.im.InputMethodRequests;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.nio.IntBuffer;
import java.security.AccessController;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.application.Platform;
import javafx.embed.swing.InputMethodSupport;
import javafx.scene.Scene;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import sun.awt.AppContext;
import sun.awt.CausedFocusEvent;
import sun.awt.SunToolkit;
import sun.awt.UngrabEvent;
import sun.java2d.SunGraphics2D;
import sun.util.logging.PlatformLogger;

/* loaded from: jfxrt.jar:javafx/embed/swing/JFXPanel.class */
public class JFXPanel extends JComponent {
    private static final PlatformLogger log = PlatformLogger.getLogger(JFXPanel.class.getName());
    private static AtomicInteger instanceCount = new AtomicInteger(0);
    private static PlatformImpl.FinishListener finishListener;
    private HostContainer hostContainer;
    private volatile EmbeddedWindow stage;
    private volatile Scene scene;
    private SwingDnD dnd;
    private EmbeddedStageInterface stagePeer;
    private EmbeddedSceneInterface scenePeer;
    private int pWidth;
    private int pHeight;
    private BufferedImage pixelsIm;
    private static boolean fxInitialized;
    private int scaleFactor = 1;
    private volatile int pPreferredWidth = -1;
    private volatile int pPreferredHeight = -1;
    private volatile int screenX = 0;
    private volatile int screenY = 0;
    private volatile float opacity = 1.0f;
    private AtomicInteger disableCount = new AtomicInteger(0);
    private boolean isCapturingMouse = false;
    private final AWTEventListener ungrabListener = event -> {
        if (event instanceof UngrabEvent) {
            SwingFXUtils.runOnFxThread(() -> {
                if (this.stagePeer != null) {
                    this.stagePeer.focusUngrab();
                }
            });
        }
        if ((event instanceof MouseEvent) && event.getID() == 501 && (event.getSource() instanceof Component)) {
            Window jfxPanelWindow = SwingUtilities.getWindowAncestor(this);
            Component source = (Component) event.getSource();
            Window eventWindow = source instanceof Window ? (Window) source : SwingUtilities.getWindowAncestor(source);
            if (jfxPanelWindow == eventWindow) {
                SwingFXUtils.runOnFxThread(() -> {
                    if (this.stagePeer != null) {
                        this.stagePeer.focusUngrab();
                    }
                });
            }
        }
    };

    private synchronized void registerFinishListener() {
        if (instanceCount.getAndIncrement() > 0) {
            return;
        }
        finishListener = new PlatformImpl.FinishListener() { // from class: javafx.embed.swing.JFXPanel.1
            @Override // com.sun.javafx.application.PlatformImpl.FinishListener
            public void idle(boolean implicitExit) {
            }

            @Override // com.sun.javafx.application.PlatformImpl.FinishListener
            public void exitCalled() {
            }
        };
        PlatformImpl.addListener(finishListener);
    }

    private synchronized void deregisterFinishListener() {
        if (instanceCount.decrementAndGet() > 0) {
            return;
        }
        PlatformImpl.removeListener(finishListener);
        finishListener = null;
    }

    private static synchronized void initFx() {
        AccessController.doPrivileged(() -> {
            System.setProperty("glass.win.uiScale", "100%");
            System.setProperty("glass.win.renderScale", "100%");
            return null;
        });
        if (fxInitialized) {
            return;
        }
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        defaultToolkit.getClass();
        EventQueue eventQueue = (EventQueue) AccessController.doPrivileged(defaultToolkit::getSystemEventQueue);
        if (EventQueue.isDispatchThread()) {
            SecondaryLoop secondaryLoop = eventQueue.createSecondaryLoop();
            Throwable[] th = {null};
            new Thread(() -> {
                try {
                    PlatformImpl.startup(() -> {
                    });
                } catch (Throwable t2) {
                    th[0] = t2;
                } finally {
                    secondaryLoop.exit();
                }
            }).start();
            secondaryLoop.enter();
            if (th[0] != null) {
                if (th[0] instanceof RuntimeException) {
                    throw ((RuntimeException) th[0]);
                }
                if (th[0] instanceof Error) {
                    throw ((Error) th[0]);
                }
                throw new RuntimeException("FX initialization failed", th[0]);
            }
        } else {
            PlatformImpl.startup(() -> {
            });
        }
        fxInitialized = true;
    }

    public JFXPanel() {
        initFx();
        this.hostContainer = new HostContainer();
        enableEvents(231485L);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }

    public Scene getScene() {
        return this.scene;
    }

    public void setScene(Scene newScene) {
        if (com.sun.javafx.tk.Toolkit.getToolkit().isFxUserThread()) {
            setSceneImpl(newScene);
            return;
        }
        CountDownLatch initLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            setSceneImpl(newScene);
            initLatch.countDown();
        });
        try {
            initLatch.await();
        } catch (InterruptedException z2) {
            z2.printStackTrace(System.err);
        }
    }

    private void setSceneImpl(Scene newScene) {
        if (this.stage != null && newScene == null) {
            this.stage.hide();
            this.stage = null;
        }
        this.scene = newScene;
        if (this.stage == null && newScene != null) {
            this.stage = new EmbeddedWindow(this.hostContainer);
        }
        if (this.stage != null) {
            this.stage.setScene(newScene);
            if (!this.stage.isShowing()) {
                this.stage.show();
            }
        }
    }

    @Override // javax.swing.JComponent
    public final void setOpaque(boolean opaque) {
        if (!opaque) {
            super.setOpaque(opaque);
        }
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public final boolean isOpaque() {
        return false;
    }

    private void sendMouseEventToFX(MouseEvent e2) {
        if (this.scenePeer == null || !isFxEnabled()) {
            return;
        }
        switch (e2.getID()) {
            case 501:
            case 502:
            case 506:
                if (e2.getButton() > 3) {
                    return;
                }
                break;
        }
        int extModifiers = e2.getModifiersEx();
        boolean primaryBtnDown = (extModifiers & 1024) != 0;
        boolean middleBtnDown = (extModifiers & 2048) != 0;
        boolean secondaryBtnDown = (extModifiers & 4096) != 0;
        if (e2.getID() == 506) {
            if (!this.isCapturingMouse) {
                return;
            }
        } else if (e2.getID() == 501) {
            this.isCapturingMouse = true;
        } else if (e2.getID() == 502) {
            if (!this.isCapturingMouse) {
                return;
            } else {
                this.isCapturingMouse = primaryBtnDown || middleBtnDown || secondaryBtnDown;
            }
        } else if (e2.getID() == 500) {
            return;
        }
        boolean popupTrigger = false;
        if (e2.getID() == 501 || e2.getID() == 502) {
            popupTrigger = e2.isPopupTrigger();
        }
        this.scenePeer.mouseEvent(SwingEvents.mouseIDToEmbedMouseType(e2.getID()), SwingEvents.mouseButtonToEmbedMouseButton(e2.getButton(), extModifiers), primaryBtnDown, middleBtnDown, secondaryBtnDown, e2.getX(), e2.getY(), e2.getXOnScreen(), e2.getYOnScreen(), (extModifiers & 64) != 0, (extModifiers & 128) != 0, (extModifiers & 512) != 0, (extModifiers & 256) != 0, SwingEvents.getWheelRotation(e2), popupTrigger);
        if (e2.isPopupTrigger()) {
            this.scenePeer.menuEvent(e2.getX(), e2.getY(), e2.getXOnScreen(), e2.getYOnScreen(), false);
        }
    }

    @Override // javax.swing.JComponent, java.awt.Component
    protected void processMouseEvent(MouseEvent e2) {
        if (e2.getID() == 501 && e2.getButton() == 1 && !hasFocus()) {
            requestFocus();
        }
        sendMouseEventToFX(e2);
        super.processMouseEvent(e2);
    }

    @Override // javax.swing.JComponent, java.awt.Component
    protected void processMouseMotionEvent(MouseEvent e2) {
        sendMouseEventToFX(e2);
        super.processMouseMotionEvent(e2);
    }

    @Override // java.awt.Component
    protected void processMouseWheelEvent(MouseWheelEvent e2) {
        sendMouseEventToFX(e2);
        super.processMouseWheelEvent(e2);
    }

    private void sendKeyEventToFX(KeyEvent e2) {
        if (this.scenePeer == null || !isFxEnabled()) {
            return;
        }
        char[] chars = e2.getKeyChar() == 65535 ? new char[0] : new char[]{SwingEvents.keyCharToEmbedKeyChar(e2.getKeyChar())};
        this.scenePeer.keyEvent(SwingEvents.keyIDToEmbedKeyType(e2.getID()), e2.getKeyCode(), chars, SwingEvents.keyModifiersToEmbedKeyModifiers(e2.getModifiersEx()));
    }

    @Override // javax.swing.JComponent, java.awt.Component
    protected void processKeyEvent(KeyEvent e2) {
        sendKeyEventToFX(e2);
        super.processKeyEvent(e2);
    }

    private void sendResizeEventToFX() {
        if (this.stagePeer != null) {
            this.stagePeer.setSize(this.pWidth, this.pHeight);
        }
        if (this.scenePeer != null) {
            this.scenePeer.setSize(this.pWidth, this.pHeight);
        }
    }

    @Override // java.awt.Component
    protected void processComponentEvent(ComponentEvent e2) {
        switch (e2.getID()) {
            case 100:
                if (updateScreenLocation()) {
                    sendMoveEventToFX();
                    break;
                }
                break;
            case 101:
                updateComponentSize();
                break;
        }
        super.processComponentEvent(e2);
    }

    private void updateComponentSize() {
        int oldWidth = this.pWidth;
        int oldHeight = this.pHeight;
        this.pWidth = Math.max(0, getWidth());
        this.pHeight = Math.max(0, getHeight());
        if (getBorder() != null) {
            Insets i2 = getBorder().getBorderInsets(this);
            this.pWidth -= i2.left + i2.right;
            this.pHeight -= i2.top + i2.bottom;
        }
        if (oldWidth != this.pWidth || oldHeight != this.pHeight) {
            resizePixelBuffer(this.scaleFactor);
            sendResizeEventToFX();
        }
    }

    private boolean updateScreenLocation() {
        synchronized (getTreeLock()) {
            if (isShowing()) {
                Point p2 = getLocationOnScreen();
                this.screenX = p2.f12370x;
                this.screenY = p2.f12371y;
                return true;
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendMoveEventToFX() {
        if (this.stagePeer == null) {
            return;
        }
        this.stagePeer.setLocation(this.screenX, this.screenY);
    }

    @Override // java.awt.Component
    protected void processHierarchyBoundsEvent(HierarchyEvent e2) {
        if (e2.getID() == 1401 && updateScreenLocation()) {
            sendMoveEventToFX();
        }
        super.processHierarchyBoundsEvent(e2);
    }

    @Override // java.awt.Component
    protected void processHierarchyEvent(HierarchyEvent e2) {
        if ((e2.getChangeFlags() & 4) != 0 && updateScreenLocation()) {
            sendMoveEventToFX();
        }
        super.processHierarchyEvent(e2);
    }

    private void sendFocusEventToFX(FocusEvent e2) {
        if (this.stage == null || this.stagePeer == null || !isFxEnabled()) {
            return;
        }
        boolean focused = e2.getID() == 1004;
        int focusCause = focused ? 0 : 3;
        if (focused && (e2 instanceof CausedFocusEvent)) {
            CausedFocusEvent ce = (CausedFocusEvent) e2;
            if (ce.getCause() == CausedFocusEvent.Cause.TRAVERSAL_FORWARD) {
                focusCause = 1;
            } else if (ce.getCause() == CausedFocusEvent.Cause.TRAVERSAL_BACKWARD) {
                focusCause = 2;
            }
        }
        this.stagePeer.setFocused(focused, focusCause);
    }

    @Override // java.awt.Component
    protected void processFocusEvent(FocusEvent e2) {
        sendFocusEventToFX(e2);
        super.processFocusEvent(e2);
    }

    private void resizePixelBuffer(int newScaleFactor) {
        if (this.pWidth <= 0 || this.pHeight <= 0) {
            this.pixelsIm = null;
            return;
        }
        BufferedImage oldIm = this.pixelsIm;
        this.pixelsIm = new BufferedImage(this.pWidth * newScaleFactor, this.pHeight * newScaleFactor, 2);
        if (oldIm != null) {
            double ratio = newScaleFactor / this.scaleFactor;
            int oldW = (int) Math.round(oldIm.getWidth() * ratio);
            int oldH = (int) Math.round(oldIm.getHeight() * ratio);
            Graphics g2 = this.pixelsIm.getGraphics();
            try {
                g2.drawImage(oldIm, 0, 0, oldW, oldH, null);
                g2.dispose();
            } catch (Throwable th) {
                g2.dispose();
                throw th;
            }
        }
    }

    @Override // java.awt.Component
    protected void processInputMethodEvent(InputMethodEvent e2) {
        if (e2.getID() == 1100) {
            sendInputMethodEventToFX(e2);
        }
        super.processInputMethodEvent(e2);
    }

    private void sendInputMethodEventToFX(InputMethodEvent e2) {
        String t2 = InputMethodSupport.getTextForEvent(e2);
        this.scenePeer.inputMethodEvent(javafx.scene.input.InputMethodEvent.INPUT_METHOD_TEXT_CHANGED, InputMethodSupport.inputMethodEventComposed(t2, e2.getCommittedCharacterCount()), t2.substring(0, e2.getCommittedCharacterCount()), e2.getCaret().getInsertionIndex());
    }

    @Override // javax.swing.JComponent
    protected void paintComponent(Graphics g2) {
        if (this.scenePeer == null || this.pixelsIm == null) {
            return;
        }
        DataBufferInt dataBuf = (DataBufferInt) this.pixelsIm.getRaster().getDataBuffer();
        int[] pixelsData = dataBuf.getData();
        IntBuffer buf = IntBuffer.wrap(pixelsData);
        if (!this.scenePeer.getPixels(buf, this.pWidth, this.pHeight)) {
        }
        Graphics gg = null;
        try {
            try {
                gg = g2.create();
                if (this.opacity < 1.0f && (gg instanceof Graphics2D)) {
                    Graphics2D g2d = (Graphics2D) gg;
                    AlphaComposite c2 = AlphaComposite.getInstance(3, this.opacity);
                    g2d.setComposite(c2);
                }
                if (getBorder() != null) {
                    Insets i2 = getBorder().getBorderInsets(this);
                    gg.translate(i2.left, i2.top);
                }
                gg.drawImage(this.pixelsIm, 0, 0, this.pWidth, this.pHeight, null);
                int newScaleFactor = this.scaleFactor;
                if (g2 instanceof SunGraphics2D) {
                    newScaleFactor = ((SunGraphics2D) g2).surfaceData.getDefaultScale();
                }
                if (this.scaleFactor != newScaleFactor) {
                    resizePixelBuffer(newScaleFactor);
                    this.scenePeer.setPixelScaleFactor(newScaleFactor);
                    this.scaleFactor = newScaleFactor;
                }
                if (gg != null) {
                    gg.dispose();
                }
            } catch (Throwable th) {
                th.printStackTrace();
                if (gg != null) {
                    gg.dispose();
                }
            }
        } catch (Throwable th2) {
            if (gg != null) {
                gg.dispose();
            }
            throw th2;
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        if (isPreferredSizeSet() || this.scenePeer == null) {
            return super.getPreferredSize();
        }
        return new Dimension(this.pPreferredWidth, this.pPreferredHeight);
    }

    private boolean isFxEnabled() {
        return this.disableCount.get() == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setFxEnabled(boolean enabled) {
        if (!enabled) {
            if (this.disableCount.incrementAndGet() == 1 && this.dnd != null) {
                this.dnd.removeNotify();
                return;
            }
            return;
        }
        if (this.disableCount.get() != 0 && this.disableCount.decrementAndGet() == 0 && this.dnd != null) {
            this.dnd.addNotify();
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void addNotify() {
        super.addNotify();
        registerFinishListener();
        AccessController.doPrivileged(() -> {
            getToolkit().addAWTEventListener(this.ungrabListener, -2147483632L);
            return null;
        });
        updateComponentSize();
        SwingFXUtils.runOnFxThread(() -> {
            if (this.stage != null && !this.stage.isShowing()) {
                this.stage.show();
                sendMoveEventToFX();
            }
        });
    }

    @Override // java.awt.Component
    public InputMethodRequests getInputMethodRequests() {
        if (this.scenePeer == null) {
            return null;
        }
        return new InputMethodSupport.InputMethodRequestsAdapter(this.scenePeer.getInputMethodRequests());
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void removeNotify() {
        SwingFXUtils.runOnFxThread(() -> {
            if (this.stage != null && this.stage.isShowing()) {
                this.stage.hide();
            }
        });
        this.pixelsIm = null;
        this.pWidth = 0;
        this.pHeight = 0;
        super.removeNotify();
        AccessController.doPrivileged(() -> {
            getToolkit().removeAWTEventListener(this.ungrabListener);
            return null;
        });
        getInputContext().removeNotify(this);
        deregisterFinishListener();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invokeOnClientEDT(Runnable r2) {
        AppContext context = SunToolkit.targetToAppContext(this);
        if (context == null) {
            if (log.isLoggable(PlatformLogger.Level.FINE)) {
                log.fine("null AppContext encountered!");
                return;
            }
            return;
        }
        SunToolkit.postEvent(context, new InvocationEvent(this, r2));
    }

    /* loaded from: jfxrt.jar:javafx/embed/swing/JFXPanel$HostContainer.class */
    private class HostContainer implements HostInterface {
        private HostContainer() {
        }

        @Override // com.sun.javafx.embed.HostInterface
        public void setEmbeddedStage(EmbeddedStageInterface embeddedStage) {
            JFXPanel.this.stagePeer = embeddedStage;
            if (JFXPanel.this.stagePeer != null) {
                if (JFXPanel.this.pWidth > 0 && JFXPanel.this.pHeight > 0) {
                    JFXPanel.this.stagePeer.setSize(JFXPanel.this.pWidth, JFXPanel.this.pHeight);
                }
                JFXPanel.this.invokeOnClientEDT(() -> {
                    if (JFXPanel.this.stagePeer != null && JFXPanel.this.isFocusOwner()) {
                        JFXPanel.this.stagePeer.setFocused(true, 0);
                    }
                });
                JFXPanel.this.sendMoveEventToFX();
            }
        }

        @Override // com.sun.javafx.embed.HostInterface
        public void setEmbeddedScene(EmbeddedSceneInterface embeddedScene) {
            if (JFXPanel.this.scenePeer != embeddedScene) {
                JFXPanel.this.scenePeer = embeddedScene;
                if (JFXPanel.this.scenePeer == null) {
                    JFXPanel.this.invokeOnClientEDT(() -> {
                        if (JFXPanel.this.dnd != null) {
                            JFXPanel.this.dnd.removeNotify();
                            JFXPanel.this.dnd = null;
                        }
                    });
                    return;
                }
                if (JFXPanel.this.pWidth > 0 && JFXPanel.this.pHeight > 0) {
                    JFXPanel.this.scenePeer.setSize(JFXPanel.this.pWidth, JFXPanel.this.pHeight);
                }
                JFXPanel.this.scenePeer.setPixelScaleFactor(JFXPanel.this.scaleFactor);
                JFXPanel.this.invokeOnClientEDT(() -> {
                    JFXPanel.this.dnd = new SwingDnD(JFXPanel.this, JFXPanel.this.scenePeer);
                    JFXPanel.this.dnd.addNotify();
                    if (JFXPanel.this.scenePeer != null) {
                        JFXPanel.this.scenePeer.setDragStartListener(JFXPanel.this.dnd.getDragStartListener());
                    }
                });
            }
        }

        @Override // com.sun.javafx.embed.HostInterface
        public boolean requestFocus() {
            return JFXPanel.this.requestFocusInWindow();
        }

        @Override // com.sun.javafx.embed.HostInterface
        public boolean traverseFocusOut(boolean forward) {
            KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
            if (forward) {
                kfm.focusNextComponent(JFXPanel.this);
                return true;
            }
            kfm.focusPreviousComponent(JFXPanel.this);
            return true;
        }

        @Override // com.sun.javafx.embed.HostInterface
        public void setPreferredSize(int width, int height) {
            JFXPanel.this.invokeOnClientEDT(() -> {
                JFXPanel.this.pPreferredWidth = width;
                JFXPanel.this.pPreferredHeight = height;
                JFXPanel.this.revalidate();
            });
        }

        @Override // com.sun.javafx.embed.HostInterface
        public void repaint() {
            JFXPanel.this.invokeOnClientEDT(() -> {
                JFXPanel.this.repaint();
            });
        }

        @Override // com.sun.javafx.embed.HostInterface
        public void setEnabled(boolean enabled) {
            JFXPanel.this.setFxEnabled(enabled);
        }

        @Override // com.sun.javafx.embed.HostInterface
        public void setCursor(CursorFrame cursorFrame) {
            Cursor cursor = getPlatformCursor(cursorFrame);
            JFXPanel.this.invokeOnClientEDT(() -> {
                JFXPanel.this.setCursor(cursor);
            });
        }

        private Cursor getPlatformCursor(CursorFrame cursorFrame) {
            Cursor cachedPlatformCursor = (Cursor) cursorFrame.getPlatformCursor(Cursor.class);
            if (cachedPlatformCursor != null) {
                return cachedPlatformCursor;
            }
            Cursor platformCursor = SwingCursors.embedCursorToCursor(cursorFrame);
            cursorFrame.setPlatforCursor(Cursor.class, platformCursor);
            return platformCursor;
        }

        @Override // com.sun.javafx.embed.HostInterface
        public boolean grabFocus() {
            if (PlatformUtil.isLinux()) {
                return true;
            }
            JFXPanel.this.invokeOnClientEDT(() -> {
                Window window = SwingUtilities.getWindowAncestor(JFXPanel.this);
                if (window != null && (JFXPanel.this.getToolkit() instanceof SunToolkit)) {
                    ((SunToolkit) JFXPanel.this.getToolkit()).grab(window);
                }
            });
            return true;
        }

        @Override // com.sun.javafx.embed.HostInterface
        public void ungrabFocus() {
            if (PlatformUtil.isLinux()) {
                return;
            }
            JFXPanel.this.invokeOnClientEDT(() -> {
                Window window = SwingUtilities.getWindowAncestor(JFXPanel.this);
                if (window != null && (JFXPanel.this.getToolkit() instanceof SunToolkit)) {
                    ((SunToolkit) JFXPanel.this.getToolkit()).ungrab(window);
                }
            });
        }
    }
}
