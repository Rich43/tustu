package javafx.scene.chart;

import java.lang.Number;
import javafx.scene.chart.ValueAxisBuilder;
import javafx.util.StringConverter;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/chart/ValueAxisBuilder.class */
public abstract class ValueAxisBuilder<T extends Number, B extends ValueAxisBuilder<T, B>> extends AxisBuilder<T, B> {
    private int __set;
    private double lowerBound;
    private int minorTickCount;
    private double minorTickLength;
    private boolean minorTickVisible;
    private StringConverter<T> tickLabelFormatter;
    private double upperBound;

    protected ValueAxisBuilder() {
    }

    public void applyTo(ValueAxis<T> x2) {
        super.applyTo((Axis<T>) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setLowerBound(this.lowerBound);
        }
        if ((set & 2) != 0) {
            x2.setMinorTickCount(this.minorTickCount);
        }
        if ((set & 4) != 0) {
            x2.setMinorTickLength(this.minorTickLength);
        }
        if ((set & 8) != 0) {
            x2.setMinorTickVisible(this.minorTickVisible);
        }
        if ((set & 16) != 0) {
            x2.setTickLabelFormatter(this.tickLabelFormatter);
        }
        if ((set & 32) != 0) {
            x2.setUpperBound(this.upperBound);
        }
    }

    public B lowerBound(double x2) {
        this.lowerBound = x2;
        this.__set |= 1;
        return this;
    }

    public B minorTickCount(int x2) {
        this.minorTickCount = x2;
        this.__set |= 2;
        return this;
    }

    public B minorTickLength(double x2) {
        this.minorTickLength = x2;
        this.__set |= 4;
        return this;
    }

    public B minorTickVisible(boolean x2) {
        this.minorTickVisible = x2;
        this.__set |= 8;
        return this;
    }

    public B tickLabelFormatter(StringConverter<T> x2) {
        this.tickLabelFormatter = x2;
        this.__set |= 16;
        return this;
    }

    public B upperBound(double x2) {
        this.upperBound = x2;
        this.__set |= 32;
        return this;
    }
}
