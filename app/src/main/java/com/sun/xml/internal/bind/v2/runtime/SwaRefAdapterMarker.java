package com.sun.xml.internal.bind.v2.runtime;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/SwaRefAdapterMarker.class */
public class SwaRefAdapterMarker extends XmlAdapter<String, DataHandler> {
    @Override // javax.xml.bind.annotation.adapters.XmlAdapter
    public DataHandler unmarshal(String v2) throws Exception {
        throw new IllegalStateException("Not implemented");
    }

    @Override // javax.xml.bind.annotation.adapters.XmlAdapter
    public String marshal(DataHandler v2) throws Exception {
        throw new IllegalStateException("Not implemented");
    }
}
