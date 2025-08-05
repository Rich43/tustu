package com.sun.xml.internal.ws.transport.http;

import com.oracle.webservices.internal.api.databinding.DatabindingModeFeature;
import com.oracle.webservices.internal.api.databinding.ExternalMetadataFeature;
import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.BindingID;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.databinding.MetadataReader;
import com.sun.xml.internal.ws.api.server.Container;
import com.sun.xml.internal.ws.api.server.SDDocumentSource;
import com.sun.xml.internal.ws.api.server.WSEndpoint;
import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
import com.sun.xml.internal.ws.binding.WebServiceFeatureList;
import com.sun.xml.internal.ws.handler.HandlerChainsModel;
import com.sun.xml.internal.ws.resources.ServerMessages;
import com.sun.xml.internal.ws.resources.WsservletMessages;
import com.sun.xml.internal.ws.server.EndpointFactory;
import com.sun.xml.internal.ws.server.ServerRtException;
import com.sun.xml.internal.ws.streaming.Attributes;
import com.sun.xml.internal.ws.streaming.TidyXMLStreamReader;
import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil;
import com.sun.xml.internal.ws.util.HandlerAnnotationInfo;
import com.sun.xml.internal.ws.util.exception.LocatableWebServiceException;
import com.sun.xml.internal.ws.util.xml.XmlUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.http.HTTPBinding;
import javax.xml.ws.soap.MTOMFeature;
import javax.xml.ws.soap.SOAPBinding;
import org.xml.sax.EntityResolver;

/* loaded from: rt.jar:com/sun/xml/internal/ws/transport/http/DeploymentDescriptorParser.class */
public class DeploymentDescriptorParser<A> {
    public static final String NS_RUNTIME = "http://java.sun.com/xml/ns/jax-ws/ri/runtime";
    public static final String JAXWS_WSDL_DD_DIR = "WEB-INF/wsdl";
    public static final QName QNAME_ENDPOINTS;
    public static final QName QNAME_ENDPOINT;
    public static final QName QNAME_EXT_METADA;
    public static final String ATTR_FILE = "file";
    public static final String ATTR_RESOURCE = "resource";
    public static final String ATTR_VERSION = "version";
    public static final String ATTR_NAME = "name";
    public static final String ATTR_IMPLEMENTATION = "implementation";
    public static final String ATTR_WSDL = "wsdl";
    public static final String ATTR_SERVICE = "service";
    public static final String ATTR_PORT = "port";
    public static final String ATTR_URL_PATTERN = "url-pattern";
    public static final String ATTR_ENABLE_MTOM = "enable-mtom";
    public static final String ATTR_MTOM_THRESHOLD_VALUE = "mtom-threshold-value";
    public static final String ATTR_BINDING = "binding";
    public static final String ATTR_DATABINDING = "databinding";
    public static final List<String> ATTRVALUE_SUPPORTED_VERSIONS;
    private static final Logger logger;
    private final Container container;
    private final ClassLoader classLoader;
    private final ResourceLoader loader;
    private final AdapterFactory<A> adapterFactory;
    private final Set<String> names = new HashSet();
    private final Map<String, SDDocumentSource> docs = new HashMap();
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: rt.jar:com/sun/xml/internal/ws/transport/http/DeploymentDescriptorParser$AdapterFactory.class */
    public interface AdapterFactory<A> {
        A createAdapter(String str, String str2, WSEndpoint<?> wSEndpoint);
    }

    static {
        $assertionsDisabled = !DeploymentDescriptorParser.class.desiredAssertionStatus();
        QNAME_ENDPOINTS = new QName(NS_RUNTIME, "endpoints");
        QNAME_ENDPOINT = new QName(NS_RUNTIME, "endpoint");
        QNAME_EXT_METADA = new QName(NS_RUNTIME, "external-metadata");
        ATTRVALUE_SUPPORTED_VERSIONS = Arrays.asList("2.0", "2.1");
        logger = Logger.getLogger("com.sun.xml.internal.ws.server.http");
    }

    public DeploymentDescriptorParser(ClassLoader cl, ResourceLoader loader, Container container, AdapterFactory<A> adapterFactory) throws MalformedURLException {
        this.classLoader = cl;
        this.loader = loader;
        this.container = container;
        this.adapterFactory = adapterFactory;
        collectDocs("/WEB-INF/wsdl/");
        logger.log(Level.FINE, "war metadata={0}", this.docs);
    }

