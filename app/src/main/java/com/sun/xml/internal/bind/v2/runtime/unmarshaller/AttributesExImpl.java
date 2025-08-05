package com.sun.xml.internal.bind.v2.runtime.unmarshaller;

import com.sun.xml.internal.bind.util.AttributesImpl;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/AttributesExImpl.class */
public final class AttributesExImpl extends AttributesImpl implements AttributesEx {
    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.AttributesEx
    public CharSequence getData(int idx) {
        return getValue(idx);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.AttributesEx
    public CharSequence getData(String nsUri, String localName) {
        return getValue(nsUri, localName);
    }
}
