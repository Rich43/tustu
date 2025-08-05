package javafx.scene.chart;

import com.sun.javafx.charts.Legend;
import com.sun.javafx.css.converters.BooleanConverter;
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
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.StrokeLineJoin;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:javafx/scene/chart/LineChart.class */
public class LineChart<X, Y> extends XYChart<X, Y> {
    private Map<XYChart.Series<X, Y>, DoubleProperty> seriesYMultiplierMap;
    private Legend legend;
    private Timeline dataRemoveTimeline;
    private XYChart.Series<X, Y> seriesOfDataRemoved;
    private XYChart.Data<X, Y> dataItemBeingRemoved;
    private FadeTransition fadeSymbolTransition;
    private Map<XYChart.Data<X, Y>, Double> XYValueMap;
    private Timeline seriesRemoveTimeline;
    private BooleanProperty createSymbols;
    private ObjectProperty<SortingPolicy> axisSortingPolicy;

    /* loaded from: jfxrt.jar:javafx/scene/chart/LineChart$SortingPolicy.class */
    public enum SortingPolicy {
        NONE,
        X_AXIS,
        Y_AXIS
    }

    public final boolean getCreateSymbols() {
        return this.createSymbols.getValue2().booleanValue();
    }

    public final void setCreateSymbols(boolean value) {
        this.createSymbols.setValue(Boolean.valueOf(value));
    }

    public final BooleanProperty createSymbolsProperty() {
        return this.createSymbols;
    }

    public final SortingPolicy getAxisSortingPolicy() {
        return this.axisSortingPolicy.getValue2();
    }

    public final void setAxisSortingPolicy(SortingPolicy value) {
        this.axisSortingPolicy.setValue(value);
    }

    public final ObjectProperty<SortingPolicy> axisSortingPolicyProperty() {
        return this.axisSortingPolicy;
    }

    public LineChart(@NamedArg("xAxis") Axis<X> xAxis, @NamedArg("yAxis") Axis<Y> yAxis) {
        this(xAxis, yAxis, FXCollections.observableArrayList());
    }

