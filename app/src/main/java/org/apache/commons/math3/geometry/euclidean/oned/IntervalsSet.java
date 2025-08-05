package org.apache.commons.math3.geometry.euclidean.oned;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.partitioning.AbstractRegion;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.BoundaryProjection;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.util.Precision;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/euclidean/oned/IntervalsSet.class */
public class IntervalsSet extends AbstractRegion<Euclidean1D, Euclidean1D> implements Iterable<double[]> {
    private static final double DEFAULT_TOLERANCE = 1.0E-10d;

    @Override // org.apache.commons.math3.geometry.partitioning.AbstractRegion, org.apache.commons.math3.geometry.partitioning.Region
    public /* bridge */ /* synthetic */ AbstractRegion buildNew(BSPTree bSPTree) {
        return buildNew((BSPTree<Euclidean1D>) bSPTree);
    }

    @Override // org.apache.commons.math3.geometry.partitioning.AbstractRegion, org.apache.commons.math3.geometry.partitioning.Region
    public /* bridge */ /* synthetic */ Region buildNew(BSPTree bSPTree) {
        return buildNew((BSPTree<Euclidean1D>) bSPTree);
    }

    public IntervalsSet(double tolerance) {
        super(tolerance);
    }

    public IntervalsSet(double lower, double upper, double tolerance) {
        super(buildTree(lower, upper, tolerance), tolerance);
    }

    public IntervalsSet(BSPTree<Euclidean1D> tree, double tolerance) {
        super(tree, tolerance);
    }

    public IntervalsSet(Collection<SubHyperplane<Euclidean1D>> boundary, double tolerance) {
        super(boundary, tolerance);
    }

    @Deprecated
    public IntervalsSet() {
        this(1.0E-10d);
    }

    @Deprecated
    public IntervalsSet(double lower, double upper) {
        this(lower, upper, 1.0E-10d);
    }

    @Deprecated
    public IntervalsSet(BSPTree<Euclidean1D> tree) {
        this(tree, 1.0E-10d);
    }

    @Deprecated
    public IntervalsSet(Collection<SubHyperplane<Euclidean1D>> boundary) {
        this(boundary, 1.0E-10d);
    }

    private static BSPTree<Euclidean1D> buildTree(double lower, double upper, double tolerance) {
        if (Double.isInfinite(lower) && lower < 0.0d) {
            if (Double.isInfinite(upper) && upper > 0.0d) {
                return new BSPTree<>(Boolean.TRUE);
            }
            SubHyperplane<Euclidean1D> upperCut = new OrientedPoint(new Vector1D(upper), true, tolerance).wholeHyperplane();
            return new BSPTree<>(upperCut, new BSPTree(Boolean.FALSE), new BSPTree(Boolean.TRUE), null);
        }
        SubHyperplane<Euclidean1D> lowerCut = new OrientedPoint(new Vector1D(lower), false, tolerance).wholeHyperplane();
        if (Double.isInfinite(upper) && upper > 0.0d) {
            return new BSPTree<>(lowerCut, new BSPTree(Boolean.FALSE), new BSPTree(Boolean.TRUE), null);
        }
        SubHyperplane<Euclidean1D> upperCut2 = new OrientedPoint(new Vector1D(upper), true, tolerance).wholeHyperplane();
        return new BSPTree<>(lowerCut, new BSPTree(Boolean.FALSE), new BSPTree(upperCut2, new BSPTree(Boolean.FALSE), new BSPTree(Boolean.TRUE), null), null);
    }

    @Override // org.apache.commons.math3.geometry.partitioning.AbstractRegion, org.apache.commons.math3.geometry.partitioning.Region
    public IntervalsSet buildNew(BSPTree<Euclidean1D> tree) {
        return new IntervalsSet(tree, getTolerance());
    }

    @Override // org.apache.commons.math3.geometry.partitioning.AbstractRegion
    protected void computeGeometricalProperties() {
        if (getTree(false).getCut() == null) {
            setBarycenter((Point) Vector1D.NaN);
            setSize(((Boolean) getTree(false).getAttribute()).booleanValue() ? Double.POSITIVE_INFINITY : 0.0d);
            return;
        }
        double size = 0.0d;
        double sum = 0.0d;
        for (Interval interval : asList()) {
            size += interval.getSize();
            sum += interval.getSize() * interval.getBarycenter();
        }
        setSize(size);
        if (Double.isInfinite(size)) {
            setBarycenter((Point) Vector1D.NaN);
        } else if (size >= Precision.SAFE_MIN) {
            setBarycenter((Point) new Vector1D(sum / size));
        } else {
            setBarycenter((Point) ((OrientedPoint) getTree(false).getCut().getHyperplane()).getLocation());
        }
    }

