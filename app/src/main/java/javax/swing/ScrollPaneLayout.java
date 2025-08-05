package javax.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.io.Serializable;
import javax.swing.border.Border;

/* loaded from: rt.jar:javax/swing/ScrollPaneLayout.class */
public class ScrollPaneLayout implements LayoutManager, ScrollPaneConstants, Serializable {
    protected JViewport viewport;
    protected JScrollBar vsb;
    protected JScrollBar hsb;
    protected JViewport rowHead;
    protected JViewport colHead;
    protected Component lowerLeft;
    protected Component lowerRight;
    protected Component upperLeft;
    protected Component upperRight;
    protected int vsbPolicy = 20;
    protected int hsbPolicy = 30;

    /* loaded from: rt.jar:javax/swing/ScrollPaneLayout$UIResource.class */
    public static class UIResource extends ScrollPaneLayout implements javax.swing.plaf.UIResource {
    }

    public void syncWithScrollPane(JScrollPane jScrollPane) {
        this.viewport = jScrollPane.getViewport();
        this.vsb = jScrollPane.getVerticalScrollBar();
        this.hsb = jScrollPane.getHorizontalScrollBar();
        this.rowHead = jScrollPane.getRowHeader();
        this.colHead = jScrollPane.getColumnHeader();
        this.lowerLeft = jScrollPane.getCorner(ScrollPaneConstants.LOWER_LEFT_CORNER);
        this.lowerRight = jScrollPane.getCorner(ScrollPaneConstants.LOWER_RIGHT_CORNER);
        this.upperLeft = jScrollPane.getCorner(ScrollPaneConstants.UPPER_LEFT_CORNER);
        this.upperRight = jScrollPane.getCorner(ScrollPaneConstants.UPPER_RIGHT_CORNER);
        this.vsbPolicy = jScrollPane.getVerticalScrollBarPolicy();
        this.hsbPolicy = jScrollPane.getHorizontalScrollBarPolicy();
    }

    protected Component addSingletonComponent(Component component, Component component2) {
        if (component != null && component != component2) {
            component.getParent().remove(component);
        }
        return component2;
    }

    @Override // java.awt.LayoutManager
    public void addLayoutComponent(String str, Component component) {
        if (str.equals(ScrollPaneConstants.VIEWPORT)) {
            this.viewport = (JViewport) addSingletonComponent(this.viewport, component);
            return;
        }
        if (str.equals(ScrollPaneConstants.VERTICAL_SCROLLBAR)) {
            this.vsb = (JScrollBar) addSingletonComponent(this.vsb, component);
            return;
        }
        if (str.equals(ScrollPaneConstants.HORIZONTAL_SCROLLBAR)) {
            this.hsb = (JScrollBar) addSingletonComponent(this.hsb, component);
            return;
        }
        if (str.equals(ScrollPaneConstants.ROW_HEADER)) {
            this.rowHead = (JViewport) addSingletonComponent(this.rowHead, component);
            return;
        }
        if (str.equals(ScrollPaneConstants.COLUMN_HEADER)) {
            this.colHead = (JViewport) addSingletonComponent(this.colHead, component);
            return;
        }
        if (str.equals(ScrollPaneConstants.LOWER_LEFT_CORNER)) {
            this.lowerLeft = addSingletonComponent(this.lowerLeft, component);
            return;
        }
        if (str.equals(ScrollPaneConstants.LOWER_RIGHT_CORNER)) {
            this.lowerRight = addSingletonComponent(this.lowerRight, component);
        } else if (str.equals(ScrollPaneConstants.UPPER_LEFT_CORNER)) {
            this.upperLeft = addSingletonComponent(this.upperLeft, component);
        } else {
            if (str.equals(ScrollPaneConstants.UPPER_RIGHT_CORNER)) {
                this.upperRight = addSingletonComponent(this.upperRight, component);
                return;
            }
            throw new IllegalArgumentException("invalid layout key " + str);
        }
    }

