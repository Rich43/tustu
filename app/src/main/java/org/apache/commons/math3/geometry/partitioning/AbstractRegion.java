package org.apache.commons.math3.geometry.partitioning;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.Space;
import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/partitioning/AbstractRegion.class */
public abstract class AbstractRegion<S extends Space, T extends Space> implements Region<S> {
    private BSPTree<S> tree;
    private final double tolerance;
    private double size;
    private Point<S> barycenter;

    @Override // org.apache.commons.math3.geometry.partitioning.Region
    public abstract AbstractRegion<S, T> buildNew(BSPTree<S> bSPTree);

    protected abstract void computeGeometricalProperties();

    protected AbstractRegion(double tolerance) {
        this.tree = new BSPTree<>(Boolean.TRUE);
        this.tolerance = tolerance;
    }

    protected AbstractRegion(BSPTree<S> tree, double tolerance) {
        this.tree = tree;
        this.tolerance = tolerance;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected AbstractRegion(Collection<SubHyperplane<S>> collection, double d2) {
        this.tolerance = d2;
        if (collection.size() == 0) {
            this.tree = new BSPTree<>(Boolean.TRUE);
            return;
        }
        TreeSet treeSet = new TreeSet(new Comparator<SubHyperplane<S>>() { // from class: org.apache.commons.math3.geometry.partitioning.AbstractRegion.1
            @Override // java.util.Comparator
            public int compare(SubHyperplane<S> o1, SubHyperplane<S> o2) {
                double size1 = o1.getSize();
                double size2 = o2.getSize();
                if (size2 < size1) {
                    return -1;
                }
                return o1 == o2 ? 0 : 1;
            }
        });
        treeSet.addAll(collection);
        this.tree = new BSPTree<>();
        insertCuts(this.tree, treeSet);
        this.tree.visit(new BSPTreeVisitor<S>() { // from class: org.apache.commons.math3.geometry.partitioning.AbstractRegion.2
            @Override // org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor
            public BSPTreeVisitor.Order visitOrder(BSPTree<S> node) {
                return BSPTreeVisitor.Order.PLUS_SUB_MINUS;
            }

            @Override // org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor
            public void visitInternalNode(BSPTree<S> node) {
            }

            @Override // org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor
            public void visitLeafNode(BSPTree<S> node) {
                if (node.getParent() == null || node == node.getParent().getMinus()) {
                    node.setAttribute(Boolean.TRUE);
                } else {
                    node.setAttribute(Boolean.FALSE);
                }
            }
        });
    }

    public AbstractRegion(Hyperplane<S>[] hyperplanes, double tolerance) {
        this.tolerance = tolerance;
        if (hyperplanes == null || hyperplanes.length == 0) {
            this.tree = new BSPTree<>(Boolean.FALSE);
            return;
        }
        this.tree = hyperplanes[0].wholeSpace().getTree(false);
        BSPTree<S> node = this.tree;
        node.setAttribute(Boolean.TRUE);
        for (Hyperplane<S> hyperplane : hyperplanes) {
            if (node.insertCut(hyperplane)) {
                node.setAttribute(null);
                node.getPlus().setAttribute(Boolean.FALSE);
                node = node.getMinus();
                node.setAttribute(Boolean.TRUE);
            }
        }
    }

    public double getTolerance() {
        return this.tolerance;
    }

    private void insertCuts(BSPTree<S> node, Collection<SubHyperplane<S>> boundary) {
        Iterator<SubHyperplane<S>> iterator = boundary.iterator();
        Hyperplane<S> inserted = null;
        while (inserted == null && iterator.hasNext()) {
            inserted = iterator.next().getHyperplane();
            if (!node.insertCut(inserted.copySelf())) {
                inserted = null;
            }
        }
        if (!iterator.hasNext()) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        while (iterator.hasNext()) {
            SubHyperplane<S> other = iterator.next();
            SubHyperplane.SplitSubHyperplane<S> split = other.split(inserted);
            switch (split.getSide()) {
                case PLUS:
                    arrayList.add(other);
                    break;
                case MINUS:
                    arrayList2.add(other);
                    break;
                case BOTH:
                    arrayList.add(split.getPlus());
                    arrayList2.add(split.getMinus());
                    break;
            }
        }
        insertCuts(node.getPlus(), arrayList);
        insertCuts(node.getMinus(), arrayList2);
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Region
    public AbstractRegion<S, T> copySelf() {
        return buildNew((BSPTree) this.tree.copySelf());
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Region
    public boolean isEmpty() {
        return isEmpty(this.tree);
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Region
    public boolean isEmpty(BSPTree<S> node) {
        return node.getCut() == null ? !((Boolean) node.getAttribute()).booleanValue() : isEmpty(node.getMinus()) && isEmpty(node.getPlus());
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Region
    public boolean isFull() {
        return isFull(this.tree);
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Region
    public boolean isFull(BSPTree<S> node) {
        if (node.getCut() == null) {
            return ((Boolean) node.getAttribute()).booleanValue();
        }
        return isFull(node.getMinus()) && isFull(node.getPlus());
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Region
    public boolean contains(Region<S> region) {
        return new RegionFactory().difference(region, this).isEmpty();
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Region
    public BoundaryProjection<S> projectToBoundary(Point<S> point) {
        BoundaryProjector<S, T> projector = new BoundaryProjector<>(point);
        getTree(true).visit(projector);
        return projector.getProjection();
    }

    public Region.Location checkPoint(Vector<S> point) {
        return checkPoint((Point) point);
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Region
    public Region.Location checkPoint(Point<S> point) {
        return checkPoint(this.tree, point);
    }

    protected Region.Location checkPoint(BSPTree<S> node, Vector<S> point) {
        return checkPoint((BSPTree) node, (Point) point);
    }

    protected Region.Location checkPoint(BSPTree<S> node, Point<S> point) {
        BSPTree<S> cell = node.getCell(point, this.tolerance);
        if (cell.getCut() == null) {
            return ((Boolean) cell.getAttribute()).booleanValue() ? Region.Location.INSIDE : Region.Location.OUTSIDE;
        }
        Region.Location minusCode = checkPoint(cell.getMinus(), point);
        Region.Location plusCode = checkPoint(cell.getPlus(), point);
        return minusCode == plusCode ? minusCode : Region.Location.BOUNDARY;
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Region
    public BSPTree<S> getTree(boolean includeBoundaryAttributes) {
        if (includeBoundaryAttributes && this.tree.getCut() != null && this.tree.getAttribute() == null) {
            this.tree.visit(new BoundaryBuilder());
        }
        return this.tree;
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Region
    public double getBoundarySize() {
        BoundarySizeVisitor<S> visitor = new BoundarySizeVisitor<>();
        getTree(true).visit(visitor);
        return visitor.getSize();
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Region
    public double getSize() {
        if (this.barycenter == null) {
            computeGeometricalProperties();
        }
        return this.size;
    }

    protected void setSize(double size) {
        this.size = size;
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Region
    public Point<S> getBarycenter() {
        if (this.barycenter == null) {
            computeGeometricalProperties();
        }
        return this.barycenter;
    }

    protected void setBarycenter(Vector<S> barycenter) {
        setBarycenter((Point) barycenter);
    }

    protected void setBarycenter(Point<S> barycenter) {
        this.barycenter = barycenter;
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Region
    @Deprecated
    public Side side(Hyperplane<S> hyperplane) {
        InsideFinder<S> finder = new InsideFinder<>(this);
        finder.recurseSides(this.tree, hyperplane.wholeHyperplane());
        return finder.plusFound() ? finder.minusFound() ? Side.BOTH : Side.PLUS : finder.minusFound() ? Side.MINUS : Side.HYPER;
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Region
    public SubHyperplane<S> intersection(SubHyperplane<S> sub) {
        return recurseIntersection(this.tree, sub);
    }

    private SubHyperplane<S> recurseIntersection(BSPTree<S> node, SubHyperplane<S> sub) {
        if (node.getCut() == null) {
            if (((Boolean) node.getAttribute()).booleanValue()) {
                return sub.copySelf();
            }
            return null;
        }
        Hyperplane<S> hyperplane = node.getCut().getHyperplane();
        SubHyperplane.SplitSubHyperplane<S> split = sub.split(hyperplane);
        if (split.getPlus() != null) {
            if (split.getMinus() != null) {
                SubHyperplane<S> plus = recurseIntersection(node.getPlus(), split.getPlus());
                SubHyperplane<S> minus = recurseIntersection(node.getMinus(), split.getMinus());
                if (plus == null) {
                    return minus;
                }
                if (minus == null) {
                    return plus;
                }
                return plus.reunite(minus);
            }
            return recurseIntersection(node.getPlus(), sub);
        }
        if (split.getMinus() != null) {
            return recurseIntersection(node.getMinus(), sub);
        }
        return recurseIntersection(node.getPlus(), recurseIntersection(node.getMinus(), sub));
    }

    public AbstractRegion<S, T> applyTransform(Transform<S, T> transform) {
        BoundaryAttribute<S> original;
        Map<BSPTree<S>, BSPTree<S>> map = new HashMap<>();
        BSPTree<S> transformedTree = recurseTransform(getTree(false), transform, map);
        for (Map.Entry<BSPTree<S>, BSPTree<S>> entry : map.entrySet()) {
            if (entry.getKey().getCut() != null && (original = (BoundaryAttribute) entry.getKey().getAttribute()) != null) {
                BoundaryAttribute<S> transformed = (BoundaryAttribute) entry.getValue().getAttribute();
                Iterator i$ = original.getSplitters().iterator();
                while (i$.hasNext()) {
                    BSPTree<S> splitter = i$.next();
                    transformed.getSplitters().add(map.get(splitter));
                }
            }
        }
        return buildNew((BSPTree) transformedTree);
    }

    private BSPTree<S> recurseTransform(BSPTree<S> node, Transform<S, T> transform, Map<BSPTree<S>, BSPTree<S>> map) {
        BSPTree<S> transformedNode;
        if (node.getCut() == null) {
            transformedNode = new BSPTree<>(node.getAttribute());
        } else {
            SubHyperplane<S> sub = node.getCut();
            SubHyperplane<S> tSub = ((AbstractSubHyperplane) sub).applyTransform(transform);
            BoundaryAttribute<S> attribute = (BoundaryAttribute) node.getAttribute();
            if (attribute != null) {
                SubHyperplane<S> tPO = attribute.getPlusOutside() == null ? null : ((AbstractSubHyperplane) attribute.getPlusOutside()).applyTransform(transform);
                SubHyperplane<S> tPI = attribute.getPlusInside() == null ? null : ((AbstractSubHyperplane) attribute.getPlusInside()).applyTransform(transform);
                attribute = new BoundaryAttribute<>(tPO, tPI, new NodesSet());
            }
            transformedNode = new BSPTree<>(tSub, recurseTransform(node.getPlus(), transform, map), recurseTransform(node.getMinus(), transform, map), attribute);
        }
        map.put(node, transformedNode);
        return transformedNode;
    }
}
