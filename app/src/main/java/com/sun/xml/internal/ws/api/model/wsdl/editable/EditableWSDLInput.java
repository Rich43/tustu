package com.sun.xml.internal.ws.api.model.wsdl.editable;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLInput;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/wsdl/editable/EditableWSDLInput.class */
public interface EditableWSDLInput extends WSDLInput {
    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLInput
    EditableWSDLMessage getMessage();

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLInput
    @NotNull
    EditableWSDLOperation getOperation();

    void setAction(String str);

    void setDefaultAction(boolean z2);

    void freeze(EditableWSDLModel editableWSDLModel);
}
