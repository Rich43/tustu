package javax.management;

/* loaded from: rt.jar:javax/management/PersistentMBean.class */
public interface PersistentMBean {
    void load() throws MBeanException, InstanceNotFoundException, RuntimeOperationsException;

    void store() throws MBeanException, InstanceNotFoundException, RuntimeOperationsException;
}
