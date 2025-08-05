package javax.swing.text.html;

import java.awt.Color;
import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AbstractWriter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.ElementIterator;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import org.apache.commons.math3.geometry.VectorFormat;
import org.icepdf.core.pobjects.annotations.FreeTextAnnotation;

/* loaded from: rt.jar:javax/swing/text/html/MinimalHTMLWriter.class */
public class MinimalHTMLWriter extends AbstractWriter {
    private static final int BOLD = 1;
    private static final int ITALIC = 2;
    private static final int UNDERLINE = 4;
    private static final CSS css = new CSS();
    private int fontMask;
    int startOffset;
    int endOffset;
    private AttributeSet fontAttributes;
    private Hashtable<String, String> styleNameMapping;

    public MinimalHTMLWriter(Writer writer, StyledDocument styledDocument) {
        super(writer, styledDocument);
        this.fontMask = 0;
        this.startOffset = 0;
        this.endOffset = 0;
    }

    public MinimalHTMLWriter(Writer writer, StyledDocument styledDocument, int i2, int i3) {
        super(writer, styledDocument, i2, i3);
        this.fontMask = 0;
        this.startOffset = 0;
        this.endOffset = 0;
    }

    @Override // javax.swing.text.AbstractWriter
    public void write() throws IOException, BadLocationException {
        this.styleNameMapping = new Hashtable<>();
        writeStartTag("<html>");
        writeHeader();
        writeBody();
        writeEndTag("</html>");
    }

