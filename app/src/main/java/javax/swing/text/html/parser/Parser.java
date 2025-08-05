package javax.swing.text.html.parser;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.org.apache.xml.internal.serialize.LineSeparator;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.text.ChangedCharSetException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.html.HTML;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:javax/swing/text/html/parser/Parser.class */
public class Parser implements DTDConstants {
    private TagElement last;
    private boolean space;
    protected DTD dtd;
    private int ch;
    private int ln;
    private Reader in;
    private Element recent;
    private TagStack stack;
    private boolean ignoreSpace;
    private int crlfCount;
    private int crCount;
    private int lfCount;
    private int currentBlockStartPos;
    private int lastBlockStartPos;
    private static final String START_COMMENT = "<!--";
    private static final String END_COMMENT = "-->";
    private int pos;
    private int len;
    private int currentPosition;
    private static final char[] cp1252Map = {8218, 402, 8222, 8230, 8224, 8225, 710, 8240, 352, 8249, 338, 141, 142, 143, 144, 8216, 8217, 8220, 8221, 8226, 8211, 8212, 732, 8482, 353, 8250, 339, 157, 158, 376};
    private static final char[] SCRIPT_END_TAG = "</script>".toCharArray();
    private static final char[] SCRIPT_END_TAG_UPPER_CASE = "</SCRIPT>".toCharArray();
    private char[] text = new char[1024];
    private int textpos = 0;
    private char[] str = new char[128];
    private int strpos = 0;
    private boolean skipTag = false;
    private TagElement lastFormSent = null;
    private SimpleAttributeSet attributes = new SimpleAttributeSet();
    private boolean seenHtml = false;
    private boolean seenHead = false;
    private boolean seenBody = false;
    protected boolean strict = false;
    private char[] buf = new char[1];

    public Parser(DTD dtd) {
        this.dtd = null;
        this.dtd = dtd;
    }

    protected int getCurrentLine() {
        return this.ln;
    }

    int getBlockStartPosition() {
        return Math.max(0, this.lastBlockStartPos - 1);
    }

    protected TagElement makeTag(Element element, boolean z2) {
        return new TagElement(element, z2);
    }

    protected TagElement makeTag(Element element) {
        return makeTag(element, false);
    }

    protected SimpleAttributeSet getAttributes() {
        return this.attributes;
    }

    protected void flushAttributes() {
        this.attributes.removeAttributes(this.attributes);
    }

    protected void handleText(char[] cArr) {
    }

    protected void handleTitle(char[] cArr) {
        handleText(cArr);
    }

    protected void handleComment(char[] cArr) {
    }

    protected void handleEOFInComment() {
        int iStrIndexOf = strIndexOf('\n');
        if (iStrIndexOf >= 0) {
            handleComment(getChars(0, iStrIndexOf));
            try {
                this.in.close();
                this.in = new CharArrayReader(getChars(iStrIndexOf + 1));
                this.ch = 62;
            } catch (IOException e2) {
                error("ioexception");
            }
            resetStrBuffer();
            return;
        }
        error("eof.comment");
    }

    protected void handleEmptyTag(TagElement tagElement) throws ChangedCharSetException {
    }

    protected void handleStartTag(TagElement tagElement) {
    }

    protected void handleEndTag(TagElement tagElement) {
    }

    protected void handleError(int i2, String str) {
    }

    void handleText(TagElement tagElement) {
        if (tagElement.breaksFlow()) {
            this.space = false;
            if (!this.strict) {
                this.ignoreSpace = true;
            }
        }
        if (this.textpos == 0 && (!this.space || this.stack == null || this.last.breaksFlow() || !this.stack.advance(this.dtd.pcdata))) {
            this.last = tagElement;
            this.space = false;
            this.lastBlockStartPos = this.currentBlockStartPos;
            return;
        }
        if (this.space) {
            if (!this.ignoreSpace) {
                if (this.textpos + 1 > this.text.length) {
                    char[] cArr = new char[this.text.length + 200];
                    System.arraycopy(this.text, 0, cArr, 0, this.text.length);
                    this.text = cArr;
                }
                char[] cArr2 = this.text;
                int i2 = this.textpos;
                this.textpos = i2 + 1;
                cArr2[i2] = ' ';
                if (!this.strict && !tagElement.getElement().isEmpty()) {
                    this.ignoreSpace = true;
                }
            }
            this.space = false;
        }
        char[] cArr3 = new char[this.textpos];
        System.arraycopy(this.text, 0, cArr3, 0, this.textpos);
        if (tagElement.getElement().getName().equals("title")) {
            handleTitle(cArr3);
        } else {
            handleText(cArr3);
        }
        this.lastBlockStartPos = this.currentBlockStartPos;
        this.textpos = 0;
        this.last = tagElement;
        this.space = false;
    }

