package javax.swing.text.rtf;

import com.sun.org.apache.xml.internal.utils.res.XResourceBundle;
import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.Segment;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.TabStop;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:javax/swing/text/rtf/RTFGenerator.class */
class RTFGenerator {
    Dictionary<Object, Integer> colorTable = new Hashtable();
    int colorCount;
    Dictionary<String, Integer> fontTable;
    int fontCount;
    Dictionary<AttributeSet, Integer> styleTable;
    int styleCount;
    OutputStream outputStream;
    boolean afterKeyword;
    MutableAttributeSet outputAttributes;
    int unicodeCount;
    private Segment workingSegment;
    int[] outputConversion;
    public static final float defaultFontSize = 12.0f;
    public static final String defaultFontFamily = "Helvetica";
    protected static CharacterKeywordPair[] textKeywords;
    static final char[] hexdigits;
    public static final Color defaultRTFColor = Color.black;
    private static final Object MagicToken = new Object();

    static {
        Dictionary<String, String> dictionary = RTFReader.textKeywords;
        Enumeration<String> enumerationKeys = dictionary.keys();
        Vector vector = new Vector();
        while (enumerationKeys.hasMoreElements()) {
            CharacterKeywordPair characterKeywordPair = new CharacterKeywordPair();
            characterKeywordPair.keyword = enumerationKeys.nextElement();
            characterKeywordPair.character = dictionary.get(characterKeywordPair.keyword).charAt(0);
            vector.addElement(characterKeywordPair);
        }
        textKeywords = new CharacterKeywordPair[vector.size()];
        vector.copyInto(textKeywords);
        hexdigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    }

    /* loaded from: rt.jar:javax/swing/text/rtf/RTFGenerator$CharacterKeywordPair.class */
    static class CharacterKeywordPair {
        public char character;
        public String keyword;

        CharacterKeywordPair() {
        }
    }

    public static void writeDocument(Document document, OutputStream outputStream) throws IOException {
        RTFGenerator rTFGenerator = new RTFGenerator(outputStream);
        Element defaultRootElement = document.getDefaultRootElement();
        rTFGenerator.examineElement(defaultRootElement);
        rTFGenerator.writeRTFHeader();
        rTFGenerator.writeDocumentProperties(document);
        int elementCount = defaultRootElement.getElementCount();
        for (int i2 = 0; i2 < elementCount; i2++) {
            rTFGenerator.writeParagraphElement(defaultRootElement.getElement(i2));
        }
        rTFGenerator.writeRTFTrailer();
    }

    public RTFGenerator(OutputStream outputStream) {
        this.colorTable.put(defaultRTFColor, 0);
        this.colorCount = 1;
        this.fontTable = new Hashtable();
        this.fontCount = 0;
        this.styleTable = new Hashtable();
        this.styleCount = 0;
        this.workingSegment = new Segment();
        this.outputStream = outputStream;
        this.unicodeCount = 1;
    }

    public void examineElement(Element element) {
        AttributeSet attributes = element.getAttributes();
        tallyStyles(attributes);
        if (attributes != null) {
            Color foreground = StyleConstants.getForeground(attributes);
            if (foreground != null && this.colorTable.get(foreground) == null) {
                this.colorTable.put(foreground, new Integer(this.colorCount));
                this.colorCount++;
            }
            Object attribute = attributes.getAttribute(StyleConstants.Background);
            if (attribute != null && this.colorTable.get(attribute) == null) {
                this.colorTable.put(attribute, new Integer(this.colorCount));
                this.colorCount++;
            }
            String fontFamily = StyleConstants.getFontFamily(attributes);
            if (fontFamily == null) {
                fontFamily = defaultFontFamily;
            }
            if (fontFamily != null && this.fontTable.get(fontFamily) == null) {
                this.fontTable.put(fontFamily, new Integer(this.fontCount));
                this.fontCount++;
            }
        }
        int elementCount = element.getElementCount();
        for (int i2 = 0; i2 < elementCount; i2++) {
            examineElement(element.getElement(i2));
        }
    }

    private void tallyStyles(AttributeSet attributeSet) {
        while (attributeSet != null) {
            if ((attributeSet instanceof Style) && this.styleTable.get(attributeSet) == null) {
                this.styleCount++;
                this.styleTable.put(attributeSet, new Integer(this.styleCount));
            }
            attributeSet = attributeSet.getResolveParent();
        }
    }

