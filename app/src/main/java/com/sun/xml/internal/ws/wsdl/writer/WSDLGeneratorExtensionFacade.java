package com.sun.xml.internal.ws.wsdl.writer;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.ws.api.model.CheckedException;
import com.sun.xml.internal.ws.api.model.JavaMethod;
import com.sun.xml.internal.ws.api.wsdl.writer.WSDLGenExtnContext;
import com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension;

/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/writer/WSDLGeneratorExtensionFacade.class */
final class WSDLGeneratorExtensionFacade extends WSDLGeneratorExtension {
    private final WSDLGeneratorExtension[] extensions;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !WSDLGeneratorExtensionFacade.class.desiredAssertionStatus();
    }

    WSDLGeneratorExtensionFacade(WSDLGeneratorExtension... extensions) {
        if (!$assertionsDisabled && extensions == null) {
            throw new AssertionError();
        }
        this.extensions = extensions;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void start(WSDLGenExtnContext ctxt) {
        for (WSDLGeneratorExtension e2 : this.extensions) {
            e2.start(ctxt);
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void end(@NotNull WSDLGenExtnContext ctxt) {
        for (WSDLGeneratorExtension e2 : this.extensions) {
            e2.end(ctxt);
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addDefinitionsExtension(TypedXmlWriter definitions) {
        for (WSDLGeneratorExtension e2 : this.extensions) {
            e2.addDefinitionsExtension(definitions);
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addServiceExtension(TypedXmlWriter service) {
        for (WSDLGeneratorExtension e2 : this.extensions) {
            e2.addServiceExtension(service);
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addPortExtension(TypedXmlWriter port) {
        for (WSDLGeneratorExtension e2 : this.extensions) {
            e2.addPortExtension(port);
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addPortTypeExtension(TypedXmlWriter portType) {
        for (WSDLGeneratorExtension e2 : this.extensions) {
            e2.addPortTypeExtension(portType);
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addBindingExtension(TypedXmlWriter binding) {
        for (WSDLGeneratorExtension e2 : this.extensions) {
            e2.addBindingExtension(binding);
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addOperationExtension(TypedXmlWriter operation, JavaMethod method) {
        for (WSDLGeneratorExtension e2 : this.extensions) {
            e2.addOperationExtension(operation, method);
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addBindingOperationExtension(TypedXmlWriter operation, JavaMethod method) {
        for (WSDLGeneratorExtension e2 : this.extensions) {
            e2.addBindingOperationExtension(operation, method);
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addInputMessageExtension(TypedXmlWriter message, JavaMethod method) {
        for (WSDLGeneratorExtension e2 : this.extensions) {
            e2.addInputMessageExtension(message, method);
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addOutputMessageExtension(TypedXmlWriter message, JavaMethod method) {
        for (WSDLGeneratorExtension e2 : this.extensions) {
            e2.addOutputMessageExtension(message, method);
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addOperationInputExtension(TypedXmlWriter input, JavaMethod method) {
        for (WSDLGeneratorExtension e2 : this.extensions) {
            e2.addOperationInputExtension(input, method);
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addOperationOutputExtension(TypedXmlWriter output, JavaMethod method) {
        for (WSDLGeneratorExtension e2 : this.extensions) {
            e2.addOperationOutputExtension(output, method);
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addBindingOperationInputExtension(TypedXmlWriter input, JavaMethod method) {
        for (WSDLGeneratorExtension e2 : this.extensions) {
            e2.addBindingOperationInputExtension(input, method);
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addBindingOperationOutputExtension(TypedXmlWriter output, JavaMethod method) {
        for (WSDLGeneratorExtension e2 : this.extensions) {
            e2.addBindingOperationOutputExtension(output, method);
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addBindingOperationFaultExtension(TypedXmlWriter fault, JavaMethod method, CheckedException ce) {
        for (WSDLGeneratorExtension e2 : this.extensions) {
            e2.addBindingOperationFaultExtension(fault, method, ce);
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addFaultMessageExtension(TypedXmlWriter message, JavaMethod method, CheckedException ce) {
        for (WSDLGeneratorExtension e2 : this.extensions) {
            e2.addFaultMessageExtension(message, method, ce);
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addOperationFaultExtension(TypedXmlWriter fault, JavaMethod method, CheckedException ce) {
        for (WSDLGeneratorExtension e2 : this.extensions) {
            e2.addOperationFaultExtension(fault, method, ce);
        }
    }
}
