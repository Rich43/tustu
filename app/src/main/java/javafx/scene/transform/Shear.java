package javafx.scene.transform;

import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:javafx/scene/transform/Shear.class */
public class Shear extends Transform {

    /* renamed from: x, reason: collision with root package name */
    private DoubleProperty f12753x;

    /* renamed from: y, reason: collision with root package name */
    private DoubleProperty f12754y;
    private DoubleProperty pivotX;
    private DoubleProperty pivotY;

    public Shear() {
    }

    public Shear(double x2, double y2) {
        setX(x2);
        setY(y2);
    }

    public Shear(double x2, double y2, double pivotX, double pivotY) {
        setX(x2);
        setY(y2);
        setPivotX(pivotX);
        setPivotY(pivotY);
    }

    public final void setX(double value) {
        xProperty().set(value);
    }

    public final double getX() {
        if (this.f12753x == null) {
            return 0.0d;
        }
        return this.f12753x.get();
    }

    public final DoubleProperty xProperty() {
        if (this.f12753x == null) {
            this.f12753x = new DoublePropertyBase() { // from class: javafx.scene.transform.Shear.1
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Shear.this.transformChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Shear.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return LanguageTag.PRIVATEUSE;
                }
            };
        }
        return this.f12753x;
    }

    public final void setY(double value) {
        yProperty().set(value);
    }

    public final double getY() {
        if (this.f12754y == null) {
            return 0.0d;
        }
        return this.f12754y.get();
    }

    public final DoubleProperty yProperty() {
        if (this.f12754y == null) {
            this.f12754y = new DoublePropertyBase() { // from class: javafx.scene.transform.Shear.2
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Shear.this.transformChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Shear.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return PdfOps.y_TOKEN;
                }
            };
        }
        return this.f12754y;
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
            this.pivotX = new DoublePropertyBase() { // from class: javafx.scene.transform.Shear.3
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Shear.this.transformChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Shear.this;
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
            this.pivotY = new DoublePropertyBase() { // from class: javafx.scene.transform.Shear.4
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Shear.this.transformChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Shear.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "pivotY";
                }
            };
        }
        return this.pivotY;
    }

    @Override // javafx.scene.transform.Transform
    public double getMxy() {
        return getX();
    }

    @Override // javafx.scene.transform.Transform
    public double getMyx() {
        return getY();
    }

    @Override // javafx.scene.transform.Transform
    public double getTx() {
        return (-getX()) * getPivotY();
    }

    @Override // javafx.scene.transform.Transform
    public double getTy() {
        return (-getY()) * getPivotX();
    }

    @Override // javafx.scene.transform.Transform
    boolean computeIs2D() {
        return true;
    }

    @Override // javafx.scene.transform.Transform
    boolean computeIsIdentity() {
        return getX() == 0.0d && getY() == 0.0d;
    }

    @Override // javafx.scene.transform.Transform
    void fill2DArray(double[] array) {
        double sx = getX();
        double sy = getY();
        array[0] = 1.0d;
        array[1] = sx;
        array[2] = (-sx) * getPivotY();
        array[3] = sy;
        array[4] = 1.0d;
        array[5] = (-sy) * getPivotX();
    }

    @Override // javafx.scene.transform.Transform
    void fill3DArray(double[] array) {
        double sx = getX();
        double sy = getY();
        array[0] = 1.0d;
        array[1] = sx;
        array[2] = 0.0d;
        array[3] = (-sx) * getPivotY();
        array[4] = sy;
        array[5] = 1.0d;
        array[6] = 0.0d;
        array[7] = (-sy) * getPivotX();
        array[8] = 0.0d;
        array[9] = 0.0d;
        array[10] = 1.0d;
        array[11] = 0.0d;
    }

    @Override // javafx.scene.transform.Transform
    public Transform createConcatenation(Transform transform) {
        if (transform instanceof Affine) {
            Affine a2 = (Affine) transform.mo1183clone();
            a2.prepend(this);
            return a2;
        }
        double sx = getX();
        double sy = getY();
        double txx = transform.getMxx();
        double txy = transform.getMxy();
        double txz = transform.getMxz();
        double ttx = transform.getTx();
        double tyx = transform.getMyx();
        double tyy = transform.getMyy();
        double tyz = transform.getMyz();
        double tty = transform.getTy();
        return new Affine(txx + (sx * tyx), txy + (sx * tyy), txz + (sx * tyz), (ttx + (sx * tty)) - (sx * getPivotY()), (sy * txx) + tyx, (sy * txy) + tyy, (sy * txz) + tyz, ((sy * ttx) + tty) - (sy * getPivotX()), transform.getMzx(), transform.getMzy(), transform.getMzz(), transform.getTz());
    }

    @Override // javafx.scene.transform.Transform
    public Transform createInverse() {
        double sx = getX();
        double sy = getY();
        if (sy == 0.0d) {
            return new Shear(-sx, 0.0d, 0.0d, getPivotY());
        }
        if (sx == 0.0d) {
            return new Shear(0.0d, -sy, getPivotX(), 0.0d);
        }
        double px = getPivotX();
        double py = getPivotY();
        double coef = 1.0d / (1.0d - (sx * sy));
        return new Affine(coef, (-sx) * coef, 0.0d, sx * (py - (sy * px)) * coef, (-sy) * coef, 1.0d + (sx * sy * coef), 0.0d, (sy * px) + (sy * (((sx * sy) * px) - (sx * py)) * coef), 0.0d, 0.0d, 1.0d, 0.0d);
    }

    @Override // javafx.scene.transform.Transform
    /* renamed from: clone */
    public Shear mo1183clone() {
        return new Shear(getX(), getY(), getPivotX(), getPivotY());
    }

    @Override // javafx.scene.transform.Transform
    public Point2D transform(double x2, double y2) {
        double mxy = getX();
        double myx = getY();
        return new Point2D((x2 + (mxy * y2)) - (mxy * getPivotY()), ((myx * x2) + y2) - (myx * getPivotX()));
    }

    @Override // javafx.scene.transform.Transform
    public Point3D transform(double x2, double y2, double z2) {
        double mxy = getX();
        double myx = getY();
        return new Point3D((x2 + (mxy * y2)) - (mxy * getPivotY()), ((myx * x2) + y2) - (myx * getPivotX()), z2);
    }

    @Override // javafx.scene.transform.Transform
    void transform2DPointsImpl(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) {
        double xy = getX();
        double yx = getY();
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
                dstPts[i3] = (x2 + (xy * y2)) - (xy * py);
                dstOff = dstOff2 + 1;
                dstPts[dstOff2] = ((yx * x2) + y2) - (yx * px);
            } else {
                return;
            }
        }
    }

    @Override // javafx.scene.transform.Transform
    void transform3DPointsImpl(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) {
        double xy = getX();
        double yx = getY();
        double px = getPivotX();
        double py = getPivotY();
        while (true) {
            numPts--;
            if (numPts >= 0) {
                int i2 = srcOff;
                int srcOff2 = srcOff + 1;
                double x2 = srcPts[i2];
                int srcOff3 = srcOff2 + 1;
                double y2 = srcPts[srcOff2];
                int i3 = dstOff;
                int dstOff2 = dstOff + 1;
                dstPts[i3] = (x2 + (xy * y2)) - (xy * py);
                int dstOff3 = dstOff2 + 1;
                dstPts[dstOff2] = ((yx * x2) + y2) - (yx * px);
                dstOff = dstOff3 + 1;
                srcOff = srcOff3 + 1;
                dstPts[dstOff3] = srcPts[srcOff3];
            } else {
                return;
            }
        }
    }

    @Override // javafx.scene.transform.Transform
    public Point2D deltaTransform(double x2, double y2) {
        return new Point2D(x2 + (getX() * y2), (getY() * x2) + y2);
    }

    @Override // javafx.scene.transform.Transform
    public Point3D deltaTransform(double x2, double y2, double z2) {
        return new Point3D(x2 + (getX() * y2), (getY() * x2) + y2, z2);
    }

    @Override // javafx.scene.transform.Transform
    public Point2D inverseTransform(double x2, double y2) throws NonInvertibleTransformException {
        double sx = getX();
        double sy = getY();
        if (sy == 0.0d) {
            double mxy = -getX();
            return new Point2D((x2 + (mxy * y2)) - (mxy * getPivotY()), y2);
        }
        if (sx == 0.0d) {
            double myx = -getY();
            return new Point2D(x2, ((myx * x2) + y2) - (myx * getPivotX()));
        }
        return super.inverseTransform(x2, y2);
    }

    @Override // javafx.scene.transform.Transform
    public Point3D inverseTransform(double x2, double y2, double z2) throws NonInvertibleTransformException {
        double sx = getX();
        double sy = getY();
        if (sy == 0.0d) {
            double mxy = -getX();
            return new Point3D((x2 + (mxy * y2)) - (mxy * getPivotY()), y2, z2);
        }
        if (sx == 0.0d) {
            double myx = -getY();
            return new Point3D(x2, ((myx * x2) + y2) - (myx * getPivotX()), z2);
        }
        return super.inverseTransform(x2, y2, z2);
    }

    @Override // javafx.scene.transform.Transform
    void inverseTransform2DPointsImpl(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) throws NonInvertibleTransformException {
        double px = getPivotX();
        double py = getPivotY();
        double sx = getX();
        double sy = getY();
        if (sy == 0.0d) {
            double xy = -sx;
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
                    dstPts[i3] = (x2 + (xy * y2)) - (xy * py);
                    dstOff = dstOff2 + 1;
                    dstPts[dstOff2] = y2;
                } else {
                    return;
                }
            }
        } else if (sx == 0.0d) {
            double yx = -sy;
            while (true) {
                numPts--;
                if (numPts >= 0) {
                    int i4 = srcOff;
                    int srcOff3 = srcOff + 1;
                    double x3 = srcPts[i4];
                    srcOff = srcOff3 + 1;
                    double y3 = srcPts[srcOff3];
                    int i5 = dstOff;
                    int dstOff3 = dstOff + 1;
                    dstPts[i5] = x3;
                    dstOff = dstOff3 + 1;
                    dstPts[dstOff3] = ((yx * x3) + y3) - (yx * px);
                } else {
                    return;
                }
            }
        } else {
            super.inverseTransform2DPointsImpl(srcPts, srcOff, dstPts, dstOff, numPts);
        }
    }

    @Override // javafx.scene.transform.Transform
    void inverseTransform3DPointsImpl(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) throws NonInvertibleTransformException {
        double px = getPivotX();
        double py = getPivotY();
        double sx = getX();
        double sy = getY();
        if (sy == 0.0d) {
            double xy = -sx;
            while (true) {
                numPts--;
                if (numPts >= 0) {
                    int i2 = srcOff;
                    int srcOff2 = srcOff + 1;
                    double x2 = srcPts[i2];
                    int srcOff3 = srcOff2 + 1;
                    double y2 = srcPts[srcOff2];
                    int i3 = dstOff;
                    int dstOff2 = dstOff + 1;
                    dstPts[i3] = (x2 + (xy * y2)) - (xy * py);
                    int dstOff3 = dstOff2 + 1;
                    dstPts[dstOff2] = y2;
                    dstOff = dstOff3 + 1;
                    srcOff = srcOff3 + 1;
                    dstPts[dstOff3] = srcPts[srcOff3];
                } else {
                    return;
                }
            }
        } else if (sx == 0.0d) {
            double yx = -sy;
            while (true) {
                numPts--;
                if (numPts >= 0) {
                    int i4 = srcOff;
                    int srcOff4 = srcOff + 1;
                    double x3 = srcPts[i4];
                    int srcOff5 = srcOff4 + 1;
                    double y3 = srcPts[srcOff4];
                    int i5 = dstOff;
                    int dstOff4 = dstOff + 1;
                    dstPts[i5] = x3;
                    int dstOff5 = dstOff4 + 1;
                    dstPts[dstOff4] = ((yx * x3) + y3) - (yx * px);
                    dstOff = dstOff5 + 1;
                    srcOff = srcOff5 + 1;
                    dstPts[dstOff5] = srcPts[srcOff5];
                } else {
                    return;
                }
            }
        } else {
            super.inverseTransform3DPointsImpl(srcPts, srcOff, dstPts, dstOff, numPts);
        }
    }

    @Override // javafx.scene.transform.Transform
    public Point2D inverseDeltaTransform(double x2, double y2) throws NonInvertibleTransformException {
        double sx = getX();
        double sy = getY();
        if (sy == 0.0d) {
            return new Point2D(x2 - (getX() * y2), y2);
        }
        if (sx == 0.0d) {
            return new Point2D(x2, ((-getY()) * x2) + y2);
        }
        return super.inverseDeltaTransform(x2, y2);
    }

    @Override // javafx.scene.transform.Transform
    public Point3D inverseDeltaTransform(double x2, double y2, double z2) throws NonInvertibleTransformException {
        double sx = getX();
        double sy = getY();
        if (sy == 0.0d) {
            return new Point3D(x2 - (getX() * y2), y2, z2);
        }
        if (sx == 0.0d) {
            return new Point3D(x2, ((-getY()) * x2) + y2, z2);
        }
        return super.inverseDeltaTransform(x2, y2, z2);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Shear [");
        sb.append("x=").append(getX());
        sb.append(", y=").append(getY());
        sb.append(", pivotX=").append(getPivotX());
        sb.append(", pivotY=").append(getPivotY());
        return sb.append("]").toString();
    }

    @Override // javafx.scene.transform.Transform
    @Deprecated
    public void impl_apply(Affine3D trans) {
        if (getPivotX() != 0.0d || getPivotY() != 0.0d) {
            trans.translate(getPivotX(), getPivotY());
            trans.shear(getX(), getY());
            trans.translate(-getPivotX(), -getPivotY());
            return;
        }
        trans.shear(getX(), getY());
    }

    @Override // javafx.scene.transform.Transform
    @Deprecated
    public BaseTransform impl_derive(BaseTransform trans) {
        return trans.deriveWithConcatenation(1.0d, getY(), getX(), 1.0d, getTx(), getTy());
    }

    @Override // javafx.scene.transform.Transform
    void validate() {
        getX();
        getPivotX();
        getY();
        getPivotY();
    }

    @Override // javafx.scene.transform.Transform
    void appendTo(Affine a2) {
        a2.appendShear(getX(), getY(), getPivotX(), getPivotY());
    }

    @Override // javafx.scene.transform.Transform
    void prependTo(Affine a2) {
        a2.prependShear(getX(), getY(), getPivotX(), getPivotY());
    }
}
