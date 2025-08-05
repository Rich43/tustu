package java.security.cert;

import java.security.AccessController;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivilegedAction;
import java.security.Provider;
import java.security.Security;
import sun.security.jca.GetInstance;
import sun.security.validator.Validator;

/* loaded from: rt.jar:java/security/cert/CertPathValidator.class */
public class CertPathValidator {
    private static final String CPV_TYPE = "certpathvalidator.type";
    private final CertPathValidatorSpi validatorSpi;
    private final Provider provider;
    private final String algorithm;

    protected CertPathValidator(CertPathValidatorSpi certPathValidatorSpi, Provider provider, String str) {
        this.validatorSpi = certPathValidatorSpi;
        this.provider = provider;
        this.algorithm = str;
    }

    public static CertPathValidator getInstance(String str) throws NoSuchAlgorithmException {
        GetInstance.Instance getInstance = GetInstance.getInstance("CertPathValidator", (Class<?>) CertPathValidatorSpi.class, str);
        return new CertPathValidator((CertPathValidatorSpi) getInstance.impl, getInstance.provider, str);
    }

    public static CertPathValidator getInstance(String str, String str2) throws NoSuchAlgorithmException, NoSuchProviderException {
        GetInstance.Instance getInstance = GetInstance.getInstance("CertPathValidator", (Class<?>) CertPathValidatorSpi.class, str, str2);
        return new CertPathValidator((CertPathValidatorSpi) getInstance.impl, getInstance.provider, str);
    }

    public static CertPathValidator getInstance(String str, Provider provider) throws NoSuchAlgorithmException {
        GetInstance.Instance getInstance = GetInstance.getInstance("CertPathValidator", (Class<?>) CertPathValidatorSpi.class, str, provider);
        return new CertPathValidator((CertPathValidatorSpi) getInstance.impl, getInstance.provider, str);
    }

    public final Provider getProvider() {
        return this.provider;
    }

    public final String getAlgorithm() {
        return this.algorithm;
    }

    public final CertPathValidatorResult validate(CertPath certPath, CertPathParameters certPathParameters) throws CertPathValidatorException, InvalidAlgorithmParameterException {
        return this.validatorSpi.engineValidate(certPath, certPathParameters);
    }

    public static final String getDefaultType() {
        String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: java.security.cert.CertPathValidator.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public String run2() {
                return Security.getProperty(CertPathValidator.CPV_TYPE);
            }
        });
        return str == null ? Validator.TYPE_PKIX : str;
    }

    public final CertPathChecker getRevocationChecker() {
        return this.validatorSpi.engineGetRevocationChecker();
    }
}
