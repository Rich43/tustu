package sun.security.provider.certpath;

import java.io.IOException;
import java.security.cert.CertPathValidatorException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.PKIXReason;
import java.security.cert.PolicyNode;
import java.security.cert.PolicyQualifierInfo;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import sun.security.util.Debug;
import sun.security.x509.CertificatePoliciesExtension;
import sun.security.x509.CertificatePolicyMap;
import sun.security.x509.InhibitAnyPolicyExtension;
import sun.security.x509.PKIXExtensions;
import sun.security.x509.PolicyConstraintsExtension;
import sun.security.x509.PolicyInformation;
import sun.security.x509.PolicyMappingsExtension;
import sun.security.x509.X509CertImpl;

/* loaded from: rt.jar:sun/security/provider/certpath/PolicyChecker.class */
class PolicyChecker extends PKIXCertPathChecker {
    private final Set<String> initPolicies;
    private final int certPathLen;
    private final boolean expPolicyRequired;
    private final boolean polMappingInhibited;
    private final boolean anyPolicyInhibited;
    private final boolean rejectPolicyQualifiers;
    private PolicyNodeImpl rootNode;
    private int explicitPolicy;
    private int policyMapping;
    private int inhibitAnyPolicy;
    private int certIndex;
    private Set<String> supportedExts;
    private static final Debug debug = Debug.getInstance("certpath");
    static final String ANY_POLICY = "2.5.29.32.0";

    PolicyChecker(Set<String> set, int i2, boolean z2, boolean z3, boolean z4, boolean z5, PolicyNodeImpl policyNodeImpl) {
        if (set.isEmpty()) {
            this.initPolicies = new HashSet(1);
            this.initPolicies.add(ANY_POLICY);
        } else {
            this.initPolicies = new HashSet(set);
        }
        this.certPathLen = i2;
        this.expPolicyRequired = z2;
        this.polMappingInhibited = z3;
        this.anyPolicyInhibited = z4;
        this.rejectPolicyQualifiers = z5;
        this.rootNode = policyNodeImpl;
    }

    @Override // java.security.cert.PKIXCertPathChecker, java.security.cert.CertPathChecker
    public void init(boolean z2) throws CertPathValidatorException {
        if (z2) {
            throw new CertPathValidatorException("forward checking not supported");
        }
        this.certIndex = 1;
        this.explicitPolicy = this.expPolicyRequired ? 0 : this.certPathLen + 1;
        this.policyMapping = this.polMappingInhibited ? 0 : this.certPathLen + 1;
        this.inhibitAnyPolicy = this.anyPolicyInhibited ? 0 : this.certPathLen + 1;
    }

    @Override // java.security.cert.PKIXCertPathChecker, java.security.cert.CertPathChecker
    public boolean isForwardCheckingSupported() {
        return false;
    }

    @Override // java.security.cert.PKIXCertPathChecker
    public Set<String> getSupportedExtensions() {
        if (this.supportedExts == null) {
            this.supportedExts = new HashSet(4);
            this.supportedExts.add(PKIXExtensions.CertificatePolicies_Id.toString());
            this.supportedExts.add(PKIXExtensions.PolicyMappings_Id.toString());
            this.supportedExts.add(PKIXExtensions.PolicyConstraints_Id.toString());
            this.supportedExts.add(PKIXExtensions.InhibitAnyPolicy_Id.toString());
            this.supportedExts = Collections.unmodifiableSet(this.supportedExts);
        }
        return this.supportedExts;
    }

    @Override // java.security.cert.PKIXCertPathChecker
    public void check(Certificate certificate, Collection<String> collection) throws CertPathValidatorException {
        checkPolicy((X509Certificate) certificate);
        if (collection != null && !collection.isEmpty()) {
            collection.remove(PKIXExtensions.CertificatePolicies_Id.toString());
            collection.remove(PKIXExtensions.PolicyMappings_Id.toString());
            collection.remove(PKIXExtensions.PolicyConstraints_Id.toString());
            collection.remove(PKIXExtensions.InhibitAnyPolicy_Id.toString());
        }
    }

