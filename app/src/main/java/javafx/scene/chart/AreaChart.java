package javafx.scene.chart;

import com.sun.javafx.charts.Legend;
import com.sun.javafx.css.converters.BooleanConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
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
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.StrokeLineJoin;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:javafx/scene/chart/AreaChart.class */
public class AreaChart<X, Y> extends XYChart<X, Y> {
    private Map<XYChart.Series<X, Y>, DoubleProperty> seriesYMultiplierMap;
    private Legend legend;
    private BooleanProperty createSymbols;

    public final boolean getCreateSymbols() {
        return this.createSymbols.getValue2().booleanValue();
    }

    public final void setCreateSymbols(boolean value) {
        this.createSymbols.setValue(Boolean.valueOf(value));
    }

    public final BooleanProperty createSymbolsProperty() {
        return this.createSymbols;
    }

    public AreaChart(@NamedArg("xAxis") Axis<X> xAxis, @NamedArg("yAxis") Axis<Y> yAxis) {
        this(xAxis, yAxis, FXCollections.observableArrayList());
    }

    public AreaChart(@NamedArg("xAxis") Axis<X> xAxis, @NamedArg("yAxis") Axis<Y> yAxis, @NamedArg("data") ObservableList<XYChart.Series<X, Y>> data) {
        super(xAxis, yAxis);
        this.seriesYMultiplierMap = new HashMap();
        this.legend = new Legend();
        this.createSymbols = new StyleableBooleanProperty(true) { // from class: javafx.scene.chart.AreaChart.1
            @Override // javafx.beans.property.BooleanPropertyBase
            protected void invalidated() {
                for (int seriesIndex = 0; seriesIndex < AreaChart.this.getData().size(); seriesIndex++) {
                    XYChart.Series<X, Y> series = AreaChart.this.getData().get(seriesIndex);
                    for (int itemIndex = 0; itemIndex < series.getData().size(); itemIndex++) {
                        XYChart.Data<X, Y> item = series.getData().get(itemIndex);
                        Node symbol = item.getNode();
                        if (get() && symbol == null) {
                            Node symbol2 = AreaChart.this.createSymbol(series, AreaChart.this.getData().indexOf(series), item, itemIndex);
                            if (null != symbol2) {
                                AreaChart.this.getPlotChildren().add(symbol2);
                            }
                        } else if (!get() && symbol != null) {
                            AreaChart.this.getPlotChildren().remove(symbol);
                            item.setNode(null);
                        }
                    }
                }
                AreaChart.this.requestChartLayout();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return this;
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
        setLegend(this.legend);
        setData(data);
    }

    private static double doubleValue(Number number) {
        return doubleValue(number, 0.0d);
    }

    private static double doubleValue(Number number, double nullDefault) {
        return number == null ? nullDefault : number.doubleValue();
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
        if (!shouldAnimate()) {
            if (symbol != null) {
                getPlotChildren().add(symbol);
                return;
            }
            return;
        }
        boolean animate = false;
        if (itemIndex > 0 && itemIndex < series.getData().size() - 1) {
            animate = true;
            XYChart.Data<X, Y> p1 = series.getData().get(itemIndex - 1);
            XYChart.Data<X, Y> p2 = series.getData().get(itemIndex + 1);
            double x1 = getXAxis().toNumericValue(p1.getXValue());
            double y1 = getYAxis().toNumericValue(p1.getYValue());
            double x3 = getXAxis().toNumericValue(p2.getXValue());
            double y3 = getYAxis().toNumericValue(p2.getYValue());
            double x2 = getXAxis().toNumericValue(item.getXValue());
            getYAxis().toNumericValue(item.getYValue());
            double y2 = (((y3 - y1) / (x3 - x1)) * x2) + (((x3 * y1) - (y3 * x1)) / (x3 - x1));
            item.setCurrentY(getYAxis().toRealValue(y2));
            item.setCurrentX(getXAxis().toRealValue(x2));
        } else if (itemIndex == 0 && series.getData().size() > 1) {
            animate = true;
            item.setCurrentX(series.getData().get(1).getXValue());
            item.setCurrentY(series.getData().get(1).getYValue());
        } else if (itemIndex == series.getData().size() - 1 && series.getData().size() > 1) {
            animate = true;
            int last = series.getData().size() - 2;
            item.setCurrentX(series.getData().get(last).getXValue());
            item.setCurrentY(series.getData().get(last).getYValue());
        }
        if (symbol != null) {
            symbol.setOpacity(0.0d);
            getPlotChildren().add(symbol);
            FadeTransition ft = new FadeTransition(Duration.millis(500.0d), symbol);
            ft.setToValue(1.0d);
            ft.play();
        }
        if (animate) {
            animate(new KeyFrame(Duration.ZERO, (EventHandler<ActionEvent>) e2 -> {
                if (symbol != null && !getPlotChildren().contains(symbol)) {
                    getPlotChildren().add(symbol);
                }
            }, new KeyValue(item.currentYProperty(), item.getCurrentY()), new KeyValue(item.currentXProperty(), item.getCurrentX())), new KeyFrame(Duration.millis(800.0d), new KeyValue(item.currentYProperty(), item.getYValue(), Interpolator.EASE_BOTH), new KeyValue(item.currentXProperty(), item.getXValue(), Interpolator.EASE_BOTH)));
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
                double y4 = (((y3 - y1) / (x3 - x1)) * x2) + (((x3 * y1) - (y3 * x1)) / (x3 - x1));
                item.setCurrentX(getXAxis().toRealValue(x2));
                item.setCurrentY(getYAxis().toRealValue(y2));
                item.setXValue(getXAxis().toRealValue(x2));
                item.setYValue(getYAxis().toRealValue(y4));
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
                symbol.setOpacity(0.0d);
                FadeTransition ft = new FadeTransition(Duration.millis(500.0d), symbol);
                ft.setToValue(0.0d);
                ft.setOnFinished(actionEvent -> {
                    getPlotChildren().remove(symbol);
                    removeDataItemFromDisplay(series, item);
                });
                ft.play();
            }
            if (animate) {
                animate(new KeyFrame(Duration.ZERO, new KeyValue(item.currentYProperty(), item.getCurrentY()), new KeyValue(item.currentXProperty(), item.getCurrentX())), new KeyFrame(Duration.millis(800.0d), (EventHandler<ActionEvent>) actionEvent2 -> {
                    item.setSeries(null);
                    getPlotChildren().remove(symbol);
                    removeDataItemFromDisplay(series, item);
                }, new KeyValue(item.currentYProperty(), item.getYValue(), Interpolator.EASE_BOTH), new KeyValue(item.currentXProperty(), item.getXValue(), Interpolator.EASE_BOTH)));
                return;
            }
            return;
        }
        item.setSeries(null);
        getPlotChildren().remove(symbol);
        removeDataItemFromDisplay(series, item);
    }

    @Override // javafx.scene.chart.XYChart
    protected void dataItemChanged(XYChart.Data<X, Y> item) {
    }

    @Override // javafx.scene.chart.XYChart
    protected void seriesChanged(ListChangeListener.Change<? extends XYChart.Series> c2) {
        for (int i2 = 0; i2 < getDataSize(); i2++) {
            XYChart.Series<X, Y> s2 = getData().get(i2);
            Path seriesLine = (Path) ((Group) s2.getNode()).getChildren().get(1);
            Path fillPath = (Path) ((Group) s2.getNode()).getChildren().get(0);
            seriesLine.getStyleClass().setAll("chart-series-area-line", "series" + i2, s2.defaultColorStyleClass);
            fillPath.getStyleClass().setAll("chart-series-area-fill", "series" + i2, s2.defaultColorStyleClass);
            for (int j2 = 0; j2 < s2.getData().size(); j2++) {
                XYChart.Data<X, Y> item = s2.getData().get(j2);
                Node node = item.getNode();
                if (node != null) {
                    node.getStyleClass().setAll("chart-area-symbol", "series" + i2, "data" + j2, s2.defaultColorStyleClass);
                }
            }
        }
    }

    @Override // javafx.scene.chart.XYChart
    protected void seriesAdded(XYChart.Series<X, Y> series, int seriesIndex) {
        Path seriesLine = new Path();
        Path fillPath = new Path();
        seriesLine.setStrokeLineJoin(StrokeLineJoin.BEVEL);
        Group areaGroup = new Group(fillPath, seriesLine);
        series.setNode(areaGroup);
        DoubleProperty seriesYAnimMultiplier = new SimpleDoubleProperty(this, "seriesYMultiplier");
        this.seriesYMultiplierMap.put(series, seriesYAnimMultiplier);
        if (shouldAnimate()) {
            seriesYAnimMultiplier.setValue(Double.valueOf(0.0d));
        } else {
            seriesYAnimMultiplier.setValue(Double.valueOf(1.0d));
        }
        getPlotChildren().add(areaGroup);
        List<KeyFrame> keyFrames = new ArrayList<>();
        if (shouldAnimate()) {
            keyFrames.add(new KeyFrame(Duration.ZERO, new KeyValue(areaGroup.opacityProperty(), 0), new KeyValue(seriesYAnimMultiplier, 0)));
            keyFrames.add(new KeyFrame(Duration.millis(200.0d), new KeyValue(areaGroup.opacityProperty(), 1)));
            keyFrames.add(new KeyFrame(Duration.millis(500.0d), new KeyValue(seriesYAnimMultiplier, 1)));
        }
        for (int j2 = 0; j2 < series.getData().size(); j2++) {
            XYChart.Data<X, Y> item = series.getData().get(j2);
            Node symbol = createSymbol(series, seriesIndex, item, j2);
            if (symbol != null) {
                if (shouldAnimate()) {
                    symbol.setOpacity(0.0d);
                    getPlotChildren().add(symbol);
                    keyFrames.add(new KeyFrame(Duration.ZERO, new KeyValue(symbol.opacityProperty(), 0)));
                    keyFrames.add(new KeyFrame(Duration.millis(200.0d), new KeyValue(symbol.opacityProperty(), 1)));
                } else {
                    getPlotChildren().add(symbol);
                }
            }
        }
        if (shouldAnimate()) {
            animate((KeyFrame[]) keyFrames.toArray(new KeyFrame[keyFrames.size()]));
        }
    }

    private void updateDefaultColorIndex(XYChart.Series<X, Y> series) {
        int clearIndex = this.seriesColorMap.get(series).intValue();
        Path seriesLine = (Path) ((Group) series.getNode()).getChildren().get(1);
        Path fillPath = (Path) ((Group) series.getNode()).getChildren().get(0);
        if (seriesLine != null) {
            seriesLine.getStyleClass().remove(DEFAULT_COLOR + clearIndex);
        }
        if (fillPath != null) {
            fillPath.getStyleClass().remove(DEFAULT_COLOR + clearIndex);
        }
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
            Timeline tl = new Timeline(createSeriesRemoveTimeLine(series, 400L));
            tl.play();
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
            double lastX = 0.0d;
            ObservableList<Node> children = ((Group) series.getNode()).getChildren();
            ObservableList<PathElement> seriesLine = ((Path) children.get(1)).getElements();
            ObservableList<PathElement> fillPath = ((Path) children.get(0)).getElements();
            seriesLine.clear();
            fillPath.clear();
            constructedPath.clear();
            Iterator<XYChart.Data<X, Y>> it = getDisplayedDataIterator(series);
            while (it.hasNext()) {
                XYChart.Data<X, Y> item = it.next();
                double x2 = getXAxis().getDisplayPosition(item.getCurrentX());
                double y2 = getYAxis().getDisplayPosition(getYAxis().toRealValue(getYAxis().toNumericValue(item.getCurrentY()) * seriesYAnimMultiplier.getValue2().doubleValue()));
                constructedPath.add(new LineTo(x2, y2));
                if (!Double.isNaN(x2) && !Double.isNaN(y2)) {
                    lastX = x2;
                    Node symbol = item.getNode();
                    if (symbol != null) {
                        double w2 = symbol.prefWidth(-1.0d);
                        double h2 = symbol.prefHeight(-1.0d);
                        symbol.resizeRelocate(x2 - (w2 / 2.0d), y2 - (h2 / 2.0d), w2, h2);
                    }
                }
            }
            if (!constructedPath.isEmpty()) {
                Collections.sort(constructedPath, (e1, e2) -> {
                    return Double.compare(e1.getX(), e2.getX());
                });
                LineTo first = constructedPath.get(0);
                double displayYPos = first.getY();
                double numericYPos = getYAxis().toNumericValue(getYAxis().getValueForDisplay(displayYPos));
                double yAxisZeroPos = getYAxis().getZeroPosition();
                boolean isYAxisZeroPosVisible = !Double.isNaN(yAxisZeroPos);
                double yAxisHeight = getYAxis().getHeight();
                double yFillPos = isYAxisZeroPosVisible ? yAxisZeroPos : numericYPos < 0.0d ? numericYPos - yAxisHeight : yAxisHeight;
                seriesLine.add(new MoveTo(first.getX(), displayYPos));
                fillPath.add(new MoveTo(first.getX(), yFillPos));
                seriesLine.addAll(constructedPath);
                fillPath.addAll(constructedPath);
                fillPath.add(new LineTo(lastX, yFillPos));
                fillPath.add(new ClosePath());
            }
        }
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
            symbol.getStyleClass().setAll("chart-area-symbol", "series" + seriesIndex, "data" + itemIndex, series.defaultColorStyleClass);
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
                legenditem.getSymbol().getStyleClass().addAll("chart-area-symbol", "series" + seriesIndex, "area-legend-symbol", series.defaultColorStyleClass);
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

    /* loaded from: jfxrt.jar:javafx/scene/chart/AreaChart$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<AreaChart<?, ?>, Boolean> CREATE_SYMBOLS = new CssMetaData<AreaChart<?, ?>, Boolean>("-fx-create-symbols", BooleanConverter.getInstance(), Boolean.TRUE) { // from class: javafx.scene.chart.AreaChart.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(AreaChart<?, ?> node) {
                return ((AreaChart) node).createSymbols == null || !((AreaChart) node).createSymbols.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(AreaChart<?, ?> node) {
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
