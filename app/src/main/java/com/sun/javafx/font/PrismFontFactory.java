package com.sun.javafx.font;

import com.sun.glass.ui.Screen;
import com.sun.glass.utils.NativeLibLoader;
import com.sun.istack.internal.localization.Localizable;
import com.sun.javafx.PlatformUtil;
import com.sun.javafx.font.FontFileWriter;
import com.sun.javafx.text.GlyphLayout;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.AllPermission;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: jfxrt.jar:com/sun/javafx/font/PrismFontFactory.class */
public abstract class PrismFontFactory implements FontFactory {
    public static final boolean debugFonts;
    public static final int cacheLayoutSize;
    private static int subPixelMode;
    public static final int SUB_PIXEL_ON = 1;
    public static final int SUB_PIXEL_Y = 2;
    public static final int SUB_PIXEL_NATIVE = 4;
    private static boolean lcdEnabled;
    private static String jreFontDir;
    private static final String jreDefaultFont = "Lucida Sans Regular";
    private static final String jreDefaultFontLC = "lucida sans regular";
    private static final String jreDefaultFontFile = "LucidaSansRegular.ttf";
    private static final String CT_FACTORY = "com.sun.javafx.font.coretext.CTFactory";
    private static final String DW_FACTORY = "com.sun.javafx.font.directwrite.DWFactory";
    private static final String FT_FACTORY = "com.sun.javafx.font.freetype.FTFactory";
    private ArrayList<WeakReference<PrismFontFile>> tmpFonts;
    private static ArrayList<String> allFamilyNames;
    private static ArrayList<String> allFontNames;
    private HashMap<String, PrismFontFile> embeddedFonts;
    private static float systemFontSize;
    private static float fontSizeLimit = 80.0f;
    private static float lcdContrast = -1.0f;
    public static final boolean isWindows = PlatformUtil.isWindows();
    public static final boolean isMacOSX = PlatformUtil.isMac();
    public static final boolean isLinux = PlatformUtil.isLinux();
    public static final boolean isIOS = PlatformUtil.isIOS();
    public static final boolean isAndroid = PlatformUtil.isAndroid();
    public static final boolean isEmbedded = PlatformUtil.isEmbedded();
    private static PrismFontFactory theFontFactory = null;
    private static final String[] STR_ARRAY = new String[0];
    private static String sysFontDir = null;
    private static String userFontDir = null;
    private static Thread fileCloser = null;
    private static String systemFontFamily = null;
    private static String monospaceFontFamily = null;
    HashMap<String, FontResource> fontResourceMap = new HashMap<>();
    HashMap<String, CompositeFontResource> compResourceMap = new HashMap<>();
    private HashMap<String, PrismFontFile> fileNameToFontResourceMap = new HashMap<>();
    private volatile HashMap<String, String> fontToFileMap = null;
    private HashMap<String, String> fileToFontMap = null;
    private HashMap<String, String> fontToFamilyNameMap = null;
    private HashMap<String, ArrayList<String>> familyToFontListMap = null;
    private int numEmbeddedFonts = 0;

    protected abstract PrismFontFile createFontFile(String str, String str2, int i2, boolean z2, boolean z3, boolean z4, boolean z5) throws Exception;

    public abstract GlyphLayout createGlyphLayout();

    private static native byte[] getFontPath();

    private static native String regReadFontLink(String str);

    private static native String getEUDCFontFile();

    static native void populateFontFileNameMap(HashMap<String, String> map, HashMap<String, String> map2, HashMap<String, ArrayList<String>> map3, Locale locale);

    static native int getLCDContrastWin32();

    private static native int getSystemFontSizeNative();

    private static native String getSystemFontNative();

    static native short getSystemLCID();

    static {
        int[] tempCacheLayoutSize = {65536};
        debugFonts = ((Boolean) AccessController.doPrivileged(() -> {
            NativeLibLoader.loadLibrary("javafx_font");
            String dbg = System.getProperty("prism.debugfonts", "");
            boolean debug = "true".equals(dbg);
            jreFontDir = System.getProperty("java.home", "") + File.separator + "lib" + File.separator + "fonts" + File.separator;
            String s2 = System.getProperty("com.sun.javafx.fontSize");
            systemFontSize = -1.0f;
            if (s2 != null) {
                try {
                    systemFontSize = Float.parseFloat(s2);
                } catch (NumberFormatException e2) {
                    System.err.println("Cannot parse font size '" + s2 + PdfOps.SINGLE_QUOTE_TOKEN);
                }
            }
            String s3 = System.getProperty("prism.subpixeltext", FXMLLoader.EVENT_HANDLER_PREFIX);
            if (s3.indexOf(FXMLLoader.EVENT_HANDLER_PREFIX) != -1 || s3.indexOf("true") != -1) {
                subPixelMode = 1;
            }
            if (s3.indexOf("native") != -1) {
                subPixelMode |= 5;
            }
            if (s3.indexOf("vertical") != -1) {
                subPixelMode |= 7;
            }
            String s4 = System.getProperty("prism.fontSizeLimit");
            if (s4 != null) {
                try {
                    fontSizeLimit = Float.parseFloat(s4);
                    if (fontSizeLimit <= 0.0f) {
                        fontSizeLimit = Float.POSITIVE_INFINITY;
                    }
                } catch (NumberFormatException e3) {
                    System.err.println("Cannot parse fontSizeLimit '" + s4 + PdfOps.SINGLE_QUOTE_TOKEN);
                }
            }
            boolean lcdTextOff = isIOS || isAndroid || isEmbedded;
            String defLCDProp = lcdTextOff ? "false" : "true";
            String lcdProp = System.getProperty("prism.lcdtext", defLCDProp);
            lcdEnabled = lcdProp.equals("true");
            String s5 = System.getProperty("prism.cacheLayoutSize");
            if (s5 != null) {
                try {
                    tempCacheLayoutSize[0] = Integer.parseInt(s5);
                    if (tempCacheLayoutSize[0] < 0) {
                        tempCacheLayoutSize[0] = 0;
                    }
                } catch (NumberFormatException e4) {
                    System.err.println("Cannot parse cache layout size '" + s5 + PdfOps.SINGLE_QUOTE_TOKEN);
                }
            }
            return Boolean.valueOf(debug);
        })).booleanValue();
        cacheLayoutSize = tempCacheLayoutSize[0];
    }

