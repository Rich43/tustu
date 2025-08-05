package com.sun.javafx.font;

import com.sun.glass.utils.NativeLibLoader;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/* loaded from: jfxrt.jar:com/sun/javafx/font/MacFontFinder.class */
class MacFontFinder {
    private static final int SystemFontType = 2;
    private static final int MonospacedFontType = 1;

    private static native String getFont(int i2);

    static native float getSystemFontSize();

    private static native String[] getFontData();

    MacFontFinder() {
    }

    static {
        AccessController.doPrivileged(() -> {
            NativeLibLoader.loadLibrary("javafx_font");
            return null;
        });
    }

    public static String getSystemFont() {
        return getFont(2);
    }

    public static String getMonospacedFont() {
        return getFont(1);
    }

    public static boolean populateFontFileNameMap(HashMap<String, String> fontToFileMap, HashMap<String, String> fontToFamilyNameMap, HashMap<String, ArrayList<String>> familyToFontListMap, Locale locale) {
        if (fontToFileMap == null || fontToFamilyNameMap == null || familyToFontListMap == null) {
            return false;
        }
        if (locale == null) {
            locale = Locale.ENGLISH;
        }
        String[] fontData = getFontData();
        if (fontData == null) {
            return false;
        }
        int i2 = 0;
        while (i2 < fontData.length) {
            int i3 = i2;
            int i4 = i2 + 1;
            String name = fontData[i3];
            int i5 = i4 + 1;
            String family = fontData[i4];
            i2 = i5 + 1;
            String file = fontData[i5];
            if (PrismFontFactory.debugFonts) {
                System.err.println("[MacFontFinder] Name=" + name);
                System.err.println("\tFamily=" + family);
                System.err.println("\tFile=" + file);
            }
            String lcName = name.toLowerCase(locale);
            String lcFamily = family.toLowerCase(locale);
            fontToFileMap.put(lcName, file);
            fontToFamilyNameMap.put(lcName, family);
            ArrayList<String> list = familyToFontListMap.get(lcFamily);
            if (list == null) {
                list = new ArrayList<>();
                familyToFontListMap.put(lcFamily, list);
            }
            list.add(name);
        }
        return true;
    }
}
