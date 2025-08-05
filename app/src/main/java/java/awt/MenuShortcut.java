package java.awt;

import java.awt.event.KeyEvent;
import java.io.Serializable;
import org.slf4j.Marker;

/* loaded from: rt.jar:java/awt/MenuShortcut.class */
public class MenuShortcut implements Serializable {
    int key;
    boolean usesShift;
    private static final long serialVersionUID = 143448358473180225L;

    public MenuShortcut(int i2) {
        this(i2, false);
    }

    public MenuShortcut(int i2, boolean z2) {
        this.key = i2;
        this.usesShift = z2;
    }

    public int getKey() {
        return this.key;
    }

    public boolean usesShiftModifier() {
        return this.usesShift;
    }

    public boolean equals(MenuShortcut menuShortcut) {
        return menuShortcut != null && menuShortcut.getKey() == this.key && menuShortcut.usesShiftModifier() == this.usesShift;
    }

    public boolean equals(Object obj) {
        if (obj instanceof MenuShortcut) {
            return equals((MenuShortcut) obj);
        }
        return false;
    }

    public int hashCode() {
        return this.usesShift ? this.key ^ (-1) : this.key;
    }

    public String toString() throws HeadlessException {
        int menuShortcutKeyMask = 0;
        if (!GraphicsEnvironment.isHeadless()) {
            menuShortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
        }
        if (usesShiftModifier()) {
            menuShortcutKeyMask |= 1;
        }
        return KeyEvent.getKeyModifiersText(menuShortcutKeyMask) + Marker.ANY_NON_NULL_MARKER + KeyEvent.getKeyText(this.key);
    }

    protected String paramString() {
        String str = "key=" + this.key;
        if (usesShiftModifier()) {
            str = str + ",usesShiftModifier";
        }
        return str;
    }
}
