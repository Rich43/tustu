package com.sun.glass.ui;

import com.intel.bluetooth.BlueCoveImpl;
import java.security.AccessController;
import java.util.Locale;

/* loaded from: jfxrt.jar:com/sun/glass/ui/Platform.class */
final class Platform {
    public static final String MAC = "Mac";
    public static final String WINDOWS = "Win";
    public static final String GTK = "Gtk";
    public static final String IOS = "Ios";
    public static final String UNKNOWN = "unknown";
    private static String type = null;

    Platform() {
    }

    public static synchronized String determinePlatform() {
        if (type == null) {
            String userPlatform = (String) AccessController.doPrivileged(() -> {
                return System.getProperty("glass.platform");
            });
            if (userPlatform != null) {
                if (userPlatform.equals("macosx")) {
                    type = MAC;
                } else if (userPlatform.equals("windows")) {
                    type = WINDOWS;
                } else if (userPlatform.equals("linux") || userPlatform.equals("gtk")) {
                    type = GTK;
                } else if (userPlatform.equals("ios")) {
                    type = IOS;
                } else {
                    type = userPlatform;
                }
                return type;
            }
            String osName = System.getProperty("os.name");
            String osNameLowerCase = osName.toLowerCase(Locale.ROOT);
            if (osNameLowerCase.startsWith(BlueCoveImpl.STACK_OSX) || osNameLowerCase.startsWith("darwin")) {
                type = MAC;
            } else if (osNameLowerCase.startsWith("wind")) {
                type = WINDOWS;
            } else if (osNameLowerCase.startsWith("linux")) {
                type = GTK;
            }
        }
        return type;
    }
}
