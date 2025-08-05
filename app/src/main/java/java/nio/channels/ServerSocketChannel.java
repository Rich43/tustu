package java.nio.channels;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.nio.channels.spi.SelectorProvider;

/* loaded from: rt.jar:java/nio/channels/ServerSocketChannel.class */
public abstract class ServerSocketChannel extends AbstractSelectableChannel implements NetworkChannel {
    public abstract ServerSocketChannel bind(SocketAddress socketAddress, int i2) throws IOException;

    @Override // java.nio.channels.NetworkChannel
    public abstract <T> ServerSocketChannel setOption(SocketOption<T> socketOption, T t2) throws IOException;

    public abstract ServerSocket socket();

    public abstract SocketChannel accept() throws IOException;

    @Override // java.nio.channels.NetworkChannel
    public abstract SocketAddress getLocalAddress() throws IOException;

    @Override // java.nio.channels.NetworkChannel
    public /* bridge */ /* synthetic */ NetworkChannel setOption(SocketOption socketOption, Object obj) throws IOException {
        return setOption((SocketOption<SocketOption>) socketOption, (SocketOption) obj);
    }

    protected ServerSocketChannel(SelectorProvider selectorProvider) {
        super(selectorProvider);
    }

    public static ServerSocketChannel open() throws IOException {
        return SelectorProvider.provider().openServerSocketChannel();
    }

    @Override // java.nio.channels.SelectableChannel
    public final int validOps() {
        return 16;
    }

    @Override // java.nio.channels.NetworkChannel
    public final ServerSocketChannel bind(SocketAddress socketAddress) throws IOException {
        return bind(socketAddress, 0);
    }
}
