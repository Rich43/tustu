package sun.font;

import java.awt.Shape;
import java.awt.font.LayoutPath;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;
import org.apache.commons.math3.geometry.VectorFormat;

/* loaded from: rt.jar:sun/font/LayoutPathImpl.class */
public abstract class LayoutPathImpl extends LayoutPath {
    private static final boolean LOGMAP = false;
    private static final Formatter LOG = new Formatter(System.out);

    public abstract double start();

    public abstract double end();

    public abstract double length();

    public abstract Shape mapShape(Shape shape);

    public Point2D pointToPath(double d2, double d3) {
        Point2D.Double r0 = new Point2D.Double(d2, d3);
        pointToPath(r0, r0);
        return r0;
    }

    public Point2D pathToPoint(double d2, double d3, boolean z2) {
        Point2D.Double r0 = new Point2D.Double(d2, d3);
        pathToPoint(r0, z2, r0);
        return r0;
    }

    public void pointToPath(double d2, double d3, Point2D point2D) {
        point2D.setLocation(d2, d3);
        pointToPath(point2D, point2D);
    }

    public void pathToPoint(double d2, double d3, boolean z2, Point2D point2D) {
        point2D.setLocation(d2, d3);
        pathToPoint(point2D, z2, point2D);
    }

    /* loaded from: rt.jar:sun/font/LayoutPathImpl$EndType.class */
    public enum EndType {
        PINNED,
        EXTENDED,
        CLOSED;

        public boolean isPinned() {
            return this == PINNED;
        }

        public boolean isExtended() {
            return this == EXTENDED;
        }

        public boolean isClosed() {
            return this == CLOSED;
        }
    }

    public static LayoutPathImpl getPath(EndType endType, double... dArr) {
        if ((dArr.length & 1) != 0) {
            throw new IllegalArgumentException("odd number of points not allowed");
        }
        return SegmentPath.get(endType, dArr);
    }

    /* loaded from: rt.jar:sun/font/LayoutPathImpl$SegmentPathBuilder.class */
    public static final class SegmentPathBuilder {
        private double[] data;

        /* renamed from: w, reason: collision with root package name */
        private int f13555w;
        private double px;
        private double py;

        /* renamed from: a, reason: collision with root package name */
        private double f13556a;
        private boolean pconnect;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !LayoutPathImpl.class.desiredAssertionStatus();
        }

        public void reset(int i2) {
            if (this.data == null || i2 > this.data.length) {
                this.data = new double[i2];
            } else if (i2 == 0) {
                this.data = null;
            }
            this.f13555w = 0;
            this.py = 0.0d;
            this.px = 0.0d;
            this.pconnect = false;
        }

        public SegmentPath build(EndType endType, double... dArr) {
            if (!$assertionsDisabled && dArr.length % 2 != 0) {
                throw new AssertionError();
            }
            reset((dArr.length / 2) * 3);
            int i2 = 0;
            while (i2 < dArr.length) {
                nextPoint(dArr[i2], dArr[i2 + 1], i2 != 0);
                i2 += 2;
            }
            return complete(endType);
        }

        public void moveTo(double d2, double d3) {
            nextPoint(d2, d3, false);
        }

        public void lineTo(double d2, double d3) {
            nextPoint(d2, d3, true);
        }

        private void nextPoint(double d2, double d3, boolean z2) {
            if (d2 == this.px && d3 == this.py) {
                return;
            }
            if (this.f13555w == 0) {
                if (this.data == null) {
                    this.data = new double[6];
                }
                if (z2) {
                    this.f13555w = 3;
                }
            }
            if (this.f13555w != 0 && !z2 && !this.pconnect) {
                double[] dArr = this.data;
                int i2 = this.f13555w - 3;
                this.px = d2;
                dArr[i2] = d2;
                double[] dArr2 = this.data;
                int i3 = this.f13555w - 2;
                this.py = d3;
                dArr2[i3] = d3;
                return;
            }
            if (this.f13555w == this.data.length) {
                double[] dArr3 = new double[this.f13555w * 2];
                System.arraycopy(this.data, 0, dArr3, 0, this.f13555w);
                this.data = dArr3;
            }
            if (z2) {
                double d4 = d2 - this.px;
                double d5 = d3 - this.py;
                this.f13556a += Math.sqrt((d4 * d4) + (d5 * d5));
            }
            double[] dArr4 = this.data;
            int i4 = this.f13555w;
            this.f13555w = i4 + 1;
            dArr4[i4] = d2;
            double[] dArr5 = this.data;
            int i5 = this.f13555w;
            this.f13555w = i5 + 1;
            dArr5[i5] = d3;
            double[] dArr6 = this.data;
            int i6 = this.f13555w;
            this.f13555w = i6 + 1;
            dArr6[i6] = this.f13556a;
            this.px = d2;
            this.py = d3;
            this.pconnect = z2;
        }

