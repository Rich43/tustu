package com.sun.xml.internal.ws.wsdl.parser;

import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLModel;
import com.sun.xml.internal.ws.api.policy.PolicyResolver;
import com.sun.xml.internal.ws.api.server.Container;
import com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtensionContext;

/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/parser/WSDLParserExtensionContextImpl.class */
final class WSDLParserExtensionContextImpl implements WSDLParserExtensionContext {
    private final boolean isClientSide;
    private final EditableWSDLModel wsdlModel;
    private final Container container;
    private final PolicyResolver policyResolver;

    protected WSDLParserExtensionContextImpl(EditableWSDLModel model, boolean isClientSide, Container container, PolicyResolver policyResolver) {
        this.wsdlModel = model;
        this.isClientSide = isClientSide;
        this.container = container;
        this.policyResolver = policyResolver;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtensionContext
    public boolean isClientSide() {
        return this.isClientSide;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtensionContext
    public EditableWSDLModel getWSDLModel() {
        return this.wsdlModel;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtensionContext
    public Container getContainer() {
        return this.container;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtensionContext
    public PolicyResolver getPolicyResolver() {
        return this.policyResolver;
    }
}