    public double getInf() {
        BSPTree<Euclidean1D> node = getTree(false);
        double inf = Double.POSITIVE_INFINITY;
        while (node.getCut() != null) {
            OrientedPoint op = (OrientedPoint) node.getCut().getHyperplane();
            inf = op.getLocation().getX();
            node = op.isDirect() ? node.getMinus() : node.getPlus();
        }
        if (((Boolean) node.getAttribute()).booleanValue()) {
            return Double.NEGATIVE_INFINITY;
        }
        return inf;
    }

    public double getSup() {
        BSPTree<Euclidean1D> node = getTree(false);
        double sup = Double.NEGATIVE_INFINITY;
        while (node.getCut() != null) {
            OrientedPoint op = (OrientedPoint) node.getCut().getHyperplane();
            sup = op.getLocation().getX();
            node = op.isDirect() ? node.getPlus() : node.getMinus();
        }
        if (((Boolean) node.getAttribute()).booleanValue()) {
            return Double.POSITIVE_INFINITY;
        }
        return sup;
    }

    @Override // org.apache.commons.math3.geometry.partitioning.AbstractRegion, org.apache.commons.math3.geometry.partitioning.Region
    public BoundaryProjection<Euclidean1D> projectToBoundary(Point<Euclidean1D> point) {
        double x2 = ((Vector1D) point).getX();
        double previous = Double.NEGATIVE_INFINITY;
        Iterator i$ = iterator();
        while (i$.hasNext()) {
            double[] a2 = i$.next();
            if (x2 < a2[0]) {
                double previousOffset = x2 - previous;
                double currentOffset = a2[0] - x2;
                if (previousOffset < currentOffset) {
                    return new BoundaryProjection<>(point, finiteOrNullPoint(previous), previousOffset);
                }
                return new BoundaryProjection<>(point, finiteOrNullPoint(a2[0]), currentOffset);
            }
            if (x2 <= a2[1]) {
                double offset0 = a2[0] - x2;
                double offset1 = x2 - a2[1];
                if (offset0 < offset1) {
                    return new BoundaryProjection<>(point, finiteOrNullPoint(a2[1]), offset1);
                }
                return new BoundaryProjection<>(point, finiteOrNullPoint(a2[0]), offset0);
            }
            previous = a2[1];
        }
        return new BoundaryProjection<>(point, finiteOrNullPoint(previous), x2 - previous);
    }

    private Vector1D finiteOrNullPoint(double x2) {
        if (Double.isInfinite(x2)) {
            return null;
        }
        return new Vector1D(x2);
    }

