package com.sun.xml.internal.ws.api.model.wsdl.editable;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLOperation;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/wsdl/editable/EditableWSDLOperation.class */
public interface EditableWSDLOperation extends WSDLOperation {
    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLOperation
    @NotNull
    EditableWSDLInput getInput();

    void setInput(EditableWSDLInput editableWSDLInput);

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLOperation
    @Nullable
    EditableWSDLOutput getOutput();

    void setOutput(EditableWSDLOutput editableWSDLOutput);

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLOperation
    Iterable<? extends EditableWSDLFault> getFaults();

    void addFault(EditableWSDLFault editableWSDLFault);

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLOperation
    @Nullable
    EditableWSDLFault getFault(QName qName);

    void setParameterOrder(String str);

    void freeze(EditableWSDLModel editableWSDLModel);
}
