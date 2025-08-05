package com.sun.xml.internal.ws.api.model.wsdl.editable;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLModel;
import com.sun.xml.internal.ws.policy.PolicyMap;
import java.util.Map;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/wsdl/editable/EditableWSDLModel.class */
public interface EditableWSDLModel extends WSDLModel {
    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLModel
    EditableWSDLPortType getPortType(@NotNull QName qName);

    void addBinding(EditableWSDLBoundPortType editableWSDLBoundPortType);

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLModel
    EditableWSDLBoundPortType getBinding(@NotNull QName qName);

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLModel
    EditableWSDLBoundPortType getBinding(@NotNull QName qName, @NotNull QName qName2);

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLModel
    EditableWSDLService getService(@NotNull QName qName);

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLModel
    @NotNull
    Map<QName, ? extends EditableWSDLMessage> getMessages();

    void addMessage(EditableWSDLMessage editableWSDLMessage);

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLModel
    @NotNull
    Map<QName, ? extends EditableWSDLPortType> getPortTypes();

    void addPortType(EditableWSDLPortType editableWSDLPortType);

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLModel
    @NotNull
    Map<QName, ? extends EditableWSDLBoundPortType> getBindings();

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLModel
    @NotNull
    Map<QName, ? extends EditableWSDLService> getServices();

    void addService(EditableWSDLService editableWSDLService);

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLModel
    EditableWSDLMessage getMessage(QName qName);

    void setPolicyMap(PolicyMap policyMap);

    void finalizeRpcLitBinding(EditableWSDLBoundPortType editableWSDLBoundPortType);

    void freeze();
}
