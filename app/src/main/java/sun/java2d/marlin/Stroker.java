package sun.java2d.marlin;

import java.util.Arrays;
import sun.awt.geom.PathConsumer2D;
import sun.java2d.marlin.Curve;

/* loaded from: rt.jar:sun/java2d/marlin/Stroker.class */
final class Stroker implements PathConsumer2D, MarlinConst {
    private static final int MOVE_TO = 0;
    private static final int DRAWING_OP_TO = 1;
    private static final int CLOSE = 2;
    public static final int JOIN_MITER = 0;
    public static final int JOIN_ROUND = 1;
    public static final int JOIN_BEVEL = 2;
    public static final int CAP_BUTT = 0;
    public static final int CAP_ROUND = 1;
    public static final int CAP_SQUARE = 2;
    private static final float ROUND_JOIN_THRESHOLD = 0.015258789f;

    /* renamed from: C, reason: collision with root package name */
    private static final float f13568C = 0.5522848f;
    private static final int MAX_N_CURVES = 11;
    private PathConsumer2D out;
    private int capStyle;
    private int joinStyle;
    private float lineWidth2;
    private float invHalfLineWidth2Sq;
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
    final RendererContext rdrCtx;
    final Curve curve;
    static final /* synthetic */ boolean $assertionsDisabled;
    private final float[] offset0 = new float[2];
    private final float[] offset1 = new float[2];
    private final float[] offset2 = new float[2];
    private final float[] miter = new float[2];
    private final float[] middle = new float[16];
    private final float[] lp = new float[8];
    private final float[] rp = new float[8];
    private final float[] subdivTs = new float[10];

    static {
        $assertionsDisabled = !Stroker.class.desiredAssertionStatus();
    }

    Stroker(RendererContext rendererContext) {
        this.rdrCtx = rendererContext;
        this.reverse = new PolyStack(rendererContext);
        this.curve = rendererContext.curve;
    }

    Stroker init(PathConsumer2D pathConsumer2D, float f2, int i2, int i3, float f3) {
        this.out = pathConsumer2D;
        this.lineWidth2 = f2 / 2.0f;
        this.invHalfLineWidth2Sq = 1.0f / ((2.0f * this.lineWidth2) * this.lineWidth2);
        this.capStyle = i2;
        this.joinStyle = i3;
        float f4 = f3 * this.lineWidth2;
        this.miterLimitSq = f4 * f4;
        this.prev = 2;
        this.rdrCtx.stroking = 1;
        return this;
    }

    void dispose() {
        this.reverse.dispose();
    }

    private static void computeOffset(float f2, float f3, float f4, float[] fArr) {
        float f5 = (f2 * f2) + (f3 * f3);
        if (f5 == 0.0f) {
            fArr[0] = 0.0f;
            fArr[1] = 0.0f;
        } else {
            float fSqrt = (float) Math.sqrt(f5);
            fArr[0] = (f3 * f4) / fSqrt;
            fArr[1] = (-(f2 * f4)) / fSqrt;
        }
    }

    private static boolean isCW(float f2, float f3, float f4, float f5) {
        return f2 * f5 <= f3 * f4;
    }

    private void drawRoundJoin(float f2, float f3, float f4, float f5, float f6, float f7, boolean z2, float f8) {
        if (f4 == 0.0f && f5 == 0.0f) {
            return;
        }
        if (f6 == 0.0f && f7 == 0.0f) {
            return;
        }
        float f9 = f4 - f6;
        float f10 = f5 - f7;
        if ((f9 * f9) + (f10 * f10) < f8) {
            return;
        }
        if (z2) {
            f4 = -f4;
            f5 = -f5;
            f6 = -f6;
            f7 = -f7;
        }
        drawRoundJoin(f2, f3, f4, f5, f6, f7, z2);
    }

    private void drawRoundJoin(float f2, float f3, float f4, float f5, float f6, float f7, boolean z2) {
        switch ((f4 * f6) + (f5 * f7) >= 0.0f ? (char) 1 : (char) 2) {
            case 1:
                drawBezApproxForArc(f2, f3, f4, f5, f6, f7, z2);
                break;
            case 2:
                float fSqrt = this.lineWidth2 / ((float) Math.sqrt((r0 * r0) + (r0 * r0)));
                float f8 = (f7 - f5) * fSqrt;
                float f9 = (f4 - f6) * fSqrt;
                if (z2) {
                    f8 = -f8;
                    f9 = -f9;
                }
                drawBezApproxForArc(f2, f3, f4, f5, f8, f9, z2);
                drawBezApproxForArc(f2, f3, f8, f9, f6, f7, z2);
                break;
        }
    }

