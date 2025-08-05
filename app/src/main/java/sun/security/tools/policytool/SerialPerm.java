package sun.security.tools.policytool;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/SerialPerm.class */
class SerialPerm extends Perm {
    public SerialPerm() {
        super("SerializablePermission", "java.io.SerializablePermission", new String[]{"enableSubclassImplementation", "enableSubstitution"}, null);
    }
}
