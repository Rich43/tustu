package com.sun.media.jfxmediaimpl;

import com.sun.glass.utils.NativeLibLoader;
import com.sun.media.jfxmedia.Media;
import com.sun.media.jfxmedia.MediaError;
import com.sun.media.jfxmedia.MediaException;
import com.sun.media.jfxmedia.MediaPlayer;
import com.sun.media.jfxmedia.MetadataParser;
import com.sun.media.jfxmedia.events.MediaErrorListener;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmediaimpl.MediaDisposer;
import com.sun.media.jfxmediaimpl.platform.PlatformManager;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.WeakHashMap;

/* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/NativeMediaManager.class */
public class NativeMediaManager {
    private final List<WeakReference<MediaErrorListener>> errorListeners = new ArrayList();
    private final List<String> supportedContentTypes = new ArrayList();
    private final List<String> supportedProtocols = new ArrayList();
    private static boolean isNativeLayerInitialized = false;
    private static final NativeMediaPlayerDisposer playerDisposer = new NativeMediaPlayerDisposer();
    private static final Map<MediaPlayer, Boolean> allMediaPlayers = new WeakHashMap();

    /* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/NativeMediaManager$NativeMediaManagerInitializer.class */
    private static class NativeMediaManagerInitializer {
        private static final NativeMediaManager globalInstance = new NativeMediaManager();

        private NativeMediaManagerInitializer() {
        }
    }

    public static NativeMediaManager getDefaultInstance() {
        return NativeMediaManagerInitializer.globalInstance;
    }

    protected NativeMediaManager() {
        try {
            AccessController.doPrivileged(() -> {
                if (HostUtils.isWindows() || HostUtils.isMacOSX()) {
                    NativeLibLoader.loadLibrary("glib-lite");
                }
                if (!HostUtils.isLinux() && !HostUtils.isIOS()) {
                    NativeLibLoader.loadLibrary("gstreamer-lite");
                }
                NativeLibLoader.loadLibrary("jfxmedia");
                return null;
            });
        } catch (PrivilegedActionException pae) {
            MediaUtils.error(null, MediaError.ERROR_MANAGER_ENGINEINIT_FAIL.code(), "Unable to load one or more dependent libraries.", pae);
        }
        if (!Logger.initNative()) {
            MediaUtils.error(null, MediaError.ERROR_MANAGER_LOGGER_INIT.code(), "Unable to init logger", null);
        }
    }

    static synchronized void initNativeLayer() {
        if (!isNativeLayerInitialized) {
            PlatformManager.getManager().loadPlatforms();
            isNativeLayerInitialized = true;
        }
    }

    private synchronized void loadContentTypes() {
        if (!this.supportedContentTypes.isEmpty()) {
            return;
        }
        List<String> npt = PlatformManager.getManager().getSupportedContentTypes();
        if (null != npt && !npt.isEmpty()) {
            this.supportedContentTypes.addAll(npt);
        }
        if (Logger.canLog(1)) {
            StringBuilder sb = new StringBuilder("JFXMedia supported content types:\n");
            for (String type : this.supportedContentTypes) {
                sb.append("    ");
                sb.append(type);
                sb.append("\n");
            }
            Logger.logMsg(1, sb.toString());
        }
    }

    private synchronized void loadProtocols() {
        if (!this.supportedProtocols.isEmpty()) {
            return;
        }
        List<String> npt = PlatformManager.getManager().getSupportedProtocols();
        if (null != npt && !npt.isEmpty()) {
            this.supportedProtocols.addAll(npt);
        }
        if (Logger.canLog(1)) {
            StringBuilder sb = new StringBuilder("JFXMedia supported protocols:\n");
            for (String type : this.supportedProtocols) {
                sb.append("    ");
                sb.append(type);
                sb.append("\n");
            }
            Logger.logMsg(1, sb.toString());
        }
    }

    public boolean canPlayContentType(String contentType) {
        if (contentType == null) {
            throw new IllegalArgumentException("contentType == null!");
        }
        if (this.supportedContentTypes.isEmpty()) {
            loadContentTypes();
        }
        for (String type : this.supportedContentTypes) {
            if (contentType.equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }

    public String[] getSupportedContentTypes() {
        if (this.supportedContentTypes.isEmpty()) {
            loadContentTypes();
        }
        return (String[]) this.supportedContentTypes.toArray(new String[1]);
    }

    public boolean canPlayProtocol(String protocol) {
        if (protocol == null) {
            throw new IllegalArgumentException("protocol == null!");
        }
        if (this.supportedProtocols.isEmpty()) {
            loadProtocols();
        }
        for (String type : this.supportedProtocols) {
            if (protocol.equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }

    public static MetadataParser getMetadataParser(Locator locator) {
        return PlatformManager.getManager().createMetadataParser(locator);
    }

    public MediaPlayer getPlayer(Locator locator) {
        initNativeLayer();
        MediaPlayer player = PlatformManager.getManager().createMediaPlayer(locator);
        if (null == player) {
            throw new MediaException("Could not create player!");
        }
        allMediaPlayers.put(player, Boolean.TRUE);
        return player;
    }

    public Media getMedia(Locator locator) {
        initNativeLayer();
        return PlatformManager.getManager().createMedia(locator);
    }

    public void addMediaErrorListener(MediaErrorListener listener) {
        if (listener != null) {
            ListIterator<WeakReference<MediaErrorListener>> it = this.errorListeners.listIterator();
            while (it.hasNext()) {
                MediaErrorListener l2 = it.next().get();
                if (l2 == null) {
                    it.remove();
                }
            }
            this.errorListeners.add(new WeakReference<>(listener));
        }
    }

    public void removeMediaErrorListener(MediaErrorListener listener) {
        if (listener != null) {
            ListIterator<WeakReference<MediaErrorListener>> it = this.errorListeners.listIterator();
            while (it.hasNext()) {
                MediaErrorListener l2 = it.next().get();
                if (l2 == null || l2 == listener) {
                    it.remove();
                }
            }
        }
    }

    public static void registerMediaPlayerForDispose(Object obj, MediaPlayer player) {
        MediaDisposer.addResourceDisposer(obj, player, playerDisposer);
    }

    public List<MediaPlayer> getAllMediaPlayers() {
        List<MediaPlayer> allPlayers = null;
        if (!allMediaPlayers.isEmpty()) {
            allPlayers = new ArrayList<>(allMediaPlayers.keySet());
        }
        return allPlayers;
    }

    List<WeakReference<MediaErrorListener>> getMediaErrorListeners() {
        return this.errorListeners;
    }

    /* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/NativeMediaManager$NativeMediaPlayerDisposer.class */
    private static class NativeMediaPlayerDisposer implements MediaDisposer.ResourceDisposer {
        private NativeMediaPlayerDisposer() {
        }

        @Override // com.sun.media.jfxmediaimpl.MediaDisposer.ResourceDisposer
        public void disposeResource(Object resource) {
            MediaPlayer player = (MediaPlayer) resource;
            if (player != null) {
                player.dispose();
            }
        }
    }
}