    private Style findStyle(AttributeSet attributeSet) {
        while (attributeSet != null) {
            if ((attributeSet instanceof Style) && this.styleTable.get(attributeSet) != null) {
                return (Style) attributeSet;
            }
            attributeSet = attributeSet.getResolveParent();
        }
        return null;
    }

    private Integer findStyleNumber(AttributeSet attributeSet, String str) {
        Integer num;
        while (attributeSet != null) {
            if ((attributeSet instanceof Style) && (num = this.styleTable.get(attributeSet)) != null && (str == null || str.equals(attributeSet.getAttribute("style:type")))) {
                return num;
            }
            attributeSet = attributeSet.getResolveParent();
        }
        return null;
    }

    private static Object attrDiff(MutableAttributeSet mutableAttributeSet, AttributeSet attributeSet, Object obj, Object obj2) {
        Object attribute = mutableAttributeSet.getAttribute(obj);
        Object attribute2 = attributeSet.getAttribute(obj);
        if (attribute2 == attribute) {
            return null;
        }
        if (attribute2 == null) {
            mutableAttributeSet.removeAttribute(obj);
            if (obj2 != null && !obj2.equals(attribute)) {
                return obj2;
            }
            return null;
        }
        if (attribute == null || !equalArraysOK(attribute, attribute2)) {
            mutableAttributeSet.addAttribute(obj, attribute2);
            return attribute2;
        }
        return null;
    }

    private static boolean equalArraysOK(Object obj, Object obj2) {
        if (obj == obj2) {
            return true;
        }
        if (obj == null || obj2 == null) {
            return false;
        }
        if (obj.equals(obj2)) {
            return true;
        }
        if (!obj.getClass().isArray() || !obj2.getClass().isArray()) {
            return false;
        }
        Object[] objArr = (Object[]) obj;
        Object[] objArr2 = (Object[]) obj2;
        if (objArr.length != objArr2.length) {
            return false;
        }
        int length = objArr.length;
        for (int i2 = 0; i2 < length; i2++) {
            if (!equalArraysOK(objArr[i2], objArr2[i2])) {
                return false;
            }
        }
        return true;
    }

    public void writeLineBreak() throws IOException {
        writeRawString("\n");
        this.afterKeyword = false;
    }

