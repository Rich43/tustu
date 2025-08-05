package com.sun.xml.internal.ws.api.addressing;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.xml.internal.stream.buffer.MutableXMLStreamBuffer;
import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
import com.sun.xml.internal.stream.buffer.XMLStreamBufferResult;
import com.sun.xml.internal.stream.buffer.XMLStreamBufferSource;
import com.sun.xml.internal.stream.buffer.sax.SAXBufferProcessor;
import com.sun.xml.internal.stream.buffer.stax.StreamReaderBufferProcessor;
import com.sun.xml.internal.stream.buffer.stax.StreamWriterBufferCreator;
import com.sun.xml.internal.ws.addressing.EndpointReferenceUtil;
import com.sun.xml.internal.ws.addressing.W3CAddressingMetadataConstants;
import com.sun.xml.internal.ws.addressing.WSEPRExtension;
import com.sun.xml.internal.ws.addressing.model.InvalidAddressingHeaderException;
import com.sun.xml.internal.ws.addressing.v200408.MemberSubmissionAddressingConstants;
import com.sun.xml.internal.ws.api.message.Header;
import com.sun.xml.internal.ws.api.message.HeaderList;
import com.sun.xml.internal.ws.api.message.MessageHeaders;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLExtension;
import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
import com.sun.xml.internal.ws.resources.AddressingMessages;
import com.sun.xml.internal.ws.resources.ClientMessages;
import com.sun.xml.internal.ws.spi.ProviderImpl;
import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil;
import com.sun.xml.internal.ws.util.DOMUtil;
import com.sun.xml.internal.ws.util.xml.XMLStreamReaderToXMLStreamWriter;
import com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter;
import com.sun.xml.internal.ws.util.xml.XmlUtil;
import com.sun.xml.internal.ws.wsdl.parser.WSDLConstants;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/addressing/WSEndpointReference.class */
public final class WSEndpointReference implements WSDLExtension {
    private final XMLStreamBuffer infoset;
    private final AddressingVersion version;

    @NotNull
    private Header[] referenceParameters;

    @NotNull
    private String address;

    @NotNull
    private QName rootElement;
    private static final OutboundReferenceParameterHeader[] EMPTY_ARRAY;
    private Map<QName, EPRExtension> rootEprExtensions;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/addressing/WSEndpointReference$EPRExtension.class */
    public static abstract class EPRExtension {
        public abstract XMLStreamReader readAsXMLStreamReader() throws XMLStreamException;

        public abstract QName getQName();
    }

    static {
        $assertionsDisabled = !WSEndpointReference.class.desiredAssertionStatus();
        EMPTY_ARRAY = new OutboundReferenceParameterHeader[0];
    }

    public WSEndpointReference(EndpointReference epr, AddressingVersion version) {
        try {
            MutableXMLStreamBuffer xsb = new MutableXMLStreamBuffer();
            epr.writeTo(new XMLStreamBufferResult(xsb));
            this.infoset = xsb;
            this.version = version;
            this.rootElement = new QName("EndpointReference", version.nsUri);
            parse();
        } catch (XMLStreamException e2) {
            throw new WebServiceException(ClientMessages.FAILED_TO_PARSE_EPR(epr), e2);
        }
    }

    public WSEndpointReference(EndpointReference epr) {
        this(epr, AddressingVersion.fromSpecClass(epr.getClass()));
    }

    public WSEndpointReference(XMLStreamBuffer infoset, AddressingVersion version) {
        try {
            this.infoset = infoset;
            this.version = version;
            this.rootElement = new QName("EndpointReference", version.nsUri);
            parse();
        } catch (XMLStreamException e2) {
            throw new AssertionError(e2);
        }
    }

    public WSEndpointReference(InputStream infoset, AddressingVersion version) throws XMLStreamException {
        this(XMLStreamReaderFactory.create((String) null, infoset, false), version);
    }

    public WSEndpointReference(XMLStreamReader in, AddressingVersion version) throws XMLStreamException {
        this(XMLStreamBuffer.createNewBufferFromXMLStreamReader(in), version);
    }

    public WSEndpointReference(URL address, AddressingVersion version) {
        this(address.toExternalForm(), version);
    }

    public WSEndpointReference(URI address, AddressingVersion version) {
        this(address.toString(), version);
    }

    public WSEndpointReference(String address, AddressingVersion version) {
        this.infoset = createBufferFromAddress(address, version);
        this.version = version;
        this.address = address;
        this.rootElement = new QName("EndpointReference", version.nsUri);
        this.referenceParameters = EMPTY_ARRAY;
    }

