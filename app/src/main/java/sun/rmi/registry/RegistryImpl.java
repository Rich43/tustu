package sun.rmi.registry;

import java.io.FilePermission;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketPermission;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.activation.ActivationID;
import java.rmi.registry.Registry;
import java.rmi.server.ObjID;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UID;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.ProtectionDomain;
import java.security.Security;
import java.security.cert.Certificate;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import sun.misc.ObjectInputFilter;
import sun.misc.URLClassPath;
import sun.rmi.runtime.Log;
import sun.rmi.server.LoaderHandler;
import sun.rmi.server.UnicastRef;
import sun.rmi.server.UnicastServerRef;
import sun.rmi.server.UnicastServerRef2;
import sun.rmi.transport.LiveRef;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:sun/rmi/registry/RegistryImpl.class */
public class RegistryImpl extends RemoteServer implements Registry {
    private static final long serialVersionUID = 4666870661827494597L;
    private Hashtable<String, Remote> bindings;
    private static RegistryImpl registry;
    private static final String REGISTRY_FILTER_PROPNAME = "sun.rmi.registry.registryFilter";
    private static final int REGISTRY_MAX_DEPTH = 20;
    private static final int REGISTRY_MAX_ARRAY_SIZE = 1000000;
    private static Hashtable<InetAddress, InetAddress> allowedAccessCache = new Hashtable<>(3);
    private static ObjID id = new ObjID(0);
    private static ResourceBundle resources = null;
    private static final ObjectInputFilter registryFilter = (ObjectInputFilter) AccessController.doPrivileged(RegistryImpl::initRegistryFilter);

    private static ObjectInputFilter initRegistryFilter() {
        ObjectInputFilter objectInputFilterCreateFilter2 = null;
        String property = System.getProperty(REGISTRY_FILTER_PROPNAME);
        if (property == null) {
            property = Security.getProperty(REGISTRY_FILTER_PROPNAME);
        }
        if (property != null) {
            objectInputFilterCreateFilter2 = ObjectInputFilter.Config.createFilter2(property);
            Log log = Log.getLog("sun.rmi.registry", "registry", -1);
            if (log.isLoggable(Log.BRIEF)) {
                log.log(Log.BRIEF, "registryFilter = " + ((Object) objectInputFilterCreateFilter2));
            }
        }
        return objectInputFilterCreateFilter2;
    }

    public RegistryImpl(int i2, RMIClientSocketFactory rMIClientSocketFactory, RMIServerSocketFactory rMIServerSocketFactory) throws RemoteException {
        this(i2, rMIClientSocketFactory, rMIServerSocketFactory, RegistryImpl::registryFilter);
    }

