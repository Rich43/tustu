package javafx.scene.layout;

import com.sun.javafx.geom.Vec2d;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.collections.ListChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javax.swing.JSplitPane;

/* loaded from: jfxrt.jar:javafx/scene/layout/BorderPane.class */
public class BorderPane extends Pane {
    private static final String MARGIN = "borderpane-margin";
    private static final String ALIGNMENT = "borderpane-alignment";
    private ObjectProperty<Node> center;
    private ObjectProperty<Node> top;
    private ObjectProperty<Node> bottom;
    private ObjectProperty<Node> left;
    private ObjectProperty<Node> right;

    public static void setAlignment(Node child, Pos value) {
        setConstraint(child, ALIGNMENT, value);
    }

    public static Pos getAlignment(Node child) {
        return (Pos) getConstraint(child, ALIGNMENT);
    }

    public static void setMargin(Node child, Insets value) {
        setConstraint(child, MARGIN, value);
    }

    public static Insets getMargin(Node child) {
        return (Insets) getConstraint(child, MARGIN);
    }

    private static Insets getNodeMargin(Node child) {
        Insets margin = getMargin(child);
        return margin != null ? margin : Insets.EMPTY;
    }

    public static void clearConstraints(Node child) {
        setAlignment(child, null);
        setMargin(child, null);
    }

    public BorderPane() {
    }

    public BorderPane(Node center) {
        setCenter(center);
    }

    public BorderPane(Node center, Node top, Node right, Node bottom, Node left) {
        setCenter(center);
        setTop(top);
        setRight(right);
        setBottom(bottom);
        setLeft(left);
    }

    public final ObjectProperty<Node> centerProperty() {
        if (this.center == null) {
            this.center = new BorderPositionProperty("center");
        }
        return this.center;
    }

    public final void setCenter(Node value) {
        centerProperty().set(value);
    }

    public final Node getCenter() {
        if (this.center == null) {
            return null;
        }
        return this.center.get();
    }

    public final ObjectProperty<Node> topProperty() {
        if (this.top == null) {
            this.top = new BorderPositionProperty(JSplitPane.TOP);
        }
        return this.top;
    }

    public final void setTop(Node value) {
        topProperty().set(value);
    }

    public final Node getTop() {
        if (this.top == null) {
            return null;
        }
        return this.top.get();
    }

    public final ObjectProperty<Node> bottomProperty() {
        if (this.bottom == null) {
            this.bottom = new BorderPositionProperty(JSplitPane.BOTTOM);
        }
        return this.bottom;
    }

    public final void setBottom(Node value) {
        bottomProperty().set(value);
    }

    public final Node getBottom() {
        if (this.bottom == null) {
            return null;
        }
        return this.bottom.get();
    }

    public final ObjectProperty<Node> leftProperty() {
        if (this.left == null) {
            this.left = new BorderPositionProperty(JSplitPane.LEFT);
        }
        return this.left;
    }

    public final void setLeft(Node value) {
        leftProperty().set(value);
    }

    public final Node getLeft() {
        if (this.left == null) {
            return null;
        }
        return this.left.get();
    }

    public final ObjectProperty<Node> rightProperty() {
        if (this.right == null) {
            this.right = new BorderPositionProperty(JSplitPane.RIGHT);
        }
        return this.right;
    }

    public final void setRight(Node value) {
        rightProperty().set(value);
    }

    public final Node getRight() {
        if (this.right == null) {
            return null;
        }
        return this.right.get();
    }

