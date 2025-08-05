package javafx.scene.input;

import javafx.scene.Node;
import javafx.scene.input.MnemonicBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/input/MnemonicBuilder.class */
public class MnemonicBuilder<B extends MnemonicBuilder<B>> implements Builder<Mnemonic> {
    private KeyCombination keyCombination;
    private Node node;

    protected MnemonicBuilder() {
    }

    public static MnemonicBuilder<?> create() {
        return new MnemonicBuilder<>();
    }

    public B keyCombination(KeyCombination x2) {
        this.keyCombination = x2;
        return this;
    }

    public B node(Node x2) {
        this.node = x2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public Mnemonic build2() {
        Mnemonic x2 = new Mnemonic(this.node, this.keyCombination);
        return x2;
    }
}
