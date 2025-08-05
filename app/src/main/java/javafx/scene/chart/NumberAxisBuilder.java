package javafx.scene.chart;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/chart/NumberAxisBuilder.class */
public final class NumberAxisBuilder extends ValueAxisBuilder<Number, NumberAxisBuilder> {
    private int __set;
    private boolean forceZeroInRange;
    private double tickUnit;

    protected NumberAxisBuilder() {
    }

    public static NumberAxisBuilder create() {
        return new NumberAxisBuilder();
    }

    public void applyTo(NumberAxis x2) {
        super.applyTo((ValueAxis) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setForceZeroInRange(this.forceZeroInRange);
        }
        if ((set & 2) != 0) {
            x2.setTickUnit(this.tickUnit);
        }
    }

    public NumberAxisBuilder forceZeroInRange(boolean x2) {
        this.forceZeroInRange = x2;
        this.__set |= 1;
        return this;
    }

    public NumberAxisBuilder tickUnit(double x2) {
        this.tickUnit = x2;
        this.__set |= 2;
        return this;
    }

    @Override // javafx.scene.layout.RegionBuilder, javafx.util.Builder
    /* renamed from: build */
    public NumberAxis build2() {
        NumberAxis x2 = new NumberAxis();
        applyTo(x2);
        return x2;
    }
}
