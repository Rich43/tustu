package com.sun.xml.internal.ws.api.model.wsdl.editable;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLService;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/wsdl/editable/EditableWSDLService.class */
public interface EditableWSDLService extends WSDLService {
    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLService
    @NotNull
    EditableWSDLModel getParent();

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLService
    EditableWSDLPort get(QName qName);

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLService
    EditableWSDLPort getFirstPort();

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLService
    @Nullable
    EditableWSDLPort getMatchingPort(QName qName);

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLService
    Iterable<? extends EditableWSDLPort> getPorts();

    void put(QName qName, EditableWSDLPort editableWSDLPort);

    void freeze(EditableWSDLModel editableWSDLModel);
}
