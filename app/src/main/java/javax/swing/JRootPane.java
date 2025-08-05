package javax.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.IllegalComponentStateException;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.security.AccessController;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.RootPaneUI;
import sun.awt.AWTAccessor;
import sun.security.action.GetBooleanAction;

/* loaded from: rt.jar:javax/swing/JRootPane.class */
public class JRootPane extends JComponent implements Accessible {
    private static final String uiClassID = "RootPaneUI";
    private static final boolean LOG_DISABLE_TRUE_DOUBLE_BUFFERING = ((Boolean) AccessController.doPrivileged(new GetBooleanAction("swing.logDoubleBufferingDisable"))).booleanValue();
    private static final boolean IGNORE_DISABLE_TRUE_DOUBLE_BUFFERING = ((Boolean) AccessController.doPrivileged(new GetBooleanAction("swing.ignoreDoubleBufferingDisable"))).booleanValue();
    public static final int NONE = 0;
    public static final int FRAME = 1;
    public static final int PLAIN_DIALOG = 2;
    public static final int INFORMATION_DIALOG = 3;
    public static final int ERROR_DIALOG = 4;
    public static final int COLOR_CHOOSER_DIALOG = 5;
    public static final int FILE_CHOOSER_DIALOG = 6;
    public static final int QUESTION_DIALOG = 7;
    public static final int WARNING_DIALOG = 8;
    private int windowDecorationStyle;
    protected JMenuBar menuBar;
    protected Container contentPane;
    protected JLayeredPane layeredPane;
    protected Component glassPane;
    protected JButton defaultButton;

    @Deprecated
    protected DefaultAction defaultPressAction;

    @Deprecated
    protected DefaultAction defaultReleaseAction;
    boolean useTrueDoubleBuffering = true;

    public JRootPane() {
        setGlassPane(createGlassPane());
        setLayeredPane(createLayeredPane());
        setContentPane(createContentPane());
        setLayout(createRootLayout());
        setDoubleBuffered(true);
        updateUI();
    }

    @Override // javax.swing.JComponent
    public void setDoubleBuffered(boolean z2) {
        if (isDoubleBuffered() != z2) {
            super.setDoubleBuffered(z2);
            RepaintManager.currentManager((JComponent) this).doubleBufferingChanged(this);
        }
    }

    public int getWindowDecorationStyle() {
        return this.windowDecorationStyle;
    }

    public void setWindowDecorationStyle(int i2) {
        if (i2 < 0 || i2 > 8) {
            throw new IllegalArgumentException("Invalid decoration style");
        }
        int windowDecorationStyle = getWindowDecorationStyle();
        this.windowDecorationStyle = i2;
        firePropertyChange("windowDecorationStyle", windowDecorationStyle, i2);
    }

    public RootPaneUI getUI() {
        return (RootPaneUI) this.ui;
    }

    public void setUI(RootPaneUI rootPaneUI) {
        super.setUI((ComponentUI) rootPaneUI);
    }

    @Override // javax.swing.JComponent
    public void updateUI() {
        setUI((RootPaneUI) UIManager.getUI(this));
    }

