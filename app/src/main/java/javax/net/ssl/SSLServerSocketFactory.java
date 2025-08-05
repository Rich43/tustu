package javax.net.ssl;

import java.security.NoSuchAlgorithmException;
import javax.net.ServerSocketFactory;

/* loaded from: rt.jar:javax/net/ssl/SSLServerSocketFactory.class */
public abstract class SSLServerSocketFactory extends ServerSocketFactory {
    private static SSLServerSocketFactory theFactory;
    private static boolean propertyChecked;

    public abstract String[] getDefaultCipherSuites();

    public abstract String[] getSupportedCipherSuites();

    private static void log(String str) {
        if (SSLSocketFactory.DEBUG) {
            System.out.println(str);
        }
    }

    protected SSLServerSocketFactory() {
    }

    public static synchronized ServerSocketFactory getDefault() {
        if (theFactory != null) {
            return theFactory;
        }
        if (!propertyChecked) {
            propertyChecked = true;
            String securityProperty = SSLSocketFactory.getSecurityProperty("ssl.ServerSocketFactory.provider");
            if (securityProperty != null) {
                log("setting up default SSLServerSocketFactory");
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
                    SSLServerSocketFactory sSLServerSocketFactory = (SSLServerSocketFactory) clsLoadClass.newInstance();
                    log("instantiated an instance of class " + securityProperty);
                    theFactory = sSLServerSocketFactory;
                    return sSLServerSocketFactory;
                } catch (Exception e3) {
                    log("SSLServerSocketFactory instantiation failed: " + ((Object) e3));
                    theFactory = new DefaultSSLServerSocketFactory(e3);
                    return theFactory;
                }
            }
        }
        try {
            return SSLContext.getDefault().getServerSocketFactory();
        } catch (NoSuchAlgorithmException e4) {
            return new DefaultSSLServerSocketFactory(e4);
        }
    }
}
