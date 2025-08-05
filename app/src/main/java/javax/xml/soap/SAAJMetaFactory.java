package javax.xml.soap;

/* loaded from: rt.jar:javax/xml/soap/SAAJMetaFactory.class */
public abstract class SAAJMetaFactory {
    private static final String META_FACTORY_CLASS_PROPERTY = "javax.xml.soap.MetaFactory";
    static final String DEFAULT_META_FACTORY_CLASS = "com.sun.xml.internal.messaging.saaj.soap.SAAJMetaFactoryImpl";

    protected abstract MessageFactory newMessageFactory(String str) throws SOAPException;

    protected abstract SOAPFactory newSOAPFactory(String str) throws SOAPException;

    static SAAJMetaFactory getInstance() throws SOAPException {
        try {
            SAAJMetaFactory instance = (SAAJMetaFactory) FactoryFinder.find(META_FACTORY_CLASS_PROPERTY, DEFAULT_META_FACTORY_CLASS);
            return instance;
        } catch (Exception e2) {
            throw new SOAPException("Unable to create SAAJ meta-factory" + e2.getMessage());
        }
    }

    protected SAAJMetaFactory() {
    }
}
