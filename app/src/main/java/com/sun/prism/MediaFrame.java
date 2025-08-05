package com.sun.prism;

import java.nio.ByteBuffer;

/* loaded from: jfxrt.jar:com/sun/prism/MediaFrame.class */
public interface MediaFrame {
    ByteBuffer getBufferForPlane(int i2);

    PixelFormat getPixelFormat();

    int getWidth();

    int getHeight();

    int getEncodedWidth();

    int getEncodedHeight();

    int planeCount();

    int[] planeStrides();

    int strideForPlane(int i2);

    MediaFrame convertToFormat(PixelFormat pixelFormat);

    void holdFrame();

    void releaseFrame();
}
