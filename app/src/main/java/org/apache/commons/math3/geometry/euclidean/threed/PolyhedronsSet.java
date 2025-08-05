package org.apache.commons.math3.geometry.euclidean.threed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.PolygonsSet;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractRegion;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor;
import org.apache.commons.math3.geometry.partitioning.BoundaryAttribute;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.RegionFactory;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.Transform;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/euclidean/threed/PolyhedronsSet.class */
public class PolyhedronsSet extends AbstractRegion<Euclidean3D, Euclidean2D> {
    private static final double DEFAULT_TOLERANCE = 1.0E-10d;

    @Override // org.apache.commons.math3.geometry.partitioning.AbstractRegion, org.apache.commons.math3.geometry.partitioning.Region
    public /* bridge */ /* synthetic */ AbstractRegion buildNew(BSPTree bSPTree) {
        return buildNew((BSPTree<Euclidean3D>) bSPTree);
    }

    @Override // org.apache.commons.math3.geometry.partitioning.AbstractRegion, org.apache.commons.math3.geometry.partitioning.Region
    public /* bridge */ /* synthetic */ Region buildNew(BSPTree bSPTree) {
        return buildNew((BSPTree<Euclidean3D>) bSPTree);
    }

    public PolyhedronsSet(double tolerance) {
        super(tolerance);
    }

    public PolyhedronsSet(BSPTree<Euclidean3D> tree, double tolerance) {
        super(tree, tolerance);
    }

    public PolyhedronsSet(Collection<SubHyperplane<Euclidean3D>> boundary, double tolerance) {
        super(boundary, tolerance);
    }

    public PolyhedronsSet(List<Vector3D> vertices, List<int[]> facets, double tolerance) {
        super(buildBoundary(vertices, facets, tolerance), tolerance);
    }

    public PolyhedronsSet(double xMin, double xMax, double yMin, double yMax, double zMin, double zMax, double tolerance) {
        super(buildBoundary(xMin, xMax, yMin, yMax, zMin, zMax, tolerance), tolerance);
    }

    @Deprecated
    public PolyhedronsSet() {
        this(1.0E-10d);
    }

    @Deprecated
    public PolyhedronsSet(BSPTree<Euclidean3D> tree) {
        this(tree, 1.0E-10d);
    }

    @Deprecated
    public PolyhedronsSet(Collection<SubHyperplane<Euclidean3D>> boundary) {
        this(boundary, 1.0E-10d);
    }

    @Deprecated
    public PolyhedronsSet(double xMin, double xMax, double yMin, double yMax, double zMin, double zMax) {
        this(xMin, xMax, yMin, yMax, zMin, zMax, 1.0E-10d);
    }

    private static BSPTree<Euclidean3D> buildBoundary(double xMin, double xMax, double yMin, double yMax, double zMin, double zMax, double tolerance) {
        if (xMin >= xMax - tolerance || yMin >= yMax - tolerance || zMin >= zMax - tolerance) {
            return new BSPTree<>(Boolean.FALSE);
        }
        Plane pxMin = new Plane(new Vector3D(xMin, 0.0d, 0.0d), Vector3D.MINUS_I, tolerance);
        Plane pxMax = new Plane(new Vector3D(xMax, 0.0d, 0.0d), Vector3D.PLUS_I, tolerance);
        Plane pyMin = new Plane(new Vector3D(0.0d, yMin, 0.0d), Vector3D.MINUS_J, tolerance);
        Plane pyMax = new Plane(new Vector3D(0.0d, yMax, 0.0d), Vector3D.PLUS_J, tolerance);
        Plane pzMin = new Plane(new Vector3D(0.0d, 0.0d, zMin), Vector3D.MINUS_K, tolerance);
        Plane pzMax = new Plane(new Vector3D(0.0d, 0.0d, zMax), Vector3D.PLUS_K, tolerance);
        Region<Euclidean3D> boundary = new RegionFactory().buildConvex(pxMin, pxMax, pyMin, pyMax, pzMin, pzMax);
        return boundary.getTree(false);
    }

