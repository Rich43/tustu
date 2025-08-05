package org.icepdf.core.pobjects.fonts;

import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.rtf.RTFGenerator;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.Defs;
import org.icepdf.core.util.FontUtil;
import sun.util.locale.LanguageTag;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/fonts/FontManager.class */
public class FontManager {
    private static List<Object[]> fontList;
    private static List<Object[]> fontJarList;
    private static FontManager fontManager;
    private static final Logger logger = Logger.getLogger(FontManager.class.toString());
    private static int PLAIN = -268435455;
    private static int BOLD = -268435440;
    private static int ITALIC = -268435200;
    private static int BOLD_ITALIC = -268431360;
    private static final String[][] TYPE1_FONT_DIFFS = {new String[]{"Bookman-Demi", "URWBookmanL-DemiBold", "Arial"}, new String[]{"Bookman-DemiItalic", "URWBookmanL-DemiBoldItal", "Arial"}, new String[]{"Bookman-Light", "URWBookmanL-Ligh", "Arial"}, new String[]{"Bookman-LightItalic", "URWBookmanL-LighItal", "Arial"}, new String[]{"Courier", "NimbusMonL-Regu", "Nimbus Mono L", "CourierNew", "CourierNewPSMT"}, new String[]{"Courier-Oblique", "NimbusMonL-ReguObli", "Nimbus Mono L", "Courier,Italic", "CourierNew-Italic", "CourierNew,Italic", "CourierNewPS-ItalicMT"}, new String[]{"Courier-Bold", "NimbusMonL-Bold", "Nimbus Mono L", "Courier,Bold", "CourierNew,Bold", "CourierNew-Bold", "CourierNewPS-BoldMT"}, new String[]{"Courier-BoldOblique", "NimbusMonL-BoldObli", "Nimbus Mono L", "Courier,BoldItalic", "CourierNew-BoldItalic", "CourierNew,BoldItalic", "CourierNewPS-BoldItalicMT"}, new String[]{"AvantGarde-Book", "URWGothicL-Book", "Arial"}, new String[]{"AvantGarde-BookOblique", "URWGothicL-BookObli", "Arial"}, new String[]{"AvantGarde-Demi", "URWGothicL-Demi", "Arial"}, new String[]{"AvantGarde-DemiOblique", "URWGothicL-DemiObli", "Arial"}, new String[]{RTFGenerator.defaultFontFamily, RTFGenerator.defaultFontFamily, "Arial", "ArialMT", "NimbusSanL-Regu", "Nimbus Sans L"}, new String[]{"Helvetica-Oblique", "NimbusSanL-ReguItal", "Nimbus Sans L", "Helvetica,Italic", "Helvetica-Italic", "Arial,Italic", "Arial-Italic", "Arial-ItalicMT"}, new String[]{"Helvetica-Bold", "Helvetica,Bold", "Arial,Bold", "Arial-Bold", "Arial-BoldMT", "NimbusSanL-Bold", "Nimbus Sans L"}, new String[]{"Helvetica-BoldOblique", "NimbusSanL-BoldItal", "Helvetica-BlackOblique", "Nimbus Sans L", "Helvetica,BoldItalic", "Helvetica-BoldItalic", "Arial,BoldItalic", "Arial-BoldItalic", "Arial-BoldItalicMT"}, new String[]{"Helvetica-Black", "Helvetica,Bold", "Arial,Bold", "Arial-Bold", "Arial-BoldMT", "NimbusSanL-Bold", "Nimbus Sans L"}, new String[]{"Helvetica-BlackOblique", "NimbusSanL-BoldItal", "Helvetica-BlackOblique", "Nimbus Sans L", "Helvetica,BoldItalic", "Helvetica-BoldItalic", "Arial,BoldItalic", "Arial-BoldItalic", "Arial-BoldItalicMT"}, new String[]{"Helvetica-Narrow", "NimbusSanL-ReguCond", "Nimbus Sans L"}, new String[]{"Helvetica-Narrow-Oblique", "NimbusSanL-ReguCondItal", "Nimbus Sans L"}, new String[]{"Helvetica-Narrow-Bold", "NimbusSanL-BoldCond", "Nimbus Sans L"}, new String[]{"Helvetica-Narrow-BoldOblique", "NimbusSanL-BoldCondItal", "Nimbus Sans L"}, new String[]{"Helvetica-Condensed", "NimbusSanL-ReguCond", "Nimbus Sans L"}, new String[]{"Helvetica-Condensed-Oblique", "NimbusSanL-ReguCondItal", "Nimbus Sans L"}, new String[]{"Helvetica-Condensed-Bold", "NimbusSanL-BoldCond", "Nimbus Sans L"}, new String[]{"Helvetica-Condensed-BoldOblique", "NimbusSanL-BoldCondItal", "Nimbus Sans L"}, new String[]{"Palatino-Roman", "URWPalladioL-Roma", "Arial"}, new String[]{"Palatino-Italic", "URWPalladioL-Ital", "Arial"}, new String[]{"Palatino-Bold", "URWPalladioL-Bold", "Arial"}, new String[]{"Palatino-BoldItalic", "URWPalladioL-BoldItal", "Arial"}, new String[]{"NewCenturySchlbk-Roman", "CenturySchL-Roma", "Arial"}, new String[]{"NewCenturySchlbk-Italic", "CenturySchL-Ital", "Arial"}, new String[]{"NewCenturySchlbk-Bold", "CenturySchL-Bold", "Arial"}, new String[]{"NewCenturySchlbk-BoldItalic", "CenturySchL-BoldItal", "Arial"}, new String[]{"Times-Roman", "NimbusRomNo9L-Regu", "Nimbus Roman No9 L", "TimesNewRoman", "TimesNewRomanPSMT", "TimesNewRomanPS"}, new String[]{"Times-Italic", "NimbusRomNo9L-ReguItal", "Nimbus Roman No9 L", "TimesNewRoman,Italic", "TimesNewRoman-Italic", "TimesNewRomanPS-Italic", "TimesNewRomanPS-ItalicMT"}, new String[]{"Times-Bold", "NimbusRomNo9L-Medi", "Nimbus Roman No9 L", "TimesNewRoman,Bold", "TimesNewRoman-Bold", "TimesNewRomanPS-Bold", "TimesNewRomanPS-BoldMT"}, new String[]{"Times-BoldItalic", "NimbusRomNo9L-MediItal", "Nimbus Roman No9 L", "TimesNewRoman,BoldItalic", "TimesNewRoman-BoldItalic", "TimesNewRomanPS-BoldItalic", "TimesNewRomanPS-BoldItalicMT"}, new String[]{"Symbol", "StandardSymL", "Standard Symbols L"}, new String[]{"ZapfChancery-MediumItalic", "URWChanceryL-MediItal", "Arial"}, new String[]{"ZapfDingbats", "Dingbats", "Zapf-Dingbats"}};
    private static final String[] JAPANESE_FONT_NAMES = {"Arial Unicode MS", "PMingLiU", "MingLiU", "MS PMincho", "MS Mincho", "Kochi Mincho", "Hiragino Mincho Pro", "KozMinPro Regular Acro", "HeiseiMin W3 Acro", "Adobe Ming Std Acro"};
    private static final String[] CHINESE_SIMPLIFIED_FONT_NAMES = {"Arial Unicode MS", "PMingLiU", "MingLiU", "SimSun", "NSimSun", "Kochi Mincho", "STFangsong", "STSong Light Acro", "Adobe Song Std Acro"};
    private static final String[] CHINESE_TRADITIONAL_FONT_NAMES = {"Arial Unicode MS", "PMingLiU", "MingLiU", "SimSun", "NSimSun", "Kochi Mincho", "BiauKai", "MSungStd Light Acro", "Adobe Song Std Acro"};
    private static final String[] KOREAN_FONT_NAMES = {"Arial Unicode MS", "Gulim", "Batang", "BatangChe", "HYSMyeongJoStd Medium Acro", "Adobe Myungjo Std Acro"};
    private static String JAVA_FONT_PATHS = Defs.sysProperty("java.home") + "/lib/fonts";
    private static String[] SYSTEM_FONT_PATHS = {System.getenv("WINDIR") + "\\Fonts", "c:\\cygwin\\usr\\share\\ghostscript\\fonts\\", "d:\\cygwin\\usr\\share\\ghostscript\\fonts\\", Defs.sysProperty("user.home") + "/Library/Fonts/", "/Library/Fonts/", "/Network/Library/Fonts/", "/System/Library/Fonts/", "/System Folder/Fonts", "/usr/local/share/ghostscript/", "/Applications/GarageBand.app/Contents/Resources/", "/Applications/NeoOffice.app/Contents/share/fonts/truetype/", "/Library/Dictionaries/Shogakukan Daijisen.dictionary/Contents/", "/Library/Dictionaries/Shogakukan Progressive English-Japanese Japanese-English Dictionary.dictionary/Contents/", "/Library/Dictionaries/Shogakukan Ruigo Reikai Jiten.dictionary/Contents/", "/Library/Fonts/", "/Volumes/Untitled/WINDOWS/Fonts/", "/usr/share/enscript/", "/usr/share/groff/1.19.2/font/devps/generate/", "/usr/X11/lib/X11/fonts/Type1/", "/usr/X11/lib/X11/fonts/TrueType/", "/usr/X11/lib/X11/fonts/", "/etc/fonts/", System.getProperty("user.home") + "/.fonts/", "/system/etc/fonts/", "/usr/lib/X11/fonts", "/usr/share/a2ps/afm/", "/usr/share/enscript/afm/", "/usr/share/fonts/local/", "/usr/share/fonts/truetype/", "/usr/share/fonts/truetype/freefont/", "/usr/share/fonts/truetype/msttcorefonts/", "/usr/share/fonts/Type1/", "/usr/share/fonts/type1/gsfonts/", "/usr/share/fonts/X11/Type1/", "/usr/share/ghostscript/fonts/", "/usr/share/groff/1.18.1/font/devps/", "/usr/share/groff/1.18.1/font/devps/generate/", "/usr/share/libwmf/fonts/", "/usr/share/ogonkify/afm/", "/usr/X11R6/lib/X11/fonts/", "/var/lib/defoma/gs.d/dirs/fonts/", "/usr/openwin/lib/locale/ar/X11/fonts/TrueType/", "/usr/openwin/lib/locale/euro_fonts/X11/fonts/TrueType/", "/usr/openwin/lib/locale/hi_IN.UTF-8/X11/fonts/TrueType/", "/usr/openwin/lib/locale/iso_8859_13/X11/fonts/TrueType/", "/usr/openwin/lib/locale/iso_8859_15/X11/fonts/TrueType/", "/usr/openwin/lib/locale/iso_8859_2/X11/fonts/TrueType/", "/usr/openwin/lib/locale/iso_8859_2/X11/fonts/Type1/afm/", "/usr/openwin/lib/locale/iso_8859_4/X11/fonts/Type1/afm/", "/usr/openwin/lib/locale/iso_8859_5/X11/fonts/TrueType/", "/usr/openwin/lib/locale/iso_8859_5/X11/fonts/Type1/afm/", "/usr/openwin/lib/locale/iso_8859_7/X11/fonts/TrueType/", "/usr/openwin/lib/locale/iso_8859_7/X11/fonts/Type1/afm/", "/usr/openwin/lib/locale/iso_8859_8/X11/fonts/TrueType/", "/usr/openwin/lib/locale/iso_8859_8/X11/fonts/Type1/", "/usr/openwin/lib/locale/iso_8859_8/X11/fonts/Type1/afm/", "/usr/openwin/lib/locale/iso_8859_9/X11/fonts/TrueType/", "/usr/openwin/lib/locale/iso_8859_9/X11/fonts/Type1/afm/", "/usr/openwin/lib/locale/ja//X11/fonts/TrueType/", "/usr/openwin/lib/locale/K0I8-R/X11/fonts/TrueType/", "/usr/openwin/lib/locale/ru.ansi-1251/X11/fonts/TrueType/", "/usr/openwin/lib/locale/th_TH/X11/fonts/TrueType/", "/usr/openwin/lib/locale/zh.GBK/X11/fonts/TrueType/", "/usr/openwin/lib/locale/zh/X11/fonts/TrueType/", "/usr/openwin/lib/locale/zh_CN.GB18030/X11/fonts/TrueType/", "/usr/openwin/lib/locale/zh_TW.BIG5/X11/fonts/TrueType/", "/usr/openwin/lib/locale/zh_TW/X11/fonts/TrueType/", "/usr/openwin/lib/X11/fonts/", "/usr/openwin/lib/X11/fonts/F3/afm/", "/usr/openwin/lib/X11/fonts/misc/", "/usr/openwin/lib/X11/fonts/TrueType/", "/usr/openwin/lib/X11/fonts/Type1/", "/usr/openwin/lib/X11/fonts/Type1/afm/", "/usr/openwin/lib/X11/fonts/Type1/outline/", "/usr/openwin/lib/X11/fonts/Type1/sun/", "/usr/openwin/lib/X11/fonts/Type1/sun/afm/", "/usr/sfw/share/a2ps/afm/", "/usr/sfw/share/ghostscript/fonts/", "/usr/sfw/share/ghostscript/fonts/"};
    private static String baseFontName = Defs.property("org.icepdf.core.font.basefont", "lucidasans");

