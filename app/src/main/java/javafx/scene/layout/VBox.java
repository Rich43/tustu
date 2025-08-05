package javafx.scene.layout;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.util.Callback;

/* loaded from: jfxrt.jar:javafx/scene/layout/VBox.class */
public class VBox extends Pane {
    private boolean biasDirty;
    private boolean performingLayout;
    private Orientation bias;
    private double[][] tempArray;
    private static final String MARGIN_CONSTRAINT = "vbox-margin";
    private static final String VGROW_CONSTRAINT = "vbox-vgrow";
    private static final Callback<Node, Insets> marginAccessor = n2 -> {
        return getMargin(n2);
    };
    private DoubleProperty spacing;
    private ObjectProperty<Pos> alignment;
    private BooleanProperty fillWidth;

    public static void setVgrow(Node child, Priority value) {
        setConstraint(child, VGROW_CONSTRAINT, value);
    }

    public static Priority getVgrow(Node child) {
        return (Priority) getConstraint(child, VGROW_CONSTRAINT);
    }

    public static void setMargin(Node child, Insets value) {
        setConstraint(child, MARGIN_CONSTRAINT, value);
    }

    public static Insets getMargin(Node child) {
        return (Insets) getConstraint(child, MARGIN_CONSTRAINT);
    }

    public static void clearConstraints(Node child) {
        setVgrow(child, null);
        setMargin(child, null);
    }

    public VBox() {
        this.biasDirty = true;
        this.performingLayout = false;
    }

    public VBox(double spacing) {
        this();
        setSpacing(spacing);
    }

    public VBox(Node... children) {
        this.biasDirty = true;
        this.performingLayout = false;
        getChildren().addAll(children);
    }

    public VBox(double spacing, Node... children) {
        this();
        setSpacing(spacing);
        getChildren().addAll(children);
    }

