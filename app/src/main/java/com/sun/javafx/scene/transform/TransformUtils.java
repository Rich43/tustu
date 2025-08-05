package com.sun.javafx.scene.transform;

import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/transform/TransformUtils.class */
public class TransformUtils {
    public static Transform immutableTransform(double mxx, double mxy, double mxz, double tx, double myx, double myy, double myz, double ty, double mzx, double mzy, double mzz, double tz) {
        return new ImmutableTransform(mxx, mxy, mxz, tx, myx, myy, myz, ty, mzx, mzy, mzz, tz);
    }

    public static Transform immutableTransform(Transform t2) {
        return new ImmutableTransform(t2.getMxx(), t2.getMxy(), t2.getMxz(), t2.getTx(), t2.getMyx(), t2.getMyy(), t2.getMyz(), t2.getTy(), t2.getMzx(), t2.getMzy(), t2.getMzz(), t2.getTz());
    }

    public static Transform immutableTransform(Transform reuse, double mxx, double mxy, double mxz, double tx, double myx, double myy, double myz, double ty, double mzx, double mzy, double mzz, double tz) {
        if (reuse == null) {
            return new ImmutableTransform(mxx, mxy, mxz, tx, myx, myy, myz, ty, mzx, mzy, mzz, tz);
        }
        ((ImmutableTransform) reuse).setToTransform(mxx, mxy, mxz, tx, myx, myy, myz, ty, mzx, mzy, mzz, tz);
        return reuse;
    }

    public static Transform immutableTransform(Transform reuse, Transform t2) {
        return immutableTransform((ImmutableTransform) reuse, t2.getMxx(), t2.getMxy(), t2.getMxz(), t2.getTx(), t2.getMyx(), t2.getMyy(), t2.getMyz(), t2.getTy(), t2.getMzx(), t2.getMzy(), t2.getMzz(), t2.getTz());
    }

