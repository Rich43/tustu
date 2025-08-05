package javafx.scene.transform;

import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

/* loaded from: jfxrt.jar:javafx/scene/transform/Rotate.class */
public class Rotate extends Transform {
    public static final Point3D X_AXIS = new Point3D(1.0d, 0.0d, 0.0d);
    public static final Point3D Y_AXIS = new Point3D(0.0d, 1.0d, 0.0d);
    public static final Point3D Z_AXIS = new Point3D(0.0d, 0.0d, 1.0d);
    private MatrixCache cache;
    private MatrixCache inverseCache;
    private DoubleProperty angle;
    private DoubleProperty pivotX;
    private DoubleProperty pivotY;
    private DoubleProperty pivotZ;
    private ObjectProperty<Point3D> axis;

    public Rotate() {
    }

    public Rotate(double angle) {
        setAngle(angle);
    }

    public Rotate(double angle, Point3D axis) {
        setAngle(angle);
        setAxis(axis);
    }

    public Rotate(double angle, double pivotX, double pivotY) {
        setAngle(angle);
        setPivotX(pivotX);
        setPivotY(pivotY);
    }

    public Rotate(double angle, double pivotX, double pivotY, double pivotZ) {
        this(angle, pivotX, pivotY);
        setPivotZ(pivotZ);
    }

    public Rotate(double angle, double pivotX, double pivotY, double pivotZ, Point3D axis) {
        this(angle, pivotX, pivotY);
        setPivotZ(pivotZ);
        setAxis(axis);
    }

    public final void setAngle(double value) {
        angleProperty().set(value);
    }

    public final double getAngle() {
        if (this.angle == null) {
            return 0.0d;
        }
        return this.angle.get();
    }

