package com.sun.xml.internal.ws.api.model.wsdl.editable;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
import java.util.Map;
import javax.jws.WebParam;
import javax.jws.soap.SOAPBinding;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/wsdl/editable/EditableWSDLBoundOperation.class */
public interface EditableWSDLBoundOperation extends WSDLBoundOperation {
    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation
    @NotNull
    EditableWSDLOperation getOperation();

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation
    @NotNull
    EditableWSDLBoundPortType getBoundPortType();

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation
    @Nullable
    EditableWSDLPart getPart(@NotNull String str, @NotNull WebParam.Mode mode);

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation
    @NotNull
    Map<String, ? extends EditableWSDLPart> getInParts();

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation
    @NotNull
    Map<String, ? extends EditableWSDLPart> getOutParts();

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation
    @NotNull
    Iterable<? extends EditableWSDLBoundFault> getFaults();

    void addPart(EditableWSDLPart editableWSDLPart, WebParam.Mode mode);

    void addFault(@NotNull EditableWSDLBoundFault editableWSDLBoundFault);

    void setAnonymous(WSDLBoundOperation.ANONYMOUS anonymous);

    void setInputExplicitBodyParts(boolean z2);

    void setOutputExplicitBodyParts(boolean z2);

    void setFaultExplicitBodyParts(boolean z2);

    void setRequestNamespace(String str);

    void setResponseNamespace(String str);

    void setSoapAction(String str);

    void setStyle(SOAPBinding.Style style);

    void freeze(EditableWSDLModel editableWSDLModel);
}
