package org.icepdf.core.pobjects.fonts.ofont;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.awt.GraphicsEnvironment;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.rtf.RTFGenerator;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.Reference;
import org.icepdf.core.pobjects.Stream;
import org.icepdf.core.pobjects.fonts.AFM;
import org.icepdf.core.pobjects.fonts.FontDescriptor;
import org.icepdf.core.pobjects.graphics.Separation;
import org.icepdf.core.util.FontUtil;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.PdfOps;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/fonts/ofont/Font.class */
public class Font extends org.icepdf.core.pobjects.fonts.Font {
    private Encoding encoding;
    private Name encodingName;
    private List widths;
    private HashMap<Integer, Float> cidWidths;
    private char[] cMap;
    private CMap toUnicodeCMap;
    protected AFM afm;
    protected int style;
    private static final Logger logger = Logger.getLogger(Font.class.toString());
    public static final Name BASE_ENCODING_KEY = new Name("BaseEncoding");
    public static final Name ENCODING_KEY = new Name(Constants._ATT_ENCODING);
    public static final Name TOUNICODE_KEY = new Name("ToUnicode");
    public static final Name DIFFERENCES_KEY = new Name("Differences");
    public static final Name WIDTHS_KEY = new Name("Widths");
    public static final Name FIRST_CHAR_KEY = new Name("FirstChar");
    public static final Name W_KEY = new Name(PdfOps.W_TOKEN);
    public static final Name FONT_DESCRIPTOR_KEY = new Name("FontDescriptor");
    public static final Name DESCENDANT_FONTS_KEY = new Name("DescendantFonts");
    public static final Name NONE_KEY = new Name(Separation.COLORANT_NONE);
    public static final Name STANDARD_ENCODING_KEY = new Name("StandardEncoding");
    public static final Name MACROMAN_ENCODING_KEY = new Name("MacRomanEncoding");
    public static final Name WINANSI_ENCODING_KEY = new Name("WinAnsiEncoding");
    public static final Name PDF_DOC_ENCODING_KEY = new Name("PDFDocEncoding");
    private static final java.awt.Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
    static final String[][] type1Diff = {new String[]{"Bookman-Demi", "URWBookmanL-DemiBold", "Arial"}, new String[]{"Bookman-DemiItalic", "URWBookmanL-DemiBoldItal", "Arial"}, new String[]{"Bookman-Light", "URWBookmanL-Ligh", "Arial"}, new String[]{"Bookman-LightItalic", "URWBookmanL-LighItal", "Arial"}, new String[]{"Courier", "Nimbus Mono L Regular", "Nimbus Mono L"}, new String[]{"Courier-Oblique", "Nimbus Mono L Regular Oblique", "Nimbus Mono L"}, new String[]{"Courier-Bold", "Nimbus Mono L Bold", "Nimbus Mono L"}, new String[]{"Courier-BoldOblique", "Nimbus Mono L Bold Oblique", "Nimbus Mono L"}, new String[]{"AvantGarde-Book", "URWGothicL-Book", "Arial"}, new String[]{"AvantGarde-BookOblique", "URWGothicL-BookObli", "Arial"}, new String[]{"AvantGarde-Demi", "URWGothicL-Demi", "Arial"}, new String[]{"AvantGarde-DemiOblique", "URWGothicL-DemiObli", "Arial"}, new String[]{RTFGenerator.defaultFontFamily, "Nimbus Sans L Regular", "Nimbus Sans L"}, new String[]{"Helvetica-Oblique", "Nimbus Sans L Regular Italic", "Nimbus Sans L"}, new String[]{"Helvetica-Bold", "Nimbus Sans L Bold", "Nimbus Sans L"}, new String[]{"Helvetica-BoldOblique", "Nimbus Sans L Bold Italic", "Nimbus Sans L"}, new String[]{"Helvetica-Narrow", "Nimbus Sans L Regular Condensed", "Nimbus Sans L"}, new String[]{"Helvetica-Narrow-Oblique", "Nimbus Sans L Regular Condensed Italic", "Nimbus Sans L"}, new String[]{"Helvetica-Narrow-Bold", "Nimbus Sans L Bold Condensed", "Nimbus Sans L"}, new String[]{"Helvetica-Narrow-BoldOblique", "Nimbus Sans L Bold Condensed Italic", "Nimbus Sans L"}, new String[]{"Helvetica-Condensed", "Nimbus Sans L Regular Condensed", "Nimbus Sans L"}, new String[]{"Helvetica-Condensed-Oblique", "Nimbus Sans L Regular Condensed Italic", "Nimbus Sans L"}, new String[]{"Helvetica-Condensed-Bold", "Nimbus Sans L Bold Condensed", "Nimbus Sans L"}, new String[]{"Helvetica-Condensed-BoldOblique", "Nimbus Sans L Bold Condensed Italic", "Nimbus Sans L"}, new String[]{"Palatino-Roman", "URWPalladioL-Roma", "Arial"}, new String[]{"Palatino-Italic", "URWPalladioL-Ital", "Arial"}, new String[]{"Palatino-Bold", "URWPalladioL-Bold", "Arial"}, new String[]{"Palatino-BoldItalic", "URWPalladioL-BoldItal", "Arial"}, new String[]{"NewCenturySchlbk-Roman", "CenturySchL-Roma", "Arial"}, new String[]{"NewCenturySchlbk-Italic", "CenturySchL-Ital", "Arial"}, new String[]{"NewCenturySchlbk-Bold", "CenturySchL-Bold", "Arial"}, new String[]{"NewCenturySchlbk-BoldItalic", "CenturySchL-BoldItal", "Arial"}, new String[]{"Times-Roman", "Nimbus Roman No9 L Regular", "Nimbus Roman No9 L"}, new String[]{"Times-Italic", "Nimbus Roman No9 L Regular Italic", "Nimbus Roman No9 L"}, new String[]{"Times-Bold", "Nimbus Roman No9 L Medium", "Nimbus Roman No9 L"}, new String[]{"Times-BoldItalic", "Nimbus Roman No9 L Medium Italic", "Nimbus Roman No9 L"}, new String[]{"Symbol", "Standard Symbols L", "Standard Symbols L"}, new String[]{"ZapfChancery-MediumItalic", "URWChanceryL-MediItal", "Arial"}, new String[]{"ZapfDingbats", "Dingbats", "Dingbats"}};

