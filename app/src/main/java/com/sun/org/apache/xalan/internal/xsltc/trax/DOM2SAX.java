package com.sun.org.apache.xalan.internal.xsltc.trax;

import com.sun.org.apache.xalan.internal.xsltc.dom.SAXImpl;
import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/trax/DOM2SAX.class */
public class DOM2SAX implements XMLReader, Locator {
    private static final String EMPTYSTRING = "";
    private static final String XMLNS_PREFIX = "xmlns";
    private Node _dom;
    private ContentHandler _sax = null;
    private LexicalHandler _lex = null;
    private SAXImpl _saxImpl = null;
    private Map<String, Stack> _nsPrefixes = new HashMap();

    public DOM2SAX(Node root) {
        this._dom = null;
        this._dom = root;
    }

    @Override // org.xml.sax.XMLReader
    public ContentHandler getContentHandler() {
        return this._sax;
    }

    @Override // org.xml.sax.XMLReader
    public void setContentHandler(ContentHandler handler) throws NullPointerException {
        this._sax = handler;
        if (handler instanceof LexicalHandler) {
            this._lex = (LexicalHandler) handler;
        }
        if (handler instanceof SAXImpl) {
            this._saxImpl = (SAXImpl) handler;
        }
    }

    private boolean startPrefixMapping(String prefix, String uri) throws SAXException {
        boolean pushed = true;
        Stack uriStack = this._nsPrefixes.get(prefix);
        if (uriStack != null) {
            if (uriStack.isEmpty()) {
                this._sax.startPrefixMapping(prefix, uri);
                uriStack.push(uri);
            } else {
                String lastUri = (String) uriStack.peek();
                if (!lastUri.equals(uri)) {
                    this._sax.startPrefixMapping(prefix, uri);
                    uriStack.push(uri);
                } else {
                    pushed = false;
                }
            }
        } else {
            this._sax.startPrefixMapping(prefix, uri);
            Map<String, Stack> map = this._nsPrefixes;
            Stack uriStack2 = new Stack();
            map.put(prefix, uriStack2);
            uriStack2.push(uri);
        }
        return pushed;
    }

    private void endPrefixMapping(String prefix) throws SAXException {
        Stack uriStack = this._nsPrefixes.get(prefix);
        if (uriStack != null) {
            this._sax.endPrefixMapping(prefix);
            uriStack.pop();
        }
    }

    private static String getLocalName(Node node) {
        String localName = node.getLocalName();
        if (localName == null) {
            String qname = node.getNodeName();
            int col = qname.lastIndexOf(58);
            return col > 0 ? qname.substring(col + 1) : qname;
        }
        return localName;
    }

    @Override // org.xml.sax.XMLReader
    public void parse(InputSource unused) throws DOMException, SAXException, IOException {
        parse(this._dom);
    }

    public void parse() throws SAXException, IOException {
        if (this._dom != null) {
            boolean isIncomplete = this._dom.getNodeType() != 9;
            if (isIncomplete) {
                this._sax.startDocument();
                parse(this._dom);
                this._sax.endDocument();
                return;
            }
            parse(this._dom);
        }
    }

    private void parse(Node node) throws DOMException, SAXException, IOException {
        String prefix;
        if (node == null) {
        }
        switch (node.getNodeType()) {
            case 1:
                Vector pushedPrefixes = new Vector();
                AttributesImpl attrs = new AttributesImpl();
                NamedNodeMap map = node.getAttributes();
                int length = map.getLength();
                for (int i2 = 0; i2 < length; i2++) {
                    Node attr = map.item(i2);
                    String qnameAttr = attr.getNodeName();
                    if (qnameAttr.startsWith("xmlns")) {
                        String uriAttr = attr.getNodeValue();
                        int colon = qnameAttr.lastIndexOf(58);
                        String prefix2 = colon > 0 ? qnameAttr.substring(colon + 1) : "";
                        if (startPrefixMapping(prefix2, uriAttr)) {
                            pushedPrefixes.addElement(prefix2);
                        }
                    }
                }
                for (int i3 = 0; i3 < length; i3++) {
                    Node attr2 = map.item(i3);
                    String qnameAttr2 = attr2.getNodeName();
                    if (!qnameAttr2.startsWith("xmlns")) {
                        String uriAttr2 = attr2.getNamespaceURI();
                        getLocalName(attr2);
                        if (uriAttr2 != null) {
                            int colon2 = qnameAttr2.lastIndexOf(58);
                            if (colon2 > 0) {
                                prefix = qnameAttr2.substring(0, colon2);
                            } else {
                                prefix = BasisLibrary.generatePrefix();
                                qnameAttr2 = prefix + ':' + qnameAttr2;
                            }
                            if (startPrefixMapping(prefix, uriAttr2)) {
                                pushedPrefixes.addElement(prefix);
                            }
                        }
                        attrs.addAttribute(attr2.getNamespaceURI(), getLocalName(attr2), qnameAttr2, "CDATA", attr2.getNodeValue());
                    }
                }
                String qname = node.getNodeName();
                String uri = node.getNamespaceURI();
                String localName = getLocalName(node);
                if (uri != null) {
                    int colon3 = qname.lastIndexOf(58);
                    String prefix3 = colon3 > 0 ? qname.substring(0, colon3) : "";
                    if (startPrefixMapping(prefix3, uri)) {
                        pushedPrefixes.addElement(prefix3);
                    }
                }
                if (this._saxImpl != null) {
                    this._saxImpl.startElement(uri, localName, qname, attrs, node);
                } else {
                    this._sax.startElement(uri, localName, qname, attrs);
                }
                Node firstChild = node.getFirstChild();
                while (true) {
                    Node next = firstChild;
                    if (next != null) {
                        parse(next);
                        firstChild = next.getNextSibling();
                    } else {
                        this._sax.endElement(uri, localName, qname);
                        int nPushedPrefixes = pushedPrefixes.size();
                        for (int i4 = 0; i4 < nPushedPrefixes; i4++) {
                            endPrefixMapping((String) pushedPrefixes.elementAt(i4));
                        }
                        break;
                    }
                }
            case 3:
                String data = node.getNodeValue();
                this._sax.characters(data.toCharArray(), 0, data.length());
                break;
            case 4:
                String cdata = node.getNodeValue();
                if (this._lex != null) {
                    this._lex.startCDATA();
                    this._sax.characters(cdata.toCharArray(), 0, cdata.length());
                    this._lex.endCDATA();
                    break;
                } else {
                    this._sax.characters(cdata.toCharArray(), 0, cdata.length());
                    break;
                }
            case 7:
                this._sax.processingInstruction(node.getNodeName(), node.getNodeValue());
                break;
            case 8:
                if (this._lex != null) {
                    String value = node.getNodeValue();
                    this._lex.comment(value.toCharArray(), 0, value.length());
                    break;
                }
                break;
            case 9:
                this._sax.setDocumentLocator(this);
                this._sax.startDocument();
                Node firstChild2 = node.getFirstChild();
                while (true) {
                    Node next2 = firstChild2;
                    if (next2 != null) {
                        parse(next2);
                        firstChild2 = next2.getNextSibling();
                    } else {
                        this._sax.endDocument();
                        break;
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
