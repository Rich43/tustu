package com.sun.javafx.geom.transform;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.Vec3d;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/transform/BaseTransform.class */
public abstract class BaseTransform implements CanTransformVec3d {
    public static final BaseTransform IDENTITY_TRANSFORM = new Identity();
    protected static final int TYPE_UNKNOWN = -1;
    public static final int TYPE_IDENTITY = 0;
    public static final int TYPE_TRANSLATION = 1;
    public static final int TYPE_UNIFORM_SCALE = 2;
    public static final int TYPE_GENERAL_SCALE = 4;
    public static final int TYPE_MASK_SCALE = 6;
    public static final int TYPE_FLIP = 64;
    public static final int TYPE_QUADRANT_ROTATION = 8;
    public static final int TYPE_GENERAL_ROTATION = 16;
    public static final int TYPE_MASK_ROTATION = 24;
    public static final int TYPE_GENERAL_TRANSFORM = 32;
    public static final int TYPE_AFFINE2D_MASK = 127;
    public static final int TYPE_AFFINE_3D = 128;
    static final double EPSILON_ABSOLUTE = 1.0E-5d;

    /* loaded from: jfxrt.jar:com/sun/javafx/geom/transform/BaseTransform$Degree.class */
    public enum Degree {
        IDENTITY,
        TRANSLATE_2D,
        AFFINE_2D,
        TRANSLATE_3D,
        AFFINE_3D
    }

    public abstract Degree getDegree();

    public abstract int getType();

    public abstract boolean isIdentity();

    public abstract boolean isTranslateOrIdentity();

    public abstract boolean is2D();

    public abstract double getDeterminant();

    public abstract Point2D transform(Point2D point2D, Point2D point2D2);

    public abstract Point2D inverseTransform(Point2D point2D, Point2D point2D2) throws NoninvertibleTransformException;

    @Override // com.sun.javafx.geom.transform.CanTransformVec3d
    public abstract Vec3d transform(Vec3d vec3d, Vec3d vec3d2);

    public abstract Vec3d deltaTransform(Vec3d vec3d, Vec3d vec3d2);

    public abstract Vec3d inverseTransform(Vec3d vec3d, Vec3d vec3d2) throws NoninvertibleTransformException;

    public abstract Vec3d inverseDeltaTransform(Vec3d vec3d, Vec3d vec3d2) throws NoninvertibleTransformException;

    public abstract void transform(float[] fArr, int i2, float[] fArr2, int i3, int i4);

    public abstract void transform(double[] dArr, int i2, double[] dArr2, int i3, int i4);

    public abstract void transform(float[] fArr, int i2, double[] dArr, int i3, int i4);

    public abstract void transform(double[] dArr, int i2, float[] fArr, int i3, int i4);

    public abstract void deltaTransform(float[] fArr, int i2, float[] fArr2, int i3, int i4);

    public abstract void deltaTransform(double[] dArr, int i2, double[] dArr2, int i3, int i4);

    public abstract void inverseTransform(float[] fArr, int i2, float[] fArr2, int i3, int i4) throws NoninvertibleTransformException;

    public abstract void inverseDeltaTransform(float[] fArr, int i2, float[] fArr2, int i3, int i4) throws NoninvertibleTransformException;

    public abstract void inverseTransform(double[] dArr, int i2, double[] dArr2, int i3, int i4) throws NoninvertibleTransformException;

    public abstract BaseBounds transform(BaseBounds baseBounds, BaseBounds baseBounds2);

    public abstract void transform(Rectangle rectangle, Rectangle rectangle2);

    public abstract BaseBounds inverseTransform(BaseBounds baseBounds, BaseBounds baseBounds2) throws NoninvertibleTransformException;

    public abstract void inverseTransform(Rectangle rectangle, Rectangle rectangle2) throws NoninvertibleTransformException;

    public abstract Shape createTransformedShape(Shape shape);

    public abstract void setToIdentity();

    public abstract void setTransform(BaseTransform baseTransform);

    public abstract void invert() throws NoninvertibleTransformException;

    public abstract void restoreTransform(double d2, double d3, double d4, double d5, double d6, double d7);

    public abstract void restoreTransform(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13);

    public abstract BaseTransform deriveWithTranslation(double d2, double d3);

    public abstract BaseTransform deriveWithTranslation(double d2, double d3, double d4);

    public abstract BaseTransform deriveWithScale(double d2, double d3, double d4);

    public abstract BaseTransform deriveWithRotation(double d2, double d3, double d4, double d5);

    public abstract BaseTransform deriveWithPreTranslation(double d2, double d3);

    public abstract BaseTransform deriveWithConcatenation(double d2, double d3, double d4, double d5, double d6, double d7);

    public abstract BaseTransform deriveWithConcatenation(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13);

    public abstract BaseTransform deriveWithPreConcatenation(BaseTransform baseTransform);

    public abstract BaseTransform deriveWithConcatenation(BaseTransform baseTransform);

    public abstract BaseTransform deriveWithNewTransform(BaseTransform baseTransform);

