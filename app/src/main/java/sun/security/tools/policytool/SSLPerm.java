package sun.security.tools.policytool;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/SSLPerm.class */
class SSLPerm extends Perm {
    public SSLPerm() {
        super("SSLPermission", "javax.net.ssl.SSLPermission", new String[]{"setHostnameVerifier", "getSSLSessionContext"}, null);
    }
}
