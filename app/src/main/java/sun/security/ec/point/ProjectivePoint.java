package sun.security.ec.point;

import sun.security.util.math.ImmutableIntegerModuloP;
import sun.security.util.math.IntegerFieldModuloP;
import sun.security.util.math.IntegerModuloP;
import sun.security.util.math.MutableIntegerModuloP;

/* loaded from: sunec.jar:sun/security/ec/point/ProjectivePoint.class */
public abstract class ProjectivePoint<T extends IntegerModuloP> implements Point {

    /* renamed from: x, reason: collision with root package name */
    protected final T f13607x;

    /* renamed from: y, reason: collision with root package name */
    protected final T f13608y;

    /* renamed from: z, reason: collision with root package name */
    protected final T f13609z;

    protected ProjectivePoint(T t2, T t3, T t4) {
        this.f13607x = t2;
        this.f13608y = t3;
        this.f13609z = t4;
    }

    @Override // sun.security.ec.point.Point
    public IntegerFieldModuloP getField() {
        return this.f13607x.getField();
    }

    @Override // sun.security.ec.point.Point
    public Immutable fixed() {
        return new Immutable(this.f13607x.fixed(), this.f13608y.fixed(), this.f13609z.fixed());
    }

    @Override // sun.security.ec.point.Point
    public Mutable mutable() {
        return new Mutable(this.f13607x.mutable(), this.f13608y.mutable(), this.f13609z.mutable());
    }

    public T getX() {
        return this.f13607x;
    }

    public T getY() {
        return this.f13608y;
    }

    public T getZ() {
        return this.f13609z;
    }

    @Override // sun.security.ec.point.Point
    public AffinePoint asAffine() {
        ImmutableIntegerModuloP immutableIntegerModuloPMultiplicativeInverse = this.f13609z.multiplicativeInverse();
        return new AffinePoint(this.f13607x.multiply(immutableIntegerModuloPMultiplicativeInverse), this.f13608y.multiply(immutableIntegerModuloPMultiplicativeInverse));
    }

    /* loaded from: sunec.jar:sun/security/ec/point/ProjectivePoint$Immutable.class */
    public static class Immutable extends ProjectivePoint<ImmutableIntegerModuloP> implements ImmutablePoint {
        @Override // sun.security.ec.point.ProjectivePoint, sun.security.ec.point.Point
        public /* bridge */ /* synthetic */ MutablePoint mutable() {
            return super.mutable();
        }

        @Override // sun.security.ec.point.ProjectivePoint, sun.security.ec.point.Point
        public /* bridge */ /* synthetic */ ImmutablePoint fixed() {
            return super.fixed();
        }

        public Immutable(ImmutableIntegerModuloP immutableIntegerModuloP, ImmutableIntegerModuloP immutableIntegerModuloP2, ImmutableIntegerModuloP immutableIntegerModuloP3) {
            super(immutableIntegerModuloP, immutableIntegerModuloP2, immutableIntegerModuloP3);
        }
    }

    /* loaded from: sunec.jar:sun/security/ec/point/ProjectivePoint$Mutable.class */
    public static class Mutable extends ProjectivePoint<MutableIntegerModuloP> implements MutablePoint {
        @Override // sun.security.ec.point.ProjectivePoint, sun.security.ec.point.Point
        public /* bridge */ /* synthetic */ MutablePoint mutable() {
            return super.mutable();
        }

        @Override // sun.security.ec.point.ProjectivePoint, sun.security.ec.point.Point
        public /* bridge */ /* synthetic */ ImmutablePoint fixed() {
            return super.fixed();
        }

        public Mutable(MutableIntegerModuloP mutableIntegerModuloP, MutableIntegerModuloP mutableIntegerModuloP2, MutableIntegerModuloP mutableIntegerModuloP3) {
            super(mutableIntegerModuloP, mutableIntegerModuloP2, mutableIntegerModuloP3);
        }

        public Mutable(IntegerFieldModuloP integerFieldModuloP) {
            super(integerFieldModuloP.get0().mutable(), integerFieldModuloP.get0().mutable(), integerFieldModuloP.get0().mutable());
        }

        @Override // sun.security.ec.point.MutablePoint
        public Mutable conditionalSet(Point point, int i2) {
            if (!(point instanceof ProjectivePoint)) {
                throw new RuntimeException("Incompatible point");
            }
            return conditionalSet((ProjectivePoint) point, i2);
        }

        private <T extends IntegerModuloP> Mutable conditionalSet(ProjectivePoint<T> projectivePoint, int i2) {
            ((MutableIntegerModuloP) this.f13607x).conditionalSet(projectivePoint.f13607x, i2);
            ((MutableIntegerModuloP) this.f13608y).conditionalSet(projectivePoint.f13608y, i2);
            ((MutableIntegerModuloP) this.f13609z).conditionalSet(projectivePoint.f13609z, i2);
            return this;
        }

        @Override // sun.security.ec.point.MutablePoint
        public Mutable setValue(AffinePoint affinePoint) {
            ((MutableIntegerModuloP) this.f13607x).setValue(affinePoint.getX());
            ((MutableIntegerModuloP) this.f13608y).setValue(affinePoint.getY());
            ((MutableIntegerModuloP) this.f13609z).setValue(affinePoint.getX().getField().get1());
            return this;
        }

        @Override // sun.security.ec.point.MutablePoint
        public Mutable setValue(Point point) {
            if (!(point instanceof ProjectivePoint)) {
                throw new RuntimeException("Incompatible point");
            }
            return setValue((ProjectivePoint) point);
        }

        private <T extends IntegerModuloP> Mutable setValue(ProjectivePoint<T> projectivePoint) {
            ((MutableIntegerModuloP) this.f13607x).setValue(projectivePoint.f13607x);
            ((MutableIntegerModuloP) this.f13608y).setValue(projectivePoint.f13608y);
            ((MutableIntegerModuloP) this.f13609z).setValue(projectivePoint.f13609z);
            return this;
        }
    }
}
