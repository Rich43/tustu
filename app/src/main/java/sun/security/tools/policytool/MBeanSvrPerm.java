package sun.security.tools.policytool;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/MBeanSvrPerm.class */
class MBeanSvrPerm extends Perm {
    public MBeanSvrPerm() {
        super("MBeanServerPermission", "javax.management.MBeanServerPermission", new String[]{"createMBeanServer", "findMBeanServer", "newMBeanServer", "releaseMBeanServer"}, null);
    }
}
