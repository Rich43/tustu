package com.sun.jmx.remote.internal;

import javax.management.remote.NotificationResult;

/* loaded from: rt.jar:com/sun/jmx/remote/internal/NotificationBuffer.class */
public interface NotificationBuffer {
    NotificationResult fetchNotifications(NotificationBufferFilter notificationBufferFilter, long j2, long j3, int i2) throws InterruptedException;

    void dispose();
}
