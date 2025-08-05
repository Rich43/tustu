package javafx.scene.chart;

import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChartBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/chart/PieChartBuilder.class */
public class PieChartBuilder<B extends PieChartBuilder<B>> extends ChartBuilder<B> {
    private int __set;
    private boolean clockwise;
    private ObservableList<PieChart.Data> data;
    private double labelLineLength;
    private boolean labelsVisible;
    private double startAngle;

    protected PieChartBuilder() {
    }

    public static PieChartBuilder<?> create() {
        return new PieChartBuilder<>();
    }

    public void applyTo(PieChart x2) {
        super.applyTo((Chart) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setClockwise(this.clockwise);
        }
        if ((set & 2) != 0) {
            x2.setData(this.data);
        }
        if ((set & 4) != 0) {
            x2.setLabelLineLength(this.labelLineLength);
        }
        if ((set & 8) != 0) {
            x2.setLabelsVisible(this.labelsVisible);
        }
        if ((set & 16) != 0) {
            x2.setStartAngle(this.startAngle);
        }
    }

    public B clockwise(boolean x2) {
        this.clockwise = x2;
        this.__set |= 1;
        return this;
    }

    public B data(ObservableList<PieChart.Data> x2) {
        this.data = x2;
        this.__set |= 2;
        return this;
    }

    public B labelLineLength(double x2) {
        this.labelLineLength = x2;
        this.__set |= 4;
        return this;
    }

    public B labelsVisible(boolean x2) {
        this.labelsVisible = x2;
        this.__set |= 8;
        return this;
    }

    public B startAngle(double x2) {
        this.startAngle = x2;
        this.__set |= 16;
        return this;
    }

    @Override // javafx.scene.layout.RegionBuilder, javafx.util.Builder
    /* renamed from: build */
    public PieChart build2() {
        PieChart x2 = new PieChart();
        applyTo(x2);
        return x2;
    }
}
