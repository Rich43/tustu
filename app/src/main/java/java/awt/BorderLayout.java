package java.awt;

import java.io.Serializable;

/* loaded from: rt.jar:java/awt/BorderLayout.class */
public class BorderLayout implements LayoutManager2, Serializable {
    int hgap;
    int vgap;
    Component north;
    Component west;
    Component east;
    Component south;
    Component center;
    Component firstLine;
    Component lastLine;
    Component firstItem;
    Component lastItem;
    public static final String NORTH = "North";
    public static final String SOUTH = "South";
    public static final String EAST = "East";
    public static final String WEST = "West";
    public static final String CENTER = "Center";
    public static final String BEFORE_FIRST_LINE = "First";
    public static final String AFTER_LAST_LINE = "Last";
    public static final String BEFORE_LINE_BEGINS = "Before";
    public static final String AFTER_LINE_ENDS = "After";
    public static final String PAGE_START = "First";
    public static final String PAGE_END = "Last";
    public static final String LINE_START = "Before";
    public static final String LINE_END = "After";
    private static final long serialVersionUID = -8658291919501921765L;

    public BorderLayout() {
        this(0, 0);
    }

    public BorderLayout(int i2, int i3) {
        this.hgap = i2;
        this.vgap = i3;
    }

    public int getHgap() {
        return this.hgap;
    }

    public void setHgap(int i2) {
        this.hgap = i2;
    }

    public int getVgap() {
        return this.vgap;
    }

    public void setVgap(int i2) {
        this.vgap = i2;
    }

    @Override // java.awt.LayoutManager2
    public void addLayoutComponent(Component component, Object obj) {
        synchronized (component.getTreeLock()) {
            if (obj != null) {
                if (!(obj instanceof String)) {
                    throw new IllegalArgumentException("cannot add to layout: constraint must be a string (or null)");
                }
            }
            addLayoutComponent((String) obj, component);
        }
    }

    @Override // java.awt.LayoutManager
    @Deprecated
    public void addLayoutComponent(String str, Component component) {
        synchronized (component.getTreeLock()) {
            if (str == null) {
                str = CENTER;
            }
            if (CENTER.equals(str)) {
                this.center = component;
            } else if ("North".equals(str)) {
                this.north = component;
            } else if ("South".equals(str)) {
                this.south = component;
            } else if ("East".equals(str)) {
                this.east = component;
            } else if ("West".equals(str)) {
                this.west = component;
            } else if ("First".equals(str)) {
                this.firstLine = component;
            } else if ("Last".equals(str)) {
                this.lastLine = component;
            } else if ("Before".equals(str)) {
                this.firstItem = component;
            } else if ("After".equals(str)) {
                this.lastItem = component;
            } else {
                throw new IllegalArgumentException("cannot add to layout: unknown constraint: " + str);
            }
        }
    }

    @Override // java.awt.LayoutManager
    public void removeLayoutComponent(Component component) {
        synchronized (component.getTreeLock()) {
            if (component == this.center) {
                this.center = null;
            } else if (component == this.north) {
                this.north = null;
            } else if (component == this.south) {
                this.south = null;
            } else if (component == this.east) {
                this.east = null;
            } else if (component == this.west) {
                this.west = null;
            }
            if (component == this.firstLine) {
                this.firstLine = null;
            } else if (component == this.lastLine) {
                this.lastLine = null;
            } else if (component == this.firstItem) {
                this.firstItem = null;
            } else if (component == this.lastItem) {
                this.lastItem = null;
            }
        }
    }

    public Component getLayoutComponent(Object obj) {
        if (CENTER.equals(obj)) {
            return this.center;
        }
        if ("North".equals(obj)) {
            return this.north;
        }
        if ("South".equals(obj)) {
            return this.south;
        }
        if ("West".equals(obj)) {
            return this.west;
        }
        if ("East".equals(obj)) {
            return this.east;
        }
        if ("First".equals(obj)) {
            return this.firstLine;
        }
        if ("Last".equals(obj)) {
            return this.lastLine;
        }
        if ("Before".equals(obj)) {
            return this.firstItem;
        }
        if ("After".equals(obj)) {
            return this.lastItem;
        }
        throw new IllegalArgumentException("cannot get component: unknown constraint: " + obj);
    }

