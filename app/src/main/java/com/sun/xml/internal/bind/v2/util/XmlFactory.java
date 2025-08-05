package com.sun.xml.internal.bind.v2.util;

import com.sun.org.apache.xerces.internal.impl.Constants;
import com.sun.xml.internal.bind.v2.Messages;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/util/XmlFactory.class */
public class XmlFactory {
    public static final String ACCESS_EXTERNAL_SCHEMA = "http://javax.xml.XMLConstants/property/accessExternalSchema";
    public static final String ACCESS_EXTERNAL_DTD = "http://javax.xml.XMLConstants/property/accessExternalDTD";
    private static final String DISABLE_XML_SECURITY = "com.sun.xml.internal.bind.disableXmlSecurity";
    private static final Logger LOGGER = Logger.getLogger(XmlFactory.class.getName());
    private static final boolean XML_SECURITY_DISABLED = ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: com.sun.xml.internal.bind.v2.util.XmlFactory.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public Boolean run2() {
            return Boolean.valueOf(Boolean.getBoolean("com.sun.xml.internal.bind.disableXmlSecurity"));
        }
    })).booleanValue();

    private static boolean isXMLSecurityDisabled(boolean runtimeSetting) {
        return XML_SECURITY_DISABLED || runtimeSetting;
    }

    public static SchemaFactory createSchemaFactory(String language, boolean disableSecureProcessing) throws IllegalStateException {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(language);
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "SchemaFactory instance: {0}", factory);
            }
            factory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", !isXMLSecurityDisabled(disableSecureProcessing));
            return factory;
        } catch (AbstractMethodError er) {
            LOGGER.log(Level.SEVERE, (String) null, (Throwable) er);
            throw new IllegalStateException(Messages.INVALID_JAXP_IMPLEMENTATION.format(new Object[0]), er);
        } catch (SAXNotRecognizedException ex) {
            LOGGER.log(Level.SEVERE, (String) null, (Throwable) ex);
            throw new IllegalStateException(ex);
        } catch (SAXNotSupportedException ex2) {
            LOGGER.log(Level.SEVERE, (String) null, (Throwable) ex2);
            throw new IllegalStateException(ex2);
        }
    }

    public static SAXParserFactory createParserFactory(boolean disableSecureProcessing) throws IllegalStateException {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "SAXParserFactory instance: {0}", factory);
            }
            factory.setNamespaceAware(true);
            factory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", !isXMLSecurityDisabled(disableSecureProcessing));
            return factory;
        } catch (AbstractMethodError er) {
            LOGGER.log(Level.SEVERE, (String) null, (Throwable) er);
            throw new IllegalStateException(Messages.INVALID_JAXP_IMPLEMENTATION.format(new Object[0]), er);
        } catch (ParserConfigurationException ex) {
            LOGGER.log(Level.SEVERE, (String) null, (Throwable) ex);
            throw new IllegalStateException(ex);
        } catch (SAXNotRecognizedException ex2) {
            LOGGER.log(Level.SEVERE, (String) null, (Throwable) ex2);
            throw new IllegalStateException(ex2);
        } catch (SAXNotSupportedException ex3) {
            LOGGER.log(Level.SEVERE, (String) null, (Throwable) ex3);
            throw new IllegalStateException(ex3);
        }
    }

    public static XPathFactory createXPathFactory(boolean disableSecureProcessing) throws IllegalStateException {
        try {
            XPathFactory factory = XPathFactory.newInstance();
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "XPathFactory instance: {0}", factory);
            }
            factory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", !isXMLSecurityDisabled(disableSecureProcessing));
            return factory;
        } catch (AbstractMethodError er) {
            LOGGER.log(Level.SEVERE, (String) null, (Throwable) er);
            throw new IllegalStateException(Messages.INVALID_JAXP_IMPLEMENTATION.format(new Object[0]), er);
        } catch (XPathFactoryConfigurationException ex) {
            LOGGER.log(Level.SEVERE, (String) null, (Throwable) ex);
            throw new IllegalStateException(ex);
        }
    }

    public static TransformerFactory createTransformerFactory(boolean disableSecureProcessing) throws IllegalStateException, TransformerFactoryConfigurationError {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "TransformerFactory instance: {0}", factory);
            }
            factory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", !isXMLSecurityDisabled(disableSecureProcessing));
            return factory;
        } catch (AbstractMethodError er) {
            LOGGER.log(Level.SEVERE, (String) null, (Throwable) er);
            throw new IllegalStateException(Messages.INVALID_JAXP_IMPLEMENTATION.format(new Object[0]), er);
        } catch (TransformerConfigurationException ex) {
            LOGGER.log(Level.SEVERE, (String) null, (Throwable) ex);
            throw new IllegalStateException(ex);
        }
    }

    public static DocumentBuilderFactory createDocumentBuilderFactory(boolean disableSecureProcessing) throws IllegalStateException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "DocumentBuilderFactory instance: {0}", factory);
            }
            factory.setNamespaceAware(true);
            factory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", !isXMLSecurityDisabled(disableSecureProcessing));
            return factory;
        } catch (AbstractMethodError er) {
            LOGGER.log(Level.SEVERE, (String) null, (Throwable) er);
            throw new IllegalStateException(Messages.INVALID_JAXP_IMPLEMENTATION.format(new Object[0]), er);
        } catch (ParserConfigurationException ex) {
            LOGGER.log(Level.SEVERE, (String) null, (Throwable) ex);
            throw new IllegalStateException(ex);
        }
    }

    public static SchemaFactory allowExternalAccess(SchemaFactory sf, String value, boolean disableSecureProcessing) {
        if (isXMLSecurityDisabled(disableSecureProcessing)) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, Messages.JAXP_XML_SECURITY_DISABLED.format(new Object[0]));
            }
            return sf;
        }
        if (System.getProperty(Constants.SP_ACCESS_EXTERNAL_SCHEMA) != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, Messages.JAXP_EXTERNAL_ACCESS_CONFIGURED.format(new Object[0]));
            }
            return sf;
        }
        try {
            sf.setProperty("http://javax.xml.XMLConstants/property/accessExternalSchema", value);
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, Messages.JAXP_SUPPORTED_PROPERTY.format("http://javax.xml.XMLConstants/property/accessExternalSchema"));
            }
        } catch (SAXException ignored) {
            if (LOGGER.isLoggable(Level.CONFIG)) {
                LOGGER.log(Level.CONFIG, Messages.JAXP_UNSUPPORTED_PROPERTY.format("http://javax.xml.XMLConstants/property/accessExternalSchema"), (Throwable) ignored);
            }
        }
        return sf;
    }

    public static SchemaFactory allowExternalDTDAccess(SchemaFactory sf, String value, boolean disableSecureProcessing) {
        if (isXMLSecurityDisabled(disableSecureProcessing)) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, Messages.JAXP_XML_SECURITY_DISABLED.format(new Object[0]));
            }
            return sf;
        }
        if (System.getProperty("javax.xml.accessExternalDTD") != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, Messages.JAXP_EXTERNAL_ACCESS_CONFIGURED.format(new Object[0]));
            }
            return sf;
        }
        try {
            sf.setProperty("http://javax.xml.XMLConstants/property/accessExternalDTD", value);
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, Messages.JAXP_SUPPORTED_PROPERTY.format("http://javax.xml.XMLConstants/property/accessExternalDTD"));
            }
        } catch (SAXException ignored) {
            if (LOGGER.isLoggable(Level.CONFIG)) {
                LOGGER.log(Level.CONFIG, Messages.JAXP_UNSUPPORTED_PROPERTY.format("http://javax.xml.XMLConstants/property/accessExternalDTD"), (Throwable) ignored);
            }
        }
        return sf;
    }
}
