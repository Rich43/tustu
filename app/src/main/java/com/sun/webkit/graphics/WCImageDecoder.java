package com.sun.webkit.graphics;

/* loaded from: jfxrt.jar:com/sun/webkit/graphics/WCImageDecoder.class */
public abstract class WCImageDecoder {
    protected abstract void addImageData(byte[] bArr);

    protected abstract int[] getImageSize();

    protected abstract int getFrameCount();

    protected abstract WCImageFrame getFrame(int i2);

    protected abstract int getFrameDuration(int i2);

    protected abstract int[] getFrameSize(int i2);

    protected abstract boolean getFrameCompleteStatus(int i2);

    protected abstract void loadFromResource(String str);

    protected abstract void destroy();

    protected abstract String getFilenameExtension();
}
