package com.sun.media.jfxmediaimpl.platform.ios;

import com.sun.media.jfxmedia.MediaError;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmediaimpl.MediaUtils;
import com.sun.media.jfxmediaimpl.NativeMedia;
import com.sun.media.jfxmediaimpl.platform.Platform;

/* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/platform/ios/IOSMedia.class */
final class IOSMedia extends NativeMedia {
    private long refNativeMedia;

    private native int iosInitNativeMedia(Locator locator, String str, long j2, long[] jArr);

    private native void iosDispose(long j2);

    IOSMedia(Locator locator) {
        super(locator);
        init();
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMedia
    public Platform getPlatform() {
        return IOSPlatform.getPlatformInstance();
    }

    private void init() {
        long[] nativeMediaHandle = new long[1];
        Locator loc = getLocator();
        MediaError err = MediaError.getFromCode(iosInitNativeMedia(loc, loc.getContentType(), loc.getContentLength(), nativeMediaHandle));
        if (err != MediaError.ERROR_NONE) {
            MediaUtils.nativeError(this, err);
        }
        this.refNativeMedia = nativeMediaHandle[0];
    }

    long getNativeMediaRef() {
        return this.refNativeMedia;
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMedia
    public synchronized void dispose() {
        if (0 != this.refNativeMedia) {
            iosDispose(this.refNativeMedia);
            this.refNativeMedia = 0L;
        }
    }
}
