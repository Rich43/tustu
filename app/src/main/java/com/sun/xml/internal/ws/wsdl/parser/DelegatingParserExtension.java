package com.sun.xml.internal.ws.wsdl.parser;

import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundFault;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundOperation;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundPortType;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLFault;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLInput;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLMessage;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLOperation;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLOutput;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLPort;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLPortType;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLService;
import com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension;
import com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtensionContext;
import javax.xml.stream.XMLStreamReader;

/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/parser/DelegatingParserExtension.class */
class DelegatingParserExtension extends WSDLParserExtension {
    protected final WSDLParserExtension core;

    public DelegatingParserExtension(WSDLParserExtension core) {
        this.core = core;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void start(WSDLParserExtensionContext context) {
        this.core.start(context);
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void serviceAttributes(EditableWSDLService service, XMLStreamReader reader) {
        this.core.serviceAttributes(service, reader);
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean serviceElements(EditableWSDLService service, XMLStreamReader reader) {
        return this.core.serviceElements(service, reader);
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void portAttributes(EditableWSDLPort port, XMLStreamReader reader) {
        this.core.portAttributes(port, reader);
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean portElements(EditableWSDLPort port, XMLStreamReader reader) {
        return this.core.portElements(port, reader);
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean portTypeOperationInput(EditableWSDLOperation op, XMLStreamReader reader) {
        return this.core.portTypeOperationInput(op, reader);
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean portTypeOperationOutput(EditableWSDLOperation op, XMLStreamReader reader) {
        return this.core.portTypeOperationOutput(op, reader);
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean portTypeOperationFault(EditableWSDLOperation op, XMLStreamReader reader) {
        return this.core.portTypeOperationFault(op, reader);
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean definitionsElements(XMLStreamReader reader) {
        return this.core.definitionsElements(reader);
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean bindingElements(EditableWSDLBoundPortType binding, XMLStreamReader reader) {
        return this.core.bindingElements(binding, reader);
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void bindingAttributes(EditableWSDLBoundPortType binding, XMLStreamReader reader) {
        this.core.bindingAttributes(binding, reader);
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean portTypeElements(EditableWSDLPortType portType, XMLStreamReader reader) {
        return this.core.portTypeElements(portType, reader);
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void portTypeAttributes(EditableWSDLPortType portType, XMLStreamReader reader) {
        this.core.portTypeAttributes(portType, reader);
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean portTypeOperationElements(EditableWSDLOperation operation, XMLStreamReader reader) {
        return this.core.portTypeOperationElements(operation, reader);
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void portTypeOperationAttributes(EditableWSDLOperation operation, XMLStreamReader reader) {
        this.core.portTypeOperationAttributes(operation, reader);
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean bindingOperationElements(EditableWSDLBoundOperation operation, XMLStreamReader reader) {
        return this.core.bindingOperationElements(operation, reader);
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void bindingOperationAttributes(EditableWSDLBoundOperation operation, XMLStreamReader reader) {
        this.core.bindingOperationAttributes(operation, reader);
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean messageElements(EditableWSDLMessage msg, XMLStreamReader reader) {
        return this.core.messageElements(msg, reader);
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void messageAttributes(EditableWSDLMessage msg, XMLStreamReader reader) {
        this.core.messageAttributes(msg, reader);
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean portTypeOperationInputElements(EditableWSDLInput input, XMLStreamReader reader) {
        return this.core.portTypeOperationInputElements(input, reader);
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void portTypeOperationInputAttributes(EditableWSDLInput input, XMLStreamReader reader) {
        this.core.portTypeOperationInputAttributes(input, reader);
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean portTypeOperationOutputElements(EditableWSDLOutput output, XMLStreamReader reader) {
        return this.core.portTypeOperationOutputElements(output, reader);
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void portTypeOperationOutputAttributes(EditableWSDLOutput output, XMLStreamReader reader) {
        this.core.portTypeOperationOutputAttributes(output, reader);
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean portTypeOperationFaultElements(EditableWSDLFault fault, XMLStreamReader reader) {
        return this.core.portTypeOperationFaultElements(fault, reader);
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void portTypeOperationFaultAttributes(EditableWSDLFault fault, XMLStreamReader reader) {
        this.core.portTypeOperationFaultAttributes(fault, reader);
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean bindingOperationInputElements(EditableWSDLBoundOperation operation, XMLStreamReader reader) {
        return this.core.bindingOperationInputElements(operation, reader);
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void bindingOperationInputAttributes(EditableWSDLBoundOperation operation, XMLStreamReader reader) {
        this.core.bindingOperationInputAttributes(operation, reader);
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean bindingOperationOutputElements(EditableWSDLBoundOperation operation, XMLStreamReader reader) {
        return this.core.bindingOperationOutputElements(operation, reader);
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void bindingOperationOutputAttributes(EditableWSDLBoundOperation operation, XMLStreamReader reader) {
        this.core.bindingOperationOutputAttributes(operation, reader);
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean bindingOperationFaultElements(EditableWSDLBoundFault fault, XMLStreamReader reader) {
        return this.core.bindingOperationFaultElements(fault, reader);
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void bindingOperationFaultAttributes(EditableWSDLBoundFault fault, XMLStreamReader reader) {
        this.core.bindingOperationFaultAttributes(fault, reader);
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void finished(WSDLParserExtensionContext context) {
        this.core.finished(context);
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void postFinished(WSDLParserExtensionContext context) {
        this.core.postFinished(context);
    }
}
