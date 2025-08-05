package sun.rmi.server;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectStreamClass;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.AccessException;
import java.rmi.MarshalException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.ServerError;
import java.rmi.ServerException;
import java.rmi.UnmarshalException;
import java.rmi.server.ExportException;
import java.rmi.server.RemoteCall;
import java.rmi.server.RemoteRef;
import java.rmi.server.RemoteStub;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.ServerRef;
import java.rmi.server.Skeleton;
import java.rmi.server.SkeletonNotFoundException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.misc.ObjectInputFilter;
import sun.rmi.runtime.Log;
import sun.rmi.server.MarshalInputStream;
import sun.rmi.transport.LiveRef;
import sun.rmi.transport.StreamRemoteCall;
import sun.rmi.transport.Target;
import sun.rmi.transport.tcp.TCPTransport;
import sun.security.action.GetBooleanAction;

/* loaded from: rt.jar:sun/rmi/server/UnicastServerRef.class */
public class UnicastServerRef extends UnicastRef implements ServerRef, Dispatcher {
    private static final long serialVersionUID = -7384275867073752268L;
    private boolean forceStubUse;
    private transient Skeleton skel;
    private final transient ObjectInputFilter filter;
    private transient Map<Long, Method> hashToMethod_Map;
    private final AtomicInteger methodCallIDCount;
    public static final boolean logCalls = ((Boolean) AccessController.doPrivileged(new GetBooleanAction("java.rmi.server.logCalls"))).booleanValue();
    public static final Log callLog = Log.getLog("sun.rmi.server.call", "RMI", logCalls);
    private static final boolean wantExceptionLog = ((Boolean) AccessController.doPrivileged(new GetBooleanAction("sun.rmi.server.exceptionTrace"))).booleanValue();
    private static final boolean suppressStackTraces = ((Boolean) AccessController.doPrivileged(new GetBooleanAction("sun.rmi.server.suppressStackTraces"))).booleanValue();
    private static final WeakClassHashMap<Map<Long, Method>> hashToMethod_Maps = new HashToMethod_Maps();
    private static final Map<Class<?>, ?> withoutSkeletons = Collections.synchronizedMap(new WeakHashMap());

    public UnicastServerRef() {
        this.forceStubUse = false;
        this.hashToMethod_Map = null;
        this.methodCallIDCount = new AtomicInteger(0);
        this.filter = null;
    }

    public UnicastServerRef(LiveRef liveRef) {
        super(liveRef);
        this.forceStubUse = false;
        this.hashToMethod_Map = null;
        this.methodCallIDCount = new AtomicInteger(0);
        this.filter = null;
    }

    public UnicastServerRef(LiveRef liveRef, ObjectInputFilter objectInputFilter) {
        super(liveRef);
        this.forceStubUse = false;
        this.hashToMethod_Map = null;
        this.methodCallIDCount = new AtomicInteger(0);
        this.filter = objectInputFilter;
    }

    public UnicastServerRef(int i2) {
        super(new LiveRef(i2));
        this.forceStubUse = false;
        this.hashToMethod_Map = null;
        this.methodCallIDCount = new AtomicInteger(0);
        this.filter = null;
    }

    public UnicastServerRef(boolean z2) {
        this(0);
        this.forceStubUse = z2;
    }

    @Override // java.rmi.server.ServerRef
    public RemoteStub exportObject(Remote remote, Object obj) throws RemoteException {
        this.forceStubUse = true;
        return (RemoteStub) exportObject(remote, obj, false);
    }

    public Remote exportObject(Remote remote, Object obj, boolean z2) throws RemoteException {
        Class<?> cls = remote.getClass();
        try {
            Remote remoteCreateProxy = Util.createProxy(cls, getClientRef(), this.forceStubUse);
            if (remoteCreateProxy instanceof RemoteStub) {
                setSkeleton(remote);
            }
            this.ref.exportObject(new Target(remote, this, remoteCreateProxy, this.ref.getObjID(), z2));
            this.hashToMethod_Map = hashToMethod_Maps.get(cls);
            return remoteCreateProxy;
        } catch (IllegalArgumentException e2) {
            throw new ExportException("remote object implements illegal remote interface", e2);
        }
    }