    public RegistryImpl(final int i2, final RMIClientSocketFactory rMIClientSocketFactory, final RMIServerSocketFactory rMIServerSocketFactory, final ObjectInputFilter objectInputFilter) throws RemoteException {
        this.bindings = new Hashtable<>(101);
        if (i2 == 1099 && System.getSecurityManager() != null) {
            try {
                AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() { // from class: sun.rmi.registry.RegistryImpl.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public Void run() throws RemoteException {
                        RegistryImpl.this.setup(new UnicastServerRef2(new LiveRef(RegistryImpl.id, i2, rMIClientSocketFactory, rMIServerSocketFactory), objectInputFilter));
                        return null;
                    }
                }, (AccessControlContext) null, new SocketPermission("localhost:" + i2, "listen,accept"));
                return;
            } catch (PrivilegedActionException e2) {
                throw ((RemoteException) e2.getException());
            }
        }
        setup(new UnicastServerRef2(new LiveRef(id, i2, rMIClientSocketFactory, rMIServerSocketFactory), objectInputFilter));
    }

    public RegistryImpl(final int i2) throws RemoteException {
        this.bindings = new Hashtable<>(101);
        if (i2 == 1099 && System.getSecurityManager() != null) {
            try {
                AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() { // from class: sun.rmi.registry.RegistryImpl.2
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public Void run() throws RemoteException {
                        RegistryImpl.this.setup(new UnicastServerRef(new LiveRef(RegistryImpl.id, i2), filterInfo -> {
                            return RegistryImpl.registryFilter(filterInfo);
                        }));
                        return null;
                    }
                }, (AccessControlContext) null, new SocketPermission("localhost:" + i2, "listen,accept"));
                return;
            } catch (PrivilegedActionException e2) {
                throw ((RemoteException) e2.getException());
            }
        }
        setup(new UnicastServerRef(new LiveRef(id, i2), RegistryImpl::registryFilter));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setup(UnicastServerRef unicastServerRef) throws RemoteException {
        this.ref = unicastServerRef;
        unicastServerRef.exportObject(this, null, true);
    }

    public Remote lookup(String str) throws NotBoundException, RemoteException {
        Remote remote;
        synchronized (this.bindings) {
            remote = this.bindings.get(str);
            if (remote == null) {
                throw new NotBoundException(str);
            }
        }
        return remote;
    }

    public void bind(String str, Remote remote) throws AlreadyBoundException, RemoteException {
        synchronized (this.bindings) {
            if (this.bindings.get(str) != null) {
                throw new AlreadyBoundException(str);
            }
            this.bindings.put(str, remote);
        }
    }

    public void unbind(String str) throws NotBoundException, RemoteException {
        synchronized (this.bindings) {
            if (this.bindings.get(str) == null) {
                throw new NotBoundException(str);
            }
            this.bindings.remove(str);
        }
    }

    public void rebind(String str, Remote remote) throws RemoteException {
        this.bindings.put(str, remote);
    }

    public String[] list() throws RemoteException {
        String[] strArr;
        synchronized (this.bindings) {
            int size = this.bindings.size();
            strArr = new String[size];
            Enumeration<String> enumerationKeys = this.bindings.keys();
            while (true) {
                size--;
                if (size >= 0) {
                    strArr[size] = enumerationKeys.nextElement2();
                }
            }
        }
        return strArr;
    }

    public static void checkAccess(String str) throws AccessException, UnknownHostException {
        try {
            final String clientHost = getClientHost();
            try {
                final InetAddress inetAddress = (InetAddress) AccessController.doPrivileged(new PrivilegedExceptionAction<InetAddress>() { // from class: sun.rmi.registry.RegistryImpl.3
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public InetAddress run() throws UnknownHostException {
                        return InetAddress.getByName(clientHost);
                    }
                });
                if (allowedAccessCache.get(inetAddress) == null) {
                    if (inetAddress.isAnyLocalAddress()) {
                        throw new AccessException(str + " disallowed; origin unknown");
                    }
                    try {
                        AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() { // from class: sun.rmi.registry.RegistryImpl.4
                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // java.security.PrivilegedExceptionAction
                            public Void run() throws IOException {
                                new ServerSocket(0, 10, inetAddress).close();
                                RegistryImpl.allowedAccessCache.put(inetAddress, inetAddress);
                                return null;
                            }
                        });
                    } catch (PrivilegedActionException e2) {
                        throw new AccessException(str + " disallowed; origin " + ((Object) inetAddress) + " is non-local host");
                    }
                }
            } catch (PrivilegedActionException e3) {
                throw ((UnknownHostException) e3.getException());
            }
        } catch (UnknownHostException e4) {
            throw new AccessException(str + " disallowed; origin is unknown host");
        } catch (ServerNotActiveException e5) {
        }
    }

    public static ObjID getID() {
        return id;
    }

    private static String getTextResource(String str) {
        if (resources == null) {
            try {
                resources = ResourceBundle.getBundle("sun.rmi.registry.resources.rmiregistry");
            } catch (MissingResourceException e2) {
            }
            if (resources == null) {
                return "[missing resource file: " + str + "]";
            }
        }
        String string = null;
        try {
            string = resources.getString(str);
        } catch (MissingResourceException e3) {
        }
        if (string == null) {
            return "[missing resource: " + str + "]";
        }
        return string;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ObjectInputFilter.Status registryFilter(ObjectInputFilter.FilterInfo filterInfo) {
        ObjectInputFilter.Status statusCheckInput;
        if (registryFilter != null && (statusCheckInput = registryFilter.checkInput(filterInfo)) != ObjectInputFilter.Status.UNDECIDED) {
            return statusCheckInput;
        }
        if (filterInfo.depth() > 20) {
            return ObjectInputFilter.Status.REJECTED;
        }
        Class<?> clsSerialClass = filterInfo.serialClass();
        if (clsSerialClass != null) {
            if (clsSerialClass.isArray()) {
                return (filterInfo.arrayLength() < 0 || filterInfo.arrayLength() <= 1000000) ? ObjectInputFilter.Status.UNDECIDED : ObjectInputFilter.Status.REJECTED;
            }
            if (String.class == clsSerialClass || Number.class.isAssignableFrom(clsSerialClass) || Remote.class.isAssignableFrom(clsSerialClass) || Proxy.class.isAssignableFrom(clsSerialClass) || UnicastRef.class.isAssignableFrom(clsSerialClass) || RMIClientSocketFactory.class.isAssignableFrom(clsSerialClass) || RMIServerSocketFactory.class.isAssignableFrom(clsSerialClass) || ActivationID.class.isAssignableFrom(clsSerialClass) || UID.class.isAssignableFrom(clsSerialClass)) {
                return ObjectInputFilter.Status.ALLOWED;
            }
            return ObjectInputFilter.Status.REJECTED;
        }
        return ObjectInputFilter.Status.UNDECIDED;
    }

    public static void main(String[] strArr) throws RemoteException {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
        try {
            String property = System.getProperty("env.class.path");
            if (property == null) {
                property = ".";
            }
            URLClassLoader uRLClassLoader = new URLClassLoader(URLClassPath.pathToURLs(property));
            LoaderHandler.registerCodebaseLoader(uRLClassLoader);
            Thread.currentThread().setContextClassLoader(uRLClassLoader);
            final int i2 = strArr.length >= 1 ? Integer.parseInt(strArr[0]) : Registry.REGISTRY_PORT;
            try {
                registry = (RegistryImpl) AccessController.doPrivileged(new PrivilegedExceptionAction<RegistryImpl>() { // from class: sun.rmi.registry.RegistryImpl.5
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public RegistryImpl run() throws RemoteException {
                        return new RegistryImpl(i2);
                    }
                }, getAccessControlContext(i2));
                while (true) {
                    try {
                        Thread.sleep(Long.MAX_VALUE);
                    } catch (InterruptedException e2) {
                    }
                }
            } catch (PrivilegedActionException e3) {
                throw ((RemoteException) e3.getException());
            }
        } catch (NumberFormatException e4) {
            System.err.println(MessageFormat.format(getTextResource("rmiregistry.port.badnumber"), strArr[0]));
            System.err.println(MessageFormat.format(getTextResource("rmiregistry.usage"), "rmiregistry"));
            System.exit(1);
        } catch (Exception e5) {
            e5.printStackTrace();
            System.exit(1);
        }
    }

    private static AccessControlContext getAccessControlContext(int i2) {
        PermissionCollection permissionCollection = (PermissionCollection) AccessController.doPrivileged(new PrivilegedAction<PermissionCollection>() { // from class: sun.rmi.registry.RegistryImpl.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public PermissionCollection run2() {
                CodeSource codeSource = new CodeSource((URL) null, (Certificate[]) null);
                Policy policy = Policy.getPolicy();
                if (policy != null) {
                    return policy.getPermissions(codeSource);
                }
                return new Permissions();
            }
        });
        permissionCollection.add(new SocketPermission("*", SecurityConstants.SOCKET_CONNECT_ACCEPT_ACTION));
        permissionCollection.add(new SocketPermission("localhost:" + i2, "listen,accept"));
        permissionCollection.add(new RuntimePermission("accessClassInPackage.sun.jvmstat.*"));
        permissionCollection.add(new RuntimePermission("accessClassInPackage.sun.jvm.hotspot.*"));
        permissionCollection.add(new FilePermission("<<ALL FILES>>", "read"));
        return new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(new CodeSource((URL) null, (Certificate[]) null), permissionCollection)});
    }
}
