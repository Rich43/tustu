package com.sun.openpisces;

import com.sun.javafx.geom.PathConsumer2D;
import java.util.Arrays;

/* loaded from: jfxrt.jar:com/sun/openpisces/Stroker.class */
public final class Stroker implements PathConsumer2D {
    private static final int MOVE_TO = 0;
    private static final int DRAWING_OP_TO = 1;
    private static final int CLOSE = 2;
    public static final int JOIN_MITER = 0;
    public static final int JOIN_ROUND = 1;
    public static final int JOIN_BEVEL = 2;
    public static final int CAP_BUTT = 0;
    public static final int CAP_ROUND = 1;
    public static final int CAP_SQUARE = 2;
    private PathConsumer2D out;
    private int capStyle;
    private int joinStyle;
    private float lineWidth2;
    private final float[][] offset;
    private final float[] miter;
    private float miterLimitSq;
    private int prev;
    private float sx0;
    private float sy0;
    private float sdx;
    private float sdy;
    private float cx0;
    private float cy0;
    private float cdx;
    private float cdy;
    private float smx;
    private float smy;
    private float cmx;
    private float cmy;
    private final PolyStack reverse;
    private static final float ROUND_JOIN_THRESHOLD = 0.015258789f;
    private float[] middle;
    private float[] lp;
    private float[] rp;
    private static final int MAX_N_CURVES = 11;
    private float[] subdivTs;

