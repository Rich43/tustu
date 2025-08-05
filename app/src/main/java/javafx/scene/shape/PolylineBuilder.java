package javafx.scene.shape;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.shape.PolylineBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/shape/PolylineBuilder.class */
public class PolylineBuilder<B extends PolylineBuilder<B>> extends ShapeBuilder<B> implements Builder<Polyline> {
    private boolean __set;
    private Collection<? extends Double> points;

    protected PolylineBuilder() {
    }

    public static PolylineBuilder<?> create() {
        return new PolylineBuilder<>();
    }

    public void applyTo(Polyline x2) {
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
    public Polyline build2() {
        Polyline x2 = new Polyline();
        applyTo(x2);
        return x2;
    }
}
