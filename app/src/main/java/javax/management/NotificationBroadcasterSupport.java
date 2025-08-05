package javax.management;

import com.sun.jmx.remote.util.ClassLogger;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;

/* loaded from: rt.jar:javax/management/NotificationBroadcasterSupport.class */
public class NotificationBroadcasterSupport implements NotificationEmitter {
    private List<ListenerInfo> listenerList;
    private final Executor executor;
    private final MBeanNotificationInfo[] notifInfo;
    private static final Executor defaultExecutor = new Executor() { // from class: javax.management.NotificationBroadcasterSupport.1
        @Override // java.util.concurrent.Executor
        public void execute(Runnable runnable) {
            runnable.run();
        }
    };
    private static final MBeanNotificationInfo[] NO_NOTIFICATION_INFO = new MBeanNotificationInfo[0];
    private static final ClassLogger logger = new ClassLogger("javax.management", "NotificationBroadcasterSupport");

    public NotificationBroadcasterSupport() {
        this(null, (MBeanNotificationInfo[]) null);
    }

    public NotificationBroadcasterSupport(Executor executor) {
        this(executor, (MBeanNotificationInfo[]) null);
    }

    public NotificationBroadcasterSupport(MBeanNotificationInfo... mBeanNotificationInfoArr) {
        this(null, mBeanNotificationInfoArr);
    }

    public NotificationBroadcasterSupport(Executor executor, MBeanNotificationInfo... mBeanNotificationInfoArr) {
        this.listenerList = new CopyOnWriteArrayList();
        this.executor = executor != null ? executor : defaultExecutor;
        this.notifInfo = mBeanNotificationInfoArr == null ? NO_NOTIFICATION_INFO : (MBeanNotificationInfo[]) mBeanNotificationInfoArr.clone();
    }

    @Override // javax.management.NotificationBroadcaster
    public void addNotificationListener(NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) {
        if (notificationListener == null) {
            throw new IllegalArgumentException("Listener can't be null");
        }
        this.listenerList.add(new ListenerInfo(notificationListener, notificationFilter, obj));
    }

    @Override // javax.management.NotificationBroadcaster
    public void removeNotificationListener(NotificationListener notificationListener) throws ListenerNotFoundException {
        if (!this.listenerList.removeAll(Collections.singleton(new WildcardListenerInfo(notificationListener)))) {
            throw new ListenerNotFoundException("Listener not registered");
        }
    }

    @Override // javax.management.NotificationEmitter
    public void removeNotificationListener(NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws ListenerNotFoundException {
        if (!this.listenerList.remove(new ListenerInfo(notificationListener, notificationFilter, obj))) {
            throw new ListenerNotFoundException("Listener not registered (with this filter and handback)");
        }
    }

    @Override // javax.management.NotificationBroadcaster
    public MBeanNotificationInfo[] getNotificationInfo() {
        if (this.notifInfo.length == 0) {
            return this.notifInfo;
        }
        return (MBeanNotificationInfo[]) this.notifInfo.clone();
    }

    public void sendNotification(Notification notification) {
        if (notification == null) {
            return;
        }
        for (ListenerInfo listenerInfo : this.listenerList) {
            try {
                if (listenerInfo.filter == null || listenerInfo.filter.isNotificationEnabled(notification)) {
                    this.executor.execute(new SendNotifJob(notification, listenerInfo));
                }
            } catch (Exception e2) {
                if (logger.debugOn()) {
                    logger.debug("sendNotification", e2);
                }
            }
        }
    }

    protected void handleNotification(NotificationListener notificationListener, Notification notification, Object obj) {
        notificationListener.handleNotification(notification, obj);
    }

    /* loaded from: rt.jar:javax/management/NotificationBroadcasterSupport$ListenerInfo.class */
    private static class ListenerInfo {
        NotificationListener listener;
        NotificationFilter filter;
        Object handback;

        ListenerInfo(NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) {
            this.listener = notificationListener;
            this.filter = notificationFilter;
            this.handback = obj;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof ListenerInfo)) {
                return false;
            }
            ListenerInfo listenerInfo = (ListenerInfo) obj;
            return listenerInfo instanceof WildcardListenerInfo ? listenerInfo.listener == this.listener : listenerInfo.listener == this.listener && listenerInfo.filter == this.filter && listenerInfo.handback == this.handback;
        }

        public int hashCode() {
            return Objects.hashCode(this.listener);
        }
    }

    /* loaded from: rt.jar:javax/management/NotificationBroadcasterSupport$WildcardListenerInfo.class */
    private static class WildcardListenerInfo extends ListenerInfo {
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !NotificationBroadcasterSupport.class.desiredAssertionStatus();
        }

        WildcardListenerInfo(NotificationListener notificationListener) {
            super(notificationListener, null, null);
        }

        @Override // javax.management.NotificationBroadcasterSupport.ListenerInfo
        public boolean equals(Object obj) {
            if ($assertionsDisabled || !(obj instanceof WildcardListenerInfo)) {
                return obj.equals(this);
            }
            throw new AssertionError();
        }

        @Override // javax.management.NotificationBroadcasterSupport.ListenerInfo
        public int hashCode() {
            return super.hashCode();
        }
    }

    /* loaded from: rt.jar:javax/management/NotificationBroadcasterSupport$SendNotifJob.class */
    private class SendNotifJob implements Runnable {
        private final Notification notif;
        private final ListenerInfo listenerInfo;

        public SendNotifJob(Notification notification, ListenerInfo listenerInfo) {
            this.notif = notification;
            this.listenerInfo = listenerInfo;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                NotificationBroadcasterSupport.this.handleNotification(this.listenerInfo.listener, this.notif, this.listenerInfo.handback);
            } catch (Exception e2) {
                if (NotificationBroadcasterSupport.logger.debugOn()) {
                    NotificationBroadcasterSupport.logger.debug("SendNotifJob-run", e2);
                }
            }
        }
    }
}
