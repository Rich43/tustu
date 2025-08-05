package javax.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.LayoutManager;
import java.awt.Window;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.swing.TransferHandler;
import sun.awt.SunToolkit;

/* loaded from: rt.jar:javax/swing/JWindow.class */
public class JWindow extends Window implements Accessible, RootPaneContainer, TransferHandler.HasGetTransferHandler {
    protected JRootPane rootPane;
    protected boolean rootPaneCheckingEnabled;
    private TransferHandler transferHandler;
    protected AccessibleContext accessibleContext;

    public JWindow() {
        this((Frame) null);
    }

    public JWindow(GraphicsConfiguration graphicsConfiguration) {
        this(null, graphicsConfiguration);
        super.setFocusableWindowState(false);
    }

    public JWindow(Frame frame) {
        super(frame == null ? SwingUtilities.getSharedOwnerFrame() : frame);
        this.rootPaneCheckingEnabled = false;
        this.accessibleContext = null;
        if (frame == null) {
            addWindowListener(SwingUtilities.getSharedOwnerFrameShutdownListener());
        }
        windowInit();
    }

    public JWindow(Window window) {
        super(window == null ? SwingUtilities.getSharedOwnerFrame() : window);
        this.rootPaneCheckingEnabled = false;
        this.accessibleContext = null;
        if (window == null) {
            addWindowListener(SwingUtilities.getSharedOwnerFrameShutdownListener());
        }
        windowInit();
    }

    public JWindow(Window window, GraphicsConfiguration graphicsConfiguration) {
        super(window == null ? SwingUtilities.getSharedOwnerFrame() : window, graphicsConfiguration);
        this.rootPaneCheckingEnabled = false;
        this.accessibleContext = null;
        if (window == null) {
            addWindowListener(SwingUtilities.getSharedOwnerFrameShutdownListener());
        }
        windowInit();
    }

    protected void windowInit() {
        setLocale(JComponent.getDefaultLocale());
        setRootPane(createRootPane());
        setRootPaneCheckingEnabled(true);
        SunToolkit.checkAndSetPolicy(this);
    }

    protected JRootPane createRootPane() {
        JRootPane jRootPane = new JRootPane();
        jRootPane.setOpaque(true);
        return jRootPane;
    }

    protected boolean isRootPaneCheckingEnabled() {
        return this.rootPaneCheckingEnabled;
    }

    public void setTransferHandler(TransferHandler transferHandler) {
        TransferHandler transferHandler2 = this.transferHandler;
        this.transferHandler = transferHandler;
        SwingUtilities.installSwingDropTargetAsNecessary(this, this.transferHandler);
        firePropertyChange("transferHandler", transferHandler2, transferHandler);
    }

    @Override // javax.swing.TransferHandler.HasGetTransferHandler
    public TransferHandler getTransferHandler() {
        return this.transferHandler;
    }

    @Override // java.awt.Container, java.awt.Component
    public void update(Graphics graphics) {
        paint(graphics);
    }

    protected void setRootPaneCheckingEnabled(boolean z2) {
        this.rootPaneCheckingEnabled = z2;
    }

    @Override // java.awt.Container
    protected void addImpl(Component component, Object obj, int i2) {
        if (isRootPaneCheckingEnabled()) {
            getContentPane().add(component, obj, i2);
        } else {
            super.addImpl(component, obj, i2);
        }
    }

    @Override // java.awt.Container
    public void remove(Component component) {
        if (component == this.rootPane) {
            super.remove(component);
        } else {
            getContentPane().remove(component);
        }
    }

    @Override // java.awt.Container
    public void setLayout(LayoutManager layoutManager) {
        if (isRootPaneCheckingEnabled()) {
            getContentPane().setLayout(layoutManager);
        } else {
            super.setLayout(layoutManager);
        }
    }

    @Override // javax.swing.RootPaneContainer
    public JRootPane getRootPane() {
        return this.rootPane;
    }

    protected void setRootPane(JRootPane jRootPane) {
        if (this.rootPane != null) {
            remove(this.rootPane);
        }
        this.rootPane = jRootPane;
        if (this.rootPane != null) {
            boolean zIsRootPaneCheckingEnabled = isRootPaneCheckingEnabled();
            try {
                setRootPaneCheckingEnabled(false);
                add(this.rootPane, BorderLayout.CENTER);
            } finally {
                setRootPaneCheckingEnabled(zIsRootPaneCheckingEnabled);
            }
        }
    }

    @Override // javax.swing.RootPaneContainer
    public Container getContentPane() {
        return getRootPane().getContentPane();
    }

    @Override // javax.swing.RootPaneContainer
    public void setContentPane(Container container) {
        getRootPane().setContentPane(container);
    }

    @Override // javax.swing.RootPaneContainer
    public JLayeredPane getLayeredPane() {
        return getRootPane().getLayeredPane();
    }

    @Override // javax.swing.RootPaneContainer
    public void setLayeredPane(JLayeredPane jLayeredPane) {
        getRootPane().setLayeredPane(jLayeredPane);
    }

    @Override // javax.swing.RootPaneContainer
    public Component getGlassPane() {
        return getRootPane().getGlassPane();
    }

    @Override // javax.swing.RootPaneContainer
    public void setGlassPane(Component component) {
        getRootPane().setGlassPane(component);
    }

    @Override // java.awt.Component
    public Graphics getGraphics() {
        JComponent.getGraphicsInvoked(this);
        return super.getGraphics();
    }

    @Override // java.awt.Component
    public void repaint(long j2, int i2, int i3, int i4, int i5) {
        if (RepaintManager.HANDLE_TOP_LEVEL_PAINT) {
            RepaintManager.currentManager(this).addDirtyRegion(this, i2, i3, i4, i5);
        } else {
            super.repaint(j2, i2, i3, i4, i5);
        }
    }

    @Override // java.awt.Container, java.awt.Component
    protected String paramString() {
        return super.paramString() + ",rootPaneCheckingEnabled=" + (this.rootPaneCheckingEnabled ? "true" : "false");
    }

    @Override // java.awt.Window, java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJWindow();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JWindow$AccessibleJWindow.class */
    protected class AccessibleJWindow extends Window.AccessibleAWTWindow {
        protected AccessibleJWindow() {
            super();
        }
    }
}
