package com.sun.org.glassfish.external.amx;

import com.sun.org.glassfish.external.amx.MBeanListener.Callback;
import com.sun.org.glassfish.external.arc.Stability;
import com.sun.org.glassfish.external.arc.Taxonomy;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerNotification;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import jdk.internal.dynalink.CallSiteDescriptor;

@Taxonomy(stability = Stability.UNCOMMITTED)
/* loaded from: rt.jar:com/sun/org/glassfish/external/amx/MBeanListener.class */
public class MBeanListener<T extends Callback> implements NotificationListener {
    private final String mJMXDomain;
    private final String mType;
    private final String mName;
    private final ObjectName mObjectName;
    private final MBeanServerConnection mMBeanServer;
    private final T mCallback;

    /* loaded from: rt.jar:com/sun/org/glassfish/external/amx/MBeanListener$Callback.class */
    public interface Callback {
        void mbeanRegistered(ObjectName objectName, MBeanListener mBeanListener);

        void mbeanUnregistered(ObjectName objectName, MBeanListener mBeanListener);
    }

    private static void debug(Object o2) {
        System.out.println("" + o2);
    }

    public String toString() {
        return "MBeanListener: ObjectName=" + ((Object) this.mObjectName) + ", type=" + this.mType + ", name=" + this.mName;
    }

    public String getType() {
        return this.mType;
    }

    public String getName() {
        return this.mName;
    }

    public MBeanServerConnection getMBeanServer() {
        return this.mMBeanServer;
    }

    /* loaded from: rt.jar:com/sun/org/glassfish/external/amx/MBeanListener$CallbackImpl.class */
    public static class CallbackImpl implements Callback {
        private volatile ObjectName mRegistered;
        private volatile ObjectName mUnregistered;
        private final boolean mStopAtFirst;
        protected final CountDownLatch mLatch;

        public CallbackImpl() {
            this(true);
        }

        public CallbackImpl(boolean stopAtFirst) {
            this.mRegistered = null;
            this.mUnregistered = null;
            this.mLatch = new CountDownLatch(1);
            this.mStopAtFirst = stopAtFirst;
        }

        public ObjectName getRegistered() {
            return this.mRegistered;
        }

        public ObjectName getUnregistered() {
            return this.mUnregistered;
        }

        public void await() {
            try {
                this.mLatch.await();
            } catch (InterruptedException e2) {
                throw new RuntimeException(e2);
            }
        }

        @Override // com.sun.org.glassfish.external.amx.MBeanListener.Callback
        public void mbeanRegistered(ObjectName objectName, MBeanListener listener) {
            this.mRegistered = objectName;
            if (this.mStopAtFirst) {
                listener.stopListening();
            }
        }

        @Override // com.sun.org.glassfish.external.amx.MBeanListener.Callback
        public void mbeanUnregistered(ObjectName objectName, MBeanListener listener) {
            this.mUnregistered = objectName;
            if (this.mStopAtFirst) {
                listener.stopListening();
            }
        }
    }

    public T getCallback() {
        return this.mCallback;
    }

    public MBeanListener(MBeanServerConnection server, ObjectName objectName, T callback) {
        this.mMBeanServer = server;
        this.mObjectName = objectName;
        this.mJMXDomain = null;
        this.mType = null;
        this.mName = null;
        this.mCallback = callback;
    }

    public MBeanListener(MBeanServerConnection server, String domain, String type, T callback) {
        this(server, domain, type, null, callback);
    }

    public MBeanListener(MBeanServerConnection server, String domain, String type, String name, T callback) {
        this.mMBeanServer = server;
        this.mJMXDomain = domain;
        this.mType = type;
        this.mName = name;
        this.mObjectName = null;
        this.mCallback = callback;
    }

    private boolean isRegistered(MBeanServerConnection conn, ObjectName objectName) {
        try {
            return conn.isRegistered(objectName);
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }

    public void startListening() {
        try {
            this.mMBeanServer.addNotificationListener(AMXUtil.getMBeanServerDelegateObjectName(), this, (NotificationFilter) null, this);
            if (this.mObjectName != null) {
                if (isRegistered(this.mMBeanServer, this.mObjectName)) {
                    this.mCallback.mbeanRegistered(this.mObjectName, this);
                    return;
                }
                return;
            }
            String props = "type=" + this.mType;
            if (this.mName != null) {
                props = props + ",name" + this.mName;
            }
            ObjectName pattern = AMXUtil.newObjectName(this.mJMXDomain + CallSiteDescriptor.TOKEN_DELIMITER + props);
            try {
                Set<ObjectName> matched = this.mMBeanServer.queryNames(pattern, null);
                for (ObjectName objectName : matched) {
                    this.mCallback.mbeanRegistered(objectName, this);
                }
            } catch (Exception e2) {
                throw new RuntimeException(e2);
            }
        } catch (Exception e3) {
            throw new RuntimeException("Can't add NotificationListener", e3);
        }
    }

    public void stopListening() {
        try {
            this.mMBeanServer.removeNotificationListener(AMXUtil.getMBeanServerDelegateObjectName(), this);
        } catch (Exception e2) {
            throw new RuntimeException("Can't remove NotificationListener " + ((Object) this), e2);
        }
    }

    @Override // javax.management.NotificationListener
    public void handleNotification(Notification notifIn, Object handback) {
        if (notifIn instanceof MBeanServerNotification) {
            MBeanServerNotification notif = (MBeanServerNotification) notifIn;
            ObjectName objectName = notif.getMBeanName();
            boolean match = false;
            if (this.mObjectName != null && this.mObjectName.equals(objectName)) {
                match = true;
            } else if (objectName.getDomain().equals(this.mJMXDomain) && this.mType != null && this.mType.equals(objectName.getKeyProperty("type"))) {
                String mbeanName = objectName.getKeyProperty("name");
                if (this.mName != null && this.mName.equals(mbeanName)) {
                    match = true;
                }
            }
            if (match) {
                String notifType = notif.getType();
                if (MBeanServerNotification.REGISTRATION_NOTIFICATION.equals(notifType)) {
                    this.mCallback.mbeanRegistered(objectName, this);
                } else if (MBeanServerNotification.UNREGISTRATION_NOTIFICATION.equals(notifType)) {
                    this.mCallback.mbeanUnregistered(objectName, this);
                }
            }
        }
    }
}
