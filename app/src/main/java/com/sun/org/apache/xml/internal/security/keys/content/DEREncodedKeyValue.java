package com.sun.org.apache.xml.internal.security.keys.content;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.Signature11ElementProxy;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/content/DEREncodedKeyValue.class */
public class DEREncodedKeyValue extends Signature11ElementProxy implements KeyInfoContent {
    private static final String[] supportedKeyTypes = {"RSA", "DSA", "EC"};

    public DEREncodedKeyValue(Element element, String str) throws XMLSecurityException {
        super(element, str);
    }

    public DEREncodedKeyValue(Document document, PublicKey publicKey) throws XMLSecurityException {
        super(document);
        addBase64Text(getEncodedDER(publicKey));
    }

    public DEREncodedKeyValue(Document document, byte[] bArr) {
        super(document);
        addBase64Text(bArr);
    }

    public void setId(String str) {
        setLocalIdAttribute(Constants._ATT_ID, str);
    }

    public String getId() {
        return getLocalAttribute(Constants._ATT_ID);
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseLocalName() {
        return Constants._TAG_DERENCODEDKEYVALUE;
    }

    public PublicKey getPublicKey() throws XMLSecurityException {
        PublicKey publicKeyGeneratePublic;
        byte[] bytesFromTextChild = getBytesFromTextChild();
        for (String str : supportedKeyTypes) {
            try {
                publicKeyGeneratePublic = KeyFactory.getInstance(str).generatePublic(new X509EncodedKeySpec(bytesFromTextChild));
            } catch (NoSuchAlgorithmException e2) {
            } catch (InvalidKeySpecException e3) {
            }
            if (publicKeyGeneratePublic != null) {
                return publicKeyGeneratePublic;
            }
        }
        throw new XMLSecurityException("DEREncodedKeyValue.UnsupportedEncodedKey");
    }

    protected byte[] getEncodedDER(PublicKey publicKey) throws XMLSecurityException {
        try {
            return ((X509EncodedKeySpec) KeyFactory.getInstance(publicKey.getAlgorithm()).getKeySpec(publicKey, X509EncodedKeySpec.class)).getEncoded();
        } catch (NoSuchAlgorithmException e2) {
            throw new XMLSecurityException(e2, "DEREncodedKeyValue.UnsupportedPublicKey", new Object[]{publicKey.getAlgorithm(), publicKey.getFormat(), publicKey.getClass().getName()});
        } catch (InvalidKeySpecException e3) {
            throw new XMLSecurityException(e3, "DEREncodedKeyValue.UnsupportedPublicKey", new Object[]{publicKey.getAlgorithm(), publicKey.getFormat(), publicKey.getClass().getName()});
        }
    }
}
