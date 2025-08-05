package com.sun.org.apache.xml.internal.utils;

import com.sun.org.apache.xml.internal.res.XMLMessages;
import java.io.Writer;
import java.util.Stack;
import org.w3c.dom.CDATASection;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/DOMBuilder.class */
public class DOMBuilder implements ContentHandler, LexicalHandler {
    public Document m_doc;
    protected Node m_currentNode;
    protected Node m_root;
    protected Node m_nextSibling;
    public DocumentFragment m_docFrag;
    protected Stack m_elemStack;
    protected boolean m_inCData;

    public DOMBuilder(Document doc, Node node) {
        this.m_currentNode = null;
        this.m_root = null;
        this.m_nextSibling = null;
        this.m_docFrag = null;
        this.m_elemStack = new Stack();
        this.m_inCData = false;
        this.m_doc = doc;
        this.m_root = node;
        this.m_currentNode = node;
        if (node instanceof Element) {
            this.m_elemStack.push(node);
        }
    }

    public DOMBuilder(Document doc, DocumentFragment docFrag) {
        this.m_currentNode = null;
        this.m_root = null;
        this.m_nextSibling = null;
        this.m_docFrag = null;
        this.m_elemStack = new Stack();
        this.m_inCData = false;
        this.m_doc = doc;
        this.m_docFrag = docFrag;
    }

    public DOMBuilder(Document doc) {
        this.m_currentNode = null;
        this.m_root = null;
        this.m_nextSibling = null;
        this.m_docFrag = null;
        this.m_elemStack = new Stack();
        this.m_inCData = false;
        this.m_doc = doc;
    }

    public Node getRootDocument() {
        return null != this.m_docFrag ? this.m_docFrag : this.m_doc;
    }

    public Node getRootNode() {
        return this.m_root;
    }

    public Node getCurrentNode() {
        return this.m_currentNode;
    }

    public void setNextSibling(Node nextSibling) {
        this.m_nextSibling = nextSibling;
    }

    public Node getNextSibling() {
        return this.m_nextSibling;
    }

    public Writer getWriter() {
        return null;
    }

    protected void append(Node newNode) throws DOMException, SAXException {
        Node currentNode = this.m_currentNode;
        if (null != currentNode) {
            if (currentNode == this.m_root && this.m_nextSibling != null) {
                currentNode.insertBefore(newNode, this.m_nextSibling);
                return;
            } else {
                currentNode.appendChild(newNode);
                return;
            }
        }
        if (null != this.m_docFrag) {
            if (this.m_nextSibling != null) {
                this.m_docFrag.insertBefore(newNode, this.m_nextSibling);
                return;
            } else {
                this.m_docFrag.appendChild(newNode);
                return;
            }
        }
        boolean ok = true;
        short type = newNode.getNodeType();
        if (type == 3) {
            String data = newNode.getNodeValue();
            if (null != data && data.trim().length() > 0) {
                throw new SAXException(XMLMessages.createXMLMessage("ER_CANT_OUTPUT_TEXT_BEFORE_DOC", null));
            }
            ok = false;
        } else if (type == 1 && this.m_doc.getDocumentElement() != null) {
            throw new SAXException(XMLMessages.createXMLMessage("ER_CANT_HAVE_MORE_THAN_ONE_ROOT", null));
        }
        if (ok) {
            if (this.m_nextSibling != null) {
                this.m_doc.insertBefore(newNode, this.m_nextSibling);
            } else {
                this.m_doc.appendChild(newNode);
            }
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator locator) {
    }

    @Override // org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
    }

