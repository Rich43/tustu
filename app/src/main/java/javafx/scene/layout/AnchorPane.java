package javafx.scene.layout;

import java.util.List;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;

/* loaded from: jfxrt.jar:javafx/scene/layout/AnchorPane.class */
public class AnchorPane extends Pane {
    private static final String TOP_ANCHOR = "pane-top-anchor";
    private static final String LEFT_ANCHOR = "pane-left-anchor";
    private static final String BOTTOM_ANCHOR = "pane-bottom-anchor";
    private static final String RIGHT_ANCHOR = "pane-right-anchor";

    public static void setTopAnchor(Node child, Double value) {
        setConstraint(child, TOP_ANCHOR, value);
    }

    public static Double getTopAnchor(Node child) {
        return (Double) getConstraint(child, TOP_ANCHOR);
    }

    public static void setLeftAnchor(Node child, Double value) {
        setConstraint(child, LEFT_ANCHOR, value);
    }

    public static Double getLeftAnchor(Node child) {
        return (Double) getConstraint(child, LEFT_ANCHOR);
    }

    public static void setBottomAnchor(Node child, Double value) {
        setConstraint(child, BOTTOM_ANCHOR, value);
    }

    public static Double getBottomAnchor(Node child) {
        return (Double) getConstraint(child, BOTTOM_ANCHOR);
    }

    public static void setRightAnchor(Node child, Double value) {
        setConstraint(child, RIGHT_ANCHOR, value);
    }

    public static Double getRightAnchor(Node child) {
        return (Double) getConstraint(child, RIGHT_ANCHOR);
    }

    public static void clearConstraints(Node child) {
        setTopAnchor(child, null);
        setRightAnchor(child, null);
        setBottomAnchor(child, null);
        setLeftAnchor(child, null);
    }

    public AnchorPane() {
    }

