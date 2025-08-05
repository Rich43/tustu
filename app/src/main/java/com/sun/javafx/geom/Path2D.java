package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/Path2D.class */
public class Path2D extends Shape implements PathConsumer2D {
    static final int[] curvecoords = {2, 2, 4, 6, 0};
    public static final int WIND_EVEN_ODD = 0;
    public static final int WIND_NON_ZERO = 1;
    private static final byte SEG_MOVETO = 0;
    private static final byte SEG_LINETO = 1;
    private static final byte SEG_QUADTO = 2;
    private static final byte SEG_CUBICTO = 3;
    private static final byte SEG_CLOSE = 4;
    byte[] pointTypes;
    int numTypes;
    int numCoords;
    int windingRule;
    static final int INIT_SIZE = 20;
    static final int EXPAND_MAX = 500;
    float[] floatCoords;
    float moveX;
    float moveY;
    float prevX;
    float prevY;
    float currX;
    float currY;

    /* loaded from: jfxrt.jar:com/sun/javafx/geom/Path2D$CornerPrefix.class */
    public enum CornerPrefix {
        CORNER_ONLY,
        MOVE_THEN_CORNER,
        LINE_THEN_CORNER
    }

    public Path2D() {
        this(1, 20);
    }

    public Path2D(int rule) {
        this(rule, 20);
    }

    public Path2D(int rule, int initialCapacity) {
        setWindingRule(rule);
        this.pointTypes = new byte[initialCapacity];
        this.floatCoords = new float[initialCapacity * 2];
    }

    public Path2D(Shape s2) {
        this(s2, (BaseTransform) null);
    }

    public Path2D(Shape s2, BaseTransform tx) {
        if (s2 instanceof Path2D) {
            Path2D p2d = (Path2D) s2;
            setWindingRule(p2d.windingRule);
            this.numTypes = p2d.numTypes;
            this.pointTypes = copyOf(p2d.pointTypes, p2d.pointTypes.length);
            this.numCoords = p2d.numCoords;
            if (tx == null || tx.isIdentity()) {
                this.floatCoords = copyOf(p2d.floatCoords, this.numCoords);
                this.moveX = p2d.moveX;
                this.moveY = p2d.moveY;
                this.prevX = p2d.prevX;
                this.prevY = p2d.prevY;
                this.currX = p2d.currX;
                this.currY = p2d.currY;
                return;
            }
            this.floatCoords = new float[this.numCoords + 6];
            tx.transform(p2d.floatCoords, 0, this.floatCoords, 0, this.numCoords / 2);
            this.floatCoords[this.numCoords + 0] = this.moveX;
            this.floatCoords[this.numCoords + 1] = this.moveY;
            this.floatCoords[this.numCoords + 2] = this.prevX;
            this.floatCoords[this.numCoords + 3] = this.prevY;
            this.floatCoords[this.numCoords + 4] = this.currX;
            this.floatCoords[this.numCoords + 5] = this.currY;
            tx.transform(this.floatCoords, this.numCoords, this.floatCoords, this.numCoords, 3);
            this.moveX = this.floatCoords[this.numCoords + 0];
            this.moveY = this.floatCoords[this.numCoords + 1];
            this.prevX = this.floatCoords[this.numCoords + 2];
            this.prevY = this.floatCoords[this.numCoords + 3];
            this.currX = this.floatCoords[this.numCoords + 4];
            this.currY = this.floatCoords[this.numCoords + 5];
            return;
        }
        PathIterator pi = s2.getPathIterator(tx);
        setWindingRule(pi.getWindingRule());
        this.pointTypes = new byte[20];
        this.floatCoords = new float[40];
        append(pi, false);
    }

    public Path2D(int windingRule, byte[] pointTypes, int numTypes, float[] pointCoords, int numCoords) {
        this.windingRule = windingRule;
        this.pointTypes = pointTypes;
        this.numTypes = numTypes;
        this.floatCoords = pointCoords;
        this.numCoords = numCoords;
    }

    Point2D getPoint(int coordindex) {
        return new Point2D(this.floatCoords[coordindex], this.floatCoords[coordindex + 1]);
    }

    private boolean close(int ix, float fx, float tolerance) {
        return Math.abs(((float) ix) - fx) <= tolerance;
    }

    public boolean checkAndGetIntRect(Rectangle retrect, float tolerance) {
        int x2;
        int w2;
        int y2;
        int h2;
        if (this.numTypes == 5) {
            if (this.pointTypes[4] != 1 && this.pointTypes[4] != 4) {
                return false;
            }
        } else if (this.numTypes == 6) {
            if (this.pointTypes[4] != 1 || this.pointTypes[5] != 4) {
                return false;
            }
        } else if (this.numTypes != 4) {
            return false;
        }
        if (this.pointTypes[0] != 0 || this.pointTypes[1] != 1 || this.pointTypes[2] != 1 || this.pointTypes[3] != 1) {
            return false;
        }
        int x0 = (int) (this.floatCoords[0] + 0.5f);
        int y0 = (int) (this.floatCoords[1] + 0.5f);
        if (!close(x0, this.floatCoords[0], tolerance) || !close(y0, this.floatCoords[1], tolerance)) {
            return false;
        }
        int x1 = (int) (this.floatCoords[2] + 0.5f);
        int y1 = (int) (this.floatCoords[3] + 0.5f);
        if (!close(x1, this.floatCoords[2], tolerance) || !close(y1, this.floatCoords[3], tolerance)) {
            return false;
        }
        int x22 = (int) (this.floatCoords[4] + 0.5f);
        int y22 = (int) (this.floatCoords[5] + 0.5f);
        if (!close(x22, this.floatCoords[4], tolerance) || !close(y22, this.floatCoords[5], tolerance)) {
            return false;
        }
        int x3 = (int) (this.floatCoords[6] + 0.5f);
        int y3 = (int) (this.floatCoords[7] + 0.5f);
        if (!close(x3, this.floatCoords[6], tolerance) || !close(y3, this.floatCoords[7], tolerance)) {
            return false;
        }
        if (this.numTypes > 4 && this.pointTypes[4] == 1 && (!close(x0, this.floatCoords[8], tolerance) || !close(y0, this.floatCoords[9], tolerance))) {
            return false;
        }
        if ((x0 == x1 && x22 == x3 && y0 == y3 && y1 == y22) || (y0 == y1 && y22 == y3 && x0 == x3 && x1 == x22)) {
            if (x22 < x0) {
                x2 = x22;
                w2 = x0 - x22;
            } else {
                x2 = x0;
                w2 = x22 - x0;
            }
            if (y22 < y0) {
                y2 = y22;
                h2 = y0 - y22;
            } else {
                y2 = y0;
                h2 = y22 - y0;
            }
            if (w2 < 0 || h2 < 0) {
                return false;
            }
            if (retrect != null) {
                retrect.setBounds(x2, y2, w2, h2);
                return true;
            }
            return true;
        }
        return false;
    }

    void needRoom(boolean needMove, int newCoords) {
        if (needMove && this.numTypes == 0) {
            throw new IllegalPathStateException("missing initial moveto in path definition");
        }
        int size = this.pointTypes.length;
        if (size == 0) {
            this.pointTypes = new byte[2];
        } else if (this.numTypes >= size) {
            int grow = size;
            if (grow > 500) {
                grow = 500;
            }
            this.pointTypes = copyOf(this.pointTypes, size + grow);
        }
        int size2 = this.floatCoords.length;
        if (this.numCoords + newCoords > size2) {
            int grow2 = size2;
            if (grow2 > 1000) {
                grow2 = 1000;
            }
            if (grow2 < newCoords) {
                grow2 = newCoords;
            }
            this.floatCoords = copyOf(this.floatCoords, size2 + grow2);
        }
    }

