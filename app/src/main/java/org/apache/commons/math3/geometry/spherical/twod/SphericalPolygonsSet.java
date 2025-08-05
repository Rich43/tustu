package org.apache.commons.math3.geometry.spherical.twod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.geometry.enclosing.EnclosingBall;
import org.apache.commons.math3.geometry.enclosing.WelzlEncloser;
import org.apache.commons.math3.geometry.euclidean.threed.Euclidean3D;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.SphereGenerator;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.partitioning.AbstractRegion;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.BoundaryProjection;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.RegionFactory;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.spherical.oned.Sphere1D;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/spherical/twod/SphericalPolygonsSet.class */
public class SphericalPolygonsSet extends AbstractRegion<Sphere2D, Sphere1D> {
    private List<Vertex> loops;

    @Override // org.apache.commons.math3.geometry.partitioning.AbstractRegion, org.apache.commons.math3.geometry.partitioning.Region
    public /* bridge */ /* synthetic */ AbstractRegion buildNew(BSPTree bSPTree) {
        return buildNew((BSPTree<Sphere2D>) bSPTree);
    }

    @Override // org.apache.commons.math3.geometry.partitioning.AbstractRegion, org.apache.commons.math3.geometry.partitioning.Region
    public /* bridge */ /* synthetic */ Region buildNew(BSPTree bSPTree) {
        return buildNew((BSPTree<Sphere2D>) bSPTree);
    }

    public SphericalPolygonsSet(double tolerance) {
        super(tolerance);
    }

    public SphericalPolygonsSet(Vector3D pole, double tolerance) {
        super(new BSPTree(new Circle(pole, tolerance).wholeHyperplane(), new BSPTree(Boolean.FALSE), new BSPTree(Boolean.TRUE), null), tolerance);
    }

    public SphericalPolygonsSet(Vector3D center, Vector3D meridian, double outsideRadius, int n2, double tolerance) {
        this(tolerance, createRegularPolygonVertices(center, meridian, outsideRadius, n2));
    }

    public SphericalPolygonsSet(BSPTree<Sphere2D> tree, double tolerance) {
        super(tree, tolerance);
    }

    public SphericalPolygonsSet(Collection<SubHyperplane<Sphere2D>> boundary, double tolerance) {
        super(boundary, tolerance);
    }

    public SphericalPolygonsSet(double hyperplaneThickness, S2Point... vertices) {
        super(verticesToTree(hyperplaneThickness, vertices), hyperplaneThickness);
    }

    private static S2Point[] createRegularPolygonVertices(Vector3D center, Vector3D meridian, double outsideRadius, int n2) {
        S2Point[] array = new S2Point[n2];
        Rotation r0 = new Rotation(Vector3D.crossProduct(center, meridian), outsideRadius, RotationConvention.VECTOR_OPERATOR);
        array[0] = new S2Point(r0.applyTo(center));
        Rotation r2 = new Rotation(center, 6.283185307179586d / n2, RotationConvention.VECTOR_OPERATOR);
        for (int i2 = 1; i2 < n2; i2++) {
            array[i2] = new S2Point(r2.applyTo(array[i2 - 1].getVector()));
        }
        return array;
    }

    private static BSPTree<Sphere2D> verticesToTree(double hyperplaneThickness, S2Point... vertices) {
        int n2 = vertices.length;
        if (n2 == 0) {
            return new BSPTree<>(Boolean.TRUE);
        }
        Vertex[] vArray = new Vertex[n2];
        for (int i2 = 0; i2 < n2; i2++) {
            vArray[i2] = new Vertex(vertices[i2]);
        }
        List<Edge> edges = new ArrayList<>(n2);
        Vertex end = vArray[n2 - 1];
        for (int i3 = 0; i3 < n2; i3++) {
            Vertex start = end;
            end = vArray[i3];
            Circle circle = start.sharedCircleWith(end);
            if (circle == null) {
                circle = new Circle(start.getLocation(), end.getLocation(), hyperplaneThickness);
            }
            edges.add(new Edge(start, end, Vector3D.angle(start.getLocation().getVector(), end.getLocation().getVector()), circle));
            for (Vertex vertex : vArray) {
                if (vertex != start && vertex != end && FastMath.abs(circle.getOffset(vertex.getLocation())) <= hyperplaneThickness) {
                    vertex.bindWith(circle);
                }
            }
        }
        BSPTree<Sphere2D> tree = new BSPTree<>();
        insertEdges(hyperplaneThickness, tree, edges);
        return tree;
    }

