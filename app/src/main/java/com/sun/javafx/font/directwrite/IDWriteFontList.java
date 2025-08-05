package com.sun.javafx.font.directwrite;

/* loaded from: jfxrt.jar:com/sun/javafx/font/directwrite/IDWriteFontList.class */
class IDWriteFontList extends IUnknown {
    IDWriteFontList(long ptr) {
        super(ptr);
    }

    int GetFontCount() {
        return OS.GetFontCount(this.ptr);
    }

    IDWriteFont GetFont(int index) {
        long result = OS.GetFont(this.ptr, index);
        if (result != 0) {
            return new IDWriteFont(result);
        }
        return null;
    }
}
