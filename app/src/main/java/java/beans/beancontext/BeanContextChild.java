package java.beans.beancontext;

import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

/* loaded from: rt.jar:java/beans/beancontext/BeanContextChild.class */
public interface BeanContextChild {
    void setBeanContext(BeanContext beanContext) throws PropertyVetoException;

    BeanContext getBeanContext();

    void addPropertyChangeListener(String str, PropertyChangeListener propertyChangeListener);

    void removePropertyChangeListener(String str, PropertyChangeListener propertyChangeListener);

    void addVetoableChangeListener(String str, VetoableChangeListener vetoableChangeListener);

    void removeVetoableChangeListener(String str, VetoableChangeListener vetoableChangeListener);
}
