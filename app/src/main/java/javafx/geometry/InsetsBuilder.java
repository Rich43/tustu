package javafx.geometry;

import javafx.geometry.InsetsBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/geometry/InsetsBuilder.class */
public class InsetsBuilder<B extends InsetsBuilder<B>> implements Builder<Insets> {
    private double bottom;
    private double left;
    private double right;
    private double top;

    protected InsetsBuilder() {
    }

    public static InsetsBuilder<?> create() {
        return new InsetsBuilder<>();
    }

    public B bottom(double x2) {
        this.bottom = x2;
        return this;
    }

    public B left(double x2) {
        this.left = x2;
        return this;
    }

    public B right(double x2) {
        this.right = x2;
        return this;
    }

    public B top(double x2) {
        this.top = x2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public Insets build() {
        Insets x2 = new Insets(this.top, this.right, this.bottom, this.left);
        return x2;
    }
}
