package com.sun.javafx.font;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;
import sun.font.SunFontManager;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:com/sun/javafx/font/FontConfigManager.class */
class FontConfigManager {
    static boolean debugFonts = false;
    static boolean useFontConfig = true;
    static boolean fontConfigFailed = false;
    static boolean useEmbeddedFontSupport = false;
    private static final String[] fontConfigNames;
    private static FcCompFont[] fontConfigFonts;
    private static String defaultFontFile;

    /* loaded from: jfxrt.jar:com/sun/javafx/font/FontConfigManager$FcCompFont.class */
    public static class FcCompFont {
        public String fcName;
        public String fcFamily;
        public int style;
        public FontConfigFont firstFont;
        public FontConfigFont[] allFonts;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/font/FontConfigManager$FontConfigFont.class */
    public static class FontConfigFont {
        public String familyName;
        public String styleStr;
        public String fullName;
        public String fontFile;
    }

    private static native boolean getFontConfig(String str, FcCompFont[] fcCompFontArr, boolean z2);

    private static native boolean populateMapsNative(HashMap<String, String> map, HashMap<String, String> map2, HashMap<String, ArrayList<String>> map3, Locale locale);

    static {
        AccessController.doPrivileged(() -> {
            String dbg = System.getProperty("prism.debugfonts", "");
            debugFonts = "true".equals(dbg);
            String ufc = System.getProperty("prism.useFontConfig", "true");
            useFontConfig = "true".equals(ufc);
            String emb = System.getProperty("prism.embeddedfonts", "");
            useEmbeddedFontSupport = "true".equals(emb);
            return null;
        });
        fontConfigNames = new String[]{"sans:regular:roman", "sans:bold:roman", "sans:regular:italic", "sans:bold:italic", "serif:regular:roman", "serif:bold:roman", "serif:regular:italic", "serif:bold:italic", "monospace:regular:roman", "monospace:bold:roman", "monospace:regular:italic", "monospace:bold:italic"};
    }

    private FontConfigManager() {
    }

    private static String[] getFontConfigNames() {
        return fontConfigNames;
    }

    private static String getFCLocaleStr() {
        Locale l2 = Locale.getDefault();
        String localeStr = l2.getLanguage();
        String country = l2.getCountry();
        if (!country.equals("")) {
            localeStr = localeStr + LanguageTag.SEP + country;
        }
        return localeStr;
    }

    private static synchronized void initFontConfigLogFonts() {
        if (fontConfigFonts != null || fontConfigFailed) {
            return;
        }
        long t0 = 0;
        if (debugFonts) {
            t0 = System.nanoTime();
        }
        String[] fontConfigNames2 = getFontConfigNames();
        FcCompFont[] fontArr = new FcCompFont[fontConfigNames2.length];
        for (int i2 = 0; i2 < fontArr.length; i2++) {
            fontArr[i2] = new FcCompFont();
            fontArr[i2].fcName = fontConfigNames2[i2];
            int colonPos = fontArr[i2].fcName.indexOf(58);
            fontArr[i2].fcFamily = fontArr[i2].fcName.substring(0, colonPos);
            fontArr[i2].style = i2 % 4;
        }
        boolean foundFontConfig = false;
        if (useFontConfig) {
            foundFontConfig = getFontConfig(getFCLocaleStr(), fontArr, true);
        } else if (debugFonts) {
            System.err.println("Not using FontConfig");
        }
        if (useEmbeddedFontSupport || !foundFontConfig) {
            EmbeddedFontSupport.initLogicalFonts(fontArr);
        }
        FontConfigFont anyFont = null;
        for (int i3 = 0; i3 < fontArr.length; i3++) {
            FcCompFont fci = fontArr[i3];
            if (fci.firstFont == null) {
                if (debugFonts) {
                    System.err.println("Fontconfig returned no font for " + fontArr[i3].fcName);
                }
                fontConfigFailed = true;
            } else if (anyFont == null) {
                anyFont = fci.firstFont;
                defaultFontFile = anyFont.fontFile;
            }
        }
        if (anyFont == null) {
            fontConfigFailed = true;
            System.err.println("Error: JavaFX detected no fonts! Please refer to release notes for proper font configuration");
            return;
        }
        if (fontConfigFailed) {
            for (int i4 = 0; i4 < fontArr.length; i4++) {
                if (fontArr[i4].firstFont == null) {
                    fontArr[i4].firstFont = anyFont;
                }
            }
        }
        fontConfigFonts = fontArr;
        if (debugFonts) {
            long t1 = System.nanoTime();
            System.err.println("Time spent accessing fontconfig=" + ((t1 - t0) / 1000000) + "ms.");
            for (int i5 = 0; i5 < fontConfigFonts.length; i5++) {
                FcCompFont fci2 = fontConfigFonts[i5];
                System.err.println("FC font " + fci2.fcName + " maps to " + fci2.firstFont.fullName + " in file " + fci2.firstFont.fontFile);
                if (fci2.allFonts != null) {
                    for (int f2 = 0; f2 < fci2.allFonts.length; f2++) {
                        FontConfigFont fcf = fci2.allFonts[f2];
                        System.err.println(" " + f2 + ") Family=" + fcf.familyName + ", Style=" + fcf.styleStr + ", Fullname=" + fcf.fullName + ", File=" + fcf.fontFile);
                    }
                }
            }
        }
    }

