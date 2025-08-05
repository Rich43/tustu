package sun.security.provider.certpath.ldap;

import java.io.IOException;
import java.net.URI;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.X509CRLSelector;
import java.security.cert.X509CertSelector;
import java.util.Collection;
import javax.naming.CommunicationException;
import javax.naming.ServiceUnavailableException;
import javax.security.auth.x500.X500Principal;
import sun.security.provider.certpath.CertStoreHelper;
import sun.security.provider.certpath.ldap.LDAPCertStore;

/* loaded from: rt.jar:sun/security/provider/certpath/ldap/LDAPCertStoreHelper.class */
public final class LDAPCertStoreHelper extends CertStoreHelper {
    @Override // sun.security.provider.certpath.CertStoreHelper
    public CertStore getCertStore(URI uri) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        return LDAPCertStore.getInstance(LDAPCertStore.getParameters(uri));
    }

    @Override // sun.security.provider.certpath.CertStoreHelper
    public X509CertSelector wrap(X509CertSelector x509CertSelector, X500Principal x500Principal, String str) throws IOException {
        return new LDAPCertStore.LDAPCertSelector(x509CertSelector, x500Principal, str);
    }

    @Override // sun.security.provider.certpath.CertStoreHelper
    public X509CRLSelector wrap(X509CRLSelector x509CRLSelector, Collection<X500Principal> collection, String str) throws IOException {
        return new LDAPCertStore.LDAPCRLSelector(x509CRLSelector, collection, str);
    }

    @Override // sun.security.provider.certpath.CertStoreHelper
    public boolean isCausedByNetworkIssue(CertStoreException certStoreException) {
        Throwable cause = certStoreException.getCause();
        return cause != null && ((cause instanceof ServiceUnavailableException) || (cause instanceof CommunicationException));
    }
}