    private static XMLStreamBuffer createBufferFromAddress(String address, AddressingVersion version) {
        try {
            MutableXMLStreamBuffer xsb = new MutableXMLStreamBuffer();
            StreamWriterBufferCreator w2 = new StreamWriterBufferCreator(xsb);
            w2.writeStartDocument();
            w2.writeStartElement(version.getPrefix(), "EndpointReference", version.nsUri);
            w2.writeNamespace(version.getPrefix(), version.nsUri);
            w2.writeStartElement(version.getPrefix(), version.eprType.address, version.nsUri);
            w2.writeCharacters(address);
            w2.writeEndElement();
            w2.writeEndElement();
            w2.writeEndDocument();
            w2.close();
            return xsb;
        } catch (XMLStreamException e2) {
            throw new AssertionError(e2);
        }
    }

    public WSEndpointReference(@NotNull AddressingVersion version, @NotNull String address, @Nullable QName service, @Nullable QName port, @Nullable QName portType, @Nullable List<Element> metadata, @Nullable String wsdlAddress, @Nullable List<Element> referenceParameters) {
        this(version, address, service, port, portType, metadata, wsdlAddress, null, referenceParameters, null, null);
    }

    public WSEndpointReference(@NotNull AddressingVersion version, @NotNull String address, @Nullable QName service, @Nullable QName port, @Nullable QName portType, @Nullable List<Element> metadata, @Nullable String wsdlAddress, @Nullable List<Element> referenceParameters, @Nullable Collection<EPRExtension> extns, @Nullable Map<QName, String> attributes) {
        this(createBufferFromData(version, address, referenceParameters, service, port, portType, metadata, wsdlAddress, (String) null, extns, attributes), version);
    }

    public WSEndpointReference(@NotNull AddressingVersion version, @NotNull String address, @Nullable QName service, @Nullable QName port, @Nullable QName portType, @Nullable List<Element> metadata, @Nullable String wsdlAddress, @Nullable String wsdlTargetNamepsace, @Nullable List<Element> referenceParameters, @Nullable List<Element> elements, @Nullable Map<QName, String> attributes) {
        this(createBufferFromData(version, address, referenceParameters, service, port, portType, metadata, wsdlAddress, wsdlTargetNamepsace, elements, attributes), version);
    }

    private static XMLStreamBuffer createBufferFromData(AddressingVersion version, String address, List<Element> referenceParameters, QName service, QName port, QName portType, List<Element> metadata, String wsdlAddress, String wsdlTargetNamespace, @Nullable List<Element> elements, @Nullable Map<QName, String> attributes) {
        StreamWriterBufferCreator writer = new StreamWriterBufferCreator();
        try {
            writer.writeStartDocument();
            writer.writeStartElement(version.getPrefix(), "EndpointReference", version.nsUri);
            writer.writeNamespace(version.getPrefix(), version.nsUri);
            writePartialEPRInfoset(writer, version, address, referenceParameters, service, port, portType, metadata, wsdlAddress, wsdlTargetNamespace, attributes);
            if (elements != null) {
                for (Element e2 : elements) {
                    DOMUtil.serializeNode(e2, writer);
                }
            }
            writer.writeEndElement();
            writer.writeEndDocument();
            writer.flush();
            return writer.getXMLStreamBuffer();
        } catch (XMLStreamException e3) {
            throw new WebServiceException(e3);
        }
    }

    private static XMLStreamBuffer createBufferFromData(AddressingVersion version, String address, List<Element> referenceParameters, QName service, QName port, QName portType, List<Element> metadata, String wsdlAddress, String wsdlTargetNamespace, @Nullable Collection<EPRExtension> extns, @Nullable Map<QName, String> attributes) {
        StreamWriterBufferCreator writer = new StreamWriterBufferCreator();
        try {
            writer.writeStartDocument();
            writer.writeStartElement(version.getPrefix(), "EndpointReference", version.nsUri);
            writer.writeNamespace(version.getPrefix(), version.nsUri);
            writePartialEPRInfoset(writer, version, address, referenceParameters, service, port, portType, metadata, wsdlAddress, wsdlTargetNamespace, attributes);
            if (extns != null) {
                for (EPRExtension e2 : extns) {
                    XMLStreamReaderToXMLStreamWriter c2 = new XMLStreamReaderToXMLStreamWriter();
                    XMLStreamReader r2 = e2.readAsXMLStreamReader();
                    c2.bridge(r2, writer);
                    XMLStreamReaderFactory.recycle(r2);
                }
            }
            writer.writeEndElement();
            writer.writeEndDocument();
            writer.flush();
            return writer.getXMLStreamBuffer();
        } catch (XMLStreamException e3) {
            throw new WebServiceException(e3);
        }
    }