    @NotNull
    public List<A> parse(String systemId, InputStream is) {
        XMLStreamReader reader = null;
        try {
            reader = new TidyXMLStreamReader(XMLStreamReaderFactory.create(systemId, is, true), is);
            XMLStreamReaderUtil.nextElementContent(reader);
            List<A> adapters = parseAdapters(reader);
            if (reader != null) {
                try {
                    reader.close();
                } catch (XMLStreamException e2) {
                    throw new ServerRtException("runtime.parser.xmlReader", e2);
                }
            }
            try {
                is.close();
            } catch (IOException e3) {
            }
            return adapters;
        } catch (Throwable th) {
            if (reader != null) {
                try {
                    reader.close();
                } catch (XMLStreamException e4) {
                    throw new ServerRtException("runtime.parser.xmlReader", e4);
                }
            }
            try {
                is.close();
            } catch (IOException e5) {
            }
            throw th;
        }
    }

    @NotNull
    public List<A> parse(File f2) throws IOException {
        FileInputStream in = new FileInputStream(f2);
        try {
            List<A> list = parse(f2.getPath(), in);
            in.close();
            return list;
        } catch (Throwable th) {
            in.close();
            throw th;
        }
    }

    private void collectDocs(String dirPath) throws MalformedURLException {
        Set<String> paths = this.loader.getResourcePaths(dirPath);
        if (paths != null) {
            for (String path : paths) {
                if (path.endsWith("/")) {
                    if (!path.endsWith("/CVS/") && !path.endsWith("/.svn/")) {
                        collectDocs(path);
                    }
                } else {
                    URL res = this.loader.getResource(path);
                    this.docs.put(res.toString(), SDDocumentSource.create(res));
                }
            }
        }
    }

    private List<A> parseAdapters(XMLStreamReader reader) throws WebServiceException {
        if (!reader.getName().equals(QNAME_ENDPOINTS)) {
            failWithFullName("runtime.parser.invalidElement", reader);
        }
        List<A> adapters = new ArrayList<>();
        String version = getMandatoryNonEmptyAttribute(reader, XMLStreamReaderUtil.getAttributes(reader), "version");
        if (!ATTRVALUE_SUPPORTED_VERSIONS.contains(version)) {
            failWithLocalName("runtime.parser.invalidVersionNumber", reader, version);
        }
        while (XMLStreamReaderUtil.nextElementContent(reader) != 2) {
            if (reader.getName().equals(QNAME_ENDPOINT)) {
                Attributes attrs = XMLStreamReaderUtil.getAttributes(reader);
                String name = getMandatoryNonEmptyAttribute(reader, attrs, "name");
                if (!this.names.add(name)) {
                    logger.warning(WsservletMessages.SERVLET_WARNING_DUPLICATE_ENDPOINT_NAME());
                }
                String implementationName = getMandatoryNonEmptyAttribute(reader, attrs, ATTR_IMPLEMENTATION);
                Class<?> implementorClass = getImplementorClass(implementationName, reader);
                MetadataReader metadataReader = null;
                ExternalMetadataFeature externalMetadataFeature = null;
                XMLStreamReaderUtil.nextElementContent(reader);
                if (reader.getEventType() != 2) {
                    externalMetadataFeature = configureExternalMetadataReader(reader);
                    if (externalMetadataFeature != null) {
                        metadataReader = externalMetadataFeature.getMetadataReader(implementorClass.getClassLoader(), false);
                    }
                }
                QName serviceName = getQNameAttribute(attrs, ATTR_SERVICE);
                if (serviceName == null) {
                    serviceName = EndpointFactory.getDefaultServiceName(implementorClass, metadataReader);
                }
                QName portName = getQNameAttribute(attrs, ATTR_PORT);
                if (portName == null) {
                    portName = EndpointFactory.getDefaultPortName(serviceName, implementorClass, metadataReader);
                }
                String enable_mtom = getAttribute(attrs, ATTR_ENABLE_MTOM);
                String mtomThreshold = getAttribute(attrs, ATTR_MTOM_THRESHOLD_VALUE);
                String dbMode = getAttribute(attrs, ATTR_DATABINDING);
                String bindingId = getAttribute(attrs, ATTR_BINDING);
                if (bindingId != null) {
                    bindingId = getBindingIdForToken(bindingId);
                }
                WSBinding binding = createBinding(bindingId, implementorClass, enable_mtom, mtomThreshold, dbMode);
                if (externalMetadataFeature != null) {
                    binding.getFeatures().mergeFeatures(new WebServiceFeature[]{externalMetadataFeature}, true);
                }
                String urlPattern = getMandatoryNonEmptyAttribute(reader, attrs, ATTR_URL_PATTERN);
                boolean handlersSetInDD = setHandlersAndRoles(binding, reader, serviceName, portName);
                EndpointFactory.verifyImplementorClass(implementorClass, metadataReader);
                SDDocumentSource primaryWSDL = getPrimaryWSDL(reader, attrs, implementorClass, metadataReader);
                WSEndpoint<?> endpoint = WSEndpoint.create(implementorClass, !handlersSetInDD, null, serviceName, portName, this.container, binding, primaryWSDL, this.docs.values(), createEntityResolver(), false);
                adapters.add(this.adapterFactory.createAdapter(name, urlPattern, endpoint));
            } else {
                failWithLocalName("runtime.parser.invalidElement", reader);
            }
        }
        return adapters;
    }

