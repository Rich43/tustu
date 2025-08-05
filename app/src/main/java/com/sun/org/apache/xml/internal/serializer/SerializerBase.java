package com.sun.org.apache.xml.internal.serializer;

import com.sun.org.apache.xml.internal.serializer.utils.Utils;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Set;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.Transformer;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.Locator2;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/SerializerBase.class */
public abstract class SerializerBase implements SerializationHandler, SerializerConstants {
    protected String m_doctypeSystem;
    protected String m_doctypePublic;
    private String m_standalone;
    protected String m_mediatype;
    private Transformer m_transformer;
    protected NamespaceMappings m_prefixMap;
    protected SerializerTrace m_tracer;
    protected SourceLocator m_sourceLocator;
    private HashMap<String, String> m_OutputProps;
    private HashMap<String, String> m_OutputPropsDefault;
    protected boolean m_needToCallStartDocument = true;
    protected boolean m_cdataTagOpen = false;
    protected AttributesImplSerializer m_attributes = new AttributesImplSerializer();
    protected boolean m_inEntityRef = false;
    protected boolean m_inExternalDTD = false;
    boolean m_needToOutputDocTypeDecl = true;
    protected boolean m_shouldNotWriteXMLHeader = false;
    protected boolean m_standaloneWasSpecified = false;
    protected boolean m_isStandalone = false;
    protected boolean m_doIndent = false;
    protected int m_indentAmount = 0;
    protected String m_version = null;
    protected Writer m_writer = null;
    protected ElemContext m_elemContext = new ElemContext();
    protected char[] m_charsBuff = new char[60];
    protected char[] m_attrBuff = new char[30];
    private Locator m_locator = null;
    protected boolean m_needToCallSetDocumentInfo = true;
    protected String m_StringOfCDATASections = null;
    boolean m_docIsEmpty = true;
    protected HashMap<String, HashMap<String, String>> m_CdataElems = null;

    protected void fireEndElem(String name) throws SAXException {
        if (this.m_tracer != null) {
            flushMyWriter();
            this.m_tracer.fireGenerateEvent(4, name, (Attributes) null);
        }
    }

