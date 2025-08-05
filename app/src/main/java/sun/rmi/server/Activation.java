package sun.rmi.server;

import com.sun.rmi.rmid.ExecOptionPermission;
import com.sun.rmi.rmid.ExecPermission;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.channels.Channel;
import java.nio.channels.ServerSocketChannel;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.MarshalledObject;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.UnexpectedException;
import java.rmi.activation.ActivationDesc;
import java.rmi.activation.ActivationException;
import java.rmi.activation.ActivationGroup;
import java.rmi.activation.ActivationGroupDesc;
import java.rmi.activation.ActivationGroupID;
import java.rmi.activation.ActivationID;
import java.rmi.activation.ActivationInstantiator;
import java.rmi.activation.ActivationMonitor;
import java.rmi.activation.ActivationSystem;
import java.rmi.activation.Activator;
import java.rmi.activation.UnknownGroupException;
import java.rmi.activation.UnknownObjectException;
import java.rmi.registry.Registry;
import java.rmi.server.ObjID;
import java.rmi.server.RMIClassLoader;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.RemoteObject;
import java.rmi.server.RemoteRef;
import java.rmi.server.RemoteServer;
import java.rmi.server.RemoteStub;
import java.rmi.server.UnicastRemoteObject;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.security.cert.Certificate;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.math3.optimization.direct.CMAESOptimizer;
import org.icepdf.core.pobjects.graphics.Separation;
import sun.rmi.log.LogHandler;
import sun.rmi.log.ReliableLog;
import sun.rmi.registry.RegistryImpl;
import sun.rmi.transport.LiveRef;
import sun.security.action.GetBooleanAction;
import sun.security.action.GetIntegerAction;
import sun.security.action.GetPropertyAction;
import sun.security.provider.PolicyFile;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:sun/rmi/server/Activation.class */
public class Activation implements Serializable {
    private static final long serialVersionUID = 2921265612698155191L;
    private static final byte MAJOR_VERSION = 1;
    private static final byte MINOR_VERSION = 0;
    private static Object execPolicy;
    private static Method execPolicyMethod;
    private static boolean debugExec;
    private Map<ActivationID, ActivationGroupID> idTable;
    private Map<ActivationGroupID, GroupEntry> groupTable;
    private byte majorVersion;
    private byte minorVersion;
    private transient int groupSemaphore;
    private transient int groupCounter;
    private transient ReliableLog log;
    private transient int numUpdates;
    private transient String[] command;
    private transient Activator activator;
    private transient Activator activatorStub;
    private transient ActivationSystem system;
    private transient ActivationSystem systemStub;
    private transient ActivationMonitor monitor;
    private transient Registry registry;
    private volatile transient boolean shuttingDown;
    private volatile transient Object startupLock;
    private transient Thread shutdownHook;
    private static final long groupTimeout = getInt("sun.rmi.activation.groupTimeout", 60000);
    private static final int snapshotInterval = getInt("sun.rmi.activation.snapshotInterval", 200);
    private static final long execTimeout = getInt("sun.rmi.activation.execTimeout", CMAESOptimizer.DEFAULT_MAXITERATIONS);
    private static final Object initLock = new Object();
    private static boolean initDone = false;
    private static ResourceBundle resources = null;

    /* loaded from: rt.jar:sun/rmi/server/Activation$ActivationSystemImpl_Stub.class */
    public final class ActivationSystemImpl_Stub extends RemoteStub implements ActivationSystem, Remote {
        private static final long serialVersionUID = 2;
        private static Method $method_activeGroup_0;
        private static Method $method_getActivationDesc_1;
        private static Method $method_getActivationGroupDesc_2;
        private static Method $method_registerGroup_3;
        private static Method $method_registerObject_4;
        private static Method $method_setActivationDesc_5;
        private static Method $method_setActivationGroupDesc_6;
        private static Method $method_shutdown_7;
        private static Method $method_unregisterGroup_8;
        private static Method $method_unregisterObject_9;
        static Class class$java$rmi$activation$ActivationSystem;
        static Class class$java$rmi$activation$ActivationGroupID;
        static Class class$java$rmi$activation$ActivationInstantiator;
        static Class class$java$rmi$activation$ActivationID;
        static Class class$java$rmi$activation$ActivationGroupDesc;
        static Class class$java$rmi$activation$ActivationDesc;

        static {
            Class clsClass$;
            Class<?> clsClass$2;
            Class<?> clsClass$3;
            Class clsClass$4;
            Class<?> clsClass$5;
            Class clsClass$6;
            Class<?> clsClass$7;
            Class clsClass$8;
            Class<?> clsClass$9;
            Class clsClass$10;
            Class<?> clsClass$11;
            Class clsClass$12;
            Class<?> clsClass$13;
            Class<?> clsClass$14;
            Class clsClass$15;
            Class<?> clsClass$16;
            Class<?> clsClass$17;
            Class clsClass$18;
            Class clsClass$19;
            Class<?> clsClass$20;
            Class clsClass$21;
            Class<?> clsClass$22;
            try {
                if (class$java$rmi$activation$ActivationSystem != null) {
                    clsClass$ = class$java$rmi$activation$ActivationSystem;
                } else {
                    clsClass$ = class$("java.rmi.activation.ActivationSystem");
                    class$java$rmi$activation$ActivationSystem = clsClass$;
                }
                Class<?>[] clsArr = new Class[3];
                if (class$java$rmi$activation$ActivationGroupID != null) {
                    clsClass$2 = class$java$rmi$activation$ActivationGroupID;
                } else {
                    clsClass$2 = class$("java.rmi.activation.ActivationGroupID");
                    class$java$rmi$activation$ActivationGroupID = clsClass$2;
                }
                clsArr[0] = clsClass$2;
                if (class$java$rmi$activation$ActivationInstantiator != null) {
                    clsClass$3 = class$java$rmi$activation$ActivationInstantiator;
                } else {
                    clsClass$3 = class$("java.rmi.activation.ActivationInstantiator");
                    class$java$rmi$activation$ActivationInstantiator = clsClass$3;
                }
                clsArr[1] = clsClass$3;
                clsArr[2] = Long.TYPE;
                $method_activeGroup_0 = clsClass$.getMethod("activeGroup", clsArr);
                if (class$java$rmi$activation$ActivationSystem != null) {
                    clsClass$4 = class$java$rmi$activation$ActivationSystem;
                } else {
                    clsClass$4 = class$("java.rmi.activation.ActivationSystem");
                    class$java$rmi$activation$ActivationSystem = clsClass$4;
                }
                Class<?>[] clsArr2 = new Class[1];
                if (class$java$rmi$activation$ActivationID != null) {
                    clsClass$5 = class$java$rmi$activation$ActivationID;
                } else {
                    clsClass$5 = class$("java.rmi.activation.ActivationID");
                    class$java$rmi$activation$ActivationID = clsClass$5;
                }
                clsArr2[0] = clsClass$5;
                $method_getActivationDesc_1 = clsClass$4.getMethod("getActivationDesc", clsArr2);
                if (class$java$rmi$activation$ActivationSystem != null) {
                    clsClass$6 = class$java$rmi$activation$ActivationSystem;
                } else {
                    clsClass$6 = class$("java.rmi.activation.ActivationSystem");
                    class$java$rmi$activation$ActivationSystem = clsClass$6;
                }
                Class<?>[] clsArr3 = new Class[1];
                if (class$java$rmi$activation$ActivationGroupID != null) {
                    clsClass$7 = class$java$rmi$activation$ActivationGroupID;
                } else {
                    clsClass$7 = class$("java.rmi.activation.ActivationGroupID");
                    class$java$rmi$activation$ActivationGroupID = clsClass$7;
                }
                clsArr3[0] = clsClass$7;
                $method_getActivationGroupDesc_2 = clsClass$6.getMethod("getActivationGroupDesc", clsArr3);
                if (class$java$rmi$activation$ActivationSystem != null) {
                    clsClass$8 = class$java$rmi$activation$ActivationSystem;
                } else {
                    clsClass$8 = class$("java.rmi.activation.ActivationSystem");
                    class$java$rmi$activation$ActivationSystem = clsClass$8;
                }
                Class<?>[] clsArr4 = new Class[1];
                if (class$java$rmi$activation$ActivationGroupDesc != null) {
                    clsClass$9 = class$java$rmi$activation$ActivationGroupDesc;
                } else {
                    clsClass$9 = class$("java.rmi.activation.ActivationGroupDesc");
                    class$java$rmi$activation$ActivationGroupDesc = clsClass$9;
                }
                clsArr4[0] = clsClass$9;
                $method_registerGroup_3 = clsClass$8.getMethod("registerGroup", clsArr4);
                if (class$java$rmi$activation$ActivationSystem != null) {
                    clsClass$10 = class$java$rmi$activation$ActivationSystem;
                } else {
                    clsClass$10 = class$("java.rmi.activation.ActivationSystem");
                    class$java$rmi$activation$ActivationSystem = clsClass$10;
                }
                Class<?>[] clsArr5 = new Class[1];
                if (class$java$rmi$activation$ActivationDesc != null) {
                    clsClass$11 = class$java$rmi$activation$ActivationDesc;
                } else {
                    clsClass$11 = class$("java.rmi.activation.ActivationDesc");
                    class$java$rmi$activation$ActivationDesc = clsClass$11;
                }
                clsArr5[0] = clsClass$11;
                $method_registerObject_4 = clsClass$10.getMethod("registerObject", clsArr5);
                if (class$java$rmi$activation$ActivationSystem != null) {
                    clsClass$12 = class$java$rmi$activation$ActivationSystem;
                } else {
                    clsClass$12 = class$("java.rmi.activation.ActivationSystem");
                    class$java$rmi$activation$ActivationSystem = clsClass$12;
                }
                Class<?>[] clsArr6 = new Class[2];
                if (class$java$rmi$activation$ActivationID != null) {
                    clsClass$13 = class$java$rmi$activation$ActivationID;
                } else {
                    clsClass$13 = class$("java.rmi.activation.ActivationID");
                    class$java$rmi$activation$ActivationID = clsClass$13;
                }
                clsArr6[0] = clsClass$13;
                if (class$java$rmi$activation$ActivationDesc != null) {
                    clsClass$14 = class$java$rmi$activation$ActivationDesc;
                } else {
                    clsClass$14 = class$("java.rmi.activation.ActivationDesc");
                    class$java$rmi$activation$ActivationDesc = clsClass$14;
                }
                clsArr6[1] = clsClass$14;
                $method_setActivationDesc_5 = clsClass$12.getMethod("setActivationDesc", clsArr6);
                if (class$java$rmi$activation$ActivationSystem != null) {
                    clsClass$15 = class$java$rmi$activation$ActivationSystem;
                } else {
                    clsClass$15 = class$("java.rmi.activation.ActivationSystem");
                    class$java$rmi$activation$ActivationSystem = clsClass$15;
                }
                Class<?>[] clsArr7 = new Class[2];
                if (class$java$rmi$activation$ActivationGroupID != null) {
                    clsClass$16 = class$java$rmi$activation$ActivationGroupID;
                } else {
                    clsClass$16 = class$("java.rmi.activation.ActivationGroupID");
                    class$java$rmi$activation$ActivationGroupID = clsClass$16;
                }
                clsArr7[0] = clsClass$16;
                if (class$java$rmi$activation$ActivationGroupDesc != null) {
                    clsClass$17 = class$java$rmi$activation$ActivationGroupDesc;
                } else {
                    clsClass$17 = class$("java.rmi.activation.ActivationGroupDesc");
                    class$java$rmi$activation$ActivationGroupDesc = clsClass$17;
                }
                clsArr7[1] = clsClass$17;
                $method_setActivationGroupDesc_6 = clsClass$15.getMethod("setActivationGroupDesc", clsArr7);
                if (class$java$rmi$activation$ActivationSystem != null) {
                    clsClass$18 = class$java$rmi$activation$ActivationSystem;
                } else {
                    clsClass$18 = class$("java.rmi.activation.ActivationSystem");
                    class$java$rmi$activation$ActivationSystem = clsClass$18;
                }
                $method_shutdown_7 = clsClass$18.getMethod("shutdown", new Class[0]);
                if (class$java$rmi$activation$ActivationSystem != null) {
                    clsClass$19 = class$java$rmi$activation$ActivationSystem;
                } else {
                    clsClass$19 = class$("java.rmi.activation.ActivationSystem");
                    class$java$rmi$activation$ActivationSystem = clsClass$19;
                }
                Class<?>[] clsArr8 = new Class[1];
                if (class$java$rmi$activation$ActivationGroupID != null) {
                    clsClass$20 = class$java$rmi$activation$ActivationGroupID;
                } else {
                    clsClass$20 = class$("java.rmi.activation.ActivationGroupID");
                    class$java$rmi$activation$ActivationGroupID = clsClass$20;
                }
                clsArr8[0] = clsClass$20;
                $method_unregisterGroup_8 = clsClass$19.getMethod("unregisterGroup", clsArr8);
                if (class$java$rmi$activation$ActivationSystem != null) {
                    clsClass$21 = class$java$rmi$activation$ActivationSystem;
                } else {
                    clsClass$21 = class$("java.rmi.activation.ActivationSystem");
                    class$java$rmi$activation$ActivationSystem = clsClass$21;
                }
                Class<?>[] clsArr9 = new Class[1];
                if (class$java$rmi$activation$ActivationID != null) {
                    clsClass$22 = class$java$rmi$activation$ActivationID;
                } else {
                    clsClass$22 = class$("java.rmi.activation.ActivationID");
                    class$java$rmi$activation$ActivationID = clsClass$22;
                }
                clsArr9[0] = clsClass$22;
                $method_unregisterObject_9 = clsClass$21.getMethod("unregisterObject", clsArr9);
            } catch (NoSuchMethodException unused) {
                throw new NoSuchMethodError("stub class initialization failed");
            }
        }

