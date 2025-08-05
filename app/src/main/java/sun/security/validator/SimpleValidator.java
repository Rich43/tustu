package sun.security.validator;

import java.io.IOException;
import java.security.AlgorithmConstraints;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertificateException;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import sun.security.provider.certpath.AlgorithmChecker;
import sun.security.provider.certpath.UntrustedChecker;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;
import sun.security.x509.NetscapeCertTypeExtension;
import sun.security.x509.X509CertImpl;

/* loaded from: rt.jar:sun/security/validator/SimpleValidator.class */
public final class SimpleValidator extends Validator {
    static final String OID_BASIC_CONSTRAINTS = "2.5.29.19";
    static final String OID_NETSCAPE_CERT_TYPE = "2.16.840.1.113730.1.1";
    static final String OID_KEY_USAGE = "2.5.29.15";
    static final String OID_EXTENDED_KEY_USAGE = "2.5.29.37";
    static final String OID_EKU_ANY_USAGE = "2.5.29.37.0";
    static final ObjectIdentifier OBJID_NETSCAPE_CERT_TYPE = NetscapeCertTypeExtension.NetscapeCertType_Id;
    private static final String NSCT_SSL_CA = "ssl_ca";
    private static final String NSCT_CODE_SIGNING_CA = "object_signing_ca";
    private final Map<X500Principal, List<X509Certificate>> trustedX500Principals;
    private final Collection<X509Certificate> trustedCerts;

    SimpleValidator(String str, Collection<X509Certificate> collection) {
        super(Validator.TYPE_SIMPLE, str);
        this.trustedCerts = collection;
        this.trustedX500Principals = new HashMap();
        for (X509Certificate x509Certificate : collection) {
            X500Principal subjectX500Principal = x509Certificate.getSubjectX500Principal();
            List<X509Certificate> arrayList = this.trustedX500Principals.get(subjectX500Principal);
            if (arrayList == null) {
                arrayList = new ArrayList(2);
                this.trustedX500Principals.put(subjectX500Principal, arrayList);
            }
            arrayList.add(x509Certificate);
        }
    }

    @Override // sun.security.validator.Validator
    public Collection<X509Certificate> getTrustedCertificates() {
        return this.trustedCerts;
    }

    @Override // sun.security.validator.Validator
    X509Certificate[] engineValidate(X509Certificate[] x509CertificateArr, Collection<X509Certificate> collection, List<byte[]> list, AlgorithmConstraints algorithmConstraints, Object obj) throws CertificateException {
        if (x509CertificateArr == null || x509CertificateArr.length == 0) {
            throw new CertificateException("null or zero-length certificate chain");
        }
        X509Certificate[] x509CertificateArrBuildTrustedChain = buildTrustedChain(x509CertificateArr);
        Date date = this.validationDate;
        if (date == null) {
            date = new Date();
        }
        UntrustedChecker untrustedChecker = new UntrustedChecker();
        X509Certificate x509Certificate = x509CertificateArrBuildTrustedChain[x509CertificateArrBuildTrustedChain.length - 1];
        try {
            untrustedChecker.check(x509Certificate);
            TrustAnchor trustAnchor = new TrustAnchor(x509Certificate, null);
            AlgorithmChecker algorithmChecker = new AlgorithmChecker(trustAnchor, this.variant);
            AlgorithmChecker algorithmChecker2 = null;
            if (algorithmConstraints != null) {
                algorithmChecker2 = new AlgorithmChecker(trustAnchor, algorithmConstraints, null, this.variant);
            }
            int length = x509CertificateArrBuildTrustedChain.length - 1;
            for (int length2 = x509CertificateArrBuildTrustedChain.length - 2; length2 >= 0; length2--) {
                X509Certificate x509Certificate2 = x509CertificateArrBuildTrustedChain[length2 + 1];
                X509Certificate x509Certificate3 = x509CertificateArrBuildTrustedChain[length2];
                try {
                    untrustedChecker.check(x509Certificate3, Collections.emptySet());
                    try {
                        algorithmChecker.check(x509Certificate3, Collections.emptySet());
                        if (algorithmChecker2 != null) {
                            algorithmChecker2.check(x509Certificate3, Collections.emptySet());
                        }
                        if (!this.variant.equals(Validator.VAR_CODE_SIGNING) && !this.variant.equals(Validator.VAR_JCE_SIGNING)) {
                            x509Certificate3.checkValidity(date);
                        }
                        if (!x509Certificate3.getIssuerX500Principal().equals(x509Certificate2.getSubjectX500Principal())) {
                            throw new ValidatorException(ValidatorException.T_NAME_CHAINING, x509Certificate3);
                        }
                        try {
                            x509Certificate3.verify(x509Certificate2.getPublicKey());
                            if (length2 != 0) {
                                length = checkExtensions(x509Certificate3, length);
                            }
                        } catch (GeneralSecurityException e2) {
                            throw new ValidatorException(ValidatorException.T_SIGNATURE_ERROR, x509Certificate3, e2);
                        }
                    } catch (CertPathValidatorException e3) {
                        throw new ValidatorException(ValidatorException.T_ALGORITHM_DISABLED, x509Certificate3, e3);
                    }
                } catch (CertPathValidatorException e4) {
                    throw new ValidatorException("Untrusted certificate: " + ((Object) x509Certificate3.getSubjectX500Principal()), ValidatorException.T_UNTRUSTED_CERT, x509Certificate3, e4);
                }
            }
            return x509CertificateArrBuildTrustedChain;
        } catch (CertPathValidatorException e5) {
            throw new ValidatorException("Untrusted certificate: " + ((Object) x509Certificate.getSubjectX500Principal()), ValidatorException.T_UNTRUSTED_CERT, x509Certificate, e5);
        }
    }

