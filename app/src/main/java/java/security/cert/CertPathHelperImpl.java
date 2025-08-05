package java.security.cert;

import java.util.Date;
import java.util.Set;
import sun.security.provider.certpath.CertPathHelper;
import sun.security.x509.GeneralNameInterface;

/* loaded from: rt.jar:java/security/cert/CertPathHelperImpl.class */
class CertPathHelperImpl extends CertPathHelper {
    private CertPathHelperImpl() {
    }

    static synchronized void initialize() {
        if (CertPathHelper.instance == null) {
            CertPathHelper.instance = new CertPathHelperImpl();
        }
    }

    @Override // sun.security.provider.certpath.CertPathHelper
    protected void implSetPathToNames(X509CertSelector x509CertSelector, Set<GeneralNameInterface> set) {
        x509CertSelector.setPathToNamesInternal(set);
    }

    @Override // sun.security.provider.certpath.CertPathHelper
    protected void implSetDateAndTime(X509CRLSelector x509CRLSelector, Date date, long j2) {
        x509CRLSelector.setDateAndTime(date, j2);
    }

    @Override // sun.security.provider.certpath.CertPathHelper
    protected boolean implIsJdkCA(TrustAnchor trustAnchor) {
        return trustAnchor.isJdkCA();
    }
}
