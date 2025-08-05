package javax.rmi.CORBA;

import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import org.omg.CORBA.ORB;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:javax/rmi/CORBA/UtilDelegate.class */
public interface UtilDelegate {
    RemoteException mapSystemException(SystemException systemException);

    void writeAny(OutputStream outputStream, Object obj);

    Object readAny(InputStream inputStream);

    void writeRemoteObject(OutputStream outputStream, Object obj);

    void writeAbstractObject(OutputStream outputStream, Object obj);

    void registerTarget(Tie tie, Remote remote);

    void unexportObject(Remote remote) throws NoSuchObjectException;

    Tie getTie(Remote remote);

    ValueHandler createValueHandler();

    String getCodebase(Class cls);

    Class loadClass(String str, String str2, ClassLoader classLoader) throws ClassNotFoundException;

    boolean isLocal(Stub stub) throws RemoteException;

    RemoteException wrapException(Throwable th);

    Object copyObject(Object obj, ORB orb) throws RemoteException;

    Object[] copyObjects(Object[] objArr, ORB orb) throws RemoteException;
}
