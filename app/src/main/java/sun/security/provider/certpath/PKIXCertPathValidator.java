package sun.security.provider.certpath;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.cert.CertPath;
import java.security.cert.CertPathChecker;
import java.security.cert.CertPathParameters;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertPathValidatorResult;
import java.security.cert.CertPathValidatorSpi;
import java.security.cert.CertificateException;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.PKIXCertPathValidatorResult;
import java.security.cert.PKIXReason;
import java.security.cert.PKIXRevocationChecker;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import sun.security.provider.certpath.PKIX;
import sun.security.util.Debug;
import sun.security.x509.X509CertImpl;

/* loaded from: rt.jar:sun/security/provider/certpath/PKIXCertPathValidator.class */
public final class PKIXCertPathValidator extends CertPathValidatorSpi {
    private static final Debug debug = Debug.getInstance("certpath");

    @Override // java.security.cert.CertPathValidatorSpi
    public CertPathChecker engineGetRevocationChecker() {
        return new RevocationChecker();
    }

    @Override // java.security.cert.CertPathValidatorSpi
    public CertPathValidatorResult engineValidate(CertPath certPath, CertPathParameters certPathParameters) throws CertPathValidatorException, InvalidAlgorithmParameterException {
        return validate(PKIX.checkParams(certPath, certPathParameters));
    }

    private static PKIXCertPathValidatorResult validate(PKIX.ValidatorParams validatorParams) throws CertPathValidatorException {
        if (debug != null) {
            debug.println("PKIXCertPathValidator.engineValidate()...");
        }
        AdaptableX509CertSelector adaptableX509CertSelector = null;
        List<X509Certificate> listCertificates = validatorParams.certificates();
        if (!listCertificates.isEmpty()) {
            adaptableX509CertSelector = new AdaptableX509CertSelector();
            X509Certificate x509Certificate = listCertificates.get(0);
            adaptableX509CertSelector.setSubject(x509Certificate.getIssuerX500Principal());
            try {
                adaptableX509CertSelector.setSkiAndSerialNumber(X509CertImpl.toImpl(x509Certificate).getAuthorityKeyIdentifierExtension());
            } catch (IOException | CertificateException e2) {
            }
        }
        CertPathValidatorException certPathValidatorException = null;
        for (TrustAnchor trustAnchor : validatorParams.trustAnchors()) {
            X509Certificate trustedCert = trustAnchor.getTrustedCert();
            if (trustedCert != null) {
                if (adaptableX509CertSelector != null && !adaptableX509CertSelector.match(trustedCert)) {
                    if (debug != null) {
                        debug.println("NO - don't try this trustedCert");
                    }
                } else if (debug != null) {
                    debug.println("YES - try this trustedCert");
                    debug.println("anchor.getTrustedCert().getSubjectX500Principal() = " + ((Object) trustedCert.getSubjectX500Principal()));
                }
            } else if (debug != null) {
                debug.println("PKIXCertPathValidator.engineValidate(): anchor.getTrustedCert() == null");
            }
            try {
                return validate(trustAnchor, validatorParams);
            } catch (CertPathValidatorException e3) {
                certPathValidatorException = e3;
            }
        }
        if (certPathValidatorException != null) {
            throw certPathValidatorException;
        }
        throw new CertPathValidatorException("Path does not chain with any of the trust anchors", null, null, -1, PKIXReason.NO_TRUST_ANCHOR);
    }

    private static PKIXCertPathValidatorResult validate(TrustAnchor trustAnchor, PKIX.ValidatorParams validatorParams) throws CertPathValidatorException {
        UntrustedChecker untrustedChecker = new UntrustedChecker();
        X509Certificate trustedCert = trustAnchor.getTrustedCert();
        if (trustedCert != null) {
            untrustedChecker.check(trustedCert);
        }
        int size = validatorParams.certificates().size();
        ArrayList arrayList = new ArrayList();
        arrayList.add(untrustedChecker);
        arrayList.add(new AlgorithmChecker(trustAnchor, null, validatorParams.timestamp(), validatorParams.variant()));
        arrayList.add(new KeyChecker(size, validatorParams.targetCertConstraints()));
        arrayList.add(new ConstraintsChecker(size));
        PolicyChecker policyChecker = new PolicyChecker(validatorParams.initialPolicies(), size, validatorParams.explicitPolicyRequired(), validatorParams.policyMappingInhibited(), validatorParams.anyPolicyInhibited(), validatorParams.policyQualifiersRejected(), new PolicyNodeImpl(null, "2.5.29.32.0", null, false, Collections.singleton("2.5.29.32.0"), false));
        arrayList.add(policyChecker);
        BasicChecker basicChecker = new BasicChecker(trustAnchor, validatorParams.date(), validatorParams.sigProvider(), false);
        arrayList.add(basicChecker);
        boolean z2 = false;
        List<PKIXCertPathChecker> listCertPathCheckers = validatorParams.certPathCheckers();
        for (PKIXCertPathChecker pKIXCertPathChecker : listCertPathCheckers) {
            if (pKIXCertPathChecker instanceof PKIXRevocationChecker) {
                if (z2) {
                    throw new CertPathValidatorException("Only one PKIXRevocationChecker can be specified");
                }
                z2 = true;
                if (pKIXCertPathChecker instanceof RevocationChecker) {
                    ((RevocationChecker) pKIXCertPathChecker).init(trustAnchor, validatorParams);
                }
            }
        }
        if (validatorParams.revocationEnabled() && !z2) {
            arrayList.add(new RevocationChecker(trustAnchor, validatorParams));
        }
        arrayList.addAll(listCertPathCheckers);
        PKIXMasterCertPathValidator.validate(validatorParams.certPath(), validatorParams.certificates(), arrayList);
        return new PKIXCertPathValidatorResult(trustAnchor, policyChecker.getPolicyTree(), basicChecker.getPublicKey());
    }
}
