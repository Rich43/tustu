package com.sun.prism.impl;

import com.sun.javafx.geom.Quat4f;
import com.sun.javafx.geom.Vec2f;
import com.sun.javafx.geom.Vec3f;

/* loaded from: jfxrt.jar:com/sun/prism/impl/MeshUtil.class */
class MeshUtil {
    static final float NORMAL_WELD_COS = 0.9952f;
    static final float TANGENT_WELD_COS = 0.866f;
    static final float G_UV_PARALLEL = 0.9988f;
    static final float COS_1_DEGREE = 0.9998477f;
    static final float BIG_ENOUGH_NORMA2 = 0.0625f;
    static final double PI = 3.141592653589793d;
    static final float INV_SQRT2 = 0.70710677f;
    static final float DEAD_FACE = 9.094947E-13f;
    static final float MAGIC_SMALL = 1.0E-10f;
    static final float COS110 = -0.33333334f;

    private MeshUtil() {
    }

    static boolean isDeadFace(float areaSquared) {
        return areaSquared < DEAD_FACE;
    }

    static boolean isDeadFace(int[] f2) {
        return f2[0] == f2[1] || f2[1] == f2[2] || f2[2] == f2[0];
    }

    static boolean isNormalAlmostEqual(Vec3f n1, Vec3f n2) {
        return n1.dot(n2) >= COS_1_DEGREE;
    }

    static boolean isTangentOk(Vec3f[] t1, Vec3f[] t2) {
        return t1[0].dot(t2[0]) >= NORMAL_WELD_COS && t1[1].dot(t2[1]) >= TANGENT_WELD_COS && t1[2].dot(t2[2]) >= TANGENT_WELD_COS;
    }

    static boolean isNormalOkAfterWeld(Vec3f normalSum) {
        return normalSum.dot(normalSum) > BIG_ENOUGH_NORMA2;
    }

    static boolean isTangentOK(Vec3f[] nSum) {
        return isTangentOk(nSum, nSum);
    }

    static boolean isOppositeLookingNormals(Vec3f[] n1, Vec3f[] n2) {
        float cosPhi = n1[0].dot(n2[0]);
        return cosPhi < COS110;
    }

    static float fabs(float x2) {
        return x2 < 0.0f ? -x2 : x2;
    }

    static void getOrt(Vec3f a2, Vec3f b2) {
        b2.cross(a2, b2);
        b2.cross(b2, a2);
    }

    static void orthogonalizeTB(Vec3f[] norm) {
        getOrt(norm[0], norm[1]);
        getOrt(norm[0], norm[2]);
        norm[1].normalize();
        norm[2].normalize();
    }

    static void computeTBNNormalized(Vec3f pa, Vec3f pb, Vec3f pc, Vec2f ta, Vec2f tb, Vec2f tc, Vec3f[] norm) {
        MeshTempState instance = MeshTempState.getInstance();
        Vec3f n2 = instance.vec3f1;
        Vec3f v1 = instance.vec3f2;
        Vec3f v2 = instance.vec3f3;
        v1.sub(pb, pa);
        v2.sub(pc, pa);
        n2.cross(v1, v2);
        norm[0].set(n2);
        norm[0].normalize();
        v1.set(0.0f, tb.f11928x - ta.f11928x, tb.f11929y - ta.f11929y);
        v2.set(0.0f, tc.f11928x - ta.f11928x, tc.f11929y - ta.f11929y);
        if (v1.f11934y * v2.f11935z == v1.f11935z * v2.f11934y) {
            generateTB(pa, pb, pc, norm);
            return;
        }
        v1.f11933x = pb.f11933x - pa.f11933x;
        v2.f11933x = pc.f11933x - pa.f11933x;
        n2.cross(v1, v2);
        norm[1].f11933x = (-n2.f11934y) / n2.f11933x;
        norm[2].f11933x = (-n2.f11935z) / n2.f11933x;
        v1.f11933x = pb.f11934y - pa.f11934y;
        v2.f11933x = pc.f11934y - pa.f11934y;
        n2.cross(v1, v2);
        norm[1].f11934y = (-n2.f11934y) / n2.f11933x;
        norm[2].f11934y = (-n2.f11935z) / n2.f11933x;
        v1.f11933x = pb.f11935z - pa.f11935z;
        v2.f11933x = pc.f11935z - pa.f11935z;
        n2.cross(v1, v2);
        norm[1].f11935z = (-n2.f11934y) / n2.f11933x;
        norm[2].f11935z = (-n2.f11935z) / n2.f11933x;
        norm[1].normalize();
        norm[2].normalize();
    }

    static void fixParallelTB(Vec3f[] ntb) {
        MeshTempState instance = MeshTempState.getInstance();
        Vec3f median = instance.vec3f1;
        median.add(ntb[1], ntb[2]);
        Vec3f ort = instance.vec3f2;
        ort.cross(ntb[0], median);
        median.normalize();
        ort.normalize();
        ntb[1].add(median, ort);
        ntb[1].mul(INV_SQRT2);
        ntb[2].sub(median, ort);
        ntb[2].mul(INV_SQRT2);
    }

