package javax.swing;

import javax.swing.event.ChangeListener;

/* loaded from: rt.jar:javax/swing/SingleSelectionModel.class */
public interface SingleSelectionModel {
    int getSelectedIndex();

    void setSelectedIndex(int i2);

    void clearSelection();

    boolean isSelected();

    void addChangeListener(ChangeListener changeListener);

    void removeChangeListener(ChangeListener changeListener);
}
