package javafx.scene.chart;

import com.sun.javafx.charts.Legend;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.beans.NamedArg;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Ellipse;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:javafx/scene/chart/BubbleChart.class */
public class BubbleChart<X, Y> extends XYChart<X, Y> {
    private Legend legend;

    public BubbleChart(@NamedArg("xAxis") Axis<X> xAxis, @NamedArg("yAxis") Axis<Y> yAxis) {
        this(xAxis, yAxis, FXCollections.observableArrayList());
    }

    public BubbleChart(@NamedArg("xAxis") Axis<X> xAxis, @NamedArg("yAxis") Axis<Y> yAxis, @NamedArg("data") ObservableList<XYChart.Series<X, Y>> data) {
        super(xAxis, yAxis);
        this.legend = new Legend();
        setLegend(this.legend);
        if (!(xAxis instanceof ValueAxis) || !(yAxis instanceof ValueAxis)) {
            throw new IllegalArgumentException("Axis type incorrect, X and Y should both be NumberAxis");
        }
        setData(data);
    }

    private static double getDoubleValue(Object number, double nullDefault) {
        return !(number instanceof Number) ? nullDefault : ((Number) number).doubleValue();
    }

    @Override // javafx.scene.chart.XYChart
    protected void layoutPlotChildren() {
        Node bubble;
        Ellipse ellipse;
        for (int seriesIndex = 0; seriesIndex < getDataSize(); seriesIndex++) {
            XYChart.Series<X, Y> series = getData().get(seriesIndex);
            Iterator<XYChart.Data<X, Y>> iter = getDisplayedDataIterator(series);
            while (iter.hasNext()) {
                XYChart.Data<X, Y> item = iter.next();
                double x2 = getXAxis().getDisplayPosition(item.getCurrentX());
                double y2 = getYAxis().getDisplayPosition(item.getCurrentY());
                if (!Double.isNaN(x2) && !Double.isNaN(y2) && (bubble = item.getNode()) != null && (bubble instanceof StackPane)) {
                    StackPane region = (StackPane) item.getNode();
                    if (region.getShape() == null) {
                        ellipse = new Ellipse(getDoubleValue(item.getExtraValue(), 1.0d), getDoubleValue(item.getExtraValue(), 1.0d));
                    } else if (region.getShape() instanceof Ellipse) {
                        ellipse = (Ellipse) region.getShape();
                    } else {
                        return;
                    }
                    ellipse.setRadiusX(getDoubleValue(item.getExtraValue(), 1.0d) * (getXAxis() instanceof NumberAxis ? Math.abs(((NumberAxis) getXAxis()).getScale()) : 1.0d));
                    ellipse.setRadiusY(getDoubleValue(item.getExtraValue(), 1.0d) * (getYAxis() instanceof NumberAxis ? Math.abs(((NumberAxis) getYAxis()).getScale()) : 1.0d));
                    region.setShape(null);
                    region.setShape(ellipse);
                    region.setScaleShape(false);
                    region.setCenterShape(false);
                    region.setCacheShape(false);
                    bubble.setLayoutX(x2);
                    bubble.setLayoutY(y2);
                }
            }
        }
    }

    @Override // javafx.scene.chart.XYChart
    protected void dataItemAdded(XYChart.Series<X, Y> series, int itemIndex, XYChart.Data<X, Y> item) {
        Node bubble = createBubble(series, getData().indexOf(series), item, itemIndex);
        if (shouldAnimate()) {
            bubble.setOpacity(0.0d);
            getPlotChildren().add(bubble);
            FadeTransition ft = new FadeTransition(Duration.millis(500.0d), bubble);
            ft.setToValue(1.0d);
            ft.play();
            return;
        }
        getPlotChildren().add(bubble);
    }

    @Override // javafx.scene.chart.XYChart
    protected void dataItemRemoved(XYChart.Data<X, Y> item, XYChart.Series<X, Y> series) {
        Node bubble = item.getNode();
        if (shouldAnimate()) {
            FadeTransition ft = new FadeTransition(Duration.millis(500.0d), bubble);
            ft.setToValue(0.0d);
            ft.setOnFinished(actionEvent -> {
                getPlotChildren().remove(bubble);
                removeDataItemFromDisplay(series, item);
                bubble.setOpacity(1.0d);
            });
            ft.play();
            return;
        }
        getPlotChildren().remove(bubble);
        removeDataItemFromDisplay(series, item);
    }

