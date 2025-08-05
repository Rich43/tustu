package com.sun.xml.internal.ws.api.model.wsdl.editable;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundFault;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/wsdl/editable/EditableWSDLBoundFault.class */
public interface EditableWSDLBoundFault extends WSDLBoundFault {
    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundFault
    @Nullable
    EditableWSDLFault getFault();

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundFault
    @NotNull
    EditableWSDLBoundOperation getBoundOperation();

    void freeze(EditableWSDLBoundOperation editableWSDLBoundOperation);
}
