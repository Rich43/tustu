package java.awt.geom;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import sun.awt.geom.Curve;

/* loaded from: rt.jar:java/awt/geom/Path2D.class */
public abstract class Path2D implements Shape, Cloneable {
    public static final int WIND_EVEN_ODD = 0;
    public static final int WIND_NON_ZERO = 1;
    private static final byte SEG_MOVETO = 0;
    private static final byte SEG_LINETO = 1;
    private static final byte SEG_QUADTO = 2;
    private static final byte SEG_CUBICTO = 3;
    private static final byte SEG_CLOSE = 4;
    transient byte[] pointTypes;
    transient int numTypes;
    transient int numCoords;
    transient int windingRule;
    static final int INIT_SIZE = 20;
    static final int EXPAND_MAX = 500;
    static final int EXPAND_MAX_COORDS = 1000;
    static final int EXPAND_MIN = 10;
    private static final byte SERIAL_STORAGE_FLT_ARRAY = 48;
    private static final byte SERIAL_STORAGE_DBL_ARRAY = 49;
    private static final byte SERIAL_SEG_FLT_MOVETO = 64;
    private static final byte SERIAL_SEG_FLT_LINETO = 65;
    private static final byte SERIAL_SEG_FLT_QUADTO = 66;
    private static final byte SERIAL_SEG_FLT_CUBICTO = 67;
    private static final byte SERIAL_SEG_DBL_MOVETO = 80;
    private static final byte SERIAL_SEG_DBL_LINETO = 81;
    private static final byte SERIAL_SEG_DBL_QUADTO = 82;
    private static final byte SERIAL_SEG_DBL_CUBICTO = 83;
    private static final byte SERIAL_SEG_CLOSE = 96;
    private static final byte SERIAL_PATH_END = 97;
    static final /* synthetic */ boolean $assertionsDisabled;

    abstract float[] cloneCoordsFloat(AffineTransform affineTransform);

    abstract double[] cloneCoordsDouble(AffineTransform affineTransform);

    abstract void append(float f2, float f3);

    abstract void append(double d2, double d3);

    abstract Point2D getPoint(int i2);

    abstract void needRoom(boolean z2, int i2);

    abstract int pointCrossings(double d2, double d3);

    abstract int rectCrossings(double d2, double d3, double d4, double d5);

    public abstract void moveTo(double d2, double d3);

    public abstract void lineTo(double d2, double d3);

    public abstract void quadTo(double d2, double d3, double d4, double d5);

    public abstract void curveTo(double d2, double d3, double d4, double d5, double d6, double d7);

    public abstract void append(PathIterator pathIterator, boolean z2);

    public abstract void transform(AffineTransform affineTransform);

    public abstract Object clone();

    static {
        $assertionsDisabled = !Path2D.class.desiredAssertionStatus();
    }

    Path2D() {
    }

    Path2D(int i2, int i3) {
        setWindingRule(i2);
        this.pointTypes = new byte[i3];
    }

    static byte[] expandPointTypes(byte[] bArr, int i2) {
        int length = bArr.length;
        int i3 = length + i2;
        if (i3 < length) {
            throw new ArrayIndexOutOfBoundsException("pointTypes exceeds maximum capacity !");
        }
        int iMax = length;
        if (iMax > 500) {
            iMax = Math.max(500, length >> 3);
        } else if (iMax < 10) {
            iMax = 10;
        }
        if (!$assertionsDisabled && iMax <= 0) {
            throw new AssertionError();
        }
        int i4 = length + iMax;
        if (i4 < i3) {
            i4 = Integer.MAX_VALUE;
        }
        while (true) {
            try {
                return Arrays.copyOf(bArr, i4);
            } catch (OutOfMemoryError e2) {
                if (i4 == i3) {
                    throw e2;
                }
                i4 = i3 + ((i4 - i3) / 2);
            }
        }
    }

    /* loaded from: rt.jar:java/awt/geom/Path2D$Float.class */
    public static class Float extends Path2D implements Serializable {
        transient float[] floatCoords;
        private static final long serialVersionUID = 6990832515060788886L;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Path2D.class.desiredAssertionStatus();
        }

        public Float() {
            this(1, 20);
        }

        public Float(int i2) {
            this(i2, 20);
        }

        public Float(int i2, int i3) {
            super(i2, i3);
            this.floatCoords = new float[i3 * 2];
        }

        public Float(Shape shape) {
            this(shape, (AffineTransform) null);
        }

        public Float(Shape shape, AffineTransform affineTransform) {
            if (shape instanceof Path2D) {
                Path2D path2D = (Path2D) shape;
                setWindingRule(path2D.windingRule);
                this.numTypes = path2D.numTypes;
                this.pointTypes = Arrays.copyOf(path2D.pointTypes, path2D.numTypes);
                this.numCoords = path2D.numCoords;
                this.floatCoords = path2D.cloneCoordsFloat(affineTransform);
                return;
            }
            PathIterator pathIterator = shape.getPathIterator(affineTransform);
            setWindingRule(pathIterator.getWindingRule());
            this.pointTypes = new byte[20];
            this.floatCoords = new float[40];
            append(pathIterator, false);
        }

        @Override // java.awt.geom.Path2D
        float[] cloneCoordsFloat(AffineTransform affineTransform) {
            float[] fArrCopyOf;
            if (affineTransform == null) {
                fArrCopyOf = Arrays.copyOf(this.floatCoords, this.numCoords);
            } else {
                fArrCopyOf = new float[this.numCoords];
                affineTransform.transform(this.floatCoords, 0, fArrCopyOf, 0, this.numCoords / 2);
            }
            return fArrCopyOf;
        }

        @Override // java.awt.geom.Path2D
        double[] cloneCoordsDouble(AffineTransform affineTransform) {
            double[] dArr = new double[this.numCoords];
            if (affineTransform == null) {
                for (int i2 = 0; i2 < this.numCoords; i2++) {
                    dArr[i2] = this.floatCoords[i2];
                }
            } else {
                affineTransform.transform(this.floatCoords, 0, dArr, 0, this.numCoords / 2);
            }
            return dArr;
        }

        @Override // java.awt.geom.Path2D
        void append(float f2, float f3) {
            float[] fArr = this.floatCoords;
            int i2 = this.numCoords;
            this.numCoords = i2 + 1;
            fArr[i2] = f2;
            float[] fArr2 = this.floatCoords;
            int i3 = this.numCoords;
            this.numCoords = i3 + 1;
            fArr2[i3] = f3;
        }

        @Override // java.awt.geom.Path2D
        void append(double d2, double d3) {
            float[] fArr = this.floatCoords;
            int i2 = this.numCoords;
            this.numCoords = i2 + 1;
            fArr[i2] = (float) d2;
            float[] fArr2 = this.floatCoords;
            int i3 = this.numCoords;
            this.numCoords = i3 + 1;
            fArr2[i3] = (float) d3;
        }

        @Override // java.awt.geom.Path2D
        Point2D getPoint(int i2) {
            return new Point2D.Float(this.floatCoords[i2], this.floatCoords[i2 + 1]);
        }

        @Override // java.awt.geom.Path2D
        void needRoom(boolean z2, int i2) {
            if (this.numTypes == 0 && z2) {
                throw new IllegalPathStateException("missing initial moveto in path definition");
            }
            if (this.numTypes >= this.pointTypes.length) {
                this.pointTypes = expandPointTypes(this.pointTypes, 1);
            }
            if (this.numCoords > this.floatCoords.length - i2) {
                this.floatCoords = expandCoords(this.floatCoords, i2);
            }
        }

