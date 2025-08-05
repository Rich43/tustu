package com.sun.javafx.scene.input;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.input.KeyCode;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/input/KeyCodeMap.class */
public final class KeyCodeMap {
    private static final Map<Integer, KeyCode> charMap = new HashMap(KeyCode.values().length);

    KeyCodeMap() {
    }

    static {
        for (KeyCode c2 : KeyCode.values()) {
            charMap.put(Integer.valueOf(c2.impl_getCode()), c2);
        }
    }

    public static KeyCode valueOf(int code) {
        return charMap.get(Integer.valueOf(code));
    }
}
