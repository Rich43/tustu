package sun.rmi.transport;

import java.io.IOException;
import java.io.ObjectInput;
import java.rmi.MarshalException;
import java.rmi.Remote;
import java.rmi.UnmarshalException;
import java.rmi.dgc.Lease;
import java.rmi.dgc.VMID;
import java.rmi.server.ObjID;
import java.rmi.server.Operation;
import java.rmi.server.RemoteCall;
import java.rmi.server.Skeleton;
import java.rmi.server.SkeletonMismatchException;

/* loaded from: rt.jar:sun/rmi/transport/DGCImpl_Skel.class */
public final class DGCImpl_Skel implements Skeleton {
    private static final Operation[] operations = {new Operation("void clean(java.rmi.server.ObjID[], long, java.rmi.dgc.VMID, boolean)"), new Operation("java.rmi.dgc.Lease dirty(java.rmi.server.ObjID[], long, java.rmi.dgc.Lease)")};
    private static final long interfaceHash = -669196253586618813L;

    @Override // java.rmi.server.Skeleton
    public Operation[] getOperations() {
        return (Operation[]) operations.clone();
    }

    @Override // java.rmi.server.Skeleton
    public void dispatch(Remote remote, RemoteCall remoteCall, int i2, long j2) throws Exception {
        if (j2 != interfaceHash) {
            throw new SkeletonMismatchException("interface hash mismatch");
        }
        DGCImpl dGCImpl = (DGCImpl) remote;
        StreamRemoteCall streamRemoteCall = (StreamRemoteCall) remoteCall;
        try {
            switch (i2) {
                case 0:
                    try {
                        try {
                            ObjectInput inputStream = streamRemoteCall.getInputStream();
                            ObjID[] objIDArr = (ObjID[]) inputStream.readObject();
                            long j3 = inputStream.readLong();
                            VMID vmid = (VMID) inputStream.readObject();
                            boolean z2 = inputStream.readBoolean();
                            streamRemoteCall.releaseInputStream();
                            dGCImpl.clean(objIDArr, j3, vmid, z2);
                            try {
                                streamRemoteCall.getResultStream(true);
                                return;
                            } catch (IOException e2) {
                                throw new MarshalException("error marshalling return", e2);
                            }
                        } finally {
                        }
                    } catch (IOException | ClassCastException | ClassNotFoundException e3) {
                        streamRemoteCall.discardPendingRefs();
                        throw new UnmarshalException("error unmarshalling arguments", e3);
                    }
                case 1:
                    try {
                        ObjectInput inputStream2 = streamRemoteCall.getInputStream();
                        ObjID[] objIDArr2 = (ObjID[]) inputStream2.readObject();
                        long j4 = inputStream2.readLong();
                        Lease lease = (Lease) inputStream2.readObject();
                        streamRemoteCall.releaseInputStream();
                        try {
                            streamRemoteCall.getResultStream(true).writeObject(dGCImpl.dirty(objIDArr2, j4, lease));
                            return;
                        } catch (IOException e4) {
                            throw new MarshalException("error marshalling return", e4);
                        }
                    } catch (IOException | ClassCastException | ClassNotFoundException e5) {
                        streamRemoteCall.discardPendingRefs();
                        throw new UnmarshalException("error unmarshalling arguments", e5);
                    }
                default:
                    throw new UnmarshalException("invalid method number");
            }
        } finally {
        }
    }
}