    public List<Interval> asList() {
        List<Interval> list = new ArrayList<>();
        Iterator i$ = iterator();
        while (i$.hasNext()) {
            double[] a2 = i$.next();
            list.add(new Interval(a2[0], a2[1]));
        }
        return list;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BSPTree<Euclidean1D> getFirstLeaf(BSPTree<Euclidean1D> root) {
        if (root.getCut() == null) {
            return root;
        }
        BSPTree<Euclidean1D> smallest = null;
        BSPTree<Euclidean1D> bSPTreePreviousInternalNode = root;
        while (true) {
            BSPTree<Euclidean1D> n2 = bSPTreePreviousInternalNode;
            if (n2 != null) {
                smallest = n2;
                bSPTreePreviousInternalNode = previousInternalNode(n2);
            } else {
                return leafBefore(smallest);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public BSPTree<Euclidean1D> getFirstIntervalBoundary() {
        BSPTree bSPTree;
        BSPTree<Euclidean1D> tree = getTree(false);
        if (tree.getCut() == null) {
            return null;
        }
        BSPTree parent = getFirstLeaf(tree).getParent();
        while (true) {
            bSPTree = parent;
            if (bSPTree == null || isIntervalStart(bSPTree) || isIntervalEnd(bSPTree)) {
                break;
            }
            parent = nextInternalNode(bSPTree);
        }
        return bSPTree;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isIntervalStart(BSPTree<Euclidean1D> node) {
        if (((Boolean) leafBefore(node).getAttribute()).booleanValue() || !((Boolean) leafAfter(node).getAttribute()).booleanValue()) {
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isIntervalEnd(BSPTree<Euclidean1D> node) {
        if (!((Boolean) leafBefore(node).getAttribute()).booleanValue() || ((Boolean) leafAfter(node).getAttribute()).booleanValue()) {
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BSPTree<Euclidean1D> nextInternalNode(BSPTree<Euclidean1D> node) {
        if (childAfter(node).getCut() != null) {
            return leafAfter(node).getParent();
        }
        while (isAfterParent(node)) {
            node = node.getParent();
        }
        return node.getParent();
    }

    private BSPTree<Euclidean1D> previousInternalNode(BSPTree<Euclidean1D> node) {
        if (childBefore(node).getCut() != null) {
            return leafBefore(node).getParent();
        }
        while (isBeforeParent(node)) {
            node = node.getParent();
        }
        return node.getParent();
    }

    private BSPTree<Euclidean1D> leafBefore(BSPTree<Euclidean1D> node) {
        BSPTree<Euclidean1D> bSPTreeChildBefore = childBefore(node);
        while (true) {
            BSPTree<Euclidean1D> node2 = bSPTreeChildBefore;
            if (node2.getCut() != null) {
                bSPTreeChildBefore = childAfter(node2);
            } else {
                return node2;
            }
        }
    }

    private BSPTree<Euclidean1D> leafAfter(BSPTree<Euclidean1D> node) {
        BSPTree<Euclidean1D> bSPTreeChildAfter = childAfter(node);
        while (true) {
            BSPTree<Euclidean1D> node2 = bSPTreeChildAfter;
            if (node2.getCut() != null) {
                bSPTreeChildAfter = childBefore(node2);
            } else {
                return node2;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean isBeforeParent(BSPTree<Euclidean1D> node) {
        BSPTree<S> parent = node.getParent();
        return parent != 0 && node == childBefore(parent);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean isAfterParent(BSPTree<Euclidean1D> node) {
        BSPTree<S> parent = node.getParent();
        return parent != 0 && node == childAfter(parent);
    }

    private BSPTree<Euclidean1D> childBefore(BSPTree<Euclidean1D> node) {
        if (isDirect(node)) {
            return node.getMinus();
        }
        return node.getPlus();
    }

    private BSPTree<Euclidean1D> childAfter(BSPTree<Euclidean1D> node) {
        if (isDirect(node)) {
            return node.getPlus();
        }
        return node.getMinus();
    }

    private boolean isDirect(BSPTree<Euclidean1D> node) {
        return ((OrientedPoint) node.getCut().getHyperplane()).isDirect();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public double getAngle(BSPTree<Euclidean1D> node) {
        return ((OrientedPoint) node.getCut().getHyperplane()).getLocation().getX();
    }

    @Override // java.lang.Iterable, java.util.List
    public Iterator<double[]> iterator() {
        return new SubIntervalsIterator();
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/euclidean/oned/IntervalsSet$SubIntervalsIterator.class */
    private class SubIntervalsIterator implements Iterator<double[]> {
        private BSPTree<Euclidean1D> current;
        private double[] pending;

        SubIntervalsIterator() {
            this.current = IntervalsSet.this.getFirstIntervalBoundary();
            if (this.current == null) {
                if (((Boolean) IntervalsSet.this.getFirstLeaf(IntervalsSet.this.getTree(false)).getAttribute()).booleanValue()) {
                    this.pending = new double[]{Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY};
                    return;
                } else {
                    this.pending = null;
                    return;
                }
            }
            if (IntervalsSet.this.isIntervalEnd(this.current)) {
                this.pending = new double[]{Double.NEGATIVE_INFINITY, IntervalsSet.this.getAngle(this.current)};
            } else {
                selectPending();
            }
        }

        private void selectPending() {
            BSPTree<Euclidean1D> start;
            BSPTree<Euclidean1D> end;
            BSPTree<Euclidean1D> bSPTreeNextInternalNode = this.current;
            while (true) {
                start = bSPTreeNextInternalNode;
                if (start == null || IntervalsSet.this.isIntervalStart(start)) {
                    break;
                } else {
                    bSPTreeNextInternalNode = IntervalsSet.this.nextInternalNode(start);
                }
            }
            if (start == null) {
                this.current = null;
                this.pending = null;
                return;
            }
            BSPTree<Euclidean1D> bSPTreeNextInternalNode2 = start;
            while (true) {
                end = bSPTreeNextInternalNode2;
                if (end == null || IntervalsSet.this.isIntervalEnd(end)) {
                    break;
                } else {
                    bSPTreeNextInternalNode2 = IntervalsSet.this.nextInternalNode(end);
                }
            }
            if (end != null) {
                this.pending = new double[]{IntervalsSet.this.getAngle(start), IntervalsSet.this.getAngle(end)};
                this.current = end;
            } else {
                this.pending = new double[]{IntervalsSet.this.getAngle(start), Double.POSITIVE_INFINITY};
                this.current = null;
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.pending != null;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public double[] next() {
            if (this.pending == null) {
                throw new NoSuchElementException();
            }
            double[] next = this.pending;
            selectPending();
            return next;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
