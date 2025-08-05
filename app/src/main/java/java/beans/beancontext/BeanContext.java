package java.beans.beancontext;

import java.beans.DesignMode;
import java.beans.Visibility;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;

/* loaded from: rt.jar:java/beans/beancontext/BeanContext.class */
public interface BeanContext extends BeanContextChild, Collection, DesignMode, Visibility {
    public static final Object globalHierarchyLock = new Object();

    Object instantiateChild(String str) throws IOException, ClassNotFoundException;

    InputStream getResourceAsStream(String str, BeanContextChild beanContextChild) throws IllegalArgumentException;

    URL getResource(String str, BeanContextChild beanContextChild) throws IllegalArgumentException;

    void addBeanContextMembershipListener(BeanContextMembershipListener beanContextMembershipListener);

    void removeBeanContextMembershipListener(BeanContextMembershipListener beanContextMembershipListener);
}
