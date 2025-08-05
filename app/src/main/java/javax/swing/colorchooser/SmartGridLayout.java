package javax.swing.colorchooser;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.io.Serializable;

/* loaded from: rt.jar:javax/swing/colorchooser/SmartGridLayout.class */
class SmartGridLayout implements LayoutManager, Serializable {
    int rows;
    int columns;
    int xGap = 2;
    int yGap = 2;
    int componentCount = 0;
    Component[][] layoutGrid;

    public SmartGridLayout(int i2, int i3) {
        this.rows = 2;
        this.columns = 2;
        this.rows = i3;
        this.columns = i2;
        this.layoutGrid = new Component[i2][i3];
    }

    @Override // java.awt.LayoutManager
    public void layoutContainer(Container container) {
        buildLayoutGrid(container);
        int[] iArr = new int[this.rows];
        int[] iArr2 = new int[this.columns];
        for (int i2 = 0; i2 < this.rows; i2++) {
            iArr[i2] = computeRowHeight(i2);
        }
        for (int i3 = 0; i3 < this.columns; i3++) {
            iArr2[i3] = computeColumnWidth(i3);
        }
        Insets insets = container.getInsets();
        if (container.getComponentOrientation().isLeftToRight()) {
            int i4 = insets.left;
            for (int i5 = 0; i5 < this.columns; i5++) {
                int i6 = insets.top;
                for (int i7 = 0; i7 < this.rows; i7++) {
                    this.layoutGrid[i5][i7].setBounds(i4, i6, iArr2[i5], iArr[i7]);
                    i6 += iArr[i7] + this.yGap;
                }
                i4 += iArr2[i5] + this.xGap;
            }
            return;
        }
        int width = container.getWidth() - insets.right;
        for (int i8 = 0; i8 < this.columns; i8++) {
            int i9 = insets.top;
            int i10 = width - iArr2[i8];
            for (int i11 = 0; i11 < this.rows; i11++) {
                this.layoutGrid[i8][i11].setBounds(i10, i9, iArr2[i8], iArr[i11]);
                i9 += iArr[i11] + this.yGap;
            }
            width = i10 - this.xGap;
        }
    }

    @Override // java.awt.LayoutManager
    public Dimension minimumLayoutSize(Container container) {
        buildLayoutGrid(container);
        Insets insets = container.getInsets();
        int iComputeRowHeight = 0;
        int iComputeColumnWidth = 0;
        for (int i2 = 0; i2 < this.rows; i2++) {
            iComputeRowHeight += computeRowHeight(i2);
        }
        for (int i3 = 0; i3 < this.columns; i3++) {
            iComputeColumnWidth += computeColumnWidth(i3);
        }
        return new Dimension(iComputeColumnWidth + (this.xGap * (this.columns - 1)) + insets.right + insets.left, iComputeRowHeight + (this.yGap * (this.rows - 1)) + insets.top + insets.bottom);
    }

    @Override // java.awt.LayoutManager
    public Dimension preferredLayoutSize(Container container) {
        return minimumLayoutSize(container);
    }

    @Override // java.awt.LayoutManager
    public void addLayoutComponent(String str, Component component) {
    }

    @Override // java.awt.LayoutManager
    public void removeLayoutComponent(Component component) {
    }

    private void buildLayoutGrid(Container container) {
        Component[] components = container.getComponents();
        for (int i2 = 0; i2 < components.length; i2++) {
            int i3 = 0;
            int i4 = 0;
            if (i2 != 0) {
                i4 = i2 % this.columns;
                i3 = (i2 - i4) / this.columns;
            }
            this.layoutGrid[i4][i3] = components[i2];
        }
    }

    private int computeColumnWidth(int i2) {
        int i3 = 1;
        for (int i4 = 0; i4 < this.rows; i4++) {
            int i5 = this.layoutGrid[i2][i4].getPreferredSize().width;
            if (i5 > i3) {
                i3 = i5;
            }
        }
        return i3;
    }

    private int computeRowHeight(int i2) {
        int i3 = 1;
        for (int i4 = 0; i4 < this.columns; i4++) {
            int i5 = this.layoutGrid[i4][i2].getPreferredSize().height;
            if (i5 > i3) {
                i3 = i5;
            }
        }
        return i3;
    }
}
