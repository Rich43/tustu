package sun.security.provider.certpath;

import java.io.IOException;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/* loaded from: rt.jar:sun/security/provider/certpath/State.class */
interface State extends Cloneable {
    void updateState(X509Certificate x509Certificate) throws IOException, CertificateException, CertPathValidatorException;

    Object clone();

    boolean isInitial();
}
