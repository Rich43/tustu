package javafx.scene.effect;

import javafx.scene.effect.ShadowBuilder;
import javafx.scene.paint.Color;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/effect/ShadowBuilder.class */
public class ShadowBuilder<B extends ShadowBuilder<B>> implements Builder<Shadow> {
    private int __set;
    private BlurType blurType;
    private Color color;
    private double height;
    private Effect input;
    private double radius;
    private double width;

    protected ShadowBuilder() {
    }

    public static ShadowBuilder<?> create() {
        return new ShadowBuilder<>();
    }

    public void applyTo(Shadow x2) {
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setBlurType(this.blurType);
        }
        if ((set & 2) != 0) {
            x2.setColor(this.color);
        }
        if ((set & 4) != 0) {
            x2.setHeight(this.height);
        }
        if ((set & 8) != 0) {
            x2.setInput(this.input);
        }
        if ((set & 16) != 0) {
            x2.setRadius(this.radius);
        }
        if ((set & 32) != 0) {
            x2.setWidth(this.width);
        }
    }

    public B blurType(BlurType x2) {
        this.blurType = x2;
        this.__set |= 1;
        return this;
    }

    public B color(Color x2) {
        this.color = x2;
        this.__set |= 2;
        return this;
    }

    public B height(double x2) {
        this.height = x2;
        this.__set |= 4;
        return this;
    }

    public B input(Effect x2) {
        this.input = x2;
        this.__set |= 8;
        return this;
    }

    public B radius(double x2) {
        this.radius = x2;
        this.__set |= 16;
        return this;
    }

    public B width(double x2) {
        this.width = x2;
        this.__set |= 32;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public Shadow build2() {
        Shadow x2 = new Shadow();
        applyTo(x2);
        return x2;
    }
}
