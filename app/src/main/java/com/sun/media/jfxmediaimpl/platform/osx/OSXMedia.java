package com.sun.media.jfxmediaimpl.platform.osx;

import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmediaimpl.NativeMedia;
import com.sun.media.jfxmediaimpl.platform.Platform;

/* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/platform/osx/OSXMedia.class */
final class OSXMedia extends NativeMedia {
    OSXMedia(Locator source) {
        super(source);
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMedia
    public Platform getPlatform() {
        return OSXPlatform.getPlatformInstance();
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMedia
    public void dispose() {
    }
}
