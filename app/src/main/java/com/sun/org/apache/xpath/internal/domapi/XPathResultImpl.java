package com.sun.org.apache.xpath.internal.domapi;

import com.sun.org.apache.xerces.internal.dom.events.MutationEventImpl;
import com.sun.org.apache.xpath.internal.XPath;
import com.sun.org.apache.xpath.internal.objects.XObject;
import com.sun.org.apache.xpath.internal.res.XPATHMessages;
import javax.xml.transform.TransformerException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.traversal.NodeIterator;
import org.w3c.dom.xpath.XPathException;
import org.w3c.dom.xpath.XPathResult;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/domapi/XPathResultImpl.class */
class XPathResultImpl implements XPathResult, EventListener {
    private final XObject m_resultObj;
    private final XPath m_xpath;
    private final short m_resultType;
    private boolean m_isInvalidIteratorState = false;
    private final Node m_contextNode;
    private NodeIterator m_iterator;
    private NodeList m_list;

    XPathResultImpl(short type, XObject result, Node contextNode, XPath xpath) {
        this.m_iterator = null;
        this.m_list = null;
        if (!isValidType(type)) {
            String fmsg = XPATHMessages.createXPATHMessage("ER_INVALID_XPATH_TYPE", new Object[]{new Integer(type)});
            throw new XPathException((short) 2, fmsg);
        }
        if (null == result) {
            String fmsg2 = XPATHMessages.createXPATHMessage("ER_EMPTY_XPATH_RESULT", null);
            throw new XPathException((short) 1, fmsg2);
        }
        this.m_resultObj = result;
        this.m_contextNode = contextNode;
        this.m_xpath = xpath;
        if (type == 0) {
            this.m_resultType = getTypeFromXObject(result);
        } else {
            this.m_resultType = type;
        }
        if (this.m_resultType == 5 || this.m_resultType == 4) {
            addEventListener();
        }
        if (this.m_resultType == 5 || this.m_resultType == 4 || this.m_resultType == 8 || this.m_resultType == 9) {
            try {
                this.m_iterator = this.m_resultObj.nodeset();
            } catch (TransformerException e2) {
                String fmsg3 = XPATHMessages.createXPATHMessage("ER_INCOMPATIBLE_TYPES", new Object[]{this.m_xpath.getPatternString(), getTypeString(getTypeFromXObject(this.m_resultObj)), getTypeString(this.m_resultType)});
                throw new XPathException((short) 2, fmsg3);
            }
        } else if (this.m_resultType == 6 || this.m_resultType == 7) {
            try {
                this.m_list = this.m_resultObj.nodelist();
            } catch (TransformerException e3) {
                String fmsg4 = XPATHMessages.createXPATHMessage("ER_INCOMPATIBLE_TYPES", new Object[]{this.m_xpath.getPatternString(), getTypeString(getTypeFromXObject(this.m_resultObj)), getTypeString(this.m_resultType)});
                throw new XPathException((short) 2, fmsg4);
            }
        }
    }

    @Override // org.w3c.dom.xpath.XPathResult
    public short getResultType() {
        return this.m_resultType;
    }

    @Override // org.w3c.dom.xpath.XPathResult
    public double getNumberValue() throws XPathException {
        if (getResultType() != 1) {
            String fmsg = XPATHMessages.createXPATHMessage("ER_CANT_CONVERT_XPATHRESULTTYPE_TO_NUMBER", new Object[]{this.m_xpath.getPatternString(), getTypeString(this.m_resultType)});
            throw new XPathException((short) 2, fmsg);
        }
        try {
            return this.m_resultObj.num();
        } catch (Exception e2) {
            throw new XPathException((short) 2, e2.getMessage());
        }
    }

    @Override // org.w3c.dom.xpath.XPathResult
    public String getStringValue() throws XPathException {
        if (getResultType() != 2) {
            String fmsg = XPATHMessages.createXPATHMessage("ER_CANT_CONVERT_TO_STRING", new Object[]{this.m_xpath.getPatternString(), this.m_resultObj.getTypeString()});
            throw new XPathException((short) 2, fmsg);
        }
        try {
            return this.m_resultObj.str();
        } catch (Exception e2) {
            throw new XPathException((short) 2, e2.getMessage());
        }
    }

    @Override // org.w3c.dom.xpath.XPathResult
    public boolean getBooleanValue() throws XPathException {
        if (getResultType() != 3) {
            String fmsg = XPATHMessages.createXPATHMessage("ER_CANT_CONVERT_TO_BOOLEAN", new Object[]{this.m_xpath.getPatternString(), getTypeString(this.m_resultType)});
            throw new XPathException((short) 2, fmsg);
        }
        try {
            return this.m_resultObj.bool();
        } catch (TransformerException e2) {
            throw new XPathException((short) 2, e2.getMessage());
        }
    }