    public Font(Library library, HashMap entries) {
        super(library, entries);
        this.cMap = new char[256];
        char c2 = 0;
        while (true) {
            char i2 = c2;
            if (i2 >= 256) {
                break;
            }
            this.cMap[i2] = i2;
            c2 = (char) (i2 + 1);
        }
        this.style = FontUtil.guessAWTFontStyle(this.basefont);
        this.basefont = cleanFontName(this.basefont);
        if (this.subtype.equals("Type3")) {
            this.basefont = "Symbol";
            this.encoding = Encoding.getSymbol();
        }
        if (this.subtype.equals("Type1")) {
            if (this.basefont.equals("Symbol")) {
                this.encoding = Encoding.getSymbol();
            } else if (this.basefont.equalsIgnoreCase("ZapfDingbats") && this.subtype.equals("Type1")) {
                this.encoding = Encoding.getZapfDingBats();
            } else {
                String[][] arr$ = type1Diff;
                int len$ = arr$.length;
                int i$ = 0;
                while (true) {
                    if (i$ >= len$) {
                        break;
                    }
                    String[] aType1Diff = arr$[i$];
                    if (!this.basefont.equals(aType1Diff[0])) {
                        i$++;
                    } else {
                        this.encodingName = STANDARD_ENCODING_KEY;
                        this.encoding = Encoding.getStandard();
                        break;
                    }
                }
            }
        }
        if (this.subtype.equals("TrueType") && this.basefont.equals("Symbol")) {
            this.encodingName = WINANSI_ENCODING_KEY;
            this.encoding = Encoding.getWinAnsi();
        }
    }

