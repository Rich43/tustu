package javax.swing;

import java.awt.Component;
import java.awt.event.ActionListener;

/* loaded from: rt.jar:javax/swing/ComboBoxEditor.class */
public interface ComboBoxEditor {
    Component getEditorComponent();

    void setItem(Object obj);

    Object getItem();

    void selectAll();

    void addActionListener(ActionListener actionListener);

    void removeActionListener(ActionListener actionListener);
}
