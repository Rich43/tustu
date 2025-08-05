package javax.security.cert;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.security.Security;
import java.util.Date;

/* loaded from: rt.jar:javax/security/cert/X509Certificate.class */
public abstract class X509Certificate extends Certificate {
    private static final String X509_PROVIDER = "cert.provider.x509v1";
    private static String X509Provider = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: javax.security.cert.X509Certificate.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public String run2() {
            return Security.getProperty(X509Certificate.X509_PROVIDER);
        }
    });

    public abstract void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException;

    public abstract void checkValidity(Date date) throws CertificateExpiredException, CertificateNotYetValidException;

    public abstract int getVersion();

    public abstract BigInteger getSerialNumber();

    public abstract Principal getIssuerDN();

    public abstract Principal getSubjectDN();

    public abstract Date getNotBefore();

    public abstract Date getNotAfter();

    public abstract String getSigAlgName();

    public abstract String getSigAlgOID();

    public abstract byte[] getSigAlgParams();

    public static final X509Certificate getInstance(InputStream inputStream) throws CertificateException {
        return getInst(inputStream);
    }

    public static final X509Certificate getInstance(byte[] bArr) throws CertificateException {
        return getInst(bArr);
    }

    private static final X509Certificate getInst(Object obj) throws CertificateException {
        Class<?>[] clsArr;
        String str = X509Provider;
        if (str == null || str.length() == 0) {
            str = "com.sun.security.cert.internal.x509.X509V1CertImpl";
        }
        try {
            if (obj instanceof InputStream) {
                clsArr = new Class[]{InputStream.class};
            } else if (obj instanceof byte[]) {
                clsArr = new Class[]{obj.getClass()};
            } else {
                throw new CertificateException("Unsupported argument type");
            }
            return (X509Certificate) Class.forName(str).getConstructor(clsArr).newInstance(obj);
        } catch (ClassNotFoundException e2) {
            throw new CertificateException("Could not find class: " + ((Object) e2));
        } catch (IllegalAccessException e3) {
            throw new CertificateException("Could not access class: " + ((Object) e3));
        } catch (InstantiationException e4) {
            throw new CertificateException("Problems instantiating: " + ((Object) e4));
        } catch (NoSuchMethodException e5) {
            throw new CertificateException("Could not find class method: " + e5.getMessage());
        } catch (InvocationTargetException e6) {
            throw new CertificateException("InvocationTargetException: " + ((Object) e6.getTargetException()));
        }
    }
}