    public static void populateMaps(HashMap<String, String> fontToFileMap, HashMap<String, String> fontToFamilyNameMap, HashMap<String, ArrayList<String>> familyToFontListMap, Locale locale) {
        boolean pnm = false;
        if (useFontConfig && !fontConfigFailed) {
            pnm = populateMapsNative(fontToFileMap, fontToFamilyNameMap, familyToFontListMap, locale);
        }
        if (fontConfigFailed || useEmbeddedFontSupport || !pnm) {
            EmbeddedFontSupport.populateMaps(fontToFileMap, fontToFamilyNameMap, familyToFontListMap, locale);
        }
    }

    private static String mapFxToFcLogicalFamilyName(String fxName) {
        if (fxName.equals("serif")) {
            return "serif";
        }
        if (fxName.equals("monospaced")) {
            return "monospace";
        }
        return "sans";
    }

    public static FcCompFont getFontConfigFont(String fxFamilyName, boolean bold, boolean italic) {
        initFontConfigLogFonts();
        if (fontConfigFonts == null) {
            return null;
        }
        String name = mapFxToFcLogicalFamilyName(fxFamilyName.toLowerCase());
        int style = bold ? 1 : 0;
        if (italic) {
            style += 2;
        }
        FcCompFont fcInfo = null;
        int i2 = 0;
        while (true) {
            if (i2 < fontConfigFonts.length) {
                if (!name.equals(fontConfigFonts[i2].fcFamily) || style != fontConfigFonts[i2].style) {
                    i2++;
                } else {
                    fcInfo = fontConfigFonts[i2];
                    break;
                }
            } else {
                break;
            }
        }
        if (fcInfo == null) {
            fcInfo = fontConfigFonts[0];
        }
        if (debugFonts) {
            System.err.println("FC name=" + name + " style=" + style + " uses " + fcInfo.firstFont.fullName + " in file: " + fcInfo.firstFont.fontFile);
        }
        return fcInfo;
    }

    public static String getDefaultFontPath() {
        if (fontConfigFonts == null && !fontConfigFailed) {
            getFontConfigFont(LogicalFont.SYSTEM, false, false);
        }
        return defaultFontFile;
    }

    public static ArrayList<String> getFileNames(FcCompFont font, boolean fallBacksOnly) {
        ArrayList fileList = new ArrayList();
        if (font.allFonts != null) {
            int start = fallBacksOnly ? 1 : 0;
            for (int i2 = start; i2 < font.allFonts.length; i2++) {
                fileList.add(font.allFonts[i2].fontFile);
            }
        }
        return fileList;
    }

