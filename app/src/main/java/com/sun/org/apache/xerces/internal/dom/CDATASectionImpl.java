package com.sun.org.apache.xerces.internal.dom;

import org.w3c.dom.CDATASection;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/CDATASectionImpl.class */
public class CDATASectionImpl extends TextImpl implements CDATASection {
    static final long serialVersionUID = 2372071297878177780L;

    public CDATASectionImpl(CoreDocumentImpl ownerDoc, String data) {
        super(ownerDoc, data);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.TextImpl, com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public short getNodeType() {
        return (short) 4;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.TextImpl, com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public String getNodeName() {
        return "#cdata-section";
    }
}
