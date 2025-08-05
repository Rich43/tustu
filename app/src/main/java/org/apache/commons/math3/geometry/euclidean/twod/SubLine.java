package org.apache.commons.math3.geometry.euclidean.twod;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.apache.commons.math3.geometry.euclidean.oned.OrientedPoint;
import org.apache.commons.math3.geometry.euclidean.oned.SubOrientedPoint;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/euclidean/twod/SubLine.class */
public class SubLine extends AbstractSubHyperplane<Euclidean2D, Euclidean1D> {
    private static final double DEFAULT_TOLERANCE = 1.0E-10d;

    public SubLine(Hyperplane<Euclidean2D> hyperplane, Region<Euclidean1D> remainingRegion) {
        super(hyperplane, remainingRegion);
    }

    public SubLine(Vector2D start, Vector2D end, double tolerance) {
        super(new Line(start, end, tolerance), buildIntervalSet(start, end, tolerance));
    }

    @Deprecated
    public SubLine(Vector2D start, Vector2D end) {
        this(start, end, 1.0E-10d);
    }

    public SubLine(Segment segment) {
        super(segment.getLine(), buildIntervalSet(segment.getStart(), segment.getEnd(), segment.getLine().getTolerance()));
    }

    public List<Segment> getSegments() {
        Line line = (Line) getHyperplane();
        List<Interval> list = ((IntervalsSet) getRemainingRegion()).asList();
        List<Segment> segments = new ArrayList<>(list.size());
        for (Interval interval : list) {
            Vector2D start = line.toSpace((Point<Euclidean1D>) new Vector1D(interval.getInf()));
            Vector2D end = line.toSpace((Point<Euclidean1D>) new Vector1D(interval.getSup()));
            segments.add(new Segment(start, end, line));
        }
        return segments;
    }

    public Vector2D intersection(SubLine subLine, boolean includeEndPoints) {
        Line line1 = (Line) getHyperplane();
        Line line2 = (Line) subLine.getHyperplane();
        Vector2D v2D = line1.intersection(line2);
        if (v2D == null) {
            return null;
        }
        Region.Location loc1 = getRemainingRegion().checkPoint(line1.toSubSpace((Point<Euclidean2D>) v2D));
        Region.Location loc2 = subLine.getRemainingRegion().checkPoint(line2.toSubSpace((Point<Euclidean2D>) v2D));
        if (includeEndPoints) {
            if (loc1 == Region.Location.OUTSIDE || loc2 == Region.Location.OUTSIDE) {
                return null;
            }
            return v2D;
        }
        if (loc1 == Region.Location.INSIDE && loc2 == Region.Location.INSIDE) {
            return v2D;
        }
        return null;
    }

    private static IntervalsSet buildIntervalSet(Vector2D start, Vector2D end, double tolerance) {
        Line line = new Line(start, end, tolerance);
        return new IntervalsSet(line.toSubSpace((Point<Euclidean2D>) start).getX(), line.toSubSpace((Point<Euclidean2D>) end).getX(), tolerance);
    }

    @Override // org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane
    protected AbstractSubHyperplane<Euclidean2D, Euclidean1D> buildNew(Hyperplane<Euclidean2D> hyperplane, Region<Euclidean1D> remainingRegion) {
        return new SubLine(hyperplane, remainingRegion);
    }

    @Override // org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane, org.apache.commons.math3.geometry.partitioning.SubHyperplane
    public SubHyperplane.SplitSubHyperplane<Euclidean2D> split(Hyperplane<Euclidean2D> hyperplane) {
        Line thisLine = (Line) getHyperplane();
        Line otherLine = (Line) hyperplane;
        Vector2D crossing = thisLine.intersection(otherLine);
        double tolerance = thisLine.getTolerance();
        if (crossing == null) {
            double global = otherLine.getOffset(thisLine);
            if (global < (-tolerance)) {
                return new SubHyperplane.SplitSubHyperplane<>(null, this);
            }
            if (global > tolerance) {
                return new SubHyperplane.SplitSubHyperplane<>(this, null);
            }
            return new SubHyperplane.SplitSubHyperplane<>(null, null);
        }
        boolean direct = FastMath.sin(thisLine.getAngle() - otherLine.getAngle()) < 0.0d;
        Vector1D x2 = thisLine.toSubSpace((Point<Euclidean2D>) crossing);
        SubHyperplane<Euclidean1D> subPlus = new OrientedPoint(x2, !direct, tolerance).wholeHyperplane();
        SubOrientedPoint subOrientedPointWholeHyperplane = new OrientedPoint(x2, direct, tolerance).wholeHyperplane();
        BSPTree<Euclidean1D> splitTree = getRemainingRegion().getTree(false).split(subOrientedPointWholeHyperplane);
        BSPTree<Euclidean1D> plusTree = getRemainingRegion().isEmpty(splitTree.getPlus()) ? new BSPTree<>(Boolean.FALSE) : new BSPTree<>(subPlus, new BSPTree(Boolean.FALSE), splitTree.getPlus(), null);
        BSPTree<Euclidean1D> minusTree = getRemainingRegion().isEmpty(splitTree.getMinus()) ? new BSPTree<>(Boolean.FALSE) : new BSPTree<>(subOrientedPointWholeHyperplane, new BSPTree(Boolean.FALSE), splitTree.getMinus(), null);
        return new SubHyperplane.SplitSubHyperplane<>(new SubLine(thisLine.copySelf(), new IntervalsSet(plusTree, tolerance)), new SubLine(thisLine.copySelf(), new IntervalsSet(minusTree, tolerance)));
    }
}
