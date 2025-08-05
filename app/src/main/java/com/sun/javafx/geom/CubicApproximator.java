package com.sun.javafx.geom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/CubicApproximator.class */
public class CubicApproximator {
    private float accuracy;
    private float maxCubicSize;

    public CubicApproximator(float accuracy, float maxCubicSize) {
        this.accuracy = accuracy;
        this.maxCubicSize = maxCubicSize;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public float getAccuracy() {
        return this.accuracy;
    }

    public void setMaxCubicSize(float maxCCubicSize) {
        this.maxCubicSize = maxCCubicSize;
    }

    public float getMaxCubicSize() {
        return this.maxCubicSize;
    }

    public float approximate(List<QuadCurve2D> res, List<CubicCurve2D> tmp, CubicCurve2D curve) {
        float d2 = getApproxError(curve);
        if (d2 < this.accuracy) {
            tmp.add(curve);
            res.add(approximate(curve));
            return d2;
        }
        SplitCubic(tmp, new float[]{curve.x1, curve.y1, curve.ctrlx1, curve.ctrly1, curve.ctrlx2, curve.ctrly2, curve.x2, curve.y2});
        return approximate(tmp, res);
    }

    public float approximate(List<QuadCurve2D> res, CubicCurve2D curve) {
        List<CubicCurve2D> tmp = new ArrayList<>();
        return approximate(res, tmp, curve);
    }

    private QuadCurve2D approximate(CubicCurve2D c2) {
        return new QuadCurve2D(c2.x1, c2.y1, ((((3.0f * c2.ctrlx1) - c2.x1) + (3.0f * c2.ctrlx2)) - c2.x2) / 4.0f, ((((3.0f * c2.ctrly1) - c2.y1) + (3.0f * c2.ctrly2)) - c2.y2) / 4.0f, c2.x2, c2.y2);
    }

    private float approximate(List<CubicCurve2D> curves, List<QuadCurve2D> res) {
        QuadCurve2D approx = approximate(curves.get(0));
        float dMax = compareCPs(curves.get(0), elevate(approx));
        res.add(approx);
        for (int i2 = 1; i2 < curves.size(); i2++) {
            QuadCurve2D approx2 = approximate(curves.get(i2));
            float d2 = compareCPs(curves.get(i2), elevate(approx2));
            if (d2 > dMax) {
                dMax = d2;
            }
            res.add(approx2);
        }
        return dMax;
    }

    private static CubicCurve2D elevate(QuadCurve2D q2) {
        return new CubicCurve2D(q2.x1, q2.y1, (q2.x1 + (2.0f * q2.ctrlx)) / 3.0f, (q2.y1 + (2.0f * q2.ctrly)) / 3.0f, ((2.0f * q2.ctrlx) + q2.x2) / 3.0f, ((2.0f * q2.ctrly) + q2.y2) / 3.0f, q2.x2, q2.y2);
    }

    private static float compare(CubicCurve2D c1, CubicCurve2D c2) {
        float res = Math.abs(c1.x1 - c2.x1);
        float d2 = Math.abs(c1.y1 - c2.y1);
        if (res < d2) {
            res = d2;
        }
        float d3 = Math.abs(c1.ctrlx1 - c2.ctrlx1);
        if (res < d3) {
            res = d3;
        }
        float d4 = Math.abs(c1.ctrly1 - c2.ctrly1);
        if (res < d4) {
            res = d4;
        }
        float d5 = Math.abs(c1.ctrlx2 - c2.ctrlx2);
        if (res < d5) {
            res = d5;
        }
        float d6 = Math.abs(c1.ctrly2 - c2.ctrly2);
        if (res < d6) {
            res = d6;
        }
        float d7 = Math.abs(c1.x2 - c2.x2);
        if (res < d7) {
            res = d7;
        }
        float d8 = Math.abs(c1.y2 - c2.y2);
        if (res < d8) {
            res = d8;
        }
        return res;
    }

    private static float getApproxError(float[] coords) {
        float res = (((((-3.0f) * coords[2]) + coords[0]) + (3.0f * coords[4])) - coords[6]) / 6.0f;
        float d2 = (((((-3.0f) * coords[3]) + coords[1]) + (3.0f * coords[5])) - coords[7]) / 6.0f;
        if (res < d2) {
            res = d2;
        }
        float d3 = ((((3.0f * coords[2]) - coords[0]) - (3.0f * coords[4])) + coords[6]) / 6.0f;
        if (res < d3) {
            res = d3;
        }
        float d4 = ((((3.0f * coords[3]) - coords[1]) - (3.0f * coords[5])) + coords[7]) / 6.0f;
        if (res < d4) {
            res = d4;
        }
        return res;
    }

    public static float getApproxError(CubicCurve2D curve) {
        return getApproxError(new float[]{curve.x1, curve.y1, curve.ctrlx1, curve.ctrly1, curve.ctrlx2, curve.ctrly2, curve.x2, curve.y2});
    }

    private static float compareCPs(CubicCurve2D c1, CubicCurve2D c2) {
        float res = Math.abs(c1.ctrlx1 - c2.ctrlx1);
        float d2 = Math.abs(c1.ctrly1 - c2.ctrly1);
        if (res < d2) {
            res = d2;
        }
        float d3 = Math.abs(c1.ctrlx2 - c2.ctrlx2);
        if (res < d3) {
            res = d3;
        }
        float d4 = Math.abs(c1.ctrly2 - c2.ctrly2);
        if (res < d4) {
            res = d4;
        }
        return res;
    }

    private void ProcessMonotonicCubic(List<CubicCurve2D> resVect, float[] coords) {
        float[] coords1 = new float[8];
        float f2 = coords[0];
        float xMax = f2;
        float xMin = f2;
        float f3 = coords[1];
        float yMax = f3;
        float yMin = f3;
        for (int i2 = 2; i2 < 8; i2 += 2) {
            xMin = xMin > coords[i2] ? coords[i2] : xMin;
            xMax = xMax < coords[i2] ? coords[i2] : xMax;
            yMin = yMin > coords[i2 + 1] ? coords[i2 + 1] : yMin;
            yMax = yMax < coords[i2 + 1] ? coords[i2 + 1] : yMax;
        }
        if (xMax - xMin > this.maxCubicSize || yMax - yMin > this.maxCubicSize || getApproxError(coords) > this.accuracy) {
            coords1[6] = coords[6];
            coords1[7] = coords[7];
            coords1[4] = (coords[4] + coords[6]) / 2.0f;
            coords1[5] = (coords[5] + coords[7]) / 2.0f;
            float tx = (coords[2] + coords[4]) / 2.0f;
            float ty = (coords[3] + coords[5]) / 2.0f;
            coords1[2] = (tx + coords1[4]) / 2.0f;
            coords1[3] = (ty + coords1[5]) / 2.0f;
            coords[2] = (coords[0] + coords[2]) / 2.0f;
            coords[3] = (coords[1] + coords[3]) / 2.0f;
            coords[4] = (coords[2] + tx) / 2.0f;
            coords[5] = (coords[3] + ty) / 2.0f;
            float f4 = (coords[4] + coords1[2]) / 2.0f;
            coords1[0] = f4;
            coords[6] = f4;
            float f5 = (coords[5] + coords1[3]) / 2.0f;
            coords1[1] = f5;
            coords[7] = f5;
            ProcessMonotonicCubic(resVect, coords);
            ProcessMonotonicCubic(resVect, coords1);
            return;
        }
        resVect.add(new CubicCurve2D(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5], coords[6], coords[7]));
    }

