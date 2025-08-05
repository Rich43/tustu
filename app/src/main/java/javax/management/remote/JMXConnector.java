package javax.management.remote;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.security.auth.Subject;

/* loaded from: rt.jar:javax/management/remote/JMXConnector.class */
public interface JMXConnector extends Closeable {
    public static final String CREDENTIALS = "jmx.remote.credentials";

    void connect() throws IOException;

    void connect(Map<String, ?> map) throws IOException;

    MBeanServerConnection getMBeanServerConnection() throws IOException;

    MBeanServerConnection getMBeanServerConnection(Subject subject) throws IOException;

    @Override // java.io.Closeable, java.lang.AutoCloseable
    void close() throws IOException;

    void addConnectionNotificationListener(NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj);

    void removeConnectionNotificationListener(NotificationListener notificationListener) throws ListenerNotFoundException;

    void removeConnectionNotificationListener(NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws ListenerNotFoundException;

    String getConnectionId() throws IOException;
}
