package javafx.scene.chart;

import com.sun.javafx.charts.Legend;
import java.util.Iterator;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:javafx/scene/chart/ScatterChart.class */
public class ScatterChart<X, Y> extends XYChart<X, Y> {
    private Legend legend;

    public ScatterChart(@NamedArg("xAxis") Axis<X> xAxis, @NamedArg("yAxis") Axis<Y> yAxis) {
        this(xAxis, yAxis, FXCollections.observableArrayList());
    }

    public ScatterChart(@NamedArg("xAxis") Axis<X> xAxis, @NamedArg("yAxis") Axis<Y> yAxis, @NamedArg("data") ObservableList<XYChart.Series<X, Y>> data) {
        super(xAxis, yAxis);
        this.legend = new Legend();
        setLegend(this.legend);
        setData(data);
    }

    @Override // javafx.scene.chart.XYChart
    protected void dataItemAdded(XYChart.Series<X, Y> series, int itemIndex, XYChart.Data<X, Y> item) {
        Node symbol = item.getNode();
        if (symbol == null) {
            symbol = new StackPane();
            symbol.setAccessibleRole(AccessibleRole.TEXT);
            symbol.setAccessibleRoleDescription("Point");
            symbol.focusTraversableProperty().bind(Platform.accessibilityActiveProperty());
            item.setNode(symbol);
        }
        symbol.getStyleClass().setAll("chart-symbol", "series" + getData().indexOf(series), "data" + itemIndex, series.defaultColorStyleClass);
        if (shouldAnimate()) {
            symbol.setOpacity(0.0d);
            getPlotChildren().add(symbol);
            FadeTransition ft = new FadeTransition(Duration.millis(500.0d), symbol);
            ft.setToValue(1.0d);
            ft.play();
            return;
        }
        getPlotChildren().add(symbol);
    }

    @Override // javafx.scene.chart.XYChart
    protected void dataItemRemoved(XYChart.Data<X, Y> item, XYChart.Series<X, Y> series) {
        Node symbol = item.getNode();
        if (symbol != null) {
            symbol.focusTraversableProperty().unbind();
        }
        if (shouldAnimate()) {
            FadeTransition ft = new FadeTransition(Duration.millis(500.0d), symbol);
            ft.setToValue(0.0d);
            ft.setOnFinished(actionEvent -> {
                getPlotChildren().remove(symbol);
                removeDataItemFromDisplay(series, item);
                symbol.setOpacity(1.0d);
            });
            ft.play();
            return;
        }
        getPlotChildren().remove(symbol);
        removeDataItemFromDisplay(series, item);
    }

    @Override // javafx.scene.chart.XYChart
    protected void dataItemChanged(XYChart.Data<X, Y> item) {
    }

    @Override // javafx.scene.chart.XYChart
    protected void seriesAdded(XYChart.Series<X, Y> series, int seriesIndex) {
        for (int j2 = 0; j2 < series.getData().size(); j2++) {
            dataItemAdded(series, j2, series.getData().get(j2));
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
                Node symbol = d2.getNode();
                FadeTransition ft = new FadeTransition(Duration.millis(500.0d), symbol);
                ft.setToValue(0.0d);
                ft.setOnFinished(actionEvent -> {
                    getPlotChildren().remove(symbol);
                    symbol.setOpacity(1.0d);
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

    @Override // javafx.scene.chart.XYChart
    protected void layoutPlotChildren() {
        Node symbol;
        for (int seriesIndex = 0; seriesIndex < getDataSize(); seriesIndex++) {
            XYChart.Series<X, Y> series = getData().get(seriesIndex);
            Iterator<XYChart.Data<X, Y>> it = getDisplayedDataIterator(series);
            while (it.hasNext()) {
                XYChart.Data<X, Y> item = it.next();
                double x2 = getXAxis().getDisplayPosition(item.getCurrentX());
                double y2 = getYAxis().getDisplayPosition(item.getCurrentY());
                if (!Double.isNaN(x2) && !Double.isNaN(y2) && (symbol = item.getNode()) != null) {
                    double w2 = symbol.prefWidth(-1.0d);
                    double h2 = symbol.prefHeight(-1.0d);
                    symbol.resizeRelocate(x2 - (w2 / 2.0d), y2 - (h2 / 2.0d), w2, h2);
                }
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
                if (!series.getData().isEmpty() && series.getData().get(0).getNode() != null) {
                    legenditem.getSymbol().getStyleClass().addAll(series.getData().get(0).getNode().getStyleClass());
                }
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
