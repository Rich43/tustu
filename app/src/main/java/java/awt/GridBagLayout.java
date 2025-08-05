package java.awt;

import java.awt.Component;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Hashtable;

/* loaded from: rt.jar:java/awt/GridBagLayout.class */
public class GridBagLayout implements LayoutManager2, Serializable {
    static final int EMPIRICMULTIPLIER = 2;
    protected static final int MAXGRIDSIZE = 512;
    protected static final int MINSIZE = 1;
    protected static final int PREFERREDSIZE = 2;
    protected GridBagLayoutInfo layoutInfo;
    public int[] columnWidths;
    public int[] rowHeights;
    public double[] columnWeights;
    public double[] rowWeights;
    private Component componentAdjusting;
    static final long serialVersionUID = 8838754796412211005L;
    transient boolean rightToLeft = false;
    protected Hashtable<Component, GridBagConstraints> comptable = new Hashtable<>();
    protected GridBagConstraints defaultConstraints = new GridBagConstraints();

    public void setConstraints(Component component, GridBagConstraints gridBagConstraints) {
        this.comptable.put(component, (GridBagConstraints) gridBagConstraints.clone());
    }

    public GridBagConstraints getConstraints(Component component) {
        GridBagConstraints gridBagConstraints = this.comptable.get(component);
        if (gridBagConstraints == null) {
            setConstraints(component, this.defaultConstraints);
            gridBagConstraints = this.comptable.get(component);
        }
        return (GridBagConstraints) gridBagConstraints.clone();
    }

    protected GridBagConstraints lookupConstraints(Component component) {
        GridBagConstraints gridBagConstraints = this.comptable.get(component);
        if (gridBagConstraints == null) {
            setConstraints(component, this.defaultConstraints);
            gridBagConstraints = this.comptable.get(component);
        }
        return gridBagConstraints;
    }

    private void removeConstraints(Component component) {
        this.comptable.remove(component);
    }

