package com.sun.org.apache.xml.internal.security.keys.content.keyvalues;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.I18n;
import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/content/keyvalues/RSAKeyValue.class */
public class RSAKeyValue extends SignatureElementProxy implements KeyValueContent {
    public RSAKeyValue(Element element, String str) throws XMLSecurityException {
        super(element, str);
    }

    public RSAKeyValue(Document document, BigInteger bigInteger, BigInteger bigInteger2) {
        super(document);
        addReturnToSelf();
        addBigIntegerElement(bigInteger, Constants._TAG_MODULUS);
        addBigIntegerElement(bigInteger2, Constants._TAG_EXPONENT);
    }

    public RSAKeyValue(Document document, Key key) throws IllegalArgumentException {
        super(document);
        addReturnToSelf();
        if (key instanceof RSAPublicKey) {
            addBigIntegerElement(((RSAPublicKey) key).getModulus(), Constants._TAG_MODULUS);
            addBigIntegerElement(((RSAPublicKey) key).getPublicExponent(), Constants._TAG_EXPONENT);
            return;
        }
        throw new IllegalArgumentException(I18n.translate("KeyValue.IllegalArgument", new Object[]{Constants._TAG_RSAKEYVALUE, key.getClass().getName()}));
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.content.keyvalues.KeyValueContent
    public PublicKey getPublicKey() throws XMLSecurityException {
        try {
            return KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(getBigIntegerFromChildElement(Constants._TAG_MODULUS, "http://www.w3.org/2000/09/xmldsig#"), getBigIntegerFromChildElement(Constants._TAG_EXPONENT, "http://www.w3.org/2000/09/xmldsig#")));
        } catch (NoSuchAlgorithmException e2) {
            throw new XMLSecurityException(e2);
        } catch (InvalidKeySpecException e3) {
            throw new XMLSecurityException(e3);
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseLocalName() {
        return Constants._TAG_RSAKEYVALUE;
    }
}
