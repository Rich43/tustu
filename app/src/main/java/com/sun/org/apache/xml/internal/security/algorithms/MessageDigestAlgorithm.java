package com.sun.org.apache.xml.internal.security.algorithms;

import com.sun.org.apache.xml.internal.security.signature.XMLSignatureException;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import org.w3c.dom.Document;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/algorithms/MessageDigestAlgorithm.class */
public class MessageDigestAlgorithm extends Algorithm {
    public static final String ALGO_ID_DIGEST_NOT_RECOMMENDED_MD5 = "http://www.w3.org/2001/04/xmldsig-more#md5";
    public static final String ALGO_ID_DIGEST_SHA1 = "http://www.w3.org/2000/09/xmldsig#sha1";
    public static final String ALGO_ID_DIGEST_SHA224 = "http://www.w3.org/2001/04/xmldsig-more#sha224";
    public static final String ALGO_ID_DIGEST_SHA256 = "http://www.w3.org/2001/04/xmlenc#sha256";
    public static final String ALGO_ID_DIGEST_SHA384 = "http://www.w3.org/2001/04/xmldsig-more#sha384";
    public static final String ALGO_ID_DIGEST_SHA512 = "http://www.w3.org/2001/04/xmlenc#sha512";
    public static final String ALGO_ID_DIGEST_RIPEMD160 = "http://www.w3.org/2001/04/xmlenc#ripemd160";
    private final MessageDigest algorithm;

    private MessageDigestAlgorithm(Document document, String str) throws XMLSignatureException {
        super(document, str);
        this.algorithm = getDigestInstance(str);
    }

    public static MessageDigestAlgorithm getInstance(Document document, String str) throws XMLSignatureException {
        return new MessageDigestAlgorithm(document, str);
    }

    private static MessageDigest getDigestInstance(String str) throws XMLSignatureException {
        MessageDigest messageDigest;
        String strTranslateURItoJCEID = JCEMapper.translateURItoJCEID(str);
        if (strTranslateURItoJCEID == null) {
            throw new XMLSignatureException("algorithms.NoSuchMap", new Object[]{str});
        }
        String providerId = JCEMapper.getProviderId();
        try {
            if (providerId == null) {
                messageDigest = MessageDigest.getInstance(strTranslateURItoJCEID);
            } else {
                messageDigest = MessageDigest.getInstance(strTranslateURItoJCEID, providerId);
            }
            return messageDigest;
        } catch (NoSuchAlgorithmException e2) {
            throw new XMLSignatureException("algorithms.NoSuchAlgorithm", new Object[]{strTranslateURItoJCEID, e2.getLocalizedMessage()});
        } catch (NoSuchProviderException e3) {
            throw new XMLSignatureException("algorithms.NoSuchAlgorithm", new Object[]{strTranslateURItoJCEID, e3.getLocalizedMessage()});
        }
    }

    public MessageDigest getAlgorithm() {
        return this.algorithm;
    }

    public static boolean isEqual(byte[] bArr, byte[] bArr2) {
        return MessageDigest.isEqual(bArr, bArr2);
    }

    public byte[] digest() {
        return this.algorithm.digest();
    }

    public byte[] digest(byte[] bArr) {
        return this.algorithm.digest(bArr);
    }

    public int digest(byte[] bArr, int i2, int i3) throws DigestException {
        return this.algorithm.digest(bArr, i2, i3);
    }

    public String getJCEAlgorithmString() {
        return this.algorithm.getAlgorithm();
    }

    public Provider getJCEProvider() {
        return this.algorithm.getProvider();
    }

    public int getDigestLength() {
        return this.algorithm.getDigestLength();
    }

    public void reset() {
        this.algorithm.reset();
    }

    public void update(byte[] bArr) {
        this.algorithm.update(bArr);
    }

    public void update(byte b2) {
        this.algorithm.update(b2);
    }

    public void update(byte[] bArr, int i2, int i3) {
        this.algorithm.update(bArr, i2, i3);
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy, com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseNamespace() {
        return "http://www.w3.org/2000/09/xmldsig#";
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseLocalName() {
        return Constants._TAG_DIGESTMETHOD;
    }
}
