package com.sun.org.apache.xerces.internal.impl.xs.opti;

import org.icepdf.core.util.PdfOps;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.TypeInfo;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/opti/AttrImpl.class */
public class AttrImpl extends NodeImpl implements Attr {
    Element element;
    String value;

    public AttrImpl() {
        this.nodeType = (short) 2;
    }

    public AttrImpl(Element element, String prefix, String localpart, String rawname, String uri, String value) {
        super(prefix, localpart, rawname, uri, (short) 2);
        this.element = element;
        this.value = value;
    }

    @Override // org.w3c.dom.Attr
    public String getName() {
        return this.rawname;
    }

    @Override // org.w3c.dom.Attr
    public boolean getSpecified() {
        return true;
    }

    @Override // org.w3c.dom.Attr
    public String getValue() {
        return this.value;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultNode, org.w3c.dom.Node
    public String getNodeValue() {
        return getValue();
    }

    @Override // org.w3c.dom.Attr
    public Element getOwnerElement() {
        return this.element;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultNode, org.w3c.dom.Node
    public Document getOwnerDocument() {
        return this.element.getOwnerDocument();
    }

    @Override // org.w3c.dom.Attr
    public void setValue(String value) throws DOMException {
        this.value = value;
    }

    @Override // org.w3c.dom.Attr
    public boolean isId() {
        return false;
    }

    @Override // org.w3c.dom.Attr
    public TypeInfo getSchemaTypeInfo() {
        return null;
    }

    public String toString() {
        return getName() + "=\"" + getValue() + PdfOps.DOUBLE_QUOTE__TOKEN;
    }
}
