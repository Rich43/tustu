package javafx.scene.input;

import javafx.scene.input.KeyCombination;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/input/KeyCharacterCombinationBuilder.class */
public final class KeyCharacterCombinationBuilder implements Builder<KeyCharacterCombination> {
    private KeyCombination.ModifierValue alt;
    private String character;
    private KeyCombination.ModifierValue control;
    private KeyCombination.ModifierValue meta;
    private KeyCombination.ModifierValue shift;
    private KeyCombination.ModifierValue shortcut;

    protected KeyCharacterCombinationBuilder() {
    }

    public static KeyCharacterCombinationBuilder create() {
        return new KeyCharacterCombinationBuilder();
    }

    public KeyCharacterCombinationBuilder alt(KeyCombination.ModifierValue x2) {
        this.alt = x2;
        return this;
    }

    public KeyCharacterCombinationBuilder character(String x2) {
        this.character = x2;
        return this;
    }

    public KeyCharacterCombinationBuilder control(KeyCombination.ModifierValue x2) {
        this.control = x2;
        return this;
    }

    public KeyCharacterCombinationBuilder meta(KeyCombination.ModifierValue x2) {
        this.meta = x2;
        return this;
    }

    public KeyCharacterCombinationBuilder shift(KeyCombination.ModifierValue x2) {
        this.shift = x2;
        return this;
    }

    public KeyCharacterCombinationBuilder shortcut(KeyCombination.ModifierValue x2) {
        this.shortcut = x2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public KeyCharacterCombination build2() {
        KeyCharacterCombination x2 = new KeyCharacterCombination(this.character, this.shift, this.control, this.alt, this.meta, this.shortcut);
        return x2;
    }
}