    public static FontManager getInstance() {
        if (fontManager == null) {
            fontManager = new FontManager();
        }
        return fontManager;
    }

    public Properties getFontProperties() {
        if (fontList == null) {
            readSystemFonts(null);
        }
        Properties fontProperites = new Properties();
        for (Object[] currentFont : fontList) {
            String name = (String) currentFont[0];
            String family = (String) currentFont[1];
            Integer decorations = (Integer) currentFont[2];
            String path = (String) currentFont[3];
            fontProperites.put(name, family + CallSiteDescriptor.OPERATOR_DELIMITER + ((Object) decorations) + CallSiteDescriptor.OPERATOR_DELIMITER + path);
        }
        return fontProperites;
    }

    public void setFontProperties(Properties fontProperties) throws IllegalArgumentException {
        try {
            fontList = new ArrayList(150);
            Enumeration fonts = fontProperties.propertyNames();
            while (fonts.hasMoreElements()) {
                String name = (String) fonts.nextElement2();
                StringTokenizer tokens = new StringTokenizer((String) fontProperties.get(name), CallSiteDescriptor.OPERATOR_DELIMITER);
                String family = tokens.nextToken();
                Integer decorations = new Integer(tokens.nextToken());
                String path = tokens.nextToken();
                if (name != null && family != null && path != null) {
                    fontList.add(new Object[]{name, family, decorations, path});
                } else {
                    throw new IllegalArgumentException("Error parsing font properties ");
                }
            }
            sortFontListByName();
        } catch (Throwable e2) {
            logger.log(Level.FINE, "Error setting font properties ", e2);
            throw new IllegalArgumentException("Error parsing font properties ");
        }
    }

