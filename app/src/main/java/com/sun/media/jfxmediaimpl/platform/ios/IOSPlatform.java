package com.sun.media.jfxmediaimpl.platform.ios;

import com.sun.media.jfxmedia.Media;
import com.sun.media.jfxmedia.MediaPlayer;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmediaimpl.HostUtils;
import com.sun.media.jfxmediaimpl.MediaUtils;
import com.sun.media.jfxmediaimpl.platform.Platform;
import java.util.Arrays;

/* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/platform/ios/IOSPlatform.class */
public final class IOSPlatform extends Platform {
    private static final String[] CONTENT_TYPES = {MediaUtils.CONTENT_TYPE_MP4, MediaUtils.CONTENT_TYPE_M4A, MediaUtils.CONTENT_TYPE_M4V, MediaUtils.CONTENT_TYPE_M3U8, MediaUtils.CONTENT_TYPE_M3U, MediaUtils.CONTENT_TYPE_MPA, MediaUtils.CONTENT_TYPE_MP3, MediaUtils.CONTENT_TYPE_WAV, "video/quicktime", "video/x-quicktime", MediaUtils.CONTENT_TYPE_AIFF};
    private static final String[] PROTOCOLS = {"http", "https", "ipod-library"};

    private static native void iosPlatformInit();

    /* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/platform/ios/IOSPlatform$IOSPlatformInitializer.class */
    private static final class IOSPlatformInitializer {
        private static final IOSPlatform globalInstance = new IOSPlatform();

        private IOSPlatformInitializer() {
        }
    }

    public static Platform getPlatformInstance() {
        return IOSPlatformInitializer.globalInstance;
    }

    private IOSPlatform() {
    }

    @Override // com.sun.media.jfxmediaimpl.platform.Platform
    public boolean loadPlatform() {
        if (!HostUtils.isIOS()) {
            return false;
        }
        try {
            iosPlatformInit();
            return true;
        } catch (UnsatisfiedLinkError e2) {
            if (Logger.canLog(1)) {
                Logger.logMsg(1, "Unable to load iOS platform.");
                return false;
            }
            return false;
        }
    }

    @Override // com.sun.media.jfxmediaimpl.platform.Platform
    public String[] getSupportedContentTypes() {
        return (String[]) Arrays.copyOf(CONTENT_TYPES, CONTENT_TYPES.length);
    }

    @Override // com.sun.media.jfxmediaimpl.platform.Platform
    public String[] getSupportedProtocols() {
        return (String[]) Arrays.copyOf(PROTOCOLS, PROTOCOLS.length);
    }

    @Override // com.sun.media.jfxmediaimpl.platform.Platform
    public Media createMedia(Locator source) {
        return new IOSMedia(source);
    }

    @Override // com.sun.media.jfxmediaimpl.platform.Platform
    public MediaPlayer createMediaPlayer(Locator source) {
        try {
            return new IOSMediaPlayer(source);
        } catch (Exception e2) {
            if (Logger.canLog(1)) {
                Logger.logMsg(1, "IOSPlatform caught exception while creating media player: " + ((Object) e2));
                return null;
            }
            return null;
        }
    }
}
