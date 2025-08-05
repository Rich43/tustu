package javafx.scene.chart;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChartBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/chart/XYChartBuilder.class */
public abstract class XYChartBuilder<X, Y, B extends XYChartBuilder<X, Y, B>> extends ChartBuilder<B> {
    private int __set;
    private boolean alternativeColumnFillVisible;
    private boolean alternativeRowFillVisible;
    private ObservableList<XYChart.Series<X, Y>> data;
    private boolean horizontalGridLinesVisible;
    private boolean horizontalZeroLineVisible;
    private boolean verticalGridLinesVisible;
    private boolean verticalZeroLineVisible;
    private Axis<X> XAxis;
    private Axis<Y> YAxis;

    protected XYChartBuilder() {
    }

    public void applyTo(XYChart<X, Y> x2) {
        super.applyTo((Chart) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setAlternativeColumnFillVisible(this.alternativeColumnFillVisible);
        }
        if ((set & 2) != 0) {
            x2.setAlternativeRowFillVisible(this.alternativeRowFillVisible);
        }
        if ((set & 4) != 0) {
            x2.setData(this.data);
        }
        if ((set & 8) != 0) {
            x2.setHorizontalGridLinesVisible(this.horizontalGridLinesVisible);
        }
        if ((set & 16) != 0) {
            x2.setHorizontalZeroLineVisible(this.horizontalZeroLineVisible);
        }
        if ((set & 32) != 0) {
            x2.setVerticalGridLinesVisible(this.verticalGridLinesVisible);
        }
        if ((set & 64) != 0) {
            x2.setVerticalZeroLineVisible(this.verticalZeroLineVisible);
        }
    }

    public B alternativeColumnFillVisible(boolean x2) {
        this.alternativeColumnFillVisible = x2;
        this.__set |= 1;
        return this;
    }

    public B alternativeRowFillVisible(boolean x2) {
        this.alternativeRowFillVisible = x2;
        this.__set |= 2;
        return this;
    }

    public B data(ObservableList<XYChart.Series<X, Y>> x2) {
        this.data = x2;
        this.__set |= 4;
        return this;
    }

    public B horizontalGridLinesVisible(boolean x2) {
        this.horizontalGridLinesVisible = x2;
        this.__set |= 8;
        return this;
    }

    public B horizontalZeroLineVisible(boolean x2) {
        this.horizontalZeroLineVisible = x2;
        this.__set |= 16;
        return this;
    }

    public B verticalGridLinesVisible(boolean x2) {
        this.verticalGridLinesVisible = x2;
        this.__set |= 32;
        return this;
    }

    public B verticalZeroLineVisible(boolean x2) {
        this.verticalZeroLineVisible = x2;
        this.__set |= 64;
        return this;
    }

    public B XAxis(Axis<X> x2) {
        this.XAxis = x2;
        return this;
    }

    public B YAxis(Axis<Y> x2) {
        this.YAxis = x2;
        return this;
    }
}
