package javax.swing.plaf.basic;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BoundedRangeModel;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.LookAndFeel;
import javax.swing.ScrollPaneConstants;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.ScrollPaneUI;
import javax.swing.plaf.UIResource;
import sun.swing.DefaultLookup;
import sun.swing.UIAction;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicScrollPaneUI.class */
public class BasicScrollPaneUI extends ScrollPaneUI implements ScrollPaneConstants {
    protected JScrollPane scrollpane;
    protected ChangeListener vsbChangeListener;
    protected ChangeListener hsbChangeListener;
    protected ChangeListener viewportChangeListener;
    protected PropertyChangeListener spPropertyChangeListener;
    private MouseWheelListener mouseScrollListener;
    private PropertyChangeListener vsbPropertyChangeListener;
    private PropertyChangeListener hsbPropertyChangeListener;
    private Handler handler;
    private int oldExtent = Integer.MIN_VALUE;
    private boolean setValueCalled = false;

    public static ComponentUI createUI(JComponent jComponent) {
        return new BasicScrollPaneUI();
    }

    static void loadActionMap(LazyActionMap lazyActionMap) {
        lazyActionMap.put(new Actions("scrollUp"));
        lazyActionMap.put(new Actions("scrollDown"));
        lazyActionMap.put(new Actions("scrollHome"));
        lazyActionMap.put(new Actions("scrollEnd"));
        lazyActionMap.put(new Actions("unitScrollUp"));
        lazyActionMap.put(new Actions("unitScrollDown"));
        lazyActionMap.put(new Actions("scrollLeft"));
        lazyActionMap.put(new Actions("scrollRight"));
        lazyActionMap.put(new Actions("unitScrollRight"));
        lazyActionMap.put(new Actions("unitScrollLeft"));
    }

    @Override // javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        Border viewportBorder = this.scrollpane.getViewportBorder();
        if (viewportBorder != null) {
            Rectangle viewportBorderBounds = this.scrollpane.getViewportBorderBounds();
            viewportBorder.paintBorder(this.scrollpane, graphics, viewportBorderBounds.f12372x, viewportBorderBounds.f12373y, viewportBorderBounds.width, viewportBorderBounds.height);
        }
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMaximumSize(JComponent jComponent) {
        return new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
    }

    protected void installDefaults(JScrollPane jScrollPane) {
        LookAndFeel.installBorder(jScrollPane, "ScrollPane.border");
        LookAndFeel.installColorsAndFont(jScrollPane, "ScrollPane.background", "ScrollPane.foreground", "ScrollPane.font");
        Border viewportBorder = jScrollPane.getViewportBorder();
        if (viewportBorder == null || (viewportBorder instanceof UIResource)) {
            jScrollPane.setViewportBorder(UIManager.getBorder("ScrollPane.viewportBorder"));
        }
        LookAndFeel.installProperty(jScrollPane, "opaque", Boolean.TRUE);
    }

    protected void installListeners(JScrollPane jScrollPane) {
        this.vsbChangeListener = createVSBChangeListener();
        this.vsbPropertyChangeListener = createVSBPropertyChangeListener();
        this.hsbChangeListener = createHSBChangeListener();
        this.hsbPropertyChangeListener = createHSBPropertyChangeListener();
        this.viewportChangeListener = createViewportChangeListener();
        this.spPropertyChangeListener = createPropertyChangeListener();
        JViewport viewport = this.scrollpane.getViewport();
        JScrollBar verticalScrollBar = this.scrollpane.getVerticalScrollBar();
        JScrollBar horizontalScrollBar = this.scrollpane.getHorizontalScrollBar();
        if (viewport != null) {
            viewport.addChangeListener(this.viewportChangeListener);
        }
        if (verticalScrollBar != null) {
            verticalScrollBar.getModel().addChangeListener(this.vsbChangeListener);
            verticalScrollBar.addPropertyChangeListener(this.vsbPropertyChangeListener);
        }
        if (horizontalScrollBar != null) {
            horizontalScrollBar.getModel().addChangeListener(this.hsbChangeListener);
            horizontalScrollBar.addPropertyChangeListener(this.hsbPropertyChangeListener);
        }
        this.scrollpane.addPropertyChangeListener(this.spPropertyChangeListener);
        this.mouseScrollListener = createMouseWheelListener();
        this.scrollpane.addMouseWheelListener(this.mouseScrollListener);
    }

