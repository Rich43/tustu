package com.sun.org.apache.xml.internal.utils;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/AttList.class */
public class AttList implements Attributes {
    NamedNodeMap m_attrs;
    int m_lastIndex;

    public AttList(NamedNodeMap attrs) {
        this.m_attrs = attrs;
        this.m_lastIndex = this.m_attrs.getLength() - 1;
    }

    @Override // org.xml.sax.Attributes
    public int getLength() {
        return this.m_attrs.getLength();
    }

    @Override // org.xml.sax.Attributes
    public String getURI(int index) {
        String ns = DOM2Helper.getNamespaceOfNode((Attr) this.m_attrs.item(index));
        if (null == ns) {
            ns = "";
        }
        return ns;
    }

    @Override // org.xml.sax.Attributes
    public String getLocalName(int index) {
        return DOM2Helper.getLocalNameOfNode((Attr) this.m_attrs.item(index));
    }

    @Override // org.xml.sax.Attributes
    public String getQName(int i2) {
        return ((Attr) this.m_attrs.item(i2)).getName();
    }

    @Override // org.xml.sax.Attributes
    public String getType(int i2) {
        return "CDATA";
    }

    @Override // org.xml.sax.Attributes
    public String getValue(int i2) {
        return ((Attr) this.m_attrs.item(i2)).getValue();
    }

    @Override // org.xml.sax.Attributes
    public String getType(String name) {
        return "CDATA";
    }

    @Override // org.xml.sax.Attributes
    public String getType(String uri, String localName) {
        return "CDATA";
    }

    @Override // org.xml.sax.Attributes
    public String getValue(String name) {
        Attr attr = (Attr) this.m_attrs.getNamedItem(name);
        if (null != attr) {
            return attr.getValue();
        }
        return null;
    }

    @Override // org.xml.sax.Attributes
    public String getValue(String uri, String localName) throws DOMException {
        Node a2 = this.m_attrs.getNamedItemNS(uri, localName);
        if (a2 == null) {
            return null;
        }
        return a2.getNodeValue();
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x003a  */
    @Override // org.xml.sax.Attributes
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int getIndex(java.lang.String r4, java.lang.String r5) {
        /*
            r3 = this;
            r0 = r3
            org.w3c.dom.NamedNodeMap r0 = r0.m_attrs
            int r0 = r0.getLength()
            r1 = 1
            int r0 = r0 - r1
            r6 = r0
        Lc:
            r0 = r6
            if (r0 < 0) goto L50
            r0 = r3
            org.w3c.dom.NamedNodeMap r0 = r0.m_attrs
            r1 = r6
            org.w3c.dom.Node r0 = r0.item(r1)
            r7 = r0
            r0 = r7
            java.lang.String r0 = r0.getNamespaceURI()
            r8 = r0
            r0 = r8
            if (r0 != 0) goto L31
            r0 = r4
            if (r0 != 0) goto L4a
            goto L3a
        L31:
            r0 = r8
            r1 = r4
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L4a
        L3a:
            r0 = r7
            java.lang.String r0 = r0.getLocalName()
            r1 = r5
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L4a
            r0 = r6
            return r0
        L4a:
            int r6 = r6 + (-1)
            goto Lc
        L50:
            r0 = -1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xml.internal.utils.AttList.getIndex(java.lang.String, java.lang.String):int");
    }

    @Override // org.xml.sax.Attributes
    public int getIndex(String qName) {
        for (int i2 = this.m_attrs.getLength() - 1; i2 >= 0; i2--) {
            Node a2 = this.m_attrs.item(i2);
            if (a2.getNodeName().equals(qName)) {
                return i2;
            }
        }
        return -1;
    }
}
