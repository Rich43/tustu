package java.security.cert;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.Set;

/* loaded from: rt.jar:java/security/cert/PKIXBuilderParameters.class */
public class PKIXBuilderParameters extends PKIXParameters {
    private int maxPathLength;

    public PKIXBuilderParameters(Set<TrustAnchor> set, CertSelector certSelector) throws InvalidAlgorithmParameterException {
        super(set);
        this.maxPathLength = 5;
        setTargetCertConstraints(certSelector);
    }

    public PKIXBuilderParameters(KeyStore keyStore, CertSelector certSelector) throws KeyStoreException, InvalidAlgorithmParameterException {
        super(keyStore);
        this.maxPathLength = 5;
        setTargetCertConstraints(certSelector);
    }

    public void setMaxPathLength(int i2) {
        if (i2 < -1) {
            throw new InvalidParameterException("the maximum path length parameter can not be less than -1");
        }
        this.maxPathLength = i2;
    }

    public int getMaxPathLength() {
        return this.maxPathLength;
    }

    @Override // java.security.cert.PKIXParameters
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[\n");
        stringBuffer.append(super.toString());
        stringBuffer.append("  Maximum Path Length: " + this.maxPathLength + "\n");
        stringBuffer.append("]\n");
        return stringBuffer.toString();
    }
}
