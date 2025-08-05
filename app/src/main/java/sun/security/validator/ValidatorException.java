package sun.security.validator;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/* loaded from: rt.jar:sun/security/validator/ValidatorException.class */
public class ValidatorException extends CertificateException {
    private static final long serialVersionUID = -2836879718282292155L;
    public static final Object T_NO_TRUST_ANCHOR = "No trusted certificate found";
    public static final Object T_EE_EXTENSIONS = "End entity certificate extension check failed";
    public static final Object T_CA_EXTENSIONS = "CA certificate extension check failed";
    public static final Object T_CERT_EXPIRED = "Certificate expired";
    public static final Object T_SIGNATURE_ERROR = "Certificate signature validation failed";
    public static final Object T_NAME_CHAINING = "Certificate chaining error";
    public static final Object T_ALGORITHM_DISABLED = "Certificate signature algorithm disabled";
    public static final Object T_UNTRUSTED_CERT = "Untrusted certificate";
    private Object type;
    private X509Certificate cert;

    public ValidatorException(String str) {
        super(str);
    }

    public ValidatorException(String str, Throwable th) {
        super(str);
        initCause(th);
    }

    public ValidatorException(Object obj) {
        this(obj, (X509Certificate) null);
    }

    public ValidatorException(Object obj, X509Certificate x509Certificate) {
        super((String) obj);
        this.type = obj;
        this.cert = x509Certificate;
    }

    public ValidatorException(Object obj, X509Certificate x509Certificate, Throwable th) {
        this(obj, x509Certificate);
        initCause(th);
    }

    public ValidatorException(String str, Object obj, X509Certificate x509Certificate) {
        super(str);
        this.type = obj;
        this.cert = x509Certificate;
    }

    public ValidatorException(String str, Object obj, X509Certificate x509Certificate, Throwable th) {
        this(str, obj, x509Certificate);
        initCause(th);
    }

    public Object getErrorType() {
        return this.type;
    }

    public X509Certificate getErrorCertificate() {
        return this.cert;
    }
}
