package com.sun.javafx.font.directwrite;

/* loaded from: jfxrt.jar:com/sun/javafx/font/directwrite/IWICImagingFactory.class */
class IWICImagingFactory extends IUnknown {
    IWICImagingFactory(long ptr) {
        super(ptr);
    }

    IWICBitmap CreateBitmap(int uiWidth, int uiHeight, int pixelFormat, int options) {
        long result = OS.CreateBitmap(this.ptr, uiWidth, uiHeight, pixelFormat, options);
        if (result != 0) {
            return new IWICBitmap(result);
        }
        return null;
    }
}