    protected void installKeyboardActions(JScrollPane jScrollPane) {
        SwingUtilities.replaceUIInputMap(jScrollPane, 1, getInputMap(1));
        LazyActionMap.installLazyActionMap(jScrollPane, BasicScrollPaneUI.class, "ScrollPane.actionMap");
    }

    InputMap getInputMap(int i2) {
        InputMap inputMap;
        if (i2 == 1) {
            InputMap inputMap2 = (InputMap) DefaultLookup.get(this.scrollpane, this, "ScrollPane.ancestorInputMap");
            if (this.scrollpane.getComponentOrientation().isLeftToRight() || (inputMap = (InputMap) DefaultLookup.get(this.scrollpane, this, "ScrollPane.ancestorInputMap.RightToLeft")) == null) {
                return inputMap2;
            }
            inputMap.setParent(inputMap2);
            return inputMap;
        }
        return null;
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        this.scrollpane = (JScrollPane) jComponent;
        installDefaults(this.scrollpane);
        installListeners(this.scrollpane);
        installKeyboardActions(this.scrollpane);
    }

    protected void uninstallDefaults(JScrollPane jScrollPane) {
        LookAndFeel.uninstallBorder(this.scrollpane);
        if (this.scrollpane.getViewportBorder() instanceof UIResource) {
            this.scrollpane.setViewportBorder(null);
        }
    }

    protected void uninstallListeners(JComponent jComponent) {
        JViewport viewport = this.scrollpane.getViewport();
        JScrollBar verticalScrollBar = this.scrollpane.getVerticalScrollBar();
        JScrollBar horizontalScrollBar = this.scrollpane.getHorizontalScrollBar();
        if (viewport != null) {
            viewport.removeChangeListener(this.viewportChangeListener);
        }
        if (verticalScrollBar != null) {
            verticalScrollBar.getModel().removeChangeListener(this.vsbChangeListener);
            verticalScrollBar.removePropertyChangeListener(this.vsbPropertyChangeListener);
        }
        if (horizontalScrollBar != null) {
            horizontalScrollBar.getModel().removeChangeListener(this.hsbChangeListener);
            horizontalScrollBar.removePropertyChangeListener(this.hsbPropertyChangeListener);
        }
        this.scrollpane.removePropertyChangeListener(this.spPropertyChangeListener);
        if (this.mouseScrollListener != null) {
            this.scrollpane.removeMouseWheelListener(this.mouseScrollListener);
        }
        this.vsbChangeListener = null;
        this.hsbChangeListener = null;
        this.viewportChangeListener = null;
        this.spPropertyChangeListener = null;
        this.mouseScrollListener = null;
        this.handler = null;
    }

