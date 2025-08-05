package javax.management.openmbean;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;

/* loaded from: rt.jar:javax/management/openmbean/OpenMBeanInfo.class */
public interface OpenMBeanInfo {
    String getClassName();

    String getDescription();

    MBeanAttributeInfo[] getAttributes();

    MBeanOperationInfo[] getOperations();

    MBeanConstructorInfo[] getConstructors();

    MBeanNotificationInfo[] getNotifications();

    boolean equals(Object obj);

    int hashCode();

    String toString();
}
