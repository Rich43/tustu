package sun.security.tools.policytool;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/PrivCredPerm.class */
class PrivCredPerm extends Perm {
    public PrivCredPerm() {
        super("PrivateCredentialPermission", "javax.security.auth.PrivateCredentialPermission", new String[0], new String[]{"read"});
    }
}
