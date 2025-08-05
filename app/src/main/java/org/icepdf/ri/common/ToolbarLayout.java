package org.icepdf.ri.common;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/ToolbarLayout.class */
class ToolbarLayout implements LayoutManager {
    public static final int LEFT = 0;
    public static final int CENTER = 1;
    public static final int RIGHT = 2;
    public static final int LEADING = 3;
    public static final int TRAILING = 4;
    int align;
    int hgap;
    int vgap;

    public ToolbarLayout() {
        this(1, 5, 5);
    }

    public ToolbarLayout(int align) {
        this(align, 5, 5);
    }

    public ToolbarLayout(int align, int hgap, int vgap) {
        this.hgap = hgap;
        this.vgap = vgap;
        setAlignment(align);
    }

    public int getAlignment() {
        return this.align;
    }

    public void setAlignment(int align) {
        switch (align) {
            case 3:
                this.align = 0;
                break;
            case 4:
                this.align = 2;
                break;
            default:
                this.align = align;
                break;
        }
    }

    public int getHgap() {
        return this.hgap;
    }

    public void setHgap(int hgap) {
        this.hgap = hgap;
    }

    public int getVgap() {
        return this.vgap;
    }

    public void setVgap(int vgap) {
        this.vgap = vgap;
    }

    @Override // java.awt.LayoutManager
    public Dimension minimumLayoutSize(Container parent) {
        Dimension dim;
        synchronized (parent.getTreeLock()) {
            dim = new Dimension(0, 0);
            int componentCount = parent.getComponentCount();
            for (int i2 = 0; i2 < componentCount; i2++) {
                Component c2 = parent.getComponent(i2);
                if (c2.isVisible()) {
                    Dimension d2 = c2.getMinimumSize();
                    dim.height = Math.max(dim.height, d2.height);
                    if (i2 > 0) {
                        dim.width += this.hgap;
                    }
                    dim.width += d2.width;
                }
            }
            Insets insets = parent.getInsets();
            dim.width += insets.left + insets.right + (2 * this.hgap);
            dim.height += insets.top + insets.bottom + (2 * this.vgap);
        }
        return dim;
    }

    @Override // java.awt.LayoutManager
    public Dimension preferredLayoutSize(Container parent) {
        Dimension dim;
        if (parent.getWidth() == 0) {
            return minimumLayoutSize(parent);
        }
        synchronized (parent.getTreeLock()) {
            dim = new Dimension(0, 0);
            int maxWidth = 0;
            int componentCount = parent.getComponentCount();
            Insets insets = parent.getInsets();
            int padWidths = (this.hgap * 2) + insets.left + insets.right;
            for (int i2 = 0; i2 < componentCount; i2++) {
                Component c2 = parent.getComponent(i2);
                if (c2.isVisible()) {
                    Dimension d2 = c2.getPreferredSize();
                    if (dim.width + d2.width + padWidths <= parent.getWidth()) {
                        dim.height = Math.max(dim.height, d2.height);
                    } else {
                        dim.height += this.vgap + d2.height;
                        dim.width = 0;
                    }
                    if (dim.width > 0) {
                        dim.width += this.hgap;
                    }
                    dim.width += d2.width;
                    if (dim.width > maxWidth) {
                        maxWidth = dim.width;
                    }
                }
            }
            dim.width = Math.max(dim.width, maxWidth);
            dim.width += insets.left + insets.right + (2 * this.hgap);
            dim.height += insets.top + insets.bottom + (2 * this.vgap);
        }
        return dim;
    }

    @Override // java.awt.LayoutManager
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int maxWidth = parent.getWidth() - ((insets.left + insets.right) + (this.hgap * 2));
            int componentCount = parent.getComponentCount();
            int x2 = 0;
            int y2 = insets.top + this.vgap;
            int rowh = 0;
            int start = 0;
            boolean ltr = parent.getComponentOrientation().isLeftToRight();
            for (int i2 = 0; i2 < componentCount; i2++) {
                Component c2 = parent.getComponent(i2);
                if (c2.isVisible()) {
                    Dimension d2 = c2.getPreferredSize();
                    c2.setSize(d2.width, d2.height);
                    if (x2 == 0 || x2 + d2.width <= maxWidth) {
                        if (x2 > 0) {
                            x2 += this.hgap;
                        }
                        x2 += d2.width;
                        rowh = Math.max(rowh, d2.height);
                    } else {
                        int rowh2 = moveComponents(parent, insets.left + this.hgap, y2, maxWidth - x2, rowh, start, i2, ltr);
                        x2 = d2.width;
                        y2 += this.vgap + rowh2;
                        rowh = d2.height;
                        start = i2;
                    }
                }
            }
            moveComponents(parent, insets.left + this.hgap, y2, maxWidth - x2, rowh, start, componentCount, ltr);
        }
    }

    private int moveComponents(Container parent, int x2, int y2, int width, int height, int rowStart, int rowEnd, boolean ltr) {
        switch (this.align) {
            case 0:
                x2 += ltr ? 0 : width;
                break;
            case 1:
                x2 += width / 2;
                break;
            case 2:
                x2 += ltr ? width : 0;
                break;
            case 4:
                x2 += width;
                break;
        }
        for (int i2 = rowStart; i2 < rowEnd; i2++) {
            Component c2 = parent.getComponent(i2);
            if (c2.isVisible()) {
                int cy = y2 + ((height - c2.getHeight()) / 2);
                if (ltr) {
                    c2.setLocation(x2, cy);
                } else {
                    c2.setLocation((parent.getWidth() - x2) - c2.getWidth(), cy);
                }
                x2 += c2.getWidth() + this.hgap;
            }
        }
        return height;
    }

    @Override // java.awt.LayoutManager
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override // java.awt.LayoutManager
    public void removeLayoutComponent(Component comp) {
    }
}