    public Point getLayoutOrigin() {
        Point point = new Point(0, 0);
        if (this.layoutInfo != null) {
            point.f12370x = this.layoutInfo.startx;
            point.f12371y = this.layoutInfo.starty;
        }
        return point;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v3, types: [int[], int[][]] */
    public int[][] getLayoutDimensions() {
        if (this.layoutInfo == null) {
            return new int[2][0];
        }
        ?? r0 = {new int[this.layoutInfo.width], new int[this.layoutInfo.height]};
        System.arraycopy(this.layoutInfo.minWidth, 0, r0[0], 0, this.layoutInfo.width);
        System.arraycopy(this.layoutInfo.minHeight, 0, r0[1], 0, this.layoutInfo.height);
        return r0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v3, types: [double[], double[][]] */
    public double[][] getLayoutWeights() {
        if (this.layoutInfo == null) {
            return new double[2][0];
        }
        ?? r0 = {new double[this.layoutInfo.width], new double[this.layoutInfo.height]};
        System.arraycopy(this.layoutInfo.weightX, 0, r0[0], 0, this.layoutInfo.width);
        System.arraycopy(this.layoutInfo.weightY, 0, r0[1], 0, this.layoutInfo.height);
        return r0;
    }

    public Point location(int i2, int i3) {
        int i4;
        Point point = new Point(0, 0);
        if (this.layoutInfo == null) {
            return point;
        }
        int i5 = this.layoutInfo.startx;
        if (!this.rightToLeft) {
            i4 = 0;
            while (i4 < this.layoutInfo.width) {
                i5 += this.layoutInfo.minWidth[i4];
                if (i5 > i2) {
                    break;
                }
                i4++;
            }
        } else {
            int i6 = this.layoutInfo.width - 1;
            while (i6 >= 0 && i5 <= i2) {
                i5 += this.layoutInfo.minWidth[i6];
                i6--;
            }
            i4 = i6 + 1;
        }
        point.f12370x = i4;
        int i7 = this.layoutInfo.starty;
        int i8 = 0;
        while (i8 < this.layoutInfo.height) {
            i7 += this.layoutInfo.minHeight[i8];
            if (i7 > i3) {
                break;
            }
            i8++;
        }
        point.f12371y = i8;
        return point;
    }

    @Override // java.awt.LayoutManager
    public void addLayoutComponent(String str, Component component) {
    }

    @Override // java.awt.LayoutManager2
    public void addLayoutComponent(Component component, Object obj) {
        if (obj instanceof GridBagConstraints) {
            setConstraints(component, (GridBagConstraints) obj);
        } else if (obj != null) {
            throw new IllegalArgumentException("cannot add to layout: constraints must be a GridBagConstraint");
        }
    }

    @Override // java.awt.LayoutManager
    public void removeLayoutComponent(Component component) {
        removeConstraints(component);
    }

    @Override // java.awt.LayoutManager
    public Dimension preferredLayoutSize(Container container) {
        return getMinSize(container, getLayoutInfo(container, 2));
    }

    @Override // java.awt.LayoutManager
    public Dimension minimumLayoutSize(Container container) {
        return getMinSize(container, getLayoutInfo(container, 1));
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
        arrangeGrid(container);
    }

    public String toString() {
        return getClass().getName();
    }

    protected GridBagLayoutInfo getLayoutInfo(Container container, int i2) {
        return GetLayoutInfo(container, i2);
    }

    private long[] preInitMaximumArraySizes(Container container) {
        int iMax = 0;
        int iMax2 = 0;
        long[] jArr = new long[2];
        for (Component component : container.getComponents()) {
            if (component.isVisible()) {
                GridBagConstraints gridBagConstraintsLookupConstraints = lookupConstraints(component);
                int i2 = gridBagConstraintsLookupConstraints.gridx;
                int i3 = gridBagConstraintsLookupConstraints.gridy;
                int i4 = gridBagConstraintsLookupConstraints.gridwidth;
                int i5 = gridBagConstraintsLookupConstraints.gridheight;
                if (i2 < 0) {
                    iMax2++;
                    i2 = iMax2;
                }
                if (i3 < 0) {
                    iMax++;
                    i3 = iMax;
                }
                if (i4 <= 0) {
                    i4 = 1;
                }
                if (i5 <= 0) {
                    i5 = 1;
                }
                iMax = Math.max(i3 + i5, iMax);
                iMax2 = Math.max(i2 + i4, iMax2);
            }
        }
        jArr[0] = iMax;
        jArr[1] = iMax2;
        return jArr;
    }

    protected GridBagLayoutInfo GetLayoutInfo(Container container, int i2) {
        GridBagLayoutInfo gridBagLayoutInfo;
        Dimension minimumSize;
        synchronized (container.getTreeLock()) {
            Component[] components = container.getComponents();
            int length = 0;
            int length2 = 0;
            int i3 = -1;
            int i4 = -1;
            long[] jArrPreInitMaximumArraySizes = preInitMaximumArraySizes(container);
            int iMax = 2 * jArrPreInitMaximumArraySizes[0] > 2147483647L ? Integer.MAX_VALUE : 2 * ((int) jArrPreInitMaximumArraySizes[0]);
            int iMax2 = 2 * jArrPreInitMaximumArraySizes[1] > 2147483647L ? Integer.MAX_VALUE : 2 * ((int) jArrPreInitMaximumArraySizes[1]);
            if (this.rowHeights != null) {
                iMax = Math.max(iMax, this.rowHeights.length);
            }
            if (this.columnWidths != null) {
                iMax2 = Math.max(iMax2, this.columnWidths.length);
            }
            int[] iArr = new int[iMax];
            int[] iArr2 = new int[iMax2];
            boolean z2 = false;
            for (Component component : components) {
                if (component.isVisible()) {
                    GridBagConstraints gridBagConstraintsLookupConstraints = lookupConstraints(component);
                    int i5 = gridBagConstraintsLookupConstraints.gridx;
                    int i6 = gridBagConstraintsLookupConstraints.gridy;
                    int i7 = gridBagConstraintsLookupConstraints.gridwidth;
                    if (i7 <= 0) {
                        i7 = 1;
                    }
                    int i8 = gridBagConstraintsLookupConstraints.gridheight;
                    if (i8 <= 0) {
                        i8 = 1;
                    }
                    if (i5 < 0 && i6 < 0) {
                        if (i4 >= 0) {
                            i6 = i4;
                        } else if (i3 >= 0) {
                            i5 = i3;
                        } else {
                            i6 = 0;
                        }
                    }
                    if (i5 < 0) {
                        int iMax3 = 0;
                        for (int i9 = i6; i9 < i6 + i8; i9++) {
                            iMax3 = Math.max(iMax3, iArr[i9]);
                        }
                        i5 = (iMax3 - i5) - 1;
                        if (i5 < 0) {
                            i5 = 0;
                        }
                    } else if (i6 < 0) {
                        int iMax4 = 0;
                        for (int i10 = i5; i10 < i5 + i7; i10++) {
                            iMax4 = Math.max(iMax4, iArr2[i10]);
                        }
                        i6 = (iMax4 - i6) - 1;
                        if (i6 < 0) {
                            i6 = 0;
                        }
                    }
                    int i11 = i5 + i7;
                    if (length2 < i11) {
                        length2 = i11;
                    }
                    int i12 = i6 + i8;
                    if (length < i12) {
                        length = i12;
                    }
                    for (int i13 = i5; i13 < i5 + i7; i13++) {
                        iArr2[i13] = i12;
                    }
                    for (int i14 = i6; i14 < i6 + i8; i14++) {
                        iArr[i14] = i11;
                    }
                    if (i2 == 2) {
                        minimumSize = component.getPreferredSize();
                    } else {
                        minimumSize = component.getMinimumSize();
                    }
                    gridBagConstraintsLookupConstraints.minWidth = minimumSize.width;
                    gridBagConstraintsLookupConstraints.minHeight = minimumSize.height;
                    if (calculateBaseline(component, gridBagConstraintsLookupConstraints, minimumSize)) {
                        z2 = true;
                    }
                    if (gridBagConstraintsLookupConstraints.gridheight == 0 && gridBagConstraintsLookupConstraints.gridwidth == 0) {
                        i3 = -1;
                        i4 = -1;
                    }
                    if (gridBagConstraintsLookupConstraints.gridheight == 0 && i4 < 0) {
                        i3 = i5 + i7;
                    } else if (gridBagConstraintsLookupConstraints.gridwidth == 0 && i3 < 0) {
                        i4 = i6 + i8;
                    }
                }
            }
            if (this.columnWidths != null && length2 < this.columnWidths.length) {
                length2 = this.columnWidths.length;
            }
            if (this.rowHeights != null && length < this.rowHeights.length) {
                length = this.rowHeights.length;
            }
            gridBagLayoutInfo = new GridBagLayoutInfo(length2, length);
            int i15 = -1;
            int i16 = -1;
            Arrays.fill(iArr, 0);
            Arrays.fill(iArr2, 0);
            int[] iArr3 = null;
            int[] iArr4 = null;
            short[] sArr = null;
            if (z2) {
                int[] iArr5 = new int[length];
                iArr3 = iArr5;
                gridBagLayoutInfo.maxAscent = iArr5;
                int[] iArr6 = new int[length];
                iArr4 = iArr6;
                gridBagLayoutInfo.maxDescent = iArr6;
                short[] sArr2 = new short[length];
                sArr = sArr2;
                gridBagLayoutInfo.baselineType = sArr2;
                gridBagLayoutInfo.hasBaseline = true;
            }
            for (Component component2 : components) {
                if (component2.isVisible()) {
                    GridBagConstraints gridBagConstraintsLookupConstraints2 = lookupConstraints(component2);
                    int i17 = gridBagConstraintsLookupConstraints2.gridx;
                    int i18 = gridBagConstraintsLookupConstraints2.gridy;
                    int i19 = gridBagConstraintsLookupConstraints2.gridwidth;
                    int i20 = gridBagConstraintsLookupConstraints2.gridheight;
                    if (i17 < 0 && i18 < 0) {
                        if (i16 >= 0) {
                            i18 = i16;
                        } else if (i15 >= 0) {
                            i17 = i15;
                        } else {
                            i18 = 0;
                        }
                    }
                    if (i17 < 0) {
                        if (i20 <= 0) {
                            i20 += gridBagLayoutInfo.height - i18;
                            if (i20 < 1) {
                                i20 = 1;
                            }
                        }
                        int iMax5 = 0;
                        for (int i21 = i18; i21 < i18 + i20; i21++) {
                            iMax5 = Math.max(iMax5, iArr[i21]);
                        }
                        i17 = (iMax5 - i17) - 1;
                        if (i17 < 0) {
                            i17 = 0;
                        }
                    } else if (i18 < 0) {
                        if (i19 <= 0) {
                            i19 += gridBagLayoutInfo.width - i17;
                            if (i19 < 1) {
                                i19 = 1;
                            }
                        }
                        int iMax6 = 0;
                        for (int i22 = i17; i22 < i17 + i19; i22++) {
                            iMax6 = Math.max(iMax6, iArr2[i22]);
                        }
                        i18 = (iMax6 - i18) - 1;
                        if (i18 < 0) {
                            i18 = 0;
                        }
                    }
                    if (i19 <= 0) {
                        i19 += gridBagLayoutInfo.width - i17;
                        if (i19 < 1) {
                            i19 = 1;
                        }
                    }
                    if (i20 <= 0) {
                        i20 += gridBagLayoutInfo.height - i18;
                        if (i20 < 1) {
                            i20 = 1;
                        }
                    }
                    int i23 = i17 + i19;
                    int i24 = i18 + i20;
                    for (int i25 = i17; i25 < i17 + i19; i25++) {
                        iArr2[i25] = i24;
                    }
                    for (int i26 = i18; i26 < i18 + i20; i26++) {
                        iArr[i26] = i23;
                    }
                    if (gridBagConstraintsLookupConstraints2.gridheight == 0 && gridBagConstraintsLookupConstraints2.gridwidth == 0) {
                        i15 = -1;
                        i16 = -1;
                    }
                    if (gridBagConstraintsLookupConstraints2.gridheight == 0 && i16 < 0) {
                        i15 = i17 + i19;
                    } else if (gridBagConstraintsLookupConstraints2.gridwidth == 0 && i15 < 0) {
                        i16 = i18 + i20;
                    }
                    gridBagConstraintsLookupConstraints2.tempX = i17;
                    gridBagConstraintsLookupConstraints2.tempY = i18;
                    gridBagConstraintsLookupConstraints2.tempWidth = i19;
                    gridBagConstraintsLookupConstraints2.tempHeight = i20;
                    int i27 = gridBagConstraintsLookupConstraints2.anchor;
                    if (z2) {
                        switch (i27) {
                            case 256:
                            case 512:
                            case 768:
                                if (gridBagConstraintsLookupConstraints2.ascent < 0) {
                                    break;
                                } else {
                                    if (i20 == 1) {
                                        iArr3[i18] = Math.max(iArr3[i18], gridBagConstraintsLookupConstraints2.ascent);
                                        iArr4[i18] = Math.max(iArr4[i18], gridBagConstraintsLookupConstraints2.descent);
                                    } else if (gridBagConstraintsLookupConstraints2.baselineResizeBehavior == Component.BaselineResizeBehavior.CONSTANT_DESCENT) {
                                        iArr4[(i18 + i20) - 1] = Math.max(iArr4[(i18 + i20) - 1], gridBagConstraintsLookupConstraints2.descent);
                                    } else {
                                        iArr3[i18] = Math.max(iArr3[i18], gridBagConstraintsLookupConstraints2.ascent);
                                    }
                                    if (gridBagConstraintsLookupConstraints2.baselineResizeBehavior == Component.BaselineResizeBehavior.CONSTANT_DESCENT) {
                                        short[] sArr3 = sArr;
                                        int i28 = (i18 + i20) - 1;
                                        sArr3[i28] = (short) (sArr3[i28] | (1 << gridBagConstraintsLookupConstraints2.baselineResizeBehavior.ordinal()));
                                        break;
                                    } else {
                                        short[] sArr4 = sArr;
                                        int i29 = i18;
                                        sArr4[i29] = (short) (sArr4[i29] | (1 << gridBagConstraintsLookupConstraints2.baselineResizeBehavior.ordinal()));
                                        break;
                                    }
                                }
                            case 1024:
                            case 1280:
                            case 1536:
                                iArr3[i18] = Math.max(iArr3[i18], gridBagConstraintsLookupConstraints2.minHeight + gridBagConstraintsLookupConstraints2.insets.top + gridBagConstraintsLookupConstraints2.ipady);
                                iArr4[i18] = Math.max(iArr4[i18], gridBagConstraintsLookupConstraints2.insets.bottom);
                                break;
                            case 1792:
                            case 2048:
                            case 2304:
                                iArr4[i18] = Math.max(iArr4[i18], gridBagConstraintsLookupConstraints2.minHeight + gridBagConstraintsLookupConstraints2.insets.bottom + gridBagConstraintsLookupConstraints2.ipady);
                                iArr3[i18] = Math.max(iArr3[i18], gridBagConstraintsLookupConstraints2.insets.top);
                                break;
                        }
                    }
                }
            }
            gridBagLayoutInfo.weightX = new double[iMax2];
            gridBagLayoutInfo.weightY = new double[iMax];
            gridBagLayoutInfo.minWidth = new int[iMax2];
            gridBagLayoutInfo.minHeight = new int[iMax];
            if (this.columnWidths != null) {
                System.arraycopy(this.columnWidths, 0, gridBagLayoutInfo.minWidth, 0, this.columnWidths.length);
            }
            if (this.rowHeights != null) {
                System.arraycopy(this.rowHeights, 0, gridBagLayoutInfo.minHeight, 0, this.rowHeights.length);
            }
            if (this.columnWeights != null) {
                System.arraycopy(this.columnWeights, 0, gridBagLayoutInfo.weightX, 0, Math.min(gridBagLayoutInfo.weightX.length, this.columnWeights.length));
            }
            if (this.rowWeights != null) {
                System.arraycopy(this.rowWeights, 0, gridBagLayoutInfo.weightY, 0, Math.min(gridBagLayoutInfo.weightY.length, this.rowWeights.length));
            }
            int i30 = Integer.MAX_VALUE;
            int i31 = 1;
            while (i31 != Integer.MAX_VALUE) {
                for (Component component3 : components) {
                    if (component3.isVisible()) {
                        GridBagConstraints gridBagConstraintsLookupConstraints3 = lookupConstraints(component3);
                        if (gridBagConstraintsLookupConstraints3.tempWidth == i31) {
                            int i32 = gridBagConstraintsLookupConstraints3.tempX + gridBagConstraintsLookupConstraints3.tempWidth;
                            double d2 = gridBagConstraintsLookupConstraints3.weightx;
                            for (int i33 = gridBagConstraintsLookupConstraints3.tempX; i33 < i32; i33++) {
                                d2 -= gridBagLayoutInfo.weightX[i33];
                            }
                            if (d2 > 0.0d) {
                                double d3 = 0.0d;
                                for (int i34 = gridBagConstraintsLookupConstraints3.tempX; i34 < i32; i34++) {
                                    d3 += gridBagLayoutInfo.weightX[i34];
                                }
                                for (int i35 = gridBagConstraintsLookupConstraints3.tempX; d3 > 0.0d && i35 < i32; i35++) {
                                    double d4 = gridBagLayoutInfo.weightX[i35];
                                    double d5 = (d4 * d2) / d3;
                                    double[] dArr = gridBagLayoutInfo.weightX;
                                    int i36 = i35;
                                    dArr[i36] = dArr[i36] + d5;
                                    d2 -= d5;
                                    d3 -= d4;
                                }
                                double[] dArr2 = gridBagLayoutInfo.weightX;
                                int i37 = i32 - 1;
                                dArr2[i37] = dArr2[i37] + d2;
                            }
                            int i38 = gridBagConstraintsLookupConstraints3.minWidth + gridBagConstraintsLookupConstraints3.ipadx + gridBagConstraintsLookupConstraints3.insets.left + gridBagConstraintsLookupConstraints3.insets.right;
                            for (int i39 = gridBagConstraintsLookupConstraints3.tempX; i39 < i32; i39++) {
                                i38 -= gridBagLayoutInfo.minWidth[i39];
                            }
                            if (i38 > 0) {
                                double d6 = 0.0d;
                                for (int i40 = gridBagConstraintsLookupConstraints3.tempX; i40 < i32; i40++) {
                                    d6 += gridBagLayoutInfo.weightX[i40];
                                }
                                for (int i41 = gridBagConstraintsLookupConstraints3.tempX; d6 > 0.0d && i41 < i32; i41++) {
                                    double d7 = gridBagLayoutInfo.weightX[i41];
                                    int i42 = (int) ((d7 * i38) / d6);
                                    int[] iArr7 = gridBagLayoutInfo.minWidth;
                                    int i43 = i41;
                                    iArr7[i43] = iArr7[i43] + i42;
                                    i38 -= i42;
                                    d6 -= d7;
                                }
                                int[] iArr8 = gridBagLayoutInfo.minWidth;
                                int i44 = i32 - 1;
                                iArr8[i44] = iArr8[i44] + i38;
                            }
                        } else if (gridBagConstraintsLookupConstraints3.tempWidth > i31 && gridBagConstraintsLookupConstraints3.tempWidth < i30) {
                            i30 = gridBagConstraintsLookupConstraints3.tempWidth;
                        }
                        if (gridBagConstraintsLookupConstraints3.tempHeight == i31) {
                            int i45 = gridBagConstraintsLookupConstraints3.tempY + gridBagConstraintsLookupConstraints3.tempHeight;
                            double d8 = gridBagConstraintsLookupConstraints3.weighty;
                            for (int i46 = gridBagConstraintsLookupConstraints3.tempY; i46 < i45; i46++) {
                                d8 -= gridBagLayoutInfo.weightY[i46];
                            }
                            if (d8 > 0.0d) {
                                double d9 = 0.0d;
                                for (int i47 = gridBagConstraintsLookupConstraints3.tempY; i47 < i45; i47++) {
                                    d9 += gridBagLayoutInfo.weightY[i47];
                                }
                                for (int i48 = gridBagConstraintsLookupConstraints3.tempY; d9 > 0.0d && i48 < i45; i48++) {
                                    double d10 = gridBagLayoutInfo.weightY[i48];
                                    double d11 = (d10 * d8) / d9;
                                    double[] dArr3 = gridBagLayoutInfo.weightY;
                                    int i49 = i48;
                                    dArr3[i49] = dArr3[i49] + d11;
                                    d8 -= d11;
                                    d9 -= d10;
                                }
                                double[] dArr4 = gridBagLayoutInfo.weightY;
                                int i50 = i45 - 1;
                                dArr4[i50] = dArr4[i50] + d8;
                            }
                            int i51 = -1;
                            if (z2) {
                                switch (gridBagConstraintsLookupConstraints3.anchor) {
                                    case 256:
                                    case 512:
                                    case 768:
                                        if (gridBagConstraintsLookupConstraints3.ascent >= 0) {
                                            if (gridBagConstraintsLookupConstraints3.tempHeight == 1) {
                                                i51 = iArr3[gridBagConstraintsLookupConstraints3.tempY] + iArr4[gridBagConstraintsLookupConstraints3.tempY];
                                                break;
                                            } else if (gridBagConstraintsLookupConstraints3.baselineResizeBehavior != Component.BaselineResizeBehavior.CONSTANT_DESCENT) {
                                                i51 = iArr3[gridBagConstraintsLookupConstraints3.tempY] + gridBagConstraintsLookupConstraints3.descent;
                                                break;
                                            } else {
                                                i51 = gridBagConstraintsLookupConstraints3.ascent + iArr4[(gridBagConstraintsLookupConstraints3.tempY + gridBagConstraintsLookupConstraints3.tempHeight) - 1];
                                                break;
                                            }
                                        }
                                        break;
                                    case 1024:
                                    case 1280:
                                    case 1536:
                                        i51 = gridBagConstraintsLookupConstraints3.insets.top + gridBagConstraintsLookupConstraints3.minHeight + gridBagConstraintsLookupConstraints3.ipady + iArr4[gridBagConstraintsLookupConstraints3.tempY];
                                        break;
                                    case 1792:
                                    case 2048:
                                    case 2304:
                                        i51 = iArr3[gridBagConstraintsLookupConstraints3.tempY] + gridBagConstraintsLookupConstraints3.minHeight + gridBagConstraintsLookupConstraints3.insets.bottom + gridBagConstraintsLookupConstraints3.ipady;
                                        break;
                                }
                            }
                            if (i51 == -1) {
                                i51 = gridBagConstraintsLookupConstraints3.minHeight + gridBagConstraintsLookupConstraints3.ipady + gridBagConstraintsLookupConstraints3.insets.top + gridBagConstraintsLookupConstraints3.insets.bottom;
                            }
                            for (int i52 = gridBagConstraintsLookupConstraints3.tempY; i52 < i45; i52++) {
                                i51 -= gridBagLayoutInfo.minHeight[i52];
                            }
                            if (i51 > 0) {
                                double d12 = 0.0d;
                                for (int i53 = gridBagConstraintsLookupConstraints3.tempY; i53 < i45; i53++) {
                                    d12 += gridBagLayoutInfo.weightY[i53];
                                }
                                for (int i54 = gridBagConstraintsLookupConstraints3.tempY; d12 > 0.0d && i54 < i45; i54++) {
                                    double d13 = gridBagLayoutInfo.weightY[i54];
                                    int i55 = (int) ((d13 * i51) / d12);
                                    int[] iArr9 = gridBagLayoutInfo.minHeight;
                                    int i56 = i54;
                                    iArr9[i56] = iArr9[i56] + i55;
                                    i51 -= i55;
                                    d12 -= d13;
                                }
                                int[] iArr10 = gridBagLayoutInfo.minHeight;
                                int i57 = i45 - 1;
                                iArr10[i57] = iArr10[i57] + i51;
                            }
                        } else if (gridBagConstraintsLookupConstraints3.tempHeight > i31 && gridBagConstraintsLookupConstraints3.tempHeight < i30) {
                            i30 = gridBagConstraintsLookupConstraints3.tempHeight;
                        }
                    }
                }
                i31 = i30;
                i30 = Integer.MAX_VALUE;
            }
        }
        return gridBagLayoutInfo;
    }

    private boolean calculateBaseline(Component component, GridBagConstraints gridBagConstraints, Dimension dimension) {
        int i2 = gridBagConstraints.anchor;
        if (i2 == 256 || i2 == 512 || i2 == 768) {
            int i3 = dimension.width + gridBagConstraints.ipadx;
            int i4 = dimension.height + gridBagConstraints.ipady;
            gridBagConstraints.ascent = component.getBaseline(i3, i4);
            if (gridBagConstraints.ascent >= 0) {
                int i5 = gridBagConstraints.ascent;
                gridBagConstraints.descent = (i4 - gridBagConstraints.ascent) + gridBagConstraints.insets.bottom;
                gridBagConstraints.ascent += gridBagConstraints.insets.top;
                gridBagConstraints.baselineResizeBehavior = component.getBaselineResizeBehavior();
                gridBagConstraints.centerPadding = 0;
                if (gridBagConstraints.baselineResizeBehavior == Component.BaselineResizeBehavior.CENTER_OFFSET) {
                    int baseline = component.getBaseline(i3, i4 + 1);
                    gridBagConstraints.centerOffset = i5 - (i4 / 2);
                    if (i4 % 2 == 0) {
                        if (i5 != baseline) {
                            gridBagConstraints.centerPadding = 1;
                            return true;
                        }
                        return true;
                    }
                    if (i5 == baseline) {
                        gridBagConstraints.centerOffset--;
                        gridBagConstraints.centerPadding = 1;
                        return true;
                    }
                    return true;
                }
                return true;
            }
            return true;
        }
        gridBagConstraints.ascent = -1;
        return false;
    }

    protected void adjustForGravity(GridBagConstraints gridBagConstraints, Rectangle rectangle) {
        AdjustForGravity(gridBagConstraints, rectangle);
    }

    protected void AdjustForGravity(GridBagConstraints gridBagConstraints, Rectangle rectangle) {
        int i2 = rectangle.f12373y;
        int i3 = rectangle.height;
        if (!this.rightToLeft) {
            rectangle.f12372x += gridBagConstraints.insets.left;
        } else {
            rectangle.f12372x -= rectangle.width - gridBagConstraints.insets.right;
        }
        rectangle.width -= gridBagConstraints.insets.left + gridBagConstraints.insets.right;
        rectangle.f12373y += gridBagConstraints.insets.top;
        rectangle.height -= gridBagConstraints.insets.top + gridBagConstraints.insets.bottom;
        int i4 = 0;
        if (gridBagConstraints.fill != 2 && gridBagConstraints.fill != 1 && rectangle.width > gridBagConstraints.minWidth + gridBagConstraints.ipadx) {
            i4 = rectangle.width - (gridBagConstraints.minWidth + gridBagConstraints.ipadx);
            rectangle.width = gridBagConstraints.minWidth + gridBagConstraints.ipadx;
        }
        int i5 = 0;
        if (gridBagConstraints.fill != 3 && gridBagConstraints.fill != 1 && rectangle.height > gridBagConstraints.minHeight + gridBagConstraints.ipady) {
            i5 = rectangle.height - (gridBagConstraints.minHeight + gridBagConstraints.ipady);
            rectangle.height = gridBagConstraints.minHeight + gridBagConstraints.ipady;
        }
        switch (gridBagConstraints.anchor) {
            case 10:
                rectangle.f12372x += i4 / 2;
                rectangle.f12373y += i5 / 2;
                return;
            case 11:
            case 19:
                rectangle.f12372x += i4 / 2;
                return;
            case 12:
                rectangle.f12372x += i4;
                return;
            case 13:
                rectangle.f12372x += i4;
                rectangle.f12373y += i5 / 2;
                return;
            case 14:
                rectangle.f12372x += i4;
                rectangle.f12373y += i5;
                return;
            case 15:
            case 20:
                rectangle.f12372x += i4 / 2;
                rectangle.f12373y += i5;
                return;
            case 16:
                rectangle.f12373y += i5;
                return;
            case 17:
                rectangle.f12373y += i5 / 2;
                return;
            case 18:
                return;
            case 21:
                if (this.rightToLeft) {
                    rectangle.f12372x += i4;
                }
                rectangle.f12373y += i5 / 2;
                return;
            case 22:
                if (!this.rightToLeft) {
                    rectangle.f12372x += i4;
                }
                rectangle.f12373y += i5 / 2;
                return;
            case 23:
                if (this.rightToLeft) {
                    rectangle.f12372x += i4;
                    return;
                }
                return;
            case 24:
                if (!this.rightToLeft) {
                    rectangle.f12372x += i4;
                    return;
                }
                return;
            case 25:
                if (this.rightToLeft) {
                    rectangle.f12372x += i4;
                }
                rectangle.f12373y += i5;
                return;
            case 26:
                if (!this.rightToLeft) {
                    rectangle.f12372x += i4;
                }
                rectangle.f12373y += i5;
                return;
            case 256:
                rectangle.f12372x += i4 / 2;
                alignOnBaseline(gridBagConstraints, rectangle, i2, i3);
                return;
            case 512:
                if (this.rightToLeft) {
                    rectangle.f12372x += i4;
                }
                alignOnBaseline(gridBagConstraints, rectangle, i2, i3);
                return;
            case 768:
                if (!this.rightToLeft) {
                    rectangle.f12372x += i4;
                }
                alignOnBaseline(gridBagConstraints, rectangle, i2, i3);
                return;
            case 1024:
                rectangle.f12372x += i4 / 2;
                alignAboveBaseline(gridBagConstraints, rectangle, i2, i3);
                return;
            case 1280:
                if (this.rightToLeft) {
                    rectangle.f12372x += i4;
                }
                alignAboveBaseline(gridBagConstraints, rectangle, i2, i3);
                return;
            case 1536:
                if (!this.rightToLeft) {
                    rectangle.f12372x += i4;
                }
                alignAboveBaseline(gridBagConstraints, rectangle, i2, i3);
                return;
            case 1792:
                rectangle.f12372x += i4 / 2;
                alignBelowBaseline(gridBagConstraints, rectangle, i2, i3);
                return;
            case 2048:
                if (this.rightToLeft) {
                    rectangle.f12372x += i4;
                }
                alignBelowBaseline(gridBagConstraints, rectangle, i2, i3);
                return;
            case 2304:
                if (!this.rightToLeft) {
                    rectangle.f12372x += i4;
                }
                alignBelowBaseline(gridBagConstraints, rectangle, i2, i3);
                return;
            default:
                throw new IllegalArgumentException("illegal anchor value");
        }
    }

    private void alignOnBaseline(GridBagConstraints gridBagConstraints, Rectangle rectangle, int i2, int i3) {
        int i4;
        if (gridBagConstraints.ascent >= 0) {
            if (gridBagConstraints.baselineResizeBehavior == Component.BaselineResizeBehavior.CONSTANT_DESCENT) {
                int i5 = (((i2 + i3) - this.layoutInfo.maxDescent[(gridBagConstraints.tempY + gridBagConstraints.tempHeight) - 1]) + gridBagConstraints.descent) - gridBagConstraints.insets.bottom;
                if (!gridBagConstraints.isVerticallyResizable()) {
                    rectangle.f12373y = i5 - gridBagConstraints.minHeight;
                    rectangle.height = gridBagConstraints.minHeight;
                    return;
                } else {
                    rectangle.height = (i5 - i2) - gridBagConstraints.insets.top;
                    return;
                }
            }
            int baseline = gridBagConstraints.ascent;
            if (this.layoutInfo.hasConstantDescent(gridBagConstraints.tempY)) {
                i4 = i3 - this.layoutInfo.maxDescent[gridBagConstraints.tempY];
            } else {
                i4 = this.layoutInfo.maxAscent[gridBagConstraints.tempY];
            }
            if (gridBagConstraints.baselineResizeBehavior == Component.BaselineResizeBehavior.OTHER) {
                boolean z2 = false;
                baseline = this.componentAdjusting.getBaseline(rectangle.width, rectangle.height);
                if (baseline >= 0) {
                    baseline += gridBagConstraints.insets.top;
                }
                if (baseline >= 0 && baseline <= i4) {
                    if (i4 + ((rectangle.height - baseline) - gridBagConstraints.insets.top) <= i3 - gridBagConstraints.insets.bottom) {
                        z2 = true;
                    } else if (gridBagConstraints.isVerticallyResizable()) {
                        int baseline2 = this.componentAdjusting.getBaseline(rectangle.width, ((i3 - gridBagConstraints.insets.bottom) - i4) + baseline);
                        if (baseline2 >= 0) {
                            baseline2 += gridBagConstraints.insets.top;
                        }
                        if (baseline2 >= 0 && baseline2 <= baseline) {
                            rectangle.height = ((i3 - gridBagConstraints.insets.bottom) - i4) + baseline;
                            baseline = baseline2;
                            z2 = true;
                        }
                    }
                }
                if (!z2) {
                    baseline = gridBagConstraints.ascent;
                    rectangle.width = gridBagConstraints.minWidth;
                    rectangle.height = gridBagConstraints.minHeight;
                }
            }
            rectangle.f12373y = ((i2 + i4) - baseline) + gridBagConstraints.insets.top;
            if (gridBagConstraints.isVerticallyResizable()) {
                switch (gridBagConstraints.baselineResizeBehavior) {
                    case CONSTANT_ASCENT:
                        rectangle.height = Math.max(gridBagConstraints.minHeight, ((i2 + i3) - rectangle.f12373y) - gridBagConstraints.insets.bottom);
                        break;
                    case CENTER_OFFSET:
                        int iMin = Math.min((rectangle.f12373y - i2) - gridBagConstraints.insets.top, (((i2 + i3) - rectangle.f12373y) - gridBagConstraints.minHeight) - gridBagConstraints.insets.bottom);
                        int i6 = iMin + iMin;
                        if (i6 > 0 && (((gridBagConstraints.minHeight + gridBagConstraints.centerPadding) + i6) / 2) + gridBagConstraints.centerOffset != i4) {
                            i6--;
                        }
                        rectangle.height = gridBagConstraints.minHeight + i6;
                        rectangle.f12373y = ((i2 + i4) - ((rectangle.height + gridBagConstraints.centerPadding) / 2)) - gridBagConstraints.centerOffset;
                        break;
                }
            }
            return;
        }
        centerVertically(gridBagConstraints, rectangle, i3);
    }

    private void alignAboveBaseline(GridBagConstraints gridBagConstraints, Rectangle rectangle, int i2, int i3) {
        int i4;
        if (this.layoutInfo.hasBaseline(gridBagConstraints.tempY)) {
            if (this.layoutInfo.hasConstantDescent(gridBagConstraints.tempY)) {
                i4 = (i2 + i3) - this.layoutInfo.maxDescent[gridBagConstraints.tempY];
            } else {
                i4 = i2 + this.layoutInfo.maxAscent[gridBagConstraints.tempY];
            }
            if (gridBagConstraints.isVerticallyResizable()) {
                rectangle.f12373y = i2 + gridBagConstraints.insets.top;
                rectangle.height = i4 - rectangle.f12373y;
                return;
            } else {
                rectangle.height = gridBagConstraints.minHeight + gridBagConstraints.ipady;
                rectangle.f12373y = i4 - rectangle.height;
                return;
            }
        }
        centerVertically(gridBagConstraints, rectangle, i3);
    }

    private void alignBelowBaseline(GridBagConstraints gridBagConstraints, Rectangle rectangle, int i2, int i3) {
        if (this.layoutInfo.hasBaseline(gridBagConstraints.tempY)) {
            if (this.layoutInfo.hasConstantDescent(gridBagConstraints.tempY)) {
                rectangle.f12373y = (i2 + i3) - this.layoutInfo.maxDescent[gridBagConstraints.tempY];
            } else {
                rectangle.f12373y = i2 + this.layoutInfo.maxAscent[gridBagConstraints.tempY];
            }
            if (gridBagConstraints.isVerticallyResizable()) {
                rectangle.height = ((i2 + i3) - rectangle.f12373y) - gridBagConstraints.insets.bottom;
                return;
            }
            return;
        }
        centerVertically(gridBagConstraints, rectangle, i3);
    }

    private void centerVertically(GridBagConstraints gridBagConstraints, Rectangle rectangle, int i2) {
        if (!gridBagConstraints.isVerticallyResizable()) {
            rectangle.f12373y += Math.max(0, ((((i2 - gridBagConstraints.insets.top) - gridBagConstraints.insets.bottom) - gridBagConstraints.minHeight) - gridBagConstraints.ipady) / 2);
        }
    }

    protected Dimension getMinSize(Container container, GridBagLayoutInfo gridBagLayoutInfo) {
        return GetMinSize(container, gridBagLayoutInfo);
    }

    protected Dimension GetMinSize(Container container, GridBagLayoutInfo gridBagLayoutInfo) {
        Dimension dimension = new Dimension();
        Insets insets = container.getInsets();
        int i2 = 0;
        for (int i3 = 0; i3 < gridBagLayoutInfo.width; i3++) {
            i2 += gridBagLayoutInfo.minWidth[i3];
        }
        dimension.width = i2 + insets.left + insets.right;
        int i4 = 0;
        for (int i5 = 0; i5 < gridBagLayoutInfo.height; i5++) {
            i4 += gridBagLayoutInfo.minHeight[i5];
        }
        dimension.height = i4 + insets.top + insets.bottom;
        return dimension;
    }

    protected void arrangeGrid(Container container) {
        ArrangeGrid(container);
    }

    protected void ArrangeGrid(Container container) {
        int i2;
        int i3;
        Insets insets = container.getInsets();
        Component[] components = container.getComponents();
        Rectangle rectangle = new Rectangle();
        this.rightToLeft = !container.getComponentOrientation().isLeftToRight();
        if (components.length == 0 && ((this.columnWidths == null || this.columnWidths.length == 0) && (this.rowHeights == null || this.rowHeights.length == 0))) {
            return;
        }
        GridBagLayoutInfo layoutInfo = getLayoutInfo(container, 2);
        Dimension minSize = getMinSize(container, layoutInfo);
        if (container.width < minSize.width || container.height < minSize.height) {
            layoutInfo = getLayoutInfo(container, 1);
            minSize = getMinSize(container, layoutInfo);
        }
        this.layoutInfo = layoutInfo;
        rectangle.width = minSize.width;
        rectangle.height = minSize.height;
        int i4 = container.width - rectangle.width;
        if (i4 != 0) {
            double d2 = 0.0d;
            for (int i5 = 0; i5 < layoutInfo.width; i5++) {
                d2 += layoutInfo.weightX[i5];
            }
            if (d2 > 0.0d) {
                for (int i6 = 0; i6 < layoutInfo.width; i6++) {
                    int i7 = (int) ((i4 * layoutInfo.weightX[i6]) / d2);
                    int[] iArr = layoutInfo.minWidth;
                    int i8 = i6;
                    iArr[i8] = iArr[i8] + i7;
                    rectangle.width += i7;
                    if (layoutInfo.minWidth[i6] < 0) {
                        rectangle.width -= layoutInfo.minWidth[i6];
                        layoutInfo.minWidth[i6] = 0;
                    }
                }
            }
            i2 = container.width - rectangle.width;
        } else {
            i2 = 0;
        }
        int i9 = container.height - rectangle.height;
        if (i9 != 0) {
            double d3 = 0.0d;
            for (int i10 = 0; i10 < layoutInfo.height; i10++) {
                d3 += layoutInfo.weightY[i10];
            }
            if (d3 > 0.0d) {
                for (int i11 = 0; i11 < layoutInfo.height; i11++) {
                    int i12 = (int) ((i9 * layoutInfo.weightY[i11]) / d3);
                    int[] iArr2 = layoutInfo.minHeight;
                    int i13 = i11;
                    iArr2[i13] = iArr2[i13] + i12;
                    rectangle.height += i12;
                    if (layoutInfo.minHeight[i11] < 0) {
                        rectangle.height -= layoutInfo.minHeight[i11];
                        layoutInfo.minHeight[i11] = 0;
                    }
                }
            }
            i3 = container.height - rectangle.height;
        } else {
            i3 = 0;
        }
        layoutInfo.startx = (i2 / 2) + insets.left;
        layoutInfo.starty = (i3 / 2) + insets.top;
        for (Component component : components) {
            if (component.isVisible()) {
                GridBagConstraints gridBagConstraintsLookupConstraints = lookupConstraints(component);
                if (!this.rightToLeft) {
                    rectangle.f12372x = layoutInfo.startx;
                    for (int i14 = 0; i14 < gridBagConstraintsLookupConstraints.tempX; i14++) {
                        rectangle.f12372x += layoutInfo.minWidth[i14];
                    }
                } else {
                    rectangle.f12372x = container.width - ((i2 / 2) + insets.right);
                    for (int i15 = 0; i15 < gridBagConstraintsLookupConstraints.tempX; i15++) {
                        rectangle.f12372x -= layoutInfo.minWidth[i15];
                    }
                }
                rectangle.f12373y = layoutInfo.starty;
                for (int i16 = 0; i16 < gridBagConstraintsLookupConstraints.tempY; i16++) {
                    rectangle.f12373y += layoutInfo.minHeight[i16];
                }
                rectangle.width = 0;
                for (int i17 = gridBagConstraintsLookupConstraints.tempX; i17 < gridBagConstraintsLookupConstraints.tempX + gridBagConstraintsLookupConstraints.tempWidth; i17++) {
                    rectangle.width += layoutInfo.minWidth[i17];
                }
                rectangle.height = 0;
                for (int i18 = gridBagConstraintsLookupConstraints.tempY; i18 < gridBagConstraintsLookupConstraints.tempY + gridBagConstraintsLookupConstraints.tempHeight; i18++) {
                    rectangle.height += layoutInfo.minHeight[i18];
                }
                this.componentAdjusting = component;
                adjustForGravity(gridBagConstraintsLookupConstraints, rectangle);
                if (rectangle.f12372x < 0) {
                    rectangle.width += rectangle.f12372x;
                    rectangle.f12372x = 0;
                }
                if (rectangle.f12373y < 0) {
                    rectangle.height += rectangle.f12373y;
                    rectangle.f12373y = 0;
                }
                if (rectangle.width <= 0 || rectangle.height <= 0) {
                    component.setBounds(0, 0, 0, 0);
                } else if (component.f12361x != rectangle.f12372x || component.f12362y != rectangle.f12373y || component.width != rectangle.width || component.height != rectangle.height) {
                    component.setBounds(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
                }
            }
        }
    }
}
