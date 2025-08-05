package com.sun.xml.internal.ws.api.model.wsdl.editable;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/wsdl/editable/EditableWSDLOutput.class */
public interface EditableWSDLOutput extends WSDLOutput {
    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput
    EditableWSDLMessage getMessage();

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput
    @NotNull
    EditableWSDLOperation getOperation();

    void setAction(String str);

    void setDefaultAction(boolean z2);

    void freeze(EditableWSDLModel editableWSDLModel);
}
