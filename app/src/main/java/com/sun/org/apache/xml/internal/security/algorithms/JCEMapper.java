package com.sun.org.apache.xml.internal.security.algorithms;

import com.sun.glass.ui.Platform;
import com.sun.org.apache.xml.internal.security.signature.XMLSignature;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.JavaUtils;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/algorithms/JCEMapper.class */
public class JCEMapper {
    private static final Logger LOG = LoggerFactory.getLogger(JCEMapper.class);
    private static Map<String, Algorithm> algorithmsMap = new ConcurrentHashMap();
    private static String providerName;

    public static void register(String str, Algorithm algorithm) {
        JavaUtils.checkRegisterPermission();
        algorithmsMap.put(str, algorithm);
    }

    public static void registerDefaultAlgorithms() {
        algorithmsMap.put(MessageDigestAlgorithm.ALGO_ID_DIGEST_NOT_RECOMMENDED_MD5, new Algorithm("", "MD5", "MessageDigest"));
        algorithmsMap.put("http://www.w3.org/2001/04/xmlenc#ripemd160", new Algorithm("", "RIPEMD160", "MessageDigest"));
        algorithmsMap.put("http://www.w3.org/2000/09/xmldsig#sha1", new Algorithm("", "SHA-1", "MessageDigest"));
        algorithmsMap.put(MessageDigestAlgorithm.ALGO_ID_DIGEST_SHA224, new Algorithm("", "SHA-224", "MessageDigest"));
        algorithmsMap.put("http://www.w3.org/2001/04/xmlenc#sha256", new Algorithm("", "SHA-256", "MessageDigest"));
        algorithmsMap.put(MessageDigestAlgorithm.ALGO_ID_DIGEST_SHA384, new Algorithm("", "SHA-384", "MessageDigest"));
        algorithmsMap.put("http://www.w3.org/2001/04/xmlenc#sha512", new Algorithm("", "SHA-512", "MessageDigest"));
        algorithmsMap.put("http://www.w3.org/2000/09/xmldsig#dsa-sha1", new Algorithm("DSA", "SHA1withDSA", Constants._TAG_SIGNATURE));
        algorithmsMap.put(XMLSignature.ALGO_ID_SIGNATURE_DSA_SHA256, new Algorithm("DSA", "SHA256withDSA", Constants._TAG_SIGNATURE));
        algorithmsMap.put(XMLSignature.ALGO_ID_SIGNATURE_NOT_RECOMMENDED_RSA_MD5, new Algorithm("RSA", "MD5withRSA", Constants._TAG_SIGNATURE));
        algorithmsMap.put(XMLSignature.ALGO_ID_SIGNATURE_RSA_RIPEMD160, new Algorithm("RSA", "RIPEMD160withRSA", Constants._TAG_SIGNATURE));
        algorithmsMap.put("http://www.w3.org/2000/09/xmldsig#rsa-sha1", new Algorithm("RSA", "SHA1withRSA", Constants._TAG_SIGNATURE));
        algorithmsMap.put(XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA224, new Algorithm("RSA", "SHA224withRSA", Constants._TAG_SIGNATURE));
        algorithmsMap.put(XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA256, new Algorithm("RSA", "SHA256withRSA", Constants._TAG_SIGNATURE));
        algorithmsMap.put(XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA384, new Algorithm("RSA", "SHA384withRSA", Constants._TAG_SIGNATURE));
        algorithmsMap.put(XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA512, new Algorithm("RSA", "SHA512withRSA", Constants._TAG_SIGNATURE));
        algorithmsMap.put(XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA1_MGF1, new Algorithm("RSA", "SHA1withRSAandMGF1", Constants._TAG_SIGNATURE));
        algorithmsMap.put(XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA224_MGF1, new Algorithm("RSA", "SHA224withRSAandMGF1", Constants._TAG_SIGNATURE));
        algorithmsMap.put(XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA256_MGF1, new Algorithm("RSA", "SHA256withRSAandMGF1", Constants._TAG_SIGNATURE));
        algorithmsMap.put(XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA384_MGF1, new Algorithm("RSA", "SHA384withRSAandMGF1", Constants._TAG_SIGNATURE));
        algorithmsMap.put(XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA512_MGF1, new Algorithm("RSA", "SHA512withRSAandMGF1", Constants._TAG_SIGNATURE));
        algorithmsMap.put(XMLSignature.ALGO_ID_SIGNATURE_ECDSA_SHA1, new Algorithm("EC", "SHA1withECDSA", Constants._TAG_SIGNATURE));
        algorithmsMap.put(XMLSignature.ALGO_ID_SIGNATURE_ECDSA_SHA224, new Algorithm("EC", "SHA224withECDSA", Constants._TAG_SIGNATURE));
        algorithmsMap.put(XMLSignature.ALGO_ID_SIGNATURE_ECDSA_SHA256, new Algorithm("EC", "SHA256withECDSA", Constants._TAG_SIGNATURE));
        algorithmsMap.put(XMLSignature.ALGO_ID_SIGNATURE_ECDSA_SHA384, new Algorithm("EC", "SHA384withECDSA", Constants._TAG_SIGNATURE));
        algorithmsMap.put(XMLSignature.ALGO_ID_SIGNATURE_ECDSA_SHA512, new Algorithm("EC", "SHA512withECDSA", Constants._TAG_SIGNATURE));
        algorithmsMap.put(XMLSignature.ALGO_ID_SIGNATURE_ECDSA_RIPEMD160, new Algorithm("EC", "RIPEMD160withECDSA", Constants._TAG_SIGNATURE));
        algorithmsMap.put(XMLSignature.ALGO_ID_MAC_HMAC_NOT_RECOMMENDED_MD5, new Algorithm("", "HmacMD5", Platform.MAC, 0, 0));
        algorithmsMap.put(XMLSignature.ALGO_ID_MAC_HMAC_RIPEMD160, new Algorithm("", "HMACRIPEMD160", Platform.MAC, 0, 0));
        algorithmsMap.put("http://www.w3.org/2000/09/xmldsig#hmac-sha1", new Algorithm("", "HmacSHA1", Platform.MAC, 0, 0));
        algorithmsMap.put(XMLSignature.ALGO_ID_MAC_HMAC_SHA224, new Algorithm("", "HmacSHA224", Platform.MAC, 0, 0));
        algorithmsMap.put(XMLSignature.ALGO_ID_MAC_HMAC_SHA256, new Algorithm("", "HmacSHA256", Platform.MAC, 0, 0));
        algorithmsMap.put(XMLSignature.ALGO_ID_MAC_HMAC_SHA384, new Algorithm("", "HmacSHA384", Platform.MAC, 0, 0));
        algorithmsMap.put(XMLSignature.ALGO_ID_MAC_HMAC_SHA512, new Algorithm("", "HmacSHA512", Platform.MAC, 0, 0));
    }

