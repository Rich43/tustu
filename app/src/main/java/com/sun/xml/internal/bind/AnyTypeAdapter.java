package com.sun.xml.internal.bind;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/* loaded from: rt.jar:com/sun/xml/internal/bind/AnyTypeAdapter.class */
public final class AnyTypeAdapter extends XmlAdapter<Object, Object> {
    @Override // javax.xml.bind.annotation.adapters.XmlAdapter
    public Object unmarshal(Object v2) {
        return v2;
    }

    @Override // javax.xml.bind.annotation.adapters.XmlAdapter
    public Object marshal(Object v2) {
        return v2;
    }
}