    public Component getLayoutComponent(Container container, Object obj) {
        Component component;
        boolean zIsLeftToRight = container.getComponentOrientation().isLeftToRight();
        if ("North".equals(obj)) {
            component = this.firstLine != null ? this.firstLine : this.north;
        } else if ("South".equals(obj)) {
            component = this.lastLine != null ? this.lastLine : this.south;
        } else if ("West".equals(obj)) {
            component = zIsLeftToRight ? this.firstItem : this.lastItem;
            if (component == null) {
                component = this.west;
            }
        } else if ("East".equals(obj)) {
            component = zIsLeftToRight ? this.lastItem : this.firstItem;
            if (component == null) {
                component = this.east;
            }
        } else if (CENTER.equals(obj)) {
            component = this.center;
        } else {
            throw new IllegalArgumentException("cannot get component: invalid constraint: " + obj);
        }
        return component;
    }

    public Object getConstraints(Component component) {
        if (component == null) {
            return null;
        }
        if (component == this.center) {
            return CENTER;
        }
        if (component == this.north) {
            return "North";
        }
        if (component == this.south) {
            return "South";
        }
        if (component == this.west) {
            return "West";
        }
        if (component == this.east) {
            return "East";
        }
        if (component == this.firstLine) {
            return "First";
        }
        if (component == this.lastLine) {
            return "Last";
        }
        if (component == this.firstItem) {
            return "Before";
        }
        if (component == this.lastItem) {
            return "After";
        }
        return null;
    }

    @Override // java.awt.LayoutManager
    public Dimension minimumLayoutSize(Container container) {
        Dimension dimension;
        synchronized (container.getTreeLock()) {
            dimension = new Dimension(0, 0);
            boolean zIsLeftToRight = container.getComponentOrientation().isLeftToRight();
            Component child = getChild("East", zIsLeftToRight);
            if (child != null) {
                Dimension minimumSize = child.getMinimumSize();
                dimension.width += minimumSize.width + this.hgap;
                dimension.height = Math.max(minimumSize.height, dimension.height);
            }
            Component child2 = getChild("West", zIsLeftToRight);
            if (child2 != null) {
                Dimension minimumSize2 = child2.getMinimumSize();
                dimension.width += minimumSize2.width + this.hgap;
                dimension.height = Math.max(minimumSize2.height, dimension.height);
            }
            Component child3 = getChild(CENTER, zIsLeftToRight);
            if (child3 != null) {
                Dimension minimumSize3 = child3.getMinimumSize();
                dimension.width += minimumSize3.width;
                dimension.height = Math.max(minimumSize3.height, dimension.height);
            }
            Component child4 = getChild("North", zIsLeftToRight);
            if (child4 != null) {
                Dimension minimumSize4 = child4.getMinimumSize();
                dimension.width = Math.max(minimumSize4.width, dimension.width);
                dimension.height += minimumSize4.height + this.vgap;
            }
            Component child5 = getChild("South", zIsLeftToRight);
            if (child5 != null) {
                Dimension minimumSize5 = child5.getMinimumSize();
                dimension.width = Math.max(minimumSize5.width, dimension.width);
                dimension.height += minimumSize5.height + this.vgap;
            }
            Insets insets = container.getInsets();
            dimension.width += insets.left + insets.right;
            dimension.height += insets.top + insets.bottom;
        }
        return dimension;
    }

