package com.sun.nio.sctp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Set;
import jdk.Exported;
import sun.nio.ch.sctp.SctpChannelImpl;

@Exported
/* loaded from: rt.jar:com/sun/nio/sctp/SctpChannel.class */
public abstract class SctpChannel extends AbstractSelectableChannel {
    public abstract Association association() throws IOException;

    public abstract SctpChannel bind(SocketAddress socketAddress) throws IOException;

    public abstract SctpChannel bindAddress(InetAddress inetAddress) throws IOException;

    public abstract SctpChannel unbindAddress(InetAddress inetAddress) throws IOException;

    public abstract boolean connect(SocketAddress socketAddress) throws IOException;

    public abstract boolean connect(SocketAddress socketAddress, int i2, int i3) throws IOException;

    public abstract boolean isConnectionPending();

    public abstract boolean finishConnect() throws IOException;

    public abstract Set<SocketAddress> getAllLocalAddresses() throws IOException;

    public abstract Set<SocketAddress> getRemoteAddresses() throws IOException;

    public abstract SctpChannel shutdown() throws IOException;

    public abstract <T> T getOption(SctpSocketOption<T> sctpSocketOption) throws IOException;

    public abstract <T> SctpChannel setOption(SctpSocketOption<T> sctpSocketOption, T t2) throws IOException;

    public abstract Set<SctpSocketOption<?>> supportedOptions();

    public abstract <T> MessageInfo receive(ByteBuffer byteBuffer, T t2, NotificationHandler<T> notificationHandler) throws IOException;

    public abstract int send(ByteBuffer byteBuffer, MessageInfo messageInfo) throws IOException;

    protected SctpChannel(SelectorProvider selectorProvider) {
        super(selectorProvider);
    }

    public static SctpChannel open() throws IOException {
        return new SctpChannelImpl((SelectorProvider) null);
    }

    public static SctpChannel open(SocketAddress socketAddress, int i2, int i3) throws IOException {
        SctpChannel sctpChannelOpen = open();
        sctpChannelOpen.connect(socketAddress, i2, i3);
        return sctpChannelOpen;
    }

    @Override // java.nio.channels.SelectableChannel
    public final int validOps() {
        return 13;
    }
}