    private static void writePartialEPRInfoset(StreamWriterBufferCreator writer, AddressingVersion version, String address, List<Element> referenceParameters, QName service, QName port, QName portType, List<Element> metadata, String wsdlAddress, String wsdlTargetNamespace, @Nullable Map<QName, String> attributes) throws XMLStreamException {
        if (attributes != null) {
            for (Map.Entry<QName, String> entry : attributes.entrySet()) {
                QName qname = entry.getKey();
                writer.writeAttribute(qname.getPrefix(), qname.getNamespaceURI(), qname.getLocalPart(), entry.getValue());
            }
        }
        writer.writeStartElement(version.getPrefix(), version.eprType.address, version.nsUri);
        writer.writeCharacters(address);
        writer.writeEndElement();
        if (referenceParameters != null && referenceParameters.size() > 0) {
            writer.writeStartElement(version.getPrefix(), version.eprType.referenceParameters, version.nsUri);
            for (Element e2 : referenceParameters) {
                DOMUtil.serializeNode(e2, writer);
            }
            writer.writeEndElement();
        }
        switch (version) {
            case W3C:
                writeW3CMetaData(writer, service, port, portType, metadata, wsdlAddress, wsdlTargetNamespace);
                break;
            case MEMBER:
                writeMSMetaData(writer, service, port, portType, metadata);
                if (wsdlAddress != null) {
                    writer.writeStartElement(MemberSubmissionAddressingConstants.MEX_METADATA.getPrefix(), MemberSubmissionAddressingConstants.MEX_METADATA.getLocalPart(), MemberSubmissionAddressingConstants.MEX_METADATA.getNamespaceURI());
                    writer.writeStartElement(MemberSubmissionAddressingConstants.MEX_METADATA_SECTION.getPrefix(), MemberSubmissionAddressingConstants.MEX_METADATA_SECTION.getLocalPart(), MemberSubmissionAddressingConstants.MEX_METADATA_SECTION.getNamespaceURI());
                    writer.writeAttribute(MemberSubmissionAddressingConstants.MEX_METADATA_DIALECT_ATTRIBUTE, "http://schemas.xmlsoap.org/wsdl/");
                    writeWsdl(writer, service, wsdlAddress);
                    writer.writeEndElement();
                    writer.writeEndElement();
                    break;
                }
                break;
        }
    }

    private static boolean isEmty(QName qname) {
        return qname == null || qname.toString().trim().length() == 0;
    }

    private static void writeW3CMetaData(StreamWriterBufferCreator writer, QName service, QName port, QName portType, List<Element> metadata, String wsdlAddress, String wsdlTargetNamespace) throws XMLStreamException {
        if (isEmty(service) && isEmty(port) && isEmty(portType) && metadata == null) {
            return;
        }
        writer.writeStartElement(AddressingVersion.W3C.getPrefix(), AddressingVersion.W3C.eprType.wsdlMetadata.getLocalPart(), AddressingVersion.W3C.nsUri);
        writer.writeNamespace(AddressingVersion.W3C.getWsdlPrefix(), AddressingVersion.W3C.wsdlNsUri);
        if (wsdlAddress != null) {
            writeWsdliLocation(writer, service, wsdlAddress, wsdlTargetNamespace);
        }
        if (portType != null) {
            writer.writeStartElement(W3CAddressingMetadataConstants.WSAM_PREFIX_NAME, AddressingVersion.W3C.eprType.portTypeName, W3CAddressingMetadataConstants.WSAM_NAMESPACE_NAME);
            writer.writeNamespace(W3CAddressingMetadataConstants.WSAM_PREFIX_NAME, W3CAddressingMetadataConstants.WSAM_NAMESPACE_NAME);
            String portTypePrefix = portType.getPrefix();
            if (portTypePrefix == null || portTypePrefix.equals("")) {
                portTypePrefix = "wsns";
            }
            writer.writeNamespace(portTypePrefix, portType.getNamespaceURI());
            writer.writeCharacters(portTypePrefix + CallSiteDescriptor.TOKEN_DELIMITER + portType.getLocalPart());
            writer.writeEndElement();
        }
        if (service != null && !service.getNamespaceURI().equals("") && !service.getLocalPart().equals("")) {
            writer.writeStartElement(W3CAddressingMetadataConstants.WSAM_PREFIX_NAME, AddressingVersion.W3C.eprType.serviceName, W3CAddressingMetadataConstants.WSAM_NAMESPACE_NAME);
            writer.writeNamespace(W3CAddressingMetadataConstants.WSAM_PREFIX_NAME, W3CAddressingMetadataConstants.WSAM_NAMESPACE_NAME);
            String servicePrefix = service.getPrefix();
            if (servicePrefix == null || servicePrefix.equals("")) {
                servicePrefix = "wsns";
            }
            writer.writeNamespace(servicePrefix, service.getNamespaceURI());
            if (port != null) {
                writer.writeAttribute(AddressingVersion.W3C.eprType.portName, port.getLocalPart());
            }
            writer.writeCharacters(servicePrefix + CallSiteDescriptor.TOKEN_DELIMITER + service.getLocalPart());
            writer.writeEndElement();
        }
        if (metadata != null) {
            for (Element e2 : metadata) {
                DOMUtil.serializeNode(e2, writer);
            }
        }
        writer.writeEndElement();
    }

