package javax.swing.text.rtf;

import com.intel.bluetooth.BlueCoveImpl;
import com.sun.org.apache.xml.internal.serialize.LineSeparator;
import com.sun.org.apache.xml.internal.utils.res.XResourceBundle;
import com.sun.xml.internal.ws.policy.PolicyConstants;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Pagination;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.TabStop;
import org.apache.commons.math3.geometry.VectorFormat;
import org.apache.commons.net.ftp.FTP;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:javax/swing/text/rtf/RTFReader.class */
class RTFReader extends RTFParser {
    StyledDocument target;
    Destination rtfDestination;
    Color[] colorTable;
    Map<Integer, Style> characterStyles;
    Map<Integer, Style> paragraphStyles;
    Map<Integer, Style> sectionStyles;
    boolean ignoreGroupIfUnknownKeyword;
    int skippingCharacters;
    private static Dictionary<String, RTFAttribute> straightforwardAttributes = RTFAttributes.attributesByKeyword();
    static Dictionary<String, String> textKeywords;
    static final String TabAlignmentKey = "tab_alignment";
    static final String TabLeaderKey = "tab_leader";
    static Dictionary<String, char[]> characterSets;
    static boolean useNeXTForAnsi;
    Dictionary<Object, Object> parserState = new Hashtable();
    Dictionary<Integer, String> fontTable = new Hashtable();
    int rtfversion = -1;
    private MockAttributeSet mockery = new MockAttributeSet();
    MutableAttributeSet documentAttributes = new SimpleAttributeSet();

    /* loaded from: rt.jar:javax/swing/text/rtf/RTFReader$Destination.class */
    interface Destination {
        void handleBinaryBlob(byte[] bArr);

        void handleText(String str);

        boolean handleKeyword(String str);

        boolean handleKeyword(String str, int i2);

        void begingroup();

        void endgroup(Dictionary dictionary);

        void close();
    }

    static {
        textKeywords = null;
        textKeywords = new Hashtable();
        textKeywords.put(FXMLLoader.ESCAPE_PREFIX, FXMLLoader.ESCAPE_PREFIX);
        textKeywords.put(VectorFormat.DEFAULT_PREFIX, VectorFormat.DEFAULT_PREFIX);
        textKeywords.put("}", "}");
        textKeywords.put(" ", " ");
        textKeywords.put("~", " ");
        textKeywords.put("_", "‑");
        textKeywords.put(Pagination.STYLE_CLASS_BULLET, "•");
        textKeywords.put("emdash", "—");
        textKeywords.put("emspace", "\u2003");
        textKeywords.put("endash", "–");
        textKeywords.put("enspace", "\u2002");
        textKeywords.put("ldblquote", "“");
        textKeywords.put("lquote", "‘");
        textKeywords.put("ltrmark", "\u200e");
        textKeywords.put("rdblquote", "”");
        textKeywords.put("rquote", "’");
        textKeywords.put("rtlmark", "\u200f");
        textKeywords.put("tab", "\t");
        textKeywords.put("zwj", "\u200d");
        textKeywords.put("zwnj", "\u200c");
        textKeywords.put(LanguageTag.SEP, "‧");
        useNeXTForAnsi = false;
        characterSets = new Hashtable();
    }

    public RTFReader(StyledDocument styledDocument) {
        this.target = styledDocument;
    }

    @Override // javax.swing.text.rtf.RTFParser
    public void handleBinaryBlob(byte[] bArr) {
        if (this.skippingCharacters > 0) {
            this.skippingCharacters--;
        }
    }

    @Override // javax.swing.text.rtf.RTFParser
    public void handleText(String str) {
        if (this.skippingCharacters > 0) {
            if (this.skippingCharacters >= str.length()) {
                this.skippingCharacters -= str.length();
                return;
            } else {
                str = str.substring(this.skippingCharacters);
                this.skippingCharacters = 0;
            }
        }
        if (this.rtfDestination != null) {
            this.rtfDestination.handleText(str);
        } else {
            warning("Text with no destination. oops.");
        }
    }

    Color defaultColor() {
        return Color.black;
    }

    @Override // javax.swing.text.rtf.RTFParser
    public void begingroup() {
        if (this.skippingCharacters > 0) {
            this.skippingCharacters = 0;
        }
        Object obj = this.parserState.get("_savedState");
        if (obj != null) {
            this.parserState.remove("_savedState");
        }
        Dictionary dictionary = (Dictionary) ((Hashtable) this.parserState).clone();
        if (obj != null) {
            dictionary.put("_savedState", obj);
        }
        this.parserState.put("_savedState", dictionary);
        if (this.rtfDestination != null) {
            this.rtfDestination.begingroup();
        }
    }

    @Override // javax.swing.text.rtf.RTFParser
    public void endgroup() {
        if (this.skippingCharacters > 0) {
            this.skippingCharacters = 0;
        }
        Dictionary<Object, Object> dictionary = (Dictionary) this.parserState.get("_savedState");
        Destination destination = (Destination) dictionary.get("dst");
        if (destination != this.rtfDestination) {
            this.rtfDestination.close();
            this.rtfDestination = destination;
        }
        Dictionary<Object, Object> dictionary2 = this.parserState;
        this.parserState = dictionary;
        if (this.rtfDestination != null) {
            this.rtfDestination.endgroup(dictionary2);
        }
    }

