package javafx.geometry;

import javafx.geometry.Dimension2DBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/geometry/Dimension2DBuilder.class */
public class Dimension2DBuilder<B extends Dimension2DBuilder<B>> implements Builder<Dimension2D> {
    private double height;
    private double width;

    protected Dimension2DBuilder() {
    }

    public static Dimension2DBuilder<?> create() {
        return new Dimension2DBuilder<>();
    }

    public B height(double x2) {
        this.height = x2;
        return this;
    }

    public B width(double x2) {
        this.width = x2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public Dimension2D build() {
        Dimension2D x2 = new Dimension2D(this.width, this.height);
        return x2;
    }
}
