package sun.security.provider.certpath;

import java.io.IOException;
import java.net.URI;
import java.security.AccessController;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.X509CRLSelector;
import java.security.cert.X509CertSelector;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.security.auth.x500.X500Principal;
import sun.security.util.Cache;

/* loaded from: rt.jar:sun/security/provider/certpath/CertStoreHelper.class */
public abstract class CertStoreHelper {
    private static final int NUM_TYPES = 2;
    private static final Map<String, String> classMap = new HashMap(2);
    private static Cache<String, CertStoreHelper> cache;

    public abstract CertStore getCertStore(URI uri) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException;

    public abstract X509CertSelector wrap(X509CertSelector x509CertSelector, X500Principal x500Principal, String str) throws IOException;

    public abstract X509CRLSelector wrap(X509CRLSelector x509CRLSelector, Collection<X500Principal> collection, String str) throws IOException;

    public abstract boolean isCausedByNetworkIssue(CertStoreException certStoreException);

    static {
        classMap.put("LDAP", "sun.security.provider.certpath.ldap.LDAPCertStoreHelper");
        classMap.put("SSLServer", "sun.security.provider.certpath.ssl.SSLServerCertStoreHelper");
        cache = Cache.newSoftMemoryCache(2);
    }

    public static CertStoreHelper getInstance(final String str) throws NoSuchAlgorithmException {
        CertStoreHelper certStoreHelper = cache.get(str);
        if (certStoreHelper != null) {
            return certStoreHelper;
        }
        final String str2 = classMap.get(str);
        if (str2 == null) {
            throw new NoSuchAlgorithmException(str + " not available");
        }
        try {
            return (CertStoreHelper) AccessController.doPrivileged(new PrivilegedExceptionAction<CertStoreHelper>() { // from class: sun.security.provider.certpath.CertStoreHelper.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public CertStoreHelper run() throws ClassNotFoundException {
                    try {
                        CertStoreHelper certStoreHelper2 = (CertStoreHelper) Class.forName(str2, true, null).newInstance();
                        CertStoreHelper.cache.put(str, certStoreHelper2);
                        return certStoreHelper2;
                    } catch (IllegalAccessException | InstantiationException e2) {
                        throw new AssertionError(e2);
                    }
                }
            });
        } catch (PrivilegedActionException e2) {
            throw new NoSuchAlgorithmException(str + " not available", e2.getException());
        }
    }

    static boolean isCausedByNetworkIssue(String str, CertStoreException certStoreException) {
        switch (str) {
            case "LDAP":
            case "SSLServer":
                try {
                    return getInstance(str).isCausedByNetworkIssue(certStoreException);
                } catch (NoSuchAlgorithmException e2) {
                    return false;
                }
            case "URI":
                Throwable cause = certStoreException.getCause();
                return cause != null && (cause instanceof IOException);
            default:
                return false;
        }
    }
}
