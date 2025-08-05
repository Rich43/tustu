package sun.security.ssl;

import java.security.AccessControlContext;
import java.security.Permission;
import java.security.Principal;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;

/* loaded from: jsse.jar:sun/security/ssl/Krb5Proxy.class */
public interface Krb5Proxy {
    Subject getClientSubject(AccessControlContext accessControlContext) throws LoginException;

    Subject getServerSubject(AccessControlContext accessControlContext) throws LoginException;

    Object getServiceCreds(AccessControlContext accessControlContext) throws LoginException;

    String getServerPrincipalName(Object obj);

    String getPrincipalHostName(Principal principal);

    Permission getServicePermission(String str, String str2);

    boolean isRelated(Subject subject, Principal principal);
}
