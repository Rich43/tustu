package sun.net.www.protocol.http;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.PasswordAuthentication;
import java.net.URL;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:sun/net/www/protocol/http/NTLMAuthenticationProxy.class */
class NTLMAuthenticationProxy {
    private static Method supportsTA;
    private static Method isTrustedSite;
    private static final String clazzStr = "sun.net.www.protocol.http.ntlm.NTLMAuthentication";
    private static final String supportsTAStr = "supportsTransparentAuth";
    private static final String isTrustedSiteStr = "isTrustedSite";
    static final NTLMAuthenticationProxy proxy = tryLoadNTLMAuthentication();
    static final boolean supported;
    static final boolean supportsTransparentAuth;
    private final Constructor<? extends AuthenticationInfo> threeArgCtr;
    private final Constructor<? extends AuthenticationInfo> fiveArgCtr;

    static {
        supported = proxy != null;
        supportsTransparentAuth = supported ? supportsTransparentAuth() : false;
    }

    private NTLMAuthenticationProxy(Constructor<? extends AuthenticationInfo> constructor, Constructor<? extends AuthenticationInfo> constructor2) {
        this.threeArgCtr = constructor;
        this.fiveArgCtr = constructor2;
    }

    AuthenticationInfo create(boolean z2, URL url, PasswordAuthentication passwordAuthentication) {
        try {
            return this.threeArgCtr.newInstance(Boolean.valueOf(z2), url, passwordAuthentication);
        } catch (ReflectiveOperationException e2) {
            finest(e2);
            return null;
        }
    }

    AuthenticationInfo create(boolean z2, String str, int i2, PasswordAuthentication passwordAuthentication) {
        try {
            return this.fiveArgCtr.newInstance(Boolean.valueOf(z2), str, Integer.valueOf(i2), passwordAuthentication);
        } catch (ReflectiveOperationException e2) {
            finest(e2);
            return null;
        }
    }

    private static boolean supportsTransparentAuth() {
        try {
            return ((Boolean) supportsTA.invoke(null, new Object[0])).booleanValue();
        } catch (ReflectiveOperationException e2) {
            finest(e2);
            return false;
        }
    }

    public static boolean isTrustedSite(URL url) {
        try {
            return ((Boolean) isTrustedSite.invoke(null, url)).booleanValue();
        } catch (ReflectiveOperationException e2) {
            finest(e2);
            return false;
        }
    }

    private static NTLMAuthenticationProxy tryLoadNTLMAuthentication() throws SecurityException {
        try {
            Class<?> cls = Class.forName(clazzStr, true, null);
            if (cls != null) {
                Constructor<?> constructor = cls.getConstructor(Boolean.TYPE, URL.class, PasswordAuthentication.class);
                Constructor<?> constructor2 = cls.getConstructor(Boolean.TYPE, String.class, Integer.TYPE, PasswordAuthentication.class);
                supportsTA = cls.getDeclaredMethod(supportsTAStr, new Class[0]);
                isTrustedSite = cls.getDeclaredMethod(isTrustedSiteStr, URL.class);
                return new NTLMAuthenticationProxy(constructor, constructor2);
            }
            return null;
        } catch (ClassNotFoundException e2) {
            finest(e2);
            return null;
        } catch (ReflectiveOperationException e3) {
            throw new AssertionError(e3);
        }
    }

    static void finest(Exception exc) {
        PlatformLogger httpLogger = HttpURLConnection.getHttpLogger();
        if (httpLogger.isLoggable(PlatformLogger.Level.FINEST)) {
            httpLogger.finest("NTLMAuthenticationProxy: " + ((Object) exc));
        }
    }
}
