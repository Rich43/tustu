package javafx.scene.effect;

import javafx.scene.effect.GlowBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/effect/GlowBuilder.class */
public class GlowBuilder<B extends GlowBuilder<B>> implements Builder<Glow> {
    private int __set;
    private Effect input;
    private double level;

    protected GlowBuilder() {
    }

    public static GlowBuilder<?> create() {
        return new GlowBuilder<>();
    }

    public void applyTo(Glow x2) {
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setInput(this.input);
        }
        if ((set & 2) != 0) {
            x2.setLevel(this.level);
        }
    }

    public B input(Effect x2) {
        this.input = x2;
        this.__set |= 1;
        return this;
    }

    public B level(double x2) {
        this.level = x2;
        this.__set |= 2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public Glow build() {
        Glow x2 = new Glow();
        applyTo(x2);
        return x2;
    }
}
