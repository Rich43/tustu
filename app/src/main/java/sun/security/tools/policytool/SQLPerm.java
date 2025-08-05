package sun.security.tools.policytool;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/SQLPerm.class */
class SQLPerm extends Perm {
    public SQLPerm() {
        super("SQLPermission", "java.sql.SQLPermission", new String[]{"setLog", "callAbort", "setSyncFactory", "setNetworkTimeout"}, null);
    }
}
