package com.sun.jmx.remote.internal;

import com.sun.jmx.remote.security.NotificationAccessController;
import com.sun.jmx.remote.util.ClassLogger;
import com.sun.jmx.remote.util.EnvHelp;
import com.sun.org.apache.xalan.internal.templates.Constants;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.management.InstanceNotFoundException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanPermission;
import javax.management.MBeanServer;
import javax.management.MBeanServerDelegate;
import javax.management.MBeanServerNotification;
import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.NotificationBroadcaster;
import javax.management.NotificationFilter;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.remote.NotificationResult;
import javax.management.remote.TargetedNotification;
import javax.security.auth.Subject;

/* loaded from: rt.jar:com/sun/jmx/remote/internal/ServerNotifForwarder.class */
public class ServerNotifForwarder {
    private MBeanServer mbeanServer;
    private final String connectionId;
    private final long connectionTimeout;
    private NotificationBuffer notifBuffer;
    private final boolean checkNotificationEmission;
    private final NotificationAccessController notificationAccessController;
    private static int listenerCounter = 0;
    private static final int[] listenerCounterLock = new int[0];
    static final String broadcasterClass = NotificationBroadcaster.class.getName();
    private static final ClassLogger logger = new ClassLogger("javax.management.remote.misc", "ServerNotifForwarder");
    private final NotifForwarderBufferFilter bufferFilter = new NotifForwarderBufferFilter();
    private final Map<ObjectName, Set<IdAndFilter>> listenerMap = new HashMap();
    private boolean terminated = false;
    private final int[] terminationLock = new int[0];

    public ServerNotifForwarder(MBeanServer mBeanServer, Map<String, ?> map, NotificationBuffer notificationBuffer, String str) {
        this.mbeanServer = mBeanServer;
        this.notifBuffer = notificationBuffer;
        this.connectionId = str;
        this.connectionTimeout = EnvHelp.getServerConnectionTimeout(map);
        this.checkNotificationEmission = EnvHelp.computeBooleanFromString((String) map.get("jmx.remote.x.check.notification.emission"));
        this.notificationAccessController = EnvHelp.getNotificationAccessController(map);
    }

