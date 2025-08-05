package com.sun.media.jfxmedia.track;

/* loaded from: jfxrt.jar:com/sun/media/jfxmedia/track/VideoResolution.class */
public class VideoResolution {
    public int width;
    public int height;

    public VideoResolution(int width, int height) {
        if (width <= 0) {
            throw new IllegalArgumentException("width <= 0");
        }
        if (height <= 0) {
            throw new IllegalArgumentException("height <= 0");
        }
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public String toString() {
        return "VideoResolution {width: " + this.width + " height: " + this.height + "}";
    }
}
