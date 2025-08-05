package com.sun.xml.internal.ws.util.xml;

import com.sun.istack.internal.Nullable;
import com.sun.org.apache.xerces.internal.impl.Constants;
import com.sun.org.apache.xml.internal.resolver.Catalog;
import com.sun.org.apache.xml.internal.resolver.CatalogManager;
import com.sun.org.apache.xml.internal.resolver.tools.CatalogResolver;
import com.sun.xml.internal.ws.server.ServerRtException;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.ws.WebServiceException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/xml/XmlUtil.class */
public class XmlUtil {
    private static final String ACCESS_EXTERNAL_SCHEMA = "http://javax.xml.XMLConstants/property/accessExternalSchema";
    private static final String LEXICAL_HANDLER_PROPERTY = "http://xml.org/sax/properties/lexical-handler";
    private static final String DISALLOW_DOCTYPE_DECL = "http://apache.org/xml/features/disallow-doctype-decl";
    private static final String EXTERNAL_GE = "http://xml.org/sax/features/external-general-entities";
    private static final String EXTERNAL_PE = "http://xml.org/sax/features/external-parameter-entities";
    private static final String LOAD_EXTERNAL_DTD = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
    private static final String DISABLE_XML_SECURITY = "com.sun.xml.internal.ws.disableXmlSecurity";
    private static final Logger LOGGER = Logger.getLogger(XmlUtil.class.getName());
    private static boolean XML_SECURITY_DISABLED = ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: com.sun.xml.internal.ws.util.xml.XmlUtil.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        public Boolean run() {
            return Boolean.valueOf(Boolean.getBoolean(XmlUtil.DISABLE_XML_SECURITY));
        }
    })).booleanValue();
    static final ContextClassloaderLocal<TransformerFactory> transformerFactory = new ContextClassloaderLocal<TransformerFactory>() { // from class: com.sun.xml.internal.ws.util.xml.XmlUtil.2
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.sun.xml.internal.ws.util.xml.ContextClassloaderLocal
        public TransformerFactory initialValue() throws Exception {
            return TransformerFactory.newInstance();
        }
    };
    static final ContextClassloaderLocal<SAXParserFactory> saxParserFactory = new ContextClassloaderLocal<SAXParserFactory>() { // from class: com.sun.xml.internal.ws.util.xml.XmlUtil.3
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.sun.xml.internal.ws.util.xml.ContextClassloaderLocal
        public SAXParserFactory initialValue() throws Exception {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            return factory;
        }
    };
    public static final ErrorHandler DRACONIAN_ERROR_HANDLER = new ErrorHandler() { // from class: com.sun.xml.internal.ws.util.xml.XmlUtil.5
        @Override // org.xml.sax.ErrorHandler
        public void warning(SAXParseException exception) {
        }

        @Override // org.xml.sax.ErrorHandler
        public void error(SAXParseException exception) throws SAXException {
            throw exception;
        }

        @Override // org.xml.sax.ErrorHandler
        public void fatalError(SAXParseException exception) throws SAXException {
            throw exception;
        }
    };

    public static String getPrefix(String s2) {
        int i2 = s2.indexOf(58);
        if (i2 == -1) {
            return null;
        }
        return s2.substring(0, i2);
    }

    public static String getLocalPart(String s2) {
        int i2 = s2.indexOf(58);
        if (i2 == -1) {
            return s2;
        }
        return s2.substring(i2 + 1);
    }

    public static String getAttributeOrNull(Element e2, String name) {
        Attr a2 = e2.getAttributeNode(name);
        if (a2 == null) {
            return null;
        }
        return a2.getValue();
    }

    public static String getAttributeNSOrNull(Element e2, String name, String nsURI) throws DOMException {
        Attr a2 = e2.getAttributeNodeNS(nsURI, name);
        if (a2 == null) {
            return null;
        }
        return a2.getValue();
    }

    public static String getAttributeNSOrNull(Element e2, QName name) throws DOMException {
        Attr a2 = e2.getAttributeNodeNS(name.getNamespaceURI(), name.getLocalPart());
        if (a2 == null) {
            return null;
        }
        return a2.getValue();
    }

    public static Iterator getAllChildren(Element element) {
        return new NodeListIterator(element.getChildNodes());
    }

    public static Iterator getAllAttributes(Element element) {
        return new NamedNodeMapIterator(element.getAttributes());
    }

    public static List<String> parseTokenList(String tokenList) {
        List<String> result = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(tokenList, " ");
        while (tokenizer.hasMoreTokens()) {
            result.add(tokenizer.nextToken());
        }
        return result;
    }

    public static String getTextForNode(Node node) {
        String s2;
        StringBuilder sb = new StringBuilder();
        NodeList children = node.getChildNodes();
        if (children.getLength() == 0) {
            return null;
        }
        for (int i2 = 0; i2 < children.getLength(); i2++) {
            Node n2 = children.item(i2);
            if (n2 instanceof Text) {
                sb.append(n2.getNodeValue());
            } else {
                if (!(n2 instanceof EntityReference) || (s2 = getTextForNode(n2)) == null) {
                    return null;
                }
                sb.append(s2);
            }
        }
        return sb.toString();
    }

    public static InputStream getUTF8Stream(String s2) {
        try {
            ByteArrayBuffer bab = new ByteArrayBuffer();
            Writer w2 = new OutputStreamWriter(bab, "utf-8");
            w2.write(s2);
            w2.close();
            return bab.newInputStream();
        } catch (IOException e2) {
            throw new RuntimeException("should not happen");
        }
    }

    public static Transformer newTransformer() {
        try {
            return transformerFactory.get().newTransformer();
        } catch (TransformerConfigurationException e2) {
            throw new IllegalStateException("Unable to create a JAXP transformer");
        }
    }

    public static <T extends Result> T identityTransform(Source src, T result) throws TransformerException, ParserConfigurationException, SAXException, IOException, IllegalArgumentException {
        if (src instanceof StreamSource) {
            StreamSource ssrc = (StreamSource) src;
            TransformerHandler th = ((SAXTransformerFactory) transformerFactory.get()).newTransformerHandler();
            th.setResult(result);
            XMLReader reader = saxParserFactory.get().newSAXParser().getXMLReader();
            reader.setContentHandler(th);
            reader.setProperty("http://xml.org/sax/properties/lexical-handler", th);
            reader.parse(toInputSource(ssrc));
        } else {
            newTransformer().transform(src, result);
        }
        return result;
    }

    private static InputSource toInputSource(StreamSource src) {
        InputSource is = new InputSource();
        is.setByteStream(src.getInputStream());
        is.setCharacterStream(src.getReader());
        is.setPublicId(src.getPublicId());
        is.setSystemId(src.getSystemId());
        return is;
    }

    public static EntityResolver createEntityResolver(@Nullable URL catalogUrl) {
        CatalogManager manager = new CatalogManager();
        manager.setIgnoreMissingProperties(true);
        manager.setUseStaticCatalog(false);
        Catalog catalog = manager.getCatalog();
        if (catalogUrl != null) {
            try {
                catalog.parseCatalog(catalogUrl);
            } catch (IOException e2) {
                throw new ServerRtException("server.rt.err", e2);
            }
        }
        return workaroundCatalogResolver(catalog);
    }

    public static EntityResolver createDefaultCatalogResolver() {
        Enumeration<URL> catalogEnum;
        CatalogManager manager = new CatalogManager();
        manager.setIgnoreMissingProperties(true);
        manager.setUseStaticCatalog(false);
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Catalog catalog = manager.getCatalog();
        try {
            if (cl == null) {
                catalogEnum = ClassLoader.getSystemResources("META-INF/jax-ws-catalog.xml");
            } else {
                catalogEnum = cl.getResources("META-INF/jax-ws-catalog.xml");
            }
            while (catalogEnum.hasMoreElements()) {
                URL url = catalogEnum.nextElement2();
                catalog.parseCatalog(url);
            }
            return workaroundCatalogResolver(catalog);
        } catch (IOException e2) {
            throw new WebServiceException(e2);
        }
    }

    private static CatalogResolver workaroundCatalogResolver(final Catalog catalog) {
        CatalogManager manager = new CatalogManager() { // from class: com.sun.xml.internal.ws.util.xml.XmlUtil.4
            @Override // com.sun.org.apache.xml.internal.resolver.CatalogManager
            public Catalog getCatalog() {
                return catalog;
            }
        };
        manager.setIgnoreMissingProperties(true);
        manager.setUseStaticCatalog(false);
        return new CatalogResolver(manager);
    }

    public static DocumentBuilderFactory newDocumentBuilderFactory() {
        return newDocumentBuilderFactory(false);
    }

    public static DocumentBuilderFactory newDocumentBuilderFactory(boolean disableSecurity) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        String featureToSet = "http://javax.xml.XMLConstants/feature/secure-processing";
        try {
            boolean securityOn = !isXMLSecurityDisabled(disableSecurity);
            factory.setFeature(featureToSet, securityOn);
            factory.setNamespaceAware(true);
            if (securityOn) {
                factory.setExpandEntityReferences(false);
                factory.setFeature(DISALLOW_DOCTYPE_DECL, true);
                factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
                factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
                featureToSet = LOAD_EXTERNAL_DTD;
                factory.setFeature(featureToSet, false);
            }
        } catch (ParserConfigurationException e2) {
            LOGGER.log(Level.WARNING, "Factory [{0}] doesn't support " + featureToSet + " feature!", new Object[]{factory.getClass().getName()});
        }
        return factory;
    }

    public static TransformerFactory newTransformerFactory(boolean secureXmlProcessingEnabled) throws TransformerFactoryConfigurationError {
        TransformerFactory factory = TransformerFactory.newInstance();
        try {
            factory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", isXMLSecurityDisabled(secureXmlProcessingEnabled));
        } catch (TransformerConfigurationException e2) {
            LOGGER.log(Level.WARNING, "Factory [{0}] doesn't support secure xml processing!", new Object[]{factory.getClass().getName()});
        }
        return factory;
    }

    public static TransformerFactory newTransformerFactory() {
        return newTransformerFactory(true);
    }

    public static SAXParserFactory newSAXParserFactory(boolean disableSecurity) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        String featureToSet = "http://javax.xml.XMLConstants/feature/secure-processing";
        try {
            boolean securityOn = !isXMLSecurityDisabled(disableSecurity);
            factory.setFeature(featureToSet, securityOn);
            factory.setNamespaceAware(true);
            if (securityOn) {
                factory.setFeature(DISALLOW_DOCTYPE_DECL, true);
                factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
                factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
                featureToSet = LOAD_EXTERNAL_DTD;
                factory.setFeature(featureToSet, false);
            }
        } catch (ParserConfigurationException | SAXNotRecognizedException | SAXNotSupportedException e2) {
            LOGGER.log(Level.WARNING, "Factory [{0}] doesn't support " + featureToSet + " feature!", new Object[]{factory.getClass().getName()});
        }
        return factory;
    }

    public static XPathFactory newXPathFactory(boolean secureXmlProcessingEnabled) {
        XPathFactory factory = XPathFactory.newInstance();
        try {
            factory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", isXMLSecurityDisabled(secureXmlProcessingEnabled));
        } catch (XPathFactoryConfigurationException e2) {
            LOGGER.log(Level.WARNING, "Factory [{0}] doesn't support secure xml processing!", new Object[]{factory.getClass().getName()});
        }
        return factory;
    }

    public static XMLInputFactory newXMLInputFactory(boolean secureXmlProcessingEnabled) throws IllegalArgumentException, FactoryConfigurationError {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        if (isXMLSecurityDisabled(secureXmlProcessingEnabled)) {
            factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        }
        return factory;
    }

    private static boolean isXMLSecurityDisabled(boolean runtimeDisabled) {
        return XML_SECURITY_DISABLED || runtimeDisabled;
    }

    public static SchemaFactory allowExternalAccess(SchemaFactory sf, String value, boolean disableSecureProcessing) {
        if (isXMLSecurityDisabled(disableSecureProcessing)) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Xml Security disabled, no JAXP xsd external access configuration necessary.");
            }
            return sf;
        }
        if (System.getProperty(Constants.SP_ACCESS_EXTERNAL_SCHEMA) != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Detected explicitly JAXP configuration, no JAXP xsd external access configuration necessary.");
            }
            return sf;
        }
        try {
            sf.setProperty("http://javax.xml.XMLConstants/property/accessExternalSchema", value);
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Property \"{0}\" is supported and has been successfully set by used JAXP implementation.", new Object[]{"http://javax.xml.XMLConstants/property/accessExternalSchema"});
            }
        } catch (SAXException e2) {
            if (LOGGER.isLoggable(Level.CONFIG)) {
                LOGGER.log(Level.CONFIG, "Property \"{0}\" is not supported by used JAXP implementation.", new Object[]{"http://javax.xml.XMLConstants/property/accessExternalSchema"});
            }
        }
        return sf;
    }
}