    public Integer addNotificationListener(final ObjectName objectName, NotificationFilter notificationFilter) throws IOException, SecurityException, InstanceNotFoundException {
        if (logger.traceOn()) {
            logger.trace("addNotificationListener", "Add a listener at " + ((Object) objectName));
        }
        checkState();
        checkMBeanPermission(objectName, "addNotificationListener");
        if (this.notificationAccessController != null) {
            this.notificationAccessController.addNotificationListener(this.connectionId, objectName, getSubject());
        }
        try {
            if (!((Boolean) AccessController.doPrivileged(new PrivilegedExceptionAction<Boolean>() { // from class: com.sun.jmx.remote.internal.ServerNotifForwarder.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public Boolean run() throws InstanceNotFoundException {
                    return Boolean.valueOf(ServerNotifForwarder.this.mbeanServer.isInstanceOf(objectName, ServerNotifForwarder.broadcasterClass));
                }
            })).booleanValue()) {
                throw new IllegalArgumentException("The specified MBean [" + ((Object) objectName) + "] is not a NotificationBroadcaster object.");
            }
            Integer listenerID = getListenerID();
            ObjectName objectName2 = objectName;
            if (objectName.getDomain() == null || objectName.getDomain().equals("")) {
                try {
                    objectName2 = ObjectName.getInstance(this.mbeanServer.getDefaultDomain(), objectName.getKeyPropertyList());
                } catch (MalformedObjectNameException e2) {
                    IOException iOException = new IOException(e2.getMessage());
                    iOException.initCause(e2);
                    throw iOException;
                }
            }
            synchronized (this.listenerMap) {
                IdAndFilter idAndFilter = new IdAndFilter(listenerID, notificationFilter);
                Set<IdAndFilter> hashSet = this.listenerMap.get(objectName2);
                if (hashSet == null) {
                    hashSet = Collections.singleton(idAndFilter);
                } else {
                    if (hashSet.size() == 1) {
                        hashSet = new HashSet(hashSet);
                    }
                    hashSet.add(idAndFilter);
                }
                this.listenerMap.put(objectName2, hashSet);
            }
            return listenerID;
        } catch (PrivilegedActionException e3) {
            throw ((InstanceNotFoundException) extractException(e3));
        }
    }

    public void removeNotificationListener(ObjectName objectName, Integer[] numArr) throws Exception {
        if (logger.traceOn()) {
            logger.trace("removeNotificationListener", "Remove some listeners from " + ((Object) objectName));
        }
        checkState();
        checkMBeanPermission(objectName, "removeNotificationListener");
        if (this.notificationAccessController != null) {
            this.notificationAccessController.removeNotificationListener(this.connectionId, objectName, getSubject());
        }
        Exception exc = null;
        for (Integer num : numArr) {
            try {
                removeNotificationListener(objectName, num);
            } catch (Exception e2) {
                if (exc != null) {
                    exc = e2;
                }
            }
        }
        if (exc != null) {
            throw exc;
        }
    }

    public void removeNotificationListener(ObjectName objectName, Integer num) throws ListenerNotFoundException, IOException, InstanceNotFoundException {
        if (logger.traceOn()) {
            logger.trace("removeNotificationListener", "Remove the listener " + ((Object) num) + " from " + ((Object) objectName));
        }
        checkState();
        if (objectName != null && !objectName.isPattern() && !this.mbeanServer.isRegistered(objectName)) {
            throw new InstanceNotFoundException("The MBean " + ((Object) objectName) + " is not registered.");
        }
        synchronized (this.listenerMap) {
            Set<IdAndFilter> set = this.listenerMap.get(objectName);
            IdAndFilter idAndFilter = new IdAndFilter(num, null);
            if (set == null || !set.contains(idAndFilter)) {
                throw new ListenerNotFoundException("Listener not found");
            }
            if (set.size() == 1) {
                this.listenerMap.remove(objectName);
            } else {
                set.remove(idAndFilter);
            }
        }
    }

    /* loaded from: rt.jar:com/sun/jmx/remote/internal/ServerNotifForwarder$NotifForwarderBufferFilter.class */
    final class NotifForwarderBufferFilter implements NotificationBufferFilter {
        NotifForwarderBufferFilter() {
        }

        @Override // com.sun.jmx.remote.internal.NotificationBufferFilter
        public void apply(List<TargetedNotification> list, ObjectName objectName, Notification notification) {
            synchronized (ServerNotifForwarder.this.listenerMap) {
                Set set = (Set) ServerNotifForwarder.this.listenerMap.get(objectName);
                if (set == null) {
                    ServerNotifForwarder.logger.debug("bufferFilter", "no listeners for this name");
                    return;
                }
                IdAndFilter[] idAndFilterArr = new IdAndFilter[set.size()];
                set.toArray(idAndFilterArr);
                for (IdAndFilter idAndFilter : idAndFilterArr) {
                    NotificationFilter filter = idAndFilter.getFilter();
                    if (filter == null || filter.isNotificationEnabled(notification)) {
                        ServerNotifForwarder.logger.debug("bufferFilter", "filter matches");
                        TargetedNotification targetedNotification = new TargetedNotification(notification, idAndFilter.getId());
                        if (ServerNotifForwarder.this.allowNotificationEmission(objectName, targetedNotification)) {
                            list.add(targetedNotification);
                        }
                    }
                }
            }
        }
    }

    public NotificationResult fetchNotifs(long j2, long j3, int i2) {
        NotificationResult notificationResult;
        if (logger.traceOn()) {
            logger.trace("fetchNotifs", "Fetching notifications, the startSequenceNumber is " + j2 + ", the timeout is " + j3 + ", the maxNotifications is " + i2);
        }
        try {
            notificationResult = this.notifBuffer.fetchNotifications(this.bufferFilter, j2, Math.min(this.connectionTimeout, j3), i2);
            snoopOnUnregister(notificationResult);
        } catch (InterruptedException e2) {
            notificationResult = new NotificationResult(0L, 0L, new TargetedNotification[0]);
        }
        if (logger.traceOn()) {
            logger.trace("fetchNotifs", "Forwarding the notifs: " + ((Object) notificationResult));
        }
        return notificationResult;
    }

    private void snoopOnUnregister(NotificationResult notificationResult) {
        synchronized (this.listenerMap) {
            Set<IdAndFilter> set = this.listenerMap.get(MBeanServerDelegate.DELEGATE_NAME);
            if (set == null || set.isEmpty()) {
                return;
            }
            ArrayList arrayList = new ArrayList(set);
            for (TargetedNotification targetedNotification : notificationResult.getTargetedNotifications()) {
                Integer listenerID = targetedNotification.getListenerID();
                Iterator<E> it = arrayList.iterator();
                while (it.hasNext()) {
                    if (((IdAndFilter) it.next()).id == listenerID) {
                        Notification notification = targetedNotification.getNotification();
                        if ((notification instanceof MBeanServerNotification) && notification.getType().equals(MBeanServerNotification.UNREGISTRATION_NOTIFICATION)) {
                            ObjectName mBeanName = ((MBeanServerNotification) notification).getMBeanName();
                            synchronized (this.listenerMap) {
                                this.listenerMap.remove(mBeanName);
                            }
                        }
                    }
                }
            }
        }
    }

    public void terminate() {
        if (logger.traceOn()) {
            logger.trace(Constants.ATTRNAME_TERMINATE, "Be called.");
        }
        synchronized (this.terminationLock) {
            if (this.terminated) {
                return;
            }
            this.terminated = true;
            synchronized (this.listenerMap) {
                this.listenerMap.clear();
            }
            if (logger.traceOn()) {
                logger.trace(Constants.ATTRNAME_TERMINATE, "Terminated.");
            }
        }
    }

    private Subject getSubject() {
        return Subject.getSubject(AccessController.getContext());
    }

    private void checkState() throws IOException {
        synchronized (this.terminationLock) {
            if (this.terminated) {
                throw new IOException("The connection has been terminated.");
            }
        }
    }

    private Integer getListenerID() {
        Integer numValueOf;
        synchronized (listenerCounterLock) {
            int i2 = listenerCounter;
            listenerCounter = i2 + 1;
            numValueOf = Integer.valueOf(i2);
        }
        return numValueOf;
    }

    public final void checkMBeanPermission(ObjectName objectName, String str) throws SecurityException, InstanceNotFoundException {
        checkMBeanPermission(this.mbeanServer, objectName, str);
    }

    static void checkMBeanPermission(final MBeanServer mBeanServer, final ObjectName objectName, String str) throws SecurityException, InstanceNotFoundException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            try {
                securityManager.checkPermission(new MBeanPermission(((ObjectInstance) AccessController.doPrivileged(new PrivilegedExceptionAction<ObjectInstance>() { // from class: com.sun.jmx.remote.internal.ServerNotifForwarder.2
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public ObjectInstance run() throws InstanceNotFoundException {
                        return mBeanServer.getObjectInstance(objectName);
                    }
                })).getClassName(), null, objectName, str), AccessController.getContext());
            } catch (PrivilegedActionException e2) {
                throw ((InstanceNotFoundException) extractException(e2));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean allowNotificationEmission(ObjectName objectName, TargetedNotification targetedNotification) {
        try {
            if (this.checkNotificationEmission) {
                checkMBeanPermission(objectName, "addNotificationListener");
            }
            if (this.notificationAccessController != null) {
                this.notificationAccessController.fetchNotification(this.connectionId, objectName, targetedNotification.getNotification(), getSubject());
                return true;
            }
            return true;
        } catch (SecurityException e2) {
            if (logger.debugOn()) {
                logger.debug("fetchNotifs", "Notification " + ((Object) targetedNotification.getNotification()) + " not forwarded: the caller didn't have the required access rights");
                return false;
            }
            return false;
        } catch (Exception e3) {
            if (logger.debugOn()) {
                logger.debug("fetchNotifs", "Notification " + ((Object) targetedNotification.getNotification()) + " not forwarded: got an unexpected exception: " + ((Object) e3));
                return false;
            }
            return false;
        }
    }

    private static Exception extractException(Exception exc) {
        while (exc instanceof PrivilegedActionException) {
            exc = ((PrivilegedActionException) exc).getException();
        }
        return exc;
    }

    /* loaded from: rt.jar:com/sun/jmx/remote/internal/ServerNotifForwarder$IdAndFilter.class */
    private static class IdAndFilter {
        private Integer id;
        private NotificationFilter filter;

        IdAndFilter(Integer num, NotificationFilter notificationFilter) {
            this.id = num;
            this.filter = notificationFilter;
        }

        Integer getId() {
            return this.id;
        }

        NotificationFilter getFilter() {
            return this.filter;
        }

        public int hashCode() {
            return this.id.hashCode();
        }

        public boolean equals(Object obj) {
            return (obj instanceof IdAndFilter) && ((IdAndFilter) obj).getId().equals(getId());
        }
    }
}
