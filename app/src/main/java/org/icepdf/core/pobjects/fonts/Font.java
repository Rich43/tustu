package org.icepdf.core.pobjects.fonts;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.util.HashMap;
import javax.swing.text.rtf.RTFGenerator;
import org.icepdf.core.pobjects.Dictionary;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.Resources;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.PdfOps;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/fonts/Font.class */
public abstract class Font extends Dictionary {
    protected Name name;
    protected String basefont;
    protected Name subtype;
    public static final int SIMPLE_FORMAT = 1;
    public static final int CID_FORMAT = 2;
    protected int subTypeFormat;
    protected FontFile font;
    protected int firstchar;
    protected int lastchar;
    protected FontDescriptor fontDescriptor;
    protected boolean inited;
    protected boolean isAFMFont;
    protected boolean isVerticalWriting;
    protected boolean isFontSubstitution;
    protected Resources parentResource;
    public static final Name TYPE = new Name("Font");
    public static final Name NAME_KEY = new Name("Name");
    public static final Name BASEFONT_KEY = new Name("BaseFont");
    public static final Name ENCODING_KEY = new Name(Constants._ATT_ENCODING);
    public static final Name FIRST_CHAR_KEY = new Name("FirstChar");
    public static final Name LAST_CHAR_KEY = new Name("LastChar");
    protected static final String[][] TO_UNICODE = {new String[]{"GBpc-EUC-UCS2", "GBpc-EUC-H", "GBpc-EUC-V"}, new String[]{"GBK-EUC-UCS2", "GBK-EUC-H", "GBK-EUC-V"}, new String[]{"UniGB-UCS2-H", "GB-EUC-H", "GBT-EUC-H", "GBK2K-H", "GBKp-EUC-H"}, new String[]{"UniGB-UCS2-V", "GB-EUC-V", "GBT-EUC-V", "GBK2K-V", "GBKp-EUC-V"}, new String[]{"B5pc-UCS2", "B5pc-H", "B5pc-V"}, new String[]{"ETen-B5-UCS2", "ETen-B5-H", "ETen-B5-V", "ETenms-B5-H", "ETenms-B5-V"}, new String[]{"UniCNS-UCS2-H", "HKscs-B5-H", "CNS-EUC-H"}, new String[]{"UniCNS-UCS2-V", "HKscs-B5-V", "CNS-EUC-V"}, new String[]{"90pv-RKSJ-UCS2", "90pv-RKSJ-H", "83pv-RKSJ-H"}, new String[]{"90ms-RKSJ-UCS2", "90ms-RKSJ-H", "90ms-RKSJ-V", "90msp-RKSJ-H", "90msp-RKSJ-V"}, new String[]{"UniJIS-UCS2-H", "Ext-RKSJ-H", PdfOps.H_TOKEN, "Add-RKSJ-H", "EUC-H"}, new String[]{"UniJIS-UCS2-V", "Ext-RKSJ-V", "V", "Add-RKSJ-V", "EUC-V"}, new String[]{"KSCms-UHC-UCS2", "KSCms-UHC-H", "KSCms-UHC-V", "KSCms-UHC-HW-H", "KSCms-UHC-HW-V"}, new String[]{"KSCpc-EUC-UCS2", "KSCpc-EUC-H"}, new String[]{"UniKS-UCS2-H", "KSC-EUC-H"}, new String[]{"UniKS-UCS2-V", "KSC-EUC-V"}};
    protected static final String[] CORE14 = {"Times-Roman", "Times-Bold", "Times-Italic", "Times-BoldItalic", RTFGenerator.defaultFontFamily, "Helvetica-Bold", "Helvetica-Oblique", "Helvetica-BoldOblique", "Courier", "Courier-Bold", "Courier-Oblique", "Courier-BoldOblique", "Symbol", "ZapfDingbats"};
    protected static final String[][] TYPE1_FONT_NAME = {new String[]{"Times-Roman", "Times New Roman", "TimesNewRoman", "TimesNewRomanPS", "TimesNewRomanPSMT"}, new String[]{"Times-Bold", "TimesNewRoman,Bold", "TimesNewRoman-Bold", "TimesNewRomanPS-Bold", "TimesNewRomanPS-BoldMT"}, new String[]{"Times-Italic", "TimesNewRoman,Italic", "TimesNewRoman-Italic", "TimesNewRomanPS-Italic", "TimesNewRomanPS-ItalicMT"}, new String[]{"Times-BoldItalic", "TimesNewRoman,BoldItalic", "TimesNewRoman-BoldItalic", "TimesNewRomanPS-BoldItalic", "TimesNewRomanPS-BoldItalicMT"}, new String[]{RTFGenerator.defaultFontFamily, "Arial", "ArialMT"}, new String[]{"Helvetica-Bold", "Helvetica,Bold", "Arial,Bold", "Arial-Bold", "Arial-BoldMT"}, new String[]{"Helvetica-Oblique", "Helvetica,Italic", "Helvetica-Italic", "Arial,Italic", "Arial-Italic", "Arial-ItalicMT"}, new String[]{"Helvetica-BoldOblique", "Helvetica,BoldItalic", "Helvetica-BoldItalic", "Arial,BoldItalic", "Arial-BoldItalic", "Arial-BoldItalicMT"}, new String[]{"Courier", "CourierNew", "CourierNewPSMT"}, new String[]{"Courier-Bold", "Courier,Bold", "CourierNew,Bold", "CourierNew-Bold", "CourierNewPS-BoldMT"}, new String[]{"Courier-Oblique", "Courier,Italic", "CourierNew-Italic", "CourierNew,Italic", "CourierNewPS-ItalicMT"}, new String[]{"Courier-BoldOblique", "Courier,BoldItalic", "CourierNew-BoldItalic", "CourierNew,BoldItalic", "CourierNewPS-BoldItalicMT"}, new String[]{"Symbol"}, new String[]{"ZapfDingbats", "Zapf-Dingbats", "Dingbats"}};

