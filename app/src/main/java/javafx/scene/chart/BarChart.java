package javafx.scene.chart;

import com.sun.javafx.charts.Legend;
import com.sun.javafx.css.converters.SizeConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:javafx/scene/chart/BarChart.class */
public class BarChart<X, Y> extends XYChart<X, Y> {
    private Map<XYChart.Series<X, Y>, Map<String, XYChart.Data<X, Y>>> seriesCategoryMap;
    private Legend legend;
    private final Orientation orientation;
    private CategoryAxis categoryAxis;
    private ValueAxis valueAxis;
    private Timeline dataRemoveTimeline;
    private double bottomPos;
    private ParallelTransition pt;
    private Map<XYChart.Data<X, Y>, Double> XYValueMap;
    private DoubleProperty barGap;
    private DoubleProperty categoryGap;
    private static String NEGATIVE_STYLE = "negative";
    private static final PseudoClass VERTICAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("vertical");
    private static final PseudoClass HORIZONTAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("horizontal");

    public final double getBarGap() {
        return this.barGap.getValue2().doubleValue();
    }

    public final void setBarGap(double value) {
        this.barGap.setValue((Number) Double.valueOf(value));
    }

    public final DoubleProperty barGapProperty() {
        return this.barGap;
    }

    public final double getCategoryGap() {
        return this.categoryGap.getValue2().doubleValue();
    }

    public final void setCategoryGap(double value) {
        this.categoryGap.setValue((Number) Double.valueOf(value));
    }

    public final DoubleProperty categoryGapProperty() {
        return this.categoryGap;
    }

    public BarChart(@NamedArg("xAxis") Axis<X> xAxis, @NamedArg("yAxis") Axis<Y> yAxis) {
        this(xAxis, yAxis, FXCollections.observableArrayList());
    }

    public BarChart(@NamedArg("xAxis") Axis<X> xAxis, @NamedArg("yAxis") Axis<Y> yAxis, @NamedArg("data") ObservableList<XYChart.Series<X, Y>> data) {
        super(xAxis, yAxis);
        this.seriesCategoryMap = new HashMap();
        this.legend = new Legend();
        this.bottomPos = 0.0d;
        this.XYValueMap = new HashMap();
        this.barGap = new StyleableDoubleProperty(4.0d) { // from class: javafx.scene.chart.BarChart.1
            @Override // javafx.beans.property.DoublePropertyBase
            protected void invalidated() {
                get();
                BarChart.this.requestChartLayout();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return BarChart.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "barGap";
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                return StyleableProperties.BAR_GAP;
            }
        };
        this.categoryGap = new StyleableDoubleProperty(10.0d) { // from class: javafx.scene.chart.BarChart.2
            @Override // javafx.beans.property.DoublePropertyBase
            protected void invalidated() {
                get();
                BarChart.this.requestChartLayout();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return BarChart.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "categoryGap";
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                return StyleableProperties.CATEGORY_GAP;
            }
        };
        getStyleClass().add("bar-chart");
        setLegend(this.legend);
        if ((!(xAxis instanceof ValueAxis) || !(yAxis instanceof CategoryAxis)) && (!(yAxis instanceof ValueAxis) || !(xAxis instanceof CategoryAxis))) {
            throw new IllegalArgumentException("Axis type incorrect, one of X,Y should be CategoryAxis and the other NumberAxis");
        }
        if (xAxis instanceof CategoryAxis) {
            this.categoryAxis = (CategoryAxis) xAxis;
            this.valueAxis = (ValueAxis) yAxis;
            this.orientation = Orientation.VERTICAL;
        } else {
            this.categoryAxis = (CategoryAxis) yAxis;
            this.valueAxis = (ValueAxis) xAxis;
            this.orientation = Orientation.HORIZONTAL;
        }
        pseudoClassStateChanged(HORIZONTAL_PSEUDOCLASS_STATE, this.orientation == Orientation.HORIZONTAL);
        pseudoClassStateChanged(VERTICAL_PSEUDOCLASS_STATE, this.orientation == Orientation.VERTICAL);
        setData(data);
    }

