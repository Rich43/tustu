package javax.rmi.CORBA;

import com.sun.corba.se.impl.orbutil.GetPropertyAction;
import java.io.SerializablePermission;
import java.net.MalformedURLException;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RMIClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Properties;
import org.omg.CORBA.INITIALIZE;
import org.omg.CORBA.ORB;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:javax/rmi/CORBA/Util.class */
public class Util {
    private static final String ALLOW_CREATEVALUEHANDLER_PROP = "jdk.rmi.CORBA.allowCustomValueHandler";
    private static final String UtilClassKey = "javax.rmi.CORBA.UtilClass";
    private static final UtilDelegate utilDelegate = (UtilDelegate) createDelegate(UtilClassKey);
    private static boolean allowCustomValueHandler = readAllowCustomValueHandlerProperty();

    private static boolean readAllowCustomValueHandlerProperty() {
        return ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: javax.rmi.CORBA.Util.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Boolean run2() {
                return Boolean.valueOf(Boolean.getBoolean(Util.ALLOW_CREATEVALUEHANDLER_PROP));
            }
        })).booleanValue();
    }

    private Util() {
    }

    public static RemoteException mapSystemException(SystemException systemException) {
        if (utilDelegate != null) {
            return utilDelegate.mapSystemException(systemException);
        }
        return null;
    }

    public static void writeAny(OutputStream outputStream, Object obj) {
        if (utilDelegate != null) {
            utilDelegate.writeAny(outputStream, obj);
        }
    }

    public static Object readAny(InputStream inputStream) {
        if (utilDelegate != null) {
            return utilDelegate.readAny(inputStream);
        }
        return null;
    }

    public static void writeRemoteObject(OutputStream outputStream, Object obj) {
        if (utilDelegate != null) {
            utilDelegate.writeRemoteObject(outputStream, obj);
        }
    }

    public static void writeAbstractObject(OutputStream outputStream, Object obj) {
        if (utilDelegate != null) {
            utilDelegate.writeAbstractObject(outputStream, obj);
        }
    }

    public static void registerTarget(Tie tie, Remote remote) {
        if (utilDelegate != null) {
            utilDelegate.registerTarget(tie, remote);
        }
    }

    public static void unexportObject(Remote remote) throws NoSuchObjectException {
        if (utilDelegate != null) {
            utilDelegate.unexportObject(remote);
        }
    }

    public static Tie getTie(Remote remote) {
        if (utilDelegate != null) {
            return utilDelegate.getTie(remote);
        }
        return null;
    }

    public static ValueHandler createValueHandler() {
        isCustomSerializationPermitted();
        if (utilDelegate != null) {
            return utilDelegate.createValueHandler();
        }
        return null;
    }

    public static String getCodebase(Class cls) {
        if (utilDelegate != null) {
            return utilDelegate.getCodebase(cls);
        }
        return null;
    }

    public static Class loadClass(String str, String str2, ClassLoader classLoader) throws ClassNotFoundException {
        if (utilDelegate != null) {
            return utilDelegate.loadClass(str, str2, classLoader);
        }
        return null;
    }

    public static boolean isLocal(Stub stub) throws RemoteException {
        if (utilDelegate != null) {
            return utilDelegate.isLocal(stub);
        }
        return false;
    }

    public static RemoteException wrapException(Throwable th) {
        if (utilDelegate != null) {
            return utilDelegate.wrapException(th);
        }
        return null;
    }

    public static Object[] copyObjects(Object[] objArr, ORB orb) throws RemoteException {
        if (utilDelegate != null) {
            return utilDelegate.copyObjects(objArr, orb);
        }
        return null;
    }

    public static Object copyObject(Object obj, ORB orb) throws RemoteException {
        if (utilDelegate != null) {
            return utilDelegate.copyObject(obj, orb);
        }
        return null;
    }

    private static Object createDelegate(String str) {
        Properties oRBPropertiesFile;
        String property = (String) AccessController.doPrivileged(new GetPropertyAction(str));
        if (property == null && (oRBPropertiesFile = getORBPropertiesFile()) != null) {
            property = oRBPropertiesFile.getProperty(str);
        }
        if (property == null) {
            return new com.sun.corba.se.impl.javax.rmi.CORBA.Util();
        }
        try {
            return loadDelegateClass(property).newInstance();
        } catch (ClassNotFoundException e2) {
            INITIALIZE initialize = new INITIALIZE("Cannot instantiate " + property);
            initialize.initCause(e2);
            throw initialize;
        } catch (Exception e3) {
            INITIALIZE initialize2 = new INITIALIZE("Error while instantiating" + property);
            initialize2.initCause(e3);
            throw initialize2;
        }
    }

    private static Class loadDelegateClass(String str) throws ClassNotFoundException {
        try {
            return Class.forName(str, false, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e2) {
            try {
                return RMIClassLoader.loadClass(str);
            } catch (MalformedURLException e3) {
                throw new ClassNotFoundException("Could not load " + str + ": " + e3.toString());
            }
        }
    }

    private static Properties getORBPropertiesFile() {
        return (Properties) AccessController.doPrivileged(new GetORBPropertiesFileAction());
    }

    private static void isCustomSerializationPermitted() {
        SecurityManager securityManager = System.getSecurityManager();
        if (!allowCustomValueHandler && securityManager != null) {
            securityManager.checkPermission(new SerializablePermission("enableCustomValueHandler"));
        }
    }
}
