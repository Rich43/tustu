package javax.swing;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.LayoutManager;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.swing.TransferHandler;
import sun.awt.SunToolkit;

/* loaded from: rt.jar:javax/swing/JApplet.class */
public class JApplet extends Applet implements Accessible, RootPaneContainer, TransferHandler.HasGetTransferHandler {
    protected JRootPane rootPane;
    private TransferHandler transferHandler;
    protected boolean rootPaneCheckingEnabled = false;
    protected AccessibleContext accessibleContext = null;

    public JApplet() throws HeadlessException {
        TimerQueue timerQueueSharedInstance = TimerQueue.sharedInstance();
        if (timerQueueSharedInstance != null) {
            timerQueueSharedInstance.startIfNeeded();
        }
        setForeground(Color.black);
        setBackground(Color.white);
        setLocale(JComponent.getDefaultLocale());
        setLayout(new BorderLayout());
        setRootPane(createRootPane());
        setRootPaneCheckingEnabled(true);
        setFocusTraversalPolicyProvider(true);
        SunToolkit.checkAndSetPolicy(this);
        enableEvents(8L);
    }

    protected JRootPane createRootPane() {
        JRootPane jRootPane = new JRootPane();
        jRootPane.setOpaque(true);
        return jRootPane;
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

    public void setJMenuBar(JMenuBar jMenuBar) {
        getRootPane().setMenuBar(jMenuBar);
    }

    public JMenuBar getJMenuBar() {
        return getRootPane().getMenuBar();
    }

    protected boolean isRootPaneCheckingEnabled() {
        return this.rootPaneCheckingEnabled;
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
        return super.paramString() + ",rootPane=" + (this.rootPane != null ? this.rootPane.toString() : "") + ",rootPaneCheckingEnabled=" + (this.rootPaneCheckingEnabled ? "true" : "false");
    }

    @Override // java.applet.Applet, java.awt.Panel, java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJApplet();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JApplet$AccessibleJApplet.class */
    protected class AccessibleJApplet extends Applet.AccessibleApplet {
        protected AccessibleJApplet() {
            super();
        }
    }
}
