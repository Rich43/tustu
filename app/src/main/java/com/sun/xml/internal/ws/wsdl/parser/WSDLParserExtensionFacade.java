package com.sun.xml.internal.ws.wsdl.parser;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
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
import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamReader;
import org.xml.sax.Locator;
import org.xml.sax.helpers.LocatorImpl;

/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/parser/WSDLParserExtensionFacade.class */
final class WSDLParserExtensionFacade extends WSDLParserExtension {
    private final WSDLParserExtension[] extensions;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !WSDLParserExtensionFacade.class.desiredAssertionStatus();
    }

    WSDLParserExtensionFacade(WSDLParserExtension... extensions) {
        if (!$assertionsDisabled && extensions == null) {
            throw new AssertionError();
        }
        this.extensions = extensions;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void start(WSDLParserExtensionContext context) {
        for (WSDLParserExtension e2 : this.extensions) {
            e2.start(context);
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean serviceElements(EditableWSDLService service, XMLStreamReader reader) {
        for (WSDLParserExtension e2 : this.extensions) {
            if (e2.serviceElements(service, reader)) {
                return true;
            }
        }
        XMLStreamReaderUtil.skipElement(reader);
        return true;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void serviceAttributes(EditableWSDLService service, XMLStreamReader reader) {
        for (WSDLParserExtension e2 : this.extensions) {
            e2.serviceAttributes(service, reader);
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean portElements(EditableWSDLPort port, XMLStreamReader reader) {
        for (WSDLParserExtension e2 : this.extensions) {
            if (e2.portElements(port, reader)) {
                return true;
            }
        }
        if (isRequiredExtension(reader)) {
            port.addNotUnderstoodExtension(reader.getName(), getLocator(reader));
        }
        XMLStreamReaderUtil.skipElement(reader);
        return true;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean portTypeOperationInput(EditableWSDLOperation op, XMLStreamReader reader) {
        for (WSDLParserExtension e2 : this.extensions) {
            e2.portTypeOperationInput(op, reader);
        }
        return false;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean portTypeOperationOutput(EditableWSDLOperation op, XMLStreamReader reader) {
        for (WSDLParserExtension e2 : this.extensions) {
            e2.portTypeOperationOutput(op, reader);
        }
        return false;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean portTypeOperationFault(EditableWSDLOperation op, XMLStreamReader reader) {
        for (WSDLParserExtension e2 : this.extensions) {
            e2.portTypeOperationFault(op, reader);
        }
        return false;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void portAttributes(EditableWSDLPort port, XMLStreamReader reader) {
        for (WSDLParserExtension e2 : this.extensions) {
            e2.portAttributes(port, reader);
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean definitionsElements(XMLStreamReader reader) {
        for (WSDLParserExtension e2 : this.extensions) {
            if (e2.definitionsElements(reader)) {
                return true;
            }
        }
        XMLStreamReaderUtil.skipElement(reader);
        return true;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean bindingElements(EditableWSDLBoundPortType binding, XMLStreamReader reader) {
        for (WSDLParserExtension e2 : this.extensions) {
            if (e2.bindingElements(binding, reader)) {
                return true;
            }
        }
        if (isRequiredExtension(reader)) {
            binding.addNotUnderstoodExtension(reader.getName(), getLocator(reader));
        }
        XMLStreamReaderUtil.skipElement(reader);
        return true;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void bindingAttributes(EditableWSDLBoundPortType binding, XMLStreamReader reader) {
        for (WSDLParserExtension e2 : this.extensions) {
            e2.bindingAttributes(binding, reader);
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean portTypeElements(EditableWSDLPortType portType, XMLStreamReader reader) {
        for (WSDLParserExtension e2 : this.extensions) {
            if (e2.portTypeElements(portType, reader)) {
                return true;
            }
        }
        XMLStreamReaderUtil.skipElement(reader);
        return true;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void portTypeAttributes(EditableWSDLPortType portType, XMLStreamReader reader) {
        for (WSDLParserExtension e2 : this.extensions) {
            e2.portTypeAttributes(portType, reader);
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean portTypeOperationElements(EditableWSDLOperation operation, XMLStreamReader reader) {
        for (WSDLParserExtension e2 : this.extensions) {
            if (e2.portTypeOperationElements(operation, reader)) {
                return true;
            }
        }
        XMLStreamReaderUtil.skipElement(reader);
        return true;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void portTypeOperationAttributes(EditableWSDLOperation operation, XMLStreamReader reader) {
        for (WSDLParserExtension e2 : this.extensions) {
            e2.portTypeOperationAttributes(operation, reader);
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean bindingOperationElements(EditableWSDLBoundOperation operation, XMLStreamReader reader) {
        for (WSDLParserExtension e2 : this.extensions) {
            if (e2.bindingOperationElements(operation, reader)) {
                return true;
            }
        }
        XMLStreamReaderUtil.skipElement(reader);
        return true;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void bindingOperationAttributes(EditableWSDLBoundOperation operation, XMLStreamReader reader) {
        for (WSDLParserExtension e2 : this.extensions) {
            e2.bindingOperationAttributes(operation, reader);
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean messageElements(EditableWSDLMessage msg, XMLStreamReader reader) {
        for (WSDLParserExtension e2 : this.extensions) {
            if (e2.messageElements(msg, reader)) {
                return true;
            }
        }
        XMLStreamReaderUtil.skipElement(reader);
        return true;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void messageAttributes(EditableWSDLMessage msg, XMLStreamReader reader) {
        for (WSDLParserExtension e2 : this.extensions) {
            e2.messageAttributes(msg, reader);
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean portTypeOperationInputElements(EditableWSDLInput input, XMLStreamReader reader) {
        for (WSDLParserExtension e2 : this.extensions) {
            if (e2.portTypeOperationInputElements(input, reader)) {
                return true;
            }
        }
        XMLStreamReaderUtil.skipElement(reader);
        return true;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void portTypeOperationInputAttributes(EditableWSDLInput input, XMLStreamReader reader) {
        for (WSDLParserExtension e2 : this.extensions) {
            e2.portTypeOperationInputAttributes(input, reader);
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean portTypeOperationOutputElements(EditableWSDLOutput output, XMLStreamReader reader) {
        for (WSDLParserExtension e2 : this.extensions) {
            if (e2.portTypeOperationOutputElements(output, reader)) {
                return true;
            }
        }
        XMLStreamReaderUtil.skipElement(reader);
        return true;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void portTypeOperationOutputAttributes(EditableWSDLOutput output, XMLStreamReader reader) {
        for (WSDLParserExtension e2 : this.extensions) {
            e2.portTypeOperationOutputAttributes(output, reader);
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean portTypeOperationFaultElements(EditableWSDLFault fault, XMLStreamReader reader) {
        for (WSDLParserExtension e2 : this.extensions) {
            if (e2.portTypeOperationFaultElements(fault, reader)) {
                return true;
            }
        }
        XMLStreamReaderUtil.skipElement(reader);
        return true;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void portTypeOperationFaultAttributes(EditableWSDLFault fault, XMLStreamReader reader) {
        for (WSDLParserExtension e2 : this.extensions) {
            e2.portTypeOperationFaultAttributes(fault, reader);
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean bindingOperationInputElements(EditableWSDLBoundOperation operation, XMLStreamReader reader) {
        for (WSDLParserExtension e2 : this.extensions) {
            if (e2.bindingOperationInputElements(operation, reader)) {
                return true;
            }
        }
        XMLStreamReaderUtil.skipElement(reader);
        return true;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void bindingOperationInputAttributes(EditableWSDLBoundOperation operation, XMLStreamReader reader) {
        for (WSDLParserExtension e2 : this.extensions) {
            e2.bindingOperationInputAttributes(operation, reader);
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean bindingOperationOutputElements(EditableWSDLBoundOperation operation, XMLStreamReader reader) {
        for (WSDLParserExtension e2 : this.extensions) {
            if (e2.bindingOperationOutputElements(operation, reader)) {
                return true;
            }
        }
        XMLStreamReaderUtil.skipElement(reader);
        return true;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void bindingOperationOutputAttributes(EditableWSDLBoundOperation operation, XMLStreamReader reader) {
        for (WSDLParserExtension e2 : this.extensions) {
            e2.bindingOperationOutputAttributes(operation, reader);
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean bindingOperationFaultElements(EditableWSDLBoundFault fault, XMLStreamReader reader) {
        for (WSDLParserExtension e2 : this.extensions) {
            if (e2.bindingOperationFaultElements(fault, reader)) {
                return true;
            }
        }
        XMLStreamReaderUtil.skipElement(reader);
        return true;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void bindingOperationFaultAttributes(EditableWSDLBoundFault fault, XMLStreamReader reader) {
        for (WSDLParserExtension e2 : this.extensions) {
            e2.bindingOperationFaultAttributes(fault, reader);
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void finished(WSDLParserExtensionContext context) {
        for (WSDLParserExtension e2 : this.extensions) {
            e2.finished(context);
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void postFinished(WSDLParserExtensionContext context) {
        for (WSDLParserExtension e2 : this.extensions) {
            e2.postFinished(context);
        }
    }

    private boolean isRequiredExtension(XMLStreamReader reader) {
        String required = reader.getAttributeValue("http://schemas.xmlsoap.org/wsdl/", SchemaSymbols.ATTVAL_REQUIRED);
        if (required != null) {
            return Boolean.parseBoolean(required);
        }
        return false;
    }

    private Locator getLocator(XMLStreamReader reader) {
        Location location = reader.getLocation();
        LocatorImpl loc = new LocatorImpl();
        loc.setSystemId(location.getSystemId());
        loc.setLineNumber(location.getLineNumber());
        return loc;
    }
}
