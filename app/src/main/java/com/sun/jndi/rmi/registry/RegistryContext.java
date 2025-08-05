package com.sun.jndi.rmi.registry;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.ConnectException;
import java.rmi.ConnectIOException;
import java.rmi.MarshalException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.ServerException;
import java.rmi.StubNotFoundException;
import java.rmi.UnknownHostException;
import java.rmi.UnmarshalException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.SocketSecurityException;
import java.security.AccessController;
import java.util.Hashtable;
import javax.naming.Binding;
import javax.naming.CommunicationException;
import javax.naming.CompositeName;
import javax.naming.ConfigurationException;
import javax.naming.Context;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameClassPair;
import javax.naming.NameNotFoundException;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.NoPermissionException;
import javax.naming.OperationNotSupportedException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.ServiceUnavailableException;
import javax.naming.StringRefAddr;
import javax.naming.spi.NamingManager;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/jndi/rmi/registry/RegistryContext.class */
public class RegistryContext implements Context, Referenceable {
    private Hashtable<String, Object> environment;
    private Registry registry;
    private String host;
    private int port;
    private static final String SOCKET_FACTORY = "com.sun.jndi.rmi.factory.socket";
    Reference reference;
    public static final String SECURITY_MGR = "java.naming.rmi.security.manager";
    private static final NameParser nameParser = new AtomicNameParser();
    static final boolean trustURLCodebase = "true".equalsIgnoreCase((String) AccessController.doPrivileged(() -> {
        return System.getProperty("com.sun.jndi.rmi.object.trustURLCodebase", "false");
    }));

    public RegistryContext(String str, int i2, Hashtable<?, ?> hashtable) throws NamingException {
        this.reference = null;
        this.environment = hashtable == null ? new Hashtable(5) : hashtable;
        if (this.environment.get(SECURITY_MGR) != null) {
            installSecurityMgr();
        }
        if (str != null && str.charAt(0) == '[') {
            str = str.substring(1, str.length() - 1);
        }
        this.registry = getRegistry(str, i2, (RMIClientSocketFactory) this.environment.get(SOCKET_FACTORY));
        this.host = str;
        this.port = i2;
    }

    RegistryContext(RegistryContext registryContext) {
        this.reference = null;
        this.environment = (Hashtable) registryContext.environment.clone();
        this.registry = registryContext.registry;
        this.host = registryContext.host;
        this.port = registryContext.port;
        this.reference = registryContext.reference;
    }

    protected void finalize() {
        close();
    }

    @Override // javax.naming.Context
    public Object lookup(Name name) throws NamingException {
        if (name.isEmpty()) {
            return new RegistryContext(this);
        }
        try {
            return decodeObject(this.registry.lookup(name.get(0)), name.getPrefix(1));
        } catch (NotBoundException e2) {
            throw new NameNotFoundException(name.get(0));
        } catch (RemoteException e3) {
            throw ((NamingException) wrapRemoteException(e3).fillInStackTrace());
        }
    }

    @Override // javax.naming.Context
    public Object lookup(String str) throws NamingException {
        return lookup(new CompositeName(str));
    }

    @Override // javax.naming.Context
    public void bind(Name name, Object obj) throws NamingException {
        if (name.isEmpty()) {
            throw new InvalidNameException("RegistryContext: Cannot bind empty name");
        }
        try {
            this.registry.bind(name.get(0), encodeObject(obj, name.getPrefix(1)));
        } catch (AlreadyBoundException e2) {
            NameAlreadyBoundException nameAlreadyBoundException = new NameAlreadyBoundException(name.get(0));
            nameAlreadyBoundException.setRootCause(e2);
            throw nameAlreadyBoundException;
        } catch (RemoteException e3) {
            throw ((NamingException) wrapRemoteException(e3).fillInStackTrace());
        }
    }

    @Override // javax.naming.Context
    public void bind(String str, Object obj) throws NamingException {
        bind(new CompositeName(str), obj);
    }

    @Override // javax.naming.Context
    public void rebind(Name name, Object obj) throws NamingException {
        if (name.isEmpty()) {
            throw new InvalidNameException("RegistryContext: Cannot rebind empty name");
        }
        try {
            this.registry.rebind(name.get(0), encodeObject(obj, name.getPrefix(1)));
        } catch (RemoteException e2) {
            throw ((NamingException) wrapRemoteException(e2).fillInStackTrace());
        }
    }