    @Override // java.rmi.server.ServerRef
    public String getClientHost() throws ServerNotActiveException {
        return TCPTransport.getClientHost();
    }

    public void setSkeleton(Remote remote) throws RemoteException {
        if (!withoutSkeletons.containsKey(remote.getClass())) {
            try {
                this.skel = Util.createSkeleton(remote);
            } catch (SkeletonNotFoundException e2) {
                withoutSkeletons.put(remote.getClass(), null);
            }
        }
    }

    /* JADX WARN: Finally extract failed */
    @Override // sun.rmi.server.Dispatcher
    public void dispatch(Remote remote, RemoteCall remoteCall) throws IOException {
        try {
            try {
                try {
                    ObjectInput inputStream = remoteCall.getInputStream();
                    int i2 = inputStream.readInt();
                    if (this.skel != null) {
                        oldDispatch(remote, remoteCall, i2);
                        remoteCall.releaseInputStream();
                        remoteCall.releaseOutputStream();
                        return;
                    }
                    if (i2 >= 0) {
                        throw new UnmarshalException("skeleton class not found but required for client version");
                    }
                    try {
                        long j2 = inputStream.readLong();
                        MarshalInputStream marshalInputStream = (MarshalInputStream) inputStream;
                        marshalInputStream.skipDefaultResolveClass();
                        Method method = this.hashToMethod_Map.get(Long.valueOf(j2));
                        if (method == null) {
                            throw new UnmarshalException("unrecognized method hash: method not supported by remote object");
                        }
                        logCall(remote, method);
                        try {
                            try {
                                unmarshalCustomCallData(inputStream);
                                Object[] objArrUnmarshalParameters = unmarshalParameters(remote, method, marshalInputStream);
                                remoteCall.releaseInputStream();
                                try {
                                    Object objInvoke = method.invoke(remote, objArrUnmarshalParameters);
                                    try {
                                        ObjectOutput resultStream = remoteCall.getResultStream(true);
                                        Class<?> returnType = method.getReturnType();
                                        if (returnType != Void.TYPE) {
                                            marshalValue(returnType, objInvoke, resultStream);
                                        }
                                        remoteCall.releaseInputStream();
                                        remoteCall.releaseOutputStream();
                                    } catch (IOException e2) {
                                        throw new MarshalException("error marshalling return", e2);
                                    }
                                } catch (InvocationTargetException e3) {
                                    throw e3.getTargetException();
                                }
                            } catch (AccessException e4) {
                                ((StreamRemoteCall) remoteCall).discardPendingRefs();
                                throw e4;
                            } catch (IOException | ClassNotFoundException e5) {
                                ((StreamRemoteCall) remoteCall).discardPendingRefs();
                                throw new UnmarshalException("error unmarshalling arguments", e5);
                            }
                        } catch (Throwable th) {
                            remoteCall.releaseInputStream();
                            throw th;
                        }
                    } catch (Exception e6) {
                        throw new UnmarshalException("error unmarshalling call header", e6);
                    }
                } catch (Throwable th2) {
                    th = th2;
                    logCallException(th);
                    ObjectOutput resultStream2 = remoteCall.getResultStream(false);
                    if (th instanceof Error) {
                        th = new ServerError("Error occurred in server thread", (Error) th);
                    } else if (th instanceof RemoteException) {
                        th = new ServerException("RemoteException occurred in server thread", (Exception) th);
                    }
                    if (suppressStackTraces) {
                        clearStackTraces(th);
                    }
                    resultStream2.writeObject(th);
                    if (th instanceof AccessException) {
                        throw new IOException("Connection is not reusable", th);
                    }
                    remoteCall.releaseInputStream();
                    remoteCall.releaseOutputStream();
                }
            } catch (Exception e7) {
                throw new UnmarshalException("error unmarshalling call header", e7);
            }
        } catch (Throwable th3) {
            remoteCall.releaseInputStream();
            remoteCall.releaseOutputStream();
            throw th3;
        }
    }

