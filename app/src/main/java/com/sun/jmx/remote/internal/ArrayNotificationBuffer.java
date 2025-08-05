package com.sun.jmx.remote.internal;

import com.sun.jmx.remote.util.ClassLogger;
import com.sun.jmx.remote.util.EnvHelp;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServer;
import javax.management.MBeanServerDelegate;
import javax.management.MBeanServerNotification;
import javax.management.Notification;
import javax.management.NotificationBroadcaster;
import javax.management.NotificationFilter;
import javax.management.NotificationFilterSupport;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.QueryEval;
import javax.management.QueryExp;
import javax.management.remote.NotificationResult;

/* loaded from: rt.jar:com/sun/jmx/remote/internal/ArrayNotificationBuffer.class */
public class ArrayNotificationBuffer implements NotificationBuffer {
    private static final NotificationFilter creationFilter;
    private static final ClassLogger logger;
    private final MBeanServer mBeanServer;
    private final ArrayQueue<NamedNotification> queue;
    private int queueSize;
    private long earliestSequenceNumber;
    private long nextSequenceNumber;
    private Set<ObjectName> createdDuringQuery;
    static final String broadcasterClass;
    private static final Object globalLock = new Object();
    private static final HashMap<MBeanServer, ArrayNotificationBuffer> mbsToBuffer = new HashMap<>(1);
    private static final QueryExp broadcasterQuery = new BroadcasterQuery();
    private boolean disposed = false;
    private final Collection<ShareBuffer> sharers = new HashSet(1);
    private final NotificationListener bufferListener = new BufferListener();
    private final NotificationListener creationListener = new NotificationListener() { // from class: com.sun.jmx.remote.internal.ArrayNotificationBuffer.5
        @Override // javax.management.NotificationListener
        public void handleNotification(Notification notification, Object obj) {
            ArrayNotificationBuffer.logger.debug("creationListener", "handleNotification called");
            ArrayNotificationBuffer.this.createdNotification((MBeanServerNotification) notification);
        }
    };

    static {
        NotificationFilterSupport notificationFilterSupport = new NotificationFilterSupport();
        notificationFilterSupport.enableType(MBeanServerNotification.REGISTRATION_NOTIFICATION);
        creationFilter = notificationFilterSupport;
        logger = new ClassLogger("javax.management.remote.misc", "ArrayNotificationBuffer");
        broadcasterClass = NotificationBroadcaster.class.getName();
    }

    public static NotificationBuffer getNotificationBuffer(MBeanServer mBeanServer, Map<String, ?> map) {
        ArrayNotificationBuffer arrayNotificationBuffer;
        boolean z2;
        ShareBuffer shareBuffer;
        if (map == null) {
            map = Collections.emptyMap();
        }
        int notifBufferSize = EnvHelp.getNotifBufferSize(map);
        synchronized (globalLock) {
            arrayNotificationBuffer = mbsToBuffer.get(mBeanServer);
            z2 = arrayNotificationBuffer == null;
            if (z2) {
                arrayNotificationBuffer = new ArrayNotificationBuffer(mBeanServer, notifBufferSize);
                mbsToBuffer.put(mBeanServer, arrayNotificationBuffer);
            }
            ArrayNotificationBuffer arrayNotificationBuffer2 = arrayNotificationBuffer;
            arrayNotificationBuffer2.getClass();
            shareBuffer = arrayNotificationBuffer2.new ShareBuffer(notifBufferSize);
        }
        if (z2) {
            arrayNotificationBuffer.createListeners();
        }
        return shareBuffer;
    }

    static void removeNotificationBuffer(MBeanServer mBeanServer) {
        synchronized (globalLock) {
            mbsToBuffer.remove(mBeanServer);
        }
    }