    @Override // java.awt.LayoutManager
    public void removeLayoutComponent(Component component) {
        if (component == this.viewport) {
            this.viewport = null;
            return;
        }
        if (component == this.vsb) {
            this.vsb = null;
            return;
        }
        if (component == this.hsb) {
            this.hsb = null;
            return;
        }
        if (component == this.rowHead) {
            this.rowHead = null;
            return;
        }
        if (component == this.colHead) {
            this.colHead = null;
            return;
        }
        if (component == this.lowerLeft) {
            this.lowerLeft = null;
            return;
        }
        if (component == this.lowerRight) {
            this.lowerRight = null;
        } else if (component == this.upperLeft) {
            this.upperLeft = null;
        } else if (component == this.upperRight) {
            this.upperRight = null;
        }
    }

    public int getVerticalScrollBarPolicy() {
        return this.vsbPolicy;
    }

    public void setVerticalScrollBarPolicy(int i2) {
        switch (i2) {
            case 20:
            case 21:
            case 22:
                this.vsbPolicy = i2;
                return;
            default:
                throw new IllegalArgumentException("invalid verticalScrollBarPolicy");
        }
    }

    public int getHorizontalScrollBarPolicy() {
        return this.hsbPolicy;
    }

    public void setHorizontalScrollBarPolicy(int i2) {
        switch (i2) {
            case 30:
            case 31:
            case 32:
                this.hsbPolicy = i2;
                return;
            default:
                throw new IllegalArgumentException("invalid horizontalScrollBarPolicy");
        }
    }

    public JViewport getViewport() {
        return this.viewport;
    }

    public JScrollBar getHorizontalScrollBar() {
        return this.hsb;
    }

    public JScrollBar getVerticalScrollBar() {
        return this.vsb;
    }

    public JViewport getRowHeader() {
        return this.rowHead;
    }

    public JViewport getColumnHeader() {
        return this.colHead;
    }

    public Component getCorner(String str) {
        if (str.equals(ScrollPaneConstants.LOWER_LEFT_CORNER)) {
            return this.lowerLeft;
        }
        if (str.equals(ScrollPaneConstants.LOWER_RIGHT_CORNER)) {
            return this.lowerRight;
        }
        if (str.equals(ScrollPaneConstants.UPPER_LEFT_CORNER)) {
            return this.upperLeft;
        }
        if (str.equals(ScrollPaneConstants.UPPER_RIGHT_CORNER)) {
            return this.upperRight;
        }
        return null;
    }

    @Override // java.awt.LayoutManager
    public Dimension preferredLayoutSize(Container container) {
        JScrollPane jScrollPane = (JScrollPane) container;
        this.vsbPolicy = jScrollPane.getVerticalScrollBarPolicy();
        this.hsbPolicy = jScrollPane.getHorizontalScrollBarPolicy();
        Insets insets = container.getInsets();
        int i2 = insets.left + insets.right;
        int i3 = insets.top + insets.bottom;
        Dimension preferredSize = null;
        Dimension dimension = null;
        Component view = null;
        if (this.viewport != null) {
            preferredSize = this.viewport.getPreferredSize();
            view = this.viewport.getView();
            if (view != null) {
                dimension = view.getPreferredSize();
            } else {
                dimension = new Dimension(0, 0);
            }
        }
        if (preferredSize != null) {
            i2 += preferredSize.width;
            i3 += preferredSize.height;
        }
        Border viewportBorder = jScrollPane.getViewportBorder();
        if (viewportBorder != null) {
            Insets borderInsets = viewportBorder.getBorderInsets(container);
            i2 += borderInsets.left + borderInsets.right;
            i3 += borderInsets.top + borderInsets.bottom;
        }
        if (this.rowHead != null && this.rowHead.isVisible()) {
            i2 += this.rowHead.getPreferredSize().width;
        }
        if (this.colHead != null && this.colHead.isVisible()) {
            i3 += this.colHead.getPreferredSize().height;
        }
        if (this.vsb != null && this.vsbPolicy != 21) {
            if (this.vsbPolicy == 22) {
                i2 += this.vsb.getPreferredSize().width;
            } else if (dimension != null && preferredSize != null) {
                boolean z2 = true;
                if (view instanceof Scrollable) {
                    z2 = !((Scrollable) view).getScrollableTracksViewportHeight();
                }
                if (z2 && dimension.height > preferredSize.height) {
                    i2 += this.vsb.getPreferredSize().width;
                }
            }
        }
        if (this.hsb != null && this.hsbPolicy != 31) {
            if (this.hsbPolicy == 32) {
                i3 += this.hsb.getPreferredSize().height;
            } else if (dimension != null && preferredSize != null) {
                boolean z3 = true;
                if (view instanceof Scrollable) {
                    z3 = !((Scrollable) view).getScrollableTracksViewportWidth();
                }
                if (z3 && dimension.width > preferredSize.width) {
                    i3 += this.hsb.getPreferredSize().height;
                }
            }
        }
        return new Dimension(i2, i3);
    }