    protected void unmarshalCustomCallData(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        if (this.filter != null && (objectInput instanceof ObjectInputStream)) {
            final ObjectInputStream objectInputStream = (ObjectInputStream) objectInput;
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.rmi.server.UnicastServerRef.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() {
                    ObjectInputFilter.Config.setObjectInputFilter(objectInputStream, UnicastServerRef.this.filter);
                    return null;
                }
            });
        }
    }

    private void oldDispatch(Remote remote, RemoteCall remoteCall, int i2) throws Exception {
        ObjectInput inputStream = remoteCall.getInputStream();
        try {
            if (Class.forName("sun.rmi.transport.DGCImpl_Skel").isAssignableFrom(this.skel.getClass())) {
                ((MarshalInputStream) inputStream).useCodebaseOnly();
            }
        } catch (ClassNotFoundException e2) {
        }
        try {
            long j2 = inputStream.readLong();
            Object[] operations = this.skel.getOperations();
            logCall(remote, (i2 < 0 || i2 >= operations.length) ? "op: " + i2 : operations[i2]);
            unmarshalCustomCallData(inputStream);
            this.skel.dispatch(remote, remoteCall, i2, j2);
        } catch (Exception e3) {
            throw new UnmarshalException("error unmarshalling call header", e3);
        }
    }

    public static void clearStackTraces(Throwable th) {
        StackTraceElement[] stackTraceElementArr = new StackTraceElement[0];
        while (th != null) {
            th.setStackTrace(stackTraceElementArr);
            th = th.getCause();
        }
    }

    private void logCall(Remote remote, Object obj) {
        String clientHost;
        if (callLog.isLoggable(Log.VERBOSE)) {
            try {
                clientHost = getClientHost();
            } catch (ServerNotActiveException e2) {
                clientHost = "(local)";
            }
            callLog.log(Log.VERBOSE, "[" + clientHost + ": " + remote.getClass().getName() + this.ref.getObjID().toString() + ": " + obj + "]");
        }
    }

    private void logCallException(Throwable th) {
        if (callLog.isLoggable(Log.BRIEF)) {
            String str = "";
            try {
                str = "[" + getClientHost() + "] ";
            } catch (ServerNotActiveException e2) {
            }
            callLog.log(Log.BRIEF, str + "exception: ", th);
        }
        if (wantExceptionLog) {
            PrintStream printStream = System.err;
            synchronized (printStream) {
                printStream.println();
                printStream.println("Exception dispatching call to " + ((Object) this.ref.getObjID()) + " in thread \"" + Thread.currentThread().getName() + "\" at " + ((Object) new Date()) + CallSiteDescriptor.TOKEN_DELIMITER);
                th.printStackTrace(printStream);
            }
        }
    }

    @Override // sun.rmi.server.UnicastRef, java.rmi.server.RemoteRef
    public String getRefClass(ObjectOutput objectOutput) {
        return "UnicastServerRef";
    }

    protected RemoteRef getClientRef() {
        return new UnicastRef(this.ref);
    }

    @Override // sun.rmi.server.UnicastRef, java.io.Externalizable
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
    }

    @Override // sun.rmi.server.UnicastRef, java.io.Externalizable
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        this.ref = null;
        this.skel = null;
    }

    /* loaded from: rt.jar:sun/rmi/server/UnicastServerRef$HashToMethod_Maps.class */
    private static class HashToMethod_Maps extends WeakClassHashMap<Map<Long, Method>> {
        @Override // sun.rmi.server.WeakClassHashMap
        protected /* bridge */ /* synthetic */ Map<Long, Method> computeValue(Class cls) {
            return computeValue((Class<?>) cls);
        }

        HashToMethod_Maps() {
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // sun.rmi.server.WeakClassHashMap
        protected Map<Long, Method> computeValue(Class<?> cls) throws SecurityException {
            HashMap map = new HashMap();
            Class<?> superclass = cls;
            while (true) {
                Class<?> cls2 = superclass;
                if (cls2 != null) {
                    for (Class<?> cls3 : cls2.getInterfaces()) {
                        if (Remote.class.isAssignableFrom(cls3)) {
                            for (final Method method : cls3.getMethods()) {
                                AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.rmi.server.UnicastServerRef.HashToMethod_Maps.1
                                    /* JADX WARN: Can't rename method to resolve collision */
                                    @Override // java.security.PrivilegedAction
                                    /* renamed from: run */
                                    public Void run2() {
                                        method.setAccessible(true);
                                        return null;
                                    }
                                });
                                map.put(Long.valueOf(Util.computeMethodHash(method)), method);
                            }
                        }
                    }
                    superclass = cls2.getSuperclass();
                } else {
                    return map;
                }
            }
        }
    }

    private Object[] unmarshalParameters(Object obj, Method method, MarshalInputStream marshalInputStream) throws IOException, ClassNotFoundException {
        if (obj instanceof DeserializationChecker) {
            return unmarshalParametersChecked((DeserializationChecker) obj, method, marshalInputStream);
        }
        return unmarshalParametersUnchecked(method, marshalInputStream);
    }

    private Object[] unmarshalParametersUnchecked(Method method, ObjectInput objectInput) throws IOException, ClassNotFoundException {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] objArr = new Object[parameterTypes.length];
        for (int i2 = 0; i2 < parameterTypes.length; i2++) {
            objArr[i2] = unmarshalValue(parameterTypes[i2], objectInput);
        }
        return objArr;
    }

    private Object[] unmarshalParametersChecked(DeserializationChecker deserializationChecker, Method method, MarshalInputStream marshalInputStream) throws IOException, ClassNotFoundException {
        int andIncrement = this.methodCallIDCount.getAndIncrement();
        MyChecker myChecker = new MyChecker(deserializationChecker, method, andIncrement);
        marshalInputStream.setStreamChecker(myChecker);
        try {
            Class<?>[] parameterTypes = method.getParameterTypes();
            Object[] objArr = new Object[parameterTypes.length];
            for (int i2 = 0; i2 < parameterTypes.length; i2++) {
                myChecker.setIndex(i2);
                objArr[i2] = unmarshalValue(parameterTypes[i2], marshalInputStream);
            }
            myChecker.end(andIncrement);
            marshalInputStream.setStreamChecker(null);
            return objArr;
        } catch (Throwable th) {
            marshalInputStream.setStreamChecker(null);
            throw th;
        }
    }

    /* loaded from: rt.jar:sun/rmi/server/UnicastServerRef$MyChecker.class */
    private static class MyChecker implements MarshalInputStream.StreamChecker {
        private final DeserializationChecker descriptorCheck;
        private final Method method;
        private final int callID;
        private int parameterIndex;

        MyChecker(DeserializationChecker deserializationChecker, Method method, int i2) {
            this.descriptorCheck = deserializationChecker;
            this.method = method;
            this.callID = i2;
        }

        @Override // sun.misc.ObjectStreamClassValidator
        public void validateDescriptor(ObjectStreamClass objectStreamClass) {
            this.descriptorCheck.check(this.method, objectStreamClass, this.parameterIndex, this.callID);
        }

        @Override // sun.rmi.server.MarshalInputStream.StreamChecker
        public void checkProxyInterfaceNames(String[] strArr) {
            this.descriptorCheck.checkProxyClass(this.method, strArr, this.parameterIndex, this.callID);
        }

        void setIndex(int i2) {
            this.parameterIndex = i2;
        }

        void end(int i2) {
            this.descriptorCheck.end(i2);
        }
    }
}