        static float[] expandCoords(float[] fArr, int i2) {
            int length = fArr.length;
            int i3 = length + i2;
            if (i3 < length) {
                throw new ArrayIndexOutOfBoundsException("coords exceeds maximum capacity !");
            }
            int iMax = length;
            if (iMax > 1000) {
                iMax = Math.max(1000, length >> 3);
            } else if (iMax < 10) {
                iMax = 10;
            }
            if (!$assertionsDisabled && iMax <= i2) {
                throw new AssertionError();
            }
            int i4 = length + iMax;
            if (i4 < i3) {
                i4 = Integer.MAX_VALUE;
            }
            while (true) {
                try {
                    return Arrays.copyOf(fArr, i4);
                } catch (OutOfMemoryError e2) {
                    if (i4 == i3) {
                        throw e2;
                    }
                    i4 = i3 + ((i4 - i3) / 2);
                }
            }
        }

        @Override // java.awt.geom.Path2D
        public final synchronized void moveTo(double d2, double d3) {
            if (this.numTypes > 0 && this.pointTypes[this.numTypes - 1] == 0) {
                this.floatCoords[this.numCoords - 2] = (float) d2;
                this.floatCoords[this.numCoords - 1] = (float) d3;
                return;
            }
            needRoom(false, 2);
            byte[] bArr = this.pointTypes;
            int i2 = this.numTypes;
            this.numTypes = i2 + 1;
            bArr[i2] = 0;
            float[] fArr = this.floatCoords;
            int i3 = this.numCoords;
            this.numCoords = i3 + 1;
            fArr[i3] = (float) d2;
            float[] fArr2 = this.floatCoords;
            int i4 = this.numCoords;
            this.numCoords = i4 + 1;
            fArr2[i4] = (float) d3;
        }

        public final synchronized void moveTo(float f2, float f3) {
            if (this.numTypes > 0 && this.pointTypes[this.numTypes - 1] == 0) {
                this.floatCoords[this.numCoords - 2] = f2;
                this.floatCoords[this.numCoords - 1] = f3;
                return;
            }
            needRoom(false, 2);
            byte[] bArr = this.pointTypes;
            int i2 = this.numTypes;
            this.numTypes = i2 + 1;
            bArr[i2] = 0;
            float[] fArr = this.floatCoords;
            int i3 = this.numCoords;
            this.numCoords = i3 + 1;
            fArr[i3] = f2;
            float[] fArr2 = this.floatCoords;
            int i4 = this.numCoords;
            this.numCoords = i4 + 1;
            fArr2[i4] = f3;
        }

        @Override // java.awt.geom.Path2D
        public final synchronized void lineTo(double d2, double d3) {
            needRoom(true, 2);
            byte[] bArr = this.pointTypes;
            int i2 = this.numTypes;
            this.numTypes = i2 + 1;
            bArr[i2] = 1;
            float[] fArr = this.floatCoords;
            int i3 = this.numCoords;
            this.numCoords = i3 + 1;
            fArr[i3] = (float) d2;
            float[] fArr2 = this.floatCoords;
            int i4 = this.numCoords;
            this.numCoords = i4 + 1;
            fArr2[i4] = (float) d3;
        }

        public final synchronized void lineTo(float f2, float f3) {
            needRoom(true, 2);
            byte[] bArr = this.pointTypes;
            int i2 = this.numTypes;
            this.numTypes = i2 + 1;
            bArr[i2] = 1;
            float[] fArr = this.floatCoords;
            int i3 = this.numCoords;
            this.numCoords = i3 + 1;
            fArr[i3] = f2;
            float[] fArr2 = this.floatCoords;
            int i4 = this.numCoords;
            this.numCoords = i4 + 1;
            fArr2[i4] = f3;
        }

        @Override // java.awt.geom.Path2D
        public final synchronized void quadTo(double d2, double d3, double d4, double d5) {
            needRoom(true, 4);
            byte[] bArr = this.pointTypes;
            int i2 = this.numTypes;
            this.numTypes = i2 + 1;
            bArr[i2] = 2;
            float[] fArr = this.floatCoords;
            int i3 = this.numCoords;
            this.numCoords = i3 + 1;
            fArr[i3] = (float) d2;
            float[] fArr2 = this.floatCoords;
            int i4 = this.numCoords;
            this.numCoords = i4 + 1;
            fArr2[i4] = (float) d3;
            float[] fArr3 = this.floatCoords;
            int i5 = this.numCoords;
            this.numCoords = i5 + 1;
            fArr3[i5] = (float) d4;
            float[] fArr4 = this.floatCoords;
            int i6 = this.numCoords;
            this.numCoords = i6 + 1;
            fArr4[i6] = (float) d5;
        }

        public final synchronized void quadTo(float f2, float f3, float f4, float f5) {
            needRoom(true, 4);
            byte[] bArr = this.pointTypes;
            int i2 = this.numTypes;
            this.numTypes = i2 + 1;
            bArr[i2] = 2;
            float[] fArr = this.floatCoords;
            int i3 = this.numCoords;
            this.numCoords = i3 + 1;
            fArr[i3] = f2;
            float[] fArr2 = this.floatCoords;
            int i4 = this.numCoords;
            this.numCoords = i4 + 1;
            fArr2[i4] = f3;
            float[] fArr3 = this.floatCoords;
            int i5 = this.numCoords;
            this.numCoords = i5 + 1;
            fArr3[i5] = f4;
            float[] fArr4 = this.floatCoords;
            int i6 = this.numCoords;
            this.numCoords = i6 + 1;
            fArr4[i6] = f5;
        }

        @Override // java.awt.geom.Path2D
        public final synchronized void curveTo(double d2, double d3, double d4, double d5, double d6, double d7) {
            needRoom(true, 6);
            byte[] bArr = this.pointTypes;
            int i2 = this.numTypes;
            this.numTypes = i2 + 1;
            bArr[i2] = 3;
            float[] fArr = this.floatCoords;
            int i3 = this.numCoords;
            this.numCoords = i3 + 1;
            fArr[i3] = (float) d2;
            float[] fArr2 = this.floatCoords;
            int i4 = this.numCoords;
            this.numCoords = i4 + 1;
            fArr2[i4] = (float) d3;
            float[] fArr3 = this.floatCoords;
            int i5 = this.numCoords;
            this.numCoords = i5 + 1;
            fArr3[i5] = (float) d4;
            float[] fArr4 = this.floatCoords;
            int i6 = this.numCoords;
            this.numCoords = i6 + 1;
            fArr4[i6] = (float) d5;
            float[] fArr5 = this.floatCoords;
            int i7 = this.numCoords;
            this.numCoords = i7 + 1;
            fArr5[i7] = (float) d6;
            float[] fArr6 = this.floatCoords;
            int i8 = this.numCoords;
            this.numCoords = i8 + 1;
            fArr6[i8] = (float) d7;
        }

