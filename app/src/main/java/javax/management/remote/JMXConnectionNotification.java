package javax.management.remote;

import javax.management.Notification;

/* loaded from: rt.jar:javax/management/remote/JMXConnectionNotification.class */
public class JMXConnectionNotification extends Notification {
    private static final long serialVersionUID = -2331308725952627538L;
    public static final String OPENED = "jmx.remote.connection.opened";
    public static final String CLOSED = "jmx.remote.connection.closed";
    public static final String FAILED = "jmx.remote.connection.failed";
    public static final String NOTIFS_LOST = "jmx.remote.connection.notifs.lost";
    private final String connectionId;

    public JMXConnectionNotification(String str, Object obj, String str2, long j2, String str3, Object obj2) {
        super((String) nonNull(str), nonNull(obj), Math.max(0L, j2), System.currentTimeMillis(), str3);
        if (str == null || obj == null || str2 == null) {
            throw new NullPointerException("Illegal null argument");
        }
        if (j2 < 0) {
            throw new IllegalArgumentException("Negative sequence number");
        }
        this.connectionId = str2;
        setUserData(obj2);
    }

    private static Object nonNull(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj;
    }

    public String getConnectionId() {
        return this.connectionId;
    }
}
