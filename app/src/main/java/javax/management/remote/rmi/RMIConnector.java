package javax.management.remote.rmi;

import com.sun.jmx.mbeanserver.Util;
import com.sun.jmx.remote.internal.ClientCommunicatorAdmin;
import com.sun.jmx.remote.internal.ClientListenerInfo;
import com.sun.jmx.remote.internal.ClientNotifForwarder;
import com.sun.jmx.remote.internal.IIOPHelper;
import com.sun.jmx.remote.internal.ProxyRef;
import com.sun.jmx.remote.util.ClassLogger;
import com.sun.jmx.remote.util.EnvHelp;
import com.sun.org.apache.bcel.internal.Constants;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidObjectException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.rmi.MarshalException;
import java.rmi.MarshalledObject;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.ServerException;
import java.rmi.UnmarshalException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RemoteObject;
import java.rmi.server.RemoteObjectInvocationHandler;
import java.rmi.server.RemoteRef;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.stream.Collectors;
import javafx.fxml.FXMLLoader;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerDelegate;
import javax.management.MBeanServerNotification;
import javax.management.NotCompliantMBeanException;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.NotificationFilter;
import javax.management.NotificationFilterSupport;
import javax.management.NotificationListener;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.ReflectionException;
import javax.management.remote.JMXAddressable;
import javax.management.remote.JMXConnectionNotification;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.NotificationResult;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.security.auth.Subject;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.reflect.misc.ReflectUtil;
import sun.rmi.server.UnicastRef2;
import sun.security.util.SecurityConstants;

/*  JADX ERROR: NullPointerException in pass: ClassModifier
    java.lang.NullPointerException
    */
/* loaded from: rt.jar:javax/management/remote/rmi/RMIConnector.class */
public class RMIConnector implements JMXConnector, Serializable, JMXAddressable {
    private static final long serialVersionUID = 817323035842634473L;
    private static final Class<?> rmiServerImplStubClass;
    private static final Class<?> rmiConnectionImplStubClass;
    private static final String pRefClassName = "com.sun.jmx.remote.internal.PRef";
    private static final Constructor<?> proxyRefConstructor;
    private static final String iiopConnectionStubClassName = "org.omg.stub.javax.management.remote.rmi._RMIConnection_Stub";
    private static final String proxyStubClassName = "com.sun.jmx.remote.protocol.iiop.ProxyStub";
    private static final String ProxyInputStreamClassName = "com.sun.jmx.remote.protocol.iiop.ProxyInputStream";
    private static final String pInputStreamClassName = "com.sun.jmx.remote.protocol.iiop.PInputStream";
    private static final Class<?> proxyStubClass;
    private static final byte[] base64ToInt;
    private final RMIServer rmiServer;
    private final JMXServiceURL jmxServiceURL;
    private transient Map<String, Object> env;
    private transient ClassLoader defaultClassLoader;
    private transient RMIConnection connection;
    private transient String connectionId;
    private transient long clientNotifSeqNo;
    private transient WeakHashMap<Subject, WeakReference<MBeanServerConnection>> rmbscMap;
    private transient WeakReference<MBeanServerConnection> nullSubjectConnRef;
    private transient RMINotifClient rmiNotifClient;
    private transient long clientNotifCounter;
    private transient boolean connected;
    private transient boolean terminated;
    private transient Exception closeException;
    private transient NotificationBroadcasterSupport connectionBroadcaster;
    private transient ClientCommunicatorAdmin communicatorAdmin;
    private static volatile WeakReference<Object> orb;
    private static final ClassLogger logger = new ClassLogger("javax.management.remote.rmi", "RMIConnector");
    private static final String rmiServerImplStubClassName = RMIServer.class.getName() + "Impl_Stub";
    private static final String rmiConnectionImplStubClassName = RMIConnection.class.getName() + "Impl_Stub";

