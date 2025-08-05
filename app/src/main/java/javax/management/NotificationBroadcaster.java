package javax.management;

/* loaded from: rt.jar:javax/management/NotificationBroadcaster.class */
public interface NotificationBroadcaster {
    void addNotificationListener(NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws IllegalArgumentException;

    void removeNotificationListener(NotificationListener notificationListener) throws ListenerNotFoundException;

    MBeanNotificationInfo[] getNotificationInfo();
}
