package com.sun.javafx.scene.traversal;

import java.util.List;
import javafx.geometry.Bounds;
import javafx.scene.Node;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/traversal/ContainerTabOrder.class */
public class ContainerTabOrder implements Algorithm {
    ContainerTabOrder() {
    }

    @Override // com.sun.javafx.scene.traversal.Algorithm
    public Node select(Node node, Direction dir, TraversalContext context) {
        switch (dir) {
            case NEXT:
            case NEXT_IN_LINE:
                return TabOrderHelper.findNextFocusablePeer(node, context.getRoot(), dir == Direction.NEXT);
            case PREVIOUS:
                return TabOrderHelper.findPreviousFocusablePeer(node, context.getRoot());
            case UP:
            case DOWN:
            case LEFT:
            case RIGHT:
                List<Node> nodes = context.getAllTargetNodes();
                int target = trav2D(context.getSceneLayoutBounds(node), dir, nodes, context);
                if (target != -1) {
                    return nodes.get(target);
                }
                return null;
            default:
                return null;
        }
    }

    @Override // com.sun.javafx.scene.traversal.Algorithm
    public Node selectFirst(TraversalContext context) {
        return TabOrderHelper.getFirstTargetNode(context.getRoot());
    }

    @Override // com.sun.javafx.scene.traversal.Algorithm
    public Node selectLast(TraversalContext context) {
        return TabOrderHelper.getLastTargetNode(context.getRoot());
    }

    private int trav2D(Bounds origin, Direction dir, List<Node> peers, TraversalContext context) {
        double metric;
        Bounds bestBounds = null;
        double bestMetric = 0.0d;
        int bestIndex = -1;
        for (int i2 = 0; i2 < peers.size(); i2++) {
            Bounds targetBounds = context.getSceneLayoutBounds(peers.get(i2));
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
}
