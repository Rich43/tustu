package java.beans.beancontext;

import java.util.Iterator;

/* loaded from: rt.jar:java/beans/beancontext/BeanContextServiceProvider.class */
public interface BeanContextServiceProvider {
    Object getService(BeanContextServices beanContextServices, Object obj, Class cls, Object obj2);

    void releaseService(BeanContextServices beanContextServices, Object obj, Object obj2);

    Iterator getCurrentServiceSelectors(BeanContextServices beanContextServices, Class cls);
}