    /*  JADX ERROR: Failed to decode insn: 0x0005: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[8]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ long access$1108(javax.management.remote.rmi.RMIConnector r8) {
        /*
            r0 = r8
            r1 = r0
            long r1 = r1.clientNotifSeqNo
            // decode failed: arraycopy: source index -1 out of bounds for object array[8]
            r2 = 1
            long r1 = r1 + r2
            r0.clientNotifSeqNo = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.management.remote.rmi.RMIConnector.access$1108(javax.management.remote.rmi.RMIConnector):long");
    }

    /*  JADX ERROR: Failed to decode insn: 0x0005: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[8]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ long access$1408(javax.management.remote.rmi.RMIConnector r8) {
        /*
            r0 = r8
            r1 = r0
            long r1 = r1.clientNotifCounter
            // decode failed: arraycopy: source index -1 out of bounds for object array[8]
            r2 = 1
            long r1 = r1 + r2
            r0.clientNotifCounter = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.management.remote.rmi.RMIConnector.access$1408(javax.management.remote.rmi.RMIConnector):long");
    }

    /* JADX WARN: Type inference failed for: r0v29, types: [byte[], byte[][]] */
    static {
        Class<?> cls;
        Class<?> cls2;
        Constructor<?> constructor;
        Class<?> cls3;
        final byte[] bArrStringToBytes = NoCallStackClassLoader.stringToBytes("Êþº¾������.��\u0017\n��\u0005��\r\t��\u0004��\u000e\u000b��\u000f��\u0010\u0007��\u0011\u0007��\u0012\u0001��\u0006<init>\u0001��\u001e(Ljava/rmi/server/RemoteRef;)V\u0001��\u0004Code\u0001��\u0006invoke\u0001��S(Ljava/rmi/Remote;Ljava/lang/reflect/Method;[Ljava/lang/Object;J)Ljava/lang/Object;\u0001��\nExceptions\u0007��\u0013\f��\u0006��\u0007\f��\u0014��\u0015\u0007��\u0016\f��\t��\n\u0001�� com/sun/jmx/remote/internal/PRef\u0001��$com/sun/jmx/remote/internal/ProxyRef\u0001��\u0013java/lang/Exception\u0001��\u0003ref\u0001��\u001bLjava/rmi/server/RemoteRef;\u0001��\u0019java/rmi/server/RemoteRef��!��\u0004��\u0005����������\u0002��\u0001��\u0006��\u0007��\u0001��\b������\u0012��\u0002��\u0002������\u0006*+·��\u0001±����������\u0001��\t��\n��\u0002��\b������\u001b��\u0006��\u0006������\u000f*´��\u0002+,-\u0016\u0004¹��\u0003\u0006��°����������\u000b������\u0004��\u0001��\f����");
        PrivilegedExceptionAction<Constructor<?>> privilegedExceptionAction = new PrivilegedExceptionAction<Constructor<?>>() { // from class: javax.management.remote.rmi.RMIConnector.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedExceptionAction
            public Constructor<?> run() throws Exception {
                return new NoCallStackClassLoader(RMIConnector.pRefClassName, bArrStringToBytes, new String[]{ProxyRef.class.getName()}, RMIConnector.class.getClassLoader(), RMIConnector.class.getProtectionDomain()).loadClass(RMIConnector.pRefClassName).getConstructor(RemoteRef.class);
            }
        };
        try {
            cls = Class.forName(rmiServerImplStubClassName);
        } catch (Exception e2) {
            logger.error(Constants.STATIC_INITIALIZER_NAME, "Failed to instantiate " + rmiServerImplStubClassName + ": " + ((Object) e2));
            logger.debug(Constants.STATIC_INITIALIZER_NAME, e2);
            cls = null;
        }
        rmiServerImplStubClass = cls;
        try {
            cls2 = Class.forName(rmiConnectionImplStubClassName);
            constructor = (Constructor) AccessController.doPrivileged(privilegedExceptionAction);
        } catch (Exception e3) {
            logger.error(Constants.STATIC_INITIALIZER_NAME, "Failed to initialize proxy reference constructor for " + rmiConnectionImplStubClassName + ": " + ((Object) e3));
            logger.debug(Constants.STATIC_INITIALIZER_NAME, e3);
            cls2 = null;
            constructor = null;
        }
        rmiConnectionImplStubClass = cls2;
        proxyRefConstructor = constructor;
        byte[] bArrStringToBytes2 = NoCallStackClassLoader.stringToBytes("Êþº¾������3��+\n��\f��\u0018\u0007��\u0019\n��\f��\u001a\n��\u0002��\u001b\u0007��\u001c\n��\u0005��\u001d\n��\u0005��\u001e\n��\u0005��\u001f\n��\u0002�� \n��\f��!\u0007��\"\u0007��#\u0001��\u0006<init>\u0001��\u0003()V\u0001��\u0004Code\u0001��\u0007_invoke\u0001��K(Lorg/omg/CORBA/portable/OutputStream;)Lorg/omg/CORBA/portable/InputStream;\u0001��\rStackMapTable\u0007��\u001c\u0001��\nExceptions\u0007��$\u0001��\r_releaseReply\u0001��'(Lorg/omg/CORBA/portable/InputStream;)V\f��\r��\u000e\u0001��-com/sun/jmx/remote/protocol/iiop/PInputStream\f��\u0010��\u0011\f��\r��\u0017\u0001��+org/omg/CORBA/portable/ApplicationException\f��%��&\f��'��(\f��\r��)\f��*��&\f��\u0016��\u0017\u0001��*com/sun/jmx/remote/protocol/iiop/ProxyStub\u0001��<org/omg/stub/javax/management/remote/rmi/_RMIConnection_Stub\u0001��)org/omg/CORBA/portable/RemarshalException\u0001��\u000egetInputStream\u0001��&()Lorg/omg/CORBA/portable/InputStream;\u0001��\u0005getId\u0001��\u0014()Ljava/lang/String;\u0001��9(Ljava/lang/String;Lorg/omg/CORBA/portable/InputStream;)V\u0001��\u0015getProxiedInputStream��!��\u000b��\f����������\u0003��\u0001��\r��\u000e��\u0001��\u000f������\u0011��\u0001��\u0001������\u0005*·��\u0001±����������\u0001��\u0010��\u0011��\u0002��\u000f������G��\u0004��\u0004������'»��\u0002Y*+·��\u0003·��\u0004°M»��\u0002Y,¶��\u0006·��\u0004N»��\u0005Y,¶��\u0007-·��\b¿��\u0001������\f��\r��\u0005��\u0001��\u0012������\u0006��\u0001M\u0007��\u0013��\u0014������\u0006��\u0002��\u0005��\u0015��\u0001��\u0016��\u0017��\u0001��\u000f������'��\u0002��\u0002������\u0012+Æ��\u000b+À��\u0002¶��\tL*+·��\n±������\u0001��\u0012������\u0003��\u0001\f����");
        byte[] bArrStringToBytes3 = NoCallStackClassLoader.stringToBytes("Êþº¾������3��\u001e\n��\u0007��\u000f\t��\u0006��\u0010\n��\u0011��\u0012\n��\u0006��\u0013\n��\u0014��\u0015\u0007��\u0016\u0007��\u0017\u0001��\u0006<init>\u0001��'(Lorg/omg/CORBA/portable/InputStream;)V\u0001��\u0004Code\u0001��\bread_any\u0001��\u0015()Lorg/omg/CORBA/Any;\u0001��\nread_value\u0001��)(Ljava/lang/Class;)Ljava/io/Serializable;\f��\b��\t\f��\u0018��\u0019\u0007��\u001a\f��\u000b��\f\f��\u001b��\u001c\u0007��\u001d\f��\r��\u000e\u0001��-com/sun/jmx/remote/protocol/iiop/PInputStream\u0001��1com/sun/jmx/remote/protocol/iiop/ProxyInputStream\u0001��\u0002in\u0001��$Lorg/omg/CORBA/portable/InputStream;\u0001��\"org/omg/CORBA/portable/InputStream\u0001��\u0006narrow\u0001��*()Lorg/omg/CORBA_2_3/portable/InputStream;\u0001��&org/omg/CORBA_2_3/portable/InputStream��!��\u0006��\u0007����������\u0003��\u0001��\b��\t��\u0001��\n������\u0012��\u0002��\u0002������\u0006*+·��\u0001±����������\u0001��\u000b��\f��\u0001��\n������\u0014��\u0001��\u0001������\b*´��\u0002¶��\u0003°����������\u0001��\r��\u000e��\u0001��\n������\u0015��\u0002��\u0002������\t*¶��\u0004+¶��\u0005°������������");
        final String[] strArr = {proxyStubClassName, pInputStreamClassName};
        final ?? r0 = {bArrStringToBytes2, bArrStringToBytes3};
        final String[] strArr2 = {iiopConnectionStubClassName, ProxyInputStreamClassName};
        if (IIOPHelper.isAvailable()) {
            try {
                cls3 = (Class) AccessController.doPrivileged(new PrivilegedExceptionAction<Class<?>>() { // from class: javax.management.remote.rmi.RMIConnector.2
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public Class<?> run() throws Exception {
                        return new NoCallStackClassLoader(strArr, r0, strArr2, RMIConnector.class.getClassLoader(), RMIConnector.class.getProtectionDomain()).loadClass(RMIConnector.proxyStubClassName);
                    }
                });
            } catch (Exception e4) {
                logger.error(Constants.STATIC_INITIALIZER_NAME, "Unexpected exception making shadow IIOP stub class: " + ((Object) e4));
                logger.debug(Constants.STATIC_INITIALIZER_NAME, e4);
                cls3 = null;
            }
            proxyStubClass = cls3;
        } else {
            proxyStubClass = null;
        }
        base64ToInt = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51};
        orb = null;
    }

    private RMIConnector(RMIServer rMIServer, JMXServiceURL jMXServiceURL, Map<String, ?> map) {
        this.clientNotifSeqNo = 0L;
        this.nullSubjectConnRef = null;
        this.clientNotifCounter = 0L;
        if (rMIServer == null && jMXServiceURL == null) {
            throw new IllegalArgumentException("rmiServer and jmxServiceURL both null");
        }
        initTransients();
        this.rmiServer = rMIServer;
        this.jmxServiceURL = jMXServiceURL;
        if (map == null) {
            this.env = Collections.emptyMap();
        } else {
            EnvHelp.checkAttributes(map);
            this.env = Collections.unmodifiableMap(map);
        }
    }

    public RMIConnector(JMXServiceURL jMXServiceURL, Map<String, ?> map) {
        this(null, jMXServiceURL, map);
    }

    public RMIConnector(RMIServer rMIServer, Map<String, ?> map) {
        this(rMIServer, null, map);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getName());
        sb.append(CallSiteDescriptor.TOKEN_DELIMITER);
        if (this.rmiServer != null) {
            sb.append(" rmiServer=").append(this.rmiServer.toString());
        }
        if (this.jmxServiceURL != null) {
            if (this.rmiServer != null) {
                sb.append(",");
            }
            sb.append(" jmxServiceURL=").append(this.jmxServiceURL.toString());
        }
        return sb.toString();
    }

    @Override // javax.management.remote.JMXAddressable
    public JMXServiceURL getAddress() {
        return this.jmxServiceURL;
    }

    @Override // javax.management.remote.JMXConnector
    public void connect() throws IOException {
        connect(null);
    }

    @Override // javax.management.remote.JMXConnector
    public synchronized void connect(Map<String, ?> map) throws IOException {
        boolean zTraceOn = logger.traceOn();
        String str = zTraceOn ? "[" + toString() + "]" : null;
        if (this.terminated) {
            logger.trace(SecurityConstants.SOCKET_CONNECT_ACTION, str + " already closed.");
            throw new IOException("Connector closed");
        }
        if (this.connected) {
            logger.trace(SecurityConstants.SOCKET_CONNECT_ACTION, str + " already connected.");
            return;
        }
        if (zTraceOn) {
            try {
                logger.trace(SecurityConstants.SOCKET_CONNECT_ACTION, str + " connecting...");
            } catch (IOException e2) {
                if (zTraceOn) {
                    logger.trace(SecurityConstants.SOCKET_CONNECT_ACTION, str + " failed to connect: " + ((Object) e2));
                }
                throw e2;
            } catch (RuntimeException e3) {
                if (zTraceOn) {
                    logger.trace(SecurityConstants.SOCKET_CONNECT_ACTION, str + " failed to connect: " + ((Object) e3));
                }
                throw e3;
            } catch (NamingException e4) {
                String str2 = "Failed to retrieve RMIServer stub: " + ((Object) e4);
                if (zTraceOn) {
                    logger.trace(SecurityConstants.SOCKET_CONNECT_ACTION, str + " " + str2);
                }
                throw ((IOException) EnvHelp.initCause(new IOException(str2), e4));
            }
        }
        HashMap map2 = new HashMap(this.env == null ? Collections.emptyMap() : this.env);
        if (map != null) {
            EnvHelp.checkAttributes(map);
            map2.putAll(map);
        }
        if (zTraceOn) {
            logger.trace(SecurityConstants.SOCKET_CONNECT_ACTION, str + " finding stub...");
        }
        RMIServer rMIServerFindRMIServer = this.rmiServer != null ? this.rmiServer : findRMIServer(this.jmxServiceURL, map2);
        boolean zComputeBooleanFromString = EnvHelp.computeBooleanFromString((String) map2.get("jmx.remote.x.check.stub"));
        if (zComputeBooleanFromString) {
            checkStub(rMIServerFindRMIServer, rmiServerImplStubClass);
        }
        if (zTraceOn) {
            logger.trace(SecurityConstants.SOCKET_CONNECT_ACTION, str + " connecting stub...");
        }
        RMIServer rMIServerConnectStub = connectStub(rMIServerFindRMIServer, map2);
        String str3 = zTraceOn ? "[" + toString() + "]" : null;
        if (zTraceOn) {
            logger.trace(SecurityConstants.SOCKET_CONNECT_ACTION, str3 + " getting connection...");
        }
        try {
            this.connection = getConnection(rMIServerConnectStub, map2.get(JMXConnector.CREDENTIALS), zComputeBooleanFromString);
            if (zTraceOn) {
                logger.trace(SecurityConstants.SOCKET_CONNECT_ACTION, str3 + " getting class loader...");
            }
            this.defaultClassLoader = EnvHelp.resolveClientClassLoader(map2);
            map2.put("jmx.remote.default.class.loader", this.defaultClassLoader);
            this.rmiNotifClient = new RMINotifClient(this.defaultClassLoader, map2);
            this.env = map2;
            this.communicatorAdmin = new RMIClientCommunicatorAdmin(EnvHelp.getConnectionCheckPeriod(map2));
            this.connected = true;
            this.connectionId = getConnectionId();
            String str4 = this.connectionId;
            long j2 = this.clientNotifSeqNo;
            this.clientNotifSeqNo = j2 + 1;
            sendNotification(new JMXConnectionNotification(JMXConnectionNotification.OPENED, this, str4, j2, "Successful connection", null));
            if (zTraceOn) {
                logger.trace(SecurityConstants.SOCKET_CONNECT_ACTION, str3 + " done...");
            }
        } catch (RemoteException e5) {
            if (this.jmxServiceURL != null) {
                String protocol = this.jmxServiceURL.getProtocol();
                String uRLPath = this.jmxServiceURL.getURLPath();
                if ("rmi".equals(protocol) && uRLPath.startsWith("/jndi/iiop:")) {
                    MalformedURLException malformedURLException = new MalformedURLException("Protocol is rmi but JNDI scheme is iiop: " + ((Object) this.jmxServiceURL));
                    malformedURLException.initCause(e5);
                    throw malformedURLException;
                }
            }
            throw e5;
        }
    }

    @Override // javax.management.remote.JMXConnector
    public synchronized String getConnectionId() throws IOException {
        if (this.terminated || !this.connected) {
            if (logger.traceOn()) {
                logger.trace("getConnectionId", "[" + toString() + "] not connected.");
            }
            throw new IOException("Not connected");
        }
        return this.connection.getConnectionId();
    }

    @Override // javax.management.remote.JMXConnector
    public synchronized MBeanServerConnection getMBeanServerConnection() throws IOException {
        return getMBeanServerConnection(null);
    }

    @Override // javax.management.remote.JMXConnector
    public synchronized MBeanServerConnection getMBeanServerConnection(Subject subject) throws IOException {
        if (this.terminated) {
            if (logger.traceOn()) {
                logger.trace("getMBeanServerConnection", "[" + toString() + "] already closed.");
            }
            throw new IOException("Connection closed");
        }
        if (!this.connected) {
            if (logger.traceOn()) {
                logger.trace("getMBeanServerConnection", "[" + toString() + "] is not connected.");
            }
            throw new IOException("Not connected");
        }
        return getConnectionWithSubject(subject);
    }

    @Override // javax.management.remote.JMXConnector
    public void addConnectionNotificationListener(NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) {
        if (notificationListener == null) {
            throw new NullPointerException("listener");
        }
        this.connectionBroadcaster.addNotificationListener(notificationListener, notificationFilter, obj);
    }

    @Override // javax.management.remote.JMXConnector
    public void removeConnectionNotificationListener(NotificationListener notificationListener) throws ListenerNotFoundException {
        if (notificationListener == null) {
            throw new NullPointerException("listener");
        }
        this.connectionBroadcaster.removeNotificationListener(notificationListener);
    }

    @Override // javax.management.remote.JMXConnector
    public void removeConnectionNotificationListener(NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws ListenerNotFoundException {
        if (notificationListener == null) {
            throw new NullPointerException("listener");
        }
        this.connectionBroadcaster.removeNotificationListener(notificationListener, notificationFilter, obj);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendNotification(Notification notification) {
        this.connectionBroadcaster.sendNotification(notification);
    }

    @Override // javax.management.remote.JMXConnector, java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() throws IOException {
        close(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void close(boolean z2) throws IOException {
        boolean zTraceOn = logger.traceOn();
        boolean zDebugOn = logger.debugOn();
        String str = zTraceOn ? "[" + toString() + "]" : null;
        if (!z2) {
            if (this.terminated) {
                if (this.closeException == null) {
                    if (zTraceOn) {
                        logger.trace("close", str + " already closed.");
                        return;
                    }
                    return;
                }
            } else {
                this.terminated = true;
            }
        }
        if (this.closeException != null && zTraceOn && zTraceOn) {
            logger.trace("close", str + " had failed: " + ((Object) this.closeException));
            logger.trace("close", str + " attempting to close again.");
        }
        String str2 = null;
        if (this.connected) {
            str2 = this.connectionId;
        }
        this.closeException = null;
        if (zTraceOn) {
            logger.trace("close", str + " closing.");
        }
        if (this.communicatorAdmin != null) {
            this.communicatorAdmin.terminate();
        }
        if (this.rmiNotifClient != null) {
            try {
                this.rmiNotifClient.terminate();
                if (zTraceOn) {
                    logger.trace("close", str + " RMI Notification client terminated.");
                }
            } catch (RuntimeException e2) {
                this.closeException = e2;
                if (zTraceOn) {
                    logger.trace("close", str + " Failed to terminate RMI Notification client: " + ((Object) e2));
                }
                if (zDebugOn) {
                    logger.debug("close", e2);
                }
            }
        }
        if (this.connection != null) {
            try {
                this.connection.close();
                if (zTraceOn) {
                    logger.trace("close", str + " closed.");
                }
            } catch (NoSuchObjectException e3) {
            } catch (IOException e4) {
                this.closeException = e4;
                if (zTraceOn) {
                    logger.trace("close", str + " Failed to close RMIServer: " + ((Object) e4));
                }
                if (zDebugOn) {
                    logger.debug("close", e4);
                }
            }
        }
        this.rmbscMap.clear();
        if (str2 != null) {
            long j2 = this.clientNotifSeqNo;
            this.clientNotifSeqNo = j2 + 1;
            sendNotification(new JMXConnectionNotification(JMXConnectionNotification.CLOSED, this, str2, j2, "Client has been closed", null));
        }
        if (this.closeException != null) {
            if (zTraceOn) {
                logger.trace("close", str + " failed to close: " + ((Object) this.closeException));
            }
            if (this.closeException instanceof IOException) {
                throw ((IOException) this.closeException);
            }
            if (this.closeException instanceof RuntimeException) {
                throw ((RuntimeException) this.closeException);
            }
            throw ((IOException) EnvHelp.initCause(new IOException("Failed to close: " + ((Object) this.closeException)), this.closeException));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Integer addListenerWithSubject(ObjectName objectName, MarshalledObject<NotificationFilter> marshalledObject, Subject subject, boolean z2) throws IOException, InstanceNotFoundException {
        boolean zDebugOn = logger.debugOn();
        if (zDebugOn) {
            logger.debug("addListenerWithSubject", "(ObjectName,MarshalledObject,Subject)");
        }
        Integer[] numArrAddListenersWithSubjects = addListenersWithSubjects(new ObjectName[]{objectName}, (MarshalledObject[]) Util.cast(new MarshalledObject[]{marshalledObject}), new Subject[]{subject}, z2);
        if (zDebugOn) {
            logger.debug("addListenerWithSubject", "listenerID=" + ((Object) numArrAddListenersWithSubjects[0]));
        }
        return numArrAddListenersWithSubjects[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Integer[] addListenersWithSubjects(ObjectName[] objectNameArr, MarshalledObject<NotificationFilter>[] marshalledObjectArr, Subject[] subjectArr, boolean z2) throws IOException, InstanceNotFoundException {
        boolean zDebugOn = logger.debugOn();
        if (zDebugOn) {
            logger.debug("addListenersWithSubjects", "(ObjectName[],MarshalledObject[],Subject[])");
        }
        ClassLoader classLoaderPushDefaultClassLoader = pushDefaultClassLoader();
        Integer[] numArrAddNotificationListeners = null;
        try {
            try {
                numArrAddNotificationListeners = this.connection.addNotificationListeners(objectNameArr, marshalledObjectArr, subjectArr);
                popDefaultClassLoader(classLoaderPushDefaultClassLoader);
            } catch (NoSuchObjectException e2) {
                if (!z2) {
                    throw e2;
                }
                this.communicatorAdmin.gotIOException(e2);
                numArrAddNotificationListeners = this.connection.addNotificationListeners(objectNameArr, marshalledObjectArr, subjectArr);
                popDefaultClassLoader(classLoaderPushDefaultClassLoader);
            } catch (IOException e3) {
                this.communicatorAdmin.gotIOException(e3);
                popDefaultClassLoader(classLoaderPushDefaultClassLoader);
            }
            if (zDebugOn) {
                logger.debug("addListenersWithSubjects", "registered " + (numArrAddNotificationListeners == null ? 0 : numArrAddNotificationListeners.length) + " listener(s)");
            }
            return numArrAddNotificationListeners;
        } catch (Throwable th) {
            popDefaultClassLoader(classLoaderPushDefaultClassLoader);
            throw th;
        }
    }

    /* loaded from: rt.jar:javax/management/remote/rmi/RMIConnector$RemoteMBeanServerConnection.class */
    private class RemoteMBeanServerConnection implements MBeanServerConnection {
        private Subject delegationSubject;

        public RemoteMBeanServerConnection(RMIConnector rMIConnector) {
            this(null);
        }

        public RemoteMBeanServerConnection(Subject subject) {
            this.delegationSubject = subject;
        }

        @Override // javax.management.MBeanServerConnection
        public ObjectInstance createMBean(String str, ObjectName objectName) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, IOException, ReflectionException {
            if (RMIConnector.logger.debugOn()) {
                RMIConnector.logger.debug("createMBean(String,ObjectName)", "className=" + str + ", name=" + ((Object) objectName));
            }
            ClassLoader classLoaderPushDefaultClassLoader = RMIConnector.this.pushDefaultClassLoader();
            try {
                try {
                    ObjectInstance objectInstanceCreateMBean = RMIConnector.this.connection.createMBean(str, objectName, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                    return objectInstanceCreateMBean;
                } catch (IOException e2) {
                    RMIConnector.this.communicatorAdmin.gotIOException(e2);
                    ObjectInstance objectInstanceCreateMBean2 = RMIConnector.this.connection.createMBean(str, objectName, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                    return objectInstanceCreateMBean2;
                }
            } catch (Throwable th) {
                RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                throw th;
            }
        }

        @Override // javax.management.MBeanServerConnection
        public ObjectInstance createMBean(String str, ObjectName objectName, ObjectName objectName2) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, IOException, InstanceNotFoundException, ReflectionException {
            if (RMIConnector.logger.debugOn()) {
                RMIConnector.logger.debug("createMBean(String,ObjectName,ObjectName)", "className=" + str + ", name=" + ((Object) objectName) + ", loaderName=" + ((Object) objectName2) + ")");
            }
            ClassLoader classLoaderPushDefaultClassLoader = RMIConnector.this.pushDefaultClassLoader();
            try {
                try {
                    ObjectInstance objectInstanceCreateMBean = RMIConnector.this.connection.createMBean(str, objectName, objectName2, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                    return objectInstanceCreateMBean;
                } catch (IOException e2) {
                    RMIConnector.this.communicatorAdmin.gotIOException(e2);
                    ObjectInstance objectInstanceCreateMBean2 = RMIConnector.this.connection.createMBean(str, objectName, objectName2, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                    return objectInstanceCreateMBean2;
                }
            } catch (Throwable th) {
                RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                throw th;
            }
        }

        @Override // javax.management.MBeanServerConnection
        public ObjectInstance createMBean(String str, ObjectName objectName, Object[] objArr, String[] strArr) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, IOException, ReflectionException {
            if (RMIConnector.logger.debugOn()) {
                RMIConnector.logger.debug("createMBean(String,ObjectName,Object[],String[])", "className=" + str + ", name=" + ((Object) objectName) + ", signature=" + RMIConnector.strings(strArr));
            }
            MarshalledObject marshalledObject = new MarshalledObject(objArr);
            ClassLoader classLoaderPushDefaultClassLoader = RMIConnector.this.pushDefaultClassLoader();
            try {
                try {
                    ObjectInstance objectInstanceCreateMBean = RMIConnector.this.connection.createMBean(str, objectName, marshalledObject, strArr, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                    return objectInstanceCreateMBean;
                } catch (IOException e2) {
                    RMIConnector.this.communicatorAdmin.gotIOException(e2);
                    ObjectInstance objectInstanceCreateMBean2 = RMIConnector.this.connection.createMBean(str, objectName, marshalledObject, strArr, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                    return objectInstanceCreateMBean2;
                }
            } catch (Throwable th) {
                RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                throw th;
            }
        }

        @Override // javax.management.MBeanServerConnection
        public ObjectInstance createMBean(String str, ObjectName objectName, ObjectName objectName2, Object[] objArr, String[] strArr) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, IOException, InstanceNotFoundException, ReflectionException {
            if (RMIConnector.logger.debugOn()) {
                RMIConnector.logger.debug("createMBean(String,ObjectName,ObjectName,Object[],String[])", "className=" + str + ", name=" + ((Object) objectName) + ", loaderName=" + ((Object) objectName2) + ", signature=" + RMIConnector.strings(strArr));
            }
            MarshalledObject marshalledObject = new MarshalledObject(objArr);
            ClassLoader classLoaderPushDefaultClassLoader = RMIConnector.this.pushDefaultClassLoader();
            try {
                try {
                    ObjectInstance objectInstanceCreateMBean = RMIConnector.this.connection.createMBean(str, objectName, objectName2, marshalledObject, strArr, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                    return objectInstanceCreateMBean;
                } catch (IOException e2) {
                    RMIConnector.this.communicatorAdmin.gotIOException(e2);
                    ObjectInstance objectInstanceCreateMBean2 = RMIConnector.this.connection.createMBean(str, objectName, objectName2, marshalledObject, strArr, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                    return objectInstanceCreateMBean2;
                }
            } catch (Throwable th) {
                RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                throw th;
            }
        }

        @Override // javax.management.MBeanServerConnection
        public void unregisterMBean(ObjectName objectName) throws MBeanRegistrationException, IOException, InstanceNotFoundException {
            if (RMIConnector.logger.debugOn()) {
                RMIConnector.logger.debug("unregisterMBean", "name=" + ((Object) objectName));
            }
            ClassLoader classLoaderPushDefaultClassLoader = RMIConnector.this.pushDefaultClassLoader();
            try {
                try {
                    RMIConnector.this.connection.unregisterMBean(objectName, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                } catch (IOException e2) {
                    RMIConnector.this.communicatorAdmin.gotIOException(e2);
                    RMIConnector.this.connection.unregisterMBean(objectName, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                }
            } catch (Throwable th) {
                RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                throw th;
            }
        }

        @Override // javax.management.MBeanServerConnection
        public ObjectInstance getObjectInstance(ObjectName objectName) throws IOException, InstanceNotFoundException {
            if (RMIConnector.logger.debugOn()) {
                RMIConnector.logger.debug("getObjectInstance", "name=" + ((Object) objectName));
            }
            ClassLoader classLoaderPushDefaultClassLoader = RMIConnector.this.pushDefaultClassLoader();
            try {
                try {
                    ObjectInstance objectInstance = RMIConnector.this.connection.getObjectInstance(objectName, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                    return objectInstance;
                } catch (IOException e2) {
                    RMIConnector.this.communicatorAdmin.gotIOException(e2);
                    ObjectInstance objectInstance2 = RMIConnector.this.connection.getObjectInstance(objectName, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                    return objectInstance2;
                }
            } catch (Throwable th) {
                RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                throw th;
            }
        }

        @Override // javax.management.MBeanServerConnection
        public Set<ObjectInstance> queryMBeans(ObjectName objectName, QueryExp queryExp) throws IOException {
            if (RMIConnector.logger.debugOn()) {
                RMIConnector.logger.debug("queryMBeans", "name=" + ((Object) objectName) + ", query=" + ((Object) queryExp));
            }
            MarshalledObject marshalledObject = new MarshalledObject(queryExp);
            ClassLoader classLoaderPushDefaultClassLoader = RMIConnector.this.pushDefaultClassLoader();
            try {
                try {
                    Set<ObjectInstance> setQueryMBeans = RMIConnector.this.connection.queryMBeans(objectName, marshalledObject, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                    return setQueryMBeans;
                } catch (IOException e2) {
                    RMIConnector.this.communicatorAdmin.gotIOException(e2);
                    Set<ObjectInstance> setQueryMBeans2 = RMIConnector.this.connection.queryMBeans(objectName, marshalledObject, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                    return setQueryMBeans2;
                }
            } catch (Throwable th) {
                RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                throw th;
            }
        }

        @Override // javax.management.MBeanServerConnection
        public Set<ObjectName> queryNames(ObjectName objectName, QueryExp queryExp) throws IOException {
            if (RMIConnector.logger.debugOn()) {
                RMIConnector.logger.debug("queryNames", "name=" + ((Object) objectName) + ", query=" + ((Object) queryExp));
            }
            MarshalledObject marshalledObject = new MarshalledObject(queryExp);
            ClassLoader classLoaderPushDefaultClassLoader = RMIConnector.this.pushDefaultClassLoader();
            try {
                try {
                    Set<ObjectName> setQueryNames = RMIConnector.this.connection.queryNames(objectName, marshalledObject, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                    return setQueryNames;
                } catch (IOException e2) {
                    RMIConnector.this.communicatorAdmin.gotIOException(e2);
                    Set<ObjectName> setQueryNames2 = RMIConnector.this.connection.queryNames(objectName, marshalledObject, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                    return setQueryNames2;
                }
            } catch (Throwable th) {
                RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                throw th;
            }
        }

        @Override // javax.management.MBeanServerConnection
        public boolean isRegistered(ObjectName objectName) throws IOException {
            if (RMIConnector.logger.debugOn()) {
                RMIConnector.logger.debug("isRegistered", "name=" + ((Object) objectName));
            }
            ClassLoader classLoaderPushDefaultClassLoader = RMIConnector.this.pushDefaultClassLoader();
            try {
                try {
                    boolean zIsRegistered = RMIConnector.this.connection.isRegistered(objectName, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                    return zIsRegistered;
                } catch (IOException e2) {
                    RMIConnector.this.communicatorAdmin.gotIOException(e2);
                    boolean zIsRegistered2 = RMIConnector.this.connection.isRegistered(objectName, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                    return zIsRegistered2;
                }
            } catch (Throwable th) {
                RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                throw th;
            }
        }

        @Override // javax.management.MBeanServerConnection
        public Integer getMBeanCount() throws IOException {
            if (RMIConnector.logger.debugOn()) {
                RMIConnector.logger.debug("getMBeanCount", "");
            }
            ClassLoader classLoaderPushDefaultClassLoader = RMIConnector.this.pushDefaultClassLoader();
            try {
                try {
                    Integer mBeanCount = RMIConnector.this.connection.getMBeanCount(this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                    return mBeanCount;
                } catch (IOException e2) {
                    RMIConnector.this.communicatorAdmin.gotIOException(e2);
                    Integer mBeanCount2 = RMIConnector.this.connection.getMBeanCount(this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                    return mBeanCount2;
                }
            } catch (Throwable th) {
                RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                throw th;
            }
        }

        @Override // javax.management.MBeanServerConnection
        public Object getAttribute(ObjectName objectName, String str) throws MBeanException, AttributeNotFoundException, IOException, InstanceNotFoundException, ReflectionException {
            if (RMIConnector.logger.debugOn()) {
                RMIConnector.logger.debug("getAttribute", "name=" + ((Object) objectName) + ", attribute=" + str);
            }
            ClassLoader classLoaderPushDefaultClassLoader = RMIConnector.this.pushDefaultClassLoader();
            try {
                try {
                    Object attribute = RMIConnector.this.connection.getAttribute(objectName, str, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                    return attribute;
                } catch (IOException e2) {
                    RMIConnector.this.communicatorAdmin.gotIOException(e2);
                    Object attribute2 = RMIConnector.this.connection.getAttribute(objectName, str, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                    return attribute2;
                }
            } catch (Throwable th) {
                RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                throw th;
            }
        }

        @Override // javax.management.MBeanServerConnection
        public AttributeList getAttributes(ObjectName objectName, String[] strArr) throws IOException, InstanceNotFoundException, ReflectionException {
            if (RMIConnector.logger.debugOn()) {
                RMIConnector.logger.debug("getAttributes", "name=" + ((Object) objectName) + ", attributes=" + RMIConnector.strings(strArr));
            }
            ClassLoader classLoaderPushDefaultClassLoader = RMIConnector.this.pushDefaultClassLoader();
            try {
                try {
                    AttributeList attributes = RMIConnector.this.connection.getAttributes(objectName, strArr, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                    return attributes;
                } catch (IOException e2) {
                    RMIConnector.this.communicatorAdmin.gotIOException(e2);
                    AttributeList attributes2 = RMIConnector.this.connection.getAttributes(objectName, strArr, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                    return attributes2;
                }
            } catch (Throwable th) {
                RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                throw th;
            }
        }

        @Override // javax.management.MBeanServerConnection
        public void setAttribute(ObjectName objectName, Attribute attribute) throws InvalidAttributeValueException, MBeanException, AttributeNotFoundException, IOException, InstanceNotFoundException, ReflectionException {
            if (RMIConnector.logger.debugOn()) {
                RMIConnector.logger.debug("setAttribute", "name=" + ((Object) objectName) + ", attribute name=" + attribute.getName());
            }
            MarshalledObject marshalledObject = new MarshalledObject(attribute);
            ClassLoader classLoaderPushDefaultClassLoader = RMIConnector.this.pushDefaultClassLoader();
            try {
                try {
                    RMIConnector.this.connection.setAttribute(objectName, marshalledObject, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                } catch (IOException e2) {
                    RMIConnector.this.communicatorAdmin.gotIOException(e2);
                    RMIConnector.this.connection.setAttribute(objectName, marshalledObject, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                }
            } catch (Throwable th) {
                RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                throw th;
            }
        }

        @Override // javax.management.MBeanServerConnection
        public AttributeList setAttributes(ObjectName objectName, AttributeList attributeList) throws IOException, InstanceNotFoundException, ReflectionException {
            if (RMIConnector.logger.debugOn()) {
                RMIConnector.logger.debug("setAttributes", "name=" + ((Object) objectName) + ", attribute names=" + RMIConnector.getAttributesNames(attributeList));
            }
            MarshalledObject marshalledObject = new MarshalledObject(attributeList);
            ClassLoader classLoaderPushDefaultClassLoader = RMIConnector.this.pushDefaultClassLoader();
            try {
                try {
                    AttributeList attributes = RMIConnector.this.connection.setAttributes(objectName, marshalledObject, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                    return attributes;
                } catch (IOException e2) {
                    RMIConnector.this.communicatorAdmin.gotIOException(e2);
                    AttributeList attributes2 = RMIConnector.this.connection.setAttributes(objectName, marshalledObject, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                    return attributes2;
                }
            } catch (Throwable th) {
                RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                throw th;
            }
        }

        @Override // javax.management.MBeanServerConnection
        public Object invoke(ObjectName objectName, String str, Object[] objArr, String[] strArr) throws MBeanException, IOException, InstanceNotFoundException, ReflectionException {
            if (RMIConnector.logger.debugOn()) {
                RMIConnector.logger.debug("invoke", "name=" + ((Object) objectName) + ", operationName=" + str + ", signature=" + RMIConnector.strings(strArr));
            }
            MarshalledObject marshalledObject = new MarshalledObject(objArr);
            ClassLoader classLoaderPushDefaultClassLoader = RMIConnector.this.pushDefaultClassLoader();
            try {
                try {
                    Object objInvoke = RMIConnector.this.connection.invoke(objectName, str, marshalledObject, strArr, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                    return objInvoke;
                } catch (IOException e2) {
                    RMIConnector.this.communicatorAdmin.gotIOException(e2);
                    Object objInvoke2 = RMIConnector.this.connection.invoke(objectName, str, marshalledObject, strArr, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                    return objInvoke2;
                }
            } catch (Throwable th) {
                RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                throw th;
            }
        }

        @Override // javax.management.MBeanServerConnection
        public String getDefaultDomain() throws IOException {
            if (RMIConnector.logger.debugOn()) {
                RMIConnector.logger.debug("getDefaultDomain", "");
            }
            ClassLoader classLoaderPushDefaultClassLoader = RMIConnector.this.pushDefaultClassLoader();
            try {
                try {
                    String defaultDomain = RMIConnector.this.connection.getDefaultDomain(this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                    return defaultDomain;
                } catch (IOException e2) {
                    RMIConnector.this.communicatorAdmin.gotIOException(e2);
                    String defaultDomain2 = RMIConnector.this.connection.getDefaultDomain(this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                    return defaultDomain2;
                }
            } catch (Throwable th) {
                RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                throw th;
            }
        }

        @Override // javax.management.MBeanServerConnection
        public String[] getDomains() throws IOException {
            if (RMIConnector.logger.debugOn()) {
                RMIConnector.logger.debug("getDomains", "");
            }
            ClassLoader classLoaderPushDefaultClassLoader = RMIConnector.this.pushDefaultClassLoader();
            try {
                try {
                    String[] domains = RMIConnector.this.connection.getDomains(this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                    return domains;
                } catch (IOException e2) {
                    RMIConnector.this.communicatorAdmin.gotIOException(e2);
                    String[] domains2 = RMIConnector.this.connection.getDomains(this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                    return domains2;
                }
            } catch (Throwable th) {
                RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                throw th;
            }
        }

        @Override // javax.management.MBeanServerConnection
        public MBeanInfo getMBeanInfo(ObjectName objectName) throws IntrospectionException, IOException, InstanceNotFoundException, ReflectionException {
            if (RMIConnector.logger.debugOn()) {
                RMIConnector.logger.debug("getMBeanInfo", "name=" + ((Object) objectName));
            }
            ClassLoader classLoaderPushDefaultClassLoader = RMIConnector.this.pushDefaultClassLoader();
            try {
                try {
                    MBeanInfo mBeanInfo = RMIConnector.this.connection.getMBeanInfo(objectName, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                    return mBeanInfo;
                } catch (IOException e2) {
                    RMIConnector.this.communicatorAdmin.gotIOException(e2);
                    MBeanInfo mBeanInfo2 = RMIConnector.this.connection.getMBeanInfo(objectName, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                    return mBeanInfo2;
                }
            } catch (Throwable th) {
                RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                throw th;
            }
        }

        @Override // javax.management.MBeanServerConnection
        public boolean isInstanceOf(ObjectName objectName, String str) throws IOException, InstanceNotFoundException {
            if (RMIConnector.logger.debugOn()) {
                RMIConnector.logger.debug("isInstanceOf", "name=" + ((Object) objectName) + ", className=" + str);
            }
            ClassLoader classLoaderPushDefaultClassLoader = RMIConnector.this.pushDefaultClassLoader();
            try {
                try {
                    boolean zIsInstanceOf = RMIConnector.this.connection.isInstanceOf(objectName, str, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                    return zIsInstanceOf;
                } catch (IOException e2) {
                    RMIConnector.this.communicatorAdmin.gotIOException(e2);
                    boolean zIsInstanceOf2 = RMIConnector.this.connection.isInstanceOf(objectName, str, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                    return zIsInstanceOf2;
                }
            } catch (Throwable th) {
                RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                throw th;
            }
        }

        @Override // javax.management.MBeanServerConnection
        public void addNotificationListener(ObjectName objectName, ObjectName objectName2, NotificationFilter notificationFilter, Object obj) throws IOException, InstanceNotFoundException {
            if (RMIConnector.logger.debugOn()) {
                RMIConnector.logger.debug("addNotificationListener(ObjectName,ObjectName,NotificationFilter,Object)", "name=" + ((Object) objectName) + ", listener=" + ((Object) objectName2) + ", filter=" + ((Object) notificationFilter) + ", handback=" + obj);
            }
            MarshalledObject marshalledObject = new MarshalledObject(notificationFilter);
            MarshalledObject marshalledObject2 = new MarshalledObject(obj);
            ClassLoader classLoaderPushDefaultClassLoader = RMIConnector.this.pushDefaultClassLoader();
            try {
                try {
                    RMIConnector.this.connection.addNotificationListener(objectName, objectName2, marshalledObject, marshalledObject2, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                } catch (IOException e2) {
                    RMIConnector.this.communicatorAdmin.gotIOException(e2);
                    RMIConnector.this.connection.addNotificationListener(objectName, objectName2, marshalledObject, marshalledObject2, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                }
            } catch (Throwable th) {
                RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                throw th;
            }
        }

        @Override // javax.management.MBeanServerConnection
        public void removeNotificationListener(ObjectName objectName, ObjectName objectName2) throws ListenerNotFoundException, IOException, InstanceNotFoundException {
            if (RMIConnector.logger.debugOn()) {
                RMIConnector.logger.debug("removeNotificationListener(ObjectName,ObjectName)", "name=" + ((Object) objectName) + ", listener=" + ((Object) objectName2));
            }
            ClassLoader classLoaderPushDefaultClassLoader = RMIConnector.this.pushDefaultClassLoader();
            try {
                try {
                    RMIConnector.this.connection.removeNotificationListener(objectName, objectName2, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                } catch (IOException e2) {
                    RMIConnector.this.communicatorAdmin.gotIOException(e2);
                    RMIConnector.this.connection.removeNotificationListener(objectName, objectName2, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                }
            } catch (Throwable th) {
                RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                throw th;
            }
        }

        @Override // javax.management.MBeanServerConnection
        public void removeNotificationListener(ObjectName objectName, ObjectName objectName2, NotificationFilter notificationFilter, Object obj) throws ListenerNotFoundException, IOException, InstanceNotFoundException {
            if (RMIConnector.logger.debugOn()) {
                RMIConnector.logger.debug("removeNotificationListener(ObjectName,ObjectName,NotificationFilter,Object)", "name=" + ((Object) objectName) + ", listener=" + ((Object) objectName2) + ", filter=" + ((Object) notificationFilter) + ", handback=" + obj);
            }
            MarshalledObject marshalledObject = new MarshalledObject(notificationFilter);
            MarshalledObject marshalledObject2 = new MarshalledObject(obj);
            ClassLoader classLoaderPushDefaultClassLoader = RMIConnector.this.pushDefaultClassLoader();
            try {
                try {
                    RMIConnector.this.connection.removeNotificationListener(objectName, objectName2, marshalledObject, marshalledObject2, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                } catch (IOException e2) {
                    RMIConnector.this.communicatorAdmin.gotIOException(e2);
                    RMIConnector.this.connection.removeNotificationListener(objectName, objectName2, marshalledObject, marshalledObject2, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                }
            } catch (Throwable th) {
                RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                throw th;
            }
        }

        @Override // javax.management.MBeanServerConnection
        public void addNotificationListener(ObjectName objectName, NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws IOException, InstanceNotFoundException {
            if (RMIConnector.logger.debugOn()) {
                RMIConnector.logger.debug("addNotificationListener(ObjectName,NotificationListener,NotificationFilter,Object)", "name=" + ((Object) objectName) + ", listener=" + ((Object) notificationListener) + ", filter=" + ((Object) notificationFilter) + ", handback=" + obj);
            }
            RMIConnector.this.rmiNotifClient.addNotificationListener(RMIConnector.this.addListenerWithSubject(objectName, new MarshalledObject(notificationFilter), this.delegationSubject, true), objectName, notificationListener, notificationFilter, obj, this.delegationSubject);
        }

        @Override // javax.management.MBeanServerConnection
        public void removeNotificationListener(ObjectName objectName, NotificationListener notificationListener) throws ListenerNotFoundException, IOException, InstanceNotFoundException {
            boolean zDebugOn = RMIConnector.logger.debugOn();
            if (zDebugOn) {
                RMIConnector.logger.debug("removeNotificationListener(ObjectName,NotificationListener)", "name=" + ((Object) objectName) + ", listener=" + ((Object) notificationListener));
            }
            Integer[] numArrRemoveNotificationListener = RMIConnector.this.rmiNotifClient.removeNotificationListener(objectName, notificationListener);
            if (zDebugOn) {
                RMIConnector.logger.debug("removeNotificationListener", "listenerIDs=" + RMIConnector.objects(numArrRemoveNotificationListener));
            }
            ClassLoader classLoaderPushDefaultClassLoader = RMIConnector.this.pushDefaultClassLoader();
            try {
                try {
                    RMIConnector.this.connection.removeNotificationListeners(objectName, numArrRemoveNotificationListener, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                } catch (IOException e2) {
                    RMIConnector.this.communicatorAdmin.gotIOException(e2);
                    RMIConnector.this.connection.removeNotificationListeners(objectName, numArrRemoveNotificationListener, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                }
            } catch (Throwable th) {
                RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                throw th;
            }
        }

        @Override // javax.management.MBeanServerConnection
        public void removeNotificationListener(ObjectName objectName, NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws ListenerNotFoundException, IOException, InstanceNotFoundException {
            boolean zDebugOn = RMIConnector.logger.debugOn();
            if (zDebugOn) {
                RMIConnector.logger.debug("removeNotificationListener(ObjectName,NotificationListener,NotificationFilter,Object)", "name=" + ((Object) objectName) + ", listener=" + ((Object) notificationListener) + ", filter=" + ((Object) notificationFilter) + ", handback=" + obj);
            }
            Integer numRemoveNotificationListener = RMIConnector.this.rmiNotifClient.removeNotificationListener(objectName, notificationListener, notificationFilter, obj);
            if (zDebugOn) {
                RMIConnector.logger.debug("removeNotificationListener", "listenerID=" + ((Object) numRemoveNotificationListener));
            }
            ClassLoader classLoaderPushDefaultClassLoader = RMIConnector.this.pushDefaultClassLoader();
            try {
                try {
                    RMIConnector.this.connection.removeNotificationListeners(objectName, new Integer[]{numRemoveNotificationListener}, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                } catch (IOException e2) {
                    RMIConnector.this.communicatorAdmin.gotIOException(e2);
                    RMIConnector.this.connection.removeNotificationListeners(objectName, new Integer[]{numRemoveNotificationListener}, this.delegationSubject);
                    RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                }
            } catch (Throwable th) {
                RMIConnector.this.popDefaultClassLoader(classLoaderPushDefaultClassLoader);
                throw th;
            }
        }
    }

    /* loaded from: rt.jar:javax/management/remote/rmi/RMIConnector$RMINotifClient.class */
    private class RMINotifClient extends ClientNotifForwarder {
        public RMINotifClient(ClassLoader classLoader, Map<String, ?> map) {
            super(classLoader, map);
        }

        /* JADX WARN: Failed to check method for inline after forced processjavax.management.remote.rmi.RMIConnector.access$1108(javax.management.remote.rmi.RMIConnector):long */
        @Override // com.sun.jmx.remote.internal.ClientNotifForwarder
        protected NotificationResult fetchNotifs(long j2, int i2, long j3) throws IOException, ClassNotFoundException {
            boolean z2 = false;
            while (true) {
                try {
                    return RMIConnector.this.connection.fetchNotifications(j2, i2, j3);
                } catch (IOException e2) {
                    rethrowDeserializationException(e2);
                    try {
                        RMIConnector.this.communicatorAdmin.gotIOException(e2);
                    } catch (IOException e3) {
                        boolean z3 = false;
                        synchronized (this) {
                            if (RMIConnector.this.terminated) {
                                throw e2;
                            }
                            if (z2) {
                                z3 = true;
                            }
                            if (z3) {
                                RMIConnector.this.sendNotification(new JMXConnectionNotification(JMXConnectionNotification.FAILED, this, RMIConnector.this.connectionId, RMIConnector.access$1108(RMIConnector.this), "Failed to communicate with the server: " + e2.toString(), e2));
                                try {
                                    RMIConnector.this.close(true);
                                } catch (Exception e4) {
                                }
                                throw e2;
                            }
                            z2 = true;
                        }
                    }
                }
            }
        }

        private void rethrowDeserializationException(IOException iOException) throws IOException, ClassNotFoundException {
            if (iOException instanceof UnmarshalException) {
                throw iOException;
            }
            if (iOException instanceof MarshalException) {
                MarshalException marshalException = (MarshalException) iOException;
                if (marshalException.detail instanceof NotSerializableException) {
                    throw ((NotSerializableException) marshalException.detail);
                }
            }
        }

        @Override // com.sun.jmx.remote.internal.ClientNotifForwarder
        protected Integer addListenerForMBeanRemovedNotif() throws IOException, IllegalArgumentException, InstanceNotFoundException {
            Integer[] numArrAddNotificationListeners;
            NotificationFilterSupport notificationFilterSupport = new NotificationFilterSupport();
            notificationFilterSupport.enableType(MBeanServerNotification.UNREGISTRATION_NOTIFICATION);
            MarshalledObject marshalledObject = new MarshalledObject(notificationFilterSupport);
            ObjectName[] objectNameArr = {MBeanServerDelegate.DELEGATE_NAME};
            MarshalledObject[] marshalledObjectArr = (MarshalledObject[]) Util.cast(new MarshalledObject[]{marshalledObject});
            Subject[] subjectArr = {null};
            try {
                numArrAddNotificationListeners = RMIConnector.this.connection.addNotificationListeners(objectNameArr, marshalledObjectArr, subjectArr);
            } catch (IOException e2) {
                RMIConnector.this.communicatorAdmin.gotIOException(e2);
                numArrAddNotificationListeners = RMIConnector.this.connection.addNotificationListeners(objectNameArr, marshalledObjectArr, subjectArr);
            }
            return numArrAddNotificationListeners[0];
        }

        @Override // com.sun.jmx.remote.internal.ClientNotifForwarder
        protected void removeListenerForMBeanRemovedNotif(Integer num) throws ListenerNotFoundException, IOException, InstanceNotFoundException {
            try {
                RMIConnector.this.connection.removeNotificationListeners(MBeanServerDelegate.DELEGATE_NAME, new Integer[]{num}, null);
            } catch (IOException e2) {
                RMIConnector.this.communicatorAdmin.gotIOException(e2);
                RMIConnector.this.connection.removeNotificationListeners(MBeanServerDelegate.DELEGATE_NAME, new Integer[]{num}, null);
            }
        }

        /* JADX WARN: Failed to check method for inline after forced processjavax.management.remote.rmi.RMIConnector.access$1408(javax.management.remote.rmi.RMIConnector):long */
        @Override // com.sun.jmx.remote.internal.ClientNotifForwarder
        protected void lostNotifs(String str, long j2) {
            RMIConnector.this.sendNotification(new JMXConnectionNotification(JMXConnectionNotification.NOTIFS_LOST, RMIConnector.this, RMIConnector.this.connectionId, RMIConnector.access$1408(RMIConnector.this), str, Long.valueOf(j2)));
        }
    }

    /* loaded from: rt.jar:javax/management/remote/rmi/RMIConnector$RMIClientCommunicatorAdmin.class */
    private class RMIClientCommunicatorAdmin extends ClientCommunicatorAdmin {
        public RMIClientCommunicatorAdmin(long j2) {
            super(j2);
        }

        /* JADX WARN: Failed to check method for inline after forced processjavax.management.remote.rmi.RMIConnector.access$1108(javax.management.remote.rmi.RMIConnector):long */
        @Override // com.sun.jmx.remote.internal.ClientCommunicatorAdmin
        public void gotIOException(IOException iOException) throws IOException {
            if (!(iOException instanceof NoSuchObjectException)) {
                try {
                    RMIConnector.this.connection.getDefaultDomain(null);
                } catch (IOException e2) {
                    boolean z2 = false;
                    synchronized (this) {
                        if (!RMIConnector.this.terminated) {
                            RMIConnector.this.terminated = true;
                            z2 = true;
                        }
                        if (z2) {
                            RMIConnector.this.sendNotification(new JMXConnectionNotification(JMXConnectionNotification.FAILED, this, RMIConnector.this.connectionId, RMIConnector.access$1108(RMIConnector.this), "Failed to communicate with the server: " + iOException.toString(), iOException));
                            try {
                                RMIConnector.this.close(true);
                            } catch (Exception e3) {
                            }
                        }
                    }
                }
                if (iOException instanceof ServerException) {
                    Throwable th = ((ServerException) iOException).detail;
                    if (th instanceof IOException) {
                        throw ((IOException) th);
                    }
                    if (th instanceof RuntimeException) {
                        throw ((RuntimeException) th);
                    }
                }
                throw iOException;
            }
            super.gotIOException(iOException);
        }

        public void reconnectNotificationListeners(ClientListenerInfo[] clientListenerInfoArr) throws IOException {
            int length = clientListenerInfoArr.length;
            ClientListenerInfo[] clientListenerInfoArr2 = new ClientListenerInfo[length];
            Subject[] subjectArr = new Subject[length];
            ObjectName[] objectNameArr = new ObjectName[length];
            NotificationListener[] notificationListenerArr = new NotificationListener[length];
            NotificationFilter[] notificationFilterArr = new NotificationFilter[length];
            MarshalledObject[] marshalledObjectArr = (MarshalledObject[]) Util.cast(new MarshalledObject[length]);
            Object[] objArr = new Object[length];
            for (int i2 = 0; i2 < length; i2++) {
                subjectArr[i2] = clientListenerInfoArr[i2].getDelegationSubject();
                objectNameArr[i2] = clientListenerInfoArr[i2].getObjectName();
                notificationListenerArr[i2] = clientListenerInfoArr[i2].getListener();
                notificationFilterArr[i2] = clientListenerInfoArr[i2].getNotificationFilter();
                marshalledObjectArr[i2] = new MarshalledObject(notificationFilterArr[i2]);
                objArr[i2] = clientListenerInfoArr[i2].getHandback();
            }
            try {
                Integer[] numArrAddListenersWithSubjects = RMIConnector.this.addListenersWithSubjects(objectNameArr, marshalledObjectArr, subjectArr, false);
                for (int i3 = 0; i3 < length; i3++) {
                    clientListenerInfoArr2[i3] = new ClientListenerInfo(numArrAddListenersWithSubjects[i3], objectNameArr[i3], notificationListenerArr[i3], notificationFilterArr[i3], objArr[i3], subjectArr[i3]);
                }
                RMIConnector.this.rmiNotifClient.postReconnection(clientListenerInfoArr2);
            } catch (InstanceNotFoundException e2) {
                int i4 = 0;
                for (int i5 = 0; i5 < length; i5++) {
                    try {
                        int i6 = i4;
                        i4++;
                        clientListenerInfoArr2[i6] = new ClientListenerInfo(RMIConnector.this.addListenerWithSubject(objectNameArr[i5], new MarshalledObject(notificationFilterArr[i5]), subjectArr[i5], false), objectNameArr[i5], notificationListenerArr[i5], notificationFilterArr[i5], objArr[i5], subjectArr[i5]);
                    } catch (InstanceNotFoundException e3) {
                        RMIConnector.logger.warning("reconnectNotificationListeners", "Can't reconnect listener for " + ((Object) objectNameArr[i5]));
                    }
                }
                if (i4 != length) {
                    clientListenerInfoArr2 = new ClientListenerInfo[i4];
                    System.arraycopy(clientListenerInfoArr2, 0, clientListenerInfoArr2, 0, i4);
                }
                RMIConnector.this.rmiNotifClient.postReconnection(clientListenerInfoArr2);
            }
        }

        @Override // com.sun.jmx.remote.internal.ClientCommunicatorAdmin
        protected void checkConnection() throws IOException {
            if (RMIConnector.logger.debugOn()) {
                RMIConnector.logger.debug("RMIClientCommunicatorAdmin-checkConnection", "Calling the method getDefaultDomain.");
            }
            RMIConnector.this.connection.getDefaultDomain(null);
        }

        /* JADX WARN: Failed to check method for inline after forced processjavax.management.remote.rmi.RMIConnector.access$1108(javax.management.remote.rmi.RMIConnector):long */
        @Override // com.sun.jmx.remote.internal.ClientCommunicatorAdmin
        protected void doStart() throws IOException {
            try {
                RMIConnector.this.connection = RMIConnector.connectStub(RMIConnector.this.rmiServer != null ? RMIConnector.this.rmiServer : RMIConnector.this.findRMIServer(RMIConnector.this.jmxServiceURL, RMIConnector.this.env), RMIConnector.this.env).newClient(RMIConnector.this.env.get(JMXConnector.CREDENTIALS));
                reconnectNotificationListeners(RMIConnector.this.rmiNotifClient.preReconnection());
                RMIConnector.this.connectionId = RMIConnector.this.getConnectionId();
                RMIConnector.this.sendNotification(new JMXConnectionNotification(JMXConnectionNotification.OPENED, this, RMIConnector.this.connectionId, RMIConnector.access$1108(RMIConnector.this), "Reconnected to server", null));
            } catch (NamingException e2) {
                throw new IOException("Failed to get a RMI stub: " + ((Object) e2));
            }
        }

        @Override // com.sun.jmx.remote.internal.ClientCommunicatorAdmin
        protected void doStop() {
            try {
                RMIConnector.this.close();
            } catch (IOException e2) {
                RMIConnector.logger.warning("RMIClientCommunicatorAdmin-doStop", "Failed to call the method close():" + ((Object) e2));
                RMIConnector.logger.debug("RMIClientCommunicatorAdmin-doStop", e2);
            }
        }
    }

    static RMIServer connectStub(RMIServer rMIServer, Map<String, ?> map) throws IOException {
        if (IIOPHelper.isStub(rMIServer)) {
            try {
                IIOPHelper.getOrb(rMIServer);
            } catch (UnsupportedOperationException e2) {
                IIOPHelper.connect(rMIServer, resolveOrb(map));
            }
        }
        return rMIServer;
    }

    static Object resolveOrb(Map<String, ?> map) throws IOException {
        if (map != null) {
            Object obj = map.get(EnvHelp.DEFAULT_ORB);
            if (obj != null && !IIOPHelper.isOrb(obj)) {
                throw new IllegalArgumentException("java.naming.corba.orb must be an instance of org.omg.CORBA.ORB.");
            }
            if (obj != null) {
                return obj;
            }
        }
        Object obj2 = orb == null ? null : orb.get();
        if (obj2 != null) {
            return obj2;
        }
        Object objCreateOrb = IIOPHelper.createOrb((String[]) null, (Properties) null);
        orb = new WeakReference<>(objCreateOrb);
        return objCreateOrb;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (this.rmiServer == null && this.jmxServiceURL == null) {
            throw new InvalidObjectException("rmiServer and jmxServiceURL both null");
        }
        initTransients();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (this.rmiServer == null && this.jmxServiceURL == null) {
            throw new InvalidObjectException("rmiServer and jmxServiceURL both null.");
        }
        connectStub(this.rmiServer, this.env);
        objectOutputStream.defaultWriteObject();
    }

    private void initTransients() {
        this.rmbscMap = new WeakHashMap<>();
        this.connected = false;
        this.terminated = false;
        this.connectionBroadcaster = new NotificationBroadcasterSupport();
    }

    private static void checkStub(Remote remote, Class<?> cls) throws IllegalArgumentException {
        if (remote.getClass() != cls) {
            if (!Proxy.isProxyClass(remote.getClass())) {
                throw new SecurityException("Expecting a " + cls.getName() + " stub!");
            }
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(remote);
            if (invocationHandler.getClass() != RemoteObjectInvocationHandler.class) {
                throw new SecurityException("Expecting a dynamic proxy instance with a " + RemoteObjectInvocationHandler.class.getName() + " invocation handler!");
            }
            remote = (Remote) invocationHandler;
        }
        RemoteRef ref = ((RemoteObject) remote).getRef();
        if (ref.getClass() != UnicastRef2.class) {
            throw new SecurityException("Expecting a " + UnicastRef2.class.getName() + " remote reference in stub!");
        }
        RMIClientSocketFactory clientSocketFactory = ((UnicastRef2) ref).getLiveRef().getClientSocketFactory();
        if (clientSocketFactory == null || clientSocketFactory.getClass() != SslRMIClientSocketFactory.class) {
            throw new SecurityException("Expecting a " + SslRMIClientSocketFactory.class.getName() + " RMI client socket factory in stub!");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public RMIServer findRMIServer(JMXServiceURL jMXServiceURL, Map<String, Object> map) throws IOException, NamingException {
        boolean zIsIiopURL = RMIConnectorServer.isIiopURL(jMXServiceURL, true);
        if (zIsIiopURL) {
            map.put(EnvHelp.DEFAULT_ORB, resolveOrb(map));
        }
        String uRLPath = jMXServiceURL.getURLPath();
        int iIndexOf = uRLPath.indexOf(59);
        if (iIndexOf < 0) {
            iIndexOf = uRLPath.length();
        }
        if (uRLPath.startsWith("/jndi/")) {
            return findRMIServerJNDI(uRLPath.substring(6, iIndexOf), map, zIsIiopURL);
        }
        if (uRLPath.startsWith("/stub/")) {
            return findRMIServerJRMP(uRLPath.substring(6, iIndexOf), map, zIsIiopURL);
        }
        if (uRLPath.startsWith("/ior/")) {
            if (!IIOPHelper.isAvailable()) {
                throw new IOException("iiop protocol not available");
            }
            return findRMIServerIIOP(uRLPath.substring(5, iIndexOf), map, zIsIiopURL);
        }
        throw new MalformedURLException("URL path must begin with /jndi/ or /stub/ or /ior/: " + uRLPath);
    }

    private RMIServer findRMIServerJNDI(String str, Map<String, ?> map, boolean z2) throws NamingException {
        InitialContext initialContext = new InitialContext((Hashtable<?, ?>) EnvHelp.mapToHashtable(map));
        Object objLookup = initialContext.lookup(str);
        initialContext.close();
        if (z2) {
            return narrowIIOPServer(objLookup);
        }
        return narrowJRMPServer(objLookup);
    }

    private static RMIServer narrowJRMPServer(Object obj) {
        return (RMIServer) obj;
    }

    private static RMIServer narrowIIOPServer(Object obj) {
        try {
            return (RMIServer) IIOPHelper.narrow(obj, RMIServer.class);
        } catch (ClassCastException e2) {
            if (logger.traceOn()) {
                logger.trace("narrowIIOPServer", "Failed to narrow objref=" + obj + ": " + ((Object) e2));
            }
            if (logger.debugOn()) {
                logger.debug("narrowIIOPServer", e2);
                return null;
            }
            return null;
        }
    }

    private RMIServer findRMIServerIIOP(String str, Map<String, ?> map, boolean z2) {
        return (RMIServer) IIOPHelper.narrow(IIOPHelper.stringToObject(map.get(EnvHelp.DEFAULT_ORB), str), RMIServer.class);
    }

    private RMIServer findRMIServerJRMP(String str, Map<String, ?> map, boolean z2) throws IOException {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(base64ToByteArray(str));
            ClassLoader classLoaderResolveClientClassLoader = EnvHelp.resolveClientClassLoader(map);
            try {
                return (RMIServer) (classLoaderResolveClientClassLoader == null ? new ObjectInputStream(byteArrayInputStream) : new ObjectInputStreamWithLoader(byteArrayInputStream, classLoaderResolveClientClassLoader)).readObject();
            } catch (ClassNotFoundException e2) {
                throw new MalformedURLException("Class not found: " + ((Object) e2));
            }
        } catch (IllegalArgumentException e3) {
            throw new MalformedURLException("Bad BASE64 encoding: " + e3.getMessage());
        }
    }

    /* loaded from: rt.jar:javax/management/remote/rmi/RMIConnector$ObjectInputStreamWithLoader.class */
    private static final class ObjectInputStreamWithLoader extends ObjectInputStream {
        private final ClassLoader loader;

        ObjectInputStreamWithLoader(InputStream inputStream, ClassLoader classLoader) throws IOException {
            super(inputStream);
            this.loader = classLoader;
        }

        @Override // java.io.ObjectInputStream
        protected Class<?> resolveClass(ObjectStreamClass objectStreamClass) throws IOException, ClassNotFoundException {
            String name = objectStreamClass.getName();
            ReflectUtil.checkPackageAccess(name);
            return Class.forName(name, false, this.loader);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0051  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x001c  */
    /* JADX WARN: Type inference failed for: r0v13, types: [javax.management.MBeanServerConnection] */
    /* JADX WARN: Type inference failed for: r0v22, types: [javax.management.MBeanServerConnection] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private javax.management.MBeanServerConnection getConnectionWithSubject(javax.security.auth.Subject r7) {
        /*
            r6 = this;
            r0 = 0
            r8 = r0
            r0 = r7
            if (r0 != 0) goto L35
            r0 = r6
            java.lang.ref.WeakReference<javax.management.MBeanServerConnection> r0 = r0.nullSubjectConnRef
            if (r0 == 0) goto L1c
            r0 = r6
            java.lang.ref.WeakReference<javax.management.MBeanServerConnection> r0 = r0.nullSubjectConnRef
            java.lang.Object r0 = r0.get()
            javax.management.MBeanServerConnection r0 = (javax.management.MBeanServerConnection) r0
            r1 = r0
            r8 = r1
            if (r0 != 0) goto L6c
        L1c:
            javax.management.remote.rmi.RMIConnector$RemoteMBeanServerConnection r0 = new javax.management.remote.rmi.RMIConnector$RemoteMBeanServerConnection
            r1 = r0
            r2 = r6
            r3 = 0
            r1.<init>(r3)
            r8 = r0
            r0 = r6
            java.lang.ref.WeakReference r1 = new java.lang.ref.WeakReference
            r2 = r1
            r3 = r8
            r2.<init>(r3)
            r0.nullSubjectConnRef = r1
            goto L6c
        L35:
            r0 = r6
            java.util.WeakHashMap<javax.security.auth.Subject, java.lang.ref.WeakReference<javax.management.MBeanServerConnection>> r0 = r0.rmbscMap
            r1 = r7
            java.lang.Object r0 = r0.get(r1)
            java.lang.ref.WeakReference r0 = (java.lang.ref.WeakReference) r0
            r9 = r0
            r0 = r9
            if (r0 == 0) goto L51
            r0 = r9
            java.lang.Object r0 = r0.get()
            javax.management.MBeanServerConnection r0 = (javax.management.MBeanServerConnection) r0
            r1 = r0
            r8 = r1
            if (r0 != 0) goto L6c
        L51:
            javax.management.remote.rmi.RMIConnector$RemoteMBeanServerConnection r0 = new javax.management.remote.rmi.RMIConnector$RemoteMBeanServerConnection
            r1 = r0
            r2 = r6
            r3 = r7
            r1.<init>(r3)
            r8 = r0
            r0 = r6
            java.util.WeakHashMap<javax.security.auth.Subject, java.lang.ref.WeakReference<javax.management.MBeanServerConnection>> r0 = r0.rmbscMap
            r1 = r7
            java.lang.ref.WeakReference r2 = new java.lang.ref.WeakReference
            r3 = r2
            r4 = r8
            r3.<init>(r4)
            java.lang.Object r0 = r0.put(r1, r2)
        L6c:
            r0 = r8
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.management.remote.rmi.RMIConnector.getConnectionWithSubject(javax.security.auth.Subject):javax.management.MBeanServerConnection");
    }

    private static RMIConnection shadowJrmpStub(RemoteObject remoteObject) throws IllegalAccessException, NoSuchMethodException, InstantiationException, ClassNotFoundException, InvocationTargetException {
        return (RMIConnection) rmiConnectionImplStubClass.getConstructor(RemoteRef.class).newInstance((RemoteRef) proxyRefConstructor.newInstance(remoteObject.getRef()));
    }

    private static RMIConnection shadowIiopStub(Object obj) throws IllegalAccessException, InstantiationException {
        try {
            Object objDoPrivileged = AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() { // from class: javax.management.remote.rmi.RMIConnector.3
                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws Exception {
                    return RMIConnector.proxyStubClass.newInstance();
                }
            });
            IIOPHelper.setDelegate(objDoPrivileged, IIOPHelper.getDelegate(obj));
            return (RMIConnection) objDoPrivileged;
        } catch (PrivilegedActionException e2) {
            throw new InternalError();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static RMIConnection getConnection(RMIServer rMIServer, Object obj, boolean z2) throws IOException, IllegalArgumentException {
        RMIConnection rMIConnectionNewClient = rMIServer.newClient(obj);
        if (z2) {
            checkStub(rMIConnectionNewClient, rmiConnectionImplStubClass);
        }
        try {
        } catch (Exception e2) {
            logger.error("getConnection", "Could not wrap " + ((Object) rMIConnectionNewClient.getClass()) + " to foil stack search for classes: class loading semantics may be incorrect: " + ((Object) e2));
            logger.debug("getConnection", e2);
        }
        if (rMIConnectionNewClient.getClass() == rmiConnectionImplStubClass) {
            return shadowJrmpStub((RemoteObject) rMIConnectionNewClient);
        }
        if (rMIConnectionNewClient.getClass().getName().equals(iiopConnectionStubClassName)) {
            return shadowIiopStub(rMIConnectionNewClient);
        }
        logger.trace("getConnection", "Did not wrap " + ((Object) rMIConnectionNewClient.getClass()) + " to foil stack search for classes: class loading semantics may be incorrect");
        return rMIConnectionNewClient;
    }

    private static byte[] base64ToByteArray(String str) {
        int length = str.length();
        int i2 = length / 4;
        if (4 * i2 != length) {
            throw new IllegalArgumentException("String length must be a multiple of four.");
        }
        int i3 = 0;
        int i4 = i2;
        if (length != 0) {
            if (str.charAt(length - 1) == '=') {
                i3 = 0 + 1;
                i4--;
            }
            if (str.charAt(length - 2) == '=') {
                i3++;
            }
        }
        byte[] bArr = new byte[(3 * i2) - i3];
        int i5 = 0;
        int i6 = 0;
        for (int i7 = 0; i7 < i4; i7++) {
            int i8 = i5;
            int i9 = i5 + 1;
            int iBase64toInt = base64toInt(str.charAt(i8));
            int i10 = i9 + 1;
            int iBase64toInt2 = base64toInt(str.charAt(i9));
            int i11 = i10 + 1;
            int iBase64toInt3 = base64toInt(str.charAt(i10));
            i5 = i11 + 1;
            int iBase64toInt4 = base64toInt(str.charAt(i11));
            int i12 = i6;
            int i13 = i6 + 1;
            bArr[i12] = (byte) ((iBase64toInt << 2) | (iBase64toInt2 >> 4));
            int i14 = i13 + 1;
            bArr[i13] = (byte) ((iBase64toInt2 << 4) | (iBase64toInt3 >> 2));
            i6 = i14 + 1;
            bArr[i14] = (byte) ((iBase64toInt3 << 6) | iBase64toInt4);
        }
        if (i3 != 0) {
            int i15 = i5;
            int i16 = i5 + 1;
            int iBase64toInt5 = base64toInt(str.charAt(i15));
            int i17 = i16 + 1;
            int iBase64toInt6 = base64toInt(str.charAt(i16));
            int i18 = i6;
            int i19 = i6 + 1;
            bArr[i18] = (byte) ((iBase64toInt5 << 2) | (iBase64toInt6 >> 4));
            if (i3 == 1) {
                int i20 = i17 + 1;
                int i21 = i19 + 1;
                bArr[i19] = (byte) ((iBase64toInt6 << 4) | (base64toInt(str.charAt(i17)) >> 2));
            }
        }
        return bArr;
    }

    private static int base64toInt(char c2) {
        byte b2;
        if (c2 >= base64ToInt.length) {
            b2 = -1;
        } else {
            b2 = base64ToInt[c2];
        }
        if (b2 < 0) {
            throw new IllegalArgumentException("Illegal character " + c2);
        }
        return b2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ClassLoader pushDefaultClassLoader() {
        final Thread threadCurrentThread = Thread.currentThread();
        ClassLoader contextClassLoader = threadCurrentThread.getContextClassLoader();
        if (this.defaultClassLoader != null) {
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: javax.management.remote.rmi.RMIConnector.4
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() {
                    threadCurrentThread.setContextClassLoader(RMIConnector.this.defaultClassLoader);
                    return null;
                }
            });
        }
        return contextClassLoader;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void popDefaultClassLoader(final ClassLoader classLoader) {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: javax.management.remote.rmi.RMIConnector.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                Thread.currentThread().setContextClassLoader(classLoader);
                return null;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String objects(Object[] objArr) {
        if (objArr == null) {
            return FXMLLoader.NULL_KEYWORD;
        }
        return Arrays.asList(objArr).toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String strings(String[] strArr) {
        return objects(strArr);
    }

    static String getAttributesNames(AttributeList attributeList) {
        return attributeList != null ? (String) attributeList.asList().stream().map((v0) -> {
            return v0.getName();
        }).collect(Collectors.joining(", ", "[", "]")) : "[]";
    }
}
