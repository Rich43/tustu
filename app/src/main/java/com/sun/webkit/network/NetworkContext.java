package com.sun.webkit.network;

import com.sun.webkit.WebPage;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permission;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: jfxrt.jar:com/sun/webkit/network/NetworkContext.class */
final class NetworkContext {
    private static final int THREAD_POOL_SIZE = 20;
    private static final int DEFAULT_HTTP_MAX_CONNECTIONS = 5;
    private static final int BYTE_BUFFER_SIZE = 40960;
    private static final ByteBufferPool byteBufferPool;
    private static final Logger logger = Logger.getLogger(NetworkContext.class.getName());
    private static final long THREAD_POOL_KEEP_ALIVE_TIME = 10000;
    private static final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(20, 20, THREAD_POOL_KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), new URLLoaderThreadFactory());

    static {
        threadPool.allowCoreThreadTimeOut(true);
        byteBufferPool = ByteBufferPool.newInstance(BYTE_BUFFER_SIZE);
    }

    private NetworkContext() {
        throw new AssertionError();
    }

    private static boolean canHandleURL(String url) {
        URL u2 = null;
        try {
            u2 = URLs.newURL(url);
        } catch (MalformedURLException e2) {
        }
        return u2 != null;
    }

    private static URLLoader fwkLoad(WebPage webPage, boolean asynchronous, String url, String method, String headers, FormDataElement[] formDataElements, long data) {
        if (logger.isLoggable(Level.FINEST)) {
            Logger logger2 = logger;
            Level level = Level.FINEST;
            Object[] objArr = new Object[7];
            objArr[0] = webPage;
            objArr[1] = Boolean.valueOf(asynchronous);
            objArr[2] = url;
            objArr[3] = method;
            objArr[4] = formDataElements != null ? Arrays.asList(formDataElements) : "[null]";
            objArr[5] = Long.valueOf(data);
            objArr[6] = Util.formatHeaders(headers);
            logger2.log(level, String.format("webPage: [%s], asynchronous: [%s], url: [%s], method: [%s], formDataElements: %s, data: [0x%016X], headers:%n%s", objArr));
        }
        URLLoader loader = new URLLoader(webPage, byteBufferPool, asynchronous, url, method, headers, formDataElements, data);
        if (asynchronous) {
            threadPool.submit(loader);
            if (logger.isLoggable(Level.FINEST)) {
                logger.log(Level.FINEST, "active count: [{0}], pool size: [{1}], max pool size: [{2}], task count: [{3}], completed task count: [{4}]", new Object[]{Integer.valueOf(threadPool.getActiveCount()), Integer.valueOf(threadPool.getPoolSize()), Integer.valueOf(threadPool.getMaximumPoolSize()), Long.valueOf(threadPool.getTaskCount()), Long.valueOf(threadPool.getCompletedTaskCount())});
            }
            return loader;
        }
        loader.run();
        return null;
    }

    private static int fwkGetMaximumHTTPConnectionCountPerHost() {
        int propValue = ((Integer) AccessController.doPrivileged(() -> {
            return Integer.getInteger("http.maxConnections", -1);
        })).intValue();
        if (propValue >= 0) {
            return propValue;
        }
        return 5;
    }

    /* loaded from: jfxrt.jar:com/sun/webkit/network/NetworkContext$URLLoaderThreadFactory.class */
    private static final class URLLoaderThreadFactory implements ThreadFactory {
        private final ThreadGroup group;
        private final AtomicInteger index;
        private static final Permission modifyThreadGroupPerm = new RuntimePermission("modifyThreadGroup");
        private static final Permission modifyThreadPerm = new RuntimePermission("modifyThread");

        private URLLoaderThreadFactory() {
            this.index = new AtomicInteger(1);
            SecurityManager sm = System.getSecurityManager();
            this.group = sm != null ? sm.getThreadGroup() : Thread.currentThread().getThreadGroup();
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable r2) {
            return (Thread) AccessController.doPrivileged(() -> {
                Thread t2 = new Thread(this.group, r2, "URL-Loader-" + this.index.getAndIncrement());
                t2.setDaemon(true);
                if (t2.getPriority() != 5) {
                    t2.setPriority(5);
                }
                return t2;
            }, (AccessControlContext) null, modifyThreadGroupPerm, modifyThreadPerm);
        }
    }
}