    public static ArrayList<String> getFontNames(FcCompFont font, boolean fallBacksOnly) {
        ArrayList fontList = new ArrayList();
        if (font.allFonts != null) {
            int start = fallBacksOnly ? 1 : 0;
            for (int i2 = start; i2 < font.allFonts.length; i2++) {
                fontList.add(font.allFonts[i2].fullName);
            }
        }
        return fontList;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/font/FontConfigManager$EmbeddedFontSupport.class */
    private static class EmbeddedFontSupport {
        private static String fontDir;
        static String[] jreFontsProperties;
        private static String fontDirProp = null;
        private static boolean fontDirFromJRE = false;

        private EmbeddedFontSupport() {
        }

        static {
            AccessController.doPrivileged(() -> {
                initEmbeddedFonts();
                return null;
            });
            jreFontsProperties = new String[]{"sans.regular.0.font", SunFontManager.lucidaFontName, "sans.regular.0.file", "LucidaSansRegular.ttf", "sans.bold.0.font", "Lucida Sans Bold", "sans.bold.0.file", "LucidaSansDemiBold.ttf", "monospace.regular.0.font", "Lucida Typewriter Regular", "monospace.regular.0.file", "LucidaTypewriterRegular.ttf", "monospace.bold.0.font", "Lucida Typewriter Bold", "monospace.bold.0.file", "LucidaTypewriterBold.ttf", "serif.regular.0.font", "Lucida Bright", "serif.regular.0.file", "LucidaBrightRegular.ttf", "serif.bold.0.font", "Lucida Bright Demibold", "serif.bold.0.file", "LucidaBrightDemiBold.ttf", "serif.italic.0.font", "Lucida Bright Italic", "serif.italic.0.file", "LucidaBrightItalic.ttf", "serif.bolditalic.0.font", "Lucida Bright Demibold Italic", "serif.bolditalic.0.file", "LucidaBrightDemiItalic.ttf"};
        }

        private static void initEmbeddedFonts() {
            fontDirProp = System.getProperty("prism.fontdir");
            if (fontDirProp != null) {
                fontDir = fontDirProp;
                return;
            }
            try {
                String javaHome = System.getProperty("java.home");
                if (javaHome == null) {
                    return;
                }
                File fontDirectory = new File(javaHome, "lib/fonts");
                if (fontDirectory.exists()) {
                    fontDirFromJRE = true;
                    fontDir = fontDirectory.getPath();
                }
                if (FontConfigManager.debugFonts) {
                    System.err.println("Fallback fontDir is " + ((Object) fontDirectory) + " exists = " + fontDirectory.exists());
                }
            } catch (Exception e2) {
                if (FontConfigManager.debugFonts) {
                    e2.printStackTrace();
                }
                fontDir = "/";
            }
        }

        private static String getStyleStr(int style) {
            switch (style) {
                case 0:
                    return "regular";
                case 1:
                    return "bold";
                case 2:
                    return "italic";
                case 3:
                    return "bolditalic";
                default:
                    return "regular";
            }
        }

        private static boolean exists(File f2) {
            return ((Boolean) AccessController.doPrivileged(() -> {
                return Boolean.valueOf(f2.exists());
            })).booleanValue();
        }

        static void initLogicalFonts(FcCompFont[] fonts) {
            Properties props = new Properties();
            try {
                File f2 = new File(fontDir, "logicalfonts.properties");
                if (f2.exists()) {
                    FileInputStream fis = new FileInputStream(f2);
                    props.load(fis);
                    fis.close();
                } else if (fontDirFromJRE) {
                    for (int i2 = 0; i2 < jreFontsProperties.length; i2 += 2) {
                        props.setProperty(jreFontsProperties[i2], jreFontsProperties[i2 + 1]);
                    }
                    if (FontConfigManager.debugFonts) {
                        System.err.println("Using fallback implied logicalfonts.properties");
                    }
                }
            } catch (IOException ioe) {
                if (FontConfigManager.debugFonts) {
                    System.err.println(ioe);
                    return;
                }
            }
            for (int f3 = 0; f3 < fonts.length; f3++) {
                String fcFamily = fonts[f3].fcFamily;
                String styleStr = getStyleStr(fonts[f3].style);
                String key = fcFamily + "." + styleStr + ".";
                ArrayList<FontConfigFont> allFonts = new ArrayList<>();
                int i3 = 0;
                while (true) {
                    String file = props.getProperty(key + i3 + ".file");
                    String font = props.getProperty(key + i3 + ".font");
                    i3++;
                    if (file == null) {
                        break;
                    }
                    File ff = new File(fontDir, file);
                    if (!exists(ff)) {
                        if (FontConfigManager.debugFonts) {
                            System.out.println("Failed to find logical font file " + ((Object) ff));
                        }
                    } else {
                        FontConfigFont fcFont = new FontConfigFont();
                        fcFont.fontFile = ff.getPath();
                        fcFont.fullName = font;
                        fcFont.familyName = null;
                        fcFont.styleStr = null;
                        if (fonts[f3].firstFont == null) {
                            fonts[f3].firstFont = fcFont;
                        }
                        allFonts.add(fcFont);
                    }
                }
                if (allFonts.size() > 0) {
                    fonts[f3].allFonts = new FontConfigFont[allFonts.size()];
                    allFonts.toArray(fonts[f3].allFonts);
                }
            }
        }

        static void populateMaps(HashMap<String, String> fontToFileMap, HashMap<String, String> fontToFamilyNameMap, HashMap<String, ArrayList<String>> familyToFontListMap, Locale locale) {
            Properties props = new Properties();
            AccessController.doPrivileged(() -> {
                try {
                    String lFile = fontDir + "/allfonts.properties";
                    FileInputStream fis = new FileInputStream(lFile);
                    props.load(fis);
                    fis.close();
                    return null;
                } catch (IOException ioe) {
                    props.clear();
                    if (FontConfigManager.debugFonts) {
                        System.err.println(ioe);
                        System.err.println("Fall back to opening the files");
                        return null;
                    }
                    return null;
                }
            });
            if (!props.isEmpty()) {
                int maxFont = Integer.MAX_VALUE;
                try {
                    maxFont = Integer.parseInt(props.getProperty("maxFont", ""));
                } catch (NumberFormatException e2) {
                }
                if (maxFont <= 0) {
                    maxFont = Integer.MAX_VALUE;
                }
                for (int f2 = 0; f2 < maxFont; f2++) {
                    String family = props.getProperty("family." + f2);
                    String font = props.getProperty("font." + f2);
                    String file = props.getProperty("file." + f2);
                    if (file != null) {
                        File ff = new File(fontDir, file);
                        if (exists(ff) && family != null && font != null) {
                            String fontLC = font.toLowerCase(Locale.ENGLISH);
                            String familyLC = family.toLowerCase(Locale.ENGLISH);
                            fontToFileMap.put(fontLC, ff.getPath());
                            fontToFamilyNameMap.put(fontLC, family);
                            ArrayList<String> familyArr = familyToFontListMap.get(familyLC);
                            if (familyArr == null) {
                                familyArr = new ArrayList<>(4);
                                familyToFontListMap.put(familyLC, familyArr);
                            }
                            familyArr.add(font);
                        }
                    } else {
                        return;
                    }
                }
            }
        }
    }
}
