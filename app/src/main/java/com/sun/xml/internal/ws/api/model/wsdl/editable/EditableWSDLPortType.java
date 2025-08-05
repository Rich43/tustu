package com.sun.xml.internal.ws.api.model.wsdl.editable;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLPortType;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/wsdl/editable/EditableWSDLPortType.class */
public interface EditableWSDLPortType extends WSDLPortType {
    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLPortType
    EditableWSDLOperation get(String str);

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLPortType
    Iterable<? extends EditableWSDLOperation> getOperations();

    void put(String str, EditableWSDLOperation editableWSDLOperation);

    void freeze();
}