    public BarChart(@NamedArg("xAxis") Axis<X> xAxis, @NamedArg("yAxis") Axis<Y> yAxis, @NamedArg("data") ObservableList<XYChart.Series<X, Y>> data, @NamedArg("categoryGap") double categoryGap) {
        this(xAxis, yAxis);
        setData(data);
        setCategoryGap(categoryGap);
    }

    @Override // javafx.scene.chart.XYChart
    protected void dataItemAdded(XYChart.Series<X, Y> series, int i2, XYChart.Data<X, Y> data) {
        String str;
        if (this.orientation == Orientation.VERTICAL) {
            str = (String) data.getXValue();
        } else {
            str = (String) data.getYValue();
        }
        Map<String, XYChart.Data<X, Y>> map = this.seriesCategoryMap.get(series);
        if (map == null) {
            map = new HashMap();
            this.seriesCategoryMap.put(series, map);
        }
        if (!this.categoryAxis.getCategories().contains(str)) {
            this.categoryAxis.getCategories().add(i2, str);
        } else if (map.containsKey(str)) {
            XYChart.Data<X, Y> data2 = map.get(str);
            getPlotChildren().remove(data2.getNode());
            removeDataItemFromDisplay(series, data2);
            requestChartLayout();
            map.remove(str);
        }
        map.put(str, data);
        Node nodeCreateBar = createBar(series, getData().indexOf(series), data, i2);
        if (shouldAnimate()) {
            animateDataAdd(data, nodeCreateBar);
        } else {
            getPlotChildren().add(nodeCreateBar);
        }
    }

    @Override // javafx.scene.chart.XYChart
    protected void dataItemRemoved(XYChart.Data<X, Y> item, XYChart.Series<X, Y> series) {
        Node bar = item.getNode();
        if (bar != null) {
            bar.focusTraversableProperty().unbind();
        }
        if (shouldAnimate()) {
            this.XYValueMap.clear();
            this.dataRemoveTimeline = createDataRemoveTimeline(item, bar, series);
            this.dataRemoveTimeline.setOnFinished(event -> {
                item.setSeries(null);
                removeDataItemFromDisplay(series, item);
            });
            this.dataRemoveTimeline.play();
            return;
        }
        processDataRemove(series, item);
        removeDataItemFromDisplay(series, item);
    }

    @Override // javafx.scene.chart.XYChart
    protected void dataItemChanged(XYChart.Data<X, Y> data) {
        double dDoubleValue;
        double dDoubleValue2;
        if (this.orientation == Orientation.VERTICAL) {
            dDoubleValue = ((Number) data.getYValue()).doubleValue();
            dDoubleValue2 = ((Number) data.getCurrentY()).doubleValue();
        } else {
            dDoubleValue = ((Number) data.getXValue()).doubleValue();
            dDoubleValue2 = ((Number) data.getCurrentX()).doubleValue();
        }
        if (dDoubleValue2 > 0.0d && dDoubleValue < 0.0d) {
            data.getNode().getStyleClass().add(NEGATIVE_STYLE);
        } else if (dDoubleValue2 < 0.0d && dDoubleValue > 0.0d) {
            data.getNode().getStyleClass().remove(NEGATIVE_STYLE);
        }
    }

    @Override // javafx.scene.chart.XYChart
    protected void seriesAdded(XYChart.Series<X, Y> series, int i2) {
        String str;
        HashMap map = new HashMap();
        for (int i3 = 0; i3 < series.getData().size(); i3++) {
            XYChart.Data<X, Y> data = series.getData().get(i3);
            Node nodeCreateBar = createBar(series, i2, data, i3);
            if (this.orientation == Orientation.VERTICAL) {
                str = (String) data.getXValue();
            } else {
                str = (String) data.getYValue();
            }
            map.put(str, data);
            if (shouldAnimate()) {
                animateDataAdd(data, nodeCreateBar);
            } else {
                if ((this.orientation == Orientation.VERTICAL ? ((Number) data.getYValue()).doubleValue() : ((Number) data.getXValue()).doubleValue()) < 0.0d) {
                    nodeCreateBar.getStyleClass().add(NEGATIVE_STYLE);
                }
                getPlotChildren().add(nodeCreateBar);
            }
        }
        if (map.size() > 0) {
            this.seriesCategoryMap.put(series, map);
        }
    }