    public LineChart(@NamedArg("xAxis") Axis<X> xAxis, @NamedArg("yAxis") Axis<Y> yAxis, @NamedArg("data") ObservableList<XYChart.Series<X, Y>> data) {
        super(xAxis, yAxis);
        this.seriesYMultiplierMap = new HashMap();
        this.legend = new Legend();
        this.seriesOfDataRemoved = null;
        this.dataItemBeingRemoved = null;
        this.fadeSymbolTransition = null;
        this.XYValueMap = new HashMap();
        this.seriesRemoveTimeline = null;
        this.createSymbols = new StyleableBooleanProperty(true) { // from class: javafx.scene.chart.LineChart.1
            @Override // javafx.beans.property.BooleanPropertyBase
            protected void invalidated() {
                for (int seriesIndex = 0; seriesIndex < LineChart.this.getData().size(); seriesIndex++) {
                    XYChart.Series<X, Y> series = LineChart.this.getData().get(seriesIndex);
                    for (int itemIndex = 0; itemIndex < series.getData().size(); itemIndex++) {
                        XYChart.Data<X, Y> item = series.getData().get(itemIndex);
                        Node symbol = item.getNode();
                        if (get() && symbol == null) {
                            LineChart.this.getPlotChildren().add(LineChart.this.createSymbol(series, LineChart.this.getData().indexOf(series), item, itemIndex));
                        } else if (!get() && symbol != null) {
                            LineChart.this.getPlotChildren().remove(symbol);
                            item.setNode(null);
                        }
                    }
                }
                LineChart.this.requestChartLayout();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return LineChart.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "createSymbols";
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                return StyleableProperties.CREATE_SYMBOLS;
            }
        };
        this.axisSortingPolicy = new ObjectPropertyBase<SortingPolicy>(SortingPolicy.X_AXIS) { // from class: javafx.scene.chart.LineChart.2
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                LineChart.this.requestChartLayout();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return LineChart.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "axisSortingPolicy";
            }
        };
        setLegend(this.legend);
        setData(data);
    }

    @Override // javafx.scene.chart.XYChart
    protected void updateAxisRange() {
        Axis<X> xa = getXAxis();
        Axis<Y> ya = getYAxis();
        List<X> xData = xa.isAutoRanging() ? new ArrayList<>() : null;
        List<Y> yData = ya.isAutoRanging() ? new ArrayList<>() : null;
        if (xData != null || yData != null) {
            for (XYChart.Series<X, Y> series : getData()) {
                for (XYChart.Data<X, Y> data : series.getData()) {
                    if (xData != null) {
                        xData.add(data.getXValue());
                    }
                    if (yData != null) {
                        yData.add(data.getYValue());
                    }
                }
            }
            if (xData != null && (xData.size() != 1 || getXAxis().toNumericValue(xData.get(0)) != 0.0d)) {
                xa.invalidateRange(xData);
            }
            if (yData != null) {
                if (yData.size() != 1 || getYAxis().toNumericValue(yData.get(0)) != 0.0d) {
                    ya.invalidateRange(yData);
                }
            }
        }
    }

    @Override // javafx.scene.chart.XYChart
    protected void dataItemAdded(XYChart.Series<X, Y> series, int itemIndex, XYChart.Data<X, Y> item) {
        Node symbol = createSymbol(series, getData().indexOf(series), item, itemIndex);
        if (shouldAnimate()) {
            if (this.dataRemoveTimeline != null && this.dataRemoveTimeline.getStatus().equals(Animation.Status.RUNNING) && this.seriesOfDataRemoved == series) {
                this.dataRemoveTimeline.stop();
                this.dataRemoveTimeline = null;
                getPlotChildren().remove(this.dataItemBeingRemoved.getNode());
                removeDataItemFromDisplay(this.seriesOfDataRemoved, this.dataItemBeingRemoved);
                this.seriesOfDataRemoved = null;
                this.dataItemBeingRemoved = null;
            }
            boolean animate = false;
            if (itemIndex > 0 && itemIndex < series.getData().size() - 1) {
                animate = true;
                XYChart.Data<X, Y> p1 = series.getData().get(itemIndex - 1);
                XYChart.Data<X, Y> p2 = series.getData().get(itemIndex + 1);
                if (p1 != null && p2 != null) {
                    double x1 = getXAxis().toNumericValue(p1.getXValue());
                    double y1 = getYAxis().toNumericValue(p1.getYValue());
                    double x3 = getXAxis().toNumericValue(p2.getXValue());
                    double y3 = getYAxis().toNumericValue(p2.getYValue());
                    double x2 = getXAxis().toNumericValue(item.getXValue());
                    if (x2 > x1 && x2 < x3) {
                        double y2 = (((y3 - y1) / (x3 - x1)) * x2) + (((x3 * y1) - (y3 * x1)) / (x3 - x1));
                        item.setCurrentY(getYAxis().toRealValue(y2));
                        item.setCurrentX(getXAxis().toRealValue(x2));
                    } else {
                        double x4 = (x3 + x1) / 2.0d;
                        double y4 = (y3 + y1) / 2.0d;
                        item.setCurrentX(getXAxis().toRealValue(x4));
                        item.setCurrentY(getYAxis().toRealValue(y4));
                    }
                }
            } else if (itemIndex == 0 && series.getData().size() > 1) {
                animate = true;
                item.setCurrentX(series.getData().get(1).getXValue());
                item.setCurrentY(series.getData().get(1).getYValue());
            } else if (itemIndex == series.getData().size() - 1 && series.getData().size() > 1) {
                animate = true;
                int last = series.getData().size() - 2;
                item.setCurrentX(series.getData().get(last).getXValue());
                item.setCurrentY(series.getData().get(last).getYValue());
            } else if (symbol != null) {
                symbol.setOpacity(0.0d);
                getPlotChildren().add(symbol);
                FadeTransition ft = new FadeTransition(Duration.millis(500.0d), symbol);
                ft.setToValue(1.0d);
                ft.play();
            }
            if (animate) {
                animate(new KeyFrame(Duration.ZERO, (EventHandler<ActionEvent>) e2 -> {
                    if (symbol == null || getPlotChildren().contains(symbol)) {
                        return;
                    }
                    getPlotChildren().add(symbol);
                }, new KeyValue(item.currentYProperty(), item.getCurrentY()), new KeyValue(item.currentXProperty(), item.getCurrentX())), new KeyFrame(Duration.millis(700.0d), new KeyValue(item.currentYProperty(), item.getYValue(), Interpolator.EASE_BOTH), new KeyValue(item.currentXProperty(), item.getXValue(), Interpolator.EASE_BOTH)));
                return;
            }
            return;
        }
        if (symbol != null) {
            getPlotChildren().add(symbol);
        }
    }

    @Override // javafx.scene.chart.XYChart
    protected void dataItemRemoved(XYChart.Data<X, Y> item, XYChart.Series<X, Y> series) {
        Node symbol = item.getNode();
        if (symbol != null) {
            symbol.focusTraversableProperty().unbind();
        }
        int itemIndex = series.getItemIndex(item);
        if (shouldAnimate()) {
            this.XYValueMap.clear();
            boolean animate = false;
            int dataSize = series.getDataSize();
            int dataListSize = series.getData().size();
            if (itemIndex > 0 && itemIndex < dataSize - 1) {
                animate = true;
                XYChart.Data<X, Y> p1 = series.getItem(itemIndex - 1);
                XYChart.Data<X, Y> p2 = series.getItem(itemIndex + 1);
                double x1 = getXAxis().toNumericValue(p1.getXValue());
                double y1 = getYAxis().toNumericValue(p1.getYValue());
                double x3 = getXAxis().toNumericValue(p2.getXValue());
                double y3 = getYAxis().toNumericValue(p2.getYValue());
                double x2 = getXAxis().toNumericValue(item.getXValue());
                double y2 = getYAxis().toNumericValue(item.getYValue());
                if (x2 > x1 && x2 < x3) {
                    double y4 = (((y3 - y1) / (x3 - x1)) * x2) + (((x3 * y1) - (y3 * x1)) / (x3 - x1));
                    item.setCurrentX(getXAxis().toRealValue(x2));
                    item.setCurrentY(getYAxis().toRealValue(y2));
                    item.setXValue(getXAxis().toRealValue(x2));
                    item.setYValue(getYAxis().toRealValue(y4));
                } else {
                    double x4 = (x3 + x1) / 2.0d;
                    double y5 = (y3 + y1) / 2.0d;
                    item.setCurrentX(getXAxis().toRealValue(x4));
                    item.setCurrentY(getYAxis().toRealValue(y5));
                }
            } else if (itemIndex == 0 && dataListSize > 1) {
                animate = true;
                item.setXValue(series.getData().get(0).getXValue());
                item.setYValue(series.getData().get(0).getYValue());
            } else if (itemIndex == dataSize - 1 && dataListSize > 1) {
                animate = true;
                int last = dataListSize - 1;
                item.setXValue(series.getData().get(last).getXValue());
                item.setYValue(series.getData().get(last).getYValue());
            } else if (symbol != null) {
                this.fadeSymbolTransition = new FadeTransition(Duration.millis(500.0d), symbol);
                this.fadeSymbolTransition.setToValue(0.0d);
                this.fadeSymbolTransition.setOnFinished(actionEvent -> {
                    item.setSeries(null);
                    getPlotChildren().remove(symbol);
                    removeDataItemFromDisplay(series, item);
                    symbol.setOpacity(1.0d);
                });
                this.fadeSymbolTransition.play();
            }
            if (animate) {
                this.dataRemoveTimeline = createDataRemoveTimeline(item, symbol, series);
                this.seriesOfDataRemoved = series;
                this.dataItemBeingRemoved = item;
                this.dataRemoveTimeline.play();
                return;
            }
            return;
        }
        item.setSeries(null);
        if (symbol != null) {
            getPlotChildren().remove(symbol);
        }
        removeDataItemFromDisplay(series, item);
    }

    @Override // javafx.scene.chart.XYChart
    protected void dataItemChanged(XYChart.Data<X, Y> item) {
    }

    @Override // javafx.scene.chart.XYChart
    protected void seriesChanged(ListChangeListener.Change<? extends XYChart.Series> c2) {
        for (int i2 = 0; i2 < getDataSize(); i2++) {
            XYChart.Series<X, Y> s2 = getData().get(i2);
            Node seriesNode = s2.getNode();
            if (seriesNode != null) {
                seriesNode.getStyleClass().setAll("chart-series-line", "series" + i2, s2.defaultColorStyleClass);
            }
        }
    }

    @Override // javafx.scene.chart.XYChart
    protected void seriesAdded(XYChart.Series<X, Y> series, int seriesIndex) {
        Path seriesLine = new Path();
        seriesLine.setStrokeLineJoin(StrokeLineJoin.BEVEL);
        series.setNode(seriesLine);
        DoubleProperty seriesYAnimMultiplier = new SimpleDoubleProperty(this, "seriesYMultiplier");
        this.seriesYMultiplierMap.put(series, seriesYAnimMultiplier);
        if (shouldAnimate()) {
            seriesLine.setOpacity(0.0d);
            seriesYAnimMultiplier.setValue(Double.valueOf(0.0d));
        } else {
            seriesYAnimMultiplier.setValue(Double.valueOf(1.0d));
        }
        getPlotChildren().add(seriesLine);
        List<KeyFrame> keyFrames = new ArrayList<>();
        if (shouldAnimate()) {
            keyFrames.add(new KeyFrame(Duration.ZERO, new KeyValue(seriesLine.opacityProperty(), 0), new KeyValue(seriesYAnimMultiplier, 0)));
            keyFrames.add(new KeyFrame(Duration.millis(200.0d), new KeyValue(seriesLine.opacityProperty(), 1)));
            keyFrames.add(new KeyFrame(Duration.millis(500.0d), new KeyValue(seriesYAnimMultiplier, 1)));
        }
        for (int j2 = 0; j2 < series.getData().size(); j2++) {
            XYChart.Data<X, Y> item = series.getData().get(j2);
            Node symbol = createSymbol(series, seriesIndex, item, j2);
            if (symbol != null) {
                if (shouldAnimate()) {
                    symbol.setOpacity(0.0d);
                }
                getPlotChildren().add(symbol);
                if (shouldAnimate()) {
                    keyFrames.add(new KeyFrame(Duration.ZERO, new KeyValue(symbol.opacityProperty(), 0)));
                    keyFrames.add(new KeyFrame(Duration.millis(200.0d), new KeyValue(symbol.opacityProperty(), 1)));
                }
            }
        }
        if (shouldAnimate()) {
            animate((KeyFrame[]) keyFrames.toArray(new KeyFrame[keyFrames.size()]));
        }
    }

    private void updateDefaultColorIndex(XYChart.Series<X, Y> series) {
        int clearIndex = this.seriesColorMap.get(series).intValue();
        series.getNode().getStyleClass().remove(DEFAULT_COLOR + clearIndex);
        for (int j2 = 0; j2 < series.getData().size(); j2++) {
            Node node = series.getData().get(j2).getNode();
            if (node != null) {
                node.getStyleClass().remove(DEFAULT_COLOR + clearIndex);
            }
        }
    }

    @Override // javafx.scene.chart.XYChart
    protected void seriesRemoved(XYChart.Series<X, Y> series) {
        updateDefaultColorIndex(series);
        this.seriesYMultiplierMap.remove(series);
        if (shouldAnimate()) {
            this.seriesRemoveTimeline = new Timeline(createSeriesRemoveTimeLine(series, 900L));
            this.seriesRemoveTimeline.play();
            return;
        }
        getPlotChildren().remove(series.getNode());
        for (XYChart.Data<X, Y> d2 : series.getData()) {
            getPlotChildren().remove(d2.getNode());
        }
        removeSeriesFromDisplay(series);
    }

    @Override // javafx.scene.chart.XYChart
    protected void layoutPlotChildren() {
        List<LineTo> constructedPath = new ArrayList<>(getDataSize());
        for (int seriesIndex = 0; seriesIndex < getDataSize(); seriesIndex++) {
            XYChart.Series<X, Y> series = getData().get(seriesIndex);
            DoubleProperty seriesYAnimMultiplier = this.seriesYMultiplierMap.get(series);
            if (series.getNode() instanceof Path) {
                ObservableList<PathElement> seriesLine = ((Path) series.getNode()).getElements();
                seriesLine.clear();
                constructedPath.clear();
                Iterator<XYChart.Data<X, Y>> it = getDisplayedDataIterator(series);
                while (it.hasNext()) {
                    XYChart.Data<X, Y> item = it.next();
                    double x2 = getXAxis().getDisplayPosition(item.getCurrentX());
                    double y2 = getYAxis().getDisplayPosition(getYAxis().toRealValue(getYAxis().toNumericValue(item.getCurrentY()) * seriesYAnimMultiplier.getValue2().doubleValue()));
                    if (!Double.isNaN(x2) && !Double.isNaN(y2)) {
                        constructedPath.add(new LineTo(x2, y2));
                        Node symbol = item.getNode();
                        if (symbol != null) {
                            double w2 = symbol.prefWidth(-1.0d);
                            double h2 = symbol.prefHeight(-1.0d);
                            symbol.resizeRelocate(x2 - (w2 / 2.0d), y2 - (h2 / 2.0d), w2, h2);
                        }
                    }
                }
                switch (getAxisSortingPolicy()) {
                    case X_AXIS:
                        Collections.sort(constructedPath, (e1, e2) -> {
                            return Double.compare(e1.getX(), e2.getX());
                        });
                        break;
                    case Y_AXIS:
                        Collections.sort(constructedPath, (e12, e22) -> {
                            return Double.compare(e12.getY(), e22.getY());
                        });
                        break;
                }
                if (!constructedPath.isEmpty()) {
                    LineTo first = constructedPath.get(0);
                    seriesLine.add(new MoveTo(first.getX(), first.getY()));
                    seriesLine.addAll(constructedPath);
                }
            }
        }
    }

    @Override // javafx.scene.chart.XYChart
    void dataBeingRemovedIsAdded(XYChart.Data item, XYChart.Series series) {
        if (this.fadeSymbolTransition != null) {
            this.fadeSymbolTransition.setOnFinished(null);
            this.fadeSymbolTransition.stop();
        }
        if (this.dataRemoveTimeline != null) {
            this.dataRemoveTimeline.setOnFinished(null);
            this.dataRemoveTimeline.stop();
        }
        Node symbol = item.getNode();
        if (symbol != null) {
            getPlotChildren().remove(symbol);
        }
        item.setSeries(null);
        removeDataItemFromDisplay(series, item);
        Double value = this.XYValueMap.get(item);
        if (value != null) {
            item.setYValue(value);
            item.setCurrentY(value);
        }
        this.XYValueMap.clear();
    }

    @Override // javafx.scene.chart.XYChart
    void seriesBeingRemovedIsAdded(XYChart.Series<X, Y> series) {
        if (this.seriesRemoveTimeline != null) {
            this.seriesRemoveTimeline.setOnFinished(null);
            this.seriesRemoveTimeline.stop();
            getPlotChildren().remove(series.getNode());
            for (XYChart.Data<X, Y> d2 : series.getData()) {
                getPlotChildren().remove(d2.getNode());
            }
            removeSeriesFromDisplay(series);
        }
    }

    private Timeline createDataRemoveTimeline(XYChart.Data<X, Y> data, Node node, XYChart.Series<X, Y> series) {
        Timeline timeline = new Timeline();
        this.XYValueMap.put(data, Double.valueOf(((Number) data.getYValue()).doubleValue()));
        timeline.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(data.currentYProperty(), data.getCurrentY()), new KeyValue(data.currentXProperty(), data.getCurrentX())), new KeyFrame(Duration.millis(500.0d), (EventHandler<ActionEvent>) actionEvent -> {
            if (node != null) {
                getPlotChildren().remove(node);
            }
            removeDataItemFromDisplay(series, data);
            this.XYValueMap.clear();
        }, new KeyValue(data.currentYProperty(), data.getYValue(), Interpolator.EASE_BOTH), new KeyValue(data.currentXProperty(), data.getXValue(), Interpolator.EASE_BOTH)));
        return timeline;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Node createSymbol(XYChart.Series<X, Y> series, int seriesIndex, XYChart.Data<X, Y> item, int itemIndex) {
        Node symbol = item.getNode();
        if (symbol == null && getCreateSymbols()) {
            symbol = new StackPane();
            symbol.setAccessibleRole(AccessibleRole.TEXT);
            symbol.setAccessibleRoleDescription("Point");
            symbol.focusTraversableProperty().bind(Platform.accessibilityActiveProperty());
            item.setNode(symbol);
        }
        if (symbol != null) {
            symbol.getStyleClass().addAll("chart-line-symbol", "series" + seriesIndex, "data" + itemIndex, series.defaultColorStyleClass);
        }
        return symbol;
    }

    @Override // javafx.scene.chart.XYChart
    protected void updateLegend() {
        this.legend.getItems().clear();
        if (getData() != null) {
            for (int seriesIndex = 0; seriesIndex < getData().size(); seriesIndex++) {
                XYChart.Series<X, Y> series = getData().get(seriesIndex);
                Legend.LegendItem legenditem = new Legend.LegendItem(series.getName());
                legenditem.getSymbol().getStyleClass().addAll("chart-line-symbol", "series" + seriesIndex, series.defaultColorStyleClass);
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

    /* loaded from: jfxrt.jar:javafx/scene/chart/LineChart$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<LineChart<?, ?>, Boolean> CREATE_SYMBOLS = new CssMetaData<LineChart<?, ?>, Boolean>("-fx-create-symbols", BooleanConverter.getInstance(), Boolean.TRUE) { // from class: javafx.scene.chart.LineChart.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(LineChart<?, ?> node) {
                return ((LineChart) node).createSymbols == null || !((LineChart) node).createSymbols.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(LineChart<?, ?> node) {
                return (StyleableProperty) node.createSymbolsProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(XYChart.getClassCssMetaData());
            styleables.add(CREATE_SYMBOLS);
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
