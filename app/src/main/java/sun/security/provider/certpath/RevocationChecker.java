package sun.security.provider.certpath;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.AccessController;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivilegedAction;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CRL;
import java.security.cert.CRLException;
import java.security.cert.CRLReason;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertPathBuilderException;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateRevokedException;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.Extension;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.PKIXRevocationChecker;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509CRLSelector;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import sun.security.provider.certpath.OCSP;
import sun.security.provider.certpath.OCSPResponse;
import sun.security.provider.certpath.PKIX;
import sun.security.util.Debug;
import sun.security.validator.Validator;
import sun.security.x509.AccessDescription;
import sun.security.x509.AuthorityInfoAccessExtension;
import sun.security.x509.CRLDistributionPointsExtension;
import sun.security.x509.DistributionPoint;
import sun.security.x509.GeneralName;
import sun.security.x509.GeneralNames;
import sun.security.x509.PKIXExtensions;
import sun.security.x509.X500Name;
import sun.security.x509.X509CRLEntryImpl;
import sun.security.x509.X509CertImpl;

/* loaded from: rt.jar:sun/security/provider/certpath/RevocationChecker.class */
class RevocationChecker extends PKIXRevocationChecker {
    private TrustAnchor anchor;
    private PKIX.ValidatorParams params;
    private boolean onlyEE;
    private boolean softFail;
    private boolean crlDP;
    private URI responderURI;
    private X509Certificate responderCert;
    private List<CertStore> certStores;
    private Map<X509Certificate, byte[]> ocspResponses;
    private List<Extension> ocspExtensions;
    private OCSPResponse.IssuerInfo issuerInfo;
    private PublicKey prevPubKey;
    private boolean crlSignFlag;
    private int certIndex;
    private static final long MAX_CLOCK_SKEW = 900000;
    private static final String HEX_DIGITS = "0123456789ABCDEFabcdef";
    private static final Debug debug = Debug.getInstance("certpath");
    private static final boolean[] ALL_REASONS = {true, true, true, true, true, true, true, true, true};
    private static final boolean[] CRL_SIGN_USAGE = {false, false, false, false, false, false, true};
    private LinkedList<CertPathValidatorException> softFailExceptions = new LinkedList<>();
    private Mode mode = Mode.PREFER_OCSP;
    private final boolean legacy = false;

    /* loaded from: rt.jar:sun/security/provider/certpath/RevocationChecker$Mode.class */
    private enum Mode {
        PREFER_OCSP,
        PREFER_CRLS,
        ONLY_CRLS,
        ONLY_OCSP
    }

    /* loaded from: rt.jar:sun/security/provider/certpath/RevocationChecker$RevocationProperties.class */
    private static class RevocationProperties {
        boolean onlyEE;
        boolean ocspEnabled;
        boolean crlDPEnabled;
        String ocspUrl;
        String ocspSubject;
        String ocspIssuer;
        String ocspSerial;

        private RevocationProperties() {
        }
    }

    RevocationChecker() {
    }

    RevocationChecker(TrustAnchor trustAnchor, PKIX.ValidatorParams validatorParams) throws CertPathValidatorException {
        init(trustAnchor, validatorParams);
    }

