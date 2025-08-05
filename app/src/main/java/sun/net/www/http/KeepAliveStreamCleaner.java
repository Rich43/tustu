package sun.net.www.http;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.LinkedList;
import sun.net.NetProperties;

/* loaded from: rt.jar:sun/net/www/http/KeepAliveStreamCleaner.class */
class KeepAliveStreamCleaner extends LinkedList<KeepAliveCleanerEntry> implements Runnable {
    protected static int MAX_DATA_REMAINING;
    protected static int MAX_CAPACITY;
    protected static final int TIMEOUT = 5000;
    private static final int MAX_RETRIES = 5;

    KeepAliveStreamCleaner() {
    }

    static {
        MAX_DATA_REMAINING = 512;
        MAX_CAPACITY = 10;
        MAX_DATA_REMAINING = ((Integer) AccessController.doPrivileged(new PrivilegedAction<Integer>() { // from class: sun.net.www.http.KeepAliveStreamCleaner.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Integer run2() {
                return NetProperties.getInteger("http.KeepAlive.remainingData", KeepAliveStreamCleaner.MAX_DATA_REMAINING);
            }
        })).intValue() * 1024;
        MAX_CAPACITY = ((Integer) AccessController.doPrivileged(new PrivilegedAction<Integer>() { // from class: sun.net.www.http.KeepAliveStreamCleaner.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Integer run2() {
                return NetProperties.getInteger("http.KeepAlive.queuedConnections", KeepAliveStreamCleaner.MAX_CAPACITY);
            }
        })).intValue();
    }

    @Override // java.util.LinkedList, java.util.Deque, java.util.Queue
    public boolean offer(KeepAliveCleanerEntry keepAliveCleanerEntry) {
        if (size() >= MAX_CAPACITY) {
            return false;
        }
        return super.offer((KeepAliveStreamCleaner) keepAliveCleanerEntry);
    }

    @Override // java.lang.Runnable
    public void run() {
        KeepAliveCleanerEntry keepAliveCleanerEntryPoll = null;
        do {
            try {
                synchronized (this) {
                    long jCurrentTimeMillis = System.currentTimeMillis();
                    long j2 = 5000;
                    while (true) {
                        KeepAliveCleanerEntry keepAliveCleanerEntryPoll2 = poll();
                        keepAliveCleanerEntryPoll = keepAliveCleanerEntryPoll2;
                        if (keepAliveCleanerEntryPoll2 != null) {
                            break;
                        }
                        wait(j2);
                        long jCurrentTimeMillis2 = System.currentTimeMillis();
                        long j3 = jCurrentTimeMillis2 - jCurrentTimeMillis;
                        if (j3 > j2) {
                            keepAliveCleanerEntryPoll = poll();
                            break;
                        } else {
                            jCurrentTimeMillis = jCurrentTimeMillis2;
                            j2 -= j3;
                        }
                    }
                }
            } catch (InterruptedException e2) {
            }
            if (keepAliveCleanerEntryPoll != null) {
                KeepAliveStream keepAliveStream = keepAliveCleanerEntryPoll.getKeepAliveStream();
                if (keepAliveStream != null) {
                    synchronized (keepAliveStream) {
                        HttpClient httpClient = keepAliveCleanerEntryPoll.getHttpClient();
                        if (httpClient != null) {
                            try {
                                try {
                                    if (!httpClient.isInKeepAliveCache()) {
                                        int readTimeout = httpClient.getReadTimeout();
                                        httpClient.setReadTimeout(5000);
                                        long jRemainingToRead = keepAliveStream.remainingToRead();
                                        if (jRemainingToRead > 0) {
                                            long jSkip = 0;
                                            int i2 = 0;
                                            while (jSkip < jRemainingToRead && i2 < 5) {
                                                jRemainingToRead -= jSkip;
                                                jSkip = keepAliveStream.skip(jRemainingToRead);
                                                if (jSkip == 0) {
                                                    i2++;
                                                }
                                            }
                                            jRemainingToRead -= jSkip;
                                        }
                                        if (jRemainingToRead == 0) {
                                            httpClient.setReadTimeout(readTimeout);
                                            httpClient.finished();
                                        } else {
                                            httpClient.closeServer();
                                        }
                                    }
                                    keepAliveStream.setClosed();
                                } catch (IOException e3) {
                                    httpClient.closeServer();
                                    keepAliveStream.setClosed();
                                }
                            } catch (Throwable th) {
                                keepAliveStream.setClosed();
                                throw th;
                            }
                        } else {
                            keepAliveStream.setClosed();
                        }
                    }
                }
            } else {
                return;
            }
        } while (keepAliveCleanerEntryPoll != null);
    }
}
