package com.sun.media.jfxmediaimpl.platform.gstreamer;

import com.sun.media.jfxmedia.MediaError;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmediaimpl.MediaUtils;
import com.sun.media.jfxmediaimpl.NativeMedia;
import com.sun.media.jfxmediaimpl.platform.Platform;

/* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/platform/gstreamer/GSTMedia.class */
final class GSTMedia extends NativeMedia {
    private final Object markerMutex;
    protected long refNativeMedia;

    private native int gstInitNativeMedia(Locator locator, String str, long j2, long[] jArr);

    private native void gstDispose(long j2);

    GSTMedia(Locator locator) {
        super(locator);
        this.markerMutex = new Object();
        init();
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMedia
    public Platform getPlatform() {
        return GSTPlatform.getPlatformInstance();
    }

    private void init() {
        long[] nativeMediaHandle = new long[1];
        Locator loc = getLocator();
        MediaError ret = MediaError.getFromCode(gstInitNativeMedia(loc, loc.getContentType(), loc.getContentLength(), nativeMediaHandle));
        if (ret != MediaError.ERROR_NONE && ret != MediaError.ERROR_PLATFORM_UNSUPPORTED) {
            MediaUtils.nativeError(this, ret);
        }
        this.refNativeMedia = nativeMediaHandle[0];
    }

    long getNativeMediaRef() {
        return this.refNativeMedia;
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMedia
    public synchronized void dispose() {
        if (0 != this.refNativeMedia) {
            gstDispose(this.refNativeMedia);
            this.refNativeMedia = 0L;
        }
    }
}
