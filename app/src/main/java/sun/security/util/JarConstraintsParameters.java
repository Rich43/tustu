package sun.security.util;

import java.security.CodeSigner;
import java.security.Key;
import java.security.Timestamp;
import java.security.cert.CertPath;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import sun.security.validator.Validator;

/* loaded from: rt.jar:sun/security/util/JarConstraintsParameters.class */
public class JarConstraintsParameters implements ConstraintsParameters {
    private boolean anchorIsJdkCA;
    private boolean anchorIsJdkCASet;
    private Date timestamp;
    private final Set<Key> keys = new HashSet();
    private final Set<X509Certificate> certsIssuedByAnchor = new HashSet();
    private String message;

    public JarConstraintsParameters(CodeSigner[] codeSignerArr) {
        Date date = null;
        boolean z2 = false;
        for (CodeSigner codeSigner : codeSignerArr) {
            addToCertsAndKeys(codeSigner.getSignerCertPath());
            Timestamp timestamp = codeSigner.getTimestamp();
            if (timestamp == null) {
                date = null;
                z2 = true;
            } else {
                addToCertsAndKeys(timestamp.getSignerCertPath());
                if (!z2) {
                    Date timestamp2 = timestamp.getTimestamp();
                    if (date == null) {
                        date = timestamp2;
                    } else if (date.before(timestamp2)) {
                        date = timestamp2;
                    }
                }
            }
        }
        this.timestamp = date;
    }

    public JarConstraintsParameters(List<X509Certificate> list, Date date) {
        addToCertsAndKeys(list);
        this.timestamp = date;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void addToCertsAndKeys(CertPath certPath) {
        addToCertsAndKeys((List<X509Certificate>) certPath.getCertificates());
    }

    private void addToCertsAndKeys(List<X509Certificate> list) {
        if (!list.isEmpty()) {
            this.certsIssuedByAnchor.add(list.get(list.size() - 1));
            this.keys.add(list.get(0).getPublicKey());
        }
    }

    @Override // sun.security.util.ConstraintsParameters
    public String getVariant() {
        return Validator.VAR_GENERIC;
    }

    @Override // sun.security.util.ConstraintsParameters
    public boolean anchorIsJdkCA() {
        if (this.anchorIsJdkCASet) {
            return this.anchorIsJdkCA;
        }
        Iterator<X509Certificate> it = this.certsIssuedByAnchor.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            if (AnchorCertificates.issuerOf(it.next())) {
                this.anchorIsJdkCA = true;
                break;
            }
        }
        this.anchorIsJdkCASet = true;
        return this.anchorIsJdkCA;
    }

    @Override // sun.security.util.ConstraintsParameters
    public Date getDate() {
        return this.timestamp;
    }

    @Override // sun.security.util.ConstraintsParameters
    public Set<Key> getKeys() {
        return this.keys;
    }

    public void setExtendedExceptionMsg(String str, String str2) {
        this.message = " used" + (str2 != null ? " with " + str2 : "") + " in " + str + " file.";
    }

    @Override // sun.security.util.ConstraintsParameters
    public String extendedExceptionMsg() {
        return this.message == null ? "." : this.message;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[\n");
        sb.append("  Variant: ").append(getVariant());
        sb.append("\n  Certs Issued by Anchor:");
        for (X509Certificate x509Certificate : this.certsIssuedByAnchor) {
            sb.append("\n    Cert Issuer: ").append((Object) x509Certificate.getIssuerX500Principal());
            sb.append("\n    Cert Subject: ").append((Object) x509Certificate.getSubjectX500Principal());
        }
        Iterator<Key> it = this.keys.iterator();
        while (it.hasNext()) {
            sb.append("\n  Key: ").append(it.next().getAlgorithm());
        }
        if (this.timestamp != null) {
            sb.append("\n  Timestamp: ").append((Object) this.timestamp);
        }
        sb.append("\n]");
        return sb.toString();
    }
}