    public void writeRTFHeader() throws IOException {
        MutableAttributeSet simpleAttributeSet;
        Integer num;
        Integer num2;
        writeBegingroup();
        writeControlWord("rtf", 1);
        writeControlWord("ansi");
        this.outputConversion = outputConversionForName("ansi");
        writeLineBreak();
        String[] strArr = new String[this.fontCount];
        Enumeration<String> enumerationKeys = this.fontTable.keys();
        while (enumerationKeys.hasMoreElements()) {
            String strNextElement = enumerationKeys.nextElement();
            strArr[this.fontTable.get(strNextElement).intValue()] = strNextElement;
        }
        writeBegingroup();
        writeControlWord("fonttbl");
        for (int i2 = 0; i2 < this.fontCount; i2++) {
            writeControlWord(PdfOps.f_TOKEN, i2);
            writeControlWord("fnil");
            writeText(strArr[i2]);
            writeText(";");
        }
        writeEndgroup();
        writeLineBreak();
        if (this.colorCount > 1) {
            Color[] colorArr = new Color[this.colorCount];
            Enumeration<Object> enumerationKeys2 = this.colorTable.keys();
            while (enumerationKeys2.hasMoreElements()) {
                Color color = (Color) enumerationKeys2.nextElement();
                colorArr[this.colorTable.get(color).intValue()] = color;
            }
            writeBegingroup();
            writeControlWord("colortbl");
            for (int i3 = 0; i3 < this.colorCount; i3++) {
                Color color2 = colorArr[i3];
                if (color2 != null) {
                    writeControlWord("red", color2.getRed());
                    writeControlWord("green", color2.getGreen());
                    writeControlWord("blue", color2.getBlue());
                }
                writeRawString(";");
            }
            writeEndgroup();
            writeLineBreak();
        }
        if (this.styleCount > 1) {
            writeBegingroup();
            writeControlWord(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_STYLESHEET_STRING);
            Enumeration<AttributeSet> enumerationKeys3 = this.styleTable.keys();
            while (enumerationKeys3.hasMoreElements()) {
                Style style = (Style) enumerationKeys3.nextElement();
                int iIntValue = this.styleTable.get(style).intValue();
                writeBegingroup();
                String str = (String) style.getAttribute("style:type");
                if (str == null) {
                    str = AbstractDocument.ParagraphElementName;
                }
                if (str.equals("character")) {
                    writeControlWord("*");
                    writeControlWord(PdfOps.cs_TOKEN, iIntValue);
                } else if (str.equals(AbstractDocument.SectionElementName)) {
                    writeControlWord("*");
                    writeControlWord("ds", iIntValue);
                } else {
                    writeControlWord(PdfOps.s_TOKEN, iIntValue);
                }
                AttributeSet resolveParent = style.getResolveParent();
                if (resolveParent == null) {
                    simpleAttributeSet = new SimpleAttributeSet();
                } else {
                    simpleAttributeSet = new SimpleAttributeSet(resolveParent);
                }
                updateSectionAttributes(simpleAttributeSet, style, false);
                updateParagraphAttributes(simpleAttributeSet, style, false);
                updateCharacterAttributes(simpleAttributeSet, style, false);
                AttributeSet resolveParent2 = style.getResolveParent();
                if (resolveParent2 != null && (resolveParent2 instanceof Style) && (num2 = this.styleTable.get(resolveParent2)) != null) {
                    writeControlWord("sbasedon", num2.intValue());
                }
                Style style2 = (Style) style.getAttribute("style:nextStyle");
                if (style2 != null && (num = this.styleTable.get(style2)) != null) {
                    writeControlWord("snext", num.intValue());
                }
                Boolean bool = (Boolean) style.getAttribute("style:hidden");
                if (bool != null && bool.booleanValue()) {
                    writeControlWord("shidden");
                }
                Boolean bool2 = (Boolean) style.getAttribute("style:additive");
                if (bool2 != null && bool2.booleanValue()) {
                    writeControlWord(XResourceBundle.LANG_ADDITIVE);
                }
                writeText(style.getName());
                writeText(";");
                writeEndgroup();
            }
            writeEndgroup();
            writeLineBreak();
        }
        this.outputAttributes = new SimpleAttributeSet();
    }

    void writeDocumentProperties(Document document) throws IOException {
        boolean z2 = false;
        for (int i2 = 0; i2 < RTFAttributes.attributes.length; i2++) {
            RTFAttribute rTFAttribute = RTFAttributes.attributes[i2];
            if (rTFAttribute.domain() == 3 && rTFAttribute.writeValue(document.getProperty(rTFAttribute.swingName()), this, false)) {
                z2 = true;
            }
        }
        if (z2) {
            writeLineBreak();
        }
    }

    public void writeRTFTrailer() throws IOException {
        writeEndgroup();
        writeLineBreak();
    }

    protected void checkNumericControlWord(MutableAttributeSet mutableAttributeSet, AttributeSet attributeSet, Object obj, String str, float f2, float f3) throws IOException {
        float fFloatValue;
        Object objAttrDiff = attrDiff(mutableAttributeSet, attributeSet, obj, MagicToken);
        if (objAttrDiff != null) {
            if (objAttrDiff == MagicToken) {
                fFloatValue = f2;
            } else {
                fFloatValue = ((Number) objAttrDiff).floatValue();
            }
            writeControlWord(str, Math.round(fFloatValue * f3));
        }
    }

    protected void checkControlWord(MutableAttributeSet mutableAttributeSet, AttributeSet attributeSet, RTFAttribute rTFAttribute) throws IOException {
        Object objAttrDiff = attrDiff(mutableAttributeSet, attributeSet, rTFAttribute.swingName(), MagicToken);
        Object obj = objAttrDiff;
        if (objAttrDiff != null) {
            if (obj == MagicToken) {
                obj = null;
            }
            rTFAttribute.writeValue(obj, this, true);
        }
    }

    protected void checkControlWords(MutableAttributeSet mutableAttributeSet, AttributeSet attributeSet, RTFAttribute[] rTFAttributeArr, int i2) throws IOException {
        for (RTFAttribute rTFAttribute : rTFAttributeArr) {
            if (rTFAttribute.domain() == i2) {
                checkControlWord(mutableAttributeSet, attributeSet, rTFAttribute);
            }
        }
    }

