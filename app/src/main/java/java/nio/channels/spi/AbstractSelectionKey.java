package java.nio.channels.spi;

import java.nio.channels.SelectionKey;

/* loaded from: rt.jar:java/nio/channels/spi/AbstractSelectionKey.class */
public abstract class AbstractSelectionKey extends SelectionKey {
    private volatile boolean valid = true;

    protected AbstractSelectionKey() {
    }

    @Override // java.nio.channels.SelectionKey
    public final boolean isValid() {
        return this.valid;
    }

    void invalidate() {
        this.valid = false;
    }

    @Override // java.nio.channels.SelectionKey
    public final void cancel() {
        synchronized (this) {
            if (this.valid) {
                this.valid = false;
                ((AbstractSelector) selector()).cancel(this);
            }
        }
    }
}
