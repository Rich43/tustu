package java.util.logging;

import java.security.BasicPermission;

/* loaded from: rt.jar:java/util/logging/LoggingPermission.class */
public final class LoggingPermission extends BasicPermission {
    private static final long serialVersionUID = 63564341580231582L;

    public LoggingPermission(String str, String str2) throws IllegalArgumentException {
        super(str);
        if (!str.equals("control")) {
            throw new IllegalArgumentException("name: " + str);
        }
        if (str2 != null && str2.length() > 0) {
            throw new IllegalArgumentException("actions: " + str2);
        }
    }
}
