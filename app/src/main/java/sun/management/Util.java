package sun.management;

import java.lang.management.ManagementPermission;
import java.util.List;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/* loaded from: rt.jar:sun/management/Util.class */
public class Util {
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    private static ManagementPermission monitorPermission = new ManagementPermission("monitor");
    private static ManagementPermission controlPermission = new ManagementPermission("control");

    private Util() {
    }

    static RuntimeException newException(Exception exc) {
        throw new RuntimeException(exc);
    }

    static String[] toStringArray(List<String> list) {
        return (String[]) list.toArray(EMPTY_STRING_ARRAY);
    }

    public static ObjectName newObjectName(String str, String str2) {
        return newObjectName(str + ",name=" + str2);
    }

    public static ObjectName newObjectName(String str) {
        try {
            return ObjectName.getInstance(str);
        } catch (MalformedObjectNameException e2) {
            throw new IllegalArgumentException(e2);
        }
    }

    static void checkAccess(ManagementPermission managementPermission) throws SecurityException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(managementPermission);
        }
    }

    static void checkMonitorAccess() throws SecurityException {
        checkAccess(monitorPermission);
    }

    static void checkControlAccess() throws SecurityException {
        checkAccess(controlPermission);
    }
}