    @Override // java.awt.LayoutManager
    public Dimension preferredLayoutSize(Container container) {
        Dimension dimension;
        synchronized (container.getTreeLock()) {
            dimension = new Dimension(0, 0);
            boolean zIsLeftToRight = container.getComponentOrientation().isLeftToRight();
            Component child = getChild("East", zIsLeftToRight);
            if (child != null) {
                Dimension preferredSize = child.getPreferredSize();
                dimension.width += preferredSize.width + this.hgap;
                dimension.height = Math.max(preferredSize.height, dimension.height);
            }
            Component child2 = getChild("West", zIsLeftToRight);
            if (child2 != null) {
                Dimension preferredSize2 = child2.getPreferredSize();
                dimension.width += preferredSize2.width + this.hgap;
                dimension.height = Math.max(preferredSize2.height, dimension.height);
            }
            Component child3 = getChild(CENTER, zIsLeftToRight);
            if (child3 != null) {
                Dimension preferredSize3 = child3.getPreferredSize();
                dimension.width += preferredSize3.width;
                dimension.height = Math.max(preferredSize3.height, dimension.height);
            }
            Component child4 = getChild("North", zIsLeftToRight);
            if (child4 != null) {
                Dimension preferredSize4 = child4.getPreferredSize();
                dimension.width = Math.max(preferredSize4.width, dimension.width);
                dimension.height += preferredSize4.height + this.vgap;
            }
            Component child5 = getChild("South", zIsLeftToRight);
            if (child5 != null) {
                Dimension preferredSize5 = child5.getPreferredSize();
                dimension.width = Math.max(preferredSize5.width, dimension.width);
                dimension.height += preferredSize5.height + this.vgap;
            }
            Insets insets = container.getInsets();
            dimension.width += insets.left + insets.right;
            dimension.height += insets.top + insets.bottom;
        }
        return dimension;
    }

    @Override // java.awt.LayoutManager2
    public Dimension maximumLayoutSize(Container container) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override // java.awt.LayoutManager2
    public float getLayoutAlignmentX(Container container) {
        return 0.5f;
    }

    @Override // java.awt.LayoutManager2
    public float getLayoutAlignmentY(Container container) {
        return 0.5f;
    }

    @Override // java.awt.LayoutManager2
    public void invalidateLayout(Container container) {
    }

    @Override // java.awt.LayoutManager
    public void layoutContainer(Container container) {
        synchronized (container.getTreeLock()) {
            Insets insets = container.getInsets();
            int i2 = insets.top;
            int i3 = container.height - insets.bottom;
            int i4 = insets.left;
            int i5 = container.width - insets.right;
            boolean zIsLeftToRight = container.getComponentOrientation().isLeftToRight();
            Component child = getChild("North", zIsLeftToRight);
            if (child != null) {
                child.setSize(i5 - i4, child.height);
                Dimension preferredSize = child.getPreferredSize();
                child.setBounds(i4, i2, i5 - i4, preferredSize.height);
                i2 += preferredSize.height + this.vgap;
            }
            Component child2 = getChild("South", zIsLeftToRight);
            if (child2 != null) {
                child2.setSize(i5 - i4, child2.height);
                Dimension preferredSize2 = child2.getPreferredSize();
                child2.setBounds(i4, i3 - preferredSize2.height, i5 - i4, preferredSize2.height);
                i3 -= preferredSize2.height + this.vgap;
            }
            Component child3 = getChild("East", zIsLeftToRight);
            if (child3 != null) {
                child3.setSize(child3.width, i3 - i2);
                Dimension preferredSize3 = child3.getPreferredSize();
                child3.setBounds(i5 - preferredSize3.width, i2, preferredSize3.width, i3 - i2);
                i5 -= preferredSize3.width + this.hgap;
            }
            Component child4 = getChild("West", zIsLeftToRight);
            if (child4 != null) {
                child4.setSize(child4.width, i3 - i2);
                Dimension preferredSize4 = child4.getPreferredSize();
                child4.setBounds(i4, i2, preferredSize4.width, i3 - i2);
                i4 += preferredSize4.width + this.hgap;
            }
            Component child5 = getChild(CENTER, zIsLeftToRight);
            if (child5 != null) {
                child5.setBounds(i4, i2, i5 - i4, i3 - i2);
            }
        }
    }

    private Component getChild(String str, boolean z2) {
        Component component = null;
        if (str == "North") {
            component = this.firstLine != null ? this.firstLine : this.north;
        } else if (str == "South") {
            component = this.lastLine != null ? this.lastLine : this.south;
        } else if (str == "West") {
            component = z2 ? this.firstItem : this.lastItem;
            if (component == null) {
                component = this.west;
            }
        } else if (str == "East") {
            component = z2 ? this.lastItem : this.firstItem;
            if (component == null) {
                component = this.east;
            }
        } else if (str == CENTER) {
            component = this.center;
        }
        if (component != null && !component.visible) {
            component = null;
        }
        return component;
    }

    public String toString() {
        return getClass().getName() + "[hgap=" + this.hgap + ",vgap=" + this.vgap + "]";
    }
}
