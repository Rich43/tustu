package sun.rmi.registry;

import java.io.IOException;
import java.io.ObjectOutput;
import java.rmi.AlreadyBoundException;
import java.rmi.MarshalException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.UnexpectedException;
import java.rmi.UnmarshalException;
import java.rmi.registry.Registry;
import java.rmi.server.Operation;
import java.rmi.server.RemoteRef;
import java.rmi.server.RemoteStub;
import sun.rmi.transport.StreamRemoteCall;

/* loaded from: rt.jar:sun/rmi/registry/RegistryImpl_Stub.class */
public final class RegistryImpl_Stub extends RemoteStub implements Registry, Remote {
    private static final Operation[] operations = {new Operation("void bind(java.lang.String, java.rmi.Remote)"), new Operation("java.lang.String list()[]"), new Operation("java.rmi.Remote lookup(java.lang.String)"), new Operation("void rebind(java.lang.String, java.rmi.Remote)"), new Operation("void unbind(java.lang.String)")};
    private static final long interfaceHash = 4905912898345647071L;

    public RegistryImpl_Stub() {
    }

    public RegistryImpl_Stub(RemoteRef remoteRef) {
        super(remoteRef);
    }

    @Override // java.rmi.registry.Registry
    public void bind(String str, Remote remote) throws AlreadyBoundException, RemoteException {
        try {
            StreamRemoteCall streamRemoteCall = (StreamRemoteCall) this.ref.newCall(this, operations, 0, interfaceHash);
            try {
                ObjectOutput outputStream = streamRemoteCall.getOutputStream();
                outputStream.writeObject(str);
                outputStream.writeObject(remote);
                this.ref.invoke(streamRemoteCall);
                this.ref.done(streamRemoteCall);
            } catch (IOException e2) {
                throw new MarshalException("error marshalling arguments", e2);
            }
        } catch (RuntimeException e3) {
            throw e3;
        } catch (AlreadyBoundException e4) {
            throw e4;
        } catch (RemoteException e5) {
            throw e5;
        } catch (Exception e6) {
            throw new UnexpectedException("undeclared checked exception", e6);
        }
    }

    @Override // java.rmi.registry.Registry
    public String[] list() throws RemoteException {
        try {
            StreamRemoteCall streamRemoteCall = (StreamRemoteCall) this.ref.newCall(this, operations, 1, interfaceHash);
            this.ref.invoke(streamRemoteCall);
            try {
                try {
                    String[] strArr = (String[]) streamRemoteCall.getInputStream().readObject();
                    this.ref.done(streamRemoteCall);
                    return strArr;
                } catch (Throwable th) {
                    this.ref.done(streamRemoteCall);
                    throw th;
                }
            } catch (IOException | ClassCastException | ClassNotFoundException e2) {
                streamRemoteCall.discardPendingRefs();
                throw new UnmarshalException("error unmarshalling return", e2);
            }
        } catch (RuntimeException e3) {
            throw e3;
        } catch (RemoteException e4) {
            throw e4;
        } catch (Exception e5) {
            throw new UnexpectedException("undeclared checked exception", e5);
        }
    }

    @Override // java.rmi.registry.Registry
    public Remote lookup(String str) throws NotBoundException, RemoteException {
        try {
            StreamRemoteCall streamRemoteCall = (StreamRemoteCall) this.ref.newCall(this, operations, 2, interfaceHash);
            try {
                streamRemoteCall.getOutputStream().writeObject(str);
                this.ref.invoke(streamRemoteCall);
                try {
                    try {
                        Remote remote = (Remote) streamRemoteCall.getInputStream().readObject();
                        this.ref.done(streamRemoteCall);
                        return remote;
                    } catch (Throwable th) {
                        this.ref.done(streamRemoteCall);
                        throw th;
                    }
                } catch (IOException | ClassCastException | ClassNotFoundException e2) {
                    streamRemoteCall.discardPendingRefs();
                    throw new UnmarshalException("error unmarshalling return", e2);
                }
            } catch (IOException e3) {
                throw new MarshalException("error marshalling arguments", e3);
            }
        } catch (RuntimeException e4) {
            throw e4;
        } catch (NotBoundException e5) {
            throw e5;
        } catch (RemoteException e6) {
            throw e6;
        } catch (Exception e7) {
            throw new UnexpectedException("undeclared checked exception", e7);
        }
    }

    @Override // java.rmi.registry.Registry
    public void rebind(String str, Remote remote) throws RemoteException {
        try {
            StreamRemoteCall streamRemoteCall = (StreamRemoteCall) this.ref.newCall(this, operations, 3, interfaceHash);
            try {
                ObjectOutput outputStream = streamRemoteCall.getOutputStream();
                outputStream.writeObject(str);
                outputStream.writeObject(remote);
                this.ref.invoke(streamRemoteCall);
                this.ref.done(streamRemoteCall);
            } catch (IOException e2) {
                throw new MarshalException("error marshalling arguments", e2);
            }
        } catch (RuntimeException e3) {
            throw e3;
        } catch (RemoteException e4) {
            throw e4;
        } catch (Exception e5) {
            throw new UnexpectedException("undeclared checked exception", e5);
        }
    }

    @Override // java.rmi.registry.Registry
    public void unbind(String str) throws NotBoundException, RemoteException {
        try {
            StreamRemoteCall streamRemoteCall = (StreamRemoteCall) this.ref.newCall(this, operations, 4, interfaceHash);
            try {
                streamRemoteCall.getOutputStream().writeObject(str);
                this.ref.invoke(streamRemoteCall);
                this.ref.done(streamRemoteCall);
            } catch (IOException e2) {
                throw new MarshalException("error marshalling arguments", e2);
            }
        } catch (RuntimeException e3) {
            throw e3;
        } catch (NotBoundException e4) {
            throw e4;
        } catch (RemoteException e5) {
            throw e5;
        } catch (Exception e6) {
            throw new UnexpectedException("undeclared checked exception", e6);
        }
    }
}
