package com.sun.org.apache.xml.internal.serializer;

import java.util.HashMap;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/AttributesImplSerializer.class */
public final class AttributesImplSerializer extends AttributesImpl {
    private final Map<String, Integer> m_indexFromQName = new HashMap();
    private final StringBuffer m_buff = new StringBuffer();
    private static final int MAX = 12;
    private static final int MAXMinus1 = 11;

    @Override // org.xml.sax.helpers.AttributesImpl, org.xml.sax.Attributes
    public final int getIndex(String qname) {
        int index;
        if (super.getLength() < 12) {
            int index2 = super.getIndex(qname);
            return index2;
        }
        Integer i2 = this.m_indexFromQName.get(qname);
        if (i2 == null) {
            index = -1;
        } else {
            index = i2.intValue();
        }
        return index;
    }

    @Override // org.xml.sax.helpers.AttributesImpl
    public final void addAttribute(String uri, String local, String qname, String type, String val) {
        int index = super.getLength();
        super.addAttribute(uri, local, qname, type, val);
        if (index < 11) {
            return;
        }
        if (index == 11) {
            switchOverToHash(12);
            return;
        }
        Integer i2 = Integer.valueOf(index);
        this.m_indexFromQName.put(qname, i2);
        this.m_buff.setLength(0);
        this.m_buff.append('{').append(uri).append('}').append(local);
        String key = this.m_buff.toString();
        this.m_indexFromQName.put(key, i2);
    }

    private void switchOverToHash(int numAtts) {
        for (int index = 0; index < numAtts; index++) {
            String qName = super.getQName(index);
            Integer i2 = Integer.valueOf(index);
            this.m_indexFromQName.put(qName, i2);
            String uri = super.getURI(index);
            String local = super.getLocalName(index);
            this.m_buff.setLength(0);
            this.m_buff.append('{').append(uri).append('}').append(local);
            String key = this.m_buff.toString();
            this.m_indexFromQName.put(key, i2);
        }
    }

    @Override // org.xml.sax.helpers.AttributesImpl
    public final void clear() {
        int len = super.getLength();
        super.clear();
        if (12 <= len) {
            this.m_indexFromQName.clear();
        }
    }

    @Override // org.xml.sax.helpers.AttributesImpl
    public final void setAttributes(Attributes atts) {
        super.setAttributes(atts);
        int numAtts = atts.getLength();
        if (12 <= numAtts) {
            switchOverToHash(numAtts);
        }
    }

    @Override // org.xml.sax.helpers.AttributesImpl, org.xml.sax.Attributes
    public final int getIndex(String uri, String localName) {
        int index;
        if (super.getLength() < 12) {
            int index2 = super.getIndex(uri, localName);
            return index2;
        }
        this.m_buff.setLength(0);
        this.m_buff.append('{').append(uri).append('}').append(localName);
        String key = this.m_buff.toString();
        Integer i2 = this.m_indexFromQName.get(key);
        if (i2 == null) {
            index = -1;
        } else {
            index = i2.intValue();
        }
        return index;
    }
}
