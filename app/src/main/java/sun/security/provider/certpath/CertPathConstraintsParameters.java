package sun.security.provider.certpath;

import java.security.Key;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import sun.security.util.ConstraintsParameters;
import sun.security.validator.Validator;

/* loaded from: rt.jar:sun/security/provider/certpath/CertPathConstraintsParameters.class */
public class CertPathConstraintsParameters implements ConstraintsParameters {
    private final Key key;
    private final TrustAnchor anchor;
    private final Date date;
    private final String variant;
    private final X509Certificate cert;

    public CertPathConstraintsParameters(X509Certificate x509Certificate, String str, TrustAnchor trustAnchor, Date date) {
        this(x509Certificate.getPublicKey(), str, trustAnchor, date, x509Certificate);
    }

    public CertPathConstraintsParameters(Key key, String str, TrustAnchor trustAnchor) {
        this(key, str, trustAnchor, null, null);
    }

    private CertPathConstraintsParameters(Key key, String str, TrustAnchor trustAnchor, Date date, X509Certificate x509Certificate) {
        this.key = key;
        this.variant = str == null ? Validator.VAR_GENERIC : str;
        this.anchor = trustAnchor;
        this.date = date;
        this.cert = x509Certificate;
    }

    @Override // sun.security.util.ConstraintsParameters
    public boolean anchorIsJdkCA() {
        return CertPathHelper.isJdkCA(this.anchor);
    }

    @Override // sun.security.util.ConstraintsParameters
    public Set<Key> getKeys() {
        return this.key == null ? Collections.emptySet() : Collections.singleton(this.key);
    }

    @Override // sun.security.util.ConstraintsParameters
    public Date getDate() {
        return this.date;
    }

    @Override // sun.security.util.ConstraintsParameters
    public String getVariant() {
        return this.variant;
    }

    @Override // sun.security.util.ConstraintsParameters
    public String extendedExceptionMsg() {
        return this.cert == null ? "." : " used with certificate: " + ((Object) this.cert.getSubjectX500Principal());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[\n");
        sb.append("  Variant: ").append(this.variant);
        if (this.anchor != null) {
            sb.append("\n  Anchor: ").append((Object) this.anchor);
        }
        if (this.cert != null) {
            sb.append("\n  Cert Issuer: ").append((Object) this.cert.getIssuerX500Principal());
            sb.append("\n  Cert Subject: ").append((Object) this.cert.getSubjectX500Principal());
        }
        if (this.key != null) {
            sb.append("\n  Key: ").append(this.key.getAlgorithm());
        }
        if (this.date != null) {
            sb.append("\n  Date: ").append((Object) this.date);
        }
        sb.append("\n]");
        return sb.toString();
    }
}
