package com.sun.xml.internal.ws.api.wsdl.parser;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLModel;
import com.sun.xml.internal.ws.api.policy.PolicyResolver;
import com.sun.xml.internal.ws.api.server.Container;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/wsdl/parser/WSDLParserExtensionContext.class */
public interface WSDLParserExtensionContext {
    boolean isClientSide();

    EditableWSDLModel getWSDLModel();

    @NotNull
    Container getContainer();

    @NotNull
    PolicyResolver getPolicyResolver();
}
