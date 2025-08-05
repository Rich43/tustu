package sun.rmi.registry;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.rmi.MarshalException;
import java.rmi.Remote;
import java.rmi.UnmarshalException;
import java.rmi.server.Operation;
import java.rmi.server.RemoteCall;
import java.rmi.server.Skeleton;
import java.rmi.server.SkeletonMismatchException;
import sun.misc.SharedSecrets;
import sun.rmi.transport.StreamRemoteCall;

/* loaded from: rt.jar:sun/rmi/registry/RegistryImpl_Skel.class */
public final class RegistryImpl_Skel implements Skeleton {
    private static final Operation[] operations = {new Operation("void bind(java.lang.String, java.rmi.Remote)"), new Operation("java.lang.String list()[]"), new Operation("java.rmi.Remote lookup(java.lang.String)"), new Operation("void rebind(java.lang.String, java.rmi.Remote)"), new Operation("void unbind(java.lang.String)")};
    private static final long interfaceHash = 4905912898345647071L;

    @Override // java.rmi.server.Skeleton
    public Operation[] getOperations() {
        return (Operation[]) operations.clone();
    }

    @Override // java.rmi.server.Skeleton
    public void dispatch(Remote remote, RemoteCall remoteCall, int i2, long j2) throws Exception {
        if (i2 < 0) {
            if (j2 == 7583982177005850366L) {
                i2 = 0;
            } else if (j2 == 2571371476350237748L) {
                i2 = 1;
            } else if (j2 == -7538657168040752697L) {
                i2 = 2;
            } else if (j2 == -8381844669958460146L) {
                i2 = 3;
            } else if (j2 == 7305022919901907578L) {
                i2 = 4;
            } else {
                throw new UnmarshalException("invalid method hash");
            }
        } else if (j2 != interfaceHash) {
            throw new SkeletonMismatchException("interface hash mismatch");
        }
        RegistryImpl registryImpl = (RegistryImpl) remote;
        StreamRemoteCall streamRemoteCall = (StreamRemoteCall) remoteCall;
        try {
            switch (i2) {
                case 0:
                    RegistryImpl.checkAccess("Registry.bind");
                    try {
                        try {
                            ObjectInputStream objectInputStream = (ObjectInputStream) streamRemoteCall.getInputStream();
                            String string = SharedSecrets.getJavaObjectInputStreamReadString().readString(objectInputStream);
                            Remote remote2 = (Remote) objectInputStream.readObject();
                            streamRemoteCall.releaseInputStream();
                            registryImpl.bind(string, remote2);
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
                    streamRemoteCall.releaseInputStream();
                    try {
                        streamRemoteCall.getResultStream(true).writeObject(registryImpl.list());
                        return;
                    } catch (IOException e4) {
                        throw new MarshalException("error marshalling return", e4);
                    }
                case 2:
                    try {
                        String string2 = SharedSecrets.getJavaObjectInputStreamReadString().readString((ObjectInputStream) streamRemoteCall.getInputStream());
                        streamRemoteCall.releaseInputStream();
                        try {
                            streamRemoteCall.getResultStream(true).writeObject(registryImpl.lookup(string2));
                            return;
                        } catch (IOException e5) {
                            throw new MarshalException("error marshalling return", e5);
                        }
                    } catch (IOException | ClassCastException e6) {
                        streamRemoteCall.discardPendingRefs();
                        throw new UnmarshalException("error unmarshalling arguments", e6);
                    }
                case 3:
                    RegistryImpl.checkAccess("Registry.rebind");
                    try {
                        try {
                            ObjectInputStream objectInputStream2 = (ObjectInputStream) streamRemoteCall.getInputStream();
                            String string3 = SharedSecrets.getJavaObjectInputStreamReadString().readString(objectInputStream2);
                            Remote remote3 = (Remote) objectInputStream2.readObject();
                            streamRemoteCall.releaseInputStream();
                            registryImpl.rebind(string3, remote3);
                            try {
                                streamRemoteCall.getResultStream(true);
                                return;
                            } catch (IOException e7) {
                                throw new MarshalException("error marshalling return", e7);
                            }
                        } finally {
                        }
                    } catch (IOException | ClassCastException | ClassNotFoundException e8) {
                        streamRemoteCall.discardPendingRefs();
                        throw new UnmarshalException("error unmarshalling arguments", e8);
                    }
                case 4:
                    RegistryImpl.checkAccess("Registry.unbind");
                    try {
                        try {
                            String string4 = SharedSecrets.getJavaObjectInputStreamReadString().readString((ObjectInputStream) streamRemoteCall.getInputStream());
                            streamRemoteCall.releaseInputStream();
                            registryImpl.unbind(string4);
                            try {
                                streamRemoteCall.getResultStream(true);
                                return;
                            } catch (IOException e9) {
                                throw new MarshalException("error marshalling return", e9);
                            }
                        } finally {
                            streamRemoteCall.releaseInputStream();
                        }
                    } catch (IOException | ClassCastException e10) {
                        streamRemoteCall.discardPendingRefs();
                        throw new UnmarshalException("error unmarshalling arguments", e10);
                    }
                default:
                    throw new UnmarshalException("invalid method number");
            }
        } finally {
            streamRemoteCall.releaseInputStream();
        }
        streamRemoteCall.releaseInputStream();
    }
}