    private void drawBezApproxForArc(float f2, float f3, float f4, float f5, float f6, float f7, boolean z2) {
        float f8 = ((f4 * f6) + (f5 * f7)) * this.invHalfLineWidth2Sq;
        if (f8 >= 0.5f) {
            return;
        }
        float fSqrt = (float) ((1.3333333333333333d * Math.sqrt(0.5d - f8)) / (1.0d + Math.sqrt(f8 + 0.5d)));
        if (z2) {
            fSqrt = -fSqrt;
        }
        float f9 = f2 + f4;
        float f10 = f3 + f5;
        float f11 = f9 - (fSqrt * f5);
        float f12 = f10 + (fSqrt * f4);
        float f13 = f2 + f6;
        float f14 = f3 + f7;
        emitCurveTo(f9, f10, f11, f12, f13 + (fSqrt * f7), f14 - (fSqrt * f6), f13, f14, z2);
    }

    private void drawRoundCap(float f2, float f3, float f4, float f5) {
        emitCurveTo((f2 + f4) - (f13568C * f5), f3 + f5 + (f13568C * f4), (f2 - f5) + (f13568C * f4), f3 + f4 + (f13568C * f5), f2 - f5, f3 + f4);
        emitCurveTo((f2 - f5) - (f13568C * f4), (f3 + f4) - (f13568C * f5), (f2 - f4) - (f13568C * f5), (f3 - f5) + (f13568C * f4), f2 - f4, f3 - f5);
    }

    private static void computeIntersection(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float[] fArr, int i2) {
        float f10 = f4 - f2;
        float f11 = f5 - f3;
        float f12 = f8 - f6;
        float f13 = f9 - f7;
        float f14 = ((f12 * (f3 - f7)) - (f13 * (f2 - f6))) / ((f10 * f13) - (f12 * f11));
        fArr[i2] = f2 + (f14 * f10);
        fArr[i2 + 1] = f3 + (f14 * f11);
    }

    private void drawMiter(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, boolean z2) {
        if (f10 == f8 && f11 == f9) {
            return;
        }
        if (f2 == 0.0f && f3 == 0.0f) {
            return;
        }
        if (f6 == 0.0f && f7 == 0.0f) {
            return;
        }
        if (z2) {
            f8 = -f8;
            f9 = -f9;
            f10 = -f10;
            f11 = -f11;
        }
        computeIntersection((f4 - f2) + f8, (f5 - f3) + f9, f4 + f8, f5 + f9, f6 + f4 + f10, f7 + f5 + f11, f4 + f10, f5 + f11, this.miter, 0);
        float f12 = this.miter[0];
        float f13 = this.miter[1];
        if (((f12 - f4) * (f12 - f4)) + ((f13 - f5) * (f13 - f5)) < this.miterLimitSq) {
            emitLineTo(f12, f13, z2);
        }
    }

    @Override // sun.awt.geom.PathConsumer2D
    public void moveTo(float f2, float f3) {
        if (this.prev == 1) {
            finish();
        }
        this.cx0 = f2;
        this.sx0 = f2;
        this.cy0 = f3;
        this.sy0 = f3;
        this.sdx = 1.0f;
        this.cdx = 1.0f;
        this.sdy = 0.0f;
        this.cdy = 0.0f;
        this.prev = 0;
    }

    @Override // sun.awt.geom.PathConsumer2D
    public void lineTo(float f2, float f3) {
        float f4 = f2 - this.cx0;
        float f5 = f3 - this.cy0;
        if (f4 == 0.0f && f5 == 0.0f) {
            f4 = 1.0f;
        }
        computeOffset(f4, f5, this.lineWidth2, this.offset0);
        float f6 = this.offset0[0];
        float f7 = this.offset0[1];
        drawJoin(this.cdx, this.cdy, this.cx0, this.cy0, f4, f5, this.cmx, this.cmy, f6, f7);
        emitLineTo(this.cx0 + f6, this.cy0 + f7);
        emitLineTo(f2 + f6, f3 + f7);
        emitLineToRev(this.cx0 - f6, this.cy0 - f7);
        emitLineToRev(f2 - f6, f3 - f7);
        this.cmx = f6;
        this.cmy = f7;
        this.cdx = f4;
        this.cdy = f5;
        this.cx0 = f2;
        this.cy0 = f3;
        this.prev = 1;
    }