        public final synchronized void curveTo(float f2, float f3, float f4, float f5, float f6, float f7) {
            needRoom(true, 6);
            byte[] bArr = this.pointTypes;
            int i2 = this.numTypes;
            this.numTypes = i2 + 1;
            bArr[i2] = 3;
            float[] fArr = this.floatCoords;
            int i3 = this.numCoords;
            this.numCoords = i3 + 1;
            fArr[i3] = f2;
            float[] fArr2 = this.floatCoords;
            int i4 = this.numCoords;
            this.numCoords = i4 + 1;
            fArr2[i4] = f3;
            float[] fArr3 = this.floatCoords;
            int i5 = this.numCoords;
            this.numCoords = i5 + 1;
            fArr3[i5] = f4;
            float[] fArr4 = this.floatCoords;
            int i6 = this.numCoords;
            this.numCoords = i6 + 1;
            fArr4[i6] = f5;
            float[] fArr5 = this.floatCoords;
            int i7 = this.numCoords;
            this.numCoords = i7 + 1;
            fArr5[i7] = f6;
            float[] fArr6 = this.floatCoords;
            int i8 = this.numCoords;
            this.numCoords = i8 + 1;
            fArr6[i8] = f7;
        }

        @Override // java.awt.geom.Path2D
        int pointCrossings(double d2, double d3) {
            if (this.numTypes == 0) {
                return 0;
            }
            float[] fArr = this.floatCoords;
            double d4 = fArr[0];
            double d5 = d4;
            double d6 = d4;
            double d7 = fArr[1];
            double d8 = d7;
            double d9 = d7;
            int iPointCrossingsForLine = 0;
            int i2 = 2;
            for (int i3 = 1; i3 < this.numTypes; i3++) {
                switch (this.pointTypes[i3]) {
                    case 0:
                        if (d9 != d8) {
                            iPointCrossingsForLine += Curve.pointCrossingsForLine(d2, d3, d6, d9, d5, d8);
                        }
                        int i4 = i2;
                        int i5 = i2 + 1;
                        double d10 = fArr[i4];
                        d6 = d10;
                        d5 = d10;
                        i2 = i5 + 1;
                        double d11 = fArr[i5];
                        d9 = d11;
                        d8 = d11;
                        break;
                    case 1:
                        int i6 = iPointCrossingsForLine;
                        int i7 = i2;
                        i2 = i2 + 1 + 1;
                        iPointCrossingsForLine = (i6 == true ? 1 : 0) + Curve.pointCrossingsForLine(d2, d3, d6, d9, fArr[i7], fArr[r43]);
                        d6 = i6 == true ? 1 : 0;
                        d9 = i6 == true ? 1 : 0;
                        break;
                    case 2:
                        int i8 = iPointCrossingsForLine;
                        int i9 = i2;
                        int i10 = i2 + 1;
                        double d12 = fArr[i9];
                        int i11 = i10 + 1;
                        double d13 = fArr[i10];
                        double d14 = fArr[i11];
                        i2 = i11 + 1 + 1;
                        iPointCrossingsForLine = (i8 == true ? 1 : 0) + Curve.pointCrossingsForQuad(d2, d3, d6, d9, d12, d13, d14, fArr[r43], 0);
                        d6 = i8 == true ? 1 : 0;
                        d9 = i8 == true ? 1 : 0;
                        break;
                    case 3:
                        int i12 = iPointCrossingsForLine;
                        int i13 = i2;
                        int i14 = i2 + 1;
                        double d15 = fArr[i13];
                        int i15 = i14 + 1;
                        double d16 = fArr[i14];
                        int i16 = i15 + 1;
                        double d17 = fArr[i15];
                        int i17 = i16 + 1;
                        double d18 = fArr[i16];
                        double d19 = fArr[i17];
                        i2 = i17 + 1 + 1;
                        iPointCrossingsForLine = (i12 == true ? 1 : 0) + Curve.pointCrossingsForCubic(d2, d3, d6, d9, d15, d16, d17, d18, d19, fArr[r43], 0);
                        d6 = i12 == true ? 1 : 0;
                        d9 = i12 == true ? 1 : 0;
                        break;
                    case 4:
                        if (d9 != d8) {
                            iPointCrossingsForLine += Curve.pointCrossingsForLine(d2, d3, d6, d9, d5, d8);
                        }
                        d6 = d5;
                        d9 = d8;
                        break;
                }
            }
            if (d9 != d8) {
                iPointCrossingsForLine += Curve.pointCrossingsForLine(d2, d3, d6, d9, d5, d8);
            }
            return iPointCrossingsForLine;
        }

        @Override // java.awt.geom.Path2D
        int rectCrossings(double d2, double d3, double d4, double d5) {
            double d6;
            if (this.numTypes == 0) {
                return 0;
            }
            float[] fArr = this.floatCoords;
            double d7 = fArr[0];
            double d8 = d7;
            double d9 = d7;
            double d10 = fArr[1];
            double d11 = d10;
            double d12 = d10;
            int iRectCrossingsForLine = 0;
            int i2 = 2;
            for (int i3 = 1; iRectCrossingsForLine != Integer.MIN_VALUE && i3 < this.numTypes; i3++) {
                switch (this.pointTypes[i3]) {
                    case 0:
                        if (d9 != d8 || d12 != d11) {
                            d6 = d3;
                            iRectCrossingsForLine = Curve.rectCrossingsForLine(iRectCrossingsForLine, d2, d6, d4, d5, d9, d12, d8, d11);
                        }
                        int i4 = i2;
                        int i5 = i2 + 1;
                        d9 = d6;
                        d8 = fArr[i4];
                        i2 = i5 + 1;
                        d12 = d6;
                        d11 = fArr[i5];
                        break;
                    case 1:
                        d6 = d3;
                        int i6 = i2;
                        i2 = i2 + 1 + 1;
                        iRectCrossingsForLine = Curve.rectCrossingsForLine(iRectCrossingsForLine, d2, d6, d4, d5, d9, d12, fArr[i6], fArr[r51]);
                        d9 = d6;
                        d12 = d6;
                        break;
                    case 2:
                        d6 = d3;
                        int i7 = i2;
                        int i8 = i2 + 1;
                        double d13 = fArr[i7];
                        int i9 = i8 + 1;
                        double d14 = fArr[i8];
                        double d15 = fArr[i9];
                        i2 = i9 + 1 + 1;
                        iRectCrossingsForLine = Curve.rectCrossingsForQuad(iRectCrossingsForLine, d2, d6, d4, d5, d9, d12, d13, d14, d15, fArr[r51], 0);
                        d9 = d6;
                        d12 = d6;
                        break;
                    case 3:
                        d6 = d3;
                        int i10 = i2;
                        int i11 = i2 + 1;
                        double d16 = fArr[i10];
                        int i12 = i11 + 1;
                        double d17 = fArr[i11];
                        int i13 = i12 + 1;
                        double d18 = fArr[i12];
                        int i14 = i13 + 1;
                        double d19 = fArr[i13];
                        double d20 = fArr[i14];
                        i2 = i14 + 1 + 1;
                        iRectCrossingsForLine = Curve.rectCrossingsForCubic(iRectCrossingsForLine, d2, d6, d4, d5, d9, d12, d16, d17, d18, d19, d20, fArr[r51], 0);
                        d9 = d6;
                        d12 = d6;
                        break;
                    case 4:
                        if (d9 != d8 || d12 != d11) {
                            d6 = d3;
                            iRectCrossingsForLine = Curve.rectCrossingsForLine(iRectCrossingsForLine, d2, d6, d4, d5, d9, d12, d8, d11);
                        }
                        d9 = d8;
                        d12 = d11;
                        break;
                }
            }
            if (iRectCrossingsForLine != Integer.MIN_VALUE && (d9 != d8 || d12 != d11)) {
                iRectCrossingsForLine = Curve.rectCrossingsForLine(iRectCrossingsForLine, d2, d3, d4, d5, d9, d12, d8, d11);
            }
            return iRectCrossingsForLine;
        }

