package javafx.scene.effect;

import javafx.scene.effect.FloatMapBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/effect/FloatMapBuilder.class */
public class FloatMapBuilder<B extends FloatMapBuilder<B>> implements Builder<FloatMap> {
    private int __set;
    private int height;
    private int width;

    protected FloatMapBuilder() {
    }

    public static FloatMapBuilder<?> create() {
        return new FloatMapBuilder<>();
    }

    public void applyTo(FloatMap x2) {
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setHeight(this.height);
        }
        if ((set & 2) != 0) {
            x2.setWidth(this.width);
        }
    }

    public B height(int x2) {
        this.height = x2;
        this.__set |= 1;
        return this;
    }

    public B width(int x2) {
        this.width = x2;
        this.__set |= 2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public FloatMap build() {
        FloatMap x2 = new FloatMap();
        applyTo(x2);
        return x2;
    }
}
