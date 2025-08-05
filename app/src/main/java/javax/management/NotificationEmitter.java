package javax.management;

/* loaded from: rt.jar:javax/management/NotificationEmitter.class */
public interface NotificationEmitter extends NotificationBroadcaster {
    void removeNotificationListener(NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws ListenerNotFoundException;
}
