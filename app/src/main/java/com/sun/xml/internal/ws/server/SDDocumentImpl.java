package com.sun.xml.internal.ws.server;

import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.server.DocumentAddressResolver;
import com.sun.xml.internal.ws.api.server.PortAddressResolver;
import com.sun.xml.internal.ws.api.server.SDDocument;
import com.sun.xml.internal.ws.api.server.SDDocumentFilter;
import com.sun.xml.internal.ws.api.server.SDDocumentSource;
import com.sun.xml.internal.ws.api.streaming.XMLStreamWriterFactory;
import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil;
import com.sun.xml.internal.ws.util.RuntimeVersion;
import com.sun.xml.internal.ws.util.xml.XMLStreamReaderToXMLStreamWriter;
import com.sun.xml.internal.ws.wsdl.SDDocumentResolver;
import com.sun.xml.internal.ws.wsdl.parser.ParserUtil;
import com.sun.xml.internal.ws.wsdl.parser.WSDLConstants;
import com.sun.xml.internal.ws.wsdl.writer.DocumentLocationResolver;
import com.sun.xml.internal.ws.wsdl.writer.WSDLPatcher;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/server/SDDocumentImpl.class */
public class SDDocumentImpl extends SDDocumentSource implements SDDocument {
    private static final String NS_XSD = "http://www.w3.org/2001/XMLSchema";
    private static final QName SCHEMA_INCLUDE_QNAME = new QName("http://www.w3.org/2001/XMLSchema", "include");
    private static final QName SCHEMA_IMPORT_QNAME = new QName("http://www.w3.org/2001/XMLSchema", "import");
    private static final QName SCHEMA_REDEFINE_QNAME = new QName("http://www.w3.org/2001/XMLSchema", "redefine");
    private static final String VERSION_COMMENT = " Published by JAX-WS RI (http://jax-ws.java.net). RI's version is " + ((Object) RuntimeVersion.VERSION) + ". ";
    private final QName rootName;
    private final SDDocumentSource source;

    @Nullable
    List<SDDocumentFilter> filters;

    @Nullable
    SDDocumentResolver sddocResolver;
    private final URL url;
    private final Set<String> imports;

    public static SDDocumentImpl create(SDDocumentSource src, QName serviceName, QName portTypeName) {
        String importedDoc;
        String importedDoc2;
        URL systemId = src.getSystemId();
        try {
            XMLStreamReader reader = src.read();
            try {
                XMLStreamReaderUtil.nextElementContent(reader);
                QName rootName = reader.getName();
                if (rootName.equals(WSDLConstants.QNAME_SCHEMA)) {
                    String tns = ParserUtil.getMandatoryNonEmptyAttribute(reader, WSDLConstants.ATTR_TNS);
                    Set<String> importedDocs = new HashSet<>();
                    while (XMLStreamReaderUtil.nextContent(reader) != 8) {
                        if (reader.getEventType() == 1) {
                            QName name = reader.getName();
                            if ((SCHEMA_INCLUDE_QNAME.equals(name) || SCHEMA_IMPORT_QNAME.equals(name) || SCHEMA_REDEFINE_QNAME.equals(name)) && (importedDoc2 = reader.getAttributeValue(null, "schemaLocation")) != null) {
                                importedDocs.add(new URL(src.getSystemId(), importedDoc2).toString());
                            }
                        }
                    }
                    SchemaImpl schemaImpl = new SchemaImpl(rootName, systemId, src, tns, importedDocs);
                    reader.close();
                    return schemaImpl;
                }
                if (!rootName.equals(WSDLConstants.QNAME_DEFINITIONS)) {
                    SDDocumentImpl sDDocumentImpl = new SDDocumentImpl(rootName, systemId, src);
                    reader.close();
                    return sDDocumentImpl;
                }
                String tns2 = ParserUtil.getMandatoryNonEmptyAttribute(reader, WSDLConstants.ATTR_TNS);
                boolean hasPortType = false;
                boolean hasService = false;
                Set<String> importedDocs2 = new HashSet<>();
                Set<QName> allServices = new HashSet<>();
                while (XMLStreamReaderUtil.nextContent(reader) != 8) {
                    if (reader.getEventType() == 1) {
                        QName name2 = reader.getName();
                        if (WSDLConstants.QNAME_PORT_TYPE.equals(name2)) {
                            String pn = ParserUtil.getMandatoryNonEmptyAttribute(reader, "name");
                            if (portTypeName != null && portTypeName.getLocalPart().equals(pn) && portTypeName.getNamespaceURI().equals(tns2)) {
                                hasPortType = true;
                            }
                        } else if (WSDLConstants.QNAME_SERVICE.equals(name2)) {
                            String sn = ParserUtil.getMandatoryNonEmptyAttribute(reader, "name");
                            QName sqn = new QName(tns2, sn);
                            allServices.add(sqn);
                            if (serviceName.equals(sqn)) {
                                hasService = true;
                            }
                        } else if (WSDLConstants.QNAME_IMPORT.equals(name2)) {
                            String importedDoc3 = reader.getAttributeValue(null, "location");
                            if (importedDoc3 != null) {
                                importedDocs2.add(new URL(src.getSystemId(), importedDoc3).toString());
                            }
                        } else if ((SCHEMA_INCLUDE_QNAME.equals(name2) || SCHEMA_IMPORT_QNAME.equals(name2) || SCHEMA_REDEFINE_QNAME.equals(name2)) && (importedDoc = reader.getAttributeValue(null, "schemaLocation")) != null) {
                            importedDocs2.add(new URL(src.getSystemId(), importedDoc).toString());
                        }
                    }
                }
                WSDLImpl wSDLImpl = new WSDLImpl(rootName, systemId, src, tns2, hasPortType, hasService, importedDocs2, allServices);
                reader.close();
                return wSDLImpl;
            } catch (Throwable th) {
                reader.close();
                throw th;
            }
        } catch (IOException e2) {
            throw new ServerRtException("runtime.parser.wsdl", systemId, e2);
        } catch (XMLStreamException e3) {
            throw new ServerRtException("runtime.parser.wsdl", systemId, e3);
        } catch (WebServiceException e4) {
            throw new ServerRtException("runtime.parser.wsdl", systemId, e4);
        }
    }

