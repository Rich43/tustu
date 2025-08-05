package com.sun.jmx.remote.security;

import com.sun.jmx.mbeanserver.GetPropertyAction;
import java.io.ObjectInputStream;
import java.security.AccessController;
import java.util.Set;
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
import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.OperationsException;
import javax.management.QueryExp;
import javax.management.ReflectionException;
import javax.management.loading.ClassLoaderRepository;
import javax.management.remote.MBeanServerForwarder;

/* loaded from: rt.jar:com/sun/jmx/remote/security/MBeanServerAccessController.class */
public abstract class MBeanServerAccessController implements MBeanServerForwarder {
    private MBeanServer mbs;

    protected abstract void checkRead();

    protected abstract void checkWrite();

    @Override // javax.management.remote.MBeanServerForwarder
    public MBeanServer getMBeanServer() {
        return this.mbs;
    }

    @Override // javax.management.remote.MBeanServerForwarder
    public void setMBeanServer(MBeanServer mBeanServer) {
        if (mBeanServer == null) {
            throw new IllegalArgumentException("Null MBeanServer");
        }
        if (this.mbs != null) {
            throw new IllegalArgumentException("MBeanServer object already initialized");
        }
        this.mbs = mBeanServer;
    }

    protected void checkCreate(String str) {
        checkWrite();
    }