    @Override // javax.naming.Context
    public void rebind(String str, Object obj) throws NamingException {
        rebind(new CompositeName(str), obj);
    }

    @Override // javax.naming.Context
    public void unbind(Name name) throws NamingException {
        if (name.isEmpty()) {
            throw new InvalidNameException("RegistryContext: Cannot unbind empty name");
        }
        try {
            this.registry.unbind(name.get(0));
        } catch (NotBoundException e2) {
        } catch (RemoteException e3) {
            throw ((NamingException) wrapRemoteException(e3).fillInStackTrace());
        }
    }

    @Override // javax.naming.Context
    public void unbind(String str) throws NamingException {
        unbind(new CompositeName(str));
    }

    @Override // javax.naming.Context
    public void rename(Name name, Name name2) throws NamingException {
        bind(name2, lookup(name));
        unbind(name);
    }

    @Override // javax.naming.Context
    public void rename(String str, String str2) throws NamingException {
        rename(new CompositeName(str), new CompositeName(str2));
    }

    @Override // javax.naming.Context
    public NamingEnumeration<NameClassPair> list(Name name) throws NamingException {
        if (!name.isEmpty()) {
            throw new InvalidNameException("RegistryContext: can only list \"\"");
        }
        try {
            return new NameClassPairEnumeration(this.registry.list());
        } catch (RemoteException e2) {
            throw ((NamingException) wrapRemoteException(e2).fillInStackTrace());
        }
    }

    @Override // javax.naming.Context
    public NamingEnumeration<NameClassPair> list(String str) throws NamingException {
        return list(new CompositeName(str));
    }

    @Override // javax.naming.Context
    public NamingEnumeration<Binding> listBindings(Name name) throws NamingException {
        if (!name.isEmpty()) {
            throw new InvalidNameException("RegistryContext: can only list \"\"");
        }
        try {
            return new BindingEnumeration(this, this.registry.list());
        } catch (RemoteException e2) {
            throw ((NamingException) wrapRemoteException(e2).fillInStackTrace());
        }
    }

    @Override // javax.naming.Context
    public NamingEnumeration<Binding> listBindings(String str) throws NamingException {
        return listBindings(new CompositeName(str));
    }

    @Override // javax.naming.Context
    public void destroySubcontext(Name name) throws NamingException {
        throw new OperationNotSupportedException();
    }

    @Override // javax.naming.Context
    public void destroySubcontext(String str) throws NamingException {
        throw new OperationNotSupportedException();
    }

    @Override // javax.naming.Context
    public Context createSubcontext(Name name) throws NamingException {
        throw new OperationNotSupportedException();
    }

    @Override // javax.naming.Context
    public Context createSubcontext(String str) throws NamingException {
        throw new OperationNotSupportedException();
    }

    @Override // javax.naming.Context
    public Object lookupLink(Name name) throws NamingException {
        return lookup(name);
    }

    @Override // javax.naming.Context
    public Object lookupLink(String str) throws NamingException {
        return lookup(str);
    }

    @Override // javax.naming.Context
    public NameParser getNameParser(Name name) throws NamingException {
        return nameParser;
    }

    @Override // javax.naming.Context
    public NameParser getNameParser(String str) throws NamingException {
        return nameParser;
    }

    @Override // javax.naming.Context
    public Name composeName(Name name, Name name2) throws NamingException {
        return ((Name) name2.clone()).addAll(name);
    }

    @Override // javax.naming.Context
    public String composeName(String str, String str2) throws NamingException {
        return composeName(new CompositeName(str), new CompositeName(str2)).toString();
    }

    @Override // javax.naming.Context
    public Object removeFromEnvironment(String str) throws NamingException {
        return this.environment.remove(str);
    }

    @Override // javax.naming.Context
    public Object addToEnvironment(String str, Object obj) throws NamingException {
        if (str.equals(SECURITY_MGR)) {
            installSecurityMgr();
        }
        return this.environment.put(str, obj);
    }

    @Override // javax.naming.Context
    public Hashtable<String, Object> getEnvironment() throws NamingException {
        return (Hashtable) this.environment.clone();
    }

