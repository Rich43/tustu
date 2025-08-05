package javafx.scene.input;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.tk.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.icepdf.core.util.PdfOps;
import org.slf4j.Marker;

/* loaded from: jfxrt.jar:javafx/scene/input/KeyCombination.class */
public abstract class KeyCombination {
    public static final Modifier SHIFT_DOWN = new Modifier(KeyCode.SHIFT, ModifierValue.DOWN);
    public static final Modifier SHIFT_ANY = new Modifier(KeyCode.SHIFT, ModifierValue.ANY);
    public static final Modifier CONTROL_DOWN = new Modifier(KeyCode.CONTROL, ModifierValue.DOWN);
    public static final Modifier CONTROL_ANY = new Modifier(KeyCode.CONTROL, ModifierValue.ANY);
    public static final Modifier ALT_DOWN = new Modifier(KeyCode.ALT, ModifierValue.DOWN);
    public static final Modifier ALT_ANY = new Modifier(KeyCode.ALT, ModifierValue.ANY);
    public static final Modifier META_DOWN = new Modifier(KeyCode.META, ModifierValue.DOWN);
    public static final Modifier META_ANY = new Modifier(KeyCode.META, ModifierValue.ANY);
    public static final Modifier SHORTCUT_DOWN = new Modifier(KeyCode.SHORTCUT, ModifierValue.DOWN);
    public static final Modifier SHORTCUT_ANY = new Modifier(KeyCode.SHORTCUT, ModifierValue.ANY);
    private static final Modifier[] POSSIBLE_MODIFIERS = {SHIFT_DOWN, SHIFT_ANY, CONTROL_DOWN, CONTROL_ANY, ALT_DOWN, ALT_ANY, META_DOWN, META_ANY, SHORTCUT_DOWN, SHORTCUT_ANY};
    public static final KeyCombination NO_MATCH = new KeyCombination(new Modifier[0]) { // from class: javafx.scene.input.KeyCombination.1
        @Override // javafx.scene.input.KeyCombination
        public boolean match(KeyEvent e2) {
            return false;
        }
    };
    private final ModifierValue shift;
    private final ModifierValue control;
    private final ModifierValue alt;
    private final ModifierValue meta;
    private final ModifierValue shortcut;

    /* loaded from: jfxrt.jar:javafx/scene/input/KeyCombination$ModifierValue.class */
    public enum ModifierValue {
        DOWN,
        UP,
        ANY
    }

    public final ModifierValue getShift() {
        return this.shift;
    }

    public final ModifierValue getControl() {
        return this.control;
    }

    public final ModifierValue getAlt() {
        return this.alt;
    }

    public final ModifierValue getMeta() {
        return this.meta;
    }

    public final ModifierValue getShortcut() {
        return this.shortcut;
    }

    protected KeyCombination(ModifierValue shift, ModifierValue control, ModifierValue alt, ModifierValue meta, ModifierValue shortcut) {
        if (shift == null || control == null || alt == null || meta == null || shortcut == null) {
            throw new NullPointerException("Modifier value must not be null!");
        }
        this.shift = shift;
        this.control = control;
        this.alt = alt;
        this.meta = meta;
        this.shortcut = shortcut;
    }

    protected KeyCombination(Modifier... modifiers) {
        this(getModifierValue(modifiers, KeyCode.SHIFT), getModifierValue(modifiers, KeyCode.CONTROL), getModifierValue(modifiers, KeyCode.ALT), getModifierValue(modifiers, KeyCode.META), getModifierValue(modifiers, KeyCode.SHORTCUT));
    }

    public boolean match(KeyEvent event) {
        KeyCode shortcutKey = Toolkit.getToolkit().getPlatformShortcutKey();
        return test(KeyCode.SHIFT, this.shift, shortcutKey, this.shortcut, event.isShiftDown()) && test(KeyCode.CONTROL, this.control, shortcutKey, this.shortcut, event.isControlDown()) && test(KeyCode.ALT, this.alt, shortcutKey, this.shortcut, event.isAltDown()) && test(KeyCode.META, this.meta, shortcutKey, this.shortcut, event.isMetaDown());
    }

    public String getName() {
        StringBuilder sb = new StringBuilder();
        addModifiersIntoString(sb);
        return sb.toString();
    }

