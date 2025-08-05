package com.sun.org.apache.xml.internal.serializer;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.serializer.utils.Utils;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;
import javafx.scene.control.ColorPicker;
import javax.xml.transform.Result;
import jdk.xml.internal.JdkXmlUtils;
import org.icepdf.core.util.PdfOps;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import sun.java2d.marlin.MarlinConst;
import sun.security.ssl.Record;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/ToHTMLStream.class */
public final class ToHTMLStream extends ToStream {
    private static final CharInfo m_htmlcharInfo = CharInfo.getCharInfoInternal(CharInfo.HTML_ENTITIES_RESOURCE, "html");
    static final Trie m_elementFlags = new Trie();
    private static final ElemDesc m_dummy;
    protected boolean m_inDTD = false;
    private boolean m_inBlockElem = false;
    private boolean m_specialEscapeURLs = true;
    private boolean m_omitMetaTag = false;
    private Trie m_htmlInfo = new Trie(m_elementFlags);

    static {
        initTagReference(m_elementFlags);
        m_dummy = new ElemDesc(8);
    }

    static void initTagReference(Trie m_elementFlags2) {
        m_elementFlags2.put("BASEFONT", new ElemDesc(2));
        m_elementFlags2.put("FRAME", new ElemDesc(10));
        m_elementFlags2.put("FRAMESET", new ElemDesc(8));
        m_elementFlags2.put("NOFRAMES", new ElemDesc(8));
        m_elementFlags2.put("ISINDEX", new ElemDesc(10));
        m_elementFlags2.put("APPLET", new ElemDesc(2097152));
        m_elementFlags2.put("CENTER", new ElemDesc(8));
        m_elementFlags2.put("DIR", new ElemDesc(8));
        m_elementFlags2.put("MENU", new ElemDesc(8));
        m_elementFlags2.put("TT", new ElemDesc(4096));
        m_elementFlags2.put("I", new ElemDesc(4096));
        m_elementFlags2.put(PdfOps.B_TOKEN, new ElemDesc(4096));
        m_elementFlags2.put("BIG", new ElemDesc(4096));
        m_elementFlags2.put("SMALL", new ElemDesc(4096));
        m_elementFlags2.put("EM", new ElemDesc(8192));
        m_elementFlags2.put("STRONG", new ElemDesc(8192));
        m_elementFlags2.put("DFN", new ElemDesc(8192));
        m_elementFlags2.put("CODE", new ElemDesc(8192));
        m_elementFlags2.put("SAMP", new ElemDesc(8192));
        m_elementFlags2.put("KBD", new ElemDesc(8192));
        m_elementFlags2.put("VAR", new ElemDesc(8192));
        m_elementFlags2.put("CITE", new ElemDesc(8192));
        m_elementFlags2.put("ABBR", new ElemDesc(8192));
        m_elementFlags2.put("ACRONYM", new ElemDesc(8192));
        m_elementFlags2.put("SUP", new ElemDesc(MarlinConst.INITIAL_EDGES_CAPACITY));
        m_elementFlags2.put("SUB", new ElemDesc(MarlinConst.INITIAL_EDGES_CAPACITY));
        m_elementFlags2.put("SPAN", new ElemDesc(MarlinConst.INITIAL_EDGES_CAPACITY));
        m_elementFlags2.put("BDO", new ElemDesc(MarlinConst.INITIAL_EDGES_CAPACITY));
        m_elementFlags2.put("BR", new ElemDesc(98314));
        m_elementFlags2.put("BODY", new ElemDesc(8));
        m_elementFlags2.put("ADDRESS", new ElemDesc(56));
        m_elementFlags2.put("DIV", new ElemDesc(56));
        m_elementFlags2.put("A", new ElemDesc(32768));
        m_elementFlags2.put("MAP", new ElemDesc(98312));
        m_elementFlags2.put("AREA", new ElemDesc(10));
        m_elementFlags2.put("LINK", new ElemDesc(131082));
        m_elementFlags2.put("IMG", new ElemDesc(2195458));
        m_elementFlags2.put("OBJECT", new ElemDesc(2326528));
        m_elementFlags2.put("PARAM", new ElemDesc(2));
        m_elementFlags2.put("HR", new ElemDesc(58));
        m_elementFlags2.put(Constants._TAG_P, new ElemDesc(56));
        m_elementFlags2.put("H1", new ElemDesc(262152));
        m_elementFlags2.put("H2", new ElemDesc(262152));
        m_elementFlags2.put("H3", new ElemDesc(262152));
        m_elementFlags2.put("H4", new ElemDesc(262152));
        m_elementFlags2.put("H5", new ElemDesc(262152));
        m_elementFlags2.put("H6", new ElemDesc(262152));
        m_elementFlags2.put("PRE", new ElemDesc(1048584));
        m_elementFlags2.put("Q", new ElemDesc(MarlinConst.INITIAL_EDGES_CAPACITY));
        m_elementFlags2.put("BLOCKQUOTE", new ElemDesc(56));
        m_elementFlags2.put("INS", new ElemDesc(0));
        m_elementFlags2.put("DEL", new ElemDesc(0));
        m_elementFlags2.put("DL", new ElemDesc(56));
        m_elementFlags2.put("DT", new ElemDesc(8));
        m_elementFlags2.put("DD", new ElemDesc(8));
        m_elementFlags2.put("OL", new ElemDesc(524296));
        m_elementFlags2.put("UL", new ElemDesc(524296));
        m_elementFlags2.put("LI", new ElemDesc(8));
        m_elementFlags2.put("FORM", new ElemDesc(8));
        m_elementFlags2.put("LABEL", new ElemDesc(16384));
        m_elementFlags2.put("INPUT", new ElemDesc(18434));
        m_elementFlags2.put("SELECT", new ElemDesc(Record.maxFragmentSize));
        m_elementFlags2.put("OPTGROUP", new ElemDesc(0));
        m_elementFlags2.put("OPTION", new ElemDesc(0));
        m_elementFlags2.put("TEXTAREA", new ElemDesc(Record.maxFragmentSize));
        m_elementFlags2.put("FIELDSET", new ElemDesc(24));
        m_elementFlags2.put("LEGEND", new ElemDesc(0));
        m_elementFlags2.put("BUTTON", new ElemDesc(Record.maxFragmentSize));
        m_elementFlags2.put("TABLE", new ElemDesc(56));
        m_elementFlags2.put("CAPTION", new ElemDesc(8));
        m_elementFlags2.put("THEAD", new ElemDesc(8));
        m_elementFlags2.put("TFOOT", new ElemDesc(8));
        m_elementFlags2.put("TBODY", new ElemDesc(8));
        m_elementFlags2.put("COLGROUP", new ElemDesc(8));
        m_elementFlags2.put("COL", new ElemDesc(10));
        m_elementFlags2.put("TR", new ElemDesc(8));
        m_elementFlags2.put("TH", new ElemDesc(0));
        m_elementFlags2.put(PdfOps.TD_TOKEN, new ElemDesc(0));
        m_elementFlags2.put("HEAD", new ElemDesc(4194312));
        m_elementFlags2.put("TITLE", new ElemDesc(8));
        m_elementFlags2.put("BASE", new ElemDesc(10));
        m_elementFlags2.put("META", new ElemDesc(131082));
        m_elementFlags2.put("STYLE", new ElemDesc(131336));
        m_elementFlags2.put("SCRIPT", new ElemDesc(229632));
        m_elementFlags2.put("NOSCRIPT", new ElemDesc(56));
        m_elementFlags2.put("HTML", new ElemDesc(8));
        m_elementFlags2.put("FONT", new ElemDesc(4096));
        m_elementFlags2.put(PdfOps.S_TOKEN, new ElemDesc(4096));
        m_elementFlags2.put("STRIKE", new ElemDesc(4096));
        m_elementFlags2.put("U", new ElemDesc(4096));
        m_elementFlags2.put("NOBR", new ElemDesc(4096));
        m_elementFlags2.put("IFRAME", new ElemDesc(56));
        m_elementFlags2.put("LAYER", new ElemDesc(56));
        m_elementFlags2.put("ILAYER", new ElemDesc(56));
        ElemDesc elemDesc = (ElemDesc) m_elementFlags2.get("a");
        elemDesc.setAttr("HREF", 2);
        elemDesc.setAttr("NAME", 2);
        ElemDesc elemDesc2 = (ElemDesc) m_elementFlags2.get("area");
        elemDesc2.setAttr("HREF", 2);
        elemDesc2.setAttr("NOHREF", 4);
        ((ElemDesc) m_elementFlags2.get("base")).setAttr("HREF", 2);
        ((ElemDesc) m_elementFlags2.get(ColorPicker.STYLE_CLASS_BUTTON)).setAttr("DISABLED", 4);
        ((ElemDesc) m_elementFlags2.get("blockquote")).setAttr("CITE", 2);
        ((ElemDesc) m_elementFlags2.get("del")).setAttr("CITE", 2);
        ((ElemDesc) m_elementFlags2.get("dir")).setAttr("COMPACT", 4);
        ElemDesc elemDesc3 = (ElemDesc) m_elementFlags2.get("div");
        elemDesc3.setAttr("SRC", 2);
        elemDesc3.setAttr("NOWRAP", 4);
        ((ElemDesc) m_elementFlags2.get("dl")).setAttr("COMPACT", 4);
        ((ElemDesc) m_elementFlags2.get("form")).setAttr("ACTION", 2);
        ElemDesc elemDesc4 = (ElemDesc) m_elementFlags2.get("frame");
        elemDesc4.setAttr("SRC", 2);
        elemDesc4.setAttr("LONGDESC", 2);
        elemDesc4.setAttr("NORESIZE", 4);
        ((ElemDesc) m_elementFlags2.get("head")).setAttr("PROFILE", 2);
        ((ElemDesc) m_elementFlags2.get("hr")).setAttr("NOSHADE", 4);
        ElemDesc elemDesc5 = (ElemDesc) m_elementFlags2.get("iframe");
        elemDesc5.setAttr("SRC", 2);
        elemDesc5.setAttr("LONGDESC", 2);
        ((ElemDesc) m_elementFlags2.get("ilayer")).setAttr("SRC", 2);
        ElemDesc elemDesc6 = (ElemDesc) m_elementFlags2.get("img");
        elemDesc6.setAttr("SRC", 2);
        elemDesc6.setAttr("LONGDESC", 2);
        elemDesc6.setAttr("USEMAP", 2);
        elemDesc6.setAttr("ISMAP", 4);
        ElemDesc elemDesc7 = (ElemDesc) m_elementFlags2.get("input");
        elemDesc7.setAttr("SRC", 2);
        elemDesc7.setAttr("USEMAP", 2);
        elemDesc7.setAttr("CHECKED", 4);
        elemDesc7.setAttr("DISABLED", 4);
        elemDesc7.setAttr("ISMAP", 4);
        elemDesc7.setAttr("READONLY", 4);
        ((ElemDesc) m_elementFlags2.get("ins")).setAttr("CITE", 2);
        ((ElemDesc) m_elementFlags2.get("layer")).setAttr("SRC", 2);
        ((ElemDesc) m_elementFlags2.get("link")).setAttr("HREF", 2);
        ((ElemDesc) m_elementFlags2.get("menu")).setAttr("COMPACT", 4);
        ElemDesc elemDesc8 = (ElemDesc) m_elementFlags2.get("object");
        elemDesc8.setAttr("CLASSID", 2);
        elemDesc8.setAttr("CODEBASE", 2);
        elemDesc8.setAttr("DATA", 2);
        elemDesc8.setAttr("ARCHIVE", 2);
        elemDesc8.setAttr("USEMAP", 2);
        elemDesc8.setAttr("DECLARE", 4);
        ((ElemDesc) m_elementFlags2.get("ol")).setAttr("COMPACT", 4);
        ((ElemDesc) m_elementFlags2.get("optgroup")).setAttr("DISABLED", 4);
        ElemDesc elemDesc9 = (ElemDesc) m_elementFlags2.get("option");
        elemDesc9.setAttr("SELECTED", 4);
        elemDesc9.setAttr("DISABLED", 4);
        ((ElemDesc) m_elementFlags2.get(PdfOps.q_TOKEN)).setAttr("CITE", 2);
        ElemDesc elemDesc10 = (ElemDesc) m_elementFlags2.get("script");
        elemDesc10.setAttr("SRC", 2);
        elemDesc10.setAttr("FOR", 2);
        elemDesc10.setAttr("DEFER", 4);
        ElemDesc elemDesc11 = (ElemDesc) m_elementFlags2.get(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_SELECT);
        elemDesc11.setAttr("DISABLED", 4);
        elemDesc11.setAttr("MULTIPLE", 4);
        ((ElemDesc) m_elementFlags2.get("table")).setAttr("NOWRAP", 4);
        ((ElemDesc) m_elementFlags2.get("td")).setAttr("NOWRAP", 4);
        ElemDesc elemDesc12 = (ElemDesc) m_elementFlags2.get("textarea");
        elemDesc12.setAttr("DISABLED", 4);
        elemDesc12.setAttr("READONLY", 4);
        ((ElemDesc) m_elementFlags2.get("th")).setAttr("NOWRAP", 4);
        ((ElemDesc) m_elementFlags2.get("tr")).setAttr("NOWRAP", 4);
        ((ElemDesc) m_elementFlags2.get("ul")).setAttr("COMPACT", 4);
    }

