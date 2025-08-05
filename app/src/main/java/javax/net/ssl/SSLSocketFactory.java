package javax.net.ssl;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.AccessController;
import java.security.NoSuchAlgorithmException;
import java.security.PrivilegedAction;
import java.security.Security;
import java.util.Locale;
import javax.net.SocketFactory;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:javax/net/ssl/SSLSocketFactory.class */
public abstract class SSLSocketFactory extends SocketFactory {
    private static SSLSocketFactory theFactory;
    private static boolean propertyChecked;
    static final boolean DEBUG;

    public abstract String[] getDefaultCipherSuites();

    public abstract String[] getSupportedCipherSuites();

    public abstract Socket createSocket(Socket socket, String str, int i2, boolean z2) throws IOException;

    static {
        String lowerCase = ((String) AccessController.doPrivileged(new GetPropertyAction("javax.net.debug", ""))).toLowerCase(Locale.ENGLISH);
        DEBUG = lowerCase.contains("all") || lowerCase.contains("ssl");
    }

    private static void log(String str) {
        if (DEBUG) {
            System.out.println(str);
        }
    }

    public static synchronized SocketFactory getDefault() {
        if (theFactory != null) {
            return theFactory;
        }
        if (!propertyChecked) {
            propertyChecked = true;
            String securityProperty = getSecurityProperty("ssl.SocketFactory.provider");
            if (securityProperty != null) {
                log("setting up default SSLSocketFactory");
                Class<?> clsLoadClass = null;
                try {
                    try {
                        clsLoadClass = Class.forName(securityProperty);
                    } catch (ClassNotFoundException e2) {
                        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
                        if (systemClassLoader != null) {
                            clsLoadClass = systemClassLoader.loadClass(securityProperty);
                        }
                    }
                    log("class " + securityProperty + " is loaded");
                    SSLSocketFactory sSLSocketFactory = (SSLSocketFactory) clsLoadClass.newInstance();
                    log("instantiated an instance of class " + securityProperty);
                    theFactory = sSLSocketFactory;
                    return sSLSocketFactory;
                } catch (Exception e3) {
                    log("SSLSocketFactory instantiation failed: " + e3.toString());
                    theFactory = new DefaultSSLSocketFactory(e3);
                    return theFactory;
                }
            }
        }
        try {
            return SSLContext.getDefault().getSocketFactory();
        } catch (NoSuchAlgorithmException e4) {
            return new DefaultSSLSocketFactory(e4);
        }
    }

    static String getSecurityProperty(final String str) {
        return (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: javax.net.ssl.SSLSocketFactory.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public String run() {
                String property = Security.getProperty(str);
                if (property != null) {
                    property = property.trim();
                    if (property.length() == 0) {
                        property = null;
                    }
                }
                return property;
            }
        });
    }

    public Socket createSocket(Socket socket, InputStream inputStream, boolean z2) throws IOException {
        throw new UnsupportedOperationException();
    }
}
