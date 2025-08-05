package sun.security.tools.policytool;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/InqSecContextPerm.class */
class InqSecContextPerm extends Perm {
    public InqSecContextPerm() {
        super("InquireSecContextPermission", "com.sun.security.jgss.InquireSecContextPermission", new String[]{"KRB5_GET_SESSION_KEY", "KRB5_GET_TKT_FLAGS", "KRB5_GET_AUTHZ_DATA", "KRB5_GET_AUTHTIME"}, null);
    }
}
