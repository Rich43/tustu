package com.sun.xml.internal.ws.wsdl.writer;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.addressing.W3CAddressingConstants;
import com.sun.xml.internal.ws.addressing.v200408.MemberSubmissionAddressingConstants;
import com.sun.xml.internal.ws.api.server.PortAddressResolver;
import com.sun.xml.internal.ws.util.xml.XMLStreamReaderToXMLStreamWriter;
import com.sun.xml.internal.ws.wsdl.parser.WSDLConstants;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/writer/WSDLPatcher.class */
public final class WSDLPatcher extends XMLStreamReaderToXMLStreamWriter {
    private static final String NS_XSD = "http://www.w3.org/2001/XMLSchema";
    private static final QName SCHEMA_INCLUDE_QNAME = new QName("http://www.w3.org/2001/XMLSchema", "include");
    private static final QName SCHEMA_IMPORT_QNAME = new QName("http://www.w3.org/2001/XMLSchema", "import");
    private static final QName SCHEMA_REDEFINE_QNAME = new QName("http://www.w3.org/2001/XMLSchema", "redefine");
    private static final Logger logger = Logger.getLogger("com.sun.xml.internal.ws.wsdl.patcher");
    private final DocumentLocationResolver docResolver;
    private final PortAddressResolver portAddressResolver;
    private String targetNamespace;
    private QName serviceName;
    private QName portName;
    private String portAddress;
    private boolean inEpr;
    private boolean inEprAddress;

    public WSDLPatcher(@NotNull PortAddressResolver portAddressResolver, @NotNull DocumentLocationResolver docResolver) {
        this.portAddressResolver = portAddressResolver;
        this.docResolver = docResolver;
    }

    @Override // com.sun.xml.internal.ws.util.xml.XMLStreamReaderToXMLStreamWriter
    protected void handleAttribute(int i2) throws XMLStreamException {
        QName name = this.in.getName();
        String attLocalName = this.in.getAttributeLocalName(i2);
        if ((name.equals(SCHEMA_INCLUDE_QNAME) && attLocalName.equals("schemaLocation")) || ((name.equals(SCHEMA_IMPORT_QNAME) && attLocalName.equals("schemaLocation")) || ((name.equals(SCHEMA_REDEFINE_QNAME) && attLocalName.equals("schemaLocation")) || (name.equals(WSDLConstants.QNAME_IMPORT) && attLocalName.equals("location"))))) {
            String relPath = this.in.getAttributeValue(i2);
            String actualPath = getPatchedImportLocation(relPath);
            if (actualPath == null) {
                return;
            }
            logger.fine("Fixing the relative location:" + relPath + " with absolute location:" + actualPath);
            writeAttribute(i2, actualPath);
            return;
        }
        if ((name.equals(WSDLConstants.NS_SOAP_BINDING_ADDRESS) || name.equals(WSDLConstants.NS_SOAP12_BINDING_ADDRESS)) && attLocalName.equals("location")) {
            this.portAddress = this.in.getAttributeValue(i2);
            String value = getAddressLocation();
            if (value != null) {
                logger.fine("Service:" + ((Object) this.serviceName) + " port:" + ((Object) this.portName) + " current address " + this.portAddress + " Patching it with " + value);
                writeAttribute(i2, value);
                return;
            }
        }
        super.handleAttribute(i2);
    }

    private void writeAttribute(int i2, String value) throws XMLStreamException {
        String nsUri = this.in.getAttributeNamespace(i2);
        if (nsUri != null) {
            this.out.writeAttribute(this.in.getAttributePrefix(i2), nsUri, this.in.getAttributeLocalName(i2), value);
        } else {
            this.out.writeAttribute(this.in.getAttributeLocalName(i2), value);
        }
    }

    @Override // com.sun.xml.internal.ws.util.xml.XMLStreamReaderToXMLStreamWriter
    protected void handleStartElement() throws XMLStreamException {
        QName name = this.in.getName();
        if (name.equals(WSDLConstants.QNAME_DEFINITIONS)) {
            String value = this.in.getAttributeValue(null, WSDLConstants.ATTR_TNS);
            if (value != null) {
                this.targetNamespace = value;
            }
        } else if (name.equals(WSDLConstants.QNAME_SERVICE)) {
            String value2 = this.in.getAttributeValue(null, "name");
            if (value2 != null) {
                this.serviceName = new QName(this.targetNamespace, value2);
            }
        } else if (name.equals(WSDLConstants.QNAME_PORT)) {
            String value3 = this.in.getAttributeValue(null, "name");
            if (value3 != null) {
                this.portName = new QName(this.targetNamespace, value3);
            }
        } else if (name.equals(W3CAddressingConstants.WSA_EPR_QNAME) || name.equals(MemberSubmissionAddressingConstants.WSA_EPR_QNAME)) {
            if (this.serviceName != null && this.portName != null) {
                this.inEpr = true;
            }
        } else if ((name.equals(W3CAddressingConstants.WSA_ADDRESS_QNAME) || name.equals(MemberSubmissionAddressingConstants.WSA_ADDRESS_QNAME)) && this.inEpr) {
            this.inEprAddress = true;
        }
        super.handleStartElement();
    }

    @Override // com.sun.xml.internal.ws.util.xml.XMLStreamReaderToXMLStreamWriter
    protected void handleEndElement() throws XMLStreamException {
        QName name = this.in.getName();
        if (name.equals(WSDLConstants.QNAME_SERVICE)) {
            this.serviceName = null;
        } else if (name.equals(WSDLConstants.QNAME_PORT)) {
            this.portName = null;
        } else if (name.equals(W3CAddressingConstants.WSA_EPR_QNAME) || name.equals(MemberSubmissionAddressingConstants.WSA_EPR_QNAME)) {
            if (this.inEpr) {
                this.inEpr = false;
            }
        } else if ((name.equals(W3CAddressingConstants.WSA_ADDRESS_QNAME) || name.equals(MemberSubmissionAddressingConstants.WSA_ADDRESS_QNAME)) && this.inEprAddress) {
            String value = getAddressLocation();
            if (value != null) {
                logger.fine("Fixing EPR Address for service:" + ((Object) this.serviceName) + " port:" + ((Object) this.portName) + " address with " + value);
                this.out.writeCharacters(value);
            }
            this.inEprAddress = false;
        }
        super.handleEndElement();
    }

    @Override // com.sun.xml.internal.ws.util.xml.XMLStreamReaderToXMLStreamWriter
    protected void handleCharacters() throws XMLStreamException {
        if (this.inEprAddress) {
            String value = getAddressLocation();
            if (value != null) {
                return;
            }
        }
        super.handleCharacters();
    }

    @Nullable
    private String getPatchedImportLocation(String relPath) {
        return this.docResolver.getLocationFor(null, relPath);
    }

    private String getAddressLocation() {
        if (this.portAddressResolver == null || this.portName == null) {
            return null;
        }
        return this.portAddressResolver.getAddressFor(this.serviceName, this.portName.getLocalPart(), this.portAddress);
    }
}
