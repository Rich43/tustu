package java.security.cert;

import java.security.PublicKey;

/* loaded from: rt.jar:java/security/cert/PKIXCertPathBuilderResult.class */
public class PKIXCertPathBuilderResult extends PKIXCertPathValidatorResult implements CertPathBuilderResult {
    private CertPath certPath;

    public PKIXCertPathBuilderResult(CertPath certPath, TrustAnchor trustAnchor, PolicyNode policyNode, PublicKey publicKey) {
        super(trustAnchor, policyNode, publicKey);
        if (certPath == null) {
            throw new NullPointerException("certPath must be non-null");
        }
        this.certPath = certPath;
    }

    @Override // java.security.cert.CertPathBuilderResult
    public CertPath getCertPath() {
        return this.certPath;
    }

    @Override // java.security.cert.PKIXCertPathValidatorResult
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("PKIXCertPathBuilderResult: [\n");
        stringBuffer.append("  Certification Path: " + ((Object) this.certPath) + "\n");
        stringBuffer.append("  Trust Anchor: " + getTrustAnchor().toString() + "\n");
        stringBuffer.append("  Policy Tree: " + String.valueOf(getPolicyTree()) + "\n");
        stringBuffer.append("  Subject Public Key: " + ((Object) getPublicKey()) + "\n");
        stringBuffer.append("]");
        return stringBuffer.toString();
    }
}
