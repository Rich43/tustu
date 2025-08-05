package org.apache.commons.math3.geometry.spherical.oned;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.partitioning.AbstractRegion;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.BoundaryProjection;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/spherical/oned/ArcsSet.class */
public class ArcsSet extends AbstractRegion<Sphere1D, Sphere1D> implements Iterable<double[]> {
    @Override // org.apache.commons.math3.geometry.partitioning.AbstractRegion, org.apache.commons.math3.geometry.partitioning.Region
    public /* bridge */ /* synthetic */ AbstractRegion buildNew(BSPTree bSPTree) {
        return buildNew((BSPTree<Sphere1D>) bSPTree);
    }

    @Override // org.apache.commons.math3.geometry.partitioning.AbstractRegion, org.apache.commons.math3.geometry.partitioning.Region
    public /* bridge */ /* synthetic */ Region buildNew(BSPTree bSPTree) {
        return buildNew((BSPTree<Sphere1D>) bSPTree);
    }

    public ArcsSet(double tolerance) {
        super(tolerance);
    }

    public ArcsSet(double lower, double upper, double tolerance) throws NumberIsTooLargeException {
        super(buildTree(lower, upper, tolerance), tolerance);
    }

    public ArcsSet(BSPTree<Sphere1D> tree, double tolerance) throws InconsistentStateAt2PiWrapping {
        super(tree, tolerance);
        check2PiConsistency();
    }

    public ArcsSet(Collection<SubHyperplane<Sphere1D>> boundary, double tolerance) throws InconsistentStateAt2PiWrapping {
        super(boundary, tolerance);
        check2PiConsistency();
    }

    private static BSPTree<Sphere1D> buildTree(double lower, double upper, double tolerance) throws NumberIsTooLargeException {
        if (Precision.equals(lower, upper, 0) || upper - lower >= 6.283185307179586d) {
            return new BSPTree<>(Boolean.TRUE);
        }
        if (lower > upper) {
            throw new NumberIsTooLargeException(LocalizedFormats.ENDPOINTS_NOT_AN_INTERVAL, Double.valueOf(lower), Double.valueOf(upper), true);
        }
        double normalizedLower = MathUtils.normalizeAngle(lower, 3.141592653589793d);
        double normalizedUpper = normalizedLower + (upper - lower);
        SubHyperplane<Sphere1D> lowerCut = new LimitAngle(new S1Point(normalizedLower), false, tolerance).wholeHyperplane();
        if (normalizedUpper <= 6.283185307179586d) {
            SubHyperplane<Sphere1D> upperCut = new LimitAngle(new S1Point(normalizedUpper), true, tolerance).wholeHyperplane();
            return new BSPTree<>(lowerCut, new BSPTree(Boolean.FALSE), new BSPTree(upperCut, new BSPTree(Boolean.FALSE), new BSPTree(Boolean.TRUE), null), null);
        }
        SubHyperplane<Sphere1D> upperCut2 = new LimitAngle(new S1Point(normalizedUpper - 6.283185307179586d), true, tolerance).wholeHyperplane();
        return new BSPTree<>(lowerCut, new BSPTree(upperCut2, new BSPTree(Boolean.FALSE), new BSPTree(Boolean.TRUE), null), new BSPTree(Boolean.TRUE), null);
    }

