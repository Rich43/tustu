package javax.swing.text.html;

import com.sun.org.apache.xml.internal.serializer.SerializerConstants;
import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Stack;
import java.util.Vector;
import javax.swing.text.AbstractWriter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.ElementIterator;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.Segment;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.CSS;
import javax.swing.text.html.HTML;
import org.apache.commons.math3.geometry.VectorFormat;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:javax/swing/text/html/HTMLWriter.class */
public class HTMLWriter extends AbstractWriter {
    private Stack<Element> blockElementStack;
    private boolean inContent;
    private boolean inPre;
    private int preEndOffset;
    private boolean inTextArea;
    private boolean newlineOutputed;
    private boolean completeDoc;
    private Vector<HTML.Tag> tags;
    private Vector<Object> tagValues;
    private Segment segment;
    private Vector<HTML.Tag> tagsToRemove;
    private boolean wroteHead;
    private boolean replaceEntities;
    private char[] tempChars;
    private boolean indentNext;
    private boolean writeCSS;
    private MutableAttributeSet convAttr;
    private MutableAttributeSet oConvAttr;
    private boolean indented;

    public HTMLWriter(Writer writer, HTMLDocument hTMLDocument) {
        this(writer, hTMLDocument, 0, hTMLDocument.getLength());
    }

    public HTMLWriter(Writer writer, HTMLDocument hTMLDocument, int i2, int i3) {
        super(writer, hTMLDocument, i2, i3);
        this.blockElementStack = new Stack<>();
        this.inContent = false;
        this.inPre = false;
        this.inTextArea = false;
        this.newlineOutputed = false;
        this.tags = new Vector<>(10);
        this.tagValues = new Vector<>(10);
        this.tagsToRemove = new Vector<>(10);
        this.indentNext = false;
        this.writeCSS = false;
        this.convAttr = new SimpleAttributeSet();
        this.oConvAttr = new SimpleAttributeSet();
        this.indented = false;
        this.completeDoc = i2 == 0 && i3 == hTMLDocument.getLength();
        setLineLength(80);
    }

    @Override // javax.swing.text.AbstractWriter
    public void write() throws IOException, BadLocationException {
        Element elementPeek;
        ElementIterator elementIterator = getElementIterator();
        Element element = null;
        this.wroteHead = false;
        setCurrentLineLength(0);
        this.replaceEntities = false;
        setCanWrapLines(false);
        if (this.segment == null) {
            this.segment = new Segment();
        }
        this.inPre = false;
        boolean z2 = false;
        while (true) {
            Element next = elementIterator.next();
            if (next == null) {
                break;
            }
            if (!inRange(next)) {
                if (this.completeDoc && next.getAttributes().getAttribute(StyleConstants.NameAttribute) == HTML.Tag.BODY) {
                    z2 = true;
                }
            }
            if (element != null) {
                if (indentNeedsIncrementing(element, next)) {
                    incrIndent();
                } else if (element.getParentElement() != next.getParentElement()) {
                    Element elementPeek2 = this.blockElementStack.peek();
                    while (true) {
                        Element element2 = elementPeek2;
                        if (element2 == next.getParentElement()) {
                            break;
                        }
                        this.blockElementStack.pop();
                        if (!synthesizedElement(element2)) {
                            AttributeSet attributes = element2.getAttributes();
                            if (!matchNameAttribute(attributes, HTML.Tag.PRE) && !isFormElementWithContent(attributes)) {
                                decrIndent();
                            }
                            endTag(element2);
                        }
                        elementPeek2 = this.blockElementStack.peek();
                    }
                } else if (element.getParentElement() == next.getParentElement() && (elementPeek = this.blockElementStack.peek()) == element) {
                    this.blockElementStack.pop();
                    endTag(elementPeek);
                }
            }
            if (!next.isLeaf() || isFormElementWithContent(next.getAttributes())) {
                this.blockElementStack.push(next);
                startTag(next);
            } else {
                emptyTag(next);
            }
            element = next;
        }
        closeOutUnwantedEmbeddedTags(null);
        if (z2) {
            this.blockElementStack.pop();
            endTag(element);
        }
        while (!this.blockElementStack.empty()) {
            Element elementPop = this.blockElementStack.pop();
            if (!synthesizedElement(elementPop)) {
                AttributeSet attributes2 = elementPop.getAttributes();
                if (!matchNameAttribute(attributes2, HTML.Tag.PRE) && !isFormElementWithContent(attributes2)) {
                    decrIndent();
                }
                endTag(elementPop);
            }
        }
        if (this.completeDoc) {
            writeAdditionalComments();
        }
        this.segment.array = null;
    }

