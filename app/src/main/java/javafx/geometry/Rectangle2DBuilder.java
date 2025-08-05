package javafx.geometry;

import javafx.geometry.Rectangle2DBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/geometry/Rectangle2DBuilder.class */
public class Rectangle2DBuilder<B extends Rectangle2DBuilder<B>> implements Builder<Rectangle2D> {
    private double height;
    private double minX;
    private double minY;
    private double width;

    protected Rectangle2DBuilder() {
    }

    public static Rectangle2DBuilder<?> create() {
        return new Rectangle2DBuilder<>();
    }

    public B height(double x2) {
        this.height = x2;
        return this;
    }

    public B minX(double x2) {
        this.minX = x2;
        return this;
    }

    public B minY(double x2) {
        this.minY = x2;
        return this;
    }

    public B width(double x2) {
        this.width = x2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public Rectangle2D build() {
        Rectangle2D x2 = new Rectangle2D(this.minX, this.minY, this.width, this.height);
        return x2;
    }
}
