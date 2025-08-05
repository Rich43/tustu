package javafx.scene.shape;

import javafx.scene.shape.CircleBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/shape/CircleBuilder.class */
public class CircleBuilder<B extends CircleBuilder<B>> extends ShapeBuilder<B> implements Builder<Circle> {
    private int __set;
    private double centerX;
    private double centerY;
    private double radius;

    protected CircleBuilder() {
    }

    public static CircleBuilder<?> create() {
        return new CircleBuilder<>();
    }

    public void applyTo(Circle x2) {
        super.applyTo((Shape) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setCenterX(this.centerX);
        }
        if ((set & 2) != 0) {
            x2.setCenterY(this.centerY);
        }
        if ((set & 4) != 0) {
            x2.setRadius(this.radius);
        }
    }

    public B centerX(double x2) {
        this.centerX = x2;
        this.__set |= 1;
        return this;
    }

    public B centerY(double x2) {
        this.centerY = x2;
        this.__set |= 2;
        return this;
    }

    public B radius(double x2) {
        this.radius = x2;
        this.__set |= 4;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public Circle build2() {
        Circle x2 = new Circle();
        applyTo(x2);
        return x2;
    }
}
