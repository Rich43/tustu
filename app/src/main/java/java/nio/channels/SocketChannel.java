package java.nio.channels;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.nio.channels.spi.SelectorProvider;

/* loaded from: rt.jar:java/nio/channels/SocketChannel.class */
public abstract class SocketChannel extends AbstractSelectableChannel implements ByteChannel, ScatteringByteChannel, GatheringByteChannel, NetworkChannel {
    static final /* synthetic */ boolean $assertionsDisabled;

    @Override // java.nio.channels.NetworkChannel
    public abstract SocketChannel bind(SocketAddress socketAddress) throws IOException;

    @Override // java.nio.channels.NetworkChannel
    public abstract <T> SocketChannel setOption(SocketOption<T> socketOption, T t2) throws IOException;

    public abstract SocketChannel shutdownInput() throws IOException;

    public abstract SocketChannel shutdownOutput() throws IOException;

    public abstract Socket socket();

    public abstract boolean isConnected();

    public abstract boolean isConnectionPending();

    public abstract boolean connect(SocketAddress socketAddress) throws IOException;

    public abstract boolean finishConnect() throws IOException;

    public abstract SocketAddress getRemoteAddress() throws IOException;

    @Override // java.nio.channels.ReadableByteChannel
    public abstract int read(ByteBuffer byteBuffer) throws IOException;

    @Override // java.nio.channels.ScatteringByteChannel
    public abstract long read(ByteBuffer[] byteBufferArr, int i2, int i3) throws IOException;

    @Override // java.nio.channels.WritableByteChannel
    public abstract int write(ByteBuffer byteBuffer) throws IOException;

    @Override // java.nio.channels.GatheringByteChannel
    public abstract long write(ByteBuffer[] byteBufferArr, int i2, int i3) throws IOException;

    @Override // java.nio.channels.NetworkChannel
    public abstract SocketAddress getLocalAddress() throws IOException;

    @Override // java.nio.channels.NetworkChannel
    public /* bridge */ /* synthetic */ NetworkChannel setOption(SocketOption socketOption, Object obj) throws IOException {
        return setOption((SocketOption<SocketOption>) socketOption, (SocketOption) obj);
    }

    static {
        $assertionsDisabled = !SocketChannel.class.desiredAssertionStatus();
    }

    protected SocketChannel(SelectorProvider selectorProvider) {
        super(selectorProvider);
    }

    public static SocketChannel open() throws IOException {
        return SelectorProvider.provider().openSocketChannel();
    }

    public static SocketChannel open(SocketAddress socketAddress) throws IOException {
        SocketChannel socketChannelOpen = open();
        try {
            socketChannelOpen.connect(socketAddress);
            if ($assertionsDisabled || socketChannelOpen.isConnected()) {
                return socketChannelOpen;
            }
            throw new AssertionError();
        } catch (Throwable th) {
            try {
                socketChannelOpen.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    @Override // java.nio.channels.SelectableChannel
    public final int validOps() {
        return 13;
    }

    @Override // java.nio.channels.ScatteringByteChannel
    public final long read(ByteBuffer[] byteBufferArr) throws IOException {
        return read(byteBufferArr, 0, byteBufferArr.length);
    }

    @Override // java.nio.channels.GatheringByteChannel
    public final long write(ByteBuffer[] byteBufferArr) throws IOException {
        return write(byteBufferArr, 0, byteBufferArr.length);
    }
}
