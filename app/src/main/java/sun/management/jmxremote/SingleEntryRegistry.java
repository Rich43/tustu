package sun.management.jmxremote;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import sun.misc.ObjectInputFilter;
import sun.rmi.registry.RegistryImpl;

/* loaded from: rt.jar:sun/management/jmxremote/SingleEntryRegistry.class */
public class SingleEntryRegistry extends RegistryImpl {
    private final String name;
    private final Remote object;
    private static final long serialVersionUID = -4897238949499730950L;

    SingleEntryRegistry(int i2, String str, Remote remote) throws RemoteException {
        super(i2, null, null, SingleEntryRegistry::singleRegistryFilter);
        this.name = str;
        this.object = remote;
    }

    SingleEntryRegistry(int i2, RMIClientSocketFactory rMIClientSocketFactory, RMIServerSocketFactory rMIServerSocketFactory, String str, Remote remote) throws RemoteException {
        super(i2, rMIClientSocketFactory, rMIServerSocketFactory, SingleEntryRegistry::singleRegistryFilter);
        this.name = str;
        this.object = remote;
    }

    @Override // sun.rmi.registry.RegistryImpl, java.rmi.registry.Registry
    public String[] list() {
        return new String[]{this.name};
    }

    @Override // sun.rmi.registry.RegistryImpl, java.rmi.registry.Registry
    public Remote lookup(String str) throws NotBoundException {
        if (str.equals(this.name)) {
            return this.object;
        }
        throw new NotBoundException("Not bound: \"" + str + "\" (only bound name is \"" + this.name + "\")");
    }

    @Override // sun.rmi.registry.RegistryImpl, java.rmi.registry.Registry
    public void bind(String str, Remote remote) throws AccessException {
        throw new AccessException("Cannot modify this registry");
    }

    @Override // sun.rmi.registry.RegistryImpl, java.rmi.registry.Registry
    public void rebind(String str, Remote remote) throws AccessException {
        throw new AccessException("Cannot modify this registry");
    }

    @Override // sun.rmi.registry.RegistryImpl, java.rmi.registry.Registry
    public void unbind(String str) throws AccessException {
        throw new AccessException("Cannot modify this registry");
    }

    private static ObjectInputFilter.Status singleRegistryFilter(ObjectInputFilter.FilterInfo filterInfo) {
        return (filterInfo.serialClass() != null || filterInfo.depth() > 2 || filterInfo.references() > 4 || filterInfo.arrayLength() >= 0) ? ObjectInputFilter.Status.REJECTED : ObjectInputFilter.Status.ALLOWED;
    }
}
