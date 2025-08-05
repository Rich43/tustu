package javafx.scene.chart;

import javafx.scene.chart.BarChartBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/chart/BarChartBuilder.class */
public class BarChartBuilder<X, Y, B extends BarChartBuilder<X, Y, B>> extends XYChartBuilder<X, Y, B> {
    private int __set;
    private double barGap;
    private double categoryGap;
    private Axis<X> XAxis;
    private Axis<Y> YAxis;

    protected BarChartBuilder() {
    }

    public static <X, Y> BarChartBuilder<X, Y, ?> create() {
        return new BarChartBuilder<>();
    }

    public void applyTo(BarChart<X, Y> x2) {
        super.applyTo((XYChart) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setBarGap(this.barGap);
        }
        if ((set & 2) != 0) {
            x2.setCategoryGap(this.categoryGap);
        }
    }

    public B barGap(double x2) {
        this.barGap = x2;
        this.__set |= 1;
        return this;
    }

    public B categoryGap(double x2) {
        this.categoryGap = x2;
        this.__set |= 2;
        return this;
    }

    @Override // javafx.scene.chart.XYChartBuilder
    public B XAxis(Axis<X> x2) {
        this.XAxis = x2;
        return this;
    }

    @Override // javafx.scene.chart.XYChartBuilder
    public B YAxis(Axis<Y> x2) {
        this.YAxis = x2;
        return this;
    }

    @Override // javafx.scene.layout.RegionBuilder, javafx.util.Builder
    /* renamed from: build */
    public BarChart<X, Y> build2() {
        BarChart<X, Y> x2 = new BarChart<>(this.XAxis, this.YAxis);
        applyTo((BarChart) x2);
        return x2;
    }
}