    @Override // com.sun.javafx.geom.PathConsumer2D
    public final void moveTo(float x2, float y2) {
        if (this.numTypes > 0 && this.pointTypes[this.numTypes - 1] == 0) {
            float[] fArr = this.floatCoords;
            int i2 = this.numCoords - 2;
            this.currX = x2;
            this.prevX = x2;
            this.moveX = x2;
            fArr[i2] = x2;
            float[] fArr2 = this.floatCoords;
            int i3 = this.numCoords - 1;
            this.currY = y2;
            this.prevY = y2;
            this.moveY = y2;
            fArr2[i3] = y2;
            return;
        }
        needRoom(false, 2);
        byte[] bArr = this.pointTypes;
        int i4 = this.numTypes;
        this.numTypes = i4 + 1;
        bArr[i4] = 0;
        float[] fArr3 = this.floatCoords;
        int i5 = this.numCoords;
        this.numCoords = i5 + 1;
        this.currX = x2;
        this.prevX = x2;
        this.moveX = x2;
        fArr3[i5] = x2;
        float[] fArr4 = this.floatCoords;
        int i6 = this.numCoords;
        this.numCoords = i6 + 1;
        this.currY = y2;
        this.prevY = y2;
        this.moveY = y2;
        fArr4[i6] = y2;
    }

    public final void moveToRel(float relx, float rely) {
        if (this.numTypes > 0 && this.pointTypes[this.numTypes - 1] == 0) {
            float[] fArr = this.floatCoords;
            int i2 = this.numCoords - 2;
            float f2 = this.currX + relx;
            this.currX = f2;
            this.prevX = f2;
            this.moveX = f2;
            fArr[i2] = f2;
            float[] fArr2 = this.floatCoords;
            int i3 = this.numCoords - 1;
            float f3 = this.currY + rely;
            this.currY = f3;
            this.prevY = f3;
            this.moveY = f3;
            fArr2[i3] = f3;
            return;
        }
        needRoom(true, 2);
        byte[] bArr = this.pointTypes;
        int i4 = this.numTypes;
        this.numTypes = i4 + 1;
        bArr[i4] = 0;
        float[] fArr3 = this.floatCoords;
        int i5 = this.numCoords;
        this.numCoords = i5 + 1;
        float f4 = this.currX + relx;
        this.currX = f4;
        this.prevX = f4;
        this.moveX = f4;
        fArr3[i5] = f4;
        float[] fArr4 = this.floatCoords;
        int i6 = this.numCoords;
        this.numCoords = i6 + 1;
        float f5 = this.currY + rely;
        this.currY = f5;
        this.prevY = f5;
        this.moveY = f5;
        fArr4[i6] = f5;
    }

    @Override // com.sun.javafx.geom.PathConsumer2D
    public final void lineTo(float x2, float y2) {
        needRoom(true, 2);
        byte[] bArr = this.pointTypes;
        int i2 = this.numTypes;
        this.numTypes = i2 + 1;
        bArr[i2] = 1;
        float[] fArr = this.floatCoords;
        int i3 = this.numCoords;
        this.numCoords = i3 + 1;
        this.currX = x2;
        this.prevX = x2;
        fArr[i3] = x2;
        float[] fArr2 = this.floatCoords;
        int i4 = this.numCoords;
        this.numCoords = i4 + 1;
        this.currY = y2;
        this.prevY = y2;
        fArr2[i4] = y2;
    }

    public final void lineToRel(float relx, float rely) {
        needRoom(true, 2);
        byte[] bArr = this.pointTypes;
        int i2 = this.numTypes;
        this.numTypes = i2 + 1;
        bArr[i2] = 1;
        float[] fArr = this.floatCoords;
        int i3 = this.numCoords;
        this.numCoords = i3 + 1;
        float f2 = this.currX + relx;
        this.currX = f2;
        this.prevX = f2;
        fArr[i3] = f2;
        float[] fArr2 = this.floatCoords;
        int i4 = this.numCoords;
        this.numCoords = i4 + 1;
        float f3 = this.currY + rely;
        this.currY = f3;
        this.prevY = f3;
        fArr2[i4] = f3;
    }

    @Override // com.sun.javafx.geom.PathConsumer2D
    public final void quadTo(float x1, float y1, float x2, float y2) {
        needRoom(true, 4);
        byte[] bArr = this.pointTypes;
        int i2 = this.numTypes;
        this.numTypes = i2 + 1;
        bArr[i2] = 2;
        float[] fArr = this.floatCoords;
        int i3 = this.numCoords;
        this.numCoords = i3 + 1;
        this.prevX = x1;
        fArr[i3] = x1;
        float[] fArr2 = this.floatCoords;
        int i4 = this.numCoords;
        this.numCoords = i4 + 1;
        this.prevY = y1;
        fArr2[i4] = y1;
        float[] fArr3 = this.floatCoords;
        int i5 = this.numCoords;
        this.numCoords = i5 + 1;
        this.currX = x2;
        fArr3[i5] = x2;
        float[] fArr4 = this.floatCoords;
        int i6 = this.numCoords;
        this.numCoords = i6 + 1;
        this.currY = y2;
        fArr4[i6] = y2;
    }

    public final void quadToRel(float relx1, float rely1, float relx2, float rely2) {
        needRoom(true, 4);
        byte[] bArr = this.pointTypes;
        int i2 = this.numTypes;
        this.numTypes = i2 + 1;
        bArr[i2] = 2;
        float[] fArr = this.floatCoords;
        int i3 = this.numCoords;
        this.numCoords = i3 + 1;
        float f2 = this.currX + relx1;
        this.prevX = f2;
        fArr[i3] = f2;
        float[] fArr2 = this.floatCoords;
        int i4 = this.numCoords;
        this.numCoords = i4 + 1;
        float f3 = this.currY + rely1;
        this.prevY = f3;
        fArr2[i4] = f3;
        float[] fArr3 = this.floatCoords;
        int i5 = this.numCoords;
        this.numCoords = i5 + 1;
        float f4 = this.currX + relx2;
        this.currX = f4;
        fArr3[i5] = f4;
        float[] fArr4 = this.floatCoords;
        int i6 = this.numCoords;
        this.numCoords = i6 + 1;
        float f5 = this.currY + rely2;
        this.currY = f5;
        fArr4[i6] = f5;
    }

    public final void quadToSmooth(float x2, float y2) {
        needRoom(true, 4);
        byte[] bArr = this.pointTypes;
        int i2 = this.numTypes;
        this.numTypes = i2 + 1;
        bArr[i2] = 2;
        float[] fArr = this.floatCoords;
        int i3 = this.numCoords;
        this.numCoords = i3 + 1;
        float f2 = (this.currX * 2.0f) - this.prevX;
        this.prevX = f2;
        fArr[i3] = f2;
        float[] fArr2 = this.floatCoords;
        int i4 = this.numCoords;
        this.numCoords = i4 + 1;
        float f3 = (this.currY * 2.0f) - this.prevY;
        this.prevY = f3;
        fArr2[i4] = f3;
        float[] fArr3 = this.floatCoords;
        int i5 = this.numCoords;
        this.numCoords = i5 + 1;
        this.currX = x2;
        fArr3[i5] = x2;
        float[] fArr4 = this.floatCoords;
        int i6 = this.numCoords;
        this.numCoords = i6 + 1;
        this.currY = y2;
        fArr4[i6] = y2;
    }

