package org.apache.commons.math3.geometry.spherical.twod;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.spherical.oned.Arc;
import org.apache.commons.math3.geometry.spherical.oned.ArcsSet;
import org.apache.commons.math3.geometry.spherical.oned.Sphere1D;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/spherical/twod/SubCircle.class */
public class SubCircle extends AbstractSubHyperplane<Sphere2D, Sphere1D> {
    public SubCircle(Hyperplane<Sphere2D> hyperplane, Region<Sphere1D> remainingRegion) {
        super(hyperplane, remainingRegion);
    }

    @Override // org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane
    protected AbstractSubHyperplane<Sphere2D, Sphere1D> buildNew(Hyperplane<Sphere2D> hyperplane, Region<Sphere1D> remainingRegion) {
        return new SubCircle(hyperplane, remainingRegion);
    }

    @Override // org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane, org.apache.commons.math3.geometry.partitioning.SubHyperplane
    public SubHyperplane.SplitSubHyperplane<Sphere2D> split(Hyperplane<Sphere2D> hyperplane) throws MathArithmeticException {
        Circle thisCircle = (Circle) getHyperplane();
        Circle otherCircle = (Circle) hyperplane;
        double angle = Vector3D.angle(thisCircle.getPole(), otherCircle.getPole());
        if (angle < thisCircle.getTolerance() || angle > 3.141592653589793d - thisCircle.getTolerance()) {
            return new SubHyperplane.SplitSubHyperplane<>(null, null);
        }
        Arc arc = thisCircle.getInsideArc(otherCircle);
        ArcsSet.Split split = ((ArcsSet) getRemainingRegion()).split(arc);
        ArcsSet plus = split.getPlus();
        ArcsSet minus = split.getMinus();
        return new SubHyperplane.SplitSubHyperplane<>(plus == null ? null : new SubCircle(thisCircle.copySelf(), plus), minus == null ? null : new SubCircle(thisCircle.copySelf(), minus));
    }
}