    protected void error(String str, String str2, String str3, String str4) {
        handleError(this.ln, str + " " + str2 + " " + str3 + " " + str4);
    }

    protected void error(String str, String str2, String str3) {
        error(str, str2, str3, "?");
    }

    protected void error(String str, String str2) {
        error(str, str2, "?", "?");
    }

    protected void error(String str) {
        error(str, "?", "?", "?");
    }

    protected void startTag(TagElement tagElement) throws ChangedCharSetException {
        Element element = tagElement.getElement();
        if (!element.isEmpty() || ((this.last != null && !this.last.breaksFlow()) || this.textpos != 0)) {
            handleText(tagElement);
        } else {
            this.last = tagElement;
            this.space = false;
        }
        this.lastBlockStartPos = this.currentBlockStartPos;
        AttributeList attributeList = element.atts;
        while (true) {
            AttributeList attributeList2 = attributeList;
            if (attributeList2 == null) {
                break;
            }
            if (attributeList2.modifier == 2 && (this.attributes.isEmpty() || (!this.attributes.isDefined(attributeList2.name) && !this.attributes.isDefined(HTML.getAttributeKey(attributeList2.name))))) {
                error("req.att ", attributeList2.getName(), element.getName());
            }
            attributeList = attributeList2.next;
        }
        if (element.isEmpty()) {
            handleEmptyTag(tagElement);
            return;
        }
        this.recent = element;
        this.stack = new TagStack(tagElement, this.stack);
        handleStartTag(tagElement);
    }

    protected void endTag(boolean z2) {
        handleText(this.stack.tag);
        if (z2 && !this.stack.elem.omitEnd()) {
            error("end.missing", this.stack.elem.getName());
        } else if (!this.stack.terminate()) {
            error("end.unexpected", this.stack.elem.getName());
        }
        handleEndTag(this.stack.tag);
        this.stack = this.stack.next;
        this.recent = this.stack != null ? this.stack.elem : null;
    }

    boolean ignoreElement(Element element) {
        TagStack tagStack;
        String name = this.stack.elem.getName();
        String name2 = element.getName();
        if (!name2.equals("html") || !this.seenHtml) {
            if (!name2.equals("head") || !this.seenHead) {
                if (name2.equals("body") && this.seenBody) {
                    return true;
                }
                if (name2.equals("dt") || name2.equals("dd")) {
                    TagStack tagStack2 = this.stack;
                    while (true) {
                        tagStack = tagStack2;
                        if (tagStack == null || tagStack.elem.getName().equals("dl")) {
                            break;
                        }
                        tagStack2 = tagStack.next;
                    }
                    if (tagStack == null) {
                        return true;
                    }
                }
                if (!name.equals("table") || name2.equals("#pcdata") || name2.equals("input")) {
                    if (!name2.equals("font") || (!name.equals("ul") && !name.equals("ol"))) {
                        if (!name2.equals("meta") || this.stack == null) {
                            if (!name2.equals(Constants.ATTRNAME_STYLE) || !this.seenBody) {
                                if (name.equals("table") && name2.equals("a")) {
                                    return true;
                                }
                                return false;
                            }
                            return true;
                        }
                        return true;
                    }
                    return true;
                }
                return true;
            }
            return true;
        }
        return true;
    }

    protected void markFirstTime(Element element) {
        String name = element.getName();
        if (name.equals("html")) {
            this.seenHtml = true;
            return;
        }
        if (name.equals("head")) {
            this.seenHead = true;
            return;
        }
        if (name.equals("body")) {
            if (this.buf.length == 1) {
                char[] cArr = new char[256];
                cArr[0] = this.buf[0];
                this.buf = cArr;
            }
            this.seenBody = true;
        }
    }

