package com.sun.jmx.remote.internal;

import com.sun.jmx.remote.util.ClassLogger;
import com.sun.jmx.remote.util.EnvHelp;
import com.sun.org.apache.xalan.internal.templates.Constants;
import java.io.IOException;
import java.io.NotSerializableException;
import java.rmi.UnmarshalException;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import javax.management.InstanceNotFoundException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanServerNotification;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.remote.NotificationResult;
import javax.management.remote.TargetedNotification;
import javax.security.auth.Subject;
import org.icepdf.core.util.PdfOps;

/*  JADX ERROR: NullPointerException in pass: ClassModifier
    java.lang.NullPointerException
    */
/* loaded from: rt.jar:com/sun/jmx/remote/internal/ClientNotifForwarder.class */
public abstract class ClientNotifForwarder {
    private final AccessControlContext acc;
    private static int threadId;
    private final ClassLoader defaultClassLoader;
    private final Executor executor;
    private final Map<Integer, ClientListenerInfo> infoList;
    private long clientSequenceNumber;
    private final int maxNotifications;
    private final long timeout;
    private Integer mbeanRemovedNotifID;
    private Thread currentFetchThread;
    private static final int STARTING = 0;
    private static final int STARTED = 1;
    private static final int STOPPING = 2;
    private static final int STOPPED = 3;
    private static final int TERMINATED = 4;
    private int state;
    private boolean beingReconnected;
    private static final ClassLogger logger = new ClassLogger("javax.management.remote.misc", "ClientNotifForwarder");

    protected abstract NotificationResult fetchNotifs(long j2, int i2, long j3) throws IOException, ClassNotFoundException;

    protected abstract Integer addListenerForMBeanRemovedNotif() throws IOException, InstanceNotFoundException;

    protected abstract void removeListenerForMBeanRemovedNotif(Integer num) throws ListenerNotFoundException, IOException, InstanceNotFoundException;

    protected abstract void lostNotifs(String str, long j2);

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ long access$1102(com.sun.jmx.remote.internal.ClientNotifForwarder r6, long r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.clientSequenceNumber = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.jmx.remote.internal.ClientNotifForwarder.access$1102(com.sun.jmx.remote.internal.ClientNotifForwarder, long):long");
    }

    static /* synthetic */ int access$204() {
        int i2 = threadId + 1;
        threadId = i2;
        return i2;
    }

    public ClientNotifForwarder(Map map) {
        this(null, map);
    }

    /* loaded from: rt.jar:com/sun/jmx/remote/internal/ClientNotifForwarder$LinearExecutor.class */
    private static class LinearExecutor implements Executor {
        private Runnable command;
        private Thread thread;

        private LinearExecutor() {
        }

        @Override // java.util.concurrent.Executor
        public synchronized void execute(Runnable runnable) {
            if (this.command != null) {
                throw new IllegalArgumentException("More than one command");
            }
            this.command = runnable;
            if (this.thread == null) {
                this.thread = new Thread() { // from class: com.sun.jmx.remote.internal.ClientNotifForwarder.LinearExecutor.1
                    @Override // java.lang.Thread, java.lang.Runnable
                    public void run() {
                        Runnable runnable2;
                        while (true) {
                            synchronized (LinearExecutor.this) {
                                if (LinearExecutor.this.command == null) {
                                    LinearExecutor.this.thread = null;
                                    return;
                                } else {
                                    runnable2 = LinearExecutor.this.command;
                                    LinearExecutor.this.command = null;
                                }
                            }
                            runnable2.run();
                        }
                    }
                };
                this.thread.setDaemon(true);
                this.thread.setName("ClientNotifForwarder-" + ClientNotifForwarder.access$204());
                this.thread.start();
            }
        }
    }

