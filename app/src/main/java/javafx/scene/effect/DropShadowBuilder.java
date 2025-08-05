package javafx.scene.effect;

import javafx.scene.effect.DropShadowBuilder;
import javafx.scene.paint.Color;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/effect/DropShadowBuilder.class */
public class DropShadowBuilder<B extends DropShadowBuilder<B>> implements Builder<DropShadow> {
    private int __set;
    private BlurType blurType;
    private Color color;
    private double height;
    private Effect input;
    private double offsetX;
    private double offsetY;
    private double radius;
    private double spread;
    private double width;

    protected DropShadowBuilder() {
    }

    public static DropShadowBuilder<?> create() {
        return new DropShadowBuilder<>();
    }

    private void __set(int i2) {
        this.__set |= 1 << i2;
    }

    public void applyTo(DropShadow x2) {
        int set = this.__set;
        while (set != 0) {
            int i2 = Integer.numberOfTrailingZeros(set);
            set &= (1 << i2) ^ (-1);
            switch (i2) {
                case 0:
                    x2.setBlurType(this.blurType);
                    break;
                case 1:
                    x2.setColor(this.color);
                    break;
                case 2:
                    x2.setHeight(this.height);
                    break;
                case 3:
                    x2.setInput(this.input);
                    break;
                case 4:
                    x2.setOffsetX(this.offsetX);
                    break;
                case 5:
                    x2.setOffsetY(this.offsetY);
                    break;
                case 6:
                    x2.setRadius(this.radius);
                    break;
                case 7:
                    x2.setSpread(this.spread);
                    break;
                case 8:
                    x2.setWidth(this.width);
                    break;
            }
        }
    }

    public B blurType(BlurType x2) {
        this.blurType = x2;
        __set(0);
        return this;
    }

    public B color(Color x2) {
        this.color = x2;
        __set(1);
        return this;
    }

    public B height(double x2) {
        this.height = x2;
        __set(2);
        return this;
    }

    public B input(Effect x2) {
        this.input = x2;
        __set(3);
        return this;
    }

    public B offsetX(double x2) {
        this.offsetX = x2;
        __set(4);
        return this;
    }

    public B offsetY(double x2) {
        this.offsetY = x2;
        __set(5);
        return this;
    }

    public B radius(double x2) {
        this.radius = x2;
        __set(6);
        return this;
    }

    public B spread(double x2) {
        this.spread = x2;
        __set(7);
        return this;
    }

    public B width(double x2) {
        this.width = x2;
        __set(8);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public DropShadow build2() {
        DropShadow x2 = new DropShadow();
        applyTo(x2);
        return x2;
    }
}
