package com.sun.xml.internal.ws.model.wsdl;

import com.sun.xml.internal.ws.api.model.SEIModel;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/model/wsdl/WSDLDirectProperties.class */
public final class WSDLDirectProperties extends WSDLProperties {
    private final QName serviceName;
    private final QName portName;

    public WSDLDirectProperties(QName serviceName, QName portName) {
        this(serviceName, portName, null);
    }

    public WSDLDirectProperties(QName serviceName, QName portName, SEIModel seiModel) {
        super(seiModel);
        this.serviceName = serviceName;
        this.portName = portName;
    }

    @Override // com.sun.xml.internal.ws.model.wsdl.WSDLProperties
    public QName getWSDLService() {
        return this.serviceName;
    }

    @Override // com.sun.xml.internal.ws.model.wsdl.WSDLProperties
    public QName getWSDLPort() {
        return this.portName;
    }

    @Override // com.sun.xml.internal.ws.model.wsdl.WSDLProperties
    public QName getWSDLPortType() {
        return null;
    }
}