    void init(TrustAnchor trustAnchor, PKIX.ValidatorParams validatorParams) throws CertPathValidatorException {
        RevocationProperties revocationProperties = getRevocationProperties();
        URI ocspResponder = getOcspResponder();
        this.responderURI = ocspResponder == null ? toURI(revocationProperties.ocspUrl) : ocspResponder;
        X509Certificate ocspResponderCert = getOcspResponderCert();
        this.responderCert = ocspResponderCert == null ? getResponderCert(revocationProperties, validatorParams.trustAnchors(), validatorParams.certStores()) : ocspResponderCert;
        Set<PKIXRevocationChecker.Option> options = getOptions();
        for (PKIXRevocationChecker.Option option : options) {
            switch (option) {
                case ONLY_END_ENTITY:
                case PREFER_CRLS:
                case SOFT_FAIL:
                case NO_FALLBACK:
                default:
                    throw new CertPathValidatorException("Unrecognized revocation parameter option: " + ((Object) option));
            }
        }
        this.softFail = options.contains(PKIXRevocationChecker.Option.SOFT_FAIL);
        if (this.legacy) {
            this.mode = revocationProperties.ocspEnabled ? Mode.PREFER_OCSP : Mode.ONLY_CRLS;
            this.onlyEE = revocationProperties.onlyEE;
        } else {
            if (options.contains(PKIXRevocationChecker.Option.NO_FALLBACK)) {
                if (options.contains(PKIXRevocationChecker.Option.PREFER_CRLS)) {
                    this.mode = Mode.ONLY_CRLS;
                } else {
                    this.mode = Mode.ONLY_OCSP;
                }
            } else if (options.contains(PKIXRevocationChecker.Option.PREFER_CRLS)) {
                this.mode = Mode.PREFER_CRLS;
            }
            this.onlyEE = options.contains(PKIXRevocationChecker.Option.ONLY_END_ENTITY);
        }
        if (this.legacy) {
            this.crlDP = revocationProperties.crlDPEnabled;
        } else {
            this.crlDP = true;
        }
        this.ocspResponses = getOcspResponses();
        this.ocspExtensions = getOcspExtensions();
        this.anchor = trustAnchor;
        this.params = validatorParams;
        this.certStores = new ArrayList(validatorParams.certStores());
        try {
            this.certStores.add(CertStore.getInstance("Collection", new CollectionCertStoreParameters(validatorParams.certificates())));
        } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException e2) {
            if (debug != null) {
                debug.println("RevocationChecker: error creating Collection CertStore: " + e2);
            }
        }
    }

    private static URI toURI(String str) throws CertPathValidatorException {
        if (str != null) {
            try {
                return new URI(str);
            } catch (URISyntaxException e2) {
                throw new CertPathValidatorException("cannot parse ocsp.responderURL property", e2);
            }
        }
        return null;
    }

    private static RevocationProperties getRevocationProperties() {
        return (RevocationProperties) AccessController.doPrivileged(new PrivilegedAction<RevocationProperties>() { // from class: sun.security.provider.certpath.RevocationChecker.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public RevocationProperties run2() {
                RevocationProperties revocationProperties = new RevocationProperties();
                String property = Security.getProperty("com.sun.security.onlyCheckRevocationOfEECert");
                revocationProperties.onlyEE = property != null && property.equalsIgnoreCase("true");
                String property2 = Security.getProperty("ocsp.enable");
                revocationProperties.ocspEnabled = property2 != null && property2.equalsIgnoreCase("true");
                revocationProperties.ocspUrl = Security.getProperty("ocsp.responderURL");
                revocationProperties.ocspSubject = Security.getProperty("ocsp.responderCertSubjectName");
                revocationProperties.ocspIssuer = Security.getProperty("ocsp.responderCertIssuerName");
                revocationProperties.ocspSerial = Security.getProperty("ocsp.responderCertSerialNumber");
                revocationProperties.crlDPEnabled = Boolean.getBoolean("com.sun.security.enableCRLDP");
                return revocationProperties;
            }
        });
    }

    private static X509Certificate getResponderCert(RevocationProperties revocationProperties, Set<TrustAnchor> set, List<CertStore> list) throws CertPathValidatorException {
        if (revocationProperties.ocspSubject != null) {
            return getResponderCert(revocationProperties.ocspSubject, set, list);
        }
        if (revocationProperties.ocspIssuer != null && revocationProperties.ocspSerial != null) {
            return getResponderCert(revocationProperties.ocspIssuer, revocationProperties.ocspSerial, set, list);
        }
        if (revocationProperties.ocspIssuer != null || revocationProperties.ocspSerial != null) {
            throw new CertPathValidatorException("Must specify both ocsp.responderCertIssuerName and ocsp.responderCertSerialNumber properties");
        }
        return null;
    }

    private static X509Certificate getResponderCert(String str, Set<TrustAnchor> set, List<CertStore> list) throws CertPathValidatorException {
        X509CertSelector x509CertSelector = new X509CertSelector();
        try {
            x509CertSelector.setSubject(new X500Principal(str));
            return getResponderCert(x509CertSelector, set, list);
        } catch (IllegalArgumentException e2) {
            throw new CertPathValidatorException("cannot parse ocsp.responderCertSubjectName property", e2);
        }
    }

    private static X509Certificate getResponderCert(String str, String str2, Set<TrustAnchor> set, List<CertStore> list) throws CertPathValidatorException {
        X509CertSelector x509CertSelector = new X509CertSelector();
        try {
            x509CertSelector.setIssuer(new X500Principal(str));
            try {
                x509CertSelector.setSerialNumber(new BigInteger(stripOutSeparators(str2), 16));
                return getResponderCert(x509CertSelector, set, list);
            } catch (NumberFormatException e2) {
                throw new CertPathValidatorException("cannot parse ocsp.responderCertSerialNumber property", e2);
            }
        } catch (IllegalArgumentException e3) {
            throw new CertPathValidatorException("cannot parse ocsp.responderCertIssuerName property", e3);
        }
    }

    private static X509Certificate getResponderCert(X509CertSelector x509CertSelector, Set<TrustAnchor> set, List<CertStore> list) throws CertPathValidatorException {
        Collection<? extends Certificate> certificates;
        Iterator<TrustAnchor> it = set.iterator();
        while (it.hasNext()) {
            X509Certificate trustedCert = it.next().getTrustedCert();
            if (trustedCert != null && x509CertSelector.match(trustedCert)) {
                return trustedCert;
            }
        }
        Iterator<CertStore> it2 = list.iterator();
        while (it2.hasNext()) {
            try {
                certificates = it2.next().getCertificates(x509CertSelector);
            } catch (CertStoreException e2) {
                if (debug != null) {
                    debug.println("CertStore exception:" + ((Object) e2));
                }
            }
            if (!certificates.isEmpty()) {
                return (X509Certificate) certificates.iterator().next();
            }
            continue;
        }
        throw new CertPathValidatorException("Cannot find the responder's certificate (set using the OCSP security properties).");
    }

    @Override // java.security.cert.PKIXCertPathChecker, java.security.cert.CertPathChecker
    public void init(boolean z2) throws CertPathValidatorException {
        if (z2) {
            throw new CertPathValidatorException("forward checking not supported");
        }
        if (this.anchor != null) {
            this.issuerInfo = new OCSPResponse.IssuerInfo(this.anchor);
            this.prevPubKey = this.issuerInfo.getPublicKey();
        }
        this.crlSignFlag = true;
        if (this.params != null && this.params.certPath() != null) {
            this.certIndex = this.params.certPath().getCertificates().size() - 1;
        } else {
            this.certIndex = -1;
        }
        this.softFailExceptions.clear();
    }

    @Override // java.security.cert.PKIXCertPathChecker, java.security.cert.CertPathChecker
    public boolean isForwardCheckingSupported() {
        return false;
    }

    @Override // java.security.cert.PKIXCertPathChecker
    public Set<String> getSupportedExtensions() {
        return null;
    }

    @Override // java.security.cert.PKIXRevocationChecker
    public List<CertPathValidatorException> getSoftFailExceptions() {
        return Collections.unmodifiableList(this.softFailExceptions);
    }

    @Override // java.security.cert.PKIXCertPathChecker
    public void check(Certificate certificate, Collection<String> collection) throws CertPathValidatorException {
        check((X509Certificate) certificate, collection, this.prevPubKey, this.crlSignFlag);
    }

    private void check(X509Certificate x509Certificate, Collection<String> collection, PublicKey publicKey, boolean z2) throws CertPathValidatorException {
        if (debug != null) {
            debug.println("RevocationChecker.check: checking cert\n  SN: " + Debug.toHexString(x509Certificate.getSerialNumber()) + "\n  Subject: " + ((Object) x509Certificate.getSubjectX500Principal()) + "\n  Issuer: " + ((Object) x509Certificate.getIssuerX500Principal()));
        }
        try {
            try {
                if (this.onlyEE && x509Certificate.getBasicConstraints() != -1) {
                    if (debug != null) {
                        debug.println("Skipping revocation check; cert is not an end entity cert");
                    }
                    updateState(x509Certificate);
                } else {
                    switch (this.mode) {
                        case PREFER_OCSP:
                        case ONLY_OCSP:
                            checkOCSP(x509Certificate, collection);
                            break;
                        case PREFER_CRLS:
                        case ONLY_CRLS:
                            checkCRLs(x509Certificate, collection, null, publicKey, z2);
                            break;
                    }
                    updateState(x509Certificate);
                }
            } catch (CertPathValidatorException e2) {
                if (e2.getReason() == CertPathValidatorException.BasicReason.REVOKED) {
                    throw e2;
                }
                boolean zIsSoftFailException = isSoftFailException(e2);
                if (zIsSoftFailException) {
                    if (this.mode == Mode.ONLY_OCSP || this.mode == Mode.ONLY_CRLS) {
                        updateState(x509Certificate);
                        return;
                    }
                } else if (this.mode == Mode.ONLY_OCSP || this.mode == Mode.ONLY_CRLS) {
                    throw e2;
                }
                if (debug != null) {
                    debug.println("RevocationChecker.check() " + e2.getMessage());
                    debug.println("RevocationChecker.check() preparing to failover");
                }
                try {
                    switch (this.mode) {
                        case PREFER_OCSP:
                            checkCRLs(x509Certificate, collection, null, publicKey, z2);
                            break;
                        case PREFER_CRLS:
                            checkOCSP(x509Certificate, collection);
                            break;
                    }
                } catch (CertPathValidatorException e3) {
                    if (debug != null) {
                        debug.println("RevocationChecker.check() failover failed");
                        debug.println("RevocationChecker.check() " + e3.getMessage());
                    }
                    if (e3.getReason() == CertPathValidatorException.BasicReason.REVOKED) {
                        throw e3;
                    }
                    if (!isSoftFailException(e3)) {
                        e2.addSuppressed(e3);
                        throw e2;
                    }
                    if (!zIsSoftFailException) {
                        throw e2;
                    }
                }
                updateState(x509Certificate);
            }
        } catch (Throwable th) {
            updateState(x509Certificate);
            throw th;
        }
    }

    private boolean isSoftFailException(CertPathValidatorException certPathValidatorException) {
        if (this.softFail && certPathValidatorException.getReason() == CertPathValidatorException.BasicReason.UNDETERMINED_REVOCATION_STATUS) {
            this.softFailExceptions.addFirst(new CertPathValidatorException(certPathValidatorException.getMessage(), certPathValidatorException.getCause(), this.params.certPath(), this.certIndex, certPathValidatorException.getReason()));
            return true;
        }
        return false;
    }

    private void updateState(X509Certificate x509Certificate) throws CertPathValidatorException {
        this.issuerInfo = new OCSPResponse.IssuerInfo(this.anchor, x509Certificate);
        PublicKey publicKey = x509Certificate.getPublicKey();
        if (PKIX.isDSAPublicKeyWithoutParams(publicKey)) {
            publicKey = BasicChecker.makeInheritedParamsKey(publicKey, this.prevPubKey);
        }
        this.prevPubKey = publicKey;
        this.crlSignFlag = certCanSignCrl(x509Certificate);
        if (this.certIndex > 0) {
            this.certIndex--;
        }
    }

    private void checkCRLs(X509Certificate x509Certificate, Collection<String> collection, Set<X509Certificate> set, PublicKey publicKey, boolean z2) throws CertPathValidatorException {
        checkCRLs(x509Certificate, publicKey, null, z2, true, set, this.params.trustAnchors());
    }

    static boolean isCausedByNetworkIssue(String str, CertStoreException certStoreException) {
        Throwable cause;
        boolean z2;
        cause = certStoreException.getCause();
        switch (str) {
            case "LDAP":
                if (cause != null) {
                    String name = cause.getClass().getName();
                    z2 = name.equals("javax.naming.ServiceUnavailableException") || name.equals("javax.naming.CommunicationException");
                    break;
                } else {
                    z2 = false;
                    break;
                }
                break;
            case "SSLServer":
                z2 = cause != null && (cause instanceof IOException);
                break;
            case "URI":
                z2 = cause != null && (cause instanceof IOException);
                break;
            default:
                return false;
        }
        return z2;
    }

    private void checkCRLs(X509Certificate x509Certificate, PublicKey publicKey, X509Certificate x509Certificate2, boolean z2, boolean z3, Set<X509Certificate> set, Set<TrustAnchor> set2) throws CertPathValidatorException {
        if (debug != null) {
            debug.println("RevocationChecker.checkCRLs() ---checking revocation status ...");
        }
        if (set != null && set.contains(x509Certificate)) {
            if (debug != null) {
                debug.println("RevocationChecker.checkCRLs() circular dependency");
            }
            throw new CertPathValidatorException("Could not determine revocation status", null, null, -1, CertPathValidatorException.BasicReason.UNDETERMINED_REVOCATION_STATUS);
        }
        HashSet hashSet = new HashSet();
        HashSet hashSet2 = new HashSet();
        X509CRLSelector x509CRLSelector = new X509CRLSelector();
        x509CRLSelector.setCertificateChecking(x509Certificate);
        CertPathHelper.setDateAndTime(x509CRLSelector, this.params.date(), MAX_CLOCK_SKEW);
        CertPathValidatorException certPathValidatorException = null;
        for (CertStore certStore : this.certStores) {
            try {
                Iterator<? extends CRL> it = certStore.getCRLs(x509CRLSelector).iterator();
                while (it.hasNext()) {
                    hashSet.add((X509CRL) it.next());
                }
            } catch (CertStoreException e2) {
                if (debug != null) {
                    debug.println("RevocationChecker.checkCRLs() CertStoreException: " + e2.getMessage());
                }
                if (certPathValidatorException == null && isCausedByNetworkIssue(certStore.getType(), e2)) {
                    certPathValidatorException = new CertPathValidatorException("Unable to determine revocation status due to network error", e2, null, -1, CertPathValidatorException.BasicReason.UNDETERMINED_REVOCATION_STATUS);
                }
            }
        }
        if (debug != null) {
            debug.println("RevocationChecker.checkCRLs() possible crls.size() = " + hashSet.size());
        }
        boolean[] zArr = new boolean[9];
        if (!hashSet.isEmpty()) {
            hashSet2.addAll(verifyPossibleCRLs(hashSet, x509Certificate, publicKey, z2, zArr, set2));
        }
        if (debug != null) {
            debug.println("RevocationChecker.checkCRLs() approved crls.size() = " + hashSet2.size());
        }
        if (!hashSet2.isEmpty() && Arrays.equals(zArr, ALL_REASONS)) {
            checkApprovedCRLs(x509Certificate, hashSet2);
            return;
        }
        try {
            if (this.crlDP) {
                hashSet2.addAll(DistributionPointFetcher.getCRLs(x509CRLSelector, z2, publicKey, x509Certificate2, this.params.sigProvider(), this.certStores, zArr, set2, null, this.params.variant(), this.anchor));
            }
            if (!hashSet2.isEmpty() && Arrays.equals(zArr, ALL_REASONS)) {
                checkApprovedCRLs(x509Certificate, hashSet2);
                return;
            }
            if (z3) {
                try {
                    verifyWithSeparateSigningKey(x509Certificate, publicKey, z2, set);
                    return;
                } catch (CertPathValidatorException e3) {
                    if (certPathValidatorException != null) {
                        throw certPathValidatorException;
                    }
                    throw e3;
                }
            }
            if (certPathValidatorException != null) {
                throw certPathValidatorException;
            }
            throw new CertPathValidatorException("Could not determine revocation status", null, null, -1, CertPathValidatorException.BasicReason.UNDETERMINED_REVOCATION_STATUS);
        } catch (CertStoreException e4) {
            if ((e4 instanceof PKIX.CertStoreTypeException) && isCausedByNetworkIssue(((PKIX.CertStoreTypeException) e4).getType(), e4)) {
                throw new CertPathValidatorException("Unable to determine revocation status due to network error", e4, null, -1, CertPathValidatorException.BasicReason.UNDETERMINED_REVOCATION_STATUS);
            }
            throw new CertPathValidatorException(e4);
        }
    }

    private void checkApprovedCRLs(X509Certificate x509Certificate, Set<X509CRL> set) throws CertPathValidatorException {
        if (debug != null) {
            BigInteger serialNumber = x509Certificate.getSerialNumber();
            debug.println("RevocationChecker.checkApprovedCRLs() starting the final sweep...");
            debug.println("RevocationChecker.checkApprovedCRLs() cert SN: " + serialNumber.toString());
        }
        CRLReason cRLReason = CRLReason.UNSPECIFIED;
        for (X509CRL x509crl : set) {
            X509CRLEntry revokedCertificate = x509crl.getRevokedCertificate(x509Certificate);
            if (revokedCertificate != null) {
                try {
                    X509CRLEntryImpl impl = X509CRLEntryImpl.toImpl(revokedCertificate);
                    if (debug != null) {
                        debug.println("RevocationChecker.checkApprovedCRLs() CRL entry: " + impl.toString());
                    }
                    Set<String> criticalExtensionOIDs = impl.getCriticalExtensionOIDs();
                    if (criticalExtensionOIDs != null && !criticalExtensionOIDs.isEmpty()) {
                        criticalExtensionOIDs.remove(PKIXExtensions.ReasonCode_Id.toString());
                        criticalExtensionOIDs.remove(PKIXExtensions.CertificateIssuer_Id.toString());
                        if (!criticalExtensionOIDs.isEmpty()) {
                            throw new CertPathValidatorException("Unrecognized critical extension(s) in revoked CRL entry");
                        }
                    }
                    CRLReason revocationReason = impl.getRevocationReason();
                    if (revocationReason == null) {
                        revocationReason = CRLReason.UNSPECIFIED;
                    }
                    Date revocationDate = impl.getRevocationDate();
                    if (revocationDate.before(this.params.date())) {
                        CertificateRevokedException certificateRevokedException = new CertificateRevokedException(revocationDate, revocationReason, x509crl.getIssuerX500Principal(), impl.getExtensions());
                        throw new CertPathValidatorException(certificateRevokedException.getMessage(), certificateRevokedException, null, -1, CertPathValidatorException.BasicReason.REVOKED);
                    }
                } catch (CRLException e2) {
                    throw new CertPathValidatorException(e2);
                }
            }
        }
    }

    private void checkOCSP(X509Certificate x509Certificate, Collection<String> collection) throws CertPathValidatorException {
        OCSPResponse oCSPResponseCheck;
        try {
            X509CertImpl impl = X509CertImpl.toImpl(x509Certificate);
            try {
                CertId certId = new CertId(this.issuerInfo.getName(), this.issuerInfo.getPublicKey(), impl.getSerialNumberObject());
                byte[] bArr = this.ocspResponses.get(x509Certificate);
                if (bArr != null) {
                    if (debug != null) {
                        debug.println("Found cached OCSP response");
                    }
                    oCSPResponseCheck = new OCSPResponse(bArr);
                    byte[] value = null;
                    for (Extension extension : this.ocspExtensions) {
                        if (extension.getId().equals("1.3.6.1.5.5.7.48.1.2")) {
                            value = extension.getValue();
                        }
                    }
                    oCSPResponseCheck.verify(Collections.singletonList(certId), this.issuerInfo, this.responderCert, this.params.date(), value, this.params.variant());
                } else {
                    URI responderURI = this.responderURI != null ? this.responderURI : OCSP.getResponderURI(impl);
                    if (responderURI == null) {
                        throw new CertPathValidatorException("Certificate does not specify OCSP responder", null, null, -1);
                    }
                    oCSPResponseCheck = OCSP.check((List<CertId>) Collections.singletonList(certId), responderURI, this.issuerInfo, this.responderCert, (Date) null, this.ocspExtensions, this.params.variant());
                }
                OCSPResponse.SingleResponse singleResponse = oCSPResponseCheck.getSingleResponse(certId);
                OCSP.RevocationStatus.CertStatus certStatus = singleResponse.getCertStatus();
                if (certStatus != OCSP.RevocationStatus.CertStatus.REVOKED) {
                    if (certStatus == OCSP.RevocationStatus.CertStatus.UNKNOWN) {
                        throw new CertPathValidatorException("Certificate's revocation status is unknown", null, this.params.certPath(), -1, CertPathValidatorException.BasicReason.UNDETERMINED_REVOCATION_STATUS);
                    }
                } else {
                    Date revocationTime = singleResponse.getRevocationTime();
                    if (revocationTime.before(this.params.date())) {
                        CertificateRevokedException certificateRevokedException = new CertificateRevokedException(revocationTime, singleResponse.getRevocationReason(), oCSPResponseCheck.getSignerCertificate().getSubjectX500Principal(), singleResponse.getSingleExtensions());
                        throw new CertPathValidatorException(certificateRevokedException.getMessage(), certificateRevokedException, null, -1, CertPathValidatorException.BasicReason.REVOKED);
                    }
                }
            } catch (IOException e2) {
                throw new CertPathValidatorException("Unable to determine revocation status due to network error", e2, null, -1, CertPathValidatorException.BasicReason.UNDETERMINED_REVOCATION_STATUS);
            }
        } catch (CertificateException e3) {
            throw new CertPathValidatorException(e3);
        }
    }

    private static String stripOutSeparators(String str) {
        char[] charArray = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < charArray.length; i2++) {
            if (HEX_DIGITS.indexOf(charArray[i2]) != -1) {
                sb.append(charArray[i2]);
            }
        }
        return sb.toString();
    }

    static boolean certCanSignCrl(X509Certificate x509Certificate) {
        boolean[] keyUsage = x509Certificate.getKeyUsage();
        if (keyUsage != null) {
            return keyUsage[6];
        }
        return false;
    }

    private Collection<X509CRL> verifyPossibleCRLs(Set<X509CRL> set, X509Certificate x509Certificate, PublicKey publicKey, boolean z2, boolean[] zArr, Set<TrustAnchor> set2) throws CertPathValidatorException {
        List<DistributionPoint> listSingletonList;
        try {
            X509CertImpl impl = X509CertImpl.toImpl(x509Certificate);
            if (debug != null) {
                debug.println("RevocationChecker.verifyPossibleCRLs: Checking CRLDPs for " + ((Object) impl.getSubjectX500Principal()));
            }
            CRLDistributionPointsExtension cRLDistributionPointsExtension = impl.getCRLDistributionPointsExtension();
            if (cRLDistributionPointsExtension == null) {
                listSingletonList = Collections.singletonList(new DistributionPoint(new GeneralNames().add(new GeneralName((X500Name) impl.getIssuerDN())), (boolean[]) null, (GeneralNames) null));
            } else {
                listSingletonList = cRLDistributionPointsExtension.get(CRLDistributionPointsExtension.POINTS);
            }
            HashSet hashSet = new HashSet();
            for (DistributionPoint distributionPoint : listSingletonList) {
                for (X509CRL x509crl : set) {
                    if (DistributionPointFetcher.verifyCRL(impl, distributionPoint, x509crl, zArr, z2, publicKey, null, this.params.sigProvider(), set2, this.certStores, this.params.date(), this.params.variant(), this.anchor)) {
                        hashSet.add(x509crl);
                    }
                }
                if (Arrays.equals(zArr, ALL_REASONS)) {
                    break;
                }
            }
            return hashSet;
        } catch (IOException | CRLException | CertificateException e2) {
            if (debug != null) {
                debug.println("Exception while verifying CRL: " + e2.getMessage());
                e2.printStackTrace();
            }
            return Collections.emptySet();
        }
    }

    private void verifyWithSeparateSigningKey(X509Certificate x509Certificate, PublicKey publicKey, boolean z2, Set<X509Certificate> set) throws CertPathValidatorException {
        if (debug != null) {
            debug.println("RevocationChecker.verifyWithSeparateSigningKey() ---checking revocation status...");
        }
        if (set != null && set.contains(x509Certificate)) {
            if (debug != null) {
                debug.println("RevocationChecker.verifyWithSeparateSigningKey() circular dependency");
            }
            throw new CertPathValidatorException("Could not determine revocation status", null, null, -1, CertPathValidatorException.BasicReason.UNDETERMINED_REVOCATION_STATUS);
        }
        if (!z2) {
            buildToNewKey(x509Certificate, null, set);
        } else {
            buildToNewKey(x509Certificate, publicKey, set);
        }
    }

    private void buildToNewKey(X509Certificate x509Certificate, PublicKey publicKey, Set<X509Certificate> set) throws CertPathValidatorException {
        Set<TrustAnchor> setSingleton;
        List<AccessDescription> accessDescriptions;
        if (debug != null) {
            debug.println("RevocationChecker.buildToNewKey() starting work");
        }
        HashSet hashSet = new HashSet();
        if (publicKey != null) {
            hashSet.add(publicKey);
        }
        RejectKeySelector rejectKeySelector = new RejectKeySelector(hashSet);
        rejectKeySelector.setSubject(x509Certificate.getIssuerX500Principal());
        rejectKeySelector.setKeyUsage(CRL_SIGN_USAGE);
        if (this.anchor == null) {
            setSingleton = this.params.trustAnchors();
        } else {
            setSingleton = Collections.singleton(this.anchor);
        }
        Set<TrustAnchor> set2 = setSingleton;
        try {
            PKIXBuilderParameters pKIXBuilderParameters = new PKIXBuilderParameters(set2, rejectKeySelector);
            pKIXBuilderParameters.setInitialPolicies(this.params.initialPolicies());
            pKIXBuilderParameters.setCertStores(this.certStores);
            pKIXBuilderParameters.setExplicitPolicyRequired(this.params.explicitPolicyRequired());
            pKIXBuilderParameters.setPolicyMappingInhibited(this.params.policyMappingInhibited());
            pKIXBuilderParameters.setAnyPolicyInhibited(this.params.anyPolicyInhibited());
            pKIXBuilderParameters.setDate(this.params.date());
            pKIXBuilderParameters.setCertPathCheckers(this.params.certPathCheckers());
            pKIXBuilderParameters.setSigProvider(this.params.sigProvider());
            pKIXBuilderParameters.setRevocationEnabled(false);
            if (Builder.USE_AIA) {
                X509CertImpl impl = null;
                try {
                    impl = X509CertImpl.toImpl(x509Certificate);
                } catch (CertificateException e2) {
                    if (debug != null) {
                        debug.println("RevocationChecker.buildToNewKey: error decoding cert: " + ((Object) e2));
                    }
                }
                AuthorityInfoAccessExtension authorityInfoAccessExtension = null;
                if (impl != null) {
                    authorityInfoAccessExtension = impl.getAuthorityInfoAccessExtension();
                }
                if (authorityInfoAccessExtension != null && (accessDescriptions = authorityInfoAccessExtension.getAccessDescriptions()) != null) {
                    Iterator<AccessDescription> it = accessDescriptions.iterator();
                    while (it.hasNext()) {
                        CertStore uRICertStore = URICertStore.getInstance(it.next());
                        if (uRICertStore != null) {
                            if (debug != null) {
                                debug.println("adding AIAext CertStore");
                            }
                            pKIXBuilderParameters.addCertStore(uRICertStore);
                        }
                    }
                }
            }
            try {
                CertPathBuilder certPathBuilder = CertPathBuilder.getInstance(Validator.TYPE_PKIX);
                while (true) {
                    try {
                        if (debug != null) {
                            debug.println("RevocationChecker.buildToNewKey() about to try build ...");
                        }
                        PKIXCertPathBuilderResult pKIXCertPathBuilderResult = (PKIXCertPathBuilderResult) certPathBuilder.build(pKIXBuilderParameters);
                        if (debug != null) {
                            debug.println("RevocationChecker.buildToNewKey() about to check revocation ...");
                        }
                        if (set == null) {
                            set = new HashSet();
                        }
                        set.add(x509Certificate);
                        TrustAnchor trustAnchor = pKIXCertPathBuilderResult.getTrustAnchor();
                        PublicKey cAPublicKey = trustAnchor.getCAPublicKey();
                        if (cAPublicKey == null) {
                            cAPublicKey = trustAnchor.getTrustedCert().getPublicKey();
                        }
                        boolean zCertCanSignCrl = true;
                        List<? extends Certificate> certificates = pKIXCertPathBuilderResult.getCertPath().getCertificates();
                        try {
                            for (int size = certificates.size() - 1; size >= 0; size--) {
                                X509Certificate x509Certificate2 = (X509Certificate) certificates.get(size);
                                if (debug != null) {
                                    debug.println("RevocationChecker.buildToNewKey() index " + size + " checking " + ((Object) x509Certificate2));
                                }
                                checkCRLs(x509Certificate2, cAPublicKey, null, zCertCanSignCrl, true, set, set2);
                                zCertCanSignCrl = certCanSignCrl(x509Certificate2);
                                cAPublicKey = x509Certificate2.getPublicKey();
                            }
                            if (debug != null) {
                                debug.println("RevocationChecker.buildToNewKey() got key " + ((Object) pKIXCertPathBuilderResult.getPublicKey()));
                            }
                            PublicKey publicKey2 = pKIXCertPathBuilderResult.getPublicKey();
                            try {
                                checkCRLs(x509Certificate, publicKey2, certificates.isEmpty() ? null : (X509Certificate) certificates.get(0), true, false, null, this.params.trustAnchors());
                                return;
                            } catch (CertPathValidatorException e3) {
                                if (e3.getReason() == CertPathValidatorException.BasicReason.REVOKED) {
                                    throw e3;
                                }
                                hashSet.add(publicKey2);
                            }
                        } catch (CertPathValidatorException e4) {
                            hashSet.add(pKIXCertPathBuilderResult.getPublicKey());
                        }
                    } catch (InvalidAlgorithmParameterException e5) {
                        throw new CertPathValidatorException(e5);
                    } catch (CertPathBuilderException e6) {
                        throw new CertPathValidatorException("Could not determine revocation status", null, null, -1, CertPathValidatorException.BasicReason.UNDETERMINED_REVOCATION_STATUS);
                    }
                }
            } catch (NoSuchAlgorithmException e7) {
                throw new CertPathValidatorException(e7);
            }
        } catch (InvalidAlgorithmParameterException e8) {
            throw new RuntimeException(e8);
        }
    }

    /* loaded from: rt.jar:sun/security/provider/certpath/RevocationChecker$RejectKeySelector.class */
    private static class RejectKeySelector extends X509CertSelector {
        private final Set<PublicKey> badKeySet;

        RejectKeySelector(Set<PublicKey> set) {
            this.badKeySet = set;
        }

        @Override // java.security.cert.X509CertSelector, java.security.cert.CertSelector
        public boolean match(Certificate certificate) {
            if (!super.match(certificate)) {
                return false;
            }
            if (this.badKeySet.contains(certificate.getPublicKey())) {
                if (RevocationChecker.debug != null) {
                    RevocationChecker.debug.println("RejectKeySelector.match: bad key");
                    return false;
                }
                return false;
            }
            if (RevocationChecker.debug != null) {
                RevocationChecker.debug.println("RejectKeySelector.match: returning true");
                return true;
            }
            return true;
        }

        @Override // java.security.cert.X509CertSelector
        public String toString() {
            return "RejectKeySelector: [\n" + super.toString() + ((Object) this.badKeySet) + "]";
        }
    }
}
