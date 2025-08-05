package javafx.scene.effect;

import javafx.scene.effect.PerspectiveTransformBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/effect/PerspectiveTransformBuilder.class */
public class PerspectiveTransformBuilder<B extends PerspectiveTransformBuilder<B>> implements Builder<PerspectiveTransform> {
    private int __set;
    private Effect input;
    private double llx;
    private double lly;
    private double lrx;
    private double lry;
    private double ulx;
    private double uly;
    private double urx;
    private double ury;

    protected PerspectiveTransformBuilder() {
    }

    public static PerspectiveTransformBuilder<?> create() {
        return new PerspectiveTransformBuilder<>();
    }

    private void __set(int i2) {
        this.__set |= 1 << i2;
    }

    public void applyTo(PerspectiveTransform x2) {
        int set = this.__set;
        while (set != 0) {
            int i2 = Integer.numberOfTrailingZeros(set);
            set &= (1 << i2) ^ (-1);
            switch (i2) {
                case 0:
                    x2.setInput(this.input);
                    break;
                case 1:
                    x2.setLlx(this.llx);
                    break;
                case 2:
                    x2.setLly(this.lly);
                    break;
                case 3:
                    x2.setLrx(this.lrx);
                    break;
                case 4:
                    x2.setLry(this.lry);
                    break;
                case 5:
                    x2.setUlx(this.ulx);
                    break;
                case 6:
                    x2.setUly(this.uly);
                    break;
                case 7:
                    x2.setUrx(this.urx);
                    break;
                case 8:
                    x2.setUry(this.ury);
                    break;
            }
        }
    }

    public B input(Effect x2) {
        this.input = x2;
        __set(0);
        return this;
    }

    public B llx(double x2) {
        this.llx = x2;
        __set(1);
        return this;
    }

    public B lly(double x2) {
        this.lly = x2;
        __set(2);
        return this;
    }

    public B lrx(double x2) {
        this.lrx = x2;
        __set(3);
        return this;
    }

    public B lry(double x2) {
        this.lry = x2;
        __set(4);
        return this;
    }

    public B ulx(double x2) {
        this.ulx = x2;
        __set(5);
        return this;
    }

    public B uly(double x2) {
        this.uly = x2;
        __set(6);
        return this;
    }

    public B urx(double x2) {
        this.urx = x2;
        __set(7);
        return this;
    }

    public B ury(double x2) {
        this.ury = x2;
        __set(8);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public PerspectiveTransform build2() {
        PerspectiveTransform x2 = new PerspectiveTransform();
        applyTo(x2);
        return x2;
    }
}
