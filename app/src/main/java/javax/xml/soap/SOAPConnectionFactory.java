package javax.xml.soap;

/* loaded from: rt.jar:javax/xml/soap/SOAPConnectionFactory.class */
public abstract class SOAPConnectionFactory {
    static final String DEFAULT_SOAP_CONNECTION_FACTORY = "com.sun.xml.internal.messaging.saaj.client.p2p.HttpSOAPConnectionFactory";
    private static final String SF_PROPERTY = "javax.xml.soap.SOAPConnectionFactory";

    public abstract SOAPConnection createConnection() throws SOAPException;

    public static SOAPConnectionFactory newInstance() throws UnsupportedOperationException, SOAPException {
        try {
            return (SOAPConnectionFactory) FactoryFinder.find(SF_PROPERTY, DEFAULT_SOAP_CONNECTION_FACTORY);
        } catch (Exception ex) {
            throw new SOAPException("Unable to create SOAP connection factory: " + ex.getMessage());
        }
    }
}
