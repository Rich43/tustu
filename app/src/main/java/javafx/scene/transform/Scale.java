package javafx.scene.transform;

import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:javafx/scene/transform/Scale.class */
public class Scale extends Transform {

    /* renamed from: x, reason: collision with root package name */
    private DoubleProperty f12747x;

    /* renamed from: y, reason: collision with root package name */
    private DoubleProperty f12748y;

    /* renamed from: z, reason: collision with root package name */
    private DoubleProperty f12749z;
    private DoubleProperty pivotX;
    private DoubleProperty pivotY;
    private DoubleProperty pivotZ;

    public Scale() {
    }

    public Scale(double x2, double y2) {
        setX(x2);
        setY(y2);
    }

    public Scale(double x2, double y2, double pivotX, double pivotY) {
        this(x2, y2);
        setPivotX(pivotX);
        setPivotY(pivotY);
    }

    public Scale(double x2, double y2, double z2) {
        this(x2, y2);
        setZ(z2);
    }

    public Scale(double x2, double y2, double z2, double pivotX, double pivotY, double pivotZ) {
        this(x2, y2, pivotX, pivotY);
        setZ(z2);
        setPivotZ(pivotZ);
    }

    public final void setX(double value) {
        xProperty().set(value);
    }

    public final double getX() {
        if (this.f12747x == null) {
            return 1.0d;
        }
        return this.f12747x.get();
    }

