package javafx.scene.shape;

import javafx.scene.shape.HLineToBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/shape/HLineToBuilder.class */
public class HLineToBuilder<B extends HLineToBuilder<B>> extends PathElementBuilder<B> implements Builder<HLineTo> {
    private boolean __set;

    /* renamed from: x, reason: collision with root package name */
    private double f12724x;

    protected HLineToBuilder() {
    }

    public static HLineToBuilder<?> create() {
        return new HLineToBuilder<>();
    }

    public void applyTo(HLineTo x2) {
        super.applyTo((PathElement) x2);
        if (this.__set) {
            x2.setX(this.f12724x);
        }
    }

    public B x(double x2) {
        this.f12724x = x2;
        this.__set = true;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public HLineTo build() {
        HLineTo x2 = new HLineTo();
        applyTo(x2);
        return x2;
    }
}
