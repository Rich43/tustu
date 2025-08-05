package com.sun.xml.internal.ws.api.model.wsdl.editable;

import com.sun.xml.internal.ws.api.model.ParameterBinding;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPart;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/wsdl/editable/EditableWSDLPart.class */
public interface EditableWSDLPart extends WSDLPart {
    void setBinding(ParameterBinding parameterBinding);

    void setIndex(int i2);
}
