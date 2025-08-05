package sun.security.tools.policytool;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/LogPerm.class */
class LogPerm extends Perm {
    public LogPerm() {
        super("LoggingPermission", "java.util.logging.LoggingPermission", new String[]{"control"}, null);
    }
}
