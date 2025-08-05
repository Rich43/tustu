package com.sun.javafx.font.directwrite;

/* loaded from: jfxrt.jar:com/sun/javafx/font/directwrite/IDWriteLocalizedStrings.class */
class IDWriteLocalizedStrings extends IUnknown {
    IDWriteLocalizedStrings(long ptr) {
        super(ptr);
    }

    int FindLocaleName(String locale) {
        return OS.FindLocaleName(this.ptr, (locale + (char) 0).toCharArray());
    }

    int GetStringLength(int index) {
        return OS.GetStringLength(this.ptr, index);
    }

    String GetString(int index, int size) {
        char[] buffer = OS.GetString(this.ptr, index, size + 1);
        if (buffer != null) {
            return new String(buffer, 0, size);
        }
        return null;
    }
}
