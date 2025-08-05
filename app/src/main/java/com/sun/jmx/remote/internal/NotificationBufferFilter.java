package com.sun.jmx.remote.internal;

import java.util.List;
import javax.management.Notification;
import javax.management.ObjectName;
import javax.management.remote.TargetedNotification;

/* loaded from: rt.jar:com/sun/jmx/remote/internal/NotificationBufferFilter.class */
public interface NotificationBufferFilter {
    void apply(List<TargetedNotification> list, ObjectName objectName, Notification notification);
}