    public final DoubleProperty spacingProperty() {
        if (this.spacing == null) {
            this.spacing = new StyleableDoubleProperty() { // from class: javafx.scene.layout.VBox.1
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    VBox.this.requestLayout();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return VBox.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "spacing";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.SPACING;
                }
            };
        }
        return this.spacing;
    }

    public final void setSpacing(double value) {
        spacingProperty().set(value);
    }

    public final double getSpacing() {
        if (this.spacing == null) {
            return 0.0d;
        }
        return this.spacing.get();
    }

    public final ObjectProperty<Pos> alignmentProperty() {
        if (this.alignment == null) {
            this.alignment = new StyleableObjectProperty<Pos>(Pos.TOP_LEFT) { // from class: javafx.scene.layout.VBox.2
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    VBox.this.requestLayout();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return VBox.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "alignment";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<VBox, Pos> getCssMetaData() {
                    return StyleableProperties.ALIGNMENT;
                }
            };
        }
        return this.alignment;
    }

    public final void setAlignment(Pos value) {
        alignmentProperty().set(value);
    }

    public final Pos getAlignment() {
        return this.alignment == null ? Pos.TOP_LEFT : this.alignment.get();
    }

    private Pos getAlignmentInternal() {
        Pos localPos = getAlignment();
        return localPos == null ? Pos.TOP_LEFT : localPos;
    }

    public final BooleanProperty fillWidthProperty() {
        if (this.fillWidth == null) {
            this.fillWidth = new StyleableBooleanProperty(true) { // from class: javafx.scene.layout.VBox.3
                @Override // javafx.beans.property.BooleanPropertyBase
                public void invalidated() {
                    VBox.this.requestLayout();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return VBox.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "fillWidth";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                    return StyleableProperties.FILL_WIDTH;
                }
            };
        }
        return this.fillWidth;
    }

    public final void setFillWidth(boolean value) {
        fillWidthProperty().set(value);
    }

    public final boolean isFillWidth() {
        if (this.fillWidth == null) {
            return true;
        }
        return this.fillWidth.get();
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
        double contentWidth;
        Insets insets = getInsets();
        List<Node> managed = getManagedChildren();
        if (height != -1.0d && getContentBias() != null) {
            double[][] prefHeights = getAreaHeights(managed, -1.0d, false);
            adjustAreaHeights(managed, prefHeights, height, -1.0d);
            contentWidth = computeMaxMinAreaWidth(managed, marginAccessor, prefHeights[0], false);
        } else {
            contentWidth = computeMaxMinAreaWidth(managed, marginAccessor);
        }
        return snapSpace(insets.getLeft()) + contentWidth + snapSpace(insets.getRight());
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computeMinHeight(double width) {
        Insets insets = getInsets();
        return snapSpace(insets.getTop()) + computeContentHeight(getManagedChildren(), width, true) + snapSpace(insets.getBottom());
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefWidth(double height) {
        double contentWidth;
        Insets insets = getInsets();
        List<Node> managed = getManagedChildren();
        if (height != -1.0d && getContentBias() != null) {
            double[][] prefHeights = getAreaHeights(managed, -1.0d, false);
            adjustAreaHeights(managed, prefHeights, height, -1.0d);
            contentWidth = computeMaxPrefAreaWidth(managed, marginAccessor, prefHeights[0], false);
        } else {
            contentWidth = computeMaxPrefAreaWidth(managed, marginAccessor);
        }
        return snapSpace(insets.getLeft()) + contentWidth + snapSpace(insets.getRight());
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefHeight(double width) {
        Insets insets = getInsets();
        double d2 = snapSpace(insets.getTop()) + computeContentHeight(getManagedChildren(), width, false) + snapSpace(insets.getBottom());
        return d2;
    }

    private double[][] getAreaHeights(List<Node> managed, double width, boolean minimum) {
        double[][] temp = getTempArray(managed.size());
        double insideWidth = width == -1.0d ? -1.0d : (width - snapSpace(getInsets().getLeft())) - snapSpace(getInsets().getRight());
        boolean isFillWidth = isFillWidth();
        int size = managed.size();
        for (int i2 = 0; i2 < size; i2++) {
            Node child = managed.get(i2);
            Insets margin = getMargin(child);
            if (minimum) {
                if (insideWidth != -1.0d && isFillWidth) {
                    temp[0][i2] = computeChildMinAreaHeight(child, -1.0d, margin, insideWidth);
                } else {
                    temp[0][i2] = computeChildMinAreaHeight(child, -1.0d, margin, -1.0d);
                }
            } else if (insideWidth != -1.0d && isFillWidth) {
                temp[0][i2] = computeChildPrefAreaHeight(child, -1.0d, margin, insideWidth);
            } else {
                temp[0][i2] = computeChildPrefAreaHeight(child, -1.0d, margin, -1.0d);
            }
        }
        return temp;
    }

    private double adjustAreaHeights(List<Node> managed, double[][] areaHeights, double height, double width) {
        Insets insets = getInsets();
        double left = snapSpace(insets.getLeft());
        double right = snapSpace(insets.getRight());
        double contentHeight = sum(areaHeights[0], managed.size()) + ((managed.size() - 1) * snapSpace(getSpacing()));
        double extraHeight = ((height - snapSpace(insets.getTop())) - snapSpace(insets.getBottom())) - contentHeight;
        if (extraHeight != 0.0d) {
            double refWidth = (!isFillWidth() || width == -1.0d) ? -1.0d : (width - left) - right;
            double remaining = growOrShrinkAreaHeights(managed, areaHeights, Priority.ALWAYS, extraHeight, refWidth);
            contentHeight += extraHeight - growOrShrinkAreaHeights(managed, areaHeights, Priority.SOMETIMES, remaining, refWidth);
        }
        return contentHeight;
    }

    private double growOrShrinkAreaHeights(List<Node> managed, double[][] areaHeights, Priority priority, double extraHeight, double width) {
        boolean shrinking = extraHeight < 0.0d;
        int adjustingNumber = 0;
        double[] usedHeights = areaHeights[0];
        double[] temp = areaHeights[1];
        if (shrinking) {
            adjustingNumber = managed.size();
            int size = managed.size();
            for (int i2 = 0; i2 < size; i2++) {
                Node child = managed.get(i2);
                temp[i2] = computeChildMinAreaHeight(child, -1.0d, getMargin(child), width);
            }
        } else {
            int size2 = managed.size();
            for (int i3 = 0; i3 < size2; i3++) {
                Node child2 = managed.get(i3);
                if (getVgrow(child2) == priority) {
                    temp[i3] = computeChildMaxAreaHeight(child2, -1.0d, getMargin(child2), width);
                    adjustingNumber++;
                } else {
                    temp[i3] = -1.0d;
                }
            }
        }
        double available = extraHeight;
        loop1: while (Math.abs(available) > 1.0d && adjustingNumber > 0) {
            double portion = snapPortion(available / adjustingNumber);
            int size3 = managed.size();
            for (int i4 = 0; i4 < size3; i4++) {
                if (temp[i4] != -1.0d) {
                    double limit = temp[i4] - usedHeights[i4];
                    double change = Math.abs(limit) <= Math.abs(portion) ? limit : portion;
                    int i5 = i4;
                    usedHeights[i5] = usedHeights[i5] + change;
                    available -= change;
                    if (Math.abs(available) < 1.0d) {
                        break loop1;
                    }
                    if (Math.abs(change) < Math.abs(portion)) {
                        temp[i4] = -1.0d;
                        adjustingNumber--;
                    }
                }
            }
        }
        return available;
    }

    private double computeContentHeight(List<Node> managedChildren, double width, boolean minimum) {
        return sum(getAreaHeights(managedChildren, width, minimum)[0], managedChildren.size()) + ((managedChildren.size() - 1) * snapSpace(getSpacing()));
    }

    private static double sum(double[] array, int size) {
        int i2 = 0;
        double d2 = 0.0d;
        while (true) {
            double res = d2;
            if (i2 != size) {
                int i3 = i2;
                i2++;
                d2 = res + array[i3];
            } else {
                return res;
            }
        }
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
        Insets insets = getInsets();
        double width = getWidth();
        double height = getHeight();
        double top = snapSpace(insets.getTop());
        double left = snapSpace(insets.getLeft());
        double bottom = snapSpace(insets.getBottom());
        double right = snapSpace(insets.getRight());
        double space = snapSpace(getSpacing());
        HPos hpos = getAlignmentInternal().getHpos();
        VPos vpos = getAlignmentInternal().getVpos();
        boolean isFillWidth = isFillWidth();
        double[][] actualAreaHeights = getAreaHeights(managed, width, false);
        double contentWidth = (width - left) - right;
        double contentHeight = adjustAreaHeights(managed, actualAreaHeights, height, width);
        double y2 = top + computeYOffset((height - top) - bottom, contentHeight, vpos);
        int size = managed.size();
        for (int i2 = 0; i2 < size; i2++) {
            Node child = managed.get(i2);
            layoutInArea(child, left, y2, contentWidth, actualAreaHeights[0][i2], actualAreaHeights[0][i2], getMargin(child), isFillWidth, true, hpos, vpos);
            y2 += actualAreaHeights[0][i2] + space;
        }
        this.performingLayout = false;
    }

    private double[][] getTempArray(int size) {
        if (this.tempArray == null) {
            this.tempArray = new double[2][size];
        } else if (this.tempArray[0].length < size) {
            this.tempArray = new double[2][Math.max(this.tempArray.length * 3, size)];
        }
        return this.tempArray;
    }

    /* loaded from: jfxrt.jar:javafx/scene/layout/VBox$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<VBox, Pos> ALIGNMENT = new CssMetaData<VBox, Pos>("-fx-alignment", new EnumConverter(Pos.class), Pos.TOP_LEFT) { // from class: javafx.scene.layout.VBox.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(VBox node) {
                return node.alignment == null || !node.alignment.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Pos> getStyleableProperty(VBox node) {
                return (StyleableProperty) node.alignmentProperty();
            }
        };
        private static final CssMetaData<VBox, Boolean> FILL_WIDTH = new CssMetaData<VBox, Boolean>("-fx-fill-width", BooleanConverter.getInstance(), Boolean.TRUE) { // from class: javafx.scene.layout.VBox.StyleableProperties.2
            @Override // javafx.css.CssMetaData
            public boolean isSettable(VBox node) {
                return node.fillWidth == null || !node.fillWidth.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(VBox node) {
                return (StyleableProperty) node.fillWidthProperty();
            }
        };
        private static final CssMetaData<VBox, Number> SPACING = new CssMetaData<VBox, Number>("-fx-spacing", SizeConverter.getInstance(), Double.valueOf(0.0d)) { // from class: javafx.scene.layout.VBox.StyleableProperties.3
            @Override // javafx.css.CssMetaData
            public boolean isSettable(VBox node) {
                return node.spacing == null || !node.spacing.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(VBox node) {
                return (StyleableProperty) node.spacingProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Region.getClassCssMetaData());
            styleables.add(ALIGNMENT);
            styleables.add(FILL_WIDTH);
            styleables.add(SPACING);
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
