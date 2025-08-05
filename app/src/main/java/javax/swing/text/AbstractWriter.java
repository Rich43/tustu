package javax.swing.text;

import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;

/* loaded from: rt.jar:javax/swing/text/AbstractWriter.class */
public abstract class AbstractWriter {
    private ElementIterator it;
    private Writer out;
    private int indentLevel;
    private int indentSpace;
    private Document doc;
    private int maxLineLength;
    private int currLength;
    private int startOffset;
    private int endOffset;
    private int offsetIndent;
    private String lineSeparator;
    private boolean canWrapLines;
    private boolean isLineEmpty;
    private char[] indentChars;
    private char[] tempChars;
    private char[] newlineChars;
    private Segment segment;
    protected static final char NEWLINE = '\n';

    protected abstract void write() throws IOException, BadLocationException;

    protected AbstractWriter(Writer writer, Document document) {
        this(writer, document, 0, document.getLength());
    }

    protected AbstractWriter(Writer writer, Document document, int i2, int i3) {
        this.indentLevel = 0;
        this.indentSpace = 2;
        this.doc = null;
        this.maxLineLength = 100;
        this.currLength = 0;
        this.startOffset = 0;
        this.endOffset = 0;
        this.offsetIndent = 0;
        this.doc = document;
        this.it = new ElementIterator(document.getDefaultRootElement());
        this.out = writer;
        this.startOffset = i2;
        this.endOffset = i2 + i3;
        Object property = document.getProperty(DefaultEditorKit.EndOfLineStringProperty);
        if (property instanceof String) {
            setLineSeparator((String) property);
        } else {
            String property2 = null;
            try {
                property2 = System.getProperty("line.separator");
            } catch (SecurityException e2) {
            }
            setLineSeparator(property2 == null ? "\n" : property2);
        }
        this.canWrapLines = true;
    }

    protected AbstractWriter(Writer writer, Element element) {
        this(writer, element, 0, element.getEndOffset());
    }

    protected AbstractWriter(Writer writer, Element element, int i2, int i3) {
        this.indentLevel = 0;
        this.indentSpace = 2;
        this.doc = null;
        this.maxLineLength = 100;
        this.currLength = 0;
        this.startOffset = 0;
        this.endOffset = 0;
        this.offsetIndent = 0;
        this.doc = element.getDocument();
        this.it = new ElementIterator(element);
        this.out = writer;
        this.startOffset = i2;
        this.endOffset = i2 + i3;
        this.canWrapLines = true;
    }

    public int getStartOffset() {
        return this.startOffset;
    }

    public int getEndOffset() {
        return this.endOffset;
    }

    protected ElementIterator getElementIterator() {
        return this.it;
    }

    protected Writer getWriter() {
        return this.out;
    }

    protected Document getDocument() {
        return this.doc;
    }

    protected boolean inRange(Element element) {
        int startOffset = getStartOffset();
        int endOffset = getEndOffset();
        if (element.getStartOffset() < startOffset || element.getStartOffset() >= endOffset) {
            if (startOffset >= element.getStartOffset() && startOffset < element.getEndOffset()) {
                return true;
            }
            return false;
        }
        return true;
    }

    protected String getText(Element element) throws BadLocationException {
        return this.doc.getText(element.getStartOffset(), element.getEndOffset() - element.getStartOffset());
    }

    protected void text(Element element) throws IOException, BadLocationException {
        int iMax = Math.max(getStartOffset(), element.getStartOffset());
        int iMin = Math.min(getEndOffset(), element.getEndOffset());
        if (iMax < iMin) {
            if (this.segment == null) {
                this.segment = new Segment();
            }
            getDocument().getText(iMax, iMin - iMax, this.segment);
            if (this.segment.count > 0) {
                write(this.segment.array, this.segment.offset, this.segment.count);
            }
        }
    }

    protected void setLineLength(int i2) {
        this.maxLineLength = i2;
    }

    protected int getLineLength() {
        return this.maxLineLength;
    }

    protected void setCurrentLineLength(int i2) {
        this.currLength = i2;
        this.isLineEmpty = this.currLength == 0;
    }

    protected int getCurrentLineLength() {
        return this.currLength;
    }

    protected boolean isLineEmpty() {
        return this.isLineEmpty;
    }

    protected void setCanWrapLines(boolean z2) {
        this.canWrapLines = z2;
    }

    protected boolean getCanWrapLines() {
        return this.canWrapLines;
    }

    protected void setIndentSpace(int i2) {
        this.indentSpace = i2;
    }

    protected int getIndentSpace() {
        return this.indentSpace;
    }

    public void setLineSeparator(String str) {
        this.lineSeparator = str;
    }

    public String getLineSeparator() {
        return this.lineSeparator;
    }

    protected void incrIndent() {
        if (this.offsetIndent > 0) {
            this.offsetIndent++;
            return;
        }
        int i2 = this.indentLevel + 1;
        this.indentLevel = i2;
        if (i2 * getIndentSpace() >= getLineLength()) {
            this.offsetIndent++;
            this.indentLevel--;
        }
    }

    protected void decrIndent() {
        if (this.offsetIndent > 0) {
            this.offsetIndent--;
        } else {
            this.indentLevel--;
        }
    }

    protected int getIndentLevel() {
        return this.indentLevel;
    }

