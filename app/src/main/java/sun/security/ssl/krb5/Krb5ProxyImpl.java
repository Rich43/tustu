package sun.security.ssl.krb5;

import java.security.AccessControlContext;
import java.security.Permission;
import java.security.Principal;
import java.util.Iterator;
import javax.security.auth.Subject;
import javax.security.auth.kerberos.KeyTab;
import javax.security.auth.kerberos.ServicePermission;
import javax.security.auth.login.LoginException;
import sun.security.jgss.GSSCaller;
import sun.security.jgss.krb5.Krb5Util;
import sun.security.jgss.krb5.ServiceCreds;
import sun.security.krb5.PrincipalName;
import sun.security.ssl.Krb5Proxy;

/* loaded from: jsse.jar:sun/security/ssl/krb5/Krb5ProxyImpl.class */
public class Krb5ProxyImpl implements Krb5Proxy {
    @Override // sun.security.ssl.Krb5Proxy
    public Subject getClientSubject(AccessControlContext accessControlContext) throws LoginException {
        return Krb5Util.getSubject(GSSCaller.CALLER_SSL_CLIENT, accessControlContext);
    }

    @Override // sun.security.ssl.Krb5Proxy
    public Subject getServerSubject(AccessControlContext accessControlContext) throws LoginException {
        return Krb5Util.getSubject(GSSCaller.CALLER_SSL_SERVER, accessControlContext);
    }

    @Override // sun.security.ssl.Krb5Proxy
    public Object getServiceCreds(AccessControlContext accessControlContext) throws LoginException {
        return Krb5Util.getServiceCreds(GSSCaller.CALLER_SSL_SERVER, null, accessControlContext);
    }

    @Override // sun.security.ssl.Krb5Proxy
    public String getServerPrincipalName(Object obj) {
        return ((ServiceCreds) obj).getName();
    }

    @Override // sun.security.ssl.Krb5Proxy
    public String getPrincipalHostName(Principal principal) {
        if (principal == null) {
            return null;
        }
        String str = null;
        try {
            String[] nameStrings = new PrincipalName(principal.getName(), 3).getNameStrings();
            if (nameStrings.length >= 2) {
                str = nameStrings[1];
            }
        } catch (Exception e2) {
        }
        return str;
    }

    @Override // sun.security.ssl.Krb5Proxy
    public Permission getServicePermission(String str, String str2) {
        return new ServicePermission(str, str2);
    }

    @Override // sun.security.ssl.Krb5Proxy
    public boolean isRelated(Subject subject, Principal principal) {
        if (principal == null) {
            return false;
        }
        if (subject.getPrincipals(Principal.class).contains(principal)) {
            return true;
        }
        Iterator it = subject.getPrivateCredentials(KeyTab.class).iterator();
        while (it.hasNext()) {
            if (!((KeyTab) it.next()).isBound()) {
                return true;
            }
        }
        return false;
    }
}
