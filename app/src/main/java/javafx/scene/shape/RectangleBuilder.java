package javafx.scene.shape;

import javafx.scene.shape.RectangleBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/shape/RectangleBuilder.class */
public class RectangleBuilder<B extends RectangleBuilder<B>> extends ShapeBuilder<B> implements Builder<Rectangle> {
    private int __set;
    private double arcHeight;
    private double arcWidth;
    private double height;
    private double width;

    /* renamed from: x, reason: collision with root package name */
    private double f12739x;

    /* renamed from: y, reason: collision with root package name */
    private double f12740y;

    protected RectangleBuilder() {
    }

    public static RectangleBuilder<?> create() {
        return new RectangleBuilder<>();
    }

    public void applyTo(Rectangle x2) {
        super.applyTo((Shape) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setArcHeight(this.arcHeight);
        }
        if ((set & 2) != 0) {
            x2.setArcWidth(this.arcWidth);
        }
        if ((set & 4) != 0) {
            x2.setHeight(this.height);
        }
        if ((set & 8) != 0) {
            x2.setWidth(this.width);
        }
        if ((set & 16) != 0) {
            x2.setX(this.f12739x);
        }
        if ((set & 32) != 0) {
            x2.setY(this.f12740y);
        }
    }

    public B arcHeight(double x2) {
        this.arcHeight = x2;
        this.__set |= 1;
        return this;
    }

    public B arcWidth(double x2) {
        this.arcWidth = x2;
        this.__set |= 2;
        return this;
    }

    public B height(double x2) {
        this.height = x2;
        this.__set |= 4;
        return this;
    }

    public B width(double x2) {
        this.width = x2;
        this.__set |= 8;
        return this;
    }

    public B x(double x2) {
        this.f12739x = x2;
        this.__set |= 16;
        return this;
    }

    public B y(double x2) {
        this.f12740y = x2;
        this.__set |= 32;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public Rectangle build2() {
        Rectangle x2 = new Rectangle();
        applyTo(x2);
        return x2;
    }
}
