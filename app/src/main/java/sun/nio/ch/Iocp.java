package sun.nio.ch;

import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.channels.Channel;
import java.nio.channels.ShutdownChannelGroupException;
import java.nio.channels.spi.AsynchronousChannelProvider;
import java.security.AccessController;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import sun.misc.Unsafe;
import sun.nio.ch.Invoker;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/nio/ch/Iocp.class */
class Iocp extends AsynchronousChannelGroupImpl {
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final long INVALID_HANDLE_VALUE = -1;
    private static final boolean supportsThreadAgnosticIo;
    private final ReadWriteLock keyToChannelLock;
    private final Map<Integer, OverlappedChannel> keyToChannel;
    private int nextCompletionKey;
    private final long port;
    private boolean closed;
    private final Set<Long> staleIoSet;

    /* loaded from: rt.jar:sun/nio/ch/Iocp$OverlappedChannel.class */
    interface OverlappedChannel extends Closeable {
        <V, A> PendingFuture<V, A> getByOverlapped(long j2);
    }

    /* loaded from: rt.jar:sun/nio/ch/Iocp$ResultHandler.class */
    interface ResultHandler {
        void completed(int i2, boolean z2);

        void failed(int i2, IOException iOException);
    }

    private static native void initIDs();

    private static native long createIoCompletionPort(long j2, long j3, int i2, int i3) throws IOException;

