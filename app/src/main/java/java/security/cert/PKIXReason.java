package java.security.cert;

import java.security.cert.CertPathValidatorException;

/* loaded from: rt.jar:java/security/cert/PKIXReason.class */
public enum PKIXReason implements CertPathValidatorException.Reason {
    NAME_CHAINING,
    INVALID_KEY_USAGE,
    INVALID_POLICY,
    NO_TRUST_ANCHOR,
    UNRECOGNIZED_CRIT_EXT,
    NOT_CA_CERT,
    PATH_TOO_LONG,
    INVALID_NAME
}