    private static void writeWsdliLocation(StreamWriterBufferCreator writer, QName service, String wsdlAddress, String wsdlTargetNamespace) throws XMLStreamException {
        String wsdliLocation;
        if (wsdlTargetNamespace != null) {
            wsdliLocation = wsdlTargetNamespace + " ";
        } else if (service != null) {
            wsdliLocation = service.getNamespaceURI() + " ";
        } else {
            throw new WebServiceException("WSDL target Namespace cannot be resolved");
        }
        writer.writeNamespace(W3CAddressingMetadataConstants.WSAM_WSDLI_ATTRIBUTE_PREFIX, W3CAddressingMetadataConstants.WSAM_WSDLI_ATTRIBUTE_NAMESPACE);
        writer.writeAttribute(W3CAddressingMetadataConstants.WSAM_WSDLI_ATTRIBUTE_PREFIX, W3CAddressingMetadataConstants.WSAM_WSDLI_ATTRIBUTE_NAMESPACE, W3CAddressingMetadataConstants.WSAM_WSDLI_ATTRIBUTE_LOCALNAME, wsdliLocation + wsdlAddress);
    }

    private static void writeMSMetaData(StreamWriterBufferCreator writer, QName service, QName port, QName portType, List<Element> metadata) throws XMLStreamException {
        if (portType != null) {
            writer.writeStartElement(AddressingVersion.MEMBER.getPrefix(), AddressingVersion.MEMBER.eprType.portTypeName, AddressingVersion.MEMBER.nsUri);
            String portTypePrefix = portType.getPrefix();
            if (portTypePrefix == null || portTypePrefix.equals("")) {
                portTypePrefix = "wsns";
            }
            writer.writeNamespace(portTypePrefix, portType.getNamespaceURI());
            writer.writeCharacters(portTypePrefix + CallSiteDescriptor.TOKEN_DELIMITER + portType.getLocalPart());
            writer.writeEndElement();
        }
        if (service != null && !service.getNamespaceURI().equals("") && !service.getLocalPart().equals("")) {
            writer.writeStartElement(AddressingVersion.MEMBER.getPrefix(), AddressingVersion.MEMBER.eprType.serviceName, AddressingVersion.MEMBER.nsUri);
            String servicePrefix = service.getPrefix();
            if (servicePrefix == null || servicePrefix.equals("")) {
                servicePrefix = "wsns";
            }
            writer.writeNamespace(servicePrefix, service.getNamespaceURI());
            if (port != null) {
                writer.writeAttribute(AddressingVersion.MEMBER.eprType.portName, port.getLocalPart());
            }
            writer.writeCharacters(servicePrefix + CallSiteDescriptor.TOKEN_DELIMITER + service.getLocalPart());
            writer.writeEndElement();
        }
    }

    private static void writeWsdl(StreamWriterBufferCreator writer, QName service, String wsdlAddress) throws XMLStreamException {
        writer.writeStartElement("wsdl", WSDLConstants.QNAME_DEFINITIONS.getLocalPart(), "http://schemas.xmlsoap.org/wsdl/");
        writer.writeNamespace("wsdl", "http://schemas.xmlsoap.org/wsdl/");
        writer.writeStartElement("wsdl", WSDLConstants.QNAME_IMPORT.getLocalPart(), "http://schemas.xmlsoap.org/wsdl/");
        writer.writeAttribute(Constants.ATTRNAME_NAMESPACE, service.getNamespaceURI());
        writer.writeAttribute("location", wsdlAddress);
        writer.writeEndElement();
        writer.writeEndElement();
    }

    @Nullable
    public static WSEndpointReference create(@Nullable EndpointReference epr) {
        if (epr != null) {
            return new WSEndpointReference(epr);
        }
        return null;
    }

