package java.nio.channels;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ProtocolFamily;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.nio.channels.spi.SelectorProvider;

/* loaded from: rt.jar:java/nio/channels/DatagramChannel.class */
public abstract class DatagramChannel extends AbstractSelectableChannel implements ByteChannel, ScatteringByteChannel, GatheringByteChannel, MulticastChannel {
    @Override // java.nio.channels.NetworkChannel
    public abstract DatagramChannel bind(SocketAddress socketAddress) throws IOException;

    @Override // java.nio.channels.NetworkChannel
    public abstract <T> DatagramChannel setOption(SocketOption<T> socketOption, T t2) throws IOException;

    public abstract DatagramSocket socket();

    public abstract boolean isConnected();

    public abstract DatagramChannel connect(SocketAddress socketAddress) throws IOException;

    public abstract DatagramChannel disconnect() throws IOException;

    public abstract SocketAddress getRemoteAddress() throws IOException;

    public abstract SocketAddress receive(ByteBuffer byteBuffer) throws IOException;

    public abstract int send(ByteBuffer byteBuffer, SocketAddress socketAddress) throws IOException;

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

    protected DatagramChannel(SelectorProvider selectorProvider) {
        super(selectorProvider);
    }

    public static DatagramChannel open() throws IOException {
        return SelectorProvider.provider().openDatagramChannel();
    }

    public static DatagramChannel open(ProtocolFamily protocolFamily) throws IOException {
        return SelectorProvider.provider().openDatagramChannel(protocolFamily);
    }

    @Override // java.nio.channels.SelectableChannel
    public final int validOps() {
        return 5;
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