    @Override // org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
    }

    @Override // org.xml.sax.ContentHandler
    public void startElement(String ns, String localName, String name, Attributes atts) throws DOMException, SAXException {
        Element elem;
        if (null == ns || ns.length() == 0) {
            elem = this.m_doc.createElementNS(null, name);
        } else {
            elem = this.m_doc.createElementNS(ns, name);
        }
        append(elem);
        try {
            int nAtts = atts.getLength();
            if (0 != nAtts) {
                for (int i2 = 0; i2 < nAtts; i2++) {
                    if (atts.getType(i2).equalsIgnoreCase("ID")) {
                        setIDAttribute(atts.getValue(i2), elem);
                    }
                    String attrNS = atts.getURI(i2);
                    if ("".equals(attrNS)) {
                        attrNS = null;
                    }
                    String attrQName = atts.getQName(i2);
                    if (attrQName.startsWith("xmlns:") || attrQName.equals("xmlns")) {
                        attrNS = "http://www.w3.org/2000/xmlns/";
                    }
                    elem.setAttributeNS(attrNS, attrQName, atts.getValue(i2));
                }
            }
            this.m_elemStack.push(elem);
            this.m_currentNode = elem;
        } catch (Exception de2) {
            throw new SAXException(de2);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void endElement(String ns, String localName, String name) throws SAXException {
        this.m_elemStack.pop();
        this.m_currentNode = this.m_elemStack.isEmpty() ? null : (Node) this.m_elemStack.peek();
    }

    public void setIDAttribute(String id, Element elem) {
    }

    @Override // org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) throws SAXException, DOMException {
        if (isOutsideDocElem() && XMLCharacterRecognizer.isWhiteSpace(ch, start, length)) {
            return;
        }
        if (this.m_inCData) {
            cdata(ch, start, length);
            return;
        }
        String s2 = new String(ch, start, length);
        Node childNode = this.m_currentNode != null ? this.m_currentNode.getLastChild() : null;
        if (childNode != null && childNode.getNodeType() == 3) {
            ((Text) childNode).appendData(s2);
        } else {
            Text text = this.m_doc.createTextNode(s2);
            append(text);
        }
    }

    public void charactersRaw(char[] ch, int start, int length) throws DOMException, SAXException {
        if (isOutsideDocElem() && XMLCharacterRecognizer.isWhiteSpace(ch, start, length)) {
            return;
        }
        String s2 = new String(ch, start, length);
        append(this.m_doc.createProcessingInstruction("xslt-next-is-raw", "formatter-to-dom"));
        append(this.m_doc.createTextNode(s2));
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startEntity(String name) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endEntity(String name) throws SAXException {
    }

    public void entityReference(String name) throws DOMException, SAXException {
        append(this.m_doc.createEntityReference(name));
    }

    @Override // org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] ch, int start, int length) throws DOMException, SAXException {
        if (isOutsideDocElem()) {
            return;
        }
        String s2 = new String(ch, start, length);
        append(this.m_doc.createTextNode(s2));
    }

    private boolean isOutsideDocElem() {
        return null == this.m_docFrag && this.m_elemStack.size() == 0 && (null == this.m_currentNode || this.m_currentNode.getNodeType() == 9);
    }

    @Override // org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws DOMException, SAXException {
        append(this.m_doc.createProcessingInstruction(target, data));
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void comment(char[] ch, int start, int length) throws DOMException, SAXException {
        append(this.m_doc.createComment(new String(ch, start, length)));
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startCDATA() throws DOMException, SAXException {
        this.m_inCData = true;
        append(this.m_doc.createCDATASection(""));
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endCDATA() throws SAXException {
        this.m_inCData = false;
    }

    public void cdata(char[] ch, int start, int length) throws SAXException {
        if (isOutsideDocElem() && XMLCharacterRecognizer.isWhiteSpace(ch, start, length)) {
            return;
        }
        String s2 = new String(ch, start, length);
        CDATASection section = (CDATASection) this.m_currentNode.getLastChild();
        section.appendData(s2);
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startDTD(String name, String publicId, String systemId) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endDTD() throws SAXException {
    }

    @Override // org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
    }

    @Override // org.xml.sax.ContentHandler
    public void endPrefixMapping(String prefix) throws SAXException {
    }

    @Override // org.xml.sax.ContentHandler
    public void skippedEntity(String name) throws SAXException {
    }
}
