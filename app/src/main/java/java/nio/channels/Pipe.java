package java.nio.channels;

import java.io.IOException;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.nio.channels.spi.SelectorProvider;

/* loaded from: rt.jar:java/nio/channels/Pipe.class */
public abstract class Pipe {
    public abstract SourceChannel source();

    public abstract SinkChannel sink();

    /* loaded from: rt.jar:java/nio/channels/Pipe$SourceChannel.class */
    public static abstract class SourceChannel extends AbstractSelectableChannel implements ReadableByteChannel, ScatteringByteChannel {
        protected SourceChannel(SelectorProvider selectorProvider) {
            super(selectorProvider);
        }

        @Override // java.nio.channels.SelectableChannel
        public final int validOps() {
            return 1;
        }
    }

    /* loaded from: rt.jar:java/nio/channels/Pipe$SinkChannel.class */
    public static abstract class SinkChannel extends AbstractSelectableChannel implements WritableByteChannel, GatheringByteChannel {
        protected SinkChannel(SelectorProvider selectorProvider) {
            super(selectorProvider);
        }

        @Override // java.nio.channels.SelectableChannel
        public final int validOps() {
            return 4;
        }
    }

    protected Pipe() {
    }

    public static Pipe open() throws IOException {
        return SelectorProvider.provider().openPipe();
    }
}
