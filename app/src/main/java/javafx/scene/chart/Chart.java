package javafx.scene.chart;

import com.sun.javafx.charts.ChartLayoutAnimator;
import com.sun.javafx.charts.Legend;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.scene.control.skin.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

/* loaded from: jfxrt.jar:javafx/scene/chart/Chart.class */
public abstract class Chart extends Region {
    private static final int MIN_WIDTH_TO_LEAVE_FOR_CHART_CONTENT = 200;
    private static final int MIN_HEIGHT_TO_LEAVE_FOR_CHART_CONTENT = 150;
    private final Label titleLabel = new Label();
    private final Pane chartContent = new Pane() { // from class: javafx.scene.chart.Chart.1
        @Override // javafx.scene.Parent
        protected void layoutChildren() {
            double top = snappedTopInset();
            double left = snappedLeftInset();
            double bottom = snappedBottomInset();
            double right = snappedRightInset();
            double width = getWidth();
            double height = getHeight();
            double contentWidth = snapSize(width - (left + right));
            double contentHeight = snapSize(height - (top + bottom));
            Chart.this.layoutChartChildren(snapPosition(top), snapPosition(left), contentWidth, contentHeight);
        }

        @Override // javafx.scene.Node
        public boolean usesMirroring() {
            return Chart.this.useChartContentMirroring;
        }
    };
    boolean useChartContentMirroring = true;
    private final ChartLayoutAnimator animator = new ChartLayoutAnimator(this.chartContent);
    private StringProperty title = new StringPropertyBase() { // from class: javafx.scene.chart.Chart.2
        @Override // javafx.beans.property.StringPropertyBase
        protected void invalidated() {
            Chart.this.titleLabel.setText(get());
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Chart.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "title";
        }
    };
    private ObjectProperty<Side> titleSide = new StyleableObjectProperty<Side>(Side.TOP) { // from class: javafx.scene.chart.Chart.3
        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            Chart.this.requestLayout();
        }

