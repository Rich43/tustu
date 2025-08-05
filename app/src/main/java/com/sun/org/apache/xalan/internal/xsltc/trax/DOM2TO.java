package com.sun.org.apache.xalan.internal.xsltc.trax;

import com.sun.org.apache.xml.internal.serializer.NamespaceMappings;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import java.io.IOException;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.Locator2;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/trax/DOM2TO.class */
public class DOM2TO implements XMLReader, Locator2 {
    private static final String EMPTYSTRING = "";
    private static final String XMLNS_PREFIX = "xmlns";
    private Node _dom;
    private SerializationHandler _handler;
    private String xmlVersion = null;
    private String xmlEncoding = null;

    public DOM2TO(Node root, SerializationHandler handler) {
        this._dom = root;
        this._handler = handler;
    }

    @Override // org.xml.sax.XMLReader
    public ContentHandler getContentHandler() {
        return null;
    }

    @Override // org.xml.sax.XMLReader
    public void setContentHandler(ContentHandler handler) {
    }

    @Override // org.xml.sax.XMLReader
    public void parse(InputSource unused) throws SAXException, DOMException, IOException {
        parse(this._dom);
    }

    public void parse() throws SAXException, DOMException, IOException {
        if (this._dom != null) {
            boolean isIncomplete = this._dom.getNodeType() != 9;
            if (isIncomplete) {
                this._handler.startDocument();
                parse(this._dom);
                this._handler.endDocument();
                return;
            }
            parse(this._dom);
        }
    }

    private void parse(Node node) throws SAXException, DOMException, IOException {
        if (node == null) {
        }
        switch (node.getNodeType()) {
            case 1:
                String qname = node.getNodeName();
                this._handler.startElement(null, null, qname);
                NamedNodeMap map = node.getAttributes();
                int length = map.getLength();
                for (int i2 = 0; i2 < length; i2++) {
                    Node attr = map.item(i2);
                    String qnameAttr = attr.getNodeName();
                    if (qnameAttr.startsWith("xmlns")) {
                        String uriAttr = attr.getNodeValue();
                        int colon = qnameAttr.lastIndexOf(58);
                        this._handler.namespaceAfterStartElement(colon > 0 ? qnameAttr.substring(colon + 1) : "", uriAttr);
                    }
                }
                NamespaceMappings nm = new NamespaceMappings();
                for (int i3 = 0; i3 < length; i3++) {
                    Node attr2 = map.item(i3);
                    String qnameAttr2 = attr2.getNodeName();
                    if (!qnameAttr2.startsWith("xmlns")) {
                        String uriAttr2 = attr2.getNamespaceURI();
                        if (uriAttr2 != null && !uriAttr2.equals("")) {
                            int colon2 = qnameAttr2.lastIndexOf(58);
                            String newPrefix = nm.lookupPrefix(uriAttr2);
                            if (newPrefix == null) {
                                newPrefix = nm.generateNextPrefix();
                            }
                            String prefix = colon2 > 0 ? qnameAttr2.substring(0, colon2) : newPrefix;
                            this._handler.namespaceAfterStartElement(prefix, uriAttr2);
                            this._handler.addAttribute(prefix + CallSiteDescriptor.TOKEN_DELIMITER + qnameAttr2, attr2.getNodeValue());
                        } else {
                            this._handler.addAttribute(qnameAttr2, attr2.getNodeValue());
                        }
                    }
                }
                String uri = node.getNamespaceURI();
                String localName = node.getLocalName();
                if (uri != null) {
                    int colon3 = qname.lastIndexOf(58);
                    this._handler.namespaceAfterStartElement(colon3 > 0 ? qname.substring(0, colon3) : "", uri);
                } else if (uri == null && localName != null) {
                    this._handler.namespaceAfterStartElement("", "");
                }
                Node firstChild = node.getFirstChild();
                while (true) {
                    Node next = firstChild;
                    if (next != null) {
                        parse(next);
                        firstChild = next.getNextSibling();
                    } else {
                        this._handler.endElement(qname);
                        break;
                    }
                }
                break;
            case 3:
                this._handler.characters(node.getNodeValue());
                break;
            case 4:
                this._handler.startCDATA();
                this._handler.characters(node.getNodeValue());
                this._handler.endCDATA();
                break;
            case 7:
                this._handler.processingInstruction(node.getNodeName(), node.getNodeValue());
                break;
            case 8:
                this._handler.comment(node.getNodeValue());
                break;
            case 9:
                setDocumentInfo((Document) node);
                this._handler.setDocumentLocator(this);
                this._handler.startDocument();
                Node firstChild2 = node.getFirstChild();
                while (true) {
                    Node next2 = firstChild2;
                    if (next2 != null) {
                        parse(next2);
                        firstChild2 = next2.getNextSibling();
                    } else {
                        this._handler.endDocument();
                        break;
                    }
                }
            case 11:
                Node firstChild3 = node.getFirstChild();
                while (true) {
                    Node next3 = firstChild3;
                    if (next3 == null) {
                        break;
                    } else {
                        parse(next3);
                        firstChild3 = next3.getNextSibling();
                    }
                }
        }
    }