    private static String getNativeFactoryName() {
        if (isWindows) {
            return DW_FACTORY;
        }
        if (isMacOSX || isIOS) {
            return CT_FACTORY;
        }
        if (isLinux || isAndroid) {
            return FT_FACTORY;
        }
        return null;
    }

    public static float getFontSizeLimit() {
        return fontSizeLimit;
    }

    public static synchronized PrismFontFactory getFontFactory() {
        if (theFontFactory != null) {
            return theFontFactory;
        }
        String factoryClass = getNativeFactoryName();
        if (factoryClass == null) {
            throw new InternalError("cannot find a native font factory");
        }
        if (debugFonts) {
            System.err.println("Loading FontFactory " + factoryClass);
            if (subPixelMode != 0) {
                String s2 = "Subpixel: enabled";
                if ((subPixelMode & 2) != 0) {
                    s2 = s2 + ", vertical";
                }
                if ((subPixelMode & 4) != 0) {
                    s2 = s2 + ", native";
                }
                System.err.println(s2);
            }
        }
        theFontFactory = getFontFactory(factoryClass);
        if (theFontFactory == null) {
            throw new InternalError("cannot load font factory: " + factoryClass);
        }
        return theFontFactory;
    }

    private static synchronized PrismFontFactory getFontFactory(String factoryClass) {
        try {
            Class<?> clazz = Class.forName(factoryClass);
            Method mid = clazz.getMethod("getFactory", (Class[]) null);
            return (PrismFontFactory) mid.invoke(null, new Object[0]);
        } catch (Throwable th) {
            if (debugFonts) {
                System.err.println("Loading font factory failed " + factoryClass);
                return null;
            }
            return null;
        }
    }

    private PrismFontFile createFontResource(String filename, int index) {
        return createFontResource(filename, index, true, false, false, false);
    }

    private PrismFontFile createFontResource(String filename, int index, boolean register, boolean embedded, boolean copy, boolean tracked) {
        String key = (filename + index).toLowerCase();
        PrismFontFile fr = this.fileNameToFontResourceMap.get(key);
        if (fr != null) {
            return fr;
        }
        try {
            PrismFontFile fr2 = createFontFile(null, filename, index, register, embedded, copy, tracked);
            if (register) {
                storeInMap(fr2.getFullName(), fr2);
                this.fileNameToFontResourceMap.put(key, fr2);
            }
            return fr2;
        } catch (Exception e2) {
            if (debugFonts) {
                e2.printStackTrace();
                return null;
            }
            return null;
        }
    }

    private PrismFontFile createFontResource(String name, String filename) {
        return createFontResource(name, filename, true, false, false, false);
    }

    private PrismFontFile createFontResource(String name, String filename, boolean register, boolean embedded, boolean copy, boolean tracked) {
        PrismFontFile fr;
        if (filename == null) {
            return null;
        }
        String lcFN = filename.toLowerCase();
        if (lcFN.endsWith(".ttc")) {
            int index = 0;
            PrismFontFile namedFR = null;
            do {
                String key = (filename + index).toLowerCase();
                try {
                    fr = this.fileNameToFontResourceMap.get(key);
                    if (fr != null) {
                        if (name.equals(fr.getFullName())) {
                            return fr;
                        }
                    } else {
                        fr = createFontFile(name, filename, index, register, embedded, copy, tracked);
                        String fontname = fr.getFullName();
                        if (register) {
                            storeInMap(fontname, fr);
                            this.fileNameToFontResourceMap.put(key, fr);
                        }
                        if (index == 0 || name.equals(fontname)) {
                            namedFR = fr;
                        }
                    }
                    index++;
                } catch (Exception e2) {
                    if (debugFonts) {
                        e2.printStackTrace();
                        return null;
                    }
                    return null;
                }
            } while (index < fr.getFontCount());
            return namedFR;
        }
        return createFontResource(filename, 0, register, embedded, copy, tracked);
    }

    private String dotStyleStr(boolean bold, boolean italic) {
        if (!bold) {
            if (!italic) {
                return "";
            }
            return ".italic";
        }
        if (!italic) {
            return ".bold";
        }
        return ".bolditalic";
    }

    private void storeInMap(String name, FontResource resource) {
        if (name == null || resource == null) {
            return;
        }
        if (resource instanceof PrismCompositeFontResource) {
            System.err.println(name + " is a composite " + ((Object) resource));
            Thread.dumpStack();
        } else {
            this.fontResourceMap.put(name.toLowerCase(), resource);
        }
    }

    synchronized void addDecodedFont(PrismFontFile fr) {
        fr.setIsDecoded(true);
        addTmpFont(fr);
    }

    private synchronized void addTmpFont(PrismFontFile fr) {
        WeakReference<PrismFontFile> ref;
        if (this.tmpFonts == null) {
            this.tmpFonts = new ArrayList<>();
        }
        if (fr.isRegistered()) {
            ref = new WeakReference<>(fr);
        } else {
            ref = fr.createFileDisposer(this);
        }
        this.tmpFonts.add(ref);
        addFileCloserHook();
    }

    synchronized void removeTmpFont(WeakReference<PrismFontFile> ref) {
        if (this.tmpFonts != null) {
            this.tmpFonts.remove(ref);
        }
    }

