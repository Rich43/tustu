package com.sun.org.apache.xalan.internal.xsltc.trax;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Constants;
import com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl;
import java.util.Stack;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import jdk.xml.internal.JdkXmlUtils;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.ext.Locator2;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/trax/SAX2DOM.class */
public class SAX2DOM implements ContentHandler, LexicalHandler, Constants {
    private Node _root;
    private Document _document;
    private Node _nextSibling;
    private Stack _nodeStk;
    private Vector _namespaceDecls;
    private Node _lastSibling;
    private Locator locator;
    private boolean needToSetDocumentInfo;
    private StringBuilder _textBuffer;
    private Node _nextSiblingCache;
    private DocumentBuilderFactory _factory;
    private boolean _internal;

    public SAX2DOM(boolean overrideDefaultParser) throws ParserConfigurationException {
        this._root = null;
        this._document = null;
        this._nextSibling = null;
        this._nodeStk = new Stack();
        this._namespaceDecls = null;
        this._lastSibling = null;
        this.locator = null;
        this.needToSetDocumentInfo = true;
        this._textBuffer = new StringBuilder();
        this._nextSiblingCache = null;
        this._internal = true;
        this._document = createDocument(overrideDefaultParser);
        this._root = this._document;
    }

    public SAX2DOM(Node root, Node nextSibling, boolean overrideDefaultParser) throws ParserConfigurationException {
        this._root = null;
        this._document = null;
        this._nextSibling = null;
        this._nodeStk = new Stack();
        this._namespaceDecls = null;
        this._lastSibling = null;
        this.locator = null;
        this.needToSetDocumentInfo = true;
        this._textBuffer = new StringBuilder();
        this._nextSiblingCache = null;
        this._internal = true;
        this._root = root;
        if (root instanceof Document) {
            this._document = (Document) root;
        } else if (root != null) {
            this._document = root.getOwnerDocument();
        } else {
            this._document = createDocument(overrideDefaultParser);
            this._root = this._document;
        }
        this._nextSibling = nextSibling;
    }

    public SAX2DOM(Node root, boolean overrideDefaultParser) throws ParserConfigurationException {
        this(root, null, overrideDefaultParser);
    }

    public Node getDOM() {
        return this._root;
    }

    @Override // org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) {
        if (length == 0) {
            return;
        }
        Node last = (Node) this._nodeStk.peek();
        if (last != this._document) {
            this._nextSiblingCache = this._nextSibling;
            this._textBuffer.append(ch, start, length);
        }
    }

    private void appendTextNode() {
        if (this._textBuffer.length() > 0) {
            Node last = (Node) this._nodeStk.peek();
            if (last == this._root && this._nextSiblingCache != null) {
                this._lastSibling = last.insertBefore(this._document.createTextNode(this._textBuffer.toString()), this._nextSiblingCache);
            } else {
                this._lastSibling = last.appendChild(this._document.createTextNode(this._textBuffer.toString()));
            }
            this._textBuffer.setLength(0);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void startDocument() {
        this._nodeStk.push(this._root);
    }

    @Override // org.xml.sax.ContentHandler
    public void endDocument() {
        this._nodeStk.pop();
    }

    private void setDocumentInfo() throws DOMException {
        if (this.locator == null) {
            return;
        }
        try {
            this._document.setXmlVersion(((Locator2) this.locator).getXMLVersion());
        } catch (ClassCastException e2) {
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void startElement(String namespace, String localName, String qName, Attributes attrs) throws DOMException {
        appendTextNode();
        if (this.needToSetDocumentInfo) {
            setDocumentInfo();
            this.needToSetDocumentInfo = false;
        }
        Element tmp = this._document.createElementNS(namespace, qName);
        if (this._namespaceDecls != null) {
            int nDecls = this._namespaceDecls.size();
            int i2 = 0;
            while (i2 < nDecls) {
                int i3 = i2;
                int i4 = i2 + 1;
                String prefix = (String) this._namespaceDecls.elementAt(i3);
                if (prefix == null || prefix.equals("")) {
                    tmp.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", (String) this._namespaceDecls.elementAt(i4));
                } else {
                    tmp.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + prefix, (String) this._namespaceDecls.elementAt(i4));
                }
                i2 = i4 + 1;
            }
            this._namespaceDecls.clear();
        }
        int nattrs = attrs.getLength();
        for (int i5 = 0; i5 < nattrs; i5++) {
            String attQName = attrs.getQName(i5);
            String attURI = attrs.getURI(i5);
            if (attrs.getLocalName(i5).equals("")) {
                tmp.setAttribute(attQName, attrs.getValue(i5));
                if (attrs.getType(i5).equals("ID")) {
                    tmp.setIdAttribute(attQName, true);
                }
            } else {
                tmp.setAttributeNS(attURI, attQName, attrs.getValue(i5));
                if (attrs.getType(i5).equals("ID")) {
                    tmp.setIdAttributeNS(attURI, attrs.getLocalName(i5), true);
                }
            }
        }
        Node last = (Node) this._nodeStk.peek();
        if (last == this._root && this._nextSibling != null) {
            last.insertBefore(tmp, this._nextSibling);
        } else {
            last.appendChild(tmp);
        }
        this._nodeStk.push(tmp);
        this._lastSibling = null;
    }

    @Override // org.xml.sax.ContentHandler
    public void endElement(String namespace, String localName, String qName) {
        appendTextNode();
        this._nodeStk.pop();
        this._lastSibling = null;
    }

    @Override // org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) {
        if (this._namespaceDecls == null) {
            this._namespaceDecls = new Vector(2);
        }
        this._namespaceDecls.addElement(prefix);
        this._namespaceDecls.addElement(uri);
    }

    @Override // org.xml.sax.ContentHandler
    public void endPrefixMapping(String prefix) {
    }

    @Override // org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] ch, int start, int length) {
    }

    @Override // org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws DOMException {
        appendTextNode();
        Node last = (Node) this._nodeStk.peek();
        ProcessingInstruction pi = this._document.createProcessingInstruction(target, data);
        if (pi != null) {
            if (last == this._root && this._nextSibling != null) {
                last.insertBefore(pi, this._nextSibling);
            } else {
                last.appendChild(pi);
            }
            this._lastSibling = pi;
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
    }

    @Override // org.xml.sax.ContentHandler
    public void skippedEntity(String name) {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void comment(char[] ch, int start, int length) throws DOMException {
        appendTextNode();
        Node last = (Node) this._nodeStk.peek();
        Comment comment = this._document.createComment(new String(ch, start, length));
        if (comment != null) {
            if (last == this._root && this._nextSibling != null) {
                last.insertBefore(comment, this._nextSibling);
            } else {
                last.appendChild(comment);
            }
            this._lastSibling = comment;
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startCDATA() {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endCDATA() {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startEntity(String name) {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endDTD() {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endEntity(String name) {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startDTD(String name, String publicId, String systemId) throws SAXException {
    }

    private Document createDocument(boolean overrideDefaultParser) throws ParserConfigurationException {
        Document doc;
        if (this._factory == null) {
            this._factory = JdkXmlUtils.getDOMFactory(overrideDefaultParser);
            this._internal = true;
            if (!(this._factory instanceof DocumentBuilderFactoryImpl)) {
                this._internal = false;
            }
        }
        if (this._internal) {
            doc = this._factory.newDocumentBuilder().newDocument();
        } else {
            synchronized (SAX2DOM.class) {
                doc = this._factory.newDocumentBuilder().newDocument();
            }
        }
        return doc;
    }
}
