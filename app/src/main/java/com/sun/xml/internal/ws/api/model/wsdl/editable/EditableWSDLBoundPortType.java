package com.sun.xml.internal.ws.api.model.wsdl.editable;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.BindingID;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType;
import javax.jws.soap.SOAPBinding;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/wsdl/editable/EditableWSDLBoundPortType.class */
public interface EditableWSDLBoundPortType extends WSDLBoundPortType {
    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType
    @NotNull
    EditableWSDLModel getOwner();

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType
    EditableWSDLBoundOperation get(QName qName);

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType
    EditableWSDLPortType getPortType();

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType
    Iterable<? extends EditableWSDLBoundOperation> getBindingOperations();

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType
    @Nullable
    EditableWSDLBoundOperation getOperation(String str, String str2);

    void put(QName qName, EditableWSDLBoundOperation editableWSDLBoundOperation);

    void setBindingId(BindingID bindingID);

    void setStyle(SOAPBinding.Style style);

    void freeze();
}
