package sun.security.validator;

import java.security.AlgorithmConstraints;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/* loaded from: rt.jar:sun/security/validator/Validator.class */
public abstract class Validator {
    static final X509Certificate[] CHAIN0 = new X509Certificate[0];
    public static final String TYPE_SIMPLE = "Simple";
    public static final String TYPE_PKIX = "PKIX";
    public static final String VAR_GENERIC = "generic";
    public static final String VAR_CODE_SIGNING = "code signing";
    public static final String VAR_JCE_SIGNING = "jce signing";
    public static final String VAR_TLS_CLIENT = "tls client";
    public static final String VAR_TLS_SERVER = "tls server";
    public static final String VAR_TSA_SERVER = "tsa server";
    public static final String VAR_PLUGIN_CODE_SIGNING = "plugin code signing";
    private final String type;
    final EndEntityChecker endEntityChecker;
    final String variant;

    @Deprecated
    volatile Date validationDate;

    abstract X509Certificate[] engineValidate(X509Certificate[] x509CertificateArr, Collection<X509Certificate> collection, List<byte[]> list, AlgorithmConstraints algorithmConstraints, Object obj) throws CertificateException;

    public abstract Collection<X509Certificate> getTrustedCertificates();

    Validator(String str, String str2) {
        this.type = str;
        this.variant = str2;
        this.endEntityChecker = EndEntityChecker.getInstance(str, str2);
    }

    public static Validator getInstance(String str, String str2, KeyStore keyStore) {
        return getInstance(str, str2, TrustStoreUtil.getTrustedCerts(keyStore));
    }

    public static Validator getInstance(String str, String str2, Collection<X509Certificate> collection) {
        if (str.equals(TYPE_SIMPLE)) {
            return new SimpleValidator(str2, collection);
        }
        if (str.equals(TYPE_PKIX)) {
            return new PKIXValidator(str2, collection);
        }
        throw new IllegalArgumentException("Unknown validator type: " + str);
    }

    public static Validator getInstance(String str, String str2, PKIXBuilderParameters pKIXBuilderParameters) {
        if (!str.equals(TYPE_PKIX)) {
            throw new IllegalArgumentException("getInstance(PKIXBuilderParameters) can only be used with PKIX validator");
        }
        return new PKIXValidator(str2, pKIXBuilderParameters);
    }

    public final X509Certificate[] validate(X509Certificate[] x509CertificateArr) throws CertificateException {
        return validate(x509CertificateArr, null, null);
    }

    public final X509Certificate[] validate(X509Certificate[] x509CertificateArr, Collection<X509Certificate> collection) throws CertificateException {
        return validate(x509CertificateArr, collection, null);
    }

    public final X509Certificate[] validate(X509Certificate[] x509CertificateArr, Collection<X509Certificate> collection, Object obj) throws CertificateException {
        return validate(x509CertificateArr, collection, Collections.emptyList(), null, obj);
    }

    public final X509Certificate[] validate(X509Certificate[] x509CertificateArr, Collection<X509Certificate> collection, List<byte[]> list, AlgorithmConstraints algorithmConstraints, Object obj) throws CertificateException {
        X509Certificate[] x509CertificateArrEngineValidate = engineValidate(x509CertificateArr, collection, list, algorithmConstraints, obj);
        if (x509CertificateArrEngineValidate.length > 1) {
            this.endEntityChecker.check(x509CertificateArrEngineValidate, obj, this.type != TYPE_PKIX);
        }
        return x509CertificateArrEngineValidate;
    }

    @Deprecated
    public void setValidationDate(Date date) {
        this.validationDate = date;
    }
}
