package sun.rmi.server;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.rmi.MarshalledObject;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationDesc;
import java.rmi.activation.ActivationException;
import java.rmi.activation.ActivationGroup;
import java.rmi.activation.ActivationGroupID;
import java.rmi.activation.ActivationID;
import java.rmi.activation.UnknownObjectException;
import java.rmi.server.RMIClassLoader;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.RMISocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import sun.rmi.registry.RegistryImpl;

/* loaded from: rt.jar:sun/rmi/server/ActivationGroupImpl.class */
public class ActivationGroupImpl extends ActivationGroup {
    private static final long serialVersionUID = 5758693559430427303L;
    private final Hashtable<ActivationID, ActiveEntry> active;
    private boolean groupInactive;
    private final ActivationGroupID groupID;
    private final List<ActivationID> lockedIDs;

    public ActivationGroupImpl(ActivationGroupID activationGroupID, MarshalledObject<?> marshalledObject) throws RemoteException {
        super(activationGroupID);
        this.active = new Hashtable<>();
        this.groupInactive = false;
        this.lockedIDs = new ArrayList();
        this.groupID = activationGroupID;
        unexportObject(this, true);
        UnicastRemoteObject.exportObject(this, 0, null, new ServerSocketFactoryImpl());
        if (System.getSecurityManager() == null) {
            try {
                System.setSecurityManager(new SecurityManager());
            } catch (Exception e2) {
                throw new RemoteException("unable to set security manager", e2);
            }
        }
    }

    /* loaded from: rt.jar:sun/rmi/server/ActivationGroupImpl$ServerSocketFactoryImpl.class */
    private static class ServerSocketFactoryImpl implements RMIServerSocketFactory {
        private ServerSocketFactoryImpl() {
        }

        @Override // java.rmi.server.RMIServerSocketFactory
        public ServerSocket createServerSocket(int i2) throws IOException {
            RMISocketFactory socketFactory = RMISocketFactory.getSocketFactory();
            if (socketFactory == null) {
                socketFactory = RMISocketFactory.getDefaultSocketFactory();
            }
            return socketFactory.createServerSocket(i2);
        }
    }

    private void acquireLock(ActivationID activationID) {
        ActivationID activationID2;
        while (true) {
            synchronized (this.lockedIDs) {
                int iIndexOf = this.lockedIDs.indexOf(activationID);
                if (iIndexOf < 0) {
                    this.lockedIDs.add(activationID);
                    return;
                }
                activationID2 = this.lockedIDs.get(iIndexOf);
            }
            synchronized (activationID2) {
                synchronized (this.lockedIDs) {
                    int iIndexOf2 = this.lockedIDs.indexOf(activationID2);
                    if (iIndexOf2 >= 0) {
                        if (this.lockedIDs.get(iIndexOf2) == activationID2) {
                            try {
                                activationID2.wait();
                            } catch (InterruptedException e2) {
                            }
                        }
                    }
                }
            }
        }
    }

    private void releaseLock(ActivationID activationID) {
        ActivationID activationIDRemove;
        synchronized (this.lockedIDs) {
            activationIDRemove = this.lockedIDs.remove(this.lockedIDs.indexOf(activationID));
        }
        synchronized (activationIDRemove) {
            activationIDRemove.notifyAll();
        }
    }

