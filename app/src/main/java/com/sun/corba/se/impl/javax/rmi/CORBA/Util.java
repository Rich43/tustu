package com.sun.corba.se.impl.javax.rmi.CORBA;

import com.sun.corba.se.impl.corba.AnyImpl;
import com.sun.corba.se.impl.io.ValueHandlerImpl;
import com.sun.corba.se.impl.logging.UtilSystemException;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.impl.util.IdentityHashtable;
import com.sun.corba.se.impl.util.JDKBridge;
import com.sun.corba.se.impl.util.Utility;
import com.sun.corba.se.pept.transport.ContactInfoList;
import com.sun.corba.se.spi.copyobject.ReflectiveCopyException;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORBVersionFactory;
import com.sun.corba.se.spi.protocol.CorbaClientDelegate;
import com.sun.corba.se.spi.transport.CorbaContactInfoList;
import java.io.NotSerializableException;
import java.io.Serializable;
import java.rmi.AccessException;
import java.rmi.MarshalException;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.ServerError;
import java.rmi.ServerException;
import java.rmi.UnexpectedException;
import java.rmi.server.RMIClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.EmptyStackException;
import java.util.Enumeration;
import javax.rmi.CORBA.Stub;
import javax.rmi.CORBA.Tie;
import javax.rmi.CORBA.UtilDelegate;
import javax.rmi.CORBA.ValueHandler;
import javax.transaction.InvalidTransactionException;
import javax.transaction.TransactionRequiredException;
import javax.transaction.TransactionRolledbackException;
import org.omg.CORBA.ACTIVITY_COMPLETED;
import org.omg.CORBA.ACTIVITY_REQUIRED;
import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.COMM_FAILURE;
import org.omg.CORBA.INVALID_ACTIVITY;
import org.omg.CORBA.INVALID_TRANSACTION;
import org.omg.CORBA.INV_OBJREF;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.NO_PERMISSION;
import org.omg.CORBA.OBJECT_NOT_EXIST;
import org.omg.CORBA.OBJ_ADAPTER;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TRANSACTION_REQUIRED;
import org.omg.CORBA.TRANSACTION_ROLLEDBACK;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.Delegate;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.UnknownException;
import sun.corba.SharedSecrets;

