package com.sun.xml.internal.ws.model.wsdl;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLDescriptorKind;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPartDescriptor;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

/* loaded from: rt.jar:com/sun/xml/internal/ws/model/wsdl/WSDLPartDescriptorImpl.class */
public final class WSDLPartDescriptorImpl extends AbstractObjectImpl implements WSDLPartDescriptor {
    private QName name;
    private WSDLDescriptorKind type;

    public WSDLPartDescriptorImpl(XMLStreamReader xsr, QName name, WSDLDescriptorKind kind) {
        super(xsr);
        this.name = name;
        this.type = kind;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLPartDescriptor
    public QName name() {
        return this.name;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLPartDescriptor
    public WSDLDescriptorKind type() {
        return this.type;
    }
}
