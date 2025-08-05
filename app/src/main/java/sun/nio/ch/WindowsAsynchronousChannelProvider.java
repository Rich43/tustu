package sun.nio.ch;

import java.io.IOException;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.IllegalChannelGroupException;
import java.nio.channels.spi.AsynchronousChannelProvider;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

/* loaded from: rt.jar:sun/nio/ch/WindowsAsynchronousChannelProvider.class */
public class WindowsAsynchronousChannelProvider extends AsynchronousChannelProvider {
    private static volatile Iocp defaultIocp;

    private Iocp defaultIocp() throws IOException {
        if (defaultIocp == null) {
            synchronized (WindowsAsynchronousChannelProvider.class) {
                if (defaultIocp == null) {
                    defaultIocp = new Iocp(this, ThreadPool.getDefault()).start();
                }
            }
        }
        return defaultIocp;
    }

    @Override // java.nio.channels.spi.AsynchronousChannelProvider
    public AsynchronousChannelGroup openAsynchronousChannelGroup(int i2, ThreadFactory threadFactory) throws IOException {
        return new Iocp(this, ThreadPool.create(i2, threadFactory)).start();
    }

    @Override // java.nio.channels.spi.AsynchronousChannelProvider
    public AsynchronousChannelGroup openAsynchronousChannelGroup(ExecutorService executorService, int i2) throws IOException {
        return new Iocp(this, ThreadPool.wrap(executorService, i2)).start();
    }

    private Iocp toIocp(AsynchronousChannelGroup asynchronousChannelGroup) throws IOException {
        if (asynchronousChannelGroup == null) {
            return defaultIocp();
        }
        if (!(asynchronousChannelGroup instanceof Iocp)) {
            throw new IllegalChannelGroupException();
        }
        return (Iocp) asynchronousChannelGroup;
    }

    @Override // java.nio.channels.spi.AsynchronousChannelProvider
    public AsynchronousServerSocketChannel openAsynchronousServerSocketChannel(AsynchronousChannelGroup asynchronousChannelGroup) throws IOException {
        return new WindowsAsynchronousServerSocketChannelImpl(toIocp(asynchronousChannelGroup));
    }

    @Override // java.nio.channels.spi.AsynchronousChannelProvider
    public AsynchronousSocketChannel openAsynchronousSocketChannel(AsynchronousChannelGroup asynchronousChannelGroup) throws IOException {
        return new WindowsAsynchronousSocketChannelImpl(toIocp(asynchronousChannelGroup));
    }
}
