package javax.swing;

import java.awt.AWTKeyStroke;
import java.awt.event.KeyEvent;

/* loaded from: rt.jar:javax/swing/KeyStroke.class */
public class KeyStroke extends AWTKeyStroke {
    private static final long serialVersionUID = -9060180771037902530L;

    private KeyStroke() {
    }

    private KeyStroke(char c2, int i2, int i3, boolean z2) {
        super(c2, i2, i3, z2);
    }

    public static KeyStroke getKeyStroke(char c2) {
        KeyStroke keyStroke;
        synchronized (AWTKeyStroke.class) {
            registerSubclass(KeyStroke.class);
            keyStroke = (KeyStroke) getAWTKeyStroke(c2);
        }
        return keyStroke;
    }

    @Deprecated
    public static KeyStroke getKeyStroke(char c2, boolean z2) {
        return new KeyStroke(c2, 0, 0, z2);
    }

    public static KeyStroke getKeyStroke(Character ch, int i2) {
        KeyStroke keyStroke;
        synchronized (AWTKeyStroke.class) {
            registerSubclass(KeyStroke.class);
            keyStroke = (KeyStroke) getAWTKeyStroke(ch, i2);
        }
        return keyStroke;
    }

    public static KeyStroke getKeyStroke(int i2, int i3, boolean z2) {
        KeyStroke keyStroke;
        synchronized (AWTKeyStroke.class) {
            registerSubclass(KeyStroke.class);
            keyStroke = (KeyStroke) getAWTKeyStroke(i2, i3, z2);
        }
        return keyStroke;
    }

    public static KeyStroke getKeyStroke(int i2, int i3) {
        KeyStroke keyStroke;
        synchronized (AWTKeyStroke.class) {
            registerSubclass(KeyStroke.class);
            keyStroke = (KeyStroke) getAWTKeyStroke(i2, i3);
        }
        return keyStroke;
    }

    public static KeyStroke getKeyStrokeForEvent(KeyEvent keyEvent) {
        KeyStroke keyStroke;
        synchronized (AWTKeyStroke.class) {
            registerSubclass(KeyStroke.class);
            keyStroke = (KeyStroke) getAWTKeyStrokeForEvent(keyEvent);
        }
        return keyStroke;
    }

    public static KeyStroke getKeyStroke(String str) {
        KeyStroke keyStroke;
        if (str == null || str.length() == 0) {
            return null;
        }
        synchronized (AWTKeyStroke.class) {
            registerSubclass(KeyStroke.class);
            try {
                keyStroke = (KeyStroke) getAWTKeyStroke(str);
            } catch (IllegalArgumentException e2) {
                return null;
            }
        }
        return keyStroke;
    }
}
