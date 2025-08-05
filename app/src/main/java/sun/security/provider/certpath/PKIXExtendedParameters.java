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

/* loaded from: rt.jar:sun/security/provider/certpath/PKIXExtendedParameters.class */
public class PKIXExtendedParameters extends PKIXBuilderParameters {

    /* renamed from: p, reason: collision with root package name */
    private final PKIXBuilderParameters f13644p;
    private Timestamp jarTimestamp;
    private final String variant;

    public PKIXExtendedParameters(PKIXBuilderParameters pKIXBuilderParameters, Timestamp timestamp, String str) throws InvalidAlgorithmParameterException {
        super(pKIXBuilderParameters.getTrustAnchors(), (CertSelector) null);
        this.f13644p = pKIXBuilderParameters;
        this.jarTimestamp = timestamp;
        this.variant = str;
    }

    public Timestamp getTimestamp() {
        return this.jarTimestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.jarTimestamp = timestamp;
    }

    public String getVariant() {
        return this.variant;
    }

    @Override // java.security.cert.PKIXParameters
    public void setDate(Date date) {
        this.f13644p.setDate(date);
    }

    @Override // java.security.cert.PKIXParameters
    public void addCertPathChecker(PKIXCertPathChecker pKIXCertPathChecker) {
        this.f13644p.addCertPathChecker(pKIXCertPathChecker);
    }

    @Override // java.security.cert.PKIXBuilderParameters
    public void setMaxPathLength(int i2) {
        this.f13644p.setMaxPathLength(i2);
    }

    @Override // java.security.cert.PKIXBuilderParameters
    public int getMaxPathLength() {
        return this.f13644p.getMaxPathLength();
    }

    @Override // java.security.cert.PKIXBuilderParameters, java.security.cert.PKIXParameters
    public String toString() {
        return this.f13644p.toString();
    }

    @Override // java.security.cert.PKIXParameters
    public Set<TrustAnchor> getTrustAnchors() {
        return this.f13644p.getTrustAnchors();
    }

    @Override // java.security.cert.PKIXParameters
    public void setTrustAnchors(Set<TrustAnchor> set) throws InvalidAlgorithmParameterException {
        if (this.f13644p == null) {
            return;
        }
        this.f13644p.setTrustAnchors(set);
    }

    @Override // java.security.cert.PKIXParameters
    public Set<String> getInitialPolicies() {
        return this.f13644p.getInitialPolicies();
    }

    @Override // java.security.cert.PKIXParameters
    public void setInitialPolicies(Set<String> set) {
        this.f13644p.setInitialPolicies(set);
    }

    @Override // java.security.cert.PKIXParameters
    public void setCertStores(List<CertStore> list) {
        this.f13644p.setCertStores(list);
    }

    @Override // java.security.cert.PKIXParameters
    public void addCertStore(CertStore certStore) {
        this.f13644p.addCertStore(certStore);
    }

    @Override // java.security.cert.PKIXParameters
    public List<CertStore> getCertStores() {
        return this.f13644p.getCertStores();
    }

    @Override // java.security.cert.PKIXParameters
    public void setRevocationEnabled(boolean z2) {
        this.f13644p.setRevocationEnabled(z2);
    }

    @Override // java.security.cert.PKIXParameters
    public boolean isRevocationEnabled() {
        return this.f13644p.isRevocationEnabled();
    }

    @Override // java.security.cert.PKIXParameters
    public void setExplicitPolicyRequired(boolean z2) {
        this.f13644p.setExplicitPolicyRequired(z2);
    }

    @Override // java.security.cert.PKIXParameters
    public boolean isExplicitPolicyRequired() {
        return this.f13644p.isExplicitPolicyRequired();
    }

    @Override // java.security.cert.PKIXParameters
    public void setPolicyMappingInhibited(boolean z2) {
        this.f13644p.setPolicyMappingInhibited(z2);
    }

    @Override // java.security.cert.PKIXParameters
    public boolean isPolicyMappingInhibited() {
        return this.f13644p.isPolicyMappingInhibited();
    }

    @Override // java.security.cert.PKIXParameters
    public void setAnyPolicyInhibited(boolean z2) {
        this.f13644p.setAnyPolicyInhibited(z2);
    }

    @Override // java.security.cert.PKIXParameters
    public boolean isAnyPolicyInhibited() {
        return this.f13644p.isAnyPolicyInhibited();
    }

    @Override // java.security.cert.PKIXParameters
    public void setPolicyQualifiersRejected(boolean z2) {
        this.f13644p.setPolicyQualifiersRejected(z2);
    }

    @Override // java.security.cert.PKIXParameters
    public boolean getPolicyQualifiersRejected() {
        return this.f13644p.getPolicyQualifiersRejected();
    }

    @Override // java.security.cert.PKIXParameters
    public Date getDate() {
        return this.f13644p.getDate();
    }

    @Override // java.security.cert.PKIXParameters
    public void setCertPathCheckers(List<PKIXCertPathChecker> list) {
        this.f13644p.setCertPathCheckers(list);
    }

    @Override // java.security.cert.PKIXParameters
    public List<PKIXCertPathChecker> getCertPathCheckers() {
        return this.f13644p.getCertPathCheckers();
    }

    @Override // java.security.cert.PKIXParameters
    public String getSigProvider() {
        return this.f13644p.getSigProvider();
    }

    @Override // java.security.cert.PKIXParameters
    public void setSigProvider(String str) {
        this.f13644p.setSigProvider(str);
    }

    @Override // java.security.cert.PKIXParameters
    public CertSelector getTargetCertConstraints() {
        return this.f13644p.getTargetCertConstraints();
    }

    @Override // java.security.cert.PKIXParameters
    public void setTargetCertConstraints(CertSelector certSelector) {
        if (this.f13644p == null) {
            return;
        }
        this.f13644p.setTargetCertConstraints(certSelector);
    }
}