        public ActivationSystemImpl_Stub(RemoteRef remoteRef) {
            super(remoteRef);
        }

        @Override // java.rmi.activation.ActivationSystem
        public ActivationMonitor activeGroup(ActivationGroupID activationGroupID, ActivationInstantiator activationInstantiator, long j2) throws ActivationException, RemoteException {
            try {
                return (ActivationMonitor) this.ref.invoke(this, $method_activeGroup_0, new Object[]{activationGroupID, activationInstantiator, new Long(j2)}, -4575843150759415294L);
            } catch (RuntimeException e2) {
                throw e2;
            } catch (RemoteException e3) {
                throw e3;
            } catch (ActivationException e4) {
                throw e4;
            } catch (Exception e5) {
                throw new UnexpectedException("undeclared checked exception", e5);
            }
        }

        static Class class$(String str) {
            try {
                return Class.forName(str);
            } catch (ClassNotFoundException e2) {
                throw new NoClassDefFoundError(e2.getMessage());
            }
        }

        @Override // java.rmi.activation.ActivationSystem
        public ActivationDesc getActivationDesc(ActivationID activationID) throws ActivationException, RemoteException {
            try {
                return (ActivationDesc) this.ref.invoke(this, $method_getActivationDesc_1, new Object[]{activationID}, 4830055440982622087L);
            } catch (RuntimeException e2) {
                throw e2;
            } catch (RemoteException e3) {
                throw e3;
            } catch (ActivationException e4) {
                throw e4;
            } catch (Exception e5) {
                throw new UnexpectedException("undeclared checked exception", e5);
            }
        }

        @Override // java.rmi.activation.ActivationSystem
        public ActivationGroupDesc getActivationGroupDesc(ActivationGroupID activationGroupID) throws ActivationException, RemoteException {
            try {
                return (ActivationGroupDesc) this.ref.invoke(this, $method_getActivationGroupDesc_2, new Object[]{activationGroupID}, -8701843806548736528L);
            } catch (RuntimeException e2) {
                throw e2;
            } catch (RemoteException e3) {
                throw e3;
            } catch (ActivationException e4) {
                throw e4;
            } catch (Exception e5) {
                throw new UnexpectedException("undeclared checked exception", e5);
            }
        }

        @Override // java.rmi.activation.ActivationSystem
        public ActivationGroupID registerGroup(ActivationGroupDesc activationGroupDesc) throws ActivationException, RemoteException {
            try {
                return (ActivationGroupID) this.ref.invoke(this, $method_registerGroup_3, new Object[]{activationGroupDesc}, 6921515268192657754L);
            } catch (RuntimeException e2) {
                throw e2;
            } catch (RemoteException e3) {
                throw e3;
            } catch (ActivationException e4) {
                throw e4;
            } catch (Exception e5) {
                throw new UnexpectedException("undeclared checked exception", e5);
            }
        }

        @Override // java.rmi.activation.ActivationSystem
        public ActivationID registerObject(ActivationDesc activationDesc) throws ActivationException, RemoteException {
            try {
                return (ActivationID) this.ref.invoke(this, $method_registerObject_4, new Object[]{activationDesc}, -3006759798994351347L);
            } catch (RuntimeException e2) {
                throw e2;
            } catch (RemoteException e3) {
                throw e3;
            } catch (ActivationException e4) {
                throw e4;
            } catch (Exception e5) {
                throw new UnexpectedException("undeclared checked exception", e5);
            }
        }

        @Override // java.rmi.activation.ActivationSystem
        public ActivationDesc setActivationDesc(ActivationID activationID, ActivationDesc activationDesc) throws ActivationException, RemoteException {
            try {
                return (ActivationDesc) this.ref.invoke(this, $method_setActivationDesc_5, new Object[]{activationID, activationDesc}, 7128043237057180796L);
            } catch (RuntimeException e2) {
                throw e2;
            } catch (RemoteException e3) {
                throw e3;
            } catch (ActivationException e4) {
                throw e4;
            } catch (Exception e5) {
                throw new UnexpectedException("undeclared checked exception", e5);
            }
        }

        @Override // java.rmi.activation.ActivationSystem
        public ActivationGroupDesc setActivationGroupDesc(ActivationGroupID activationGroupID, ActivationGroupDesc activationGroupDesc) throws ActivationException, RemoteException {
            try {
                return (ActivationGroupDesc) this.ref.invoke(this, $method_setActivationGroupDesc_6, new Object[]{activationGroupID, activationGroupDesc}, 1213918527826541191L);
            } catch (RuntimeException e2) {
                throw e2;
            } catch (RemoteException e3) {
                throw e3;
            } catch (ActivationException e4) {
                throw e4;
            } catch (Exception e5) {
                throw new UnexpectedException("undeclared checked exception", e5);
            }
        }

