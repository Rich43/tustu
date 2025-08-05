package javax.xml.crypto.dsig.keyinfo;

import java.util.List;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;

/* loaded from: rt.jar:javax/xml/crypto/dsig/keyinfo/KeyInfo.class */
public interface KeyInfo extends XMLStructure {
    List getContent();

    String getId();

    void marshal(XMLStructure xMLStructure, XMLCryptoContext xMLCryptoContext) throws MarshalException;
}