    void updateSectionAttributes(MutableAttributeSet mutableAttributeSet, AttributeSet attributeSet, boolean z2) throws IOException {
        Object attribute;
        Integer numFindStyleNumber;
        if (z2 && (attribute = mutableAttributeSet.getAttribute("sectionStyle")) != (numFindStyleNumber = findStyleNumber(attributeSet, AbstractDocument.SectionElementName))) {
            if (attribute != null) {
                resetSectionAttributes(mutableAttributeSet);
            }
            if (numFindStyleNumber != null) {
                writeControlWord("ds", numFindStyleNumber.intValue());
                mutableAttributeSet.addAttribute("sectionStyle", numFindStyleNumber);
            } else {
                mutableAttributeSet.removeAttribute("sectionStyle");
            }
        }
        checkControlWords(mutableAttributeSet, attributeSet, RTFAttributes.attributes, 2);
    }

    protected void resetSectionAttributes(MutableAttributeSet mutableAttributeSet) throws IOException {
        writeControlWord("sectd");
        int length = RTFAttributes.attributes.length;
        for (int i2 = 0; i2 < length; i2++) {
            RTFAttribute rTFAttribute = RTFAttributes.attributes[i2];
            if (rTFAttribute.domain() == 2) {
                rTFAttribute.setDefault(mutableAttributeSet);
            }
        }
        mutableAttributeSet.removeAttribute("sectionStyle");
    }

    void updateParagraphAttributes(MutableAttributeSet mutableAttributeSet, AttributeSet attributeSet, boolean z2) throws IOException {
        Object attribute;
        Object objFindStyleNumber;
        if (z2) {
            attribute = mutableAttributeSet.getAttribute("paragraphStyle");
            objFindStyleNumber = findStyleNumber(attributeSet, AbstractDocument.ParagraphElementName);
            if (attribute != objFindStyleNumber && attribute != null) {
                resetParagraphAttributes(mutableAttributeSet);
                attribute = null;
            }
        } else {
            attribute = null;
            objFindStyleNumber = null;
        }
        Object attribute2 = mutableAttributeSet.getAttribute("tabs");
        Object attribute3 = attributeSet.getAttribute("tabs");
        if (attribute2 != attribute3 && attribute2 != null) {
            resetParagraphAttributes(mutableAttributeSet);
            attribute2 = null;
            attribute = null;
        }
        if (attribute != objFindStyleNumber && objFindStyleNumber != null) {
            writeControlWord(PdfOps.s_TOKEN, ((Integer) objFindStyleNumber).intValue());
            mutableAttributeSet.addAttribute("paragraphStyle", objFindStyleNumber);
        }
        checkControlWords(mutableAttributeSet, attributeSet, RTFAttributes.attributes, 1);
        if (attribute2 != attribute3 && attribute3 != null) {
            TabStop[] tabStopArr = (TabStop[]) attribute3;
            for (TabStop tabStop : tabStopArr) {
                switch (tabStop.getAlignment()) {
                    case 1:
                        writeControlWord("tqr");
                        break;
                    case 2:
                        writeControlWord("tqc");
                        break;
                    case 4:
                        writeControlWord("tqdec");
                        break;
                }
                switch (tabStop.getLeader()) {
                    case 1:
                        writeControlWord("tldot");
                        break;
                    case 2:
                        writeControlWord("tlhyph");
                        break;
                    case 3:
                        writeControlWord("tlul");
                        break;
                    case 4:
                        writeControlWord("tlth");
                        break;
                    case 5:
                        writeControlWord("tleq");
                        break;
                }
                int iRound = Math.round(20.0f * tabStop.getPosition());
                if (tabStop.getAlignment() == 5) {
                    writeControlWord("tb", iRound);
                } else {
                    writeControlWord("tx", iRound);
                }
            }
            mutableAttributeSet.addAttribute("tabs", tabStopArr);
        }
    }

    public void writeParagraphElement(Element element) throws IOException {
        updateParagraphAttributes(this.outputAttributes, element.getAttributes(), true);
        int elementCount = element.getElementCount();
        for (int i2 = 0; i2 < elementCount; i2++) {
            writeTextElement(element.getElement(i2));
        }
        writeControlWord("par");
        writeLineBreak();
    }

