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

/* loaded from: jfxrt.jar:javafx/scene/layout/HBox.class */
public class HBox extends Pane {
    private boolean biasDirty;
    private boolean performingLayout;
    private double minBaselineComplement;
    private double prefBaselineComplement;
    private Orientation bias;
    private double[][] tempArray;
    private static final String MARGIN_CONSTRAINT = "hbox-margin";
    private static final String HGROW_CONSTRAINT = "hbox-hgrow";
    private static final Callback<Node, Insets> marginAccessor = n2 -> {
        return getMargin(n2);
    };
    private DoubleProperty spacing;
    private ObjectProperty<Pos> alignment;
    private BooleanProperty fillHeight;
    private double baselineOffset;

    public static void setHgrow(Node child, Priority value) {
        setConstraint(child, HGROW_CONSTRAINT, value);
    }

    public static Priority getHgrow(Node child) {
        return (Priority) getConstraint(child, HGROW_CONSTRAINT);
    }

    public static void setMargin(Node child, Insets value) {
        setConstraint(child, MARGIN_CONSTRAINT, value);
    }

    public static Insets getMargin(Node child) {
        return (Insets) getConstraint(child, MARGIN_CONSTRAINT);
    }

    public static void clearConstraints(Node child) {
        setHgrow(child, null);
        setMargin(child, null);
    }

    public HBox() {
        this.biasDirty = true;
        this.performingLayout = false;
        this.minBaselineComplement = Double.NaN;
        this.prefBaselineComplement = Double.NaN;
        this.baselineOffset = Double.NaN;
    }

    public HBox(double spacing) {
        this();
        setSpacing(spacing);
    }

    public HBox(Node... children) {
        this.biasDirty = true;
        this.performingLayout = false;
        this.minBaselineComplement = Double.NaN;
        this.prefBaselineComplement = Double.NaN;
        this.baselineOffset = Double.NaN;
        getChildren().addAll(children);
    }

    public HBox(double spacing, Node... children) {
        this();
        setSpacing(spacing);
        getChildren().addAll(children);
    }

