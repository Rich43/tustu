package javax.management.remote.rmi;

import java.io.Closeable;
import java.io.IOException;
import java.rmi.MarshalledObject;
import java.rmi.Remote;
import java.util.Set;
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
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.NotificationResult;
import javax.security.auth.Subject;

/* loaded from: rt.jar:javax/management/remote/rmi/RMIConnection.class */
public interface RMIConnection extends Closeable, Remote {
    String getConnectionId() throws IOException;

    @Override // java.io.Closeable, java.lang.AutoCloseable
    void close() throws IOException;

    ObjectInstance createMBean(String str, ObjectName objectName, Subject subject) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, IOException, ReflectionException;

    ObjectInstance createMBean(String str, ObjectName objectName, ObjectName objectName2, Subject subject) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, IOException, InstanceNotFoundException, ReflectionException;

    ObjectInstance createMBean(String str, ObjectName objectName, MarshalledObject marshalledObject, String[] strArr, Subject subject) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, IOException, ReflectionException;

    ObjectInstance createMBean(String str, ObjectName objectName, ObjectName objectName2, MarshalledObject marshalledObject, String[] strArr, Subject subject) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, IOException, InstanceNotFoundException, ReflectionException;

    void unregisterMBean(ObjectName objectName, Subject subject) throws MBeanRegistrationException, IOException, InstanceNotFoundException;

    ObjectInstance getObjectInstance(ObjectName objectName, Subject subject) throws IOException, InstanceNotFoundException;

    Set<ObjectInstance> queryMBeans(ObjectName objectName, MarshalledObject marshalledObject, Subject subject) throws IOException;

    Set<ObjectName> queryNames(ObjectName objectName, MarshalledObject marshalledObject, Subject subject) throws IOException;

    boolean isRegistered(ObjectName objectName, Subject subject) throws IOException;

    Integer getMBeanCount(Subject subject) throws IOException;

    Object getAttribute(ObjectName objectName, String str, Subject subject) throws MBeanException, AttributeNotFoundException, IOException, InstanceNotFoundException, ReflectionException;

    AttributeList getAttributes(ObjectName objectName, String[] strArr, Subject subject) throws IOException, InstanceNotFoundException, ReflectionException;

    void setAttribute(ObjectName objectName, MarshalledObject marshalledObject, Subject subject) throws InvalidAttributeValueException, MBeanException, AttributeNotFoundException, IOException, InstanceNotFoundException, ReflectionException;

    AttributeList setAttributes(ObjectName objectName, MarshalledObject marshalledObject, Subject subject) throws IOException, InstanceNotFoundException, ReflectionException;

    Object invoke(ObjectName objectName, String str, MarshalledObject marshalledObject, String[] strArr, Subject subject) throws MBeanException, IOException, InstanceNotFoundException, ReflectionException;

    String getDefaultDomain(Subject subject) throws IOException;

    String[] getDomains(Subject subject) throws IOException;

    MBeanInfo getMBeanInfo(ObjectName objectName, Subject subject) throws IntrospectionException, IOException, InstanceNotFoundException, ReflectionException;

    boolean isInstanceOf(ObjectName objectName, String str, Subject subject) throws IOException, InstanceNotFoundException;

    void addNotificationListener(ObjectName objectName, ObjectName objectName2, MarshalledObject marshalledObject, MarshalledObject marshalledObject2, Subject subject) throws IOException, InstanceNotFoundException;

    void removeNotificationListener(ObjectName objectName, ObjectName objectName2, Subject subject) throws ListenerNotFoundException, IOException, InstanceNotFoundException;

    void removeNotificationListener(ObjectName objectName, ObjectName objectName2, MarshalledObject marshalledObject, MarshalledObject marshalledObject2, Subject subject) throws ListenerNotFoundException, IOException, InstanceNotFoundException;

    Integer[] addNotificationListeners(ObjectName[] objectNameArr, MarshalledObject[] marshalledObjectArr, Subject[] subjectArr) throws IOException, InstanceNotFoundException;

    void removeNotificationListeners(ObjectName objectName, Integer[] numArr, Subject subject) throws ListenerNotFoundException, IOException, InstanceNotFoundException;

    NotificationResult fetchNotifications(long j2, int i2, long j3) throws IOException;
}
