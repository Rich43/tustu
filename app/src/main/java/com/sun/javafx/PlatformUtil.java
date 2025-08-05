package com.sun.javafx;

import com.sun.glass.ui.Platform;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.util.Properties;
import org.icepdf.core.util.PdfOps;

/* loaded from: jfxrt.jar:com/sun/javafx/PlatformUtil.class */
public class PlatformUtil {
    private static final boolean embedded;
    private static final String embeddedType;
    private static final boolean useEGL;
    private static final boolean doEGLCompositing;
    private static final boolean ANDROID;
    private static final boolean WINDOWS;
    private static final boolean WINDOWS_VISTA_OR_LATER;
    private static final boolean WINDOWS_7_OR_LATER;
    private static final boolean MAC;
    private static final boolean LINUX;
    private static final boolean SOLARIS;
    private static final boolean IOS;
    private static final String os = System.getProperty("os.name");
    private static final String version = System.getProperty("os.version");
    private static String javafxPlatform = (String) AccessController.doPrivileged(() -> {
        return System.getProperty("javafx.platform");
    });

    static {
        loadProperties();
        embedded = ((Boolean) AccessController.doPrivileged(() -> {
            return Boolean.valueOf(Boolean.getBoolean("com.sun.javafx.isEmbedded"));
        })).booleanValue();
        embeddedType = (String) AccessController.doPrivileged(() -> {
            return System.getProperty("embedded");
        });
        useEGL = ((Boolean) AccessController.doPrivileged(() -> {
            return Boolean.valueOf(Boolean.getBoolean("use.egl"));
        })).booleanValue();
        if (useEGL) {
            doEGLCompositing = ((Boolean) AccessController.doPrivileged(() -> {
                return Boolean.valueOf(Boolean.getBoolean("doNativeComposite"));
            })).booleanValue();
        } else {
            doEGLCompositing = false;
        }
        ANDROID = "android".equals(javafxPlatform) || "Dalvik".equals(System.getProperty("java.vm.name"));
        WINDOWS = os.startsWith("Windows");
        WINDOWS_VISTA_OR_LATER = WINDOWS && versionNumberGreaterThanOrEqualTo(6.0f);
        WINDOWS_7_OR_LATER = WINDOWS && versionNumberGreaterThanOrEqualTo(6.1f);
        MAC = os.startsWith(Platform.MAC);
        LINUX = os.startsWith("Linux") && !ANDROID;
        SOLARIS = os.startsWith("SunOS");
        IOS = os.startsWith("iOS");
    }

    private static boolean versionNumberGreaterThanOrEqualTo(float value) {
        try {
            return Float.parseFloat(version) >= value;
        } catch (Exception e2) {
            return false;
        }
    }

    public static boolean isWindows() {
        return WINDOWS;
    }

    public static boolean isWinVistaOrLater() {
        return WINDOWS_VISTA_OR_LATER;
    }

    public static boolean isWin7OrLater() {
        return WINDOWS_7_OR_LATER;
    }

    public static boolean isMac() {
        return MAC;
    }

    public static boolean isLinux() {
        return LINUX;
    }

    public static boolean useEGL() {
        return useEGL;
    }

    public static boolean useEGLWindowComposition() {
        return doEGLCompositing;
    }

    public static boolean useGLES2() {
        String useGles2 = (String) AccessController.doPrivileged(() -> {
            return System.getProperty("use.gles2");
        });
        if ("true".equals(useGles2)) {
            return true;
        }
        return false;
    }

    public static boolean isSolaris() {
        return SOLARIS;
    }

    public static boolean isUnix() {
        return LINUX || SOLARIS;
    }

    public static boolean isEmbedded() {
        return embedded;
    }

    public static String getEmbeddedType() {
        return embeddedType;
    }

    public static boolean isIOS() {
        return IOS;
    }

    private static void loadPropertiesFromFile(File file) {
        Properties p2 = new Properties();
        try {
            InputStream in = new FileInputStream(file);
            p2.load(in);
            in.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        if (javafxPlatform == null) {
            javafxPlatform = p2.getProperty("javafx.platform");
        }
        String prefix = javafxPlatform + ".";
        int prefixLength = prefix.length();
        boolean foundPlatform = false;
        for (Object o2 : p2.keySet()) {
            String key = (String) o2;
            if (key.startsWith(prefix)) {
                foundPlatform = true;
                String systemKey = key.substring(prefixLength);
                if (System.getProperty(systemKey) == null) {
                    String value = p2.getProperty(key);
                    System.setProperty(systemKey, value);
                }
            }
        }
        if (!foundPlatform) {
            System.err.println("Warning: No settings found for javafx.platform='" + javafxPlatform + PdfOps.SINGLE_QUOTE_TOKEN);
        }
    }

    private static File getRTDir() {
        try {
            URL url = PlatformUtil.class.getResource("PlatformUtil.class");
            if (url == null) {
                return null;
            }
            String classUrlString = url.toString();
            if (!classUrlString.startsWith("jar:file:") || classUrlString.indexOf(33) == -1) {
                return null;
            }
            String s2 = classUrlString.substring(4, classUrlString.lastIndexOf(33));
            int lastIndexOfSlash = Math.max(s2.lastIndexOf(47), s2.lastIndexOf(92));
            return new File(new URL(s2.substring(0, lastIndexOfSlash + 1)).getPath()).getParentFile();
        } catch (MalformedURLException e2) {
            return null;
        }
    }

    private static void loadProperties() {
        String vmname = System.getProperty("java.vm.name");
        String arch = System.getProperty("os.arch");
        if (javafxPlatform == null && ((arch == null || !arch.equals("arm")) && (vmname == null || vmname.indexOf("Embedded") <= 0))) {
            return;
        }
        AccessController.doPrivileged(() -> {
            File rtDir = getRTDir();
            File rtProperties = new File(rtDir, "javafx.platform.properties");
            if (rtProperties.exists()) {
                loadPropertiesFromFile(rtProperties);
                return null;
            }
            String javaHome = System.getProperty("java.home");
            File javaHomeProperties = new File(javaHome, "lib" + File.separator + "javafx.platform.properties");
            if (javaHomeProperties.exists()) {
                loadPropertiesFromFile(javaHomeProperties);
                return null;
            }
            String javafxRuntimePath = System.getProperty("javafx.runtime.path");
            File javafxRuntimePathProperties = new File(javafxRuntimePath, File.separator + "javafx.platform.properties");
            if (javafxRuntimePathProperties.exists()) {
                loadPropertiesFromFile(javafxRuntimePathProperties);
                return null;
            }
            return null;
        });
    }

    public static boolean isAndroid() {
        return ANDROID;
    }
}