    public final DoubleProperty angleProperty() {
        if (this.angle == null) {
            this.angle = new DoublePropertyBase() { // from class: javafx.scene.transform.Rotate.1
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Rotate.this.transformChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Rotate.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "angle";
                }
            };
        }
        return this.angle;
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
            this.pivotX = new DoublePropertyBase() { // from class: javafx.scene.transform.Rotate.2
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Rotate.this.transformChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Rotate.this;
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
            this.pivotY = new DoublePropertyBase() { // from class: javafx.scene.transform.Rotate.3
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Rotate.this.transformChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Rotate.this;
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
            this.pivotZ = new DoublePropertyBase() { // from class: javafx.scene.transform.Rotate.4
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Rotate.this.transformChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Rotate.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "pivotZ";
                }
            };
        }
        return this.pivotZ;
    }

    public final void setAxis(Point3D value) {
        axisProperty().set(value);
    }

    public final Point3D getAxis() {
        return this.axis == null ? Z_AXIS : this.axis.get();
    }

    public final ObjectProperty<Point3D> axisProperty() {
        if (this.axis == null) {
            this.axis = new ObjectPropertyBase<Point3D>(Z_AXIS) { // from class: javafx.scene.transform.Rotate.5
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    Rotate.this.transformChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Rotate.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "axis";
                }
            };
        }
        return this.axis;
    }

    @Override // javafx.scene.transform.Transform
    public double getMxx() {
        updateCache();
        return this.cache.mxx;
    }

    @Override // javafx.scene.transform.Transform
    public double getMxy() {
        updateCache();
        return this.cache.mxy;
    }

    @Override // javafx.scene.transform.Transform
    public double getMxz() {
        updateCache();
        return this.cache.mxz;
    }

    @Override // javafx.scene.transform.Transform
    public double getTx() {
        updateCache();
        return this.cache.tx;
    }

    @Override // javafx.scene.transform.Transform
    public double getMyx() {
        updateCache();
        return this.cache.myx;
    }

    @Override // javafx.scene.transform.Transform
    public double getMyy() {
        updateCache();
        return this.cache.myy;
    }

    @Override // javafx.scene.transform.Transform
    public double getMyz() {
        updateCache();
        return this.cache.myz;
    }

    @Override // javafx.scene.transform.Transform
    public double getTy() {
        updateCache();
        return this.cache.ty;
    }

    @Override // javafx.scene.transform.Transform
    public double getMzx() {
        updateCache();
        return this.cache.mzx;
    }

    @Override // javafx.scene.transform.Transform
    public double getMzy() {
        updateCache();
        return this.cache.mzy;
    }

    @Override // javafx.scene.transform.Transform
    public double getMzz() {
        updateCache();
        return this.cache.mzz;
    }

    @Override // javafx.scene.transform.Transform
    public double getTz() {
        updateCache();
        return this.cache.tz;
    }

    @Override // javafx.scene.transform.Transform
    boolean computeIs2D() {
        Point3D a2 = getAxis();
        return (a2.getX() == 0.0d && a2.getY() == 0.0d) || getAngle() == 0.0d;
    }

    @Override // javafx.scene.transform.Transform
    boolean computeIsIdentity() {
        if (getAngle() == 0.0d) {
            return true;
        }
        Point3D a2 = getAxis();
        return a2.getX() == 0.0d && a2.getY() == 0.0d && a2.getZ() == 0.0d;
    }

    @Override // javafx.scene.transform.Transform
    void fill2DArray(double[] array) {
        updateCache();
        array[0] = this.cache.mxx;
        array[1] = this.cache.mxy;
        array[2] = this.cache.tx;
        array[3] = this.cache.myx;
        array[4] = this.cache.myy;
        array[5] = this.cache.ty;
    }

    @Override // javafx.scene.transform.Transform
    void fill3DArray(double[] array) {
        updateCache();
        array[0] = this.cache.mxx;
        array[1] = this.cache.mxy;
        array[2] = this.cache.mxz;
        array[3] = this.cache.tx;
        array[4] = this.cache.myx;
        array[5] = this.cache.myy;
        array[6] = this.cache.myz;
        array[7] = this.cache.ty;
        array[8] = this.cache.mzx;
        array[9] = this.cache.mzy;
        array[10] = this.cache.mzz;
        array[11] = this.cache.tz;
    }

    @Override // javafx.scene.transform.Transform
    public Transform createConcatenation(Transform transform) {
        if (transform instanceof Rotate) {
            Rotate r2 = (Rotate) transform;
            double px = getPivotX();
            double py = getPivotY();
            double pz = getPivotZ();
            if ((r2.getAxis() == getAxis() || r2.getAxis().normalize().equals(getAxis().normalize())) && px == r2.getPivotX() && py == r2.getPivotY() && pz == r2.getPivotZ()) {
                return new Rotate(getAngle() + r2.getAngle(), px, py, pz, getAxis());
            }
        }
        if (transform instanceof Affine) {
            Affine a2 = (Affine) transform.mo1183clone();
            a2.prepend(this);
            return a2;
        }
        return super.createConcatenation(transform);
    }

    @Override // javafx.scene.transform.Transform
    public Transform createInverse() throws NonInvertibleTransformException {
        return new Rotate(-getAngle(), getPivotX(), getPivotY(), getPivotZ(), getAxis());
    }

    @Override // javafx.scene.transform.Transform
    /* renamed from: clone */
    public Rotate mo1183clone() {
        return new Rotate(getAngle(), getPivotX(), getPivotY(), getPivotZ(), getAxis());
    }

    @Override // javafx.scene.transform.Transform
    public Point2D transform(double x2, double y2) {
        ensureCanTransform2DPoint();
        updateCache();
        return new Point2D((this.cache.mxx * x2) + (this.cache.mxy * y2) + this.cache.tx, (this.cache.myx * x2) + (this.cache.myy * y2) + this.cache.ty);
    }

    @Override // javafx.scene.transform.Transform
    public Point3D transform(double x2, double y2, double z2) {
        updateCache();
        return new Point3D((this.cache.mxx * x2) + (this.cache.mxy * y2) + (this.cache.mxz * z2) + this.cache.tx, (this.cache.myx * x2) + (this.cache.myy * y2) + (this.cache.myz * z2) + this.cache.ty, (this.cache.mzx * x2) + (this.cache.mzy * y2) + (this.cache.mzz * z2) + this.cache.tz);
    }

    @Override // javafx.scene.transform.Transform
    void transform2DPointsImpl(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) {
        updateCache();
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
                dstPts[i3] = (this.cache.mxx * x2) + (this.cache.mxy * y2) + this.cache.tx;
                dstOff = dstOff2 + 1;
                dstPts[dstOff2] = (this.cache.myx * x2) + (this.cache.myy * y2) + this.cache.ty;
            } else {
                return;
            }
        }
    }

    @Override // javafx.scene.transform.Transform
    void transform3DPointsImpl(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) {
        updateCache();
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
                dstPts[i3] = (this.cache.mxx * x2) + (this.cache.mxy * y2) + (this.cache.mxz * z2) + this.cache.tx;
                int dstOff3 = dstOff2 + 1;
                dstPts[dstOff2] = (this.cache.myx * x2) + (this.cache.myy * y2) + (this.cache.myz * z2) + this.cache.ty;
                dstOff = dstOff3 + 1;
                dstPts[dstOff3] = (this.cache.mzx * x2) + (this.cache.mzy * y2) + (this.cache.mzz * z2) + this.cache.tz;
            } else {
                return;
            }
        }
    }

    @Override // javafx.scene.transform.Transform
    public Point2D deltaTransform(double x2, double y2) {
        ensureCanTransform2DPoint();
        updateCache();
        return new Point2D((this.cache.mxx * x2) + (this.cache.mxy * y2), (this.cache.myx * x2) + (this.cache.myy * y2));
    }

    @Override // javafx.scene.transform.Transform
    public Point3D deltaTransform(double x2, double y2, double z2) {
        updateCache();
        return new Point3D((this.cache.mxx * x2) + (this.cache.mxy * y2) + (this.cache.mxz * z2), (this.cache.myx * x2) + (this.cache.myy * y2) + (this.cache.myz * z2), (this.cache.mzx * x2) + (this.cache.mzy * y2) + (this.cache.mzz * z2));
    }

    @Override // javafx.scene.transform.Transform
    public Point2D inverseTransform(double x2, double y2) {
        ensureCanTransform2DPoint();
        updateInverseCache();
        return new Point2D((this.inverseCache.mxx * x2) + (this.inverseCache.mxy * y2) + this.inverseCache.tx, (this.inverseCache.myx * x2) + (this.inverseCache.myy * y2) + this.inverseCache.ty);
    }

    @Override // javafx.scene.transform.Transform
    public Point3D inverseTransform(double x2, double y2, double z2) {
        updateInverseCache();
        return new Point3D((this.inverseCache.mxx * x2) + (this.inverseCache.mxy * y2) + (this.inverseCache.mxz * z2) + this.inverseCache.tx, (this.inverseCache.myx * x2) + (this.inverseCache.myy * y2) + (this.inverseCache.myz * z2) + this.inverseCache.ty, (this.inverseCache.mzx * x2) + (this.inverseCache.mzy * y2) + (this.inverseCache.mzz * z2) + this.inverseCache.tz);
    }

    @Override // javafx.scene.transform.Transform
    void inverseTransform2DPointsImpl(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) {
        updateInverseCache();
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
                dstPts[i3] = (this.inverseCache.mxx * x2) + (this.inverseCache.mxy * y2) + this.inverseCache.tx;
                dstOff = dstOff2 + 1;
                dstPts[dstOff2] = (this.inverseCache.myx * x2) + (this.inverseCache.myy * y2) + this.inverseCache.ty;
            } else {
                return;
            }
        }
    }

    @Override // javafx.scene.transform.Transform
    void inverseTransform3DPointsImpl(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) {
        updateInverseCache();
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
                dstPts[i3] = (this.inverseCache.mxx * x2) + (this.inverseCache.mxy * y2) + (this.inverseCache.mxz * z2) + this.inverseCache.tx;
                int dstOff3 = dstOff2 + 1;
                dstPts[dstOff2] = (this.inverseCache.myx * x2) + (this.inverseCache.myy * y2) + (this.inverseCache.myz * z2) + this.inverseCache.ty;
                dstOff = dstOff3 + 1;
                dstPts[dstOff3] = (this.inverseCache.mzx * x2) + (this.inverseCache.mzy * y2) + (this.inverseCache.mzz * z2) + this.inverseCache.tz;
            } else {
                return;
            }
        }
    }

    @Override // javafx.scene.transform.Transform
    public Point2D inverseDeltaTransform(double x2, double y2) {
        ensureCanTransform2DPoint();
        updateInverseCache();
        return new Point2D((this.inverseCache.mxx * x2) + (this.inverseCache.mxy * y2), (this.inverseCache.myx * x2) + (this.inverseCache.myy * y2));
    }

    @Override // javafx.scene.transform.Transform
    public Point3D inverseDeltaTransform(double x2, double y2, double z2) {
        updateInverseCache();
        return new Point3D((this.inverseCache.mxx * x2) + (this.inverseCache.mxy * y2) + (this.inverseCache.mxz * z2), (this.inverseCache.myx * x2) + (this.inverseCache.myy * y2) + (this.inverseCache.myz * z2), (this.inverseCache.mzx * x2) + (this.inverseCache.mzy * y2) + (this.inverseCache.mzz * z2));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Rotate [");
        sb.append("angle=").append(getAngle());
        sb.append(", pivotX=").append(getPivotX());
        sb.append(", pivotY=").append(getPivotY());
        sb.append(", pivotZ=").append(getPivotZ());
        sb.append(", axis=").append((Object) getAxis());
        return sb.append("]").toString();
    }

    @Override // javafx.scene.transform.Transform
    @Deprecated
    public void impl_apply(Affine3D trans) {
        double localPivotX = getPivotX();
        double localPivotY = getPivotY();
        double localPivotZ = getPivotZ();
        double localAngle = getAngle();
        if (localPivotX != 0.0d || localPivotY != 0.0d || localPivotZ != 0.0d) {
            trans.translate(localPivotX, localPivotY, localPivotZ);
            trans.rotate(Math.toRadians(localAngle), getAxis().getX(), getAxis().getY(), getAxis().getZ());
            trans.translate(-localPivotX, -localPivotY, -localPivotZ);
            return;
        }
        trans.rotate(Math.toRadians(localAngle), getAxis().getX(), getAxis().getY(), getAxis().getZ());
    }

    @Override // javafx.scene.transform.Transform
    @Deprecated
    public BaseTransform impl_derive(BaseTransform trans) {
        if (isIdentity()) {
            return trans;
        }
        double localPivotX = getPivotX();
        double localPivotY = getPivotY();
        double localPivotZ = getPivotZ();
        double localAngle = getAngle();
        if (localPivotX != 0.0d || localPivotY != 0.0d || localPivotZ != 0.0d) {
            return trans.deriveWithTranslation(localPivotX, localPivotY, localPivotZ).deriveWithRotation(Math.toRadians(localAngle), getAxis().getX(), getAxis().getY(), getAxis().getZ()).deriveWithTranslation(-localPivotX, -localPivotY, -localPivotZ);
        }
        return trans.deriveWithRotation(Math.toRadians(localAngle), getAxis().getX(), getAxis().getY(), getAxis().getZ());
    }

    @Override // javafx.scene.transform.Transform
    void validate() {
        getAxis();
        getAngle();
        getPivotX();
        getPivotY();
        getPivotZ();
    }

    @Override // javafx.scene.transform.Transform
    protected void transformChanged() {
        if (this.cache != null) {
            this.cache.invalidate();
        }
        super.transformChanged();
    }

    @Override // javafx.scene.transform.Transform
    void appendTo(Affine a2) {
        a2.appendRotation(getAngle(), getPivotX(), getPivotY(), getPivotZ(), getAxis());
    }

    @Override // javafx.scene.transform.Transform
    void prependTo(Affine a2) {
        a2.prependRotation(getAngle(), getPivotX(), getPivotY(), getPivotZ(), getAxis());
    }

    private void updateCache() {
        if (this.cache == null) {
            this.cache = new MatrixCache();
        }
        if (!this.cache.valid) {
            this.cache.update(getAngle(), getAxis(), getPivotX(), getPivotY(), getPivotZ());
        }
    }

    private void updateInverseCache() {
        if (this.inverseCache == null) {
            this.inverseCache = new MatrixCache();
        }
        if (!this.inverseCache.valid) {
            this.inverseCache.update(-getAngle(), getAxis(), getPivotX(), getPivotY(), getPivotZ());
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/transform/Rotate$MatrixCache.class */
    private static class MatrixCache {
        double mxx;
        double mxy;
        double mxz;
        double tx;
        double myx;
        double myy;
        double myz;
        double ty;
        double mzx;
        double mzy;
        double tz;
        boolean valid = false;
        boolean is3D = false;
        double mzz = 1.0d;

        public void update(double angle, Point3D axis, double px, double py, double pz) {
            double axisX;
            double axisY;
            double axisZ;
            double rads = Math.toRadians(angle);
            double sin = Math.sin(rads);
            double cos = Math.cos(rads);
            if (axis == Rotate.Z_AXIS || (axis.getX() == 0.0d && axis.getY() == 0.0d && axis.getZ() > 0.0d)) {
                this.mxx = cos;
                this.mxy = -sin;
                this.tx = (px * (1.0d - cos)) + (py * sin);
                this.myx = sin;
                this.myy = cos;
                this.ty = (py * (1.0d - cos)) - (px * sin);
                if (this.is3D) {
                    this.mxz = 0.0d;
                    this.myz = 0.0d;
                    this.mzx = 0.0d;
                    this.mzy = 0.0d;
                    this.mzz = 1.0d;
                    this.tz = 0.0d;
                    this.is3D = false;
                }
                this.valid = true;
                return;
            }
            this.is3D = true;
            if (axis == Rotate.X_AXIS || axis == Rotate.Y_AXIS || axis == Rotate.Z_AXIS) {
                axisX = axis.getX();
                axisY = axis.getY();
                axisZ = axis.getZ();
            } else {
                double mag = Math.sqrt((axis.getX() * axis.getX()) + (axis.getY() * axis.getY()) + (axis.getZ() * axis.getZ()));
                if (mag == 0.0d) {
                    this.mxx = 1.0d;
                    this.mxy = 0.0d;
                    this.mxz = 0.0d;
                    this.tx = 0.0d;
                    this.myx = 0.0d;
                    this.myy = 1.0d;
                    this.myz = 0.0d;
                    this.ty = 0.0d;
                    this.mzx = 0.0d;
                    this.mzy = 0.0d;
                    this.mzz = 1.0d;
                    this.tz = 0.0d;
                    this.valid = true;
                    return;
                }
                axisX = axis.getX() / mag;
                axisY = axis.getY() / mag;
                axisZ = axis.getZ() / mag;
            }
            this.mxx = cos + (axisX * axisX * (1.0d - cos));
            this.mxy = ((axisX * axisY) * (1.0d - cos)) - (axisZ * sin);
            this.mxz = (axisX * axisZ * (1.0d - cos)) + (axisY * sin);
            this.tx = ((px * (1.0d - this.mxx)) - (py * this.mxy)) - (pz * this.mxz);
            this.myx = (axisY * axisX * (1.0d - cos)) + (axisZ * sin);
            this.myy = cos + (axisY * axisY * (1.0d - cos));
            this.myz = ((axisY * axisZ) * (1.0d - cos)) - (axisX * sin);
            this.ty = ((py * (1.0d - this.myy)) - (px * this.myx)) - (pz * this.myz);
            this.mzx = ((axisZ * axisX) * (1.0d - cos)) - (axisY * sin);
            this.mzy = (axisZ * axisY * (1.0d - cos)) + (axisX * sin);
            this.mzz = cos + (axisZ * axisZ * (1.0d - cos));
            this.tz = ((pz * (1.0d - this.mzz)) - (px * this.mzx)) - (py * this.mzy);
            this.valid = true;
        }

        public void invalidate() {
            this.valid = false;
        }
    }
}
