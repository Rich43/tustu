package sun.security.provider.certpath;

import java.io.IOException;
import java.security.AccessController;
import java.security.GeneralSecurityException;
import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import sun.security.action.GetBooleanAction;
import sun.security.provider.certpath.PKIX;
import sun.security.util.Debug;
import sun.security.x509.GeneralNameInterface;
import sun.security.x509.GeneralNames;
import sun.security.x509.GeneralSubtrees;
import sun.security.x509.NameConstraintsExtension;
import sun.security.x509.SubjectAlternativeNameExtension;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertImpl;

/* loaded from: rt.jar:sun/security/provider/certpath/Builder.class */
public abstract class Builder {
    private Set<String> matchingPolicies;
    final PKIX.BuilderParams buildParams;
    final X509CertSelector targetCertConstraints;
    private static final Debug debug = Debug.getInstance("certpath");
    static final boolean USE_AIA = ((Boolean) AccessController.doPrivileged(new GetBooleanAction("com.sun.security.enableAIAcaIssuers"))).booleanValue();

    abstract Collection<X509Certificate> getMatchingCerts(State state, List<CertStore> list) throws IOException, CertificateException, CertStoreException;

    abstract void verifyCert(X509Certificate x509Certificate, State state, List<X509Certificate> list) throws GeneralSecurityException;

    abstract boolean isPathCompleted(X509Certificate x509Certificate);

    abstract void addCertToPath(X509Certificate x509Certificate, LinkedList<X509Certificate> linkedList);

    abstract void removeFinalCertFromPath(LinkedList<X509Certificate> linkedList);

    Builder(PKIX.BuilderParams builderParams) {
        this.buildParams = builderParams;
        this.targetCertConstraints = (X509CertSelector) builderParams.targetCertConstraints();
    }

    static int distance(GeneralNameInterface generalNameInterface, GeneralNameInterface generalNameInterface2, int i2) {
        switch (generalNameInterface.constrains(generalNameInterface2)) {
            case -1:
                if (debug != null) {
                    debug.println("Builder.distance(): Names are different types");
                }
                break;
            case 3:
                if (debug != null) {
                    debug.println("Builder.distance(): Names are same type but in different subtrees");
                }
                break;
        }
        return i2;
    }

    static int hops(GeneralNameInterface generalNameInterface, GeneralNameInterface generalNameInterface2, int i2) throws UnsupportedOperationException {
        switch (generalNameInterface.constrains(generalNameInterface2)) {
            case -1:
                if (debug != null) {
                    debug.println("Builder.hops(): Names are different types");
                }
                return i2;
            case 0:
                return 0;
            case 1:
                return generalNameInterface2.subtreeDepth() - generalNameInterface.subtreeDepth();
            case 2:
                return generalNameInterface2.subtreeDepth() - generalNameInterface.subtreeDepth();
            case 3:
                if (generalNameInterface.getType() != 4) {
                    if (debug != null) {
                        debug.println("Builder.hops(): hopDistance not implemented for this name type");
                    }
                    return i2;
                }
                X500Name x500Name = (X500Name) generalNameInterface;
                X500Name x500Name2 = (X500Name) generalNameInterface2;
                X500Name x500NameCommonAncestor = x500Name.commonAncestor(x500Name2);
                if (x500NameCommonAncestor == null) {
                    if (debug != null) {
                        debug.println("Builder.hops(): Names are in different namespaces");
                    }
                    return i2;
                }
                return (x500Name.subtreeDepth() + x500Name2.subtreeDepth()) - (2 * x500NameCommonAncestor.subtreeDepth());
            default:
                return i2;
        }
    }

