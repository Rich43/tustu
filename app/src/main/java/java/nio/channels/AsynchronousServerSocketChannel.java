package java.nio.channels;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.channels.spi.AsynchronousChannelProvider;
import java.util.concurrent.Future;

/* loaded from: rt.jar:java/nio/channels/AsynchronousServerSocketChannel.class */
public abstract class AsynchronousServerSocketChannel implements AsynchronousChannel, NetworkChannel {
    private final AsynchronousChannelProvider provider;

    public abstract AsynchronousServerSocketChannel bind(SocketAddress socketAddress, int i2) throws IOException;

    @Override // java.nio.channels.NetworkChannel
    public abstract <T> AsynchronousServerSocketChannel setOption(SocketOption<T> socketOption, T t2) throws IOException;

    public abstract <A> void accept(A a2, CompletionHandler<AsynchronousSocketChannel, ? super A> completionHandler);

    public abstract Future<AsynchronousSocketChannel> accept();

    @Override // java.nio.channels.NetworkChannel
    public abstract SocketAddress getLocalAddress() throws IOException;

    @Override // java.nio.channels.NetworkChannel
    public /* bridge */ /* synthetic */ NetworkChannel setOption(SocketOption socketOption, Object obj) throws IOException {
        return setOption((SocketOption<SocketOption>) socketOption, (SocketOption) obj);
    }

    protected AsynchronousServerSocketChannel(AsynchronousChannelProvider asynchronousChannelProvider) {
        this.provider = asynchronousChannelProvider;
    }

    public final AsynchronousChannelProvider provider() {
        return this.provider;
    }

    public static AsynchronousServerSocketChannel open(AsynchronousChannelGroup asynchronousChannelGroup) throws IOException {
        return (asynchronousChannelGroup == null ? AsynchronousChannelProvider.provider() : asynchronousChannelGroup.provider()).openAsynchronousServerSocketChannel(asynchronousChannelGroup);
    }

    public static AsynchronousServerSocketChannel open() throws IOException {
        return open(null);
    }

    @Override // java.nio.channels.NetworkChannel
    public final AsynchronousServerSocketChannel bind(SocketAddress socketAddress) throws IOException {
        return bind(socketAddress, 0);
    }
}
