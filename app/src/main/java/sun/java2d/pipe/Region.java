package sun.java2d.pipe;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.RectangularShape;

/* loaded from: rt.jar:sun/java2d/pipe/Region.class */
public class Region {
    static final int INIT_SIZE = 50;
    static final int GROW_SIZE = 50;
    public static final Region EMPTY_REGION = new ImmutableRegion(0, 0, 0, 0);
    public static final Region WHOLE_REGION = new ImmutableRegion(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    int lox;
    int loy;
    int hix;
    int hiy;
    int endIndex;
    int[] bands;
    static final int INCLUDE_A = 1;
    static final int INCLUDE_B = 2;
    static final int INCLUDE_COMMON = 4;

    private static native void initIDs();

    /* loaded from: rt.jar:sun/java2d/pipe/Region$ImmutableRegion.class */
    private static final class ImmutableRegion extends Region {
        protected ImmutableRegion(int i2, int i3, int i4, int i5) {
            super(i2, i3, i4, i5);
        }

        @Override // sun.java2d.pipe.Region
        public void appendSpans(SpanIterator spanIterator) {
        }

        @Override // sun.java2d.pipe.Region
        public void setOutputArea(Rectangle rectangle) {
        }

        @Override // sun.java2d.pipe.Region
        public void setOutputAreaXYWH(int i2, int i3, int i4, int i5) {
        }

        @Override // sun.java2d.pipe.Region
        public void setOutputArea(int[] iArr) {
        }

        @Override // sun.java2d.pipe.Region
        public void setOutputAreaXYXY(int i2, int i3, int i4, int i5) {
        }
    }

    static {
        initIDs();
    }

    public static int dimAdd(int i2, int i3) {
        if (i3 <= 0) {
            return i2;
        }
        int i4 = i3 + i2;
        if (i4 < i2) {
            return Integer.MAX_VALUE;
        }
        return i4;
    }

    public static int clipAdd(int i2, int i3) {
        int i4 = i2 + i3;
        if ((i4 > i2) != (i3 > 0)) {
            i4 = i3 < 0 ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        }
        return i4;
    }

    public static int clipScale(int i2, double d2) {
        if (d2 == 1.0d) {
            return i2;
        }
        double d3 = i2 * d2;
        if (d3 < -2.147483648E9d) {
            return Integer.MIN_VALUE;
        }
        if (d3 > 2.147483647E9d) {
            return Integer.MAX_VALUE;
        }
        return (int) Math.round(d3);
    }

    protected Region(int i2, int i3, int i4, int i5) {
        this.lox = i2;
        this.loy = i3;
        this.hix = i4;
        this.hiy = i5;
    }

    private Region(int i2, int i3, int i4, int i5, int[] iArr, int i6) {
        this.lox = i2;
        this.loy = i3;
        this.hix = i4;
        this.hiy = i5;
        this.bands = iArr;
        this.endIndex = i6;
    }

    public static Region getInstance(Shape shape, AffineTransform affineTransform) {
        return getInstance(WHOLE_REGION, false, shape, affineTransform);
    }

    public static Region getInstance(Region region, Shape shape, AffineTransform affineTransform) {
        return getInstance(region, false, shape, affineTransform);
    }

    public static Region getInstance(Region region, boolean z2, Shape shape, AffineTransform affineTransform) {
        if ((shape instanceof RectangularShape) && ((RectangularShape) shape).isEmpty()) {
            return EMPTY_REGION;
        }
        int[] iArr = new int[4];
        ShapeSpanIterator shapeSpanIterator = new ShapeSpanIterator(z2);
        try {
            shapeSpanIterator.setOutputArea(region);
            shapeSpanIterator.appendPath(shape.getPathIterator(affineTransform));
            shapeSpanIterator.getPathBox(iArr);
            Region region2 = getInstance(iArr);
            region2.appendSpans(shapeSpanIterator);
            shapeSpanIterator.dispose();
            return region2;
        } catch (Throwable th) {
            shapeSpanIterator.dispose();
            throw th;
        }
    }

    static Region getInstance(int i2, int i3, int i4, int i5, int[] iArr) {
        int i6 = iArr[0];
        int i7 = iArr[1];
        if (i5 <= i3 || i4 <= i2 || i7 <= i6) {
            return EMPTY_REGION;
        }
        int[] iArr2 = new int[(i7 - i6) * 5];
        int i8 = 0;
        int i9 = 2;
        for (int i10 = i6; i10 < i7; i10++) {
            int i11 = i9;
            int i12 = i9 + 1;
            int iMax = Math.max(clipAdd(i2, iArr[i11]), i2);
            i9 = i12 + 1;
            int iMin = Math.min(clipAdd(i2, iArr[i12]), i4);
            if (iMax < iMin) {
                int iMax2 = Math.max(clipAdd(i3, i10), i3);
                int iMin2 = Math.min(clipAdd(iMax2, 1), i5);
                if (iMax2 < iMin2) {
                    int i13 = i8;
                    int i14 = i8 + 1;
                    iArr2[i13] = iMax2;
                    int i15 = i14 + 1;
                    iArr2[i14] = iMin2;
                    int i16 = i15 + 1;
                    iArr2[i15] = 1;
                    int i17 = i16 + 1;
                    iArr2[i16] = iMax;
                    i8 = i17 + 1;
                    iArr2[i17] = iMin;
                }
            }
        }
        return i8 != 0 ? new Region(i2, i3, i4, i5, iArr2, i8) : EMPTY_REGION;
    }

    public static Region getInstance(Rectangle rectangle) {
        return getInstanceXYWH(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
    }

    public static Region getInstanceXYWH(int i2, int i3, int i4, int i5) {
        return getInstanceXYXY(i2, i3, dimAdd(i2, i4), dimAdd(i3, i5));
    }

    public static Region getInstance(int[] iArr) {
        return new Region(iArr[0], iArr[1], iArr[2], iArr[3]);
    }

    public static Region getInstanceXYXY(int i2, int i3, int i4, int i5) {
        return new Region(i2, i3, i4, i5);
    }

    public void setOutputArea(Rectangle rectangle) {
        setOutputAreaXYWH(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
    }

    public void setOutputAreaXYWH(int i2, int i3, int i4, int i5) {
        setOutputAreaXYXY(i2, i3, dimAdd(i2, i4), dimAdd(i3, i5));
    }

    public void setOutputArea(int[] iArr) {
        this.lox = iArr[0];
        this.loy = iArr[1];
        this.hix = iArr[2];
        this.hiy = iArr[3];
    }

    public void setOutputAreaXYXY(int i2, int i3, int i4, int i5) {
        this.lox = i2;
        this.loy = i3;
        this.hix = i4;
        this.hiy = i5;
    }

    public void appendSpans(SpanIterator spanIterator) {
        int[] iArr = new int[6];
        while (spanIterator.nextSpan(iArr)) {
            appendSpan(iArr);
        }
        endRow(iArr);
        calcBBox();
    }

    public Region getScaledRegion(double d2, double d3) {
        if (d2 == 0.0d || d3 == 0.0d || this == EMPTY_REGION) {
            return EMPTY_REGION;
        }
        if ((d2 == 1.0d && d3 == 1.0d) || this == WHOLE_REGION) {
            return this;
        }
        Region region = new Region(clipScale(this.lox, d2), clipScale(this.loy, d3), clipScale(this.hix, d2), clipScale(this.hiy, d3));
        int[] iArr = this.bands;
        if (iArr != null) {
            int i2 = this.endIndex;
            int[] iArr2 = new int[i2];
            int i3 = 0;
            int i4 = 0;
            while (i3 < i2) {
                int i5 = i4;
                int i6 = i4 + 1;
                int i7 = i3;
                int i8 = i3 + 1;
                int iClipScale = clipScale(iArr[i7], d3);
                iArr2[i5] = iClipScale;
                int i9 = i6 + 1;
                int i10 = i8 + 1;
                int iClipScale2 = clipScale(iArr[i8], d3);
                iArr2[i6] = iClipScale2;
                i4 = i9 + 1;
                i3 = i10 + 1;
                int i11 = iArr[i10];
                int i12 = i11;
                iArr2[i9] = i11;
                if (iClipScale < iClipScale2) {
                    while (true) {
                        i12--;
                        if (i12 < 0) {
                            break;
                        }
                        int i13 = i3;
                        int i14 = i3 + 1;
                        int iClipScale3 = clipScale(iArr[i13], d2);
                        i3 = i14 + 1;
                        int iClipScale4 = clipScale(iArr[i14], d2);
                        if (iClipScale3 < iClipScale4) {
                            int i15 = i4;
                            int i16 = i4 + 1;
                            iArr2[i15] = iClipScale3;
                            i4 = i16 + 1;
                            iArr2[i16] = iClipScale4;
                        }
                    }
                } else {
                    i3 += i12 * 2;
                }
                if (i4 > i4) {
                    iArr2[i4 - 1] = (i4 - i4) / 2;
                } else {
                    i4 -= 3;
                }
            }
            if (i4 <= 5) {
                if (i4 < 5) {
                    region.hiy = 0;
                    region.hix = 0;
                    region.loy = 0;
                    region.lox = 0;
                } else {
                    region.loy = iArr2[0];
                    region.hiy = iArr2[1];
                    region.lox = iArr2[3];
                    region.hix = iArr2[4];
                }
            } else {
                region.endIndex = i4;
                region.bands = iArr2;
            }
        }
        return region;
    }

    public Region getTranslatedRegion(int i2, int i3) {
        if ((i2 | i3) == 0) {
            return this;
        }
        int i4 = this.lox + i2;
        int i5 = this.loy + i3;
        int i6 = this.hix + i2;
        int i7 = this.hiy + i3;
        if ((i4 > this.lox) == (i2 > 0)) {
            if ((i5 > this.loy) == (i3 > 0)) {
                if ((i6 > this.hix) == (i2 > 0)) {
                    if ((i7 > this.hiy) == (i3 > 0)) {
                        Region region = new Region(i4, i5, i6, i7);
                        int[] iArr = this.bands;
                        if (iArr != null) {
                            int i8 = this.endIndex;
                            region.endIndex = i8;
                            int[] iArr2 = new int[i8];
                            region.bands = iArr2;
                            int i9 = 0;
                            while (i9 < i8) {
                                iArr2[i9] = iArr[i9] + i3;
                                int i10 = i9 + 1;
                                iArr2[i10] = iArr[i10] + i3;
                                int i11 = i10 + 1;
                                int i12 = iArr[i11];
                                int i13 = i12;
                                iArr2[i11] = i12;
                                while (true) {
                                    i9 = i11 + 1;
                                    i13--;
                                    if (i13 >= 0) {
                                        iArr2[i9] = iArr[i9] + i2;
                                        i11 = i9 + 1;
                                        iArr2[i11] = iArr[i11] + i2;
                                    }
                                }
                            }
                        }
                        return region;
                    }
                }
            }
        }
        return getSafeTranslatedRegion(i2, i3);
    }

    private Region getSafeTranslatedRegion(int i2, int i3) {
        Region region = new Region(clipAdd(this.lox, i2), clipAdd(this.loy, i3), clipAdd(this.hix, i2), clipAdd(this.hiy, i3));
        int[] iArr = this.bands;
        if (iArr != null) {
            int i4 = this.endIndex;
            int[] iArr2 = new int[i4];
            int i5 = 0;
            int i6 = 0;
            while (i5 < i4) {
                int i7 = i6;
                int i8 = i6 + 1;
                int i9 = i5;
                int i10 = i5 + 1;
                int iClipAdd = clipAdd(iArr[i9], i3);
                iArr2[i7] = iClipAdd;
                int i11 = i8 + 1;
                int i12 = i10 + 1;
                int iClipAdd2 = clipAdd(iArr[i10], i3);
                iArr2[i8] = iClipAdd2;
                i6 = i11 + 1;
                i5 = i12 + 1;
                int i13 = iArr[i12];
                int i14 = i13;
                iArr2[i11] = i13;
                if (iClipAdd < iClipAdd2) {
                    while (true) {
                        i14--;
                        if (i14 < 0) {
                            break;
                        }
                        int i15 = i5;
                        int i16 = i5 + 1;
                        int iClipAdd3 = clipAdd(iArr[i15], i2);
                        i5 = i16 + 1;
                        int iClipAdd4 = clipAdd(iArr[i16], i2);
                        if (iClipAdd3 < iClipAdd4) {
                            int i17 = i6;
                            int i18 = i6 + 1;
                            iArr2[i17] = iClipAdd3;
                            i6 = i18 + 1;
                            iArr2[i18] = iClipAdd4;
                        }
                    }
                } else {
                    i5 += i14 * 2;
                }
                if (i6 > i6) {
                    iArr2[i6 - 1] = (i6 - i6) / 2;
                } else {
                    i6 -= 3;
                }
            }
            if (i6 <= 5) {
                if (i6 < 5) {
                    region.hiy = 0;
                    region.hix = 0;
                    region.loy = 0;
                    region.lox = 0;
                } else {
                    region.loy = iArr2[0];
                    region.hiy = iArr2[1];
                    region.lox = iArr2[3];
                    region.hix = iArr2[4];
                }
            } else {
                region.endIndex = i6;
                region.bands = iArr2;
            }
        }
        return region;
    }

    public Region getIntersection(Rectangle rectangle) {
        return getIntersectionXYWH(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
    }

    public Region getIntersectionXYWH(int i2, int i3, int i4, int i5) {
        return getIntersectionXYXY(i2, i3, dimAdd(i2, i4), dimAdd(i3, i5));
    }

    public Region getIntersectionXYXY(int i2, int i3, int i4, int i5) {
        if (isInsideXYXY(i2, i3, i4, i5)) {
            return this;
        }
        Region region = new Region(i2 < this.lox ? this.lox : i2, i3 < this.loy ? this.loy : i3, i4 > this.hix ? this.hix : i4, i5 > this.hiy ? this.hiy : i5);
        if (this.bands != null) {
            region.appendSpans(getSpanIterator());
        }
        return region;
    }

    public Region getIntersection(Region region) {
        if (isInsideQuickCheck(region)) {
            return this;
        }
        if (region.isInsideQuickCheck(this)) {
            return region;
        }
        Region region2 = new Region(region.lox < this.lox ? this.lox : region.lox, region.loy < this.loy ? this.loy : region.loy, region.hix > this.hix ? this.hix : region.hix, region.hiy > this.hiy ? this.hiy : region.hiy);
        if (!region2.isEmpty()) {
            region2.filterSpans(this, region, 4);
        }
        return region2;
    }

    public Region getUnion(Region region) {
        if (region.isEmpty() || region.isInsideQuickCheck(this)) {
            return this;
        }
        if (isEmpty() || isInsideQuickCheck(region)) {
            return region;
        }
        Region region2 = new Region(region.lox > this.lox ? this.lox : region.lox, region.loy > this.loy ? this.loy : region.loy, region.hix < this.hix ? this.hix : region.hix, region.hiy < this.hiy ? this.hiy : region.hiy);
        region2.filterSpans(this, region, 7);
        return region2;
    }

    public Region getDifference(Region region) {
        if (!region.intersectsQuickCheck(this)) {
            return this;
        }
        if (isInsideQuickCheck(region)) {
            return EMPTY_REGION;
        }
        Region region2 = new Region(this.lox, this.loy, this.hix, this.hiy);
        region2.filterSpans(this, region, 1);
        return region2;
    }

    public Region getExclusiveOr(Region region) {
        if (region.isEmpty()) {
            return this;
        }
        if (isEmpty()) {
            return region;
        }
        Region region2 = new Region(region.lox > this.lox ? this.lox : region.lox, region.loy > this.loy ? this.loy : region.loy, region.hix < this.hix ? this.hix : region.hix, region.hiy < this.hiy ? this.hiy : region.hiy);
        region2.filterSpans(this, region, 3);
        return region2;
    }

    private void filterSpans(Region region, Region region2, int i2) {
        int iMin;
        int iMin2;
        boolean z2;
        int[] iArr = region.bands;
        int[] iArr2 = region2.bands;
        if (iArr == null) {
            iArr = new int[]{region.loy, region.hiy, 1, region.lox, region.hix};
        }
        if (iArr2 == null) {
            iArr2 = new int[]{region2.loy, region2.hiy, 1, region2.lox, region2.hix};
        }
        int[] iArr3 = new int[6];
        int i3 = 0 + 1;
        int i4 = iArr[0];
        int i5 = i3 + 1;
        int i6 = iArr[i3];
        int i7 = i5 + 1;
        int i8 = i7 + (2 * iArr[i5]);
        int i9 = 0 + 1;
        int i10 = iArr2[0];
        int i11 = i9 + 1;
        int i12 = iArr2[i9];
        int i13 = i11 + 1;
        int i14 = i13 + (2 * iArr2[i11]);
        int iMin3 = this.loy;
        while (iMin3 < this.hiy) {
            if (iMin3 >= i6) {
                if (i8 < region.endIndex) {
                    int i15 = i8;
                    int i16 = i15 + 1;
                    i4 = iArr[i15];
                    int i17 = i16 + 1;
                    i6 = iArr[i16];
                    i7 = i17 + 1;
                    i8 = i7 + (2 * iArr[i17]);
                } else {
                    if ((i2 & 2) == 0) {
                        break;
                    }
                    int i18 = this.hiy;
                    i6 = i18;
                    i4 = i18;
                }
            } else if (iMin3 >= i12) {
                if (i14 < region2.endIndex) {
                    int i19 = i14;
                    int i20 = i19 + 1;
                    i10 = iArr2[i19];
                    int i21 = i20 + 1;
                    i12 = iArr2[i20];
                    i13 = i21 + 1;
                    i14 = i13 + (2 * iArr2[i21]);
                } else {
                    if ((i2 & 1) == 0) {
                        break;
                    }
                    int i22 = this.hiy;
                    i12 = i22;
                    i10 = i22;
                }
            } else {
                if (iMin3 < i10) {
                    if (iMin3 < i4) {
                        iMin3 = Math.min(i4, i10);
                    } else {
                        iMin = Math.min(i6, i10);
                        if ((i2 & 1) != 0) {
                            iArr3[1] = iMin3;
                            iArr3[3] = iMin;
                            int i23 = i7;
                            while (i23 < i8) {
                                int i24 = i23;
                                int i25 = i23 + 1;
                                iArr3[0] = iArr[i24];
                                i23 = i25 + 1;
                                iArr3[2] = iArr[i25];
                                appendSpan(iArr3);
                            }
                        }
                    }
                } else if (iMin3 < i4) {
                    iMin = Math.min(i12, i4);
                    if ((i2 & 2) != 0) {
                        iArr3[1] = iMin3;
                        iArr3[3] = iMin;
                        int i26 = i13;
                        while (i26 < i14) {
                            int i27 = i26;
                            int i28 = i26 + 1;
                            iArr3[0] = iArr2[i27];
                            i26 = i28 + 1;
                            iArr3[2] = iArr2[i28];
                            appendSpan(iArr3);
                        }
                    }
                } else {
                    iMin = Math.min(i6, i12);
                    iArr3[1] = iMin3;
                    iArr3[3] = iMin;
                    int i29 = i7;
                    int i30 = i13;
                    int i31 = i29 + 1;
                    int i32 = iArr[i29];
                    int i33 = i31 + 1;
                    int i34 = iArr[i31];
                    int i35 = i30 + 1;
                    int i36 = iArr2[i30];
                    int i37 = i35 + 1;
                    int i38 = iArr2[i35];
                    int iMin4 = Math.min(i32, i36);
                    if (iMin4 < this.lox) {
                        iMin4 = this.lox;
                    }
                    while (iMin4 < this.hix) {
                        if (iMin4 >= i34) {
                            if (i33 < i8) {
                                int i39 = i33;
                                int i40 = i33 + 1;
                                i32 = iArr[i39];
                                i33 = i40 + 1;
                                i34 = iArr[i40];
                            } else {
                                if ((i2 & 2) == 0) {
                                    break;
                                }
                                int i41 = this.hix;
                                i34 = i41;
                                i32 = i41;
                            }
                        } else if (iMin4 >= i38) {
                            if (i37 < i14) {
                                int i42 = i37;
                                int i43 = i37 + 1;
                                i36 = iArr2[i42];
                                i37 = i43 + 1;
                                i38 = iArr2[i43];
                            } else {
                                if ((i2 & 1) == 0) {
                                    break;
                                }
                                int i44 = this.hix;
                                i38 = i44;
                                i36 = i44;
                            }
                        } else {
                            if (iMin4 < i36) {
                                if (iMin4 < i32) {
                                    iMin2 = Math.min(i32, i36);
                                    z2 = false;
                                } else {
                                    iMin2 = Math.min(i34, i36);
                                    z2 = (i2 & 1) != 0;
                                }
                            } else if (iMin4 < i32) {
                                iMin2 = Math.min(i32, i38);
                                z2 = (i2 & 2) != 0;
                            } else {
                                iMin2 = Math.min(i34, i38);
                                z2 = (i2 & 4) != 0;
                            }
                            if (z2) {
                                iArr3[0] = iMin4;
                                iArr3[2] = iMin2;
                                appendSpan(iArr3);
                            }
                            iMin4 = iMin2;
                        }
                    }
                }
                iMin3 = iMin;
            }
        }
        endRow(iArr3);
        calcBBox();
    }

    public Region getBoundsIntersection(Rectangle rectangle) {
        return getBoundsIntersectionXYWH(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
    }

    public Region getBoundsIntersectionXYWH(int i2, int i3, int i4, int i5) {
        return getBoundsIntersectionXYXY(i2, i3, dimAdd(i2, i4), dimAdd(i3, i5));
    }

    public Region getBoundsIntersectionXYXY(int i2, int i3, int i4, int i5) {
        if (this.bands == null && this.lox >= i2 && this.loy >= i3 && this.hix <= i4 && this.hiy <= i5) {
            return this;
        }
        return new Region(i2 < this.lox ? this.lox : i2, i3 < this.loy ? this.loy : i3, i4 > this.hix ? this.hix : i4, i5 > this.hiy ? this.hiy : i5);
    }

    public Region getBoundsIntersection(Region region) {
        if (encompasses(region)) {
            return region;
        }
        if (region.encompasses(this)) {
            return this;
        }
        return new Region(region.lox < this.lox ? this.lox : region.lox, region.loy < this.loy ? this.loy : region.loy, region.hix > this.hix ? this.hix : region.hix, region.hiy > this.hiy ? this.hiy : region.hiy);
    }

    private void appendSpan(int[] iArr) {
        int i2 = iArr[0];
        int i3 = i2;
        if (i2 < this.lox) {
            i3 = this.lox;
        }
        int i4 = iArr[1];
        int i5 = i4;
        if (i4 < this.loy) {
            i5 = this.loy;
        }
        int i6 = iArr[2];
        int i7 = i6;
        if (i6 > this.hix) {
            i7 = this.hix;
        }
        int i8 = iArr[3];
        int i9 = i8;
        if (i8 > this.hiy) {
            i9 = this.hiy;
        }
        if (i7 <= i3 || i9 <= i5) {
            return;
        }
        int i10 = iArr[4];
        if (this.endIndex == 0 || i5 >= this.bands[i10 + 1]) {
            if (this.bands == null) {
                this.bands = new int[50];
            } else {
                needSpace(5);
                endRow(iArr);
                i10 = iArr[4];
            }
            int[] iArr2 = this.bands;
            int i11 = this.endIndex;
            this.endIndex = i11 + 1;
            iArr2[i11] = i5;
            int[] iArr3 = this.bands;
            int i12 = this.endIndex;
            this.endIndex = i12 + 1;
            iArr3[i12] = i9;
            int[] iArr4 = this.bands;
            int i13 = this.endIndex;
            this.endIndex = i13 + 1;
            iArr4[i13] = 0;
        } else if (i5 == this.bands[i10] && i9 == this.bands[i10 + 1] && i3 >= this.bands[this.endIndex - 1]) {
            if (i3 == this.bands[this.endIndex - 1]) {
                this.bands[this.endIndex - 1] = i7;
                return;
            }
            needSpace(2);
        } else {
            throw new InternalError("bad span");
        }
        int[] iArr5 = this.bands;
        int i14 = this.endIndex;
        this.endIndex = i14 + 1;
        iArr5[i14] = i3;
        int[] iArr6 = this.bands;
        int i15 = this.endIndex;
        this.endIndex = i15 + 1;
        iArr6[i15] = i7;
        int[] iArr7 = this.bands;
        int i16 = i10 + 2;
        iArr7[i16] = iArr7[i16] + 1;
    }

    private void needSpace(int i2) {
        if (this.endIndex + i2 >= this.bands.length) {
            int[] iArr = new int[this.bands.length + 50];
            System.arraycopy(this.bands, 0, iArr, 0, this.endIndex);
            this.bands = iArr;
        }
    }

    private void endRow(int[] iArr) {
        int i2 = iArr[4];
        int i3 = iArr[5];
        if (i2 > i3) {
            int[] iArr2 = this.bands;
            if (iArr2[i3 + 1] == iArr2[i2] && iArr2[i3 + 2] == iArr2[i2 + 2]) {
                int i4 = iArr2[i2 + 2] * 2;
                int i5 = i2 + 3;
                int i6 = i3 + 3;
                while (i4 > 0) {
                    int i7 = i5;
                    i5++;
                    int i8 = i6;
                    i6++;
                    if (iArr2[i7] != iArr2[i8]) {
                        break;
                    } else {
                        i4--;
                    }
                }
                if (i4 == 0) {
                    iArr2[iArr[5] + 1] = iArr2[i6 + 1];
                    this.endIndex = i6;
                    return;
                }
            }
        }
        iArr[5] = iArr[4];
        iArr[4] = this.endIndex;
    }

    private void calcBBox() {
        int[] iArr = this.bands;
        if (this.endIndex <= 5) {
            if (this.endIndex == 0) {
                this.hiy = 0;
                this.hix = 0;
                this.loy = 0;
                this.lox = 0;
            } else {
                this.loy = iArr[0];
                this.hiy = iArr[1];
                this.lox = iArr[3];
                this.hix = iArr[4];
                this.endIndex = 0;
            }
            this.bands = null;
            return;
        }
        int i2 = this.hix;
        int i3 = this.lox;
        int i4 = 0;
        int i5 = 0;
        while (i5 < this.endIndex) {
            i4 = i5;
            int i6 = iArr[i5 + 2];
            int i7 = i5 + 3;
            if (i2 > iArr[i7]) {
                i2 = iArr[i7];
            }
            i5 = i7 + (i6 * 2);
            if (i3 < iArr[i5 - 1]) {
                i3 = iArr[i5 - 1];
            }
        }
        this.lox = i2;
        this.loy = iArr[0];
        this.hix = i3;
        this.hiy = iArr[i4 + 1];
    }

    public final int getLoX() {
        return this.lox;
    }

    public final int getLoY() {
        return this.loy;
    }

    public final int getHiX() {
        return this.hix;
    }

    public final int getHiY() {
        return this.hiy;
    }

    public final int getWidth() {
        if (this.hix < this.lox) {
            return 0;
        }
        int i2 = this.hix - this.lox;
        int i3 = i2;
        if (i2 < 0) {
            i3 = Integer.MAX_VALUE;
        }
        return i3;
    }

    public final int getHeight() {
        if (this.hiy < this.loy) {
            return 0;
        }
        int i2 = this.hiy - this.loy;
        int i3 = i2;
        if (i2 < 0) {
            i3 = Integer.MAX_VALUE;
        }
        return i3;
    }

    public boolean isEmpty() {
        return this.hix <= this.lox || this.hiy <= this.loy;
    }

    public boolean isRectangular() {
        return this.bands == null;
    }

    public boolean contains(int i2, int i3) {
        if (i2 < this.lox || i2 >= this.hix || i3 < this.loy || i3 >= this.hiy) {
            return false;
        }
        if (this.bands == null) {
            return true;
        }
        int i4 = 0;
        while (true) {
            int i5 = i4;
            if (i5 < this.endIndex) {
                int i6 = i5 + 1;
                if (i3 < this.bands[i5]) {
                    return false;
                }
                int i7 = i6 + 1;
                if (i3 >= this.bands[i6]) {
                    i4 = i7 + 1 + (this.bands[i7] * 2);
                } else {
                    int i8 = i7 + 1;
                    int i9 = i8 + (this.bands[i7] * 2);
                    while (i8 < i9) {
                        int i10 = i8;
                        int i11 = i8 + 1;
                        if (i2 < this.bands[i10]) {
                            return false;
                        }
                        i8 = i11 + 1;
                        if (i2 < this.bands[i11]) {
                            return true;
                        }
                    }
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    public boolean isInsideXYWH(int i2, int i3, int i4, int i5) {
        return isInsideXYXY(i2, i3, dimAdd(i2, i4), dimAdd(i3, i5));
    }

    public boolean isInsideXYXY(int i2, int i3, int i4, int i5) {
        return this.lox >= i2 && this.loy >= i3 && this.hix <= i4 && this.hiy <= i5;
    }

    public boolean isInsideQuickCheck(Region region) {
        return region.bands == null && region.lox <= this.lox && region.loy <= this.loy && region.hix >= this.hix && region.hiy >= this.hiy;
    }

    public boolean intersectsQuickCheckXYXY(int i2, int i3, int i4, int i5) {
        return i4 > this.lox && i2 < this.hix && i5 > this.loy && i3 < this.hiy;
    }

    public boolean intersectsQuickCheck(Region region) {
        return region.hix > this.lox && region.lox < this.hix && region.hiy > this.loy && region.loy < this.hiy;
    }

    public boolean encompasses(Region region) {
        return this.bands == null && this.lox <= region.lox && this.loy <= region.loy && this.hix >= region.hix && this.hiy >= region.hiy;
    }

    public boolean encompassesXYWH(int i2, int i3, int i4, int i5) {
        return encompassesXYXY(i2, i3, dimAdd(i2, i4), dimAdd(i3, i5));
    }

    public boolean encompassesXYXY(int i2, int i3, int i4, int i5) {
        return this.bands == null && this.lox <= i2 && this.loy <= i3 && this.hix >= i4 && this.hiy >= i5;
    }

    public void getBounds(int[] iArr) {
        iArr[0] = this.lox;
        iArr[1] = this.loy;
        iArr[2] = this.hix;
        iArr[3] = this.hiy;
    }

    public void clipBoxToBounds(int[] iArr) {
        if (iArr[0] < this.lox) {
            iArr[0] = this.lox;
        }
        if (iArr[1] < this.loy) {
            iArr[1] = this.loy;
        }
        if (iArr[2] > this.hix) {
            iArr[2] = this.hix;
        }
        if (iArr[3] > this.hiy) {
            iArr[3] = this.hiy;
        }
    }

    public RegionIterator getIterator() {
        return new RegionIterator(this);
    }

    public SpanIterator getSpanIterator() {
        return new RegionSpanIterator(this);
    }

    public SpanIterator getSpanIterator(int[] iArr) {
        SpanIterator spanIterator = getSpanIterator();
        spanIterator.intersectClipBox(iArr[0], iArr[1], iArr[2], iArr[3]);
        return spanIterator;
    }

    public SpanIterator filter(SpanIterator spanIterator) {
        if (this.bands == null) {
            spanIterator.intersectClipBox(this.lox, this.loy, this.hix, this.hiy);
        } else {
            spanIterator = new RegionClipSpanIterator(this, spanIterator);
        }
        return spanIterator;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Region[[");
        stringBuffer.append(this.lox);
        stringBuffer.append(", ");
        stringBuffer.append(this.loy);
        stringBuffer.append(" => ");
        stringBuffer.append(this.hix);
        stringBuffer.append(", ");
        stringBuffer.append(this.hiy);
        stringBuffer.append("]");
        if (this.bands != null) {
            int i2 = 0;
            while (i2 < this.endIndex) {
                stringBuffer.append("y{");
                int i3 = i2;
                int i4 = i2 + 1;
                stringBuffer.append(this.bands[i3]);
                stringBuffer.append(",");
                int i5 = i4 + 1;
                stringBuffer.append(this.bands[i4]);
                stringBuffer.append("}[");
                i2 = i5 + 1;
                int i6 = i2 + (this.bands[i5] * 2);
                while (i2 < i6) {
                    stringBuffer.append("x(");
                    int i7 = i2;
                    int i8 = i2 + 1;
                    stringBuffer.append(this.bands[i7]);
                    stringBuffer.append(", ");
                    i2 = i8 + 1;
                    stringBuffer.append(this.bands[i8]);
                    stringBuffer.append(")");
                }
                stringBuffer.append("]");
            }
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }

    public int hashCode() {
        if (isEmpty()) {
            return 0;
        }
        return (this.lox * 3) + (this.loy * 5) + (this.hix * 7) + (this.hiy * 9);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Region)) {
            return false;
        }
        Region region = (Region) obj;
        if (isEmpty()) {
            return region.isEmpty();
        }
        if (region.isEmpty() || region.lox != this.lox || region.loy != this.loy || region.hix != this.hix || region.hiy != this.hiy) {
            return false;
        }
        if (this.bands == null) {
            return region.bands == null;
        }
        if (region.bands == null || this.endIndex != region.endIndex) {
            return false;
        }
        int[] iArr = this.bands;
        int[] iArr2 = region.bands;
        for (int i2 = 0; i2 < this.endIndex; i2++) {
            if (iArr[i2] != iArr2[i2]) {
                return false;
            }
        }
        return true;
    }
}