    /* renamed from: c, reason: collision with root package name */
    private static Curve f11994c;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Stroker.class.desiredAssertionStatus();
        f11994c = new Curve();
    }

    public Stroker(PathConsumer2D pc2d, float lineWidth, int capStyle, int joinStyle, float miterLimit) {
        this(pc2d);
        reset(lineWidth, capStyle, joinStyle, miterLimit);
    }

    public Stroker(PathConsumer2D pc2d) {
        this.offset = new float[3][2];
        this.miter = new float[2];
        this.reverse = new PolyStack();
        this.middle = new float[88];
        this.lp = new float[8];
        this.rp = new float[8];
        this.subdivTs = new float[10];
        setConsumer(pc2d);
    }

    public void setConsumer(PathConsumer2D pc2d) {
        this.out = pc2d;
    }

    public void reset(float lineWidth, int capStyle, int joinStyle, float miterLimit) {
        this.lineWidth2 = lineWidth / 2.0f;
        this.capStyle = capStyle;
        this.joinStyle = joinStyle;
        float limit = miterLimit * this.lineWidth2;
        this.miterLimitSq = limit * limit;
        this.prev = 2;
    }

    private static void computeOffset(float lx, float ly, float w2, float[] m2) {
        float len = (float) Math.sqrt((lx * lx) + (ly * ly));
        if (len == 0.0f) {
            m2[1] = 0.0f;
            m2[0] = 0.0f;
        } else {
            m2[0] = (ly * w2) / len;
            m2[1] = (-(lx * w2)) / len;
        }
    }

    private static boolean isCW(float dx1, float dy1, float dx2, float dy2) {
        return dx1 * dy2 <= dy1 * dx2;
    }

    private void drawRoundJoin(float x2, float y2, float omx, float omy, float mx, float my, boolean rev, float threshold) {
        if (omx == 0.0f && omy == 0.0f) {
            return;
        }
        if (mx == 0.0f && my == 0.0f) {
            return;
        }
        float domx = omx - mx;
        float domy = omy - my;
        float len = (domx * domx) + (domy * domy);
        if (len < threshold) {
            return;
        }
        if (rev) {
            omx = -omx;
            omy = -omy;
            mx = -mx;
            my = -my;
        }
        drawRoundJoin(x2, y2, omx, omy, mx, my, rev);
    }

    private void drawRoundJoin(float cx, float cy, float omx, float omy, float mx, float my, boolean rev) {
        double cosext = (omx * mx) + (omy * my);
        int numCurves = cosext >= 0.0d ? 1 : 2;
        switch (numCurves) {
            case 1:
                drawBezApproxForArc(cx, cy, omx, omy, mx, my, rev);
                break;
            case 2:
                float nx = my - omy;
                float ny = omx - mx;
                float nlen = (float) Math.sqrt((nx * nx) + (ny * ny));
                float scale = this.lineWidth2 / nlen;
                float mmx = nx * scale;
                float mmy = ny * scale;
                if (rev) {
                    mmx = -mmx;
                    mmy = -mmy;
                }
                drawBezApproxForArc(cx, cy, omx, omy, mmx, mmy, rev);
                drawBezApproxForArc(cx, cy, mmx, mmy, mx, my, rev);
                break;
        }
    }

    private void drawBezApproxForArc(float cx, float cy, float omx, float omy, float mx, float my, boolean rev) {
        float cosext2 = ((omx * mx) + (omy * my)) / ((2.0f * this.lineWidth2) * this.lineWidth2);
        float cv = (float) ((1.3333333333333333d * Math.sqrt(0.5d - cosext2)) / (1.0d + Math.sqrt(cosext2 + 0.5d)));
        if (rev) {
            cv = -cv;
        }
        float x1 = cx + omx;
        float y1 = cy + omy;
        float x2 = x1 - (cv * omy);
        float y2 = y1 + (cv * omx);
        float x4 = cx + mx;
        float y4 = cy + my;
        float x3 = x4 + (cv * my);
        float y3 = y4 - (cv * mx);
        emitCurveTo(x1, y1, x2, y2, x3, y3, x4, y4, rev);
    }

    private void drawRoundCap(float cx, float cy, float mx, float my) {
        emitCurveTo(cx + mx, cy + my, (cx + mx) - (0.5522848f * my), cy + my + (0.5522848f * mx), (cx - my) + (0.5522848f * mx), cy + mx + (0.5522848f * my), cx - my, cy + mx, false);
        emitCurveTo(cx - my, cy + mx, (cx - my) - (0.5522848f * mx), (cy + mx) - (0.5522848f * my), (cx - mx) - (0.5522848f * my), (cy - my) + (0.5522848f * mx), cx - mx, cy - my, false);
    }

    private void computeMiter(float x0, float y0, float x1, float y1, float x0p, float y0p, float x1p, float y1p, float[] m2, int off) {
        float x10 = x1 - x0;
        float y10 = y1 - y0;
        float x10p = x1p - x0p;
        float y10p = y1p - y0p;
        float den = (x10 * y10p) - (x10p * y10);
        float t2 = ((x10p * (y0 - y0p)) - (y10p * (x0 - x0p))) / den;
        m2[off] = x0 + (t2 * x10);
        m2[off + 1] = y0 + (t2 * y10);
    }

    private void safecomputeMiter(float x0, float y0, float x1, float y1, float x0p, float y0p, float x1p, float y1p, float[] m2, int off) {
        float x10 = x1 - x0;
        float y10 = y1 - y0;
        float x10p = x1p - x0p;
        float y10p = y1p - y0p;
        float den = (x10 * y10p) - (x10p * y10);
        if (den == 0.0f) {
            m2[off] = (x0 + x0p) / 2.0f;
            m2[off + 1] = (y0 + y0p) / 2.0f;
        } else {
            float t2 = ((x10p * (y0 - y0p)) - (y10p * (x0 - x0p))) / den;
            m2[off] = x0 + (t2 * x10);
            m2[off + 1] = y0 + (t2 * y10);
        }
    }

    private void drawMiter(float pdx, float pdy, float x0, float y0, float dx, float dy, float omx, float omy, float mx, float my, boolean rev) {
        if (mx == omx && my == omy) {
            return;
        }
        if (pdx == 0.0f && pdy == 0.0f) {
            return;
        }
        if (dx == 0.0f && dy == 0.0f) {
            return;
        }
        if (rev) {
            omx = -omx;
            omy = -omy;
            mx = -mx;
            my = -my;
        }
        computeMiter((x0 - pdx) + omx, (y0 - pdy) + omy, x0 + omx, y0 + omy, dx + x0 + mx, dy + y0 + my, x0 + mx, y0 + my, this.miter, 0);
        float lenSq = ((this.miter[0] - x0) * (this.miter[0] - x0)) + ((this.miter[1] - y0) * (this.miter[1] - y0));
        if (lenSq < this.miterLimitSq) {
            emitLineTo(this.miter[0], this.miter[1], rev);
        }
    }

    @Override // com.sun.javafx.geom.PathConsumer2D
    public void moveTo(float x0, float y0) {
        if (this.prev == 1) {
            finish();
        }
        this.cx0 = x0;
        this.sx0 = x0;
        this.cy0 = y0;
        this.sy0 = y0;
        this.sdx = 1.0f;
        this.cdx = 1.0f;
        this.sdy = 0.0f;
        this.cdy = 0.0f;
        this.prev = 0;
    }

    @Override // com.sun.javafx.geom.PathConsumer2D
    public void lineTo(float x1, float y1) {
        float dx = x1 - this.cx0;
        float dy = y1 - this.cy0;
        if (dx == 0.0f && dy == 0.0f) {
            dx = 1.0f;
        }
        computeOffset(dx, dy, this.lineWidth2, this.offset[0]);
        float mx = this.offset[0][0];
        float my = this.offset[0][1];
        drawJoin(this.cdx, this.cdy, this.cx0, this.cy0, dx, dy, this.cmx, this.cmy, mx, my);
        emitLineTo(this.cx0 + mx, this.cy0 + my);
        emitLineTo(x1 + mx, y1 + my);
        emitLineTo(this.cx0 - mx, this.cy0 - my, true);
        emitLineTo(x1 - mx, y1 - my, true);
        this.cmx = mx;
        this.cmy = my;
        this.cdx = dx;
        this.cdy = dy;
        this.cx0 = x1;
        this.cy0 = y1;
        this.prev = 1;
    }

    @Override // com.sun.javafx.geom.PathConsumer2D
    public void closePath() {
        if (this.prev != 1) {
            if (this.prev == 2) {
                return;
            }
            emitMoveTo(this.cx0, this.cy0 - this.lineWidth2);
            this.smx = 0.0f;
            this.cmx = 0.0f;
            float f2 = -this.lineWidth2;
            this.smy = f2;
            this.cmy = f2;
            this.sdx = 1.0f;
            this.cdx = 1.0f;
            this.sdy = 0.0f;
            this.cdy = 0.0f;
            finish();
            return;
        }
        if (this.cx0 != this.sx0 || this.cy0 != this.sy0) {
            lineTo(this.sx0, this.sy0);
        }
        drawJoin(this.cdx, this.cdy, this.cx0, this.cy0, this.sdx, this.sdy, this.cmx, this.cmy, this.smx, this.smy);
        emitLineTo(this.sx0 + this.smx, this.sy0 + this.smy);
        emitMoveTo(this.sx0 - this.smx, this.sy0 - this.smy);
        emitReverse();
        this.prev = 2;
        emitClose();
    }

    private void emitReverse() {
        while (!this.reverse.isEmpty()) {
            this.reverse.pop(this.out);
        }
    }

    @Override // com.sun.javafx.geom.PathConsumer2D
    public void pathDone() {
        if (this.prev == 1) {
            finish();
        }
        this.out.pathDone();
        this.prev = 2;
    }

    private void finish() {
        if (this.capStyle == 1) {
            drawRoundCap(this.cx0, this.cy0, this.cmx, this.cmy);
        } else if (this.capStyle == 2) {
            emitLineTo((this.cx0 - this.cmy) + this.cmx, this.cy0 + this.cmx + this.cmy);
            emitLineTo((this.cx0 - this.cmy) - this.cmx, (this.cy0 + this.cmx) - this.cmy);
        }
        emitReverse();
        if (this.capStyle == 1) {
            drawRoundCap(this.sx0, this.sy0, -this.smx, -this.smy);
        } else if (this.capStyle == 2) {
            emitLineTo((this.sx0 + this.smy) - this.smx, (this.sy0 - this.smx) - this.smy);
            emitLineTo(this.sx0 + this.smy + this.smx, (this.sy0 - this.smx) + this.smy);
        }
        emitClose();
    }

    private void emitMoveTo(float x0, float y0) {
        this.out.moveTo(x0, y0);
    }

    private void emitLineTo(float x1, float y1) {
        this.out.lineTo(x1, y1);
    }

    private void emitLineTo(float x1, float y1, boolean rev) {
        if (rev) {
            this.reverse.pushLine(x1, y1);
        } else {
            emitLineTo(x1, y1);
        }
    }

    private void emitQuadTo(float x0, float y0, float x1, float y1, float x2, float y2, boolean rev) {
        if (rev) {
            this.reverse.pushQuad(x0, y0, x1, y1);
        } else {
            this.out.quadTo(x1, y1, x2, y2);
        }
    }

    private void emitCurveTo(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3, boolean rev) {
        if (rev) {
            this.reverse.pushCubic(x0, y0, x1, y1, x2, y2);
        } else {
            this.out.curveTo(x1, y1, x2, y2, x3, y3);
        }
    }

    private void emitClose() {
        this.out.closePath();
    }

    private void drawJoin(float pdx, float pdy, float x0, float y0, float dx, float dy, float omx, float omy, float mx, float my) {
        if (this.prev != 1) {
            emitMoveTo(x0 + mx, y0 + my);
            this.sdx = dx;
            this.sdy = dy;
            this.smx = mx;
            this.smy = my;
        } else {
            boolean cw = isCW(pdx, pdy, dx, dy);
            if (this.joinStyle == 0) {
                drawMiter(pdx, pdy, x0, y0, dx, dy, omx, omy, mx, my, cw);
            } else if (this.joinStyle == 1) {
                drawRoundJoin(x0, y0, omx, omy, mx, my, cw, ROUND_JOIN_THRESHOLD);
            }
            emitLineTo(x0, y0, !cw);
        }
        this.prev = 1;
    }

    private static boolean within(float x1, float y1, float x2, float y2, float ERR) {
        if ($assertionsDisabled || ERR > 0.0f) {
            return Helpers.within(x1, x2, ERR) && Helpers.within(y1, y2, ERR);
        }
        throw new AssertionError((Object) "");
    }

    private void getLineOffsets(float x1, float y1, float x2, float y2, float[] left, float[] right) {
        computeOffset(x2 - x1, y2 - y1, this.lineWidth2, this.offset[0]);
        left[0] = x1 + this.offset[0][0];
        left[1] = y1 + this.offset[0][1];
        left[2] = x2 + this.offset[0][0];
        left[3] = y2 + this.offset[0][1];
        right[0] = x1 - this.offset[0][0];
        right[1] = y1 - this.offset[0][1];
        right[2] = x2 - this.offset[0][0];
        right[3] = y2 - this.offset[0][1];
    }

    private int computeOffsetCubic(float[] pts, int off, float[] leftOff, float[] rightOff) {
        float x1 = pts[off + 0];
        float y1 = pts[off + 1];
        float x2 = pts[off + 2];
        float y2 = pts[off + 3];
        float x3 = pts[off + 4];
        float y3 = pts[off + 5];
        float x4 = pts[off + 6];
        float y4 = pts[off + 7];
        float dx4 = x4 - x3;
        float dy4 = y4 - y3;
        float dx1 = x2 - x1;
        float dy1 = y2 - y1;
        boolean p1eqp2 = within(x1, y1, x2, y2, 6.0f * Math.ulp(y2));
        boolean p3eqp4 = within(x3, y3, x4, y4, 6.0f * Math.ulp(y4));
        if (p1eqp2 && p3eqp4) {
            getLineOffsets(x1, y1, x4, y4, leftOff, rightOff);
            return 4;
        }
        if (p1eqp2) {
            dx1 = x3 - x1;
            dy1 = y3 - y1;
        } else if (p3eqp4) {
            dx4 = x4 - x2;
            dy4 = y4 - y2;
        }
        float dotsq = (dx1 * dx4) + (dy1 * dy4);
        float dotsq2 = dotsq * dotsq;
        float l1sq = (dx1 * dx1) + (dy1 * dy1);
        float l4sq = (dx4 * dx4) + (dy4 * dy4);
        if (Helpers.within(dotsq2, l1sq * l4sq, 4.0f * Math.ulp(dotsq2))) {
            getLineOffsets(x1, y1, x4, y4, leftOff, rightOff);
            return 4;
        }
        float x5 = 0.125f * (x1 + (3.0f * (x2 + x3)) + x4);
        float y5 = 0.125f * (y1 + (3.0f * (y2 + y3)) + y4);
        float dxm = ((x3 + x4) - x1) - x2;
        float dym = ((y3 + y4) - y1) - y2;
        computeOffset(dx1, dy1, this.lineWidth2, this.offset[0]);
        computeOffset(dxm, dym, this.lineWidth2, this.offset[1]);
        computeOffset(dx4, dy4, this.lineWidth2, this.offset[2]);
        float x1p = x1 + this.offset[0][0];
        float y1p = y1 + this.offset[0][1];
        float xi = x5 + this.offset[1][0];
        float yi = y5 + this.offset[1][1];
        float x4p = x4 + this.offset[2][0];
        float y4p = y4 + this.offset[2][1];
        float invdet43 = 4.0f / (3.0f * ((dx1 * dy4) - (dy1 * dx4)));
        float two_pi_m_p1_m_p4x = ((2.0f * xi) - x1p) - x4p;
        float two_pi_m_p1_m_p4y = ((2.0f * yi) - y1p) - y4p;
        float c1 = invdet43 * ((dy4 * two_pi_m_p1_m_p4x) - (dx4 * two_pi_m_p1_m_p4y));
        float c2 = invdet43 * ((dx1 * two_pi_m_p1_m_p4y) - (dy1 * two_pi_m_p1_m_p4x));
        float x2p = x1p + (c1 * dx1);
        float y2p = y1p + (c1 * dy1);
        float x3p = x4p + (c2 * dx4);
        float y3p = y4p + (c2 * dy4);
        leftOff[0] = x1p;
        leftOff[1] = y1p;
        leftOff[2] = x2p;
        leftOff[3] = y2p;
        leftOff[4] = x3p;
        leftOff[5] = y3p;
        leftOff[6] = x4p;
        leftOff[7] = y4p;
        float x1p2 = x1 - this.offset[0][0];
        float y1p2 = y1 - this.offset[0][1];
        float xi2 = xi - (2.0f * this.offset[1][0]);
        float yi2 = yi - (2.0f * this.offset[1][1]);
        float x4p2 = x4 - this.offset[2][0];
        float y4p2 = y4 - this.offset[2][1];
        float two_pi_m_p1_m_p4x2 = ((2.0f * xi2) - x1p2) - x4p2;
        float two_pi_m_p1_m_p4y2 = ((2.0f * yi2) - y1p2) - y4p2;
        float c12 = invdet43 * ((dy4 * two_pi_m_p1_m_p4x2) - (dx4 * two_pi_m_p1_m_p4y2));
        float c22 = invdet43 * ((dx1 * two_pi_m_p1_m_p4y2) - (dy1 * two_pi_m_p1_m_p4x2));
        float x2p2 = x1p2 + (c12 * dx1);
        float y2p2 = y1p2 + (c12 * dy1);
        float x3p2 = x4p2 + (c22 * dx4);
        float y3p2 = y4p2 + (c22 * dy4);
        rightOff[0] = x1p2;
        rightOff[1] = y1p2;
        rightOff[2] = x2p2;
        rightOff[3] = y2p2;
        rightOff[4] = x3p2;
        rightOff[5] = y3p2;
        rightOff[6] = x4p2;
        rightOff[7] = y4p2;
        return 8;
    }

    private int computeOffsetQuad(float[] pts, int off, float[] leftOff, float[] rightOff) {
        float x1 = pts[off + 0];
        float y1 = pts[off + 1];
        float x2 = pts[off + 2];
        float y2 = pts[off + 3];
        float x3 = pts[off + 4];
        float y3 = pts[off + 5];
        float dx3 = x3 - x2;
        float dy3 = y3 - y2;
        float dx1 = x2 - x1;
        float dy1 = y2 - y1;
        boolean p1eqp2 = within(x1, y1, x2, y2, 6.0f * Math.ulp(y2));
        boolean p2eqp3 = within(x2, y2, x3, y3, 6.0f * Math.ulp(y3));
        if (p1eqp2 || p2eqp3) {
            getLineOffsets(x1, y1, x3, y3, leftOff, rightOff);
            return 4;
        }
        float dotsq = (dx1 * dx3) + (dy1 * dy3);
        float dotsq2 = dotsq * dotsq;
        float l1sq = (dx1 * dx1) + (dy1 * dy1);
        float l3sq = (dx3 * dx3) + (dy3 * dy3);
        if (Helpers.within(dotsq2, l1sq * l3sq, 4.0f * Math.ulp(dotsq2))) {
            getLineOffsets(x1, y1, x3, y3, leftOff, rightOff);
            return 4;
        }
        computeOffset(dx1, dy1, this.lineWidth2, this.offset[0]);
        computeOffset(dx3, dy3, this.lineWidth2, this.offset[1]);
        float x1p = x1 + this.offset[0][0];
        float y1p = y1 + this.offset[0][1];
        float x3p = x3 + this.offset[1][0];
        float y3p = y3 + this.offset[1][1];
        safecomputeMiter(x1p, y1p, x1p + dx1, y1p + dy1, x3p, y3p, x3p - dx3, y3p - dy3, leftOff, 2);
        leftOff[0] = x1p;
        leftOff[1] = y1p;
        leftOff[4] = x3p;
        leftOff[5] = y3p;
        float x1p2 = x1 - this.offset[0][0];
        float y1p2 = y1 - this.offset[0][1];
        float x3p2 = x3 - this.offset[1][0];
        float y3p2 = y3 - this.offset[1][1];
        safecomputeMiter(x1p2, y1p2, x1p2 + dx1, y1p2 + dy1, x3p2, y3p2, x3p2 - dx3, y3p2 - dy3, rightOff, 2);
        rightOff[0] = x1p2;
        rightOff[1] = y1p2;
        rightOff[4] = x3p2;
        rightOff[5] = y3p2;
        return 6;
    }

    private static int findSubdivPoints(float[] pts, float[] ts, int type, float w2) {
        float x12 = pts[2] - pts[0];
        float y12 = pts[3] - pts[1];
        if (y12 != 0.0f && x12 != 0.0f) {
            float hypot = (float) Math.sqrt((x12 * x12) + (y12 * y12));
            float cos = x12 / hypot;
            float sin = y12 / hypot;
            float x1 = (cos * pts[0]) + (sin * pts[1]);
            float y1 = (cos * pts[1]) - (sin * pts[0]);
            float x2 = (cos * pts[2]) + (sin * pts[3]);
            float y2 = (cos * pts[3]) - (sin * pts[2]);
            float x3 = (cos * pts[4]) + (sin * pts[5]);
            float y3 = (cos * pts[5]) - (sin * pts[4]);
            switch (type) {
                case 6:
                    f11994c.set(x1, y1, x2, y2, x3, y3);
                    break;
                case 8:
                    float x4 = (cos * pts[6]) + (sin * pts[7]);
                    float y4 = (cos * pts[7]) - (sin * pts[6]);
                    f11994c.set(x1, y1, x2, y2, x3, y3, x4, y4);
                    break;
            }
        } else {
            f11994c.set(pts, type);
        }
        int ret = 0 + f11994c.dxRoots(ts, 0);
        int ret2 = ret + f11994c.dyRoots(ts, ret);
        if (type == 8) {
            ret2 += f11994c.infPoints(ts, ret2);
        }
        int ret3 = Helpers.filterOutNotInAB(ts, 0, ret2 + f11994c.rootsOfROCMinusW(ts, ret2, w2, 1.0E-4f), 1.0E-4f, 0.9999f);
        Helpers.isort(ts, 0, ret3);
        return ret3;
    }

    @Override // com.sun.javafx.geom.PathConsumer2D
    public void curveTo(float x1, float y1, float x2, float y2, float x3, float y3) {
        this.middle[0] = this.cx0;
        this.middle[1] = this.cy0;
        this.middle[2] = x1;
        this.middle[3] = y1;
        this.middle[4] = x2;
        this.middle[5] = y2;
        this.middle[6] = x3;
        this.middle[7] = y3;
        float xf = this.middle[6];
        float yf = this.middle[7];
        float dxs = this.middle[2] - this.middle[0];
        float dys = this.middle[3] - this.middle[1];
        float dxf = this.middle[6] - this.middle[4];
        float dyf = this.middle[7] - this.middle[5];
        boolean p1eqp2 = dxs == 0.0f && dys == 0.0f;
        boolean p3eqp4 = dxf == 0.0f && dyf == 0.0f;
        if (p1eqp2) {
            dxs = this.middle[4] - this.middle[0];
            dys = this.middle[5] - this.middle[1];
            if (dxs == 0.0f && dys == 0.0f) {
                dxs = this.middle[6] - this.middle[0];
                dys = this.middle[7] - this.middle[1];
            }
        }
        if (p3eqp4) {
            dxf = this.middle[6] - this.middle[2];
            dyf = this.middle[7] - this.middle[3];
            if (dxf == 0.0f && dyf == 0.0f) {
                dxf = this.middle[6] - this.middle[0];
                dyf = this.middle[7] - this.middle[1];
            }
        }
        if (dxs == 0.0f && dys == 0.0f) {
            lineTo(this.middle[0], this.middle[1]);
            return;
        }
        if (Math.abs(dxs) < 0.1f && Math.abs(dys) < 0.1f) {
            float len = (float) Math.sqrt((dxs * dxs) + (dys * dys));
            dxs /= len;
            dys /= len;
        }
        if (Math.abs(dxf) < 0.1f && Math.abs(dyf) < 0.1f) {
            float len2 = (float) Math.sqrt((dxf * dxf) + (dyf * dyf));
            dxf /= len2;
            dyf /= len2;
        }
        computeOffset(dxs, dys, this.lineWidth2, this.offset[0]);
        float mx = this.offset[0][0];
        float my = this.offset[0][1];
        drawJoin(this.cdx, this.cdy, this.cx0, this.cy0, dxs, dys, this.cmx, this.cmy, mx, my);
        int nSplits = findSubdivPoints(this.middle, this.subdivTs, 8, this.lineWidth2);
        float prevT = 0.0f;
        for (int i2 = 0; i2 < nSplits; i2++) {
            float t2 = this.subdivTs[i2];
            Helpers.subdivideCubicAt((t2 - prevT) / (1.0f - prevT), this.middle, i2 * 6, this.middle, i2 * 6, this.middle, (i2 * 6) + 6);
            prevT = t2;
        }
        int kind = 0;
        for (int i3 = 0; i3 <= nSplits; i3++) {
            kind = computeOffsetCubic(this.middle, i3 * 6, this.lp, this.rp);
            if (kind != 0) {
                emitLineTo(this.lp[0], this.lp[1]);
                switch (kind) {
                    case 4:
                        emitLineTo(this.lp[2], this.lp[3]);
                        emitLineTo(this.rp[0], this.rp[1], true);
                        break;
                    case 8:
                        emitCurveTo(this.lp[0], this.lp[1], this.lp[2], this.lp[3], this.lp[4], this.lp[5], this.lp[6], this.lp[7], false);
                        emitCurveTo(this.rp[0], this.rp[1], this.rp[2], this.rp[3], this.rp[4], this.rp[5], this.rp[6], this.rp[7], true);
                        break;
                }
                emitLineTo(this.rp[kind - 2], this.rp[kind - 1], true);
            }
        }
        this.cmx = (this.lp[kind - 2] - this.rp[kind - 2]) / 2.0f;
        this.cmy = (this.lp[kind - 1] - this.rp[kind - 1]) / 2.0f;
        this.cdx = dxf;
        this.cdy = dyf;
        this.cx0 = xf;
        this.cy0 = yf;
        this.prev = 1;
    }

    @Override // com.sun.javafx.geom.PathConsumer2D
    public void quadTo(float x1, float y1, float x2, float y2) {
        this.middle[0] = this.cx0;
        this.middle[1] = this.cy0;
        this.middle[2] = x1;
        this.middle[3] = y1;
        this.middle[4] = x2;
        this.middle[5] = y2;
        float xf = this.middle[4];
        float yf = this.middle[5];
        float dxs = this.middle[2] - this.middle[0];
        float dys = this.middle[3] - this.middle[1];
        float dxf = this.middle[4] - this.middle[2];
        float dyf = this.middle[5] - this.middle[3];
        if ((dxs == 0.0f && dys == 0.0f) || (dxf == 0.0f && dyf == 0.0f)) {
            float f2 = this.middle[4] - this.middle[0];
            dxf = f2;
            dxs = f2;
            float f3 = this.middle[5] - this.middle[1];
            dyf = f3;
            dys = f3;
        }
        if (dxs == 0.0f && dys == 0.0f) {
            lineTo(this.middle[0], this.middle[1]);
            return;
        }
        if (Math.abs(dxs) < 0.1f && Math.abs(dys) < 0.1f) {
            float len = (float) Math.sqrt((dxs * dxs) + (dys * dys));
            dxs /= len;
            dys /= len;
        }
        if (Math.abs(dxf) < 0.1f && Math.abs(dyf) < 0.1f) {
            float len2 = (float) Math.sqrt((dxf * dxf) + (dyf * dyf));
            dxf /= len2;
            dyf /= len2;
        }
        computeOffset(dxs, dys, this.lineWidth2, this.offset[0]);
        float mx = this.offset[0][0];
        float my = this.offset[0][1];
        drawJoin(this.cdx, this.cdy, this.cx0, this.cy0, dxs, dys, this.cmx, this.cmy, mx, my);
        int nSplits = findSubdivPoints(this.middle, this.subdivTs, 6, this.lineWidth2);
        float prevt = 0.0f;
        for (int i2 = 0; i2 < nSplits; i2++) {
            float t2 = this.subdivTs[i2];
            Helpers.subdivideQuadAt((t2 - prevt) / (1.0f - prevt), this.middle, i2 * 4, this.middle, i2 * 4, this.middle, (i2 * 4) + 4);
            prevt = t2;
        }
        int kind = 0;
        for (int i3 = 0; i3 <= nSplits; i3++) {
            kind = computeOffsetQuad(this.middle, i3 * 4, this.lp, this.rp);
            if (kind != 0) {
                emitLineTo(this.lp[0], this.lp[1]);
                switch (kind) {
                    case 4:
                        emitLineTo(this.lp[2], this.lp[3]);
                        emitLineTo(this.rp[0], this.rp[1], true);
                        break;
                    case 6:
                        emitQuadTo(this.lp[0], this.lp[1], this.lp[2], this.lp[3], this.lp[4], this.lp[5], false);
                        emitQuadTo(this.rp[0], this.rp[1], this.rp[2], this.rp[3], this.rp[4], this.rp[5], true);
                        break;
                }
                emitLineTo(this.rp[kind - 2], this.rp[kind - 1], true);
            }
        }
        this.cmx = (this.lp[kind - 2] - this.rp[kind - 2]) / 2.0f;
        this.cmy = (this.lp[kind - 1] - this.rp[kind - 1]) / 2.0f;
        this.cdx = dxf;
        this.cdy = dyf;
        this.cx0 = xf;
        this.cy0 = yf;
        this.prev = 1;
    }

    /* loaded from: jfxrt.jar:com/sun/openpisces/Stroker$PolyStack.class */
    private static final class PolyStack {
        private static final int INIT_SIZE = 50;
        float[] curves = new float[400];
        int[] curveTypes = new int[50];
        int end = 0;
        int numCurves = 0;

        PolyStack() {
        }

        public boolean isEmpty() {
            return this.numCurves == 0;
        }

        private void ensureSpace(int n2) {
            if (this.end + n2 >= this.curves.length) {
                int newSize = (this.end + n2) * 2;
                this.curves = Arrays.copyOf(this.curves, newSize);
            }
            if (this.numCurves >= this.curveTypes.length) {
                int newSize2 = this.numCurves * 2;
                this.curveTypes = Arrays.copyOf(this.curveTypes, newSize2);
            }
        }

        public void pushCubic(float x0, float y0, float x1, float y1, float x2, float y2) {
            ensureSpace(6);
            int[] iArr = this.curveTypes;
            int i2 = this.numCurves;
            this.numCurves = i2 + 1;
            iArr[i2] = 8;
            float[] fArr = this.curves;
            int i3 = this.end;
            this.end = i3 + 1;
            fArr[i3] = x2;
            float[] fArr2 = this.curves;
            int i4 = this.end;
            this.end = i4 + 1;
            fArr2[i4] = y2;
            float[] fArr3 = this.curves;
            int i5 = this.end;
            this.end = i5 + 1;
            fArr3[i5] = x1;
            float[] fArr4 = this.curves;
            int i6 = this.end;
            this.end = i6 + 1;
            fArr4[i6] = y1;
            float[] fArr5 = this.curves;
            int i7 = this.end;
            this.end = i7 + 1;
            fArr5[i7] = x0;
            float[] fArr6 = this.curves;
            int i8 = this.end;
            this.end = i8 + 1;
            fArr6[i8] = y0;
        }

        public void pushQuad(float x0, float y0, float x1, float y1) {
            ensureSpace(4);
            int[] iArr = this.curveTypes;
            int i2 = this.numCurves;
            this.numCurves = i2 + 1;
            iArr[i2] = 6;
            float[] fArr = this.curves;
            int i3 = this.end;
            this.end = i3 + 1;
            fArr[i3] = x1;
            float[] fArr2 = this.curves;
            int i4 = this.end;
            this.end = i4 + 1;
            fArr2[i4] = y1;
            float[] fArr3 = this.curves;
            int i5 = this.end;
            this.end = i5 + 1;
            fArr3[i5] = x0;
            float[] fArr4 = this.curves;
            int i6 = this.end;
            this.end = i6 + 1;
            fArr4[i6] = y0;
        }

        public void pushLine(float x2, float y2) {
            ensureSpace(2);
            int[] iArr = this.curveTypes;
            int i2 = this.numCurves;
            this.numCurves = i2 + 1;
            iArr[i2] = 4;
            float[] fArr = this.curves;
            int i3 = this.end;
            this.end = i3 + 1;
            fArr[i3] = x2;
            float[] fArr2 = this.curves;
            int i4 = this.end;
            this.end = i4 + 1;
            fArr2[i4] = y2;
        }

        public int pop(float[] pts) {
            int ret = this.curveTypes[this.numCurves - 1];
            this.numCurves--;
            this.end -= ret - 2;
            System.arraycopy(this.curves, this.end, pts, 0, ret - 2);
            return ret;
        }

        public void pop(PathConsumer2D io) {
            this.numCurves--;
            int type = this.curveTypes[this.numCurves];
            this.end -= type - 2;
            switch (type) {
                case 4:
                    io.lineTo(this.curves[this.end], this.curves[this.end + 1]);
                    break;
                case 6:
                    io.quadTo(this.curves[this.end + 0], this.curves[this.end + 1], this.curves[this.end + 2], this.curves[this.end + 3]);
                    break;
                case 8:
                    io.curveTo(this.curves[this.end + 0], this.curves[this.end + 1], this.curves[this.end + 2], this.curves[this.end + 3], this.curves[this.end + 4], this.curves[this.end + 5]);
                    break;
            }
        }

        public String toString() {
            String ret = "";
            int nc = this.numCurves;
            int last = this.end;
            while (nc > 0) {
                nc--;
                int type = this.curveTypes[this.numCurves];
                last -= type - 2;
                switch (type) {
                    case 4:
                        ret = ret + "line: ";
                        break;
                    case 6:
                        ret = ret + "quad: ";
                        break;
                    case 8:
                        ret = ret + "cubic: ";
                        break;
                }
                ret = ret + Arrays.toString(Arrays.copyOfRange(this.curves, last, (last + type) - 2)) + "\n";
            }
            return ret;
        }
    }
}
