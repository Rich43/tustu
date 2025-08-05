package java.beans;

/* loaded from: rt.jar:java/beans/PropertyVetoException.class */
public class PropertyVetoException extends Exception {
    private static final long serialVersionUID = 129596057694162164L;
    private PropertyChangeEvent evt;

    public PropertyVetoException(String str, PropertyChangeEvent propertyChangeEvent) {
        super(str);
        this.evt = propertyChangeEvent;
    }

    public PropertyChangeEvent getPropertyChangeEvent() {
        return this.evt;
    }
}
