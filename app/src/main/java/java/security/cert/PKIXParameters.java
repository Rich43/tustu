package java.security.cert;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* loaded from: rt.jar:java/security/cert/PKIXParameters.class */
public class PKIXParameters implements CertPathParameters {
    private Set<TrustAnchor> unmodTrustAnchors;
    private Date date;
    private List<PKIXCertPathChecker> certPathCheckers;
    private String sigProvider;
    private Set<String> unmodInitialPolicies;
    private List<CertStore> certStores;
    private CertSelector certSelector;
    private boolean revocationEnabled = true;
    private boolean explicitPolicyRequired = false;
    private boolean policyMappingInhibited = false;
    private boolean anyPolicyInhibited = false;
    private boolean policyQualifiersRejected = true;

    public PKIXParameters(Set<TrustAnchor> set) throws InvalidAlgorithmParameterException {
        setTrustAnchors(set);
        this.unmodInitialPolicies = Collections.emptySet();
        this.certPathCheckers = new ArrayList();
        this.certStores = new ArrayList();
    }

    public PKIXParameters(KeyStore keyStore) throws KeyStoreException, InvalidAlgorithmParameterException {
        if (keyStore == null) {
            throw new NullPointerException("the keystore parameter must be non-null");
        }
        HashSet hashSet = new HashSet();
        Enumeration<String> enumerationAliases = keyStore.aliases();
        while (enumerationAliases.hasMoreElements()) {
            String strNextElement2 = enumerationAliases.nextElement2();
            if (keyStore.isCertificateEntry(strNextElement2)) {
                Certificate certificate = keyStore.getCertificate(strNextElement2);
                if (certificate instanceof X509Certificate) {
                    hashSet.add(new TrustAnchor((X509Certificate) certificate, null));
                }
            }
        }
        setTrustAnchors(hashSet);
        this.unmodInitialPolicies = Collections.emptySet();
        this.certPathCheckers = new ArrayList();
        this.certStores = new ArrayList();
    }

    public Set<TrustAnchor> getTrustAnchors() {
        return this.unmodTrustAnchors;
    }

    public void setTrustAnchors(Set<TrustAnchor> set) throws InvalidAlgorithmParameterException {
        if (set == null) {
            throw new NullPointerException("the trustAnchors parameters must be non-null");
        }
        if (set.isEmpty()) {
            throw new InvalidAlgorithmParameterException("the trustAnchors parameter must be non-empty");
        }
        Iterator<TrustAnchor> it = set.iterator();
        while (it.hasNext()) {
            if (!(it.next() instanceof TrustAnchor)) {
                throw new ClassCastException("all elements of set must be of type java.security.cert.TrustAnchor");
            }
        }
        this.unmodTrustAnchors = Collections.unmodifiableSet(new HashSet(set));
    }

    public Set<String> getInitialPolicies() {
        return this.unmodInitialPolicies;
    }

    public void setInitialPolicies(Set<String> set) {
        if (set != null) {
            Iterator<String> it = set.iterator();
            while (it.hasNext()) {
                if (!(it.next() instanceof String)) {
                    throw new ClassCastException("all elements of set must be of type java.lang.String");
                }
            }
            this.unmodInitialPolicies = Collections.unmodifiableSet(new HashSet(set));
            return;
        }
        this.unmodInitialPolicies = Collections.emptySet();
    }

    public void setCertStores(List<CertStore> list) {
        if (list == null) {
            this.certStores = new ArrayList();
            return;
        }
        Iterator<CertStore> it = list.iterator();
        while (it.hasNext()) {
            if (!(it.next() instanceof CertStore)) {
                throw new ClassCastException("all elements of list must be of type java.security.cert.CertStore");
            }
        }
        this.certStores = new ArrayList(list);
    }

    public void addCertStore(CertStore certStore) {
        if (certStore != null) {
            this.certStores.add(certStore);
        }
    }

    public List<CertStore> getCertStores() {
        return Collections.unmodifiableList(new ArrayList(this.certStores));
    }

    public void setRevocationEnabled(boolean z2) {
        this.revocationEnabled = z2;
    }

    public boolean isRevocationEnabled() {
        return this.revocationEnabled;
    }

    public void setExplicitPolicyRequired(boolean z2) {
        this.explicitPolicyRequired = z2;
    }

    public boolean isExplicitPolicyRequired() {
        return this.explicitPolicyRequired;
    }

    public void setPolicyMappingInhibited(boolean z2) {
        this.policyMappingInhibited = z2;
    }

    public boolean isPolicyMappingInhibited() {
        return this.policyMappingInhibited;
    }