    @Override // javafx.scene.chart.XYChart
    protected void seriesRemoved(XYChart.Series<X, Y> series) {
        updateDefaultColorIndex(series);
        if (shouldAnimate()) {
            this.pt = new ParallelTransition();
            this.pt.setOnFinished(event -> {
                removeSeriesFromDisplay(series);
            });
            boolean lastSeries = getSeriesSize() <= 1;
            this.XYValueMap.clear();
            for (XYChart.Data<X, Y> d2 : series.getData()) {
                Node bar = d2.getNode();
                if (!lastSeries) {
                    Timeline t2 = createDataRemoveTimeline(d2, bar, series);
                    this.pt.getChildren().add(t2);
                } else {
                    FadeTransition ft = new FadeTransition(Duration.millis(700.0d), bar);
                    ft.setFromValue(1.0d);
                    ft.setToValue(0.0d);
                    ft.setOnFinished(actionEvent -> {
                        processDataRemove(series, d2);
                        bar.setOpacity(1.0d);
                    });
                    this.pt.getChildren().add(ft);
                }
            }
            this.pt.play();
            return;
        }
        for (XYChart.Data<X, Y> d3 : series.getData()) {
            getPlotChildren().remove(d3.getNode());
            updateMap(series, d3);
        }
        removeSeriesFromDisplay(series);
    }

    @Override // javafx.scene.chart.XYChart
    protected void layoutPlotChildren() {
        double categoryPos;
        double valPos;
        double catSpace = this.categoryAxis.getCategorySpacing();
        double avilableBarSpace = catSpace - (getCategoryGap() + getBarGap());
        double barWidth = (avilableBarSpace / getSeriesSize()) - getBarGap();
        double barOffset = -((catSpace - getCategoryGap()) / 2.0d);
        double zeroPos = this.valueAxis.getLowerBound() > 0.0d ? this.valueAxis.getDisplayPosition((ValueAxis) Double.valueOf(this.valueAxis.getLowerBound())) : this.valueAxis.getZeroPosition();
        if (barWidth <= 0.0d) {
            barWidth = 1.0d;
        }
        int catIndex = 0;
        for (String category : this.categoryAxis.getCategories()) {
            int index = 0;
            Iterator<XYChart.Series<X, Y>> sit = getDisplayedSeriesIterator();
            while (sit.hasNext()) {
                XYChart.Series<X, Y> series = sit.next();
                XYChart.Data<X, Y> item = getDataItem(series, index, catIndex, category);
                if (item != null) {
                    Node bar = item.getNode();
                    if (this.orientation == Orientation.VERTICAL) {
                        categoryPos = getXAxis().getDisplayPosition(item.getCurrentX());
                        valPos = getYAxis().getDisplayPosition(item.getCurrentY());
                    } else {
                        categoryPos = getYAxis().getDisplayPosition(item.getCurrentY());
                        valPos = getXAxis().getDisplayPosition(item.getCurrentX());
                    }
                    if (!Double.isNaN(categoryPos) && !Double.isNaN(valPos)) {
                        double bottom = Math.min(valPos, zeroPos);
                        double top = Math.max(valPos, zeroPos);
                        this.bottomPos = bottom;
                        if (this.orientation == Orientation.VERTICAL) {
                            bar.resizeRelocate(categoryPos + barOffset + ((barWidth + getBarGap()) * index), bottom, barWidth, top - bottom);
                        } else {
                            bar.resizeRelocate(bottom, categoryPos + barOffset + ((barWidth + getBarGap()) * index), top - bottom, barWidth);
                        }
                        index++;
                    }
                }
            }
            catIndex++;
        }
    }

    @Override // javafx.scene.chart.XYChart
    protected void updateLegend() {
        this.legend.getItems().clear();
        if (getData() != null) {
            for (int seriesIndex = 0; seriesIndex < getData().size(); seriesIndex++) {
                XYChart.Series<X, Y> series = getData().get(seriesIndex);
                Legend.LegendItem legenditem = new Legend.LegendItem(series.getName());
                legenditem.getSymbol().getStyleClass().addAll("chart-bar", "series" + seriesIndex, "bar-legend-symbol", series.defaultColorStyleClass);
                this.legend.getItems().add(legenditem);
            }
        }
        if (this.legend.getItems().size() > 0) {
            if (getLegend() == null) {
                setLegend(this.legend);
                return;
            }
            return;
        }
        setLegend(null);
    }

