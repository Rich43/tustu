package sun.security.tools.policytool;

import sun.security.util.SecurityConstants;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/SocketPerm.class */
class SocketPerm extends Perm {
    public SocketPerm() {
        super("SocketPermission", "java.net.SocketPermission", new String[0], new String[]{SecurityConstants.SOCKET_ACCEPT_ACTION, SecurityConstants.SOCKET_CONNECT_ACTION, SecurityConstants.SOCKET_LISTEN_ACTION, SecurityConstants.SOCKET_RESOLVE_ACTION});
    }
}
