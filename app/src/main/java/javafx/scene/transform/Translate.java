package javafx.scene.transform;

import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:javafx/scene/transform/Translate.class */
public class Translate extends Transform {

    /* renamed from: x, reason: collision with root package name */
    private DoubleProperty f12757x;

    /* renamed from: y, reason: collision with root package name */
    private DoubleProperty f12758y;

    /* renamed from: z, reason: collision with root package name */
    private DoubleProperty f12759z;

    public Translate() {
    }

    public Translate(double x2, double y2) {
        setX(x2);
        setY(y2);
    }

    public Translate(double x2, double y2, double z2) {
        this(x2, y2);
        setZ(z2);
    }

    public final void setX(double value) {
        xProperty().set(value);
    }

    public final double getX() {
        if (this.f12757x == null) {
            return 0.0d;
        }
        return this.f12757x.get();
    }

    public final DoubleProperty xProperty() {
        if (this.f12757x == null) {
            this.f12757x = new DoublePropertyBase() { // from class: javafx.scene.transform.Translate.1
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Translate.this.transformChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Translate.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return LanguageTag.PRIVATEUSE;
                }
            };
        }
        return this.f12757x;
    }

    public final void setY(double value) {
        yProperty().set(value);
    }

    public final double getY() {
        if (this.f12758y == null) {
            return 0.0d;
        }
        return this.f12758y.get();
    }

    public final DoubleProperty yProperty() {
        if (this.f12758y == null) {
            this.f12758y = new DoublePropertyBase() { // from class: javafx.scene.transform.Translate.2
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Translate.this.transformChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Translate.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return PdfOps.y_TOKEN;
                }
            };
        }
        return this.f12758y;
    }

    public final void setZ(double value) {
        zProperty().set(value);
    }

    public final double getZ() {
        if (this.f12759z == null) {
            return 0.0d;
        }
        return this.f12759z.get();
    }

    public final DoubleProperty zProperty() {
        if (this.f12759z == null) {
            this.f12759z = new DoublePropertyBase() { // from class: javafx.scene.transform.Translate.3
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Translate.this.transformChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Translate.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "z";
                }
            };
        }
        return this.f12759z;
    }

    @Override // javafx.scene.transform.Transform
    public double getTx() {
        return getX();
    }

    @Override // javafx.scene.transform.Transform
    public double getTy() {
        return getY();
    }

    @Override // javafx.scene.transform.Transform
    public double getTz() {
        return getZ();
    }

    @Override // javafx.scene.transform.Transform
    boolean computeIs2D() {
        return getZ() == 0.0d;
    }

    @Override // javafx.scene.transform.Transform
    boolean computeIsIdentity() {
        return getX() == 0.0d && getY() == 0.0d && getZ() == 0.0d;
    }

    @Override // javafx.scene.transform.Transform
    void fill2DArray(double[] array) {
        array[0] = 1.0d;
        array[1] = 0.0d;
        array[2] = getX();
        array[3] = 0.0d;
        array[4] = 1.0d;
        array[5] = getY();
    }

    @Override // javafx.scene.transform.Transform
    void fill3DArray(double[] array) {
        array[0] = 1.0d;
        array[1] = 0.0d;
        array[2] = 0.0d;
        array[3] = getX();
        array[4] = 0.0d;
        array[5] = 1.0d;
        array[6] = 0.0d;
        array[7] = getY();
        array[8] = 0.0d;
        array[9] = 0.0d;
        array[10] = 1.0d;
        array[11] = getZ();
    }

    @Override // javafx.scene.transform.Transform
    public Transform createConcatenation(Transform transform) {
        if (transform instanceof Translate) {
            Translate t2 = (Translate) transform;
            return new Translate(getX() + t2.getX(), getY() + t2.getY(), getZ() + t2.getZ());
        }
        if (transform instanceof Scale) {
            Scale s2 = (Scale) transform;
            double sx = s2.getX();
            double sy = s2.getY();
            double sz = s2.getZ();
            double tx = getX();
            double ty = getY();
            double tz = getZ();
            if ((tx == 0.0d || sx != 1.0d) && ((ty == 0.0d || sy != 1.0d) && (tz == 0.0d || sz != 1.0d))) {
                return new Scale(sx, sy, sz, s2.getPivotX() + (sx == 1.0d ? 0.0d : tx / (1.0d - sx)), s2.getPivotY() + (sy == 1.0d ? 0.0d : ty / (1.0d - sy)), s2.getPivotZ() + (sz == 1.0d ? 0.0d : tz / (1.0d - sz)));
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
        return new Affine(txx, txy, txz, ttx + getX(), tyx, tyy, tyz, tty + getY(), tzx, tzy, tzz, ttz + getZ());
    }

    @Override // javafx.scene.transform.Transform
    public Translate createInverse() {
        return new Translate(-getX(), -getY(), -getZ());
    }

    @Override // javafx.scene.transform.Transform
    /* renamed from: clone */
    public Translate mo1183clone() {
        return new Translate(getX(), getY(), getZ());
    }

    @Override // javafx.scene.transform.Transform
    public Point2D transform(double x2, double y2) {
        ensureCanTransform2DPoint();
        return new Point2D(x2 + getX(), y2 + getY());
    }

    @Override // javafx.scene.transform.Transform
    public Point3D transform(double x2, double y2, double z2) {
        return new Point3D(x2 + getX(), y2 + getY(), z2 + getZ());
    }

    @Override // javafx.scene.transform.Transform
    void transform2DPointsImpl(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) {
        double tx = getX();
        double ty = getY();
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
                dstPts[i3] = x2 + tx;
                dstOff = dstOff2 + 1;
                dstPts[dstOff2] = y2 + ty;
            } else {
                return;
            }
        }
    }

    @Override // javafx.scene.transform.Transform
    void transform3DPointsImpl(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) {
        double tx = getX();
        double ty = getY();
        double tz = getZ();
        while (true) {
            numPts--;
            if (numPts >= 0) {
                int i2 = srcOff;
                int srcOff2 = srcOff + 1;
                double x2 = srcPts[i2];
                int srcOff3 = srcOff2 + 1;
                double y2 = srcPts[srcOff2];
                srcOff = srcOff3 + 1;
                double z2 = srcPts[srcOff3];
                int i3 = dstOff;
                int dstOff2 = dstOff + 1;
                dstPts[i3] = x2 + tx;
                int dstOff3 = dstOff2 + 1;
                dstPts[dstOff2] = y2 + ty;
                dstOff = dstOff3 + 1;
                dstPts[dstOff3] = z2 + tz;
            } else {
                return;
            }
        }
    }

    @Override // javafx.scene.transform.Transform
    public Point2D deltaTransform(double x2, double y2) {
        ensureCanTransform2DPoint();
        return new Point2D(x2, y2);
    }

    @Override // javafx.scene.transform.Transform
    public Point2D deltaTransform(Point2D point) {
        if (point == null) {
            throw new NullPointerException();
        }
        ensureCanTransform2DPoint();
        return point;
    }

    @Override // javafx.scene.transform.Transform
    public Point3D deltaTransform(double x2, double y2, double z2) {
        return new Point3D(x2, y2, z2);
    }

    @Override // javafx.scene.transform.Transform
    public Point3D deltaTransform(Point3D point) {
        if (point == null) {
            throw new NullPointerException();
        }
        return point;
    }

    @Override // javafx.scene.transform.Transform
    public Point2D inverseTransform(double x2, double y2) {
        ensureCanTransform2DPoint();
        return new Point2D(x2 - getX(), y2 - getY());
    }

    @Override // javafx.scene.transform.Transform
    public Point3D inverseTransform(double x2, double y2, double z2) {
        return new Point3D(x2 - getX(), y2 - getY(), z2 - getZ());
    }

    @Override // javafx.scene.transform.Transform
    void inverseTransform2DPointsImpl(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) {
        double tx = getX();
        double ty = getY();
        while (true) {
            numPts--;
            if (numPts >= 0) {
                int i2 = dstOff;
                int dstOff2 = dstOff + 1;
                int i3 = srcOff;
                int srcOff2 = srcOff + 1;
                dstPts[i2] = srcPts[i3] - tx;
                dstOff = dstOff2 + 1;
                srcOff = srcOff2 + 1;
                dstPts[dstOff2] = srcPts[srcOff2] - ty;
            } else {
                return;
            }
        }
    }

    @Override // javafx.scene.transform.Transform
    void inverseTransform3DPointsImpl(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) {
        double tx = getX();
        double ty = getY();
        double tz = getZ();
        while (true) {
            numPts--;
            if (numPts >= 0) {
                int i2 = dstOff;
                int dstOff2 = dstOff + 1;
                int i3 = srcOff;
                int srcOff2 = srcOff + 1;
                dstPts[i2] = srcPts[i3] - tx;
                int dstOff3 = dstOff2 + 1;
                int srcOff3 = srcOff2 + 1;
                dstPts[dstOff2] = srcPts[srcOff2] - ty;
                dstOff = dstOff3 + 1;
                srcOff = srcOff3 + 1;
                dstPts[dstOff3] = srcPts[srcOff3] - tz;
            } else {
                return;
            }
        }
    }

    @Override // javafx.scene.transform.Transform
    public Point2D inverseDeltaTransform(double x2, double y2) {
        ensureCanTransform2DPoint();
        return new Point2D(x2, y2);
    }

    @Override // javafx.scene.transform.Transform
    public Point2D inverseDeltaTransform(Point2D point) {
        if (point == null) {
            throw new NullPointerException();
        }
        ensureCanTransform2DPoint();
        return point;
    }

    @Override // javafx.scene.transform.Transform
    public Point3D inverseDeltaTransform(double x2, double y2, double z2) {
        return new Point3D(x2, y2, z2);
    }

    @Override // javafx.scene.transform.Transform
    public Point3D inverseDeltaTransform(Point3D point) {
        if (point == null) {
            throw new NullPointerException();
        }
        return point;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Translate [");
        sb.append("x=").append(getX());
        sb.append(", y=").append(getY());
        sb.append(", z=").append(getZ());
        return sb.append("]").toString();
    }

    @Override // javafx.scene.transform.Transform
    @Deprecated
    public void impl_apply(Affine3D trans) {
        trans.translate(getX(), getY(), getZ());
    }

    @Override // javafx.scene.transform.Transform
    @Deprecated
    public BaseTransform impl_derive(BaseTransform trans) {
        return trans.deriveWithTranslation(getX(), getY(), getZ());
    }

    @Override // javafx.scene.transform.Transform
    void validate() {
        getX();
        getY();
        getZ();
    }

    @Override // javafx.scene.transform.Transform
    void appendTo(Affine a2) {
        a2.appendTranslation(getTx(), getTy(), getTz());
    }

    @Override // javafx.scene.transform.Transform
    void prependTo(Affine a2) {
        a2.prependTranslation(getTx(), getTy(), getTz());
    }
}
