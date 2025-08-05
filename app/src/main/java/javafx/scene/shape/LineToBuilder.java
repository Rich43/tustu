package javafx.scene.shape;

import javafx.scene.shape.LineToBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/shape/LineToBuilder.class */
public class LineToBuilder<B extends LineToBuilder<B>> extends PathElementBuilder<B> implements Builder<LineTo> {
    private int __set;

    /* renamed from: x, reason: collision with root package name */
    private double f12727x;

    /* renamed from: y, reason: collision with root package name */
    private double f12728y;

    protected LineToBuilder() {
    }

    public static LineToBuilder<?> create() {
        return new LineToBuilder<>();
    }

    public void applyTo(LineTo x2) {
        super.applyTo((PathElement) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setX(this.f12727x);
        }
        if ((set & 2) != 0) {
            x2.setY(this.f12728y);
        }
    }

    public B x(double x2) {
        this.f12727x = x2;
        this.__set |= 1;
        return this;
    }

    public B y(double x2) {
        this.f12728y = x2;
        this.__set |= 2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public LineTo build() {
        LineTo x2 = new LineTo();
        applyTo(x2);
        return x2;
    }
}
