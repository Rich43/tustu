package com.sun.javafx.font.directwrite;

/* loaded from: jfxrt.jar:com/sun/javafx/font/directwrite/IDWriteFont.class */
class IDWriteFont extends IUnknown {
    IDWriteFont(long ptr) {
        super(ptr);
    }

    IDWriteFontFace CreateFontFace() {
        long result = OS.CreateFontFace(this.ptr);
        if (result != 0) {
            return new IDWriteFontFace(result);
        }
        return null;
    }

    IDWriteLocalizedStrings GetFaceNames() {
        long result = OS.GetFaceNames(this.ptr);
        if (result != 0) {
            return new IDWriteLocalizedStrings(result);
        }
        return null;
    }

    IDWriteFontFamily GetFontFamily() {
        long result = OS.GetFontFamily(this.ptr);
        if (result != 0) {
            return new IDWriteFontFamily(result);
        }
        return null;
    }

    IDWriteLocalizedStrings GetInformationalStrings(int informationalStringID) {
        long result = OS.GetInformationalStrings(this.ptr, informationalStringID);
        if (result != 0) {
            return new IDWriteLocalizedStrings(result);
        }
        return null;
    }

    int GetSimulations() {
        return OS.GetSimulations(this.ptr);
    }

    int GetStretch() {
        return OS.GetStretch(this.ptr);
    }

    int GetStyle() {
        return OS.GetStyle(this.ptr);
    }

    int GetWeight() {
        return OS.GetWeight(this.ptr);
    }
}
