package java.rmi.registry;

import java.rmi.RemoteException;

@Deprecated
/* loaded from: rt.jar:java/rmi/registry/RegistryHandler.class */
public interface RegistryHandler {
    @Deprecated
    Registry registryStub(String str, int i2) throws RemoteException;

    @Deprecated
    Registry registryImpl(int i2) throws RemoteException;
}
