package javafx.scene.input;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.util.Utils;
import javafx.beans.NamedArg;
import javafx.scene.input.KeyCombination;
import org.slf4j.Marker;

/* loaded from: jfxrt.jar:javafx/scene/input/KeyCodeCombination.class */
public final class KeyCodeCombination extends KeyCombination {
    private KeyCode code;

    public final KeyCode getCode() {
        return this.code;
    }

    public KeyCodeCombination(@NamedArg("code") KeyCode code, @NamedArg("shift") KeyCombination.ModifierValue shift, @NamedArg("control") KeyCombination.ModifierValue control, @NamedArg("alt") KeyCombination.ModifierValue alt, @NamedArg("meta") KeyCombination.ModifierValue meta, @NamedArg("shortcut") KeyCombination.ModifierValue shortcut) {
        super(shift, control, alt, meta, shortcut);
        validateKeyCode(code);
        this.code = code;
    }

    public KeyCodeCombination(@NamedArg("code") KeyCode code, @NamedArg("modifiers") KeyCombination.Modifier... modifiers) {
        super(modifiers);
        validateKeyCode(code);
        this.code = code;
    }

    @Override // javafx.scene.input.KeyCombination
    public boolean match(KeyEvent event) {
        return event.getCode() == getCode() && super.match(event);
    }

    @Override // javafx.scene.input.KeyCombination
    public String getName() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getName());
        if (sb.length() > 0) {
            sb.append(Marker.ANY_NON_NULL_MARKER);
        }
        return sb.append(this.code.getName()).toString();
    }

    @Override // javafx.scene.input.KeyCombination
    public String getDisplayText() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getDisplayText());
        int initialLength = sb.length();
        char c2 = getSingleChar(this.code);
        if (c2 != 0) {
            sb.append(c2);
            return sb.toString();
        }
        String name = this.code.toString();
        String[] words = Utils.split(name, "_");
        for (String word : words) {
            if (sb.length() > initialLength) {
                sb.append(' ');
            }
            sb.append(word.charAt(0));
            sb.append(word.substring(1).toLowerCase());
        }
        return sb.toString();
    }

    @Override // javafx.scene.input.KeyCombination
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof KeyCodeCombination) && getCode() == ((KeyCodeCombination) obj).getCode() && super.equals(obj);
    }

    @Override // javafx.scene.input.KeyCombination
    public int hashCode() {
        return (23 * super.hashCode()) + this.code.hashCode();
    }

    private static void validateKeyCode(KeyCode keyCode) {
        if (keyCode == null) {
            throw new NullPointerException("Key code must not be null!");
        }
        if (getModifier(keyCode.getName()) != null) {
            throw new IllegalArgumentException("Key code must not match modifier key!");
        }
        if (keyCode == KeyCode.UNDEFINED) {
            throw new IllegalArgumentException("Key code must differ from undefined value!");
        }
    }

    private static char getSingleChar(KeyCode code) {
        switch (code) {
            case ENTER:
                return (char) 8629;
            case LEFT:
                return (char) 8592;
            case UP:
                return (char) 8593;
            case RIGHT:
                return (char) 8594;
            case DOWN:
                return (char) 8595;
            case COMMA:
                return ',';
            case MINUS:
                return '-';
            case PERIOD:
                return '.';
            case SLASH:
                return '/';
            case SEMICOLON:
                return ';';
            case EQUALS:
                return '=';
            case OPEN_BRACKET:
                return '[';
            case BACK_SLASH:
                return '\\';
            case CLOSE_BRACKET:
                return ']';
            case MULTIPLY:
                return '*';
            case ADD:
                return '+';
            case SUBTRACT:
                return '-';
            case DECIMAL:
                return '.';
            case DIVIDE:
                return '/';
            case BACK_QUOTE:
                return '`';
            case QUOTE:
                return '\"';
            case AMPERSAND:
                return '&';
            case ASTERISK:
                return '*';
            case LESS:
                return '<';
            case GREATER:
                return '>';
            case BRACELEFT:
                return '{';
            case BRACERIGHT:
                return '}';
            case AT:
                return '@';
            case COLON:
                return ':';
            case CIRCUMFLEX:
                return '^';
            case DOLLAR:
                return '$';
            case EURO_SIGN:
                return (char) 8364;
            case EXCLAMATION_MARK:
                return '!';
            case LEFT_PARENTHESIS:
                return '(';
            case NUMBER_SIGN:
                return '#';
            case PLUS:
                return '+';
            case RIGHT_PARENTHESIS:
                return ')';
            case UNDERSCORE:
                return '_';
            case DIGIT0:
                return '0';
            case DIGIT1:
                return '1';
            case DIGIT2:
                return '2';
            case DIGIT3:
                return '3';
            case DIGIT4:
                return '4';
            case DIGIT5:
                return '5';
            case DIGIT6:
                return '6';
            case DIGIT7:
                return '7';
            case DIGIT8:
                return '8';
            case DIGIT9:
                return '9';
            default:
                if (PlatformUtil.isMac()) {
                    switch (code) {
                        case BACK_SPACE:
                            return (char) 9003;
                        case ESCAPE:
                            return (char) 9099;
                        case DELETE:
                            return (char) 8998;
                        default:
                            return (char) 0;
                    }
                }
                return (char) 0;
        }
    }
}
