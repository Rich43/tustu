package com.sun.nio.sctp;

import jdk.Exported;

@Exported
/* loaded from: rt.jar:com/sun/nio/sctp/AbstractNotificationHandler.class */
public class AbstractNotificationHandler<T> implements NotificationHandler<T> {
    protected AbstractNotificationHandler() {
    }

    @Override // com.sun.nio.sctp.NotificationHandler
    public HandlerResult handleNotification(Notification notification, T t2) {
        return HandlerResult.CONTINUE;
    }

    public HandlerResult handleNotification(AssociationChangeNotification associationChangeNotification, T t2) {
        return HandlerResult.CONTINUE;
    }

    public HandlerResult handleNotification(PeerAddressChangeNotification peerAddressChangeNotification, T t2) {
        return HandlerResult.CONTINUE;
    }

    public HandlerResult handleNotification(SendFailedNotification sendFailedNotification, T t2) {
        return HandlerResult.CONTINUE;
    }

    public HandlerResult handleNotification(ShutdownNotification shutdownNotification, T t2) {
        return HandlerResult.CONTINUE;
    }
}
