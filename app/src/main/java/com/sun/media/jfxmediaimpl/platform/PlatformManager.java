package com.sun.media.jfxmediaimpl.platform;

import com.sun.media.jfxmedia.Media;
import com.sun.media.jfxmedia.MediaPlayer;
import com.sun.media.jfxmedia.MetadataParser;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmediaimpl.HostUtils;
import com.sun.media.jfxmediaimpl.platform.gstreamer.GSTPlatform;
import com.sun.media.jfxmediaimpl.platform.ios.IOSPlatform;
import com.sun.media.jfxmediaimpl.platform.java.JavaPlatform;
import com.sun.media.jfxmediaimpl.platform.osx.OSXPlatform;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/platform/PlatformManager.class */
public final class PlatformManager {
    private static String enabledPlatforms;
    private final List<Platform> platforms;

    static {
        AccessController.doPrivileged(() -> {
            getPlatformSettings();
            return null;
        });
    }

    private static void getPlatformSettings() {
        enabledPlatforms = System.getProperty("jfxmedia.platforms", "").toLowerCase();
    }

    private static boolean isPlatformEnabled(String name) {
        return null == enabledPlatforms || enabledPlatforms.length() == 0 || enabledPlatforms.indexOf(name.toLowerCase()) != -1;
    }

    /* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/platform/PlatformManager$PlatformManagerInitializer.class */
    private static final class PlatformManagerInitializer {
        private static final PlatformManager globalInstance = new PlatformManager();

        private PlatformManagerInitializer() {
        }
    }

    public static PlatformManager getManager() {
        return PlatformManagerInitializer.globalInstance;
    }

    private PlatformManager() {
        Platform platty;
        Platform platty2;
        Platform platty3;
        Platform platty4;
        this.platforms = new ArrayList();
        if (isPlatformEnabled("JavaPlatform") && null != (platty4 = JavaPlatform.getPlatformInstance())) {
            this.platforms.add(platty4);
        }
        if (!HostUtils.isIOS() && isPlatformEnabled("GSTPlatform") && null != (platty3 = GSTPlatform.getPlatformInstance())) {
            this.platforms.add(platty3);
        }
        if (HostUtils.isMacOSX() && isPlatformEnabled("OSXPlatform") && null != (platty2 = OSXPlatform.getPlatformInstance())) {
            this.platforms.add(platty2);
        }
        if (HostUtils.isIOS() && isPlatformEnabled("IOSPlatform") && null != (platty = IOSPlatform.getPlatformInstance())) {
            this.platforms.add(platty);
        }
        if (Logger.canLog(1)) {
            StringBuilder sb = new StringBuilder("Enabled JFXMedia platforms: ");
            for (Platform p2 : this.platforms) {
                sb.append("\n   - ");
                sb.append(p2.getClass().getName());
            }
            Logger.logMsg(1, sb.toString());
        }
    }

    public synchronized void loadPlatforms() {
        Iterator<Platform> iter = this.platforms.iterator();
        while (iter.hasNext()) {
            Platform platty = iter.next();
            if (!platty.loadPlatform()) {
                if (Logger.canLog(1)) {
                    Logger.logMsg(1, "Failed to load platform: " + ((Object) platty));
                }
                iter.remove();
            }
        }
    }

    public List<String> getSupportedContentTypes() {
        ArrayList<String> outTypes = new ArrayList<>();
        if (!this.platforms.isEmpty()) {
            for (Platform platty : this.platforms) {
                if (Logger.canLog(1)) {
                    Logger.logMsg(1, "Getting content types from platform: " + ((Object) platty));
                }
                String[] npt = platty.getSupportedContentTypes();
                if (npt != null) {
                    for (String type : npt) {
                        if (!outTypes.contains(type)) {
                            outTypes.add(type);
                        }
                    }
                }
            }
        }
        return outTypes;
    }

    public List<String> getSupportedProtocols() {
        ArrayList<String> outProtocols = new ArrayList<>();
        if (!this.platforms.isEmpty()) {
            for (Platform platty : this.platforms) {
                String[] npt = platty.getSupportedProtocols();
                if (npt != null) {
                    for (String p2 : npt) {
                        if (!outProtocols.contains(p2)) {
                            outProtocols.add(p2);
                        }
                    }
                }
            }
        }
        return outProtocols;
    }

    public MetadataParser createMetadataParser(Locator source) {
        for (Platform platty : this.platforms) {
            MetadataParser parser = platty.createMetadataParser(source);
            if (parser != null) {
                return parser;
            }
        }
        return null;
    }

    public Media createMedia(Locator source) {
        Media outMedia;
        String mimeType = source.getContentType();
        String protocol = source.getProtocol();
        for (Platform platty : this.platforms) {
            if (platty.canPlayContentType(mimeType) && platty.canPlayProtocol(protocol) && null != (outMedia = platty.createMedia(source))) {
                return outMedia;
            }
        }
        return null;
    }

    public MediaPlayer createMediaPlayer(Locator source) {
        MediaPlayer outPlayer;
        String mimeType = source.getContentType();
        String protocol = source.getProtocol();
        for (Platform platty : this.platforms) {
            if (platty.canPlayContentType(mimeType) && platty.canPlayProtocol(protocol) && null != (outPlayer = platty.createMediaPlayer(source))) {
                return outPlayer;
            }
        }
        return null;
    }
}
