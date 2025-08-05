package com.sun.javafx.font.directwrite;

/* loaded from: jfxrt.jar:com/sun/javafx/font/directwrite/IDWriteFontCollection.class */
class IDWriteFontCollection extends IUnknown {
    IDWriteFontCollection(long ptr) {
        super(ptr);
    }

    int GetFontFamilyCount() {
        return OS.GetFontFamilyCount(this.ptr);
    }

    IDWriteFontFamily GetFontFamily(int index) {
        long result = OS.GetFontFamily(this.ptr, index);
        if (result != 0) {
            return new IDWriteFontFamily(result);
        }
        return null;
    }

    int FindFamilyName(String familyName) {
        return OS.FindFamilyName(this.ptr, (familyName + (char) 0).toCharArray());
    }

    IDWriteFont GetFontFromFontFace(IDWriteFontFace fontface) {
        long result = OS.GetFontFromFontFace(this.ptr, fontface.ptr);
        if (result != 0) {
            return new IDWriteFont(result);
        }
        return null;
    }
}
