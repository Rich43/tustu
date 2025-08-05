package sun.security.provider.certpath;

import java.security.cert.TrustAnchor;
import java.security.cert.X509CRLSelector;
import java.security.cert.X509CertSelector;
import java.util.Date;
import java.util.Set;
import sun.security.x509.GeneralNameInterface;

/* loaded from: rt.jar:sun/security/provider/certpath/CertPathHelper.class */
public abstract class CertPathHelper {
    protected static CertPathHelper instance;

    protected abstract void implSetPathToNames(X509CertSelector x509CertSelector, Set<GeneralNameInterface> set);

    protected abstract void implSetDateAndTime(X509CRLSelector x509CRLSelector, Date date, long j2);

    protected abstract boolean implIsJdkCA(TrustAnchor trustAnchor);

    protected CertPathHelper() {
    }

    static void setPathToNames(X509CertSelector x509CertSelector, Set<GeneralNameInterface> set) {
        instance.implSetPathToNames(x509CertSelector, set);
    }

    public static void setDateAndTime(X509CRLSelector x509CRLSelector, Date date, long j2) {
        instance.implSetDateAndTime(x509CRLSelector, date, j2);
    }

    public static boolean isJdkCA(TrustAnchor trustAnchor) {
        if (trustAnchor == null) {
            return false;
        }
        return instance.implIsJdkCA(trustAnchor);
    }
}
