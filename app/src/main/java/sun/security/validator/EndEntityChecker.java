package sun.security.validator;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* loaded from: rt.jar:sun/security/validator/EndEntityChecker.class */
class EndEntityChecker {
    private static final String OID_EXTENDED_KEY_USAGE = "2.5.29.37";
    private static final String OID_EKU_TLS_SERVER = "1.3.6.1.5.5.7.3.1";
    private static final String OID_EKU_TLS_CLIENT = "1.3.6.1.5.5.7.3.2";
    private static final String OID_EKU_CODE_SIGNING = "1.3.6.1.5.5.7.3.3";
    private static final String OID_EKU_TIME_STAMPING = "1.3.6.1.5.5.7.3.8";
    private static final String OID_EKU_ANY_USAGE = "2.5.29.37.0";
    private static final String OID_EKU_NS_SGC = "2.16.840.1.113730.4.1";
    private static final String OID_EKU_MS_SGC = "1.3.6.1.4.1.311.10.3.3";
    private static final String OID_SUBJECT_ALT_NAME = "2.5.29.17";
    private static final String NSCT_SSL_CLIENT = "ssl_client";
    private static final String NSCT_SSL_SERVER = "ssl_server";
    private static final String NSCT_CODE_SIGNING = "object_signing";
    private static final int KU_SIGNATURE = 0;
    private static final int KU_KEY_ENCIPHERMENT = 2;
    private static final int KU_KEY_AGREEMENT = 4;
    private static final Collection<String> KU_SERVER_SIGNATURE = Arrays.asList("DHE_DSS", "DHE_RSA", "ECDHE_ECDSA", "ECDHE_RSA", "RSA_EXPORT", "UNKNOWN");
    private static final Collection<String> KU_SERVER_ENCRYPTION = Arrays.asList("RSA");
    private static final Collection<String> KU_SERVER_KEY_AGREEMENT = Arrays.asList("DH_DSS", "DH_RSA", "ECDH_ECDSA", "ECDH_RSA");
    private final String variant;
    private final String type;

    private EndEntityChecker(String str, String str2) {
        this.type = str;
        this.variant = str2;
    }

    static EndEntityChecker getInstance(String str, String str2) {
        return new EndEntityChecker(str, str2);
    }

    void check(X509Certificate[] x509CertificateArr, Object obj, boolean z2) throws CertificateException {
        if (this.variant.equals(Validator.VAR_GENERIC)) {
            return;
        }
        Set<String> criticalExtensions = getCriticalExtensions(x509CertificateArr[0]);
        if (this.variant.equals(Validator.VAR_TLS_SERVER)) {
            checkTLSServer(x509CertificateArr[0], (String) obj, criticalExtensions);
        } else if (this.variant.equals(Validator.VAR_TLS_CLIENT)) {
            checkTLSClient(x509CertificateArr[0], criticalExtensions);
        } else if (this.variant.equals(Validator.VAR_CODE_SIGNING) || this.variant.equals(Validator.VAR_JCE_SIGNING) || this.variant.equals(Validator.VAR_PLUGIN_CODE_SIGNING)) {
            checkCodeSigning(x509CertificateArr[0], criticalExtensions);
        } else if (this.variant.equals(Validator.VAR_TSA_SERVER)) {
            checkTSAServer(x509CertificateArr[0], criticalExtensions);
        } else {
            throw new CertificateException("Unknown variant: " + this.variant);
        }
        if (z2) {
            checkRemainingExtensions(criticalExtensions);
        }
        Iterator<CADistrustPolicy> it = CADistrustPolicy.POLICIES.iterator();
        while (it.hasNext()) {
            it.next().checkDistrust(this.variant, x509CertificateArr);
        }
    }

    private Set<String> getCriticalExtensions(X509Certificate x509Certificate) {
        Set<String> criticalExtensionOIDs = x509Certificate.getCriticalExtensionOIDs();
        if (criticalExtensionOIDs == null) {
            criticalExtensionOIDs = Collections.emptySet();
        }
        return criticalExtensionOIDs;
    }

    private void checkRemainingExtensions(Set<String> set) throws CertificateException {
        set.remove("2.5.29.19");
        set.remove(OID_SUBJECT_ALT_NAME);
        if (!set.isEmpty()) {
            throw new CertificateException("Certificate contains unsupported critical extensions: " + ((Object) set));
        }
    }

    private boolean checkEKU(X509Certificate x509Certificate, Set<String> set, String str) throws CertificateException {
        List<String> extendedKeyUsage = x509Certificate.getExtendedKeyUsage();
        return extendedKeyUsage == null || extendedKeyUsage.contains(str) || extendedKeyUsage.contains(OID_EKU_ANY_USAGE);
    }

    private boolean checkKeyUsage(X509Certificate x509Certificate, int i2) throws CertificateException {
        boolean[] keyUsage = x509Certificate.getKeyUsage();
        if (keyUsage == null) {
            return true;
        }
        return keyUsage.length > i2 && keyUsage[i2];
    }

