package sun.security.tools.policytool;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/MgmtPerm.class */
class MgmtPerm extends Perm {
    public MgmtPerm() {
        super("ManagementPermission", "java.lang.management.ManagementPermission", new String[]{"control", "monitor"}, null);
    }
}
