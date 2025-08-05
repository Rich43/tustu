package javafx.scene.shape;

import javafx.scene.shape.CubicCurveToBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/shape/CubicCurveToBuilder.class */
public class CubicCurveToBuilder<B extends CubicCurveToBuilder<B>> extends PathElementBuilder<B> implements Builder<CubicCurveTo> {
    private int __set;
    private double controlX1;
    private double controlX2;
    private double controlY1;
    private double controlY2;

    /* renamed from: x, reason: collision with root package name */
    private double f12721x;

    /* renamed from: y, reason: collision with root package name */
    private double f12722y;

    protected CubicCurveToBuilder() {
    }

    public static CubicCurveToBuilder<?> create() {
        return new CubicCurveToBuilder<>();
    }

    public void applyTo(CubicCurveTo x2) {
        super.applyTo((PathElement) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setControlX1(this.controlX1);
        }
        if ((set & 2) != 0) {
            x2.setControlX2(this.controlX2);
        }
        if ((set & 4) != 0) {
            x2.setControlY1(this.controlY1);
        }
        if ((set & 8) != 0) {
            x2.setControlY2(this.controlY2);
        }
        if ((set & 16) != 0) {
            x2.setX(this.f12721x);
        }
        if ((set & 32) != 0) {
            x2.setY(this.f12722y);
        }
    }

    public B controlX1(double x2) {
        this.controlX1 = x2;
        this.__set |= 1;
        return this;
    }

    public B controlX2(double x2) {
        this.controlX2 = x2;
        this.__set |= 2;
        return this;
    }

    public B controlY1(double x2) {
        this.controlY1 = x2;
        this.__set |= 4;
        return this;
    }

    public B controlY2(double x2) {
        this.controlY2 = x2;
        this.__set |= 8;
        return this;
    }

    public B x(double x2) {
        this.f12721x = x2;
        this.__set |= 16;
        return this;
    }

    public B y(double x2) {
        this.f12722y = x2;
        this.__set |= 32;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public CubicCurveTo build() {
        CubicCurveTo x2 = new CubicCurveTo();
        applyTo(x2);
        return x2;
    }
}
