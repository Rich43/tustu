package java.beans;

/* loaded from: rt.jar:java/beans/Customizer.class */
public interface Customizer {
    void setObject(Object obj);

    void addPropertyChangeListener(PropertyChangeListener propertyChangeListener);

    void removePropertyChangeListener(PropertyChangeListener propertyChangeListener);
}
