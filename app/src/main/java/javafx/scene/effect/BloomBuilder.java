package javafx.scene.effect;

import javafx.scene.effect.BloomBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/effect/BloomBuilder.class */
public class BloomBuilder<B extends BloomBuilder<B>> implements Builder<Bloom> {
    private int __set;
    private Effect input;
    private double threshold;

    protected BloomBuilder() {
    }

    public static BloomBuilder<?> create() {
        return new BloomBuilder<>();
    }

    public void applyTo(Bloom x2) {
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setInput(this.input);
        }
        if ((set & 2) != 0) {
            x2.setThreshold(this.threshold);
        }
    }

    public B input(Effect x2) {
        this.input = x2;
        this.__set |= 1;
        return this;
    }

    public B threshold(double x2) {
        this.threshold = x2;
        this.__set |= 2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public Bloom build() {
        Bloom x2 = new Bloom();
        applyTo(x2);
        return x2;
    }
}
