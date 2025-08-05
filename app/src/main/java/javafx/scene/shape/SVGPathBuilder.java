package javafx.scene.shape;

import javafx.scene.shape.SVGPathBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/shape/SVGPathBuilder.class */
public class SVGPathBuilder<B extends SVGPathBuilder<B>> extends ShapeBuilder<B> implements Builder<SVGPath> {
    private int __set;
    private String content;
    private FillRule fillRule;

    protected SVGPathBuilder() {
    }

    public static SVGPathBuilder<?> create() {
        return new SVGPathBuilder<>();
    }

    public void applyTo(SVGPath x2) {
        super.applyTo((Shape) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setContent(this.content);
        }
        if ((set & 2) != 0) {
            x2.setFillRule(this.fillRule);
        }
    }

    public B content(String x2) {
        this.content = x2;
        this.__set |= 1;
        return this;
    }

    public B fillRule(FillRule x2) {
        this.fillRule = x2;
        this.__set |= 2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public SVGPath build2() {
        SVGPath x2 = new SVGPath();
        applyTo(x2);
        return x2;
    }
}
