package com.sun.javafx.font.directwrite;

/* loaded from: jfxrt.jar:com/sun/javafx/font/directwrite/IDWriteFontFamily.class */
class IDWriteFontFamily extends IDWriteFontList {
    IDWriteFontFamily(long ptr) {
        super(ptr);
    }

    IDWriteLocalizedStrings GetFamilyNames() {
        long result = OS.GetFamilyNames(this.ptr);
        if (result != 0) {
            return new IDWriteLocalizedStrings(result);
        }
        return null;
    }

    IDWriteFont GetFirstMatchingFont(int weight, int stretch, int style) {
        long result = OS.GetFirstMatchingFont(this.ptr, weight, stretch, style);
        if (result != 0) {
            return new IDWriteFont(result);
        }
        return null;
    }
}
