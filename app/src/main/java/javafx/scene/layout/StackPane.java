package javafx.scene.layout;

import com.sun.javafx.css.converters.EnumConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.util.Callback;

/* loaded from: jfxrt.jar:javafx/scene/layout/StackPane.class */
public class StackPane extends Pane {
    private boolean biasDirty = true;
    private boolean performingLayout = false;
    private Orientation bias;
    private static final String MARGIN_CONSTRAINT = "stackpane-margin";
    private static final String ALIGNMENT_CONSTRAINT = "stackpane-alignment";
    private static final Callback<Node, Insets> marginAccessor = n2 -> {
        return getMargin(n2);
    };
    private ObjectProperty<Pos> alignment;

    public static void setAlignment(Node child, Pos value) {
        setConstraint(child, ALIGNMENT_CONSTRAINT, value);
    }

    public static Pos getAlignment(Node child) {
        return (Pos) getConstraint(child, ALIGNMENT_CONSTRAINT);
    }

    public static void setMargin(Node child, Insets value) {
        setConstraint(child, MARGIN_CONSTRAINT, value);
    }

    public static Insets getMargin(Node child) {
        return (Insets) getConstraint(child, MARGIN_CONSTRAINT);
    }

    public static void clearConstraints(Node child) {
        setAlignment(child, null);
        setMargin(child, null);
    }

    public StackPane() {
    }

    public StackPane(Node... children) {
        getChildren().addAll(children);
    }

    public final ObjectProperty<Pos> alignmentProperty() {
        if (this.alignment == null) {
            this.alignment = new StyleableObjectProperty<Pos>(Pos.CENTER) { // from class: javafx.scene.layout.StackPane.1
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    StackPane.this.requestLayout();
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<StackPane, Pos> getCssMetaData() {
                    return StyleableProperties.ALIGNMENT;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return StackPane.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "alignment";
                }
            };
        }
        return this.alignment;
    }

    public final void setAlignment(Pos value) {
        alignmentProperty().set(value);
    }

    public final Pos getAlignment() {
        return this.alignment == null ? Pos.CENTER : this.alignment.get();
    }

    private Pos getAlignmentInternal() {
        Pos localPos = getAlignment();
        return localPos == null ? Pos.CENTER : localPos;
    }

    @Override // javafx.scene.Node
    public Orientation getContentBias() {
        if (this.biasDirty) {
            this.bias = null;
            List<Node> children = getManagedChildren();
            for (Node child : children) {
                Orientation contentBias = child.getContentBias();
                if (contentBias != null) {
                    this.bias = contentBias;
                    if (contentBias == Orientation.HORIZONTAL) {
                        break;
                    }
                }
            }
            this.biasDirty = false;
        }
        return this.bias;
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computeMinWidth(double height) {
        List<Node> managed = getManagedChildren();
        return getInsets().getLeft() + computeMaxMinAreaWidth(managed, marginAccessor, height, true) + getInsets().getRight();
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computeMinHeight(double width) {
        List<Node> managed = getManagedChildren();
        return getInsets().getTop() + computeMaxMinAreaHeight(managed, marginAccessor, getAlignmentInternal().getVpos(), width) + getInsets().getBottom();
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefWidth(double height) {
        List<Node> managed = getManagedChildren();
        Insets padding = getInsets();
        return padding.getLeft() + computeMaxPrefAreaWidth(managed, marginAccessor, height == -1.0d ? -1.0d : (height - padding.getTop()) - padding.getBottom(), true) + padding.getRight();
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefHeight(double width) {
        List<Node> managed = getManagedChildren();
        Insets padding = getInsets();
        return padding.getTop() + computeMaxPrefAreaHeight(managed, marginAccessor, width == -1.0d ? -1.0d : (width - padding.getLeft()) - padding.getRight(), getAlignmentInternal().getVpos()) + padding.getBottom();
    }

    @Override // javafx.scene.Parent
    public void requestLayout() {
        if (this.performingLayout) {
            return;
        }
        this.biasDirty = true;
        this.bias = null;
        super.requestLayout();
    }

    @Override // javafx.scene.Parent
    protected void layoutChildren() {
        this.performingLayout = true;
        List<Node> managed = getManagedChildren();
        Pos align = getAlignmentInternal();
        HPos alignHpos = align.getHpos();
        VPos alignVpos = align.getVpos();
        double width = getWidth();
        double height = getHeight();
        double top = getInsets().getTop();
        double right = getInsets().getRight();
        double left = getInsets().getLeft();
        double bottom = getInsets().getBottom();
        double contentWidth = (width - left) - right;
        double contentHeight = (height - top) - bottom;
        double baselineOffset = alignVpos == VPos.BASELINE ? getAreaBaselineOffset(managed, marginAccessor, i2 -> {
            return Double.valueOf(width);
        }, contentHeight, true) : 0.0d;
        int size = managed.size();
        for (int i3 = 0; i3 < size; i3++) {
            Node child = managed.get(i3);
            Pos childAlignment = getAlignment(child);
            layoutInArea(child, left, top, contentWidth, contentHeight, baselineOffset, getMargin(child), childAlignment != null ? childAlignment.getHpos() : alignHpos, childAlignment != null ? childAlignment.getVpos() : alignVpos);
        }
        this.performingLayout = false;
    }

    /* loaded from: jfxrt.jar:javafx/scene/layout/StackPane$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<StackPane, Pos> ALIGNMENT = new CssMetaData<StackPane, Pos>("-fx-alignment", new EnumConverter(Pos.class), Pos.CENTER) { // from class: javafx.scene.layout.StackPane.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(StackPane node) {
                return node.alignment == null || !node.alignment.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Pos> getStyleableProperty(StackPane node) {
                return (StyleableProperty) node.alignmentProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Region.getClassCssMetaData());
            styleables.add(ALIGNMENT);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Node, javafx.css.Styleable
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }
}
