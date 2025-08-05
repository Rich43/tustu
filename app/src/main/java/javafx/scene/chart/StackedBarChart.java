package javafx.scene.chart;

import com.sun.javafx.charts.Legend;
import com.sun.javafx.css.converters.SizeConverter;
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
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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

/* loaded from: jfxrt.jar:javafx/scene/chart/StackedBarChart.class */
public class StackedBarChart<X, Y> extends XYChart<X, Y> {
    private Map<XYChart.Series, Map<String, List<XYChart.Data<X, Y>>>> seriesCategoryMap;
    private Legend legend;
    private final Orientation orientation;
    private CategoryAxis categoryAxis;
    private ValueAxis valueAxis;
    private int seriesDefaultColorIndex;
    private Map<XYChart.Series<X, Y>, String> seriesDefaultColorMap;
    private ListChangeListener<String> categoriesListener;
    private DoubleProperty categoryGap;
    private static final PseudoClass VERTICAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("vertical");
    private static final PseudoClass HORIZONTAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("horizontal");

    public double getCategoryGap() {
        return this.categoryGap.getValue2().doubleValue();
    }

    public void setCategoryGap(double value) {
        this.categoryGap.setValue((Number) Double.valueOf(value));
    }

    public DoubleProperty categoryGapProperty() {
        return this.categoryGap;
    }

    public StackedBarChart(@NamedArg("xAxis") Axis<X> xAxis, @NamedArg("yAxis") Axis<Y> yAxis) {
        this(xAxis, yAxis, FXCollections.observableArrayList());
    }