    private void updateMap(XYChart.Series<X, Y> series, XYChart.Data<X, Y> data) {
        String str = this.orientation == Orientation.VERTICAL ? (String) data.getXValue() : (String) data.getYValue();
        Map<String, XYChart.Data<X, Y>> map = this.seriesCategoryMap.get(series);
        if (map != null) {
            map.remove(str);
            if (map.isEmpty()) {
                this.seriesCategoryMap.remove(series);
            }
        }
        if (!this.seriesCategoryMap.isEmpty() || !this.categoryAxis.isAutoRanging()) {
            return;
        }
        this.categoryAxis.getCategories().clear();
    }

    private void processDataRemove(XYChart.Series<X, Y> series, XYChart.Data<X, Y> item) {
        Node bar = item.getNode();
        getPlotChildren().remove(bar);
        updateMap(series, item);
    }

    private void animateDataAdd(XYChart.Data<X, Y> data, Node node) {
        if (this.orientation == Orientation.VERTICAL) {
            double dDoubleValue = ((Number) data.getYValue()).doubleValue();
            if (dDoubleValue < 0.0d) {
                node.getStyleClass().add(NEGATIVE_STYLE);
            }
            data.setCurrentY(getYAxis().toRealValue(dDoubleValue < 0.0d ? -this.bottomPos : this.bottomPos));
            getPlotChildren().add(node);
            data.setYValue(getYAxis().toRealValue(dDoubleValue));
            animate(new KeyFrame(Duration.ZERO, new KeyValue(data.currentYProperty(), data.getCurrentY())), new KeyFrame(Duration.millis(700.0d), new KeyValue(data.currentYProperty(), data.getYValue(), Interpolator.EASE_BOTH)));
            return;
        }
        double dDoubleValue2 = ((Number) data.getXValue()).doubleValue();
        if (dDoubleValue2 < 0.0d) {
            node.getStyleClass().add(NEGATIVE_STYLE);
        }
        data.setCurrentX(getXAxis().toRealValue(dDoubleValue2 < 0.0d ? -this.bottomPos : this.bottomPos));
        getPlotChildren().add(node);
        data.setXValue(getXAxis().toRealValue(dDoubleValue2));
        animate(new KeyFrame(Duration.ZERO, new KeyValue(data.currentXProperty(), data.getCurrentX())), new KeyFrame(Duration.millis(700.0d), new KeyValue(data.currentXProperty(), data.getXValue(), Interpolator.EASE_BOTH)));
    }

