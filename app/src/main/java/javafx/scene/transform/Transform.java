package javafx.scene.transform;

import com.sun.javafx.binding.ExpressionHelper;
import com.sun.javafx.event.EventHandlerManager;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geometry.BoundsUtils;
import com.sun.javafx.scene.transform.TransformUtils;
import com.sun.javafx.util.WeakReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Iterator;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Node;

/* loaded from: jfxrt.jar:javafx/scene/transform/Transform.class */
public abstract class Transform implements Cloneable, EventTarget {
    private SoftReference<Transform> inverseCache = null;
    private WeakReferenceQueue impl_nodes = new WeakReferenceQueue();
    private LazyBooleanProperty type2D;
    private LazyBooleanProperty identity;
    private EventHandlerManager internalEventDispatcher;
    private ObjectProperty<EventHandler<? super TransformChangedEvent>> onTransformChanged;

    @Deprecated
    public abstract void impl_apply(Affine3D affine3D);

    @Deprecated
    public abstract BaseTransform impl_derive(BaseTransform baseTransform);

    public static Affine affine(double mxx, double myx, double mxy, double myy, double tx, double ty) {
        Affine affine = new Affine();
        affine.setMxx(mxx);
        affine.setMxy(mxy);
        affine.setTx(tx);
        affine.setMyx(myx);
        affine.setMyy(myy);
        affine.setTy(ty);
        return affine;
    }

    public static Affine affine(double mxx, double mxy, double mxz, double tx, double myx, double myy, double myz, double ty, double mzx, double mzy, double mzz, double tz) {
        Affine affine = new Affine();
        affine.setMxx(mxx);
        affine.setMxy(mxy);
        affine.setMxz(mxz);
        affine.setTx(tx);
        affine.setMyx(myx);
        affine.setMyy(myy);
        affine.setMyz(myz);
        affine.setTy(ty);
        affine.setMzx(mzx);
        affine.setMzy(mzy);
        affine.setMzz(mzz);
        affine.setTz(tz);
        return affine;
    }

    public static Translate translate(double x2, double y2) {
        Translate translate = new Translate();
        translate.setX(x2);
        translate.setY(y2);
        return translate;
    }

    public static Rotate rotate(double angle, double pivotX, double pivotY) {
        Rotate rotate = new Rotate();
        rotate.setAngle(angle);
        rotate.setPivotX(pivotX);
        rotate.setPivotY(pivotY);
        return rotate;
    }

    public static Scale scale(double x2, double y2) {
        Scale scale = new Scale();
        scale.setX(x2);
        scale.setY(y2);
        return scale;
    }

    public static Scale scale(double x2, double y2, double pivotX, double pivotY) {
        Scale scale = new Scale();
        scale.setX(x2);
        scale.setY(y2);
        scale.setPivotX(pivotX);
        scale.setPivotY(pivotY);
        return scale;
    }

    public static Shear shear(double x2, double y2) {
        Shear shear = new Shear();
        shear.setX(x2);
        shear.setY(y2);
        return shear;
    }