    protected void fireCharEvent(char[] chars, int start, int length) throws SAXException {
        if (this.m_tracer != null) {
            flushMyWriter();
            this.m_tracer.fireGenerateEvent(5, chars, start, length);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedLexicalHandler
    public void comment(String data) throws SAXException {
        int length = data.length();
        if (length > this.m_charsBuff.length) {
            this.m_charsBuff = new char[(length * 2) + 1];
        }
        data.getChars(0, length, this.m_charsBuff, 0);
        comment(this.m_charsBuff, 0, length);
    }

    protected String patchName(String qname) {
        int lastColon = qname.lastIndexOf(58);
        if (lastColon > 0) {
            int firstColon = qname.indexOf(58);
            String prefix = qname.substring(0, firstColon);
            String localName = qname.substring(lastColon + 1);
            String uri = this.m_prefixMap.lookupNamespace(prefix);
            if (uri != null && uri.length() == 0) {
                return localName;
            }
            if (firstColon != lastColon) {
                return prefix + ':' + localName;
            }
        }
        return qname;
    }

    protected static String getLocalName(String qname) {
        int col = qname.lastIndexOf(58);
        return col > 0 ? qname.substring(col + 1) : qname;
    }

    @Override // org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator locator) {
        this.m_locator = locator;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void addAttribute(String uri, String localName, String rawName, String type, String value, boolean XSLAttribute) throws SAXException {
        if (this.m_elemContext.m_startTagOpen) {
            addAttributeAlways(uri, localName, rawName, type, value, XSLAttribute);
        }
    }

    public boolean addAttributeAlways(String uri, String localName, String rawName, String type, String value, boolean XSLAttribute) {
        int index;
        boolean was_added;
        if (localName == null || uri == null || uri.length() == 0) {
            index = this.m_attributes.getIndex(rawName);
        } else {
            index = this.m_attributes.getIndex(uri, localName);
        }
        if (index >= 0) {
            this.m_attributes.setValue(index, value);
            was_added = false;
        } else {
            this.m_attributes.addAttribute(uri, localName, rawName, type, value);
            was_added = true;
        }
        return was_added;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void addAttribute(String name, String value) {
        if (this.m_elemContext.m_startTagOpen) {
            String patchedName = patchName(name);
            String localName = getLocalName(patchedName);
            String uri = getNamespaceURI(patchedName, false);
            addAttributeAlways(uri, localName, patchedName, "CDATA", value, false);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void addXSLAttribute(String name, String value, String uri) {
        if (this.m_elemContext.m_startTagOpen) {
            String patchedName = patchName(name);
            String localName = getLocalName(patchedName);
            addAttributeAlways(uri, localName, patchedName, "CDATA", value, true);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void addAttributes(Attributes atts) throws SAXException {
        int nAtts = atts.getLength();
        for (int i2 = 0; i2 < nAtts; i2++) {
            String uri = atts.getURI(i2);
            if (null == uri) {
                uri = "";
            }
            addAttributeAlways(uri, atts.getLocalName(i2), atts.getQName(i2), atts.getType(i2), atts.getValue(i2), false);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.Serializer
    public ContentHandler asContentHandler() throws IOException {
        return this;
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endEntity(String name) throws SAXException {
        if (name.equals("[dtd]")) {
            this.m_inExternalDTD = false;
        }
        this.m_inEntityRef = false;
        if (this.m_tracer != null) {
            fireEndEntity(name);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public void close() {
    }

    protected void initCDATA() {
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public String getEncoding() {
        return getOutputProperty("encoding");
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setEncoding(String encoding) {
        setOutputProperty("encoding", encoding);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setOmitXMLDeclaration(boolean b2) {
        String val = b2 ? "yes" : "no";
        setOutputProperty("omit-xml-declaration", val);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public boolean getOmitXMLDeclaration() {
        return this.m_shouldNotWriteXMLHeader;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public String getDoctypePublic() {
        return this.m_doctypePublic;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setDoctypePublic(String doctypePublic) {
        setOutputProperty("doctype-public", doctypePublic);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public String getDoctypeSystem() {
        return this.m_doctypeSystem;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setDoctypeSystem(String doctypeSystem) {
        setOutputProperty("doctype-system", doctypeSystem);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setDoctype(String doctypeSystem, String doctypePublic) {
        setOutputProperty("doctype-system", doctypeSystem);
        setOutputProperty("doctype-public", doctypePublic);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setStandalone(String standalone) {
        setOutputProperty("standalone", standalone);
    }

    protected void setStandaloneInternal(String standalone) {
        if ("yes".equals(standalone)) {
            this.m_standalone = "yes";
        } else {
            this.m_standalone = "no";
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public String getStandalone() {
        return this.m_standalone;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public boolean getIndent() {
        return this.m_doIndent;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public String getMediaType() {
        return this.m_mediatype;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public String getVersion() {
        return this.m_version;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setVersion(String version) {
        setOutputProperty("version", version);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setMediaType(String mediaType) {
        setOutputProperty("media-type", mediaType);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public int getIndentAmount() {
        return this.m_indentAmount;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public void setIndentAmount(int m_indentAmount) {
        this.m_indentAmount = m_indentAmount;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setIndent(boolean doIndent) {
        String val = doIndent ? "yes" : "no";
        setOutputProperty("indent", val);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public void setIsStandalone(boolean isStandalone) {
        this.m_isStandalone = isStandalone;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void namespaceAfterStartElement(String uri, String prefix) throws SAXException {
    }

    @Override // com.sun.org.apache.xml.internal.serializer.Serializer
    public DOMSerializer asDOMSerializer() throws IOException {
        return this;
    }

    private static final boolean subPartMatch(String p2, String t2) {
        return p2 == t2 || (null != p2 && p2.equals(t2));
    }

    protected static final String getPrefixPart(String qname) {
        int col = qname.indexOf(58);
        if (col > 0) {
            return qname.substring(0, col);
        }
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public NamespaceMappings getNamespaceMappings() {
        return this.m_prefixMap;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public String getPrefix(String namespaceURI) {
        String prefix = this.m_prefixMap.lookupPrefix(namespaceURI);
        return prefix;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public String getNamespaceURI(String qname, boolean isElement) {
        String uri = "";
        int col = qname.lastIndexOf(58);
        String prefix = col > 0 ? qname.substring(0, col) : "";
        if ((!"".equals(prefix) || isElement) && this.m_prefixMap != null) {
            uri = this.m_prefixMap.lookupNamespace(prefix);
            if (uri == null && !prefix.equals("xmlns")) {
                throw new RuntimeException(Utils.messages.createMessage("ER_NAMESPACE_PREFIX", new Object[]{qname.substring(0, col)}));
            }
        }
        return uri;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public String getNamespaceURIFromPrefix(String prefix) {
        String uri = null;
        if (this.m_prefixMap != null) {
            uri = this.m_prefixMap.lookupNamespace(prefix);
        }
        return uri;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void entityReference(String name) throws SAXException {
        flushPending();
        startEntity(name);
        endEntity(name);
        if (this.m_tracer != null) {
            fireEntityReference(name);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public void setTransformer(Transformer t2) {
        this.m_transformer = t2;
        if ((this.m_transformer instanceof SerializerTrace) && ((SerializerTrace) this.m_transformer).hasTraceListeners()) {
            this.m_tracer = (SerializerTrace) this.m_transformer;
        } else {
            this.m_tracer = null;
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public Transformer getTransformer() {
        return this.m_transformer;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void characters(Node node) throws DOMException, SAXException {
        flushPending();
        String data = node.getNodeValue();
        if (data != null) {
            int length = data.length();
            if (length > this.m_charsBuff.length) {
                this.m_charsBuff = new char[(length * 2) + 1];
            }
            data.getChars(0, length, this.m_charsBuff, 0);
            characters(this.m_charsBuff, 0, length);
        }
    }

    @Override // org.xml.sax.ErrorHandler
    public void error(SAXParseException exc) throws SAXException {
    }

    @Override // org.xml.sax.ErrorHandler
    public void fatalError(SAXParseException exc) throws SAXException {
        this.m_elemContext.m_startTagOpen = false;
    }

    @Override // org.xml.sax.ErrorHandler
    public void warning(SAXParseException exc) throws SAXException {
    }

    protected void fireStartEntity(String name) throws SAXException {
        if (this.m_tracer != null) {
            flushMyWriter();
            this.m_tracer.fireGenerateEvent(9, name);
        }
    }

    private void flushMyWriter() {
        if (this.m_writer != null) {
            try {
                this.m_writer.flush();
            } catch (IOException e2) {
            }
        }
    }

    protected void fireCDATAEvent(char[] chars, int start, int length) throws SAXException {
        if (this.m_tracer != null) {
            flushMyWriter();
            this.m_tracer.fireGenerateEvent(10, chars, start, length);
        }
    }

    protected void fireCommentEvent(char[] chars, int start, int length) throws SAXException {
        if (this.m_tracer != null) {
            flushMyWriter();
            this.m_tracer.fireGenerateEvent(8, new String(chars, start, length));
        }
    }

    public void fireEndEntity(String name) throws SAXException {
        if (this.m_tracer != null) {
            flushMyWriter();
        }
    }

    protected void fireStartDoc() throws SAXException {
        if (this.m_tracer != null) {
            flushMyWriter();
            this.m_tracer.fireGenerateEvent(1);
        }
    }

    protected void fireEndDoc() throws SAXException {
        if (this.m_tracer != null) {
            flushMyWriter();
            this.m_tracer.fireGenerateEvent(2);
        }
    }

    protected void fireStartElem(String elemName) throws SAXException {
        if (this.m_tracer != null) {
            flushMyWriter();
            this.m_tracer.fireGenerateEvent(3, elemName, this.m_attributes);
        }
    }

    protected void fireEscapingEvent(String name, String data) throws SAXException {
        if (this.m_tracer != null) {
            flushMyWriter();
            this.m_tracer.fireGenerateEvent(7, name, data);
        }
    }

    protected void fireEntityReference(String name) throws SAXException {
        if (this.m_tracer != null) {
            flushMyWriter();
            this.m_tracer.fireGenerateEvent(9, name, (Attributes) null);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
        startDocumentInternal();
        this.m_needToCallStartDocument = false;
    }

    protected void startDocumentInternal() throws SAXException {
        if (this.m_tracer != null) {
            fireStartDoc();
        }
    }

    protected void setDocumentInfo() {
        if (this.m_locator == null) {
            return;
        }
        try {
            String strVersion = ((Locator2) this.m_locator).getXMLVersion();
            if (strVersion != null) {
                setVersion(strVersion);
            }
        } catch (ClassCastException e2) {
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void setSourceLocator(SourceLocator locator) {
        this.m_sourceLocator = locator;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public void setNamespaceMappings(NamespaceMappings mappings) {
        this.m_prefixMap = mappings;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.Serializer
    public boolean reset() {
        resetSerializerBase();
        return true;
    }

    private void resetSerializerBase() {
        this.m_attributes.clear();
        this.m_StringOfCDATASections = null;
        this.m_elemContext = new ElemContext();
        this.m_doctypePublic = null;
        this.m_doctypeSystem = null;
        this.m_doIndent = false;
        this.m_indentAmount = 0;
        this.m_inEntityRef = false;
        this.m_inExternalDTD = false;
        this.m_mediatype = null;
        this.m_needToCallStartDocument = true;
        this.m_needToOutputDocTypeDecl = false;
        if (this.m_prefixMap != null) {
            this.m_prefixMap.reset();
        }
        this.m_shouldNotWriteXMLHeader = false;
        this.m_sourceLocator = null;
        this.m_standalone = null;
        this.m_standaloneWasSpecified = false;
        this.m_tracer = null;
        this.m_transformer = null;
        this.m_version = null;
    }

    final boolean inTemporaryOutputState() {
        return getEncoding() == null;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void addAttribute(String uri, String localName, String rawName, String type, String value) throws SAXException {
        if (this.m_elemContext.m_startTagOpen) {
            addAttributeAlways(uri, localName, rawName, type, value, false);
        }
    }

    @Override // org.xml.sax.DTDHandler
    public void notationDecl(String arg0, String arg1, String arg2) throws SAXException {
    }

    @Override // org.xml.sax.DTDHandler
    public void unparsedEntityDecl(String arg0, String arg1, String arg2, String arg3) throws SAXException {
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public void setDTDEntityExpansion(boolean expand) {
    }

    void initCdataElems(String s2) {
        if (s2 != null) {
            int max = s2.length();
            boolean inCurly = false;
            boolean foundURI = false;
            StringBuilder buf = new StringBuilder();
            String uri = null;
            for (int i2 = 0; i2 < max; i2++) {
                char c2 = s2.charAt(i2);
                if (Character.isWhitespace(c2)) {
                    if (!inCurly) {
                        if (buf.length() > 0) {
                            String localName = buf.toString();
                            if (!foundURI) {
                                uri = "";
                            }
                            addCDATAElement(uri, localName);
                            buf.setLength(0);
                            foundURI = false;
                        }
                    } else {
                        buf.append(c2);
                    }
                } else if ('{' == c2) {
                    inCurly = true;
                } else if ('}' == c2) {
                    foundURI = true;
                    uri = buf.toString();
                    buf.setLength(0);
                    inCurly = false;
                } else {
                    buf.append(c2);
                }
            }
            if (buf.length() > 0) {
                String localName2 = buf.toString();
                if (!foundURI) {
                    uri = "";
                }
                addCDATAElement(uri, localName2);
            }
        }
    }

    private void addCDATAElement(String uri, String localName) {
        if (this.m_CdataElems == null) {
            this.m_CdataElems = new HashMap<>();
        }
        HashMap<String, String> h2 = this.m_CdataElems.get(localName);
        if (h2 == null) {
            h2 = new HashMap<>();
            this.m_CdataElems.put(localName, h2);
        }
        h2.put(uri, uri);
    }

    public boolean documentIsEmpty() {
        return this.m_docIsEmpty && this.m_elemContext.m_currentElemDepth == 0;
    }

    protected boolean isCdataSection() {
        boolean b2 = false;
        if (null != this.m_StringOfCDATASections) {
            if (this.m_elemContext.m_elementLocalName == null) {
                String localName = getLocalName(this.m_elemContext.m_elementName);
                this.m_elemContext.m_elementLocalName = localName;
            }
            if (this.m_elemContext.m_elementURI == null) {
                this.m_elemContext.m_elementURI = getElementURI();
            } else if (this.m_elemContext.m_elementURI.length() == 0) {
                if (this.m_elemContext.m_elementName == null) {
                    this.m_elemContext.m_elementName = this.m_elemContext.m_elementLocalName;
                } else if (this.m_elemContext.m_elementLocalName.length() < this.m_elemContext.m_elementName.length()) {
                    this.m_elemContext.m_elementURI = getElementURI();
                }
            }
            HashMap<String, String> h2 = null;
            if (this.m_CdataElems != null) {
                h2 = this.m_CdataElems.get(this.m_elemContext.m_elementLocalName);
            }
            if (h2 != null) {
                Object obj = h2.get(this.m_elemContext.m_elementURI);
                if (obj != null) {
                    b2 = true;
                }
            }
        }
        return b2;
    }

    private String getElementURI() {
        String uri;
        String prefix = getPrefixPart(this.m_elemContext.m_elementName);
        if (prefix == null) {
            uri = this.m_prefixMap.lookupNamespace("");
        } else {
            uri = this.m_prefixMap.lookupNamespace(prefix);
        }
        if (uri == null) {
            uri = "";
        }
        return uri;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public String getOutputProperty(String name) {
        String val = getOutputPropertyNonDefault(name);
        if (val == null) {
            val = getOutputPropertyDefault(name);
        }
        return val;
    }

    public String getOutputPropertyNonDefault(String name) {
        return getProp(name, false);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public String getOutputPropertyDefault(String name) {
        return getProp(name, true);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setOutputProperty(String name, String val) {
        setProp(name, val, false);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setOutputPropertyDefault(String name, String val) {
        setProp(name, val, true);
    }

    Set<String> getOutputPropDefaultKeys() {
        return this.m_OutputPropsDefault.keySet();
    }

    Set<String> getOutputPropKeys() {
        return this.m_OutputProps.keySet();
    }

    private String getProp(String name, boolean defaultVal) {
        String val;
        if (this.m_OutputProps == null) {
            this.m_OutputProps = new HashMap<>();
            this.m_OutputPropsDefault = new HashMap<>();
        }
        if (defaultVal) {
            val = this.m_OutputPropsDefault.get(name);
        } else {
            val = this.m_OutputProps.get(name);
        }
        return val;
    }

    void setProp(String name, String val, boolean defaultVal) {
        String newVal;
        if (this.m_OutputProps == null) {
            this.m_OutputProps = new HashMap<>();
            this.m_OutputPropsDefault = new HashMap<>();
        }
        if (defaultVal) {
            this.m_OutputPropsDefault.put(name, val);
            return;
        }
        if ("cdata-section-elements".equals(name) && val != null) {
            initCdataElems(val);
            String oldVal = this.m_OutputProps.get(name);
            if (oldVal == null) {
                newVal = oldVal + ' ' + val;
            } else {
                newVal = val;
            }
            this.m_OutputProps.put(name, newVal);
            return;
        }
        this.m_OutputProps.put(name, val);
    }

    static char getFirstCharLocName(String name) {
        char first;
        int i2 = name.indexOf(125);
        if (i2 < 0) {
            first = name.charAt(0);
        } else {
            first = name.charAt(i2 + 1);
        }
        return first;
    }
}