    @Override // javax.naming.Context
    public void close() {
        this.environment = null;
        this.registry = null;
    }

    @Override // javax.naming.Context
    public String getNameInNamespace() {
        return "";
    }

    @Override // javax.naming.Referenceable
    public Reference getReference() throws NamingException {
        if (this.reference != null) {
            return (Reference) this.reference.clone();
        }
        if (this.host == null || this.host.equals("localhost")) {
            throw new ConfigurationException("Cannot create a reference for an RMI registry whose host was unspecified or specified as \"localhost\"");
        }
        String str = this.host.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) > -1 ? "rmi://[" + this.host + "]" : "rmi://" + this.host;
        if (this.port > 0) {
            str = str + CallSiteDescriptor.TOKEN_DELIMITER + Integer.toString(this.port);
        }
        return new Reference(RegistryContext.class.getName(), new StringRefAddr("URL", str), RegistryContextFactory.class.getName(), null);
    }

    public static NamingException wrapRemoteException(RemoteException remoteException) {
        NamingException configurationException;
        if (remoteException instanceof ConnectException) {
            configurationException = new ServiceUnavailableException();
        } else if (remoteException instanceof AccessException) {
            configurationException = new NoPermissionException();
        } else if ((remoteException instanceof StubNotFoundException) || (remoteException instanceof UnknownHostException) || (remoteException instanceof SocketSecurityException)) {
            configurationException = new ConfigurationException();
        } else if ((remoteException instanceof ExportException) || (remoteException instanceof ConnectIOException) || (remoteException instanceof MarshalException) || (remoteException instanceof UnmarshalException) || (remoteException instanceof NoSuchObjectException)) {
            configurationException = new CommunicationException();
        } else if ((remoteException instanceof ServerException) && (remoteException.detail instanceof RemoteException)) {
            configurationException = wrapRemoteException((RemoteException) remoteException.detail);
        } else {
            configurationException = new NamingException();
        }
        configurationException.setRootCause(remoteException);
        return configurationException;
    }

    private static Registry getRegistry(String str, int i2, RMIClientSocketFactory rMIClientSocketFactory) throws NamingException {
        try {
            if (rMIClientSocketFactory == null) {
                return LocateRegistry.getRegistry(str, i2);
            }
            return LocateRegistry.getRegistry(str, i2, rMIClientSocketFactory);
        } catch (RemoteException e2) {
            throw ((NamingException) wrapRemoteException(e2).fillInStackTrace());
        }
    }

    private static void installSecurityMgr() {
        try {
            System.setSecurityManager(new RMISecurityManager());
        } catch (Exception e2) {
        }
    }

    private Remote encodeObject(Object obj, Name name) throws NamingException, RemoteException {
        Object stateToBind = NamingManager.getStateToBind(obj, name, this, this.environment);
        if (stateToBind instanceof Remote) {
            return (Remote) stateToBind;
        }
        if (stateToBind instanceof Reference) {
            return new ReferenceWrapper((Reference) stateToBind);
        }
        if (stateToBind instanceof Referenceable) {
            return new ReferenceWrapper(((Referenceable) stateToBind).getReference());
        }
        throw new IllegalArgumentException("RegistryContext: object to bind must be Remote, Reference, or Referenceable");
    }

    private Object decodeObject(Remote remote, Name name) throws NamingException {
        try {
            Object reference = remote instanceof RemoteReference ? ((RemoteReference) remote).getReference() : remote;
            Reference reference2 = null;
            if (reference instanceof Reference) {
                reference2 = (Reference) reference;
            } else if (reference instanceof Referenceable) {
                reference2 = ((Referenceable) reference).getReference();
            }
            if (reference2 != null && reference2.getFactoryClassLocation() != null && !trustURLCodebase) {
                throw new ConfigurationException("The object factory is untrusted. Set the system property 'com.sun.jndi.rmi.object.trustURLCodebase' to 'true'.");
            }
            return NamingManager.getObjectInstance(reference, name, this, this.environment);
        } catch (RemoteException e2) {
            throw ((NamingException) wrapRemoteException(e2).fillInStackTrace());
        } catch (NamingException e3) {
            throw e3;
        } catch (Exception e4) {
            NamingException namingException = new NamingException();
            namingException.setRootCause(e4);
            throw namingException;
        }
    }
}
