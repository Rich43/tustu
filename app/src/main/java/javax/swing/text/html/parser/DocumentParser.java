package javax.swing.text.html.parser;

import com.sun.glass.ui.Clipboard;
import java.io.IOException;
import java.io.Reader;
import javax.swing.text.ChangedCharSetException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

/* loaded from: rt.jar:javax/swing/text/html/parser/DocumentParser.class */
public class DocumentParser extends Parser {
    private int inbody;
    private int intitle;
    private int inhead;
    private int instyle;
    private int inscript;
    private boolean seentitle;
    private HTMLEditorKit.ParserCallback callback;
    private boolean ignoreCharSet;
    private static final boolean debugFlag = false;

    public DocumentParser(DTD dtd) {
        super(dtd);
        this.callback = null;
        this.ignoreCharSet = false;
    }

    public void parse(Reader reader, HTMLEditorKit.ParserCallback parserCallback, boolean z2) throws IOException {
        this.ignoreCharSet = z2;
        this.callback = parserCallback;
        parse(reader);
        parserCallback.handleEndOfLineString(getEndOfLineString());
    }

    @Override // javax.swing.text.html.parser.Parser
    protected void handleStartTag(TagElement tagElement) {
        Element element = tagElement.getElement();
        if (element == this.dtd.body) {
            this.inbody++;
        } else if (element != this.dtd.html) {
            if (element == this.dtd.head) {
                this.inhead++;
            } else if (element == this.dtd.title) {
                this.intitle++;
            } else if (element == this.dtd.style) {
                this.instyle++;
            } else if (element == this.dtd.script) {
                this.inscript++;
            }
        }
        if (tagElement.fictional()) {
            SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
            simpleAttributeSet.addAttribute(HTMLEditorKit.ParserCallback.IMPLIED, Boolean.TRUE);
            this.callback.handleStartTag(tagElement.getHTMLTag(), simpleAttributeSet, getBlockStartPosition());
        } else {
            this.callback.handleStartTag(tagElement.getHTMLTag(), getAttributes(), getBlockStartPosition());
            flushAttributes();
        }
    }

    @Override // javax.swing.text.html.parser.Parser
    protected void handleComment(char[] cArr) {
        this.callback.handleComment(cArr, getBlockStartPosition());
    }

    @Override // javax.swing.text.html.parser.Parser
    protected void handleEmptyTag(TagElement tagElement) throws ChangedCharSetException {
        SimpleAttributeSet attributes;
        String str;
        Element element = tagElement.getElement();
        if (element == this.dtd.meta && !this.ignoreCharSet && (attributes = getAttributes()) != null && (str = (String) attributes.getAttribute(HTML.Attribute.CONTENT)) != null) {
            if ("content-type".equalsIgnoreCase((String) attributes.getAttribute(HTML.Attribute.HTTPEQUIV))) {
                if (!str.equalsIgnoreCase(Clipboard.HTML_TYPE) && !str.equalsIgnoreCase(Clipboard.TEXT_TYPE)) {
                    throw new ChangedCharSetException(str, false);
                }
            } else if ("charset".equalsIgnoreCase((String) attributes.getAttribute(HTML.Attribute.HTTPEQUIV))) {
                throw new ChangedCharSetException(str, true);
            }
        }
        if (this.inbody != 0 || element == this.dtd.meta || element == this.dtd.base || element == this.dtd.isindex || element == this.dtd.style || element == this.dtd.link) {
            if (tagElement.fictional()) {
                SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
                simpleAttributeSet.addAttribute(HTMLEditorKit.ParserCallback.IMPLIED, Boolean.TRUE);
                this.callback.handleSimpleTag(tagElement.getHTMLTag(), simpleAttributeSet, getBlockStartPosition());
            } else {
                this.callback.handleSimpleTag(tagElement.getHTMLTag(), getAttributes(), getBlockStartPosition());
                flushAttributes();
            }
        }
    }

    @Override // javax.swing.text.html.parser.Parser
    protected void handleEndTag(TagElement tagElement) {
        Element element = tagElement.getElement();
        if (element == this.dtd.body) {
            this.inbody--;
        } else if (element == this.dtd.title) {
            this.intitle--;
            this.seentitle = true;
        } else if (element == this.dtd.head) {
            this.inhead--;
        } else if (element == this.dtd.style) {
            this.instyle--;
        } else if (element == this.dtd.script) {
            this.inscript--;
        }
        this.callback.handleEndTag(tagElement.getHTMLTag(), getBlockStartPosition());
    }

    @Override // javax.swing.text.html.parser.Parser
    protected void handleText(char[] cArr) {
        if (cArr != null) {
            if (this.inscript != 0) {
                this.callback.handleComment(cArr, getBlockStartPosition());
            } else if (this.inbody != 0 || this.instyle != 0 || (this.intitle != 0 && !this.seentitle)) {
                this.callback.handleText(cArr, getBlockStartPosition());
            }
        }
    }

    @Override // javax.swing.text.html.parser.Parser
    protected void handleError(int i2, String str) {
        this.callback.handleError(str, getCurrentPos());
    }

    private void debug(String str) {
        System.out.println(str);
    }
}
