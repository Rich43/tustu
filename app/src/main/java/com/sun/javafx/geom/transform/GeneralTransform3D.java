package com.sun.javafx.geom.transform;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.Vec3f;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/transform/GeneralTransform3D.class */
public final class GeneralTransform3D implements CanTransformVec3d {
    protected double[] mat = new double[16];
    private boolean identity;
    private Vec3d tempV3d;
    private static final double EPSILON_ABSOLUTE = 1.0E-5d;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !GeneralTransform3D.class.desiredAssertionStatus();
    }

    public GeneralTransform3D() {
        setIdentity();
    }

    public boolean isAffine() {
        if (!isInfOrNaN() && almostZero(this.mat[12]) && almostZero(this.mat[13]) && almostZero(this.mat[14]) && almostOne(this.mat[15])) {
            return true;
        }
        return false;
    }

    public GeneralTransform3D set(GeneralTransform3D t1) {
        System.arraycopy(t1.mat, 0, this.mat, 0, this.mat.length);
        updateState();
        return this;
    }

    public GeneralTransform3D set(double[] m2) {
        System.arraycopy(m2, 0, this.mat, 0, this.mat.length);
        updateState();
        return this;
    }

    public double[] get(double[] rv) {
        if (rv == null) {
            rv = new double[this.mat.length];
        }
        System.arraycopy(this.mat, 0, rv, 0, this.mat.length);
        return rv;
    }

    public double get(int index) {
        if ($assertionsDisabled || (index >= 0 && index < this.mat.length)) {
            return this.mat[index];
        }
        throw new AssertionError();
    }

    public BaseBounds transform(BaseBounds src, BaseBounds dst) {
        if (this.tempV3d == null) {
            this.tempV3d = new Vec3d();
        }
        return TransformHelper.general3dBoundsTransform(this, src, dst, this.tempV3d);
    }

    public Point2D transform(Point2D point, Point2D pointOut) {
        if (pointOut == null) {
            pointOut = new Point2D();
        }
        double w2 = (float) ((this.mat[12] * point.f11907x) + (this.mat[13] * point.f11908y) + this.mat[15]);
        pointOut.f11907x = (float) ((this.mat[0] * point.f11907x) + (this.mat[1] * point.f11908y) + this.mat[3]);
        pointOut.f11908y = (float) ((this.mat[4] * point.f11907x) + (this.mat[5] * point.f11908y) + this.mat[7]);
        pointOut.f11907x = (float) (r0.f11907x / w2);
        pointOut.f11908y = (float) (r0.f11908y / w2);
        return pointOut;
    }

    @Override // com.sun.javafx.geom.transform.CanTransformVec3d
    public Vec3d transform(Vec3d point, Vec3d pointOut) {
        if (pointOut == null) {
            pointOut = new Vec3d();
        }
        double w2 = (float) ((this.mat[12] * point.f11930x) + (this.mat[13] * point.f11931y) + (this.mat[14] * point.f11932z) + this.mat[15]);
        pointOut.f11930x = (float) ((this.mat[0] * point.f11930x) + (this.mat[1] * point.f11931y) + (this.mat[2] * point.f11932z) + this.mat[3]);
        pointOut.f11931y = (float) ((this.mat[4] * point.f11930x) + (this.mat[5] * point.f11931y) + (this.mat[6] * point.f11932z) + this.mat[7]);
        pointOut.f11932z = (float) ((this.mat[8] * point.f11930x) + (this.mat[9] * point.f11931y) + (this.mat[10] * point.f11932z) + this.mat[11]);
        if (w2 != 0.0d) {
            pointOut.f11930x /= w2;
            pointOut.f11931y /= w2;
            pointOut.f11932z /= w2;
        }
        return pointOut;
    }

    public Vec3d transform(Vec3d point) {
        return transform(point, point);
    }

    public Vec3f transformNormal(Vec3f normal, Vec3f normalOut) {
        normal.f11933x = (float) ((this.mat[0] * normal.f11933x) + (this.mat[1] * normal.f11934y) + (this.mat[2] * normal.f11935z));
        normal.f11934y = (float) ((this.mat[4] * normal.f11933x) + (this.mat[5] * normal.f11934y) + (this.mat[6] * normal.f11935z));
        normal.f11935z = (float) ((this.mat[8] * normal.f11933x) + (this.mat[9] * normal.f11934y) + (this.mat[10] * normal.f11935z));
        return normalOut;
    }

    public Vec3f transformNormal(Vec3f normal) {
        return transformNormal(normal, normal);
    }

    public GeneralTransform3D perspective(boolean verticalFOV, double fov, double aspect, double zNear, double zFar) {
        double half_fov = fov * 0.5d;
        double deltaZ = zFar - zNear;
        double sine = Math.sin(half_fov);
        double cotangent = Math.cos(half_fov) / sine;
        this.mat[0] = verticalFOV ? cotangent / aspect : cotangent;
        this.mat[5] = verticalFOV ? cotangent : cotangent * aspect;
        this.mat[10] = (-(zFar + zNear)) / deltaZ;
        this.mat[11] = (((-2.0d) * zNear) * zFar) / deltaZ;
        this.mat[14] = -1.0d;
        double[] dArr = this.mat;
        double[] dArr2 = this.mat;
        double[] dArr3 = this.mat;
        double[] dArr4 = this.mat;
        double[] dArr5 = this.mat;
        double[] dArr6 = this.mat;
        double[] dArr7 = this.mat;
        double[] dArr8 = this.mat;
        double[] dArr9 = this.mat;
        double[] dArr10 = this.mat;
        this.mat[15] = 0.0d;
        dArr10[13] = 0.0d;
        dArr9[12] = 0.0d;
        dArr8[9] = 0.0d;
        dArr7[8] = 0.0d;
        dArr6[7] = 0.0d;
        dArr5[6] = 0.0d;
        dArr4[4] = 0.0d;
        dArr3[3] = 0.0d;
        dArr2[2] = 0.0d;
        dArr[1] = 0.0d;
        updateState();
        return this;
    }

    public GeneralTransform3D ortho(double left, double right, double bottom, double top, double near, double far) {
        double deltax = 1.0d / (right - left);
        double deltay = 1.0d / (top - bottom);
        double deltaz = 1.0d / (far - near);
        this.mat[0] = 2.0d * deltax;
        this.mat[3] = (-(right + left)) * deltax;
        this.mat[5] = 2.0d * deltay;
        this.mat[7] = (-(top + bottom)) * deltay;
        this.mat[10] = 2.0d * deltaz;
        this.mat[11] = (far + near) * deltaz;
        double[] dArr = this.mat;
        double[] dArr2 = this.mat;
        double[] dArr3 = this.mat;
        double[] dArr4 = this.mat;
        double[] dArr5 = this.mat;
        double[] dArr6 = this.mat;
        double[] dArr7 = this.mat;
        double[] dArr8 = this.mat;
        this.mat[14] = 0.0d;
        dArr8[13] = 0.0d;
        dArr7[12] = 0.0d;
        dArr6[9] = 0.0d;
        dArr5[8] = 0.0d;
        dArr4[6] = 0.0d;
        dArr3[4] = 0.0d;
        dArr2[2] = 0.0d;
        dArr[1] = 0.0d;
        this.mat[15] = 1.0d;
        updateState();
        return this;
    }

    public double computeClipZCoord() {
        double zEc = (1.0d - this.mat[15]) / this.mat[14];
        double zCc = (this.mat[10] * zEc) + this.mat[11];
        return zCc;
    }

    public double determinant() {
        return (((this.mat[0] * (((this.mat[5] * ((this.mat[10] * this.mat[15]) - (this.mat[11] * this.mat[14]))) - (this.mat[6] * ((this.mat[9] * this.mat[15]) - (this.mat[11] * this.mat[13])))) + (this.mat[7] * ((this.mat[9] * this.mat[14]) - (this.mat[10] * this.mat[13]))))) - (this.mat[1] * (((this.mat[4] * ((this.mat[10] * this.mat[15]) - (this.mat[11] * this.mat[14]))) - (this.mat[6] * ((this.mat[8] * this.mat[15]) - (this.mat[11] * this.mat[12])))) + (this.mat[7] * ((this.mat[8] * this.mat[14]) - (this.mat[10] * this.mat[12])))))) + (this.mat[2] * (((this.mat[4] * ((this.mat[9] * this.mat[15]) - (this.mat[11] * this.mat[13]))) - (this.mat[5] * ((this.mat[8] * this.mat[15]) - (this.mat[11] * this.mat[12])))) + (this.mat[7] * ((this.mat[8] * this.mat[13]) - (this.mat[9] * this.mat[12])))))) - (this.mat[3] * (((this.mat[4] * ((this.mat[9] * this.mat[14]) - (this.mat[10] * this.mat[13]))) - (this.mat[5] * ((this.mat[8] * this.mat[14]) - (this.mat[10] * this.mat[12])))) + (this.mat[6] * ((this.mat[8] * this.mat[13]) - (this.mat[9] * this.mat[12])))));
    }

    public GeneralTransform3D invert() {
        return invert(this);
    }

    private GeneralTransform3D invert(GeneralTransform3D t1) {
        double[] tmp = new double[16];
        int[] row_perm = new int[4];
        System.arraycopy(t1.mat, 0, tmp, 0, tmp.length);
        if (!luDecomposition(tmp, row_perm)) {
            throw new SingularMatrixException();
        }
        this.mat[0] = 1.0d;
        this.mat[1] = 0.0d;
        this.mat[2] = 0.0d;
        this.mat[3] = 0.0d;
        this.mat[4] = 0.0d;
        this.mat[5] = 1.0d;
        this.mat[6] = 0.0d;
        this.mat[7] = 0.0d;
        this.mat[8] = 0.0d;
        this.mat[9] = 0.0d;
        this.mat[10] = 1.0d;
        this.mat[11] = 0.0d;
        this.mat[12] = 0.0d;
        this.mat[13] = 0.0d;
        this.mat[14] = 0.0d;
        this.mat[15] = 1.0d;
        luBacksubstitution(tmp, row_perm, this.mat);
        updateState();
        return this;
    }

    private static boolean luDecomposition(double[] matrix0, int[] row_perm) {
        double[] row_scale = new double[4];
        int ptr = 0;
        int rs = 0;
        int i2 = 4;
        while (true) {
            int i3 = i2;
            i2--;
            if (i3 != 0) {
                double big = 0.0d;
                int j2 = 4;
                while (true) {
                    int i4 = j2;
                    j2--;
                    if (i4 == 0) {
                        break;
                    }
                    int i5 = ptr;
                    ptr++;
                    double temp = matrix0[i5];
                    double temp2 = Math.abs(temp);
                    if (temp2 > big) {
                        big = temp2;
                    }
                }
                if (big == 0.0d) {
                    return false;
                }
                int i6 = rs;
                rs++;
                row_scale[i6] = 1.0d / big;
            } else {
                for (int j3 = 0; j3 < 4; j3++) {
                    for (int i7 = 0; i7 < j3; i7++) {
                        int target = 0 + (4 * i7) + j3;
                        double sum = matrix0[target];
                        int k2 = i7;
                        int p1 = 0 + (4 * i7);
                        int p2 = 0 + j3;
                        while (true) {
                            int i8 = k2;
                            k2--;
                            if (i8 != 0) {
                                sum -= matrix0[p1] * matrix0[p2];
                                p1++;
                                p2 += 4;
                            }
                        }
                        matrix0[target] = sum;
                    }
                    double big2 = 0.0d;
                    int imax = -1;
                    for (int i9 = j3; i9 < 4; i9++) {
                        int target2 = 0 + (4 * i9) + j3;
                        double sum2 = matrix0[target2];
                        int k3 = j3;
                        int p12 = 0 + (4 * i9);
                        int p22 = 0 + j3;
                        while (true) {
                            int i10 = k3;
                            k3--;
                            if (i10 == 0) {
                                break;
                            }
                            sum2 -= matrix0[p12] * matrix0[p22];
                            p12++;
                            p22 += 4;
                        }
                        matrix0[target2] = sum2;
                        double temp3 = row_scale[i9] * Math.abs(sum2);
                        if (temp3 >= big2) {
                            big2 = temp3;
                            imax = i9;
                        }
                    }
                    if (imax < 0) {
                        return false;
                    }
                    if (j3 != imax) {
                        int k4 = 4;
                        int p13 = 0 + (4 * imax);
                        int p23 = 0 + (4 * j3);
                        while (true) {
                            int i11 = k4;
                            k4--;
                            if (i11 == 0) {
                                break;
                            }
                            double temp4 = matrix0[p13];
                            int i12 = p13;
                            p13++;
                            matrix0[i12] = matrix0[p23];
                            int i13 = p23;
                            p23++;
                            matrix0[i13] = temp4;
                        }
                        row_scale[imax] = row_scale[j3];
                    }
                    row_perm[j3] = imax;
                    if (matrix0[0 + (4 * j3) + j3] == 0.0d) {
                        return false;
                    }
                    if (j3 != 3) {
                        double temp5 = 1.0d / matrix0[(0 + (4 * j3)) + j3];
                        int target3 = 0 + (4 * (j3 + 1)) + j3;
                        int i14 = 3 - j3;
                        while (true) {
                            int i15 = i14;
                            i14--;
                            if (i15 != 0) {
                                int i16 = target3;
                                matrix0[i16] = matrix0[i16] * temp5;
                                target3 += 4;
                            }
                        }
                    }
                }
                return true;
            }
        }
    }

    private static void luBacksubstitution(double[] matrix1, int[] row_perm, double[] matrix2) {
        for (int k2 = 0; k2 < 4; k2++) {
            int cv = k2;
            int ii = -1;
            for (int i2 = 0; i2 < 4; i2++) {
                int ip = row_perm[0 + i2];
                double sum = matrix2[cv + (4 * ip)];
                matrix2[cv + (4 * ip)] = matrix2[cv + (4 * i2)];
                if (ii >= 0) {
                    int rv = i2 * 4;
                    for (int j2 = ii; j2 <= i2 - 1; j2++) {
                        sum -= matrix1[rv + j2] * matrix2[cv + (4 * j2)];
                    }
                } else if (sum != 0.0d) {
                    ii = i2;
                }
                matrix2[cv + (4 * i2)] = sum;
            }
            int i3 = cv + 12;
            matrix2[i3] = matrix2[i3] / matrix1[12 + 3];
            int rv2 = 12 - 4;
            matrix2[cv + 8] = (matrix2[cv + 8] - (matrix1[rv2 + 3] * matrix2[cv + 12])) / matrix1[rv2 + 2];
            int rv3 = rv2 - 4;
            matrix2[cv + 4] = ((matrix2[cv + 4] - (matrix1[rv3 + 2] * matrix2[cv + 8])) - (matrix1[rv3 + 3] * matrix2[cv + 12])) / matrix1[rv3 + 1];
            int rv4 = rv3 - 4;
            matrix2[cv + 0] = (((matrix2[cv + 0] - (matrix1[rv4 + 1] * matrix2[cv + 4])) - (matrix1[rv4 + 2] * matrix2[cv + 8])) - (matrix1[rv4 + 3] * matrix2[cv + 12])) / matrix1[rv4 + 0];
        }
    }

    public GeneralTransform3D mul(BaseTransform t1) {
        double tmp12;
        double tmp13;
        double tmp14;
        double tmp15;
        if (t1.isIdentity()) {
            return this;
        }
        double mxx = t1.getMxx();
        double mxy = t1.getMxy();
        double mxz = t1.getMxz();
        double mxt = t1.getMxt();
        double myx = t1.getMyx();
        double myy = t1.getMyy();
        double myz = t1.getMyz();
        double myt = t1.getMyt();
        double mzx = t1.getMzx();
        double mzy = t1.getMzy();
        double mzz = t1.getMzz();
        double mzt = t1.getMzt();
        double tmp0 = (this.mat[0] * mxx) + (this.mat[1] * myx) + (this.mat[2] * mzx);
        double tmp1 = (this.mat[0] * mxy) + (this.mat[1] * myy) + (this.mat[2] * mzy);
        double tmp2 = (this.mat[0] * mxz) + (this.mat[1] * myz) + (this.mat[2] * mzz);
        double tmp3 = (this.mat[0] * mxt) + (this.mat[1] * myt) + (this.mat[2] * mzt) + this.mat[3];
        double tmp4 = (this.mat[4] * mxx) + (this.mat[5] * myx) + (this.mat[6] * mzx);
        double tmp5 = (this.mat[4] * mxy) + (this.mat[5] * myy) + (this.mat[6] * mzy);
        double tmp6 = (this.mat[4] * mxz) + (this.mat[5] * myz) + (this.mat[6] * mzz);
        double tmp7 = (this.mat[4] * mxt) + (this.mat[5] * myt) + (this.mat[6] * mzt) + this.mat[7];
        double tmp8 = (this.mat[8] * mxx) + (this.mat[9] * myx) + (this.mat[10] * mzx);
        double tmp9 = (this.mat[8] * mxy) + (this.mat[9] * myy) + (this.mat[10] * mzy);
        double tmp10 = (this.mat[8] * mxz) + (this.mat[9] * myz) + (this.mat[10] * mzz);
        double tmp11 = (this.mat[8] * mxt) + (this.mat[9] * myt) + (this.mat[10] * mzt) + this.mat[11];
        if (isAffine()) {
            tmp14 = 0.0d;
            tmp13 = 0.0d;
            tmp12 = 0.0d;
            tmp15 = 1.0d;
        } else {
            tmp12 = (this.mat[12] * mxx) + (this.mat[13] * myx) + (this.mat[14] * mzx);
            tmp13 = (this.mat[12] * mxy) + (this.mat[13] * myy) + (this.mat[14] * mzy);
            tmp14 = (this.mat[12] * mxz) + (this.mat[13] * myz) + (this.mat[14] * mzz);
            tmp15 = (this.mat[12] * mxt) + (this.mat[13] * myt) + (this.mat[14] * mzt) + this.mat[15];
        }
        this.mat[0] = tmp0;
        this.mat[1] = tmp1;
        this.mat[2] = tmp2;
        this.mat[3] = tmp3;
        this.mat[4] = tmp4;
        this.mat[5] = tmp5;
        this.mat[6] = tmp6;
        this.mat[7] = tmp7;
        this.mat[8] = tmp8;
        this.mat[9] = tmp9;
        this.mat[10] = tmp10;
        this.mat[11] = tmp11;
        this.mat[12] = tmp12;
        this.mat[13] = tmp13;
        this.mat[14] = tmp14;
        this.mat[15] = tmp15;
        updateState();
        return this;
    }

    public GeneralTransform3D scale(double sx, double sy, double sz) {
        boolean update = false;
        if (sx != 1.0d) {
            double[] dArr = this.mat;
            dArr[0] = dArr[0] * sx;
            double[] dArr2 = this.mat;
            dArr2[4] = dArr2[4] * sx;
            double[] dArr3 = this.mat;
            dArr3[8] = dArr3[8] * sx;
            double[] dArr4 = this.mat;
            dArr4[12] = dArr4[12] * sx;
            update = true;
        }
        if (sy != 1.0d) {
            double[] dArr5 = this.mat;
            dArr5[1] = dArr5[1] * sy;
            double[] dArr6 = this.mat;
            dArr6[5] = dArr6[5] * sy;
            double[] dArr7 = this.mat;
            dArr7[9] = dArr7[9] * sy;
            double[] dArr8 = this.mat;
            dArr8[13] = dArr8[13] * sy;
            update = true;
        }
        if (sz != 1.0d) {
            double[] dArr9 = this.mat;
            dArr9[2] = dArr9[2] * sz;
            double[] dArr10 = this.mat;
            dArr10[6] = dArr10[6] * sz;
            double[] dArr11 = this.mat;
            dArr11[10] = dArr11[10] * sz;
            double[] dArr12 = this.mat;
            dArr12[14] = dArr12[14] * sz;
            update = true;
        }
        if (update) {
            updateState();
        }
        return this;
    }

    public GeneralTransform3D mul(GeneralTransform3D t1) {
        double tmp0;
        double tmp1;
        double tmp2;
        double tmp3;
        double tmp4;
        double tmp5;
        double tmp6;
        double tmp7;
        double tmp8;
        double tmp9;
        double tmp10;
        double tmp11;
        double tmp12;
        double tmp13;
        double tmp14;
        double tmp15;
        if (t1.isIdentity()) {
            return this;
        }
        if (t1.isAffine()) {
            tmp0 = (this.mat[0] * t1.mat[0]) + (this.mat[1] * t1.mat[4]) + (this.mat[2] * t1.mat[8]);
            tmp1 = (this.mat[0] * t1.mat[1]) + (this.mat[1] * t1.mat[5]) + (this.mat[2] * t1.mat[9]);
            tmp2 = (this.mat[0] * t1.mat[2]) + (this.mat[1] * t1.mat[6]) + (this.mat[2] * t1.mat[10]);
            tmp3 = (this.mat[0] * t1.mat[3]) + (this.mat[1] * t1.mat[7]) + (this.mat[2] * t1.mat[11]) + this.mat[3];
            tmp4 = (this.mat[4] * t1.mat[0]) + (this.mat[5] * t1.mat[4]) + (this.mat[6] * t1.mat[8]);
            tmp5 = (this.mat[4] * t1.mat[1]) + (this.mat[5] * t1.mat[5]) + (this.mat[6] * t1.mat[9]);
            tmp6 = (this.mat[4] * t1.mat[2]) + (this.mat[5] * t1.mat[6]) + (this.mat[6] * t1.mat[10]);
            tmp7 = (this.mat[4] * t1.mat[3]) + (this.mat[5] * t1.mat[7]) + (this.mat[6] * t1.mat[11]) + this.mat[7];
            tmp8 = (this.mat[8] * t1.mat[0]) + (this.mat[9] * t1.mat[4]) + (this.mat[10] * t1.mat[8]);
            tmp9 = (this.mat[8] * t1.mat[1]) + (this.mat[9] * t1.mat[5]) + (this.mat[10] * t1.mat[9]);
            tmp10 = (this.mat[8] * t1.mat[2]) + (this.mat[9] * t1.mat[6]) + (this.mat[10] * t1.mat[10]);
            tmp11 = (this.mat[8] * t1.mat[3]) + (this.mat[9] * t1.mat[7]) + (this.mat[10] * t1.mat[11]) + this.mat[11];
            if (isAffine()) {
                tmp14 = 0.0d;
                tmp13 = 0.0d;
                tmp12 = 0.0d;
                tmp15 = 1.0d;
            } else {
                tmp12 = (this.mat[12] * t1.mat[0]) + (this.mat[13] * t1.mat[4]) + (this.mat[14] * t1.mat[8]);
                tmp13 = (this.mat[12] * t1.mat[1]) + (this.mat[13] * t1.mat[5]) + (this.mat[14] * t1.mat[9]);
                tmp14 = (this.mat[12] * t1.mat[2]) + (this.mat[13] * t1.mat[6]) + (this.mat[14] * t1.mat[10]);
                tmp15 = (this.mat[12] * t1.mat[3]) + (this.mat[13] * t1.mat[7]) + (this.mat[14] * t1.mat[11]) + this.mat[15];
            }
        } else {
            tmp0 = (this.mat[0] * t1.mat[0]) + (this.mat[1] * t1.mat[4]) + (this.mat[2] * t1.mat[8]) + (this.mat[3] * t1.mat[12]);
            tmp1 = (this.mat[0] * t1.mat[1]) + (this.mat[1] * t1.mat[5]) + (this.mat[2] * t1.mat[9]) + (this.mat[3] * t1.mat[13]);
            tmp2 = (this.mat[0] * t1.mat[2]) + (this.mat[1] * t1.mat[6]) + (this.mat[2] * t1.mat[10]) + (this.mat[3] * t1.mat[14]);
            tmp3 = (this.mat[0] * t1.mat[3]) + (this.mat[1] * t1.mat[7]) + (this.mat[2] * t1.mat[11]) + (this.mat[3] * t1.mat[15]);
            tmp4 = (this.mat[4] * t1.mat[0]) + (this.mat[5] * t1.mat[4]) + (this.mat[6] * t1.mat[8]) + (this.mat[7] * t1.mat[12]);
            tmp5 = (this.mat[4] * t1.mat[1]) + (this.mat[5] * t1.mat[5]) + (this.mat[6] * t1.mat[9]) + (this.mat[7] * t1.mat[13]);
            tmp6 = (this.mat[4] * t1.mat[2]) + (this.mat[5] * t1.mat[6]) + (this.mat[6] * t1.mat[10]) + (this.mat[7] * t1.mat[14]);
            tmp7 = (this.mat[4] * t1.mat[3]) + (this.mat[5] * t1.mat[7]) + (this.mat[6] * t1.mat[11]) + (this.mat[7] * t1.mat[15]);
            tmp8 = (this.mat[8] * t1.mat[0]) + (this.mat[9] * t1.mat[4]) + (this.mat[10] * t1.mat[8]) + (this.mat[11] * t1.mat[12]);
            tmp9 = (this.mat[8] * t1.mat[1]) + (this.mat[9] * t1.mat[5]) + (this.mat[10] * t1.mat[9]) + (this.mat[11] * t1.mat[13]);
            tmp10 = (this.mat[8] * t1.mat[2]) + (this.mat[9] * t1.mat[6]) + (this.mat[10] * t1.mat[10]) + (this.mat[11] * t1.mat[14]);
            tmp11 = (this.mat[8] * t1.mat[3]) + (this.mat[9] * t1.mat[7]) + (this.mat[10] * t1.mat[11]) + (this.mat[11] * t1.mat[15]);
            if (isAffine()) {
                tmp12 = t1.mat[12];
                tmp13 = t1.mat[13];
                tmp14 = t1.mat[14];
                tmp15 = t1.mat[15];
            } else {
                tmp12 = (this.mat[12] * t1.mat[0]) + (this.mat[13] * t1.mat[4]) + (this.mat[14] * t1.mat[8]) + (this.mat[15] * t1.mat[12]);
                tmp13 = (this.mat[12] * t1.mat[1]) + (this.mat[13] * t1.mat[5]) + (this.mat[14] * t1.mat[9]) + (this.mat[15] * t1.mat[13]);
                tmp14 = (this.mat[12] * t1.mat[2]) + (this.mat[13] * t1.mat[6]) + (this.mat[14] * t1.mat[10]) + (this.mat[15] * t1.mat[14]);
                tmp15 = (this.mat[12] * t1.mat[3]) + (this.mat[13] * t1.mat[7]) + (this.mat[14] * t1.mat[11]) + (this.mat[15] * t1.mat[15]);
            }
        }
        this.mat[0] = tmp0;
        this.mat[1] = tmp1;
        this.mat[2] = tmp2;
        this.mat[3] = tmp3;
        this.mat[4] = tmp4;
        this.mat[5] = tmp5;
        this.mat[6] = tmp6;
        this.mat[7] = tmp7;
        this.mat[8] = tmp8;
        this.mat[9] = tmp9;
        this.mat[10] = tmp10;
        this.mat[11] = tmp11;
        this.mat[12] = tmp12;
        this.mat[13] = tmp13;
        this.mat[14] = tmp14;
        this.mat[15] = tmp15;
        updateState();
        return this;
    }

    public GeneralTransform3D setIdentity() {
        this.mat[0] = 1.0d;
        this.mat[1] = 0.0d;
        this.mat[2] = 0.0d;
        this.mat[3] = 0.0d;
        this.mat[4] = 0.0d;
        this.mat[5] = 1.0d;
        this.mat[6] = 0.0d;
        this.mat[7] = 0.0d;
        this.mat[8] = 0.0d;
        this.mat[9] = 0.0d;
        this.mat[10] = 1.0d;
        this.mat[11] = 0.0d;
        this.mat[12] = 0.0d;
        this.mat[13] = 0.0d;
        this.mat[14] = 0.0d;
        this.mat[15] = 1.0d;
        this.identity = true;
        return this;
    }

    public boolean isIdentity() {
        return this.identity;
    }

    private void updateState() {
        this.identity = this.mat[0] == 1.0d && this.mat[5] == 1.0d && this.mat[10] == 1.0d && this.mat[15] == 1.0d && this.mat[1] == 0.0d && this.mat[2] == 0.0d && this.mat[3] == 0.0d && this.mat[4] == 0.0d && this.mat[6] == 0.0d && this.mat[7] == 0.0d && this.mat[8] == 0.0d && this.mat[9] == 0.0d && this.mat[11] == 0.0d && this.mat[12] == 0.0d && this.mat[13] == 0.0d && this.mat[14] == 0.0d;
    }

    boolean isInfOrNaN() {
        double d2 = 0.0d;
        for (int i2 = 0; i2 < this.mat.length; i2++) {
            d2 *= this.mat[i2];
        }
        return d2 != 0.0d;
    }

    static boolean almostZero(double a2) {
        return a2 < EPSILON_ABSOLUTE && a2 > -1.0E-5d;
    }

    static boolean almostOne(double a2) {
        return a2 < 1.00001d && a2 > 0.99999d;
    }

    public GeneralTransform3D copy() {
        GeneralTransform3D newTransform = new GeneralTransform3D();
        newTransform.set(this);
        return newTransform;
    }

    public String toString() {
        return this.mat[0] + ", " + this.mat[1] + ", " + this.mat[2] + ", " + this.mat[3] + "\n" + this.mat[4] + ", " + this.mat[5] + ", " + this.mat[6] + ", " + this.mat[7] + "\n" + this.mat[8] + ", " + this.mat[9] + ", " + this.mat[10] + ", " + this.mat[11] + "\n" + this.mat[12] + ", " + this.mat[13] + ", " + this.mat[14] + ", " + this.mat[15] + "\n";
    }
}