    private static void insertEdges(double hyperplaneThickness, BSPTree<Sphere2D> node, List<Edge> edges) {
        int index = 0;
        Edge inserted = null;
        while (inserted == null && index < edges.size()) {
            int i2 = index;
            index++;
            inserted = edges.get(i2);
            if (!node.insertCut(inserted.getCircle())) {
                inserted = null;
            }
        }
        if (inserted == null) {
            BSPTree<S> parent = node.getParent();
            if (parent == 0 || node == parent.getMinus()) {
                node.setAttribute(Boolean.TRUE);
                return;
            } else {
                node.setAttribute(Boolean.FALSE);
                return;
            }
        }
        List<Edge> outsideList = new ArrayList<>();
        List<Edge> insideList = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge != inserted) {
                edge.split(inserted.getCircle(), outsideList, insideList);
            }
        }
        if (!outsideList.isEmpty()) {
            insertEdges(hyperplaneThickness, node.getPlus(), outsideList);
        } else {
            node.getPlus().setAttribute(Boolean.FALSE);
        }
        if (!insideList.isEmpty()) {
            insertEdges(hyperplaneThickness, node.getMinus(), insideList);
        } else {
            node.getMinus().setAttribute(Boolean.TRUE);
        }
    }

    @Override // org.apache.commons.math3.geometry.partitioning.AbstractRegion, org.apache.commons.math3.geometry.partitioning.Region
    public SphericalPolygonsSet buildNew(BSPTree<Sphere2D> tree) {
        return new SphericalPolygonsSet(tree, getTolerance());
    }

    @Override // org.apache.commons.math3.geometry.partitioning.AbstractRegion
    protected void computeGeometricalProperties() throws MathIllegalStateException {
        BSPTree<Sphere2D> tree = getTree(true);
        if (tree.getCut() == null) {
            if (tree.getCut() == null && ((Boolean) tree.getAttribute()).booleanValue()) {
                setSize(12.566370614359172d);
                setBarycenter(new S2Point(0.0d, 0.0d));
                return;
            } else {
                setSize(0.0d);
                setBarycenter(S2Point.NaN);
                return;
            }
        }
        PropertiesComputer pc = new PropertiesComputer(getTolerance());
        tree.visit(pc);
        setSize(pc.getArea());
        setBarycenter(pc.getBarycenter());
    }

    public List<Vertex> getBoundaryLoops() throws MathIllegalStateException {
        if (this.loops == null) {
            if (getTree(false).getCut() == null) {
                this.loops = Collections.emptyList();
            } else {
                BSPTree<Sphere2D> root = getTree(true);
                EdgesBuilder visitor = new EdgesBuilder(root, getTolerance());
                root.visit(visitor);
                List<Edge> edges = visitor.getEdges();
                this.loops = new ArrayList();
                while (!edges.isEmpty()) {
                    Edge edge = edges.get(0);
                    Vertex startVertex = edge.getStart();
                    this.loops.add(startVertex);
                    do {
                        Iterator<Edge> iterator = edges.iterator();
                        while (true) {
                            if (!iterator.hasNext()) {
                                break;
                            }
                            if (iterator.next() == edge) {
                                iterator.remove();
                                break;
                            }
                        }
                        edge = edge.getEnd().getOutgoing();
                    } while (edge.getStart() != startVertex);
                }
            }
        }
        return Collections.unmodifiableList(this.loops);
    }

    public EnclosingBall<Sphere2D, S2Point> getEnclosingCap() throws MathIllegalStateException {
        if (isEmpty()) {
            return new EnclosingBall<>(S2Point.PLUS_K, Double.NEGATIVE_INFINITY, new S2Point[0]);
        }
        if (isFull()) {
            return new EnclosingBall<>(S2Point.PLUS_K, Double.POSITIVE_INFINITY, new S2Point[0]);
        }
        BSPTree<Sphere2D> root = getTree(false);
        if (isEmpty(root.getMinus()) && isFull(root.getPlus())) {
            Circle circle = (Circle) root.getCut().getHyperplane();
            return new EnclosingBall<>(new S2Point(circle.getPole()).negate(), 1.5707963267948966d, new S2Point[0]);
        }
        if (isFull(root.getMinus()) && isEmpty(root.getPlus())) {
            Circle circle2 = (Circle) root.getCut().getHyperplane();
            return new EnclosingBall<>(new S2Point(circle2.getPole()), 1.5707963267948966d, new S2Point[0]);
        }
        List<Vector3D> points = getInsidePoints();
        List<Vertex> boundary = getBoundaryLoops();
        for (Vertex loopStart : boundary) {
            int count = 0;
            Vertex end = loopStart;
            while (true) {
                Vertex v2 = end;
                if (count == 0 || v2 != loopStart) {
                    count++;
                    points.add(v2.getLocation().getVector());
                    end = v2.getOutgoing().getEnd();
                }
            }
        }
        SphereGenerator generator = new SphereGenerator();
        WelzlEncloser<Euclidean3D, Vector3D> encloser = new WelzlEncloser<>(getTolerance(), generator);
        EnclosingBall enclosingBallEnclose = encloser.enclose(points);
        Vector3D[] support3D = (Vector3D[]) enclosingBallEnclose.getSupport();
        double r2 = enclosingBallEnclose.getRadius();
        double h2 = ((Vector3D) enclosingBallEnclose.getCenter()).getNorm();
        if (h2 < getTolerance()) {
            EnclosingBall<Sphere2D, S2Point> enclosingS2 = new EnclosingBall<>(S2Point.PLUS_K, Double.POSITIVE_INFINITY, new S2Point[0]);
            for (Vector3D outsidePoint : getOutsidePoints()) {
                S2Point outsideS2 = new S2Point(outsidePoint);
                BoundaryProjection<Sphere2D> projection = projectToBoundary(outsideS2);
                if (3.141592653589793d - projection.getOffset() < enclosingS2.getRadius()) {
                    enclosingS2 = new EnclosingBall<>(outsideS2.negate(), 3.141592653589793d - projection.getOffset(), (S2Point) projection.getProjected());
                }
            }
            return enclosingS2;
        }
        S2Point[] support = new S2Point[support3D.length];
        for (int i2 = 0; i2 < support3D.length; i2++) {
            support[i2] = new S2Point(support3D[i2]);
        }
        EnclosingBall<Sphere2D, S2Point> enclosingS22 = new EnclosingBall<>(new S2Point((Vector3D) enclosingBallEnclose.getCenter()), FastMath.acos(((1.0d + (h2 * h2)) - (r2 * r2)) / (2.0d * h2)), support);
        return enclosingS22;
    }

    private List<Vector3D> getInsidePoints() {
        PropertiesComputer pc = new PropertiesComputer(getTolerance());
        getTree(true).visit(pc);
        return pc.getConvexCellsInsidePoints();
    }

    private List<Vector3D> getOutsidePoints() {
        SphericalPolygonsSet complement = (SphericalPolygonsSet) new RegionFactory().getComplement(this);
        PropertiesComputer pc = new PropertiesComputer(getTolerance());
        complement.getTree(true).visit(pc);
        return pc.getConvexCellsInsidePoints();
    }
}