    public void clearFontList() {
        if (fontList != null) {
            fontList.clear();
        }
    }

    public synchronized void readSystemFonts(String[] extraFontPaths) {
        String[] fontDirectories;
        if (fontList == null) {
            fontList = new ArrayList(150);
        }
        if (extraFontPaths == null) {
            fontDirectories = SYSTEM_FONT_PATHS;
        } else {
            int length = SYSTEM_FONT_PATHS.length + extraFontPaths.length;
            fontDirectories = new String[length];
            System.arraycopy(SYSTEM_FONT_PATHS, 0, fontDirectories, 0, SYSTEM_FONT_PATHS.length);
            System.arraycopy(extraFontPaths, 0, fontDirectories, SYSTEM_FONT_PATHS.length, extraFontPaths.length);
        }
        if (logger.isLoggable(Level.FINER)) {
            logger.finer("Reading system fonts:");
        }
        for (int i2 = fontDirectories.length - 1; i2 >= 0; i2--) {
            String path = fontDirectories[i2];
            if (path != null) {
                loadSystemFont(new File(path));
            }
        }
        loadSystemFont(new File(JAVA_FONT_PATHS));
        sortFontListByName();
    }

    private void loadSystemFont(File directory) {
        if (directory.canRead()) {
            String[] fontPaths = directory.list();
            for (int j2 = fontPaths.length - 1; j2 >= 0; j2--) {
                String fontName = fontPaths[j2];
                StringBuilder fontPath = new StringBuilder(25);
                fontPath.append(directory.getAbsolutePath()).append(File.separatorChar).append(fontName);
                if (logger.isLoggable(Level.FINER)) {
                    logger.finer("Trying to load font file: " + ((Object) fontPath));
                }
                FontFile font = buildFont(fontPath.toString());
                if (font != null) {
                    String fontName2 = font.getName().toLowerCase();
                    fontList.add(new Object[]{font.getName().toLowerCase(), FontUtil.normalizeString(font.getFamily()), Integer.valueOf(guessFontStyle(fontName2)), fontPath.toString()});
                    if (logger.isLoggable(Level.FINER)) {
                        logger.finer("Adding system font: " + font.getName() + " " + fontPath.toString());
                    }
                }
            }
        }
    }