    @Override // javafx.scene.Node
    public Orientation getContentBias() {
        Node c2 = getCenter();
        if (c2 != null && c2.isManaged() && c2.getContentBias() != null) {
            return c2.getContentBias();
        }
        Node r2 = getRight();
        if (r2 != null && r2.isManaged() && r2.getContentBias() == Orientation.VERTICAL) {
            return r2.getContentBias();
        }
        Node l2 = getLeft();
        if (l2 != null && l2.isManaged() && l2.getContentBias() == Orientation.VERTICAL) {
            return l2.getContentBias();
        }
        Node b2 = getBottom();
        if (b2 != null && b2.isManaged() && b2.getContentBias() == Orientation.HORIZONTAL) {
            return b2.getContentBias();
        }
        Node t2 = getTop();
        if (t2 != null && t2.isManaged() && t2.getContentBias() == Orientation.HORIZONTAL) {
            return t2.getContentBias();
        }
        return null;
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computeMinWidth(double height) {
        double leftPrefWidth;
        double rightPrefWidth;
        double centerMinWidth;
        double topMinWidth = getAreaWidth(getTop(), -1.0d, true);
        double bottomMinWidth = getAreaWidth(getBottom(), -1.0d, true);
        if (height != -1.0d && (childHasContentBias(getLeft(), Orientation.VERTICAL) || childHasContentBias(getRight(), Orientation.VERTICAL) || childHasContentBias(getCenter(), Orientation.VERTICAL))) {
            double topPrefHeight = getAreaHeight(getTop(), -1.0d, false);
            double bottomPrefHeight = getAreaHeight(getBottom(), -1.0d, false);
            double middleAreaHeight = Math.max(0.0d, (height - topPrefHeight) - bottomPrefHeight);
            leftPrefWidth = getAreaWidth(getLeft(), middleAreaHeight, false);
            rightPrefWidth = getAreaWidth(getRight(), middleAreaHeight, false);
            centerMinWidth = getAreaWidth(getCenter(), middleAreaHeight, true);
        } else {
            leftPrefWidth = getAreaWidth(getLeft(), -1.0d, false);
            rightPrefWidth = getAreaWidth(getRight(), -1.0d, false);
            centerMinWidth = getAreaWidth(getCenter(), -1.0d, true);
        }
        Insets insets = getInsets();
        return insets.getLeft() + Math.max(leftPrefWidth + centerMinWidth + rightPrefWidth, Math.max(topMinWidth, bottomMinWidth)) + insets.getRight();
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computeMinHeight(double width) {
        double centerMinHeight;
        Insets insets = getInsets();
        double topPrefHeight = getAreaHeight(getTop(), width, false);
        double bottomPrefHeight = getAreaHeight(getBottom(), width, false);
        double leftMinHeight = getAreaHeight(getLeft(), -1.0d, true);
        double rightMinHeight = getAreaHeight(getRight(), -1.0d, true);
        if (width != -1.0d && childHasContentBias(getCenter(), Orientation.HORIZONTAL)) {
            double leftPrefWidth = getAreaWidth(getLeft(), -1.0d, false);
            double rightPrefWidth = getAreaWidth(getRight(), -1.0d, false);
            centerMinHeight = getAreaHeight(getCenter(), Math.max(0.0d, (width - leftPrefWidth) - rightPrefWidth), true);
        } else {
            centerMinHeight = getAreaHeight(getCenter(), -1.0d, true);
        }
        double middleAreaMinHeigh = Math.max(centerMinHeight, Math.max(rightMinHeight, leftMinHeight));
        return insets.getTop() + topPrefHeight + middleAreaMinHeigh + bottomPrefHeight + insets.getBottom();
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefWidth(double height) {
        double leftPrefWidth;
        double rightPrefWidth;
        double centerPrefWidth;
        double topPrefWidth = getAreaWidth(getTop(), -1.0d, false);
        double bottomPrefWidth = getAreaWidth(getBottom(), -1.0d, false);
        if (height != -1.0d && (childHasContentBias(getLeft(), Orientation.VERTICAL) || childHasContentBias(getRight(), Orientation.VERTICAL) || childHasContentBias(getCenter(), Orientation.VERTICAL))) {
            double topPrefHeight = getAreaHeight(getTop(), -1.0d, false);
            double bottomPrefHeight = getAreaHeight(getBottom(), -1.0d, false);
            double middleAreaHeight = Math.max(0.0d, (height - topPrefHeight) - bottomPrefHeight);
            leftPrefWidth = getAreaWidth(getLeft(), middleAreaHeight, false);
            rightPrefWidth = getAreaWidth(getRight(), middleAreaHeight, false);
            centerPrefWidth = getAreaWidth(getCenter(), middleAreaHeight, false);
        } else {
            leftPrefWidth = getAreaWidth(getLeft(), -1.0d, false);
            rightPrefWidth = getAreaWidth(getRight(), -1.0d, false);
            centerPrefWidth = getAreaWidth(getCenter(), -1.0d, false);
        }
        Insets insets = getInsets();
        return insets.getLeft() + Math.max(leftPrefWidth + centerPrefWidth + rightPrefWidth, Math.max(topPrefWidth, bottomPrefWidth)) + insets.getRight();
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefHeight(double width) {
        double centerPrefHeight;
        Insets insets = getInsets();
        double topPrefHeight = getAreaHeight(getTop(), width, false);
        double bottomPrefHeight = getAreaHeight(getBottom(), width, false);
        double leftPrefHeight = getAreaHeight(getLeft(), -1.0d, false);
        double rightPrefHeight = getAreaHeight(getRight(), -1.0d, false);
        if (width != -1.0d && childHasContentBias(getCenter(), Orientation.HORIZONTAL)) {
            double leftPrefWidth = getAreaWidth(getLeft(), -1.0d, false);
            double rightPrefWidth = getAreaWidth(getRight(), -1.0d, false);
            centerPrefHeight = getAreaHeight(getCenter(), Math.max(0.0d, (width - leftPrefWidth) - rightPrefWidth), false);
        } else {
            centerPrefHeight = getAreaHeight(getCenter(), -1.0d, false);
        }
        double middleAreaPrefHeigh = Math.max(centerPrefHeight, Math.max(rightPrefHeight, leftPrefHeight));
        return insets.getTop() + topPrefHeight + middleAreaPrefHeigh + bottomPrefHeight + insets.getBottom();
    }

    @Override // javafx.scene.Parent
    protected void layoutChildren() {
        double height;
        double width;
        Insets insets = getInsets();
        double width2 = getWidth();
        double height2 = getHeight();
        Orientation bias = getContentBias();
        if (bias == null) {
            double minWidth = minWidth(-1.0d);
            double minHeight = minHeight(-1.0d);
            width = width2 < minWidth ? minWidth : width2;
            height = height2 < minHeight ? minHeight : height2;
        } else if (bias == Orientation.HORIZONTAL) {
            double minWidth2 = minWidth(-1.0d);
            width = width2 < minWidth2 ? minWidth2 : width2;
            double minHeight2 = minHeight(width);
            height = height2 < minHeight2 ? minHeight2 : height2;
        } else {
            double minHeight3 = minHeight(-1.0d);
            height = height2 < minHeight3 ? minHeight3 : height2;
            double minWidth3 = minWidth(height);
            width = width2 < minWidth3 ? minWidth3 : width2;
        }
        double insideX = insets.getLeft();
        double insideY = insets.getTop();
        double insideWidth = (width - insideX) - insets.getRight();
        double insideHeight = (height - insideY) - insets.getBottom();
        Node c2 = getCenter();
        Node r2 = getRight();
        Node b2 = getBottom();
        Node l2 = getLeft();
        Node t2 = getTop();
        double topHeight = 0.0d;
        if (t2 != null && t2.isManaged()) {
            Insets topMargin = getNodeMargin(t2);
            double adjustedWidth = adjustWidthByMargin(insideWidth, topMargin);
            Vec2d result = boundedNodeSizeWithBias(t2, adjustedWidth, Math.min(snapSize(t2.prefHeight(adjustedWidth)), adjustHeightByMargin(insideHeight, topMargin)), true, true, TEMP_VEC2D);
            double topHeight2 = snapSize(result.f11927y);
            t2.resize(snapSize(result.f11926x), topHeight2);
            topHeight = snapSpace(topMargin.getBottom()) + topHeight2 + snapSpace(topMargin.getTop());
            Pos alignment = getAlignment(t2);
            positionInArea(t2, insideX, insideY, insideWidth, topHeight, 0.0d, topMargin, alignment != null ? alignment.getHpos() : HPos.LEFT, alignment != null ? alignment.getVpos() : VPos.TOP, isSnapToPixel());
        }
        double bottomHeight = 0.0d;
        if (b2 != null && b2.isManaged()) {
            Insets bottomMargin = getNodeMargin(b2);
            double adjustedWidth2 = adjustWidthByMargin(insideWidth, bottomMargin);
            Vec2d result2 = boundedNodeSizeWithBias(b2, adjustedWidth2, Math.min(snapSize(b2.prefHeight(adjustedWidth2)), adjustHeightByMargin(insideHeight - topHeight, bottomMargin)), true, true, TEMP_VEC2D);
            double bottomHeight2 = snapSize(result2.f11927y);
            b2.resize(snapSize(result2.f11926x), bottomHeight2);
            bottomHeight = snapSpace(bottomMargin.getBottom()) + bottomHeight2 + snapSpace(bottomMargin.getTop());
            Pos alignment2 = getAlignment(b2);
            positionInArea(b2, insideX, (insideY + insideHeight) - bottomHeight, insideWidth, bottomHeight, 0.0d, bottomMargin, alignment2 != null ? alignment2.getHpos() : HPos.LEFT, alignment2 != null ? alignment2.getVpos() : VPos.BOTTOM, isSnapToPixel());
        }
        double leftWidth = 0.0d;
        if (l2 != null && l2.isManaged()) {
            Insets leftMargin = getNodeMargin(l2);
            double adjustedWidth3 = adjustWidthByMargin(insideWidth, leftMargin);
            double adjustedHeight = adjustHeightByMargin((insideHeight - topHeight) - bottomHeight, leftMargin);
            Vec2d result3 = boundedNodeSizeWithBias(l2, Math.min(snapSize(l2.prefWidth(adjustedHeight)), adjustedWidth3), adjustedHeight, true, true, TEMP_VEC2D);
            double leftWidth2 = snapSize(result3.f11926x);
            l2.resize(leftWidth2, snapSize(result3.f11927y));
            leftWidth = snapSpace(leftMargin.getLeft()) + leftWidth2 + snapSpace(leftMargin.getRight());
            Pos alignment3 = getAlignment(l2);
            positionInArea(l2, insideX, insideY + topHeight, leftWidth, (insideHeight - topHeight) - bottomHeight, 0.0d, leftMargin, alignment3 != null ? alignment3.getHpos() : HPos.LEFT, alignment3 != null ? alignment3.getVpos() : VPos.TOP, isSnapToPixel());
        }
        double rightWidth = 0.0d;
        if (r2 != null && r2.isManaged()) {
            Insets rightMargin = getNodeMargin(r2);
            double adjustedWidth4 = adjustWidthByMargin(insideWidth - leftWidth, rightMargin);
            double adjustedHeight2 = adjustHeightByMargin((insideHeight - topHeight) - bottomHeight, rightMargin);
            Vec2d result4 = boundedNodeSizeWithBias(r2, Math.min(snapSize(r2.prefWidth(adjustedHeight2)), adjustedWidth4), adjustedHeight2, true, true, TEMP_VEC2D);
            double rightWidth2 = snapSize(result4.f11926x);
            r2.resize(rightWidth2, snapSize(result4.f11927y));
            rightWidth = snapSpace(rightMargin.getLeft()) + rightWidth2 + snapSpace(rightMargin.getRight());
            Pos alignment4 = getAlignment(r2);
            positionInArea(r2, (insideX + insideWidth) - rightWidth, insideY + topHeight, rightWidth, (insideHeight - topHeight) - bottomHeight, 0.0d, rightMargin, alignment4 != null ? alignment4.getHpos() : HPos.RIGHT, alignment4 != null ? alignment4.getVpos() : VPos.TOP, isSnapToPixel());
        }
        if (c2 != null && c2.isManaged()) {
            Pos alignment5 = getAlignment(c2);
            layoutInArea(c2, insideX + leftWidth, insideY + topHeight, (insideWidth - leftWidth) - rightWidth, (insideHeight - topHeight) - bottomHeight, 0.0d, getNodeMargin(c2), alignment5 != null ? alignment5.getHpos() : HPos.CENTER, alignment5 != null ? alignment5.getVpos() : VPos.CENTER);
        }
    }

    private double getAreaWidth(Node child, double height, boolean minimum) {
        if (child != null && child.isManaged()) {
            Insets margin = getNodeMargin(child);
            return minimum ? computeChildMinAreaWidth(child, -1.0d, margin, height, false) : computeChildPrefAreaWidth(child, -1.0d, margin, height, false);
        }
        return 0.0d;
    }

    private double getAreaHeight(Node child, double width, boolean minimum) {
        if (child != null && child.isManaged()) {
            Insets margin = getNodeMargin(child);
            return minimum ? computeChildMinAreaHeight(child, -1.0d, margin, width) : computeChildPrefAreaHeight(child, -1.0d, margin, width);
        }
        return 0.0d;
    }

    private boolean childHasContentBias(Node child, Orientation orientation) {
        return child != null && child.isManaged() && child.getContentBias() == orientation;
    }

    /* loaded from: jfxrt.jar:javafx/scene/layout/BorderPane$BorderPositionProperty.class */
    private final class BorderPositionProperty extends ObjectPropertyBase<Node> {
        private Node oldValue = null;
        private final String propertyName;
        private boolean isBeingInvalidated;

        BorderPositionProperty(String propertyName) {
            this.propertyName = propertyName;
            BorderPane.this.getChildren().addListener(new ListChangeListener<Node>() { // from class: javafx.scene.layout.BorderPane.BorderPositionProperty.1
                @Override // javafx.collections.ListChangeListener
                public void onChanged(ListChangeListener.Change<? extends Node> c2) {
                    if (BorderPositionProperty.this.oldValue == null || BorderPositionProperty.this.isBeingInvalidated) {
                        return;
                    }
                    while (c2.next()) {
                        if (c2.wasRemoved()) {
                            List<? extends Node> removed = c2.getRemoved();
                            int sz = removed.size();
                            for (int i2 = 0; i2 < sz; i2++) {
                                if (removed.get(i2) == BorderPositionProperty.this.oldValue) {
                                    BorderPositionProperty.this.oldValue = null;
                                    BorderPositionProperty.this.set(null);
                                }
                            }
                        }
                    }
                }
            });
        }

        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            List<Node> children = BorderPane.this.getChildren();
            this.isBeingInvalidated = true;
            try {
                if (this.oldValue != null) {
                    children.remove(this.oldValue);
                }
                Node _value = get();
                this.oldValue = _value;
                if (_value != null) {
                    children.add(_value);
                }
            } finally {
                this.isBeingInvalidated = false;
            }
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return BorderPane.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return this.propertyName;
        }
    }
}