    private void checkTLSClient(X509Certificate x509Certificate, Set<String> set) throws CertificateException {
        if (!checkKeyUsage(x509Certificate, 0)) {
            throw new ValidatorException("KeyUsage does not allow digital signatures", ValidatorException.T_EE_EXTENSIONS, x509Certificate);
        }
        if (!checkEKU(x509Certificate, set, OID_EKU_TLS_CLIENT)) {
            throw new ValidatorException("Extended key usage does not permit use for TLS client authentication", ValidatorException.T_EE_EXTENSIONS, x509Certificate);
        }
        if (!SimpleValidator.getNetscapeCertTypeBit(x509Certificate, "ssl_client")) {
            throw new ValidatorException("Netscape cert type does not permit use for SSL client", ValidatorException.T_EE_EXTENSIONS, x509Certificate);
        }
        set.remove("2.5.29.15");
        set.remove(OID_EXTENDED_KEY_USAGE);
        set.remove("2.16.840.1.113730.1.1");
    }

    private void checkTLSServer(X509Certificate x509Certificate, String str, Set<String> set) throws CertificateException {
        if (KU_SERVER_ENCRYPTION.contains(str)) {
            if (!checkKeyUsage(x509Certificate, 2)) {
                throw new ValidatorException("KeyUsage does not allow key encipherment", ValidatorException.T_EE_EXTENSIONS, x509Certificate);
            }
        } else if (KU_SERVER_SIGNATURE.contains(str)) {
            if (!checkKeyUsage(x509Certificate, 0)) {
                throw new ValidatorException("KeyUsage does not allow digital signatures", ValidatorException.T_EE_EXTENSIONS, x509Certificate);
            }
        } else if (KU_SERVER_KEY_AGREEMENT.contains(str)) {
            if (!checkKeyUsage(x509Certificate, 4)) {
                throw new ValidatorException("KeyUsage does not allow key agreement", ValidatorException.T_EE_EXTENSIONS, x509Certificate);
            }
        } else {
            throw new CertificateException("Unknown authType: " + str);
        }
        if (!checkEKU(x509Certificate, set, OID_EKU_TLS_SERVER) && !checkEKU(x509Certificate, set, OID_EKU_MS_SGC) && !checkEKU(x509Certificate, set, OID_EKU_NS_SGC)) {
            throw new ValidatorException("Extended key usage does not permit use for TLS server authentication", ValidatorException.T_EE_EXTENSIONS, x509Certificate);
        }
        if (!SimpleValidator.getNetscapeCertTypeBit(x509Certificate, "ssl_server")) {
            throw new ValidatorException("Netscape cert type does not permit use for SSL server", ValidatorException.T_EE_EXTENSIONS, x509Certificate);
        }
        set.remove("2.5.29.15");
        set.remove(OID_EXTENDED_KEY_USAGE);
        set.remove("2.16.840.1.113730.1.1");
    }

    private void checkCodeSigning(X509Certificate x509Certificate, Set<String> set) throws CertificateException {
        if (!checkKeyUsage(x509Certificate, 0)) {
            throw new ValidatorException("KeyUsage does not allow digital signatures", ValidatorException.T_EE_EXTENSIONS, x509Certificate);
        }
        if (!checkEKU(x509Certificate, set, OID_EKU_CODE_SIGNING)) {
            throw new ValidatorException("Extended key usage does not permit use for code signing", ValidatorException.T_EE_EXTENSIONS, x509Certificate);
        }
        if (!this.variant.equals(Validator.VAR_JCE_SIGNING)) {
            if (!SimpleValidator.getNetscapeCertTypeBit(x509Certificate, "object_signing")) {
                throw new ValidatorException("Netscape cert type does not permit use for code signing", ValidatorException.T_EE_EXTENSIONS, x509Certificate);
            }
            set.remove("2.16.840.1.113730.1.1");
        }
        set.remove("2.5.29.15");
        set.remove(OID_EXTENDED_KEY_USAGE);
    }

    private void checkTSAServer(X509Certificate x509Certificate, Set<String> set) throws CertificateException {
        if (!checkKeyUsage(x509Certificate, 0)) {
            throw new ValidatorException("KeyUsage does not allow digital signatures", ValidatorException.T_EE_EXTENSIONS, x509Certificate);
        }
        if (x509Certificate.getExtendedKeyUsage() == null) {
            throw new ValidatorException("Certificate does not contain an extended key usage extension required for a TSA server", ValidatorException.T_EE_EXTENSIONS, x509Certificate);
        }
        if (!checkEKU(x509Certificate, set, OID_EKU_TIME_STAMPING)) {
            throw new ValidatorException("Extended key usage does not permit use for TSA server", ValidatorException.T_EE_EXTENSIONS, x509Certificate);
        }
        set.remove("2.5.29.15");
        set.remove(OID_EXTENDED_KEY_USAGE);
    }
}
