package sun.security.mscapi;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.Provider;
import java.util.HashMap;
import java.util.Map;
import sun.security.action.PutAllAction;

/* loaded from: sunmscapi.jar:sun/security/mscapi/SunMSCAPI.class */
public final class SunMSCAPI extends Provider {
    private static final long serialVersionUID = 8622598936488630849L;
    private static final String INFO = "Sun's Microsoft Crypto API provider";

    static {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.security.mscapi.SunMSCAPI.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                System.loadLibrary("sunmscapi");
                return null;
            }
        });
    }

    public SunMSCAPI() {
        super("SunMSCAPI", 1.8d, INFO);
        Map map = System.getSecurityManager() == null ? this : new HashMap();
        map.put("SecureRandom.Windows-PRNG", "sun.security.mscapi.PRNG");
        map.put("KeyStore.Windows-MY", "sun.security.mscapi.CKeyStore$MY");
        map.put("KeyStore.Windows-ROOT", "sun.security.mscapi.CKeyStore$ROOT");
        map.put("Signature.NONEwithRSA", "sun.security.mscapi.CSignature$NONEwithRSA");
        map.put("Signature.SHA1withRSA", "sun.security.mscapi.CSignature$SHA1withRSA");
        map.put("Signature.SHA256withRSA", "sun.security.mscapi.CSignature$SHA256withRSA");
        map.put("Alg.Alias.Signature.1.2.840.113549.1.1.11", "SHA256withRSA");
        map.put("Alg.Alias.Signature.OID.1.2.840.113549.1.1.11", "SHA256withRSA");
        map.put("Signature.SHA384withRSA", "sun.security.mscapi.CSignature$SHA384withRSA");
        map.put("Alg.Alias.Signature.1.2.840.113549.1.1.12", "SHA384withRSA");
        map.put("Alg.Alias.Signature.OID.1.2.840.113549.1.1.12", "SHA384withRSA");
        map.put("Signature.SHA512withRSA", "sun.security.mscapi.CSignature$SHA512withRSA");
        map.put("Alg.Alias.Signature.1.2.840.113549.1.1.13", "SHA512withRSA");
        map.put("Alg.Alias.Signature.OID.1.2.840.113549.1.1.13", "SHA512withRSA");
        map.put("Signature.MD5withRSA", "sun.security.mscapi.CSignature$MD5withRSA");
        map.put("Signature.MD2withRSA", "sun.security.mscapi.CSignature$MD2withRSA");
        map.put("Signature.RSASSA-PSS", "sun.security.mscapi.CSignature$PSS");
        map.put("Alg.Alias.Signature.1.2.840.113549.1.1.10", "RSASSA-PSS");
        map.put("Alg.Alias.Signature.OID.1.2.840.113549.1.1.10", "RSASSA-PSS");
        map.put("Signature.SHA1withECDSA", "sun.security.mscapi.CSignature$SHA1withECDSA");
        map.put("Alg.Alias.Signature.1.2.840.10045.4.1", "SHA1withECDSA");
        map.put("Alg.Alias.Signature.OID.1.2.840.10045.4.1", "SHA1withECDSA");
        map.put("Signature.SHA224withECDSA", "sun.security.mscapi.CSignature$SHA224withECDSA");
        map.put("Alg.Alias.Signature.1.2.840.10045.4.3.1", "SHA224withECDSA");
        map.put("Alg.Alias.Signature.OID.1.2.840.10045.4.3.1", "SHA224withECDSA");
        map.put("Signature.SHA256withECDSA", "sun.security.mscapi.CSignature$SHA256withECDSA");
        map.put("Alg.Alias.Signature.1.2.840.10045.4.3.2", "SHA256withECDSA");
        map.put("Alg.Alias.Signature.OID.1.2.840.10045.4.3.2", "SHA256withECDSA");
        map.put("Signature.SHA384withECDSA", "sun.security.mscapi.CSignature$SHA384withECDSA");
        map.put("Alg.Alias.Signature.1.2.840.10045.4.3.3", "SHA384withECDSA");
        map.put("Alg.Alias.Signature.OID.1.2.840.10045.4.3.3", "SHA384withECDSA");
        map.put("Signature.SHA512withECDSA", "sun.security.mscapi.CSignature$SHA512withECDSA");
        map.put("Alg.Alias.Signature.1.2.840.10045.4.3.4", "SHA512withECDSA");
        map.put("Alg.Alias.Signature.OID.1.2.840.10045.4.3.4", "SHA512withECDSA");
        map.put("Signature.NONEwithRSA SupportedKeyClasses", "sun.security.mscapi.CKey");
        map.put("Signature.SHA1withRSA SupportedKeyClasses", "sun.security.mscapi.CKey");
        map.put("Signature.SHA256withRSA SupportedKeyClasses", "sun.security.mscapi.CKey");
        map.put("Signature.SHA384withRSA SupportedKeyClasses", "sun.security.mscapi.CKey");
        map.put("Signature.SHA512withRSA SupportedKeyClasses", "sun.security.mscapi.CKey");
        map.put("Signature.MD5withRSA SupportedKeyClasses", "sun.security.mscapi.CKey");
        map.put("Signature.MD2withRSA SupportedKeyClasses", "sun.security.mscapi.CKey");
        map.put("Signature.RSASSA-PSS SupportedKeyClasses", "sun.security.mscapi.CKey");
        map.put("Signature.SHA1withECDSA SupportedKeyClasses", "sun.security.mscapi.CKey");
        map.put("Signature.SHA224withECDSA SupportedKeyClasses", "sun.security.mscapi.CKey");
        map.put("Signature.SHA256withECDSA SupportedKeyClasses", "sun.security.mscapi.CKey");
        map.put("Signature.SHA384withECDSA SupportedKeyClasses", "sun.security.mscapi.CKey");
        map.put("Signature.SHA512withECDSA SupportedKeyClasses", "sun.security.mscapi.CKey");
        map.put("KeyPairGenerator.RSA", "sun.security.mscapi.CKeyPairGenerator$RSA");
        map.put("KeyPairGenerator.RSA KeySize", "1024");
        map.put("Cipher.RSA", "sun.security.mscapi.CRSACipher");
        map.put("Cipher.RSA/ECB/PKCS1Padding", "sun.security.mscapi.CRSACipher");
        map.put("Cipher.RSA SupportedModes", "ECB");
        map.put("Cipher.RSA SupportedPaddings", "PKCS1PADDING");
        map.put("Cipher.RSA SupportedKeyClasses", "sun.security.mscapi.CKey");
        if (map != this) {
            AccessController.doPrivileged(new PutAllAction(this, map));
        }
    }
}