    static int targetDistance(NameConstraintsExtension nameConstraintsExtension, X509Certificate x509Certificate, GeneralNameInterface generalNameInterface) throws IOException {
        GeneralNames generalNames;
        if (nameConstraintsExtension != null && !nameConstraintsExtension.verify(x509Certificate)) {
            throw new IOException("certificate does not satisfy existing name constraints");
        }
        try {
            X509CertImpl impl = X509CertImpl.toImpl(x509Certificate);
            if (X500Name.asX500Name(impl.getSubjectX500Principal()).equals(generalNameInterface)) {
                return 0;
            }
            SubjectAlternativeNameExtension subjectAlternativeNameExtension = impl.getSubjectAlternativeNameExtension();
            if (subjectAlternativeNameExtension != null && (generalNames = subjectAlternativeNameExtension.get(SubjectAlternativeNameExtension.SUBJECT_NAME)) != null) {
                int size = generalNames.size();
                for (int i2 = 0; i2 < size; i2++) {
                    if (generalNames.get(i2).getName().equals(generalNameInterface)) {
                        return 0;
                    }
                }
            }
            NameConstraintsExtension nameConstraintsExtension2 = impl.getNameConstraintsExtension();
            if (nameConstraintsExtension2 == null) {
                return -1;
            }
            if (nameConstraintsExtension != null) {
                nameConstraintsExtension.merge(nameConstraintsExtension2);
            } else {
                nameConstraintsExtension = (NameConstraintsExtension) nameConstraintsExtension2.clone();
            }
            if (debug != null) {
                debug.println("Builder.targetDistance() merged constraints: " + String.valueOf(nameConstraintsExtension));
            }
            GeneralSubtrees generalSubtrees = nameConstraintsExtension.get(NameConstraintsExtension.PERMITTED_SUBTREES);
            GeneralSubtrees generalSubtrees2 = nameConstraintsExtension.get(NameConstraintsExtension.EXCLUDED_SUBTREES);
            if (generalSubtrees != null) {
                generalSubtrees.reduce(generalSubtrees2);
            }
            if (debug != null) {
                debug.println("Builder.targetDistance() reduced constraints: " + ((Object) generalSubtrees));
            }
            if (!nameConstraintsExtension.verify(generalNameInterface)) {
                throw new IOException("New certificate not allowed to sign certificate for target");
            }
            if (generalSubtrees == null) {
                return -1;
            }
            int size2 = generalSubtrees.size();
            for (int i3 = 0; i3 < size2; i3++) {
                int iDistance = distance(generalSubtrees.get(i3).getName().getName(), generalNameInterface, -1);
                if (iDistance >= 0) {
                    return iDistance + 1;
                }
            }
            return -1;
        } catch (CertificateException e2) {
            throw new IOException("Invalid certificate", e2);
        }
    }

    Set<String> getMatchingPolicies() {
        if (this.matchingPolicies != null) {
            Set<String> setInitialPolicies = this.buildParams.initialPolicies();
            if (!setInitialPolicies.isEmpty() && !setInitialPolicies.contains("2.5.29.32.0") && this.buildParams.policyMappingInhibited()) {
                this.matchingPolicies = new HashSet(setInitialPolicies);
                this.matchingPolicies.add("2.5.29.32.0");
            } else {
                this.matchingPolicies = Collections.emptySet();
            }
        }
        return this.matchingPolicies;
    }

    boolean addMatchingCerts(X509CertSelector x509CertSelector, Collection<CertStore> collection, Collection<X509Certificate> collection2, boolean z2) {
        X509Certificate certificate = x509CertSelector.getCertificate();
        if (certificate != null) {
            if (x509CertSelector.match(certificate)) {
                if (debug != null) {
                    debug.println("Builder.addMatchingCerts: adding target cert\n  SN: " + Debug.toHexString(certificate.getSerialNumber()) + "\n  Subject: " + ((Object) certificate.getSubjectX500Principal()) + "\n  Issuer: " + ((Object) certificate.getIssuerX500Principal()));
                }
                return collection2.add(certificate);
            }
            return false;
        }
        boolean z3 = false;
        Iterator<CertStore> it = collection.iterator();
        while (it.hasNext()) {
            try {
                Iterator<? extends Certificate> it2 = it.next().getCertificates(x509CertSelector).iterator();
                while (it2.hasNext()) {
                    if (collection2.add((X509Certificate) it2.next())) {
                        z3 = true;
                    }
                }
            } catch (CertStoreException e2) {
                if (debug != null) {
                    debug.println("Builder.addMatchingCerts, non-fatal exception retrieving certs: " + ((Object) e2));
                    e2.printStackTrace();
                }
            }
            if (!z2 && z3) {
                return true;
            }
        }
        return z3;
    }
}