    protected void checkUnregister(ObjectName objectName) {
        checkWrite();
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public void addNotificationListener(ObjectName objectName, NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws InstanceNotFoundException {
        checkRead();
        getMBeanServer().addNotificationListener(objectName, notificationListener, notificationFilter, obj);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public void addNotificationListener(ObjectName objectName, ObjectName objectName2, NotificationFilter notificationFilter, Object obj) throws InstanceNotFoundException {
        checkRead();
        getMBeanServer().addNotificationListener(objectName, objectName2, notificationFilter, obj);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public ObjectInstance createMBean(String str, ObjectName objectName) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, ReflectionException {
        checkCreate(str);
        if (System.getSecurityManager() == null) {
            Object objInstantiate = getMBeanServer().instantiate(str);
            checkClassLoader(objInstantiate);
            return getMBeanServer().registerMBean(objInstantiate, objectName);
        }
        return getMBeanServer().createMBean(str, objectName);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public ObjectInstance createMBean(String str, ObjectName objectName, Object[] objArr, String[] strArr) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, ReflectionException {
        checkCreate(str);
        if (System.getSecurityManager() == null) {
            Object objInstantiate = getMBeanServer().instantiate(str, objArr, strArr);
            checkClassLoader(objInstantiate);
            return getMBeanServer().registerMBean(objInstantiate, objectName);
        }
        return getMBeanServer().createMBean(str, objectName, objArr, strArr);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public ObjectInstance createMBean(String str, ObjectName objectName, ObjectName objectName2) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, InstanceNotFoundException, ReflectionException {
        checkCreate(str);
        if (System.getSecurityManager() == null) {
            Object objInstantiate = getMBeanServer().instantiate(str, objectName2);
            checkClassLoader(objInstantiate);
            return getMBeanServer().registerMBean(objInstantiate, objectName);
        }
        return getMBeanServer().createMBean(str, objectName, objectName2);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public ObjectInstance createMBean(String str, ObjectName objectName, ObjectName objectName2, Object[] objArr, String[] strArr) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, InstanceNotFoundException, ReflectionException {
        checkCreate(str);
        if (System.getSecurityManager() == null) {
            Object objInstantiate = getMBeanServer().instantiate(str, objectName2, objArr, strArr);
            checkClassLoader(objInstantiate);
            return getMBeanServer().registerMBean(objInstantiate, objectName);
        }
        return getMBeanServer().createMBean(str, objectName, objectName2, objArr, strArr);
    }

    @Override // javax.management.MBeanServer
    @Deprecated
    public ObjectInputStream deserialize(ObjectName objectName, byte[] bArr) throws OperationsException {
        checkRead();
        return getMBeanServer().deserialize(objectName, bArr);
    }

    @Override // javax.management.MBeanServer
    @Deprecated
    public ObjectInputStream deserialize(String str, byte[] bArr) throws OperationsException, ReflectionException {
        checkRead();
        return getMBeanServer().deserialize(str, bArr);
    }

    @Override // javax.management.MBeanServer
    @Deprecated
    public ObjectInputStream deserialize(String str, ObjectName objectName, byte[] bArr) throws OperationsException, ReflectionException {
        checkRead();
        return getMBeanServer().deserialize(str, objectName, bArr);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public Object getAttribute(ObjectName objectName, String str) throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException {
        checkRead();
        return getMBeanServer().getAttribute(objectName, str);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public AttributeList getAttributes(ObjectName objectName, String[] strArr) throws InstanceNotFoundException, ReflectionException {
        checkRead();
        return getMBeanServer().getAttributes(objectName, strArr);
    }

    @Override // javax.management.MBeanServer
    public ClassLoader getClassLoader(ObjectName objectName) throws InstanceNotFoundException {
        checkRead();
        return getMBeanServer().getClassLoader(objectName);
    }

    @Override // javax.management.MBeanServer
    public ClassLoader getClassLoaderFor(ObjectName objectName) throws InstanceNotFoundException {
        checkRead();
        return getMBeanServer().getClassLoaderFor(objectName);
    }

    @Override // javax.management.MBeanServer
    public ClassLoaderRepository getClassLoaderRepository() {
        checkRead();
        return getMBeanServer().getClassLoaderRepository();
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public String getDefaultDomain() {
        checkRead();
        return getMBeanServer().getDefaultDomain();
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public String[] getDomains() {
        checkRead();
        return getMBeanServer().getDomains();
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public Integer getMBeanCount() {
        checkRead();
        return getMBeanServer().getMBeanCount();
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public MBeanInfo getMBeanInfo(ObjectName objectName) throws IntrospectionException, InstanceNotFoundException, ReflectionException {
        checkRead();
        return getMBeanServer().getMBeanInfo(objectName);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public ObjectInstance getObjectInstance(ObjectName objectName) throws InstanceNotFoundException {
        checkRead();
        return getMBeanServer().getObjectInstance(objectName);
    }

    @Override // javax.management.MBeanServer
    public Object instantiate(String str) throws MBeanException, ReflectionException {
        checkCreate(str);
        return getMBeanServer().instantiate(str);
    }

    @Override // javax.management.MBeanServer
    public Object instantiate(String str, Object[] objArr, String[] strArr) throws MBeanException, ReflectionException {
        checkCreate(str);
        return getMBeanServer().instantiate(str, objArr, strArr);
    }

    @Override // javax.management.MBeanServer
    public Object instantiate(String str, ObjectName objectName) throws MBeanException, InstanceNotFoundException, ReflectionException {
        checkCreate(str);
        return getMBeanServer().instantiate(str, objectName);
    }

    @Override // javax.management.MBeanServer
    public Object instantiate(String str, ObjectName objectName, Object[] objArr, String[] strArr) throws MBeanException, InstanceNotFoundException, ReflectionException {
        checkCreate(str);
        return getMBeanServer().instantiate(str, objectName, objArr, strArr);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public Object invoke(ObjectName objectName, String str, Object[] objArr, String[] strArr) throws MBeanException, InstanceNotFoundException, ReflectionException {
        checkWrite();
        checkMLetMethods(objectName, str);
        return getMBeanServer().invoke(objectName, str, objArr, strArr);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public boolean isInstanceOf(ObjectName objectName, String str) throws InstanceNotFoundException {
        checkRead();
        return getMBeanServer().isInstanceOf(objectName, str);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public boolean isRegistered(ObjectName objectName) {
        checkRead();
        return getMBeanServer().isRegistered(objectName);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public Set<ObjectInstance> queryMBeans(ObjectName objectName, QueryExp queryExp) {
        checkRead();
        return getMBeanServer().queryMBeans(objectName, queryExp);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public Set<ObjectName> queryNames(ObjectName objectName, QueryExp queryExp) {
        checkRead();
        return getMBeanServer().queryNames(objectName, queryExp);
    }

    @Override // javax.management.MBeanServer
    public ObjectInstance registerMBean(Object obj, ObjectName objectName) throws InstanceAlreadyExistsException, NotCompliantMBeanException, MBeanRegistrationException {
        checkWrite();
        return getMBeanServer().registerMBean(obj, objectName);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public void removeNotificationListener(ObjectName objectName, NotificationListener notificationListener) throws ListenerNotFoundException, InstanceNotFoundException {
        checkRead();
        getMBeanServer().removeNotificationListener(objectName, notificationListener);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public void removeNotificationListener(ObjectName objectName, NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws ListenerNotFoundException, InstanceNotFoundException {
        checkRead();
        getMBeanServer().removeNotificationListener(objectName, notificationListener, notificationFilter, obj);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public void removeNotificationListener(ObjectName objectName, ObjectName objectName2) throws ListenerNotFoundException, InstanceNotFoundException {
        checkRead();
        getMBeanServer().removeNotificationListener(objectName, objectName2);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public void removeNotificationListener(ObjectName objectName, ObjectName objectName2, NotificationFilter notificationFilter, Object obj) throws ListenerNotFoundException, InstanceNotFoundException {
        checkRead();
        getMBeanServer().removeNotificationListener(objectName, objectName2, notificationFilter, obj);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public void setAttribute(ObjectName objectName, Attribute attribute) throws InvalidAttributeValueException, MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException {
        checkWrite();
        getMBeanServer().setAttribute(objectName, attribute);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public AttributeList setAttributes(ObjectName objectName, AttributeList attributeList) throws InstanceNotFoundException, ReflectionException {
        checkWrite();
        return getMBeanServer().setAttributes(objectName, attributeList);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public void unregisterMBean(ObjectName objectName) throws MBeanRegistrationException, InstanceNotFoundException {
        checkUnregister(objectName);
        getMBeanServer().unregisterMBean(objectName);
    }

    private void checkClassLoader(Object obj) {
        if (obj instanceof ClassLoader) {
            throw new SecurityException("Access denied! Creating an MBean that is a ClassLoader is forbidden unless a security manager is installed.");
        }
    }

    private void checkMLetMethods(ObjectName objectName, String str) throws InstanceNotFoundException {
        if (System.getSecurityManager() != null) {
            return;
        }
        if ((!str.equals("addURL") && !str.equals("getMBeansFromURL")) || !getMBeanServer().isInstanceOf(objectName, "javax.management.loading.MLet")) {
            return;
        }
        if (str.equals("addURL")) {
            throw new SecurityException("Access denied! MLet method addURL cannot be invoked unless a security manager is installed.");
        }
        if (!"true".equalsIgnoreCase((String) AccessController.doPrivileged(new GetPropertyAction("jmx.remote.x.mlet.allow.getMBeansFromURL")))) {
            throw new SecurityException("Access denied! MLet method getMBeansFromURL cannot be invoked unless a security manager is installed or the system property -Djmx.remote.x.mlet.allow.getMBeansFromURL=true is specified.");
        }
    }
}
