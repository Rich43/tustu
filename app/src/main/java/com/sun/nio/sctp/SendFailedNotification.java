package com.sun.nio.sctp;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import jdk.Exported;

@Exported
/* loaded from: rt.jar:com/sun/nio/sctp/SendFailedNotification.class */
public abstract class SendFailedNotification implements Notification {
    @Override // com.sun.nio.sctp.Notification
    public abstract Association association();

    public abstract SocketAddress address();

    public abstract ByteBuffer buffer();

    public abstract int errorCode();

    public abstract int streamNumber();

    protected SendFailedNotification() {
    }
}
