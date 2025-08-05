package javafx.scene.control;

import javafx.scene.control.HyperlinkBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/HyperlinkBuilder.class */
public class HyperlinkBuilder<B extends HyperlinkBuilder<B>> extends ButtonBaseBuilder<B> implements Builder<Hyperlink> {
    private boolean __set;
    private boolean visited;

    protected HyperlinkBuilder() {
    }

    public static HyperlinkBuilder<?> create() {
        return new HyperlinkBuilder<>();
    }

    public void applyTo(Hyperlink x2) {
        super.applyTo((ButtonBase) x2);
        if (this.__set) {
            x2.setVisited(this.visited);
        }
    }

    public B visited(boolean x2) {
        this.visited = x2;
        this.__set = true;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public Hyperlink build() {
        Hyperlink x2 = new Hyperlink();
        applyTo(x2);
        return x2;
    }
}
