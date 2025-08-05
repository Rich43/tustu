package javafx.scene.text;

import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.scene.text.TextLayout;
import com.sun.javafx.scene.text.TextLayoutFactory;
import com.sun.javafx.scene.text.TextSpan;
import com.sun.javafx.tk.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/* loaded from: jfxrt.jar:javafx/scene/text/TextFlow.class */
public class TextFlow extends Pane {
    private TextLayout layout;
    private boolean needsContent;
    private boolean inLayout;
    private ObjectProperty<TextAlignment> textAlignment;
    private DoubleProperty lineSpacing;

    public TextFlow() {
        effectiveNodeOrientationProperty().addListener(observable -> {
            checkOrientation();
        });
        setAccessibleRole(AccessibleRole.TEXT);
    }

    public TextFlow(Node... children) {
        this();
        getChildren().addAll(children);
    }

    private void checkOrientation() {
        NodeOrientation orientation = getEffectiveNodeOrientation();
        boolean rtl = orientation == NodeOrientation.RIGHT_TO_LEFT;
        int dir = rtl ? 2048 : 1024;
        TextLayout layout = getTextLayout();
        if (layout.setDirection(dir)) {
            requestLayout();
        }
    }

    @Override // javafx.scene.Node
    public boolean usesMirroring() {
        return false;
    }

