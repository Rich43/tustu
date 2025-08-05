package com.sun.javafx.webkit;

import com.sun.glass.ui.Platform;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.input.KeyCode;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/KeyCodeMap.class */
public final class KeyCodeMap {
    private static final Map<KeyCode, Entry> MAP;

    /* loaded from: jfxrt.jar:com/sun/javafx/webkit/KeyCodeMap$Entry.class */
    public static final class Entry {
        private final int windowsVirtualKeyCode;
        private final String keyIdentifier;

        private Entry(int windowsVirtualKeyCode, String keyIdentifier) {
            this.windowsVirtualKeyCode = windowsVirtualKeyCode;
            this.keyIdentifier = keyIdentifier;
        }

        public int getWindowsVirtualKeyCode() {
            return this.windowsVirtualKeyCode;
        }

        public String getKeyIdentifier() {
            return this.keyIdentifier;
        }
    }

    static {
        Map<KeyCode, Entry> map = new HashMap<>();
        put(map, KeyCode.ENTER, 13, "Enter");
        put(map, KeyCode.BACK_SPACE, 8);
        put(map, KeyCode.TAB, 9);
        put(map, KeyCode.CANCEL, 3);
        put(map, KeyCode.CLEAR, 12, "Clear");
        put(map, KeyCode.SHIFT, 16, "Shift");
        put(map, KeyCode.CONTROL, 17, "Control");
        put(map, KeyCode.ALT, 18, "Alt");
        put(map, KeyCode.PAUSE, 19, "Pause");
        put(map, KeyCode.CAPS, 20, "CapsLock");
        put(map, KeyCode.ESCAPE, 27);
        put(map, KeyCode.SPACE, 32);
        put(map, KeyCode.PAGE_UP, 33, "PageUp");
        put(map, KeyCode.PAGE_DOWN, 34, "PageDown");
        put(map, KeyCode.END, 35, "End");
        put(map, KeyCode.HOME, 36, "Home");
        put(map, KeyCode.LEFT, 37, "Left");
        put(map, KeyCode.UP, 38, "Up");
        put(map, KeyCode.RIGHT, 39, "Right");
        put(map, KeyCode.DOWN, 40, "Down");
        put(map, KeyCode.COMMA, 188);
        put(map, KeyCode.MINUS, 189);
        put(map, KeyCode.PERIOD, 190);
        put(map, KeyCode.SLASH, 191);
        put(map, KeyCode.DIGIT0, 48);
        put(map, KeyCode.DIGIT1, 49);
        put(map, KeyCode.DIGIT2, 50);
        put(map, KeyCode.DIGIT3, 51);
        put(map, KeyCode.DIGIT4, 52);
        put(map, KeyCode.DIGIT5, 53);
        put(map, KeyCode.DIGIT6, 54);
        put(map, KeyCode.DIGIT7, 55);
        put(map, KeyCode.DIGIT8, 56);
        put(map, KeyCode.DIGIT9, 57);
        put(map, KeyCode.SEMICOLON, 186);
        put(map, KeyCode.EQUALS, 187);
        put(map, KeyCode.A, 65);
        put(map, KeyCode.B, 66);
        put(map, KeyCode.C, 67);
        put(map, KeyCode.D, 68);
        put(map, KeyCode.E, 69);
        put(map, KeyCode.F, 70);
        put(map, KeyCode.G, 71);
        put(map, KeyCode.H, 72);
        put(map, KeyCode.I, 73);
        put(map, KeyCode.J, 74);
        put(map, KeyCode.K, 75);
        put(map, KeyCode.L, 76);
        put(map, KeyCode.M, 77);
        put(map, KeyCode.N, 78);
        put(map, KeyCode.O, 79);
        put(map, KeyCode.P, 80);
        put(map, KeyCode.Q, 81);
        put(map, KeyCode.R, 82);
        put(map, KeyCode.S, 83);
        put(map, KeyCode.T, 84);
        put(map, KeyCode.U, 85);
        put(map, KeyCode.V, 86);
        put(map, KeyCode.W, 87);
        put(map, KeyCode.X, 88);
        put(map, KeyCode.Y, 89);
        put(map, KeyCode.Z, 90);
        put(map, KeyCode.OPEN_BRACKET, 219);
        put(map, KeyCode.BACK_SLASH, 220);
        put(map, KeyCode.CLOSE_BRACKET, 221);
        put(map, KeyCode.NUMPAD0, 96);
        put(map, KeyCode.NUMPAD1, 97);
        put(map, KeyCode.NUMPAD2, 98);
        put(map, KeyCode.NUMPAD3, 99);
        put(map, KeyCode.NUMPAD4, 100);
        put(map, KeyCode.NUMPAD5, 101);
        put(map, KeyCode.NUMPAD6, 102);
        put(map, KeyCode.NUMPAD7, 103);
        put(map, KeyCode.NUMPAD8, 104);
        put(map, KeyCode.NUMPAD9, 105);
        put(map, KeyCode.MULTIPLY, 106);
        put(map, KeyCode.ADD, 107);
        put(map, KeyCode.SEPARATOR, 108);
        put(map, KeyCode.SUBTRACT, 109);
        put(map, KeyCode.DECIMAL, 110);
        put(map, KeyCode.DIVIDE, 111);
        put(map, KeyCode.DELETE, 46, "U+007F");
        put(map, KeyCode.NUM_LOCK, 144);
        put(map, KeyCode.SCROLL_LOCK, 145, "Scroll");
        put(map, KeyCode.F1, 112, "F1");
        put(map, KeyCode.F2, 113, "F2");
        put(map, KeyCode.F3, 114, "F3");
        put(map, KeyCode.F4, 115, "F4");
        put(map, KeyCode.F5, 116, "F5");
        put(map, KeyCode.F6, 117, "F6");
        put(map, KeyCode.F7, 118, "F7");
        put(map, KeyCode.F8, 119, "F8");
        put(map, KeyCode.F9, 120, "F9");
        put(map, KeyCode.F10, 121, "F10");
        put(map, KeyCode.F11, 122, "F11");
        put(map, KeyCode.F12, 123, "F12");
        put(map, KeyCode.F13, 124, "F13");
        put(map, KeyCode.F14, 125, "F14");
        put(map, KeyCode.F15, 126, "F15");
        put(map, KeyCode.F16, 127, "F16");
        put(map, KeyCode.F17, 128, "F17");
        put(map, KeyCode.F18, 129, "F18");
        put(map, KeyCode.F19, 130, "F19");
        put(map, KeyCode.F20, 131, "F20");
        put(map, KeyCode.F21, 132, "F21");
        put(map, KeyCode.F22, 133, "F22");
        put(map, KeyCode.F23, 134, "F23");
        put(map, KeyCode.F24, 135, "F24");
        put(map, KeyCode.PRINTSCREEN, 44, "PrintScreen");
        put(map, KeyCode.INSERT, 45, "Insert");
        put(map, KeyCode.HELP, 47, "Help");
        put(map, KeyCode.META, 0, "Meta");
        put(map, KeyCode.BACK_QUOTE, 192);
        put(map, KeyCode.QUOTE, 222);
        put(map, KeyCode.KP_UP, 38, "Up");
        put(map, KeyCode.KP_DOWN, 40, "Down");
        put(map, KeyCode.KP_LEFT, 37, "Left");
        put(map, KeyCode.KP_RIGHT, 39, "Right");
        put(map, KeyCode.AMPERSAND, 55);
        put(map, KeyCode.ASTERISK, 56);
        put(map, KeyCode.QUOTEDBL, 222);
        put(map, KeyCode.LESS, 188);
        put(map, KeyCode.GREATER, 190);
        put(map, KeyCode.BRACELEFT, 219);
        put(map, KeyCode.BRACERIGHT, 221);
        put(map, KeyCode.AT, 50);
        put(map, KeyCode.COLON, 186);
        put(map, KeyCode.CIRCUMFLEX, 54);
        put(map, KeyCode.DOLLAR, 52);
        put(map, KeyCode.EXCLAMATION_MARK, 49);
        put(map, KeyCode.LEFT_PARENTHESIS, 57);
        put(map, KeyCode.NUMBER_SIGN, 51);
        put(map, KeyCode.PLUS, 187);
        put(map, KeyCode.RIGHT_PARENTHESIS, 48);
        put(map, KeyCode.UNDERSCORE, 189);
        put(map, KeyCode.WINDOWS, 91, Platform.WINDOWS);
        put(map, KeyCode.CONTEXT_MENU, 93);
        put(map, KeyCode.FINAL, 24);
        put(map, KeyCode.CONVERT, 28);
        put(map, KeyCode.NONCONVERT, 29);
        put(map, KeyCode.ACCEPT, 30);
        put(map, KeyCode.MODECHANGE, 31);
        put(map, KeyCode.KANA, 21);
        put(map, KeyCode.KANJI, 25);
        put(map, KeyCode.ALT_GRAPH, 165);
        put(map, KeyCode.PLAY, 250);
        put(map, KeyCode.TRACK_PREV, 177);
        put(map, KeyCode.TRACK_NEXT, 176);
        put(map, KeyCode.VOLUME_UP, 175);
        put(map, KeyCode.VOLUME_DOWN, 174);
        put(map, KeyCode.MUTE, 173);
        MAP = Collections.unmodifiableMap(map);
    }

    private static void put(Map<KeyCode, Entry> map, KeyCode keyCode, int windowsVirtualKeyCode, String keyIdentifier) {
        map.put(keyCode, new Entry(windowsVirtualKeyCode, keyIdentifier));
    }

    private static void put(Map<KeyCode, Entry> map, KeyCode keyCode, int windowsVirtualKeyCode) {
        put(map, keyCode, windowsVirtualKeyCode, null);
    }

    public static Entry lookup(KeyCode keyCode) {
        Entry entry = MAP.get(keyCode);
        if (entry == null || entry.getKeyIdentifier() == null) {
            int windowsVirtualKeyCode = entry != null ? entry.getWindowsVirtualKeyCode() : 0;
            String keyIdentifier = String.format("U+%04X", Integer.valueOf(windowsVirtualKeyCode));
            entry = new Entry(windowsVirtualKeyCode, keyIdentifier);
        }
        return entry;
    }
}