    @Override // java.awt.LayoutManager
    public Dimension minimumLayoutSize(Container container) {
        JScrollPane jScrollPane = (JScrollPane) container;
        this.vsbPolicy = jScrollPane.getVerticalScrollBarPolicy();
        this.hsbPolicy = jScrollPane.getHorizontalScrollBarPolicy();
        Insets insets = container.getInsets();
        int iMax = insets.left + insets.right;
        int iMax2 = insets.top + insets.bottom;
        if (this.viewport != null) {
            Dimension minimumSize = this.viewport.getMinimumSize();
            iMax += minimumSize.width;
            iMax2 += minimumSize.height;
        }
        Border viewportBorder = jScrollPane.getViewportBorder();
        if (viewportBorder != null) {
            Insets borderInsets = viewportBorder.getBorderInsets(container);
            iMax += borderInsets.left + borderInsets.right;
            iMax2 += borderInsets.top + borderInsets.bottom;
        }
        if (this.rowHead != null && this.rowHead.isVisible()) {
            Dimension minimumSize2 = this.rowHead.getMinimumSize();
            iMax += minimumSize2.width;
            iMax2 = Math.max(iMax2, minimumSize2.height);
        }
        if (this.colHead != null && this.colHead.isVisible()) {
            Dimension minimumSize3 = this.colHead.getMinimumSize();
            iMax = Math.max(iMax, minimumSize3.width);
            iMax2 += minimumSize3.height;
        }
        if (this.vsb != null && this.vsbPolicy != 21) {
            Dimension minimumSize4 = this.vsb.getMinimumSize();
            iMax += minimumSize4.width;
            iMax2 = Math.max(iMax2, minimumSize4.height);
        }
        if (this.hsb != null && this.hsbPolicy != 31) {
            Dimension minimumSize5 = this.hsb.getMinimumSize();
            iMax = Math.max(iMax, minimumSize5.width);
            iMax2 += minimumSize5.height;
        }
        return new Dimension(iMax, iMax2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.awt.LayoutManager
    public void layoutContainer(Container container) {
        Insets insets;
        Scrollable scrollable;
        boolean z2;
        boolean z3;
        JScrollPane jScrollPane = (JScrollPane) container;
        this.vsbPolicy = jScrollPane.getVerticalScrollBarPolicy();
        this.hsbPolicy = jScrollPane.getHorizontalScrollBarPolicy();
        Rectangle bounds = jScrollPane.getBounds();
        bounds.f12373y = 0;
        bounds.f12372x = 0;
        Insets insets2 = container.getInsets();
        bounds.f12372x = insets2.left;
        bounds.f12373y = insets2.top;
        bounds.width -= insets2.left + insets2.right;
        bounds.height -= insets2.top + insets2.bottom;
        boolean zIsLeftToRight = SwingUtilities.isLeftToRight(jScrollPane);
        Rectangle rectangle = new Rectangle(0, bounds.f12373y, 0, 0);
        if (this.colHead != null && this.colHead.isVisible()) {
            int iMin = Math.min(bounds.height, this.colHead.getPreferredSize().height);
            rectangle.height = iMin;
            bounds.f12373y += iMin;
            bounds.height -= iMin;
        }
        Rectangle rectangle2 = new Rectangle(0, 0, 0, 0);
        if (this.rowHead != null && this.rowHead.isVisible()) {
            int iMin2 = Math.min(bounds.width, this.rowHead.getPreferredSize().width);
            rectangle2.width = iMin2;
            bounds.width -= iMin2;
            if (zIsLeftToRight) {
                rectangle2.f12372x = bounds.f12372x;
                bounds.f12372x += iMin2;
            } else {
                rectangle2.f12372x = bounds.f12372x + bounds.width;
            }
        }
        Border viewportBorder = jScrollPane.getViewportBorder();
        if (viewportBorder != null) {
            insets = viewportBorder.getBorderInsets(container);
            bounds.f12372x += insets.left;
            bounds.f12373y += insets.top;
            bounds.width -= insets.left + insets.right;
            bounds.height -= insets.top + insets.bottom;
        } else {
            insets = new Insets(0, 0, 0, 0);
        }
        Component view = this.viewport != null ? this.viewport.getView() : null;
        Dimension preferredSize = view != 0 ? view.getPreferredSize() : new Dimension(0, 0);
        Dimension viewCoordinates = this.viewport != null ? this.viewport.toViewCoordinates(bounds.getSize()) : new Dimension(0, 0);
        boolean scrollableTracksViewportWidth = false;
        boolean scrollableTracksViewportHeight = false;
        boolean z4 = bounds.width < 0 || bounds.height < 0;
        if (!z4 && (view instanceof Scrollable)) {
            scrollable = (Scrollable) view;
            scrollableTracksViewportWidth = scrollable.getScrollableTracksViewportWidth();
            scrollableTracksViewportHeight = scrollable.getScrollableTracksViewportHeight();
        } else {
            scrollable = null;
        }
        Rectangle rectangle3 = new Rectangle(0, bounds.f12373y - insets.top, 0, 0);
        if (z4) {
            z2 = false;
        } else if (this.vsbPolicy == 22) {
            z2 = true;
        } else if (this.vsbPolicy == 21) {
            z2 = false;
        } else {
            z2 = !scrollableTracksViewportHeight && preferredSize.height > viewCoordinates.height;
        }
        if (this.vsb != null && z2) {
            adjustForVSB(true, bounds, rectangle3, insets, zIsLeftToRight);
            viewCoordinates = this.viewport.toViewCoordinates(bounds.getSize());
        }
        Rectangle rectangle4 = new Rectangle(bounds.f12372x - insets.left, 0, 0, 0);
        if (z4) {
            z3 = false;
        } else if (this.hsbPolicy == 32) {
            z3 = true;
        } else if (this.hsbPolicy == 31) {
            z3 = false;
        } else {
            z3 = !scrollableTracksViewportWidth && preferredSize.width > viewCoordinates.width;
        }
        if (this.hsb != null && z3) {
            adjustForHSB(true, bounds, rectangle4, insets);
            if (this.vsb != null && !z2 && this.vsbPolicy != 21) {
                z2 = preferredSize.height > this.viewport.toViewCoordinates(bounds.getSize()).height;
                if (z2) {
                    adjustForVSB(true, bounds, rectangle3, insets, zIsLeftToRight);
                }
            }
        }
        if (this.viewport != null) {
            this.viewport.setBounds(bounds);
            if (scrollable != null) {
                Dimension viewCoordinates2 = this.viewport.toViewCoordinates(bounds.getSize());
                boolean z5 = z3;
                boolean z6 = z2;
                boolean scrollableTracksViewportWidth2 = scrollable.getScrollableTracksViewportWidth();
                boolean scrollableTracksViewportHeight2 = scrollable.getScrollableTracksViewportHeight();
                if (this.vsb != null && this.vsbPolicy == 20) {
                    boolean z7 = !scrollableTracksViewportHeight2 && preferredSize.height > viewCoordinates2.height;
                    if (z7 != z2) {
                        z2 = z7;
                        adjustForVSB(z2, bounds, rectangle3, insets, zIsLeftToRight);
                        viewCoordinates2 = this.viewport.toViewCoordinates(bounds.getSize());
                    }
                }
                if (this.hsb != null && this.hsbPolicy == 30) {
                    boolean z8 = !scrollableTracksViewportWidth2 && preferredSize.width > viewCoordinates2.width;
                    if (z8 != z3) {
                        z3 = z8;
                        adjustForHSB(z3, bounds, rectangle4, insets);
                        if (this.vsb != null && !z2 && this.vsbPolicy != 21) {
                            z2 = preferredSize.height > this.viewport.toViewCoordinates(bounds.getSize()).height;
                            if (z2) {
                                adjustForVSB(true, bounds, rectangle3, insets, zIsLeftToRight);
                            }
                        }
                    }
                }
                if (z5 != z3 || z6 != z2) {
                    this.viewport.setBounds(bounds);
                }
            }
        }
        rectangle3.height = bounds.height + insets.top + insets.bottom;
        rectangle4.width = bounds.width + insets.left + insets.right;
        rectangle2.height = bounds.height + insets.top + insets.bottom;
        rectangle2.f12373y = bounds.f12373y - insets.top;
        rectangle.width = bounds.width + insets.left + insets.right;
        rectangle.f12372x = bounds.f12372x - insets.left;
        if (this.rowHead != null) {
            this.rowHead.setBounds(rectangle2);
        }
        if (this.colHead != null) {
            this.colHead.setBounds(rectangle);
        }
        if (this.vsb != null) {
            if (z2) {
                if (this.colHead != null && UIManager.getBoolean("ScrollPane.fillUpperCorner") && ((zIsLeftToRight && this.upperRight == null) || (!zIsLeftToRight && this.upperLeft == null))) {
                    rectangle3.f12373y = rectangle.f12373y;
                    rectangle3.height += rectangle.height;
                }
                this.vsb.setVisible(true);
                this.vsb.setBounds(rectangle3);
            } else {
                this.vsb.setVisible(false);
            }
        }
        if (this.hsb != null) {
            if (z3) {
                if (this.rowHead != null && UIManager.getBoolean("ScrollPane.fillLowerCorner") && ((zIsLeftToRight && this.lowerLeft == null) || (!zIsLeftToRight && this.lowerRight == null))) {
                    if (zIsLeftToRight) {
                        rectangle4.f12372x = rectangle2.f12372x;
                    }
                    rectangle4.width += rectangle2.width;
                }
                this.hsb.setVisible(true);
                this.hsb.setBounds(rectangle4);
            } else {
                this.hsb.setVisible(false);
            }
        }
        if (this.lowerLeft != null) {
            this.lowerLeft.setBounds(zIsLeftToRight ? rectangle2.f12372x : rectangle3.f12372x, rectangle4.f12373y, zIsLeftToRight ? rectangle2.width : rectangle3.width, rectangle4.height);
        }
        if (this.lowerRight != null) {
            this.lowerRight.setBounds(zIsLeftToRight ? rectangle3.f12372x : rectangle2.f12372x, rectangle4.f12373y, zIsLeftToRight ? rectangle3.width : rectangle2.width, rectangle4.height);
        }
        if (this.upperLeft != null) {
            this.upperLeft.setBounds(zIsLeftToRight ? rectangle2.f12372x : rectangle3.f12372x, rectangle.f12373y, zIsLeftToRight ? rectangle2.width : rectangle3.width, rectangle.height);
        }
        if (this.upperRight != null) {
            this.upperRight.setBounds(zIsLeftToRight ? rectangle3.f12372x : rectangle2.f12372x, rectangle.f12373y, zIsLeftToRight ? rectangle3.width : rectangle2.width, rectangle.height);
        }
    }

    private void adjustForVSB(boolean z2, Rectangle rectangle, Rectangle rectangle2, Insets insets, boolean z3) {
        int i2 = rectangle2.width;
        if (z2) {
            int iMax = Math.max(0, Math.min(this.vsb.getPreferredSize().width, rectangle.width));
            rectangle.width -= iMax;
            rectangle2.width = iMax;
            if (z3) {
                rectangle2.f12372x = rectangle.f12372x + rectangle.width + insets.right;
                return;
            } else {
                rectangle2.f12372x = rectangle.f12372x - insets.left;
                rectangle.f12372x += iMax;
                return;
            }
        }
        rectangle.width += i2;
    }

    private void adjustForHSB(boolean z2, Rectangle rectangle, Rectangle rectangle2, Insets insets) {
        int i2 = rectangle2.height;
        if (z2) {
            int iMax = Math.max(0, Math.min(rectangle.height, this.hsb.getPreferredSize().height));
            rectangle.height -= iMax;
            rectangle2.f12373y = rectangle.f12373y + rectangle.height + insets.bottom;
            rectangle2.height = iMax;
            return;
        }
        rectangle.height += i2;
    }

    @Deprecated
    public Rectangle getViewportBorderBounds(JScrollPane jScrollPane) {
        return jScrollPane.getViewportBorderBounds();
    }
}
