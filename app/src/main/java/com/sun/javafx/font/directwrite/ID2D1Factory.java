package com.sun.javafx.font.directwrite;

/* loaded from: jfxrt.jar:com/sun/javafx/font/directwrite/ID2D1Factory.class */
class ID2D1Factory extends IUnknown {
    ID2D1Factory(long ptr) {
        super(ptr);
    }

    ID2D1RenderTarget CreateWicBitmapRenderTarget(IWICBitmap target, D2D1_RENDER_TARGET_PROPERTIES renderTargetProperties) {
        long result = OS.CreateWicBitmapRenderTarget(this.ptr, target.ptr, renderTargetProperties);
        if (result != 0) {
            return new ID2D1RenderTarget(result);
        }
        return null;
    }
}