    private static List<SubHyperplane<Euclidean3D>> buildBoundary(List<Vector3D> vertices, List<int[]> facets, double tolerance) {
        for (int i2 = 0; i2 < vertices.size() - 1; i2++) {
            Vector3D vi = vertices.get(i2);
            for (int j2 = i2 + 1; j2 < vertices.size(); j2++) {
                if (Vector3D.distance(vi, vertices.get(j2)) <= tolerance) {
                    throw new MathIllegalArgumentException(LocalizedFormats.CLOSE_VERTICES, Double.valueOf(vi.getX()), Double.valueOf(vi.getY()), Double.valueOf(vi.getZ()));
                }
            }
        }
        int[][] references = findReferences(vertices, facets);
        int[][] successors = successors(vertices, facets, references);
        int vA = 0;
        while (vA < vertices.size()) {
            int[] arr$ = successors[vA];
            for (int vB : arr$) {
                if (vB >= 0) {
                    boolean found = false;
                    for (int i3 : successors[vB]) {
                        found = found || i3 == vA;
                    }
                    if (!found) {
                        Vector3D start = vertices.get(vA);
                        Vector3D end = vertices.get(vB);
                        throw new MathIllegalArgumentException(LocalizedFormats.EDGE_CONNECTED_TO_ONE_FACET, Double.valueOf(start.getX()), Double.valueOf(start.getY()), Double.valueOf(start.getZ()), Double.valueOf(end.getX()), Double.valueOf(end.getY()), Double.valueOf(end.getZ()));
                    }
                }
            }
            vA++;
        }
        List<SubHyperplane<Euclidean3D>> boundary = new ArrayList<>();
        for (int[] facet : facets) {
            Plane plane = new Plane(vertices.get(facet[0]), vertices.get(facet[1]), vertices.get(facet[2]), tolerance);
            Vector2D[] two2Points = new Vector2D[facet.length];
            for (int i4 = 0; i4 < facet.length; i4++) {
                Vector3D v2 = vertices.get(facet[i4]);
                if (!plane.contains(v2)) {
                    throw new MathIllegalArgumentException(LocalizedFormats.OUT_OF_PLANE, Double.valueOf(v2.getX()), Double.valueOf(v2.getY()), Double.valueOf(v2.getZ()));
                }
                two2Points[i4] = plane.toSubSpace((Vector<Euclidean3D>) v2);
            }
            boundary.add(new SubPlane(plane, new PolygonsSet(tolerance, two2Points)));
        }
        return boundary;
    }

    private static int[][] findReferences(List<Vector3D> vertices, List<int[]> facets) {
        int[] nbFacets = new int[vertices.size()];
        int maxFacets = 0;
        for (int[] facet : facets) {
            if (facet.length < 3) {
                throw new NumberIsTooSmallException(LocalizedFormats.WRONG_NUMBER_OF_POINTS, 3, Integer.valueOf(facet.length), true);
            }
            for (int index : facet) {
                int i2 = nbFacets[index] + 1;
                nbFacets[index] = i2;
                maxFacets = FastMath.max(maxFacets, i2);
            }
        }
        int[][] references = new int[vertices.size()][maxFacets];
        for (int[] r2 : references) {
            Arrays.fill(r2, -1);
        }
        for (int f2 = 0; f2 < facets.size(); f2++) {
            int[] arr$ = facets.get(f2);
            for (int v2 : arr$) {
                int k2 = 0;
                while (k2 < maxFacets && references[v2][k2] >= 0) {
                    k2++;
                }
                references[v2][k2] = f2;
            }
        }
        return references;
    }

    private static int[][] successors(List<Vector3D> vertices, List<int[]> facets, int[][] references) {
        int[][] successors = new int[vertices.size()][references[0].length];
        for (int[] s2 : successors) {
            Arrays.fill(s2, -1);
        }
        for (int v2 = 0; v2 < vertices.size(); v2++) {
            for (int k2 = 0; k2 < successors[v2].length && references[v2][k2] >= 0; k2++) {
                int[] facet = facets.get(references[v2][k2]);
                int i2 = 0;
                while (i2 < facet.length && facet[i2] != v2) {
                    i2++;
                }
                successors[v2][k2] = facet[(i2 + 1) % facet.length];
                for (int l2 = 0; l2 < k2; l2++) {
                    if (successors[v2][l2] == successors[v2][k2]) {
                        Vector3D start = vertices.get(v2);
                        Vector3D end = vertices.get(successors[v2][k2]);
                        throw new MathIllegalArgumentException(LocalizedFormats.FACET_ORIENTATION_MISMATCH, Double.valueOf(start.getX()), Double.valueOf(start.getY()), Double.valueOf(start.getZ()), Double.valueOf(end.getX()), Double.valueOf(end.getY()), Double.valueOf(end.getZ()));
                    }
                }
            }
        }
        return successors;
    }

    @Override // org.apache.commons.math3.geometry.partitioning.AbstractRegion, org.apache.commons.math3.geometry.partitioning.Region
    public PolyhedronsSet buildNew(BSPTree<Euclidean3D> tree) {
        return new PolyhedronsSet(tree, getTolerance());
    }

