package javax.xml.soap;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: rt.jar:javax/xml/soap/MessageFactory.class */
public abstract class MessageFactory {
    static final String DEFAULT_MESSAGE_FACTORY = "com.sun.xml.internal.messaging.saaj.soap.ver1_1.SOAPMessageFactory1_1Impl";
    private static final String MESSAGE_FACTORY_PROPERTY = "javax.xml.soap.MessageFactory";

    public abstract SOAPMessage createMessage() throws SOAPException;

    public abstract SOAPMessage createMessage(MimeHeaders mimeHeaders, InputStream inputStream) throws SOAPException, IOException;

    public static MessageFactory newInstance() throws SOAPException {
        try {
            MessageFactory factory = (MessageFactory) FactoryFinder.find(MESSAGE_FACTORY_PROPERTY, DEFAULT_MESSAGE_FACTORY, false);
            if (factory != null) {
                return factory;
            }
            return newInstance("SOAP 1.1 Protocol");
        } catch (Exception ex) {
            throw new SOAPException("Unable to create message factory for SOAP: " + ex.getMessage());
        }
    }

    public static MessageFactory newInstance(String protocol) throws SOAPException {
        return SAAJMetaFactory.getInstance().newMessageFactory(protocol);
    }
}