    public final void quadToSmoothRel(float relx2, float rely2) {
        needRoom(true, 4);
        byte[] bArr = this.pointTypes;
        int i2 = this.numTypes;
        this.numTypes = i2 + 1;
        bArr[i2] = 2;
        float[] fArr = this.floatCoords;
        int i3 = this.numCoords;
        this.numCoords = i3 + 1;
        float f2 = (this.currX * 2.0f) - this.prevX;
        this.prevX = f2;
        fArr[i3] = f2;
        float[] fArr2 = this.floatCoords;
        int i4 = this.numCoords;
        this.numCoords = i4 + 1;
        float f3 = (this.currY * 2.0f) - this.prevY;
        this.prevY = f3;
        fArr2[i4] = f3;
        float[] fArr3 = this.floatCoords;
        int i5 = this.numCoords;
        this.numCoords = i5 + 1;
        float f4 = this.currX + relx2;
        this.currX = f4;
        fArr3[i5] = f4;
        float[] fArr4 = this.floatCoords;
        int i6 = this.numCoords;
        this.numCoords = i6 + 1;
        float f5 = this.currY + rely2;
        this.currY = f5;
        fArr4[i6] = f5;
    }

    @Override // com.sun.javafx.geom.PathConsumer2D
    public final void curveTo(float x1, float y1, float x2, float y2, float x3, float y3) {
        needRoom(true, 6);
        byte[] bArr = this.pointTypes;
        int i2 = this.numTypes;
        this.numTypes = i2 + 1;
        bArr[i2] = 3;
        float[] fArr = this.floatCoords;
        int i3 = this.numCoords;
        this.numCoords = i3 + 1;
        fArr[i3] = x1;
        float[] fArr2 = this.floatCoords;
        int i4 = this.numCoords;
        this.numCoords = i4 + 1;
        fArr2[i4] = y1;
        float[] fArr3 = this.floatCoords;
        int i5 = this.numCoords;
        this.numCoords = i5 + 1;
        this.prevX = x2;
        fArr3[i5] = x2;
        float[] fArr4 = this.floatCoords;
        int i6 = this.numCoords;
        this.numCoords = i6 + 1;
        this.prevY = y2;
        fArr4[i6] = y2;
        float[] fArr5 = this.floatCoords;
        int i7 = this.numCoords;
        this.numCoords = i7 + 1;
        this.currX = x3;
        fArr5[i7] = x3;
        float[] fArr6 = this.floatCoords;
        int i8 = this.numCoords;
        this.numCoords = i8 + 1;
        this.currY = y3;
        fArr6[i8] = y3;
    }

    public final void curveToRel(float relx1, float rely1, float relx2, float rely2, float relx3, float rely3) {
        needRoom(true, 6);
        byte[] bArr = this.pointTypes;
        int i2 = this.numTypes;
        this.numTypes = i2 + 1;
        bArr[i2] = 3;
        float[] fArr = this.floatCoords;
        int i3 = this.numCoords;
        this.numCoords = i3 + 1;
        fArr[i3] = this.currX + relx1;
        float[] fArr2 = this.floatCoords;
        int i4 = this.numCoords;
        this.numCoords = i4 + 1;
        fArr2[i4] = this.currY + rely1;
        float[] fArr3 = this.floatCoords;
        int i5 = this.numCoords;
        this.numCoords = i5 + 1;
        float f2 = this.currX + relx2;
        this.prevX = f2;
        fArr3[i5] = f2;
        float[] fArr4 = this.floatCoords;
        int i6 = this.numCoords;
        this.numCoords = i6 + 1;
        float f3 = this.currY + rely2;
        this.prevY = f3;
        fArr4[i6] = f3;
        float[] fArr5 = this.floatCoords;
        int i7 = this.numCoords;
        this.numCoords = i7 + 1;
        float f4 = this.currX + relx3;
        this.currX = f4;
        fArr5[i7] = f4;
        float[] fArr6 = this.floatCoords;
        int i8 = this.numCoords;
        this.numCoords = i8 + 1;
        float f5 = this.currY + rely3;
        this.currY = f5;
        fArr6[i8] = f5;
    }

    public final void curveToSmooth(float x2, float y2, float x3, float y3) {
        needRoom(true, 6);
        byte[] bArr = this.pointTypes;
        int i2 = this.numTypes;
        this.numTypes = i2 + 1;
        bArr[i2] = 3;
        float[] fArr = this.floatCoords;
        int i3 = this.numCoords;
        this.numCoords = i3 + 1;
        fArr[i3] = (this.currX * 2.0f) - this.prevX;
        float[] fArr2 = this.floatCoords;
        int i4 = this.numCoords;
        this.numCoords = i4 + 1;
        fArr2[i4] = (this.currY * 2.0f) - this.prevY;
        float[] fArr3 = this.floatCoords;
        int i5 = this.numCoords;
        this.numCoords = i5 + 1;
        this.prevX = x2;
        fArr3[i5] = x2;
        float[] fArr4 = this.floatCoords;
        int i6 = this.numCoords;
        this.numCoords = i6 + 1;
        this.prevY = y2;
        fArr4[i6] = y2;
        float[] fArr5 = this.floatCoords;
        int i7 = this.numCoords;
        this.numCoords = i7 + 1;
        this.currX = x3;
        fArr5[i7] = x3;
        float[] fArr6 = this.floatCoords;
        int i8 = this.numCoords;
        this.numCoords = i8 + 1;
        this.currY = y3;
        fArr6[i8] = y3;
    }

    public final void curveToSmoothRel(float relx2, float rely2, float relx3, float rely3) {
        needRoom(true, 6);
        byte[] bArr = this.pointTypes;
        int i2 = this.numTypes;
        this.numTypes = i2 + 1;
        bArr[i2] = 3;
        float[] fArr = this.floatCoords;
        int i3 = this.numCoords;
        this.numCoords = i3 + 1;
        fArr[i3] = (this.currX * 2.0f) - this.prevX;
        float[] fArr2 = this.floatCoords;
        int i4 = this.numCoords;
        this.numCoords = i4 + 1;
        fArr2[i4] = (this.currY * 2.0f) - this.prevY;
        float[] fArr3 = this.floatCoords;
        int i5 = this.numCoords;
        this.numCoords = i5 + 1;
        float f2 = this.currX + relx2;
        this.prevX = f2;
        fArr3[i5] = f2;
        float[] fArr4 = this.floatCoords;
        int i6 = this.numCoords;
        this.numCoords = i6 + 1;
        float f3 = this.currY + rely2;
        this.prevY = f3;
        fArr4[i6] = f3;
        float[] fArr5 = this.floatCoords;
        int i7 = this.numCoords;
        this.numCoords = i7 + 1;
        float f4 = this.currX + relx3;
        this.currX = f4;
        fArr5[i7] = f4;
        float[] fArr6 = this.floatCoords;
        int i8 = this.numCoords;
        this.numCoords = i8 + 1;
        float f5 = this.currY + rely3;
        this.currY = f5;
        fArr6[i8] = f5;
    }