    private void checkPolicy(X509Certificate x509Certificate) throws CertPathValidatorException {
        if (debug != null) {
            debug.println("PolicyChecker.checkPolicy() ---checking certificate policies...");
            debug.println("PolicyChecker.checkPolicy() certIndex = " + this.certIndex);
            debug.println("PolicyChecker.checkPolicy() BEFORE PROCESSING: explicitPolicy = " + this.explicitPolicy);
            debug.println("PolicyChecker.checkPolicy() BEFORE PROCESSING: policyMapping = " + this.policyMapping);
            debug.println("PolicyChecker.checkPolicy() BEFORE PROCESSING: inhibitAnyPolicy = " + this.inhibitAnyPolicy);
            debug.println("PolicyChecker.checkPolicy() BEFORE PROCESSING: policyTree = " + ((Object) this.rootNode));
        }
        try {
            X509CertImpl impl = X509CertImpl.toImpl(x509Certificate);
            boolean z2 = this.certIndex == this.certPathLen;
            this.rootNode = processPolicies(this.certIndex, this.initPolicies, this.explicitPolicy, this.policyMapping, this.inhibitAnyPolicy, this.rejectPolicyQualifiers, this.rootNode, impl, z2);
            if (!z2) {
                this.explicitPolicy = mergeExplicitPolicy(this.explicitPolicy, impl, z2);
                this.policyMapping = mergePolicyMapping(this.policyMapping, impl);
                this.inhibitAnyPolicy = mergeInhibitAnyPolicy(this.inhibitAnyPolicy, impl);
            }
            this.certIndex++;
            if (debug != null) {
                debug.println("PolicyChecker.checkPolicy() AFTER PROCESSING: explicitPolicy = " + this.explicitPolicy);
                debug.println("PolicyChecker.checkPolicy() AFTER PROCESSING: policyMapping = " + this.policyMapping);
                debug.println("PolicyChecker.checkPolicy() AFTER PROCESSING: inhibitAnyPolicy = " + this.inhibitAnyPolicy);
                debug.println("PolicyChecker.checkPolicy() AFTER PROCESSING: policyTree = " + ((Object) this.rootNode));
                debug.println("PolicyChecker.checkPolicy() certificate policies verified");
            }
        } catch (CertificateException e2) {
            throw new CertPathValidatorException(e2);
        }
    }

    static int mergeExplicitPolicy(int i2, X509CertImpl x509CertImpl, boolean z2) throws CertPathValidatorException {
        if (i2 > 0 && !X509CertImpl.isSelfIssued(x509CertImpl)) {
            i2--;
        }
        try {
            PolicyConstraintsExtension policyConstraintsExtension = x509CertImpl.getPolicyConstraintsExtension();
            if (policyConstraintsExtension == null) {
                return i2;
            }
            int iIntValue = policyConstraintsExtension.get(PolicyConstraintsExtension.REQUIRE).intValue();
            if (debug != null) {
                debug.println("PolicyChecker.mergeExplicitPolicy() require Index from cert = " + iIntValue);
            }
            if (!z2) {
                if (iIntValue != -1 && (i2 == -1 || iIntValue < i2)) {
                    i2 = iIntValue;
                }
            } else if (iIntValue == 0) {
                i2 = iIntValue;
            }
            return i2;
        } catch (IOException e2) {
            if (debug != null) {
                debug.println("PolicyChecker.mergeExplicitPolicy unexpected exception");
                e2.printStackTrace();
            }
            throw new CertPathValidatorException(e2);
        }
    }

    static int mergePolicyMapping(int i2, X509CertImpl x509CertImpl) throws CertPathValidatorException {
        if (i2 > 0 && !X509CertImpl.isSelfIssued(x509CertImpl)) {
            i2--;
        }
        try {
            PolicyConstraintsExtension policyConstraintsExtension = x509CertImpl.getPolicyConstraintsExtension();
            if (policyConstraintsExtension == null) {
                return i2;
            }
            int iIntValue = policyConstraintsExtension.get(PolicyConstraintsExtension.INHIBIT).intValue();
            if (debug != null) {
                debug.println("PolicyChecker.mergePolicyMapping() inhibit Index from cert = " + iIntValue);
            }
            if (iIntValue != -1 && (i2 == -1 || iIntValue < i2)) {
                i2 = iIntValue;
            }
            return i2;
        } catch (IOException e2) {
            if (debug != null) {
                debug.println("PolicyChecker.mergePolicyMapping unexpected exception");
                e2.printStackTrace();
            }
            throw new CertPathValidatorException(e2);
        }
    }

