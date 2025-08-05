package javafx.scene.canvas;

import javafx.scene.Node;
import javafx.scene.NodeBuilder;
import javafx.scene.canvas.CanvasBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/canvas/CanvasBuilder.class */
public class CanvasBuilder<B extends CanvasBuilder<B>> extends NodeBuilder<B> implements Builder<Canvas> {
    private int __set;
    private double height;
    private double width;

    protected CanvasBuilder() {
    }

    public static CanvasBuilder<?> create() {
        return new CanvasBuilder<>();
    }

    public void applyTo(Canvas x2) {
        super.applyTo((Node) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setHeight(this.height);
        }
        if ((set & 2) != 0) {
            x2.setWidth(this.width);
        }
    }

    public B height(double x2) {
        this.height = x2;
        this.__set |= 1;
        return this;
    }

    public B width(double x2) {
        this.width = x2;
        this.__set |= 2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public Canvas build2() {
        Canvas x2 = new Canvas();
        applyTo(x2);
        return x2;
    }
}
