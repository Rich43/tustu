package com.sun.xml.internal.ws.api.model.wsdl.editable;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLFault;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/wsdl/editable/EditableWSDLFault.class */
public interface EditableWSDLFault extends WSDLFault {
    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLFault
    EditableWSDLMessage getMessage();

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLFault
    @NotNull
    EditableWSDLOperation getOperation();

    void setAction(String str);

    void setDefaultAction(boolean z2);

    void freeze(EditableWSDLModel editableWSDLModel);
}
