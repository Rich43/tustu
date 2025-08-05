package sun.nio.ch.sctp;

import com.sun.nio.sctp.Association;
import com.sun.nio.sctp.MessageInfo;
import com.sun.nio.sctp.NotificationHandler;
import com.sun.nio.sctp.SctpChannel;
import com.sun.nio.sctp.SctpSocketOption;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.spi.SelectorProvider;
import java.util.Set;

/* loaded from: rt.jar:sun/nio/ch/sctp/SctpChannelImpl.class */
public class SctpChannelImpl extends SctpChannel {
    private static final String message = "SCTP not supported on this platform";

    public SctpChannelImpl(SelectorProvider selectorProvider) {
        super(selectorProvider);
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpChannel
    public Association association() {
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpChannel
    public SctpChannel bind(SocketAddress socketAddress) throws IOException {
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpChannel
    public SctpChannel bindAddress(InetAddress inetAddress) throws IOException {
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpChannel
    public SctpChannel unbindAddress(InetAddress inetAddress) throws IOException {
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpChannel
    public boolean connect(SocketAddress socketAddress) throws IOException {
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpChannel
    public boolean connect(SocketAddress socketAddress, int i2, int i3) throws IOException {
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpChannel
    public boolean isConnectionPending() {
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpChannel
    public boolean finishConnect() throws IOException {
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpChannel
    public Set<SocketAddress> getAllLocalAddresses() throws IOException {
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpChannel
    public Set<SocketAddress> getRemoteAddresses() throws IOException {
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpChannel
    public SctpChannel shutdown() throws IOException {
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpChannel
    public <T> T getOption(SctpSocketOption<T> sctpSocketOption) throws IOException {
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpChannel
    public <T> SctpChannel setOption(SctpSocketOption<T> sctpSocketOption, T t2) throws IOException {
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpChannel
    public Set<SctpSocketOption<?>> supportedOptions() {
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpChannel
    public <T> MessageInfo receive(ByteBuffer byteBuffer, T t2, NotificationHandler<T> notificationHandler) throws IOException {
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpChannel
    public int send(ByteBuffer byteBuffer, MessageInfo messageInfo) throws IOException {
        throw new UnsupportedOperationException(message);
    }

    @Override // java.nio.channels.spi.AbstractSelectableChannel
    protected void implConfigureBlocking(boolean z2) throws IOException {
        throw new UnsupportedOperationException(message);
    }

    @Override // java.nio.channels.spi.AbstractSelectableChannel
    public void implCloseSelectableChannel() throws IOException {
        throw new UnsupportedOperationException(message);
    }
}