    boolean legalElementContext(Element element) throws ChangedCharSetException {
        ContentModel content;
        if (this.stack == null) {
            if (element != this.dtd.html) {
                startTag(makeTag(this.dtd.html, true));
                return legalElementContext(element);
            }
            return true;
        }
        if (this.stack.advance(element)) {
            markFirstTime(element);
            return true;
        }
        boolean z2 = false;
        String name = this.stack.elem.getName();
        String name2 = element.getName();
        if (!this.strict && ((name.equals("table") && name2.equals("td")) || ((name.equals("table") && name2.equals("th")) || (name.equals("tr") && !name2.equals("tr"))))) {
            z2 = true;
        }
        if (!this.strict && !z2 && (this.stack.elem.getName() != element.getName() || element.getName().equals("body"))) {
            boolean zIgnoreElement = ignoreElement(element);
            this.skipTag = zIgnoreElement;
            if (zIgnoreElement) {
                error("tag.ignore", element.getName());
                return this.skipTag;
            }
        }
        if (!this.strict && name.equals("table") && !name2.equals("tr") && !name2.equals("td") && !name2.equals("th") && !name2.equals("caption")) {
            TagElement tagElementMakeTag = makeTag(this.dtd.getElement("tr"), true);
            legalTagContext(tagElementMakeTag);
            startTag(tagElementMakeTag);
            error("start.missing", element.getName());
            return legalElementContext(element);
        }
        if (!z2 && this.stack.terminate() && (!this.strict || this.stack.elem.omitEnd())) {
            TagStack tagStack = this.stack.next;
            while (true) {
                TagStack tagStack2 = tagStack;
                if (tagStack2 == null) {
                    break;
                }
                if (tagStack2.advance(element)) {
                    while (this.stack != tagStack2) {
                        endTag(true);
                    }
                    return true;
                }
                if (!tagStack2.terminate() || (this.strict && !tagStack2.elem.omitEnd())) {
                    break;
                }
                tagStack = tagStack2.next;
            }
        }
        Element elementFirst = this.stack.first();
        if (elementFirst != null && ((!this.strict || elementFirst.omitStart()) && (elementFirst != this.dtd.head || element != this.dtd.pcdata))) {
            TagElement tagElementMakeTag2 = makeTag(elementFirst, true);
            legalTagContext(tagElementMakeTag2);
            startTag(tagElementMakeTag2);
            if (!elementFirst.omitStart()) {
                error("start.missing", element.getName());
            }
            return legalElementContext(element);
        }
        if (!this.strict) {
            ContentModel contentModel = this.stack.contentModel();
            Vector<Element> vector = new Vector<>();
            if (contentModel != null) {
                contentModel.getElements(vector);
                Iterator<Element> it = vector.iterator();
                while (it.hasNext()) {
                    Element next = it.next();
                    if (!this.stack.excluded(next.getIndex())) {
                        boolean z3 = false;
                        AttributeList attributes = next.getAttributes();
                        while (true) {
                            AttributeList attributeList = attributes;
                            if (attributeList == null) {
                                break;
                            }
                            if (attributeList.modifier != 2) {
                                attributes = attributeList.next;
                            } else {
                                z3 = true;
                                break;
                            }
                        }
                        if (!z3 && (content = next.getContent()) != null && content.first(element)) {
                            TagElement tagElementMakeTag3 = makeTag(next, true);
                            legalTagContext(tagElementMakeTag3);
                            startTag(tagElementMakeTag3);
                            error("start.missing", next.getName());
                            return legalElementContext(element);
                        }
                    }
                }
            }
        }
        if (!this.stack.terminate() || this.stack.elem == this.dtd.body) {
            return false;
        }
        if (!this.strict || this.stack.elem.omitEnd()) {
            if (!this.stack.elem.omitEnd()) {
                error("end.missing", element.getName());
            }
            endTag(true);
            return legalElementContext(element);
        }
        return false;
    }

    void legalTagContext(TagElement tagElement) throws ChangedCharSetException {
        if (legalElementContext(tagElement.getElement())) {
            markFirstTime(tagElement.getElement());
            return;
        }
        if (tagElement.breaksFlow() && this.stack != null && !this.stack.tag.breaksFlow()) {
            endTag(true);
            legalTagContext(tagElement);
            return;
        }
        TagStack tagStack = this.stack;
        while (true) {
            TagStack tagStack2 = tagStack;
            if (tagStack2 != null) {
                if (tagStack2.tag.getElement() != this.dtd.head) {
                    tagStack = tagStack2.next;
                } else {
                    while (this.stack != tagStack2) {
                        endTag(true);
                    }
                    endTag(true);
                    legalTagContext(tagElement);
                    return;
                }
            } else {
                error("tag.unexpected", tagElement.getElement().getName());
                return;
            }
        }
    }

    void errorContext() throws ChangedCharSetException {
        while (this.stack != null && this.stack.tag.getElement() != this.dtd.body) {
            handleEndTag(this.stack.tag);
            this.stack = this.stack.next;
        }
        if (this.stack == null) {
            legalElementContext(this.dtd.body);
            startTag(makeTag(this.dtd.body, true));
        }
    }

    void addString(int i2) {
        if (this.strpos == this.str.length) {
            char[] cArr = new char[this.str.length + 128];
            System.arraycopy(this.str, 0, cArr, 0, this.str.length);
            this.str = cArr;
        }
        char[] cArr2 = this.str;
        int i3 = this.strpos;
        this.strpos = i3 + 1;
        cArr2[i3] = (char) i2;
    }

    String getString(int i2) {
        char[] cArr = new char[this.strpos - i2];
        System.arraycopy(this.str, i2, cArr, 0, this.strpos - i2);
        this.strpos = i2;
        return new String(cArr);
    }