        @Override // java.rmi.activation.ActivationSystem
        public void shutdown() throws RemoteException {
            try {
                this.ref.invoke(this, $method_shutdown_7, null, -7207851917985848402L);
            } catch (RuntimeException e2) {
                throw e2;
            } catch (RemoteException e3) {
                throw e3;
            } catch (Exception e4) {
                throw new UnexpectedException("undeclared checked exception", e4);
            }
        }

        @Override // java.rmi.activation.ActivationSystem
        public void unregisterGroup(ActivationGroupID activationGroupID) throws ActivationException, RemoteException {
            try {
                this.ref.invoke(this, $method_unregisterGroup_8, new Object[]{activationGroupID}, 3768097077835970701L);
            } catch (RuntimeException e2) {
                throw e2;
            } catch (RemoteException e3) {
                throw e3;
            } catch (ActivationException e4) {
                throw e4;
            } catch (Exception e5) {
                throw new UnexpectedException("undeclared checked exception", e5);
            }
        }

        @Override // java.rmi.activation.ActivationSystem
        public void unregisterObject(ActivationID activationID) throws ActivationException, RemoteException {
            try {
                this.ref.invoke(this, $method_unregisterObject_9, new Object[]{activationID}, -6843850585331411084L);
            } catch (RuntimeException e2) {
                throw e2;
            } catch (RemoteException e3) {
                throw e3;
            } catch (ActivationException e4) {
                throw e4;
            } catch (Exception e5) {
                throw new UnexpectedException("undeclared checked exception", e5);
            }
        }
    }

    private static int getInt(String str, int i2) {
        return ((Integer) AccessController.doPrivileged(new GetIntegerAction(str, i2))).intValue();
    }

    private Activation() {
        this.idTable = new ConcurrentHashMap();
        this.groupTable = new ConcurrentHashMap();
        this.majorVersion = (byte) 1;
        this.minorVersion = (byte) 0;
        this.shuttingDown = false;
    }

    private static void startActivation(int i2, RMIServerSocketFactory rMIServerSocketFactory, String str, String[] strArr) throws Exception {
        ReliableLog reliableLog = new ReliableLog(str, new ActLogHandler());
        ((Activation) reliableLog.recover()).init(i2, rMIServerSocketFactory, reliableLog, strArr);
    }

