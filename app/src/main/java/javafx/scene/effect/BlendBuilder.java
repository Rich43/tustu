package javafx.scene.effect;

import javafx.scene.effect.BlendBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/effect/BlendBuilder.class */
public class BlendBuilder<B extends BlendBuilder<B>> implements Builder<Blend> {
    private int __set;
    private Effect bottomInput;
    private BlendMode mode;
    private double opacity;
    private Effect topInput;

    protected BlendBuilder() {
    }

    public static BlendBuilder<?> create() {
        return new BlendBuilder<>();
    }

    public void applyTo(Blend x2) {
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setBottomInput(this.bottomInput);
        }
        if ((set & 2) != 0) {
            x2.setMode(this.mode);
        }
        if ((set & 4) != 0) {
            x2.setOpacity(this.opacity);
        }
        if ((set & 8) != 0) {
            x2.setTopInput(this.topInput);
        }
    }

    public B bottomInput(Effect x2) {
        this.bottomInput = x2;
        this.__set |= 1;
        return this;
    }

    public B mode(BlendMode x2) {
        this.mode = x2;
        this.__set |= 2;
        return this;
    }

    public B opacity(double x2) {
        this.opacity = x2;
        this.__set |= 4;
        return this;
    }

    public B topInput(Effect x2) {
        this.topInput = x2;
        this.__set |= 8;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public Blend build2() {
        Blend x2 = new Blend();
        applyTo(x2);
        return x2;
    }
}