    public AnchorPane(Node... children) {
        getChildren().addAll(children);
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computeMinWidth(double height) {
        return computeWidth(true, height);
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computeMinHeight(double width) {
        return computeHeight(true, width);
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefWidth(double height) {
        return computeWidth(false, height);
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefHeight(double width) {
        return computeHeight(false, width);
    }

    private double computeWidth(boolean minimum, double height) {
        double minX;
        double max = 0.0d;
        double contentHeight = height != -1.0d ? (height - getInsets().getTop()) - getInsets().getBottom() : -1.0d;
        List<Node> children = getManagedChildren();
        for (Node child : children) {
            Double leftAnchor = getLeftAnchor(child);
            Double rightAnchor = getRightAnchor(child);
            if (leftAnchor != null) {
                minX = leftAnchor.doubleValue();
            } else {
                minX = rightAnchor != null ? 0.0d : child.getLayoutBounds().getMinX() + child.getLayoutX();
            }
            double left = minX;
            double right = rightAnchor != null ? rightAnchor.doubleValue() : 0.0d;
            double childHeight = -1.0d;
            if (child.getContentBias() == Orientation.VERTICAL && contentHeight != -1.0d) {
                childHeight = computeChildHeight(child, getTopAnchor(child), getBottomAnchor(child), contentHeight, -1.0d);
            }
            max = Math.max(max, left + ((!minimum || leftAnchor == null || rightAnchor == null) ? computeChildPrefAreaWidth(child, -1.0d, null, childHeight, false) : child.minWidth(childHeight)) + right);
        }
        Insets insets = getInsets();
        return insets.getLeft() + max + insets.getRight();
    }

    private double computeHeight(boolean minimum, double width) {
        double minY;
        double max = 0.0d;
        double contentWidth = width != -1.0d ? (width - getInsets().getLeft()) - getInsets().getRight() : -1.0d;
        List<Node> children = getManagedChildren();
        for (Node child : children) {
            Double topAnchor = getTopAnchor(child);
            Double bottomAnchor = getBottomAnchor(child);
            if (topAnchor != null) {
                minY = topAnchor.doubleValue();
            } else {
                minY = bottomAnchor != null ? 0.0d : child.getLayoutBounds().getMinY() + child.getLayoutY();
            }
            double top = minY;
            double bottom = bottomAnchor != null ? bottomAnchor.doubleValue() : 0.0d;
            double childWidth = -1.0d;
            if (child.getContentBias() == Orientation.HORIZONTAL && contentWidth != -1.0d) {
                childWidth = computeChildWidth(child, getLeftAnchor(child), getRightAnchor(child), contentWidth, -1.0d);
            }
            max = Math.max(max, top + ((!minimum || topAnchor == null || bottomAnchor == null) ? computeChildPrefAreaHeight(child, -1.0d, null, childWidth) : child.minHeight(childWidth)) + bottom);
        }
        Insets insets = getInsets();
        return insets.getTop() + max + insets.getBottom();
    }

    private double computeChildWidth(Node child, Double leftAnchor, Double rightAnchor, double areaWidth, double height) {
        if (leftAnchor != null && rightAnchor != null && child.isResizable()) {
            Insets insets = getInsets();
            return (((areaWidth - insets.getLeft()) - insets.getRight()) - leftAnchor.doubleValue()) - rightAnchor.doubleValue();
        }
        return computeChildPrefAreaWidth(child, -1.0d, Insets.EMPTY, height, true);
    }

    private double computeChildHeight(Node child, Double topAnchor, Double bottomAnchor, double areaHeight, double width) {
        if (topAnchor != null && bottomAnchor != null && child.isResizable()) {
            Insets insets = getInsets();
            return (((areaHeight - insets.getTop()) - insets.getBottom()) - topAnchor.doubleValue()) - bottomAnchor.doubleValue();
        }
        return computeChildPrefAreaHeight(child, -1.0d, Insets.EMPTY, width);
    }

    @Override // javafx.scene.Parent
    protected void layoutChildren() {
        double w2;
        double h2;
        Insets insets = getInsets();
        List<Node> children = getManagedChildren();
        for (Node child : children) {
            Double topAnchor = getTopAnchor(child);
            Double bottomAnchor = getBottomAnchor(child);
            Double leftAnchor = getLeftAnchor(child);
            Double rightAnchor = getRightAnchor(child);
            Bounds childLayoutBounds = child.getLayoutBounds();
            Orientation bias = child.getContentBias();
            double x2 = child.getLayoutX() + childLayoutBounds.getMinX();
            double y2 = child.getLayoutY() + childLayoutBounds.getMinY();
            if (bias == Orientation.VERTICAL) {
                h2 = computeChildHeight(child, topAnchor, bottomAnchor, getHeight(), -1.0d);
                w2 = computeChildWidth(child, leftAnchor, rightAnchor, getWidth(), h2);
            } else if (bias == Orientation.HORIZONTAL) {
                w2 = computeChildWidth(child, leftAnchor, rightAnchor, getWidth(), -1.0d);
                h2 = computeChildHeight(child, topAnchor, bottomAnchor, getHeight(), w2);
            } else {
                w2 = computeChildWidth(child, leftAnchor, rightAnchor, getWidth(), -1.0d);
                h2 = computeChildHeight(child, topAnchor, bottomAnchor, getHeight(), -1.0d);
            }
            if (leftAnchor != null) {
                x2 = insets.getLeft() + leftAnchor.doubleValue();
            } else if (rightAnchor != null) {
                x2 = ((getWidth() - insets.getRight()) - rightAnchor.doubleValue()) - w2;
            }
            if (topAnchor != null) {
                y2 = insets.getTop() + topAnchor.doubleValue();
            } else if (bottomAnchor != null) {
                y2 = ((getHeight() - insets.getBottom()) - bottomAnchor.doubleValue()) - h2;
            }
            child.resizeRelocate(x2, y2, w2, h2);
        }
    }
}
