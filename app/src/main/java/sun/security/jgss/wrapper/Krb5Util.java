package sun.security.jgss.wrapper;

import javax.security.auth.kerberos.ServicePermission;
import org.ietf.jgss.GSSException;

/* loaded from: rt.jar:sun/security/jgss/wrapper/Krb5Util.class */
class Krb5Util {
    Krb5Util() {
    }

    static String getTGSName(GSSNameElement gSSNameElement) throws GSSException {
        String krbName = gSSNameElement.getKrbName();
        String strSubstring = krbName.substring(krbName.indexOf("@") + 1);
        StringBuffer stringBuffer = new StringBuffer("krbtgt/");
        stringBuffer.append(strSubstring).append('@').append(strSubstring);
        return stringBuffer.toString();
    }

    static void checkServicePermission(String str, String str2) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            SunNativeProvider.debug("Checking ServicePermission(" + str + ", " + str2 + ")");
            securityManager.checkPermission(new ServicePermission(str, str2));
        }
    }
}
