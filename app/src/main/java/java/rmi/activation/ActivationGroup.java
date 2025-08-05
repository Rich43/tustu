package java.rmi.activation;

import java.lang.reflect.InvocationTargetException;
import java.rmi.MarshalledObject;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RMIClassLoader;
import java.rmi.server.UnicastRemoteObject;
import java.security.AccessController;
import sun.rmi.server.ActivationGroupImpl;
import sun.security.action.GetIntegerAction;

/* loaded from: rt.jar:java/rmi/activation/ActivationGroup.class */
public abstract class ActivationGroup extends UnicastRemoteObject implements ActivationInstantiator {
    private ActivationGroupID groupID;
    private ActivationMonitor monitor;
    private long incarnation;
    private static ActivationGroup currGroup;
    private static ActivationGroupID currGroupID;
    private static ActivationSystem currSystem;
    private static boolean canCreate = true;
    private static final long serialVersionUID = -7696947875314805420L;

    public abstract void activeObject(ActivationID activationID, Remote remote) throws ActivationException, RemoteException;

    protected ActivationGroup(ActivationGroupID activationGroupID) throws RemoteException {
        this.groupID = activationGroupID;
    }

    public boolean inactiveObject(ActivationID activationID) throws ActivationException, RemoteException {
        getMonitor().inactiveObject(activationID);
        return true;
    }

    public static synchronized ActivationGroup createGroup(ActivationGroupID activationGroupID, ActivationGroupDesc activationGroupDesc, long j2) throws ActivationException {
        Class clsAsSubclass;
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkSetFactory();
        }
        if (currGroup != null) {
            throw new ActivationException("group already exists");
        }
        if (!canCreate) {
            throw new ActivationException("group deactivated and cannot be recreated");
        }
        try {
            String className = activationGroupDesc.getClassName();
            if (className == null || className.equals(ActivationGroupImpl.class.getName())) {
                clsAsSubclass = ActivationGroupImpl.class;
            } else {
                try {
                    Class<?> clsLoadClass = RMIClassLoader.loadClass(activationGroupDesc.getLocation(), className);
                    if (ActivationGroup.class.isAssignableFrom(clsLoadClass)) {
                        clsAsSubclass = clsLoadClass.asSubclass(ActivationGroup.class);
                    } else {
                        throw new ActivationException("group not correct class: " + clsLoadClass.getName());
                    }
                } catch (Exception e2) {
                    throw new ActivationException("Could not load group implementation class", e2);
                }
            }
            ActivationGroup activationGroup = (ActivationGroup) clsAsSubclass.getConstructor(ActivationGroupID.class, MarshalledObject.class).newInstance(activationGroupID, activationGroupDesc.getData());
            currSystem = activationGroupID.getSystem();
            activationGroup.incarnation = j2;
            activationGroup.monitor = currSystem.activeGroup(activationGroupID, activationGroup, j2);
            currGroup = activationGroup;
            currGroupID = activationGroupID;
            canCreate = false;
            return currGroup;
        } catch (InvocationTargetException e3) {
            e3.getTargetException().printStackTrace();
            throw new ActivationException("exception in group constructor", e3.getTargetException());
        } catch (ActivationException e4) {
            throw e4;
        } catch (Exception e5) {
            throw new ActivationException("exception creating group", e5);
        }
    }

    public static synchronized ActivationGroupID currentGroupID() {
        return currGroupID;
    }

    static synchronized ActivationGroupID internalCurrentGroupID() throws ActivationException {
        if (currGroupID == null) {
            throw new ActivationException("nonexistent group");
        }
        return currGroupID;
    }

    public static synchronized void setSystem(ActivationSystem activationSystem) throws ActivationException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkSetFactory();
        }
        if (currSystem != null) {
            throw new ActivationException("activation system already set");
        }
        currSystem = activationSystem;
    }

    public static synchronized ActivationSystem getSystem() throws ActivationException {
        if (currSystem == null) {
            try {
                currSystem = (ActivationSystem) Naming.lookup("//:" + ((Integer) AccessController.doPrivileged(new GetIntegerAction("java.rmi.activation.port", ActivationSystem.SYSTEM_PORT))).intValue() + "/java.rmi.activation.ActivationSystem");
            } catch (Exception e2) {
                throw new ActivationException("unable to obtain ActivationSystem", e2);
            }
        }
        return currSystem;
    }

    protected void activeObject(ActivationID activationID, MarshalledObject<? extends Remote> marshalledObject) throws ActivationException, RemoteException {
        getMonitor().activeObject(activationID, marshalledObject);
    }

    protected void inactiveGroup() throws UnknownGroupException, RemoteException {
        try {
            getMonitor().inactiveGroup(this.groupID, this.incarnation);
        } finally {
            destroyGroup();
        }
    }

    private ActivationMonitor getMonitor() throws RemoteException {
        synchronized (ActivationGroup.class) {
            if (this.monitor != null) {
                return this.monitor;
            }
            throw new RemoteException("monitor not received");
        }
    }

    private static synchronized void destroyGroup() {
        currGroup = null;
        currGroupID = null;
    }

    static synchronized ActivationGroup currentGroup() throws ActivationException {
        if (currGroup == null) {
            throw new ActivationException("group is not active");
        }
        return currGroup;
    }
}