/* loaded from: rt.jar:com/sun/corba/se/impl/javax/rmi/CORBA/Util.class */
public class Util implements UtilDelegate {
    private static KeepAlive keepAlive;
    private static IdentityHashtable exportedServants;
    private static final ValueHandlerImpl valueHandlerSingleton;
    private UtilSystemException utilWrapper = UtilSystemException.get(CORBALogDomains.RPC_ENCODING);
    private static Util instance;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Util.class.desiredAssertionStatus();
        keepAlive = null;
        exportedServants = new IdentityHashtable();
        valueHandlerSingleton = SharedSecrets.getJavaCorbaAccess().newValueHandlerImpl();
        instance = null;
    }

    public Util() {
        setInstance(this);
    }

    private static void setInstance(Util util) {
        if (!$assertionsDisabled && instance != null) {
            throw new AssertionError((Object) "Instance already defined");
        }
        instance = util;
    }

    public static Util getInstance() {
        return instance;
    }

    public static boolean isInstanceDefined() {
        return instance != null;
    }

    public void unregisterTargetsForORB(ORB orb) {
        Enumeration enumerationKeys = exportedServants.keys();
        while (enumerationKeys.hasMoreElements()) {
            Object objNextElement2 = enumerationKeys.nextElement2();
            Remote remote = (Remote) (objNextElement2 instanceof Tie ? ((Tie) objNextElement2).getTarget() : objNextElement2);
            try {
                if (orb == getTie(remote).orb()) {
                    try {
                        unexportObject(remote);
                    } catch (NoSuchObjectException e2) {
                    }
                }
            } catch (BAD_OPERATION e3) {
            }
        }
    }

    @Override // javax.rmi.CORBA.UtilDelegate
    public RemoteException mapSystemException(SystemException systemException) {
        String str;
        if (systemException instanceof UnknownException) {
            Throwable th = ((UnknownException) systemException).originalEx;
            if (th instanceof Error) {
                return new ServerError("Error occurred in server thread", (Error) th);
            }
            if (th instanceof RemoteException) {
                return new ServerException("RemoteException occurred in server thread", (Exception) th);
            }
            if (th instanceof RuntimeException) {
                throw ((RuntimeException) th);
            }
        }
        String name = systemException.getClass().getName();
        String strSubstring = name.substring(name.lastIndexOf(46) + 1);
        switch (systemException.completed.value()) {
            case 0:
                str = "Yes";
                break;
            case 1:
                str = "No";
                break;
            case 2:
            default:
                str = "Maybe";
                break;
        }
        String str2 = "CORBA " + strSubstring + " " + systemException.minor + " " + str;
        if (systemException instanceof COMM_FAILURE) {
            return new MarshalException(str2, systemException);
        }
        if (systemException instanceof INV_OBJREF) {
            NoSuchObjectException noSuchObjectException = new NoSuchObjectException(str2);
            noSuchObjectException.detail = systemException;
            return noSuchObjectException;
        }
        if (systemException instanceof NO_PERMISSION) {
            return new AccessException(str2, systemException);
        }
        if (systemException instanceof MARSHAL) {
            return new MarshalException(str2, systemException);
        }
        if (systemException instanceof OBJECT_NOT_EXIST) {
            NoSuchObjectException noSuchObjectException2 = new NoSuchObjectException(str2);
            noSuchObjectException2.detail = systemException;
            return noSuchObjectException2;
        }
        if (systemException instanceof TRANSACTION_REQUIRED) {
            TransactionRequiredException transactionRequiredException = new TransactionRequiredException(str2);
            transactionRequiredException.detail = systemException;
            return transactionRequiredException;
        }
        if (systemException instanceof TRANSACTION_ROLLEDBACK) {
            TransactionRolledbackException transactionRolledbackException = new TransactionRolledbackException(str2);
            transactionRolledbackException.detail = systemException;
            return transactionRolledbackException;
        }
        if (systemException instanceof INVALID_TRANSACTION) {
            InvalidTransactionException invalidTransactionException = new InvalidTransactionException(str2);
            invalidTransactionException.detail = systemException;
            return invalidTransactionException;
        }
        if (systemException instanceof BAD_PARAM) {
            Exception notSerializableException = systemException;
            if (systemException.minor == 1398079489 || systemException.minor == 1330446342) {
                if (systemException.getMessage() != null) {
                    notSerializableException = new NotSerializableException(systemException.getMessage());
                } else {
                    notSerializableException = new NotSerializableException();
                }
                notSerializableException.initCause(systemException);
            }
            return new MarshalException(str2, notSerializableException);
        }
        if (systemException instanceof ACTIVITY_REQUIRED) {
            try {
                return (RemoteException) SharedSecrets.getJavaCorbaAccess().loadClass("javax.activity.ActivityRequiredException").getConstructor(String.class, Throwable.class).newInstance(str2, systemException);
            } catch (Throwable th2) {
                this.utilWrapper.classNotFound(th2, "javax.activity.ActivityRequiredException");
            }
        } else if (systemException instanceof ACTIVITY_COMPLETED) {
            try {
                return (RemoteException) SharedSecrets.getJavaCorbaAccess().loadClass("javax.activity.ActivityCompletedException").getConstructor(String.class, Throwable.class).newInstance(str2, systemException);
            } catch (Throwable th3) {
                this.utilWrapper.classNotFound(th3, "javax.activity.ActivityCompletedException");
            }
        } else if (systemException instanceof INVALID_ACTIVITY) {
            try {
                return (RemoteException) SharedSecrets.getJavaCorbaAccess().loadClass("javax.activity.InvalidActivityException").getConstructor(String.class, Throwable.class).newInstance(str2, systemException);
            } catch (Throwable th4) {
                this.utilWrapper.classNotFound(th4, "javax.activity.InvalidActivityException");
            }
        }
        return new RemoteException(str2, systemException);
    }

    @Override // javax.rmi.CORBA.UtilDelegate
    public void writeAny(OutputStream outputStream, Object obj) throws MARSHAL {
        ORB orb = outputStream.orb();
        Any anyCreate_any = orb.create_any();
        Object objAutoConnect = Utility.autoConnect(obj, orb, false);
        if (objAutoConnect instanceof Object) {
            anyCreate_any.insert_Object((Object) objAutoConnect);
        } else if (objAutoConnect == null) {
            anyCreate_any.insert_Value(null, createTypeCodeForNull(orb));
        } else if (objAutoConnect instanceof Serializable) {
            TypeCode typeCodeCreateTypeCode = createTypeCode((Serializable) objAutoConnect, anyCreate_any, orb);
            if (typeCodeCreateTypeCode == null) {
                anyCreate_any.insert_Value((Serializable) objAutoConnect);
            } else {
                anyCreate_any.insert_Value((Serializable) objAutoConnect, typeCodeCreateTypeCode);
            }
        } else if (objAutoConnect instanceof Remote) {
            ORBUtility.throwNotSerializableForCorba(objAutoConnect.getClass().getName());
        } else {
            ORBUtility.throwNotSerializableForCorba(objAutoConnect.getClass().getName());
        }
        outputStream.write_any(anyCreate_any);
    }

    private TypeCode createTypeCode(Serializable serializable, Any any, ORB orb) {
        if ((any instanceof AnyImpl) && (orb instanceof com.sun.corba.se.spi.orb.ORB)) {
            return ((AnyImpl) any).createTypeCodeForClass(serializable.getClass(), (com.sun.corba.se.spi.orb.ORB) orb);
        }
        return null;
    }

    private TypeCode createTypeCodeForNull(ORB orb) {
        if (orb instanceof com.sun.corba.se.spi.orb.ORB) {
            com.sun.corba.se.spi.orb.ORB orb2 = (com.sun.corba.se.spi.orb.ORB) orb;
            if (!ORBVersionFactory.getFOREIGN().equals(orb2.getORBVersion()) && ORBVersionFactory.getNEWER().compareTo(orb2.getORBVersion()) > 0) {
                return orb.get_primitive_tc(TCKind.tk_value);
            }
        }
        return orb.create_abstract_interface_tc("IDL:omg.org/CORBA/AbstractBase:1.0", "");
    }

    @Override // javax.rmi.CORBA.UtilDelegate
    public Object readAny(InputStream inputStream) {
        Any any = inputStream.read_any();
        if (any.type().kind().value() == 14) {
            return any.extract_Object();
        }
        return any.extract_Value();
    }

    @Override // javax.rmi.CORBA.UtilDelegate
    public void writeRemoteObject(OutputStream outputStream, Object obj) {
        outputStream.write_Object((Object) Utility.autoConnect(obj, outputStream.orb(), false));
    }

    @Override // javax.rmi.CORBA.UtilDelegate
    public void writeAbstractObject(OutputStream outputStream, Object obj) {
        ((org.omg.CORBA_2_3.portable.OutputStream) outputStream).write_abstract_interface(Utility.autoConnect(obj, outputStream.orb(), false));
    }

    @Override // javax.rmi.CORBA.UtilDelegate
    public void registerTarget(Tie tie, Remote remote) {
        synchronized (exportedServants) {
            if (lookupTie(remote) == null) {
                exportedServants.put(remote, tie);
                tie.setTarget(remote);
                if (keepAlive == null) {
                    keepAlive = (KeepAlive) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.corba.se.impl.javax.rmi.CORBA.Util.1
                        @Override // java.security.PrivilegedAction
                        /* renamed from: run */
                        public Object run2() {
                            return new KeepAlive();
                        }
                    });
                    keepAlive.start();
                }
            }
        }
    }

    @Override // javax.rmi.CORBA.UtilDelegate
    public void unexportObject(Remote remote) throws NoSuchObjectException {
        synchronized (exportedServants) {
            Tie tieLookupTie = lookupTie(remote);
            if (tieLookupTie != null) {
                exportedServants.remove(remote);
                Utility.purgeStubForTie(tieLookupTie);
                Utility.purgeTieAndServant(tieLookupTie);
                try {
                    cleanUpTie(tieLookupTie);
                } catch (BAD_OPERATION e2) {
                } catch (OBJ_ADAPTER e3) {
                }
                if (exportedServants.isEmpty()) {
                    keepAlive.quit();
                    keepAlive = null;
                }
            } else {
                throw new NoSuchObjectException("Tie not found");
            }
        }
    }

    protected void cleanUpTie(Tie tie) throws NoSuchObjectException {
        tie.setTarget(null);
        tie.deactivate();
    }

    @Override // javax.rmi.CORBA.UtilDelegate
    public Tie getTie(Remote remote) {
        Tie tieLookupTie;
        synchronized (exportedServants) {
            tieLookupTie = lookupTie(remote);
        }
        return tieLookupTie;
    }

    private static Tie lookupTie(Remote remote) {
        Tie tie = (Tie) exportedServants.get(remote);
        if (tie == null && (remote instanceof Tie) && exportedServants.contains(remote)) {
            tie = (Tie) remote;
        }
        return tie;
    }

    @Override // javax.rmi.CORBA.UtilDelegate
    public ValueHandler createValueHandler() {
        return valueHandlerSingleton;
    }

    @Override // javax.rmi.CORBA.UtilDelegate
    public String getCodebase(Class cls) {
        return RMIClassLoader.getClassAnnotation(cls);
    }

    @Override // javax.rmi.CORBA.UtilDelegate
    public Class loadClass(String str, String str2, ClassLoader classLoader) throws ClassNotFoundException {
        return JDKBridge.loadClass(str, str2, classLoader);
    }

    @Override // javax.rmi.CORBA.UtilDelegate
    public boolean isLocal(Stub stub) throws RemoteException {
        boolean zIs_local = false;
        try {
            Delegate delegate_get_delegate = stub._get_delegate();
            if (delegate_get_delegate instanceof CorbaClientDelegate) {
                ContactInfoList contactInfoList = ((CorbaClientDelegate) delegate_get_delegate).getContactInfoList();
                if (contactInfoList instanceof CorbaContactInfoList) {
                    zIs_local = ((CorbaContactInfoList) contactInfoList).getLocalClientRequestDispatcher().useLocalInvocation(null);
                }
            } else {
                zIs_local = delegate_get_delegate.is_local(stub);
            }
            return zIs_local;
        } catch (SystemException e2) {
            throw javax.rmi.CORBA.Util.mapSystemException(e2);
        }
    }

    @Override // javax.rmi.CORBA.UtilDelegate
    public RemoteException wrapException(Throwable th) {
        if (th instanceof SystemException) {
            return mapSystemException((SystemException) th);
        }
        if (th instanceof Error) {
            return new ServerError("Error occurred in server thread", (Error) th);
        }
        if (th instanceof RemoteException) {
            return new ServerException("RemoteException occurred in server thread", (Exception) th);
        }
        if (th instanceof RuntimeException) {
            throw ((RuntimeException) th);
        }
        if (th instanceof Exception) {
            return new UnexpectedException(th.toString(), (Exception) th);
        }
        return new UnexpectedException(th.toString());
    }

    @Override // javax.rmi.CORBA.UtilDelegate
    public Object[] copyObjects(Object[] objArr, ORB orb) throws RemoteException {
        if (objArr == null) {
            throw new NullPointerException();
        }
        Class<?> componentType = objArr.getClass().getComponentType();
        if (Remote.class.isAssignableFrom(componentType) && !componentType.isInterface()) {
            Remote[] remoteArr = new Remote[objArr.length];
            System.arraycopy(objArr, 0, remoteArr, 0, objArr.length);
            return (Object[]) copyObject(remoteArr, orb);
        }
        return (Object[]) copyObject(objArr, orb);
    }

    @Override // javax.rmi.CORBA.UtilDelegate
    public Object copyObject(Object obj, ORB orb) throws RemoteException {
        if (orb instanceof com.sun.corba.se.spi.orb.ORB) {
            com.sun.corba.se.spi.orb.ORB orb2 = (com.sun.corba.se.spi.orb.ORB) orb;
            try {
                try {
                    return orb2.peekInvocationInfo().getCopierFactory().make().copy(obj);
                } catch (EmptyStackException e2) {
                    return orb2.getCopierManager().getDefaultObjectCopierFactory().make().copy(obj);
                }
            } catch (ReflectiveCopyException e3) {
                RemoteException remoteException = new RemoteException();
                remoteException.initCause(e3);
                throw remoteException;
            }
        }
        org.omg.CORBA_2_3.portable.OutputStream outputStream = (org.omg.CORBA_2_3.portable.OutputStream) orb.create_output_stream();
        outputStream.write_value((Serializable) obj);
        return ((org.omg.CORBA_2_3.portable.InputStream) outputStream.create_input_stream()).read_value();
    }
}
