package sun.security.validator;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.EnumSet;
import sun.security.util.Debug;

/* loaded from: rt.jar:sun/security/validator/CADistrustPolicy.class */
enum CADistrustPolicy {
    SYMANTEC_TLS { // from class: sun.security.validator.CADistrustPolicy.1
        @Override // sun.security.validator.CADistrustPolicy
        void checkDistrust(String str, X509Certificate[] x509CertificateArr) throws ValidatorException {
            if (!str.equals(Validator.VAR_TLS_SERVER)) {
                return;
            }
            SymantecTLSPolicy.checkDistrust(x509CertificateArr);
        }
    };

    static final EnumSet<CADistrustPolicy> POLICIES = parseProperty();

    abstract void checkDistrust(String str, X509Certificate[] x509CertificateArr) throws ValidatorException;

    private static EnumSet<CADistrustPolicy> parseProperty() {
        String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: sun.security.validator.CADistrustPolicy.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public String run2() {
                return Security.getProperty("jdk.security.caDistrustPolicies");
            }
        });
        EnumSet<CADistrustPolicy> enumSetNoneOf = EnumSet.noneOf(CADistrustPolicy.class);
        if (str == null || str.isEmpty()) {
            return enumSetNoneOf;
        }
        for (String str2 : str.split(",")) {
            String strTrim = str2.trim();
            try {
                enumSetNoneOf.add((CADistrustPolicy) Enum.valueOf(CADistrustPolicy.class, strTrim));
            } catch (IllegalArgumentException e2) {
                Debug debug = Debug.getInstance("certpath");
                if (debug != null) {
                    debug.println("Unknown value for the jdk.security.caDistrustPolicies property: " + strTrim);
                }
            }
        }
        return enumSetNoneOf;
    }
}
