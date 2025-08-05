package java.nio.channels;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.spi.SelectorProvider;
import java.util.Set;

/* loaded from: rt.jar:java/nio/channels/Selector.class */
public abstract class Selector implements Closeable {
    public abstract boolean isOpen();

    public abstract SelectorProvider provider();

    public abstract Set<SelectionKey> keys();

    public abstract Set<SelectionKey> selectedKeys();

    public abstract int selectNow() throws IOException;

    public abstract int select(long j2) throws IOException;

    public abstract int select() throws IOException;

    public abstract Selector wakeup();

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public abstract void close() throws IOException;

    protected Selector() {
    }

    public static Selector open() throws IOException {
        return SelectorProvider.provider().openSelector();
    }
}
