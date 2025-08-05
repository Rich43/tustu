package javax.rmi;

import com.sun.corba.se.impl.orbutil.GetPropertyAction;
import java.net.MalformedURLException;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RMIClassLoader;
import java.security.AccessController;
import java.util.Properties;
import javax.rmi.CORBA.PortableRemoteObjectDelegate;
import org.omg.CORBA.INITIALIZE;

/* loaded from: rt.jar:javax/rmi/PortableRemoteObject.class */
public class PortableRemoteObject {
    private static final String PortableRemoteObjectClassKey = "javax.rmi.CORBA.PortableRemoteObjectClass";
    private static final PortableRemoteObjectDelegate proDelegate = (PortableRemoteObjectDelegate) createDelegate(PortableRemoteObjectClassKey);

    /* JADX WARN: Multi-variable type inference failed */
    protected PortableRemoteObject() throws RemoteException {
        if (proDelegate != null) {
            exportObject((Remote) this);
        }
    }

    public static void exportObject(Remote remote) throws RemoteException {
        if (proDelegate != null) {
            proDelegate.exportObject(remote);
        }
    }

    public static Remote toStub(Remote remote) throws NoSuchObjectException {
        if (proDelegate != null) {
            return proDelegate.toStub(remote);
        }
        return null;
    }

    public static void unexportObject(Remote remote) throws NoSuchObjectException {
        if (proDelegate != null) {
            proDelegate.unexportObject(remote);
        }
    }

    public static Object narrow(Object obj, Class cls) throws ClassCastException {
        if (proDelegate != null) {
            return proDelegate.narrow(obj, cls);
        }
        return null;
    }

    public static void connect(Remote remote, Remote remote2) throws RemoteException {
        if (proDelegate != null) {
            proDelegate.connect(remote, remote2);
        }
    }

    private static Object createDelegate(String str) {
        Properties oRBPropertiesFile;
        String property = (String) AccessController.doPrivileged(new GetPropertyAction(str));
        if (property == null && (oRBPropertiesFile = getORBPropertiesFile()) != null) {
            property = oRBPropertiesFile.getProperty(str);
        }
        if (property == null) {
            return new com.sun.corba.se.impl.javax.rmi.PortableRemoteObject();
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
}
