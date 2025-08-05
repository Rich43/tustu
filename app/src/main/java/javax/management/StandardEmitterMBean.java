package javax.management;

/* loaded from: rt.jar:javax/management/StandardEmitterMBean.class */
public class StandardEmitterMBean extends StandardMBean implements NotificationEmitter {
    private static final MBeanNotificationInfo[] NO_NOTIFICATION_INFO = new MBeanNotificationInfo[0];
    private final NotificationEmitter emitter;
    private final MBeanNotificationInfo[] notificationInfo;

    public <T> StandardEmitterMBean(T t2, Class<T> cls, NotificationEmitter notificationEmitter) {
        this(t2, cls, false, notificationEmitter);
    }

    public <T> StandardEmitterMBean(T t2, Class<T> cls, boolean z2, NotificationEmitter notificationEmitter) {
        super(t2, cls, z2);
        if (notificationEmitter == null) {
            throw new IllegalArgumentException("Null emitter");
        }
        this.emitter = notificationEmitter;
        MBeanNotificationInfo[] notificationInfo = notificationEmitter.getNotificationInfo();
        if (notificationInfo == null || notificationInfo.length == 0) {
            this.notificationInfo = NO_NOTIFICATION_INFO;
        } else {
            this.notificationInfo = (MBeanNotificationInfo[]) notificationInfo.clone();
        }
    }

    protected StandardEmitterMBean(Class<?> cls, NotificationEmitter notificationEmitter) {
        this(cls, false, notificationEmitter);
    }

    protected StandardEmitterMBean(Class<?> cls, boolean z2, NotificationEmitter notificationEmitter) {
        super(cls, z2);
        if (notificationEmitter == null) {
            throw new IllegalArgumentException("Null emitter");
        }
        this.emitter = notificationEmitter;
        MBeanNotificationInfo[] notificationInfo = notificationEmitter.getNotificationInfo();
        if (notificationInfo == null || notificationInfo.length == 0) {
            this.notificationInfo = NO_NOTIFICATION_INFO;
        } else {
            this.notificationInfo = (MBeanNotificationInfo[]) notificationInfo.clone();
        }
    }

    @Override // javax.management.NotificationBroadcaster
    public void removeNotificationListener(NotificationListener notificationListener) throws ListenerNotFoundException {
        this.emitter.removeNotificationListener(notificationListener);
    }

    @Override // javax.management.NotificationEmitter
    public void removeNotificationListener(NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws ListenerNotFoundException {
        this.emitter.removeNotificationListener(notificationListener, notificationFilter, obj);
    }

    @Override // javax.management.NotificationBroadcaster
    public void addNotificationListener(NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) {
        this.emitter.addNotificationListener(notificationListener, notificationFilter, obj);
    }

    @Override // javax.management.NotificationBroadcaster
    public MBeanNotificationInfo[] getNotificationInfo() {
        if (this.notificationInfo == null) {
            return NO_NOTIFICATION_INFO;
        }
        if (this.notificationInfo.length == 0) {
            return this.notificationInfo;
        }
        return (MBeanNotificationInfo[]) this.notificationInfo.clone();
    }

    public void sendNotification(Notification notification) {
        if (this.emitter instanceof NotificationBroadcasterSupport) {
            ((NotificationBroadcasterSupport) this.emitter).sendNotification(notification);
            return;
        }
        throw new ClassCastException("Cannot sendNotification when emitter is not an instance of NotificationBroadcasterSupport: " + this.emitter.getClass().getName());
    }

    @Override // javax.management.StandardMBean
    MBeanNotificationInfo[] getNotifications(MBeanInfo mBeanInfo) {
        return getNotificationInfo();
    }
}
