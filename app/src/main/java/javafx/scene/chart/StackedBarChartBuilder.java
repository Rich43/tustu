package javafx.scene.chart;

import javafx.scene.chart.StackedBarChartBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/chart/StackedBarChartBuilder.class */
public class StackedBarChartBuilder<X, Y, B extends StackedBarChartBuilder<X, Y, B>> extends XYChartBuilder<X, Y, B> {
    private boolean __set;
    private double categoryGap;
    private Axis<X> XAxis;
    private Axis<Y> YAxis;

    protected StackedBarChartBuilder() {
    }

    public static <X, Y> StackedBarChartBuilder<X, Y, ?> create() {
        return new StackedBarChartBuilder<>();
    }

    public void applyTo(StackedBarChart<X, Y> x2) {
        super.applyTo((XYChart) x2);
        if (this.__set) {
            x2.setCategoryGap(this.categoryGap);
        }
    }

    public B categoryGap(double x2) {
        this.categoryGap = x2;
        this.__set = true;
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
    public StackedBarChart<X, Y> build2() {
        StackedBarChart<X, Y> x2 = new StackedBarChart<>(this.XAxis, this.YAxis);
        applyTo((StackedBarChart) x2);
        return x2;
    }
}