    public static String guessFamily(String name) {
        String fam = name;
        int inx = fam.indexOf(44);
        if (inx > 0) {
            fam = fam.substring(0, inx);
        }
        int inx2 = fam.lastIndexOf(45);
        if (inx2 > 0) {
            fam = fam.substring(0, inx2);
        }
        return fam;
    }

    public String[] getAvailableNames() {
        if (fontList != null) {
            String[] availableNames = new String[fontList.size()];
            int i2 = 0;
            for (Object[] fontData : fontList) {
                availableNames[i2] = fontData[0].toString();
                i2++;
            }
            return availableNames;
        }
        return null;
    }

    public String[] getAvailableFamilies() {
        if (fontList != null) {
            String[] availableNames = new String[fontList.size()];
            int i2 = 0;
            for (Object[] fontData : fontList) {
                availableNames[i2] = fontData[1].toString();
                i2++;
            }
            return availableNames;
        }
        return null;
    }

    public String[] getAvailableStyle() {
        if (fontList != null) {
            String[] availableStyles = new String[fontList.size()];
            String style = "";
            int i2 = 0;
            for (Object[] fontData : fontList) {
                int decorations = ((Integer) fontData[2]).intValue();
                if ((decorations & BOLD_ITALIC) == BOLD_ITALIC) {
                    style = style + " BoldItalic";
                } else if ((decorations & BOLD) == BOLD) {
                    style = style + " Bold";
                } else if ((decorations & ITALIC) == ITALIC) {
                    style = style + " Italic";
                } else if ((decorations & PLAIN) == PLAIN) {
                    style = style + " Plain";
                }
                availableStyles[i2] = style;
                style = "";
                i2++;
            }
            return availableStyles;
        }
        return null;
    }

    public FontFile getJapaneseInstance(String name, int fontFlags) {
        return getAsianInstance(fontList, name, JAPANESE_FONT_NAMES, fontFlags);
    }

    public FontFile getKoreanInstance(String name, int fontFlags) {
        return getAsianInstance(fontList, name, KOREAN_FONT_NAMES, fontFlags);
    }