    public final void ovalQuadrantTo(float cx, float cy, float ex, float ey, float tfrom, float tto) {
        if (this.numTypes < 1) {
            throw new IllegalPathStateException("missing initial moveto in path definition");
        }
        appendOvalQuadrant(this.currX, this.currY, cx, cy, ex, ey, tfrom, tto, CornerPrefix.CORNER_ONLY);
    }

    public final void appendOvalQuadrant(float sx, float sy, float cx, float cy, float ex, float ey, float tfrom, float tto, CornerPrefix prefix) {
        if (tfrom < 0.0f || tfrom > tto || tto > 1.0f) {
            throw new IllegalArgumentException("0 <= tfrom <= tto <= 1 required");
        }
        float cx0 = (float) (sx + ((cx - sx) * 0.5522847498307933d));
        float cy0 = (float) (sy + ((cy - sy) * 0.5522847498307933d));
        float cx1 = (float) (ex + ((cx - ex) * 0.5522847498307933d));
        float cy1 = (float) (ey + ((cy - ey) * 0.5522847498307933d));
        if (tto < 1.0f) {
            float t2 = 1.0f - tto;
            float ex2 = ex + ((cx1 - ex) * t2);
            float ey2 = ey + ((cy1 - ey) * t2);
            float cx12 = cx1 + ((cx0 - cx1) * t2);
            float cy12 = cy1 + ((cy0 - cy1) * t2);
            cx0 += (sx - cx0) * t2;
            cy0 += (sy - cy0) * t2;
            float ex3 = ex2 + ((cx12 - ex2) * t2);
            float ey3 = ey2 + ((cy12 - ey2) * t2);
            cx1 = cx12 + ((cx0 - cx12) * t2);
            cy1 = cy12 + ((cy0 - cy12) * t2);
            ex = ex3 + ((cx1 - ex3) * t2);
            ey = ey3 + ((cy1 - ey3) * t2);
        }
        if (tfrom > 0.0f) {
            if (tto < 1.0f) {
                tfrom /= tto;
            }
            float sx2 = sx + ((cx0 - sx) * tfrom);
            float sy2 = sy + ((cy0 - sy) * tfrom);
            float cx02 = cx0 + ((cx1 - cx0) * tfrom);
            float cy02 = cy0 + ((cy1 - cy0) * tfrom);
            cx1 += (ex - cx1) * tfrom;
            cy1 += (ey - cy1) * tfrom;
            float sx3 = sx2 + ((cx02 - sx2) * tfrom);
            float sy3 = sy2 + ((cy02 - sy2) * tfrom);
            cx0 = cx02 + ((cx1 - cx02) * tfrom);
            cy0 = cy02 + ((cy1 - cy02) * tfrom);
            sx = sx3 + ((cx0 - sx3) * tfrom);
            sy = sy3 + ((cy0 - sy3) * tfrom);
        }
        if (prefix == CornerPrefix.MOVE_THEN_CORNER) {
            moveTo(sx, sy);
        } else if (prefix == CornerPrefix.LINE_THEN_CORNER && (this.numTypes == 1 || sx != this.currX || sy != this.currY)) {
            lineTo(sx, sy);
        }
        if (tfrom == tto || (sx == cx0 && cx0 == cx1 && cx1 == ex && sy == cy0 && cy0 == cy1 && cy1 == ey)) {
            if (prefix != CornerPrefix.LINE_THEN_CORNER) {
                lineTo(ex, ey);
                return;
            }
            return;
        }
        curveTo(cx0, cy0, cx1, cy1, ex, ey);
    }

    public void arcTo(float radiusx, float radiusy, float xAxisRotation, boolean largeArcFlag, boolean sweepFlag, float x2, float y2) {
        double cosphi;
        double sinphi;
        if (this.numTypes < 1) {
            throw new IllegalPathStateException("missing initial moveto in path definition");
        }
        double rx = Math.abs(radiusx);
        double ry = Math.abs(radiusy);
        if (rx == 0.0d || ry == 0.0d) {
            lineTo(x2, y2);
            return;
        }
        double x1 = this.currX;
        double y1 = this.currY;
        double x22 = x2;
        double y22 = y2;
        if (x1 == x22 && y1 == y22) {
            return;
        }
        if (xAxisRotation == 0.0d) {
            cosphi = 1.0d;
            sinphi = 0.0d;
        } else {
            cosphi = Math.cos(xAxisRotation);
            sinphi = Math.sin(xAxisRotation);
        }
        double mx = (x1 + x22) / 2.0d;
        double my = (y1 + y22) / 2.0d;
        double relx1 = x1 - mx;
        double rely1 = y1 - my;
        double x1p = ((cosphi * relx1) + (sinphi * rely1)) / rx;
        double y1p = ((cosphi * rely1) - (sinphi * relx1)) / ry;
        double lenpsq = (x1p * x1p) + (y1p * y1p);
        if (lenpsq >= 1.0d) {
            double xqpr = y1p * rx;
            double yqpr = x1p * ry;
            if (sweepFlag) {
                xqpr = -xqpr;
            } else {
                yqpr = -yqpr;
            }
            double relxq = (cosphi * xqpr) - (sinphi * yqpr);
            double relyq = (cosphi * yqpr) + (sinphi * xqpr);
            double xq = mx + relxq;
            double yq = my + relyq;
            double xc = x1 + relxq;
            double yc = y1 + relyq;
            appendOvalQuadrant((float) x1, (float) y1, (float) xc, (float) yc, (float) xq, (float) yq, 0.0f, 1.0f, CornerPrefix.CORNER_ONLY);
            double xc2 = x22 + relxq;
            double yc2 = y22 + relyq;
            appendOvalQuadrant((float) xq, (float) yq, (float) xc2, (float) yc2, (float) x22, (float) y22, 0.0f, 1.0f, CornerPrefix.CORNER_ONLY);
            return;
        }
        double scalef = Math.sqrt((1.0d - lenpsq) / lenpsq);
        double cxp = scalef * y1p;
        double cyp = scalef * x1p;
        if (largeArcFlag == sweepFlag) {
            cxp = -cxp;
        } else {
            cyp = -cyp;
        }
        double mx2 = mx + (((cosphi * cxp) * rx) - ((sinphi * cyp) * ry));
        double my2 = my + (cosphi * cyp * ry) + (sinphi * cxp * rx);
        double ux = x1p - cxp;
        double uy = y1p - cyp;
        double vx = -(x1p + cxp);
        double vy = -(y1p + cyp);
        boolean done = false;
        float quadlen = 1.0f;
        boolean wasclose = false;
        do {
            double xqp = uy;
            double yqp = ux;
            if (sweepFlag) {
                xqp = -xqp;
            } else {
                yqp = -yqp;
            }
            if ((xqp * vx) + (yqp * vy) > 0.0d) {
                double dot = (ux * vx) + (uy * vy);
                if (dot >= 0.0d) {
                    quadlen = (float) (Math.acos(dot) / 1.5707963267948966d);
                    done = true;
                }
                wasclose = true;
            } else if (wasclose) {
                return;
            }
            double relxq2 = ((cosphi * xqp) * rx) - ((sinphi * yqp) * ry);
            double relyq2 = (cosphi * yqp * ry) + (sinphi * xqp * rx);
            double xq2 = mx2 + relxq2;
            double yq2 = my2 + relyq2;
            double xc3 = x1 + relxq2;
            double yc3 = y1 + relyq2;
            appendOvalQuadrant((float) x1, (float) y1, (float) xc3, (float) yc3, (float) xq2, (float) yq2, 0.0f, quadlen, CornerPrefix.CORNER_ONLY);
            x1 = xq2;
            y1 = yq2;
            ux = xqp;
            uy = yqp;
        } while (!done);
    }

