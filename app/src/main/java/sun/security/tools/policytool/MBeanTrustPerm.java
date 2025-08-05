package sun.security.tools.policytool;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/MBeanTrustPerm.class */
class MBeanTrustPerm extends Perm {
    public MBeanTrustPerm() {
        super("MBeanTrustPermission", "javax.management.MBeanTrustPermission", new String[]{"register"}, null);
    }
}
