package javax.management.modelmbean;

import javax.management.Descriptor;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.RuntimeOperationsException;

/* loaded from: rt.jar:javax/management/modelmbean/ModelMBeanInfo.class */
public interface ModelMBeanInfo {
    Descriptor[] getDescriptors(String str) throws MBeanException, RuntimeOperationsException;

    void setDescriptors(Descriptor[] descriptorArr) throws MBeanException, RuntimeOperationsException;

    Descriptor getDescriptor(String str, String str2) throws MBeanException, RuntimeOperationsException;

    void setDescriptor(Descriptor descriptor, String str) throws MBeanException, RuntimeOperationsException;

    Descriptor getMBeanDescriptor() throws MBeanException, RuntimeOperationsException;

    void setMBeanDescriptor(Descriptor descriptor) throws MBeanException, RuntimeOperationsException;

    ModelMBeanAttributeInfo getAttribute(String str) throws MBeanException, RuntimeOperationsException;

    ModelMBeanOperationInfo getOperation(String str) throws MBeanException, RuntimeOperationsException;

    ModelMBeanNotificationInfo getNotification(String str) throws MBeanException, RuntimeOperationsException;

    Object clone();

    MBeanAttributeInfo[] getAttributes();

    String getClassName();

    MBeanConstructorInfo[] getConstructors();

    String getDescription();

    MBeanNotificationInfo[] getNotifications();

    MBeanOperationInfo[] getOperations();
}
