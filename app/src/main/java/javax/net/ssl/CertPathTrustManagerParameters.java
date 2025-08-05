package javax.net.ssl;

import java.security.cert.CertPathParameters;

/* loaded from: rt.jar:javax/net/ssl/CertPathTrustManagerParameters.class */
public class CertPathTrustManagerParameters implements ManagerFactoryParameters {
    private final CertPathParameters parameters;

    public CertPathTrustManagerParameters(CertPathParameters certPathParameters) {
        this.parameters = (CertPathParameters) certPathParameters.clone();
    }

    public CertPathParameters getParameters() {
        return (CertPathParameters) this.parameters.clone();
    }
}
