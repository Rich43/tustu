package javafx.geometry;

import javafx.geometry.Point3DBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/geometry/Point3DBuilder.class */
public class Point3DBuilder<B extends Point3DBuilder<B>> implements Builder<Point3D> {

    /* renamed from: x, reason: collision with root package name */
    private double f12641x;

    /* renamed from: y, reason: collision with root package name */
    private double f12642y;

    /* renamed from: z, reason: collision with root package name */
    private double f12643z;

    protected Point3DBuilder() {
    }

    public static Point3DBuilder<?> create() {
        return new Point3DBuilder<>();
    }

    public B x(double x2) {
        this.f12641x = x2;
        return this;
    }

    public B y(double x2) {
        this.f12642y = x2;
        return this;
    }

    public B z(double x2) {
        this.f12643z = x2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public Point3D build() {
        Point3D x2 = new Point3D(this.f12641x, this.f12642y, this.f12643z);
        return x2;
    }
}
