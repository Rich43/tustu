package javafx.scene.chart;

import javafx.scene.chart.StackedAreaChartBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/chart/StackedAreaChartBuilder.class */
public class StackedAreaChartBuilder<X, Y, B extends StackedAreaChartBuilder<X, Y, B>> extends XYChartBuilder<X, Y, B> {
    private Axis<X> XAxis;
    private Axis<Y> YAxis;

    protected StackedAreaChartBuilder() {
    }

    public static <X, Y> StackedAreaChartBuilder<X, Y, ?> create() {
        return new StackedAreaChartBuilder<>();
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
    public StackedAreaChart<X, Y> build2() {
        StackedAreaChart<X, Y> x2 = new StackedAreaChart<>(this.XAxis, this.YAxis);
        applyTo((XYChart) x2);
        return x2;
    }
}
