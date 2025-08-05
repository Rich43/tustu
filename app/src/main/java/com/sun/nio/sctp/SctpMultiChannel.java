package com.sun.nio.sctp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Set;
import jdk.Exported;
import sun.nio.ch.sctp.SctpMultiChannelImpl;

@Exported
/* loaded from: rt.jar:com/sun/nio/sctp/SctpMultiChannel.class */
public abstract class SctpMultiChannel extends AbstractSelectableChannel {
    public abstract Set<Association> associations() throws IOException;

    public abstract SctpMultiChannel bind(SocketAddress socketAddress, int i2) throws IOException;

    public abstract SctpMultiChannel bindAddress(InetAddress inetAddress) throws IOException;

    public abstract SctpMultiChannel unbindAddress(InetAddress inetAddress) throws IOException;

    public abstract Set<SocketAddress> getAllLocalAddresses() throws IOException;

    public abstract Set<SocketAddress> getRemoteAddresses(Association association) throws IOException;

    public abstract SctpMultiChannel shutdown(Association association) throws IOException;

    public abstract <T> T getOption(SctpSocketOption<T> sctpSocketOption, Association association) throws IOException;

    public abstract <T> SctpMultiChannel setOption(SctpSocketOption<T> sctpSocketOption, T t2, Association association) throws IOException;

    public abstract Set<SctpSocketOption<?>> supportedOptions();

    public abstract <T> MessageInfo receive(ByteBuffer byteBuffer, T t2, NotificationHandler<T> notificationHandler) throws IOException;

    public abstract int send(ByteBuffer byteBuffer, MessageInfo messageInfo) throws IOException;

    public abstract SctpChannel branch(Association association) throws IOException;

    protected SctpMultiChannel(SelectorProvider selectorProvider) {
        super(selectorProvider);
    }

    public static SctpMultiChannel open() throws IOException {
        return new SctpMultiChannelImpl((SelectorProvider) null);
    }

    public final SctpMultiChannel bind(SocketAddress socketAddress) throws IOException {
        return bind(socketAddress, 0);
    }

    @Override // java.nio.channels.SelectableChannel
    public final int validOps() {
        return 5;
    }
}