    protected void resetParagraphAttributes(MutableAttributeSet mutableAttributeSet) throws IOException {
        writeControlWord("pard");
        mutableAttributeSet.addAttribute(StyleConstants.Alignment, 0);
        int length = RTFAttributes.attributes.length;
        for (int i2 = 0; i2 < length; i2++) {
            RTFAttribute rTFAttribute = RTFAttributes.attributes[i2];
            if (rTFAttribute.domain() == 1) {
                rTFAttribute.setDefault(mutableAttributeSet);
            }
        }
        mutableAttributeSet.removeAttribute("paragraphStyle");
        mutableAttributeSet.removeAttribute("tabs");
    }

    void updateCharacterAttributes(MutableAttributeSet mutableAttributeSet, AttributeSet attributeSet, boolean z2) throws IOException {
        int iIntValue;
        int iIntValue2;
        Object attribute;
        Integer numFindStyleNumber;
        if (z2 && (attribute = mutableAttributeSet.getAttribute("characterStyle")) != (numFindStyleNumber = findStyleNumber(attributeSet, "character"))) {
            if (attribute != null) {
                resetCharacterAttributes(mutableAttributeSet);
            }
            if (numFindStyleNumber != null) {
                writeControlWord(PdfOps.cs_TOKEN, numFindStyleNumber.intValue());
                mutableAttributeSet.addAttribute("characterStyle", numFindStyleNumber);
            } else {
                mutableAttributeSet.removeAttribute("characterStyle");
            }
        }
        Object objAttrDiff = attrDiff(mutableAttributeSet, attributeSet, StyleConstants.FontFamily, null);
        if (objAttrDiff != null) {
            writeControlWord(PdfOps.f_TOKEN, this.fontTable.get(objAttrDiff).intValue());
        }
        checkNumericControlWord(mutableAttributeSet, attributeSet, StyleConstants.FontSize, "fs", 12.0f, 2.0f);
        checkControlWords(mutableAttributeSet, attributeSet, RTFAttributes.attributes, 0);
        checkNumericControlWord(mutableAttributeSet, attributeSet, StyleConstants.LineSpacing, "sl", 0.0f, 20.0f);
        Object objAttrDiff2 = attrDiff(mutableAttributeSet, attributeSet, StyleConstants.Background, MagicToken);
        if (objAttrDiff2 != null) {
            if (objAttrDiff2 == MagicToken) {
                iIntValue2 = 0;
            } else {
                iIntValue2 = this.colorTable.get(objAttrDiff2).intValue();
            }
            writeControlWord("cb", iIntValue2);
        }
        Object objAttrDiff3 = attrDiff(mutableAttributeSet, attributeSet, StyleConstants.Foreground, null);
        if (objAttrDiff3 != null) {
            if (objAttrDiff3 == MagicToken) {
                iIntValue = 0;
            } else {
                iIntValue = this.colorTable.get(objAttrDiff3).intValue();
            }
            writeControlWord("cf", iIntValue);
        }
    }

    protected void resetCharacterAttributes(MutableAttributeSet mutableAttributeSet) throws IOException {
        writeControlWord("plain");
        int length = RTFAttributes.attributes.length;
        for (int i2 = 0; i2 < length; i2++) {
            RTFAttribute rTFAttribute = RTFAttributes.attributes[i2];
            if (rTFAttribute.domain() == 0) {
                rTFAttribute.setDefault(mutableAttributeSet);
            }
        }
        StyleConstants.setFontFamily(mutableAttributeSet, defaultFontFamily);
        mutableAttributeSet.removeAttribute(StyleConstants.FontSize);
        mutableAttributeSet.removeAttribute(StyleConstants.Background);
        mutableAttributeSet.removeAttribute(StyleConstants.Foreground);
        mutableAttributeSet.removeAttribute(StyleConstants.LineSpacing);
        mutableAttributeSet.removeAttribute("characterStyle");
    }

