package javafx.scene.effect;

import javafx.scene.effect.ReflectionBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/effect/ReflectionBuilder.class */
public class ReflectionBuilder<B extends ReflectionBuilder<B>> implements Builder<Reflection> {
    private int __set;
    private double bottomOpacity;
    private double fraction;
    private Effect input;
    private double topOffset;
    private double topOpacity;

    protected ReflectionBuilder() {
    }

    public static ReflectionBuilder<?> create() {
        return new ReflectionBuilder<>();
    }

    public void applyTo(Reflection x2) {
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setBottomOpacity(this.bottomOpacity);
        }
        if ((set & 2) != 0) {
            x2.setFraction(this.fraction);
        }
        if ((set & 4) != 0) {
            x2.setInput(this.input);
        }
        if ((set & 8) != 0) {
            x2.setTopOffset(this.topOffset);
        }
        if ((set & 16) != 0) {
            x2.setTopOpacity(this.topOpacity);
        }
    }

    public B bottomOpacity(double x2) {
        this.bottomOpacity = x2;
        this.__set |= 1;
        return this;
    }

    public B fraction(double x2) {
        this.fraction = x2;
        this.__set |= 2;
        return this;
    }

    public B input(Effect x2) {
        this.input = x2;
        this.__set |= 4;
        return this;
    }

    public B topOffset(double x2) {
        this.topOffset = x2;
        this.__set |= 8;
        return this;
    }

    public B topOpacity(double x2) {
        this.topOpacity = x2;
        this.__set |= 16;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public Reflection build() {
        Reflection x2 = new Reflection();
        applyTo(x2);
        return x2;
    }
}
