package javafx.scene.shape;

import javafx.scene.shape.QuadCurveBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/shape/QuadCurveBuilder.class */
public class QuadCurveBuilder<B extends QuadCurveBuilder<B>> extends ShapeBuilder<B> implements Builder<QuadCurve> {
    private int __set;
    private double controlX;
    private double controlY;
    private double endX;
    private double endY;
    private double startX;
    private double startY;

    protected QuadCurveBuilder() {
    }

    public static QuadCurveBuilder<?> create() {
        return new QuadCurveBuilder<>();
    }

    public void applyTo(QuadCurve x2) {
        super.applyTo((Shape) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setControlX(this.controlX);
        }
        if ((set & 2) != 0) {
            x2.setControlY(this.controlY);
        }
        if ((set & 4) != 0) {
            x2.setEndX(this.endX);
        }
        if ((set & 8) != 0) {
            x2.setEndY(this.endY);
        }
        if ((set & 16) != 0) {
            x2.setStartX(this.startX);
        }
        if ((set & 32) != 0) {
            x2.setStartY(this.startY);
        }
    }

    public B controlX(double x2) {
        this.controlX = x2;
        this.__set |= 1;
        return this;
    }

    public B controlY(double x2) {
        this.controlY = x2;
        this.__set |= 2;
        return this;
    }

    public B endX(double x2) {
        this.endX = x2;
        this.__set |= 4;
        return this;
    }

    public B endY(double x2) {
        this.endY = x2;
        this.__set |= 8;
        return this;
    }

    public B startX(double x2) {
        this.startX = x2;
        this.__set |= 16;
        return this;
    }

    public B startY(double x2) {
        this.startY = x2;
        this.__set |= 32;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public QuadCurve build2() {
        QuadCurve x2 = new QuadCurve();
        applyTo(x2);
        return x2;
    }
}
