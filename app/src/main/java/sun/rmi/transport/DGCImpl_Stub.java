package sun.rmi.transport;

import java.io.IOException;
import java.io.ObjectOutput;
import java.rmi.MarshalException;
import java.rmi.RemoteException;
import java.rmi.UnexpectedException;
import java.rmi.UnmarshalException;
import java.rmi.dgc.DGC;
import java.rmi.dgc.Lease;
import java.rmi.dgc.VMID;
import java.rmi.server.ObjID;
import java.rmi.server.Operation;
import java.rmi.server.RemoteRef;
import java.rmi.server.RemoteStub;
import java.rmi.server.UID;
import java.util.ArrayList;
import sun.misc.ObjectInputFilter;
import sun.rmi.transport.tcp.TCPConnection;

/* loaded from: rt.jar:sun/rmi/transport/DGCImpl_Stub.class */
public final class DGCImpl_Stub extends RemoteStub implements DGC {
    private static final long interfaceHash = -669196253586618813L;
    private static final Operation[] operations = {new Operation("void clean(java.rmi.server.ObjID[], long, java.rmi.dgc.VMID, boolean)"), new Operation("java.rmi.dgc.Lease dirty(java.rmi.server.ObjID[], long, java.rmi.dgc.Lease)")};
    private static int DGCCLIENT_MAX_DEPTH = 6;
    private static int DGCCLIENT_MAX_ARRAY_SIZE = 10000;

    public DGCImpl_Stub() {
    }

    public DGCImpl_Stub(RemoteRef remoteRef) {
        super(remoteRef);
    }

    @Override // java.rmi.dgc.DGC
    public void clean(ObjID[] objIDArr, long j2, VMID vmid, boolean z2) throws RemoteException {
        try {
            StreamRemoteCall streamRemoteCall = (StreamRemoteCall) this.ref.newCall(this, operations, 0, interfaceHash);
            streamRemoteCall.setObjectInputFilter(DGCImpl_Stub::leaseFilter);
            try {
                ObjectOutput outputStream = streamRemoteCall.getOutputStream();
                outputStream.writeObject(objIDArr);
                outputStream.writeLong(j2);
                outputStream.writeObject(vmid);
                outputStream.writeBoolean(z2);
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

    @Override // java.rmi.dgc.DGC
    public Lease dirty(ObjID[] objIDArr, long j2, Lease lease) throws RemoteException {
        try {
            StreamRemoteCall streamRemoteCall = (StreamRemoteCall) this.ref.newCall(this, operations, 1, interfaceHash);
            streamRemoteCall.setObjectInputFilter(DGCImpl_Stub::leaseFilter);
            try {
                ObjectOutput outputStream = streamRemoteCall.getOutputStream();
                outputStream.writeObject(objIDArr);
                outputStream.writeLong(j2);
                outputStream.writeObject(lease);
                this.ref.invoke(streamRemoteCall);
                Connection connection = streamRemoteCall.getConnection();
                try {
                    try {
                        Lease lease2 = (Lease) streamRemoteCall.getInputStream().readObject();
                        this.ref.done(streamRemoteCall);
                        return lease2;
                    } catch (Throwable th) {
                        this.ref.done(streamRemoteCall);
                        throw th;
                    }
                } catch (IOException | ClassCastException | ClassNotFoundException e2) {
                    if (connection instanceof TCPConnection) {
                        ((TCPConnection) connection).getChannel().free(connection, false);
                    }
                    streamRemoteCall.discardPendingRefs();
                    throw new UnmarshalException("error unmarshalling return", e2);
                }
            } catch (IOException e3) {
                throw new MarshalException("error marshalling arguments", e3);
            }
        } catch (RuntimeException e4) {
            throw e4;
        } catch (RemoteException e5) {
            throw e5;
        } catch (Exception e6) {
            throw new UnexpectedException("undeclared checked exception", e6);
        }
    }

    private static ObjectInputFilter.Status leaseFilter(ObjectInputFilter.FilterInfo filterInfo) {
        if (filterInfo.depth() > DGCCLIENT_MAX_DEPTH) {
            return ObjectInputFilter.Status.REJECTED;
        }
        Class<?> clsSerialClass = filterInfo.serialClass();
        if (clsSerialClass != null) {
            while (clsSerialClass.isArray()) {
                if (filterInfo.arrayLength() >= 0 && filterInfo.arrayLength() > DGCCLIENT_MAX_ARRAY_SIZE) {
                    return ObjectInputFilter.Status.REJECTED;
                }
                clsSerialClass = clsSerialClass.getComponentType();
            }
            if (clsSerialClass.isPrimitive()) {
                return ObjectInputFilter.Status.ALLOWED;
            }
            return (clsSerialClass == UID.class || clsSerialClass == VMID.class || clsSerialClass == Lease.class || (Throwable.class.isAssignableFrom(clsSerialClass) && clsSerialClass.getClassLoader() == Object.class.getClassLoader()) || clsSerialClass == StackTraceElement.class || clsSerialClass == ArrayList.class || clsSerialClass == Object.class || clsSerialClass.getName().equals("java.util.Collections$UnmodifiableList") || clsSerialClass.getName().equals("java.util.Collections$UnmodifiableCollection") || clsSerialClass.getName().equals("java.util.Collections$UnmodifiableRandomAccessList")) ? ObjectInputFilter.Status.ALLOWED : ObjectInputFilter.Status.REJECTED;
        }
        return ObjectInputFilter.Status.UNDECIDED;
    }
}
