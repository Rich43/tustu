package com.sun.javafx.font.directwrite;

/* loaded from: jfxrt.jar:com/sun/javafx/font/directwrite/IWICBitmap.class */
class IWICBitmap extends IUnknown {
    IWICBitmap(long ptr) {
        super(ptr);
    }

    IWICBitmapLock Lock(int x2, int y2, int width, int height, int flags) {
        long result = OS.Lock(this.ptr, x2, y2, width, height, flags);
        if (result != 0) {
            return new IWICBitmapLock(result);
        }
        return null;
    }
}