    public FontFile getChineseTraditionalInstance(String name, int fontFlags) {
        return getAsianInstance(fontList, name, CHINESE_TRADITIONAL_FONT_NAMES, fontFlags);
    }

    public FontFile getChineseSimplifiedInstance(String name, int fontFlags) {
        return getAsianInstance(fontList, name, CHINESE_SIMPLIFIED_FONT_NAMES, fontFlags);
    }

    private FontFile getAsianInstance(List<Object[]> fontList2, String name, String[] list, int flags) {
        if (fontList2 == null) {
            readSystemFonts(null);
        }
        FontFile font = null;
        if (list != null) {
            for (int i2 = list.length - 1; i2 >= 0; i2--) {
                font = findFont(fontList2, name, flags);
                if (font != null) {
                    if (logger.isLoggable(Level.FINER)) {
                        logger.finer("Font Substitution: Found Asian font: " + font.getName() + " for named font " + name);
                    }
                    return font;
                }
            }
            for (int i3 = list.length - 1; i3 >= 0; i3--) {
                font = findFont(fontList2, list[i3], flags);
                if (font != null) {
                    if (logger.isLoggable(Level.FINER)) {
                        logger.finer("Font Substitution: Found Asian font: " + font.getName() + " for named font " + name);
                    }
                    return font;
                }
            }
        }
        return font;
    }

    public void readFontPackage(String fontResourcePackage, List<String> resources) {
        if (fontJarList == null) {
            fontJarList = new ArrayList(35);
        }
        for (String resourceName : resources) {
            URL resourcePath = FontManager.class.getResource("/" + fontResourcePackage + "/" + resourceName);
            FontFile font = buildFont(resourcePath);
            if (font != null) {
                String fontName = font.getName().toLowerCase();
                fontJarList.add(new Object[]{font.getName().toLowerCase(), FontUtil.normalizeString(font.getFamily()), Integer.valueOf(guessFontStyle(fontName)), resourcePath.toString()});
                if (logger.isLoggable(Level.FINER)) {
                    logger.finer("Adding system font: " + font.getName() + " " + resourcePath.toString());
                }
            }
        }
    }