    @Override // javax.swing.text.AbstractWriter
    protected void writeAttributes(AttributeSet attributeSet) throws IOException {
        this.convAttr.removeAttributes(this.convAttr);
        convertToHTML32(attributeSet, this.convAttr);
        Enumeration<?> attributeNames = this.convAttr.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            Object objNextElement2 = attributeNames.nextElement2();
            if (!(objNextElement2 instanceof HTML.Tag) && !(objNextElement2 instanceof StyleConstants) && objNextElement2 != HTML.Attribute.ENDTAG) {
                write(" " + objNextElement2 + "=\"" + this.convAttr.getAttribute(objNextElement2) + PdfOps.DOUBLE_QUOTE__TOKEN);
            }
        }
    }

    protected void emptyTag(Element element) throws IOException, BadLocationException {
        if (!this.inContent && !this.inPre) {
            indentSmart();
        }
        AttributeSet attributes = element.getAttributes();
        closeOutUnwantedEmbeddedTags(attributes);
        writeEmbeddedTags(attributes);
        if (matchNameAttribute(attributes, HTML.Tag.CONTENT)) {
            this.inContent = true;
            text(element);
            return;
        }
        if (matchNameAttribute(attributes, HTML.Tag.COMMENT)) {
            comment(element);
            return;
        }
        boolean zIsBlockTag = isBlockTag(element.getAttributes());
        if (this.inContent && zIsBlockTag) {
            writeLineSeparator();
            indentSmart();
        }
        Object attribute = attributes != null ? attributes.getAttribute(StyleConstants.NameAttribute) : null;
        Object attribute2 = attributes != null ? attributes.getAttribute(HTML.Attribute.ENDTAG) : null;
        boolean z2 = false;
        if (attribute != null && attribute2 != null && (attribute2 instanceof String) && attribute2.equals("true")) {
            z2 = true;
        }
        if (this.completeDoc && matchNameAttribute(attributes, HTML.Tag.HEAD)) {
            if (z2) {
                writeStyles(((HTMLDocument) getDocument()).getStyleSheet());
            }
            this.wroteHead = true;
        }
        write('<');
        if (z2) {
            write('/');
        }
        write(element.getName());
        writeAttributes(attributes);
        write('>');
        if (matchNameAttribute(attributes, HTML.Tag.TITLE) && !z2) {
            write((String) element.getDocument().getProperty("title"));
            return;
        }
        if (!this.inContent || zIsBlockTag) {
            writeLineSeparator();
            if (zIsBlockTag && this.inContent) {
                indentSmart();
            }
        }
    }

    protected boolean isBlockTag(AttributeSet attributeSet) {
        Object attribute = attributeSet.getAttribute(StyleConstants.NameAttribute);
        if (attribute instanceof HTML.Tag) {
            return ((HTML.Tag) attribute).isBlock();
        }
        return false;
    }

    protected void startTag(Element element) throws IOException, BadLocationException {
        HTML.Tag tag;
        if (synthesizedElement(element)) {
            return;
        }
        AttributeSet attributes = element.getAttributes();
        Object attribute = attributes.getAttribute(StyleConstants.NameAttribute);
        if (attribute instanceof HTML.Tag) {
            tag = (HTML.Tag) attribute;
        } else {
            tag = null;
        }
        if (tag == HTML.Tag.PRE) {
            this.inPre = true;
            this.preEndOffset = element.getEndOffset();
        }
        closeOutUnwantedEmbeddedTags(attributes);
        if (this.inContent) {
            writeLineSeparator();
            this.inContent = false;
            this.newlineOutputed = false;
        }
        if (this.completeDoc && tag == HTML.Tag.BODY && !this.wroteHead) {
            this.wroteHead = true;
            indentSmart();
            write("<head>");
            writeLineSeparator();
            incrIndent();
            writeStyles(((HTMLDocument) getDocument()).getStyleSheet());
            decrIndent();
            writeLineSeparator();
            indentSmart();
            write("</head>");
            writeLineSeparator();
        }
        indentSmart();
        write('<');
        write(element.getName());
        writeAttributes(attributes);
        write('>');
        if (tag != HTML.Tag.PRE) {
            writeLineSeparator();
        }
        if (tag == HTML.Tag.TEXTAREA) {
            textAreaContent(element.getAttributes());
            return;
        }
        if (tag == HTML.Tag.SELECT) {
            selectContent(element.getAttributes());
            return;
        }
        if (this.completeDoc && tag == HTML.Tag.BODY) {
            writeMaps(((HTMLDocument) getDocument()).getMaps());
            return;
        }
        if (tag == HTML.Tag.HEAD) {
            HTMLDocument hTMLDocument = (HTMLDocument) getDocument();
            this.wroteHead = true;
            incrIndent();
            writeStyles(hTMLDocument.getStyleSheet());
            if (hTMLDocument.hasBaseTag()) {
                indentSmart();
                write("<base href=\"" + ((Object) hTMLDocument.getBase()) + "\">");
                writeLineSeparator();
            }
            decrIndent();
        }
    }

    protected void textAreaContent(AttributeSet attributeSet) throws IOException, BadLocationException {
        Document document = (Document) attributeSet.getAttribute(StyleConstants.ModelAttribute);
        if (document != null && document.getLength() > 0) {
            if (this.segment == null) {
                this.segment = new Segment();
            }
            document.getText(0, document.getLength(), this.segment);
            if (this.segment.count > 0) {
                this.inTextArea = true;
                incrIndent();
                indentSmart();
                setCanWrapLines(true);
                this.replaceEntities = true;
                write(this.segment.array, this.segment.offset, this.segment.count);
                this.replaceEntities = false;
                setCanWrapLines(false);
                writeLineSeparator();
                this.inTextArea = false;
                decrIndent();
            }
        }
    }

    @Override // javax.swing.text.AbstractWriter
    protected void text(Element element) throws IOException, BadLocationException {
        int iMax = Math.max(getStartOffset(), element.getStartOffset());
        int iMin = Math.min(getEndOffset(), element.getEndOffset());
        if (iMax < iMin) {
            if (this.segment == null) {
                this.segment = new Segment();
            }
            getDocument().getText(iMax, iMin - iMax, this.segment);
            this.newlineOutputed = false;
            if (this.segment.count > 0) {
                if (this.segment.array[(this.segment.offset + this.segment.count) - 1] == '\n') {
                    this.newlineOutputed = true;
                }
                if (this.inPre && iMin == this.preEndOffset) {
                    if (this.segment.count > 1) {
                        this.segment.count--;
                    } else {
                        return;
                    }
                }
                this.replaceEntities = true;
                setCanWrapLines(!this.inPre);
                write(this.segment.array, this.segment.offset, this.segment.count);
                setCanWrapLines(false);
                this.replaceEntities = false;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void selectContent(AttributeSet attributeSet) throws IOException {
        Object attribute = attributeSet.getAttribute(StyleConstants.ModelAttribute);
        incrIndent();
        if (attribute instanceof OptionListModel) {
            OptionListModel optionListModel = (OptionListModel) attribute;
            int size = optionListModel.getSize();
            for (int i2 = 0; i2 < size; i2++) {
                writeOption((Option) optionListModel.getElementAt(i2));
            }
        } else if (attribute instanceof OptionComboBoxModel) {
            OptionComboBoxModel optionComboBoxModel = (OptionComboBoxModel) attribute;
            int size2 = optionComboBoxModel.getSize();
            for (int i3 = 0; i3 < size2; i3++) {
                writeOption((Option) optionComboBoxModel.getElementAt(i3));
            }
        }
        decrIndent();
    }

    protected void writeOption(Option option) throws IOException {
        indentSmart();
        write('<');
        write("option");
        Object attribute = option.getAttributes().getAttribute(HTML.Attribute.VALUE);
        if (attribute != null) {
            write(" value=" + attribute);
        }
        if (option.isSelected()) {
            write(" selected");
        }
        write('>');
        if (option.getLabel() != null) {
            write(option.getLabel());
        }
        writeLineSeparator();
    }

    protected void endTag(Element element) throws IOException {
        if (synthesizedElement(element)) {
            return;
        }
        closeOutUnwantedEmbeddedTags(element.getAttributes());
        if (this.inContent) {
            if (!this.newlineOutputed && !this.inPre) {
                writeLineSeparator();
            }
            this.newlineOutputed = false;
            this.inContent = false;
        }
        if (!this.inPre) {
            indentSmart();
        }
        if (matchNameAttribute(element.getAttributes(), HTML.Tag.PRE)) {
            this.inPre = false;
        }
        write('<');
        write('/');
        write(element.getName());
        write('>');
        writeLineSeparator();
    }

    protected void comment(Element element) throws IOException, BadLocationException {
        AttributeSet attributes = element.getAttributes();
        if (matchNameAttribute(attributes, HTML.Tag.COMMENT)) {
            Object attribute = attributes.getAttribute(HTML.Attribute.COMMENT);
            if (attribute instanceof String) {
                writeComment((String) attribute);
            } else {
                writeComment(null);
            }
        }
    }

    void writeComment(String str) throws IOException {
        write("<!--");
        if (str != null) {
            write(str);
        }
        write("-->");
        writeLineSeparator();
        indentSmart();
    }

    void writeAdditionalComments() throws IOException {
        Object property = getDocument().getProperty(HTMLDocument.AdditionalComments);
        if (property instanceof Vector) {
            Vector vector = (Vector) property;
            int size = vector.size();
            for (int i2 = 0; i2 < size; i2++) {
                writeComment(vector.elementAt(i2).toString());
            }
        }
    }

    protected boolean synthesizedElement(Element element) {
        if (matchNameAttribute(element.getAttributes(), HTML.Tag.IMPLIED)) {
            return true;
        }
        return false;
    }

    protected boolean matchNameAttribute(AttributeSet attributeSet, HTML.Tag tag) {
        Object attribute = attributeSet.getAttribute(StyleConstants.NameAttribute);
        if ((attribute instanceof HTML.Tag) && ((HTML.Tag) attribute) == tag) {
            return true;
        }
        return false;
    }

    protected void writeEmbeddedTags(AttributeSet attributeSet) throws IOException {
        HTML.Tag tag;
        AttributeSet attributeSetConvertToHTML = convertToHTML(attributeSet, this.oConvAttr);
        Enumeration<?> attributeNames = attributeSetConvertToHTML.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            Object objNextElement2 = attributeNames.nextElement2();
            if ((objNextElement2 instanceof HTML.Tag) && (tag = (HTML.Tag) objNextElement2) != HTML.Tag.FORM && !this.tags.contains(tag)) {
                write('<');
                write(tag.toString());
                Object attribute = attributeSetConvertToHTML.getAttribute(tag);
                if (attribute != null && (attribute instanceof AttributeSet)) {
                    writeAttributes((AttributeSet) attribute);
                }
                write('>');
                this.tags.addElement(tag);
                this.tagValues.addElement(attribute);
            }
        }
    }

    private boolean noMatchForTagInAttributes(AttributeSet attributeSet, HTML.Tag tag, Object obj) {
        if (attributeSet != null && attributeSet.isDefined(tag)) {
            Object attribute = attributeSet.getAttribute(tag);
            if (obj == null) {
                if (attribute == null) {
                    return false;
                }
                return true;
            }
            if (attribute != null && obj.equals(attribute)) {
                return false;
            }
            return true;
        }
        return true;
    }

    protected void closeOutUnwantedEmbeddedTags(AttributeSet attributeSet) throws IOException {
        this.tagsToRemove.removeAllElements();
        AttributeSet attributeSetConvertToHTML = convertToHTML(attributeSet, null);
        int i2 = -1;
        int size = this.tags.size();
        for (int i3 = size - 1; i3 >= 0; i3--) {
            HTML.Tag tagElementAt = this.tags.elementAt(i3);
            Object objElementAt = this.tagValues.elementAt(i3);
            if (attributeSetConvertToHTML == null || noMatchForTagInAttributes(attributeSetConvertToHTML, tagElementAt, objElementAt)) {
                i2 = i3;
                this.tagsToRemove.addElement(tagElementAt);
            }
        }
        if (i2 != -1) {
            boolean z2 = size - i2 == this.tagsToRemove.size();
            for (int i4 = size - 1; i4 >= i2; i4--) {
                HTML.Tag tagElementAt2 = this.tags.elementAt(i4);
                if (z2 || this.tagsToRemove.contains(tagElementAt2)) {
                    this.tags.removeElementAt(i4);
                    this.tagValues.removeElementAt(i4);
                }
                write('<');
                write('/');
                write(tagElementAt2.toString());
                write('>');
            }
            int size2 = this.tags.size();
            for (int i5 = i2; i5 < size2; i5++) {
                HTML.Tag tagElementAt3 = this.tags.elementAt(i5);
                write('<');
                write(tagElementAt3.toString());
                Object objElementAt2 = this.tagValues.elementAt(i5);
                if (objElementAt2 != null && (objElementAt2 instanceof AttributeSet)) {
                    writeAttributes((AttributeSet) objElementAt2);
                }
                write('>');
            }
        }
    }

    private boolean isFormElementWithContent(AttributeSet attributeSet) {
        return matchNameAttribute(attributeSet, HTML.Tag.TEXTAREA) || matchNameAttribute(attributeSet, HTML.Tag.SELECT);
    }

    private boolean indentNeedsIncrementing(Element element, Element element2) {
        if (element2.getParentElement() == element && !this.inPre) {
            if (this.indentNext) {
                this.indentNext = false;
                return true;
            }
            if (synthesizedElement(element2)) {
                this.indentNext = true;
                return false;
            }
            if (!synthesizedElement(element)) {
                return true;
            }
            return false;
        }
        return false;
    }

    void writeMaps(Enumeration enumeration) throws IOException {
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                Map map = (Map) enumeration.nextElement2();
                String name = map.getName();
                incrIndent();
                indentSmart();
                write("<map");
                if (name != null) {
                    write(" name=\"");
                    write(name);
                    write("\">");
                } else {
                    write('>');
                }
                writeLineSeparator();
                incrIndent();
                AttributeSet[] areas = map.getAreas();
                if (areas != null) {
                    for (AttributeSet attributeSet : areas) {
                        indentSmart();
                        write("<area");
                        writeAttributes(attributeSet);
                        write("></area>");
                        writeLineSeparator();
                    }
                }
                decrIndent();
                indentSmart();
                write("</map>");
                writeLineSeparator();
                decrIndent();
            }
        }
    }

    void writeStyles(StyleSheet styleSheet) throws IOException {
        Enumeration<?> styleNames;
        if (styleSheet != null && (styleNames = styleSheet.getStyleNames()) != null) {
            boolean z2 = false;
            while (styleNames.hasMoreElements()) {
                String str = (String) styleNames.nextElement2();
                if (!"default".equals(str) && writeStyle(str, styleSheet.getStyle(str), z2)) {
                    z2 = true;
                }
            }
            if (z2) {
                writeStyleEndTag();
            }
        }
    }

    boolean writeStyle(String str, Style style, boolean z2) throws IOException {
        String string;
        boolean z3 = false;
        Enumeration<?> attributeNames = style.getAttributeNames();
        if (attributeNames != null) {
            while (attributeNames.hasMoreElements()) {
                Object objNextElement2 = attributeNames.nextElement2();
                if ((objNextElement2 instanceof CSS.Attribute) && (string = style.getAttribute(objNextElement2).toString()) != null) {
                    if (!z2) {
                        writeStyleStartTag();
                        z2 = true;
                    }
                    if (!z3) {
                        z3 = true;
                        indentSmart();
                        write(str);
                        write(" {");
                    } else {
                        write(";");
                    }
                    write(' ');
                    write(objNextElement2.toString());
                    write(": ");
                    write(string);
                }
            }
        }
        if (z3) {
            write(" }");
            writeLineSeparator();
        }
        return z3;
    }

    void writeStyleStartTag() throws IOException {
        indentSmart();
        write("<style type=\"text/css\">");
        incrIndent();
        writeLineSeparator();
        indentSmart();
        write("<!--");
        incrIndent();
        writeLineSeparator();
    }

    void writeStyleEndTag() throws IOException {
        decrIndent();
        indentSmart();
        write("-->");
        writeLineSeparator();
        decrIndent();
        indentSmart();
        write("</style>");
        writeLineSeparator();
        indentSmart();
    }

    AttributeSet convertToHTML(AttributeSet attributeSet, MutableAttributeSet mutableAttributeSet) {
        if (mutableAttributeSet == null) {
            mutableAttributeSet = this.convAttr;
        }
        mutableAttributeSet.removeAttributes(mutableAttributeSet);
        if (this.writeCSS) {
            convertToHTML40(attributeSet, mutableAttributeSet);
        } else {
            convertToHTML32(attributeSet, mutableAttributeSet);
        }
        return mutableAttributeSet;
    }

    private static void convertToHTML32(AttributeSet attributeSet, MutableAttributeSet mutableAttributeSet) {
        if (attributeSet == null) {
            return;
        }
        Enumeration<?> attributeNames = attributeSet.getAttributeNames();
        String str = "";
        while (attributeNames.hasMoreElements()) {
            Object objNextElement2 = attributeNames.nextElement2();
            if (objNextElement2 instanceof CSS.Attribute) {
                if (objNextElement2 == CSS.Attribute.FONT_FAMILY || objNextElement2 == CSS.Attribute.FONT_SIZE || objNextElement2 == CSS.Attribute.COLOR) {
                    createFontAttribute((CSS.Attribute) objNextElement2, attributeSet, mutableAttributeSet);
                } else if (objNextElement2 == CSS.Attribute.FONT_WEIGHT) {
                    CSS.FontWeight fontWeight = (CSS.FontWeight) attributeSet.getAttribute(CSS.Attribute.FONT_WEIGHT);
                    if (fontWeight != null && fontWeight.getValue() > 400) {
                        addAttribute(mutableAttributeSet, HTML.Tag.f12847B, SimpleAttributeSet.EMPTY);
                    }
                } else if (objNextElement2 == CSS.Attribute.FONT_STYLE) {
                    if (attributeSet.getAttribute(objNextElement2).toString().indexOf("italic") >= 0) {
                        addAttribute(mutableAttributeSet, HTML.Tag.f12848I, SimpleAttributeSet.EMPTY);
                    }
                } else if (objNextElement2 == CSS.Attribute.TEXT_DECORATION) {
                    String string = attributeSet.getAttribute(objNextElement2).toString();
                    if (string.indexOf("underline") >= 0) {
                        addAttribute(mutableAttributeSet, HTML.Tag.f12851U, SimpleAttributeSet.EMPTY);
                    }
                    if (string.indexOf("line-through") >= 0) {
                        addAttribute(mutableAttributeSet, HTML.Tag.STRIKE, SimpleAttributeSet.EMPTY);
                    }
                } else if (objNextElement2 == CSS.Attribute.VERTICAL_ALIGN) {
                    String string2 = attributeSet.getAttribute(objNextElement2).toString();
                    if (string2.indexOf("sup") >= 0) {
                        addAttribute(mutableAttributeSet, HTML.Tag.SUP, SimpleAttributeSet.EMPTY);
                    }
                    if (string2.indexOf("sub") >= 0) {
                        addAttribute(mutableAttributeSet, HTML.Tag.SUB, SimpleAttributeSet.EMPTY);
                    }
                } else if (objNextElement2 == CSS.Attribute.TEXT_ALIGN) {
                    addAttribute(mutableAttributeSet, HTML.Attribute.ALIGN, attributeSet.getAttribute(objNextElement2).toString());
                } else {
                    if (str.length() > 0) {
                        str = str + VectorFormat.DEFAULT_SEPARATOR;
                    }
                    str = str + objNextElement2 + ": " + attributeSet.getAttribute(objNextElement2);
                }
            } else {
                Object attribute = attributeSet.getAttribute(objNextElement2);
                if (attribute instanceof AttributeSet) {
                    attribute = ((AttributeSet) attribute).copyAttributes();
                }
                addAttribute(mutableAttributeSet, objNextElement2, attribute);
            }
        }
        if (str.length() > 0) {
            mutableAttributeSet.addAttribute(HTML.Attribute.STYLE, str);
        }
    }

    private static void addAttribute(MutableAttributeSet mutableAttributeSet, Object obj, Object obj2) {
        Object attribute = mutableAttributeSet.getAttribute(obj);
        if (attribute == null || attribute == SimpleAttributeSet.EMPTY) {
            mutableAttributeSet.addAttribute(obj, obj2);
        } else if ((attribute instanceof MutableAttributeSet) && (obj2 instanceof AttributeSet)) {
            ((MutableAttributeSet) attribute).addAttributes((AttributeSet) obj2);
        }
    }

    private static void createFontAttribute(CSS.Attribute attribute, AttributeSet attributeSet, MutableAttributeSet mutableAttributeSet) {
        MutableAttributeSet simpleAttributeSet = (MutableAttributeSet) mutableAttributeSet.getAttribute(HTML.Tag.FONT);
        if (simpleAttributeSet == null) {
            simpleAttributeSet = new SimpleAttributeSet();
            mutableAttributeSet.addAttribute(HTML.Tag.FONT, simpleAttributeSet);
        }
        String string = attributeSet.getAttribute(attribute).toString();
        if (attribute == CSS.Attribute.FONT_FAMILY) {
            simpleAttributeSet.addAttribute(HTML.Attribute.FACE, string);
        } else if (attribute == CSS.Attribute.FONT_SIZE) {
            simpleAttributeSet.addAttribute(HTML.Attribute.SIZE, string);
        } else if (attribute == CSS.Attribute.COLOR) {
            simpleAttributeSet.addAttribute(HTML.Attribute.COLOR, string);
        }
    }

    private static void convertToHTML40(AttributeSet attributeSet, MutableAttributeSet mutableAttributeSet) {
        Enumeration<?> attributeNames = attributeSet.getAttributeNames();
        String str = "";
        while (attributeNames.hasMoreElements()) {
            Object objNextElement2 = attributeNames.nextElement2();
            if (objNextElement2 instanceof CSS.Attribute) {
                str = str + " " + objNextElement2 + "=" + attributeSet.getAttribute(objNextElement2) + ";";
            } else {
                mutableAttributeSet.addAttribute(objNextElement2, attributeSet.getAttribute(objNextElement2));
            }
        }
        if (str.length() > 0) {
            mutableAttributeSet.addAttribute(HTML.Attribute.STYLE, str);
        }
    }

    @Override // javax.swing.text.AbstractWriter
    protected void writeLineSeparator() throws IOException {
        boolean z2 = this.replaceEntities;
        this.replaceEntities = false;
        super.writeLineSeparator();
        this.replaceEntities = z2;
        this.indented = false;
    }

    @Override // javax.swing.text.AbstractWriter
    protected void output(char[] cArr, int i2, int i3) throws IOException {
        if (!this.replaceEntities) {
            super.output(cArr, i2, i3);
            return;
        }
        int i4 = i2;
        int i5 = i3 + i2;
        for (int i6 = i2; i6 < i5; i6++) {
            switch (cArr[i6]) {
                case '\t':
                case '\n':
                case '\r':
                    break;
                case '\"':
                    if (i6 > i4) {
                        super.output(cArr, i4, i6 - i4);
                    }
                    i4 = i6 + 1;
                    output(SerializerConstants.ENTITY_QUOT);
                    break;
                case '&':
                    if (i6 > i4) {
                        super.output(cArr, i4, i6 - i4);
                    }
                    i4 = i6 + 1;
                    output(SerializerConstants.ENTITY_AMP);
                    break;
                case '<':
                    if (i6 > i4) {
                        super.output(cArr, i4, i6 - i4);
                    }
                    i4 = i6 + 1;
                    output(SerializerConstants.ENTITY_LT);
                    break;
                case '>':
                    if (i6 > i4) {
                        super.output(cArr, i4, i6 - i4);
                    }
                    i4 = i6 + 1;
                    output(SerializerConstants.ENTITY_GT);
                    break;
                default:
                    if (cArr[i6] < ' ' || cArr[i6] > 127) {
                        if (i6 > i4) {
                            super.output(cArr, i4, i6 - i4);
                        }
                        i4 = i6 + 1;
                        output("&#");
                        output(String.valueOf((int) cArr[i6]));
                        output(";");
                        break;
                    } else {
                        break;
                    }
                    break;
            }
        }
        if (i4 < i5) {
            super.output(cArr, i4, i5 - i4);
        }
    }

    private void output(String str) throws IOException {
        int length = str.length();
        if (this.tempChars == null || this.tempChars.length < length) {
            this.tempChars = new char[length];
        }
        str.getChars(0, length, this.tempChars, 0);
        super.output(this.tempChars, 0, length);
    }

    private void indentSmart() throws IOException {
        if (!this.indented) {
            indent();
            this.indented = true;
        }
    }
}
