package javafx.scene.shape;

import javafx.scene.shape.ArcToBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/shape/ArcToBuilder.class */
public class ArcToBuilder<B extends ArcToBuilder<B>> extends PathElementBuilder<B> implements Builder<ArcTo> {
    private int __set;
    private boolean largeArcFlag;
    private double radiusX;
    private double radiusY;
    private boolean sweepFlag;

    /* renamed from: x, reason: collision with root package name */
    private double f12717x;
    private double XAxisRotation;

    /* renamed from: y, reason: collision with root package name */
    private double f12718y;

    protected ArcToBuilder() {
    }

    public static ArcToBuilder<?> create() {
        return new ArcToBuilder<>();
    }

    public void applyTo(ArcTo x2) {
        super.applyTo((PathElement) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setLargeArcFlag(this.largeArcFlag);
        }
        if ((set & 2) != 0) {
            x2.setRadiusX(this.radiusX);
        }
        if ((set & 4) != 0) {
            x2.setRadiusY(this.radiusY);
        }
        if ((set & 8) != 0) {
            x2.setSweepFlag(this.sweepFlag);
        }
        if ((set & 16) != 0) {
            x2.setX(this.f12717x);
        }
        if ((set & 32) != 0) {
            x2.setXAxisRotation(this.XAxisRotation);
        }
        if ((set & 64) != 0) {
            x2.setY(this.f12718y);
        }
    }

    public B largeArcFlag(boolean x2) {
        this.largeArcFlag = x2;
        this.__set |= 1;
        return this;
    }

    public B radiusX(double x2) {
        this.radiusX = x2;
        this.__set |= 2;
        return this;
    }

    public B radiusY(double x2) {
        this.radiusY = x2;
        this.__set |= 4;
        return this;
    }

    public B sweepFlag(boolean x2) {
        this.sweepFlag = x2;
        this.__set |= 8;
        return this;
    }

    public B x(double x2) {
        this.f12717x = x2;
        this.__set |= 16;
        return this;
    }

    public B XAxisRotation(double x2) {
        this.XAxisRotation = x2;
        this.__set |= 32;
        return this;
    }

    public B y(double x2) {
        this.f12718y = x2;
        this.__set |= 64;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public ArcTo build() {
        ArcTo x2 = new ArcTo();
        applyTo(x2);
        return x2;
    }
}
