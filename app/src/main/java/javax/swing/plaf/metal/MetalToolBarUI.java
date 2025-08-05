package javax.swing.plaf.metal;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ContainerListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicToolBarUI;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalToolBarUI.class */
public class MetalToolBarUI extends BasicToolBarUI {
    private static List<WeakReference<JComponent>> components = new ArrayList();
    protected ContainerListener contListener;
    protected PropertyChangeListener rolloverListener;
    private static Border nonRolloverBorder;
    private JMenuBar lastMenuBar;

    static synchronized void register(JComponent jComponent) {
        if (jComponent == null) {
            throw new NullPointerException("JComponent must be non-null");
        }
        components.add(new WeakReference<>(jComponent));
    }

    static synchronized void unregister(JComponent jComponent) {
        for (int size = components.size() - 1; size >= 0; size--) {
            JComponent jComponent2 = components.get(size).get();
            if (jComponent2 == jComponent || jComponent2 == null) {
                components.remove(size);
            }
        }
    }

    static synchronized Object findRegisteredComponentOfType(JComponent jComponent, Class cls) {
        JRootPane rootPane = SwingUtilities.getRootPane(jComponent);
        if (rootPane != null) {
            for (int size = components.size() - 1; size >= 0; size--) {
                JComponent jComponent2 = components.get(size).get();
                if (jComponent2 == null) {
                    components.remove(size);
                } else if (cls.isInstance(jComponent2) && SwingUtilities.getRootPane(jComponent2) == rootPane) {
                    return jComponent2;
                }
            }
            return null;
        }
        return null;
    }

    static boolean doesMenuBarBorderToolBar(JMenuBar jMenuBar) {
        JToolBar jToolBar = (JToolBar) findRegisteredComponentOfType(jMenuBar, JToolBar.class);
        if (jToolBar != null && jToolBar.getOrientation() == 0) {
            JRootPane rootPane = SwingUtilities.getRootPane(jMenuBar);
            Point pointConvertPoint = SwingUtilities.convertPoint(jMenuBar, new Point(0, 0), rootPane);
            int i2 = pointConvertPoint.f12370x;
            int i3 = pointConvertPoint.f12371y;
            pointConvertPoint.f12371y = 0;
            pointConvertPoint.f12370x = 0;
            Point pointConvertPoint2 = SwingUtilities.convertPoint(jToolBar, pointConvertPoint, rootPane);
            return pointConvertPoint2.f12370x == i2 && i3 + jMenuBar.getHeight() == pointConvertPoint2.f12371y && jMenuBar.getWidth() == jToolBar.getWidth();
        }
        return false;
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new MetalToolBarUI();
    }

