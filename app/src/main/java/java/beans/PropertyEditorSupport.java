package java.beans;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Vector;

/* loaded from: rt.jar:java/beans/PropertyEditorSupport.class */
public class PropertyEditorSupport implements PropertyEditor {
    private Object value;
    private Object source;
    private Vector<PropertyChangeListener> listeners;

    public PropertyEditorSupport() {
        setSource(this);
    }

    public PropertyEditorSupport(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        setSource(obj);
    }

    public Object getSource() {
        return this.source;
    }

    public void setSource(Object obj) {
        this.source = obj;
    }

    @Override // java.beans.PropertyEditor
    public void setValue(Object obj) {
        this.value = obj;
        firePropertyChange();
    }

    @Override // java.beans.PropertyEditor
    public Object getValue() {
        return this.value;
    }

    @Override // java.beans.PropertyEditor
    public boolean isPaintable() {
        return false;
    }

    @Override // java.beans.PropertyEditor
    public void paintValue(Graphics graphics, Rectangle rectangle) {
    }

    @Override // java.beans.PropertyEditor
    public String getJavaInitializationString() {
        return "???";
    }

    @Override // java.beans.PropertyEditor
    public String getAsText() {
        if (this.value != null) {
            return this.value.toString();
        }
        return null;
    }

    @Override // java.beans.PropertyEditor
    public void setAsText(String str) throws IllegalArgumentException {
        if (this.value instanceof String) {
            setValue(str);
            return;
        }
        throw new IllegalArgumentException(str);
    }

    @Override // java.beans.PropertyEditor
    public String[] getTags() {
        return null;
    }

    @Override // java.beans.PropertyEditor
    public Component getCustomEditor() {
        return null;
    }

    @Override // java.beans.PropertyEditor
    public boolean supportsCustomEditor() {
        return false;
    }

    @Override // java.beans.PropertyEditor
    public synchronized void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        if (this.listeners == null) {
            this.listeners = new Vector<>();
        }
        this.listeners.addElement(propertyChangeListener);
    }

    @Override // java.beans.PropertyEditor
    public synchronized void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        if (this.listeners == null) {
            return;
        }
        this.listeners.removeElement(propertyChangeListener);
    }

    public void firePropertyChange() {
        synchronized (this) {
            if (this.listeners == null) {
                return;
            }
            Vector vectorUnsafeClone = unsafeClone(this.listeners);
            PropertyChangeEvent propertyChangeEvent = new PropertyChangeEvent(this.source, null, null, null);
            for (int i2 = 0; i2 < vectorUnsafeClone.size(); i2++) {
                ((PropertyChangeListener) vectorUnsafeClone.elementAt(i2)).propertyChange(propertyChangeEvent);
            }
        }
    }

    private <T> Vector<T> unsafeClone(Vector<T> vector) {
        return (Vector) vector.clone();
    }
}