    @Override // sun.awt.geom.PathConsumer2D
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
        this.reverse.popAll(this.out);
    }

    @Override // sun.awt.geom.PathConsumer2D
    public void pathDone() {
        if (this.prev == 1) {
            finish();
        }
        this.out.pathDone();
        this.prev = 2;
        dispose();
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

    private void emitMoveTo(float f2, float f3) {
        this.out.moveTo(f2, f3);
    }

    private void emitLineTo(float f2, float f3) {
        this.out.lineTo(f2, f3);
    }

    private void emitLineToRev(float f2, float f3) {
        this.reverse.pushLine(f2, f3);
    }

    private void emitLineTo(float f2, float f3, boolean z2) {
        if (z2) {
            emitLineToRev(f2, f3);
        } else {
            emitLineTo(f2, f3);
        }
    }

    private void emitQuadTo(float f2, float f3, float f4, float f5) {
        this.out.quadTo(f2, f3, f4, f5);
    }

    private void emitQuadToRev(float f2, float f3, float f4, float f5) {
        this.reverse.pushQuad(f2, f3, f4, f5);
    }

    private void emitCurveTo(float f2, float f3, float f4, float f5, float f6, float f7) {
        this.out.curveTo(f2, f3, f4, f5, f6, f7);
    }

    private void emitCurveToRev(float f2, float f3, float f4, float f5, float f6, float f7) {
        this.reverse.pushCubic(f2, f3, f4, f5, f6, f7);
    }

    private void emitCurveTo(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, boolean z2) {
        if (z2) {
            this.reverse.pushCubic(f2, f3, f4, f5, f6, f7);
        } else {
            this.out.curveTo(f4, f5, f6, f7, f8, f9);
        }
    }

    private void emitClose() {
        this.out.closePath();
    }

    private void drawJoin(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11) {
        if (this.prev != 1) {
            emitMoveTo(f4 + f10, f5 + f11);
            this.sdx = f6;
            this.sdy = f7;
            this.smx = f10;
            this.smy = f11;
        } else {
            boolean zIsCW = isCW(f2, f3, f6, f7);
            if (this.joinStyle == 0) {
                drawMiter(f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, zIsCW);
            } else if (this.joinStyle == 1) {
                drawRoundJoin(f4, f5, f8, f9, f10, f11, zIsCW, ROUND_JOIN_THRESHOLD);
            }
            emitLineTo(f4, f5, !zIsCW);
        }
        this.prev = 1;
    }

    private static boolean within(float f2, float f3, float f4, float f5, float f6) {
        if ($assertionsDisabled || f6 > 0.0f) {
            return Helpers.within(f2, f4, f6) && Helpers.within(f3, f5, f6);
        }
        throw new AssertionError((Object) "");
    }

    private void getLineOffsets(float f2, float f3, float f4, float f5, float[] fArr, float[] fArr2) {
        computeOffset(f4 - f2, f5 - f3, this.lineWidth2, this.offset0);
        float f6 = this.offset0[0];
        float f7 = this.offset0[1];
        fArr[0] = f2 + f6;
        fArr[1] = f3 + f7;
        fArr[2] = f4 + f6;
        fArr[3] = f5 + f7;
        fArr2[0] = f2 - f6;
        fArr2[1] = f3 - f7;
        fArr2[2] = f4 - f6;
        fArr2[3] = f5 - f7;
    }

    private int computeOffsetCubic(float[] fArr, int i2, float[] fArr2, float[] fArr3) {
        float f2 = fArr[i2 + 0];
        float f3 = fArr[i2 + 1];
        float f4 = fArr[i2 + 2];
        float f5 = fArr[i2 + 3];
        float f6 = fArr[i2 + 4];
        float f7 = fArr[i2 + 5];
        float f8 = fArr[i2 + 6];
        float f9 = fArr[i2 + 7];
        float f10 = f8 - f6;
        float f11 = f9 - f7;
        float f12 = f4 - f2;
        float f13 = f5 - f3;
        boolean zWithin = within(f2, f3, f4, f5, 6.0f * Math.ulp(f5));
        boolean zWithin2 = within(f6, f7, f8, f9, 6.0f * Math.ulp(f9));
        if (zWithin && zWithin2) {
            getLineOffsets(f2, f3, f8, f9, fArr2, fArr3);
            return 4;
        }
        if (zWithin) {
            f12 = f6 - f2;
            f13 = f7 - f3;
        } else if (zWithin2) {
            f10 = f8 - f4;
            f11 = f9 - f5;
        }
        float f14 = (f12 * f10) + (f13 * f11);
        float f15 = f14 * f14;
        if (Helpers.within(f15, ((f12 * f12) + (f13 * f13)) * ((f10 * f10) + (f11 * f11)), 4.0f * Math.ulp(f15))) {
            getLineOffsets(f2, f3, f8, f9, fArr2, fArr3);
            return 4;
        }
        float f16 = ((f2 + (3.0f * (f4 + f6))) + f8) / 8.0f;
        float f17 = ((f3 + (3.0f * (f5 + f7))) + f9) / 8.0f;
        computeOffset(f12, f13, this.lineWidth2, this.offset0);
        computeOffset(((f6 + f8) - f2) - f4, ((f7 + f9) - f3) - f5, this.lineWidth2, this.offset1);
        computeOffset(f10, f11, this.lineWidth2, this.offset2);
        float f18 = f2 + this.offset0[0];
        float f19 = f3 + this.offset0[1];
        float f20 = f16 + this.offset1[0];
        float f21 = f17 + this.offset1[1];
        float f22 = f8 + this.offset2[0];
        float f23 = f9 + this.offset2[1];
        float f24 = 4.0f / (3.0f * ((f12 * f11) - (f13 * f10)));
        float f25 = ((2.0f * f20) - f18) - f22;
        float f26 = ((2.0f * f21) - f19) - f23;
        float f27 = f24 * ((f11 * f25) - (f10 * f26));
        float f28 = f24 * ((f12 * f26) - (f13 * f25));
        float f29 = f18 + (f27 * f12);
        float f30 = f19 + (f27 * f13);
        fArr2[0] = f18;
        fArr2[1] = f19;
        fArr2[2] = f29;
        fArr2[3] = f30;
        fArr2[4] = f22 + (f28 * f10);
        fArr2[5] = f23 + (f28 * f11);
        fArr2[6] = f22;
        fArr2[7] = f23;
        float f31 = f2 - this.offset0[0];
        float f32 = f3 - this.offset0[1];
        float f33 = f20 - (2.0f * this.offset1[0]);
        float f34 = f21 - (2.0f * this.offset1[1]);
        float f35 = f8 - this.offset2[0];
        float f36 = f9 - this.offset2[1];
        float f37 = ((2.0f * f33) - f31) - f35;
        float f38 = ((2.0f * f34) - f32) - f36;
        float f39 = f24 * ((f11 * f37) - (f10 * f38));
        float f40 = f24 * ((f12 * f38) - (f13 * f37));
        float f41 = f31 + (f39 * f12);
        float f42 = f32 + (f39 * f13);
        fArr3[0] = f31;
        fArr3[1] = f32;
        fArr3[2] = f41;
        fArr3[3] = f42;
        fArr3[4] = f35 + (f40 * f10);
        fArr3[5] = f36 + (f40 * f11);
        fArr3[6] = f35;
        fArr3[7] = f36;
        return 8;
    }

    private int computeOffsetQuad(float[] fArr, int i2, float[] fArr2, float[] fArr3) {
        float f2 = fArr[i2 + 0];
        float f3 = fArr[i2 + 1];
        float f4 = fArr[i2 + 2];
        float f5 = fArr[i2 + 3];
        float f6 = fArr[i2 + 4];
        float f7 = fArr[i2 + 5];
        float f8 = f6 - f4;
        float f9 = f7 - f5;
        float f10 = f4 - f2;
        float f11 = f5 - f3;
        computeOffset(f10, f11, this.lineWidth2, this.offset0);
        computeOffset(f8, f9, this.lineWidth2, this.offset1);
        fArr2[0] = f2 + this.offset0[0];
        fArr2[1] = f3 + this.offset0[1];
        fArr2[4] = f6 + this.offset1[0];
        fArr2[5] = f7 + this.offset1[1];
        fArr3[0] = f2 - this.offset0[0];
        fArr3[1] = f3 - this.offset0[1];
        fArr3[4] = f6 - this.offset1[0];
        fArr3[5] = f7 - this.offset1[1];
        float f12 = fArr2[0];
        float f13 = fArr2[1];
        float f14 = fArr2[4];
        float f15 = fArr2[5];
        computeIntersection(f12, f13, f12 + f10, f13 + f11, f14, f15, f14 - f8, f15 - f9, fArr2, 2);
        float f16 = fArr2[2];
        float f17 = fArr2[3];
        if (!isFinite(f16) || !isFinite(f17)) {
            float f18 = fArr3[0];
            float f19 = fArr3[1];
            float f20 = fArr3[4];
            float f21 = fArr3[5];
            computeIntersection(f18, f19, f18 + f10, f19 + f11, f20, f21, f20 - f8, f21 - f9, fArr3, 2);
            float f22 = fArr3[2];
            float f23 = fArr3[3];
            if (!isFinite(f22) || !isFinite(f23)) {
                getLineOffsets(f2, f3, f6, f7, fArr2, fArr3);
                return 4;
            }
            fArr2[2] = (2.0f * f4) - f22;
            fArr2[3] = (2.0f * f5) - f23;
            return 6;
        }
        fArr3[2] = (2.0f * f4) - f16;
        fArr3[3] = (2.0f * f5) - f17;
        return 6;
    }

    private static boolean isFinite(float f2) {
        return Float.NEGATIVE_INFINITY < f2 && f2 < Float.POSITIVE_INFINITY;
    }

    private static int findSubdivPoints(Curve curve, float[] fArr, float[] fArr2, int i2, float f2) {
        float f3 = fArr[2] - fArr[0];
        float f4 = fArr[3] - fArr[1];
        if (f4 != 0.0f && f3 != 0.0f) {
            float fSqrt = (float) Math.sqrt((f3 * f3) + (f4 * f4));
            float f5 = f3 / fSqrt;
            float f6 = f4 / fSqrt;
            float f7 = (f5 * fArr[0]) + (f6 * fArr[1]);
            float f8 = (f5 * fArr[1]) - (f6 * fArr[0]);
            float f9 = (f5 * fArr[2]) + (f6 * fArr[3]);
            float f10 = (f5 * fArr[3]) - (f6 * fArr[2]);
            float f11 = (f5 * fArr[4]) + (f6 * fArr[5]);
            float f12 = (f5 * fArr[5]) - (f6 * fArr[4]);
            switch (i2) {
                case 6:
                    curve.set(f7, f8, f9, f10, f11, f12);
                    break;
                case 8:
                    curve.set(f7, f8, f9, f10, f11, f12, (f5 * fArr[6]) + (f6 * fArr[7]), (f5 * fArr[7]) - (f6 * fArr[6]));
                    break;
            }
        } else {
            curve.set(fArr, i2);
        }
        int iDxRoots = 0 + curve.dxRoots(fArr2, 0);
        int iDyRoots = iDxRoots + curve.dyRoots(fArr2, iDxRoots);
        if (i2 == 8) {
            iDyRoots += curve.infPoints(fArr2, iDyRoots);
        }
        int iFilterOutNotInAB = Helpers.filterOutNotInAB(fArr2, 0, iDyRoots + curve.rootsOfROCMinusW(fArr2, iDyRoots, f2, 1.0E-4f), 1.0E-4f, 0.9999f);
        Helpers.isort(fArr2, 0, iFilterOutNotInAB);
        return iFilterOutNotInAB;
    }

    @Override // sun.awt.geom.PathConsumer2D
    public void curveTo(float f2, float f3, float f4, float f5, float f6, float f7) {
        float[] fArr = this.middle;
        fArr[0] = this.cx0;
        fArr[1] = this.cy0;
        fArr[2] = f2;
        fArr[3] = f3;
        fArr[4] = f4;
        fArr[5] = f5;
        fArr[6] = f6;
        fArr[7] = f7;
        float f8 = fArr[6];
        float f9 = fArr[7];
        float f10 = fArr[2] - fArr[0];
        float f11 = fArr[3] - fArr[1];
        float f12 = fArr[6] - fArr[4];
        float f13 = fArr[7] - fArr[5];
        boolean z2 = f10 == 0.0f && f11 == 0.0f;
        boolean z3 = f12 == 0.0f && f13 == 0.0f;
        if (z2) {
            f10 = fArr[4] - fArr[0];
            f11 = fArr[5] - fArr[1];
            if (f10 == 0.0f && f11 == 0.0f) {
                f10 = fArr[6] - fArr[0];
                f11 = fArr[7] - fArr[1];
            }
        }
        if (z3) {
            f12 = fArr[6] - fArr[2];
            f13 = fArr[7] - fArr[3];
            if (f12 == 0.0f && f13 == 0.0f) {
                f12 = fArr[6] - fArr[0];
                f13 = fArr[7] - fArr[1];
            }
        }
        if (f10 == 0.0f && f11 == 0.0f) {
            lineTo(fArr[0], fArr[1]);
            return;
        }
        if (Math.abs(f10) < 0.1f && Math.abs(f11) < 0.1f) {
            float fSqrt = (float) Math.sqrt((f10 * f10) + (f11 * f11));
            f10 /= fSqrt;
            f11 /= fSqrt;
        }
        if (Math.abs(f12) < 0.1f && Math.abs(f13) < 0.1f) {
            float fSqrt2 = (float) Math.sqrt((f12 * f12) + (f13 * f13));
            f12 /= fSqrt2;
            f13 /= fSqrt2;
        }
        computeOffset(f10, f11, this.lineWidth2, this.offset0);
        drawJoin(this.cdx, this.cdy, this.cx0, this.cy0, f10, f11, this.cmx, this.cmy, this.offset0[0], this.offset0[1]);
        int iFindSubdivPoints = findSubdivPoints(this.curve, fArr, this.subdivTs, 8, this.lineWidth2);
        float[] fArr2 = this.lp;
        float[] fArr3 = this.rp;
        int iComputeOffsetCubic = 0;
        Curve.BreakPtrIterator breakPtrIteratorBreakPtsAtTs = this.curve.breakPtsAtTs(fArr, 8, this.subdivTs, iFindSubdivPoints);
        while (breakPtrIteratorBreakPtsAtTs.hasNext()) {
            iComputeOffsetCubic = computeOffsetCubic(fArr, breakPtrIteratorBreakPtsAtTs.next(), fArr2, fArr3);
            emitLineTo(fArr2[0], fArr2[1]);
            switch (iComputeOffsetCubic) {
                case 4:
                    emitLineTo(fArr2[2], fArr2[3]);
                    emitLineToRev(fArr3[0], fArr3[1]);
                    break;
                case 8:
                    emitCurveTo(fArr2[2], fArr2[3], fArr2[4], fArr2[5], fArr2[6], fArr2[7]);
                    emitCurveToRev(fArr3[0], fArr3[1], fArr3[2], fArr3[3], fArr3[4], fArr3[5]);
                    break;
            }
            emitLineToRev(fArr3[iComputeOffsetCubic - 2], fArr3[iComputeOffsetCubic - 1]);
        }
        this.cmx = (fArr2[iComputeOffsetCubic - 2] - fArr3[iComputeOffsetCubic - 2]) / 2.0f;
        this.cmy = (fArr2[iComputeOffsetCubic - 1] - fArr3[iComputeOffsetCubic - 1]) / 2.0f;
        this.cdx = f12;
        this.cdy = f13;
        this.cx0 = f8;
        this.cy0 = f9;
        this.prev = 1;
    }

    @Override // sun.awt.geom.PathConsumer2D
    public void quadTo(float f2, float f3, float f4, float f5) {
        float[] fArr = this.middle;
        fArr[0] = this.cx0;
        fArr[1] = this.cy0;
        fArr[2] = f2;
        fArr[3] = f3;
        fArr[4] = f4;
        fArr[5] = f5;
        float f6 = fArr[4];
        float f7 = fArr[5];
        float f8 = fArr[2] - fArr[0];
        float f9 = fArr[3] - fArr[1];
        float f10 = fArr[4] - fArr[2];
        float f11 = fArr[5] - fArr[3];
        if ((f8 == 0.0f && f9 == 0.0f) || (f10 == 0.0f && f11 == 0.0f)) {
            float f12 = fArr[4] - fArr[0];
            f10 = f12;
            f8 = f12;
            float f13 = fArr[5] - fArr[1];
            f11 = f13;
            f9 = f13;
        }
        if (f8 == 0.0f && f9 == 0.0f) {
            lineTo(fArr[0], fArr[1]);
            return;
        }
        if (Math.abs(f8) < 0.1f && Math.abs(f9) < 0.1f) {
            float fSqrt = (float) Math.sqrt((f8 * f8) + (f9 * f9));
            f8 /= fSqrt;
            f9 /= fSqrt;
        }
        if (Math.abs(f10) < 0.1f && Math.abs(f11) < 0.1f) {
            float fSqrt2 = (float) Math.sqrt((f10 * f10) + (f11 * f11));
            f10 /= fSqrt2;
            f11 /= fSqrt2;
        }
        computeOffset(f8, f9, this.lineWidth2, this.offset0);
        drawJoin(this.cdx, this.cdy, this.cx0, this.cy0, f8, f9, this.cmx, this.cmy, this.offset0[0], this.offset0[1]);
        int iFindSubdivPoints = findSubdivPoints(this.curve, fArr, this.subdivTs, 6, this.lineWidth2);
        float[] fArr2 = this.lp;
        float[] fArr3 = this.rp;
        int iComputeOffsetQuad = 0;
        Curve.BreakPtrIterator breakPtrIteratorBreakPtsAtTs = this.curve.breakPtsAtTs(fArr, 6, this.subdivTs, iFindSubdivPoints);
        while (breakPtrIteratorBreakPtsAtTs.hasNext()) {
            iComputeOffsetQuad = computeOffsetQuad(fArr, breakPtrIteratorBreakPtsAtTs.next(), fArr2, fArr3);
            emitLineTo(fArr2[0], fArr2[1]);
            switch (iComputeOffsetQuad) {
                case 4:
                    emitLineTo(fArr2[2], fArr2[3]);
                    emitLineToRev(fArr3[0], fArr3[1]);
                    break;
                case 6:
                    emitQuadTo(fArr2[2], fArr2[3], fArr2[4], fArr2[5]);
                    emitQuadToRev(fArr3[0], fArr3[1], fArr3[2], fArr3[3]);
                    break;
            }
            emitLineToRev(fArr3[iComputeOffsetQuad - 2], fArr3[iComputeOffsetQuad - 1]);
        }
        this.cmx = (fArr2[iComputeOffsetQuad - 2] - fArr3[iComputeOffsetQuad - 2]) / 2.0f;
        this.cmy = (fArr2[iComputeOffsetQuad - 1] - fArr3[iComputeOffsetQuad - 1]) / 2.0f;
        this.cdx = f10;
        this.cdy = f11;
        this.cx0 = f6;
        this.cy0 = f7;
        this.prev = 1;
    }

    @Override // sun.awt.geom.PathConsumer2D
    public long getNativeConsumer() {
        throw new InternalError("Stroker doesn't use a native consumer");
    }

    /* loaded from: rt.jar:sun/java2d/marlin/Stroker$PolyStack.class */
    static final class PolyStack {
        private static final byte TYPE_LINETO = 0;
        private static final byte TYPE_QUADTO = 1;
        private static final byte TYPE_CUBICTO = 2;
        final RendererContext rdrCtx;
        int curveTypesUseMark;
        int curvesUseMark;
        private final float[] curves_initial = new float[8193];
        private final byte[] curveTypes_initial = new byte[8193];
        float[] curves = this.curves_initial;
        byte[] curveTypes = this.curveTypes_initial;
        int end = 0;
        int numCurves = 0;

        PolyStack(RendererContext rendererContext) {
            this.rdrCtx = rendererContext;
            if (MarlinConst.doStats) {
                this.curveTypesUseMark = 0;
                this.curvesUseMark = 0;
            }
        }

        void dispose() {
            this.end = 0;
            this.numCurves = 0;
            if (MarlinConst.doStats) {
                RendererContext.stats.stat_rdr_poly_stack_types.add(this.curveTypesUseMark);
                RendererContext.stats.stat_rdr_poly_stack_curves.add(this.curvesUseMark);
                this.curveTypesUseMark = 0;
                this.curvesUseMark = 0;
            }
            if (this.curves != this.curves_initial) {
                this.rdrCtx.putDirtyFloatArray(this.curves);
                this.curves = this.curves_initial;
            }
            if (this.curveTypes != this.curveTypes_initial) {
                this.rdrCtx.putDirtyByteArray(this.curveTypes);
                this.curveTypes = this.curveTypes_initial;
            }
        }

        private void ensureSpace(int i2) {
            if (this.curves.length - this.end < i2) {
                if (MarlinConst.doStats) {
                    RendererContext.stats.stat_array_stroker_polystack_curves.add(this.end + i2);
                }
                this.curves = this.rdrCtx.widenDirtyFloatArray(this.curves, this.end, this.end + i2);
            }
            if (this.curveTypes.length <= this.numCurves) {
                if (MarlinConst.doStats) {
                    RendererContext.stats.stat_array_stroker_polystack_curveTypes.add(this.numCurves + 1);
                }
                this.curveTypes = this.rdrCtx.widenDirtyByteArray(this.curveTypes, this.numCurves, this.numCurves + 1);
            }
        }

        void pushCubic(float f2, float f3, float f4, float f5, float f6, float f7) {
            ensureSpace(6);
            byte[] bArr = this.curveTypes;
            int i2 = this.numCurves;
            this.numCurves = i2 + 1;
            bArr[i2] = 2;
            float[] fArr = this.curves;
            int i3 = this.end;
            int i4 = i3 + 1;
            fArr[i3] = f6;
            int i5 = i4 + 1;
            fArr[i4] = f7;
            int i6 = i5 + 1;
            fArr[i5] = f4;
            int i7 = i6 + 1;
            fArr[i6] = f5;
            int i8 = i7 + 1;
            fArr[i7] = f2;
            fArr[i8] = f3;
            this.end = i8 + 1;
        }

        void pushQuad(float f2, float f3, float f4, float f5) {
            ensureSpace(4);
            byte[] bArr = this.curveTypes;
            int i2 = this.numCurves;
            this.numCurves = i2 + 1;
            bArr[i2] = 1;
            float[] fArr = this.curves;
            int i3 = this.end;
            int i4 = i3 + 1;
            fArr[i3] = f4;
            int i5 = i4 + 1;
            fArr[i4] = f5;
            int i6 = i5 + 1;
            fArr[i5] = f2;
            fArr[i6] = f3;
            this.end = i6 + 1;
        }

        void pushLine(float f2, float f3) {
            ensureSpace(2);
            byte[] bArr = this.curveTypes;
            int i2 = this.numCurves;
            this.numCurves = i2 + 1;
            bArr[i2] = 0;
            float[] fArr = this.curves;
            int i3 = this.end;
            this.end = i3 + 1;
            fArr[i3] = f2;
            float[] fArr2 = this.curves;
            int i4 = this.end;
            this.end = i4 + 1;
            fArr2[i4] = f3;
        }

        void popAll(PathConsumer2D pathConsumer2D) {
            if (MarlinConst.doStats) {
                if (this.numCurves > this.curveTypesUseMark) {
                    this.curveTypesUseMark = this.numCurves;
                }
                if (this.end > this.curvesUseMark) {
                    this.curvesUseMark = this.end;
                }
            }
            byte[] bArr = this.curveTypes;
            float[] fArr = this.curves;
            int i2 = this.numCurves;
            int i3 = this.end;
            while (i2 != 0) {
                i2--;
                switch (bArr[i2]) {
                    case 0:
                        i3 -= 2;
                        pathConsumer2D.lineTo(fArr[i3], fArr[i3 + 1]);
                        break;
                    case 1:
                        i3 -= 4;
                        pathConsumer2D.quadTo(fArr[i3 + 0], fArr[i3 + 1], fArr[i3 + 2], fArr[i3 + 3]);
                        break;
                    case 2:
                        i3 -= 6;
                        pathConsumer2D.curveTo(fArr[i3 + 0], fArr[i3 + 1], fArr[i3 + 2], fArr[i3 + 3], fArr[i3 + 4], fArr[i3 + 5]);
                        break;
                }
            }
            this.numCurves = 0;
            this.end = 0;
        }

        public String toString() {
            int i2;
            String str = "";
            int i3 = this.numCurves;
            int i4 = this.end;
            while (i3 != 0) {
                i3--;
                switch (this.curveTypes[i3]) {
                    case 0:
                        i2 = 2;
                        str = str + "line: ";
                        break;
                    case 1:
                        i2 = 4;
                        str = str + "quad: ";
                        break;
                    case 2:
                        i2 = 6;
                        str = str + "cubic: ";
                        break;
                    default:
                        i2 = 0;
                        break;
                }
                i4 -= i2;
                str = str + Arrays.toString(Arrays.copyOfRange(this.curves, i4, i4 + i2)) + "\n";
            }
            return str;
        }
    }
}
