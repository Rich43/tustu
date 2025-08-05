package java.security.cert;

import java.security.PublicKey;

/* loaded from: rt.jar:java/security/cert/PKIXCertPathValidatorResult.class */
public class PKIXCertPathValidatorResult implements CertPathValidatorResult {
    private TrustAnchor trustAnchor;
    private PolicyNode policyTree;
    private PublicKey subjectPublicKey;

    public PKIXCertPathValidatorResult(TrustAnchor trustAnchor, PolicyNode policyNode, PublicKey publicKey) {
        if (publicKey == null) {
            throw new NullPointerException("subjectPublicKey must be non-null");
        }
        if (trustAnchor == null) {
            throw new NullPointerException("trustAnchor must be non-null");
        }
        this.trustAnchor = trustAnchor;
        this.policyTree = policyNode;
        this.subjectPublicKey = publicKey;
    }

    public TrustAnchor getTrustAnchor() {
        return this.trustAnchor;
    }

    public PolicyNode getPolicyTree() {
        return this.policyTree;
    }

    public PublicKey getPublicKey() {
        return this.subjectPublicKey;
    }

    @Override // java.security.cert.CertPathValidatorResult
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2.toString(), e2);
        }
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("PKIXCertPathValidatorResult: [\n");
        stringBuffer.append("  Trust Anchor: " + this.trustAnchor.toString() + "\n");
        stringBuffer.append("  Policy Tree: " + String.valueOf(this.policyTree) + "\n");
        stringBuffer.append("  Subject Public Key: " + ((Object) this.subjectPublicKey) + "\n");
        stringBuffer.append("]");
        return stringBuffer.toString();
    }
}