    private int checkExtensions(X509Certificate x509Certificate, int i2) throws CertificateException {
        Set<String> criticalExtensionOIDs = x509Certificate.getCriticalExtensionOIDs();
        if (criticalExtensionOIDs == null) {
            criticalExtensionOIDs = Collections.emptySet();
        }
        int iCheckBasicConstraints = checkBasicConstraints(x509Certificate, criticalExtensionOIDs, i2);
        checkKeyUsage(x509Certificate, criticalExtensionOIDs);
        checkNetscapeCertType(x509Certificate, criticalExtensionOIDs);
        if (!criticalExtensionOIDs.isEmpty()) {
            throw new ValidatorException("Certificate contains unknown critical extensions: " + ((Object) criticalExtensionOIDs), ValidatorException.T_CA_EXTENSIONS, x509Certificate);
        }
        return iCheckBasicConstraints;
    }

    private void checkNetscapeCertType(X509Certificate x509Certificate, Set<String> set) throws CertificateException {
        if (!this.variant.equals(Validator.VAR_GENERIC)) {
            if (this.variant.equals(Validator.VAR_TLS_CLIENT) || this.variant.equals(Validator.VAR_TLS_SERVER)) {
                if (!getNetscapeCertTypeBit(x509Certificate, "ssl_ca")) {
                    throw new ValidatorException("Invalid Netscape CertType extension for SSL CA certificate", ValidatorException.T_CA_EXTENSIONS, x509Certificate);
                }
                set.remove(OID_NETSCAPE_CERT_TYPE);
            } else {
                if (this.variant.equals(Validator.VAR_CODE_SIGNING) || this.variant.equals(Validator.VAR_JCE_SIGNING)) {
                    if (!getNetscapeCertTypeBit(x509Certificate, "object_signing_ca")) {
                        throw new ValidatorException("Invalid Netscape CertType extension for code signing CA certificate", ValidatorException.T_CA_EXTENSIONS, x509Certificate);
                    }
                    set.remove(OID_NETSCAPE_CERT_TYPE);
                    return;
                }
                throw new CertificateException("Unknown variant " + this.variant);
            }
        }
    }