    @Override // javax.swing.plaf.basic.BasicToolBarUI, javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        super.installUI(jComponent);
        register(jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicToolBarUI, javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
        nonRolloverBorder = null;
        unregister(jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicToolBarUI
    protected void installListeners() {
        super.installListeners();
        this.contListener = createContainerListener();
        if (this.contListener != null) {
            this.toolBar.addContainerListener(this.contListener);
        }
        this.rolloverListener = createRolloverListener();
        if (this.rolloverListener != null) {
            this.toolBar.addPropertyChangeListener(this.rolloverListener);
        }
    }

    @Override // javax.swing.plaf.basic.BasicToolBarUI
    protected void uninstallListeners() {
        super.uninstallListeners();
        if (this.contListener != null) {
            this.toolBar.removeContainerListener(this.contListener);
        }
        this.rolloverListener = createRolloverListener();
        if (this.rolloverListener != null) {
            this.toolBar.removePropertyChangeListener(this.rolloverListener);
        }
    }

    @Override // javax.swing.plaf.basic.BasicToolBarUI
    protected Border createRolloverBorder() {
        return super.createRolloverBorder();
    }

    @Override // javax.swing.plaf.basic.BasicToolBarUI
    protected Border createNonRolloverBorder() {
        return super.createNonRolloverBorder();
    }

    private Border createNonRolloverToggleBorder() {
        return createNonRolloverBorder();
    }

    @Override // javax.swing.plaf.basic.BasicToolBarUI
    protected void setBorderToNonRollover(Component component) {
        if ((component instanceof JToggleButton) && !(component instanceof JCheckBox)) {
            JToggleButton jToggleButton = (JToggleButton) component;
            Border border = jToggleButton.getBorder();
            super.setBorderToNonRollover(component);
            if (border instanceof UIResource) {
                if (nonRolloverBorder == null) {
                    nonRolloverBorder = createNonRolloverToggleBorder();
                }
                jToggleButton.setBorder(nonRolloverBorder);
                return;
            }
            return;
        }
        super.setBorderToNonRollover(component);
    }

    protected ContainerListener createContainerListener() {
        return null;
    }

    protected PropertyChangeListener createRolloverListener() {
        return null;
    }

    @Override // javax.swing.plaf.basic.BasicToolBarUI
    protected MouseInputListener createDockingListener() {
        return new MetalDockingListener(this.toolBar);
    }

    protected void setDragOffset(Point point) {
        if (!GraphicsEnvironment.isHeadless()) {
            if (this.dragWindow == null) {
                this.dragWindow = createDragWindow(this.toolBar);
            }
            this.dragWindow.setOffset(point);
        }
    }

    @Override // javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        if (graphics == null) {
            throw new NullPointerException("graphics must be non-null");
        }
        if (jComponent.isOpaque() && (jComponent.getBackground() instanceof UIResource) && ((JToolBar) jComponent).getOrientation() == 0 && UIManager.get("MenuBar.gradient") != null) {
            JRootPane rootPane = SwingUtilities.getRootPane(jComponent);
            JMenuBar jMenuBar = (JMenuBar) findRegisteredComponentOfType(jComponent, JMenuBar.class);
            if (jMenuBar != null && jMenuBar.isOpaque() && (jMenuBar.getBackground() instanceof UIResource)) {
                Point pointConvertPoint = SwingUtilities.convertPoint(jComponent, new Point(0, 0), rootPane);
                int i2 = pointConvertPoint.f12370x;
                int i3 = pointConvertPoint.f12371y;
                pointConvertPoint.f12371y = 0;
                pointConvertPoint.f12370x = 0;
                Point pointConvertPoint2 = SwingUtilities.convertPoint(jMenuBar, pointConvertPoint, rootPane);
                if (pointConvertPoint2.f12370x == i2 && i3 == pointConvertPoint2.f12371y + jMenuBar.getHeight() && jMenuBar.getWidth() == jComponent.getWidth() && MetalUtils.drawGradient(jComponent, graphics, "MenuBar.gradient", 0, -jMenuBar.getHeight(), jComponent.getWidth(), jComponent.getHeight() + jMenuBar.getHeight(), true)) {
                    setLastMenuBar(jMenuBar);
                    paint(graphics, jComponent);
                    return;
                }
            }
            if (MetalUtils.drawGradient(jComponent, graphics, "MenuBar.gradient", 0, 0, jComponent.getWidth(), jComponent.getHeight(), true)) {
                setLastMenuBar(null);
                paint(graphics, jComponent);
                return;
            }
        }
        setLastMenuBar(null);
        super.update(graphics, jComponent);
    }

    private void setLastMenuBar(JMenuBar jMenuBar) {
        if (MetalLookAndFeel.usingOcean() && this.lastMenuBar != jMenuBar) {
            if (this.lastMenuBar != null) {
                this.lastMenuBar.repaint();
            }
            if (jMenuBar != null) {
                jMenuBar.repaint();
            }
            this.lastMenuBar = jMenuBar;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalToolBarUI$MetalContainerListener.class */
    protected class MetalContainerListener extends BasicToolBarUI.ToolBarContListener {
        protected MetalContainerListener() {
            super();
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalToolBarUI$MetalRolloverListener.class */
    protected class MetalRolloverListener extends BasicToolBarUI.PropertyListener {
        protected MetalRolloverListener() {
            super();
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalToolBarUI$MetalDockingListener.class */
    protected class MetalDockingListener extends BasicToolBarUI.DockingListener {
        private boolean pressedInBumps;

        public MetalDockingListener(JToolBar jToolBar) {
            super(jToolBar);
            this.pressedInBumps = false;
        }

        @Override // javax.swing.plaf.basic.BasicToolBarUI.DockingListener, java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            super.mousePressed(mouseEvent);
            if (!this.toolBar.isEnabled()) {
                return;
            }
            this.pressedInBumps = false;
            Rectangle rectangle = new Rectangle();
            if (this.toolBar.getOrientation() == 0) {
                rectangle.setBounds(MetalUtils.isLeftToRight(this.toolBar) ? 0 : this.toolBar.getSize().width - 14, 0, 14, this.toolBar.getSize().height);
            } else {
                rectangle.setBounds(0, 0, this.toolBar.getSize().width, 14);
            }
            if (rectangle.contains(mouseEvent.getPoint())) {
                this.pressedInBumps = true;
                Point point = mouseEvent.getPoint();
                if (!MetalUtils.isLeftToRight(this.toolBar)) {
                    point.f12370x -= this.toolBar.getSize().width - this.toolBar.getPreferredSize().width;
                }
                MetalToolBarUI.this.setDragOffset(point);
            }
        }

        @Override // javax.swing.plaf.basic.BasicToolBarUI.DockingListener, java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
            if (this.pressedInBumps) {
                super.mouseDragged(mouseEvent);
            }
        }
    }
}
