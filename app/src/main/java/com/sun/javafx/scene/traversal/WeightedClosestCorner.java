package com.sun.javafx.scene.traversal;

import java.util.List;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/traversal/WeightedClosestCorner.class */
public class WeightedClosestCorner implements Algorithm {
    WeightedClosestCorner() {
    }

    private boolean isOnAxis(Direction dir, Bounds cur, Bounds tgt) {
        double cmin;
        double cmax;
        double tmin;
        double tmax;
        if (dir == Direction.UP || dir == Direction.DOWN) {
            cmin = cur.getMinX();
            cmax = cur.getMaxX();
            tmin = tgt.getMinX();
            tmax = tgt.getMaxX();
        } else {
            cmin = cur.getMinY();
            cmax = cur.getMaxY();
            tmin = tgt.getMinY();
            tmax = tgt.getMaxY();
        }
        return tmin <= cmax && tmax >= cmin;
    }

    private double outDistance(Direction dir, Bounds cur, Bounds tgt) {
        double distance;
        if (dir == Direction.UP) {
            distance = cur.getMinY() - tgt.getMaxY();
        } else if (dir == Direction.DOWN) {
            distance = tgt.getMinY() - cur.getMaxY();
        } else if (dir == Direction.LEFT) {
            distance = cur.getMinX() - tgt.getMaxX();
        } else {
            distance = tgt.getMinX() - cur.getMaxX();
        }
        return distance;
    }

    private double centerSideDistance(Direction dir, Bounds cur, Bounds tgt) {
        double cc;
        double tc;
        if (dir == Direction.UP || dir == Direction.DOWN) {
            cc = cur.getMinX() + (cur.getWidth() / 2.0d);
            tc = tgt.getMinX() + (tgt.getWidth() / 2.0d);
        } else {
            cc = cur.getMinY() + (cur.getHeight() / 2.0d);
            tc = tgt.getMinY() + (tgt.getHeight() / 2.0d);
        }
        return Math.abs(tc - cc);
    }

    private double cornerSideDistance(Direction dir, Bounds cur, Bounds tgt) {
        double distance;
        if (dir == Direction.UP || dir == Direction.DOWN) {
            if (tgt.getMinX() > cur.getMaxX()) {
                distance = tgt.getMinX() - cur.getMaxX();
            } else {
                distance = cur.getMinX() - tgt.getMaxX();
            }
        } else if (tgt.getMinY() > cur.getMaxY()) {
            distance = tgt.getMinY() - cur.getMaxY();
        } else {
            distance = cur.getMinY() - tgt.getMaxY();
        }
        return distance;
    }

    @Override // com.sun.javafx.scene.traversal.Algorithm
    public Node select(Node node, Direction dir, TraversalContext context) {
        Node newNode = null;
        List<Node> nodes = context.getAllTargetNodes();
        int target = traverse(context.getSceneLayoutBounds(node), dir, nodes, context);
        if (target != -1) {
            newNode = nodes.get(target);
        }
        return newNode;
    }

    @Override // com.sun.javafx.scene.traversal.Algorithm
    public Node selectFirst(TraversalContext context) {
        List<Node> nodes = context.getAllTargetNodes();
        Point2D zeroZero = new Point2D(0.0d, 0.0d);
        if (nodes.size() > 0) {
            Node nearestNode = nodes.get(0);
            double nearestDistance = zeroZero.distance(context.getSceneLayoutBounds(nodes.get(0)).getMinX(), context.getSceneLayoutBounds(nodes.get(0)).getMinY());
            for (int nodeIndex = 1; nodeIndex < nodes.size(); nodeIndex++) {
                double distance = zeroZero.distance(context.getSceneLayoutBounds(nodes.get(nodeIndex)).getMinX(), context.getSceneLayoutBounds(nodes.get(nodeIndex)).getMinY());
                if (nearestDistance > distance) {
                    nearestDistance = distance;
                    nearestNode = nodes.get(nodeIndex);
                }
            }
            return nearestNode;
        }
        return null;
    }

    @Override // com.sun.javafx.scene.traversal.Algorithm
    public Node selectLast(TraversalContext context) {
        return null;
    }

    public int traverse(Bounds origin, Direction dir, List<Node> targets, TraversalContext context) {
        int target;
        if (dir == Direction.NEXT || dir == Direction.NEXT_IN_LINE || dir == Direction.PREVIOUS) {
            target = trav1D(origin, dir, targets, context);
        } else {
            target = trav2D(origin, dir, targets, context);
        }
        return target;
    }

    private int trav2D(Bounds origin, Direction dir, List<Node> targets, TraversalContext context) {
        double metric;
        Bounds bestBounds = null;
        double bestMetric = 0.0d;
        int bestIndex = -1;
        for (int i2 = 0; i2 < targets.size(); i2++) {
            Bounds targetBounds = context.getSceneLayoutBounds(targets.get(i2));
            double outd = outDistance(dir, origin, targetBounds);
            if (isOnAxis(dir, origin, targetBounds)) {
                metric = outd + (centerSideDistance(dir, origin, targetBounds) / 100.0d);
            } else {
                double cosd = cornerSideDistance(dir, origin, targetBounds);
                metric = 100000.0d + (outd * outd) + (9.0d * cosd * cosd);
            }
            if (outd >= 0.0d && (bestBounds == null || metric < bestMetric)) {
                bestBounds = targetBounds;
                bestMetric = metric;
                bestIndex = i2;
            }
        }
        return bestIndex;
    }

    private int compare1D(Bounds a2, Bounds b2) {
        int res = 0;
        double metric1a = (a2.getMinY() + a2.getMaxY()) / 2.0d;
        double metric1b = (b2.getMinY() + b2.getMaxY()) / 2.0d;
        double metric2a = (a2.getMinX() + a2.getMaxX()) / 2.0d;
        double metric2b = (b2.getMinX() + b2.getMaxX()) / 2.0d;
        double metric3a = a2.hashCode();
        double metric3b = b2.hashCode();
        if (metric1a < metric1b) {
            res = -1;
        } else if (metric1a > metric1b) {
            res = 1;
        } else if (metric2a < metric2b) {
            res = -1;
        } else if (metric2a > metric2b) {
            res = 1;
        } else if (metric3a < metric3b) {
            res = -1;
        } else if (metric3a > metric3b) {
            res = 1;
        }
        return res;
    }

    private int compare1D(Bounds a2, Bounds b2, Direction dir) {
        return dir != Direction.PREVIOUS ? -compare1D(a2, b2) : compare1D(a2, b2);
    }

    private int trav1D(Bounds origin, Direction dir, List<Node> targets, TraversalContext context) {
        int bestSoFar = -1;
        int leastSoFar = -1;
        for (int i2 = 0; i2 < targets.size(); i2++) {
            if (leastSoFar == -1 || compare1D(context.getSceneLayoutBounds(targets.get(i2)), context.getSceneLayoutBounds(targets.get(leastSoFar)), dir) < 0) {
                leastSoFar = i2;
            }
            if (compare1D(context.getSceneLayoutBounds(targets.get(i2)), origin, dir) >= 0 && (bestSoFar == -1 || compare1D(context.getSceneLayoutBounds(targets.get(i2)), context.getSceneLayoutBounds(targets.get(bestSoFar)), dir) < 0)) {
                bestSoFar = i2;
            }
        }
        return bestSoFar == -1 ? leastSoFar : bestSoFar;
    }
}
