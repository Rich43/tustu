package javafx.scene.effect;

import javafx.scene.effect.BoxBlurBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/effect/BoxBlurBuilder.class */
public class BoxBlurBuilder<B extends BoxBlurBuilder<B>> implements Builder<BoxBlur> {
    private int __set;
    private double height;
    private Effect input;
    private int iterations;
    private double width;

    protected BoxBlurBuilder() {
    }

    public static BoxBlurBuilder<?> create() {
        return new BoxBlurBuilder<>();
    }

    public void applyTo(BoxBlur x2) {
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setHeight(this.height);
        }
        if ((set & 2) != 0) {
            x2.setInput(this.input);
        }
        if ((set & 4) != 0) {
            x2.setIterations(this.iterations);
        }
        if ((set & 8) != 0) {
            x2.setWidth(this.width);
        }
    }

    public B height(double x2) {
        this.height = x2;
        this.__set |= 1;
        return this;
    }

    public B input(Effect x2) {
        this.input = x2;
        this.__set |= 2;
        return this;
    }

    public B iterations(int x2) {
        this.iterations = x2;
        this.__set |= 4;
        return this;
    }

    public B width(double x2) {
        this.width = x2;
        this.__set |= 8;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public BoxBlur build() {
        BoxBlur x2 = new BoxBlur();
        applyTo(x2);
        return x2;
    }
}
