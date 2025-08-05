package java.beans.beancontext;

import java.util.Iterator;
import java.util.TooManyListenersException;

/* loaded from: rt.jar:java/beans/beancontext/BeanContextServices.class */
public interface BeanContextServices extends BeanContext, BeanContextServicesListener {
    boolean addService(Class cls, BeanContextServiceProvider beanContextServiceProvider);

    void revokeService(Class cls, BeanContextServiceProvider beanContextServiceProvider, boolean z2);

    boolean hasService(Class cls);

    Object getService(BeanContextChild beanContextChild, Object obj, Class cls, Object obj2, BeanContextServiceRevokedListener beanContextServiceRevokedListener) throws TooManyListenersException;

    void releaseService(BeanContextChild beanContextChild, Object obj, Object obj2);

    Iterator getCurrentServiceClasses();

    Iterator getCurrentServiceSelectors(Class cls);

    void addBeanContextServicesListener(BeanContextServicesListener beanContextServicesListener);

    void removeBeanContextServicesListener(BeanContextServicesListener beanContextServicesListener);
}