    public void arcToRel(float radiusx, float radiusy, float xAxisRotation, boolean largeArcFlag, boolean sweepFlag, float relx, float rely) {
        arcTo(radiusx, radiusy, xAxisRotation, largeArcFlag, sweepFlag, this.currX + relx, this.currY + rely);
    }

    int pointCrossings(float px, float py) {
        float[] coords = this.floatCoords;
        float f2 = coords[0];
        float movx = f2;
        float curx = f2;
        float f3 = coords[1];
        float movy = f3;
        float cury = f3;
        int crossings = 0;
        int ci = 2;
        for (int i2 = 1; i2 < this.numTypes; i2++) {
            switch (this.pointTypes[i2]) {
                case 0:
                    if (cury != movy) {
                        crossings += Shape.pointCrossingsForLine(px, py, curx, cury, movx, movy);
                    }
                    int i3 = ci;
                    int ci2 = ci + 1;
                    float f4 = coords[i3];
                    curx = f4;
                    movx = f4;
                    ci = ci2 + 1;
                    float f5 = coords[ci2];
                    cury = f5;
                    movy = f5;
                    break;
                case 1:
                    int i4 = ci;
                    int ci3 = ci + 1;
                    float endx = coords[i4];
                    ci = ci3 + 1;
                    float endy = coords[ci3];
                    crossings += Shape.pointCrossingsForLine(px, py, curx, cury, endx, endy);
                    curx = endx;
                    cury = endy;
                    break;
                case 2:
                    int i5 = ci;
                    int ci4 = ci + 1;
                    float f6 = coords[i5];
                    int ci5 = ci4 + 1;
                    float f7 = coords[ci4];
                    int ci6 = ci5 + 1;
                    float endx2 = coords[ci5];
                    ci = ci6 + 1;
                    float endy2 = coords[ci6];
                    crossings += Shape.pointCrossingsForQuad(px, py, curx, cury, f6, f7, endx2, endy2, 0);
                    curx = endx2;
                    cury = endy2;
                    break;
                case 3:
                    int i6 = ci;
                    int ci7 = ci + 1;
                    float f8 = coords[i6];
                    int ci8 = ci7 + 1;
                    float f9 = coords[ci7];
                    int ci9 = ci8 + 1;
                    float f10 = coords[ci8];
                    int ci10 = ci9 + 1;
                    float f11 = coords[ci9];
                    int ci11 = ci10 + 1;
                    float endx3 = coords[ci10];
                    ci = ci11 + 1;
                    float endy3 = coords[ci11];
                    crossings += Shape.pointCrossingsForCubic(px, py, curx, cury, f8, f9, f10, f11, endx3, endy3, 0);
                    curx = endx3;
                    cury = endy3;
                    break;
                case 4:
                    if (cury != movy) {
                        crossings += Shape.pointCrossingsForLine(px, py, curx, cury, movx, movy);
                    }
                    curx = movx;
                    cury = movy;
                    break;
            }
        }
        if (cury != movy) {
            crossings += Shape.pointCrossingsForLine(px, py, curx, cury, movx, movy);
        }
        return crossings;
    }

    int rectCrossings(float rxmin, float rymin, float rxmax, float rymax) {
        float[] coords = this.floatCoords;
        float f2 = coords[0];
        float movx = f2;
        float curx = f2;
        float f3 = coords[1];
        float movy = f3;
        float cury = f3;
        int crossings = 0;
        int ci = 2;
        for (int i2 = 1; crossings != Integer.MIN_VALUE && i2 < this.numTypes; i2++) {
            switch (this.pointTypes[i2]) {
                case 0:
                    if (curx != movx || cury != movy) {
                        crossings = Shape.rectCrossingsForLine(crossings, rxmin, rymin, rxmax, rymax, curx, cury, movx, movy);
                    }
                    int i3 = ci;
                    int ci2 = ci + 1;
                    float f4 = coords[i3];
                    curx = f4;
                    movx = f4;
                    ci = ci2 + 1;
                    float f5 = coords[ci2];
                    cury = f5;
                    movy = f5;
                    break;
                case 1:
                    int i4 = ci;
                    int ci3 = ci + 1;
                    float endx = coords[i4];
                    ci = ci3 + 1;
                    float endy = coords[ci3];
                    crossings = Shape.rectCrossingsForLine(crossings, rxmin, rymin, rxmax, rymax, curx, cury, endx, endy);
                    curx = endx;
                    cury = endy;
                    break;
                case 2:
                    int i5 = ci;
                    int ci4 = ci + 1;
                    float f6 = coords[i5];
                    int ci5 = ci4 + 1;
                    float f7 = coords[ci4];
                    int ci6 = ci5 + 1;
                    float endx2 = coords[ci5];
                    ci = ci6 + 1;
                    float endy2 = coords[ci6];
                    crossings = Shape.rectCrossingsForQuad(crossings, rxmin, rymin, rxmax, rymax, curx, cury, f6, f7, endx2, endy2, 0);
                    curx = endx2;
                    cury = endy2;
                    break;
                case 3:
                    int i6 = ci;
                    int ci7 = ci + 1;
                    float f8 = coords[i6];
                    int ci8 = ci7 + 1;
                    float f9 = coords[ci7];
                    int ci9 = ci8 + 1;
                    float f10 = coords[ci8];
                    int ci10 = ci9 + 1;
                    float f11 = coords[ci9];
                    int ci11 = ci10 + 1;
                    float endx3 = coords[ci10];
                    ci = ci11 + 1;
                    float endy3 = coords[ci11];
                    crossings = Shape.rectCrossingsForCubic(crossings, rxmin, rymin, rxmax, rymax, curx, cury, f8, f9, f10, f11, endx3, endy3, 0);
                    curx = endx3;
                    cury = endy3;
                    break;
                case 4:
                    if (curx != movx || cury != movy) {
                        crossings = Shape.rectCrossingsForLine(crossings, rxmin, rymin, rxmax, rymax, curx, cury, movx, movy);
                    }
                    curx = movx;
                    cury = movy;
                    break;
            }
        }
        if (crossings != Integer.MIN_VALUE && (curx != movx || cury != movy)) {
            crossings = Shape.rectCrossingsForLine(crossings, rxmin, rymin, rxmax, rymax, curx, cury, movx, movy);
        }
        return crossings;
    }

