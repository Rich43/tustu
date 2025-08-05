package javafx.scene.effect;

import javafx.scene.effect.ColorInputBuilder;
import javafx.scene.paint.Paint;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/effect/ColorInputBuilder.class */
public class ColorInputBuilder<B extends ColorInputBuilder<B>> implements Builder<ColorInput> {
    private int __set;
    private double height;
    private Paint paint;
    private double width;

    /* renamed from: x, reason: collision with root package name */
    private double f12651x;

    /* renamed from: y, reason: collision with root package name */
    private double f12652y;

    protected ColorInputBuilder() {
    }

    public static ColorInputBuilder<?> create() {
        return new ColorInputBuilder<>();
    }

    public void applyTo(ColorInput x2) {
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setHeight(this.height);
        }
        if ((set & 2) != 0) {
            x2.setPaint(this.paint);
        }
        if ((set & 4) != 0) {
            x2.setWidth(this.width);
        }
        if ((set & 8) != 0) {
            x2.setX(this.f12651x);
        }
        if ((set & 16) != 0) {
            x2.setY(this.f12652y);
        }
    }

    public B height(double x2) {
        this.height = x2;
        this.__set |= 1;
        return this;
    }

    public B paint(Paint x2) {
        this.paint = x2;
        this.__set |= 2;
        return this;
    }

    public B width(double x2) {
        this.width = x2;
        this.__set |= 4;
        return this;
    }

    public B x(double x2) {
        this.f12651x = x2;
        this.__set |= 8;
        return this;
    }

    public B y(double x2) {
        this.f12652y = x2;
        this.__set |= 16;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public ColorInput build() {
        ColorInput x2 = new ColorInput();
        applyTo(x2);
        return x2;
    }
}
