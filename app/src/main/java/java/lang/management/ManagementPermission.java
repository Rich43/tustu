package java.lang.management;

import java.security.BasicPermission;

/* loaded from: rt.jar:java/lang/management/ManagementPermission.class */
public final class ManagementPermission extends BasicPermission {
    private static final long serialVersionUID = 1897496590799378737L;

    public ManagementPermission(String str) {
        super(str);
        if (!str.equals("control") && !str.equals("monitor")) {
            throw new IllegalArgumentException("name: " + str);
        }
    }

    public ManagementPermission(String str, String str2) throws IllegalArgumentException {
        super(str);
        if (!str.equals("control") && !str.equals("monitor")) {
            throw new IllegalArgumentException("name: " + str);
        }
        if (str2 != null && str2.length() > 0) {
            throw new IllegalArgumentException("actions: " + str2);
        }
    }
}
