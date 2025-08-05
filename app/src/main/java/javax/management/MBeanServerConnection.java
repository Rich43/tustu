package javax.management;

import java.io.IOException;
import java.util.Set;

/* loaded from: rt.jar:javax/management/MBeanServerConnection.class */
public interface MBeanServerConnection {
    ObjectInstance createMBean(String str, ObjectName objectName) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, IOException, ReflectionException;

    ObjectInstance createMBean(String str, ObjectName objectName, ObjectName objectName2) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, IOException, InstanceNotFoundException, ReflectionException;

    ObjectInstance createMBean(String str, ObjectName objectName, Object[] objArr, String[] strArr) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, IOException, ReflectionException;

    ObjectInstance createMBean(String str, ObjectName objectName, ObjectName objectName2, Object[] objArr, String[] strArr) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, IOException, InstanceNotFoundException, ReflectionException;

    void unregisterMBean(ObjectName objectName) throws MBeanRegistrationException, IOException, InstanceNotFoundException;

    ObjectInstance getObjectInstance(ObjectName objectName) throws IOException, InstanceNotFoundException;

    Set<ObjectInstance> queryMBeans(ObjectName objectName, QueryExp queryExp) throws IOException;

    Set<ObjectName> queryNames(ObjectName objectName, QueryExp queryExp) throws IOException;

    boolean isRegistered(ObjectName objectName) throws IOException;

    Integer getMBeanCount() throws IOException;

    Object getAttribute(ObjectName objectName, String str) throws MBeanException, AttributeNotFoundException, IOException, InstanceNotFoundException, ReflectionException;

    AttributeList getAttributes(ObjectName objectName, String[] strArr) throws IOException, InstanceNotFoundException, ReflectionException;

    void setAttribute(ObjectName objectName, Attribute attribute) throws InvalidAttributeValueException, MBeanException, AttributeNotFoundException, IOException, InstanceNotFoundException, ReflectionException;

    AttributeList setAttributes(ObjectName objectName, AttributeList attributeList) throws IOException, InstanceNotFoundException, ReflectionException;

    Object invoke(ObjectName objectName, String str, Object[] objArr, String[] strArr) throws MBeanException, IOException, InstanceNotFoundException, ReflectionException;

    String getDefaultDomain() throws IOException;

    String[] getDomains() throws IOException;

    void addNotificationListener(ObjectName objectName, NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws IOException, InstanceNotFoundException;

    void addNotificationListener(ObjectName objectName, ObjectName objectName2, NotificationFilter notificationFilter, Object obj) throws IOException, InstanceNotFoundException;

    void removeNotificationListener(ObjectName objectName, ObjectName objectName2) throws ListenerNotFoundException, IOException, InstanceNotFoundException;

    void removeNotificationListener(ObjectName objectName, ObjectName objectName2, NotificationFilter notificationFilter, Object obj) throws ListenerNotFoundException, IOException, InstanceNotFoundException;

    void removeNotificationListener(ObjectName objectName, NotificationListener notificationListener) throws ListenerNotFoundException, IOException, InstanceNotFoundException;

    void removeNotificationListener(ObjectName objectName, NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws ListenerNotFoundException, IOException, InstanceNotFoundException;

    MBeanInfo getMBeanInfo(ObjectName objectName) throws IntrospectionException, IOException, InstanceNotFoundException, ReflectionException;

    boolean isInstanceOf(ObjectName objectName, String str) throws IOException, InstanceNotFoundException;
}
