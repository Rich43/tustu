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
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/parser/FoolProofParserExtension.class */
final class FoolProofParserExtension extends DelegatingParserExtension {
    public FoolProofParserExtension(WSDLParserExtension core) {
        super(core);
    }

    private QName pre(XMLStreamReader xsr) {
        return xsr.getName();
    }

    private boolean post(QName tagName, XMLStreamReader xsr, boolean result) {
        if (!tagName.equals(xsr.getName())) {
            return foundFool();
        }
        if (result) {
            if (xsr.getEventType() != 2) {
                foundFool();
            }
        } else if (xsr.getEventType() != 1) {
            foundFool();
        }
        return result;
    }

    private boolean foundFool() {
        throw new AssertionError((Object) ("XMLStreamReader is placed at the wrong place after invoking " + ((Object) this.core)));
    }

    @Override // com.sun.xml.internal.ws.wsdl.parser.DelegatingParserExtension, com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean serviceElements(EditableWSDLService service, XMLStreamReader reader) {
        return post(pre(reader), reader, super.serviceElements(service, reader));
    }

    @Override // com.sun.xml.internal.ws.wsdl.parser.DelegatingParserExtension, com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean portElements(EditableWSDLPort port, XMLStreamReader reader) {
        return post(pre(reader), reader, super.portElements(port, reader));
    }

    @Override // com.sun.xml.internal.ws.wsdl.parser.DelegatingParserExtension, com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean definitionsElements(XMLStreamReader reader) {
        return post(pre(reader), reader, super.definitionsElements(reader));
    }

    @Override // com.sun.xml.internal.ws.wsdl.parser.DelegatingParserExtension, com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean bindingElements(EditableWSDLBoundPortType binding, XMLStreamReader reader) {
        return post(pre(reader), reader, super.bindingElements(binding, reader));
    }

    @Override // com.sun.xml.internal.ws.wsdl.parser.DelegatingParserExtension, com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean portTypeElements(EditableWSDLPortType portType, XMLStreamReader reader) {
        return post(pre(reader), reader, super.portTypeElements(portType, reader));
    }

    @Override // com.sun.xml.internal.ws.wsdl.parser.DelegatingParserExtension, com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean portTypeOperationElements(EditableWSDLOperation operation, XMLStreamReader reader) {
        return post(pre(reader), reader, super.portTypeOperationElements(operation, reader));
    }

    @Override // com.sun.xml.internal.ws.wsdl.parser.DelegatingParserExtension, com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean bindingOperationElements(EditableWSDLBoundOperation operation, XMLStreamReader reader) {
        return post(pre(reader), reader, super.bindingOperationElements(operation, reader));
    }

    @Override // com.sun.xml.internal.ws.wsdl.parser.DelegatingParserExtension, com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean messageElements(EditableWSDLMessage msg, XMLStreamReader reader) {
        return post(pre(reader), reader, super.messageElements(msg, reader));
    }

    @Override // com.sun.xml.internal.ws.wsdl.parser.DelegatingParserExtension, com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean portTypeOperationInputElements(EditableWSDLInput input, XMLStreamReader reader) {
        return post(pre(reader), reader, super.portTypeOperationInputElements(input, reader));
    }

    @Override // com.sun.xml.internal.ws.wsdl.parser.DelegatingParserExtension, com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean portTypeOperationOutputElements(EditableWSDLOutput output, XMLStreamReader reader) {
        return post(pre(reader), reader, super.portTypeOperationOutputElements(output, reader));
    }

    @Override // com.sun.xml.internal.ws.wsdl.parser.DelegatingParserExtension, com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean portTypeOperationFaultElements(EditableWSDLFault fault, XMLStreamReader reader) {
        return post(pre(reader), reader, super.portTypeOperationFaultElements(fault, reader));
    }

    @Override // com.sun.xml.internal.ws.wsdl.parser.DelegatingParserExtension, com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean bindingOperationInputElements(EditableWSDLBoundOperation operation, XMLStreamReader reader) {
        return super.bindingOperationInputElements(operation, reader);
    }

    @Override // com.sun.xml.internal.ws.wsdl.parser.DelegatingParserExtension, com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean bindingOperationOutputElements(EditableWSDLBoundOperation operation, XMLStreamReader reader) {
        return post(pre(reader), reader, super.bindingOperationOutputElements(operation, reader));
    }

    @Override // com.sun.xml.internal.ws.wsdl.parser.DelegatingParserExtension, com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean bindingOperationFaultElements(EditableWSDLBoundFault fault, XMLStreamReader reader) {
        return post(pre(reader), reader, super.bindingOperationFaultElements(fault, reader));
    }
}
