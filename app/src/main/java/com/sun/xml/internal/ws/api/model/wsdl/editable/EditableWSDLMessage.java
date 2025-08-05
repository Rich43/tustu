package com.sun.xml.internal.ws.api.model.wsdl.editable;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLMessage;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/wsdl/editable/EditableWSDLMessage.class */
public interface EditableWSDLMessage extends WSDLMessage {
    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLMessage
    Iterable<? extends EditableWSDLPart> parts();

    void add(EditableWSDLPart editableWSDLPart);
}