    private static WSBinding createBinding(String ddBindingId, Class implClass, String mtomEnabled, String mtomThreshold, String dataBindingMode) throws WebServiceException {
        BindingID bindingID;
        WebServiceFeatureList features;
        MTOMFeature mtomfeature = null;
        if (mtomEnabled != null) {
            if (mtomThreshold != null) {
                mtomfeature = new MTOMFeature(Boolean.valueOf(mtomEnabled).booleanValue(), Integer.valueOf(mtomThreshold).intValue());
            } else {
                mtomfeature = new MTOMFeature(Boolean.valueOf(mtomEnabled).booleanValue());
            }
        }
        if (ddBindingId != null) {
            bindingID = BindingID.parse(ddBindingId);
            features = bindingID.createBuiltinFeatureList();
            if (checkMtomConflict((MTOMFeature) features.get(MTOMFeature.class), mtomfeature)) {
                throw new ServerRtException(ServerMessages.DD_MTOM_CONFLICT(ddBindingId, mtomEnabled), new Object[0]);
            }
        } else {
            bindingID = BindingID.parse((Class<?>) implClass);
            features = new WebServiceFeatureList();
            if (mtomfeature != null) {
                features.add(mtomfeature);
            }
            features.addAll(bindingID.createBuiltinFeatureList());
        }
        if (dataBindingMode != null) {
            features.add(new DatabindingModeFeature(dataBindingMode));
        }
        return bindingID.createBinding(features.toArray());
    }

    private static boolean checkMtomConflict(MTOMFeature lhs, MTOMFeature rhs) {
        if (lhs == null || rhs == null) {
            return false;
        }
        return lhs.isEnabled() ^ rhs.isEnabled();
    }

    @NotNull
    public static String getBindingIdForToken(@NotNull String lexical) {
        if (lexical.equals(HandlerChainsModel.PROTOCOL_SOAP11_TOKEN)) {
            return SOAPBinding.SOAP11HTTP_BINDING;
        }
        if (lexical.equals("##SOAP11_HTTP_MTOM")) {
            return SOAPBinding.SOAP11HTTP_MTOM_BINDING;
        }
        if (lexical.equals(HandlerChainsModel.PROTOCOL_SOAP12_TOKEN)) {
            return "http://www.w3.org/2003/05/soap/bindings/HTTP/";
        }
        if (lexical.equals("##SOAP12_HTTP_MTOM")) {
            return SOAPBinding.SOAP12HTTP_MTOM_BINDING;
        }
        if (lexical.equals(HandlerChainsModel.PROTOCOL_XML_TOKEN)) {
            return HTTPBinding.HTTP_BINDING;
        }
        return lexical;
    }

    private SDDocumentSource getPrimaryWSDL(XMLStreamReader xsr, Attributes attrs, Class<?> implementorClass, MetadataReader metadataReader) {
        String wsdlFile = getAttribute(attrs, "wsdl");
        if (wsdlFile == null) {
            wsdlFile = EndpointFactory.getWsdlLocation(implementorClass, metadataReader);
        }
        if (wsdlFile != null) {
            if (!wsdlFile.startsWith(JAXWS_WSDL_DD_DIR)) {
                logger.log(Level.WARNING, "Ignoring wrong wsdl={0}. It should start with {1}. Going to generate and publish a new WSDL.", new Object[]{wsdlFile, JAXWS_WSDL_DD_DIR});
                return null;
            }
            try {
                URL wsdl = this.loader.getResource('/' + wsdlFile);
                if (wsdl == null) {
                    throw new LocatableWebServiceException(ServerMessages.RUNTIME_PARSER_WSDL_NOT_FOUND(wsdlFile), xsr);
                }
                SDDocumentSource docInfo = this.docs.get(wsdl.toExternalForm());
                if ($assertionsDisabled || docInfo != null) {
                    return docInfo;
                }
                throw new AssertionError();
            } catch (MalformedURLException e2) {
                throw new LocatableWebServiceException(ServerMessages.RUNTIME_PARSER_WSDL_NOT_FOUND(wsdlFile), e2, xsr);
            }
        }
        return null;
    }

    private EntityResolver createEntityResolver() {
        try {
            return XmlUtil.createEntityResolver(this.loader.getCatalogFile());
        } catch (MalformedURLException e2) {
            throw new WebServiceException(e2);
        }
    }

    protected String getAttribute(Attributes attrs, String name) {
        String value = attrs.getValue(name);
        if (value != null) {
            value = value.trim();
        }
        return value;
    }