    @Override // javafx.scene.layout.Region
    protected void setWidth(double value) {
        if (value != getWidth()) {
            TextLayout layout = getTextLayout();
            Insets insets = getInsets();
            double left = snapSpace(insets.getLeft());
            double right = snapSpace(insets.getRight());
            double width = Math.max(1.0d, (value - left) - right);
            layout.setWrapWidth((float) width);
            super.setWidth(value);
        }
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefWidth(double height) {
        TextLayout layout = getTextLayout();
        layout.setWrapWidth(0.0f);
        double width = layout.getBounds().getWidth();
        Insets insets = getInsets();
        double left = snapSpace(insets.getLeft());
        double right = snapSpace(insets.getRight());
        double wrappingWidth = Math.max(1.0d, (getWidth() - left) - right);
        layout.setWrapWidth((float) wrappingWidth);
        return left + width + right;
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefHeight(double width) {
        TextLayout layout = getTextLayout();
        Insets insets = getInsets();
        double left = snapSpace(insets.getLeft());
        double right = snapSpace(insets.getRight());
        if (width == -1.0d) {
            layout.setWrapWidth(0.0f);
        } else {
            double wrappingWidth = Math.max(1.0d, (width - left) - right);
            layout.setWrapWidth((float) wrappingWidth);
        }
        double height = layout.getBounds().getHeight();
        double wrappingWidth2 = Math.max(1.0d, (getWidth() - left) - right);
        layout.setWrapWidth((float) wrappingWidth2);
        double top = snapSpace(insets.getTop());
        double bottom = snapSpace(insets.getBottom());
        return top + height + bottom;
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computeMinHeight(double width) {
        return computePrefHeight(width);
    }

    @Override // javafx.scene.Parent
    public void requestLayout() {
        if (this.inLayout) {
            return;
        }
        this.needsContent = true;
        super.requestLayout();
    }

    @Override // javafx.scene.Node
    public Orientation getContentBias() {
        return Orientation.HORIZONTAL;
    }

    @Override // javafx.scene.Parent
    protected void layoutChildren() {
        this.inLayout = true;
        Insets insets = getInsets();
        double top = snapSpace(insets.getTop());
        double left = snapSpace(insets.getLeft());
        GlyphList[] runs = getTextLayout().getRuns();
        for (GlyphList run : runs) {
            TextSpan span = run.getTextSpan();
            if (span instanceof EmbeddedSpan) {
                Node child = ((EmbeddedSpan) span).getNode();
                Point2D location = run.getLocation();
                double baselineOffset = -run.getLineBounds().getMinY();
                layoutInArea(child, left + location.f11907x, top + location.f11908y, run.getWidth(), run.getHeight(), baselineOffset, null, true, true, HPos.CENTER, VPos.BASELINE);
            }
        }
        List<Node> managed = getManagedChildren();
        for (Node node : managed) {
            if (node instanceof Text) {
                Text text = (Text) node;
                text.layoutSpan(runs);
                BaseBounds spanBounds = text.getSpanBounds();
                text.relocate(left + spanBounds.getMinX(), top + spanBounds.getMinY());
            }
        }
        this.inLayout = false;
    }

    /* loaded from: jfxrt.jar:javafx/scene/text/TextFlow$EmbeddedSpan.class */
    private static class EmbeddedSpan implements TextSpan {
        RectBounds bounds;
        Node node;

        public EmbeddedSpan(Node node, double baseline, double width, double height) {
            this.node = node;
            this.bounds = new RectBounds(0.0f, (float) (-baseline), (float) width, (float) (height - baseline));
        }

        @Override // com.sun.javafx.scene.text.TextSpan
        public String getText() {
            return "￼";
        }

        @Override // com.sun.javafx.scene.text.TextSpan
        public Object getFont() {
            return null;
        }

        @Override // com.sun.javafx.scene.text.TextSpan
        public RectBounds getBounds() {
            return this.bounds;
        }

        public Node getNode() {
            return this.node;
        }
    }

    TextLayout getTextLayout() {
        if (this.layout == null) {
            TextLayoutFactory factory = Toolkit.getToolkit().getTextLayoutFactory();
            this.layout = factory.createLayout();
            this.needsContent = true;
        }
        if (this.needsContent) {
            List<Node> children = getManagedChildren();
            TextSpan[] spans = new TextSpan[children.size()];
            for (int i2 = 0; i2 < spans.length; i2++) {
                Node node = children.get(i2);
                if (node instanceof Text) {
                    spans[i2] = ((Text) node).getTextSpan();
                } else {
                    double baseline = node.getBaselineOffset();
                    if (baseline == Double.NEGATIVE_INFINITY) {
                        baseline = node.getLayoutBounds().getHeight();
                    }
                    double width = computeChildPrefAreaWidth(node, null);
                    double height = computeChildPrefAreaHeight(node, null);
                    spans[i2] = new EmbeddedSpan(node, baseline, width, height);
                }
            }
            this.layout.setContent(spans);
            this.needsContent = false;
        }
        return this.layout;
    }

    public final void setTextAlignment(TextAlignment value) {
        textAlignmentProperty().set(value);
    }

    public final TextAlignment getTextAlignment() {
        return this.textAlignment == null ? TextAlignment.LEFT : this.textAlignment.get();
    }

    public final ObjectProperty<TextAlignment> textAlignmentProperty() {
        if (this.textAlignment == null) {
            this.textAlignment = new StyleableObjectProperty<TextAlignment>(TextAlignment.LEFT) { // from class: javafx.scene.text.TextFlow.1
                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TextFlow.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "textAlignment";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<TextFlow, TextAlignment> getCssMetaData() {
                    return StyleableProperties.TEXT_ALIGNMENT;
                }

                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    TextAlignment align = get();
                    if (align == null) {
                        align = TextAlignment.LEFT;
                    }
                    TextLayout layout = TextFlow.this.getTextLayout();
                    layout.setAlignment(align.ordinal());
                    TextFlow.this.requestLayout();
                }
            };
        }
        return this.textAlignment;
    }

    public final void setLineSpacing(double spacing) {
        lineSpacingProperty().set(spacing);
    }

    public final double getLineSpacing() {
        if (this.lineSpacing == null) {
            return 0.0d;
        }
        return this.lineSpacing.get();
    }

    public final DoubleProperty lineSpacingProperty() {
        if (this.lineSpacing == null) {
            this.lineSpacing = new StyleableDoubleProperty(0.0d) { // from class: javafx.scene.text.TextFlow.2
                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TextFlow.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "lineSpacing";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.LINE_SPACING;
                }

                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    TextLayout layout = TextFlow.this.getTextLayout();
                    if (layout.setLineSpacing((float) get())) {
                        TextFlow.this.requestLayout();
                    }
                }
            };
        }
        return this.lineSpacing;
    }

    @Override // javafx.scene.Parent, javafx.scene.Node
    public final double getBaselineOffset() {
        Insets insets = getInsets();
        double top = snapSpace(insets.getTop());
        return top - getTextLayout().getBounds().getMinY();
    }

