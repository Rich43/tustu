package javax.management.remote.rmi;

import java.io.IOError;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.SerializablePermission;
import java.rmi.RemoteException;
import java.rmi.UnexpectedException;
import javax.rmi.CORBA.Stub;
import javax.rmi.CORBA.Util;
import javax.rmi.PortableRemoteObject;
import org.omg.CORBA.Object;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.RemarshalException;
import org.omg.CORBA.portable.ServantObject;

/* loaded from: rt.jar:javax/management/remote/rmi/_RMIServer_Stub.class */
public class _RMIServer_Stub extends Stub implements RMIServer {
    private static final String[] _type_ids = {"RMI:javax.management.remote.rmi.RMIServer:0000000000000000"};
    private transient boolean _instantiated;
    static Class class$java$lang$String;
    static Class class$javax$management$remote$rmi$RMIServer;
    static Class class$javax$management$remote$rmi$RMIConnection;
    static Class class$java$io$IOException;

    public _RMIServer_Stub() {
        this(checkPermission());
        this._instantiated = true;
    }

    private _RMIServer_Stub(Void r4) {
        this._instantiated = false;
    }

    @Override // org.omg.CORBA.portable.ObjectImpl
    public String[] _ids() {
        return (String[]) _type_ids.clone();
    }

    private static Void checkPermission() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager == null) {
            return null;
        }
        securityManager.checkPermission(new SerializablePermission("enableSubclassImplementation"));
        return null;
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e2) {
            throw new NoClassDefFoundError(e2.getMessage());
        }
    }

    @Override // javax.management.remote.rmi.RMIServer
    public String getVersion() throws RemoteException {
        Class clsClass$;
        Class clsClass$2;
        if (System.getSecurityManager() != null && !this._instantiated) {
            throw new IOError(new IOException("InvalidObject "));
        }
        if (Util.isLocal(this)) {
            if (class$javax$management$remote$rmi$RMIServer != null) {
                clsClass$ = class$javax$management$remote$rmi$RMIServer;
            } else {
                clsClass$ = class$("javax.management.remote.rmi.RMIServer");
                class$javax$management$remote$rmi$RMIServer = clsClass$;
            }
            ServantObject servantObject_servant_preinvoke = _servant_preinvoke("_get_version", clsClass$);
            try {
                if (servantObject_servant_preinvoke == null) {
                    return getVersion();
                }
                try {
                    return ((RMIServer) servantObject_servant_preinvoke.servant).getVersion();
                } catch (Throwable th) {
                    throw Util.wrapException((Throwable) Util.copyObject(th, _orb()));
                }
            } finally {
                _servant_postinvoke(servantObject_servant_preinvoke);
            }
        }
        InputStream inputStream = null;
        try {
            try {
                try {
                    inputStream = (org.omg.CORBA_2_3.portable.InputStream) _invoke(_request("_get_version", true));
                    if (class$java$lang$String != null) {
                        clsClass$2 = class$java$lang$String;
                    } else {
                        clsClass$2 = class$("java.lang.String");
                        class$java$lang$String = clsClass$2;
                    }
                    return (String) inputStream.read_value(clsClass$2);
                } catch (ApplicationException e2) {
                    throw new UnexpectedException(((org.omg.CORBA_2_3.portable.InputStream) e2.getInputStream()).read_string());
                } catch (RemarshalException unused) {
                    inputStream = inputStream;
                    return getVersion();
                }
            } finally {
                _releaseReply(null);
            }
        } catch (SystemException e3) {
            throw Util.mapSystemException(e3);
        }
    }

    @Override // javax.management.remote.rmi.RMIServer
    public RMIConnection newClient(Object obj) throws IOException {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        if (System.getSecurityManager() != null && !this._instantiated) {
            throw new IOError(new IOException("InvalidObject "));
        }
        if (Util.isLocal(this)) {
            if (class$javax$management$remote$rmi$RMIServer != null) {
                clsClass$ = class$javax$management$remote$rmi$RMIServer;
            } else {
                clsClass$ = class$("javax.management.remote.rmi.RMIServer");
                class$javax$management$remote$rmi$RMIServer = clsClass$;
            }
            ServantObject servantObject_servant_preinvoke = _servant_preinvoke("newClient", clsClass$);
            try {
                if (servantObject_servant_preinvoke == null) {
                    return newClient(obj);
                }
                try {
                    return (RMIConnection) Util.copyObject(((RMIServer) servantObject_servant_preinvoke.servant).newClient(Util.copyObject(obj, _orb())), _orb());
                } catch (Throwable th) {
                    Throwable th2 = (Throwable) Util.copyObject(th, _orb());
                    if (th2 instanceof IOException) {
                        throw ((IOException) th2);
                    }
                    throw Util.wrapException(th2);
                }
            } finally {
                _servant_postinvoke(servantObject_servant_preinvoke);
            }
        }
        org.omg.CORBA_2_3.portable.InputStream inputStream = null;
        try {
            try {
                try {
                    OutputStream outputStream_request = _request("newClient", true);
                    Util.writeAny(outputStream_request, obj);
                    inputStream = (org.omg.CORBA_2_3.portable.InputStream) _invoke(outputStream_request);
                    Object object = inputStream.read_Object();
                    if (class$javax$management$remote$rmi$RMIConnection != null) {
                        clsClass$3 = class$javax$management$remote$rmi$RMIConnection;
                    } else {
                        clsClass$3 = class$("javax.management.remote.rmi.RMIConnection");
                        class$javax$management$remote$rmi$RMIConnection = clsClass$3;
                    }
                    return (RMIConnection) PortableRemoteObject.narrow(object, clsClass$3);
                } catch (ApplicationException e2) {
                    org.omg.CORBA_2_3.portable.InputStream inputStream2 = (org.omg.CORBA_2_3.portable.InputStream) e2.getInputStream();
                    String str = inputStream2.read_string();
                    if (!str.equals("IDL:java/io/IOEx:1.0")) {
                        throw new UnexpectedException(str);
                    }
                    if (class$java$io$IOException != null) {
                        clsClass$2 = class$java$io$IOException;
                    } else {
                        clsClass$2 = class$("java.io.IOException");
                        class$java$io$IOException = clsClass$2;
                    }
                    throw ((IOException) inputStream2.read_value(clsClass$2));
                } catch (RemarshalException unused) {
                    inputStream = inputStream;
                    return newClient(obj);
                }
            } catch (SystemException e3) {
                throw Util.mapSystemException(e3);
            }
        } finally {
            _releaseReply(null);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        checkPermission();
        objectInputStream.defaultReadObject();
        this._instantiated = true;
    }
}
