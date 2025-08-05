package sun.security.action;

import java.security.PrivilegedAction;

/* loaded from: rt.jar:sun/security/action/GetBooleanSecurityPropertyAction.class */
public class GetBooleanSecurityPropertyAction implements PrivilegedAction<Boolean> {
    private String theProp;

    public GetBooleanSecurityPropertyAction(String str) {
        this.theProp = str;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    /* JADX WARN: Removed duplicated region for block: B:8:0x001b  */
    @Override // java.security.PrivilegedAction
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.Boolean run() {
        /*
            r3 = this;
            r0 = 0
            r4 = r0
            r0 = r3
            java.lang.String r0 = r0.theProp     // Catch: java.lang.NullPointerException -> L20
            java.lang.String r0 = java.security.Security.getProperty(r0)     // Catch: java.lang.NullPointerException -> L20
            r5 = r0
            r0 = r5
            if (r0 == 0) goto L1b
            r0 = r5
            java.lang.String r1 = "true"
            boolean r0 = r0.equalsIgnoreCase(r1)     // Catch: java.lang.NullPointerException -> L20
            if (r0 == 0) goto L1b
            r0 = 1
            goto L1c
        L1b:
            r0 = 0
        L1c:
            r4 = r0
            goto L21
        L20:
            r5 = move-exception
        L21:
            r0 = r4
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r0)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.action.GetBooleanSecurityPropertyAction.run():java.lang.Boolean");
    }
}