    void addSharer(ShareBuffer shareBuffer) {
        synchronized (globalLock) {
            synchronized (this) {
                if (shareBuffer.getSize() > this.queueSize) {
                    resize(shareBuffer.getSize());
                }
            }
            this.sharers.add(shareBuffer);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeSharer(ShareBuffer shareBuffer) {
        boolean zIsEmpty;
        synchronized (globalLock) {
            this.sharers.remove(shareBuffer);
            zIsEmpty = this.sharers.isEmpty();
            if (zIsEmpty) {
                removeNotificationBuffer(this.mBeanServer);
            } else {
                int i2 = 0;
                Iterator<ShareBuffer> it = this.sharers.iterator();
                while (it.hasNext()) {
                    int size = it.next().getSize();
                    if (size > i2) {
                        i2 = size;
                    }
                }
                if (i2 < this.queueSize) {
                    resize(i2);
                }
            }
        }
        if (zIsEmpty) {
            synchronized (this) {
                this.disposed = true;
                notifyAll();
            }
            destroyListeners();
        }
    }

    private synchronized void resize(int i2) {
        if (i2 == this.queueSize) {
            return;
        }
        while (this.queue.size() > i2) {
            dropNotification();
        }
        this.queue.resize(i2);
        this.queueSize = i2;
    }

    /* loaded from: rt.jar:com/sun/jmx/remote/internal/ArrayNotificationBuffer$ShareBuffer.class */
    private class ShareBuffer implements NotificationBuffer {
        private final int size;

        ShareBuffer(int i2) {
            this.size = i2;
            ArrayNotificationBuffer.this.addSharer(this);
        }

        @Override // com.sun.jmx.remote.internal.NotificationBuffer
        public NotificationResult fetchNotifications(NotificationBufferFilter notificationBufferFilter, long j2, long j3, int i2) throws InterruptedException {
            return ArrayNotificationBuffer.this.fetchNotifications(notificationBufferFilter, j2, j3, i2);
        }

        @Override // com.sun.jmx.remote.internal.NotificationBuffer
        public void dispose() {
            ArrayNotificationBuffer.this.removeSharer(this);
        }

        int getSize() {
            return this.size;
        }
    }

    private ArrayNotificationBuffer(MBeanServer mBeanServer, int i2) {
        if (logger.traceOn()) {
            logger.trace("Constructor", "queueSize=" + i2);
        }
        if (mBeanServer == null || i2 < 1) {
            throw new IllegalArgumentException("Bad args");
        }
        this.mBeanServer = mBeanServer;
        this.queueSize = i2;
        this.queue = new ArrayQueue<>(i2);
        this.earliestSequenceNumber = System.currentTimeMillis();
        this.nextSequenceNumber = this.earliestSequenceNumber;
        logger.trace("Constructor", "ends");
    }

    private synchronized boolean isDisposed() {
        return this.disposed;
    }

    @Override // com.sun.jmx.remote.internal.NotificationBuffer
    public void dispose() {
        throw new UnsupportedOperationException();
    }

    /* JADX WARN: Code restructure failed: missing block: B:103:0x0320, code lost:
    
        r0 = r0.getObjectName();
        r0 = r0.getNotification();
        r0 = new java.util.ArrayList();
        com.sun.jmx.remote.internal.ArrayNotificationBuffer.logger.debug("fetchNotifications", "applying filter to candidate");
        r9.apply(r0, r0, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x0354, code lost:
    
        if (r0.size() <= 0) goto L112;
     */
    /* JADX WARN: Code restructure failed: missing block: B:106:0x0359, code lost:
    
        if (r14 > 0) goto L108;
     */
    /* JADX WARN: Code restructure failed: missing block: B:107:0x035c, code lost:
    
        com.sun.jmx.remote.internal.ArrayNotificationBuffer.logger.debug("fetchNotifications", "reached maxNotifications");
     */
    /* JADX WARN: Code restructure failed: missing block: B:108:0x0369, code lost:
    
        r14 = r14 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:109:0x0372, code lost:
    
        if (com.sun.jmx.remote.internal.ArrayNotificationBuffer.logger.debugOn() == false) goto L111;
     */
    /* JADX WARN: Code restructure failed: missing block: B:110:0x0375, code lost:
    
        com.sun.jmx.remote.internal.ArrayNotificationBuffer.logger.debug("fetchNotifications", "add: " + ((java.lang.Object) r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:111:0x0391, code lost:
    
        r0.addAll(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:112:0x039b, code lost:
    
        r19 = r19 + 1;
     */
    @Override // com.sun.jmx.remote.internal.NotificationBuffer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public javax.management.remote.NotificationResult fetchNotifications(com.sun.jmx.remote.internal.NotificationBufferFilter r9, long r10, long r12, int r14) throws java.lang.InterruptedException {
        /*
            Method dump skipped, instructions count: 1008
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.jmx.remote.internal.ArrayNotificationBuffer.fetchNotifications(com.sun.jmx.remote.internal.NotificationBufferFilter, long, long, int):javax.management.remote.NotificationResult");
    }

    synchronized long earliestSequenceNumber() {
        return this.earliestSequenceNumber;
    }

    synchronized long nextSequenceNumber() {
        return this.nextSequenceNumber;
    }

    synchronized void addNotification(NamedNotification namedNotification) {
        if (logger.traceOn()) {
            logger.trace("addNotification", namedNotification.toString());
        }
        while (this.queue.size() >= this.queueSize) {
            dropNotification();
            if (logger.debugOn()) {
                logger.debug("addNotification", "dropped oldest notif, earliestSeq=" + this.earliestSequenceNumber);
            }
        }
        this.queue.add(namedNotification);
        this.nextSequenceNumber++;
        if (logger.debugOn()) {
            logger.debug("addNotification", "nextSeq=" + this.nextSequenceNumber);
        }
        notifyAll();
    }

    private void dropNotification() {
        this.queue.remove(0);
        this.earliestSequenceNumber++;
    }

    synchronized NamedNotification notificationAt(long j2) {
        long j3 = j2 - this.earliestSequenceNumber;
        if (j3 < 0 || j3 > 2147483647L) {
            String str = "Bad sequence number: " + j2 + " (earliest " + this.earliestSequenceNumber + ")";
            logger.trace("notificationAt", str);
            throw new IllegalArgumentException(str);
        }
        return this.queue.get((int) j3);
    }

    /* loaded from: rt.jar:com/sun/jmx/remote/internal/ArrayNotificationBuffer$NamedNotification.class */
    private static class NamedNotification {
        private final ObjectName sender;
        private final Notification notification;

        NamedNotification(ObjectName objectName, Notification notification) {
            this.sender = objectName;
            this.notification = notification;
        }

        ObjectName getObjectName() {
            return this.sender;
        }

        Notification getNotification() {
            return this.notification;
        }

        public String toString() {
            return "NamedNotification(" + ((Object) this.sender) + ", " + ((Object) this.notification) + ")";
        }
    }

    private void createListeners() {
        logger.debug("createListeners", "starts");
        synchronized (this) {
            this.createdDuringQuery = new HashSet();
        }
        try {
            addNotificationListener(MBeanServerDelegate.DELEGATE_NAME, this.creationListener, creationFilter, null);
            logger.debug("createListeners", "added creationListener");
            HashSet hashSet = new HashSet(queryNames(null, broadcasterQuery));
            synchronized (this) {
                hashSet.addAll(this.createdDuringQuery);
                this.createdDuringQuery = null;
            }
            Iterator<E> it = hashSet.iterator();
            while (it.hasNext()) {
                addBufferListener((ObjectName) it.next());
            }
            logger.debug("createListeners", "ends");
        } catch (Exception e2) {
            IllegalArgumentException illegalArgumentException = new IllegalArgumentException("Can't add listener to MBean server delegate: " + ((Object) e2));
            EnvHelp.initCause(illegalArgumentException, e2);
            logger.fine("createListeners", "Can't add listener to MBean server delegate: " + ((Object) e2));
            logger.debug("createListeners", e2);
            throw illegalArgumentException;
        }
    }

    private void addBufferListener(ObjectName objectName) {
        checkNoLocks();
        if (logger.debugOn()) {
            logger.debug("addBufferListener", objectName.toString());
        }
        try {
            addNotificationListener(objectName, this.bufferListener, null, objectName);
        } catch (Exception e2) {
            logger.trace("addBufferListener", e2);
        }
    }

    private void removeBufferListener(ObjectName objectName) {
        checkNoLocks();
        if (logger.debugOn()) {
            logger.debug("removeBufferListener", objectName.toString());
        }
        try {
            removeNotificationListener(objectName, this.bufferListener);
        } catch (Exception e2) {
            logger.trace("removeBufferListener", e2);
        }
    }

    private void addNotificationListener(final ObjectName objectName, final NotificationListener notificationListener, final NotificationFilter notificationFilter, final Object obj) throws Exception {
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() { // from class: com.sun.jmx.remote.internal.ArrayNotificationBuffer.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public Void run() throws InstanceNotFoundException {
                    ArrayNotificationBuffer.this.mBeanServer.addNotificationListener(objectName, notificationListener, notificationFilter, obj);
                    return null;
                }
            });
        } catch (Exception e2) {
            throw extractException(e2);
        }
    }

    private void removeNotificationListener(final ObjectName objectName, final NotificationListener notificationListener) throws Exception {
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() { // from class: com.sun.jmx.remote.internal.ArrayNotificationBuffer.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public Void run() throws Exception {
                    ArrayNotificationBuffer.this.mBeanServer.removeNotificationListener(objectName, notificationListener);
                    return null;
                }
            });
        } catch (Exception e2) {
            throw extractException(e2);
        }
    }

    private Set<ObjectName> queryNames(final ObjectName objectName, final QueryExp queryExp) {
        try {
            return (Set) AccessController.doPrivileged(new PrivilegedAction<Set<ObjectName>>() { // from class: com.sun.jmx.remote.internal.ArrayNotificationBuffer.3
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Set<ObjectName> run2() {
                    return ArrayNotificationBuffer.this.mBeanServer.queryNames(objectName, queryExp);
                }
            });
        } catch (RuntimeException e2) {
            logger.fine("queryNames", "Failed to query names: " + ((Object) e2));
            logger.debug("queryNames", e2);
            throw e2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isInstanceOf(final MBeanServer mBeanServer, final ObjectName objectName, final String str) {
        try {
            return ((Boolean) AccessController.doPrivileged(new PrivilegedExceptionAction<Boolean>() { // from class: com.sun.jmx.remote.internal.ArrayNotificationBuffer.4
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public Boolean run() throws InstanceNotFoundException {
                    return Boolean.valueOf(mBeanServer.isInstanceOf(objectName, str));
                }
            })).booleanValue();
        } catch (Exception e2) {
            logger.fine("isInstanceOf", "failed: " + ((Object) e2));
            logger.debug("isInstanceOf", e2);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createdNotification(MBeanServerNotification mBeanServerNotification) {
        if (!mBeanServerNotification.getType().equals(MBeanServerNotification.REGISTRATION_NOTIFICATION)) {
            logger.warning("createNotification", "bad type: " + mBeanServerNotification.getType());
            return;
        }
        ObjectName mBeanName = mBeanServerNotification.getMBeanName();
        if (logger.debugOn()) {
            logger.debug("createdNotification", "for: " + ((Object) mBeanName));
        }
        synchronized (this) {
            if (this.createdDuringQuery != null) {
                this.createdDuringQuery.add(mBeanName);
            } else if (isInstanceOf(this.mBeanServer, mBeanName, broadcasterClass)) {
                addBufferListener(mBeanName);
                if (isDisposed()) {
                    removeBufferListener(mBeanName);
                }
            }
        }
    }

    /* loaded from: rt.jar:com/sun/jmx/remote/internal/ArrayNotificationBuffer$BufferListener.class */
    private class BufferListener implements NotificationListener {
        private BufferListener() {
        }

        @Override // javax.management.NotificationListener
        public void handleNotification(Notification notification, Object obj) {
            if (ArrayNotificationBuffer.logger.debugOn()) {
                ArrayNotificationBuffer.logger.debug("BufferListener.handleNotification", "notif=" + ((Object) notification) + "; handback=" + obj);
            }
            ArrayNotificationBuffer.this.addNotification(new NamedNotification((ObjectName) obj, notification));
        }
    }

    /* loaded from: rt.jar:com/sun/jmx/remote/internal/ArrayNotificationBuffer$BroadcasterQuery.class */
    private static class BroadcasterQuery extends QueryEval implements QueryExp {
        private static final long serialVersionUID = 7378487660587592048L;

        private BroadcasterQuery() {
        }

        @Override // javax.management.QueryExp
        public boolean apply(ObjectName objectName) {
            return ArrayNotificationBuffer.isInstanceOf(QueryEval.getMBeanServer(), objectName, ArrayNotificationBuffer.broadcasterClass);
        }
    }

    private void destroyListeners() {
        checkNoLocks();
        logger.debug("destroyListeners", "starts");
        try {
            removeNotificationListener(MBeanServerDelegate.DELEGATE_NAME, this.creationListener);
        } catch (Exception e2) {
            logger.warning("remove listener from MBeanServer delegate", e2);
        }
        for (ObjectName objectName : queryNames(null, broadcasterQuery)) {
            if (logger.debugOn()) {
                logger.debug("destroyListeners", "remove listener from " + ((Object) objectName));
            }
            removeBufferListener(objectName);
        }
        logger.debug("destroyListeners", "ends");
    }

    private void checkNoLocks() {
        if (Thread.holdsLock(this) || Thread.holdsLock(globalLock)) {
            logger.warning("checkNoLocks", "lock protocol violation");
        }
    }

    private static Exception extractException(Exception exc) {
        while (exc instanceof PrivilegedActionException) {
            exc = ((PrivilegedActionException) exc).getException();
        }
        return exc;
    }
}