    @Override // javafx.scene.chart.XYChart
    protected void dataItemChanged(XYChart.Data<X, Y> item) {
    }

    @Override // javafx.scene.chart.XYChart
    protected void seriesAdded(XYChart.Series<X, Y> series, int seriesIndex) {
        for (int j2 = 0; j2 < series.getData().size(); j2++) {
            XYChart.Data<X, Y> item = series.getData().get(j2);
            Node bubble = createBubble(series, seriesIndex, item, j2);
            if (shouldAnimate()) {
                bubble.setOpacity(0.0d);
                getPlotChildren().add(bubble);
                FadeTransition ft = new FadeTransition(Duration.millis(500.0d), bubble);
                ft.setToValue(1.0d);
                ft.play();
            } else {
                getPlotChildren().add(bubble);
            }
        }
    }

    @Override // javafx.scene.chart.XYChart
    protected void seriesRemoved(XYChart.Series<X, Y> series) {
        if (shouldAnimate()) {
            ParallelTransition pt = new ParallelTransition();
            pt.setOnFinished(event -> {
                removeSeriesFromDisplay(series);
            });
            for (XYChart.Data<X, Y> d2 : series.getData()) {
                Node bubble = d2.getNode();
                FadeTransition ft = new FadeTransition(Duration.millis(500.0d), bubble);
                ft.setToValue(0.0d);
                ft.setOnFinished(actionEvent -> {
                    getPlotChildren().remove(bubble);
                    bubble.setOpacity(1.0d);
                });
                pt.getChildren().add(ft);
            }
            pt.play();
            return;
        }
        for (XYChart.Data<X, Y> d3 : series.getData()) {
            getPlotChildren().remove(d3.getNode());
        }
        removeSeriesFromDisplay(series);
    }

    private Node createBubble(XYChart.Series<X, Y> series, int seriesIndex, XYChart.Data<X, Y> item, int itemIndex) {
        Node bubble = item.getNode();
        if (bubble == null) {
            bubble = new StackPane();
            item.setNode(bubble);
        }
        bubble.getStyleClass().setAll("chart-bubble", "series" + seriesIndex, "data" + itemIndex, series.defaultColorStyleClass);
        return bubble;
    }

    @Override // javafx.scene.chart.XYChart
    protected void updateAxisRange() {
        Axis<X> xa = getXAxis();
        Axis<Y> ya = getYAxis();
        List<X> xData = xa.isAutoRanging() ? new ArrayList<>() : null;
        List<Y> yData = ya.isAutoRanging() ? new ArrayList<>() : null;
        boolean xIsCategory = xa instanceof CategoryAxis;
        boolean yIsCategory = ya instanceof CategoryAxis;
        if (xData != null || yData != null) {
            for (XYChart.Series<X, Y> series : getData()) {
                for (XYChart.Data<X, Y> data : series.getData()) {
                    if (xData != null) {
                        if (xIsCategory) {
                            xData.add(data.getXValue());
                        } else {
                            xData.add(xa.toRealValue(xa.toNumericValue(data.getXValue()) + getDoubleValue(data.getExtraValue(), 0.0d)));
                            xData.add(xa.toRealValue(xa.toNumericValue(data.getXValue()) - getDoubleValue(data.getExtraValue(), 0.0d)));
                        }
                    }
                    if (yData != null) {
                        if (yIsCategory) {
                            yData.add(data.getYValue());
                        } else {
                            yData.add(ya.toRealValue(ya.toNumericValue(data.getYValue()) + getDoubleValue(data.getExtraValue(), 0.0d)));
                            yData.add(ya.toRealValue(ya.toNumericValue(data.getYValue()) - getDoubleValue(data.getExtraValue(), 0.0d)));
                        }
                    }
                }
            }
            if (xData != null) {
                xa.invalidateRange(xData);
            }
            if (yData != null) {
                ya.invalidateRange(yData);
            }
        }
    }

    @Override // javafx.scene.chart.XYChart
    protected void updateLegend() {
        this.legend.getItems().clear();
        if (getData() != null) {
            for (int seriesIndex = 0; seriesIndex < getData().size(); seriesIndex++) {
                XYChart.Series<X, Y> series = getData().get(seriesIndex);
                Legend.LegendItem legenditem = new Legend.LegendItem(series.getName());
                legenditem.getSymbol().getStyleClass().addAll("series" + seriesIndex, "chart-bubble", "bubble-legend-symbol", series.defaultColorStyleClass);
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
}
