package com.sun.jmx.remote.internal;

import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.security.auth.Subject;

/* loaded from: rt.jar:com/sun/jmx/remote/internal/ClientListenerInfo.class */
public class ClientListenerInfo {
    private final ObjectName name;
    private final Integer listenerID;
    private final NotificationFilter filter;
    private final NotificationListener listener;
    private final Object handback;
    private final Subject delegationSubject;

    public ClientListenerInfo(Integer num, ObjectName objectName, NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj, Subject subject) {
        this.listenerID = num;
        this.name = objectName;
        this.listener = notificationListener;
        this.filter = notificationFilter;
        this.handback = obj;
        this.delegationSubject = subject;
    }

    public ObjectName getObjectName() {
        return this.name;
    }

    public Integer getListenerID() {
        return this.listenerID;
    }

    public NotificationFilter getNotificationFilter() {
        return this.filter;
    }

    public NotificationListener getListener() {
        return this.listener;
    }

    public Object getHandback() {
        return this.handback;
    }

    public Subject getDelegationSubject() {
        return this.delegationSubject;
    }

    public boolean sameAs(ObjectName objectName) {
        return getObjectName().equals(objectName);
    }

    public boolean sameAs(ObjectName objectName, NotificationListener notificationListener) {
        return getObjectName().equals(objectName) && getListener() == notificationListener;
    }

    public boolean sameAs(ObjectName objectName, NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) {
        return getObjectName().equals(objectName) && getListener() == notificationListener && getNotificationFilter() == notificationFilter && getHandback() == obj;
    }
}
