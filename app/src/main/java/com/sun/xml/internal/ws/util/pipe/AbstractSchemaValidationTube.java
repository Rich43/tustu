package com.sun.xml.internal.ws.util.pipe;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.stream.buffer.XMLStreamBufferResult;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.api.pipe.TubeCloner;
import com.sun.xml.internal.ws.api.pipe.helper.AbstractFilterTubeImpl;
import com.sun.xml.internal.ws.api.server.DocumentAddressResolver;
import com.sun.xml.internal.ws.api.server.PortAddressResolver;
import com.sun.xml.internal.ws.api.server.SDDocument;
import com.sun.xml.internal.ws.api.server.SDDocumentSource;
import com.sun.xml.internal.ws.developer.SchemaValidationFeature;
import com.sun.xml.internal.ws.developer.ValidationErrorHandler;
import com.sun.xml.internal.ws.server.SDDocumentImpl;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;
import com.sun.xml.internal.ws.util.xml.XmlUtil;
import com.sun.xml.internal.ws.wsdl.SDDocumentResolver;
import com.sun.xml.internal.ws.wsdl.parser.WSDLConstants;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.ws.WebServiceException;
import org.icepdf.core.util.PdfOps;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.NamespaceSupport;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/pipe/AbstractSchemaValidationTube.class */
public abstract class AbstractSchemaValidationTube extends AbstractFilterTubeImpl {
    private static final Logger LOGGER;
    protected final WSBinding binding;
    protected final SchemaValidationFeature feature;
    protected final DocumentAddressResolver resolver;
    protected final SchemaFactory sf;
    static final /* synthetic */ boolean $assertionsDisabled;

    protected abstract Validator getValidator();

    protected abstract boolean isNoValidation();

    static {
        $assertionsDisabled = !AbstractSchemaValidationTube.class.desiredAssertionStatus();
        LOGGER = Logger.getLogger(AbstractSchemaValidationTube.class.getName());
    }