    @NotNull
    public WSEndpointReference createWithAddress(@NotNull URI newAddress) {
        return createWithAddress(newAddress.toString());
    }

    @NotNull
    public WSEndpointReference createWithAddress(@NotNull URL newAddress) {
        return createWithAddress(newAddress.toString());
    }

    @NotNull
    public WSEndpointReference createWithAddress(@NotNull final String newAddress) {
        MutableXMLStreamBuffer xsb = new MutableXMLStreamBuffer();
        XMLFilterImpl filter = new XMLFilterImpl() { // from class: com.sun.xml.internal.ws.api.addressing.WSEndpointReference.1
            private boolean inAddress = false;

            @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.ContentHandler
            public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
                if (localName.equals("Address") && uri.equals(WSEndpointReference.this.version.nsUri)) {
                    this.inAddress = true;
                }
                super.startElement(uri, localName, qName, atts);
            }

            @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.ContentHandler
            public void characters(char[] ch, int start, int length) throws SAXException {
                if (!this.inAddress) {
                    super.characters(ch, start, length);
                }
            }

            @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.ContentHandler
            public void endElement(String uri, String localName, String qName) throws SAXException {
                if (this.inAddress) {
                    super.characters(newAddress.toCharArray(), 0, newAddress.length());
                }
                this.inAddress = false;
                super.endElement(uri, localName, qName);
            }
        };
        filter.setContentHandler(xsb.createFromSAXBufferCreator());
        try {
            this.infoset.writeTo((ContentHandler) filter, false);
            return new WSEndpointReference(xsb, this.version);
        } catch (SAXException e2) {
            throw new AssertionError(e2);
        }
    }

    @NotNull
    public EndpointReference toSpec() {
        return ProviderImpl.INSTANCE.readEndpointReference(asSource("EndpointReference"));
    }

    @NotNull
    public <T extends EndpointReference> T toSpec(Class<T> cls) {
        return (T) EndpointReferenceUtil.transform(cls, toSpec());
    }

    @NotNull
    public <T> T getPort(@NotNull Service service, @NotNull Class<T> cls, WebServiceFeature... webServiceFeatureArr) {
        return (T) service.getPort(toSpec(), cls, webServiceFeatureArr);
    }

    @NotNull
    public <T> Dispatch<T> createDispatch(@NotNull Service jaxwsService, @NotNull Class<T> type, @NotNull Service.Mode mode, WebServiceFeature... features) {
        return jaxwsService.createDispatch(toSpec(), type, mode, features);
    }

    @NotNull
    public Dispatch<Object> createDispatch(@NotNull Service jaxwsService, @NotNull JAXBContext context, @NotNull Service.Mode mode, WebServiceFeature... features) {
        return jaxwsService.createDispatch(toSpec(), context, mode, features);
    }

    @NotNull
    public AddressingVersion getVersion() {
        return this.version;
    }

    @NotNull
    public String getAddress() {
        return this.address;
    }

    public boolean isAnonymous() {
        return this.address.equals(this.version.anonymousUri);
    }

    public boolean isNone() {
        return this.address.equals(this.version.noneUri);
    }

    private void parse() throws XMLStreamException {
        StreamReaderBufferProcessor xsr = this.infoset.readAsXMLStreamReader();
        if (xsr.getEventType() == 7) {
            xsr.nextTag();
        }
        if (!$assertionsDisabled && xsr.getEventType() != 1) {
            throw new AssertionError();
        }
        String rootLocalName = xsr.getLocalName();
        if (!xsr.getNamespaceURI().equals(this.version.nsUri)) {
            throw new WebServiceException(AddressingMessages.WRONG_ADDRESSING_VERSION(this.version.nsUri, xsr.getNamespaceURI()));
        }
        this.rootElement = new QName(xsr.getNamespaceURI(), rootLocalName);
        List<Header> marks = null;
        while (xsr.nextTag() == 1) {
            String localName = xsr.getLocalName();
            if (this.version.isReferenceParameter(localName)) {
                while (true) {
                    XMLStreamBuffer mark = xsr.nextTagAndMark();
                    if (mark != null) {
                        if (marks == null) {
                            marks = new ArrayList<>();
                        }
                        marks.add(this.version.createReferenceParameterHeader(mark, xsr.getNamespaceURI(), xsr.getLocalName()));
                        XMLStreamReaderUtil.skipElement(xsr);
                    }
                }
            } else if (localName.equals("Address")) {
                if (this.address != null) {
                    throw new InvalidAddressingHeaderException(new QName(this.version.nsUri, rootLocalName), AddressingVersion.fault_duplicateAddressInEpr);
                }
                this.address = xsr.getElementText().trim();
            } else {
                XMLStreamReaderUtil.skipElement(xsr);
            }
        }
        if (marks == null) {
            this.referenceParameters = EMPTY_ARRAY;
        } else {
            this.referenceParameters = (Header[]) marks.toArray(new Header[marks.size()]);
        }
        if (this.address == null) {
            throw new InvalidAddressingHeaderException(new QName(this.version.nsUri, rootLocalName), this.version.fault_missingAddressInEpr);
        }
    }

    public XMLStreamReader read(@NotNull final String localName) throws XMLStreamException {
        return new StreamReaderBufferProcessor(this.infoset) { // from class: com.sun.xml.internal.ws.api.addressing.WSEndpointReference.2
            @Override // com.sun.xml.internal.stream.buffer.stax.StreamReaderBufferProcessor
            protected void processElement(String prefix, String uri, String _localName, boolean inScope) {
                if (this._depth == 0) {
                    _localName = localName;
                }
                super.processElement(prefix, uri, _localName, WSEndpointReference.this.isInscope(WSEndpointReference.this.infoset, this._depth));
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isInscope(XMLStreamBuffer buffer, int depth) {
        return buffer.getInscopeNamespaces().size() > 0 && depth == 0;
    }

    public Source asSource(@NotNull String localName) {
        return new SAXSource(new SAXBufferProcessorImpl(localName), new InputSource());
    }

    public void writeTo(@NotNull String localName, ContentHandler contentHandler, ErrorHandler errorHandler, boolean fragment) throws SAXException {
        SAXBufferProcessorImpl p2 = new SAXBufferProcessorImpl(localName);
        p2.setContentHandler(contentHandler);
        p2.setErrorHandler(errorHandler);
        p2.process(this.infoset, fragment);
    }

    public void writeTo(@NotNull final String localName, @NotNull XMLStreamWriter w2) throws XMLStreamException {
        this.infoset.writeToXMLStreamWriter(new XMLStreamWriterFilter(w2) { // from class: com.sun.xml.internal.ws.api.addressing.WSEndpointReference.3
            private boolean root = true;

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void writeStartDocument() throws XMLStreamException {
            }

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void writeStartDocument(String encoding, String version) throws XMLStreamException {
            }

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void writeStartDocument(String version) throws XMLStreamException {
            }

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void writeEndDocument() throws XMLStreamException {
            }

            private String override(String ln) {
                if (this.root) {
                    this.root = false;
                    return localName;
                }
                return ln;
            }

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void writeStartElement(String localName2) throws XMLStreamException {
                super.writeStartElement(override(localName2));
            }

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void writeStartElement(String namespaceURI, String localName2) throws XMLStreamException {
                super.writeStartElement(namespaceURI, override(localName2));
            }

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void writeStartElement(String prefix, String localName2, String namespaceURI) throws XMLStreamException {
                super.writeStartElement(prefix, override(localName2), namespaceURI);
            }
        }, true);
    }

    public Header createHeader(QName rootTagName) {
        return new EPRHeader(rootTagName, this);
    }

    public void addReferenceParametersToList(HeaderList outbound) {
        for (Header header : this.referenceParameters) {
            outbound.add(header);
        }
    }

    public void addReferenceParametersToList(MessageHeaders outbound) {
        for (Header header : this.referenceParameters) {
            outbound.add(header);
        }
    }

    public void addReferenceParameters(HeaderList headers) {
        if (headers != null) {
            Header[] hs = new Header[this.referenceParameters.length + headers.size()];
            System.arraycopy(this.referenceParameters, 0, hs, 0, this.referenceParameters.length);
            int i2 = this.referenceParameters.length;
            Iterator<Header> it = headers.iterator();
            while (it.hasNext()) {
                Header h2 = it.next();
                int i3 = i2;
                i2++;
                hs[i3] = h2;
            }
            this.referenceParameters = hs;
        }
    }

    public String toString() {
        try {
            StringWriter sw = new StringWriter();
            XmlUtil.newTransformer().transform(asSource("EndpointReference"), new StreamResult(sw));
            return sw.toString();
        } catch (TransformerException e2) {
            return e2.toString();
        }
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLExtension
    public QName getName() {
        return this.rootElement;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/addressing/WSEndpointReference$SAXBufferProcessorImpl.class */
    class SAXBufferProcessorImpl extends SAXBufferProcessor {
        private final String rootLocalName;
        private boolean root;

        public SAXBufferProcessorImpl(String rootLocalName) {
            super(WSEndpointReference.this.infoset, false);
            this.root = true;
            this.rootLocalName = rootLocalName;
        }

        @Override // com.sun.xml.internal.stream.buffer.sax.SAXBufferProcessor
        protected void processElement(String uri, String localName, String qName, boolean inscope) throws SAXException {
            if (this.root) {
                this.root = false;
                if (qName.equals(localName)) {
                    String str = this.rootLocalName;
                    localName = str;
                    qName = str;
                } else {
                    localName = this.rootLocalName;
                    int idx = qName.indexOf(58);
                    qName = qName.substring(0, idx + 1) + this.rootLocalName;
                }
            }
            super.processElement(uri, localName, qName, inscope);
        }
    }

    @Nullable
    public EPRExtension getEPRExtension(QName extnQName) throws XMLStreamException {
        if (this.rootEprExtensions == null) {
            parseEPRExtensions();
        }
        return this.rootEprExtensions.get(extnQName);
    }

    @NotNull
    public Collection<EPRExtension> getEPRExtensions() throws XMLStreamException {
        if (this.rootEprExtensions == null) {
            parseEPRExtensions();
        }
        return this.rootEprExtensions.values();
    }

    private void parseEPRExtensions() throws XMLStreamException {
        this.rootEprExtensions = new HashMap();
        StreamReaderBufferProcessor xsr = this.infoset.readAsXMLStreamReader();
        if (xsr.getEventType() == 7) {
            xsr.nextTag();
        }
        if (!$assertionsDisabled && xsr.getEventType() != 1) {
            throw new AssertionError();
        }
        if (!xsr.getNamespaceURI().equals(this.version.nsUri)) {
            throw new WebServiceException(AddressingMessages.WRONG_ADDRESSING_VERSION(this.version.nsUri, xsr.getNamespaceURI()));
        }
        while (true) {
            XMLStreamBuffer mark = xsr.nextTagAndMark();
            if (mark != null) {
                String localName = xsr.getLocalName();
                String ns = xsr.getNamespaceURI();
                if (this.version.nsUri.equals(ns)) {
                    XMLStreamReaderUtil.skipElement(xsr);
                } else {
                    QName qn = new QName(ns, localName);
                    this.rootEprExtensions.put(qn, new WSEPRExtension(mark, qn));
                    XMLStreamReaderUtil.skipElement(xsr);
                }
            } else {
                return;
            }
        }
    }

    @NotNull
    public Metadata getMetaData() {
        return new Metadata();
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/addressing/WSEndpointReference$Metadata.class */
    public class Metadata {

        @Nullable
        private QName serviceName;

        @Nullable
        private QName portName;

        @Nullable
        private QName portTypeName;

        @Nullable
        private Source wsdlSource;

        @Nullable
        private String wsdliLocation;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !WSEndpointReference.class.desiredAssertionStatus();
        }

        @Nullable
        public QName getServiceName() {
            return this.serviceName;
        }

        @Nullable
        public QName getPortName() {
            return this.portName;
        }

        @Nullable
        public QName getPortTypeName() {
            return this.portTypeName;
        }

        @Nullable
        public Source getWsdlSource() {
            return this.wsdlSource;
        }

        @Nullable
        public String getWsdliLocation() {
            return this.wsdliLocation;
        }

        private Metadata() {
            try {
                parseMetaData();
            } catch (XMLStreamException e2) {
                throw new WebServiceException(e2);
            }
        }

        private void parseMetaData() throws XMLStreamException {
            StreamReaderBufferProcessor xsr = WSEndpointReference.this.infoset.readAsXMLStreamReader();
            if (xsr.getEventType() == 7) {
                xsr.nextTag();
            }
            if (!$assertionsDisabled && xsr.getEventType() != 1) {
                throw new AssertionError();
            }
            String rootElement = xsr.getLocalName();
            if (xsr.getNamespaceURI().equals(WSEndpointReference.this.version.nsUri)) {
                if (WSEndpointReference.this.version != AddressingVersion.W3C) {
                    if (WSEndpointReference.this.version == AddressingVersion.MEMBER) {
                        do {
                            String localName = xsr.getLocalName();
                            String ns = xsr.getNamespaceURI();
                            if (localName.equals(WSEndpointReference.this.version.eprType.wsdlMetadata.getLocalPart()) && ns.equals(WSEndpointReference.this.version.eprType.wsdlMetadata.getNamespaceURI())) {
                                while (xsr.nextTag() == 1) {
                                    while (true) {
                                        XMLStreamBuffer mark = xsr.nextTagAndMark();
                                        if (mark != null) {
                                            String localName2 = xsr.getLocalName();
                                            String ns2 = xsr.getNamespaceURI();
                                            if (ns2.equals("http://schemas.xmlsoap.org/wsdl/") && localName2.equals(WSDLConstants.QNAME_DEFINITIONS.getLocalPart())) {
                                                this.wsdlSource = new XMLStreamBufferSource(mark);
                                            } else {
                                                XMLStreamReaderUtil.skipElement(xsr);
                                            }
                                        }
                                    }
                                }
                            } else if (!localName.equals(WSEndpointReference.this.version.eprType.serviceName)) {
                                if (localName.equals(WSEndpointReference.this.version.eprType.portTypeName)) {
                                    this.portTypeName = getElementTextAsQName(xsr);
                                } else if (!xsr.getLocalName().equals(rootElement)) {
                                    XMLStreamReaderUtil.skipElement(xsr);
                                }
                            } else {
                                String portStr = xsr.getAttributeValue(null, WSEndpointReference.this.version.eprType.portName);
                                this.serviceName = getElementTextAsQName(xsr);
                                if (this.serviceName != null && portStr != null) {
                                    this.portName = new QName(this.serviceName.getNamespaceURI(), portStr);
                                }
                            }
                        } while (XMLStreamReaderUtil.nextElementContent(xsr) == 1);
                        return;
                    }
                    return;
                }
                do {
                    if (xsr.getLocalName().equals(WSEndpointReference.this.version.eprType.wsdlMetadata.getLocalPart())) {
                        String wsdlLoc = xsr.getAttributeValue(W3CAddressingMetadataConstants.WSAM_WSDLI_ATTRIBUTE_NAMESPACE, W3CAddressingMetadataConstants.WSAM_WSDLI_ATTRIBUTE_LOCALNAME);
                        if (wsdlLoc != null) {
                            this.wsdliLocation = wsdlLoc.trim();
                        }
                        while (true) {
                            XMLStreamBuffer mark2 = xsr.nextTagAndMark();
                            if (mark2 == null) {
                                break;
                            }
                            String localName3 = xsr.getLocalName();
                            String ns3 = xsr.getNamespaceURI();
                            if (!localName3.equals(WSEndpointReference.this.version.eprType.serviceName)) {
                                if (localName3.equals(WSEndpointReference.this.version.eprType.portTypeName)) {
                                    if (this.portTypeName != null) {
                                        throw new RuntimeException("More than one " + WSEndpointReference.this.version.eprType.portTypeName + " element in EPR Metadata");
                                    }
                                    this.portTypeName = getElementTextAsQName(xsr);
                                } else if (ns3.equals("http://schemas.xmlsoap.org/wsdl/") && localName3.equals(WSDLConstants.QNAME_DEFINITIONS.getLocalPart())) {
                                    this.wsdlSource = new XMLStreamBufferSource(mark2);
                                } else {
                                    XMLStreamReaderUtil.skipElement(xsr);
                                }
                            } else {
                                String portStr2 = xsr.getAttributeValue(null, WSEndpointReference.this.version.eprType.portName);
                                if (this.serviceName != null) {
                                    throw new RuntimeException("More than one " + WSEndpointReference.this.version.eprType.serviceName + " element in EPR Metadata");
                                }
                                this.serviceName = getElementTextAsQName(xsr);
                                if (this.serviceName != null && portStr2 != null) {
                                    this.portName = new QName(this.serviceName.getNamespaceURI(), portStr2);
                                }
                            }
                        }
                    } else if (!xsr.getLocalName().equals(rootElement)) {
                        XMLStreamReaderUtil.skipElement(xsr);
                    }
                } while (XMLStreamReaderUtil.nextElementContent(xsr) == 1);
                if (this.wsdliLocation != null) {
                    String wsdlLocation = this.wsdliLocation.trim();
                    this.wsdlSource = new StreamSource(wsdlLocation.substring(this.wsdliLocation.lastIndexOf(" ")));
                    return;
                }
                return;
            }
            throw new WebServiceException(AddressingMessages.WRONG_ADDRESSING_VERSION(WSEndpointReference.this.version.nsUri, xsr.getNamespaceURI()));
        }

        private QName getElementTextAsQName(StreamReaderBufferProcessor xsr) throws XMLStreamException {
            String text = xsr.getElementText().trim();
            String prefix = XmlUtil.getPrefix(text);
            String name = XmlUtil.getLocalPart(text);
            if (name != null) {
                if (prefix != null) {
                    String ns = xsr.getNamespaceURI(prefix);
                    if (ns != null) {
                        return new QName(ns, name, prefix);
                    }
                    return null;
                }
                return new QName(null, name);
            }
            return null;
        }
    }
}
