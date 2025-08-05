package com.sun.org.apache.xerces.internal.dom;

import com.sun.org.apache.xerces.internal.dom.ParentNode;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.MissingResourceException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/NodeImpl.class */
public abstract class NodeImpl implements Node, NodeList, EventTarget, Cloneable, Serializable {
    public static final short TREE_POSITION_PRECEDING = 1;
    public static final short TREE_POSITION_FOLLOWING = 2;
    public static final short TREE_POSITION_ANCESTOR = 4;
    public static final short TREE_POSITION_DESCENDANT = 8;
    public static final short TREE_POSITION_EQUIVALENT = 16;
    public static final short TREE_POSITION_SAME_NODE = 32;
    public static final short TREE_POSITION_DISCONNECTED = 0;
    public static final short DOCUMENT_POSITION_DISCONNECTED = 1;
    public static final short DOCUMENT_POSITION_PRECEDING = 2;
    public static final short DOCUMENT_POSITION_FOLLOWING = 4;
    public static final short DOCUMENT_POSITION_CONTAINS = 8;
    public static final short DOCUMENT_POSITION_IS_CONTAINED = 16;
    public static final short DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC = 32;
    static final long serialVersionUID = -6316591992167219696L;
    public static final short ELEMENT_DEFINITION_NODE = 21;
    protected NodeImpl ownerNode;
    protected short flags;
    protected static final short READONLY = 1;
    protected static final short SYNCDATA = 2;
    protected static final short SYNCCHILDREN = 4;
    protected static final short OWNED = 8;
    protected static final short FIRSTCHILD = 16;
    protected static final short SPECIFIED = 32;
    protected static final short IGNORABLEWS = 64;
    protected static final short HASSTRING = 128;
    protected static final short NORMALIZED = 256;
    protected static final short ID = 512;

    @Override // org.w3c.dom.Node
    public abstract short getNodeType();

    @Override // org.w3c.dom.Node
    public abstract String getNodeName();

    protected NodeImpl(CoreDocumentImpl ownerDocument) {
        this.ownerNode = ownerDocument;
    }

    public NodeImpl() {
    }

    @Override // org.w3c.dom.Node
    public String getNodeValue() throws DOMException {
        return null;
    }

    @Override // org.w3c.dom.Node
    public void setNodeValue(String x2) throws DOMException {
    }

    @Override // org.w3c.dom.Node
    public Node appendChild(Node newChild) throws DOMException {
        return insertBefore(newChild, null);
    }

    @Override // org.w3c.dom.Node
    public Node cloneNode(boolean deep) {
        if (needsSyncData()) {
            synchronizeData();
        }
        try {
            NodeImpl newnode = (NodeImpl) clone();
            newnode.ownerNode = ownerDocument();
            newnode.isOwned(false);
            newnode.isReadOnly(false);
            ownerDocument().callUserDataHandlers(this, newnode, (short) 1);
            return newnode;
        } catch (CloneNotSupportedException e2) {
            throw new RuntimeException("**Internal Error**" + ((Object) e2));
        }
    }

    @Override // org.w3c.dom.Node
    public Document getOwnerDocument() {
        if (isOwned()) {
            return this.ownerNode.ownerDocument();
        }
        return (Document) this.ownerNode;
    }

    CoreDocumentImpl ownerDocument() {
        if (isOwned()) {
            return this.ownerNode.ownerDocument();
        }
        return (CoreDocumentImpl) this.ownerNode;
    }

    void setOwnerDocument(CoreDocumentImpl doc) {
        if (needsSyncData()) {
            synchronizeData();
        }
        if (!isOwned()) {
            this.ownerNode = doc;
        }
    }

    protected int getNodeNumber() {
        CoreDocumentImpl cd = (CoreDocumentImpl) getOwnerDocument();
        int nodeNumber = cd.getNodeNumber(this);
        return nodeNumber;
    }

    @Override // org.w3c.dom.Node
    public Node getParentNode() {
        return null;
    }

    NodeImpl parentNode() {
        return null;
    }

    @Override // org.w3c.dom.Node
    public Node getNextSibling() {
        return null;
    }

    @Override // org.w3c.dom.Node
    public Node getPreviousSibling() {
        return null;
    }

