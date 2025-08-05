package javax.xml.soap;

import javax.xml.namespace.QName;
import org.w3c.dom.Element;

/* loaded from: rt.jar:javax/xml/soap/SOAPFactory.class */
public abstract class SOAPFactory {
    private static final String SOAP_FACTORY_PROPERTY = "javax.xml.soap.SOAPFactory";
    static final String DEFAULT_SOAP_FACTORY = "com.sun.xml.internal.messaging.saaj.soap.ver1_1.SOAPFactory1_1Impl";

    public abstract SOAPElement createElement(Name name) throws SOAPException;

    public abstract SOAPElement createElement(String str) throws SOAPException;

    public abstract SOAPElement createElement(String str, String str2, String str3) throws SOAPException;

    public abstract Detail createDetail() throws SOAPException;

    public abstract SOAPFault createFault(String str, QName qName) throws SOAPException;

    public abstract SOAPFault createFault() throws SOAPException;

    public abstract Name createName(String str, String str2, String str3) throws SOAPException;

    public abstract Name createName(String str) throws SOAPException;

    public SOAPElement createElement(Element domElement) throws SOAPException {
        throw new UnsupportedOperationException("createElement(org.w3c.dom.Element) must be overridden by all subclasses of SOAPFactory.");
    }

    public SOAPElement createElement(QName qname) throws SOAPException {
        throw new UnsupportedOperationException("createElement(QName) must be overridden by all subclasses of SOAPFactory.");
    }

    public static SOAPFactory newInstance() throws SOAPException {
        try {
            SOAPFactory factory = (SOAPFactory) FactoryFinder.find(SOAP_FACTORY_PROPERTY, DEFAULT_SOAP_FACTORY, false);
            if (factory != null) {
                return factory;
            }
            return newInstance("SOAP 1.1 Protocol");
        } catch (Exception ex) {
            throw new SOAPException("Unable to create SOAP Factory: " + ex.getMessage());
        }
    }

    public static SOAPFactory newInstance(String protocol) throws SOAPException {
        return SAAJMetaFactory.getInstance().newSOAPFactory(protocol);
    }
}