    public void writeTextElement(Element element) throws IOException {
        updateCharacterAttributes(this.outputAttributes, element.getAttributes(), true);
        if (element.isLeaf()) {
            try {
                element.getDocument().getText(element.getStartOffset(), element.getEndOffset() - element.getStartOffset(), this.workingSegment);
                writeText(this.workingSegment);
                return;
            } catch (BadLocationException e2) {
                e2.printStackTrace();
                throw new InternalError(e2.getMessage());
            }
        }
        int elementCount = element.getElementCount();
        for (int i2 = 0; i2 < elementCount; i2++) {
            writeTextElement(element.getElement(i2));
        }
    }

    public void writeText(Segment segment) throws IOException {
        int i2 = segment.offset;
        int i3 = i2 + segment.count;
        char[] cArr = segment.array;
        while (i2 < i3) {
            writeCharacter(cArr[i2]);
            i2++;
        }
    }

    public void writeText(String str) throws IOException {
        int length = str.length();
        for (int i2 = 0; i2 < length; i2++) {
            writeCharacter(str.charAt(i2));
        }
    }

    public void writeRawString(String str) throws IOException {
        int length = str.length();
        for (int i2 = 0; i2 < length; i2++) {
            this.outputStream.write(str.charAt(i2));
        }
    }

    public void writeControlWord(String str) throws IOException {
        this.outputStream.write(92);
        writeRawString(str);
        this.afterKeyword = true;
    }

    public void writeControlWord(String str, int i2) throws IOException {
        this.outputStream.write(92);
        writeRawString(str);
        writeRawString(String.valueOf(i2));
        this.afterKeyword = true;
    }

    public void writeBegingroup() throws IOException {
        this.outputStream.write(123);
        this.afterKeyword = false;
    }

    public void writeEndgroup() throws IOException {
        this.outputStream.write(125);
        this.afterKeyword = false;
    }

    public void writeCharacter(char c2) throws IOException {
        if (c2 == 160) {
            this.outputStream.write(92);
            this.outputStream.write(126);
            this.afterKeyword = false;
            return;
        }
        if (c2 == '\t') {
            writeControlWord("tab");
            return;
        }
        if (c2 == '\n' || c2 == '\r') {
            return;
        }
        int iConvertCharacter = convertCharacter(this.outputConversion, c2);
        if (iConvertCharacter == 0) {
            for (int i2 = 0; i2 < textKeywords.length; i2++) {
                if (textKeywords[i2].character == c2) {
                    writeControlWord(textKeywords[i2].keyword);
                    return;
                }
            }
            String strApproximationForUnicode = approximationForUnicode(c2);
            if (strApproximationForUnicode.length() != this.unicodeCount) {
                this.unicodeCount = strApproximationForUnicode.length();
                writeControlWord("uc", this.unicodeCount);
            }
            writeControlWord("u", c2);
            writeRawString(" ");
            writeRawString(strApproximationForUnicode);
            this.afterKeyword = false;
            return;
        }
        if (iConvertCharacter > 127) {
            this.outputStream.write(92);
            this.outputStream.write(39);
            this.outputStream.write(hexdigits[(iConvertCharacter & 240) >>> 4]);
            this.outputStream.write(hexdigits[iConvertCharacter & 15]);
            this.afterKeyword = false;
            return;
        }
        switch (iConvertCharacter) {
            case 92:
            case 123:
            case 125:
                this.outputStream.write(92);
                this.afterKeyword = false;
                break;
        }
        if (this.afterKeyword) {
            this.outputStream.write(32);
            this.afterKeyword = false;
        }
        this.outputStream.write(iConvertCharacter);
    }

    String approximationForUnicode(char c2) {
        return "?";
    }

    static int[] outputConversionFromTranslationTable(char[] cArr) {
        int[] iArr = new int[2 * cArr.length];
        for (int i2 = 0; i2 < cArr.length; i2++) {
            iArr[i2 * 2] = cArr[i2];
            iArr[(i2 * 2) + 1] = i2;
        }
        return iArr;
    }

    static int[] outputConversionForName(String str) throws IOException {
        return outputConversionFromTranslationTable((char[]) RTFReader.getCharacterSet(str));
    }

    protected static int convertCharacter(int[] iArr, char c2) {
        for (int i2 = 0; i2 < iArr.length; i2 += 2) {
            if (iArr[i2] == c2) {
                return iArr[i2 + 1];
            }
        }
        return 0;
    }
}
