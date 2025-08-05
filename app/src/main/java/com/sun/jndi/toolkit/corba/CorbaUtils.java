package com.sun.jndi.toolkit.corba;

import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.jndi.cosnaming.CNCtx;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import javax.naming.ConfigurationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:com/sun/jndi/toolkit/corba/CorbaUtils.class */
public class CorbaUtils {
    private static Method toStubMethod = null;
    private static Method connectMethod = null;
    private static Class<?> corbaStubClass = null;

    public static Object remoteToCorba(Remote remote, ORB orb) throws ClassNotFoundException, ConfigurationException, IllegalArgumentException {
        synchronized (CorbaUtils.class) {
            if (toStubMethod == null) {
                initMethodHandles();
            }
        }
        try {
            Object objInvoke = toStubMethod.invoke(null, remote);
            if (!corbaStubClass.isInstance(objInvoke)) {
                return null;
            }
            try {
                connectMethod.invoke(objInvoke, orb);
            } catch (IllegalAccessException e2) {
                ConfigurationException configurationException = new ConfigurationException("Cannot invoke javax.rmi.CORBA.Stub.connect()");
                configurationException.setRootCause(e2);
                throw configurationException;
            } catch (InvocationTargetException e3) {
                Throwable targetException = e3.getTargetException();
                if (!(targetException instanceof RemoteException)) {
                    ConfigurationException configurationException2 = new ConfigurationException("Problem invoking javax.rmi.CORBA.Stub.connect()");
                    configurationException2.setRootCause(targetException);
                    throw configurationException2;
                }
            }
            return (Object) objInvoke;
        } catch (IllegalAccessException e4) {
            ConfigurationException configurationException3 = new ConfigurationException("Cannot invoke javax.rmi.PortableRemoteObject.toStub(java.rmi.Remote)");
            configurationException3.setRootCause(e4);
            throw configurationException3;
        } catch (InvocationTargetException e5) {
            Throwable targetException2 = e5.getTargetException();
            ConfigurationException configurationException4 = new ConfigurationException("Problem with PortableRemoteObject.toStub(); object not exported or stub not found");
            configurationException4.setRootCause(targetException2);
            throw configurationException4;
        }
    }

    public static ORB getOrb(String str, int i2, Hashtable<?, ?> hashtable) {
        Properties properties;
        Object obj;
        if (hashtable != null) {
            if (hashtable instanceof Properties) {
                properties = (Properties) hashtable.clone();
            } else {
                properties = new Properties();
                Enumeration<?> enumerationKeys = hashtable.keys();
                while (enumerationKeys.hasMoreElements()) {
                    String str2 = (String) enumerationKeys.nextElement2();
                    Object obj2 = hashtable.get(str2);
                    if (obj2 instanceof String) {
                        properties.put(str2, obj2);
                    }
                }
            }
        } else {
            properties = new Properties();
        }
        if (str != null) {
            properties.put(ORBConstants.INITIAL_HOST_PROPERTY, str);
        }
        if (i2 >= 0) {
            properties.put(ORBConstants.INITIAL_PORT_PROPERTY, "" + i2);
        }
        if (hashtable != null && (obj = hashtable.get(Context.APPLET)) != null) {
            return initAppletORB(obj, properties);
        }
        return ORB.init(new String[0], properties);
    }

    public static boolean isObjectFactoryTrusted(Object obj) throws NamingException {
        Reference reference = null;
        if (obj instanceof Reference) {
            reference = (Reference) obj;
        } else if (obj instanceof Referenceable) {
            reference = ((Referenceable) obj).getReference();
        }
        if (reference != null && reference.getFactoryClassLocation() != null && !CNCtx.trustURLCodebase) {
            throw new ConfigurationException("The object factory is untrusted. Set the system property 'com.sun.jndi.cosnaming.object.trustURLCodebase' to 'true'.");
        }
        return true;
    }

    private static ORB initAppletORB(Object obj, Properties properties) {
        try {
            Class<?> cls = Class.forName("java.applet.Applet", true, null);
            if (!cls.isInstance(obj)) {
                throw new ClassCastException(obj.getClass().getName());
            }
            return (ORB) ORB.class.getMethod("init", cls, Properties.class).invoke(null, obj, properties);
        } catch (ClassNotFoundException e2) {
            throw new ClassCastException(obj.getClass().getName());
        } catch (IllegalAccessException e3) {
            throw new AssertionError(e3);
        } catch (NoSuchMethodException e4) {
            throw new AssertionError(e4);
        } catch (InvocationTargetException e5) {
            Throwable cause = e5.getCause();
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            }
            if (cause instanceof Error) {
                throw ((Error) cause);
            }
            throw new AssertionError(e5);
        }
    }

    private static void initMethodHandles() throws ClassNotFoundException {
        corbaStubClass = Class.forName("javax.rmi.CORBA.Stub");
        try {
            connectMethod = corbaStubClass.getMethod(SecurityConstants.SOCKET_CONNECT_ACTION, ORB.class);
            try {
                toStubMethod = Class.forName("javax.rmi.PortableRemoteObject").getMethod("toStub", Remote.class);
            } catch (NoSuchMethodException e2) {
                throw new IllegalStateException("No method definition for javax.rmi.PortableRemoteObject.toStub(java.rmi.Remote)");
            }
        } catch (NoSuchMethodException e3) {
            throw new IllegalStateException("No method definition for javax.rmi.CORBA.Stub.connect(org.omg.CORBA.ORB)");
        }
    }
}
