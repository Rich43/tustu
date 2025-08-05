package javax.swing.plaf.metal;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicRootPaneUI;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalRootPaneUI.class */
public class MetalRootPaneUI extends BasicRootPaneUI {
    private static final int CORNER_DRAG_WIDTH = 16;
    private static final int BORDER_DRAG_THICKNESS = 5;
    private Window window;
    private JComponent titlePane;
    private MouseInputListener mouseInputListener;
    private LayoutManager layoutManager;
    private LayoutManager savedOldLayout;
    private JRootPane root;
    private Cursor lastCursor = Cursor.getPredefinedCursor(0);
    private static final String[] borderKeys = {null, "RootPane.frameBorder", "RootPane.plainDialogBorder", "RootPane.informationDialogBorder", "RootPane.errorDialogBorder", "RootPane.colorChooserDialogBorder", "RootPane.fileChooserDialogBorder", "RootPane.questionDialogBorder", "RootPane.warningDialogBorder"};
    private static final int[] cursorMapping = {6, 6, 8, 7, 7, 6, 0, 0, 0, 7, 10, 0, 0, 0, 11, 4, 0, 0, 0, 5, 4, 4, 9, 5, 5};

    public static ComponentUI createUI(JComponent jComponent) {
        return new MetalRootPaneUI();
    }

