package javafx.scene.control;

import javafx.geometry.Orientation;
import javafx.scene.control.ScrollBarBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/ScrollBarBuilder.class */
public class ScrollBarBuilder<B extends ScrollBarBuilder<B>> extends ControlBuilder<B> implements Builder<ScrollBar> {
    private int __set;
    private double blockIncrement;
    private double max;
    private double min;
    private Orientation orientation;
    private double unitIncrement;
    private double value;
    private double visibleAmount;

    protected ScrollBarBuilder() {
    }

    public static ScrollBarBuilder<?> create() {
        return new ScrollBarBuilder<>();
    }

    public void applyTo(ScrollBar x2) {
        super.applyTo((Control) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setBlockIncrement(this.blockIncrement);
        }
        if ((set & 2) != 0) {
            x2.setMax(this.max);
        }
        if ((set & 4) != 0) {
            x2.setMin(this.min);
        }
        if ((set & 8) != 0) {
            x2.setOrientation(this.orientation);
        }
        if ((set & 16) != 0) {
            x2.setUnitIncrement(this.unitIncrement);
        }
        if ((set & 32) != 0) {
            x2.setValue(this.value);
        }
        if ((set & 64) != 0) {
            x2.setVisibleAmount(this.visibleAmount);
        }
    }

    public B blockIncrement(double x2) {
        this.blockIncrement = x2;
        this.__set |= 1;
        return this;
    }

    public B max(double x2) {
        this.max = x2;
        this.__set |= 2;
        return this;
    }

    public B min(double x2) {
        this.min = x2;
        this.__set |= 4;
        return this;
    }

    public B orientation(Orientation x2) {
        this.orientation = x2;
        this.__set |= 8;
        return this;
    }

    public B unitIncrement(double x2) {
        this.unitIncrement = x2;
        this.__set |= 16;
        return this;
    }

    public B value(double x2) {
        this.value = x2;
        this.__set |= 32;
        return this;
    }

    public B visibleAmount(double x2) {
        this.visibleAmount = x2;
        this.__set |= 64;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public ScrollBar build2() {
        ScrollBar x2 = new ScrollBar();
        applyTo(x2);
        return x2;
    }
}
