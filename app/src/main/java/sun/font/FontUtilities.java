package sun.font;

import com.sun.corba.se.impl.util.Version;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.ref.SoftReference;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.plaf.FontUIResource;
import org.apache.commons.net.ftp.FTP;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:sun/font/FontUtilities.class */
public final class FontUtilities {
    public static boolean isSolaris;
    public static boolean isLinux;
    public static boolean isMacOSX;
    public static boolean isMacOSX14;
    public static boolean isSolaris8;
    public static boolean isSolaris9;
    public static boolean isOpenSolaris;
    public static boolean useT2K;
    public static boolean isWindows;
    public static boolean isOpenJDK;
    static final String LUCIDA_FILE_NAME = "LucidaSansRegular.ttf";
    private static boolean debugFonts = false;
    private static PlatformLogger logger = null;
    private static boolean logging;
    public static final int MIN_LAYOUT_CHARCODE = 768;
    public static final int MAX_LAYOUT_CHARCODE = 8303;
    private static volatile SoftReference<ConcurrentHashMap<PhysicalFont, CompositeFont>> compMapRef;
    private static final String[][] nameMap;

    /* JADX WARN: Type inference failed for: r0v6, types: [java.lang.String[], java.lang.String[][]] */
    static {
        AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.font.FontUtilities.1
            @Override // java.security.PrivilegedAction
            public Object run() {
                String property = System.getProperty("os.name", "unknownOS");
                FontUtilities.isSolaris = property.startsWith("SunOS");
                FontUtilities.isLinux = property.startsWith("Linux");
                FontUtilities.isMacOSX = property.contains("OS X");
                if (FontUtilities.isMacOSX) {
                    FontUtilities.isMacOSX14 = true;
                    String property2 = System.getProperty("os.version", "");
                    if (property2.startsWith("10.")) {
                        String strSubstring = property2.substring(3);
                        int iIndexOf = strSubstring.indexOf(46);
                        if (iIndexOf != -1) {
                            strSubstring = strSubstring.substring(0, iIndexOf);
                        }
                        try {
                            FontUtilities.isMacOSX14 = Integer.parseInt(strSubstring) >= 14;
                        } catch (NumberFormatException e2) {
                        }
                    }
                }
                String property3 = System.getProperty("sun.java2d.font.scaler");
                if (property3 != null) {
                    FontUtilities.useT2K = "t2k".equals(property3);
                } else {
                    FontUtilities.useT2K = false;
                }
                if (FontUtilities.isSolaris) {
                    String property4 = System.getProperty("os.version", Version.BUILD);
                    FontUtilities.isSolaris8 = property4.startsWith("5.8");
                    FontUtilities.isSolaris9 = property4.startsWith("5.9");
                    if (Float.parseFloat(property4) > 5.1f) {
                        String line = null;
                        try {
                            FileInputStream fileInputStream = new FileInputStream(new File("/etc/release"));
                            line = new BufferedReader(new InputStreamReader(fileInputStream, FTP.DEFAULT_CONTROL_ENCODING)).readLine();
                            fileInputStream.close();
                        } catch (Exception e3) {
                        }
                        if (line != null && line.indexOf("OpenSolaris") >= 0) {
                            FontUtilities.isOpenSolaris = true;
                        } else {
                            FontUtilities.isOpenSolaris = false;
                        }
                    } else {
                        FontUtilities.isOpenSolaris = false;
                    }
                } else {
                    FontUtilities.isSolaris8 = false;
                    FontUtilities.isSolaris9 = false;
                    FontUtilities.isOpenSolaris = false;
                }
                FontUtilities.isWindows = property.startsWith("Windows");
                FontUtilities.isOpenJDK = !new File(new StringBuilder().append(new StringBuilder().append(new StringBuilder().append(System.getProperty("java.home", "")).append(File.separator).append("lib").toString()).append(File.separator).append("fonts").toString()).append(File.separator).append(FontUtilities.LUCIDA_FILE_NAME).toString()).exists();
                String property5 = System.getProperty("sun.java2d.debugfonts");
                if (property5 != null && !property5.equals("false")) {
                    boolean unused = FontUtilities.debugFonts = true;
                    PlatformLogger unused2 = FontUtilities.logger = PlatformLogger.getLogger("sun.java2d");
                    if (property5.equals("warning")) {
                        FontUtilities.logger.setLevel(PlatformLogger.Level.WARNING);
                    } else if (property5.equals("severe")) {
                        FontUtilities.logger.setLevel(PlatformLogger.Level.SEVERE);
                    }
                }
                if (FontUtilities.debugFonts) {
                    PlatformLogger unused3 = FontUtilities.logger = PlatformLogger.getLogger("sun.java2d");
                    boolean unused4 = FontUtilities.logging = FontUtilities.logger.isEnabled();
                    return null;
                }
                return null;
            }
        });
        compMapRef = new SoftReference<>(null);
        nameMap = new String[]{new String[]{"sans", "sansserif"}, new String[]{"sans-serif", "sansserif"}, new String[]{"serif", "serif"}, new String[]{"monospace", "monospaced"}};
    }

    public static Font2D getFont2D(Font font) {
        return FontAccess.getFontAccess().getFont2D(font);
    }

    public static boolean isComplexText(char[] cArr, int i2, int i3) {
        for (int i4 = i2; i4 < i3; i4++) {
            if (cArr[i4] >= 768 && isNonSimpleChar(cArr[i4])) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNonSimpleChar(char c2) {
        return isComplexCharCode(c2) || (c2 >= 55296 && c2 <= 57343);
    }

    public static boolean isComplexCharCode(int i2) {
        if (i2 < 768 || i2 > 8303) {
            return false;
        }
        if (i2 <= 879) {
            return true;
        }
        if (i2 < 1424) {
            return false;
        }
        if (i2 <= 1791) {
            return true;
        }
        if (i2 < 2304) {
            return false;
        }
        if (i2 <= 3711) {
            return true;
        }
        if (i2 < 3840) {
            return false;
        }
        if (i2 <= 4095) {
            return true;
        }
        if (i2 < 4352) {
            return false;
        }
        if (i2 < 4607) {
            return true;
        }
        if (i2 < 6016) {
            return false;
        }
        if (i2 <= 6143) {
            return true;
        }
        if (i2 < 8204) {
            return false;
        }
        if (i2 <= 8205) {
            return true;
        }
        if (i2 >= 8234 && i2 <= 8238) {
            return true;
        }
        if (i2 >= 8298 && i2 <= 8303) {
            return true;
        }
        return false;
    }

    public static PlatformLogger getLogger() {
        return logger;
    }

    public static boolean isLogging() {
        return logging;
    }

    public static boolean debugFonts() {
        return debugFonts;
    }

    public static boolean fontSupportsDefaultEncoding(Font font) {
        return getFont2D(font) instanceof CompositeFont;
    }

    public static FontUIResource getCompositeFontUIResource(Font font) {
        FontUIResource fontUIResource = new FontUIResource(font);
        Font2D font2D = getFont2D(font);
        if (!(font2D instanceof PhysicalFont)) {
            return fontUIResource;
        }
        Font2D font2DFindFont2D = FontManagerFactory.getInstance().findFont2D("dialog", font.getStyle(), 0);
        if (font2DFindFont2D == null || !(font2DFindFont2D instanceof CompositeFont)) {
            return fontUIResource;
        }
        CompositeFont compositeFont = (CompositeFont) font2DFindFont2D;
        PhysicalFont physicalFont = (PhysicalFont) font2D;
        ConcurrentHashMap<PhysicalFont, CompositeFont> concurrentHashMap = compMapRef.get();
        if (concurrentHashMap == null) {
            concurrentHashMap = new ConcurrentHashMap<>();
            compMapRef = new SoftReference<>(concurrentHashMap);
        }
        CompositeFont compositeFont2 = concurrentHashMap.get(physicalFont);
        if (compositeFont2 == null) {
            compositeFont2 = new CompositeFont(physicalFont, compositeFont);
            concurrentHashMap.put(physicalFont, compositeFont2);
        }
        FontAccess.getFontAccess().setFont2D(fontUIResource, compositeFont2.handle);
        FontAccess.getFontAccess().setCreatedFont(fontUIResource);
        return fontUIResource;
    }

    public static String mapFcName(String str) {
        for (int i2 = 0; i2 < nameMap.length; i2++) {
            if (str.equals(nameMap[i2][0])) {
                return nameMap[i2][1];
            }
        }
        return null;
    }

    public static FontUIResource getFontConfigFUIR(String str, int i2, int i3) {
        FontUIResource fontUIResource;
        String strMapFcName = mapFcName(str);
        if (strMapFcName == null) {
            strMapFcName = "sansserif";
        }
        FontManager fontManagerFactory = FontManagerFactory.getInstance();
        if (fontManagerFactory instanceof SunFontManager) {
            fontUIResource = ((SunFontManager) fontManagerFactory).getFontConfigFUIR(strMapFcName, i2, i3);
        } else {
            fontUIResource = new FontUIResource(strMapFcName, i2, i3);
        }
        return fontUIResource;
    }

    public static boolean textLayoutIsCompatible(Font font) {
        Font2D font2D = getFont2D(font);
        if (font2D instanceof TrueTypeFont) {
            TrueTypeFont trueTypeFont = (TrueTypeFont) font2D;
            return trueTypeFont.getDirectoryEntry(1196643650) == null || trueTypeFont.getDirectoryEntry(1196445523) != null;
        }
        return false;
    }
}