    public synchronized FontResource getFontResource(String familyName, boolean bold, boolean italic, boolean wantComp) {
        FontResource fr;
        if (familyName == null || familyName.isEmpty()) {
            return null;
        }
        String lcFamilyName = familyName.toLowerCase();
        String styleStr = dotStyleStr(bold, italic);
        FontResource fr2 = lookupResource(lcFamilyName + styleStr, wantComp);
        if (fr2 != null) {
            return fr2;
        }
        if (this.embeddedFonts != null && wantComp) {
            FontResource fr3 = lookupResource(lcFamilyName + styleStr, false);
            if (fr3 != null) {
                return new PrismCompositeFontResource(fr3, lcFamilyName + styleStr);
            }
            for (PrismFontFile embeddedFont : this.embeddedFonts.values()) {
                String lcEmFamily = embeddedFont.getFamilyName().toLowerCase();
                if (lcEmFamily.equals(lcFamilyName)) {
                    return new PrismCompositeFontResource(embeddedFont, lcFamilyName + styleStr);
                }
            }
        }
        if (isWindows) {
            int style = (bold ? 1 : 0) + (italic ? 2 : 0);
            String fontFile = WindowsFontMap.findFontFile(lcFamilyName, style);
            if (fontFile != null) {
                FontResource fr4 = createFontResource((String) null, fontFile);
                if (fr4 != null) {
                    if (bold == fr4.isBold() && italic == fr4.isItalic() && !styleStr.isEmpty()) {
                        storeInMap(lcFamilyName + styleStr, fr4);
                    }
                    if (wantComp) {
                        fr4 = new PrismCompositeFontResource(fr4, lcFamilyName + styleStr);
                    }
                    return fr4;
                }
            }
        }
        getFullNameToFileMap();
        ArrayList<String> family = this.familyToFontListMap.get(lcFamilyName);
        if (family == null) {
            return null;
        }
        FontResource plainFR = null;
        FontResource boldFR = null;
        FontResource italicFR = null;
        FontResource boldItalicFR = null;
        Iterator<String> it = family.iterator();
        while (it.hasNext()) {
            String fontName = it.next();
            String lcFontName = fontName.toLowerCase();
            FontResource fr5 = this.fontResourceMap.get(lcFontName);
            if (fr5 == null) {
                String file = findFile(lcFontName);
                if (file != null) {
                    fr5 = getFontResource(fontName, file);
                }
                if (fr5 == null) {
                    continue;
                } else {
                    storeInMap(lcFontName, fr5);
                }
            }
            if (bold == fr5.isBold() && italic == fr5.isItalic()) {
                storeInMap(lcFamilyName + styleStr, fr5);
                if (wantComp) {
                    fr5 = new PrismCompositeFontResource(fr5, lcFamilyName + styleStr);
                }
                return fr5;
            }
            if (!fr5.isBold()) {
                if (!fr5.isItalic()) {
                    plainFR = fr5;
                } else {
                    italicFR = fr5;
                }
            } else if (!fr5.isItalic()) {
                boldFR = fr5;
            } else {
                boldItalicFR = fr5;
            }
        }
        if (!bold && !italic) {
            if (boldFR != null) {
                fr = boldFR;
            } else if (italicFR != null) {
                fr = italicFR;
            } else {
                fr = boldItalicFR;
            }
        } else if (bold && !italic) {
            if (plainFR != null) {
                fr = plainFR;
            } else if (boldItalicFR != null) {
                fr = boldItalicFR;
            } else {
                fr = italicFR;
            }
        } else if (!bold && italic) {
            if (boldItalicFR != null) {
                fr = boldItalicFR;
            } else if (plainFR != null) {
                fr = plainFR;
            } else {
                fr = boldFR;
            }
        } else if (italicFR != null) {
            fr = italicFR;
        } else if (boldFR != null) {
            fr = boldFR;
        } else {
            fr = plainFR;
        }
        if (fr != null) {
            storeInMap(lcFamilyName + styleStr, fr);
            if (wantComp) {
                fr = new PrismCompositeFontResource(fr, lcFamilyName + styleStr);
            }
        }
        return fr;
    }

    @Override // com.sun.javafx.font.FontFactory
    public synchronized PGFont createFont(String familyName, boolean bold, boolean italic, float size) {
        FontResource fr = null;
        if (familyName != null && !familyName.isEmpty()) {
            PGFont logFont = LogicalFont.getLogicalFont(familyName, bold, italic, size);
            if (logFont != null) {
                return logFont;
            }
            fr = getFontResource(familyName, bold, italic, true);
        }
        if (fr == null) {
            return LogicalFont.getLogicalFont(LogicalFont.SYSTEM, bold, italic, size);
        }
        return new PrismFont(fr, fr.getFullName(), size);
    }

    @Override // com.sun.javafx.font.FontFactory
    public synchronized PGFont createFont(String name, float size) {
        FontResource fr = null;
        if (name != null && !name.isEmpty()) {
            PGFont logFont = LogicalFont.getLogicalFont(name, size);
            if (logFont != null) {
                return logFont;
            }
            fr = getFontResource(name, null, true);
        }
        if (fr == null) {
            return LogicalFont.getLogicalFont(FontFactory.DEFAULT_FULLNAME, size);
        }
        return new PrismFont(fr, fr.getFullName(), size);
    }

    private PrismFontFile getFontResource(String name, String file) {
        PrismFontFile fr = null;
        if (isMacOSX) {
            DFontDecoder decoder = null;
            if (name != null && file.endsWith(".dfont")) {
                decoder = new DFontDecoder();
                try {
                    decoder.openFile();
                    decoder.decode(name);
                    decoder.closeFile();
                    file = decoder.getFile().getPath();
                } catch (Exception e2) {
                    file = null;
                    decoder.deleteFile();
                    decoder = null;
                    if (debugFonts) {
                        e2.printStackTrace();
                    }
                }
            }
            if (file != null) {
                fr = createFontResource(name, file);
            }
            if (decoder != null) {
                if (fr != null) {
                    addDecodedFont(fr);
                } else {
                    decoder.deleteFile();
                }
            }
        } else {
            fr = createFontResource(name, file);
        }
        return fr;
    }

    @Override // com.sun.javafx.font.FontFactory
    public synchronized PGFont deriveFont(PGFont font, boolean bold, boolean italic, float size) {
        FontResource fr = font.getFontResource();
        return new PrismFont(fr, fr.getFullName(), size);
    }

    private FontResource lookupResource(String lcName, boolean wantComp) {
        if (wantComp) {
            return this.compResourceMap.get(lcName);
        }
        return this.fontResourceMap.get(lcName);
    }