    ChildNode previousSibling() {
        return null;
    }

    @Override // org.w3c.dom.Node
    public NamedNodeMap getAttributes() {
        return null;
    }

    @Override // org.w3c.dom.Node
    public boolean hasAttributes() {
        return false;
    }

    @Override // org.w3c.dom.Node
    public boolean hasChildNodes() {
        return false;
    }

    @Override // org.w3c.dom.Node
    public NodeList getChildNodes() {
        return this;
    }

    @Override // org.w3c.dom.Node
    public Node getFirstChild() {
        return null;
    }

    @Override // org.w3c.dom.Node
    public Node getLastChild() {
        return null;
    }

    @Override // org.w3c.dom.Node
    public Node insertBefore(Node newChild, Node refChild) throws DOMException {
        throw new DOMException((short) 3, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "HIERARCHY_REQUEST_ERR", null));
    }

    @Override // org.w3c.dom.Node
    public Node removeChild(Node oldChild) throws DOMException {
        throw new DOMException((short) 8, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_FOUND_ERR", null));
    }

    @Override // org.w3c.dom.Node
    public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
        throw new DOMException((short) 3, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "HIERARCHY_REQUEST_ERR", null));
    }

    public int getLength() {
        return 0;
    }

    public Node item(int index) {
        return null;
    }

    @Override // org.w3c.dom.Node
    public void normalize() {
    }

    @Override // org.w3c.dom.Node
    public boolean isSupported(String feature, String version) {
        return ownerDocument().getImplementation().hasFeature(feature, version);
    }

    @Override // org.w3c.dom.Node
    public String getNamespaceURI() {
        return null;
    }

    @Override // org.w3c.dom.Node
    public String getPrefix() {
        return null;
    }

    @Override // org.w3c.dom.Node
    public void setPrefix(String prefix) throws DOMException {
        throw new DOMException((short) 14, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NAMESPACE_ERR", null));
    }

    @Override // org.w3c.dom.Node
    public String getLocalName() {
        return null;
    }

    @Override // org.w3c.dom.events.EventTarget
    public void addEventListener(String type, EventListener listener, boolean useCapture) {
        ownerDocument().addEventListener(this, type, listener, useCapture);
    }

    @Override // org.w3c.dom.events.EventTarget
    public void removeEventListener(String type, EventListener listener, boolean useCapture) {
        ownerDocument().removeEventListener(this, type, listener, useCapture);
    }

    @Override // org.w3c.dom.events.EventTarget
    public boolean dispatchEvent(Event event) {
        return ownerDocument().dispatchEvent(this, event);
    }

    @Override // org.w3c.dom.Node
    public String getBaseURI() {
        return null;
    }

    public short compareTreePosition(Node other) {
        if (this == other) {
            return (short) 48;
        }
        short thisType = getNodeType();
        short otherType = other.getNodeType();
        if (thisType == 6 || thisType == 12 || otherType == 6 || otherType == 12) {
            return (short) 0;
        }
        Node thisAncestor = this;
        Node otherAncestor = other;
        int thisDepth = 0;
        int otherDepth = 0;
        Node parentNode = this;
        while (true) {
            Node node = parentNode;
            if (node != null) {
                thisDepth++;
                if (node == other) {
                    return (short) 5;
                }
                thisAncestor = node;
                parentNode = node.getParentNode();
            } else {
                Node parentNode2 = other;
                while (true) {
                    Node node2 = parentNode2;
                    if (node2 != null) {
                        otherDepth++;
                        if (node2 == this) {
                            return (short) 10;
                        }
                        otherAncestor = node2;
                        parentNode2 = node2.getParentNode();
                    } else {
                        Node thisNode = this;
                        Node otherNode = other;
                        int thisAncestorType = thisAncestor.getNodeType();
                        int otherAncestorType = otherAncestor.getNodeType();
                        if (thisAncestorType == 2) {
                            thisNode = ((AttrImpl) thisAncestor).getOwnerElement();
                        }
                        if (otherAncestorType == 2) {
                            otherNode = ((AttrImpl) otherAncestor).getOwnerElement();
                        }
                        if (thisAncestorType == 2 && otherAncestorType == 2 && thisNode == otherNode) {
                            return (short) 16;
                        }
                        if (thisAncestorType == 2) {
                            thisDepth = 0;
                            Node parentNode3 = thisNode;
                            while (true) {
                                Node node3 = parentNode3;
                                if (node3 == null) {
                                    break;
                                }
                                thisDepth++;
                                if (node3 == otherNode) {
                                    return (short) 1;
                                }
                                thisAncestor = node3;
                                parentNode3 = node3.getParentNode();
                            }
                        }
                        if (otherAncestorType == 2) {
                            otherDepth = 0;
                            Node parentNode4 = otherNode;
                            while (true) {
                                Node node4 = parentNode4;
                                if (node4 == null) {
                                    break;
                                }
                                otherDepth++;
                                if (node4 == thisNode) {
                                    return (short) 2;
                                }
                                otherAncestor = node4;
                                parentNode4 = node4.getParentNode();
                            }
                        }
                        if (thisAncestor != otherAncestor) {
                            return (short) 0;
                        }
                        if (thisDepth > otherDepth) {
                            for (int i2 = 0; i2 < thisDepth - otherDepth; i2++) {
                                thisNode = thisNode.getParentNode();
                            }
                            if (thisNode == otherNode) {
                                return (short) 1;
                            }
                        } else {
                            for (int i3 = 0; i3 < otherDepth - thisDepth; i3++) {
                                otherNode = otherNode.getParentNode();
                            }
                            if (otherNode == thisNode) {
                                return (short) 2;
                            }
                        }
                        Node thisNodeP = thisNode.getParentNode();
                        Node parentNode5 = otherNode.getParentNode();
                        while (true) {
                            Node otherNodeP = parentNode5;
                            if (thisNodeP == otherNodeP) {
                                break;
                            }
                            thisNode = thisNodeP;
                            otherNode = otherNodeP;
                            thisNodeP = thisNodeP.getParentNode();
                            parentNode5 = otherNodeP.getParentNode();
                        }
                        Node firstChild = thisNodeP.getFirstChild();
                        while (true) {
                            Node current = firstChild;
                            if (current != null) {
                                if (current == otherNode) {
                                    return (short) 1;
                                }
                                if (current != thisNode) {
                                    firstChild = current.getNextSibling();
                                } else {
                                    return (short) 2;
                                }
                            } else {
                                return (short) 0;
                            }
                        }
                    }
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.w3c.dom.Node
    public short compareDocumentPosition(Node other) throws DOMException, MissingResourceException {
        Document thisOwnerDoc;
        Document otherOwnerDoc;
        if (this == other) {
            return (short) 0;
        }
        try {
            if (getNodeType() == 9) {
                thisOwnerDoc = (Document) this;
            } else {
                thisOwnerDoc = getOwnerDocument();
            }
            if (other.getNodeType() == 9) {
                otherOwnerDoc = (Document) other;
            } else {
                otherOwnerDoc = other.getOwnerDocument();
            }
            if (thisOwnerDoc != otherOwnerDoc && thisOwnerDoc != null && otherOwnerDoc != null) {
                int otherDocNum = ((CoreDocumentImpl) otherOwnerDoc).getNodeNumber();
                int thisDocNum = ((CoreDocumentImpl) thisOwnerDoc).getNodeNumber();
                if (otherDocNum > thisDocNum) {
                    return (short) 37;
                }
                return (short) 35;
            }
            Node thisAncestor = this;
            Node otherAncestor = other;
            int thisDepth = 0;
            int otherDepth = 0;
            Node parentNode = this;
            while (true) {
                Node node = parentNode;
                if (node != null) {
                    thisDepth++;
                    if (node == other) {
                        return (short) 10;
                    }
                    thisAncestor = node;
                    parentNode = node.getParentNode();
                } else {
                    Node parentNode2 = other;
                    while (true) {
                        Node node2 = parentNode2;
                        if (node2 != null) {
                            otherDepth++;
                            if (node2 == this) {
                                return (short) 20;
                            }
                            otherAncestor = node2;
                            parentNode2 = node2.getParentNode();
                        } else {
                            int thisAncestorType = thisAncestor.getNodeType();
                            int otherAncestorType = otherAncestor.getNodeType();
                            Node thisNode = this;
                            Node otherNode = other;
                            switch (thisAncestorType) {
                                case 2:
                                    thisNode = ((AttrImpl) thisAncestor).getOwnerElement();
                                    if (otherAncestorType == 2) {
                                        otherNode = ((AttrImpl) otherAncestor).getOwnerElement();
                                        if (otherNode == thisNode) {
                                            if (((NamedNodeMapImpl) thisNode.getAttributes()).precedes(other, this)) {
                                                return (short) 34;
                                            }
                                            return (short) 36;
                                        }
                                    }
                                    thisDepth = 0;
                                    Node parentNode3 = thisNode;
                                    while (true) {
                                        Node node3 = parentNode3;
                                        if (node3 == null) {
                                            break;
                                        } else {
                                            thisDepth++;
                                            if (node3 == otherNode) {
                                                return (short) 10;
                                            }
                                            thisAncestor = node3;
                                            parentNode3 = node3.getParentNode();
                                        }
                                    }
                                case 6:
                                case 12:
                                    DocumentType container = thisOwnerDoc.getDoctype();
                                    if (container == otherAncestor) {
                                        return (short) 10;
                                    }
                                    switch (otherAncestorType) {
                                        case 6:
                                        case 12:
                                            if (thisAncestorType != otherAncestorType) {
                                                return thisAncestorType > otherAncestorType ? (short) 2 : (short) 4;
                                            }
                                            if (thisAncestorType == 12) {
                                                if (((NamedNodeMapImpl) container.getNotations()).precedes(otherAncestor, thisAncestor)) {
                                                    return (short) 34;
                                                }
                                                return (short) 36;
                                            }
                                            if (((NamedNodeMapImpl) container.getEntities()).precedes(otherAncestor, thisAncestor)) {
                                                return (short) 34;
                                            }
                                            return (short) 36;
                                        default:
                                            Document document = thisOwnerDoc;
                                            thisAncestor = document;
                                            thisNode = document;
                                            break;
                                    }
                                case 10:
                                    if (otherNode == thisOwnerDoc) {
                                        return (short) 10;
                                    }
                                    if (thisOwnerDoc != null && thisOwnerDoc == otherOwnerDoc) {
                                        return (short) 4;
                                    }
                                    break;
                            }
                            switch (otherAncestorType) {
                                case 2:
                                    otherDepth = 0;
                                    otherNode = ((AttrImpl) otherAncestor).getOwnerElement();
                                    Node parentNode4 = otherNode;
                                    while (true) {
                                        Node node4 = parentNode4;
                                        if (node4 == null) {
                                            break;
                                        } else {
                                            otherDepth++;
                                            if (node4 == thisNode) {
                                                return (short) 20;
                                            }
                                            otherAncestor = node4;
                                            parentNode4 = node4.getParentNode();
                                        }
                                    }
                                case 6:
                                case 12:
                                    if (thisOwnerDoc.getDoctype() == this) {
                                        return (short) 20;
                                    }
                                    Document document2 = thisOwnerDoc;
                                    otherAncestor = document2;
                                    otherNode = document2;
                                    break;
                                case 10:
                                    if (thisNode == otherOwnerDoc) {
                                        return (short) 20;
                                    }
                                    if (otherOwnerDoc != null && thisOwnerDoc == otherOwnerDoc) {
                                        return (short) 2;
                                    }
                                    break;
                            }
                            if (thisAncestor != otherAncestor) {
                                int thisAncestorNum = ((NodeImpl) thisAncestor).getNodeNumber();
                                int otherAncestorNum = ((NodeImpl) otherAncestor).getNodeNumber();
                                if (thisAncestorNum > otherAncestorNum) {
                                    return (short) 37;
                                }
                                return (short) 35;
                            }
                            if (thisDepth > otherDepth) {
                                for (int i2 = 0; i2 < thisDepth - otherDepth; i2++) {
                                    thisNode = thisNode.getParentNode();
                                }
                                if (thisNode == otherNode) {
                                    return (short) 2;
                                }
                            } else {
                                for (int i3 = 0; i3 < otherDepth - thisDepth; i3++) {
                                    otherNode = otherNode.getParentNode();
                                }
                                if (otherNode == thisNode) {
                                    return (short) 4;
                                }
                            }
                            Node thisNodeP = thisNode.getParentNode();
                            Node parentNode5 = otherNode.getParentNode();
                            while (true) {
                                Node otherNodeP = parentNode5;
                                if (thisNodeP != otherNodeP) {
                                    thisNode = thisNodeP;
                                    otherNode = otherNodeP;
                                    thisNodeP = thisNodeP.getParentNode();
                                    parentNode5 = otherNodeP.getParentNode();
                                } else {
                                    Node firstChild = thisNodeP.getFirstChild();
                                    while (true) {
                                        Node current = firstChild;
                                        if (current != null) {
                                            if (current == otherNode) {
                                                return (short) 2;
                                            }
                                            if (current != thisNode) {
                                                firstChild = current.getNextSibling();
                                            } else {
                                                return (short) 4;
                                            }
                                        } else {
                                            return (short) 0;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (ClassCastException e2) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_SUPPORTED_ERR", null);
            throw new DOMException((short) 9, msg);
        }
    }

    @Override // org.w3c.dom.Node
    public String getTextContent() throws DOMException {
        return getNodeValue();
    }

    void getTextContent(StringBuffer buf) throws DOMException {
        String content = getNodeValue();
        if (content != null) {
            buf.append(content);
        }
    }

    @Override // org.w3c.dom.Node
    public void setTextContent(String textContent) throws DOMException {
        setNodeValue(textContent);
    }

    @Override // org.w3c.dom.Node
    public boolean isSameNode(Node other) {
        return this == other;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.w3c.dom.Node
    public boolean isDefaultNamespace(String namespaceURI) throws DOMException {
        short type = getNodeType();
        switch (type) {
            case 1:
                String namespace = getNamespaceURI();
                String prefix = getPrefix();
                if (prefix == null || prefix.length() == 0) {
                    if (namespaceURI == null) {
                        return namespace == namespaceURI;
                    }
                    return namespaceURI.equals(namespace);
                }
                if (hasAttributes()) {
                    ElementImpl elem = (ElementImpl) this;
                    NodeImpl attr = (NodeImpl) elem.getAttributeNodeNS("http://www.w3.org/2000/xmlns/", "xmlns");
                    if (attr != null) {
                        String value = attr.getNodeValue();
                        if (namespaceURI == null) {
                            return namespace == value;
                        }
                        return namespaceURI.equals(value);
                    }
                }
                NodeImpl ancestor = (NodeImpl) getElementAncestor(this);
                if (ancestor != null) {
                    return ancestor.isDefaultNamespace(namespaceURI);
                }
                return false;
            case 2:
                if (this.ownerNode.getNodeType() == 1) {
                    return this.ownerNode.isDefaultNamespace(namespaceURI);
                }
                return false;
            case 3:
            case 4:
            case 5:
            case 7:
            case 8:
            default:
                NodeImpl ancestor2 = (NodeImpl) getElementAncestor(this);
                if (ancestor2 != null) {
                    return ancestor2.isDefaultNamespace(namespaceURI);
                }
                return false;
            case 6:
            case 10:
            case 11:
            case 12:
                return false;
            case 9:
                return ((NodeImpl) ((Document) this).getDocumentElement()).isDefaultNamespace(namespaceURI);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.w3c.dom.Node
    public String lookupPrefix(String namespaceURI) {
        if (namespaceURI == null) {
            return null;
        }
        short type = getNodeType();
        switch (type) {
            case 1:
                getNamespaceURI();
                return lookupNamespacePrefix(namespaceURI, (ElementImpl) this);
            case 2:
                if (this.ownerNode.getNodeType() == 1) {
                    return this.ownerNode.lookupPrefix(namespaceURI);
                }
                return null;
            case 3:
            case 4:
            case 5:
            case 7:
            case 8:
            default:
                NodeImpl ancestor = (NodeImpl) getElementAncestor(this);
                if (ancestor != null) {
                    return ancestor.lookupPrefix(namespaceURI);
                }
                return null;
            case 6:
            case 10:
            case 11:
            case 12:
                return null;
            case 9:
                return ((NodeImpl) ((Document) this).getDocumentElement()).lookupPrefix(namespaceURI);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.w3c.dom.Node
    public String lookupNamespaceURI(String specifiedPrefix) throws DOMException {
        String namespace;
        short type = getNodeType();
        switch (type) {
            case 1:
                namespace = getNamespaceURI();
                String prefix = getPrefix();
                if (namespace != null) {
                    if (specifiedPrefix != null || prefix != specifiedPrefix) {
                        if (prefix != null && prefix.equals(specifiedPrefix)) {
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (hasAttributes()) {
                    NamedNodeMap map = getAttributes();
                    int length = map.getLength();
                    for (int i2 = 0; i2 < length; i2++) {
                        Node attr = map.item(i2);
                        String attrPrefix = attr.getPrefix();
                        String value = attr.getNodeValue();
                        String namespace2 = attr.getNamespaceURI();
                        if (namespace2 != null && namespace2.equals("http://www.w3.org/2000/xmlns/")) {
                            if (specifiedPrefix == null && attr.getNodeName().equals("xmlns")) {
                                break;
                            } else if (attrPrefix != null && attrPrefix.equals("xmlns") && attr.getLocalName().equals(specifiedPrefix)) {
                                break;
                            }
                        }
                    }
                }
                NodeImpl ancestor = (NodeImpl) getElementAncestor(this);
                if (ancestor != null) {
                    break;
                }
                break;
            case 2:
                if (this.ownerNode.getNodeType() == 1) {
                    break;
                }
                break;
            case 3:
            case 4:
            case 5:
            case 7:
            case 8:
            default:
                NodeImpl ancestor2 = (NodeImpl) getElementAncestor(this);
                if (ancestor2 != null) {
                    break;
                }
                break;
            case 6:
            case 10:
            case 11:
            case 12:
                break;
            case 9:
                break;
        }
        return namespace;
    }

    Node getElementAncestor(Node currentNode) {
        Node parent = currentNode.getParentNode();
        if (parent != null) {
            short type = parent.getNodeType();
            if (type == 1) {
                return parent;
            }
            return getElementAncestor(parent);
        }
        return null;
    }

    String lookupNamespacePrefix(String namespaceURI, ElementImpl el) throws DOMException {
        String localname;
        String foundNamespace;
        String foundNamespace2;
        String namespace = getNamespaceURI();
        String prefix = getPrefix();
        if (namespace != null && namespace.equals(namespaceURI) && prefix != null && (foundNamespace2 = el.lookupNamespaceURI(prefix)) != null && foundNamespace2.equals(namespaceURI)) {
            return prefix;
        }
        if (hasAttributes()) {
            NamedNodeMap map = getAttributes();
            int length = map.getLength();
            for (int i2 = 0; i2 < length; i2++) {
                Node attr = map.item(i2);
                String attrPrefix = attr.getPrefix();
                String value = attr.getNodeValue();
                String namespace2 = attr.getNamespaceURI();
                if (namespace2 != null && namespace2.equals("http://www.w3.org/2000/xmlns/") && ((attr.getNodeName().equals("xmlns") || (attrPrefix != null && attrPrefix.equals("xmlns") && value.equals(namespaceURI))) && (foundNamespace = el.lookupNamespaceURI((localname = attr.getLocalName()))) != null && foundNamespace.equals(namespaceURI))) {
                    return localname;
                }
            }
        }
        NodeImpl ancestor = (NodeImpl) getElementAncestor(this);
        if (ancestor != null) {
            return ancestor.lookupNamespacePrefix(namespaceURI, el);
        }
        return null;
    }

    @Override // org.w3c.dom.Node
    public boolean isEqualNode(Node arg) {
        if (arg == this) {
            return true;
        }
        if (arg.getNodeType() != getNodeType()) {
            return false;
        }
        if (getNodeName() == null) {
            if (arg.getNodeName() != null) {
                return false;
            }
        } else if (!getNodeName().equals(arg.getNodeName())) {
            return false;
        }
        if (getLocalName() == null) {
            if (arg.getLocalName() != null) {
                return false;
            }
        } else if (!getLocalName().equals(arg.getLocalName())) {
            return false;
        }
        if (getNamespaceURI() == null) {
            if (arg.getNamespaceURI() != null) {
                return false;
            }
        } else if (!getNamespaceURI().equals(arg.getNamespaceURI())) {
            return false;
        }
        if (getPrefix() == null) {
            if (arg.getPrefix() != null) {
                return false;
            }
        } else if (!getPrefix().equals(arg.getPrefix())) {
            return false;
        }
        if (getNodeValue() == null) {
            if (arg.getNodeValue() != null) {
                return false;
            }
            return true;
        }
        if (!getNodeValue().equals(arg.getNodeValue())) {
            return false;
        }
        return true;
    }

    @Override // org.w3c.dom.Node
    public Object getFeature(String feature, String version) {
        if (isSupported(feature, version)) {
            return this;
        }
        return null;
    }

    @Override // org.w3c.dom.Node
    public Object setUserData(String key, Object data, UserDataHandler handler) {
        return ownerDocument().setUserData(this, key, data, handler);
    }

    @Override // org.w3c.dom.Node
    public Object getUserData(String key) {
        return ownerDocument().getUserData(this, key);
    }

    protected Map<String, ParentNode.UserDataRecord> getUserDataRecord() {
        return ownerDocument().getUserDataRecord(this);
    }

    public void setReadOnly(boolean readOnly, boolean deep) {
        if (needsSyncData()) {
            synchronizeData();
        }
        isReadOnly(readOnly);
    }

    public boolean getReadOnly() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return isReadOnly();
    }

    public void setUserData(Object data) {
        ownerDocument().setUserData(this, data);
    }

    public Object getUserData() {
        return ownerDocument().getUserData(this);
    }

    protected void changed() {
        ownerDocument().changed();
    }

    protected int changes() {
        return ownerDocument().changes();
    }

    protected void synchronizeData() {
        needsSyncData(false);
    }

    protected Node getContainer() {
        return null;
    }

    final boolean isReadOnly() {
        return (this.flags & 1) != 0;
    }

    final void isReadOnly(boolean value) {
        this.flags = (short) (value ? this.flags | 1 : this.flags & (-2));
    }

    final boolean needsSyncData() {
        return (this.flags & 2) != 0;
    }

    final void needsSyncData(boolean value) {
        this.flags = (short) (value ? this.flags | 2 : this.flags & (-3));
    }

    final boolean needsSyncChildren() {
        return (this.flags & 4) != 0;
    }

    public final void needsSyncChildren(boolean value) {
        this.flags = (short) (value ? this.flags | 4 : this.flags & (-5));
    }

    final boolean isOwned() {
        return (this.flags & 8) != 0;
    }

    final void isOwned(boolean value) {
        this.flags = (short) (value ? this.flags | 8 : this.flags & (-9));
    }

    final boolean isFirstChild() {
        return (this.flags & 16) != 0;
    }

    final void isFirstChild(boolean value) {
        this.flags = (short) (value ? this.flags | 16 : this.flags & (-17));
    }

    final boolean isSpecified() {
        return (this.flags & 32) != 0;
    }

    final void isSpecified(boolean value) {
        this.flags = (short) (value ? this.flags | 32 : this.flags & (-33));
    }

    final boolean internalIsIgnorableWhitespace() {
        return (this.flags & 64) != 0;
    }

    final void isIgnorableWhitespace(boolean value) {
        this.flags = (short) (value ? this.flags | 64 : this.flags & (-65));
    }

    final boolean hasStringValue() {
        return (this.flags & 128) != 0;
    }

    final void hasStringValue(boolean value) {
        this.flags = (short) (value ? this.flags | 128 : this.flags & (-129));
    }

    final boolean isNormalized() {
        return (this.flags & 256) != 0;
    }

    final void isNormalized(boolean value) {
        if (!value && isNormalized() && this.ownerNode != null) {
            this.ownerNode.isNormalized(false);
        }
        this.flags = (short) (value ? this.flags | 256 : this.flags & (-257));
    }

    final boolean isIdAttribute() {
        return (this.flags & 512) != 0;
    }

    final void isIdAttribute(boolean value) {
        this.flags = (short) (value ? this.flags | 512 : this.flags & (-513));
    }

    public String toString() {
        return "[" + getNodeName() + ": " + getNodeValue() + "]";
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        if (needsSyncData()) {
            synchronizeData();
        }
        out.defaultWriteObject();
    }
}