    public final void append(PathIterator pi, boolean connect) {
        float[] coords = new float[6];
        while (!pi.isDone()) {
            switch (pi.currentSegment(coords)) {
                case 0:
                    if (!connect || this.numTypes < 1 || this.numCoords < 1) {
                        moveTo(coords[0], coords[1]);
                    } else if (this.pointTypes[this.numTypes - 1] == 4 || this.floatCoords[this.numCoords - 2] != coords[0] || this.floatCoords[this.numCoords - 1] != coords[1]) {
                    }
                    pi.next();
                    connect = false;
                    break;
                case 1:
                    break;
                case 2:
                    quadTo(coords[0], coords[1], coords[2], coords[3]);
                    continue;
                    pi.next();
                    connect = false;
                case 3:
                    curveTo(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
                    continue;
                    pi.next();
                    connect = false;
                case 4:
                    closePath();
                    continue;
                    pi.next();
                    connect = false;
                default:
                    pi.next();
                    connect = false;
            }
            lineTo(coords[0], coords[1]);
            pi.next();
            connect = false;
        }
    }

    public final void transform(BaseTransform tx) {
        if (this.numCoords == 0) {
            return;
        }
        needRoom(false, 6);
        this.floatCoords[this.numCoords + 0] = this.moveX;
        this.floatCoords[this.numCoords + 1] = this.moveY;
        this.floatCoords[this.numCoords + 2] = this.prevX;
        this.floatCoords[this.numCoords + 3] = this.prevY;
        this.floatCoords[this.numCoords + 4] = this.currX;
        this.floatCoords[this.numCoords + 5] = this.currY;
        tx.transform(this.floatCoords, 0, this.floatCoords, 0, (this.numCoords / 2) + 3);
        this.moveX = this.floatCoords[this.numCoords + 0];
        this.moveY = this.floatCoords[this.numCoords + 1];
        this.prevX = this.floatCoords[this.numCoords + 2];
        this.prevY = this.floatCoords[this.numCoords + 3];
        this.currX = this.floatCoords[this.numCoords + 4];
        this.currY = this.floatCoords[this.numCoords + 5];
    }

    @Override // com.sun.javafx.geom.Shape
    public final RectBounds getBounds() {
        float y2;
        float x2;
        float y1;
        float x1;
        int i2 = this.numCoords;
        if (i2 > 0) {
            int i3 = i2 - 1;
            float f2 = this.floatCoords[i3];
            y2 = f2;
            y1 = f2;
            int i4 = i3 - 1;
            float f3 = this.floatCoords[i4];
            x2 = f3;
            x1 = f3;
            while (i4 > 0) {
                int i5 = i4 - 1;
                float y3 = this.floatCoords[i5];
                i4 = i5 - 1;
                float x3 = this.floatCoords[i4];
                if (x3 < x1) {
                    x1 = x3;
                }
                if (y3 < y1) {
                    y1 = y3;
                }
                if (x3 > x2) {
                    x2 = x3;
                }
                if (y3 > y2) {
                    y2 = y3;
                }
            }
        } else {
            y2 = 0.0f;
            x2 = 0.0f;
            y1 = 0.0f;
            x1 = 0.0f;
        }
        return new RectBounds(x1, y1, x2, y2);
    }

    public final int getNumCommands() {
        return this.numTypes;
    }

    public final byte[] getCommandsNoClone() {
        return this.pointTypes;
    }

    public final float[] getFloatCoordsNoClone() {
        return this.floatCoords;
    }

    @Override // com.sun.javafx.geom.Shape
    public PathIterator getPathIterator(BaseTransform tx) {
        if (tx == null) {
            return new CopyIterator(this);
        }
        return new TxIterator(this, tx);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/geom/Path2D$CopyIterator.class */
    static class CopyIterator extends Iterator {
        float[] floatCoords;

        CopyIterator(Path2D p2df) {
            super(p2df);
            this.floatCoords = p2df.floatCoords;
        }

        @Override // com.sun.javafx.geom.PathIterator
        public int currentSegment(float[] coords) {
            byte b2 = this.path.pointTypes[this.typeIdx];
            int numCoords = Path2D.curvecoords[b2];
            if (numCoords > 0) {
                System.arraycopy(this.floatCoords, this.pointIdx, coords, 0, numCoords);
            }
            return b2;
        }

        public int currentSegment(double[] coords) {
            byte b2 = this.path.pointTypes[this.typeIdx];
            int numCoords = Path2D.curvecoords[b2];
            if (numCoords > 0) {
                for (int i2 = 0; i2 < numCoords; i2++) {
                    coords[i2] = this.floatCoords[this.pointIdx + i2];
                }
            }
            return b2;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/geom/Path2D$TxIterator.class */
    static class TxIterator extends Iterator {
        float[] floatCoords;
        BaseTransform transform;

        TxIterator(Path2D p2df, BaseTransform tx) {
            super(p2df);
            this.floatCoords = p2df.floatCoords;
            this.transform = tx;
        }

        @Override // com.sun.javafx.geom.PathIterator
        public int currentSegment(float[] coords) {
            byte b2 = this.path.pointTypes[this.typeIdx];
            int numCoords = Path2D.curvecoords[b2];
            if (numCoords > 0) {
                this.transform.transform(this.floatCoords, this.pointIdx, coords, 0, numCoords / 2);
            }
            return b2;
        }

        public int currentSegment(double[] coords) {
            byte b2 = this.path.pointTypes[this.typeIdx];
            int numCoords = Path2D.curvecoords[b2];
            if (numCoords > 0) {
                this.transform.transform(this.floatCoords, this.pointIdx, coords, 0, numCoords / 2);
            }
            return b2;
        }
    }

    @Override // com.sun.javafx.geom.PathConsumer2D
    public final void closePath() {
        if (this.numTypes == 0 || this.pointTypes[this.numTypes - 1] != 4) {
            needRoom(true, 0);
            byte[] bArr = this.pointTypes;
            int i2 = this.numTypes;
            this.numTypes = i2 + 1;
            bArr[i2] = 4;
            float f2 = this.moveX;
            this.currX = f2;
            this.prevX = f2;
            float f3 = this.moveY;
            this.currY = f3;
            this.prevY = f3;
        }
    }

    @Override // com.sun.javafx.geom.PathConsumer2D
    public void pathDone() {
    }

    public final void append(Shape s2, boolean connect) {
        append(s2.getPathIterator(null), connect);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/geom/Path2D$SVGParser.class */
    static class SVGParser {
        final String svgpath;
        final int len;
        int pos;
        boolean allowcomma;

        public SVGParser(String svgpath) {
            this.svgpath = svgpath;
            this.len = svgpath.length();
        }

        public boolean isDone() {
            return toNextNonWsp() >= this.len;
        }

        public char getChar() {
            String str = this.svgpath;
            int i2 = this.pos;
            this.pos = i2 + 1;
            return str.charAt(i2);
        }

        public boolean nextIsNumber() {
            if (toNextNonWsp() < this.len) {
                switch (this.svgpath.charAt(this.pos)) {
                    case '+':
                    case '-':
                    case '.':
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                        return true;
                    case ',':
                    case '/':
                    default:
                        return false;
                }
            }
            return false;
        }

        public float f() {
            return getFloat();
        }

        public float a() {
            return (float) Math.toRadians(getFloat());
        }

        public float getFloat() {
            int start = toNextNonWsp();
            this.allowcomma = true;
            int end = toNumberEnd();
            if (start < end) {
                String flstr = this.svgpath.substring(start, end);
                try {
                    return Float.parseFloat(flstr);
                } catch (NumberFormatException e2) {
                    throw new IllegalArgumentException("invalid float (" + flstr + ") in path at pos=" + start);
                }
            }
            throw new IllegalArgumentException("end of path looking for float");
        }

        public boolean b() {
            toNextNonWsp();
            this.allowcomma = true;
            if (this.pos < this.len) {
                char flag = this.svgpath.charAt(this.pos);
                switch (flag) {
                    case '0':
                        this.pos++;
                        return false;
                    case '1':
                        this.pos++;
                        return true;
                    default:
                        throw new IllegalArgumentException("invalid boolean flag (" + flag + ") in path at pos=" + this.pos);
                }
            }
            throw new IllegalArgumentException("end of path looking for boolean");
        }

        private int toNextNonWsp() {
            boolean canbecomma = this.allowcomma;
            while (this.pos < this.len) {
                switch (this.svgpath.charAt(this.pos)) {
                    case '\t':
                    case '\n':
                    case '\r':
                    case ' ':
                        break;
                    case ',':
                        if (!canbecomma) {
                            return this.pos;
                        }
                        canbecomma = false;
                        break;
                    default:
                        return this.pos;
                }
                this.pos++;
            }
            return this.pos;
        }

        private int toNumberEnd() {
            boolean allowsign = true;
            boolean hasexp = false;
            boolean hasdecimal = false;
            while (this.pos < this.len) {
                switch (this.svgpath.charAt(this.pos)) {
                    case '+':
                    case '-':
                        if (!allowsign) {
                            return this.pos;
                        }
                        allowsign = false;
                        break;
                    case ',':
                    case '/':
                    case ':':
                    case ';':
                    case '<':
                    case '=':
                    case '>':
                    case '?':
                    case '@':
                    case 'A':
                    case 'B':
                    case 'C':
                    case 'D':
                    case 'F':
                    case 'G':
                    case 'H':
                    case 'I':
                    case 'J':
                    case 'K':
                    case 'L':
                    case 'M':
                    case 'N':
                    case 'O':
                    case 'P':
                    case 'Q':
                    case 'R':
                    case 'S':
                    case 'T':
                    case 'U':
                    case 'V':
                    case 'W':
                    case 'X':
                    case 'Y':
                    case 'Z':
                    case '[':
                    case '\\':
                    case ']':
                    case '^':
                    case '_':
                    case '`':
                    case 'a':
                    case 'b':
                    case 'c':
                    case 'd':
                    default:
                        return this.pos;
                    case '.':
                        if (!hasexp && !hasdecimal) {
                            hasdecimal = true;
                            allowsign = false;
                            break;
                        } else {
                            return this.pos;
                        }
                        break;
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                        allowsign = false;
                        break;
                    case 'E':
                    case 'e':
                        if (!hasexp) {
                            allowsign = true;
                            hasexp = true;
                            break;
                        } else {
                            return this.pos;
                        }
                }
                this.pos++;
            }
            return this.pos;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public final void appendSVGPath(String svgpath) {
        SVGParser p2 = new SVGParser(svgpath);
        p2.allowcomma = false;
        while (!p2.isDone()) {
            p2.allowcomma = false;
            char cmd = p2.getChar();
            switch (cmd) {
                case 'A':
                    do {
                        arcTo(p2.f(), p2.f(), p2.a(), p2.b(), p2.b(), p2.f(), p2.f());
                    } while (p2.nextIsNumber());
                    p2.allowcomma = false;
                case 'B':
                case 'D':
                case 'E':
                case 'F':
                case 'G':
                case 'I':
                case 'J':
                case 'K':
                case 'N':
                case 'O':
                case 'P':
                case 'R':
                case 'U':
                case 'W':
                case 'X':
                case 'Y':
                case '[':
                case '\\':
                case ']':
                case '^':
                case '_':
                case '`':
                case 'b':
                case 'd':
                case 'e':
                case 'f':
                case 'g':
                case 'i':
                case 'j':
                case 'k':
                case 'n':
                case 'o':
                case 'p':
                case 'r':
                case 'u':
                case 'w':
                case 'x':
                case 'y':
                default:
                    throw new IllegalArgumentException("invalid command (" + cmd + ") in SVG path at pos=" + p2.pos);
                case 'C':
                    do {
                        curveTo(p2.f(), p2.f(), p2.f(), p2.f(), p2.f(), p2.f());
                    } while (p2.nextIsNumber());
                    p2.allowcomma = false;
                case 'H':
                    do {
                        lineTo(p2.f(), this.currY);
                    } while (p2.nextIsNumber());
                    p2.allowcomma = false;
                case 'L':
                    do {
                        lineTo(p2.f(), p2.f());
                    } while (p2.nextIsNumber());
                    p2.allowcomma = false;
                case 'M':
                    moveTo(p2.f(), p2.f());
                    while (p2.nextIsNumber()) {
                        lineTo(p2.f(), p2.f());
                    }
                    p2.allowcomma = false;
                case 'Q':
                    do {
                        quadTo(p2.f(), p2.f(), p2.f(), p2.f());
                    } while (p2.nextIsNumber());
                    p2.allowcomma = false;
                case 'S':
                    do {
                        curveToSmooth(p2.f(), p2.f(), p2.f(), p2.f());
                    } while (p2.nextIsNumber());
                    p2.allowcomma = false;
                case 'T':
                    do {
                        quadToSmooth(p2.f(), p2.f());
                    } while (p2.nextIsNumber());
                    p2.allowcomma = false;
                case 'V':
                    do {
                        lineTo(this.currX, p2.f());
                    } while (p2.nextIsNumber());
                    p2.allowcomma = false;
                case 'Z':
                case 'z':
                    closePath();
                    p2.allowcomma = false;
                case 'a':
                    do {
                        arcToRel(p2.f(), p2.f(), p2.a(), p2.b(), p2.b(), p2.f(), p2.f());
                    } while (p2.nextIsNumber());
                    p2.allowcomma = false;
                case 'c':
                    do {
                        curveToRel(p2.f(), p2.f(), p2.f(), p2.f(), p2.f(), p2.f());
                    } while (p2.nextIsNumber());
                    p2.allowcomma = false;
                case 'h':
                    do {
                        lineToRel(p2.f(), 0.0f);
                    } while (p2.nextIsNumber());
                    p2.allowcomma = false;
                case 'l':
                    do {
                        lineToRel(p2.f(), p2.f());
                    } while (p2.nextIsNumber());
                    p2.allowcomma = false;
                case 'm':
                    if (this.numTypes > 0) {
                        moveToRel(p2.f(), p2.f());
                    } else {
                        moveTo(p2.f(), p2.f());
                    }
                    while (p2.nextIsNumber()) {
                        lineToRel(p2.f(), p2.f());
                    }
                    p2.allowcomma = false;
                case 'q':
                    do {
                        quadToRel(p2.f(), p2.f(), p2.f(), p2.f());
                    } while (p2.nextIsNumber());
                    p2.allowcomma = false;
                case 's':
                    do {
                        curveToSmoothRel(p2.f(), p2.f(), p2.f(), p2.f());
                    } while (p2.nextIsNumber());
                    p2.allowcomma = false;
                case 't':
                    do {
                        quadToSmoothRel(p2.f(), p2.f());
                    } while (p2.nextIsNumber());
                    p2.allowcomma = false;
                case 'v':
                    do {
                        lineToRel(0.0f, p2.f());
                    } while (p2.nextIsNumber());
                    p2.allowcomma = false;
            }
        }
    }

    public final int getWindingRule() {
        return this.windingRule;
    }

    public final void setWindingRule(int rule) {
        if (rule != 0 && rule != 1) {
            throw new IllegalArgumentException("winding rule must be WIND_EVEN_ODD or WIND_NON_ZERO");
        }
        this.windingRule = rule;
    }

    public final Point2D getCurrentPoint() {
        if (this.numTypes < 1) {
            return null;
        }
        return new Point2D(this.currX, this.currY);
    }

    public final float getCurrentX() {
        if (this.numTypes < 1) {
            throw new IllegalPathStateException("no current point in empty path");
        }
        return this.currX;
    }

    public final float getCurrentY() {
        if (this.numTypes < 1) {
            throw new IllegalPathStateException("no current point in empty path");
        }
        return this.currY;
    }

    public final void reset() {
        this.numCoords = 0;
        this.numTypes = 0;
        this.currY = 0.0f;
        this.currX = 0.0f;
        this.prevY = 0.0f;
        this.prevX = 0.0f;
        this.moveY = 0.0f;
        this.moveX = 0.0f;
    }

    public final Shape createTransformedShape(BaseTransform tx) {
        return new Path2D(this, tx);
    }

    @Override // com.sun.javafx.geom.Shape
    public Path2D copy() {
        return new Path2D(this);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Path2D) {
            Path2D p2 = (Path2D) obj;
            if (p2.numTypes == this.numTypes && p2.numCoords == this.numCoords && p2.windingRule == this.windingRule) {
                for (int i2 = 0; i2 < this.numTypes; i2++) {
                    if (p2.pointTypes[i2] != this.pointTypes[i2]) {
                        return false;
                    }
                }
                for (int i3 = 0; i3 < this.numCoords; i3++) {
                    if (p2.floatCoords[i3] != this.floatCoords[i3]) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        int hash = (11 * 7) + this.numTypes;
        int hash2 = (11 * ((11 * hash) + this.numCoords)) + this.windingRule;
        for (int i2 = 0; i2 < this.numTypes; i2++) {
            hash2 = (11 * hash2) + this.pointTypes[i2];
        }
        for (int i3 = 0; i3 < this.numCoords; i3++) {
            hash2 = (11 * hash2) + Float.floatToIntBits(this.floatCoords[i3]);
        }
        return hash2;
    }

    public static boolean contains(PathIterator pi, float x2, float y2) {
        if ((x2 * 0.0f) + (y2 * 0.0f) == 0.0f) {
            int mask = pi.getWindingRule() == 1 ? -1 : 1;
            int cross = Shape.pointCrossingsForPath(pi, x2, y2);
            return (cross & mask) != 0;
        }
        return false;
    }

    public static boolean contains(PathIterator pi, Point2D p2) {
        return contains(pi, p2.f11907x, p2.f11908y);
    }

    @Override // com.sun.javafx.geom.Shape
    public final boolean contains(float x2, float y2) {
        if ((x2 * 0.0f) + (y2 * 0.0f) != 0.0f || this.numTypes < 2) {
            return false;
        }
        int mask = this.windingRule == 1 ? -1 : 1;
        return (pointCrossings(x2, y2) & mask) != 0;
    }

    @Override // com.sun.javafx.geom.Shape
    public final boolean contains(Point2D p2) {
        return contains(p2.f11907x, p2.f11908y);
    }

    public static boolean contains(PathIterator pi, float x2, float y2, float w2, float h2) {
        if (Float.isNaN(x2 + w2) || Float.isNaN(y2 + h2) || w2 <= 0.0f || h2 <= 0.0f) {
            return false;
        }
        int mask = pi.getWindingRule() == 1 ? -1 : 2;
        int crossings = Shape.rectCrossingsForPath(pi, x2, y2, x2 + w2, y2 + h2);
        return (crossings == Integer.MIN_VALUE || (crossings & mask) == 0) ? false : true;
    }

    @Override // com.sun.javafx.geom.Shape
    public final boolean contains(float x2, float y2, float w2, float h2) {
        if (Float.isNaN(x2 + w2) || Float.isNaN(y2 + h2) || w2 <= 0.0f || h2 <= 0.0f) {
            return false;
        }
        int mask = this.windingRule == 1 ? -1 : 2;
        int crossings = rectCrossings(x2, y2, x2 + w2, y2 + h2);
        return (crossings == Integer.MIN_VALUE || (crossings & mask) == 0) ? false : true;
    }

    public static boolean intersects(PathIterator pi, float x2, float y2, float w2, float h2) {
        if (Float.isNaN(x2 + w2) || Float.isNaN(y2 + h2) || w2 <= 0.0f || h2 <= 0.0f) {
            return false;
        }
        int mask = pi.getWindingRule() == 1 ? -1 : 2;
        int crossings = Shape.rectCrossingsForPath(pi, x2, y2, x2 + w2, y2 + h2);
        return crossings == Integer.MIN_VALUE || (crossings & mask) != 0;
    }

    @Override // com.sun.javafx.geom.Shape
    public final boolean intersects(float x2, float y2, float w2, float h2) {
        if (Float.isNaN(x2 + w2) || Float.isNaN(y2 + h2) || w2 <= 0.0f || h2 <= 0.0f) {
            return false;
        }
        int mask = this.windingRule == 1 ? -1 : 2;
        int crossings = rectCrossings(x2, y2, x2 + w2, y2 + h2);
        return crossings == Integer.MIN_VALUE || (crossings & mask) != 0;
    }

    @Override // com.sun.javafx.geom.Shape
    public PathIterator getPathIterator(BaseTransform tx, float flatness) {
        return new FlatteningPathIterator(getPathIterator(tx), flatness);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/geom/Path2D$Iterator.class */
    static abstract class Iterator implements PathIterator {
        int typeIdx;
        int pointIdx;
        Path2D path;

        Iterator(Path2D path) {
            this.path = path;
        }

        @Override // com.sun.javafx.geom.PathIterator
        public int getWindingRule() {
            return this.path.getWindingRule();
        }

        @Override // com.sun.javafx.geom.PathIterator
        public boolean isDone() {
            return this.typeIdx >= this.path.numTypes;
        }

        @Override // com.sun.javafx.geom.PathIterator
        public void next() {
            byte[] bArr = this.path.pointTypes;
            int i2 = this.typeIdx;
            this.typeIdx = i2 + 1;
            this.pointIdx += Path2D.curvecoords[bArr[i2]];
        }
    }

    static byte[] copyOf(byte[] original, int newLength) {
        byte[] copy = new byte[newLength];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
        return copy;
    }

    static float[] copyOf(float[] original, int newLength) {
        float[] copy = new float[newLength];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
        return copy;
    }

    public void setTo(Path2D otherPath) {
        this.numTypes = otherPath.numTypes;
        this.numCoords = otherPath.numCoords;
        if (this.numTypes > this.pointTypes.length) {
            this.pointTypes = new byte[this.numTypes];
        }
        System.arraycopy(otherPath.pointTypes, 0, this.pointTypes, 0, this.numTypes);
        if (this.numCoords > this.floatCoords.length) {
            this.floatCoords = new float[this.numCoords];
        }
        System.arraycopy(otherPath.floatCoords, 0, this.floatCoords, 0, this.numCoords);
        this.windingRule = otherPath.windingRule;
        this.moveX = otherPath.moveX;
        this.moveY = otherPath.moveY;
        this.prevX = otherPath.prevX;
        this.prevY = otherPath.prevY;
        this.currX = otherPath.currX;
        this.currY = otherPath.currY;
    }
}
