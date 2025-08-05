package org.apache.commons.math3.geometry.spherical.twod;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor;
import org.apache.commons.math3.geometry.partitioning.BoundaryAttribute;
import org.apache.commons.math3.geometry.spherical.oned.Arc;
import org.apache.commons.math3.geometry.spherical.oned.ArcsSet;
import org.apache.commons.math3.geometry.spherical.oned.S1Point;
import org.apache.commons.math3.geometry.spherical.oned.Sphere1D;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/spherical/twod/EdgesBuilder.class */
class EdgesBuilder implements BSPTreeVisitor<Sphere2D> {
    private final BSPTree<Sphere2D> root;
    private final double tolerance;
    private final Map<Edge, BSPTree<Sphere2D>> edgeToNode = new IdentityHashMap();
    private final Map<BSPTree<Sphere2D>, List<Edge>> nodeToEdgesList = new IdentityHashMap();

    EdgesBuilder(BSPTree<Sphere2D> root, double tolerance) {
        this.root = root;
        this.tolerance = tolerance;
    }

    @Override // org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor
    public BSPTreeVisitor.Order visitOrder(BSPTree<Sphere2D> node) {
        return BSPTreeVisitor.Order.MINUS_SUB_PLUS;
    }

    @Override // org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor
    public void visitInternalNode(BSPTree<Sphere2D> node) {
        this.nodeToEdgesList.put(node, new ArrayList());
        BoundaryAttribute<Sphere2D> attribute = (BoundaryAttribute) node.getAttribute();
        if (attribute.getPlusOutside() != null) {
            addContribution((SubCircle) attribute.getPlusOutside(), false, node);
        }
        if (attribute.getPlusInside() != null) {
            addContribution((SubCircle) attribute.getPlusInside(), true, node);
        }
    }

    @Override // org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor
    public void visitLeafNode(BSPTree<Sphere2D> node) {
    }

    private void addContribution(SubCircle sub, boolean reversed, BSPTree<Sphere2D> node) {
        Edge edge;
        Circle circle = (Circle) sub.getHyperplane();
        List<Arc> arcs = ((ArcsSet) sub.getRemainingRegion()).asList();
        for (Arc a2 : arcs) {
            Vertex start = new Vertex(circle.toSpace((Point<Sphere1D>) new S1Point(a2.getInf())));
            Vertex end = new Vertex(circle.toSpace((Point<Sphere1D>) new S1Point(a2.getSup())));
            start.bindWith(circle);
            end.bindWith(circle);
            if (reversed) {
                edge = new Edge(end, start, a2.getSize(), circle.getReverse());
            } else {
                edge = new Edge(start, end, a2.getSize(), circle);
            }
            Edge edge2 = edge;
            this.edgeToNode.put(edge2, node);
            this.nodeToEdgesList.get(node).add(edge2);
        }
    }

    private Edge getFollowingEdge(Edge previous) throws MathIllegalStateException, MathArithmeticException {
        S2Point point = previous.getEnd().getLocation();
        List<BSPTree<S>> closeCuts = this.root.getCloseCuts(point, this.tolerance);
        double closest = this.tolerance;
        Edge following = null;
        Iterator i$ = closeCuts.iterator();
        while (i$.hasNext()) {
            BSPTree<Sphere2D> node = (BSPTree) i$.next();
            for (Edge edge : this.nodeToEdgesList.get(node)) {
                if (edge != previous && edge.getStart().getIncoming() == null) {
                    Vector3D edgeStart = edge.getStart().getLocation().getVector();
                    double gap = Vector3D.angle(point.getVector(), edgeStart);
                    if (gap <= closest) {
                        closest = gap;
                        following = edge;
                    }
                }
            }
        }
        if (following == null) {
            Vector3D previousStart = previous.getStart().getLocation().getVector();
            if (Vector3D.angle(point.getVector(), previousStart) <= this.tolerance) {
                return previous;
            }
            throw new MathIllegalStateException(LocalizedFormats.OUTLINE_BOUNDARY_LOOP_OPEN, new Object[0]);
        }
        return following;
    }

    public List<Edge> getEdges() throws MathIllegalStateException {
        for (Edge previous : this.edgeToNode.keySet()) {
            previous.setNextEdge(getFollowingEdge(previous));
        }
        return new ArrayList(this.edgeToNode.keySet());
    }
}
