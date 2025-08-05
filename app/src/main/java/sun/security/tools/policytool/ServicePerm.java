package sun.security.tools.policytool;

import sun.security.util.SecurityConstants;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/ServicePerm.class */
class ServicePerm extends Perm {
    public ServicePerm() {
        super("ServicePermission", "javax.security.auth.kerberos.ServicePermission", new String[0], new String[]{"initiate", SecurityConstants.SOCKET_ACCEPT_ACTION});
    }
}