    @Override // javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
    }

    protected JLayeredPane createLayeredPane() {
        JLayeredPane jLayeredPane = new JLayeredPane();
        jLayeredPane.setName(getName() + ".layeredPane");
        return jLayeredPane;
    }

    protected Container createContentPane() {
        JPanel jPanel = new JPanel();
        jPanel.setName(getName() + ".contentPane");
        jPanel.setLayout(new BorderLayout() { // from class: javax.swing.JRootPane.1
            @Override // java.awt.BorderLayout, java.awt.LayoutManager2
            public void addLayoutComponent(Component component, Object obj) {
                if (obj == null) {
                    obj = BorderLayout.CENTER;
                }
                super.addLayoutComponent(component, obj);
            }
        });
        return jPanel;
    }

    protected Component createGlassPane() {
        JPanel jPanel = new JPanel();
        jPanel.setName(getName() + ".glassPane");
        jPanel.setVisible(false);
        jPanel.setOpaque(false);
        return jPanel;
    }

    protected LayoutManager createRootLayout() {
        return new RootLayout();
    }

    public void setJMenuBar(JMenuBar jMenuBar) {
        if (this.menuBar != null && this.menuBar.getParent() == this.layeredPane) {
            this.layeredPane.remove(this.menuBar);
        }
        this.menuBar = jMenuBar;
        if (this.menuBar != null) {
            this.layeredPane.add(this.menuBar, JLayeredPane.FRAME_CONTENT_LAYER);
        }
    }

    @Deprecated
    public void setMenuBar(JMenuBar jMenuBar) {
        if (this.menuBar != null && this.menuBar.getParent() == this.layeredPane) {
            this.layeredPane.remove(this.menuBar);
        }
        this.menuBar = jMenuBar;
        if (this.menuBar != null) {
            this.layeredPane.add(this.menuBar, JLayeredPane.FRAME_CONTENT_LAYER);
        }
    }

    public JMenuBar getJMenuBar() {
        return this.menuBar;
    }

    @Deprecated
    public JMenuBar getMenuBar() {
        return this.menuBar;
    }

    public void setContentPane(Container container) {
        if (container == null) {
            throw new IllegalComponentStateException("contentPane cannot be set to null.");
        }
        if (this.contentPane != null && this.contentPane.getParent() == this.layeredPane) {
            this.layeredPane.remove(this.contentPane);
        }
        this.contentPane = container;
        this.layeredPane.add(this.contentPane, JLayeredPane.FRAME_CONTENT_LAYER);
    }

    public Container getContentPane() {
        return this.contentPane;
    }

    public void setLayeredPane(JLayeredPane jLayeredPane) {
        if (jLayeredPane == null) {
            throw new IllegalComponentStateException("layeredPane cannot be set to null.");
        }
        if (this.layeredPane != null && this.layeredPane.getParent() == this) {
            remove(this.layeredPane);
        }
        this.layeredPane = jLayeredPane;
        add(this.layeredPane, -1);
    }

    public JLayeredPane getLayeredPane() {
        return this.layeredPane;
    }

    public void setGlassPane(Component component) {
        if (component == null) {
            throw new NullPointerException("glassPane cannot be set to null.");
        }
        AWTAccessor.getComponentAccessor().setMixingCutoutShape(component, new Rectangle());
        boolean zIsVisible = false;
        if (this.glassPane != null && this.glassPane.getParent() == this) {
            remove(this.glassPane);
            zIsVisible = this.glassPane.isVisible();
        }
        component.setVisible(zIsVisible);
        this.glassPane = component;
        add(this.glassPane, 0);
        if (zIsVisible) {
            repaint();
        }
    }

    public Component getGlassPane() {
        return this.glassPane;
    }

    @Override // javax.swing.JComponent, java.awt.Container
    public boolean isValidateRoot() {
        return true;
    }

    @Override // javax.swing.JComponent
    public boolean isOptimizedDrawingEnabled() {
        return !this.glassPane.isVisible();
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void addNotify() {
        super.addNotify();
        enableEvents(8L);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void removeNotify() {
        super.removeNotify();
    }

    public void setDefaultButton(JButton jButton) {
        JButton jButton2 = this.defaultButton;
        if (jButton2 != jButton) {
            this.defaultButton = jButton;
            if (jButton2 != null) {
                jButton2.repaint();
            }
            if (jButton != null) {
                jButton.repaint();
            }
        }
        firePropertyChange("defaultButton", jButton2, jButton);
    }

    public JButton getDefaultButton() {
        return this.defaultButton;
    }

    final void setUseTrueDoubleBuffering(boolean z2) {
        this.useTrueDoubleBuffering = z2;
    }

    final boolean getUseTrueDoubleBuffering() {
        return this.useTrueDoubleBuffering;
    }

    final void disableTrueDoubleBuffering() {
        if (this.useTrueDoubleBuffering && !IGNORE_DISABLE_TRUE_DOUBLE_BUFFERING) {
            if (LOG_DISABLE_TRUE_DOUBLE_BUFFERING) {
                System.out.println("Disabling true double buffering for " + ((Object) this));
                Thread.dumpStack();
            }
            this.useTrueDoubleBuffering = false;
            RepaintManager.currentManager((JComponent) this).doubleBufferingChanged(this);
        }
    }

    /* loaded from: rt.jar:javax/swing/JRootPane$DefaultAction.class */
    static class DefaultAction extends AbstractAction {
        JButton owner;
        JRootPane root;
        boolean press;

        DefaultAction(JRootPane jRootPane, boolean z2) {
            this.root = jRootPane;
            this.press = z2;
        }

        public void setOwner(JButton jButton) {
            this.owner = jButton;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            if (this.owner != null && SwingUtilities.getRootPane(this.owner) == this.root) {
                ButtonModel model = this.owner.getModel();
                if (this.press) {
                    model.setArmed(true);
                    model.setPressed(true);
                } else {
                    model.setPressed(false);
                }
            }
        }

        @Override // javax.swing.AbstractAction, javax.swing.Action
        public boolean isEnabled() {
            return this.owner.getModel().isEnabled();
        }
    }

    @Override // java.awt.Container
    protected void addImpl(Component component, Object obj, int i2) {
        super.addImpl(component, obj, i2);
        if (this.glassPane != null && this.glassPane.getParent() == this && getComponent(0) != this.glassPane) {
            add(this.glassPane, 0);
        }
    }

    /* loaded from: rt.jar:javax/swing/JRootPane$RootLayout.class */
    protected class RootLayout implements LayoutManager2, Serializable {
        protected RootLayout() {
        }

        @Override // java.awt.LayoutManager
        public Dimension preferredLayoutSize(Container container) {
            Dimension size;
            Dimension dimension;
            Insets insets = JRootPane.this.getInsets();
            if (JRootPane.this.contentPane != null) {
                size = JRootPane.this.contentPane.getPreferredSize();
            } else {
                size = container.getSize();
            }
            if (JRootPane.this.menuBar != null && JRootPane.this.menuBar.isVisible()) {
                dimension = JRootPane.this.menuBar.getPreferredSize();
            } else {
                dimension = new Dimension(0, 0);
            }
            return new Dimension(Math.max(size.width, dimension.width) + insets.left + insets.right, size.height + dimension.height + insets.top + insets.bottom);
        }

        @Override // java.awt.LayoutManager
        public Dimension minimumLayoutSize(Container container) {
            Dimension size;
            Dimension dimension;
            Insets insets = JRootPane.this.getInsets();
            if (JRootPane.this.contentPane != null) {
                size = JRootPane.this.contentPane.getMinimumSize();
            } else {
                size = container.getSize();
            }
            if (JRootPane.this.menuBar != null && JRootPane.this.menuBar.isVisible()) {
                dimension = JRootPane.this.menuBar.getMinimumSize();
            } else {
                dimension = new Dimension(0, 0);
            }
            return new Dimension(Math.max(size.width, dimension.width) + insets.left + insets.right, size.height + dimension.height + insets.top + insets.bottom);
        }

        @Override // java.awt.LayoutManager2
        public Dimension maximumLayoutSize(Container container) {
            Dimension dimension;
            Dimension dimension2;
            Insets insets = JRootPane.this.getInsets();
            if (JRootPane.this.menuBar != null && JRootPane.this.menuBar.isVisible()) {
                dimension = JRootPane.this.menuBar.getMaximumSize();
            } else {
                dimension = new Dimension(0, 0);
            }
            if (JRootPane.this.contentPane != null) {
                dimension2 = JRootPane.this.contentPane.getMaximumSize();
            } else {
                dimension2 = new Dimension(Integer.MAX_VALUE, (((Integer.MAX_VALUE - insets.top) - insets.bottom) - dimension.height) - 1);
            }
            return new Dimension(Math.min(dimension2.width, dimension.width) + insets.left + insets.right, dimension2.height + dimension.height + insets.top + insets.bottom);
        }

        @Override // java.awt.LayoutManager
        public void layoutContainer(Container container) {
            Rectangle bounds = container.getBounds();
            Insets insets = JRootPane.this.getInsets();
            int i2 = 0;
            int i3 = (bounds.width - insets.right) - insets.left;
            int i4 = (bounds.height - insets.top) - insets.bottom;
            if (JRootPane.this.layeredPane != null) {
                JRootPane.this.layeredPane.setBounds(insets.left, insets.top, i3, i4);
            }
            if (JRootPane.this.glassPane != null) {
                JRootPane.this.glassPane.setBounds(insets.left, insets.top, i3, i4);
            }
            if (JRootPane.this.menuBar != null && JRootPane.this.menuBar.isVisible()) {
                Dimension preferredSize = JRootPane.this.menuBar.getPreferredSize();
                JRootPane.this.menuBar.setBounds(0, 0, i3, preferredSize.height);
                i2 = 0 + preferredSize.height;
            }
            if (JRootPane.this.contentPane != null) {
                JRootPane.this.contentPane.setBounds(0, i2, i3, i4 - i2);
            }
        }

        @Override // java.awt.LayoutManager
        public void addLayoutComponent(String str, Component component) {
        }

        @Override // java.awt.LayoutManager
        public void removeLayoutComponent(Component component) {
        }

        @Override // java.awt.LayoutManager2
        public void addLayoutComponent(Component component, Object obj) {
        }

        @Override // java.awt.LayoutManager2
        public float getLayoutAlignmentX(Container container) {
            return 0.0f;
        }

        @Override // java.awt.LayoutManager2
        public float getLayoutAlignmentY(Container container) {
            return 0.0f;
        }

        @Override // java.awt.LayoutManager2
        public void invalidateLayout(Container container) {
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    protected String paramString() {
        return super.paramString();
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJRootPane();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JRootPane$AccessibleJRootPane.class */
    protected class AccessibleJRootPane extends JComponent.AccessibleJComponent {
        protected AccessibleJRootPane() {
            super();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.ROOT_PANE;
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Container.AccessibleAWTContainer, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public int getAccessibleChildrenCount() {
            return super.getAccessibleChildrenCount();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Container.AccessibleAWTContainer, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public Accessible getAccessibleChild(int i2) {
            return super.getAccessibleChild(i2);
        }
    }
}
