package com.sun.xml.internal.bind.v2.runtime;

import com.sun.xml.internal.bind.api.Bridge;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/InternalBridge.class */
abstract class InternalBridge<T> extends Bridge<T> {
    abstract void marshal(T t2, XMLSerializer xMLSerializer) throws SAXException, XMLStreamException, IOException;

    protected InternalBridge(JAXBContextImpl context) {
        super(context);
    }

    @Override // com.sun.xml.internal.bind.api.Bridge
    public JAXBContextImpl getContext() {
        return this.context;
    }
}
