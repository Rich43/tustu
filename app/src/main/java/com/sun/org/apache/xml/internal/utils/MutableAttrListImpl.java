package com.sun.org.apache.xml.internal.utils;

import java.io.Serializable;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/MutableAttrListImpl.class */
public class MutableAttrListImpl extends AttributesImpl implements Serializable {
    static final long serialVersionUID = 6289452013442934470L;

    public MutableAttrListImpl() {
    }

    public MutableAttrListImpl(Attributes atts) {
        super(atts);
    }

    @Override // org.xml.sax.helpers.AttributesImpl
    public void addAttribute(String uri, String localName, String qName, String type, String value) {
        if (null == uri) {
            uri = "";
        }
        int index = getIndex(qName);
        if (index >= 0) {
            setAttribute(index, uri, localName, qName, type, value);
        } else {
            super.addAttribute(uri, localName, qName, type, value);
        }
    }

    public void addAttributes(Attributes atts) {
        int nAtts = atts.getLength();
        for (int i2 = 0; i2 < nAtts; i2++) {
            String uri = atts.getURI(i2);
            if (null == uri) {
                uri = "";
            }
            String localName = atts.getLocalName(i2);
            String qname = atts.getQName(i2);
            int index = getIndex(uri, localName);
            if (index >= 0) {
                setAttribute(index, uri, localName, qname, atts.getType(i2), atts.getValue(i2));
            } else {
                addAttribute(uri, localName, qname, atts.getType(i2), atts.getValue(i2));
            }
        }
    }

    public boolean contains(String name) {
        return getValue(name) != null;
    }
}