    private void check2PiConsistency() throws InconsistentStateAt2PiWrapping {
        BSPTree<Sphere1D> root = getTree(false);
        if (root.getCut() == null) {
            return;
        }
        Boolean stateBefore = (Boolean) getFirstLeaf(root).getAttribute();
        Boolean stateAfter = (Boolean) getLastLeaf(root).getAttribute();
        if (stateBefore.booleanValue() ^ stateAfter.booleanValue()) {
            throw new InconsistentStateAt2PiWrapping();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BSPTree<Sphere1D> getFirstLeaf(BSPTree<Sphere1D> root) {
        if (root.getCut() == null) {
            return root;
        }
        BSPTree<Sphere1D> smallest = null;
        BSPTree<Sphere1D> bSPTreePreviousInternalNode = root;
        while (true) {
            BSPTree<Sphere1D> n2 = bSPTreePreviousInternalNode;
            if (n2 != null) {
                smallest = n2;
                bSPTreePreviousInternalNode = previousInternalNode(n2);
            } else {
                return leafBefore(smallest);
            }
        }
    }

    private BSPTree<Sphere1D> getLastLeaf(BSPTree<Sphere1D> root) {
        if (root.getCut() == null) {
            return root;
        }
        BSPTree<Sphere1D> largest = null;
        BSPTree<Sphere1D> bSPTreeNextInternalNode = root;
        while (true) {
            BSPTree<Sphere1D> n2 = bSPTreeNextInternalNode;
            if (n2 != null) {
                largest = n2;
                bSPTreeNextInternalNode = nextInternalNode(n2);
            } else {
                return leafAfter(largest);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public BSPTree<Sphere1D> getFirstArcStart() {
        BSPTree bSPTree;
        BSPTree<Sphere1D> tree = getTree(false);
        if (tree.getCut() == null) {
            return null;
        }
        BSPTree parent = getFirstLeaf(tree).getParent();
        while (true) {
            bSPTree = parent;
            if (bSPTree == null || isArcStart(bSPTree)) {
                break;
            }
            parent = nextInternalNode(bSPTree);
        }
        return bSPTree;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isArcStart(BSPTree<Sphere1D> node) {
        if (((Boolean) leafBefore(node).getAttribute()).booleanValue() || !((Boolean) leafAfter(node).getAttribute()).booleanValue()) {
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isArcEnd(BSPTree<Sphere1D> node) {
        if (!((Boolean) leafBefore(node).getAttribute()).booleanValue() || ((Boolean) leafAfter(node).getAttribute()).booleanValue()) {
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BSPTree<Sphere1D> nextInternalNode(BSPTree<Sphere1D> node) {
        if (childAfter(node).getCut() != null) {
            return leafAfter(node).getParent();
        }
        while (isAfterParent(node)) {
            node = node.getParent();
        }
        return node.getParent();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BSPTree<Sphere1D> previousInternalNode(BSPTree<Sphere1D> node) {
        if (childBefore(node).getCut() != null) {
            return leafBefore(node).getParent();
        }
        while (isBeforeParent(node)) {
            node = node.getParent();
        }
        return node.getParent();
    }

    private BSPTree<Sphere1D> leafBefore(BSPTree<Sphere1D> node) {
        BSPTree<Sphere1D> bSPTreeChildBefore = childBefore(node);
        while (true) {
            BSPTree<Sphere1D> node2 = bSPTreeChildBefore;
            if (node2.getCut() != null) {
                bSPTreeChildBefore = childAfter(node2);
            } else {
                return node2;
            }
        }
    }

    private BSPTree<Sphere1D> leafAfter(BSPTree<Sphere1D> node) {
        BSPTree<Sphere1D> bSPTreeChildAfter = childAfter(node);
        while (true) {
            BSPTree<Sphere1D> node2 = bSPTreeChildAfter;
            if (node2.getCut() != null) {
                bSPTreeChildAfter = childBefore(node2);
            } else {
                return node2;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean isBeforeParent(BSPTree<Sphere1D> node) {
        BSPTree<S> parent = node.getParent();
        return parent != 0 && node == childBefore(parent);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean isAfterParent(BSPTree<Sphere1D> node) {
        BSPTree<S> parent = node.getParent();
        return parent != 0 && node == childAfter(parent);
    }

    private BSPTree<Sphere1D> childBefore(BSPTree<Sphere1D> node) {
        if (isDirect(node)) {
            return node.getMinus();
        }
        return node.getPlus();
    }

    private BSPTree<Sphere1D> childAfter(BSPTree<Sphere1D> node) {
        if (isDirect(node)) {
            return node.getPlus();
        }
        return node.getMinus();
    }

    private boolean isDirect(BSPTree<Sphere1D> node) {
        return ((LimitAngle) node.getCut().getHyperplane()).isDirect();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public double getAngle(BSPTree<Sphere1D> node) {
        return ((LimitAngle) node.getCut().getHyperplane()).getLocation().getAlpha();
    }

    @Override // org.apache.commons.math3.geometry.partitioning.AbstractRegion, org.apache.commons.math3.geometry.partitioning.Region
    public ArcsSet buildNew(BSPTree<Sphere1D> tree) {
        return new ArcsSet(tree, getTolerance());
    }

    @Override // org.apache.commons.math3.geometry.partitioning.AbstractRegion
    protected void computeGeometricalProperties() {
        if (getTree(false).getCut() == null) {
            setBarycenter(S1Point.NaN);
            setSize(((Boolean) getTree(false).getAttribute()).booleanValue() ? 6.283185307179586d : 0.0d);
            return;
        }
        double size = 0.0d;
        double sum = 0.0d;
        Iterator i$ = iterator();
        while (i$.hasNext()) {
            double[] a2 = i$.next();
            double length = a2[1] - a2[0];
            size += length;
            sum += length * (a2[0] + a2[1]);
        }
        setSize(size);
        if (Precision.equals(size, 6.283185307179586d, 0)) {
            setBarycenter(S1Point.NaN);
        } else if (size >= Precision.SAFE_MIN) {
            setBarycenter(new S1Point(sum / (2.0d * size)));
        } else {
            LimitAngle limit = (LimitAngle) getTree(false).getCut().getHyperplane();
            setBarycenter(limit.getLocation());
        }
    }

    @Override // org.apache.commons.math3.geometry.partitioning.AbstractRegion, org.apache.commons.math3.geometry.partitioning.Region
    public BoundaryProjection<Sphere1D> projectToBoundary(Point<Sphere1D> point) {
        double alpha = ((S1Point) point).getAlpha();
        boolean wrapFirst = false;
        double first = Double.NaN;
        double previous = Double.NaN;
        Iterator i$ = iterator();
        while (i$.hasNext()) {
            double[] a2 = i$.next();
            if (Double.isNaN(first)) {
                first = a2[0];
            }
            if (!wrapFirst) {
                if (alpha < a2[0]) {
                    if (Double.isNaN(previous)) {
                        wrapFirst = true;
                    } else {
                        double previousOffset = alpha - previous;
                        double currentOffset = a2[0] - alpha;
                        if (previousOffset < currentOffset) {
                            return new BoundaryProjection<>(point, new S1Point(previous), previousOffset);
                        }
                        return new BoundaryProjection<>(point, new S1Point(a2[0]), currentOffset);
                    }
                } else if (alpha <= a2[1]) {
                    double offset0 = a2[0] - alpha;
                    double offset1 = alpha - a2[1];
                    if (offset0 < offset1) {
                        return new BoundaryProjection<>(point, new S1Point(a2[1]), offset1);
                    }
                    return new BoundaryProjection<>(point, new S1Point(a2[0]), offset0);
                }
            }
            previous = a2[1];
        }
        if (Double.isNaN(previous)) {
            return new BoundaryProjection<>(point, null, 6.283185307179586d);
        }
        if (wrapFirst) {
            double previousOffset2 = alpha - (previous - 6.283185307179586d);
            double currentOffset2 = first - alpha;
            if (previousOffset2 < currentOffset2) {
                return new BoundaryProjection<>(point, new S1Point(previous), previousOffset2);
            }
            return new BoundaryProjection<>(point, new S1Point(first), currentOffset2);
        }
        double previousOffset3 = alpha - previous;
        double currentOffset3 = (first + 6.283185307179586d) - alpha;
        if (previousOffset3 < currentOffset3) {
            return new BoundaryProjection<>(point, new S1Point(previous), previousOffset3);
        }
        return new BoundaryProjection<>(point, new S1Point(first), currentOffset3);
    }

    public List<Arc> asList() {
        List<Arc> list = new ArrayList<>();
        Iterator i$ = iterator();
        while (i$.hasNext()) {
            double[] a2 = i$.next();
            list.add(new Arc(a2[0], a2[1], getTolerance()));
        }
        return list;
    }

    @Override // java.lang.Iterable, java.util.List
    public Iterator<double[]> iterator() {
        return new SubArcsIterator();
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/spherical/oned/ArcsSet$SubArcsIterator.class */
    private class SubArcsIterator implements Iterator<double[]> {
        private final BSPTree<Sphere1D> firstStart;
        private BSPTree<Sphere1D> current;
        private double[] pending;

        SubArcsIterator() {
            this.firstStart = ArcsSet.this.getFirstArcStart();
            this.current = this.firstStart;
            if (this.firstStart == null) {
                if (((Boolean) ArcsSet.this.getFirstLeaf(ArcsSet.this.getTree(false)).getAttribute()).booleanValue()) {
                    this.pending = new double[]{0.0d, 6.283185307179586d};
                    return;
                } else {
                    this.pending = null;
                    return;
                }
            }
            selectPending();
        }

        private void selectPending() {
            BSPTree<Sphere1D> start;
            BSPTree<Sphere1D> end;
            BSPTree<Sphere1D> end2;
            BSPTree<Sphere1D> bSPTreeNextInternalNode = this.current;
            while (true) {
                start = bSPTreeNextInternalNode;
                if (start == null || ArcsSet.this.isArcStart(start)) {
                    break;
                } else {
                    bSPTreeNextInternalNode = ArcsSet.this.nextInternalNode(start);
                }
            }
            if (start == null) {
                this.current = null;
                this.pending = null;
                return;
            }
            BSPTree<Sphere1D> bSPTreeNextInternalNode2 = start;
            while (true) {
                end = bSPTreeNextInternalNode2;
                if (end == null || ArcsSet.this.isArcEnd(end)) {
                    break;
                } else {
                    bSPTreeNextInternalNode2 = ArcsSet.this.nextInternalNode(end);
                }
            }
            if (end != null) {
                this.pending = new double[]{ArcsSet.this.getAngle(start), ArcsSet.this.getAngle(end)};
                this.current = end;
                return;
            }
            BSPTree<Sphere1D> bSPTreePreviousInternalNode = this.firstStart;
            while (true) {
                end2 = bSPTreePreviousInternalNode;
                if (end2 == null || ArcsSet.this.isArcEnd(end2)) {
                    break;
                } else {
                    bSPTreePreviousInternalNode = ArcsSet.this.previousInternalNode(end2);
                }
            }
            if (end2 == null) {
                throw new MathInternalError();
            }
            this.pending = new double[]{ArcsSet.this.getAngle(start), ArcsSet.this.getAngle(end2) + 6.283185307179586d};
            this.current = null;
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

    @Deprecated
    public Side side(Arc arc) {
        return split(arc).getSide();
    }

    public Split split(Arc arc) {
        List<Double> minus = new ArrayList<>();
        List<Double> plus = new ArrayList<>();
        double reference = 3.141592653589793d + arc.getInf();
        double arcLength = arc.getSup() - arc.getInf();
        Iterator i$ = iterator();
        while (i$.hasNext()) {
            double[] a2 = i$.next();
            double syncedStart = MathUtils.normalizeAngle(a2[0], reference) - arc.getInf();
            double arcOffset = a2[0] - syncedStart;
            double syncedEnd = a2[1] - arcOffset;
            if (syncedStart < arcLength) {
                minus.add(Double.valueOf(a2[0]));
                if (syncedEnd > arcLength) {
                    double minusToPlus = arcLength + arcOffset;
                    minus.add(Double.valueOf(minusToPlus));
                    plus.add(Double.valueOf(minusToPlus));
                    if (syncedEnd > 6.283185307179586d) {
                        double plusToMinus = 6.283185307179586d + arcOffset;
                        plus.add(Double.valueOf(plusToMinus));
                        minus.add(Double.valueOf(plusToMinus));
                        minus.add(Double.valueOf(a2[1]));
                    } else {
                        plus.add(Double.valueOf(a2[1]));
                    }
                } else {
                    minus.add(Double.valueOf(a2[1]));
                }
            } else {
                plus.add(Double.valueOf(a2[0]));
                if (syncedEnd > 6.283185307179586d) {
                    double plusToMinus2 = 6.283185307179586d + arcOffset;
                    plus.add(Double.valueOf(plusToMinus2));
                    minus.add(Double.valueOf(plusToMinus2));
                    if (syncedEnd > 6.283185307179586d + arcLength) {
                        double minusToPlus2 = 6.283185307179586d + arcLength + arcOffset;
                        minus.add(Double.valueOf(minusToPlus2));
                        plus.add(Double.valueOf(minusToPlus2));
                        plus.add(Double.valueOf(a2[1]));
                    } else {
                        minus.add(Double.valueOf(a2[1]));
                    }
                } else {
                    plus.add(Double.valueOf(a2[1]));
                }
            }
        }
        return new Split(createSplitPart(minus));
    }

    private void addArcLimit(BSPTree<Sphere1D> tree, double alpha, boolean isStart) {
        LimitAngle limit = new LimitAngle(new S1Point(alpha), !isStart, getTolerance());
        BSPTree<S> cell = tree.getCell(limit.getLocation(), getTolerance());
        if (cell.getCut() != null) {
            throw new MathInternalError();
        }
        cell.insertCut(limit);
        cell.setAttribute(null);
        cell.getPlus().setAttribute(Boolean.FALSE);
        cell.getMinus().setAttribute(Boolean.TRUE);
    }

    private ArcsSet createSplitPart(List<Double> limits) {
        if (limits.isEmpty()) {
            return null;
        }
        int i2 = 0;
        while (i2 < limits.size()) {
            int j2 = (i2 + 1) % limits.size();
            double lA = limits.get(i2).doubleValue();
            double lB = MathUtils.normalizeAngle(limits.get(j2).doubleValue(), lA);
            if (FastMath.abs(lB - lA) <= getTolerance()) {
                if (j2 > 0) {
                    limits.remove(j2);
                    limits.remove(i2);
                    i2--;
                } else {
                    double lEnd = limits.remove(limits.size() - 1).doubleValue();
                    double lStart = limits.remove(0).doubleValue();
                    if (limits.isEmpty()) {
                        if (lEnd - lStart > 3.141592653589793d) {
                            return new ArcsSet((BSPTree<Sphere1D>) new BSPTree(Boolean.TRUE), getTolerance());
                        }
                        return null;
                    }
                    limits.add(Double.valueOf(limits.remove(0).doubleValue() + 6.283185307179586d));
                }
            }
            i2++;
        }
        BSPTree<Sphere1D> tree = new BSPTree<>(Boolean.FALSE);
        for (int i3 = 0; i3 < limits.size() - 1; i3 += 2) {
            addArcLimit(tree, limits.get(i3).doubleValue(), true);
            addArcLimit(tree, limits.get(i3 + 1).doubleValue(), false);
        }
        if (tree.getCut() == null) {
            return null;
        }
        return new ArcsSet(tree, getTolerance());
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/spherical/oned/ArcsSet$Split.class */
    public static class Split {
        private final ArcsSet plus;
        private final ArcsSet minus;

        private Split(ArcsSet plus, ArcsSet minus) {
            this.plus = plus;
            this.minus = minus;
        }

        public ArcsSet getPlus() {
            return this.plus;
        }

        public ArcsSet getMinus() {
            return this.minus;
        }

        public Side getSide() {
            if (this.plus != null) {
                if (this.minus != null) {
                    return Side.BOTH;
                }
                return Side.PLUS;
            }
            if (this.minus != null) {
                return Side.MINUS;
            }
            return Side.HYPER;
        }
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/spherical/oned/ArcsSet$InconsistentStateAt2PiWrapping.class */
    public static class InconsistentStateAt2PiWrapping extends MathIllegalArgumentException {
        private static final long serialVersionUID = 20140107;

        public InconsistentStateAt2PiWrapping() {
            super(LocalizedFormats.INCONSISTENT_STATE_AT_2_PI_WRAPPING, new Object[0]);
        }
    }
}
