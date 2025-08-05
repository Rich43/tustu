package org.apache.commons.math3.geometry.euclidean.threed;

import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.PolygonsSet;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/euclidean/threed/SubPlane.class */
public class SubPlane extends AbstractSubHyperplane<Euclidean3D, Euclidean2D> {
    public SubPlane(Hyperplane<Euclidean3D> hyperplane, Region<Euclidean2D> remainingRegion) {
        super(hyperplane, remainingRegion);
    }

    @Override // org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane
    protected AbstractSubHyperplane<Euclidean3D, Euclidean2D> buildNew(Hyperplane<Euclidean3D> hyperplane, Region<Euclidean2D> remainingRegion) {
        return new SubPlane(hyperplane, remainingRegion);
    }

    @Override // org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane, org.apache.commons.math3.geometry.partitioning.SubHyperplane
    public SubHyperplane.SplitSubHyperplane<Euclidean3D> split(Hyperplane<Euclidean3D> hyperplane) {
        Plane otherPlane = (Plane) hyperplane;
        Plane thisPlane = (Plane) getHyperplane();
        Line inter = otherPlane.intersection(thisPlane);
        double tolerance = thisPlane.getTolerance();
        if (inter == null) {
            double global = otherPlane.getOffset(thisPlane);
            if (global < (-tolerance)) {
                return new SubHyperplane.SplitSubHyperplane<>(null, this);
            }
            if (global > tolerance) {
                return new SubHyperplane.SplitSubHyperplane<>(this, null);
            }
            return new SubHyperplane.SplitSubHyperplane<>(null, null);
        }
        Vector2D p2 = thisPlane.toSubSpace((Point<Euclidean3D>) inter.toSpace((Point<Euclidean1D>) Vector1D.ZERO));
        Vector2D q2 = thisPlane.toSubSpace((Point<Euclidean3D>) inter.toSpace((Point<Euclidean1D>) Vector1D.ONE));
        Vector3D crossP = Vector3D.crossProduct(inter.getDirection(), thisPlane.getNormal());
        if (crossP.dotProduct(otherPlane.getNormal()) < 0.0d) {
            p2 = q2;
            q2 = p2;
        }
        org.apache.commons.math3.geometry.euclidean.twod.SubLine subLineWholeHyperplane = new org.apache.commons.math3.geometry.euclidean.twod.Line(p2, q2, tolerance).wholeHyperplane();
        SubHyperplane<Euclidean2D> l2DPlus = new org.apache.commons.math3.geometry.euclidean.twod.Line(q2, p2, tolerance).wholeHyperplane();
        BSPTree<Euclidean2D> splitTree = getRemainingRegion().getTree(false).split(subLineWholeHyperplane);
        BSPTree<Euclidean2D> plusTree = getRemainingRegion().isEmpty(splitTree.getPlus()) ? new BSPTree<>(Boolean.FALSE) : new BSPTree<>(l2DPlus, new BSPTree(Boolean.FALSE), splitTree.getPlus(), null);
        BSPTree<Euclidean2D> minusTree = getRemainingRegion().isEmpty(splitTree.getMinus()) ? new BSPTree<>(Boolean.FALSE) : new BSPTree<>(subLineWholeHyperplane, new BSPTree(Boolean.FALSE), splitTree.getMinus(), null);
        return new SubHyperplane.SplitSubHyperplane<>(new SubPlane(thisPlane.copySelf(), new PolygonsSet(plusTree, tolerance)), new SubPlane(thisPlane.copySelf(), new PolygonsSet(minusTree, tolerance)));
    }
}