    public static Shear shear(double x2, double y2, double pivotX, double pivotY) {
        Shear shear = new Shear();
        shear.setX(x2);
        shear.setY(y2);
        shear.setPivotX(pivotX);
        shear.setPivotY(pivotY);
        return shear;
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

    public double getTx() {
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

    public double getTy() {
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

    public double getTz() {
        return 0.0d;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:200)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:61)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.processFallThroughCases(SwitchRegionMaker.java:105)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.processFallThroughCases(SwitchRegionMaker.java:105)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:101)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0110 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0112 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0114 A[RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public double getElement(javafx.scene.transform.MatrixType r6, int r7, int r8) {
        /*
            Method dump skipped, instructions count: 535
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: javafx.scene.transform.Transform.getElement(javafx.scene.transform.MatrixType, int, int):double");
    }

    boolean computeIs2D() {
        return getMxz() == 0.0d && getMzx() == 0.0d && getMzy() == 0.0d && getMzz() == 1.0d && getTz() == 0.0d;
    }

    boolean computeIsIdentity() {
        return getMxx() == 1.0d && getMxy() == 0.0d && getMxz() == 0.0d && getTx() == 0.0d && getMyx() == 0.0d && getMyy() == 1.0d && getMyz() == 0.0d && getTy() == 0.0d && getMzx() == 0.0d && getMzy() == 0.0d && getMzz() == 1.0d && getTz() == 0.0d;
    }

    public double determinant() {
        double myx = getMyx();
        double myy = getMyy();
        double myz = getMyz();
        double mzx = getMzx();
        double mzy = getMzy();
        double mzz = getMzz();
        return (getMxx() * ((myy * mzz) - (mzy * myz))) + (getMxy() * ((myz * mzx) - (mzz * myx))) + (getMxz() * ((myx * mzy) - (mzx * myy)));
    }

    public final boolean isType2D() {
        return this.type2D == null ? computeIs2D() : this.type2D.get();
    }

    public final ReadOnlyBooleanProperty type2DProperty() {
        if (this.type2D == null) {
            this.type2D = new LazyBooleanProperty() { // from class: javafx.scene.transform.Transform.1
                @Override // javafx.scene.transform.Transform.LazyBooleanProperty
                protected boolean computeValue() {
                    return Transform.this.computeIs2D();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Transform.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "type2D";
                }
            };
        }
        return this.type2D;
    }

    public final boolean isIdentity() {
        return this.identity == null ? computeIsIdentity() : this.identity.get();
    }

    public final ReadOnlyBooleanProperty identityProperty() {
        if (this.identity == null) {
            this.identity = new LazyBooleanProperty() { // from class: javafx.scene.transform.Transform.2
                @Override // javafx.scene.transform.Transform.LazyBooleanProperty
                protected boolean computeValue() {
                    return Transform.this.computeIsIdentity();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Transform.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "identity";
                }
            };
        }
        return this.identity;
    }

    /* loaded from: jfxrt.jar:javafx/scene/transform/Transform$LazyBooleanProperty.class */
    private static abstract class LazyBooleanProperty extends ReadOnlyBooleanProperty {
        private ExpressionHelper<Boolean> helper;
        private boolean valid;
        private boolean value;

        protected abstract boolean computeValue();

        private LazyBooleanProperty() {
        }

        @Override // javafx.beans.Observable
        public void addListener(InvalidationListener listener) {
            this.helper = ExpressionHelper.addListener(this.helper, this, listener);
        }

        @Override // javafx.beans.Observable
        public void removeListener(InvalidationListener listener) {
            this.helper = ExpressionHelper.removeListener(this.helper, listener);
        }

        @Override // javafx.beans.value.ObservableValue
        public void addListener(ChangeListener<? super Boolean> listener) {
            this.helper = ExpressionHelper.addListener(this.helper, this, listener);
        }

        @Override // javafx.beans.value.ObservableValue
        public void removeListener(ChangeListener<? super Boolean> listener) {
            this.helper = ExpressionHelper.removeListener(this.helper, listener);
        }

        @Override // javafx.beans.value.ObservableBooleanValue
        public boolean get() {
            if (!this.valid) {
                this.value = computeValue();
                this.valid = true;
            }
            return this.value;
        }

        public void invalidate() {
            if (this.valid) {
                this.valid = false;
                ExpressionHelper.fireValueChangedEvent(this.helper);
            }
        }
    }

    private double transformDiff(Transform t2, double x2, double y2) throws IllegalStateException {
        Point2D byThis = transform(x2, y2);
        Point2D byOther = t2.transform(x2, y2);
        return byThis.distance(byOther);
    }

    private double transformDiff(Transform t2, double x2, double y2, double z2) {
        Point3D byThis = transform(x2, y2, z2);
        Point3D byOther = t2.transform(x2, y2, z2);
        return byThis.distance(byOther);
    }

    public boolean similarTo(Transform transform, Bounds range, double maxDelta) {
        if (isType2D() && transform.isType2D()) {
            double cornerX = range.getMinX();
            double cornerY = range.getMinY();
            if (transformDiff(transform, cornerX, cornerY) > maxDelta) {
                return false;
            }
            double cornerY2 = range.getMaxY();
            if (transformDiff(transform, cornerX, cornerY2) > maxDelta) {
                return false;
            }
            double cornerX2 = range.getMaxX();
            double cornerY3 = range.getMinY();
            if (transformDiff(transform, cornerX2, cornerY3) > maxDelta) {
                return false;
            }
            double cornerY4 = range.getMaxY();
            if (transformDiff(transform, cornerX2, cornerY4) > maxDelta) {
                return false;
            }
            return true;
        }
        double cornerX3 = range.getMinX();
        double cornerY5 = range.getMinY();
        double cornerZ = range.getMinZ();
        if (transformDiff(transform, cornerX3, cornerY5, cornerZ) > maxDelta) {
            return false;
        }
        double cornerY6 = range.getMaxY();
        if (transformDiff(transform, cornerX3, cornerY6, cornerZ) > maxDelta) {
            return false;
        }
        double cornerX4 = range.getMaxX();
        double cornerY7 = range.getMinY();
        if (transformDiff(transform, cornerX4, cornerY7, cornerZ) > maxDelta) {
            return false;
        }
        double cornerY8 = range.getMaxY();
        if (transformDiff(transform, cornerX4, cornerY8, cornerZ) > maxDelta) {
            return false;
        }
        if (range.getDepth() != 0.0d) {
            double cornerX5 = range.getMinX();
            double cornerY9 = range.getMinY();
            double cornerZ2 = range.getMaxZ();
            if (transformDiff(transform, cornerX5, cornerY9, cornerZ2) > maxDelta) {
                return false;
            }
            double cornerY10 = range.getMaxY();
            if (transformDiff(transform, cornerX5, cornerY10, cornerZ2) > maxDelta) {
                return false;
            }
            double cornerX6 = range.getMaxX();
            double cornerY11 = range.getMinY();
            if (transformDiff(transform, cornerX6, cornerY11, cornerZ2) > maxDelta) {
                return false;
            }
            double cornerY12 = range.getMaxY();
            if (transformDiff(transform, cornerX6, cornerY12, cornerZ2) > maxDelta) {
                return false;
            }
            return true;
        }
        return true;
    }

    void fill2DArray(double[] array) {
        array[0] = getMxx();
        array[1] = getMxy();
        array[2] = getTx();
        array[3] = getMyx();
        array[4] = getMyy();
        array[5] = getTy();
    }

    void fill3DArray(double[] array) {
        array[0] = getMxx();
        array[1] = getMxy();
        array[2] = getMxz();
        array[3] = getTx();
        array[4] = getMyx();
        array[5] = getMyy();
        array[6] = getMyz();
        array[7] = getTy();
        array[8] = getMzx();
        array[9] = getMzy();
        array[10] = getMzz();
        array[11] = getTz();
    }

    public double[] toArray(MatrixType type, double[] array) throws IllegalArgumentException {
        checkRequestedMAT(type);
        if (array == null || array.length < type.elements()) {
            array = new double[type.elements()];
        }
        switch (type) {
            case MT_2D_3x3:
                array[6] = 0.0d;
                array[7] = 0.0d;
                array[8] = 1.0d;
            case MT_2D_2x3:
                fill2DArray(array);
                return array;
            case MT_3D_4x4:
                array[12] = 0.0d;
                array[13] = 0.0d;
                array[14] = 0.0d;
                array[15] = 1.0d;
            case MT_3D_3x4:
                fill3DArray(array);
                return array;
            default:
                throw new InternalError("Unsupported matrix type " + ((Object) type));
        }
    }

    public double[] toArray(MatrixType type) {
        return toArray(type, null);
    }

    public double[] row(MatrixType type, int row, double[] array) throws IllegalArgumentException {
        checkRequestedMAT(type);
        if (row < 0 || row >= type.rows()) {
            throw new IndexOutOfBoundsException("Cannot get row " + row + " from " + ((Object) type));
        }
        if (array == null || array.length < type.columns()) {
            array = new double[type.columns()];
        }
        switch (type) {
            case MT_2D_2x3:
            case MT_2D_3x3:
                switch (row) {
                    case 0:
                        array[0] = getMxx();
                        array[1] = getMxy();
                        array[2] = getTx();
                        break;
                    case 1:
                        array[0] = getMyx();
                        array[1] = getMyy();
                        array[2] = getTy();
                        break;
                    case 2:
                        array[0] = 0.0d;
                        array[1] = 0.0d;
                        array[2] = 1.0d;
                        break;
                }
            case MT_3D_3x4:
            case MT_3D_4x4:
                switch (row) {
                    case 0:
                        array[0] = getMxx();
                        array[1] = getMxy();
                        array[2] = getMxz();
                        array[3] = getTx();
                        break;
                    case 1:
                        array[0] = getMyx();
                        array[1] = getMyy();
                        array[2] = getMyz();
                        array[3] = getTy();
                        break;
                    case 2:
                        array[0] = getMzx();
                        array[1] = getMzy();
                        array[2] = getMzz();
                        array[3] = getTz();
                        break;
                    case 3:
                        array[0] = 0.0d;
                        array[1] = 0.0d;
                        array[2] = 0.0d;
                        array[3] = 1.0d;
                        break;
                }
            default:
                throw new InternalError("Unsupported row " + row + " of " + ((Object) type));
        }
        return array;
    }

    public double[] row(MatrixType type, int row) {
        return row(type, row, null);
    }

    public double[] column(MatrixType type, int column, double[] array) throws IllegalArgumentException {
        checkRequestedMAT(type);
        if (column < 0 || column >= type.columns()) {
            throw new IndexOutOfBoundsException("Cannot get row " + column + " from " + ((Object) type));
        }
        if (array == null || array.length < type.rows()) {
            array = new double[type.rows()];
        }
        switch (type) {
            case MT_2D_2x3:
                switch (column) {
                    case 0:
                        array[0] = getMxx();
                        array[1] = getMyx();
                        break;
                    case 1:
                        array[0] = getMxy();
                        array[1] = getMyy();
                        break;
                    case 2:
                        array[0] = getTx();
                        array[1] = getTy();
                        break;
                }
            case MT_2D_3x3:
                switch (column) {
                    case 0:
                        array[0] = getMxx();
                        array[1] = getMyx();
                        array[2] = 0.0d;
                        break;
                    case 1:
                        array[0] = getMxy();
                        array[1] = getMyy();
                        array[2] = 0.0d;
                        break;
                    case 2:
                        array[0] = getTx();
                        array[1] = getTy();
                        array[2] = 1.0d;
                        break;
                }
            case MT_3D_3x4:
                switch (column) {
                    case 0:
                        array[0] = getMxx();
                        array[1] = getMyx();
                        array[2] = getMzx();
                        break;
                    case 1:
                        array[0] = getMxy();
                        array[1] = getMyy();
                        array[2] = getMzy();
                        break;
                    case 2:
                        array[0] = getMxz();
                        array[1] = getMyz();
                        array[2] = getMzz();
                        break;
                    case 3:
                        array[0] = getTx();
                        array[1] = getTy();
                        array[2] = getTz();
                        break;
                }
            case MT_3D_4x4:
                switch (column) {
                    case 0:
                        array[0] = getMxx();
                        array[1] = getMyx();
                        array[2] = getMzx();
                        array[3] = 0.0d;
                        break;
                    case 1:
                        array[0] = getMxy();
                        array[1] = getMyy();
                        array[2] = getMzy();
                        array[3] = 0.0d;
                        break;
                    case 2:
                        array[0] = getMxz();
                        array[1] = getMyz();
                        array[2] = getMzz();
                        array[3] = 0.0d;
                        break;
                    case 3:
                        array[0] = getTx();
                        array[1] = getTy();
                        array[2] = getTz();
                        array[3] = 1.0d;
                        break;
                }
            default:
                throw new InternalError("Unsupported column " + column + " of " + ((Object) type));
        }
        return array;
    }

    public double[] column(MatrixType type, int column) {
        return column(type, column, null);
    }

    public Transform createConcatenation(Transform transform) {
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
        return new Affine((getMxx() * txx) + (getMxy() * tyx) + (getMxz() * tzx), (getMxx() * txy) + (getMxy() * tyy) + (getMxz() * tzy), (getMxx() * txz) + (getMxy() * tyz) + (getMxz() * tzz), (getMxx() * ttx) + (getMxy() * tty) + (getMxz() * ttz) + getTx(), (getMyx() * txx) + (getMyy() * tyx) + (getMyz() * tzx), (getMyx() * txy) + (getMyy() * tyy) + (getMyz() * tzy), (getMyx() * txz) + (getMyy() * tyz) + (getMyz() * tzz), (getMyx() * ttx) + (getMyy() * tty) + (getMyz() * ttz) + getTy(), (getMzx() * txx) + (getMzy() * tyx) + (getMzz() * tzx), (getMzx() * txy) + (getMzy() * tyy) + (getMzz() * tzy), (getMzx() * txz) + (getMzy() * tyz) + (getMzz() * tzz), (getMzx() * ttx) + (getMzy() * tty) + (getMzz() * ttz) + getTz());
    }

    public Transform createInverse() throws NonInvertibleTransformException {
        return getInverseCache().mo1183clone();
    }

    @Override // 
    /* renamed from: clone */
    public Transform mo1183clone() {
        return TransformUtils.immutableTransform(this);
    }

    public Point2D transform(double x2, double y2) throws IllegalStateException {
        ensureCanTransform2DPoint();
        return new Point2D((getMxx() * x2) + (getMxy() * y2) + getTx(), (getMyx() * x2) + (getMyy() * y2) + getTy());
    }

    public Point2D transform(Point2D point) {
        return transform(point.getX(), point.getY());
    }

    public Point3D transform(double x2, double y2, double z2) {
        return new Point3D((getMxx() * x2) + (getMxy() * y2) + (getMxz() * z2) + getTx(), (getMyx() * x2) + (getMyy() * y2) + (getMyz() * z2) + getTy(), (getMzx() * x2) + (getMzy() * y2) + (getMzz() * z2) + getTz());
    }

    public Point3D transform(Point3D point) {
        return transform(point.getX(), point.getY(), point.getZ());
    }

    public Bounds transform(Bounds bounds) throws IllegalStateException {
        if (isType2D() && bounds.getMinZ() == 0.0d && bounds.getMaxZ() == 0.0d) {
            Point2D p1 = transform(bounds.getMinX(), bounds.getMinY());
            Point2D p2 = transform(bounds.getMaxX(), bounds.getMinY());
            Point2D p3 = transform(bounds.getMaxX(), bounds.getMaxY());
            Point2D p4 = transform(bounds.getMinX(), bounds.getMaxY());
            return BoundsUtils.createBoundingBox(p1, p2, p3, p4);
        }
        Point3D p12 = transform(bounds.getMinX(), bounds.getMinY(), bounds.getMinZ());
        Point3D p22 = transform(bounds.getMinX(), bounds.getMinY(), bounds.getMaxZ());
        Point3D p32 = transform(bounds.getMinX(), bounds.getMaxY(), bounds.getMinZ());
        Point3D p42 = transform(bounds.getMinX(), bounds.getMaxY(), bounds.getMaxZ());
        Point3D p5 = transform(bounds.getMaxX(), bounds.getMaxY(), bounds.getMinZ());
        Point3D p6 = transform(bounds.getMaxX(), bounds.getMaxY(), bounds.getMaxZ());
        Point3D p7 = transform(bounds.getMaxX(), bounds.getMinY(), bounds.getMinZ());
        Point3D p8 = transform(bounds.getMaxX(), bounds.getMinY(), bounds.getMaxZ());
        return BoundsUtils.createBoundingBox(p12, p22, p32, p42, p5, p6, p7, p8);
    }

    void transform2DPointsImpl(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) {
        double xx = getMxx();
        double xy = getMxy();
        double tx = getTx();
        double yx = getMyx();
        double yy = getMyy();
        double ty = getTy();
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
                dstPts[i3] = (xx * x2) + (xy * y2) + tx;
                dstOff = dstOff2 + 1;
                dstPts[dstOff2] = (yx * x2) + (yy * y2) + ty;
            } else {
                return;
            }
        }
    }

    void transform3DPointsImpl(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) {
        double xx = getMxx();
        double xy = getMxy();
        double xz = getMxz();
        double tx = getTx();
        double yx = getMyx();
        double yy = getMyy();
        double yz = getMyz();
        double ty = getTy();
        double zx = getMzx();
        double zy = getMzy();
        double zz = getMzz();
        double tz = getTz();
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
                dstPts[i3] = (xx * x2) + (xy * y2) + (xz * z2) + tx;
                int dstOff3 = dstOff2 + 1;
                dstPts[dstOff2] = (yx * x2) + (yy * y2) + (yz * z2) + ty;
                dstOff = dstOff3 + 1;
                dstPts[dstOff3] = (zx * x2) + (zy * y2) + (zz * z2) + tz;
            } else {
                return;
            }
        }
    }

    public void transform2DPoints(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) {
        if (srcPts == null || dstPts == null) {
            throw new NullPointerException();
        }
        if (!isType2D()) {
            throw new IllegalStateException("Cannot transform 2D points with a 3D transform");
        }
        transform2DPointsImpl(srcPts, getFixedSrcOffset(srcPts, srcOff, dstPts, dstOff, numPts, 2), dstPts, dstOff, numPts);
    }

    public void transform3DPoints(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) {
        if (srcPts == null || dstPts == null) {
            throw new NullPointerException();
        }
        transform3DPointsImpl(srcPts, getFixedSrcOffset(srcPts, srcOff, dstPts, dstOff, numPts, 3), dstPts, dstOff, numPts);
    }

    public Point2D deltaTransform(double x2, double y2) throws IllegalStateException {
        ensureCanTransform2DPoint();
        return new Point2D((getMxx() * x2) + (getMxy() * y2), (getMyx() * x2) + (getMyy() * y2));
    }

    public Point2D deltaTransform(Point2D point) {
        return deltaTransform(point.getX(), point.getY());
    }

    public Point3D deltaTransform(double x2, double y2, double z2) {
        return new Point3D((getMxx() * x2) + (getMxy() * y2) + (getMxz() * z2), (getMyx() * x2) + (getMyy() * y2) + (getMyz() * z2), (getMzx() * x2) + (getMzy() * y2) + (getMzz() * z2));
    }

    public Point3D deltaTransform(Point3D point) {
        return deltaTransform(point.getX(), point.getY(), point.getZ());
    }

    public Point2D inverseTransform(double x2, double y2) throws IllegalStateException, NonInvertibleTransformException {
        ensureCanTransform2DPoint();
        return getInverseCache().transform(x2, y2);
    }

    public Point2D inverseTransform(Point2D point) throws NonInvertibleTransformException {
        return inverseTransform(point.getX(), point.getY());
    }

    public Point3D inverseTransform(double x2, double y2, double z2) throws NonInvertibleTransformException {
        return getInverseCache().transform(x2, y2, z2);
    }

    public Point3D inverseTransform(Point3D point) throws NonInvertibleTransformException {
        return inverseTransform(point.getX(), point.getY(), point.getZ());
    }

    public Bounds inverseTransform(Bounds bounds) throws IllegalStateException, NonInvertibleTransformException {
        if (isType2D() && bounds.getMinZ() == 0.0d && bounds.getMaxZ() == 0.0d) {
            Point2D p1 = inverseTransform(bounds.getMinX(), bounds.getMinY());
            Point2D p2 = inverseTransform(bounds.getMaxX(), bounds.getMinY());
            Point2D p3 = inverseTransform(bounds.getMaxX(), bounds.getMaxY());
            Point2D p4 = inverseTransform(bounds.getMinX(), bounds.getMaxY());
            return BoundsUtils.createBoundingBox(p1, p2, p3, p4);
        }
        Point3D p12 = inverseTransform(bounds.getMinX(), bounds.getMinY(), bounds.getMinZ());
        Point3D p22 = inverseTransform(bounds.getMinX(), bounds.getMinY(), bounds.getMaxZ());
        Point3D p32 = inverseTransform(bounds.getMinX(), bounds.getMaxY(), bounds.getMinZ());
        Point3D p42 = inverseTransform(bounds.getMinX(), bounds.getMaxY(), bounds.getMaxZ());
        Point3D p5 = inverseTransform(bounds.getMaxX(), bounds.getMaxY(), bounds.getMinZ());
        Point3D p6 = inverseTransform(bounds.getMaxX(), bounds.getMaxY(), bounds.getMaxZ());
        Point3D p7 = inverseTransform(bounds.getMaxX(), bounds.getMinY(), bounds.getMinZ());
        Point3D p8 = inverseTransform(bounds.getMaxX(), bounds.getMinY(), bounds.getMaxZ());
        return BoundsUtils.createBoundingBox(p12, p22, p32, p42, p5, p6, p7, p8);
    }

    void inverseTransform2DPointsImpl(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) throws NonInvertibleTransformException {
        getInverseCache().transform2DPointsImpl(srcPts, srcOff, dstPts, dstOff, numPts);
    }

    void inverseTransform3DPointsImpl(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) throws NonInvertibleTransformException {
        getInverseCache().transform3DPointsImpl(srcPts, srcOff, dstPts, dstOff, numPts);
    }

    public void inverseTransform2DPoints(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) throws NonInvertibleTransformException {
        if (srcPts == null || dstPts == null) {
            throw new NullPointerException();
        }
        if (!isType2D()) {
            throw new IllegalStateException("Cannot transform 2D points with a 3D transform");
        }
        inverseTransform2DPointsImpl(srcPts, getFixedSrcOffset(srcPts, srcOff, dstPts, dstOff, numPts, 2), dstPts, dstOff, numPts);
    }

    public void inverseTransform3DPoints(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) throws NonInvertibleTransformException {
        if (srcPts == null || dstPts == null) {
            throw new NullPointerException();
        }
        inverseTransform3DPointsImpl(srcPts, getFixedSrcOffset(srcPts, srcOff, dstPts, dstOff, numPts, 3), dstPts, dstOff, numPts);
    }

    public Point2D inverseDeltaTransform(double x2, double y2) throws IllegalStateException, NonInvertibleTransformException {
        ensureCanTransform2DPoint();
        return getInverseCache().deltaTransform(x2, y2);
    }

    public Point2D inverseDeltaTransform(Point2D point) throws NonInvertibleTransformException {
        return inverseDeltaTransform(point.getX(), point.getY());
    }

    public Point3D inverseDeltaTransform(double x2, double y2, double z2) throws NonInvertibleTransformException {
        return getInverseCache().deltaTransform(x2, y2, z2);
    }

    public Point3D inverseDeltaTransform(Point3D point) throws NonInvertibleTransformException {
        return inverseDeltaTransform(point.getX(), point.getY(), point.getZ());
    }

    private int getFixedSrcOffset(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts, int dimensions) {
        if (dstPts == srcPts && dstOff > srcOff && dstOff < srcOff + (numPts * dimensions)) {
            System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * dimensions);
            return dstOff;
        }
        return srcOff;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public EventHandlerManager getInternalEventDispatcher() {
        if (this.internalEventDispatcher == null) {
            this.internalEventDispatcher = new EventHandlerManager(this);
        }
        return this.internalEventDispatcher;
    }

    @Override // javafx.event.EventTarget
    public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
        return this.internalEventDispatcher == null ? tail : tail.append(getInternalEventDispatcher());
    }

    public final <T extends Event> void addEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        getInternalEventDispatcher().addEventHandler(eventType, eventHandler);
        validate();
    }

    public final <T extends Event> void removeEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        getInternalEventDispatcher().removeEventHandler(eventType, eventHandler);
    }

    public final <T extends Event> void addEventFilter(EventType<T> eventType, EventHandler<? super T> eventFilter) {
        getInternalEventDispatcher().addEventFilter(eventType, eventFilter);
        validate();
    }

    public final <T extends Event> void removeEventFilter(EventType<T> eventType, EventHandler<? super T> eventFilter) {
        getInternalEventDispatcher().removeEventFilter(eventType, eventFilter);
    }

    public final void setOnTransformChanged(EventHandler<? super TransformChangedEvent> value) {
        onTransformChangedProperty().set(value);
        validate();
    }

    public final EventHandler<? super TransformChangedEvent> getOnTransformChanged() {
        if (this.onTransformChanged == null) {
            return null;
        }
        return this.onTransformChanged.get();
    }

    public final ObjectProperty<EventHandler<? super TransformChangedEvent>> onTransformChangedProperty() {
        if (this.onTransformChanged == null) {
            this.onTransformChanged = new SimpleObjectProperty<EventHandler<? super TransformChangedEvent>>(this, "onTransformChanged") { // from class: javafx.scene.transform.Transform.3
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Transform.this.getInternalEventDispatcher().setEventHandler(TransformChangedEvent.TRANSFORM_CHANGED, get());
                }
            };
        }
        return this.onTransformChanged;
    }

    void checkRequestedMAT(MatrixType type) throws IllegalArgumentException {
        if (type.is2D() && !isType2D()) {
            throw new IllegalArgumentException("Cannot access 2D matrix for a 3D transform");
        }
    }

    void ensureCanTransform2DPoint() throws IllegalStateException {
        if (!isType2D()) {
            throw new IllegalStateException("Cannot transform 2D point with a 3D transform");
        }
    }

    void validate() {
        getMxx();
        getMxy();
        getMxz();
        getTx();
        getMyx();
        getMyy();
        getMyz();
        getTy();
        getMzx();
        getMzy();
        getMzz();
        getTz();
    }

    @Deprecated
    public void impl_add(Node node) {
        this.impl_nodes.add(node);
    }

    @Deprecated
    public void impl_remove(Node node) {
        this.impl_nodes.remove(node);
    }

    protected void transformChanged() {
        this.inverseCache = null;
        Iterator iterator = this.impl_nodes.iterator();
        while (iterator.hasNext()) {
            ((Node) iterator.next()).impl_transformsChanged();
        }
        if (this.type2D != null) {
            this.type2D.invalidate();
        }
        if (this.identity != null) {
            this.identity.invalidate();
        }
        if (this.internalEventDispatcher != null) {
            validate();
            Event.fireEvent(this, new TransformChangedEvent(this, this));
        }
    }

    void appendTo(Affine a2) {
        a2.append(getMxx(), getMxy(), getMxz(), getTx(), getMyx(), getMyy(), getMyz(), getTy(), getMzx(), getMzy(), getMzz(), getTz());
    }

    void prependTo(Affine a2) {
        a2.prepend(getMxx(), getMxy(), getMxz(), getTx(), getMyx(), getMyy(), getMyz(), getTy(), getMzx(), getMzy(), getMzz(), getTz());
    }

    private Transform getInverseCache() throws NonInvertibleTransformException {
        if (this.inverseCache == null || this.inverseCache.get() == null) {
            Affine inv = new Affine(getMxx(), getMxy(), getMxz(), getTx(), getMyx(), getMyy(), getMyz(), getTy(), getMzx(), getMzy(), getMzz(), getTz());
            inv.invert();
            this.inverseCache = new SoftReference<>(inv);
            return inv;
        }
        return this.inverseCache.get();
    }

    void clearInverseCache() {
        if (this.inverseCache != null) {
            this.inverseCache.clear();
        }
    }
}
