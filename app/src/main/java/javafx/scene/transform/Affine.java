package javafx.scene.transform;

import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

/* loaded from: jfxrt.jar:javafx/scene/transform/Affine.class */
public class Affine extends Transform {
    AffineAtomicChange atomicChange;
    private static final int APPLY_IDENTITY = 0;
    private static final int APPLY_TRANSLATE = 1;
    private static final int APPLY_SCALE = 2;
    private static final int APPLY_SHEAR = 4;
    private static final int APPLY_NON_3D = 0;
    private static final int APPLY_3D_COMPLEX = 4;
    private transient int state2d;
    private transient int state3d;
    private double xx;
    private double xy;
    private double xz;
    private double yx;
    private double yy;
    private double yz;
    private double zx;
    private double zy;
    private double zz;
    private double xt;
    private double yt;
    private double zt;
    private AffineElementProperty mxx;
    private AffineElementProperty mxy;
    private AffineElementProperty mxz;
    private AffineElementProperty tx;
    private AffineElementProperty myx;
    private AffineElementProperty myy;
    private AffineElementProperty myz;
    private AffineElementProperty ty;
    private AffineElementProperty mzx;
    private AffineElementProperty mzy;
    private AffineElementProperty mzz;
    private AffineElementProperty tz;
    private static final int[] rot90conversion = {4, 5, 4, 5, 2, 3, 6, 7};

    /* JADX WARN: Multi-variable type inference failed */
    public Affine() {
        this.atomicChange = new AffineAtomicChange();
        this.zz = 1.0d;
        this.yy = 1.0d;
        4607182418800017408.xx = this;
    }

    public Affine(Transform transform) {
        this(transform.getMxx(), transform.getMxy(), transform.getMxz(), transform.getTx(), transform.getMyx(), transform.getMyy(), transform.getMyz(), transform.getTy(), transform.getMzx(), transform.getMzy(), transform.getMzz(), transform.getTz());
    }

    public Affine(double mxx, double mxy, double tx, double myx, double myy, double ty) {
        this.atomicChange = new AffineAtomicChange();
        this.xx = mxx;
        this.xy = mxy;
        this.xt = tx;
        this.yx = myx;
        this.yy = myy;
        this.yt = ty;
        this.zz = 1.0d;
        updateState2D();
    }

