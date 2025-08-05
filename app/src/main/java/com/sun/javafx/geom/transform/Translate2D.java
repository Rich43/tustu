package com.sun.javafx.geom.transform;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.BaseTransform;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/transform/Translate2D.class */
public class Translate2D extends BaseTransform {
    private double mxt;
    private double myt;
    private static final long BASE_HASH;

    public static BaseTransform getInstance(double mxt, double myt) {
        if (mxt == 0.0d && myt == 0.0d) {
            return IDENTITY_TRANSFORM;
        }
        return new Translate2D(mxt, myt);
    }

    public Translate2D(double tx, double ty) {
        this.mxt = tx;
        this.myt = ty;
    }

    public Translate2D(BaseTransform tx) {
        if (!tx.isTranslateOrIdentity()) {
            degreeError(BaseTransform.Degree.TRANSLATE_2D);
        }
        this.mxt = tx.getMxt();
        this.myt = tx.getMyt();
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform.Degree getDegree() {
        return BaseTransform.Degree.TRANSLATE_2D;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public double getDeterminant() {
        return 1.0d;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public double getMxt() {
        return this.mxt;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public double getMyt() {
        return this.myt;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public int getType() {
        return (this.mxt == 0.0d && this.myt == 0.0d) ? 0 : 1;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public boolean isIdentity() {
        return this.mxt == 0.0d && this.myt == 0.0d;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public boolean isTranslateOrIdentity() {
        return true;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public boolean is2D() {
        return true;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public Point2D transform(Point2D src, Point2D dst) {
        if (dst == null) {
            dst = makePoint(src, dst);
        }
        dst.setLocation((float) (src.f11907x + this.mxt), (float) (src.f11908y + this.myt));
        return dst;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public Point2D inverseTransform(Point2D src, Point2D dst) {
        if (dst == null) {
            dst = makePoint(src, dst);
        }
        dst.setLocation((float) (src.f11907x - this.mxt), (float) (src.f11908y - this.myt));
        return dst;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform, com.sun.javafx.geom.transform.CanTransformVec3d
    public Vec3d transform(Vec3d src, Vec3d dst) {
        if (dst == null) {
            dst = new Vec3d();
        }
        dst.f11930x = src.f11930x + this.mxt;
        dst.f11931y = src.f11931y + this.myt;
        dst.f11932z = src.f11932z;
        return dst;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public Vec3d deltaTransform(Vec3d src, Vec3d dst) {
        if (dst == null) {
            dst = new Vec3d();
        }
        dst.set(src);
        return dst;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public Vec3d inverseTransform(Vec3d src, Vec3d dst) {
        if (dst == null) {
            dst = new Vec3d();
        }
        dst.f11930x = src.f11930x - this.mxt;
        dst.f11931y = src.f11931y - this.myt;
        dst.f11932z = src.f11932z;
        return dst;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public Vec3d inverseDeltaTransform(Vec3d src, Vec3d dst) {
        if (dst == null) {
            dst = new Vec3d();
        }
        dst.set(src);
        return dst;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void transform(float[] srcPts, int srcOff, float[] dstPts, int dstOff, int numPts) {
        float tx = (float) this.mxt;
        float ty = (float) this.myt;
        if (dstPts == srcPts) {
            if (dstOff > srcOff && dstOff < srcOff + (numPts * 2)) {
                System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
                srcOff = dstOff;
            }
            if (dstOff == srcOff && tx == 0.0f && ty == 0.0f) {
                return;
            }
        }
        for (int i2 = 0; i2 < numPts; i2++) {
            int i3 = dstOff;
            int dstOff2 = dstOff + 1;
            int i4 = srcOff;
            int srcOff2 = srcOff + 1;
            dstPts[i3] = srcPts[i4] + tx;
            dstOff = dstOff2 + 1;
            srcOff = srcOff2 + 1;
            dstPts[dstOff2] = srcPts[srcOff2] + ty;
        }
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void transform(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) {
        double tx = this.mxt;
        double ty = this.myt;
        if (dstPts == srcPts) {
            if (dstOff > srcOff && dstOff < srcOff + (numPts * 2)) {
                System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
                srcOff = dstOff;
            }
            if (dstOff == srcOff && tx == 0.0d && ty == 0.0d) {
                return;
            }
        }
        for (int i2 = 0; i2 < numPts; i2++) {
            int i3 = dstOff;
            int dstOff2 = dstOff + 1;
            int i4 = srcOff;
            int srcOff2 = srcOff + 1;
            dstPts[i3] = srcPts[i4] + tx;
            dstOff = dstOff2 + 1;
            srcOff = srcOff2 + 1;
            dstPts[dstOff2] = srcPts[srcOff2] + ty;
        }
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void transform(float[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) {
        double tx = this.mxt;
        double ty = this.myt;
        for (int i2 = 0; i2 < numPts; i2++) {
            int i3 = dstOff;
            int dstOff2 = dstOff + 1;
            int i4 = srcOff;
            dstPts[i3] = srcPts[i4] + tx;
            dstOff = dstOff2 + 1;
            srcOff = srcOff + 1 + 1;
            dstPts[dstOff2] = srcPts[r9] + ty;
        }
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void transform(double[] srcPts, int srcOff, float[] dstPts, int dstOff, int numPts) {
        double tx = this.mxt;
        double ty = this.myt;
        for (int i2 = 0; i2 < numPts; i2++) {
            int i3 = dstOff;
            int dstOff2 = dstOff + 1;
            int i4 = srcOff;
            int srcOff2 = srcOff + 1;
            dstPts[i3] = (float) (srcPts[i4] + tx);
            dstOff = dstOff2 + 1;
            srcOff = srcOff2 + 1;
            dstPts[dstOff2] = (float) (srcPts[srcOff2] + ty);
        }
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void deltaTransform(float[] srcPts, int srcOff, float[] dstPts, int dstOff, int numPts) {
        if (srcPts != dstPts || srcOff != dstOff) {
            System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
        }
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void deltaTransform(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) {
        if (srcPts != dstPts || srcOff != dstOff) {
            System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
        }
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void inverseTransform(float[] srcPts, int srcOff, float[] dstPts, int dstOff, int numPts) {
        float tx = (float) this.mxt;
        float ty = (float) this.myt;
        if (dstPts == srcPts) {
            if (dstOff > srcOff && dstOff < srcOff + (numPts * 2)) {
                System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
                srcOff = dstOff;
            }
            if (dstOff == srcOff && tx == 0.0f && ty == 0.0f) {
                return;
            }
        }
        for (int i2 = 0; i2 < numPts; i2++) {
            int i3 = dstOff;
            int dstOff2 = dstOff + 1;
            int i4 = srcOff;
            int srcOff2 = srcOff + 1;
            dstPts[i3] = srcPts[i4] - tx;
            dstOff = dstOff2 + 1;
            srcOff = srcOff2 + 1;
            dstPts[dstOff2] = srcPts[srcOff2] - ty;
        }
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void inverseDeltaTransform(float[] srcPts, int srcOff, float[] dstPts, int dstOff, int numPts) {
        if (srcPts != dstPts || srcOff != dstOff) {
            System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
        }
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void inverseTransform(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) {
        double tx = this.mxt;
        double ty = this.myt;
        if (dstPts == srcPts) {
            if (dstOff > srcOff && dstOff < srcOff + (numPts * 2)) {
                System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
                srcOff = dstOff;
            }
            if (dstOff == srcOff && tx == 0.0d && ty == 0.0d) {
                return;
            }
        }
        for (int i2 = 0; i2 < numPts; i2++) {
            int i3 = dstOff;
            int dstOff2 = dstOff + 1;
            int i4 = srcOff;
            int srcOff2 = srcOff + 1;
            dstPts[i3] = srcPts[i4] - tx;
            dstOff = dstOff2 + 1;
            srcOff = srcOff2 + 1;
            dstPts[dstOff2] = srcPts[srcOff2] - ty;
        }
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseBounds transform(BaseBounds bounds, BaseBounds result) {
        float minX = (float) (bounds.getMinX() + this.mxt);
        float minY = (float) (bounds.getMinY() + this.myt);
        float minZ = bounds.getMinZ();
        float maxX = (float) (bounds.getMaxX() + this.mxt);
        float maxY = (float) (bounds.getMaxY() + this.myt);
        float maxZ = bounds.getMaxZ();
        return result.deriveWithNewBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void transform(Rectangle rect, Rectangle result) {
        transform(rect, result, this.mxt, this.myt);
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseBounds inverseTransform(BaseBounds bounds, BaseBounds result) {
        float minX = (float) (bounds.getMinX() - this.mxt);
        float minY = (float) (bounds.getMinY() - this.myt);
        float minZ = bounds.getMinZ();
        float maxX = (float) (bounds.getMaxX() - this.mxt);
        float maxY = (float) (bounds.getMaxY() - this.myt);
        float maxZ = bounds.getMaxZ();
        return result.deriveWithNewBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void inverseTransform(Rectangle rect, Rectangle result) {
        transform(rect, result, -this.mxt, -this.myt);
    }

    static void transform(Rectangle rect, Rectangle result, double mxt, double myt) {
        int imxt = (int) mxt;
        int imyt = (int) myt;
        if (imxt == mxt && imyt == myt) {
            result.setBounds(rect);
            result.translate(imxt, imyt);
            return;
        }
        double x1 = rect.f11913x + mxt;
        double y1 = rect.f11914y + myt;
        double x2 = Math.ceil(x1 + rect.width);
        double y2 = Math.ceil(y1 + rect.height);
        double x12 = Math.floor(x1);
        double y12 = Math.floor(y1);
        result.setBounds((int) x12, (int) y12, (int) (x2 - x12), (int) (y2 - y12));
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public Shape createTransformedShape(Shape s2) {
        return new Path2D(s2, this);
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void setToIdentity() {
        this.myt = 0.0d;
        this.mxt = 0.0d;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void setTransform(BaseTransform xform) {
        if (!xform.isTranslateOrIdentity()) {
            degreeError(BaseTransform.Degree.TRANSLATE_2D);
        }
        this.mxt = xform.getMxt();
        this.myt = xform.getMyt();
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void invert() {
        this.mxt = -this.mxt;
        this.myt = -this.myt;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void restoreTransform(double mxx, double myx, double mxy, double myy, double mxt, double myt) {
        if (mxx != 1.0d || myx != 0.0d || mxy != 0.0d || myy != 1.0d) {
            degreeError(BaseTransform.Degree.TRANSLATE_2D);
        }
        this.mxt = mxt;
        this.myt = myt;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void restoreTransform(double mxx, double mxy, double mxz, double mxt, double myx, double myy, double myz, double myt, double mzx, double mzy, double mzz, double mzt) {
        if (mxx != 1.0d || mxy != 0.0d || mxz != 0.0d || myx != 0.0d || myy != 1.0d || myz != 0.0d || mzx != 0.0d || mzy != 0.0d || mzz != 1.0d || mzt != 0.0d) {
            degreeError(BaseTransform.Degree.TRANSLATE_2D);
        }
        this.mxt = mxt;
        this.myt = myt;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform deriveWithTranslation(double mxt, double myt) {
        this.mxt += mxt;
        this.myt += myt;
        return this;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform deriveWithTranslation(double mxt, double myt, double mzt) {
        if (mzt == 0.0d) {
            this.mxt += mxt;
            this.myt += myt;
            return this;
        }
        Affine3D a2 = new Affine3D();
        a2.translate(this.mxt + mxt, this.myt + myt, mzt);
        return a2;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform deriveWithScale(double mxx, double myy, double mzz) {
        if (mzz != 1.0d) {
            Affine3D a2 = new Affine3D();
            a2.translate(this.mxt, this.myt);
            a2.scale(mxx, myy, mzz);
            return a2;
        }
        if (mxx == 1.0d && myy == 1.0d) {
            return this;
        }
        Affine2D a3 = new Affine2D();
        a3.translate(this.mxt, this.myt);
        a3.scale(mxx, myy);
        return a3;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform deriveWithRotation(double theta, double axisX, double axisY, double axisZ) {
        if (theta == 0.0d) {
            return this;
        }
        if (!almostZero(axisX) || !almostZero(axisY)) {
            Affine3D a2 = new Affine3D();
            a2.translate(this.mxt, this.myt);
            a2.rotate(theta, axisX, axisY, axisZ);
            return a2;
        }
        if (axisZ == 0.0d) {
            return this;
        }
        Affine2D a3 = new Affine2D();
        a3.translate(this.mxt, this.myt);
        if (axisZ > 0.0d) {
            a3.rotate(theta);
        } else if (axisZ < 0.0d) {
            a3.rotate(-theta);
        }
        return a3;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform deriveWithPreTranslation(double mxt, double myt) {
        this.mxt += mxt;
        this.myt += myt;
        return this;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform deriveWithConcatenation(double mxx, double myx, double mxy, double myy, double mxt, double myt) {
        if (mxx == 1.0d && myx == 0.0d && mxy == 0.0d && myy == 1.0d) {
            this.mxt += mxt;
            this.myt += myt;
            return this;
        }
        return new Affine2D(mxx, myx, mxy, myy, this.mxt + mxt, this.myt + myt);
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform deriveWithConcatenation(double mxx, double mxy, double mxz, double mxt, double myx, double myy, double myz, double myt, double mzx, double mzy, double mzz, double mzt) {
        if (mxz == 0.0d && myz == 0.0d && mzx == 0.0d && mzy == 0.0d && mzz == 1.0d && mzt == 0.0d) {
            return deriveWithConcatenation(mxx, myx, mxy, myy, mxt, myt);
        }
        return new Affine3D(mxx, mxy, mxz, mxt + this.mxt, myx, myy, myz, myt + this.myt, mzx, mzy, mzz, mzt);
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform deriveWithConcatenation(BaseTransform tx) {
        if (tx.isTranslateOrIdentity()) {
            this.mxt += tx.getMxt();
            this.myt += tx.getMyt();
            return this;
        }
        if (tx.is2D()) {
            return getInstance(tx.getMxx(), tx.getMyx(), tx.getMxy(), tx.getMyy(), this.mxt + tx.getMxt(), this.myt + tx.getMyt());
        }
        Affine3D t3d = new Affine3D(tx);
        t3d.preTranslate(this.mxt, this.myt, 0.0d);
        return t3d;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform deriveWithPreConcatenation(BaseTransform tx) {
        if (tx.isTranslateOrIdentity()) {
            this.mxt += tx.getMxt();
            this.myt += tx.getMyt();
            return this;
        }
        if (tx.is2D()) {
            Affine2D t2d = new Affine2D(tx);
            t2d.translate(this.mxt, this.myt);
            return t2d;
        }
        Affine3D t3d = new Affine3D(tx);
        t3d.translate(this.mxt, this.myt, 0.0d);
        return t3d;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform deriveWithNewTransform(BaseTransform tx) {
        if (tx.isTranslateOrIdentity()) {
            this.mxt = tx.getMxt();
            this.myt = tx.getMyt();
            return this;
        }
        return getInstance(tx);
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform createInverse() {
        if (isIdentity()) {
            return IDENTITY_TRANSFORM;
        }
        return new Translate2D(-this.mxt, -this.myt);
    }

    private static double _matround(double matval) {
        return Math.rint(matval * 1.0E15d) / 1.0E15d;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public String toString() {
        return "Translate2D[" + _matround(this.mxt) + ", " + _matround(this.myt) + "]";
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform copy() {
        return new Translate2D(this.mxt, this.myt);
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public boolean equals(Object obj) {
        if (obj instanceof BaseTransform) {
            BaseTransform tx = (BaseTransform) obj;
            return tx.isTranslateOrIdentity() && tx.getMxt() == this.mxt && tx.getMyt() == this.myt;
        }
        return false;
    }

    static {
        long bits = (0 * 31) + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMzz());
        BASE_HASH = (((((((((((((((((bits * 31) + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMzy())) * 31) + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMzx())) * 31) + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMyz())) * 31) + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMxz())) * 31) + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMyy())) * 31) + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMyx())) * 31) + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMxy())) * 31) + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMxx())) * 31) + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMzt());
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public int hashCode() {
        if (isIdentity()) {
            return 0;
        }
        long bits = BASE_HASH;
        long bits2 = (((bits * 31) + Double.doubleToLongBits(getMyt())) * 31) + Double.doubleToLongBits(getMxt());
        return ((int) bits2) ^ ((int) (bits2 >> 32));
    }
}
