package javax.management.remote.rmi;

import com.sun.jmx.mbeanserver.Util;
import com.sun.jmx.remote.internal.ServerCommunicatorAdmin;
import com.sun.jmx.remote.internal.ServerNotifForwarder;
import com.sun.jmx.remote.security.JMXSubjectDomainCombiner;
import com.sun.jmx.remote.security.SubjectDelegator;
import com.sun.jmx.remote.util.ClassLoaderWithRepository;
import com.sun.jmx.remote.util.ClassLogger;
import com.sun.jmx.remote.util.EnvHelp;
import com.sun.jmx.remote.util.OrderClassLoaders;
import java.io.IOException;
import java.rmi.MarshalledObject;
import java.rmi.UnmarshalException;
import java.rmi.server.Unreferenced;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permission;
import java.security.Permissions;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
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
import javax.management.MBeanPermission;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.NotificationFilter;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.ReflectionException;
import javax.management.RuntimeOperationsException;
import javax.management.loading.ClassLoaderRepository;
import javax.management.remote.JMXServerErrorException;
import javax.management.remote.NotificationResult;
import javax.security.auth.Subject;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:javax/management/remote/rmi/RMIConnectionImpl.class */
public class RMIConnectionImpl implements RMIConnection, Unreferenced {
    private final Subject subject;
    private final SubjectDelegator subjectDelegator;
    private final boolean removeCallerContext;
    private final AccessControlContext acc;
    private final RMIServerImpl rmiServer;
    private final MBeanServer mbeanServer;
    private final ClassLoader defaultClassLoader;
    private final ClassLoader defaultContextClassLoader;
    private final ClassLoaderWithRepository classLoaderWithRepository;
    private boolean terminated = false;
    private final String connectionId;
    private final ServerCommunicatorAdmin serverCommunicatorAdmin;
    private static final int ADD_NOTIFICATION_LISTENERS = 1;
    private static final int ADD_NOTIFICATION_LISTENER_OBJECTNAME = 2;
    private static final int CREATE_MBEAN = 3;
    private static final int CREATE_MBEAN_PARAMS = 4;
    private static final int CREATE_MBEAN_LOADER = 5;
    private static final int CREATE_MBEAN_LOADER_PARAMS = 6;
    private static final int GET_ATTRIBUTE = 7;
    private static final int GET_ATTRIBUTES = 8;
    private static final int GET_DEFAULT_DOMAIN = 9;
    private static final int GET_DOMAINS = 10;
    private static final int GET_MBEAN_COUNT = 11;
    private static final int GET_MBEAN_INFO = 12;
    private static final int GET_OBJECT_INSTANCE = 13;
    private static final int INVOKE = 14;
    private static final int IS_INSTANCE_OF = 15;
    private static final int IS_REGISTERED = 16;
    private static final int QUERY_MBEANS = 17;
    private static final int QUERY_NAMES = 18;
    private static final int REMOVE_NOTIFICATION_LISTENER = 19;
    private static final int REMOVE_NOTIFICATION_LISTENER_OBJECTNAME = 20;
    private static final int REMOVE_NOTIFICATION_LISTENER_OBJECTNAME_FILTER_HANDBACK = 21;
    private static final int SET_ATTRIBUTE = 22;
    private static final int SET_ATTRIBUTES = 23;
    private static final int UNREGISTER_MBEAN = 24;
    private ServerNotifForwarder serverNotifForwarder;
    private Map<String, ?> env;
    private static final Object[] NO_OBJECTS = new Object[0];
    private static final String[] NO_STRINGS = new String[0];
    private static final ClassLogger logger = new ClassLogger("javax.management.remote.rmi", "RMIConnectionImpl");