    @Override // org.w3c.dom.xpath.XPathResult
    public Node getSingleNodeValue() throws DOMException, XPathException {
        if (this.m_resultType != 8 && this.m_resultType != 9) {
            String fmsg = XPATHMessages.createXPATHMessage("ER_CANT_CONVERT_TO_SINGLENODE", new Object[]{this.m_xpath.getPatternString(), getTypeString(this.m_resultType)});
            throw new XPathException((short) 2, fmsg);
        }
        try {
            NodeIterator result = this.m_resultObj.nodeset();
            if (null == result) {
                return null;
            }
            Node node = result.nextNode();
            if (isNamespaceNode(node)) {
                return new XPathNamespaceImpl(node);
            }
            return node;
        } catch (TransformerException te) {
            throw new XPathException((short) 2, te.getMessage());
        }
    }

    @Override // org.w3c.dom.xpath.XPathResult
    public boolean getInvalidIteratorState() {
        return this.m_isInvalidIteratorState;
    }

    @Override // org.w3c.dom.xpath.XPathResult
    public int getSnapshotLength() throws XPathException {
        if (this.m_resultType != 6 && this.m_resultType != 7) {
            String fmsg = XPATHMessages.createXPATHMessage("ER_CANT_GET_SNAPSHOT_LENGTH", new Object[]{this.m_xpath.getPatternString(), getTypeString(this.m_resultType)});
            throw new XPathException((short) 2, fmsg);
        }
        return this.m_list.getLength();
    }

    @Override // org.w3c.dom.xpath.XPathResult
    public Node iterateNext() throws DOMException, XPathException {
        if (this.m_resultType != 4 && this.m_resultType != 5) {
            String fmsg = XPATHMessages.createXPATHMessage("ER_NON_ITERATOR_TYPE", new Object[]{this.m_xpath.getPatternString(), getTypeString(this.m_resultType)});
            throw new XPathException((short) 2, fmsg);
        }
        if (getInvalidIteratorState()) {
            String fmsg2 = XPATHMessages.createXPATHMessage("ER_DOC_MUTATED", null);
            throw new DOMException((short) 11, fmsg2);
        }
        Node node = this.m_iterator.nextNode();
        if (null == node) {
            removeEventListener();
        }
        if (isNamespaceNode(node)) {
            return new XPathNamespaceImpl(node);
        }
        return node;
    }

    @Override // org.w3c.dom.xpath.XPathResult
    public Node snapshotItem(int index) throws XPathException {
        if (this.m_resultType != 6 && this.m_resultType != 7) {
            String fmsg = XPATHMessages.createXPATHMessage("ER_NON_SNAPSHOT_TYPE", new Object[]{this.m_xpath.getPatternString(), getTypeString(this.m_resultType)});
            throw new XPathException((short) 2, fmsg);
        }
        Node node = this.m_list.item(index);
        if (isNamespaceNode(node)) {
            return new XPathNamespaceImpl(node);
        }
        return node;
    }

    static boolean isValidType(short type) {
        switch (type) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                return true;
            default:
                return false;
        }
    }

    @Override // org.w3c.dom.events.EventListener
    public void handleEvent(Event event) {
        if (event.getType().equals(MutationEventImpl.DOM_SUBTREE_MODIFIED)) {
            this.m_isInvalidIteratorState = true;
            removeEventListener();
        }
    }

    private String getTypeString(int type) {
        switch (type) {
            case 0:
                return "ANY_TYPE";
            case 1:
                return "NUMBER_TYPE";
            case 2:
                return "STRING_TYPE";
            case 3:
                return "BOOLEAN";
            case 4:
                return "UNORDERED_NODE_ITERATOR_TYPE";
            case 5:
                return "ORDERED_NODE_ITERATOR_TYPE";
            case 6:
                return "UNORDERED_NODE_SNAPSHOT_TYPE";
            case 7:
                return "ORDERED_NODE_SNAPSHOT_TYPE";
            case 8:
                return "ANY_UNORDERED_NODE_TYPE";
            case 9:
                return "FIRST_ORDERED_NODE_TYPE";
            default:
                return "#UNKNOWN";
        }
    }

    private short getTypeFromXObject(XObject object) {
        switch (object.getType()) {
            case -1:
                return (short) 0;
            case 0:
            default:
                return (short) 0;
            case 1:
                return (short) 3;
            case 2:
                return (short) 1;
            case 3:
                return (short) 2;
            case 4:
                return (short) 4;
            case 5:
                return (short) 4;
        }
    }

    private boolean isNamespaceNode(Node node) {
        if (null != node && node.getNodeType() == 2) {
            if (node.getNodeName().startsWith("xmlns:") || node.getNodeName().equals("xmlns")) {
                return true;
            }
            return false;
        }
        return false;
    }

    private void addEventListener() {
        if (this.m_contextNode instanceof EventTarget) {
            ((EventTarget) this.m_contextNode).addEventListener(MutationEventImpl.DOM_SUBTREE_MODIFIED, this, true);
        }
    }

    private void removeEventListener() {
        if (this.m_contextNode instanceof EventTarget) {
            ((EventTarget) this.m_contextNode).removeEventListener(MutationEventImpl.DOM_SUBTREE_MODIFIED, this, true);
        }
    }
}
