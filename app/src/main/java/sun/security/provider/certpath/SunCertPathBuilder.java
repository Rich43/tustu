package sun.security.provider.certpath;

import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Certificate;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.PublicKey;
import java.security.cert.CertPathBuilderException;
import java.security.cert.CertPathBuilderResult;
import java.security.cert.CertPathBuilderSpi;
import java.security.cert.CertPathChecker;
import java.security.cert.CertPathParameters;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertSelector;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.PKIXReason;
import java.security.cert.PKIXRevocationChecker;
import java.security.cert.PolicyNode;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import sun.security.provider.certpath.PKIX;
import sun.security.util.Debug;
import sun.security.x509.PKIXExtensions;
import sun.security.x509.X509CertImpl;

/* loaded from: rt.jar:sun/security/provider/certpath/SunCertPathBuilder.class */
public final class SunCertPathBuilder extends CertPathBuilderSpi {
    private static final Debug debug = Debug.getInstance("certpath");
    private PKIX.BuilderParams buildParams;
    private CertificateFactory cf;
    private boolean pathCompleted = false;
    private PolicyNode policyTreeResult;
    private TrustAnchor trustAnchor;
    private PublicKey finalPublicKey;

    public SunCertPathBuilder() throws CertPathBuilderException {
        try {
            this.cf = CertificateFactory.getInstance(XMLX509Certificate.JCA_CERT_ID);
        } catch (CertificateException e2) {
            throw new CertPathBuilderException(e2);
        }
    }

    @Override // java.security.cert.CertPathBuilderSpi
    public CertPathChecker engineGetRevocationChecker() {
        return new RevocationChecker();
    }

    @Override // java.security.cert.CertPathBuilderSpi
    public CertPathBuilderResult engineBuild(CertPathParameters certPathParameters) throws CertPathBuilderException, InvalidAlgorithmParameterException {
        if (debug != null) {
            debug.println("SunCertPathBuilder.engineBuild(" + ((Object) certPathParameters) + ")");
        }
        this.buildParams = PKIX.checkBuilderParams(certPathParameters);
        return build();
    }

    private PKIXCertPathBuilderResult build() throws CertPathBuilderException {
        ArrayList arrayList = new ArrayList();
        PKIXCertPathBuilderResult pKIXCertPathBuilderResultBuildCertPath = buildCertPath(false, arrayList);
        if (pKIXCertPathBuilderResultBuildCertPath == null) {
            if (this.buildParams.certStores().size() > 1 || Builder.USE_AIA) {
                if (debug != null) {
                    debug.println("SunCertPathBuilder.engineBuild: 2nd pass; try building again searching all certstores");
                }
                arrayList.clear();
                PKIXCertPathBuilderResult pKIXCertPathBuilderResultBuildCertPath2 = buildCertPath(true, arrayList);
                if (pKIXCertPathBuilderResultBuildCertPath2 != null) {
                    return pKIXCertPathBuilderResultBuildCertPath2;
                }
            }
            throw new SunCertPathBuilderException("unable to find valid certification path to requested target", new AdjacencyList(arrayList));
        }
        return pKIXCertPathBuilderResultBuildCertPath;
    }

    private PKIXCertPathBuilderResult buildCertPath(boolean z2, List<List<Vertex>> list) throws CertPathBuilderException {
        this.pathCompleted = false;
        this.trustAnchor = null;
        this.finalPublicKey = null;
        this.policyTreeResult = null;
        LinkedList<X509Certificate> linkedList = new LinkedList<>();
        try {
            buildForward(list, linkedList, z2);
            try {
                if (this.pathCompleted) {
                    if (debug != null) {
                        debug.println("SunCertPathBuilder.engineBuild() pathCompleted");
                    }
                    Collections.reverse(linkedList);
                    return new SunCertPathBuilderResult(this.cf.generateCertPath(linkedList), this.trustAnchor, this.policyTreeResult, this.finalPublicKey, new AdjacencyList(list));
                }
                return null;
            } catch (CertificateException e2) {
                if (debug != null) {
                    debug.println("SunCertPathBuilder.engineBuild() exception in wrap-up");
                    e2.printStackTrace();
                }
                throw new SunCertPathBuilderException("unable to find valid certification path to requested target", e2, new AdjacencyList(list));
            }
        } catch (IOException | GeneralSecurityException e3) {
            if (debug != null) {
                debug.println("SunCertPathBuilder.engineBuild() exception in build");
                e3.printStackTrace();
            }
            throw new SunCertPathBuilderException("unable to find valid certification path to requested target", e3, new AdjacencyList(list));
        }
    }

