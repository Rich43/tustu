package sun.nio.ch.sctp;

import com.sun.nio.sctp.SctpChannel;
import com.sun.nio.sctp.SctpServerChannel;
import com.sun.nio.sctp.SctpSocketOption;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.nio.channels.spi.SelectorProvider;
import java.util.Set;

/* loaded from: rt.jar:sun/nio/ch/sctp/SctpServerChannelImpl.class */
public class SctpServerChannelImpl extends SctpServerChannel {
    private static final String message = "SCTP not supported on this platform";

    public SctpServerChannelImpl(SelectorProvider selectorProvider) {
        super(selectorProvider);
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpServerChannel
    public SctpChannel accept() throws IOException {
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpServerChannel
    public SctpServerChannel bind(SocketAddress socketAddress, int i2) throws IOException {
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpServerChannel
    public SctpServerChannel bindAddress(InetAddress inetAddress) throws IOException {
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpServerChannel
    public SctpServerChannel unbindAddress(InetAddress inetAddress) throws IOException {
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpServerChannel
    public Set<SocketAddress> getAllLocalAddresses() throws IOException {
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpServerChannel
    public <T> T getOption(SctpSocketOption<T> sctpSocketOption) throws IOException {
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpServerChannel
    public <T> SctpServerChannel setOption(SctpSocketOption<T> sctpSocketOption, T t2) throws IOException {
        throw new UnsupportedOperationException(message);
    }

    @Override // com.sun.nio.sctp.SctpServerChannel
    public Set<SctpSocketOption<?>> supportedOptions() {
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