    public RMIConnectionImpl(RMIServerImpl rMIServerImpl, String str, final ClassLoader classLoader, Subject subject, Map<String, ?> map) {
        if (rMIServerImpl == null || str == null) {
            throw new NullPointerException("Illegal null argument");
        }
        map = map == null ? Collections.emptyMap() : map;
        this.rmiServer = rMIServerImpl;
        this.connectionId = str;
        this.defaultClassLoader = classLoader;
        this.subjectDelegator = new SubjectDelegator();
        this.subject = subject;
        if (subject == null) {
            this.acc = null;
            this.removeCallerContext = false;
        } else {
            this.removeCallerContext = SubjectDelegator.checkRemoveCallerContext(subject);
            if (this.removeCallerContext) {
                this.acc = JMXSubjectDomainCombiner.getDomainCombinerContext(subject);
            } else {
                this.acc = JMXSubjectDomainCombiner.getContext(subject);
            }
        }
        this.mbeanServer = rMIServerImpl.getMBeanServer();
        final ClassLoaderRepository classLoaderRepository = (ClassLoaderRepository) AccessController.doPrivileged(new PrivilegedAction<ClassLoaderRepository>() { // from class: javax.management.remote.rmi.RMIConnectionImpl.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public ClassLoaderRepository run2() {
                return RMIConnectionImpl.this.mbeanServer.getClassLoaderRepository();
            }
        }, withPermissions(new MBeanPermission("*", "getClassLoaderRepository")));
        this.classLoaderWithRepository = (ClassLoaderWithRepository) AccessController.doPrivileged(new PrivilegedAction<ClassLoaderWithRepository>() { // from class: javax.management.remote.rmi.RMIConnectionImpl.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public ClassLoaderWithRepository run2() {
                return new ClassLoaderWithRepository(classLoaderRepository, classLoader);
            }
        }, withPermissions(new RuntimePermission("createClassLoader")));
        this.defaultContextClassLoader = (ClassLoader) AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() { // from class: javax.management.remote.rmi.RMIConnectionImpl.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public ClassLoader run2() {
                return new CombinedClassLoader(Thread.currentThread().getContextClassLoader(), classLoader);
            }
        });
        this.serverCommunicatorAdmin = new RMIServerCommunicatorAdmin(EnvHelp.getServerConnectionTimeout(map));
        this.env = map;
    }

    private static AccessControlContext withPermissions(Permission... permissionArr) {
        Permissions permissions = new Permissions();
        for (Permission permission : permissionArr) {
            permissions.add(permission);
        }
        return new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(null, permissions)});
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized ServerNotifForwarder getServerNotifFwd() {
        if (this.serverNotifForwarder == null) {
            this.serverNotifForwarder = new ServerNotifForwarder(this.mbeanServer, this.env, this.rmiServer.getNotifBuffer(), this.connectionId);
        }
        return this.serverNotifForwarder;
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public String getConnectionId() throws IOException {
        return this.connectionId;
    }

    @Override // javax.management.remote.rmi.RMIConnection, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        boolean zDebugOn = logger.debugOn();
        String str = zDebugOn ? "[" + toString() + "]" : null;
        synchronized (this) {
            if (this.terminated) {
                if (zDebugOn) {
                    logger.debug("close", str + " already terminated.");
                }
                return;
            }
            if (zDebugOn) {
                logger.debug("close", str + " closing.");
            }
            this.terminated = true;
            if (this.serverCommunicatorAdmin != null) {
                this.serverCommunicatorAdmin.terminate();
            }
            if (this.serverNotifForwarder != null) {
                this.serverNotifForwarder.terminate();
            }
            this.rmiServer.clientClosed(this);
            if (zDebugOn) {
                logger.debug("close", str + " closed.");
            }
        }
    }

    @Override // java.rmi.server.Unreferenced
    public void unreferenced() {
        logger.debug("unreferenced", "called");
        try {
            close();
            logger.debug("unreferenced", "done");
        } catch (IOException e2) {
            logger.fine("unreferenced", e2);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public ObjectInstance createMBean(String str, ObjectName objectName, Subject subject) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, IOException, ReflectionException {
        try {
            Object[] objArr = {str, objectName};
            if (logger.debugOn()) {
                logger.debug("createMBean(String,ObjectName)", "connectionId=" + this.connectionId + ", className=" + str + ", name=" + ((Object) objectName));
            }
            return (ObjectInstance) doPrivilegedOperation(3, objArr, subject);
        } catch (PrivilegedActionException e2) {
            Exception excExtractException = extractException(e2);
            if (excExtractException instanceof ReflectionException) {
                throw ((ReflectionException) excExtractException);
            }
            if (excExtractException instanceof InstanceAlreadyExistsException) {
                throw ((InstanceAlreadyExistsException) excExtractException);
            }
            if (excExtractException instanceof MBeanRegistrationException) {
                throw ((MBeanRegistrationException) excExtractException);
            }
            if (excExtractException instanceof MBeanException) {
                throw ((MBeanException) excExtractException);
            }
            if (excExtractException instanceof NotCompliantMBeanException) {
                throw ((NotCompliantMBeanException) excExtractException);
            }
            if (excExtractException instanceof IOException) {
                throw ((IOException) excExtractException);
            }
            throw newIOException("Got unexpected server exception: " + ((Object) excExtractException), excExtractException);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public ObjectInstance createMBean(String str, ObjectName objectName, ObjectName objectName2, Subject subject) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, IOException, InstanceNotFoundException, ReflectionException {
        try {
            Object[] objArr = {str, objectName, objectName2};
            if (logger.debugOn()) {
                logger.debug("createMBean(String,ObjectName,ObjectName)", "connectionId=" + this.connectionId + ", className=" + str + ", name=" + ((Object) objectName) + ", loaderName=" + ((Object) objectName2));
            }
            return (ObjectInstance) doPrivilegedOperation(5, objArr, subject);
        } catch (PrivilegedActionException e2) {
            Exception excExtractException = extractException(e2);
            if (excExtractException instanceof ReflectionException) {
                throw ((ReflectionException) excExtractException);
            }
            if (excExtractException instanceof InstanceAlreadyExistsException) {
                throw ((InstanceAlreadyExistsException) excExtractException);
            }
            if (excExtractException instanceof MBeanRegistrationException) {
                throw ((MBeanRegistrationException) excExtractException);
            }
            if (excExtractException instanceof MBeanException) {
                throw ((MBeanException) excExtractException);
            }
            if (excExtractException instanceof NotCompliantMBeanException) {
                throw ((NotCompliantMBeanException) excExtractException);
            }
            if (excExtractException instanceof InstanceNotFoundException) {
                throw ((InstanceNotFoundException) excExtractException);
            }
            if (excExtractException instanceof IOException) {
                throw ((IOException) excExtractException);
            }
            throw newIOException("Got unexpected server exception: " + ((Object) excExtractException), excExtractException);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public ObjectInstance createMBean(String str, ObjectName objectName, MarshalledObject marshalledObject, String[] strArr, Subject subject) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, IOException, ReflectionException {
        boolean zDebugOn = logger.debugOn();
        if (zDebugOn) {
            logger.debug("createMBean(String,ObjectName,Object[],String[])", "connectionId=" + this.connectionId + ", unwrapping parameters using classLoaderWithRepository.");
        }
        try {
            Object[] objArr = {str, objectName, nullIsEmpty((Object[]) unwrap(marshalledObject, this.classLoaderWithRepository, Object[].class, subject)), nullIsEmpty(strArr)};
            if (zDebugOn) {
                logger.debug("createMBean(String,ObjectName,Object[],String[])", "connectionId=" + this.connectionId + ", className=" + str + ", name=" + ((Object) objectName) + ", signature=" + strings(strArr));
            }
            return (ObjectInstance) doPrivilegedOperation(4, objArr, subject);
        } catch (PrivilegedActionException e2) {
            Exception excExtractException = extractException(e2);
            if (excExtractException instanceof ReflectionException) {
                throw ((ReflectionException) excExtractException);
            }
            if (excExtractException instanceof InstanceAlreadyExistsException) {
                throw ((InstanceAlreadyExistsException) excExtractException);
            }
            if (excExtractException instanceof MBeanRegistrationException) {
                throw ((MBeanRegistrationException) excExtractException);
            }
            if (excExtractException instanceof MBeanException) {
                throw ((MBeanException) excExtractException);
            }
            if (excExtractException instanceof NotCompliantMBeanException) {
                throw ((NotCompliantMBeanException) excExtractException);
            }
            if (excExtractException instanceof IOException) {
                throw ((IOException) excExtractException);
            }
            throw newIOException("Got unexpected server exception: " + ((Object) excExtractException), excExtractException);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public ObjectInstance createMBean(String str, ObjectName objectName, ObjectName objectName2, MarshalledObject marshalledObject, String[] strArr, Subject subject) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, IOException, InstanceNotFoundException, ReflectionException {
        boolean zDebugOn = logger.debugOn();
        if (zDebugOn) {
            logger.debug("createMBean(String,ObjectName,ObjectName,Object[],String[])", "connectionId=" + this.connectionId + ", unwrapping params with MBean extended ClassLoader.");
        }
        try {
            Object[] objArr = {str, objectName, objectName2, nullIsEmpty((Object[]) unwrap(marshalledObject, getClassLoader(objectName2), this.defaultClassLoader, Object[].class, subject)), nullIsEmpty(strArr)};
            if (zDebugOn) {
                logger.debug("createMBean(String,ObjectName,ObjectName,Object[],String[])", "connectionId=" + this.connectionId + ", className=" + str + ", name=" + ((Object) objectName) + ", loaderName=" + ((Object) objectName2) + ", signature=" + strings(strArr));
            }
            return (ObjectInstance) doPrivilegedOperation(6, objArr, subject);
        } catch (PrivilegedActionException e2) {
            Exception excExtractException = extractException(e2);
            if (excExtractException instanceof ReflectionException) {
                throw ((ReflectionException) excExtractException);
            }
            if (excExtractException instanceof InstanceAlreadyExistsException) {
                throw ((InstanceAlreadyExistsException) excExtractException);
            }
            if (excExtractException instanceof MBeanRegistrationException) {
                throw ((MBeanRegistrationException) excExtractException);
            }
            if (excExtractException instanceof MBeanException) {
                throw ((MBeanException) excExtractException);
            }
            if (excExtractException instanceof NotCompliantMBeanException) {
                throw ((NotCompliantMBeanException) excExtractException);
            }
            if (excExtractException instanceof InstanceNotFoundException) {
                throw ((InstanceNotFoundException) excExtractException);
            }
            if (excExtractException instanceof IOException) {
                throw ((IOException) excExtractException);
            }
            throw newIOException("Got unexpected server exception: " + ((Object) excExtractException), excExtractException);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public void unregisterMBean(ObjectName objectName, Subject subject) throws MBeanRegistrationException, IOException, InstanceNotFoundException {
        try {
            Object[] objArr = {objectName};
            if (logger.debugOn()) {
                logger.debug("unregisterMBean", "connectionId=" + this.connectionId + ", name=" + ((Object) objectName));
            }
            doPrivilegedOperation(24, objArr, subject);
        } catch (PrivilegedActionException e2) {
            Exception excExtractException = extractException(e2);
            if (excExtractException instanceof InstanceNotFoundException) {
                throw ((InstanceNotFoundException) excExtractException);
            }
            if (excExtractException instanceof MBeanRegistrationException) {
                throw ((MBeanRegistrationException) excExtractException);
            }
            if (excExtractException instanceof IOException) {
                throw ((IOException) excExtractException);
            }
            throw newIOException("Got unexpected server exception: " + ((Object) excExtractException), excExtractException);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public ObjectInstance getObjectInstance(ObjectName objectName, Subject subject) throws IOException, InstanceNotFoundException {
        checkNonNull("ObjectName", objectName);
        try {
            Object[] objArr = {objectName};
            if (logger.debugOn()) {
                logger.debug("getObjectInstance", "connectionId=" + this.connectionId + ", name=" + ((Object) objectName));
            }
            return (ObjectInstance) doPrivilegedOperation(13, objArr, subject);
        } catch (PrivilegedActionException e2) {
            Exception excExtractException = extractException(e2);
            if (excExtractException instanceof InstanceNotFoundException) {
                throw ((InstanceNotFoundException) excExtractException);
            }
            if (excExtractException instanceof IOException) {
                throw ((IOException) excExtractException);
            }
            throw newIOException("Got unexpected server exception: " + ((Object) excExtractException), excExtractException);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public Set<ObjectInstance> queryMBeans(ObjectName objectName, MarshalledObject marshalledObject, Subject subject) throws IOException {
        boolean zDebugOn = logger.debugOn();
        if (zDebugOn) {
            logger.debug("queryMBeans", "connectionId=" + this.connectionId + " unwrapping query with defaultClassLoader.");
        }
        try {
            Object[] objArr = {objectName, (QueryExp) unwrap(marshalledObject, this.defaultContextClassLoader, QueryExp.class, subject)};
            if (zDebugOn) {
                logger.debug("queryMBeans", "connectionId=" + this.connectionId + ", name=" + ((Object) objectName) + ", query=" + ((Object) marshalledObject));
            }
            return (Set) Util.cast(doPrivilegedOperation(17, objArr, subject));
        } catch (PrivilegedActionException e2) {
            Exception excExtractException = extractException(e2);
            if (excExtractException instanceof IOException) {
                throw ((IOException) excExtractException);
            }
            throw newIOException("Got unexpected server exception: " + ((Object) excExtractException), excExtractException);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public Set<ObjectName> queryNames(ObjectName objectName, MarshalledObject marshalledObject, Subject subject) throws IOException {
        boolean zDebugOn = logger.debugOn();
        if (zDebugOn) {
            logger.debug("queryNames", "connectionId=" + this.connectionId + " unwrapping query with defaultClassLoader.");
        }
        try {
            Object[] objArr = {objectName, (QueryExp) unwrap(marshalledObject, this.defaultContextClassLoader, QueryExp.class, subject)};
            if (zDebugOn) {
                logger.debug("queryNames", "connectionId=" + this.connectionId + ", name=" + ((Object) objectName) + ", query=" + ((Object) marshalledObject));
            }
            return (Set) Util.cast(doPrivilegedOperation(18, objArr, subject));
        } catch (PrivilegedActionException e2) {
            Exception excExtractException = extractException(e2);
            if (excExtractException instanceof IOException) {
                throw ((IOException) excExtractException);
            }
            throw newIOException("Got unexpected server exception: " + ((Object) excExtractException), excExtractException);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public boolean isRegistered(ObjectName objectName, Subject subject) throws IOException {
        try {
            return ((Boolean) doPrivilegedOperation(16, new Object[]{objectName}, subject)).booleanValue();
        } catch (PrivilegedActionException e2) {
            Exception excExtractException = extractException(e2);
            if (excExtractException instanceof IOException) {
                throw ((IOException) excExtractException);
            }
            throw newIOException("Got unexpected server exception: " + ((Object) excExtractException), excExtractException);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public Integer getMBeanCount(Subject subject) throws IOException {
        try {
            Object[] objArr = new Object[0];
            if (logger.debugOn()) {
                logger.debug("getMBeanCount", "connectionId=" + this.connectionId);
            }
            return (Integer) doPrivilegedOperation(11, objArr, subject);
        } catch (PrivilegedActionException e2) {
            Exception excExtractException = extractException(e2);
            if (excExtractException instanceof IOException) {
                throw ((IOException) excExtractException);
            }
            throw newIOException("Got unexpected server exception: " + ((Object) excExtractException), excExtractException);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public Object getAttribute(ObjectName objectName, String str, Subject subject) throws MBeanException, AttributeNotFoundException, IOException, InstanceNotFoundException, ReflectionException {
        try {
            Object[] objArr = {objectName, str};
            if (logger.debugOn()) {
                logger.debug("getAttribute", "connectionId=" + this.connectionId + ", name=" + ((Object) objectName) + ", attribute=" + str);
            }
            return doPrivilegedOperation(7, objArr, subject);
        } catch (PrivilegedActionException e2) {
            Exception excExtractException = extractException(e2);
            if (excExtractException instanceof MBeanException) {
                throw ((MBeanException) excExtractException);
            }
            if (excExtractException instanceof AttributeNotFoundException) {
                throw ((AttributeNotFoundException) excExtractException);
            }
            if (excExtractException instanceof InstanceNotFoundException) {
                throw ((InstanceNotFoundException) excExtractException);
            }
            if (excExtractException instanceof ReflectionException) {
                throw ((ReflectionException) excExtractException);
            }
            if (excExtractException instanceof IOException) {
                throw ((IOException) excExtractException);
            }
            throw newIOException("Got unexpected server exception: " + ((Object) excExtractException), excExtractException);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public AttributeList getAttributes(ObjectName objectName, String[] strArr, Subject subject) throws IOException, InstanceNotFoundException, ReflectionException {
        try {
            Object[] objArr = {objectName, strArr};
            if (logger.debugOn()) {
                logger.debug("getAttributes", "connectionId=" + this.connectionId + ", name=" + ((Object) objectName) + ", attributes=" + strings(strArr));
            }
            return (AttributeList) doPrivilegedOperation(8, objArr, subject);
        } catch (PrivilegedActionException e2) {
            Exception excExtractException = extractException(e2);
            if (excExtractException instanceof InstanceNotFoundException) {
                throw ((InstanceNotFoundException) excExtractException);
            }
            if (excExtractException instanceof ReflectionException) {
                throw ((ReflectionException) excExtractException);
            }
            if (excExtractException instanceof IOException) {
                throw ((IOException) excExtractException);
            }
            throw newIOException("Got unexpected server exception: " + ((Object) excExtractException), excExtractException);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public void setAttribute(ObjectName objectName, MarshalledObject marshalledObject, Subject subject) throws InvalidAttributeValueException, MBeanException, AttributeNotFoundException, IOException, InstanceNotFoundException, ReflectionException {
        boolean zDebugOn = logger.debugOn();
        if (zDebugOn) {
            logger.debug("setAttribute", "connectionId=" + this.connectionId + " unwrapping attribute with MBean extended ClassLoader.");
        }
        Attribute attribute = (Attribute) unwrap(marshalledObject, getClassLoaderFor(objectName), this.defaultClassLoader, Attribute.class, subject);
        try {
            Object[] objArr = {objectName, attribute};
            if (zDebugOn) {
                logger.debug("setAttribute", "connectionId=" + this.connectionId + ", name=" + ((Object) objectName) + ", attribute name=" + attribute.getName());
            }
            doPrivilegedOperation(22, objArr, subject);
        } catch (PrivilegedActionException e2) {
            Exception excExtractException = extractException(e2);
            if (excExtractException instanceof InstanceNotFoundException) {
                throw ((InstanceNotFoundException) excExtractException);
            }
            if (excExtractException instanceof AttributeNotFoundException) {
                throw ((AttributeNotFoundException) excExtractException);
            }
            if (excExtractException instanceof InvalidAttributeValueException) {
                throw ((InvalidAttributeValueException) excExtractException);
            }
            if (excExtractException instanceof MBeanException) {
                throw ((MBeanException) excExtractException);
            }
            if (excExtractException instanceof ReflectionException) {
                throw ((ReflectionException) excExtractException);
            }
            if (excExtractException instanceof IOException) {
                throw ((IOException) excExtractException);
            }
            throw newIOException("Got unexpected server exception: " + ((Object) excExtractException), excExtractException);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public AttributeList setAttributes(ObjectName objectName, MarshalledObject marshalledObject, Subject subject) throws IOException, InstanceNotFoundException, ReflectionException {
        boolean zDebugOn = logger.debugOn();
        if (zDebugOn) {
            logger.debug("setAttributes", "connectionId=" + this.connectionId + " unwrapping attributes with MBean extended ClassLoader.");
        }
        AttributeList attributeList = (AttributeList) unwrap(marshalledObject, getClassLoaderFor(objectName), this.defaultClassLoader, AttributeList.class, subject);
        try {
            Object[] objArr = {objectName, attributeList};
            if (zDebugOn) {
                logger.debug("setAttributes", "connectionId=" + this.connectionId + ", name=" + ((Object) objectName) + ", attribute names=" + RMIConnector.getAttributesNames(attributeList));
            }
            return (AttributeList) doPrivilegedOperation(23, objArr, subject);
        } catch (PrivilegedActionException e2) {
            Exception excExtractException = extractException(e2);
            if (excExtractException instanceof InstanceNotFoundException) {
                throw ((InstanceNotFoundException) excExtractException);
            }
            if (excExtractException instanceof ReflectionException) {
                throw ((ReflectionException) excExtractException);
            }
            if (excExtractException instanceof IOException) {
                throw ((IOException) excExtractException);
            }
            throw newIOException("Got unexpected server exception: " + ((Object) excExtractException), excExtractException);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public Object invoke(ObjectName objectName, String str, MarshalledObject marshalledObject, String[] strArr, Subject subject) throws MBeanException, IOException, InstanceNotFoundException, ReflectionException {
        checkNonNull("ObjectName", objectName);
        checkNonNull("Operation name", str);
        boolean zDebugOn = logger.debugOn();
        if (zDebugOn) {
            logger.debug("invoke", "connectionId=" + this.connectionId + " unwrapping params with MBean extended ClassLoader.");
        }
        try {
            Object[] objArr = {objectName, str, nullIsEmpty((Object[]) unwrap(marshalledObject, getClassLoaderFor(objectName), this.defaultClassLoader, Object[].class, subject)), nullIsEmpty(strArr)};
            if (zDebugOn) {
                logger.debug("invoke", "connectionId=" + this.connectionId + ", name=" + ((Object) objectName) + ", operationName=" + str + ", signature=" + strings(strArr));
            }
            return doPrivilegedOperation(14, objArr, subject);
        } catch (PrivilegedActionException e2) {
            Exception excExtractException = extractException(e2);
            if (excExtractException instanceof InstanceNotFoundException) {
                throw ((InstanceNotFoundException) excExtractException);
            }
            if (excExtractException instanceof MBeanException) {
                throw ((MBeanException) excExtractException);
            }
            if (excExtractException instanceof ReflectionException) {
                throw ((ReflectionException) excExtractException);
            }
            if (excExtractException instanceof IOException) {
                throw ((IOException) excExtractException);
            }
            throw newIOException("Got unexpected server exception: " + ((Object) excExtractException), excExtractException);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public String getDefaultDomain(Subject subject) throws IOException {
        try {
            Object[] objArr = new Object[0];
            if (logger.debugOn()) {
                logger.debug("getDefaultDomain", "connectionId=" + this.connectionId);
            }
            return (String) doPrivilegedOperation(9, objArr, subject);
        } catch (PrivilegedActionException e2) {
            Exception excExtractException = extractException(e2);
            if (excExtractException instanceof IOException) {
                throw ((IOException) excExtractException);
            }
            throw newIOException("Got unexpected server exception: " + ((Object) excExtractException), excExtractException);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public String[] getDomains(Subject subject) throws IOException {
        try {
            Object[] objArr = new Object[0];
            if (logger.debugOn()) {
                logger.debug("getDomains", "connectionId=" + this.connectionId);
            }
            return (String[]) doPrivilegedOperation(10, objArr, subject);
        } catch (PrivilegedActionException e2) {
            Exception excExtractException = extractException(e2);
            if (excExtractException instanceof IOException) {
                throw ((IOException) excExtractException);
            }
            throw newIOException("Got unexpected server exception: " + ((Object) excExtractException), excExtractException);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public MBeanInfo getMBeanInfo(ObjectName objectName, Subject subject) throws IntrospectionException, IOException, InstanceNotFoundException, ReflectionException {
        checkNonNull("ObjectName", objectName);
        try {
            Object[] objArr = {objectName};
            if (logger.debugOn()) {
                logger.debug("getMBeanInfo", "connectionId=" + this.connectionId + ", name=" + ((Object) objectName));
            }
            return (MBeanInfo) doPrivilegedOperation(12, objArr, subject);
        } catch (PrivilegedActionException e2) {
            Exception excExtractException = extractException(e2);
            if (excExtractException instanceof InstanceNotFoundException) {
                throw ((InstanceNotFoundException) excExtractException);
            }
            if (excExtractException instanceof IntrospectionException) {
                throw ((IntrospectionException) excExtractException);
            }
            if (excExtractException instanceof ReflectionException) {
                throw ((ReflectionException) excExtractException);
            }
            if (excExtractException instanceof IOException) {
                throw ((IOException) excExtractException);
            }
            throw newIOException("Got unexpected server exception: " + ((Object) excExtractException), excExtractException);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public boolean isInstanceOf(ObjectName objectName, String str, Subject subject) throws IOException, InstanceNotFoundException {
        checkNonNull("ObjectName", objectName);
        try {
            Object[] objArr = {objectName, str};
            if (logger.debugOn()) {
                logger.debug("isInstanceOf", "connectionId=" + this.connectionId + ", name=" + ((Object) objectName) + ", className=" + str);
            }
            return ((Boolean) doPrivilegedOperation(15, objArr, subject)).booleanValue();
        } catch (PrivilegedActionException e2) {
            Exception excExtractException = extractException(e2);
            if (excExtractException instanceof InstanceNotFoundException) {
                throw ((InstanceNotFoundException) excExtractException);
            }
            if (excExtractException instanceof IOException) {
                throw ((IOException) excExtractException);
            }
            throw newIOException("Got unexpected server exception: " + ((Object) excExtractException), excExtractException);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javax.management.remote.rmi.RMIConnection
    public Integer[] addNotificationListeners(ObjectName[] objectNameArr, MarshalledObject[] marshalledObjectArr, Subject[] subjectArr) throws IOException, InstanceNotFoundException {
        if (objectNameArr == null || marshalledObjectArr == null) {
            throw new IllegalArgumentException("Got null arguments.");
        }
        Subject[] subjectArr2 = subjectArr != null ? subjectArr : new Subject[objectNameArr.length];
        if (objectNameArr.length != marshalledObjectArr.length || marshalledObjectArr.length != subjectArr2.length) {
            throw new IllegalArgumentException("The value lengths of 3 parameters are not same.");
        }
        for (ObjectName objectName : objectNameArr) {
            if (objectName == null) {
                throw new IllegalArgumentException("Null Object name.");
            }
        }
        NotificationFilter[] notificationFilterArr = new NotificationFilter[objectNameArr.length];
        Integer[] numArr = new Integer[objectNameArr.length];
        boolean zDebugOn = logger.debugOn();
        for (int i2 = 0; i2 < objectNameArr.length; i2++) {
            try {
                ClassLoader classLoaderFor = getClassLoaderFor(objectNameArr[i2]);
                if (zDebugOn) {
                    logger.debug("addNotificationListener(ObjectName,NotificationFilter)", "connectionId=" + this.connectionId + " unwrapping filter with target extended ClassLoader.");
                }
                notificationFilterArr[i2] = (NotificationFilter) unwrap(marshalledObjectArr[i2], classLoaderFor, this.defaultClassLoader, NotificationFilter.class, subjectArr2[i2]);
                if (zDebugOn) {
                    logger.debug("addNotificationListener(ObjectName,NotificationFilter)", "connectionId=" + this.connectionId + ", name=" + ((Object) objectNameArr[i2]) + ", filter=" + ((Object) notificationFilterArr[i2]));
                }
                numArr[i2] = (Integer) doPrivilegedOperation(1, new Object[]{objectNameArr[i2], notificationFilterArr[i2]}, subjectArr2[i2]);
            } catch (Exception e2) {
                e = e2;
                for (int i3 = 0; i3 < i2; i3++) {
                    try {
                        getServerNotifFwd().removeNotificationListener(objectNameArr[i3], numArr[i3]);
                    } catch (Exception e3) {
                    }
                }
                if (e instanceof PrivilegedActionException) {
                    e = extractException(e);
                }
                if (e instanceof ClassCastException) {
                    throw ((ClassCastException) e);
                }
                if (e instanceof IOException) {
                    throw ((IOException) e);
                }
                if (e instanceof InstanceNotFoundException) {
                    throw ((InstanceNotFoundException) e);
                }
                if (e instanceof RuntimeException) {
                    throw ((RuntimeException) e);
                }
                throw newIOException("Got unexpected server exception: " + ((Object) e), e);
            }
        }
        return numArr;
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public void addNotificationListener(ObjectName objectName, ObjectName objectName2, MarshalledObject marshalledObject, MarshalledObject marshalledObject2, Subject subject) throws IOException, InstanceNotFoundException {
        checkNonNull("Target MBean name", objectName);
        checkNonNull("Listener MBean name", objectName2);
        boolean zDebugOn = logger.debugOn();
        ClassLoader classLoaderFor = getClassLoaderFor(objectName);
        if (zDebugOn) {
            logger.debug("addNotificationListener(ObjectName,ObjectName,NotificationFilter,Object)", "connectionId=" + this.connectionId + " unwrapping filter with target extended ClassLoader.");
        }
        NotificationFilter notificationFilter = (NotificationFilter) unwrap(marshalledObject, classLoaderFor, this.defaultClassLoader, NotificationFilter.class, subject);
        if (zDebugOn) {
            logger.debug("addNotificationListener(ObjectName,ObjectName,NotificationFilter,Object)", "connectionId=" + this.connectionId + " unwrapping handback with target extended ClassLoader.");
        }
        Object objUnwrap = unwrap(marshalledObject2, classLoaderFor, this.defaultClassLoader, Object.class, subject);
        try {
            Object[] objArr = {objectName, objectName2, notificationFilter, objUnwrap};
            if (zDebugOn) {
                logger.debug("addNotificationListener(ObjectName,ObjectName,NotificationFilter,Object)", "connectionId=" + this.connectionId + ", name=" + ((Object) objectName) + ", listenerName=" + ((Object) objectName2) + ", filter=" + ((Object) notificationFilter) + ", handback=" + objUnwrap);
            }
            doPrivilegedOperation(2, objArr, subject);
        } catch (PrivilegedActionException e2) {
            Exception excExtractException = extractException(e2);
            if (excExtractException instanceof InstanceNotFoundException) {
                throw ((InstanceNotFoundException) excExtractException);
            }
            if (excExtractException instanceof IOException) {
                throw ((IOException) excExtractException);
            }
            throw newIOException("Got unexpected server exception: " + ((Object) excExtractException), excExtractException);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public void removeNotificationListeners(ObjectName objectName, Integer[] numArr, Subject subject) throws ListenerNotFoundException, IOException, InstanceNotFoundException {
        if (objectName == null || numArr == null) {
            throw new IllegalArgumentException("Illegal null parameter");
        }
        for (Integer num : numArr) {
            if (num == null) {
                throw new IllegalArgumentException("Null listener ID");
            }
        }
        try {
            Object[] objArr = {objectName, numArr};
            if (logger.debugOn()) {
                logger.debug("removeNotificationListener(ObjectName,Integer[])", "connectionId=" + this.connectionId + ", name=" + ((Object) objectName) + ", listenerIDs=" + objects(numArr));
            }
            doPrivilegedOperation(19, objArr, subject);
        } catch (PrivilegedActionException e2) {
            Exception excExtractException = extractException(e2);
            if (excExtractException instanceof InstanceNotFoundException) {
                throw ((InstanceNotFoundException) excExtractException);
            }
            if (excExtractException instanceof ListenerNotFoundException) {
                throw ((ListenerNotFoundException) excExtractException);
            }
            if (excExtractException instanceof IOException) {
                throw ((IOException) excExtractException);
            }
            throw newIOException("Got unexpected server exception: " + ((Object) excExtractException), excExtractException);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public void removeNotificationListener(ObjectName objectName, ObjectName objectName2, Subject subject) throws ListenerNotFoundException, IOException, InstanceNotFoundException {
        checkNonNull("Target MBean name", objectName);
        checkNonNull("Listener MBean name", objectName2);
        try {
            Object[] objArr = {objectName, objectName2};
            if (logger.debugOn()) {
                logger.debug("removeNotificationListener(ObjectName,ObjectName)", "connectionId=" + this.connectionId + ", name=" + ((Object) objectName) + ", listenerName=" + ((Object) objectName2));
            }
            doPrivilegedOperation(20, objArr, subject);
        } catch (PrivilegedActionException e2) {
            Exception excExtractException = extractException(e2);
            if (excExtractException instanceof InstanceNotFoundException) {
                throw ((InstanceNotFoundException) excExtractException);
            }
            if (excExtractException instanceof ListenerNotFoundException) {
                throw ((ListenerNotFoundException) excExtractException);
            }
            if (excExtractException instanceof IOException) {
                throw ((IOException) excExtractException);
            }
            throw newIOException("Got unexpected server exception: " + ((Object) excExtractException), excExtractException);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public void removeNotificationListener(ObjectName objectName, ObjectName objectName2, MarshalledObject marshalledObject, MarshalledObject marshalledObject2, Subject subject) throws ListenerNotFoundException, IOException, InstanceNotFoundException {
        checkNonNull("Target MBean name", objectName);
        checkNonNull("Listener MBean name", objectName2);
        boolean zDebugOn = logger.debugOn();
        ClassLoader classLoaderFor = getClassLoaderFor(objectName);
        if (zDebugOn) {
            logger.debug("removeNotificationListener(ObjectName,ObjectName,NotificationFilter,Object)", "connectionId=" + this.connectionId + " unwrapping filter with target extended ClassLoader.");
        }
        NotificationFilter notificationFilter = (NotificationFilter) unwrap(marshalledObject, classLoaderFor, this.defaultClassLoader, NotificationFilter.class, subject);
        if (zDebugOn) {
            logger.debug("removeNotificationListener(ObjectName,ObjectName,NotificationFilter,Object)", "connectionId=" + this.connectionId + " unwrapping handback with target extended ClassLoader.");
        }
        Object objUnwrap = unwrap(marshalledObject2, classLoaderFor, this.defaultClassLoader, Object.class, subject);
        try {
            Object[] objArr = {objectName, objectName2, notificationFilter, objUnwrap};
            if (zDebugOn) {
                logger.debug("removeNotificationListener(ObjectName,ObjectName,NotificationFilter,Object)", "connectionId=" + this.connectionId + ", name=" + ((Object) objectName) + ", listenerName=" + ((Object) objectName2) + ", filter=" + ((Object) notificationFilter) + ", handback=" + objUnwrap);
            }
            doPrivilegedOperation(21, objArr, subject);
        } catch (PrivilegedActionException e2) {
            Exception excExtractException = extractException(e2);
            if (excExtractException instanceof InstanceNotFoundException) {
                throw ((InstanceNotFoundException) excExtractException);
            }
            if (excExtractException instanceof ListenerNotFoundException) {
                throw ((ListenerNotFoundException) excExtractException);
            }
            if (excExtractException instanceof IOException) {
                throw ((IOException) excExtractException);
            }
            throw newIOException("Got unexpected server exception: " + ((Object) excExtractException), excExtractException);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public NotificationResult fetchNotifications(final long j2, final int i2, final long j3) throws IOException {
        if (logger.debugOn()) {
            logger.debug("fetchNotifications", "connectionId=" + this.connectionId + ", timeout=" + j3);
        }
        if (i2 < 0 || j3 < 0) {
            throw new IllegalArgumentException("Illegal negative argument");
        }
        try {
            if (this.serverCommunicatorAdmin.reqIncoming()) {
                if (logger.debugOn()) {
                    logger.debug("fetchNotifications", "The notification server has been closed, returns null to force the client to stop fetching");
                }
                return null;
            }
            PrivilegedAction<NotificationResult> privilegedAction = new PrivilegedAction<NotificationResult>() { // from class: javax.management.remote.rmi.RMIConnectionImpl.4
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public NotificationResult run2() {
                    return RMIConnectionImpl.this.getServerNotifFwd().fetchNotifs(j2, j3, i2);
                }
            };
            if (this.acc == null) {
                NotificationResult notificationResultRun2 = privilegedAction.run2();
                this.serverCommunicatorAdmin.rspOutgoing();
                return notificationResultRun2;
            }
            NotificationResult notificationResult = (NotificationResult) AccessController.doPrivileged(privilegedAction, this.acc);
            this.serverCommunicatorAdmin.rspOutgoing();
            return notificationResult;
        } finally {
            this.serverCommunicatorAdmin.rspOutgoing();
        }
    }

    public String toString() {
        return super.toString() + ": connectionId=" + this.connectionId;
    }

    /* loaded from: rt.jar:javax/management/remote/rmi/RMIConnectionImpl$PrivilegedOperation.class */
    private class PrivilegedOperation implements PrivilegedExceptionAction<Object> {
        private int operation;
        private Object[] params;

        public PrivilegedOperation(int i2, Object[] objArr) {
            this.operation = i2;
            this.params = objArr;
        }

        @Override // java.security.PrivilegedExceptionAction
        public Object run() throws Exception {
            return RMIConnectionImpl.this.doOperation(this.operation, this.params);
        }
    }

    /* loaded from: rt.jar:javax/management/remote/rmi/RMIConnectionImpl$RMIServerCommunicatorAdmin.class */
    private class RMIServerCommunicatorAdmin extends ServerCommunicatorAdmin {
        public RMIServerCommunicatorAdmin(long j2) {
            super(j2);
        }

        @Override // com.sun.jmx.remote.internal.ServerCommunicatorAdmin
        protected void doStop() {
            try {
                RMIConnectionImpl.this.close();
            } catch (IOException e2) {
                RMIConnectionImpl.logger.warning("RMIServerCommunicatorAdmin-doStop", "Failed to close: " + ((Object) e2));
                RMIConnectionImpl.logger.debug("RMIServerCommunicatorAdmin-doStop", e2);
            }
        }
    }

    private ClassLoader getClassLoader(final ObjectName objectName) throws InstanceNotFoundException {
        try {
            return (ClassLoader) AccessController.doPrivileged(new PrivilegedExceptionAction<ClassLoader>() { // from class: javax.management.remote.rmi.RMIConnectionImpl.5
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public ClassLoader run() throws InstanceNotFoundException {
                    return RMIConnectionImpl.this.mbeanServer.getClassLoader(objectName);
                }
            }, withPermissions(new MBeanPermission("*", "getClassLoader")));
        } catch (PrivilegedActionException e2) {
            throw ((InstanceNotFoundException) extractException(e2));
        }
    }

    private ClassLoader getClassLoaderFor(final ObjectName objectName) throws InstanceNotFoundException {
        try {
            return (ClassLoader) AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() { // from class: javax.management.remote.rmi.RMIConnectionImpl.6
                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws InstanceNotFoundException {
                    return RMIConnectionImpl.this.mbeanServer.getClassLoaderFor(objectName);
                }
            }, withPermissions(new MBeanPermission("*", "getClassLoaderFor")));
        } catch (PrivilegedActionException e2) {
            throw ((InstanceNotFoundException) extractException(e2));
        }
    }

    private Object doPrivilegedOperation(int i2, Object[] objArr, Subject subject) throws PrivilegedActionException, IOException {
        AccessControlContext accessControlContextDelegatedContext;
        this.serverCommunicatorAdmin.reqIncoming();
        try {
            try {
                if (subject == null) {
                    accessControlContextDelegatedContext = this.acc;
                } else {
                    if (this.subject == null) {
                        throw new SecurityException("Subject delegation cannot be enabled unless an authenticated subject is put in place");
                    }
                    accessControlContextDelegatedContext = this.subjectDelegator.delegatedContext(this.acc, subject, this.removeCallerContext);
                }
                PrivilegedOperation privilegedOperation = new PrivilegedOperation(i2, objArr);
                if (accessControlContextDelegatedContext == null) {
                    try {
                        Object objRun = privilegedOperation.run();
                        this.serverCommunicatorAdmin.rspOutgoing();
                        return objRun;
                    } catch (Exception e2) {
                        if (e2 instanceof RuntimeException) {
                            throw ((RuntimeException) e2);
                        }
                        throw new PrivilegedActionException(e2);
                    }
                }
                Object objDoPrivileged = AccessController.doPrivileged(privilegedOperation, accessControlContextDelegatedContext);
                this.serverCommunicatorAdmin.rspOutgoing();
                return objDoPrivileged;
            } catch (Error e3) {
                throw new JMXServerErrorException(e3.toString(), e3);
            }
        } catch (Throwable th) {
            this.serverCommunicatorAdmin.rspOutgoing();
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object doOperation(int i2, Object[] objArr) throws Exception {
        switch (i2) {
            case 1:
                return getServerNotifFwd().addNotificationListener((ObjectName) objArr[0], (NotificationFilter) objArr[1]);
            case 2:
                this.mbeanServer.addNotificationListener((ObjectName) objArr[0], (ObjectName) objArr[1], (NotificationFilter) objArr[2], objArr[3]);
                return null;
            case 3:
                return this.mbeanServer.createMBean((String) objArr[0], (ObjectName) objArr[1]);
            case 4:
                return this.mbeanServer.createMBean((String) objArr[0], (ObjectName) objArr[1], (Object[]) objArr[2], (String[]) objArr[3]);
            case 5:
                return this.mbeanServer.createMBean((String) objArr[0], (ObjectName) objArr[1], (ObjectName) objArr[2]);
            case 6:
                return this.mbeanServer.createMBean((String) objArr[0], (ObjectName) objArr[1], (ObjectName) objArr[2], (Object[]) objArr[3], (String[]) objArr[4]);
            case 7:
                return this.mbeanServer.getAttribute((ObjectName) objArr[0], (String) objArr[1]);
            case 8:
                return this.mbeanServer.getAttributes((ObjectName) objArr[0], (String[]) objArr[1]);
            case 9:
                return this.mbeanServer.getDefaultDomain();
            case 10:
                return this.mbeanServer.getDomains();
            case 11:
                return this.mbeanServer.getMBeanCount();
            case 12:
                return this.mbeanServer.getMBeanInfo((ObjectName) objArr[0]);
            case 13:
                return this.mbeanServer.getObjectInstance((ObjectName) objArr[0]);
            case 14:
                return this.mbeanServer.invoke((ObjectName) objArr[0], (String) objArr[1], (Object[]) objArr[2], (String[]) objArr[3]);
            case 15:
                return this.mbeanServer.isInstanceOf((ObjectName) objArr[0], (String) objArr[1]) ? Boolean.TRUE : Boolean.FALSE;
            case 16:
                return this.mbeanServer.isRegistered((ObjectName) objArr[0]) ? Boolean.TRUE : Boolean.FALSE;
            case 17:
                return this.mbeanServer.queryMBeans((ObjectName) objArr[0], (QueryExp) objArr[1]);
            case 18:
                return this.mbeanServer.queryNames((ObjectName) objArr[0], (QueryExp) objArr[1]);
            case 19:
                getServerNotifFwd().removeNotificationListener((ObjectName) objArr[0], (Integer[]) objArr[1]);
                return null;
            case 20:
                this.mbeanServer.removeNotificationListener((ObjectName) objArr[0], (ObjectName) objArr[1]);
                return null;
            case 21:
                this.mbeanServer.removeNotificationListener((ObjectName) objArr[0], (ObjectName) objArr[1], (NotificationFilter) objArr[2], objArr[3]);
                return null;
            case 22:
                this.mbeanServer.setAttribute((ObjectName) objArr[0], (Attribute) objArr[1]);
                return null;
            case 23:
                return this.mbeanServer.setAttributes((ObjectName) objArr[0], (AttributeList) objArr[1]);
            case 24:
                this.mbeanServer.unregisterMBean((ObjectName) objArr[0]);
                return null;
            default:
                throw new IllegalArgumentException("Invalid operation");
        }
    }

    /* loaded from: rt.jar:javax/management/remote/rmi/RMIConnectionImpl$SetCcl.class */
    private static class SetCcl implements PrivilegedExceptionAction<ClassLoader> {
        private final ClassLoader classLoader;

        SetCcl(ClassLoader classLoader) {
            this.classLoader = classLoader;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedExceptionAction
        public ClassLoader run() {
            Thread threadCurrentThread = Thread.currentThread();
            ClassLoader contextClassLoader = threadCurrentThread.getContextClassLoader();
            threadCurrentThread.setContextClassLoader(this.classLoader);
            return contextClassLoader;
        }
    }

    private <T> T unwrap(MarshalledObject<?> marshalledObject, ClassLoader classLoader, Class<T> cls, Subject subject) throws IOException {
        AccessControlContext accessControlContextDelegatedContext;
        if (marshalledObject == null) {
            return null;
        }
        try {
            ClassLoader classLoader2 = (ClassLoader) AccessController.doPrivileged(new SetCcl(classLoader));
            try {
                if (subject == null) {
                    accessControlContextDelegatedContext = this.acc;
                } else {
                    if (this.subject == null) {
                        throw new SecurityException("Subject delegation cannot be enabled unless an authenticated subject is put in place");
                    }
                    accessControlContextDelegatedContext = this.subjectDelegator.delegatedContext(this.acc, subject, this.removeCallerContext);
                }
                if (accessControlContextDelegatedContext != null) {
                    T t2 = (T) AccessController.doPrivileged(() -> {
                        return cls.cast(marshalledObject.get());
                    }, accessControlContextDelegatedContext);
                    AccessController.doPrivileged(new SetCcl(classLoader2));
                    return t2;
                }
                T tCast = cls.cast(marshalledObject.get());
                AccessController.doPrivileged(new SetCcl(classLoader2));
                return tCast;
            } catch (Throwable th) {
                AccessController.doPrivileged(new SetCcl(classLoader2));
                throw th;
            }
        } catch (ClassNotFoundException e2) {
            logger.warning("unwrap", "Failed to unmarshall object: " + ((Object) e2));
            logger.debug("unwrap", e2);
            throw new UnmarshalException(e2.toString(), e2);
        } catch (PrivilegedActionException e3) {
            Exception excExtractException = extractException(e3);
            if (excExtractException instanceof IOException) {
                throw ((IOException) excExtractException);
            }
            if (excExtractException instanceof ClassNotFoundException) {
                throw new UnmarshalException(excExtractException.toString(), excExtractException);
            }
            logger.warning("unwrap", "Failed to unmarshall object: " + ((Object) excExtractException));
            logger.debug("unwrap", excExtractException);
            return null;
        }
    }

    private <T> T unwrap(MarshalledObject<?> marshalledObject, final ClassLoader classLoader, final ClassLoader classLoader2, Class<T> cls, Subject subject) throws IOException {
        if (marshalledObject == null) {
            return null;
        }
        try {
            return (T) unwrap(marshalledObject, (ClassLoader) AccessController.doPrivileged(new PrivilegedExceptionAction<ClassLoader>() { // from class: javax.management.remote.rmi.RMIConnectionImpl.7
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public ClassLoader run() throws Exception {
                    return new CombinedClassLoader(Thread.currentThread().getContextClassLoader(), new OrderClassLoaders(classLoader, classLoader2));
                }
            }), cls, subject);
        } catch (PrivilegedActionException e2) {
            Exception excExtractException = extractException(e2);
            if (excExtractException instanceof IOException) {
                throw ((IOException) excExtractException);
            }
            if (excExtractException instanceof ClassNotFoundException) {
                throw new UnmarshalException(excExtractException.toString(), excExtractException);
            }
            logger.warning("unwrap", "Failed to unmarshall object: " + ((Object) excExtractException));
            logger.debug("unwrap", excExtractException);
            return null;
        }
    }

    private static IOException newIOException(String str, Throwable th) {
        return (IOException) EnvHelp.initCause(new IOException(str), th);
    }

    private static Exception extractException(Exception exc) {
        while (exc instanceof PrivilegedActionException) {
            exc = ((PrivilegedActionException) exc).getException();
        }
        return exc;
    }

    private static Object[] nullIsEmpty(Object[] objArr) {
        return objArr == null ? NO_OBJECTS : objArr;
    }

    private static String[] nullIsEmpty(String[] strArr) {
        return strArr == null ? NO_STRINGS : strArr;
    }

    private static void checkNonNull(String str, Object obj) {
        if (obj == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException(str + " must not be null"));
        }
    }

    private static String objects(Object[] objArr) {
        if (objArr == null) {
            return FXMLLoader.NULL_KEYWORD;
        }
        return Arrays.asList(objArr).toString();
    }

    private static String strings(String[] strArr) {
        return objects(strArr);
    }

    /* loaded from: rt.jar:javax/management/remote/rmi/RMIConnectionImpl$CombinedClassLoader.class */
    private static final class CombinedClassLoader extends ClassLoader {
        final ClassLoaderWrapper defaultCL;

        /* loaded from: rt.jar:javax/management/remote/rmi/RMIConnectionImpl$CombinedClassLoader$ClassLoaderWrapper.class */
        private static final class ClassLoaderWrapper extends ClassLoader {
            ClassLoaderWrapper(ClassLoader classLoader) {
                super(classLoader);
            }

            @Override // java.lang.ClassLoader
            protected Class<?> loadClass(String str, boolean z2) throws ClassNotFoundException {
                return super.loadClass(str, z2);
            }
        }

        private CombinedClassLoader(ClassLoader classLoader, ClassLoader classLoader2) {
            super(classLoader);
            this.defaultCL = new ClassLoaderWrapper(classLoader2);
        }

        @Override // java.lang.ClassLoader
        protected Class<?> loadClass(String str, boolean z2) throws ClassNotFoundException {
            ReflectUtil.checkPackageAccess(str);
            try {
                super.loadClass(str, z2);
            } catch (Exception e2) {
                Throwable cause = e2;
                while (true) {
                    Exception exc = cause;
                    if (exc == null) {
                        break;
                    }
                    if (!(exc instanceof SecurityException)) {
                        cause = exc.getCause();
                    } else {
                        if (exc == e2) {
                            throw ((SecurityException) exc);
                        }
                        throw new SecurityException(exc.getMessage(), e2);
                    }
                }
            }
            return this.defaultCL.loadClass(str, z2);
        }
    }
}