    public static String translateURItoJCEID(String str) {
        Algorithm algorithm = getAlgorithm(str);
        if (algorithm != null) {
            return algorithm.jceName;
        }
        return null;
    }

    public static String getAlgorithmClassFromURI(String str) {
        Algorithm algorithm = getAlgorithm(str);
        if (algorithm != null) {
            return algorithm.algorithmClass;
        }
        return null;
    }

    public static int getKeyLengthFromURI(String str) {
        Algorithm algorithm = getAlgorithm(str);
        if (algorithm != null) {
            return algorithm.keyLength;
        }
        return 0;
    }

    public static int getIVLengthFromURI(String str) {
        Algorithm algorithm = getAlgorithm(str);
        if (algorithm != null) {
            return algorithm.ivLength;
        }
        return 0;
    }

    public static String getJCEKeyAlgorithmFromURI(String str) {
        Algorithm algorithm = getAlgorithm(str);
        if (algorithm != null) {
            return algorithm.requiredKey;
        }
        return null;
    }

    public static String getJCEProviderFromURI(String str) {
        Algorithm algorithm = getAlgorithm(str);
        if (algorithm != null) {
            return algorithm.jceProvider;
        }
        return null;
    }

    private static Algorithm getAlgorithm(String str) {
        LOG.debug("Request for URI {}", str);
        if (str != null) {
            return algorithmsMap.get(str);
        }
        return null;
    }

    public static String getProviderId() {
        return providerName;
    }

    public static void setProviderId(String str) {
        JavaUtils.checkRegisterPermission();
        providerName = str;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/algorithms/JCEMapper$Algorithm.class */
    public static class Algorithm {
        final String requiredKey;
        final String jceName;
        final String algorithmClass;
        final int keyLength;
        final int ivLength;
        final String jceProvider;

        public Algorithm(Element element) {
            this.requiredKey = element.getAttributeNS(null, "RequiredKey");
            this.jceName = element.getAttributeNS(null, "JCEName");
            this.algorithmClass = element.getAttributeNS(null, "AlgorithmClass");
            this.jceProvider = element.getAttributeNS(null, "JCEProvider");
            if (element.hasAttribute("KeyLength")) {
                this.keyLength = Integer.parseInt(element.getAttributeNS(null, "KeyLength"));
            } else {
                this.keyLength = 0;
            }
            if (element.hasAttribute("IVLength")) {
                this.ivLength = Integer.parseInt(element.getAttributeNS(null, "IVLength"));
            } else {
                this.ivLength = 0;
            }
        }

        public Algorithm(String str, String str2) {
            this(str, str2, null, 0, 0);
        }

        public Algorithm(String str, String str2, String str3) {
            this(str, str2, str3, 0, 0);
        }

        public Algorithm(String str, String str2, int i2) {
            this(str, str2, null, i2, 0);
        }

        public Algorithm(String str, String str2, String str3, int i2, int i3) {
            this(str, str2, str3, i2, i3, null);
        }

        public Algorithm(String str, String str2, String str3, int i2, int i3, String str4) {
            this.requiredKey = str;
            this.jceName = str2;
            this.algorithmClass = str3;
            this.keyLength = i2;
            this.ivLength = i3;
            this.jceProvider = str4;
        }
    }
}
