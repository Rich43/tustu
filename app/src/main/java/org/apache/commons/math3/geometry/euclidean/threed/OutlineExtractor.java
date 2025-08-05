package org.apache.commons.math3.geometry.euclidean.threed;

import java.util.ArrayList;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.PolygonsSet;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor;
import org.apache.commons.math3.geometry.partitioning.BoundaryAttribute;
import org.apache.commons.math3.geometry.partitioning.RegionFactory;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/euclidean/threed/OutlineExtractor.class */
public class OutlineExtractor {

    /* renamed from: u, reason: collision with root package name */
    private Vector3D f13004u;

    /* renamed from: v, reason: collision with root package name */
    private Vector3D f13005v;

    /* renamed from: w, reason: collision with root package name */
    private Vector3D f13006w;

    public OutlineExtractor(Vector3D u2, Vector3D v2) {
        this.f13004u = u2;
        this.f13005v = v2;
        this.f13006w = Vector3D.crossProduct(u2, v2);
    }

    public Vector2D[][] getOutline(PolyhedronsSet polyhedronsSet) {
        BoundaryProjector projector = new BoundaryProjector(polyhedronsSet.getTolerance());
        polyhedronsSet.getTree(true).visit(projector);
        PolygonsSet projected = projector.getProjected();
        Vector2D[][] outline = projected.getVertices();
        for (int i2 = 0; i2 < outline.length; i2++) {
            Vector2D[] rawLoop = outline[i2];
            int end = rawLoop.length;
            int j2 = 0;
            while (j2 < end) {
                if (pointIsBetween(rawLoop, end, j2)) {
                    for (int k2 = j2; k2 < end - 1; k2++) {
                        rawLoop[k2] = rawLoop[k2 + 1];
                    }
                    end--;
                } else {
                    j2++;
                }
            }
            if (end != rawLoop.length) {
                outline[i2] = new Vector2D[end];
                System.arraycopy(rawLoop, 0, outline[i2], 0, end);
            }
        }
        return outline;
    }