        @Override // java.awt.geom.Path2D
        public final void append(PathIterator pathIterator, boolean z2) {
            float[] fArr = new float[6];
            while (!pathIterator.isDone()) {
                switch (pathIterator.currentSegment(fArr)) {
                    case 0:
                        if (!z2 || this.numTypes < 1 || this.numCoords < 1) {
                            moveTo(fArr[0], fArr[1]);
                            break;
                        } else if (this.pointTypes[this.numTypes - 1] == 4 || this.floatCoords[this.numCoords - 2] != fArr[0] || this.floatCoords[this.numCoords - 1] != fArr[1]) {
                            lineTo(fArr[0], fArr[1]);
                            break;
                        } else {
                            break;
                        }
                        break;
                    case 1:
                        lineTo(fArr[0], fArr[1]);
                        break;
                    case 2:
                        quadTo(fArr[0], fArr[1], fArr[2], fArr[3]);
                        break;
                    case 3:
                        curveTo(fArr[0], fArr[1], fArr[2], fArr[3], fArr[4], fArr[5]);
                        break;
                    case 4:
                        closePath();
                        break;
                }
                pathIterator.next();
                z2 = false;
            }
        }

        @Override // java.awt.geom.Path2D
        public final void transform(AffineTransform affineTransform) {
            affineTransform.transform(this.floatCoords, 0, this.floatCoords, 0, this.numCoords / 2);
        }

        @Override // java.awt.Shape
        public final synchronized Rectangle2D getBounds2D() {
            float f2;
            float f3;
            float f4;
            float f5;
            int i2 = this.numCoords;
            if (i2 > 0) {
                int i3 = i2 - 1;
                float f6 = this.floatCoords[i3];
                f2 = f6;
                f4 = f6;
                int i4 = i3 - 1;
                float f7 = this.floatCoords[i4];
                f3 = f7;
                f5 = f7;
                while (i4 > 0) {
                    int i5 = i4 - 1;
                    float f8 = this.floatCoords[i5];
                    i4 = i5 - 1;
                    float f9 = this.floatCoords[i4];
                    if (f9 < f5) {
                        f5 = f9;
                    }
                    if (f8 < f4) {
                        f4 = f8;
                    }
                    if (f9 > f3) {
                        f3 = f9;
                    }
                    if (f8 > f2) {
                        f2 = f8;
                    }
                }
            } else {
                f2 = 0.0f;
                f3 = 0.0f;
                f4 = 0.0f;
                f5 = 0.0f;
            }
            return new Rectangle2D.Float(f5, f4, f3 - f5, f2 - f4);
        }

        @Override // java.awt.Shape
        public final PathIterator getPathIterator(AffineTransform affineTransform) {
            if (affineTransform == null) {
                return new CopyIterator(this);
            }
            return new TxIterator(this, affineTransform);
        }

        @Override // java.awt.geom.Path2D
        public final Object clone() {
            if (this instanceof GeneralPath) {
                return new GeneralPath(this);
            }
            return new Float(this);
        }

