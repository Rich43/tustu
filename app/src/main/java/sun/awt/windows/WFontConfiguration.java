package sun.awt.windows;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Hashtable;
import sun.awt.FontConfiguration;
import sun.awt.FontDescriptor;
import sun.font.SunFontManager;

/* loaded from: rt.jar:sun/awt/windows/WFontConfiguration.class */
public final class WFontConfiguration extends FontConfiguration {
    private boolean useCompatibilityFallbacks;
    private static HashMap subsetCharsetMap = new HashMap();
    private static HashMap subsetEncodingMap = new HashMap();
    private static String textInputCharset;

    public WFontConfiguration(SunFontManager sunFontManager) {
        super(sunFontManager);
        this.useCompatibilityFallbacks = "windows-1252".equals(encoding);
        initTables(encoding);
    }

    public WFontConfiguration(SunFontManager sunFontManager, boolean z2, boolean z3) {
        super(sunFontManager, z2, z3);
        this.useCompatibilityFallbacks = "windows-1252".equals(encoding);
    }

    @Override // sun.awt.FontConfiguration
    protected void initReorderMap() {
        if (encoding.equalsIgnoreCase("windows-31j")) {
            localeMap = new Hashtable();
            localeMap.put("dialoginput.plain.japanese", "MS Mincho");
            localeMap.put("dialoginput.bold.japanese", "MS Mincho");
            localeMap.put("dialoginput.italic.japanese", "MS Mincho");
            localeMap.put("dialoginput.bolditalic.japanese", "MS Mincho");
        }
        this.reorderMap = new HashMap();
        this.reorderMap.put("UTF-8.hi", "devanagari");
        this.reorderMap.put("windows-1255", "hebrew");
        this.reorderMap.put("x-windows-874", "thai");
        this.reorderMap.put("windows-31j", "japanese");
        this.reorderMap.put("x-windows-949", "korean");
        this.reorderMap.put("GBK", "chinese-ms936");
        this.reorderMap.put("GB18030", "chinese-gb18030");
        this.reorderMap.put("x-windows-950", "chinese-ms950");
        this.reorderMap.put("x-MS950-HKSCS", split("chinese-ms950,chinese-hkscs"));
    }

    @Override // sun.awt.FontConfiguration
    protected void setOsNameAndVersion() {
        super.setOsNameAndVersion();
        if (osName.startsWith("Windows")) {
            int iIndexOf = osName.indexOf(32);
            if (iIndexOf == -1) {
                osName = null;
            } else {
                int iIndexOf2 = osName.indexOf(32, iIndexOf + 1);
                if (iIndexOf2 == -1) {
                    osName = osName.substring(iIndexOf + 1);
                } else {
                    osName = osName.substring(iIndexOf + 1, iIndexOf2);
                }
            }
            osVersion = null;
        }
    }

    @Override // sun.awt.FontConfiguration
    public String getFallbackFamilyName(String str, String str2) {
        String compatibilityFamilyName;
        if (this.useCompatibilityFallbacks && (compatibilityFamilyName = getCompatibilityFamilyName(str)) != null) {
            return compatibilityFamilyName;
        }
        return str2;
    }

    @Override // sun.awt.FontConfiguration
    protected String makeAWTFontName(String str, String str2) {
        String str3 = (String) subsetCharsetMap.get(str2);
        if (str3 == null) {
            str3 = "DEFAULT_CHARSET";
        }
        return str + "," + str3;
    }

    @Override // sun.awt.FontConfiguration
    protected String getEncoding(String str, String str2) {
        String str3 = (String) subsetEncodingMap.get(str2);
        if (str3 == null) {
            str3 = "default";
        }
        return str3;
    }

    @Override // sun.awt.FontConfiguration
    protected Charset getDefaultFontCharset(String str) {
        return new WDefaultFontCharset(str);
    }

    @Override // sun.awt.FontConfiguration
    public String getFaceNameFromComponentFontName(String str) {
        return str;
    }

    @Override // sun.awt.FontConfiguration
    protected String getFileNameFromComponentFontName(String str) {
        return getFileNameFromPlatformName(str);
    }

    public String getTextComponentFontName(String str, int i2) {
        FontDescriptor[] fontDescriptors = getFontDescriptors(str, i2);
        String strFindFontWithCharset = findFontWithCharset(fontDescriptors, textInputCharset);
        if (strFindFontWithCharset == null) {
            strFindFontWithCharset = findFontWithCharset(fontDescriptors, "DEFAULT_CHARSET");
        }
        return strFindFontWithCharset;
    }

