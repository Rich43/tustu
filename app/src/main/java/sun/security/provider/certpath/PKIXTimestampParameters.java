package sun.security.provider.certpath;

import java.security.InvalidAlgorithmParameterException;
import java.security.Timestamp;
import java.security.cert.CertSelector;
import java.security.cert.CertStore;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.TrustAnchor;
import java.util.Date;
import java.util.List;
import java.util.Set;

/* loaded from: rt.jar:sun/security/provider/certpath/PKIXTimestampParameters.class */
public class PKIXTimestampParameters extends PKIXBuilderParameters {

    /* renamed from: p, reason: collision with root package name */
    private final PKIXBuilderParameters f13645p;
    private Timestamp jarTimestamp;

    public PKIXTimestampParameters(PKIXBuilderParameters pKIXBuilderParameters, Timestamp timestamp) throws InvalidAlgorithmParameterException {
        super(pKIXBuilderParameters.getTrustAnchors(), (CertSelector) null);
        this.f13645p = pKIXBuilderParameters;
        this.jarTimestamp = timestamp;
    }

    public Timestamp getTimestamp() {
        return this.jarTimestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.jarTimestamp = timestamp;
    }

    @Override // java.security.cert.PKIXParameters
    public void setDate(Date date) {
        this.f13645p.setDate(date);
    }

    @Override // java.security.cert.PKIXParameters
    public void addCertPathChecker(PKIXCertPathChecker pKIXCertPathChecker) {
        this.f13645p.addCertPathChecker(pKIXCertPathChecker);
    }

    @Override // java.security.cert.PKIXBuilderParameters
    public void setMaxPathLength(int i2) {
        this.f13645p.setMaxPathLength(i2);
    }

    @Override // java.security.cert.PKIXBuilderParameters
    public int getMaxPathLength() {
        return this.f13645p.getMaxPathLength();
    }

    @Override // java.security.cert.PKIXBuilderParameters, java.security.cert.PKIXParameters
    public String toString() {
        return this.f13645p.toString();
    }

    @Override // java.security.cert.PKIXParameters
    public Set<TrustAnchor> getTrustAnchors() {
        return this.f13645p.getTrustAnchors();
    }

    @Override // java.security.cert.PKIXParameters
    public void setTrustAnchors(Set<TrustAnchor> set) throws InvalidAlgorithmParameterException {
        if (this.f13645p == null) {
            return;
        }
        this.f13645p.setTrustAnchors(set);
    }

    @Override // java.security.cert.PKIXParameters
    public Set<String> getInitialPolicies() {
        return this.f13645p.getInitialPolicies();
    }

    @Override // java.security.cert.PKIXParameters
    public void setInitialPolicies(Set<String> set) {
        this.f13645p.setInitialPolicies(set);
    }

    @Override // java.security.cert.PKIXParameters
    public void setCertStores(List<CertStore> list) {
        this.f13645p.setCertStores(list);
    }

    @Override // java.security.cert.PKIXParameters
    public void addCertStore(CertStore certStore) {
        this.f13645p.addCertStore(certStore);
    }

    @Override // java.security.cert.PKIXParameters
    public List<CertStore> getCertStores() {
        return this.f13645p.getCertStores();
    }

    @Override // java.security.cert.PKIXParameters
    public void setRevocationEnabled(boolean z2) {
        this.f13645p.setRevocationEnabled(z2);
    }

    @Override // java.security.cert.PKIXParameters
    public boolean isRevocationEnabled() {
        return this.f13645p.isRevocationEnabled();
    }

    @Override // java.security.cert.PKIXParameters
    public void setExplicitPolicyRequired(boolean z2) {
        this.f13645p.setExplicitPolicyRequired(z2);
    }

    @Override // java.security.cert.PKIXParameters
    public boolean isExplicitPolicyRequired() {
        return this.f13645p.isExplicitPolicyRequired();
    }

    @Override // java.security.cert.PKIXParameters
    public void setPolicyMappingInhibited(boolean z2) {
        this.f13645p.setPolicyMappingInhibited(z2);
    }

    @Override // java.security.cert.PKIXParameters
    public boolean isPolicyMappingInhibited() {
        return this.f13645p.isPolicyMappingInhibited();
    }

    @Override // java.security.cert.PKIXParameters
    public void setAnyPolicyInhibited(boolean z2) {
        this.f13645p.setAnyPolicyInhibited(z2);
    }

    @Override // java.security.cert.PKIXParameters
    public boolean isAnyPolicyInhibited() {
        return this.f13645p.isAnyPolicyInhibited();
    }

    @Override // java.security.cert.PKIXParameters
    public void setPolicyQualifiersRejected(boolean z2) {
        this.f13645p.setPolicyQualifiersRejected(z2);
    }

    @Override // java.security.cert.PKIXParameters
    public boolean getPolicyQualifiersRejected() {
        return this.f13645p.getPolicyQualifiersRejected();
    }

    @Override // java.security.cert.PKIXParameters
    public Date getDate() {
        return this.f13645p.getDate();
    }

    @Override // java.security.cert.PKIXParameters
    public void setCertPathCheckers(List<PKIXCertPathChecker> list) {
        this.f13645p.setCertPathCheckers(list);
    }

    @Override // java.security.cert.PKIXParameters
    public List<PKIXCertPathChecker> getCertPathCheckers() {
        return this.f13645p.getCertPathCheckers();
    }

    @Override // java.security.cert.PKIXParameters
    public String getSigProvider() {
        return this.f13645p.getSigProvider();
    }

    @Override // java.security.cert.PKIXParameters
    public void setSigProvider(String str) {
        this.f13645p.setSigProvider(str);
    }

    @Override // java.security.cert.PKIXParameters
    public CertSelector getTargetCertConstraints() {
        return this.f13645p.getTargetCertConstraints();
    }

    @Override // java.security.cert.PKIXParameters
    public void setTargetCertConstraints(CertSelector certSelector) {
        if (this.f13645p == null) {
            return;
        }
        this.f13645p.setTargetCertConstraints(certSelector);
    }
}
