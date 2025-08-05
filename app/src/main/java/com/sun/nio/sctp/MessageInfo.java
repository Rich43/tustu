package com.sun.nio.sctp;

import java.net.SocketAddress;
import jdk.Exported;
import sun.nio.ch.sctp.MessageInfoImpl;

@Exported
/* loaded from: rt.jar:com/sun/nio/sctp/MessageInfo.class */
public abstract class MessageInfo {
    public abstract SocketAddress address();

    public abstract Association association();

    public abstract int bytes();

    public abstract boolean isComplete();

    public abstract MessageInfo complete(boolean z2);

    public abstract boolean isUnordered();

    public abstract MessageInfo unordered(boolean z2);

    public abstract int payloadProtocolID();

    public abstract MessageInfo payloadProtocolID(int i2);

    public abstract int streamNumber();

    public abstract MessageInfo streamNumber(int i2);

    public abstract long timeToLive();

    public abstract MessageInfo timeToLive(long j2);

    protected MessageInfo() {
    }

    public static MessageInfo createOutgoing(SocketAddress socketAddress, int i2) {
        if (i2 < 0 || i2 > 65536) {
            throw new IllegalArgumentException("Invalid stream number");
        }
        return new MessageInfoImpl(null, socketAddress, i2);
    }

    public static MessageInfo createOutgoing(Association association, SocketAddress socketAddress, int i2) {
        if (association == null) {
            throw new IllegalArgumentException("association cannot be null");
        }
        if (i2 < 0 || i2 > 65536) {
            throw new IllegalArgumentException("Invalid stream number");
        }
        return new MessageInfoImpl(association, socketAddress, i2);
    }
}
