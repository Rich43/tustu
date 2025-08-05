package javafx.scene.control;

import javafx.scene.Node;
import javafx.scene.control.LabelBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/LabelBuilder.class */
public class LabelBuilder<B extends LabelBuilder<B>> extends LabeledBuilder<B> implements Builder<Label> {
    private boolean __set;
    private Node labelFor;

    protected LabelBuilder() {
    }

    public static LabelBuilder<?> create() {
        return new LabelBuilder<>();
    }

    public void applyTo(Label x2) {
        super.applyTo((Labeled) x2);
        if (this.__set) {
            x2.setLabelFor(this.labelFor);
        }
    }

    public B labelFor(Node x2) {
        this.labelFor = x2;
        this.__set = true;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public Label build2() {
        Label x2 = new Label();
        applyTo(x2);
        return x2;
    }
}
