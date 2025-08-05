package sun.awt;

import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import sun.awt.windows.WFontConfiguration;
import sun.font.SunFontManager;
import sun.font.TrueTypeFont;

/* loaded from: rt.jar:sun/awt/Win32FontManager.class */
public final class Win32FontManager extends SunFontManager {
    private static TrueTypeFont eudcFont;
    static String fontsForPrinting;

    /* JADX INFO: Access modifiers changed from: private */
    public static native String getEUDCFontFile();

    private static native void populateFontFileNameMap0(HashMap<String, String> map, HashMap<String, String> map2, HashMap<String, ArrayList<String>> map3, Locale locale);

    @Override // sun.font.SunFontManager
    protected native synchronized String getFontPath(boolean z2);

    protected static native void registerFontWithPlatform(String str);

    protected static native void deRegisterFontWithPlatform(String str);

    static {
        AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.awt.Win32FontManager.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                String eUDCFontFile = Win32FontManager.getEUDCFontFile();
                if (eUDCFontFile != null) {
                    try {
                        TrueTypeFont unused = Win32FontManager.eudcFont = new TrueTypeFont(eUDCFontFile, null, 0, true, false);
                        return null;
                    } catch (FontFormatException e2) {
                        return null;
                    }
                }
                return null;
            }
        });
        fontsForPrinting = null;
    }

    @Override // sun.font.SunFontManager
    public TrueTypeFont getEUDCFont() {
        return eudcFont;
    }

    public Win32FontManager() {
        AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.awt.Win32FontManager.2
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                Win32FontManager.this.registerJREFontsWithPlatform(SunFontManager.jreFontDirName);
                return null;
            }
        });
    }

    @Override // sun.font.SunFontManager
    protected boolean useAbsoluteFontFileNames() {
        return false;
    }

    @Override // sun.font.SunFontManager
    protected void registerFontFile(String str, String[] strArr, int i2, boolean z2) {
        int i3;
        if (this.registeredFontFiles.contains(str)) {
            return;
        }
        this.registeredFontFiles.add(str);
        if (getTrueTypeFilter().accept(null, str)) {
            i3 = 0;
        } else if (getType1Filter().accept(null, str)) {
            i3 = 1;
        } else {
            return;
        }
        if (this.fontPath == null) {
            this.fontPath = getPlatformFontPath(noType1Font);
        }
        StringTokenizer stringTokenizer = new StringTokenizer(jreFontDirName + File.pathSeparator + this.fontPath, File.pathSeparator);
        boolean z3 = false;
        while (true) {
            if (0 != 0) {
                break;
            }
            try {
                if (stringTokenizer.hasMoreTokens()) {
                    String strNextToken = stringTokenizer.nextToken();
                    boolean zEquals = strNextToken.equals(jreFontDirName);
                    File file = new File(strNextToken, str);
                    if (file.canRead()) {
                        z3 = true;
                        String absolutePath = file.getAbsolutePath();
                        if (z2) {
                            registerDeferredFont(str, absolutePath, strArr, i3, zEquals, i2);
                        } else {
                            registerFontFile(absolutePath, strArr, i3, zEquals, i2);
                        }
                    }
                }
            } catch (NoSuchElementException e2) {
                System.err.println(e2);
            }
        }
        if (!z3) {
            addToMissingFontFileList(str);
        }
    }

    @Override // sun.font.SunFontManager
    protected FontConfiguration createFontConfiguration() {
        WFontConfiguration wFontConfiguration = new WFontConfiguration(this);
        wFontConfiguration.init();
        return wFontConfiguration;
    }

    @Override // sun.font.SunFontManager
    public FontConfiguration createFontConfiguration(boolean z2, boolean z3) {
        return new WFontConfiguration(this, z2, z3);
    }

    @Override // sun.font.SunFontManager
    protected void populateFontFileNameMap(HashMap<String, String> map, HashMap<String, String> map2, HashMap<String, ArrayList<String>> map3, Locale locale) {
        populateFontFileNameMap0(map, map2, map3, locale);
    }

    @Override // sun.font.SunFontManager
    protected String[] getDefaultPlatformFont() {
        String[] strArr = {"Arial", "c:\\windows\\fonts"};
        final String[] platformFontDirs = getPlatformFontDirs(true);
        if (platformFontDirs.length > 1) {
            String str = (String) AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.awt.Win32FontManager.3
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Object run2() {
                    for (int i2 = 0; i2 < platformFontDirs.length; i2++) {
                        if (new File(platformFontDirs[i2] + File.separator + "arial.ttf").exists()) {
                            return platformFontDirs[i2];
                        }
                    }
                    return null;
                }
            });
            if (str != null) {
                strArr[1] = str;
            }
        } else {
            strArr[1] = platformFontDirs[0];
        }
        strArr[1] = strArr[1] + File.separator + "arial.ttf";
        return strArr;
    }

    protected void registerJREFontsWithPlatform(String str) {
        fontsForPrinting = str;
    }

    public static void registerJREFontsForPrinting() {
        synchronized (Win32GraphicsEnvironment.class) {
            GraphicsEnvironment.getLocalGraphicsEnvironment();
            if (fontsForPrinting == null) {
                return;
            }
            final String str = fontsForPrinting;
            fontsForPrinting = null;
            AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.awt.Win32FontManager.4
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Object run2() {
                    File file = new File(str);
                    String[] list = file.list(SunFontManager.getInstance().getTrueTypeFilter());
                    if (list == null) {
                        return null;
                    }
                    for (String str2 : list) {
                        Win32FontManager.registerFontWithPlatform(new File(file, str2).getAbsolutePath());
                    }
                    return null;
                }
            });
        }
    }

    @Override // sun.font.SunFontManager
    public HashMap<String, SunFontManager.FamilyDescription> populateHardcodedFileNameMap() {
        HashMap<String, SunFontManager.FamilyDescription> map = new HashMap<>();
        SunFontManager.FamilyDescription familyDescription = new SunFontManager.FamilyDescription();
        familyDescription.familyName = "Segoe UI";
        familyDescription.plainFullName = "Segoe UI";
        familyDescription.plainFileName = "segoeui.ttf";
        familyDescription.boldFullName = "Segoe UI Bold";
        familyDescription.boldFileName = "segoeuib.ttf";
        familyDescription.italicFullName = "Segoe UI Italic";
        familyDescription.italicFileName = "segoeuii.ttf";
        familyDescription.boldItalicFullName = "Segoe UI Bold Italic";
        familyDescription.boldItalicFileName = "segoeuiz.ttf";
        map.put("segoe", familyDescription);
        SunFontManager.FamilyDescription familyDescription2 = new SunFontManager.FamilyDescription();
        familyDescription2.familyName = "Tahoma";
        familyDescription2.plainFullName = "Tahoma";
        familyDescription2.plainFileName = "tahoma.ttf";
        familyDescription2.boldFullName = "Tahoma Bold";
        familyDescription2.boldFileName = "tahomabd.ttf";
        map.put("tahoma", familyDescription2);
        SunFontManager.FamilyDescription familyDescription3 = new SunFontManager.FamilyDescription();
        familyDescription3.familyName = "Verdana";
        familyDescription3.plainFullName = "Verdana";
        familyDescription3.plainFileName = "verdana.TTF";
        familyDescription3.boldFullName = "Verdana Bold";
        familyDescription3.boldFileName = "verdanab.TTF";
        familyDescription3.italicFullName = "Verdana Italic";
        familyDescription3.italicFileName = "verdanai.TTF";
        familyDescription3.boldItalicFullName = "Verdana Bold Italic";
        familyDescription3.boldItalicFileName = "verdanaz.TTF";
        map.put("verdana", familyDescription3);
        SunFontManager.FamilyDescription familyDescription4 = new SunFontManager.FamilyDescription();
        familyDescription4.familyName = "Arial";
        familyDescription4.plainFullName = "Arial";
        familyDescription4.plainFileName = "ARIAL.TTF";
        familyDescription4.boldFullName = "Arial Bold";
        familyDescription4.boldFileName = "ARIALBD.TTF";
        familyDescription4.italicFullName = "Arial Italic";
        familyDescription4.italicFileName = "ARIALI.TTF";
        familyDescription4.boldItalicFullName = "Arial Bold Italic";
        familyDescription4.boldItalicFileName = "ARIALBI.TTF";
        map.put("arial", familyDescription4);
        SunFontManager.FamilyDescription familyDescription5 = new SunFontManager.FamilyDescription();
        familyDescription5.familyName = "Symbol";
        familyDescription5.plainFullName = "Symbol";
        familyDescription5.plainFileName = "Symbol.TTF";
        map.put("symbol", familyDescription5);
        SunFontManager.FamilyDescription familyDescription6 = new SunFontManager.FamilyDescription();
        familyDescription6.familyName = "WingDings";
        familyDescription6.plainFullName = "WingDings";
        familyDescription6.plainFileName = "WINGDING.TTF";
        map.put("wingdings", familyDescription6);
        return map;
    }
}
