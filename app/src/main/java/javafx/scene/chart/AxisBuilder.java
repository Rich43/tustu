package javafx.scene.chart;

import java.util.Arrays;
import java.util.Collection;
import javafx.geometry.Side;
import javafx.scene.chart.Axis;
import javafx.scene.chart.AxisBuilder;
import javafx.scene.layout.Region;
import javafx.scene.layout.RegionBuilder;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/chart/AxisBuilder.class */
public abstract class AxisBuilder<T, B extends AxisBuilder<T, B>> extends RegionBuilder<B> {
    private int __set;
    private boolean animated;
    private boolean autoRanging;
    private String label;
    private Side side;
    private Paint tickLabelFill;
    private Font tickLabelFont;
    private double tickLabelGap;
    private double tickLabelRotation;
    private boolean tickLabelsVisible;
    private double tickLength;
    private Collection<? extends Axis.TickMark<T>> tickMarks;
    private boolean tickMarkVisible;

    protected AxisBuilder() {
    }

    private void __set(int i2) {
        this.__set |= 1 << i2;
    }

    public void applyTo(Axis<T> x2) {
        super.applyTo((Region) x2);
        int set = this.__set;
        while (set != 0) {
            int i2 = Integer.numberOfTrailingZeros(set);
            set &= (1 << i2) ^ (-1);
            switch (i2) {
                case 0:
                    x2.setAnimated(this.animated);
                    break;
                case 1:
                    x2.setAutoRanging(this.autoRanging);
                    break;
                case 2:
                    x2.setLabel(this.label);
                    break;
                case 3:
                    x2.setSide(this.side);
                    break;
                case 4:
                    x2.setTickLabelFill(this.tickLabelFill);
                    break;
                case 5:
                    x2.setTickLabelFont(this.tickLabelFont);
                    break;
                case 6:
                    x2.setTickLabelGap(this.tickLabelGap);
                    break;
                case 7:
                    x2.setTickLabelRotation(this.tickLabelRotation);
                    break;
                case 8:
                    x2.setTickLabelsVisible(this.tickLabelsVisible);
                    break;
                case 9:
                    x2.setTickLength(this.tickLength);
                    break;
                case 10:
                    x2.getTickMarks().addAll(this.tickMarks);
                    break;
                case 11:
                    x2.setTickMarkVisible(this.tickMarkVisible);
                    break;
            }
        }
    }

    public B animated(boolean x2) {
        this.animated = x2;
        __set(0);
        return this;
    }

    public B autoRanging(boolean x2) {
        this.autoRanging = x2;
        __set(1);
        return this;
    }

    public B label(String x2) {
        this.label = x2;
        __set(2);
        return this;
    }

    public B side(Side x2) {
        this.side = x2;
        __set(3);
        return this;
    }

    public B tickLabelFill(Paint x2) {
        this.tickLabelFill = x2;
        __set(4);
        return this;
    }

    public B tickLabelFont(Font x2) {
        this.tickLabelFont = x2;
        __set(5);
        return this;
    }

    public B tickLabelGap(double x2) {
        this.tickLabelGap = x2;
        __set(6);
        return this;
    }

    public B tickLabelRotation(double x2) {
        this.tickLabelRotation = x2;
        __set(7);
        return this;
    }

    public B tickLabelsVisible(boolean x2) {
        this.tickLabelsVisible = x2;
        __set(8);
        return this;
    }

    public B tickLength(double x2) {
        this.tickLength = x2;
        __set(9);
        return this;
    }

    public B tickMarks(Collection<? extends Axis.TickMark<T>> x2) {
        this.tickMarks = x2;
        __set(10);
        return this;
    }

    public B tickMarks(Axis.TickMark<T>... tickMarkArr) {
        return (B) tickMarks(Arrays.asList(tickMarkArr));
    }

    public B tickMarkVisible(boolean x2) {
        this.tickMarkVisible = x2;
        __set(11);
        return this;
    }
}
