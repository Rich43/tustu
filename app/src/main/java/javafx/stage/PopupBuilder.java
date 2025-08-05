package javafx.stage;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.Node;
import javafx.stage.PopupBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/stage/PopupBuilder.class */
public class PopupBuilder<B extends PopupBuilder<B>> extends PopupWindowBuilder<B> implements Builder<Popup> {
    private boolean __set;
    private Collection<? extends Node> content;

    protected PopupBuilder() {
    }

    public static PopupBuilder<?> create() {
        return new PopupBuilder<>();
    }

    public void applyTo(Popup x2) {
        super.applyTo((PopupWindow) x2);
        if (this.__set) {
            x2.getContent().addAll(this.content);
        }
    }

    public B content(Collection<? extends Node> x2) {
        this.content = x2;
        this.__set = true;
        return this;
    }

    public B content(Node... nodeArr) {
        return (B) content(Arrays.asList(nodeArr));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public Popup build2() {
        Popup x2 = new Popup();
        applyTo(x2);
        return x2;
    }
}