    public abstract BaseTransform createInverse() throws NoninvertibleTransformException;

    public abstract BaseTransform copy();

    static void degreeError(Degree maxSupported) {
        throw new InternalError("does not support higher than " + ((Object) maxSupported) + " operations");
    }

    public static BaseTransform getInstance(BaseTransform tx) {
        if (tx.isIdentity()) {
            return IDENTITY_TRANSFORM;
        }
        if (tx.isTranslateOrIdentity()) {
            return new Translate2D(tx);
        }
        if (tx.is2D()) {
            return new Affine2D(tx);
        }
        return new Affine3D(tx);
    }

    public static BaseTransform getInstance(double mxx, double mxy, double mxz, double mxt, double myx, double myy, double myz, double myt, double mzx, double mzy, double mzz, double mzt) {
        if (mxz == 0.0d && myz == 0.0d && mzx == 0.0d && mzy == 0.0d && mzz == 1.0d && mzt == 0.0d) {
            return getInstance(mxx, myx, mxy, myy, mxt, myt);
        }
        return new Affine3D(mxx, mxy, mxz, mxt, myx, myy, myz, myt, mzx, mzy, mzz, mzt);
    }

    public static BaseTransform getInstance(double mxx, double myx, double mxy, double myy, double mxt, double myt) {
        if (mxx == 1.0d && myx == 0.0d && mxy == 0.0d && myy == 1.0d) {
            return getTranslateInstance(mxt, myt);
        }
        return new Affine2D(mxx, myx, mxy, myy, mxt, myt);
    }

    public static BaseTransform getTranslateInstance(double mxt, double myt) {
        if (mxt == 0.0d && myt == 0.0d) {
            return IDENTITY_TRANSFORM;
        }
        return new Translate2D(mxt, myt);
    }

    public static BaseTransform getScaleInstance(double mxx, double myy) {
        return getInstance(mxx, 0.0d, 0.0d, myy, 0.0d, 0.0d);
    }

    public static BaseTransform getRotateInstance(double theta, double x2, double y2) {
        Affine2D a2 = new Affine2D();
        a2.setToRotation(theta, x2, y2);
        return a2;
    }

    public double getMxx() {
        return 1.0d;
    }

    public double getMxy() {
        return 0.0d;
    }

    public double getMxz() {
        return 0.0d;
    }

    public double getMxt() {
        return 0.0d;
    }

    public double getMyx() {
        return 0.0d;
    }

    public double getMyy() {
        return 1.0d;
    }

    public double getMyz() {
        return 0.0d;
    }

    public double getMyt() {
        return 0.0d;
    }

    public double getMzx() {
        return 0.0d;
    }

    public double getMzy() {
        return 0.0d;
    }

    public double getMzz() {
        return 1.0d;
    }

    public double getMzt() {
        return 0.0d;
    }

    public int hashCode() {
        if (isIdentity()) {
            return 0;
        }
        long bits = (0 * 31) + Double.doubleToLongBits(getMzz());
        long bits2 = (((((((((((((((((((((bits * 31) + Double.doubleToLongBits(getMzy())) * 31) + Double.doubleToLongBits(getMzx())) * 31) + Double.doubleToLongBits(getMyz())) * 31) + Double.doubleToLongBits(getMxz())) * 31) + Double.doubleToLongBits(getMyy())) * 31) + Double.doubleToLongBits(getMyx())) * 31) + Double.doubleToLongBits(getMxy())) * 31) + Double.doubleToLongBits(getMxx())) * 31) + Double.doubleToLongBits(getMzt())) * 31) + Double.doubleToLongBits(getMyt())) * 31) + Double.doubleToLongBits(getMxt());
        return ((int) bits2) ^ ((int) (bits2 >> 32));
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof BaseTransform)) {
            return false;
        }
        BaseTransform a2 = (BaseTransform) obj;
        return getMxx() == a2.getMxx() && getMxy() == a2.getMxy() && getMxz() == a2.getMxz() && getMxt() == a2.getMxt() && getMyx() == a2.getMyx() && getMyy() == a2.getMyy() && getMyz() == a2.getMyz() && getMyt() == a2.getMyt() && getMzx() == a2.getMzx() && getMzy() == a2.getMzy() && getMzz() == a2.getMzz() && getMzt() == a2.getMzt();
    }

    static Point2D makePoint(Point2D src, Point2D dst) {
        if (dst == null) {
            dst = new Point2D();
        }
        return dst;
    }

    public static boolean almostZero(double a2) {
        return a2 < EPSILON_ABSOLUTE && a2 > -1.0E-5d;
    }

    public String toString() {
        return "Matrix: degree " + ((Object) getDegree()) + "\n" + getMxx() + ", " + getMxy() + ", " + getMxz() + ", " + getMxt() + "\n" + getMyx() + ", " + getMyy() + ", " + getMyz() + ", " + getMyt() + "\n" + getMzx() + ", " + getMzy() + ", " + getMzz() + ", " + getMzt() + "\n";
    }
}
