package sun.security.ec.point;

/* loaded from: sunec.jar:sun/security/ec/point/MutablePoint.class */
public interface MutablePoint extends Point {
    MutablePoint setValue(AffinePoint affinePoint);

    MutablePoint setValue(Point point);

    MutablePoint conditionalSet(Point point, int i2);
}
