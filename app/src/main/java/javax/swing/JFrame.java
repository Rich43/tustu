package javax.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.WindowEvent;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import javax.swing.TransferHandler;
import sun.awt.SunToolkit;

/* loaded from: rt.jar:javax/swing/JFrame.class */
public class JFrame extends Frame implements WindowConstants, Accessible, RootPaneContainer, TransferHandler.HasGetTransferHandler {
    public static final int EXIT_ON_CLOSE = 3;
    private static final Object defaultLookAndFeelDecoratedKey = new StringBuffer("JFrame.defaultLookAndFeelDecorated");
    private int defaultCloseOperation;
    private TransferHandler transferHandler;
    protected JRootPane rootPane;
    protected boolean rootPaneCheckingEnabled;
    protected AccessibleContext accessibleContext;

    public JFrame() throws HeadlessException {
        this.defaultCloseOperation = 1;
        this.rootPaneCheckingEnabled = false;
        this.accessibleContext = null;
        frameInit();
    }

    public JFrame(GraphicsConfiguration graphicsConfiguration) {
        super(graphicsConfiguration);
        this.defaultCloseOperation = 1;
        this.rootPaneCheckingEnabled = false;
        this.accessibleContext = null;
        frameInit();
    }

    public JFrame(String str) throws HeadlessException {
        super(str);
        this.defaultCloseOperation = 1;
        this.rootPaneCheckingEnabled = false;
        this.accessibleContext = null;
        frameInit();
    }

    public JFrame(String str, GraphicsConfiguration graphicsConfiguration) {
        super(str, graphicsConfiguration);
        this.defaultCloseOperation = 1;
        this.rootPaneCheckingEnabled = false;
        this.accessibleContext = null;
        frameInit();
    }

    protected void frameInit() {
        enableEvents(72L);
        setLocale(JComponent.getDefaultLocale());
        setRootPane(createRootPane());
        setBackground(UIManager.getColor("control"));
        setRootPaneCheckingEnabled(true);
        if (isDefaultLookAndFeelDecorated() && UIManager.getLookAndFeel().getSupportsWindowDecorations()) {
            setUndecorated(true);
            getRootPane().setWindowDecorationStyle(1);
        }
        SunToolkit.checkAndSetPolicy(this);
    }

    protected JRootPane createRootPane() {
        JRootPane jRootPane = new JRootPane();
        jRootPane.setOpaque(true);
        return jRootPane;
    }

    @Override // java.awt.Window
    protected void processWindowEvent(WindowEvent windowEvent) {
        super.processWindowEvent(windowEvent);
        if (windowEvent.getID() == 201) {
            switch (this.defaultCloseOperation) {
                case 1:
                    setVisible(false);
                    break;
                case 2:
                    dispose();
                    break;
                case 3:
                    System.exit(0);
                    break;
            }
        }
    }

    public void setDefaultCloseOperation(int i2) {
        SecurityManager securityManager;
        if (i2 != 0 && i2 != 1 && i2 != 2 && i2 != 3) {
            throw new IllegalArgumentException("defaultCloseOperation must be one of: DO_NOTHING_ON_CLOSE, HIDE_ON_CLOSE, DISPOSE_ON_CLOSE, or EXIT_ON_CLOSE");
        }
        if (i2 == 3 && (securityManager = System.getSecurityManager()) != null) {
            securityManager.checkExit(0);
        }
        if (this.defaultCloseOperation != i2) {
            int i3 = this.defaultCloseOperation;
            this.defaultCloseOperation = i2;
            firePropertyChange("defaultCloseOperation", i3, i2);
        }
    }

    public int getDefaultCloseOperation() {
        return this.defaultCloseOperation;
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

    @Override // java.awt.Frame, java.awt.Window
    public void setIconImage(Image image) {
        super.setIconImage(image);
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

    public static void setDefaultLookAndFeelDecorated(boolean z2) {
        if (z2) {
            SwingUtilities.appContextPut(defaultLookAndFeelDecoratedKey, Boolean.TRUE);
        } else {
            SwingUtilities.appContextPut(defaultLookAndFeelDecoratedKey, Boolean.FALSE);
        }
    }

    public static boolean isDefaultLookAndFeelDecorated() {
        Boolean bool = (Boolean) SwingUtilities.appContextGet(defaultLookAndFeelDecoratedKey);
        if (bool == null) {
            bool = Boolean.FALSE;
        }
        return bool.booleanValue();
    }

    @Override // java.awt.Frame, java.awt.Container, java.awt.Component
    protected String paramString() {
        String str;
        if (this.defaultCloseOperation == 1) {
            str = "HIDE_ON_CLOSE";
        } else if (this.defaultCloseOperation == 2) {
            str = "DISPOSE_ON_CLOSE";
        } else if (this.defaultCloseOperation == 0) {
            str = "DO_NOTHING_ON_CLOSE";
        } else if (this.defaultCloseOperation == 3) {
            str = "EXIT_ON_CLOSE";
        } else {
            str = "";
        }
        return super.paramString() + ",defaultCloseOperation=" + str + ",rootPane=" + (this.rootPane != null ? this.rootPane.toString() : "") + ",rootPaneCheckingEnabled=" + (this.rootPaneCheckingEnabled ? "true" : "false");
    }

    @Override // java.awt.Frame, java.awt.Window, java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJFrame();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JFrame$AccessibleJFrame.class */
    protected class AccessibleJFrame extends Frame.AccessibleAWTFrame {
        protected AccessibleJFrame() {
            super();
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public String getAccessibleName() {
            if (this.accessibleName != null) {
                return this.accessibleName;
            }
            if (JFrame.this.getTitle() == null) {
                return super.getAccessibleName();
            }
            return JFrame.this.getTitle();
        }

        @Override // java.awt.Frame.AccessibleAWTFrame, java.awt.Window.AccessibleAWTWindow, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleStateSet getAccessibleStateSet() {
            AccessibleStateSet accessibleStateSet = super.getAccessibleStateSet();
            if (JFrame.this.isResizable()) {
                accessibleStateSet.add(AccessibleState.RESIZABLE);
            }
            if (JFrame.this.getFocusOwner() != null) {
                accessibleStateSet.add(AccessibleState.ACTIVE);
            }
            return accessibleStateSet;
        }
    }
}