        private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            super.writeObject(objectOutputStream, false);
        }

        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            super.readObject(objectInputStream, false);
        }

        /* loaded from: rt.jar:java/awt/geom/Path2D$Float$CopyIterator.class */
        static class CopyIterator extends Iterator {
            float[] floatCoords;

            CopyIterator(Float r4) {
                super(r4);
                this.floatCoords = r4.floatCoords;
            }

            @Override // java.awt.geom.PathIterator
            public int currentSegment(float[] fArr) {
                byte b2 = this.path.pointTypes[this.typeIdx];
                int i2 = curvecoords[b2];
                if (i2 > 0) {
                    System.arraycopy(this.floatCoords, this.pointIdx, fArr, 0, i2);
                }
                return b2;
            }

            @Override // java.awt.geom.PathIterator
            public int currentSegment(double[] dArr) {
                byte b2 = this.path.pointTypes[this.typeIdx];
                int i2 = curvecoords[b2];
                if (i2 > 0) {
                    for (int i3 = 0; i3 < i2; i3++) {
                        dArr[i3] = this.floatCoords[this.pointIdx + i3];
                    }
                }
                return b2;
            }
        }

        /* loaded from: rt.jar:java/awt/geom/Path2D$Float$TxIterator.class */
        static class TxIterator extends Iterator {
            float[] floatCoords;
            AffineTransform affine;

            TxIterator(Float r4, AffineTransform affineTransform) {
                super(r4);
                this.floatCoords = r4.floatCoords;
                this.affine = affineTransform;
            }

            @Override // java.awt.geom.PathIterator
            public int currentSegment(float[] fArr) {
                byte b2 = this.path.pointTypes[this.typeIdx];
                int i2 = curvecoords[b2];
                if (i2 > 0) {
                    this.affine.transform(this.floatCoords, this.pointIdx, fArr, 0, i2 / 2);
                }
                return b2;
            }

            @Override // java.awt.geom.PathIterator
            public int currentSegment(double[] dArr) {
                byte b2 = this.path.pointTypes[this.typeIdx];
                int i2 = curvecoords[b2];
                if (i2 > 0) {
                    this.affine.transform(this.floatCoords, this.pointIdx, dArr, 0, i2 / 2);
                }
                return b2;
            }
        }
    }

    /* loaded from: rt.jar:java/awt/geom/Path2D$Double.class */
    public static class Double extends Path2D implements Serializable {
        transient double[] doubleCoords;
        private static final long serialVersionUID = 1826762518450014216L;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Path2D.class.desiredAssertionStatus();
        }

        public Double() {
            this(1, 20);
        }

        public Double(int i2) {
            this(i2, 20);
        }

        public Double(int i2, int i3) {
            super(i2, i3);
            this.doubleCoords = new double[i3 * 2];
        }

        public Double(Shape shape) {
            this(shape, (AffineTransform) null);
        }

        public Double(Shape shape, AffineTransform affineTransform) {
            if (shape instanceof Path2D) {
                Path2D path2D = (Path2D) shape;
                setWindingRule(path2D.windingRule);
                this.numTypes = path2D.numTypes;
                this.pointTypes = Arrays.copyOf(path2D.pointTypes, path2D.numTypes);
                this.numCoords = path2D.numCoords;
                this.doubleCoords = path2D.cloneCoordsDouble(affineTransform);
                return;
            }
            PathIterator pathIterator = shape.getPathIterator(affineTransform);
            setWindingRule(pathIterator.getWindingRule());
            this.pointTypes = new byte[20];
            this.doubleCoords = new double[40];
            append(pathIterator, false);
        }

        @Override // java.awt.geom.Path2D
        float[] cloneCoordsFloat(AffineTransform affineTransform) {
            float[] fArr = new float[this.numCoords];
            if (affineTransform == null) {
                for (int i2 = 0; i2 < this.numCoords; i2++) {
                    fArr[i2] = (float) this.doubleCoords[i2];
                }
            } else {
                affineTransform.transform(this.doubleCoords, 0, fArr, 0, this.numCoords / 2);
            }
            return fArr;
        }

        @Override // java.awt.geom.Path2D
        double[] cloneCoordsDouble(AffineTransform affineTransform) {
            double[] dArrCopyOf;
            if (affineTransform == null) {
                dArrCopyOf = Arrays.copyOf(this.doubleCoords, this.numCoords);
            } else {
                dArrCopyOf = new double[this.numCoords];
                affineTransform.transform(this.doubleCoords, 0, dArrCopyOf, 0, this.numCoords / 2);
            }
            return dArrCopyOf;
        }

        @Override // java.awt.geom.Path2D
        void append(float f2, float f3) {
            double[] dArr = this.doubleCoords;
            int i2 = this.numCoords;
            this.numCoords = i2 + 1;
            dArr[i2] = f2;
            double[] dArr2 = this.doubleCoords;
            int i3 = this.numCoords;
            this.numCoords = i3 + 1;
            dArr2[i3] = f3;
        }

        @Override // java.awt.geom.Path2D
        void append(double d2, double d3) {
            double[] dArr = this.doubleCoords;
            int i2 = this.numCoords;
            this.numCoords = i2 + 1;
            dArr[i2] = d2;
            double[] dArr2 = this.doubleCoords;
            int i3 = this.numCoords;
            this.numCoords = i3 + 1;
            dArr2[i3] = d3;
        }

        @Override // java.awt.geom.Path2D
        Point2D getPoint(int i2) {
            return new Point2D.Double(this.doubleCoords[i2], this.doubleCoords[i2 + 1]);
        }

        @Override // java.awt.geom.Path2D
        void needRoom(boolean z2, int i2) {
            if (this.numTypes == 0 && z2) {
                throw new IllegalPathStateException("missing initial moveto in path definition");
            }
            if (this.numTypes >= this.pointTypes.length) {
                this.pointTypes = expandPointTypes(this.pointTypes, 1);
            }
            if (this.numCoords > this.doubleCoords.length - i2) {
                this.doubleCoords = expandCoords(this.doubleCoords, i2);
            }
        }

        static double[] expandCoords(double[] dArr, int i2) {
            int length = dArr.length;
            int i3 = length + i2;
            if (i3 < length) {
                throw new ArrayIndexOutOfBoundsException("coords exceeds maximum capacity !");
            }
            int iMax = length;
            if (iMax > 1000) {
                iMax = Math.max(1000, length >> 3);
            } else if (iMax < 10) {
                iMax = 10;
            }
            if (!$assertionsDisabled && iMax <= i2) {
                throw new AssertionError();
            }
            int i4 = length + iMax;
            if (i4 < i3) {
                i4 = Integer.MAX_VALUE;
            }
            while (true) {
                try {
                    return Arrays.copyOf(dArr, i4);
                } catch (OutOfMemoryError e2) {
                    if (i4 == i3) {
                        throw e2;
                    }
                    i4 = i3 + ((i4 - i3) / 2);
                }
            }
        }

        @Override // java.awt.geom.Path2D
        public final synchronized void moveTo(double d2, double d3) {
            if (this.numTypes > 0 && this.pointTypes[this.numTypes - 1] == 0) {
                this.doubleCoords[this.numCoords - 2] = d2;
                this.doubleCoords[this.numCoords - 1] = d3;
                return;
            }
            needRoom(false, 2);
            byte[] bArr = this.pointTypes;
            int i2 = this.numTypes;
            this.numTypes = i2 + 1;
            bArr[i2] = 0;
            double[] dArr = this.doubleCoords;
            int i3 = this.numCoords;
            this.numCoords = i3 + 1;
            dArr[i3] = d2;
            double[] dArr2 = this.doubleCoords;
            int i4 = this.numCoords;
            this.numCoords = i4 + 1;
            dArr2[i4] = d3;
        }

        @Override // java.awt.geom.Path2D
        public final synchronized void lineTo(double d2, double d3) {
            needRoom(true, 2);
            byte[] bArr = this.pointTypes;
            int i2 = this.numTypes;
            this.numTypes = i2 + 1;
            bArr[i2] = 1;
            double[] dArr = this.doubleCoords;
            int i3 = this.numCoords;
            this.numCoords = i3 + 1;
            dArr[i3] = d2;
            double[] dArr2 = this.doubleCoords;
            int i4 = this.numCoords;
            this.numCoords = i4 + 1;
            dArr2[i4] = d3;
        }

        @Override // java.awt.geom.Path2D
        public final synchronized void quadTo(double d2, double d3, double d4, double d5) {
            needRoom(true, 4);
            byte[] bArr = this.pointTypes;
            int i2 = this.numTypes;
            this.numTypes = i2 + 1;
            bArr[i2] = 2;
            double[] dArr = this.doubleCoords;
            int i3 = this.numCoords;
            this.numCoords = i3 + 1;
            dArr[i3] = d2;
            double[] dArr2 = this.doubleCoords;
            int i4 = this.numCoords;
            this.numCoords = i4 + 1;
            dArr2[i4] = d3;
            double[] dArr3 = this.doubleCoords;
            int i5 = this.numCoords;
            this.numCoords = i5 + 1;
            dArr3[i5] = d4;
            double[] dArr4 = this.doubleCoords;
            int i6 = this.numCoords;
            this.numCoords = i6 + 1;
            dArr4[i6] = d5;
        }

        @Override // java.awt.geom.Path2D
        public final synchronized void curveTo(double d2, double d3, double d4, double d5, double d6, double d7) {
            needRoom(true, 6);
            byte[] bArr = this.pointTypes;
            int i2 = this.numTypes;
            this.numTypes = i2 + 1;
            bArr[i2] = 3;
            double[] dArr = this.doubleCoords;
            int i3 = this.numCoords;
            this.numCoords = i3 + 1;
            dArr[i3] = d2;
            double[] dArr2 = this.doubleCoords;
            int i4 = this.numCoords;
            this.numCoords = i4 + 1;
            dArr2[i4] = d3;
            double[] dArr3 = this.doubleCoords;
            int i5 = this.numCoords;
            this.numCoords = i5 + 1;
            dArr3[i5] = d4;
            double[] dArr4 = this.doubleCoords;
            int i6 = this.numCoords;
            this.numCoords = i6 + 1;
            dArr4[i6] = d5;
            double[] dArr5 = this.doubleCoords;
            int i7 = this.numCoords;
            this.numCoords = i7 + 1;
            dArr5[i7] = d6;
            double[] dArr6 = this.doubleCoords;
            int i8 = this.numCoords;
            this.numCoords = i8 + 1;
            dArr6[i8] = d7;
        }

        @Override // java.awt.geom.Path2D
        int pointCrossings(double d2, double d3) {
            if (this.numTypes == 0) {
                return 0;
            }
            double[] dArr = this.doubleCoords;
            double d4 = dArr[0];
            double d5 = d4;
            double d6 = d4;
            double d7 = dArr[1];
            double d8 = d7;
            double d9 = d7;
            int iPointCrossingsForLine = 0;
            int i2 = 2;
            for (int i3 = 1; i3 < this.numTypes; i3++) {
                switch (this.pointTypes[i3]) {
                    case 0:
                        if (d9 != d8) {
                            iPointCrossingsForLine += Curve.pointCrossingsForLine(d2, d3, d6, d9, d5, d8);
                        }
                        int i4 = i2;
                        int i5 = i2 + 1;
                        double d10 = dArr[i4];
                        d6 = d10;
                        d5 = d10;
                        i2 = i5 + 1;
                        double d11 = dArr[i5];
                        d9 = d11;
                        d8 = d11;
                        break;
                    case 1:
                        int i6 = iPointCrossingsForLine;
                        int i7 = i2;
                        int i8 = i2 + 1;
                        i2 = i8 + 1;
                        iPointCrossingsForLine = (i6 == true ? 1 : 0) + Curve.pointCrossingsForLine(d2, d3, d6, d9, dArr[i7], dArr[i8]);
                        d6 = i6 == true ? 1 : 0;
                        d9 = i6 == true ? 1 : 0;
                        break;
                    case 2:
                        int i9 = iPointCrossingsForLine;
                        int i10 = i2;
                        int i11 = i2 + 1;
                        double d12 = dArr[i10];
                        int i12 = i11 + 1;
                        double d13 = dArr[i11];
                        int i13 = i12 + 1;
                        double d14 = dArr[i12];
                        i2 = i13 + 1;
                        iPointCrossingsForLine = (i9 == true ? 1 : 0) + Curve.pointCrossingsForQuad(d2, d3, d6, d9, d12, d13, d14, dArr[i13], 0);
                        d6 = i9 == true ? 1 : 0;
                        d9 = i9 == true ? 1 : 0;
                        break;
                    case 3:
                        int i14 = iPointCrossingsForLine;
                        int i15 = i2;
                        int i16 = i2 + 1;
                        double d15 = dArr[i15];
                        int i17 = i16 + 1;
                        double d16 = dArr[i16];
                        int i18 = i17 + 1;
                        double d17 = dArr[i17];
                        int i19 = i18 + 1;
                        double d18 = dArr[i18];
                        int i20 = i19 + 1;
                        double d19 = dArr[i19];
                        i2 = i20 + 1;
                        iPointCrossingsForLine = (i14 == true ? 1 : 0) + Curve.pointCrossingsForCubic(d2, d3, d6, d9, d15, d16, d17, d18, d19, dArr[i20], 0);
                        d6 = i14 == true ? 1 : 0;
                        d9 = i14 == true ? 1 : 0;
                        break;
                    case 4:
                        if (d9 != d8) {
                            iPointCrossingsForLine += Curve.pointCrossingsForLine(d2, d3, d6, d9, d5, d8);
                        }
                        d6 = d5;
                        d9 = d8;
                        break;
                }
            }
            if (d9 != d8) {
                iPointCrossingsForLine += Curve.pointCrossingsForLine(d2, d3, d6, d9, d5, d8);
            }
            return iPointCrossingsForLine;
        }

        @Override // java.awt.geom.Path2D
        int rectCrossings(double d2, double d3, double d4, double d5) {
            double d6;
            if (this.numTypes == 0) {
                return 0;
            }
            double[] dArr = this.doubleCoords;
            double d7 = dArr[0];
            double d8 = d7;
            double d9 = d7;
            double d10 = dArr[1];
            double d11 = d10;
            double d12 = d10;
            int iRectCrossingsForLine = 0;
            int i2 = 2;
            for (int i3 = 1; iRectCrossingsForLine != Integer.MIN_VALUE && i3 < this.numTypes; i3++) {
                switch (this.pointTypes[i3]) {
                    case 0:
                        if (d9 != d8 || d12 != d11) {
                            d6 = d3;
                            iRectCrossingsForLine = Curve.rectCrossingsForLine(iRectCrossingsForLine, d2, d6, d4, d5, d9, d12, d8, d11);
                        }
                        int i4 = i2;
                        int i5 = i2 + 1;
                        d9 = d6;
                        d8 = dArr[i4];
                        i2 = i5 + 1;
                        d12 = d6;
                        d11 = dArr[i5];
                        break;
                    case 1:
                        int i6 = i2;
                        int i7 = i2 + 1;
                        double d13 = dArr[i6];
                        i2 = i7 + 1;
                        double d14 = dArr[i7];
                        d6 = d3;
                        iRectCrossingsForLine = Curve.rectCrossingsForLine(iRectCrossingsForLine, d2, d6, d4, d5, d9, d12, d13, d14);
                        d9 = d13;
                        d12 = d14;
                        break;
                    case 2:
                        d6 = d3;
                        int i8 = i2;
                        int i9 = i2 + 1;
                        double d15 = dArr[i8];
                        int i10 = i9 + 1;
                        double d16 = dArr[i9];
                        int i11 = i10 + 1;
                        double d17 = dArr[i10];
                        i2 = i11 + 1;
                        iRectCrossingsForLine = Curve.rectCrossingsForQuad(iRectCrossingsForLine, d2, d6, d4, d5, d9, d12, d15, d16, d17, dArr[i11], 0);
                        d9 = d6;
                        d12 = d6;
                        break;
                    case 3:
                        d6 = d3;
                        int i12 = i2;
                        int i13 = i2 + 1;
                        double d18 = dArr[i12];
                        int i14 = i13 + 1;
                        double d19 = dArr[i13];
                        int i15 = i14 + 1;
                        double d20 = dArr[i14];
                        int i16 = i15 + 1;
                        double d21 = dArr[i15];
                        int i17 = i16 + 1;
                        double d22 = dArr[i16];
                        i2 = i17 + 1;
                        iRectCrossingsForLine = Curve.rectCrossingsForCubic(iRectCrossingsForLine, d2, d6, d4, d5, d9, d12, d18, d19, d20, d21, d22, dArr[i17], 0);
                        d9 = d6;
                        d12 = d6;
                        break;
                    case 4:
                        if (d9 != d8 || d12 != d11) {
                            d6 = d3;
                            iRectCrossingsForLine = Curve.rectCrossingsForLine(iRectCrossingsForLine, d2, d6, d4, d5, d9, d12, d8, d11);
                        }
                        d9 = d8;
                        d12 = d11;
                        break;
                }
            }
            if (iRectCrossingsForLine != Integer.MIN_VALUE && (d9 != d8 || d12 != d11)) {
                iRectCrossingsForLine = Curve.rectCrossingsForLine(iRectCrossingsForLine, d2, d3, d4, d5, d9, d12, d8, d11);
            }
            return iRectCrossingsForLine;
        }

        @Override // java.awt.geom.Path2D
        public final void append(PathIterator pathIterator, boolean z2) {
            double[] dArr = new double[6];
            while (!pathIterator.isDone()) {
                switch (pathIterator.currentSegment(dArr)) {
                    case 0:
                        if (!z2 || this.numTypes < 1 || this.numCoords < 1) {
                            moveTo(dArr[0], dArr[1]);
                            break;
                        } else if (this.pointTypes[this.numTypes - 1] == 4 || this.doubleCoords[this.numCoords - 2] != dArr[0] || this.doubleCoords[this.numCoords - 1] != dArr[1]) {
                            lineTo(dArr[0], dArr[1]);
                            break;
                        } else {
                            break;
                        }
                        break;
                    case 1:
                        lineTo(dArr[0], dArr[1]);
                        break;
                    case 2:
                        quadTo(dArr[0], dArr[1], dArr[2], dArr[3]);
                        break;
                    case 3:
                        curveTo(dArr[0], dArr[1], dArr[2], dArr[3], dArr[4], dArr[5]);
                        break;
                    case 4:
                        closePath();
                        break;
                }
                pathIterator.next();
                z2 = false;
            }
        }

        @Override // java.awt.geom.Path2D
        public final void transform(AffineTransform affineTransform) {
            affineTransform.transform(this.doubleCoords, 0, this.doubleCoords, 0, this.numCoords / 2);
        }

        @Override // java.awt.Shape
        public final synchronized Rectangle2D getBounds2D() {
            double d2;
            double d3;
            double d4;
            double d5;
            int i2 = this.numCoords;
            if (i2 > 0) {
                int i3 = i2 - 1;
                double d6 = this.doubleCoords[i3];
                d2 = d6;
                d4 = d6;
                int i4 = i3 - 1;
                double d7 = this.doubleCoords[i4];
                d3 = d7;
                d5 = d7;
                while (i4 > 0) {
                    int i5 = i4 - 1;
                    double d8 = this.doubleCoords[i5];
                    i4 = i5 - 1;
                    double d9 = this.doubleCoords[i4];
                    if (d9 < d5) {
                        d5 = d9;
                    }
                    if (d8 < d4) {
                        d4 = d8;
                    }
                    if (d9 > d3) {
                        d3 = d9;
                    }
                    if (d8 > d2) {
                        d2 = d8;
                    }
                }
            } else {
                d2 = 0.0d;
                d3 = 0.0d;
                d4 = 0.0d;
                d5 = 0.0d;
            }
            return new Rectangle2D.Double(d5, d4, d3 - d5, d2 - d4);
        }

        @Override // java.awt.Shape
        public final PathIterator getPathIterator(AffineTransform affineTransform) {
            if (affineTransform == null) {
                return new CopyIterator(this);
            }
            return new TxIterator(this, affineTransform);
        }

        @Override // java.awt.geom.Path2D
        public final Object clone() {
            return new Double(this);
        }

        private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            super.writeObject(objectOutputStream, true);
        }

        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            super.readObject(objectInputStream, true);
        }

        /* loaded from: rt.jar:java/awt/geom/Path2D$Double$CopyIterator.class */
        static class CopyIterator extends Iterator {
            double[] doubleCoords;

            CopyIterator(Double r4) {
                super(r4);
                this.doubleCoords = r4.doubleCoords;
            }

            @Override // java.awt.geom.PathIterator
            public int currentSegment(float[] fArr) {
                byte b2 = this.path.pointTypes[this.typeIdx];
                int i2 = curvecoords[b2];
                if (i2 > 0) {
                    for (int i3 = 0; i3 < i2; i3++) {
                        fArr[i3] = (float) this.doubleCoords[this.pointIdx + i3];
                    }
                }
                return b2;
            }

            @Override // java.awt.geom.PathIterator
            public int currentSegment(double[] dArr) {
                byte b2 = this.path.pointTypes[this.typeIdx];
                int i2 = curvecoords[b2];
                if (i2 > 0) {
                    System.arraycopy(this.doubleCoords, this.pointIdx, dArr, 0, i2);
                }
                return b2;
            }
        }

        /* loaded from: rt.jar:java/awt/geom/Path2D$Double$TxIterator.class */
        static class TxIterator extends Iterator {
            double[] doubleCoords;
            AffineTransform affine;

            TxIterator(Double r4, AffineTransform affineTransform) {
                super(r4);
                this.doubleCoords = r4.doubleCoords;
                this.affine = affineTransform;
            }

            @Override // java.awt.geom.PathIterator
            public int currentSegment(float[] fArr) {
                byte b2 = this.path.pointTypes[this.typeIdx];
                int i2 = curvecoords[b2];
                if (i2 > 0) {
                    this.affine.transform(this.doubleCoords, this.pointIdx, fArr, 0, i2 / 2);
                }
                return b2;
            }

            @Override // java.awt.geom.PathIterator
            public int currentSegment(double[] dArr) {
                byte b2 = this.path.pointTypes[this.typeIdx];
                int i2 = curvecoords[b2];
                if (i2 > 0) {
                    this.affine.transform(this.doubleCoords, this.pointIdx, dArr, 0, i2 / 2);
                }
                return b2;
            }
        }
    }

    public final synchronized void closePath() {
        if (this.numTypes == 0 || this.pointTypes[this.numTypes - 1] != 4) {
            needRoom(true, 0);
            byte[] bArr = this.pointTypes;
            int i2 = this.numTypes;
            this.numTypes = i2 + 1;
            bArr[i2] = 4;
        }
    }

    public final void append(Shape shape, boolean z2) {
        append(shape.getPathIterator(null), z2);
    }

    public final synchronized int getWindingRule() {
        return this.windingRule;
    }

    public final void setWindingRule(int i2) {
        if (i2 != 0 && i2 != 1) {
            throw new IllegalArgumentException("winding rule must be WIND_EVEN_ODD or WIND_NON_ZERO");
        }
        this.windingRule = i2;
    }

    public final synchronized Point2D getCurrentPoint() {
        int i2 = this.numCoords;
        if (this.numTypes < 1 || i2 < 1) {
            return null;
        }
        if (this.pointTypes[this.numTypes - 1] == 4) {
            for (int i3 = this.numTypes - 2; i3 > 0; i3--) {
                switch (this.pointTypes[i3]) {
                    case 1:
                        i2 -= 2;
                        break;
                    case 2:
                        i2 -= 4;
                        break;
                    case 3:
                        i2 -= 6;
                        break;
                }
            }
        }
        return getPoint(i2 - 2);
    }

    public final synchronized void reset() {
        this.numCoords = 0;
        this.numTypes = 0;
    }

    public final synchronized Shape createTransformedShape(AffineTransform affineTransform) {
        Path2D path2D = (Path2D) clone();
        if (affineTransform != null) {
            path2D.transform(affineTransform);
        }
        return path2D;
    }

    @Override // java.awt.Shape
    public final Rectangle getBounds() {
        return getBounds2D().getBounds();
    }

    public static boolean contains(PathIterator pathIterator, double d2, double d3) {
        if ((d2 * 0.0d) + (d3 * 0.0d) == 0.0d) {
            return (Curve.pointCrossingsForPath(pathIterator, d2, d3) & (pathIterator.getWindingRule() == 1 ? -1 : 1)) != 0;
        }
        return false;
    }

    public static boolean contains(PathIterator pathIterator, Point2D point2D) {
        return contains(pathIterator, point2D.getX(), point2D.getY());
    }

    @Override // java.awt.Shape
    public final boolean contains(double d2, double d3) {
        if ((d2 * 0.0d) + (d3 * 0.0d) != 0.0d || this.numTypes < 2) {
            return false;
        }
        return (pointCrossings(d2, d3) & (this.windingRule == 1 ? -1 : 1)) != 0;
    }

    @Override // java.awt.Shape
    public final boolean contains(Point2D point2D) {
        return contains(point2D.getX(), point2D.getY());
    }

    public static boolean contains(PathIterator pathIterator, double d2, double d3, double d4, double d5) {
        if (java.lang.Double.isNaN(d2 + d4) || java.lang.Double.isNaN(d3 + d5) || d4 <= 0.0d || d5 <= 0.0d) {
            return false;
        }
        int i2 = pathIterator.getWindingRule() == 1 ? -1 : 2;
        int iRectCrossingsForPath = Curve.rectCrossingsForPath(pathIterator, d2, d3, d2 + d4, d3 + d5);
        return (iRectCrossingsForPath == Integer.MIN_VALUE || (iRectCrossingsForPath & i2) == 0) ? false : true;
    }

    public static boolean contains(PathIterator pathIterator, Rectangle2D rectangle2D) {
        return contains(pathIterator, rectangle2D.getX(), rectangle2D.getY(), rectangle2D.getWidth(), rectangle2D.getHeight());
    }

    @Override // java.awt.Shape
    public final boolean contains(double d2, double d3, double d4, double d5) {
        if (java.lang.Double.isNaN(d2 + d4) || java.lang.Double.isNaN(d3 + d5) || d4 <= 0.0d || d5 <= 0.0d) {
            return false;
        }
        int i2 = this.windingRule == 1 ? -1 : 2;
        int iRectCrossings = rectCrossings(d2, d3, d2 + d4, d3 + d5);
        return (iRectCrossings == Integer.MIN_VALUE || (iRectCrossings & i2) == 0) ? false : true;
    }

    @Override // java.awt.Shape
    public final boolean contains(Rectangle2D rectangle2D) {
        return contains(rectangle2D.getX(), rectangle2D.getY(), rectangle2D.getWidth(), rectangle2D.getHeight());
    }

    public static boolean intersects(PathIterator pathIterator, double d2, double d3, double d4, double d5) {
        if (java.lang.Double.isNaN(d2 + d4) || java.lang.Double.isNaN(d3 + d5) || d4 <= 0.0d || d5 <= 0.0d) {
            return false;
        }
        int i2 = pathIterator.getWindingRule() == 1 ? -1 : 2;
        int iRectCrossingsForPath = Curve.rectCrossingsForPath(pathIterator, d2, d3, d2 + d4, d3 + d5);
        return iRectCrossingsForPath == Integer.MIN_VALUE || (iRectCrossingsForPath & i2) != 0;
    }

    public static boolean intersects(PathIterator pathIterator, Rectangle2D rectangle2D) {
        return intersects(pathIterator, rectangle2D.getX(), rectangle2D.getY(), rectangle2D.getWidth(), rectangle2D.getHeight());
    }

    @Override // java.awt.Shape
    public final boolean intersects(double d2, double d3, double d4, double d5) {
        if (java.lang.Double.isNaN(d2 + d4) || java.lang.Double.isNaN(d3 + d5) || d4 <= 0.0d || d5 <= 0.0d) {
            return false;
        }
        int i2 = this.windingRule == 1 ? -1 : 2;
        int iRectCrossings = rectCrossings(d2, d3, d2 + d4, d3 + d5);
        return iRectCrossings == Integer.MIN_VALUE || (iRectCrossings & i2) != 0;
    }

    @Override // java.awt.Shape
    public final boolean intersects(Rectangle2D rectangle2D) {
        return intersects(rectangle2D.getX(), rectangle2D.getY(), rectangle2D.getWidth(), rectangle2D.getHeight());
    }

    @Override // java.awt.Shape
    public final PathIterator getPathIterator(AffineTransform affineTransform, double d2) {
        return new FlatteningPathIterator(getPathIterator(affineTransform), d2);
    }

    final void writeObject(ObjectOutputStream objectOutputStream, boolean z2) throws IOException {
        float[] fArr;
        double[] dArr;
        int i2;
        int i3;
        objectOutputStream.defaultWriteObject();
        if (z2) {
            dArr = ((Double) this).doubleCoords;
            fArr = null;
        } else {
            fArr = ((Float) this).floatCoords;
            dArr = null;
        }
        int i4 = this.numTypes;
        objectOutputStream.writeByte(z2 ? 49 : 48);
        objectOutputStream.writeInt(i4);
        objectOutputStream.writeInt(this.numCoords);
        objectOutputStream.writeByte((byte) this.windingRule);
        int i5 = 0;
        for (int i6 = 0; i6 < i4; i6++) {
            switch (this.pointTypes[i6]) {
                case 0:
                    i2 = 1;
                    i3 = z2 ? 80 : 64;
                    break;
                case 1:
                    i2 = 1;
                    i3 = z2 ? 81 : 65;
                    break;
                case 2:
                    i2 = 2;
                    i3 = z2 ? 82 : 66;
                    break;
                case 3:
                    i2 = 3;
                    i3 = z2 ? 83 : 67;
                    break;
                case 4:
                    i2 = 0;
                    i3 = 96;
                    break;
                default:
                    throw new InternalError("unrecognized path type");
            }
            objectOutputStream.writeByte(i3);
            while (true) {
                i2--;
                if (i2 >= 0) {
                    if (z2) {
                        int i7 = i5;
                        int i8 = i5 + 1;
                        objectOutputStream.writeDouble(dArr[i7]);
                        i5 = i8 + 1;
                        objectOutputStream.writeDouble(dArr[i8]);
                    } else {
                        int i9 = i5;
                        int i10 = i5 + 1;
                        objectOutputStream.writeFloat(fArr[i9]);
                        i5 = i10 + 1;
                        objectOutputStream.writeFloat(fArr[i10]);
                    }
                }
            }
        }
        objectOutputStream.writeByte(97);
    }

    /* JADX WARN: Code restructure failed: missing block: B:59:0x0201, code lost:
    
        if (r0 < 0) goto L64;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x020a, code lost:
    
        if (r7.readByte() == 97) goto L72;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0216, code lost:
    
        throw new java.io.StreamCorruptedException("missing PATH_END");
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0217, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:?, code lost:
    
        return;
     */
    /* JADX WARN: Removed duplicated region for block: B:47:0x01a9  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x01ad  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x01ba A[LOOP:1: B:51:0x01ba->B:53:0x01c2, LOOP_START, PHI: r14
  0x01ba: PHI (r14v11 int) = (r14v8 int), (r14v12 int) binds: [B:50:0x01b7, B:53:0x01c2] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:54:0x01d1 A[LOOP:2: B:54:0x01d1->B:56:0x01d9, LOOP_START, PHI: r14
  0x01d1: PHI (r14v9 int) = (r14v8 int), (r14v10 int) binds: [B:50:0x01b7, B:56:0x01d9] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    final void readObject(java.io.ObjectInputStream r7, boolean r8) throws java.io.IOException, java.lang.ClassNotFoundException {
        /*
            Method dump skipped, instructions count: 536
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.awt.geom.Path2D.readObject(java.io.ObjectInputStream, boolean):void");
    }

    /* loaded from: rt.jar:java/awt/geom/Path2D$Iterator.class */
    static abstract class Iterator implements PathIterator {
        int typeIdx;
        int pointIdx;
        Path2D path;
        static final int[] curvecoords = {2, 2, 4, 6, 0};

        Iterator(Path2D path2D) {
            this.path = path2D;
        }

        @Override // java.awt.geom.PathIterator
        public int getWindingRule() {
            return this.path.getWindingRule();
        }

        @Override // java.awt.geom.PathIterator
        public boolean isDone() {
            return this.typeIdx >= this.path.numTypes;
        }

        @Override // java.awt.geom.PathIterator
        public void next() {
            byte[] bArr = this.path.pointTypes;
            int i2 = this.typeIdx;
            this.typeIdx = i2 + 1;
            this.pointIdx += curvecoords[bArr[i2]];
        }
    }
}
