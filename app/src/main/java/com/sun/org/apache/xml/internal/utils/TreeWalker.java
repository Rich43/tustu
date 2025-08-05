package com.sun.org.apache.xml.internal.utils;

import com.sun.org.apache.xml.internal.dtm.ref.dom2dtm.DOM2DTM;
import javax.xml.transform.Result;
import jdk.internal.dynalink.CallSiteDescriptor;
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

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/TreeWalker.class */
public class TreeWalker {
    private ContentHandler m_contentHandler;
    private LocatorImpl m_locator;
    boolean nextIsRaw;

    public ContentHandler getContentHandler() {
        return this.m_contentHandler;
    }

    public void setContentHandler(ContentHandler ch) {
        this.m_contentHandler = ch;
    }

    public TreeWalker(ContentHandler contentHandler, String systemId) {
        this.m_contentHandler = null;
        this.m_locator = new LocatorImpl();
        this.nextIsRaw = false;
        this.m_contentHandler = contentHandler;
        if (this.m_contentHandler != null) {
            this.m_contentHandler.setDocumentLocator(this.m_locator);
        }
        if (systemId != null) {
            this.m_locator.setSystemId(systemId);
        }
    }

    public TreeWalker(ContentHandler contentHandler) {
        this(contentHandler, null);
    }

    public void traverse(Node pos) throws SAXException {
        this.m_contentHandler.startDocument();
        traverseFragment(pos);
        this.m_contentHandler.endDocument();
    }

    public void traverseFragment(Node pos) throws SAXException {
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
        if (this.m_contentHandler instanceof DOM2DTM.CharacterNodeHandler) {
            ((DOM2DTM.CharacterNodeHandler) this.m_contentHandler).characters(node);
        } else {
            String data = ((Text) node).getData();
            this.m_contentHandler.characters(data.toCharArray(), 0, data.length());
        }
    }

    protected void startNode(Node node) throws SAXException {
        if (this.m_contentHandler instanceof NodeConsumer) {
            ((NodeConsumer) this.m_contentHandler).setOriginatingNode(node);
        }
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
                NamedNodeMap atts = ((Element) node).getAttributes();
                int nAttrs = atts.getLength();
                for (int i2 = 0; i2 < nAttrs; i2++) {
                    Node attr = atts.item(i2);
                    String attrName = attr.getNodeName();
                    if (attrName.equals("xmlns") || attrName.startsWith("xmlns:")) {
                        int index = attrName.indexOf(CallSiteDescriptor.TOKEN_DELIMITER);
                        String prefix = index < 0 ? "" : attrName.substring(index + 1);
                        this.m_contentHandler.startPrefixMapping(prefix, attr.getNodeValue());
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
        switch (node.getNodeType()) {
            case 1:
                String ns = DOM2Helper.getNamespaceOfNode(node);
                if (null == ns) {
                    ns = "";
                }
                this.m_contentHandler.endElement(ns, DOM2Helper.getLocalNameOfNode(node), node.getNodeName());
                NamedNodeMap atts = ((Element) node).getAttributes();
                int nAttrs = atts.getLength();
                for (int i2 = 0; i2 < nAttrs; i2++) {
                    Node attr = atts.item(i2);
                    String attrName = attr.getNodeName();
                    if (attrName.equals("xmlns") || attrName.startsWith("xmlns:")) {
                        int index = attrName.indexOf(CallSiteDescriptor.TOKEN_DELIMITER);
                        String prefix = index < 0 ? "" : attrName.substring(index + 1);
                        this.m_contentHandler.endPrefixMapping(prefix);
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
