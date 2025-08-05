package com.sun.xml.internal.ws.api.message;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.bind.api.Bridge;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import com.sun.xml.internal.ws.api.model.JavaMethod;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.api.model.WSDLOperationMapping;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.encoding.soap.streaming.SOAPNamespaceConstants;
import com.sun.xml.internal.ws.fault.SOAPFaultBuilder;
import com.sun.xml.internal.ws.message.AttachmentSetImpl;
import com.sun.xml.internal.ws.message.StringHeader;
import com.sun.xml.internal.ws.spi.db.XMLBridge;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Source;
import javax.xml.ws.WebServiceException;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/message/Message.class */
public abstract class Message {
    protected AttachmentSet attachmentSet;
    private WSDLBoundOperation operation = null;
    private WSDLOperationMapping wsdlOperationMapping = null;
    private MessageMetadata messageMetadata = null;
    private Boolean isOneWay;
    static final /* synthetic */ boolean $assertionsDisabled;

    public abstract boolean hasHeaders();

    @NotNull
    public abstract MessageHeaders getHeaders();

    @Nullable
    public abstract String getPayloadLocalPart();

    public abstract String getPayloadNamespaceURI();

    public abstract boolean hasPayload();

    public abstract Source readEnvelopeAsSource();

    public abstract Source readPayloadAsSource();

    public abstract SOAPMessage readAsSOAPMessage() throws SOAPException;

    public abstract <T> T readPayloadAsJAXB(Unmarshaller unmarshaller) throws JAXBException;

    public abstract <T> T readPayloadAsJAXB(Bridge<T> bridge) throws JAXBException;

    public abstract <T> T readPayloadAsJAXB(XMLBridge<T> xMLBridge) throws JAXBException;

    public abstract XMLStreamReader readPayload() throws XMLStreamException;

    public abstract void writePayloadTo(XMLStreamWriter xMLStreamWriter) throws XMLStreamException;

    public abstract void writeTo(XMLStreamWriter xMLStreamWriter) throws XMLStreamException;

    public abstract void writeTo(ContentHandler contentHandler, ErrorHandler errorHandler) throws SAXException;

    public abstract Message copy();

    static {
        $assertionsDisabled = !Message.class.desiredAssertionStatus();
    }

    @NotNull
    public AttachmentSet getAttachments() {
        if (this.attachmentSet == null) {
            this.attachmentSet = new AttachmentSetImpl();
        }
        return this.attachmentSet;
    }

    protected boolean hasAttachments() {
        return this.attachmentSet != null;
    }

    public void setMessageMedadata(MessageMetadata metadata) {
        this.messageMetadata = metadata;
    }

    @Deprecated
    @Nullable
    public final WSDLBoundOperation getOperation(@NotNull WSDLBoundPortType boundPortType) {
        if (this.operation == null && this.messageMetadata != null) {
            if (this.wsdlOperationMapping == null) {
                this.wsdlOperationMapping = this.messageMetadata.getWSDLOperationMapping();
            }
            if (this.wsdlOperationMapping != null) {
                this.operation = this.wsdlOperationMapping.getWSDLBoundOperation();
            }
        }
        if (this.operation == null) {
            this.operation = boundPortType.getOperation(getPayloadNamespaceURI(), getPayloadLocalPart());
        }
        return this.operation;
    }

    @Deprecated
    @Nullable
    public final WSDLBoundOperation getOperation(@NotNull WSDLPort port) {
        return getOperation(port.getBinding());
    }

    @Deprecated
    @Nullable
    public final JavaMethod getMethod(@NotNull SEIModel seiModel) {
        String nsUri;
        if (this.wsdlOperationMapping == null && this.messageMetadata != null) {
            this.wsdlOperationMapping = this.messageMetadata.getWSDLOperationMapping();
        }
        if (this.wsdlOperationMapping != null) {
            return this.wsdlOperationMapping.getJavaMethod();
        }
        String localPart = getPayloadLocalPart();
        if (localPart == null) {
            localPart = "";
            nsUri = "";
        } else {
            nsUri = getPayloadNamespaceURI();
        }
        QName name = new QName(nsUri, localPart);
        return seiModel.getJavaMethod(name);
    }

    public boolean isOneWay(@NotNull WSDLPort port) {
        if (this.isOneWay == null) {
            WSDLBoundOperation op = getOperation(port);
            if (op != null) {
                this.isOneWay = Boolean.valueOf(op.getOperation().isOneWay());
            } else {
                this.isOneWay = false;
            }
        }
        return this.isOneWay.booleanValue();
    }

    public final void assertOneWay(boolean value) {
        if (!$assertionsDisabled && this.isOneWay != null && this.isOneWay.booleanValue() != value) {
            throw new AssertionError();
        }
        this.isOneWay = Boolean.valueOf(value);
    }

    public boolean isFault() {
        String localPart = getPayloadLocalPart();
        if (localPart == null || !localPart.equals(SOAPNamespaceConstants.TAG_FAULT)) {
            return false;
        }
        String nsUri = getPayloadNamespaceURI();
        return nsUri.equals(SOAPVersion.SOAP_11.nsUri) || nsUri.equals(SOAPVersion.SOAP_12.nsUri);
    }

    @Nullable
    public QName getFirstDetailEntryName() {
        if (!$assertionsDisabled && !isFault()) {
            throw new AssertionError();
        }
        Message msg = copy();
        try {
            SOAPFaultBuilder fault = SOAPFaultBuilder.create(msg);
            return fault.getFirstDetailEntryName();
        } catch (JAXBException e2) {
            throw new WebServiceException(e2);
        }
    }

    public SOAPMessage readAsSOAPMessage(Packet packet, boolean inbound) throws SOAPException {
        return readAsSOAPMessage();
    }

    public static Map<String, List<String>> getTransportHeaders(Packet packet) {
        return getTransportHeaders(packet, packet.getState().isInbound());
    }

    public static Map<String, List<String>> getTransportHeaders(Packet packet, boolean inbound) {
        Map<String, List<String>> headers = null;
        String key = inbound ? Packet.INBOUND_TRANSPORT_HEADERS : Packet.OUTBOUND_TRANSPORT_HEADERS;
        if (packet.supports(key)) {
            headers = (Map) packet.get(key);
        }
        return headers;
    }

    public static void addSOAPMimeHeaders(MimeHeaders mh, Map<String, List<String>> headers) {
        for (Map.Entry<String, List<String>> e2 : headers.entrySet()) {
            if (!e2.getKey().equalsIgnoreCase("Content-Type")) {
                for (String value : e2.getValue()) {
                    mh.addHeader(e2.getKey(), value);
                }
            }
        }
    }

    public void consume() {
    }

    @NotNull
    public String getID(@NotNull WSBinding binding) {
        return getID(binding.getAddressingVersion(), binding.getSOAPVersion());
    }

    @NotNull
    public String getID(AddressingVersion av2, SOAPVersion sv) {
        String uuid = null;
        if (av2 != null) {
            uuid = AddressingUtils.getMessageID(getHeaders(), av2, sv);
        }
        if (uuid == null) {
            uuid = generateMessageID();
            getHeaders().add(new StringHeader(av2.messageIDTag, uuid));
        }
        return uuid;
    }

    public static String generateMessageID() {
        return "uuid:" + UUID.randomUUID().toString();
    }

    public SOAPVersion getSOAPVersion() {
        return null;
    }
}