    public synchronized FontResource getFontResource(String name, String file, boolean wantComp) {
        FontResource fr;
        FontResource fr2;
        String lcName;
        String fontFile;
        if (name != null) {
            String lcName2 = name.toLowerCase();
            FontResource fontResource = lookupResource(lcName2, wantComp);
            if (fontResource != null) {
                return fontResource;
            }
            if (this.embeddedFonts != null && wantComp) {
                FontResource fr3 = lookupResource(lcName2, false);
                if (fr3 != null) {
                    fr3 = new PrismCompositeFontResource(fr3, lcName2);
                }
                if (fr3 != null) {
                    return fr3;
                }
            }
        }
        if (isWindows && name != null && (fontFile = WindowsFontMap.findFontFile((lcName = name.toLowerCase()), -1)) != null) {
            FontResource fr4 = createFontResource((String) null, fontFile);
            if (fr4 != null) {
                if (wantComp) {
                    fr4 = new PrismCompositeFontResource(fr4, lcName);
                }
                return fr4;
            }
        }
        getFullNameToFileMap();
        if (name != null && file != null) {
            FontResource fr5 = getFontResource(name, file);
            if (fr5 != null) {
                if (wantComp) {
                    fr5 = new PrismCompositeFontResource(fr5, name.toLowerCase());
                }
                return fr5;
            }
        }
        if (name != null && (fr2 = getFontResourceByFullName(name, wantComp)) != null) {
            return fr2;
        }
        if (file != null && (fr = getFontResourceByFileName(file, wantComp)) != null) {
            return fr;
        }
        return null;
    }

    boolean isInstalledFont(String fileName) {
        String fileKey;
        if (isWindows) {
            if (fileName.toLowerCase().contains("\\windows\\fonts")) {
                return true;
            }
            File f2 = new File(fileName);
            fileKey = f2.getName();
        } else {
            if (isMacOSX && fileName.toLowerCase().contains("/library/fonts")) {
                return true;
            }
            File f3 = new File(fileName);
            fileKey = f3.getPath();
        }
        getFullNameToFileMap();
        return this.fileToFontMap.get(fileKey.toLowerCase()) != null;
    }

    private synchronized FontResource getFontResourceByFileName(String file, boolean wantComp) {
        FontResource fontResource;
        String fullPath;
        if (this.fontToFileMap.size() <= 1) {
            return null;
        }
        String name = this.fileToFontMap.get(file.toLowerCase());
        if (name == null) {
            fontResource = createFontResource(file, 0);
            if (fontResource != null) {
                String lcName = fontResource.getFullName().toLowerCase();
                storeInMap(lcName, fontResource);
                if (wantComp) {
                    fontResource = new PrismCompositeFontResource(fontResource, lcName);
                }
            }
        } else {
            String lcName2 = name.toLowerCase();
            fontResource = lookupResource(lcName2, wantComp);
            if (fontResource == null && (fullPath = findFile(lcName2)) != null) {
                fontResource = getFontResource(name, fullPath);
                if (fontResource != null) {
                    storeInMap(lcName2, fontResource);
                }
                if (wantComp) {
                    fontResource = new PrismCompositeFontResource(fontResource, lcName2);
                }
            }
        }
        return fontResource;
    }

    private synchronized FontResource getFontResourceByFullName(String name, boolean wantComp) {
        String lcName = name.toLowerCase();
        if (this.fontToFileMap.size() <= 1) {
            name = "Lucida Sans Regular";
        }
        FontResource fontResource = null;
        String file = findFile(lcName);
        if (file != null) {
            fontResource = getFontResource(name, file);
            if (fontResource != null) {
                storeInMap(lcName, fontResource);
                if (wantComp) {
                    fontResource = new PrismCompositeFontResource(fontResource, lcName);
                }
            }
        }
        return fontResource;
    }

    FontResource getDefaultFontResource(boolean wantComp) {
        String path;
        FontResource fontResource = lookupResource(jreDefaultFontLC, wantComp);
        if (fontResource == null) {
            fontResource = createFontResource("Lucida Sans Regular", jreFontDir + jreDefaultFontFile);
            if (fontResource == null) {
                for (String font : this.fontToFileMap.keySet()) {
                    String file = findFile(font);
                    fontResource = createFontResource(jreDefaultFontLC, file);
                    if (fontResource != null) {
                        break;
                    }
                }
                if (fontResource == null && isLinux && (path = FontConfigManager.getDefaultFontPath()) != null) {
                    fontResource = createFontResource(jreDefaultFontLC, path);
                }
                if (fontResource == null) {
                    return null;
                }
            }
            storeInMap(jreDefaultFontLC, fontResource);
            if (wantComp) {
                fontResource = new PrismCompositeFontResource(fontResource, jreDefaultFontLC);
            }
        }
        return fontResource;
    }

    private String findFile(String name) {
        if (name.equals(jreDefaultFontLC)) {
            return jreFontDir + jreDefaultFontFile;
        }
        getFullNameToFileMap();
        String filename = this.fontToFileMap.get(name);
        if (isWindows) {
            filename = getPathNameWindows(filename);
        }
        return filename;
    }

    private static void getPlatformFontDirs() {
        if (userFontDir != null || sysFontDir != null) {
            return;
        }
        byte[] pathBytes = getFontPath();
        String path = new String(pathBytes);
        int scIdx = path.indexOf(59);
        if (scIdx < 0) {
            sysFontDir = path;
        } else {
            sysFontDir = path.substring(0, scIdx);
            userFontDir = path.substring(scIdx + 1, path.length());
        }
    }

