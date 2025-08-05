package com.sun.org.apache.xml.internal.dtm.ref;

import com.sun.org.apache.xml.internal.dtm.DTM;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMNamedNodeMap.class */
public class DTMNamedNodeMap implements NamedNodeMap {
    DTM dtm;
    int element;
    short m_count = -1;

    public DTMNamedNodeMap(DTM dtm, int element) {
        this.dtm = dtm;
        this.element = element;
    }

    @Override // org.w3c.dom.NamedNodeMap
    public int getLength() {
        if (this.m_count == -1) {
            short count = 0;
            int firstAttribute = this.dtm.getFirstAttribute(this.element);
            while (true) {
                int n2 = firstAttribute;
                if (n2 == -1) {
                    break;
                }
                count = (short) (count + 1);
                firstAttribute = this.dtm.getNextAttribute(n2);
            }
            this.m_count = count;
        }
        return this.m_count;
    }

    @Override // org.w3c.dom.NamedNodeMap
    public Node getNamedItem(String name) {
        int firstAttribute = this.dtm.getFirstAttribute(this.element);
        while (true) {
            int n2 = firstAttribute;
            if (n2 != -1) {
                if (!this.dtm.getNodeName(n2).equals(name)) {
                    firstAttribute = this.dtm.getNextAttribute(n2);
                } else {
                    return this.dtm.getNode(n2);
                }
            } else {
                return null;
            }
        }
    }

    @Override // org.w3c.dom.NamedNodeMap
    public Node item(int i2) {
        int count = 0;
        int firstAttribute = this.dtm.getFirstAttribute(this.element);
        while (true) {
            int n2 = firstAttribute;
            if (n2 != -1) {
                if (count == i2) {
                    return this.dtm.getNode(n2);
                }
                count++;
                firstAttribute = this.dtm.getNextAttribute(n2);
            } else {
                return null;
            }
        }
    }

    @Override // org.w3c.dom.NamedNodeMap
    public Node setNamedItem(Node newNode) {
        throw new DTMException((short) 7);
    }

    @Override // org.w3c.dom.NamedNodeMap
    public Node removeNamedItem(String name) {
        throw new DTMException((short) 7);
    }

    @Override // org.w3c.dom.NamedNodeMap
    public Node getNamedItemNS(String namespaceURI, String localName) {
        int n2;
        Node retNode = null;
        int firstAttribute = this.dtm.getFirstAttribute(this.element);
        while (true) {
            n2 = firstAttribute;
            if (n2 == -1) {
                break;
            }
            if (localName.equals(this.dtm.getLocalName(n2))) {
                String nsURI = this.dtm.getNamespaceURI(n2);
                if ((namespaceURI == null && nsURI == null) || (namespaceURI != null && namespaceURI.equals(nsURI))) {
                    break;
                }
            }
            firstAttribute = this.dtm.getNextAttribute(n2);
        }
        retNode = this.dtm.getNode(n2);
        return retNode;
    }

    @Override // org.w3c.dom.NamedNodeMap
    public Node setNamedItemNS(Node arg) throws DOMException {
        throw new DTMException((short) 7);
    }

    @Override // org.w3c.dom.NamedNodeMap
    public Node removeNamedItemNS(String namespaceURI, String localName) throws DOMException {
        throw new DTMException((short) 7);
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMNamedNodeMap$DTMException.class */
    public class DTMException extends DOMException {
        static final long serialVersionUID = -8290238117162437678L;

        public DTMException(short code, String message) {
            super(code, message);
        }

        public DTMException(short code) {
            super(code, "");
        }
    }
}