    @Override // java.rmi.activation.ActivationInstantiator
    public MarshalledObject<? extends Remote> newInstance(final ActivationID activationID, final ActivationDesc activationDesc) throws ActivationException, UnknownHostException, RemoteException {
        RegistryImpl.checkAccess("ActivationInstantiator.newInstance");
        try {
            if (!this.groupID.equals(activationDesc.getGroupID())) {
                throw new ActivationException("newInstance in wrong group");
            }
            try {
                try {
                    acquireLock(activationID);
                    synchronized (this) {
                        if (this.groupInactive) {
                            throw new InactiveGroupException("group is inactive");
                        }
                    }
                    ActiveEntry activeEntry = this.active.get(activationID);
                    if (activeEntry != null) {
                        MarshalledObject<Remote> marshalledObject = activeEntry.mobj;
                        releaseLock(activationID);
                        checkInactiveGroup();
                        return marshalledObject;
                    }
                    final Class<? extends U> clsAsSubclass = RMIClassLoader.loadClass(activationDesc.getLocation(), activationDesc.getClassName()).asSubclass(Remote.class);
                    Remote remote = null;
                    final Thread threadCurrentThread = Thread.currentThread();
                    final ClassLoader contextClassLoader = threadCurrentThread.getContextClassLoader();
                    ClassLoader classLoader = clsAsSubclass.getClassLoader();
                    final ClassLoader classLoader2 = covers(classLoader, contextClassLoader) ? classLoader : contextClassLoader;
                    try {
                        remote = (Remote) AccessController.doPrivileged(new PrivilegedExceptionAction<Remote>() { // from class: sun.rmi.server.ActivationGroupImpl.1
                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // java.security.PrivilegedExceptionAction
                            public Remote run() throws IllegalAccessException, NoSuchMethodException, InstantiationException, SecurityException, InvocationTargetException {
                                Constructor declaredConstructor = clsAsSubclass.getDeclaredConstructor(ActivationID.class, MarshalledObject.class);
                                declaredConstructor.setAccessible(true);
                                try {
                                    threadCurrentThread.setContextClassLoader(classLoader2);
                                    return (Remote) declaredConstructor.newInstance(activationID, activationDesc.getData());
                                } finally {
                                    threadCurrentThread.setContextClassLoader(contextClassLoader);
                                }
                            }
                        });
                    } catch (PrivilegedActionException e2) {
                        Throwable exception = e2.getException();
                        if (exception instanceof InstantiationException) {
                            throw ((InstantiationException) exception);
                        }
                        if (exception instanceof NoSuchMethodException) {
                            throw ((NoSuchMethodException) exception);
                        }
                        if (exception instanceof IllegalAccessException) {
                            throw ((IllegalAccessException) exception);
                        }
                        if (exception instanceof InvocationTargetException) {
                            throw ((InvocationTargetException) exception);
                        }
                        if (exception instanceof RuntimeException) {
                            throw ((RuntimeException) exception);
                        }
                        if (exception instanceof Error) {
                            throw ((Error) exception);
                        }
                    }
                    ActiveEntry activeEntry2 = new ActiveEntry(remote);
                    this.active.put(activationID, activeEntry2);
                    MarshalledObject<Remote> marshalledObject2 = activeEntry2.mobj;
                    releaseLock(activationID);
                    checkInactiveGroup();
                    return marshalledObject2;
                } catch (InvocationTargetException e3) {
                    throw new ActivationException("exception in object constructor", e3.getTargetException());
                } catch (Exception e4) {
                    throw new ActivationException("unable to activate object", e4);
                }
            } catch (NoSuchMethodError | NoSuchMethodException e5) {
                throw new ActivationException("Activatable object must provide an activation constructor", e5);
            }
        } catch (Throwable th) {
            releaseLock(activationID);
            checkInactiveGroup();
            throw th;
        }
    }

    @Override // java.rmi.activation.ActivationGroup
    public boolean inactiveObject(ActivationID activationID) throws ActivationException, RemoteException {
        try {
            acquireLock(activationID);
            synchronized (this) {
                if (this.groupInactive) {
                    throw new ActivationException("group is inactive");
                }
            }
            ActiveEntry activeEntry = this.active.get(activationID);
            if (activeEntry == null) {
                throw new UnknownObjectException("object not active");
            }
            if (!Activatable.unexportObject(activeEntry.impl, false)) {
                return false;
            }
            try {
                super.inactiveObject(activationID);
            } catch (UnknownObjectException e2) {
            }
            this.active.remove(activationID);
            releaseLock(activationID);
            checkInactiveGroup();
            return true;
        } finally {
            releaseLock(activationID);
            checkInactiveGroup();
        }
    }

    private void checkInactiveGroup() {
        boolean z2 = false;
        synchronized (this) {
            if (this.active.size() == 0 && this.lockedIDs.size() == 0 && !this.groupInactive) {
                this.groupInactive = true;
                z2 = true;
            }
        }
        if (z2) {
            try {
                super.inactiveGroup();
            } catch (Exception e2) {
            }
            try {
                UnicastRemoteObject.unexportObject(this, true);
            } catch (NoSuchObjectException e3) {
            }
        }
    }

    @Override // java.rmi.activation.ActivationGroup
    public void activeObject(ActivationID activationID, Remote remote) throws ActivationException, RemoteException {
        try {
            acquireLock(activationID);
            synchronized (this) {
                if (this.groupInactive) {
                    throw new ActivationException("group is inactive");
                }
            }
            if (!this.active.contains(activationID)) {
                ActiveEntry activeEntry = new ActiveEntry(remote);
                this.active.put(activationID, activeEntry);
                try {
                    super.activeObject(activationID, activeEntry.mobj);
                } catch (RemoteException e2) {
                }
            }
        } finally {
            releaseLock(activationID);
            checkInactiveGroup();
        }
    }

    /* loaded from: rt.jar:sun/rmi/server/ActivationGroupImpl$ActiveEntry.class */
    private static class ActiveEntry {
        Remote impl;
        MarshalledObject<Remote> mobj;

        ActiveEntry(Remote remote) throws ActivationException {
            this.impl = remote;
            try {
                this.mobj = new MarshalledObject<>(remote);
            } catch (IOException e2) {
                throw new ActivationException("failed to marshal remote object", e2);
            }
        }
    }

    private static boolean covers(ClassLoader classLoader, ClassLoader classLoader2) {
        if (classLoader2 == null) {
            return true;
        }
        if (classLoader == null) {
            return false;
        }
        while (classLoader != classLoader2) {
            classLoader = classLoader.getParent();
            if (classLoader == null) {
                return false;
            }
        }
        return true;
    }
}
