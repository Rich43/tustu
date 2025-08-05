package java.nio.channels;

import java.io.IOException;
import java.nio.channels.spi.AsynchronousChannelProvider;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/* loaded from: rt.jar:java/nio/channels/AsynchronousChannelGroup.class */
public abstract class AsynchronousChannelGroup {
    private final AsynchronousChannelProvider provider;

    public abstract boolean isShutdown();

    public abstract boolean isTerminated();

    public abstract void shutdown();

    public abstract void shutdownNow() throws IOException;

    public abstract boolean awaitTermination(long j2, TimeUnit timeUnit) throws InterruptedException;

    protected AsynchronousChannelGroup(AsynchronousChannelProvider asynchronousChannelProvider) {
        this.provider = asynchronousChannelProvider;
    }

    public final AsynchronousChannelProvider provider() {
        return this.provider;
    }

    public static AsynchronousChannelGroup withFixedThreadPool(int i2, ThreadFactory threadFactory) throws IOException {
        return AsynchronousChannelProvider.provider().openAsynchronousChannelGroup(i2, threadFactory);
    }

    public static AsynchronousChannelGroup withCachedThreadPool(ExecutorService executorService, int i2) throws IOException {
        return AsynchronousChannelProvider.provider().openAsynchronousChannelGroup(executorService, i2);
    }

    public static AsynchronousChannelGroup withThreadPool(ExecutorService executorService) throws IOException {
        return AsynchronousChannelProvider.provider().openAsynchronousChannelGroup(executorService, 0);
    }
}