    @Override // org.icepdf.core.pobjects.Dictionary
    public abstract void init();

    public Font(Library library, HashMap entries) {
        super(library, entries);
        this.subTypeFormat = 1;
        this.firstchar = 32;
        this.lastchar = 255;
        this.name = library.getName(entries, NAME_KEY);
        this.subtype = library.getName(entries, SUBTYPE_KEY);
        if (this.subtype != null) {
            this.subTypeFormat = (this.subtype.getName().toLowerCase().equals("type0") || this.subtype.getName().toLowerCase().contains("cid")) ? 2 : 1;
        }
        int tmpInt = library.getInt(entries, FIRST_CHAR_KEY);
        if (tmpInt != 0) {
            this.firstchar = tmpInt;
        }
        int tmpInt2 = library.getInt(entries, LAST_CHAR_KEY);
        if (tmpInt2 != 0) {
            this.lastchar = tmpInt2;
        }
        this.basefont = "Serif";
        Object tmp = entries.get(BASEFONT_KEY);
        if (tmp != null && (tmp instanceof Name)) {
            this.basefont = ((Name) tmp).getName();
        }
    }

    protected String getCanonicalName(String name) {
        String[][] arr$ = TYPE1_FONT_NAME;
        for (String[] aTYPE1_FONT_NAME : arr$) {
            for (String anATYPE1_FONT_NAME : aTYPE1_FONT_NAME) {
                if (name.startsWith(anATYPE1_FONT_NAME)) {
                    return aTYPE1_FONT_NAME[0];
                }
            }
        }
        return null;
    }

    public String getBaseFont() {
        return this.basefont;
    }

    public Name getName() {
        return this.name;
    }

    public Name getSubType() {
        return this.subtype;
    }

    public int getSubTypeFormat() {
        return this.subTypeFormat;
    }

    public FontFile getFont() {
        return this.font;
    }

    public boolean isVerticalWriting() {
        return this.isVerticalWriting;
    }

    public boolean isAFMFont() {
        return this.isAFMFont;
    }

    public boolean isFontSubstitution() {
        return this.isFontSubstitution;
    }

    public boolean isCore14(String fontName) {
        String[] arr$ = CORE14;
        for (String aCORE14 : arr$) {
            if (fontName.startsWith(aCORE14)) {
                return true;
            }
        }
        return false;
    }

    @Override // org.icepdf.core.pobjects.Dictionary
    public String toString() {
        return ((Object) getPObjectReference()) + " FONT= " + this.basefont + " " + this.entries.toString();
    }

    public Resources getParentResource() {
        return this.parentResource;
    }

    public void setParentResource(Resources parentResource) {
        this.parentResource = parentResource;
    }
}
