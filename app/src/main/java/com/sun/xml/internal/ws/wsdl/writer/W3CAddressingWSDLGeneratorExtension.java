package com.sun.xml.internal.ws.wsdl.writer;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import com.sun.xml.internal.ws.api.model.CheckedException;
import com.sun.xml.internal.ws.api.model.JavaMethod;
import com.sun.xml.internal.ws.api.wsdl.writer.WSDLGenExtnContext;
import com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;
import javax.xml.ws.soap.AddressingFeature;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/writer/W3CAddressingWSDLGeneratorExtension.class */
public class W3CAddressingWSDLGeneratorExtension extends WSDLGeneratorExtension {
    private boolean enabled;
    private boolean required = false;
    private static final Logger LOGGER = Logger.getLogger(W3CAddressingWSDLGeneratorExtension.class.getName());

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void start(WSDLGenExtnContext ctxt) {
        WSBinding binding = ctxt.getBinding();
        TypedXmlWriter root = ctxt.getRoot();
        this.enabled = binding.isFeatureEnabled(AddressingFeature.class);
        if (!this.enabled) {
            return;
        }
        AddressingFeature ftr = (AddressingFeature) binding.getFeature(AddressingFeature.class);
        this.required = ftr.isRequired();
        root._namespace(AddressingVersion.W3C.wsdlNsUri, AddressingVersion.W3C.getWsdlPrefix());
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addOperationInputExtension(TypedXmlWriter input, JavaMethod method) {
        if (!this.enabled) {
            return;
        }
        Action a2 = (Action) method.getSEIMethod().getAnnotation(Action.class);
        if (a2 != null && !a2.input().equals("")) {
            addAttribute(input, a2.input());
            return;
        }
        String soapAction = method.getBinding().getSOAPAction();
        if (soapAction == null || soapAction.equals("")) {
            String defaultAction = getDefaultAction(method);
            addAttribute(input, defaultAction);
        }
    }

    protected static final String getDefaultAction(JavaMethod method) {
        String tns = method.getOwner().getTargetNamespace();
        String delim = "/";
        try {
            URI uri = new URI(tns);
            if (uri.getScheme().equalsIgnoreCase("urn")) {
                delim = CallSiteDescriptor.TOKEN_DELIMITER;
            }
        } catch (URISyntaxException e2) {
            LOGGER.warning("TargetNamespace of WebService is not a valid URI");
        }
        if (tns.endsWith(delim)) {
            tns = tns.substring(0, tns.length() - 1);
        }
        String name = method.getMEP().isOneWay() ? method.getOperationName() : method.getOperationName() + "Request";
        return tns + delim + method.getOwner().getPortTypeName().getLocalPart() + delim + name;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addOperationOutputExtension(TypedXmlWriter output, JavaMethod method) {
        Action a2;
        if (this.enabled && (a2 = (Action) method.getSEIMethod().getAnnotation(Action.class)) != null && !a2.output().equals("")) {
            addAttribute(output, a2.output());
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addOperationFaultExtension(TypedXmlWriter fault, JavaMethod method, CheckedException ce) {
        if (!this.enabled) {
            return;
        }
        Action a2 = (Action) method.getSEIMethod().getAnnotation(Action.class);
        Class[] exs = method.getSEIMethod().getExceptionTypes();
        if (exs != null && a2 != null && a2.fault() != null) {
            for (FaultAction fa : a2.fault()) {
                if (fa.className().getName().equals(ce.getExceptionClass().getName())) {
                    if (fa.value().equals("")) {
                        return;
                    }
                    addAttribute(fault, fa.value());
                    return;
                }
            }
        }
    }

    private void addAttribute(TypedXmlWriter writer, String attrValue) {
        writer._attribute(AddressingVersion.W3C.wsdlActionTag, attrValue);
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addBindingExtension(TypedXmlWriter binding) {
        if (!this.enabled) {
            return;
        }
        binding._element(AddressingVersion.W3C.wsdlExtensionTag, UsingAddressing.class);
    }
}
