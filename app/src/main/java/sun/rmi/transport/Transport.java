package sun.rmi.transport;

import com.sun.xml.internal.ws.wsdl.parser.WSDLConstants;
import java.io.IOException;
import java.io.ObjectOutput;
import java.rmi.MarshalException;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.LogStream;
import java.rmi.server.ObjID;
import java.rmi.server.RemoteCall;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permissions;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.ProtectionDomain;
import sun.rmi.runtime.Log;
import sun.rmi.server.Dispatcher;
import sun.rmi.server.UnicastServerRef;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/rmi/transport/Transport.class */
public abstract class Transport {
    static final int logLevel = LogStream.parseLevel(getLogLevel());
    static final Log transportLog = Log.getLog("sun.rmi.transport.misc", WSDLConstants.ATTR_TRANSPORT, logLevel);
    private static final ThreadLocal<Transport> currentTransport = new ThreadLocal<>();
    private static final ObjID dgcID = new ObjID(2);
    private static final AccessControlContext SETCCL_ACC;

    public abstract Channel getChannel(Endpoint endpoint);

    public abstract void free(Endpoint endpoint);

    protected abstract void checkAcceptPermission(AccessControlContext accessControlContext);

    static {
        Permissions permissions = new Permissions();
        permissions.add(new RuntimePermission("setContextClassLoader"));
        SETCCL_ACC = new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(null, permissions)});
    }

    private static String getLogLevel() {
        return (String) AccessController.doPrivileged(new GetPropertyAction("sun.rmi.transport.logLevel"));
    }

    public void exportObject(Target target) throws RemoteException {
        target.setExportedTransport(this);
        ObjectTable.putTarget(target);
    }

    protected void targetUnexported() {
    }

    static Transport currentTransport() {
        return currentTransport.get();
    }

    private static void setContextClassLoader(ClassLoader classLoader) {
        AccessController.doPrivileged(() -> {
            Thread.currentThread().setContextClassLoader(classLoader);
            return null;
        }, SETCCL_ACC);
    }

    /* JADX WARN: Finally extract failed */
    public boolean serviceCall(final RemoteCall remoteCall) throws NoSuchObjectException, MarshalException {
        final Remote impl;
        try {
            try {
                ObjID objID = ObjID.read(remoteCall.getInputStream());
                Target target = ObjectTable.getTarget(new ObjectEndpoint(objID, objID.equals(dgcID) ? null : this));
                if (target == null || (impl = target.getImpl()) == null) {
                    throw new NoSuchObjectException("no such object in table");
                }
                final Dispatcher dispatcher = target.getDispatcher();
                target.incrementCallCount();
                try {
                    try {
                        transportLog.log(Log.VERBOSE, "call dispatcher");
                        final AccessControlContext accessControlContext = target.getAccessControlContext();
                        ClassLoader contextClassLoader = target.getContextClassLoader();
                        ClassLoader contextClassLoader2 = Thread.currentThread().getContextClassLoader();
                        try {
                            setContextClassLoader(contextClassLoader);
                            currentTransport.set(this);
                            try {
                                AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() { // from class: sun.rmi.transport.Transport.1
                                    /* JADX WARN: Can't rename method to resolve collision */
                                    @Override // java.security.PrivilegedExceptionAction
                                    public Void run() throws IOException {
                                        Transport.this.checkAcceptPermission(accessControlContext);
                                        dispatcher.dispatch(impl, remoteCall);
                                        return null;
                                    }
                                }, accessControlContext);
                                setContextClassLoader(contextClassLoader2);
                                currentTransport.set(null);
                                target.decrementCallCount();
                                return true;
                            } catch (PrivilegedActionException e2) {
                                throw ((IOException) e2.getException());
                            }
                        } catch (Throwable th) {
                            setContextClassLoader(contextClassLoader2);
                            currentTransport.set(null);
                            throw th;
                        }
                    } catch (IOException e3) {
                        transportLog.log(Log.BRIEF, "exception thrown by dispatcher: ", e3);
                        target.decrementCallCount();
                        return false;
                    }
                } catch (Throwable th2) {
                    target.decrementCallCount();
                    throw th2;
                }
            } catch (IOException e4) {
                throw new MarshalException("unable to read objID", e4);
            }
        } catch (RemoteException e5) {
            if (UnicastServerRef.callLog.isLoggable(Log.BRIEF)) {
                String str = "";
                try {
                    str = "[" + RemoteServer.getClientHost() + "] ";
                } catch (ServerNotActiveException e6) {
                }
                UnicastServerRef.callLog.log(Log.BRIEF, str + "exception: ", e5);
            }
            try {
                ObjectOutput resultStream = remoteCall.getResultStream(false);
                UnicastServerRef.clearStackTraces(e5);
                resultStream.writeObject(e5);
                remoteCall.releaseOutputStream();
                return true;
            } catch (IOException e7) {
                transportLog.log(Log.BRIEF, "exception thrown marshalling exception: ", e7);
                return false;
            }
        }
    }
}
