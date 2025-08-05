package javafx.embed.swing;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javafx.event.EventType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;

/* loaded from: jfxrt.jar:javafx/embed/swing/SwingEvents.class */
class SwingEvents {
    SwingEvents() {
    }

    static int mouseIDToEmbedMouseType(int id) {
        switch (id) {
            case 500:
                return 2;
            case 501:
                return 0;
            case 502:
                return 1;
            case 503:
                return 5;
            case 504:
                return 3;
            case 505:
                return 4;
            case 506:
                return 6;
            case 507:
                return 7;
            default:
                return 0;
        }
    }

    static int mouseButtonToEmbedMouseButton(int button, int extModifiers) {
        int abstractButton = 0;
        switch (button) {
            case 1:
                abstractButton = 1;
                break;
            case 2:
                abstractButton = 4;
                break;
            case 3:
                abstractButton = 2;
                break;
        }
        if ((extModifiers & 1024) != 0) {
            abstractButton = 1;
        } else if ((extModifiers & 2048) != 0) {
            abstractButton = 4;
        } else if ((extModifiers & 4096) != 0) {
            abstractButton = 2;
        }
        return abstractButton;
    }

    static int getWheelRotation(MouseEvent e2) {
        if (e2 instanceof MouseWheelEvent) {
            return ((MouseWheelEvent) e2).getWheelRotation();
        }
        return 0;
    }

    static int keyIDToEmbedKeyType(int id) {
        switch (id) {
            case 400:
                return 2;
            case 401:
                return 0;
            case 402:
                return 1;
            default:
                return 0;
        }
    }

    static int keyModifiersToEmbedKeyModifiers(int extModifiers) {
        int embedModifiers = 0;
        if ((extModifiers & 64) != 0) {
            embedModifiers = 0 | 1;
        }
        if ((extModifiers & 128) != 0) {
            embedModifiers |= 2;
        }
        if ((extModifiers & 512) != 0) {
            embedModifiers |= 4;
        }
        if ((extModifiers & 256) != 0) {
            embedModifiers |= 8;
        }
        return embedModifiers;
    }

    static char keyCharToEmbedKeyChar(char ch) {
        if (ch == '\n') {
            return '\r';
        }
        return ch;
    }

    static int fxMouseEventTypeToMouseID(javafx.scene.input.MouseEvent event) {
        EventType<?> type = event.getEventType();
        if (type == javafx.scene.input.MouseEvent.MOUSE_MOVED) {
            return 503;
        }
        if (type == javafx.scene.input.MouseEvent.MOUSE_PRESSED) {
            return 501;
        }
        if (type == javafx.scene.input.MouseEvent.MOUSE_RELEASED) {
            return 502;
        }
        if (type == javafx.scene.input.MouseEvent.MOUSE_CLICKED) {
            return 500;
        }
        if (type == javafx.scene.input.MouseEvent.MOUSE_ENTERED) {
            return 504;
        }
        if (type == javafx.scene.input.MouseEvent.MOUSE_EXITED) {
            return 505;
        }
        if (type == javafx.scene.input.MouseEvent.MOUSE_DRAGGED) {
            return 506;
        }
        if (type == javafx.scene.input.MouseEvent.DRAG_DETECTED) {
            return -1;
        }
        throw new RuntimeException("Unknown MouseEvent type: " + ((Object) type));
    }

    static int fxMouseModsToMouseMods(javafx.scene.input.MouseEvent event) {
        int mods = 0;
        if (event.isAltDown()) {
            mods = 0 | 512;
        }
        if (event.isControlDown()) {
            mods |= 128;
        }
        if (event.isMetaDown()) {
            mods |= 256;
        }
        if (event.isShiftDown()) {
            mods |= 64;
        }
        if (event.isPrimaryButtonDown()) {
            mods |= 1024;
        }
        if (event.isSecondaryButtonDown()) {
            mods |= 4096;
        }
        if (event.isMiddleButtonDown()) {
            mods |= 2048;
        }
        return mods;
    }

    static int fxMouseButtonToMouseButton(javafx.scene.input.MouseEvent event) {
        switch (event.getButton()) {
            case PRIMARY:
                return 1;
            case SECONDARY:
                return 3;
            case MIDDLE:
                return 2;
            default:
                return 0;
        }
    }

    static int fxKeyEventTypeToKeyID(KeyEvent event) {
        EventType<?> eventType = event.getEventType();
        if (eventType == KeyEvent.KEY_PRESSED) {
            return 401;
        }
        if (eventType == KeyEvent.KEY_RELEASED) {
            return 402;
        }
        if (eventType == KeyEvent.KEY_TYPED) {
            return 400;
        }
        throw new RuntimeException("Unknown KeyEvent type: " + ((Object) eventType));
    }

    static int fxKeyModsToKeyMods(KeyEvent event) {
        int mods = 0;
        if (event.isAltDown()) {
            mods = 0 | 512;
        }
        if (event.isControlDown()) {
            mods |= 128;
        }
        if (event.isMetaDown()) {
            mods |= 256;
        }
        if (event.isShiftDown()) {
            mods |= 64;
        }
        return mods;
    }

    static int fxScrollModsToMouseWheelMods(ScrollEvent event) {
        int mods = 0;
        if (event.isAltDown()) {
            mods = 0 | 512;
        }
        if (event.isControlDown()) {
            mods |= 128;
        }
        if (event.isMetaDown()) {
            mods |= 256;
        }
        if (event.isShiftDown()) {
            mods |= 64;
        }
        return mods;
    }
}
