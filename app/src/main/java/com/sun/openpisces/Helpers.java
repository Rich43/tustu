package com.sun.openpisces;

import java.util.Arrays;

/* loaded from: jfxrt.jar:com/sun/openpisces/Helpers.class */
final class Helpers {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Helpers.class.desiredAssertionStatus();
    }

    private Helpers() {
        throw new Error("This is a non instantiable class");
    }

    static boolean within(float x2, float y2, float err) {
        float d2 = y2 - x2;
        return d2 <= err && d2 >= (-err);
    }

    static boolean within(double x2, double y2, double err) {
        double d2 = y2 - x2;
        return d2 <= err && d2 >= (-err);
    }

    static int quadraticRoots(float a2, float b2, float c2, float[] zeroes, int off) {
        int ret = off;
        if (a2 != 0.0f) {
            float dis = (b2 * b2) - ((4.0f * a2) * c2);
            if (dis > 0.0f) {
                float sqrtDis = (float) Math.sqrt(dis);
                if (b2 >= 0.0f) {
                    int ret2 = ret + 1;
                    zeroes[ret] = (2.0f * c2) / ((-b2) - sqrtDis);
                    ret = ret2 + 1;
                    zeroes[ret2] = ((-b2) - sqrtDis) / (2.0f * a2);
                } else {
                    int ret3 = ret + 1;
                    zeroes[ret] = ((-b2) + sqrtDis) / (2.0f * a2);
                    ret = ret3 + 1;
                    zeroes[ret3] = (2.0f * c2) / ((-b2) + sqrtDis);
                }
            } else if (dis == 0.0f) {
                float t2 = (-b2) / (2.0f * a2);
                ret++;
                zeroes[ret] = t2;
            }
        } else if (b2 != 0.0f) {
            float t3 = (-c2) / b2;
            ret++;
            zeroes[ret] = t3;
        }
        return ret - off;
    }

    static int cubicRootsInAB(float d2, float a2, float b2, float c2, float[] pts, int off, float A2, float B2) {
        int num;
        if (d2 == 0.0f) {
            int num2 = quadraticRoots(a2, b2, c2, pts, off);
            return filterOutNotInAB(pts, off, num2, A2, B2) - off;
        }
        float a3 = a2 / d2;
        float b3 = b2 / d2;
        double sq_A = a3 * a3;
        double p2 = 0.3333333333333333d * (((-0.3333333333333333d) * sq_A) + b3);
        double q2 = 0.5d * ((((0.07407407407407407d * a3) * sq_A) - ((0.3333333333333333d * a3) * b3)) + (c2 / d2));
        double cb_p = p2 * p2 * p2;
        double D2 = (q2 * q2) + cb_p;
        if (D2 < 0.0d) {
            double phi = 0.3333333333333333d * Math.acos((-q2) / Math.sqrt(-cb_p));
            double t2 = 2.0d * Math.sqrt(-p2);
            pts[off + 0] = (float) (t2 * Math.cos(phi));
            pts[off + 1] = (float) ((-t2) * Math.cos(phi + 1.0471975511965976d));
            pts[off + 2] = (float) ((-t2) * Math.cos(phi - 1.0471975511965976d));
            num = 3;
        } else {
            double sqrt_D = Math.sqrt(D2);
            double u2 = Math.cbrt(sqrt_D - q2);
            double v2 = -Math.cbrt(sqrt_D + q2);
            pts[off] = (float) (u2 + v2);
            num = 1;
            if (within(D2, 0.0d, 1.0E-8d)) {
                pts[off + 1] = -(pts[off] / 2.0f);
                num = 2;
            }
        }
        float sub = 0.33333334f * a3;
        for (int i2 = 0; i2 < num; i2++) {
            int i3 = off + i2;
            pts[i3] = pts[i3] - sub;
        }
        return filterOutNotInAB(pts, off, num, A2, B2) - off;
    }

    static float[] widenArray(float[] in, int cursize, int numToAdd) {
        if (in.length >= cursize + numToAdd) {
            return in;
        }
        return Arrays.copyOf(in, 2 * (cursize + numToAdd));
    }

    static int[] widenArray(int[] in, int cursize, int numToAdd) {
        if (in.length >= cursize + numToAdd) {
            return in;
        }
        return Arrays.copyOf(in, 2 * (cursize + numToAdd));
    }

    static float evalCubic(float a2, float b2, float c2, float d2, float t2) {
        return (t2 * ((t2 * ((t2 * a2) + b2)) + c2)) + d2;
    }

    static float evalQuad(float a2, float b2, float c2, float t2) {
        return (t2 * ((t2 * a2) + b2)) + c2;
    }

    static int filterOutNotInAB(float[] nums, int off, int len, float a2, float b2) {
        int ret = off;
        for (int i2 = off; i2 < off + len; i2++) {
            if (nums[i2] >= a2 && nums[i2] < b2) {
                int i3 = ret;
                ret++;
                nums[i3] = nums[i2];
            }
        }
        return ret;
    }

    static float polyLineLength(float[] poly, int off, int nCoords) {
        if (!$assertionsDisabled && (nCoords % 2 != 0 || poly.length < off + nCoords)) {
            throw new AssertionError((Object) "");
        }
        float acc = 0.0f;
        for (int i2 = off + 2; i2 < off + nCoords; i2 += 2) {
            acc += linelen(poly[i2], poly[i2 + 1], poly[i2 - 2], poly[i2 - 1]);
        }
        return acc;
    }

    static float linelen(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.sqrt((dx * dx) + (dy * dy));
    }

    static void subdivide(float[] src, int srcoff, float[] left, int leftoff, float[] right, int rightoff, int type) {
        switch (type) {
            case 6:
                subdivideQuad(src, srcoff, left, leftoff, right, rightoff);
                return;
            case 8:
                subdivideCubic(src, srcoff, left, leftoff, right, rightoff);
                return;
            default:
                throw new InternalError("Unsupported curve type");
        }
    }

    static void isort(float[] a2, int off, int len) {
        for (int i2 = off + 1; i2 < off + len; i2++) {
            float ai2 = a2[i2];
            int j2 = i2 - 1;
            while (j2 >= off && a2[j2] > ai2) {
                a2[j2 + 1] = a2[j2];
                j2--;
            }
            a2[j2 + 1] = ai2;
        }
    }

    static void subdivideCubic(float[] src, int srcoff, float[] left, int leftoff, float[] right, int rightoff) {
        float x1 = src[srcoff + 0];
        float y1 = src[srcoff + 1];
        float ctrlx1 = src[srcoff + 2];
        float ctrly1 = src[srcoff + 3];
        float ctrlx2 = src[srcoff + 4];
        float ctrly2 = src[srcoff + 5];
        float x2 = src[srcoff + 6];
        float y2 = src[srcoff + 7];
        if (left != null) {
            left[leftoff + 0] = x1;
            left[leftoff + 1] = y1;
        }
        if (right != null) {
            right[rightoff + 6] = x2;
            right[rightoff + 7] = y2;
        }
        float x12 = (x1 + ctrlx1) / 2.0f;
        float y12 = (y1 + ctrly1) / 2.0f;
        float x22 = (x2 + ctrlx2) / 2.0f;
        float y22 = (y2 + ctrly2) / 2.0f;
        float centerx = (ctrlx1 + ctrlx2) / 2.0f;
        float centery = (ctrly1 + ctrly2) / 2.0f;
        float ctrlx12 = (x12 + centerx) / 2.0f;
        float ctrly12 = (y12 + centery) / 2.0f;
        float ctrlx22 = (x22 + centerx) / 2.0f;
        float ctrly22 = (y22 + centery) / 2.0f;
        float centerx2 = (ctrlx12 + ctrlx22) / 2.0f;
        float centery2 = (ctrly12 + ctrly22) / 2.0f;
        if (left != null) {
            left[leftoff + 2] = x12;
            left[leftoff + 3] = y12;
            left[leftoff + 4] = ctrlx12;
            left[leftoff + 5] = ctrly12;
            left[leftoff + 6] = centerx2;
            left[leftoff + 7] = centery2;
        }
        if (right != null) {
            right[rightoff + 0] = centerx2;
            right[rightoff + 1] = centery2;
            right[rightoff + 2] = ctrlx22;
            right[rightoff + 3] = ctrly22;
            right[rightoff + 4] = x22;
            right[rightoff + 5] = y22;
        }
    }

    static void subdivideCubicAt(float t2, float[] src, int srcoff, float[] left, int leftoff, float[] right, int rightoff) {
        float x1 = src[srcoff + 0];
        float y1 = src[srcoff + 1];
        float ctrlx1 = src[srcoff + 2];
        float ctrly1 = src[srcoff + 3];
        float ctrlx2 = src[srcoff + 4];
        float ctrly2 = src[srcoff + 5];
        float x2 = src[srcoff + 6];
        float y2 = src[srcoff + 7];
        if (left != null) {
            left[leftoff + 0] = x1;
            left[leftoff + 1] = y1;
        }
        if (right != null) {
            right[rightoff + 6] = x2;
            right[rightoff + 7] = y2;
        }
        float x12 = x1 + (t2 * (ctrlx1 - x1));
        float y12 = y1 + (t2 * (ctrly1 - y1));
        float x22 = ctrlx2 + (t2 * (x2 - ctrlx2));
        float y22 = ctrly2 + (t2 * (y2 - ctrly2));
        float centerx = ctrlx1 + (t2 * (ctrlx2 - ctrlx1));
        float centery = ctrly1 + (t2 * (ctrly2 - ctrly1));
        float ctrlx12 = x12 + (t2 * (centerx - x12));
        float ctrly12 = y12 + (t2 * (centery - y12));
        float ctrlx22 = centerx + (t2 * (x22 - centerx));
        float ctrly22 = centery + (t2 * (y22 - centery));
        float centerx2 = ctrlx12 + (t2 * (ctrlx22 - ctrlx12));
        float centery2 = ctrly12 + (t2 * (ctrly22 - ctrly12));
        if (left != null) {
            left[leftoff + 2] = x12;
            left[leftoff + 3] = y12;
            left[leftoff + 4] = ctrlx12;
            left[leftoff + 5] = ctrly12;
            left[leftoff + 6] = centerx2;
            left[leftoff + 7] = centery2;
        }
        if (right != null) {
            right[rightoff + 0] = centerx2;
            right[rightoff + 1] = centery2;
            right[rightoff + 2] = ctrlx22;
            right[rightoff + 3] = ctrly22;
            right[rightoff + 4] = x22;
            right[rightoff + 5] = y22;
        }
    }

    static void subdivideQuad(float[] src, int srcoff, float[] left, int leftoff, float[] right, int rightoff) {
        float x1 = src[srcoff + 0];
        float y1 = src[srcoff + 1];
        float ctrlx = src[srcoff + 2];
        float ctrly = src[srcoff + 3];
        float x2 = src[srcoff + 4];
        float y2 = src[srcoff + 5];
        if (left != null) {
            left[leftoff + 0] = x1;
            left[leftoff + 1] = y1;
        }
        if (right != null) {
            right[rightoff + 4] = x2;
            right[rightoff + 5] = y2;
        }
        float x12 = (x1 + ctrlx) / 2.0f;
        float y12 = (y1 + ctrly) / 2.0f;
        float x22 = (x2 + ctrlx) / 2.0f;
        float y22 = (y2 + ctrly) / 2.0f;
        float ctrlx2 = (x12 + x22) / 2.0f;
        float ctrly2 = (y12 + y22) / 2.0f;
        if (left != null) {
            left[leftoff + 2] = x12;
            left[leftoff + 3] = y12;
            left[leftoff + 4] = ctrlx2;
            left[leftoff + 5] = ctrly2;
        }
        if (right != null) {
            right[rightoff + 0] = ctrlx2;
            right[rightoff + 1] = ctrly2;
            right[rightoff + 2] = x22;
            right[rightoff + 3] = y22;
        }
    }

    static void subdivideQuadAt(float t2, float[] src, int srcoff, float[] left, int leftoff, float[] right, int rightoff) {
        float x1 = src[srcoff + 0];
        float y1 = src[srcoff + 1];
        float ctrlx = src[srcoff + 2];
        float ctrly = src[srcoff + 3];
        float x2 = src[srcoff + 4];
        float y2 = src[srcoff + 5];
        if (left != null) {
            left[leftoff + 0] = x1;
            left[leftoff + 1] = y1;
        }
        if (right != null) {
            right[rightoff + 4] = x2;
            right[rightoff + 5] = y2;
        }
        float x12 = x1 + (t2 * (ctrlx - x1));
        float y12 = y1 + (t2 * (ctrly - y1));
        float x22 = ctrlx + (t2 * (x2 - ctrlx));
        float y22 = ctrly + (t2 * (y2 - ctrly));
        float ctrlx2 = x12 + (t2 * (x22 - x12));
        float ctrly2 = y12 + (t2 * (y22 - y12));
        if (left != null) {
            left[leftoff + 2] = x12;
            left[leftoff + 3] = y12;
            left[leftoff + 4] = ctrlx2;
            left[leftoff + 5] = ctrly2;
        }
        if (right != null) {
            right[rightoff + 0] = ctrlx2;
            right[rightoff + 1] = ctrly2;
            right[rightoff + 2] = x22;
            right[rightoff + 3] = y22;
        }
    }

    static void subdivideAt(float t2, float[] src, int srcoff, float[] left, int leftoff, float[] right, int rightoff, int size) {
        switch (size) {
            case 6:
                subdivideQuadAt(t2, src, srcoff, left, leftoff, right, rightoff);
                break;
            case 8:
                subdivideCubicAt(t2, src, srcoff, left, leftoff, right, rightoff);
                break;
        }
    }
}