    static ArrayList<String>[] getLinkedFonts(String searchFont, boolean addSearchFont) {
        ArrayList<String>[] fontRegInfo = {new ArrayList<>(), new ArrayList<>()};
        if (isMacOSX) {
            fontRegInfo[0].add("/Library/Fonts/Arial Unicode.ttf");
            fontRegInfo[1].add("Arial Unicode MS");
            fontRegInfo[0].add(jreFontDir + jreDefaultFontFile);
            fontRegInfo[1].add("Lucida Sans Regular");
            fontRegInfo[0].add("/System/Library/Fonts/Apple Symbols.ttf");
            fontRegInfo[1].add("Apple Symbols");
            fontRegInfo[0].add("/System/Library/Fonts/STHeiti Light.ttf");
            fontRegInfo[1].add("Heiti SC Light");
            return fontRegInfo;
        }
        if (!isWindows) {
            return fontRegInfo;
        }
        if (addSearchFont) {
            fontRegInfo[0].add(null);
            fontRegInfo[1].add(searchFont);
        }
        String fontRegBuf = regReadFontLink(searchFont);
        if (fontRegBuf != null && fontRegBuf.length() > 0) {
            String[] fontRegList = fontRegBuf.split(Localizable.NOT_LOCALIZABLE);
            for (String str : fontRegList) {
                String[] splitFontData = str.split(",");
                int len = splitFontData.length;
                String file = getPathNameWindows(splitFontData[0]);
                String name = len > 1 ? splitFontData[1] : null;
                if ((name == null || !fontRegInfo[1].contains(name)) && (name != null || !fontRegInfo[0].contains(file))) {
                    fontRegInfo[0].add(file);
                    fontRegInfo[1].add(name);
                }
            }
        }
        String eudcFontFile = getEUDCFontFile();
        if (eudcFontFile != null) {
            fontRegInfo[0].add(eudcFontFile);
            fontRegInfo[1].add(null);
        }
        fontRegInfo[0].add(jreFontDir + jreDefaultFontFile);
        fontRegInfo[1].add("Lucida Sans Regular");
        if (PlatformUtil.isWinVistaOrLater()) {
            fontRegInfo[0].add(getPathNameWindows("mingliub.ttc"));
            fontRegInfo[1].add("MingLiU-ExtB");
            if (PlatformUtil.isWin7OrLater()) {
                fontRegInfo[0].add(getPathNameWindows("seguisym.ttf"));
                fontRegInfo[1].add("Segoe UI Symbol");
            } else {
                fontRegInfo[0].add(getPathNameWindows("cambria.ttc"));
                fontRegInfo[1].add("Cambria Math");
            }
        }
        return fontRegInfo;
    }

    private void resolveWindowsFonts(HashMap<String, String> fontToFileMap, HashMap<String, String> fontToFamilyNameMap, HashMap<String, ArrayList<String>> familyToFontListMap) {
        ArrayList<String> family;
        ArrayList<String> unmappedFontNames = null;
        for (String font : fontToFamilyNameMap.keySet()) {
            if (fontToFileMap.get(font) == null) {
                int dsi = font.indexOf(Constants.INDENT);
                if (dsi > 0) {
                    String newName = font.substring(0, dsi).concat(font.substring(dsi + 1));
                    String file = fontToFileMap.get(newName);
                    if (file != null && !fontToFamilyNameMap.containsKey(newName)) {
                        fontToFileMap.remove(newName);
                        fontToFileMap.put(font, file);
                    }
                } else if (font.equals("marlett")) {
                    fontToFileMap.put(font, "marlett.ttf");
                } else if (font.equals("david")) {
                    String file2 = fontToFileMap.get("david regular");
                    if (file2 != null) {
                        fontToFileMap.remove("david regular");
                        fontToFileMap.put("david", file2);
                    }
                } else {
                    if (unmappedFontNames == null) {
                        unmappedFontNames = new ArrayList<>();
                    }
                    unmappedFontNames.add(font);
                }
            }
        }
        if (unmappedFontNames != null) {
            HashSet<String> hashSet = new HashSet<>();
            HashMap<String, String> ffmapCopy = new HashMap<>();
            ffmapCopy.putAll(fontToFileMap);
            Iterator<String> it = fontToFamilyNameMap.keySet().iterator();
            while (it.hasNext()) {
                ffmapCopy.remove(it.next());
            }
            for (String key : ffmapCopy.keySet()) {
                hashSet.add(ffmapCopy.get(key));
                fontToFileMap.remove(key);
            }
            resolveFontFiles(hashSet, unmappedFontNames, fontToFileMap, fontToFamilyNameMap, familyToFontListMap);
            if (unmappedFontNames.size() > 0) {
                int sz = unmappedFontNames.size();
                for (int i2 = 0; i2 < sz; i2++) {
                    String name = unmappedFontNames.get(i2);
                    String familyName = fontToFamilyNameMap.get(name);
                    if (familyName != null && (family = familyToFontListMap.get(familyName)) != null && family.size() <= 1) {
                        familyToFontListMap.remove(familyName);
                    }
                    fontToFamilyNameMap.remove(name);
                }
            }
        }
    }

    private void resolveFontFiles(HashSet<String> unmappedFiles, ArrayList<String> unmappedFonts, HashMap<String, String> fontToFileMap, HashMap<String, String> fontToFamilyNameMap, HashMap<String, ArrayList<String>> familyToFontListMap) {
        PrismFontFile ttf;
        Iterator<String> it = unmappedFiles.iterator();
        while (it.hasNext()) {
            String file = it.next();
            try {
                int fn = 0;
                String fullPath = getPathNameWindows(file);
                do {
                    int i2 = fn;
                    fn++;
                    ttf = createFontResource(fullPath, i2);
                    if (ttf == null) {
                        break;
                    }
                    String fontNameLC = ttf.getFullName().toLowerCase();
                    String localeNameLC = ttf.getLocaleFullName().toLowerCase();
                    if (unmappedFonts.contains(fontNameLC) || unmappedFonts.contains(localeNameLC)) {
                        fontToFileMap.put(fontNameLC, file);
                        unmappedFonts.remove(fontNameLC);
                        if (unmappedFonts.contains(localeNameLC)) {
                            unmappedFonts.remove(localeNameLC);
                            String family = ttf.getFamilyName();
                            String familyLC = family.toLowerCase();
                            fontToFamilyNameMap.remove(localeNameLC);
                            fontToFamilyNameMap.put(fontNameLC, family);
                            ArrayList<String> familylist = familyToFontListMap.get(familyLC);
                            if (familylist != null) {
                                familylist.remove(ttf.getLocaleFullName());
                            } else {
                                String localeFamilyLC = ttf.getLocaleFamilyName().toLowerCase();
                                if (familyToFontListMap.get(localeFamilyLC) != null) {
                                    familyToFontListMap.remove(localeFamilyLC);
                                }
                                familylist = new ArrayList<>();
                                familyToFontListMap.put(familyLC, familylist);
                            }
                            familylist.add(ttf.getFullName());
                        }
                    }
                } while (fn < ttf.getFontCount());
            } catch (Exception e2) {
                if (debugFonts) {
                    e2.printStackTrace();
                }
            }
        }
    }