    @Override // org.icepdf.core.pobjects.fonts.Font, org.icepdf.core.pobjects.Dictionary
    public synchronized void init() {
        String fontName;
        AFM a2;
        String name;
        Object afm;
        if (this.inited) {
            return;
        }
        if (this.encoding != null) {
            char c2 = 0;
            while (true) {
                char i2 = c2;
                if (i2 >= 256) {
                    break;
                }
                this.cMap[i2] = this.encoding.get(i2);
                c2 = (char) (i2 + 1);
            }
        }
        Object objectUnicode = this.library.getObject(this.entries, TOUNICODE_KEY);
        if (objectUnicode != null && (objectUnicode instanceof Stream)) {
            this.toUnicodeCMap = new CMap(this.library, new HashMap(), (Stream) objectUnicode);
            this.toUnicodeCMap.init();
        }
        Object o2 = this.library.getObject(this.entries, ENCODING_KEY);
        if (o2 != null) {
            if (o2 instanceof HashMap) {
                HashMap encoding = (HashMap) o2;
                setBaseEncoding(this.library.getName(encoding, BASE_ENCODING_KEY));
                List differences = (List) this.library.getObject(encoding, DIFFERENCES_KEY);
                if (differences != null) {
                    int c3 = 0;
                    for (Object oo : differences) {
                        if (oo instanceof Number) {
                            c3 = ((Number) oo).intValue();
                        } else if (oo instanceof Name) {
                            String n2 = oo.toString();
                            int c1 = Encoding.getUV(n2);
                            if (c1 == -1 && n2.charAt(0) == 'a') {
                                try {
                                    c1 = Integer.parseInt(n2.substring(1));
                                } catch (Exception e2) {
                                    logger.log(Level.FINE, "Error parings font differences");
                                }
                            }
                            this.cMap[c3] = (char) c1;
                            c3++;
                        }
                    }
                }
            } else if (o2 instanceof Name) {
                setBaseEncoding((Name) o2);
            }
        }
        this.widths = (List) this.library.getObject(this.entries, WIDTHS_KEY);
        if (this.widths != null) {
            Object o3 = this.library.getObject(this.entries, FIRST_CHAR_KEY);
            if (o3 != null) {
                this.firstchar = ((Number) o3).intValue();
            }
        } else if (this.library.getObject(this.entries, W_KEY) != null) {
            this.cidWidths = calculateCIDWidths();
            this.firstchar = 0;
            this.isAFMFont = false;
        } else {
            this.isAFMFont = false;
        }
        Object of = this.library.getObject(this.entries, FONT_DESCRIPTOR_KEY);
        if (of instanceof FontDescriptor) {
            this.fontDescriptor = (FontDescriptor) of;
            this.fontDescriptor.init();
        }
        if (this.fontDescriptor == null && this.basefont != null && (afm = AFM.AFMs.get(this.basefont.toLowerCase())) != null && (afm instanceof AFM)) {
            AFM fontMetrix = (AFM) afm;
            this.fontDescriptor = FontDescriptor.createDescriptor(this.library, fontMetrix);
            this.fontDescriptor.init();
        }
        if (this.fontDescriptor != null && (name = this.fontDescriptor.getFontName()) != null && name.length() > 0) {
            this.basefont = cleanFontName(name);
        }
        if (this.fontDescriptor != null && (this.fontDescriptor.getFlags() & 64) != 0 && this.encoding == null) {
            this.encodingName = STANDARD_ENCODING_KEY;
            this.encoding = Encoding.getStandard();
        }
        Object descendant = this.library.getObject(this.entries, DESCENDANT_FONTS_KEY);
        if (descendant != null) {
            List tmp = (List) descendant;
            if (tmp.get(0) instanceof Reference) {
                Object fontReference = this.library.getObject((Reference) tmp.get(0));
                if (fontReference instanceof Font) {
                    Font desendant = (Font) fontReference;
                    desendant.toUnicodeCMap = this.toUnicodeCMap;
                    desendant.init();
                    this.cidWidths = desendant.cidWidths;
                    if (this.fontDescriptor == null) {
                        this.fontDescriptor = desendant.fontDescriptor;
                        String name2 = this.fontDescriptor.getFontName();
                        if (name2 != null) {
                            this.basefont = cleanFontName(name2);
                        }
                    }
                }
            }
        }
        if (this.subtype.equals("Type1") && (a2 = AFM.AFMs.get(this.basefont.toLowerCase())) != null && a2.getFontName() != null) {
            this.afm = a2;
        }
        if (this.subtype.equals("Type1")) {
            String[][] arr$ = type1Diff;
            int len$ = arr$.length;
            int i$ = 0;
            while (true) {
                if (i$ >= len$) {
                    break;
                }
                String[] aType1Diff = arr$[i$];
                if (this.basefont.equals(aType1Diff[0])) {
                    java.awt.Font f2 = new java.awt.Font(aType1Diff[1], this.style, 12);
                    if (f2.getFamily().equals(aType1Diff[2])) {
                        this.basefont = aType1Diff[1];
                        break;
                    }
                }
                i$++;
            }
        }
        this.isFontSubstitution = true;
        if (this.fontDescriptor != null && this.fontDescriptor.getEmbeddedFont() != null) {
            this.font = this.fontDescriptor.getEmbeddedFont();
            this.isFontSubstitution = false;
            this.isAFMFont = false;
        }
        if (this.font == null && this.basefont != null) {
            java.awt.Font[] arr$2 = fonts;
            int len$2 = arr$2.length;
            int i$2 = 0;
            while (true) {
                if (i$2 >= len$2) {
                    break;
                }
                java.awt.Font font1 = arr$2[i$2];
                StringTokenizer st = new StringTokenizer(font1.getPSName(), " ", false);
                String str = "";
                while (true) {
                    fontName = str;
                    if (!st.hasMoreElements()) {
                        break;
                    } else {
                        str = fontName + st.nextElement2();
                    }
                }
                if (!fontName.equalsIgnoreCase(this.basefont)) {
                    i$2++;
                } else {
                    this.font = new OFont(new java.awt.Font(font1.getFamily(), this.style, 1));
                    this.basefont = font1.getPSName();
                    this.isFontSubstitution = true;
                    break;
                }
            }
        }
        if (this.font == null && this.basefont != null) {
            String fontFamily = FontUtil.guessFamily(this.basefont);
            java.awt.Font[] arr$3 = fonts;
            int len$3 = arr$3.length;
            int i$3 = 0;
            while (true) {
                if (i$3 >= len$3) {
                    break;
                }
                java.awt.Font font12 = arr$3[i$3];
                if (!FontUtil.normalizeString(font12.getFamily()).equalsIgnoreCase(fontFamily)) {
                    i$3++;
                } else {
                    this.font = new OFont(new java.awt.Font(font12.getFamily(), this.style, 1));
                    this.basefont = font12.getFontName();
                    this.isFontSubstitution = true;
                    break;
                }
            }
        }
        if (this.font == null) {
            try {
                this.font = new OFont(java.awt.Font.getFont(this.basefont, new java.awt.Font(this.basefont, this.style, 12)));
                this.basefont = this.font.getName();
            } catch (Exception e3) {
                if (logger.isLoggable(Level.WARNING)) {
                    logger.warning("Error creating awt.font for: " + ((Object) this.entries));
                }
            }
        }
        if (!this.isFontSubstitution && this.font != null && !this.font.getName().toLowerCase().contains(this.font.getFamily().toLowerCase())) {
            if (this.font.getName().toLowerCase().contains("times new roman") || this.font.getName().toLowerCase().contains("timesnewroman") || this.font.getName().toLowerCase().contains("bodoni") || this.font.getName().toLowerCase().contains("garamond") || this.font.getName().toLowerCase().contains("minion web") || this.font.getName().toLowerCase().contains("stone serif") || this.font.getName().toLowerCase().contains("stoneserif") || this.font.getName().toLowerCase().contains("georgia") || this.font.getName().toLowerCase().contains("bitstream cyberbit")) {
                this.font = new OFont(new java.awt.Font("serif", this.font.getStyle(), (int) this.font.getSize()));
                this.basefont = "serif";
            } else if (this.font.getName().toLowerCase().contains("helvetica") || this.font.getName().toLowerCase().contains("arial") || this.font.getName().toLowerCase().contains("trebuchet") || this.font.getName().toLowerCase().contains("avant garde gothic") || this.font.getName().toLowerCase().contains("avantgardegothic") || this.font.getName().toLowerCase().contains("verdana") || this.font.getName().toLowerCase().contains("univers") || this.font.getName().toLowerCase().contains("futura") || this.font.getName().toLowerCase().contains("stone sans") || this.font.getName().toLowerCase().contains("stonesans") || this.font.getName().toLowerCase().contains("gill sans") || this.font.getName().toLowerCase().contains("gillsans") || this.font.getName().toLowerCase().contains("akzidenz") || this.font.getName().toLowerCase().contains("grotesk")) {
                this.font = new OFont(new java.awt.Font("sansserif", this.font.getStyle(), (int) this.font.getSize()));
                this.basefont = "sansserif";
            } else if (this.font.getName().toLowerCase().contains("courier") || this.font.getName().toLowerCase().contains("courier new") || this.font.getName().toLowerCase().contains("couriernew") || this.font.getName().toLowerCase().contains("prestige") || this.font.getName().toLowerCase().contains("eversonmono") || this.font.getName().toLowerCase().contains("Everson Mono")) {
                this.font = new OFont(new java.awt.Font("monospaced", this.font.getStyle(), (int) this.font.getSize()));
                this.basefont = "monospaced";
            } else {
                this.font = new OFont(new java.awt.Font("serif", this.font.getStyle(), (int) this.font.getSize()));
                this.basefont = "serif";
            }
        }
        if (this.font == null) {
            this.font = new OFont(new java.awt.Font("serif", this.style, 12));
            this.basefont = "serif";
        }
        setWidth();
        this.font = this.font.deriveFont(this.encoding, this.toUnicodeCMap);
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(((Object) this.name) + " - " + ((Object) this.encodingName) + " " + this.basefont + " " + this.font.toString() + " " + this.isFontSubstitution);
        }
        this.inited = true;
    }

    private void setBaseEncoding(Name baseEncoding) {
        if (baseEncoding == null) {
            this.encodingName = NONE_KEY;
            return;
        }
        this.encodingName = baseEncoding;
        if (baseEncoding.equals(STANDARD_ENCODING_KEY)) {
            this.encoding = Encoding.getStandard();
        } else if (baseEncoding.equals(MACROMAN_ENCODING_KEY)) {
            this.encoding = Encoding.getMacRoman();
        } else if (baseEncoding.equals(WINANSI_ENCODING_KEY)) {
            this.encoding = Encoding.getWinAnsi();
        } else if (baseEncoding.equals(PDF_DOC_ENCODING_KEY)) {
            this.encoding = Encoding.getPDFDoc();
        }
        if (this.encoding != null) {
            char c2 = 0;
            while (true) {
                char i2 = c2;
                if (i2 < 256) {
                    this.cMap[i2] = this.encoding.get(i2);
                    c2 = (char) (i2 + 1);
                } else {
                    return;
                }
            }
        }
    }

    @Override // org.icepdf.core.pobjects.fonts.Font, org.icepdf.core.pobjects.Dictionary
    public String toString() {
        return "FONT= " + ((Object) this.encodingName) + " " + this.entries.toString();
    }

    private void setWidth() {
        float missingWidth = 0.0f;
        float ascent = 0.0f;
        float descent = 0.0f;
        if (this.fontDescriptor != null && this.fontDescriptor.getMissingWidth() > 0.0f) {
            missingWidth = this.fontDescriptor.getMissingWidth() / 1000.0f;
            ascent = this.fontDescriptor.getAscent() / 1000.0f;
            descent = this.fontDescriptor.getDescent() / 1000.0f;
        }
        if (this.widths == null) {
            if (this.cidWidths != null) {
                this.font = this.font.deriveFont(this.cidWidths, this.firstchar, missingWidth, ascent, descent, (char[]) null);
                return;
            } else {
                if (this.afm != null && this.isAFMFont) {
                    this.font = this.font.deriveFont(this.afm.getWidths(), this.firstchar, missingWidth, ascent, descent, this.cMap);
                    return;
                }
                return;
            }
        }
        float[] newWidth = new float[256 - this.firstchar];
        int max = this.widths.size();
        for (int i2 = 0; i2 < max; i2++) {
            if (this.widths.get(i2) != null) {
                newWidth[i2] = ((Number) this.widths.get(i2)).floatValue() / 1000.0f;
            }
        }
        this.font = this.font.deriveFont(newWidth, this.firstchar, missingWidth, ascent, descent, this.cMap);
    }

    private String cleanFontName(String fontName) {
        String fontName2 = FontUtil.removeBaseFontSubset(fontName);
        if ((this.subtype.equals("Type0") || this.subtype.equals("Type1") || this.subtype.equals("MMType1") || this.subtype.equals("TrueType")) && fontName2 != null) {
            fontName2 = fontName2.replace(',', '-');
        }
        return fontName2;
    }

    private HashMap<Integer, Float> calculateCIDWidths() {
        HashMap<Integer, Float> cidWidths = new HashMap<>(75);
        Object o2 = this.library.getObject(this.entries, W_KEY);
        if (o2 instanceof List) {
            List cidWidth = (List) o2;
            int i2 = 0;
            int max = cidWidth.size() - 1;
            while (i2 < max) {
                Object current = cidWidth.get(i2);
                Object peek = cidWidth.get(i2 + 1);
                if ((current instanceof Integer) && (peek instanceof List)) {
                    int currentChar = ((Integer) current).intValue();
                    List subWidth = (List) peek;
                    int subMax = subWidth.size();
                    for (int j2 = 0; j2 < subMax; j2++) {
                        if (subWidth.get(j2) instanceof Integer) {
                            cidWidths.put(Integer.valueOf(currentChar + j2), Float.valueOf(((Integer) subWidth.get(j2)).intValue() / 1000.0f));
                        } else if (subWidth.get(j2) instanceof Float) {
                            cidWidths.put(Integer.valueOf(currentChar + j2), Float.valueOf(((Float) subWidth.get(j2)).floatValue() / 1000.0f));
                        }
                    }
                    i2++;
                }
                if ((current instanceof Integer) && (peek instanceof Integer)) {
                    for (int j3 = ((Integer) current).intValue(); j3 <= ((Integer) peek).intValue(); j3++) {
                        int currentChar2 = j3;
                        if (cidWidth.get(i2 + 2) instanceof Integer) {
                            cidWidths.put(Integer.valueOf(currentChar2), Float.valueOf(((Integer) cidWidth.get(i2 + 2)).intValue() / 1000.0f));
                        } else if (cidWidth.get(i2 + 2) instanceof Float) {
                            cidWidths.put(Integer.valueOf(currentChar2), Float.valueOf(((Float) cidWidth.get(i2 + 2)).floatValue() / 1000.0f));
                        }
                    }
                    i2 += 2;
                }
                i2++;
            }
        }
        return cidWidths;
    }
}
