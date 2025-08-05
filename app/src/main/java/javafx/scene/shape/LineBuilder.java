package javafx.scene.shape;

import javafx.scene.shape.LineBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/shape/LineBuilder.class */
public class LineBuilder<B extends LineBuilder<B>> extends ShapeBuilder<B> implements Builder<Line> {
    private int __set;
    private double endX;
    private double endY;
    private double startX;
    private double startY;

    protected LineBuilder() {
    }

    public static LineBuilder<?> create() {
        return new LineBuilder<>();
    }

    public void applyTo(Line x2) {
        super.applyTo((Shape) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setEndX(this.endX);
        }
        if ((set & 2) != 0) {
            x2.setEndY(this.endY);
        }
        if ((set & 4) != 0) {
            x2.setStartX(this.startX);
        }
        if ((set & 8) != 0) {
            x2.setStartY(this.startY);
        }
    }

    public B endX(double x2) {
        this.endX = x2;
        this.__set |= 1;
        return this;
    }

    public B endY(double x2) {
        this.endY = x2;
        this.__set |= 2;
        return this;
    }

    public B startX(double x2) {
        this.startX = x2;
        this.__set |= 4;
        return this;
    }

    public B startY(double x2) {
        this.startY = x2;
        this.__set |= 8;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public Line build2() {
        Line x2 = new Line();
        applyTo(x2);
        return x2;
    }
}
