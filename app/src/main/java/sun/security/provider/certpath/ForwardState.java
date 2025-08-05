package sun.security.provider.certpath;

import java.io.IOException;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertificateException;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.security.auth.x500.X500Principal;
import sun.security.util.Debug;
import sun.security.x509.GeneralName;
import sun.security.x509.GeneralNameInterface;
import sun.security.x509.SubjectAlternativeNameExtension;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertImpl;

/* loaded from: rt.jar:sun/security/provider/certpath/ForwardState.class */
class ForwardState implements State {
    private static final Debug debug = Debug.getInstance("certpath");
    X500Principal issuerDN;
    X509CertImpl cert;
    HashSet<GeneralNameInterface> subjectNamesTraversed;
    int traversedCACerts;
    private boolean init = true;
    UntrustedChecker untrustedChecker;
    ArrayList<PKIXCertPathChecker> forwardCheckers;
    boolean selfIssued;

    ForwardState() {
    }

    @Override // sun.security.provider.certpath.State
    public boolean isInitial() {
        return this.init;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("State [");
        sb.append("\n  issuerDN of last cert: ").append((Object) this.issuerDN);
        sb.append("\n  traversedCACerts: ").append(this.traversedCACerts);
        sb.append("\n  init: ").append(String.valueOf(this.init));
        sb.append("\n  subjectNamesTraversed: \n").append((Object) this.subjectNamesTraversed);
        sb.append("\n  selfIssued: ").append(String.valueOf(this.selfIssued));
        sb.append("]\n");
        return sb.toString();
    }

    public void initState(List<PKIXCertPathChecker> list) throws CertPathValidatorException {
        this.subjectNamesTraversed = new HashSet<>();
        this.traversedCACerts = 0;
        this.forwardCheckers = new ArrayList<>();
        for (PKIXCertPathChecker pKIXCertPathChecker : list) {
            if (pKIXCertPathChecker.isForwardCheckingSupported()) {
                pKIXCertPathChecker.init(true);
                this.forwardCheckers.add(pKIXCertPathChecker);
            }
        }
        this.init = true;
    }

    @Override // sun.security.provider.certpath.State
    public void updateState(X509Certificate x509Certificate) throws IOException, CertificateException, CertPathValidatorException {
        if (x509Certificate == null) {
            return;
        }
        X509CertImpl impl = X509CertImpl.toImpl(x509Certificate);
        this.cert = impl;
        this.issuerDN = x509Certificate.getIssuerX500Principal();
        this.selfIssued = X509CertImpl.isSelfIssued(x509Certificate);
        if (!this.selfIssued && !this.init && x509Certificate.getBasicConstraints() != -1) {
            this.traversedCACerts++;
        }
        if (this.init || !this.selfIssued) {
            this.subjectNamesTraversed.add(X500Name.asX500Name(x509Certificate.getSubjectX500Principal()));
            try {
                SubjectAlternativeNameExtension subjectAlternativeNameExtension = impl.getSubjectAlternativeNameExtension();
                if (subjectAlternativeNameExtension != null) {
                    Iterator<GeneralName> it = subjectAlternativeNameExtension.get(SubjectAlternativeNameExtension.SUBJECT_NAME).names().iterator();
                    while (it.hasNext()) {
                        this.subjectNamesTraversed.add(it.next().getName());
                    }
                }
            } catch (IOException e2) {
                if (debug != null) {
                    debug.println("ForwardState.updateState() unexpected exception");
                    e2.printStackTrace();
                }
                throw new CertPathValidatorException(e2);
            }
        }
        this.init = false;
    }

    @Override // sun.security.provider.certpath.State
    public Object clone() {
        try {
            ForwardState forwardState = (ForwardState) super.clone();
            forwardState.forwardCheckers = (ArrayList) this.forwardCheckers.clone();
            ListIterator<PKIXCertPathChecker> listIterator = forwardState.forwardCheckers.listIterator();
            while (listIterator.hasNext()) {
                PKIXCertPathChecker next = listIterator.next();
                if (next instanceof Cloneable) {
                    listIterator.set((PKIXCertPathChecker) next.clone());
                }
            }
            forwardState.subjectNamesTraversed = (HashSet) this.subjectNamesTraversed.clone();
            return forwardState;
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2.toString(), e2);
        }
    }
}
