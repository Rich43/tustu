package javafx.scene.chart;

import javafx.scene.chart.ScatterChartBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/chart/ScatterChartBuilder.class */
public class ScatterChartBuilder<X, Y, B extends ScatterChartBuilder<X, Y, B>> extends XYChartBuilder<X, Y, B> {
    private Axis<X> XAxis;
    private Axis<Y> YAxis;

    protected ScatterChartBuilder() {
    }

    public static <X, Y> ScatterChartBuilder<X, Y, ?> create() {
        return new ScatterChartBuilder<>();
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
    public ScatterChart<X, Y> build2() {
        ScatterChart<X, Y> x2 = new ScatterChart<>(this.XAxis, this.YAxis);
        applyTo((XYChart) x2);
        return x2;
    }
}