    protected SDDocumentImpl(QName rootName, URL url, SDDocumentSource source) {
        this(rootName, url, source, new HashSet());
    }

    protected SDDocumentImpl(QName rootName, URL url, SDDocumentSource source, Set<String> imports) {
        if (url == null) {
            throw new IllegalArgumentException("Cannot construct SDDocument with null URL.");
        }
        this.rootName = rootName;
        this.source = source;
        this.url = url;
        this.imports = imports;
    }

    void setFilters(List<SDDocumentFilter> filters) {
        this.filters = filters;
    }

    void setResolver(SDDocumentResolver sddocResolver) {
        this.sddocResolver = sddocResolver;
    }

    @Override // com.sun.xml.internal.ws.api.server.SDDocument
    public QName getRootName() {
        return this.rootName;
    }

    @Override // com.sun.xml.internal.ws.api.server.SDDocument
    public boolean isWSDL() {
        return false;
    }

    @Override // com.sun.xml.internal.ws.api.server.SDDocument
    public boolean isSchema() {
        return false;
    }

    @Override // com.sun.xml.internal.ws.api.server.SDDocument
    public URL getURL() {
        return this.url;
    }

    @Override // com.sun.xml.internal.ws.api.server.SDDocumentSource
    public XMLStreamReader read(XMLInputFactory xif) throws XMLStreamException, IOException {
        return this.source.read(xif);
    }

    @Override // com.sun.xml.internal.ws.api.server.SDDocumentSource
    public XMLStreamReader read() throws XMLStreamException, IOException {
        return this.source.read();
    }

    @Override // com.sun.xml.internal.ws.api.server.SDDocumentSource
    public URL getSystemId() {
        return this.url;
    }

    @Override // com.sun.xml.internal.ws.api.server.SDDocument
    public Set<String> getImports() {
        return this.imports;
    }

    public void writeTo(OutputStream os) throws IOException {
        XMLStreamWriter w2 = null;
        try {
            try {
                w2 = XMLStreamWriterFactory.create(os, "UTF-8");
                w2.writeStartDocument("UTF-8", "1.0");
                new XMLStreamReaderToXMLStreamWriter().bridge(this.source.read(), w2);
                w2.writeEndDocument();
                if (w2 != null) {
                    try {
                        w2.close();
                    } catch (XMLStreamException e2) {
                        IOException ioe = new IOException(e2.getMessage());
                        ioe.initCause(e2);
                        throw ioe;
                    }
                }
            } catch (XMLStreamException e3) {
                IOException ioe2 = new IOException(e3.getMessage());
                ioe2.initCause(e3);
                throw ioe2;
            }
        } catch (Throwable th) {
            if (w2 != null) {
                try {
                    w2.close();
                } catch (XMLStreamException e4) {
                    IOException ioe3 = new IOException(e4.getMessage());
                    ioe3.initCause(e4);
                    throw ioe3;
                }
            }
            throw th;
        }
    }