        public SegmentPath complete() {
            return complete(EndType.EXTENDED);
        }

        public SegmentPath complete(EndType endType) {
            SegmentPath segmentPath;
            if (this.data == null || this.f13555w < 6) {
                return null;
            }
            if (this.f13555w == this.data.length) {
                segmentPath = new SegmentPath(this.data, endType);
                reset(0);
            } else {
                double[] dArr = new double[this.f13555w];
                System.arraycopy(this.data, 0, dArr, 0, this.f13555w);
                segmentPath = new SegmentPath(dArr, endType);
                reset(2);
            }
            return segmentPath;
        }
    }

    /* loaded from: rt.jar:sun/font/LayoutPathImpl$SegmentPath.class */
    public static final class SegmentPath extends LayoutPathImpl {
        private double[] data;
        EndType etype;

        public static SegmentPath get(EndType endType, double... dArr) {
            return new SegmentPathBuilder().build(endType, dArr);
        }

        SegmentPath(double[] dArr, EndType endType) {
            this.data = dArr;
            this.etype = endType;
        }

        @Override // java.awt.font.LayoutPath
        public void pathToPoint(Point2D point2D, boolean z2, Point2D point2D2) {
            locateAndGetIndex(point2D, z2, point2D2);
        }

        @Override // java.awt.font.LayoutPath
        public boolean pointToPath(Point2D point2D, Point2D point2D2) {
            double d2;
            double d3;
            double d4;
            int length;
            double x2 = point2D.getX();
            double y2 = point2D.getY();
            double d5 = this.data[0];
            double d6 = this.data[1];
            double d7 = this.data[2];
            double d8 = Double.MAX_VALUE;
            double d9 = 0.0d;
            double d10 = 0.0d;
            double d11 = 0.0d;
            int i2 = 0;
            for (int i3 = 3; i3 < this.data.length; i3 += 3) {
                double d12 = this.data[i3];
                double d13 = this.data[i3 + 1];
                double d14 = this.data[i3 + 2];
                double d15 = d12 - d5;
                double d16 = d13 - d6;
                double d17 = d14 - d7;
                double d18 = (d15 * (x2 - d5)) + (d16 * (y2 - d6));
                if (d17 == 0.0d || (d18 < 0.0d && (!this.etype.isExtended() || i3 != 3))) {
                    d2 = d5;
                    d3 = d6;
                    d4 = d7;
                    length = i3;
                } else {
                    double d19 = d17 * d17;
                    if (d18 <= d19 || (this.etype.isExtended() && i3 == this.data.length - 3)) {
                        double d20 = d18 / d19;
                        d2 = d5 + (d20 * d15);
                        d3 = d6 + (d20 * d16);
                        d4 = d7 + (d20 * d17);
                        length = i3;
                    } else if (i3 == this.data.length - 3) {
                        d2 = d12;
                        d3 = d13;
                        d4 = d14;
                        length = this.data.length;
                    } else {
                        d5 = d12;
                        d6 = d13;
                        d7 = d14;
                    }
                }
                double d21 = x2 - d2;
                double d22 = y2 - d3;
                double d23 = (d21 * d21) + (d22 * d22);
                if (d23 <= d8) {
                    d8 = d23;
                    d9 = d2;
                    d10 = d3;
                    d11 = d4;
                    i2 = length;
                }
                d5 = d12;
                d6 = d13;
                d7 = d14;
            }
            double d24 = this.data[i2 - 3];
            double d25 = this.data[i2 - 2];
            if (d9 != d24 || d10 != d25) {
                double d26 = this.data[i2];
                double d27 = this.data[i2 + 1];
                double dSqrt = Math.sqrt(d8);
                if ((x2 - d9) * (d27 - d25) > (y2 - d10) * (d26 - d24)) {
                    dSqrt = -dSqrt;
                }
                point2D2.setLocation(d11, dSqrt);
                return false;
            }
            boolean z2 = (i2 == 3 || this.data[i2 - 1] == this.data[i2 - 4]) ? false : true;
            boolean z3 = (i2 == this.data.length || this.data[i2 - 1] == this.data[i2 + 2]) ? false : true;
            boolean z4 = this.etype.isExtended() && (i2 == 3 || i2 == this.data.length);
            if (z2 && z3) {
                Point2D.Double r0 = new Point2D.Double(x2, y2);
                calcoffset(i2 - 3, z4, r0);
                Point2D.Double r02 = new Point2D.Double(x2, y2);
                calcoffset(i2, z4, r02);
                if (Math.abs(r0.f12395y) > Math.abs(r02.f12395y)) {
                    point2D2.setLocation(r0);
                    return true;
                }
                point2D2.setLocation(r02);
                return false;
            }
            if (z2) {
                point2D2.setLocation(x2, y2);
                calcoffset(i2 - 3, z4, point2D2);
                return true;
            }
            point2D2.setLocation(x2, y2);
            calcoffset(i2, z4, point2D2);
            return false;
        }

        private void calcoffset(int i2, boolean z2, Point2D point2D) {
            double d2 = this.data[i2 - 3];
            double d3 = this.data[i2 - 2];
            double x2 = point2D.getX() - d2;
            double y2 = point2D.getY() - d3;
            double d4 = this.data[i2] - d2;
            double d5 = this.data[i2 + 1] - d3;
            double d6 = this.data[i2 + 2] - this.data[i2 - 1];
            double d7 = ((x2 * d4) + (y2 * d5)) / d6;
            double d8 = ((x2 * (-d5)) + (y2 * d4)) / d6;
            if (!z2) {
                if (d7 < 0.0d) {
                    d7 = 0.0d;
                } else if (d7 > d6) {
                    d7 = d6;
                }
            }
            point2D.setLocation(d7 + this.data[i2 - 1], d8);
        }

        @Override // sun.font.LayoutPathImpl
        public Shape mapShape(Shape shape) {
            return new Mapper().mapShape(shape);
        }

        @Override // sun.font.LayoutPathImpl
        public double start() {
            return this.data[2];
        }

        @Override // sun.font.LayoutPathImpl
        public double end() {
            return this.data[this.data.length - 1];
        }

        @Override // sun.font.LayoutPathImpl
        public double length() {
            return this.data[this.data.length - 1] - this.data[2];
        }

        private double getClosedAdvance(double d2, boolean z2) {
            if (this.etype.isClosed()) {
                double length = (d2 - this.data[2]) - (((int) (r0 / length())) * length());
                if (length < 0.0d || (length == 0.0d && z2)) {
                    length += length();
                }
                d2 = length + this.data[2];
            }
            return d2;
        }

        private int getSegmentIndexForAdvance(double d2, boolean z2) {
            double closedAdvance = getClosedAdvance(d2, z2);
            int i2 = 5;
            int length = this.data.length - 1;
            while (i2 < length) {
                double d3 = this.data[i2];
                if (closedAdvance < d3 || (closedAdvance == d3 && z2)) {
                    break;
                }
                i2 += 3;
            }
            return i2 - 2;
        }

        private void map(int i2, double d2, double d3, Point2D point2D) {
            double d4 = this.data[i2] - this.data[i2 - 3];
            double d5 = this.data[i2 + 1] - this.data[i2 - 2];
            double d6 = this.data[i2 + 2] - this.data[i2 - 1];
            double d7 = d4 / d6;
            double d8 = d5 / d6;
            double d9 = d2 - this.data[i2 - 1];
            point2D.setLocation((this.data[i2 - 3] + (d9 * d7)) - (d3 * d8), this.data[i2 - 2] + (d9 * d8) + (d3 * d7));
        }

        private int locateAndGetIndex(Point2D point2D, boolean z2, Point2D point2D2) {
            double x2 = point2D.getX();
            double y2 = point2D.getY();
            int segmentIndexForAdvance = getSegmentIndexForAdvance(x2, z2);
            map(segmentIndexForAdvance, x2, y2, point2D2);
            return segmentIndexForAdvance;
        }

        /* loaded from: rt.jar:sun/font/LayoutPathImpl$SegmentPath$LineInfo.class */
        class LineInfo {
            double sx;
            double sy;
            double lx;
            double ly;

            /* renamed from: m, reason: collision with root package name */
            double f13554m;

            LineInfo() {
            }

            void set(double d2, double d3, double d4, double d5) {
                this.sx = d2;
                this.sy = d3;
                this.lx = d4;
                this.ly = d5;
                double d6 = d4 - d2;
                if (d6 == 0.0d) {
                    this.f13554m = 0.0d;
                } else {
                    this.f13554m = (d5 - d3) / d6;
                }
            }

            void set(LineInfo lineInfo) {
                this.sx = lineInfo.sx;
                this.sy = lineInfo.sy;
                this.lx = lineInfo.lx;
                this.ly = lineInfo.ly;
                this.f13554m = lineInfo.f13554m;
            }

            boolean pin(double d2, double d3, LineInfo lineInfo) {
                lineInfo.set(this);
                if (this.lx >= this.sx) {
                    if (this.sx >= d3 || this.lx < d2) {
                        return false;
                    }
                    if (this.sx < d2) {
                        if (this.f13554m != 0.0d) {
                            lineInfo.sy = this.sy + (this.f13554m * (d2 - this.sx));
                        }
                        lineInfo.sx = d2;
                    }
                    if (this.lx > d3) {
                        if (this.f13554m != 0.0d) {
                            lineInfo.ly = this.ly + (this.f13554m * (d3 - this.lx));
                        }
                        lineInfo.lx = d3;
                        return true;
                    }
                    return true;
                }
                if (this.lx >= d3 || this.sx < d2) {
                    return false;
                }
                if (this.lx < d2) {
                    if (this.f13554m != 0.0d) {
                        lineInfo.ly = this.ly + (this.f13554m * (d2 - this.lx));
                    }
                    lineInfo.lx = d2;
                }
                if (this.sx > d3) {
                    if (this.f13554m != 0.0d) {
                        lineInfo.sy = this.sy + (this.f13554m * (d3 - this.sx));
                    }
                    lineInfo.sx = d3;
                    return true;
                }
                return true;
            }

            boolean pin(int i2, LineInfo lineInfo) {
                double d2 = SegmentPath.this.data[i2 - 1];
                double d3 = SegmentPath.this.data[i2 + 2];
                switch (SegmentPath.this.etype) {
                    case EXTENDED:
                        if (i2 == 3) {
                            d2 = Double.NEGATIVE_INFINITY;
                        }
                        if (i2 == SegmentPath.this.data.length - 3) {
                            d3 = Double.POSITIVE_INFINITY;
                            break;
                        }
                        break;
                }
                return pin(d2, d3, lineInfo);
            }
        }

        /* loaded from: rt.jar:sun/font/LayoutPathImpl$SegmentPath$Segment.class */
        class Segment {
            final int ix;
            final double ux;
            final double uy;
            final LineInfo temp;
            boolean broken;
            double cx;
            double cy;
            GeneralPath gp;

            Segment(int i2) {
                this.ix = i2;
                double d2 = SegmentPath.this.data[i2 + 2] - SegmentPath.this.data[i2 - 1];
                this.ux = (SegmentPath.this.data[i2] - SegmentPath.this.data[i2 - 3]) / d2;
                this.uy = (SegmentPath.this.data[i2 + 1] - SegmentPath.this.data[i2 - 2]) / d2;
                this.temp = SegmentPath.this.new LineInfo();
            }

            void init() {
                this.broken = true;
                this.cy = Double.MIN_VALUE;
                this.cx = Double.MIN_VALUE;
                this.gp = new GeneralPath();
            }

            void move() {
                this.broken = true;
            }

            void close() {
                if (!this.broken) {
                    this.gp.closePath();
                }
            }

            void line(LineInfo lineInfo) {
                if (lineInfo.pin(this.ix, this.temp)) {
                    this.temp.sx -= SegmentPath.this.data[this.ix - 1];
                    double d2 = (SegmentPath.this.data[this.ix - 3] + (this.temp.sx * this.ux)) - (this.temp.sy * this.uy);
                    double d3 = SegmentPath.this.data[this.ix - 2] + (this.temp.sx * this.uy) + (this.temp.sy * this.ux);
                    this.temp.lx -= SegmentPath.this.data[this.ix - 1];
                    double d4 = (SegmentPath.this.data[this.ix - 3] + (this.temp.lx * this.ux)) - (this.temp.ly * this.uy);
                    double d5 = SegmentPath.this.data[this.ix - 2] + (this.temp.lx * this.uy) + (this.temp.ly * this.ux);
                    if (d2 != this.cx || d3 != this.cy) {
                        if (this.broken) {
                            this.gp.moveTo((float) d2, (float) d3);
                        } else {
                            this.gp.lineTo((float) d2, (float) d3);
                        }
                    }
                    this.gp.lineTo((float) d4, (float) d5);
                    this.broken = false;
                    this.cx = d4;
                    this.cy = d5;
                }
            }
        }

        /* loaded from: rt.jar:sun/font/LayoutPathImpl$SegmentPath$Mapper.class */
        class Mapper {
            final LineInfo li;
            final ArrayList<Segment> segments = new ArrayList<>();
            final Point2D.Double mpt;
            final Point2D.Double cpt;
            boolean haveMT;

            Mapper() {
                this.li = SegmentPath.this.new LineInfo();
                for (int i2 = 3; i2 < SegmentPath.this.data.length; i2 += 3) {
                    if (SegmentPath.this.data[i2 + 2] != SegmentPath.this.data[i2 - 1]) {
                        this.segments.add(SegmentPath.this.new Segment(i2));
                    }
                }
                this.mpt = new Point2D.Double();
                this.cpt = new Point2D.Double();
            }

            void init() {
                this.haveMT = false;
                Iterator<Segment> it = this.segments.iterator();
                while (it.hasNext()) {
                    it.next().init();
                }
            }

            void moveTo(double d2, double d3) {
                this.mpt.f12394x = d2;
                this.mpt.f12395y = d3;
                this.haveMT = true;
            }

            void lineTo(double d2, double d3) {
                if (this.haveMT) {
                    this.cpt.f12394x = this.mpt.f12394x;
                    this.cpt.f12395y = this.mpt.f12395y;
                }
                if (d2 == this.cpt.f12394x && d3 == this.cpt.f12395y) {
                    return;
                }
                if (this.haveMT) {
                    this.haveMT = false;
                    Iterator<Segment> it = this.segments.iterator();
                    while (it.hasNext()) {
                        it.next().move();
                    }
                }
                this.li.set(this.cpt.f12394x, this.cpt.f12395y, d2, d3);
                Iterator<Segment> it2 = this.segments.iterator();
                while (it2.hasNext()) {
                    it2.next().line(this.li);
                }
                this.cpt.f12394x = d2;
                this.cpt.f12395y = d3;
            }

            void close() {
                lineTo(this.mpt.f12394x, this.mpt.f12395y);
                Iterator<Segment> it = this.segments.iterator();
                while (it.hasNext()) {
                    it.next().close();
                }
            }

            public Shape mapShape(Shape shape) {
                PathIterator pathIterator = shape.getPathIterator(null, 1.0d);
                init();
                double[] dArr = new double[2];
                while (!pathIterator.isDone()) {
                    switch (pathIterator.currentSegment(dArr)) {
                        case 0:
                            moveTo(dArr[0], dArr[1]);
                            break;
                        case 1:
                            lineTo(dArr[0], dArr[1]);
                            break;
                        case 4:
                            close();
                            break;
                    }
                    pathIterator.next();
                }
                GeneralPath generalPath = new GeneralPath();
                Iterator<Segment> it = this.segments.iterator();
                while (it.hasNext()) {
                    generalPath.append((Shape) it.next().gp, false);
                }
                return generalPath;
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(VectorFormat.DEFAULT_PREFIX);
            sb.append(this.etype.toString());
            sb.append(" ");
            for (int i2 = 0; i2 < this.data.length; i2 += 3) {
                if (i2 > 0) {
                    sb.append(",");
                }
                sb.append(VectorFormat.DEFAULT_PREFIX);
                sb.append(((int) (this.data[i2] * 100.0d)) / 100.0f);
                sb.append(",");
                sb.append(((int) (this.data[i2 + 1] * 100.0d)) / 100.0f);
                sb.append(",");
                sb.append(((int) (this.data[i2 + 2] * 10.0d)) / 10.0f);
                sb.append("}");
            }
            sb.append("}");
            return sb.toString();
        }
    }

    /* loaded from: rt.jar:sun/font/LayoutPathImpl$EmptyPath.class */
    public static class EmptyPath extends LayoutPathImpl {
        private AffineTransform tx;

        public EmptyPath(AffineTransform affineTransform) {
            this.tx = affineTransform;
        }

        @Override // java.awt.font.LayoutPath
        public void pathToPoint(Point2D point2D, boolean z2, Point2D point2D2) {
            if (this.tx != null) {
                this.tx.transform(point2D, point2D2);
            } else {
                point2D2.setLocation(point2D);
            }
        }

        @Override // java.awt.font.LayoutPath
        public boolean pointToPath(Point2D point2D, Point2D point2D2) {
            point2D2.setLocation(point2D);
            if (this.tx != null) {
                try {
                    this.tx.inverseTransform(point2D, point2D2);
                } catch (NoninvertibleTransformException e2) {
                }
            }
            return point2D2.getX() > 0.0d;
        }

        @Override // sun.font.LayoutPathImpl
        public double start() {
            return 0.0d;
        }

        @Override // sun.font.LayoutPathImpl
        public double end() {
            return 0.0d;
        }

        @Override // sun.font.LayoutPathImpl
        public double length() {
            return 0.0d;
        }

        @Override // sun.font.LayoutPathImpl
        public Shape mapShape(Shape shape) {
            if (this.tx != null) {
                return this.tx.createTransformedShape(shape);
            }
            return shape;
        }
    }
}
