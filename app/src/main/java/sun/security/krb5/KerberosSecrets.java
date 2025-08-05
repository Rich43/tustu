package sun.security.krb5;

import javax.security.auth.kerberos.KeyTab;
import sun.misc.Unsafe;

/* loaded from: rt.jar:sun/security/krb5/KerberosSecrets.class */
public class KerberosSecrets {
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static JavaxSecurityAuthKerberosAccess javaxSecurityAuthKerberosAccess;

    public static void setJavaxSecurityAuthKerberosAccess(JavaxSecurityAuthKerberosAccess javaxSecurityAuthKerberosAccess2) {
        javaxSecurityAuthKerberosAccess = javaxSecurityAuthKerberosAccess2;
    }

    public static JavaxSecurityAuthKerberosAccess getJavaxSecurityAuthKerberosAccess() {
        if (javaxSecurityAuthKerberosAccess == null) {
            unsafe.ensureClassInitialized(KeyTab.class);
        }
        return javaxSecurityAuthKerberosAccess;
    }
}