    private String findFontWithCharset(FontDescriptor[] fontDescriptorArr, String str) {
        String str2 = null;
        for (FontDescriptor fontDescriptor : fontDescriptorArr) {
            String nativeName = fontDescriptor.getNativeName();
            if (nativeName.endsWith(str)) {
                str2 = nativeName;
            }
        }
        return str2;
    }

    private void initTables(String str) {
        subsetCharsetMap.put(Constants.ATTRVAL_ALPHABETIC, "ANSI_CHARSET");
        subsetCharsetMap.put("alphabetic/1252", "ANSI_CHARSET");
        subsetCharsetMap.put("alphabetic/default", "DEFAULT_CHARSET");
        subsetCharsetMap.put("arabic", "ARABIC_CHARSET");
        subsetCharsetMap.put("chinese-ms936", "GB2312_CHARSET");
        subsetCharsetMap.put("chinese-gb18030", "GB2312_CHARSET");
        subsetCharsetMap.put("chinese-ms950", "CHINESEBIG5_CHARSET");
        subsetCharsetMap.put("chinese-hkscs", "CHINESEBIG5_CHARSET");
        subsetCharsetMap.put("cyrillic", "RUSSIAN_CHARSET");
        subsetCharsetMap.put("devanagari", "DEFAULT_CHARSET");
        subsetCharsetMap.put("dingbats", "SYMBOL_CHARSET");
        subsetCharsetMap.put("greek", "GREEK_CHARSET");
        subsetCharsetMap.put("hebrew", "HEBREW_CHARSET");
        subsetCharsetMap.put("japanese", "SHIFTJIS_CHARSET");
        subsetCharsetMap.put("korean", "HANGEUL_CHARSET");
        subsetCharsetMap.put("latin", "ANSI_CHARSET");
        subsetCharsetMap.put("symbol", "SYMBOL_CHARSET");
        subsetCharsetMap.put("thai", "THAI_CHARSET");
        subsetEncodingMap.put(Constants.ATTRVAL_ALPHABETIC, "default");
        subsetEncodingMap.put("alphabetic/1252", "windows-1252");
        subsetEncodingMap.put("alphabetic/default", str);
        subsetEncodingMap.put("arabic", "windows-1256");
        subsetEncodingMap.put("chinese-ms936", "GBK");
        subsetEncodingMap.put("chinese-gb18030", "GB18030");
        if ("x-MS950-HKSCS".equals(str)) {
            subsetEncodingMap.put("chinese-ms950", "x-MS950-HKSCS");
        } else {
            subsetEncodingMap.put("chinese-ms950", "x-windows-950");
        }
        subsetEncodingMap.put("chinese-hkscs", "sun.awt.HKSCS");
        subsetEncodingMap.put("cyrillic", "windows-1251");
        subsetEncodingMap.put("devanagari", "UTF-16LE");
        subsetEncodingMap.put("dingbats", "sun.awt.windows.WingDings");
        subsetEncodingMap.put("greek", "windows-1253");
        subsetEncodingMap.put("hebrew", "windows-1255");
        subsetEncodingMap.put("japanese", "windows-31j");
        subsetEncodingMap.put("korean", "x-windows-949");
        subsetEncodingMap.put("latin", "windows-1252");
        subsetEncodingMap.put("symbol", "sun.awt.Symbol");
        subsetEncodingMap.put("thai", "x-windows-874");
        if ("windows-1256".equals(str)) {
            textInputCharset = "ARABIC_CHARSET";
            return;
        }
        if ("GBK".equals(str)) {
            textInputCharset = "GB2312_CHARSET";
            return;
        }
        if ("GB18030".equals(str)) {
            textInputCharset = "GB2312_CHARSET";
            return;
        }
        if ("x-windows-950".equals(str)) {
            textInputCharset = "CHINESEBIG5_CHARSET";
            return;
        }
        if ("x-MS950-HKSCS".equals(str)) {
            textInputCharset = "CHINESEBIG5_CHARSET";
            return;
        }
        if ("windows-1251".equals(str)) {
            textInputCharset = "RUSSIAN_CHARSET";
            return;
        }
        if ("UTF-8".equals(str)) {
            textInputCharset = "DEFAULT_CHARSET";
            return;
        }
        if ("windows-1253".equals(str)) {
            textInputCharset = "GREEK_CHARSET";
            return;
        }
        if ("windows-1255".equals(str)) {
            textInputCharset = "HEBREW_CHARSET";
            return;
        }
        if ("windows-31j".equals(str)) {
            textInputCharset = "SHIFTJIS_CHARSET";
            return;
        }
        if ("x-windows-949".equals(str)) {
            textInputCharset = "HANGEUL_CHARSET";
        } else if ("x-windows-874".equals(str)) {
            textInputCharset = "THAI_CHARSET";
        } else {
            textInputCharset = "DEFAULT_CHARSET";
        }
    }
}