    public FontFile getInstance(String name, int flags) {
        FontFile font;
        FontFile font2;
        if (fontList == null) {
            readSystemFonts(null);
        }
        if (fontJarList != null && (font2 = getType1Fonts(fontJarList, name, flags)) != null) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Font Substitution: Found type1 font: " + font2.getName() + " for named font " + name);
            }
            return font2;
        }
        FontFile font3 = getType1Fonts(fontList, name, flags);
        if (font3 != null) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Font Substitution: Found type1 font: " + font3.getName() + " for named font " + name);
            }
            return font3;
        }
        if (fontJarList != null && (font = findFont(fontJarList, name, flags)) != null) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Font Substitution: Found type1 font: " + font.getName() + " for named font " + name);
            }
            return font;
        }
        FontFile font4 = findFont(fontList, name, flags);
        if (font4 != null) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Font Substitution: Found system font: " + font4.getName() + " for named font " + name);
            }
            return font4;
        }
        FontFile font5 = getCoreJavaFont(name, flags);
        if (font5 != null) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Font Substitution: Found java font: " + font5.getName() + " for named font " + name);
            }
            return font5;
        }
        if (fontList.size() > 0) {
            boolean found = false;
            int decorations = guessFontStyle(name);
            int i2 = fontList.size() - 1;
            while (true) {
                if (i2 < 0) {
                    break;
                }
                Object[] fontData = fontList.get(i2);
                int style = ((Integer) fontData[2]).intValue();
                if ((decorations & BOLD_ITALIC) == BOLD_ITALIC && (style & BOLD_ITALIC) == BOLD_ITALIC) {
                    found = true;
                } else if ((decorations & BOLD) == BOLD && (style & BOLD) == BOLD) {
                    found = true;
                } else if ((decorations & ITALIC) == ITALIC && (style & ITALIC) == ITALIC) {
                    found = true;
                } else if ((decorations & PLAIN) == PLAIN && (style & PLAIN) == PLAIN) {
                    found = true;
                }
                if (!found) {
                    i2--;
                } else {
                    font5 = buildFont((String) fontData[3]);
                    break;
                }
            }
            if (!found) {
                font5 = buildFont((String) fontList.get(0)[3]);
            }
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Font Substitution: Found failed " + name + " " + font5.getName());
            }
        }
        if (font5 == null && logger.isLoggable(Level.FINE)) {
            logger.fine("No Fonts can be found on your system. ");
        }
        return font5;
    }

    private FontFile findFont(List<Object[]> fontList2, String fontName, int flags) {
        FontFile font = null;
        int decorations = guessFontStyle(fontName);
        String name = FontUtil.normalizeString(fontName);
        if (fontList2 != null) {
            for (int i2 = fontList2.size() - 1; i2 >= 0; i2--) {
                Object[] fontData = fontList2.get(i2);
                String baseName = (String) fontData[0];
                String familyName = (String) fontData[1];
                String path = (String) fontData[3];
                if (logger.isLoggable(Level.FINEST)) {
                    logger.finest(baseName + " : " + familyName + "  : " + name);
                }
                if (name.contains(familyName) || fontName.toLowerCase().contains(baseName)) {
                    int style = ((Integer) fontData[2]).intValue();
                    boolean found = false;
                    if (!baseName.equals("opensymbol") && !baseName.equals("starsymbol") && !baseName.equals("arial-black") && !baseName.equals("arial-blackitalic") && !baseName.equals("new") && !baseName.equals("timesnewromanps")) {
                        if ((decorations & BOLD_ITALIC) == BOLD_ITALIC && (style & BOLD_ITALIC) == BOLD_ITALIC) {
                            found = true;
                        } else if ((decorations & BOLD) == BOLD && (style & BOLD) == BOLD) {
                            found = true;
                        } else if ((decorations & ITALIC) == ITALIC && (style & ITALIC) == ITALIC) {
                            found = true;
                        } else if (((decorations & PLAIN) == PLAIN && (style & PLAIN) == PLAIN) || baseName.contains("wingdings") || baseName.contains("zapfdingbats") || baseName.contains("dingbats") || baseName.contains("symbol")) {
                            found = true;
                        }
                    }
                    if (found) {
                        if (logger.isLoggable(Level.FINER)) {
                            logger.finer("----> Found font: " + baseName + " family: " + getFontStyle(style, 0) + " for: " + fontName + " " + path);
                        }
                        font = buildFont((String) fontData[3]);
                        if (font != null) {
                            break;
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
        return font;
    }

    private FontFile buildFont(String fontPath) {
        FontFile font = null;
        try {
            if (fontPath.startsWith("jar:file")) {
                font = buildFont(new URL(fontPath));
            } else {
                File file = new File(fontPath);
                if (!file.canRead()) {
                    return null;
                }
                font = buildFont(file);
            }
        } catch (Throwable e2) {
            logger.log(Level.FINE, "Error reading font program.", e2);
        }
        return font;
    }

    private FontFile buildFont(File fontFile) {
        String fontPath = fontFile.getPath();
        FontFactory fontFactory = FontFactory.getInstance();
        FontFile font = null;
        if (fontPath.endsWith(".ttf") || fontPath.endsWith(".TTF") || fontPath.endsWith(".dfont") || fontPath.endsWith(".DFONT") || fontPath.endsWith(".ttc") || fontPath.endsWith(".TTC")) {
            font = fontFactory.createFontFile(fontFile, 0);
        } else if (fontPath.endsWith(".pfa") || fontPath.endsWith(".PFA") || fontPath.endsWith(".pfb") || fontPath.endsWith(".PFB")) {
            font = fontFactory.createFontFile(fontFile, 1);
        } else if (fontPath.endsWith(".otf") || fontPath.endsWith(".OTF") || fontPath.endsWith(".otc") || fontPath.endsWith(".OTC")) {
            font = fontFactory.createFontFile(fontFile, 5);
        }
        return font;
    }

    private FontFile buildFont(URL fontUri) {
        FontFile font = null;
        try {
            String fontPath = fontUri.getPath();
            FontFactory fontFactory = FontFactory.getInstance();
            if (fontPath.endsWith(".ttf") || fontPath.endsWith(".TTF") || fontPath.endsWith(".dfont") || fontPath.endsWith(".DFONT") || fontPath.endsWith(".ttc") || fontPath.endsWith(".TTC")) {
                font = fontFactory.createFontFile(fontUri, 0);
            } else if (fontPath.endsWith(".pfa") || fontPath.endsWith(".PFA") || fontPath.endsWith(".pfb") || fontPath.endsWith(".PFB")) {
                font = fontFactory.createFontFile(fontUri, 1);
            } else if (fontPath.endsWith(".otf") || fontPath.endsWith(".OTF") || fontPath.endsWith(".otc") || fontPath.endsWith(".OTC")) {
                font = fontFactory.createFontFile(fontUri, 5);
            }
        } catch (Throwable e2) {
            logger.log(Level.FINE, "Error reading font program.", e2);
        }
        return font;
    }

    private FontFile getCoreJavaFont(String fontName, int flags) {
        FontFile font;
        int decorations = guessFontStyle(fontName);
        String fontName2 = FontUtil.normalizeString(fontName);
        if (fontName2.contains("timesnewroman") || fontName2.contains("bodoni") || fontName2.contains("garamond") || fontName2.contains("minionweb") || fontName2.contains("stoneserif") || fontName2.contains("georgia") || fontName2.contains("bitstreamcyberbit")) {
            font = findFont(fontList, "lucidabright-" + getFontStyle(decorations, flags), 0);
        } else if (fontName2.contains("helvetica") || fontName2.contains("arial") || fontName2.contains("trebuchet") || fontName2.contains("avantgardegothic") || fontName2.contains("verdana") || fontName2.contains("univers") || fontName2.contains("futura") || fontName2.contains("stonesans") || fontName2.contains("gillsans") || fontName2.contains("akzidenz") || fontName2.contains("frutiger") || fontName2.contains("grotesk")) {
            font = findFont(fontList, baseFontName + LanguageTag.SEP + getFontStyle(decorations, flags), 0);
        } else if (fontName2.contains("courier") || fontName2.contains("couriernew") || fontName2.contains("prestige") || fontName2.contains("eversonmono")) {
            font = findFont(fontList, baseFontName + "typewriter-" + getFontStyle(decorations, flags), 0);
        } else {
            font = findFont(fontList, "lucidabright-" + getFontStyle(decorations, flags), 0);
        }
        return font;
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x0077 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x007d A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private org.icepdf.core.pobjects.fonts.FontFile getType1Fonts(java.util.List<java.lang.Object[]> r6, java.lang.String r7, int r8) {
        /*
            r5 = this;
            r0 = 0
            r9 = r0
            r0 = 0
            r10 = r0
            r0 = 1
            r11 = r0
            java.lang.String[][] r0 = org.icepdf.core.pobjects.fonts.FontManager.TYPE1_FONT_DIFFS
            r12 = r0
            r0 = r12
            int r0 = r0.length
            r13 = r0
            r0 = 0
            r14 = r0
        L16:
            r0 = r14
            r1 = r13
            if (r0 >= r1) goto L91
            r0 = r12
            r1 = r14
            r0 = r0[r1]
            r15 = r0
            r0 = r15
            r16 = r0
            r0 = r16
            int r0 = r0.length
            r17 = r0
            r0 = 0
            r18 = r0
        L30:
            r0 = r18
            r1 = r17
            if (r0 >= r1) goto L83
            r0 = r16
            r1 = r18
            r0 = r0[r1]
            r19 = r0
            r0 = r15
            r1 = 0
            r0 = r0[r1]
            r1 = r7
            boolean r0 = r0.contains(r1)
            if (r0 == 0) goto L7d
            r0 = r11
            if (r0 == 0) goto L68
            r0 = r5
            r1 = r6
            r2 = r15
            r3 = 1
            r2 = r2[r3]
            r3 = r8
            org.icepdf.core.pobjects.fonts.FontFile r0 = r0.findFont(r1, r2, r3)
            r9 = r0
            r0 = r9
            if (r0 == 0) goto L65
            r0 = 1
            r10 = r0
            goto L83
        L65:
            r0 = 0
            r11 = r0
        L68:
            r0 = r5
            r1 = r6
            r2 = r19
            r3 = r8
            org.icepdf.core.pobjects.fonts.FontFile r0 = r0.findFont(r1, r2, r3)
            r9 = r0
            r0 = r9
            if (r0 == 0) goto L7d
            r0 = 1
            r10 = r0
            goto L83
        L7d:
            int r18 = r18 + 1
            goto L30
        L83:
            r0 = r10
            if (r0 == 0) goto L8b
            goto L91
        L8b:
            int r14 = r14 + 1
            goto L16
        L91:
            r0 = r9
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.icepdf.core.pobjects.fonts.FontManager.getType1Fonts(java.util.List, java.lang.String, int):org.icepdf.core.pobjects.fonts.FontFile");
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x006e A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0074 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.awt.Font getType1AWTFont(java.lang.String r5, int r6) {
        /*
            r4 = this;
            r0 = 0
            r7 = r0
            r0 = 0
            r8 = r0
            r0 = 1
            r9 = r0
            java.lang.String[][] r0 = org.icepdf.core.pobjects.fonts.FontManager.TYPE1_FONT_DIFFS
            r10 = r0
            r0 = r10
            int r0 = r0.length
            r11 = r0
            r0 = 0
            r12 = r0
        L15:
            r0 = r12
            r1 = r11
            if (r0 >= r1) goto L88
            r0 = r10
            r1 = r12
            r0 = r0[r1]
            r13 = r0
            r0 = r13
            r14 = r0
            r0 = r14
            int r0 = r0.length
            r15 = r0
            r0 = 0
            r16 = r0
        L2f:
            r0 = r16
            r1 = r15
            if (r0 >= r1) goto L7a
            r0 = r14
            r1 = r16
            r0 = r0[r1]
            r17 = r0
            r0 = r13
            r1 = 0
            r0 = r0[r1]
            r1 = r5
            boolean r0 = r0.contains(r1)
            if (r0 == 0) goto L74
            r0 = r9
            if (r0 == 0) goto L63
            r0 = r4
            r1 = r13
            r2 = 1
            r1 = r1[r2]
            java.awt.Font r0 = r0.findAWTFont(r1)
            r7 = r0
            r0 = r7
            if (r0 == 0) goto L60
            r0 = 1
            r8 = r0
            goto L7a
        L60:
            r0 = 0
            r9 = r0
        L63:
            r0 = r4
            r1 = r17
            java.awt.Font r0 = r0.findAWTFont(r1)
            r7 = r0
            r0 = r7
            if (r0 == 0) goto L74
            r0 = 1
            r8 = r0
            goto L7a
        L74:
            int r16 = r16 + 1
            goto L2f
        L7a:
            r0 = r8
            if (r0 == 0) goto L82
            goto L88
        L82:
            int r12 = r12 + 1
            goto L15
        L88:
            r0 = r7
            if (r0 == 0) goto L93
            r0 = r7
            r1 = r6
            float r1 = (float) r1
            java.awt.Font r0 = r0.deriveFont(r1)
            r7 = r0
        L93:
            r0 = r7
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.icepdf.core.pobjects.fonts.FontManager.getType1AWTFont(java.lang.String, int):java.awt.Font");
    }

    private java.awt.Font findAWTFont(String fontName) {
        java.awt.Font font = null;
        int decorations = guessFontStyle(fontName);
        String name = FontUtil.normalizeString(fontName);
        if (fontList != null) {
            for (int i2 = fontList.size() - 1; i2 >= 0; i2--) {
                Object[] fontData = fontList.get(i2);
                String baseName = (String) fontData[0];
                String familyName = (String) fontData[1];
                if (logger.isLoggable(Level.FINEST)) {
                    logger.finest(baseName + " : " + familyName + "  : " + name);
                }
                if (name.contains(familyName) || fontName.toLowerCase().contains(baseName)) {
                    int style = ((Integer) fontData[2]).intValue();
                    boolean found = false;
                    if (!baseName.equals("opensymbol") && !baseName.equals("starsymbol") && !baseName.equals("arial-black") && !baseName.equals("arial-blackitalic") && !baseName.equals("new") && !baseName.equals("timesnewromanps")) {
                        if ((decorations & BOLD_ITALIC) == BOLD_ITALIC && (style & BOLD_ITALIC) == BOLD_ITALIC) {
                            found = true;
                        } else if ((decorations & BOLD) == BOLD && (style & BOLD) == BOLD) {
                            found = true;
                        } else if ((decorations & ITALIC) == ITALIC && (style & ITALIC) == ITALIC) {
                            found = true;
                        } else if (((decorations & PLAIN) == PLAIN && (style & PLAIN) == PLAIN) || baseName.contains("wingdings") || baseName.contains("zapfdingbats") || baseName.contains("symbol")) {
                            found = true;
                        }
                    }
                    if (found) {
                        if (logger.isLoggable(Level.FINER)) {
                            logger.finer("----> Found font: " + baseName + " family: " + getFontStyle(style, 0) + " for: " + fontName);
                        }
                        try {
                            font = java.awt.Font.createFont(0, new File((String) fontData[3]));
                        } catch (FontFormatException e2) {
                            logger.log(Level.FINE, "Error create new font", (Throwable) e2);
                        } catch (IOException e3) {
                            logger.log(Level.FINE, "Error reading font", (Throwable) e3);
                        }
                        if (font != null) {
                            break;
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
        return font;
    }

    private static int guessFontStyle(String name) {
        int decorations;
        String name2 = name.toLowerCase();
        if (name2.indexOf("boldital") > 0 || name2.indexOf("demiital") > 0) {
            decorations = 0 | BOLD_ITALIC;
        } else if (name2.indexOf("bold") > 0 || name2.indexOf("black") > 0 || name2.indexOf("demi") > 0) {
            decorations = 0 | BOLD;
        } else if (name2.indexOf("ital") > 0 || name2.indexOf("obli") > 0) {
            decorations = 0 | ITALIC;
        } else {
            decorations = 0 | PLAIN;
        }
        return decorations;
    }

    private String getFontStyle(int sytle, int flags) {
        String style = "";
        if ((sytle & BOLD_ITALIC) == BOLD_ITALIC) {
            style = style + " BoldItalic";
        } else if ((sytle & BOLD) == BOLD) {
            style = style + " Bold";
        } else if ((sytle & ITALIC) == ITALIC) {
            style = style + " Italic";
        } else if ((sytle & PLAIN) == PLAIN) {
            style = style + " Plain";
        }
        return style;
    }

    private static void sortFontListByName() {
        Collections.sort(fontList, new Comparator<Object[]>() { // from class: org.icepdf.core.pobjects.fonts.FontManager.1
            @Override // java.util.Comparator
            public int compare(Object[] o1, Object[] o2) {
                return ((String) o2[0]).compareTo((String) o1[0]);
            }
        });
    }
}
