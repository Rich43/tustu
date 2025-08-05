package com.sun.javafx.geom.transform;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.BaseTransform;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/transform/Identity.class */
public final class Identity extends BaseTransform {
    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform.Degree getDegree() {
        return BaseTransform.Degree.IDENTITY;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public int getType() {
        return 0;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public boolean isIdentity() {
        return true;
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
    public double getDeterminant() {
        return 1.0d;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public Point2D transform(Point2D src, Point2D dst) {
        if (dst == null) {
            dst = makePoint(src, dst);
        }
        dst.setLocation(src);
        return dst;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public Point2D inverseTransform(Point2D src, Point2D dst) {
        if (dst == null) {
            dst = makePoint(src, dst);
        }
        dst.setLocation(src);
        return dst;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform, com.sun.javafx.geom.transform.CanTransformVec3d
    public Vec3d transform(Vec3d src, Vec3d dst) {
        if (dst == null) {
            return new Vec3d(src);
        }
        dst.set(src);
        return dst;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public Vec3d deltaTransform(Vec3d src, Vec3d dst) {
        if (dst == null) {
            return new Vec3d(src);
        }
        dst.set(src);
        return dst;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public Vec3d inverseTransform(Vec3d src, Vec3d dst) {
        if (dst == null) {
            return new Vec3d(src);
        }
        dst.set(src);
        return dst;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public Vec3d inverseDeltaTransform(Vec3d src, Vec3d dst) {
        if (dst == null) {
            return new Vec3d(src);
        }
        dst.set(src);
        return dst;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void transform(float[] srcPts, int srcOff, float[] dstPts, int dstOff, int numPts) {
        if (srcPts != dstPts || srcOff != dstOff) {
            System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
        }
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void transform(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) {
        if (srcPts != dstPts || srcOff != dstOff) {
            System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
        }
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void transform(float[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) {
        for (int i2 = 0; i2 < numPts; i2++) {
            int i3 = dstOff;
            int dstOff2 = dstOff + 1;
            int i4 = srcOff;
            dstPts[i3] = srcPts[i4];
            dstOff = dstOff2 + 1;
            srcOff = srcOff + 1 + 1;
            dstPts[dstOff2] = srcPts[r7];
        }
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void transform(double[] srcPts, int srcOff, float[] dstPts, int dstOff, int numPts) {
        for (int i2 = 0; i2 < numPts; i2++) {
            int i3 = dstOff;
            int dstOff2 = dstOff + 1;
            int i4 = srcOff;
            int srcOff2 = srcOff + 1;
            dstPts[i3] = (float) srcPts[i4];
            dstOff = dstOff2 + 1;
            srcOff = srcOff2 + 1;
            dstPts[dstOff2] = (float) srcPts[srcOff2];
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
        if (srcPts != dstPts || srcOff != dstOff) {
            System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
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
        if (srcPts != dstPts || srcOff != dstOff) {
            System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
        }
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseBounds transform(BaseBounds bounds, BaseBounds result) {
        if (result != bounds) {
            result = result.deriveWithNewBounds(bounds);
        }
        return result;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void transform(Rectangle rect, Rectangle result) {
        if (result != rect) {
            result.setBounds(rect);
        }
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseBounds inverseTransform(BaseBounds bounds, BaseBounds result) {
        if (result != bounds) {
            result = result.deriveWithNewBounds(bounds);
        }
        return result;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void inverseTransform(Rectangle rect, Rectangle result) {
        if (result != rect) {
            result.setBounds(rect);
        }
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public Shape createTransformedShape(Shape s2) {
        return new Path2D(s2);
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void setToIdentity() {
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void setTransform(BaseTransform xform) {
        if (!xform.isIdentity()) {
            degreeError(BaseTransform.Degree.IDENTITY);
        }
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void invert() {
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void restoreTransform(double mxx, double myx, double mxy, double myy, double mxt, double myt) {
        if (mxx != 1.0d || myx != 0.0d || mxy != 0.0d || myy != 1.0d || mxt != 0.0d || myt != 0.0d) {
            degreeError(BaseTransform.Degree.IDENTITY);
        }
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void restoreTransform(double mxx, double mxy, double mxz, double mxt, double myx, double myy, double myz, double myt, double mzx, double mzy, double mzz, double mzt) {
        if (mxx != 1.0d || mxy != 0.0d || mxz != 0.0d || mxt != 0.0d || myx != 0.0d || myy != 1.0d || myz != 0.0d || myt != 0.0d || mzx != 0.0d || mzy != 0.0d || mzz != 1.0d || mzt != 0.0d) {
            degreeError(BaseTransform.Degree.IDENTITY);
        }
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform deriveWithTranslation(double mxt, double myt) {
        return Translate2D.getInstance(mxt, myt);
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform deriveWithPreTranslation(double mxt, double myt) {
        return Translate2D.getInstance(mxt, myt);
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform deriveWithTranslation(double mxt, double myt, double mzt) {
        if (mzt != 0.0d) {
            Affine3D a2 = new Affine3D();
            a2.translate(mxt, myt, mzt);
            return a2;
        }
        if (mxt == 0.0d && myt == 0.0d) {
            return this;
        }
        return new Translate2D(mxt, myt);
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform deriveWithScale(double mxx, double myy, double mzz) {
        if (mzz != 1.0d) {
            Affine3D a2 = new Affine3D();
            a2.scale(mxx, myy, mzz);
            return a2;
        }
        if (mxx == 1.0d && myy == 1.0d) {
            return this;
        }
        Affine2D a3 = new Affine2D();
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
            a2.rotate(theta, axisX, axisY, axisZ);
            return a2;
        }
        if (axisZ == 0.0d) {
            return this;
        }
        Affine2D a3 = new Affine2D();
        if (axisZ > 0.0d) {
            a3.rotate(theta);
        } else if (axisZ < 0.0d) {
            a3.rotate(-theta);
        }
        return a3;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform deriveWithConcatenation(double mxx, double myx, double mxy, double myy, double mxt, double myt) {
        return getInstance(mxx, myx, mxy, myy, mxt, myt);
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform deriveWithConcatenation(double mxx, double mxy, double mxz, double mxt, double myx, double myy, double myz, double myt, double mzx, double mzy, double mzz, double mzt) {
        return getInstance(mxx, mxy, mxz, mxt, myx, myy, myz, myt, mzx, mzy, mzz, mzt);
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform deriveWithConcatenation(BaseTransform tx) {
        return getInstance(tx);
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform deriveWithPreConcatenation(BaseTransform tx) {
        return getInstance(tx);
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform deriveWithNewTransform(BaseTransform tx) {
        return getInstance(tx);
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform createInverse() {
        return this;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public String toString() {
        return "Identity[]";
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform copy() {
        return this;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public boolean equals(Object obj) {
        return (obj instanceof BaseTransform) && ((BaseTransform) obj).isIdentity();
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public int hashCode() {
        return 0;
    }
}
