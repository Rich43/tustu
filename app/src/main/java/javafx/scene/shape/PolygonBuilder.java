package javafx.scene.shape;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.shape.PolygonBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/shape/PolygonBuilder.class */
public class PolygonBuilder<B extends PolygonBuilder<B>> extends ShapeBuilder<B> implements Builder<Polygon> {
    private boolean __set;
    private Collection<? extends Double> points;

    protected PolygonBuilder() {
    }

    public static PolygonBuilder<?> create() {
        return new PolygonBuilder<>();
    }

    public void applyTo(Polygon x2) {
        super.applyTo((Shape) x2);
        if (this.__set) {
            x2.getPoints().addAll(this.points);
        }
    }

    public B points(Collection<? extends Double> x2) {
        this.points = x2;
        this.__set = true;
        return this;
    }

    public B points(Double... dArr) {
        return (B) points(Arrays.asList(dArr));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public Polygon build2() {
        Polygon x2 = new Polygon();
        applyTo(x2);
        return x2;
    }
}
