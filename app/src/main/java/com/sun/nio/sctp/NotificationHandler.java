package com.sun.nio.sctp;

import jdk.Exported;

@Exported
/* loaded from: rt.jar:com/sun/nio/sctp/NotificationHandler.class */
public interface NotificationHandler<T> {
    HandlerResult handleNotification(Notification notification, T t2);
}
