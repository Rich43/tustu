package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.control.VideoDataBuffer;
import com.sun.media.jfxmedia.control.VideoFormat;
import com.sun.media.jfxmediaimpl.MediaDisposer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/NativeVideoBuffer.class */
final class NativeVideoBuffer implements VideoDataBuffer {
    private long nativePeer;
    private final AtomicInteger holdCount = new AtomicInteger(1);
    private NativeVideoBuffer cachedBGRARep;
    private static final boolean DEBUG_DISPOSED_BUFFERS = false;
    private static final VideoBufferDisposer disposer = new VideoBufferDisposer();

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativeDisposeBuffer(long j2);

    private native double nativeGetTimestamp(long j2);

    private native ByteBuffer nativeGetBufferForPlane(long j2, int i2);

    private native int nativeGetWidth(long j2);

    private native int nativeGetHeight(long j2);

    private native int nativeGetEncodedWidth(long j2);

    private native int nativeGetEncodedHeight(long j2);

    private native int nativeGetFormat(long j2);

    private native boolean nativeHasAlpha(long j2);

    private native int nativeGetPlaneCount(long j2);

    private native int[] nativeGetPlaneStrides(long j2);

    private native long nativeConvertToFormat(long j2, int i2);

    private native void nativeSetDirty(long j2);

    public static NativeVideoBuffer createVideoBuffer(long nativePeer) {
        NativeVideoBuffer buffer = new NativeVideoBuffer(nativePeer);
        MediaDisposer.addResourceDisposer(buffer, Long.valueOf(nativePeer), disposer);
        return buffer;
    }

    private NativeVideoBuffer(long nativePeer) {
        this.nativePeer = nativePeer;
    }

    @Override // com.sun.media.jfxmedia.control.VideoDataBuffer
    public void holdFrame() {
        if (0 != this.nativePeer) {
            this.holdCount.incrementAndGet();
        }
    }

    @Override // com.sun.media.jfxmedia.control.VideoDataBuffer
    public void releaseFrame() {
        if (0 != this.nativePeer && this.holdCount.decrementAndGet() <= 0) {
            if (null != this.cachedBGRARep) {
                this.cachedBGRARep.releaseFrame();
                this.cachedBGRARep = null;
            }
            MediaDisposer.removeResourceDisposer(Long.valueOf(this.nativePeer));
            nativeDisposeBuffer(this.nativePeer);
            this.nativePeer = 0L;
        }
    }

    @Override // com.sun.media.jfxmedia.control.VideoDataBuffer
    public double getTimestamp() {
        if (0 != this.nativePeer) {
            return nativeGetTimestamp(this.nativePeer);
        }
        return 0.0d;
    }

    @Override // com.sun.media.jfxmedia.control.VideoDataBuffer
    public ByteBuffer getBufferForPlane(int plane) {
        if (0 != this.nativePeer) {
            ByteBuffer buffer = nativeGetBufferForPlane(this.nativePeer, plane);
            buffer.order(ByteOrder.nativeOrder());
            return buffer;
        }
        return null;
    }

    @Override // com.sun.media.jfxmedia.control.VideoDataBuffer
    public int getWidth() {
        if (0 != this.nativePeer) {
            return nativeGetWidth(this.nativePeer);
        }
        return 0;
    }

    @Override // com.sun.media.jfxmedia.control.VideoDataBuffer
    public int getHeight() {
        if (0 != this.nativePeer) {
            return nativeGetHeight(this.nativePeer);
        }
        return 0;
    }

    @Override // com.sun.media.jfxmedia.control.VideoDataBuffer
    public int getEncodedWidth() {
        if (0 != this.nativePeer) {
            return nativeGetEncodedWidth(this.nativePeer);
        }
        return 0;
    }

    @Override // com.sun.media.jfxmedia.control.VideoDataBuffer
    public int getEncodedHeight() {
        if (0 != this.nativePeer) {
            return nativeGetEncodedHeight(this.nativePeer);
        }
        return 0;
    }

    @Override // com.sun.media.jfxmedia.control.VideoDataBuffer
    public VideoFormat getFormat() {
        if (0 != this.nativePeer) {
            int formatType = nativeGetFormat(this.nativePeer);
            return VideoFormat.formatForType(formatType);
        }
        return null;
    }

    @Override // com.sun.media.jfxmedia.control.VideoDataBuffer
    public boolean hasAlpha() {
        if (0 != this.nativePeer) {
            return nativeHasAlpha(this.nativePeer);
        }
        return false;
    }

    @Override // com.sun.media.jfxmedia.control.VideoDataBuffer
    public int getPlaneCount() {
        if (0 != this.nativePeer) {
            return nativeGetPlaneCount(this.nativePeer);
        }
        return 0;
    }

    @Override // com.sun.media.jfxmedia.control.VideoDataBuffer
    public int getStrideForPlane(int planeIndex) {
        if (0 != this.nativePeer) {
            int[] strides = nativeGetPlaneStrides(this.nativePeer);
            return strides[planeIndex];
        }
        return 0;
    }

    @Override // com.sun.media.jfxmedia.control.VideoDataBuffer
    public int[] getPlaneStrides() {
        if (0 != this.nativePeer) {
            return nativeGetPlaneStrides(this.nativePeer);
        }
        return null;
    }

    @Override // com.sun.media.jfxmedia.control.VideoDataBuffer
    public VideoDataBuffer convertToFormat(VideoFormat newFormat) {
        if (0 != this.nativePeer) {
            if (newFormat == VideoFormat.BGRA_PRE && null != this.cachedBGRARep) {
                this.cachedBGRARep.holdFrame();
                return this.cachedBGRARep;
            }
            long newFrame = nativeConvertToFormat(this.nativePeer, newFormat.getNativeType());
            if (0 != newFrame) {
                NativeVideoBuffer frame = createVideoBuffer(newFrame);
                if (newFormat == VideoFormat.BGRA_PRE) {
                    frame.holdFrame();
                    this.cachedBGRARep = frame;
                }
                return frame;
            }
            throw new UnsupportedOperationException("Conversion from " + ((Object) getFormat()) + " to " + ((Object) newFormat) + " is not supported.");
        }
        return null;
    }

    @Override // com.sun.media.jfxmedia.control.VideoDataBuffer
    public void setDirty() {
        if (0 != this.nativePeer) {
            nativeSetDirty(this.nativePeer);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/NativeVideoBuffer$VideoBufferDisposer.class */
    private static class VideoBufferDisposer implements MediaDisposer.ResourceDisposer {
        private VideoBufferDisposer() {
        }

        @Override // com.sun.media.jfxmediaimpl.MediaDisposer.ResourceDisposer
        public void disposeResource(Object resource) {
            if (resource instanceof Long) {
                NativeVideoBuffer.nativeDisposeBuffer(((Long) resource).longValue());
            }
        }
    }

    public String toString() {
        return "[NativeVideoBuffer peer=" + Long.toHexString(this.nativePeer) + ", format=" + ((Object) getFormat()) + ", size=(" + getWidth() + "," + getHeight() + "), timestamp=" + getTimestamp() + "]";
    }
}
