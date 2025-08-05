package org.omg.PortableServer;

/* loaded from: rt.jar:org/omg/PortableServer/ServantActivatorOperations.class */
public interface ServantActivatorOperations extends ServantManagerOperations {
    Servant incarnate(byte[] bArr, POA poa) throws ForwardRequest;

    void etherealize(byte[] bArr, POA poa, Servant servant, boolean z2, boolean z3);
}
