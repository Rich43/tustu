package com.sun.javafx.scene.traversal;

import java.util.Stack;
import java.util.function.Function;
import javafx.geometry.Bounds;
import javafx.scene.Node;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/traversal/Hueristic2D.class */
public class Hueristic2D implements Algorithm {
    protected Node cacheStartTraversalNode = null;
    protected Direction cacheStartTraversalDirection = null;
    protected boolean reverseDirection = false;
    protected Node cacheLastTraversalNode = null;
    protected Stack<Node> traversalNodeStack = new Stack<>();
    private static final Function<Bounds, Double> BOUNDS_TOP_SIDE = t2 -> {
        return Double.valueOf(t2.getMinY());
    };
    private static final Function<Bounds, Double> BOUNDS_BOTTOM_SIDE = t2 -> {
        return Double.valueOf(t2.getMaxY());
    };
    private static final Function<Bounds, Double> BOUNDS_LEFT_SIDE = t2 -> {
        return Double.valueOf(t2.getMinX());
    };
    private static final Function<Bounds, Double> BOUNDS_RIGHT_SIDE = t2 -> {
        return Double.valueOf(t2.getMaxX());
    };

    Hueristic2D() {
    }

    @Override // com.sun.javafx.scene.traversal.Algorithm
    public Node select(Node node, Direction dir, TraversalContext context) {
        Node newNode = null;
        cacheTraversal(node, dir);
        if (Direction.NEXT.equals(dir) || Direction.NEXT_IN_LINE.equals(dir)) {
            newNode = TabOrderHelper.findNextFocusablePeer(node, context.getRoot(), dir == Direction.NEXT);
        } else if (Direction.PREVIOUS.equals(dir)) {
            newNode = TabOrderHelper.findPreviousFocusablePeer(node, context.getRoot());
        } else if (Direction.UP.equals(dir) || Direction.DOWN.equals(dir) || Direction.LEFT.equals(dir) || Direction.RIGHT.equals(dir)) {
            if (this.reverseDirection && !this.traversalNodeStack.empty()) {
                if (!this.traversalNodeStack.peek().isFocusTraversable()) {
                    this.traversalNodeStack.clear();
                } else {
                    newNode = this.traversalNodeStack.pop();
                }
            }
            if (newNode == null) {
                Bounds currentB = node.localToScene(node.getLayoutBounds());
                if (this.cacheStartTraversalNode != null) {
                    Bounds cachedB = this.cacheStartTraversalNode.localToScene(this.cacheStartTraversalNode.getLayoutBounds());
                    switch (dir) {
                        case UP:
                        case DOWN:
                            newNode = getNearestNodeUpOrDown(currentB, cachedB, context, dir);
                            break;
                        case LEFT:
                        case RIGHT:
                            newNode = getNearestNodeLeftOrRight(currentB, cachedB, context, dir);
                            break;
                    }
                }
            }
        }
        if (newNode != null) {
            this.cacheLastTraversalNode = newNode;
            if (!this.reverseDirection) {
                this.traversalNodeStack.push(node);
            }
        }
        return newNode;
    }

    @Override // com.sun.javafx.scene.traversal.Algorithm
    public Node selectFirst(TraversalContext context) {
        return TabOrderHelper.getFirstTargetNode(context.getRoot());
    }

    @Override // com.sun.javafx.scene.traversal.Algorithm
    public Node selectLast(TraversalContext context) {
        return TabOrderHelper.getLastTargetNode(context.getRoot());
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

    private void cacheTraversal(Node node, Direction dir) {
        if (!this.traversalNodeStack.empty() && node != this.cacheLastTraversalNode) {
            this.traversalNodeStack.clear();
        }
        if (dir == Direction.NEXT || dir == Direction.PREVIOUS) {
            this.traversalNodeStack.clear();
            this.reverseDirection = false;
            return;
        }
        if (this.cacheStartTraversalNode == null || dir != this.cacheStartTraversalDirection) {
            if ((dir == Direction.UP && this.cacheStartTraversalDirection == Direction.DOWN) || ((dir == Direction.DOWN && this.cacheStartTraversalDirection == Direction.UP) || ((dir == Direction.LEFT && this.cacheStartTraversalDirection == Direction.RIGHT) || (dir == Direction.RIGHT && this.cacheStartTraversalDirection == Direction.LEFT && !this.traversalNodeStack.empty())))) {
                this.reverseDirection = true;
                return;
            }
            this.cacheStartTraversalNode = node;
            this.cacheStartTraversalDirection = dir;
            this.reverseDirection = false;
            this.traversalNodeStack.clear();
            return;
        }
        this.reverseDirection = false;
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x0182  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected javafx.scene.Node getNearestNodeUpOrDown(javafx.geometry.Bounds r12, javafx.geometry.Bounds r13, com.sun.javafx.scene.traversal.TraversalContext r14, com.sun.javafx.scene.traversal.Direction r15) {
        /*
            Method dump skipped, instructions count: 2466
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.scene.traversal.Hueristic2D.getNearestNodeUpOrDown(javafx.geometry.Bounds, javafx.geometry.Bounds, com.sun.javafx.scene.traversal.TraversalContext, com.sun.javafx.scene.traversal.Direction):javafx.scene.Node");
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x0182  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected javafx.scene.Node getNearestNodeLeftOrRight(javafx.geometry.Bounds r12, javafx.geometry.Bounds r13, com.sun.javafx.scene.traversal.TraversalContext r14, com.sun.javafx.scene.traversal.Direction r15) {
        /*
            Method dump skipped, instructions count: 2387
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.scene.traversal.Hueristic2D.getNearestNodeLeftOrRight(javafx.geometry.Bounds, javafx.geometry.Bounds, com.sun.javafx.scene.traversal.TraversalContext, com.sun.javafx.scene.traversal.Direction):javafx.scene.Node");
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/traversal/Hueristic2D$TargetNode.class */
    static final class TargetNode {
        Node node = null;
        Bounds bounds = null;
        double biased2DMetric = Double.MAX_VALUE;
        double current2DMetric = Double.MAX_VALUE;
        double leftCornerDistance = Double.MAX_VALUE;
        double rightCornerDistance = Double.MAX_VALUE;
        double topCornerDistance = Double.MAX_VALUE;
        double bottomCornerDistance = Double.MAX_VALUE;
        double shortestDistance = Double.MAX_VALUE;
        double biasShortestDistance = Double.MAX_VALUE;
        double averageDistance = Double.MAX_VALUE;
        double originLeftCornerDistance = Double.MAX_VALUE;
        double originTopCornerDistance = Double.MAX_VALUE;

        TargetNode() {
        }

        void copy(TargetNode source) {
            this.node = source.node;
            this.bounds = source.bounds;
            this.biased2DMetric = source.biased2DMetric;
            this.current2DMetric = source.current2DMetric;
            this.leftCornerDistance = source.leftCornerDistance;
            this.rightCornerDistance = source.rightCornerDistance;
            this.shortestDistance = source.shortestDistance;
            this.biasShortestDistance = source.biasShortestDistance;
            this.averageDistance = source.averageDistance;
            this.topCornerDistance = source.topCornerDistance;
            this.bottomCornerDistance = source.bottomCornerDistance;
            this.originLeftCornerDistance = source.originLeftCornerDistance;
            this.originTopCornerDistance = source.originTopCornerDistance;
        }
    }

    public static double findMin(double... values) {
        double minValue = Double.MAX_VALUE;
        for (int i2 = 0; i2 < values.length; i2++) {
            minValue = minValue < values[i2] ? minValue : values[i2];
        }
        return minValue;
    }
}