    public final DoubleProperty spacingProperty() {
        if (this.spacing == null) {
            this.spacing = new StyleableDoubleProperty() { // from class: javafx.scene.layout.HBox.1
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    HBox.this.requestLayout();
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.SPACING;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return HBox.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "spacing";
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
            this.alignment = new StyleableObjectProperty<Pos>(Pos.TOP_LEFT) { // from class: javafx.scene.layout.HBox.2
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    HBox.this.requestLayout();
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<HBox, Pos> getCssMetaData() {
                    return StyleableProperties.ALIGNMENT;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return HBox.this;
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
        return this.alignment == null ? Pos.TOP_LEFT : this.alignment.get();
    }

    private Pos getAlignmentInternal() {
        Pos localPos = getAlignment();
        return localPos == null ? Pos.TOP_LEFT : localPos;
    }

    public final BooleanProperty fillHeightProperty() {
        if (this.fillHeight == null) {
            this.fillHeight = new StyleableBooleanProperty(true) { // from class: javafx.scene.layout.HBox.3
                @Override // javafx.beans.property.BooleanPropertyBase
                public void invalidated() {
                    HBox.this.requestLayout();
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                    return StyleableProperties.FILL_HEIGHT;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return HBox.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "fillHeight";
                }
            };
        }
        return this.fillHeight;
    }

    public final void setFillHeight(boolean value) {
        fillHeightProperty().set(value);
    }

    public final boolean isFillHeight() {
        if (this.fillHeight == null) {
            return true;
        }
        return this.fillHeight.get();
    }

    private boolean shouldFillHeight() {
        return isFillHeight() && getAlignmentInternal().getVpos() != VPos.BASELINE;
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
        Insets insets = getInsets();
        return snapSpace(insets.getLeft()) + computeContentWidth(getManagedChildren(), height, true) + snapSpace(insets.getRight());
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computeMinHeight(double width) {
        double contentHeight;
        Insets insets = getInsets();
        List<Node> managed = getManagedChildren();
        if (width != -1.0d && getContentBias() != null) {
            double[][] prefWidths = getAreaWidths(managed, -1.0d, false);
            adjustAreaWidths(managed, prefWidths, width, -1.0d);
            contentHeight = computeMaxMinAreaHeight(managed, marginAccessor, prefWidths[0], getAlignmentInternal().getVpos());
        } else {
            contentHeight = computeMaxMinAreaHeight(managed, marginAccessor, getAlignmentInternal().getVpos());
        }
        return snapSpace(insets.getTop()) + contentHeight + snapSpace(insets.getBottom());
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefWidth(double height) {
        Insets insets = getInsets();
        return snapSpace(insets.getLeft()) + computeContentWidth(getManagedChildren(), height, false) + snapSpace(insets.getRight());
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefHeight(double width) {
        double contentHeight;
        Insets insets = getInsets();
        List<Node> managed = getManagedChildren();
        if (width != -1.0d && getContentBias() != null) {
            double[][] prefWidths = getAreaWidths(managed, -1.0d, false);
            adjustAreaWidths(managed, prefWidths, width, -1.0d);
            contentHeight = computeMaxPrefAreaHeight(managed, marginAccessor, prefWidths[0], getAlignmentInternal().getVpos());
        } else {
            contentHeight = computeMaxPrefAreaHeight(managed, marginAccessor, getAlignmentInternal().getVpos());
        }
        return snapSpace(insets.getTop()) + contentHeight + snapSpace(insets.getBottom());
    }

    private double[][] getAreaWidths(List<Node> managed, double height, boolean minimum) {
        double[][] temp = getTempArray(managed.size());
        double insideHeight = height == -1.0d ? -1.0d : (height - snapSpace(getInsets().getTop())) - snapSpace(getInsets().getBottom());
        boolean shouldFillHeight = shouldFillHeight();
        int size = managed.size();
        for (int i2 = 0; i2 < size; i2++) {
            Node child = managed.get(i2);
            Insets margin = getMargin(child);
            if (minimum) {
                temp[0][i2] = computeChildMinAreaWidth(child, getMinBaselineComplement(), margin, insideHeight, shouldFillHeight);
            } else {
                temp[0][i2] = computeChildPrefAreaWidth(child, getPrefBaselineComplement(), margin, insideHeight, shouldFillHeight);
            }
        }
        return temp;
    }

    private double adjustAreaWidths(List<Node> managed, double[][] areaWidths, double width, double height) {
        Insets insets = getInsets();
        double top = snapSpace(insets.getTop());
        double bottom = snapSpace(insets.getBottom());
        double contentWidth = sum(areaWidths[0], managed.size()) + ((managed.size() - 1) * snapSpace(getSpacing()));
        double extraWidth = ((width - snapSpace(insets.getLeft())) - snapSpace(insets.getRight())) - contentWidth;
        if (extraWidth != 0.0d) {
            double refHeight = (!shouldFillHeight() || height == -1.0d) ? -1.0d : (height - top) - bottom;
            double remaining = growOrShrinkAreaWidths(managed, areaWidths, Priority.ALWAYS, extraWidth, refHeight);
            contentWidth += extraWidth - growOrShrinkAreaWidths(managed, areaWidths, Priority.SOMETIMES, remaining, refHeight);
        }
        return contentWidth;
    }

    private double growOrShrinkAreaWidths(List<Node> managed, double[][] areaWidths, Priority priority, double extraWidth, double height) {
        boolean shrinking = extraWidth < 0.0d;
        int adjustingNumber = 0;
        double[] usedWidths = areaWidths[0];
        double[] temp = areaWidths[1];
        boolean shouldFillHeight = shouldFillHeight();
        if (shrinking) {
            adjustingNumber = managed.size();
            int size = managed.size();
            for (int i2 = 0; i2 < size; i2++) {
                Node child = managed.get(i2);
                temp[i2] = computeChildMinAreaWidth(child, getMinBaselineComplement(), getMargin(child), height, shouldFillHeight);
            }
        } else {
            int size2 = managed.size();
            for (int i3 = 0; i3 < size2; i3++) {
                Node child2 = managed.get(i3);
                if (getHgrow(child2) == priority) {
                    temp[i3] = computeChildMaxAreaWidth(child2, getMinBaselineComplement(), getMargin(child2), height, shouldFillHeight);
                    adjustingNumber++;
                } else {
                    temp[i3] = -1.0d;
                }
            }
        }
        double available = extraWidth;
        loop1: while (Math.abs(available) > 1.0d && adjustingNumber > 0) {
            double portion = snapPortion(available / adjustingNumber);
            int size3 = managed.size();
            for (int i4 = 0; i4 < size3; i4++) {
                if (temp[i4] != -1.0d) {
                    double limit = temp[i4] - usedWidths[i4];
                    double change = Math.abs(limit) <= Math.abs(portion) ? limit : portion;
                    int i5 = i4;
                    usedWidths[i5] = usedWidths[i5] + change;
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

    private double computeContentWidth(List<Node> managedChildren, double height, boolean minimum) {
        return sum(getAreaWidths(managedChildren, height, minimum)[0], managedChildren.size()) + ((managedChildren.size() - 1) * snapSpace(getSpacing()));
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
        this.minBaselineComplement = Double.NaN;
        this.prefBaselineComplement = Double.NaN;
        this.baselineOffset = Double.NaN;
        super.requestLayout();
    }

    private double getMinBaselineComplement() {
        if (Double.isNaN(this.minBaselineComplement)) {
            if (getAlignmentInternal().getVpos() == VPos.BASELINE) {
                this.minBaselineComplement = getMinBaselineComplement(getManagedChildren());
            } else {
                this.minBaselineComplement = -1.0d;
            }
        }
        return this.minBaselineComplement;
    }

    private double getPrefBaselineComplement() {
        if (Double.isNaN(this.prefBaselineComplement)) {
            if (getAlignmentInternal().getVpos() == VPos.BASELINE) {
                this.prefBaselineComplement = getPrefBaselineComplement(getManagedChildren());
            } else {
                this.prefBaselineComplement = -1.0d;
            }
        }
        return this.prefBaselineComplement;
    }

    @Override // javafx.scene.Parent, javafx.scene.Node
    public double getBaselineOffset() {
        List<Node> managed = getManagedChildren();
        if (managed.isEmpty()) {
            return Double.NEGATIVE_INFINITY;
        }
        if (Double.isNaN(this.baselineOffset)) {
            VPos vpos = getAlignmentInternal().getVpos();
            if (vpos == VPos.BASELINE) {
                double max = 0.0d;
                int i2 = 0;
                int sz = managed.size();
                while (true) {
                    if (i2 >= sz) {
                        break;
                    }
                    Node child = managed.get(i2);
                    double offset = child.getBaselineOffset();
                    if (offset == Double.NEGATIVE_INFINITY) {
                        this.baselineOffset = Double.NEGATIVE_INFINITY;
                        break;
                    }
                    Insets margin = getMargin(child);
                    double top = margin != null ? margin.getTop() : 0.0d;
                    max = Math.max(max, top + child.getLayoutBounds().getMinY() + offset);
                    i2++;
                }
                this.baselineOffset = max + snappedTopInset();
            } else {
                this.baselineOffset = Double.NEGATIVE_INFINITY;
            }
        }
        return this.baselineOffset;
    }

    @Override // javafx.scene.Parent
    protected void layoutChildren() {
        this.performingLayout = true;
        List<Node> managed = getManagedChildren();
        Insets insets = getInsets();
        Pos align = getAlignmentInternal();
        HPos alignHpos = align.getHpos();
        VPos alignVpos = align.getVpos();
        double width = getWidth();
        double height = getHeight();
        double top = snapSpace(insets.getTop());
        double left = snapSpace(insets.getLeft());
        double bottom = snapSpace(insets.getBottom());
        double right = snapSpace(insets.getRight());
        double space = snapSpace(getSpacing());
        boolean shouldFillHeight = shouldFillHeight();
        double[][] actualAreaWidths = getAreaWidths(managed, height, false);
        double contentWidth = adjustAreaWidths(managed, actualAreaWidths, width, height);
        double contentHeight = (height - top) - bottom;
        double x2 = left + computeXOffset((width - left) - right, contentWidth, align.getHpos());
        double baselineOffset = -1.0d;
        if (alignVpos == VPos.BASELINE) {
            double baselineComplement = getMinBaselineComplement();
            baselineOffset = getAreaBaselineOffset(managed, marginAccessor, i2 -> {
                return Double.valueOf(actualAreaWidths[0][i2.intValue()]);
            }, contentHeight, shouldFillHeight, baselineComplement);
        }
        int size = managed.size();
        for (int i3 = 0; i3 < size; i3++) {
            Node child = managed.get(i3);
            Insets margin = getMargin(child);
            layoutInArea(child, x2, top, actualAreaWidths[0][i3], contentHeight, baselineOffset, margin, true, shouldFillHeight, alignHpos, alignVpos);
            x2 += actualAreaWidths[0][i3] + space;
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

    /* loaded from: jfxrt.jar:javafx/scene/layout/HBox$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<HBox, Pos> ALIGNMENT = new CssMetaData<HBox, Pos>("-fx-alignment", new EnumConverter(Pos.class), Pos.TOP_LEFT) { // from class: javafx.scene.layout.HBox.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(HBox node) {
                return node.alignment == null || !node.alignment.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Pos> getStyleableProperty(HBox node) {
                return (StyleableProperty) node.alignmentProperty();
            }
        };
        private static final CssMetaData<HBox, Boolean> FILL_HEIGHT = new CssMetaData<HBox, Boolean>("-fx-fill-height", BooleanConverter.getInstance(), Boolean.TRUE) { // from class: javafx.scene.layout.HBox.StyleableProperties.2
            @Override // javafx.css.CssMetaData
            public boolean isSettable(HBox node) {
                return node.fillHeight == null || !node.fillHeight.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(HBox node) {
                return (StyleableProperty) node.fillHeightProperty();
            }
        };
        private static final CssMetaData<HBox, Number> SPACING = new CssMetaData<HBox, Number>("-fx-spacing", SizeConverter.getInstance(), Double.valueOf(0.0d)) { // from class: javafx.scene.layout.HBox.StyleableProperties.3
            @Override // javafx.css.CssMetaData
            public boolean isSettable(HBox node) {
                return node.spacing == null || !node.spacing.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(HBox node) {
                return (StyleableProperty) node.spacingProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Pane.getClassCssMetaData());
            styleables.add(FILL_HEIGHT);
            styleables.add(ALIGNMENT);
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
