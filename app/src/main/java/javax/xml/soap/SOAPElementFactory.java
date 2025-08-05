package javax.xml.soap;

/* loaded from: rt.jar:javax/xml/soap/SOAPElementFactory.class */
public class SOAPElementFactory {
    private SOAPFactory soapFactory;

    private SOAPElementFactory(SOAPFactory soapFactory) {
        this.soapFactory = soapFactory;
    }

    public SOAPElement create(Name name) throws SOAPException {
        return this.soapFactory.createElement(name);
    }

    public SOAPElement create(String localName) throws SOAPException {
        return this.soapFactory.createElement(localName);
    }

    public SOAPElement create(String localName, String prefix, String uri) throws SOAPException {
        return this.soapFactory.createElement(localName, prefix, uri);
    }

    public static SOAPElementFactory newInstance() throws SOAPException {
        try {
            return new SOAPElementFactory(SOAPFactory.newInstance());
        } catch (Exception ex) {
            throw new SOAPException("Unable to create SOAP Element Factory: " + ex.getMessage());
        }
    }
}
