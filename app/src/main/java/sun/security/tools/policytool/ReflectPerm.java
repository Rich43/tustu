package sun.security.tools.policytool;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/ReflectPerm.class */
class ReflectPerm extends Perm {
    public ReflectPerm() {
        super("ReflectPermission", "java.lang.reflect.ReflectPermission", new String[]{"suppressAccessChecks"}, null);
    }
}
