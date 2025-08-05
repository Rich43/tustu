package sun.nio.ch;

import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;

/* loaded from: rt.jar:sun/nio/ch/Secrets.class */
public final class Secrets {
    private Secrets() {
    }

    private static SelectorProvider provider() {
        SelectorProvider selectorProviderProvider = SelectorProvider.provider();
        if (!(selectorProviderProvider instanceof SelectorProviderImpl)) {
            throw new UnsupportedOperationException();
        }
        return selectorProviderProvider;
    }

    public static SocketChannel newSocketChannel(FileDescriptor fileDescriptor) {
        try {
            return new SocketChannelImpl(provider(), fileDescriptor, false);
        } catch (IOException e2) {
            throw new AssertionError(e2);
        }
    }

    public static ServerSocketChannel newServerSocketChannel(FileDescriptor fileDescriptor) {
        try {
            return new ServerSocketChannelImpl(provider(), fileDescriptor, false);
        } catch (IOException e2) {
            throw new AssertionError(e2);
        }
    }
}
