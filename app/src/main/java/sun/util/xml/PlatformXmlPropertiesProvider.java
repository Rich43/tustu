package sun.util.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import sun.util.spi.XmlPropertiesProvider;

/* loaded from: rt.jar:sun/util/xml/PlatformXmlPropertiesProvider.class */
public class PlatformXmlPropertiesProvider extends XmlPropertiesProvider {
    private static final String PROPS_DTD_URI = "http://java.sun.com/dtd/properties.dtd";
    private static final String PROPS_DTD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!-- DTD for properties --><!ELEMENT properties ( comment?, entry* ) ><!ATTLIST properties version CDATA #FIXED \"1.0\"><!ELEMENT comment (#PCDATA) ><!ELEMENT entry (#PCDATA) ><!ATTLIST entry  key CDATA #REQUIRED>";
    private static final String EXTERNAL_XML_VERSION = "1.0";
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !PlatformXmlPropertiesProvider.class.desiredAssertionStatus();
    }

    @Override // sun.util.spi.XmlPropertiesProvider
    public void load(Properties properties, InputStream inputStream) throws IOException {
        try {
            Element documentElement = getLoadingDoc(inputStream).getDocumentElement();
            String attribute = documentElement.getAttribute("version");
            if (attribute.compareTo("1.0") > 0) {
                throw new InvalidPropertiesFormatException("Exported Properties file format version " + attribute + " is not supported. This java installation can read versions 1.0 or older. You may need to install a newer version of JDK.");
            }
            importProperties(properties, documentElement);
        } catch (SAXException e2) {
            throw new InvalidPropertiesFormatException(e2);
        }
    }

    static Document getLoadingDoc(InputStream inputStream) throws SAXException, IOException {
        DocumentBuilderFactory documentBuilderFactoryNewInstance = DocumentBuilderFactory.newInstance();
        documentBuilderFactoryNewInstance.setIgnoringElementContentWhitespace(true);
        documentBuilderFactoryNewInstance.setValidating(true);
        documentBuilderFactoryNewInstance.setCoalescing(true);
        documentBuilderFactoryNewInstance.setIgnoringComments(true);
        try {
            DocumentBuilder documentBuilderNewDocumentBuilder = documentBuilderFactoryNewInstance.newDocumentBuilder();
            documentBuilderNewDocumentBuilder.setEntityResolver(new Resolver());
            documentBuilderNewDocumentBuilder.setErrorHandler(new EH());
            return documentBuilderNewDocumentBuilder.parse(new InputSource(inputStream));
        } catch (ParserConfigurationException e2) {
            throw new Error(e2);
        }
    }

    static void importProperties(Properties properties, Element element) {
        NodeList childNodes = element.getChildNodes();
        int length = childNodes.getLength();
        for (int i2 = (length <= 0 || !childNodes.item(0).getNodeName().equals("comment")) ? 0 : 1; i2 < length; i2++) {
            Element element2 = (Element) childNodes.item(i2);
            if (element2.hasAttribute("key")) {
                Node firstChild = element2.getFirstChild();
                properties.setProperty(element2.getAttribute("key"), firstChild == null ? "" : firstChild.getNodeValue());
            }
        }
    }

    @Override // sun.util.spi.XmlPropertiesProvider
    public void store(Properties properties, OutputStream outputStream, String str, String str2) throws IOException, IllegalArgumentException {
        try {
            Charset.forName(str2);
            DocumentBuilder documentBuilderNewDocumentBuilder = null;
            try {
                documentBuilderNewDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            } catch (ParserConfigurationException e2) {
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
            }
            Document documentNewDocument = documentBuilderNewDocumentBuilder.newDocument();
            Element element = (Element) documentNewDocument.appendChild(documentNewDocument.createElement("properties"));
            if (str != null) {
                ((Element) element.appendChild(documentNewDocument.createElement("comment"))).appendChild(documentNewDocument.createTextNode(str));
            }
            synchronized (properties) {
                for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                    Object key = entry.getKey();
                    Object value = entry.getValue();
                    if ((key instanceof String) && (value instanceof String)) {
                        Element element2 = (Element) element.appendChild(documentNewDocument.createElement("entry"));
                        element2.setAttribute("key", (String) key);
                        element2.appendChild(documentNewDocument.createTextNode((String) value));
                    }
                }
            }
            emitDocument(documentNewDocument, outputStream, str2);
        } catch (IllegalCharsetNameException | UnsupportedCharsetException e3) {
            throw new UnsupportedEncodingException(str2);
        }
    }

    static void emitDocument(Document document, OutputStream outputStream, String str) throws IOException, IllegalArgumentException {
        Transformer transformerNewTransformer = null;
        try {
            transformerNewTransformer = TransformerFactory.newInstance().newTransformer();
            transformerNewTransformer.setOutputProperty("doctype-system", PROPS_DTD_URI);
            transformerNewTransformer.setOutputProperty("indent", "yes");
            transformerNewTransformer.setOutputProperty("method", "xml");
            transformerNewTransformer.setOutputProperty("encoding", str);
        } catch (TransformerConfigurationException e2) {
            if (!$assertionsDisabled) {
                throw new AssertionError();
            }
        }
        try {
            transformerNewTransformer.transform(new DOMSource(document), new StreamResult(outputStream));
        } catch (TransformerException e3) {
            throw new IOException(e3);
        }
    }

    /* loaded from: rt.jar:sun/util/xml/PlatformXmlPropertiesProvider$Resolver.class */
    private static class Resolver implements EntityResolver {
        private Resolver() {
        }

        @Override // org.xml.sax.EntityResolver
        public InputSource resolveEntity(String str, String str2) throws SAXException {
            if (str2.equals(PlatformXmlPropertiesProvider.PROPS_DTD_URI)) {
                InputSource inputSource = new InputSource(new StringReader(PlatformXmlPropertiesProvider.PROPS_DTD));
                inputSource.setSystemId(PlatformXmlPropertiesProvider.PROPS_DTD_URI);
                return inputSource;
            }
            throw new SAXException("Invalid system identifier: " + str2);
        }
    }

    /* loaded from: rt.jar:sun/util/xml/PlatformXmlPropertiesProvider$EH.class */
    private static class EH implements ErrorHandler {
        private EH() {
        }

        @Override // org.xml.sax.ErrorHandler
        public void error(SAXParseException sAXParseException) throws SAXException {
            throw sAXParseException;
        }

        @Override // org.xml.sax.ErrorHandler
        public void fatalError(SAXParseException sAXParseException) throws SAXException {
            throw sAXParseException;
        }

        @Override // org.xml.sax.ErrorHandler
        public void warning(SAXParseException sAXParseException) throws SAXException {
            throw sAXParseException;
        }
    }
}