    private void init(int i2, RMIServerSocketFactory rMIServerSocketFactory, ReliableLog reliableLog, String[] strArr) throws Exception {
        this.log = reliableLog;
        this.numUpdates = 0;
        this.shutdownHook = new ShutdownHook();
        this.groupSemaphore = getInt("sun.rmi.activation.groupThrottle", 3);
        this.groupCounter = 0;
        Runtime.getRuntime().addShutdownHook(this.shutdownHook);
        ActivationGroupID[] activationGroupIDArr = (ActivationGroupID[]) this.groupTable.keySet().toArray(new ActivationGroupID[0]);
        Object obj = new Object();
        this.startupLock = obj;
        synchronized (obj) {
            this.activator = new ActivatorImpl(i2, rMIServerSocketFactory);
            this.activatorStub = (Activator) RemoteObject.toStub(this.activator);
            this.system = new ActivationSystemImpl(i2, rMIServerSocketFactory);
            this.systemStub = (ActivationSystem) RemoteObject.toStub(this.system);
            this.monitor = new ActivationMonitorImpl(i2, rMIServerSocketFactory);
            initCommand(strArr);
            this.registry = new SystemRegistryImpl(i2, null, rMIServerSocketFactory, this.systemStub);
            if (rMIServerSocketFactory != null) {
                synchronized (initLock) {
                    initDone = true;
                    initLock.notifyAll();
                }
            }
        }
        this.startupLock = null;
        int length = activationGroupIDArr.length;
        while (true) {
            length--;
            if (length >= 0) {
                try {
                    getGroupEntry(activationGroupIDArr[length]).restartServices();
                } catch (UnknownGroupException e2) {
                    System.err.println(getTextResource("rmid.restart.group.warning"));
                    e2.printStackTrace();
                }
            } else {
                return;
            }
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (!(this.groupTable instanceof ConcurrentHashMap)) {
            this.groupTable = new ConcurrentHashMap(this.groupTable);
        }
        if (!(this.idTable instanceof ConcurrentHashMap)) {
            this.idTable = new ConcurrentHashMap(this.idTable);
        }
    }

    /* loaded from: rt.jar:sun/rmi/server/Activation$SystemRegistryImpl.class */
    private static class SystemRegistryImpl extends RegistryImpl {
        private static final String NAME = ActivationSystem.class.getName();
        private static final long serialVersionUID = 4877330021609408794L;
        private final ActivationSystem systemStub;

        SystemRegistryImpl(int i2, RMIClientSocketFactory rMIClientSocketFactory, RMIServerSocketFactory rMIServerSocketFactory, ActivationSystem activationSystem) throws RemoteException {
            super(i2, rMIClientSocketFactory, rMIServerSocketFactory);
            this.systemStub = activationSystem;
        }

        @Override // sun.rmi.registry.RegistryImpl, java.rmi.registry.Registry
        public Remote lookup(String str) throws NotBoundException, RemoteException {
            if (str.equals(NAME)) {
                return this.systemStub;
            }
            return super.lookup(str);
        }

        @Override // sun.rmi.registry.RegistryImpl, java.rmi.registry.Registry
        public String[] list() throws RemoteException {
            String[] list = super.list();
            int length = list.length;
            String[] strArr = new String[length + 1];
            if (length > 0) {
                System.arraycopy(list, 0, strArr, 0, length);
            }
            strArr[length] = NAME;
            return strArr;
        }

        @Override // sun.rmi.registry.RegistryImpl, java.rmi.registry.Registry
        public void bind(String str, Remote remote) throws AlreadyBoundException, UnknownHostException, RemoteException {
            if (str.equals(NAME)) {
                throw new AccessException("binding ActivationSystem is disallowed");
            }
            RegistryImpl.checkAccess("ActivationSystem.bind");
            super.bind(str, remote);
        }

        @Override // sun.rmi.registry.RegistryImpl, java.rmi.registry.Registry
        public void unbind(String str) throws NotBoundException, UnknownHostException, RemoteException {
            if (str.equals(NAME)) {
                throw new AccessException("unbinding ActivationSystem is disallowed");
            }
            RegistryImpl.checkAccess("ActivationSystem.unbind");
            super.unbind(str);
        }

        @Override // sun.rmi.registry.RegistryImpl, java.rmi.registry.Registry
        public void rebind(String str, Remote remote) throws UnknownHostException, RemoteException {
            if (str.equals(NAME)) {
                throw new AccessException("binding ActivationSystem is disallowed");
            }
            RegistryImpl.checkAccess("ActivationSystem.rebind");
            super.rebind(str, remote);
        }
    }

    /* loaded from: rt.jar:sun/rmi/server/Activation$ActivatorImpl.class */
    class ActivatorImpl extends RemoteServer implements Activator {
        private static final long serialVersionUID = -3654244726254566136L;

        ActivatorImpl(int i2, RMIServerSocketFactory rMIServerSocketFactory) throws RemoteException {
            UnicastServerRef unicastServerRef = new UnicastServerRef(new LiveRef(new ObjID(1), i2, null, rMIServerSocketFactory));
            this.ref = unicastServerRef;
            unicastServerRef.exportObject(this, null, false);
        }

        @Override // java.rmi.activation.Activator
        public MarshalledObject<? extends Remote> activate(ActivationID activationID, boolean z2) throws ActivationException, RemoteException {
            Activation.this.checkShutdown();
            return Activation.this.getGroupEntry(activationID).activate(activationID, z2);
        }
    }

    /* loaded from: rt.jar:sun/rmi/server/Activation$ActivationMonitorImpl.class */
    class ActivationMonitorImpl extends UnicastRemoteObject implements ActivationMonitor {
        private static final long serialVersionUID = -6214940464757948867L;

        ActivationMonitorImpl(int i2, RMIServerSocketFactory rMIServerSocketFactory) throws RemoteException {
            super(i2, null, rMIServerSocketFactory);
        }

        @Override // java.rmi.activation.ActivationMonitor
        public void inactiveObject(ActivationID activationID) throws UnknownHostException, UnknownObjectException, RemoteException {
            try {
                Activation.this.checkShutdown();
                RegistryImpl.checkAccess("Activator.inactiveObject");
                Activation.this.getGroupEntry(activationID).inactiveObject(activationID);
            } catch (ActivationException e2) {
            }
        }

        @Override // java.rmi.activation.ActivationMonitor
        public void activeObject(ActivationID activationID, MarshalledObject<? extends Remote> marshalledObject) throws UnknownHostException, UnknownObjectException, RemoteException {
            try {
                Activation.this.checkShutdown();
                RegistryImpl.checkAccess("ActivationSystem.activeObject");
                Activation.this.getGroupEntry(activationID).activeObject(activationID, marshalledObject);
            } catch (ActivationException e2) {
            }
        }

        @Override // java.rmi.activation.ActivationMonitor
        public void inactiveGroup(ActivationGroupID activationGroupID, long j2) throws UnknownGroupException, UnknownHostException, RemoteException {
            try {
                Activation.this.checkShutdown();
                RegistryImpl.checkAccess("ActivationMonitor.inactiveGroup");
                Activation.this.getGroupEntry(activationGroupID).inactiveGroup(j2, false);
            } catch (ActivationException e2) {
            }
        }
    }

    /* loaded from: rt.jar:sun/rmi/server/Activation$SameHostOnlyServerRef.class */
    static class SameHostOnlyServerRef extends UnicastServerRef {
        private static final long serialVersionUID = 1234;
        private String accessKind;

        SameHostOnlyServerRef(LiveRef liveRef, String str) {
            super(liveRef);
            this.accessKind = str;
        }

        @Override // sun.rmi.server.UnicastServerRef
        protected void unmarshalCustomCallData(ObjectInput objectInput) throws IOException, ClassNotFoundException {
            RegistryImpl.checkAccess(this.accessKind);
            super.unmarshalCustomCallData(objectInput);
        }
    }

    /* loaded from: rt.jar:sun/rmi/server/Activation$ActivationSystemImpl.class */
    class ActivationSystemImpl extends RemoteServer implements ActivationSystem {
        private static final long serialVersionUID = 9100152600327688967L;

        ActivationSystemImpl(int i2, RMIServerSocketFactory rMIServerSocketFactory) throws RemoteException {
            SameHostOnlyServerRef sameHostOnlyServerRef = new SameHostOnlyServerRef(new LiveRef(new ObjID(4), i2, null, rMIServerSocketFactory), "ActivationSystem.nonLocalAccess");
            this.ref = sameHostOnlyServerRef;
            sameHostOnlyServerRef.exportObject(this, null);
        }

        @Override // java.rmi.activation.ActivationSystem
        public ActivationID registerObject(ActivationDesc activationDesc) throws ActivationException, RemoteException {
            Activation.this.checkShutdown();
            ActivationGroupID groupID = activationDesc.getGroupID();
            ActivationID activationID = new ActivationID(Activation.this.activatorStub);
            Activation.this.getGroupEntry(groupID).registerObject(activationID, activationDesc, true);
            return activationID;
        }

        @Override // java.rmi.activation.ActivationSystem
        public void unregisterObject(ActivationID activationID) throws ActivationException, RemoteException {
            Activation.this.checkShutdown();
            Activation.this.getGroupEntry(activationID).unregisterObject(activationID, true);
        }

        @Override // java.rmi.activation.ActivationSystem
        public ActivationGroupID registerGroup(ActivationGroupDesc activationGroupDesc) throws ActivationException, SecurityException, RemoteException {
            Activation.this.checkShutdown();
            Activation.this.checkArgs(activationGroupDesc, null);
            ActivationGroupID activationGroupID = new ActivationGroupID(Activation.this.systemStub);
            Activation.this.groupTable.put(activationGroupID, Activation.this.new GroupEntry(activationGroupID, activationGroupDesc));
            Activation.this.addLogRecord(new LogRegisterGroup(activationGroupID, activationGroupDesc));
            return activationGroupID;
        }

        @Override // java.rmi.activation.ActivationSystem
        public ActivationMonitor activeGroup(ActivationGroupID activationGroupID, ActivationInstantiator activationInstantiator, long j2) throws ActivationException, RemoteException {
            Activation.this.checkShutdown();
            Activation.this.getGroupEntry(activationGroupID).activeGroup(activationInstantiator, j2);
            return Activation.this.monitor;
        }

        @Override // java.rmi.activation.ActivationSystem
        public void unregisterGroup(ActivationGroupID activationGroupID) throws ActivationException, RemoteException {
            Activation.this.checkShutdown();
            Activation.this.removeGroupEntry(activationGroupID).unregisterGroup(true);
        }

        @Override // java.rmi.activation.ActivationSystem
        public ActivationDesc setActivationDesc(ActivationID activationID, ActivationDesc activationDesc) throws ActivationException, RemoteException {
            Activation.this.checkShutdown();
            if (Activation.this.getGroupID(activationID).equals(activationDesc.getGroupID())) {
                return Activation.this.getGroupEntry(activationID).setActivationDesc(activationID, activationDesc, true);
            }
            throw new ActivationException("ActivationDesc contains wrong group");
        }

        @Override // java.rmi.activation.ActivationSystem
        public ActivationGroupDesc setActivationGroupDesc(ActivationGroupID activationGroupID, ActivationGroupDesc activationGroupDesc) throws ActivationException, SecurityException, RemoteException {
            Activation.this.checkShutdown();
            Activation.this.checkArgs(activationGroupDesc, null);
            return Activation.this.getGroupEntry(activationGroupID).setActivationGroupDesc(activationGroupID, activationGroupDesc, true);
        }

        @Override // java.rmi.activation.ActivationSystem
        public ActivationDesc getActivationDesc(ActivationID activationID) throws ActivationException, RemoteException {
            Activation.this.checkShutdown();
            return Activation.this.getGroupEntry(activationID).getActivationDesc(activationID);
        }

        @Override // java.rmi.activation.ActivationSystem
        public ActivationGroupDesc getActivationGroupDesc(ActivationGroupID activationGroupID) throws ActivationException, RemoteException {
            Activation.this.checkShutdown();
            return Activation.this.getGroupEntry(activationGroupID).desc;
        }

        @Override // java.rmi.activation.ActivationSystem
        public void shutdown() throws AccessException {
            Object obj = Activation.this.startupLock;
            if (obj != null) {
                synchronized (obj) {
                }
            }
            synchronized (Activation.this) {
                if (!Activation.this.shuttingDown) {
                    Activation.this.shuttingDown = true;
                    Activation.this.new Shutdown().start();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkShutdown() throws ActivationException {
        Object obj = this.startupLock;
        if (obj != null) {
            synchronized (obj) {
            }
        }
        if (this.shuttingDown) {
            throw new ActivationException("activation system shutting down");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void unexport(Remote remote) {
        while (!UnicastRemoteObject.unexportObject(remote, false)) {
            try {
                Thread.sleep(100L);
            } catch (Exception e2) {
            }
        }
    }

    /* loaded from: rt.jar:sun/rmi/server/Activation$Shutdown.class */
    private class Shutdown extends Thread {
        Shutdown() {
            super("rmid Shutdown");
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            try {
                Activation.unexport(Activation.this.activator);
                Activation.unexport(Activation.this.system);
                Iterator it = Activation.this.groupTable.values().iterator();
                while (it.hasNext()) {
                    ((GroupEntry) it.next()).shutdown();
                }
                Runtime.getRuntime().removeShutdownHook(Activation.this.shutdownHook);
                Activation.unexport(Activation.this.monitor);
                try {
                    synchronized (Activation.this.log) {
                        Activation.this.log.close();
                    }
                } catch (IOException e2) {
                }
                System.err.println(Activation.getTextResource("rmid.daemon.shutdown"));
                System.exit(0);
            } catch (Throwable th) {
                System.err.println(Activation.getTextResource("rmid.daemon.shutdown"));
                System.exit(0);
                throw th;
            }
        }
    }

    /* loaded from: rt.jar:sun/rmi/server/Activation$ShutdownHook.class */
    private class ShutdownHook extends Thread {
        ShutdownHook() {
            super("rmid ShutdownHook");
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            synchronized (Activation.this) {
                Activation.this.shuttingDown = true;
            }
            Iterator it = Activation.this.groupTable.values().iterator();
            while (it.hasNext()) {
                ((GroupEntry) it.next()).shutdownFast();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ActivationGroupID getGroupID(ActivationID activationID) throws UnknownObjectException {
        ActivationGroupID activationGroupID = this.idTable.get(activationID);
        if (activationGroupID != null) {
            return activationGroupID;
        }
        throw new UnknownObjectException("unknown object: " + ((Object) activationID));
    }

    private GroupEntry getGroupEntry(ActivationGroupID activationGroupID, boolean z2) throws UnknownGroupException {
        GroupEntry groupEntryRemove;
        if (activationGroupID.getClass() == ActivationGroupID.class) {
            if (z2) {
                groupEntryRemove = this.groupTable.remove(activationGroupID);
            } else {
                groupEntryRemove = this.groupTable.get(activationGroupID);
            }
            if (groupEntryRemove != null && !groupEntryRemove.removed) {
                return groupEntryRemove;
            }
        }
        throw new UnknownGroupException("group unknown");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public GroupEntry getGroupEntry(ActivationGroupID activationGroupID) throws UnknownGroupException {
        return getGroupEntry(activationGroupID, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public GroupEntry removeGroupEntry(ActivationGroupID activationGroupID) throws UnknownGroupException {
        return getGroupEntry(activationGroupID, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public GroupEntry getGroupEntry(ActivationID activationID) throws UnknownObjectException {
        GroupEntry groupEntry = this.groupTable.get(getGroupID(activationID));
        if (groupEntry != null && !groupEntry.removed) {
            return groupEntry;
        }
        throw new UnknownObjectException("object's group removed");
    }

    /* loaded from: rt.jar:sun/rmi/server/Activation$GroupEntry.class */
    private class GroupEntry implements Serializable {
        private static final long serialVersionUID = 7222464070032993304L;
        private static final int MAX_TRIES = 2;
        private static final int NORMAL = 0;
        private static final int CREATING = 1;
        private static final int TERMINATE = 2;
        private static final int TERMINATING = 3;
        ActivationGroupDesc desc;
        ActivationGroupID groupID;
        long incarnation = 0;
        Map<ActivationID, ObjectEntry> objects = new HashMap();
        Set<ActivationID> restartSet = new HashSet();
        transient ActivationInstantiator group = null;
        transient int status = 0;
        transient long waitTime = 0;
        transient String groupName = null;
        transient Process child = null;
        transient boolean removed = false;
        transient Watchdog watchdog = null;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Activation.class.desiredAssertionStatus();
        }

        GroupEntry(ActivationGroupID activationGroupID, ActivationGroupDesc activationGroupDesc) {
            this.desc = null;
            this.groupID = null;
            this.groupID = activationGroupID;
            this.desc = activationGroupDesc;
        }

        void restartServices() {
            synchronized (this) {
                if (this.restartSet.isEmpty()) {
                    return;
                }
                Iterator it = new HashSet(this.restartSet).iterator();
                while (it.hasNext()) {
                    try {
                        activate((ActivationID) it.next(), true);
                    } catch (Exception e2) {
                        if (Activation.this.shuttingDown) {
                            return;
                        }
                        System.err.println(Activation.getTextResource("rmid.restart.service.warning"));
                        e2.printStackTrace();
                    }
                }
            }
        }

        synchronized void activeGroup(ActivationInstantiator activationInstantiator, long j2) throws ActivationException {
            if (this.incarnation != j2) {
                throw new ActivationException("invalid incarnation");
            }
            if (this.group != null) {
                if (this.group.equals(activationInstantiator)) {
                } else {
                    throw new ActivationException("group already active");
                }
            } else {
                if (this.child != null && this.status != 1) {
                    throw new ActivationException("group not being created");
                }
                this.group = activationInstantiator;
                this.status = 0;
                notifyAll();
            }
        }

        private void checkRemoved() throws UnknownGroupException {
            if (this.removed) {
                throw new UnknownGroupException("group removed");
            }
        }

        private ObjectEntry getObjectEntry(ActivationID activationID) throws UnknownObjectException {
            if (this.removed) {
                throw new UnknownObjectException("object's group removed");
            }
            ObjectEntry objectEntry = this.objects.get(activationID);
            if (objectEntry == null) {
                throw new UnknownObjectException("object unknown");
            }
            return objectEntry;
        }

        synchronized void registerObject(ActivationID activationID, ActivationDesc activationDesc, boolean z2) throws ActivationException {
            checkRemoved();
            this.objects.put(activationID, new ObjectEntry(activationDesc));
            if (activationDesc.getRestartMode()) {
                this.restartSet.add(activationID);
            }
            Activation.this.idTable.put(activationID, this.groupID);
            if (z2) {
                Activation.this.addLogRecord(new LogRegisterObject(activationID, activationDesc));
            }
        }

        synchronized void unregisterObject(ActivationID activationID, boolean z2) throws ActivationException {
            ObjectEntry objectEntry = getObjectEntry(activationID);
            objectEntry.removed = true;
            this.objects.remove(activationID);
            if (objectEntry.desc.getRestartMode()) {
                this.restartSet.remove(activationID);
            }
            Activation.this.idTable.remove(activationID);
            if (z2) {
                Activation.this.addLogRecord(new LogUnregisterObject(activationID));
            }
        }

        synchronized void unregisterGroup(boolean z2) throws ActivationException {
            checkRemoved();
            this.removed = true;
            for (Map.Entry<ActivationID, ObjectEntry> entry : this.objects.entrySet()) {
                Activation.this.idTable.remove(entry.getKey());
                entry.getValue().removed = true;
            }
            this.objects.clear();
            this.restartSet.clear();
            reset();
            childGone();
            if (z2) {
                Activation.this.addLogRecord(new LogUnregisterGroup(this.groupID));
            }
        }

        synchronized ActivationDesc setActivationDesc(ActivationID activationID, ActivationDesc activationDesc, boolean z2) throws ActivationException {
            ObjectEntry objectEntry = getObjectEntry(activationID);
            ActivationDesc activationDesc2 = objectEntry.desc;
            objectEntry.desc = activationDesc;
            if (activationDesc.getRestartMode()) {
                this.restartSet.add(activationID);
            } else {
                this.restartSet.remove(activationID);
            }
            if (z2) {
                Activation.this.addLogRecord(new LogUpdateDesc(activationID, activationDesc));
            }
            return activationDesc2;
        }

        synchronized ActivationDesc getActivationDesc(ActivationID activationID) throws UnknownGroupException, UnknownObjectException {
            return getObjectEntry(activationID).desc;
        }

        synchronized ActivationGroupDesc setActivationGroupDesc(ActivationGroupID activationGroupID, ActivationGroupDesc activationGroupDesc, boolean z2) throws ActivationException {
            checkRemoved();
            ActivationGroupDesc activationGroupDesc2 = this.desc;
            this.desc = activationGroupDesc;
            if (z2) {
                Activation.this.addLogRecord(new LogUpdateGroupDesc(activationGroupID, activationGroupDesc));
            }
            return activationGroupDesc2;
        }

        synchronized void inactiveGroup(long j2, boolean z2) throws UnknownGroupException {
            checkRemoved();
            if (this.incarnation != j2) {
                throw new UnknownGroupException("invalid incarnation");
            }
            reset();
            if (z2) {
                terminate();
            } else if (this.child != null && this.status == 0) {
                this.status = 2;
                this.watchdog.noRestart();
            }
        }

        synchronized void activeObject(ActivationID activationID, MarshalledObject<? extends Remote> marshalledObject) throws UnknownObjectException {
            getObjectEntry(activationID).stub = marshalledObject;
        }

        synchronized void inactiveObject(ActivationID activationID) throws UnknownObjectException {
            getObjectEntry(activationID).reset();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void reset() {
            this.group = null;
            Iterator<ObjectEntry> it = this.objects.values().iterator();
            while (it.hasNext()) {
                it.next().reset();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void childGone() {
            if (this.child != null) {
                this.child = null;
                this.watchdog.dispose();
                this.watchdog = null;
                this.status = 0;
                notifyAll();
            }
        }

        private void terminate() {
            if (this.child != null && this.status != 3) {
                this.child.destroy();
                this.status = 3;
                this.waitTime = System.currentTimeMillis() + Activation.groupTimeout;
                notifyAll();
            }
        }

        private void await() {
            while (true) {
                switch (this.status) {
                    case 0:
                        return;
                    case 1:
                        try {
                            wait();
                            continue;
                        } catch (InterruptedException e2) {
                        }
                    case 2:
                        terminate();
                        break;
                }
                try {
                    this.child.exitValue();
                } catch (IllegalThreadStateException e3) {
                    long jCurrentTimeMillis = System.currentTimeMillis();
                    if (this.waitTime > jCurrentTimeMillis) {
                        try {
                            wait(this.waitTime - jCurrentTimeMillis);
                        } catch (InterruptedException e4) {
                        }
                    }
                }
            }
            childGone();
        }

        void shutdownFast() {
            Process process = this.child;
            if (process != null) {
                process.destroy();
            }
        }

        synchronized void shutdown() {
            reset();
            terminate();
            await();
        }

        /* JADX WARN: Removed duplicated region for block: B:45:0x0099 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:53:0x00cd A[SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        java.rmi.MarshalledObject<? extends java.rmi.Remote> activate(java.rmi.activation.ActivationID r8, boolean r9) throws java.rmi.activation.ActivationException {
            /*
                Method dump skipped, instructions count: 222
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: sun.rmi.server.Activation.GroupEntry.activate(java.rmi.activation.ActivationID, boolean):java.rmi.MarshalledObject");
        }

        private ActivationInstantiator getInstantiator(ActivationGroupID activationGroupID) throws ActivationException {
            if (!$assertionsDisabled && !Thread.holdsLock(this)) {
                throw new AssertionError();
            }
            await();
            if (this.group != null) {
                return this.group;
            }
            checkRemoved();
            boolean z2 = false;
            try {
                this.groupName = Activation.this.Pstartgroup();
                z2 = true;
                String[] strArrActivationArgs = Activation.this.activationArgs(this.desc);
                Activation.this.checkArgs(this.desc, strArrActivationArgs);
                if (Activation.debugExec) {
                    StringBuffer stringBuffer = new StringBuffer(strArrActivationArgs[0]);
                    for (int i2 = 1; i2 < strArrActivationArgs.length; i2++) {
                        stringBuffer.append(' ');
                        stringBuffer.append(strArrActivationArgs[i2]);
                    }
                    System.err.println(MessageFormat.format(Activation.getTextResource("rmid.exec.command"), stringBuffer.toString()));
                }
                try {
                    this.child = Runtime.getRuntime().exec(strArrActivationArgs);
                    this.status = 1;
                    this.incarnation++;
                    this.watchdog = new Watchdog();
                    this.watchdog.start();
                    Activation.this.addLogRecord(new LogGroupIncarnation(activationGroupID, this.incarnation));
                    PipeWriter.plugTogetherPair(this.child.getInputStream(), System.out, this.child.getErrorStream(), System.err);
                    MarshalOutputStream marshalOutputStream = new MarshalOutputStream(this.child.getOutputStream());
                    Throwable th = null;
                    try {
                        try {
                            marshalOutputStream.writeObject(activationGroupID);
                            marshalOutputStream.writeObject(this.desc);
                            marshalOutputStream.writeLong(this.incarnation);
                            marshalOutputStream.flush();
                            if (marshalOutputStream != null) {
                                if (0 != 0) {
                                    try {
                                        marshalOutputStream.close();
                                    } catch (Throwable th2) {
                                        th.addSuppressed(th2);
                                    }
                                } else {
                                    marshalOutputStream.close();
                                }
                            }
                            try {
                                long jCurrentTimeMillis = System.currentTimeMillis();
                                long j2 = jCurrentTimeMillis + Activation.execTimeout;
                                do {
                                    wait(j2 - jCurrentTimeMillis);
                                    if (this.group != null) {
                                        ActivationInstantiator activationInstantiator = this.group;
                                        if (1 != 0) {
                                            Activation.this.Vstartgroup();
                                        }
                                        return activationInstantiator;
                                    }
                                    jCurrentTimeMillis = System.currentTimeMillis();
                                    if (this.status != 1) {
                                        break;
                                    }
                                } while (jCurrentTimeMillis < j2);
                            } catch (InterruptedException e2) {
                            }
                            terminate();
                            throw new ActivationException(this.removed ? "activation group unregistered" : "timeout creating child process");
                        } catch (Throwable th3) {
                            if (marshalOutputStream != null) {
                                if (th != null) {
                                    try {
                                        marshalOutputStream.close();
                                    } catch (Throwable th4) {
                                        th.addSuppressed(th4);
                                    }
                                } else {
                                    marshalOutputStream.close();
                                }
                            }
                            throw th3;
                        }
                    } finally {
                    }
                } catch (IOException e3) {
                    terminate();
                    throw new ActivationException("unable to create activation group", e3);
                }
            } catch (Throwable th5) {
                if (z2) {
                    Activation.this.Vstartgroup();
                }
                throw th5;
            }
        }

        /* loaded from: rt.jar:sun/rmi/server/Activation$GroupEntry$Watchdog.class */
        private class Watchdog extends Thread {
            private final Process groupProcess;
            private final long groupIncarnation;
            private boolean canInterrupt;
            private boolean shouldQuit;
            private boolean shouldRestart;

            Watchdog() {
                super("WatchDog-" + GroupEntry.this.groupName + LanguageTag.SEP + GroupEntry.this.incarnation);
                this.groupProcess = GroupEntry.this.child;
                this.groupIncarnation = GroupEntry.this.incarnation;
                this.canInterrupt = true;
                this.shouldQuit = false;
                this.shouldRestart = true;
                setDaemon(true);
            }

            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                if (this.shouldQuit) {
                    return;
                }
                try {
                    this.groupProcess.waitFor();
                    boolean z2 = false;
                    synchronized (GroupEntry.this) {
                        if (this.shouldQuit) {
                            return;
                        }
                        this.canInterrupt = false;
                        interrupted();
                        if (this.groupIncarnation == GroupEntry.this.incarnation) {
                            z2 = this.shouldRestart && !Activation.this.shuttingDown;
                            GroupEntry.this.reset();
                            GroupEntry.this.childGone();
                        }
                        if (z2) {
                            GroupEntry.this.restartServices();
                        }
                    }
                } catch (InterruptedException e2) {
                }
            }

            void dispose() {
                this.shouldQuit = true;
                if (this.canInterrupt) {
                    interrupt();
                }
            }

            void noRestart() {
                this.shouldRestart = false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String[] activationArgs(ActivationGroupDesc activationGroupDesc) {
        ActivationGroupDesc.CommandEnvironment commandEnvironment = activationGroupDesc.getCommandEnvironment();
        ArrayList arrayList = new ArrayList();
        arrayList.add((commandEnvironment == null || commandEnvironment.getCommandPath() == null) ? this.command[0] : commandEnvironment.getCommandPath());
        if (commandEnvironment != null && commandEnvironment.getCommandOptions() != null) {
            arrayList.addAll(Arrays.asList(commandEnvironment.getCommandOptions()));
        }
        Properties propertyOverrides = activationGroupDesc.getPropertyOverrides();
        if (propertyOverrides != null) {
            Enumeration<?> enumerationPropertyNames = propertyOverrides.propertyNames();
            while (enumerationPropertyNames.hasMoreElements()) {
                String str = (String) enumerationPropertyNames.nextElement2();
                arrayList.add("-D" + str + "=" + propertyOverrides.getProperty(str));
            }
        }
        for (int i2 = 1; i2 < this.command.length; i2++) {
            arrayList.add(this.command[i2]);
        }
        String[] strArr = new String[arrayList.size()];
        System.arraycopy(arrayList.toArray(), 0, strArr, 0, strArr.length);
        return strArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkArgs(ActivationGroupDesc activationGroupDesc, String[] strArr) throws ActivationException, SecurityException {
        if (execPolicyMethod != null) {
            if (strArr == null) {
                strArr = activationArgs(activationGroupDesc);
            }
            try {
                execPolicyMethod.invoke(execPolicy, activationGroupDesc, strArr);
            } catch (InvocationTargetException e2) {
                Throwable targetException = e2.getTargetException();
                if (targetException instanceof SecurityException) {
                    throw ((SecurityException) targetException);
                }
                throw new ActivationException(execPolicyMethod.getName() + ": unexpected exception", e2);
            } catch (Exception e3) {
                throw new ActivationException(execPolicyMethod.getName() + ": unexpected exception", e3);
            }
        }
    }

    /* loaded from: rt.jar:sun/rmi/server/Activation$ObjectEntry.class */
    private static class ObjectEntry implements Serializable {
        private static final long serialVersionUID = -5500114225321357856L;
        ActivationDesc desc;
        volatile transient MarshalledObject<? extends Remote> stub = null;
        volatile transient boolean removed = false;

        ObjectEntry(ActivationDesc activationDesc) {
            this.desc = activationDesc;
        }

        synchronized MarshalledObject<? extends Remote> activate(ActivationID activationID, boolean z2, ActivationInstantiator activationInstantiator) throws ActivationException, RemoteException {
            MarshalledObject<? extends Remote> marshalledObject = this.stub;
            if (this.removed) {
                throw new UnknownObjectException("object removed");
            }
            if (!z2 && marshalledObject != null) {
                return marshalledObject;
            }
            MarshalledObject<? extends Remote> marshalledObjectNewInstance = activationInstantiator.newInstance(activationID, this.desc);
            this.stub = marshalledObjectNewInstance;
            return marshalledObjectNewInstance;
        }

        void reset() {
            this.stub = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addLogRecord(LogRecord logRecord) throws ActivationException {
        synchronized (this.log) {
            checkShutdown();
            try {
                this.log.update(logRecord, true);
            } catch (Exception e2) {
                this.numUpdates = snapshotInterval;
                System.err.println(getTextResource("rmid.log.update.warning"));
                e2.printStackTrace();
            }
            int i2 = this.numUpdates + 1;
            this.numUpdates = i2;
            if (i2 < snapshotInterval) {
                return;
            }
            try {
                this.log.snapshot(this);
                this.numUpdates = 0;
            } catch (Exception e3) {
                System.err.println(getTextResource("rmid.log.snapshot.warning"));
                e3.printStackTrace();
                try {
                    this.system.shutdown();
                } catch (RemoteException e4) {
                }
                throw new ActivationException("log snapshot failed", e3);
            }
        }
    }

    /* loaded from: rt.jar:sun/rmi/server/Activation$ActLogHandler.class */
    private static class ActLogHandler extends LogHandler {
        ActLogHandler() {
        }

        @Override // sun.rmi.log.LogHandler
        public Object initialSnapshot() {
            return new Activation();
        }

        @Override // sun.rmi.log.LogHandler
        public Object applyUpdate(Object obj, Object obj2) throws Exception {
            return ((LogRecord) obj).apply(obj2);
        }
    }

    /* loaded from: rt.jar:sun/rmi/server/Activation$LogRecord.class */
    private static abstract class LogRecord implements Serializable {
        private static final long serialVersionUID = 8395140512322687529L;

        abstract Object apply(Object obj) throws Exception;

        private LogRecord() {
        }
    }

    /* loaded from: rt.jar:sun/rmi/server/Activation$LogRegisterObject.class */
    private static class LogRegisterObject extends LogRecord {
        private static final long serialVersionUID = -6280336276146085143L;
        private ActivationID id;
        private ActivationDesc desc;

        LogRegisterObject(ActivationID activationID, ActivationDesc activationDesc) {
            super();
            this.id = activationID;
            this.desc = activationDesc;
        }

        @Override // sun.rmi.server.Activation.LogRecord
        Object apply(Object obj) {
            try {
                ((Activation) obj).getGroupEntry(this.desc.getGroupID()).registerObject(this.id, this.desc, false);
            } catch (Exception e2) {
                System.err.println(MessageFormat.format(Activation.getTextResource("rmid.log.recover.warning"), "LogRegisterObject"));
                e2.printStackTrace();
            }
            return obj;
        }
    }

    /* loaded from: rt.jar:sun/rmi/server/Activation$LogUnregisterObject.class */
    private static class LogUnregisterObject extends LogRecord {
        private static final long serialVersionUID = 6269824097396935501L;
        private ActivationID id;

        LogUnregisterObject(ActivationID activationID) {
            super();
            this.id = activationID;
        }

        @Override // sun.rmi.server.Activation.LogRecord
        Object apply(Object obj) {
            try {
                ((Activation) obj).getGroupEntry(this.id).unregisterObject(this.id, false);
            } catch (Exception e2) {
                System.err.println(MessageFormat.format(Activation.getTextResource("rmid.log.recover.warning"), "LogUnregisterObject"));
                e2.printStackTrace();
            }
            return obj;
        }
    }

    /* loaded from: rt.jar:sun/rmi/server/Activation$LogRegisterGroup.class */
    private static class LogRegisterGroup extends LogRecord {
        private static final long serialVersionUID = -1966827458515403625L;
        private ActivationGroupID id;
        private ActivationGroupDesc desc;

        LogRegisterGroup(ActivationGroupID activationGroupID, ActivationGroupDesc activationGroupDesc) {
            super();
            this.id = activationGroupID;
            this.desc = activationGroupDesc;
        }

        @Override // sun.rmi.server.Activation.LogRecord
        Object apply(Object obj) {
            Map map = ((Activation) obj).groupTable;
            ActivationGroupID activationGroupID = this.id;
            Activation activation = (Activation) obj;
            activation.getClass();
            map.put(activationGroupID, activation.new GroupEntry(this.id, this.desc));
            return obj;
        }
    }

    /* loaded from: rt.jar:sun/rmi/server/Activation$LogUpdateDesc.class */
    private static class LogUpdateDesc extends LogRecord {
        private static final long serialVersionUID = 545511539051179885L;
        private ActivationID id;
        private ActivationDesc desc;

        LogUpdateDesc(ActivationID activationID, ActivationDesc activationDesc) {
            super();
            this.id = activationID;
            this.desc = activationDesc;
        }

        @Override // sun.rmi.server.Activation.LogRecord
        Object apply(Object obj) {
            try {
                ((Activation) obj).getGroupEntry(this.id).setActivationDesc(this.id, this.desc, false);
            } catch (Exception e2) {
                System.err.println(MessageFormat.format(Activation.getTextResource("rmid.log.recover.warning"), "LogUpdateDesc"));
                e2.printStackTrace();
            }
            return obj;
        }
    }

    /* loaded from: rt.jar:sun/rmi/server/Activation$LogUpdateGroupDesc.class */
    private static class LogUpdateGroupDesc extends LogRecord {
        private static final long serialVersionUID = -1271300989218424337L;
        private ActivationGroupID id;
        private ActivationGroupDesc desc;

        LogUpdateGroupDesc(ActivationGroupID activationGroupID, ActivationGroupDesc activationGroupDesc) {
            super();
            this.id = activationGroupID;
            this.desc = activationGroupDesc;
        }

        @Override // sun.rmi.server.Activation.LogRecord
        Object apply(Object obj) {
            try {
                ((Activation) obj).getGroupEntry(this.id).setActivationGroupDesc(this.id, this.desc, false);
            } catch (Exception e2) {
                System.err.println(MessageFormat.format(Activation.getTextResource("rmid.log.recover.warning"), "LogUpdateGroupDesc"));
                e2.printStackTrace();
            }
            return obj;
        }
    }

    /* loaded from: rt.jar:sun/rmi/server/Activation$LogUnregisterGroup.class */
    private static class LogUnregisterGroup extends LogRecord {
        private static final long serialVersionUID = -3356306586522147344L;
        private ActivationGroupID id;

        LogUnregisterGroup(ActivationGroupID activationGroupID) {
            super();
            this.id = activationGroupID;
        }

        @Override // sun.rmi.server.Activation.LogRecord
        Object apply(Object obj) {
            try {
                ((GroupEntry) ((Activation) obj).groupTable.remove(this.id)).unregisterGroup(false);
            } catch (Exception e2) {
                System.err.println(MessageFormat.format(Activation.getTextResource("rmid.log.recover.warning"), "LogUnregisterGroup"));
                e2.printStackTrace();
            }
            return obj;
        }
    }

    /* loaded from: rt.jar:sun/rmi/server/Activation$LogGroupIncarnation.class */
    private static class LogGroupIncarnation extends LogRecord {
        private static final long serialVersionUID = 4146872747377631897L;
        private ActivationGroupID id;
        private long inc;

        LogGroupIncarnation(ActivationGroupID activationGroupID, long j2) {
            super();
            this.id = activationGroupID;
            this.inc = j2;
        }

        @Override // sun.rmi.server.Activation.LogRecord
        Object apply(Object obj) {
            try {
                ((Activation) obj).getGroupEntry(this.id).incarnation = this.inc;
            } catch (Exception e2) {
                System.err.println(MessageFormat.format(Activation.getTextResource("rmid.log.recover.warning"), "LogGroupIncarnation"));
                e2.printStackTrace();
            }
            return obj;
        }
    }

    private void initCommand(String[] strArr) {
        this.command = new String[strArr.length + 2];
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.rmi.server.Activation.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                try {
                    Activation.this.command[0] = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
                    return null;
                } catch (Exception e2) {
                    System.err.println(Activation.getTextResource("rmid.unfound.java.home.property"));
                    Activation.this.command[0] = "java";
                    return null;
                }
            }
        });
        System.arraycopy(strArr, 0, this.command, 1, strArr.length);
        this.command[this.command.length - 1] = "sun.rmi.server.ActivationGroupInit";
    }

    private static void bomb(String str) {
        System.err.println("rmid: " + str);
        System.err.println(MessageFormat.format(getTextResource("rmid.usage"), "rmid"));
        System.exit(1);
    }

    /* loaded from: rt.jar:sun/rmi/server/Activation$DefaultExecPolicy.class */
    public static class DefaultExecPolicy {
        public void checkExecCommand(ActivationGroupDesc activationGroupDesc, String[] strArr) throws SecurityException {
            PermissionCollection execPermissions = getExecPermissions();
            Properties propertyOverrides = activationGroupDesc.getPropertyOverrides();
            if (propertyOverrides != null) {
                Enumeration<?> enumerationPropertyNames = propertyOverrides.propertyNames();
                while (enumerationPropertyNames.hasMoreElements()) {
                    String str = (String) enumerationPropertyNames.nextElement2();
                    String property = propertyOverrides.getProperty(str);
                    try {
                        checkPermission(execPermissions, new ExecOptionPermission("-D" + str + "=" + property));
                    } catch (AccessControlException e2) {
                        if (property.equals("")) {
                            checkPermission(execPermissions, new ExecOptionPermission("-D" + str));
                        } else {
                            throw e2;
                        }
                    }
                }
            }
            String className = activationGroupDesc.getClassName();
            if ((className != null && !className.equals(ActivationGroupImpl.class.getName())) || activationGroupDesc.getLocation() != null || activationGroupDesc.getData() != null) {
                throw new AccessControlException("access denied (custom group implementation not allowed)");
            }
            ActivationGroupDesc.CommandEnvironment commandEnvironment = activationGroupDesc.getCommandEnvironment();
            if (commandEnvironment != null) {
                String commandPath = commandEnvironment.getCommandPath();
                if (commandPath != null) {
                    checkPermission(execPermissions, new ExecPermission(commandPath));
                }
                String[] commandOptions = commandEnvironment.getCommandOptions();
                if (commandOptions != null) {
                    for (String str2 : commandOptions) {
                        checkPermission(execPermissions, new ExecOptionPermission(str2));
                    }
                }
            }
        }

        static void checkConfiguration() {
            if (!(((Policy) AccessController.doPrivileged(new PrivilegedAction<Policy>() { // from class: sun.rmi.server.Activation.DefaultExecPolicy.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Policy run2() {
                    return Policy.getPolicy();
                }
            })) instanceof PolicyFile)) {
                return;
            }
            Enumeration<Permission> enumerationElements = getExecPermissions().elements();
            while (enumerationElements.hasMoreElements()) {
                Permission permissionNextElement2 = enumerationElements.nextElement2();
                if ((permissionNextElement2 instanceof AllPermission) || (permissionNextElement2 instanceof ExecPermission) || (permissionNextElement2 instanceof ExecOptionPermission)) {
                    return;
                }
            }
            System.err.println(Activation.getTextResource("rmid.exec.perms.inadequate"));
        }

        private static PermissionCollection getExecPermissions() {
            return (PermissionCollection) AccessController.doPrivileged(new PrivilegedAction<PermissionCollection>() { // from class: sun.rmi.server.Activation.DefaultExecPolicy.2
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
        }

        private static void checkPermission(PermissionCollection permissionCollection, Permission permission) throws AccessControlException {
            if (!permissionCollection.implies(permission)) {
                throw new AccessControlException("access denied " + permission.toString());
            }
        }
    }

    public static void main(String[] strArr) {
        boolean z2 = false;
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            int localPort = 1098;
            ActivationServerSocketFactory activationServerSocketFactory = null;
            Channel channel = (Channel) AccessController.doPrivileged(new PrivilegedExceptionAction<Channel>() { // from class: sun.rmi.server.Activation.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public Channel run() throws IOException {
                    return System.inheritedChannel();
                }
            });
            if (channel != null && (channel instanceof ServerSocketChannel)) {
                AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() { // from class: sun.rmi.server.Activation.3
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public Void run() throws IOException {
                        System.setErr(new PrintStream(new FileOutputStream(Files.createTempFile("rmid-err", null, new FileAttribute[0]).toFile())));
                        return null;
                    }
                });
                ServerSocket serverSocketSocket = ((ServerSocketChannel) channel).socket();
                localPort = serverSocketSocket.getLocalPort();
                activationServerSocketFactory = new ActivationServerSocketFactory(serverSocketSocket);
                System.err.println(new Date());
                System.err.println(getTextResource("rmid.inherited.channel.info") + ": " + ((Object) channel));
            }
            String str = null;
            ArrayList arrayList = new ArrayList();
            int i2 = 0;
            while (i2 < strArr.length) {
                if (strArr[i2].equals("-port")) {
                    if (activationServerSocketFactory != null) {
                        bomb(getTextResource("rmid.syntax.port.badarg"));
                    }
                    if (i2 + 1 < strArr.length) {
                        try {
                            i2++;
                            localPort = Integer.parseInt(strArr[i2]);
                        } catch (NumberFormatException e2) {
                            bomb(getTextResource("rmid.syntax.port.badnumber"));
                        }
                    } else {
                        bomb(getTextResource("rmid.syntax.port.missing"));
                    }
                } else if (strArr[i2].equals("-log")) {
                    if (i2 + 1 < strArr.length) {
                        i2++;
                        str = strArr[i2];
                    } else {
                        bomb(getTextResource("rmid.syntax.log.missing"));
                    }
                } else if (strArr[i2].equals("-stop")) {
                    z2 = true;
                } else if (strArr[i2].startsWith("-C")) {
                    arrayList.add(strArr[i2].substring(2));
                } else {
                    bomb(MessageFormat.format(getTextResource("rmid.syntax.illegal.option"), strArr[i2]));
                }
                i2++;
            }
            if (str == null) {
                if (activationServerSocketFactory != null) {
                    bomb(getTextResource("rmid.syntax.log.required"));
                } else {
                    str = "log";
                }
            }
            debugExec = ((Boolean) AccessController.doPrivileged(new GetBooleanAction("sun.rmi.server.activation.debugExec"))).booleanValue();
            String name = (String) AccessController.doPrivileged(new GetPropertyAction("sun.rmi.activation.execPolicy", null));
            if (name == null) {
                if (!z2) {
                    DefaultExecPolicy.checkConfiguration();
                }
                name = "default";
            }
            if (!name.equals(Separation.COLORANT_NONE)) {
                if (name.equals("") || name.equals("default")) {
                    name = DefaultExecPolicy.class.getName();
                }
                try {
                    Class<?> rMIClass = getRMIClass(name);
                    execPolicy = rMIClass.newInstance();
                    execPolicyMethod = rMIClass.getMethod("checkExecCommand", ActivationGroupDesc.class, String[].class);
                } catch (Exception e3) {
                    if (debugExec) {
                        System.err.println(getTextResource("rmid.exec.policy.exception"));
                        e3.printStackTrace();
                    }
                    bomb(getTextResource("rmid.exec.policy.invalid"));
                }
            }
            if (z2) {
                final int i3 = localPort;
                AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.rmi.server.Activation.4
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public Void run2() {
                        System.setProperty("java.rmi.activation.port", Integer.toString(i3));
                        return null;
                    }
                });
                ActivationGroup.getSystem().shutdown();
                System.exit(0);
            }
            startActivation(localPort, activationServerSocketFactory, str, (String[]) arrayList.toArray(new String[arrayList.size()]));
            while (true) {
                try {
                    Thread.sleep(Long.MAX_VALUE);
                } catch (InterruptedException e4) {
                }
            }
        } catch (Exception e5) {
            System.err.println(MessageFormat.format(getTextResource("rmid.unexpected.exception"), e5));
            e5.printStackTrace();
            System.exit(1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getTextResource(String str) {
        if (resources == null) {
            try {
                resources = ResourceBundle.getBundle("sun.rmi.server.resources.rmid");
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

    private static Class<?> getRMIClass(String str) throws Exception {
        return RMIClassLoader.loadClass(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized String Pstartgroup() throws ActivationException {
        while (true) {
            checkShutdown();
            if (this.groupSemaphore > 0) {
                this.groupSemaphore--;
                StringBuilder sbAppend = new StringBuilder().append("Group-");
                int i2 = this.groupCounter;
                this.groupCounter = i2 + 1;
                return sbAppend.append(i2).toString();
            }
            try {
                wait();
            } catch (InterruptedException e2) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void Vstartgroup() {
        this.groupSemaphore++;
        notifyAll();
    }

    /* loaded from: rt.jar:sun/rmi/server/Activation$ActivationServerSocketFactory.class */
    private static class ActivationServerSocketFactory implements RMIServerSocketFactory {
        private final ServerSocket serverSocket;

        ActivationServerSocketFactory(ServerSocket serverSocket) {
            this.serverSocket = serverSocket;
        }

        @Override // java.rmi.server.RMIServerSocketFactory
        public ServerSocket createServerSocket(int i2) throws IOException {
            return new DelayedAcceptServerSocket(this.serverSocket);
        }
    }

    /* loaded from: rt.jar:sun/rmi/server/Activation$DelayedAcceptServerSocket.class */
    private static class DelayedAcceptServerSocket extends ServerSocket {
        private final ServerSocket serverSocket;

        DelayedAcceptServerSocket(ServerSocket serverSocket) throws IOException {
            this.serverSocket = serverSocket;
        }

        @Override // java.net.ServerSocket
        public void bind(SocketAddress socketAddress) throws IOException {
            this.serverSocket.bind(socketAddress);
        }

        @Override // java.net.ServerSocket
        public void bind(SocketAddress socketAddress, int i2) throws IOException {
            this.serverSocket.bind(socketAddress, i2);
        }

        @Override // java.net.ServerSocket
        public InetAddress getInetAddress() {
            return (InetAddress) AccessController.doPrivileged(new PrivilegedAction<InetAddress>() { // from class: sun.rmi.server.Activation.DelayedAcceptServerSocket.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public InetAddress run2() {
                    return DelayedAcceptServerSocket.this.serverSocket.getInetAddress();
                }
            });
        }

        @Override // java.net.ServerSocket
        public int getLocalPort() {
            return this.serverSocket.getLocalPort();
        }

        @Override // java.net.ServerSocket
        public SocketAddress getLocalSocketAddress() {
            return (SocketAddress) AccessController.doPrivileged(new PrivilegedAction<SocketAddress>() { // from class: sun.rmi.server.Activation.DelayedAcceptServerSocket.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public SocketAddress run2() {
                    return DelayedAcceptServerSocket.this.serverSocket.getLocalSocketAddress();
                }
            });
        }

        @Override // java.net.ServerSocket
        public Socket accept() throws IOException {
            synchronized (Activation.initLock) {
                while (!Activation.initDone) {
                    try {
                        Activation.initLock.wait();
                    } catch (InterruptedException e2) {
                        throw new AssertionError(e2);
                    }
                }
            }
            return this.serverSocket.accept();
        }

        @Override // java.net.ServerSocket, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this.serverSocket.close();
        }

        @Override // java.net.ServerSocket
        public ServerSocketChannel getChannel() {
            return this.serverSocket.getChannel();
        }

        @Override // java.net.ServerSocket
        public boolean isBound() {
            return this.serverSocket.isBound();
        }

        @Override // java.net.ServerSocket
        public boolean isClosed() {
            return this.serverSocket.isClosed();
        }

        @Override // java.net.ServerSocket
        public void setSoTimeout(int i2) throws SocketException {
            this.serverSocket.setSoTimeout(i2);
        }

        @Override // java.net.ServerSocket
        public int getSoTimeout() throws IOException {
            return this.serverSocket.getSoTimeout();
        }

        @Override // java.net.ServerSocket
        public void setReuseAddress(boolean z2) throws SocketException {
            this.serverSocket.setReuseAddress(z2);
        }

        @Override // java.net.ServerSocket
        public boolean getReuseAddress() throws SocketException {
            return this.serverSocket.getReuseAddress();
        }

        @Override // java.net.ServerSocket
        public String toString() {
            return this.serverSocket.toString();
        }

        @Override // java.net.ServerSocket
        public void setReceiveBufferSize(int i2) throws SocketException {
            this.serverSocket.setReceiveBufferSize(i2);
        }

        @Override // java.net.ServerSocket
        public int getReceiveBufferSize() throws SocketException {
            return this.serverSocket.getReceiveBufferSize();
        }
    }
}
