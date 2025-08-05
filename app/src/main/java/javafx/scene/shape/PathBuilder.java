package javafx.scene.shape;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.shape.PathBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/shape/PathBuilder.class */
public class PathBuilder<B extends PathBuilder<B>> extends ShapeBuilder<B> implements Builder<Path> {
    private int __set;
    private Collection<? extends PathElement> elements;
    private FillRule fillRule;

    protected PathBuilder() {
    }

    public static PathBuilder<?> create() {
        return new PathBuilder<>();
    }

    public void applyTo(Path x2) {
        super.applyTo((Shape) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.getElements().addAll(this.elements);
        }
        if ((set & 2) != 0) {
            x2.setFillRule(this.fillRule);
        }
    }

    public B elements(Collection<? extends PathElement> x2) {
        this.elements = x2;
        this.__set |= 1;
        return this;
    }

    public B elements(PathElement... pathElementArr) {
        return (B) elements(Arrays.asList(pathElementArr));
    }

    public B fillRule(FillRule x2) {
        this.fillRule = x2;
        this.__set |= 2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public Path build2() {
        Path x2 = new Path();
        applyTo(x2);
        return x2;
    }
}
