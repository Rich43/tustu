package com.sun.xml.internal.ws.model.wsdl;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/model/wsdl/WSDLPortProperties.class */
public final class WSDLPortProperties extends WSDLProperties {

    @NotNull
    private final WSDLPort port;

    public WSDLPortProperties(@NotNull WSDLPort port) {
        this(port, null);
    }

    public WSDLPortProperties(@NotNull WSDLPort port, @Nullable SEIModel seiModel) {
        super(seiModel);
        this.port = port;
    }

    @Override // com.sun.xml.internal.ws.model.wsdl.WSDLProperties
    public QName getWSDLService() {
        return this.port.getOwner().getName();
    }

    @Override // com.sun.xml.internal.ws.model.wsdl.WSDLProperties
    public QName getWSDLPort() {
        return this.port.getName();
    }

    @Override // com.sun.xml.internal.ws.model.wsdl.WSDLProperties
    public QName getWSDLPortType() {
        return this.port.getBinding().getPortTypeName();
    }
}