    @Override // org.xml.sax.XMLReader
    public DTDHandler getDTDHandler() {
        return null;
    }

    @Override // org.xml.sax.XMLReader
    public ErrorHandler getErrorHandler() {
        return null;
    }

    @Override // org.xml.sax.XMLReader
    public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        return false;
    }

    @Override // org.xml.sax.XMLReader
    public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
    }

    @Override // org.xml.sax.XMLReader
    public void parse(String sysId) throws SAXException, IOException {
        throw new IOException("This method is not yet implemented.");
    }

    @Override // org.xml.sax.XMLReader
    public void setDTDHandler(DTDHandler handler) throws NullPointerException {
    }

    @Override // org.xml.sax.XMLReader
    public void setEntityResolver(EntityResolver resolver) throws NullPointerException {
    }

    @Override // org.xml.sax.XMLReader
    public EntityResolver getEntityResolver() {
        return null;
    }

    @Override // org.xml.sax.XMLReader
    public void setErrorHandler(ErrorHandler handler) throws NullPointerException {
    }

    @Override // org.xml.sax.XMLReader
    public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
    }

    @Override // org.xml.sax.XMLReader
    public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        return null;
    }

    @Override // org.xml.sax.Locator
    public int getColumnNumber() {
        return 0;
    }

    @Override // org.xml.sax.Locator
    public int getLineNumber() {
        return 0;
    }

    @Override // org.xml.sax.Locator
    public String getPublicId() {
        return null;
    }

    @Override // org.xml.sax.Locator
    public String getSystemId() {
        return null;
    }

    private void setDocumentInfo(Document document) {
        if (!document.getXmlStandalone()) {
            this._handler.setStandalone(Boolean.toString(document.getXmlStandalone()));
        }
        setXMLVersion(document.getXmlVersion());
        setEncoding(document.getXmlEncoding());
    }

    @Override // org.xml.sax.ext.Locator2
    public String getXMLVersion() {
        return this.xmlVersion;
    }

    private void setXMLVersion(String version) {
        if (version != null) {
            this.xmlVersion = version;
            this._handler.setVersion(this.xmlVersion);
        }
    }

    @Override // org.xml.sax.ext.Locator2
    public String getEncoding() {
        return this.xmlEncoding;
    }

    private void setEncoding(String encoding) {
        if (encoding != null) {
            this.xmlEncoding = encoding;
            this._handler.setEncoding(encoding);
        }
    }

    private String getNodeTypeFromCode(short code) {
        String retval = null;
        switch (code) {
            case 1:
                retval = "ELEMENT_NODE";
                break;
            case 2:
                retval = "ATTRIBUTE_NODE";
                break;
            case 3:
                retval = "TEXT_NODE";
                break;
            case 4:
                retval = "CDATA_SECTION_NODE";
                break;
            case 5:
                retval = "ENTITY_REFERENCE_NODE";
                break;
            case 6:
                retval = "ENTITY_NODE";
                break;
            case 7:
                retval = "PROCESSING_INSTRUCTION_NODE";
                break;
            case 8:
                retval = "COMMENT_NODE";
                break;
            case 9:
                retval = "DOCUMENT_NODE";
                break;
            case 10:
                retval = "DOCUMENT_TYPE_NODE";
                break;
            case 11:
                retval = "DOCUMENT_FRAGMENT_NODE";
                break;
            case 12:
                retval = "NOTATION_NODE";
                break;
        }
        return retval;
    }
}
