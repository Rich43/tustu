package sun.security.tools.policytool;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/PropPerm.class */
class PropPerm extends Perm {
    public PropPerm() {
        super("PropertyPermission", "java.util.PropertyPermission", new String[0], new String[]{"read", "write"});
    }
}
