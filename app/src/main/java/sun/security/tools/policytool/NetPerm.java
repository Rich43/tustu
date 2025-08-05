package sun.security.tools.policytool;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/NetPerm.class */
class NetPerm extends Perm {
    public NetPerm() {
        super("NetPermission", "java.net.NetPermission", new String[]{"setDefaultAuthenticator", "requestPasswordAuthentication", "specifyStreamHandler", "setProxySelector", "getProxySelector", "setCookieHandler", "getCookieHandler", "setResponseCache", "getResponseCache"}, null);
    }
}
