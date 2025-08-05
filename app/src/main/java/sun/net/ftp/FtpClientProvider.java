package sun.net.ftp;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ServiceConfigurationError;
import sun.net.ftp.impl.DefaultFtpClientProvider;

/* loaded from: rt.jar:sun/net/ftp/FtpClientProvider.class */
public abstract class FtpClientProvider {
    private static final Object lock = new Object();
    private static FtpClientProvider provider = null;

    public abstract FtpClient createFtpClient();

    protected FtpClientProvider() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("ftpClientProvider"));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean loadProviderFromProperty() {
        String property = System.getProperty("sun.net.ftpClientProvider");
        if (property == null) {
            return false;
        }
        try {
            provider = (FtpClientProvider) Class.forName(property, true, null).newInstance();
            return true;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SecurityException e2) {
            throw new ServiceConfigurationError(e2.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean loadProviderAsService() {
        return false;
    }

    public static FtpClientProvider provider() {
        synchronized (lock) {
            if (provider != null) {
                return provider;
            }
            return (FtpClientProvider) AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: sun.net.ftp.FtpClientProvider.1
                @Override // java.security.PrivilegedAction
                public Object run() {
                    if (!FtpClientProvider.loadProviderFromProperty() && !FtpClientProvider.loadProviderAsService()) {
                        FtpClientProvider unused = FtpClientProvider.provider = new DefaultFtpClientProvider();
                        return FtpClientProvider.provider;
                    }
                    return FtpClientProvider.provider;
                }
            });
        }
    }
}
