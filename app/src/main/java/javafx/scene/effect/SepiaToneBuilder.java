package javafx.scene.effect;

import javafx.scene.effect.SepiaToneBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/effect/SepiaToneBuilder.class */
public class SepiaToneBuilder<B extends SepiaToneBuilder<B>> implements Builder<SepiaTone> {
    private int __set;
    private Effect input;
    private double level;

    protected SepiaToneBuilder() {
    }

    public static SepiaToneBuilder<?> create() {
        return new SepiaToneBuilder<>();
    }

    public void applyTo(SepiaTone x2) {
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
    public SepiaTone build() {
        SepiaTone x2 = new SepiaTone();
        applyTo(x2);
        return x2;
    }
}