    @Override // com.sun.xml.internal.ws.api.server.SDDocument
    public void writeTo(PortAddressResolver portAddressResolver, DocumentAddressResolver resolver, OutputStream os) throws IOException {
        XMLStreamWriter w2 = null;
        try {
            try {
                w2 = XMLStreamWriterFactory.create(os, "UTF-8");
                w2.writeStartDocument("UTF-8", "1.0");
                writeTo(portAddressResolver, resolver, w2);
                w2.writeEndDocument();
                if (w2 != null) {
                    try {
                        w2.close();
                    } catch (XMLStreamException e2) {
                        IOException ioe = new IOException(e2.getMessage());
                        ioe.initCause(e2);
                        throw ioe;
                    }
                }
            } catch (XMLStreamException e3) {
                IOException ioe2 = new IOException(e3.getMessage());
                ioe2.initCause(e3);
                throw ioe2;
            }
        } catch (Throwable th) {
            if (w2 != null) {
                try {
                    w2.close();
                } catch (XMLStreamException e4) {
                    IOException ioe3 = new IOException(e4.getMessage());
                    ioe3.initCause(e4);
                    throw ioe3;
                }
            }
            throw th;
        }
    }

    @Override // com.sun.xml.internal.ws.api.server.SDDocument
    public void writeTo(PortAddressResolver portAddressResolver, DocumentAddressResolver resolver, XMLStreamWriter out) throws XMLStreamException, IOException {
        if (this.filters != null) {
            for (SDDocumentFilter f2 : this.filters) {
                out = f2.filter(this, out);
            }
        }
        XMLStreamReader xsr = this.source.read();
        try {
            out.writeComment(VERSION_COMMENT);
            new WSDLPatcher(portAddressResolver, new DocumentLocationResolverImpl(resolver)).bridge(xsr, out);
            xsr.close();
        } catch (Throwable th) {
            xsr.close();
            throw th;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/SDDocumentImpl$SchemaImpl.class */
    private static final class SchemaImpl extends SDDocumentImpl implements SDDocument.Schema {
        private final String targetNamespace;

        public SchemaImpl(QName rootName, URL url, SDDocumentSource source, String targetNamespace, Set<String> imports) {
            super(rootName, url, source, imports);
            this.targetNamespace = targetNamespace;
        }

        @Override // com.sun.xml.internal.ws.api.server.SDDocument.Schema
        public String getTargetNamespace() {
            return this.targetNamespace;
        }

        @Override // com.sun.xml.internal.ws.server.SDDocumentImpl, com.sun.xml.internal.ws.api.server.SDDocument
        public boolean isSchema() {
            return true;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/SDDocumentImpl$WSDLImpl.class */
    private static final class WSDLImpl extends SDDocumentImpl implements SDDocument.WSDL {
        private final String targetNamespace;
        private final boolean hasPortType;
        private final boolean hasService;
        private final Set<QName> allServices;

        public WSDLImpl(QName rootName, URL url, SDDocumentSource source, String targetNamespace, boolean hasPortType, boolean hasService, Set<String> imports, Set<QName> allServices) {
            super(rootName, url, source, imports);
            this.targetNamespace = targetNamespace;
            this.hasPortType = hasPortType;
            this.hasService = hasService;
            this.allServices = allServices;
        }

        @Override // com.sun.xml.internal.ws.api.server.SDDocument.WSDL
        public String getTargetNamespace() {
            return this.targetNamespace;
        }

        @Override // com.sun.xml.internal.ws.api.server.SDDocument.WSDL
        public boolean hasPortType() {
            return this.hasPortType;
        }

        @Override // com.sun.xml.internal.ws.api.server.SDDocument.WSDL
        public boolean hasService() {
            return this.hasService;
        }

        @Override // com.sun.xml.internal.ws.api.server.SDDocument.WSDL
        public Set<QName> getAllServices() {
            return this.allServices;
        }

        @Override // com.sun.xml.internal.ws.server.SDDocumentImpl, com.sun.xml.internal.ws.api.server.SDDocument
        public boolean isWSDL() {
            return true;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/SDDocumentImpl$DocumentLocationResolverImpl.class */
    private class DocumentLocationResolverImpl implements DocumentLocationResolver {
        private DocumentAddressResolver delegate;

        DocumentLocationResolverImpl(DocumentAddressResolver delegate) {
            this.delegate = delegate;
        }

        @Override // com.sun.xml.internal.ws.wsdl.writer.DocumentLocationResolver
        public String getLocationFor(String namespaceURI, String systemId) {
            if (SDDocumentImpl.this.sddocResolver == null) {
                return systemId;
            }
            try {
                URL ref = new URL(SDDocumentImpl.this.getURL(), systemId);
                SDDocument refDoc = SDDocumentImpl.this.sddocResolver.resolve(ref.toExternalForm());
                if (refDoc == null) {
                    return systemId;
                }
                return this.delegate.getRelativeAddressFor(SDDocumentImpl.this, refDoc);
            } catch (MalformedURLException e2) {
                return null;
            }
        }
    }
}
