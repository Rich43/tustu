package javafx.scene.input;

import javafx.scene.input.KeyCombination;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/input/KeyCodeCombinationBuilder.class */
public final class KeyCodeCombinationBuilder implements Builder<KeyCodeCombination> {
    private KeyCombination.ModifierValue alt;
    private KeyCode code;
    private KeyCombination.ModifierValue control;
    private KeyCombination.ModifierValue meta;
    private KeyCombination.ModifierValue shift;
    private KeyCombination.ModifierValue shortcut;

    protected KeyCodeCombinationBuilder() {
    }

    public static KeyCodeCombinationBuilder create() {
        return new KeyCodeCombinationBuilder();
    }

    public KeyCodeCombinationBuilder alt(KeyCombination.ModifierValue x2) {
        this.alt = x2;
        return this;
    }

    public KeyCodeCombinationBuilder code(KeyCode x2) {
        this.code = x2;
        return this;
    }

    public KeyCodeCombinationBuilder control(KeyCombination.ModifierValue x2) {
        this.control = x2;
        return this;
    }

    public KeyCodeCombinationBuilder meta(KeyCombination.ModifierValue x2) {
        this.meta = x2;
        return this;
    }

    public KeyCodeCombinationBuilder shift(KeyCombination.ModifierValue x2) {
        this.shift = x2;
        return this;
    }

    public KeyCodeCombinationBuilder shortcut(KeyCombination.ModifierValue x2) {
        this.shortcut = x2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public KeyCodeCombination build() {
        KeyCodeCombination x2 = new KeyCodeCombination(this.code, this.shift, this.control, this.alt, this.meta, this.shortcut);
        return x2;
    }
}
