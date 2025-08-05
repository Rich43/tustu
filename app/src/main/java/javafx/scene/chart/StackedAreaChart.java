package javafx.scene.chart;

import com.sun.javafx.charts.Legend;
import com.sun.javafx.css.converters.BooleanConverter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
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
import javafx.scene.shape.StrokeLineJoin;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:javafx/scene/chart/StackedAreaChart.class */
public class StackedAreaChart<X, Y> extends XYChart<X, Y> {
    private Map<XYChart.Series<X, Y>, DoubleProperty> seriesYMultiplierMap;
    private Legend legend;
    private BooleanProperty createSymbols;

    /* loaded from: jfxrt.jar:javafx/scene/chart/StackedAreaChart$PartOf.class */
    private enum PartOf {
        CURRENT,
        PREVIOUS
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

    public StackedAreaChart(@NamedArg("xAxis") Axis<X> xAxis, @NamedArg("yAxis") Axis<Y> yAxis) {
        this(xAxis, yAxis, FXCollections.observableArrayList());
    }

    public StackedAreaChart(@NamedArg("xAxis") Axis<X> xAxis, @NamedArg("yAxis") Axis<Y> yAxis, @NamedArg("data") ObservableList<XYChart.Series<X, Y>> data) {
        super(xAxis, yAxis);
        this.seriesYMultiplierMap = new HashMap();
        this.legend = new Legend();
        this.createSymbols = new StyleableBooleanProperty(true) { // from class: javafx.scene.chart.StackedAreaChart.1
            @Override // javafx.beans.property.BooleanPropertyBase
            protected void invalidated() {
                for (int seriesIndex = 0; seriesIndex < StackedAreaChart.this.getData().size(); seriesIndex++) {
                    XYChart.Series<X, Y> series = StackedAreaChart.this.getData().get(seriesIndex);
                    for (int itemIndex = 0; itemIndex < series.getData().size(); itemIndex++) {
                        XYChart.Data<X, Y> item = series.getData().get(itemIndex);
                        Node symbol = item.getNode();
                        if (get() && symbol == null) {
                            Node symbol2 = StackedAreaChart.this.createSymbol(series, StackedAreaChart.this.getData().indexOf(series), item, itemIndex);
                            if (null != symbol2) {
                                StackedAreaChart.this.getPlotChildren().add(symbol2);
                            }
                        } else if (!get() && symbol != null) {
                            StackedAreaChart.this.getPlotChildren().remove(symbol);
                            item.setNode(null);
                        }
                    }
                }
                StackedAreaChart.this.requestChartLayout();
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
        if (!(yAxis instanceof ValueAxis)) {
            throw new IllegalArgumentException("Axis type incorrect, yAxis must be of ValueAxis type.");
        }
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
        } else if (symbol != null) {
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
                    symbol.setOpacity(1.0d);
                });
                ft.play();
            }
            if (animate) {
                animate(new KeyFrame(Duration.ZERO, new KeyValue(item.currentYProperty(), item.getCurrentY()), new KeyValue(item.currentXProperty(), item.getCurrentX())), new KeyFrame(Duration.millis(800.0d), (EventHandler<ActionEvent>) actionEvent2 -> {
                    getPlotChildren().remove(symbol);
                    removeDataItemFromDisplay(series, item);
                }, new KeyValue(item.currentYProperty(), item.getYValue(), Interpolator.EASE_BOTH), new KeyValue(item.currentXProperty(), item.getXValue(), Interpolator.EASE_BOTH)));
                return;
            }
            return;
        }
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
        fillPath.setStrokeLineJoin(StrokeLineJoin.BEVEL);
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

