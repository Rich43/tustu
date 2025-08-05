package java.beans;

import java.util.EventListener;

/* loaded from: rt.jar:java/beans/VetoableChangeListener.class */
public interface VetoableChangeListener extends EventListener {
    void vetoableChange(PropertyChangeEvent propertyChangeEvent) throws PropertyVetoException;
}
