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
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.DSAPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/content/keyvalues/DSAKeyValue.class */
public class DSAKeyValue extends SignatureElementProxy implements KeyValueContent {
    public DSAKeyValue(Element element, String str) throws XMLSecurityException {
        super(element, str);
    }

    public DSAKeyValue(Document document, BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4) {
        super(document);
        addReturnToSelf();
        addBigIntegerElement(bigInteger, Constants._TAG_P);
        addBigIntegerElement(bigInteger2, "Q");
        addBigIntegerElement(bigInteger3, "G");
        addBigIntegerElement(bigInteger4, Constants._TAG_Y);
    }

    public DSAKeyValue(Document document, Key key) throws IllegalArgumentException {
        super(document);
        addReturnToSelf();
        if (key instanceof DSAPublicKey) {
            DSAParams params = ((DSAPublicKey) key).getParams();
            addBigIntegerElement(params.getP(), Constants._TAG_P);
            addBigIntegerElement(params.getQ(), "Q");
            addBigIntegerElement(params.getG(), "G");
            addBigIntegerElement(((DSAPublicKey) key).getY(), Constants._TAG_Y);
            return;
        }
        throw new IllegalArgumentException(I18n.translate("KeyValue.IllegalArgument", new Object[]{Constants._TAG_DSAKEYVALUE, key.getClass().getName()}));
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.content.keyvalues.KeyValueContent
    public PublicKey getPublicKey() throws XMLSecurityException {
        try {
            return KeyFactory.getInstance("DSA").generatePublic(new DSAPublicKeySpec(getBigIntegerFromChildElement(Constants._TAG_Y, "http://www.w3.org/2000/09/xmldsig#"), getBigIntegerFromChildElement(Constants._TAG_P, "http://www.w3.org/2000/09/xmldsig#"), getBigIntegerFromChildElement("Q", "http://www.w3.org/2000/09/xmldsig#"), getBigIntegerFromChildElement("G", "http://www.w3.org/2000/09/xmldsig#")));
        } catch (NoSuchAlgorithmException e2) {
            throw new XMLSecurityException(e2);
        } catch (InvalidKeySpecException e3) {
            throw new XMLSecurityException(e3);
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseLocalName() {
        return Constants._TAG_DSAKEYVALUE;
    }
}
