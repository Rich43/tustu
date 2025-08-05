package com.sun.nio.sctp;

import java.net.SocketAddress;
import jdk.Exported;

@Exported
/* loaded from: rt.jar:com/sun/nio/sctp/PeerAddressChangeNotification.class */
public abstract class PeerAddressChangeNotification implements Notification {

    @Exported
    /* loaded from: rt.jar:com/sun/nio/sctp/PeerAddressChangeNotification$AddressChangeEvent.class */
    public enum AddressChangeEvent {
        ADDR_AVAILABLE,
        ADDR_UNREACHABLE,
        ADDR_REMOVED,
        ADDR_ADDED,
        ADDR_MADE_PRIMARY,
        ADDR_CONFIRMED
    }

    public abstract SocketAddress address();

    @Override // com.sun.nio.sctp.Notification
    public abstract Association association();

    public abstract AddressChangeEvent event();

    protected PeerAddressChangeNotification() {
    }
}