    public final DoubleProperty xProperty() {
        if (this.f12747x == null) {
            this.f12747x = new DoublePropertyBase(1.0d) { // from class: javafx.scene.transform.Scale.1
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Scale.this.transformChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scale.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return LanguageTag.PRIVATEUSE;
                }
            };
        }
        return this.f12747x;
    }

    public final void setY(double value) {
        yProperty().set(value);
    }

    public final double getY() {
        if (this.f12748y == null) {
            return 1.0d;
        }
        return this.f12748y.get();
    }

    public final DoubleProperty yProperty() {
        if (this.f12748y == null) {
            this.f12748y = new DoublePropertyBase(1.0d) { // from class: javafx.scene.transform.Scale.2
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Scale.this.transformChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scale.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return PdfOps.y_TOKEN;
                }
            };
        }
        return this.f12748y;
    }

    public final void setZ(double value) {
        zProperty().set(value);
    }

    public final double getZ() {
        if (this.f12749z == null) {
            return 1.0d;
        }
        return this.f12749z.get();
    }

    public final DoubleProperty zProperty() {
        if (this.f12749z == null) {
            this.f12749z = new DoublePropertyBase(1.0d) { // from class: javafx.scene.transform.Scale.3
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Scale.this.transformChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scale.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "z";
                }
            };
        }
        return this.f12749z;
    }

    public final void setPivotX(double value) {
        pivotXProperty().set(value);
    }

    public final double getPivotX() {
        if (this.pivotX == null) {
            return 0.0d;
        }
        return this.pivotX.get();
    }

    public final DoubleProperty pivotXProperty() {
        if (this.pivotX == null) {
            this.pivotX = new DoublePropertyBase() { // from class: javafx.scene.transform.Scale.4
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Scale.this.transformChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scale.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "pivotX";
                }
            };
        }
        return this.pivotX;
    }

    public final void setPivotY(double value) {
        pivotYProperty().set(value);
    }

    public final double getPivotY() {
        if (this.pivotY == null) {
            return 0.0d;
        }
        return this.pivotY.get();
    }

    public final DoubleProperty pivotYProperty() {
        if (this.pivotY == null) {
            this.pivotY = new DoublePropertyBase() { // from class: javafx.scene.transform.Scale.5
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Scale.this.transformChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scale.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "pivotY";
                }
            };
        }
        return this.pivotY;
    }

    public final void setPivotZ(double value) {
        pivotZProperty().set(value);
    }

    public final double getPivotZ() {
        if (this.pivotZ == null) {
            return 0.0d;
        }
        return this.pivotZ.get();
    }

    public final DoubleProperty pivotZProperty() {
        if (this.pivotZ == null) {
            this.pivotZ = new DoublePropertyBase() { // from class: javafx.scene.transform.Scale.6
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Scale.this.transformChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Scale.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "pivotZ";
                }
            };
        }
        return this.pivotZ;
    }

    @Override // javafx.scene.transform.Transform
    public double getMxx() {
        return getX();
    }

    @Override // javafx.scene.transform.Transform
    public double getMyy() {
        return getY();
    }

    @Override // javafx.scene.transform.Transform
    public double getMzz() {
        return getZ();
    }

    @Override // javafx.scene.transform.Transform
    public double getTx() {
        return (1.0d - getX()) * getPivotX();
    }

    @Override // javafx.scene.transform.Transform
    public double getTy() {
        return (1.0d - getY()) * getPivotY();
    }

    @Override // javafx.scene.transform.Transform
    public double getTz() {
        return (1.0d - getZ()) * getPivotZ();
    }

    @Override // javafx.scene.transform.Transform
    boolean computeIs2D() {
        return getZ() == 1.0d;
    }

    @Override // javafx.scene.transform.Transform
    boolean computeIsIdentity() {
        return getX() == 1.0d && getY() == 1.0d && getZ() == 1.0d;
    }

    @Override // javafx.scene.transform.Transform
    void fill2DArray(double[] array) {
        double sx = getX();
        double sy = getY();
        array[0] = sx;
        array[1] = 0.0d;
        array[2] = (1.0d - sx) * getPivotX();
        array[3] = 0.0d;
        array[4] = sy;
        array[5] = (1.0d - sy) * getPivotY();
    }

    @Override // javafx.scene.transform.Transform
    void fill3DArray(double[] array) {
        double sx = getX();
        double sy = getY();
        double sz = getZ();
        array[0] = sx;
        array[1] = 0.0d;
        array[2] = 0.0d;
        array[3] = (1.0d - sx) * getPivotX();
        array[4] = 0.0d;
        array[5] = sy;
        array[6] = 0.0d;
        array[7] = (1.0d - sy) * getPivotY();
        array[8] = 0.0d;
        array[9] = 0.0d;
        array[10] = sz;
        array[11] = (1.0d - sz) * getPivotZ();
    }

    @Override // javafx.scene.transform.Transform
    public Transform createConcatenation(Transform transform) {
        double sx = getX();
        double sy = getY();
        double sz = getZ();
        if (transform instanceof Scale) {
            Scale other = (Scale) transform;
            if (other.getPivotX() == getPivotX() && other.getPivotY() == getPivotY() && other.getPivotZ() == getPivotZ()) {
                return new Scale(sx * other.getX(), sy * other.getY(), sz * other.getZ(), getPivotX(), getPivotY(), getPivotZ());
            }
        }
        if (transform instanceof Translate) {
            Translate t2 = (Translate) transform;
            double tx = t2.getX();
            double ty = t2.getY();
            double tz = t2.getZ();
            if ((tx == 0.0d || (sx != 1.0d && sx != 0.0d)) && ((ty == 0.0d || (sy != 1.0d && sy != 0.0d)) && (tz == 0.0d || (sz != 1.0d && sz != 0.0d)))) {
                return new Scale(sx, sy, sz, (sx != 1.0d ? (sx * tx) / (1.0d - sx) : 0.0d) + getPivotX(), (sy != 1.0d ? (sy * ty) / (1.0d - sy) : 0.0d) + getPivotY(), (sz != 1.0d ? (sz * tz) / (1.0d - sz) : 0.0d) + getPivotZ());
            }
        }
        if (transform instanceof Affine) {
            Affine a2 = (Affine) transform.mo1183clone();
            a2.prepend(this);
            return a2;
        }
        double txx = transform.getMxx();
        double txy = transform.getMxy();
        double txz = transform.getMxz();
        double ttx = transform.getTx();
        double tyx = transform.getMyx();
        double tyy = transform.getMyy();
        double tyz = transform.getMyz();
        double tty = transform.getTy();
        double tzx = transform.getMzx();
        double tzy = transform.getMzy();
        double tzz = transform.getMzz();
        double ttz = transform.getTz();
        return new Affine(sx * txx, sx * txy, sx * txz, (sx * ttx) + ((1.0d - sx) * getPivotX()), sy * tyx, sy * tyy, sy * tyz, (sy * tty) + ((1.0d - sy) * getPivotY()), sz * tzx, sz * tzy, sz * tzz, (sz * ttz) + ((1.0d - sz) * getPivotZ()));
    }

    @Override // javafx.scene.transform.Transform
    public Scale createInverse() throws NonInvertibleTransformException {
        double sx = getX();
        double sy = getY();
        double sz = getZ();
        if (sx == 0.0d || sy == 0.0d || sz == 0.0d) {
            throw new NonInvertibleTransformException("Zero scale is not invertible");
        }
        return new Scale(1.0d / sx, 1.0d / sy, 1.0d / sz, getPivotX(), getPivotY(), getPivotZ());
    }

    @Override // javafx.scene.transform.Transform
    /* renamed from: clone */
    public Scale mo1183clone() {
        return new Scale(getX(), getY(), getZ(), getPivotX(), getPivotY(), getPivotZ());
    }

    @Override // javafx.scene.transform.Transform
    public Point2D transform(double x2, double y2) {
        ensureCanTransform2DPoint();
        double mxx = getX();
        double myy = getY();
        return new Point2D((mxx * x2) + ((1.0d - mxx) * getPivotX()), (myy * y2) + ((1.0d - myy) * getPivotY()));
    }

    @Override // javafx.scene.transform.Transform
    public Point3D transform(double x2, double y2, double z2) {
        double mxx = getX();
        double myy = getY();
        double mzz = getZ();
        return new Point3D((mxx * x2) + ((1.0d - mxx) * getPivotX()), (myy * y2) + ((1.0d - myy) * getPivotY()), (mzz * z2) + ((1.0d - mzz) * getPivotZ()));
    }

    @Override // javafx.scene.transform.Transform
    void transform2DPointsImpl(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) {
        double xx = getX();
        double yy = getY();
        double px = getPivotX();
        double py = getPivotY();
        while (true) {
            numPts--;
            if (numPts >= 0) {
                int i2 = srcOff;
                int srcOff2 = srcOff + 1;
                double x2 = srcPts[i2];
                srcOff = srcOff2 + 1;
                double y2 = srcPts[srcOff2];
                int i3 = dstOff;
                int dstOff2 = dstOff + 1;
                dstPts[i3] = (xx * x2) + ((1.0d - xx) * px);
                dstOff = dstOff2 + 1;
                dstPts[dstOff2] = (yy * y2) + ((1.0d - yy) * py);
            } else {
                return;
            }
        }
    }

    @Override // javafx.scene.transform.Transform
    void transform3DPointsImpl(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) {
        double xx = getX();
        double yy = getY();
        double zz = getZ();
        double px = getPivotX();
        double py = getPivotY();
        double pz = getPivotZ();
        while (true) {
            numPts--;
            if (numPts >= 0) {
                int i2 = dstOff;
                int dstOff2 = dstOff + 1;
                int i3 = srcOff;
                int srcOff2 = srcOff + 1;
                dstPts[i2] = (xx * srcPts[i3]) + ((1.0d - xx) * px);
                int dstOff3 = dstOff2 + 1;
                int srcOff3 = srcOff2 + 1;
                dstPts[dstOff2] = (yy * srcPts[srcOff2]) + ((1.0d - yy) * py);
                dstOff = dstOff3 + 1;
                srcOff = srcOff3 + 1;
                dstPts[dstOff3] = (zz * srcPts[srcOff3]) + ((1.0d - zz) * pz);
            } else {
                return;
            }
        }
    }

    @Override // javafx.scene.transform.Transform
    public Point2D deltaTransform(double x2, double y2) {
        ensureCanTransform2DPoint();
        return new Point2D(getX() * x2, getY() * y2);
    }

    @Override // javafx.scene.transform.Transform
    public Point3D deltaTransform(double x2, double y2, double z2) {
        return new Point3D(getX() * x2, getY() * y2, getZ() * z2);
    }

    @Override // javafx.scene.transform.Transform
    public Point2D inverseTransform(double x2, double y2) throws NonInvertibleTransformException {
        ensureCanTransform2DPoint();
        double sx = getX();
        double sy = getY();
        if (sx == 0.0d || sy == 0.0d) {
            throw new NonInvertibleTransformException("Zero scale is not invertible");
        }
        double mxx = 1.0d / sx;
        double myy = 1.0d / sy;
        return new Point2D((mxx * x2) + ((1.0d - mxx) * getPivotX()), (myy * y2) + ((1.0d - myy) * getPivotY()));
    }

    @Override // javafx.scene.transform.Transform
    public Point3D inverseTransform(double x2, double y2, double z2) throws NonInvertibleTransformException {
        double sx = getX();
        double sy = getY();
        double sz = getZ();
        if (sx == 0.0d || sy == 0.0d || sz == 0.0d) {
            throw new NonInvertibleTransformException("Zero scale is not invertible");
        }
        double mxx = 1.0d / sx;
        double myy = 1.0d / sy;
        double mzz = 1.0d / sz;
        return new Point3D((mxx * x2) + ((1.0d - mxx) * getPivotX()), (myy * y2) + ((1.0d - myy) * getPivotY()), (mzz * z2) + ((1.0d - mzz) * getPivotZ()));
    }

    @Override // javafx.scene.transform.Transform
    void inverseTransform2DPointsImpl(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) throws NonInvertibleTransformException {
        double sx = getX();
        double sy = getY();
        if (sx == 0.0d || sy == 0.0d) {
            throw new NonInvertibleTransformException("Zero scale is not invertible");
        }
        double xx = 1.0d / sx;
        double yy = 1.0d / sy;
        double px = getPivotX();
        double py = getPivotY();
        while (true) {
            numPts--;
            if (numPts >= 0) {
                int i2 = dstOff;
                int dstOff2 = dstOff + 1;
                int i3 = srcOff;
                int srcOff2 = srcOff + 1;
                dstPts[i2] = (xx * srcPts[i3]) + ((1.0d - xx) * px);
                dstOff = dstOff2 + 1;
                srcOff = srcOff2 + 1;
                dstPts[dstOff2] = (yy * srcPts[srcOff2]) + ((1.0d - yy) * py);
            } else {
                return;
            }
        }
    }

    @Override // javafx.scene.transform.Transform
    void inverseTransform3DPointsImpl(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) throws NonInvertibleTransformException {
        double sx = getX();
        double sy = getY();
        double sz = getZ();
        if (sx == 0.0d || sy == 0.0d || sz == 0.0d) {
            throw new NonInvertibleTransformException("Zero scale is not invertible");
        }
        double xx = 1.0d / sx;
        double yy = 1.0d / sy;
        double zz = 1.0d / sz;
        double px = getPivotX();
        double py = getPivotY();
        double pz = getPivotZ();
        while (true) {
            numPts--;
            if (numPts >= 0) {
                int i2 = dstOff;
                int dstOff2 = dstOff + 1;
                int i3 = srcOff;
                int srcOff2 = srcOff + 1;
                dstPts[i2] = (xx * srcPts[i3]) + ((1.0d - xx) * px);
                int dstOff3 = dstOff2 + 1;
                int srcOff3 = srcOff2 + 1;
                dstPts[dstOff2] = (yy * srcPts[srcOff2]) + ((1.0d - yy) * py);
                dstOff = dstOff3 + 1;
                srcOff = srcOff3 + 1;
                dstPts[dstOff3] = (zz * srcPts[srcOff3]) + ((1.0d - zz) * pz);
            } else {
                return;
            }
        }
    }

    @Override // javafx.scene.transform.Transform
    public Point2D inverseDeltaTransform(double x2, double y2) throws NonInvertibleTransformException {
        ensureCanTransform2DPoint();
        double sx = getX();
        double sy = getY();
        if (sx == 0.0d || sy == 0.0d) {
            throw new NonInvertibleTransformException("Zero scale is not invertible");
        }
        return new Point2D((1.0d / sx) * x2, (1.0d / sy) * y2);
    }

    @Override // javafx.scene.transform.Transform
    public Point3D inverseDeltaTransform(double x2, double y2, double z2) throws NonInvertibleTransformException {
        double sx = getX();
        double sy = getY();
        double sz = getZ();
        if (sx == 0.0d || sy == 0.0d || sz == 0.0d) {
            throw new NonInvertibleTransformException("Zero scale is not invertible");
        }
        return new Point3D((1.0d / sx) * x2, (1.0d / sy) * y2, (1.0d / sz) * z2);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Scale [");
        sb.append("x=").append(getX());
        sb.append(", y=").append(getY());
        sb.append(", z=").append(getZ());
        sb.append(", pivotX=").append(getPivotX());
        sb.append(", pivotY=").append(getPivotY());
        sb.append(", pivotZ=").append(getPivotZ());
        return sb.append("]").toString();
    }

    @Override // javafx.scene.transform.Transform
    @Deprecated
    public void impl_apply(Affine3D trans) {
        if (getPivotX() != 0.0d || getPivotY() != 0.0d || getPivotZ() != 0.0d) {
            trans.translate(getPivotX(), getPivotY(), getPivotZ());
            trans.scale(getX(), getY(), getZ());
            trans.translate(-getPivotX(), -getPivotY(), -getPivotZ());
            return;
        }
        trans.scale(getX(), getY(), getZ());
    }

    @Override // javafx.scene.transform.Transform
    @Deprecated
    public BaseTransform impl_derive(BaseTransform trans) {
        if (isIdentity()) {
            return trans;
        }
        if (getPivotX() != 0.0d || getPivotY() != 0.0d || getPivotZ() != 0.0d) {
            return trans.deriveWithTranslation(getPivotX(), getPivotY(), getPivotZ()).deriveWithScale(getX(), getY(), getZ()).deriveWithTranslation(-getPivotX(), -getPivotY(), -getPivotZ());
        }
        return trans.deriveWithScale(getX(), getY(), getZ());
    }

    @Override // javafx.scene.transform.Transform
    void validate() {
        getX();
        getPivotX();
        getY();
        getPivotY();
        getZ();
        getPivotZ();
    }

    @Override // javafx.scene.transform.Transform
    void appendTo(Affine a2) {
        a2.appendScale(getX(), getY(), getZ(), getPivotX(), getPivotY(), getPivotZ());
    }

    @Override // javafx.scene.transform.Transform
    void prependTo(Affine a2) {
        a2.prependScale(getX(), getY(), getZ(), getPivotX(), getPivotY(), getPivotZ());
    }
}
