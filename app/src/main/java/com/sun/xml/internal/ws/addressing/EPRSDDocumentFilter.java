package com.sun.xml.internal.ws.addressing;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
import com.sun.xml.internal.ws.api.server.BoundEndpoint;
import com.sun.xml.internal.ws.api.server.Module;
import com.sun.xml.internal.ws.api.server.SDDocument;
import com.sun.xml.internal.ws.api.server.SDDocumentFilter;
import com.sun.xml.internal.ws.api.server.WSEndpoint;
import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
import com.sun.xml.internal.ws.server.WSEndpointImpl;
import com.sun.xml.internal.ws.util.xml.XMLStreamReaderToXMLStreamWriter;
import com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter;
import com.sun.xml.internal.ws.wsdl.parser.WSDLConstants;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/* loaded from: rt.jar:com/sun/xml/internal/ws/addressing/EPRSDDocumentFilter.class */
public class EPRSDDocumentFilter implements SDDocumentFilter {
    private final WSEndpointImpl<?> endpoint;
    List<BoundEndpoint> beList;

    public EPRSDDocumentFilter(@NotNull WSEndpointImpl<?> endpoint) {
        this.endpoint = endpoint;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Nullable
    public WSEndpointImpl<?> getEndpoint(String serviceName, String portName) {
        if (serviceName == null || portName == null) {
            return null;
        }
        if (this.endpoint.getServiceName().getLocalPart().equals(serviceName) && this.endpoint.getPortName().getLocalPart().equals(portName)) {
            return this.endpoint;
        }
        if (this.beList == null) {
            Module module = (Module) this.endpoint.getContainer().getSPI(Module.class);
            if (module != null) {
                this.beList = module.getBoundEndpoints();
            } else {
                this.beList = Collections.emptyList();
            }
        }
        for (BoundEndpoint be2 : this.beList) {
            WSEndpoint wse = be2.getEndpoint();
            if (wse.getServiceName().getLocalPart().equals(serviceName) && wse.getPortName().getLocalPart().equals(portName)) {
                return (WSEndpointImpl) wse;
            }
        }
        return null;
    }

    @Override // com.sun.xml.internal.ws.api.server.SDDocumentFilter
    public XMLStreamWriter filter(SDDocument doc, XMLStreamWriter w2) throws XMLStreamException, IOException {
        if (!doc.isWSDL()) {
            return w2;
        }
        return new XMLStreamWriterFilter(w2) { // from class: com.sun.xml.internal.ws.addressing.EPRSDDocumentFilter.1
            private String portAddress;
            private boolean eprExtnFilterON = false;
            private boolean portHasEPR = false;
            private int eprDepth = -1;
            private String serviceName = null;
            private boolean onService = false;
            private int serviceDepth = -1;
            private String portName = null;
            private boolean onPort = false;
            private int portDepth = -1;
            private boolean onPortAddress = false;

            private void handleStartElement(String localName, String namespaceURI) throws XMLStreamException {
                resetOnElementFlags();
                if (this.serviceDepth >= 0) {
                    this.serviceDepth++;
                }
                if (this.portDepth >= 0) {
                    this.portDepth++;
                }
                if (this.eprDepth >= 0) {
                    this.eprDepth++;
                }
                if (namespaceURI.equals(WSDLConstants.QNAME_SERVICE.getNamespaceURI()) && localName.equals(WSDLConstants.QNAME_SERVICE.getLocalPart())) {
                    this.onService = true;
                    this.serviceDepth = 0;
                } else if (namespaceURI.equals(WSDLConstants.QNAME_PORT.getNamespaceURI()) && localName.equals(WSDLConstants.QNAME_PORT.getLocalPart())) {
                    if (this.serviceDepth >= 1) {
                        this.onPort = true;
                        this.portDepth = 0;
                    }
                } else if (namespaceURI.equals(W3CAddressingConstants.WSA_NAMESPACE_NAME) && localName.equals("EndpointReference")) {
                    if (this.serviceDepth >= 1 && this.portDepth >= 1) {
                        this.portHasEPR = true;
                        this.eprDepth = 0;
                    }
                } else if ((namespaceURI.equals(WSDLConstants.NS_SOAP_BINDING_ADDRESS.getNamespaceURI()) || namespaceURI.equals(WSDLConstants.NS_SOAP12_BINDING_ADDRESS.getNamespaceURI())) && localName.equals("address") && this.portDepth == 1) {
                    this.onPortAddress = true;
                }
                WSEndpoint endpoint = EPRSDDocumentFilter.this.getEndpoint(this.serviceName, this.portName);
                if (endpoint != null && this.eprDepth == 1 && !namespaceURI.equals(W3CAddressingConstants.WSA_NAMESPACE_NAME)) {
                    this.eprExtnFilterON = true;
                }
            }

            private void resetOnElementFlags() {
                if (this.onService) {
                    this.onService = false;
                }
                if (this.onPort) {
                    this.onPort = false;
                }
                if (this.onPortAddress) {
                    this.onPortAddress = false;
                }
            }

            private void writeEPRExtensions(Collection<WSEndpointReference.EPRExtension> eprExtns) throws XMLStreamException {
                if (eprExtns != null) {
                    for (WSEndpointReference.EPRExtension e2 : eprExtns) {
                        XMLStreamReaderToXMLStreamWriter c2 = new XMLStreamReaderToXMLStreamWriter();
                        XMLStreamReader r2 = e2.readAsXMLStreamReader();
                        c2.bridge(r2, this.writer);
                        XMLStreamReaderFactory.recycle(r2);
                    }
                }
            }

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
                handleStartElement(localName, namespaceURI);
                if (!this.eprExtnFilterON) {
                    super.writeStartElement(prefix, localName, namespaceURI);
                }
            }

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
                handleStartElement(localName, namespaceURI);
                if (!this.eprExtnFilterON) {
                    super.writeStartElement(namespaceURI, localName);
                }
            }

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void writeStartElement(String localName) throws XMLStreamException {
                if (!this.eprExtnFilterON) {
                    super.writeStartElement(localName);
                }
            }

            private void handleEndElement() throws XMLStreamException {
                resetOnElementFlags();
                if (this.portDepth == 0 && !this.portHasEPR && EPRSDDocumentFilter.this.getEndpoint(this.serviceName, this.portName) != null) {
                    this.writer.writeStartElement(AddressingVersion.W3C.getPrefix(), "EndpointReference", AddressingVersion.W3C.nsUri);
                    this.writer.writeNamespace(AddressingVersion.W3C.getPrefix(), AddressingVersion.W3C.nsUri);
                    this.writer.writeStartElement(AddressingVersion.W3C.getPrefix(), AddressingVersion.W3C.eprType.address, AddressingVersion.W3C.nsUri);
                    this.writer.writeCharacters(this.portAddress);
                    this.writer.writeEndElement();
                    writeEPRExtensions(EPRSDDocumentFilter.this.getEndpoint(this.serviceName, this.portName).getEndpointReferenceExtensions());
                    this.writer.writeEndElement();
                }
                if (this.eprDepth == 0) {
                    if (this.portHasEPR && EPRSDDocumentFilter.this.getEndpoint(this.serviceName, this.portName) != null) {
                        writeEPRExtensions(EPRSDDocumentFilter.this.getEndpoint(this.serviceName, this.portName).getEndpointReferenceExtensions());
                    }
                    this.eprExtnFilterON = false;
                }
                if (this.serviceDepth >= 0) {
                    this.serviceDepth--;
                }
                if (this.portDepth >= 0) {
                    this.portDepth--;
                }
                if (this.eprDepth >= 0) {
                    this.eprDepth--;
                }
                if (this.serviceDepth == -1) {
                    this.serviceName = null;
                }
                if (this.portDepth == -1) {
                    this.portHasEPR = false;
                    this.portAddress = null;
                    this.portName = null;
                }
            }

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void writeEndElement() throws XMLStreamException {
                handleEndElement();
                if (!this.eprExtnFilterON) {
                    super.writeEndElement();
                }
            }

            private void handleAttribute(String localName, String value) {
                if (localName.equals("name")) {
                    if (this.onService) {
                        this.serviceName = value;
                        this.onService = false;
                    } else if (this.onPort) {
                        this.portName = value;
                        this.onPort = false;
                    }
                }
                if (localName.equals("location") && this.onPortAddress) {
                    this.portAddress = value;
                }
            }

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void writeAttribute(String prefix, String namespaceURI, String localName, String value) throws XMLStreamException {
                handleAttribute(localName, value);
                if (!this.eprExtnFilterON) {
                    super.writeAttribute(prefix, namespaceURI, localName, value);
                }
            }

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException {
                handleAttribute(localName, value);
                if (!this.eprExtnFilterON) {
                    super.writeAttribute(namespaceURI, localName, value);
                }
            }

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void writeAttribute(String localName, String value) throws XMLStreamException {
                handleAttribute(localName, value);
                if (!this.eprExtnFilterON) {
                    super.writeAttribute(localName, value);
                }
            }

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException {
                if (!this.eprExtnFilterON) {
                    super.writeEmptyElement(namespaceURI, localName);
                }
            }

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException {
                if (!this.eprExtnFilterON) {
                    super.writeNamespace(prefix, namespaceURI);
                }
            }

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void setNamespaceContext(NamespaceContext context) throws XMLStreamException {
                if (!this.eprExtnFilterON) {
                    super.setNamespaceContext(context);
                }
            }

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void setDefaultNamespace(String uri) throws XMLStreamException {
                if (!this.eprExtnFilterON) {
                    super.setDefaultNamespace(uri);
                }
            }

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void setPrefix(String prefix, String uri) throws XMLStreamException {
                if (!this.eprExtnFilterON) {
                    super.setPrefix(prefix, uri);
                }
            }

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void writeProcessingInstruction(String target, String data) throws XMLStreamException {
                if (!this.eprExtnFilterON) {
                    super.writeProcessingInstruction(target, data);
                }
            }

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
                if (!this.eprExtnFilterON) {
                    super.writeEmptyElement(prefix, localName, namespaceURI);
                }
            }

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void writeCData(String data) throws XMLStreamException {
                if (!this.eprExtnFilterON) {
                    super.writeCData(data);
                }
            }

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void writeCharacters(String text) throws XMLStreamException {
                if (!this.eprExtnFilterON) {
                    super.writeCharacters(text);
                }
            }

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void writeComment(String data) throws XMLStreamException {
                if (!this.eprExtnFilterON) {
                    super.writeComment(data);
                }
            }

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void writeDTD(String dtd) throws XMLStreamException {
                if (!this.eprExtnFilterON) {
                    super.writeDTD(dtd);
                }
            }

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void writeDefaultNamespace(String namespaceURI) throws XMLStreamException {
                if (!this.eprExtnFilterON) {
                    super.writeDefaultNamespace(namespaceURI);
                }
            }

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void writeEmptyElement(String localName) throws XMLStreamException {
                if (!this.eprExtnFilterON) {
                    super.writeEmptyElement(localName);
                }
            }

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void writeEntityRef(String name) throws XMLStreamException {
                if (!this.eprExtnFilterON) {
                    super.writeEntityRef(name);
                }
            }

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void writeProcessingInstruction(String target) throws XMLStreamException {
                if (!this.eprExtnFilterON) {
                    super.writeProcessingInstruction(target);
                }
            }

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void writeCharacters(char[] text, int start, int len) throws XMLStreamException {
                if (!this.eprExtnFilterON) {
                    super.writeCharacters(text, start, len);
                }
            }
        };
    }
}