    public AbstractSchemaValidationTube(WSBinding binding, Tube next) {
        super(next);
        this.resolver = new ValidationDocumentAddressResolver();
        this.binding = binding;
        this.feature = (SchemaValidationFeature) binding.getFeature(SchemaValidationFeature.class);
        this.sf = XmlUtil.allowExternalAccess(SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema"), DeploymentDescriptorParser.ATTR_FILE, false);
    }

    protected AbstractSchemaValidationTube(AbstractSchemaValidationTube that, TubeCloner cloner) {
        super(that, cloner);
        this.resolver = new ValidationDocumentAddressResolver();
        this.binding = that.binding;
        this.feature = that.feature;
        this.sf = that.sf;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/util/pipe/AbstractSchemaValidationTube$ValidationDocumentAddressResolver.class */
    private static class ValidationDocumentAddressResolver implements DocumentAddressResolver {
        private ValidationDocumentAddressResolver() {
        }

        @Override // com.sun.xml.internal.ws.api.server.DocumentAddressResolver
        @Nullable
        public String getRelativeAddressFor(@NotNull SDDocument current, @NotNull SDDocument referenced) {
            AbstractSchemaValidationTube.LOGGER.log(Level.FINE, "Current = {0} resolved relative={1}", new Object[]{current.getURL(), referenced.getURL()});
            return referenced.getURL().toExternalForm();
        }
    }

    private Document createDOM(SDDocument doc) {
        ByteArrayBuffer bab = new ByteArrayBuffer();
        try {
            doc.writeTo((PortAddressResolver) null, this.resolver, bab);
            Transformer trans = XmlUtil.newTransformer();
            Source source = new StreamSource(bab.newInputStream(), (String) null);
            DOMResult result = new DOMResult();
            try {
                trans.transform(source, result);
                return (Document) result.getNode();
            } catch (TransformerException te) {
                throw new WebServiceException(te);
            }
        } catch (IOException ioe) {
            throw new WebServiceException(ioe);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/util/pipe/AbstractSchemaValidationTube$MetadataResolverImpl.class */
    protected class MetadataResolverImpl implements SDDocumentResolver, LSResourceResolver {
        final Map<String, SDDocument> docs = new HashMap();
        final Map<String, SDDocument> nsMapping = new HashMap();
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !AbstractSchemaValidationTube.class.desiredAssertionStatus();
        }

        public MetadataResolverImpl() {
        }

        public MetadataResolverImpl(Iterable<SDDocument> it) {
            for (SDDocument doc : it) {
                if (doc.isSchema()) {
                    this.docs.put(doc.getURL().toExternalForm(), doc);
                    this.nsMapping.put(((SDDocument.Schema) doc).getTargetNamespace(), doc);
                }
            }
        }

        void addSchema(Source schema) {
            if (!$assertionsDisabled && schema.getSystemId() == null) {
                throw new AssertionError();
            }
            String systemId = schema.getSystemId();
            try {
                XMLStreamBufferResult xsbr = (XMLStreamBufferResult) XmlUtil.identityTransform(schema, new XMLStreamBufferResult());
                SDDocumentSource sds = SDDocumentSource.create(new URL(systemId), xsbr.getXMLStreamBuffer());
                SDDocument sdoc = SDDocumentImpl.create(sds, new QName(""), new QName(""));
                this.docs.put(systemId, sdoc);
                this.nsMapping.put(((SDDocument.Schema) sdoc).getTargetNamespace(), sdoc);
            } catch (Exception ex) {
                AbstractSchemaValidationTube.LOGGER.log(Level.WARNING, "Exception in adding schemas to resolver", (Throwable) ex);
            }
        }

        void addSchemas(Collection<? extends Source> schemas) {
            for (Source src : schemas) {
                addSchema(src);
            }
        }

        @Override // com.sun.xml.internal.ws.wsdl.SDDocumentResolver
        public SDDocument resolve(String systemId) {
            SDDocument sdi = this.docs.get(systemId);
            if (sdi == null) {
                try {
                    SDDocumentSource sds = SDDocumentSource.create(new URL(systemId));
                    sdi = SDDocumentImpl.create(sds, new QName(""), new QName(""));
                    this.docs.put(systemId, sdi);
                } catch (MalformedURLException e2) {
                    throw new WebServiceException(e2);
                }
            }
            return sdi;
        }

        @Override // org.w3c.dom.ls.LSResourceResolver
        public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
            SDDocument doc;
            if (AbstractSchemaValidationTube.LOGGER.isLoggable(Level.FINE)) {
                AbstractSchemaValidationTube.LOGGER.log(Level.FINE, "type={0} namespaceURI={1} publicId={2} systemId={3} baseURI={4}", new Object[]{type, namespaceURI, publicId, systemId, baseURI});
            }
            try {
                if (systemId == null) {
                    doc = this.nsMapping.get(namespaceURI);
                } else {
                    URI rel = baseURI != null ? new URI(baseURI).resolve(systemId) : new URI(systemId);
                    doc = this.docs.get(rel.toString());
                }
                if (doc != null) {
                    final SDDocument sDDocument = doc;
                    return new LSInput() { // from class: com.sun.xml.internal.ws.util.pipe.AbstractSchemaValidationTube.MetadataResolverImpl.1
                        @Override // org.w3c.dom.ls.LSInput
                        public Reader getCharacterStream() {
                            return null;
                        }

                        @Override // org.w3c.dom.ls.LSInput
                        public void setCharacterStream(Reader characterStream) {
                            throw new UnsupportedOperationException();
                        }

                        @Override // org.w3c.dom.ls.LSInput
                        public InputStream getByteStream() {
                            ByteArrayBuffer bab = new ByteArrayBuffer();
                            try {
                                sDDocument.writeTo((PortAddressResolver) null, AbstractSchemaValidationTube.this.resolver, bab);
                                return bab.newInputStream();
                            } catch (IOException ioe) {
                                throw new WebServiceException(ioe);
                            }
                        }

                        @Override // org.w3c.dom.ls.LSInput
                        public void setByteStream(InputStream byteStream) {
                            throw new UnsupportedOperationException();
                        }

                        @Override // org.w3c.dom.ls.LSInput
                        public String getStringData() {
                            return null;
                        }

                        @Override // org.w3c.dom.ls.LSInput
                        public void setStringData(String stringData) {
                            throw new UnsupportedOperationException();
                        }

                        @Override // org.w3c.dom.ls.LSInput
                        public String getSystemId() {
                            return sDDocument.getURL().toExternalForm();
                        }

                        @Override // org.w3c.dom.ls.LSInput
                        public void setSystemId(String systemId2) {
                            throw new UnsupportedOperationException();
                        }

                        @Override // org.w3c.dom.ls.LSInput
                        public String getPublicId() {
                            return null;
                        }

                        @Override // org.w3c.dom.ls.LSInput
                        public void setPublicId(String publicId2) {
                            throw new UnsupportedOperationException();
                        }

                        @Override // org.w3c.dom.ls.LSInput
                        public String getBaseURI() {
                            return sDDocument.getURL().toExternalForm();
                        }

                        @Override // org.w3c.dom.ls.LSInput
                        public void setBaseURI(String baseURI2) {
                            throw new UnsupportedOperationException();
                        }

                        @Override // org.w3c.dom.ls.LSInput
                        public String getEncoding() {
                            return null;
                        }

                        @Override // org.w3c.dom.ls.LSInput
                        public void setEncoding(String encoding) {
                            throw new UnsupportedOperationException();
                        }

                        @Override // org.w3c.dom.ls.LSInput
                        public boolean getCertifiedText() {
                            return false;
                        }

                        @Override // org.w3c.dom.ls.LSInput
                        public void setCertifiedText(boolean certifiedText) {
                            throw new UnsupportedOperationException();
                        }
                    };
                }
            } catch (Exception e2) {
                AbstractSchemaValidationTube.LOGGER.log(Level.WARNING, "Exception in LSResourceResolver impl", (Throwable) e2);
            }
            if (AbstractSchemaValidationTube.LOGGER.isLoggable(Level.FINE)) {
                AbstractSchemaValidationTube.LOGGER.log(Level.FINE, "Don''t know about systemId={0} baseURI={1}", new Object[]{systemId, baseURI});
                return null;
            }
            return null;
        }
    }

    private void updateMultiSchemaForTns(String tns, String systemId, Map<String, List<String>> schemas) {
        List<String> docIdList = schemas.get(tns);
        if (docIdList == null) {
            docIdList = new ArrayList();
            schemas.put(tns, docIdList);
        }
        docIdList.add(systemId);
    }

    protected Source[] getSchemaSources(Iterable<SDDocument> docs, MetadataResolverImpl mdresolver) throws DOMException {
        String systemId;
        Map<String, DOMSource> inlinedSchemas = new HashMap<>();
        Map<String, List<String>> multiSchemaForTns = new HashMap<>();
        for (SDDocument sdoc : docs) {
            if (sdoc.isWSDL()) {
                Document dom = createDOM(sdoc);
                addSchemaFragmentSource(dom, sdoc.getURL().toExternalForm(), inlinedSchemas);
            } else if (sdoc.isSchema()) {
                updateMultiSchemaForTns(((SDDocument.Schema) sdoc).getTargetNamespace(), sdoc.getURL().toExternalForm(), multiSchemaForTns);
            }
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "WSDL inlined schema fragment documents(these are used to create a pseudo schema) = {0}", inlinedSchemas.keySet());
        }
        for (DOMSource src : inlinedSchemas.values()) {
            String tns = getTargetNamespace(src);
            updateMultiSchemaForTns(tns, src.getSystemId(), multiSchemaForTns);
        }
        if (multiSchemaForTns.isEmpty()) {
            return new Source[0];
        }
        if (multiSchemaForTns.size() == 1 && multiSchemaForTns.values().iterator().next().size() == 1) {
            String systemId2 = multiSchemaForTns.values().iterator().next().get(0);
            return new Source[]{inlinedSchemas.get(systemId2)};
        }
        mdresolver.addSchemas(inlinedSchemas.values());
        Map<String, String> oneSchemaForTns = new HashMap<>();
        int i2 = 0;
        for (Map.Entry<String, List<String>> e2 : multiSchemaForTns.entrySet()) {
            List<String> sameTnsSchemas = e2.getValue();
            if (sameTnsSchemas.size() > 1) {
                int i3 = i2;
                i2++;
                systemId = "file:x-jax-ws-include-" + i3;
                mdresolver.addSchema(createSameTnsPseudoSchema(e2.getKey(), sameTnsSchemas, systemId));
            } else {
                systemId = sameTnsSchemas.get(0);
            }
            oneSchemaForTns.put(e2.getKey(), systemId);
        }
        Source pseudoSchema = createMasterPseudoSchema(oneSchemaForTns);
        return new Source[]{pseudoSchema};
    }

    @Nullable
    private void addSchemaFragmentSource(Document doc, String systemId, Map<String, DOMSource> map) throws DOMException {
        Element e2 = doc.getDocumentElement();
        if (!$assertionsDisabled && !e2.getNamespaceURI().equals("http://schemas.xmlsoap.org/wsdl/")) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && !e2.getLocalName().equals("definitions")) {
            throw new AssertionError();
        }
        NodeList typesList = e2.getElementsByTagNameNS("http://schemas.xmlsoap.org/wsdl/", "types");
        for (int i2 = 0; i2 < typesList.getLength(); i2++) {
            NodeList schemaList = ((Element) typesList.item(i2)).getElementsByTagNameNS("http://www.w3.org/2001/XMLSchema", "schema");
            for (int j2 = 0; j2 < schemaList.getLength(); j2++) {
                Element elem = (Element) schemaList.item(j2);
                NamespaceSupport nss = new NamespaceSupport();
                buildNamespaceSupport(nss, elem);
                patchDOMFragment(nss, elem);
                String docId = systemId + "#schema" + j2;
                map.put(docId, new DOMSource(elem, docId));
            }
        }
    }