    public String getDisplayText() {
        StringBuilder stringBuilder = new StringBuilder();
        if (PlatformUtil.isMac()) {
            if (getControl() == ModifierValue.DOWN) {
                stringBuilder.append("⌃");
            }
            if (getAlt() == ModifierValue.DOWN) {
                stringBuilder.append("⌥");
            }
            if (getShift() == ModifierValue.DOWN) {
                stringBuilder.append("⇧");
            }
            if (getMeta() == ModifierValue.DOWN || getShortcut() == ModifierValue.DOWN) {
                stringBuilder.append("⌘");
            }
        } else {
            if (getControl() == ModifierValue.DOWN || getShortcut() == ModifierValue.DOWN) {
                stringBuilder.append("Ctrl+");
            }
            if (getAlt() == ModifierValue.DOWN) {
                stringBuilder.append("Alt+");
            }
            if (getShift() == ModifierValue.DOWN) {
                stringBuilder.append("Shift+");
            }
            if (getMeta() == ModifierValue.DOWN) {
                stringBuilder.append("Meta+");
            }
        }
        return stringBuilder.toString();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof KeyCombination)) {
            return false;
        }
        KeyCombination other = (KeyCombination) obj;
        return this.shift == other.shift && this.control == other.control && this.alt == other.alt && this.meta == other.meta && this.shortcut == other.shortcut;
    }

    public int hashCode() {
        int hash = (23 * 7) + this.shift.hashCode();
        return (23 * ((23 * ((23 * ((23 * hash) + this.control.hashCode())) + this.alt.hashCode())) + this.meta.hashCode())) + this.shortcut.hashCode();
    }

    public String toString() {
        return getName();
    }

    public static KeyCombination valueOf(String value) {
        List<Modifier> modifiers = new ArrayList<>(4);
        String[] tokens = splitName(value);
        KeyCode keyCode = null;
        String keyCharacter = null;
        for (String token : tokens) {
            if (token.length() > 2 && token.charAt(0) == '\'' && token.charAt(token.length() - 1) == '\'') {
                if (keyCode != null || keyCharacter != null) {
                    throw new IllegalArgumentException("Cannot parse key binding " + value);
                }
                keyCharacter = token.substring(1, token.length() - 1).replace("\\'", PdfOps.SINGLE_QUOTE_TOKEN);
            } else {
                String normalizedToken = normalizeToken(token);
                Modifier modifier = getModifier(normalizedToken);
                if (modifier != null) {
                    modifiers.add(modifier);
                } else {
                    if (keyCode != null || keyCharacter != null) {
                        throw new IllegalArgumentException("Cannot parse key binding " + value);
                    }
                    keyCode = KeyCode.getKeyCode(normalizedToken);
                    if (keyCode == null) {
                        keyCharacter = token;
                    }
                }
            }
        }
        if (keyCode == null && keyCharacter == null) {
            throw new IllegalArgumentException("Cannot parse key binding " + value);
        }
        Modifier[] modifierArray = (Modifier[]) modifiers.toArray(new Modifier[modifiers.size()]);
        return keyCode != null ? new KeyCodeCombination(keyCode, modifierArray) : new KeyCharacterCombination(keyCharacter, modifierArray);
    }

    public static KeyCombination keyCombination(String name) {
        return valueOf(name);
    }

    /* loaded from: jfxrt.jar:javafx/scene/input/KeyCombination$Modifier.class */
    public static final class Modifier {
        private final KeyCode key;
        private final ModifierValue value;

        private Modifier(KeyCode key, ModifierValue value) {
            this.key = key;
            this.value = value;
        }

        public KeyCode getKey() {
            return this.key;
        }

        public ModifierValue getValue() {
            return this.value;
        }

        public String toString() {
            return (this.value == ModifierValue.ANY ? "Ignore " : "") + this.key.getName();
        }
    }

    private void addModifiersIntoString(StringBuilder sb) {
        addModifierIntoString(sb, KeyCode.SHIFT, this.shift);
        addModifierIntoString(sb, KeyCode.CONTROL, this.control);
        addModifierIntoString(sb, KeyCode.ALT, this.alt);
        addModifierIntoString(sb, KeyCode.META, this.meta);
        addModifierIntoString(sb, KeyCode.SHORTCUT, this.shortcut);
    }

    private static void addModifierIntoString(StringBuilder sb, KeyCode modifierKey, ModifierValue modifierValue) {
        if (modifierValue == ModifierValue.UP) {
            return;
        }
        if (sb.length() > 0) {
            sb.append(Marker.ANY_NON_NULL_MARKER);
        }
        if (modifierValue == ModifierValue.ANY) {
            sb.append("Ignore ");
        }
        sb.append(modifierKey.getName());
    }

    private static boolean test(KeyCode testedModifierKey, ModifierValue testedModifierValue, KeyCode shortcutModifierKey, ModifierValue shortcutModifierValue, boolean isKeyDown) {
        ModifierValue finalModifierValue = testedModifierKey == shortcutModifierKey ? resolveModifierValue(testedModifierValue, shortcutModifierValue) : testedModifierValue;
        return test(finalModifierValue, isKeyDown);
    }

    private static boolean test(ModifierValue modifierValue, boolean isDown) {
        switch (modifierValue) {
            case DOWN:
                return isDown;
            case UP:
                return !isDown;
            case ANY:
            default:
                return true;
        }
    }

    private static ModifierValue resolveModifierValue(ModifierValue firstValue, ModifierValue secondValue) {
        if (firstValue == ModifierValue.DOWN || secondValue == ModifierValue.DOWN) {
            return ModifierValue.DOWN;
        }
        if (firstValue == ModifierValue.ANY || secondValue == ModifierValue.ANY) {
            return ModifierValue.ANY;
        }
        return ModifierValue.UP;
    }

    static Modifier getModifier(String name) {
        for (Modifier modifier : POSSIBLE_MODIFIERS) {
            if (modifier.toString().equals(name)) {
                return modifier;
            }
        }
        return null;
    }

    private static ModifierValue getModifierValue(Modifier[] modifiers, KeyCode modifierKey) {
        ModifierValue modifierValue = ModifierValue.UP;
        for (Modifier modifier : modifiers) {
            if (modifier == null) {
                throw new NullPointerException("Modifier must not be null!");
            }
            if (modifier.getKey() == modifierKey) {
                if (modifierValue != ModifierValue.UP) {
                    throw new IllegalArgumentException(modifier.getValue() != modifierValue ? "Conflicting modifiers specified!" : "Duplicate modifiers specified!");
                }
                modifierValue = modifier.getValue();
            }
        }
        return modifierValue;
    }

    private static String normalizeToken(String token) {
        String[] words = token.split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(word.substring(0, 1).toUpperCase(Locale.ROOT));
            sb.append(word.substring(1).toLowerCase(Locale.ROOT));
        }
        return sb.toString();
    }

    private static String[] splitName(String name) {
        List<String> tokens = new ArrayList<>();
        char[] chars = name.trim().toCharArray();
        int state = 0;
        int tokenStart = 0;
        int tokenEnd = -1;
        for (int i2 = 0; i2 < chars.length; i2++) {
            char c2 = chars[i2];
            switch (state) {
                case 0:
                    switch (c2) {
                        case '\t':
                        case '\n':
                        case 11:
                        case '\f':
                        case '\r':
                        case ' ':
                            tokenEnd = i2;
                            state = 1;
                            break;
                        case '\'':
                            if (i2 == 0 || chars[i2 - 1] != '\\') {
                                state = 3;
                                break;
                            } else {
                                break;
                            }
                        case '+':
                            tokenEnd = i2;
                            state = 2;
                            break;
                    }
                case 1:
                    switch (c2) {
                        case '\t':
                        case '\n':
                        case 11:
                        case '\f':
                        case '\r':
                        case ' ':
                            break;
                        case '\'':
                            state = 3;
                            tokenEnd = -1;
                            break;
                        case '+':
                            state = 2;
                            break;
                        default:
                            state = 0;
                            tokenEnd = -1;
                            break;
                    }
                case 2:
                    switch (c2) {
                        case '\t':
                        case '\n':
                        case 11:
                        case '\f':
                        case '\r':
                        case ' ':
                            break;
                        case '+':
                            throw new IllegalArgumentException("Cannot parse key binding " + name);
                        default:
                            if (tokenEnd <= tokenStart) {
                                throw new IllegalArgumentException("Cannot parse key binding " + name);
                            }
                            tokens.add(new String(chars, tokenStart, tokenEnd - tokenStart));
                            tokenStart = i2;
                            tokenEnd = -1;
                            state = c2 == '\'' ? 3 : 0;
                            break;
                    }
                case 3:
                    if (c2 != '\'' || chars[i2 - 1] == '\\') {
                        break;
                    } else {
                        state = 0;
                        break;
                    }
            }
        }
        switch (state) {
            case 0:
            case 1:
                tokens.add(new String(chars, tokenStart, chars.length - tokenStart));
                break;
            case 2:
            case 3:
                throw new IllegalArgumentException("Cannot parse key binding " + name);
        }
        return (String[]) tokens.toArray(new String[tokens.size()]);
    }
}
