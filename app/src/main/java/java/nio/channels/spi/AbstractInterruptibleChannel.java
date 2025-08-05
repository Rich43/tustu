package java.nio.channels.spi;

import java.io.IOException;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.Channel;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.InterruptibleChannel;
import sun.misc.SharedSecrets;
import sun.nio.ch.Interruptible;

/* loaded from: rt.jar:java/nio/channels/spi/AbstractInterruptibleChannel.class */
public abstract class AbstractInterruptibleChannel implements Channel, InterruptibleChannel {
    private final Object closeLock = new Object();
    private volatile boolean open = true;
    private Interruptible interruptor;
    private volatile Thread interrupted;

    protected abstract void implCloseChannel() throws IOException;

    protected AbstractInterruptibleChannel() {
    }

    @Override // java.nio.channels.Channel, java.io.Closeable, java.lang.AutoCloseable
    public final void close() throws IOException {
        synchronized (this.closeLock) {
            if (this.open) {
                this.open = false;
                implCloseChannel();
            }
        }
    }

    @Override // java.nio.channels.Channel
    public final boolean isOpen() {
        return this.open;
    }

    protected final void begin() {
        if (this.interruptor == null) {
            this.interruptor = new Interruptible() { // from class: java.nio.channels.spi.AbstractInterruptibleChannel.1
                @Override // sun.nio.ch.Interruptible
                public void interrupt(Thread thread) {
                    synchronized (AbstractInterruptibleChannel.this.closeLock) {
                        if (AbstractInterruptibleChannel.this.open) {
                            AbstractInterruptibleChannel.this.open = false;
                            AbstractInterruptibleChannel.this.interrupted = thread;
                            try {
                                AbstractInterruptibleChannel.this.implCloseChannel();
                            } catch (IOException e2) {
                            }
                        }
                    }
                }
            };
        }
        blockedOn(this.interruptor);
        Thread threadCurrentThread = Thread.currentThread();
        if (threadCurrentThread.isInterrupted()) {
            this.interruptor.interrupt(threadCurrentThread);
        }
    }

    protected final void end(boolean z2) throws AsynchronousCloseException {
        blockedOn(null);
        Thread thread = this.interrupted;
        if (thread != null && thread == Thread.currentThread()) {
            throw new ClosedByInterruptException();
        }
        if (!z2 && !this.open) {
            throw new AsynchronousCloseException();
        }
    }

    static void blockedOn(Interruptible interruptible) {
        SharedSecrets.getJavaLangAccess().blockedOn(Thread.currentThread(), interruptible);
    }
}