    @Override // org.apache.commons.math3.geometry.partitioning.AbstractRegion
    protected void computeGeometricalProperties() {
        getTree(true).visit(new FacetsContributionVisitor());
        if (getSize() < 0.0d) {
            setSize(Double.POSITIVE_INFINITY);
            setBarycenter((Point) Vector3D.NaN);
        } else {
            setSize(getSize() / 3.0d);
            setBarycenter((Point) new Vector3D(1.0d / (4.0d * getSize()), (Vector3D) getBarycenter()));
        }
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/euclidean/threed/PolyhedronsSet$FacetsContributionVisitor.class */
    private class FacetsContributionVisitor implements BSPTreeVisitor<Euclidean3D> {
        FacetsContributionVisitor() {
            PolyhedronsSet.this.setSize(0.0d);
            PolyhedronsSet.this.setBarycenter((Point) new Vector3D(0.0d, 0.0d, 0.0d));
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
        private void addContribution(SubHyperplane<Euclidean3D> facet, boolean reversed) {
            Region<Euclidean2D> polygon = ((SubPlane) facet).getRemainingRegion();
            double area = polygon.getSize();
            if (Double.isInfinite(area)) {
                PolyhedronsSet.this.setSize(Double.POSITIVE_INFINITY);
                PolyhedronsSet.this.setBarycenter((Point) Vector3D.NaN);
                return;
            }
            Plane plane = (Plane) facet.getHyperplane();
            Vector3D facetB = plane.toSpace((Point<Euclidean2D>) polygon.getBarycenter());
            double scaled = area * facetB.dotProduct(plane.getNormal());
            if (reversed) {
                scaled = -scaled;
            }
            PolyhedronsSet.this.setSize(PolyhedronsSet.this.getSize() + scaled);
            PolyhedronsSet.this.setBarycenter((Point) new Vector3D(1.0d, (Vector3D) PolyhedronsSet.this.getBarycenter(), scaled, facetB));
        }
    }

    public SubHyperplane<Euclidean3D> firstIntersection(Vector3D point, Line line) {
        return recurseFirstIntersection(getTree(true), point, line);
    }

    private SubHyperplane<Euclidean3D> recurseFirstIntersection(BSPTree<Euclidean3D> node, Vector3D point, Line line) {
        BSPTree<Euclidean3D> near;
        BSPTree<Euclidean3D> far;
        Vector3D hit3D;
        SubHyperplane<Euclidean3D> facet;
        SubHyperplane<Euclidean3D> facet2;
        SubHyperplane<S> cut = node.getCut();
        if (cut == 0) {
            return null;
        }
        BSPTree<Euclidean3D> minus = node.getMinus();
        BSPTree<Euclidean3D> plus = node.getPlus();
        Plane plane = (Plane) cut.getHyperplane();
        double offset = plane.getOffset((Point<Euclidean3D>) point);
        boolean in = FastMath.abs(offset) < getTolerance();
        if (offset < 0.0d) {
            near = minus;
            far = plus;
        } else {
            near = plus;
            far = minus;
        }
        if (in && (facet2 = boundaryFacet(point, node)) != null) {
            return facet2;
        }
        SubHyperplane<Euclidean3D> crossed = recurseFirstIntersection(near, point, line);
        if (crossed != null) {
            return crossed;
        }
        if (!in && (hit3D = plane.intersection(line)) != null && line.getAbscissa(hit3D) > line.getAbscissa(point) && (facet = boundaryFacet(hit3D, node)) != null) {
            return facet;
        }
        return recurseFirstIntersection(far, point, line);
    }

    private SubHyperplane<Euclidean3D> boundaryFacet(Vector3D point, BSPTree<Euclidean3D> node) {
        Vector2D point2D = ((Plane) node.getCut().getHyperplane()).toSubSpace((Point<Euclidean3D>) point);
        BoundaryAttribute<Euclidean3D> attribute = (BoundaryAttribute) node.getAttribute();
        if (attribute.getPlusOutside() != null && ((SubPlane) attribute.getPlusOutside()).getRemainingRegion().checkPoint(point2D) == Region.Location.INSIDE) {
            return attribute.getPlusOutside();
        }
        if (attribute.getPlusInside() != null && ((SubPlane) attribute.getPlusInside()).getRemainingRegion().checkPoint(point2D) == Region.Location.INSIDE) {
            return attribute.getPlusInside();
        }
        return null;
    }

    public PolyhedronsSet rotate(Vector3D center, Rotation rotation) {
        return (PolyhedronsSet) applyTransform(new RotationTransform(center, rotation));
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/euclidean/threed/PolyhedronsSet$RotationTransform.class */
    private static class RotationTransform implements Transform<Euclidean3D, Euclidean2D> {
        private Vector3D center;
        private Rotation rotation;
        private Plane cachedOriginal;
        private Transform<Euclidean2D, Euclidean1D> cachedTransform;

        @Override // org.apache.commons.math3.geometry.partitioning.Transform
        public /* bridge */ /* synthetic */ Hyperplane apply(Hyperplane hyperplane) {
            return apply((Hyperplane<Euclidean3D>) hyperplane);
        }

        @Override // org.apache.commons.math3.geometry.partitioning.Transform
        public /* bridge */ /* synthetic */ Point apply(Point point) {
            return apply((Point<Euclidean3D>) point);
        }

        RotationTransform(Vector3D center, Rotation rotation) {
            this.center = center;
            this.rotation = rotation;
        }

        @Override // org.apache.commons.math3.geometry.partitioning.Transform
        public Vector3D apply(Point<Euclidean3D> point) {
            Vector3D delta = ((Vector3D) point).subtract((Vector<Euclidean3D>) this.center);
            return new Vector3D(1.0d, this.center, 1.0d, this.rotation.applyTo(delta));
        }

        @Override // org.apache.commons.math3.geometry.partitioning.Transform
        public Plane apply(Hyperplane<Euclidean3D> hyperplane) {
            return ((Plane) hyperplane).rotate(this.center, this.rotation);
        }

        @Override // org.apache.commons.math3.geometry.partitioning.Transform
        public SubHyperplane<Euclidean2D> apply(SubHyperplane<Euclidean2D> sub, Hyperplane<Euclidean3D> original, Hyperplane<Euclidean3D> transformed) {
            if (original != this.cachedOriginal) {
                Plane oPlane = (Plane) original;
                Plane tPlane = (Plane) transformed;
                Vector3D p00 = oPlane.getOrigin();
                Vector3D p10 = oPlane.toSpace((Point<Euclidean2D>) new Vector2D(1.0d, 0.0d));
                Vector3D p01 = oPlane.toSpace((Point<Euclidean2D>) new Vector2D(0.0d, 1.0d));
                Vector2D tP00 = tPlane.toSubSpace((Point<Euclidean3D>) apply((Point<Euclidean3D>) p00));
                Vector2D tP10 = tPlane.toSubSpace((Point<Euclidean3D>) apply((Point<Euclidean3D>) p10));
                Vector2D tP01 = tPlane.toSubSpace((Point<Euclidean3D>) apply((Point<Euclidean3D>) p01));
                this.cachedOriginal = (Plane) original;
                this.cachedTransform = org.apache.commons.math3.geometry.euclidean.twod.Line.getTransform(tP10.getX() - tP00.getX(), tP10.getY() - tP00.getY(), tP01.getX() - tP00.getX(), tP01.getY() - tP00.getY(), tP00.getX(), tP00.getY());
            }
            return ((org.apache.commons.math3.geometry.euclidean.twod.SubLine) sub).applyTransform(this.cachedTransform);
        }
    }

    public PolyhedronsSet translate(Vector3D translation) {
        return (PolyhedronsSet) applyTransform(new TranslationTransform(translation));
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/euclidean/threed/PolyhedronsSet$TranslationTransform.class */
    private static class TranslationTransform implements Transform<Euclidean3D, Euclidean2D> {
        private Vector3D translation;
        private Plane cachedOriginal;
        private Transform<Euclidean2D, Euclidean1D> cachedTransform;

        @Override // org.apache.commons.math3.geometry.partitioning.Transform
        public /* bridge */ /* synthetic */ Hyperplane apply(Hyperplane hyperplane) {
            return apply((Hyperplane<Euclidean3D>) hyperplane);
        }

        @Override // org.apache.commons.math3.geometry.partitioning.Transform
        public /* bridge */ /* synthetic */ Point apply(Point point) {
            return apply((Point<Euclidean3D>) point);
        }

        TranslationTransform(Vector3D translation) {
            this.translation = translation;
        }

        @Override // org.apache.commons.math3.geometry.partitioning.Transform
        public Vector3D apply(Point<Euclidean3D> point) {
            return new Vector3D(1.0d, (Vector3D) point, 1.0d, this.translation);
        }

        @Override // org.apache.commons.math3.geometry.partitioning.Transform
        public Plane apply(Hyperplane<Euclidean3D> hyperplane) {
            return ((Plane) hyperplane).translate(this.translation);
        }

        @Override // org.apache.commons.math3.geometry.partitioning.Transform
        public SubHyperplane<Euclidean2D> apply(SubHyperplane<Euclidean2D> sub, Hyperplane<Euclidean3D> original, Hyperplane<Euclidean3D> transformed) {
            if (original != this.cachedOriginal) {
                Plane oPlane = (Plane) original;
                Plane tPlane = (Plane) transformed;
                Vector2D shift = tPlane.toSubSpace((Point<Euclidean3D>) apply((Point<Euclidean3D>) oPlane.getOrigin()));
                this.cachedOriginal = (Plane) original;
                this.cachedTransform = org.apache.commons.math3.geometry.euclidean.twod.Line.getTransform(1.0d, 0.0d, 0.0d, 1.0d, shift.getX(), shift.getY());
            }
            return ((org.apache.commons.math3.geometry.euclidean.twod.SubLine) sub).applyTransform(this.cachedTransform);
        }
    }
}
