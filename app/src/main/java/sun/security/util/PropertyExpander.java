package sun.security.util;

import java.security.GeneralSecurityException;

/* loaded from: rt.jar:sun/security/util/PropertyExpander.class */
public class PropertyExpander {

    /* loaded from: rt.jar:sun/security/util/PropertyExpander$ExpandException.class */
    public static class ExpandException extends GeneralSecurityException {
        private static final long serialVersionUID = -7941948581406161702L;

        public ExpandException(String str) {
            super(str);
        }
    }

    public static String expand(String str) throws ExpandException {
        return expand(str, false);
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x0075, code lost:
    
        r0.append(r6.substring(r8));
     */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0157 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:68:? A[LOOP:0: B:11:0x002a->B:68:?, LOOP_END, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String expand(java.lang.String r6, boolean r7) throws sun.security.util.PropertyExpander.ExpandException {
        /*
            Method dump skipped, instructions count: 374
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.util.PropertyExpander.expand(java.lang.String, boolean):java.lang.String");
    }
}
