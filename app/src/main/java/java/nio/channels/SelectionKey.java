package java.nio.channels;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeBodyPart;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/* loaded from: rt.jar:java/nio/channels/SelectionKey.class */
public abstract class SelectionKey {
    public static final int OP_READ = 1;
    public static final int OP_WRITE = 4;
    public static final int OP_CONNECT = 8;
    public static final int OP_ACCEPT = 16;
    private volatile Object attachment = null;
    private static final AtomicReferenceFieldUpdater<SelectionKey, Object> attachmentUpdater = AtomicReferenceFieldUpdater.newUpdater(SelectionKey.class, Object.class, MimeBodyPart.ATTACHMENT);

    public abstract SelectableChannel channel();

    public abstract Selector selector();

    public abstract boolean isValid();

    public abstract void cancel();

    public abstract int interestOps();

    public abstract SelectionKey interestOps(int i2);

    public abstract int readyOps();

    protected SelectionKey() {
    }

    public final boolean isReadable() {
        return (readyOps() & 1) != 0;
    }

    public final boolean isWritable() {
        return (readyOps() & 4) != 0;
    }

    public final boolean isConnectable() {
        return (readyOps() & 8) != 0;
    }

    public final boolean isAcceptable() {
        return (readyOps() & 16) != 0;
    }

    public final Object attach(Object obj) {
        return attachmentUpdater.getAndSet(this, obj);
    }

    public final Object attachment() {
        return this.attachment;
    }
}
