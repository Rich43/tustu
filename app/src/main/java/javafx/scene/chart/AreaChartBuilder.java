package javafx.scene.chart;

import javafx.scene.chart.AreaChartBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/chart/AreaChartBuilder.class */
public class AreaChartBuilder<X, Y, B extends AreaChartBuilder<X, Y, B>> extends XYChartBuilder<X, Y, B> {
    private Axis<X> XAxis;
    private Axis<Y> YAxis;

    protected AreaChartBuilder() {
    }

    public static <X, Y> AreaChartBuilder<X, Y, ?> create() {
        return new AreaChartBuilder<>();
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
    public AreaChart<X, Y> build2() {
        AreaChart<X, Y> x2 = new AreaChart<>(this.XAxis, this.YAxis);
        applyTo((XYChart) x2);
        return x2;
    }
}
