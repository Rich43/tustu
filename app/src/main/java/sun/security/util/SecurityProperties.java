package sun.security.util;

import java.security.AccessController;
import java.security.Security;

/* loaded from: rt.jar:sun/security/util/SecurityProperties.class */
public class SecurityProperties {
    public static String privilegedGetOverridable(String str) {
        if (System.getSecurityManager() == null) {
            return getOverridableProperty(str);
        }
        return (String) AccessController.doPrivileged(() -> {
            return getOverridableProperty(str);
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getOverridableProperty(String str) {
        String property = System.getProperty(str);
        if (property == null) {
            return Security.getProperty(str);
        }
        return property;
    }

    public static boolean includedInExceptions(String str) {
        String strPrivilegedGetOverridable = privilegedGetOverridable("jdk.includeInExceptions");
        if (strPrivilegedGetOverridable == null) {
            return false;
        }
        for (String str2 : strPrivilegedGetOverridable.split(",")) {
            if (str2.trim().equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }
}