    public Affine(double mxx, double mxy, double mxz, double tx, double myx, double myy, double myz, double ty, double mzx, double mzy, double mzz, double tz) {
        this.atomicChange = new AffineAtomicChange();
        this.xx = mxx;
        this.xy = mxy;
        this.xz = mxz;
        this.xt = tx;
        this.yx = myx;
        this.yy = myy;
        this.yz = myz;
        this.yt = ty;
        this.zx = mzx;
        this.zy = mzy;
        this.zz = mzz;
        this.zt = tz;
        updateState();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public Affine(double[] matrix, MatrixType type, int offset) {
        this.atomicChange = new AffineAtomicChange();
        if (matrix.length < offset + type.elements()) {
            throw new IndexOutOfBoundsException("The array is too short.");
        }
        switch (type) {
            case MT_2D_3x3:
                if (matrix[offset + 6] == 0.0d || matrix[offset + 7] != 0.0d || matrix[offset + 8] != 1.0d) {
                    throw new IllegalArgumentException("The matrix is not affine");
                }
                int offset2 = offset + 1;
                this.xx = matrix[offset];
                int offset3 = offset2 + 1;
                this.xy = matrix[offset2];
                int offset4 = offset3 + 1;
                this.xt = matrix[offset3];
                int offset5 = offset4 + 1;
                this.yx = matrix[offset4];
                this.yy = matrix[offset5];
                this.yt = matrix[offset5 + 1];
                this.zz = 1.0d;
                updateState2D();
                return;
            case MT_2D_2x3:
                int offset22 = offset + 1;
                this.xx = matrix[offset];
                int offset32 = offset22 + 1;
                this.xy = matrix[offset22];
                int offset42 = offset32 + 1;
                this.xt = matrix[offset32];
                int offset52 = offset42 + 1;
                this.yx = matrix[offset42];
                this.yy = matrix[offset52];
                this.yt = matrix[offset52 + 1];
                this.zz = 1.0d;
                updateState2D();
                return;
            case MT_3D_4x4:
                if (matrix[offset + 12] != 0.0d || matrix[offset + 13] != 0.0d || matrix[offset + 14] != 0.0d || matrix[offset + 15] != 1.0d) {
                    throw new IllegalArgumentException("The matrix is not affine");
                }
                int offset6 = offset + 1;
                this.xx = matrix[offset];
                int offset7 = offset6 + 1;
                this.xy = matrix[offset6];
                int offset8 = offset7 + 1;
                this.xz = matrix[offset7];
                int offset9 = offset8 + 1;
                this.xt = matrix[offset8];
                int offset10 = offset9 + 1;
                this.yx = matrix[offset9];
                int offset11 = offset10 + 1;
                this.yy = matrix[offset10];
                int offset12 = offset11 + 1;
                this.yz = matrix[offset11];
                int offset13 = offset12 + 1;
                this.yt = matrix[offset12];
                int offset14 = offset13 + 1;
                this.zx = matrix[offset13];
                int offset15 = offset14 + 1;
                this.zy = matrix[offset14];
                this.zz = matrix[offset15];
                this.zt = matrix[offset15 + 1];
                updateState();
                return;
            case MT_3D_3x4:
                int offset62 = offset + 1;
                this.xx = matrix[offset];
                int offset72 = offset62 + 1;
                this.xy = matrix[offset62];
                int offset82 = offset72 + 1;
                this.xz = matrix[offset72];
                int offset92 = offset82 + 1;
                this.xt = matrix[offset82];
                int offset102 = offset92 + 1;
                this.yx = matrix[offset92];
                int offset112 = offset102 + 1;
                this.yy = matrix[offset102];
                int offset122 = offset112 + 1;
                this.yz = matrix[offset112];
                int offset132 = offset122 + 1;
                this.yt = matrix[offset122];
                int offset142 = offset132 + 1;
                this.zx = matrix[offset132];
                int offset152 = offset142 + 1;
                this.zy = matrix[offset142];
                this.zz = matrix[offset152];
                this.zt = matrix[offset152 + 1];
                updateState();
                return;
            default:
                stateError();
                if (matrix[offset + 6] == 0.0d) {
                    break;
                }
                throw new IllegalArgumentException("The matrix is not affine");
        }
    }

    public final void setMxx(double value) {
        if (this.mxx != null) {
            mxxProperty().set(value);
        } else if (this.xx != value) {
            this.xx = value;
            postProcessChange();
        }
    }

    @Override // javafx.scene.transform.Transform
    public final double getMxx() {
        return this.mxx == null ? this.xx : this.mxx.get();
    }

    public final DoubleProperty mxxProperty() {
        if (this.mxx == null) {
            this.mxx = new AffineElementProperty(this.xx) { // from class: javafx.scene.transform.Affine.1
                @Override // javafx.beans.property.SimpleDoubleProperty, javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Affine.this;
                }

                @Override // javafx.beans.property.SimpleDoubleProperty, javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "mxx";
                }
            };
        }
        return this.mxx;
    }

    public final void setMxy(double value) {
        if (this.mxy != null) {
            mxyProperty().set(value);
        } else if (this.xy != value) {
            this.xy = value;
            postProcessChange();
        }
    }

    @Override // javafx.scene.transform.Transform
    public final double getMxy() {
        return this.mxy == null ? this.xy : this.mxy.get();
    }

    public final DoubleProperty mxyProperty() {
        if (this.mxy == null) {
            this.mxy = new AffineElementProperty(this.xy) { // from class: javafx.scene.transform.Affine.2
                @Override // javafx.beans.property.SimpleDoubleProperty, javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Affine.this;
                }

                @Override // javafx.beans.property.SimpleDoubleProperty, javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "mxy";
                }
            };
        }
        return this.mxy;
    }

    public final void setMxz(double value) {
        if (this.mxz != null) {
            mxzProperty().set(value);
        } else if (this.xz != value) {
            this.xz = value;
            postProcessChange();
        }
    }

    @Override // javafx.scene.transform.Transform
    public final double getMxz() {
        return this.mxz == null ? this.xz : this.mxz.get();
    }

    public final DoubleProperty mxzProperty() {
        if (this.mxz == null) {
            this.mxz = new AffineElementProperty(this.xz) { // from class: javafx.scene.transform.Affine.3
                @Override // javafx.beans.property.SimpleDoubleProperty, javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Affine.this;
                }

                @Override // javafx.beans.property.SimpleDoubleProperty, javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "mxz";
                }
            };
        }
        return this.mxz;
    }

    public final void setTx(double value) {
        if (this.tx != null) {
            txProperty().set(value);
        } else if (this.xt != value) {
            this.xt = value;
            postProcessChange();
        }
    }

    @Override // javafx.scene.transform.Transform
    public final double getTx() {
        return this.tx == null ? this.xt : this.tx.get();
    }

    public final DoubleProperty txProperty() {
        if (this.tx == null) {
            this.tx = new AffineElementProperty(this.xt) { // from class: javafx.scene.transform.Affine.4
                @Override // javafx.beans.property.SimpleDoubleProperty, javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Affine.this;
                }

                @Override // javafx.beans.property.SimpleDoubleProperty, javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "tx";
                }
            };
        }
        return this.tx;
    }

    public final void setMyx(double value) {
        if (this.myx != null) {
            myxProperty().set(value);
        } else if (this.yx != value) {
            this.yx = value;
            postProcessChange();
        }
    }

    @Override // javafx.scene.transform.Transform
    public final double getMyx() {
        return this.myx == null ? this.yx : this.myx.get();
    }

    public final DoubleProperty myxProperty() {
        if (this.myx == null) {
            this.myx = new AffineElementProperty(this.yx) { // from class: javafx.scene.transform.Affine.5
                @Override // javafx.beans.property.SimpleDoubleProperty, javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Affine.this;
                }

                @Override // javafx.beans.property.SimpleDoubleProperty, javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "myx";
                }
            };
        }
        return this.myx;
    }

    public final void setMyy(double value) {
        if (this.myy != null) {
            myyProperty().set(value);
        } else if (this.yy != value) {
            this.yy = value;
            postProcessChange();
        }
    }

    @Override // javafx.scene.transform.Transform
    public final double getMyy() {
        return this.myy == null ? this.yy : this.myy.get();
    }

    public final DoubleProperty myyProperty() {
        if (this.myy == null) {
            this.myy = new AffineElementProperty(this.yy) { // from class: javafx.scene.transform.Affine.6
                @Override // javafx.beans.property.SimpleDoubleProperty, javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Affine.this;
                }

                @Override // javafx.beans.property.SimpleDoubleProperty, javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "myy";
                }
            };
        }
        return this.myy;
    }

    public final void setMyz(double value) {
        if (this.myz != null) {
            myzProperty().set(value);
        } else if (this.yz != value) {
            this.yz = value;
            postProcessChange();
        }
    }

    @Override // javafx.scene.transform.Transform
    public final double getMyz() {
        return this.myz == null ? this.yz : this.myz.get();
    }

    public final DoubleProperty myzProperty() {
        if (this.myz == null) {
            this.myz = new AffineElementProperty(this.yz) { // from class: javafx.scene.transform.Affine.7
                @Override // javafx.beans.property.SimpleDoubleProperty, javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Affine.this;
                }

                @Override // javafx.beans.property.SimpleDoubleProperty, javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "myz";
                }
            };
        }
        return this.myz;
    }

    public final void setTy(double value) {
        if (this.ty != null) {
            tyProperty().set(value);
        } else if (this.yt != value) {
            this.yt = value;
            postProcessChange();
        }
    }

    @Override // javafx.scene.transform.Transform
    public final double getTy() {
        return this.ty == null ? this.yt : this.ty.get();
    }

    public final DoubleProperty tyProperty() {
        if (this.ty == null) {
            this.ty = new AffineElementProperty(this.yt) { // from class: javafx.scene.transform.Affine.8
                @Override // javafx.beans.property.SimpleDoubleProperty, javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Affine.this;
                }

                @Override // javafx.beans.property.SimpleDoubleProperty, javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "ty";
                }
            };
        }
        return this.ty;
    }

    public final void setMzx(double value) {
        if (this.mzx != null) {
            mzxProperty().set(value);
        } else if (this.zx != value) {
            this.zx = value;
            postProcessChange();
        }
    }

    @Override // javafx.scene.transform.Transform
    public final double getMzx() {
        return this.mzx == null ? this.zx : this.mzx.get();
    }

    public final DoubleProperty mzxProperty() {
        if (this.mzx == null) {
            this.mzx = new AffineElementProperty(this.zx) { // from class: javafx.scene.transform.Affine.9
                @Override // javafx.beans.property.SimpleDoubleProperty, javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Affine.this;
                }

                @Override // javafx.beans.property.SimpleDoubleProperty, javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "mzx";
                }
            };
        }
        return this.mzx;
    }

    public final void setMzy(double value) {
        if (this.mzy != null) {
            mzyProperty().set(value);
        } else if (this.zy != value) {
            this.zy = value;
            postProcessChange();
        }
    }

    @Override // javafx.scene.transform.Transform
    public final double getMzy() {
        return this.mzy == null ? this.zy : this.mzy.get();
    }

    public final DoubleProperty mzyProperty() {
        if (this.mzy == null) {
            this.mzy = new AffineElementProperty(this.zy) { // from class: javafx.scene.transform.Affine.10
                @Override // javafx.beans.property.SimpleDoubleProperty, javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Affine.this;
                }

                @Override // javafx.beans.property.SimpleDoubleProperty, javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "mzy";
                }
            };
        }
        return this.mzy;
    }

    public final void setMzz(double value) {
        if (this.mzz != null) {
            mzzProperty().set(value);
        } else if (this.zz != value) {
            this.zz = value;
            postProcessChange();
        }
    }

    @Override // javafx.scene.transform.Transform
    public final double getMzz() {
        return this.mzz == null ? this.zz : this.mzz.get();
    }

    public final DoubleProperty mzzProperty() {
        if (this.mzz == null) {
            this.mzz = new AffineElementProperty(this.zz) { // from class: javafx.scene.transform.Affine.11
                @Override // javafx.beans.property.SimpleDoubleProperty, javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Affine.this;
                }

                @Override // javafx.beans.property.SimpleDoubleProperty, javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "mzz";
                }
            };
        }
        return this.mzz;
    }

    public final void setTz(double value) {
        if (this.tz != null) {
            tzProperty().set(value);
        } else if (this.zt != value) {
            this.zt = value;
            postProcessChange();
        }
    }

    @Override // javafx.scene.transform.Transform
    public final double getTz() {
        return this.tz == null ? this.zt : this.tz.get();
    }

    public final DoubleProperty tzProperty() {
        if (this.tz == null) {
            this.tz = new AffineElementProperty(this.zt) { // from class: javafx.scene.transform.Affine.12
                @Override // javafx.beans.property.SimpleDoubleProperty, javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Affine.this;
                }

                @Override // javafx.beans.property.SimpleDoubleProperty, javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "tz";
                }
            };
        }
        return this.tz;
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
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:39:0x011c  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0124  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x012c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void setElement(javafx.scene.transform.MatrixType r7, int r8, int r9, double r10) {
        /*
            Method dump skipped, instructions count: 643
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: javafx.scene.transform.Affine.setElement(javafx.scene.transform.MatrixType, int, int, double):void");
    }

    /* loaded from: jfxrt.jar:javafx/scene/transform/Affine$AffineElementProperty.class */
    private class AffineElementProperty extends SimpleDoubleProperty {
        private boolean needsValueChangedEvent;
        private double oldValue;

        public AffineElementProperty(double initialValue) {
            super(initialValue);
            this.needsValueChangedEvent = false;
        }

        @Override // javafx.beans.property.DoublePropertyBase
        public void invalidated() {
            if (!Affine.this.atomicChange.runs()) {
                Affine.this.updateState();
                Affine.this.transformChanged();
            }
        }

        @Override // javafx.beans.property.DoublePropertyBase
        protected void fireValueChangedEvent() {
            if (!Affine.this.atomicChange.runs()) {
                super.fireValueChangedEvent();
            } else {
                this.needsValueChangedEvent = true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void preProcessAtomicChange() {
            this.oldValue = get();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void postProcessAtomicChange() {
            if (this.needsValueChangedEvent) {
                this.needsValueChangedEvent = false;
                if (this.oldValue != get()) {
                    super.fireValueChangedEvent();
                }
            }
        }
    }

    private void postProcessChange() {
        if (!this.atomicChange.runs()) {
            updateState();
            transformChanged();
        }
    }

    @Override // javafx.scene.transform.Transform
    boolean computeIs2D() {
        return this.state3d == 0;
    }

    @Override // javafx.scene.transform.Transform
    boolean computeIsIdentity() {
        return this.state3d == 0 && this.state2d == 0;
    }

    @Override // javafx.scene.transform.Transform
    public double determinant() {
        if (this.state3d == 0) {
            return getDeterminant2D();
        }
        return getDeterminant3D();
    }

    private double getDeterminant2D() {
        switch (this.state2d) {
            case 0:
            case 1:
                return 1.0d;
            case 2:
            case 3:
                return getMxx() * getMyy();
            case 4:
            case 5:
                return -(getMxy() * getMyx());
            case 6:
            case 7:
                break;
            default:
                stateError();
                break;
        }
        return (getMxx() * getMyy()) - (getMxy() * getMyx());
    }

    private double getDeterminant3D() {
        switch (this.state3d) {
            case 1:
                return 1.0d;
            case 2:
            case 3:
                return getMxx() * getMyy() * getMzz();
            case 4:
                double myx = getMyx();
                double myy = getMyy();
                double myz = getMyz();
                double mzx = getMzx();
                double mzy = getMzy();
                double mzz = getMzz();
                return (getMxx() * ((myy * mzz) - (mzy * myz))) + (getMxy() * ((myz * mzx) - (mzz * myx))) + (getMxz() * ((myx * mzy) - (mzx * myy)));
            default:
                stateError();
                return 1.0d;
        }
    }

    @Override // javafx.scene.transform.Transform
    public Transform createConcatenation(Transform transform) {
        Affine a2 = mo1183clone();
        a2.append(transform);
        return a2;
    }

    @Override // javafx.scene.transform.Transform
    public Affine createInverse() throws NonInvertibleTransformException {
        Affine t2 = mo1183clone();
        t2.invert();
        return t2;
    }

    @Override // javafx.scene.transform.Transform
    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public Affine mo1183clone() {
        return new Affine(this);
    }

    public void setToTransform(Transform transform) {
        setToTransform(transform.getMxx(), transform.getMxy(), transform.getMxz(), transform.getTx(), transform.getMyx(), transform.getMyy(), transform.getMyz(), transform.getTy(), transform.getMzx(), transform.getMzy(), transform.getMzz(), transform.getTz());
    }

    public void setToTransform(double mxx, double mxy, double tx, double myx, double myy, double ty) {
        setToTransform(mxx, mxy, 0.0d, tx, myx, myy, 0.0d, ty, 0.0d, 0.0d, 1.0d, 0.0d);
    }

    public void setToTransform(double mxx, double mxy, double mxz, double tx, double myx, double myy, double myz, double ty, double mzx, double mzy, double mzz, double tz) {
        this.atomicChange.start();
        setMxx(mxx);
        setMxy(mxy);
        setMxz(mxz);
        setTx(tx);
        setMyx(myx);
        setMyy(myy);
        setMyz(myz);
        setTy(ty);
        setMzx(mzx);
        setMzy(mzy);
        setMzz(mzz);
        setTz(tz);
        updateState();
        this.atomicChange.end();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public void setToTransform(double[] matrix, MatrixType type, int offset) {
        if (matrix.length < offset + type.elements()) {
            throw new IndexOutOfBoundsException("The array is too short.");
        }
        switch (type) {
            case MT_2D_3x3:
                if (matrix[offset + 6] == 0.0d || matrix[offset + 7] != 0.0d || matrix[offset + 8] != 1.0d) {
                    throw new IllegalArgumentException("The matrix is not affine");
                }
                int offset2 = offset + 1;
                double d2 = matrix[offset];
                int offset3 = offset2 + 1;
                double d3 = matrix[offset2];
                int offset4 = offset3 + 1;
                double d4 = matrix[offset3];
                int offset5 = offset4 + 1;
                double d5 = matrix[offset4];
                int offset6 = offset5 + 1;
                double d6 = matrix[offset5];
                int i2 = offset6 + 1;
                setToTransform(d2, d3, d4, d5, d6, matrix[offset6]);
                return;
            case MT_2D_2x3:
                int offset22 = offset + 1;
                double d22 = matrix[offset];
                int offset32 = offset22 + 1;
                double d32 = matrix[offset22];
                int offset42 = offset32 + 1;
                double d42 = matrix[offset32];
                int offset52 = offset42 + 1;
                double d52 = matrix[offset42];
                int offset62 = offset52 + 1;
                double d62 = matrix[offset52];
                int i22 = offset62 + 1;
                setToTransform(d22, d32, d42, d52, d62, matrix[offset62]);
                return;
            case MT_3D_4x4:
                if (matrix[offset + 12] != 0.0d || matrix[offset + 13] != 0.0d || matrix[offset + 14] != 0.0d || matrix[offset + 15] != 1.0d) {
                    throw new IllegalArgumentException("The matrix is not affine");
                }
                int offset7 = offset + 1;
                double d7 = matrix[offset];
                int offset8 = offset7 + 1;
                double d8 = matrix[offset7];
                int offset9 = offset8 + 1;
                double d9 = matrix[offset8];
                int offset10 = offset9 + 1;
                double d10 = matrix[offset9];
                int offset11 = offset10 + 1;
                double d11 = matrix[offset10];
                int offset12 = offset11 + 1;
                double d12 = matrix[offset11];
                int offset13 = offset12 + 1;
                double d13 = matrix[offset12];
                int offset14 = offset13 + 1;
                double d14 = matrix[offset13];
                int offset15 = offset14 + 1;
                double d15 = matrix[offset14];
                int offset16 = offset15 + 1;
                double d16 = matrix[offset15];
                int offset17 = offset16 + 1;
                double d17 = matrix[offset16];
                int i3 = offset17 + 1;
                setToTransform(d7, d8, d9, d10, d11, d12, d13, d14, d15, d16, d17, matrix[offset17]);
                return;
            case MT_3D_3x4:
                int offset72 = offset + 1;
                double d72 = matrix[offset];
                int offset82 = offset72 + 1;
                double d82 = matrix[offset72];
                int offset92 = offset82 + 1;
                double d92 = matrix[offset82];
                int offset102 = offset92 + 1;
                double d102 = matrix[offset92];
                int offset112 = offset102 + 1;
                double d112 = matrix[offset102];
                int offset122 = offset112 + 1;
                double d122 = matrix[offset112];
                int offset132 = offset122 + 1;
                double d132 = matrix[offset122];
                int offset142 = offset132 + 1;
                double d142 = matrix[offset132];
                int offset152 = offset142 + 1;
                double d152 = matrix[offset142];
                int offset162 = offset152 + 1;
                double d162 = matrix[offset152];
                int offset172 = offset162 + 1;
                double d172 = matrix[offset162];
                int i32 = offset172 + 1;
                setToTransform(d72, d82, d92, d102, d112, d122, d132, d142, d152, d162, d172, matrix[offset172]);
                return;
            default:
                stateError();
                if (matrix[offset + 6] == 0.0d) {
                    break;
                }
                throw new IllegalArgumentException("The matrix is not affine");
        }
    }

    public void setToIdentity() {
        this.atomicChange.start();
        if (this.state3d != 0) {
            setMxx(1.0d);
            setMxy(0.0d);
            setMxz(0.0d);
            setTx(0.0d);
            setMyx(0.0d);
            setMyy(1.0d);
            setMyz(0.0d);
            setTy(0.0d);
            setMzx(0.0d);
            setMzy(0.0d);
            setMzz(1.0d);
            setTz(0.0d);
            this.state3d = 0;
            this.state2d = 0;
        } else if (this.state2d != 0) {
            setMxx(1.0d);
            setMxy(0.0d);
            setTx(0.0d);
            setMyx(0.0d);
            setMyy(1.0d);
            setTy(0.0d);
            this.state2d = 0;
        }
        this.atomicChange.end();
    }

    public void invert() throws NonInvertibleTransformException {
        this.atomicChange.start();
        if (this.state3d == 0) {
            invert2D();
            updateState2D();
        } else {
            invert3D();
            updateState();
        }
        this.atomicChange.end();
    }

    private void invert2D() throws NonInvertibleTransformException {
        switch (this.state2d) {
            case 0:
                return;
            case 1:
                setTx(-getTx());
                setTy(-getTy());
                return;
            case 2:
                double Mxx = getMxx();
                double Myy = getMyy();
                if (Mxx != 0.0d && Myy != 0.0d) {
                    setMxx(1.0d / Mxx);
                    setMyy(1.0d / Myy);
                    return;
                } else {
                    this.atomicChange.cancel();
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
            case 3:
                double Mxx2 = getMxx();
                double Mxt = getTx();
                double Myy2 = getMyy();
                double Myt = getTy();
                if (Mxx2 != 0.0d && Myy2 != 0.0d) {
                    setMxx(1.0d / Mxx2);
                    setMyy(1.0d / Myy2);
                    setTx((-Mxt) / Mxx2);
                    setTy((-Myt) / Myy2);
                    return;
                }
                this.atomicChange.cancel();
                throw new NonInvertibleTransformException("Determinant is 0");
            case 4:
                double Mxy = getMxy();
                double Myx = getMyx();
                if (Mxy != 0.0d && Myx != 0.0d) {
                    setMyx(1.0d / Mxy);
                    setMxy(1.0d / Myx);
                    return;
                } else {
                    this.atomicChange.cancel();
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
            case 5:
                double Mxy2 = getMxy();
                double Mxt2 = getTx();
                double Myx2 = getMyx();
                double Myt2 = getTy();
                if (Mxy2 != 0.0d && Myx2 != 0.0d) {
                    setMyx(1.0d / Mxy2);
                    setMxy(1.0d / Myx2);
                    setTx((-Myt2) / Myx2);
                    setTy((-Mxt2) / Mxy2);
                    return;
                }
                this.atomicChange.cancel();
                throw new NonInvertibleTransformException("Determinant is 0");
            case 6:
                double Mxx3 = getMxx();
                double Mxy3 = getMxy();
                double Myx3 = getMyx();
                double Myy3 = getMyy();
                double det = getDeterminant2D();
                if (det != 0.0d) {
                    setMxx(Myy3 / det);
                    setMyx((-Myx3) / det);
                    setMxy((-Mxy3) / det);
                    setMyy(Mxx3 / det);
                    return;
                }
                this.atomicChange.cancel();
                throw new NonInvertibleTransformException("Determinant is 0");
            case 7:
                break;
            default:
                stateError();
                break;
        }
        double Mxx4 = getMxx();
        double Mxy4 = getMxy();
        double Mxt3 = getTx();
        double Myx4 = getMyx();
        double Myy4 = getMyy();
        double Myt3 = getTy();
        double det2 = getDeterminant2D();
        if (det2 != 0.0d) {
            setMxx(Myy4 / det2);
            setMyx((-Myx4) / det2);
            setMxy((-Mxy4) / det2);
            setMyy(Mxx4 / det2);
            setTx(((Mxy4 * Myt3) - (Myy4 * Mxt3)) / det2);
            setTy(((Myx4 * Mxt3) - (Mxx4 * Myt3)) / det2);
            return;
        }
        this.atomicChange.cancel();
        throw new NonInvertibleTransformException("Determinant is 0");
    }

    private void invert3D() throws NonInvertibleTransformException {
        switch (this.state3d) {
            case 1:
                break;
            case 2:
                double mxx_s = getMxx();
                double myy_s = getMyy();
                double mzz_s = getMzz();
                if (mxx_s != 0.0d && myy_s != 0.0d && mzz_s != 0.0d) {
                    setMxx(1.0d / mxx_s);
                    setMyy(1.0d / myy_s);
                    setMzz(1.0d / mzz_s);
                    return;
                }
                this.atomicChange.cancel();
                throw new NonInvertibleTransformException("Determinant is 0");
            case 3:
                double mxx_st = getMxx();
                double tx_st = getTx();
                double myy_st = getMyy();
                double ty_st = getTy();
                double mzz_st = getMzz();
                double tz_st = getTz();
                if (mxx_st != 0.0d && myy_st != 0.0d && mzz_st != 0.0d) {
                    setMxx(1.0d / mxx_st);
                    setMyy(1.0d / myy_st);
                    setMzz(1.0d / mzz_st);
                    setTx((-tx_st) / mxx_st);
                    setTy((-ty_st) / myy_st);
                    setTz((-tz_st) / mzz_st);
                    return;
                }
                this.atomicChange.cancel();
                throw new NonInvertibleTransformException("Determinant is 0");
            case 4:
                double mxx = getMxx();
                double mxy = getMxy();
                double mxz = getMxz();
                double tx = getTx();
                double myx = getMyx();
                double myy = getMyy();
                double myz = getMyz();
                double ty = getTy();
                double mzy = getMzy();
                double mzx = getMzx();
                double mzz = getMzz();
                double tz = getTz();
                double det = (mxx * ((myy * mzz) - (mzy * myz))) + (mxy * ((myz * mzx) - (mzz * myx))) + (mxz * ((myx * mzy) - (mzx * myy)));
                if (det != 0.0d) {
                    double cxx = (myy * mzz) - (myz * mzy);
                    double cyx = ((-myx) * mzz) + (myz * mzx);
                    double czx = (myx * mzy) - (myy * mzx);
                    double cxt = (((-mxy) * ((myz * tz) - (mzz * ty))) - (mxz * ((ty * mzy) - (tz * myy)))) - (tx * ((myy * mzz) - (mzy * myz)));
                    double cxy = ((-mxy) * mzz) + (mxz * mzy);
                    double cyy = (mxx * mzz) - (mxz * mzx);
                    double czy = ((-mxx) * mzy) + (mxy * mzx);
                    double cyt = (mxx * ((myz * tz) - (mzz * ty))) + (mxz * ((ty * mzx) - (tz * myx))) + (tx * ((myx * mzz) - (mzx * myz)));
                    double cxz = (mxy * myz) - (mxz * myy);
                    double cyz = ((-mxx) * myz) + (mxz * myx);
                    double czz = (mxx * myy) - (mxy * myx);
                    double czt = (((-mxx) * ((myy * tz) - (mzy * ty))) - (mxy * ((ty * mzx) - (tz * myx)))) - (tx * ((myx * mzy) - (mzx * myy)));
                    setMxx(cxx / det);
                    setMxy(cxy / det);
                    setMxz(cxz / det);
                    setTx(cxt / det);
                    setMyx(cyx / det);
                    setMyy(cyy / det);
                    setMyz(cyz / det);
                    setTy(cyt / det);
                    setMzx(czx / det);
                    setMzy(czy / det);
                    setMzz(czz / det);
                    setTz(czt / det);
                    return;
                }
                this.atomicChange.cancel();
                throw new NonInvertibleTransformException("Determinant is 0");
            default:
                stateError();
                break;
        }
        setTx(-getTx());
        setTy(-getTy());
        setTz(-getTz());
    }

    public void append(Transform transform) {
        transform.appendTo(this);
    }

    public void append(double mxx, double mxy, double tx, double myx, double myy, double ty) {
        if (this.state3d != 0) {
            append(mxx, mxy, 0.0d, tx, myx, myy, 0.0d, ty, 0.0d, 0.0d, 1.0d, 0.0d);
            return;
        }
        this.atomicChange.start();
        double m_xx = getMxx();
        double m_xy = getMxy();
        double m_yx = getMyx();
        double m_yy = getMyy();
        setMxx((m_xx * mxx) + (m_xy * myx));
        setMxy((m_xx * mxy) + (m_xy * myy));
        setTx((m_xx * tx) + (m_xy * ty) + getTx());
        setMyx((m_yx * mxx) + (m_yy * myx));
        setMyy((m_yx * mxy) + (m_yy * myy));
        setTy((m_yx * tx) + (m_yy * ty) + getTy());
        updateState();
        this.atomicChange.end();
    }

    public void append(double mxx, double mxy, double mxz, double tx, double myx, double myy, double myz, double ty, double mzx, double mzy, double mzz, double tz) {
        this.atomicChange.start();
        double m_xx = getMxx();
        double m_xy = getMxy();
        double m_xz = getMxz();
        double t_x = getTx();
        double m_yx = getMyx();
        double m_yy = getMyy();
        double m_yz = getMyz();
        double t_y = getTy();
        double m_zx = getMzx();
        double m_zy = getMzy();
        double m_zz = getMzz();
        double t_z = getTz();
        setMxx((m_xx * mxx) + (m_xy * myx) + (m_xz * mzx));
        setMxy((m_xx * mxy) + (m_xy * myy) + (m_xz * mzy));
        setMxz((m_xx * mxz) + (m_xy * myz) + (m_xz * mzz));
        setTx((m_xx * tx) + (m_xy * ty) + (m_xz * tz) + t_x);
        setMyx((m_yx * mxx) + (m_yy * myx) + (m_yz * mzx));
        setMyy((m_yx * mxy) + (m_yy * myy) + (m_yz * mzy));
        setMyz((m_yx * mxz) + (m_yy * myz) + (m_yz * mzz));
        setTy((m_yx * tx) + (m_yy * ty) + (m_yz * tz) + t_y);
        setMzx((m_zx * mxx) + (m_zy * myx) + (m_zz * mzx));
        setMzy((m_zx * mxy) + (m_zy * myy) + (m_zz * mzy));
        setMzz((m_zx * mxz) + (m_zy * myz) + (m_zz * mzz));
        setTz((m_zx * tx) + (m_zy * ty) + (m_zz * tz) + t_z);
        updateState();
        this.atomicChange.end();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public void append(double[] matrix, MatrixType type, int offset) {
        if (matrix.length < offset + type.elements()) {
            throw new IndexOutOfBoundsException("The array is too short.");
        }
        switch (type) {
            case MT_2D_3x3:
                if (matrix[offset + 6] == 0.0d || matrix[offset + 7] != 0.0d || matrix[offset + 8] != 1.0d) {
                    throw new IllegalArgumentException("The matrix is not affine");
                }
                int offset2 = offset + 1;
                double d2 = matrix[offset];
                int offset3 = offset2 + 1;
                double d3 = matrix[offset2];
                int offset4 = offset3 + 1;
                double d4 = matrix[offset3];
                int offset5 = offset4 + 1;
                double d5 = matrix[offset4];
                int offset6 = offset5 + 1;
                double d6 = matrix[offset5];
                int i2 = offset6 + 1;
                append(d2, d3, d4, d5, d6, matrix[offset6]);
                return;
            case MT_2D_2x3:
                int offset22 = offset + 1;
                double d22 = matrix[offset];
                int offset32 = offset22 + 1;
                double d32 = matrix[offset22];
                int offset42 = offset32 + 1;
                double d42 = matrix[offset32];
                int offset52 = offset42 + 1;
                double d52 = matrix[offset42];
                int offset62 = offset52 + 1;
                double d62 = matrix[offset52];
                int i22 = offset62 + 1;
                append(d22, d32, d42, d52, d62, matrix[offset62]);
                return;
            case MT_3D_4x4:
                if (matrix[offset + 12] != 0.0d || matrix[offset + 13] != 0.0d || matrix[offset + 14] != 0.0d || matrix[offset + 15] != 1.0d) {
                    throw new IllegalArgumentException("The matrix is not affine");
                }
                int offset7 = offset + 1;
                double d7 = matrix[offset];
                int offset8 = offset7 + 1;
                double d8 = matrix[offset7];
                int offset9 = offset8 + 1;
                double d9 = matrix[offset8];
                int offset10 = offset9 + 1;
                double d10 = matrix[offset9];
                int offset11 = offset10 + 1;
                double d11 = matrix[offset10];
                int offset12 = offset11 + 1;
                double d12 = matrix[offset11];
                int offset13 = offset12 + 1;
                double d13 = matrix[offset12];
                int offset14 = offset13 + 1;
                double d14 = matrix[offset13];
                int offset15 = offset14 + 1;
                double d15 = matrix[offset14];
                int offset16 = offset15 + 1;
                double d16 = matrix[offset15];
                int offset17 = offset16 + 1;
                double d17 = matrix[offset16];
                int i3 = offset17 + 1;
                append(d7, d8, d9, d10, d11, d12, d13, d14, d15, d16, d17, matrix[offset17]);
                return;
            case MT_3D_3x4:
                int offset72 = offset + 1;
                double d72 = matrix[offset];
                int offset82 = offset72 + 1;
                double d82 = matrix[offset72];
                int offset92 = offset82 + 1;
                double d92 = matrix[offset82];
                int offset102 = offset92 + 1;
                double d102 = matrix[offset92];
                int offset112 = offset102 + 1;
                double d112 = matrix[offset102];
                int offset122 = offset112 + 1;
                double d122 = matrix[offset112];
                int offset132 = offset122 + 1;
                double d132 = matrix[offset122];
                int offset142 = offset132 + 1;
                double d142 = matrix[offset132];
                int offset152 = offset142 + 1;
                double d152 = matrix[offset142];
                int offset162 = offset152 + 1;
                double d162 = matrix[offset152];
                int offset172 = offset162 + 1;
                double d172 = matrix[offset162];
                int i32 = offset172 + 1;
                append(d72, d82, d92, d102, d112, d122, d132, d142, d152, d162, d172, matrix[offset172]);
                return;
            default:
                stateError();
                if (matrix[offset + 6] == 0.0d) {
                    break;
                }
                throw new IllegalArgumentException("The matrix is not affine");
        }
    }

    @Override // javafx.scene.transform.Transform
    void appendTo(Affine a2) {
        switch (this.state3d) {
            case 0:
                break;
            case 1:
                a2.appendTranslation(getTx(), getTy(), getTz());
                return;
            case 2:
                a2.appendScale(getMxx(), getMyy(), getMzz());
                return;
            case 3:
                a2.appendTranslation(getTx(), getTy(), getTz());
                a2.appendScale(getMxx(), getMyy(), getMzz());
                return;
            case 4:
                a2.append(getMxx(), getMxy(), getMxz(), getTx(), getMyx(), getMyy(), getMyz(), getTy(), getMzx(), getMzy(), getMzz(), getTz());
                return;
            default:
                stateError();
                break;
        }
        switch (this.state2d) {
            case 0:
                break;
            case 1:
                a2.appendTranslation(getTx(), getTy());
                break;
            case 2:
                a2.appendScale(getMxx(), getMyy());
                break;
            case 3:
                a2.appendTranslation(getTx(), getTy());
                a2.appendScale(getMxx(), getMyy());
                break;
            default:
                a2.append(getMxx(), getMxy(), getTx(), getMyx(), getMyy(), getTy());
                break;
        }
    }

    public void prepend(Transform transform) {
        transform.prependTo(this);
    }

    public void prepend(double mxx, double mxy, double tx, double myx, double myy, double ty) {
        if (this.state3d != 0) {
            prepend(mxx, mxy, 0.0d, tx, myx, myy, 0.0d, ty, 0.0d, 0.0d, 1.0d, 0.0d);
            return;
        }
        this.atomicChange.start();
        double m_xx = getMxx();
        double m_xy = getMxy();
        double t_x = getTx();
        double m_yx = getMyx();
        double m_yy = getMyy();
        double t_y = getTy();
        setMxx((mxx * m_xx) + (mxy * m_yx));
        setMxy((mxx * m_xy) + (mxy * m_yy));
        setTx((mxx * t_x) + (mxy * t_y) + tx);
        setMyx((myx * m_xx) + (myy * m_yx));
        setMyy((myx * m_xy) + (myy * m_yy));
        setTy((myx * t_x) + (myy * t_y) + ty);
        updateState2D();
        this.atomicChange.end();
    }

    public void prepend(double mxx, double mxy, double mxz, double tx, double myx, double myy, double myz, double ty, double mzx, double mzy, double mzz, double tz) {
        this.atomicChange.start();
        double m_xx = getMxx();
        double m_xy = getMxy();
        double m_xz = getMxz();
        double t_x = getTx();
        double m_yx = getMyx();
        double m_yy = getMyy();
        double m_yz = getMyz();
        double t_y = getTy();
        double m_zx = getMzx();
        double m_zy = getMzy();
        double m_zz = getMzz();
        double t_z = getTz();
        setMxx((mxx * m_xx) + (mxy * m_yx) + (mxz * m_zx));
        setMxy((mxx * m_xy) + (mxy * m_yy) + (mxz * m_zy));
        setMxz((mxx * m_xz) + (mxy * m_yz) + (mxz * m_zz));
        setTx((mxx * t_x) + (mxy * t_y) + (mxz * t_z) + tx);
        setMyx((myx * m_xx) + (myy * m_yx) + (myz * m_zx));
        setMyy((myx * m_xy) + (myy * m_yy) + (myz * m_zy));
        setMyz((myx * m_xz) + (myy * m_yz) + (myz * m_zz));
        setTy((myx * t_x) + (myy * t_y) + (myz * t_z) + ty);
        setMzx((mzx * m_xx) + (mzy * m_yx) + (mzz * m_zx));
        setMzy((mzx * m_xy) + (mzy * m_yy) + (mzz * m_zy));
        setMzz((mzx * m_xz) + (mzy * m_yz) + (mzz * m_zz));
        setTz((mzx * t_x) + (mzy * t_y) + (mzz * t_z) + tz);
        updateState();
        this.atomicChange.end();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public void prepend(double[] matrix, MatrixType type, int offset) {
        if (matrix.length < offset + type.elements()) {
            throw new IndexOutOfBoundsException("The array is too short.");
        }
        switch (type) {
            case MT_2D_3x3:
                if (matrix[offset + 6] == 0.0d || matrix[offset + 7] != 0.0d || matrix[offset + 8] != 1.0d) {
                    throw new IllegalArgumentException("The matrix is not affine");
                }
                int offset2 = offset + 1;
                double d2 = matrix[offset];
                int offset3 = offset2 + 1;
                double d3 = matrix[offset2];
                int offset4 = offset3 + 1;
                double d4 = matrix[offset3];
                int offset5 = offset4 + 1;
                double d5 = matrix[offset4];
                int offset6 = offset5 + 1;
                double d6 = matrix[offset5];
                int i2 = offset6 + 1;
                prepend(d2, d3, d4, d5, d6, matrix[offset6]);
                return;
            case MT_2D_2x3:
                int offset22 = offset + 1;
                double d22 = matrix[offset];
                int offset32 = offset22 + 1;
                double d32 = matrix[offset22];
                int offset42 = offset32 + 1;
                double d42 = matrix[offset32];
                int offset52 = offset42 + 1;
                double d52 = matrix[offset42];
                int offset62 = offset52 + 1;
                double d62 = matrix[offset52];
                int i22 = offset62 + 1;
                prepend(d22, d32, d42, d52, d62, matrix[offset62]);
                return;
            case MT_3D_4x4:
                if (matrix[offset + 12] != 0.0d || matrix[offset + 13] != 0.0d || matrix[offset + 14] != 0.0d || matrix[offset + 15] != 1.0d) {
                    throw new IllegalArgumentException("The matrix is not affine");
                }
                int offset7 = offset + 1;
                double d7 = matrix[offset];
                int offset8 = offset7 + 1;
                double d8 = matrix[offset7];
                int offset9 = offset8 + 1;
                double d9 = matrix[offset8];
                int offset10 = offset9 + 1;
                double d10 = matrix[offset9];
                int offset11 = offset10 + 1;
                double d11 = matrix[offset10];
                int offset12 = offset11 + 1;
                double d12 = matrix[offset11];
                int offset13 = offset12 + 1;
                double d13 = matrix[offset12];
                int offset14 = offset13 + 1;
                double d14 = matrix[offset13];
                int offset15 = offset14 + 1;
                double d15 = matrix[offset14];
                int offset16 = offset15 + 1;
                double d16 = matrix[offset15];
                int offset17 = offset16 + 1;
                double d17 = matrix[offset16];
                int i3 = offset17 + 1;
                prepend(d7, d8, d9, d10, d11, d12, d13, d14, d15, d16, d17, matrix[offset17]);
                return;
            case MT_3D_3x4:
                int offset72 = offset + 1;
                double d72 = matrix[offset];
                int offset82 = offset72 + 1;
                double d82 = matrix[offset72];
                int offset92 = offset82 + 1;
                double d92 = matrix[offset82];
                int offset102 = offset92 + 1;
                double d102 = matrix[offset92];
                int offset112 = offset102 + 1;
                double d112 = matrix[offset102];
                int offset122 = offset112 + 1;
                double d122 = matrix[offset112];
                int offset132 = offset122 + 1;
                double d132 = matrix[offset122];
                int offset142 = offset132 + 1;
                double d142 = matrix[offset132];
                int offset152 = offset142 + 1;
                double d152 = matrix[offset142];
                int offset162 = offset152 + 1;
                double d162 = matrix[offset152];
                int offset172 = offset162 + 1;
                double d172 = matrix[offset162];
                int i32 = offset172 + 1;
                prepend(d72, d82, d92, d102, d112, d122, d132, d142, d152, d162, d172, matrix[offset172]);
                return;
            default:
                stateError();
                if (matrix[offset + 6] == 0.0d) {
                    break;
                }
                throw new IllegalArgumentException("The matrix is not affine");
        }
    }

    @Override // javafx.scene.transform.Transform
    void prependTo(Affine a2) {
        switch (this.state3d) {
            case 0:
                break;
            case 1:
                a2.prependTranslation(getTx(), getTy(), getTz());
                return;
            case 2:
                a2.prependScale(getMxx(), getMyy(), getMzz());
                return;
            case 3:
                a2.prependScale(getMxx(), getMyy(), getMzz());
                a2.prependTranslation(getTx(), getTy(), getTz());
                return;
            case 4:
                a2.prepend(getMxx(), getMxy(), getMxz(), getTx(), getMyx(), getMyy(), getMyz(), getTy(), getMzx(), getMzy(), getMzz(), getTz());
                return;
            default:
                stateError();
                break;
        }
        switch (this.state2d) {
            case 0:
                break;
            case 1:
                a2.prependTranslation(getTx(), getTy());
                break;
            case 2:
                a2.prependScale(getMxx(), getMyy());
                break;
            case 3:
                a2.prependScale(getMxx(), getMyy());
                a2.prependTranslation(getTx(), getTy());
                break;
            default:
                a2.prepend(getMxx(), getMxy(), getTx(), getMyx(), getMyy(), getTy());
                break;
        }
    }

    public void appendTranslation(double tx, double ty) {
        this.atomicChange.start();
        translate2D(tx, ty);
        this.atomicChange.end();
    }

    public void appendTranslation(double tx, double ty, double tz) {
        this.atomicChange.start();
        translate3D(tx, ty, tz);
        this.atomicChange.end();
    }

    private void translate2D(double tx, double ty) {
        if (this.state3d != 0) {
            translate3D(tx, ty, 0.0d);
            return;
        }
        switch (this.state2d) {
            case 0:
                setTx(tx);
                setTy(ty);
                if (tx != 0.0d || ty != 0.0d) {
                    this.state2d = 1;
                    return;
                }
                return;
            case 1:
                setTx(tx + getTx());
                setTy(ty + getTy());
                if (getTx() == 0.0d && getTy() == 0.0d) {
                    this.state2d = 0;
                    return;
                }
                return;
            case 2:
                setTx(tx * getMxx());
                setTy(ty * getMyy());
                if (getTx() != 0.0d || getTy() != 0.0d) {
                    this.state2d = 3;
                    return;
                }
                return;
            case 3:
                setTx((tx * getMxx()) + getTx());
                setTy((ty * getMyy()) + getTy());
                if (getTx() == 0.0d && getTy() == 0.0d) {
                    this.state2d = 2;
                    return;
                }
                return;
            case 4:
                setTx(ty * getMxy());
                setTy(tx * getMyx());
                if (getTx() != 0.0d || getTy() != 0.0d) {
                    this.state2d = 5;
                    return;
                }
                return;
            case 5:
                setTx((ty * getMxy()) + getTx());
                setTy((tx * getMyx()) + getTy());
                if (getTx() == 0.0d && getTy() == 0.0d) {
                    this.state2d = 4;
                    return;
                }
                return;
            case 6:
                setTx((tx * getMxx()) + (ty * getMxy()));
                setTy((tx * getMyx()) + (ty * getMyy()));
                if (getTx() != 0.0d || getTy() != 0.0d) {
                    this.state2d = 7;
                    return;
                }
                return;
            case 7:
                break;
            default:
                stateError();
                break;
        }
        setTx((tx * getMxx()) + (ty * getMxy()) + getTx());
        setTy((tx * getMyx()) + (ty * getMyy()) + getTy());
        if (getTx() == 0.0d && getTy() == 0.0d) {
            this.state2d = 6;
        }
    }

    private void translate3D(double tx, double ty, double tz) {
        switch (this.state3d) {
            case 0:
                break;
            case 1:
                setTx(tx + getTx());
                setTy(ty + getTy());
                setTz(tz + getTz());
                if (getTz() == 0.0d) {
                    this.state3d = 0;
                    if (getTx() == 0.0d && getTy() == 0.0d) {
                        this.state2d = 0;
                        return;
                    } else {
                        this.state2d = 1;
                        return;
                    }
                }
                return;
            case 2:
                setTx(tx * getMxx());
                setTy(ty * getMyy());
                setTz(tz * getMzz());
                if (getTx() != 0.0d || getTy() != 0.0d || getTz() != 0.0d) {
                    this.state3d |= 1;
                    return;
                }
                return;
            case 3:
                setTx((tx * getMxx()) + getTx());
                setTy((ty * getMyy()) + getTy());
                setTz((tz * getMzz()) + getTz());
                if (getTz() == 0.0d) {
                    if (getTx() == 0.0d && getTy() == 0.0d) {
                        this.state3d = 2;
                    }
                    if (getMzz() == 1.0d) {
                        this.state2d = this.state3d;
                        this.state3d = 0;
                        return;
                    }
                    return;
                }
                return;
            case 4:
                setTx((tx * getMxx()) + (ty * getMxy()) + (tz * getMxz()) + getTx());
                setTy((tx * getMyx()) + (ty * getMyy()) + (tz * getMyz()) + getTy());
                setTz((tx * getMzx()) + (ty * getMzy()) + (tz * getMzz()) + getTz());
                updateState();
                return;
            default:
                stateError();
                break;
        }
        translate2D(tx, ty);
        if (tz != 0.0d) {
            setTz(tz);
            if ((this.state2d & 4) == 0) {
                this.state3d = (this.state2d & 2) | 1;
            } else {
                this.state3d = 4;
            }
        }
    }

    public void prependTranslation(double tx, double ty, double tz) {
        this.atomicChange.start();
        preTranslate3D(tx, ty, tz);
        this.atomicChange.end();
    }

    public void prependTranslation(double tx, double ty) {
        this.atomicChange.start();
        preTranslate2D(tx, ty);
        this.atomicChange.end();
    }

    private void preTranslate2D(double tx, double ty) {
        if (this.state3d != 0) {
            preTranslate3D(tx, ty, 0.0d);
            return;
        }
        setTx(getTx() + tx);
        setTy(getTy() + ty);
        if (getTx() == 0.0d && getTy() == 0.0d) {
            this.state2d &= -2;
        } else {
            this.state2d |= 1;
        }
    }

    private void preTranslate3D(double tx, double ty, double tz) {
        switch (this.state3d) {
            case 0:
                break;
            case 1:
                setTx(getTx() + tx);
                setTy(getTy() + ty);
                setTz(getTz() + tz);
                if (getTz() == 0.0d) {
                    this.state3d = 0;
                    if (getTx() == 0.0d && getTy() == 0.0d) {
                        this.state2d = 0;
                        return;
                    } else {
                        this.state2d = 1;
                        return;
                    }
                }
                return;
            case 2:
                setTx(tx);
                setTy(ty);
                setTz(tz);
                if (tx != 0.0d || ty != 0.0d || tz != 0.0d) {
                    this.state3d |= 1;
                    return;
                }
                return;
            case 3:
                setTx(getTx() + tx);
                setTy(getTy() + ty);
                setTz(getTz() + tz);
                if (getTz() == 0.0d) {
                    if (getTx() == 0.0d && getTy() == 0.0d) {
                        this.state3d = 2;
                    }
                    if (getMzz() == 1.0d) {
                        this.state2d = this.state3d;
                        this.state3d = 0;
                        return;
                    }
                    return;
                }
                return;
            case 4:
                setTx(getTx() + tx);
                setTy(getTy() + ty);
                setTz(getTz() + tz);
                if (getTz() == 0.0d && getMxz() == 0.0d && getMyz() == 0.0d && getMzx() == 0.0d && getMzy() == 0.0d && getMzz() == 1.0d) {
                    this.state3d = 0;
                    updateState2D();
                    return;
                }
                return;
            default:
                stateError();
                break;
        }
        preTranslate2D(tx, ty);
        if (tz != 0.0d) {
            setTz(tz);
            if ((this.state2d & 4) == 0) {
                this.state3d = (this.state2d & 2) | 1;
            } else {
                this.state3d = 4;
            }
        }
    }

    public void appendScale(double sx, double sy) {
        this.atomicChange.start();
        scale2D(sx, sy);
        this.atomicChange.end();
    }

    public void appendScale(double sx, double sy, double pivotX, double pivotY) {
        this.atomicChange.start();
        if (pivotX != 0.0d || pivotY != 0.0d) {
            translate2D(pivotX, pivotY);
            scale2D(sx, sy);
            translate2D(-pivotX, -pivotY);
        } else {
            scale2D(sx, sy);
        }
        this.atomicChange.end();
    }

    public void appendScale(double sx, double sy, Point2D pivot) {
        appendScale(sx, sy, pivot.getX(), pivot.getY());
    }

    public void appendScale(double sx, double sy, double sz) {
        this.atomicChange.start();
        scale3D(sx, sy, sz);
        this.atomicChange.end();
    }

    public void appendScale(double sx, double sy, double sz, double pivotX, double pivotY, double pivotZ) {
        this.atomicChange.start();
        if (pivotX != 0.0d || pivotY != 0.0d || pivotZ != 0.0d) {
            translate3D(pivotX, pivotY, pivotZ);
            scale3D(sx, sy, sz);
            translate3D(-pivotX, -pivotY, -pivotZ);
        } else {
            scale3D(sx, sy, sz);
        }
        this.atomicChange.end();
    }

    public void appendScale(double sx, double sy, double sz, Point3D pivot) {
        appendScale(sx, sy, sz, pivot.getX(), pivot.getY(), pivot.getZ());
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0078  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x00b1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void scale2D(double r9, double r11) {
        /*
            Method dump skipped, instructions count: 279
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: javafx.scene.transform.Affine.scale2D(double, double):void");
    }

    private void scale3D(double sx, double sy, double sz) {
        switch (this.state3d) {
            case 0:
                break;
            case 1:
                setMxx(sx);
                setMyy(sy);
                setMzz(sz);
                if (sx != 1.0d || sy != 1.0d || sz != 1.0d) {
                    this.state3d |= 2;
                    return;
                }
                return;
            case 2:
                setMxx(getMxx() * sx);
                setMyy(getMyy() * sy);
                setMzz(getMzz() * sz);
                if (getMzz() == 1.0d) {
                    this.state3d = 0;
                    if (getMxx() == 1.0d && getMyy() == 1.0d) {
                        this.state2d = 0;
                        return;
                    } else {
                        this.state2d = 2;
                        return;
                    }
                }
                return;
            case 3:
                setMxx(getMxx() * sx);
                setMyy(getMyy() * sy);
                setMzz(getMzz() * sz);
                if (getMxx() == 1.0d && getMyy() == 1.0d && getMzz() == 1.0d) {
                    this.state3d &= -3;
                }
                if (getTz() == 0.0d && getMzz() == 1.0d) {
                    this.state2d = this.state3d;
                    this.state3d = 0;
                    return;
                }
                return;
            case 4:
                setMxx(getMxx() * sx);
                setMxy(getMxy() * sy);
                setMxz(getMxz() * sz);
                setMyx(getMyx() * sx);
                setMyy(getMyy() * sy);
                setMyz(getMyz() * sz);
                setMzx(getMzx() * sx);
                setMzy(getMzy() * sy);
                setMzz(getMzz() * sz);
                if (sx == 0.0d || sy == 0.0d || sz == 0.0d) {
                    updateState();
                    return;
                }
                return;
            default:
                stateError();
                break;
        }
        scale2D(sx, sy);
        if (sz != 1.0d) {
            setMzz(sz);
            if ((this.state2d & 4) == 0) {
                this.state3d = (this.state2d & 1) | 2;
            } else {
                this.state3d = 4;
            }
        }
    }

    public void prependScale(double sx, double sy) {
        this.atomicChange.start();
        preScale2D(sx, sy);
        this.atomicChange.end();
    }

    public void prependScale(double sx, double sy, double pivotX, double pivotY) {
        this.atomicChange.start();
        if (pivotX != 0.0d || pivotY != 0.0d) {
            preTranslate2D(-pivotX, -pivotY);
            preScale2D(sx, sy);
            preTranslate2D(pivotX, pivotY);
        } else {
            preScale2D(sx, sy);
        }
        this.atomicChange.end();
    }

    public void prependScale(double sx, double sy, Point2D pivot) {
        prependScale(sx, sy, pivot.getX(), pivot.getY());
    }

    public void prependScale(double sx, double sy, double sz) {
        this.atomicChange.start();
        preScale3D(sx, sy, sz);
        this.atomicChange.end();
    }

    public void prependScale(double sx, double sy, double sz, double pivotX, double pivotY, double pivotZ) {
        this.atomicChange.start();
        if (pivotX != 0.0d || pivotY != 0.0d || pivotZ != 0.0d) {
            preTranslate3D(-pivotX, -pivotY, -pivotZ);
            preScale3D(sx, sy, sz);
            preTranslate3D(pivotX, pivotY, pivotZ);
        } else {
            preScale3D(sx, sy, sz);
        }
        this.atomicChange.end();
    }

    public void prependScale(double sx, double sy, double sz, Point3D pivot) {
        prependScale(sx, sy, sz, pivot.getX(), pivot.getY(), pivot.getZ());
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:17:0x00ab  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void preScale2D(double r9, double r11) {
        /*
            Method dump skipped, instructions count: 516
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: javafx.scene.transform.Affine.preScale2D(double, double):void");
    }

    private void preScale3D(double sx, double sy, double sz) {
        switch (this.state3d) {
            case 0:
                break;
            case 1:
                setTx(getTx() * sx);
                setTy(getTy() * sy);
                setTz(getTz() * sz);
                setMxx(sx);
                setMyy(sy);
                setMzz(sz);
                if (getTx() == 0.0d && getTy() == 0.0d && getTz() == 0.0d) {
                    this.state3d &= -2;
                }
                if (sx != 1.0d || sy != 1.0d || sz != 1.0d) {
                    this.state3d |= 2;
                    return;
                }
                return;
            case 2:
                setMxx(getMxx() * sx);
                setMyy(getMyy() * sy);
                setMzz(getMzz() * sz);
                if (getMzz() == 1.0d) {
                    this.state3d = 0;
                    if (getMxx() == 1.0d && getMyy() == 1.0d) {
                        this.state2d = 0;
                        return;
                    } else {
                        this.state2d = 2;
                        return;
                    }
                }
                return;
            case 3:
                setTx(getTx() * sx);
                setTy(getTy() * sy);
                setTz(getTz() * sz);
                setMxx(getMxx() * sx);
                setMyy(getMyy() * sy);
                setMzz(getMzz() * sz);
                if (getTx() == 0.0d && getTy() == 0.0d && getTz() == 0.0d) {
                    this.state3d &= -2;
                }
                if (getMxx() == 1.0d && getMyy() == 1.0d && getMzz() == 1.0d) {
                    this.state3d &= -3;
                }
                if (getTz() == 0.0d && getMzz() == 1.0d) {
                    this.state2d = this.state3d;
                    this.state3d = 0;
                    return;
                }
                return;
            case 4:
                setMxx(getMxx() * sx);
                setMxy(getMxy() * sx);
                setMxz(getMxz() * sx);
                setTx(getTx() * sx);
                setMyx(getMyx() * sy);
                setMyy(getMyy() * sy);
                setMyz(getMyz() * sy);
                setTy(getTy() * sy);
                setMzx(getMzx() * sz);
                setMzy(getMzy() * sz);
                setMzz(getMzz() * sz);
                setTz(getTz() * sz);
                if (sx == 0.0d || sy == 0.0d || sz == 0.0d) {
                    updateState();
                    return;
                }
                return;
            default:
                stateError();
                break;
        }
        preScale2D(sx, sy);
        if (sz != 1.0d) {
            setMzz(sz);
            if ((this.state2d & 4) == 0) {
                this.state3d = (this.state2d & 1) | 2;
            } else {
                this.state3d = 4;
            }
        }
    }

    public void appendShear(double shx, double shy) {
        this.atomicChange.start();
        shear2D(shx, shy);
        this.atomicChange.end();
    }

    public void appendShear(double shx, double shy, double pivotX, double pivotY) {
        this.atomicChange.start();
        if (pivotX != 0.0d || pivotY != 0.0d) {
            translate2D(pivotX, pivotY);
            shear2D(shx, shy);
            translate2D(-pivotX, -pivotY);
        } else {
            shear2D(shx, shy);
        }
        this.atomicChange.end();
    }

    public void appendShear(double shx, double shy, Point2D pivot) {
        appendShear(shx, shy, pivot.getX(), pivot.getY());
    }

    private void shear2D(double shx, double shy) {
        if (this.state3d != 0) {
            shear3D(shx, shy);
            return;
        }
        int mystate = this.state2d;
        switch (mystate) {
            case 0:
            case 1:
                setMxy(shx);
                setMyx(shy);
                if (getMxy() != 0.0d || getMyx() != 0.0d) {
                    this.state2d = mystate | 2 | 4;
                    return;
                }
                return;
            case 2:
            case 3:
                setMxy(getMxx() * shx);
                setMyx(getMyy() * shy);
                if (getMxy() != 0.0d || getMyx() != 0.0d) {
                    this.state2d = mystate | 4;
                    return;
                }
                return;
            case 4:
            case 5:
                setMxx(getMxy() * shy);
                setMyy(getMyx() * shx);
                if (getMxx() != 0.0d || getMyy() != 0.0d) {
                    this.state2d = mystate | 2;
                    return;
                }
                return;
            case 6:
            case 7:
                break;
            default:
                stateError();
                break;
        }
        double M0 = getMxx();
        double M1 = getMxy();
        setMxx(M0 + (M1 * shy));
        setMxy((M0 * shx) + M1);
        double M02 = getMyx();
        double M12 = getMyy();
        setMyx(M02 + (M12 * shy));
        setMyy((M02 * shx) + M12);
        updateState2D();
    }

    private void shear3D(double shx, double shy) {
        switch (this.state3d) {
            case 0:
                break;
            case 1:
                setMxy(shx);
                setMyx(shy);
                if (shx != 0.0d || shy != 0.0d) {
                    this.state3d = 4;
                    return;
                }
                return;
            case 2:
            case 3:
                setMxy(getMxx() * shx);
                setMyx(getMyy() * shy);
                if (getMxy() != 0.0d || getMyx() != 0.0d) {
                    this.state3d = 4;
                    return;
                }
                return;
            case 4:
                double m_xx = getMxx();
                double m_xy = getMxy();
                double m_yx = getMyx();
                double m_yy = getMyy();
                double m_zx = getMzx();
                double m_zy = getMzy();
                setMxx(m_xx + (m_xy * shy));
                setMxy(m_xy + (m_xx * shx));
                setMyx(m_yx + (m_yy * shy));
                setMyy(m_yy + (m_yx * shx));
                setMzx(m_zx + (m_zy * shy));
                setMzy(m_zy + (m_zx * shx));
                updateState();
                return;
            default:
                stateError();
                break;
        }
        shear2D(shx, shy);
    }

    public void prependShear(double shx, double shy) {
        this.atomicChange.start();
        preShear2D(shx, shy);
        this.atomicChange.end();
    }

    public void prependShear(double shx, double shy, double pivotX, double pivotY) {
        this.atomicChange.start();
        if (pivotX != 0.0d || pivotY != 0.0d) {
            preTranslate2D(-pivotX, -pivotY);
            preShear2D(shx, shy);
            preTranslate2D(pivotX, pivotY);
        } else {
            preShear2D(shx, shy);
        }
        this.atomicChange.end();
    }

    public void prependShear(double shx, double shy, Point2D pivot) {
        prependShear(shx, shy, pivot.getX(), pivot.getY());
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private void preShear2D(double shx, double shy) {
        if (this.state3d != 0) {
            preShear3D(shx, shy);
        }
        int mystate = this.state2d;
        switch (mystate) {
            case 0:
                setMxy(shx);
                setMyx(shy);
                if (getMxy() == 0.0d || getMyx() != 0.0d) {
                    this.state2d = mystate | 2 | 4;
                    break;
                }
                break;
            case 1:
                double t_x_3 = getTx();
                double t_y_3 = getTy();
                setTx(t_x_3 + (shx * t_y_3));
                setTy(t_y_3 + (shy * t_x_3));
                if (getTx() == 0.0d && getTy() == 0.0d) {
                    mystate &= -2;
                    this.state2d = mystate;
                }
                setMxy(shx);
                setMyx(shy);
                if (getMxy() == 0.0d) {
                    break;
                }
                this.state2d = mystate | 2 | 4;
                break;
            case 2:
                setMxy(shx * getMyy());
                setMyx(shy * getMxx());
                if (getMxy() == 0.0d || getMyx() != 0.0d) {
                    this.state2d = mystate | 4;
                    break;
                }
                break;
            case 3:
                double t_x_2 = getTx();
                double t_y_2 = getTy();
                setTx(t_x_2 + (shx * t_y_2));
                setTy(t_y_2 + (shy * t_x_2));
                if (getTx() == 0.0d && getTy() == 0.0d) {
                    mystate &= -2;
                    this.state2d = mystate;
                }
                setMxy(shx * getMyy());
                setMyx(shy * getMxx());
                if (getMxy() == 0.0d) {
                    break;
                }
                this.state2d = mystate | 4;
                break;
            case 4:
            case 6:
                double m_xx = getMxx();
                double m_xy = getMxy();
                double m_yx = getMyx();
                double m_yy = getMyy();
                setMxx(m_xx + (shx * m_yx));
                setMxy(m_xy + (shx * m_yy));
                setMyx((shy * m_xx) + m_yx);
                setMyy((shy * m_xy) + m_yy);
                updateState2D();
                break;
            case 5:
            case 7:
                double t_x_1 = getTx();
                double t_y_1 = getTy();
                setTx(t_x_1 + (shx * t_y_1));
                setTy(t_y_1 + (shy * t_x_1));
                double m_xx2 = getMxx();
                double m_xy2 = getMxy();
                double m_yx2 = getMyx();
                double m_yy2 = getMyy();
                setMxx(m_xx2 + (shx * m_yx2));
                setMxy(m_xy2 + (shx * m_yy2));
                setMyx((shy * m_xx2) + m_yx2);
                setMyy((shy * m_xy2) + m_yy2);
                updateState2D();
                break;
            default:
                stateError();
                double t_x_12 = getTx();
                double t_y_12 = getTy();
                setTx(t_x_12 + (shx * t_y_12));
                setTy(t_y_12 + (shy * t_x_12));
                double m_xx22 = getMxx();
                double m_xy22 = getMxy();
                double m_yx22 = getMyx();
                double m_yy22 = getMyy();
                setMxx(m_xx22 + (shx * m_yx22));
                setMxy(m_xy22 + (shx * m_yy22));
                setMyx((shy * m_xx22) + m_yx22);
                setMyy((shy * m_xy22) + m_yy22);
                updateState2D();
                break;
        }
    }

    private void preShear3D(double shx, double shy) {
        switch (this.state3d) {
            case 0:
                break;
            case 1:
                double tx_t = getTx();
                setMxy(shx);
                setTx(tx_t + (getTy() * shx));
                setMyx(shy);
                setTy((tx_t * shy) + getTy());
                if (shx != 0.0d || shy != 0.0d) {
                    this.state3d = 4;
                    return;
                }
                return;
            case 2:
                setMxy(getMyy() * shx);
                setMyx(getMxx() * shy);
                if (getMxy() != 0.0d || getMyx() != 0.0d) {
                    this.state3d = 4;
                    return;
                }
                return;
            case 3:
                double tx_st = getTx();
                setMxy(getMyy() * shx);
                setTx(tx_st + (getTy() * shx));
                setMyx(getMxx() * shy);
                setTy((tx_st * shy) + getTy());
                if (getMxy() != 0.0d || getMyx() != 0.0d) {
                    this.state3d = 4;
                    return;
                }
                return;
            case 4:
                double m_xx = getMxx();
                double m_xy = getMxy();
                double m_yx = getMyx();
                double t_x = getTx();
                double m_yy = getMyy();
                double m_xz = getMxz();
                double m_yz = getMyz();
                double t_y = getTy();
                setMxx(m_xx + (m_yx * shx));
                setMxy(m_xy + (m_yy * shx));
                setMxz(m_xz + (m_yz * shx));
                setTx(t_x + (t_y * shx));
                setMyx((m_xx * shy) + m_yx);
                setMyy((m_xy * shy) + m_yy);
                setMyz((m_xz * shy) + m_yz);
                setTy((t_x * shy) + t_y);
                updateState();
                return;
            default:
                stateError();
                break;
        }
        preShear2D(shx, shy);
    }

    public void appendRotation(double angle) {
        this.atomicChange.start();
        rotate2D(angle);
        this.atomicChange.end();
    }

    public void appendRotation(double angle, double pivotX, double pivotY) {
        this.atomicChange.start();
        if (pivotX != 0.0d || pivotY != 0.0d) {
            translate2D(pivotX, pivotY);
            rotate2D(angle);
            translate2D(-pivotX, -pivotY);
        } else {
            rotate2D(angle);
        }
        this.atomicChange.end();
    }

    public void appendRotation(double angle, Point2D pivot) {
        appendRotation(angle, pivot.getX(), pivot.getY());
    }

    public void appendRotation(double angle, double pivotX, double pivotY, double pivotZ, double axisX, double axisY, double axisZ) {
        this.atomicChange.start();
        if (pivotX != 0.0d || pivotY != 0.0d || pivotZ != 0.0d) {
            translate3D(pivotX, pivotY, pivotZ);
            rotate3D(angle, axisX, axisY, axisZ);
            translate3D(-pivotX, -pivotY, -pivotZ);
        } else {
            rotate3D(angle, axisX, axisY, axisZ);
        }
        this.atomicChange.end();
    }

    public void appendRotation(double angle, double pivotX, double pivotY, double pivotZ, Point3D axis) {
        appendRotation(angle, pivotX, pivotY, pivotZ, axis.getX(), axis.getY(), axis.getZ());
    }

    public void appendRotation(double angle, Point3D pivot, Point3D axis) {
        appendRotation(angle, pivot.getX(), pivot.getY(), pivot.getZ(), axis.getX(), axis.getY(), axis.getZ());
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:21:0x015c  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x015f A[FALL_THROUGH] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x01e6  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x023d  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0294  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void rotate3D(double r9, double r11, double r13, double r15) {
        /*
            Method dump skipped, instructions count: 1124
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: javafx.scene.transform.Affine.rotate3D(double, double, double, double):void");
    }

    private void rotate2D(double theta) {
        if (this.state3d != 0) {
            rotate3D(theta);
            return;
        }
        double sin = Math.sin(Math.toRadians(theta));
        if (sin == 1.0d) {
            rotate2D_90();
            return;
        }
        if (sin == -1.0d) {
            rotate2D_270();
            return;
        }
        double cos = Math.cos(Math.toRadians(theta));
        if (cos == -1.0d) {
            rotate2D_180();
            return;
        }
        if (cos != 1.0d) {
            double M0 = getMxx();
            double M1 = getMxy();
            setMxx((cos * M0) + (sin * M1));
            setMxy(((-sin) * M0) + (cos * M1));
            double M02 = getMyx();
            double M12 = getMyy();
            setMyx((cos * M02) + (sin * M12));
            setMyy(((-sin) * M02) + (cos * M12));
            updateState2D();
        }
    }

    private void rotate2D_90() {
        double M0 = getMxx();
        setMxx(getMxy());
        setMxy(-M0);
        double M02 = getMyx();
        setMyx(getMyy());
        setMyy(-M02);
        int newstate = rot90conversion[this.state2d];
        if ((newstate & 6) == 2 && getMxx() == 1.0d && getMyy() == 1.0d) {
            newstate -= 2;
        } else if ((newstate & 6) == 4 && getMxy() == 0.0d && getMyx() == 0.0d) {
            newstate = (newstate & (-5)) | 2;
        }
        this.state2d = newstate;
    }

    private void rotate2D_180() {
        setMxx(-getMxx());
        setMyy(-getMyy());
        int oldstate = this.state2d;
        if ((oldstate & 4) != 0) {
            setMxy(-getMxy());
            setMyx(-getMyx());
        } else if (getMxx() == 1.0d && getMyy() == 1.0d) {
            this.state2d = oldstate & (-3);
        } else {
            this.state2d = oldstate | 2;
        }
    }

    private void rotate2D_270() {
        double M0 = getMxx();
        setMxx(-getMxy());
        setMxy(M0);
        double M02 = getMyx();
        setMyx(-getMyy());
        setMyy(M02);
        int newstate = rot90conversion[this.state2d];
        if ((newstate & 6) == 2 && getMxx() == 1.0d && getMyy() == 1.0d) {
            newstate -= 2;
        } else if ((newstate & 6) == 4 && getMxy() == 0.0d && getMyx() == 0.0d) {
            newstate = (newstate & (-5)) | 2;
        }
        this.state2d = newstate;
    }

    private void rotate3D(double theta) {
        if (this.state3d == 0) {
            rotate2D(theta);
            return;
        }
        double sin = Math.sin(Math.toRadians(theta));
        if (sin == 1.0d) {
            rotate3D_90();
            return;
        }
        if (sin == -1.0d) {
            rotate3D_270();
            return;
        }
        double cos = Math.cos(Math.toRadians(theta));
        if (cos == -1.0d) {
            rotate3D_180();
            return;
        }
        if (cos != 1.0d) {
            double M0 = getMxx();
            double M1 = getMxy();
            setMxx((cos * M0) + (sin * M1));
            setMxy(((-sin) * M0) + (cos * M1));
            double M02 = getMyx();
            double M12 = getMyy();
            setMyx((cos * M02) + (sin * M12));
            setMyy(((-sin) * M02) + (cos * M12));
            double M03 = getMzx();
            double M13 = getMzy();
            setMzx((cos * M03) + (sin * M13));
            setMzy(((-sin) * M03) + (cos * M13));
            updateState();
        }
    }

    private void rotate3D_90() {
        double M0 = getMxx();
        setMxx(getMxy());
        setMxy(-M0);
        double M02 = getMyx();
        setMyx(getMyy());
        setMyy(-M02);
        double M03 = getMzx();
        setMzx(getMzy());
        setMzy(-M03);
        switch (this.state3d) {
            case 1:
                break;
            case 2:
            case 3:
                if (getMxy() != 0.0d || getMyx() != 0.0d) {
                    this.state3d = 4;
                    return;
                }
                return;
            case 4:
                updateState();
                return;
            default:
                stateError();
                break;
        }
        this.state3d = 4;
    }

    private void rotate3D_180() {
        double mxx = getMxx();
        double myy = getMyy();
        setMxx(-mxx);
        setMyy(-myy);
        if (this.state3d == 4) {
            setMxy(-getMxy());
            setMyx(-getMyx());
            setMzx(-getMzx());
            setMzy(-getMzy());
            updateState();
            return;
        }
        if (mxx == -1.0d && myy == -1.0d && getMzz() == 1.0d) {
            this.state3d &= -3;
        } else {
            this.state3d |= 2;
        }
    }

    private void rotate3D_270() {
        double M0 = getMxx();
        setMxx(-getMxy());
        setMxy(M0);
        double M02 = getMyx();
        setMyx(-getMyy());
        setMyy(M02);
        double M03 = getMzx();
        setMzx(-getMzy());
        setMzy(M03);
        switch (this.state3d) {
            case 1:
                break;
            case 2:
            case 3:
                if (getMxy() != 0.0d || getMyx() != 0.0d) {
                    this.state3d = 4;
                    return;
                }
                return;
            case 4:
                updateState();
                return;
            default:
                stateError();
                break;
        }
        this.state3d = 4;
    }

    public void prependRotation(double angle) {
        this.atomicChange.start();
        preRotate2D(angle);
        this.atomicChange.end();
    }

    public void prependRotation(double angle, double pivotX, double pivotY) {
        this.atomicChange.start();
        if (pivotX != 0.0d || pivotY != 0.0d) {
            preTranslate2D(-pivotX, -pivotY);
            preRotate2D(angle);
            preTranslate2D(pivotX, pivotY);
        } else {
            preRotate2D(angle);
        }
        this.atomicChange.end();
    }

    public void prependRotation(double angle, Point2D pivot) {
        prependRotation(angle, pivot.getX(), pivot.getY());
    }

    public void prependRotation(double angle, double pivotX, double pivotY, double pivotZ, double axisX, double axisY, double axisZ) {
        this.atomicChange.start();
        if (pivotX != 0.0d || pivotY != 0.0d || pivotZ != 0.0d) {
            preTranslate3D(-pivotX, -pivotY, -pivotZ);
            preRotate3D(angle, axisX, axisY, axisZ);
            preTranslate3D(pivotX, pivotY, pivotZ);
        } else {
            preRotate3D(angle, axisX, axisY, axisZ);
        }
        this.atomicChange.end();
    }

    public void prependRotation(double angle, double pivotX, double pivotY, double pivotZ, Point3D axis) {
        prependRotation(angle, pivotX, pivotY, pivotZ, axis.getX(), axis.getY(), axis.getZ());
    }

    public void prependRotation(double angle, Point3D pivot, Point3D axis) {
        prependRotation(angle, pivot.getX(), pivot.getY(), pivot.getZ(), axis.getX(), axis.getY(), axis.getZ());
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:21:0x015c  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x015f A[FALL_THROUGH] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x021f  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x02a6  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0336  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x038d  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x041d  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0474  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x04e6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void preRotate3D(double r9, double r11, double r13, double r15) {
        /*
            Method dump skipped, instructions count: 2063
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: javafx.scene.transform.Affine.preRotate3D(double, double, double, double):void");
    }

    private void preRotate2D(double theta) {
        if (this.state3d != 0) {
            preRotate3D(theta);
            return;
        }
        double sin = Math.sin(Math.toRadians(theta));
        if (sin == 1.0d) {
            preRotate2D_90();
            return;
        }
        if (sin == -1.0d) {
            preRotate2D_270();
            return;
        }
        double cos = Math.cos(Math.toRadians(theta));
        if (cos == -1.0d) {
            preRotate2D_180();
            return;
        }
        if (cos != 1.0d) {
            double M0 = getMxx();
            double M1 = getMyx();
            setMxx((cos * M0) - (sin * M1));
            setMyx((sin * M0) + (cos * M1));
            double M02 = getMxy();
            double M12 = getMyy();
            setMxy((cos * M02) - (sin * M12));
            setMyy((sin * M02) + (cos * M12));
            double M03 = getTx();
            double M13 = getTy();
            setTx((cos * M03) - (sin * M13));
            setTy((sin * M03) + (cos * M13));
            updateState2D();
        }
    }

    private void preRotate2D_90() {
        double M0 = getMxx();
        setMxx(-getMyx());
        setMyx(M0);
        double M02 = getMxy();
        setMxy(-getMyy());
        setMyy(M02);
        double M03 = getTx();
        setTx(-getTy());
        setTy(M03);
        int newstate = rot90conversion[this.state2d];
        if ((newstate & 6) == 2 && getMxx() == 1.0d && getMyy() == 1.0d) {
            newstate -= 2;
        } else if ((newstate & 6) == 4 && getMxy() == 0.0d && getMyx() == 0.0d) {
            newstate = (newstate & (-5)) | 2;
        }
        this.state2d = newstate;
    }

    private void preRotate2D_180() {
        setMxx(-getMxx());
        setMxy(-getMxy());
        setTx(-getTx());
        setMyx(-getMyx());
        setMyy(-getMyy());
        setTy(-getTy());
        if ((this.state2d & 4) != 0) {
            if (getMxx() == 0.0d && getMyy() == 0.0d) {
                this.state2d &= -3;
                return;
            } else {
                this.state2d |= 2;
                return;
            }
        }
        if (getMxx() == 1.0d && getMyy() == 1.0d) {
            this.state2d &= -3;
        } else {
            this.state2d |= 2;
        }
    }

    private void preRotate2D_270() {
        double M0 = getMxx();
        setMxx(getMyx());
        setMyx(-M0);
        double M02 = getMxy();
        setMxy(getMyy());
        setMyy(-M02);
        double M03 = getTx();
        setTx(getTy());
        setTy(-M03);
        int newstate = rot90conversion[this.state2d];
        if ((newstate & 6) == 2 && getMxx() == 1.0d && getMyy() == 1.0d) {
            newstate -= 2;
        } else if ((newstate & 6) == 4 && getMxy() == 0.0d && getMyx() == 0.0d) {
            newstate = (newstate & (-5)) | 2;
        }
        this.state2d = newstate;
    }

    private void preRotate3D(double theta) {
        if (this.state3d == 0) {
            preRotate2D(theta);
            return;
        }
        double sin = Math.sin(Math.toRadians(theta));
        if (sin == 1.0d) {
            preRotate3D_90();
            return;
        }
        if (sin == -1.0d) {
            preRotate3D_270();
            return;
        }
        double cos = Math.cos(Math.toRadians(theta));
        if (cos == -1.0d) {
            preRotate3D_180();
            return;
        }
        if (cos != 1.0d) {
            double M0 = getMxx();
            double M1 = getMyx();
            setMxx((cos * M0) - (sin * M1));
            setMyx((sin * M0) + (cos * M1));
            double M02 = getMxy();
            double M12 = getMyy();
            setMxy((cos * M02) - (sin * M12));
            setMyy((sin * M02) + (cos * M12));
            double M03 = getMxz();
            double M13 = getMyz();
            setMxz((cos * M03) - (sin * M13));
            setMyz((sin * M03) + (cos * M13));
            double M04 = getTx();
            double M14 = getTy();
            setTx((cos * M04) - (sin * M14));
            setTy((sin * M04) + (cos * M14));
            updateState();
        }
    }

    private void preRotate3D_90() {
        double M0 = getMxx();
        setMxx(-getMyx());
        setMyx(M0);
        double M02 = getMxy();
        setMxy(-getMyy());
        setMyy(M02);
        double M03 = getMxz();
        setMxz(-getMyz());
        setMyz(M03);
        double M04 = getTx();
        setTx(-getTy());
        setTy(M04);
        switch (this.state3d) {
            case 1:
                break;
            case 2:
            case 3:
                if (getMxy() != 0.0d || getMyx() != 0.0d) {
                    this.state3d = 4;
                    return;
                }
                return;
            case 4:
                updateState();
                return;
            default:
                stateError();
                break;
        }
        this.state3d = 4;
    }

    private void preRotate3D_180() {
        double mxx = getMxx();
        double myy = getMyy();
        setMxx(-mxx);
        setMyy(-myy);
        setTx(-getTx());
        setTy(-getTy());
        if (this.state3d == 4) {
            setMxy(-getMxy());
            setMxz(-getMxz());
            setMyx(-getMyx());
            setMyz(-getMyz());
            updateState();
            return;
        }
        if (mxx == -1.0d && myy == -1.0d && getMzz() == 1.0d) {
            this.state3d &= -3;
        } else {
            this.state3d |= 2;
        }
    }

    private void preRotate3D_270() {
        double M0 = getMxx();
        setMxx(getMyx());
        setMyx(-M0);
        double M02 = getMxy();
        setMxy(getMyy());
        setMyy(-M02);
        double M03 = getMxz();
        setMxz(getMyz());
        setMyz(-M03);
        double M04 = getTx();
        setTx(getTy());
        setTy(-M04);
        switch (this.state3d) {
            case 1:
                break;
            case 2:
            case 3:
                if (getMxy() != 0.0d || getMyx() != 0.0d) {
                    this.state3d = 4;
                    return;
                }
                return;
            case 4:
                updateState();
                return;
            default:
                stateError();
                break;
        }
        this.state3d = 4;
    }

    @Override // javafx.scene.transform.Transform
    public Point2D transform(double x2, double y2) {
        ensureCanTransform2DPoint();
        switch (this.state2d) {
            case 0:
                return new Point2D(x2, y2);
            case 1:
                return new Point2D(x2 + getTx(), y2 + getTy());
            case 2:
                return new Point2D(getMxx() * x2, getMyy() * y2);
            case 3:
                return new Point2D((getMxx() * x2) + getTx(), (getMyy() * y2) + getTy());
            case 4:
                return new Point2D(getMxy() * y2, getMyx() * x2);
            case 5:
                return new Point2D((getMxy() * y2) + getTx(), (getMyx() * x2) + getTy());
            case 6:
                return new Point2D((getMxx() * x2) + (getMxy() * y2), (getMyx() * x2) + (getMyy() * y2));
            case 7:
                break;
            default:
                stateError();
                break;
        }
        return new Point2D((getMxx() * x2) + (getMxy() * y2) + getTx(), (getMyx() * x2) + (getMyy() * y2) + getTy());
    }

    @Override // javafx.scene.transform.Transform
    public Point3D transform(double x2, double y2, double z2) {
        switch (this.state3d) {
            case 0:
                break;
            case 1:
                return new Point3D(x2 + getTx(), y2 + getTy(), z2 + getTz());
            case 2:
                return new Point3D(getMxx() * x2, getMyy() * y2, getMzz() * z2);
            case 3:
                return new Point3D((getMxx() * x2) + getTx(), (getMyy() * y2) + getTy(), (getMzz() * z2) + getTz());
            case 4:
                return new Point3D((getMxx() * x2) + (getMxy() * y2) + (getMxz() * z2) + getTx(), (getMyx() * x2) + (getMyy() * y2) + (getMyz() * z2) + getTy(), (getMzx() * x2) + (getMzy() * y2) + (getMzz() * z2) + getTz());
            default:
                stateError();
                break;
        }
        switch (this.state2d) {
            case 0:
                return new Point3D(x2, y2, z2);
            case 1:
                return new Point3D(x2 + getTx(), y2 + getTy(), z2);
            case 2:
                return new Point3D(getMxx() * x2, getMyy() * y2, z2);
            case 3:
                return new Point3D((getMxx() * x2) + getTx(), (getMyy() * y2) + getTy(), z2);
            case 4:
                return new Point3D(getMxy() * y2, getMyx() * x2, z2);
            case 5:
                return new Point3D((getMxy() * y2) + getTx(), (getMyx() * x2) + getTy(), z2);
            case 6:
                return new Point3D((getMxx() * x2) + (getMxy() * y2), (getMyx() * x2) + (getMyy() * y2), z2);
            case 7:
                break;
            default:
                stateError();
                break;
        }
        return new Point3D((getMxx() * x2) + (getMxy() * y2) + getTx(), (getMyx() * x2) + (getMyy() * y2) + getTy(), z2);
    }

    @Override // javafx.scene.transform.Transform
    void transform2DPointsImpl(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) {
        switch (this.state2d) {
            case 0:
                if (srcPts != dstPts || srcOff != dstOff) {
                    System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
                    return;
                }
                return;
            case 1:
                double tx = getTx();
                double ty = getTy();
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i2 = dstOff;
                        int dstOff2 = dstOff + 1;
                        int i3 = srcOff;
                        int srcOff2 = srcOff + 1;
                        dstPts[i2] = srcPts[i3] + tx;
                        dstOff = dstOff2 + 1;
                        srcOff = srcOff2 + 1;
                        dstPts[dstOff2] = srcPts[srcOff2] + ty;
                    } else {
                        return;
                    }
                }
            case 2:
                double mxx = getMxx();
                double myy = getMyy();
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i4 = dstOff;
                        int dstOff3 = dstOff + 1;
                        int i5 = srcOff;
                        int srcOff3 = srcOff + 1;
                        dstPts[i4] = mxx * srcPts[i5];
                        dstOff = dstOff3 + 1;
                        srcOff = srcOff3 + 1;
                        dstPts[dstOff3] = myy * srcPts[srcOff3];
                    } else {
                        return;
                    }
                }
            case 3:
                double mxx2 = getMxx();
                double tx2 = getTx();
                double myy2 = getMyy();
                double ty2 = getTy();
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i6 = dstOff;
                        int dstOff4 = dstOff + 1;
                        int i7 = srcOff;
                        int srcOff4 = srcOff + 1;
                        dstPts[i6] = (mxx2 * srcPts[i7]) + tx2;
                        dstOff = dstOff4 + 1;
                        srcOff = srcOff4 + 1;
                        dstPts[dstOff4] = (myy2 * srcPts[srcOff4]) + ty2;
                    } else {
                        return;
                    }
                }
            case 4:
                double mxy = getMxy();
                double myx = getMyx();
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i8 = srcOff;
                        int srcOff5 = srcOff + 1;
                        double x2 = srcPts[i8];
                        int i9 = dstOff;
                        int dstOff5 = dstOff + 1;
                        srcOff = srcOff5 + 1;
                        dstPts[i9] = mxy * srcPts[srcOff5];
                        dstOff = dstOff5 + 1;
                        dstPts[dstOff5] = myx * x2;
                    } else {
                        return;
                    }
                }
            case 5:
                double mxy2 = getMxy();
                double tx3 = getTx();
                double myx2 = getMyx();
                double ty3 = getTy();
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i10 = srcOff;
                        int srcOff6 = srcOff + 1;
                        double x3 = srcPts[i10];
                        int i11 = dstOff;
                        int dstOff6 = dstOff + 1;
                        srcOff = srcOff6 + 1;
                        dstPts[i11] = (mxy2 * srcPts[srcOff6]) + tx3;
                        dstOff = dstOff6 + 1;
                        dstPts[dstOff6] = (myx2 * x3) + ty3;
                    } else {
                        return;
                    }
                }
            case 6:
                double mxx3 = getMxx();
                double mxy3 = getMxy();
                double myx3 = getMyx();
                double myy3 = getMyy();
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i12 = srcOff;
                        int srcOff7 = srcOff + 1;
                        double x4 = srcPts[i12];
                        srcOff = srcOff7 + 1;
                        double y2 = srcPts[srcOff7];
                        int i13 = dstOff;
                        int dstOff7 = dstOff + 1;
                        dstPts[i13] = (mxx3 * x4) + (mxy3 * y2);
                        dstOff = dstOff7 + 1;
                        dstPts[dstOff7] = (myx3 * x4) + (myy3 * y2);
                    } else {
                        return;
                    }
                }
            case 7:
                break;
            default:
                stateError();
                break;
        }
        double mxx4 = getMxx();
        double mxy4 = getMxy();
        double tx4 = getTx();
        double myx4 = getMyx();
        double myy4 = getMyy();
        double ty4 = getTy();
        while (true) {
            numPts--;
            if (numPts >= 0) {
                int i14 = srcOff;
                int srcOff8 = srcOff + 1;
                double x5 = srcPts[i14];
                srcOff = srcOff8 + 1;
                double y3 = srcPts[srcOff8];
                int i15 = dstOff;
                int dstOff8 = dstOff + 1;
                dstPts[i15] = (mxx4 * x5) + (mxy4 * y3) + tx4;
                dstOff = dstOff8 + 1;
                dstPts[dstOff8] = (myx4 * x5) + (myy4 * y3) + ty4;
            } else {
                return;
            }
        }
    }

    @Override // javafx.scene.transform.Transform
    void transform3DPointsImpl(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) {
        switch (this.state3d) {
            case 0:
                break;
            case 1:
                double tx = getTx();
                double ty = getTy();
                double tz = getTz();
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i2 = dstOff;
                        int dstOff2 = dstOff + 1;
                        int i3 = srcOff;
                        int srcOff2 = srcOff + 1;
                        dstPts[i2] = srcPts[i3] + tx;
                        int dstOff3 = dstOff2 + 1;
                        int srcOff3 = srcOff2 + 1;
                        dstPts[dstOff2] = srcPts[srcOff2] + ty;
                        dstOff = dstOff3 + 1;
                        srcOff = srcOff3 + 1;
                        dstPts[dstOff3] = srcPts[srcOff3] + tz;
                    } else {
                        return;
                    }
                }
            case 2:
                double mxx = getMxx();
                double myy = getMyy();
                double mzz = getMzz();
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i4 = dstOff;
                        int dstOff4 = dstOff + 1;
                        int i5 = srcOff;
                        int srcOff4 = srcOff + 1;
                        dstPts[i4] = mxx * srcPts[i5];
                        int dstOff5 = dstOff4 + 1;
                        int srcOff5 = srcOff4 + 1;
                        dstPts[dstOff4] = myy * srcPts[srcOff4];
                        dstOff = dstOff5 + 1;
                        srcOff = srcOff5 + 1;
                        dstPts[dstOff5] = mzz * srcPts[srcOff5];
                    } else {
                        return;
                    }
                }
            case 3:
                double mxx2 = getMxx();
                double tx2 = getTx();
                double myy2 = getMyy();
                double ty2 = getTy();
                double mzz2 = getMzz();
                double tz2 = getTz();
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i6 = dstOff;
                        int dstOff6 = dstOff + 1;
                        int i7 = srcOff;
                        int srcOff6 = srcOff + 1;
                        dstPts[i6] = (mxx2 * srcPts[i7]) + tx2;
                        int dstOff7 = dstOff6 + 1;
                        int srcOff7 = srcOff6 + 1;
                        dstPts[dstOff6] = (myy2 * srcPts[srcOff6]) + ty2;
                        dstOff = dstOff7 + 1;
                        srcOff = srcOff7 + 1;
                        dstPts[dstOff7] = (mzz2 * srcPts[srcOff7]) + tz2;
                    } else {
                        return;
                    }
                }
            case 4:
                double mxx3 = getMxx();
                double mxy = getMxy();
                double mxz = getMxz();
                double tx3 = getTx();
                double myx = getMyx();
                double myy3 = getMyy();
                double myz = getMyz();
                double ty3 = getTy();
                double mzx = getMzx();
                double mzy = getMzy();
                double mzz3 = getMzz();
                double tz3 = getTz();
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i8 = srcOff;
                        int srcOff8 = srcOff + 1;
                        double x2 = srcPts[i8];
                        int srcOff9 = srcOff8 + 1;
                        double y2 = srcPts[srcOff8];
                        srcOff = srcOff9 + 1;
                        double z2 = srcPts[srcOff9];
                        int i9 = dstOff;
                        int dstOff8 = dstOff + 1;
                        dstPts[i9] = (mxx3 * x2) + (mxy * y2) + (mxz * z2) + tx3;
                        int dstOff9 = dstOff8 + 1;
                        dstPts[dstOff8] = (myx * x2) + (myy3 * y2) + (myz * z2) + ty3;
                        dstOff = dstOff9 + 1;
                        dstPts[dstOff9] = (mzx * x2) + (mzy * y2) + (mzz3 * z2) + tz3;
                    } else {
                        return;
                    }
                }
            default:
                stateError();
                break;
        }
        switch (this.state2d) {
            case 0:
                if (srcPts != dstPts || srcOff != dstOff) {
                    System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 3);
                    return;
                }
                return;
            case 1:
                double tx4 = getTx();
                double ty4 = getTy();
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i10 = dstOff;
                        int dstOff10 = dstOff + 1;
                        int i11 = srcOff;
                        int srcOff10 = srcOff + 1;
                        dstPts[i10] = srcPts[i11] + tx4;
                        int dstOff11 = dstOff10 + 1;
                        int srcOff11 = srcOff10 + 1;
                        dstPts[dstOff10] = srcPts[srcOff10] + ty4;
                        dstOff = dstOff11 + 1;
                        srcOff = srcOff11 + 1;
                        dstPts[dstOff11] = srcPts[srcOff11];
                    } else {
                        return;
                    }
                }
            case 2:
                double mxx4 = getMxx();
                double myy4 = getMyy();
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i12 = dstOff;
                        int dstOff12 = dstOff + 1;
                        int i13 = srcOff;
                        int srcOff12 = srcOff + 1;
                        dstPts[i12] = mxx4 * srcPts[i13];
                        int dstOff13 = dstOff12 + 1;
                        int srcOff13 = srcOff12 + 1;
                        dstPts[dstOff12] = myy4 * srcPts[srcOff12];
                        dstOff = dstOff13 + 1;
                        srcOff = srcOff13 + 1;
                        dstPts[dstOff13] = srcPts[srcOff13];
                    } else {
                        return;
                    }
                }
            case 3:
                double mxx5 = getMxx();
                double tx5 = getTx();
                double myy5 = getMyy();
                double ty5 = getTy();
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i14 = dstOff;
                        int dstOff14 = dstOff + 1;
                        int i15 = srcOff;
                        int srcOff14 = srcOff + 1;
                        dstPts[i14] = (mxx5 * srcPts[i15]) + tx5;
                        int dstOff15 = dstOff14 + 1;
                        int srcOff15 = srcOff14 + 1;
                        dstPts[dstOff14] = (myy5 * srcPts[srcOff14]) + ty5;
                        dstOff = dstOff15 + 1;
                        srcOff = srcOff15 + 1;
                        dstPts[dstOff15] = srcPts[srcOff15];
                    } else {
                        return;
                    }
                }
            case 4:
                double mxy2 = getMxy();
                double myx2 = getMyx();
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i16 = srcOff;
                        int srcOff16 = srcOff + 1;
                        double x3 = srcPts[i16];
                        int i17 = dstOff;
                        int dstOff16 = dstOff + 1;
                        int srcOff17 = srcOff16 + 1;
                        dstPts[i17] = mxy2 * srcPts[srcOff16];
                        int dstOff17 = dstOff16 + 1;
                        dstPts[dstOff16] = myx2 * x3;
                        dstOff = dstOff17 + 1;
                        srcOff = srcOff17 + 1;
                        dstPts[dstOff17] = srcPts[srcOff17];
                    } else {
                        return;
                    }
                }
            case 5:
                double mxy3 = getMxy();
                double tx6 = getTx();
                double myx3 = getMyx();
                double ty6 = getTy();
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i18 = srcOff;
                        int srcOff18 = srcOff + 1;
                        double x4 = srcPts[i18];
                        int i19 = dstOff;
                        int dstOff18 = dstOff + 1;
                        int srcOff19 = srcOff18 + 1;
                        dstPts[i19] = (mxy3 * srcPts[srcOff18]) + tx6;
                        int dstOff19 = dstOff18 + 1;
                        dstPts[dstOff18] = (myx3 * x4) + ty6;
                        dstOff = dstOff19 + 1;
                        srcOff = srcOff19 + 1;
                        dstPts[dstOff19] = srcPts[srcOff19];
                    } else {
                        return;
                    }
                }
            case 6:
                double mxx6 = getMxx();
                double mxy4 = getMxy();
                double myx4 = getMyx();
                double myy6 = getMyy();
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i20 = srcOff;
                        int srcOff20 = srcOff + 1;
                        double x5 = srcPts[i20];
                        int srcOff21 = srcOff20 + 1;
                        double y3 = srcPts[srcOff20];
                        int i21 = dstOff;
                        int dstOff20 = dstOff + 1;
                        dstPts[i21] = (mxx6 * x5) + (mxy4 * y3);
                        int dstOff21 = dstOff20 + 1;
                        dstPts[dstOff20] = (myx4 * x5) + (myy6 * y3);
                        dstOff = dstOff21 + 1;
                        srcOff = srcOff21 + 1;
                        dstPts[dstOff21] = srcPts[srcOff21];
                    } else {
                        return;
                    }
                }
            case 7:
                break;
            default:
                stateError();
                break;
        }
        double mxx7 = getMxx();
        double mxy5 = getMxy();
        double tx7 = getTx();
        double myx5 = getMyx();
        double myy7 = getMyy();
        double ty7 = getTy();
        while (true) {
            numPts--;
            if (numPts >= 0) {
                int i22 = srcOff;
                int srcOff22 = srcOff + 1;
                double x6 = srcPts[i22];
                int srcOff23 = srcOff22 + 1;
                double y4 = srcPts[srcOff22];
                int i23 = dstOff;
                int dstOff22 = dstOff + 1;
                dstPts[i23] = (mxx7 * x6) + (mxy5 * y4) + tx7;
                int dstOff23 = dstOff22 + 1;
                dstPts[dstOff22] = (myx5 * x6) + (myy7 * y4) + ty7;
                dstOff = dstOff23 + 1;
                srcOff = srcOff23 + 1;
                dstPts[dstOff23] = srcPts[srcOff23];
            } else {
                return;
            }
        }
    }

    @Override // javafx.scene.transform.Transform
    public Point2D deltaTransform(double x2, double y2) {
        ensureCanTransform2DPoint();
        switch (this.state2d) {
            case 0:
            case 1:
                return new Point2D(x2, y2);
            case 2:
            case 3:
                return new Point2D(getMxx() * x2, getMyy() * y2);
            case 4:
            case 5:
                return new Point2D(getMxy() * y2, getMyx() * x2);
            case 6:
            case 7:
                break;
            default:
                stateError();
                break;
        }
        return new Point2D((getMxx() * x2) + (getMxy() * y2), (getMyx() * x2) + (getMyy() * y2));
    }

    @Override // javafx.scene.transform.Transform
    public Point3D deltaTransform(double x2, double y2, double z2) {
        switch (this.state3d) {
            case 0:
                break;
            case 1:
                return new Point3D(x2, y2, z2);
            case 2:
            case 3:
                return new Point3D(getMxx() * x2, getMyy() * y2, getMzz() * z2);
            case 4:
                return new Point3D((getMxx() * x2) + (getMxy() * y2) + (getMxz() * z2), (getMyx() * x2) + (getMyy() * y2) + (getMyz() * z2), (getMzx() * x2) + (getMzy() * y2) + (getMzz() * z2));
            default:
                stateError();
                break;
        }
        switch (this.state2d) {
            case 0:
            case 1:
                return new Point3D(x2, y2, z2);
            case 2:
            case 3:
                return new Point3D(getMxx() * x2, getMyy() * y2, z2);
            case 4:
            case 5:
                return new Point3D(getMxy() * y2, getMyx() * x2, z2);
            case 6:
            case 7:
                break;
            default:
                stateError();
                break;
        }
        return new Point3D((getMxx() * x2) + (getMxy() * y2), (getMyx() * x2) + (getMyy() * y2), z2);
    }

    @Override // javafx.scene.transform.Transform
    public Point2D inverseTransform(double x2, double y2) throws NonInvertibleTransformException {
        ensureCanTransform2DPoint();
        switch (this.state2d) {
            case 0:
                return new Point2D(x2, y2);
            case 1:
                return new Point2D(x2 - getTx(), y2 - getTy());
            case 2:
                double mxx_s = getMxx();
                double myy_s = getMyy();
                if (mxx_s == 0.0d || myy_s == 0.0d) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                return new Point2D((1.0d / mxx_s) * x2, (1.0d / myy_s) * y2);
            case 3:
                double mxx_st = getMxx();
                double myy_st = getMyy();
                if (mxx_st == 0.0d || myy_st == 0.0d) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                return new Point2D(((1.0d / mxx_st) * x2) - (getTx() / mxx_st), ((1.0d / myy_st) * y2) - (getTy() / myy_st));
            case 4:
                double mxy_s = getMxy();
                double myx_s = getMyx();
                if (mxy_s == 0.0d || myx_s == 0.0d) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                return new Point2D((1.0d / myx_s) * y2, (1.0d / mxy_s) * x2);
            case 5:
                double mxy_st = getMxy();
                double myx_st = getMyx();
                if (mxy_st == 0.0d || myx_st == 0.0d) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                return new Point2D(((1.0d / myx_st) * y2) - (getTy() / myx_st), ((1.0d / mxy_st) * x2) - (getTx() / mxy_st));
            default:
                return super.inverseTransform(x2, y2);
        }
    }

    @Override // javafx.scene.transform.Transform
    public Point3D inverseTransform(double x2, double y2, double z2) throws NonInvertibleTransformException {
        switch (this.state3d) {
            case 0:
                break;
            case 1:
                return new Point3D(x2 - getTx(), y2 - getTy(), z2 - getTz());
            case 2:
                double mxx_s = getMxx();
                double myy_s = getMyy();
                double mzz_s = getMzz();
                if (mxx_s == 0.0d || myy_s == 0.0d || mzz_s == 0.0d) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                return new Point3D((1.0d / mxx_s) * x2, (1.0d / myy_s) * y2, (1.0d / mzz_s) * z2);
            case 3:
                double mxx_st = getMxx();
                double myy_st = getMyy();
                double mzz_st = getMzz();
                if (mxx_st == 0.0d || myy_st == 0.0d || mzz_st == 0.0d) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                return new Point3D(((1.0d / mxx_st) * x2) - (getTx() / mxx_st), ((1.0d / myy_st) * y2) - (getTy() / myy_st), ((1.0d / mzz_st) * z2) - (getTz() / mzz_st));
            case 4:
                return super.inverseTransform(x2, y2, z2);
            default:
                stateError();
                break;
        }
        switch (this.state2d) {
            case 0:
                return new Point3D(x2, y2, z2);
            case 1:
                return new Point3D(x2 - getTx(), y2 - getTy(), z2);
            case 2:
                double mxx_s2 = getMxx();
                double myy_s2 = getMyy();
                if (mxx_s2 == 0.0d || myy_s2 == 0.0d) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                return new Point3D((1.0d / mxx_s2) * x2, (1.0d / myy_s2) * y2, z2);
            case 3:
                double mxx_st2 = getMxx();
                double myy_st2 = getMyy();
                if (mxx_st2 == 0.0d || myy_st2 == 0.0d) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                return new Point3D(((1.0d / mxx_st2) * x2) - (getTx() / mxx_st2), ((1.0d / myy_st2) * y2) - (getTy() / myy_st2), z2);
            case 4:
                double mxy_s = getMxy();
                double myx_s = getMyx();
                if (mxy_s == 0.0d || myx_s == 0.0d) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                return new Point3D((1.0d / myx_s) * y2, (1.0d / mxy_s) * x2, z2);
            case 5:
                double mxy_st = getMxy();
                double myx_st = getMyx();
                if (mxy_st == 0.0d || myx_st == 0.0d) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                return new Point3D(((1.0d / myx_st) * y2) - (getTy() / myx_st), ((1.0d / mxy_st) * x2) - (getTx() / mxy_st), z2);
            default:
                return super.inverseTransform(x2, y2, z2);
        }
    }

    @Override // javafx.scene.transform.Transform
    void inverseTransform2DPointsImpl(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) throws NonInvertibleTransformException {
        switch (this.state2d) {
            case 0:
                if (srcPts != dstPts || srcOff != dstOff) {
                    System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
                    return;
                }
                return;
            case 1:
                double tx = getTx();
                double ty = getTy();
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
            case 2:
                double mxx = getMxx();
                double myy = getMyy();
                if (mxx == 0.0d || myy == 0.0d) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                double mxx2 = 1.0d / mxx;
                double myy2 = 1.0d / myy;
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i4 = dstOff;
                        int dstOff3 = dstOff + 1;
                        int i5 = srcOff;
                        int srcOff3 = srcOff + 1;
                        dstPts[i4] = mxx2 * srcPts[i5];
                        dstOff = dstOff3 + 1;
                        srcOff = srcOff3 + 1;
                        dstPts[dstOff3] = myy2 * srcPts[srcOff3];
                    } else {
                        return;
                    }
                }
                break;
            case 3:
                double mxx3 = getMxx();
                double tx2 = getTx();
                double myy3 = getMyy();
                double ty2 = getTy();
                if (mxx3 == 0.0d || myy3 == 0.0d) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                double tx3 = (-tx2) / mxx3;
                double ty3 = (-ty2) / myy3;
                double mxx4 = 1.0d / mxx3;
                double myy4 = 1.0d / myy3;
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i6 = dstOff;
                        int dstOff4 = dstOff + 1;
                        int i7 = srcOff;
                        int srcOff4 = srcOff + 1;
                        dstPts[i6] = (mxx4 * srcPts[i7]) + tx3;
                        dstOff = dstOff4 + 1;
                        srcOff = srcOff4 + 1;
                        dstPts[dstOff4] = (myy4 * srcPts[srcOff4]) + ty3;
                    } else {
                        return;
                    }
                }
                break;
            case 4:
                double mxy = getMxy();
                double myx = getMyx();
                if (mxy == 0.0d || myx == 0.0d) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                double myx2 = 1.0d / mxy;
                double mxy2 = 1.0d / myx;
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i8 = srcOff;
                        int srcOff5 = srcOff + 1;
                        double x2 = srcPts[i8];
                        int i9 = dstOff;
                        int dstOff5 = dstOff + 1;
                        srcOff = srcOff5 + 1;
                        dstPts[i9] = mxy2 * srcPts[srcOff5];
                        dstOff = dstOff5 + 1;
                        dstPts[dstOff5] = myx2 * x2;
                    } else {
                        return;
                    }
                }
                break;
            case 5:
                double mxy3 = getMxy();
                double tx4 = getTx();
                double myx3 = getMyx();
                double ty4 = getTy();
                if (mxy3 == 0.0d || myx3 == 0.0d) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                double tx5 = (-ty4) / myx3;
                double ty5 = (-tx4) / mxy3;
                double myx4 = 1.0d / mxy3;
                double mxy4 = 1.0d / myx3;
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i10 = srcOff;
                        int srcOff6 = srcOff + 1;
                        double x3 = srcPts[i10];
                        int i11 = dstOff;
                        int dstOff6 = dstOff + 1;
                        srcOff = srcOff6 + 1;
                        dstPts[i11] = (mxy4 * srcPts[srcOff6]) + tx5;
                        dstOff = dstOff6 + 1;
                        dstPts[dstOff6] = (myx4 * x3) + ty5;
                    } else {
                        return;
                    }
                }
                break;
            default:
                super.inverseTransform2DPointsImpl(srcPts, srcOff, dstPts, dstOff, numPts);
                return;
        }
    }

    @Override // javafx.scene.transform.Transform
    void inverseTransform3DPointsImpl(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) throws NonInvertibleTransformException {
        switch (this.state3d) {
            case 0:
                break;
            case 1:
                double tx = getTx();
                double ty = getTy();
                double tz = getTz();
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
            case 2:
                double mxx = getMxx();
                double myy = getMyy();
                double mzz = getMzz();
                if (mxx != 0.0d) {
                    if (!((myy == 0.0d) | (mzz == 0.0d))) {
                        double mxx2 = 1.0d / mxx;
                        double myy2 = 1.0d / myy;
                        double mzz2 = 1.0d / mzz;
                        while (true) {
                            numPts--;
                            if (numPts >= 0) {
                                int i4 = dstOff;
                                int dstOff4 = dstOff + 1;
                                int i5 = srcOff;
                                int srcOff4 = srcOff + 1;
                                dstPts[i4] = mxx2 * srcPts[i5];
                                int dstOff5 = dstOff4 + 1;
                                int srcOff5 = srcOff4 + 1;
                                dstPts[dstOff4] = myy2 * srcPts[srcOff4];
                                dstOff = dstOff5 + 1;
                                srcOff = srcOff5 + 1;
                                dstPts[dstOff5] = mzz2 * srcPts[srcOff5];
                            } else {
                                return;
                            }
                        }
                    }
                }
                throw new NonInvertibleTransformException("Determinant is 0");
            case 3:
                double mxx3 = getMxx();
                double tx2 = getTx();
                double myy3 = getMyy();
                double ty2 = getTy();
                double mzz3 = getMzz();
                double tz2 = getTz();
                if (mxx3 == 0.0d || myy3 == 0.0d || mzz3 == 0.0d) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                double tx3 = (-tx2) / mxx3;
                double ty3 = (-ty2) / myy3;
                double tz3 = (-tz2) / mzz3;
                double mxx4 = 1.0d / mxx3;
                double myy4 = 1.0d / myy3;
                double mzz4 = 1.0d / mzz3;
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i6 = dstOff;
                        int dstOff6 = dstOff + 1;
                        int i7 = srcOff;
                        int srcOff6 = srcOff + 1;
                        dstPts[i6] = (mxx4 * srcPts[i7]) + tx3;
                        int dstOff7 = dstOff6 + 1;
                        int srcOff7 = srcOff6 + 1;
                        dstPts[dstOff6] = (myy4 * srcPts[srcOff6]) + ty3;
                        dstOff = dstOff7 + 1;
                        srcOff = srcOff7 + 1;
                        dstPts[dstOff7] = (mzz4 * srcPts[srcOff7]) + tz3;
                    } else {
                        return;
                    }
                }
                break;
            case 4:
                super.inverseTransform3DPointsImpl(srcPts, srcOff, dstPts, dstOff, numPts);
                return;
            default:
                stateError();
                break;
        }
        switch (this.state2d) {
            case 0:
                if (srcPts != dstPts || srcOff != dstOff) {
                    System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 3);
                    return;
                }
                return;
            case 1:
                double tx4 = getTx();
                double ty4 = getTy();
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i8 = dstOff;
                        int dstOff8 = dstOff + 1;
                        int i9 = srcOff;
                        int srcOff8 = srcOff + 1;
                        dstPts[i8] = srcPts[i9] - tx4;
                        int dstOff9 = dstOff8 + 1;
                        int srcOff9 = srcOff8 + 1;
                        dstPts[dstOff8] = srcPts[srcOff8] - ty4;
                        dstOff = dstOff9 + 1;
                        srcOff = srcOff9 + 1;
                        dstPts[dstOff9] = srcPts[srcOff9];
                    } else {
                        return;
                    }
                }
            case 2:
                double mxx5 = getMxx();
                double myy5 = getMyy();
                if (mxx5 == 0.0d || myy5 == 0.0d) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                double mxx6 = 1.0d / mxx5;
                double myy6 = 1.0d / myy5;
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i10 = dstOff;
                        int dstOff10 = dstOff + 1;
                        int i11 = srcOff;
                        int srcOff10 = srcOff + 1;
                        dstPts[i10] = mxx6 * srcPts[i11];
                        int dstOff11 = dstOff10 + 1;
                        int srcOff11 = srcOff10 + 1;
                        dstPts[dstOff10] = myy6 * srcPts[srcOff10];
                        dstOff = dstOff11 + 1;
                        srcOff = srcOff11 + 1;
                        dstPts[dstOff11] = srcPts[srcOff11];
                    } else {
                        return;
                    }
                }
                break;
            case 3:
                double mxx7 = getMxx();
                double tx5 = getTx();
                double myy7 = getMyy();
                double ty5 = getTy();
                if (mxx7 == 0.0d || myy7 == 0.0d) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                double tx6 = (-tx5) / mxx7;
                double ty6 = (-ty5) / myy7;
                double mxx8 = 1.0d / mxx7;
                double myy8 = 1.0d / myy7;
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i12 = dstOff;
                        int dstOff12 = dstOff + 1;
                        int i13 = srcOff;
                        int srcOff12 = srcOff + 1;
                        dstPts[i12] = (mxx8 * srcPts[i13]) + tx6;
                        int dstOff13 = dstOff12 + 1;
                        int srcOff13 = srcOff12 + 1;
                        dstPts[dstOff12] = (myy8 * srcPts[srcOff12]) + ty6;
                        dstOff = dstOff13 + 1;
                        srcOff = srcOff13 + 1;
                        dstPts[dstOff13] = srcPts[srcOff13];
                    } else {
                        return;
                    }
                }
                break;
            case 4:
                double mxy = getMxy();
                double myx = getMyx();
                if (mxy == 0.0d || myx == 0.0d) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                double myx2 = 1.0d / mxy;
                double mxy2 = 1.0d / myx;
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i14 = srcOff;
                        int srcOff14 = srcOff + 1;
                        double x2 = srcPts[i14];
                        int i15 = dstOff;
                        int dstOff14 = dstOff + 1;
                        int srcOff15 = srcOff14 + 1;
                        dstPts[i15] = mxy2 * srcPts[srcOff14];
                        int dstOff15 = dstOff14 + 1;
                        dstPts[dstOff14] = myx2 * x2;
                        dstOff = dstOff15 + 1;
                        srcOff = srcOff15 + 1;
                        dstPts[dstOff15] = srcPts[srcOff15];
                    } else {
                        return;
                    }
                }
                break;
            case 5:
                double mxy3 = getMxy();
                double tx7 = getTx();
                double myx3 = getMyx();
                double ty7 = getTy();
                if (mxy3 == 0.0d || myx3 == 0.0d) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                double tx8 = (-ty7) / myx3;
                double ty8 = (-tx7) / mxy3;
                double myx4 = 1.0d / mxy3;
                double mxy4 = 1.0d / myx3;
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i16 = srcOff;
                        int srcOff16 = srcOff + 1;
                        double x3 = srcPts[i16];
                        int i17 = dstOff;
                        int dstOff16 = dstOff + 1;
                        int srcOff17 = srcOff16 + 1;
                        dstPts[i17] = (mxy4 * srcPts[srcOff16]) + tx8;
                        int dstOff17 = dstOff16 + 1;
                        dstPts[dstOff16] = (myx4 * x3) + ty8;
                        dstOff = dstOff17 + 1;
                        srcOff = srcOff17 + 1;
                        dstPts[dstOff17] = srcPts[srcOff17];
                    } else {
                        return;
                    }
                }
                break;
            default:
                super.inverseTransform3DPointsImpl(srcPts, srcOff, dstPts, dstOff, numPts);
                return;
        }
    }

    @Override // javafx.scene.transform.Transform
    public Point2D inverseDeltaTransform(double x2, double y2) throws NonInvertibleTransformException {
        ensureCanTransform2DPoint();
        switch (this.state2d) {
            case 0:
            case 1:
                return new Point2D(x2, y2);
            case 2:
            case 3:
                double mxx_s = getMxx();
                double myy_s = getMyy();
                if (mxx_s == 0.0d || myy_s == 0.0d) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                return new Point2D((1.0d / mxx_s) * x2, (1.0d / myy_s) * y2);
            case 4:
            case 5:
                double mxy_s = getMxy();
                double myx_s = getMyx();
                if (mxy_s == 0.0d || myx_s == 0.0d) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                return new Point2D((1.0d / myx_s) * y2, (1.0d / mxy_s) * x2);
            default:
                return super.inverseDeltaTransform(x2, y2);
        }
    }

    @Override // javafx.scene.transform.Transform
    public Point3D inverseDeltaTransform(double x2, double y2, double z2) throws NonInvertibleTransformException {
        switch (this.state3d) {
            case 0:
                break;
            case 1:
                return new Point3D(x2, y2, z2);
            case 2:
            case 3:
                double mxx_s = getMxx();
                double myy_s = getMyy();
                double mzz_s = getMzz();
                if (mxx_s == 0.0d || myy_s == 0.0d || mzz_s == 0.0d) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                return new Point3D((1.0d / mxx_s) * x2, (1.0d / myy_s) * y2, (1.0d / mzz_s) * z2);
            case 4:
                return super.inverseDeltaTransform(x2, y2, z2);
            default:
                stateError();
                break;
        }
        switch (this.state2d) {
            case 0:
            case 1:
                return new Point3D(x2, y2, z2);
            case 2:
            case 3:
                double mxx_s2 = getMxx();
                double myy_s2 = getMyy();
                if (mxx_s2 == 0.0d || myy_s2 == 0.0d) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                return new Point3D((1.0d / mxx_s2) * x2, (1.0d / myy_s2) * y2, z2);
            case 4:
            case 5:
                double mxy_s = getMxy();
                double myx_s = getMyx();
                if (mxy_s == 0.0d || myx_s == 0.0d) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                return new Point3D((1.0d / myx_s) * y2, (1.0d / mxy_s) * x2, z2);
            default:
                return super.inverseDeltaTransform(x2, y2, z2);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Affine [\n");
        sb.append("\t").append(getMxx());
        sb.append(", ").append(getMxy());
        sb.append(", ").append(getMxz());
        sb.append(", ").append(getTx());
        sb.append('\n');
        sb.append("\t").append(getMyx());
        sb.append(", ").append(getMyy());
        sb.append(", ").append(getMyz());
        sb.append(", ").append(getTy());
        sb.append('\n');
        sb.append("\t").append(getMzx());
        sb.append(", ").append(getMzy());
        sb.append(", ").append(getMzz());
        sb.append(", ").append(getTz());
        return sb.append("\n]").toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateState() {
        updateState2D();
        this.state3d = 0;
        if (getMxz() != 0.0d || getMyz() != 0.0d || getMzx() != 0.0d || getMzy() != 0.0d) {
            this.state3d = 4;
            return;
        }
        if ((this.state2d & 4) != 0) {
            if (getMzz() != 1.0d || getTz() != 0.0d) {
                this.state3d = 4;
                return;
            }
            return;
        }
        if (getTz() != 0.0d) {
            this.state3d |= 1;
        }
        if (getMzz() != 1.0d) {
            this.state3d |= 2;
        }
        if (this.state3d != 0) {
            this.state3d |= this.state2d & 3;
        }
    }

    private void updateState2D() {
        if (getMxy() == 0.0d && getMyx() == 0.0d) {
            if (getMxx() == 1.0d && getMyy() == 1.0d) {
                if (getTx() == 0.0d && getTy() == 0.0d) {
                    this.state2d = 0;
                    return;
                } else {
                    this.state2d = 1;
                    return;
                }
            }
            if (getTx() == 0.0d && getTy() == 0.0d) {
                this.state2d = 2;
                return;
            } else {
                this.state2d = 3;
                return;
            }
        }
        if (getMxx() == 0.0d && getMyy() == 0.0d) {
            if (getTx() == 0.0d && getTy() == 0.0d) {
                this.state2d = 4;
                return;
            } else {
                this.state2d = 5;
                return;
            }
        }
        if (getTx() == 0.0d && getTy() == 0.0d) {
            this.state2d = 6;
        } else {
            this.state2d = 7;
        }
    }

    private static void stateError() {
        throw new InternalError("missing case in a switch");
    }

    @Override // javafx.scene.transform.Transform
    @Deprecated
    public void impl_apply(Affine3D trans) {
        trans.concatenate(getMxx(), getMxy(), getMxz(), getTx(), getMyx(), getMyy(), getMyz(), getTy(), getMzx(), getMzy(), getMzz(), getTz());
    }

    @Override // javafx.scene.transform.Transform
    @Deprecated
    public BaseTransform impl_derive(BaseTransform trans) {
        switch (this.state3d) {
            case 0:
                break;
            case 1:
                return trans.deriveWithTranslation(getTx(), getTy(), getTz());
            case 2:
                return trans.deriveWithScale(getMxx(), getMyy(), getMzz());
            case 3:
            case 4:
                return trans.deriveWithConcatenation(getMxx(), getMxy(), getMxz(), getTx(), getMyx(), getMyy(), getMyz(), getTy(), getMzx(), getMzy(), getMzz(), getTz());
            default:
                stateError();
                break;
        }
        switch (this.state2d) {
            case 0:
                return trans;
            case 1:
                return trans.deriveWithTranslation(getTx(), getTy());
            case 2:
                return trans.deriveWithScale(getMxx(), getMyy(), 1.0d);
            case 3:
            default:
                return trans.deriveWithConcatenation(getMxx(), getMyx(), getMxy(), getMyy(), getTx(), getTy());
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/transform/Affine$AffineAtomicChange.class */
    private class AffineAtomicChange {
        private boolean running;

        private AffineAtomicChange() {
            this.running = false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void start() {
            if (!this.running) {
                if (Affine.this.mxx != null) {
                    Affine.this.mxx.preProcessAtomicChange();
                }
                if (Affine.this.mxy != null) {
                    Affine.this.mxy.preProcessAtomicChange();
                }
                if (Affine.this.mxz != null) {
                    Affine.this.mxz.preProcessAtomicChange();
                }
                if (Affine.this.tx != null) {
                    Affine.this.tx.preProcessAtomicChange();
                }
                if (Affine.this.myx != null) {
                    Affine.this.myx.preProcessAtomicChange();
                }
                if (Affine.this.myy != null) {
                    Affine.this.myy.preProcessAtomicChange();
                }
                if (Affine.this.myz != null) {
                    Affine.this.myz.preProcessAtomicChange();
                }
                if (Affine.this.ty != null) {
                    Affine.this.ty.preProcessAtomicChange();
                }
                if (Affine.this.mzx != null) {
                    Affine.this.mzx.preProcessAtomicChange();
                }
                if (Affine.this.mzy != null) {
                    Affine.this.mzy.preProcessAtomicChange();
                }
                if (Affine.this.mzz != null) {
                    Affine.this.mzz.preProcessAtomicChange();
                }
                if (Affine.this.tz != null) {
                    Affine.this.tz.preProcessAtomicChange();
                }
                this.running = true;
                return;
            }
            throw new InternalError("Affine internal error: trying to run inner atomic operation");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void end() {
            this.running = false;
            Affine.this.transformChanged();
            if (Affine.this.mxx != null) {
                Affine.this.mxx.postProcessAtomicChange();
            }
            if (Affine.this.mxy != null) {
                Affine.this.mxy.postProcessAtomicChange();
            }
            if (Affine.this.mxz != null) {
                Affine.this.mxz.postProcessAtomicChange();
            }
            if (Affine.this.tx != null) {
                Affine.this.tx.postProcessAtomicChange();
            }
            if (Affine.this.myx != null) {
                Affine.this.myx.postProcessAtomicChange();
            }
            if (Affine.this.myy != null) {
                Affine.this.myy.postProcessAtomicChange();
            }
            if (Affine.this.myz != null) {
                Affine.this.myz.postProcessAtomicChange();
            }
            if (Affine.this.ty != null) {
                Affine.this.ty.postProcessAtomicChange();
            }
            if (Affine.this.mzx != null) {
                Affine.this.mzx.postProcessAtomicChange();
            }
            if (Affine.this.mzy != null) {
                Affine.this.mzy.postProcessAtomicChange();
            }
            if (Affine.this.mzz != null) {
                Affine.this.mzz.postProcessAtomicChange();
            }
            if (Affine.this.tz != null) {
                Affine.this.tz.postProcessAtomicChange();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void cancel() {
            this.running = false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean runs() {
            return this.running;
        }
    }

    int getState2d() {
        return this.state2d;
    }

    int getState3d() {
        return this.state3d;
    }

    boolean atomicChangeRuns() {
        return this.atomicChange.runs();
    }
}