    protected QName getQNameAttribute(Attributes attrs, String name) {
        String value = getAttribute(attrs, name);
        if (value == null || value.equals("")) {
            return null;
        }
        return QName.valueOf(value);
    }

    protected String getNonEmptyAttribute(XMLStreamReader reader, Attributes attrs, String name) {
        String value = getAttribute(attrs, name);
        if (value != null && value.equals("")) {
            failWithLocalName("runtime.parser.invalidAttributeValue", reader, name);
        }
        return value;
    }

    protected String getMandatoryAttribute(XMLStreamReader reader, Attributes attrs, String name) {
        String value = getAttribute(attrs, name);
        if (value == null) {
            failWithLocalName("runtime.parser.missing.attribute", reader, name);
        }
        return value;
    }

    protected String getMandatoryNonEmptyAttribute(XMLStreamReader reader, Attributes attributes, String name) {
        String value = getAttribute(attributes, name);
        if (value == null) {
            failWithLocalName("runtime.parser.missing.attribute", reader, name);
        } else if (value.equals("")) {
            failWithLocalName("runtime.parser.invalidAttributeValue", reader, name);
        }
        return value;
    }

    protected boolean setHandlersAndRoles(WSBinding binding, XMLStreamReader reader, QName serviceName, QName portName) {
        if (reader.getEventType() == 2 || !reader.getName().equals(HandlerChainsModel.QNAME_HANDLER_CHAINS)) {
            return false;
        }
        HandlerAnnotationInfo handlerInfo = HandlerChainsModel.parseHandlerFile(reader, this.classLoader, serviceName, portName, binding);
        binding.setHandlerChain(handlerInfo.getHandlers());
        if (binding instanceof SOAPBinding) {
            ((SOAPBinding) binding).setRoles(handlerInfo.getRoles());
        }
        XMLStreamReaderUtil.nextContent(reader);
        return true;
    }

    protected ExternalMetadataFeature configureExternalMetadataReader(XMLStreamReader reader) {
        ExternalMetadataFeature.Builder featureBuilder = null;
        while (QNAME_EXT_METADA.equals(reader.getName())) {
            if (reader.getEventType() == 1) {
                Attributes attrs = XMLStreamReaderUtil.getAttributes(reader);
                String file = getAttribute(attrs, ATTR_FILE);
                if (file != null) {
                    if (featureBuilder == null) {
                        featureBuilder = ExternalMetadataFeature.builder();
                    }
                    featureBuilder.addFiles(new File(file));
                }
                String res = getAttribute(attrs, ATTR_RESOURCE);
                if (res != null) {
                    if (featureBuilder == null) {
                        featureBuilder = ExternalMetadataFeature.builder();
                    }
                    featureBuilder.addResources(res);
                }
            }
            XMLStreamReaderUtil.nextElementContent(reader);
        }
        return buildFeature(featureBuilder);
    }

    private ExternalMetadataFeature buildFeature(ExternalMetadataFeature.Builder builder) {
        if (builder != null) {
            return builder.build();
        }
        return null;
    }

    protected static void fail(String key, XMLStreamReader reader) {
        logger.log(Level.SEVERE, "{0}{1}", new Object[]{key, Integer.valueOf(reader.getLocation().getLineNumber())});
        throw new ServerRtException(key, Integer.toString(reader.getLocation().getLineNumber()));
    }

    protected static void failWithFullName(String key, XMLStreamReader reader) {
        throw new ServerRtException(key, Integer.valueOf(reader.getLocation().getLineNumber()), reader.getName());
    }

    protected static void failWithLocalName(String key, XMLStreamReader reader) {
        throw new ServerRtException(key, Integer.valueOf(reader.getLocation().getLineNumber()), reader.getLocalName());
    }

    protected static void failWithLocalName(String key, XMLStreamReader reader, String arg) {
        throw new ServerRtException(key, Integer.valueOf(reader.getLocation().getLineNumber()), reader.getLocalName(), arg);
    }

    protected Class loadClass(String name) {
        try {
            return Class.forName(name, true, this.classLoader);
        } catch (ClassNotFoundException e2) {
            logger.log(Level.SEVERE, e2.getMessage(), (Throwable) e2);
            throw new ServerRtException("runtime.parser.classNotFound", name);
        }
    }

    private Class getImplementorClass(String name, XMLStreamReader xsr) {
        try {
            return Class.forName(name, true, this.classLoader);
        } catch (ClassNotFoundException e2) {
            logger.log(Level.SEVERE, e2.getMessage(), (Throwable) e2);
            throw new LocatableWebServiceException(ServerMessages.RUNTIME_PARSER_CLASS_NOT_FOUND(name), e2, xsr);
        }
    }
}