    private boolean pointIsBetween(Vector2D[] loop, int n2, int i2) {
        Vector2D previous = loop[((i2 + n2) - 1) % n2];
        Vector2D current = loop[i2];
        Vector2D next = loop[(i2 + 1) % n2];
        double dx1 = current.getX() - previous.getX();
        double dy1 = current.getY() - previous.getY();
        double dx2 = next.getX() - current.getX();
        double dy2 = next.getY() - current.getY();
        double cross = (dx1 * dy2) - (dx2 * dy1);
        double dot = (dx1 * dx2) + (dy1 * dy2);
        double d1d2 = FastMath.sqrt(((dx1 * dx1) + (dy1 * dy1)) * ((dx2 * dx2) + (dy2 * dy2)));
        return FastMath.abs(cross) <= 1.0E-6d * d1d2 && dot >= 0.0d;
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/euclidean/threed/OutlineExtractor$BoundaryProjector.class */
    private class BoundaryProjector implements BSPTreeVisitor<Euclidean3D> {
        private PolygonsSet projected;
        private final double tolerance;

        BoundaryProjector(double tolerance) {
            this.projected = new PolygonsSet((BSPTree<Euclidean2D>) new BSPTree(Boolean.FALSE), tolerance);
            this.tolerance = tolerance;
        }

        @Override // org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor
        public BSPTreeVisitor.Order visitOrder(BSPTree<Euclidean3D> node) {
            return BSPTreeVisitor.Order.MINUS_SUB_PLUS;
        }

        @Override // org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor
        public void visitInternalNode(BSPTree<Euclidean3D> node) {
            BoundaryAttribute<Euclidean3D> attribute = (BoundaryAttribute) node.getAttribute();
            if (attribute.getPlusOutside() != null) {
                addContribution(attribute.getPlusOutside(), false);
            }
            if (attribute.getPlusInside() != null) {
                addContribution(attribute.getPlusInside(), true);
            }
        }

        @Override // org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor
        public void visitLeafNode(BSPTree<Euclidean3D> node) {
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v80, types: [org.apache.commons.math3.geometry.euclidean.twod.Vector2D[]] */
        private void addContribution(SubHyperplane<Euclidean3D> facet, boolean reversed) {
            AbstractSubHyperplane<Euclidean3D, Euclidean2D> absFacet = (AbstractSubHyperplane) facet;
            Plane plane = (Plane) facet.getHyperplane();
            double scal = plane.getNormal().dotProduct(OutlineExtractor.this.f13006w);
            if (FastMath.abs(scal) > 0.001d) {
                Vector2D[][] vertices = ((PolygonsSet) absFacet.getRemainingRegion()).getVertices();
                if ((scal < 0.0d) ^ reversed) {
                    ?? r0 = new Vector2D[vertices.length];
                    for (int i2 = 0; i2 < vertices.length; i2++) {
                        Vector2D[] loop = vertices[i2];
                        Vector2D[] newLoop = new Vector2D[loop.length];
                        if (loop[0] == null) {
                            newLoop[0] = null;
                            for (int j2 = 1; j2 < loop.length; j2++) {
                                newLoop[j2] = loop[loop.length - j2];
                            }
                        } else {
                            for (int j3 = 0; j3 < loop.length; j3++) {
                                newLoop[j3] = loop[loop.length - (j3 + 1)];
                            }
                        }
                        r0[i2] = newLoop;
                    }
                    vertices = r0;
                }
                ArrayList<SubHyperplane<Euclidean2D>> edges = new ArrayList<>();
                Vector2D[][] arr$ = vertices;
                for (Vector2D[] loop2 : arr$) {
                    boolean closed = loop2[0] != null;
                    int previous = closed ? loop2.length - 1 : 1;
                    Vector3D previous3D = plane.toSpace((Point<Euclidean2D>) loop2[previous]);
                    int current = (previous + 1) % loop2.length;
                    Vector2D vector2D = new Vector2D(previous3D.dotProduct(OutlineExtractor.this.f13004u), previous3D.dotProduct(OutlineExtractor.this.f13005v));
                    while (true) {
                        Vector2D pPoint = vector2D;
                        if (current < loop2.length) {
                            Vector3D current3D = plane.toSpace((Point<Euclidean2D>) loop2[current]);
                            Vector2D cPoint = new Vector2D(current3D.dotProduct(OutlineExtractor.this.f13004u), current3D.dotProduct(OutlineExtractor.this.f13005v));
                            org.apache.commons.math3.geometry.euclidean.twod.Line line = new org.apache.commons.math3.geometry.euclidean.twod.Line(pPoint, cPoint, this.tolerance);
                            SubHyperplane<Euclidean2D> edge = line.wholeHyperplane();
                            if (closed || previous != 1) {
                                double angle = line.getAngle() + 1.5707963267948966d;
                                org.apache.commons.math3.geometry.euclidean.twod.Line l2 = new org.apache.commons.math3.geometry.euclidean.twod.Line(pPoint, angle, this.tolerance);
                                edge = edge.split(l2).getPlus();
                            }
                            if (closed || current != loop2.length - 1) {
                                double angle2 = line.getAngle() + 1.5707963267948966d;
                                org.apache.commons.math3.geometry.euclidean.twod.Line l3 = new org.apache.commons.math3.geometry.euclidean.twod.Line(cPoint, angle2, this.tolerance);
                                edge = edge.split(l3).getMinus();
                            }
                            edges.add(edge);
                            int i3 = current;
                            current++;
                            previous = i3;
                            vector2D = cPoint;
                        }
                    }
                }
                PolygonsSet projectedFacet = new PolygonsSet(edges, this.tolerance);
                this.projected = (PolygonsSet) new RegionFactory().union(this.projected, projectedFacet);
            }
        }

        public PolygonsSet getProjected() {
            return this.projected;
        }
    }
}