    private void buildNamespaceSupport(NamespaceSupport nss, Node node) {
        if (node == null || node.getNodeType() != 1) {
            return;
        }
        buildNamespaceSupport(nss, node.getParentNode());
        nss.pushContext();
        NamedNodeMap atts = node.getAttributes();
        for (int i2 = 0; i2 < atts.getLength(); i2++) {
            Attr a2 = (Attr) atts.item(i2);
            if ("xmlns".equals(a2.getPrefix())) {
                nss.declarePrefix(a2.getLocalName(), a2.getValue());
            } else if ("xmlns".equals(a2.getName())) {
                nss.declarePrefix("", a2.getValue());
            }
        }
    }

    @Nullable
    private void patchDOMFragment(NamespaceSupport nss, Element elem) throws DOMException {
        NamedNodeMap atts = elem.getAttributes();
        Enumeration en = nss.getPrefixes();
        while (en.hasMoreElements()) {
            String prefix = (String) en.nextElement2();
            for (int i2 = 0; i2 < atts.getLength(); i2++) {
                Attr a2 = (Attr) atts.item(i2);
                if (!"xmlns".equals(a2.getPrefix()) || !a2.getLocalName().equals(prefix)) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE, "Patching with xmlns:{0}={1}", new Object[]{prefix, nss.getURI(prefix)});
                    }
                    elem.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + prefix, nss.getURI(prefix));
                }
            }
        }
    }

    @Nullable
    private Source createSameTnsPseudoSchema(String tns, Collection<String> docs, String pseudoSystemId) {
        if (!$assertionsDisabled && docs.size() <= 1) {
            throw new AssertionError();
        }
        final StringBuilder sb = new StringBuilder("<xsd:schema xmlns:xsd='http://www.w3.org/2001/XMLSchema'");
        if (!tns.equals("")) {
            sb.append(" targetNamespace='").append(tns).append(PdfOps.SINGLE_QUOTE_TOKEN);
        }
        sb.append(">\n");
        for (String systemId : docs) {
            sb.append("<xsd:include schemaLocation='").append(systemId).append("'/>\n");
        }
        sb.append("</xsd:schema>\n");
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Pseudo Schema for the same tns={0}is {1}", new Object[]{tns, sb});
        }
        return new StreamSource(pseudoSystemId) { // from class: com.sun.xml.internal.ws.util.pipe.AbstractSchemaValidationTube.1
            @Override // javax.xml.transform.stream.StreamSource
            public Reader getReader() {
                return new StringReader(sb.toString());
            }
        };
    }

    private Source createMasterPseudoSchema(Map<String, String> docs) {
        final StringBuilder sb = new StringBuilder("<xsd:schema xmlns:xsd='http://www.w3.org/2001/XMLSchema' targetNamespace='urn:x-jax-ws-master'>\n");
        for (Map.Entry<String, String> e2 : docs.entrySet()) {
            String systemId = e2.getValue();
            String ns = e2.getKey();
            sb.append("<xsd:import schemaLocation='").append(systemId).append(PdfOps.SINGLE_QUOTE_TOKEN);
            if (!ns.equals("")) {
                sb.append(" namespace='").append(ns).append(PdfOps.SINGLE_QUOTE_TOKEN);
            }
            sb.append("/>\n");
        }
        sb.append("</xsd:schema>");
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Master Pseudo Schema = {0}", sb);
        }
        return new StreamSource("file:x-jax-ws-master-doc") { // from class: com.sun.xml.internal.ws.util.pipe.AbstractSchemaValidationTube.2
            @Override // javax.xml.transform.stream.StreamSource
            public Reader getReader() {
                return new StringReader(sb.toString());
            }
        };
    }

    protected void doProcess(Packet packet) throws SAXException {
        getValidator().reset();
        Class<? extends ValidationErrorHandler> handlerClass = this.feature.getErrorHandler();
        try {
            ValidationErrorHandler handler = handlerClass.newInstance();
            handler.setPacket(packet);
            getValidator().setErrorHandler(handler);
            Message msg = packet.getMessage().copy();
            Source source = msg.readPayloadAsSource();
            try {
                getValidator().validate(source);
            } catch (IOException e2) {
                throw new WebServiceException(e2);
            }
        } catch (Exception e3) {
            throw new WebServiceException(e3);
        }
    }

    private String getTargetNamespace(DOMSource src) {
        Element elem = (Element) src.getNode();
        return elem.getAttribute(WSDLConstants.ATTR_TNS);
    }
}
