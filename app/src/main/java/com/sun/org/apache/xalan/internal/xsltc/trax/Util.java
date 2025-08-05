package com.sun.org.apache.xalan.internal.xsltc.trax;

import com.sun.org.apache.xalan.internal.xsltc.compiler.XSLTC;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import java.io.InputStream;
import java.io.Reader;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamSource;
import jdk.xml.internal.JdkXmlFeatures;
import jdk.xml.internal.JdkXmlUtils;
import jdk.xml.internal.XMLSecurityManager;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/trax/Util.class */
public final class Util {
    private static final String property = "org.xml.sax.driver";

    public static String baseName(String name) {
        return com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util.baseName(name);
    }

    public static String noExtName(String name) {
        return com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util.noExtName(name);
    }

    public static String toJavaName(String name) {
        return com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util.toJavaName(name);
    }

    public static InputSource getInputSource(XSLTC xsltc, Source source) throws TransformerConfigurationException {
        InputSource input;
        String systemId = source.getSystemId();
        try {
            if (source instanceof SAXSource) {
                SAXSource sax = (SAXSource) source;
                input = sax.getInputSource();
                try {
                    XMLReader reader = sax.getXMLReader();
                    if (reader == null) {
                        boolean overrideDefaultParser = xsltc.getFeature(JdkXmlFeatures.XmlFeature.JDK_OVERRIDE_PARSER);
                        reader = JdkXmlUtils.getXMLReader(overrideDefaultParser, xsltc.isSecureProcessing());
                    } else {
                        reader.setFeature("http://xml.org/sax/features/namespaces", true);
                        reader.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
                    }
                    try {
                        reader.setProperty("http://javax.xml.XMLConstants/property/accessExternalDTD", xsltc.getProperty("http://javax.xml.XMLConstants/property/accessExternalDTD"));
                    } catch (SAXNotRecognizedException e2) {
                        XMLSecurityManager.printWarning(reader.getClass().getName(), "http://javax.xml.XMLConstants/property/accessExternalDTD", e2);
                    }
                    String lastProperty = "";
                    try {
                        XMLSecurityManager securityManager = (XMLSecurityManager) xsltc.getProperty("http://apache.org/xml/properties/security-manager");
                        if (securityManager != null) {
                            for (XMLSecurityManager.Limit limit : XMLSecurityManager.Limit.values()) {
                                if (limit.isSupported(XMLSecurityManager.Processor.PARSER)) {
                                    String lastProperty2 = limit.apiProperty();
                                    reader.setProperty(lastProperty2, securityManager.getLimitValueAsString(limit));
                                }
                            }
                            if (securityManager.printEntityCountInfo()) {
                                lastProperty = "http://www.oracle.com/xml/jaxp/properties/getEntityCountInfo";
                                reader.setProperty("http://www.oracle.com/xml/jaxp/properties/getEntityCountInfo", "yes");
                            }
                        }
                    } catch (SAXException se) {
                        XMLSecurityManager.printWarning(reader.getClass().getName(), lastProperty, se);
                    }
                    xsltc.setXMLReader(reader);
                } catch (SAXNotRecognizedException snre) {
                    throw new TransformerConfigurationException("SAXNotRecognizedException ", snre);
                } catch (SAXNotSupportedException snse) {
                    throw new TransformerConfigurationException("SAXNotSupportedException ", snse);
                }
            } else if (source instanceof DOMSource) {
                DOMSource domsrc = (DOMSource) source;
                Document dom = (Document) domsrc.getNode();
                DOM2SAX dom2sax = new DOM2SAX(dom);
                xsltc.setXMLReader(dom2sax);
                input = SAXSource.sourceToInputSource(source);
                if (input == null) {
                    input = new InputSource(domsrc.getSystemId());
                }
            } else if (source instanceof StAXSource) {
                StAXSource staxSource = (StAXSource) source;
                if (staxSource.getXMLEventReader() != null) {
                    XMLEventReader xmlEventReader = staxSource.getXMLEventReader();
                    StAXEvent2SAX staxevent2sax = new StAXEvent2SAX(xmlEventReader);
                    xsltc.setXMLReader(staxevent2sax);
                } else if (staxSource.getXMLStreamReader() != null) {
                    XMLStreamReader xmlStreamReader = staxSource.getXMLStreamReader();
                    StAXStream2SAX staxStream2SAX = new StAXStream2SAX(xmlStreamReader);
                    xsltc.setXMLReader(staxStream2SAX);
                }
                input = SAXSource.sourceToInputSource(source);
                if (input == null) {
                    input = new InputSource(staxSource.getSystemId());
                }
            } else if (source instanceof StreamSource) {
                StreamSource stream = (StreamSource) source;
                InputStream istream = stream.getInputStream();
                Reader reader2 = stream.getReader();
                xsltc.setXMLReader(null);
                if (istream != null) {
                    input = new InputSource(istream);
                } else if (reader2 != null) {
                    input = new InputSource(reader2);
                } else {
                    input = new InputSource(systemId);
                }
            } else {
                ErrorMsg err = new ErrorMsg(ErrorMsg.JAXP_UNKNOWN_SOURCE_ERR);
                throw new TransformerConfigurationException(err.toString());
            }
            input.setSystemId(systemId);
            return input;
        } catch (NullPointerException e3) {
            ErrorMsg err2 = new ErrorMsg(ErrorMsg.JAXP_NO_SOURCE_ERR, "TransformerFactory.newTemplates()");
            throw new TransformerConfigurationException(err2.toString());
        } catch (SecurityException e4) {
            ErrorMsg err3 = new ErrorMsg(ErrorMsg.FILE_ACCESS_ERR, systemId);
            throw new TransformerConfigurationException(err3.toString());
        }
    }
}
