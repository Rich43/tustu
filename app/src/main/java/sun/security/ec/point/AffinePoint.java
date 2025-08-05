package sun.security.ec.point;

import java.util.Objects;
import sun.security.util.math.ImmutableIntegerModuloP;

/* loaded from: sunec.jar:sun/security/ec/point/AffinePoint.class */
public class AffinePoint {

    /* renamed from: x, reason: collision with root package name */
    private final ImmutableIntegerModuloP f13605x;

    /* renamed from: y, reason: collision with root package name */
    private final ImmutableIntegerModuloP f13606y;

    public AffinePoint(ImmutableIntegerModuloP immutableIntegerModuloP, ImmutableIntegerModuloP immutableIntegerModuloP2) {
        this.f13605x = immutableIntegerModuloP;
        this.f13606y = immutableIntegerModuloP2;
    }

    public ImmutableIntegerModuloP getX() {
        return this.f13605x;
    }

    public ImmutableIntegerModuloP getY() {
        return this.f13606y;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof AffinePoint)) {
            return false;
        }
        AffinePoint affinePoint = (AffinePoint) obj;
        return this.f13605x.asBigInteger().equals(affinePoint.f13605x.asBigInteger()) && this.f13606y.asBigInteger().equals(affinePoint.f13606y.asBigInteger());
    }

    public int hashCode() {
        return Objects.hash(this.f13605x, this.f13606y);
    }

    public String toString() {
        return "(" + this.f13605x.asBigInteger().toString() + "," + this.f13606y.asBigInteger().toString() + ")";
    }
}
