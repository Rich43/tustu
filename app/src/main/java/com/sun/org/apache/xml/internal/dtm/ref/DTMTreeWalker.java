package com.sun.org.apache.xml.internal.dtm.ref;

import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xml.internal.utils.NodeConsumer;
import com.sun.org.apache.xml.internal.utils.XMLString;
import javax.xml.transform.Result;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMTreeWalker.class */
public class DTMTreeWalker {
    private ContentHandler m_contentHandler;
    protected DTM m_dtm;
    boolean nextIsRaw;

    public void setDTM(DTM dtm) {
        this.m_dtm = dtm;
    }

    public ContentHandler getcontentHandler() {
        return this.m_contentHandler;
    }

    public void setcontentHandler(ContentHandler ch) {
        this.m_contentHandler = ch;
    }

    public DTMTreeWalker() {
        this.m_contentHandler = null;
        this.nextIsRaw = false;
    }

    public DTMTreeWalker(ContentHandler contentHandler, DTM dtm) {
        this.m_contentHandler = null;
        this.nextIsRaw = false;
        this.m_contentHandler = contentHandler;
        this.m_dtm = dtm;
    }

    public void traverse(int pos) throws SAXException {
        while (-1 != pos) {
            startNode(pos);
            int nextNode = this.m_dtm.getFirstChild(pos);
            while (-1 == nextNode) {
                endNode(pos);
                if (pos == pos) {
                    break;
                }
                nextNode = this.m_dtm.getNextSibling(pos);
                if (-1 == nextNode) {
                    pos = this.m_dtm.getParent(pos);
                    if (-1 == pos || pos == pos) {
                        if (-1 != pos) {
                            endNode(pos);
                        }
                        nextNode = -1;
                    }
                }
                pos = nextNode;
            }
            pos = nextNode;
        }
    }

    public void traverse(int pos, int top) throws SAXException {
        while (-1 != pos) {
            startNode(pos);
            int nextNode = this.m_dtm.getFirstChild(pos);
            while (-1 == nextNode) {
                endNode(pos);
                if (-1 == top || top != pos) {
                    nextNode = this.m_dtm.getNextSibling(pos);
                    if (-1 == nextNode) {
                        pos = this.m_dtm.getParent(pos);
                        if (-1 == pos || (-1 != top && top == pos)) {
                            nextNode = -1;
                            break;
                        }
                    }
                }
            }
            pos = nextNode;
        }
    }

    private final void dispatachChars(int node) throws SAXException {
        this.m_dtm.dispatchCharactersEvents(node, this.m_contentHandler, false);
    }

    protected void startNode(int node) throws SAXException {
        if (this.m_contentHandler instanceof NodeConsumer) {
        }
        switch (this.m_dtm.getNodeType(node)) {
            case 1:
                DTM dtm = this.m_dtm;
                int firstNamespaceNode = dtm.getFirstNamespaceNode(node, true);
                while (true) {
                    int nsn = firstNamespaceNode;
                    if (-1 != nsn) {
                        String prefix = dtm.getNodeNameX(nsn);
                        this.m_contentHandler.startPrefixMapping(prefix, dtm.getNodeValue(nsn));
                        firstNamespaceNode = dtm.getNextNamespaceNode(node, nsn, true);
                    } else {
                        String ns = dtm.getNamespaceURI(node);
                        if (null == ns) {
                            ns = "";
                        }
                        AttributesImpl attrs = new AttributesImpl();
                        int firstAttribute = dtm.getFirstAttribute(node);
                        while (true) {
                            int i2 = firstAttribute;
                            if (i2 != -1) {
                                attrs.addAttribute(dtm.getNamespaceURI(i2), dtm.getLocalName(i2), dtm.getNodeName(i2), "CDATA", dtm.getNodeValue(i2));
                                firstAttribute = dtm.getNextAttribute(i2);
                            } else {
                                this.m_contentHandler.startElement(ns, this.m_dtm.getLocalName(node), this.m_dtm.getNodeName(node), attrs);
                                break;
                            }
                        }
                    }
                }
            case 3:
                if (this.nextIsRaw) {
                    this.nextIsRaw = false;
                    this.m_contentHandler.processingInstruction(Result.PI_DISABLE_OUTPUT_ESCAPING, "");
                    dispatachChars(node);
                    this.m_contentHandler.processingInstruction(Result.PI_ENABLE_OUTPUT_ESCAPING, "");
                    break;
                } else {
                    dispatachChars(node);
                    break;
                }
            case 4:
                boolean isLexH = this.m_contentHandler instanceof LexicalHandler;
                LexicalHandler lh = isLexH ? (LexicalHandler) this.m_contentHandler : null;
                if (isLexH) {
                    lh.startCDATA();
                }
                dispatachChars(node);
                if (isLexH) {
                    lh.endCDATA();
                    break;
                }
                break;
            case 5:
                if (this.m_contentHandler instanceof LexicalHandler) {
                    ((LexicalHandler) this.m_contentHandler).startEntity(this.m_dtm.getNodeName(node));
                    break;
                }
                break;
            case 7:
                String name = this.m_dtm.getNodeName(node);
                if (name.equals("xslt-next-is-raw")) {
                    this.nextIsRaw = true;
                    break;
                } else {
                    this.m_contentHandler.processingInstruction(name, this.m_dtm.getNodeValue(node));
                    break;
                }
            case 8:
                XMLString data = this.m_dtm.getStringValue(node);
                if (this.m_contentHandler instanceof LexicalHandler) {
                    data.dispatchAsComment((LexicalHandler) this.m_contentHandler);
                    break;
                }
                break;
            case 9:
                this.m_contentHandler.startDocument();
                break;
        }
    }

    protected void endNode(int node) throws SAXException {
        switch (this.m_dtm.getNodeType(node)) {
            case 1:
                String ns = this.m_dtm.getNamespaceURI(node);
                if (null == ns) {
                    ns = "";
                }
                this.m_contentHandler.endElement(ns, this.m_dtm.getLocalName(node), this.m_dtm.getNodeName(node));
                int firstNamespaceNode = this.m_dtm.getFirstNamespaceNode(node, true);
                while (true) {
                    int nsn = firstNamespaceNode;
                    if (-1 == nsn) {
                        break;
                    } else {
                        String prefix = this.m_dtm.getNodeNameX(nsn);
                        this.m_contentHandler.endPrefixMapping(prefix);
                        firstNamespaceNode = this.m_dtm.getNextNamespaceNode(node, nsn, true);
                    }
                }
            case 5:
                if (this.m_contentHandler instanceof LexicalHandler) {
                    LexicalHandler lh = (LexicalHandler) this.m_contentHandler;
                    lh.endEntity(this.m_dtm.getNodeName(node));
                    break;
                }
                break;
            case 9:
                this.m_contentHandler.endDocument();
                break;
        }
    }
}
