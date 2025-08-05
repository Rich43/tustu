package com.sun.xml.internal.ws.wsdl.parser;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLFeaturedObject;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundOperation;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundPortType;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLFault;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLInput;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLModel;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLOperation;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLOutput;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLPort;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLPortType;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLService;
import com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension;
import com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtensionContext;
import com.sun.xml.internal.ws.encoding.soap.streaming.SOAPNamespaceConstants;
import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.AddressingFeature;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/parser/W3CAddressingWSDLParserExtension.class */
public class W3CAddressingWSDLParserExtension extends WSDLParserExtension {
    protected static final String COLON_DELIMITER = ":";
    protected static final String SLASH_DELIMITER = "/";

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean bindingElements(EditableWSDLBoundPortType binding, XMLStreamReader reader) {
        return addressibleElement(reader, binding);
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean portElements(EditableWSDLPort port, XMLStreamReader reader) {
        return addressibleElement(reader, port);
    }

    private boolean addressibleElement(XMLStreamReader reader, WSDLFeaturedObject binding) {
        QName ua = reader.getName();
        if (ua.equals(AddressingVersion.W3C.wsdlExtensionTag)) {
            String required = reader.getAttributeValue("http://schemas.xmlsoap.org/wsdl/", SchemaSymbols.ATTVAL_REQUIRED);
            binding.addFeature(new AddressingFeature(true, Boolean.parseBoolean(required)));
            XMLStreamReaderUtil.skipElement(reader);
            return true;
        }
        return false;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean bindingOperationElements(EditableWSDLBoundOperation operation, XMLStreamReader reader) {
        QName anon = reader.getName();
        if (anon.equals(AddressingVersion.W3C.wsdlAnonymousTag)) {
            try {
                String value = reader.getElementText();
                if (value == null || value.trim().equals("")) {
                    throw new WebServiceException("Null values not permitted in wsaw:Anonymous.");
                }
                if (value.equals(SchemaSymbols.ATTVAL_OPTIONAL)) {
                    operation.setAnonymous(WSDLBoundOperation.ANONYMOUS.optional);
                } else if (value.equals(SchemaSymbols.ATTVAL_REQUIRED)) {
                    operation.setAnonymous(WSDLBoundOperation.ANONYMOUS.required);
                } else if (value.equals(SchemaSymbols.ATTVAL_PROHIBITED)) {
                    operation.setAnonymous(WSDLBoundOperation.ANONYMOUS.prohibited);
                } else {
                    throw new WebServiceException("wsaw:Anonymous value \"" + value + "\" not understood.");
                }
                return true;
            } catch (XMLStreamException e2) {
                throw new WebServiceException(e2);
            }
        }
        return false;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void portTypeOperationInputAttributes(EditableWSDLInput input, XMLStreamReader reader) {
        String action = ParserUtil.getAttribute(reader, getWsdlActionTag());
        if (action != null) {
            input.setAction(action);
            input.setDefaultAction(false);
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void portTypeOperationOutputAttributes(EditableWSDLOutput output, XMLStreamReader reader) {
        String action = ParserUtil.getAttribute(reader, getWsdlActionTag());
        if (action != null) {
            output.setAction(action);
            output.setDefaultAction(false);
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void portTypeOperationFaultAttributes(EditableWSDLFault fault, XMLStreamReader reader) {
        String action = ParserUtil.getAttribute(reader, getWsdlActionTag());
        if (action != null) {
            fault.setAction(action);
            fault.setDefaultAction(false);
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void finished(WSDLParserExtensionContext context) {
        EditableWSDLModel model = context.getWSDLModel();
        for (EditableWSDLService service : model.getServices().values()) {
            for (EditableWSDLPort port : service.getPorts()) {
                EditableWSDLBoundPortType binding = port.getBinding();
                populateActions(binding);
                patchAnonymousDefault(binding);
            }
        }
    }

    protected String getNamespaceURI() {
        return AddressingVersion.W3C.wsdlNsUri;
    }

    protected QName getWsdlActionTag() {
        return AddressingVersion.W3C.wsdlActionTag;
    }

    private void populateActions(EditableWSDLBoundPortType binding) {
        EditableWSDLPortType porttype = binding.getPortType();
        for (EditableWSDLOperation o2 : porttype.getOperations()) {
            EditableWSDLBoundOperation wboi = binding.get(o2.getName());
            if (wboi == null) {
                o2.getInput().setAction(defaultInputAction(o2));
            } else {
                String soapAction = wboi.getSOAPAction();
                if (o2.getInput().getAction() == null || o2.getInput().getAction().equals("")) {
                    if (soapAction != null && !soapAction.equals("")) {
                        o2.getInput().setAction(soapAction);
                    } else {
                        o2.getInput().setAction(defaultInputAction(o2));
                    }
                }
                if (o2.getOutput() != null) {
                    if (o2.getOutput().getAction() == null || o2.getOutput().getAction().equals("")) {
                        o2.getOutput().setAction(defaultOutputAction(o2));
                    }
                    if (o2.getFaults() != null && o2.getFaults().iterator().hasNext()) {
                        for (EditableWSDLFault f2 : o2.getFaults()) {
                            if (f2.getAction() == null || f2.getAction().equals("")) {
                                f2.setAction(defaultFaultAction(f2.getName(), o2));
                            }
                        }
                    }
                }
            }
        }
    }

    protected void patchAnonymousDefault(EditableWSDLBoundPortType binding) {
        for (EditableWSDLBoundOperation wbo : binding.getBindingOperations()) {
            if (wbo.getAnonymous() == null) {
                wbo.setAnonymous(WSDLBoundOperation.ANONYMOUS.optional);
            }
        }
    }

    private String defaultInputAction(EditableWSDLOperation o2) {
        return buildAction(o2.getInput().getName(), o2, false);
    }

    private String defaultOutputAction(EditableWSDLOperation o2) {
        return buildAction(o2.getOutput().getName(), o2, false);
    }

    private String defaultFaultAction(String name, EditableWSDLOperation o2) {
        return buildAction(name, o2, true);
    }

    protected static final String buildAction(String name, EditableWSDLOperation o2, boolean isFault) {
        String tns = o2.getName().getNamespaceURI();
        String delim = "/";
        if (!tns.startsWith("http")) {
            delim = ":";
        }
        if (tns.endsWith(delim)) {
            tns = tns.substring(0, tns.length() - 1);
        }
        if (o2.getPortTypeName() == null) {
            throw new WebServiceException(PdfOps.DOUBLE_QUOTE__TOKEN + ((Object) o2.getName()) + "\" operation's owning portType name is null.");
        }
        return tns + delim + o2.getPortTypeName().getLocalPart() + delim + (isFault ? o2.getName().getLocalPart() + delim + SOAPNamespaceConstants.TAG_FAULT + delim : "") + name;
    }
}
