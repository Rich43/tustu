package sun.nio.ch;

import java.io.IOException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.Pipe;
import java.nio.channels.spi.SelectorProvider;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.SecureRandom;
import java.util.Random;

/* loaded from: rt.jar:sun/nio/ch/PipeImpl.class */
class PipeImpl extends Pipe {
    private static final int NUM_SECRET_BYTES = 16;
    private static final Random RANDOM_NUMBER_GENERATOR = new SecureRandom();
    private Pipe.SourceChannel source;
    private Pipe.SinkChannel sink;

    /* loaded from: rt.jar:sun/nio/ch/PipeImpl$Initializer.class */
    private class Initializer implements PrivilegedExceptionAction<Void> {
        private final SelectorProvider sp;
        private IOException ioe;

        private Initializer(SelectorProvider selectorProvider) {
            this.ioe = null;
            this.sp = selectorProvider;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedExceptionAction
        public Void run() throws IOException {
            LoopbackConnector loopbackConnector = new LoopbackConnector();
            loopbackConnector.run();
            if (this.ioe instanceof ClosedByInterruptException) {
                this.ioe = null;
                Thread thread = new Thread(loopbackConnector) { // from class: sun.nio.ch.PipeImpl.Initializer.1
                    @Override // java.lang.Thread
                    public void interrupt() {
                    }
                };
                thread.start();
                while (true) {
                    try {
                        thread.join();
                        break;
                    } catch (InterruptedException e2) {
                    }
                }
                Thread.currentThread().interrupt();
            }
            if (this.ioe != null) {
                throw new IOException("Unable to establish loopback connection", this.ioe);
            }
            return null;
        }

        /* loaded from: rt.jar:sun/nio/ch/PipeImpl$Initializer$LoopbackConnector.class */
        private class LoopbackConnector implements Runnable {
            static final /* synthetic */ boolean $assertionsDisabled;

            private LoopbackConnector() {
            }

            static {
                $assertionsDisabled = !PipeImpl.class.desiredAssertionStatus();
            }

            /* JADX WARN: Removed duplicated region for block: B:64:0x0127 A[EXC_TOP_SPLITTER, SYNTHETIC] */
            @Override // java.lang.Runnable
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public void run() {
                /*
                    Method dump skipped, instructions count: 326
                    To view this dump change 'Code comments level' option to 'DEBUG'
                */
                throw new UnsupportedOperationException("Method not decompiled: sun.nio.ch.PipeImpl.Initializer.LoopbackConnector.run():void");
            }
        }
    }

    PipeImpl(SelectorProvider selectorProvider) throws IOException {
        try {
            AccessController.doPrivileged(new Initializer(selectorProvider));
        } catch (PrivilegedActionException e2) {
            throw ((IOException) e2.getCause());
        }
    }

    @Override // java.nio.channels.Pipe
    public Pipe.SourceChannel source() {
        return this.source;
    }

    @Override // java.nio.channels.Pipe
    public Pipe.SinkChannel sink() {
        return this.sink;
    }
}
