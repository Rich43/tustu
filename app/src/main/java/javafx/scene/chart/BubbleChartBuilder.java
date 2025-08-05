package javafx.scene.chart;

import javafx.scene.chart.BubbleChartBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/chart/BubbleChartBuilder.class */
public class BubbleChartBuilder<X, Y, B extends BubbleChartBuilder<X, Y, B>> extends XYChartBuilder<X, Y, B> {
    private Axis<X> XAxis;
    private Axis<Y> YAxis;

    protected BubbleChartBuilder() {
    }

    public static <X, Y> BubbleChartBuilder<X, Y, ?> create() {
        return new BubbleChartBuilder<>();
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
    public BubbleChart<X, Y> build2() {
        BubbleChart<X, Y> x2 = new BubbleChart<>(this.XAxis, this.YAxis);
        applyTo((XYChart) x2);
        return x2;
    }
}
