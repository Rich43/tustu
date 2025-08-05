package com.sun.media.jfxmediaimpl.platform.osx;

import com.sun.glass.utils.NativeLibLoader;
import com.sun.media.jfxmedia.Media;
import com.sun.media.jfxmedia.MediaPlayer;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmediaimpl.HostUtils;
import com.sun.media.jfxmediaimpl.MediaUtils;
import com.sun.media.jfxmediaimpl.platform.Platform;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.security.AccessController;
import java.util.Arrays;

/* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/platform/osx/OSXPlatform.class */
public final class OSXPlatform extends Platform {
    private static final String[] CONTENT_TYPES = {MediaUtils.CONTENT_TYPE_AIFF, MediaUtils.CONTENT_TYPE_MP3, MediaUtils.CONTENT_TYPE_MPA, MediaUtils.CONTENT_TYPE_M4A, MediaUtils.CONTENT_TYPE_MP4, MediaUtils.CONTENT_TYPE_M4V, MediaUtils.CONTENT_TYPE_M3U8, MediaUtils.CONTENT_TYPE_M3U};
    private static final String[] PROTOCOLS = {DeploymentDescriptorParser.ATTR_FILE, "http", "https"};

    private static native boolean osxPlatformInit();

    /* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/platform/osx/OSXPlatform$OSXPlatformInitializer.class */
    private static final class OSXPlatformInitializer {
        private static final OSXPlatform globalInstance;

        private OSXPlatformInitializer() {
        }

        static {
            boolean isLoaded = false;
            try {
                isLoaded = ((Boolean) AccessController.doPrivileged(() -> {
                    boolean avf = false;
                    try {
                        NativeLibLoader.loadLibrary("jfxmedia_avf");
                        avf = true;
                    } catch (UnsatisfiedLinkError e2) {
                    }
                    return Boolean.valueOf(avf);
                })).booleanValue();
            } catch (Exception e2) {
            }
            if (isLoaded) {
                globalInstance = new OSXPlatform();
            } else {
                globalInstance = null;
            }
        }
    }

    public static Platform getPlatformInstance() {
        return OSXPlatformInitializer.globalInstance;
    }

    private OSXPlatform() {
    }

    @Override // com.sun.media.jfxmediaimpl.platform.Platform
    public boolean loadPlatform() {
        if (!HostUtils.isMacOSX()) {
            return false;
        }
        try {
            return osxPlatformInit();
        } catch (UnsatisfiedLinkError e2) {
            if (Logger.canLog(1)) {
                Logger.logMsg(1, "Unable to load OSX platform.");
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
        return new OSXMedia(source);
    }

    @Override // com.sun.media.jfxmediaimpl.platform.Platform
    public MediaPlayer createMediaPlayer(Locator source) {
        try {
            return new OSXMediaPlayer(source);
        } catch (Exception ex) {
            if (Logger.canLog(1)) {
                Logger.logMsg(1, "OSXPlatform caught exception while creating media player: " + ((Object) ex));
                ex.printStackTrace();
                return null;
            }
            return null;
        }
    }
}
