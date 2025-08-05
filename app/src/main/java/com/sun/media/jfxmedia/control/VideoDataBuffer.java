package com.sun.media.jfxmedia.control;

import java.nio.ByteBuffer;

/* loaded from: jfxrt.jar:com/sun/media/jfxmedia/control/VideoDataBuffer.class */
public interface VideoDataBuffer {
    public static final int PACKED_FORMAT_PLANE = 0;
    public static final int YCBCR_PLANE_LUMA = 0;
    public static final int YCBCR_PLANE_CR = 1;
    public static final int YCBCR_PLANE_CB = 2;
    public static final int YCBCR_PLANE_ALPHA = 3;

    ByteBuffer getBufferForPlane(int i2);

    double getTimestamp();

    int getWidth();

    int getHeight();

    int getEncodedWidth();

    int getEncodedHeight();

    VideoFormat getFormat();

    boolean hasAlpha();

    int getPlaneCount();

    int getStrideForPlane(int i2);

    int[] getPlaneStrides();

    VideoDataBuffer convertToFormat(VideoFormat videoFormat);

    void setDirty();

    void holdFrame();

    void releaseFrame();
}
