package javax.management.modelmbean;

import javax.management.DynamicMBean;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.PersistentMBean;
import javax.management.RuntimeOperationsException;

/* loaded from: rt.jar:javax/management/modelmbean/ModelMBean.class */
public interface ModelMBean extends DynamicMBean, PersistentMBean, ModelMBeanNotificationBroadcaster {
    void setModelMBeanInfo(ModelMBeanInfo modelMBeanInfo) throws MBeanException, RuntimeOperationsException;

    void setManagedResource(Object obj, String str) throws InvalidTargetObjectTypeException, MBeanException, InstanceNotFoundException, RuntimeOperationsException;
}
