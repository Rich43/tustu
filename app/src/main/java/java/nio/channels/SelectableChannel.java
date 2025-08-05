package java.nio.channels;

import java.io.IOException;
import java.nio.channels.spi.AbstractInterruptibleChannel;
import java.nio.channels.spi.SelectorProvider;

/* loaded from: rt.jar:java/nio/channels/SelectableChannel.class */
public abstract class SelectableChannel extends AbstractInterruptibleChannel implements Channel {
    public abstract SelectorProvider provider();

    public abstract int validOps();

    public abstract boolean isRegistered();

    public abstract SelectionKey keyFor(Selector selector);

    public abstract SelectionKey register(Selector selector, int i2, Object obj) throws ClosedChannelException;

    public abstract SelectableChannel configureBlocking(boolean z2) throws IOException;

    public abstract boolean isBlocking();

    public abstract Object blockingLock();

    protected SelectableChannel() {
    }

    public final SelectionKey register(Selector selector, int i2) throws ClosedChannelException {
        return register(selector, i2, null);
    }
}