    private static native void close0(long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void getQueuedCompletionStatus(long j2, CompletionStatus completionStatus) throws IOException;

    private static native void postQueuedCompletionStatus(long j2, int i2) throws IOException;

    private static native String getErrorMessage(int i2);

    static {
        IOUtil.load();
        initIDs();
        supportsThreadAgnosticIo = Integer.parseInt(((String) AccessController.doPrivileged(new GetPropertyAction("os.version"))).split("\\.")[0]) >= 6;
    }

    Iocp(AsynchronousChannelProvider asynchronousChannelProvider, ThreadPool threadPool) throws IOException {
        super(asynchronousChannelProvider, threadPool);
        this.keyToChannelLock = new ReentrantReadWriteLock();
        this.keyToChannel = new HashMap();
        this.staleIoSet = new HashSet();
        this.port = createIoCompletionPort(-1L, 0L, 0, fixedThreadCount());
        this.nextCompletionKey = 1;
    }

    Iocp start() {
        startThreads(new EventHandlerTask());
        return this;
    }

    static boolean supportsThreadAgnosticIo() {
        return supportsThreadAgnosticIo;
    }

    void implClose() {
        synchronized (this) {
            if (this.closed) {
                return;
            }
            this.closed = true;
            close0(this.port);
            synchronized (this.staleIoSet) {
                Iterator<Long> it = this.staleIoSet.iterator();
                while (it.hasNext()) {
                    unsafe.freeMemory(it.next().longValue());
                }
                this.staleIoSet.clear();
            }
        }
    }

    @Override // sun.nio.ch.AsynchronousChannelGroupImpl
    boolean isEmpty() {
        this.keyToChannelLock.writeLock().lock();
        try {
            return this.keyToChannel.isEmpty();
        } finally {
            this.keyToChannelLock.writeLock().unlock();
        }
    }

    @Override // sun.nio.ch.AsynchronousChannelGroupImpl
    final Object attachForeignChannel(final Channel channel, FileDescriptor fileDescriptor) throws IOException {
        return Integer.valueOf(associate(new OverlappedChannel() { // from class: sun.nio.ch.Iocp.1
            @Override // sun.nio.ch.Iocp.OverlappedChannel
            public <V, A> PendingFuture<V, A> getByOverlapped(long j2) {
                return null;
            }

            @Override // java.io.Closeable, java.lang.AutoCloseable
            public void close() throws IOException {
                channel.close();
            }
        }, 0L));
    }

    @Override // sun.nio.ch.AsynchronousChannelGroupImpl
    final void detachForeignChannel(Object obj) {
        disassociate(((Integer) obj).intValue());
    }

    @Override // sun.nio.ch.AsynchronousChannelGroupImpl
    void closeAllChannels() {
        int i2;
        OverlappedChannel[] overlappedChannelArr = new OverlappedChannel[32];
        do {
            this.keyToChannelLock.writeLock().lock();
            i2 = 0;
            try {
                Iterator<Integer> it = this.keyToChannel.keySet().iterator();
                while (it.hasNext()) {
                    int i3 = i2;
                    i2++;
                    overlappedChannelArr[i3] = this.keyToChannel.get(it.next());
                    if (i2 >= 32) {
                        break;
                    }
                }
                for (int i4 = 0; i4 < i2; i4++) {
                    try {
                        overlappedChannelArr[i4].close();
                    } catch (IOException e2) {
                    }
                }
            } finally {
                this.keyToChannelLock.writeLock().unlock();
            }
        } while (i2 > 0);
    }

    private void wakeup() {
        try {
            postQueuedCompletionStatus(this.port, 0);
        } catch (IOException e2) {
            throw new AssertionError(e2);
        }
    }

    @Override // sun.nio.ch.AsynchronousChannelGroupImpl
    void executeOnHandlerTask(Runnable runnable) {
        synchronized (this) {
            if (this.closed) {
                throw new RejectedExecutionException();
            }
            offerTask(runnable);
            wakeup();
        }
    }

    @Override // sun.nio.ch.AsynchronousChannelGroupImpl
    void shutdownHandlerTasks() {
        int iThreadCount = threadCount();
        while (true) {
            int i2 = iThreadCount;
            iThreadCount--;
            if (i2 > 0) {
                wakeup();
            } else {
                return;
            }
        }
    }

    int associate(OverlappedChannel overlappedChannel, long j2) throws IOException {
        int i2;
        this.keyToChannelLock.writeLock().lock();
        try {
            if (isShutdown()) {
                throw new ShutdownChannelGroupException();
            }
            while (true) {
                i2 = this.nextCompletionKey;
                this.nextCompletionKey = i2 + 1;
                if (i2 != 0 && !this.keyToChannel.containsKey(Integer.valueOf(i2))) {
                    break;
                }
            }
            if (j2 != 0) {
                createIoCompletionPort(j2, this.port, i2, 0);
            }
            this.keyToChannel.put(Integer.valueOf(i2), overlappedChannel);
            this.keyToChannelLock.writeLock().unlock();
            return i2;
        } catch (Throwable th) {
            this.keyToChannelLock.writeLock().unlock();
            throw th;
        }
    }

    void disassociate(int i2) {
        boolean z2 = false;
        this.keyToChannelLock.writeLock().lock();
        try {
            this.keyToChannel.remove(Integer.valueOf(i2));
            if (this.keyToChannel.isEmpty()) {
                z2 = true;
            }
            if (z2 && isShutdown()) {
                try {
                    shutdownNow();
                } catch (IOException e2) {
                }
            }
        } finally {
            this.keyToChannelLock.writeLock().unlock();
        }
    }

    void makeStale(Long l2) {
        synchronized (this.staleIoSet) {
            this.staleIoSet.add(l2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkIfStale(long j2) {
        synchronized (this.staleIoSet) {
            if (this.staleIoSet.remove(Long.valueOf(j2))) {
                unsafe.freeMemory(j2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static IOException translateErrorToIOException(int i2) {
        String errorMessage = getErrorMessage(i2);
        if (errorMessage == null) {
            errorMessage = "Unknown error: 0x0" + Integer.toHexString(i2);
        }
        return new IOException(errorMessage);
    }

    /* loaded from: rt.jar:sun/nio/ch/Iocp$EventHandlerTask.class */
    private class EventHandlerTask implements Runnable {
        private EventHandlerTask() {
        }

        /* JADX WARN: Finally extract failed */
        @Override // java.lang.Runnable
        public void run() {
            Invoker.GroupAndInvokeCount groupAndInvokeCount = Invoker.getGroupAndInvokeCount();
            boolean z2 = groupAndInvokeCount != null;
            CompletionStatus completionStatus = new CompletionStatus();
            boolean z3 = false;
            while (true) {
                if (groupAndInvokeCount != null) {
                    try {
                        groupAndInvokeCount.resetInvokeCount();
                    } catch (Throwable th) {
                        if (Iocp.this.threadExit(this, z3) == 0 && Iocp.this.isShutdown()) {
                            Iocp.this.implClose();
                        }
                        throw th;
                    }
                }
                z3 = false;
                try {
                    Iocp.getQueuedCompletionStatus(Iocp.this.port, completionStatus);
                    if (completionStatus.completionKey() != 0 || completionStatus.overlapped() != 0) {
                        Iocp.this.keyToChannelLock.readLock().lock();
                        try {
                            OverlappedChannel overlappedChannel = (OverlappedChannel) Iocp.this.keyToChannel.get(Integer.valueOf(completionStatus.completionKey()));
                            if (overlappedChannel == null) {
                                Iocp.this.checkIfStale(completionStatus.overlapped());
                                Iocp.this.keyToChannelLock.readLock().unlock();
                            } else {
                                Iocp.this.keyToChannelLock.readLock().unlock();
                                PendingFuture byOverlapped = overlappedChannel.getByOverlapped(completionStatus.overlapped());
                                if (byOverlapped == null) {
                                    Iocp.this.checkIfStale(completionStatus.overlapped());
                                } else {
                                    synchronized (byOverlapped) {
                                        if (!byOverlapped.isDone()) {
                                            int iError = completionStatus.error();
                                            ResultHandler resultHandler = (ResultHandler) byOverlapped.getContext();
                                            z3 = true;
                                            if (iError == 0) {
                                                resultHandler.completed(completionStatus.bytesTransferred(), z2);
                                            } else {
                                                resultHandler.failed(iError, Iocp.translateErrorToIOException(iError));
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Throwable th2) {
                            Iocp.this.keyToChannelLock.readLock().unlock();
                            throw th2;
                        }
                    } else {
                        Runnable runnablePollTask = Iocp.this.pollTask();
                        if (runnablePollTask == null) {
                            break;
                        }
                        z3 = true;
                        runnablePollTask.run();
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                    if (Iocp.this.threadExit(this, false) != 0 || !Iocp.this.isShutdown()) {
                        return;
                    }
                    Iocp.this.implClose();
                    return;
                }
            }
            if (Iocp.this.threadExit(this, false) != 0 || !Iocp.this.isShutdown()) {
                return;
            }
            Iocp.this.implClose();
        }
    }

    /* loaded from: rt.jar:sun/nio/ch/Iocp$CompletionStatus.class */
    private static class CompletionStatus {
        private int error;
        private int bytesTransferred;
        private int completionKey;
        private long overlapped;

        private CompletionStatus() {
        }

        int error() {
            return this.error;
        }

        int bytesTransferred() {
            return this.bytesTransferred;
        }

        int completionKey() {
            return this.completionKey;
        }

        long overlapped() {
            return this.overlapped;
        }
    }
}
