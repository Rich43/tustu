package com.sun.xml.internal.ws.api.model.wsdl.editable;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.EndpointAddress;
import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/wsdl/editable/EditableWSDLPort.class */
public interface EditableWSDLPort extends WSDLPort {
    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLPort
    @NotNull
    EditableWSDLBoundPortType getBinding();

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLPort
    @NotNull
    EditableWSDLService getOwner();

    void setAddress(EndpointAddress endpointAddress);

    void setEPR(@NotNull WSEndpointReference wSEndpointReference);

    void freeze(EditableWSDLModel editableWSDLModel);
}
