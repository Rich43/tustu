package com.sun.media.jfxmediaimpl.platform.gstreamer;

import com.sun.media.jfxmedia.Media;
import com.sun.media.jfxmedia.MediaError;
import com.sun.media.jfxmedia.MediaPlayer;
import com.sun.media.jfxmedia.events.PlayerStateEvent;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmediaimpl.HostUtils;
import com.sun.media.jfxmediaimpl.MediaUtils;
import com.sun.media.jfxmediaimpl.platform.Platform;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.util.Arrays;
import sun.java2d.marlin.MarlinConst;

/* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/platform/gstreamer/GSTPlatform.class */
public final class GSTPlatform extends Platform {
    private static final String[] CONTENT_TYPES = {MediaUtils.CONTENT_TYPE_AIFF, MediaUtils.CONTENT_TYPE_MP3, MediaUtils.CONTENT_TYPE_MPA, MediaUtils.CONTENT_TYPE_WAV, MediaUtils.CONTENT_TYPE_JFX, MediaUtils.CONTENT_TYPE_FLV, "video/x-fxm", MediaUtils.CONTENT_TYPE_MP4, MediaUtils.CONTENT_TYPE_M4A, MediaUtils.CONTENT_TYPE_M4V, MediaUtils.CONTENT_TYPE_M3U8, MediaUtils.CONTENT_TYPE_M3U};
    private static final String[] PROTOCOLS = {DeploymentDescriptorParser.ATTR_FILE, "http", "https"};
    private static GSTPlatform globalInstance = null;

    private static native int gstInitPlatform();

    @Override // com.sun.media.jfxmediaimpl.platform.Platform
    public boolean loadPlatform() {
        MediaError ret;
        try {
            ret = MediaError.getFromCode(gstInitPlatform());
        } catch (UnsatisfiedLinkError e2) {
            ret = MediaError.ERROR_MANAGER_ENGINEINIT_FAIL;
        }
        if (ret != MediaError.ERROR_NONE) {
            MediaUtils.nativeError(GSTPlatform.class, ret);
            return true;
        }
        return true;
    }

    public static synchronized Platform getPlatformInstance() {
        if (null == globalInstance) {
            globalInstance = new GSTPlatform();
        }
        return globalInstance;
    }

    private GSTPlatform() {
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
        return new GSTMedia(source);
    }

    @Override // com.sun.media.jfxmediaimpl.platform.Platform
    public MediaPlayer createMediaPlayer(Locator source) {
        try {
            GSTMediaPlayer player = new GSTMediaPlayer(source);
            if (HostUtils.isMacOSX()) {
                String contentType = source.getContentType();
                if (MediaUtils.CONTENT_TYPE_MP4.equals(contentType) || MediaUtils.CONTENT_TYPE_M4V.equals(contentType) || source.getStringLocation().endsWith(".m3u8")) {
                    String scheme = source.getURI().getScheme();
                    long timeout = (scheme.equals("http") || scheme.equals("https")) ? 60000L : MarlinConst.statDump;
                    long timeWaited = 0;
                    Object lock = new Object();
                    PlayerStateEvent.PlayerState state = player.getState();
                    while (true) {
                        PlayerStateEvent.PlayerState state2 = state;
                        if (timeWaited >= timeout || !(state2 == PlayerStateEvent.PlayerState.UNKNOWN || state2 == PlayerStateEvent.PlayerState.STALLED)) {
                            break;
                        }
                        try {
                            synchronized (lock) {
                                lock.wait(50L);
                                timeWaited += 50;
                            }
                        } catch (InterruptedException e2) {
                        }
                        if (player.isErrorEventCached()) {
                            break;
                        }
                        state = player.getState();
                    }
                    if (player.getState() != PlayerStateEvent.PlayerState.READY) {
                        player.dispose();
                        player = null;
                    }
                }
            }
            return player;
        } catch (Exception e3) {
            if (Logger.canLog(1)) {
                Logger.logMsg(1, "GSTPlatform caught exception while creating media player: " + ((Object) e3));
                return null;
            }
            return null;
        }
    }
}
