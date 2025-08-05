package sun.management;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;

/* loaded from: rt.jar:sun/management/NotificationEmitterSupport.class */
abstract class NotificationEmitterSupport implements NotificationEmitter {
    private Object listenerLock = new Object();
    private List<ListenerInfo> listenerList = Collections.emptyList();

    @Override // javax.management.NotificationBroadcaster
    public abstract MBeanNotificationInfo[] getNotificationInfo();

    protected NotificationEmitterSupport() {
    }

    @Override // javax.management.NotificationBroadcaster
    public void addNotificationListener(NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) {
        if (notificationListener == null) {
            throw new IllegalArgumentException("Listener can't be null");
        }
        synchronized (this.listenerLock) {
            ArrayList arrayList = new ArrayList(this.listenerList.size() + 1);
            arrayList.addAll(this.listenerList);
            arrayList.add(new ListenerInfo(notificationListener, notificationFilter, obj));
            this.listenerList = arrayList;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javax.management.NotificationBroadcaster
    public void removeNotificationListener(NotificationListener notificationListener) throws ListenerNotFoundException {
        synchronized (this.listenerLock) {
            ArrayList arrayList = new ArrayList(this.listenerList);
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                if (((ListenerInfo) arrayList.get(size)).listener == notificationListener) {
                    arrayList.remove(size);
                }
            }
            if (arrayList.size() == this.listenerList.size()) {
                throw new ListenerNotFoundException("Listener not registered");
            }
            this.listenerList = arrayList;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javax.management.NotificationEmitter
    public void removeNotificationListener(NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws ListenerNotFoundException {
        boolean z2 = false;
        synchronized (this.listenerLock) {
            ArrayList arrayList = new ArrayList(this.listenerList);
            int size = arrayList.size();
            for (int i2 = 0; i2 < size; i2++) {
                ListenerInfo listenerInfo = (ListenerInfo) arrayList.get(i2);
                if (listenerInfo.listener == notificationListener) {
                    z2 = true;
                    if (listenerInfo.filter == notificationFilter && listenerInfo.handback == obj) {
                        arrayList.remove(i2);
                        this.listenerList = arrayList;
                        return;
                    }
                }
            }
            if (z2) {
                throw new ListenerNotFoundException("Listener not registered with this filter and handback");
            }
            throw new ListenerNotFoundException("Listener not registered");
        }
    }

    void sendNotification(Notification notification) {
        List<ListenerInfo> list;
        if (notification == null) {
            return;
        }
        synchronized (this.listenerLock) {
            list = this.listenerList;
        }
        int size = list.size();
        for (int i2 = 0; i2 < size; i2++) {
            ListenerInfo listenerInfo = list.get(i2);
            if (listenerInfo.filter == null || listenerInfo.filter.isNotificationEnabled(notification)) {
                try {
                    listenerInfo.listener.handleNotification(notification, listenerInfo.handback);
                } catch (Exception e2) {
                    e2.printStackTrace();
                    throw new AssertionError((Object) "Error in invoking listener");
                }
            }
        }
    }

    boolean hasListeners() {
        boolean z2;
        synchronized (this.listenerLock) {
            z2 = !this.listenerList.isEmpty();
        }
        return z2;
    }

    /* loaded from: rt.jar:sun/management/NotificationEmitterSupport$ListenerInfo.class */
    private class ListenerInfo {
        public NotificationListener listener;
        NotificationFilter filter;
        Object handback;

        public ListenerInfo(NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) {
            this.listener = notificationListener;
            this.filter = notificationFilter;
            this.handback = obj;
        }
    }
}
