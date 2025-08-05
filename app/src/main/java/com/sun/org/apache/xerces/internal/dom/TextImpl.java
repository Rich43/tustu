package com.sun.org.apache.xerces.internal.dom;

import com.sun.org.apache.xpath.internal.compiler.PsuedoNames;
import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/TextImpl.class */
public class TextImpl extends CharacterDataImpl implements CharacterData, Text {
    static final long serialVersionUID = -5294980852957403469L;

    public TextImpl() {
    }

    public TextImpl(CoreDocumentImpl ownerDoc, String data) {
        super(ownerDoc, data);
    }

    public void setValues(CoreDocumentImpl ownerDoc, String data) {
        this.flags = (short) 0;
        this.nextSibling = null;
        this.previousSibling = null;
        setOwnerDocument(ownerDoc);
        this.data = data;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public short getNodeType() {
        return (short) 3;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public String getNodeName() {
        return PsuedoNames.PSEUDONAME_TEXT;
    }

    public void setIgnorableWhitespace(boolean ignore) {
        if (needsSyncData()) {
            synchronizeData();
        }
        isIgnorableWhitespace(ignore);
    }

    @Override // org.w3c.dom.Text
    public boolean isElementContentWhitespace() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return internalIsIgnorableWhitespace();
    }

    @Override // org.w3c.dom.Text
    public String getWholeText() throws DOMException {
        if (needsSyncData()) {
            synchronizeData();
        }
        if (this.fBufferStr == null) {
            this.fBufferStr = new StringBuffer();
        } else {
            this.fBufferStr.setLength(0);
        }
        if (this.data != null && this.data.length() != 0) {
            this.fBufferStr.append(this.data);
        }
        getWholeTextBackward(getPreviousSibling(), this.fBufferStr, getParentNode());
        String temp = this.fBufferStr.toString();
        this.fBufferStr.setLength(0);
        getWholeTextForward(getNextSibling(), this.fBufferStr, getParentNode());
        return temp + this.fBufferStr.toString();
    }

    protected void insertTextContent(StringBuffer buf) throws DOMException {
        String content = getNodeValue();
        if (content != null) {
            buf.insert(0, content);
        }
    }

    private boolean getWholeTextForward(Node node, StringBuffer buffer, Node parent) throws DOMException {
        boolean inEntRef = false;
        if (parent != null) {
            inEntRef = parent.getNodeType() == 5;
        }
        while (node != null) {
            short type = node.getNodeType();
            if (type == 5) {
                if (getWholeTextForward(node.getFirstChild(), buffer, node)) {
                    return true;
                }
            } else if (type == 3 || type == 4) {
                ((NodeImpl) node).getTextContent(buffer);
            } else {
                return true;
            }
            node = node.getNextSibling();
        }
        if (inEntRef) {
            getWholeTextForward(parent.getNextSibling(), buffer, parent.getParentNode());
            return true;
        }
        return false;
    }

    private boolean getWholeTextBackward(Node node, StringBuffer buffer, Node parent) throws DOMException {
        boolean inEntRef = false;
        if (parent != null) {
            inEntRef = parent.getNodeType() == 5;
        }
        while (node != null) {
            short type = node.getNodeType();
            if (type == 5) {
                if (getWholeTextBackward(node.getLastChild(), buffer, node)) {
                    return true;
                }
            } else if (type == 3 || type == 4) {
                ((TextImpl) node).insertTextContent(buffer);
            } else {
                return true;
            }
            node = node.getPreviousSibling();
        }
        if (inEntRef) {
            getWholeTextBackward(parent.getPreviousSibling(), buffer, parent.getParentNode());
            return true;
        }
        return false;
    }

    @Override // org.w3c.dom.Text
    public Text replaceWholeText(String content) throws DOMException {
        Text currentNode;
        if (needsSyncData()) {
            synchronizeData();
        }
        Node parent = getParentNode();
        if (content == null || content.length() == 0) {
            if (parent != null) {
                parent.removeChild(this);
                return null;
            }
            return null;
        }
        if (ownerDocument().errorChecking) {
            if (!canModifyPrev(this)) {
                throw new DOMException((short) 7, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null));
            }
            if (!canModifyNext(this)) {
                throw new DOMException((short) 7, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null));
            }
        }
        if (isReadOnly()) {
            Text newNode = ownerDocument().createTextNode(content);
            if (parent != null) {
                parent.insertBefore(newNode, this);
                parent.removeChild(this);
                currentNode = newNode;
            } else {
                return newNode;
            }
        } else {
            setData(content);
            currentNode = this;
        }
        Node previousSibling = currentNode.getPreviousSibling();
        while (true) {
            Node prev = previousSibling;
            if (prev == null || !(prev.getNodeType() == 3 || prev.getNodeType() == 4 || (prev.getNodeType() == 5 && hasTextOnlyChildren(prev)))) {
                break;
            }
            parent.removeChild(prev);
            previousSibling = currentNode.getPreviousSibling();
        }
        Node nextSibling = currentNode.getNextSibling();
        while (true) {
            Node next = nextSibling;
            if (next == null || !(next.getNodeType() == 3 || next.getNodeType() == 4 || (next.getNodeType() == 5 && hasTextOnlyChildren(next)))) {
                break;
            }
            parent.removeChild(next);
            nextSibling = currentNode.getNextSibling();
        }
        return currentNode;
    }

    private boolean canModifyPrev(Node node) {
        boolean textLastChild = false;
        Node previousSibling = node.getPreviousSibling();
        while (true) {
            Node prev = previousSibling;
            if (prev != null) {
                short type = prev.getNodeType();
                if (type == 5) {
                    Node lastChild = prev.getLastChild();
                    if (lastChild == null) {
                        return false;
                    }
                    while (lastChild != null) {
                        short lType = lastChild.getNodeType();
                        if (lType != 3 && lType != 4) {
                            if (lType == 5) {
                                if (!canModifyPrev(lastChild)) {
                                    return false;
                                }
                            } else {
                                if (textLastChild) {
                                    return false;
                                }
                                return true;
                            }
                        }
                        textLastChild = true;
                        lastChild = lastChild.getPreviousSibling();
                    }
                } else if (type != 3 && type != 4) {
                    return true;
                }
                previousSibling = prev.getPreviousSibling();
            } else {
                return true;
            }
        }
    }

    private boolean canModifyNext(Node node) {
        boolean textFirstChild = false;
        Node nextSibling = node.getNextSibling();
        while (true) {
            Node next = nextSibling;
            if (next != null) {
                short type = next.getNodeType();
                if (type == 5) {
                    Node firstChild = next.getFirstChild();
                    if (firstChild == null) {
                        return false;
                    }
                    while (firstChild != null) {
                        short lType = firstChild.getNodeType();
                        if (lType != 3 && lType != 4) {
                            if (lType == 5) {
                                if (!canModifyNext(firstChild)) {
                                    return false;
                                }
                            } else {
                                if (textFirstChild) {
                                    return false;
                                }
                                return true;
                            }
                        }
                        textFirstChild = true;
                        firstChild = firstChild.getNextSibling();
                    }
                } else if (type != 3 && type != 4) {
                    return true;
                }
                nextSibling = next.getNextSibling();
            } else {
                return true;
            }
        }
    }

    private boolean hasTextOnlyChildren(Node node) {
        if (node == null) {
            return false;
        }
        Node firstChild = node.getFirstChild();
        while (true) {
            Node child = firstChild;
            if (child != null) {
                int type = child.getNodeType();
                if (type == 5) {
                    return hasTextOnlyChildren(child);
                }
                if (type != 3 && type != 4 && type != 5) {
                    return false;
                }
                firstChild = child.getNextSibling();
            } else {
                return true;
            }
        }
    }

    public boolean isIgnorableWhitespace() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return internalIsIgnorableWhitespace();
    }

    @Override // org.w3c.dom.Text
    public Text splitText(int offset) throws DOMException {
        if (isReadOnly()) {
            throw new DOMException((short) 7, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null));
        }
        if (needsSyncData()) {
            synchronizeData();
        }
        if (offset < 0 || offset > this.data.length()) {
            throw new DOMException((short) 1, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INDEX_SIZE_ERR", null));
        }
        Text newText = getOwnerDocument().createTextNode(this.data.substring(offset));
        setNodeValue(this.data.substring(0, offset));
        Node parentNode = getParentNode();
        if (parentNode != null) {
            parentNode.insertBefore(newText, this.nextSibling);
        }
        return newText;
    }

    public void replaceData(String value) {
        this.data = value;
    }

    public String removeData() {
        String olddata = this.data;
        this.data = "";
        return olddata;
    }
}