    public static Transform immutableTransform(Transform reuse, Transform left, Transform right) {
        if (reuse == null) {
            reuse = new ImmutableTransform();
        }
        ((ImmutableTransform) reuse).setToConcatenation((ImmutableTransform) left, (ImmutableTransform) right);
        return reuse;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/transform/TransformUtils$ImmutableTransform.class */
    static class ImmutableTransform extends Transform {
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

        /* JADX WARN: Multi-variable type inference failed */
        public ImmutableTransform() {
            this.zz = 1.0d;
            this.yy = 1.0d;
            4607182418800017408.xx = this;
        }

        public ImmutableTransform(Transform transform) {
            this(transform.getMxx(), transform.getMxy(), transform.getMxz(), transform.getTx(), transform.getMyx(), transform.getMyy(), transform.getMyz(), transform.getTy(), transform.getMzx(), transform.getMzy(), transform.getMzz(), transform.getTz());
        }

        public ImmutableTransform(double mxx, double mxy, double mxz, double tx, double myx, double myy, double myz, double ty, double mzx, double mzy, double mzz, double tz) {
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

        /* JADX INFO: Access modifiers changed from: private */
        public void setToTransform(double mxx, double mxy, double mxz, double tx, double myx, double myy, double myz, double ty, double mzx, double mzy, double mzz, double tz) {
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

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r5v0, types: [com.sun.javafx.scene.transform.TransformUtils$ImmutableTransform] */
        public void setToConcatenation(ImmutableTransform left, ImmutableTransform right) {
            if (left.state3d == 0 && right.state3d == 0) {
                this.xx = (left.xx * right.xx) + (left.xy * right.yx);
                this.xy = (left.xx * right.xy) + (left.xy * right.yy);
                this.xt = (left.xx * right.xt) + (left.xy * right.yt) + left.xt;
                this.yx = (left.yx * right.xx) + (left.yy * right.yx);
                this.yy = (left.yx * right.xy) + (left.yy * right.yy);
                this.yt = (left.yx * right.xt) + (left.yy * right.yt) + left.yt;
                if (this.state3d != 0) {
                    ?? r5 = 0;
                    this.zt = 0.0d;
                    this.zy = 0.0d;
                    r5.zx = this;
                    this.yz = this;
                    this.xz = 0.0d;
                    this.zz = 1.0d;
                    this.state3d = 0;
                }
                updateState2D();
                return;
            }
            this.xx = (left.xx * right.xx) + (left.xy * right.yx) + (left.xz * right.zx);
            this.xy = (left.xx * right.xy) + (left.xy * right.yy) + (left.xz * right.zy);
            this.xz = (left.xx * right.xz) + (left.xy * right.yz) + (left.xz * right.zz);
            this.xt = (left.xx * right.xt) + (left.xy * right.yt) + (left.xz * right.zt) + left.xt;
            this.yx = (left.yx * right.xx) + (left.yy * right.yx) + (left.yz * right.zx);
            this.yy = (left.yx * right.xy) + (left.yy * right.yy) + (left.yz * right.zy);
            this.yz = (left.yx * right.xz) + (left.yy * right.yz) + (left.yz * right.zz);
            this.yt = (left.yx * right.xt) + (left.yy * right.yt) + (left.yz * right.zt) + left.yt;
            this.zx = (left.zx * right.xx) + (left.zy * right.yx) + (left.zz * right.zx);
            this.zy = (left.zx * right.xy) + (left.zy * right.yy) + (left.zz * right.zy);
            this.zz = (left.zx * right.xz) + (left.zy * right.yz) + (left.zz * right.zz);
            this.zt = (left.zx * right.xt) + (left.zy * right.yt) + (left.zz * right.zt) + left.zt;
            updateState();
        }

        @Override // javafx.scene.transform.Transform
        public double getMxx() {
            return this.xx;
        }

        @Override // javafx.scene.transform.Transform
        public double getMxy() {
            return this.xy;
        }

        @Override // javafx.scene.transform.Transform
        public double getMxz() {
            return this.xz;
        }

        @Override // javafx.scene.transform.Transform
        public double getTx() {
            return this.xt;
        }

        @Override // javafx.scene.transform.Transform
        public double getMyx() {
            return this.yx;
        }

        @Override // javafx.scene.transform.Transform
        public double getMyy() {
            return this.yy;
        }

        @Override // javafx.scene.transform.Transform
        public double getMyz() {
            return this.yz;
        }

        @Override // javafx.scene.transform.Transform
        public double getTy() {
            return this.yt;
        }

        @Override // javafx.scene.transform.Transform
        public double getMzx() {
            return this.zx;
        }

        @Override // javafx.scene.transform.Transform
        public double getMzy() {
            return this.zy;
        }

        @Override // javafx.scene.transform.Transform
        public double getMzz() {
            return this.zz;
        }

        @Override // javafx.scene.transform.Transform
        public double getTz() {
            return this.zt;
        }

        @Override // javafx.scene.transform.Transform
        public double determinant() {
            switch (this.state3d) {
                case 0:
                    break;
                case 1:
                    return 1.0d;
                case 2:
                case 3:
                    return this.xx * this.yy * this.zz;
                case 4:
                    return (this.xx * ((this.yy * this.zz) - (this.zy * this.yz))) + (this.xy * ((this.yz * this.zx) - (this.zz * this.yx))) + (this.xz * ((this.yx * this.zy) - (this.zx * this.yy)));
                default:
                    stateError();
                    break;
            }
            switch (this.state2d) {
                case 0:
                case 1:
                    return 1.0d;
                case 2:
                case 3:
                    return this.xx * this.yy;
                case 4:
                case 5:
                    return -(this.xy * this.yx);
                case 6:
                case 7:
                    break;
                default:
                    stateError();
                    break;
            }
            return (this.xx * this.yy) - (this.xy * this.yx);
        }

        @Override // javafx.scene.transform.Transform
        public Transform createConcatenation(Transform transform) {
            Affine a2 = new Affine(this);
            a2.append(transform);
            return a2;
        }

        @Override // javafx.scene.transform.Transform
        public Affine createInverse() throws NonInvertibleTransformException {
            Affine t2 = new Affine(this);
            t2.invert();
            return t2;
        }

        @Override // javafx.scene.transform.Transform
        /* renamed from: clone */
        public Transform mo1183clone() {
            return new ImmutableTransform(this);
        }

        @Override // javafx.scene.transform.Transform
        public Point2D transform(double x2, double y2) throws IllegalStateException {
            ensureCanTransform2DPoint();
            switch (this.state2d) {
                case 0:
                    return new Point2D(x2, y2);
                case 1:
                    return new Point2D(x2 + this.xt, y2 + this.yt);
                case 2:
                    return new Point2D(this.xx * x2, this.yy * y2);
                case 3:
                    return new Point2D((this.xx * x2) + this.xt, (this.yy * y2) + this.yt);
                case 4:
                    return new Point2D(this.xy * y2, this.yx * x2);
                case 5:
                    return new Point2D((this.xy * y2) + this.xt, (this.yx * x2) + this.yt);
                case 6:
                    return new Point2D((this.xx * x2) + (this.xy * y2), (this.yx * x2) + (this.yy * y2));
                case 7:
                    break;
                default:
                    stateError();
                    break;
            }
            return new Point2D((this.xx * x2) + (this.xy * y2) + this.xt, (this.yx * x2) + (this.yy * y2) + this.yt);
        }

        @Override // javafx.scene.transform.Transform
        public Point3D transform(double x2, double y2, double z2) {
            switch (this.state3d) {
                case 0:
                    break;
                case 1:
                    return new Point3D(x2 + this.xt, y2 + this.yt, z2 + this.zt);
                case 2:
                    return new Point3D(this.xx * x2, this.yy * y2, this.zz * z2);
                case 3:
                    return new Point3D((this.xx * x2) + this.xt, (this.yy * y2) + this.yt, (this.zz * z2) + this.zt);
                case 4:
                    return new Point3D((this.xx * x2) + (this.xy * y2) + (this.xz * z2) + this.xt, (this.yx * x2) + (this.yy * y2) + (this.yz * z2) + this.yt, (this.zx * x2) + (this.zy * y2) + (this.zz * z2) + this.zt);
                default:
                    stateError();
                    break;
            }
            switch (this.state2d) {
                case 0:
                    return new Point3D(x2, y2, z2);
                case 1:
                    return new Point3D(x2 + this.xt, y2 + this.yt, z2);
                case 2:
                    return new Point3D(this.xx * x2, this.yy * y2, z2);
                case 3:
                    return new Point3D((this.xx * x2) + this.xt, (this.yy * y2) + this.yt, z2);
                case 4:
                    return new Point3D(this.xy * y2, this.yx * x2, z2);
                case 5:
                    return new Point3D((this.xy * y2) + this.xt, (this.yx * x2) + this.yt, z2);
                case 6:
                    return new Point3D((this.xx * x2) + (this.xy * y2), (this.yx * x2) + (this.yy * y2), z2);
                case 7:
                    break;
                default:
                    stateError();
                    break;
            }
            return new Point3D((this.xx * x2) + (this.xy * y2) + this.xt, (this.yx * x2) + (this.yy * y2) + this.yt, z2);
        }

        @Override // javafx.scene.transform.Transform
        public Point2D deltaTransform(double x2, double y2) throws IllegalStateException {
            ensureCanTransform2DPoint();
            switch (this.state2d) {
                case 0:
                case 1:
                    return new Point2D(x2, y2);
                case 2:
                case 3:
                    return new Point2D(this.xx * x2, this.yy * y2);
                case 4:
                case 5:
                    return new Point2D(this.xy * y2, this.yx * x2);
                case 6:
                case 7:
                    break;
                default:
                    stateError();
                    break;
            }
            return new Point2D((this.xx * x2) + (this.xy * y2), (this.yx * x2) + (this.yy * y2));
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
                    return new Point3D(this.xx * x2, this.yy * y2, this.zz * z2);
                case 4:
                    return new Point3D((this.xx * x2) + (this.xy * y2) + (this.xz * z2), (this.yx * x2) + (this.yy * y2) + (this.yz * z2), (this.zx * x2) + (this.zy * y2) + (this.zz * z2));
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
                    return new Point3D(this.xx * x2, this.yy * y2, z2);
                case 4:
                case 5:
                    return new Point3D(this.xy * y2, this.yx * x2, z2);
                case 6:
                case 7:
                    break;
                default:
                    stateError();
                    break;
            }
            return new Point3D((this.xx * x2) + (this.xy * y2), (this.yx * x2) + (this.yy * y2), z2);
        }

        @Override // javafx.scene.transform.Transform
        public Point2D inverseTransform(double x2, double y2) throws IllegalStateException, NonInvertibleTransformException {
            ensureCanTransform2DPoint();
            switch (this.state2d) {
                case 0:
                    return new Point2D(x2, y2);
                case 1:
                    return new Point2D(x2 - this.xt, y2 - this.yt);
                case 2:
                    if (this.xx == 0.0d || this.yy == 0.0d) {
                        throw new NonInvertibleTransformException("Determinant is 0");
                    }
                    return new Point2D((1.0d / this.xx) * x2, (1.0d / this.yy) * y2);
                case 3:
                    if (this.xx == 0.0d || this.yy == 0.0d) {
                        throw new NonInvertibleTransformException("Determinant is 0");
                    }
                    return new Point2D(((1.0d / this.xx) * x2) - (this.xt / this.xx), ((1.0d / this.yy) * y2) - (this.yt / this.yy));
                case 4:
                    if (this.xy == 0.0d || this.yx == 0.0d) {
                        throw new NonInvertibleTransformException("Determinant is 0");
                    }
                    return new Point2D((1.0d / this.yx) * y2, (1.0d / this.xy) * x2);
                case 5:
                    if (this.xy == 0.0d || this.yx == 0.0d) {
                        throw new NonInvertibleTransformException("Determinant is 0");
                    }
                    return new Point2D(((1.0d / this.yx) * y2) - (this.yt / this.yx), ((1.0d / this.xy) * x2) - (this.xt / this.xy));
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
                    return new Point3D(x2 - this.xt, y2 - this.yt, z2 - this.zt);
                case 2:
                    if (this.xx == 0.0d || this.yy == 0.0d || this.zz == 0.0d) {
                        throw new NonInvertibleTransformException("Determinant is 0");
                    }
                    return new Point3D((1.0d / this.xx) * x2, (1.0d / this.yy) * y2, (1.0d / this.zz) * z2);
                case 3:
                    if (this.xx == 0.0d || this.yy == 0.0d || this.zz == 0.0d) {
                        throw new NonInvertibleTransformException("Determinant is 0");
                    }
                    return new Point3D(((1.0d / this.xx) * x2) - (this.xt / this.xx), ((1.0d / this.yy) * y2) - (this.yt / this.yy), ((1.0d / this.zz) * z2) - (this.zt / this.zz));
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
                    return new Point3D(x2 - this.xt, y2 - this.yt, z2);
                case 2:
                    if (this.xx == 0.0d || this.yy == 0.0d) {
                        throw new NonInvertibleTransformException("Determinant is 0");
                    }
                    return new Point3D((1.0d / this.xx) * x2, (1.0d / this.yy) * y2, z2);
                case 3:
                    if (this.xx == 0.0d || this.yy == 0.0d) {
                        throw new NonInvertibleTransformException("Determinant is 0");
                    }
                    return new Point3D(((1.0d / this.xx) * x2) - (this.xt / this.xx), ((1.0d / this.yy) * y2) - (this.yt / this.yy), z2);
                case 4:
                    if (this.xy == 0.0d || this.yx == 0.0d) {
                        throw new NonInvertibleTransformException("Determinant is 0");
                    }
                    return new Point3D((1.0d / this.yx) * y2, (1.0d / this.xy) * x2, z2);
                case 5:
                    if (this.xy == 0.0d || this.yx == 0.0d) {
                        throw new NonInvertibleTransformException("Determinant is 0");
                    }
                    return new Point3D(((1.0d / this.yx) * y2) - (this.yt / this.yx), ((1.0d / this.xy) * x2) - (this.xt / this.xy), z2);
                default:
                    return super.inverseTransform(x2, y2, z2);
            }
        }

        @Override // javafx.scene.transform.Transform
        public Point2D inverseDeltaTransform(double x2, double y2) throws IllegalStateException, NonInvertibleTransformException {
            ensureCanTransform2DPoint();
            switch (this.state2d) {
                case 0:
                case 1:
                    return new Point2D(x2, y2);
                case 2:
                case 3:
                    if (this.xx == 0.0d || this.yy == 0.0d) {
                        throw new NonInvertibleTransformException("Determinant is 0");
                    }
                    return new Point2D((1.0d / this.xx) * x2, (1.0d / this.yy) * y2);
                case 4:
                case 5:
                    if (this.xy == 0.0d || this.yx == 0.0d) {
                        throw new NonInvertibleTransformException("Determinant is 0");
                    }
                    return new Point2D((1.0d / this.yx) * y2, (1.0d / this.xy) * x2);
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
                    if (this.xx == 0.0d || this.yy == 0.0d || this.zz == 0.0d) {
                        throw new NonInvertibleTransformException("Determinant is 0");
                    }
                    return new Point3D((1.0d / this.xx) * x2, (1.0d / this.yy) * y2, (1.0d / this.zz) * z2);
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
                    if (this.xx == 0.0d || this.yy == 0.0d) {
                        throw new NonInvertibleTransformException("Determinant is 0");
                    }
                    return new Point3D((1.0d / this.xx) * x2, (1.0d / this.yy) * y2, z2);
                case 4:
                case 5:
                    if (this.xy == 0.0d || this.yx == 0.0d) {
                        throw new NonInvertibleTransformException("Determinant is 0");
                    }
                    return new Point3D((1.0d / this.yx) * y2, (1.0d / this.xy) * x2, z2);
                default:
                    return super.inverseDeltaTransform(x2, y2, z2);
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("Transform [\n");
            sb.append("\t").append(this.xx);
            sb.append(", ").append(this.xy);
            sb.append(", ").append(this.xz);
            sb.append(", ").append(this.xt);
            sb.append('\n');
            sb.append("\t").append(this.yx);
            sb.append(", ").append(this.yy);
            sb.append(", ").append(this.yz);
            sb.append(", ").append(this.yt);
            sb.append('\n');
            sb.append("\t").append(this.zx);
            sb.append(", ").append(this.zy);
            sb.append(", ").append(this.zz);
            sb.append(", ").append(this.zt);
            return sb.append("\n]").toString();
        }

        private void updateState() {
            updateState2D();
            this.state3d = 0;
            if (this.xz != 0.0d || this.yz != 0.0d || this.zx != 0.0d || this.zy != 0.0d) {
                this.state3d = 4;
                return;
            }
            if ((this.state2d & 4) != 0) {
                if (this.zz != 1.0d || this.zt != 0.0d) {
                    this.state3d = 4;
                    return;
                }
                return;
            }
            if (this.zt != 0.0d) {
                this.state3d |= 1;
            }
            if (this.zz != 1.0d) {
                this.state3d |= 2;
            }
            if (this.state3d != 0) {
                this.state3d |= this.state2d & 3;
            }
        }

        private void updateState2D() {
            if (this.xy == 0.0d && this.yx == 0.0d) {
                if (this.xx == 1.0d && this.yy == 1.0d) {
                    if (this.xt == 0.0d && this.yt == 0.0d) {
                        this.state2d = 0;
                        return;
                    } else {
                        this.state2d = 1;
                        return;
                    }
                }
                if (this.xt == 0.0d && this.yt == 0.0d) {
                    this.state2d = 2;
                    return;
                } else {
                    this.state2d = 3;
                    return;
                }
            }
            if (this.xx == 0.0d && this.yy == 0.0d) {
                if (this.xt == 0.0d && this.yt == 0.0d) {
                    this.state2d = 4;
                    return;
                } else {
                    this.state2d = 5;
                    return;
                }
            }
            if (this.xt == 0.0d && this.yt == 0.0d) {
                this.state2d = 6;
            } else {
                this.state2d = 7;
            }
        }

        void ensureCanTransform2DPoint() throws IllegalStateException {
            if (this.state3d != 0) {
                throw new IllegalStateException("Cannot transform 2D point with a 3D transform");
            }
        }

        private static void stateError() {
            throw new InternalError("missing case in a switch");
        }

        @Override // javafx.scene.transform.Transform
        @Deprecated
        public void impl_apply(Affine3D trans) {
            trans.concatenate(this.xx, this.xy, this.xz, this.xt, this.yx, this.yy, this.yz, this.yt, this.zx, this.zy, this.zz, this.zt);
        }

        @Override // javafx.scene.transform.Transform
        @Deprecated
        public BaseTransform impl_derive(BaseTransform trans) {
            return trans.deriveWithConcatenation(this.xx, this.xy, this.xz, this.xt, this.yx, this.yy, this.yz, this.yt, this.zx, this.zy, this.zz, this.zt);
        }

        int getState2d() {
            return this.state2d;
        }

        int getState3d() {
            return this.state3d;
        }
    }
}
