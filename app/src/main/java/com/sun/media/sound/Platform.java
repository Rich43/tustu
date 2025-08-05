package com.sun.media.sound;

import java.security.AccessController;
import java.util.StringTokenizer;

/* loaded from: rt.jar:com/sun/media/sound/Platform.class */
final class Platform {
    private static final String libNameMain = "jsound";
    private static final String libNameALSA = "jsoundalsa";
    private static final String libNameDSound = "jsoundds";
    public static final int LIB_MAIN = 1;
    public static final int LIB_ALSA = 2;
    public static final int LIB_DSOUND = 4;
    private static int loadedLibs = 0;
    public static final int FEATURE_MIDIIO = 1;
    public static final int FEATURE_PORTS = 2;
    public static final int FEATURE_DIRECT_AUDIO = 3;
    private static boolean signed8;
    private static boolean bigEndian;

    private static native boolean nIsBigEndian();

    private static native boolean nIsSigned8();

    private static native String nGetExtraLibraries();

    private static native int nGetLibraryForFeature(int i2);

    static {
        loadLibraries();
        readProperties();
    }

    private Platform() {
    }

    static void initialize() {
    }

    static boolean isBigEndian() {
        return bigEndian;
    }

    static boolean isSigned8() {
        return signed8;
    }

    private static void loadLibraries() {
        AccessController.doPrivileged(() -> {
            System.loadLibrary(libNameMain);
            return null;
        });
        loadedLibs |= 1;
        StringTokenizer stringTokenizer = new StringTokenizer(nGetExtraLibraries());
        while (stringTokenizer.hasMoreTokens()) {
            String strNextToken = stringTokenizer.nextToken();
            try {
                AccessController.doPrivileged(() -> {
                    System.loadLibrary(strNextToken);
                    return null;
                });
                if (strNextToken.equals(libNameALSA)) {
                    loadedLibs |= 2;
                } else if (strNextToken.equals(libNameDSound)) {
                    loadedLibs |= 4;
                }
            } catch (Throwable th) {
            }
        }
    }

    static boolean isMidiIOEnabled() {
        return isFeatureLibLoaded(1);
    }

    static boolean isPortsEnabled() {
        return isFeatureLibLoaded(2);
    }

    static boolean isDirectAudioEnabled() {
        return isFeatureLibLoaded(3);
    }

    private static boolean isFeatureLibLoaded(int i2) {
        int iNGetLibraryForFeature = nGetLibraryForFeature(i2);
        return iNGetLibraryForFeature != 0 && (loadedLibs & iNGetLibraryForFeature) == iNGetLibraryForFeature;
    }

    private static void readProperties() {
        bigEndian = nIsBigEndian();
        signed8 = nIsSigned8();
    }
}
