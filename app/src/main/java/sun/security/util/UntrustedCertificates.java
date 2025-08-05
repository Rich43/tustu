package sun.security.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Properties;
import sun.security.x509.X509CertImpl;

/* loaded from: rt.jar:sun/security/util/UntrustedCertificates.class */
public final class UntrustedCertificates {
    private static final String ALGORITHM_KEY = "Algorithm";
    private static final String algorithm;
    private static final Debug debug = Debug.getInstance("certpath");
    private static final Properties props = new Properties();

    static {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.security.util.UntrustedCertificates.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                try {
                    FileInputStream fileInputStream = new FileInputStream(new File(System.getProperty("java.home"), "lib/security/blacklisted.certs"));
                    Throwable th = null;
                    try {
                        try {
                            UntrustedCertificates.props.load(fileInputStream);
                            if (fileInputStream != null) {
                                if (0 != 0) {
                                    try {
                                        fileInputStream.close();
                                    } catch (Throwable th2) {
                                        th.addSuppressed(th2);
                                    }
                                } else {
                                    fileInputStream.close();
                                }
                            }
                            return null;
                        } finally {
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        throw th3;
                    }
                } catch (IOException e2) {
                    if (UntrustedCertificates.debug != null) {
                        UntrustedCertificates.debug.println("Error parsing blacklisted.certs");
                        return null;
                    }
                    return null;
                }
            }
        });
        algorithm = props.getProperty("Algorithm");
    }

    public static boolean isUntrusted(X509Certificate x509Certificate) {
        String fingerprint;
        if (algorithm == null) {
            return false;
        }
        if (x509Certificate instanceof X509CertImpl) {
            fingerprint = ((X509CertImpl) x509Certificate).getFingerprint(algorithm);
        } else {
            try {
                fingerprint = new X509CertImpl(x509Certificate.getEncoded()).getFingerprint(algorithm);
            } catch (CertificateException e2) {
                return false;
            }
        }
        return props.containsKey(fingerprint);
    }

    private UntrustedCertificates() {
    }
}
