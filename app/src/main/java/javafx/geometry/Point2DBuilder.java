package javafx.geometry;

import javafx.geometry.Point2DBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/geometry/Point2DBuilder.class */
public class Point2DBuilder<B extends Point2DBuilder<B>> implements Builder<Point2D> {

    /* renamed from: x, reason: collision with root package name */
    private double f12636x;

    /* renamed from: y, reason: collision with root package name */
    private double f12637y;

    protected Point2DBuilder() {
    }

    public static Point2DBuilder<?> create() {
        return new Point2DBuilder<>();
    }

    public B x(double x2) {
        this.f12636x = x2;
        return this;
    }

    public B y(double x2) {
        this.f12637y = x2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public Point2D build() {
        Point2D x2 = new Point2D(this.f12636x, this.f12637y);
        return x2;
    }
}
