package javax.management;

import java.io.ObjectInputStream;
import java.util.Set;
import javax.management.loading.ClassLoaderRepository;

/* loaded from: rt.jar:javax/management/MBeanServer.class */
public interface MBeanServer extends MBeanServerConnection {
    @Override // javax.management.MBeanServerConnection
    ObjectInstance createMBean(String str, ObjectName objectName) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, ReflectionException;

    @Override // javax.management.MBeanServerConnection
    ObjectInstance createMBean(String str, ObjectName objectName, ObjectName objectName2) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, InstanceNotFoundException, ReflectionException;

    @Override // javax.management.MBeanServerConnection
    ObjectInstance createMBean(String str, ObjectName objectName, Object[] objArr, String[] strArr) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, ReflectionException;

    @Override // javax.management.MBeanServerConnection
    ObjectInstance createMBean(String str, ObjectName objectName, ObjectName objectName2, Object[] objArr, String[] strArr) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, InstanceNotFoundException, ReflectionException;

    ObjectInstance registerMBean(Object obj, ObjectName objectName) throws InstanceAlreadyExistsException, NotCompliantMBeanException, MBeanRegistrationException;

    @Override // javax.management.MBeanServerConnection
    void unregisterMBean(ObjectName objectName) throws MBeanRegistrationException, InstanceNotFoundException;

    @Override // javax.management.MBeanServerConnection
    ObjectInstance getObjectInstance(ObjectName objectName) throws InstanceNotFoundException;

    @Override // javax.management.MBeanServerConnection
    Set<ObjectInstance> queryMBeans(ObjectName objectName, QueryExp queryExp);

    @Override // javax.management.MBeanServerConnection
    Set<ObjectName> queryNames(ObjectName objectName, QueryExp queryExp);

    @Override // javax.management.MBeanServerConnection
    boolean isRegistered(ObjectName objectName);

    @Override // javax.management.MBeanServerConnection
    Integer getMBeanCount();

    @Override // javax.management.MBeanServerConnection
    Object getAttribute(ObjectName objectName, String str) throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException;

    @Override // javax.management.MBeanServerConnection
    AttributeList getAttributes(ObjectName objectName, String[] strArr) throws InstanceNotFoundException, ReflectionException;

    @Override // javax.management.MBeanServerConnection
    void setAttribute(ObjectName objectName, Attribute attribute) throws InvalidAttributeValueException, MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException;

    @Override // javax.management.MBeanServerConnection
    AttributeList setAttributes(ObjectName objectName, AttributeList attributeList) throws InstanceNotFoundException, ReflectionException;

    @Override // javax.management.MBeanServerConnection
    Object invoke(ObjectName objectName, String str, Object[] objArr, String[] strArr) throws MBeanException, InstanceNotFoundException, ReflectionException;

    @Override // javax.management.MBeanServerConnection
    String getDefaultDomain();

    @Override // javax.management.MBeanServerConnection
    String[] getDomains();

    @Override // javax.management.MBeanServerConnection
    void addNotificationListener(ObjectName objectName, NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws InstanceNotFoundException;

    @Override // javax.management.MBeanServerConnection
    void addNotificationListener(ObjectName objectName, ObjectName objectName2, NotificationFilter notificationFilter, Object obj) throws InstanceNotFoundException;

    @Override // javax.management.MBeanServerConnection
    void removeNotificationListener(ObjectName objectName, ObjectName objectName2) throws ListenerNotFoundException, InstanceNotFoundException;

    @Override // javax.management.MBeanServerConnection
    void removeNotificationListener(ObjectName objectName, ObjectName objectName2, NotificationFilter notificationFilter, Object obj) throws ListenerNotFoundException, InstanceNotFoundException;

    @Override // javax.management.MBeanServerConnection
    void removeNotificationListener(ObjectName objectName, NotificationListener notificationListener) throws ListenerNotFoundException, InstanceNotFoundException;

    @Override // javax.management.MBeanServerConnection
    void removeNotificationListener(ObjectName objectName, NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws ListenerNotFoundException, InstanceNotFoundException;

    @Override // javax.management.MBeanServerConnection
    MBeanInfo getMBeanInfo(ObjectName objectName) throws IntrospectionException, InstanceNotFoundException, ReflectionException;

    @Override // javax.management.MBeanServerConnection
    boolean isInstanceOf(ObjectName objectName, String str) throws InstanceNotFoundException;

    Object instantiate(String str) throws MBeanException, ReflectionException;

    Object instantiate(String str, ObjectName objectName) throws MBeanException, InstanceNotFoundException, ReflectionException;

    Object instantiate(String str, Object[] objArr, String[] strArr) throws MBeanException, ReflectionException;

    Object instantiate(String str, ObjectName objectName, Object[] objArr, String[] strArr) throws MBeanException, InstanceNotFoundException, ReflectionException;

    @Deprecated
    ObjectInputStream deserialize(ObjectName objectName, byte[] bArr) throws OperationsException;

    @Deprecated
    ObjectInputStream deserialize(String str, byte[] bArr) throws OperationsException, ReflectionException;

    @Deprecated
    ObjectInputStream deserialize(String str, ObjectName objectName, byte[] bArr) throws OperationsException, ReflectionException;

    ClassLoader getClassLoaderFor(ObjectName objectName) throws InstanceNotFoundException;

    ClassLoader getClassLoader(ObjectName objectName) throws InstanceNotFoundException;

    ClassLoaderRepository getClassLoaderRepository();
}