    protected void uninstallKeyboardActions(JScrollPane jScrollPane) {
        SwingUtilities.replaceUIActionMap(jScrollPane, null);
        SwingUtilities.replaceUIInputMap(jScrollPane, 1, null);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        uninstallDefaults(this.scrollpane);
        uninstallListeners(this.scrollpane);
        uninstallKeyboardActions(this.scrollpane);
        this.scrollpane = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Handler getHandler() {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        return this.handler;
    }

    protected void syncScrollPaneWithViewport() {
        int iMax;
        JViewport viewport = this.scrollpane.getViewport();
        JScrollBar verticalScrollBar = this.scrollpane.getVerticalScrollBar();
        JScrollBar horizontalScrollBar = this.scrollpane.getHorizontalScrollBar();
        JViewport rowHeader = this.scrollpane.getRowHeader();
        JViewport columnHeader = this.scrollpane.getColumnHeader();
        boolean zIsLeftToRight = this.scrollpane.getComponentOrientation().isLeftToRight();
        if (viewport != null) {
            Dimension extentSize = viewport.getExtentSize();
            Dimension viewSize = viewport.getViewSize();
            Point viewPosition = viewport.getViewPosition();
            if (verticalScrollBar != null) {
                int i2 = extentSize.height;
                int i3 = viewSize.height;
                verticalScrollBar.setValues(Math.max(0, Math.min(viewPosition.f12371y, i3 - i2)), i2, 0, i3);
            }
            if (horizontalScrollBar != null) {
                int i4 = extentSize.width;
                int i5 = viewSize.width;
                if (zIsLeftToRight) {
                    iMax = Math.max(0, Math.min(viewPosition.f12370x, i5 - i4));
                } else {
                    int value = horizontalScrollBar.getValue();
                    if (this.setValueCalled && i5 - value == viewPosition.f12370x) {
                        iMax = Math.max(0, Math.min(i5 - i4, value));
                        if (i4 != 0) {
                            this.setValueCalled = false;
                        }
                    } else if (i4 > i5) {
                        viewPosition.f12370x = i5 - i4;
                        viewport.setViewPosition(viewPosition);
                        iMax = 0;
                    } else {
                        iMax = Math.max(0, Math.min(i5 - i4, (i5 - i4) - viewPosition.f12370x));
                        if (this.oldExtent > i4) {
                            iMax -= this.oldExtent - i4;
                        }
                    }
                }
                this.oldExtent = i4;
                horizontalScrollBar.setValues(iMax, i4, 0, i5);
            }
            if (rowHeader != null) {
                Point viewPosition2 = rowHeader.getViewPosition();
                viewPosition2.f12371y = viewport.getViewPosition().f12371y;
                viewPosition2.f12370x = 0;
                rowHeader.setViewPosition(viewPosition2);
            }
            if (columnHeader != null) {
                Point viewPosition3 = columnHeader.getViewPosition();
                if (zIsLeftToRight) {
                    viewPosition3.f12370x = viewport.getViewPosition().f12370x;
                } else {
                    viewPosition3.f12370x = Math.max(0, viewport.getViewPosition().f12370x);
                }
                viewPosition3.f12371y = 0;
                columnHeader.setViewPosition(viewPosition3);
            }
        }
    }

    @Override // javax.swing.plaf.ComponentUI
    public int getBaseline(JComponent jComponent, int i2, int i3) {
        int baseline;
        if (jComponent == null) {
            throw new NullPointerException("Component must be non-null");
        }
        if (i2 < 0 || i3 < 0) {
            throw new IllegalArgumentException("Width and height must be >= 0");
        }
        JViewport viewport = this.scrollpane.getViewport();
        Insets insets = this.scrollpane.getInsets();
        int i4 = insets.top;
        int iMax = (i3 - insets.top) - insets.bottom;
        int iMax2 = (i2 - insets.left) - insets.right;
        JViewport columnHeader = this.scrollpane.getColumnHeader();
        if (columnHeader != null && columnHeader.isVisible()) {
            Component view = columnHeader.getView();
            if (view != null && view.isVisible()) {
                Dimension preferredSize = view.getPreferredSize();
                int baseline2 = view.getBaseline(preferredSize.width, preferredSize.height);
                if (baseline2 >= 0) {
                    return i4 + baseline2;
                }
            }
            Dimension preferredSize2 = columnHeader.getPreferredSize();
            iMax -= preferredSize2.height;
            i4 += preferredSize2.height;
        }
        Component view2 = viewport == null ? null : viewport.getView();
        if (view2 != null && view2.isVisible() && view2.getBaselineResizeBehavior() == Component.BaselineResizeBehavior.CONSTANT_ASCENT) {
            Border viewportBorder = this.scrollpane.getViewportBorder();
            if (viewportBorder != null) {
                Insets borderInsets = viewportBorder.getBorderInsets(this.scrollpane);
                i4 += borderInsets.top;
                iMax = (iMax - borderInsets.top) - borderInsets.bottom;
                iMax2 = (iMax2 - borderInsets.left) - borderInsets.right;
            }
            if (view2.getWidth() > 0 && view2.getHeight() > 0) {
                Dimension minimumSize = view2.getMinimumSize();
                iMax2 = Math.max(minimumSize.width, view2.getWidth());
                iMax = Math.max(minimumSize.height, view2.getHeight());
            }
            if (iMax2 > 0 && iMax > 0 && (baseline = view2.getBaseline(iMax2, iMax)) > 0) {
                return i4 + baseline;
            }
            return -1;
        }
        return -1;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent jComponent) {
        super.getBaselineResizeBehavior(jComponent);
        return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicScrollPaneUI$ViewportChangeHandler.class */
    public class ViewportChangeHandler implements ChangeListener {
        public ViewportChangeHandler() {
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            BasicScrollPaneUI.this.getHandler().stateChanged(changeEvent);
        }
    }

    protected ChangeListener createViewportChangeListener() {
        return getHandler();
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicScrollPaneUI$HSBChangeListener.class */
    public class HSBChangeListener implements ChangeListener {
        public HSBChangeListener() {
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            BasicScrollPaneUI.this.getHandler().stateChanged(changeEvent);
        }
    }

    private PropertyChangeListener createHSBPropertyChangeListener() {
        return getHandler();
    }

    protected ChangeListener createHSBChangeListener() {
        return getHandler();
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicScrollPaneUI$VSBChangeListener.class */
    public class VSBChangeListener implements ChangeListener {
        public VSBChangeListener() {
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            BasicScrollPaneUI.this.getHandler().stateChanged(changeEvent);
        }
    }

    private PropertyChangeListener createVSBPropertyChangeListener() {
        return getHandler();
    }

    protected ChangeListener createVSBChangeListener() {
        return getHandler();
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicScrollPaneUI$MouseWheelHandler.class */
    protected class MouseWheelHandler implements MouseWheelListener {
        protected MouseWheelHandler() {
        }

        @Override // java.awt.event.MouseWheelListener
        public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
            BasicScrollPaneUI.this.getHandler().mouseWheelMoved(mouseWheelEvent);
        }
    }

    protected MouseWheelListener createMouseWheelListener() {
        return getHandler();
    }

    protected void updateScrollBarDisplayPolicy(PropertyChangeEvent propertyChangeEvent) {
        this.scrollpane.revalidate();
        this.scrollpane.repaint();
    }

    protected void updateViewport(PropertyChangeEvent propertyChangeEvent) {
        JViewport jViewport = (JViewport) propertyChangeEvent.getOldValue();
        JViewport jViewport2 = (JViewport) propertyChangeEvent.getNewValue();
        if (jViewport != null) {
            jViewport.removeChangeListener(this.viewportChangeListener);
        }
        if (jViewport2 != null) {
            Point viewPosition = jViewport2.getViewPosition();
            if (this.scrollpane.getComponentOrientation().isLeftToRight()) {
                viewPosition.f12370x = Math.max(viewPosition.f12370x, 0);
            } else {
                int i2 = jViewport2.getViewSize().width;
                int i3 = jViewport2.getExtentSize().width;
                if (i3 > i2) {
                    viewPosition.f12370x = i2 - i3;
                } else {
                    viewPosition.f12370x = Math.max(0, Math.min(i2 - i3, viewPosition.f12370x));
                }
            }
            viewPosition.f12371y = Math.max(viewPosition.f12371y, 0);
            jViewport2.setViewPosition(viewPosition);
            jViewport2.addChangeListener(this.viewportChangeListener);
        }
    }

    protected void updateRowHeader(PropertyChangeEvent propertyChangeEvent) {
        JViewport jViewport = (JViewport) propertyChangeEvent.getNewValue();
        if (jViewport != null) {
            JViewport viewport = this.scrollpane.getViewport();
            Point viewPosition = jViewport.getViewPosition();
            viewPosition.f12371y = viewport != null ? viewport.getViewPosition().f12371y : 0;
            jViewport.setViewPosition(viewPosition);
        }
    }

    protected void updateColumnHeader(PropertyChangeEvent propertyChangeEvent) {
        JViewport jViewport = (JViewport) propertyChangeEvent.getNewValue();
        if (jViewport != null) {
            JViewport viewport = this.scrollpane.getViewport();
            Point viewPosition = jViewport.getViewPosition();
            if (viewport == null) {
                viewPosition.f12370x = 0;
            } else if (this.scrollpane.getComponentOrientation().isLeftToRight()) {
                viewPosition.f12370x = viewport.getViewPosition().f12370x;
            } else {
                viewPosition.f12370x = Math.max(0, viewport.getViewPosition().f12370x);
            }
            jViewport.setViewPosition(viewPosition);
            this.scrollpane.add(jViewport, ScrollPaneConstants.COLUMN_HEADER);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateHorizontalScrollBar(PropertyChangeEvent propertyChangeEvent) {
        updateScrollBar(propertyChangeEvent, this.hsbChangeListener, this.hsbPropertyChangeListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateVerticalScrollBar(PropertyChangeEvent propertyChangeEvent) {
        updateScrollBar(propertyChangeEvent, this.vsbChangeListener, this.vsbPropertyChangeListener);
    }

    private void updateScrollBar(PropertyChangeEvent propertyChangeEvent, ChangeListener changeListener, PropertyChangeListener propertyChangeListener) {
        JScrollBar jScrollBar = (JScrollBar) propertyChangeEvent.getOldValue();
        if (jScrollBar != null) {
            if (changeListener != null) {
                jScrollBar.getModel().removeChangeListener(changeListener);
            }
            if (propertyChangeListener != null) {
                jScrollBar.removePropertyChangeListener(propertyChangeListener);
            }
        }
        JScrollBar jScrollBar2 = (JScrollBar) propertyChangeEvent.getNewValue();
        if (jScrollBar2 != null) {
            if (changeListener != null) {
                jScrollBar2.getModel().addChangeListener(changeListener);
            }
            if (propertyChangeListener != null) {
                jScrollBar2.addPropertyChangeListener(propertyChangeListener);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicScrollPaneUI$PropertyChangeHandler.class */
    public class PropertyChangeHandler implements PropertyChangeListener {
        public PropertyChangeHandler() {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            BasicScrollPaneUI.this.getHandler().propertyChange(propertyChangeEvent);
        }
    }

    protected PropertyChangeListener createPropertyChangeListener() {
        return getHandler();
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicScrollPaneUI$Actions.class */
    private static class Actions extends UIAction {
        private static final String SCROLL_UP = "scrollUp";
        private static final String SCROLL_DOWN = "scrollDown";
        private static final String SCROLL_HOME = "scrollHome";
        private static final String SCROLL_END = "scrollEnd";
        private static final String UNIT_SCROLL_UP = "unitScrollUp";
        private static final String UNIT_SCROLL_DOWN = "unitScrollDown";
        private static final String SCROLL_LEFT = "scrollLeft";
        private static final String SCROLL_RIGHT = "scrollRight";
        private static final String UNIT_SCROLL_LEFT = "unitScrollLeft";
        private static final String UNIT_SCROLL_RIGHT = "unitScrollRight";

        Actions(String str) {
            super(str);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JScrollPane jScrollPane = (JScrollPane) actionEvent.getSource();
            boolean zIsLeftToRight = jScrollPane.getComponentOrientation().isLeftToRight();
            String name = getName();
            if (name == SCROLL_UP) {
                scroll(jScrollPane, 1, -1, true);
                return;
            }
            if (name == SCROLL_DOWN) {
                scroll(jScrollPane, 1, 1, true);
                return;
            }
            if (name == SCROLL_HOME) {
                scrollHome(jScrollPane);
                return;
            }
            if (name == SCROLL_END) {
                scrollEnd(jScrollPane);
                return;
            }
            if (name == UNIT_SCROLL_UP) {
                scroll(jScrollPane, 1, -1, false);
                return;
            }
            if (name == UNIT_SCROLL_DOWN) {
                scroll(jScrollPane, 1, 1, false);
                return;
            }
            if (name == SCROLL_LEFT) {
                scroll(jScrollPane, 0, zIsLeftToRight ? -1 : 1, true);
                return;
            }
            if (name == SCROLL_RIGHT) {
                scroll(jScrollPane, 0, zIsLeftToRight ? 1 : -1, true);
            } else if (name == UNIT_SCROLL_LEFT) {
                scroll(jScrollPane, 0, zIsLeftToRight ? -1 : 1, false);
            } else if (name == UNIT_SCROLL_RIGHT) {
                scroll(jScrollPane, 0, zIsLeftToRight ? 1 : -1, false);
            }
        }

        private void scrollEnd(JScrollPane jScrollPane) {
            Component view;
            JViewport viewport = jScrollPane.getViewport();
            if (viewport != null && (view = viewport.getView()) != null) {
                Rectangle viewRect = viewport.getViewRect();
                Rectangle bounds = view.getBounds();
                if (jScrollPane.getComponentOrientation().isLeftToRight()) {
                    viewport.setViewPosition(new Point(bounds.width - viewRect.width, bounds.height - viewRect.height));
                } else {
                    viewport.setViewPosition(new Point(0, bounds.height - viewRect.height));
                }
            }
        }

        private void scrollHome(JScrollPane jScrollPane) {
            Component view;
            JViewport viewport = jScrollPane.getViewport();
            if (viewport != null && (view = viewport.getView()) != null) {
                if (jScrollPane.getComponentOrientation().isLeftToRight()) {
                    viewport.setViewPosition(new Point(0, 0));
                } else {
                    viewport.setViewPosition(new Point(view.getBounds().width - viewport.getViewRect().width, 0));
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        private void scroll(JScrollPane jScrollPane, int i2, int i3, boolean z2) {
            Component view;
            int scrollableUnitIncrement;
            JViewport viewport = jScrollPane.getViewport();
            if (viewport != null && (view = viewport.getView()) != 0) {
                Rectangle viewRect = viewport.getViewRect();
                Dimension size = view.getSize();
                if (view instanceof Scrollable) {
                    if (z2) {
                        scrollableUnitIncrement = ((Scrollable) view).getScrollableBlockIncrement(viewRect, i2, i3);
                    } else {
                        scrollableUnitIncrement = ((Scrollable) view).getScrollableUnitIncrement(viewRect, i2, i3);
                    }
                } else if (z2) {
                    if (i2 == 1) {
                        scrollableUnitIncrement = viewRect.height;
                    } else {
                        scrollableUnitIncrement = viewRect.width;
                    }
                } else {
                    scrollableUnitIncrement = 10;
                }
                if (i2 == 1) {
                    viewRect.f12373y += scrollableUnitIncrement * i3;
                    if (viewRect.f12373y + viewRect.height > size.height) {
                        viewRect.f12373y = Math.max(0, size.height - viewRect.height);
                    } else if (viewRect.f12373y < 0) {
                        viewRect.f12373y = 0;
                    }
                } else if (jScrollPane.getComponentOrientation().isLeftToRight()) {
                    viewRect.f12372x += scrollableUnitIncrement * i3;
                    if (viewRect.f12372x + viewRect.width > size.width) {
                        viewRect.f12372x = Math.max(0, size.width - viewRect.width);
                    } else if (viewRect.f12372x < 0) {
                        viewRect.f12372x = 0;
                    }
                } else {
                    viewRect.f12372x -= scrollableUnitIncrement * i3;
                    if (viewRect.width > size.width) {
                        viewRect.f12372x = size.width - viewRect.width;
                    } else {
                        viewRect.f12372x = Math.max(0, Math.min(size.width - viewRect.width, viewRect.f12372x));
                    }
                }
                viewport.setViewPosition(viewRect.getLocation());
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicScrollPaneUI$Handler.class */
    class Handler implements ChangeListener, PropertyChangeListener, MouseWheelListener {
        static final /* synthetic */ boolean $assertionsDisabled;

        Handler() {
        }

        static {
            $assertionsDisabled = !BasicScrollPaneUI.class.desiredAssertionStatus();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.awt.event.MouseWheelListener
        public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
            if (BasicScrollPaneUI.this.scrollpane.isWheelScrollingEnabled() && mouseWheelEvent.getWheelRotation() != 0) {
                JScrollBar verticalScrollBar = BasicScrollPaneUI.this.scrollpane.getVerticalScrollBar();
                int i2 = mouseWheelEvent.getWheelRotation() < 0 ? -1 : 1;
                int i3 = 1;
                if (verticalScrollBar == null || !verticalScrollBar.isVisible() || mouseWheelEvent.isShiftDown()) {
                    verticalScrollBar = BasicScrollPaneUI.this.scrollpane.getHorizontalScrollBar();
                    if (verticalScrollBar == null || !verticalScrollBar.isVisible()) {
                        return;
                    } else {
                        i3 = 0;
                    }
                }
                mouseWheelEvent.consume();
                if (mouseWheelEvent.getScrollType() == 0) {
                    JViewport viewport = BasicScrollPaneUI.this.scrollpane.getViewport();
                    if (viewport == null) {
                        return;
                    }
                    Component view = viewport.getView();
                    int iAbs = Math.abs(mouseWheelEvent.getUnitsToScroll());
                    boolean z2 = Math.abs(mouseWheelEvent.getWheelRotation()) == 1;
                    if (Boolean.TRUE == verticalScrollBar.getClientProperty("JScrollBar.fastWheelScrolling") && (view instanceof Scrollable)) {
                        Scrollable scrollable = (Scrollable) view;
                        Rectangle viewRect = viewport.getViewRect();
                        int i4 = viewRect.f12372x;
                        boolean zIsLeftToRight = view.getComponentOrientation().isLeftToRight();
                        int minimum = verticalScrollBar.getMinimum();
                        int maximum = verticalScrollBar.getMaximum() - verticalScrollBar.getModel().getExtent();
                        if (z2) {
                            int scrollableBlockIncrement = scrollable.getScrollableBlockIncrement(viewRect, i3, i2);
                            if (i2 < 0) {
                                minimum = Math.max(minimum, verticalScrollBar.getValue() - scrollableBlockIncrement);
                            } else {
                                maximum = Math.min(maximum, verticalScrollBar.getValue() + scrollableBlockIncrement);
                            }
                        }
                        int i5 = 0;
                        while (true) {
                            if (i5 >= iAbs) {
                                break;
                            }
                            int scrollableUnitIncrement = scrollable.getScrollableUnitIncrement(viewRect, i3, i2);
                            if (i3 == 1) {
                                if (i2 < 0) {
                                    viewRect.f12373y -= scrollableUnitIncrement;
                                    if (viewRect.f12373y > minimum) {
                                        i5++;
                                    } else {
                                        viewRect.f12373y = minimum;
                                        break;
                                    }
                                } else {
                                    viewRect.f12373y += scrollableUnitIncrement;
                                    if (viewRect.f12373y < maximum) {
                                        i5++;
                                    } else {
                                        viewRect.f12373y = maximum;
                                        break;
                                    }
                                }
                            } else if ((zIsLeftToRight && i2 < 0) || (!zIsLeftToRight && i2 > 0)) {
                                viewRect.f12372x -= scrollableUnitIncrement;
                                if (!zIsLeftToRight || viewRect.f12372x >= minimum) {
                                    i5++;
                                } else {
                                    viewRect.f12372x = minimum;
                                    break;
                                }
                            } else {
                                if ((zIsLeftToRight && i2 > 0) || (!zIsLeftToRight && i2 < 0)) {
                                    viewRect.f12372x += scrollableUnitIncrement;
                                    if (zIsLeftToRight && viewRect.f12372x > maximum) {
                                        viewRect.f12372x = maximum;
                                        break;
                                    }
                                } else if (!$assertionsDisabled) {
                                    throw new AssertionError((Object) "Non-sensical ComponentOrientation / scroll direction");
                                }
                                i5++;
                            }
                        }
                        if (i3 == 1) {
                            verticalScrollBar.setValue(viewRect.f12373y);
                            return;
                        }
                        if (zIsLeftToRight) {
                            verticalScrollBar.setValue(viewRect.f12372x);
                            return;
                        }
                        int value = verticalScrollBar.getValue() - (viewRect.f12372x - i4);
                        if (value < minimum) {
                            value = minimum;
                        } else if (value > maximum) {
                            value = maximum;
                        }
                        verticalScrollBar.setValue(value);
                        return;
                    }
                    BasicScrollBarUI.scrollByUnits(verticalScrollBar, i2, iAbs, z2);
                    return;
                }
                if (mouseWheelEvent.getScrollType() == 1) {
                    BasicScrollBarUI.scrollByBlock(verticalScrollBar, i2);
                }
            }
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            JViewport viewport = BasicScrollPaneUI.this.scrollpane.getViewport();
            if (viewport != null) {
                if (changeEvent.getSource() == viewport) {
                    BasicScrollPaneUI.this.syncScrollPaneWithViewport();
                    return;
                }
                JScrollBar horizontalScrollBar = BasicScrollPaneUI.this.scrollpane.getHorizontalScrollBar();
                if (horizontalScrollBar != null && changeEvent.getSource() == horizontalScrollBar.getModel()) {
                    hsbStateChanged(viewport, changeEvent);
                    return;
                }
                JScrollBar verticalScrollBar = BasicScrollPaneUI.this.scrollpane.getVerticalScrollBar();
                if (verticalScrollBar != null && changeEvent.getSource() == verticalScrollBar.getModel()) {
                    vsbStateChanged(viewport, changeEvent);
                }
            }
        }

        private void vsbStateChanged(JViewport jViewport, ChangeEvent changeEvent) {
            BoundedRangeModel boundedRangeModel = (BoundedRangeModel) changeEvent.getSource();
            Point viewPosition = jViewport.getViewPosition();
            viewPosition.f12371y = boundedRangeModel.getValue();
            jViewport.setViewPosition(viewPosition);
        }

        private void hsbStateChanged(JViewport jViewport, ChangeEvent changeEvent) {
            BoundedRangeModel boundedRangeModel = (BoundedRangeModel) changeEvent.getSource();
            Point viewPosition = jViewport.getViewPosition();
            int value = boundedRangeModel.getValue();
            if (BasicScrollPaneUI.this.scrollpane.getComponentOrientation().isLeftToRight()) {
                viewPosition.f12370x = value;
            } else {
                int i2 = jViewport.getViewSize().width;
                int i3 = jViewport.getExtentSize().width;
                int i4 = viewPosition.f12370x;
                viewPosition.f12370x = (i2 - i3) - value;
                if (i3 == 0 && value != 0 && i4 == i2) {
                    BasicScrollPaneUI.this.setValueCalled = true;
                } else if (i3 != 0 && i4 < 0 && viewPosition.f12370x == 0) {
                    viewPosition.f12370x += value;
                }
            }
            jViewport.setViewPosition(viewPosition);
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if (propertyChangeEvent.getSource() == BasicScrollPaneUI.this.scrollpane) {
                scrollPanePropertyChange(propertyChangeEvent);
            } else {
                sbPropertyChange(propertyChangeEvent);
            }
        }

        private void scrollPanePropertyChange(PropertyChangeEvent propertyChangeEvent) {
            String propertyName = propertyChangeEvent.getPropertyName();
            if (propertyName == "verticalScrollBarDisplayPolicy") {
                BasicScrollPaneUI.this.updateScrollBarDisplayPolicy(propertyChangeEvent);
                return;
            }
            if (propertyName == "horizontalScrollBarDisplayPolicy") {
                BasicScrollPaneUI.this.updateScrollBarDisplayPolicy(propertyChangeEvent);
                return;
            }
            if (propertyName == "viewport") {
                BasicScrollPaneUI.this.updateViewport(propertyChangeEvent);
                return;
            }
            if (propertyName == "rowHeader") {
                BasicScrollPaneUI.this.updateRowHeader(propertyChangeEvent);
                return;
            }
            if (propertyName == "columnHeader") {
                BasicScrollPaneUI.this.updateColumnHeader(propertyChangeEvent);
                return;
            }
            if (propertyName == "verticalScrollBar") {
                BasicScrollPaneUI.this.updateVerticalScrollBar(propertyChangeEvent);
                return;
            }
            if (propertyName == "horizontalScrollBar") {
                BasicScrollPaneUI.this.updateHorizontalScrollBar(propertyChangeEvent);
            } else if (propertyName == "componentOrientation") {
                BasicScrollPaneUI.this.scrollpane.revalidate();
                BasicScrollPaneUI.this.scrollpane.repaint();
            }
        }

        private void sbPropertyChange(PropertyChangeEvent propertyChangeEvent) {
            String propertyName = propertyChangeEvent.getPropertyName();
            Object source = propertyChangeEvent.getSource();
            if ("model" == propertyName) {
                JScrollBar verticalScrollBar = BasicScrollPaneUI.this.scrollpane.getVerticalScrollBar();
                BoundedRangeModel boundedRangeModel = (BoundedRangeModel) propertyChangeEvent.getOldValue();
                ChangeListener changeListener = null;
                if (source == verticalScrollBar) {
                    changeListener = BasicScrollPaneUI.this.vsbChangeListener;
                } else if (source == BasicScrollPaneUI.this.scrollpane.getHorizontalScrollBar()) {
                    verticalScrollBar = BasicScrollPaneUI.this.scrollpane.getHorizontalScrollBar();
                    changeListener = BasicScrollPaneUI.this.hsbChangeListener;
                }
                if (changeListener != null) {
                    if (boundedRangeModel != null) {
                        boundedRangeModel.removeChangeListener(changeListener);
                    }
                    if (verticalScrollBar.getModel() != null) {
                        verticalScrollBar.getModel().addChangeListener(changeListener);
                        return;
                    }
                    return;
                }
                return;
            }
            if ("componentOrientation" == propertyName && source == BasicScrollPaneUI.this.scrollpane.getHorizontalScrollBar()) {
                JScrollBar horizontalScrollBar = BasicScrollPaneUI.this.scrollpane.getHorizontalScrollBar();
                JViewport viewport = BasicScrollPaneUI.this.scrollpane.getViewport();
                Point viewPosition = viewport.getViewPosition();
                if (BasicScrollPaneUI.this.scrollpane.getComponentOrientation().isLeftToRight()) {
                    viewPosition.f12370x = horizontalScrollBar.getValue();
                } else {
                    viewPosition.f12370x = (viewport.getViewSize().width - viewport.getExtentSize().width) - horizontalScrollBar.getValue();
                }
                viewport.setViewPosition(viewPosition);
            }
        }
    }
}
