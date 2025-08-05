package javafx.scene.chart;

import javafx.scene.chart.LineChartBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/chart/LineChartBuilder.class */
public class LineChartBuilder<X, Y, B extends LineChartBuilder<X, Y, B>> extends XYChartBuilder<X, Y, B> {
    private boolean __set;
    private boolean createSymbols;
    private Axis<X> XAxis;
    private Axis<Y> YAxis;

    protected LineChartBuilder() {
    }

    public static <X, Y> LineChartBuilder<X, Y, ?> create() {
        return new LineChartBuilder<>();
    }

    public void applyTo(LineChart<X, Y> x2) {
        super.applyTo((XYChart) x2);
        if (this.__set) {
            x2.setCreateSymbols(this.createSymbols);
        }
    }

    public B createSymbols(boolean x2) {
        this.createSymbols = x2;
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
    public LineChart<X, Y> build2() {
        LineChart<X, Y> x2 = new LineChart<>(this.XAxis, this.YAxis);
        applyTo((LineChart) x2);
        return x2;
    }
}