    public void setSpecialEscapeURLs(boolean bool) {
        this.m_specialEscapeURLs = bool;
    }

    public void setOmitMetaTag(boolean bool) {
        this.m_omitMetaTag = bool;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToStream, com.sun.org.apache.xml.internal.serializer.Serializer
    public void setOutputFormat(Properties format) {
        this.m_specialEscapeURLs = OutputPropertyUtils.getBooleanProperty(OutputPropertiesFactory.S_USE_URL_ESCAPING, format);
        this.m_omitMetaTag = OutputPropertyUtils.getBooleanProperty(OutputPropertiesFactory.S_OMIT_META_TAG, format);
        super.setOutputFormat(format);
    }

    private final boolean getSpecialEscapeURLs() {
        return this.m_specialEscapeURLs;
    }

    private final boolean getOmitMetaTag() {
        return this.m_omitMetaTag;
    }

    public static final ElemDesc getElemDesc(String name) {
        Object obj = m_elementFlags.get(name);
        if (null != obj) {
            return (ElemDesc) obj;
        }
        return m_dummy;
    }

    private ElemDesc getElemDesc2(String name) {
        Object obj = this.m_htmlInfo.get2(name);
        if (null != obj) {
            return (ElemDesc) obj;
        }
        return m_dummy;
    }

    public ToHTMLStream() {
        this.m_charInfo = m_htmlcharInfo;
        this.m_prefixMap = new NamespaceMappings();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase
    protected void startDocumentInternal() throws SAXException {
        super.startDocumentInternal();
        this.m_needToCallStartDocument = false;
        this.m_needToOutputDocTypeDecl = true;
        this.m_startNewLine = false;
        setOmitXMLDeclaration(true);
        if (true == this.m_needToOutputDocTypeDecl) {
            String doctypeSystem = getDoctypeSystem();
            String doctypePublic = getDoctypePublic();
            if (null != doctypeSystem || null != doctypePublic) {
                Writer writer = this.m_writer;
                try {
                    writer.write("<!DOCTYPE html");
                    writer.write(JdkXmlUtils.getDTDExternalDecl(doctypePublic, doctypeSystem));
                    writer.write(62);
                    outputLineSep();
                } catch (IOException e2) {
                    throw new SAXException(e2);
                }
            }
        }
        this.m_needToOutputDocTypeDecl = false;
    }

    @Override // org.xml.sax.ContentHandler
    public final void endDocument() throws SAXException {
        flushPending();
        if (this.m_doIndent && !this.m_isprevtext) {
            try {
                outputLineSep();
            } catch (IOException e2) {
                throw new SAXException(e2);
            }
        }
        flushWriter();
        if (this.m_tracer != null) {
            super.fireEndDoc();
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToStream, org.xml.sax.ContentHandler
    public void startElement(String namespaceURI, String localName, String name, Attributes atts) throws SAXException {
        ElemContext elemContext = this.m_elemContext;
        if (elemContext.m_startTagOpen) {
            closeStartTag();
            elemContext.m_startTagOpen = false;
        } else if (this.m_cdataTagOpen) {
            closeCDATA();
            this.m_cdataTagOpen = false;
        } else if (this.m_needToCallStartDocument) {
            startDocumentInternal();
            this.m_needToCallStartDocument = false;
        }
        if (null != namespaceURI && namespaceURI.length() > 0) {
            super.startElement(namespaceURI, localName, name, atts);
            return;
        }
        try {
            ElemDesc elemDesc = getElemDesc2(name);
            int elemFlags = elemDesc.getFlags();
            if (this.m_doIndent) {
                boolean isBlockElement = (elemFlags & 8) != 0;
                if (this.m_ispreserve) {
                    this.m_ispreserve = false;
                } else if (null != elemContext.m_elementName && (!this.m_inBlockElem || isBlockElement)) {
                    this.m_startNewLine = true;
                    indent();
                }
                this.m_inBlockElem = !isBlockElement;
            }
            if (atts != null) {
                addAttributes(atts);
            }
            this.m_isprevtext = false;
            Writer writer = this.m_writer;
            writer.write(60);
            writer.write(name);
            if (this.m_tracer != null) {
                firePseudoAttributes();
            }
            if ((elemFlags & 2) != 0) {
                this.m_elemContext = elemContext.push();
                this.m_elemContext.m_elementName = name;
                this.m_elemContext.m_elementDesc = elemDesc;
                return;
            }
            ElemContext elemContext2 = elemContext.push(namespaceURI, localName, name);
            this.m_elemContext = elemContext2;
            elemContext2.m_elementDesc = elemDesc;
            elemContext2.m_isRaw = (elemFlags & 256) != 0;
            if ((elemFlags & 4194304) != 0) {
                closeStartTag();
                elemContext2.m_startTagOpen = false;
                if (!this.m_omitMetaTag) {
                    if (this.m_doIndent) {
                        indent();
                    }
                    writer.write("<META http-equiv=\"Content-Type\" content=\"text/html; charset=");
                    String encoding = getEncoding();
                    String encode = Encodings.getMimeEncoding(encoding);
                    writer.write(encode);
                    writer.write("\">");
                }
            }
        } catch (IOException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToStream, org.xml.sax.ContentHandler
    public final void endElement(String namespaceURI, String localName, String name) throws SAXException {
        if (this.m_cdataTagOpen) {
            closeCDATA();
        }
        if (null != namespaceURI && namespaceURI.length() > 0) {
            super.endElement(namespaceURI, localName, name);
            return;
        }
        try {
            ElemContext elemContext = this.m_elemContext;
            ElemDesc elemDesc = elemContext.m_elementDesc;
            int elemFlags = elemDesc.getFlags();
            boolean elemEmpty = (elemFlags & 2) != 0;
            if (this.m_doIndent) {
                boolean isBlockElement = (elemFlags & 8) != 0;
                boolean shouldIndent = false;
                if (this.m_ispreserve) {
                    this.m_ispreserve = false;
                } else if (this.m_doIndent && (!this.m_inBlockElem || isBlockElement)) {
                    this.m_startNewLine = true;
                    shouldIndent = true;
                }
                if (!elemContext.m_startTagOpen && shouldIndent) {
                    indent(elemContext.m_currentElemDepth - 1);
                }
                this.m_inBlockElem = !isBlockElement;
            }
            Writer writer = this.m_writer;
            if (!elemContext.m_startTagOpen) {
                writer.write("</");
                writer.write(name);
                writer.write(62);
            } else {
                if (this.m_tracer != null) {
                    super.fireStartElem(name);
                }
                int nAttrs = this.m_attributes.getLength();
                if (nAttrs > 0) {
                    processAttributes(this.m_writer, nAttrs);
                    this.m_attributes.clear();
                }
                if (!elemEmpty) {
                    writer.write("></");
                    writer.write(name);
                    writer.write(62);
                } else {
                    writer.write(62);
                }
            }
            if ((elemFlags & 2097152) != 0) {
                this.m_ispreserve = true;
            }
            this.m_isprevtext = false;
            if (this.m_tracer != null) {
                super.fireEndElem(name);
            }
            if (elemEmpty) {
                this.m_elemContext = elemContext.m_prev;
                return;
            }
            if (!elemContext.m_startTagOpen && this.m_doIndent && !this.m_preserves.isEmpty()) {
                this.m_preserves.pop();
            }
            this.m_elemContext = elemContext.m_prev;
        } catch (IOException e2) {
            throw new SAXException(e2);
        }
    }

    protected void processAttribute(Writer writer, String name, String value, ElemDesc elemDesc) throws SAXException, IOException {
        writer.write(32);
        if ((value.length() == 0 || value.equalsIgnoreCase(name)) && elemDesc != null && elemDesc.isAttrFlagSet(name, 4)) {
            writer.write(name);
            return;
        }
        writer.write(name);
        writer.write("=\"");
        if (elemDesc != null && elemDesc.isAttrFlagSet(name, 2)) {
            writeAttrURI(writer, value, this.m_specialEscapeURLs);
        } else {
            writeAttrString(writer, value, getEncoding());
        }
        writer.write(34);
    }

    private boolean isASCIIDigit(char c2) {
        return c2 >= '0' && c2 <= '9';
    }

    private static String makeHHString(int i2) {
        String s2 = Integer.toHexString(i2).toUpperCase();
        if (s2.length() == 1) {
            s2 = "0" + s2;
        }
        return s2;
    }

    private boolean isHHSign(String str) {
        boolean sign = true;
        try {
        } catch (NumberFormatException e2) {
            sign = false;
        }
        return sign;
    }

    public void writeAttrURI(Writer writer, String string, boolean doURLEscaping) throws IOException {
        int end = string.length();
        if (end > this.m_attrBuff.length) {
            this.m_attrBuff = new char[(end * 2) + 1];
        }
        string.getChars(0, end, this.m_attrBuff, 0);
        char[] chars = this.m_attrBuff;
        int cleanStart = 0;
        int cleanLength = 0;
        char ch = 0;
        int i2 = 0;
        while (i2 < end) {
            ch = chars[i2];
            if (ch < ' ' || ch > '~') {
                if (cleanLength > 0) {
                    writer.write(chars, cleanStart, cleanLength);
                    cleanLength = 0;
                }
                if (doURLEscaping) {
                    if (ch <= 127) {
                        writer.write(37);
                        writer.write(makeHHString(ch));
                    } else if (ch <= 2047) {
                        int high = (ch >> 6) | 192;
                        int low = (ch & '?') | 128;
                        writer.write(37);
                        writer.write(makeHHString(high));
                        writer.write(37);
                        writer.write(makeHHString(low));
                    } else if (Encodings.isHighUTF16Surrogate(ch)) {
                        int highSurrogate = ch & 1023;
                        int wwww = (highSurrogate & 960) >> 6;
                        int uuuuu = wwww + 1;
                        int zzzz = (highSurrogate & 60) >> 2;
                        int yyyyyy = ((highSurrogate & 3) << 4) & 48;
                        i2++;
                        ch = chars[i2];
                        int lowSurrogate = ch & 1023;
                        int yyyyyy2 = yyyyyy | ((lowSurrogate & 960) >> 6);
                        int xxxxxx = lowSurrogate & 63;
                        int byte1 = 240 | (uuuuu >> 2);
                        int byte2 = 128 | (((uuuuu & 3) << 4) & 48) | zzzz;
                        int byte3 = 128 | yyyyyy2;
                        int byte4 = 128 | xxxxxx;
                        writer.write(37);
                        writer.write(makeHHString(byte1));
                        writer.write(37);
                        writer.write(makeHHString(byte2));
                        writer.write(37);
                        writer.write(makeHHString(byte3));
                        writer.write(37);
                        writer.write(makeHHString(byte4));
                    } else {
                        int high2 = (ch >> '\f') | 224;
                        int middle = ((ch & 4032) >> 6) | 128;
                        int low2 = (ch & '?') | 128;
                        writer.write(37);
                        writer.write(makeHHString(high2));
                        writer.write(37);
                        writer.write(makeHHString(middle));
                        writer.write(37);
                        writer.write(makeHHString(low2));
                    }
                } else if (escapingNotNeeded(ch)) {
                    writer.write(ch);
                } else {
                    writer.write("&#");
                    writer.write(Integer.toString(ch));
                    writer.write(59);
                }
                cleanStart = i2 + 1;
            } else if (ch == '\"') {
                if (cleanLength > 0) {
                    writer.write(chars, cleanStart, cleanLength);
                    cleanLength = 0;
                }
                if (doURLEscaping) {
                    writer.write("%22");
                } else {
                    writer.write(SerializerConstants.ENTITY_QUOT);
                }
                cleanStart = i2 + 1;
            } else if (ch == '&') {
                if (cleanLength > 0) {
                    writer.write(chars, cleanStart, cleanLength);
                    cleanLength = 0;
                }
                writer.write(SerializerConstants.ENTITY_AMP);
                cleanStart = i2 + 1;
            } else {
                cleanLength++;
            }
            i2++;
        }
        if (cleanLength > 1) {
            if (cleanStart == 0) {
                writer.write(string);
                return;
            } else {
                writer.write(chars, cleanStart, cleanLength);
                return;
            }
        }
        if (cleanLength == 1) {
            writer.write(ch);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToStream
    public void writeAttrString(Writer writer, String string, String encoding) throws SAXException, IOException {
        int end = string.length();
        if (end > this.m_attrBuff.length) {
            this.m_attrBuff = new char[(end * 2) + 1];
        }
        string.getChars(0, end, this.m_attrBuff, 0);
        char[] chars = this.m_attrBuff;
        int cleanStart = 0;
        int cleanLength = 0;
        char ch = 0;
        int i2 = 0;
        while (i2 < end) {
            ch = chars[i2];
            if (escapingNotNeeded(ch) && !this.m_charInfo.isSpecialAttrChar(ch)) {
                cleanLength++;
            } else if ('<' == ch || '>' == ch) {
                cleanLength++;
            } else if ('&' == ch && i2 + 1 < end && '{' == chars[i2 + 1]) {
                cleanLength++;
            } else {
                if (cleanLength > 0) {
                    writer.write(chars, cleanStart, cleanLength);
                    cleanLength = 0;
                }
                int pos = accumDefaultEntity(writer, ch, i2, chars, end, false, true);
                if (i2 != pos) {
                    i2 = pos - 1;
                } else {
                    if ((Encodings.isHighUTF16Surrogate(ch) || Encodings.isLowUTF16Surrogate(ch)) && writeUTF16Surrogate(ch, chars, i2, end) >= 0 && Encodings.isHighUTF16Surrogate(ch)) {
                        i2++;
                    }
                    String outputStringForChar = this.m_charInfo.getOutputStringForChar(ch);
                    if (null != outputStringForChar) {
                        writer.write(outputStringForChar);
                    } else if (escapingNotNeeded(ch)) {
                        writer.write(ch);
                    } else {
                        writer.write("&#");
                        writer.write(Integer.toString(ch));
                        writer.write(59);
                    }
                }
                cleanStart = i2 + 1;
            }
            i2++;
        }
        if (cleanLength > 1) {
            if (cleanStart == 0) {
                writer.write(string);
                return;
            } else {
                writer.write(chars, cleanStart, cleanLength);
                return;
            }
        }
        if (cleanLength == 1) {
            writer.write(ch);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToStream, org.xml.sax.ContentHandler
    public final void characters(char[] chars, int start, int length) throws SAXException {
        if (this.m_elemContext.m_isRaw) {
            try {
                if (this.m_elemContext.m_startTagOpen) {
                    closeStartTag();
                    this.m_elemContext.m_startTagOpen = false;
                }
                this.m_ispreserve = true;
                writeNormalizedChars(chars, start, length, false, this.m_lineSepUse);
                if (this.m_tracer != null) {
                    super.fireCharEvent(chars, start, length);
                    return;
                }
                return;
            } catch (IOException ioe) {
                throw new SAXException(Utils.messages.createMessage("ER_OIERROR", null), ioe);
            }
        }
        super.characters(chars, start, length);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToStream
    public final void cdata(char[] ch, int start, int length) throws SAXException {
        if (null != this.m_elemContext.m_elementName && (this.m_elemContext.m_elementName.equalsIgnoreCase("SCRIPT") || this.m_elemContext.m_elementName.equalsIgnoreCase("STYLE"))) {
            try {
                if (this.m_elemContext.m_startTagOpen) {
                    closeStartTag();
                    this.m_elemContext.m_startTagOpen = false;
                }
                this.m_ispreserve = true;
                if (shouldIndent()) {
                    indent();
                }
                writeNormalizedChars(ch, start, length, true, this.m_lineSepUse);
                return;
            } catch (IOException ioe) {
                throw new SAXException(Utils.messages.createMessage("ER_OIERROR", null), ioe);
            }
        }
        super.cdata(ch, start, length);
    }

    @Override // org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws SAXException {
        flushPending();
        if (target.equals(Result.PI_DISABLE_OUTPUT_ESCAPING)) {
            startNonEscaping();
        } else if (target.equals(Result.PI_ENABLE_OUTPUT_ESCAPING)) {
            endNonEscaping();
        } else {
            try {
                if (this.m_elemContext.m_startTagOpen) {
                    closeStartTag();
                    this.m_elemContext.m_startTagOpen = false;
                } else if (this.m_needToCallStartDocument) {
                    startDocumentInternal();
                }
                if (shouldIndent()) {
                    indent();
                }
                Writer writer = this.m_writer;
                writer.write("<?");
                writer.write(target);
                if (data.length() > 0 && !Character.isSpaceChar(data.charAt(0))) {
                    writer.write(32);
                }
                writer.write(data);
                writer.write(62);
                if (this.m_elemContext.m_currentElemDepth <= 0) {
                    outputLineSep();
                }
                this.m_startNewLine = true;
            } catch (IOException e2) {
                throw new SAXException(e2);
            }
        }
        if (this.m_tracer != null) {
            super.fireEscapingEvent(target, data);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public final void entityReference(String name) throws SAXException {
        try {
            Writer writer = this.m_writer;
            writer.write(38);
            writer.write(name);
            writer.write(59);
        } catch (IOException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToStream, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public final void endElement(String elemName) throws SAXException {
        endElement(null, null, elemName);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToStream
    public void processAttributes(Writer writer, int nAttrs) throws SAXException, IOException {
        for (int i2 = 0; i2 < nAttrs; i2++) {
            processAttribute(writer, this.m_attributes.getQName(i2), this.m_attributes.getValue(i2), this.m_elemContext.m_elementDesc);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToStream
    protected void closeStartTag() throws SAXException {
        try {
            if (this.m_tracer != null) {
                super.fireStartElem(this.m_elemContext.m_elementName);
            }
            int nAttrs = this.m_attributes.getLength();
            if (nAttrs > 0) {
                processAttributes(this.m_writer, nAttrs);
                this.m_attributes.clear();
            }
            this.m_writer.write(62);
            if (this.m_StringOfCDATASections != null) {
                this.m_elemContext.m_isCdataSection = isCdataSection();
            }
            if (this.m_doIndent) {
                this.m_isprevtext = false;
                this.m_preserves.push(this.m_ispreserve);
            }
        } catch (IOException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void namespaceAfterStartElement(String prefix, String uri) throws SAXException {
        if (this.m_elemContext.m_elementURI == null) {
            String prefix1 = getPrefixPart(this.m_elemContext.m_elementName);
            if (prefix1 == null && "".equals(prefix)) {
                this.m_elemContext.m_elementURI = uri;
            }
        }
        startPrefixMapping(prefix, uri, false);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToStream, org.xml.sax.ext.LexicalHandler
    public void startDTD(String name, String publicId, String systemId) throws SAXException {
        this.m_inDTD = true;
        super.startDTD(name, publicId, systemId);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToStream, org.xml.sax.ext.LexicalHandler
    public void endDTD() throws SAXException {
        this.m_inDTD = false;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToStream, org.xml.sax.ext.DeclHandler
    public void attributeDecl(String eName, String aName, String type, String valueDefault, String value) throws SAXException {
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToStream, org.xml.sax.ext.DeclHandler
    public void elementDecl(String name, String model) throws SAXException {
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToStream, org.xml.sax.ext.DeclHandler
    public void internalEntityDecl(String name, String value) throws SAXException {
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToStream, org.xml.sax.ext.DeclHandler
    public void externalEntityDecl(String name, String publicId, String systemId) throws SAXException {
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void addUniqueAttribute(String name, String value, int flags) throws SAXException {
        try {
            Writer writer = this.m_writer;
            if ((flags & 1) > 0 && m_htmlcharInfo.onlyQuotAmpLtGt) {
                writer.write(32);
                writer.write(name);
                writer.write("=\"");
                writer.write(value);
                writer.write(34);
            } else if ((flags & 2) > 0 && (value.length() == 0 || value.equalsIgnoreCase(name))) {
                writer.write(32);
                writer.write(name);
            } else {
                writer.write(32);
                writer.write(name);
                writer.write("=\"");
                if ((flags & 4) > 0) {
                    writeAttrURI(writer, value, this.m_specialEscapeURLs);
                } else {
                    writeAttrString(writer, value, getEncoding());
                }
                writer.write(34);
            }
        } catch (IOException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToStream, org.xml.sax.ext.LexicalHandler
    public void comment(char[] ch, int start, int length) throws SAXException {
        if (this.m_inDTD) {
            return;
        }
        super.comment(ch, start, length);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToStream, com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.Serializer
    public boolean reset() {
        boolean ret = super.reset();
        if (!ret) {
            return false;
        }
        initToHTMLStream();
        return true;
    }

    private void initToHTMLStream() {
        this.m_inBlockElem = false;
        this.m_inDTD = false;
        this.m_omitMetaTag = false;
        this.m_specialEscapeURLs = true;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/ToHTMLStream$Trie.class */
    static class Trie {
        public static final int ALPHA_SIZE = 128;
        final Node m_Root;
        private char[] m_charBuffer;
        private final boolean m_lowerCaseOnly;

        public Trie() {
            this.m_charBuffer = new char[0];
            this.m_Root = new Node();
            this.m_lowerCaseOnly = false;
        }

        public Trie(boolean lowerCaseOnly) {
            this.m_charBuffer = new char[0];
            this.m_Root = new Node();
            this.m_lowerCaseOnly = lowerCaseOnly;
        }

        public Object put(String key, Object value) {
            int len = key.length();
            if (len > this.m_charBuffer.length) {
                this.m_charBuffer = new char[len];
            }
            Node node = this.m_Root;
            int i2 = 0;
            while (true) {
                if (i2 >= len) {
                    break;
                }
                Node nextNode = node.m_nextChar[Character.toLowerCase(key.charAt(i2))];
                if (nextNode != null) {
                    node = nextNode;
                    i2++;
                } else {
                    while (i2 < len) {
                        Node newNode = new Node();
                        if (this.m_lowerCaseOnly) {
                            node.m_nextChar[Character.toLowerCase(key.charAt(i2))] = newNode;
                        } else {
                            node.m_nextChar[Character.toUpperCase(key.charAt(i2))] = newNode;
                            node.m_nextChar[Character.toLowerCase(key.charAt(i2))] = newNode;
                        }
                        node = newNode;
                        i2++;
                    }
                }
            }
            Object ret = node.m_Value;
            node.m_Value = value;
            return ret;
        }

        public Object get(String key) {
            int len = key.length();
            if (this.m_charBuffer.length < len) {
                return null;
            }
            Node node = this.m_Root;
            switch (len) {
                case 0:
                    break;
                case 1:
                    char ch = key.charAt(0);
                    if (ch < 128 && (node = node.m_nextChar[ch]) != null) {
                        break;
                    }
                    break;
                default:
                    for (int i2 = 0; i2 < len; i2++) {
                        char ch2 = key.charAt(i2);
                        if (128 <= ch2) {
                            break;
                        } else {
                            node = node.m_nextChar[ch2];
                            if (node == null) {
                                break;
                            }
                        }
                    }
                    break;
            }
            return null;
        }

        /* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/ToHTMLStream$Trie$Node.class */
        private class Node {
            final Node[] m_nextChar = new Node[128];
            Object m_Value = null;

            Node() {
            }
        }

        public Trie(Trie existingTrie) {
            this.m_charBuffer = new char[0];
            this.m_Root = existingTrie.m_Root;
            this.m_lowerCaseOnly = existingTrie.m_lowerCaseOnly;
            int max = existingTrie.getLongestKeyLength();
            this.m_charBuffer = new char[max];
        }

        public Object get2(String key) {
            int len = key.length();
            if (this.m_charBuffer.length < len) {
                return null;
            }
            Node node = this.m_Root;
            switch (len) {
                case 0:
                    break;
                case 1:
                    char ch = key.charAt(0);
                    if (ch < 128 && (node = node.m_nextChar[ch]) != null) {
                        break;
                    }
                    break;
                default:
                    key.getChars(0, len, this.m_charBuffer, 0);
                    for (int i2 = 0; i2 < len; i2++) {
                        char ch2 = this.m_charBuffer[i2];
                        if (128 <= ch2) {
                            break;
                        } else {
                            node = node.m_nextChar[ch2];
                            if (node == null) {
                                break;
                            }
                        }
                    }
                    break;
            }
            return null;
        }

        public int getLongestKeyLength() {
            return this.m_charBuffer.length;
        }
    }
}