    char[] getChars(int i2) {
        char[] cArr = new char[this.strpos - i2];
        System.arraycopy(this.str, i2, cArr, 0, this.strpos - i2);
        this.strpos = i2;
        return cArr;
    }

    char[] getChars(int i2, int i3) {
        char[] cArr = new char[i3 - i2];
        System.arraycopy(this.str, i2, cArr, 0, i3 - i2);
        return cArr;
    }

    void resetStrBuffer() {
        this.strpos = 0;
    }

    int strIndexOf(char c2) {
        for (int i2 = 0; i2 < this.strpos; i2++) {
            if (this.str[i2] == c2) {
                return i2;
            }
        }
        return -1;
    }

    void skipSpace() throws IOException {
        while (true) {
            switch (this.ch) {
                case 9:
                case 32:
                    this.ch = readCh();
                    break;
                case 10:
                    this.ln++;
                    this.ch = readCh();
                    this.lfCount++;
                    break;
                case 13:
                    this.ln++;
                    int ch = readCh();
                    this.ch = ch;
                    if (ch == 10) {
                        this.ch = readCh();
                        this.crlfCount++;
                        break;
                    } else {
                        this.crCount++;
                        break;
                    }
                default:
                    return;
            }
        }
    }

    boolean parseIdentifier(boolean z2) throws IOException {
        switch (this.ch) {
            case 65:
            case 66:
            case 67:
            case 68:
            case 69:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
            case 78:
            case 79:
            case 80:
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
                if (z2) {
                    this.ch = 97 + (this.ch - 65);
                    break;
                }
                break;
            case 91:
            case 92:
            case 93:
            case 94:
            case 95:
            case 96:
            default:
                return false;
            case 97:
            case 98:
            case 99:
            case 100:
            case 101:
            case 102:
            case 103:
            case 104:
            case 105:
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
            case 111:
            case 112:
            case 113:
            case 114:
            case 115:
            case 116:
            case 117:
            case 118:
            case 119:
            case 120:
            case 121:
            case 122:
                break;
        }
        while (true) {
            addString(this.ch);
            int ch = readCh();
            this.ch = ch;
            switch (ch) {
                case 45:
                case 46:
                case 48:
                case 49:
                case 50:
                case 51:
                case 52:
                case 53:
                case 54:
                case 55:
                case 56:
                case 57:
                case 95:
                case 97:
                case 98:
                case 99:
                case 100:
                case 101:
                case 102:
                case 103:
                case 104:
                case 105:
                case 106:
                case 107:
                case 108:
                case 109:
                case 110:
                case 111:
                case 112:
                case 113:
                case 114:
                case 115:
                case 116:
                case 117:
                case 118:
                case 119:
                case 120:
                case 121:
                case 122:
                    break;
                case 47:
                case 58:
                case 59:
                case 60:
                case 61:
                case 62:
                case 63:
                case 64:
                case 91:
                case 92:
                case 93:
                case 94:
                case 96:
                default:
                    return true;
                case 65:
                case 66:
                case 67:
                case 68:
                case 69:
                case 70:
                case 71:
                case 72:
                case 73:
                case 74:
                case 75:
                case 76:
                case 77:
                case 78:
                case 79:
                case 80:
                case 81:
                case 82:
                case 83:
                case 84:
                case 85:
                case 86:
                case 87:
                case 88:
                case 89:
                case 90:
                    if (!z2) {
                        break;
                    } else {
                        this.ch = 97 + (this.ch - 65);
                        break;
                    }
            }
        }
    }

