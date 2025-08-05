package javafx.scene.shape;

import javafx.scene.shape.MoveToBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/shape/MoveToBuilder.class */
public class MoveToBuilder<B extends MoveToBuilder<B>> extends PathElementBuilder<B> implements Builder<MoveTo> {
    private int __set;

    /* renamed from: x, reason: collision with root package name */
    private double f12731x;

    /* renamed from: y, reason: collision with root package name */
    private double f12732y;

    protected MoveToBuilder() {
    }

    public static MoveToBuilder<?> create() {
        return new MoveToBuilder<>();
    }

    public void applyTo(MoveTo x2) {
        super.applyTo((PathElement) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setX(this.f12731x);
        }
        if ((set & 2) != 0) {
            x2.setY(this.f12732y);
        }
    }

    public B x(double x2) {
        this.f12731x = x2;
        this.__set |= 1;
        return this;
    }

    public B y(double x2) {
        this.f12732y = x2;
        this.__set |= 2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public MoveTo build() {
        MoveTo x2 = new MoveTo();
        applyTo(x2);
        return x2;
    }
}
