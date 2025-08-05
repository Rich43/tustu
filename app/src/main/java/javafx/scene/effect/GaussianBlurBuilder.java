package javafx.scene.effect;

import javafx.scene.effect.GaussianBlurBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/effect/GaussianBlurBuilder.class */
public class GaussianBlurBuilder<B extends GaussianBlurBuilder<B>> implements Builder<GaussianBlur> {
    private int __set;
    private Effect input;
    private double radius;

    protected GaussianBlurBuilder() {
    }

    public static GaussianBlurBuilder<?> create() {
        return new GaussianBlurBuilder<>();
    }

    public void applyTo(GaussianBlur x2) {
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setInput(this.input);
        }
        if ((set & 2) != 0) {
            x2.setRadius(this.radius);
        }
    }

    public B input(Effect x2) {
        this.input = x2;
        this.__set |= 1;
        return this;
    }

    public B radius(double x2) {
        this.radius = x2;
        this.__set |= 2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public GaussianBlur build() {
        GaussianBlur x2 = new GaussianBlur();
        applyTo(x2);
        return x2;
    }
}
