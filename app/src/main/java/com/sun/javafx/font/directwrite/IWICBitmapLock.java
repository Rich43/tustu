package com.sun.javafx.font.directwrite;

/* loaded from: jfxrt.jar:com/sun/javafx/font/directwrite/IWICBitmapLock.class */
class IWICBitmapLock extends IUnknown {
    IWICBitmapLock(long ptr) {
        super(ptr);
    }

    byte[] GetDataPointer() {
        return OS.GetDataPointer(this.ptr);
    }

    int GetStride() {
        return OS.GetStride(this.ptr);
    }
}
