package com.sun.nio.sctp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Set;
import jdk.Exported;
import sun.nio.ch.sctp.SctpServerChannelImpl;

@Exported
/* loaded from: rt.jar:com/sun/nio/sctp/SctpServerChannel.class */
public abstract class SctpServerChannel extends AbstractSelectableChannel {
    public abstract SctpChannel accept() throws IOException;

    public abstract SctpServerChannel bind(SocketAddress socketAddress, int i2) throws IOException;

    public abstract SctpServerChannel bindAddress(InetAddress inetAddress) throws IOException;

    public abstract SctpServerChannel unbindAddress(InetAddress inetAddress) throws IOException;

    public abstract Set<SocketAddress> getAllLocalAddresses() throws IOException;

    public abstract <T> T getOption(SctpSocketOption<T> sctpSocketOption) throws IOException;

    public abstract <T> SctpServerChannel setOption(SctpSocketOption<T> sctpSocketOption, T t2) throws IOException;

    public abstract Set<SctpSocketOption<?>> supportedOptions();

    protected SctpServerChannel(SelectorProvider selectorProvider) {
        super(selectorProvider);
    }

    public static SctpServerChannel open() throws IOException {
        return new SctpServerChannelImpl((SelectorProvider) null);
    }

    public final SctpServerChannel bind(SocketAddress socketAddress) throws IOException {
        return bind(socketAddress, 0);
    }

    @Override // java.nio.channels.SelectableChannel
    public final int validOps() {
        return 16;
    }
}