    private char[] parseEntityReference() throws IOException {
        int i2;
        int i3 = this.strpos;
        int ch = readCh();
        this.ch = ch;
        if (ch == 35) {
            int i4 = 0;
            this.ch = readCh();
            if ((this.ch >= 48 && this.ch <= 57) || this.ch == 120 || this.ch == 88) {
                if (this.ch >= 48 && this.ch <= 57) {
                    while (this.ch >= 48 && this.ch <= 57) {
                        i4 = ((i4 * 10) + this.ch) - 48;
                        this.ch = readCh();
                    }
                } else {
                    this.ch = readCh();
                    int lowerCase = Character.toLowerCase(this.ch);
                    while (true) {
                        char c2 = (char) lowerCase;
                        if ((c2 < '0' || c2 > '9') && (c2 < 'a' || c2 > 'f')) {
                            break;
                        }
                        if (c2 >= '0' && c2 <= '9') {
                            i2 = ((i4 * 16) + c2) - 48;
                        } else {
                            i2 = (((i4 * 16) + c2) - 97) + 10;
                        }
                        i4 = i2;
                        this.ch = readCh();
                        lowerCase = Character.toLowerCase(this.ch);
                    }
                }
                switch (this.ch) {
                    case 10:
                        this.ln++;
                        this.ch = readCh();
                        this.lfCount++;
                        break;
                    case 13:
                        this.ln++;
                        int ch2 = readCh();
                        this.ch = ch2;
                        if (ch2 == 10) {
                            this.ch = readCh();
                            this.crlfCount++;
                            break;
                        } else {
                            this.crCount++;
                            break;
                        }
                    case 59:
                        this.ch = readCh();
                        break;
                }
                return mapNumericReference(i4);
            }
            addString(35);
            if (!parseIdentifier(false)) {
                error("ident.expected");
                this.strpos = i3;
                return new char[]{'&', '#'};
            }
        } else if (!parseIdentifier(false)) {
            return new char[]{'&'};
        }
        boolean z2 = false;
        switch (this.ch) {
            case 10:
                this.ln++;
                this.ch = readCh();
                this.lfCount++;
                break;
            case 13:
                this.ln++;
                int ch3 = readCh();
                this.ch = ch3;
                if (ch3 == 10) {
                    this.ch = readCh();
                    this.crlfCount++;
                    break;
                } else {
                    this.crCount++;
                    break;
                }
            case 59:
                z2 = true;
                this.ch = readCh();
                break;
        }
        String string = getString(i3);
        Entity entity = this.dtd.getEntity(string);
        if (!this.strict && entity == null) {
            entity = this.dtd.getEntity(string.toLowerCase());
        }
        if (entity == null || !entity.isGeneral()) {
            if (string.length() == 0) {
                error("invalid.entref", string);
                return new char[0];
            }
            String str = "&" + string + (z2 ? ";" : "");
            char[] cArr = new char[str.length()];
            str.getChars(0, cArr.length, cArr, 0);
            return cArr;
        }
        return entity.getData();
    }

    private char[] mapNumericReference(int i2) {
        char[] chars;
        if (i2 >= 65535) {
            try {
                chars = Character.toChars(i2);
            } catch (IllegalArgumentException e2) {
                chars = new char[0];
            }
        } else {
            chars = new char[1];
            chars[0] = (i2 < 130 || i2 > 159) ? (char) i2 : cp1252Map[i2 - 130];
        }
        return chars;
    }

