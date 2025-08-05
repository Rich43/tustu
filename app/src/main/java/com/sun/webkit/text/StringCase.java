package com.sun.webkit.text;

import java.util.Locale;

/* loaded from: jfxrt.jar:com/sun/webkit/text/StringCase.class */
final class StringCase {
    StringCase() {
    }

    private static String toLowerCase(String src) {
        return src.toLowerCase(Locale.ROOT);
    }

    private static String toUpperCase(String src) {
        return src.toUpperCase(Locale.ROOT);
    }

    private static String foldCase(String src) {
        return src.toUpperCase(Locale.ROOT).toLowerCase(Locale.ROOT);
    }
}
