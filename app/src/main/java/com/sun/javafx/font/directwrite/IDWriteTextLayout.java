package com.sun.javafx.font.directwrite;

/* loaded from: jfxrt.jar:com/sun/javafx/font/directwrite/IDWriteTextLayout.class */
class IDWriteTextLayout extends IUnknown {
    IDWriteTextLayout(long ptr) {
        super(ptr);
    }

    int Draw(long clientData, JFXTextRenderer renderer, float x2, float y2) {
        return OS.Draw(this.ptr, clientData, renderer.ptr, x2, y2);
    }
}
