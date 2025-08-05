package javax.crypto;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.cert.Certificate;
import java.util.jar.JarEntry;
import java.util.jar.JarException;
import java.util.jar.JarFile;

/* loaded from: jce.jar:javax/crypto/JarVerifier.class */
final class JarVerifier {
    private URL jarURL;
    private boolean savePerms;
    private CryptoPermissions appPerms = null;

    JarVerifier(URL url, boolean z2) {
        this.jarURL = url;
        this.savePerms = z2;
    }

    void verify() throws IOException {
        if (!this.savePerms) {
            return;
        }
        final URL url = this.jarURL.getProtocol().equalsIgnoreCase("jar") ? this.jarURL : new URL("jar:" + this.jarURL.toString() + "!/");
        JarFile jarFile = null;
        try {
            try {
                jarFile = (JarFile) AccessController.doPrivileged(new PrivilegedExceptionAction<JarFile>() { // from class: javax.crypto.JarVerifier.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public JarFile run() throws Exception {
                        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                        jarURLConnection.setUseCaches(false);
                        return jarURLConnection.getJarFile();
                    }
                });
                if (jarFile != null) {
                    JarEntry jarEntry = jarFile.getJarEntry("cryptoPerms");
                    if (jarEntry == null) {
                        throw new JarException("Can not find cryptoPerms");
                    }
                    try {
                        this.appPerms = new CryptoPermissions();
                        this.appPerms.load(jarFile.getInputStream(jarEntry));
                    } catch (Exception e2) {
                        JarException jarException = new JarException("Cannot load/parse" + this.jarURL.toString());
                        jarException.initCause(e2);
                        throw jarException;
                    }
                }
                if (jarFile != null) {
                    jarFile.close();
                }
            } catch (PrivilegedActionException e3) {
                throw new SecurityException("Cannot load " + url.toString(), e3);
            }
        } catch (Throwable th) {
            if (jarFile != null) {
                jarFile.close();
            }
            throw th;
        }
    }

    static void verifyPolicySigned(Certificate[] certificateArr) throws Exception {
    }

    CryptoPermissions getPermissions() {
        return this.appPerms;
    }
}
