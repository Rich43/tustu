package sun.nio.fs;

import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/* loaded from: rt.jar:sun/nio/fs/AbstractPoller.class */
abstract class AbstractPoller implements Runnable {
    private final LinkedList<Request> requestList = new LinkedList<>();
    private boolean shutdown = false;

    /* loaded from: rt.jar:sun/nio/fs/AbstractPoller$RequestType.class */
    private enum RequestType {
        REGISTER,
        CANCEL,
        CLOSE
    }

    abstract void wakeup() throws IOException;

    abstract Object implRegister(Path path, Set<? extends WatchEvent.Kind<?>> set, WatchEvent.Modifier... modifierArr);

    abstract void implCancelKey(WatchKey watchKey);

    abstract void implCloseAll();

    protected AbstractPoller() {
    }

    public void start() {
        AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: sun.nio.fs.AbstractPoller.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                Thread thread = new Thread(this);
                thread.setDaemon(true);
                thread.start();
                return null;
            }
        });
    }

    final WatchKey register(Path path, WatchEvent.Kind<?>[] kindArr, WatchEvent.Modifier... modifierArr) throws IOException {
        if (path == null) {
            throw new NullPointerException();
        }
        HashSet hashSet = new HashSet(kindArr.length);
        for (WatchEvent.Kind<?> kind : kindArr) {
            if (kind == StandardWatchEventKinds.ENTRY_CREATE || kind == StandardWatchEventKinds.ENTRY_MODIFY || kind == StandardWatchEventKinds.ENTRY_DELETE) {
                hashSet.add(kind);
            } else if (kind != StandardWatchEventKinds.OVERFLOW) {
                if (kind == null) {
                    throw new NullPointerException("An element in event set is 'null'");
                }
                throw new UnsupportedOperationException(kind.name());
            }
        }
        if (hashSet.isEmpty()) {
            throw new IllegalArgumentException("No events to register");
        }
        return (WatchKey) invoke(RequestType.REGISTER, path, hashSet, modifierArr);
    }

    final void cancel(WatchKey watchKey) {
        try {
            invoke(RequestType.CANCEL, watchKey);
        } catch (IOException e2) {
            throw new AssertionError((Object) e2.getMessage());
        }
    }

    final void close() throws IOException {
        invoke(RequestType.CLOSE, new Object[0]);
    }

    /* loaded from: rt.jar:sun/nio/fs/AbstractPoller$Request.class */
    private static class Request {
        private final RequestType type;
        private final Object[] params;
        private boolean completed = false;
        private Object result = null;

        Request(RequestType requestType, Object... objArr) {
            this.type = requestType;
            this.params = objArr;
        }

        RequestType type() {
            return this.type;
        }

        Object[] parameters() {
            return this.params;
        }

        void release(Object obj) {
            synchronized (this) {
                this.completed = true;
                this.result = obj;
                notifyAll();
            }
        }

        Object awaitResult() {
            Object obj;
            boolean z2 = false;
            synchronized (this) {
                while (!this.completed) {
                    try {
                        wait();
                    } catch (InterruptedException e2) {
                        z2 = true;
                    }
                }
                if (z2) {
                    Thread.currentThread().interrupt();
                }
                obj = this.result;
            }
            return obj;
        }
    }

    private Object invoke(RequestType requestType, Object... objArr) throws IOException {
        Request request = new Request(requestType, objArr);
        synchronized (this.requestList) {
            if (this.shutdown) {
                throw new ClosedWatchServiceException();
            }
            this.requestList.add(request);
        }
        wakeup();
        Object objAwaitResult = request.awaitResult();
        if (objAwaitResult instanceof RuntimeException) {
            throw ((RuntimeException) objAwaitResult);
        }
        if (objAwaitResult instanceof IOException) {
            throw ((IOException) objAwaitResult);
        }
        return objAwaitResult;
    }

    boolean processRequests() {
        synchronized (this.requestList) {
            while (true) {
                Request requestPoll = this.requestList.poll();
                if (requestPoll != null) {
                    if (this.shutdown) {
                        requestPoll.release(new ClosedWatchServiceException());
                    }
                    switch (requestPoll.type()) {
                        case REGISTER:
                            Object[] objArrParameters = requestPoll.parameters();
                            requestPoll.release(implRegister((Path) objArrParameters[0], (Set) objArrParameters[1], (WatchEvent.Modifier[]) objArrParameters[2]));
                            break;
                        case CANCEL:
                            implCancelKey((WatchKey) requestPoll.parameters()[0]);
                            requestPoll.release(null);
                            break;
                        case CLOSE:
                            implCloseAll();
                            requestPoll.release(null);
                            this.shutdown = true;
                            break;
                        default:
                            requestPoll.release(new IOException("request not recognized"));
                            break;
                    }
                }
            }
        }
        return this.shutdown;
    }
}