    protected void indent() throws IOException {
        int indentLevel = getIndentLevel() * getIndentSpace();
        if (this.indentChars == null || indentLevel > this.indentChars.length) {
            this.indentChars = new char[indentLevel];
            for (int i2 = 0; i2 < indentLevel; i2++) {
                this.indentChars[i2] = ' ';
            }
        }
        int currentLineLength = getCurrentLineLength();
        boolean zIsLineEmpty = isLineEmpty();
        output(this.indentChars, 0, indentLevel);
        if (zIsLineEmpty && currentLineLength == 0) {
            this.isLineEmpty = true;
        }
    }

    protected void write(char c2) throws IOException {
        if (this.tempChars == null) {
            this.tempChars = new char[128];
        }
        this.tempChars[0] = c2;
        write(this.tempChars, 0, 1);
    }

    protected void write(String str) throws IOException {
        if (str == null) {
            return;
        }
        int length = str.length();
        if (this.tempChars == null || this.tempChars.length < length) {
            this.tempChars = new char[length];
        }
        str.getChars(0, length, this.tempChars, 0);
        write(this.tempChars, 0, length);
    }

    protected void writeLineSeparator() throws IOException {
        String lineSeparator = getLineSeparator();
        int length = lineSeparator.length();
        if (this.newlineChars == null || this.newlineChars.length < length) {
            this.newlineChars = new char[length];
        }
        lineSeparator.getChars(0, length, this.newlineChars, 0);
        output(this.newlineChars, 0, length);
        setCurrentLineLength(0);
    }

    protected void write(char[] cArr, int i2, int i3) throws IOException {
        int i4;
        if (!getCanWrapLines()) {
            int i5 = i2;
            int i6 = i2 + i3;
            int iIndexOf = indexOf(cArr, '\n', i2, i6);
            while (true) {
                int i7 = iIndexOf;
                if (i7 == -1) {
                    break;
                }
                if (i7 > i5) {
                    output(cArr, i5, i7 - i5);
                }
                writeLineSeparator();
                i5 = i7 + 1;
                iIndexOf = indexOf(cArr, '\n', i5, i6);
            }
            if (i5 < i6) {
                output(cArr, i5, i6 - i5);
                return;
            }
            return;
        }
        int i8 = i2;
        int i9 = i2 + i3;
        getCurrentLineLength();
        int lineLength = getLineLength();
        while (i8 < i9) {
            int iIndexOf2 = indexOf(cArr, '\n', i8, i9);
            boolean z2 = false;
            boolean z3 = false;
            int currentLineLength = getCurrentLineLength();
            if (iIndexOf2 != -1 && currentLineLength + (iIndexOf2 - i8) < lineLength) {
                if (iIndexOf2 > i8) {
                    output(cArr, i8, iIndexOf2 - i8);
                }
                i8 = iIndexOf2 + 1;
                z3 = true;
            } else if (iIndexOf2 == -1 && currentLineLength + (i9 - i8) < lineLength) {
                if (i9 > i8) {
                    output(cArr, i8, i9 - i8);
                }
                i8 = i9;
            } else {
                int i10 = -1;
                int iMin = Math.min(i9 - i8, (lineLength - currentLineLength) - 1);
                for (int i11 = 0; i11 < iMin; i11++) {
                    if (Character.isWhitespace(cArr[i11 + i8])) {
                        i10 = i11;
                    }
                }
                if (i10 != -1) {
                    int i12 = i10 + i8 + 1;
                    output(cArr, i8, i12 - i8);
                    i8 = i12;
                    z2 = true;
                } else {
                    int iMax = Math.max(0, iMin);
                    int i13 = i9 - i8;
                    while (true) {
                        if (iMax >= i13) {
                            break;
                        }
                        if (Character.isWhitespace(cArr[iMax + i8])) {
                            i10 = iMax;
                            break;
                        }
                        iMax++;
                    }
                    if (i10 == -1) {
                        output(cArr, i8, i9 - i8);
                        i4 = i9;
                    } else {
                        int i14 = i10 + i8;
                        if (cArr[i14] == '\n') {
                            i4 = i14 + 1;
                            output(cArr, i8, i14 - i8);
                            z3 = true;
                        } else {
                            i4 = i14 + 1;
                            output(cArr, i8, i4 - i8);
                            z2 = true;
                        }
                    }
                    i8 = i4;
                }
            }
            if (z3 || z2 || i8 < i9) {
                writeLineSeparator();
                if (i8 < i9 || !z3) {
                    indent();
                }
            }
        }
    }

    protected void writeAttributes(AttributeSet attributeSet) throws IOException {
        Enumeration<?> attributeNames = attributeSet.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            Object objNextElement2 = attributeNames.nextElement2();
            write(" " + objNextElement2 + "=" + attributeSet.getAttribute(objNextElement2));
        }
    }

    protected void output(char[] cArr, int i2, int i3) throws IOException {
        getWriter().write(cArr, i2, i3);
        setCurrentLineLength(getCurrentLineLength() + i3);
    }

    private int indexOf(char[] cArr, char c2, int i2, int i3) {
        while (i2 < i3) {
            if (cArr[i2] == c2) {
                return i2;
            }
            i2++;
        }
        return -1;
    }
}
