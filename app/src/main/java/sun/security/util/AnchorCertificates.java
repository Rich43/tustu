package sun.security.util;

import java.io.File;
import java.io.FileInputStream;
import java.security.AccessController;
import java.security.KeyStore;
import java.security.PrivilegedAction;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import sun.security.x509.X509CertImpl;

/* loaded from: rt.jar:sun/security/util/AnchorCertificates.class */
public class AnchorCertificates {
    private static final String HASH = "SHA-256";
    private static final Debug debug = Debug.getInstance("certpath");
    private static Set<String> certs = Collections.emptySet();
    private static Set<X500Principal> certIssuers = Collections.emptySet();

    static {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.security.util.AnchorCertificates.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                File file = new File(System.getProperty("java.home"), "lib/security/cacerts");
                try {
                    KeyStore keyStore = KeyStore.getInstance("JKS");
                    FileInputStream fileInputStream = new FileInputStream(file);
                    Throwable th = null;
                    try {
                        try {
                            keyStore.load(fileInputStream, null);
                            Set unused = AnchorCertificates.certs = new HashSet();
                            Set unused2 = AnchorCertificates.certIssuers = new HashSet();
                            Enumeration<String> enumerationAliases = keyStore.aliases();
                            while (enumerationAliases.hasMoreElements()) {
                                String strNextElement2 = enumerationAliases.nextElement2();
                                if (strNextElement2.contains(" [jdk")) {
                                    X509Certificate x509Certificate = (X509Certificate) keyStore.getCertificate(strNextElement2);
                                    AnchorCertificates.certs.add(X509CertImpl.getFingerprint(AnchorCertificates.HASH, x509Certificate));
                                    AnchorCertificates.certIssuers.add(x509Certificate.getSubjectX500Principal());
                                }
                            }
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
                } catch (Exception e2) {
                    if (AnchorCertificates.debug != null) {
                        AnchorCertificates.debug.println("Error parsing cacerts");
                        e2.printStackTrace();
                        return null;
                    }
                    return null;
                }
            }
        });
    }

    public static boolean contains(X509Certificate x509Certificate) {
        boolean zContains = certs.contains(X509CertImpl.getFingerprint(HASH, x509Certificate));
        if (zContains && debug != null) {
            debug.println("AnchorCertificate.contains: matched " + ((Object) x509Certificate.getSubjectDN()));
        }
        return zContains;
    }

    public static boolean issuerOf(X509Certificate x509Certificate) {
        return certIssuers.contains(x509Certificate.getIssuerX500Principal());
    }

    private AnchorCertificates() {
    }
}
