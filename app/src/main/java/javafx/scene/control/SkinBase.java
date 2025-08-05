package javafx.scene.control;

import java.util.Collections;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

/* loaded from: jfxrt.jar:javafx/scene/control/SkinBase.class */
public abstract class SkinBase<C extends Control> implements Skin<C> {
    private C control;
    private ObservableList<Node> children;
    private static final EventHandler<MouseEvent> mouseEventConsumer = event -> {
        event.consume();
    };

    protected SkinBase(C control) {
        if (control == null) {
            throw new IllegalArgumentException("Cannot pass null for control");
        }
        this.control = control;
        this.children = control.getControlChildren();
        consumeMouseEvents(true);
    }

    @Override // javafx.scene.control.Skin
    public final C getSkinnable() {
        return this.control;
    }

    @Override // javafx.scene.control.Skin
    public final Node getNode() {
        return this.control;
    }

    @Override // javafx.scene.control.Skin
    public void dispose() {
        this.control = null;
    }

    public final ObservableList<Node> getChildren() {
        return this.children;
    }

    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        int max = this.children.size();
        for (int i2 = 0; i2 < max; i2++) {
            Node child = this.children.get(i2);
            if (child.isManaged()) {
                layoutInArea(child, contentX, contentY, contentWidth, contentHeight, -1.0d, HPos.CENTER, VPos.CENTER);
            }
        }
    }

    protected final void consumeMouseEvents(boolean value) {
        if (value) {
            this.control.addEventHandler(MouseEvent.ANY, mouseEventConsumer);
        } else {
            this.control.removeEventHandler(MouseEvent.ANY, mouseEventConsumer);
        }
    }

    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double minX = 0.0d;
        double maxX = 0.0d;
        boolean firstManagedChild = true;
        for (int i2 = 0; i2 < this.children.size(); i2++) {
            Node node = this.children.get(i2);
            if (node.isManaged()) {
                double x2 = node.getLayoutBounds().getMinX() + node.getLayoutX();
                if (!firstManagedChild) {
                    minX = Math.min(minX, x2);
                    maxX = Math.max(maxX, x2 + node.minWidth(-1.0d));
                } else {
                    minX = x2;
                    maxX = x2 + node.minWidth(-1.0d);
                    firstManagedChild = false;
                }
            }
        }
        double minWidth = maxX - minX;
        return leftInset + minWidth + rightInset;
    }

    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        double minY = 0.0d;
        double maxY = 0.0d;
        boolean firstManagedChild = true;
        for (int i2 = 0; i2 < this.children.size(); i2++) {
            Node node = this.children.get(i2);
            if (node.isManaged()) {
                double y2 = node.getLayoutBounds().getMinY() + node.getLayoutY();
                if (!firstManagedChild) {
                    minY = Math.min(minY, y2);
                    maxY = Math.max(maxY, y2 + node.minHeight(-1.0d));
                } else {
                    minY = y2;
                    maxY = y2 + node.minHeight(-1.0d);
                    firstManagedChild = false;
                }
            }
        }
        double minHeight = maxY - minY;
        return topInset + minHeight + bottomInset;
    }

    protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return Double.MAX_VALUE;
    }

    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return Double.MAX_VALUE;
    }

    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double minX = 0.0d;
        double maxX = 0.0d;
        boolean firstManagedChild = true;
        for (int i2 = 0; i2 < this.children.size(); i2++) {
            Node node = this.children.get(i2);
            if (node.isManaged()) {
                double x2 = node.getLayoutBounds().getMinX() + node.getLayoutX();
                if (!firstManagedChild) {
                    minX = Math.min(minX, x2);
                    maxX = Math.max(maxX, x2 + node.prefWidth(-1.0d));
                } else {
                    minX = x2;
                    maxX = x2 + node.prefWidth(-1.0d);
                    firstManagedChild = false;
                }
            }
        }
        return maxX - minX;
    }

    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        double minY = 0.0d;
        double maxY = 0.0d;
        boolean firstManagedChild = true;
        for (int i2 = 0; i2 < this.children.size(); i2++) {
            Node node = this.children.get(i2);
            if (node.isManaged()) {
                double y2 = node.getLayoutBounds().getMinY() + node.getLayoutY();
                if (!firstManagedChild) {
                    minY = Math.min(minY, y2);
                    maxY = Math.max(maxY, y2 + node.prefHeight(-1.0d));
                } else {
                    minY = y2;
                    maxY = y2 + node.prefHeight(-1.0d);
                    firstManagedChild = false;
                }
            }
        }
        return maxY - minY;
    }

    protected double computeBaselineOffset(double topInset, double rightInset, double bottomInset, double leftInset) {
        int size = this.children.size();
        for (int i2 = 0; i2 < size; i2++) {
            Node child = this.children.get(i2);
            if (child.isManaged()) {
                double offset = child.getBaselineOffset();
                if (offset != Double.NEGATIVE_INFINITY) {
                    return child.getLayoutBounds().getMinY() + child.getLayoutY() + offset;
                }
            }
        }
        return Double.NEGATIVE_INFINITY;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public double snappedTopInset() {
        return this.control.snappedTopInset();
    }

    protected double snappedBottomInset() {
        return this.control.snappedBottomInset();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public double snappedLeftInset() {
        return this.control.snappedLeftInset();
    }

    protected double snappedRightInset() {
        return this.control.snappedRightInset();
    }

    protected double snapSpace(double value) {
        return this.control.isSnapToPixel() ? Math.round(value) : value;
    }

    protected double snapSize(double value) {
        return this.control.isSnapToPixel() ? Math.ceil(value) : value;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public double snapPosition(double value) {
        return this.control.isSnapToPixel() ? Math.round(value) : value;
    }

    protected void positionInArea(Node child, double areaX, double areaY, double areaWidth, double areaHeight, double areaBaselineOffset, HPos halignment, VPos valignment) {
        positionInArea(child, areaX, areaY, areaWidth, areaHeight, areaBaselineOffset, Insets.EMPTY, halignment, valignment);
    }

    protected void positionInArea(Node child, double areaX, double areaY, double areaWidth, double areaHeight, double areaBaselineOffset, Insets margin, HPos halignment, VPos valignment) {
        Region.positionInArea(child, areaX, areaY, areaWidth, areaHeight, areaBaselineOffset, margin, halignment, valignment, this.control.isSnapToPixel());
    }

    protected void layoutInArea(Node child, double areaX, double areaY, double areaWidth, double areaHeight, double areaBaselineOffset, HPos halignment, VPos valignment) {
        layoutInArea(child, areaX, areaY, areaWidth, areaHeight, areaBaselineOffset, Insets.EMPTY, true, true, halignment, valignment);
    }

    protected void layoutInArea(Node child, double areaX, double areaY, double areaWidth, double areaHeight, double areaBaselineOffset, Insets margin, HPos halignment, VPos valignment) {
        layoutInArea(child, areaX, areaY, areaWidth, areaHeight, areaBaselineOffset, margin, true, true, halignment, valignment);
    }

    protected void layoutInArea(Node child, double areaX, double areaY, double areaWidth, double areaHeight, double areaBaselineOffset, Insets margin, boolean fillWidth, boolean fillHeight, HPos halignment, VPos valignment) {
        Region.layoutInArea(child, areaX, areaY, areaWidth, areaHeight, areaBaselineOffset, margin, fillWidth, fillHeight, halignment, valignment, this.control.isSnapToPixel());
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/SkinBase$StyleableProperties.class */
    private static class StyleableProperties {
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES = Collections.unmodifiableList(Control.getClassCssMetaData());

        private StyleableProperties() {
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

    public final void pseudoClassStateChanged(PseudoClass pseudoClass, boolean active) {
        Control ctl = getSkinnable();
        if (ctl != null) {
            ctl.pseudoClassStateChanged(pseudoClass, active);
        }
    }

    protected Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        return null;
    }

    protected void executeAccessibleAction(AccessibleAction action, Object... parameters) {
    }
}
