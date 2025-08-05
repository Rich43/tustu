package javax.swing.plaf.basic;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import javax.swing.DefaultDesktopManager;
import javax.swing.DesktopManager;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.DesktopIconUI;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicDesktopIconUI.class */
public class BasicDesktopIconUI extends DesktopIconUI {
    protected JInternalFrame.JDesktopIcon desktopIcon;
    protected JInternalFrame frame;
    protected JComponent iconPane;
    MouseInputListener mouseInputListener;

    public static ComponentUI createUI(JComponent jComponent) {
        return new BasicDesktopIconUI();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        JDesktopPane desktopPane;
        this.desktopIcon = (JInternalFrame.JDesktopIcon) jComponent;
        this.frame = this.desktopIcon.getInternalFrame();
        installDefaults();
        installComponents();
        JInternalFrame internalFrame = this.desktopIcon.getInternalFrame();
        if (internalFrame.isIcon() && internalFrame.getParent() == null && (desktopPane = this.desktopIcon.getDesktopPane()) != null) {
            DesktopManager desktopManager = desktopPane.getDesktopManager();
            if (desktopManager instanceof DefaultDesktopManager) {
                desktopManager.iconifyFrame(internalFrame);
            }
        }
        installListeners();
        JLayeredPane.putLayer(this.desktopIcon, JLayeredPane.getLayer((JComponent) this.frame));
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        JDesktopPane desktopPane;
        uninstallDefaults();
        uninstallComponents();
        JInternalFrame internalFrame = this.desktopIcon.getInternalFrame();
        if (internalFrame.isIcon() && (desktopPane = this.desktopIcon.getDesktopPane()) != null && (desktopPane.getDesktopManager() instanceof DefaultDesktopManager)) {
            internalFrame.putClientProperty("wasIconOnce", null);
            this.desktopIcon.setLocation(Integer.MIN_VALUE, 0);
        }
        uninstallListeners();
        this.frame = null;
        this.desktopIcon = null;
    }

    protected void installComponents() {
        this.iconPane = new BasicInternalFrameTitlePane(this.frame);
        this.desktopIcon.setLayout(new BorderLayout());
        this.desktopIcon.add(this.iconPane, BorderLayout.CENTER);
    }

    protected void uninstallComponents() {
        this.desktopIcon.remove(this.iconPane);
        this.desktopIcon.setLayout(null);
        this.iconPane = null;
    }

    protected void installListeners() {
        this.mouseInputListener = createMouseInputListener();
        this.desktopIcon.addMouseMotionListener(this.mouseInputListener);
        this.desktopIcon.addMouseListener(this.mouseInputListener);
    }

    protected void uninstallListeners() {
        this.desktopIcon.removeMouseMotionListener(this.mouseInputListener);
        this.desktopIcon.removeMouseListener(this.mouseInputListener);
        this.mouseInputListener = null;
    }

    protected void installDefaults() {
        LookAndFeel.installBorder(this.desktopIcon, "DesktopIcon.border");
        LookAndFeel.installProperty(this.desktopIcon, "opaque", Boolean.TRUE);
    }

    protected void uninstallDefaults() {
        LookAndFeel.uninstallBorder(this.desktopIcon);
    }

