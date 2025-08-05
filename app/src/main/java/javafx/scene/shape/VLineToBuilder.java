package javafx.scene.shape;

import javafx.scene.shape.VLineToBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/shape/VLineToBuilder.class */
public class VLineToBuilder<B extends VLineToBuilder<B>> extends PathElementBuilder<B> implements Builder<VLineTo> {
    private boolean __set;

    /* renamed from: y, reason: collision with root package name */
    private double f12742y;

    protected VLineToBuilder() {
    }

    public static VLineToBuilder<?> create() {
        return new VLineToBuilder<>();
    }

    public void applyTo(VLineTo x2) {
        super.applyTo((PathElement) x2);
        if (this.__set) {
            x2.setY(this.f12742y);
        }
    }

    public B y(double x2) {
        this.f12742y = x2;
        this.__set = true;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public VLineTo build() {
        VLineTo x2 = new VLineTo();
        applyTo(x2);
        return x2;
    }
}
