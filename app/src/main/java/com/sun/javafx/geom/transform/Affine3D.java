package com.sun.javafx.geom.transform;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.BaseTransform;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/transform/Affine3D.class */
public class Affine3D extends AffineBase {
    private double mxz;
    private double myz;
    private double mzx;
    private double mzy;
    private double mzz;
    private double mzt;

    /* JADX WARN: Multi-variable type inference failed */
    public Affine3D() {
        this.mzz = 1.0d;
        this.myy = 1.0d;
        4607182418800017408.mxx = this;
    }

    public Affine3D(BaseTransform transform) {
        setTransform(transform);
    }

    public Affine3D(double mxx, double mxy, double mxz, double mxt, double myx, double myy, double myz, double myt, double mzx, double mzy, double mzz, double mzt) {
        this.mxx = mxx;
        this.mxy = mxy;
        this.mxz = mxz;
        this.mxt = mxt;
        this.myx = myx;
        this.myy = myy;
        this.myz = myz;
        this.myt = myt;
        this.mzx = mzx;
        this.mzy = mzy;
        this.mzz = mzz;
        this.mzt = mzt;
        updateState();
    }

    public Affine3D(Affine3D other) {
        this.mxx = other.mxx;
        this.mxy = other.mxy;
        this.mxz = other.mxz;
        this.mxt = other.mxt;
        this.myx = other.myx;
        this.myy = other.myy;
        this.myz = other.myz;
        this.myt = other.myt;
        this.mzx = other.mzx;
        this.mzy = other.mzy;
        this.mzz = other.mzz;
        this.mzt = other.mzt;
        this.state = other.state;
        this.type = other.type;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform copy() {
        return new Affine3D(this);
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform.Degree getDegree() {
        return BaseTransform.Degree.AFFINE_3D;
    }

    @Override // com.sun.javafx.geom.transform.AffineBase
    protected void reset3Delements() {
        this.mxz = 0.0d;
        this.myz = 0.0d;
        this.mzx = 0.0d;
        this.mzy = 0.0d;
        this.mzz = 1.0d;
        this.mzt = 0.0d;
    }

    @Override // com.sun.javafx.geom.transform.AffineBase
    protected void updateState() {
        super.updateState();
        if (!almostZero(this.mxz) || !almostZero(this.myz) || !almostZero(this.mzx) || !almostZero(this.mzy) || !almostOne(this.mzz) || !almostZero(this.mzt)) {
            this.state |= 8;
            if (this.type != -1) {
                this.type |= 128;
            }
        }
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public double getMxz() {
        return this.mxz;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public double getMyz() {
        return this.myz;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public double getMzx() {
        return this.mzx;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public double getMzy() {
        return this.mzy;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public double getMzz() {
        return this.mzz;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public double getMzt() {
        return this.mzt;
    }

    @Override // com.sun.javafx.geom.transform.AffineBase, com.sun.javafx.geom.transform.BaseTransform
    public double getDeterminant() {
        if ((this.state & 8) == 0) {
            return super.getDeterminant();
        }
        return (this.mxx * ((this.myy * this.mzz) - (this.mzy * this.myz))) + (this.mxy * ((this.myz * this.mzx) - (this.mzz * this.myx))) + (this.mxz * ((this.myx * this.mzy) - (this.mzx * this.myy)));
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void setTransform(BaseTransform transform) {
        this.mxx = transform.getMxx();
        this.mxy = transform.getMxy();
        this.mxz = transform.getMxz();
        this.mxt = transform.getMxt();
        this.myx = transform.getMyx();
        this.myy = transform.getMyy();
        this.myz = transform.getMyz();
        this.myt = transform.getMyt();
        this.mzx = transform.getMzx();
        this.mzy = transform.getMzy();
        this.mzz = transform.getMzz();
        this.mzt = transform.getMzt();
        updateState();
    }

    public void setTransform(double mxx, double mxy, double mxz, double mxt, double myx, double myy, double myz, double myt, double mzx, double mzy, double mzz, double mzt) {
        this.mxx = mxx;
        this.mxy = mxy;
        this.mxz = mxz;
        this.mxt = mxt;
        this.myx = myx;
        this.myy = myy;
        this.myz = myz;
        this.myt = myt;
        this.mzx = mzx;
        this.mzy = mzy;
        this.mzz = mzz;
        this.mzt = mzt;
        updateState();
    }

    public void setToTranslation(double tx, double ty, double tz) {
        this.mxx = 1.0d;
        this.mxy = 0.0d;
        this.mxz = 0.0d;
        this.mxt = tx;
        this.myx = 0.0d;
        this.myy = 1.0d;
        this.myz = 0.0d;
        this.myt = ty;
        this.mzx = 0.0d;
        this.mzy = 0.0d;
        this.mzz = 1.0d;
        this.mzt = tz;
        if (tz == 0.0d) {
            if (tx == 0.0d && ty == 0.0d) {
                this.state = 0;
                this.type = 0;
                return;
            } else {
                this.state = 1;
                this.type = 1;
                return;
            }
        }
        if (tx == 0.0d && ty == 0.0d) {
            this.state = 8;
            this.type = 128;
        } else {
            this.state = 9;
            this.type = 129;
        }
    }

    public void setToScale(double sx, double sy, double sz) {
        this.mxx = sx;
        this.mxy = 0.0d;
        this.mxz = 0.0d;
        this.mxt = 0.0d;
        this.myx = 0.0d;
        this.myy = sy;
        this.myz = 0.0d;
        this.myt = 0.0d;
        this.mzx = 0.0d;
        this.mzy = 0.0d;
        this.mzz = sz;
        this.mzt = 0.0d;
        if (sz == 1.0d) {
            if (sx == 1.0d && sy == 1.0d) {
                this.state = 0;
                this.type = 0;
                return;
            } else {
                this.state = 2;
                this.type = -1;
                return;
            }
        }
        if (sx == 1.0d && sy == 1.0d) {
            this.state = 8;
            this.type = 128;
        } else {
            this.state = 10;
            this.type = -1;
        }
    }

    public void setToRotation(double theta, double axisX, double axisY, double axisZ, double pivotX, double pivotY, double pivotZ) {
        setToRotation(theta, axisX, axisY, axisZ);
        if (pivotX != 0.0d || pivotY != 0.0d || pivotZ != 0.0d) {
            preTranslate(pivotX, pivotY, pivotZ);
            translate(-pivotX, -pivotY, -pivotZ);
        }
    }

    public void setToRotation(double theta, double axisX, double axisY, double axisZ) {
        double mag = Math.sqrt((axisX * axisX) + (axisY * axisY) + (axisZ * axisZ));
        if (almostZero(mag)) {
            setToIdentity();
            return;
        }
        double mag2 = 1.0d / mag;
        double ax2 = axisX * mag2;
        double ay2 = axisY * mag2;
        double az2 = axisZ * mag2;
        double sinTheta = Math.sin(theta);
        double cosTheta = Math.cos(theta);
        double t2 = 1.0d - cosTheta;
        double xz = ax2 * az2;
        double xy = ax2 * ay2;
        double yz = ay2 * az2;
        this.mxx = (t2 * ax2 * ax2) + cosTheta;
        this.mxy = (t2 * xy) - (sinTheta * az2);
        this.mxz = (t2 * xz) + (sinTheta * ay2);
        this.mxt = 0.0d;
        this.myx = (t2 * xy) + (sinTheta * az2);
        this.myy = (t2 * ay2 * ay2) + cosTheta;
        this.myz = (t2 * yz) - (sinTheta * ax2);
        this.myt = 0.0d;
        this.mzx = (t2 * xz) - (sinTheta * ay2);
        this.mzy = (t2 * yz) + (sinTheta * ax2);
        this.mzz = (t2 * az2 * az2) + cosTheta;
        this.mzt = 0.0d;
        updateState();
    }

    @Override // com.sun.javafx.geom.transform.AffineBase, com.sun.javafx.geom.transform.BaseTransform
    public BaseBounds transform(BaseBounds src, BaseBounds dst) {
        if ((this.state & 8) == 0) {
            return super.transform(src, dst);
        }
        switch (this.state) {
            case 0:
                if (src != dst) {
                    dst = dst.deriveWithNewBounds(src);
                    break;
                }
                break;
            case 1:
                dst = dst.deriveWithNewBounds((float) (src.getMinX() + this.mxt), (float) (src.getMinY() + this.myt), (float) (src.getMinZ() + this.mzt), (float) (src.getMaxX() + this.mxt), (float) (src.getMaxY() + this.myt), (float) (src.getMaxZ() + this.mzt));
                break;
            case 2:
                dst = dst.deriveWithNewBoundsAndSort((float) (src.getMinX() * this.mxx), (float) (src.getMinY() * this.myy), (float) (src.getMinZ() * this.mzz), (float) (src.getMaxX() * this.mxx), (float) (src.getMaxY() * this.myy), (float) (src.getMaxZ() * this.mzz));
                break;
            case 3:
                dst = dst.deriveWithNewBoundsAndSort((float) ((src.getMinX() * this.mxx) + this.mxt), (float) ((src.getMinY() * this.myy) + this.myt), (float) ((src.getMinZ() * this.mzz) + this.mzt), (float) ((src.getMaxX() * this.mxx) + this.mxt), (float) ((src.getMaxY() * this.myy) + this.myt), (float) ((src.getMaxZ() * this.mzz) + this.mzt));
                break;
            case 4:
            case 5:
            case 6:
            case 7:
            default:
                Vec3d tempV3d = new Vec3d();
                dst = TransformHelper.general3dBoundsTransform(this, src, dst, tempV3d);
                break;
        }
        return dst;
    }

    @Override // com.sun.javafx.geom.transform.AffineBase, com.sun.javafx.geom.transform.BaseTransform, com.sun.javafx.geom.transform.CanTransformVec3d
    public Vec3d transform(Vec3d src, Vec3d dst) {
        if ((this.state & 8) == 0) {
            return super.transform(src, dst);
        }
        if (dst == null) {
            dst = new Vec3d();
        }
        double x2 = src.f11930x;
        double y2 = src.f11931y;
        double z2 = src.f11932z;
        dst.f11930x = (this.mxx * x2) + (this.mxy * y2) + (this.mxz * z2) + this.mxt;
        dst.f11931y = (this.myx * x2) + (this.myy * y2) + (this.myz * z2) + this.myt;
        dst.f11932z = (this.mzx * x2) + (this.mzy * y2) + (this.mzz * z2) + this.mzt;
        return dst;
    }

    @Override // com.sun.javafx.geom.transform.AffineBase, com.sun.javafx.geom.transform.BaseTransform
    public Vec3d deltaTransform(Vec3d src, Vec3d dst) {
        if ((this.state & 8) == 0) {
            return super.deltaTransform(src, dst);
        }
        if (dst == null) {
            dst = new Vec3d();
        }
        double x2 = src.f11930x;
        double y2 = src.f11931y;
        double z2 = src.f11932z;
        dst.f11930x = (this.mxx * x2) + (this.mxy * y2) + (this.mxz * z2);
        dst.f11931y = (this.myx * x2) + (this.myy * y2) + (this.myz * z2);
        dst.f11932z = (this.mzx * x2) + (this.mzy * y2) + (this.mzz * z2);
        return dst;
    }

    @Override // com.sun.javafx.geom.transform.AffineBase, com.sun.javafx.geom.transform.BaseTransform
    public void inverseTransform(float[] srcPts, int srcOff, float[] dstPts, int dstOff, int numPts) throws NoninvertibleTransformException {
        if ((this.state & 8) == 0) {
            super.inverseTransform(srcPts, srcOff, dstPts, dstOff, numPts);
        } else {
            createInverse().transform(srcPts, srcOff, dstPts, dstOff, numPts);
        }
    }

    @Override // com.sun.javafx.geom.transform.AffineBase, com.sun.javafx.geom.transform.BaseTransform
    public void inverseDeltaTransform(float[] srcPts, int srcOff, float[] dstPts, int dstOff, int numPts) throws NoninvertibleTransformException {
        if ((this.state & 8) == 0) {
            super.inverseDeltaTransform(srcPts, srcOff, dstPts, dstOff, numPts);
        } else {
            createInverse().deltaTransform(srcPts, srcOff, dstPts, dstOff, numPts);
        }
    }

    @Override // com.sun.javafx.geom.transform.AffineBase, com.sun.javafx.geom.transform.BaseTransform
    public void inverseTransform(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) throws NoninvertibleTransformException {
        if ((this.state & 8) == 0) {
            super.inverseTransform(srcPts, srcOff, dstPts, dstOff, numPts);
        } else {
            createInverse().transform(srcPts, srcOff, dstPts, dstOff, numPts);
        }
    }

    @Override // com.sun.javafx.geom.transform.AffineBase, com.sun.javafx.geom.transform.BaseTransform
    public Point2D inverseTransform(Point2D src, Point2D dst) throws NoninvertibleTransformException {
        if ((this.state & 8) == 0) {
            return super.inverseTransform(src, dst);
        }
        return createInverse().transform(src, dst);
    }

    @Override // com.sun.javafx.geom.transform.AffineBase, com.sun.javafx.geom.transform.BaseTransform
    public Vec3d inverseTransform(Vec3d src, Vec3d dst) throws NoninvertibleTransformException {
        if ((this.state & 8) == 0) {
            return super.inverseTransform(src, dst);
        }
        return createInverse().transform(src, dst);
    }

    @Override // com.sun.javafx.geom.transform.AffineBase, com.sun.javafx.geom.transform.BaseTransform
    public Vec3d inverseDeltaTransform(Vec3d src, Vec3d dst) throws NoninvertibleTransformException {
        if ((this.state & 8) == 0) {
            return super.inverseDeltaTransform(src, dst);
        }
        return createInverse().deltaTransform(src, dst);
    }

    @Override // com.sun.javafx.geom.transform.AffineBase, com.sun.javafx.geom.transform.BaseTransform
    public BaseBounds inverseTransform(BaseBounds bounds, BaseBounds result) throws NoninvertibleTransformException {
        BaseBounds result2;
        if ((this.state & 8) == 0) {
            result2 = super.inverseTransform(bounds, result);
        } else {
            result2 = createInverse().transform(bounds, result);
        }
        return result2;
    }

    @Override // com.sun.javafx.geom.transform.AffineBase, com.sun.javafx.geom.transform.BaseTransform
    public void inverseTransform(Rectangle bounds, Rectangle result) throws NoninvertibleTransformException {
        if ((this.state & 8) == 0) {
            super.inverseTransform(bounds, result);
        } else {
            createInverse().transform(bounds, result);
        }
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform createInverse() throws NoninvertibleTransformException {
        BaseTransform t2 = copy();
        t2.invert();
        return t2;
    }

    @Override // com.sun.javafx.geom.transform.AffineBase, com.sun.javafx.geom.transform.BaseTransform
    public void invert() throws NoninvertibleTransformException {
        if ((this.state & 8) == 0) {
            super.invert();
            return;
        }
        double cxx = minor(0, 0);
        double cyx = -minor(0, 1);
        double czx = minor(0, 2);
        double cxy = -minor(1, 0);
        double cyy = minor(1, 1);
        double czy = -minor(1, 2);
        double cxz = minor(2, 0);
        double cyz = -minor(2, 1);
        double czz = minor(2, 2);
        double cxt = -minor(3, 0);
        double cyt = minor(3, 1);
        double czt = -minor(3, 2);
        double det = getDeterminant();
        this.mxx = cxx / det;
        this.mxy = cxy / det;
        this.mxz = cxz / det;
        this.mxt = cxt / det;
        this.myx = cyx / det;
        this.myy = cyy / det;
        this.myz = cyz / det;
        this.myt = cyt / det;
        this.mzx = czx / det;
        this.mzy = czy / det;
        this.mzz = czz / det;
        this.mzt = czt / det;
        updateState();
    }

    private double minor(int row, int col) {
        double m00 = this.mxx;
        double m01 = this.mxy;
        double m02 = this.mxz;
        double m10 = this.myx;
        double m11 = this.myy;
        double m12 = this.myz;
        double m20 = this.mzx;
        double m21 = this.mzy;
        double m22 = this.mzz;
        switch (col) {
            case 0:
                m00 = m01;
                m10 = m11;
                m20 = m21;
            case 1:
                m01 = m02;
                m11 = m12;
                m21 = m22;
            case 2:
                m02 = this.mxt;
                m12 = this.myt;
                m22 = this.mzt;
                break;
        }
        switch (row) {
            case 0:
                m00 = m10;
                m01 = m11;
            case 1:
                m10 = m20;
                m11 = m21;
            case 2:
            default:
                return (m00 * m11) - (m01 * m10);
            case 3:
                return (m00 * ((m11 * m22) - (m21 * m12))) + (m01 * ((m12 * m20) - (m22 * m10))) + (m02 * ((m10 * m21) - (m20 * m11)));
        }
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public Affine3D deriveWithNewTransform(BaseTransform tx) {
        setTransform(tx);
        return this;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public Affine3D deriveWithTranslation(double tx, double ty) {
        translate(tx, ty, 0.0d);
        return this;
    }

    @Override // com.sun.javafx.geom.transform.AffineBase
    public void translate(double tx, double ty) {
        if ((this.state & 8) == 0) {
            super.translate(tx, ty);
        } else {
            translate(tx, ty, 0.0d);
        }
    }

    public void translate(double tx, double ty, double tz) {
        if ((this.state & 8) == 0) {
            super.translate(tx, ty);
            if (tz != 0.0d) {
                this.mzt = tz;
                this.state |= 8;
                if (this.type != -1) {
                    this.type |= 128;
                    return;
                }
                return;
            }
            return;
        }
        this.mxt = (tx * this.mxx) + (ty * this.mxy) + (tz * this.mxz) + this.mxt;
        this.myt = (tx * this.myx) + (ty * this.myy) + (tz * this.myz) + this.myt;
        this.mzt = (tx * this.mzx) + (ty * this.mzy) + (tz * this.mzz) + this.mzt;
        updateState();
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public Affine3D deriveWithPreTranslation(double mxt, double myt) {
        preTranslate(mxt, myt, 0.0d);
        return this;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform deriveWithTranslation(double mxt, double myt, double mzt) {
        translate(mxt, myt, mzt);
        return this;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform deriveWithScale(double mxx, double myy, double mzz) {
        scale(mxx, myy, mzz);
        return this;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform deriveWithRotation(double theta, double axisX, double axisY, double axisZ) {
        rotate(theta, axisX, axisY, axisZ);
        return this;
    }

    public void preTranslate(double mxt, double myt, double mzt) {
        this.mxt += mxt;
        this.myt += myt;
        this.mzt += mzt;
        int clearflags = 0;
        int setflags = 0;
        if (this.mzt == 0.0d) {
            if ((this.state & 8) != 0) {
                updateState();
                return;
            }
        } else {
            this.state |= 8;
            setflags = 128;
        }
        if (this.mxt == 0.0d && this.myt == 0.0d) {
            this.state &= -2;
            clearflags = 1;
        } else {
            this.state |= 1;
            setflags |= 1;
        }
        if (this.type != -1) {
            this.type = (this.type & (clearflags ^ (-1))) | setflags;
        }
    }

    @Override // com.sun.javafx.geom.transform.AffineBase
    public void scale(double sx, double sy) {
        if ((this.state & 8) == 0) {
            super.scale(sx, sy);
        } else {
            scale(sx, sy, 1.0d);
        }
    }

    public void scale(double sx, double sy, double sz) {
        if ((this.state & 8) == 0) {
            super.scale(sx, sy);
            if (sz != 1.0d) {
                this.mzz = sz;
                this.state |= 8;
                if (this.type != -1) {
                    this.type |= 128;
                    return;
                }
                return;
            }
            return;
        }
        this.mxx *= sx;
        this.mxy *= sy;
        this.mxz *= sz;
        this.myx *= sx;
        this.myy *= sy;
        this.myz *= sz;
        this.mzx *= sx;
        this.mzy *= sy;
        this.mzz *= sz;
        updateState();
    }

    @Override // com.sun.javafx.geom.transform.AffineBase
    public void rotate(double theta) {
        if ((this.state & 8) == 0) {
            super.rotate(theta);
        } else {
            rotate(theta, 0.0d, 0.0d, 1.0d);
        }
    }

    public void rotate(double theta, double axisX, double axisY, double axisZ) {
        if ((this.state & 8) == 0 && almostZero(axisX) && almostZero(axisY)) {
            if (axisZ > 0.0d) {
                super.rotate(theta);
                return;
            } else {
                if (axisZ < 0.0d) {
                    super.rotate(-theta);
                    return;
                }
                return;
            }
        }
        double mag = Math.sqrt((axisX * axisX) + (axisY * axisY) + (axisZ * axisZ));
        if (almostZero(mag)) {
            return;
        }
        double mag2 = 1.0d / mag;
        double ax2 = axisX * mag2;
        double ay2 = axisY * mag2;
        double az2 = axisZ * mag2;
        double sinTheta = Math.sin(theta);
        double cosTheta = Math.cos(theta);
        double t2 = 1.0d - cosTheta;
        double xz = ax2 * az2;
        double xy = ax2 * ay2;
        double yz = ay2 * az2;
        double Txx = (t2 * ax2 * ax2) + cosTheta;
        double Txy = (t2 * xy) - (sinTheta * az2);
        double Txz = (t2 * xz) + (sinTheta * ay2);
        double Tyx = (t2 * xy) + (sinTheta * az2);
        double Tyy = (t2 * ay2 * ay2) + cosTheta;
        double Tyz = (t2 * yz) - (sinTheta * ax2);
        double Tzx = (t2 * xz) - (sinTheta * ay2);
        double Tzy = (t2 * yz) + (sinTheta * ax2);
        double Tzz = (t2 * az2 * az2) + cosTheta;
        double rxx = (this.mxx * Txx) + (this.mxy * Tyx) + (this.mxz * Tzx);
        double rxy = (this.mxx * Txy) + (this.mxy * Tyy) + (this.mxz * Tzy);
        double rxz = (this.mxx * Txz) + (this.mxy * Tyz) + (this.mxz * Tzz);
        double ryx = (this.myx * Txx) + (this.myy * Tyx) + (this.myz * Tzx);
        double ryy = (this.myx * Txy) + (this.myy * Tyy) + (this.myz * Tzy);
        double ryz = (this.myx * Txz) + (this.myy * Tyz) + (this.myz * Tzz);
        double rzx = (this.mzx * Txx) + (this.mzy * Tyx) + (this.mzz * Tzx);
        double rzy = (this.mzx * Txy) + (this.mzy * Tyy) + (this.mzz * Tzy);
        double rzz = (this.mzx * Txz) + (this.mzy * Tyz) + (this.mzz * Tzz);
        this.mxx = rxx;
        this.mxy = rxy;
        this.mxz = rxz;
        this.myx = ryx;
        this.myy = ryy;
        this.myz = ryz;
        this.mzx = rzx;
        this.mzy = rzy;
        this.mzz = rzz;
        updateState();
    }

    @Override // com.sun.javafx.geom.transform.AffineBase
    public void shear(double shx, double shy) {
        if ((this.state & 8) == 0) {
            super.shear(shx, shy);
            return;
        }
        double rxx = this.mxx + (this.mxy * shy);
        double rxy = this.mxy + (this.mxx * shx);
        double ryx = this.myx + (this.myy * shy);
        double ryy = this.myy + (this.myx * shx);
        double rzx = this.mzx + (this.mzy * shy);
        double rzy = this.mzy + (this.mzx * shx);
        this.mxx = rxx;
        this.mxy = rxy;
        this.myx = ryx;
        this.myy = ryy;
        this.mzx = rzx;
        this.mzy = rzy;
        updateState();
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public Affine3D deriveWithConcatenation(BaseTransform transform) {
        concatenate(transform);
        return this;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public Affine3D deriveWithPreConcatenation(BaseTransform transform) {
        preConcatenate(transform);
        return this;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0055  */
    @Override // com.sun.javafx.geom.transform.AffineBase
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void concatenate(com.sun.javafx.geom.transform.BaseTransform r9) {
        /*
            Method dump skipped, instructions count: 561
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.geom.transform.Affine3D.concatenate(com.sun.javafx.geom.transform.BaseTransform):void");
    }

    public void concatenate(double Txx, double Txy, double Txz, double Txt, double Tyx, double Tyy, double Tyz, double Tyt, double Tzx, double Tzy, double Tzz, double Tzt) {
        double rxx = (this.mxx * Txx) + (this.mxy * Tyx) + (this.mxz * Tzx);
        double rxy = (this.mxx * Txy) + (this.mxy * Tyy) + (this.mxz * Tzy);
        double rxz = (this.mxx * Txz) + (this.mxy * Tyz) + (this.mxz * Tzz);
        double rxt = (this.mxx * Txt) + (this.mxy * Tyt) + (this.mxz * Tzt) + this.mxt;
        double ryx = (this.myx * Txx) + (this.myy * Tyx) + (this.myz * Tzx);
        double ryy = (this.myx * Txy) + (this.myy * Tyy) + (this.myz * Tzy);
        double ryz = (this.myx * Txz) + (this.myy * Tyz) + (this.myz * Tzz);
        double ryt = (this.myx * Txt) + (this.myy * Tyt) + (this.myz * Tzt) + this.myt;
        double rzx = (this.mzx * Txx) + (this.mzy * Tyx) + (this.mzz * Tzx);
        double rzy = (this.mzx * Txy) + (this.mzy * Tyy) + (this.mzz * Tzy);
        double rzz = (this.mzx * Txz) + (this.mzy * Tyz) + (this.mzz * Tzz);
        double rzt = (this.mzx * Txt) + (this.mzy * Tyt) + (this.mzz * Tzt) + this.mzt;
        this.mxx = rxx;
        this.mxy = rxy;
        this.mxz = rxz;
        this.mxt = rxt;
        this.myx = ryx;
        this.myy = ryy;
        this.myz = ryz;
        this.myt = ryt;
        this.mzx = rzx;
        this.mzy = rzy;
        this.mzz = rzz;
        this.mzt = rzt;
        updateState();
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public Affine3D deriveWithConcatenation(double Txx, double Tyx, double Txy, double Tyy, double Txt, double Tyt) {
        double rxx = (this.mxx * Txx) + (this.mxy * Tyx);
        double rxy = (this.mxx * Txy) + (this.mxy * Tyy);
        double rxt = (this.mxx * Txt) + (this.mxy * Tyt) + this.mxt;
        double ryx = (this.myx * Txx) + (this.myy * Tyx);
        double ryy = (this.myx * Txy) + (this.myy * Tyy);
        double ryt = (this.myx * Txt) + (this.myy * Tyt) + this.myt;
        double rzx = (this.mzx * Txx) + (this.mzy * Tyx);
        double rzy = (this.mzx * Txy) + (this.mzy * Tyy);
        double rzt = (this.mzx * Txt) + (this.mzy * Tyt) + this.mzt;
        this.mxx = rxx;
        this.mxy = rxy;
        this.mxt = rxt;
        this.myx = ryx;
        this.myy = ryy;
        this.myt = ryt;
        this.mzx = rzx;
        this.mzy = rzy;
        this.mzt = rzt;
        updateState();
        return this;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform deriveWithConcatenation(double mxx, double mxy, double mxz, double mxt, double myx, double myy, double myz, double myt, double mzx, double mzy, double mzz, double mzt) {
        concatenate(mxx, mxy, mxz, mxt, myx, myy, myz, myt, mzx, mzy, mzz, mzt);
        return this;
    }

    public void preConcatenate(BaseTransform transform) {
        switch (transform.getDegree()) {
            case IDENTITY:
                break;
            case TRANSLATE_2D:
                preTranslate(transform.getMxt(), transform.getMyt(), 0.0d);
                break;
            case TRANSLATE_3D:
                preTranslate(transform.getMxt(), transform.getMyt(), transform.getMzt());
                break;
            default:
                double Txx = transform.getMxx();
                double Txy = transform.getMxy();
                double Txz = transform.getMxz();
                double Txt = transform.getMxt();
                double Tyx = transform.getMyx();
                double Tyy = transform.getMyy();
                double Tyz = transform.getMyz();
                double Tyt = transform.getMyt();
                double Tzx = transform.getMzx();
                double Tzy = transform.getMzy();
                double Tzz = transform.getMzz();
                double Tzt = transform.getMzt();
                double rxx = (Txx * this.mxx) + (Txy * this.myx) + (Txz * this.mzx);
                double rxy = (Txx * this.mxy) + (Txy * this.myy) + (Txz * this.mzy);
                double rxz = (Txx * this.mxz) + (Txy * this.myz) + (Txz * this.mzz);
                double rxt = (Txx * this.mxt) + (Txy * this.myt) + (Txz * this.mzt) + Txt;
                double ryx = (Tyx * this.mxx) + (Tyy * this.myx) + (Tyz * this.mzx);
                double ryy = (Tyx * this.mxy) + (Tyy * this.myy) + (Tyz * this.mzy);
                double ryz = (Tyx * this.mxz) + (Tyy * this.myz) + (Tyz * this.mzz);
                double ryt = (Tyx * this.mxt) + (Tyy * this.myt) + (Tyz * this.mzt) + Tyt;
                double rzx = (Tzx * this.mxx) + (Tzy * this.myx) + (Tzz * this.mzx);
                double rzy = (Tzx * this.mxy) + (Tzy * this.myy) + (Tzz * this.mzy);
                double rzz = (Tzx * this.mxz) + (Tzy * this.myz) + (Tzz * this.mzz);
                double rzt = (Tzx * this.mxt) + (Tzy * this.myt) + (Tzz * this.mzt) + Tzt;
                this.mxx = rxx;
                this.mxy = rxy;
                this.mxz = rxz;
                this.mxt = rxt;
                this.myx = ryx;
                this.myy = ryy;
                this.myz = ryz;
                this.myt = ryt;
                this.mzx = rzx;
                this.mzy = rzy;
                this.mzz = rzz;
                this.mzt = rzt;
                updateState();
                break;
        }
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void restoreTransform(double mxx, double myx, double mxy, double myy, double mxt, double myt) {
        throw new InternalError("must use Affine3D restore method to prevent loss of information");
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void restoreTransform(double mxx, double mxy, double mxz, double mxt, double myx, double myy, double myz, double myt, double mzx, double mzy, double mzz, double mzt) {
        this.mxx = mxx;
        this.mxy = mxy;
        this.mxz = mxz;
        this.mxt = mxt;
        this.myx = myx;
        this.myy = myy;
        this.myz = myz;
        this.myt = myt;
        this.mzx = mzx;
        this.mzy = mzy;
        this.mzz = mzz;
        this.mzt = mzt;
        updateState();
    }

    public Affine3D lookAt(Vec3d eye, Vec3d center, Vec3d up) {
        double forwardx = eye.f11930x - center.f11930x;
        double forwardy = eye.f11931y - center.f11931y;
        double forwardz = eye.f11932z - center.f11932z;
        double invMag = 1.0d / Math.sqrt(((forwardx * forwardx) + (forwardy * forwardy)) + (forwardz * forwardz));
        double forwardx2 = forwardx * invMag;
        double forwardy2 = forwardy * invMag;
        double forwardz2 = forwardz * invMag;
        double invMag2 = 1.0d / Math.sqrt(((up.f11930x * up.f11930x) + (up.f11931y * up.f11931y)) + (up.f11932z * up.f11932z));
        double upx = up.f11930x * invMag2;
        double upy = up.f11931y * invMag2;
        double upz = up.f11932z * invMag2;
        double sidex = (upy * forwardz2) - (forwardy2 * upz);
        double sidey = (upz * forwardx2) - (upx * forwardz2);
        double sidez = (upx * forwardy2) - (upy * forwardx2);
        double invMag3 = 1.0d / Math.sqrt(((sidex * sidex) + (sidey * sidey)) + (sidez * sidez));
        double sidex2 = sidex * invMag3;
        double sidey2 = sidey * invMag3;
        double sidez2 = sidez * invMag3;
        double upx2 = (forwardy2 * sidez2) - (sidey2 * forwardz2);
        this.mxx = sidex2;
        this.mxy = sidey2;
        this.mxz = sidez2;
        this.myx = upx2;
        this.myy = (forwardz2 * sidex2) - (forwardx2 * sidez2);
        this.myz = (forwardx2 * sidey2) - (forwardy2 * sidex2);
        this.mzx = forwardx2;
        this.mzy = forwardy2;
        this.mzz = forwardz2;
        this.mxt = ((-eye.f11930x) * this.mxx) + ((-eye.f11931y) * this.mxy) + ((-eye.f11932z) * this.mxz);
        this.myt = ((-eye.f11930x) * this.myx) + ((-eye.f11931y) * this.myy) + ((-eye.f11932z) * this.myz);
        this.mzt = ((-eye.f11930x) * this.mzx) + ((-eye.f11931y) * this.mzy) + ((-eye.f11932z) * this.mzz);
        updateState();
        return this;
    }

    static boolean almostOne(double a2) {
        return a2 < 1.00001d && a2 > 0.99999d;
    }

    private static double _matround(double matval) {
        return Math.rint(matval * 1.0E15d) / 1.0E15d;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public String toString() {
        return "Affine3D[[" + _matround(this.mxx) + ", " + _matround(this.mxy) + ", " + _matround(this.mxz) + ", " + _matround(this.mxt) + "], [" + _matround(this.myx) + ", " + _matround(this.myy) + ", " + _matround(this.myz) + ", " + _matround(this.myt) + "], [" + _matround(this.mzx) + ", " + _matround(this.mzy) + ", " + _matround(this.mzz) + ", " + _matround(this.mzt) + "]]";
    }
}
