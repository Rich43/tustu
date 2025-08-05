package com.sun.nio.sctp;

import jdk.Exported;

@Exported
/* loaded from: rt.jar:com/sun/nio/sctp/AssociationChangeNotification.class */
public abstract class AssociationChangeNotification implements Notification {

    @Exported
    /* loaded from: rt.jar:com/sun/nio/sctp/AssociationChangeNotification$AssocChangeEvent.class */
    public enum AssocChangeEvent {
        COMM_UP,
        COMM_LOST,
        RESTART,
        SHUTDOWN,
        CANT_START
    }

    @Override // com.sun.nio.sctp.Notification
    public abstract Association association();

    public abstract AssocChangeEvent event();

    protected AssociationChangeNotification() {
    }
}
