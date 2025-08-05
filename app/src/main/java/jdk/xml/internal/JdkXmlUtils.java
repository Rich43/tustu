package jdk.xml.internal;

import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl;
import com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/* loaded from: rt.jar:jdk/xml/internal/JdkXmlUtils.class */
public class JdkXmlUtils {
    private static final String DOM_FACTORY_ID = "javax.xml.parsers.DocumentBuilderFactory";
    private static final String SAX_FACTORY_ID = "javax.xml.parsers.SAXParserFactory";
    private static final String SAX_DRIVER = "org.xml.sax.driver";
    public static final String NAMESPACES_FEATURE = "http://xml.org/sax/features/namespaces";
    public static final String NAMESPACE_PREFIXES_FEATURE = "http://xml.org/sax/features/namespace-prefixes";
    public static final String FEATURE_TRUE = "true";
    public static final String FEATURE_FALSE = "false";
    public static final String OVERRIDE_PARSER = "jdk.xml.overrideDefaultParser";
    public static final boolean OVERRIDE_PARSER_DEFAULT = ((Boolean) SecuritySupport.getJAXPSystemProperty(Boolean.class, OVERRIDE_PARSER, "false")).booleanValue();
    private static final SAXParserFactory defaultSAXFactory = getSAXFactory(false);

    public static int getValue(Object value, int defValue) {
        if (value == null) {
            return defValue;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof String) {
            return Integer.parseInt(String.valueOf(value));
        }
        throw new IllegalArgumentException("Unexpected class: " + ((Object) value.getClass()));
    }

    public static void setXMLReaderPropertyIfSupport(XMLReader reader, String property, Object value, boolean warn) {
        try {
            reader.setProperty(property, value);
        } catch (SAXNotRecognizedException | SAXNotSupportedException e2) {
            if (warn) {
                XMLSecurityManager.printWarning(reader.getClass().getName(), property, e2);
            }
        }
    }

    public static XMLReader getXMLReader(boolean overrideDefaultParser, boolean secureProcessing) {
        XMLReader reader = null;
        String spSAXDriver = SecuritySupport.getSystemProperty(SAX_DRIVER);
        if (spSAXDriver != null) {
            reader = getXMLReaderWXMLReaderFactory();
        } else if (overrideDefaultParser) {
            reader = getXMLReaderWSAXFactory(overrideDefaultParser);
        }
        if (reader != null) {
            if (secureProcessing) {
                try {
                    reader.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", secureProcessing);
                } catch (SAXException e2) {
                    XMLSecurityManager.printWarning(reader.getClass().getName(), "http://javax.xml.XMLConstants/feature/secure-processing", e2);
                }
            }
            try {
                reader.setFeature("http://xml.org/sax/features/namespaces", true);
                reader.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
            } catch (SAXException e3) {
            }
            return reader;
        }
        SAXParserFactory saxFactory = defaultSAXFactory;
        try {
            reader = saxFactory.newSAXParser().getXMLReader();
        } catch (ParserConfigurationException | SAXException e4) {
        }
        return reader;
    }

    public static Document getDOMDocument() {
        try {
            DocumentBuilderFactory dbf = getDOMFactory(false);
            return dbf.newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e2) {
            return null;
        }
    }

    public static DocumentBuilderFactory getDOMFactory(boolean overrideDefaultParser) {
        boolean override = overrideDefaultParser;
        String spDOMFactory = SecuritySupport.getJAXPSystemProperty(DOM_FACTORY_ID);
        if (spDOMFactory != null && System.getSecurityManager() == null) {
            override = true;
        }
        DocumentBuilderFactory dbf = !override ? new DocumentBuilderFactoryImpl() : DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setValidating(false);
        return dbf;
    }

    public static SAXParserFactory getSAXFactory(boolean overrideDefaultParser) {
        boolean override = overrideDefaultParser;
        String spSAXFactory = SecuritySupport.getJAXPSystemProperty(SAX_FACTORY_ID);
        if (spSAXFactory != null && System.getSecurityManager() == null) {
            override = true;
        }
        SAXParserFactory factory = !override ? new SAXParserFactoryImpl() : SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        return factory;
    }

    public static SAXTransformerFactory getSAXTransformFactory(boolean overrideDefaultParser) {
        SAXTransformerFactory tf = overrideDefaultParser ? (SAXTransformerFactory) SAXTransformerFactory.newInstance() : new TransformerFactoryImpl();
        try {
            tf.setFeature(OVERRIDE_PARSER, overrideDefaultParser);
        } catch (TransformerConfigurationException e2) {
        }
        return tf;
    }

    public static String getDTDExternalDecl(String publicId, String systemId) {
        StringBuilder sb = new StringBuilder();
        if (null != publicId) {
            sb.append(" PUBLIC ");
            sb.append(quoteString(publicId));
        }
        if (null != systemId) {
            if (null == publicId) {
                sb.append(" SYSTEM ");
            } else {
                sb.append(" ");
            }
            sb.append(quoteString(systemId));
        }
        return sb.toString();
    }

    private static String quoteString(String s2) {
        char c2 = s2.indexOf(34) > -1 ? '\'' : '\"';
        return c2 + s2 + c2;
    }

    private static XMLReader getXMLReaderWSAXFactory(boolean overrideDefaultParser) {
        SAXParserFactory saxFactory = getSAXFactory(overrideDefaultParser);
        try {
            return saxFactory.newSAXParser().getXMLReader();
        } catch (ParserConfigurationException | SAXException e2) {
            return getXMLReaderWXMLReaderFactory();
        }
    }

    private static XMLReader getXMLReaderWXMLReaderFactory() {
        try {
            return XMLReaderFactory.createXMLReader();
        } catch (SAXException e2) {
            return null;
        }
    }
}
