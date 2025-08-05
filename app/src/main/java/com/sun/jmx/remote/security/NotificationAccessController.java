package com.sun.jmx.remote.security;

import javax.management.Notification;
import javax.management.ObjectName;
import javax.security.auth.Subject;

/* loaded from: rt.jar:com/sun/jmx/remote/security/NotificationAccessController.class */
public interface NotificationAccessController {
    void addNotificationListener(String str, ObjectName objectName, Subject subject) throws SecurityException;

    void removeNotificationListener(String str, ObjectName objectName, Subject subject) throws SecurityException;

    void fetchNotification(String str, ObjectName objectName, Notification notification, Subject subject) throws SecurityException;
}
