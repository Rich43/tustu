package com.sun.xml.internal.ws.wsdl.parser;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLFeaturedObject;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundOperation;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundPortType;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLPort;
import com.sun.xml.internal.ws.developer.MemberSubmissionAddressingFeature;
import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/parser/MemberSubmissionAddressingWSDLParserExtension.class */
public class MemberSubmissionAddressingWSDLParserExtension extends W3CAddressingWSDLParserExtension {
    @Override // com.sun.xml.internal.ws.wsdl.parser.W3CAddressingWSDLParserExtension, com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean bindingElements(EditableWSDLBoundPortType binding, XMLStreamReader reader) {
        return addressibleElement(reader, binding);
    }

    @Override // com.sun.xml.internal.ws.wsdl.parser.W3CAddressingWSDLParserExtension, com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean portElements(EditableWSDLPort port, XMLStreamReader reader) {
        return addressibleElement(reader, port);
    }

    private boolean addressibleElement(XMLStreamReader reader, WSDLFeaturedObject binding) {
        QName ua = reader.getName();
        if (ua.equals(AddressingVersion.MEMBER.wsdlExtensionTag)) {
            String required = reader.getAttributeValue("http://schemas.xmlsoap.org/wsdl/", SchemaSymbols.ATTVAL_REQUIRED);
            binding.addFeature(new MemberSubmissionAddressingFeature(Boolean.parseBoolean(required)));
            XMLStreamReaderUtil.skipElement(reader);
            return true;
        }
        return false;
    }

    @Override // com.sun.xml.internal.ws.wsdl.parser.W3CAddressingWSDLParserExtension, com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean bindingOperationElements(EditableWSDLBoundOperation operation, XMLStreamReader reader) {
        return false;
    }

    @Override // com.sun.xml.internal.ws.wsdl.parser.W3CAddressingWSDLParserExtension
    protected void patchAnonymousDefault(EditableWSDLBoundPortType binding) {
    }

    @Override // com.sun.xml.internal.ws.wsdl.parser.W3CAddressingWSDLParserExtension
    protected String getNamespaceURI() {
        return AddressingVersion.MEMBER.wsdlNsUri;
    }

    @Override // com.sun.xml.internal.ws.wsdl.parser.W3CAddressingWSDLParserExtension
    protected QName getWsdlActionTag() {
        return AddressingVersion.MEMBER.wsdlActionTag;
    }
}
