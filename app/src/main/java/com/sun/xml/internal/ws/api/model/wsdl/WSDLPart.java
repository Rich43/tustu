package com.sun.xml.internal.ws.api.model.wsdl;

import com.sun.xml.internal.ws.api.model.ParameterBinding;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/wsdl/WSDLPart.class */
public interface WSDLPart extends WSDLObject {
    String getName();

    ParameterBinding getBinding();

    int getIndex();

    WSDLPartDescriptor getDescriptor();
}
