package com.sun.xml.internal.ws.wsdl.parser;

import com.sun.xml.internal.ws.addressing.W3CAddressingMetadataConstants;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundOperation;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundPortType;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLPort;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/parser/W3CAddressingMetadataWSDLParserExtension.class */
public class W3CAddressingMetadataWSDLParserExtension extends W3CAddressingWSDLParserExtension {
    String METADATA_WSDL_EXTN_NS = W3CAddressingMetadataConstants.WSAM_NAMESPACE_NAME;
    QName METADATA_WSDL_ACTION_TAG = new QName(this.METADATA_WSDL_EXTN_NS, "Action", W3CAddressingMetadataConstants.WSAM_PREFIX_NAME);

    @Override // com.sun.xml.internal.ws.wsdl.parser.W3CAddressingWSDLParserExtension, com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean bindingElements(EditableWSDLBoundPortType binding, XMLStreamReader reader) {
        return false;
    }

    @Override // com.sun.xml.internal.ws.wsdl.parser.W3CAddressingWSDLParserExtension, com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean portElements(EditableWSDLPort port, XMLStreamReader reader) {
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
        return this.METADATA_WSDL_EXTN_NS;
    }

    @Override // com.sun.xml.internal.ws.wsdl.parser.W3CAddressingWSDLParserExtension
    protected QName getWsdlActionTag() {
        return this.METADATA_WSDL_ACTION_TAG;
    }
}
