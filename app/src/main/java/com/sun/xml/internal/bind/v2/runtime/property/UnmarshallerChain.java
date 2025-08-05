package com.sun.xml.internal.bind.v2.runtime.property;

import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/property/UnmarshallerChain.class */
public final class UnmarshallerChain {
    private int offset = 0;
    public final JAXBContextImpl context;

    public UnmarshallerChain(JAXBContextImpl context) {
        this.context = context;
    }

    public int allocateOffset() {
        int i2 = this.offset;
        this.offset = i2 + 1;
        return i2;
    }

    public int getScopeSize() {
        return this.offset;
    }
}
