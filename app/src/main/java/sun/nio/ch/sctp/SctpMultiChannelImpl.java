package sun.nio.ch.sctp;

import com.sun.nio.sctp.Association;
import com.sun.nio.sctp.MessageInfo;
import com.sun.nio.sctp.NotificationHandler;
import com.sun.nio.sctp.SctpChannel;
import com.sun.nio.sctp.SctpMultiChannel;
import com.sun.nio.sctp.SctpSocketOption;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.spi.SelectorProvider;
import java.util.Set;

/* loaded from: rt.jar:sun/nio/ch/sctp/SctpMultiChannelImpl.class */
public class SctpMultiChannelImpl extends SctpMultiChannel {
    private static final String message = "SCTP not supported on this platform";

    public SctpMultiChannelImpl(SelectorProvider selectorProvider) {
        super(selectorProvider);
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpMultiChannel
    public Set<Association> associations() {
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpMultiChannel
    public SctpMultiChannel bind(SocketAddress socketAddress, int i2) throws IOException {
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpMultiChannel
    public SctpMultiChannel bindAddress(InetAddress inetAddress) throws IOException {
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpMultiChannel
    public SctpMultiChannel unbindAddress(InetAddress inetAddress) throws IOException {
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpMultiChannel
    public Set<SocketAddress> getAllLocalAddresses() throws IOException {
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpMultiChannel
    public Set<SocketAddress> getRemoteAddresses(Association association) throws IOException {
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpMultiChannel
    public SctpMultiChannel shutdown(Association association) throws IOException {
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpMultiChannel
    public <T> T getOption(SctpSocketOption<T> sctpSocketOption, Association association) throws IOException {
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpMultiChannel
    public <T> SctpMultiChannel setOption(SctpSocketOption<T> sctpSocketOption, T t2, Association association) throws IOException {
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpMultiChannel
    public Set<SctpSocketOption<?>> supportedOptions() {
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpMultiChannel
    public <T> MessageInfo receive(ByteBuffer byteBuffer, T t2, NotificationHandler<T> notificationHandler) throws IOException {
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpMultiChannel
    public int send(ByteBuffer byteBuffer, MessageInfo messageInfo) throws IOException {
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpMultiChannel
    public SctpChannel branch(Association association) throws IOException {
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
