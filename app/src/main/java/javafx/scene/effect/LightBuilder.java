package javafx.scene.effect;

import javafx.scene.effect.LightBuilder;
import javafx.scene.paint.Color;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/effect/LightBuilder.class */
public abstract class LightBuilder<B extends LightBuilder<B>> {
    private boolean __set;
    private Color color;

    protected LightBuilder() {
    }

    public void applyTo(Light x2) {
        if (this.__set) {
            x2.setColor(this.color);
        }
    }

    public B color(Color x2) {
        this.color = x2;
        this.__set = true;
        return this;
    }
}