        @Override // javafx.css.StyleableProperty
        public CssMetaData<Chart, Side> getCssMetaData() {
            return StyleableProperties.TITLE_SIDE;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Chart.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "titleSide";
        }
    };
    private final ObjectProperty<Node> legend = new ObjectPropertyBase<Node>() { // from class: javafx.scene.chart.Chart.4
        private Node old = null;

        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            Node newLegend = get();
            if (this.old != null) {
                Chart.this.getChildren().remove(this.old);
            }
            if (newLegend != null) {
                Chart.this.getChildren().add(newLegend);
                newLegend.setVisible(Chart.this.isLegendVisible());
            }
            this.old = newLegend;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Chart.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "legend";
        }
    };
    private final BooleanProperty legendVisible = new StyleableBooleanProperty(true) { // from class: javafx.scene.chart.Chart.5
        @Override // javafx.beans.property.BooleanPropertyBase
        protected void invalidated() {
            Chart.this.requestLayout();
        }

        @Override // javafx.css.StyleableProperty
        public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
            return StyleableProperties.LEGEND_VISIBLE;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Chart.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "legendVisible";
        }
    };
    private ObjectProperty<Side> legendSide = new StyleableObjectProperty<Side>(Side.BOTTOM) { // from class: javafx.scene.chart.Chart.6
        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            Side legendSide = get();
            Node legend = Chart.this.getLegend();
            if (legend instanceof Legend) {
                ((Legend) legend).setVertical(Side.LEFT.equals(legendSide) || Side.RIGHT.equals(legendSide));
            }
            Chart.this.requestLayout();
        }

        @Override // javafx.css.StyleableProperty
        public CssMetaData<Chart, Side> getCssMetaData() {
            return StyleableProperties.LEGEND_SIDE;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Chart.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "legendSide";
        }
    };
    private BooleanProperty animated = new SimpleBooleanProperty(this, "animated", true);

    protected abstract void layoutChartChildren(double d2, double d3, double d4, double d5);

    public final String getTitle() {
        return this.title.get();
    }

    public final void setTitle(String value) {
        this.title.set(value);
    }

    public final StringProperty titleProperty() {
        return this.title;
    }

    public final Side getTitleSide() {
        return this.titleSide.get();
    }

    public final void setTitleSide(Side value) {
        this.titleSide.set(value);
    }

    public final ObjectProperty<Side> titleSideProperty() {
        return this.titleSide;
    }

    protected final Node getLegend() {
        return this.legend.getValue2();
    }

    protected final void setLegend(Node value) {
        this.legend.setValue(value);
    }

    protected final ObjectProperty<Node> legendProperty() {
        return this.legend;
    }

    public final boolean isLegendVisible() {
        return this.legendVisible.getValue2().booleanValue();
    }

    public final void setLegendVisible(boolean value) {
        this.legendVisible.setValue(Boolean.valueOf(value));
    }

    public final BooleanProperty legendVisibleProperty() {
        return this.legendVisible;
    }

    public final Side getLegendSide() {
        return this.legendSide.get();
    }

    public final void setLegendSide(Side value) {
        this.legendSide.set(value);
    }

    public final ObjectProperty<Side> legendSideProperty() {
        return this.legendSide;
    }

    public final boolean getAnimated() {
        return this.animated.get();
    }

    public final void setAnimated(boolean value) {
        this.animated.set(value);
    }

    public final BooleanProperty animatedProperty() {
        return this.animated;
    }

    protected ObservableList<Node> getChartChildren() {
        return this.chartContent.getChildren();
    }

    public Chart() {
        this.titleLabel.setAlignment(Pos.CENTER);
        this.titleLabel.focusTraversableProperty().bind(Platform.accessibilityActiveProperty());
        getChildren().addAll(this.titleLabel, this.chartContent);
        getStyleClass().add("chart");
        this.titleLabel.getStyleClass().add("chart-title");
        this.chartContent.getStyleClass().add("chart-content");
        this.chartContent.setManaged(false);
    }

    void animate(KeyFrame... keyFrames) {
        this.animator.animate(keyFrames);
    }

    protected void animate(Animation animation) {
        this.animator.animate(animation);
    }

    protected void requestChartLayout() {
        this.chartContent.requestLayout();
    }

    protected final boolean shouldAnimate() {
        return getAnimated() && impl_isTreeVisible() && getScene() != null;
    }

    @Override // javafx.scene.Parent
    protected void layoutChildren() {
        double top = snappedTopInset();
        double left = snappedLeftInset();
        double bottom = snappedBottomInset();
        double right = snappedRightInset();
        double width = getWidth();
        double height = getHeight();
        if (getTitle() != null) {
            this.titleLabel.setVisible(true);
            if (getTitleSide().equals(Side.TOP)) {
                double titleHeight = snapSize(this.titleLabel.prefHeight((width - left) - right));
                this.titleLabel.resizeRelocate(left, top, (width - left) - right, titleHeight);
                top += titleHeight;
            } else if (getTitleSide().equals(Side.BOTTOM)) {
                double titleHeight2 = snapSize(this.titleLabel.prefHeight((width - left) - right));
                this.titleLabel.resizeRelocate(left, (height - bottom) - titleHeight2, (width - left) - right, titleHeight2);
                bottom += titleHeight2;
            } else if (getTitleSide().equals(Side.LEFT)) {
                double titleWidth = snapSize(this.titleLabel.prefWidth((height - top) - bottom));
                this.titleLabel.resizeRelocate(left, top, titleWidth, (height - top) - bottom);
                left += titleWidth;
            } else if (getTitleSide().equals(Side.RIGHT)) {
                double titleWidth2 = snapSize(this.titleLabel.prefWidth((height - top) - bottom));
                this.titleLabel.resizeRelocate((width - right) - titleWidth2, top, titleWidth2, (height - top) - bottom);
                right += titleWidth2;
            }
        } else {
            this.titleLabel.setVisible(false);
        }
        Node legend = getLegend();
        if (legend != null) {
            boolean shouldShowLegend = isLegendVisible();
            if (shouldShowLegend) {
                if (getLegendSide() == Side.TOP) {
                    double legendHeight = snapSize(legend.prefHeight((width - left) - right));
                    double legendWidth = Utils.boundedSize(snapSize(legend.prefWidth(legendHeight)), 0.0d, (width - left) - right);
                    legend.resizeRelocate(left + ((((width - left) - right) - legendWidth) / 2.0d), top, legendWidth, legendHeight);
                    if (((height - bottom) - top) - legendHeight < 150.0d) {
                        shouldShowLegend = false;
                    } else {
                        top += legendHeight;
                    }
                } else if (getLegendSide() == Side.BOTTOM) {
                    double legendHeight2 = snapSize(legend.prefHeight((width - left) - right));
                    double legendWidth2 = Utils.boundedSize(snapSize(legend.prefWidth(legendHeight2)), 0.0d, (width - left) - right);
                    legend.resizeRelocate(left + ((((width - left) - right) - legendWidth2) / 2.0d), (height - bottom) - legendHeight2, legendWidth2, legendHeight2);
                    if (((height - bottom) - top) - legendHeight2 < 150.0d) {
                        shouldShowLegend = false;
                    } else {
                        bottom += legendHeight2;
                    }
                } else if (getLegendSide() == Side.LEFT) {
                    double legendWidth3 = snapSize(legend.prefWidth((height - top) - bottom));
                    double legendHeight3 = Utils.boundedSize(snapSize(legend.prefHeight(legendWidth3)), 0.0d, (height - top) - bottom);
                    legend.resizeRelocate(left, top + ((((height - top) - bottom) - legendHeight3) / 2.0d), legendWidth3, legendHeight3);
                    if (((width - left) - right) - legendWidth3 < 200.0d) {
                        shouldShowLegend = false;
                    } else {
                        left += legendWidth3;
                    }
                } else if (getLegendSide() == Side.RIGHT) {
                    double legendWidth4 = snapSize(legend.prefWidth((height - top) - bottom));
                    double legendHeight4 = Utils.boundedSize(snapSize(legend.prefHeight(legendWidth4)), 0.0d, (height - top) - bottom);
                    legend.resizeRelocate((width - right) - legendWidth4, top + ((((height - top) - bottom) - legendHeight4) / 2.0d), legendWidth4, legendHeight4);
                    if (((width - left) - right) - legendWidth4 < 200.0d) {
                        shouldShowLegend = false;
                    } else {
                        right += legendWidth4;
                    }
                }
            }
            legend.setVisible(shouldShowLegend);
        }
        this.chartContent.resizeRelocate(left, top, (width - left) - right, (height - top) - bottom);
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computeMinHeight(double width) {
        return 150.0d;
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computeMinWidth(double height) {
        return 200.0d;
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefWidth(double height) {
        return 500.0d;
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefHeight(double width) {
        return 400.0d;
    }

    /* loaded from: jfxrt.jar:javafx/scene/chart/Chart$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<Chart, Side> TITLE_SIDE = new CssMetaData<Chart, Side>("-fx-title-side", new EnumConverter(Side.class), Side.TOP) { // from class: javafx.scene.chart.Chart.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Chart node) {
                return node.titleSide == null || !node.titleSide.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Side> getStyleableProperty(Chart node) {
                return (StyleableProperty) node.titleSideProperty();
            }
        };
        private static final CssMetaData<Chart, Side> LEGEND_SIDE = new CssMetaData<Chart, Side>("-fx-legend-side", new EnumConverter(Side.class), Side.BOTTOM) { // from class: javafx.scene.chart.Chart.StyleableProperties.2
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Chart node) {
                return node.legendSide == null || !node.legendSide.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Side> getStyleableProperty(Chart node) {
                return (StyleableProperty) node.legendSideProperty();
            }
        };
        private static final CssMetaData<Chart, Boolean> LEGEND_VISIBLE = new CssMetaData<Chart, Boolean>("-fx-legend-visible", BooleanConverter.getInstance(), Boolean.TRUE) { // from class: javafx.scene.chart.Chart.StyleableProperties.3
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Chart node) {
                return node.legendVisible == null || !node.legendVisible.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(Chart node) {
                return (StyleableProperty) node.legendVisibleProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Region.getClassCssMetaData());
            styleables.add(TITLE_SIDE);
            styleables.add(LEGEND_VISIBLE);
            styleables.add(LEGEND_SIDE);
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
