package com.sun.javafx.robot;

import java.nio.Buffer;
import java.nio.IntBuffer;

/* loaded from: jfxrt.jar:com/sun/javafx/robot/FXRobotImage.class */
public class FXRobotImage {
    private final IntBuffer pixelBuffer;
    private final int width;
    private final int height;
    private final int scanlineStride;

    public static FXRobotImage create(Buffer pixelBuffer, int width, int height, int scanlineStride) {
        return new FXRobotImage(pixelBuffer, width, height, scanlineStride);
    }

    private FXRobotImage(Buffer pixelBuffer, int width, int height, int scanlineStride) {
        if (pixelBuffer == null) {
            throw new IllegalArgumentException("Pixel buffer must be non-null");
        }
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Image dimensions must be > 0");
        }
        this.pixelBuffer = (IntBuffer) pixelBuffer;
        this.width = width;
        this.height = height;
        this.scanlineStride = scanlineStride;
    }

    public Buffer getPixelBuffer() {
        return this.pixelBuffer;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getScanlineStride() {
        return this.scanlineStride;
    }

    public int getPixelStride() {
        return 4;
    }

    public int getArgbPre(int x2, int y2) {
        if (x2 < 0 || x2 >= this.width || y2 < 0 || y2 >= this.height) {
            throw new IllegalArgumentException("x,y must be >0, <width, height");
        }
        return this.pixelBuffer.get(x2 + ((y2 * this.scanlineStride) / 4));
    }

    public int getArgb(int x2, int y2) {
        if (x2 < 0 || x2 >= this.width || y2 < 0 || y2 >= this.height) {
            throw new IllegalArgumentException("x,y must be >0, <width, height");
        }
        int argb = this.pixelBuffer.get(x2 + ((y2 * this.scanlineStride) / 4));
        if ((argb >> 24) == -1) {
            return argb;
        }
        int a2 = argb >>> 24;
        int r2 = (argb >> 16) & 255;
        int g2 = (argb >> 8) & 255;
        int b2 = argb & 255;
        int a22 = a2 + (a2 >> 7);
        int r3 = (r2 * a22) >> 8;
        int g3 = (g2 * a22) >> 8;
        return (a2 << 24) | (r3 << 16) | (g3 << 8) | ((b2 * a22) >> 8);
    }

    public String toString() {
        return super.toString() + " [format=INT_ARGB_PRE width=" + this.width + " height=" + this.height + " scanlineStride=" + this.scanlineStride + " pixelStride=" + getPixelStride() + " pixelBuffer=" + ((Object) this.pixelBuffer) + "]";
    }
}
