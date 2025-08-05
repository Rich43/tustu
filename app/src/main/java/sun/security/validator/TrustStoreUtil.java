package sun.security.validator;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/* loaded from: rt.jar:sun/security/validator/TrustStoreUtil.class */
public final class TrustStoreUtil {
    private TrustStoreUtil() {
    }

    public static Set<X509Certificate> getTrustedCerts(KeyStore keyStore) {
        Certificate[] certificateChain;
        HashSet hashSet = new HashSet();
        try {
            Enumeration<String> enumerationAliases = keyStore.aliases();
            while (enumerationAliases.hasMoreElements()) {
                String strNextElement2 = enumerationAliases.nextElement2();
                if (keyStore.isCertificateEntry(strNextElement2)) {
                    Certificate certificate = keyStore.getCertificate(strNextElement2);
                    if (certificate instanceof X509Certificate) {
                        hashSet.add((X509Certificate) certificate);
                    }
                } else if (keyStore.isKeyEntry(strNextElement2) && (certificateChain = keyStore.getCertificateChain(strNextElement2)) != null && certificateChain.length > 0 && (certificateChain[0] instanceof X509Certificate)) {
                    hashSet.add((X509Certificate) certificateChain[0]);
                }
            }
        } catch (KeyStoreException e2) {
        }
        return Collections.unmodifiableSet(hashSet);
    }
}
