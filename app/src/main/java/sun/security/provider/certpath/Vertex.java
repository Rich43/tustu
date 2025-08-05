package sun.security.provider.certpath;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javafx.fxml.FXMLLoader;
import sun.security.util.Debug;
import sun.security.x509.AuthorityKeyIdentifierExtension;
import sun.security.x509.KeyIdentifier;
import sun.security.x509.SubjectKeyIdentifierExtension;
import sun.security.x509.X509CertImpl;

/* loaded from: rt.jar:sun/security/provider/certpath/Vertex.class */
public class Vertex {
    private static final Debug debug = Debug.getInstance("certpath");
    private X509Certificate cert;
    private int index = -1;
    private Throwable throwable;

    Vertex(X509Certificate x509Certificate) {
        this.cert = x509Certificate;
    }

    public X509Certificate getCertificate() {
        return this.cert;
    }

    public int getIndex() {
        return this.index;
    }

    void setIndex(int i2) {
        this.index = i2;
    }

    public Throwable getThrowable() {
        return this.throwable;
    }

    void setThrowable(Throwable th) {
        this.throwable = th;
    }

    public String toString() {
        return certToString() + throwableToString() + indexToString();
    }

    public String certToString() {
        StringBuilder sb = new StringBuilder();
        try {
            X509CertImpl impl = X509CertImpl.toImpl(this.cert);
            sb.append("Issuer:     ").append((Object) impl.getIssuerX500Principal()).append("\n");
            sb.append("Subject:    ").append((Object) impl.getSubjectX500Principal()).append("\n");
            sb.append("SerialNum:  ").append(impl.getSerialNumber().toString(16)).append("\n");
            sb.append("Expires:    ").append(impl.getNotAfter().toString()).append("\n");
            boolean[] issuerUniqueID = impl.getIssuerUniqueID();
            if (issuerUniqueID != null) {
                sb.append("IssuerUID:  ");
                for (boolean z2 : issuerUniqueID) {
                    sb.append(z2 ? 1 : 0);
                }
                sb.append("\n");
            }
            boolean[] subjectUniqueID = impl.getSubjectUniqueID();
            if (subjectUniqueID != null) {
                sb.append("SubjectUID: ");
                for (boolean z3 : subjectUniqueID) {
                    sb.append(z3 ? 1 : 0);
                }
                sb.append("\n");
            }
            try {
                SubjectKeyIdentifierExtension subjectKeyIdentifierExtension = impl.getSubjectKeyIdentifierExtension();
                if (subjectKeyIdentifierExtension != null) {
                    sb.append("SubjKeyID:  ").append(subjectKeyIdentifierExtension.get("key_id").toString());
                }
                AuthorityKeyIdentifierExtension authorityKeyIdentifierExtension = impl.getAuthorityKeyIdentifierExtension();
                if (authorityKeyIdentifierExtension != null) {
                    sb.append("AuthKeyID:  ").append(((KeyIdentifier) authorityKeyIdentifierExtension.get("key_id")).toString());
                }
            } catch (IOException e2) {
                if (debug != null) {
                    debug.println("Vertex.certToString() unexpected exception");
                    e2.printStackTrace();
                }
            }
            return sb.toString();
        } catch (CertificateException e3) {
            if (debug != null) {
                debug.println("Vertex.certToString() unexpected exception");
                e3.printStackTrace();
            }
            return sb.toString();
        }
    }

    public String throwableToString() {
        StringBuilder sb = new StringBuilder("Exception:  ");
        if (this.throwable != null) {
            sb.append(this.throwable.toString());
        } else {
            sb.append(FXMLLoader.NULL_KEYWORD);
        }
        sb.append("\n");
        return sb.toString();
    }

    public String moreToString() {
        StringBuilder sb = new StringBuilder("Last cert?  ");
        sb.append(this.index == -1 ? "Yes" : "No");
        sb.append("\n");
        return sb.toString();
    }

    public String indexToString() {
        return "Index:      " + this.index + "\n";
    }
}
