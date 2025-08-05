package com.sun.webkit.graphics;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

/* loaded from: jfxrt.jar:com/sun/webkit/graphics/WCImage.class */
public abstract class WCImage extends Ref {
    private WCRenderQueue rq;
    private String fileExtension;

    public abstract int getWidth();

    public abstract int getHeight();

    protected abstract byte[] toData(String str);

    protected abstract String toDataURL(String str);

    public abstract float getPixelScale();

    public abstract BufferedImage toBufferedImage();

    public String getFileExtension() {
        return this.fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public Object getPlatformImage() {
        return null;
    }

    public ByteBuffer getPixelBuffer() {
        return null;
    }

    protected void drawPixelBuffer() {
    }

    public synchronized void setRQ(WCRenderQueue rq) {
        this.rq = rq;
    }

    protected synchronized void flushRQ() {
        if (this.rq != null) {
            this.rq.decode();
        }
    }

    protected synchronized boolean isDirty() {
        return (this.rq == null || this.rq.isEmpty()) ? false : true;
    }

    public static WCImage getImage(Object imgFrame) {
        WCImage img = null;
        if (imgFrame instanceof WCImage) {
            img = (WCImage) imgFrame;
        } else if (imgFrame instanceof WCImageFrame) {
            img = ((WCImageFrame) imgFrame).getFrame();
        }
        return img;
    }

    public boolean isNull() {
        return getWidth() <= 0 || getHeight() <= 0 || getPlatformImage() == null;
    }
}
