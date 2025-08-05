package java.beans;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

/* loaded from: rt.jar:java/beans/PropertyEditor.class */
public interface PropertyEditor {
    void setValue(Object obj);

    Object getValue();

    boolean isPaintable();

    void paintValue(Graphics graphics, Rectangle rectangle);

    String getJavaInitializationString();

    String getAsText();

    void setAsText(String str) throws IllegalArgumentException;

    String[] getTags();

    Component getCustomEditor();

    boolean supportsCustomEditor();

    void addPropertyChangeListener(PropertyChangeListener propertyChangeListener);

    void removePropertyChangeListener(PropertyChangeListener propertyChangeListener);
}