    public void setAnyPolicyInhibited(boolean z2) {
        this.anyPolicyInhibited = z2;
    }

    public boolean isAnyPolicyInhibited() {
        return this.anyPolicyInhibited;
    }

    public void setPolicyQualifiersRejected(boolean z2) {
        this.policyQualifiersRejected = z2;
    }

    public boolean getPolicyQualifiersRejected() {
        return this.policyQualifiersRejected;
    }

    public Date getDate() {
        if (this.date == null) {
            return null;
        }
        return (Date) this.date.clone();
    }

    public void setDate(Date date) {
        if (date != null) {
            this.date = (Date) date.clone();
        }
    }

    public void setCertPathCheckers(List<PKIXCertPathChecker> list) {
        if (list != null) {
            ArrayList arrayList = new ArrayList();
            Iterator<PKIXCertPathChecker> it = list.iterator();
            while (it.hasNext()) {
                arrayList.add((PKIXCertPathChecker) it.next().clone());
            }
            this.certPathCheckers = arrayList;
            return;
        }
        this.certPathCheckers = new ArrayList();
    }

    public List<PKIXCertPathChecker> getCertPathCheckers() {
        ArrayList arrayList = new ArrayList();
        Iterator<PKIXCertPathChecker> it = this.certPathCheckers.iterator();
        while (it.hasNext()) {
            arrayList.add((PKIXCertPathChecker) it.next().clone());
        }
        return Collections.unmodifiableList(arrayList);
    }

    public void addCertPathChecker(PKIXCertPathChecker pKIXCertPathChecker) {
        if (pKIXCertPathChecker != null) {
            this.certPathCheckers.add((PKIXCertPathChecker) pKIXCertPathChecker.clone());
        }
    }

    public String getSigProvider() {
        return this.sigProvider;
    }

    public void setSigProvider(String str) {
        this.sigProvider = str;
    }

    public CertSelector getTargetCertConstraints() {
        if (this.certSelector != null) {
            return (CertSelector) this.certSelector.clone();
        }
        return null;
    }

    public void setTargetCertConstraints(CertSelector certSelector) {
        if (certSelector != null) {
            this.certSelector = (CertSelector) certSelector.clone();
        } else {
            this.certSelector = null;
        }
    }

    @Override // java.security.cert.CertPathParameters
    public Object clone() {
        try {
            PKIXParameters pKIXParameters = (PKIXParameters) super.clone();
            if (this.certStores != null) {
                pKIXParameters.certStores = new ArrayList(this.certStores);
            }
            if (this.certPathCheckers != null) {
                pKIXParameters.certPathCheckers = new ArrayList(this.certPathCheckers.size());
                Iterator<PKIXCertPathChecker> it = this.certPathCheckers.iterator();
                while (it.hasNext()) {
                    pKIXParameters.certPathCheckers.add((PKIXCertPathChecker) it.next().clone());
                }
            }
            return pKIXParameters;
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2.toString(), e2);
        }
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[\n");
        if (this.unmodTrustAnchors != null) {
            stringBuffer.append("  Trust Anchors: " + this.unmodTrustAnchors.toString() + "\n");
        }
        if (this.unmodInitialPolicies != null) {
            if (this.unmodInitialPolicies.isEmpty()) {
                stringBuffer.append("  Initial Policy OIDs: any\n");
            } else {
                stringBuffer.append("  Initial Policy OIDs: [" + this.unmodInitialPolicies.toString() + "]\n");
            }
        }
        stringBuffer.append("  Validity Date: " + String.valueOf(this.date) + "\n");
        stringBuffer.append("  Signature Provider: " + String.valueOf(this.sigProvider) + "\n");
        stringBuffer.append("  Default Revocation Enabled: " + this.revocationEnabled + "\n");
        stringBuffer.append("  Explicit Policy Required: " + this.explicitPolicyRequired + "\n");
        stringBuffer.append("  Policy Mapping Inhibited: " + this.policyMappingInhibited + "\n");
        stringBuffer.append("  Any Policy Inhibited: " + this.anyPolicyInhibited + "\n");
        stringBuffer.append("  Policy Qualifiers Rejected: " + this.policyQualifiersRejected + "\n");
        stringBuffer.append("  Target Cert Constraints: " + String.valueOf(this.certSelector) + "\n");
        if (this.certPathCheckers != null) {
            stringBuffer.append("  Certification Path Checkers: [" + this.certPathCheckers.toString() + "]\n");
        }
        if (this.certStores != null) {
            stringBuffer.append("  CertStores: [" + this.certStores.toString() + "]\n");
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }
}
