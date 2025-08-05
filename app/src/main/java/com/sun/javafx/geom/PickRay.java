package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/PickRay.class */
public class PickRay {
    private Vec3d origin = new Vec3d();
    private Vec3d direction = new Vec3d();
    private double nearClip = 0.0d;
    private double farClip = Double.POSITIVE_INFINITY;
    static final double EPS = 9.999999747378752E-6d;
    private static final double EPSILON_ABSOLUTE = 1.0E-5d;

    public PickRay() {
    }

    public PickRay(Vec3d origin, Vec3d direction, double nearClip, double farClip) {
        set(origin, direction, nearClip, farClip);
    }

    public PickRay(double x2, double y2, double z2, double nearClip, double farClip) {
        set(x2, y2, z2, nearClip, farClip);
    }

    public static PickRay computePerspectivePickRay(double x2, double y2, boolean fixedEye, double viewWidth, double viewHeight, double fieldOfViewRadians, boolean verticalFieldOfView, Affine3D cameraTransform, double nearClip, double farClip, PickRay pickRay) {
        if (pickRay == null) {
            pickRay = new PickRay();
        }
        Vec3d direction = pickRay.getDirectionNoClone();
        double halfViewWidth = viewWidth / 2.0d;
        double halfViewHeight = viewHeight / 2.0d;
        double halfViewDim = verticalFieldOfView ? halfViewHeight : halfViewWidth;
        double distanceZ = halfViewDim / Math.tan(fieldOfViewRadians / 2.0d);
        direction.f11930x = x2 - halfViewWidth;
        direction.f11931y = y2 - halfViewHeight;
        direction.f11932z = distanceZ;
        Vec3d eye = pickRay.getOriginNoClone();
        if (fixedEye) {
            eye.set(0.0d, 0.0d, 0.0d);
        } else {
            eye.set(halfViewWidth, halfViewHeight, -distanceZ);
        }
        pickRay.nearClip = nearClip * (direction.length() / (fixedEye ? distanceZ : 1.0d));
        pickRay.farClip = farClip * (direction.length() / (fixedEye ? distanceZ : 1.0d));
        pickRay.transform(cameraTransform);
        return pickRay;
    }

    public static PickRay computeParallelPickRay(double x2, double y2, double viewHeight, Affine3D cameraTransform, double nearClip, double farClip, PickRay pickRay) {
        if (pickRay == null) {
            pickRay = new PickRay();
        }
        double distanceZ = (viewHeight / 2.0d) / Math.tan(Math.toRadians(15.0d));
        pickRay.set(x2, y2, distanceZ, nearClip * distanceZ, farClip * distanceZ);
        if (cameraTransform != null) {
            pickRay.transform(cameraTransform);
        }
        return pickRay;
    }

    public final void set(Vec3d origin, Vec3d direction, double nearClip, double farClip) {
        setOrigin(origin);
        setDirection(direction);
        this.nearClip = nearClip;
        this.farClip = farClip;
    }

    public final void set(double x2, double y2, double z2, double nearClip, double farClip) {
        setOrigin(x2, y2, -z2);
        setDirection(0.0d, 0.0d, z2);
        this.nearClip = nearClip;
        this.farClip = farClip;
    }

    public void setPickRay(PickRay other) {
        setOrigin(other.origin);
        setDirection(other.direction);
        this.nearClip = other.nearClip;
        this.farClip = other.farClip;
    }

    public PickRay copy() {
        return new PickRay(this.origin, this.direction, this.nearClip, this.farClip);
    }

    public void setOrigin(Vec3d origin) {
        this.origin.set(origin);
    }

    public void setOrigin(double x2, double y2, double z2) {
        this.origin.set(x2, y2, z2);
    }

    public Vec3d getOrigin(Vec3d rv) {
        if (rv == null) {
            rv = new Vec3d();
        }
        rv.set(this.origin);
        return rv;
    }

    public Vec3d getOriginNoClone() {
        return this.origin;
    }

    public void setDirection(Vec3d direction) {
        this.direction.set(direction);
    }

    public void setDirection(double x2, double y2, double z2) {
        this.direction.set(x2, y2, z2);
    }

    public Vec3d getDirection(Vec3d rv) {
        if (rv == null) {
            rv = new Vec3d();
        }
        rv.set(this.direction);
        return rv;
    }

    public Vec3d getDirectionNoClone() {
        return this.direction;
    }

    public double getNearClip() {
        return this.nearClip;
    }

    public double getFarClip() {
        return this.farClip;
    }

    public double distance(Vec3d iPnt) {
        double x2 = iPnt.f11930x - this.origin.f11930x;
        double y2 = iPnt.f11931y - this.origin.f11931y;
        double z2 = iPnt.f11932z - this.origin.f11932z;
        return Math.sqrt((x2 * x2) + (y2 * y2) + (z2 * z2));
    }

    public Point2D projectToZeroPlane(BaseTransform inversetx, boolean perspective, Vec3d tmpvec, Point2D ret) {
        if (tmpvec == null) {
            tmpvec = new Vec3d();
        }
        inversetx.transform(this.origin, tmpvec);
        double origX = tmpvec.f11930x;
        double origY = tmpvec.f11931y;
        double origZ = tmpvec.f11932z;
        tmpvec.add(this.origin, this.direction);
        inversetx.transform(tmpvec, tmpvec);
        double dirX = tmpvec.f11930x - origX;
        double dirY = tmpvec.f11931y - origY;
        double dirZ = tmpvec.f11932z - origZ;
        if (almostZero(dirZ)) {
            return null;
        }
        double t2 = (-origZ) / dirZ;
        if (perspective && t2 < 0.0d) {
            return null;
        }
        if (ret == null) {
            ret = new Point2D();
        }
        ret.setLocation((float) (origX + (dirX * t2)), (float) (origY + (dirY * t2)));
        return ret;
    }

    static boolean almostZero(double a2) {
        return a2 < EPSILON_ABSOLUTE && a2 > -1.0E-5d;
    }

    private static boolean isNonZero(double v2) {
        return v2 > EPS || v2 < -9.999999747378752E-6d;
    }

    public void transform(BaseTransform t2) {
        t2.transform(this.origin, this.origin);
        t2.deltaTransform(this.direction, this.direction);
    }

    public void inverseTransform(BaseTransform t2) throws NoninvertibleTransformException {
        t2.inverseTransform(this.origin, this.origin);
        t2.inverseDeltaTransform(this.direction, this.direction);
    }

    public PickRay project(BaseTransform inversetx, boolean perspective, Vec3d tmpvec, Point2D ret) {
        if (tmpvec == null) {
            tmpvec = new Vec3d();
        }
        inversetx.transform(this.origin, tmpvec);
        double origX = tmpvec.f11930x;
        double origY = tmpvec.f11931y;
        double origZ = tmpvec.f11932z;
        tmpvec.add(this.origin, this.direction);
        inversetx.transform(tmpvec, tmpvec);
        double dirX = tmpvec.f11930x - origX;
        double dirY = tmpvec.f11931y - origY;
        double dirZ = tmpvec.f11932z - origZ;
        PickRay pr = new PickRay();
        pr.origin.f11930x = origX;
        pr.origin.f11931y = origY;
        pr.origin.f11932z = origZ;
        pr.direction.f11930x = dirX;
        pr.direction.f11931y = dirY;
        pr.direction.f11932z = dirZ;
        return pr;
    }

    public String toString() {
        return "origin: " + ((Object) this.origin) + "  direction: " + ((Object) this.direction);
    }
}