    void parseComment() throws IOException {
        while (true) {
            int i2 = this.ch;
            switch (i2) {
                case -1:
                    handleEOFInComment();
                    break;
                case 10:
                    this.ln++;
                    this.ch = readCh();
                    this.lfCount++;
                    addString(i2);
                case 13:
                    this.ln++;
                    int ch = readCh();
                    this.ch = ch;
                    if (ch == 10) {
                        this.ch = readCh();
                        this.crlfCount++;
                    } else {
                        this.crCount++;
                    }
                    i2 = 10;
                    addString(i2);
                case 45:
                    if (!this.strict && this.strpos != 0 && this.str[this.strpos - 1] == '-') {
                        int ch2 = readCh();
                        this.ch = ch2;
                        if (ch2 == 62) {
                            break;
                        } else if (this.ch == 33) {
                            int ch3 = readCh();
                            this.ch = ch3;
                            if (ch3 == 62) {
                                break;
                            } else {
                                addString(45);
                                addString(33);
                            }
                        } else {
                            addString(i2);
                        }
                    } else {
                        int ch4 = readCh();
                        this.ch = ch4;
                        if (ch4 == 45) {
                            this.ch = readCh();
                            if (!this.strict && this.ch != 62) {
                                if (this.ch == 33) {
                                    int ch5 = readCh();
                                    this.ch = ch5;
                                    if (ch5 == 62) {
                                        break;
                                    } else {
                                        addString(45);
                                        addString(33);
                                    }
                                } else {
                                    addString(45);
                                }
                            }
                        }
                        addString(i2);
                    }
                    break;
                case 62:
                    this.ch = readCh();
                    addString(i2);
                default:
                    this.ch = readCh();
                    addString(i2);
            }
            return;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:43:0x00bf A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void parseLiteral(boolean r7) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 521
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.swing.text.html.parser.Parser.parseLiteral(boolean):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:82:0x010e A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    java.lang.String parseAttributeValue(boolean r5) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 514
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.swing.text.html.parser.Parser.parseAttributeValue(boolean):java.lang.String");
    }

    void parseAttributeSpecificationList(Element element) throws IOException {
        String string;
        String value;
        AttributeList attributeByValue;
        while (true) {
            skipSpace();
            switch (this.ch) {
                case -1:
                case 47:
                case 60:
                case 62:
                    return;
                case 45:
                    int ch = readCh();
                    this.ch = ch;
                    if (ch == 45) {
                        this.ch = readCh();
                        parseComment();
                        this.strpos = 0;
                        break;
                    } else {
                        error("invalid.tagchar", LanguageTag.SEP, element.getName());
                        this.ch = readCh();
                        break;
                    }
                default:
                    if (parseIdentifier(true)) {
                        string = getString(0);
                        skipSpace();
                        if (this.ch == 61) {
                            this.ch = readCh();
                            skipSpace();
                            attributeByValue = element.getAttribute(string);
                            value = parseAttributeValue((attributeByValue == null || attributeByValue.type == 1 || attributeByValue.type == 11 || attributeByValue.type == 7) ? false : true);
                        } else {
                            value = string;
                            attributeByValue = element.getAttributeByValue(value);
                            if (attributeByValue == null) {
                                attributeByValue = element.getAttribute(string);
                                value = attributeByValue != null ? attributeByValue.getValue() : null;
                            }
                        }
                    } else if (!this.strict && this.ch == 44) {
                        this.ch = readCh();
                        break;
                    } else if (!this.strict && this.ch == 34) {
                        this.ch = readCh();
                        skipSpace();
                        if (parseIdentifier(true)) {
                            string = getString(0);
                            if (this.ch == 34) {
                                this.ch = readCh();
                            }
                            skipSpace();
                            if (this.ch == 61) {
                                this.ch = readCh();
                                skipSpace();
                                attributeByValue = element.getAttribute(string);
                                value = parseAttributeValue((attributeByValue == null || attributeByValue.type == 1 || attributeByValue.type == 11) ? false : true);
                            } else {
                                value = string;
                                attributeByValue = element.getAttributeByValue(value);
                                if (attributeByValue == null) {
                                    attributeByValue = element.getAttribute(string);
                                    if (attributeByValue != null) {
                                        value = attributeByValue.getValue();
                                    }
                                }
                            }
                        } else {
                            error("invalid.tagchar", new String(new char[]{(char) this.ch}), element.getName());
                            this.ch = readCh();
                            break;
                        }
                    } else if (!this.strict && this.attributes.isEmpty() && this.ch == 61) {
                        this.ch = readCh();
                        skipSpace();
                        string = element.getName();
                        attributeByValue = element.getAttribute(string);
                        value = parseAttributeValue((attributeByValue == null || attributeByValue.type == 1 || attributeByValue.type == 11) ? false : true);
                    } else {
                        if (!this.strict && this.ch == 61) {
                            this.ch = readCh();
                            skipSpace();
                            parseAttributeValue(true);
                            error("attvalerr");
                            return;
                        }
                        error("invalid.tagchar", new String(new char[]{(char) this.ch}), element.getName());
                        if (!this.strict) {
                            this.ch = readCh();
                            break;
                        } else {
                            return;
                        }
                    }
                    if (attributeByValue != null) {
                        string = attributeByValue.getName();
                    } else {
                        error("invalid.tagatt", string, element.getName());
                    }
                    if (this.attributes.isDefined(string)) {
                        error("multi.tagatt", string, element.getName());
                    }
                    if (value == null) {
                        value = (attributeByValue == null || attributeByValue.value == null) ? HTML.NULL_ATTRIBUTE_VALUE : attributeByValue.value;
                    } else if (attributeByValue != null && attributeByValue.values != null && !attributeByValue.values.contains(value)) {
                        error("invalid.tagattval", string, element.getName());
                    }
                    HTML.Attribute attributeKey = HTML.getAttributeKey(string);
                    if (attributeKey == null) {
                        this.attributes.addAttribute(string, value);
                        break;
                    } else {
                        this.attributes.addAttribute(attributeKey, value);
                        break;
                    }
                    break;
            }
        }
    }

    public String parseDTDMarkup() throws IOException {
        StringBuilder sb = new StringBuilder();
        this.ch = readCh();
        while (true) {
            switch (this.ch) {
                case -1:
                    error("invalid.markup");
                    return sb.toString();
                case 10:
                    this.ln++;
                    this.ch = readCh();
                    this.lfCount++;
                    break;
                case 13:
                    this.ln++;
                    int ch = readCh();
                    this.ch = ch;
                    if (ch == 10) {
                        this.ch = readCh();
                        this.crlfCount++;
                        break;
                    } else {
                        this.crCount++;
                        break;
                    }
                case 34:
                    this.ch = readCh();
                    break;
                case 62:
                    this.ch = readCh();
                    return sb.toString();
                default:
                    sb.append((char) (this.ch & 255));
                    this.ch = readCh();
                    break;
            }
        }
    }

    protected boolean parseMarkupDeclarations(StringBuffer stringBuffer) throws IOException {
        if (stringBuffer.length() == "DOCTYPE".length() && stringBuffer.toString().toUpperCase().equals("DOCTYPE")) {
            parseDTDMarkup();
            return true;
        }
        return false;
    }

    void parseInvalidTag() throws IOException {
        while (true) {
            skipSpace();
            switch (this.ch) {
                case -1:
                case 62:
                    this.ch = readCh();
                    return;
                case 60:
                    return;
                default:
                    this.ch = readCh();
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0324  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x032f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void parseTag() throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 1656
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.swing.text.html.parser.Parser.parseTag():void");
    }

    void parseScript() throws IOException {
        char[] cArr = new char[SCRIPT_END_TAG.length];
        boolean z2 = false;
        while (true) {
            int i2 = 0;
            while (!z2 && i2 < SCRIPT_END_TAG.length && (SCRIPT_END_TAG[i2] == this.ch || SCRIPT_END_TAG_UPPER_CASE[i2] == this.ch)) {
                cArr[i2] = (char) this.ch;
                this.ch = readCh();
                i2++;
            }
            if (i2 == SCRIPT_END_TAG.length) {
                return;
            }
            if (!z2 && i2 == 1 && cArr[0] == "<!--".charAt(0)) {
                while (i2 < "<!--".length() && "<!--".charAt(i2) == this.ch) {
                    cArr[i2] = (char) this.ch;
                    this.ch = readCh();
                    i2++;
                }
                if (i2 == "<!--".length()) {
                    z2 = true;
                }
            }
            if (z2) {
                while (i2 < "-->".length() && "-->".charAt(i2) == this.ch) {
                    cArr[i2] = (char) this.ch;
                    this.ch = readCh();
                    i2++;
                }
                if (i2 == "-->".length()) {
                    z2 = false;
                }
            }
            if (i2 > 0) {
                for (int i3 = 0; i3 < i2; i3++) {
                    addString(cArr[i3]);
                }
            } else {
                switch (this.ch) {
                    case -1:
                        error("eof.script");
                        return;
                    case 10:
                        this.ln++;
                        this.ch = readCh();
                        this.lfCount++;
                        addString(10);
                        break;
                    case 13:
                        this.ln++;
                        int ch = readCh();
                        this.ch = ch;
                        if (ch == 10) {
                            this.ch = readCh();
                            this.crlfCount++;
                        } else {
                            this.crCount++;
                        }
                        addString(10);
                        break;
                    default:
                        addString(this.ch);
                        this.ch = readCh();
                        break;
                }
            }
        }
    }

    void parseContent() throws IOException {
        Thread threadCurrentThread = Thread.currentThread();
        while (!threadCurrentThread.isInterrupted()) {
            int i2 = this.ch;
            this.currentBlockStartPos = this.currentPosition;
            if (this.recent == this.dtd.script) {
                parseScript();
                this.last = makeTag(this.dtd.getElement("comment"), true);
                String strTrim = new String(getChars(0)).trim();
                int length = "<!--".length() + "-->".length();
                if (strTrim.startsWith("<!--") && strTrim.endsWith("-->") && strTrim.length() >= length) {
                    strTrim = strTrim.substring("<!--".length(), strTrim.length() - "-->".length());
                }
                handleComment(strTrim.toCharArray());
                endTag(false);
                this.lastBlockStartPos = this.currentPosition;
            } else {
                switch (i2) {
                    case -1:
                        return;
                    case 9:
                    case 32:
                        this.ch = readCh();
                        if (this.stack != null && this.stack.pre) {
                            break;
                        } else {
                            if (this.textpos == 0) {
                                this.lastBlockStartPos = this.currentPosition;
                            }
                            if (!this.ignoreSpace) {
                                this.space = true;
                                break;
                            } else {
                                break;
                            }
                        }
                        break;
                    case 10:
                        this.ln++;
                        this.lfCount++;
                        this.ch = readCh();
                        if (this.stack != null && this.stack.pre) {
                            break;
                        } else {
                            if (this.textpos == 0) {
                                this.lastBlockStartPos = this.currentPosition;
                            }
                            if (!this.ignoreSpace) {
                                this.space = true;
                                break;
                            } else {
                                break;
                            }
                        }
                        break;
                    case 13:
                        this.ln++;
                        i2 = 10;
                        int ch = readCh();
                        this.ch = ch;
                        if (ch == 10) {
                            this.ch = readCh();
                            this.crlfCount++;
                        } else {
                            this.crCount++;
                        }
                        if (this.stack != null && this.stack.pre) {
                            break;
                        } else {
                            if (this.textpos == 0) {
                                this.lastBlockStartPos = this.currentPosition;
                            }
                            if (!this.ignoreSpace) {
                                this.space = true;
                                break;
                            } else {
                                break;
                            }
                        }
                        break;
                    case 38:
                        if (this.textpos == 0) {
                            if (!legalElementContext(this.dtd.pcdata)) {
                                error("unexpected.pcdata");
                            }
                            if (this.last.breaksFlow()) {
                                this.space = false;
                            }
                        }
                        char[] entityReference = parseEntityReference();
                        if (this.textpos + entityReference.length + 1 > this.text.length) {
                            char[] cArr = new char[Math.max(this.textpos + entityReference.length + 128, this.text.length * 2)];
                            System.arraycopy(this.text, 0, cArr, 0, this.text.length);
                            this.text = cArr;
                        }
                        if (this.space) {
                            this.space = false;
                            char[] cArr2 = this.text;
                            int i3 = this.textpos;
                            this.textpos = i3 + 1;
                            cArr2[i3] = ' ';
                        }
                        System.arraycopy(entityReference, 0, this.text, this.textpos, entityReference.length);
                        this.textpos += entityReference.length;
                        this.ignoreSpace = false;
                        continue;
                    case 47:
                        this.ch = readCh();
                        if (this.stack != null && this.stack.f12856net) {
                            endTag(false);
                            break;
                        } else if (this.textpos == 0) {
                            if (!legalElementContext(this.dtd.pcdata)) {
                                error("unexpected.pcdata");
                            }
                            if (this.last.breaksFlow()) {
                                this.space = false;
                                break;
                            }
                        }
                        break;
                    case 60:
                        parseTag();
                        this.lastBlockStartPos = this.currentPosition;
                        continue;
                    default:
                        if (this.textpos == 0) {
                            if (!legalElementContext(this.dtd.pcdata)) {
                                error("unexpected.pcdata");
                            }
                            if (this.last.breaksFlow()) {
                                this.space = false;
                            }
                        }
                        this.ch = readCh();
                        break;
                }
                if (this.textpos + 2 > this.text.length) {
                    char[] cArr3 = new char[this.text.length + 128];
                    System.arraycopy(this.text, 0, cArr3, 0, this.text.length);
                    this.text = cArr3;
                }
                if (this.space) {
                    if (this.textpos == 0) {
                        this.lastBlockStartPos--;
                    }
                    char[] cArr4 = this.text;
                    int i4 = this.textpos;
                    this.textpos = i4 + 1;
                    cArr4[i4] = ' ';
                    this.space = false;
                }
                char[] cArr5 = this.text;
                int i5 = this.textpos;
                this.textpos = i5 + 1;
                cArr5[i5] = (char) i2;
                this.ignoreSpace = false;
            }
        }
        threadCurrentThread.interrupt();
    }

    String getEndOfLineString() {
        if (this.crlfCount >= this.crCount) {
            if (this.lfCount >= this.crlfCount) {
                return "\n";
            }
            return "\r\n";
        }
        if (this.crCount > this.lfCount) {
            return LineSeparator.Macintosh;
        }
        return "\n";
    }

    public synchronized void parse(Reader reader) throws IOException {
        this.in = reader;
        this.ln = 1;
        this.seenHtml = false;
        this.seenHead = false;
        this.seenBody = false;
        this.crlfCount = 0;
        this.lfCount = 0;
        this.crCount = 0;
        try {
            try {
                try {
                    this.ch = readCh();
                    this.text = new char[1024];
                    this.str = new char[128];
                    parseContent();
                    while (this.stack != null) {
                        endTag(true);
                    }
                    reader.close();
                    while (this.stack != null) {
                        handleEndTag(this.stack.tag);
                        this.stack = this.stack.next;
                    }
                    this.text = null;
                    this.str = null;
                } catch (IOException e2) {
                    errorContext();
                    error("ioexception");
                    throw e2;
                } catch (ThreadDeath e3) {
                    errorContext();
                    error("terminated");
                    e3.printStackTrace();
                    throw e3;
                }
            } catch (Exception e4) {
                errorContext();
                error("exception", e4.getClass().getName(), e4.getMessage());
                e4.printStackTrace();
                while (this.stack != null) {
                    handleEndTag(this.stack.tag);
                    this.stack = this.stack.next;
                }
                this.text = null;
                this.str = null;
            }
        } catch (Throwable th) {
            while (this.stack != null) {
                handleEndTag(this.stack.tag);
                this.stack = this.stack.next;
            }
            this.text = null;
            this.str = null;
            throw th;
        }
    }

    private final int readCh() throws IOException {
        if (this.pos >= this.len) {
            try {
                this.len = this.in.read(this.buf);
                if (this.len <= 0) {
                    return -1;
                }
                this.pos = 0;
            } catch (InterruptedIOException e2) {
                throw e2;
            }
        }
        this.currentPosition++;
        char[] cArr = this.buf;
        int i2 = this.pos;
        this.pos = i2 + 1;
        return cArr[i2];
    }

    protected int getCurrentPos() {
        return this.currentPosition;
    }
}
