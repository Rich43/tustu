package sun.security.ec.point;

import sun.security.util.math.IntegerFieldModuloP;

/* loaded from: sunec.jar:sun/security/ec/point/Point.class */
public interface Point {
    IntegerFieldModuloP getField();

    AffinePoint asAffine();

    ImmutablePoint fixed();

    MutablePoint mutable();
}