    private void buildForward(List<List<Vertex>> list, LinkedList<X509Certificate> linkedList, boolean z2) throws GeneralSecurityException, IOException {
        if (debug != null) {
            debug.println("SunCertPathBuilder.buildForward()...");
        }
        ForwardState forwardState = new ForwardState();
        forwardState.initState(this.buildParams.certPathCheckers());
        list.clear();
        list.add(new LinkedList());
        forwardState.untrustedChecker = new UntrustedChecker();
        depthFirstSearchForward(this.buildParams.targetSubject(), forwardState, new ForwardBuilder(this.buildParams, z2), list, linkedList);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void depthFirstSearchForward(X500Principal x500Principal, ForwardState forwardState, ForwardBuilder forwardBuilder, List<List<Vertex>> list, LinkedList<X509Certificate> linkedList) throws GeneralSecurityException, IOException {
        X509Certificate last;
        Set<String> supportedExtensions;
        if (debug != null) {
            debug.println("SunCertPathBuilder.depthFirstSearchForward(" + ((Object) x500Principal) + ", " + forwardState.toString() + ")");
        }
        List<Vertex> listAddVertices = addVertices(forwardBuilder.getMatchingCerts(forwardState, this.buildParams.certStores()), list);
        if (debug != null) {
            debug.println("SunCertPathBuilder.depthFirstSearchForward(): certs.size=" + listAddVertices.size());
        }
        for (Vertex vertex : listAddVertices) {
            ForwardState forwardState2 = (ForwardState) forwardState.clone();
            X509Certificate certificate = vertex.getCertificate();
            try {
                forwardBuilder.verifyCert(certificate, forwardState2, linkedList);
                if (forwardBuilder.isPathCompleted(certificate)) {
                    if (debug != null) {
                        debug.println("SunCertPathBuilder.depthFirstSearchForward(): commencing final verification");
                    }
                    ArrayList arrayList = new ArrayList(linkedList);
                    if (forwardBuilder.trustAnchor.getTrustedCert() == null) {
                        arrayList.add(0, certificate);
                    }
                    PolicyNodeImpl policyNodeImpl = new PolicyNodeImpl(null, "2.5.29.32.0", null, false, Collections.singleton("2.5.29.32.0"), false);
                    ArrayList<PKIXCertPathChecker> arrayList2 = new ArrayList();
                    PolicyChecker policyChecker = new PolicyChecker(this.buildParams.initialPolicies(), arrayList.size(), this.buildParams.explicitPolicyRequired(), this.buildParams.policyMappingInhibited(), this.buildParams.anyPolicyInhibited(), this.buildParams.policyQualifiersRejected(), policyNodeImpl);
                    arrayList2.add(policyChecker);
                    arrayList2.add(new AlgorithmChecker(forwardBuilder.trustAnchor, this.buildParams.timestamp(), this.buildParams.variant()));
                    PublicKey publicKey = certificate.getPublicKey();
                    if (forwardBuilder.trustAnchor.getTrustedCert() == null) {
                        publicKey = forwardBuilder.trustAnchor.getCAPublicKey();
                        if (debug != null) {
                            debug.println("SunCertPathBuilder.depthFirstSearchForward using buildParams public key: " + publicKey.toString());
                        }
                    }
                    BasicChecker basicChecker = new BasicChecker(new TrustAnchor(certificate.getSubjectX500Principal(), publicKey, (byte[]) null), this.buildParams.date(), this.buildParams.sigProvider(), true);
                    arrayList2.add(basicChecker);
                    this.buildParams.setCertPath(this.cf.generateCertPath(arrayList));
                    boolean z2 = false;
                    List<PKIXCertPathChecker> listCertPathCheckers = this.buildParams.certPathCheckers();
                    for (PKIXCertPathChecker pKIXCertPathChecker : listCertPathCheckers) {
                        if (pKIXCertPathChecker instanceof PKIXRevocationChecker) {
                            if (z2) {
                                throw new CertPathValidatorException("Only one PKIXRevocationChecker can be specified");
                            }
                            z2 = true;
                            if (pKIXCertPathChecker instanceof RevocationChecker) {
                                ((RevocationChecker) pKIXCertPathChecker).init(forwardBuilder.trustAnchor, this.buildParams);
                            }
                        }
                    }
                    if (this.buildParams.revocationEnabled() && !z2) {
                        arrayList2.add(new RevocationChecker(forwardBuilder.trustAnchor, this.buildParams));
                    }
                    arrayList2.addAll(listCertPathCheckers);
                    for (int i2 = 0; i2 < arrayList.size(); i2++) {
                        X509Certificate x509Certificate = (X509Certificate) arrayList.get(i2);
                        if (debug != null) {
                            debug.println("current subject = " + ((Object) x509Certificate.getSubjectX500Principal()));
                        }
                        Set<String> criticalExtensionOIDs = x509Certificate.getCriticalExtensionOIDs();
                        if (criticalExtensionOIDs == null) {
                            criticalExtensionOIDs = Collections.emptySet();
                        }
                        for (PKIXCertPathChecker pKIXCertPathChecker2 : arrayList2) {
                            if (!pKIXCertPathChecker2.isForwardCheckingSupported()) {
                                if (i2 == 0) {
                                    pKIXCertPathChecker2.init(false);
                                    if (pKIXCertPathChecker2 instanceof AlgorithmChecker) {
                                        ((AlgorithmChecker) pKIXCertPathChecker2).trySetTrustAnchor(forwardBuilder.trustAnchor);
                                    }
                                }
                                try {
                                    pKIXCertPathChecker2.check(x509Certificate, criticalExtensionOIDs);
                                } catch (CertPathValidatorException e2) {
                                    if (debug != null) {
                                        debug.println("SunCertPathBuilder.depthFirstSearchForward(): final verification failed: " + ((Object) e2));
                                    }
                                    if (this.buildParams.targetCertConstraints().match(x509Certificate) && e2.getReason() == CertPathValidatorException.BasicReason.REVOKED) {
                                        throw e2;
                                    }
                                    vertex.setThrowable(e2);
                                }
                            }
                        }
                        for (PKIXCertPathChecker pKIXCertPathChecker3 : this.buildParams.certPathCheckers()) {
                            if (pKIXCertPathChecker3.isForwardCheckingSupported() && (supportedExtensions = pKIXCertPathChecker3.getSupportedExtensions()) != null) {
                                criticalExtensionOIDs.removeAll(supportedExtensions);
                            }
                        }
                        if (!criticalExtensionOIDs.isEmpty()) {
                            criticalExtensionOIDs.remove(PKIXExtensions.BasicConstraints_Id.toString());
                            criticalExtensionOIDs.remove(PKIXExtensions.NameConstraints_Id.toString());
                            criticalExtensionOIDs.remove(PKIXExtensions.CertificatePolicies_Id.toString());
                            criticalExtensionOIDs.remove(PKIXExtensions.PolicyMappings_Id.toString());
                            criticalExtensionOIDs.remove(PKIXExtensions.PolicyConstraints_Id.toString());
                            criticalExtensionOIDs.remove(PKIXExtensions.InhibitAnyPolicy_Id.toString());
                            criticalExtensionOIDs.remove(PKIXExtensions.SubjectAlternativeName_Id.toString());
                            criticalExtensionOIDs.remove(PKIXExtensions.KeyUsage_Id.toString());
                            criticalExtensionOIDs.remove(PKIXExtensions.ExtendedKeyUsage_Id.toString());
                            if (!criticalExtensionOIDs.isEmpty()) {
                                throw new CertPathValidatorException("unrecognized critical extension(s)", null, null, -1, PKIXReason.UNRECOGNIZED_CRIT_EXT);
                            }
                        }
                    }
                    if (debug != null) {
                        debug.println("SunCertPathBuilder.depthFirstSearchForward(): final verification succeeded - path completed!");
                    }
                    this.pathCompleted = true;
                    if (forwardBuilder.trustAnchor.getTrustedCert() == null) {
                        forwardBuilder.addCertToPath(certificate, linkedList);
                    }
                    this.trustAnchor = forwardBuilder.trustAnchor;
                    if (basicChecker != null) {
                        this.finalPublicKey = basicChecker.getPublicKey();
                    } else {
                        if (linkedList.isEmpty()) {
                            last = forwardBuilder.trustAnchor.getTrustedCert();
                        } else {
                            last = linkedList.getLast();
                        }
                        this.finalPublicKey = last.getPublicKey();
                    }
                    this.policyTreeResult = policyChecker.getPolicyTree();
                    return;
                }
                if (forwardState.selfIssued && X509CertImpl.isSelfIssued(certificate)) {
                    if (debug != null) {
                        debug.println("Successive certs are self-issued");
                        return;
                    }
                    return;
                }
                forwardBuilder.addCertToPath(certificate, linkedList);
                forwardState2.updateState(certificate);
                list.add(new LinkedList());
                vertex.setIndex(list.size() - 1);
                depthFirstSearchForward(certificate.getIssuerX500Principal(), forwardState2, forwardBuilder, list, linkedList);
                if (this.pathCompleted) {
                    return;
                }
                if (debug != null) {
                    debug.println("SunCertPathBuilder.depthFirstSearchForward(): backtracking");
                }
                forwardBuilder.removeFinalCertFromPath(linkedList);
            } catch (GeneralSecurityException e3) {
                if (debug != null) {
                    debug.println("SunCertPathBuilder.depthFirstSearchForward(): validation failed: " + ((Object) e3));
                    e3.printStackTrace();
                }
                vertex.setThrowable(e3);
            }
        }
    }

    private static List<Vertex> addVertices(Collection<X509Certificate> collection, List<List<Vertex>> list) {
        List<Vertex> list2 = list.get(list.size() - 1);
        Iterator<X509Certificate> it = collection.iterator();
        while (it.hasNext()) {
            list2.add(new Vertex(it.next()));
        }
        return list2;
    }

    private static boolean anchorIsTarget(TrustAnchor trustAnchor, CertSelector certSelector) {
        X509Certificate trustedCert = trustAnchor.getTrustedCert();
        if (trustedCert != null) {
            return certSelector.match(trustedCert);
        }
        return false;
    }
}