    @Override // javafx.scene.chart.XYChart
    protected void seriesRemoved(XYChart.Series<X, Y> series) {
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
    protected void updateAxisRange() {
        Axis<X> xa = getXAxis();
        Axis<Y> ya = getYAxis();
        if (xa.isAutoRanging()) {
            List xData = new ArrayList();
            for (XYChart.Series<X, Y> series : getData()) {
                for (XYChart.Data<X, Y> data : series.getData()) {
                    xData.add(data.getXValue());
                }
            }
            xa.invalidateRange(xData);
        }
        if (ya.isAutoRanging()) {
            double totalMinY = Double.MAX_VALUE;
            Iterator<XYChart.Series<X, Y>> seriesIterator = getDisplayedSeriesIterator();
            boolean first = true;
            Map<? extends Double, ? extends Double> treeMap = new TreeMap<>();
            NavigableMap<Double, Double> prevAccum = new TreeMap<>();
            NavigableMap<Double, Double> currentValues = new TreeMap<>();
            while (seriesIterator.hasNext()) {
                currentValues.clear();
                XYChart.Series<X, Y> series2 = seriesIterator.next();
                for (XYChart.Data<X, Y> item : series2.getData()) {
                    if (item != null) {
                        double xv = xa.toNumericValue(item.getXValue());
                        double yv = ya.toNumericValue(item.getYValue());
                        currentValues.put(Double.valueOf(xv), Double.valueOf(yv));
                        if (first) {
                            treeMap.put(Double.valueOf(xv), Double.valueOf(yv));
                            totalMinY = Math.min(totalMinY, yv);
                        } else if (prevAccum.containsKey(Double.valueOf(xv))) {
                            treeMap.put(Double.valueOf(xv), Double.valueOf(prevAccum.get(Double.valueOf(xv)).doubleValue() + yv));
                        } else {
                            Map.Entry<Double, Double> he = prevAccum.higherEntry(Double.valueOf(xv));
                            Map.Entry<Double, Double> le = prevAccum.lowerEntry(Double.valueOf(xv));
                            if (he != null && le != null) {
                                treeMap.put(Double.valueOf(xv), Double.valueOf((((xv - le.getKey().doubleValue()) / (he.getKey().doubleValue() - le.getKey().doubleValue())) * (le.getValue().doubleValue() + he.getValue().doubleValue())) + yv));
                            } else if (he != null) {
                                treeMap.put(Double.valueOf(xv), Double.valueOf(he.getValue().doubleValue() + yv));
                            } else if (le != null) {
                                treeMap.put(Double.valueOf(xv), Double.valueOf(le.getValue().doubleValue() + yv));
                            } else {
                                treeMap.put(Double.valueOf(xv), Double.valueOf(yv));
                            }
                        }
                    }
                }
                for (Map.Entry<Double, Double> e2 : prevAccum.entrySet()) {
                    if (!treeMap.keySet().contains(e2.getKey())) {
                        Double k2 = e2.getKey();
                        Double v2 = e2.getValue();
                        Map.Entry<Double, Double> he2 = currentValues.higherEntry(k2);
                        Map.Entry<Double, Double> le2 = currentValues.lowerEntry(k2);
                        if (he2 != null && le2 != null) {
                            treeMap.put(k2, Double.valueOf((((k2.doubleValue() - le2.getKey().doubleValue()) / (he2.getKey().doubleValue() - le2.getKey().doubleValue())) * (le2.getValue().doubleValue() + he2.getValue().doubleValue())) + v2.doubleValue()));
                        } else if (he2 != null) {
                            treeMap.put(k2, Double.valueOf(he2.getValue().doubleValue() + v2.doubleValue()));
                        } else if (le2 != null) {
                            treeMap.put(k2, Double.valueOf(le2.getValue().doubleValue() + v2.doubleValue()));
                        } else {
                            treeMap.put(k2, v2);
                        }
                    }
                }
                prevAccum.clear();
                prevAccum.putAll(treeMap);
                treeMap.clear();
                first = totalMinY == Double.MAX_VALUE;
            }
            if (totalMinY != Double.MAX_VALUE) {
                ya.invalidateRange(Arrays.asList(ya.toRealValue(totalMinY), ya.toRealValue(((Double) Collections.max(prevAccum.values())).doubleValue())));
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.chart.XYChart
    protected void layoutPlotChildren() {
        Node symbol;
        ArrayList<DataPointInfo<X, Y>> currentSeriesData = new ArrayList<>();
        ArrayList<DataPointInfo<X, Y>> aggregateData = new ArrayList<>();
        for (int seriesIndex = 0; seriesIndex < getDataSize(); seriesIndex++) {
            XYChart.Series<X, Y> series = (XYChart.Series) getData().get(seriesIndex);
            aggregateData.clear();
            Iterator<DataPointInfo<X, Y>> it = currentSeriesData.iterator();
            while (it.hasNext()) {
                DataPointInfo<X, Y> data = it.next();
                data.partOf = PartOf.PREVIOUS;
                aggregateData.add(data);
            }
            currentSeriesData.clear();
            Iterator<XYChart.Data<X, Y>> it2 = getDisplayedDataIterator(series);
            while (it2.hasNext()) {
                XYChart.Data<X, Y> item = it2.next();
                DataPointInfo<X, Y> itemInfo = new DataPointInfo<>(item, item.getXValue(), item.getYValue(), PartOf.CURRENT);
                aggregateData.add(itemInfo);
            }
            DoubleProperty seriesYAnimMultiplier = this.seriesYMultiplierMap.get(series);
            Path seriesLine = (Path) ((Group) series.getNode()).getChildren().get(1);
            Path fillPath = (Path) ((Group) series.getNode()).getChildren().get(0);
            seriesLine.getElements().clear();
            fillPath.getElements().clear();
            int dataIndex = 0;
            sortAggregateList(aggregateData);
            Axis yAxis = getYAxis();
            Axis xAxis = getXAxis();
            boolean firstCurrent = false;
            boolean lastCurrent = false;
            int firstCurrentIndex = findNextCurrent(aggregateData, -1);
            int lastCurrentIndex = findPreviousCurrent(aggregateData, aggregateData.size());
            double basePosition = yAxis.getZeroPosition();
            if (Double.isNaN(basePosition)) {
                ValueAxis<Number> valueYAxis = (ValueAxis) yAxis;
                if (valueYAxis.getLowerBound() > 0.0d) {
                    basePosition = valueYAxis.getDisplayPosition((ValueAxis<Number>) Double.valueOf(valueYAxis.getLowerBound()));
                } else {
                    basePosition = valueYAxis.getDisplayPosition((ValueAxis<Number>) Double.valueOf(valueYAxis.getUpperBound()));
                }
            }
            Iterator<DataPointInfo<X, Y>> it3 = aggregateData.iterator();
            while (it3.hasNext()) {
                DataPointInfo<X, Y> dataInfo = it3.next();
                if (dataIndex == lastCurrentIndex) {
                    lastCurrent = true;
                }
                if (dataIndex == firstCurrentIndex) {
                    firstCurrent = true;
                }
                XYChart.Data<X, Y> item2 = dataInfo.dataItem;
                if (dataInfo.partOf.equals(PartOf.CURRENT)) {
                    int pIndex = findPreviousPrevious(aggregateData, dataIndex);
                    int nIndex = findNextPrevious(aggregateData, dataIndex);
                    if (pIndex == -1 || (nIndex == -1 && !aggregateData.get(pIndex).f12647x.equals(dataInfo.f12647x))) {
                        if (firstCurrent) {
                            XYChart.Data<X, Y> ddItem = new XYChart.Data<>(dataInfo.f12647x, 0);
                            addDropDown(currentSeriesData, ddItem, ddItem.getXValue(), ddItem.getYValue(), xAxis.getDisplayPosition(ddItem.getCurrentX()), basePosition);
                        }
                        double x2 = xAxis.getDisplayPosition(item2.getCurrentX());
                        double y2 = yAxis.getDisplayPosition(yAxis.toRealValue(yAxis.toNumericValue(item2.getCurrentY()) * seriesYAnimMultiplier.getValue2().doubleValue()));
                        addPoint(currentSeriesData, item2, item2.getXValue(), item2.getYValue(), x2, y2, PartOf.CURRENT, false, !firstCurrent);
                        if (dataIndex == lastCurrentIndex) {
                            XYChart.Data<X, Y> ddItem2 = new XYChart.Data<>(dataInfo.f12647x, 0);
                            addDropDown(currentSeriesData, ddItem2, ddItem2.getXValue(), ddItem2.getYValue(), xAxis.getDisplayPosition(ddItem2.getCurrentX()), basePosition);
                        }
                    } else {
                        DataPointInfo<X, Y> prevPoint = aggregateData.get(pIndex);
                        if (prevPoint.f12647x.equals(dataInfo.f12647x)) {
                            if (prevPoint.dropDown) {
                                prevPoint = aggregateData.get(findPreviousPrevious(aggregateData, pIndex));
                            }
                            if (prevPoint.f12647x.equals(dataInfo.f12647x)) {
                                double x3 = xAxis.getDisplayPosition(item2.getCurrentX());
                                double yv = yAxis.toNumericValue(item2.getCurrentY()) + yAxis.toNumericValue(prevPoint.f12648y);
                                double y3 = yAxis.getDisplayPosition(yAxis.toRealValue(yv * seriesYAnimMultiplier.getValue2().doubleValue()));
                                addPoint(currentSeriesData, item2, dataInfo.f12647x, yAxis.toRealValue(yv), x3, y3, PartOf.CURRENT, false, !firstCurrent);
                            }
                            if (lastCurrent) {
                                addDropDown(currentSeriesData, item2, prevPoint.f12647x, prevPoint.f12648y, prevPoint.displayX, prevPoint.displayY);
                            }
                        } else {
                            DataPointInfo<X, Y> nextPoint = nIndex == -1 ? null : aggregateData.get(nIndex);
                            DataPointInfo<X, Y> prevPoint2 = pIndex == -1 ? null : aggregateData.get(pIndex);
                            double yValue = yAxis.toNumericValue(item2.getCurrentY());
                            if (prevPoint2 != null && nextPoint != null) {
                                double x4 = xAxis.getDisplayPosition(item2.getCurrentX());
                                double displayY = interpolate(prevPoint2.displayX, prevPoint2.displayY, nextPoint.displayX, nextPoint.displayY, x4);
                                double dataY = interpolate(xAxis.toNumericValue(prevPoint2.f12647x), yAxis.toNumericValue(prevPoint2.f12648y), xAxis.toNumericValue(nextPoint.f12647x), yAxis.toNumericValue(nextPoint.f12648y), xAxis.toNumericValue(dataInfo.f12647x));
                                if (firstCurrent) {
                                    addDropDown(currentSeriesData, new XYChart.Data<>(dataInfo.f12647x, Double.valueOf(dataY)), dataInfo.f12647x, yAxis.toRealValue(dataY), x4, displayY);
                                }
                                double y4 = yAxis.getDisplayPosition(yAxis.toRealValue((yValue + dataY) * seriesYAnimMultiplier.getValue2().doubleValue()));
                                addPoint(currentSeriesData, item2, dataInfo.f12647x, yAxis.toRealValue(yValue + dataY), x4, y4, PartOf.CURRENT, false, !firstCurrent);
                                if (dataIndex == lastCurrentIndex) {
                                    addDropDown(currentSeriesData, new XYChart.Data<>(dataInfo.f12647x, Double.valueOf(dataY)), dataInfo.f12647x, yAxis.toRealValue(dataY), x4, displayY);
                                }
                            }
                        }
                    }
                } else {
                    int pIndex2 = findPreviousCurrent(aggregateData, dataIndex);
                    int nIndex2 = findNextCurrent(aggregateData, dataIndex);
                    if (dataInfo.dropDown) {
                        if (xAxis.toNumericValue(dataInfo.f12647x) <= xAxis.toNumericValue(aggregateData.get(firstCurrentIndex).f12647x) || xAxis.toNumericValue(dataInfo.f12647x) > xAxis.toNumericValue(aggregateData.get(lastCurrentIndex).f12647x)) {
                            addDropDown(currentSeriesData, item2, dataInfo.f12647x, dataInfo.f12648y, dataInfo.displayX, dataInfo.displayY);
                        }
                    } else if (pIndex2 == -1 || nIndex2 == -1) {
                        addPoint(currentSeriesData, item2, dataInfo.f12647x, dataInfo.f12648y, dataInfo.displayX, dataInfo.displayY, PartOf.CURRENT, true, false);
                    } else {
                        DataPointInfo<X, Y> nextPoint2 = aggregateData.get(nIndex2);
                        if (!nextPoint2.f12647x.equals(dataInfo.f12647x)) {
                            DataPointInfo<X, Y> prevPoint3 = aggregateData.get(pIndex2);
                            double x5 = xAxis.getDisplayPosition(item2.getCurrentX());
                            double dataY2 = interpolate(xAxis.toNumericValue(prevPoint3.f12647x), yAxis.toNumericValue(prevPoint3.f12648y), xAxis.toNumericValue(nextPoint2.f12647x), yAxis.toNumericValue(nextPoint2.f12648y), xAxis.toNumericValue(dataInfo.f12647x));
                            double yv2 = yAxis.toNumericValue(dataInfo.f12648y) + dataY2;
                            double y5 = yAxis.getDisplayPosition(yAxis.toRealValue(yv2 * seriesYAnimMultiplier.getValue2().doubleValue()));
                            addPoint(currentSeriesData, new XYChart.Data(dataInfo.f12647x, Double.valueOf(dataY2)), dataInfo.f12647x, yAxis.toRealValue(yv2), x5, y5, PartOf.CURRENT, true, true);
                        }
                    }
                }
                dataIndex++;
                if (firstCurrent) {
                    firstCurrent = false;
                }
                if (lastCurrent) {
                    lastCurrent = false;
                }
            }
            if (!currentSeriesData.isEmpty()) {
                seriesLine.getElements().add(new MoveTo(currentSeriesData.get(0).displayX, currentSeriesData.get(0).displayY));
                fillPath.getElements().add(new MoveTo(currentSeriesData.get(0).displayX, currentSeriesData.get(0).displayY));
            }
            Iterator<DataPointInfo<X, Y>> it4 = currentSeriesData.iterator();
            while (it4.hasNext()) {
                DataPointInfo<X, Y> point = it4.next();
                if (point.lineTo) {
                    seriesLine.getElements().add(new LineTo(point.displayX, point.displayY));
                } else {
                    seriesLine.getElements().add(new MoveTo(point.displayX, point.displayY));
                }
                fillPath.getElements().add(new LineTo(point.displayX, point.displayY));
                if (!point.skipSymbol && (symbol = point.dataItem.getNode()) != null) {
                    double w2 = symbol.prefWidth(-1.0d);
                    double h2 = symbol.prefHeight(-1.0d);
                    symbol.resizeRelocate(point.displayX - (w2 / 2.0d), point.displayY - (h2 / 2.0d), w2, h2);
                }
            }
            for (int i2 = aggregateData.size() - 1; i2 > 0; i2--) {
                DataPointInfo<X, Y> point2 = aggregateData.get(i2);
                if (PartOf.PREVIOUS.equals(point2.partOf)) {
                    fillPath.getElements().add(new LineTo(point2.displayX, point2.displayY));
                }
            }
            if (!fillPath.getElements().isEmpty()) {
                fillPath.getElements().add(new ClosePath());
            }
        }
    }

    private void addDropDown(ArrayList<DataPointInfo<X, Y>> currentSeriesData, XYChart.Data<X, Y> item, X xValue, Y yValue, double x2, double y2) {
        DataPointInfo<X, Y> dropDownDataPoint = new DataPointInfo<>(true);
        dropDownDataPoint.setValues(item, xValue, yValue, x2, y2, PartOf.CURRENT, true, false);
        currentSeriesData.add(dropDownDataPoint);
    }

    private void addPoint(ArrayList<DataPointInfo<X, Y>> currentSeriesData, XYChart.Data<X, Y> item, X xValue, Y yValue, double x2, double y2, PartOf partof, boolean symbol, boolean lineTo) {
        DataPointInfo<X, Y> currentDataPoint = new DataPointInfo<>();
        currentDataPoint.setValues(item, xValue, yValue, x2, y2, partof, symbol, lineTo);
        currentSeriesData.add(currentDataPoint);
    }

    private int findNextCurrent(ArrayList<DataPointInfo<X, Y>> points, int index) {
        for (int i2 = index + 1; i2 < points.size(); i2++) {
            if (points.get(i2).partOf.equals(PartOf.CURRENT)) {
                return i2;
            }
        }
        return -1;
    }

    private int findPreviousCurrent(ArrayList<DataPointInfo<X, Y>> points, int index) {
        for (int i2 = index - 1; i2 >= 0; i2--) {
            if (points.get(i2).partOf.equals(PartOf.CURRENT)) {
                return i2;
            }
        }
        return -1;
    }

    private int findPreviousPrevious(ArrayList<DataPointInfo<X, Y>> points, int index) {
        for (int i2 = index - 1; i2 >= 0; i2--) {
            if (points.get(i2).partOf.equals(PartOf.PREVIOUS)) {
                return i2;
            }
        }
        return -1;
    }

    private int findNextPrevious(ArrayList<DataPointInfo<X, Y>> points, int index) {
        for (int i2 = index + 1; i2 < points.size(); i2++) {
            if (points.get(i2).partOf.equals(PartOf.PREVIOUS)) {
                return i2;
            }
        }
        return -1;
    }

    private void sortAggregateList(ArrayList<DataPointInfo<X, Y>> aggregateList) {
        Collections.sort(aggregateList, (o1, o2) -> {
            XYChart.Data<X, Y> d1 = o1.dataItem;
            XYChart.Data<X, Y> d2 = o2.dataItem;
            double val1 = getXAxis().toNumericValue(d1.getXValue());
            double val2 = getXAxis().toNumericValue(d2.getXValue());
            if (val1 < val2) {
                return -1;
            }
            return val1 == val2 ? 0 : 1;
        });
    }

    private double interpolate(double lowX, double lowY, double highX, double highY, double x2) {
        return (((highY - lowY) / (highX - lowX)) * (x2 - lowX)) + lowY;
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

    /* loaded from: jfxrt.jar:javafx/scene/chart/StackedAreaChart$DataPointInfo.class */
    static final class DataPointInfo<X, Y> {

        /* renamed from: x, reason: collision with root package name */
        X f12647x;

        /* renamed from: y, reason: collision with root package name */
        Y f12648y;
        double displayX;
        double displayY;
        XYChart.Data<X, Y> dataItem;
        PartOf partOf;
        boolean skipSymbol;
        boolean lineTo;
        boolean dropDown;

        DataPointInfo() {
            this.skipSymbol = false;
            this.lineTo = false;
            this.dropDown = false;
        }

        DataPointInfo(XYChart.Data<X, Y> item, X x2, Y y2, PartOf partOf) {
            this.skipSymbol = false;
            this.lineTo = false;
            this.dropDown = false;
            this.dataItem = item;
            this.f12647x = x2;
            this.f12648y = y2;
            this.partOf = partOf;
        }

        DataPointInfo(boolean dropDown) {
            this.skipSymbol = false;
            this.lineTo = false;
            this.dropDown = false;
            this.dropDown = dropDown;
        }

        void setValues(XYChart.Data<X, Y> item, X x2, Y y2, double dx, double dy, PartOf partOf, boolean skipSymbol, boolean lineTo) {
            this.dataItem = item;
            this.f12647x = x2;
            this.f12648y = y2;
            this.displayX = dx;
            this.displayY = dy;
            this.partOf = partOf;
            this.skipSymbol = skipSymbol;
            this.lineTo = lineTo;
        }

        public final X getX() {
            return this.f12647x;
        }

        public final Y getY() {
            return this.f12648y;
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/chart/StackedAreaChart$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<StackedAreaChart<?, ?>, Boolean> CREATE_SYMBOLS = new CssMetaData<StackedAreaChart<?, ?>, Boolean>("-fx-create-symbols", BooleanConverter.getInstance(), Boolean.TRUE) { // from class: javafx.scene.chart.StackedAreaChart.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(StackedAreaChart<?, ?> node) {
                return ((StackedAreaChart) node).createSymbols == null || !((StackedAreaChart) node).createSymbols.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(StackedAreaChart<?, ?> node) {
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
