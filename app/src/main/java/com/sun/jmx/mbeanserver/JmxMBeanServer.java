package com.sun.jmx.mbeanserver;

import com.sun.jmx.defaults.JmxProperties;
import com.sun.jmx.interceptor.DefaultMBeanServerInterceptor;
import java.io.ObjectInputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
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
import javax.management.MBeanServerDelegate;
import javax.management.MBeanServerPermission;
import javax.management.NotCompliantMBeanException;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.OperationsException;
import javax.management.QueryExp;
import javax.management.ReflectionException;
import javax.management.RuntimeOperationsException;
import javax.management.loading.ClassLoaderRepository;

/* loaded from: rt.jar:com/sun/jmx/mbeanserver/JmxMBeanServer.class */
public final class JmxMBeanServer implements SunJmxMBeanServer {
    public static final boolean DEFAULT_FAIR_LOCK_POLICY = true;
    private final MBeanInstantiator instantiator;
    private final SecureClassLoaderRepository secureClr;
    private final boolean interceptorsEnabled;
    private final MBeanServer outerShell;
    private volatile MBeanServer mbsInterceptor;
    private final MBeanServerDelegate mBeanServerDelegateObject;

    JmxMBeanServer(String str, MBeanServer mBeanServer, MBeanServerDelegate mBeanServerDelegate) {
        this(str, mBeanServer, mBeanServerDelegate, null, false);
    }

    JmxMBeanServer(String str, MBeanServer mBeanServer, MBeanServerDelegate mBeanServerDelegate, boolean z2) {
        this(str, mBeanServer, mBeanServerDelegate, null, false);
    }

    JmxMBeanServer(String str, MBeanServer mBeanServer, MBeanServerDelegate mBeanServerDelegate, MBeanInstantiator mBeanInstantiator, boolean z2) {
        this(str, mBeanServer, mBeanServerDelegate, mBeanInstantiator, z2, true);
    }