    static void generateTB(Vec3f v0, Vec3f v1, Vec3f v2, Vec3f[] ntb) {
        MeshTempState instance = MeshTempState.getInstance();
        Vec3f a2 = instance.vec3f1;
        a2.sub(v1, v0);
        Vec3f b2 = instance.vec3f2;
        b2.sub(v2, v0);
        if (a2.dot(a2) > b2.dot(b2)) {
            ntb[1].set(a2);
            ntb[1].normalize();
            ntb[2].cross(ntb[0], ntb[1]);
        } else {
            ntb[2].set(b2);
            ntb[2].normalize();
            ntb[1].cross(ntb[2], ntb[0]);
        }
    }

    static double clamp(double x2, double min, double max) {
        return x2 < max ? x2 > min ? x2 : min : max;
    }

    static void fixTSpace(Vec3f[] norm) {
        float nNorm = norm[0].length();
        MeshTempState instance = MeshTempState.getInstance();
        Vec3f n1 = instance.vec3f1;
        n1.set(norm[1]);
        Vec3f n2 = instance.vec3f2;
        n2.set(norm[2]);
        getOrt(norm[0], n1);
        getOrt(norm[0], n2);
        float n1Length = n1.length();
        float n2Length = n2.length();
        double cosPhi = n1.dot(n2) / (n1Length * n2Length);
        Vec3f e1 = instance.vec3f3;
        Vec3f e2 = instance.vec3f4;
        if (fabs((float) cosPhi) > 0.998d) {
            Vec3f n2fix = instance.vec3f5;
            n2fix.cross(norm[0], n1);
            n2fix.normalize();
            e2.set(n2fix);
            if (n2fix.dot(n2) < 0.0f) {
                e2.mul(-1.0f);
            }
            e1.set(n1);
            e1.mul(1.0f / n1Length);
        } else {
            double phi = Math.acos(clamp(cosPhi, -1.0d, 1.0d));
            double alpha = (1.5707963267948966d - phi) * 0.5d;
            Vec2f e1Local = instance.vec2f1;
            e1Local.set((float) Math.sin(alpha), (float) Math.cos(alpha));
            Vec2f e2Local = instance.vec2f2;
            e2Local.set((float) Math.sin(alpha + phi), (float) Math.cos(alpha + phi));
            Vec3f n1T = instance.vec3f5;
            n1T.set(n2);
            getOrt(n1, n1T);
            float n1TLength = n1T.length();
            e1.set(n1);
            e1.mul(e1Local.f11929y / n1Length);
            Vec3f n1TT = instance.vec3f6;
            n1TT.set(n1T);
            n1TT.mul(e1Local.f11928x / n1TLength);
            e1.sub(n1TT);
            e2.set(n1);
            e2.mul(e2Local.f11929y / n1Length);
            n1TT.set(n1T);
            n1TT.mul(e2Local.f11928x / n1TLength);
            e2.add(n1TT);
            e1.dot(n1);
            e2.dot(n2);
        }
        norm[1].set(e1);
        norm[2].set(e2);
        norm[0].mul(1.0f / nNorm);
    }

    static void buildQuat(Vec3f[] tm, Quat4f quat) {
        MeshTempState instance = MeshTempState.getInstance();
        float[][] m2 = instance.matrix;
        float[] tmp = instance.vector;
        for (int i2 = 0; i2 < 3; i2++) {
            m2[i2][0] = tm[i2].f11933x;
            m2[i2][1] = tm[i2].f11934y;
            m2[i2][2] = tm[i2].f11935z;
        }
        float trace = m2[0][0] + m2[1][1] + m2[2][2];
        if (trace > 0.0f) {
            float s2 = (float) Math.sqrt(trace + 1.0f);
            float t2 = 0.5f / s2;
            quat.f11912w = 0.5f * s2;
            quat.f11909x = (m2[1][2] - m2[2][1]) * t2;
            quat.f11910y = (m2[2][0] - m2[0][2]) * t2;
            quat.f11911z = (m2[0][1] - m2[1][0]) * t2;
            return;
        }
        int[] next = {1, 2, 0};
        int i3 = 0;
        if (m2[1][1] > m2[0][0]) {
            i3 = 1;
        }
        if (m2[2][2] > m2[i3][i3]) {
            i3 = 2;
        }
        int j2 = next[i3];
        int k2 = next[j2];
        float s3 = (float) Math.sqrt(((m2[i3][i3] - m2[j2][j2]) - m2[k2][k2]) + 1.0f);
        if (m2[j2][k2] < m2[k2][j2]) {
            s3 = -s3;
        }
        float t3 = 0.5f / s3;
        tmp[i3] = 0.5f * s3;
        quat.f11912w = (m2[j2][k2] - m2[k2][j2]) * t3;
        tmp[j2] = (m2[i3][j2] + m2[j2][i3]) * t3;
        tmp[k2] = (m2[i3][k2] + m2[k2][i3]) * t3;
        quat.f11909x = tmp[0];
        quat.f11910y = tmp[1];
        quat.f11911z = tmp[2];
    }
}