    private Timeline createDataRemoveTimeline(XYChart.Data<X, Y> data, Node node, XYChart.Series<X, Y> series) {
        Timeline timeline = new Timeline();
        if (this.orientation == Orientation.VERTICAL) {
            this.XYValueMap.put(data, Double.valueOf(((Number) data.getYValue()).doubleValue()));
            data.setYValue(getYAxis().toRealValue(this.bottomPos));
            timeline.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(data.currentYProperty(), data.getCurrentY())), new KeyFrame(Duration.millis(700.0d), (EventHandler<ActionEvent>) actionEvent -> {
                processDataRemove(series, data);
                this.XYValueMap.clear();
            }, new KeyValue(data.currentYProperty(), data.getYValue(), Interpolator.EASE_BOTH)));
        } else {
            this.XYValueMap.put(data, Double.valueOf(((Number) data.getXValue()).doubleValue()));
            data.setXValue(getXAxis().toRealValue(getXAxis().getZeroPosition()));
            timeline.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(data.currentXProperty(), data.getCurrentX())), new KeyFrame(Duration.millis(700.0d), (EventHandler<ActionEvent>) actionEvent2 -> {
                processDataRemove(series, data);
                this.XYValueMap.clear();
            }, new KeyValue(data.currentXProperty(), data.getXValue(), Interpolator.EASE_BOTH)));
        }
        return timeline;
    }

    @Override // javafx.scene.chart.XYChart
    void dataBeingRemovedIsAdded(XYChart.Data<X, Y> item, XYChart.Series<X, Y> series) {
        if (this.dataRemoveTimeline != null) {
            this.dataRemoveTimeline.setOnFinished(null);
            this.dataRemoveTimeline.stop();
        }
        processDataRemove(series, item);
        item.setSeries(null);
        removeDataItemFromDisplay(series, item);
        restoreDataValues(item);
        this.XYValueMap.clear();
    }

    private void restoreDataValues(XYChart.Data item) {
        Double value = this.XYValueMap.get(item);
        if (value != null) {
            if (this.orientation.equals(Orientation.VERTICAL)) {
                item.setYValue(value);
                item.setCurrentY(value);
            } else {
                item.setXValue(value);
                item.setCurrentX(value);
            }
        }
    }

    @Override // javafx.scene.chart.XYChart
    void seriesBeingRemovedIsAdded(XYChart.Series<X, Y> series) {
        boolean lastSeries = this.pt.getChildren().size() == 1;
        if (this.pt != null) {
            if (!this.pt.getChildren().isEmpty()) {
                for (Animation a2 : this.pt.getChildren()) {
                    a2.setOnFinished(null);
                }
            }
            for (XYChart.Data<X, Y> item : series.getData()) {
                processDataRemove(series, item);
                if (!lastSeries) {
                    restoreDataValues(item);
                }
            }
            this.XYValueMap.clear();
            this.pt.setOnFinished(null);
            this.pt.getChildren().clear();
            this.pt.stop();
            removeSeriesFromDisplay(series);
        }
    }

    private void updateDefaultColorIndex(XYChart.Series<X, Y> series) {
        int clearIndex = this.seriesColorMap.get(series).intValue();
        for (XYChart.Data<X, Y> d2 : series.getData()) {
            Node bar = d2.getNode();
            if (bar != null) {
                bar.getStyleClass().remove(DEFAULT_COLOR + clearIndex);
            }
        }
    }

    private Node createBar(XYChart.Series<X, Y> series, int seriesIndex, XYChart.Data<X, Y> item, int itemIndex) {
        Node bar = item.getNode();
        if (bar == null) {
            bar = new StackPane();
            bar.setAccessibleRole(AccessibleRole.TEXT);
            bar.setAccessibleRoleDescription("Bar");
            bar.focusTraversableProperty().bind(Platform.accessibilityActiveProperty());
            item.setNode(bar);
        }
        bar.getStyleClass().addAll("chart-bar", "series" + seriesIndex, "data" + itemIndex, series.defaultColorStyleClass);
        return bar;
    }

    private XYChart.Data<X, Y> getDataItem(XYChart.Series<X, Y> series, int seriesIndex, int itemIndex, String category) {
        Map<String, XYChart.Data<X, Y>> catmap = this.seriesCategoryMap.get(series);
        if (catmap != null) {
            return catmap.get(category);
        }
        return null;
    }

    /* loaded from: jfxrt.jar:javafx/scene/chart/BarChart$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<BarChart<?, ?>, Number> BAR_GAP = new CssMetaData<BarChart<?, ?>, Number>("-fx-bar-gap", SizeConverter.getInstance(), Double.valueOf(4.0d)) { // from class: javafx.scene.chart.BarChart.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(BarChart<?, ?> node) {
                return ((BarChart) node).barGap == null || !((BarChart) node).barGap.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(BarChart<?, ?> node) {
                return (StyleableProperty) node.barGapProperty();
            }
        };
        private static final CssMetaData<BarChart<?, ?>, Number> CATEGORY_GAP = new CssMetaData<BarChart<?, ?>, Number>("-fx-category-gap", SizeConverter.getInstance(), Double.valueOf(10.0d)) { // from class: javafx.scene.chart.BarChart.StyleableProperties.2
            @Override // javafx.css.CssMetaData
            public boolean isSettable(BarChart<?, ?> node) {
                return ((BarChart) node).categoryGap == null || !((BarChart) node).categoryGap.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(BarChart<?, ?> node) {
                return (StyleableProperty) node.categoryGapProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(XYChart.getClassCssMetaData());
            styleables.add(BAR_GAP);
            styleables.add(CATEGORY_GAP);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override // javafx.scene.chart.XYChart, javafx.scene.chart.Chart, javafx.scene.layout.Region, javafx.scene.Node, javafx.css.Styleable
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }
}