    JmxMBeanServer(String str, MBeanServer mBeanServer, MBeanServerDelegate mBeanServerDelegate, MBeanInstantiator mBeanInstantiator, boolean z2, boolean z3) {
        this.mbsInterceptor = null;
        mBeanInstantiator = mBeanInstantiator == null ? new MBeanInstantiator(new ClassLoaderRepositorySupport()) : mBeanInstantiator;
        final MBeanInstantiator mBeanInstantiator2 = mBeanInstantiator;
        this.secureClr = new SecureClassLoaderRepository((ClassLoaderRepository) AccessController.doPrivileged(new PrivilegedAction<ClassLoaderRepository>() { // from class: com.sun.jmx.mbeanserver.JmxMBeanServer.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public ClassLoaderRepository run() {
                return mBeanInstantiator2.getClassLoaderRepository();
            }
        }));
        mBeanServerDelegate = mBeanServerDelegate == null ? new MBeanServerDelegateImpl() : mBeanServerDelegate;
        mBeanServer = mBeanServer == null ? this : mBeanServer;
        this.instantiator = mBeanInstantiator;
        this.mBeanServerDelegateObject = mBeanServerDelegate;
        this.outerShell = mBeanServer;
        this.mbsInterceptor = new DefaultMBeanServerInterceptor(mBeanServer, mBeanServerDelegate, mBeanInstantiator, new Repository(str));
        this.interceptorsEnabled = z2;
        initialize();
    }

    @Override // com.sun.jmx.mbeanserver.SunJmxMBeanServer
    public boolean interceptorsEnabled() {
        return this.interceptorsEnabled;
    }

    @Override // com.sun.jmx.mbeanserver.SunJmxMBeanServer
    public MBeanInstantiator getMBeanInstantiator() {
        if (this.interceptorsEnabled) {
            return this.instantiator;
        }
        throw new UnsupportedOperationException("MBeanServerInterceptors are disabled.");
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public ObjectInstance createMBean(String str, ObjectName objectName) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, ReflectionException {
        return this.mbsInterceptor.createMBean(str, cloneObjectName(objectName), (Object[]) null, (String[]) null);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public ObjectInstance createMBean(String str, ObjectName objectName, ObjectName objectName2) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, InstanceNotFoundException, ReflectionException {
        return this.mbsInterceptor.createMBean(str, cloneObjectName(objectName), objectName2, (Object[]) null, (String[]) null);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public ObjectInstance createMBean(String str, ObjectName objectName, Object[] objArr, String[] strArr) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, ReflectionException {
        return this.mbsInterceptor.createMBean(str, cloneObjectName(objectName), objArr, strArr);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public ObjectInstance createMBean(String str, ObjectName objectName, ObjectName objectName2, Object[] objArr, String[] strArr) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, InstanceNotFoundException, ReflectionException {
        return this.mbsInterceptor.createMBean(str, cloneObjectName(objectName), objectName2, objArr, strArr);
    }

    @Override // javax.management.MBeanServer
    public ObjectInstance registerMBean(Object obj, ObjectName objectName) throws InstanceAlreadyExistsException, NotCompliantMBeanException, MBeanRegistrationException {
        return this.mbsInterceptor.registerMBean(obj, cloneObjectName(objectName));
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public void unregisterMBean(ObjectName objectName) throws MBeanRegistrationException, InstanceNotFoundException {
        this.mbsInterceptor.unregisterMBean(cloneObjectName(objectName));
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public ObjectInstance getObjectInstance(ObjectName objectName) throws InstanceNotFoundException {
        return this.mbsInterceptor.getObjectInstance(cloneObjectName(objectName));
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public Set<ObjectInstance> queryMBeans(ObjectName objectName, QueryExp queryExp) {
        return this.mbsInterceptor.queryMBeans(cloneObjectName(objectName), queryExp);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public Set<ObjectName> queryNames(ObjectName objectName, QueryExp queryExp) {
        return this.mbsInterceptor.queryNames(cloneObjectName(objectName), queryExp);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public boolean isRegistered(ObjectName objectName) {
        return this.mbsInterceptor.isRegistered(objectName);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public Integer getMBeanCount() {
        return this.mbsInterceptor.getMBeanCount();
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public Object getAttribute(ObjectName objectName, String str) throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException {
        return this.mbsInterceptor.getAttribute(cloneObjectName(objectName), str);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public AttributeList getAttributes(ObjectName objectName, String[] strArr) throws InstanceNotFoundException, ReflectionException {
        return this.mbsInterceptor.getAttributes(cloneObjectName(objectName), strArr);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public void setAttribute(ObjectName objectName, Attribute attribute) throws InvalidAttributeValueException, MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException {
        this.mbsInterceptor.setAttribute(cloneObjectName(objectName), cloneAttribute(attribute));
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public AttributeList setAttributes(ObjectName objectName, AttributeList attributeList) throws InstanceNotFoundException, ReflectionException {
        return this.mbsInterceptor.setAttributes(cloneObjectName(objectName), cloneAttributeList(attributeList));
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public Object invoke(ObjectName objectName, String str, Object[] objArr, String[] strArr) throws MBeanException, InstanceNotFoundException, ReflectionException {
        return this.mbsInterceptor.invoke(cloneObjectName(objectName), str, objArr, strArr);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public String getDefaultDomain() {
        return this.mbsInterceptor.getDefaultDomain();
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public String[] getDomains() {
        return this.mbsInterceptor.getDomains();
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public void addNotificationListener(ObjectName objectName, NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws InstanceNotFoundException {
        this.mbsInterceptor.addNotificationListener(cloneObjectName(objectName), notificationListener, notificationFilter, obj);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public void addNotificationListener(ObjectName objectName, ObjectName objectName2, NotificationFilter notificationFilter, Object obj) throws InstanceNotFoundException {
        this.mbsInterceptor.addNotificationListener(cloneObjectName(objectName), objectName2, notificationFilter, obj);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public void removeNotificationListener(ObjectName objectName, NotificationListener notificationListener) throws ListenerNotFoundException, InstanceNotFoundException {
        this.mbsInterceptor.removeNotificationListener(cloneObjectName(objectName), notificationListener);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public void removeNotificationListener(ObjectName objectName, NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws ListenerNotFoundException, InstanceNotFoundException {
        this.mbsInterceptor.removeNotificationListener(cloneObjectName(objectName), notificationListener, notificationFilter, obj);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public void removeNotificationListener(ObjectName objectName, ObjectName objectName2) throws ListenerNotFoundException, InstanceNotFoundException {
        this.mbsInterceptor.removeNotificationListener(cloneObjectName(objectName), objectName2);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public void removeNotificationListener(ObjectName objectName, ObjectName objectName2, NotificationFilter notificationFilter, Object obj) throws ListenerNotFoundException, InstanceNotFoundException {
        this.mbsInterceptor.removeNotificationListener(cloneObjectName(objectName), objectName2, notificationFilter, obj);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public MBeanInfo getMBeanInfo(ObjectName objectName) throws IntrospectionException, InstanceNotFoundException, ReflectionException {
        return this.mbsInterceptor.getMBeanInfo(cloneObjectName(objectName));
    }

    @Override // javax.management.MBeanServer
    public Object instantiate(String str) throws MBeanException, SecurityException, ReflectionException {
        checkMBeanPermission(str, null, null, "instantiate");
        return this.instantiator.instantiate(str);
    }

    @Override // javax.management.MBeanServer
    public Object instantiate(String str, ObjectName objectName) throws MBeanException, SecurityException, InstanceNotFoundException, ReflectionException {
        checkMBeanPermission(str, null, null, "instantiate");
        return this.instantiator.instantiate(str, objectName, this.outerShell.getClass().getClassLoader());
    }

    @Override // javax.management.MBeanServer
    public Object instantiate(String str, Object[] objArr, String[] strArr) throws MBeanException, SecurityException, ReflectionException {
        checkMBeanPermission(str, null, null, "instantiate");
        return this.instantiator.instantiate(str, objArr, strArr, this.outerShell.getClass().getClassLoader());
    }

    @Override // javax.management.MBeanServer
    public Object instantiate(String str, ObjectName objectName, Object[] objArr, String[] strArr) throws MBeanException, SecurityException, InstanceNotFoundException, ReflectionException {
        checkMBeanPermission(str, null, null, "instantiate");
        return this.instantiator.instantiate(str, objectName, objArr, strArr, this.outerShell.getClass().getClassLoader());
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public boolean isInstanceOf(ObjectName objectName, String str) throws InstanceNotFoundException {
        return this.mbsInterceptor.isInstanceOf(cloneObjectName(objectName), str);
    }

    @Override // javax.management.MBeanServer
    @Deprecated
    public ObjectInputStream deserialize(ObjectName objectName, byte[] bArr) throws OperationsException {
        return this.instantiator.deserialize(getClassLoaderFor(objectName), bArr);
    }

    @Override // javax.management.MBeanServer
    @Deprecated
    public ObjectInputStream deserialize(String str, byte[] bArr) throws OperationsException, SecurityException, ClassNotFoundException, ReflectionException {
        if (str == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException(), "Null className passed in parameter");
        }
        ClassLoaderRepository classLoaderRepository = getClassLoaderRepository();
        try {
            if (classLoaderRepository == null) {
                throw new ClassNotFoundException(str);
            }
            return this.instantiator.deserialize(classLoaderRepository.loadClass(str).getClassLoader(), bArr);
        } catch (ClassNotFoundException e2) {
            throw new ReflectionException(e2, "The given class could not be loaded by the default loader repository");
        }
    }

    @Override // javax.management.MBeanServer
    @Deprecated
    public ObjectInputStream deserialize(String str, ObjectName objectName, byte[] bArr) throws OperationsException, ReflectionException {
        ObjectName objectNameCloneObjectName = cloneObjectName(objectName);
        try {
            getClassLoader(objectNameCloneObjectName);
        } catch (SecurityException e2) {
            throw e2;
        } catch (Exception e3) {
        }
        return this.instantiator.deserialize(str, objectNameCloneObjectName, bArr, this.outerShell.getClass().getClassLoader());
    }

    private void initialize() {
        if (this.instantiator == null) {
            throw new IllegalStateException("instantiator must not be null.");
        }
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() { // from class: com.sun.jmx.mbeanserver.JmxMBeanServer.2
                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws Exception {
                    JmxMBeanServer.this.mbsInterceptor.registerMBean(JmxMBeanServer.this.mBeanServerDelegateObject, MBeanServerDelegate.DELEGATE_NAME);
                    return null;
                }
            });
            ClassLoader classLoader = this.outerShell.getClass().getClassLoader();
            ModifiableClassLoaderRepository modifiableClassLoaderRepository = (ModifiableClassLoaderRepository) AccessController.doPrivileged(new PrivilegedAction<ModifiableClassLoaderRepository>() { // from class: com.sun.jmx.mbeanserver.JmxMBeanServer.3
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public ModifiableClassLoaderRepository run() {
                    return JmxMBeanServer.this.instantiator.getClassLoaderRepository();
                }
            });
            if (modifiableClassLoaderRepository != null) {
                modifiableClassLoaderRepository.addClassLoader(classLoader);
                ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
                if (systemClassLoader != classLoader) {
                    modifiableClassLoaderRepository.addClassLoader(systemClassLoader);
                }
            }
        } catch (SecurityException e2) {
            if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINEST)) {
                JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINEST, JmxMBeanServer.class.getName(), FXMLLoader.INITIALIZE_METHOD_NAME, "Unexpected security exception occurred", (Throwable) e2);
            }
            throw e2;
        } catch (Exception e3) {
            if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINEST)) {
                JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINEST, JmxMBeanServer.class.getName(), FXMLLoader.INITIALIZE_METHOD_NAME, "Unexpected exception occurred", (Throwable) e3);
            }
            throw new IllegalStateException("Can't register delegate.", e3);
        }
    }

    @Override // com.sun.jmx.mbeanserver.SunJmxMBeanServer
    public synchronized MBeanServer getMBeanServerInterceptor() {
        if (this.interceptorsEnabled) {
            return this.mbsInterceptor;
        }
        throw new UnsupportedOperationException("MBeanServerInterceptors are disabled.");
    }

    @Override // com.sun.jmx.mbeanserver.SunJmxMBeanServer
    public synchronized void setMBeanServerInterceptor(MBeanServer mBeanServer) {
        if (!this.interceptorsEnabled) {
            throw new UnsupportedOperationException("MBeanServerInterceptors are disabled.");
        }
        if (mBeanServer == null) {
            throw new IllegalArgumentException("MBeanServerInterceptor is null");
        }
        this.mbsInterceptor = mBeanServer;
    }

    @Override // javax.management.MBeanServer
    public ClassLoader getClassLoaderFor(ObjectName objectName) throws InstanceNotFoundException {
        return this.mbsInterceptor.getClassLoaderFor(cloneObjectName(objectName));
    }

    @Override // javax.management.MBeanServer
    public ClassLoader getClassLoader(ObjectName objectName) throws InstanceNotFoundException {
        return this.mbsInterceptor.getClassLoader(cloneObjectName(objectName));
    }

    @Override // javax.management.MBeanServer
    public ClassLoaderRepository getClassLoaderRepository() throws SecurityException {
        checkMBeanPermission(null, null, null, "getClassLoaderRepository");
        return this.secureClr;
    }

    @Override // com.sun.jmx.mbeanserver.SunJmxMBeanServer
    public MBeanServerDelegate getMBeanServerDelegate() {
        if (this.interceptorsEnabled) {
            return this.mBeanServerDelegateObject;
        }
        throw new UnsupportedOperationException("MBeanServerInterceptors are disabled.");
    }

    public static MBeanServerDelegate newMBeanServerDelegate() {
        return new MBeanServerDelegateImpl();
    }

    public static MBeanServer newMBeanServer(String str, MBeanServer mBeanServer, MBeanServerDelegate mBeanServerDelegate, boolean z2) {
        checkNewMBeanServerPermission();
        return new JmxMBeanServer(str, mBeanServer, mBeanServerDelegate, null, z2, true);
    }

    private ObjectName cloneObjectName(ObjectName objectName) {
        if (objectName != null) {
            return ObjectName.getInstance(objectName);
        }
        return objectName;
    }

    private Attribute cloneAttribute(Attribute attribute) {
        if (attribute != null && !attribute.getClass().equals(Attribute.class)) {
            return new Attribute(attribute.getName(), attribute.getValue());
        }
        return attribute;
    }

    private AttributeList cloneAttributeList(AttributeList attributeList) {
        if (attributeList != null) {
            List<Attribute> listAsList = attributeList.asList();
            if (!attributeList.getClass().equals(AttributeList.class)) {
                AttributeList attributeList2 = new AttributeList(listAsList.size());
                Iterator<Attribute> it = listAsList.iterator();
                while (it.hasNext()) {
                    attributeList2.add(cloneAttribute(it.next()));
                }
                return attributeList2;
            }
            for (int i2 = 0; i2 < listAsList.size(); i2++) {
                Attribute attribute = listAsList.get(i2);
                if (!attribute.getClass().equals(Attribute.class)) {
                    attributeList.set(i2, cloneAttribute(attribute));
                }
            }
            return attributeList;
        }
        return attributeList;
    }

    private static void checkMBeanPermission(String str, String str2, ObjectName objectName, String str3) throws SecurityException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new MBeanPermission(str, str2, objectName, str3));
        }
    }

    private static void checkNewMBeanServerPermission() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new MBeanServerPermission("newMBeanServer"));
        }
    }
}
