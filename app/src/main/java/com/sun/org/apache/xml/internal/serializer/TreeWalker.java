package com.sun.org.apache.xml.internal.serializer;

import com.sun.org.apache.xml.internal.utils.AttList;
import com.sun.org.apache.xml.internal.utils.DOM2Helper;
import javax.xml.transform.Result;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.LocatorImpl;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/TreeWalker.class */
public final class TreeWalker {
    private final ContentHandler m_contentHandler;
    private final SerializationHandler m_Serializer;
    private final LocatorImpl m_locator;
    boolean nextIsRaw;

    public ContentHandler getContentHandler() {
        return this.m_contentHandler;
    }

    public TreeWalker(ContentHandler ch) {
        this(ch, null);
    }

    public TreeWalker(ContentHandler contentHandler, String systemId) {
        this.m_locator = new LocatorImpl();
        this.nextIsRaw = false;
        this.m_contentHandler = contentHandler;
        if (this.m_contentHandler instanceof SerializationHandler) {
            this.m_Serializer = (SerializationHandler) this.m_contentHandler;
        } else {
            this.m_Serializer = null;
        }
        this.m_contentHandler.setDocumentLocator(this.m_locator);
        if (systemId != null) {
            this.m_locator.setSystemId(systemId);
        }
    }

    public void traverse(Node pos) throws SAXException {
        this.m_contentHandler.startDocument();
        while (null != pos) {
            startNode(pos);
            Node nextNode = pos.getFirstChild();
            while (null == nextNode) {
                endNode(pos);
                if (pos.equals(pos)) {
                    break;
                }
                nextNode = pos.getNextSibling();
                if (null == nextNode) {
                    pos = pos.getParentNode();
                    if (null == pos || pos.equals(pos)) {
                        if (null != pos) {
                            endNode(pos);
                        }
                        nextNode = null;
                    }
                }
                pos = nextNode;
            }
            pos = nextNode;
        }
        this.m_contentHandler.endDocument();
    }

    public void traverse(Node pos, Node top) throws SAXException {
        this.m_contentHandler.startDocument();
        while (null != pos) {
            startNode(pos);
            Node nextNode = pos.getFirstChild();
            while (null == nextNode) {
                endNode(pos);
                if (null == top || !top.equals(pos)) {
                    nextNode = pos.getNextSibling();
                    if (null == nextNode) {
                        pos = pos.getParentNode();
                        if (null == pos || (null != top && top.equals(pos))) {
                            nextNode = null;
                            break;
                        }
                    }
                }
            }
            pos = nextNode;
        }
        this.m_contentHandler.endDocument();
    }

    private final void dispatachChars(Node node) throws SAXException {
        if (this.m_Serializer != null) {
            this.m_Serializer.characters(node);
        } else {
            String data = ((Text) node).getData();
            this.m_contentHandler.characters(data.toCharArray(), 0, data.length());
        }
    }

    protected void startNode(Node node) throws SAXException {
        String prefix;
        if (node instanceof Locator) {
            Locator loc = (Locator) node;
            this.m_locator.setColumnNumber(loc.getColumnNumber());
            this.m_locator.setLineNumber(loc.getLineNumber());
            this.m_locator.setPublicId(loc.getPublicId());
            this.m_locator.setSystemId(loc.getSystemId());
        } else {
            this.m_locator.setColumnNumber(0);
            this.m_locator.setLineNumber(0);
        }
        switch (node.getNodeType()) {
            case 1:
                Element elem_node = (Element) node;
                String uri = elem_node.getNamespaceURI();
                if (uri != null) {
                    String prefix2 = elem_node.getPrefix();
                    if (prefix2 == null) {
                        prefix2 = "";
                    }
                    this.m_contentHandler.startPrefixMapping(prefix2, uri);
                }
                NamedNodeMap atts = elem_node.getAttributes();
                int nAttrs = atts.getLength();
                for (int i2 = 0; i2 < nAttrs; i2++) {
                    Node attr = atts.item(i2);
                    String attrName = attr.getNodeName();
                    int colon = attrName.indexOf(58);
                    if (attrName.equals("xmlns") || attrName.startsWith("xmlns:")) {
                        if (colon < 0) {
                            prefix = "";
                        } else {
                            prefix = attrName.substring(colon + 1);
                        }
                        this.m_contentHandler.startPrefixMapping(prefix, attr.getNodeValue());
                    } else if (colon > 0) {
                        String prefix3 = attrName.substring(0, colon);
                        String uri2 = attr.getNamespaceURI();
                        if (uri2 != null) {
                            this.m_contentHandler.startPrefixMapping(prefix3, uri2);
                        }
                    }
                }
                String ns = DOM2Helper.getNamespaceOfNode(node);
                if (null == ns) {
                    ns = "";
                }
                this.m_contentHandler.startElement(ns, DOM2Helper.getLocalNameOfNode(node), node.getNodeName(), new AttList(atts));
                break;
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
                EntityReference eref = (EntityReference) node;
                if (this.m_contentHandler instanceof LexicalHandler) {
                    ((LexicalHandler) this.m_contentHandler).startEntity(eref.getNodeName());
                    break;
                }
                break;
            case 7:
                ProcessingInstruction pi = (ProcessingInstruction) node;
                String name = pi.getNodeName();
                if (name.equals("xslt-next-is-raw")) {
                    this.nextIsRaw = true;
                    break;
                } else {
                    this.m_contentHandler.processingInstruction(pi.getNodeName(), pi.getData());
                    break;
                }
            case 8:
                String data = ((Comment) node).getData();
                if (this.m_contentHandler instanceof LexicalHandler) {
                    ((LexicalHandler) this.m_contentHandler).comment(data.toCharArray(), 0, data.length());
                    break;
                }
                break;
        }
    }

    protected void endNode(Node node) throws SAXException {
        String prefix;
        switch (node.getNodeType()) {
            case 1:
                String ns = DOM2Helper.getNamespaceOfNode(node);
                if (null == ns) {
                    ns = "";
                }
                this.m_contentHandler.endElement(ns, DOM2Helper.getLocalNameOfNode(node), node.getNodeName());
                if (this.m_Serializer == null) {
                    Element elem_node = (Element) node;
                    NamedNodeMap atts = elem_node.getAttributes();
                    int nAttrs = atts.getLength();
                    for (int i2 = nAttrs - 1; 0 <= i2; i2--) {
                        Node attr = atts.item(i2);
                        String attrName = attr.getNodeName();
                        int colon = attrName.indexOf(58);
                        if (attrName.equals("xmlns") || attrName.startsWith("xmlns:")) {
                            if (colon < 0) {
                                prefix = "";
                            } else {
                                prefix = attrName.substring(colon + 1);
                            }
                            this.m_contentHandler.endPrefixMapping(prefix);
                        } else if (colon > 0) {
                            this.m_contentHandler.endPrefixMapping(attrName.substring(0, colon));
                        }
                    }
                    String uri = elem_node.getNamespaceURI();
                    if (uri != null) {
                        String prefix2 = elem_node.getPrefix();
                        if (prefix2 == null) {
                            prefix2 = "";
                        }
                        this.m_contentHandler.endPrefixMapping(prefix2);
                        break;
                    }
                }
                break;
            case 5:
                EntityReference eref = (EntityReference) node;
                if (this.m_contentHandler instanceof LexicalHandler) {
                    LexicalHandler lh = (LexicalHandler) this.m_contentHandler;
                    lh.endEntity(eref.getNodeName());
                    break;
                }
                break;
        }
    }
}
