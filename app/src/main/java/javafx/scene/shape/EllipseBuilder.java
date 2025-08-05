package javafx.scene.shape;

import javafx.scene.shape.EllipseBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/shape/EllipseBuilder.class */
public class EllipseBuilder<B extends EllipseBuilder<B>> extends ShapeBuilder<B> implements Builder<Ellipse> {
    private int __set;
    private double centerX;
    private double centerY;
    private double radiusX;
    private double radiusY;

    protected EllipseBuilder() {
    }

    public static EllipseBuilder<?> create() {
        return new EllipseBuilder<>();
    }

    public void applyTo(Ellipse x2) {
        super.applyTo((Shape) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setCenterX(this.centerX);
        }
        if ((set & 2) != 0) {
            x2.setCenterY(this.centerY);
        }
        if ((set & 4) != 0) {
            x2.setRadiusX(this.radiusX);
        }
        if ((set & 8) != 0) {
            x2.setRadiusY(this.radiusY);
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

    public B radiusX(double x2) {
        this.radiusX = x2;
        this.__set |= 4;
        return this;
    }

    public B radiusY(double x2) {
        this.radiusY = x2;
        this.__set |= 8;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public Ellipse build2() {
        Ellipse x2 = new Ellipse();
        applyTo(x2);
        return x2;
    }
}
