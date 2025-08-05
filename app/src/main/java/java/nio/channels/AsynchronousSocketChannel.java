package java.nio.channels;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.spi.AsynchronousChannelProvider;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/* loaded from: rt.jar:java/nio/channels/AsynchronousSocketChannel.class */
public abstract class AsynchronousSocketChannel implements AsynchronousByteChannel, NetworkChannel {
    private final AsynchronousChannelProvider provider;

    @Override // java.nio.channels.NetworkChannel
    public abstract AsynchronousSocketChannel bind(SocketAddress socketAddress) throws IOException;

    @Override // java.nio.channels.NetworkChannel
    public abstract <T> AsynchronousSocketChannel setOption(SocketOption<T> socketOption, T t2) throws IOException;

    public abstract AsynchronousSocketChannel shutdownInput() throws IOException;

    public abstract AsynchronousSocketChannel shutdownOutput() throws IOException;

    public abstract SocketAddress getRemoteAddress() throws IOException;

    public abstract <A> void connect(SocketAddress socketAddress, A a2, CompletionHandler<Void, ? super A> completionHandler);

    public abstract Future<Void> connect(SocketAddress socketAddress);

    public abstract <A> void read(ByteBuffer byteBuffer, long j2, TimeUnit timeUnit, A a2, CompletionHandler<Integer, ? super A> completionHandler);

    @Override // java.nio.channels.AsynchronousByteChannel
    public abstract Future<Integer> read(ByteBuffer byteBuffer);

    public abstract <A> void read(ByteBuffer[] byteBufferArr, int i2, int i3, long j2, TimeUnit timeUnit, A a2, CompletionHandler<Long, ? super A> completionHandler);

    public abstract <A> void write(ByteBuffer byteBuffer, long j2, TimeUnit timeUnit, A a2, CompletionHandler<Integer, ? super A> completionHandler);

    @Override // java.nio.channels.AsynchronousByteChannel
    public abstract Future<Integer> write(ByteBuffer byteBuffer);

    public abstract <A> void write(ByteBuffer[] byteBufferArr, int i2, int i3, long j2, TimeUnit timeUnit, A a2, CompletionHandler<Long, ? super A> completionHandler);

    @Override // java.nio.channels.NetworkChannel
    public abstract SocketAddress getLocalAddress() throws IOException;

    @Override // java.nio.channels.NetworkChannel
    public /* bridge */ /* synthetic */ NetworkChannel setOption(SocketOption socketOption, Object obj) throws IOException {
        return setOption((SocketOption<SocketOption>) socketOption, (SocketOption) obj);
    }

    protected AsynchronousSocketChannel(AsynchronousChannelProvider asynchronousChannelProvider) {
        this.provider = asynchronousChannelProvider;
    }

    public final AsynchronousChannelProvider provider() {
        return this.provider;
    }

    public static AsynchronousSocketChannel open(AsynchronousChannelGroup asynchronousChannelGroup) throws IOException {
        return (asynchronousChannelGroup == null ? AsynchronousChannelProvider.provider() : asynchronousChannelGroup.provider()).openAsynchronousSocketChannel(asynchronousChannelGroup);
    }

    public static AsynchronousSocketChannel open() throws IOException {
        return open(null);
    }

    @Override // java.nio.channels.AsynchronousByteChannel
    public final <A> void read(ByteBuffer byteBuffer, A a2, CompletionHandler<Integer, ? super A> completionHandler) {
        read(byteBuffer, 0L, TimeUnit.MILLISECONDS, a2, completionHandler);
    }

    @Override // java.nio.channels.AsynchronousByteChannel
    public final <A> void write(ByteBuffer byteBuffer, A a2, CompletionHandler<Integer, ? super A> completionHandler) {
        write(byteBuffer, 0L, TimeUnit.MILLISECONDS, a2, completionHandler);
    }
}