    public void SplitCubic(List<CubicCurve2D> resVect, float[] coords) {
        float[] params = new float[4];
        float[] eqn = new float[3];
        float[] res = new float[2];
        int cnt = 0;
        if ((coords[0] > coords[2] || coords[2] > coords[4] || coords[4] > coords[6]) && (coords[0] < coords[2] || coords[2] < coords[4] || coords[4] < coords[6])) {
            eqn[2] = (((-coords[0]) + (3.0f * coords[2])) - (3.0f * coords[4])) + coords[6];
            eqn[1] = 2.0f * ((coords[0] - (2.0f * coords[2])) + coords[4]);
            eqn[0] = (-coords[0]) + coords[2];
            int nr = QuadCurve2D.solveQuadratic(eqn, res);
            for (int i2 = 0; i2 < nr; i2++) {
                if (res[i2] > 0.0f && res[i2] < 1.0f) {
                    int i3 = cnt;
                    cnt++;
                    params[i3] = res[i2];
                }
            }
        }
        if ((coords[1] > coords[3] || coords[3] > coords[5] || coords[5] > coords[7]) && (coords[1] < coords[3] || coords[3] < coords[5] || coords[5] < coords[7])) {
            eqn[2] = (((-coords[1]) + (3.0f * coords[3])) - (3.0f * coords[5])) + coords[7];
            eqn[1] = 2.0f * ((coords[1] - (2.0f * coords[3])) + coords[5]);
            eqn[0] = (-coords[1]) + coords[3];
            int nr2 = QuadCurve2D.solveQuadratic(eqn, res);
            for (int i4 = 0; i4 < nr2; i4++) {
                if (res[i4] > 0.0f && res[i4] < 1.0f) {
                    int i5 = cnt;
                    cnt++;
                    params[i5] = res[i4];
                }
            }
        }
        if (cnt > 0) {
            Arrays.sort(params, 0, cnt);
            ProcessFirstMonotonicPartOfCubic(resVect, coords, params[0]);
            for (int i6 = 1; i6 < cnt; i6++) {
                float param = params[i6] - params[i6 - 1];
                if (param > 0.0f) {
                    ProcessFirstMonotonicPartOfCubic(resVect, coords, param / (1.0f - params[i6 - 1]));
                }
            }
        }
        ProcessMonotonicCubic(resVect, coords);
    }

    private void ProcessFirstMonotonicPartOfCubic(List<CubicCurve2D> resVector, float[] coords, float t2) {
        float[] coords1 = new float[8];
        coords1[0] = coords[0];
        coords1[1] = coords[1];
        float tx = coords[2] + (t2 * (coords[4] - coords[2]));
        float ty = coords[3] + (t2 * (coords[5] - coords[3]));
        coords1[2] = coords[0] + (t2 * (coords[2] - coords[0]));
        coords1[3] = coords[1] + (t2 * (coords[3] - coords[1]));
        coords1[4] = coords1[2] + (t2 * (tx - coords1[2]));
        coords1[5] = coords1[3] + (t2 * (ty - coords1[3]));
        coords[4] = coords[4] + (t2 * (coords[6] - coords[4]));
        coords[5] = coords[5] + (t2 * (coords[7] - coords[5]));
        coords[2] = tx + (t2 * (coords[4] - tx));
        coords[3] = ty + (t2 * (coords[5] - ty));
        float f2 = coords1[4] + (t2 * (coords[2] - coords1[4]));
        coords1[6] = f2;
        coords[0] = f2;
        float f3 = coords1[5] + (t2 * (coords[3] - coords1[5]));
        coords1[7] = f3;
        coords[1] = f3;
        ProcessMonotonicCubic(resVector, coords1);
    }
}
