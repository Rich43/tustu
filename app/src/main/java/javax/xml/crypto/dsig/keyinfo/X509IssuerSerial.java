package javax.xml.crypto.dsig.keyinfo;

import java.math.BigInteger;
import javax.xml.crypto.XMLStructure;

/* loaded from: rt.jar:javax/xml/crypto/dsig/keyinfo/X509IssuerSerial.class */
public interface X509IssuerSerial extends XMLStructure {
    String getIssuerName();

    BigInteger getSerialNumber();
}
