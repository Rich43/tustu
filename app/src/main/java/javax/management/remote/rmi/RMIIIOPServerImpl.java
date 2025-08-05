package javax.management.remote.rmi;

import com.sun.jmx.remote.internal.IIOPHelper;
import java.io.IOException;
import java.rmi.Remote;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Collections;
import java.util.Map;
import javax.security.auth.Subject;

/* loaded from: rt.jar:javax/management/remote/rmi/RMIIIOPServerImpl.class */
public class RMIIIOPServerImpl extends RMIServerImpl {
    private final Map<String, ?> env;
    private final AccessControlContext callerACC;

    public RMIIIOPServerImpl(Map<String, ?> map) throws IOException {
        super(map);
        this.env = map == null ? Collections.emptyMap() : map;
        this.callerACC = AccessController.getContext();
    }

    @Override // javax.management.remote.rmi.RMIServerImpl
    protected void export() throws IOException {
        IIOPHelper.exportObject(this);
    }

    @Override // javax.management.remote.rmi.RMIServerImpl
    protected String getProtocol() {
        return "iiop";
    }

    @Override // javax.management.remote.rmi.RMIServerImpl
    public Remote toStub() throws IOException {
        return IIOPHelper.toStub(this);
    }

    @Override // javax.management.remote.rmi.RMIServerImpl
    protected RMIConnection makeClient(String str, Subject subject) throws IOException {
        if (str == null) {
            throw new NullPointerException("Null connectionId");
        }
        RMIConnectionImpl rMIConnectionImpl = new RMIConnectionImpl(this, str, getDefaultClassLoader(), subject, this.env);
        IIOPHelper.exportObject(rMIConnectionImpl);
        return rMIConnectionImpl;
    }

    @Override // javax.management.remote.rmi.RMIServerImpl
    protected void closeClient(RMIConnection rMIConnection) throws IOException {
        IIOPHelper.unexportObject(rMIConnection);
    }

    @Override // javax.management.remote.rmi.RMIServerImpl
    protected void closeServer() throws IOException {
        IIOPHelper.unexportObject(this);
    }

    @Override // javax.management.remote.rmi.RMIServerImpl
    RMIConnection doNewClient(final Object obj) throws IOException {
        if (this.callerACC == null) {
            throw new SecurityException("AccessControlContext cannot be null");
        }
        try {
            return (RMIConnection) AccessController.doPrivileged(new PrivilegedExceptionAction<RMIConnection>() { // from class: javax.management.remote.rmi.RMIIIOPServerImpl.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public RMIConnection run() throws IOException {
                    return RMIIIOPServerImpl.this.superDoNewClient(obj);
                }
            }, this.callerACC);
        } catch (PrivilegedActionException e2) {
            throw ((IOException) e2.getCause());
        }
    }

    RMIConnection superDoNewClient(Object obj) throws IOException {
        return super.doNewClient(obj);
    }
}
