package java.awt;

import java.io.Serializable;

/* loaded from: rt.jar:java/awt/GridLayout.class */
public class GridLayout implements LayoutManager, Serializable {
    private static final long serialVersionUID = -7411804673224730901L;
    int hgap;
    int vgap;
    int rows;
    int cols;

    public GridLayout() {
        this(1, 0, 0, 0);
    }

    public GridLayout(int i2, int i3) {
        this(i2, i3, 0, 0);
    }

    public GridLayout(int i2, int i3, int i4, int i5) {
        if (i2 == 0 && i3 == 0) {
            throw new IllegalArgumentException("rows and cols cannot both be zero");
        }
        this.rows = i2;
        this.cols = i3;
        this.hgap = i4;
        this.vgap = i5;
    }

    public int getRows() {
        return this.rows;
    }

    public void setRows(int i2) {
        if (i2 == 0 && this.cols == 0) {
            throw new IllegalArgumentException("rows and cols cannot both be zero");
        }
        this.rows = i2;
    }

    public int getColumns() {
        return this.cols;
    }

    public void setColumns(int i2) {
        if (i2 == 0 && this.rows == 0) {
            throw new IllegalArgumentException("rows and cols cannot both be zero");
        }
        this.cols = i2;
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

    @Override // java.awt.LayoutManager
    public void addLayoutComponent(String str, Component component) {
    }

    @Override // java.awt.LayoutManager
    public void removeLayoutComponent(Component component) {
    }

    @Override // java.awt.LayoutManager
    public Dimension preferredLayoutSize(Container container) {
        Dimension dimension;
        synchronized (container.getTreeLock()) {
            Insets insets = container.getInsets();
            int componentCount = container.getComponentCount();
            int i2 = this.rows;
            int i3 = this.cols;
            if (i2 > 0) {
                i3 = ((componentCount + i2) - 1) / i2;
            } else {
                i2 = ((componentCount + i3) - 1) / i3;
            }
            int i4 = 0;
            int i5 = 0;
            for (int i6 = 0; i6 < componentCount; i6++) {
                Dimension preferredSize = container.getComponent(i6).getPreferredSize();
                if (i4 < preferredSize.width) {
                    i4 = preferredSize.width;
                }
                if (i5 < preferredSize.height) {
                    i5 = preferredSize.height;
                }
            }
            dimension = new Dimension(insets.left + insets.right + (i3 * i4) + ((i3 - 1) * this.hgap), insets.top + insets.bottom + (i2 * i5) + ((i2 - 1) * this.vgap));
        }
        return dimension;
    }

    @Override // java.awt.LayoutManager
    public Dimension minimumLayoutSize(Container container) {
        Dimension dimension;
        synchronized (container.getTreeLock()) {
            Insets insets = container.getInsets();
            int componentCount = container.getComponentCount();
            int i2 = this.rows;
            int i3 = this.cols;
            if (i2 > 0) {
                i3 = ((componentCount + i2) - 1) / i2;
            } else {
                i2 = ((componentCount + i3) - 1) / i3;
            }
            int i4 = 0;
            int i5 = 0;
            for (int i6 = 0; i6 < componentCount; i6++) {
                Dimension minimumSize = container.getComponent(i6).getMinimumSize();
                if (i4 < minimumSize.width) {
                    i4 = minimumSize.width;
                }
                if (i5 < minimumSize.height) {
                    i5 = minimumSize.height;
                }
            }
            dimension = new Dimension(insets.left + insets.right + (i3 * i4) + ((i3 - 1) * this.hgap), insets.top + insets.bottom + (i2 * i5) + ((i2 - 1) * this.vgap));
        }
        return dimension;
    }

    @Override // java.awt.LayoutManager
    public void layoutContainer(Container container) {
        synchronized (container.getTreeLock()) {
            Insets insets = container.getInsets();
            int componentCount = container.getComponentCount();
            int i2 = this.rows;
            int i3 = this.cols;
            boolean zIsLeftToRight = container.getComponentOrientation().isLeftToRight();
            if (componentCount == 0) {
                return;
            }
            if (i2 > 0) {
                i3 = ((componentCount + i2) - 1) / i2;
            } else {
                i2 = ((componentCount + i3) - 1) / i3;
            }
            int i4 = (i3 - 1) * this.hgap;
            int i5 = container.width - (insets.left + insets.right);
            int i6 = (i5 - i4) / i3;
            int i7 = (i5 - ((i6 * i3) + i4)) / 2;
            int i8 = (i2 - 1) * this.vgap;
            int i9 = container.height - (insets.top + insets.bottom);
            int i10 = (i9 - i8) / i2;
            int i11 = (i9 - ((i10 * i2) + i8)) / 2;
            if (zIsLeftToRight) {
                int i12 = 0;
                int i13 = insets.left + i7;
                while (i12 < i3) {
                    int i14 = 0;
                    int i15 = insets.top + i11;
                    while (i14 < i2) {
                        int i16 = (i14 * i3) + i12;
                        if (i16 < componentCount) {
                            container.getComponent(i16).setBounds(i13, i15, i6, i10);
                        }
                        i14++;
                        i15 += i10 + this.vgap;
                    }
                    i12++;
                    i13 += i6 + this.hgap;
                }
            } else {
                int i17 = 0;
                int i18 = ((container.width - insets.right) - i6) - i7;
                while (i17 < i3) {
                    int i19 = 0;
                    int i20 = insets.top + i11;
                    while (i19 < i2) {
                        int i21 = (i19 * i3) + i17;
                        if (i21 < componentCount) {
                            container.getComponent(i21).setBounds(i18, i20, i6, i10);
                        }
                        i19++;
                        i20 += i10 + this.vgap;
                    }
                    i17++;
                    i18 -= i6 + this.hgap;
                }
            }
        }
    }

    public String toString() {
        return getClass().getName() + "[hgap=" + this.hgap + ",vgap=" + this.vgap + ",rows=" + this.rows + ",cols=" + this.cols + "]";
    }
}