    static boolean getNetscapeCertTypeBit(X509Certificate x509Certificate, String str) {
        NetscapeCertTypeExtension netscapeCertTypeExtension;
        try {
            if (x509Certificate instanceof X509CertImpl) {
                netscapeCertTypeExtension = (NetscapeCertTypeExtension) ((X509CertImpl) x509Certificate).getExtension(OBJID_NETSCAPE_CERT_TYPE);
                if (netscapeCertTypeExtension == null) {
                    return true;
                }
            } else {
                byte[] extensionValue = x509Certificate.getExtensionValue(OID_NETSCAPE_CERT_TYPE);
                if (extensionValue == null) {
                    return true;
                }
                netscapeCertTypeExtension = new NetscapeCertTypeExtension(new DerValue(new DerInputStream(extensionValue).getOctetString()).getUnalignedBitString().toByteArray());
            }
            return netscapeCertTypeExtension.get(str).booleanValue();
        } catch (IOException e2) {
            return false;
        }
    }

    private int checkBasicConstraints(X509Certificate x509Certificate, Set<String> set, int i2) throws CertificateException {
        set.remove(OID_BASIC_CONSTRAINTS);
        int basicConstraints = x509Certificate.getBasicConstraints();
        if (basicConstraints < 0) {
            throw new ValidatorException("End user tried to act as a CA", ValidatorException.T_CA_EXTENSIONS, x509Certificate);
        }
        if (!X509CertImpl.isSelfIssued(x509Certificate)) {
            if (i2 <= 0) {
                throw new ValidatorException("Violated path length constraints", ValidatorException.T_CA_EXTENSIONS, x509Certificate);
            }
            i2--;
        }
        if (i2 > basicConstraints) {
            i2 = basicConstraints;
        }
        return i2;
    }

    private void checkKeyUsage(X509Certificate x509Certificate, Set<String> set) throws CertificateException {
        set.remove(OID_KEY_USAGE);
        set.remove(OID_EXTENDED_KEY_USAGE);
        boolean[] keyUsage = x509Certificate.getKeyUsage();
        if (keyUsage != null) {
            if (keyUsage.length < 6 || !keyUsage[5]) {
                throw new ValidatorException("Wrong key usage: expected keyCertSign", ValidatorException.T_CA_EXTENSIONS, x509Certificate);
            }
        }
    }

    private X509Certificate[] buildTrustedChain(X509Certificate[] x509CertificateArr) throws CertificateException {
        ArrayList arrayList = new ArrayList(x509CertificateArr.length);
        for (X509Certificate x509Certificate : x509CertificateArr) {
            X509Certificate trustedCertificate = getTrustedCertificate(x509Certificate);
            if (trustedCertificate != null) {
                arrayList.add(trustedCertificate);
                return (X509Certificate[]) arrayList.toArray(CHAIN0);
            }
            arrayList.add(x509Certificate);
        }
        X509Certificate x509Certificate2 = x509CertificateArr[x509CertificateArr.length - 1];
        x509Certificate2.getSubjectX500Principal();
        List<X509Certificate> list = this.trustedX500Principals.get(x509Certificate2.getIssuerX500Principal());
        if (list != null) {
            arrayList.add(list.iterator().next());
            return (X509Certificate[]) arrayList.toArray(CHAIN0);
        }
        throw new ValidatorException(ValidatorException.T_NO_TRUST_ANCHOR);
    }

    private X509Certificate getTrustedCertificate(X509Certificate x509Certificate) {
        List<X509Certificate> list = this.trustedX500Principals.get(x509Certificate.getSubjectX500Principal());
        if (list == null) {
            return null;
        }
        X500Principal issuerX500Principal = x509Certificate.getIssuerX500Principal();
        PublicKey publicKey = x509Certificate.getPublicKey();
        for (X509Certificate x509Certificate2 : list) {
            if (x509Certificate2.equals(x509Certificate)) {
                return x509Certificate;
            }
            if (x509Certificate2.getIssuerX500Principal().equals(issuerX500Principal) && x509Certificate2.getPublicKey().equals(publicKey)) {
                return x509Certificate2;
            }
        }
        return null;
    }
}