    static int mergeInhibitAnyPolicy(int i2, X509CertImpl x509CertImpl) throws CertPathValidatorException {
        if (i2 > 0 && !X509CertImpl.isSelfIssued(x509CertImpl)) {
            i2--;
        }
        try {
            InhibitAnyPolicyExtension inhibitAnyPolicyExtension = (InhibitAnyPolicyExtension) x509CertImpl.getExtension(PKIXExtensions.InhibitAnyPolicy_Id);
            if (inhibitAnyPolicyExtension == null) {
                return i2;
            }
            int iIntValue = inhibitAnyPolicyExtension.get(InhibitAnyPolicyExtension.SKIP_CERTS).intValue();
            if (debug != null) {
                debug.println("PolicyChecker.mergeInhibitAnyPolicy() skipCerts Index from cert = " + iIntValue);
            }
            if (iIntValue != -1 && iIntValue < i2) {
                i2 = iIntValue;
            }
            return i2;
        } catch (IOException e2) {
            if (debug != null) {
                debug.println("PolicyChecker.mergeInhibitAnyPolicy unexpected exception");
                e2.printStackTrace();
            }
            throw new CertPathValidatorException(e2);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    static PolicyNodeImpl processPolicies(int i2, Set<String> set, int i3, int i4, int i5, boolean z2, PolicyNodeImpl policyNodeImpl, X509CertImpl x509CertImpl, boolean z3) throws CertPathValidatorException {
        PolicyNodeImpl policyNodeImplCopyTree;
        boolean zIsCritical = false;
        Set hashSet = new HashSet();
        if (policyNodeImpl == null) {
            policyNodeImplCopyTree = null;
        } else {
            policyNodeImplCopyTree = policyNodeImpl.copyTree();
        }
        CertificatePoliciesExtension certificatePoliciesExtension = x509CertImpl.getCertificatePoliciesExtension();
        if (certificatePoliciesExtension != null && policyNodeImplCopyTree != null) {
            zIsCritical = certificatePoliciesExtension.isCritical();
            if (debug != null) {
                debug.println("PolicyChecker.processPolicies() policiesCritical = " + zIsCritical);
            }
            try {
                List<PolicyInformation> list = certificatePoliciesExtension.get(CertificatePoliciesExtension.POLICIES);
                if (debug != null) {
                    debug.println("PolicyChecker.processPolicies() rejectPolicyQualifiers = " + z2);
                }
                boolean z4 = false;
                for (PolicyInformation policyInformation : list) {
                    String string = policyInformation.getPolicyIdentifier().getIdentifier().toString();
                    if (string.equals(ANY_POLICY)) {
                        z4 = true;
                        hashSet = policyInformation.getPolicyQualifiers();
                    } else {
                        if (debug != null) {
                            debug.println("PolicyChecker.processPolicies() processing policy: " + string);
                        }
                        Set<PolicyQualifierInfo> policyQualifiers = policyInformation.getPolicyQualifiers();
                        if (!policyQualifiers.isEmpty() && z2 && zIsCritical) {
                            throw new CertPathValidatorException("critical policy qualifiers present in certificate", null, null, -1, PKIXReason.INVALID_POLICY);
                        }
                        if (!processParents(i2, zIsCritical, z2, policyNodeImplCopyTree, string, policyQualifiers, false)) {
                            processParents(i2, zIsCritical, z2, policyNodeImplCopyTree, string, policyQualifiers, true);
                        }
                    }
                }
                if (z4 && (i5 > 0 || (!z3 && X509CertImpl.isSelfIssued(x509CertImpl)))) {
                    if (debug != null) {
                        debug.println("PolicyChecker.processPolicies() processing policy: 2.5.29.32.0");
                    }
                    processParents(i2, zIsCritical, z2, policyNodeImplCopyTree, ANY_POLICY, hashSet, true);
                }
                policyNodeImplCopyTree.prune(i2);
                if (!policyNodeImplCopyTree.getChildren().hasNext()) {
                    policyNodeImplCopyTree = null;
                }
            } catch (IOException e2) {
                throw new CertPathValidatorException("Exception while retrieving policyOIDs", e2);
            }
        } else if (certificatePoliciesExtension == null) {
            if (debug != null) {
                debug.println("PolicyChecker.processPolicies() no policies present in cert");
            }
            policyNodeImplCopyTree = null;
        }
        if (policyNodeImplCopyTree != null && !z3) {
            policyNodeImplCopyTree = processPolicyMappings(x509CertImpl, i2, i4, policyNodeImplCopyTree, zIsCritical, hashSet);
        }
        if (policyNodeImplCopyTree != null && !set.contains(ANY_POLICY) && certificatePoliciesExtension != null) {
            policyNodeImplCopyTree = removeInvalidNodes(policyNodeImplCopyTree, i2, set, certificatePoliciesExtension);
            if (policyNodeImplCopyTree != null && z3) {
                policyNodeImplCopyTree = rewriteLeafNodes(i2, set, policyNodeImplCopyTree);
            }
        }
        if (z3) {
            i3 = mergeExplicitPolicy(i3, x509CertImpl, z3);
        }
        if (i3 == 0 && policyNodeImplCopyTree == null) {
            throw new CertPathValidatorException("non-null policy tree required and policy tree is null", null, null, -1, PKIXReason.INVALID_POLICY);
        }
        return policyNodeImplCopyTree;
    }

    private static PolicyNodeImpl rewriteLeafNodes(int i2, Set<String> set, PolicyNodeImpl policyNodeImpl) {
        Set<PolicyNodeImpl> policyNodesValid = policyNodeImpl.getPolicyNodesValid(i2, ANY_POLICY);
        if (policyNodesValid.isEmpty()) {
            return policyNodeImpl;
        }
        PolicyNodeImpl next = policyNodesValid.iterator().next();
        PolicyNodeImpl policyNodeImpl2 = (PolicyNodeImpl) next.getParent();
        policyNodeImpl2.deleteChild(next);
        HashSet<String> hashSet = new HashSet(set);
        Iterator<PolicyNodeImpl> it = policyNodeImpl.getPolicyNodes(i2).iterator();
        while (it.hasNext()) {
            hashSet.remove(it.next().getValidPolicy());
        }
        if (hashSet.isEmpty()) {
            policyNodeImpl.prune(i2);
            if (!policyNodeImpl.getChildren().hasNext()) {
                policyNodeImpl = null;
            }
        } else {
            boolean zIsCritical = next.isCritical();
            Set<PolicyQualifierInfo> policyQualifiers = next.getPolicyQualifiers();
            for (String str : hashSet) {
                new PolicyNodeImpl(policyNodeImpl2, str, policyQualifiers, zIsCritical, Collections.singleton(str), false);
            }
        }
        return policyNodeImpl;
    }

    private static boolean processParents(int i2, boolean z2, boolean z3, PolicyNodeImpl policyNodeImpl, String str, Set<PolicyQualifierInfo> set, boolean z4) throws CertPathValidatorException {
        boolean z5 = false;
        if (debug != null) {
            debug.println("PolicyChecker.processParents(): matchAny = " + z4);
        }
        for (PolicyNodeImpl policyNodeImpl2 : policyNodeImpl.getPolicyNodesExpected(i2 - 1, str, z4)) {
            if (debug != null) {
                debug.println("PolicyChecker.processParents() found parent:\n" + policyNodeImpl2.asString());
            }
            z5 = true;
            policyNodeImpl2.getValidPolicy();
            if (str.equals(ANY_POLICY)) {
                for (String str2 : policyNodeImpl2.getExpectedPolicies()) {
                    Iterator<PolicyNodeImpl> children = policyNodeImpl2.getChildren();
                    while (true) {
                        if (children.hasNext()) {
                            String validPolicy = children.next().getValidPolicy();
                            if (str2.equals(validPolicy)) {
                                if (debug != null) {
                                    debug.println(validPolicy + " in parent's expected policy set already appears in child node");
                                }
                            }
                        } else {
                            HashSet hashSet = new HashSet();
                            hashSet.add(str2);
                            new PolicyNodeImpl(policyNodeImpl2, str2, set, z2, hashSet, false);
                            break;
                        }
                    }
                }
            } else {
                HashSet hashSet2 = new HashSet();
                hashSet2.add(str);
                new PolicyNodeImpl(policyNodeImpl2, str, set, z2, hashSet2, false);
            }
        }
        return z5;
    }

    private static PolicyNodeImpl processPolicyMappings(X509CertImpl x509CertImpl, int i2, int i3, PolicyNodeImpl policyNodeImpl, boolean z2, Set<PolicyQualifierInfo> set) throws CertPathValidatorException {
        PolicyMappingsExtension policyMappingsExtension = x509CertImpl.getPolicyMappingsExtension();
        if (policyMappingsExtension == null) {
            return policyNodeImpl;
        }
        if (debug != null) {
            debug.println("PolicyChecker.processPolicyMappings() inside policyMapping check");
        }
        try {
            boolean z3 = false;
            for (CertificatePolicyMap certificatePolicyMap : policyMappingsExtension.get(PolicyMappingsExtension.MAP)) {
                String string = certificatePolicyMap.getIssuerIdentifier().getIdentifier().toString();
                String string2 = certificatePolicyMap.getSubjectIdentifier().getIdentifier().toString();
                if (debug != null) {
                    debug.println("PolicyChecker.processPolicyMappings() issuerDomain = " + string);
                    debug.println("PolicyChecker.processPolicyMappings() subjectDomain = " + string2);
                }
                if (string.equals(ANY_POLICY)) {
                    throw new CertPathValidatorException("encountered an issuerDomainPolicy of ANY_POLICY", null, null, -1, PKIXReason.INVALID_POLICY);
                }
                if (string2.equals(ANY_POLICY)) {
                    throw new CertPathValidatorException("encountered a subjectDomainPolicy of ANY_POLICY", null, null, -1, PKIXReason.INVALID_POLICY);
                }
                Set<PolicyNodeImpl> policyNodesValid = policyNodeImpl.getPolicyNodesValid(i2, string);
                if (!policyNodesValid.isEmpty()) {
                    for (PolicyNodeImpl policyNodeImpl2 : policyNodesValid) {
                        if (i3 > 0 || i3 == -1) {
                            policyNodeImpl2.addExpectedPolicy(string2);
                        } else if (i3 == 0) {
                            PolicyNodeImpl policyNodeImpl3 = (PolicyNodeImpl) policyNodeImpl2.getParent();
                            if (debug != null) {
                                debug.println("PolicyChecker.processPolicyMappings() before deleting: policy tree = " + ((Object) policyNodeImpl));
                            }
                            policyNodeImpl3.deleteChild(policyNodeImpl2);
                            z3 = true;
                            if (debug != null) {
                                debug.println("PolicyChecker.processPolicyMappings() after deleting: policy tree = " + ((Object) policyNodeImpl));
                            }
                        }
                    }
                } else if (i3 > 0 || i3 == -1) {
                    Iterator<PolicyNodeImpl> it = policyNodeImpl.getPolicyNodesValid(i2, ANY_POLICY).iterator();
                    while (it.hasNext()) {
                        PolicyNodeImpl policyNodeImpl4 = (PolicyNodeImpl) it.next().getParent();
                        HashSet hashSet = new HashSet();
                        hashSet.add(string2);
                        new PolicyNodeImpl(policyNodeImpl4, string, set, z2, hashSet, true);
                    }
                }
            }
            if (z3) {
                policyNodeImpl.prune(i2);
                if (!policyNodeImpl.getChildren().hasNext()) {
                    if (debug != null) {
                        debug.println("setting rootNode to null");
                    }
                    policyNodeImpl = null;
                }
            }
            return policyNodeImpl;
        } catch (IOException e2) {
            if (debug != null) {
                debug.println("PolicyChecker.processPolicyMappings() mapping exception");
                e2.printStackTrace();
            }
            throw new CertPathValidatorException("Exception while checking mapping", e2);
        }
    }

    private static PolicyNodeImpl removeInvalidNodes(PolicyNodeImpl policyNodeImpl, int i2, Set<String> set, CertificatePoliciesExtension certificatePoliciesExtension) throws CertPathValidatorException {
        try {
            boolean z2 = false;
            Iterator<PolicyInformation> it = certificatePoliciesExtension.get(CertificatePoliciesExtension.POLICIES).iterator();
            while (it.hasNext()) {
                String string = it.next().getPolicyIdentifier().getIdentifier().toString();
                if (debug != null) {
                    debug.println("PolicyChecker.processPolicies() processing policy second time: " + string);
                }
                for (PolicyNodeImpl policyNodeImpl2 : policyNodeImpl.getPolicyNodesValid(i2, string)) {
                    PolicyNodeImpl policyNodeImpl3 = (PolicyNodeImpl) policyNodeImpl2.getParent();
                    if (policyNodeImpl3.getValidPolicy().equals(ANY_POLICY) && !set.contains(string) && !string.equals(ANY_POLICY)) {
                        if (debug != null) {
                            debug.println("PolicyChecker.processPolicies() before deleting: policy tree = " + ((Object) policyNodeImpl));
                        }
                        policyNodeImpl3.deleteChild(policyNodeImpl2);
                        z2 = true;
                        if (debug != null) {
                            debug.println("PolicyChecker.processPolicies() after deleting: policy tree = " + ((Object) policyNodeImpl));
                        }
                    }
                }
            }
            if (z2) {
                policyNodeImpl.prune(i2);
                if (!policyNodeImpl.getChildren().hasNext()) {
                    policyNodeImpl = null;
                }
            }
            return policyNodeImpl;
        } catch (IOException e2) {
            throw new CertPathValidatorException("Exception while retrieving policyOIDs", e2);
        }
    }

    PolicyNode getPolicyTree() {
        if (this.rootNode == null) {
            return null;
        }
        PolicyNodeImpl policyNodeImplCopyTree = this.rootNode.copyTree();
        policyNodeImplCopyTree.setImmutable();
        return policyNodeImplCopyTree;
    }
}
