package javafx.scene.control;

import javafx.geometry.Orientation;
import javafx.scene.control.SliderBuilder;
import javafx.util.Builder;
import javafx.util.StringConverter;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/SliderBuilder.class */
public class SliderBuilder<B extends SliderBuilder<B>> extends ControlBuilder<B> implements Builder<Slider> {
    private int __set;
    private double blockIncrement;
    private StringConverter<Double> labelFormatter;
    private double majorTickUnit;
    private double max;
    private double min;
    private int minorTickCount;
    private Orientation orientation;
    private boolean showTickLabels;
    private boolean showTickMarks;
    private boolean snapToTicks;
    private double value;
    private boolean valueChanging;

    protected SliderBuilder() {
    }

    public static SliderBuilder<?> create() {
        return new SliderBuilder<>();
    }

    private void __set(int i2) {
        this.__set |= 1 << i2;
    }

    public void applyTo(Slider x2) {
        super.applyTo((Control) x2);
        int set = this.__set;
        while (set != 0) {
            int i2 = Integer.numberOfTrailingZeros(set);
            set &= (1 << i2) ^ (-1);
            switch (i2) {
                case 0:
                    x2.setBlockIncrement(this.blockIncrement);
                    break;
                case 1:
                    x2.setLabelFormatter(this.labelFormatter);
                    break;
                case 2:
                    x2.setMajorTickUnit(this.majorTickUnit);
                    break;
                case 3:
                    x2.setMax(this.max);
                    break;
                case 4:
                    x2.setMin(this.min);
                    break;
                case 5:
                    x2.setMinorTickCount(this.minorTickCount);
                    break;
                case 6:
                    x2.setOrientation(this.orientation);
                    break;
                case 7:
                    x2.setShowTickLabels(this.showTickLabels);
                    break;
                case 8:
                    x2.setShowTickMarks(this.showTickMarks);
                    break;
                case 9:
                    x2.setSnapToTicks(this.snapToTicks);
                    break;
                case 10:
                    x2.setValue(this.value);
                    break;
                case 11:
                    x2.setValueChanging(this.valueChanging);
                    break;
            }
        }
    }

    public B blockIncrement(double x2) {
        this.blockIncrement = x2;
        __set(0);
        return this;
    }

    public B labelFormatter(StringConverter<Double> x2) {
        this.labelFormatter = x2;
        __set(1);
        return this;
    }

    public B majorTickUnit(double x2) {
        this.majorTickUnit = x2;
        __set(2);
        return this;
    }

    public B max(double x2) {
        this.max = x2;
        __set(3);
        return this;
    }

    public B min(double x2) {
        this.min = x2;
        __set(4);
        return this;
    }

    public B minorTickCount(int x2) {
        this.minorTickCount = x2;
        __set(5);
        return this;
    }

    public B orientation(Orientation x2) {
        this.orientation = x2;
        __set(6);
        return this;
    }

    public B showTickLabels(boolean x2) {
        this.showTickLabels = x2;
        __set(7);
        return this;
    }

    public B showTickMarks(boolean x2) {
        this.showTickMarks = x2;
        __set(8);
        return this;
    }

    public B snapToTicks(boolean x2) {
        this.snapToTicks = x2;
        __set(9);
        return this;
    }

    public B value(double x2) {
        this.value = x2;
        __set(10);
        return this;
    }

    public B valueChanging(boolean x2) {
        this.valueChanging = x2;
        __set(11);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public Slider build2() {
        Slider x2 = new Slider();
        applyTo(x2);
        return x2;
    }
}
