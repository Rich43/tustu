package javafx.scene.input;

import com.sun.javafx.tk.Toolkit;
import javafx.beans.NamedArg;
import javafx.scene.input.KeyCombination;
import org.icepdf.core.util.PdfOps;
import org.slf4j.Marker;

/* loaded from: jfxrt.jar:javafx/scene/input/KeyCharacterCombination.class */
public final class KeyCharacterCombination extends KeyCombination {
    private String character;

    public final String getCharacter() {
        return this.character;
    }

    public KeyCharacterCombination(@NamedArg("character") String character, @NamedArg("shift") KeyCombination.ModifierValue shift, @NamedArg("control") KeyCombination.ModifierValue control, @NamedArg("alt") KeyCombination.ModifierValue alt, @NamedArg("meta") KeyCombination.ModifierValue meta, @NamedArg("shortcut") KeyCombination.ModifierValue shortcut) {
        super(shift, control, alt, meta, shortcut);
        this.character = "";
        validateKeyCharacter(character);
        this.character = character;
    }

    public KeyCharacterCombination(@NamedArg("character") String character, @NamedArg("modifiers") KeyCombination.Modifier... modifiers) {
        super(modifiers);
        this.character = "";
        validateKeyCharacter(character);
        this.character = character;
    }

    @Override // javafx.scene.input.KeyCombination
    public boolean match(KeyEvent event) {
        return event.getCode() != KeyCode.UNDEFINED && event.getCode().impl_getCode() == Toolkit.getToolkit().getKeyCodeForChar(getCharacter()) && super.match(event);
    }

    @Override // javafx.scene.input.KeyCombination
    public String getName() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getName());
        if (sb.length() > 0) {
            sb.append(Marker.ANY_NON_NULL_MARKER);
        }
        return sb.append('\'').append(this.character.replace(PdfOps.SINGLE_QUOTE_TOKEN, "\\'")).append('\'').toString();
    }

    @Override // javafx.scene.input.KeyCombination
    public String getDisplayText() {
        return super.getDisplayText() + getCharacter();
    }

    @Override // javafx.scene.input.KeyCombination
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof KeyCharacterCombination) && this.character.equals(((KeyCharacterCombination) obj).getCharacter()) && super.equals(obj);
    }

    @Override // javafx.scene.input.KeyCombination
    public int hashCode() {
        return (23 * super.hashCode()) + this.character.hashCode();
    }

    private static void validateKeyCharacter(String keyCharacter) {
        if (keyCharacter == null) {
            throw new NullPointerException("Key character must not be null!");
        }
    }
}
