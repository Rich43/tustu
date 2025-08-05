package com.sun.nio.sctp;

import jdk.Exported;

@Exported
/* loaded from: rt.jar:com/sun/nio/sctp/ShutdownNotification.class */
public abstract class ShutdownNotification implements Notification {
    @Override // com.sun.nio.sctp.Notification
    public abstract Association association();

    protected ShutdownNotification() {
    }
}