    protected MouseInputListener createMouseInputListener() {
        return new MouseInputHandler();
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        return this.desktopIcon.getLayout().preferredLayoutSize(this.desktopIcon);
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        Dimension dimension = new Dimension(this.iconPane.getMinimumSize());
        Border border = this.frame.getBorder();
        if (border != null) {
            dimension.height += border.getBorderInsets(this.frame).bottom + border.getBorderInsets(this.frame).top;
        }
        return dimension;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMaximumSize(JComponent jComponent) {
        return this.iconPane.getMaximumSize();
    }

    public Insets getInsets(JComponent jComponent) {
        JInternalFrame internalFrame = this.desktopIcon.getInternalFrame();
        Border border = internalFrame.getBorder();
        if (border != null) {
            return border.getBorderInsets(internalFrame);
        }
        return new Insets(0, 0, 0, 0);
    }

    public void deiconize() {
        try {
            this.frame.setIcon(false);
        } catch (PropertyVetoException e2) {
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicDesktopIconUI$MouseInputHandler.class */
    public class MouseInputHandler extends MouseInputAdapter {
        int _x;
        int _y;
        int __x;
        int __y;
        Rectangle startingBounds;

        public MouseInputHandler() {
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            this._x = 0;
            this._y = 0;
            this.__x = 0;
            this.__y = 0;
            this.startingBounds = null;
            JDesktopPane desktopPane = BasicDesktopIconUI.this.desktopIcon.getDesktopPane();
            if (desktopPane != null) {
                desktopPane.getDesktopManager().endDraggingFrame(BasicDesktopIconUI.this.desktopIcon);
            }
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            Point pointConvertPoint = SwingUtilities.convertPoint((Component) mouseEvent.getSource(), mouseEvent.getX(), mouseEvent.getY(), null);
            this.__x = mouseEvent.getX();
            this.__y = mouseEvent.getY();
            this._x = pointConvertPoint.f12370x;
            this._y = pointConvertPoint.f12371y;
            this.startingBounds = BasicDesktopIconUI.this.desktopIcon.getBounds();
            JDesktopPane desktopPane = BasicDesktopIconUI.this.desktopIcon.getDesktopPane();
            if (desktopPane != null) {
                desktopPane.getDesktopManager().beginDraggingFrame(BasicDesktopIconUI.this.desktopIcon);
            }
            try {
                BasicDesktopIconUI.this.frame.setSelected(true);
            } catch (PropertyVetoException e2) {
            }
            if (BasicDesktopIconUI.this.desktopIcon.getParent() instanceof JLayeredPane) {
                ((JLayeredPane) BasicDesktopIconUI.this.desktopIcon.getParent()).moveToFront(BasicDesktopIconUI.this.desktopIcon);
            }
            if (mouseEvent.getClickCount() > 1 && BasicDesktopIconUI.this.frame.isIconifiable() && BasicDesktopIconUI.this.frame.isIcon()) {
                BasicDesktopIconUI.this.deiconize();
            }
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
            Point pointConvertPoint = SwingUtilities.convertPoint((Component) mouseEvent.getSource(), mouseEvent.getX(), mouseEvent.getY(), null);
            Insets insets = BasicDesktopIconUI.this.desktopIcon.getInsets();
            int width = ((JComponent) BasicDesktopIconUI.this.desktopIcon.getParent()).getWidth();
            int height = ((JComponent) BasicDesktopIconUI.this.desktopIcon.getParent()).getHeight();
            if (this.startingBounds == null) {
                return;
            }
            int i2 = this.startingBounds.f12372x - (this._x - pointConvertPoint.f12370x);
            int i3 = this.startingBounds.f12373y - (this._y - pointConvertPoint.f12371y);
            if (i2 + insets.left <= (-this.__x)) {
                i2 = (-this.__x) - insets.left;
            }
            if (i3 + insets.top <= (-this.__y)) {
                i3 = (-this.__y) - insets.top;
            }
            if (i2 + this.__x + insets.right > width) {
                i2 = (width - this.__x) - insets.right;
            }
            if (i3 + this.__y + insets.bottom > height) {
                i3 = (height - this.__y) - insets.bottom;
            }
            JDesktopPane desktopPane = BasicDesktopIconUI.this.desktopIcon.getDesktopPane();
            if (desktopPane != null) {
                desktopPane.getDesktopManager().dragFrame(BasicDesktopIconUI.this.desktopIcon, i2, i3);
            } else {
                moveAndRepaint(BasicDesktopIconUI.this.desktopIcon, i2, i3, BasicDesktopIconUI.this.desktopIcon.getWidth(), BasicDesktopIconUI.this.desktopIcon.getHeight());
            }
        }

        public void moveAndRepaint(JComponent jComponent, int i2, int i3, int i4, int i5) {
            Rectangle bounds = jComponent.getBounds();
            jComponent.setBounds(i2, i3, i4, i5);
            SwingUtilities.computeUnion(i2, i3, i4, i5, bounds);
            jComponent.getParent().repaint(bounds.f12372x, bounds.f12373y, bounds.width, bounds.height);
        }
    }
}
