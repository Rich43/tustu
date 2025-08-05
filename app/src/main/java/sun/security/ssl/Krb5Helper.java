package sun.security.ssl;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permission;
import java.security.Principal;
import java.security.PrivilegedAction;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;

/* loaded from: jsse.jar:sun/security/ssl/Krb5Helper.class */
public final class Krb5Helper {
    private static final String IMPL_CLASS = "sun.security.ssl.krb5.Krb5ProxyImpl";
    private static final Krb5Proxy proxy = (Krb5Proxy) AccessController.doPrivileged(new PrivilegedAction<Krb5Proxy>() { // from class: sun.security.ssl.Krb5Helper.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public Krb5Proxy run2() {
            try {
                return (Krb5Proxy) Class.forName(Krb5Helper.IMPL_CLASS, true, null).newInstance();
            } catch (ClassNotFoundException e2) {
                return null;
            } catch (IllegalAccessException e3) {
                throw new AssertionError(e3);
            } catch (InstantiationException e4) {
                throw new AssertionError(e4);
            }
        }
    });

    private Krb5Helper() {
    }

    private static void ensureAvailable() {
        if (proxy == null) {
            throw new AssertionError((Object) "Kerberos should be available");
        }
    }

    public static Subject getClientSubject(AccessControlContext accessControlContext) throws LoginException {
        ensureAvailable();
        return proxy.getClientSubject(accessControlContext);
    }

    public static Subject getServerSubject(AccessControlContext accessControlContext) throws LoginException {
        ensureAvailable();
        return proxy.getServerSubject(accessControlContext);
    }

    public static Object getServiceCreds(AccessControlContext accessControlContext) throws LoginException {
        ensureAvailable();
        return proxy.getServiceCreds(accessControlContext);
    }

    public static String getServerPrincipalName(Object obj) {
        ensureAvailable();
        return proxy.getServerPrincipalName(obj);
    }

    public static String getPrincipalHostName(Principal principal) {
        ensureAvailable();
        return proxy.getPrincipalHostName(principal);
    }

    public static Permission getServicePermission(String str, String str2) {
        ensureAvailable();
        return proxy.getServicePermission(str, str2);
    }

    public static boolean isRelated(Subject subject, Principal principal) {
        ensureAvailable();
        return proxy.isRelated(subject, principal);
    }
}
