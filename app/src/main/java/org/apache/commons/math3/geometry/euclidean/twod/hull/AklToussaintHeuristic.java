package org.apache.commons.math3.geometry.euclidean.twod.hull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/euclidean/twod/hull/AklToussaintHeuristic.class */
public final class AklToussaintHeuristic {
    private AklToussaintHeuristic() {
    }

    public static Collection<Vector2D> reducePoints(Collection<Vector2D> points) {
        int size = 0;
        Vector2D minX = null;
        Vector2D maxX = null;
        Vector2D minY = null;
        Vector2D maxY = null;
        for (Vector2D p2 : points) {
            if (minX == null || p2.getX() < minX.getX()) {
                minX = p2;
            }
            if (maxX == null || p2.getX() > maxX.getX()) {
                maxX = p2;
            }
            if (minY == null || p2.getY() < minY.getY()) {
                minY = p2;
            }
            if (maxY == null || p2.getY() > maxY.getY()) {
                maxY = p2;
            }
            size++;
        }
        if (size < 4) {
            return points;
        }
        List<Vector2D> quadrilateral = buildQuadrilateral(minY, maxX, maxY, minX);
        if (quadrilateral.size() < 3) {
            return points;
        }
        List<Vector2D> reducedPoints = new ArrayList<>(quadrilateral);
        for (Vector2D p3 : points) {
            if (!insideQuadrilateral(p3, quadrilateral)) {
                reducedPoints.add(p3);
            }
        }
        return reducedPoints;
    }

    private static List<Vector2D> buildQuadrilateral(Vector2D... points) {
        List<Vector2D> quadrilateral = new ArrayList<>();
        for (Vector2D p2 : points) {
            if (!quadrilateral.contains(p2)) {
                quadrilateral.add(p2);
            }
        }
        return quadrilateral;
    }

    private static boolean insideQuadrilateral(Vector2D point, List<Vector2D> quadrilateralPoints) {
        Vector2D p1 = quadrilateralPoints.get(0);
        Vector2D p2 = quadrilateralPoints.get(1);
        if (point.equals(p1) || point.equals(p2)) {
            return true;
        }
        double last = point.crossProduct(p1, p2);
        int size = quadrilateralPoints.size();
        for (int i2 = 1; i2 < size; i2++) {
            Vector2D p12 = p2;
            p2 = quadrilateralPoints.get(i2 + 1 == size ? 0 : i2 + 1);
            if (point.equals(p12) || point.equals(p2)) {
                return true;
            }
            if (last * point.crossProduct(p12, p2) < 0.0d) {
                return false;
            }
        }
        return true;
    }
}