    @Override // javax.swing.text.AbstractWriter
    protected void writeAttributes(AttributeSet attributeSet) throws IOException {
        Enumeration<?> attributeNames = attributeSet.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            Object objNextElement2 = attributeNames.nextElement2();
            if ((objNextElement2 instanceof StyleConstants.ParagraphConstants) || (objNextElement2 instanceof StyleConstants.CharacterConstants) || (objNextElement2 instanceof StyleConstants.FontConstants) || (objNextElement2 instanceof StyleConstants.ColorConstants)) {
                indent();
                write(objNextElement2.toString());
                write(':');
                write(css.styleConstantsValueToCSSValue((StyleConstants) objNextElement2, attributeSet.getAttribute(objNextElement2)).toString());
                write(';');
                write('\n');
            }
        }
    }

    @Override // javax.swing.text.AbstractWriter
    protected void text(Element element) throws IOException, BadLocationException {
        String text = getText(element);
        if (text.length() > 0 && text.charAt(text.length() - 1) == '\n') {
            text = text.substring(0, text.length() - 1);
        }
        if (text.length() > 0) {
            write(text);
        }
    }

    protected void writeStartTag(String str) throws IOException {
        indent();
        write(str);
        write('\n');
        incrIndent();
    }

    protected void writeEndTag(String str) throws IOException {
        decrIndent();
        indent();
        write(str);
        write('\n');
    }

    protected void writeHeader() throws IOException {
        writeStartTag("<head>");
        writeStartTag("<style>");
        writeStartTag("<!--");
        writeStyles();
        writeEndTag("-->");
        writeEndTag("</style>");
        writeEndTag("</head>");
    }

    protected void writeStyles() throws IOException {
        DefaultStyledDocument defaultStyledDocument = (DefaultStyledDocument) getDocument();
        Enumeration<?> styleNames = defaultStyledDocument.getStyleNames();
        while (styleNames.hasMoreElements()) {
            Style style = defaultStyledDocument.getStyle((String) styleNames.nextElement2());
            if (style.getAttributeCount() != 1 || !style.isDefined(StyleConstants.NameAttribute)) {
                indent();
                write("p." + addStyleName(style.getName()));
                write(" {\n");
                incrIndent();
                writeAttributes(style);
                decrIndent();
                indent();
                write("}\n");
            }
        }
    }

    protected void writeBody() throws IOException, BadLocationException {
        ElementIterator elementIterator = getElementIterator();
        elementIterator.current();
        writeStartTag("<body>");
        boolean z2 = false;
        while (true) {
            Element next = elementIterator.next();
            if (next == null) {
                break;
            }
            if (inRange(next)) {
                if (next instanceof AbstractDocument.BranchElement) {
                    if (z2) {
                        writeEndParagraph();
                        z2 = false;
                        this.fontMask = 0;
                    }
                    writeStartParagraph(next);
                } else if (isText(next)) {
                    writeContent(next, !z2);
                    z2 = true;
                } else {
                    writeLeaf(next);
                    z2 = true;
                }
            }
        }
        if (z2) {
            writeEndParagraph();
        }
        writeEndTag(FreeTextAnnotation.BODY_END);
    }

    protected void writeEndParagraph() throws IOException {
        writeEndMask(this.fontMask);
        if (inFontTag()) {
            endSpanTag();
        } else {
            write('\n');
        }
        writeEndTag("</p>");
    }

    protected void writeStartParagraph(Element element) throws IOException {
        Object attribute = element.getAttributes().getAttribute(StyleConstants.ResolveAttribute);
        if (attribute instanceof StyleContext.NamedStyle) {
            writeStartTag("<p class=" + mapStyleName(((StyleContext.NamedStyle) attribute).getName()) + ">");
        } else {
            writeStartTag("<p>");
        }
    }

    protected void writeLeaf(Element element) throws IOException {
        indent();
        if (element.getName() == "icon") {
            writeImage(element);
        } else if (element.getName() == "component") {
            writeComponent(element);
        }
    }

    protected void writeImage(Element element) throws IOException {
    }

    protected void writeComponent(Element element) throws IOException {
    }

    protected boolean isText(Element element) {
        return element.getName() == AbstractDocument.ContentElementName;
    }

    protected void writeContent(Element element, boolean z2) throws IOException, BadLocationException {
        AttributeSet attributes = element.getAttributes();
        writeNonHTMLAttributes(attributes);
        if (z2) {
            indent();
        }
        writeHTMLTags(attributes);
        text(element);
    }

    protected void writeHTMLTags(AttributeSet attributeSet) throws IOException {
        int i2 = this.fontMask;
        setFontMask(attributeSet);
        int i3 = 0;
        int i4 = 0;
        if ((i2 & 1) != 0) {
            if ((this.fontMask & 1) == 0) {
                i3 = 0 | 1;
            }
        } else if ((this.fontMask & 1) != 0) {
            i4 = 0 | 1;
        }
        if ((i2 & 2) != 0) {
            if ((this.fontMask & 2) == 0) {
                i3 |= 2;
            }
        } else if ((this.fontMask & 2) != 0) {
            i4 |= 2;
        }
        if ((i2 & 4) != 0) {
            if ((this.fontMask & 4) == 0) {
                i3 |= 4;
            }
        } else if ((this.fontMask & 4) != 0) {
            i4 |= 4;
        }
        writeEndMask(i3);
        writeStartMask(i4);
    }

    private void setFontMask(AttributeSet attributeSet) {
        if (StyleConstants.isBold(attributeSet)) {
            this.fontMask |= 1;
        }
        if (StyleConstants.isItalic(attributeSet)) {
            this.fontMask |= 2;
        }
        if (StyleConstants.isUnderline(attributeSet)) {
            this.fontMask |= 4;
        }
    }

    private void writeStartMask(int i2) throws IOException {
        if (i2 != 0) {
            if ((i2 & 4) != 0) {
                write("<u>");
            }
            if ((i2 & 2) != 0) {
                write("<i>");
            }
            if ((i2 & 1) != 0) {
                write("<b>");
            }
        }
    }

    private void writeEndMask(int i2) throws IOException {
        if (i2 != 0) {
            if ((i2 & 1) != 0) {
                write("</b>");
            }
            if ((i2 & 2) != 0) {
                write("</i>");
            }
            if ((i2 & 4) != 0) {
                write("</u>");
            }
        }
    }

    protected void writeNonHTMLAttributes(AttributeSet attributeSet) throws IOException {
        String str = "";
        if (inFontTag() && this.fontAttributes.isEqual(attributeSet)) {
            return;
        }
        boolean z2 = true;
        Color color = (Color) attributeSet.getAttribute(StyleConstants.Foreground);
        if (color != null) {
            str = str + "color: " + css.styleConstantsValueToCSSValue((StyleConstants) StyleConstants.Foreground, color);
            z2 = false;
        }
        Integer num = (Integer) attributeSet.getAttribute(StyleConstants.FontSize);
        if (num != null) {
            if (!z2) {
                str = str + VectorFormat.DEFAULT_SEPARATOR;
            }
            str = str + "font-size: " + num.intValue() + "pt";
            z2 = false;
        }
        String str2 = (String) attributeSet.getAttribute(StyleConstants.FontFamily);
        if (str2 != null) {
            if (!z2) {
                str = str + VectorFormat.DEFAULT_SEPARATOR;
            }
            str = str + "font-family: " + str2;
        }
        if (str.length() > 0) {
            if (this.fontMask != 0) {
                writeEndMask(this.fontMask);
                this.fontMask = 0;
            }
            startSpanTag(str);
            this.fontAttributes = attributeSet;
            return;
        }
        if (this.fontAttributes != null) {
            writeEndMask(this.fontMask);
            this.fontMask = 0;
            endSpanTag();
        }
    }

    protected boolean inFontTag() {
        return this.fontAttributes != null;
    }

    protected void endFontTag() throws IOException {
        write('\n');
        writeEndTag("</font>");
        this.fontAttributes = null;
    }

    protected void startFontTag(String str) throws IOException {
        boolean z2 = false;
        if (inFontTag()) {
            endFontTag();
            z2 = true;
        }
        writeStartTag("<font style=\"" + str + "\">");
        if (z2) {
            indent();
        }
    }

    private void startSpanTag(String str) throws IOException {
        boolean z2 = false;
        if (inFontTag()) {
            endSpanTag();
            z2 = true;
        }
        writeStartTag("<span style=\"" + str + "\">");
        if (z2) {
            indent();
        }
    }

    private void endSpanTag() throws IOException {
        write('\n');
        writeEndTag("</span>");
        this.fontAttributes = null;
    }

    private String addStyleName(String str) {
        if (this.styleNameMapping == null) {
            return str;
        }
        StringBuilder sb = null;
        for (int length = str.length() - 1; length >= 0; length--) {
            if (!isValidCharacter(str.charAt(length))) {
                if (sb == null) {
                    sb = new StringBuilder(str);
                }
                sb.setCharAt(length, 'a');
            }
        }
        String string = sb != null ? sb.toString() : str;
        while (true) {
            String str2 = string;
            if (this.styleNameMapping.get(str2) != null) {
                string = str2 + 'x';
            } else {
                this.styleNameMapping.put(str, str2);
                return str2;
            }
        }
    }

    private String mapStyleName(String str) {
        if (this.styleNameMapping == null) {
            return str;
        }
        String str2 = this.styleNameMapping.get(str);
        return str2 == null ? str : str2;
    }

    private boolean isValidCharacter(char c2) {
        return (c2 >= 'a' && c2 <= 'z') || (c2 >= 'A' && c2 <= 'Z');
    }
}
