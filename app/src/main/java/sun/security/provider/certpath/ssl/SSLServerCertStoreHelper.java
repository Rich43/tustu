package sun.security.provider.certpath.ssl;

import java.io.IOException;
import java.net.URI;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.X509CRLSelector;
import java.security.cert.X509CertSelector;
import java.util.Collection;
import javax.security.auth.x500.X500Principal;
import sun.security.provider.certpath.CertStoreHelper;

/* loaded from: rt.jar:sun/security/provider/certpath/ssl/SSLServerCertStoreHelper.class */
public final class SSLServerCertStoreHelper extends CertStoreHelper {
    @Override // sun.security.provider.certpath.CertStoreHelper
    public CertStore getCertStore(URI uri) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        return SSLServerCertStore.getInstance(uri);
    }

    @Override // sun.security.provider.certpath.CertStoreHelper
    public X509CertSelector wrap(X509CertSelector x509CertSelector, X500Principal x500Principal, String str) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override // sun.security.provider.certpath.CertStoreHelper
    public X509CRLSelector wrap(X509CRLSelector x509CRLSelector, Collection<X500Principal> collection, String str) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override // sun.security.provider.certpath.CertStoreHelper
    public boolean isCausedByNetworkIssue(CertStoreException certStoreException) {
        Throwable cause = certStoreException.getCause();
        return cause != null && (cause instanceof IOException);
    }
}
