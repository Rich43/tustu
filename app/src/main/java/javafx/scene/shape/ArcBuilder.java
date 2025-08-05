package javafx.scene.shape;

import javafx.scene.shape.ArcBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/shape/ArcBuilder.class */
public class ArcBuilder<B extends ArcBuilder<B>> extends ShapeBuilder<B> implements Builder<Arc> {
    private int __set;
    private double centerX;
    private double centerY;
    private double length;
    private double radiusX;
    private double radiusY;
    private double startAngle;
    private ArcType type;

    protected ArcBuilder() {
    }

    public static ArcBuilder<?> create() {
        return new ArcBuilder<>();
    }

    public void applyTo(Arc x2) {
        super.applyTo((Shape) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setCenterX(this.centerX);
        }
        if ((set & 2) != 0) {
            x2.setCenterY(this.centerY);
        }
        if ((set & 4) != 0) {
            x2.setLength(this.length);
        }
        if ((set & 8) != 0) {
            x2.setRadiusX(this.radiusX);
        }
        if ((set & 16) != 0) {
            x2.setRadiusY(this.radiusY);
        }
        if ((set & 32) != 0) {
            x2.setStartAngle(this.startAngle);
        }
        if ((set & 64) != 0) {
            x2.setType(this.type);
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

    public B length(double x2) {
        this.length = x2;
        this.__set |= 4;
        return this;
    }

    public B radiusX(double x2) {
        this.radiusX = x2;
        this.__set |= 8;
        return this;
    }

    public B radiusY(double x2) {
        this.radiusY = x2;
        this.__set |= 16;
        return this;
    }

    public B startAngle(double x2) {
        this.startAngle = x2;
        this.__set |= 32;
        return this;
    }

    public B type(ArcType x2) {
        this.type = x2;
        this.__set |= 64;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public Arc build2() {
        Arc x2 = new Arc();
        applyTo(x2);
        return x2;
    }
}
