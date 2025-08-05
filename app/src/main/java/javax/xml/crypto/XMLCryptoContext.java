package javax.xml.crypto;

/* loaded from: rt.jar:javax/xml/crypto/XMLCryptoContext.class */
public interface XMLCryptoContext {
    String getBaseURI();

    void setBaseURI(String str);

    KeySelector getKeySelector();

    void setKeySelector(KeySelector keySelector);

    URIDereferencer getURIDereferencer();

    void setURIDereferencer(URIDereferencer uRIDereferencer);

    String getNamespacePrefix(String str, String str2);

    String putNamespacePrefix(String str, String str2);

    String getDefaultNamespacePrefix();

    void setDefaultNamespacePrefix(String str);

    Object setProperty(String str, Object obj);

    Object getProperty(String str);

    Object get(Object obj);

    Object put(Object obj, Object obj2);
}
