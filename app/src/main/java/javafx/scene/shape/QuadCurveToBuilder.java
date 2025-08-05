package javafx.scene.shape;

import javafx.scene.shape.QuadCurveToBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/shape/QuadCurveToBuilder.class */
public class QuadCurveToBuilder<B extends QuadCurveToBuilder<B>> extends PathElementBuilder<B> implements Builder<QuadCurveTo> {
    private int __set;
    private double controlX;
    private double controlY;

    /* renamed from: x, reason: collision with root package name */
    private double f12735x;

    /* renamed from: y, reason: collision with root package name */
    private double f12736y;

    protected QuadCurveToBuilder() {
    }

    public static QuadCurveToBuilder<?> create() {
        return new QuadCurveToBuilder<>();
    }

    public void applyTo(QuadCurveTo x2) {
        super.applyTo((PathElement) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setControlX(this.controlX);
        }
        if ((set & 2) != 0) {
            x2.setControlY(this.controlY);
        }
        if ((set & 4) != 0) {
            x2.setX(this.f12735x);
        }
        if ((set & 8) != 0) {
            x2.setY(this.f12736y);
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

    public B x(double x2) {
        this.f12735x = x2;
        this.__set |= 4;
        return this;
    }

    public B y(double x2) {
        this.f12736y = x2;
        this.__set |= 8;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public QuadCurveTo build() {
        QuadCurveTo x2 = new QuadCurveTo();
        applyTo(x2);
        return x2;
    }
}
