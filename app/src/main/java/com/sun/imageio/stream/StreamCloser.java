package com.sun.imageio.stream;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Set;
import java.util.WeakHashMap;
import javax.imageio.stream.ImageInputStream;

/* loaded from: rt.jar:com/sun/imageio/stream/StreamCloser.class */
public class StreamCloser {
    private static WeakHashMap<CloseAction, Object> toCloseQueue;
    private static Thread streamCloser;

    public static void addToQueue(CloseAction closeAction) {
        synchronized (StreamCloser.class) {
            if (toCloseQueue == null) {
                toCloseQueue = new WeakHashMap<>();
            }
            toCloseQueue.put(closeAction, null);
            if (streamCloser == null) {
                final Runnable runnable = new Runnable() { // from class: com.sun.imageio.stream.StreamCloser.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (StreamCloser.toCloseQueue != null) {
                            synchronized (StreamCloser.class) {
                                Set setKeySet = StreamCloser.toCloseQueue.keySet();
                                for (CloseAction closeAction2 : (CloseAction[]) setKeySet.toArray(new CloseAction[setKeySet.size()])) {
                                    if (closeAction2 != null) {
                                        try {
                                            closeAction2.performAction();
                                        } catch (IOException e2) {
                                        }
                                    }
                                }
                            }
                        }
                    }
                };
                AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.imageio.stream.StreamCloser.2
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public Object run2() {
                        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
                        ThreadGroup parent = threadGroup;
                        while (true) {
                            ThreadGroup threadGroup2 = parent;
                            if (threadGroup2 == null) {
                                Thread unused = StreamCloser.streamCloser = new Thread(threadGroup, runnable);
                                StreamCloser.streamCloser.setContextClassLoader(null);
                                Runtime.getRuntime().addShutdownHook(StreamCloser.streamCloser);
                                return null;
                            }
                            threadGroup = threadGroup2;
                            parent = threadGroup.getParent();
                        }
                    }
                });
            }
        }
    }

    public static void removeFromQueue(CloseAction closeAction) {
        synchronized (StreamCloser.class) {
            if (toCloseQueue != null) {
                toCloseQueue.remove(closeAction);
            }
        }
    }

    public static CloseAction createCloseAction(ImageInputStream imageInputStream) {
        return new CloseAction(imageInputStream);
    }

    /* loaded from: rt.jar:com/sun/imageio/stream/StreamCloser$CloseAction.class */
    public static final class CloseAction {
        private ImageInputStream iis;

        private CloseAction(ImageInputStream imageInputStream) {
            this.iis = imageInputStream;
        }

        public void performAction() throws IOException {
            if (this.iis != null) {
                this.iis.close();
            }
        }
    }
}
