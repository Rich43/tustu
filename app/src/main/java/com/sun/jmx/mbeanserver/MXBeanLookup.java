package com.sun.jmx.mbeanserver;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.security.AccessController;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import javax.management.InstanceAlreadyExistsException;
import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.management.openmbean.OpenDataException;

/* loaded from: rt.jar:com/sun/jmx/mbeanserver/MXBeanLookup.class */
public class MXBeanLookup {
    private final MBeanServerConnection mbsc;
    private final WeakIdentityHashMap<Object, ObjectName> mxbeanToObjectName = WeakIdentityHashMap.make();
    private final Map<ObjectName, WeakReference<Object>> objectNameToProxy = Util.newMap();
    private static final ThreadLocal<MXBeanLookup> currentLookup = new ThreadLocal<>();
    private static final WeakIdentityHashMap<MBeanServerConnection, WeakReference<MXBeanLookup>> mbscToLookup = WeakIdentityHashMap.make();

    private MXBeanLookup(MBeanServerConnection mBeanServerConnection) {
        this.mbsc = mBeanServerConnection;
    }

    static MXBeanLookup lookupFor(MBeanServerConnection mBeanServerConnection) {
        MXBeanLookup mXBeanLookup;
        synchronized (mbscToLookup) {
            WeakReference<MXBeanLookup> weakReference = mbscToLookup.get(mBeanServerConnection);
            MXBeanLookup mXBeanLookup2 = weakReference == null ? null : weakReference.get();
            if (mXBeanLookup2 == null) {
                mXBeanLookup2 = new MXBeanLookup(mBeanServerConnection);
                mbscToLookup.put(mBeanServerConnection, new WeakReference<>(mXBeanLookup2));
            }
            mXBeanLookup = mXBeanLookup2;
        }
        return mXBeanLookup;
    }

    synchronized <T> T objectNameToMXBean(ObjectName objectName, Class<T> cls) {
        WeakReference<Object> weakReference = this.objectNameToProxy.get(objectName);
        if (weakReference != null) {
            Object obj = weakReference.get();
            if (cls.isInstance(obj)) {
                return cls.cast(obj);
            }
        }
        T t2 = (T) JMX.newMXBeanProxy(this.mbsc, objectName, cls);
        this.objectNameToProxy.put(objectName, new WeakReference<>(t2));
        return t2;
    }

    synchronized ObjectName mxbeanToObjectName(Object obj) throws OpenDataException, IllegalArgumentException {
        String str;
        if (obj instanceof Proxy) {
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(obj);
            if (invocationHandler instanceof MBeanServerInvocationHandler) {
                MBeanServerInvocationHandler mBeanServerInvocationHandler = (MBeanServerInvocationHandler) invocationHandler;
                if (mBeanServerInvocationHandler.getMBeanServerConnection().equals(this.mbsc)) {
                    return mBeanServerInvocationHandler.getObjectName();
                }
                str = "proxy for a different MBeanServer";
            } else {
                str = "not a JMX proxy";
            }
        } else {
            ObjectName objectName = this.mxbeanToObjectName.get(obj);
            if (objectName != null) {
                return objectName;
            }
            str = "not an MXBean registered in this MBeanServer";
        }
        throw new OpenDataException("Could not convert " + (obj == null ? FXMLLoader.NULL_KEYWORD : "object of type " + obj.getClass().getName()) + " to an ObjectName: " + str);
    }

    synchronized void addReference(ObjectName objectName, Object obj) throws InstanceAlreadyExistsException {
        ObjectName objectName2 = this.mxbeanToObjectName.get(obj);
        if (objectName2 != null && !"true".equalsIgnoreCase((String) AccessController.doPrivileged(new GetPropertyAction("jmx.mxbean.multiname")))) {
            throw new InstanceAlreadyExistsException("MXBean already registered with name " + ((Object) objectName2));
        }
        this.mxbeanToObjectName.put(obj, objectName);
    }

    synchronized boolean removeReference(ObjectName objectName, Object obj) {
        if (objectName.equals(this.mxbeanToObjectName.get(obj))) {
            this.mxbeanToObjectName.remove(obj);
            return true;
        }
        return false;
    }

    static MXBeanLookup getLookup() {
        return currentLookup.get();
    }

    static void setLookup(MXBeanLookup mXBeanLookup) {
        currentLookup.set(mXBeanLookup);
    }
}