    @Override // javax.swing.plaf.basic.BasicRootPaneUI, javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        super.installUI(jComponent);
        this.root = (JRootPane) jComponent;
        if (this.root.getWindowDecorationStyle() != 0) {
            installClientDecorations(this.root);
        }
    }

    @Override // javax.swing.plaf.basic.BasicRootPaneUI, javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
        uninstallClientDecorations(this.root);
        this.layoutManager = null;
        this.mouseInputListener = null;
        this.root = null;
    }

    void installBorder(JRootPane jRootPane) {
        int windowDecorationStyle = jRootPane.getWindowDecorationStyle();
        if (windowDecorationStyle == 0) {
            LookAndFeel.uninstallBorder(jRootPane);
        } else {
            LookAndFeel.installBorder(jRootPane, borderKeys[windowDecorationStyle]);
        }
    }

    private void uninstallBorder(JRootPane jRootPane) {
        LookAndFeel.uninstallBorder(jRootPane);
    }

    private void installWindowListeners(JRootPane jRootPane, Component component) {
        if (component instanceof Window) {
            this.window = (Window) component;
        } else {
            this.window = SwingUtilities.getWindowAncestor(component);
        }
        if (this.window != null) {
            if (this.mouseInputListener == null) {
                this.mouseInputListener = createWindowMouseInputListener(jRootPane);
            }
            this.window.addMouseListener(this.mouseInputListener);
            this.window.addMouseMotionListener(this.mouseInputListener);
        }
    }

    private void uninstallWindowListeners(JRootPane jRootPane) {
        if (this.window != null) {
            this.window.removeMouseListener(this.mouseInputListener);
            this.window.removeMouseMotionListener(this.mouseInputListener);
        }
    }

    private void installLayout(JRootPane jRootPane) {
        if (this.layoutManager == null) {
            this.layoutManager = createLayoutManager();
        }
        this.savedOldLayout = jRootPane.getLayout();
        jRootPane.setLayout(this.layoutManager);
    }

    private void uninstallLayout(JRootPane jRootPane) {
        if (this.savedOldLayout != null) {
            jRootPane.setLayout(this.savedOldLayout);
            this.savedOldLayout = null;
        }
    }

    private void installClientDecorations(JRootPane jRootPane) {
        installBorder(jRootPane);
        setTitlePane(jRootPane, createTitlePane(jRootPane));
        installWindowListeners(jRootPane, jRootPane.getParent());
        installLayout(jRootPane);
        if (this.window != null) {
            jRootPane.revalidate();
            jRootPane.repaint();
        }
    }

    private void uninstallClientDecorations(JRootPane jRootPane) {
        uninstallBorder(jRootPane);
        uninstallWindowListeners(jRootPane);
        setTitlePane(jRootPane, null);
        uninstallLayout(jRootPane);
        if (jRootPane.getWindowDecorationStyle() == 0) {
            jRootPane.repaint();
            jRootPane.revalidate();
        }
        if (this.window != null) {
            this.window.setCursor(Cursor.getPredefinedCursor(0));
        }
        this.window = null;
    }

    private JComponent createTitlePane(JRootPane jRootPane) {
        return new MetalTitlePane(jRootPane, this);
    }

    private MouseInputListener createWindowMouseInputListener(JRootPane jRootPane) {
        return new MouseInputHandler();
    }

    private LayoutManager createLayoutManager() {
        return new MetalRootLayout();
    }

    private void setTitlePane(JRootPane jRootPane, JComponent jComponent) {
        JLayeredPane layeredPane = jRootPane.getLayeredPane();
        JComponent titlePane = getTitlePane();
        if (titlePane != null) {
            titlePane.setVisible(false);
            layeredPane.remove(titlePane);
        }
        if (jComponent != null) {
            layeredPane.add(jComponent, JLayeredPane.FRAME_CONTENT_LAYER);
            jComponent.setVisible(true);
        }
        this.titlePane = jComponent;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public JComponent getTitlePane() {
        return this.titlePane;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public JRootPane getRootPane() {
        return this.root;
    }

    @Override // javax.swing.plaf.basic.BasicRootPaneUI, java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        super.propertyChange(propertyChangeEvent);
        String propertyName = propertyChangeEvent.getPropertyName();
        if (propertyName == null) {
            return;
        }
        if (propertyName.equals("windowDecorationStyle")) {
            JRootPane jRootPane = (JRootPane) propertyChangeEvent.getSource();
            int windowDecorationStyle = jRootPane.getWindowDecorationStyle();
            uninstallClientDecorations(jRootPane);
            if (windowDecorationStyle != 0) {
                installClientDecorations(jRootPane);
                return;
            }
            return;
        }
        if (propertyName.equals("ancestor")) {
            uninstallWindowListeners(this.root);
            if (((JRootPane) propertyChangeEvent.getSource()).getWindowDecorationStyle() != 0) {
                installWindowListeners(this.root, this.root.getParent());
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalRootPaneUI$MetalRootLayout.class */
    private static class MetalRootLayout implements LayoutManager2 {
        private MetalRootLayout() {
        }

        @Override // java.awt.LayoutManager
        public Dimension preferredLayoutSize(Container container) {
            Dimension size;
            JComponent titlePane;
            Dimension preferredSize;
            Dimension preferredSize2;
            int i2 = 0;
            int i3 = 0;
            int i4 = 0;
            int i5 = 0;
            int i6 = 0;
            Insets insets = container.getInsets();
            JRootPane jRootPane = (JRootPane) container;
            if (jRootPane.getContentPane() != null) {
                size = jRootPane.getContentPane().getPreferredSize();
            } else {
                size = jRootPane.getSize();
            }
            if (size != null) {
                i2 = size.width;
                i3 = size.height;
            }
            if (jRootPane.getMenuBar() != null && (preferredSize2 = jRootPane.getMenuBar().getPreferredSize()) != null) {
                i4 = preferredSize2.width;
                i5 = preferredSize2.height;
            }
            if (jRootPane.getWindowDecorationStyle() != 0 && (jRootPane.getUI() instanceof MetalRootPaneUI) && (titlePane = ((MetalRootPaneUI) jRootPane.getUI()).getTitlePane()) != null && (preferredSize = titlePane.getPreferredSize()) != null) {
                i6 = preferredSize.width;
                int i7 = preferredSize.height;
            }
            return new Dimension(Math.max(Math.max(i2, i4), i6) + insets.left + insets.right, i3 + i5 + i6 + insets.top + insets.bottom);
        }

        @Override // java.awt.LayoutManager
        public Dimension minimumLayoutSize(Container container) {
            Dimension size;
            JComponent titlePane;
            Dimension minimumSize;
            Dimension minimumSize2;
            int i2 = 0;
            int i3 = 0;
            int i4 = 0;
            int i5 = 0;
            int i6 = 0;
            Insets insets = container.getInsets();
            JRootPane jRootPane = (JRootPane) container;
            if (jRootPane.getContentPane() != null) {
                size = jRootPane.getContentPane().getMinimumSize();
            } else {
                size = jRootPane.getSize();
            }
            if (size != null) {
                i2 = size.width;
                i3 = size.height;
            }
            if (jRootPane.getMenuBar() != null && (minimumSize2 = jRootPane.getMenuBar().getMinimumSize()) != null) {
                i4 = minimumSize2.width;
                i5 = minimumSize2.height;
            }
            if (jRootPane.getWindowDecorationStyle() != 0 && (jRootPane.getUI() instanceof MetalRootPaneUI) && (titlePane = ((MetalRootPaneUI) jRootPane.getUI()).getTitlePane()) != null && (minimumSize = titlePane.getMinimumSize()) != null) {
                i6 = minimumSize.width;
                int i7 = minimumSize.height;
            }
            return new Dimension(Math.max(Math.max(i2, i4), i6) + insets.left + insets.right, i3 + i5 + i6 + insets.top + insets.bottom);
        }

        @Override // java.awt.LayoutManager2
        public Dimension maximumLayoutSize(Container container) {
            JComponent titlePane;
            Dimension maximumSize;
            Dimension maximumSize2;
            Dimension maximumSize3;
            int i2 = Integer.MAX_VALUE;
            int i3 = Integer.MAX_VALUE;
            int i4 = Integer.MAX_VALUE;
            int i5 = Integer.MAX_VALUE;
            int i6 = Integer.MAX_VALUE;
            int i7 = Integer.MAX_VALUE;
            Insets insets = container.getInsets();
            JRootPane jRootPane = (JRootPane) container;
            if (jRootPane.getContentPane() != null && (maximumSize3 = jRootPane.getContentPane().getMaximumSize()) != null) {
                i2 = maximumSize3.width;
                i3 = maximumSize3.height;
            }
            if (jRootPane.getMenuBar() != null && (maximumSize2 = jRootPane.getMenuBar().getMaximumSize()) != null) {
                i4 = maximumSize2.width;
                i5 = maximumSize2.height;
            }
            if (jRootPane.getWindowDecorationStyle() != 0 && (jRootPane.getUI() instanceof MetalRootPaneUI) && (titlePane = ((MetalRootPaneUI) jRootPane.getUI()).getTitlePane()) != null && (maximumSize = titlePane.getMaximumSize()) != null) {
                i6 = maximumSize.width;
                i7 = maximumSize.height;
            }
            int iMax = Math.max(Math.max(i3, i5), i7);
            if (iMax != Integer.MAX_VALUE) {
                iMax = i3 + i5 + i7 + insets.top + insets.bottom;
            }
            int iMax2 = Math.max(Math.max(i2, i4), i6);
            if (iMax2 != Integer.MAX_VALUE) {
                iMax2 += insets.left + insets.right;
            }
            return new Dimension(iMax2, iMax);
        }

        @Override // java.awt.LayoutManager
        public void layoutContainer(Container container) {
            JComponent titlePane;
            Dimension preferredSize;
            JRootPane jRootPane = (JRootPane) container;
            Rectangle bounds = jRootPane.getBounds();
            Insets insets = jRootPane.getInsets();
            int i2 = 0;
            int i3 = (bounds.width - insets.right) - insets.left;
            int i4 = (bounds.height - insets.top) - insets.bottom;
            if (jRootPane.getLayeredPane() != null) {
                jRootPane.getLayeredPane().setBounds(insets.left, insets.top, i3, i4);
            }
            if (jRootPane.getGlassPane() != null) {
                jRootPane.getGlassPane().setBounds(insets.left, insets.top, i3, i4);
            }
            if (jRootPane.getWindowDecorationStyle() != 0 && (jRootPane.getUI() instanceof MetalRootPaneUI) && (titlePane = ((MetalRootPaneUI) jRootPane.getUI()).getTitlePane()) != null && (preferredSize = titlePane.getPreferredSize()) != null) {
                int i5 = preferredSize.height;
                titlePane.setBounds(0, 0, i3, i5);
                i2 = 0 + i5;
            }
            if (jRootPane.getMenuBar() != null) {
                Dimension preferredSize2 = jRootPane.getMenuBar().getPreferredSize();
                jRootPane.getMenuBar().setBounds(0, i2, i3, preferredSize2.height);
                i2 += preferredSize2.height;
            }
            if (jRootPane.getContentPane() != null) {
                jRootPane.getContentPane().getPreferredSize();
                jRootPane.getContentPane().setBounds(0, i2, i3, i4 < i2 ? 0 : i4 - i2);
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

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalRootPaneUI$MouseInputHandler.class */
    private class MouseInputHandler implements MouseInputListener {
        private boolean isMovingWindow;
        private int dragCursor;
        private int dragOffsetX;
        private int dragOffsetY;
        private int dragWidth;
        private int dragHeight;

        private MouseInputHandler() {
        }

        @Override // java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            if (MetalRootPaneUI.this.getRootPane().getWindowDecorationStyle() == 0) {
                return;
            }
            Point point = mouseEvent.getPoint();
            Window window = (Window) mouseEvent.getSource();
            if (window != null) {
                window.toFront();
            }
            Point pointConvertPoint = SwingUtilities.convertPoint(window, point, MetalRootPaneUI.this.getTitlePane());
            Frame frame = null;
            Dialog dialog = null;
            if (window instanceof Frame) {
                frame = (Frame) window;
            } else if (window instanceof Dialog) {
                dialog = (Dialog) window;
            }
            int extendedState = frame != null ? frame.getExtendedState() : 0;
            if (MetalRootPaneUI.this.getTitlePane() != null && MetalRootPaneUI.this.getTitlePane().contains(pointConvertPoint)) {
                if (((frame != null && (extendedState & 6) == 0) || dialog != null) && point.f12371y >= 5 && point.f12370x >= 5 && point.f12370x < window.getWidth() - 5) {
                    this.isMovingWindow = true;
                    this.dragOffsetX = point.f12370x;
                    this.dragOffsetY = point.f12371y;
                    return;
                }
                return;
            }
            if ((frame != null && frame.isResizable() && (extendedState & 6) == 0) || (dialog != null && dialog.isResizable())) {
                this.dragOffsetX = point.f12370x;
                this.dragOffsetY = point.f12371y;
                this.dragWidth = window.getWidth();
                this.dragHeight = window.getHeight();
                this.dragCursor = getCursor(calculateCorner(window, point.f12370x, point.f12371y));
            }
        }

        @Override // java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            if (this.dragCursor != 0 && MetalRootPaneUI.this.window != null && !MetalRootPaneUI.this.window.isValid()) {
                MetalRootPaneUI.this.window.validate();
                MetalRootPaneUI.this.getRootPane().repaint();
            }
            this.isMovingWindow = false;
            this.dragCursor = 0;
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
            if (MetalRootPaneUI.this.getRootPane().getWindowDecorationStyle() == 0) {
                return;
            }
            Window window = (Window) mouseEvent.getSource();
            Frame frame = null;
            Dialog dialog = null;
            if (window instanceof Frame) {
                frame = (Frame) window;
            } else if (window instanceof Dialog) {
                dialog = (Dialog) window;
            }
            int cursor = getCursor(calculateCorner(window, mouseEvent.getX(), mouseEvent.getY()));
            if (cursor != 0 && ((frame != null && frame.isResizable() && (frame.getExtendedState() & 6) == 0) || (dialog != null && dialog.isResizable()))) {
                window.setCursor(Cursor.getPredefinedCursor(cursor));
            } else {
                window.setCursor(MetalRootPaneUI.this.lastCursor);
            }
        }

        private void adjust(Rectangle rectangle, Dimension dimension, int i2, int i3, int i4, int i5) {
            rectangle.f12372x += i2;
            rectangle.f12373y += i3;
            rectangle.width += i4;
            rectangle.height += i5;
            if (dimension != null) {
                if (rectangle.width < dimension.width) {
                    int i6 = dimension.width - rectangle.width;
                    if (i2 != 0) {
                        rectangle.f12372x -= i6;
                    }
                    rectangle.width = dimension.width;
                }
                if (rectangle.height < dimension.height) {
                    int i7 = dimension.height - rectangle.height;
                    if (i3 != 0) {
                        rectangle.f12373y -= i7;
                    }
                    rectangle.height = dimension.height;
                }
            }
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
            Window window = (Window) mouseEvent.getSource();
            Point point = mouseEvent.getPoint();
            if (this.isMovingWindow) {
                Point locationOnScreen = mouseEvent.getLocationOnScreen();
                window.setLocation(locationOnScreen.f12370x - this.dragOffsetX, locationOnScreen.f12371y - this.dragOffsetY);
                return;
            }
            if (this.dragCursor != 0) {
                Rectangle bounds = window.getBounds();
                Rectangle rectangle = new Rectangle(bounds);
                Dimension minimumSize = window.getMinimumSize();
                switch (this.dragCursor) {
                    case 4:
                        adjust(bounds, minimumSize, point.f12370x - this.dragOffsetX, 0, -(point.f12370x - this.dragOffsetX), (point.f12371y + (this.dragHeight - this.dragOffsetY)) - bounds.height);
                        break;
                    case 5:
                        adjust(bounds, minimumSize, 0, 0, (point.f12370x + (this.dragWidth - this.dragOffsetX)) - bounds.width, (point.f12371y + (this.dragHeight - this.dragOffsetY)) - bounds.height);
                        break;
                    case 6:
                        adjust(bounds, minimumSize, point.f12370x - this.dragOffsetX, point.f12371y - this.dragOffsetY, -(point.f12370x - this.dragOffsetX), -(point.f12371y - this.dragOffsetY));
                        break;
                    case 7:
                        adjust(bounds, minimumSize, 0, point.f12371y - this.dragOffsetY, (point.f12370x + (this.dragWidth - this.dragOffsetX)) - bounds.width, -(point.f12371y - this.dragOffsetY));
                        break;
                    case 8:
                        adjust(bounds, minimumSize, 0, point.f12371y - this.dragOffsetY, 0, -(point.f12371y - this.dragOffsetY));
                        break;
                    case 9:
                        adjust(bounds, minimumSize, 0, 0, 0, (point.f12371y + (this.dragHeight - this.dragOffsetY)) - bounds.height);
                        break;
                    case 10:
                        adjust(bounds, minimumSize, point.f12370x - this.dragOffsetX, 0, -(point.f12370x - this.dragOffsetX), 0);
                        break;
                    case 11:
                        adjust(bounds, minimumSize, 0, 0, (point.f12370x + (this.dragWidth - this.dragOffsetX)) - bounds.width, 0);
                        break;
                }
                if (!bounds.equals(rectangle)) {
                    window.setBounds(bounds);
                    if (Toolkit.getDefaultToolkit().isDynamicLayoutActive()) {
                        window.validate();
                        MetalRootPaneUI.this.getRootPane().repaint();
                    }
                }
            }
        }

        @Override // java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
            Window window = (Window) mouseEvent.getSource();
            MetalRootPaneUI.this.lastCursor = window.getCursor();
            mouseMoved(mouseEvent);
        }

        @Override // java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
            ((Window) mouseEvent.getSource()).setCursor(MetalRootPaneUI.this.lastCursor);
        }

        @Override // java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
            Window window = (Window) mouseEvent.getSource();
            if (window instanceof Frame) {
                Frame frame = (Frame) window;
                Point pointConvertPoint = SwingUtilities.convertPoint(window, mouseEvent.getPoint(), MetalRootPaneUI.this.getTitlePane());
                int extendedState = frame.getExtendedState();
                if (MetalRootPaneUI.this.getTitlePane() != null && MetalRootPaneUI.this.getTitlePane().contains(pointConvertPoint) && mouseEvent.getClickCount() % 2 == 0 && (mouseEvent.getModifiers() & 16) != 0 && frame.isResizable()) {
                    if ((extendedState & 6) != 0) {
                        frame.setExtendedState(extendedState & (-7));
                    } else {
                        frame.setExtendedState(extendedState | 6);
                    }
                }
            }
        }

        private int calculateCorner(Window window, int i2, int i3) {
            Insets insets = window.getInsets();
            int iCalculatePosition = calculatePosition(i2 - insets.left, (window.getWidth() - insets.left) - insets.right);
            int iCalculatePosition2 = calculatePosition(i3 - insets.top, (window.getHeight() - insets.top) - insets.bottom);
            if (iCalculatePosition == -1 || iCalculatePosition2 == -1) {
                return -1;
            }
            return (iCalculatePosition2 * 5) + iCalculatePosition;
        }

        private int getCursor(int i2) {
            if (i2 != -1) {
                return MetalRootPaneUI.cursorMapping[i2];
            }
            return 0;
        }

        private int calculatePosition(int i2, int i3) {
            if (i2 < 5) {
                return 0;
            }
            if (i2 < 16) {
                return 1;
            }
            if (i2 >= i3 - 5) {
                return 4;
            }
            if (i2 >= i3 - 16) {
                return 3;
            }
            return 2;
        }
    }
}
