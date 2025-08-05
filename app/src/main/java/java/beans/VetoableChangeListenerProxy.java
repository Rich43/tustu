package java.beans;

import java.util.EventListenerProxy;

/* loaded from: rt.jar:java/beans/VetoableChangeListenerProxy.class */
public class VetoableChangeListenerProxy extends EventListenerProxy<VetoableChangeListener> implements VetoableChangeListener {
    private final String propertyName;

    public VetoableChangeListenerProxy(String str, VetoableChangeListener vetoableChangeListener) {
        super(vetoableChangeListener);
        this.propertyName = str;
    }

    @Override // java.beans.VetoableChangeListener
    public void vetoableChange(PropertyChangeEvent propertyChangeEvent) throws PropertyVetoException {
        getListener().vetoableChange(propertyChangeEvent);
    }

    public String getPropertyName() {
        return this.propertyName;
    }
}