    protected void setRTFDestination(Destination destination) {
        Dictionary dictionary = (Dictionary) this.parserState.get("_savedState");
        if (dictionary != null && this.rtfDestination != dictionary.get("dst")) {
            warning("Warning, RTF destination overridden, invalid RTF.");
            this.rtfDestination.close();
        }
        this.rtfDestination = destination;
        this.parserState.put("dst", this.rtfDestination);
    }

    @Override // javax.swing.text.rtf.RTFParser, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        Enumeration<?> attributeNames = this.documentAttributes.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            Object objNextElement = attributeNames.nextElement2();
            this.target.putProperty(objNextElement, this.documentAttributes.getAttribute(objNextElement));
        }
        warning("RTF filter done.");
        super.close();
    }

    @Override // javax.swing.text.rtf.RTFParser
    public boolean handleKeyword(String str) {
        boolean z2 = this.ignoreGroupIfUnknownKeyword;
        if (this.skippingCharacters > 0) {
            this.skippingCharacters--;
            return true;
        }
        this.ignoreGroupIfUnknownKeyword = false;
        String str2 = textKeywords.get(str);
        if (str2 != null) {
            handleText(str2);
            return true;
        }
        if (str.equals("fonttbl")) {
            setRTFDestination(new FonttblDestination());
            return true;
        }
        if (str.equals("colortbl")) {
            setRTFDestination(new ColortblDestination());
            return true;
        }
        if (str.equals(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_STYLESHEET_STRING)) {
            setRTFDestination(new StylesheetDestination());
            return true;
        }
        if (str.equals("info")) {
            setRTFDestination(new InfoDestination());
            return false;
        }
        if (str.equals(BlueCoveImpl.STACK_OSX)) {
            setCharacterSet(BlueCoveImpl.STACK_OSX);
            return true;
        }
        if (str.equals("ansi")) {
            if (useNeXTForAnsi) {
                setCharacterSet("NeXT");
                return true;
            }
            setCharacterSet("ansi");
            return true;
        }
        if (str.equals(com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.NEXT)) {
            setCharacterSet("NeXT");
            return true;
        }
        if (str.equals("pc")) {
            setCharacterSet("cpg437");
            return true;
        }
        if (str.equals("pca")) {
            setCharacterSet("cpg850");
            return true;
        }
        if (str.equals("*")) {
            this.ignoreGroupIfUnknownKeyword = true;
            return true;
        }
        if (this.rtfDestination != null && this.rtfDestination.handleKeyword(str)) {
            return true;
        }
        if (str.equals("aftncn") || str.equals("aftnsep") || str.equals("aftnsepc") || str.equals("annotation") || str.equals("atnauthor") || str.equals("atnicn") || str.equals("atnid") || str.equals("atnref") || str.equals("atntime") || str.equals("atrfend") || str.equals("atrfstart") || str.equals("bkmkend") || str.equals("bkmkstart") || str.equals("datafield") || str.equals("do") || str.equals("dptxbxtext") || str.equals("falt") || str.equals("field") || str.equals(DeploymentDescriptorParser.ATTR_FILE) || str.equals("filetbl") || str.equals("fname") || str.equals("fontemb") || str.equals("fontfile") || str.equals("footer") || str.equals("footerf") || str.equals("footerl") || str.equals("footerr") || str.equals("footnote") || str.equals("ftncn") || str.equals("ftnsep") || str.equals("ftnsepc") || str.equals("header") || str.equals("headerf") || str.equals("headerl") || str.equals("headerr") || str.equals("keycode") || str.equals("nextfile") || str.equals("object") || str.equals("pict") || str.equals("pn") || str.equals("pnseclvl") || str.equals("pntxtb") || str.equals("pntxta") || str.equals("revtbl") || str.equals("rxe") || str.equals("tc") || str.equals("template") || str.equals("txe") || str.equals("xe")) {
            z2 = true;
        }
        if (z2) {
            setRTFDestination(new DiscardingDestination());
            return false;
        }
        return false;
    }

    @Override // javax.swing.text.rtf.RTFParser
    public boolean handleKeyword(String str, int i2) {
        boolean z2 = this.ignoreGroupIfUnknownKeyword;
        if (this.skippingCharacters > 0) {
            this.skippingCharacters--;
            return true;
        }
        this.ignoreGroupIfUnknownKeyword = false;
        if (str.equals("uc")) {
            this.parserState.put("UnicodeSkip", Integer.valueOf(i2));
            return true;
        }
        if (str.equals("u")) {
            if (i2 < 0) {
                i2 += 65536;
            }
            handleText((char) i2);
            Number number = (Number) this.parserState.get("UnicodeSkip");
            if (number != null) {
                this.skippingCharacters = number.intValue();
                return true;
            }
            this.skippingCharacters = 1;
            return true;
        }
        if (str.equals("rtf")) {
            this.rtfversion = i2;
            setRTFDestination(new DocumentDestination());
            return true;
        }
        if (str.startsWith("NeXT") || str.equals(PolicyConstants.VISIBILITY_VALUE_PRIVATE)) {
            z2 = true;
        }
        if (this.rtfDestination != null && this.rtfDestination.handleKeyword(str, i2)) {
            return true;
        }
        if (z2) {
            setRTFDestination(new DiscardingDestination());
            return false;
        }
        return false;
    }

    private void setTargetAttribute(String str, Object obj) {
    }

    public void setCharacterSet(String str) {
        Object characterSet;
        try {
            characterSet = getCharacterSet(str);
        } catch (Exception e2) {
            warning("Exception loading RTF character set \"" + str + "\": " + ((Object) e2));
            characterSet = null;
        }
        if (characterSet != null) {
            this.translationTable = (char[]) characterSet;
        } else {
            warning("Unknown RTF character set \"" + str + PdfOps.DOUBLE_QUOTE__TOKEN);
            if (!str.equals("ansi")) {
                try {
                    this.translationTable = (char[]) getCharacterSet("ansi");
                } catch (IOException e3) {
                    throw new InternalError("RTFReader: Unable to find character set resources (" + ((Object) e3) + ")", e3);
                }
            }
        }
        setTargetAttribute("rtfCharacterSet", str);
    }

    public static void defineCharacterSet(String str, char[] cArr) {
        if (cArr.length < 256) {
            throw new IllegalArgumentException("Translation table must have 256 entries.");
        }
        characterSets.put(str, cArr);
    }

    public static Object getCharacterSet(final String str) throws IOException {
        char[] charset = characterSets.get(str);
        if (charset == null) {
            charset = readCharset((InputStream) AccessController.doPrivileged(new PrivilegedAction<InputStream>() { // from class: javax.swing.text.rtf.RTFReader.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public InputStream run2() {
                    return RTFReader.class.getResourceAsStream("charsets/" + str + ".txt");
                }
            }));
            defineCharacterSet(str, charset);
        }
        return charset;
    }

    static char[] readCharset(InputStream inputStream) throws IOException {
        char[] cArr = new char[256];
        StreamTokenizer streamTokenizer = new StreamTokenizer(new BufferedReader(new InputStreamReader(inputStream, FTP.DEFAULT_CONTROL_ENCODING)));
        streamTokenizer.eolIsSignificant(false);
        streamTokenizer.commentChar(35);
        streamTokenizer.slashSlashComments(true);
        streamTokenizer.slashStarComments(true);
        for (int i2 = 0; i2 < 256; i2++) {
            try {
                if (streamTokenizer.nextToken() != -2) {
                    throw new IOException("Unexpected token in character set file");
                }
                cArr[i2] = (char) streamTokenizer.nval;
            } catch (Exception e2) {
                throw new IOException("Unable to read from character set file (" + ((Object) e2) + ")");
            }
        }
        return cArr;
    }

    static char[] readCharset(URL url) throws IOException {
        return readCharset(url.openStream());
    }

    /* loaded from: rt.jar:javax/swing/text/rtf/RTFReader$DiscardingDestination.class */
    class DiscardingDestination implements Destination {
        DiscardingDestination() {
        }

        @Override // javax.swing.text.rtf.RTFReader.Destination
        public void handleBinaryBlob(byte[] bArr) {
        }

        @Override // javax.swing.text.rtf.RTFReader.Destination
        public void handleText(String str) {
        }

        @Override // javax.swing.text.rtf.RTFReader.Destination
        public boolean handleKeyword(String str) {
            return true;
        }

        @Override // javax.swing.text.rtf.RTFReader.Destination
        public boolean handleKeyword(String str, int i2) {
            return true;
        }

        @Override // javax.swing.text.rtf.RTFReader.Destination
        public void begingroup() {
        }

        @Override // javax.swing.text.rtf.RTFReader.Destination
        public void endgroup(Dictionary dictionary) {
        }

        @Override // javax.swing.text.rtf.RTFReader.Destination
        public void close() {
        }
    }

    /* loaded from: rt.jar:javax/swing/text/rtf/RTFReader$FonttblDestination.class */
    class FonttblDestination implements Destination {
        int nextFontNumber;
        Integer fontNumberKey = null;
        String nextFontFamily;

        FonttblDestination() {
        }

        @Override // javax.swing.text.rtf.RTFReader.Destination
        public void handleBinaryBlob(byte[] bArr) {
        }

        @Override // javax.swing.text.rtf.RTFReader.Destination
        public void handleText(String str) {
            String strSubstring;
            int iIndexOf = str.indexOf(59);
            if (iIndexOf > -1) {
                strSubstring = str.substring(0, iIndexOf);
            } else {
                strSubstring = str;
            }
            if (this.nextFontNumber == -1 && this.fontNumberKey != null) {
                strSubstring = RTFReader.this.fontTable.get(this.fontNumberKey) + strSubstring;
            } else {
                this.fontNumberKey = Integer.valueOf(this.nextFontNumber);
            }
            RTFReader.this.fontTable.put(this.fontNumberKey, strSubstring);
            this.nextFontNumber = -1;
            this.nextFontFamily = null;
        }

        @Override // javax.swing.text.rtf.RTFReader.Destination
        public boolean handleKeyword(String str) {
            if (str.charAt(0) == 'f') {
                this.nextFontFamily = str.substring(1);
                return true;
            }
            return false;
        }

        @Override // javax.swing.text.rtf.RTFReader.Destination
        public boolean handleKeyword(String str, int i2) {
            if (str.equals(PdfOps.f_TOKEN)) {
                this.nextFontNumber = i2;
                return true;
            }
            return false;
        }

        @Override // javax.swing.text.rtf.RTFReader.Destination
        public void begingroup() {
        }

        @Override // javax.swing.text.rtf.RTFReader.Destination
        public void endgroup(Dictionary dictionary) {
        }

        @Override // javax.swing.text.rtf.RTFReader.Destination
        public void close() {
            Enumeration<Integer> enumerationKeys = RTFReader.this.fontTable.keys();
            RTFReader.this.warning("Done reading font table.");
            while (enumerationKeys.hasMoreElements()) {
                Integer numNextElement = enumerationKeys.nextElement2();
                RTFReader.this.warning("Number " + ((Object) numNextElement) + ": " + RTFReader.this.fontTable.get(numNextElement));
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/rtf/RTFReader$ColortblDestination.class */
    class ColortblDestination implements Destination {
        int red = 0;
        int green = 0;
        int blue = 0;
        Vector<Color> proTemTable = new Vector<>();

        public ColortblDestination() {
        }

        @Override // javax.swing.text.rtf.RTFReader.Destination
        public void handleText(String str) {
            for (int i2 = 0; i2 < str.length(); i2++) {
                if (str.charAt(i2) == ';') {
                    this.proTemTable.addElement(new Color(this.red, this.green, this.blue));
                }
            }
        }

        @Override // javax.swing.text.rtf.RTFReader.Destination
        public void close() {
            int size = this.proTemTable.size();
            RTFReader.this.warning("Done reading color table, " + size + " entries.");
            RTFReader.this.colorTable = new Color[size];
            this.proTemTable.copyInto(RTFReader.this.colorTable);
        }

        @Override // javax.swing.text.rtf.RTFReader.Destination
        public boolean handleKeyword(String str, int i2) {
            if (str.equals("red")) {
                this.red = i2;
                return true;
            }
            if (str.equals("green")) {
                this.green = i2;
                return true;
            }
            if (str.equals("blue")) {
                this.blue = i2;
                return true;
            }
            return false;
        }

        @Override // javax.swing.text.rtf.RTFReader.Destination
        public boolean handleKeyword(String str) {
            return false;
        }

        @Override // javax.swing.text.rtf.RTFReader.Destination
        public void begingroup() {
        }

        @Override // javax.swing.text.rtf.RTFReader.Destination
        public void endgroup(Dictionary dictionary) {
        }

        @Override // javax.swing.text.rtf.RTFReader.Destination
        public void handleBinaryBlob(byte[] bArr) {
        }
    }

    /* loaded from: rt.jar:javax/swing/text/rtf/RTFReader$StylesheetDestination.class */
    class StylesheetDestination extends DiscardingDestination implements Destination {
        Dictionary<Integer, StyleDefiningDestination> definedStyles;

        public StylesheetDestination() {
            super();
            this.definedStyles = new Hashtable();
        }

        @Override // javax.swing.text.rtf.RTFReader.DiscardingDestination, javax.swing.text.rtf.RTFReader.Destination
        public void begingroup() {
            RTFReader.this.setRTFDestination(new StyleDefiningDestination());
        }

        @Override // javax.swing.text.rtf.RTFReader.DiscardingDestination, javax.swing.text.rtf.RTFReader.Destination
        public void close() {
            HashMap map;
            HashMap map2 = new HashMap();
            HashMap map3 = new HashMap();
            HashMap map4 = new HashMap();
            Enumeration<StyleDefiningDestination> enumerationElements = this.definedStyles.elements();
            while (enumerationElements.hasMoreElements()) {
                StyleDefiningDestination styleDefiningDestinationNextElement = enumerationElements.nextElement2();
                Style styleRealize = styleDefiningDestinationNextElement.realize();
                RTFReader.this.warning("Style " + styleDefiningDestinationNextElement.number + " (" + styleDefiningDestinationNextElement.styleName + "): " + ((Object) styleRealize));
                String str = (String) styleRealize.getAttribute("style:type");
                if (str.equals(AbstractDocument.SectionElementName)) {
                    map = map4;
                } else if (str.equals("character")) {
                    map = map2;
                } else {
                    map = map3;
                }
                map.put(Integer.valueOf(styleDefiningDestinationNextElement.number), styleRealize);
            }
            if (!map2.isEmpty()) {
                RTFReader.this.characterStyles = map2;
            }
            if (!map3.isEmpty()) {
                RTFReader.this.paragraphStyles = map3;
            }
            if (!map4.isEmpty()) {
                RTFReader.this.sectionStyles = map4;
            }
        }

        /* loaded from: rt.jar:javax/swing/text/rtf/RTFReader$StylesheetDestination$StyleDefiningDestination.class */
        class StyleDefiningDestination extends AttributeTrackingDestination implements Destination {
            final int STYLENUMBER_NONE = 222;
            boolean additive;
            boolean characterStyle;
            boolean sectionStyle;
            public String styleName;
            public int number;
            int basedOn;
            int nextStyle;
            boolean hidden;
            Style realizedStyle;

            public StyleDefiningDestination() {
                super();
                this.STYLENUMBER_NONE = 222;
                this.additive = false;
                this.characterStyle = false;
                this.sectionStyle = false;
                this.styleName = null;
                this.number = 0;
                this.basedOn = 222;
                this.nextStyle = 222;
                this.hidden = false;
            }

            @Override // javax.swing.text.rtf.RTFReader.AttributeTrackingDestination, javax.swing.text.rtf.RTFReader.Destination
            public void handleText(String str) {
                if (this.styleName != null) {
                    this.styleName += str;
                } else {
                    this.styleName = str;
                }
            }

            @Override // javax.swing.text.rtf.RTFReader.AttributeTrackingDestination, javax.swing.text.rtf.RTFReader.Destination
            public void close() {
                int iIndexOf = this.styleName == null ? 0 : this.styleName.indexOf(59);
                if (iIndexOf > 0) {
                    this.styleName = this.styleName.substring(0, iIndexOf);
                }
                StylesheetDestination.this.definedStyles.put(Integer.valueOf(this.number), this);
                super.close();
            }

            @Override // javax.swing.text.rtf.RTFReader.AttributeTrackingDestination, javax.swing.text.rtf.RTFReader.Destination
            public boolean handleKeyword(String str) {
                if (str.equals(XResourceBundle.LANG_ADDITIVE)) {
                    this.additive = true;
                    return true;
                }
                if (str.equals("shidden")) {
                    this.hidden = true;
                    return true;
                }
                return super.handleKeyword(str);
            }

            @Override // javax.swing.text.rtf.RTFReader.AttributeTrackingDestination, javax.swing.text.rtf.RTFReader.Destination
            public boolean handleKeyword(String str, int i2) {
                if (i2 > 32767) {
                    i2 = 32767;
                } else if (i2 < -32767) {
                    i2 = -32767;
                }
                if (str.equals(PdfOps.s_TOKEN)) {
                    this.characterStyle = false;
                    this.sectionStyle = false;
                    this.number = i2;
                    return true;
                }
                if (str.equals(PdfOps.cs_TOKEN)) {
                    this.characterStyle = true;
                    this.sectionStyle = false;
                    this.number = i2;
                    return true;
                }
                if (str.equals("ds")) {
                    this.characterStyle = false;
                    this.sectionStyle = true;
                    this.number = i2;
                    return true;
                }
                if (str.equals("sbasedon")) {
                    this.basedOn = i2;
                    return true;
                }
                if (str.equals("snext")) {
                    this.nextStyle = i2;
                    return true;
                }
                return super.handleKeyword(str, i2);
            }

            public Style realize() {
                return realize(null);
            }

            private Style realize(Set<Integer> set) {
                StyleDefiningDestination styleDefiningDestination;
                StyleDefiningDestination styleDefiningDestination2;
                Style styleRealize = null;
                Style styleRealize2 = null;
                if (set == null) {
                    set = new HashSet();
                }
                if (this.realizedStyle != null) {
                    return this.realizedStyle;
                }
                if (this.basedOn != 222 && set.add(Integer.valueOf(this.basedOn)) && (styleDefiningDestination2 = StylesheetDestination.this.definedStyles.get(Integer.valueOf(this.basedOn))) != null && styleDefiningDestination2 != this) {
                    styleRealize = styleDefiningDestination2.realize(set);
                }
                this.realizedStyle = RTFReader.this.target.addStyle(this.styleName, styleRealize);
                if (this.characterStyle) {
                    this.realizedStyle.addAttributes(currentTextAttributes());
                    this.realizedStyle.addAttribute("style:type", "character");
                } else if (this.sectionStyle) {
                    this.realizedStyle.addAttributes(currentSectionAttributes());
                    this.realizedStyle.addAttribute("style:type", AbstractDocument.SectionElementName);
                } else {
                    this.realizedStyle.addAttributes(currentParagraphAttributes());
                    this.realizedStyle.addAttribute("style:type", AbstractDocument.ParagraphElementName);
                }
                if (this.nextStyle != 222 && (styleDefiningDestination = StylesheetDestination.this.definedStyles.get(Integer.valueOf(this.nextStyle))) != null) {
                    styleRealize2 = styleDefiningDestination.realize();
                }
                if (styleRealize2 != null) {
                    this.realizedStyle.addAttribute("style:nextStyle", styleRealize2);
                }
                this.realizedStyle.addAttribute("style:additive", Boolean.valueOf(this.additive));
                this.realizedStyle.addAttribute("style:hidden", Boolean.valueOf(this.hidden));
                return this.realizedStyle;
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/rtf/RTFReader$InfoDestination.class */
    class InfoDestination extends DiscardingDestination implements Destination {
        InfoDestination() {
            super();
        }
    }

    /* loaded from: rt.jar:javax/swing/text/rtf/RTFReader$AttributeTrackingDestination.class */
    abstract class AttributeTrackingDestination implements Destination {
        MutableAttributeSet characterAttributes = rootCharacterAttributes();
        MutableAttributeSet paragraphAttributes;
        MutableAttributeSet sectionAttributes;

        @Override // javax.swing.text.rtf.RTFReader.Destination
        public abstract void handleText(String str);

        public AttributeTrackingDestination() {
            RTFReader.this.parserState.put("chr", this.characterAttributes);
            this.paragraphAttributes = rootParagraphAttributes();
            RTFReader.this.parserState.put("pgf", this.paragraphAttributes);
            this.sectionAttributes = rootSectionAttributes();
            RTFReader.this.parserState.put("sec", this.sectionAttributes);
        }

        @Override // javax.swing.text.rtf.RTFReader.Destination
        public void handleBinaryBlob(byte[] bArr) {
            RTFReader.this.warning("Unexpected binary data in RTF file.");
        }

        @Override // javax.swing.text.rtf.RTFReader.Destination
        public void begingroup() {
            MutableAttributeSet mutableAttributeSetCurrentTextAttributes = currentTextAttributes();
            MutableAttributeSet mutableAttributeSetCurrentParagraphAttributes = currentParagraphAttributes();
            AttributeSet attributeSetCurrentSectionAttributes = currentSectionAttributes();
            this.characterAttributes = new SimpleAttributeSet();
            this.characterAttributes.addAttributes(mutableAttributeSetCurrentTextAttributes);
            RTFReader.this.parserState.put("chr", this.characterAttributes);
            this.paragraphAttributes = new SimpleAttributeSet();
            this.paragraphAttributes.addAttributes(mutableAttributeSetCurrentParagraphAttributes);
            RTFReader.this.parserState.put("pgf", this.paragraphAttributes);
            this.sectionAttributes = new SimpleAttributeSet();
            this.sectionAttributes.addAttributes(attributeSetCurrentSectionAttributes);
            RTFReader.this.parserState.put("sec", this.sectionAttributes);
        }

        @Override // javax.swing.text.rtf.RTFReader.Destination
        public void endgroup(Dictionary dictionary) {
            this.characterAttributes = (MutableAttributeSet) RTFReader.this.parserState.get("chr");
            this.paragraphAttributes = (MutableAttributeSet) RTFReader.this.parserState.get("pgf");
            this.sectionAttributes = (MutableAttributeSet) RTFReader.this.parserState.get("sec");
        }

        @Override // javax.swing.text.rtf.RTFReader.Destination
        public void close() {
        }

        @Override // javax.swing.text.rtf.RTFReader.Destination
        public boolean handleKeyword(String str) {
            boolean z2;
            if (!str.equals("ulnone")) {
                RTFAttribute rTFAttribute = (RTFAttribute) RTFReader.straightforwardAttributes.get(str);
                if (rTFAttribute != null) {
                    switch (rTFAttribute.domain()) {
                        case 0:
                            z2 = rTFAttribute.set(this.characterAttributes);
                            break;
                        case 1:
                            z2 = rTFAttribute.set(this.paragraphAttributes);
                            break;
                        case 2:
                            z2 = rTFAttribute.set(this.sectionAttributes);
                            break;
                        case 3:
                            z2 = rTFAttribute.set(RTFReader.this.documentAttributes);
                            break;
                        case 4:
                            RTFReader.this.mockery.backing = RTFReader.this.parserState;
                            z2 = rTFAttribute.set(RTFReader.this.mockery);
                            RTFReader.this.mockery.backing = null;
                            break;
                        default:
                            z2 = false;
                            break;
                    }
                    if (z2) {
                        return true;
                    }
                }
                if (str.equals("plain")) {
                    resetCharacterAttributes();
                    return true;
                }
                if (str.equals("pard")) {
                    resetParagraphAttributes();
                    return true;
                }
                if (str.equals("sectd")) {
                    resetSectionAttributes();
                    return true;
                }
                return false;
            }
            return handleKeyword("ul", 0);
        }

        @Override // javax.swing.text.rtf.RTFReader.Destination
        public boolean handleKeyword(String str, int i2) {
            Integer numValueOf;
            boolean z2;
            boolean z3 = i2 != 0;
            if (str.equals("fc")) {
                str = "cf";
            }
            if (str.equals(PdfOps.f_TOKEN)) {
                RTFReader.this.parserState.put(str, Integer.valueOf(i2));
                return true;
            }
            if (!str.equals("cf")) {
                RTFAttribute rTFAttribute = (RTFAttribute) RTFReader.straightforwardAttributes.get(str);
                if (rTFAttribute != null) {
                    switch (rTFAttribute.domain()) {
                        case 0:
                            z2 = rTFAttribute.set(this.characterAttributes, i2);
                            break;
                        case 1:
                            z2 = rTFAttribute.set(this.paragraphAttributes, i2);
                            break;
                        case 2:
                            z2 = rTFAttribute.set(this.sectionAttributes, i2);
                            break;
                        case 3:
                            z2 = rTFAttribute.set(RTFReader.this.documentAttributes, i2);
                            break;
                        case 4:
                            RTFReader.this.mockery.backing = RTFReader.this.parserState;
                            z2 = rTFAttribute.set(RTFReader.this.mockery, i2);
                            RTFReader.this.mockery.backing = null;
                            break;
                        default:
                            z2 = false;
                            break;
                    }
                    if (z2) {
                        return true;
                    }
                }
                if (str.equals("fs")) {
                    StyleConstants.setFontSize(this.characterAttributes, i2 / 2);
                    return true;
                }
                if (str.equals("sl")) {
                    if (i2 == 1000) {
                        this.characterAttributes.removeAttribute(StyleConstants.LineSpacing);
                        return true;
                    }
                    StyleConstants.setLineSpacing(this.characterAttributes, i2 / 20.0f);
                    return true;
                }
                if (str.equals("tx") || str.equals("tb")) {
                    float f2 = i2 / 20.0f;
                    int iIntValue = 0;
                    Number number = (Number) RTFReader.this.parserState.get(RTFReader.TabAlignmentKey);
                    if (number != null) {
                        iIntValue = number.intValue();
                    }
                    int iIntValue2 = 0;
                    Number number2 = (Number) RTFReader.this.parserState.get(RTFReader.TabLeaderKey);
                    if (number2 != null) {
                        iIntValue2 = number2.intValue();
                    }
                    if (str.equals("tb")) {
                        iIntValue = 5;
                    }
                    RTFReader.this.parserState.remove(RTFReader.TabAlignmentKey);
                    RTFReader.this.parserState.remove(RTFReader.TabLeaderKey);
                    TabStop tabStop = new TabStop(f2, iIntValue, iIntValue2);
                    Dictionary hashtable = (Dictionary) RTFReader.this.parserState.get("_tabs");
                    if (hashtable == null) {
                        hashtable = new Hashtable();
                        RTFReader.this.parserState.put("_tabs", hashtable);
                        numValueOf = 1;
                    } else {
                        numValueOf = Integer.valueOf(1 + ((Integer) hashtable.get("stop count")).intValue());
                    }
                    hashtable.put(numValueOf, tabStop);
                    hashtable.put("stop count", numValueOf);
                    RTFReader.this.parserState.remove("_tabs_immutable");
                    return true;
                }
                if (str.equals(PdfOps.s_TOKEN) && RTFReader.this.paragraphStyles != null) {
                    RTFReader.this.parserState.put("paragraphStyle", RTFReader.this.paragraphStyles.get(Integer.valueOf(i2)));
                    return true;
                }
                if (str.equals(PdfOps.cs_TOKEN) && RTFReader.this.characterStyles != null) {
                    RTFReader.this.parserState.put("characterStyle", RTFReader.this.characterStyles.get(Integer.valueOf(i2)));
                    return true;
                }
                if (str.equals("ds") && RTFReader.this.sectionStyles != null) {
                    RTFReader.this.parserState.put("sectionStyle", RTFReader.this.sectionStyles.get(Integer.valueOf(i2)));
                    return true;
                }
                return false;
            }
            RTFReader.this.parserState.put(str, Integer.valueOf(i2));
            return true;
        }

        protected MutableAttributeSet rootCharacterAttributes() {
            SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
            StyleConstants.setItalic(simpleAttributeSet, false);
            StyleConstants.setBold(simpleAttributeSet, false);
            StyleConstants.setUnderline(simpleAttributeSet, false);
            StyleConstants.setForeground(simpleAttributeSet, RTFReader.this.defaultColor());
            return simpleAttributeSet;
        }

        protected MutableAttributeSet rootParagraphAttributes() {
            SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
            StyleConstants.setLeftIndent(simpleAttributeSet, 0.0f);
            StyleConstants.setRightIndent(simpleAttributeSet, 0.0f);
            StyleConstants.setFirstLineIndent(simpleAttributeSet, 0.0f);
            simpleAttributeSet.setResolveParent(RTFReader.this.target.getStyle("default"));
            return simpleAttributeSet;
        }

        protected MutableAttributeSet rootSectionAttributes() {
            return new SimpleAttributeSet();
        }

        MutableAttributeSet currentTextAttributes() {
            String str;
            SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet(this.characterAttributes);
            Integer num = (Integer) RTFReader.this.parserState.get(PdfOps.f_TOKEN);
            if (num != null) {
                str = RTFReader.this.fontTable.get(num);
            } else {
                str = null;
            }
            if (str != null) {
                StyleConstants.setFontFamily(simpleAttributeSet, str);
            } else {
                simpleAttributeSet.removeAttribute(StyleConstants.FontFamily);
            }
            if (RTFReader.this.colorTable != null) {
                Integer num2 = (Integer) RTFReader.this.parserState.get("cf");
                if (num2 != null) {
                    StyleConstants.setForeground(simpleAttributeSet, RTFReader.this.colorTable[num2.intValue()]);
                } else {
                    simpleAttributeSet.removeAttribute(StyleConstants.Foreground);
                }
            }
            if (RTFReader.this.colorTable != null) {
                Integer num3 = (Integer) RTFReader.this.parserState.get("cb");
                if (num3 != null) {
                    simpleAttributeSet.addAttribute(StyleConstants.Background, RTFReader.this.colorTable[num3.intValue()]);
                } else {
                    simpleAttributeSet.removeAttribute(StyleConstants.Background);
                }
            }
            Style style = (Style) RTFReader.this.parserState.get("characterStyle");
            if (style != null) {
                simpleAttributeSet.setResolveParent(style);
            }
            return simpleAttributeSet;
        }

        MutableAttributeSet currentParagraphAttributes() {
            Dictionary dictionary;
            SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet(this.paragraphAttributes);
            TabStop[] tabStopArr = (TabStop[]) RTFReader.this.parserState.get("_tabs_immutable");
            if (tabStopArr == null && (dictionary = (Dictionary) RTFReader.this.parserState.get("_tabs")) != null) {
                int iIntValue = ((Integer) dictionary.get("stop count")).intValue();
                tabStopArr = new TabStop[iIntValue];
                for (int i2 = 1; i2 <= iIntValue; i2++) {
                    tabStopArr[i2 - 1] = (TabStop) dictionary.get(Integer.valueOf(i2));
                }
                RTFReader.this.parserState.put("_tabs_immutable", tabStopArr);
            }
            if (tabStopArr != null) {
                simpleAttributeSet.addAttribute("tabs", tabStopArr);
            }
            Style style = (Style) RTFReader.this.parserState.get("paragraphStyle");
            if (style != null) {
                simpleAttributeSet.setResolveParent(style);
            }
            return simpleAttributeSet;
        }

        public AttributeSet currentSectionAttributes() {
            SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet(this.sectionAttributes);
            Style style = (Style) RTFReader.this.parserState.get("sectionStyle");
            if (style != null) {
                simpleAttributeSet.setResolveParent(style);
            }
            return simpleAttributeSet;
        }

        protected void resetCharacterAttributes() {
            handleKeyword(PdfOps.f_TOKEN, 0);
            handleKeyword("cf", 0);
            handleKeyword("fs", 24);
            Enumeration enumerationElements = RTFReader.straightforwardAttributes.elements();
            while (enumerationElements.hasMoreElements()) {
                RTFAttribute rTFAttribute = (RTFAttribute) enumerationElements.nextElement2();
                if (rTFAttribute.domain() == 0) {
                    rTFAttribute.setDefault(this.characterAttributes);
                }
            }
            handleKeyword("sl", 1000);
            RTFReader.this.parserState.remove("characterStyle");
        }

        protected void resetParagraphAttributes() {
            RTFReader.this.parserState.remove("_tabs");
            RTFReader.this.parserState.remove("_tabs_immutable");
            RTFReader.this.parserState.remove("paragraphStyle");
            StyleConstants.setAlignment(this.paragraphAttributes, 0);
            Enumeration enumerationElements = RTFReader.straightforwardAttributes.elements();
            while (enumerationElements.hasMoreElements()) {
                RTFAttribute rTFAttribute = (RTFAttribute) enumerationElements.nextElement2();
                if (rTFAttribute.domain() == 1) {
                    rTFAttribute.setDefault(this.characterAttributes);
                }
            }
        }

        protected void resetSectionAttributes() {
            Enumeration enumerationElements = RTFReader.straightforwardAttributes.elements();
            while (enumerationElements.hasMoreElements()) {
                RTFAttribute rTFAttribute = (RTFAttribute) enumerationElements.nextElement2();
                if (rTFAttribute.domain() == 2) {
                    rTFAttribute.setDefault(this.characterAttributes);
                }
            }
            RTFReader.this.parserState.remove("sectionStyle");
        }
    }

    /* loaded from: rt.jar:javax/swing/text/rtf/RTFReader$TextHandlingDestination.class */
    abstract class TextHandlingDestination extends AttributeTrackingDestination implements Destination {
        boolean inParagraph;

        abstract void deliverText(String str, AttributeSet attributeSet);

        abstract void finishParagraph(AttributeSet attributeSet, AttributeSet attributeSet2);

        abstract void endSection();

        public TextHandlingDestination() {
            super();
            this.inParagraph = false;
        }

        @Override // javax.swing.text.rtf.RTFReader.AttributeTrackingDestination, javax.swing.text.rtf.RTFReader.Destination
        public void handleText(String str) {
            if (!this.inParagraph) {
                beginParagraph();
            }
            deliverText(str, currentTextAttributes());
        }

        @Override // javax.swing.text.rtf.RTFReader.AttributeTrackingDestination, javax.swing.text.rtf.RTFReader.Destination
        public void close() {
            if (this.inParagraph) {
                endParagraph();
            }
            super.close();
        }

        @Override // javax.swing.text.rtf.RTFReader.AttributeTrackingDestination, javax.swing.text.rtf.RTFReader.Destination
        public boolean handleKeyword(String str) {
            if (str.equals(LineSeparator.Macintosh) || str.equals("\n")) {
                str = "par";
            }
            if (str.equals("par")) {
                endParagraph();
                return true;
            }
            if (str.equals("sect")) {
                endSection();
                return true;
            }
            return super.handleKeyword(str);
        }

        protected void beginParagraph() {
            this.inParagraph = true;
        }

        protected void endParagraph() {
            finishParagraph(currentParagraphAttributes(), currentTextAttributes());
            this.inParagraph = false;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/rtf/RTFReader$DocumentDestination.class */
    class DocumentDestination extends TextHandlingDestination implements Destination {
        DocumentDestination() {
            super();
        }

        @Override // javax.swing.text.rtf.RTFReader.TextHandlingDestination
        public void deliverText(String str, AttributeSet attributeSet) {
            try {
                RTFReader.this.target.insertString(RTFReader.this.target.getLength(), str, currentTextAttributes());
            } catch (BadLocationException e2) {
                throw new InternalError(e2.getMessage(), e2);
            }
        }

        @Override // javax.swing.text.rtf.RTFReader.TextHandlingDestination
        public void finishParagraph(AttributeSet attributeSet, AttributeSet attributeSet2) {
            int length = RTFReader.this.target.getLength();
            try {
                RTFReader.this.target.insertString(length, "\n", attributeSet2);
                RTFReader.this.target.setParagraphAttributes(length, 1, attributeSet, true);
            } catch (BadLocationException e2) {
                throw new InternalError(e2.getMessage(), e2);
            }
        }

        @Override // javax.swing.text.rtf.RTFReader.TextHandlingDestination
        public void endSection() {
        }
    }
}