    /* loaded from: jfxrt.jar:javafx/scene/text/TextFlow$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<TextFlow, TextAlignment> TEXT_ALIGNMENT = new CssMetaData<TextFlow, TextAlignment>("-fx-text-alignment", new EnumConverter(TextAlignment.class), TextAlignment.LEFT) { // from class: javafx.scene.text.TextFlow.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(TextFlow node) {
                return node.textAlignment == null || !node.textAlignment.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<TextAlignment> getStyleableProperty(TextFlow node) {
                return (StyleableProperty) node.textAlignmentProperty();
            }
        };
        private static final CssMetaData<TextFlow, Number> LINE_SPACING = new CssMetaData<TextFlow, Number>("-fx-line-spacing", SizeConverter.getInstance(), 0) { // from class: javafx.scene.text.TextFlow.StyleableProperties.2
            @Override // javafx.css.CssMetaData
            public boolean isSettable(TextFlow node) {
                return node.lineSpacing == null || !node.lineSpacing.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(TextFlow node) {
                return (StyleableProperty) node.lineSpacingProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Pane.getClassCssMetaData());
            styleables.add(TEXT_ALIGNMENT);
            styleables.add(LINE_SPACING);
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

    private static double snapSpace(double value, boolean snapToPixel) {
        return snapToPixel ? Math.round(value) : value;
    }

    static double boundedSize(double min, double pref, double max) {
        double a2 = pref >= min ? pref : min;
        double b2 = min >= max ? min : max;
        return a2 <= b2 ? a2 : b2;
    }

    double computeChildPrefAreaWidth(Node child, Insets margin) {
        return computeChildPrefAreaWidth(child, margin, -1.0d);
    }

    double computeChildPrefAreaWidth(Node child, Insets margin, double height) {
        boolean snap = isSnapToPixel();
        double top = margin != null ? snapSpace(margin.getTop(), snap) : 0.0d;
        double bottom = margin != null ? snapSpace(margin.getBottom(), snap) : 0.0d;
        double left = margin != null ? snapSpace(margin.getLeft(), snap) : 0.0d;
        double right = margin != null ? snapSpace(margin.getRight(), snap) : 0.0d;
        double alt = -1.0d;
        if (child.getContentBias() == Orientation.VERTICAL) {
            alt = snapSize(boundedSize(child.minHeight(-1.0d), height != -1.0d ? (height - top) - bottom : child.prefHeight(-1.0d), child.maxHeight(-1.0d)));
        }
        return left + snapSize(boundedSize(child.minWidth(alt), child.prefWidth(alt), child.maxWidth(alt))) + right;
    }

    double computeChildPrefAreaHeight(Node child, Insets margin) {
        return computeChildPrefAreaHeight(child, margin, -1.0d);
    }

    double computeChildPrefAreaHeight(Node child, Insets margin, double width) {
        boolean snap = isSnapToPixel();
        double top = margin != null ? snapSpace(margin.getTop(), snap) : 0.0d;
        double bottom = margin != null ? snapSpace(margin.getBottom(), snap) : 0.0d;
        double left = margin != null ? snapSpace(margin.getLeft(), snap) : 0.0d;
        double right = margin != null ? snapSpace(margin.getRight(), snap) : 0.0d;
        double alt = -1.0d;
        if (child.getContentBias() == Orientation.HORIZONTAL) {
            alt = snapSize(boundedSize(child.minWidth(-1.0d), width != -1.0d ? (width - left) - right : child.prefWidth(-1.0d), child.maxWidth(-1.0d)));
        }
        return top + snapSize(boundedSize(child.minHeight(alt), child.prefHeight(alt), child.maxHeight(alt))) + bottom;
    }

    @Override // javafx.scene.Parent, javafx.scene.Node
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case TEXT:
                String accText = getAccessibleText();
                if (accText != null && !accText.isEmpty()) {
                    return accText;
                }
                StringBuilder title = new StringBuilder();
                for (Node node : getChildren()) {
                    Object text = node.queryAccessibleAttribute(AccessibleAttribute.TEXT, parameters);
                    if (text != null) {
                        title.append(text.toString());
                    }
                }
                return title.toString();
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }
}
