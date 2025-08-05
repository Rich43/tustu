package org.apache.commons.math3.geometry.spherical.twod;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/spherical/twod/PropertiesComputer.class */
class PropertiesComputer implements BSPTreeVisitor<Sphere2D> {
    private final double tolerance;
    private double summedArea = 0.0d;
    private Vector3D summedBarycenter = Vector3D.ZERO;
    private final List<Vector3D> convexCellsInsidePoints = new ArrayList();

    PropertiesComputer(double tolerance) {
        this.tolerance = tolerance;
    }

    @Override // org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor
    public BSPTreeVisitor.Order visitOrder(BSPTree<Sphere2D> node) {
        return BSPTreeVisitor.Order.MINUS_SUB_PLUS;
    }

    @Override // org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor
    public void visitInternalNode(BSPTree<Sphere2D> node) {
    }

    @Override // org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor
    public void visitLeafNode(BSPTree<Sphere2D> node) throws MathIllegalStateException {
        if (((Boolean) node.getAttribute()).booleanValue()) {
            SphericalPolygonsSet convex = new SphericalPolygonsSet((BSPTree<Sphere2D>) node.pruneAroundConvexCell(Boolean.TRUE, Boolean.FALSE, null), this.tolerance);
            List<Vertex> boundary = convex.getBoundaryLoops();
            if (boundary.size() != 1) {
                throw new MathInternalError();
            }
            double area = convexCellArea(boundary.get(0));
            Vector3D barycenter = convexCellBarycenter(boundary.get(0));
            this.convexCellsInsidePoints.add(barycenter);
            this.summedArea += area;
            this.summedBarycenter = new Vector3D(1.0d, this.summedBarycenter, area, barycenter);
        }
    }

    private double convexCellArea(Vertex start) {
        int n2 = 0;
        double sum = 0.0d;
        Edge outgoing = start.getOutgoing();
        while (true) {
            Edge e2 = outgoing;
            if (n2 == 0 || e2.getStart() != start) {
                Vector3D previousPole = e2.getCircle().getPole();
                Vector3D nextPole = e2.getEnd().getOutgoing().getCircle().getPole();
                Vector3D point = e2.getEnd().getLocation().getVector();
                double alpha = FastMath.atan2(Vector3D.dotProduct(nextPole, Vector3D.crossProduct(point, previousPole)), -Vector3D.dotProduct(nextPole, previousPole));
                if (alpha < 0.0d) {
                    alpha += 6.283185307179586d;
                }
                sum += alpha;
                n2++;
                outgoing = e2.getEnd().getOutgoing();
            } else {
                return sum - ((n2 - 2) * 3.141592653589793d);
            }
        }
    }

    private Vector3D convexCellBarycenter(Vertex start) {
        int n2 = 0;
        Vector3D sumB = Vector3D.ZERO;
        Edge outgoing = start.getOutgoing();
        while (true) {
            Edge e2 = outgoing;
            if (n2 == 0 || e2.getStart() != start) {
                sumB = new Vector3D(1.0d, sumB, e2.getLength(), e2.getCircle().getPole());
                n2++;
                outgoing = e2.getEnd().getOutgoing();
            } else {
                return sumB.normalize();
            }
        }
    }

    public double getArea() {
        return this.summedArea;
    }

    public S2Point getBarycenter() {
        if (this.summedBarycenter.getNormSq() == 0.0d) {
            return S2Point.NaN;
        }
        return new S2Point(this.summedBarycenter);
    }

    public List<Vector3D> getConvexCellsInsidePoints() {
        return this.convexCellsInsidePoints;
    }
}
