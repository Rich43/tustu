package sun.security.tools.policytool;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/Perm.class */
class Perm {
    public final String CLASS;
    public final String FULL_CLASS;
    public final String[] TARGETS;
    public final String[] ACTIONS;

    public Perm(String str, String str2, String[] strArr, String[] strArr2) {
        this.CLASS = str;
        this.FULL_CLASS = str2;
        this.TARGETS = strArr;
        this.ACTIONS = strArr2;
    }
}