    public ClientNotifForwarder(ClassLoader classLoader, Map<String, ?> map) {
        this.infoList = new HashMap();
        this.clientSequenceNumber = -1L;
        this.mbeanRemovedNotifID = null;
        this.state = 3;
        this.beingReconnected = false;
        this.maxNotifications = EnvHelp.getMaxFetchNotifNumber(map);
        this.timeout = EnvHelp.getFetchTimeout(map);
        Executor linearExecutor = (Executor) map.get("jmx.remote.x.fetch.notifications.executor");
        if (linearExecutor == null) {
            linearExecutor = new LinearExecutor();
        } else if (logger.traceOn()) {
            logger.trace("ClientNotifForwarder", "executor is " + ((Object) linearExecutor));
        }
        this.defaultClassLoader = classLoader;
        this.executor = linearExecutor;
        this.acc = AccessController.getContext();
    }

    public synchronized void addNotificationListener(Integer num, ObjectName objectName, NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj, Subject subject) throws IOException, InstanceNotFoundException {
        if (logger.traceOn()) {
            logger.trace("addNotificationListener", "Add the listener " + ((Object) notificationListener) + " at " + ((Object) objectName));
        }
        this.infoList.put(num, new ClientListenerInfo(num, objectName, notificationListener, notificationFilter, obj, subject));
        init(false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public synchronized Integer[] removeNotificationListener(ObjectName objectName, NotificationListener notificationListener) throws ListenerNotFoundException, IOException {
        beforeRemove();
        if (logger.traceOn()) {
            logger.trace("removeNotificationListener", "Remove the listener " + ((Object) notificationListener) + " from " + ((Object) objectName));
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList(this.infoList.values());
        for (int size = arrayList2.size() - 1; size >= 0; size--) {
            ClientListenerInfo clientListenerInfo = (ClientListenerInfo) arrayList2.get(size);
            if (clientListenerInfo.sameAs(objectName, notificationListener)) {
                arrayList.add(clientListenerInfo.getListenerID());
                this.infoList.remove(clientListenerInfo.getListenerID());
            }
        }
        if (arrayList.isEmpty()) {
            throw new ListenerNotFoundException("Listener not found");
        }
        return (Integer[]) arrayList.toArray(new Integer[0]);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public synchronized Integer removeNotificationListener(ObjectName objectName, NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws ListenerNotFoundException, IOException {
        if (logger.traceOn()) {
            logger.trace("removeNotificationListener", "Remove the listener " + ((Object) notificationListener) + " from " + ((Object) objectName));
        }
        beforeRemove();
        Integer listenerID = null;
        ArrayList arrayList = new ArrayList(this.infoList.values());
        int size = arrayList.size() - 1;
        while (true) {
            if (size < 0) {
                break;
            }
            ClientListenerInfo clientListenerInfo = (ClientListenerInfo) arrayList.get(size);
            if (!clientListenerInfo.sameAs(objectName, notificationListener, notificationFilter, obj)) {
                size--;
            } else {
                listenerID = clientListenerInfo.getListenerID();
                this.infoList.remove(listenerID);
                break;
            }
        }
        if (listenerID == null) {
            throw new ListenerNotFoundException("Listener not found");
        }
        return listenerID;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public synchronized Integer[] removeNotificationListener(ObjectName objectName) {
        if (logger.traceOn()) {
            logger.trace("removeNotificationListener", "Remove all listeners registered at " + ((Object) objectName));
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList(this.infoList.values());
        for (int size = arrayList2.size() - 1; size >= 0; size--) {
            ClientListenerInfo clientListenerInfo = (ClientListenerInfo) arrayList2.get(size);
            if (clientListenerInfo.sameAs(objectName)) {
                arrayList.add(clientListenerInfo.getListenerID());
                this.infoList.remove(clientListenerInfo.getListenerID());
            }
        }
        return (Integer[]) arrayList.toArray(new Integer[0]);
    }

    public synchronized ClientListenerInfo[] preReconnection() throws IOException {
        if (this.state == 4 || this.beingReconnected) {
            throw new IOException("Illegal state.");
        }
        ClientListenerInfo[] clientListenerInfoArr = (ClientListenerInfo[]) this.infoList.values().toArray(new ClientListenerInfo[0]);
        this.beingReconnected = true;
        this.infoList.clear();
        return clientListenerInfoArr;
    }

    public synchronized void postReconnection(ClientListenerInfo[] clientListenerInfoArr) throws IOException {
        if (this.state == 4) {
            return;
        }
        while (this.state == 2) {
            try {
                wait();
            } catch (InterruptedException e2) {
                IOException iOException = new IOException(e2.toString());
                EnvHelp.initCause(iOException, e2);
                throw iOException;
            }
        }
        boolean zTraceOn = logger.traceOn();
        int length = clientListenerInfoArr.length;
        for (int i2 = 0; i2 < length; i2++) {
            if (zTraceOn) {
                logger.trace("addNotificationListeners", "Add a listener at " + ((Object) clientListenerInfoArr[i2].getListenerID()));
            }
            this.infoList.put(clientListenerInfoArr[i2].getListenerID(), clientListenerInfoArr[i2]);
        }
        this.beingReconnected = false;
        notifyAll();
        if (this.currentFetchThread == Thread.currentThread() || this.state == 0 || this.state == 1) {
            try {
                this.mbeanRemovedNotifID = addListenerForMBeanRemovedNotif();
                return;
            } catch (Exception e3) {
                if (logger.traceOn()) {
                    logger.trace("init", "Failed to register a listener to the mbean server: the client will not do clean when an MBean is unregistered", e3);
                    return;
                }
                return;
            }
        }
        while (this.state == 2) {
            try {
                wait();
            } catch (InterruptedException e4) {
                IOException iOException2 = new IOException(e4.toString());
                EnvHelp.initCause(iOException2, e4);
                throw iOException2;
            }
        }
        if (clientListenerInfoArr.length > 0) {
            init(true);
        } else if (this.infoList.size() > 0) {
            init(false);
        }
    }

    public synchronized void terminate() {
        if (this.state == 4) {
            return;
        }
        if (logger.traceOn()) {
            logger.trace(Constants.ATTRNAME_TERMINATE, "Terminating...");
        }
        if (this.state == 1) {
            this.infoList.clear();
        }
        setState(4);
    }

    /* loaded from: rt.jar:com/sun/jmx/remote/internal/ClientNotifForwarder$NotifFetcher.class */
    private class NotifFetcher implements Runnable {
        private volatile boolean alreadyLogged;

        private NotifFetcher() {
            this.alreadyLogged = false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void logOnce(String str, SecurityException securityException) {
            if (this.alreadyLogged) {
                return;
            }
            ClientNotifForwarder.logger.config("setContextClassLoader", str);
            if (securityException != null) {
                ClientNotifForwarder.logger.fine("setContextClassLoader", securityException);
            }
            this.alreadyLogged = true;
        }

        private final ClassLoader setContextClassLoader(final ClassLoader classLoader) {
            AccessControlContext accessControlContext = ClientNotifForwarder.this.acc;
            if (accessControlContext == null) {
                logOnce("AccessControlContext must not be null.", null);
                throw new SecurityException("AccessControlContext must not be null");
            }
            return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() { // from class: com.sun.jmx.remote.internal.ClientNotifForwarder.NotifFetcher.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public ClassLoader run2() {
                    try {
                        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                        if (classLoader == contextClassLoader) {
                            return contextClassLoader;
                        }
                        Thread.currentThread().setContextClassLoader(classLoader);
                        return contextClassLoader;
                    } catch (SecurityException e2) {
                        NotifFetcher.this.logOnce("Permission to set ContextClassLoader missing. Notifications will not be dispatched. Please check your Java policy configuration: " + ((Object) e2), e2);
                        throw e2;
                    }
                }
            }, accessControlContext);
        }

        @Override // java.lang.Runnable
        public void run() {
            ClassLoader contextClassLoader;
            if (ClientNotifForwarder.this.defaultClassLoader != null) {
                contextClassLoader = setContextClassLoader(ClientNotifForwarder.this.defaultClassLoader);
            } else {
                contextClassLoader = null;
            }
            try {
                doRun();
            } finally {
                if (ClientNotifForwarder.this.defaultClassLoader != null) {
                    setContextClassLoader(contextClassLoader);
                }
            }
        }

        /* JADX WARN: Failed to check method for inline after forced processcom.sun.jmx.remote.internal.ClientNotifForwarder.access$1102(com.sun.jmx.remote.internal.ClientNotifForwarder, long):long */
        private void doRun() {
            HashMap map;
            Integer num;
            synchronized (ClientNotifForwarder.this) {
                ClientNotifForwarder.this.currentFetchThread = Thread.currentThread();
                if (ClientNotifForwarder.this.state == 0) {
                    ClientNotifForwarder.this.setState(1);
                }
            }
            NotificationResult notificationResult = null;
            if (!shouldStop()) {
                NotificationResult notificationResultFetchNotifs = fetchNotifs();
                notificationResult = notificationResultFetchNotifs;
                if (notificationResultFetchNotifs != null) {
                    TargetedNotification[] targetedNotifications = notificationResult.getTargetedNotifications();
                    long earliestSequenceNumber = 0;
                    synchronized (ClientNotifForwarder.this) {
                        if (ClientNotifForwarder.this.clientSequenceNumber >= 0) {
                            earliestSequenceNumber = notificationResult.getEarliestSequenceNumber() - ClientNotifForwarder.this.clientSequenceNumber;
                        }
                        ClientNotifForwarder.access$1102(ClientNotifForwarder.this, notificationResult.getNextSequenceNumber());
                        map = new HashMap();
                        for (TargetedNotification targetedNotification : targetedNotifications) {
                            Integer listenerID = targetedNotification.getListenerID();
                            if (!listenerID.equals(ClientNotifForwarder.this.mbeanRemovedNotifID)) {
                                ClientListenerInfo clientListenerInfo = (ClientListenerInfo) ClientNotifForwarder.this.infoList.get(listenerID);
                                if (clientListenerInfo != null) {
                                    map.put(listenerID, clientListenerInfo);
                                }
                            } else {
                                Notification notification = targetedNotification.getNotification();
                                if ((notification instanceof MBeanServerNotification) && notification.getType().equals(MBeanServerNotification.UNREGISTRATION_NOTIFICATION)) {
                                    ClientNotifForwarder.this.removeNotificationListener(((MBeanServerNotification) notification).getMBeanName());
                                }
                            }
                        }
                        num = ClientNotifForwarder.this.mbeanRemovedNotifID;
                    }
                    if (earliestSequenceNumber > 0) {
                        String str = "May have lost up to " + earliestSequenceNumber + " notification" + (earliestSequenceNumber == 1 ? "" : PdfOps.s_TOKEN);
                        ClientNotifForwarder.this.lostNotifs(str, earliestSequenceNumber);
                        ClientNotifForwarder.logger.trace("NotifFetcher.run", str);
                    }
                    for (TargetedNotification targetedNotification2 : targetedNotifications) {
                        dispatchNotification(targetedNotification2, num, map);
                    }
                }
            }
            synchronized (ClientNotifForwarder.this) {
                ClientNotifForwarder.this.currentFetchThread = null;
            }
            if (notificationResult == null && ClientNotifForwarder.logger.traceOn()) {
                ClientNotifForwarder.logger.trace("NotifFetcher-run", "Recieved null object as notifs, stops fetching because the notification server is terminated.");
            }
            if (notificationResult == null || shouldStop()) {
                ClientNotifForwarder.this.setState(3);
                try {
                    ClientNotifForwarder.this.removeListenerForMBeanRemovedNotif(ClientNotifForwarder.this.mbeanRemovedNotifID);
                    return;
                } catch (Exception e2) {
                    if (ClientNotifForwarder.logger.traceOn()) {
                        ClientNotifForwarder.logger.trace("NotifFetcher-run", "removeListenerForMBeanRemovedNotif", e2);
                        return;
                    }
                    return;
                }
            }
            ClientNotifForwarder.this.executor.execute(this);
        }

        void dispatchNotification(TargetedNotification targetedNotification, Integer num, Map<Integer, ClientListenerInfo> map) {
            Notification notification = targetedNotification.getNotification();
            Integer listenerID = targetedNotification.getListenerID();
            if (listenerID.equals(num)) {
                return;
            }
            ClientListenerInfo clientListenerInfo = map.get(listenerID);
            if (clientListenerInfo == null) {
                ClientNotifForwarder.logger.trace("NotifFetcher.dispatch", "Listener ID not in map");
                return;
            }
            try {
                clientListenerInfo.getListener().handleNotification(notification, clientListenerInfo.getHandback());
            } catch (RuntimeException e2) {
                ClientNotifForwarder.logger.trace("NotifFetcher-run", "Failed to forward a notification to a listener", e2);
            }
        }

        private NotificationResult fetchNotifs() {
            try {
                NotificationResult notificationResultFetchNotifs = ClientNotifForwarder.this.fetchNotifs(ClientNotifForwarder.this.clientSequenceNumber, ClientNotifForwarder.this.maxNotifications, ClientNotifForwarder.this.timeout);
                if (ClientNotifForwarder.logger.traceOn()) {
                    ClientNotifForwarder.logger.trace("NotifFetcher-run", "Got notifications from the server: " + ((Object) notificationResultFetchNotifs));
                }
                return notificationResultFetchNotifs;
            } catch (NotSerializableException | ClassNotFoundException | UnmarshalException e2) {
                ClientNotifForwarder.logger.trace("NotifFetcher.fetchNotifs", e2);
                return fetchOneNotif();
            } catch (IOException e3) {
                if (!shouldStop()) {
                    ClientNotifForwarder.logger.error("NotifFetcher-run", "Failed to fetch notification, stopping thread. Error is: " + ((Object) e3), e3);
                    ClientNotifForwarder.logger.debug("NotifFetcher-run", e3);
                    return null;
                }
                return null;
            }
        }

        private NotificationResult fetchOneNotif() {
            ClientNotifForwarder clientNotifForwarder = ClientNotifForwarder.this;
            long nextSequenceNumber = ClientNotifForwarder.this.clientSequenceNumber;
            int i2 = 0;
            NotificationResult notificationResult = null;
            long earliestSequenceNumber = -1;
            while (notificationResult == null && !shouldStop()) {
                try {
                    NotificationResult notificationResultFetchNotifs = clientNotifForwarder.fetchNotifs(nextSequenceNumber, 0, 0L);
                    if (shouldStop() || notificationResultFetchNotifs == null) {
                        return null;
                    }
                    nextSequenceNumber = notificationResultFetchNotifs.getNextSequenceNumber();
                    if (earliestSequenceNumber < 0) {
                        earliestSequenceNumber = notificationResultFetchNotifs.getEarliestSequenceNumber();
                    }
                    try {
                        notificationResult = clientNotifForwarder.fetchNotifs(nextSequenceNumber, 1, 0L);
                    } catch (NotSerializableException | ClassNotFoundException | UnmarshalException e2) {
                        ClientNotifForwarder.logger.warning("NotifFetcher.fetchOneNotif", "Failed to deserialize a notification: " + e2.toString());
                        if (ClientNotifForwarder.logger.traceOn()) {
                            ClientNotifForwarder.logger.trace("NotifFetcher.fetchOneNotif", "Failed to deserialize a notification.", e2);
                        }
                        i2++;
                        nextSequenceNumber++;
                    } catch (Exception e3) {
                        if (!shouldStop()) {
                            ClientNotifForwarder.logger.trace("NotifFetcher.fetchOneNotif", e3);
                            return null;
                        }
                        return null;
                    }
                } catch (IOException e4) {
                    if (!shouldStop()) {
                        ClientNotifForwarder.logger.trace("NotifFetcher.fetchOneNotif", e4);
                        return null;
                    }
                    return null;
                } catch (ClassNotFoundException e5) {
                    ClientNotifForwarder.logger.warning("NotifFetcher.fetchOneNotif", "Impossible exception: " + ((Object) e5));
                    ClientNotifForwarder.logger.debug("NotifFetcher.fetchOneNotif", e5);
                    return null;
                }
            }
            if (i2 > 0) {
                ClientNotifForwarder.this.lostNotifs("Dropped " + i2 + " notification" + (i2 == 1 ? "" : PdfOps.s_TOKEN) + " because classes were missing locally or incompatible", i2);
                if (notificationResult != null) {
                    notificationResult = new NotificationResult(earliestSequenceNumber, notificationResult.getNextSequenceNumber(), notificationResult.getTargetedNotifications());
                }
            }
            return notificationResult;
        }

        private boolean shouldStop() {
            synchronized (ClientNotifForwarder.this) {
                if (ClientNotifForwarder.this.state == 1) {
                    if (ClientNotifForwarder.this.infoList.size() == 0) {
                        ClientNotifForwarder.this.setState(2);
                        return true;
                    }
                    return false;
                }
                return true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setState(int i2) {
        if (this.state == 4) {
            return;
        }
        this.state = i2;
        notifyAll();
    }

    private synchronized void init(boolean z2) throws IOException {
        switch (this.state) {
            case 0:
                return;
            case 1:
                return;
            case 2:
                if (this.beingReconnected) {
                    return;
                }
                while (this.state == 2) {
                    try {
                        wait();
                    } catch (InterruptedException e2) {
                        IOException iOException = new IOException(e2.toString());
                        EnvHelp.initCause(iOException, e2);
                        throw iOException;
                    }
                }
                init(z2);
                return;
            case 3:
                if (this.beingReconnected) {
                    return;
                }
                if (logger.traceOn()) {
                    logger.trace("init", "Initializing...");
                }
                if (!z2) {
                    try {
                        NotificationResult notificationResultFetchNotifs = fetchNotifs(-1L, 0, 0L);
                        if (this.state != 3) {
                            return;
                        } else {
                            this.clientSequenceNumber = notificationResultFetchNotifs.getNextSequenceNumber();
                        }
                    } catch (ClassNotFoundException e3) {
                        logger.warning("init", "Impossible exception: " + ((Object) e3));
                        logger.debug("init", e3);
                    }
                }
                try {
                    this.mbeanRemovedNotifID = addListenerForMBeanRemovedNotif();
                } catch (Exception e4) {
                    if (logger.traceOn()) {
                        logger.trace("init", "Failed to register a listener to the mbean server: the client will not do clean when an MBean is unregistered", e4);
                    }
                }
                setState(0);
                this.executor.execute(new NotifFetcher());
                return;
            case 4:
                throw new IOException("The ClientNotifForwarder has been terminated.");
            default:
                throw new IOException("Unknown state.");
        }
    }

    private synchronized void beforeRemove() throws IOException {
        while (this.beingReconnected) {
            if (this.state == 4) {
                throw new IOException("Terminated.");
            }
            try {
                wait();
            } catch (InterruptedException e2) {
                IOException iOException = new IOException(e2.toString());
                EnvHelp.initCause(iOException, e2);
                throw iOException;
            }
        }
        if (this.state == 4) {
            throw new IOException("Terminated.");
        }
    }

    static {
    }
}