    static String getPathNameWindows(final String filename) {
        if (filename == null) {
            return null;
        }
        getPlatformFontDirs();
        File f2 = new File(filename);
        if (f2.isAbsolute()) {
            return filename;
        }
        if (userFontDir == null) {
            return sysFontDir + FXMLLoader.ESCAPE_PREFIX + filename;
        }
        String path = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: com.sun.javafx.font.PrismFontFactory.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public String run() {
                File f3 = new File(PrismFontFactory.sysFontDir + FXMLLoader.ESCAPE_PREFIX + filename);
                if (!f3.exists()) {
                    return PrismFontFactory.userFontDir + FXMLLoader.ESCAPE_PREFIX + filename;
                }
                return f3.getAbsolutePath();
            }
        });
        if (path != null) {
            return path;
        }
        return null;
    }

    @Override // com.sun.javafx.font.FontFactory
    public String[] getFontFamilyNames() {
        if (allFamilyNames == null) {
            ArrayList<String> familyNames = new ArrayList<>();
            LogicalFont.addFamilies(familyNames);
            if (this.embeddedFonts != null) {
                for (PrismFontFile embeddedFont : this.embeddedFonts.values()) {
                    if (!familyNames.contains(embeddedFont.getFamilyName())) {
                        familyNames.add(embeddedFont.getFamilyName());
                    }
                }
            }
            getFullNameToFileMap();
            for (String f2 : this.fontToFamilyNameMap.values()) {
                if (!familyNames.contains(f2)) {
                    familyNames.add(f2);
                }
            }
            Collections.sort(familyNames);
            allFamilyNames = new ArrayList<>(familyNames);
        }
        return (String[]) allFamilyNames.toArray(STR_ARRAY);
    }

    @Override // com.sun.javafx.font.FontFactory
    public String[] getFontFullNames() {
        if (allFontNames == null) {
            ArrayList<String> fontNames = new ArrayList<>();
            LogicalFont.addFullNames(fontNames);
            if (this.embeddedFonts != null) {
                for (PrismFontFile embeddedFont : this.embeddedFonts.values()) {
                    if (!fontNames.contains(embeddedFont.getFullName())) {
                        fontNames.add(embeddedFont.getFullName());
                    }
                }
            }
            getFullNameToFileMap();
            for (ArrayList<String> a2 : this.familyToFontListMap.values()) {
                Iterator<String> it = a2.iterator();
                while (it.hasNext()) {
                    String s2 = it.next();
                    fontNames.add(s2);
                }
            }
            Collections.sort(fontNames);
            allFontNames = fontNames;
        }
        return (String[]) allFontNames.toArray(STR_ARRAY);
    }

    @Override // com.sun.javafx.font.FontFactory
    public String[] getFontFullNames(String family) {
        String[] logFonts = LogicalFont.getFontsInFamily(family);
        if (logFonts != null) {
            return logFonts;
        }
        if (this.embeddedFonts != null) {
            ArrayList<String> embeddedFamily = null;
            for (PrismFontFile embeddedFont : this.embeddedFonts.values()) {
                if (embeddedFont.getFamilyName().equalsIgnoreCase(family)) {
                    if (embeddedFamily == null) {
                        embeddedFamily = new ArrayList<>();
                    }
                    embeddedFamily.add(embeddedFont.getFullName());
                }
            }
            if (embeddedFamily != null) {
                return (String[]) embeddedFamily.toArray(STR_ARRAY);
            }
        }
        getFullNameToFileMap();
        ArrayList<String> familyFonts = this.familyToFontListMap.get(family.toLowerCase());
        if (familyFonts != null) {
            return (String[]) familyFonts.toArray(STR_ARRAY);
        }
        return STR_ARRAY;
    }

    public final int getSubPixelMode() {
        return subPixelMode;
    }

    public boolean isLCDTextSupported() {
        return lcdEnabled;
    }

    @Override // com.sun.javafx.font.FontFactory
    public boolean isPlatformFont(String name) {
        if (name == null) {
            return false;
        }
        String lcName = name.toLowerCase();
        if (LogicalFont.isLogicalFont(lcName) || lcName.startsWith("lucida sans")) {
            return true;
        }
        String systemFamily = getSystemFont(LogicalFont.SYSTEM).toLowerCase();
        return lcName.startsWith(systemFamily);
    }

    public static boolean isJreFont(FontResource fr) {
        String file = fr.getFileName();
        return file.startsWith(jreFontDir);
    }

    public static float getLCDContrast() {
        if (lcdContrast == -1.0f) {
            if (isWindows) {
                lcdContrast = getLCDContrastWin32() / 1000.0f;
            } else {
                lcdContrast = 1.3f;
            }
        }
        return lcdContrast;
    }

    private synchronized void addFileCloserHook() {
        if (fileCloser == null) {
            Runnable fileCloserRunnable = () -> {
                if (this.embeddedFonts != null) {
                    Iterator<PrismFontFile> it = this.embeddedFonts.values().iterator();
                    while (it.hasNext()) {
                        it.next().disposeOnShutdown();
                    }
                }
                if (this.tmpFonts != null) {
                    Iterator<WeakReference<PrismFontFile>> it2 = this.tmpFonts.iterator();
                    while (it2.hasNext()) {
                        WeakReference<PrismFontFile> ref = it2.next();
                        PrismFontFile font = ref.get();
                        if (font != null) {
                            font.disposeOnShutdown();
                        }
                    }
                }
            };
            AccessController.doPrivileged(() -> {
                ThreadGroup tg = Thread.currentThread().getThreadGroup();
                ThreadGroup parent = tg;
                while (true) {
                    ThreadGroup tgn = parent;
                    if (tgn == null) {
                        fileCloser = new Thread(tg, fileCloserRunnable);
                        fileCloser.setContextClassLoader(null);
                        Runtime.getRuntime().addShutdownHook(fileCloser);
                        return null;
                    }
                    tg = tgn;
                    parent = tg.getParent();
                }
            });
        }
    }

    @Override // com.sun.javafx.font.FontFactory
    public PGFont loadEmbeddedFont(String name, InputStream fontStream, float size, boolean register) {
        if (!hasPermission()) {
            return createFont(FontFactory.DEFAULT_FULLNAME, size);
        }
        if (FontFileWriter.hasTempPermission()) {
            return loadEmbeddedFont0(name, fontStream, size, register);
        }
        FontFileWriter.FontTracker tracker = FontFileWriter.FontTracker.getTracker();
        boolean acquired = false;
        try {
            acquired = tracker.acquirePermit();
            if (!acquired) {
                if (acquired) {
                    tracker.releasePermit();
                }
                return null;
            }
            PGFont pGFontLoadEmbeddedFont0 = loadEmbeddedFont0(name, fontStream, size, register);
            if (acquired) {
                tracker.releasePermit();
            }
            return pGFontLoadEmbeddedFont0;
        } catch (InterruptedException e2) {
            if (acquired) {
                tracker.releasePermit();
            }
            return null;
        } catch (Throwable th) {
            if (acquired) {
                tracker.releasePermit();
            }
            throw th;
        }
    }

    private PGFont loadEmbeddedFont0(String name, InputStream fontStream, float size, boolean register) {
        PrismFontFile fr = null;
        FontFileWriter fontWriter = new FontFileWriter();
        try {
            try {
                File tFile = fontWriter.openFile();
                byte[] buf = new byte[8192];
                while (true) {
                    int bytesRead = fontStream.read(buf);
                    if (bytesRead < 0) {
                        break;
                    }
                    fontWriter.writeBytes(buf, 0, bytesRead);
                }
                fontWriter.closeFile();
                fr = loadEmbeddedFont(name, tFile.getPath(), register, true, fontWriter.isTracking());
                if (fr != null && fr.isDecoded()) {
                    fontWriter.deleteFile();
                }
                addFileCloserHook();
                if (fr == null) {
                    fontWriter.deleteFile();
                }
            } catch (Exception e2) {
                fontWriter.deleteFile();
                if (fr == null) {
                    fontWriter.deleteFile();
                }
            }
            if (fr != null) {
                if (size <= 0.0f) {
                    size = getSystemFontSize();
                }
                return new PrismFont(fr, fr.getFullName(), size);
            }
            return null;
        } catch (Throwable th) {
            if (fr == null) {
                fontWriter.deleteFile();
            }
            throw th;
        }
    }

    @Override // com.sun.javafx.font.FontFactory
    public PGFont loadEmbeddedFont(String name, String path, float size, boolean register) {
        if (!hasPermission()) {
            return createFont(FontFactory.DEFAULT_FULLNAME, size);
        }
        addFileCloserHook();
        FontResource fr = loadEmbeddedFont(name, path, register, false, false);
        if (fr != null) {
            if (size <= 0.0f) {
                size = getSystemFontSize();
            }
            return new PrismFont(fr, fr.getFullName(), size);
        }
        return null;
    }

    private void removeEmbeddedFont(String name) {
        PrismFontFile font = this.embeddedFonts.get(name);
        if (font == null) {
            return;
        }
        this.embeddedFonts.remove(name);
        String lcName = name.toLowerCase();
        this.fontResourceMap.remove(lcName);
        this.compResourceMap.remove(lcName);
        Iterator<CompositeFontResource> fi = this.compResourceMap.values().iterator();
        while (fi.hasNext()) {
            CompositeFontResource compFont = fi.next();
            if (compFont.getSlotResource(0) == font) {
                fi.remove();
            }
        }
    }

    protected boolean registerEmbeddedFont(String path) {
        return true;
    }

    public int test_getNumEmbeddedFonts() {
        return this.numEmbeddedFonts;
    }

    private synchronized PrismFontFile loadEmbeddedFont(String name, String path, boolean register, boolean copy, boolean tracked) {
        String family;
        String fullname;
        String psname;
        FontResource resource;
        this.numEmbeddedFonts++;
        PrismFontFile fr = createFontResource(name, path, register, true, copy, tracked);
        if (fr == null || (family = fr.getFamilyName()) == null || family.length() == 0 || (fullname = fr.getFullName()) == null || fullname.length() == 0 || (psname = fr.getPSName()) == null || psname.length() == 0) {
            return null;
        }
        boolean registerEmbedded = true;
        if (this.embeddedFonts != null && (resource = this.embeddedFonts.get(fullname)) != null && fr.equals(resource)) {
            registerEmbedded = false;
        }
        if (registerEmbedded && !registerEmbeddedFont(fr.getFileName())) {
            return null;
        }
        if (!register) {
            if (copy && !fr.isDecoded()) {
                addTmpFont(fr);
            }
            return fr;
        }
        if (this.embeddedFonts == null) {
            this.embeddedFonts = new HashMap<>();
        }
        if (name != null && !name.isEmpty()) {
            this.embeddedFonts.put(name, fr);
            storeInMap(name, fr);
        }
        removeEmbeddedFont(fullname);
        this.embeddedFonts.put(fullname, fr);
        storeInMap(fullname, fr);
        String family2 = family + dotStyleStr(fr.isBold(), fr.isItalic());
        storeInMap(family2, fr);
        this.compResourceMap.remove(family2.toLowerCase());
        return fr;
    }

    private void logFontInfo(String message, HashMap<String, String> fontToFileMap, HashMap<String, String> fontToFamilyNameMap, HashMap<String, ArrayList<String>> familyToFontListMap) {
        System.err.println(message);
        for (String keyName : fontToFileMap.keySet()) {
            System.err.println("font=" + keyName + " file=" + fontToFileMap.get(keyName));
        }
        for (String keyName2 : fontToFamilyNameMap.keySet()) {
            System.err.println("font=" + keyName2 + " family=" + fontToFamilyNameMap.get(keyName2));
        }
        for (String keyName3 : familyToFontListMap.keySet()) {
            System.err.println("family=" + keyName3 + " fonts=" + ((Object) familyToFontListMap.get(keyName3)));
        }
    }

    private synchronized HashMap<String, String> getFullNameToFileMap() {
        if (this.fontToFileMap == null) {
            HashMap<String, String> tmpFontToFileMap = new HashMap<>(100);
            this.fontToFamilyNameMap = new HashMap<>(100);
            this.familyToFontListMap = new HashMap<>(50);
            this.fileToFontMap = new HashMap<>(100);
            if (isWindows) {
                getPlatformFontDirs();
                populateFontFileNameMap(tmpFontToFileMap, this.fontToFamilyNameMap, this.familyToFontListMap, Locale.ENGLISH);
                if (debugFonts) {
                    System.err.println("Windows Locale ID=" + ((int) getSystemLCID()));
                    logFontInfo(" *** WINDOWS FONTS BEFORE RESOLVING", tmpFontToFileMap, this.fontToFamilyNameMap, this.familyToFontListMap);
                }
                resolveWindowsFonts(tmpFontToFileMap, this.fontToFamilyNameMap, this.familyToFontListMap);
                if (debugFonts) {
                    logFontInfo(" *** WINDOWS FONTS AFTER RESOLVING", tmpFontToFileMap, this.fontToFamilyNameMap, this.familyToFontListMap);
                }
            } else if (isMacOSX || isIOS) {
                MacFontFinder.populateFontFileNameMap(tmpFontToFileMap, this.fontToFamilyNameMap, this.familyToFontListMap, Locale.ENGLISH);
            } else if (isLinux) {
                FontConfigManager.populateMaps(tmpFontToFileMap, this.fontToFamilyNameMap, this.familyToFontListMap, Locale.getDefault());
                if (debugFonts) {
                    logFontInfo(" *** FONTCONFIG LOCATED FONTS:", tmpFontToFileMap, this.fontToFamilyNameMap, this.familyToFontListMap);
                }
            } else if (isAndroid) {
                AndroidFontFinder.populateFontFileNameMap(tmpFontToFileMap, this.fontToFamilyNameMap, this.familyToFontListMap, Locale.ENGLISH);
            } else {
                this.fontToFileMap = tmpFontToFileMap;
                return this.fontToFileMap;
            }
            for (String font : tmpFontToFileMap.keySet()) {
                String file = tmpFontToFileMap.get(font);
                this.fileToFontMap.put(file.toLowerCase(), font);
            }
            this.fontToFileMap = tmpFontToFileMap;
            if (isAndroid) {
                populateFontFileNameMapGeneric(AndroidFontFinder.getSystemFontsDir());
            }
            populateFontFileNameMapGeneric(jreFontDir);
        }
        return this.fontToFileMap;
    }

    @Override // com.sun.javafx.font.FontFactory
    public final boolean hasPermission() {
        try {
            SecurityManager sm = System.getSecurityManager();
            if (sm != null) {
                sm.checkPermission(new AllPermission());
                return true;
            }
            return true;
        } catch (SecurityException e2) {
            return false;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/font/PrismFontFactory$TTFilter.class */
    private static class TTFilter implements FilenameFilter {
        static TTFilter ttFilter;

        @Override // java.io.FilenameFilter
        public boolean accept(File dir, String name) {
            int offset = name.length() - 4;
            if (offset <= 0) {
                return false;
            }
            return name.startsWith(".ttf", offset) || name.startsWith(".TTF", offset) || name.startsWith(".ttc", offset) || name.startsWith(".TTC", offset) || name.startsWith(".otf", offset) || name.startsWith(".OTF", offset);
        }

        private TTFilter() {
        }

        static TTFilter getInstance() {
            if (ttFilter == null) {
                ttFilter = new TTFilter();
            }
            return ttFilter;
        }
    }

    void addToMaps(PrismFontFile fr) {
        if (fr == null) {
            return;
        }
        String fullName = fr.getFullName();
        String familyName = fr.getFamilyName();
        if (fullName == null || familyName == null) {
            return;
        }
        String lcFullName = fullName.toLowerCase();
        String lcFamilyName = familyName.toLowerCase();
        this.fontToFileMap.put(lcFullName, fr.getFileName());
        this.fontToFamilyNameMap.put(lcFullName, familyName);
        ArrayList<String> familyList = this.familyToFontListMap.get(lcFamilyName);
        if (familyList == null) {
            familyList = new ArrayList<>();
            this.familyToFontListMap.put(lcFamilyName, familyList);
        }
        familyList.add(fullName);
    }

    void populateFontFileNameMapGeneric(String fontDir) {
        File dir = new File(fontDir);
        String[] files = null;
        try {
            files = (String[]) AccessController.doPrivileged(() -> {
                return dir.list(TTFilter.getInstance());
            });
        } catch (Exception e2) {
        }
        if (files == null) {
            return;
        }
        for (String str : files) {
            try {
                String path = fontDir + File.separator + str;
                if (registerEmbeddedFont(path)) {
                    int index = 0 + 1;
                    PrismFontFile fr = createFontResource(path, 0);
                    if (fr != null) {
                        addToMaps(fr);
                        while (index < fr.getFontCount()) {
                            int i2 = index;
                            index++;
                            fr = createFontResource(path, i2);
                            if (fr == null) {
                                break;
                            } else {
                                addToMaps(fr);
                            }
                        }
                    }
                }
            } catch (Exception e3) {
            }
        }
    }

    public static float getSystemFontSize() {
        if (systemFontSize == -1.0f) {
            if (isWindows) {
                float uiScale = Screen.getMainScreen().getUIScale();
                systemFontSize = getSystemFontSizeNative() / uiScale;
            } else if (isMacOSX || isIOS) {
                systemFontSize = MacFontFinder.getSystemFontSize();
            } else if (isAndroid) {
                systemFontSize = AndroidFontFinder.getSystemFontSize();
            } else if (isEmbedded) {
                try {
                    int screenDPI = Screen.getMainScreen().getResolutionY();
                    systemFontSize = screenDPI / 6.0f;
                } catch (NullPointerException e2) {
                    systemFontSize = 13.0f;
                }
            } else {
                systemFontSize = 13.0f;
            }
        }
        return systemFontSize;
    }

    public static String getSystemFont(String name) {
        if (name.equals(LogicalFont.SYSTEM)) {
            if (systemFontFamily == null) {
                if (isWindows) {
                    systemFontFamily = getSystemFontNative();
                    if (systemFontFamily == null) {
                        systemFontFamily = "Arial";
                    }
                } else if (isMacOSX || isIOS) {
                    systemFontFamily = MacFontFinder.getSystemFont();
                    if (systemFontFamily == null) {
                        systemFontFamily = "Lucida Grande";
                    }
                } else if (isAndroid) {
                    systemFontFamily = AndroidFontFinder.getSystemFont();
                } else {
                    systemFontFamily = "Lucida Sans";
                }
            }
            return systemFontFamily;
        }
        if (name.equals("SansSerif")) {
            return "Arial";
        }
        if (name.equals("Serif")) {
            return "Times New Roman";
        }
        if (monospaceFontFamily != null || isMacOSX) {
        }
        if (monospaceFontFamily == null) {
            monospaceFontFamily = "Courier New";
        }
        return monospaceFontFamily;
    }
}