    public StackedBarChart(@NamedArg("xAxis") Axis<X> xAxis, @NamedArg("yAxis") Axis<Y> yAxis, @NamedArg("data") ObservableList<XYChart.Series<X, Y>> data) {
        super(xAxis, yAxis);
        this.seriesCategoryMap = new HashMap();
        this.legend = new Legend();
        this.seriesDefaultColorIndex = 0;
        this.seriesDefaultColorMap = new HashMap();
        this.categoriesListener = new ListChangeListener<String>() { // from class: javafx.scene.chart.StackedBarChart.1
            @Override // javafx.collections.ListChangeListener
            public void onChanged(ListChangeListener.Change<? extends String> c2) {
                while (c2.next()) {
                    for (String cat : c2.getRemoved()) {
                        for (XYChart.Series<X, Y> series : StackedBarChart.this.getData()) {
                            for (XYChart.Data<X, Y> data2 : series.getData()) {
                                Orientation orientation = StackedBarChart.this.orientation;
                                Orientation unused = StackedBarChart.this.orientation;
                                if (cat.equals(orientation == Orientation.VERTICAL ? data2.getXValue() : data2.getYValue())) {
                                    boolean animatedOn = StackedBarChart.this.getAnimated();
                                    StackedBarChart.this.setAnimated(false);
                                    StackedBarChart.this.dataItemRemoved(data2, series);
                                    StackedBarChart.this.setAnimated(animatedOn);
                                }
                            }
                        }
                        StackedBarChart.this.requestChartLayout();
                    }
                }
            }
        };
        this.categoryGap = new StyleableDoubleProperty(10.0d) { // from class: javafx.scene.chart.StackedBarChart.2
            @Override // javafx.beans.property.DoublePropertyBase
            protected void invalidated() {
                get();
                StackedBarChart.this.requestChartLayout();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return StackedBarChart.this;
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
        getStyleClass().add("stacked-bar-chart");
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
        this.categoryAxis.getCategories().addListener(this.categoriesListener);
    }

    public StackedBarChart(@NamedArg("xAxis") Axis<X> xAxis, @NamedArg("yAxis") Axis<Y> yAxis, @NamedArg("data") ObservableList<XYChart.Series<X, Y>> data, @NamedArg("categoryGap") double categoryGap) {
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
        Map<String, List<XYChart.Data<X, Y>>> map = this.seriesCategoryMap.get(series);
        if (map == null) {
            map = new HashMap();
            this.seriesCategoryMap.put(series, map);
        }
        List<XYChart.Data<X, Y>> arrayList = map.get(str) != null ? map.get(str) : new ArrayList<>();
        arrayList.add(data);
        map.put(str, arrayList);
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
            Timeline t2 = createDataRemoveTimeline(item, bar, series);
            t2.setOnFinished(event -> {
                removeDataItemFromDisplay(series, item);
            });
            t2.play();
        } else {
            getPlotChildren().remove(bar);
            removeDataItemFromDisplay(series, item);
        }
    }

    @Override // javafx.scene.chart.XYChart
    protected void dataItemChanged(XYChart.Data<X, Y> data) {
        double dDoubleValue;
        double dDoubleValue2;
        if (this.orientation == Orientation.VERTICAL) {
            dDoubleValue = ((Number) data.getYValue()).doubleValue();
            dDoubleValue2 = ((Number) getCurrentDisplayedYValue(data)).doubleValue();
        } else {
            dDoubleValue = ((Number) data.getXValue()).doubleValue();
            dDoubleValue2 = ((Number) getCurrentDisplayedXValue(data)).doubleValue();
        }
        if (dDoubleValue2 > 0.0d && dDoubleValue < 0.0d) {
            data.getNode().getStyleClass().add("negative");
        } else if (dDoubleValue2 < 0.0d && dDoubleValue > 0.0d) {
            data.getNode().getStyleClass().remove("negative");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void animateDataAdd(XYChart.Data<X, Y> data, Node bar) {
        if (this.orientation == Orientation.VERTICAL) {
            double barVal = ((Number) data.getYValue()).doubleValue();
            if (barVal < 0.0d) {
                bar.getStyleClass().add("negative");
            }
            data.setYValue(getYAxis().toRealValue(getYAxis().getZeroPosition()));
            setCurrentDisplayedYValue(data, getYAxis().toRealValue(getYAxis().getZeroPosition()));
            getPlotChildren().add(bar);
            data.setYValue(getYAxis().toRealValue(barVal));
            animate(new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(currentDisplayedYValueProperty(data), getCurrentDisplayedYValue(data))), new KeyFrame(Duration.millis(700.0d), new KeyValue(currentDisplayedYValueProperty(data), data.getYValue(), Interpolator.EASE_BOTH))));
            return;
        }
        double barVal2 = ((Number) data.getXValue()).doubleValue();
        if (barVal2 < 0.0d) {
            bar.getStyleClass().add("negative");
        }
        data.setXValue(getXAxis().toRealValue(getXAxis().getZeroPosition()));
        setCurrentDisplayedXValue(data, getXAxis().toRealValue(getXAxis().getZeroPosition()));
        getPlotChildren().add(bar);
        data.setXValue(getXAxis().toRealValue(barVal2));
        animate(new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(currentDisplayedXValueProperty(data), getCurrentDisplayedXValue(data))), new KeyFrame(Duration.millis(700.0d), new KeyValue(currentDisplayedXValueProperty(data), data.getXValue(), Interpolator.EASE_BOTH))));
    }

    @Override // javafx.scene.chart.XYChart
    protected void seriesAdded(XYChart.Series<X, Y> series, int i2) {
        String str;
        this.seriesDefaultColorMap.put(series, "default-color" + (this.seriesDefaultColorIndex % 8));
        this.seriesDefaultColorIndex++;
        HashMap map = new HashMap();
        for (int i3 = 0; i3 < series.getData().size(); i3++) {
            XYChart.Data<X, Y> data = series.getData().get(i3);
            Node nodeCreateBar = createBar(series, i2, data, i3);
            if (this.orientation == Orientation.VERTICAL) {
                str = (String) data.getXValue();
            } else {
                str = (String) data.getYValue();
            }
            List arrayList = map.get(str) != null ? (List) map.get(str) : new ArrayList();
            arrayList.add(data);
            map.put(str, arrayList);
            if (shouldAnimate()) {
                animateDataAdd(data, nodeCreateBar);
            } else {
                if ((this.orientation == Orientation.VERTICAL ? ((Number) data.getYValue()).doubleValue() : ((Number) data.getXValue()).doubleValue()) < 0.0d) {
                    nodeCreateBar.getStyleClass().add("negative");
                }
                getPlotChildren().add(nodeCreateBar);
            }
        }
        if (map.size() > 0) {
            this.seriesCategoryMap.put(series, map);
        }
    }

    private Timeline createDataRemoveTimeline(XYChart.Data<X, Y> item, Node bar, XYChart.Series<X, Y> series) {
        Timeline t2 = new Timeline();
        if (this.orientation == Orientation.VERTICAL) {
            item.setYValue(getYAxis().toRealValue(getYAxis().getZeroPosition()));
            t2.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(currentDisplayedYValueProperty(item), getCurrentDisplayedYValue(item))), new KeyFrame(Duration.millis(700.0d), (EventHandler<ActionEvent>) actionEvent -> {
                getPlotChildren().remove(bar);
            }, new KeyValue(currentDisplayedYValueProperty(item), item.getYValue(), Interpolator.EASE_BOTH)));
        } else {
            item.setXValue(getXAxis().toRealValue(getXAxis().getZeroPosition()));
            t2.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(currentDisplayedXValueProperty(item), getCurrentDisplayedXValue(item))), new KeyFrame(Duration.millis(700.0d), (EventHandler<ActionEvent>) actionEvent2 -> {
                getPlotChildren().remove(bar);
            }, new KeyValue(currentDisplayedXValueProperty(item), item.getXValue(), Interpolator.EASE_BOTH)));
        }
        return t2;
    }

    @Override // javafx.scene.chart.XYChart
    protected void seriesRemoved(XYChart.Series<X, Y> series) {
        this.seriesDefaultColorIndex--;
        if (shouldAnimate()) {
            ParallelTransition pt = new ParallelTransition();
            pt.setOnFinished(event -> {
                removeSeriesFromDisplay(series);
                requestChartLayout();
            });
            for (XYChart.Data<X, Y> d2 : series.getData()) {
                Node bar = d2.getNode();
                if (getSeriesSize() > 1) {
                    for (int j2 = 0; j2 < series.getData().size(); j2++) {
                        XYChart.Data<X, Y> item = series.getData().get(j2);
                        Timeline t2 = createDataRemoveTimeline(item, bar, series);
                        pt.getChildren().add(t2);
                    }
                } else {
                    FadeTransition ft = new FadeTransition(Duration.millis(700.0d), bar);
                    ft.setFromValue(1.0d);
                    ft.setToValue(0.0d);
                    ft.setOnFinished(actionEvent -> {
                        getPlotChildren().remove(bar);
                        bar.setOpacity(1.0d);
                    });
                    pt.getChildren().add(ft);
                }
            }
            pt.play();
            return;
        }
        for (XYChart.Data<X, Y> d3 : series.getData()) {
            getPlotChildren().remove(d3.getNode());
        }
        removeSeriesFromDisplay(series);
        requestChartLayout();
    }

    @Override // javafx.scene.chart.XYChart
    protected void updateAxisRange() {
        boolean z2 = this.categoryAxis == getXAxis();
        if (this.categoryAxis.isAutoRanging()) {
            ArrayList arrayList = new ArrayList();
            Iterator<XYChart.Series<X, Y>> it = getData().iterator();
            while (it.hasNext()) {
                for (XYChart.Data<X, Y> data : it.next().getData()) {
                    if (data != null) {
                        arrayList.add(z2 ? data.getXValue() : data.getYValue());
                    }
                }
            }
            this.categoryAxis.invalidateRange(arrayList);
        }
        if (this.valueAxis.isAutoRanging()) {
            ArrayList arrayList2 = new ArrayList();
            for (String str : this.categoryAxis.getAllDataCategories()) {
                double numericValue = 0.0d;
                double numericValue2 = 0.0d;
                Iterator<XYChart.Series<X, Y>> displayedSeriesIterator = getDisplayedSeriesIterator();
                while (displayedSeriesIterator.hasNext()) {
                    for (XYChart.Data<X, Y> data2 : getDataItem(displayedSeriesIterator.next(), str)) {
                        if (data2 != null) {
                            boolean zContains = data2.getNode().getStyleClass().contains("negative");
                            Number yValue = z2 ? data2.getYValue() : data2.getXValue();
                            if (!zContains) {
                                numericValue2 += this.valueAxis.toNumericValue((ValueAxis) yValue);
                            } else {
                                numericValue += this.valueAxis.toNumericValue((ValueAxis) yValue);
                            }
                        }
                    }
                }
                arrayList2.add(Double.valueOf(numericValue2));
                arrayList2.add(Double.valueOf(numericValue));
            }
            this.valueAxis.invalidateRange(arrayList2);
        }
    }

    @Override // javafx.scene.chart.XYChart
    protected void layoutPlotChildren() {
        double categoryPos;
        double valNumber;
        double bottom;
        double top;
        double catSpace = this.categoryAxis.getCategorySpacing();
        double availableBarSpace = catSpace - getCategoryGap();
        double barOffset = -((catSpace - getCategoryGap()) / 2.0d);
        this.valueAxis.getLowerBound();
        this.valueAxis.getUpperBound();
        for (String category : this.categoryAxis.getCategories()) {
            double currentPositiveValue = 0.0d;
            double currentNegativeValue = 0.0d;
            Iterator<XYChart.Series<X, Y>> seriesIterator = getDisplayedSeriesIterator();
            while (seriesIterator.hasNext()) {
                XYChart.Series<X, Y> series = seriesIterator.next();
                for (XYChart.Data<X, Y> item : getDataItem(series, category)) {
                    if (item != null) {
                        Node bar = item.getNode();
                        X xValue = getCurrentDisplayedXValue(item);
                        Y yValue = getCurrentDisplayedYValue(item);
                        if (this.orientation == Orientation.VERTICAL) {
                            categoryPos = getXAxis().getDisplayPosition(xValue);
                            valNumber = getYAxis().toNumericValue(yValue);
                        } else {
                            categoryPos = getYAxis().getDisplayPosition(yValue);
                            valNumber = getXAxis().toNumericValue(xValue);
                        }
                        boolean isNegative = bar.getStyleClass().contains("negative");
                        if (!isNegative) {
                            bottom = this.valueAxis.getDisplayPosition((ValueAxis) Double.valueOf(currentPositiveValue));
                            top = this.valueAxis.getDisplayPosition((ValueAxis) Double.valueOf(currentPositiveValue + valNumber));
                            currentPositiveValue += valNumber;
                        } else {
                            bottom = this.valueAxis.getDisplayPosition((ValueAxis) Double.valueOf(currentNegativeValue + valNumber));
                            top = this.valueAxis.getDisplayPosition((ValueAxis) Double.valueOf(currentNegativeValue));
                            currentNegativeValue += valNumber;
                        }
                        if (this.orientation == Orientation.VERTICAL) {
                            bar.resizeRelocate(categoryPos + barOffset, top, availableBarSpace, bottom - top);
                        } else {
                            bar.resizeRelocate(bottom, categoryPos + barOffset, top - bottom, availableBarSpace);
                        }
                    }
                }
            }
        }
    }

    @Override // javafx.scene.chart.XYChart
    int getSeriesSize() {
        int count = 0;
        Iterator<XYChart.Series<X, Y>> seriesIterator = getDisplayedSeriesIterator();
        while (seriesIterator.hasNext()) {
            seriesIterator.next();
            count++;
        }
        return count;
    }

    @Override // javafx.scene.chart.XYChart
    protected void updateLegend() {
        this.legend.getItems().clear();
        if (getData() != null) {
            for (int seriesIndex = 0; seriesIndex < getData().size(); seriesIndex++) {
                XYChart.Series<X, Y> series = getData().get(seriesIndex);
                Legend.LegendItem legenditem = new Legend.LegendItem(series.getName());
                String defaultColorStyleClass = this.seriesDefaultColorMap.get(series);
                legenditem.getSymbol().getStyleClass().addAll("chart-bar", "series" + seriesIndex, "bar-legend-symbol", defaultColorStyleClass);
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

    private Node createBar(XYChart.Series series, int seriesIndex, XYChart.Data item, int itemIndex) {
        Node bar = item.getNode();
        if (bar == null) {
            bar = new StackPane();
            bar.setAccessibleRole(AccessibleRole.TEXT);
            bar.setAccessibleRoleDescription("Bar");
            bar.focusTraversableProperty().bind(Platform.accessibilityActiveProperty());
            item.setNode(bar);
        }
        String defaultColorStyleClass = this.seriesDefaultColorMap.get(series);
        bar.getStyleClass().setAll("chart-bar", "series" + seriesIndex, "data" + itemIndex, defaultColorStyleClass);
        return bar;
    }

    private List<XYChart.Data<X, Y>> getDataItem(XYChart.Series<X, Y> series, String category) {
        Map<String, List<XYChart.Data<X, Y>>> catmap = this.seriesCategoryMap.get(series);
        if (catmap != null && catmap.get(category) != null) {
            return catmap.get(category);
        }
        return new ArrayList();
    }

    /* loaded from: jfxrt.jar:javafx/scene/chart/StackedBarChart$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<StackedBarChart<?, ?>, Number> CATEGORY_GAP = new CssMetaData<StackedBarChart<?, ?>, Number>("-fx-category-gap", SizeConverter.getInstance(), Double.valueOf(10.0d)) { // from class: javafx.scene.chart.StackedBarChart.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(StackedBarChart<?, ?> node) {
                return ((StackedBarChart) node).categoryGap == null || !((StackedBarChart) node).categoryGap.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(StackedBarChart<?, ?> node) {
                return (StyleableProperty) node.categoryGapProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(XYChart.getClassCssMetaData());
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
